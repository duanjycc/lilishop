
package cn.lili.modules.liande.mapper;

import cn.lili.modules.liande.entity.dos.RechargeRecord;
import cn.lili.modules.liande.entity.dos.TransferOutRecord;
import cn.lili.modules.liande.entity.dto.QueryTransferDTO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 转入明细 Mapper 接口
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Mapper
public interface RechargeRecordMapper extends BaseMapper<RechargeRecord> {

    @Select("SELECT t.* FROM w_recharge_record t ${ew.customSqlSegment} ")
    IPage<RechargeRecord> transferInDetails(IPage<RechargeRecord> page, @Param(Constants.WRAPPER) Wrapper<QueryTransferDTO> queryWrapper);
}
