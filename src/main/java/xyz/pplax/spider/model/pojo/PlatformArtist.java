package xyz.pplax.spider.model.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * platform_artist
 * @author 
 */
@Data
public class PlatformArtist implements Serializable {
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
     * 作者昵称
     */
    private String name;

    /**
     * 主页url
     */
    private String homepageUrl;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 添加时间
     */
    private Date insertAt;


    /**
     * 平台实体 非字段
     */
    private Platform platform;

    private static final long serialVersionUID = 1L;
}