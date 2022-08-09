package cn.lili.controller.liande;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.enums.SwitchEnum;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.vo.MemberSearchVO;
import cn.lili.modules.member.entity.vo.MemberVO;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.permission.entity.dos.Department;
import cn.lili.modules.permission.service.DepartmentService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Optional;


/**
 * 邀请相关
 */
@RestController
@RequestMapping("/buyer/my/invitation")
@Api(tags = "邀请人,邀请区域")
public class InvitationController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询是否绑定邀请人")
    @GetMapping("/checkInvitee")
    public ResultMessage<Boolean> checkInvitee() {
        AuthUser currentUser = UserContext.getCurrentUser();
        Member member = memberService.findByUsername(currentUser.getUsername());
        return ResultUtil.data(ObjectUtils.isEmpty(member.getInviteeId())? false : true);
    }

    @ApiOperation(value = "绑定邀请人")
    @GetMapping("/bind/invitee")
    public ResultMessage<Object> bindInvitee(@NotNull(message = "手机号") @RequestParam String mobile) {
        return ResultUtil.data(memberService.bindInvitee(mobile));
    }


    @ApiOperation(value = "查询我的邀请人")
    @GetMapping("/queryMyInvitee")
    public ResultMessage<Object> queryMyInvitee() {
        AuthUser currentUser = UserContext.getCurrentUser();
        Optional.ofNullable(currentUser.getMember().getInviteeId()).orElseThrow(() -> new ServiceException(ResultCode.INVITATION_NOT_EXIST_ERROR));
        MemberVO member = memberService.getMember(String.valueOf(currentUser.getMember().getInviteeId()));
        return ResultUtil.data(member);
    }

    @ApiOperation(value = "查询我的邀请人列表")
    @GetMapping("/queryInvitation")
    public ResultMessage<IPage<MemberVO>> queryInvitation(PageVO page) {
        AuthUser currentUser = UserContext.getCurrentUser();
        MemberSearchVO member = new MemberSearchVO();
        member.setInviteeId(Long.parseLong(currentUser.getId()));
        member.setDisabled(SwitchEnum.OPEN.name());
        return ResultUtil.data(memberService.getMemberPage(member,page));
    }

    /**
     * 根据缓存的部门Id 查询我邀请的区域
     * @param regionId
     * @param page
     * @return
     */
    @ApiOperation(value = "查询我邀请的区域")
    @GetMapping("/queryInvitationRegion/{regionId}")
    public ResultMessage<IPage<Department>> queryInvitationRegion(@PathVariable("regionId") String regionId, PageVO page) {
        return ResultUtil.data(departmentService.page(PageUtil.initPage(page),new QueryWrapper<Department>().lambda().eq(Department::getParentId,regionId)));
    }
}
