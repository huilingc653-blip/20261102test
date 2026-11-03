package com.bytter.biz.receipt.vo;

import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 电子回单批量打印返回VO
 * @author Generator
 * @date 2026-05-15
 */
@ApiModel("电子回单批量打印返回VO")
public class EleBillBatchPrintVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 打印类型
     */
    private String printType;

    /**
     * 利息电子回单列表
     * 01-存款利息单 02-贷款利息单 03-委托贷款利息清单 
     * 04-定期利息单 05-通知利息单 19-活期保证金利息单 20-定期保证金利息单
     */
    private List<EAccrualBillVO> eAccrualBillVOList;

    /**
     * 贷款通知单列表
     * 07-贷款通知单
     */
    private List<ELoanBillVO> eLoanBillVOList;

    /**
     * 通知单列表
     * 08-付款通知单 09-收款通知单 10-内转通知单
     */
    private List<ENoticeBillVO> eNoticeBillVOList;

    /**
     * 手续费通知单列表
     * 11-手续费通知单
     */
    private List<EPoundageBillVO> ePoundageBillVOList;

    /**
     * 存单列表
     * 12-通知存单 13-定期存单 14-定期取款单 15-通知取款单
     */
    private List<ETransBillVO> eTransBillVOList;

    /**
     * 承兑汇票电子回单列表
     * 16-承兑汇票电子回单
     */
    private List<EAcceptBillVO> eAcceptBillVOList;

    /**
     * 对账单电子回单列表
     * 17-对账单电子回单
     * Map中包含: list(明细列表), eCompareBillVO(汇总信息)
     */
    private List<Map<String, Object>> eCompareBillVOList;

    /**
     * 委托贷款通知单列表
     * 21-委托贷款通知单 22-委托贷款还款通知单
     */
    private List<EPrecativeLoanBillVO> ePrecativeLoanBillVOList;

    /**
     * 签章信息Map
     * key: 单据ID, value: 签章信息
     */
    private Map<String, EVisaTypeVO> eVisaTypeVOMap;

    /**
     * 利息明细Map
     * key: 单据ID, value: 利息明细列表
     */
    private Map<String, List<FcInterestDtlDTO>> fcinterestDtlMap;

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public List<EAccrualBillVO> getEAccrualBillVOList() {
        return eAccrualBillVOList;
    }

    public void setEAccrualBillVOList(List<EAccrualBillVO> eAccrualBillVOList) {
        this.eAccrualBillVOList = eAccrualBillVOList;
    }

    public List<ELoanBillVO> getELoanBillVOList() {
        return eLoanBillVOList;
    }

    public void setELoanBillVOList(List<ELoanBillVO> eLoanBillVOList) {
        this.eLoanBillVOList = eLoanBillVOList;
    }

    public List<ENoticeBillVO> getENoticeBillVOList() {
        return eNoticeBillVOList;
    }

    public void setENoticeBillVOList(List<ENoticeBillVO> eNoticeBillVOList) {
        this.eNoticeBillVOList = eNoticeBillVOList;
    }

    public List<EPoundageBillVO> getEPoundageBillVOList() {
        return ePoundageBillVOList;
    }

    public void setEPoundageBillVOList(List<EPoundageBillVO> ePoundageBillVOList) {
        this.ePoundageBillVOList = ePoundageBillVOList;
    }

    public List<ETransBillVO> getETransBillVOList() {
        return eTransBillVOList;
    }

    public void setETransBillVOList(List<ETransBillVO> eTransBillVOList) {
        this.eTransBillVOList = eTransBillVOList;
    }

    public List<EAcceptBillVO> getEAcceptBillVOList() {
        return eAcceptBillVOList;
    }

    public void setEAcceptBillVOList(List<EAcceptBillVO> eAcceptBillVOList) {
        this.eAcceptBillVOList = eAcceptBillVOList;
    }

    public List<Map<String, Object>> getECompareBillVOList() {
        return eCompareBillVOList;
    }

    public void setECompareBillVOList(List<Map<String, Object>> eCompareBillVOList) {
        this.eCompareBillVOList = eCompareBillVOList;
    }

    public List<EPrecativeLoanBillVO> getEPrecativeLoanBillVOList() {
        return ePrecativeLoanBillVOList;
    }

    public void setEPrecativeLoanBillVOList(List<EPrecativeLoanBillVO> ePrecativeLoanBillVOList) {
        this.ePrecativeLoanBillVOList = ePrecativeLoanBillVOList;
    }

    public Map<String, EVisaTypeVO> getEVisaTypeVOMap() {
        return eVisaTypeVOMap;
    }

    public void setEVisaTypeVOMap(Map<String, EVisaTypeVO> eVisaTypeVOMap) {
        this.eVisaTypeVOMap = eVisaTypeVOMap;
    }

    public Map<String, List<FcInterestDtlDTO>> getFcinterestDtlMap() {
        return fcinterestDtlMap;
    }

    public void setFcinterestDtlMap(Map<String, List<FcInterestDtlDTO>> fcinterestDtlMap) {
        this.fcinterestDtlMap = fcinterestDtlMap;
    }
}
