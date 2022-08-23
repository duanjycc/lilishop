/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.mapper;

import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dos.RegionalPromotion;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 区域推广员表 Mapper 接口
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@Mapper
public interface RegionalPromotionMapper extends BaseMapper<RegionalPromotion> {
    @Select("SELECT * from regional_promotion")
   IPage<RegionalPromotion> listOfPromoters(Page<Object> initPage, @Param(Constants.WRAPPER)QueryWrapper<RegionalPromotion> queryWrapper);
}
