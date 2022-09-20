/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("li_service_log")
@ApiModel(value = "ServiceLog对象", description = "")
public class ServiceLog extends Model<ServiceLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "类型")
    private String context;

    @ApiModelProperty(value = "说明")
    private Date createTime;

    public ServiceLog(String areaId,String areaName,String memberId,String memberName){
        this.context = "签约【id/用户名称】【"+ memberId +" /"+  memberName+"】，签约【区域id/区域名称】：【" + areaId +"/"+ areaName+"】";
        this.createTime = new Date();
    }

}
