/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.service;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.liande.entity.dos.MemberIncome;
import cn.lili.modules.liande.entity.dos.PendingOrderForm;
import cn.lili.modules.liande.entity.dto.PendingOrderFormDTO;
import cn.lili.modules.liande.entity.vo.PendingOrderFormVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 挂单表 服务类
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
public interface IPendingOrderFormService extends IService<PendingOrderForm> {

    IPage<PendingOrderForm>  listOfPendingOrders(PageVO page,String sort,String business);

    PendingOrderForm pendingOrderInformation();

    Boolean insertPendingOrder(PendingOrderFormDTO pendingOrderFormDTO);

    Boolean updatePendingOrder(PendingOrderFormDTO pendingOrderFormDTO);

    Boolean deletePendingOrder();

    PendingOrderForm contactInformation(PendingOrderFormDTO pendingOrderFormDTO);
}
