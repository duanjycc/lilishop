package cn.lili.controller.liande;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dto.ServiceProviderParams;
import cn.lili.modules.liande.entity.dto.SignInDTO;
import cn.lili.modules.liande.entity.dto.StoreAchievementParams;
import cn.lili.modules.liande.entity.vo.AchievementVO;
import cn.lili.modules.liande.entity.vo.ServiceProviderParamsVO;
import cn.lili.modules.liande.entity.vo.ServiceRegionVO;
import cn.lili.modules.liande.entity.vo.StoreAchievementParamsVO;
import cn.lili.modules.permission.service.AdminUserService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 * 服务商管理
 */
@Slf4j
@RestController
@Api(tags = "管理端,服务商管理")
@RequestMapping("/manager/service/provider")
public class ServiceProviderController {

    @Autowired
    private AdminUserService userService;


    @ApiOperation(value = "查询服务商业")
    @GetMapping(value = "/achievement/{mobile}")
    public ResultMessage<AchievementVO> queryAchievement(@PathVariable String mobile) {
        return ResultUtil.data(userService.queryAchievement(mobile));
    }

    @ApiOperation(value = "根据区域id获取区域列表以及上级区域列表")
    @GetMapping(value = "/region/{areaId}")
    public ResultMessage<ServiceRegionVO> getRegion(@PathVariable String areaId) {
        return ResultUtil.data(userService.getRegion(areaId));
    }

    @ApiOperation(value = "检测区域是否被签约")
    @GetMapping(value = "/check/{areaId}")
    public ResultMessage<Object> checkAreaHavSign(@PathVariable String areaId) {
        return ResultUtil.data(userService.checkAreaHavSign(areaId));
    }

    @ApiOperation(value = "查询服务商业绩")
    @GetMapping(value = "/store/achievement")
    public ResultMessage<IPage<StoreAchievementParamsVO>> queryStoreAchievement(StoreAchievementParams params,
                                                                                PageVO pageVo) {
        IPage<StoreAchievementParamsVO> page = userService.queryStoreAchievement(PageUtil.initPage(pageVo), params);
        return ResultUtil.data(page);
    }

    @ApiOperation(value = "查询服务商列表分页")
    @GetMapping
    public ResultMessage<IPage<ServiceProviderParamsVO>> queryServiceProvider(ServiceProviderParams params,
                                                        PageVO pageVo) {
        IPage<ServiceProviderParamsVO> page = userService.queryServiceProvider(PageUtil.initPage(pageVo), params);
        return ResultUtil.data(page);
    }


    @PostMapping
    @ApiOperation(value = "签约")
    public ResultMessage<Object> signIn(@Valid SignInDTO signInDTO) {
        userService.signIn(signInDTO);
        return ResultUtil.success();
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public ResultMessage<Object> update(@Valid SignInDTO signInDTO) {
        userService.update(signInDTO);
        return ResultUtil.success();
    }

    @ApiOperation(value = "删除签约")
    @DeleteMapping(value = "/delete/{id}")
    public ResultMessage<Object> deleteSignIn(@PathVariable String id) {
        userService.deleteSignIn(id);
        return ResultUtil.success();
    }
}
