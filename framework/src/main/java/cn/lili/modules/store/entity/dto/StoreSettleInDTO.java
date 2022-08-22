package cn.lili.modules.store.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *
 */
@Data
public class StoreSettleInDTO {

    @Size(min = 2, max = 200)
    @NotBlank(message = "店铺名称不能为空")
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @NotBlank(message = "店铺经营类目不能为空")
    @ApiModelProperty(value = "店铺经营类目")
    private String scopeIds;

    @ApiModelProperty(value = "经纬度")
    @NotEmpty
    private String storeCenter;

    @Size(min = 6, max = 200, message = "店铺简介需在6-200字符之间")
    @NotBlank(message = "店铺简介不能为空")
    @ApiModelProperty(value = "店铺简介")
    private String storeDesc;

    @NotBlank(message = "店铺地址不能为空")
    @ApiModelProperty(value = "地址名称， '，'分割")
    private String storeAddressPath;


    @NotBlank(message = "地址ID不能为空")
    @ApiModelProperty(value = "地址id，'，'分割 ")
    private String storeAddressIdPath;

    @NotBlank(message = "地址详情")
    @ApiModelProperty(value = "地址详情")
    private String storeAddressDetail;

    @NotBlank(message = "店铺图片")
    @ApiModelProperty(value = "店铺图片")
    private String images;

    @NotBlank(message = "推荐人")
    @ApiModelProperty(value = "推荐人")
    private String recommenderId;
}
