package cn.lili.modules.statistics.mapper;

import cn.lili.modules.order.order.entity.dos.StoreFlow;
import cn.lili.modules.statistics.entity.vo.CategoryStatisticsDataVO;
import cn.lili.modules.statistics.entity.vo.GoodsStatisticsDataVO;
import cn.lili.modules.statistics.entity.vo.StoreStatisticsDataVO;
import cn.lili.modules.statistics.entity.vo.WssdHisDataVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品统计数据处理层
 *
 * @author Bulbasaur
 * @since 2020/11/17 7:34 下午
 */
public interface StoreFlowStatisticsMapper extends BaseMapper<StoreFlow> {

    /**
     * 商品统计
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 商品统计列表
     */
    @Select("SELECT goods_id,goods_name,SUM(final_price) AS price,SUM(num) AS num FROM li_store_flow ${ew.customSqlSegment}")
    List<GoodsStatisticsDataVO> getGoodsStatisticsData(IPage<GoodsStatisticsDataVO> page, @Param(Constants.WRAPPER) Wrapper<GoodsStatisticsDataVO> queryWrapper);

    /**
     * 分类统计
     *
     * @param queryWrapper 查询条件
     * @return 分类统计列表
     */
    @Select("SELECT category_id,category_name,SUM(price) AS price,SUM(num) AS num FROM li_store_flow ${ew.customSqlSegment}")
    List<CategoryStatisticsDataVO> getCateGoryStatisticsData(@Param(Constants.WRAPPER) Wrapper<CategoryStatisticsDataVO> queryWrapper);



    /**
     * 店铺统计列表
     *
     * @param page         分页
     * @param queryWrapper 查询参数
     * @return 店铺统计列表
     */
    @Select("SELECT store_id AS storeId,store_name AS storeName,SUM(final_price) AS price,SUM(num) AS num FROM li_store_flow ${ew.customSqlSegment}")
    List<StoreStatisticsDataVO> getStoreStatisticsData(IPage<GoodsStatisticsDataVO> page, @Param(Constants.WRAPPER) Wrapper<GoodsStatisticsDataVO> queryWrapper);

    /**
     * 店铺消费金额统计列表
     *
     * @param page         分页
     * @param queryWrapper 查询参数
     * @return 店铺统计列表
     */
    @Select("SELECT mer_id as store_id ,mer_name AS storeName,username as phone,SUM(surrender_price) AS price,SUM(monetary) AS num FROM w_make_account where DATE_FORMAT(create_time,'%Y-%m-%d')=curdate() ${ew.customSqlSegment}")
    List<StoreStatisticsDataVO> getStoreStatisticsTopData(IPage<StoreStatisticsDataVO> page, @Param(Constants.WRAPPER) Wrapper<StoreStatisticsDataVO> queryWrapper);

    /**
     * 店铺消费会员统计列表
     *
     * @param page         分页
     * @param queryWrapper 查询参数
     * @return 店铺统计列表
     */
    @Select("SELECT mer_id as store_id ,mer_name AS storeName,count(DISTINCT vip_phone) AS nickNum FROM w_make_account GROUP BY mer_id,mer_name ORDER BY nickNum desc limit 0, 10")
    List<StoreStatisticsDataVO> getStoreNickNumTopData(IPage<StoreStatisticsDataVO> page, @Param(Constants.WRAPPER) Wrapper<StoreStatisticsDataVO> queryWrapper);


    /**
     * 近期价格走势
     *
     * @param page         分页
     * @param queryWrapper 查询参数
     * @return 近期价格列表
     */
    @Select("SELECT date_id as dateId ,numerical_alue AS numericalAlue,create_time as createTime FROM w_ssd_his ${ew.customSqlSegment}")
    List<WssdHisDataVO> getSsdPriceTopData(IPage<WssdHisDataVO> page, @Param(Constants.WRAPPER) Wrapper<StoreStatisticsDataVO> queryWrapper);

}