package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.receipt.service.*;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.*;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.pricing.dto.DepositRateTypeDTO;
import com.bytter.system.pricing.service.IDepositRateTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 电子回单查询及打印
 * @date 2017-06-05 10:52:28
 */
@Api(value = "/v1-payment/receipt/eleBillQuery",tags = "内部 电子回单查询以及打印")
@RestController("v1EleBillQueryController")
@RequestMapping("/v1-payment/receipt/eleBillQuery")
public class EleBillQueryController extends BaseController {
    @Autowired
    private EleBillQueryService eleBillQueryService;
    @Autowired
    private EBillPrintService eBillPrintService;
    @DubboReference(check = false)
	private IDepositRateTypeService rateTypeService;
    @Autowired
    private EAccrualBillService eAccrualBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private ELoanBillService eLoanBillService;
    @Autowired
    private ENoticeBillService eNoticeBillService;
    @Autowired
    private EPoundageBillService ePoundageBillService;
    @Autowired
    private ETransBillService eTransBillService;
    @Autowired
    private EAcceptBillService eAcceptBillService;
    @Autowired
    private ECompareBillService eCompareBillService;
    @Autowired
    private EPrecativeLoanBillService ePrecativeLoanBillService;
    @DubboReference(check = false)
	private IFcCarryService fcCarryService;

    /**
     * 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_ELEBILL_QUERY)
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/billQuery")
    public String businessAccount(EleBillQueryVO eleBillQueryVO, Model model) {
        String billType = eleBillQueryVO.getBillType();
        model.addAttribute("billQueryPage", eleBillQueryVO);
        //不同的类型跳转不同的查询页面
        String url = ReceiptUtils.getListBill(billType);
        return "receipt/elecbill/list" + url;
    }
    /**
     * 查询电子回单类型列表
     *
     * @return
     */
    @ApiOperation("查询电子回单类型列表")
    @PostMapping("/json")
    @ResponseBody
    public AjaxJson<List<EReceiptTypeVO>> listByValidSign(String corpIds) {
        List<EReceiptTypeVO> list = eBillPrintService.listEleBillNum("1", corpIds, this.getCurrentUser());
        return success(list);
    }
    /**
     * 查询存款利率类型列表
     */
    @ApiOperation("查询存款利率类型列表")
    @GetMapping("/rateTypeList")
    @ResponseBody
    public AjaxJson<List<DepositRateTypeDTO>> getDepositRateType() {
        List<DepositRateTypeDTO> typeList = rateTypeService.getDepositRateType();
        return success(typeList);
    }
    /**
     * 所有电子回单查询表 table列表
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_ELEBILL_QUERY)
    @ApiOperation("所有电子回单查询表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(eleBillQueryService.listForUnion(input, this.getCurrentUser()));
    }
    /**
     * 批量打印
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_ELEBILL_QUERY)
    @ApiOperation("批量打印")
    @GetMapping("/batchPrint")
    public AjaxJson<EleBillBatchPrintVO> batchPrint(String ids, String billTypes, String printType) {
        //利息电子回单
        List<EAccrualBillVO> eAccrualBillVOList = new ArrayList<>();
        //贷款通知单
        List<ELoanBillVO> eLoanBillVOList = new ArrayList<>();
        //08-付款通知单*/
        ///**09-收款通知单*/
        ///**10-内转通知单*
        List<ENoticeBillVO> eNoticeBillVOList = new ArrayList<>();
        //11-手续费通知单
        List<EPoundageBillVO> ePoundageBillVOList = new ArrayList<>();
        //通知存单
        // 12//通知存单13//定期存单14定期取款单 15/通知取款单
        List<ETransBillVO> eTransBillVOList = new ArrayList<>();
        //16-承兑汇票电子回单
        List<EAcceptBillVO> eAcceptBillVOList = new ArrayList<>();
        //17-对账单电子回单
        List<Map<String, Object>> eCompareBillVOList = new ArrayList<>();
        //21-委托贷款通知单 22-委托贷款还款通知单
        List<EPrecativeLoanBillVO> ePrecativeLoanBillVOList = new ArrayList<>();
        String[] idStr = ids.split(CommonConstant.COMMA_SPLIT);
        String[] billTypeStr = billTypes.split(CommonConstant.COMMA_SPLIT);
        //签章
        Map<String, EVisaTypeVO> eVisaTypeVOMap = new HashMap<>();
        //利息
        Map<String, List<FcInterestDtlDTO>> fcinterestDtlMap = new HashMap<>();
        for (int i = 0; i < idStr.length; i++) {
            String billType = billTypeStr[i];
            String id = idStr[i];
            if (ReceiptConstant.RECEIPT_BILL_TYPE_04.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_05.equals(billType)) {
                DataTablesInput input = new DataTablesInput();
                input.addFilter(MatchTypeEnum.EQ, "id", id);
                DataTablesOutput output = eAccrualBillService.depositIstlist(input);
                List<EAccrualBillVO> eAccrualBillVOS = output.getData();
                EAccrualBillVO eAccrualBillVO = eAccrualBillVOS.get(0);
                if (!EmptyUtils.isEmpty(eAccrualBillVO)) {
                    eAccrualBillVOList.add(eAccrualBillVO);
                    String corpId = eAccrualBillVO.getNetId();
                    if (EmptyUtils.isNotEmpty(corpId) && EmptyUtils.isNotEmpty(billType)) {
                        String netId = corpService.getNetByCorpId(corpId).getId();
                        EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                        eVisaTypeVOMap.put(eAccrualBillVO.getId(), eVisaType);
                    }
                    // 获取操作日结的用户
                    UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(eAccrualBillVO.getNetId(), eAccrualBillVO.getTallyDate());
                    if (dayOverOperatorUser != null) {
                        eAccrualBillVO.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
                    }
                }
            }
            if (ReceiptConstant.RECEIPT_BILL_TYPE_01.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_02.equals(billType)
                    || ReceiptConstant.RECEIPT_BILL_TYPE_03.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_19.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_20.equals(billType)) {
                EAccrualBillVO eAccrualBill = eAccrualBillService.getVOByBillNo(null, id);
                if (!EmptyUtils.isEmpty(eAccrualBill)) {
                    String corpId = eAccrualBill.getNetId();
                    if (EmptyUtils.isNotEmpty(eAccrualBill.getBillNo())) {
                        List<FcInterestDtlDTO> dtlDTOList = eAccrualBillService.getFcInterestDtlDTOByOrderNo(eAccrualBill.getBillNo());
                        fcinterestDtlMap.put(eAccrualBill.getId(), dtlDTOList);
                        //本金
                        BigDecimal money = new BigDecimal(0);
                        //利息
                        BigDecimal nummoney = new BigDecimal(0);
                        for (FcInterestDtlDTO fcInterestDtlDTO : dtlDTOList) {
                            nummoney = nummoney.add(fcInterestDtlDTO.getInterest());
                        }
                        //贷款利息单重新计算本金
                        if (eAccrualBill.getBillType().equals(ReceiptConstant.RECEIPT_BILL_TYPE_02)) {
                            money = this.eAccrualBillService.getMoney(eAccrualBill.getAccNo(), eAccrualBill.getStartDate());
                            eAccrualBill.setMoney(money);
                        }
                        eAccrualBill.setNummoney(nummoney);
                    }
                    eAccrualBillVOList.add(eAccrualBill);
                    if (EmptyUtils.isNotEmpty(corpId) && EmptyUtils.isNotEmpty(billType)) {
                        String netId = corpService.getNetByCorpId(corpId).getId();
                        EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                        eVisaTypeVOMap.put(eAccrualBill.getId(), eVisaType);
                    }
                }
            }
            if (ReceiptConstant.RECEIPT_BILL_TYPE_07.equals(billType)) {
                ELoanBillVO eLoanBill = eLoanBillService.getVOByBillNo(null, id);
                String netId = eLoanBill.getNetId();
                eLoanBillVOList.add(eLoanBill);
                //设置还款金额
                eLoanBillService.setReturnMoney(eLoanBill);
                if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
                    EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                    eVisaTypeVOMap.put(eLoanBill.getId(), eVisaType);
                }
            }
            if (ReceiptConstant.RECEIPT_BILL_TYPE_08.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_09.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_10.equals(billType)) {
                ENoticeBillVO eNoticeBill = eNoticeBillService.getVOByBillNo(null, id);
                String netId = eNoticeBill.getNetId();
                // 获取操作日结的用户
                UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(eNoticeBill.getNetId(), eNoticeBill.getTallyDate());
                if (dayOverOperatorUser != null) {
                    eNoticeBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
                }
                eNoticeBillVOList.add(eNoticeBill);
                if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
                    EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                    eVisaTypeVOMap.put(eNoticeBill.getId(), eVisaType);
                }
            }
            if (ReceiptConstant.RECEIPT_BILL_TYPE_11.equals(billType)) {
                EPoundageBillVO ePoundageBill = ePoundageBillService.getVOByBillNo(null, id);
                String netId = ePoundageBill.getNetId();
                // 获取操作日结的用户
                UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(ePoundageBill.getNetId(), ePoundageBill.getTallyDate());
                if (dayOverOperatorUser != null) {
                    ePoundageBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
                }
                ePoundageBillVOList.add(ePoundageBill);
                if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
                    EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                    eVisaTypeVOMap.put(ePoundageBill.getId(), eVisaType);
                }
            }
            if (ReceiptConstant.RECEIPT_BILL_TYPE_12.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_13.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_14.equals(billType)
                    || ReceiptConstant.RECEIPT_BILL_TYPE_15.equals(billType)) {
                DataTablesInput input = new DataTablesInput();
                input.addFilter(MatchTypeEnum.EQ, "id", id);
                DataTablesOutput output = eTransBillService.datatables(input, this.getCurrentUser());
                List<ETransBillVO> eTransBillVOS = output.getData();
                ETransBillVO eTransBillVO = eTransBillVOS.get(0);
                eTransBillVOList.add(eTransBillVO);
                if (!EmptyUtils.isEmpty(eTransBillVO)) {
                    String corpId = eTransBillVO.getCorpId();
                    if (EmptyUtils.isNotEmpty(corpId) && EmptyUtils.isNotEmpty(billType)) {
                        String netId = corpService.getNetByCorpId(corpId).getId();
                        EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                        eVisaTypeVOMap.put(eTransBillVO.getId(), eVisaType);
                    }
                    // 获取操作日结的用户
                    UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(eTransBillVO.getNetId(), eTransBillVO.getTallyDate());
                    if (dayOverOperatorUser != null) {
                        eTransBillVO.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
                    }
                }
            }
            if (ReceiptConstant.RECEIPT_BILL_TYPE_16.equals(billType)) {
                EAcceptBillVO eAcceptBill = eAcceptBillService.getVOByBillNo(null, id);
                String netId = eAcceptBill.getNetId();
                // 获取操作日结的用户
                UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(eAcceptBill.getNetId(), eAcceptBill.getTallyDate());
                if (dayOverOperatorUser != null) {
                    eAcceptBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
                }
                eAcceptBillVOList.add(eAcceptBill);
                if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
                    EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                    eVisaTypeVOMap.put(eAcceptBill.getId(), eVisaType);
                }
            }
            if (ReceiptConstant.RECEIPT_BILL_TYPE_17.equals(billType)) {
                List<ECompareBillVO> list = eCompareBillService.getVOByBillNo(id);
                ECompareBillVO sumVO = eCompareBillService.getSumCompareBills(list.get(0).getBillNo());
                BeanUtils.copyNotNullProperties(sumVO, list.get(0));
                String netId = list.get(0).getNetId();
                Map<String, Object> map = new HashMap<>();
                map.put("list", list);
                map.put("eCompareBillVO", list.get(0));
                eCompareBillVOList.add(map);
                if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
                    EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                    eVisaTypeVOMap.put(id, eVisaType);
                }
            }
            if (ReceiptConstant.RECEIPT_BILL_TYPE_21.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_22.equals(billType)) {
                EPrecativeLoanBillVO ePrecativeLoanBill = ePrecativeLoanBillService.getVOByBillNo(null, id);
                String netId = ePrecativeLoanBill.getNetId();
                // 获取操作日结的用户
                UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(ePrecativeLoanBill.getNetId(), ePrecativeLoanBill.getTallyDate());
                if (dayOverOperatorUser != null) {
                    ePrecativeLoanBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
                }
                ePrecativeLoanBillVOList.add(ePrecativeLoanBill);
                if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
                    EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                    eVisaTypeVOMap.put(ePrecativeLoanBill.getId(), eVisaType);
                }
            }
        }
        EleBillBatchPrintVO result = new EleBillBatchPrintVO();
        result.setPrintType(printType);
        result.setEAccrualBillVOList(eAccrualBillVOList);
        result.setELoanBillVOList(eLoanBillVOList);
        result.setENoticeBillVOList(eNoticeBillVOList);
        result.setEPoundageBillVOList(ePoundageBillVOList);
        result.setETransBillVOList(eTransBillVOList);
        result.setEAcceptBillVOList(eAcceptBillVOList);
        result.setECompareBillVOList(eCompareBillVOList);
        result.setEPrecativeLoanBillVOList(ePrecativeLoanBillVOList);
        result.setEVisaTypeVOMap(eVisaTypeVOMap);
        result.setFcinterestDtlMap(fcinterestDtlMap);
        return success(result);
    }
}
