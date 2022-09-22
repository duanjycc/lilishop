package cn.lili.modules.liande.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@Data
@ApiModel(value = "后台管理，服务商店铺业绩返回")
public class StoreAchievementParamsVO {

    @ApiModelProperty(value = "店主")
    private String memberName;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "让利总金额")
    private Double surrenderPrice;
    @ApiModelProperty(value = "做单总数")
    private Double makeCount;
    @ApiModelProperty(value = "销毁总数")
    private Double destroyCount;

}
