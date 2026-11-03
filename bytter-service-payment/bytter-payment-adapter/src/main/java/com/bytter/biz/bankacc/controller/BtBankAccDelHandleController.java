package com.bytter.biz.bankacc.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.service.BtBankAccDelHandleService;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.service.FcReceiptTypeService;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccCommonService;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号注销办理
 * @date 2017-05-23 17:37:39
 */
@Controller
@RequestMapping("/bankacc/btbankaccdelhandle")
public class BtBankAccDelHandleController extends BaseController {
    @Autowired
    private BtBankAccDelHandleService btBankAccDelHandleService;
    @DubboReference(check = false)
    private IBtBankAccCommonService btBankAccCommonService;
    @Autowired
    private FcBankAccGroupService fcBankAccGroupService;
    @Autowired
    private FcReceiptTypeService fcReceiptTypeService;
    /**
     * 银行账号申请表列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDELHANDLE_QUERY)
    @LogMessage(content = "查询银行账号申请表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "bankacc/listBtBankAccDelHandle";
    }
    /**
     * 银行账号申请表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDELHANDLE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btBankAccDelHandleService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 银行账号申请表详情页
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDELHANDLE_QUERY)
    @LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BtBankAccDTO btBankAccVO = btBankAccCommonService.getBtBankAccVOById(id);
        //查询所有账户组
        List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
        HashMap<String, String> map = new HashMap<>();
        for (FcBankAccGroup fcBankAccGroup : validPayType) {
            map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
        }
        //查询账户组名称
        String fcAccGroupCode = btBankAccVO.getFcAccGroupCode();
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
            btBankAccVO.setFcAccGroupName(accGroupName.toString());
        }
        //获取所有的单据类型
        List<FcReceiptType> validFcReceiptType = fcReceiptTypeService.getValidFcReceiptType();
        HashMap<String, String> fcReceiptMap = new HashMap<>();
        for (FcReceiptType fcReceiptType : validFcReceiptType) {
            fcReceiptMap.put(fcReceiptType.getReceiptCode(),fcReceiptType.getReceiptName());
        }
        if (EmptyUtils.isNotEmpty(btBankAccVO.getReceiptCode())){
            List<String> receiptCodeList = Arrays.asList(btBankAccVO.getReceiptCode().split(","));
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
            if(ObjectUtil.isNotEmpty(fcReceiptType)) btBankAccVO.setReceiptName(fcReceiptType.toString());
        }
        model.addAttribute("btBankAccAppPage", btBankAccVO);
        return "bankacc/viewBtBankAccRev";
    }
    /**
     * 银行账号申请表详情页
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDELHANDLE_APPLY)
    @LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        BtBankAccAppDTO vo = btBankAccCommonService.getBtBankAccAppVOById(id);
        //查询所有账户组
        List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
        HashMap<String, String> map = new HashMap<>();
        for (FcBankAccGroup fcBankAccGroup : validPayType) {
            map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
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
        model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_DEL);
        return "bankacc/updateBtBankAccDelHandle";
    }
    /**
     * 注销办理
     *
     * @param btBankAccVO
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDELHANDLE_APPLY)
    @LogMessage(content = "修改银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BtBankAccVO btBankAccVO) {
        btBankAccDelHandleService.toModify(btBankAccVO, this.getCurrentUser());
        return success();
    }
    /**
     * 打回
     *
     * @param id
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDELHANDLE_APPLY)
    @LogMessage(content = "删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        this.btBankAccDelHandleService.back(id, this.getCurrentUser());
        return success();
    }
}
