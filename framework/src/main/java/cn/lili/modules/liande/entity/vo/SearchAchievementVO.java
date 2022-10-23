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
@ApiModel(value = "后台管理，服务商业绩查询")
public class SearchAchievementVO {

    @ApiModelProperty(value = "店铺数量")
    private Integer storeCount;

    @ApiModelProperty(value = "销毁数量")
    private Double destroySSD;

    @ApiModelProperty(value = "让利额")
    private Double surrenderPrice;

}
