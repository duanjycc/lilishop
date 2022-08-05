/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 转出明细
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_transfer_out_record")
@ApiModel(value = "TransferOutRecord对象", description = "转出明细")
public class TransferOutRecord extends Model<TransferOutRecord> {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;


    /**
     * 转出人用户ID
     */
    @ApiModelProperty(value = "转出人用户ID")
    private Long userId;


    /**
     * 订单号
     */
    @TableField("orderNo")
    @ApiModelProperty(value = "订单号")
    private Long orderNo;


    /**
     * 付款地址
     */
    @ApiModelProperty(value = "付款地址")
    private String paymentAddress;


    /**
     * 到账地址
     */
    @ApiModelProperty(value = "到账地址")
    private String intoAddress;


    /**
     * 合约地址
     */
    @ApiModelProperty(value = "合约地址")
    private String contractAddress;


    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private Double rechargeAmount;


    /**
     * 到账金额
     */
    @ApiModelProperty(value = "到账金额")
    private Double arrivalAmount;


    /**
     * 手续费
     */
    @ApiModelProperty(value = "手续费")
    private Double serviceCharge;


    /**
     * 转出时间
     */
    @ApiModelProperty(value = "转出时间")
    private LocalDateTime rechargeTime;


    /**
     * 0已经到账，1正在转
     */
    @ApiModelProperty(value = "0已经到账，1正在转")
    private String receiptStatus;


    /**
     * 到账时间
     */
    @ApiModelProperty(value = "到账时间")
    private LocalDateTime intoTime;


    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
