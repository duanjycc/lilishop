
package cn.lili.modules.liande.service;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.RechargeRecord;
import cn.lili.modules.liande.entity.dos.TransferOutRecord;
import cn.lili.modules.liande.entity.dto.QueryTransferDTO;
import cn.lili.modules.liande.entity.dto.TransferDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;


/**
 * <p>
 * 转出明细 服务类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
public interface ITransferOutRecordService extends IService<TransferOutRecord> {

    /**
     * 转账
     * @param transfer
     */
    Boolean accounts(TransferDTO transfer);

    /**
     * 转出明细
     * @param pageVo
     * @param beginDate
     * @param endDate
     */
    IPage<TransferOutRecord> transferOutDetails(PageVO pageVo, String beginDate, String endDate);

    /**
     * 转入明细
     * @param pageVo
     * @param beginDate
     * @param endDate
     */
    IPage<RechargeRecord> transferInDetails(PageVO pageVo, String beginDate,String endDate);
}
