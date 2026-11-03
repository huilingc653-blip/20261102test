package com.bytter.credit.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.service.RatCprationService;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatCprationController
 * @Description:
 * @date 2018年11月13日 下午3:52:09
 */
@Controller
@RequestMapping("/cms/rat/ratcpration")
public class RatCprationController extends BaseController {
    @Autowired
    private RatCprationService ratCprationService;
    /**
     * 内部授信数据列表
     * 
     * @param contractNo
     *            合同号 ratDate 授信时间
     * @return
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/list")
    public String datalist(Model model, String contractNo, Date ratDate, Integer start) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        input.setStart((start - 1) * input.getLength());
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
            model.addAttribute("contractNo", contractNo);
        }
        if (null != ratDate) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(ratDate), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(ratDate), Date.class);// 小于等于授信结束日期
            model.addAttribute("ratDate", DateUtils.formatDate(ratDate));
        }
        DataTablesOutput output = ratCprationService.queryRatCprationData(input, RationConstant.CMS_CPRATION_EDIT_QUERY, getUserId());
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
        return "cms/rat/cpration/listRatCpration";
    }
    /**
     * 新增内部授信
     *
     * @author luxy
     */
    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("relateCon", ratCprationService.getRelateCon());
        model.addAttribute("conType", ratCprationService.getRatConType());
        return "cms/rat/cpration/addRatCpration";
    }
    /**
     * 检查合同号是否重复
     * 
     * @author luxy
     * @param contractNo-合同号
     */
    @PostMapping("/checkContractNo")
    @ResponseBody
    public boolean checkContractNo(String contractNo) {
        return ratCprationService.checkContractNo(contractNo);
    }
    /**
     * 保存内部授信录入信息
     * 
     * @author luxy
     * @param saveType
     *            保存标志：0-暂存 1-提交 updateType
     */
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(RatBkRationVm cpRationDTO, String saveType) {
        setRatCprationDtls(cpRationDTO);
        ratCprationService.tempOrSubmit(cpRationDTO, saveType, getCurrentUser());
        return success();
    }
    /**
     * 编辑内部授信信息
     * 
     * @param
     * @return
     */
    @RequestMapping("/update")
    public String update(Model model, String id) {
        RatBkRationVm ratCpration = ratCprationService.getCpRationByRatCode(id);
        model.addAttribute("ratCpration", ratCpration);
        model.addAttribute("relateCon", ratCprationService.getRelateCon());
        model.addAttribute("conType", ratCprationService.getRatConType());
        model.addAttribute("conId", ratCpration.getConId());
        return "cms/rat/cpration/editRatCpration";
    }
    /**
     * 删除内部授信信息
     * 
     * @param
     * @return
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkration ratBkration = ratCprationService.get(id);
        ratBkration.setStatus(RationConstant.STATUS_DELETE);
        ratCprationService.updateEntity(ratBkration);
        return success();
    }
    /**
     * 查看内部授信信息
     * 
     * @param
     * @return
     */
    @RequestMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        RatBkrationPageVO ratCpration = ratCprationService.queryCprationById(id);
        model.addAttribute("ratCprationPage", ratCpration);
        model.addAttribute("viewFlag", "1");
        model.addAttribute("conId", ratCpration.getConId());
        return "cms/rat/cpration/viewRatCpration";
    }
    private void setRatCprationDtls(RatBkRationVm cpRationDTO) {
        List<RatBkRationDtlVm> dtls = new ArrayList<RatBkRationDtlVm>();
        List<RatBkRationDtlVm> rationDtls = cpRationDTO.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<RatBkRationDtlVm>();
        }
        for (RatBkRationDtlVm dtlDTO : rationDtls) {
            if (null != dtlDTO && !StringUtils.isEmpty(dtlDTO.getItemFlag())) {
                dtls.add(dtlDTO);
            }
            if (null == dtlDTO.getMoney()) {
                dtlDTO.setMoney(BigDecimal.ZERO);
            }
        }
        // 单品种排序
        Collections.sort(dtls, new Comparator<RatBkRationDtlVm>() {
            public int compare(RatBkRationDtlVm o1, RatBkRationDtlVm o2) {
                if (o1.getItemFlag().equals(o2.getItemFlag())) {
                    return o1.getItemOrder().compareTo(o2.getItemOrder());
                }
                return o1.getItemFlag().compareTo(o2.getItemFlag());
            }
        });
        // 重设itemorder
        Integer index = Integer.valueOf(1);
        for (RatBkRationDtlVm dtlDTO : dtls) {
            dtlDTO.setItemOrder(index);
            index++;
        }
        cpRationDTO.setRationDtls(dtls);
    }
    /**
     * 内部授信录入审批列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/CpApproveDataTable")
    @ResponseBody
    public DataTablesOutput CpApproveDataTable(@RequestBody DataTablesInput input) {
        return ratCprationService.getApproveDataTable(input);
    }
    /**
     * 审批列表页面
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/rat/cpration/listRatCprationApprove";
    }
    /**
     * 审批详情页面
     * 
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        RatBkrationPageVO ratCpration = ratCprationService.queryCprationById(id);
        model.addAttribute("ratCprationPage", ratCpration);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        model.addAttribute("relateCon", ratCprationService.getRelateCon());
        model.addAttribute("conType", ratCprationService.getRatConType());
        model.addAttribute("conId", ratCpration.getConId());
        return "cms/rat/cpration/viewRatCprationApprove";
    }
    /**
     * 手机审批详情页面
     *
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        RatBkrationPageVO ratCpration = ratCprationService.queryCprationById(businessId);
        model.addAttribute("ratCprationPage", ratCpration);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/rat/cpration/mobileRatCpration";
    }
}
