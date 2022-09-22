package cn.lili.modules.permission.mapper;

import cn.lili.modules.liande.entity.dto.ServiceProviderParams;
import cn.lili.modules.liande.entity.dto.StoreAchievementParams;
import cn.lili.modules.liande.entity.vo.ServiceProviderParamsVO;
import cn.lili.modules.liande.entity.vo.StoreAchievementParamsVO;
import cn.lili.modules.permission.entity.dos.AdminUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据处理层
 *
 * @author Chopper
 * @since 2020-11-22 09:17
 */
public interface AdminUserMapper extends BaseMapper<AdminUser> {



    /**
     * 获取服务商后端管理分页
     *
     * @param initPage
     * @param queryWrapper
     * @return
     */
    @Select("\tselect \n" +
            "\t\tr.id as areaId,\n" +
            "\t\tr.name as areaName,\n" +
            "\t\t(select c.name from li_region c where c.id = r.parent_id ) as parentName,\n" +
            "\t\tIF(t.id,0,1) as isSignIn,\n" +
            "\t\tt.nick_name as areaServiceProviderName,\n" +
            "\t\tt.mobile as areaServiceProviderMobile,\n" +
            "\t\tt.signCreateTime as signCreateTime\n" +
            "\t\tfrom li_region r left join\n" +
            "\t\t\t(\t\n" +
            "\t\t\t\tselect d.*,a.nick_name,a.create_time as signCreateTime,a.mobile \n" +
            "\t\t\t\t\tfrom li_department d \n" +
            "\t\t\t\t\tleft join li_admin_user a on d.id = a.department_id\n" +
            "\t\t\t) t on Cast(r.id as char) = t.area_code  ${ew.customSqlSegment}")
    IPage<ServiceProviderParamsVO>  queryServiceProvider(IPage<ServiceProviderParamsVO> initPage, @Param(Constants.WRAPPER) QueryWrapper<ServiceProviderParams> queryWrapper);


    @Select("select \n" +
            "\t\t\ts.member_name as memberName,\n" +
            "\t\t\ts.store_name as storeName,\n" +
            "\t\t\tif(sum( w.surrender_price),sum( w.surrender_price),0) as surrenderPrice,\n" +
            "\t\t\tif(count( w.id),count( w.id),0) as makeCount,\n" +
            "\t\t\t(select if(sum(w.want_count),sum(w.want_count),0) from w_destroy_detail w where w.user_id in ( s.member_id  ) ) as destroyCount\n" +
            "\t\t\tfrom li_store s \n" +
            "\t\t\t\t left join w_make_account w  on cast(s.id as char) = w.mer_id ${ew.customSqlSegment} group by s.id  ")
    IPage<StoreAchievementParamsVO>  queryStoreAchievement(IPage<StoreAchievementParamsVO> initPage, @Param(Constants.WRAPPER) QueryWrapper<StoreAchievementParams> queryWrapper);


    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    AdminUser findByUsername(String username);


    /**
     * 通过部门id获取
     * @param departmentId
     * @return
     */
    List<AdminUser> findByDepartmentId(String departmentId);

    /**
     * 通过用户名模糊搜索
     * @param username
     * @param status
     * @return
     */
    List<AdminUser> findByUsernameLikeAndStatus(String username, Integer status);
}
