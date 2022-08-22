/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.serviceimpl;

import cn.hutool.db.Page;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.PendingOrderForm;
import cn.lili.modules.liande.entity.dto.PendingOrderFormDTO;
import cn.lili.modules.liande.entity.dto.QueryTransferDTO;
import cn.lili.modules.liande.entity.vo.PendingOrderFormVO;
import cn.lili.modules.liande.mapper.PendingOrderFormMapper;
import cn.lili.modules.liande.service.IPendingOrderFormService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

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
    public IPage<PendingOrderForm> listOfPendingOrders(PageVO page,String sort,String business) {
        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc(sort);
        queryWrapper.eq("business",business);
        return pendingOrderFormMapper.listOfPendingOrders(PageUtil.initPage(page),queryWrapper);
    }

    @Override
    public PendingOrderForm pendingOrderInformation() {
        Member member = UserContext.getCurrentUser().getMember();
        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",member.getUsername());
        return pendingOrderFormMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean insertPendingOrder(PendingOrderFormDTO pendingOrderFormDTO) {
        Member member = UserContext.getCurrentUser().getMember();
        PendingOrderForm p=new PendingOrderForm();
        p.setUsername(member.getUsername());
        p.setBusiness(pendingOrderFormDTO.getBusiness());
        p.setPrice(pendingOrderFormDTO.getPrice());
        p.setWechatNumber(pendingOrderFormDTO.getWechatNumber());
        p.setPhoneNumber(pendingOrderFormDTO.getPhoneNumber());
        p.setContacts(pendingOrderFormDTO.getContacts());
        p.setSalesVolume(pendingOrderFormDTO.getSalesVolume());
        int i=pendingOrderFormMapper.insert(p);
        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",member.getUsername());
        Boolean r=false;
        if(i>0){
           r= true;
        }
        return r;
    }

    @Override
    public Boolean updatePendingOrder(PendingOrderFormDTO pendingOrderFormDTO) {
        Member member = UserContext.getCurrentUser().getMember();
        PendingOrderForm p=new PendingOrderForm();
        p.setUsername(member.getUsername());
        p.setBusiness(pendingOrderFormDTO.getBusiness());
        p.setPrice(pendingOrderFormDTO.getPrice());
        p.setWechatNumber(pendingOrderFormDTO.getWechatNumber());
        p.setPhoneNumber(pendingOrderFormDTO.getPhoneNumber());
        p.setContacts(pendingOrderFormDTO.getContacts());
        p.setSalesVolume(pendingOrderFormDTO.getSalesVolume());

        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",member.getUsername());

        int i=pendingOrderFormMapper.update(p,queryWrapper);

        Boolean r=false;
        if(i>0){
            r= true;
        }
        return r;
    }

    @Override
    public Boolean deletePendingOrder() {

        Member member = UserContext.getCurrentUser().getMember();
        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",member.getUsername());
        int i=pendingOrderFormMapper.delete(queryWrapper);

        Boolean r=false;
        if(i>0){
            r= true;
        }
        return r;
    }

    @Override
    public PendingOrderForm contactInformation(PendingOrderFormDTO pendingOrderFormDTO) {
        QueryWrapper<PendingOrderForm> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",pendingOrderFormDTO.getUsername());
        return pendingOrderFormMapper.selectOne(queryWrapper);
    }
}
