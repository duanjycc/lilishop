package cn.lili.modules.store.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 后台添加店铺信息DTO
 *
 * @author Bulbasaur
 * @since 2020/12/12 11:35
 */
@Data
public class AdminStoreApplyDTO {

    @ApiModelProperty(value = "唯一标识", hidden = true)
    private String id;

    /****店铺基本信息***/
    @ApiModelProperty(value = "会员ID")
    public String memberId;

    @NotBlank(message = "店铺名称不能为空")
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

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

    @ApiModelProperty(value = "员工总数")
    private String employeeNum;

    @ApiModelProperty(value = "注册资金")
    private Double registeredCapital;

    @ApiModelProperty(value = "联系人姓名")
    private String linkName;

    @ApiModelProperty(value = "联系人电话")
    private String linkPhone;

    @ApiModelProperty(value = "电子邮箱")
    private String companyEmail;

    @ApiModelProperty(value = "营业执照号")
    private String licenseNum;

    @ApiModelProperty(value = "法定经营范围")
    private String scope;

    @ApiModelProperty(value = "营业执照电子版")
    private String licencePhoto;

    @ApiModelProperty(value = "法人姓名")
    private String legalName;

    @ApiModelProperty(value = "法人身份证")
    private String legalId;

    @NotBlank(message = "法人身份证不能为空")
    @ApiModelProperty(value = "法人身份证照片")
    private String legalPhoto;

    @ApiModelProperty(value = "结算银行开户行名称")
    private String settlementBankAccountName;

    @ApiModelProperty(value = "结算银行开户账号")
    private String settlementBankAccountNum;

    @ApiModelProperty(value = "结算银行开户支行名称")
    private String settlementBankBranchName;

    @ApiModelProperty(value = "结算银行支行联行号")
    private String settlementBankJointName;

    @ApiModelProperty(value = "收货人姓名")
    private String salesConsigneeName;

    @ApiModelProperty(value = "收件人手机")
    private String salesConsigneeMobile;

    @ApiModelProperty(value = "地址Id， '，'分割")
    private String salesConsigneeAddressId;

    @ApiModelProperty(value = "地址名称， '，'分割")
    private String salesConsigneeAddressPath;

    @ApiModelProperty(value = "详细地址")
    private String salesConsigneeDetail;

    @ApiModelProperty(value = "同城配送达达店铺编码")
    private String ddCode;

    @ApiModelProperty(value = "结算周期")
    private String settlementCycle;

}
