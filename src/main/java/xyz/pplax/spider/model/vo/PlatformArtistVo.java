package xyz.pplax.spider.model.vo;

import lombok.Data;

@Data
public class PlatformArtistVo {

    /**
     * 作者id
     */
    Long artistId;

    /**
     * 作者昵称
     */
    String artistName;

    /**
     * 作者在平台的主页地址
     */
    String homepageUrl;

    /**
     * 平台id
     */
    Integer platformId;

    /**
     * 作者在该平台的昵称
     */
    String artistPlatformName;

    /**
     * 该平台的名称
     */
    String platformName;

    /**
     * 该平台的地址
     */
    String platformUrl;


}
