/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.serviceimpl;

import cn.lili.modules.liande.entity.dos.Configure;
import cn.lili.modules.liande.mapper.ConfigureMapper;
import cn.lili.modules.liande.service.IConfigureService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Slf4j
@Service
public class ConfigureServiceImpl extends ServiceImpl<ConfigureMapper, Configure> implements IConfigureService {

    /**
     * 获取SSD单价
     * @return
     */
    @Override
    public Double querySssUnitPrice() {
        Configure unitPrice = baseMapper.selectOne(new QueryWrapper<Configure>().lambda().eq(Configure::getType, "unitPrice"));
        return unitPrice.getNumericalAlue();
    }
}
