package xyz.pplax.spider.model.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * file
 * @author 
 */
@Data
public class File implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 作者id
     */
    private Long artistId;

    /**
     * 平台id
     */
    private Long platformId;

    /**
     * 在该平台的id
     */
    private String idInPlatform;

    /**
     * 文件url
     */
    private String fileUrl;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件的存储地址
     */
    private String filePath;

    /**
     * 文件的主页
     */
    private String pageUrl;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 添加时间
     */
    private Date insertAt;

    private static final long serialVersionUID = 1L;
}