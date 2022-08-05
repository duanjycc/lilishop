/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.serviceimpl;

import cn.lili.modules.liande.entity.dos.UserTeamTree;
import cn.lili.modules.liande.entity.enums.DelStatusEnum;
import cn.lili.modules.liande.mapper.UserTeamTreeMapper;
import cn.lili.modules.liande.service.IUserTeamTreeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
public class UserTeamTreeServiceImpl extends ServiceImpl<UserTeamTreeMapper, UserTeamTree> implements IUserTeamTreeService {

    /**
     * 查询我邀请的人
     *
     * @param phone 登陆人手机号
     * @return
     */
    @Override
    public List<String> queryInvitationRegin(String phone) {
        UserTeamTree userTeamTree = baseMapper.selectOne(new QueryWrapper<UserTeamTree>().lambda()
                .eq(UserTeamTree::getUsername, phone)
                .eq(UserTeamTree::getIsDel, DelStatusEnum.USE.getType()));
        return Arrays.asList(userTeamTree.getTeamTree());
    }
}
