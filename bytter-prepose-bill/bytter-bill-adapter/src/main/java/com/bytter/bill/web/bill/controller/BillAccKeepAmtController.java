package com.bytter.bill.web.bill.controller;

import com.bytter.bill.domain.bill.entity.BillAccKeepAmt;
import com.bytter.bill.domain.bill.service.BillAccKeepAmtService;
import com.bytter.bill.domain.bill.vo.AbReceiveBillBackVo;
import com.bytter.bill.domain.bill.vo.BillAccKeepAmtVO;
import com.bytter.biz.bdlink.api.enums.AllocationMessageEnum;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Description: 银承账户留底金额配置
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/3/26
 */
@Controller
@RequestMapping("/bill/accebill/accKeepAmt")
public class BillAccKeepAmtController extends BaseController {
    @Autowired
    private BillAccKeepAmtService billAccKeepAmtService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IDictService dictService;

    @LogMessage(content = "查询银承账户留底金额配置列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAccKeepAmt";
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput){
        return billAccKeepAmtService.getDataTables(dataTablesInput);
    }

    @GetMapping("/add")
    public String add( Model model) {
        return "bill/accebill/addAccKeepAmt";
    }

    @LogMessage(content = "添加银承账户留底金额配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BillAccKeepAmtVO accKeepAmtVO){
        billAccKeepAmtService.save(accKeepAmtVO, this.getCurrentUser());
        return success();
    }

    @GetMapping("/update")
    public String update(BillAccKeepAmtVO accKeepAmtVO, Model model) {
        if (StringUtils.isNotEmpty(accKeepAmtVO.getId())) {
            BillAccKeepAmt entity = billAccKeepAmtService.get(accKeepAmtVO.getId(), true);
            model.addAttribute("pageVO", entity);
        }
        return "bill/accebill/updateAccKeepAmt";
    }

    @LogMessage(content = "修改银承账户留底金额配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(BillAccKeepAmtVO accKeepAmtVO){
        billAccKeepAmtService.modify(accKeepAmtVO, this.getCurrentUser());
        return  success();
    }

    @LogMessage(content = "删除银承账户留底金额配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        BillAccKeepAmt policyAgreement = billAccKeepAmtService.get(id);
        billAccKeepAmtService.deleteEntity(policyAgreement);
        return success();
    }

    @LogMessage(content = "批量禁用银承账户留底金额配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchForbidden")
    @ResponseBody
    public AjaxJson batchForbidden(BillAccKeepAmtVO vo) {
        billAccKeepAmtService.batchForbidden(vo, this.getCurrentUser());
        return success();
    }

    @LogMessage(content = "导入银承账户留底金额配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BillAccKeepAmtVO> list) {
        billAccKeepAmtService.saveImport(list, this.getCurrentUser());
        return success();
    }

    @PostMapping("/getPayerAccQuery")
    @ResponseBody
    public List<BtBankAccDTO> getPayerAccQuery(String resource, String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("accType", BtBankAccConstant.ACCTYPE_10);// 活期票据专户
        //获取转账权限的银行账号
        List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), corpId, resource,  "2,3", AccountGrantConstant.TRANSFER_ACCOUNT,params);
        for (BtBankAccDTO dto : listBankAccTemp) {
            dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
            listAcc.add(dto);
        }
        return listAcc;
    }
}
