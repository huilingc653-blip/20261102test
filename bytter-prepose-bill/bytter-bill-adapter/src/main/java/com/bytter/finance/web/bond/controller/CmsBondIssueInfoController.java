package com.bytter.finance.web.bond.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.bond.entity.CmsBondIssueInfo;
import com.bytter.finance.domain.bond.entity.CmsBondRepayPlan;
import com.bytter.finance.domain.bond.service.*;
import com.bytter.finance.domain.bond.utils.BondUtils;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanDtlVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanVo;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.system.api.IBaseRateService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ICreditPeriodService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
/**
 * 债券发行Controller
 *
 * @author ouyj
 */
@Controller
@RequestMapping("/cms/bond/issue")
public class CmsBondIssueInfoController extends BaseController {
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupyService;
    @Autowired
    private CmsBondRepayPlanService cmsBondRepayPlanService;
    @Autowired
    private CmsBondRepayPlanDtlService cmsBondRepayPlanDtlService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @DubboReference(check = false)
    private ICorpService corpService;
    /**
     * 债券发行信息查询列表跳转
     *
     * @return 页面路径
     */
    @GetMapping("/list")
    public String list() {
        return "cms/bond/listBondIssueInfo";
    }
    /**
     * 债券发行查询信息查询列表跳转
     *
     * @return 页面路径
     */
    @GetMapping("/queryList")
    public String queryList() {
        return "cms/bond/listQueryBondIssueInfo";
    }
    /**
     * 获取债券发行信息新增页路径
     *
     * @param registerId
     *            注册id
     * @param model
     *            model
     * @return 债券发行信息新增页路径
     */
    @GetMapping("/add/{registerId}")
    public String add(@PathVariable("registerId") String registerId, Model model) {
        model.addAttribute("registerInfo", cmsBondRegisterInfoService.getInfoById(registerId));
        model.addAttribute("isHookBond", "1");
        model.addAttribute("pledgeConfig", "1");
        model.addAttribute("isGuarantee", "1");
        return "cms/bond/addBondIssueInfo";
    }
    /**
     * 获取债券发行信息编辑页路径
     *
     * @param id
     *            债券发行信息id
     * @param model
     *            模型
     * @return 债券发行信息编辑页路径
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        CmsBondIssueInfo cmsBondIssueInfo = cmsBondIssueInfoService.get(id, true);
        model.addAttribute("cmsBondRepayPlanVo", cmsBondRepayPlanService.getByIssueId(cmsBondIssueInfo.getId()));
        model.addAttribute("issueInfo", cmsBondIssueInfo);
        model.addAttribute("registerInfo", cmsBondRegisterInfoService.getInfoById(cmsBondIssueInfo.getRegisterId()));
        BondUtils.setBondParam(model, "1", cmsBondIssueInfo, fcBondHookService);
        BondUtils.setGuaParams(model, "0", "1", cmsBondIssueInfo, guaContractOccupyService);
        // 抵质押参数
        BondUtils.setPledgeParam(model, "1", cmsBondIssueInfo, pledgeOccupyService);
        // 第三方费率信息
        List<DfIntermediateCostVO> costList = cmsBondIssueInfoService
                .listIntermediateCostByDfId(cmsBondIssueInfo.getId());
        model.addAttribute("costVOS", costList);
        return "cms/bond/updateBondIssueInfo";
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/bond/approve/listBondIssueInfoApprove";
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId
     *            id
     * @param taskId
     *            审批任务id
     * @param model
     *            模型
     * @return 前端页面路径
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        model.addAttribute("taskId", taskId);
        CmsBondIssueInfo cmsBondIssueInfo = cmsBondIssueInfoService.get(businessId, true);
        CorpDTO corpDTO = corpService.get(cmsBondRegisterInfoService.get(cmsBondIssueInfo.getRegisterId()).getCorpId());
        model.addAttribute("corp", corpDTO);
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsBondIssueInfo.getFixPriceBase());
        if (null != tbBaseRate) {
            model.addAttribute("tbBaseRate", tbBaseRate);
        }
        if (StringUtils.isNotEmpty(cmsBondIssueInfo.getRateDeadline())) {
            TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsBondIssueInfo.getRateDeadline());
            if (null != tbCreditPeriod) {
                model.addAttribute("tbCreditPeriod", tbCreditPeriod);
            }
        }
        // 保证金参数
        BondUtils.setBondParam(model, "1", cmsBondIssueInfo, fcBondHookService);
        // 担保参数
        BondUtils.setGuaParams(model, "1", "1", cmsBondIssueInfo, guaContractOccupyService);
        // 抵质押参数
        BondUtils.setPledgeParam(model, "1", cmsBondIssueInfo, pledgeOccupyService);
        // 第三方费率信息
        List<DfIntermediateCostVO> costList = cmsBondIssueInfoService
                .listIntermediateCostByDfId(cmsBondIssueInfo.getId());
        model.addAttribute("costVOS", costList);
        model.addAttribute("issueInfo", cmsBondIssueInfo);

        String issueScope = cmsBondIssueInfo.getIssueScope();
        // 发行范围
        if (StringUtils.isNotEmpty(issueScope)) {
            List<String> issueScopeList = Arrays.asList(issueScope.split(CommonConstant.COMMA_SPLIT));
            model.addAttribute("issueScopeList", issueScopeList);
        }
        return "cms/bond/approve/viewBondIssueInfoApprove";
    }
    /**
     * 债券发行信息表详情页
     *
     * @param id
     *            id
     * @param model
     *            model
     * @return 前端页面路径
     */
    @LogMessage(content = "查询债券发行信息详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        CmsBondIssueInfo cmsBondIssueInfo = cmsBondIssueInfoService.get(id, true);
        model.addAttribute("issueInfo", cmsBondIssueInfo);
        CmsBondRegisterInfoVo registerInfo = cmsBondRegisterInfoService.getInfoById(cmsBondIssueInfo.getRegisterId());
        CorpDTO corpDTO = corpService.get(registerInfo.getCorpId());
        model.addAttribute("corp", corpDTO);
        model.addAttribute("registerInfo", registerInfo);
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsBondIssueInfo.getFixPriceBase());
        Optional.ofNullable(tbBaseRate).ifPresent(baseRate ->{
            model.addAttribute("tbBaseRate", baseRate);
        });
        if (StringUtils.isNotEmpty(cmsBondIssueInfo.getRateDeadline())) {
            TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsBondIssueInfo.getRateDeadline());
            Optional.ofNullable(tbCreditPeriod).ifPresent(creditPeriod -> {
                model.addAttribute("tbCreditPeriod", creditPeriod);
            });
        }
        // 保证金参数
        BondUtils.setBondParam(model, "1", cmsBondIssueInfo, fcBondHookService);
        // 担保参数
        BondUtils.setGuaParams(model, "1", "1", cmsBondIssueInfo, guaContractOccupyService);
        // 抵质押参数
        BondUtils.setPledgeParam(model, "1", cmsBondIssueInfo, pledgeOccupyService);
        // 第三方费率信息
        List<DfIntermediateCostVO> costList = cmsBondIssueInfoService
                .listIntermediateCostByDfId(cmsBondIssueInfo.getId());
        model.addAttribute("costVOS", costList);
        CmsBondIssueInfo issueInfo = cmsBondIssueInfoService.get(id);
        model.addAttribute("issueInfo", issueInfo);
        // 发行范围
        String issueScope = issueInfo.getIssueScope();
        if (StringUtils.isNotEmpty(issueScope)) {
            List<String> issueScopeList = Arrays.asList(issueScope.split(CommonConstant.COMMA_SPLIT));
            model.addAttribute("issueScopeList", issueScopeList);
        }
        return "cms/bond/viewBondIssueInfo";
    }
    /**
     * 债券发行明细详情列表
     *
     * @param id
     *            id
     * @param model
     *            model
     * @return 前端页面路径
     */
    @LogMessage(content = "债券发行明细详情列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/issueDtl/{id}")
    public String issueDtl(@PathVariable("id") String id, Model model) {
        model.addAttribute("registerId", id);
        return "cms/bond/listBondIssueDtl";
    }
    /**
     * 债券发行审批列表 - 数据
     *
     * @param input
     *            前端传参
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput input) {
        return cmsBondIssueInfoService.dataTableApprove(input);
    }
    /**
     * 获取债券发行信息列表数据
     *
     * @param input
     *            前端传参
     * @return 债券发行信息列表数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return cmsBondIssueInfoService.getDataTable(input, this.getCurrentUser());
    }
    /**
     * 获取债券发行查询信息列表数据
     *
     * @param input
     *            前端传参
     * @return 债券发行查询信息列表数据
     */
    @PostMapping("/queryDatatable")
    @ResponseBody
    public DataTablesOutput queryDatatable(@RequestBody DataTablesInput input) {
        return cmsBondIssueInfoService.getQueryDataTable(input, this.getCurrentUser());
    }
    /**
     * 获取债券发行详情列表数据
     *
     * @param input
     *            前端传参
     * @return 获取债券发行详情列表数据
     */
    @PostMapping("/issueDtlDatatable")
    @ResponseBody
    public DataTablesOutput issueDtlDatatable(@RequestBody DataTablesInput input) {
        return cmsBondIssueInfoService.getIssueDtlDatatable(input);
    }
    /**
     * 新增债券发行信息
     *
     * @param cmsBondIssueInfo
     *            债券发行信息
     * @return 是否新增成功
     */
    @LogMessage(content = "新增债券发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsBondIssueInfoVo cmsBondIssueInfo) {
        cmsBondIssueInfoService.saveBondIssueInfo(cmsBondIssueInfo, this.getCurrentUser());
        return success();
    }
    /**
     * 修改债券发行信息
     *
     * @param cmsBondIssueInfo
     *            债券发行信息
     * @return 是否修改成功
     */
    @LogMessage(content = "修改债券发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsBondIssueInfoVo cmsBondIssueInfo) {
        cmsBondIssueInfoService.updateBondIssueInfo(cmsBondIssueInfo, this.getCurrentUser());
        return success();
    }
    /**
     * 删除债券发行信息
     *
     * @param id
     *            债券发行信息id
     * @return 是否删除成功
     */
    @LogMessage(content = "删除债券发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsBondIssueInfoService.deleteBondIssueInfo(id, this.getCurrentUser());
        return success();
    }
    /**
     * 根据债券Id获取有效的债券发行编号
     *
     * @return 有效的债券类型集合
     */
    @GetMapping("/listValidBondIssueNum/{registerId}")
    @ResponseBody
    public List<CmsBondIssueInfoVo> listValidBondIssueNum(@PathVariable("registerId") String registerId) {
        return cmsBondIssueInfoService.listValidBondIssueNum(registerId);
    }
    /**
     * 债券还款计划登记表新增页面跳转
     *
     * @return 前端页面路径
     */
    @GetMapping("/addPlan")
    public String addPlan(CmsBondIssueInfoVo issueInfoVo, Model model) {
        String registerId = issueInfoVo.getRegisterId();
        if (StringUtils.isNotEmpty(registerId)) {
            CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(registerId);
            model.addAttribute("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
            if (cmsBondRegisterInfoVo != null) {
                // 债券发行信息
                CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoService.getByRegisterId(registerId);
                model.addAttribute("cmsBondIssueInfoVo", cmsBondIssueInfoVo);
                // 债券计划主表信息
                CmsBondRepayPlan planVO = cmsBondRepayPlanService.get(issueInfoVo.getPlanId(), true);
                if (planVO != null) {
                    // 暂时默认是 07 自定义，等后面开放其他还款方式再删除
                    planVO.setRepayType(CmsConstant.REPAY_TYPE_07);
                    model.addAttribute("cmsBondRepayPlanVo", planVO);
                }
            }
        }
        model.addAttribute("issueInfo", issueInfoVo);
        return "cms/bond/addBondIssueRepayPlan";
    }
    /**
     * 债券还款计划模块加载还款计划
     *
     * @param planId
     *            债劵注册id
     * @return 债券还款计划明细数据
     */
    @PostMapping("/listPlanDtl")
    @ResponseBody
    public List<CmsBondRepayPlanDtlVo> listPlanDtlByRegisterId(String planId) {
        return cmsBondRepayPlanDtlService.getByPlanId(planId);
    }
    /**
     * 保存债券发行还款计划数据
     *
     * @param cmsBondRepayPlanVo
     *            债券还款计划vo
     * @return 返回前端参数
     */
    @LogMessage(content = "保存债券发行还款计划数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/savePlan/{operator}")
    @ResponseBody
    public AjaxJson saveOrUpdatePlan(@PathVariable("operator") String operator, CmsBondRepayPlanVo cmsBondRepayPlanVo) {
        AjaxJson json = success();
        json.setObj(cmsBondRepayPlanService.issueSaveOrUpdate(operator, cmsBondRepayPlanVo, getCurrentUser()));
        return json;
    }
    /**
     * 根据债券Id获取有效的债券发行编号
     *
     * @return 有效的债券类型集合
     */
    @GetMapping("/getInfo/{issueId}")
    @ResponseBody
    public CmsBondIssueInfoVo getByRegisterId(@PathVariable("issueId") String issueId) {
        CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoService.getByIssueId(issueId);
        // 最大输入金额控制
        BigDecimal sendBalance = cmsBondPayService.completeSendBalance(cmsBondIssueInfoVo.getId(), null);
        cmsBondIssueInfoVo.setUnpaidMoney(sendBalance);
        return cmsBondIssueInfoVo;
    }
}
