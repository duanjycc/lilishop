/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.service;

import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dos.PendingOrderForm;
import cn.lili.modules.liande.entity.dos.RegionalPromotion;
import cn.lili.modules.liande.entity.dto.RegionalPromotionDTO;
import cn.lili.modules.liande.entity.vo.InvitationUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 区域推广员表 服务类
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
public interface IRegionalPromotionService extends IService<RegionalPromotion> {

    IPage<RegionalPromotion> listOfPromoters(PageVO page);

    ResultMessage<Object> addOfPromoters(RegionalPromotionDTO regionalPromotionDTO);

    ResultMessage<Object> updateOfPromoters(RegionalPromotionDTO regionalPromotionDTO);

    ResultMessage<Object> deleteOfPromoters(RegionalPromotionDTO regionalPromotionDTO);


    /**
     * 根据所选区域查询邀请人
     * @param regionCode
     * @return
     */
    List<InvitationUser> queryInvitationUser(String regionCode);


    /**
     * 检测是否区域推广员
     * @param mobile
     * @return
     */
    boolean checkPromoters(String mobile);
}
