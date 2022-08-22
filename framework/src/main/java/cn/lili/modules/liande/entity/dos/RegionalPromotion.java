/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 区域推广员表
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("regional_promotion")
@ApiModel(value = "RegionalPromotion对象", description = "区域推广员表")
public class RegionalPromotion extends Model<RegionalPromotion> {

    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Integer id;


    /**
     * 区域推广用户id
     */
    @ApiModelProperty(value = "区域推广用户id")
    private Long userId;


    /**
     * 区域推广用户手机号
     */
    @ApiModelProperty(value = "区域推广用户手机号")
    private String userName;


    /**
     * 区域ID
     */
    @ApiModelProperty(value = "区域ID")
    private String areaCode;


    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String areaName;


    /**
     * 推广员名称
     */
    @ApiModelProperty(value = "推广员名称")
    private String name;


    /**
     * 收入比列
     */
    @ApiModelProperty(value = "收入比列")
    private Integer incomeComparison;

}
