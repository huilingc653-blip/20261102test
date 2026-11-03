package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.constants.ExternalCorpConstants;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.ExternalCorp;
import com.bytter.system.entity.ExternalCorpAuditLog;
import com.bytter.system.entity.ExternalCorpBankAcc;
import com.bytter.system.entity.User;
import com.bytter.system.service.CorpService;
import com.bytter.system.service.ExternalCorpAuditLogService;
import com.bytter.system.service.ExternalCorpBankACCService;
import com.bytter.system.service.ExternalCorpQuickPayService;
import com.bytter.system.service.ExternalCorpService;
import com.bytter.system.service.UserService;
import com.bytter.system.api.enums.CorpMessageEnum;
import com.bytter.system.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/6/21.
 */
@Controller
@RequestMapping("/sys/externalCorp")
public class ExternalCorpController extends BaseController {
    @Autowired
    private CorpService corpService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExternalCorpService externalCorpService;
    @Autowired
    private ExternalCorpAuditLogService externalCorpAuditLogService;
    @Autowired
    private ExternalCorpBankACCService externalCorpBankACCService;
    @Autowired
    private ExternalCorpQuickPayService externalCorpQuickPayService;
    @GetMapping("/index")
    public void index(Model model) {
        model.addAttribute("corps", corpService.listAllTree());
    }
    @GetMapping("/list")
    public void list(String id, Model model) {
        model.addAttribute("corpId", id);
    }
    @PostMapping("/datatable.json")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return externalCorpService.getDataTables(dataTablesInput, getUserId());
    }
    /**
     * 按列表查询条件和当前用户权限分批导出全部客商资料。
     */
    @PostMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                // 列表查询会移除交付产品等条件，保留原始条件供下一批导出使用。
                Map<String, Object> formdata = new HashMap<>(dataTablesInput.getFormdata());
                DataTablesOutput dataTablesOutput = externalCorpService.getDataTables(dataTablesInput, getUserId());
                dataTablesInput.setFormdata(formdata);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "客商资料", 5000, ExternalCorpVO.class);
    }
    @GetMapping("/selectExternalCorpPage")
    public String selectExternalCorpPage(String corpId, String type, String flag, String externalAcc, Model model) {
        model.addAttribute("corpId", corpId);
        model.addAttribute("isFex", "0");
        model.addAttribute("flag", flag);
        model.addAttribute("externalAcc", externalAcc);
        if (EmptyUtils.isEmpty(type)) { //客商和单位同时显示
            return "sys/externalCorp/selectCorpTab";
        } else if ("showPage".equals(type) || "0".equals(type)) {            //客商选择
            return "sys/externalCorp/selectExternalCorpPage";
        } else if ("1".equals(type)) {            //单位选择
            return "sys/corp/selectCorpPage";
        } else if ("4".equals(type)) {        //包含没有账号的网点单位信息
            model.addAttribute("corpBank", "corpBank");
            return "sys/externalCorp/selectCorpTab";
        } else {//客商和单位同时显示
            return "sys/externalCorp/selectCorpTab";
        }
    }
    /**
     * 获取单位\客商 选择功能（支持多选、且不包含账户信息）
     *
     * @param paramsVO 传递参数
     * @param model
     * @return
     */
    @GetMapping("/selectExternalCorpTab")
    public String selectExternalCorpTab(ExternalCorpVO paramsVO, Model model) {
        model.addAttribute("paramsVO", paramsVO);
        if ("1".equals(paramsVO.getOpType())) {
            return "sys/externalCorp/selectExternalCorpTabToExternalCorp";
        } else if ("2".equals(paramsVO.getOpType())) {
            return "sys/externalCorp/selectExternalCorpTabToCorp";
        } else {
            return "sys/externalCorp/selectExternalCorpTab";
        }
    }
//
//	/**
//	 * 获取单位\客商 选择功能（支持多选、且不包含账户信息） 已选数据查看
//	 * @param paramsVO 传递参数
//	 * @param model
//	 * @return
//	 */
//	@GetMapping("/selectExternalCorpTabView")
//	public String selectExternalCorpTabView(ExternalCorpVO paramsVO, Model model) {
//		model.addAttribute("paramsVO", paramsVO);
//		return "sys/externalCorp/selectExternalCorpTabView";
//	}
    @PostMapping("/selectExternalCorp.json")
    @ResponseBody
    public DataTablesOutput selectExternalCorp(@RequestBody DataTablesInput dataTablesInput) {
        IPageResult<ExternalCorpBankAccDetailVO> pageResult = externalCorpService.listByExternalDetail(dataTablesInput);
        return pageResult.convert(DataTablesOutput.class);
    }
    @PostMapping("/bankAcc.json")
    @ResponseBody
    public Object bankAcc(String id) {
        return externalCorpService.listByExternalCorpId(id);
    }

    @PostMapping("/quickPay.json")
    @ResponseBody
    public Object quickPay(String id) {
        return externalCorpService.listQuickPayByExternalCorpId(id);
    }

    /**
     * 新增or编辑快捷支付信息跳转
     *
     * @return
     */
    @GetMapping("/addOrEditQuickPayInfo")
    public String addOrEditQuickPayInfo(String type, String isView, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("isView", isView);
        model.addAttribute("isFpsQuickPay", ExternalCorpConstants.isExternalCorpQuickPayFpsId(type));
        return "sys/externalCorp/addOrEditQuickPayInfo";
    }
    @GetMapping("/addOrUpdate")
    public void addOrUpdate(String id, String isCommon, Model model) {
        String corpId = "";
        if (StringUtils.isNotBlank(id)) {
            ExternalCorpVO externalCorpVO = this.externalCorpService.getByUnion(id);
            if (!EmptyUtils.isEmpty(externalCorpVO)) {
                List<String> corpIds = externalCorpService.getCorpIdsByExternalCorpId(externalCorpVO.getId());
                if (!EmptyUtils.isEmptyList(corpIds)) {
                    externalCorpVO.setCorpId(StringUtils.listTtoString(corpIds));
                }
            }
            corpId = externalCorpVO.getCorpId();
            //TODO: zhouliang
            model.addAttribute("externalCorp", externalCorpVO);
            model.addAttribute("isCommon", externalCorpVO.getIsCommon());
        } else {
            List<CorpVO> list = corpService.getAllCorpByValid();
            for (CorpVO corpVO : list) {
                if (EmptyUtils.isEmpty(corpId)) {
                    corpId = corpVO.getId();
                } else {
                    corpId += "," + corpVO.getId();
                }
            }
            model.addAttribute("isCommon", isCommon);
        }
        model.addAttribute("corpId", corpId);
    }
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(ExternalCorpVO externalCorpVO, HttpServletRequest request) {
        String corpId1 = request.getParameter("corpId1");
        String corpId2 = request.getParameter("corpId2");
        if ("0".equals(externalCorpVO.getIsCommon())) {
            externalCorpVO.setCorpId(corpId1);
        } else {
            externalCorpVO.setCorpId(corpId2);
        }
        if (StringUtils.isBlank(externalCorpVO.getCorpId()) && "0".equals(externalCorpVO.getIsCommon())) {
            throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_ID_NULL);
        }
        int num = 0;
        if (null != externalCorpVO.getExternalAcc()) {
            for (ExternalCorpBankAcc externalAccVO : externalCorpVO.getExternalAcc()) {
                if (StringUtils.isNoneBlank(externalAccVO.getIsDefaultAcc())) {
                    if (!externalAccVO.getIsDefaultAcc().equals("0")) {
                        num++;
                    }
                }
            }
        }
        if (num > 1) {
            throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_ISDEFAULTACC);
        }
        //审核客商资料时要判断最近提交客商资料的人，这里得存入这个字段
        if (externalCorpVO.getAuditStatus().equals(CommonConstant.STATUE_WAIT_CHECK)) {
            externalCorpVO.setCommitBy(getCurrentUser().getId());
        }
        externalCorpService.update(externalCorpVO, getCurrentUser());
        return success();
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ExternalCorpVO externalCorpVO, HttpServletRequest request) {
        String corpId1 = request.getParameter("corpId1");
        String corpId2 = request.getParameter("corpId2");
        if ("0".equals(externalCorpVO.getIsCommon())) {
            externalCorpVO.setCorpId(corpId1);
        } else {
            externalCorpVO.setCorpId(corpId2);
        }
        if ("0".equals(externalCorpVO.getIsCommon()) && StringUtils.isBlank(externalCorpVO.getCorpId())) {
            throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_ID_NULL);
        }
        int num = 0;
        if (null != externalCorpVO.getExternalAcc()) {
            for (ExternalCorpBankAcc externalAccVO : externalCorpVO.getExternalAcc()) {
                if (StringUtils.isNoneBlank(externalAccVO.getIsDefaultAcc())) {
                    if (!externalAccVO.getIsDefaultAcc().equals("0")) {
                        num++;
                    }
                }
            }
        }
        if (num > 1) {
            throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_ISDEFAULTACC);
        }
        //审核客商资料时要判断最近提交客商资料的人，这里得存入这个字段
        if (externalCorpVO.getAuditStatus().equals(CommonConstant.STATUE_WAIT_CHECK)) {
            externalCorpVO.setCommitBy(getCurrentUser().getId());
        }
        externalCorpService.save(externalCorpVO, getCurrentUser());
        return success();
    }
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("externalIds[]") List<String> idList) {
        Integer successNum = externalCorpService.batchSubmit(idList, getCurrentUser());
        AjaxJson success = success();
        success.setObj(successNum);
        return success;
    }
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(@RequestParam("externalIds[]") List<String> externalIds) {
        externalCorpService.delete(externalIds);
        return success();
    }
    @GetMapping("/deleteBankAcc")
    @ResponseBody
    public AjaxJson deleteBankAcc(String id) {
        externalCorpService.deleteBankAcc(id);
        return success();
    }
    /**
     * 客商资料详情页
     *
     * @param actionType 视图动作，null：有查看；audit:审核; auditView:审核查看（带审核记录）
     * @return String
     */
    @GetMapping("/view")
    public void view(String id, Model model, String actionType) {
        if (StringUtils.isNotBlank(id)) {
            ExternalCorpVO externalCorpVO = this.externalCorpService.getByUnion(id);
            model.addAttribute("externalCorp", externalCorpVO);
//            if (!EmptyUtils.isEmpty(externalCorpVO)) {
//                List<String> corpIds = externalCorpService.getCorpIdsByExternalCorpId(externalCorpVO.getId());
//                if (!EmptyUtils.isEmptyList(corpIds)) {
//                    externalCorpVO.setCorpId(StringUtils.listTtoString(corpIds));
//                }
//            }
        }
        if (StringUtils.isNotBlank(actionType)) {
            model.addAttribute("actionType", actionType);
        }
    }
    /**
     * 导入保存
     * uodate by shenc 2018/5/30
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<ExternalCorpInfoImportVO> list) {
        if (EmptyUtils.isEmptyList(list)) {
            AjaxJson ajaxJson = new AjaxJson();
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        this.externalCorpService.saveImport(list);
        return success();
    }
    /**
     * 下拉框客商组件查询
     *
     * @param corpId
     * @return
     */
    @GetMapping("/listByInputExternalCorp")
    @ResponseBody
    public Object listByInputExternalCorp(String corpId, String seach) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(seach)) {
            seach = URLDecoder.decode(seach, "UTF-8");
        }
        return this.externalCorpService.listByInputExternalCorp(corpId, seach);
    }
    /*********** 客商资料审核 *************/
    @GetMapping("/audit/list")
    public void auditList(Model model) {
        model.addAttribute("corps", corpService.listAllTree());
    }
    @PostMapping("/audit/datatable.json")
    @ResponseBody
    public DataTablesOutput auditDataTable(@RequestBody DataTablesInput dataTablesInput) {
        User currentUser = userService.getByLoginName(getCurrentUser().getLoginName());
        //管理员除外，维护与审核的不能是同一个人；客商资料维护中提交的，只能由另一个人审核，自己不能审核；
        if (currentUser.getIsAdmin() != 1) {
            dataTablesInput.getFormdata().put("commitBy", currentUser.getId());
        }
        return externalCorpService.getAllDatas(dataTablesInput, currentUser.getId());
    }
    @PostMapping("/audit")
    @ResponseBody
    public AjaxJson audit(String ids, Integer target, String auditMessage) {
        if (StringUtils.isBlank(ids)) {
            throw new BusinessException("id不能为空");
        }
        if (StringUtils.endsWith(ids, ",")) {
            ids = ids.substring(0, ids.length() - 1);
        }
        String[] idArray = ids.split(",");
        if (StringUtils.isBlank(auditMessage)) {
            auditMessage = null;
        }
        externalCorpService.saveAudit(idArray, target, auditMessage, getCurrentUser());
        return success();
    }
    @GetMapping("/auditLog")
    @ResponseBody
    public AjaxJson auditLog(String id) {
        List<ExternalCorpAuditLog> list = externalCorpAuditLogService.getLogListByCorpId(id);
        List<ExternalCorpAuditLogVO> logVOList = new ArrayList<>();
        for (ExternalCorpAuditLog externalCorpAuditLog : list) {
            ExternalCorpAuditLogVO externalCorpAuditLogVO = new ExternalCorpAuditLogVO();
            BeanUtils.copyNotNullProperties(externalCorpAuditLog, externalCorpAuditLogVO);
            logVOList.add(externalCorpAuditLogVO);
        }
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setObj(logVOList);
        return ajaxJson;
    }
    /***********客商资料查询*************/
    @GetMapping("/query/list")
    public void queryList(Model model) {
        model.addAttribute("corps", corpService.listAllTree());
    }
    /**
     * 国外客商主键
     *
     * @param corpId
     * @param type
     * @param model
     * @return
     */
    @GetMapping("/selectExternalCorpFexPage")
    public String selectExternalCorpFexPage(String corpId, String type, String externalAcc, Model model) {
        model.addAttribute("corpId", corpId);
        model.addAttribute("isFex", "1");
        model.addAttribute("externalAcc", externalAcc);
        if (EmptyUtils.isEmpty(type)) { //客商和单位同时显示
            return "sys/externalCorp/selectCorpTab";
        } else if ("showPage".equals(type) || "0".equals(type)) {           //客商选择
            return "sys/externalCorp/selectExternalCorpFexPage";
        } else if ("1".equals(type)) {            //单位选择
            return "sys/corp/selectCorpPage";
        } else {//客商和单位同时显示
            return "sys/externalCorp/selectCorpTab";
        }
    }
    /**
     * 国外客商列表查询
     */
    @PostMapping("/selectExternalCorpFex.json")
    @ResponseBody
    public DataTablesOutput selectExternalCorpFex(@RequestBody DataTablesInput dataTablesInput) {
        IPageResult<ExternalCorpBankAccDetailVO> pageResult = externalCorpService.listByForeignExternalDetail(dataTablesInput);
        return pageResult.convert(DataTablesOutput.class);
    }
    /**
     * 下拉框境外客商组件查询
     *
     * @param corpId
     * @return
     */
    @GetMapping("/listByInputExternalCorpFex")
    @ResponseBody
    public Object listByInputExternalCFex(String corpId, String seach) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(seach)) {
            seach = URLDecoder.decode(seach, "UTF-8");
        }
        return this.externalCorpService.listByInputExternalCorpFex(corpId, seach);
    }
    /**
     * 账号选择
     *
     * @return
     */
    @GetMapping("/selectExternalCorp")
    public String selectExternalCorp(ExternalCorpVO externalCorp, Model model) {
        model.addAttribute("params", externalCorp);
        return "sys/externalCorp/selectExternalCorp";
    }
    /**
     * 查询已选择账号
     *
     * @return
     */
    @GetMapping("/selectExternalCorpView")
    public String selectExternalCorpView(ExternalCorpVO externalCorp, Model model) {
        List<ExternalCorpVO> list = externalCorpService.selectExternalCorpView(externalCorp);
        model.addAttribute("list", list);
        return "sys/externalCorp/selectExternalCorpView";
    }
    @PostMapping("/dataTableForSelect")
    @ResponseBody
    public DataTablesOutput dataTableForSelect(@RequestBody DataTablesInput dataTablesInput) {
        return externalCorpService.dataTableForSelect(dataTablesInput);
    }

    /**
     * 境内外全部客商账号组件
     *
     * @return
     */
    @GetMapping("/selectAllExternalBankAccTab")
    public String selectAllExternalBankAccTab(Model model, String isCustom) {
        model.addAttribute("isCustom", isCustom);
        return "sys/externalCorp/selectallExternalBankAccTab";
    }

    /**
     * 境内账户选择
     *
     * @return
     */
    @GetMapping("/selectInnerExternalBankAcc")
    public String selectInnerExternalBankAcc(Model model, String isCustom) {
        model.addAttribute("isCustom", isCustom);
        return "sys/externalCorp/selectInnerExternalBankAcc";
    }

    @PostMapping("/innerExternalBankAccDate")
    @ResponseBody
    public DataTablesOutput innerExternalBankAccDate(@RequestBody DataTablesInput dataTablesInput) {
        return externalCorpBankACCService.selectAllExternalDate(dataTablesInput);
    }

    /**
     * 境外账户选择
     *
     * @return
     */
    @GetMapping("/selectOuterExternalBankAcc")
    public String selectOuterExternalBankAcc(Model model, String isCustom) {
        model.addAttribute("isCustom", isCustom);
        return "sys/externalCorp/selectOuterExternalBankAcc";
    }

    @PostMapping("/outerExternalBankAccDate")
    @ResponseBody
    public DataTablesOutput outerExternalBankAccDate(@RequestBody DataTablesInput dataTablesInput) {
        return externalCorpBankACCService.selectAllExternalDate(dataTablesInput);
    }

    /**
     * 网点/单位账户选择
     *
     * @return
     */
    @GetMapping("/selectAllCorpBankAcc")
    public String selectAllCorpBankAcc(Model model, String isCustom) {
        model.addAttribute("isCustom", isCustom);
        return "sys/externalCorp/selectAllCorpBankAcc";
    }

    /**
     * FPS选择
     *
     * @return
     */
    @GetMapping("/selectFpsExternalBankAcc")
    public String selectFpsExternalBankAcc(Model model, String isCustom) {
        model.addAttribute("isCustom", isCustom);
        return "sys/externalCorp/selectFpsExternalBankAcc";
    }

    @PostMapping("/fpsExternalBankAccDate")
    @ResponseBody
    public DataTablesOutput fpsExternalBankAccDate(@RequestBody DataTablesInput dataTablesInput) {
        return externalCorpQuickPayService.fpsExternalBankAccDate(dataTablesInput);
    }

    /**
     * PayPal选择
     *
     * @return
     */
    @GetMapping("/selectPayPalExternalBankAcc")
    public String selectPayPalExternalBankAcc(Model model, String isCustom) {
        model.addAttribute("isCustom", isCustom);
        return "sys/externalCorp/selectPayPalExternalBankAcc";
    }

    @PostMapping("/payPalExternalBankAccDate")
    @ResponseBody
    public DataTablesOutput payPalExternalBankAccDate(@RequestBody DataTablesInput dataTablesInput) {
        return externalCorpQuickPayService.payPalExternalBankAccDate(dataTablesInput);
    }

    /**
     * 资金收付收款账户类型对应数据
     */
    @GetMapping("/fexPayPayModelList")
    @ResponseBody
    public List<ExternalCorpQuickPayVO> fexPayPayModelList(String externalId, String proId, String field) {
        if (EmptyUtils.isEmpty(externalId)) {
            return null;
        }
        return externalCorpQuickPayService.fexPayPayModelList(externalId, proId, field);
    }

    /**
     * 资金收付收款账户类型对应账号数据
     */
    @GetMapping("/fexPayPayModelAccList")
    @ResponseBody
    public List<ExternalCorpBankAccVO> fexPayPayModelAccList(String externalId) {
        if (EmptyUtils.isEmpty(externalId)) {
            return null;
        }
        return externalCorpBankACCService.listByExternalCorpId(externalId);
    }

    /**
     * 根据客商id获取快捷支付信息
     */
    @GetMapping("/getQuickPayInfo")
    @ResponseBody
    public Object getQuickPayInfo(String externalCorpId) {
        return externalCorpService.getQuickPayInfo(externalCorpId);
    }



    /**
     * 国内外全部客商组件
     *
     * @return
     */
    @GetMapping("/selectAllExternalCorpTab")
    public String selectAllExternalCorpTab(String type,String proId,Model model) {
        String url = "sys/externalCorp/selectAllExternalCorpTab";
        if ("external".equals(type)) {
            url="sys/externalCorp/selectAllExternalTab";
        }
        if(!EmptyUtils.isEmpty(proId)){
            if(ExternalCorpConstants.isExternalCorpQuickPayProductId(proId)){
                model.addAttribute("isNative","0");
            }
        }
        return url;
    }


    /**
     * 客商选择
     *
     * @return
     */
    @GetMapping("/selectAllExternalTab")
    public String selectAllExternalTab() {
        return "sys/externalCorp/selectAllExternalTab";
    }

    @PostMapping("/selectAllExternalDate")
    @ResponseBody
    public DataTablesOutput selectAllExternalDate(@RequestBody DataTablesInput dataTablesInput) {
        return externalCorpService.selectAllExternalDate(dataTablesInput);
    }

    /**
     * 客商选择
     *
     * @return
     */
    @GetMapping("/selectAllCorpTab")
    public String selectAllCorpTab() {
        return "sys/externalCorp/selectAllCorpTab";
    }

    /**
     * 客商交易对手账户组件
     *
     * @return
     */
    @GetMapping("/selectallExternalBankAccTab")
    public String selectallExternalBankAccTab(String type,Model model,String oppId,String curId,String isCustom) {
        String url = "sys/externalCorp/selectallExternalBankAccTab";
        if(!EmptyUtils.isEmpty(oppId)) {
            Corp corp = corpService.get(oppId, true);
            if (!EmptyUtils.isEmpty(corp)) {
                type = "corpBankAcc";
                model.addAttribute("corp", corp);
            }
            ExternalCorp externalCorp = externalCorpService.get(oppId);
            if (!EmptyUtils.isEmpty(externalCorp)) {
                if (EmptyUtils.isEmpty(type) || (!type.equals("fps") && !type.equals("payPal"))) {
                    if (externalCorp.getIsNative() == 0) {
                        type = "outerBankAcc";
                    } else {
                        type = "innerBankAcc";
                    }
                }
                model.addAttribute("externalCorp", externalCorp);
            }
        }
        //币种
        if(!EmptyUtils.isEmpty(curId)){
            model.addAttribute("curId",curId);
        }
        if(!EmptyUtils.isEmpty(type)){
            String[] types=type.split(",");
            if(types.length==1){
                if("innerBankAcc".equals(types[0])){
                    url="sys/externalCorp/selectInnerExternalBankAcc";
                }else if("outerBankAcc".equals(types[0])){
                    url="sys/externalCorp/selectOuterExternalBankAcc";
                }else if("corpBankAcc".equals(types[0])){
                    url="sys/externalCorp/selectAllCorpBankAcc";
                }else if("fps".equals(types[0])){
                    url="sys/externalCorp/selectFpsExternalBankAcc";
                }else if("payPal".equals(types[0])){
                    url="sys/externalCorp/selectPayPalExternalBankAcc";
                }
            }else {
                //第一个页签
                model.addAttribute("first",types[0]);
                List<String> typeList= Arrays.asList(types);
                if(typeList.contains("innerBankAcc")){
                    model.addAttribute("innerBankAcc",true);
                }
                if(typeList.contains("outerBankAcc")){
                    model.addAttribute("outerBankAcc",true);
                }
                if(typeList.contains("corpBankAcc")){
                    model.addAttribute("corpBankAcc",true);
                }
                if(typeList.contains("fps")){
                    model.addAttribute("fps",true);
                }
                if(typeList.contains("payPal")){
                    model.addAttribute("payPal",true);
                }
            }
        }
        if(!EmptyUtils.isEmpty(isCustom)){
            model.addAttribute("isCustom",isCustom);
        }
        return url;
    }
}
