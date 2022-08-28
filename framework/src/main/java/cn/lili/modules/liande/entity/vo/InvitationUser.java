package cn.lili.modules.liande.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询推广人员
 */
@Data
@NoArgsConstructor
public class InvitationUser {

    private String label;
    private String value;

    public InvitationUser(String label, String value){
        this.label = label;
        this.value = value;
    }

}
