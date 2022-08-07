package cn.lili.modules.liande.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 */
@Data
@AllArgsConstructor
public class TransferDTO {
    @ApiModelProperty(value = "接收地址")
    @NotBlank(message = "请输入接收地址或扫描接收地址二维码")
    private String acceptAddress;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "请输入验证码")
    @Length(min = 0,max = 6)
    private String verificationCode;

    @ApiModelProperty(value = "转账数量")
    @NotBlank(message = "请输入转账数量")
    @Size(min = 0,max = 99999999,message = "转账数量需大于0")
    private Double transferCount;

    @ApiModelProperty(value = "二级密码")
    @NotBlank(message = "请输入二级密码")
    private String secondPassword;

}
