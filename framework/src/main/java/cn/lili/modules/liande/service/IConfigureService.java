
package cn.lili.modules.liande.service;

import cn.lili.modules.liande.entity.dos.Configure;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
public interface IConfigureService extends IService<Configure> {

    /**
     * 获取SSD单价
     * @return
     */
    Double querySssUnitPrice();
}
