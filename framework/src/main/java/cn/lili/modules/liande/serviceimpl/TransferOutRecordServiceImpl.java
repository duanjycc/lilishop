
package cn.lili.modules.liande.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.RechargeRecord;
import cn.lili.modules.liande.entity.dos.TransferOutRecord;
import cn.lili.modules.liande.entity.dto.QueryTransferDTO;
import cn.lili.modules.liande.entity.dto.TransferDTO;
import cn.lili.modules.liande.mapper.RechargeRecordMapper;
import cn.lili.modules.liande.mapper.TransferOutRecordMapper;
import cn.lili.modules.liande.service.ITransferOutRecordService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.mapper.MemberMapper;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


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
     * 转出明细
     *
     * @param beginDate
     * @param endDate
     */
    @Override
    public IPage<TransferOutRecord> transferOutDetails(PageVO pageVo,String beginDate,String endDate) {
        AuthUser currentUser = UserContext.getCurrentUser();
        Optional.ofNullable(currentUser).orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_LOGIN));

        QueryWrapper<QueryTransferDTO> queryWrapper = new QueryWrapper();
        queryWrapper.ge("t.recharge_time",beginDate);
        queryWrapper.le("t.recharge_time",endDate);
        queryWrapper.eq("t.user_id",currentUser.getId());
//        queryWrapper.eq("t.receipt_status", DelStatusEnum.USE.getType());
        queryWrapper.orderByDesc("t.recharge_time");

        return baseMapper.transferOutDetails(PageUtil.initPage(pageVo),queryWrapper);
    }

    /**
     * 转入明细
     *
     * @param pageVo
     * @param beginDate
     * @param endDate
     */
    @Override
    public IPage<RechargeRecord> transferInDetails(PageVO pageVo, String beginDate,String endDate) {
        AuthUser currentUser = UserContext.getCurrentUser();
        Optional.ofNullable(currentUser).orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_LOGIN));

        QueryWrapper<QueryTransferDTO> queryWrapper = new QueryWrapper();
        queryWrapper.ge("t.recharge_time",beginDate);
        queryWrapper.le("t.recharge_time",endDate);
        queryWrapper.eq("t.user_id",currentUser.getId());
//        queryWrapper.eq("t.recharge_status", DelStatusEnum.USE.getType());
        queryWrapper.orderByDesc("t.recharge_time");

        return rechargeRecordMapper.transferInDetails(PageUtil.initPage(pageVo),queryWrapper);
    }

    /**
     * 转账
     *
     * @param transfer
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean accounts(TransferDTO transfer) {
        Member acceptMember = queryMember(transfer.getAcceptAddress());
        Member transferorMember = queryMember(UserContext.getCurrentUser().getMember().getBlockAddress());

        if (!new BCryptPasswordEncoder().matches(transfer.getSecondPassword(), transferorMember.getPaymentPassword()))
            throw new ServiceException(ResultCode.TRANSFER_SECOND_PASSWORD_ERROR);

        if (transfer.getTransferCount() > transferorMember.getSSD())
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

