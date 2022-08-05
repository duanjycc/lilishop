package cn.lili.modules.liande.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_user_team_tree")
@ApiModel(value = "UserTeamTree对象", description = "")
public class UserTeamTree {

    @ApiModelProperty(value = "用户名称")
    private String  username;
    @ApiModelProperty(value = "我的邀请的人")
    private String  myTeam;
    @ApiModelProperty(value = "所属团队树")
    private String  teamTree;
    @ApiModelProperty(value = "是否删除")
    private Integer isDel;
    @ApiModelProperty(value = "邀请我的人")
    private String  myInvitee;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
}
