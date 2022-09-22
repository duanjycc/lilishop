package cn.lili.modules.store.mapper;

import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.vos.StoreVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 店铺数据处理层
 *
 * @author pikachu
 * @since2020-03-07 09:18:56
 */
public interface StoreMapper extends BaseMapper<Store> {

    /**
     * 根据区域id统计出该区域的店铺数量
     * @param areaId
     * @return
     */
    @Select("select count(s.id) from li_store s where  store_disable = 'OPEN' and FIND_IN_SET( #{areaId},store_address_id_path) ")
    int queryStoreCountByAreaId(String areaId);

    /**
     * 根据区域id统计出该区域的做单让利金额
     * @param areaId
     * @return
     */
    @Select("select \n" +
            "\tsum(surrender_price)\n" +
            "\tfrom w_make_account w\n" +
            "\t\tleft join li_store s on w.mer_id =  cast(s.id as char)\n" +
            "\twhere \n" +
            "\t s.store_disable = 'OPEN' and FIND_IN_SET( #{areaId},s.store_address_id_path)  ")
    double queryMakeSumByAreaId(String areaId);

    /**
     * 根据区域id统计出该区域的店铺做单销毁数量
     * @param areaId
     * @return
     */
    @Select("select \n" +
            "\tsum(want_count)\n" +
            "\tfrom w_destroy_detail w\n" +
            "\t\tleft join li_store s on w.user_id =  cast(s.member_id as char)\n" +
            "\twhere \n" +
            "\t s.store_disable = 'OPEN' \n" +
            "\t and FIND_IN_SET( #{areaId},s.store_address_id_path) \n" +
            "\t and w.status = 0 ")
    int queryDestroySumByAreaId(String areaId);
    /**
     * 获取店铺详细
     *
     * @param id 店铺ID
     * @return 店铺VO
     */
    @Select("select s.*,d.* from li_store s inner join li_store_detail d on s.id=d.store_id where s.id=#{id} ")
    StoreVO getStoreDetail(String id);

    /**
     * 获取店铺分页列表
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 店铺VO分页列表
     */
    @Select("select s.* from li_store as s ${ew.customSqlSegment}")
    IPage<StoreVO> getStoreList(IPage<StoreVO> page, @Param(Constants.WRAPPER) Wrapper<StoreVO> queryWrapper);

    /**
     * APP分页查找附近条件查询
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 店铺VO分页列表
     */
    @Select("select s.*, st_distance_sphere( point ( left(store_center, LOCATE(',',store_center)-1), right(store_center, LOCATE(',',store_center)-2) ),point ( ${local} ) ) / 1000 AS distance  from li_store as s ${ew.customSqlSegment}")
    IPage<StoreVO> getAppByPage(IPage<StoreVO> page,@Param("local") String local, @Param(Constants.WRAPPER) Wrapper<StoreVO> queryWrapper);

    /**
     * 通过商品分类id获取店铺
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     */
    @Select("select s.* from li_store s left join li_store_detail d on s.id = d.store_id ${ew.customSqlSegment}")
    IPage<StoreVO> listStoreByCategory(IPage<StoreVO> page, @Param(Constants.WRAPPER) Wrapper<StoreVO> queryWrapper);


    /**
     * 修改店铺收藏数据
     *
     * @param storeId 店铺id
     * @param num     收藏数量
     */
    @Update("update li_store set collection_num = collection_num + #{num} where id = #{storeId}")
    void updateCollection(String storeId, Integer num);

}