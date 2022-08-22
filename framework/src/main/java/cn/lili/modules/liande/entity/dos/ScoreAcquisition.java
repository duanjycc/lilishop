/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 积分获取明细
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("score_acquisition")
@ApiModel(value = "ScoreAcquisition对象", description = "积分获取明细")
public class ScoreAcquisition extends Model<ScoreAcquisition> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Long id;


    /**
     * 获取积分的用户id
     */
    @ApiModelProperty(value = "获取积分的用户id")
    private String userId;


    /**
     * 做单的商铺名称
     */
    @ApiModelProperty(value = "做单的商铺名称")
    private String merName;


    /**
     * 做单商铺的ID
     */
    @ApiModelProperty(value = "做单商铺的ID")
    private String merId;


    /**
     * 积分获取的时间
     */
    @ApiModelProperty(value = "积分获取的时间")
    private LocalDateTime createTime;


    /**
     * 做单的ID
     */
    @ApiModelProperty(value = "做单的ID")
    private Long orderId;


    /**
     * 积分获取类型 0是会员积分1是商铺做单积分
     */
    @ApiModelProperty(value = "积分获取类型 0是会员积分1是商铺做单积分")
    private Long integralType;

}
