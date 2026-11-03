package com.bytter.biz.finacial.report.controller;

import com.bytter.biz.finacial.report.service.FcAccountStatementService;
import com.bytter.biz.finacial.report.vo.FcAccountDtlVO;
import com.bytter.biz.finacial.report.vo.FcAccountStatementVO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @author luxy
 * @Title: FcAccountStatementController
 * @ProjectName T3.3
 * @Description: TODO
 * @date 2020/6/30 14:55
 */
@Controller
@RequestMapping("/finacial/report/accountPrint")
public class FcAccountStatementController extends BaseController {
    @Autowired
    private FcAccountStatementService statementService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;

    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;

    @DubboReference(check = false)
    private IFcVoucherTypeService iFcVoucherTypeService;
    @GetMapping("/list")
    //@RequiresPermissions(ResourceConstant.FINACIAL_ACCOUNT_STATEMENT_PRINT)
    public String list(Model model, FcAccountStatementVO statementVO, String searchFlag, Integer start) {
        model.addAttribute("account", statementVO.getAccount());
        model.addAttribute("initStartDate", DateUtils.getFirstMonthDate(DateUtils.getDate()));
        model.addAttribute("curDate", DateUtils.getDate());
        if (null == start) {
            start = Integer.valueOf(1);
        }
        if (EmptyUtils.isBlank(searchFlag)) {//首次进入页面
            statementVO.setStartDate(DateUtils.getFirstMonthDate(DateUtils.getDate()));
            statementVO.setEndDate(DateUtils.getDate());
        }
        List<FcAccDTO> fcAccDTOS = statementService.getAccAll(statementVO, getCurrentUser());
        if (null != fcAccDTOS && fcAccDTOS.size() > 0) {
            FcAccDTO accDTO = fcAccDTOS.get(start - 1);
            if (!EmptyUtils.isBlank(statementVO.getAccount())) {
                statementVO.setAccount(statementVO.getAccount());
            } else {
                statementVO.setAccount(accDTO.getBlnctrAcc());
            }
            FcAccountStatementVO accountVO = statementService.getAccountMsg(statementVO);
            if (null != accountVO) {
                List<FcAccountDtlVO> list = statementService.getAccountDtlList(statementVO, getCurrentUser());

                if (!list.isEmpty()){
                    List<String> voucherTypes = list.stream().map(FcAccountDtlVO::getVoucherType).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
                    Map<String, FcVoucherTypeDTO> fcVoucherTypeByIdsToMap = iFcVoucherTypeService.getFcVoucherTypeByIdsToMap(voucherTypes);
                    list.stream().forEach(vo -> {
                        if (fcVoucherTypeByIdsToMap.containsKey(vo.getVoucherType())){
                            FcVoucherTypeDTO fcVoucherTypeDTO = fcVoucherTypeByIdsToMap.get(vo.getVoucherType());
                            if (StringUtils.isNotBlank(fcVoucherTypeDTO.getVoucherName())){
                                vo.setVoucherName(fcVoucherTypeDTO.getVoucherName());
                            }
                        }
                    });
                }

                BigDecimal debitMoneySum = BigDecimal.ZERO;
                BigDecimal creditMoneySum = BigDecimal.ZERO;
                List<FcAccountDtlVO> tempList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    FcAccountDtlVO dtlVO = list.get(i);
                    debitMoneySum = debitMoneySum.add(dtlVO.getDebitMoney());
                    creditMoneySum = creditMoneySum.add(dtlVO.getCreditMoney());
                    tempList.add(dtlVO);
                    if (i % 23 == 0 && i > 0) {
                        FcAccountDtlVO upDtlVO = new FcAccountDtlVO();
                        upDtlVO.setBalance(dtlVO.getBalance());
                        tempList.add(upDtlVO);
                    }
                }
                if(StringUtils.isNotBlank(accountVO.getCurId()) && StringUtils.isBlank(accountVO.getCurName())){
                    BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(accountVO.getCurId());
                    accountVO.setCurName(btCurrencyDTO.getCurName());
                }

                accountVO.setDebitMoneySum(debitMoneySum);
                accountVO.setCreditMoneySum(creditMoneySum);
                accountVO.setAccountDtlVOList(tempList);
                model.addAttribute("accountVo", accountVO);
            }
            long resultCount = fcAccDTOS.size();
            model.addAttribute("curr", start);
            model.addAttribute("total", resultCount);
        }
        model.addAttribute("curDate", DateUtils.getDate());
        model.addAttribute("searchVO", statementVO);
        return "finacial/report/listAccountStatement";
    }

    /**
     * 导出excel
     **/
    @PostMapping("/excel")
    public String excel(FcAccountStatementVO searchVO, HttpServletResponse response) throws Exception {
        String fileName = "对账单";//报表名称：对账单查询
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(fileName);
        //创建表格并填充数据
        wb = statementService.addDataToSheet(searchVO, sheet, wb, getCurrentUser());
        //实现Excel以文件流的形式到浏览器中,浏览器左下角出现Excel的下载提示，
        OutputStream out = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes(), "iso-8859-1") + ".xls");
        response.setContentType("application/x-download");
        wb.write(out);
        out.flush();
        out.close();
        return null;
    }

    //@RequiresPermissions(ResourceConstant.FINACIAL_ACCOUNT_STATEMENT_PRINT)
    @PostMapping("/queryListAccount")
    @ResponseBody
    public List<FcAccDTO> queryListAccount(String corpId) {
        if ("undefined".equals(corpId)) {
            corpId = null;
        }
        return innerAccountService.listAccountByParams(null, null, corpId, null);
    }

    /**
     * 批量打印
     *
     * @return
     */
    @GetMapping("/batchPrint")
    public String batchPrint(FcAccountStatementVO searchVO, Model model) {
        List<FcAccountStatementVO> list = statementService.batchPrint(searchVO, getCurrentUser());
        model.addAttribute("list", list);
        model.addAttribute("size", list.size());
        model.addAttribute("curDate", DateUtils.getDate());
        return "finacial/report/listAccountStatementBatchPrint";
    }
}
