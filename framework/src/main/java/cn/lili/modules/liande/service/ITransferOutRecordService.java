
package cn.lili.modules.liande.service;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.RechargeRecord;
import cn.lili.modules.liande.entity.dos.TransferOutRecord;
import cn.lili.modules.liande.entity.dto.QueryTransferDTO;
import cn.lili.modules.liande.entity.dto.TransferDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



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
     * @param dto
     */
    IPage<TransferOutRecord> transferOutDetails(PageVO pageVo,QueryTransferDTO dto);

    /**
     * 转入明细
     * @param dto
     */
    IPage<RechargeRecord> transferInDetails(PageVO pageVo, QueryTransferDTO dto);
}
