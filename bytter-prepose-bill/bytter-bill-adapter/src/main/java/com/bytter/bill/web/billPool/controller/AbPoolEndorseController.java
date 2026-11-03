package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolEndorseService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolEndorseVO;
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
 * 
　 * @Title: 背书申请控制器类
　 * @author zhenghj
  * @Description:背书申请控制器类
　 * @date 2020年3月6日
　 * @version 1.0
 */
@Controller
@RequestMapping("/billPool/endorse")
public class AbPoolEndorseController extends BaseController{
	
	 @Autowired
	 private AbPoolEndorseService abPoolEndorseService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
	 /**
     * 跳转到背书转让页面
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPoolEndorse";
    }
    /**
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询背书转让TAB列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolEndorseService.getDataTablesUnion(input);
    }
    /**
     * 跳转背书转让详细界面
     *
     * @param abPoolEndorseVO
     * @param model
     * @return String
     */
    @GetMapping("/batchEndorse")
    public String batchEndorse(AbPoolEndorseVO abPoolEndorseVO, Model model) {
        model.addAttribute("abPoolEndorseVO",abPoolEndorseVO);
        return "billPool/addAbPoolEndorse";
    }
    /**
     * 保存背书转让信息
     *
     * @param abPoolEndorseVO
     * @return
     */
    @LogMessage(content = "保存背书转让信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(AbPoolEndorseVO abPoolEndorseVO, HttpServletRequest request) {
        abPoolEndorseService.saveAbPoolEndorseInfo(abPoolEndorseVO, this.getCurrentUser());
        return success();
    }
    @PostMapping("/operation")
    @ResponseBody
    public AjaxJson operation(@RequestParam("pdId") String pdId, @RequestParam("opt") String opt) {
        abPoolEndorseService.operation(pdId,opt,this.getCurrentUser(),false);
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
        return "billPool/approve/listAbPoolEndorseApprove";
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
        return abPoolEndorseService.dataTableApprove(dataTablesInput);
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
            AbPoolEndorseVO abPoolEndorseVO = abPoolEndorseService.getAbPoolEndorseVOByPdId(businessId);
            model.addAttribute("abPoolEndorseVO", abPoolEndorseVO);
            if (StringUtils.isNotEmpty(abPoolEndorseVO.getId())) {
                AbPoolEndorseVO billInfo = abPoolEndorseService.getAbBillEndorseApplyVOByVvid(abPoolEndorseVO.getVvId(), abPoolEndorseVO.getId());
                model.addAttribute("billInfo", billInfo);
            }
        }
        model.addAttribute("isApprove", true);
        model.addAttribute("taskId", taskId);
        return "billPool/approve/approveEndorseApply";
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
            AbPoolEndorseVO abPoolEndorseVO = abPoolEndorseService.getAbPoolEndorseVOByPdId(businessId);
            model.addAttribute("abPoolEndorseVO", abPoolEndorseVO);
            if (StringUtils.isNotEmpty(abPoolEndorseVO.getId())) {
                AbPoolEndorseVO billInfo = abPoolEndorseService.getAbBillEndorseApplyVOByVvid(abPoolEndorseVO.getVvId(), abPoolEndorseVO.getId());
                model.addAttribute("billInfo", billInfo);
            }
        }
        model.addAttribute("isApprove", true);
        return  "billPool/mobile/approveEndorseApply";
    }
    /**
     * 背书转让详细信息页面跳转
     *
     * @return
     */
    @GetMapping("/view")
    public String view(AbPoolEndorseVO abPoolEndorseVO, Model model) {
        if (StringUtils.isNotEmpty(abPoolEndorseVO.getId())) {
            //票据池信息
            AbPoolEndorseVO endorseVO = abPoolEndorseService.getAbPoolEndorseVOByPdId(abPoolEndorseVO.getId());
            model.addAttribute("abPoolEndorseVO", endorseVO);
            //操作日志
            model.addAttribute("abLogList", abLogService.listAbLog(null, endorseVO.getLastId() != null ? endorseVO.getLastId() : endorseVO.getId()));
            if (StringUtils.isNotEmpty(endorseVO.getVvId())) {
                //票据明细
                AbPoolEndorseVO billInfo = abPoolEndorseService.getAbBillEndorseApplyVOByVvid(endorseVO.getVvId(), endorseVO.getId());
                model.addAttribute("billInfo", billInfo);
                //业务流转信息
                if (StringUtils.isNotEmpty(billInfo.getAccId())) {
                    AbPoolDtl abPoolDtl = abPoolEndorseService.getAbPoolDtlByLastPdId(abPoolEndorseVO.getId());
                    List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(billInfo.getAccId(),abPoolDtl!=null?abPoolDtl.getId():null);
                    model.addAttribute("accrecordsList", abAccrecordsList);
                }
            }
        }
        model.addAttribute("isApprove", false);
        return "billPool/viewAbPoolEndorseApply";
    }
}
