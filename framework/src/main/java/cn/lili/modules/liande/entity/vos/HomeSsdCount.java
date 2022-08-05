package cn.lili.modules.liande.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 首页ssd销毁总量,昨日销毁量
 */
@Data
@ApiModel(value = "首页ssd销毁总量,昨日销毁量")
public class HomeSsdCount {
    @ApiModelProperty(value = "销毁总量")
    private Double sum;
    @ApiModelProperty(value = "昨日销毁量")
    private Double yesterdayCount;
}
