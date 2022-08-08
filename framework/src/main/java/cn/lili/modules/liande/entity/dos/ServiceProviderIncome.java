/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

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
 * 服务商收益表
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("w_service_provider_income")
@ApiModel(value = "ServiceProviderIncome对象", description = "服务商收益表")
public class ServiceProviderIncome extends Model<ServiceProviderIncome> {

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
     * 收益服务商会员ID
     */
    @ApiModelProperty(value = "收益服务商会员ID")
    private Long userId;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date creationTime;


    /**
     * 收益数量
     */
    @ApiModelProperty(value = "收益数量")
    private Double quantity;


    /**
     * (0本区域收益，1子区域收益，2下下级区域收益)
     */
    @TableField("Income_type")
    @ApiModelProperty(value = "(0本区域收益，1子区域收益，2下下级区域收益)")
    private Long incomeType;


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
     * 子区域名称
     */
    @ApiModelProperty(value = "子区域名称")
    private String subArea;


    /**
     * 下下级区域名称
     */
    @ApiModelProperty(value = "下下级区域名称")
    private String lowerLevelArea;

}
