package com.bytter.finance.domain.basedata.service.impl;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.vo.CmsLoanSendOutCheckVO;
import com.bytter.finance.domain.basedata.vo.CmsPrecativeLoanSendCheckVO;
import com.bytter.finance.domain.basedata.vo.CmsProvideLoanSendCheckVO;
import com.bytter.finance.domain.loan.entity.CmsLoanRepay;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.service.CmsLoanInfoService;
import com.bytter.finance.domain.loan.service.CmsLoanRepayService;
import com.bytter.finance.domain.loan.service.CmsLoanSendService;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanInfo;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanRepay;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanSend;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanDeferredService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanRepayService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanSendService;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanRepayVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import com.bytter.finance.domain.provide.dao.CmsProvideLoanRepayDao;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanRepay;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.service.CmsProvideLoanRepayService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.LoanRepayVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.finance.dto.*;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("cmsLoanServiceImpl")
@Transactional
@DubboService
public class CmsLoanServiceImpl implements ICmsLoanService {
    @Autowired
    private CmsPrecativeLoanDeferredService cmsPrecativeLoanDeferredService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @Autowired
    private CmsPrecativeLoanRepayService cmsPrecativeLoanRepayService;
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsLoanRepayService cmsLoanRepayService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private CmsLoanInfoService cmsLoanInfoService;
    @Autowired
    private LoanInfoService loaninfoservice;
    @Autowired
    private CmsProvideLoanRepayService cmsProvideLoanRepayService;
    @Autowired
    private CmsProvideLoanRepayDao cmsProvideLoanRepayDao;
    @DubboReference(check = false)
    private IDictService dictService;

    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IDictService iDictService;

    /**
     * 根据billCode获取内部贷款info表数据
     */
    @Override
    public CmsLoanInfoDTO getLoanInfoBybillCode(String billCode) {
        return cmsLoanInfoService.getLoanInfoBybillCode(billCode);
    }
    /**
     * 获取委托贷款展期申请
     */
    @Override
    public List<CmsPrecativeLoanDeferredDTO> getCmsPrecativeDeferreds() {
        return cmsPrecativeLoanDeferredService.getCmsPrecativeDeferreds();
    }
    /**
     * 根据billCode获取委托贷款发放申请
     */
    @Override
    public CmsPrecativeLoanSendDTO getCmsPrecativeLoanSendByBillCode(String billCode) {
        CmsPrecativeLoanSendDTO send = new CmsPrecativeLoanSendDTO();
        PrecativeLoanSendVo loanSend = cmsPrecativeLoanSendService.getSendByBillCode(billCode);
        if (!EmptyUtils.isEmpty(loanSend)) {
            BeanUtils.copyProperties(loanSend, send);
        }
        return send;
    }
    @Override
    public List<CmsLoanSendOutCheckDTO> getLoanSendOutInfoByEndDate(FcDayCloseDTO dayClose) {
        List<CmsLoanSendOutCheckVO> sentVO = cmsLoanSendService.getLoanSendOutInfoByEndDate(dayClose.getNetCode(),
                dayClose.getCloseDate());
        if (!EmptyUtils.isEmptyList(sentVO)) {
            List<CmsLoanSendOutCheckDTO> sentDTO = BeanUtils.copyListProperties(sentVO, CmsLoanSendOutCheckDTO.class);
            return sentDTO;
        }
        return null;
    }
    @Override
    public List<CmsProvideLoanSendCheckDTO> getBankLoadSendInfoByEndDate(FcDayCloseDTO dayClose) {
        Date date = dayClose.getCloseDate();
        List<CmsProvideLoanSendCheckVO> listVo = cmsProvideLoanSendService
                .getBankLoadSendInfoByEndDate(dayClose.getNetCode(), date);
        if (!EmptyUtils.isEmptyList(listVo)) {
            List<CmsProvideLoanSendCheckDTO> sendDTO = BeanUtils.copyListProperties(listVo,
                    CmsProvideLoanSendCheckDTO.class);
            return sendDTO;
        }
        return null;
    }
    @Override
    public List<CmsPrecativeLoanSendCheckDTO> getPrecativeLoanInfoByEndDate(FcDayCloseDTO dayClose) {
        List<CmsPrecativeLoanSendCheckVO> listVo = cmsPrecativeLoanSendService
                .getPrecativeLoanInfoByEndDate(dayClose.getNetCode(), dayClose.getCloseDate());
        if (!EmptyUtils.isEmptyList(listVo)) {
            List<CmsPrecativeLoanSendCheckDTO> sendDTO = BeanUtils.copyListProperties(listVo,
                    CmsPrecativeLoanSendCheckDTO.class);
            return sendDTO;
        }
        return null;
    }
    @Override
    public BigDecimal getPrecativeLoanRepayMoney(String voucherNo) {
        return cmsPrecativeLoanRepayService.getPrecativeLoanRepayMoney(voucherNo);
    }
    @Override
    public BigDecimal getPrecativeLoanRepayMoneySum(String infoBillCode) {
        return cmsPrecativeLoanRepayService.getPrecativeLoanRepayMoneySum(infoBillCode);
    }
    /**
     * 查询委托贷款记账列表
     */
    @Override
    public DataTablesOutput getDateTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        /*String menuType = (String) dataTablesInput.getFormdata().get("menuType");
        if (FinacialManualConstant.MANUAL_TYPE_PRECATIVE_SEND.equals(menuType)) {
            DataTablesOutput dataTablesOutput=cmsPrecativeLoanSendService.getPrecativeDataTables(dataTablesInput, user);
            List data = dataTablesOutput.getData();
            ArrayList<PrecativeLoanSendVo> precativeLoanSendVos = new ArrayList<>();
            for (Object datum : data) {
                PrecativeLoanSendVo vo = (PrecativeLoanSendVo)datum;
                String ternUnitStatus=dictService.getByName("PrecativeLoanInfo.ternUnit",vo.getTernUnit());
                vo.setTernUnitStatus(ternUnitStatus);
                precativeLoanSendVos.add(vo);
            }
            dataTablesOutput.setData(precativeLoanSendVos);
            return dataTablesOutput;
        } else {
            DataTablesOutput dataTablesOutput= cmsPrecativeLoanRepayService.getPrecativeRepayDataTables(dataTablesInput, user);
            return dataTablesOutput;
        }*/
        String menuType = (String)dataTablesInput.getFormdata().get("menuType");
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        List<String> corpAuth = null;
        if (FinacialManualConstant.MANUAL_STATUS_CHECK.equals(opType)){
            corpAuth = iCorpService.getUserCorpIds(ResourceConstant.FCL_PRECATIVE_TALLY_CHECK, user.getId());
        }else{
            corpAuth = iCorpService.getUserCorpIds(ResourceConstant.FCL_PRECATIVE_TALLY_APPLY, user.getId());
        }
        if(FinacialManualConstant.MANUAL_TYPE_PRECATIVE_SEND.equals(menuType)){
            DataTablesOutput output = cmsPrecativeLoanSendService.getPrecativeDataTables(dataTablesInput, user, corpAuth);
            List<PrecativeLoanSendVo> data = output.getData();
            Set<String> collect = data.stream().map(PrecativeLoanSendVo::getTernUnit).collect(Collectors.toSet());
            //查询字典
            //@Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitStatus")
            if (!collect.isEmpty()){
                Map<String, String> map = iDictService.getNameByTypeCodesAndTypeGroupCode(new ArrayList<>(collect), "PrecativeLoanInfo.ternUnit");
                data.forEach(a ->{
                    a.setTernUnitStatus(map.get(a.getTernUnit()));
                });
            }

            return output;
        }else{
            return cmsPrecativeLoanRepayService.getPrecativeRepayDataTables(dataTablesInput,user,corpAuth);
        }
    }
    @Override
    public CmsPrecativeLoanInfoDTO getCmsPrecativeLoanInfoByBillCode(String billCode) {
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(billCode);
        CmsPrecativeLoanInfoDTO dto = new CmsPrecativeLoanInfoDTO();
        BeanUtils.copyNotNullProperties(info, dto);
        return dto;
    }
    @Override
    public void updatePrecative(CmsPrecativeLoanSendDTO send, ICurrentUser user) {
        // 记账和打回都是走这个 update
        CmsPrecativeLoanSend entity = new CmsPrecativeLoanSend();
        BeanUtils.copyNotNullProperties(send, entity);
        entity.setUpdateBy(user.getId());
        entity.setUpdateDate(DateUtils.getDate());
        cmsPrecativeLoanSendService.update(entity);
    }
    @Override
    public void updatePrecativeRepay(CmsPrecativeLoanRepayDTO send, ICurrentUser user) {
        CmsPrecativeLoanRepay entity = new CmsPrecativeLoanRepay();
        BeanUtils.copyNotNullProperties(send, entity);
        // entity.setUpdateBy(user.getId());
        // entity.setUpdateDate(DateUtils.getDate());
        if (CmsConstant.BUSINESSSTATE_48.equals(entity.getBizStatus())) { // 是否还款完成
            CmsPrecativeLoanSend sendInfo = cmsPrecativeLoanSendService.getByBillCode(entity.getSendCode());
            CmsPrecativeLoanInfoVo loanInfo = cmsPrecativeLoanInfoService
                    .getLoanInfoByBillCode(sendInfo.getFatherCode());
            BigDecimal repaySum = cmsPrecativeLoanRepayService.getPrecativeLoanRepayMoneySum(loanInfo.getBillCode());
            BigDecimal sendBalance = cmsPrecativeLoanRepayService.completeSendBalance(entity.getSendCode());
            if (sendBalance.compareTo(entity.getRepayMoney()) == 0) { // 贷款发放的额度全部还款完成
                sendInfo.setBizStatus(CmsConstant.BUSINESSSTATE_48);
                sendInfo.setStatus(CmsConstant.FINISH);
                cmsPrecativeLoanSendService.saveOrUpdate(sendInfo);
            }
            if (loanInfo.getMoney().compareTo(repaySum.add(entity.getRepayMoney())) == 0) { // 还款全部完成业务结束
                loanInfo.setStatus(CmsConstant.FINISH);
                loanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_110);
                CmsPrecativeLoanInfo infoTemp = new CmsPrecativeLoanInfo();
                BeanUtils.copyNotNullProperties(loanInfo, infoTemp);
                cmsPrecativeLoanInfoService.update(infoTemp);
            }
        }
        cmsPrecativeLoanRepayService.update(entity);
    }
    /*
     * @Override public DataTablesOutput getPrecativeRepayDataTables(DataTablesInput dataTablesInput, ICurrentUser user)
     * { return cmsPrecativeLoanRepayService.getPrecativeRepayDataTables(dataTablesInput,user); }
     */
    @Override
    public CmsPrecativeLoanRepayDTO getCmsPrecativeLoanRepayByBillCode(String billCode) {
        CmsPrecativeLoanRepayVo repayVo = cmsPrecativeLoanRepayService.getReapyByBillCode(billCode);
        CmsPrecativeLoanRepayDTO dto = new CmsPrecativeLoanRepayDTO();
        BeanUtils.copyNotNullProperties(repayVo, dto);
        return dto;
    }
    @Override
    public CmsLoanSendDTO getLoanSendByBillCode(String billCode) {
        CmsLoanSendDTO send = new CmsLoanSendDTO();
        BeanUtils.copyProperties(cmsLoanSendService.getCmsLoanSendVOByBillCode(billCode), send);
        return send;
    }
    @Override
    public void updateCmsLoanSend(CmsLoanSendDTO send) {
        CmsLoanSend sendInfo = cmsLoanSendService.getLoanSendByBillCode(send.getBillCode());
        BeanUtils.copyNotNullProperties(send, sendInfo);
        cmsLoanSendService.update(sendInfo);
    }
    @Override
    public CmsLoanRepayDTO isFinishedRepay(CmsLoanRepayDTO repay) {
        CmsLoanRepay cmsRepay = new CmsLoanRepay();
        BeanUtils.copyProperties(repay, cmsRepay);
        cmsLoanRepayService.isFinishedRepay(cmsRepay);
        CmsLoanRepayDTO cmsLoanRepayDTO = new CmsLoanRepayDTO();
        BeanUtils.copyProperties(cmsRepay, cmsLoanRepayDTO);
        return  cmsLoanRepayDTO;
    }
    /**
     * 查询银行借款记账列表
     */
    @Override
    public DataTablesOutput getProvideDateTable(DataTablesInput dataTablesInput, ICurrentUser user, String onlineSign,
                                                String confirm) {
        String menuType = (String) dataTablesInput.getFormdata().get("menuType");
        if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_SEND.equals(menuType)) {
            return cmsProvideLoanSendService.getProvideDataTables(dataTablesInput, user);
        } else {
            return cmsProvideLoanSendService.getProvideRepayDataTables(dataTablesInput, user, onlineSign, confirm);
        }
    }
    @Override
    public CmsProvideLoanInfoDTO getLoanInfoVOByBillCode(String billCode) {
        LoanInfoVO info = loaninfoservice.getLoanInfoVOBySendBillCode(billCode);
        CmsProvideLoanInfoDTO dto = new CmsProvideLoanInfoDTO();
        BeanUtils.copyNotNullProperties(info, dto);
        return dto;
    }
    @Override
    public CmsProvideSendDTO getCmsProvideLoanSendByBillCode(String billCode) {
        LoanSendVO send = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(billCode);
        CmsProvideSendDTO dto = new CmsProvideSendDTO();
        BeanUtils.copyNotNullProperties(send, dto);
        return dto;
    }
    @Override
    public CmsProvideLoanRepayDTO getCmsProvideLoanRepayByBillCode(String billCode, String onlineSign) {
        LoanRepayVO repay = cmsProvideLoanRepayService.queryRepayVOByBillCode(billCode, onlineSign);
        CmsProvideLoanRepayDTO repayDTO = new CmsProvideLoanRepayDTO();
        BeanUtils.copyNotNullProperties(repay, repayDTO);
        return repayDTO;
    }
    @Override
    public void updateProvideRepay(CmsProvideLoanRepayDTO repay, ICurrentUser user) {
        CmsProvideLoanRepay entity = new CmsProvideLoanRepay();
        BeanUtils.copyNotNullProperties(repay, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        cmsProvideLoanRepayService.updateRepay(entity);
    }
    @Override
    public void updateProvide(CmsProvideSendDTO send, ICurrentUser user) {
        ProvideLoanSend entity = new ProvideLoanSend();
        BeanUtils.copyNotNullProperties(send, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        cmsProvideLoanSendService.updateSend(entity);
        // 记账发放全部发放完成，修改info表 记账完成
        cmsProvideLoanSendService.sendComplete(entity.getLoanCode());// 发放完处理 TODO
    }
    /**
     * 获取银行借款还款金额
     **/
    @Override
    public BigDecimal getInfoRepayMoneySum(String infoBillCode) {
        return cmsProvideLoanRepayDao.getInfoRepayMoneySum(infoBillCode);
    }
    /**
     * 获取内部贷款还款金额
     **/
    @Override
    public BigDecimal getSendRepayedMoney(String sendCode) {
        return cmsLoanRepayService.getSendRepayedMoney(sendCode);
    }
    /**
     * 获取内部贷款还款完成的金额
     **/
    @Override
    public boolean checkFinishedRepay(CmsLoanRepayDTO repay) {
        CmsLoanRepay loanRepay = new CmsLoanRepay();
        BeanUtils.copyProperties(repay, loanRepay);
        return cmsLoanRepayService.isRepayOver(loanRepay);
    }
    @Override
    public CmsLoanRepayDTO getLoanRepayById(String id) {
        return cmsLoanRepayService.getLoanRepayById(id);
    }
    @Override
    public CmsLoanRepayDTO getCmsLoanRepayByBillCode(String billCode) {
        return cmsLoanRepayService.getCmsLoanRepayByBillCode(billCode);
    }
    @Override
    public List<CmsLoanSendDTO> querySendTallyData() {
        return cmsLoanSendService.querySendTallyData();
    }
    @Override
    public List<CmsLoanRepayDTO> queryRepayTallyData() {
        return cmsLoanRepayService.queryRepayTallyData();
    }
    @Override
    public void occupyGuaContract(String id) {
        cmsLoanInfoService.updateGuaranteeConfig(id, CmsConstant.IS_TRUE);
    }
    @Override
    public void cancelGuaContract(String id) {
        cmsLoanInfoService.updateGuaranteeConfig(id, CmsConstant.IS_FALSE);
    }
    @Override
    public void relateBankFlow(String id) {
        cmsProvideLoanSendService.relateBankFlow(id);
    }
    @Override
    public void cancelBankFlow(String id) {
        cmsProvideLoanSendService.cancelBankFlow(id);
    }
    @Override
    public void provideIsRepayComplete(String repayId, ICurrentUser user) {
        cmsProvideLoanRepayService.isRepayComplete(repayId, user);
    }
    @Override
    public CmsProvideSendDTO getCmsProvideLoanSendById(String id) {
        CmsProvideSendDTO sendDTO = new CmsProvideSendDTO();
        if (EmptyUtils.isNotEmpty(id)) {
            ProvideLoanSend provideLoanSend = cmsProvideLoanSendService.get(id);
            sendDTO = this.getCmsProvideLoanSendByBillCode(provideLoanSend.getBillCode());
        }
        return sendDTO;
    }
    @Override
    public List<CmsPrecativeLoanSendDTO> queryTallyPrecativeSend() {
        return cmsPrecativeLoanSendService.queryTallyPrecativeSend();
    }
    @Override
    public List<CmsPrecativeLoanRepayDTO> queryTallyPrecativeRepay() {
        return cmsPrecativeLoanRepayService.queryTallyPrecativeRepay();
    }
    @Override
    public List<CmsProvideSendDTO> queryTallyProvideSend() {
        return cmsProvideLoanSendService.queryTallyProvideSend();
    }
    @Override
    public List<CmsProvideLoanRepayDTO> queryTallyProvideRepay(String onlineSign) {
        return cmsProvideLoanSendService.queryTallyProvideRepay(onlineSign);
    }
    /**
     * <br>
     * <b>功能：</b> 查询贷款合同金额及还款金额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/3/12 0012 <br>
     */
    @Override
    public List<CmsLoanRepayMoneyDTO> queryLoanRepayMoneyByLoanAcc(String loanAcc, Date endDate) {
        return cmsLoanRepayService.queryLoanRepayMoneyByLoanAcc(loanAcc, endDate);
    }
    @Override
    public List<GuaContractCmsLoanDTO> getHangGuaForLoan() {
        return cmsLoanInfoService.getHangGuaForLoan();
    }
    @Override
    public void batchUpdateSend(List<CmsLoanSendDTO> dataList) {
        cmsLoanSendService.batchSaveOrUpdate(BeanUtils.copyListProperties(dataList, CmsLoanSend.class));
    }
    @Override
    public CmsLoanRepayDTO getRepayUniqueBy(String origVoucherNo) {
        return cmsLoanRepayService.getRepayUniqueBy(origVoucherNo);
    }
    @Override
    public void updateRepayEntity(CmsLoanRepayDTO repay) {
        CmsLoanRepay bean = new CmsLoanRepay();
        BeanUtils.copyNotNullProperties(repay, bean);
        cmsLoanRepayService.saveOrUpdate(bean);
    }
    @Override
    public void batchUpdateRepay(List<CmsLoanRepayDTO> dataList) {
        cmsLoanRepayService.batchSaveOrUpdate(BeanUtils.copyListProperties(dataList, CmsLoanRepay.class));
    }
}
