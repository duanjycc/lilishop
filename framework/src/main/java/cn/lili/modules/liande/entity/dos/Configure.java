/*
 * Copyright © 2022- ~ hc R&D 电信支撑部/产品研发中心 All rights reserved.
 */
package cn.lili.modules.liande.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_configure")
@ApiModel(value = "Configure对象", description = "")
public class Configure extends Model<Configure> {

    private static final long serialVersionUID = 1L;


    /**
     * 组建ID
     */
    @ApiModelProperty(value = "组建ID")
    private Long id;


    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;


    /**
     * 数值
     */
    @ApiModelProperty(value = "数值")
    private Double numericalAlue;


    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String explain;

}
