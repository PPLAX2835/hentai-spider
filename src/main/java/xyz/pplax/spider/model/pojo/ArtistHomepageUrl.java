package xyz.pplax.spider.model.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * artist_homepage_url
 * @author 
 */
@Data
public class ArtistHomepageUrl implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 作者id
     */
    private Long artistId;

    /**
     * 平台id
     */
    private Integer platformId;

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

    private static final long serialVersionUID = 1L;
}