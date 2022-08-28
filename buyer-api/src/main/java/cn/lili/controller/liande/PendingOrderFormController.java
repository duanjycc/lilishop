/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.distribution.entity.dos.DistributionOrder;
import cn.lili.modules.liande.entity.dos.PendingOrderForm;
import cn.lili.modules.liande.entity.dto.MakeAccountDTO;
import cn.lili.modules.liande.entity.dto.PendingOrderFormDTO;
import cn.lili.modules.liande.entity.vo.PendingOrderFormVO;
import cn.lili.modules.liande.service.IPendingOrderFormService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 挂单表 前端控制器
 * </p>
 *
 * @author zh
 * @since 2022-08-22
 */
@RestController
@Api(tags = "挂单")
@RequestMapping("/buyer/pendingOrderForm")
public class PendingOrderFormController {

    @Autowired
    IPendingOrderFormService iPendingOrderFormService;
    @ApiOperation(value = "挂单列表")
    @GetMapping("/listOfPendingOrders")
    public ResultMessage<IPage<PendingOrderForm>>  listOfPendingOrders(PageVO page,String sort,String collation,String business) {

        return ResultUtil.data(iPendingOrderFormService.listOfPendingOrders(page,sort,business,collation));
    }

    @ApiOperation(value = "当前用户挂单信息")
    @GetMapping("/pendingOrderInformation")
    public ResultMessage<PendingOrderForm>  pendingOrderInformation() {

        return ResultUtil.data(iPendingOrderFormService.pendingOrderInformation());
    }

    @ApiOperation(value = "插入挂单")
    @GetMapping("/insertPendingOrder")
    public ResultMessage<Boolean>  insertPendingOrder(PendingOrderFormDTO pendingOrderFormDTO) {

        return ResultUtil.data(iPendingOrderFormService.insertPendingOrder(pendingOrderFormDTO));
    }

    @ApiOperation(value = "修改挂单")
    @GetMapping("/updatePendingOrder")
    public ResultMessage<Boolean>  updatePendingOrder(PendingOrderFormDTO pendingOrderFormDTO) {

        return ResultUtil.data(iPendingOrderFormService.updatePendingOrder(pendingOrderFormDTO));
    }

    @ApiOperation(value = "删除挂单")
    @GetMapping("/deletePendingOrder")
    public ResultMessage<Boolean>  deletePendingOrder() {

        return ResultUtil.data(iPendingOrderFormService.deletePendingOrder());
    }

    @ApiOperation(value = "联系人信息")
    @GetMapping("/contactInformation")
    public ResultMessage<PendingOrderForm>  contactInformation(PendingOrderFormDTO pendingOrderFormDTO) {

        return ResultUtil.data(iPendingOrderFormService.contactInformation(pendingOrderFormDTO));
    }
}

