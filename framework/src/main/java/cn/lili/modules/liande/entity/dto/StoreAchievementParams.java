package cn.lili.modules.liande.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 */
@Data
@ApiModel(value = "后台管理，服务商店铺业绩查询")
public class StoreAchievementParams {

    @ApiModelProperty(value = "区域id")
    private String areaId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private String endDate;

}
