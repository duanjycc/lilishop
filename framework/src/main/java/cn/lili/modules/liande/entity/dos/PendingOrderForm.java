/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 挂单表
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pending_order_form")
@ApiModel(value = "PendingOrderForm对象", description = "挂单表")
public class PendingOrderForm extends Model<PendingOrderForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Long id;


    /**
     * 会员用户名
     */
    @ApiModelProperty(value = "会员用户名")
    private String username;


    /**
     * 0表示买1表示卖
     */
    @ApiModelProperty(value = "0表示买1表示卖")
    private String business;


    /**
     * 买卖数量
     */
    @ApiModelProperty(value = "买卖数量")
    private Double salesVolume;


    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private Double price;


    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;


    /**
     * 微信号
     */
    @ApiModelProperty(value = "微信号")
    private String wechatNumber;


    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contacts;

}
