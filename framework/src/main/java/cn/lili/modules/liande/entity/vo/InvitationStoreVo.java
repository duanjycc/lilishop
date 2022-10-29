package cn.lili.modules.liande.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 *
 */
@Data
@ApiModel(value = "APP我邀请的商铺")
public class InvitationStoreVo {

    private String storeName;
    private String memberName;
    private String storeAddressDetail;
    private Double sumSurrenderPrice;
}
