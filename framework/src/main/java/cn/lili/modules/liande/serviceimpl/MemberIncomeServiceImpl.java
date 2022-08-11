/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.MemberIncome;
import cn.lili.modules.liande.entity.dto.QueryTransferDTO;
import cn.lili.modules.liande.mapper.MemberIncomeMapper;
import cn.lili.modules.liande.service.IMemberIncomeService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * <p>
 * 会员收益表 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Slf4j
@Service
public class MemberIncomeServiceImpl extends ServiceImpl<MemberIncomeMapper, MemberIncome> implements IMemberIncomeService {

    /**
     * 会员收益
     *
     * @param pageVo
     * @param beginDate
     * @param endDate
     */
    @Override
    public IPage<MemberIncome> memberDetails(PageVO pageVo, String beginDate, String endDate) {
        AuthUser currentUser = UserContext.getCurrentUser();
        Optional.ofNullable(currentUser).orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_LOGIN));

        QueryWrapper<MemberIncome> queryWrapper = new QueryWrapper();
        queryWrapper.ge("t.creation_time",beginDate);
        queryWrapper.le("t.creation_time",endDate);
        queryWrapper.eq("t.user_id",currentUser.getId());
//        queryWrapper.eq("t.receipt_status", DelStatusEnum.USE.getType());
        queryWrapper.orderByDesc("t.recharge_time");

        return baseMapper.memberDetails(PageUtil.initPage(pageVo),queryWrapper);
    }
}
