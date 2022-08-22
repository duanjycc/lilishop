/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.serviceimpl;

import cn.lili.modules.liande.entity.dos.PendingOrderForm;
import cn.lili.modules.liande.mapper.PendingOrderFormMapper;
import cn.lili.modules.liande.service.IPendingOrderFormService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 挂单表 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@Slf4j
@Service
public class PendingOrderFormServiceImpl extends ServiceImpl<PendingOrderFormMapper, PendingOrderForm> implements IPendingOrderFormService {

}
