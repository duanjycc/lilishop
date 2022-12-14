package cn.lili.controller.store;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.entity.vos.StoreGoodsLabelVO;
import cn.lili.modules.goods.service.StoreGoodsLabelService;
import cn.lili.modules.statistics.entity.dto.StatisticsQueryParam;
import cn.lili.modules.statistics.entity.vo.StoreStatisticsDataVO;
import cn.lili.modules.store.entity.dto.*;
import cn.lili.modules.store.entity.vos.*;
import cn.lili.modules.store.service.StoreDetailService;
import cn.lili.modules.store.service.StoreService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 买家端,店铺接口
 *
 * @author Bulbasaur
 * @since 2020/11/17 2:32 下午
 */
@RestController
@RequestMapping("/buyer/store/store")
@Api(tags = "买家端,店铺接口")
public class StoreBuyerController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreGoodsLabelService storeGoodsLabelService;
    @Autowired
    private StoreDetailService storeDetailService;

    @ApiOperation(value = "app获取店铺列表分页")
    @GetMapping("/getAppByPage")
    public ResultMessage<IPage<StoreVO>> getAppByPage(StoreSearchParams entity, PageVO page) {
        return ResultUtil.data(storeService.getAppByPage(entity, page));
    }


    @ApiOperation(value = "APP申请商铺-入驻店铺")
    @PostMapping(value = "/settleIn")
    public ResultMessage<Object> settleIn(AppStoreSettleDTO dto) {
        if (StringUtils.isNotEmpty(dto.getStoreId())){
            return ResultUtil.data(storeService.settleInUpdate(dto));
        }else{
            return ResultUtil.data(storeService.settleIn(dto));
        }
    }

    @ApiOperation(value = "app审核")
    @GetMapping("/audit/{id}/{pass}")
    public ResultMessage<Object> audit(@NotNull @PathVariable String id,@NotNull @PathVariable Integer pass) {
        storeService.audit(id, pass);
        return ResultUtil.success();
    }


    @ApiOperation(value = "app驳回")
    @GetMapping("/reject/{id}")
    public ResultMessage<Object> reject(@NotNull @PathVariable String id) {
        storeService.reject(id);
        return ResultUtil.success();
    }


    @ApiOperation(value = "获取店铺列表分页")
    @GetMapping("/getMakeByPage")
    public ResultMessage<IPage<StoreVO>> getMakeByPage(StoreSearchParams entity, PageVO page) {
        entity.setMemberId(UserContext.getCurrentUser().getId());
        return ResultUtil.data(storeService.findMakeByConditionPage(entity, page));
    }


    @ApiOperation(value = "通过商品分类获取店铺")
    @GetMapping(value = "/by/category")
    public ResultMessage<IPage<StoreVO>> listStoreByCategory(AppStoreSearchParams params, PageVO page) {
        return ResultUtil.data(storeService.listStoreByCategory(params,page));
    }

    @ApiOperation(value = "获取店铺列表分页")
    @GetMapping
    public ResultMessage<IPage<StoreVO>> getByPage(StoreSearchParams entity, PageVO page) {
        entity.setMemberId(UserContext.getCurrentUser().getId());
        return ResultUtil.data(storeService.findByConditionPage(entity, page));
    }

    @ApiOperation(value = "通过id获取店铺信息")
    @ApiImplicitParam(name = "id", value = "店铺ID", required = true, paramType = "path")
    @GetMapping(value = "/get/detail/{id}")
    public ResultMessage<StoreDetailVO> detail(@NotNull @PathVariable String id) {
        return ResultUtil.data(storeDetailService.getStoreDetailVO(id));
    }

    @ApiOperation(value = "通过id获取店铺详细信息-营业执照")
    @ApiImplicitParam(name = "id", value = "店铺ID", required = true, paramType = "path")
    @GetMapping(value = "/get/licencePhoto/{id}")
    public ResultMessage<StoreOtherVO> licencePhoto(@NotNull @PathVariable String id) {
        return ResultUtil.data(storeDetailService.getStoreOtherVO(id));
    }


    @ApiOperation(value = "通过id获取店铺商品分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "店铺ID", required = true, paramType = "path")
    })
    @GetMapping(value = "/label/get/{id}")
    public ResultMessage<List<StoreGoodsLabelVO>> storeGoodsLabel(@NotNull @PathVariable String id) {
        return ResultUtil.data(storeGoodsLabelService.listByStoreId(id));
    }

    @ApiOperation(value = "申请店铺第一步-填写企业信息")
    @PutMapping(value = "/apply/first")
    public ResultMessage<Object> applyFirstStep(StoreCompanyDTO storeCompanyDTO) {
        storeService.applyFirstStep(storeCompanyDTO);
        return ResultUtil.success();
    }

    @ApiOperation(value = "申请店铺第二步-填写银行信息")
    @PutMapping(value = "/apply/second")
    public ResultMessage<Object> applyFirstStep(StoreBankDTO storeBankDTO) {
        storeService.applySecondStep(storeBankDTO);
        return ResultUtil.success();
    }

    @ApiOperation(value = "申请店铺第三步-填写其他信息")
    @PutMapping(value = "/apply/third")
    public ResultMessage<Object> applyFirstStep(StoreOtherInfoDTO storeOtherInfoDTO) {
        storeService.applyThirdStep(storeOtherInfoDTO);
        return ResultUtil.success();
    }

    @ApiOperation(value = "获取当前登录会员的店铺信息-入驻店铺")
    @GetMapping(value = "/apply")
    public ResultMessage<StoreDetailVO> apply() {
        return ResultUtil.data(storeDetailService.getStoreDetailVOByMemberId(UserContext.getCurrentUser().getId()));
    }

    @ApiOperation(value = " 商家前十的")
    @GetMapping(value = "/getStoreStatisticsTop")
    public ResultMessage<List<StoreStatisticsDataVO>> getStoreStatisticsTop(StatisticsQueryParam statisticsQueryParam) {
        return ResultUtil.data(storeService.getStoreStatisticsTop(statisticsQueryParam));
    }
}
