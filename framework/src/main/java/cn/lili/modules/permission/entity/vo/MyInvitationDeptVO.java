package cn.lili.modules.permission.entity.vo;

import cn.lili.modules.permission.entity.dos.Department;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门VO
 *
 * @author Chopper
 * @since 2020-11-23 18:48
 */
@Data
public class MyInvitationDeptVO extends Department {
    @ApiModelProperty(value = "父级名称")
    private String parentName;
    @ApiModelProperty(value = "角色等级")
    private String deptLv;

}
