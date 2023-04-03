
package cn.lili.modules.liande.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dos.*;
import cn.lili.modules.liande.entity.dto.MakeAccountDTO;
import cn.lili.modules.liande.mapper.*;
import cn.lili.modules.liande.service.IConfigureService;
import cn.lili.modules.liande.service.IMakeAccountService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.vo.GoodsCollectionVO;
import cn.lili.modules.member.entity.vo.MemberVO;
import cn.lili.modules.member.mapper.MemberMapper;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.permission.entity.dos.AdminUser;
import cn.lili.modules.permission.entity.dos.Department;
import cn.lili.modules.permission.entity.dos.Role;
import cn.lili.modules.permission.mapper.AdminUserMapper;
import cn.lili.modules.permission.mapper.DepartmentMapper;
import cn.lili.modules.permission.mapper.RoleMapper;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.mapper.StoreMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * <p>
 * 做单明细表 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Slf4j
@Service
public class MakeAccountServiceImpl extends ServiceImpl<MakeAccountMapper, MakeAccount> implements IMakeAccountService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private IConfigureService iConfigureService;

    @Autowired
    StoreMapper storeMapper;

    @Autowired
    DepartmentMapper separtmentMapper;

    @Autowired
    AdminUserMapper adminUserMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MakeAccountMapper makeAccountMapper;

    @Autowired
    DestroyDetailMapper destroyDetailMapper;

    @Autowired
    MemberIncomeMapper memberIncomeMapper;

    @Autowired
    ServiceProviderIncomeMapper serviceProviderIncomeMapper;

    @Autowired
    ScoreAcquisitionMapper  scoreAcquisitionMapper;

    @Autowired
    RegionalPromotionMapper regionalPromotionMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage<Boolean> makeAccount(MakeAccountDTO makeAccountDTO) {
        //当前登陆会员
        Member member = UserContext.getCurrentUser().getMember();

        if (StringUtils.isEmpty(makeAccountDTO.getMerId())) {
            throw new ServiceException(ResultCode.SHANGJIAEMPTY_ERROR);
        }
        if(!matchPhoneNumber(makeAccountDTO.getVipPhone())){
            throw new ServiceException(ResultCode.TRANSFER_PHONE_ERROR);
        }
        //验证支付密码
        QueryWrapper<Member> memberpassWrapper = new QueryWrapper();
        memberpassWrapper.eq("username", member.getUsername());
        Member memberpass = memberMapper.selectOne(memberpassWrapper);
        if (!new BCryptPasswordEncoder().matches(makeAccountDTO.getSecondPassword(), memberpass.getPaymentPassword())) {
            throw new ServiceException(ResultCode.TRANSFER_SECOND_PASSWORD_ERROR);
        }
        if(member.getUsername().equals(makeAccountDTO.getVipPhone())){
            throw new ServiceException(ResultCode.DISTRIBUTIONVIP_ERROR);
        }

        if (makeAccountDTO.getSurrenderPrice() < 1) {
            throw new ServiceException(ResultCode.SURRENDERSMALLPRICE_ERRORW);
        }

        //用户积分倍数
        QueryWrapper<Configure> jfWrapper = new QueryWrapper();
        jfWrapper.eq("type", "userPoints");
        Configure jf = iConfigureService.getOne(jfWrapper);

        //商户积分倍数
        QueryWrapper<Configure> shWrapper = new QueryWrapper();
        shWrapper.eq("type", "merchantPoints");
        Configure sh = iConfigureService.getOne(shWrapper);

        //价格
        QueryWrapper<Configure> jgWrapper = new QueryWrapper();
        jgWrapper.eq("type", "unitPrice");
        Configure jg = iConfigureService.getOne(jgWrapper);


        //计算需要卷的数量
        Double wantPrice = jg.getNumericalAlue();
        Double wantsum = makeAccountDTO.getSurrenderPrice() / wantPrice;
        if (wantsum > memberpass.getSsd()) {
            throw new ServiceException(ResultCode.INSUFFICIENT_QUANTITY_ERROR);

        }
        //查找做单会员用户存不存在，不存在就注册一个
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", makeAccountDTO.getVipPhone());
        Member m = memberMapper.selectOne(queryWrapper);
        if (m == null) {
            //注册用户后继续做单
            m = new Member();
            m.setUsername(makeAccountDTO.getVipPhone());
            String password = new BCryptPasswordEncoder().encode(StringUtils.md5(makeAccountDTO.getVipPhone().substring(makeAccountDTO.getVipPhone().length() - 6)));
            m.setPassword(password);
            m.setNickName(makeAccountDTO.getVipPhone());
            m.setSex(1);
            m.setMobile(makeAccountDTO.getVipPhone());
            m.setPoint(0l);
            m.setDisabled(true);
            m.setHaveStore(false);
            m.setFrozenSSD(0.0);
            m.setSsd(0.0);
            m.setBlockAddress(UUID.randomUUID().toString());
            m.setPrivateKey(UUID.randomUUID().toString());
            m.setInviteeId(Long.valueOf(member.getId()));
            memberService.registerHandler(m);
        }
        //邀请人获得SSD卷数
        QueryWrapper<Configure> yqWrapper = new QueryWrapper();
        yqWrapper.eq("type", "invitation");
        Configure yq = iConfigureService.getOne(yqWrapper);

        //会员获得积分
        QueryWrapper<Member> userWrapper = new QueryWrapper();
        userWrapper.eq("mobile", makeAccountDTO.getVipPhone());
        Member userMember = memberMapper.selectOne(userWrapper);
        long jfh = (long) (jf.getNumericalAlue() * makeAccountDTO.getSurrenderPrice());
        userMember.setPoint(userMember.getPoint() + jfh);
        memberMapper.update(userMember, userWrapper);



        //商户获得积分/减去相对应的SSD
        QueryWrapper<Member> shangWrapper = new QueryWrapper();
        shangWrapper.eq("username", member.getUsername());
        Member shnghMember = memberMapper.selectOne(shangWrapper);
        long jfs = (long) (sh.getNumericalAlue() * makeAccountDTO.getSurrenderPrice());
        shnghMember.setPoint(shnghMember.getPoint() + jfs);
        shnghMember.setSsd(shnghMember.getSsd() - wantsum);
        memberMapper.update(shnghMember, shangWrapper);


        //生成做当ID
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//设置北京时间
        long mkid = Long.parseLong(simpleDateFormat.format(new Date()));

        //查出店铺所属服务商
        QueryWrapper<Store> storeWrapper = new QueryWrapper();
        storeWrapper.eq("id", makeAccountDTO.getMerId());
        Store st = storeMapper.selectOne(storeWrapper);

        //邀请人获得SSD卷
        QueryWrapper<Member> yqrWrapper = new QueryWrapper();
        yqrWrapper.eq("mobile", makeAccountDTO.getVipPhone());
        Member hy = memberMapper.selectOne(yqrWrapper);

        QueryWrapper<Member> yqrssdWrapper = new QueryWrapper();
        yqrssdWrapper.eq("id", hy.getInviteeId());
        Member yqrMember = memberMapper.selectOne(yqrssdWrapper);
        if (yqrMember != null) {
//            yqrMember.setSsd(yqrMember.getSsd() + wantsum * (yq.getNumericalAlue().doubleValue()));
            yqrMember.setPoint(yqrMember.getPoint()+makeAccountDTO.getSurrenderPrice().longValue());
            memberMapper.update(yqrMember, yqrssdWrapper);
            ScoreAcquisition scoreAcquisition=new ScoreAcquisition();
            scoreAcquisition.setCreateTime(new Date());
            scoreAcquisition.setIntegralType(0l);
            scoreAcquisition.setUserId(yqrMember.getId());
            scoreAcquisition.setMerId(st.getId());
            scoreAcquisition.setMerName(st.getStoreName());
            scoreAcquisition.setOrderId(mkid);
            scoreAcquisition.setIntegral(makeAccountDTO.getSurrenderPrice());
            scoreAcquisitionMapper.insert(scoreAcquisition);


//            //邀请人获取SSD日志
//            MemberIncome mi = new MemberIncome();
//            mi.setConsumerUserid(Long.parseLong(hy.getId()));
//            mi.setUserId(Long.parseLong(yqrMember.getId()));
//            mi.setCreationTime(new Date());
//            mi.setQuantity(wantsum * (yq.getNumericalAlue().doubleValue()));
//            mi.setIncomeProportion(yq.getNumericalAlue() + "");
//            mi.setOrderId(mkid + "");
//            mi.setIncomeType("0");
//            memberIncomeMapper.insert(mi);
        }



        //区域服务商获得SSD卷
        String addressId =null;
        try {
        addressId = st.getStoreAddressIdPath();
        String[] strs = addressId.split(",");
        addressId = strs[strs.length - 2];
        addressId = addressId.replace("\"/", "");
        }catch (Exception e){
            addressId="null";
        }

        //根据区域ID查找部门
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper();
        departmentQueryWrapper.eq("area_Code", addressId);
        Department department = separtmentMapper.selectOne(departmentQueryWrapper);
        if (department != null) {
            //根据部门ID查找区域负责人
            QueryWrapper<AdminUser> adminUserQueryWrapper = new QueryWrapper();
            adminUserQueryWrapper.eq("department_id", department.getId());
            AdminUser adminUser = adminUserMapper.selectOne(adminUserQueryWrapper);

            if (adminUser != null) {
                //根据部门负责人查找角色
                QueryWrapper<Role> RoleWrapper = new QueryWrapper();
                RoleWrapper.eq("id", adminUser.getRoleIds());
                Role role = roleMapper.selectOne(RoleWrapper);

                //根据邀请人和区域code查找分配比例
                Double fws =Double.parseDouble(role.getDescription());
                Double yqr =0.0;
                if(st.getInvitationPhone() !=null){
                    QueryWrapper<RegionalPromotion> regionalWrapper = new QueryWrapper();
                    regionalWrapper.eq("user_name", st.getInvitationPhone());
                    regionalWrapper.eq("area_code",addressId);
                    List<RegionalPromotion> r=regionalPromotionMapper.selectList(regionalWrapper);
                    if(r!=null && r.size() > 0){
                        //分配邀请人收益
                        yqr=  ((double)r.get(0).getIncomeComparison()/100.00);
                        QueryWrapper<Member> shqqrWrapper = new QueryWrapper();
                        shqqrWrapper.eq("username", st.getInvitationPhone());
                        Member spyqr = memberMapper.selectOne(shqqrWrapper);
                        spyqr.setSsd(spyqr.getSsd()+wantsum*yqr);
                        memberMapper.update(spyqr, shqqrWrapper);

                        //本服务商合伙人收益日志
                        ServiceProviderIncome hhr = new ServiceProviderIncome();
                        hhr.setConsumerUserid(Long.parseLong(userMember.getId()));
                        hhr.setUserId(Long.parseLong(spyqr.getId()));
                        hhr.setCreationTime(new Date());
                        hhr.setQuantity(wantsum*yqr);
                        hhr.setIncomeType(2l);
                        hhr.setIncomeProportion(yqr+"");
                        hhr.setOrderId(mkid + "");
                        serviceProviderIncomeMapper.insert(hhr);
                    }
                }



                if (role != null) {
                    //根据角色分配比列给当前服务商分ssd
                    QueryWrapper<Member> memberWrapper = new QueryWrapper();
                    memberWrapper.eq("mobile", adminUser.getUsername());
                    Member memberfws = memberMapper.selectOne(memberWrapper);
                    memberfws.setSsd(memberfws.getSsd() + wantsum * (fws-yqr));
                    memberMapper.update(memberfws, memberWrapper);

                    //本服务商收益日志
                    ServiceProviderIncome si = new ServiceProviderIncome();
                    si.setConsumerUserid(Long.parseLong(userMember.getId()));
                    si.setUserId(Long.parseLong(memberfws.getId()));
                    si.setCreationTime(new Date());
                    si.setQuantity(wantsum * (fws-yqr));
                    si.setIncomeType(0l);
                    si.setIncomeProportion((fws-yqr)+"");
                    si.setOrderId(mkid + "");
                    serviceProviderIncomeMapper.insert(si);

                    if (department.getParentId() != null) {
                        //根据父部门ID查找上级服务商
                        QueryWrapper<AdminUser> sjadminUserQueryWrapper = new QueryWrapper();
                        sjadminUserQueryWrapper.eq("department_id", department.getParentId());
                        AdminUser sjadminUser = adminUserMapper.selectOne(sjadminUserQueryWrapper);

                        if (sjadminUser != null) {

                            //查找父服务商角色
                            QueryWrapper<Role> fjRoleWrapper = new QueryWrapper();
                            fjRoleWrapper.eq("id", sjadminUser.getRoleIds());
                            Role fjrole = roleMapper.selectOne(fjRoleWrapper);

                            //根据角色分配比列给当前服务商上级分ssd
                            QueryWrapper<Member> sjmemberWrapper = new QueryWrapper();
                            sjmemberWrapper.eq("mobile", sjadminUser.getUsername());
                            Member sjmemberfws = memberMapper.selectOne(sjmemberWrapper);
                            sjmemberfws.setSsd(sjmemberfws.getSsd() + wantsum * Double.parseDouble(fjrole.getDescriptionParent()));
                            memberMapper.update(sjmemberfws, sjmemberWrapper);



                            //父区域服务商收益日志
                            ServiceProviderIncome ssj = new ServiceProviderIncome();
                            ssj.setConsumerUserid(Long.parseLong(userMember.getId()));
                            ssj.setUserId(Long.parseLong(sjmemberfws.getId()));
                            ssj.setCreationTime(new Date());
                            ssj.setQuantity(wantsum * Double.parseDouble(fjrole.getDescriptionParent()));
                            ssj.setIncomeType(1l);
                            ssj.setIncomeProportion(fjrole.getDescriptionParent());
                            ssj.setOrderId(mkid + "");
                            ssj.setSubArea(department.getTitle());
                            serviceProviderIncomeMapper.insert(ssj);
                        }


                    }

                }

            }

        }
        //插入做单记录
        MakeAccount ma = new MakeAccount();
        ma.setId(mkid);
        ma.setCreateTime(new Date());
        ma.setUserId(Long.parseLong(st.getMemberId()));
        ma.setMerId(st.getId());
        ma.setMerName(st.getStoreName());
        ma.setMonetary(makeAccountDTO.getMonetary());
        ma.setUsername(st.getMemberName());
        ma.setSurrenderPrice(makeAccountDTO.getSurrenderPrice());
        ma.setSurrenderRatio(makeAccountDTO.getSurrenderRatio());
        ma.setVipPhone(makeAccountDTO.getVipPhone());
        ma.setIsUse("0");
        ma.setWantPrice(wantPrice+"");
        ma.setShReturnPower(sh.getNumericalAlue() * makeAccountDTO.getSurrenderPrice());
        ma.setUserReturnPower(jf.getNumericalAlue() * makeAccountDTO.getSurrenderPrice());
        makeAccountMapper.insert(ma);

        //插入销毁明细
        DestroyDetail de = new DestroyDetail();
        de.setUserId(Long.parseLong(st.getMemberId()));
        de.setCreateTime(new Date());
        de.setMerName(st.getStoreName());
        de.setPrice(makeAccountDTO.getSurrenderPrice());
        de.setWantCount(wantsum*0.82);
        de.setStatus("0");
        de.setDestroyTime(new Date());
        de.setWantPrice(wantPrice+"");
        de.setStoreId(st.getId());
        destroyDetailMapper.insert(de);

        //插入会员获取积分明细
        ScoreAcquisition scoreAcquisition=new ScoreAcquisition();
        scoreAcquisition.setCreateTime(new Date());
        scoreAcquisition.setIntegralType(0l);
        scoreAcquisition.setUserId(userMember.getId());
        scoreAcquisition.setMerId(st.getId());
        scoreAcquisition.setMerName(st.getStoreName());
        scoreAcquisition.setOrderId(mkid);
        scoreAcquisition.setIntegral(jf.getNumericalAlue() * makeAccountDTO.getSurrenderPrice());
        scoreAcquisitionMapper.insert(scoreAcquisition);


        //插入商户获取积分明细
        ScoreAcquisition scoreAcquisitionsh=new ScoreAcquisition();
        scoreAcquisitionsh.setCreateTime(new Date());
        scoreAcquisitionsh.setIntegralType(1l);
        scoreAcquisitionsh.setUserId(st.getMemberId());
        scoreAcquisitionsh.setMerId(st.getId());
        scoreAcquisitionsh.setMerName(st.getStoreName());
        scoreAcquisitionsh.setOrderId(mkid);
        scoreAcquisitionsh.setIntegral(sh.getNumericalAlue() * makeAccountDTO.getSurrenderPrice());
        scoreAcquisitionMapper.insert(scoreAcquisitionsh);

        return ResultUtil.success();
    }
    /**
     * 验证手机号 由于号码段不断的更新，只需要判断手机号有11位，并且全是数字以及1开头
     * @param phoneNumber 手机号码
     * @return
     */
    private static boolean matchPhoneNumber(String phoneNumber) {
        String regex = "^1\\d{10}$";
        if(phoneNumber==null||phoneNumber.length()<=0){
            return false;
        }
        return Pattern.matches(regex, phoneNumber);
    }

    public static void main(String[] args) {
        Double a=27.098;
        System.out.println(a.longValue());
    }
}
