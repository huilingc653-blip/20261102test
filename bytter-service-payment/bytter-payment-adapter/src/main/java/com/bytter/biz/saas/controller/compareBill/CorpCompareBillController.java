package com.bytter.biz.saas.controller.compareBill;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.compareBill.entity.CorpCompareBill;
import com.bytter.biz.compareBill.service.CorpCompareBillService;
import com.bytter.biz.compareBill.vo.CorpCompareBillInputVO;
import com.bytter.biz.compareBill.vo.v1.CorpCompareBillConfigVO;
import com.bytter.biz.compareBill.vo.v1.CorpCompareBillPageVO;
import com.bytter.biz.compareBill.vo.v1.CorpCompareBillPrintPageVO;
import com.bytter.biz.payment.api.dto.CorpCompareBillSubmitDTO;
import com.bytter.biz.payment.api.service.IEVisaTypeService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.TaskResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 单位对账单控制器
 *
 * @author zhoul 2020-11-03
 * @since 3.4
 */

@Api(value = "成员单位对账",tags = "成员单位对账")
@RestController("v1CorpCompareBillController")
@RequestMapping("/v1-payment/corpCompareBill")
public class CorpCompareBillController extends BaseController {
    @Autowired
    private CorpCompareBillService corpCompareBillService;
    @DubboReference(check = false)
	private IFcAccService fcAccService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IUserService userService;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private IEVisaTypeService ieVisaTypeService;

    @DubboReference(check = false)
    private ICorpService corpService;
    @ApiOperation("跳转列表页面前的相关配置")
    @GetMapping("/getListConfig")
    @ResponseBody
    public AjaxJson<CorpCompareBillConfigVO> getListConfig(String type, String flowStatus) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.MONTH, -1);
        CorpCompareBillConfigVO configVO = new CorpCompareBillConfigVO();
        configVO.setCurrentYear(calendar.get(Calendar.YEAR));
        configVO.setCurrentMonth(calendar.get(Calendar.MONTH) + 1);
        configVO.setType(type);
        configVO.setFlowStatus(flowStatus);
        return success(configVO);
    }

    @ApiOperation(value = "对账处理",notes = "对账处理的type为index，对账查询的type为query")
    @ResponseBody
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<CorpCompareBillInputVO>> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Map<String, Object> formData = input.getFormdata();
        String type = (String) formData.get("type");
        DataTablesOutput corpCompareBillInputVO = corpCompareBillService.getCorpCompareBillInputVO(input, getCurrentUser().getId());
        List<String> corpIds = new ArrayList<>();
        List<String> currencyIdIds = new ArrayList<>();
        List<String> opCodeList = new ArrayList<>();
        if(corpCompareBillInputVO != null && CollectionUtil.isNotEmpty(corpCompareBillInputVO.getData())){
            List data = corpCompareBillInputVO.getData();
            for(Object o : data){
                CorpCompareBillInputVO vo = (CorpCompareBillInputVO)o;
                vo.setErpBalance(vo.getErpBalance() == null ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP).toString() : vo.getErpBalance());
                if(vo.getCorpId() != null){
                    corpIds.add(vo.getCorpId());
                }
                if(vo.getCurrencyId() != null){
                    currencyIdIds.add(vo.getCurrencyId());
                }
                if(vo.getOpCode() != null){
                    opCodeList.add(vo.getOpCode());
                }
            }
        }
        if(CollectionUtil.isNotEmpty(corpIds)){
            Map<String, String> nameByIds = corpService.getNameByIds(corpIds);
            if(CollectionUtil.isNotEmpty(nameByIds)){
                List data = corpCompareBillInputVO.getData();
                for(Object o : data){
                    CorpCompareBillInputVO vo = (CorpCompareBillInputVO)o;
                    vo.setCorpName(nameByIds.get(vo.getCorpId()));
                }
            }
        }
        if(CollectionUtil.isNotEmpty(currencyIdIds)){
            Map<String, BtCurrencyDTO> currencyByIds = currencyService.getCurrencyByIds(currencyIdIds);
            if(CollectionUtil.isNotEmpty(currencyByIds)){
                List data = corpCompareBillInputVO.getData();
                for(Object o : data){
                    CorpCompareBillInputVO vo = (CorpCompareBillInputVO)o;
                    vo.setCurrencyName(currencyByIds.get(vo.getCurrencyId()) == null ? null : currencyByIds.get(vo.getCurrencyId()).getCurName());
                }
            }
        }
        if(CollectionUtil.isNotEmpty(opCodeList)){
            Map<String, String> userNameByIds = userService.getUserNameByIds(opCodeList);
            if(CollectionUtil.isNotEmpty(userNameByIds)){
                List data = corpCompareBillInputVO.getData();
                for(Object o : data){
                    CorpCompareBillInputVO vo = (CorpCompareBillInputVO)o;
                    vo.setOpUserName(userNameByIds.get(vo.getOpCode()) == null ? null : userNameByIds.get(vo.getOpCode()));
                }
            }
        }
        if ("approve".equals(type)) {
            return success(corpCompareBillInputVO);
        }
        String year = (String) formData.get("year");
        String month = (String) formData.get("month");
        //当月存在余额则进行查询
        if (corpCompareBillService.existBalance(year + "-" + month)) {
            return success(corpCompareBillInputVO);
        } else {
            return success(new DataTablesOutput<Object>() {
                private boolean notExistBalance = true;
                public boolean isNotExistBalance() {
                    return notExistBalance;
                }
                public void setNotExistBalance(boolean notExistBalance) {
                    this.notExistBalance = notExistBalance;
                }
            });
        }
    }
    @ApiOperation("对账操作页面跳转数据获取")
    @GetMapping("/compare")
    @ResponseBody
    public AjaxJson<CorpCompareBillPageVO> compare(String billId, String account, String date, Model model) {
        CorpCompareBillPageVO pageVO = new CorpCompareBillPageVO();
        CorpCompareBillInputVO entity = corpCompareBillService.getCorpCompareBillByBillIdOrAccountAndDate(billId, account, date);
        pageVO.setEntity(entity);
        pageVO.setBalance(new BigDecimal(entity.getBalance()).doubleValue());
        pageVO.setErpBalance(new BigDecimal(entity.getErpBalance()==null?"0":entity.getErpBalance()).doubleValue());
        pageVO.setEntityMonth(entity.getBillMonth() == null);
        pageVO.setCompareDate(date);
        return success(pageVO);
    }
    @ApiOperation("审批详情数据获取")
    @GetMapping("/approve")
    @ResponseBody
    public AjaxJson<CorpCompareBillPageVO> approve(String billId, String account, String date, String taskId, Model model) {
        CorpCompareBillPageVO pageVO = new CorpCompareBillPageVO();
        CorpCompareBillInputVO entity = corpCompareBillService.getCorpCompareBillByBillIdOrAccountAndDate(billId, account, null);
        pageVO.setApprove("approve");
        pageVO.setTaskId(taskId);
        pageVO.setBalance(new BigDecimal(pageVO.getEntity().getBalance()).doubleValue());
        pageVO.setErpBalance(new BigDecimal(pageVO.getEntity().getErpBalance()).doubleValue());
        makeNotSameHtmlChar(entity);
        pageVO.setEntity(entity);
        pageVO.setCompareDate(date);
        return success(pageVO);
    }
    @ApiOperation("对账操作提交")
    @PostMapping("/compare")
    @ResponseBody
    public AjaxJson compare(@RequestBody CorpCompareBillSubmitDTO corpCompareBillDTO) {
        CorpCompareBill corpCompareBill = new CorpCompareBill();
        String billId = corpCompareBillDTO.getBillId();
        String id = getCurrentUser().getId();
        BeanUtils.copyProperties(corpCompareBillDTO, corpCompareBill);
        corpCompareBill.setAccountBalance(corpCompareBillDTO.getBalance() == null ? BigDecimal.ZERO : corpCompareBillDTO.getBalance());
        corpCompareBill.setOpCode(id);
        corpCompareBill.setBillMonth(StrUtil.isBlank(corpCompareBillDTO.getBillMonth()) ? corpCompareBillDTO.getCompareDate() : corpCompareBillDTO.getBillMonth());
        corpCompareBillService.saveOrUpdate(corpCompareBill, billId, id);
        return success();
    }
    @ApiOperation("批量对账 对应操作页面的对账相符")
    @PostMapping("/compareBatch")
    @ResponseBody
    public AjaxJson compareBatch(@RequestBody List<CorpCompareBill> corpCompareBillList) {
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

    @ApiOperation("回执打印 页面数据获取")
    @GetMapping("/print")
    public AjaxJson<CorpCompareBillPrintPageVO> print(String billId) {
        CorpCompareBillPrintPageVO pageVO = new CorpCompareBillPrintPageVO();
        CorpCompareBill corpCompareBill = corpCompareBillService.getEntityDao().get(billId,true);
        corpCompareBillService.updateBillPrintCount(billId);
        CorpCompareBillInputVO corpCompareBillInputVO = new CorpCompareBillInputVO();
        BeanUtils.copyProperties(corpCompareBill, corpCompareBillInputVO);
        FcAccDTO accDTOByAccount = fcAccService.getAccDTOByAccount(corpCompareBill.getAccount());
        BtCurrencyDTO currencyDTO = currencyService.getObjectById(accDTOByAccount.getCurId());
        corpCompareBillInputVO.setAccountType(accDTOByAccount.getAccType());
        corpCompareBillInputVO.setAccountName(accDTOByAccount.getAccName());
        corpCompareBillInputVO.setBalance(corpCompareBill.getAccountBalance()==null?"0": corpCompareBill.getAccountBalance().toString());
        corpCompareBillInputVO.setErpBalance(corpCompareBill.getErpBalance() == null ? "0" : corpCompareBill.getErpBalance().toString());
        corpCompareBillInputVO.setCurrencyName(currencyDTO.getCurName());
        corpCompareBillInputVO.setCorpName(accDTOByAccount.getCorpName());
        corpCompareBillInputVO.setNetName(accDTOByAccount.getNetName());
        corpCompareBillInputVO.setSame(corpCompareBill.getIsCheck());
        if(corpCompareBillInputVO.getBalance()!=null){
            pageVO.setBalance(new BigDecimal(corpCompareBillInputVO.getBalance()).doubleValue());
        }
        if(corpCompareBillInputVO.getErpBalance()!=null){
            pageVO.setErpBalance(new BigDecimal(corpCompareBillInputVO.getErpBalance()).doubleValue());
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月");
        if(EmptyUtils.isNotEmpty(corpCompareBill.getBillMonth())){
            try {
                Date parse = format1.parse(corpCompareBill.getBillMonth());
                corpCompareBillInputVO.setBillMonth(format2.format(parse));
            } catch (ParseException ignored) {
                ignored.printStackTrace();
            }
        }
        String attachmentId = ieVisaTypeService.getAttachmentId("17", accDTOByAccount.getNetId());
        pageVO.setAttachmentId(attachmentId);
        List<TaskResponseDTO> taskResponseDTOS = workFlowService.listCommentsByBusinessId(corpCompareBill.getId());
        UserDTO userDTO = userService.getByUserId(corpCompareBill.getOpCode());
        pageVO.setUsername(userDTO.getUsername());
        StringBuilder sb = new StringBuilder();
        Set<String> approvesName = new HashSet<>();
        if(taskResponseDTOS != null){
            for (int i = 0; i < taskResponseDTOS.size(); i++) {
                if(!approvesName.contains(taskResponseDTOS.get(i).getUserName())){
                    sb.append(taskResponseDTOS.get(i).getUserName());
                    sb.append(",");
                    approvesName.add(taskResponseDTOS.get(i).getUserName());
                }
            }
        }
        if(sb.length()>0){
            pageVO.setApproves(sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "");
        }
        makeNotSameHtmlChar(corpCompareBillInputVO);
        pageVO.setEntity(corpCompareBillInputVO);
        if (corpCompareBill.getCheckDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            pageVO.setCompareDate(dateFormat.format(corpCompareBill.getCheckDate()));
        }
        pageVO.setPrintDate(new Date());
        pageVO.setPrinter(getCurrentUser().getUsername());
        return success(pageVO);
    }
    private void makeNotSameHtmlChar(CorpCompareBillInputVO entity){
        if("0".equals(entity.getSame())){
            if(StringUtils.isNoneBlank(entity.getAccountCreditMoney())){
                entity.setAccountCreditMoney(entity.getAccountCreditMoney().replace("\n","<br/>"));
            }
            if(StringUtils.isNoneBlank(entity.getAccountDebitMoney())){
                entity.setAccountDebitMoney(entity.getAccountDebitMoney().replace("\n","<br/>"));
            }
            if(StringUtils.isNoneBlank(entity.getErpCreditMoney())){
                entity.setErpCreditMoney(entity.getErpCreditMoney().replace("\n","<br/>"));
            }
            if(StringUtils.isNoneBlank(entity.getErpDebitMoney())){
                entity.setErpDebitMoney(entity.getErpDebitMoney().replace("\n","<br/>"));
            }
        }
    }
}
