package com.bytter.biz.saas.controller.bdlinks.controller;

import com.bytter.biz.bdlinks.service.BpDubiousTradeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: BP_DUBIOUS_TRADE
 * @date 2021-3-5 11:39:56
 */
@Api(value = "可疑指令发送确认",tags = "可疑指令发送确认")
@RestController("v1BpDubiousTradeController")
@RequestMapping("/v1-payment/bdlink/bpdubioustrade")
public class BpDubiousTradeController extends BaseController {
    @Autowired
    private BpDubiousTradeService bpDubiousTradeService;

    /**
     * 发送付款指令表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "修改付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/send/{id}/{voucherNo}/{foreignFlag}")
    @ResponseBody
    public AjaxJson send(@PathVariable("id") String id, @PathVariable("voucherNo") String voucherNo, @PathVariable("foreignFlag") String foreignFlag) {
        bpDubiousTradeService.sendBisExc(id, voucherNo, foreignFlag, this.getCurrentUser());
        return success();
    }

    /**
     * 批量发送
     * @param ids
     * @param voucherNos
     * @param foreignFlag
     * @return
     */
    @LogMessage(content = "修改付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSend")
    @ResponseBody
    public AjaxJson batchSend(@RequestParam("ids") String ids,@RequestParam("voucherNos") String voucherNos,@RequestParam("foreignFlag") String foreignFlag) {
        String[] idStr = ids.split(CommonConstant.COMMA_SPLIT);
        String[] voucherNoStr = voucherNos.split(CommonConstant.COMMA_SPLIT);
        for (int i = 0; i < idStr.length; i++) {
            String id=idStr[i];
            String voucherNo=voucherNoStr[i];
            bpDubiousTradeService.sendBisExc(id, voucherNo, foreignFlag, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 作废付款指令表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "修改付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/defeat/{id}/{voucherNo}/{foreignFlag}")
    @ResponseBody
    public AjaxJson defeat(@PathVariable("id") String id, @PathVariable("voucherNo") String voucherNo, @PathVariable("foreignFlag") String foreignFlag) {
        bpDubiousTradeService.defeatBisExc(id, voucherNo, foreignFlag, this.getCurrentUser());
        return success();
    }

    /**
     * 批量作废
     * @param ids
     * @param voucherNos
     * @param foreignFlag
     * @return
     */
    @LogMessage(content = "修改付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchDefeat")
    @ResponseBody
    public AjaxJson batchDefeat(@RequestParam("ids") String ids,@RequestParam("voucherNos") String voucherNos,@RequestParam("foreignFlag") String foreignFlag) {
        String[] idStr = ids.split(CommonConstant.COMMA_SPLIT);
        String[] voucherNoStr = voucherNos.split(CommonConstant.COMMA_SPLIT);
        for (int i = 0; i < idStr.length; i++) {
            String id=idStr[i];
            String voucherNo=voucherNoStr[i];
            bpDubiousTradeService.defeatBisExc(id, voucherNo, foreignFlag, this.getCurrentUser());
        }
        return success();
    }
}
