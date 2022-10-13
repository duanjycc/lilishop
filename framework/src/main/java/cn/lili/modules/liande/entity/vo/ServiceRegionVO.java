package cn.lili.modules.liande.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 *
 */
@AllArgsConstructor
@Data
@ApiModel(value = "后台管理，服务商区域 以及服务商上级区域ids")
public class ServiceRegionVO {

    @ApiModelProperty(value = "服务商区域ids")
    private String[] signAreaIds;

    @ApiModelProperty(value = "服务商上级区域ids")
    private String[] parentAreaIds;

    @ApiModelProperty(value = "服务商上级区域id")
    private String parentAreaId;

    @ApiModelProperty(value = "服务商等级")
    private String serviceLevel;

}
