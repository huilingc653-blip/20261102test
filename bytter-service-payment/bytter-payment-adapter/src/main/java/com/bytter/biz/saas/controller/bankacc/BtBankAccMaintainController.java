package com.bytter.biz.saas.controller.bankacc;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.AllocationConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccMaintainService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcAccForBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK_ACC 银行账号父子关系维护
 * @date 2017-06-02 17:50:16
 */
@Api(value = "银行账号父子关系维护",tags = "银行账号父子关系维护")
@RestController("v1BtBankAccMaintainController")
@RequestMapping("/v1-payment/bankacc/btbankaccmaintain")
public class BtBankAccMaintainController extends BaseController {
    @DubboReference(check=false)
    private IBtBankAccMaintainService btBankAccMaintainService;//变接口
    @DubboReference(check=false)
    private IFcAccService fcAccService;
    @Autowired(required=false)
    private IFcCorpPoolService fcCorpPoolService;

    /**
     * BT_BANK_ACC table列表
     *
     * @return
     */
    @ApiOperation("银行与内部账户关系Table")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        String poolSign = (String) input.getFormdata().get("poolSign");
        if(AllocationConstant.POOL_SIGN_1.equals(poolSign)){
            return success(btBankAccMaintainService.getDataTablesUnionForPool(input, this.getCurrentUser()));
        }else {
            return success(btBankAccMaintainService.getDataTablesUnion(input, this.getCurrentUser()));
        }
    }

    @ApiOperation(value = "点击 关联内部账户后的table"
            ,notes = "在此之前要将行数据的对应信息传过来corpId 和 id,原先是通过showCounterAccList去跳转的")
    @PostMapping("/dataTableForCounterAcc")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccForBankAccDTO>> dataTableForCounterAcc(@RequestBody V1DataTablesInput dataTablesInput, String corpId, String id) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        String poolSign = (String) input.getFormdata().get("poolSign");
        if(AllocationConstant.POOL_SIGN_1.equals(poolSign)){
            return success(btBankAccMaintainService.dataTableForCounterPoolAcc(input, this.getCurrentUser(), corpId));
        }else {
            return success(btBankAccMaintainService.dataTableForCounterAcc(input, this.getCurrentUser(), corpId,id));
        }
    }
    /**
     * 增加子账号
     *
     * @param id
     * @return
     */
    @ApiOperation("增加内部账号")
    @LogMessage(content = "增加内部账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/toAdd")
    @ResponseBody
    public AjaxJson toAdd(@RequestParam("id") String id, @RequestParam("blnctrAcc") String blnctrAcc, @RequestParam("flag") String flag, @RequestParam("curId") String curId) {
        btBankAccMaintainService.toAdd(id, blnctrAcc, flag, curId);
        return success();
    }
    /**
     * 增加子账号
     *
     * @param id
     * @return
     */
    @ApiOperation("删除内部账号")
    @LogMessage(content = "增加内部账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/toDel")
    @ResponseBody
    public AjaxJson toDel(@RequestParam("id") String id, @RequestParam("flag") String flag, @RequestParam("curId") String curId) {
        btBankAccMaintainService.toDel(id, flag, curId);
        return success();
    }
    /**
     * 检查内部账户是否存在对应的联行网点及账号
     *
     * @param blnctrAcc
     * @return
     */

    @ApiOperation("检查内部账户是否存在对应的联行网点及账号")
    @PostMapping(path = "/checkUnionnetOrUnionacc")
    @ResponseBody
    public AjaxJson checkUnionnetOrUnionacc(String blnctrAcc) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(blnctrAcc);
        if (EmptyUtils.isBlank(fcAccDTO.getUnionNet()) || EmptyUtils.isBlank(fcAccDTO.getUnionAcc())) {
            j.setObj(true);
        }
        return j;
    }
}
