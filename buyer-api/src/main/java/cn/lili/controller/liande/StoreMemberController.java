package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.vo.MemberProfitVO;
import cn.lili.modules.liande.entity.vo.StoreMemberTopVO;
import cn.lili.modules.member.service.MemberService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Api(tags = "商铺会员管理")
@RestController
@RequestMapping("/buyer/store/member")
public class StoreMemberController {

    @Autowired
    private MemberService memberService;


    @ApiOperation(value = "商铺会员管理")
    @GetMapping
    public ResultMessage<IPage<MemberProfitVO>> getStoreMember(String mobile, PageVO page) {
        return ResultUtil.data(memberService.getStoreMember(mobile, page));
    }

    @ApiOperation(value = "商铺会员管理-top显示")
    @GetMapping("/getStoreMember/top")
    public ResultMessage<StoreMemberTopVO> getStoreMemberTop() {
        return ResultUtil.data(memberService.getStoreMemberTop());
    }
}
