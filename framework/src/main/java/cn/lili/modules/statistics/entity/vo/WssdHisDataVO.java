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
public class WssdHisDataVO {

    @ApiModelProperty(value = "日期")
    private String dateId;

    @ApiModelProperty(value = "今日价格")
    private String numericalAlue;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
