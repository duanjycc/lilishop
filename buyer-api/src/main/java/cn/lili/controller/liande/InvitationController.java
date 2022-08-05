package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.service.IUserTeamTreeService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 邀请相关
 */
@RestController
@RequestMapping("/buyer/my/invitation")
@Api(tags = "邀请人,邀请区域")
public class InvitationController {


    @Autowired
    private IUserTeamTreeService userTeamTreeService;
    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "查询是否绑定邀请人")
    @GetMapping("/checkInvitee")
    public ResultMessage<Boolean> checkInvitee() {
        AuthUser currentUser = UserContext.getCurrentUser();
        Member member = memberService.findByUsername(currentUser.getUsername());
        return ResultUtil.data(ObjectUtils.isEmpty(member.getInviteeId())? false : true);
    }

    @ApiOperation(value = "查询是否绑定邀请人")
    @GetMapping("/queryInvitationRegin")
    public ResultMessage<List> queryInvitationRegin() {
        AuthUser currentUser = UserContext.getCurrentUser();
        return ResultUtil.data(userTeamTreeService.queryInvitationRegin(currentUser.getUsername()));
    }
}
