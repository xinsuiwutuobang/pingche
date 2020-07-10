package com.yf.pingche.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangfei
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("xcx_info")
@ApiModel(value="Info对象", description="")
public class Info implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Date date;

    private Date time;

    private String departure;

    private String destination;

    private Integer gender;

    private String name;

    private String phone;

    private String remark;

    private Integer surplus;

    private Integer type;

    private String vehicle;

    private Long uid;

    private Integer status;

    private Integer see;

    private BigDecimal price;

    private Date addtime;


}
