package cn.lili.modules.liande.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 首页ssd销毁总量,昨日销毁量
 */
@Data
@AllArgsConstructor
@ApiModel(value = "商铺会员top显示")
public class StoreMemberTopVO {

    @ApiModelProperty(value = "总积分")
    private Double sumProfit;
    @ApiModelProperty(value = "总会员数")
    private Integer memberCount;
}
