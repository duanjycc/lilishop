/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dto.MakeAccountDTO;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/ui/pendingOrderForm")
public class PendingOrderFormController {


    @ApiOperation(value = "挂单列表")
    @GetMapping("/makeAccount")
    public ResultMessage<Boolean> makeAccount(MakeAccountDTO makeAccountDTO) {


        return ResultUtil.success();
    }
}

