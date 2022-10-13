package cn.lili.modules.liande.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 *
 */
@Data
@ApiModel(value = "后台管理，服务商管理签约")
public class SignInDTO {

    @ApiModelProperty(value = "签约区域")
    @NotBlank(message = "签约区域不能为空")
    private String signAreaId;

    private String[] signAreaIds;

    @ApiModelProperty(value = "签约区域名称")
    private String signAreaName;

    @ApiModelProperty(value = "上级服务商")
    @NotBlank(message = "上级服务商不能为空")
    private String parentServiceProvider;

    private String[] parentAreaIds;

    @ApiModelProperty(value = "会员手机号")
    @Length(min= 11 ,max = 11,message = "会员手机号只能11位")
    private String mobile;

    @ApiModelProperty(value = "联系人")
    @NotBlank(message = "联系人不能为空")
    private String username;

    @ApiModelProperty(value = "服务商等级")
    @NotBlank(message = "服务商等级不能为空")
    private String serviceProviderLevel;

}
