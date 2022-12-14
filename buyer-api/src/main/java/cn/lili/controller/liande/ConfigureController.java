package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.service.IConfigureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/buyer/configure")
@Api(tags = "获取配置")
public class ConfigureController {

    @Autowired
    private IConfigureService configureService;

    @ApiOperation(value = "根据类型获取配置")
    @GetMapping("/queryConfigureByType")
    public ResultMessage<Object> queryConfigureByType(@RequestParam String type,@RequestParam String blockAddress) {
        return ResultUtil.data(configureService.queryConfigureByType(type ,blockAddress));
    }
}
