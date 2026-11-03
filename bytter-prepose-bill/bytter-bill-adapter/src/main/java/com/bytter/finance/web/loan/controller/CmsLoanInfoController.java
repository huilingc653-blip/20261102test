package com.bytter.finance.web.loan.controller;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.guarantee.api.IGuaContractServie;
import com.bytter.biz.finacial.api.service.IFcInterestService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.AccrualTypeService;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.service.CmsLoanInfoService;
import com.bytter.finance.domain.loan.service.CmsLoanRepayService;
import com.bytter.finance.domain.loan.service.CmsLoanSendService;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.domain.loan.vo.CmsLoanSendVO;
import com.bytter.finance.domain.loan.vo.GuaContractCmsLoanVO;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.dto.CmsLoanRepayDTO;
import com.bytter.finance.dto.LoanTypeDTO;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.BtPurposeConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款信息表
 * @date 2017-07-21 09:46:16
 */
@Controller
@RequestMapping("/cms/loan/cmsloaninfo")
public class CmsLoanInfoController extends BaseController {
    @Autowired
    private CmsLoanInfoService cmsLoanInfoService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @DubboReference(check = false)
    private IBtPurposeService btPurposeService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IFcDayCloseService iFcDayCloseService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    @Autowired
    private IRatCpUrationService ucprationService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie contractOccupServie;
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsLoanRepayService cmsLoanRepayService;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private IFcInterestService fcInterestService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @DubboReference(check = false)
    private IDictService iDictService;
    @Autowired
    private AccrualTypeService accrualTypeService;
    @Autowired
    private LoanTypeService loanTypeService;
    @Autowired
    private IGuaContractServie guaContractService;
    /**
     * <b>功能介绍：</b>跳转修改列表<br>
     * <b>日期：</b>2017年7月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @LogMessage(content = "查询贷款信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/edit")
    public String toEdit(Model model) {
        model.addAttribute("pageType", "edit");
        return "cms/loan/createLoan/cmsLoanInfolist";
    }
    /**
     * <b>功能介绍：</b>跳转补录列表<br>
     * <b>日期：</b>2017年7月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/repair")
    public String toRepair(Model model) {
        model.addAttribute("pageType", "repair");
        return "cms/loan/createLoan/cmsLoanInfolist";
    }
    /**
     * <b>功能介绍：</b>跳转待发放列表<br>
     * <b>日期：</b>2017年7月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/send")
    public String toSend(Model model) {
        model.addAttribute("pageType", "send");
        return "cms/loan/createLoan/cmsLoanStaySend";
    }
    /**
     * <b>功能介绍：</b>跳转录入审批列表页面<br>
     * <b>日期：</b>2017年8月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listAddApprove")
    public String listAddApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("pageType", "add");
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/loan/createLoan/listLoanInfoApprove";
    }
    /**
     * <b>功能介绍：</b>新增流程手机审批详情页面<br>
     * <b>日期：</b>2017年10月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return
     * @version 1.0
     */
    @GetMapping("/mobileAddApprove")
    public String mobileAddApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            CmsLoanInfoVO cmsLoanInfo = cmsLoanInfoService.getCmsLoanInfoById(businessId);
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);
            model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
            model.addAttribute("taskId", taskId);
            model.addAttribute("pageType", "add");
        }
        return "cms/loan/createLoan/mobileAddApprove";
    }
    /**
     * <b>功能介绍：</b>补录流程手机审批详情页面<br>
     * <b>日期：</b>2017年10月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return
     * @version 1.0
     */
    @GetMapping("/mobileRepairApprove")
    public String mobileRepairApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            CmsLoanInfoVO cmsLoanInfo = cmsLoanInfoService.getCmsLoanInfoById(businessId);
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);
            model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
            model.addAttribute("taskId", taskId);
            model.addAttribute("pageType", "repair");
        }
        return "cms/loan/createLoan/mobileAddApprove";
    }
    /**
     * <b>功能介绍：</b>跳转补录审批页面<br>
     * <b>日期：</b>2017年8月22日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listRepairApprove")
    public String listRepairApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("pageType", "repair");
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/loan/createLoan/listLoanInfoApprove";
    }
    /**
     * <b>功能介绍：</b>列表数据<br>
     * <b>日期：</b>2017年7月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/listdatatable")
    @ResponseBody
    public DataTablesOutput listdatatable(@RequestBody DataTablesInput dataTablesInput, String pageType) {
        String resouse = null;
        if ("repair".equals(pageType)) {// 需修改的数据
            resouse = ResourceConstant.CMS_LOAN_REPAIR_QUERY;
        } else {
            resouse = ResourceConstant.CMS_LOAN_INFO_QUERY;
        }
        return cmsLoanInfoService.getEditDataTables(dataTablesInput, pageType, getUserId(), resouse);
    }
    /**
     * 内部贷款查询页面跳转
     */
    @GetMapping("/listQuery")
    public String listQuery(Model model, HttpServletRequest request) {
        String corpId = request.getParameter("corpId");
        ConfigDTO relatedContract = cmsLoanInfoService.getRelatedContract();
        model.addAttribute("relateCon", relatedContract.getValue());
        model.addAttribute("corpId", corpId);
        return "cms/loan/listCmsLoanQuery";
    }
    /**
     * 查询页面跳转
     */
    @PostMapping("/dataTableLoanQuery")
    @ResponseBody
    public DataTablesOutput dataTableLoanQuery(@RequestBody DataTablesInput input) {
        String corpIdExtend = (String) input.getFormdata().get("corpIdExtend");
        String ins_corpId = (String) input.getFormdata().get("ins_corpId");
        if (StringUtils.isNotEmpty(corpIdExtend)) {
            if (StringUtils.isNotEmpty(ins_corpId)) {
                corpIdExtend = "," + corpIdExtend;
            }
            ins_corpId = ins_corpId + corpIdExtend;
            input.getFormdata().put("ins_corpId", ins_corpId);
        }
        // logger.debug("-------------------------------------------dataTableLoanQuery ins_corpId ="+ins_corpId);
        return cmsLoanInfoService.dataTableLoanQuery(input, getUserId());
    }
    /**
     * <b>功能介绍：</b>贷款发放列表数据<br>
     * <b>日期：</b>2017年8月8日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/listCmsLoanStaySendData")
    @ResponseBody
    public DataTablesOutput listCmsLoanStaySendData(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanInfoService.listCmsLoanStaySendData(dataTablesInput, getUserId());
    }
    /**
     * <b>功能介绍：</b>贷款申请审批列表数据<br>
     * <b>日期：</b>2017年8月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/listAddApproveData")
    @ResponseBody
    public DataTablesOutput listAddApproveData(@RequestBody DataTablesInput dataTablesInput, String pageType) {
        return cmsLoanInfoService.listAddApprove(dataTablesInput, pageType);
    }
    /**
     * <b>功能介绍：</b>单笔录入审批<br>
     * <b>日期：</b>2017年8月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/approve")
    public String approve(Model model, String id, String taskId, String pageType) {
        boolean isCharging = false;
        if (StringUtils.isNotEmpty(id)) {
            CmsLoanInfoVO cmsLoanInfo = cmsLoanInfoService.getCmsLoanInfoById(id);
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            if (CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
                model.addAttribute("cpURationDTO",
                        ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
                BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
                model.addAttribute("cur", cur);
            }
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("pageType", pageType);
            model.addAttribute("isCharging", isCharging);
            ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
            model.addAttribute("isGuarantee", isGuarantee.getValue());
        }
        return "cms/loan/createLoan/cmsLoanInfoApprove";
    }
    /**
     * 贷款信息表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(CmsLoanInfo cmsLoanInfo, Model model) {
        boolean isCharging = false;// 是否启用台账 FIXME 默认不启用
        if (StringUtils.isNotEmpty(cmsLoanInfo.getId())) {
            cmsLoanInfo = cmsLoanInfoService.get(cmsLoanInfo.getId());
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);
        }
        ConfigDTO config = cmsLoanInfoService.getRelateRat();
        model.addAttribute("relateRat", config.getValue());// 挂接授信
        model.addAttribute("isCharging", isCharging);
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.addAttribute("isGuarantee", isGuarantee.getValue());
        if (isGuarantee.getValue().equals(CmsConstant.IS_TRUE)) {
            model.addAttribute("isHangGuarantee", cmsLoanInfo.getIsHangGuarantee());
        }
        model.addAttribute("isCharging", isCharging);
        // model.addAttribute("isAssure", isAssure);
        model.addAttribute("createDate", DateUtils.date2Str(DateUtils.date_sdf()));
        return "cms/loan/createLoan/addCmsLoanInfo";
    }
    /**
     * <b>功能介绍：</b>跳转放款申请页面<br>
     * <b>日期：</b>2017年7月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/addloansend")
    public String addloansend(CmsLoanInfo cmsLoanInfo, Model model, String sendBillCode) {
        boolean isCharging = false;
        CmsLoanInfoVO cmsLoanInfovo;
        if (StringUtils.isNotEmpty(cmsLoanInfo.getId())) {
            cmsLoanInfovo = cmsLoanInfoService.getCmsLoanInfoById(cmsLoanInfo.getId());
            LoanTypeDTO loanTypeDTO = loanTypeService.queryLoanTypeByCode(cmsLoanInfovo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT);
            cmsLoanInfovo.setLoanGenreName(loanTypeDTO.getName());// 设置贷款类型名称
            cmsLoanInfovo.setLoanMoney(new BigDecimal(cmsLoanInfovo.getLoanMoney().doubleValue()));
            // 贷款种类Id loanGenreId
            cmsLoanInfovo.setLoanGenreId(loanTypeDTO.getId());// 设置贷款类型id
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfovo);
            model.addAttribute("sendCount", cmsLoanInfoService.isFirstPay(cmsLoanInfovo.getBillCode()) + 1);// 设置分发次数
            model.addAttribute("notSendMoney", cmsLoanInfovo.getLoanMoney()
                    .subtract(cmsLoanInfoService.getSendedMoney(cmsLoanInfovo.getBillCode())));// 设置待发放金额
            model.addAttribute("createDate", DateUtils.date2Str(cmsLoanInfovo.getCreateDate(), DateUtils.date_sdf()));
            model.addAttribute("createEnterDate", DateUtils.date2Str(DateUtils.date_sdf()));
            model.addAttribute("naturalSign",
                    cmsLoanInfoService.getCurrencyDTO(cmsLoanInfovo.getCurCode()).getNaturalSign());// 本位币标识
            Date dayCloseDate = iFcDayCloseService.getDayCloseFinalDate(cmsLoanInfovo.getNetCode());// 获取网点日结日期
            model.addAttribute("dayCloseDate",
                    DateUtils.format(DateUtils.addDay(dayCloseDate, 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));// 控制发放日期
        } else {
            cmsLoanInfovo = new CmsLoanInfoVO();
        }
        if (CmsConstant.IS_TRUE.equals(cmsLoanInfovo.getIsHangCredit())) {
            model.addAttribute("cpURationDTO",
                    ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfovo.getId()));// 授信占用明细
        }
        // 查询发放的 还款计划
        CmsRepaymentSchedule crs = repaymentScheduleService.getCmsRepaymentScheduleByBillCode(cmsLoanInfo.getId());
        if (crs != null) {
            model.addAttribute("planId", crs.getId());
        }
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        model.addAttribute("cur", cur);
        model.addAttribute("isCharging", isCharging);
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.addAttribute("isGuarantee", isGuarantee.getValue());
        model.addAttribute("sendBillCode", sendBillCode);
        return "cms/loan/createLoan/addCmsLoansend";
    }
    /**
     * <b>功能介绍：</b>跳转次数对应的发放申请列表<br>
     * <b>日期：</b>2017年8月9日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/sendInfolist")
    public String sendInfolist(String billCode, String type, Model model) {
        if (EmptyUtils.isNotEmpty(billCode)) {
            List<CmsLoanSendVO> cmsloanSends = cmsLoanInfoService.getLoanSendItem(billCode, type);
            model.addAttribute("cmsloanSends", cmsloanSends);
        }
        return "cms/loan/createLoan/listLoanSendItem";
    }
    /**
     * 贷款信息表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(CmsLoanInfo cmsLoanInfo, Model model) {
        boolean isCharging = false;
        if (StringUtils.isNotEmpty(cmsLoanInfo.getId())) {
            cmsLoanInfo = cmsLoanInfoService.get(cmsLoanInfo.getId());
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);
            model.addAttribute("createDate", DateUtils.date2Str(cmsLoanInfo.getCreateDate(), DateUtils.date_sdf()));
            model.addAttribute("netName", cmsLoanInfoService.getCenNetId(cmsLoanInfo.getCorpId()).get("corpName"));
        }
        ConfigDTO config = cmsLoanInfoService.getRelateRat();// 贷款参数设置是否授信挂接
        if (CmsConstant.IS_TRUE.equals(config.getValue())) {
            if (CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
                model.addAttribute("cpURationDTO",
                        ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
            }
        }
        model.addAttribute("relateRat", config.getValue());
        model.addAttribute("isCharging", isCharging);
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.addAttribute("isGuarantee", isGuarantee.getValue());// 业务模块是否挂担保参数设置
        if (isGuarantee.getValue().equals(CmsConstant.IS_TRUE)) {
            model.addAttribute("isHangGuarantee", cmsLoanInfo.getIsHangGuarantee());// 业务是否挂担保
            if (cmsLoanInfo.getIsHangGuarantee() != null
                    && cmsLoanInfo.getIsHangGuarantee().equals(CmsConstant.IS_TRUE)) {
                setGuaContractMsg(cmsLoanInfo, model);
            }
        }
        return "cms/loan/createLoan/updateCmsLoanInfo";
    }
    /**
     * <b>功能介绍：</b>贷款信息表补录页面跳转<br>
     * <b>日期：</b>2017年7月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/repairData")
    public String repairData(CmsLoanInfo cmsLoanInfo, Model model) {
        boolean isCharging = false;
        CmsLoanInfoVO cmsLoanInfovo;
        if (StringUtils.isNotEmpty(cmsLoanInfo.getId())) {
            cmsLoanInfovo = cmsLoanInfoService.getCmsLoanInfoById(cmsLoanInfo.getId());
            LoanTypeDTO loanTypeDTO = loanTypeService.queryLoanTypeByCode(cmsLoanInfovo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT);
            cmsLoanInfovo.setLoanGenreName(loanTypeDTO.getName());// 设置贷款类型名称
            cmsLoanInfovo.setLoanMoney(new BigDecimal(cmsLoanInfovo.getLoanMoney().doubleValue()));
            // 贷款种类Id loanGenreId
            cmsLoanInfovo.setLoanGenreId(loanTypeDTO.getId());// 设置贷款类型id
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfovo);
            model.addAttribute("createDate", DateUtils.date2Str(cmsLoanInfo.getCreateDate(), DateUtils.date_sdf()));
            model.addAttribute("naturalSign",
                    cmsLoanInfoService.getCurrencyDTO(cmsLoanInfovo.getCurCode()).getNaturalSign());// 本位币标识
        } else {
            cmsLoanInfovo = new CmsLoanInfoVO();
        }
        ConfigDTO config = cmsLoanInfoService.getRelateRat();// 贷款参数设置是否授信挂接
        if (CmsConstant.IS_TRUE.equals(config.getValue())) {
            if (CmsConstant.IS_TRUE.equals(cmsLoanInfovo.getIsHangCredit())) {
                model.addAttribute("cpURationDTO",
                        ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfovo.getId()));// 授信占用明细
            }
        }
        model.addAttribute("relateRat", config.getValue());
        model.addAttribute("isCharging", isCharging);
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.addAttribute("isGuarantee", isGuarantee.getValue());
        if (isGuarantee.getValue().equals(CmsConstant.IS_TRUE)) {
            CmsLoanInfo cmsLoan = new CmsLoanInfo();
            BeanUtils.copyNotNullProperties(cmsLoanInfovo, cmsLoan);
            model.addAttribute("isHangGuarantee", cmsLoan.getIsHangGuarantee());
            if (cmsLoan.getIsHangGuarantee() != null && cmsLoan.getIsHangGuarantee().equals(CmsConstant.IS_TRUE)) {
                setGuaContractMsg(cmsLoan, model);
            }
        }
        ConfigDTO relatedContract = cmsLoanInfoService.getRelatedContract();// 信贷参数是否对接合同管理
        if (CmsConstant.RELATED_CONTRACT_YES.equals(relatedContract.getValue())) { // 对接合同管理：是
            ConfigDTO conType = cmsLoanInfoService.getRelatedConType(); // 对接合同管理模式
            model.addAttribute("conType", conType.getValue());
            model.addAttribute("conId", cmsLoanInfo.getConId());
        }
        // 查询发放的 还款计划
        CmsRepaymentSchedule crs = repaymentScheduleService.getCmsRepaymentScheduleByBillCode(cmsLoanInfo.getId());
        if (crs != null) {
            model.addAttribute("planId", crs.getId());
        }
        model.addAttribute("relateCon", relatedContract.getValue());
        return "cms/loan/createLoan/repairCmsLoanInfo";
    }
    /**
     * 贷款信息表详情页
     *
     * @return
     */
    @LogMessage(content = "查询贷款信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        boolean isCharging = false;
        CmsLoanInfoVO cmsLoanInfo = cmsLoanInfoService.getCmsLoanInfoById(id);
        cmsLoanInfo.setLoanGenreName(loanTypeService.queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
        if (CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
            model.addAttribute("cpURationDTO",
                    ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
            BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
            model.addAttribute("cur", cur);
        }
        model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
        model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);
        // 计息方式信息
        /*
         * String interestWayName = iDictService.get("Admin.PredrawSign", cmsLoanInfo.getInterestWay()); if
         * (EmptyUtils.isBlank(interestWayName)) { AccrualTypeVo accrualTypeVo =
         * accrualTypeService.queryAccrualTypeByCode(cmsLoanInfo.getInterestWay()); interestWayName =
         * accrualTypeVo.getAccrualName(); } model.addAttribute("interestWayName", interestWayName);
         */
        model.addAttribute("isCharging", isCharging);
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.addAttribute("isGuarantee", isGuarantee.getValue());
        model.addAttribute("viewType", "loanInfo");
        return "cms/loan/createLoan/viewCmsLoanInfo";
    }
    /**
     * 贷款信息表详情页
     *
     * @return
     */
    @LogMessage(content = "查询贷款信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByBillCode/{billCode}")
    public String viewByBillCode(@PathVariable("billCode") String billCode, Model model) {
        boolean isCharging = false;
        String param = "";
        String strPrefix = "";
        String page = "";
        if (StringUtils.isNotEmpty(billCode)) {
            strPrefix = billCode.substring(0, 2);
            if(strPrefix.equals("A1")){
                CmsLoanInfoVO cmsLoanInfo = cmsLoanInfoService.getLoanInfoByBillCode(billCode);
                cmsLoanInfo.setLoanGenreName(loanTypeService.queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
                if (CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
                    model.addAttribute("cpURationDTO",
                            ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
                    BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
                    model.addAttribute("cur", cur);
                }
                model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
                model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);
                model.addAttribute("isCharging", isCharging);
                ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
                model.addAttribute("isGuarantee", isGuarantee.getValue());
                model.addAttribute("viewType", "loanInfo");
                page = "cms/loan/createLoan/viewCmsLoanInfo";
            }
            if (strPrefix.equals("A2")) {// 发放数据
                CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendVOByBillCode(billCode);
                CmsLoanInfoVO cmsLoanInfo = cmsLoanSendService
                        .getLoanInfoByLoanSend(cmsLoanSendService.get(cmsLoanSend.getId(), true));
                cmsLoanInfo.setLoanGenreName(loanTypeService
                        .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
                if (RationConstant.RAT_COMMON_Y.equals(cmsLoanInfo.getIsHangCredit())) {
                    model.addAttribute("cpURationDTO",
                            ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
                }
                model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 发放页
                model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 申请页
                model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
                model.addAttribute("repayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
                model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
                model.addAttribute("isCharging", isCharging);
                if (StringUtils.isEmpty(param)) {
                    model.addAttribute("param", true);
                } else {
                    model.addAttribute("param", false);
                }
                model.addAttribute("show", 0);
                page = "cms/loan/createLoan/viewCmsLoanSend";
            } else if (strPrefix.equals("A3")) {// 还款数据
                CmsLoanRepayDTO cmsLoanRepay = cmsLoanRepayService.getCmsLoanRepayByBillCode(billCode);
                CmsLoanSend cmsLoanSend = cmsLoanRepayService.getCmsLoanSend(cmsLoanRepay.getSendCode());
                CmsLoanInfoVO cmsLoanInfo = cmsLoanRepayService.getCmsLoanInfo(cmsLoanRepay.getSendCode());
                cmsLoanInfo.setLoanGenreName(loanTypeService
                        .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
                model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
                model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
                model.addAttribute("cmsLoanRepayPage", cmsLoanRepay);
                model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
                model.addAttribute("repayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
                page = "cms/loan/loanRepay/viewCmsLoanRepay";
            }
        }
        model.addAttribute("viewFlag", "0");// 查看标识
        return page;
    }
    /**
     * 添加贷款信息表
     *
     * @param cmsLoanInfoVO
     * @return
     */
    @LogMessage(content = "添加贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsLoanInfoVO cmsLoanInfoVO, RatUrationDTO urationDTO) {
        if (StringUtils.isEmpty(cmsLoanInfoVO.getId())) {
            // 生成编号
            String billCode = cmsLoanInfoService.getLoanInfoBillCode(CmsConstant.LOAN_INFO_BILL_CODE_PREFIX);
            cmsLoanInfoVO.setBillCode(billCode);
        }
        cmsLoanInfoService.saveLoanInfo(cmsLoanInfoVO, getCurrentUser(), urationDTO);
        return success();
    }
    /**
     * 修改贷款信息表(校验)
     *
     * @param corpId
     * @return
     */
    @LogMessage(content = "校验修改贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/check")
    @ResponseBody
    public boolean check(DataTablesInput input, String corpId, String isHangGuarantee) {
        boolean flag = true;
        if (!EmptyUtils.isEmpty(isHangGuarantee) && CmsConstant.IS_TRUE.equals(isHangGuarantee)) {// 挂接（是）
//            DataTablesOutput output = cmsLoanInfoService.getApprovedDataTables(input, corpId);
//            if (output.getData().size() == 0) {
//                flag = false;
//            }
            flag = guaContractService.checkContractApprovedList(corpId);
        }
        return flag;
    }
    /**
     * 修改贷款信息表
     *
     * @param cmsLoanInfoVO
     * @return
     */
    @LogMessage(content = "修改贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(DataTablesInput input, CmsLoanInfoVO cmsLoanInfoVO, RatUrationDTO urationDTO) {
        cmsLoanInfoService.updateLoanInfo(cmsLoanInfoVO, getCurrentUser(), urationDTO);
        return success();
    }
    /**
     * <b>功能介绍：</b>打回贷款信息表<br>
     * <b>日期：</b>2017年7月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(CmsLoanInfo cmsLoanInfo) {
        CmsLoanInfo loanInfo = cmsLoanInfoService.get(cmsLoanInfo.getId());
        loanInfo.setStatus(CmsConstant.BACK);
        loanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_12);
        if (EmptyUtils.isNotEmpty(cmsLoanInfo.getRefuseWhy())) {// 设置打回原因
            loanInfo.setRefuseWhy(cmsLoanInfo.getRefuseWhy());
        }
        notificationService.complete(NotificationConstant.CMS.LOAN_REPAIR, loanInfo.getId(), getCurrentUser().getId());
        // 发送一条录入消息
        notificationService.sendToUser(loanInfo.getId(), loanInfo.getCreateBy(), NotificationConstant.CMS.LOAN_ADD,
                loanInfo);
        cmsLoanInfoService.update(loanInfo);
        cmsLoanInfoService.repairBackInsertMsg(loanInfo, getCurrentUser());
        // 补录打回,授信冻结额度还原
        if (!EmptyUtils.isEmpty(loanInfo.getIsHangCredit()) && CmsConstant.IS_TRUE.equals(loanInfo.getIsHangCredit())) {
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, loanInfo.getId());
            ucprationService.backUseCpRation(urationDTO);
        }
        // 担保挂接
        if (!EmptyUtils.isEmpty(loanInfo.getIsHangGuarantee())
                && CmsConstant.IS_TRUE.equals(loanInfo.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(loanInfo.getId());
            contractOccupServie.backUseGuaContractOccup(guaContractOccupDTO);
        }
        return success();
    }
    /**
     * <b>功能介绍：</b>发放打回<br>
     * <b>日期：</b>2017年7月28日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/sendBack/{id}")
    @ResponseBody
    public AjaxJson sendBack(@PathVariable("id") String id) {
        CmsLoanInfo cmsLoanInfo = cmsLoanInfoService.get(id);
        notificationService.complete(NotificationConstant.CMS.LOAN_SEND, id, getCurrentUser().getId());
        List<CmsLoanSend> sends = cmsLoanInfoService.getCmsLoanSendByLoanCode(cmsLoanInfo.getBillCode());
        for (CmsLoanSend sendInfo : sends) {// 标记发放打回发送的消息为完成
            notificationService.complete(NotificationConstant.CMS.LOAN_SEND, sendInfo.getId(),
                    getCurrentUser().getId());
        }
        notificationService.sendToUser(id, cmsLoanInfo.getCreateBy(), NotificationConstant.CMS.LOAN_REPAIR,
                cmsLoanInfo);
        cmsLoanInfo.setStatus(CmsConstant.APPROVED);// 修改到补录状态
        cmsLoanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_27);
        // 打回时如果对接合同管理，对合同信息进行处理
        if (StringUtils.isNotEmpty(cmsLoanInfo.getConId())) {
            // 取消合同挂接
            conCommonService.unHookConOp(cmsLoanInfo.getConId(), cmsLoanInfo.getId(), getCurrentUser());
            if (StringUtils.isNotEmpty(cmsLoanInfo.getConTypeId())) { // 业务生成合同
                cmsLoanInfo.setConId("");
                cmsLoanInfo.setConTypeId("");
            }
        }
        cmsLoanInfoService.update(cmsLoanInfo);
        cmsLoanInfoService.sendBackInsertMsg(cmsLoanInfo, getCurrentUser());
        // 发放打回,授信占用后打回(审批通过后打回)
        if (!EmptyUtils.isEmpty(cmsLoanInfo.getIsHangCredit())
                && CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, cmsLoanInfo.getId());
            urationDTO.setAllRelease(true);// 全部释放
            ucprationService.preBackUseRation(urationDTO);
        }
        // 担保挂接
        if (!EmptyUtils.isEmpty(cmsLoanInfo.getIsHangGuarantee())
                && CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(cmsLoanInfo.getId());
            contractOccupServie.sendBackUseGuaContractOccup(guaContractOccupDTO);
        }
        return success();
    }
    /**
     * 删除贷款信息表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        CmsLoanInfo cmsLoanInfo = cmsLoanInfoService.get(id);
        cmsLoanInfo.setStatus(CmsConstant.DELETED);// 更新状态为-2
        cmsLoanInfoService.updateEntity(cmsLoanInfo);
        // 授信挂接
        if (!EmptyUtils.isEmpty(cmsLoanInfo.getIsHangCredit())
                && CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, id);
            ucprationService.deleteUseCpRation(urationDTO);
        }
        notificationService.complete(NotificationConstant.CMS.LOAN_ADD, cmsLoanInfo.getId(), getCurrentUser().getId());
        return success();
    }
    /**
     * 批量删除贷款信息表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            CmsLoanInfo cmsLoanInfo = cmsLoanInfoService.get(id);
            cmsLoanInfoService.deleteEntity(cmsLoanInfo);
        }
        return success();
    }
    @GetMapping("/LoanTypeJson")
    @ResponseBody
    public List<LoanTypeDTO> LoanTypeJson() {
        //TODO 不需要走DubboReference
//        return cmsLoanInfoService.queryLoanTypeList(BaseDataConstant.TYPE_CREDIT);
        return loanTypeService.queryLoanTypeList(BaseDataConstant.TYPE_CREDIT);
    }
    /**
     * <b>功能介绍：</b>获取单位直属网点<br>
     * <b>日期：</b>2017年7月21日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/getCenNetId")
    @ResponseBody
    public Map<String, String> getCenNetId(String corpId) {
//        AjaxJson json = new AjaxJson();
        Map<String, String> corp = cmsLoanInfoService.getCenNetId(corpId);
//        json.setObj(corp);
//        json.setSuccess(true);
        return corp;
    }
    /**
     * <b>功能介绍：</b>获取利率<br>
     * <b>日期：</b>2017年8月9日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/getRate")
    @ResponseBody
    public LoanRateMaintenceVO getRate(String loanDate, String referenceRateType, String curId, String repayCycle) {
        LoanRateMaintenceVO rate = cmsPrecativeLoanInfoService.getRate(loanDate, referenceRateType, curId, repayCycle);
        return rate;
    }
    /**
     * 获取用途
     */
    @GetMapping("/getPurpose")
    @ResponseBody
    public List<BtPurposeDTO> listByCorpIdOrBusType(String corpId, String sysBusType) {
        List<BtPurposeDTO> btPurposeDTOs = new ArrayList<BtPurposeDTO>();
        if (EmptyUtils.isEmpty(corpId) || "null".equals(corpId)) {
            return new ArrayList<BtPurposeDTO>();
        }
        btPurposeDTOs = btPurposeService.listByCorpIdOrBusType(corpId, BtPurposeConstant.SYS_BUS_TYPE_CMS);
        if (EmptyUtils.isEmptyList(btPurposeDTOs)) {
            return new ArrayList<BtPurposeDTO>();
        } else {
            return btPurposeDTOs;
        }
    }
    public void setGuaContractMsg(CmsLoanInfo cmsLoanInfo, Model model) {
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        guaContractOccupDTO.setBusinessId(cmsLoanInfo.getId());
        guaContractOccupDTO.setStatus(GuaranteeConstant.GUA_CONTRACT_TEMP);// 暂存状态
        GuaContractOccupDTO guaContractOccup = contractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
        List<String> idList = guaContractOccup.getContractIds();
        String contractIds = "";
        for (String id : idList) {
            contractIds += id + ",";
        }
        model.addAttribute("idList", idList);// 担保合同id List
        model.addAttribute("contractIds", contractIds);// 担保合同id+担保合同号，存入业务表的字段
        model.addAttribute("guaWayNames", guaContractOccup.getGuaWayName());// 担保方式名称组合
        model.addAttribute("guaWay", guaContractOccup.getGuaWay());// 担保方式code组合
        model.addAttribute("guaranteeRmk", guaContractOccup.getGuaRmk());
        model.addAttribute("contractCode", guaContractOccup.getContractCode());// 担保合同编号组合，逗号隔开
    }
    /**
     * 查询内部贷款补录完成的数据
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/loanInfoList")
    @ResponseBody
    public DataTablesOutput loanInfoList(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanInfoService.loanInfoList(dataTablesInput, getUserId());
    }
    /**
     * 计息信息查询
     *
     * @param loanAcc
     * @return
     */
    @LogMessage(content = "计息信息查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/interestView")
    public String interestView(String loanAcc, Model model) {
        // 内部贷款-参数设置不记账，点击计息信息超链接，跳转计息信息页面显示为空
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.IS_ACCOUNTING);
        if (EmptyUtils.isEmpty(configDTO) || "0".equals(configDTO.getValue())) {
            model.addAttribute("interestList", new ArrayList<>());
            return "cms/loan/interestView";
        }
        // 利息明细查询
        String isPool = "0";
        List<FcInterestDtlDTO> list = fcInterestService.getFcInterestDtlByAcc(loanAcc, isPool);
        model.addAttribute("interestList", list);
        return "cms/loan/interestView";
    }
    /**
     * 获取挂接担保合同的内部借款数据
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @ResponseBody
    @PostMapping("/getGuaContract")
    public DataTablesOutput<GuaContractCmsLoanVO> getUseChangelist(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanInfoService.getGuaForLoan(dataTablesInput, getCurrentUser());
    }
}
