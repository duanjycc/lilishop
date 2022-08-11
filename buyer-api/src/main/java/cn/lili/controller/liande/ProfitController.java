package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dos.MemberIncome;
import cn.lili.modules.liande.entity.dos.RechargeRecord;
import cn.lili.modules.liande.service.IMemberIncomeService;
import cn.lili.modules.liande.serviceimpl.MemberIncomeServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Api(tags = "收益")
@RestController
@RequestMapping("/buyer/profit")
public class ProfitController {


    @Autowired
    private IMemberIncomeService memberIncomeService;

    @ApiOperation(value = "会员收益")
    @PostMapping("/member")
    public ResultMessage<IPage<MemberIncome>> memberDetails(PageVO pageVo, @RequestParam String beginDate, @RequestParam String endDate){
        return ResultUtil.data(memberIncomeService.memberDetails(pageVo, beginDate,endDate));
    }

//    @ApiOperation(value = "子区域收益")
//    @PostMapping("/child/area")
//    public ResultMessage<IPage<RechargeRecord>> transferInDetails(PageVO pageVo, @RequestParam String beginDate, @RequestParam String endDate){
//        return ResultUtil.data(transferService.transferInDetails(pageVo, beginDate,endDate));
//    }
}
