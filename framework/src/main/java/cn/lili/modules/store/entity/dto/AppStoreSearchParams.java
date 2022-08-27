package cn.lili.modules.store.entity.dto;

import cn.lili.common.vo.PageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * App商铺查询
 *
 * @author Chopper
 * @since 2021/3/17 6:08 下午
 */
@Data
public class AppStoreSearchParams extends PageVO {

    @ApiModelProperty(value = "商铺分类ID")
    private String categoryId;
    @ApiModelProperty(value = "商品名称")
    private String storeName;

}
