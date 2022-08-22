/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.service;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.ScoreAcquisition;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 积分获取明细 服务类
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
public interface IScoreAcquisitionService extends IService<ScoreAcquisition> {


    /**
     * 积分
     * @param pageVo
     * @param beginDate
     * @param endDate
     * @return
     */
    IPage<ScoreAcquisition> scoreDetails(PageVO pageVo,  String beginDate, String endDate);
}
