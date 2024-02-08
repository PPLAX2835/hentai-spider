package xyz.pplax.spiderdemo.model.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * artist_name
 * @author 
 */
@Data
public class ArtistName implements Serializable {
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