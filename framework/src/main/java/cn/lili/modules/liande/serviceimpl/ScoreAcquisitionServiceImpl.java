/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.DateUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.ScoreAcquisition;
import cn.lili.modules.liande.mapper.ScoreAcquisitionMapper;
import cn.lili.modules.liande.service.IScoreAcquisitionService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * <p>
 * 积分获取明细 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@Slf4j
@Service
public class ScoreAcquisitionServiceImpl extends ServiceImpl<ScoreAcquisitionMapper, ScoreAcquisition> implements IScoreAcquisitionService {

    /**
     * 积分
     *
     * @param pageVo
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public IPage<ScoreAcquisition> scoreDetails(PageVO pageVo, String beginDate, String endDate) {
        AuthUser currentUser = UserContext.getCurrentUser();
        Optional.ofNullable(currentUser).orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_LOGIN));

        QueryWrapper<ScoreAcquisition> queryWrapper = new QueryWrapper();
        queryWrapper.ge(ObjectUtils.isNotEmpty(beginDate),"create_time",beginDate + DateUtil.DATA_PREFIX);
        queryWrapper.le(ObjectUtils.isNotEmpty(endDate),"create_time",endDate + DateUtil.DATA_SUFFIX);
        queryWrapper.eq("user_id",currentUser.getId());
        queryWrapper.orderByDesc("create_time");

        return baseMapper.selectPage(PageUtil.initPage(pageVo),queryWrapper);
    }
}

