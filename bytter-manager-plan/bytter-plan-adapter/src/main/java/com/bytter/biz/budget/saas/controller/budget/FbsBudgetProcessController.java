package com.bytter.biz.budget.saas.controller.budget;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetProcess;
import com.bytter.biz.budget.service.*;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.vo.*;
import com.bytter.biz.finacial.api.service.IFcAccDetailBudgetMatchService;
import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.biz.vo.*;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.*;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 资金计划执行表
 * @date 2017-07-18 10:48:05
 */
@Api(value = "/v1-plan/budget/fbsBudgetProcess", tags = "资金计划执行")
@RestController("v1FbsBudgetProcessController")
@RequestMapping("/v1-plan/budget/fbsBudgetProcess")
@Slf4j
public class FbsBudgetProcessController extends BaseController {
    @Autowired
    private FbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private FbsBudgetService fbsBudgetService;
    @Autowired
    private IWorkFlowService workFlowServiceImpl;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFbsCorpBudgetService fbsCorpBudgetService;
    @Autowired
    private FbsBudgetPeriodService fbsBudgetPeriodService;
    @Autowired
    private FbsItemService fbsItemService;
    @Autowired
    private FbsProcessRuleService fbsProcessRuleService;

    @Autowired
    private FbsBudgetDetailService fbsBudgetDetailService;

    @DubboReference(check = false)
    private IFcAccDetailBudgetMatchService fcAccDetailBudgetMatchService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 获取项目计划执行页面
     *
     * @param fbsBudgetProjectDetailVO
     * @return
     */
    @ApiOperation(value = "获取预算计划执行页面")
    @LogMessage(content = "获取预算计划执行页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getViewItemDetail")
    public AjaxJson<FbsBudgetProcessPageVO> getViewItemDetail(@RequestBody FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
        FbsBudgetProcessPageVO pageVO = new FbsBudgetProcessPageVO();
        String result = this.initAttribute(pageVO, fbsBudgetProjectDetailVO);
        if (!EmptyUtils.isBlank(result)) {
            pageVO.setMsg(result);
            pageVO.setForwardUrl("budget/callErrorFbsItemCommon");
            return success(pageVO);
        }
        pageVO.setListProcess(this.fbsBudgetService.listFbsBudgetProcessCommonVOInfo(fbsBudgetProjectDetailVO, pageVO));
        pageVO.setForwardUrl("budget/callViewFbsItemCommon");
        return success(pageVO);
    }

    @ApiOperation(value = "预览匹配资金计划页面")
    @LogMessage(content = "预览匹配资金计划页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/getOpenItemDetail")
    public AjaxJson<FbsBudgetProcessPageVO> getOpenItemDetail(FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
        FbsBudgetProcessPageVO pageVO = new FbsBudgetProcessPageVO();
        String result = this.initAttribute(pageVO, fbsBudgetProjectDetailVO);
        if (!EmptyUtils.isBlank(result)) {
            pageVO.setMsg(result);
            pageVO.setForwardUrl("budget/callErrorFbsItemCommon");
            return success(pageVO);
        }
        pageVO.setForwardUrl("budget/callFbsOpenItemCommon");
        return success(pageVO);
    }

    /**
     * 获取当前科目的 余额/上级余额/顶级余额
     *
     * @param fbsBudgetProjectDetailVO
     * @return
     */
    @ApiOperation("获取当前科目的 余额/上级余额/顶级余额")
    @LogMessage(content = "获取当前科目的 余额/上级余额/顶级余额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/genItemInfo")
    public AjaxJson<FbsBudgetProcessCommonVO> genItemInfo(@RequestBody FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
        AjaxJson json = new AjaxJson();
        String corpId = fbsBudgetProjectDetailVO.getCorpId();
        String itemCode = fbsBudgetProjectDetailVO.getItemCode();
        Date processDate = fbsBudgetProjectDetailVO.getProcessDate();
        String billNo = fbsBudgetProjectDetailVO.getBillNo();
        String billId = fbsBudgetProjectDetailVO.getBillId();
        String curId = fbsBudgetProjectDetailVO.getCurId();
        if (StringUtils.isNotEmpty(corpId) && StringUtils.isNotEmpty(itemCode) && StringUtils.isNotEmpty(processDate)) {
            FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO = fbsBudgetService.getFbsBudgetProcessByItemCode(corpId, itemCode, processDate, billNo, billId, curId);
            setZeroIfNull(fbsBudgetProcessCommonVO);
            json.setObj(fbsBudgetProcessCommonVO);
            json.setSuccess(true);
        }
        return json;
    }

    private void setZeroIfNull(FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO) {
        if(fbsBudgetProcessCommonVO.getItemMoney() == null){
            fbsBudgetProcessCommonVO.setItemMoney(BigDecimal.ZERO);
        }
        if(fbsBudgetProcessCommonVO.getSuperItemMoney() == null){
            fbsBudgetProcessCommonVO.setSuperItemMoney(BigDecimal.ZERO);
        }
        if(fbsBudgetProcessCommonVO.getTopItemMoney() == null){
            fbsBudgetProcessCommonVO.setTopItemMoney(BigDecimal.ZERO);
        }
        if(fbsBudgetProcessCommonVO.getProcessMoney() == null){
            fbsBudgetProcessCommonVO.setProcessMoney(BigDecimal.ZERO);
        }
        if(fbsBudgetProcessCommonVO.getProcessCurMoney() == null){
            fbsBudgetProcessCommonVO.setProcessCurMoney(BigDecimal.ZERO);
        }
    }

    /**
     * 根据明细获取计划执行金额
     *
     * @param fbsBudgetProjectDetailVO
     * @return
     */
    @ApiOperation("获取当前科目的明细执行金额")
    @LogMessage(content = "获取当前科目的明细执行金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/genItemInfoByDetail")
    public AjaxJson<BigDecimal> genItemInfoByDetail(@RequestBody FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
        AjaxJson json = new AjaxJson();
        String corpId = fbsBudgetProjectDetailVO.getCorpId();
        String itemCode = fbsBudgetProjectDetailVO.getItemCode();
        Date processDate = fbsBudgetProjectDetailVO.getProcessDate();
        String billNo = fbsBudgetProjectDetailVO.getBillNo();
        String billId = fbsBudgetProjectDetailVO.getBillId();
        if (StringUtils.isNotEmpty(corpId) && StringUtils.isNotEmpty(itemCode) && StringUtils.isNotEmpty(processDate)) {
            BigDecimal processMoney = fbsBudgetProcessService.getBudgetProcessMoneyByDetail(corpId, itemCode, processDate, billNo, billId, fbsBudgetProjectDetailVO);
            json.setObj(processMoney);
            json.setSuccess(true);
        }
        return json;
    }

    /**
     * 初始化参数
     *
     * @param fbsBudgetProjectDetailVO
     * @return
     */
    private String initAttribute(FbsBudgetProcessPageVO pageVO, FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
        if (StringUtils.isBlank(fbsBudgetProjectDetailVO.getIsMultiItem())) {
            fbsBudgetProjectDetailVO.setIsMultiItem(CommonConstant.YES);
        }
        if (EmptyUtils.isBlank(fbsBudgetProjectDetailVO.getCorpId())) {
            //执行单位为空
            return BudgetConstant.MESSAGE_PROCESS_CORP;
        }
        String[] corpIds = fbsBudgetProjectDetailVO.getCorpId().split(CommonConstant.COMMA_SPLIT);
        boolean controlBudget = false;
        CorpDTO corpDTO = null;
        String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
        //根据执行日期获取计划周期
        FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(fbsBudgetProjectDetailVO.getProcessDate(), timeType);
        FbsBudget fbsBudget = null;
        for (int i = 0; i < corpIds.length; i++) {
            // 查询条件是否本位币
            CorpDTO corpDTOTemp = corpService.get(corpIds[i]);
            // 当单位ID为null或者当前单位不需要上报时，不加载计划科目页面
            if (!fbsCorpBudgetService.reportBudgetByCorpId(corpIds[i])) {
                if (corpIds.length == 1) {
                    return "0";
                } else {
                    //存在部分单位不需要做计划控制，无法进行批量挂接计划!
                    return BudgetConstant.MESSAGE_PROCESS_CORP_REPORTBUDGET;
                }
            }
            // 单位是否控制
            boolean controlBudgetTemp = this.fbsCorpBudgetService.controlBudgetByCorpId(corpIds[i]);
            if (i == 0) {
                controlBudget = controlBudgetTemp;
                corpDTO = corpDTOTemp;
            } else {
                if (controlBudget != controlBudgetTemp) {
                    //单位控制方式不一致，无法进行批量挂接计划!
                    return BudgetConstant.MESSAGE_PROCESS_CORP_CONTROLBUDGET;
                }
                if (!corpDTO.getCurId().equals(corpDTOTemp.getCurId())) {
                    //单位本位币不一致，无法进行批量挂接计划!
                    return BudgetConstant.MESSAGE_PROCESS_CORP_CUR;
                }
            }
            //获取单位周期计划
            FbsBudget budget = fbsBudgetService.getCorpBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
            //当前执行周期的计划未编制或者未编制完成时判断计划执行规则
            if (EmptyUtils.isEmpty(budget) || (budget != null && budget.getStatus() < BudgetConstant.STATUE_CHECK)) {
                budget = new FbsBudget();
                budget.setCurId(corpDTO.getCurId());
                //针对本期计划执行
                if (DateUtils.dayDiffs(DateUtils.getDateYMD(), fbsBudgetPeriod.getBeginDate()) >= 0
                        && DateUtils.dayDiffs(DateUtils.getDateYMD(), fbsBudgetPeriod.getEndDate()) <= 0) {
                    //判断是否有启用的计划执行规则
                    if (EmptyUtils.isNotEmpty(fbsProcessRuleService.listValidProcessRule())) {
                        FbsBudgetVO fbsBudgetVO = fbsBudgetService.buildBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), false);
                        if (fbsBudgetVO != null) {
                            BeanUtils.copyNotNullProperties(fbsBudgetVO, budget);
                        }
                    }
                }
            }
            if (budget == null) {
                //存在部分单位没有可用的计划!
                return BudgetConstant.MESSAGE_PROCESS_BUDGET;
            }
            if (i == 0) {
                fbsBudget = budget;
            }
            if (ObjectUtil.notEqual(fbsBudget.getCurId(), budget.getCurId())) {
                //单位本位币不一致，无法进行批量挂接计划!
                return BudgetConstant.MESSAGE_PROCESS_BUDGET_CUR;
            }
        }
        fbsBudgetProjectDetailVO.setControlBudget(controlBudget);
        fbsBudgetProjectDetailVO.setDetailControl(true);
        // 当单位ID为null或者当前单位不需要上报时，不加载计划科目页面
		/*if (!fbsCorpBudgetService.reportBudgetByCorpId(fbsBudgetProjectDetailVO.getCorpId())) {
			return "budget/callErrorFbsItemCommon";
		}*/
        if (EmptyUtils.isBlank(fbsBudgetProjectDetailVO.getItemCode())) {
            fbsBudgetProjectDetailVO.setItemCode(fbsItemService.getFbsItemForProcess(fbsBudgetProjectDetailVO.getSign()));
        }
		/*if (EmptyUtils.isBlank(fbsBudgetProjectDetailVO.getCorpName())) {
			fbsBudgetProjectDetailVO.setCorpName(corpService.get(fbsBudgetProjectDetailVO.getCorpId()).getName());
		}*/
        fbsBudgetProjectDetailVO.setFbsItemBalance(configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_ITEMBALANCE).getValue());
        fbsBudgetProjectDetailVO.setControlBudget(this.fbsCorpBudgetService.controlBudgetByCorpId(fbsBudgetProjectDetailVO.getCorpId()));
        if (StringUtils.isBlank(fbsBudgetProjectDetailVO.getCurId())) {
            fbsBudgetProjectDetailVO.setCurId(corpDTO.getCurId());
            fbsBudgetProjectDetailVO.setCorpName(corpDTO.getName());
        }
        if (StringUtils.isNotBlank(fbsBudgetProjectDetailVO.getCurId())) {
            BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(fbsBudgetProjectDetailVO.getCurId());
            if(btCurrencyDTO!=null){
                fbsBudgetProjectDetailVO.setCurName(btCurrencyDTO.getCurName());
            }
        }
        // 本位币查询全部
        if (!EmptyUtils.isBlank(fbsBudgetProjectDetailVO.getCurId()) && fbsBudgetProjectDetailVO.getCurId().equals(corpDTO.getCurId())) {
            pageVO.setIsRMB(true);
        }
        //String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
        //根据执行日期获取计划周期
        //FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(fbsBudgetProjectDetailVO.getProcessDate(), timeType);
        //获取单位周期计划
		/*FbsBudget budget = fbsBudgetService.getCorpBudget(fbsBudgetProjectDetailVO.getCorpId(), timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
		//当前执行周期的计划未编制或者未编制完成时判断计划执行规则
		if(EmptyUtils.isEmpty(budget) || (budget!=null && budget.getStatus()<BudgetConstant.STATUE_CHECK)){
			budget = new FbsBudget();
			budget.setCurId(corpDTO.getCurId());
			//针对本期计划执行
			if(DateUtils.dayDiffs(DateUtils.getDateYMD(), fbsBudgetPeriod.getBeginDate()) >=0
					&& DateUtils.dayDiffs(DateUtils.getDateYMD(), fbsBudgetPeriod.getEndDate())<=0) {
				//判断是否有启用的计划执行规则
				if (EmptyUtils.isNotEmpty(fbsProcessRuleService.listValidProcessRule())) {
					FbsBudgetVO fbsBudgetVO = fbsBudgetService.buildBudget(fbsBudgetProjectDetailVO.getCorpId(), timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), false);
					if(fbsBudgetVO != null){
						BeanUtils.copyNotNullProperties(fbsBudgetVO, budget);
					}
				}
			}
		}*/
        if(StringUtils.isNotEmpty(fbsBudget.getCurId())){
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(fbsBudget.getCurId());
            if(currencyDTO != null){
                fbsBudget.setCurName(currencyDTO.getCurName());
            }
        }
        pageVO.setFbsBudget(fbsBudget);
        if (fbsBudgetProjectDetailVO.getWishPayday() != null) {
            fbsBudgetProjectDetailVO.setWishPaydayStr(DateUtils.formatDate(fbsBudgetProjectDetailVO.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        }
        pageVO.setFbsBudgetProjectDetailVO(fbsBudgetProjectDetailVO);
        pageVO.setDtlColSel(configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOLSEL).getValue());
        return "";
    }


    /**
     * 编辑页面跳转
     *
     * @return
     */
    @ApiOperation("编辑页面跳转")
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getEditItemDetail")
    public AjaxJson<FbsBudgetProcessPageVO> getEditItemDetail(@RequestBody FbsBudgetProjectDetailVO vo) {
        FbsBudgetProcessPageVO model = new FbsBudgetProcessPageVO();
        String result = this.initAttribute(model, vo);
        if (!EmptyUtils.isBlank(result)) {
            model.setMsg(result);
            model.setForwardUrl("budget/callErrorFbsItemCommon");
            return AjaxJson.success(model);
        }
        model.setListProcess(this.fbsBudgetService.listFbsBudgetProcessCommonVOInfo(vo, model));
        model.setForwardUrl("budget/callEditFbsItemCommon");
        return AjaxJson.success(model);
    }


    @ApiOperation("获取绑定的资金计划科目列表（不包含明细）")
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getSavedItemList")
    public AjaxJson<List<FbsBudgetMatchedItemVO>> getSavedItemList(@RequestBody FbsBudgetProjectDetailVO vo) {
        FbsBudgetProcessPageVO model = new FbsBudgetProcessPageVO();
        String result = this.initAttribute(model, vo);
        if (!EmptyUtils.isBlank(result)) {
            return AjaxJson.success(null);
        }
        List<FbsBudgetProcessCommonVO> processCommonVOList = this.fbsBudgetService.listFbsBudgetProcessCommonVOInfo(vo, model);
        List<FbsBudgetMatchedItemVO> resultList = new ArrayList<>();
        for (FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO : processCommonVOList) {
            FbsBudgetMatchedItemVO fbsBudgetMatchedItemVO = new FbsBudgetMatchedItemVO();
            fbsBudgetMatchedItemVO.setItemObject(fbsBudgetProcessCommonVO);
            resultList.add(fbsBudgetMatchedItemVO);
        }
       return AjaxJson.success(resultList);
    }


    @ApiOperation("资金计划执行表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<FbsBudgetProcessVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String flag) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fbsBudgetProcessService.getDataTablesUnion(input, getCurrentUser(), flag));
    }


    /**
     * 资金计划执行表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("资金计划执行表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<EditItemDetailVO> update(@PathVariable("id") String id) {
        EditItemDetailVO model = new EditItemDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            FbsBudgetProcessVO fbsBudgetProcessVO = fbsBudgetProcessService.getVOById(id);
            model.setCorpCode((StringUtils.isEmpty(fbsBudgetProcessVO.getCorpId()) || corpService.get(fbsBudgetProcessVO.getCorpId()) == null) ? "" : corpService.get(fbsBudgetProcessVO.getCorpId()).getCode());
            model.setFbsBudgetProcessPage(fbsBudgetProcessVO);
        }
        return AjaxJson.success(model);
    }

    /**
     * 新增页面跳转
     *
     * @return
     */
    @ApiOperation("新增页面跳转")
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getItemDetail")
    public AjaxJson<FbsBudgetProcessPageVO> getItemDetail(FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
        FbsBudgetProcessPageVO pageVO = new FbsBudgetProcessPageVO();
        String result = this.initAttribute(pageVO, fbsBudgetProjectDetailVO);
        if (!EmptyUtils.isBlank(result)) {
            pageVO.setMsg(result);
            pageVO.setForwardUrl("budget/callErrorFbsItemCommon");
            return AjaxJson.success(pageVO);
        }
        pageVO.setForwardUrl("budget/callFbsItemCommon");
        return AjaxJson.success(pageVO);
    }


    /**
     * 手机查看页面跳转
     *
     * @return
     */
    @ApiOperation("手机查看页面跳转")
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getViewItemDetailMobile")
    public AjaxJson<List<FbsBudgetProcess>> getViewItemDetailMobile(HttpServletRequest request) {
        Model model;
        String billNo = request.getParameter("billNo");
        String billId = request.getParameter("billId");
        String businessName = request.getParameter("businessName");
        String processSysname = request.getParameter("processSysname");
        String corpId = request.getParameter("corpId");
        String processDate = request.getParameter("processDate");
        String curId = request.getParameter("curId");
        try {
            List<FbsBudgetProcess> list = null;
            if (!StringUtils.isBlank(billId)) {
                list = this.fbsBudgetProcessService.getBudgetProcessByBillId(businessName, processSysname, billId);
            } else {
                list = this.fbsBudgetProcessService.getBudgetProcessByBillNo(businessName, processSysname, billNo);
            }
            List<FbsBudgetProcessCommonVO> voList = null;
            if (!EmptyUtils.isEmpty(list)) {
                voList = new ArrayList<>();
                for (FbsBudgetProcess fbsBudgetProcess : list) {
                    FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO = fbsBudgetService.getFbsBudgetProcessByItemCode(corpId, fbsBudgetProcess.getItemCode(), DateUtils.parseDate(processDate, "yyyy-MM-dd"), null, null, curId);
                    fbsBudgetProcessCommonVO.setItemName(fbsBudgetProcess.getItemName());
                    fbsBudgetProcessCommonVO.setProcessMoney(fbsBudgetProcess.getProcessMoney());
                    voList.add(fbsBudgetProcessCommonVO);
                }
            }
            return AjaxJson.success(list);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return AjaxJson.success();
    }


    /**
     * 资金计划执行表详情页
     *
     * @return
     */
    @ApiOperation("资金计划执行表详情页")
    @LogMessage(content = "查询资金计划执行表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<EditItemDetailVO> view(@PathVariable("id") String id) {
        EditItemDetailVO model = new EditItemDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            FbsBudgetProcessVO fbsBudgetProcessVO = fbsBudgetProcessService.getVOById(id);
            model.setCorpCode((StringUtils.isEmpty(fbsBudgetProcessVO.getCorpId()) || corpService.get(fbsBudgetProcessVO.getCorpId()) == null) ? "" : corpService.get(fbsBudgetProcessVO.getCorpId()).getCode());
            model.setFbsBudgetProcessPage(fbsBudgetProcessVO);
        }
        return AjaxJson.success(model);
    }


    /**
     * 查询匹配资金计划科目
     *
     * @return
     */
    @ApiOperation("查询匹配资金计划科目")
    @LogMessage(content = "查询匹配资金计划科目", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewMatchedBudgetItem/{id}")
    public AjaxJson<List<FbsBudgetMatchedItemVO>> viewMatchedBudgetItem(@PathVariable("id") String id) {
        List<FbsBudgetMatchedItemVO> resultList = new ArrayList<>();
        FcAccDetailBudgetMatchVO matchVO = fcAccDetailBudgetMatchService.getItemInfoById(id);
        List<FbsBudgetProcessCommonVO> itemList = fbsBudgetService.getMatchedBudgetItem(matchVO);
        String dtlColSel = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOLSEL).getValue();
        Map<String, FbsBudgetProcess> processMap = this.fbsBudgetProcessService.getBudgetProcesInfo("fcAccBookDtl", "nis", matchVO.getVoucherNo() + "_" + matchVO.getCdSign(), null, null)
                .stream().collect(Collectors.toMap(FbsBudgetProcess::getItemCode, item -> item));
        for (FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO : itemList) {
            FbsBudgetMatchedItemVO fbsBudgetMatchedItemVO = new FbsBudgetMatchedItemVO();
            FbsBudgetProcess fbsBudgetProcess = processMap.get(fbsBudgetProcessCommonVO.getItemCode());
            if (fbsBudgetProcess != null) {
                fbsBudgetProcessCommonVO.setProcessMoney(fbsBudgetProcess.getProcessMoney());
                fbsBudgetProcessCommonVO.setProcessCurMoney(fbsBudgetProcess.getProcessCurMoney());
                fbsBudgetProcessCommonVO.setStatus(String.valueOf(fbsBudgetProcess.getStatus()));
                fbsBudgetProcessCommonVO.setStatusName(dictService.getByName("fbsBudgetProcess.status", String.valueOf(fbsBudgetProcess.getStatus())));
            }
            fbsBudgetMatchedItemVO.setItemObject(fbsBudgetProcessCommonVO);
            FbsBudgetProjectDetailVO itemDetailVO = new FbsBudgetProjectDetailVO();
            if (StringUtils.isNotEmpty(dtlColSel)) {
                assembleDetailVO(matchVO, fbsBudgetProcessCommonVO, itemDetailVO);
                itemDetailVO.setDtlColIds(dtlColSel);


                //获取资金明细详情数据
                FbsItemMoneyDetailInfoVO detailInfoVO = new FbsItemMoneyDetailInfoVO();
                DataTablesInput input = new DataTablesInput();
                assembleDetailDataInput(itemDetailVO, input);
                DataTablesOutput detailInfo = fbsBudgetDetailService.getProjectDetailInfo(input);
                List<FbsBudgetDetailVO> data = detailInfo.getData();
                if (ListUtils.isNotNullOrEmpty(data)) {
                    List<String> dtlColIdList = itemDetailVO.getDtlColIds() != null && !itemDetailVO.getDtlColIds().isEmpty()
                            ? Arrays.asList(itemDetailVO.getDtlColIds().substring(1, itemDetailVO.getDtlColIds().length() - 1).split(","))
                            : new ArrayList<>();
                    detailInfoVO.setDtlColSel(dtlColIdList);
                    detailInfoVO.setDetailInfo(data);
                } else {
                    detailInfoVO = null;
                }
                fbsBudgetMatchedItemVO.setDetailObject(detailInfoVO);

                //开始组装 匹配明细金额总信息
                FbsItemMoneyInfoVO moneyInfoVO = new FbsItemMoneyInfoVO();
                BigDecimal processMoney = fbsBudgetProcessService.getBudgetProcessMoneyByItem(matchVO, fbsBudgetProcessCommonVO, itemDetailVO);
                BigDecimal totalBMoney = BigDecimal.ZERO;
                if (ListUtils.isNotNullOrEmpty(data)) {
                    totalBMoney = data.stream()
                            .map(FbsBudgetDetailVO::getbMoney)
                            .filter(java.util.Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                }
                BigDecimal balance = totalBMoney.subtract(processMoney);
                moneyInfoVO.setDetailTotal(totalBMoney);
                moneyInfoVO.setProcessTotal(processMoney);
                moneyInfoVO.setBalance(balance);
                fbsBudgetMatchedItemVO.setDetailMoney(moneyInfoVO);
            }
            resultList.add(fbsBudgetMatchedItemVO);
        }


        return AjaxJson.success(resultList);
    }

    private void assembleDetailVO(FcAccDetailBudgetMatchVO matchVO, FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO, FbsBudgetProjectDetailVO itemDetailVO) {
        String corpId = matchVO.getCorpId();
        String itemCode = fbsBudgetProcessCommonVO.getItemCode();
        Date processDate = null;
        try {
            processDate = StringUtils.isNotEmpty(matchVO.getProcessDate()) ? DateUtils.parseDate(matchVO.getProcessDate(), "yyyy-MM-dd") : null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String billNo = matchVO.getVoucherNo();
        itemDetailVO.setProcessDate(processDate);
        itemDetailVO.setWishPayday(processDate);
        itemDetailVO.setBillNo(billNo);
        itemDetailVO.setCorpId(corpId);
        itemDetailVO.setCurId(matchVO.getCurId());
        itemDetailVO.setPayerAcc(matchVO.getBlnctrAcc());
        itemDetailVO.setPayeeAcc(matchVO.getOppAccNo());
        itemDetailVO.setItemCode(itemCode);
        itemDetailVO.setSettlType(matchVO.getCdSign());
    }


    private void assembleDetailDataInput(FbsBudgetProjectDetailVO itemDetailVO, DataTablesInput input) {

        String dtlColIds = itemDetailVO.getDtlColIds();
        List<String> dtlColIdList = dtlColIds != null && !dtlColIds.isEmpty()
                ? Arrays.asList(dtlColIds.substring(1, dtlColIds.length() - 1).split(","))
                : new ArrayList<>();
        input.addFormData("dtlColIds", dtlColIdList);
        input.addFormData("itemCode", itemDetailVO.getItemCode());
        input.addFormData("processDate", DateUtils.formatDate(itemDetailVO.getProcessDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        input.addFormData("corpId", itemDetailVO.getCorpId());
        input.addFormData("curId", itemDetailVO.getCurId());
        input.addFormData("payerAcc", itemDetailVO.getPayerAcc());
        input.addFormData("payeeAcc", itemDetailVO.getPayeeAcc());
        input.addFormData("settlType", itemDetailVO.getSettlType());
        input.addFormData("wishPayday", DateUtils.formatDate(itemDetailVO.getProcessDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
    }

}
