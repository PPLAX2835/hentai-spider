package xyz.pplax.spider.service;

import org.springframework.stereotype.Service;
import xyz.pplax.spider.model.pojo.File;

@Service
public interface FileService {

    int deleteByPrimaryKey(Integer id);

    int insert(File record);

    int insertSelective(File record);

    File selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(File record);

    int updateByPrimaryKey(File record);

}
