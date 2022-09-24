/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.serviceimpl;

import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.PendingOrderForm;
import cn.lili.modules.liande.entity.dto.PendingOrderFormDTO;
import cn.lili.modules.liande.mapper.PendingOrderFormMapper;
import cn.lili.modules.liande.service.IPendingOrderFormService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 挂单表 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@Slf4j
@Service
public class PendingOrderFormServiceImpl extends ServiceImpl<PendingOrderFormMapper, PendingOrderForm> implements IPendingOrderFormService {

    @Autowired
    PendingOrderFormMapper pendingOrderFormMapper;


    @Override
    public IPage<PendingOrderForm> listOfPendingOrders(PageVO page, String sort, String business, String collation) {
        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        if (collation.equals("DESC")) {

        } else {

        }

        queryWrapper.eq("business", business);
        return pendingOrderFormMapper.listOfPendingOrders(PageUtil.initPage(page), queryWrapper);
    }

    @Override
    public PendingOrderForm pendingOrderInformation() {
        Member member = UserContext.getCurrentUser().getMember();
        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", member.getUsername());
        return pendingOrderFormMapper.selectOne(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean insertPendingOrder(PendingOrderFormDTO pendingOrderFormDTO) {
        Member member = UserContext.getCurrentUser().getMember();

        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", member.getUsername());
        PendingOrderForm pis = pendingOrderFormMapper.selectOne(queryWrapper);
        if (pis != null) {
            pendingOrderFormMapper.delete(queryWrapper);
        }

        PendingOrderForm p = new PendingOrderForm();
        p.setUsername(member.getUsername());
        p.setBusiness(pendingOrderFormDTO.getBusiness());
        p.setPrice(pendingOrderFormDTO.getPrice());
        p.setWechatNumber(pendingOrderFormDTO.getWechatNumber());
        p.setPhoneNumber(pendingOrderFormDTO.getPhoneNumber());
        p.setContacts(pendingOrderFormDTO.getContacts());
        p.setSalesVolume(pendingOrderFormDTO.getSalesVolume());
        p.setBankNo(pendingOrderFormDTO.getBankNo());
        p.setAlipayCollectionCodeUrl(pendingOrderFormDTO.getAlipayCollectionCodeUrl());
        p.setWxCollectionCodeUrl(pendingOrderFormDTO.getWxCollectionCodeUrl());
        p.setAcceptAddress(ObjectUtils.isEmpty(pendingOrderFormDTO.getAcceptAddress()) ? null : pendingOrderFormDTO.getAcceptAddress());

        return pendingOrderFormMapper.insert(p) > 0 ? true : false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updatePendingOrder(PendingOrderFormDTO pendingOrderFormDTO) {
        Member member = UserContext.getCurrentUser().getMember();
        PendingOrderForm p = new PendingOrderForm();
        p.setUsername(member.getUsername());
        p.setBusiness(pendingOrderFormDTO.getBusiness());
        p.setPrice(pendingOrderFormDTO.getPrice());
        p.setWechatNumber(pendingOrderFormDTO.getWechatNumber());
        p.setPhoneNumber(pendingOrderFormDTO.getPhoneNumber());
        p.setContacts(pendingOrderFormDTO.getContacts());
        p.setSalesVolume(pendingOrderFormDTO.getSalesVolume());
        p.setBankNo(pendingOrderFormDTO.getBankNo());
        p.setAlipayCollectionCodeUrl(pendingOrderFormDTO.getAlipayCollectionCodeUrl());
        p.setWxCollectionCodeUrl(pendingOrderFormDTO.getWxCollectionCodeUrl());
        p.setAcceptAddress(ObjectUtils.isEmpty(pendingOrderFormDTO.getAcceptAddress()) ? null : pendingOrderFormDTO.getAcceptAddress());

        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", member.getUsername());

        return pendingOrderFormMapper.update(p, queryWrapper) > 0 ? true : false;

    }

    @Override
    public Boolean deletePendingOrder() {
        Member member = UserContext.getCurrentUser().getMember();
        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", member.getUsername());
        return pendingOrderFormMapper.delete(queryWrapper) > 0 ? true : false;
    }

    @Override
    public PendingOrderForm contactInformation(PendingOrderFormDTO pendingOrderFormDTO) {
        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", pendingOrderFormDTO.getUsername());
        return pendingOrderFormMapper.selectOne(queryWrapper);
    }
}
