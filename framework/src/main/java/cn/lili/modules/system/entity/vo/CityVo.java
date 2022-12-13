package cn.lili.modules.system.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CityVo {
    private String value;
    private String label;
    private List<CityVo> children;

    public CityVo(String value, String label) {
        this.value = value;
        this.label = label;
        this.children = new ArrayList<>();
    }
}
