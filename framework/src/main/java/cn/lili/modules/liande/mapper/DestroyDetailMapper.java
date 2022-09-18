package cn.lili.modules.liande.mapper;

import cn.lili.modules.liande.entity.dos.DestroyDetail;
import cn.lili.modules.member.entity.vo.GoodsCollectionVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * <p>
 * 销毁明细 Mapper 接口
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Mapper
public interface DestroyDetailMapper extends BaseMapper<DestroyDetail> {

    @Select("select IFNULL(SUM(want_count),0)  from w_destroy_detail where status = '0' ")
    Double sum();

   // @Select("select IFNULL(SUM(want_count),0)  from w_destroy_detail where status = '0' and  left(destroy_time,10) = left(DATE_SUB(now(),INTERVAL 1 DAY),10) ")
    @Select("select IFNULL(SUM(want_count),0)  from w_destroy_detail where status = '0' and  left(destroy_time,10) = left(now(),10) ")
    Double yesterdaySum();

}
