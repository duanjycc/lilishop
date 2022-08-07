
package cn.lili.modules.liande.service;

import cn.lili.common.security.context.UserContext;
import cn.lili.modules.liande.entity.dos.MakeAccount;
import cn.lili.modules.liande.entity.dto.MakeAccountDTO;
import cn.lili.modules.liande.entity.vos.MakeAccountVOS;
import cn.lili.modules.member.entity.dos.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 做单明细表 服务类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
public interface IMakeAccountService extends IService<MakeAccount> {


    MakeAccountVOS makeAccount(MakeAccountDTO makeAccountDTO);
}
