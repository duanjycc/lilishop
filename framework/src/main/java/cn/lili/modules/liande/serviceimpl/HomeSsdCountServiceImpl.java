package cn.lili.modules.liande.serviceimpl;

import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.modules.liande.entity.vo.HomeSsdCountVO;
import cn.lili.modules.liande.mapper.DestroyDetailMapper;
import cn.lili.modules.liande.service.IHomeSsdCountService;
import cn.lili.modules.permission.entity.dos.AdminUser;
import cn.lili.modules.permission.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 */
@Service
public class HomeSsdCountServiceImpl implements IHomeSsdCountService {

    @Autowired
    private DestroyDetailMapper destroyDetailMapper;

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 销毁总量，昨日销毁总量
     * @return
     */
    @Override
    public HomeSsdCountVO count() {
        HomeSsdCountVO count = new HomeSsdCountVO();
        count.setSum( destroyDetailMapper.sum());
        count.setYesterdayCount( destroyDetailMapper.yesterdaySum() );

        AuthUser authUser =  UserContext.getCurrentUser();

        if (authUser != null && authUser.getMember() != null) {
            AdminUser adminUser = adminUserService.findByMobile(authUser.getMember().getMobile());

            if (adminUser == null) {
                count.setXhlFlg(false);
            } else if ("1".equals(adminUser.getIsXhl())) {
                count.setXhlFlg(true);
            } else {
                count.setXhlFlg(false);
            }
        } else {
            count.setXhlFlg(false);
        }
        return count;
    }
}
