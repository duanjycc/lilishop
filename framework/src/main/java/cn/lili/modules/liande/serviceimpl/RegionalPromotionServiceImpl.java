/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.ObjectUtil;
import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dos.RegionalPromotion;
import cn.lili.modules.liande.entity.dto.RegionalPromotionDTO;
import cn.lili.modules.liande.entity.vo.InvitationUser;
import cn.lili.modules.liande.mapper.RegionalPromotionMapper;
import cn.lili.modules.liande.service.IRegionalPromotionService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.mapper.MemberMapper;
import cn.lili.modules.permission.entity.dos.AdminUser;
import cn.lili.modules.permission.entity.dos.Department;
import cn.lili.modules.permission.mapper.AdminUserMapper;
import cn.lili.modules.permission.mapper.DepartmentMapper;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 区域推广员表 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@Slf4j
@Service
public class RegionalPromotionServiceImpl extends ServiceImpl<RegionalPromotionMapper, RegionalPromotion> implements IRegionalPromotionService {

    @Autowired
    RegionalPromotionMapper regionalPromotionMapper;
    @Autowired
    AdminUserMapper adminUserMapper;
    @Autowired
    DepartmentMapper separtmentMapper;

    @Autowired
    private MemberMapper memberMapper;


    /**
     * 检测是否区域推广员
     *
     * @param mobile
     * @return
     */
    @Override
    public boolean checkPromoters(String mobile) {
        RegionalPromotion regionalPromotion = baseMapper.selectOne(new QueryWrapper<RegionalPromotion>().lambda()
                .eq(RegionalPromotion::getUserName, mobile));
        return ObjectUtils.isEmpty(regionalPromotion) ? false : true ;
    }

    /**
     * 根据所选区域查询邀请人
     *
     * @param regionCode
     * @return
     */
    @Override
    public List<InvitationUser> queryInvitationUser(String regionCode) {
        List<InvitationUser> invitationUsers = new ArrayList<>();
        List<RegionalPromotion> list = baseMapper.selectList(new QueryWrapper<RegionalPromotion>().lambda()
                .eq(StringUtils.isNotEmpty(regionCode), RegionalPromotion::getAreaCode, regionCode));

        list.stream().forEach(s -> {
            InvitationUser user = new InvitationUser(s.getName() + "     " + s.getUserName(), s.getUserName());
            invitationUsers.add(user);
        });
        return invitationUsers;
    }

    @Override
    public IPage<RegionalPromotion> listOfPromoters(PageVO page) {
        //查出当前登陆人属于哪个区域
        Member member = UserContext.getCurrentUser().getMember();

        //根据手机号码查询部门
        QueryWrapper<AdminUser> adminUserQueryWrapper = new QueryWrapper();
        adminUserQueryWrapper.eq("username", member.getUsername());
        AdminUser adminUser = adminUserMapper.selectOne(adminUserQueryWrapper);
        if (adminUser != null) {
            if (adminUser.getDepartmentId() != null) {
                //查找当前登陆人的部门
                QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper();
                departmentQueryWrapper.eq("id", adminUser.getDepartmentId());
                Department department = separtmentMapper.selectOne(departmentQueryWrapper);

                QueryWrapper<RegionalPromotion> queryWrapper = new QueryWrapper();
                queryWrapper.eq("area_code", department.getAreaCode());

                return regionalPromotionMapper.listOfPromoters(PageUtil.initPage(page), queryWrapper);
            }

        }


        return null;
    }

    @Override
    public ResultMessage<Object> addOfPromoters(RegionalPromotionDTO regionalPromotionDTO) {
        //查出当前登陆人属于哪个区域
        Member member = UserContext.getCurrentUser().getMember();
        QueryWrapper<Member> userMemberWrapper = new QueryWrapper();
        userMemberWrapper.eq("username", regionalPromotionDTO.getUserName());
        Member userMember = memberMapper.selectOne(userMemberWrapper);


        if (regionalPromotionDTO.getIncomeComparison() >= 13) {
            return ResultUtil.error(ResultCode.PARAMS_ERROR);
        }

        if (userMember != null) {
            //根据手机号码查询部门
            QueryWrapper<AdminUser> adminUserQueryWrapper = new QueryWrapper();
            adminUserQueryWrapper.eq("username", member.getUsername());
            AdminUser adminUser = adminUserMapper.selectOne(adminUserQueryWrapper);
            if (adminUser != null) {
                if (adminUser.getDepartmentId() != null) {

                    //查找当前登陆人的部门
                    QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper();
                    departmentQueryWrapper.eq("id", adminUser.getDepartmentId());
                    Department department = separtmentMapper.selectOne(departmentQueryWrapper);

                    QueryWrapper<RegionalPromotion> rgQueryWrapper = new QueryWrapper();
                    rgQueryWrapper.eq("user_name", regionalPromotionDTO.getUserName());
                    rgQueryWrapper.eq("area_code", department.getAreaCode());
                    RegionalPromotion rp = regionalPromotionMapper.selectOne(rgQueryWrapper);
                    if (rp != null) {
                        return ResultUtil.error(ResultCode.DISTRIBUTION_ERRORW);
                    }

                    RegionalPromotion reg = new RegionalPromotion();
                    reg.setUserId(userMember.getId());
                    reg.setAreaCode(department.getAreaCode());
                    reg.setAreaName(department.getTitle());
                    reg.setName(regionalPromotionDTO.getName());
                    reg.setUserName(regionalPromotionDTO.getUserName());
                    reg.setIncomeComparison(regionalPromotionDTO.getIncomeComparison());
                    regionalPromotionMapper.insert(reg);

                }

            }
        } else {
            return ResultUtil.error(ResultCode.INVITATION_MEMBER_NOT_EXIST_ERROR);
        }


        return ResultUtil.success();
    }

    @Override
    public ResultMessage<Object> updateOfPromoters(RegionalPromotionDTO regionalPromotionDTO) {
        //查出当前登陆人属于哪个区域
        Member member = UserContext.getCurrentUser().getMember();
        QueryWrapper<Member> userMemberWrapper = new QueryWrapper();
        userMemberWrapper.eq("username", regionalPromotionDTO.getUserName());
        Member userMember = memberMapper.selectOne(userMemberWrapper);
        if (regionalPromotionDTO.getIncomeComparison() >= 13) {
            return ResultUtil.error(ResultCode.PARAMS_ERROR);
        }
        if (userMember != null) {
            //根据手机号码查询部门
            QueryWrapper<AdminUser> adminUserQueryWrapper = new QueryWrapper();
            adminUserQueryWrapper.eq("username", member.getUsername());
            AdminUser adminUser = adminUserMapper.selectOne(adminUserQueryWrapper);
            if (adminUser != null) {
                if (adminUser.getDepartmentId() != null) {
                    //查找当前登陆人的部门
                    QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper();
                    departmentQueryWrapper.eq("id", adminUser.getDepartmentId());
                    Department department = separtmentMapper.selectOne(departmentQueryWrapper);

                    QueryWrapper<RegionalPromotion> rgQueryWrapper = new QueryWrapper();
                    rgQueryWrapper.eq("user_name", regionalPromotionDTO.getUserName());

                    RegionalPromotion reg = new RegionalPromotion();
                    reg.setUserId(userMember.getId());
                    reg.setAreaCode(department.getAreaCode());
                    reg.setAreaName(department.getTitle());
                    reg.setName(regionalPromotionDTO.getName());
                    reg.setUserName(regionalPromotionDTO.getUserName());
                    reg.setIncomeComparison(regionalPromotionDTO.getIncomeComparison());


                    regionalPromotionMapper.update(reg, rgQueryWrapper);

                }
            } else {
                return ResultUtil.error(ResultCode.INVITATION_MEMBER_NOT_EXIST_ERROR);
            }

        }
        return ResultUtil.success();
    }

    @Override
    public ResultMessage<Object> deleteOfPromoters(RegionalPromotionDTO regionalPromotionDTO) {
        //查出当前登陆人属于哪个区域
        Member member = UserContext.getCurrentUser().getMember();
        QueryWrapper<Member> userMemberWrapper = new QueryWrapper();
        userMemberWrapper.eq("username", regionalPromotionDTO.getUserName());
        Member userMember = memberMapper.selectOne(userMemberWrapper);

        if (userMember != null) {
            //根据手机号码查询部门
            QueryWrapper<AdminUser> adminUserQueryWrapper = new QueryWrapper();
            adminUserQueryWrapper.eq("username", member.getUsername());
            AdminUser adminUser = adminUserMapper.selectOne(adminUserQueryWrapper);
            if (adminUser != null) {
                if (adminUser.getDepartmentId() != null) {
                    //查找当前登陆人的部门
                    QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper();
                    departmentQueryWrapper.eq("id", adminUser.getDepartmentId());
                    Department department = separtmentMapper.selectOne(departmentQueryWrapper);

                    QueryWrapper<RegionalPromotion> rgQueryWrapper = new QueryWrapper();
                    rgQueryWrapper.eq("user_name", regionalPromotionDTO.getUserName());
                    rgQueryWrapper.eq("area_code", department.getAreaCode());


                    regionalPromotionMapper.delete(rgQueryWrapper);

                }
            } else {
                return ResultUtil.error(ResultCode.INVITATION_MEMBER_NOT_EXIST_ERROR);
            }

        }
        return ResultUtil.success();
    }
}
