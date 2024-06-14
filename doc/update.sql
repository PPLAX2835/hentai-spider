use spiderData;

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `base_path` varchar(512) DEFAULT '/home/' COMMENT '文件存储的基本路径',
    `max_request_fail_num` int DEFAULT 3 COMMENT '最大请求失败重试次数',
    `proxy_enable` boolean DEFAULT 0 COMMENT '是否启用代理',
    `proxy_host` varchar(32) DEFAULT '127.0.0.1' COMMENT '代理ip',
    `proxy_port` int DEFAULT '7890' COMMENT '代理端口',
    `user_agent` text COMMENT 'user-agent头',
    `furaffinity_enable_cookie` boolean DEFAULT 0 COMMENT '是否携带cookie，如果你想要furaffinity的限制内容，就要带上cookie',
    `furaffinity_cookie` text COMMENT 'furaffinity的cookie',
    `pixiv_enable_cookie` boolean DEFAULT 0 COMMENT '是否携带cookie，如果你想要pixiv的限制内容，就要带上cookie',
    `pixiv_cookie` text COMMENT 'furaffinity的cookie',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COMMENT='配置信息';
/*!40101 SET character_set_client = @saved_cs_client */;

insert into config (base_path, max_request_fail_num, proxy_enable, proxy_host, proxy_port, user_agent, furaffinity_enable_cookie, furaffinity_cookie, pixiv_enable_cookie, pixiv_cookie) VALUES ('/home', 3, 0, '127.0.0.1', '7890', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 Edg/121.0.0.0', 0, '', 0, '');
