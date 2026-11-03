
package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexSettleFillRuleAdd;
import com.bytter.biz.foreign.service.FexSettleFillRuleAddService;
import com.bytter.biz.foreign.service.FexSettleFillRuleService;
import com.bytter.biz.foreign.vo.FexSettleFillRuleVO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 * @author luxy
 * @Title: FexSettleFillRuleController
 * @ProjectName T3(3.2)
 * @Description: 结算自动填充规则控制层
 * @date 2020/2/18 10:22
 */
@Controller
@RequestMapping("/foreign/fexsettlefillrule")
public class FexSettleFillRuleController extends BaseController {
    @Autowired
    private FexSettleFillRuleService fexSettleFillRuleService;
    @Autowired(required=false)
    private IBtBankAccService iBtBankAccService;
    @Autowired(required=false)
    private IBtCurrencyService currencyService;
    @Autowired(required=false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @Autowired
    private FexSettleFillRuleAddService fillRuleAddService;
    @GetMapping("/list")
    @LogMessage(content = "查询结算自动填充规则列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public String list(Model model){
        return "foreign/listFexSettleFillRule";
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input){
        return fexSettleFillRuleService.datatable(input,getCurrentUser());
    }

    @GetMapping("/add")
    @LogMessage(content = "进入新增结算自动填充规则页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public String add(Model model){
//        model.addAttribute("natualCur", currencyService.getNatualCurrency().getId());
        return "foreign/addFexSettleFillRule";
    }

    @PostMapping("/saveOrUpdate")
    @ResponseBody
    @LogMessage(content = "保存结算自动填充规则数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public AjaxJson saveOrUpdate(FexSettleFillRuleVO ruleVO){
        fexSettleFillRuleService.saveOrUpdateRule(ruleVO,getCurrentUser());
        return success();
    }

    @GetMapping("/update/{id}")
    @LogMessage(content = "进入结算自动填充规则编辑页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public String update(@PathVariable("id") String id,Model model){
        FexSettleFillRuleVO ruleVO=fexSettleFillRuleService.getFillRuleById(id);
        model.addAttribute("ruleVO",ruleVO);
        model.addAttribute("addSize",ruleVO.getRuleAddList().size());
        return "foreign/updateFexSettleFillRule";
    }

    @GetMapping("/view/{id}")
    @LogMessage(content = "查看结算自动填充规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public String view(@PathVariable("id") String id,Model model){
        FexSettleFillRuleVO ruleVO=fexSettleFillRuleService.getFillRuleById(id);
        model.addAttribute("ruleVO",ruleVO);
        return "foreign/viewFexSettleFillRule";
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    @LogMessage(content = "删除结算自动填充规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public AjaxJson delete(@PathVariable("id") String id){
        fexSettleFillRuleService.deleteRule(id);
        return success();
    }

    /**
     * 获取已经开好户的银行账户
     *
     * @param corpId
     * @return
     */
    @PostMapping("/getBankAcc")
    @ResponseBody
    @LogMessage(content = "根据网点及币别获取银行账户",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public List<BtBankAccDTO> getBankAcc(String corpId,String curId) {
        List<BtBankAccDTO> listBankAcc = new ArrayList<BtBankAccDTO>();
        if (StringUtils.isNotEmpty(corpId)) {
            BtBankAccDTO bankDTO = new BtBankAccDTO();
            bankDTO.setCorpId(corpId);
            bankDTO.setCurId(curId);
            listBankAcc = iBtBankAccService.getBankAccListAll(bankDTO);
        }
        return listBankAcc;
    }

    /**
     * 获取系统结算方式为34-外付网银的自定义付款方式
     * @return
     */
    @GetMapping("/getVoucherType")
    @ResponseBody
    @LogMessage(content = "获取外付网银的付款方式",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public  List<FcVoucherTypeDTO> getVoucherType(){
        FcVoucherTypeDTO voucherTypeDTO=fcVoucherTypeService.getSysFcVoucherType("34");
        List<FcVoucherTypeDTO> voucherTypeDTOS=new ArrayList<>();
        voucherTypeDTOS.add(voucherTypeDTO);
        return voucherTypeDTOS;
    }

    /**
     * 根据规则id查询规则补充数据
     */
    @PostMapping("/getRuleAddByRuleId")
    @ResponseBody
    @LogMessage(content = "根据规则id查询规则补充数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public  List<FexSettleFillRuleAdd> getRuleAddByRuleId(String ruleId){
        List<FexSettleFillRuleAdd> addList=fillRuleAddService.getRuleAddByRuleId(ruleId);
        return addList;
    }

    /**
     * 查询规则是否被引用（现在不使用，待后期完善）
     */
    @PostMapping("/checkRuleUse/{ruleId}")
    @ResponseBody
    @LogMessage(content = "查询结算自动填充规则是否被引用",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public Boolean checkRuleUse(@PathVariable("ruleId") String ruleId){
        return false;
    }
}
