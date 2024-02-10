package xyz.pplax.spider.model.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * platform
 * @author 
 */
@Data
public class Platform implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 平台名称
     */
    private String name;

    /**
     * 地址
     */
    private String url;

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