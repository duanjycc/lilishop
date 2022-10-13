package cn.lili.modules.permission.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.token.Token;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.utils.DateUtil;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.liande.entity.dos.ServiceLog;
import cn.lili.modules.liande.entity.dto.ServiceProviderParams;
import cn.lili.modules.liande.entity.dto.SignInDTO;
import cn.lili.modules.liande.entity.dto.StoreAchievementParams;
import cn.lili.modules.liande.entity.enums.StatusEnum;
import cn.lili.modules.liande.entity.vo.AchievementVO;
import cn.lili.modules.liande.entity.vo.ServiceProviderParamsVO;
import cn.lili.modules.liande.entity.vo.ServiceRegionVO;
import cn.lili.modules.liande.entity.vo.StoreAchievementParamsVO;
import cn.lili.modules.liande.mapper.ServiceLogMapper;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.serviceimpl.MemberServiceImpl;
import cn.lili.modules.permission.entity.dos.AdminUser;
import cn.lili.modules.permission.entity.dos.Department;
import cn.lili.modules.permission.entity.dos.Role;
import cn.lili.modules.permission.entity.dos.UserRole;
import cn.lili.modules.permission.entity.dto.AdminUserDTO;
import cn.lili.modules.permission.entity.vo.AdminUserVO;
import cn.lili.modules.permission.mapper.AdminUserMapper;
import cn.lili.modules.permission.service.*;
import cn.lili.modules.store.mapper.StoreMapper;
import cn.lili.modules.system.aspect.annotation.SystemLogPoint;
import cn.lili.modules.system.entity.dos.Region;
import cn.lili.modules.system.mapper.RegionMapper;
import cn.lili.modules.system.token.ManagerTokenGenerate;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户业务层实现
 *
 * @author Chopper
 * @since 2020/11/17 3:46 下午
 */
@Slf4j
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Autowired
    private ServiceLogMapper serviceLogMapper;

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private ManagerTokenGenerate managerTokenGenerate;
    /**
     * 角色长度
     */
    private final int rolesMaxSize = 10;

    /**
     * 根据区域id获取区域列表以及上级区域列表
     *
     * @param areaId
     * @return
     */
    @Override
    public ServiceRegionVO getRegion(String areaId) {
        Department department = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getAreaCode, areaId));
        Department parentDept = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getId, department.getParentId()));
        AdminUser adminUser = baseMapper.selectOne(new QueryWrapper<AdminUser>().lambda().eq(AdminUser::getDepartmentId, department.getId()));
        Region region = regionMapper.selectById(areaId);
        Region parentRegion = regionMapper.selectById(parentDept.getAreaCode());
        region.setPath(region.getPath()+","+ region.getId());
        parentRegion.setPath(parentRegion.getPath()+","+ parentRegion.getId());
        return new ServiceRegionVO(region.getPath().split(","),parentRegion.getPath().split(","),parentRegion.getId(),adminUser.getRoleIds());
    }

    /**
     * 检测区域是否被签约
     *
     * @param areaId
     * @return
     */
    @Override
    public Boolean checkAreaHavSign(String areaId) {
        Department dept = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getAreaCode,areaId));
        return ObjectUtils.isEmpty(dept) ? true : false;
    }

    /**
     * 服务商业绩
     *
     * @param mobile
     * @return
     */
    @Override
    public AchievementVO queryAchievement(String mobile) {
        AdminUser user = baseMapper.selectOne(new QueryWrapper<AdminUser>().lambda().eq(AdminUser::getMobile, mobile));
        if (ObjectUtils.isEmpty(user)) {
            throw new ServiceException(ResultCode.AREA_IS_NOT_SIGN);
        }
        Department dept = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getId, user.getDepartmentId()));
        if (ObjectUtils.isEmpty(dept)) {
            throw new ServiceException(ResultCode.AREA_IS_HAVE_SIGN);
        }
        Department parentDept = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getId, dept.getParentId()));
        Role role = roleService.getById(user.getRoleIds());
        Region region = regionMapper.selectById(dept.getAreaCode());
        int storeCount = storeMapper.queryStoreCountByAreaId(dept.getAreaCode());
        double surrenderPrice = storeMapper.queryMakeSumByAreaId(dept.getAreaCode());
        double destroySSD = storeMapper.queryDestroySumByAreaId(dept.getAreaCode());

        AchievementVO achievementVO = new AchievementVO();
        achievementVO.setAvatar(user.getAvatar());
        achievementVO.setUsername(user.getUsername());
        achievementVO.setNickName(user.getNickName());
        achievementVO.setMobile(mobile);
        achievementVO.setServiceProviderLevel(role.getRoleName());
        achievementVO.setSignAreaName(region.getName());
        achievementVO.setSignAreaId(region.getId());
        achievementVO.setParentName(parentDept.getTitle());
        achievementVO.setSignCreateTime(user.getCreateTime());
        achievementVO.setStoreCount(storeCount);
        achievementVO.setDestroySSD(destroySSD);
        achievementVO.setSurrenderPrice(surrenderPrice);
        return achievementVO;
    }

    /**
     * 获取服务商所属店铺业绩后端管理分页
     *
     * @param initPage
     * @param params
     * @return
     */
    @Override
    public IPage<StoreAchievementParamsVO> queryStoreAchievement(Page initPage, StoreAchievementParams params) {
        QueryWrapper<StoreAchievementParams> queryWrapper = new QueryWrapper();
        queryWrapper.apply(" s.store_disable = 'OPEN' ");
        queryWrapper.apply(" FIND_IN_SET(" + params.getAreaId() + ",s.store_address_id_path)");
        queryWrapper.ge(ObjectUtils.isNotEmpty(params.getStartDate()), "w.create_time", params.getStartDate() + DateUtil.DATA_PREFIX);
        queryWrapper.le(ObjectUtils.isNotEmpty(params.getEndDate()), "w.create_time", params.getEndDate() + DateUtil.DATA_SUFFIX);

        return baseMapper.queryStoreAchievement(initPage, queryWrapper);
    }

    @Override
    public IPage<ServiceProviderParamsVO> queryServiceProvider(Page initPage, ServiceProviderParams params) {
        QueryWrapper<ServiceProviderParams> queryWrapper = new QueryWrapper();

        queryWrapper.apply("r.level in ('province','city','district')");
        if (StatusEnum.USE.getType().equals(params.getIsSignIn())) {
            queryWrapper.apply(" t.id is not null");
        }
        if (StatusEnum.DEL.getType().equals(params.getIsSignIn())) {
            queryWrapper.apply(" t.id is null");
        }
        if (ObjectUtils.isNotEmpty(params.getAreaName())) {
            queryWrapper.like("r.name", params.getAreaName());
        }
        if (ObjectUtils.isNotEmpty(params.getAreaId())) {
            queryWrapper.apply(" FIND_IN_SET(" + params.getAreaId() + ", r.path)");
        }
        queryWrapper.orderByDesc("t.signCreateTime ");
        return baseMapper.queryServiceProvider(initPage, queryWrapper);
    }

    /**
     * 服务商管理-签约
     *
     * @param signInDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void signIn(SignInDTO signInDTO) {
        Member member = memberService.getOne(new QueryWrapper<Member>().lambda().eq(Member::getMobile, signInDTO.getMobile()));
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(ResultCode.MEMBER_IS_NOT_EXIST);
        }
        AdminUser user = baseMapper.selectOne(new QueryWrapper<AdminUser>().lambda().eq(AdminUser::getMobile, signInDTO.getMobile()));
        if (ObjectUtils.isNotEmpty(user)) {
            throw new ServiceException(ResultCode.USER_IS_HAVE_SIGN);
        }
        Department dept = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getAreaCode, signInDTO.getSignAreaId()));
        if (ObjectUtils.isNotEmpty(dept)) {
            throw new ServiceException(ResultCode.AREA_IS_HAVE_SIGN);
        }

        // 插入部门表
        Department department = new Department();
        department.setAreaCode(signInDTO.getSignAreaId());
        department.setParentId(signInDTO.getParentServiceProvider());
        department.setCreateTime(new Date());
        department.setTitle(signInDTO.getSignAreaName().replace("/", ""));
        department.setCreateBy(UserContext.getCurrentUser().getUsername());
        departmentService.save(department);
        // 插入 admin_user

        AdminUser adminUser = new AdminUser();
        adminUser.setDepartmentId(department.getId());
        adminUser.setDescription(signInDTO.getUsername() + "在【" + DateUtil.getCurrentDateStr() + "】 签约成为服务商");
        adminUser.setMobile(signInDTO.getMobile());
        adminUser.setUsername(signInDTO.getMobile());
        adminUser.setNickName(signInDTO.getUsername());
        adminUser.setIsSuper(false);
        adminUser.setStatus(true);
        adminUser.setRoleIds(signInDTO.getServiceProviderLevel());
        adminUser.setEmail(signInDTO.getMobile() + "qq.com");
        String password = new BCryptPasswordEncoder().encode(StringUtils.md5(signInDTO.getMobile().substring(signInDTO.getMobile().length() - 6)));
        adminUser.setPassword(password);
        adminUser.setCreateTime(new Date());
        department.setCreateBy(UserContext.getCurrentUser().getUsername());
        baseMapper.insert(adminUser);
    }

    /**
     * 服务商管理-修改
     *
     * @param signInDTO
     */
    @Override
    public void update(SignInDTO signInDTO) {
        // 查询服务商对应的签约区域
        // 1,根据会员号找出所负责的deptId
        AdminUser user = baseMapper.selectOne(new QueryWrapper<AdminUser>().lambda().eq(AdminUser::getMobile, signInDTO.getMobile()));
        user.setMobile(signInDTO.getMobile());
        user.setNickName(signInDTO.getUsername());
        user.setRoleIds(signInDTO.getServiceProviderLevel());
        baseMapper.updateById(user);
        // 2,构造签约区域名称
        List<Region> regions = regionMapper.selectList(new QueryWrapper<Region>().lambda().in(Region::getId, signInDTO.getSignAreaIds()));
        String collect = regions.stream().map(Region::getName).collect(Collectors.joining());

        Department department = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getId, user.getDepartmentId()));
        department.setTitle(collect);
        department.setAreaCode(signInDTO.getSignAreaId());
        department.setParentId(signInDTO.getParentServiceProvider());
        departmentService.updateById(department);

        //查询该服务商所有的下级服务商
        List<Department> list = departmentService.list(new QueryWrapper<Department>().lambda().eq(Department::getParentId, user.getDepartmentId()));
        list.stream().forEach( s -> {
            s.setParentId(signInDTO.getSignAreaId());
            departmentService.updateById(department);
        });

    }

    /**
     * 服务商管理-删除签约
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteSignIn(String id) {
        Department dept = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getAreaCode, id));
        if (ObjectUtils.isEmpty(dept)) {
            throw new ServiceException(ResultCode.AREA_IS_NOT_SIGN);
        }
        AdminUser user = baseMapper.selectOne(new QueryWrapper<AdminUser>().lambda().eq(AdminUser::getDepartmentId, dept.getId()));
        if (ObjectUtils.isEmpty(user)) {
            throw new ServiceException(ResultCode.AREA_IS_NOT_SIGN);
        }

        departmentService.removeById(dept.getId());
        baseMapper.deleteById(user.getId());

        ServiceLog log = new ServiceLog(dept.getAreaCode(), dept.getTitle(), user.getId(), user.getNickName());
        serviceLogMapper.insert(log);
    }

    @Override
    public IPage<AdminUserVO> adminUserPage(Page initPage, QueryWrapper<AdminUser> initWrapper) {
        Page<AdminUser> adminUserPage = page(initPage, initWrapper);
        List<Role> roles = roleService.list();
        List<Department> departments = departmentService.list();

        List<AdminUserVO> result = new ArrayList<>();

        adminUserPage.getRecords().forEach(adminUser -> {
            AdminUserVO adminUserVO = new AdminUserVO(adminUser);
            if (!CharSequenceUtil.isEmpty(adminUser.getDepartmentId())) {
                try {
                    adminUserVO.setDepartmentTitle(
                            departments.stream().filter
                                            (department -> department.getId().equals(adminUser.getDepartmentId()))
                                    .collect(Collectors.toList())
                                    .get(0)
                                    .getTitle()
                    );
                } catch (Exception e) {
                    log.error("填充部门信息异常", e);
                }
            }
            if (!CharSequenceUtil.isEmpty(adminUser.getRoleIds())) {
                try {
                    List<String> memberRoles = Arrays.asList(adminUser.getRoleIds().split(","));
                    adminUserVO.setRoles(
                            roles.stream().filter
                                            (role -> memberRoles.contains(role.getId()))
                                    .collect(Collectors.toList())
                    );
                } catch (Exception e) {
                    log.error("填充部门信息异常", e);
                }
            }
            result.add(adminUserVO);
        });
        Page<AdminUserVO> pageResult = new Page(adminUserPage.getCurrent(), adminUserPage.getSize(), adminUserPage.getTotal());
        pageResult.setRecords(result);
        return pageResult;

    }

    @Autowired
    private void setManagerTokenGenerate(ManagerTokenGenerate managerTokenGenerate) {
        this.managerTokenGenerate = managerTokenGenerate;
    }

    @Override
    @SystemLogPoint(description = "管理员登录", customerLog = "'管理员登录请求:'+#username")
    public Token login(String username, String password) {
        AdminUser adminUser = this.findByUsername(username);

        if (adminUser == null || !adminUser.getStatus()) {
            throw new ServiceException(ResultCode.USER_PASSWORD_ERROR);
        }
        if (!new BCryptPasswordEncoder().matches(password, adminUser.getPassword())) {
            throw new ServiceException(ResultCode.USER_PASSWORD_ERROR);
        }
        try {
            return managerTokenGenerate.createToken(adminUser, false);
        } catch (Exception e) {
            log.error("管理员登录错误", e);
        }
        return null;

    }


    @Override
    public Token refreshToken(String refreshToken) {
        return managerTokenGenerate.refreshToken(refreshToken);
    }


    @Override
    public AdminUser findByUsername(String username) {

        AdminUser user = getOne(new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, username));

        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public AdminUser findByMobile(String mobile) {
        AdminUser user = getOne(new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getMobile, mobile));
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    @SystemLogPoint(description = "修改管理员", customerLog = "'修改管理员:'+#adminUser.username")
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAdminUser(AdminUser adminUser, List<String> roles) {

        if (roles != null && !roles.isEmpty()) {

            if (roles.size() > rolesMaxSize) {
                throw new ServiceException(ResultCode.PERMISSION_BEYOND_TEN);
            }
            adminUser.setRoleIds(CharSequenceUtil.join(",", roles));

        } else {
            adminUser.setRoleIds("");
        }

        updateRole(adminUser.getId(), roles);
        this.updateById(adminUser);
        return true;
    }


    @Override
    public void editPassword(String password, String newPassword) {
        AuthUser tokenUser = UserContext.getCurrentUser();
        AdminUser user = this.getById(tokenUser.getId());
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new ServiceException(ResultCode.USER_OLD_PASSWORD_ERROR);
        }
        String newEncryptPass = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(newEncryptPass);
        this.updateById(user);
    }

    @Override
    public void resetPassword(List<String> ids) {
        LambdaQueryWrapper<AdminUser> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(AdminUser::getId, ids);
        List<AdminUser> adminUsers = this.list(lambdaQueryWrapper);
        String password = StringUtils.md5("123456");
        String newEncryptPass = new BCryptPasswordEncoder().encode(password);
        if (null != adminUsers && !adminUsers.isEmpty()) {
            adminUsers.forEach(item -> item.setPassword(newEncryptPass));
            this.updateBatchById(adminUsers);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAdminUser(AdminUserDTO adminUser, List<String> roles) {
        AdminUser dbUser = new AdminUser();
        BeanUtil.copyProperties(adminUser, dbUser);
        dbUser.setPassword(new BCryptPasswordEncoder().encode(dbUser.getPassword()));
        if (roles.size() > rolesMaxSize) {
            throw new ServiceException(ResultCode.PERMISSION_BEYOND_TEN);
        }
        if (!roles.isEmpty()) {
            dbUser.setRoleIds(CharSequenceUtil.join(",", roles));
        }
        this.save(dbUser);
        dbUser = this.findByUsername(dbUser.getUsername());
        updateRole(dbUser.getId(), roles);
    }


    @Override
    public void deleteCompletely(List<String> ids) {
        //彻底删除超级管理员
        this.removeByIds(ids);

        //删除管理员角色
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", ids);
        userRoleService.remove(queryWrapper);
    }

    /**
     * 更新用户角色
     *
     * @param userId 用户id
     * @param roles  角色id集合
     */
    private void updateRole(String userId, List<String> roles) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        userRoleService.remove(queryWrapper);

        if (roles.isEmpty()) {
            return;
        }
        List<UserRole> userRoles = new ArrayList<>(roles.size());
        roles.forEach(id -> userRoles.add(new UserRole(userId, id)));
        userRoleService.updateUserRole(userId, userRoles);
    }
}
