package cn.lili.modules.liande.entity.vos;

import cn.lili.modules.liande.entity.dos.MakeAccount;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MakeAccountVOS {
    @Data
    @EqualsAndHashCode(callSuper = false)
    @TableName("w_make_account")
    @ApiModel(value = "MakeAccount对象", description = "做单明细表")
    public class MakeAccount extends Model<cn.lili.modules.liande.entity.dos.MakeAccount> {

        private static final long serialVersionUID = 1L;

        @TableId(value = "id", type = IdType.AUTO)
        @ApiModelProperty(value = "")
        private Long id;


        /**
         * 商铺用户id
         */
        @ApiModelProperty(value = "商铺用户id")
        private Long userId;


        /**
         * 商铺id
         */
        @ApiModelProperty(value = "商铺id")
        private String merId;


        /**
         * 商铺名称
         */
        @ApiModelProperty(value = "商铺名称")
        private String merName;


        /**
         * 消费金额
         */
        @ApiModelProperty(value = "消费金额")
        private Double monetary;


        /**
         * 商铺用户手机号
         */
        @ApiModelProperty(value = "商铺用户手机号")
        private String username;


        /**
         * 让利金额
         */
        @ApiModelProperty(value = "让利金额")
        private Double surrenderPrice;


        /**
         * 让利比例
         */
        @ApiModelProperty(value = "让利比例")
        private Double surrenderRatio;


        /**
         * 会员手机号
         */
        @ApiModelProperty(value = "会员手机号")
        private String vipPhone;


        /**
         * 是否生效（0:生效，1:失效）
         */
        @ApiModelProperty(value = "是否生效（0:生效，1:失效）")
        private String isUse;


        /**
         * 创建时间
         */
        @ApiModelProperty(value = "创建时间")
        private LocalDateTime createTime;


        /**
         * 用户获得算力
         */
        @ApiModelProperty(value = "用户获得算力")
        private Double userReturnPower;


        /**
         * 商户获得算力
         */
        @ApiModelProperty(value = "商户获得算力")
        private Double shReturnPower;


        /**
         * 做单时want单价
         */
        @ApiModelProperty(value = "做单时want单价")
        private String wantPrice;
    }
}
