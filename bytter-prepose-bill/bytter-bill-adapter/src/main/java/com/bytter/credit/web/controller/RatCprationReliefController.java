package com.bytter.credit.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationRelief;
import com.bytter.credit.domain.service.*;
import com.bytter.credit.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.bytter.system.api.dto.CorpDTO;
import java.math.BigDecimal;
import java.util.*;
/**
 * 
 * @author fusb
 * @version 1.0
 * @Title RatCprationReliefController
 * @Description 内部授信调剂
 * @date 2018年11月23日
 */
@Controller
@RequestMapping("/cms/rat/ratcprationrelief")
public class RatCprationReliefController extends BaseController {
    @Autowired
    private RatBkrationDisService ratDisService;
    @Autowired
    private RatBkrationDetailService ratDtlService;
    @Autowired
    private RatCprationReliefService ratCpRelieService;
    @Autowired
    private RatCprationService ratCpService;
    /**
     * 内部授信额度调剂表列表 页面跳转 取审批通过的内部授信分配数据
     *
     * @return
     */
    @LogMessage(content = "查询内部授信额度调剂表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public String list(RatBkrationVO rationVO, Model model, Integer start) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        input.setStart((start - 1) * input.getLength());
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
            model.addAttribute("contractNo", contractNo);
        }
        Date ratDate = rationVO.getBeginDate();
        if (null != rationVO.getBeginDate()) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(ratDate), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(ratDate), Date.class);// 小于等于授信结束日期
            model.addAttribute("ratDate", DateUtils.formatDate(ratDate));
        }
        DataTablesOutput output = ratCpRelieService.queryRationDisCheckData(input,
                RationConstant.CMS_CPRATION_RELIEF_QUERY, getUserId());
        Map<String, Object> result = new HashMap<String, Object>();
        long resultCount = output.getRecordsTotal();
        int pageSize = input.getLength();
        int pageCount = 0;
        if (resultCount % pageSize == 0) {
            // 整除则直接取整相除
            pageCount = (int) (resultCount / pageSize);
        } else {
            // 否则取整相除后加一
            pageCount = (int) ((resultCount / pageSize) + 1);
        }
        result.put("rations", output.getData());
        result.put("total", pageCount);
        result.put("curr", start);
        model.addAttribute("model", result);
        return "cms/rat/cpration/listRatCprationRelief";
    }
    /**
     * 授信额度调剂表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String ratCode) {
        dataTablesInput.addFilter(MatchTypeEnum.EQ, "ratCode", ratCode);
        return ratCpRelieService.queryRatBkRationReliefByRatCode(dataTablesInput,
                RationConstant.CMS_CPRATION_RELIEF_QUERY, getUserId());
    }
    /**
     * 内部授信调剂信息列表查询
     *
     * @return
     */
    @LogMessage(content = "内部授信调剂信息列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addList")
    public String addList(RatBkrationPageVO rationVO, Model model, Integer start) {
        rationVO = ratCpService.getRatBkrationInfo(rationVO.getRatCode());
        model.addAttribute("addVO", rationVO);
        return "cms/rat/cpration/listRatCprationReliefed";
    }
    /**
     * 内部授信调剂单位
     *
     * @return
     */
    @GetMapping("/getRefCorpDTOs")
    @ResponseBody
    public List<CorpDTO> getRefCorpDTOs(String ratCode, String corpId, String flag) {
        return ratCpRelieService.queryRatReliefCorp(flag, ratCode, corpId);
    }
    /**
     * 获取授信分配单位已分配的授信品种类型
     *
     */
    @PostMapping("/getReliefItemFlag")
    @ResponseBody
    public List<RatItemDisVO> getReliefItemFlag(String ratCode, String corpId) {
        return ratCpRelieService.getCorpItemFlags(ratCode, corpId);
    }
    /**
     * 获取授信分配单位已分配的授信品种
     *
     */
    @PostMapping("/getReliefItem")
    @ResponseBody
    public List<RatItemDisVO> getReliefItem(String ratCode, String corpId, String itemFlag) {
        return ratCpRelieService.getItemCustomDTOs(ratCode, corpId, itemFlag);
    }
    /**
     * 根据授信品种获取授信品种额度
     *
     */
    @PostMapping("/getItemAssure")
    @ResponseBody
    public RatItemDisVO getItemAssure(String ratCode, String itemCode) {
        return ratDtlService.getItemAssure(ratCode, itemCode);
    }
    /**
     * 获取授信分配单位已分配授信品种的授信额度
     *
     */
    @PostMapping("/getDisItemMoney")
    @ResponseBody
    /*
     * public RatBkrationCpAddDtlDTO getDisItemMoney(String ratCode, String corpId, String itemFlag, String itemCode) {
     * //return ratDisService.getRationDisItemMoney(ratCode, corpId, itemFlag, itemCode); return
     * ratCpRelieService.getDisItemMoney(ratCode, corpId, itemFlag, itemCode); }
     */
    public BigDecimal getDisItemMoney(String ratCode, String corpId, String itemFlag, String itemCode) {
        // return ratDisService.getRationDisItemMoney(ratCode, corpId, itemFlag, itemCode);
        BigDecimal rd = ratCpRelieService.getDisItemMoney(ratCode, corpId, itemFlag, itemCode);
        return rd;
    }
    /**
     * 授信额度调剂表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(String ratCode, String corpId, String flag, Model model) {
        if (StringUtils.isNotEmpty(ratCode)) {
            RatBkrationReliefPageVO refVO = ratCpRelieService.initRationReliefPage(ratCode);
            model.addAttribute("ratBkrationReliefPage", refVO);
            model.addAttribute("flag", ratDisService.queryRationDisWay(ratCode));// 所有分配方式
        }
        return "cms/rat/cpration/addRatCprationRelief";
    }
    /**
     * 授信额度调剂表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(RatBkrationRelief relief, Model model) {
        if (StringUtils.isNotEmpty(relief.getId())) {
            Map<String, Object> result = ratCpRelieService.editRationReliefPage(relief.getId());
            model.addAttribute("result", result);
        }
        return "cms/rat/cpration/updateRatCprationRelief";
    }
    /**
     * 授信额度调剂表详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信额度调剂表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            Map<String, Object> result = ratCpRelieService.editRationReliefPage(id);
            model.addAttribute("result", result);
        }
        model.addAttribute("viewFlag", "1");
        return "cms/rat/cpration/viewRatCprationRelief";
    }
    private void setRatBkrationRelief(RatBkrationReliefPageVO refVO) {
        List<RatBkrationReliefDtlPageVO> dtls = new ArrayList<RatBkrationReliefDtlPageVO>();
        List<RatBkrationReliefDtlPageVO> reliefDtls = refVO.getReliefDtls();
        for (RatBkrationReliefDtlPageVO dtlPageVO : reliefDtls) {
            if (StringUtils.isEmpty(dtlPageVO.getReliefItemCode())
                    && StringUtils.isEmpty(dtlPageVO.getByReliefItemCode())) {
                continue;
            }
            dtls.add(dtlPageVO);
        }
        if (null == refVO.getReliefOpenMoney()) {
            refVO.setReliefOpenMoney(new BigDecimal(0));
        }
        refVO.setReliefDtls(dtls);
    }
    /**
     * 暂存授信额度调剂表
     * 
     * @param ratBkrationRelief
     * @return
     */
    @LogMessage(content = "新增内部授信额度调剂", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_TEMP);
        setRatBkrationRelief(refVO);
        ratCpRelieService.saveRationRelief(refVO, this.getCurrentUser());
        return success();
    }
    /**
     * 提交授信额度调剂表
     * 
     * @param ratBkrationRelief
     * @return
     */
    @LogMessage(content = "提交内部授信额度调剂", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        setRatBkrationRelief(refVO);
        ratCpRelieService.saveRationRelief(refVO, this.getCurrentUser());
        return success();
    }
    /**
     * 暂存授信额度调剂表
     * 
     * @param ratBkrationRelief
     * @return
     */
    @LogMessage(content = "增加内部授信额度调剂", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    @ResponseBody
    public AjaxJson editSave(RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_TEMP);
        setRatBkrationRelief(refVO);
        ratCpRelieService.updateRationRelief(refVO, this.getCurrentUser());
        return success();
    }
    /**
     * 提交授信额度调剂表
     * 
     * @param ratBkrationRelief
     * @return
     */
    @LogMessage(content = "提交内部授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    @ResponseBody
    public AjaxJson editSubmit(RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        setRatBkrationRelief(refVO);
        ratCpRelieService.updateRationRelief(refVO, this.getCurrentUser());
        return success();
    }
    /**
     * 修改授信额度调剂表
     * 
     * @param ratBkrationRelief
     * @return
     */
    @LogMessage(content = "修改内部授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RatBkrationRelief ratBkrationRelief) {
        RatBkrationRelief t = ratCpRelieService.get(ratBkrationRelief.getId());
        BeanUtils.copyNotNullProperties(ratBkrationRelief, t);
        ratCpRelieService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除授信额度调剂表
     * 
     * @param id
     * @return
     */
    @LogMessage(content = "删除内部授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ratCpRelieService.updateRationRefStatus(id);
        return success();
    }
    /**
     * 批量删除授信额度调剂表
     * 
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除内部授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            ratCpRelieService.updateRationRefStatus(id);
        }
        return success();
    }
    /**
     * 授信调剂审批 - 列表页面跳转
     */
    @LogMessage(content = "查询内部授信调剂审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/rat/cpration/listRatCprationReliefApprove";
    }
    /**
     * 授信调剂审批 - 数据
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput input) {
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        return ratCpRelieService.listApproveOutputData(input);
    }
    /**
     * 授信调剂审批 - 详情页面
     */
    @GetMapping("/viewCprationApprove")
    public String viewBkrationApprove(String businessId, String taskId, Model model) {
        Map<String, Object> result = ratCpRelieService.editRationReliefPage(businessId);
        model.addAttribute("result", result);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        String page = "viewRatCprationRelief";
        model.addAttribute("viewPage", page);
        return "cms/rat/cpration/viewRatCprationReliefApprove";
    }
    /**
     * 授信调剂手机审批 - 详情页面
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        Map<String, Object> result = ratCpRelieService.editRationReliefPage(businessId);
        model.addAttribute("result", result);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/rat/cpration/mobileRatCprationRelief";
    }
}
