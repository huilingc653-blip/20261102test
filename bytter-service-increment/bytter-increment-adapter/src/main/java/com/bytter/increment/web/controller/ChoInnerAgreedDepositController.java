package com.bytter.increment.web.controller;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.InnerAccountConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.AgreedConstant;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.enums.ChoInnerFinanceMessageEnum;
import com.bytter.increment.domain.entity.FcAgreedDetail;
import com.bytter.increment.domain.service.ChoInnerAgreedDepositService;
import com.bytter.increment.domain.service.FcAgreedDetailService;
import com.bytter.increment.domain.vo.FcAccVO;
import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: 内部存款-协定存款登记表
 * @date 2019-05-10 16:44:38
 */
@Controller
@RequestMapping("/agreeddeposit/choinneragreeddeposit")
public class ChoInnerAgreedDepositController extends BaseController {
    @Autowired
    private ChoInnerAgreedDepositService choInnerAgreedDepositService;
    @Autowired
    private FcAgreedDetailService fcAgreedDetailService;
    @DubboReference(check = false)
	private ICorpService iCorpService;
    @DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
    @DubboReference(check = false)
	private IInnerAccountService iInnerAccountService;
    @DubboReference(check = false)
	private IFcDayCloseService fcDayCloseService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    /**
     * 协定存款登记列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询协定存款登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_REG_QUERY)
    @GetMapping("/listRegCho")
    public String listRegCho(Model model) {
        model.addAttribute("busType", InnerFinanceConstant.BUS_TYPE_0);
        return "agreeddeposit/listRegChoAgreedDeposit";
    }
    /**
     * 协定存款到期处理列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询协定存款到期处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_POS_QUERY)
    @GetMapping("/listPosCho")
    public String listPos(Model model) {
        model.addAttribute("busType", InnerFinanceConstant.BUS_TYPE_1);
        return "agreeddeposit/listPosChoAgreedDeposit";
    }
    /**
     * 协定存款终止列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询协定存款终止列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_TERM_QUERY)
    @GetMapping("/listTermCho")
    public String listTerm(Model model) {
        model.addAttribute("busType", InnerFinanceConstant.BUS_TYPE_2);
        return "agreeddeposit/listTermChoAgreedDeposit";
    }
    /**
     * 内部存款-协定存款登记表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(value = {ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_REG_QUERY,
//            ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_POS_QUERY,
//            ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_TERM_QUERY},
//            logical = Logical.OR)
    @PostMapping("/datatablecho")
    @ResponseBody
    public DataTablesOutput dataTableCho(@RequestBody DataTablesInput dataTablesInput) {
        ICurrentUser user = this.getCurrentUser();
        return choInnerAgreedDepositService.getDataTablesUnion(dataTablesInput, user);
    }
    /**
     * 跳转新增协定账号
     *
     * @return
     */
    @GetMapping("/addAgreedAcc")
    public String addAgreedAcc(FcAgreedDepositVO fcAgreedDepositVO, Model model) {
        model.addAttribute("beginDate", DateUtils.formatDate(fcAgreedDepositVO.getBeginDate()));
        // 获取单位直属网点(如果没有直属网点就是本网点)
        CorpDTO netCorp = iCorpService.getNetByCorpId(fcAgreedDepositVO.getCorpId());
        if (netCorp == null) {
            netCorp = new CorpDTO();
            netCorp.setId(fcAgreedDepositVO.getCorpId());
            netCorp.setCode(iCorpService.get(fcAgreedDepositVO.getCorpId()).getCode());
        }
        model.addAttribute("netInfo", netCorp);
        //币别信息
        model.addAttribute("curInfo", iBtCurrencyService.getObjectById(fcAgreedDepositVO.getCurId()));
        CorpDTO corpDTO = iCorpService.get(fcAgreedDepositVO.getCorpId());
        model.addAttribute("corpInfo", corpDTO);
        //账号信息
        List<String> accIds = new ArrayList<>();
        accIds.add(fcAgreedDepositVO.getAccId());
        List<String> accState = new ArrayList<>();
        accState.add(InnerAccountConstant.ACC_STATE_NORMAL);
        List<FcAccDTO> fcAcc = iInnerAccountService.listFcAccInfo(accIds, accState);
        if (!EmptyUtils.isEmptyList(fcAcc)) {
            model.addAttribute("accInfo", fcAcc.get(0));
        }
        FcAccDTO agrAcc = null;
        //协定账号信息
        if (StringUtils.isNotBlank(fcAgreedDepositVO.getAgrAcc())) {
            agrAcc = iInnerAccountService.getByBlnctrAcc(fcAgreedDepositVO.getAgrAcc());
            int accAccNoRule = configService.getConfig("fcs", "accAccnoRule").toInteger(1).intValue();
            if (!EmptyUtils.isEmpty(agrAcc)) {
                agrAcc.setAccAccNoRule(accAccNoRule);
            }
        } else {
            agrAcc = new FcAccDTO();
            agrAcc.setInterestSign("1");
        }
        model.addAttribute("agrAccInfo", agrAcc);
        model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
        //顺序号生成模式
        int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
        model.addAttribute("seqNumPage", seqGeneratingMode);
        //根据资金池标志生成内部账号生成法则
        String reVal = fcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
        int relen = reVal.length();
        model.addAttribute("relen", relen);
        model.addAttribute("poolSign","0");
        return "agreeddeposit/addAgreedAcc";
    }
    /**
     * 协定存款登记新增页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_REG_APPLY)
    @GetMapping("/addChoReg")
    public String addChoReg(FcAgreedDepositVO fcAgreedDepositVO, Model model) {
        if (StringUtils.isNotEmpty(fcAgreedDepositVO.getId())) {
            fcAgreedDepositVO = choInnerAgreedDepositService.getByUnion(fcAgreedDepositVO.getId());
        }
        model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
        return "agreeddeposit/addChoRegAgreedDeposit";
    }
    /**
     * 协定存款登记编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_REG_APPLY)
    @GetMapping("/updateChoReg")
    public String updateChoReg(FcAgreedDepositVO fcAgreedDeposit, Model model) {
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        if (fcAgreedDepositVO != null) {
            model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
            Date closeDate = fcDayCloseService.getDayCloseFinalDate(fcAgreedDepositVO.getNetId());
            model.addAttribute("closeDate", closeDate);
            FcAccDTO fcAccDTO = iInnerAccountService.getByBlnctrAcc(fcAgreedDepositVO.getAgrAcc());
            model.addAttribute("fcAccPage", fcAccDTO);
        }
        return "agreeddeposit/updateChoRegAgreedDeposit";
    }
    /**
     * 协定存款延期新增页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_POS_APPLY)
    @GetMapping("/addChoPos")
    public String addChoPos(FcAgreedDepositVO fcAgreedDepositVO, Model model) {
        if (StringUtils.isNotEmpty(fcAgreedDepositVO.getId())) {
            fcAgreedDepositVO = choInnerAgreedDepositService.getByUnion(fcAgreedDepositVO.getId());
        }
        model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
        return "agreeddeposit/addChoPosAgreedDeposit";
    }
    /**
     * 协定存款延期编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_POS_APPLY)
    @GetMapping("/updateChoPos")
    public String updateChoPos(FcAgreedDepositVO fcAgreedDeposit, Model model) {
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        if (fcAgreedDepositVO != null) {
            model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
        }
        return "agreeddeposit/updateChoPosAgreedDeposit";
    }
    /**
     * 协定存款终止新增页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_TERM_APPLY)
    @GetMapping("/addChoTerm")
    public String addChoTerm(FcAgreedDepositVO fcAgreedDepositVO, Model model) {
        if (StringUtils.isNotEmpty(fcAgreedDepositVO.getId())) {
            fcAgreedDepositVO = choInnerAgreedDepositService.getByUnion(fcAgreedDepositVO.getId());
        }
        model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
        return "agreeddeposit/addChoTermAgreedDeposit";
    }
    /**
     * 协定存款终止编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_TERM_APPLY)
    @GetMapping("/updateChoTerm")
    public String updateChoTerm(FcAgreedDepositVO fcAgreedDeposit, Model model) {
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        if (fcAgreedDepositVO != null) {
            model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
        }
        return "agreeddeposit/updateChoTermAgreedDeposit";
    }
    /**
     * 添加协定存款登记表
     *
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "添加协定存款登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_REG_APPLY)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDeposit.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FC_AGREED_DEPOSIT));
            choInnerAgreedDepositService.saveChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 添加协定存款延期登记表
     *
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "添加协定存款延期登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_POS_APPLY)
    @PostMapping("/savePos")
    @ResponseBody
    public AjaxJson savePos(FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDeposit.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FC_AGREED_DEPOSIT));
            choInnerAgreedDepositService.saveChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 添加协定存款终止登记表
     *
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "添加协定存款终止登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_TERM_APPLY)
    @PostMapping("/saveTerm")
    @ResponseBody
    public AjaxJson saveTerm(FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDeposit.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FC_AGREED_DEPOSIT));
            choInnerAgreedDepositService.saveChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 修改协定存款登记登记表
     *
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "修改协定存款登记登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_REG_APPLY)
    @PostMapping("/modifyReg")
    @ResponseBody
    public AjaxJson update(FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            choInnerAgreedDepositService.updateChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser(), false);
        }
        return success();
    }
    /**
     * 修改协定存款延期表
     *
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "修改协定存款延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_POS_APPLY)
    @PostMapping("/modifyPos")
    @ResponseBody
    public AjaxJson modifyPos(FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            choInnerAgreedDepositService.updateChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser(), false);
        }
        return success();
    }
    /**
     * 修改协定存款终止表
     *
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "修改协定存款终止表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_TERM_APPLY)
    @PostMapping("/modifyTerm")
    @ResponseBody
    public AjaxJson modifyTerm(FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            choInnerAgreedDepositService.updateChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser(), false);
        }
        return success();
    }
    /**
     * 协定存款表详情页
     *
     * @return
     */
    @LogMessage(content = "查询协定存款表详情页", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewCho/{id}")
    public String viewCho(@PathVariable("id") String id, Model model) {
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getByUnion(id);
        List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(id);
        fcAgreedDepositVO.setDetailList(details);
        model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
        //查看日志的业务类型
        model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_AGREE);
        if (InnerFinanceConstant.BUS_TYPE_0.equals(fcAgreedDepositVO.getBusType())) {
            return "agreeddeposit/viewChoRegAgreedDeposit";
        } else if (InnerFinanceConstant.BUS_TYPE_1.equals(fcAgreedDepositVO.getBusType())) {
            return "agreeddeposit/viewChoPosAgreedDeposit";
        } else {
            return "agreeddeposit/viewChoTermAgreedDeposit";
        }
    }
    /**
     * 删除协定存款信息
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除协定存款登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    ////@RequiresPermissions(value = {ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_REG_APPLY,
//            ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_POS_APPLY,
//            ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_TERM_APPLY},
//            logical = Logical.OR)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        choInnerAgreedDepositService.delChoAgreedDeposit(id, this.getCurrentUser());
        return success();
    }
    /**
     * 批量协定存款信息
     *
     * @param ids
     * @return
     */
    ////@RequiresPermissions(value = {ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_REG_APPLY,
//            ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_POS_APPLY,
//            ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_TERM_APPLY},
//            logical = Logical.OR)
    @LogMessage(content = "批量删除费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            choInnerAgreedDepositService.delChoAgreedDeposit(id, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 批量提交费用报销表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量提交协定存款信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    ////@RequiresPermissions(value = {ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_REG_APPLY,
//            ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_POS_APPLY,
//            ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_TERM_APPLY},
//            logical = Logical.OR)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getByUnion(id);
            fcAgreedDepositVO.setStatus(InnerFinanceConstant.STATUE_WAIT_CHECK);
            choInnerAgreedDepositService.updateChoAgreedDeposit(fcAgreedDepositVO, this.getCurrentUser(), true);
        }
        return success();
    }
    /**
     * 跳转至审核列表
     *
     * @param busType 业务类型
     * @return
     */
    @GetMapping("/listApprove/{busType}")
    public String listApprove(@PathVariable String busType, String flowStatus, Model model) {
        model.addAttribute("busType", busType);
        model.addAttribute("flowStatus", flowStatus);
        return "agreeddeposit/list" + this.getForwardByBusType(busType) + "Approve";
    }
    /**
     * 根据业务类型返回不同的跳转页面
     *
     * @param busType
     * @return
     */
    public String getForwardByBusType(String busType) {
        /**业务类型 0-登记 1-到期处理 2-终止 */
        String forward = "RegChoAgreedDeposit";
        if (InnerFinanceConstant.BUS_TYPE_1.equals(busType)) {
            forward = "PosChoAgreedDeposit";
        } else if (InnerFinanceConstant.BUS_TYPE_2.equals(busType)) {
            forward = "TermChoAgreedDeposit";
        }
        return forward;
    }
    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcAgreedDepositVO vo = this.choInnerAgreedDepositService.getByUnion(id);
            List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(id);
            vo.setDetailList(details);
            model.addAttribute("fcAgreedDepositPage", vo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_AGREE);
        }
        return "agreeddeposit/approveChoAgreedDeposit";
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            FcAgreedDepositVO vo = this.choInnerAgreedDepositService.getByUnion(businessId);
            List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(businessId);
            if(!EmptyUtils.isEmpty(vo)){
                vo.setDetailList(details);
                model.addAttribute("fcAgreedDepositPage", vo);
                model.addAttribute("taskId", taskId);
                model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_AGREE);
            }
        }
        return "agreeddeposit/viewArrpoveChoAgreedDeposit";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return this.choInnerAgreedDepositService.dataTableApprove(dataTablesInput);
    }
    /**
     * 协定存款查询 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询协定存款查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String query(Model model) {
        return "agreeddeposit/listChoAgreedDepositQuery";
    }
    /**
     * 内部存款-协定存款查询列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_QUERY)
    @PostMapping("/datatablequery")
    @ResponseBody
    public DataTablesOutput dataTableQuery(@RequestBody DataTablesInput dataTablesInput) {
        ICurrentUser user = this.getCurrentUser();
        return choInnerAgreedDepositService.getDataTableQuery(dataTablesInput, user);
    }
    /**
     * 协定存款明细查询 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询协定存款明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryDtl/{id}")
    public String queryDtl(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        return "agreeddeposit/listChoAgreedDepositDtlQuery";
    }
    /**
     * 内部存款-协定存款明细查询列表
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/datatablequerydtl")
    @ResponseBody
    public DataTablesOutput getDataTableQueryDtl(@RequestBody DataTablesInput dataTablesInput) {
        return choInnerAgreedDepositService.getDataTableQueryDtl(dataTablesInput);
    }
    /**
     * 根据开户单位,单据类型查找正常状态非资金池内部账号
     *
     * @param corpId
     * @param busType
     * @return
     */
    @RequestMapping(params = "getBlnctrAccByCorpId")
    @ResponseBody
    public List<FcAccVO> getBlnctrAccByCorpId(String corpId, String busType) {
        List<FcAccVO> list = choInnerAgreedDepositService.getBlnctrAccByCorpId(this.getCurrentUser(), corpId, busType);
        return list;
    }
    /**
     * 根据账户id,单据类型查找协定信息
     *
     * @param accId
     * @return
     */
    @RequestMapping(params = "getChoInfo")
    @ResponseBody
    public AjaxJson getChoInfo(String accId, String busType) {
        AjaxJson j = new AjaxJson();
        FcAgreedDepositVO vo = choInnerAgreedDepositService.getAgreeInfo(accId, busType);
        j.setSuccess(true);
        j.setObj(vo);
        return j;
    }
    /**
     * 根据单位id获取日结日期
     *
     * @param corpId
     * @return
     */
    @RequestMapping(params = "getBeginDate")
    @ResponseBody
    public AjaxJson getBeginDate(String corpId) {
        /*获取单位直属网点*/
        CorpDTO netCorp = iCorpService.getNetByCorpId(corpId);
        String netId = "";
        if (netCorp == null) {
            /*如果没有直属网点就是本网点*/
            netId = corpId;
        } else {
            netId = netCorp.getId();
        }
        AjaxJson j = new AjaxJson();
        Date tallyDate = fcDayCloseService.getDayCloseFinalDate(netId);
        tallyDate = DateUtils.addDay(tallyDate, 1);
        String dateStr = DateUtils.formatDate(tallyDate);
        j.setSuccess(true);
        j.setObj(dateStr);
        return j;
    }
    /**
     * 数据校验
     *
     * @param vo
     * @return
     */
    private boolean dataCheck(FcAgreedDepositVO vo) {
        boolean flag = true;
        if(InnerFinanceConstant.BUS_TYPE_0.equals(vo.getOpType())){
            List<FcAgreedDepositVO> contractList = choInnerAgreedDepositService.getInfoByContractNo(vo.getContractNo());
            if (!EmptyUtils.isEmptyList(contractList)) {
                if (StringUtils.isBlank(vo.getId())) {
                    throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_CONTRACTNO_UNIQUE, vo.getContractNo());
                }else if (!vo.getId().equals(contractList.get(0).getId())) {
                    throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_CONTRACTNO_UNIQUE, vo.getContractNo());
                }
            }
        }
        List<Integer> status = new ArrayList<Integer>();
        status.add(AgreedConstant.STATUE_TEMP);
        status.add(AgreedConstant.STATUE_BACK);
        List<FcAgreedDepositVO> agreedList = choInnerAgreedDepositService.getAgreedInfo(vo.getAccId(), vo.getOpType(), status);
        if (!EmptyUtils.isEmptyList(agreedList)) {
            if (StringUtils.isBlank(vo.getId())) {
                throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_BLNCTRACC_USE, vo.getBlnctrAcc());
            } else if (!vo.getId().equals(agreedList.get(0).getId())) {
                throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_BLNCTRACC_USE, vo.getBlnctrAcc());
            }
        }
        return flag;
    }
}
