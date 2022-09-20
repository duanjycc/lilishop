package cn.lili.modules.liande.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 */
@Data
@ApiModel(value = "后台管理，服务商管理")
public class ServiceProviderParamsVO {

    @ApiModelProperty(value = "区域id")
    private String areaId;
    @ApiModelProperty(value = "区域名称")
    private String areaName;
    @ApiModelProperty(value = "上级区域名称")
    private String parentName;
    @ApiModelProperty(value = "区域服务商号码")
    private String areaServiceProviderMobile;
    @ApiModelProperty(value = "服务商联系人名称")
    private String areaServiceProviderName;
    @ApiModelProperty(value = "签约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date signCreateTime;
    @ApiModelProperty(value = "状态( 0: 签约 ,1: 未签约) ",allowableValues = "0,1")
    private String isSignIn;
}
