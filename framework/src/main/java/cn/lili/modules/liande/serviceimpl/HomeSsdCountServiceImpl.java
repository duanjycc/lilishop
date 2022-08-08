package cn.lili.modules.liande.serviceimpl;

import cn.lili.modules.liande.entity.vo.HomeSsdCountVO;
import cn.lili.modules.liande.mapper.DestroyDetailMapper;
import cn.lili.modules.liande.service.IHomeSsdCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 */
@Service
public class HomeSsdCountServiceImpl implements IHomeSsdCountService {

    @Autowired
    private DestroyDetailMapper destroyDetailMapper;

    /**
     * 销毁总量，昨日销毁总量
     * @return
     */
    @Override
    public HomeSsdCountVO count() {
        HomeSsdCountVO count = new HomeSsdCountVO();
        count.setSum( destroyDetailMapper.sum());
        count.setYesterdayCount( destroyDetailMapper.yesterdaySum() );
        return count;
    }
}
