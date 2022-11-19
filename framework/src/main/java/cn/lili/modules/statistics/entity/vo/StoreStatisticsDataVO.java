package cn.lili.modules.statistics.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品统计VO
 *
 * @author Bulbasaur
 * @since 2020/12/9 14:25
 */
@Data
public class StoreStatisticsDataVO {

    @ApiModelProperty(value = "店铺ID")
    private String storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "让利金额")
    private String num;

    @ApiModelProperty(value = "联系号码")
    private String phone;

    @ApiModelProperty(value = "销售金额")
    private Double price;

    @ApiModelProperty(value = "会员数")
    private String nickNum;
}
