package xyz.pplax.spiderdemo.model.dto;

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
