
package cn.lili.modules.liande.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.DateUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.MemberIncome;
import cn.lili.modules.liande.entity.dos.ServiceProviderIncome;
import cn.lili.modules.liande.mapper.ServiceProviderIncomeMapper;
import cn.lili.modules.liande.service.IServiceProviderIncomeService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

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


    /**
     * 根据类型查询  区域 / 子区域 收益
     *
     * @param pageVo
     * @param incomeType
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public IPage<ServiceProviderIncome> areaDetails(PageVO pageVo, String incomeType, String beginDate, String endDate) {
        AuthUser currentUser = UserContext.getCurrentUser();
        Optional.ofNullable(currentUser).orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_LOGIN));

        QueryWrapper<ServiceProviderIncome> queryWrapper = new QueryWrapper();
        queryWrapper.ge(ObjectUtils.isNotEmpty(beginDate),"t.creation_time",beginDate + DateUtil.DATA_PREFIX);
        queryWrapper.le(ObjectUtils.isNotEmpty(endDate),"t.creation_time",endDate + DateUtil.DATA_SUFFIX);
        queryWrapper.eq("t.Income_type",incomeType);
        queryWrapper.eq("t.user_id",currentUser.getId());
//        queryWrapper.eq("t.receipt_status", DelStatusEnum.USE.getType());
        queryWrapper.orderByDesc("t.recharge_time");

        return baseMapper.areaDetails(PageUtil.initPage(pageVo),queryWrapper);
    }
}
