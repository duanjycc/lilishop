package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.vos.HomeSsdCount;
import cn.lili.modules.liande.service.HomeSsdCountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/buyer/home/ssd")
@Api(tags = "首页ssd统计")
public class HomeSsdController {

    @Autowired
    private HomeSsdCountService homeSsdCountService;


    @ApiOperation(value = "首页ssd统计")
    @GetMapping
    public ResultMessage<HomeSsdCount> count() {
        return ResultUtil.data(homeSsdCountService.count());
    }
}
