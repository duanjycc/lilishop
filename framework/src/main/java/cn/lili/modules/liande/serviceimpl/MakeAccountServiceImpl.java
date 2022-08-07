
package cn.lili.modules.liande.serviceimpl;

import cn.lili.modules.liande.entity.dos.MakeAccount;
import cn.lili.modules.liande.entity.dto.MakeAccountDTO;
import cn.lili.modules.liande.entity.vos.MakeAccountVOS;
import cn.lili.modules.liande.mapper.MakeAccountMapper;
import cn.lili.modules.liande.service.IMakeAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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

    @Override
    public MakeAccountVOS makeAccount(MakeAccountDTO makeAccountDTO) {
        return null;
    }
}
