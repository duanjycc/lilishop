package cn.lili.modules.liande.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 *
 */
@Data
@ApiModel(value = "后台管理，服务商业绩")
public class AchievementVO {

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "服务商等级")
    private String serviceProviderLevel;

    @ApiModelProperty(value = "签约区域名称")
    private String signAreaName;

    @ApiModelProperty(value = "上级区域名称")
    private String parentName;

    @ApiModelProperty(value = "签约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date signCreateTime;

    @ApiModelProperty(value = "店铺数量")
    private Integer storeCount;

    @ApiModelProperty(value = "销毁数量")
    private Double destroySSD;

    @ApiModelProperty(value = "让利额")
    private Double surrenderPrice;

}
