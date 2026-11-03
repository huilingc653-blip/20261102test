package com.bytter.check.web.controller;
import com.bytter.biz.payment.api.service.IEVisaTypeService;
import com.bytter.check.entity.CorpCompareBill;
import com.bytter.check.service.CorpCompareBillService;
import com.bytter.check.vo.CorpCompareBillInputVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.TaskResponseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 单位对账单控制器
 *
 * @author zhoul 2020-11-03
 * @since 3.4
 */
@Controller
@RequestMapping("/check/corpCompareBill")
public class CorpCompareBillController extends BaseController {
    @Autowired
    private CorpCompareBillService corpCompareBillService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IEVisaTypeService ieVisaTypeService;
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_CORPCOMPAREBILL_INDEX_APPLY,
//            ResourceConstant.NETBANK_CORPCOMPAREBILL_QUERY_APPLY},
//            logical = Logical.OR)
    @GetMapping("/list/{type}")
    public String list(@PathVariable("type") String type, Model model, String flowStatus) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.MONTH, -1);
        model.addAttribute("currentYear", calendar.get(Calendar.YEAR));
        model.addAttribute("currentMonth", calendar.get(Calendar.MONTH) + 1);
        model.addAttribute("type", type);
        model.addAttribute("flowStatus", flowStatus);
        calendar.add(Calendar.YEAR, -20);
        return "compareBill/listCorpCompareBill";
    }
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_CORPCOMPAREBILL_INDEX_APPLY,
//            ResourceConstant.NETBANK_CORPCOMPAREBILL_QUERY_APPLY},
//            logical = Logical.OR)
    @ResponseBody
    @PostMapping("/datatable")
    public Object datatable(@RequestBody DataTablesInput dataTablesInput) {
        Map<String, Object> formData = dataTablesInput.getFormdata();
        String type = (String) formData.get("type");
        if ("approve".equals(type)) {
            return corpCompareBillService.getCorpCompareBillInputVO(dataTablesInput, getCurrentUser().getId());
        }
        String year = (String) formData.get("year");
        String month = (String) formData.get("month");
        //当月存在余额则进行查询
        if (corpCompareBillService.existBalance(year + "-" + month)){
            return corpCompareBillService.getCorpCompareBillInputVO(dataTablesInput, getCurrentUser().getId());
        } else {
            return new DataTablesOutput<Object>() {
                private boolean notExistBalance = true;
                public boolean isNotExistBalance() {
                    return notExistBalance;
                }
                public void setNotExistBalance(boolean notExistBalance) {
                    this.notExistBalance = notExistBalance;
                }
            };
        }
    }
    @GetMapping({"/compare"})
    public String compare(String billId, String account, String date, Model model) {
        CorpCompareBillInputVO entity = corpCompareBillService.getCorpCompareBillByBillIdOrAccountAndDate(billId, account, date);
        model.addAttribute("entity", entity);
        model.addAttribute("balance", new BigDecimal(entity.getBalance()).doubleValue());
        model.addAttribute("erpBalance", new BigDecimal(entity.getErpBalance() == null ? "0" : entity.getErpBalance()).doubleValue());
        model.addAttribute("entityMonth", entity.getBillMonth() == null);
        model.addAttribute("compareDate", date);
        return "compareBill/compare";
    }
    @GetMapping({"/approve"})
    public String approve(String billId, String account, String date, String taskId, Model model) {
        model.addAttribute("approve", "approve");
        model.addAttribute("taskId", taskId);
        CorpCompareBillInputVO entity = corpCompareBillService.getCorpCompareBillByBillIdOrAccountAndDate(billId, account, null);
        model.addAttribute("balance", new BigDecimal(entity.getBalance()).doubleValue());
        model.addAttribute("erpBalance", new BigDecimal(entity.getErpBalance()).doubleValue());
        makeNotSameHtmlChar(entity);
        model.addAttribute("entity", entity);
        model.addAttribute("compareDate", date);
        return "compareBill/compare";
    }
    @PostMapping("/compare")
    @ResponseBody
    public Object compare(CorpCompareBill corpCompareBill, HttpServletRequest request) {
        String billId = request.getParameter("billId");
        String id = getCurrentUser().getId();
        corpCompareBill.setOpCode(id);
        corpCompareBillService.saveOrUpdate(corpCompareBill, billId, id);
        return success();
    }
    @PostMapping("/compareBatch")
    @ResponseBody
    public Object compareBatch(@RequestBody List<CorpCompareBill> corpCompareBillList) {
        String id = getCurrentUser().getId();
        for (CorpCompareBill corpCompareBill : corpCompareBillList) {
            corpCompareBill.setOpCode(id);
            if (StringUtils.isEmpty(corpCompareBill.getId())) {
                corpCompareBillService.saveOrUpdate(corpCompareBill, null, id);
            } else {
                corpCompareBillService.saveOrUpdate(corpCompareBill, corpCompareBill.getId(), id);
            }
        }
        return success();
    }
    @GetMapping("/print")
    public String print(String billId, Model model) {
        CorpCompareBill corpCompareBill = corpCompareBillService.getEntityDao().get(billId);
        corpCompareBillService.updateBillPrintCount(billId);
        CorpCompareBillInputVO corpCompareBillInputVO = new CorpCompareBillInputVO();
        BeanUtils.copyProperties(corpCompareBill, corpCompareBillInputVO);
        FcAccDTO accDTOByAccount = fcAccService.getAccDTOByAccount(corpCompareBill.getAccount());
        BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(accDTOByAccount.getCurId());
        corpCompareBillInputVO.setAccountType(accDTOByAccount.getAccType());
        corpCompareBillInputVO.setAccountName(accDTOByAccount.getAccName());
        corpCompareBillInputVO.setBalance(corpCompareBill.getAccountBalance().toString());
        corpCompareBillInputVO.setErpBalance(corpCompareBill.getErpBalance() == null ? "0" : corpCompareBill.getErpBalance().toString());
        corpCompareBillInputVO.setCurrencyName(currencyDTO.getCurName());
        corpCompareBillInputVO.setCorpName(accDTOByAccount.getCorpName());
        corpCompareBillInputVO.setNetName(accDTOByAccount.getNetName());
        corpCompareBillInputVO.setSame(corpCompareBill.getIsCheck());
        model.addAttribute("balance", new BigDecimal(corpCompareBillInputVO.getBalance()).doubleValue());
        model.addAttribute("erpBalance", new BigDecimal(corpCompareBillInputVO.getErpBalance()).doubleValue());
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月");
        try {
            Date parse = format1.parse(corpCompareBill.getBillMonth());
            corpCompareBillInputVO.setBillMonth(format2.format(parse));
        } catch (ParseException ignored) {
        }
        String attachmentId = ieVisaTypeService.getAttachmentId("17", accDTOByAccount.getNetId());
        model.addAttribute("attachmentId", attachmentId);
        List<TaskResponseDTO> taskResponseDTOS = workFlowService.listCommentsByBusinessId(corpCompareBill.getId());
        UserDTO userDTO = userService.getByUserId(corpCompareBill.getOpCode());
        model.addAttribute("username", userDTO.getUsername());
        StringBuilder sb = new StringBuilder();
        if(null != taskResponseDTOS){
            for (int i = 0; i < taskResponseDTOS.size(); i++) {
                sb.append(taskResponseDTOS.get(i).getUserName());
                if (i != taskResponseDTOS.size() - 1) {
                    sb.append(",");
                }
            }
        }

        model.addAttribute("approves", sb.toString());
        makeNotSameHtmlChar(corpCompareBillInputVO);
        model.addAttribute("entity", corpCompareBillInputVO);
        model.addAttribute("printDate", new Date());
        model.addAttribute("printer", getCurrentUser().getUsername());
        return "compareBill/print";
    }
    private void makeNotSameHtmlChar(CorpCompareBillInputVO entity) {
        if ("0".equals(entity.getSame())) {
            if (StringUtils.isNoneBlank(entity.getAccountCreditMoney())) {
                entity.setAccountCreditMoney(entity.getAccountCreditMoney().replace("\n", "<br/>"));
            }
            if (StringUtils.isNoneBlank(entity.getAccountDebitMoney())) {
                entity.setAccountDebitMoney(entity.getAccountDebitMoney().replace("\n", "<br/>"));
            }
            if (StringUtils.isNoneBlank(entity.getErpCreditMoney())) {
                entity.setErpCreditMoney(entity.getErpCreditMoney().replace("\n", "<br/>"));
            }
            if (StringUtils.isNoneBlank(entity.getErpDebitMoney())) {
                entity.setErpDebitMoney(entity.getErpDebitMoney().replace("\n", "<br/>"));
            }
        }
    }
}
