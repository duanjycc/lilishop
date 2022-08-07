package cn.lili.controller.liande;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.liande.entity.dto.TransferDTO;
import cn.lili.modules.liande.service.ITransferOutRecordService;
import cn.lili.modules.sms.SmsUtil;
import cn.lili.modules.verification.entity.enums.VerificationEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "转账")
@RestController
@RequestMapping("/buyer/my/transfer")
public class TransferController {

    @Autowired
    private ITransferOutRecordService transferService;
    @Autowired
    private SmsUtil smsUtil;

    @ApiOperation(value = "转账")
    @PostMapping("/out")
    public ResultMessage<Object> out(@RequestBody TransferDTO transfer, @RequestHeader String uuid) {
        if (smsUtil.verifyCode(UserContext.getCurrentUser().getMember().getMobile(), VerificationEnums.TRANSFER, uuid, transfer.getVerificationCode())){
            throw new ServiceException(ResultCode.TRANSFER_VERIFICATION_CODE_ERROR);
        }
        return ResultUtil.data(transferService.out(transfer));
    }
}
