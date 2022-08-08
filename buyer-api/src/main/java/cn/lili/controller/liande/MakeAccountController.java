package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dto.MakeAccountDTO;
import cn.lili.modules.liande.entity.vos.MakeAccountVOS;
import cn.lili.modules.liande.service.IMakeAccountService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.vo.MemberSearchVO;
import cn.lili.modules.member.entity.vo.MemberVO;
import cn.lili.modules.permission.service.DepartmentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyer/make/account")
@Api(tags = "做单")
public class MakeAccountController {
    @Autowired
    private IMakeAccountService iMakeAccountService;

    @ApiOperation(value = "做单")
    @GetMapping("/makeAccount")
    public ResultMessage<Boolean> makeAccount(MakeAccountDTO makeAccountDTO) {

        ResultMessage<Boolean> b=iMakeAccountService.makeAccount(makeAccountDTO);
        return ResultUtil.success();
    }
}
