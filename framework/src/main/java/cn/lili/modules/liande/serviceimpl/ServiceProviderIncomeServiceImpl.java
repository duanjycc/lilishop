
package cn.lili.modules.liande.serviceimpl;

import cn.lili.modules.liande.entity.dos.ServiceProviderIncome;
import cn.lili.modules.liande.mapper.ServiceProviderIncomeMapper;
import cn.lili.modules.liande.service.IServiceProviderIncomeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务商收益表 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Slf4j
@Service
public class ServiceProviderIncomeServiceImpl extends ServiceImpl<ServiceProviderIncomeMapper, ServiceProviderIncome> implements IServiceProviderIncomeService {

}
