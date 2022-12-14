/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 销毁明细
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("w_destroy_detail")
@ApiModel(value = "DestroyDetail对象", description = "销毁明细")
public class DestroyDetail extends Model<DestroyDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Long id;


    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;


    /**
     * 销毁时间
     */
    @ApiModelProperty(value = "销毁时间")
    private Date destroyTime;


    /**
     * 商铺名称
     */
    @ApiModelProperty(value = "商铺名称")
    private String merName;


    /**
     * 让利金额
     */
    @ApiModelProperty(value = "让利金额")
    private Double price;


    /**
     * want数量
     */
    @ApiModelProperty(value = "want数量")
    private Double wantCount;


    /**
     * 销毁状态（0:销毁成功，1:销毁失败）
     */
    @ApiModelProperty(value = "销毁状态（0:销毁成功，1:销毁失败）")
    private String status;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 做单时want单价
     */
    @ApiModelProperty(value = "做单时want单价")
    private String wantPrice;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private String storeId;

}
