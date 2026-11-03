package com.bytter.biz.bankacc.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.service.BtBankAccRevService;
import com.bytter.biz.inneraccount.vo.BtBankAccAppVO;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.netbank.entity.BtBankAccUse;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.service.BtBankAccUseService;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.service.FcReceiptTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccAppService;
import com.bytter.system.api.IBtBankAccCommonService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号变更
 * @date 2017-05-23 17:37:39
 */
@Controller
@RequestMapping("/bankacc/btbankaccrev")
public class BtBankAccRevController extends BaseController {
    @Autowired
    private BtBankAccRevService btBankAccRevService;
    @DubboReference(check = false)
    private IBtBankAccAppService btBankAccAppService;
    @DubboReference(check = false)
    private IBtBankAccCommonService btBankAccCommonService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @Autowired
    private FcBankAccGroupService fcBankAccGroupService;
    @Autowired
    private FcReceiptTypeService fcReceiptTypeService;
    @Autowired
    private BtBankAccUseService bankAccUseService;
    /**
     * 银行账号申请表列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREV_QUERY)
    @LogMessage(content = "查询银行账号申请表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "bankacc/listBtBankAccRev";
    }
    /**
     * 银行账号申请表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREV_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btBankAccRevService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 银行账号申请表编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREV_APPLY)
    @GetMapping("/update")
    public String update(BtBankAccAppVO vo, Model model) {
        if (StringUtils.isNotEmpty(vo.getId())) {
            String curId = "";
            if (BtBankAccConstant.FORM_APP.equals(vo.getIsForm())) {
                BtBankAccAppDTO btBankAccAppVO = btBankAccAppService.getBankAccApp(vo.getId());
                model.addAttribute("curId", btBankAccAppVO.getCurId());
                model.addAttribute("btBankAccAppPage", btBankAccAppVO);
                curId = btBankAccAppVO.getCurId();
                //银行类别对应的境内外标识
                BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccAppVO.getBankTypeId());
                model.addAttribute("isDomesticBankType", bankTypeDTO == null ? "" : bankTypeDTO.getIsDomesticBank());
            } else {
                BtBankAccDTO btBankAccVO = btBankAccCommonService.getBtBankAccVOById(vo.getId());
                btBankAccVO.setCreateDate(new Date());
                model.addAttribute("curId", btBankAccVO.getCurId());
                model.addAttribute("btBankAccAppPage", btBankAccVO);
                curId = btBankAccVO.getCurId();
                //银行类别对应的境内外标识
                BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccVO.getBankTypeId());
                model.addAttribute("isDomesticBankType", bankTypeDTO == null ? "" : bankTypeDTO.getIsDomesticBank());
            }
            model.addAttribute("isFrom", vo.getIsForm());
            //是否允许变更为保证金户
            String isAllowedChangeDeposit = "1";
            //获取本位币
            BtCurrencyDTO natualCur = btCurrencyService.getNatualCurrency();
            //账号币别为多币别时，都不允许变更为保证金
            if (StringUtils.isNotEmpty(curId) && (curId.length() > 32)) {
                isAllowedChangeDeposit = "0";
            }
            model.addAttribute("isAllowedChangeDeposit", isAllowedChangeDeposit);
        }
        return "bankacc/updateBtBankAccRev";
    }
    /**
     * 银行账号申请表详情页
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREV_QUERY)
    @LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BtBankAccAppDTO vo = btBankAccAppService.getBankAccApp(id);
        //查询所有账户组
        List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
        HashMap<String, String> map = new HashMap<>();
        for (FcBankAccGroup fcBankAccGroup : validPayType) {
            map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
        }
        // 账户用途
        if (ObjectUtil.isNotEmpty(vo.getAccUseCode())){
            String accUseName = "";
            String[] strings = vo.getAccUseCode().split(",");
            List<BtBankAccUse> btBankAccUses = bankAccUseService.listAll();
            for (String code : strings){
                for (BtBankAccUse use : btBankAccUses){
                    if (code.equals(use.getUseCode())){
                        accUseName += use.getUseName()+",";
                        break;
                    }
                }
            }
            if (ObjectUtil.isNotEmpty(accUseName)) accUseName = accUseName.substring(0,accUseName.length()-1);
            vo.setAccUseName(accUseName);
        }
        //查询账户组名称
        String fcAccGroupCode = vo.getFcAccGroupCode();
        if (EmptyUtils.isNotEmpty(fcAccGroupCode)){
            List<String> codes = Arrays.asList(fcAccGroupCode.split(","));
            StringBuilder accGroupName = new StringBuilder();
            for(int i = 0;i<=codes.size()-1;i++){
                if (i==codes.size()-1){
                    if (map.containsKey(codes.get(i))){
                        accGroupName.append(map.get(codes.get(i)));
                    }
                }else{
                    if (map.containsKey(codes.get(i))){
                        accGroupName.append(map.get(codes.get(i))+",");
                    }
                }
            }
            vo.setFcAccGroupName(accGroupName.toString());
        }
        //获取所有的单据类型
        List<FcReceiptType> validFcReceiptType = fcReceiptTypeService.getValidFcReceiptType();
        HashMap<String, String> fcReceiptMap = new HashMap<>();
        for (FcReceiptType fcReceiptType : validFcReceiptType) {
            fcReceiptMap.put(fcReceiptType.getReceiptCode(),fcReceiptType.getReceiptName());
        }
        if (EmptyUtils.isNotEmpty(vo.getReceiptCode())){
            List<String> receiptCodeList = Arrays.asList(vo.getReceiptCode().split(","));
            StringBuilder fcReceiptType = new StringBuilder();
            for(int i = 0;i<=receiptCodeList.size()-1;i++){
                if (i==receiptCodeList.size()-1){
                    if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
                        fcReceiptType.append(fcReceiptMap.get(receiptCodeList.get(i)));
                    }
                }else{
                    if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
                        fcReceiptType.append(fcReceiptMap.get(receiptCodeList.get(i))+",");
                    }
                }
            }
            if(ObjectUtil.isNotEmpty(fcReceiptType)) vo.setReceiptName(fcReceiptType.toString());
        }
        model.addAttribute("btBankAccAppPage", vo);
        model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_REV);
        //虚拟户
        if ((BtBankAccConstant.ACCTYPE_09).equals(vo.getAccType()) && StringUtils.isNotEmpty(vo.getEntityAccId())) {
            BtBankAccDTO bankAcc = this.btBankAccCommonService.getBtBankAccVOById(vo.getEntityAccId());
            model.addAttribute("btBankAcc", bankAcc);
        }
        //银行类别对应的境内外标识
        BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(vo.getBankTypeId());
        model.addAttribute("isDomesticBankType", bankTypeDTO == null ? "" : bankTypeDTO.getIsDomesticBank());
        return "bankacc/viewBtBankAccRev";
    }
    /**
     * 修改银行账号申请表
     *
     * @param btBankAccApp
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREV_APPLY)
    @LogMessage(content = "修改银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BtBankAccAppDTO btBankAccApp) {
        if (BtBankAccConstant.ACCTYPE_02.equals(btBankAccApp.getAccType())) {
            btBankAccApp.setRegularDate(BudgetConstant.getRegularsDate(btBankAccApp.getRegulars()));
        }
        btBankAccRevService.toModify(btBankAccApp, this.getCurrentUser());
        return success();
    }
    /**
     * 删除银行账号申请表
     *
     * @param id
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREV_APPLY)
    @LogMessage(content = "删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        this.btBankAccRevService.delete(id, this.getCurrentUser());
        return success();
    }
    /**
     * 批量删除银行账号申请表
     *
     * @param ids
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREV_APPLY)
    @LogMessage(content = "批量删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.btBankAccRevService.delete(id, this.getCurrentUser());
        }
        return success();
    }
    /**
     * @return String
     * @Description 跳转到银行账号变更列表
     * @Param
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_ACCCHANGE_QUERY)
    @LogMessage(content = "跳转到银行账号变更列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/changeList")
    public String bankAccChangeQuery() {
        return "bankacc/listBtBankAccChangeQuery";
    }
    /**
     * @return DataTablesOutput
     * @Description 查询银行账号变更列表
     * @Param dataTablesInput
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_ACCCHANGE_QUERY)
    @LogMessage(content = "查询银行账号变更列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/changeTable")
    @ResponseBody
    public DataTablesOutput changeTable(@RequestBody DataTablesInput dataTablesInput) {
        return btBankAccRevService.getChangeDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * @return String
     * @Description 附件查看
     * @Param attachmentId
     * @Param model
     */
    @GetMapping("/viewAttachment")
    public String viewAttachment(String attachmentId, Model model) {
        model.addAttribute("attachmentId", attachmentId);
        return "bankacc/viewAttachmentInfo";
    }
}
