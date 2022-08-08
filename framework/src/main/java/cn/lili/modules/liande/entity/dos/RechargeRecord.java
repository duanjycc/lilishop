/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import cn.lili.common.security.context.UserContext;
import cn.lili.modules.liande.entity.enums.DelStatusEnum;
import cn.lili.trigger.enums.DelayTypeEnums;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 转入明细
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("w_recharge_record")
@ApiModel(value = "RechargeRecord对象", description = "转入明细")
public class RechargeRecord extends Model<RechargeRecord> {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;


    /**
     * 接收人ID
     */
    @ApiModelProperty(value = "接收人ID")
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
     * 转入时间
     */
    @ApiModelProperty(value = "转入时间")
    private Date rechargeTime;

    @ApiModelProperty(value = "状态：0已经到账，1正在转")
    private String rechargeStatus;
    /**
     * 到账时间
     */
    @ApiModelProperty(value = "到账时间")
    private Date intoTime;


    /**
     * 类型（0:外部转入，1:app内部转入，2:分配转入）
     */
    @ApiModelProperty(value = "类型（0:外部转入，1:app内部转入，2:分配转入）")
    private String type;


    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


    /**
     * 内部转账构造函数
     */
    public RechargeRecord(String acceptMemId, String intoAddress, String contractAddress, Double amount) {
        this.userId = Long.parseLong(acceptMemId);
        this.orderNo = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()));
        this.paymentAddress = UserContext.getCurrentUser().getMember().getBlockAddress();
        this.intoAddress = intoAddress;
        this.contractAddress = contractAddress;
        this.rechargeAmount = amount;
        this.arrivalAmount = amount;
        this.serviceCharge = 0.00;
        this.rechargeStatus = DelStatusEnum.USE.getType();
        this.rechargeTime =  new Date();
        this.intoTime = new Date();
        this.type = "0";
    }
}
