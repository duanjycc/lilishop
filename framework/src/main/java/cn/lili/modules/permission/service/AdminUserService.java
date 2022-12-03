package cn.lili.modules.permission.service;


import cn.lili.common.security.token.Token;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dto.SearchAchievementParams;
import cn.lili.modules.liande.entity.dto.ServiceProviderParams;
import cn.lili.modules.liande.entity.dto.SignInDTO;
import cn.lili.modules.liande.entity.dto.StoreAchievementParams;
import cn.lili.modules.liande.entity.vo.*;
import cn.lili.modules.permission.entity.dos.AdminUser;
import cn.lili.modules.permission.entity.dto.AdminUserDTO;
import cn.lili.modules.permission.entity.vo.AdminUserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户业务层
 *
 * @author Chopper
 * @since 2020/11/17 3:42 下午
 */
@CacheConfig(cacheNames = "{adminuser}")
public interface AdminUserService extends IService<AdminUser> {


    /**
     * 根据区域id获取区域列表以及上级区域列表
     * @param areaId
     * @return
     */
    ServiceRegionVO getRegion(String areaId);

    /**
     * 根据区域id获取区域列表以及上级区域列表
     * @param areaId
     * @return
     */
    ServiceRegionVO getCityPath(String areaId);

    /**
     * 检测区域是否被签约
     * @param areaId
     * @return
     */
    Boolean checkAreaHavSign(String areaId);

    /**
     * 检测区域是否被签约
     * @param areaId
     * @return
     */
    ServiceRegionVO checksAreaHavSign(String areaId);

    /**
     * 检测上级区域是否被签约
     * @param areaId
     * @return
     */
    ServiceRegionVO checkpAreaHavSign(String areaId);

    /**
     * 服务商业绩
     * @param mobile
     * @return
     */
    AchievementVO queryAchievement(String mobile);
    /**
     * 服务商业绩-left
     * @param params
     * @return
     */
    SearchAchievementVO queryStoreAchievementLeft(SearchAchievementParams params);

    /**
     * 获取服务商后端管理分页
     *
     * @param initPage
     * @param params
     * @return
     */
    IPage<ServiceProviderParamsVO> queryServiceProvider(Page initPage ,ServiceProviderParams params);

    /**
     * 获取服务商所属店铺业绩后端管理分页
     *
     * @param initPage
     * @param params
     * @return
     */
    IPage<StoreAchievementParamsVO> queryStoreAchievement(Page initPage , StoreAchievementParams params);


    /**
     * 服务商管理-签约
     * @param signInDTO
     */

    void signIn(SignInDTO signInDTO);
    /**
     * 服务商管理-修改
     * @param signInDTO
     */

    void update(SignInDTO signInDTO);
    /**
     * 服务商管理-删除签约
     * @param id
     */
    void deleteSignIn(String id);

    /**
     * 获取管理员分页
     *
     * @param initPage
     * @param initWrapper
     * @return
     */
    IPage<AdminUserVO> adminUserPage(Page initPage, QueryWrapper<AdminUser> initWrapper);

    /**
     * 通过用户名获取用户
     *
     * @param username
     * @return
     */
    AdminUser findByUsername(String username);
    /**
     * 通过手机号获取用户
     *
     * @param mobile
     * @return
     */
    AdminUser findByMobile(String mobile);


    /**
     * 更新管理员
     *
     * @param adminUser
     * @param roles
     * @return
     */
    boolean updateAdminUser(AdminUser adminUser, List<String> roles);


    /**
     * 修改管理员密码
     *
     * @param password
     * @param newPassword
     */
    void editPassword(String password, String newPassword);

    /**
     * 重置密码
     *
     * @param ids id集合
     */
    void resetPassword(List<String> ids);

    /**
     * 新增管理员
     *
     * @param adminUser
     * @param roles
     */
    void saveAdminUser(AdminUserDTO adminUser, List<String> roles);

    /**
     * 彻底删除
     *
     * @param ids
     */
    void deleteCompletely(List<String> ids);


    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    Token login(String username, String password);

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return token
     */
    Token refreshToken(String refreshToken);

}
