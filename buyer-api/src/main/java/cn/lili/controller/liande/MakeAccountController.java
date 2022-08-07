package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dos.MakeAccount;
import cn.lili.modules.liande.entity.dto.MakeAccountDTO;
import cn.lili.modules.liande.entity.vo.MakeAccountVO;
import cn.lili.modules.liande.service.IMakeAccountService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    private IMakeAccountService makeAccountService;

    @ApiOperation(value = "做单")
    @GetMapping("/makeAccount")
    public ResultMessage<MakeAccountVO> makeAccount(MakeAccountDTO makeAccountDTO) {
        MakeAccountVO m = makeAccountService.makeAccount(makeAccountDTO);
        return null;
    }

    @ApiOperation(value = "查询做单列表")
    @GetMapping("/queryMakeAccount")
    public ResultMessage<IPage<MakeAccount>> queryMakeAccount(PageVO page) {
        return ResultUtil.data(makeAccountService.page(PageUtil.initPage(page),new QueryWrapper<MakeAccount>().lambda().eq(MakeAccount::getMerId, UserContext.getCurrentUser().getId())));
    }
}
