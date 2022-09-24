/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dto;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 挂单表
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@Data
@ApiModel( description = "挂单表")
public class PendingOrderFormDTO extends Model<PendingOrderFormDTO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "会员用户名")
    private String username;

    @ApiModelProperty(value = "0表示买1表示卖")
    private String business;

    @ApiModelProperty(value = "买卖数量")
    private Double salesVolume;

    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "微信号")
    private String wechatNumber;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "接受地址")
    private String acceptAddress;
    @ApiModelProperty(value = "银行卡号")
    private String bankNo;
    @ApiModelProperty(value = "支付宝收款二维码")
    private String alipayCollectionCodeUrl;
    @ApiModelProperty(value = "微信收款二维码")
    private String wxCollectionCodeUrl;

}
