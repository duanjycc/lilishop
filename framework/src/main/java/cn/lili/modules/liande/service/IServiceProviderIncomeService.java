
package cn.lili.modules.liande.service;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.ServiceProviderIncome;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 服务商收益表 服务类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
public interface IServiceProviderIncomeService extends IService<ServiceProviderIncome> {


    /**
     * 根据类型查询  区域 / 子区域 收益
     * @param pageVo
     * @param incomeType
     * @param beginDate
     * @param endDate
     * @return
     */
    IPage<ServiceProviderIncome> areaDetails(PageVO pageVo, String incomeType, String beginDate, String endDate);
}
