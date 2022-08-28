/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.mapper;

import cn.lili.modules.liande.entity.dos.PendingOrderForm;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * <p>
 * 挂单表 Mapper 接口
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@Mapper
public interface PendingOrderFormMapper extends BaseMapper<PendingOrderForm> {
    @Select("SELECT * FROM pending_order_form ${ew.customSqlSegment}")
    IPage<PendingOrderForm> listOfPendingOrders(Page<Object> initPage, @Param(Constants.WRAPPER) Wrapper<PendingOrderForm> queryWrapper);
}
