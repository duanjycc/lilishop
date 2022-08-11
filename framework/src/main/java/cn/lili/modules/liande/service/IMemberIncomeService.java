
package cn.lili.modules.liande.service;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.MemberIncome;
import cn.lili.modules.liande.entity.dos.TransferOutRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员收益表 服务类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
public interface IMemberIncomeService extends IService<MemberIncome> {

    /**
     * 会员收益
     * @param pageVo
     * @param beginDate
     * @param endDate
     */
    IPage<MemberIncome> memberDetails(PageVO pageVo, String beginDate, String endDate);
}
