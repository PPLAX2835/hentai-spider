package xyz.pplax.spider.model.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * artist
 * @author 
 */
@Data
public class Artist implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 作者昵称
     */
    private String name;

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