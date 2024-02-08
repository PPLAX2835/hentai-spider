package xyz.pplax.spider.model.dto;

import lombok.Data;

@Data
public class BaseDto {

    /**
     * 页数
     */
    private Integer page;

    /**
     * 页长
     */
    private Integer size;

}
