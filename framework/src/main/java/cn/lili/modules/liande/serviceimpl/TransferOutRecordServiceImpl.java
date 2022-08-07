
package cn.lili.modules.liande.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.context.UserContext;
import cn.lili.modules.liande.entity.dos.RechargeRecord;
import cn.lili.modules.liande.entity.dos.TransferOutRecord;
import cn.lili.modules.liande.entity.dto.TransferDTO;
import cn.lili.modules.liande.mapper.MemberIncomeMapper;
import cn.lili.modules.liande.mapper.RechargeRecordMapper;
import cn.lili.modules.liande.mapper.TransferOutRecordMapper;
import cn.lili.modules.liande.service.ITransferOutRecordService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.mapper.MemberMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 转出明细 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Slf4j
@Service
public class TransferOutRecordServiceImpl extends ServiceImpl<TransferOutRecordMapper, TransferOutRecord> implements ITransferOutRecordService {


    @Value("${lili.contract.address}")
    private String contractAddress;

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    /**
     * 转账
     *
     * @param transfer
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean out(TransferDTO transfer) {
        Member acceptMember = queryMember(transfer.getAcceptAddress());

        Member transferorMember = queryMember(UserContext.getCurrentUser().getMember().getBlockAddress());
        if (transfer.getTransferCount() > acceptMember.getSSD())
            throw new ServiceException(ResultCode.TRANSFER_COUNT_ERROR);

        // 内部转账
        if (ObjectUtils.isNotEmpty(acceptMember)) {
            transferorMember.setSSD(transferorMember.getSSD() - transfer.getTransferCount());
            memberMapper.updateById(transferorMember);

            acceptMember.setSSD(acceptMember.getSSD() + transfer.getTransferCount());
            memberMapper.updateById(acceptMember);
            // 转出
            RechargeRecord recharge = new RechargeRecord(acceptMember.getId(), acceptMember.getBlockAddress(), contractAddress, transfer.getTransferCount());
            rechargeRecordMapper.insert(recharge);
            // 转入
            TransferOutRecord transferOut = new TransferOutRecord(recharge.getOrderNo(), transfer.getAcceptAddress(), contractAddress, transfer.getTransferCount());
            this.save(transferOut);

            return true;
        } else {//外部转账，暂不开发
            return false;
        }
    }


    private Member queryMember(String address) {
        return memberMapper.selectOne(new QueryWrapper<Member>().lambda().eq(Member::getBlockAddress, address)
                .eq(Member::getDeleteFlag, false));
    }
}

