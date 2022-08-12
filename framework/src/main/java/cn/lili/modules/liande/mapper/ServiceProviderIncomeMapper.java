
package cn.lili.modules.liande.mapper;

import cn.lili.modules.liande.entity.dos.ServiceProviderIncome;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 服务商收益表 Mapper 接口
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Mapper
public interface ServiceProviderIncomeMapper extends BaseMapper<ServiceProviderIncome> {


    @Select("SELECT t.*,w.mer_name  FROM w_service_provider_income t LEFT JOIN w_make_account w on t.order_id = w.id ${ew.customSqlSegment} ")
    IPage<ServiceProviderIncome> areaDetails(IPage<ServiceProviderIncome> page, @Param(Constants.WRAPPER) Wrapper<ServiceProviderIncome> queryWrapper);
}
