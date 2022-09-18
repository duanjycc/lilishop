package cn.lili.modules.liande.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@Data
@ApiModel(value = "后台管理，服务商管理查询")
public class ServiceProviderParams {

    @ApiModelProperty(value = "区域id")
    private String areaId;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "状态( 0: 签约 ,1: 未签约) ",allowableValues = "0,1")
    private String isSignIn;

}
