package cn.lili.modules.store.serviceimpl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.cache.Cache;
import cn.lili.cache.CachePrefix;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.file.entity.File;
import cn.lili.modules.file.mapper.FileMapper;
import cn.lili.modules.goods.service.GoodsService;
import cn.lili.modules.liande.entity.enums.StatusEnum;
import cn.lili.modules.liande.entity.vo.MemberProfitVO;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.dto.CollectionDTO;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.permission.entity.dos.AdminUser;
import cn.lili.modules.permission.entity.dos.Department;
import cn.lili.modules.permission.service.AdminUserService;
import cn.lili.modules.permission.service.DepartmentService;
import cn.lili.modules.statistics.entity.dto.StatisticsQueryParam;
import cn.lili.modules.statistics.entity.vo.StoreStatisticsDataVO;
import cn.lili.modules.statistics.service.StoreFlowStatisticsService;
import cn.lili.modules.statistics.util.StatisticsDateUtil;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.dos.StoreDetail;
import cn.lili.modules.store.entity.dto.*;
import cn.lili.modules.store.entity.enums.StoreStatusEnum;
import cn.lili.modules.store.entity.vos.StoreSearchParams;
import cn.lili.modules.store.entity.vos.StoreVO;
import cn.lili.modules.store.mapper.StoreMapper;
import cn.lili.modules.store.service.StoreDetailService;
import cn.lili.modules.store.service.StoreService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 店铺业务层实现
 *
 * @author pikachu
 * @since 2020-03-07 16:18:56
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StoreDetailService storeDetailService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private Cache cache;

    /**
     * 商品统计
     */
    @Autowired
    private StoreFlowStatisticsService storeFlowStatisticsService;

    /**
     * 通过商品分类id获取店铺
     *
     * @param params 商铺分类查询参数
     * @return 店铺分类列表
     */
    @Override
    public IPage<StoreVO> listStoreByCategory(AppStoreSearchParams params, PageVO page) {
        QueryWrapper<StoreVO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(params.getCategoryId())){
            wrapper.apply("FIND_IN_SET(" + params.getCategoryId() + ",d.goods_management_category)");
        }
        if (StringUtils.isNotEmpty(params.getStoreName())){
            wrapper.like("s.store_name",params.getStoreName());
        }
        return baseMapper.listStoreByCategory(PageUtil.initPage(page), wrapper);
    }

    /**
     * APP分页条件查询
     * 用于展示店铺列表
     *
     * @param entity
     * @param page
     * @return
     */
    @Override
    public IPage<StoreVO> getAppByPage(StoreSearchParams entity, PageVO page) {
        String local = entity.getLongitude() +","+ entity.getLatitude();
        QueryWrapper<StoreVO> wrapper = entity.queryWrapper();
        wrapper.eq("store_disable", StoreStatusEnum.OPEN.name());
        wrapper.orderByAsc("distance");
        return this.baseMapper.getAppByPage(PageUtil.initPage(page),local, wrapper);
    }

    @Override
    public IPage<StoreVO> findMakeByConditionPage(StoreSearchParams storeSearchParams, PageVO page) {
        QueryWrapper<StoreVO> wrapper = storeSearchParams.queryWrapper();
        if (StringUtils.isNotEmpty(storeSearchParams.getMemberId())) {
            wrapper.eq("member_id", Long.parseLong(storeSearchParams.getMemberId()));
        }
        if (StringUtils.isNotEmpty(storeSearchParams.getStoreDisable())) {
            wrapper.eq("store_disable", storeSearchParams.getStoreDisable());
        } else {
            wrapper.eq("store_disable", StoreStatusEnum.OPEN.name());
        }
        return this.baseMapper.getStoreList(PageUtil.initPage(page), wrapper);
    }

    @Override
    public IPage<StoreVO> findByConditionPage(StoreSearchParams storeSearchParams, PageVO page) {
        AuthUser currentUser = UserContext.getCurrentUser();
        Optional.ofNullable(currentUser).orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_LOGIN));
        // 查询服务商
        QueryWrapper<StoreVO> queryWrapper = storeSearchParams.queryWrapper();
        queryWrapper.ne("store_disable", "REFUSED");
        if (currentUser.getMember() != null) {
            AdminUser admin = adminUserService.findByMobile(currentUser.getMember().getMobile());
            if (ObjectUtils.isNotEmpty(admin)) {
                Department dept = departmentService.getById(admin.getDepartmentId());
                queryWrapper.and(wrapper -> wrapper.apply("FIND_IN_SET(" + dept.getAreaCode() + ",store_address_id_path)").or().eq("member_id", Long.parseLong(storeSearchParams.getMemberId())));
            }else {
                queryWrapper.eq("member_id", Long.parseLong(storeSearchParams.getMemberId()));
            }
        }

        //wrapper.orderByAsc(" field(state,1,4,2,3)");
        //用户名查询
        queryWrapper.like(CharSequenceUtil.isNotBlank(storeSearchParams.getMemberName()), "member_name", storeSearchParams.getMemberName());
        //店铺名查询
        queryWrapper.like(CharSequenceUtil.isNotBlank(storeSearchParams.getStoreName()), "store_name", storeSearchParams.getStoreName());
        //店铺状态
        queryWrapper.like(CharSequenceUtil.isNotBlank(storeSearchParams.getStoreDisable()), "store_disable", storeSearchParams.getStoreDisable());
        queryWrapper.orderByAsc("field(store_disable,'APPLYING','REFUSED','OPEN')");

        return this.baseMapper.getStoreList(PageUtil.initPage(page), queryWrapper);
    }

    @Override
    public StoreVO getStoreDetail() {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        StoreVO storeVO = this.baseMapper.getStoreDetail(currentUser.getStoreId());
        storeVO.setNickName(currentUser.getNickName());
        return storeVO;
    }


    /**
     * app 商铺入住修改
     *
     * @param dto
     * @return
     */
    @Override
    public int settleInUpdate(AppStoreSettleDTO dto) {
        AuthUser currentUser = UserContext.getCurrentUser();
        Optional.ofNullable(currentUser).orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_LOGIN));
        if (ObjectUtils.isEmpty(dto.getInvitationPhone()) || "null".equals(dto.getInvitationPhone())){
            String s = dto.getStoreAddressIdPath().split(",")[2];
            Department department = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getAreaCode, s).eq(Department::getDeleteFlag, StatusEnum.USE.getType()));
            AdminUser adminUser = adminUserService.getOne(new QueryWrapper<AdminUser>().lambda().eq(AdminUser::getDepartmentId, department.getId()).eq(AdminUser::getDeleteFlag, StatusEnum.USE.getType()));
            Optional.ofNullable(adminUser).orElseThrow(() -> new ServiceException(ResultCode.AREA_SERVICE_PROVIDER_NOT_EXIST));

            dto.setInvitationPhone(adminUser.getUsername());
        }

        Store store = baseMapper.selectById(dto.getStoreId());

        BeanUtil.copyProperties(dto,store);
        store.setStoreDisable(StoreStatusEnum.APPLYING.value());
        fileMapper.update(null, new UpdateWrapper<File>().lambda()
                .set(File::getOwnerId, store.getId())
                .set(File::getUserEnums, UserEnums.STORE)
                .eq(File::getUrl, dto.getStoreLogo())
                .eq(File::getOwnerId, currentUser.getId()));

        StoreEditDTO storeEditDTO = new StoreEditDTO();
        storeEditDTO.setStoreId(dto.getStoreId());
        storeEditDTO.setGoodsManagementCategory(dto.getGoodsManagementCategory());
        //修改店铺详细信息
        updateStoreDetail(storeEditDTO);

        return baseMapper.updateById(store);
    }

    /**
     * app商铺入住
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean settleIn(AppStoreSettleDTO dto) {
        AuthUser currentUser = UserContext.getCurrentUser();

        Optional.ofNullable(currentUser).orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_LOGIN));
        Store memberStore = baseMapper.selectOne(new QueryWrapper<Store>().lambda()
                .eq(Store::getMemberId, currentUser.getId())
                .eq(Store::getStoreDisable, StoreStatusEnum.APPLYING.value()));

        if (ObjectUtils.isNotEmpty(memberStore) && !"1".equals(currentUser.getMember().getDoubleStore())){
            throw new ServiceException(ResultCode.MEMBER_HAVE_OPEN_STORE);
        }


        if (ObjectUtils.isEmpty(dto.getInvitationPhone()) || "null".equals(dto.getInvitationPhone())){
            String s = dto.getStoreAddressIdPath().split(",")[2];
            Department department = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getAreaCode, s).eq(Department::getDeleteFlag, StatusEnum.USE.getType()));
            Optional.ofNullable(department).orElseThrow(() -> new ServiceException(ResultCode.AREA_SERVICE_PROVIDER_NOT_EXIST));
            AdminUser adminUser = adminUserService.getOne(new QueryWrapper<AdminUser>().lambda().eq(AdminUser::getDepartmentId, department.getId()).eq(AdminUser::getDeleteFlag, StatusEnum.USE.getType()));
            Optional.ofNullable(adminUser).orElseThrow(() -> new ServiceException(ResultCode.AREA_SERVICE_PROVIDER_NOT_EXIST));

            dto.setInvitationPhone(adminUser.getUsername());
        }
        dto.setMemberId(currentUser.getId());
        AdminStoreApplyDTO adminStoreApplyDTO = new AdminStoreApplyDTO();

        BeanUtil.copyProperties(dto,adminStoreApplyDTO);
        adminStoreApplyDTO.setLegalPhoto(dto.getStoreLogo());
        Store store   = add(adminStoreApplyDTO);
        fileMapper.update(null, new UpdateWrapper<File>().lambda()
                .set(File::getOwnerId, store.getId())
                .set(File::getUserEnums, UserEnums.STORE)
                .eq(File::getUrl, dto.getStoreLogo())
                .eq(File::getOwnerId, currentUser.getId()));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Store add(AdminStoreApplyDTO adminStoreApplyDTO) {

        //判断店铺名称是否存在
        QueryWrapper<Store> queryWrapper = Wrappers.query();
        queryWrapper.eq("store_name", adminStoreApplyDTO.getStoreName());
        if (this.getOne(queryWrapper) != null) {
            throw new ServiceException(ResultCode.STORE_NAME_EXIST_ERROR);
        }

        Member member = memberService.getById(adminStoreApplyDTO.getMemberId());
        //判断用户是否存在
        if (member == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }

        //店铺状态取得
        QueryWrapper<Store> storeWrapper = Wrappers.query();
        boolean hasStore = false;
        if (StringUtils.isNotEmpty(member.getStoreId()) && "0".equals(member.getDoubleStore())) {
            storeWrapper.eq("id", member.getStoreId());
            Store storeObj = null;
            storeObj = this.getOne(storeWrapper);
            if ((storeObj != null && !StoreStatusEnum.REFUSED.value().equals(storeObj.getStoreDisable()))) {
                hasStore = true;
            }
        }

        if (hasStore && "0".equals(member.getDoubleStore())) {
            throw new ServiceException(ResultCode.STORE_APPLY_DOUBLE_ERROR);
        }

        //判断是否拥有店铺
//        if ((Boolean.TRUE.equals(member.getHaveStore()) && !hasStore && !"1".equals(member.getDoubleStore()))) {
//            throw new ServiceException(ResultCode.STORE_APPLY_DOUBLE_ERROR);
//        }

        //添加店铺
        Store store = new Store(member, adminStoreApplyDTO);
        String s = adminStoreApplyDTO.getStoreAddressIdPath().split(",")[2];
        AdminUser adminUser = adminUserService.findByMobile(member.getMobile());

        if (ObjectUtils.isNotEmpty(adminUser) ) {
            Department department = departmentService.getOne(new QueryWrapper<Department>().lambda().eq(Department::getId,adminUser.getDepartmentId()).eq(Department::getDeleteFlag, StatusEnum.USE.getType()));
            if(s.equals(department.getAreaCode())){
                store.setStoreDisable(StoreStatusEnum.OPEN.value());
            }
        }
        this.save(store);

        //判断是否存在店铺详情，如果没有则进行新建，如果存在则进行修改
        StoreDetail storeDetail = new StoreDetail(store, adminStoreApplyDTO);

        storeDetailService.save(storeDetail);

        //设置会员-店铺信息
        memberService.update(new LambdaUpdateWrapper<Member>()
                .eq(Member::getId, member.getId())
                .set(Member::getHaveStore, false)
                .set(Member::getStoreId, store.getId()));
        return store;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Store edit(StoreEditDTO storeEditDTO) {
        if (storeEditDTO != null) {
            //判断店铺名是否唯一
            Store storeTmp = getOne(new QueryWrapper<Store>().eq("store_name", storeEditDTO.getStoreName()));
            if (storeTmp != null && !CharSequenceUtil.equals(storeTmp.getId(), storeEditDTO.getStoreId())) {
                throw new ServiceException(ResultCode.STORE_NAME_EXIST_ERROR);
            }
            //修改店铺详细信息
            updateStoreDetail(storeEditDTO);
            //修改店铺信息
            return updateStore(storeEditDTO);
        } else {
            throw new ServiceException(ResultCode.STORE_NOT_EXIST);
        }
    }

    /**
     * 修改店铺基本信息
     *
     * @param storeEditDTO 修改店铺信息
     */
    private Store updateStore(StoreEditDTO storeEditDTO) {
        Store store = this.getById(storeEditDTO.getStoreId());
        if (store != null) {
            BeanUtil.copyProperties(storeEditDTO, store);
            store.setId(storeEditDTO.getStoreId());
            boolean result = this.updateById(store);
            if (result) {
                storeDetailService.updateStoreGoodsInfo(store);
            }
        }
        cache.remove(CachePrefix.STORE.getPrefix() + storeEditDTO.getStoreId());
        return store;
    }

    /**
     * 修改店铺详细细腻
     *
     * @param storeEditDTO 修改店铺信息
     */
    private void updateStoreDetail(StoreEditDTO storeEditDTO) {
        StoreDetail storeDetail = new StoreDetail();
        BeanUtil.copyProperties(storeEditDTO, storeDetail);
        storeDetailService.update(storeDetail, new QueryWrapper<StoreDetail>().eq("store_id", storeEditDTO.getStoreId()));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean audit(String id, Integer passed) {
        Store store = this.getById(id);
        if (store == null) {
            throw new ServiceException(ResultCode.STORE_NOT_EXIST);
        }
        if (passed == 0) {
            store.setStoreDisable(StoreStatusEnum.OPEN.value());
            //修改会员 表示已有店铺
            Member member = memberService.getById(store.getMemberId());
            member.setHaveStore(true);
            member.setStoreId(id);
            memberService.updateById(member);
            //设定商家的结算日
            storeDetailService.update(new LambdaUpdateWrapper<StoreDetail>()
                    .eq(StoreDetail::getStoreId, id)
                    .set(StoreDetail::getSettlementDay, new DateTime()));
        } else {
            store.setStoreDisable(StoreStatusEnum.REFUSED.value());
        }

        return this.updateById(store);
    }


    /**
     * 驳回店铺
     *
     * @param id 店铺ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int reject(String id) {
        Store store = this.getById(id);
        if (store == null) {
            throw new ServiceException(ResultCode.STORE_NOT_EXIST);
        }

        memberService.update(null,new UpdateWrapper<Member>().lambda()
                .set(Member::getHaveStore,false)
                .set(Member::getStoreId,null)
                .eq(Member::getId,store.getMemberId()));

        return baseMapper.update(null,new UpdateWrapper<Store>().lambda()
                .set(Store::getStoreDisable,StoreStatusEnum.REFUSED.value())
                .eq(Store::getId,id));
    }






    @Override
    public boolean disable(String id) {
        Store store = this.getById(id);
        if (store != null) {
            store.setStoreDisable(StoreStatusEnum.CLOSED.value());

            //下架所有此店铺商品
            goodsService.underStoreGoods(id);
            return this.updateById(store);
        }

        throw new ServiceException(ResultCode.STORE_NOT_EXIST);
    }

    @Override
    public boolean enable(String id) {
        Store store = this.getById(id);
        if (store != null) {
            store.setStoreDisable(StoreStatusEnum.OPEN.value());
            return this.updateById(store);
        }
        throw new ServiceException(ResultCode.STORE_NOT_EXIST);
    }

    @Override
    public boolean applyFirstStep(StoreCompanyDTO storeCompanyDTO) {
        //获取当前操作的店铺
        Store store = getStoreByMember();
        //如果没有申请过店铺，新增店铺
        if (!Optional.ofNullable(store).isPresent()) {
            AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
            Member member = memberService.getById(authUser.getId());
            store = new Store(member);
            BeanUtil.copyProperties(storeCompanyDTO, store);
            this.save(store);
            StoreDetail storeDetail = new StoreDetail();
            storeDetail.setStoreId(store.getId());
            BeanUtil.copyProperties(storeCompanyDTO, storeDetail);
            return storeDetailService.save(storeDetail);
        } else {
            BeanUtil.copyProperties(storeCompanyDTO, store);
            this.updateById(store);
            //判断是否存在店铺详情，如果没有则进行新建，如果存在则进行修改
            StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
            BeanUtil.copyProperties(storeCompanyDTO, storeDetail);
            return storeDetailService.updateById(storeDetail);
        }
    }

    @Override
    public boolean applySecondStep(StoreBankDTO storeBankDTO) {

        //获取当前操作的店铺
        Store store = getStoreByMember();
        StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
        //设置店铺的银行信息
        BeanUtil.copyProperties(storeBankDTO, storeDetail);
        return storeDetailService.updateById(storeDetail);
    }

    @Override
    public boolean applyThirdStep(StoreOtherInfoDTO storeOtherInfoDTO) {
        //获取当前操作的店铺
        Store store = getStoreByMember();
        BeanUtil.copyProperties(storeOtherInfoDTO, store);
        this.updateById(store);

        StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
        //设置店铺的其他信息
        BeanUtil.copyProperties(storeOtherInfoDTO, storeDetail);
        //设置店铺经营范围
        storeDetail.setGoodsManagementCategory(storeOtherInfoDTO.getGoodsManagementCategory());
        //最后一步申请，给予店铺设置库存预警默认值
        storeDetail.setStockWarning(10);
        //修改店铺详细信息
        storeDetailService.updateById(storeDetail);
        //设置店铺名称,修改店铺信息
        store.setStoreName(storeOtherInfoDTO.getStoreName());
        store.setStoreDisable(StoreStatusEnum.APPLYING.name());
        store.setStoreCenter(storeOtherInfoDTO.getStoreCenter());
        store.setStoreDesc(storeOtherInfoDTO.getStoreDesc());
        store.setStoreLogo(storeOtherInfoDTO.getStoreLogo());
        return this.updateById(store);
    }

    @Override
    public void updateStoreGoodsNum(String storeId, Long num) {
        //修改店铺商品数量
        this.update(new LambdaUpdateWrapper<Store>()
                .set(Store::getGoodsNum, num)
                .eq(Store::getId, storeId));
    }

    @Override
    public void updateStoreCollectionNum(CollectionDTO collectionDTO) {
        baseMapper.updateCollection(collectionDTO.getId(), collectionDTO.getNum());
    }

    @Override
    public List<StoreStatisticsDataVO> getStoreStatisticsTop(StatisticsQueryParam statisticsQueryParam) {
        QueryWrapper queryWrapper = Wrappers.query();

        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);
        Date startTime = dates[0], endTime = dates[1];
        queryWrapper.between("create_time", startTime, endTime);

        return storeFlowStatisticsService.getStoreNickNumTopData(null, queryWrapper);
    }

    /**
     * 获取当前登录操作的店铺
     *
     * @return 店铺信息
     */
    private Store getStoreByMember() {
        LambdaQueryWrapper<Store> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (UserContext.getCurrentUser() != null) {
            lambdaQueryWrapper.eq(Store::getMemberId, UserContext.getCurrentUser().getId());
        }
        return this.getOne(lambdaQueryWrapper, false);
    }

}