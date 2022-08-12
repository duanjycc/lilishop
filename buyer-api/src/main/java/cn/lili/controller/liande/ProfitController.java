package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dos.MemberIncome;
import cn.lili.modules.liande.entity.dos.ServiceProviderIncome;
import cn.lili.modules.liande.service.IMemberIncomeService;
import cn.lili.modules.liande.service.IServiceProviderIncomeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @Autowired
    private IServiceProviderIncomeService serviceProviderIncomeService;

    @ApiOperation(value = "会员收益")
    @PostMapping("/member")
    public ResultMessage<IPage<MemberIncome>> memberDetails(PageVO pageVo, @RequestParam String beginDate, @RequestParam String endDate){
        return ResultUtil.data(memberIncomeService.memberDetails(pageVo, beginDate,endDate));
    }

    @ApiOperation(value = "区域/子区域收益")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "incomeType", value = "类型（0：区域收益，1子区域收益）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "beginDate", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String")
    })
    @PostMapping("/area")
    public ResultMessage<IPage<ServiceProviderIncome>> areaDetails(PageVO pageVo,
                                                                   @RequestParam String incomeType,
                                                                   @RequestParam String beginDate,
                                                                   @RequestParam String endDate){
        return ResultUtil.data(serviceProviderIncomeService.areaDetails(pageVo,incomeType, beginDate,endDate));
    }
}
