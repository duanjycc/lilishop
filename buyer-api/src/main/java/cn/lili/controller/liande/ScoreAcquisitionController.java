/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dos.MemberIncome;
import cn.lili.modules.liande.entity.dos.ScoreAcquisition;
import cn.lili.modules.liande.service.IScoreAcquisitionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 积分获取明细 前端控制器
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
/**
 *
 */
@Api(tags = "积分")
@RestController
@RequestMapping("/buyer/score")
public class ScoreAcquisitionController {

    @Autowired
    private IScoreAcquisitionService scoreAcquisitionService;

    @ApiOperation(value = "积分")
    @GetMapping()
    public ResultMessage<IPage<ScoreAcquisition>> scoreDetails(PageVO pageVo, @RequestParam String beginDate, @RequestParam String endDate){
        return ResultUtil.data(scoreAcquisitionService.scoreDetails(pageVo, beginDate,endDate));
    }

}

