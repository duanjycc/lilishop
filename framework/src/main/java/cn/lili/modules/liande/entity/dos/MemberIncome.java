/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 会员收益表
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("w_member_income")
@ApiModel(value = "MemberIncome对象", description = "会员收益表")
public class MemberIncome extends Model<MemberIncome> {

    private static final long serialVersionUID = 1L;


    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;


    /**
     * 消费会员id
     */
    @ApiModelProperty(value = "消费会员id")
    private Long consumerUserid;


    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private Long userId;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creationTime;


    /**
     * 收益数量
     */
    @ApiModelProperty(value = "收益数量")
    private Double quantity;


    /**
     * 当前收益百分比
     */
    @TableField("Income_proportion")
    @ApiModelProperty(value = "当前收益百分比")
    private String incomeProportion;


    /**
     * 做单ID
     */
    @ApiModelProperty(value = "做单ID")
    private String orderId;

    /**
     * 收益类型
     */
    @ApiModelProperty(value = "收益类型")
    private String incomeType;

}
