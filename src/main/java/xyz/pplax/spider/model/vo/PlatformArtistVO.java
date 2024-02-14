package xyz.pplax.spider.model.vo;

import lombok.Data;

@Data
public class PlatformArtistVO {

    /**
     * platformArtist id
     */
    private Long platformArtistId;

    /**
     * 作者id
     */
    private Long artistId;

    /**
     * 平台id
     */
    private Long platformId;

    /**
     * 作者在该平台的id
     */
    private String idInPlatform;

    /**
     * 平台名称
     */
    private String platformName;

    /**
     * 地址
     */
    private String platformUrl;

    /**
     * 作者昵称
     */
    private String platformArtistName;

    /**
     * 主页url
     */
    private String homepageUrl;

}
