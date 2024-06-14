package xyz.pplax.spider.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import xyz.pplax.spider.dao.ConfigDao;
import xyz.pplax.spider.model.pojo.Config;

import java.util.List;

@Component
public class DBConfigBean {

    @Autowired
    private ConfigDao configDao;

    @Bean
    public Config getSystemConfig() {
        List<Config> configList = configDao.selectAll();

        if (configList.size() > 0) {
            return configList.get(0);
        }

        return null;
    }

}
