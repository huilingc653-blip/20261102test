package com.bytter.biz.dataexchange.basic.controller;
import com.bytter.biz.dataexchange.basic.service.impl.ErpPayInfoService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
/**
 * @author qijh
 */
@Controller
@RequestMapping("/dataexchange2/payable/transfer")
public class ErpPayInfoController extends BaseController {
    @Autowired
    private ErpPayInfoService erpPayInfoService;
    @LogMessage(content = "查询接口类型列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list() {
        return "dataexchange/payable/listErpPayInfo";
    }
//    @RequiresPermissions(ResourceConstant.ERP_PAYINFO_APPLY)
    @LogMessage(content = "查询付款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return erpPayInfoService.getDataTablesUnion(dataTablesInput,this.getCurrentUser());
    }
    /**
     * 数据转移
     * @param ids
     * @return
     */
    @LogMessage(content = "付款数据转移",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @RequestMapping(params = "transfer")
    @ResponseBody
    public AjaxJson transfer(String ids){
        String[] idArr = ids.split(",");
        for(int i = 0;i<idArr.length;i++) {
            erpPayInfoService.transfer(idArr[i], this.getCurrentUser());
        }
        return  success();
    }
}
