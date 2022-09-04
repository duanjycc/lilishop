package cn.lili.modules.store.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 后台添加店铺信息DTO
 *
 * @author Bulbasaur
 * @since 2020/12/12 11:35
 */
@Data
public class AppStoreSettleDTO {


    /****店铺基本信息***/
    @ApiModelProperty(value = "会员ID")
    public String memberId;

    @NotBlank(message = "店铺名称不能为空")
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺id")
    private String storeId;
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

    @ApiModelProperty(value = "推广员")
    private String invitationPhone;

    @NotBlank(message = "店铺简介不能为空")
    @ApiModelProperty(value = "店铺简介")
    private String storeDesc;

    @ApiModelProperty(value = "经纬度")
    private String storeCenter;

    @ApiModelProperty(value = "店铺经营类目")
    private String goodsManagementCategory;

    @ApiModelProperty(value = "是否自营")
    private Boolean selfOperated;

    @ApiModelProperty(value = "地址名称， '，'分割")
    private String storeAddressPath;

    @ApiModelProperty(value = "地址id，'，'分割 ")
    private String storeAddressIdPath;

    @ApiModelProperty(value = "详细地址")
    private String storeAddressDetail;

    /****公司基本信息***/
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "公司电话")
    private String companyPhone;

    @ApiModelProperty(value = "公司地址")
    private String companyAddress;

    @ApiModelProperty(value = "公司地址地区Id")
    private String companyAddressIdPath;

    @ApiModelProperty(value = "公司地址地区")
    private String companyAddressPath;


    @ApiModelProperty(value = "联系人姓名")
    private String linkName;

    @ApiModelProperty(value = "联系人电话")
    private String linkPhone;


    @ApiModelProperty(value = "法定经营范围")
    private String scope;

    @ApiModelProperty(value = "地址Id， '，'分割")
    private String salesConsigneeAddressId;

    @ApiModelProperty(value = "地址名称， '，'分割")
    private String salesConsigneeAddressPath;

    @ApiModelProperty(value = "详细地址")
    private String salesConsigneeDetail;



}
