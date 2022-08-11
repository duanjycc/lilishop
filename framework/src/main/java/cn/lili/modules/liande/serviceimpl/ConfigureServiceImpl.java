/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.serviceimpl;

import cn.lili.modules.liande.entity.dos.Configure;
import cn.lili.modules.liande.mapper.ConfigureMapper;
import cn.lili.modules.liande.service.IConfigureService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.mapper.MemberMapper;
import cn.lili.modules.member.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Slf4j
@Service
public class ConfigureServiceImpl extends ServiceImpl<ConfigureMapper, Configure> implements IConfigureService {


    @Autowired
    private MemberMapper memberMapper;

    /**
     * 获取SSD单价
     * @return
     */
    @Override
    public Object queryConfigureByType(String type,String blockAddress) {
        Configure configure = baseMapper.selectOne(new QueryWrapper<Configure>().lambda().eq(Configure::getType, type));
        if (ObjectUtils.isNotEmpty(blockAddress)){
            Member member = queryMember(blockAddress);
            return ObjectUtils.isEmpty(member) ? 0 : configure.getNumericalAlue();
        }else {
            return configure.getNumericalAlue();
        }
    }

    private Member queryMember(String address) {
        return memberMapper.selectOne(new QueryWrapper<Member>().lambda().eq(Member::getBlockAddress, address)
                .eq(Member::getDeleteFlag, false));
    }
}
