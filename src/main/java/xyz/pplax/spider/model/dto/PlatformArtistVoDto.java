package xyz.pplax.spider.model.dto;

import lombok.Data;

@Data
public class PlatformArtistVoDto extends BaseDto {

    /**
     * 平台名
     */
    private String platformName;

    /**
     * 作者昵称
     */
    private String artistName;
}
