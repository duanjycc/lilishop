package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dto.ServiceProviderParams;
import cn.lili.modules.liande.entity.vo.ServiceProviderParamsVO;
import cn.lili.modules.permission.service.AdminUserService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 服务商管理
 */
@RestController
@Api(tags = "管理端,服务商管理")
@RequestMapping("/manager/service/provider")
public class ServiceProviderController {

    @Autowired
    private AdminUserService userService;


    @ApiOperation(value = "查询服务商列表分页")
    @GetMapping
    public ResultMessage<IPage<ServiceProviderParamsVO>> queryServiceProvider(ServiceProviderParams params,
                                                        PageVO pageVo) {
        IPage<ServiceProviderParamsVO> page = userService.queryServiceProvider(PageUtil.initPage(pageVo), params);
        return ResultUtil.data(page);
    }
}
