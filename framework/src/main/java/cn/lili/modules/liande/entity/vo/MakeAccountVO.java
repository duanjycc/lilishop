package cn.lili.modules.liande.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MakeAccountVO {

    @ApiModelProperty(value = "商铺名称")
    private String merName;

    @ApiModelProperty(value = "金额")
    private Double monetary;

    @ApiModelProperty(value = "让利手机号")
    private String vipPhone;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
