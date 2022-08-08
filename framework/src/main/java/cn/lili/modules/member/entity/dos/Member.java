package cn.lili.modules.member.entity.dos;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.security.sensitive.Sensitive;
import cn.lili.common.security.sensitive.enums.SensitiveStrategy;
import cn.lili.common.utils.UuidUtils;
import cn.lili.modules.permission.entity.dos.Role;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 会员
 *
 * @author Bulbasaur
 * @since 2020-02-25 14:10:16
 */
@Data
@TableName("li_member")
@ApiModel(value = "会员")
@NoArgsConstructor
public class Member extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会员用户名")
    private String username;

    @ApiModelProperty(value = "会员密码")
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @Min(message = "会员性别参数错误", value = 0)
    @ApiModelProperty(value = "会员性别,1为男，0为女")
    private Integer sex;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "会员生日")
    private Date birthday;

    @ApiModelProperty(value = "会员地址ID")
    private String regionId;

    @ApiModelProperty(value = "会员地址")
    private String region;

    @ApiModelProperty(value = "二级密码")
    @JsonIgnore
    private String paymentPassword;

    @NotEmpty(message = "手机号码不能为空")
    @ApiModelProperty(value = "手机号码", required = true)
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String mobile;

    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(value = "积分数量")
    private Long point;

    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(value = "积分总数量")
    private Long totalPoint;

    @ApiModelProperty(value = "会员头像")
    private String face;

    @ApiModelProperty(value = "会员状态")
    private Boolean disabled;

    @ApiModelProperty(value = "是否开通店铺")
    private Boolean haveStore;

    @ApiModelProperty(value = "店铺ID")
    private String storeId;

    /**
     * @see ClientTypeEnum
     */
    @ApiModelProperty(value = "客户端")
    private String clientEnum;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后一次登录时间")
    private Date lastLoginDate;

    @ApiModelProperty(value = "会员等级ID")
    private Long gradeId;

    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(value = "经验值数量")
    private Long experience;

    @ApiModelProperty(value = "SSD卷冻结数量")
    @TableField(value = "frozen_SSD")
    private Double frozenSSD;

    @ApiModelProperty(value = "SSD卷数量")
    @TableField(value = "SSD")
    private Double SSD;
    @ApiModelProperty(value = "区块地址")
    private String blockAddress;

    @ApiModelProperty(value = "区块密钥")
    @JsonIgnore
    private String privateKey;
    @ApiModelProperty(value = "邀请人ID")
    private Long inviteeId;

    @TableField(exist = false)
    @ApiModelProperty(value = "我的区域ID")
    private String myRegionId;
    @TableField(exist = false)
    @ApiModelProperty(value = "我的区域")
    private String myRegion;
    @TableField(exist = false)
    @ApiModelProperty(value = "上级区域")
    private String myParentRegion;

    @ApiModelProperty(value = "邀请我的人")
    @TableField(exist = false)
    private String inviteeMobile;

    @ApiModelProperty(value = "角色")
    @TableField(exist = false)
    private List<Role> roles;



    public Member(String username, String password, String mobile) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.nickName = mobile;
        this.disabled = true;
        this.haveStore = false;
        this.sex = 0;
        this.point = 0L;
        this.totalPoint = 0L;
        this.blockAddress = UuidUtils.getUUID();
        this.privateKey = UuidUtils.getUUID();
        this.lastLoginDate = new Date();
    }

    public Member(String username, String password, String mobile, String nickName, String face) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.nickName = nickName;
        this.disabled = true;
        this.haveStore = false;
        this.face = face;
        this.sex = 0;
        this.point = 0L;
        this.totalPoint = 0L;
        this.blockAddress = UuidUtils.getUUID();
        this.privateKey = UuidUtils.getUUID();
        this.lastLoginDate = new Date();
    }

    public Member(String username, String password, String face, String nickName, Integer sex) {
        this.username = username;
        this.password = password;
        this.mobile = "";
        this.nickName = nickName;
        this.disabled = true;
        this.haveStore = false;
        this.face = face;
        this.sex = sex;
        this.point = 0L;
        this.totalPoint = 0L;
        this.blockAddress = UuidUtils.getUUID();
        this.privateKey = UuidUtils.getUUID();
        this.lastLoginDate = new Date();
    }
}
