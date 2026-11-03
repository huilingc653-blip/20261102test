package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolDiscountService;
import com.bytter.bill.domain.billPool.vo.AbPoolDiscountVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * 　 * @Title: 贴现申请Controller
 * 　 * @author zhenghj
 *
 * @Description:贴现申请控制器类 　 * @date 2020年3月10日
 * 　 * @version 1.0
 */
@Controller
@RequestMapping("/billPool/discount")
public class AbPoolDiscountController extends BaseController {
    @Autowired
    private AbPoolDiscountService abPoolDiscountService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    /**
     * 跳转到贴现申请页面
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPoolDiscount";
    }
    /**
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询贴现申请TAB列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolDiscountService.getDataTablesUnion(input);
    }
    /**
     * 跳转贴现申请详细界面
     *
     * @param abPoolDiscountVO
     * @param model
     * @return String
     */
    @GetMapping("/batchDiscount")
    public String batchDiscount(AbPoolDiscountVO abPoolDiscountVO, Model model) {
        model.addAttribute("abPoolDiscountVO", abPoolDiscountVO);
        return "billPool/addAbPoolDiscount";
    }
    /**
     * 保存贴现申请信息
     *
     * @param abPoolDiscountVO
     * @return
     */
    @LogMessage(content = "保存贴现申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(AbPoolDiscountVO abPoolDiscountVO, HttpServletRequest request) {
        abPoolDiscountService.saveAbPoolDiscountInfo(abPoolDiscountVO, this.getCurrentUser());
        return success();
    }
    @PostMapping("/operation")
    @ResponseBody
    public AjaxJson operation(@RequestParam("pdId") String pdId, @RequestParam("opt") String opt) {
        abPoolDiscountService.operation(pdId, opt, this.getCurrentUser());
        return success();
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "billPool/approve/listAbPoolDiscountApprove";
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
        return abPoolDiscountService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId 审批流水ID
     * @param taskId     流程ID
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolDiscountVO abPoolDiscountVO = abPoolDiscountService.getAbPoolDiscountVOByPdId(businessId);
            model.addAttribute("abPoolDiscountVO", abPoolDiscountVO);
            if (StringUtils.isNotEmpty(abPoolDiscountVO.getId())) {
                AbPoolDiscountVO billInfo = abPoolDiscountService.getAbBillDiscountApplyVOByVvid(abPoolDiscountVO.getVvId(), abPoolDiscountVO.getId());
                model.addAttribute("billInfo", billInfo);
            }
        }
        model.addAttribute("isApprove", true);
        model.addAttribute("taskId", taskId);
        return "billPool/approve/approveDiscountApply";
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
            AbPoolDiscountVO abPoolDiscountVO = abPoolDiscountService.getAbPoolDiscountVOByPdId(businessId);
            model.addAttribute("abPoolDiscountVO", abPoolDiscountVO);
            if (StringUtils.isNotEmpty(abPoolDiscountVO.getId())) {
                AbPoolDiscountVO billInfo = abPoolDiscountService.getAbBillDiscountApplyVOByVvid(abPoolDiscountVO.getVvId(), abPoolDiscountVO.getId());
                model.addAttribute("billInfo", billInfo);
            }
        }
        model.addAttribute("isApprove", true);
        return  "billPool/mobile/approveDiscountApply";
    }
    /**
     * 贴现申请详细信息页面跳转
     *
     * @return
     */
    @GetMapping("/view")
    public String view(AbPoolDiscountVO abPoolDiscountVO, Model model) {
        if (StringUtils.isNotEmpty(abPoolDiscountVO.getId())) {
            //票据池信息
            AbPoolDiscountVO discountVO = abPoolDiscountService.getAbPoolDiscountVOByPdId(abPoolDiscountVO.getId());
            model.addAttribute("abPoolDiscountVO", discountVO);
            //操作日志
            model.addAttribute("abLogList", abLogService.listAbLog(null, discountVO.getLastId() != null ? discountVO.getLastId() : discountVO.getId()));
            if (StringUtils.isNotEmpty(discountVO.getVvId())) {
                //票据明细
                AbPoolDiscountVO billInfo = abPoolDiscountService.getAbBillDiscountApplyVOByVvid(discountVO.getVvId(), discountVO.getId());
                model.addAttribute("billInfo", billInfo);
                //业务流转信息
                if (StringUtils.isNotEmpty(billInfo.getAccId())) {
                    AbPoolDtl abPoolDtl = abPoolDiscountService.getAbPoolDtlByLastPdId(abPoolDiscountVO.getId());
                    List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(billInfo.getAccId(),abPoolDtl!=null?abPoolDtl.getId():null);
                    model.addAttribute("accrecordsList", abAccrecordsList);
                }
            }
        }
        model.addAttribute("isApprove", false);
        return "billPool/viewAbPoolDiscountApply";
    }
}
