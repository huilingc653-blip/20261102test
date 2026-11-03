package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.receipt.service.BisBankReceiptDtlHisService;
import com.bytter.biz.receipt.service.BisBankReceiptDtlService;
import com.bytter.biz.receipt.vo.EBankMoreBillVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 银行电子回单
 *
 * @author zuoy 2018-04-09
 */
@ApiOperation(value = "/v1-payment/receipt/bisbankreceiptdtlhis", tags = "银行电子回单")
@RestController("v1BisBankReceiptDtlHisController")
@RequestMapping("/v1-payment/receipt/bisbankreceiptdtlhis")
public class BisBankReceiptDtlHisController extends BaseController {
    @Autowired
    private BisBankReceiptDtlHisService bisBankReceiptDtlHisService;
    @Autowired
    private BisBankReceiptDtlService bisBankReceiptDtlService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;

    /**
     * BIS_ACC_BAL列表 页面跳转
     *
     * @return
     */
    @ApiOperation("列表页面跳转")
    @LogMessage(content = "查询BIS_ACC_BAL列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<Map<String, Object>> list() {
        Map<String, Object> model = new HashMap<>();
        Date endDate = DateUtils.getDate();
        Date startDate = DateUtils.addWeek(DateUtils.getDate(), -1);
        model.put("endDate", endDate);
        model.put("startDate", startDate);
        return AjaxJson.success(model);
    }

    /**
     * BIS_ACC_BAL table列表
     *
     * @return
     */
    @ApiOperation(" table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisBankReceiptDtlHisVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisBankReceiptDtlHisService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 预览页面加载图片
     *
     * @return
     */
    @ApiOperation(" 预览页面加载图片")
    @GetMapping("/initImg")
    public void initImg(String id, HttpServletRequest request, HttpServletResponse response) {
        try {
            BisBankReceiptDtlHisVO bisBankReceiptDtlHis = this.bisBankReceiptDtlHisService.getByIdUnion(id);
            if ("pdf".equalsIgnoreCase(bisBankReceiptDtlHis.getFiletype().trim())) {
                this.bisBankReceiptDtlHisService.streamOperPdf(request, response, bisBankReceiptDtlHis);
            } else {
                this.bisBankReceiptDtlHisService.streamOper(request, response, bisBankReceiptDtlHis);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 列表打印
     *
     * @return
     */
    @ApiOperation(" 列表打印")
    @GetMapping("/print")
    public AjaxJson<String> print(String id) {
        this.bisBankReceiptDtlHisService.updatePrint(id);
        return AjaxJson.success(id);
    }

    /**
     * 批量预览
     *
     * @return
     */
    @ApiOperation(" 批量预览")
    @GetMapping("/batchPreview")
    public AjaxJson<List<String>> batchPreview(String ids) {
        List<String> listId = new ArrayList<>(Arrays.asList(ids.split(CommonConstant.COMMA_SPLIT)));
        return AjaxJson.success(listId);
    }

    /**
     * 批量打印
     *
     * @return
     */
    @ApiOperation(" 批量打印")
    @GetMapping("/batchPrint")
    public AjaxJson<List<String>> batchPrint(String ids) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.bisBankReceiptDtlHisService.updatePrint(id);
            listId.add(id);
        }
        return AjaxJson.success(listId);

    }

    /**
     * 下载
     */
    @ApiOperation(" 下载")
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        bisBankReceiptDtlHisService.download(id, request, response);
    }

    /**
     * 批量下载
     */
    @ApiOperation(" 批量下载")
    @RequestMapping("/batchDownload")
    public void batchDownload(String ids, HttpServletRequest request, HttpServletResponse response) {
        bisBankReceiptDtlHisService.batchDownload(ids, request, response);
    }

    /**
     * 关联付款单跳列表页面
     *
     * @return
     */
    @ApiOperation(" 关联付款单跳列表页面")
    @GetMapping("/morebill")
    public AjaxJson<List<EBankMoreBillVO>> morebill(String id) {
        List<String> ids = StringUtils.splitToList("/", id);
        List<EBankMoreBillVO> list1 = new ArrayList();
        for (String idi : ids) {
            BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(idi);
            EBankMoreBillVO eBankMoreBillVO = bisBankReceiptDtlService.getMoreBill(idi);
            eBankMoreBillVO.setTransTime(bisAccDtlDTO.getTransTime());
            eBankMoreBillVO.setCorpName(bisAccDtlDTO.getCorpName());
            eBankMoreBillVO.setBankAcc(bisAccDtlDTO.getBankAcc());
            eBankMoreBillVO.setAccName(bisAccDtlDTO.getAccName());
            eBankMoreBillVO.setBankName(bisAccDtlDTO.getBankName());
            eBankMoreBillVO.setCdSignName(bisAccDtlDTO.getCdSignName());
            eBankMoreBillVO.setCurName(bisAccDtlDTO.getCurName());
            eBankMoreBillVO.setAmt(bisAccDtlDTO.getAmt());
            eBankMoreBillVO.setBal(bisAccDtlDTO.getBal());
            eBankMoreBillVO.setOppAccNo(bisAccDtlDTO.getOppAccNo());
            eBankMoreBillVO.setOppAccName(bisAccDtlDTO.getOppAccName());
            eBankMoreBillVO.setOppAccBank(bisAccDtlDTO.getOppAccBank());
            eBankMoreBillVO.setRemark(bisAccDtlDTO.getRemark());
            eBankMoreBillVO.setPostscript(bisAccDtlDTO.getPostscript());
            eBankMoreBillVO.setUses(bisAccDtlDTO.getUses());
            eBankMoreBillVO.setDtlId(bisAccDtlDTO.getId());
            if (StringUtils.isNotEmpty(eBankMoreBillVO.getBillId())) {
                list1.add(eBankMoreBillVO);
            }
        }
        return AjaxJson.success(list1);
    }
}
