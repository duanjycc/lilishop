package cn.lili.modules.permission.mapper;

import cn.lili.modules.liande.entity.dto.ServiceProviderParams;
import cn.lili.modules.liande.entity.vo.ServiceProviderParamsVO;
import cn.lili.modules.permission.entity.dos.AdminUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

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
    IPage<ServiceProviderParamsVO>  queryServiceProvider(IPage<ServiceProviderParamsVO> initPage, @Param(Constants.WRAPPER) QueryWrapper<ServiceProviderParams> queryWrapper);


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
