
package cn.lili.modules.liande.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dos.Configure;
import cn.lili.modules.liande.entity.dos.MakeAccount;
import cn.lili.modules.liande.entity.dto.MakeAccountDTO;
import cn.lili.modules.liande.mapper.MakeAccountMapper;
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

import java.util.UUID;

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
    @Override
    public ResultMessage<Boolean> makeAccount(MakeAccountDTO makeAccountDTO) {

        //用户积分倍数
        QueryWrapper<Configure> jfWrapper = new QueryWrapper();
        jfWrapper.eq("type","userPoints");
        Configure jf=iConfigureService.getOne(jfWrapper);

        //商户积分倍数
        QueryWrapper<Configure> shWrapper = new QueryWrapper();
        shWrapper.eq("type","merchantPoints");
        Configure sh=iConfigureService.getOne(shWrapper);



        //当前登陆会员
        Member member = UserContext.getCurrentUser().getMember();


        //计算需要卷的数量
        Double wantPrice=Double.valueOf(makeAccountDTO.getWantPrice());
        Double wantsum=makeAccountDTO.getSurrenderPrice()/wantPrice;
        if(wantsum>member.getSSD()){
            return ResultUtil.error(ResultCode.INSUFFICIENT_QUANTITY_ERROR);
        }

        //查找做单会员用户存不存在，不存在就注册一个
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile",makeAccountDTO.getVipPhone());
        Member m= memberMapper.selectOne(queryWrapper);
        if(m==null){
            //注册用户后继续做单
            Member me=new Member();
            me.setUsername(makeAccountDTO.getVipPhone());
            String password=new BCryptPasswordEncoder().encode(makeAccountDTO.getVipPhone().substring(makeAccountDTO.getVipPhone().length() - 6));
            me.setPassword(password);
            me.setNickName(makeAccountDTO.getVipPhone());
            me.setSex(1);
            me.setMobile(makeAccountDTO.getVipPhone());
            me.setPoint(0l);
            me.setDisabled(true);
            me.setHaveStore(false);
            m.setFrozenSSD(0.0);
            m.setSSD(0.0);
            m.setBlockAddress(UUID.randomUUID().toString());
            m.setPrivateKey(UUID.randomUUID().toString());
            m.setInviteeId(Long.valueOf(member.getId()));
            memberService.registerHandler(me);
        }
        //邀请人获得SSD卷数
        QueryWrapper<Configure> yqWrapper = new QueryWrapper();
        yqWrapper.eq("type","invitation");
        Configure yq=iConfigureService.getOne(yqWrapper);

        //会员获得积分
        QueryWrapper<Member> userWrapper = new QueryWrapper();
        userWrapper.eq("mobile",makeAccountDTO.getVipPhone());
        Member userMember=memberMapper.selectOne(userWrapper);
        long jfh=(long)(jf.getNumericalAlue()*makeAccountDTO.getSurrenderPrice());
        userMember.setPoint(userMember.getPoint()+jfh);
        memberMapper.update(userMember,userWrapper);
        //商户获得积分
        QueryWrapper<Member> shangWrapper = new QueryWrapper();
        shangWrapper.eq("username",member.getUsername());
        Member shnghMember=memberMapper.selectOne(shangWrapper);
        long jfs=(long)(sh.getNumericalAlue()*makeAccountDTO.getSurrenderPrice());
        shnghMember.setPoint(shnghMember.getPoint()+jfs);
        memberMapper.update(shnghMember,shangWrapper);

        //邀请人获得SSD卷
        QueryWrapper<Member> yqrWrapper = new QueryWrapper();
        yqrWrapper.eq("mobile",makeAccountDTO.getVipPhone());
        Member hy= memberMapper.selectOne(yqrWrapper);

        QueryWrapper<Member> yqrssdWrapper = new QueryWrapper();
        yqrssdWrapper.eq("id",hy.getInviteeId());
        Member yqrMember=memberMapper.selectOne(yqrssdWrapper);
        if(yqrMember !=null){
            yqrMember.setSSD(yqrMember.getSSD()+wantsum*(yq.getNumericalAlue().doubleValue()));
            memberMapper.update(yqrMember,yqrssdWrapper);
        }


        //区域服务商获得SSD卷
        //查出店铺所属服务商
        QueryWrapper<Store> storeWrapper = new QueryWrapper();
        storeWrapper.eq("id",makeAccountDTO.getMerId());
        Store st =storeMapper.selectOne(storeWrapper);
        String addressId=st.getStoreAddressIdPath();
        String[]  strs=addressId.split(",");
        addressId=strs[strs.length-2];
        addressId= addressId.replace("\"/","");

        //根据区域ID查找部门
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper();
        departmentQueryWrapper.eq("area_Code",addressId);
        Department department=separtmentMapper.selectOne(departmentQueryWrapper);
        if(department !=null){
            //根据部门ID查找区域负责人
            QueryWrapper<AdminUser> adminUserQueryWrapper = new QueryWrapper();
            adminUserQueryWrapper.eq("department_id",department.getId());
            AdminUser adminUser=adminUserMapper.selectOne(adminUserQueryWrapper);

            if(adminUser !=null){
                //根据部门负责人查找角色
                QueryWrapper<Role> RoleWrapper = new QueryWrapper();
                RoleWrapper.eq("id",adminUser.getRoleIds());
                Role role=roleMapper.selectOne(RoleWrapper);
                if(role !=null){
                    //根据角色分配比列给当前服务商分ssd
                    QueryWrapper<Member> memberWrapper = new QueryWrapper();
                    memberWrapper.eq("mobile",adminUser.getUsername());
                    Member memberfws=memberMapper.selectOne(memberWrapper);
                    memberfws.setSSD(memberfws.getSSD()+wantsum*Double.parseDouble(role.getDescription()));
                    memberMapper.update(memberfws,memberWrapper);

                    if(department.getParentId() !=null){
                        //根据父部门ID查找上级服务商
                        QueryWrapper<AdminUser> sjadminUserQueryWrapper = new QueryWrapper();
                        sjadminUserQueryWrapper.eq("department_id",department.getParentId());
                        AdminUser sjadminUser=adminUserMapper.selectOne(sjadminUserQueryWrapper);

                        if(sjadminUser !=null){
                            //根据角色分配比列给当前服务商上级分ssd
                            QueryWrapper<Member> sjmemberWrapper = new QueryWrapper();
                            sjmemberWrapper.eq("mobile",sjadminUser.getUsername());
                            Member sjmemberfws=memberMapper.selectOne(sjmemberWrapper);
                            sjmemberfws.setSSD(sjmemberfws.getSSD()+wantsum*Double.parseDouble(role.getDescriptionParent()));
                            memberMapper.update(sjmemberfws,sjmemberWrapper);
                        }


                    }

                }

            }


        }



        return ResultUtil.success();
    }
}
