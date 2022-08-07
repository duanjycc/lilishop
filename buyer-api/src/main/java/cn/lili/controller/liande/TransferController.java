package cn.lili.controller.liande;

/**
 * 邀请相关
 */
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dto.TransferDTO;
import cn.lili.modules.liande.service.ITransferOutRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyer/my/transfer")
@Api(tags = "转入、转出")
public class TransferController {

    @Autowired
    private ITransferOutRecordService transferService;


    @ApiOperation(value = "转账")
    @PostMapping("/out")
    public ResultMessage<Boolean> out(TransferDTO transfer) {
        transferService.out(transfer);
        return null;
    }
}
