package com.bytter.biz.saas.controller.bankacc;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.inneraccount.service.BtBankAccRevService;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccAppRevPageVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccRevPageVO;
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
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "银行账号变更",tags = "银行账号变更")
@RestController("v1BtBankAccRevController")
@RequestMapping("/v1-payment/bankacc/btbankaccrev")
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
     * 银行账号申请表 table列表
     *
     * @return
     */
    @ApiOperation(value = "银行账号变更申请 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccAppDTO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btBankAccRevService.getDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 银行账号申请表编辑页面跳转
     *
     * @return
     */

    @ApiOperation("isForm为App的银行账号申请表编辑的页面数据获取")
    @GetMapping("/getAccAppUpdateInfo")
    @ResponseBody
    public AjaxJson<BtBankAccAppRevPageVO> getAppUpdateInfo(String id) {
        BtBankAccAppRevPageVO btBankAccRevPageVo = new BtBankAccAppRevPageVO();
        if (StringUtils.isNotEmpty(id)) {
            String curId = "";
            BtBankAccAppDTO btBankAccAppVO = btBankAccAppService.getBankAccApp(id);
            btBankAccRevPageVo.setCurId(btBankAccAppVO.getCurId());
            btBankAccRevPageVo.setBtBankAccAppPage(btBankAccAppVO);
            curId = btBankAccAppVO.getCurId();
            btBankAccRevPageVo.setIsDomesticBankType(btBankAccAppVO.getIsDomesticBank() == null ? "" : btBankAccAppVO.getIsDomesticBank());
            btBankAccRevPageVo.setIsFrom(BtBankAccConstant.FORM_APP);
            //是否允许变更为保证金户
            String isAllowedChangeDeposit = "1";
            //获取本位币
            BtCurrencyDTO natualCur = btCurrencyService.getNatualCurrency();
            //账号币别为多币别时，都不允许变更为保证金
            if (StringUtils.isNotEmpty(curId) && (curId.length() > 32)) {
                isAllowedChangeDeposit = "0";
            }
            btBankAccRevPageVo.setIsAllowedChangeDeposit(isAllowedChangeDeposit);
        }
        return success(btBankAccRevPageVo);
    }

    @ApiOperation("isForm为Acc的银行账号申请表编辑的页面数据获取")
    @GetMapping("/getAccUpdateInfo")
    @ResponseBody
    public AjaxJson<BtBankAccRevPageVO> getUpdateInfo(String id) {
        BtBankAccRevPageVO btBankAccRevPageVo = new BtBankAccRevPageVO();
        if (StringUtils.isNotEmpty(id)) {
            String curId = "";
            BtBankAccDTO btBankAccVO = btBankAccCommonService.getBtBankAccVOById(id);
            btBankAccVO.setCreateDate(new Date());
            btBankAccRevPageVo.setCurId(btBankAccVO.getCurId());
            btBankAccRevPageVo.setBtBankAccAppPage(btBankAccVO);
            curId = btBankAccVO.getCurId();
            //银行类别对应的境内外标识
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccVO.getBankTypeId());
            btBankAccRevPageVo.setIsDomesticBankType(btBankAccVO.getIsDomesticBank() == null ? "" : btBankAccVO.getIsDomesticBank());
            btBankAccRevPageVo.setIsFrom(BtBankAccConstant.FORM_ACC);
            //是否允许变更为保证金户
            String isAllowedChangeDeposit = "1";
            //获取本位币
            BtCurrencyDTO natualCur = btCurrencyService.getNatualCurrency();
            //账号币别为多币别时，都不允许变更为保证金
            if (StringUtils.isNotEmpty(curId) && (curId.length() > 32)) {
                isAllowedChangeDeposit = "0";
            }
            btBankAccRevPageVo.setIsAllowedChangeDeposit(isAllowedChangeDeposit);
        }
        return success(btBankAccRevPageVo);
    }
    /**
     * 银行账号申请表详情页
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREV_QUERY)
    @ApiOperation("查询银行账号申请表详情")
    @LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    @ResponseBody
    public AjaxJson<BtBankAccAppRevPageVO> view(@PathVariable("id") String id) {
        BtBankAccAppRevPageVO btBankAccRevPageVo = new BtBankAccAppRevPageVO();
        BtBankAccAppDTO btBankAccAppDTO = btBankAccAppService.getBankAccApp(id);
        //查询所有账户组
        List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
        HashMap<String, String> map = new HashMap<>();
        for (FcBankAccGroup fcBankAccGroup : validPayType) {
            map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
        }
        // 账户用途
        if (ObjectUtil.isNotEmpty(btBankAccAppDTO.getAccUseCode())){
            String accUseName = "";
            String[] strings = btBankAccAppDTO.getAccUseCode().split(",");
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
            btBankAccAppDTO.setAccUseName(accUseName);
        }
        //查询账户组名称
        String fcAccGroupCode = btBankAccAppDTO.getFcAccGroupCode();
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
            btBankAccAppDTO.setFcAccGroupName(accGroupName.toString());
        }
        //获取所有的单据类型
        List<FcReceiptType> validFcReceiptType = fcReceiptTypeService.getValidFcReceiptType();
        HashMap<String, String> fcReceiptMap = new HashMap<>();
        for (FcReceiptType fcReceiptType : validFcReceiptType) {
            fcReceiptMap.put(fcReceiptType.getReceiptCode(),fcReceiptType.getReceiptName());
        }
        if (EmptyUtils.isNotEmpty(btBankAccAppDTO.getReceiptCode())){
            List<String> receiptCodeList = Arrays.asList(btBankAccAppDTO.getReceiptCode().split(","));
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
            if(ObjectUtil.isNotEmpty(fcReceiptType)) btBankAccAppDTO.setReceiptName(fcReceiptType.toString());
        }
        btBankAccRevPageVo.setBtBankAccAppPage(btBankAccAppDTO);
        btBankAccRevPageVo.setBusType(BtBankAccMessageConstant.BUS_TYPE_REV);
        //虚拟户
        if ((BtBankAccConstant.ACCTYPE_09).equals(btBankAccAppDTO.getAccType()) && StringUtils.isNotEmpty(btBankAccAppDTO.getEntityAccId())) {
            BtBankAccDTO bankAcc = this.btBankAccCommonService.getBtBankAccVOById(btBankAccAppDTO.getEntityAccId());
            btBankAccRevPageVo.setBtBankAcc(bankAcc);
        }
        //银行类别对应的境内外标识
        btBankAccRevPageVo.setIsDomesticBankType(btBankAccAppDTO.getIsDomesticBank() == null ? "" : btBankAccAppDTO.getIsDomesticBank());
        return success(btBankAccRevPageVo);
    }
    /**
     * 修改银行账号申请表
     *
     * @param btBankAccApp
     * @return
     */
    @ApiOperation("修改银行账号申请表")
    @LogMessage(content = "修改银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody BtBankAccAppDTO btBankAccApp) {
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
    @ApiOperation("删除银行账号申请表")
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
    @ApiOperation(value = "批量删除银行账号申请表")
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
     * @return DataTablesOutput
     * @Description 查询银行账号变更列表
     * @Param dataTablesInput
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_ACCCHANGE_QUERY)
    @ApiOperation("查询银行账号变更列表")
    @LogMessage(content = "查询银行账号变更列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/changeTable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccAppDTO>> changeTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btBankAccRevService.getChangeDataTablesUnion(input, this.getCurrentUser()));
    }
}
