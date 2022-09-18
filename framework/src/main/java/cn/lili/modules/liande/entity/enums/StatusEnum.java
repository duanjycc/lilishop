package cn.lili.modules.liande.entity.enums;

/**
 * 评价枚举
 *
 * @author Chopper
 * @since 2021/3/20 10:44
 */

public enum StatusEnum {
    /**
     * 商品名称
     */
    USE("0", "正常/启用/未删除"),
    /**
     * 金额
     */
    DEL("1", "不使用/禁用/已删除");

    private final String type;
    private final String description;

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    StatusEnum(String type, String desc) {
        this.type = type;
        this.description = desc;
    }
}
