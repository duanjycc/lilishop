
package cn.lili.modules.liande.mapper;

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
 * 转出明细 Mapper 接口
 * </p>
 *
 * @author zh
 * @since 2022-08-05
 */
@Mapper
public interface TransferOutRecordMapper extends BaseMapper<TransferOutRecord> {



    @Select("SELECT t.* FROM w_transfer_out_record t ${ew.customSqlSegment} ")
    IPage<TransferOutRecord> transferOutDetails(IPage<TransferOutRecord> page, @Param(Constants.WRAPPER) Wrapper<QueryTransferDTO> queryWrapper);

}
