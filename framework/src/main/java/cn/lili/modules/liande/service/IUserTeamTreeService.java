
package cn.lili.modules.liande.service;

import cn.lili.modules.liande.entity.dos.UserTeamTree;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
public interface IUserTeamTreeService extends IService<UserTeamTree> {


    /**
     * 查询我邀请的人
     * @param phone 登陆人手机号
     * @return
     */
    List<String> queryInvitationRegin(String phone);
}
