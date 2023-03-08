package cn.lili.modules.payment.kit.plugin.wallet;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.liande.entity.dos.Configure;
import cn.lili.modules.liande.mapper.MemberIncomeMapper;
import cn.lili.modules.liande.service.IConfigureService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.mapper.MemberMapper;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.service.OrderService;
import cn.lili.modules.payment.entity.RefundLog;
import cn.lili.modules.payment.entity.enums.CashierEnum;
import cn.lili.modules.payment.entity.enums.PaymentMethodEnum;
import cn.lili.modules.payment.kit.CashierSupport;
import cn.lili.modules.payment.kit.Payment;
import cn.lili.modules.payment.kit.dto.PayParam;
import cn.lili.modules.payment.kit.dto.PaymentSuccessParams;
import cn.lili.modules.payment.kit.params.dto.CashierParam;
import cn.lili.modules.payment.service.PaymentService;
import cn.lili.modules.payment.service.RefundLogService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.mapper.StoreMapper;
import cn.lili.modules.wallet.entity.dto.MemberWalletUpdateDTO;
import cn.lili.modules.wallet.entity.enums.DepositServiceTypeEnum;
import cn.lili.modules.wallet.service.MemberWalletService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.lili.modules.liande.entity.dos.MemberIncome;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * WalletPlugin
 *
 * @author Chopper
 * @version v1.0
 * 2021-02-20 10:14
 */
@Slf4j
@Component
public class WalletPlugin implements Payment {

    /**
     * 支付日志
     */
    @Autowired
    private PaymentService paymentService;
    /**
     * 退款日志
     */
    @Autowired
    private RefundLogService refundLogService;
    /**
     * 会员余额
     */
    @Autowired
    private MemberWalletService memberWalletService;
    /**
     * 收银台
     */
    @Autowired
    private CashierSupport cashierSupport;

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private IConfigureService iConfigureService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private
    OrderService orderService;

    @Autowired
    MemberIncomeMapper memberIncomeMapper;

    @Override
    public ResultMessage<Object> h5pay(HttpServletRequest request, HttpServletResponse response, PayParam payParam) {
        savePaymentLog(payParam);
        return ResultUtil.success(ResultCode.PAY_SUCCESS);
    }

    @Override
    public ResultMessage<Object> jsApiPay(HttpServletRequest request, PayParam payParam) {
        savePaymentLog(payParam);
        return ResultUtil.success(ResultCode.PAY_SUCCESS);
    }

    @Override
    public ResultMessage<Object> appPay(HttpServletRequest request, PayParam payParam) {
        savePaymentLog(payParam);
        return ResultUtil.success(ResultCode.PAY_SUCCESS);
    }

    @Override
    public ResultMessage<Object> nativePay(HttpServletRequest request, PayParam payParam) {
        if (payParam.getOrderType().equals(CashierEnum.RECHARGE.name())) {
            throw new ServiceException(ResultCode.CAN_NOT_RECHARGE_WALLET);
        }
        savePaymentLog(payParam);
        return ResultUtil.success(ResultCode.PAY_SUCCESS);
    }

    @Override
    public ResultMessage<Object> mpPay(HttpServletRequest request, PayParam payParam) {

        savePaymentLog(payParam);
        return ResultUtil.success(ResultCode.PAY_SUCCESS);
    }

    /**
     * 保存支付日志
     *
     * @param payParam 支付参数
     */
    private void savePaymentLog(PayParam payParam) {
        //同一个会员如果在不同的客户端使用预存款支付，会存在同时支付，无法保证预存款的正确性，所以对会员加锁
        RLock lock = redisson.getLock(UserContext.getCurrentUser().getId() + "");
        lock.lock();
        try {
            //获取支付收银参数
            CashierParam cashierParam = cashierSupport.cashierParam(payParam);
            this.callBack(payParam, cashierParam);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void refund(RefundLog refundLog) {
        try {
            memberWalletService.increase(new MemberWalletUpdateDTO(refundLog.getTotalAmount(),
                    refundLog.getMemberId(),
                    "订单[" + refundLog.getOrderSn() + "]，售后单[" + refundLog.getAfterSaleNo() + "]，退还金额[" + refundLog.getTotalAmount() + "]",
                    DepositServiceTypeEnum.WALLET_REFUND.name()));
            refundLog.setIsRefund(true);
            refundLogService.save(refundLog);
        } catch (Exception e) {
            log.error("退款失败", e);
        }
    }

    /**
     * 支付订单
     *
     * @param payParam     支付参数
     * @param cashierParam 收银台参数
     */
    public void callBack(PayParam payParam, CashierParam cashierParam) {

        //支付信息
        try {
            if (UserContext.getCurrentUser() == null) {
                throw new ServiceException(ResultCode.USER_NOT_LOGIN);
            }
            Order order = orderService.getBySn(cashierParam.getOrderSns());

//            boolean result = memberWalletService.reduce(new MemberWalletUpdateDTO(
//                            cashierParam.getPrice(),
//                            UserContext.getCurrentUser().getId(),
//                            "订单[" + cashierParam.getOrderSns() + "]支付金额[" + cashierParam.getPrice() + "]",
//                            DepositServiceTypeEnum.WALLET_PAY.name()
//                    )
//            );
            //价格
            QueryWrapper<Configure> jgWrapper = new QueryWrapper();
            jgWrapper.eq("type", "unitPrice");
            Configure jg = iConfigureService.getOne(jgWrapper);

            QueryWrapper<Member> memberpassWrapper = new QueryWrapper();
            memberpassWrapper.eq("id", UserContext.getCurrentUser().getId());
            Member memberpass = memberMapper.selectOne(memberpassWrapper);

            //计算需要卷的数量
            Double wantPrice = jg.getNumericalAlue();
            Double wantsum = cashierParam.getPrice() / wantPrice;
            if (wantsum > memberpass.getSsd()) {
                throw new ServiceException(ResultCode.INSUFFICIENT_QUANTITY_ERROR);
            }

            //会员扣除一笔ssd
            QueryWrapper<Member> huiyuanWrapper = new QueryWrapper();
            huiyuanWrapper.eq("id", UserContext.getCurrentUser().getId());
            Member huiyuanMember = memberMapper.selectOne(huiyuanWrapper);
            huiyuanMember.setSsd(memberpass.getSsd() - wantsum);
            memberMapper.update(huiyuanMember, huiyuanWrapper);

            Double sjwantssd = 0.00;
            Double ptwantssd = 0.00;
            if (StringUtils.isNotEmpty(order.getShouxufei())) {
                sjwantssd = wantsum * (1- Double.valueOf(order.getShouxufei()));
                ptwantssd = wantsum - sjwantssd;
            } else {
                sjwantssd = wantsum;
            }
            //商家得到一笔ssd
            QueryWrapper<Store> storeWrapper = new QueryWrapper();
            storeWrapper.eq("id", order.getStoreId());
            Store store = storeMapper.selectOne(storeWrapper);
            QueryWrapper<Member> shangjWrapper = new QueryWrapper();
            shangjWrapper.eq("id", store.getMemberId());
            Member sjMember = memberMapper.selectOne(shangjWrapper);
            sjMember.setSsd(sjMember.getSsd() + sjwantssd);
            memberMapper.update(sjMember, shangjWrapper);

            String shouxfId = null;
            if (StringUtils.isNotEmpty(order.getShouxufei())) {
                jgWrapper = new QueryWrapper();
                jgWrapper.eq("type", "shouxufei");
                Configure jgsxuf = iConfigureService.getOne(jgWrapper);
                //平台货得手续费
                QueryWrapper<Member> pingtaiWrapper = new QueryWrapper();
                shouxfId = jgsxuf.getRemark();
                pingtaiWrapper.eq("id", jgsxuf.getRemark());
                Member pingtaiMember = memberMapper.selectOne(pingtaiWrapper);

                pingtaiMember.setSsd(pingtaiMember.getSsd() + ptwantssd);

                memberMapper.update(pingtaiMember, pingtaiWrapper);
            }

            //会员消费SSD日志
            MemberIncome mi = new MemberIncome();
            mi.setConsumerUserid(Long.parseLong(UserContext.getCurrentUser().getId()));
            mi.setUserId(Long.parseLong( UserContext.getCurrentUser().getId()));
            mi.setCreationTime(new Date());
            mi.setQuantity(wantsum);
            mi.setIncomeProportion(cashierParam.getPrice() + "");
            mi.setOrderId(order.getSn() + "");
            mi.setIncomeType("3");
            memberIncomeMapper.insert(mi);
            //店家获取SSD日志
            mi = new MemberIncome();
            mi.setConsumerUserid(Long.parseLong(UserContext.getCurrentUser().getId()));
            mi.setUserId(Long.parseLong( store.getMemberId()));
            mi.setCreationTime(new Date());
            mi.setQuantity(sjwantssd);
            mi.setIncomeProportion(cashierParam.getPrice() + "");
            mi.setOrderId(order.getSn() + "");
            mi.setIncomeType("4");
            memberIncomeMapper.insert(mi);

            //平台获取SSD日志
            if (StringUtils.isNotEmpty(order.getShouxufei())) {
                mi = new MemberIncome();
                mi.setConsumerUserid(Long.parseLong(UserContext.getCurrentUser().getId()));
                mi.setUserId(Long.parseLong(shouxfId));
                mi.setCreationTime(new Date());
                mi.setQuantity(ptwantssd);
                mi.setIncomeProportion(cashierParam.getPrice() + "");
                mi.setOrderId(order.getSn() + "");
                mi.setIncomeType("5");
                memberIncomeMapper.insert(mi);
            }

            if (true) {
                try {
                    PaymentSuccessParams paymentSuccessParams = new PaymentSuccessParams(
                            PaymentMethodEnum.WALLET.name(),
                            "",
                            cashierParam.getPrice(),
                            payParam
                    );

                    paymentService.success(paymentSuccessParams);
                    log.info("支付回调通知：余额支付：{}", payParam);
                } catch (ServiceException e) {
                    //业务异常，则支付手动回滚
                    memberWalletService.increase(new MemberWalletUpdateDTO(
                            cashierParam.getPrice(),
                            UserContext.getCurrentUser().getId(),
                            "订单[" + cashierParam.getOrderSns() + "]支付异常，余额返还[" + cashierParam.getPrice() + "]",
                            DepositServiceTypeEnum.WALLET_REFUND.name())
                    );
                    throw e;
                }
            } else {
                throw new ServiceException(ResultCode.WALLET_INSUFFICIENT);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.info("余额支付异常", e);
            throw new ServiceException(ResultCode.WALLET_INSUFFICIENT);
        }

    }

}
