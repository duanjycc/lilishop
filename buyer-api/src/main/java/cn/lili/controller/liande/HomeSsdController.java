package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.vo.HomeSsdCountVO;
import cn.lili.modules.liande.service.IHomeSsdCountService;
import cn.lili.modules.statistics.entity.vo.WssdHisDataVO;
import cn.lili.modules.statistics.service.StoreFlowStatisticsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

/**
 * 首页ssd统计
 */
@RestController
@RequestMapping("/buyer/home/ssd")
@Api(tags = "首页ssd统计")
public class HomeSsdController {

    @Autowired
    private IHomeSsdCountService IHomeSsdCountService;

    @Autowired
    private StoreFlowStatisticsService storeFlowStatisticsService;

    @ApiOperation(value = "首页ssd统计")
    @GetMapping
    public ResultMessage<HomeSsdCountVO> count() {
        return ResultUtil.data(IHomeSsdCountService.count());
    }

    @ApiOperation(value = "行情走势")
    @GetMapping("/getSsdPriceTopData")
    public ResultMessage<List<WssdHisDataVO>> getSsdPriceTopData() {
        QueryWrapper queryWrapper = Wrappers.query(); queryWrapper.orderByDesc("date_id");
        //查询最近一个月记录
        Page page = new Page<WssdHisDataVO>(1, 30);

        List<WssdHisDataVO> topDataList = storeFlowStatisticsService.getSsdPriceTopData(page, queryWrapper);
        if (topDataList != null) {
            topDataList.sort(new Comparator<WssdHisDataVO>() {
                @Override
                public int compare(WssdHisDataVO o1, WssdHisDataVO o2) {
                    return o1.getCreateTime().compareTo(o2.getCreateTime());
                }
            });
        }

        return ResultUtil.data(topDataList);
    }

}
