package xyz.pplax.spider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.pplax.spider.dao.ConfigDao;
import xyz.pplax.spider.model.pojo.Config;
import xyz.pplax.spider.service.ConfigService;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Override
    public List<Config> listAll() {
        return configDao.selectAll();
    }

    @Override
    public int add(Config record) {
        return configDao.insert(record);
    }

    @Override
    public int addSelective(Config record) {
        return configDao.insertSelective(record);
    }

    @Override
    public int updateSelectiveById(Config record) {
        return configDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateById(Config record) {
        return configDao.updateByPrimaryKey(record);
    }

    @Override
    public int save(Config record) {
        int res = configDao.save(record);

        if (res <= 0) {
            return add(record);
        }

        return res;
    }
}
