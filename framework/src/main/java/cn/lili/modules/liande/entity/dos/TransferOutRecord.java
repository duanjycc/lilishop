/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import cn.lili.common.security.context.UserContext;
import cn.lili.modules.liande.entity.enums.DelStatusEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

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

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "转出人用户ID")
    private Long userId;

    @TableField("orderNo")
    @ApiModelProperty(value = "订单号")
    private Long orderNo;

    @ApiModelProperty(value = "付款地址")
    private String paymentAddress;

    @ApiModelProperty(value = "到账地址")
    private String intoAddress;

    @ApiModelProperty(value = "合约地址")
    private String contractAddress;

    @ApiModelProperty(value = "金额")
    private Double rechargeAmount;

    @ApiModelProperty(value = "到账金额")
    private Double arrivalAmount;

    @ApiModelProperty(value = "手续费")
    private Double serviceCharge;

    @ApiModelProperty(value = "转出时间")
    private LocalDateTime rechargeTime;

    @ApiModelProperty(value = "0已经到账，1正在转")
    private String receiptStatus;

    @ApiModelProperty(value = "到账时间")
    private LocalDateTime intoTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    // 内转
    public TransferOutRecord(Long orderNo,String intoAddress, String contractAddress, Double amount) {
        this.userId = Long.parseLong(UserContext.getCurrentUser().getMember().getId());
        this.orderNo = orderNo;
        this.paymentAddress = UserContext.getCurrentUser().getMember().getBlockAddress();
        this.intoAddress = intoAddress;
        this.contractAddress = contractAddress;
        this.rechargeAmount = amount;
        this.arrivalAmount = amount;
        this.serviceCharge = 0.00;
        this.rechargeTime = LocalDateTime.now();
        this.receiptStatus = DelStatusEnum.USE.getType();
        this.intoTime = LocalDateTime.now();
    }
}
