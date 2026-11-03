package com.bytter.biz.bond.controller;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondDetailService;
import com.bytter.guarantee.api.IFcBondLogService;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.guarantee.vo.FcBondLogVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.pricing.service.IPricingDepositRateService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @author luxy
 * @version 1.0
 * @Title: Controller
 * @Description: 保证金管理
 * @date 2018-09-04
 */
@Controller
@RequestMapping("/bond/fcbond")
public class FcBondController extends BaseController {
    @DubboReference(check = false)
    private IFcBondDetailService fcBondDetailService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IPricingDepositRateService iPricingDepositRateService;
    @DubboReference(check = false)
    IFcBondLogService fcBondLogService;
    @DubboReference(check = false)
    private IBisAccBalService iBisAccBalService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 保证金列表
     */
    //////@RequiresPermissions(FcBondConstant.NETBANK_FCBOND_DEPOSITOUT_QUERY)
    @GetMapping("/list")
    @LogMessage(content = "跳转到存出保证金的详情页", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String list(Model model) {
        return "bond/listFcBondDetail";
    }

    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    @LogMessage(content = "查询审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "bond/listFcBondDetailApprove";
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return this.fcBondDetailService.dataTableApprove(dataTablesInput);
    }

    /**
     * fusb,2018-9-12添加 根据保证金id,得到保证金信息,跳转到审批详情页面
     *
     * @param id
     * @return
     */
    @LogMessage(content = "跳转到审批详情页", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcBondDetailVO fcBondDetailVO = this.fcBondDetailService.getFcBondById(id);
            if (StringUtils.isNotEmpty(fcBondDetailVO.getOutBankAcc())) {
                BisAccBalDTO bankInfo = iBisAccBalService.getLastBisAccBalBy(fcBondDetailVO.getOutBankAcc(), fcBondDetailVO.getCurId());
                if (null != bankInfo) {
                    fcBondDetailVO.setOutBankAccMoney(bankInfo.getAvailBal());
                } else {
                    fcBondDetailVO.setOutBankAccMoney(BigDecimal.ZERO);
                }
            }
            model.addAttribute("fcBond", fcBondDetailVO);
            model.addAttribute("taskId", taskId);
        }
        return "bond/approveFcBondDetail";
    }

    /**
     * 保证金表 table列表
     *
     * @param dataTablesInput
     * @return
     */
    //////@RequiresPermissions(FcBondConstant.NETBANK_FCBOND_DEPOSITOUT_QUERY)
    @LogMessage(content = "查询FC_BOND列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcBondDetailService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 保证金新增页面跳转
     *
     * @param
     * @param model
     * @return
     */
    //////@RequiresPermissions(FcBondConstant.NETBANK_FCBOND_DEPOSITOUT_APPLY)
    @LogMessage(content = "新增保证金页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("inputDate", new Date());
        return "bond/addFcBondDetail";
    }

    /**
     * 保存新增的保证金
     *
     * @param fcBond
     * @return AjaxJson
     */
    //////@RequiresPermissions(FcBondConstant.NETBANK_FCBOND_DEPOSITOUT_APPLY)
    @LogMessage(content = "添加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcBondDetailVO fcBond) {
        fcBondDetailService.saveFcBondDetail(fcBond, this.getCurrentUser());
        return success();
    }

    /**
     * 获取已经开好户的保证金银行账户
     *
     * @param corpId
     * @return
     */
    @PostMapping("/getBondAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBondAcc(String corpId) {
        List<BtBankAccDTO> listBondAcc = new ArrayList<BtBankAccDTO>();
        BtBankAccDTO bankDTO = new BtBankAccDTO();
        if (StringUtils.isNotEmpty(corpId)) {
            bankDTO.setCorpId(corpId);
        } else {
            List<String> corpIdList = corpService.getCorpIdsByUserResource("netbank:fcBond:depositOut:apply", getCurrentUser().getId());
            String corpIds = String.join(",", corpIdList);
            bankDTO.setCorpId(corpIds);
        }
        bankDTO.setAccType("04");
        listBondAcc = iBtBankAccService.getBankAccListAll(bankDTO);
        return listBondAcc;
    }

    /**
     * @author luxy 判断所选保证金账户是否已存出保证金
     */
    @PostMapping("/queryBondAcc/{id}")
    @ResponseBody
    public AjaxJson queryBondAcc(@PathVariable("id") String id) {
        if (!(fcBondDetailService.queryBondAcc(id))) {
            return null;
        }
        return success();
    }

    /**
     * 编辑存出保证金保证金页面跳转
     *
     * @param id
     * @param model
     * @return
     */
    //////@RequiresPermissions(FcBondConstant.NETBANK_FCBOND_DEPOSITOUT_APPLY)
    @LogMessage(content = "编辑保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editBond/{id}")
    public String editBond(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcBondDetailVO fcBond = fcBondDetailService.getFcBondById(id);
            if (StringUtils.isNotEmpty(fcBond.getOutBankAcc())) {
                BisAccBalDTO bankInfo = iBisAccBalService.getLastBisAccBalBy(fcBond.getOutBankAcc(), fcBond.getCurId());
                if (null != bankInfo) {
                    fcBond.setOutBankAccMoney(bankInfo.getAvailBal());
                } else {
                    fcBond.setOutBankAccMoney(BigDecimal.ZERO);
                }
            }
            model.addAttribute("fcBond", fcBond);
            model.addAttribute("outBankAccountIsOnline", fcBond.getIsOnline());
        }
        return "bond/editFcBondDetail";
    }

    /**
     * 修改存出保证金
     *
     * @param fcBond
     * @return
     */
    //////@RequiresPermissions(FcBondConstant.NETBANK_FCBOND_DEPOSITOUT_APPLY)
    @LogMessage(content = "修改保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FcBondDetailVO fcBond) {
        fcBondDetailService.updateFcBondDetail(fcBond, this.getCurrentUser());
        return success();
    }

    /**
     * 查看存出保证金详情
     *
     * @param id
     * @param model
     * @return
     */
    //////@RequiresPermissions(FcBondConstant.NETBANK_FCBOND_DEPOSITOUT_QUERY)
    @LogMessage(content = "查询保证金信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewBond/{id}")
    public String viewBond(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcBondDetailVO fcBond = fcBondDetailService.getFcBondById(id);
            if (StringUtils.isNotEmpty(fcBond.getOutBankAcc())) {
                BisAccBalDTO bankInfo = iBisAccBalService.getLastBisAccBalBy(fcBond.getOutBankAcc(), fcBond.getCurId());
                if (null != bankInfo) {
                    fcBond.setOutBankAccMoney(bankInfo.getAvailBal());
                } else {
                    fcBond.setOutBankAccMoney(BigDecimal.ZERO);
                }
            }
            model.addAttribute("fcBond", fcBond);
        }
        return "bond/viewFcBond";
    }

    /**
     * 删除存出保证金
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(FcBondConstant.NETBANK_FCBOND_DEPOSITOUT_APPLY)
    @LogMessage(content = "删除某条保证金信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/deleteBond/{id}")
    @ResponseBody
    public AjaxJson deleteBond(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            fcBondDetailService.deleteBond(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 存出保证金手机审批页面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return
     */
    @LogMessage(content = "跳转到手机审批详情页", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            FcBondDetailVO fcBondDetailVO = this.fcBondDetailService.getFcBondById(businessId);
            model.addAttribute("fcBond", fcBondDetailVO);
            model.addAttribute("taskId", taskId);
            List<FcBondLogVO> listOpMessage = fcBondLogService.listOpMessage(businessId);//操作日志
            model.addAttribute("listOpMessage", listOpMessage);
        }
        return "bond/mobileInputApprove";
    }

    /**
     * 根据期限、币别取定期利率
     */
    @PostMapping("/getRate")
    @ResponseBody
    public BigDecimal getRate(String dayLimit, String curId) {
        BigDecimal rate = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(dayLimit) && StringUtils.isNotEmpty(curId)) {
            rate = iPricingDepositRateService.getDepositRateByPeriod("05", dayLimit, curId);
        }
        return rate;
    }

    /**
     * 保证金账户查询页面编辑利率
     *
     * @param bondIds 保证金主键id
     * @param model   model对象
     * @return 编辑利率跳转地址
     */
    @GetMapping("/editFcBondInterestRate")
    public String tamFundCalendarInit(String bondIds, Model model) {
        model.addAttribute("bondIds", bondIds);
        return "bond/editFcBondInterestRate";
    }

    /**
     * 保证金账户查询批量编辑利率
     *
     * @param bondIds      多条id记录
     * @param interestRate 利率
     * @return 操作结果
     */
    @LogMessage(content = "保证金账户查询批量编辑利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchEditInterestRate")
    @ResponseBody
    public AjaxJson batchEditInterestRate(String bondIds, BigDecimal interestRate) {
        fcBondDetailService.batchEditInterestRate(bondIds, interestRate, this.getCurrentUser());
        return success();
    }
}
