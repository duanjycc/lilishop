package cn.lili.modules.liande.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商铺会员管理
 */
@Data
@ApiModel(value = "商铺会员管理")
public class MemberProfitVO {


    @ApiModelProperty(value = "会员号码")
    private String username;

    @ApiModelProperty(value = "数量")
    private Double ssd;

    @ApiModelProperty(value = "积分")
    private Double point;



}
