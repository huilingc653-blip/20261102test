package com.bytter.biz.finacial.receipt.service.impl;

import com.bytter.biz.finacial.api.service.IReceiptQueryService;
import com.bytter.biz.finacial.api.vo.EAccbookBlnedVo;
import com.bytter.biz.finacial.api.vo.ELoanBillInfoVo;
import com.bytter.biz.finacial.paramconfig.service.FcVoucherCashService;
import com.bytter.biz.finacial.receipt.dao.ReceiptQueryDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 签章类型维护
 * @date 2017-06-05 10:52:28
 */
@DubboService
@Service("receiptQueryServiceImpl")
@Transactional
public class ReceiptQueryServiceImpl implements IReceiptQueryService, Serializable {
    @Autowired
    private ReceiptQueryDao receiptQueryDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IFcSubjectService fcSubjectService;
    @Autowired
    private FcVoucherCashService fcVoucherCashService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 根据参数查询记账信息
     *
     * @param params
     * @return
     */
    public List<EAccbookBlnedVo> listAccbookBlneInfo(Map<String, Object> params) {
        return receiptQueryDao.listAccbookBlneInfo(params);
    }

    /**
     * 根据参数查询记账信息
     *
     * @param orderNo
     * @return
     */
    public EAccbookBlnedVo getEAccbookBlnedInfo(String orderNo) {
        return receiptQueryDao.getEAccbookBlnedInfo(orderNo);
    }

    /**
     * 获取结售汇非冲账数据数据
     *
     * @param params
     * @return
     */
    public List<EAccbookBlnedVo> listAccbookBlneExchangeInfo(Map<String, Object> params) {
        return receiptQueryDao.listAccbookBlneExchangeInfo(params);
    }

    /**
     * 获取结售汇冲账数据数据
     *
     * @param params
     * @return
     */
    public List<EAccbookBlnedVo> listAccbookBlneRushExchangeInfo(Map<String, Object> params) {
        return receiptQueryDao.listAccbookBlneRushExchangeInfo(params);
    }

    /**
     * 委托贷款利息清单数据数据
     *
     * @param params
     * @return
     */
    public List<EAccbookBlnedVo> lisInterestInfo(Map<String, Object> params) {
        return receiptQueryDao.lisInterestInfo(params);
    }

    /**
     * 已结流水信息
     *
     * @param params
     * @return
     */
    public List<EAccbookBlnedVo> listInterestOthersInfo(Map<String, Object> params) {
        List<FcAccDTO> fcAccList = iFcAccService.getFcAccByNetId(params.get("netId") + "");
        List<String> accList = fcAccList.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        String accSql = " and" + SqlUtil.getInSql(accList, "dtl.account", true);
        return receiptQueryDao.listInterestOthersInfo(accSql, params);
    }

    /**
     * 根据凭证流水和账号获取记账信息
     *
     * @param orderNo  凭证流水
     * @param accounts 内部账号
     * @return
     */
    public List<ELoanBillInfoVo> listAccbookBlnedAcc(String orderNo, String[] accounts) {
        List<ELoanBillInfoVo> list = receiptQueryDao.listAccbookBlnedAcc(orderNo, accounts);
        if(EmptyUtils.isNotEmpty(list)){
            Map<String, FcAccDTO> fcAccDTOMap = iFcAccService.listAllFcAcc();
            List<FcSubjectDTO>  fcSubjectList = fcSubjectService.listValidSubject();
            Map<String, FcSubjectDTO> fcSubjectMap = fcSubjectList.stream().collect(Collectors.toMap(FcSubjectDTO::getId, Function.identity()));
            for(ELoanBillInfoVo vo : list){
                if(null != fcAccDTOMap){
                    FcAccDTO fcAccDTO = fcAccDTOMap.get(vo.getAccNo());
                    if(null != fcAccDTO && null != fcSubjectMap){
                        FcSubjectDTO fcSubjectDTO = fcSubjectMap.get(fcAccDTO.getSubjectId());
                        if(null != fcSubjectDTO){
                            vo.setSysSubjectCode(fcSubjectDTO.getSysSubjectCode());
                        }
                    }
                }
            }

        }
        return list;
    }

    /**
     * 根据凭证流水和账号获取记账信息
     *
     * @param orderNo  凭证流水
     * @param accounts 内部账号
     * @return
     */
    public List<ELoanBillInfoVo> listAccbookBlnedAcc(String orderNo, String[] accounts, boolean debitMoneyNot0, boolean creditMoneyNot0) {
        return receiptQueryDao.listAccbookBlnedAcc(orderNo, accounts, debitMoneyNot0, creditMoneyNot0);
    }

    /**
     * 根据凭证流水和账号获取记账信息
     *
     * @param orderNo  凭证流水
     * @param accounts 内部账号
     * @return hashmap
     */
    public HashMap<String, List<ELoanBillInfoVo>> listAccbookBlnedAccToHashMap(List<String> orderNo, String[] accounts,
                                                                               boolean debitMoneyNot0, boolean creditMoneyNot0) {
        HashMap<String, List<ELoanBillInfoVo>> voMap = receiptQueryDao.listAccbookBlnedAccToHashMap(orderNo, accounts, debitMoneyNot0, creditMoneyNot0);
        return voMap;
    }

    public BigDecimal getCountCompareBalanceInvalid(Map<String, Object> params) {
        return receiptQueryDao.getCountCompareBalanceInvalid(params);
    }

    public List<ELoanBillInfoVo> getCompareAccBalance(Map<String, Object> params) {
        return receiptQueryDao.getCompareAccBalance(params);
    }
    public List<ELoanBillInfoVo> listFcAccbookBlnedSubjectByOrderNo(String orderNo){
        return receiptQueryDao.listFcAccbookBlnedSubjectByOrderNo(orderNo);
    }
    /**
     * 根据全局ID获取凭证明细信息和会计科目信息
     * @param busGlobalId
     * @return
     */
    public List<ELoanBillInfoVo> listFcAccbookBlnedSubjectBybusGlobalId(String busGlobalId){
        return  receiptQueryDao.listFcAccbookBlnedSubjectBybusGlobalId(busGlobalId);
    }
}
