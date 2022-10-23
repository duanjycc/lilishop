package cn.lili.modules.member.mapper;


import cn.lili.modules.liande.entity.vo.MemberProfitVO;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.vo.MemberVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 会员数据处理层
 *
 * @author Bulbasaur
 * @since 2020-02-25 14:10:16
 */
public interface MemberMapper extends BaseMapper<Member> {

    /**
     * 获取所有的会员手机号
     * @return 会员手机号
     */
    @Select("select m.mobile from li_member m")
    List<String> getAllMemberMobile();

    @Select("select * from li_member ${ew.customSqlSegment}")
    IPage<MemberVO> pageByMemberVO(IPage<MemberVO> page, @Param(Constants.WRAPPER) Wrapper<Member> queryWrapper);


    /**
     * 商铺会员管理
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("select \n" +
            "\tm.username,m.SSD as ssd ,m.point\n" +
            "\tfrom li_store s\n" +
            "\tleft join li_member m on m.id = s.member_id ${ew.customSqlSegment} ")
    IPage<MemberProfitVO> getStoreMember(IPage<MemberProfitVO> page,@Param(Constants.WRAPPER) Wrapper<MemberProfitVO> queryWrapper);


    /**
     * 商铺会员管理
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("select \n" +
            "\t\tm.mobile as username,\n" +
            "\t\tm.SSD as ssd,\n" +
            "\t\tm.point as point\n" +
            "\t\tfrom w_make_account  w \n" +
            "\t\tleft join li_member m on w.vip_phone = m.mobile ${ew.customSqlSegment}  group by w.vip_phone order by  m.ssd desc")
    IPage<MemberProfitVO> getStoreMemberV2(IPage<MemberProfitVO> page,@Param(Constants.WRAPPER) Wrapper<MemberProfitVO> queryWrapper);

    /**
     * 商铺会员管理top
     * @param queryWrapper
     * @return
     */
    @Select("select \n" +
            "\t\tm.mobile as username,\n" +
            "\t\tm.SSD as ssd,\n" +
            "\t\tm.point as point\n" +
            "\t\tfrom w_make_account  w \n" +
            "\t\tleft join li_member m on w.vip_phone = m.mobile ${ew.customSqlSegment}  group by w.vip_phone order by  m.ssd desc")
    List<MemberProfitVO> getStoreMemberTopV2(@Param(Constants.WRAPPER) Wrapper<MemberProfitVO> queryWrapper);

    /**
     * 总积分
     * @return
     */
    @Select("select \n" +
            "\tsum(m.point)\n" +
            "\tfrom li_store s\n" +
            "\tleft join li_member m on m.id = s.member_id where FIND_IN_SET(#{areaId},s.store_address_id_path) ")
    Double getSumProfit(String areaId);

    /**
     * 商铺总做单数
     * @return
     */
    @Select("select \n" +
            "\tcount(w.id)\n" +
            "\tfrom w_make_account w\n" +
            "\tleft join li_store s on w.mer_id = s.id where FIND_IN_SET(#{areaId},s.store_address_id_path)  ")
    Integer getSumMakeCount(String areaId);
}