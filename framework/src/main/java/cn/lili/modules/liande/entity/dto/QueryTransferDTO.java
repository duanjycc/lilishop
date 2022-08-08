package cn.lili.modules.liande.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 *
 */
@Data
@AllArgsConstructor
public class QueryTransferDTO {

    @ApiModelProperty(value = "查询开始日期")
    private String beginDate;

    @ApiModelProperty(value = "查询结束日期")
    private String endDate;

    @ApiModelProperty(hidden = true)
    private String userId;

}
