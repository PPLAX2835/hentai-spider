package xyz.pplax.spiderdemo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.pplax.spiderdemo.dao.FileDao;
import xyz.pplax.spiderdemo.model.pojo.File;
import xyz.pplax.spiderdemo.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileDao fileDao;


    @Override
    public int deleteByPrimaryKey(Integer id) {
        return fileDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(File record) {
        return fileDao.insert(record);
    }

    @Override
    public int insertSelective(File record) {
        return fileDao.insertSelective(record);
    }

    @Override
    public File selectByPrimaryKey(Integer id) {
        return fileDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(File record) {
        return fileDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(File record) {
        return fileDao.updateByPrimaryKey(record);
    }
}
