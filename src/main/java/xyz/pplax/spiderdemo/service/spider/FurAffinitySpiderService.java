package xyz.pplax.spiderdemo.service.spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import xyz.pplax.spiderdemo.model.pojo.File;
import xyz.pplax.spiderdemo.model.vo.PlatformArtistVo;
import xyz.pplax.spiderdemo.spiders.FurAffinitySpider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class FurAffinitySpiderService {

    private final Executor executor;
    private final FurAffinitySpider furAffinitySpider;

    @Autowired
    public FurAffinitySpiderService(@Qualifier("threadPoolTaskExecutor") Executor executor, FurAffinitySpider furAffinitySpider) {
        this.executor = executor;
        this.furAffinitySpider = furAffinitySpider;
    }

    /**
     * 根据作者名获得所有ids 非异步
     * @param artistName
     * @return
     */
    public CompletableFuture<List<String>> getArtistIdList(String artistName) {
        return getArtistIdListAsync(artistName, 1);
    }
    private CompletableFuture<List<String>> getArtistIdListAsync(String artistName, int page) {
        CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() ->
                furAffinitySpider.getArtistIdList(artistName, page), executor);

        // 在当前异步任务完成时，递归调用
        return future.thenCompose(ids -> {
            // 如果返回的List为空，返回一个已完成的CompletableFuture
            if (ids.isEmpty()) {
                return CompletableFuture.completedFuture(ids);
            } else {
                // 递归调用
                return getArtistIdListAsync(artistName, page + 1)
                        .thenApply(nextIds -> {
                            ids.addAll(nextIds);
                            return ids;
                        });
            }
        });
    }

    /**
     * 获得指定页数范围的ids
     * @param artistName
     * @param beginPage
     * @param endPage
     * @return
     */
    public CompletableFuture<List<String>> getArtistIdListAsync(String artistName, Integer beginPage, Integer endPage) {
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();

        for (int i = beginPage; i <= endPage; i++) {
            int finalI = i;
            CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> {
                return FurAffinitySpider.getArtistIdList(artistName, finalI);
            }, executor);

            futures.add(future);
        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 当所有异步任务完成后，将它们的结果合并成一个List<String>并返回
        return allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join) // 获取各异步任务的结果
                        .flatMap(List::stream) // 合并结果
                        .collect(Collectors.toList()) // 将结果收集为List
        );
    }


    /**
     * 根据作者和指定页获得ids 异步
     * @param artistName
     * @param page
     * @return
     */
    public CompletableFuture<List<String>> getArtistIdListAsync(String artistName, Integer page) {
        return CompletableFuture.supplyAsync(() -> FurAffinitySpider.getArtistIdList(artistName, page), executor);
    }

    /**
     * 根据ids获得File实例的列表（部分封装）
     *
     * @param ids
     * @return
     */
    public CompletableFuture<List<File>> getFileListByIds(List<String> ids, PlatformArtistVo platformArtistVo) {
        List<CompletableFuture<File>> files = new ArrayList<>();

        for (String sid : ids) {
            CompletableFuture<File> future = CompletableFuture.supplyAsync(() -> {
                String fileUrl = FurAffinitySpider.getFileUrl(sid);

                File file = new File();
                file.setArtistId(platformArtistVo.getArtistId());
                file.setFileUrl(fileUrl);
                file.setFileName(platformArtistVo.getArtistName() + "-" + UUID.randomUUID().toString().replace("-", ""));
                file.setPlatformId(platformArtistVo.getPlatformId());
                file.setIdInPlatform(sid);
                file.setPageUrl("https://www.furaffinity.net/view/" + sid + "/");
                file.setFileType(fileUrl.substring(fileUrl.lastIndexOf('.') + 1));
                file.setInsertAt(new Date());
                file.setUpdateAt(new Date());

                return file;
            }, executor);

            files.add(future);
        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(files.toArray(new CompletableFuture[0]));

        // 当所有异步任务完成后，将它们的结果合并成一个List<String>并返回
        return allOf.thenApply(v ->
                files.stream()
                        .map(CompletableFuture::join) // 获取各异步任务的结果
                        .collect(Collectors.toList()) // 将结果收集为List
        );
    }



}
