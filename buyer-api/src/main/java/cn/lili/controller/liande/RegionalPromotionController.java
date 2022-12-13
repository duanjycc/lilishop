package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dos.RegionalPromotion;
import cn.lili.modules.liande.entity.dto.RegionalPromotionDTO;
import cn.lili.modules.liande.entity.dto.ServiceProviderParams;
import cn.lili.modules.liande.entity.vo.ServiceProviderParamsVO;
import cn.lili.modules.liande.service.IRegionalPromotionService;
import cn.lili.modules.permission.service.AdminUserService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyer/regional")
@Api(tags = "区域推广员")
public class RegionalPromotionController {
    @Autowired
    private IRegionalPromotionService regionalPromotionService;

    @Autowired
    private AdminUserService userService;

    @ApiOperation(value = "区域推广员列表")
    @GetMapping("/listOfPromoters")
    public  ResultMessage<IPage<RegionalPromotion>> listOfPromoters(PageVO page) {

        return ResultUtil.data(regionalPromotionService.listOfPromoters(page));
    }

    @ApiOperation(value = "区域推广员添加")
    @GetMapping("/addOfPromoters")
    public ResultMessage<Object> addOfPromoters(RegionalPromotionDTO regionalPromotionDTO) {
        return regionalPromotionService.addOfPromoters(regionalPromotionDTO);
    }

    @ApiOperation(value = "区域推广员修改")
    @GetMapping("/updateOfPromoters")
    public ResultMessage<Object> updateOfPromoters(RegionalPromotionDTO regionalPromotionDTO) {
        return regionalPromotionService.updateOfPromoters(regionalPromotionDTO);
    }

    @ApiOperation(value = "区域推广员删除")
    @GetMapping("/deleteOfPromoters")
    public ResultMessage<Object> deleteOfPromoters(RegionalPromotionDTO regionalPromotionDTO) {
        return regionalPromotionService.deleteOfPromoters(regionalPromotionDTO);
    }

    @ApiOperation(value = "区域代理")
    @GetMapping("/serviceProvider")
    public ResultMessage<IPage<ServiceProviderParamsVO>> queryServiceProvider(ServiceProviderParams params,
                                                                              PageVO pageVo) {
        IPage<ServiceProviderParamsVO> page = userService.queryServiceProvider(PageUtil.initPage(pageVo), params);
        return ResultUtil.data(page);
    }


}
