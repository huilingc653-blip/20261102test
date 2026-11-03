package com.bytter.biz.dataexchange.basic.service.impl;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.dataexchange.basic.dao.ErpPayInfoDao;
import com.bytter.biz.dataexchange.basic.entity.ErpPayInfo;
import com.bytter.biz.dataexchange.basic.vo.ErpPayInfoVO;
import com.bytter.biz.dataexchange.basic.vo.ErpWageDetailVO;
import com.bytter.biz.payment.api.dto.*;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.biz.payment.api.service.INisExpenseDetailService;
import com.bytter.biz.payment.api.service.INisWageService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qijh
 */
@Service("erpPayInfoService")
@Transactional
public class ErpPayInfoService extends BaseService<ErpPayInfo,String> implements ITaskService {
    @Autowired
    private ErpPayInfoDao erpPayInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
//    @Autowired
//    private BtInputBankInfoService btInputBankInfoService;
    @DubboReference(check = false)
    private INisWageService nisWageService;
    @DubboReference(check = false)
    private INisExpenseDetailService nisExpenseDetailService;
    @Autowired
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Override
    public BaseDao<ErpPayInfo, String> getEntityDao() {
        return erpPayInfoDao;
    }
    /**
     * 查询列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        List<CorpDTO> corpDTOList=corpService.listUserCorp(ResourceConstant.ERP_PAYINFO_APPLY, user.getId());
        List<String> corpS=corpDTOList.stream().map(CorpDTO::getCode).collect(Collectors.toList());;
        String corpAuthSql = SqlUtil.getInSql(corpS, "e.corp_code", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = this.erpPayInfoDao.getDataTablesUnion(dataTablesInput, sql.toString());
        List<ErpPayInfoVO> voList=output.getData();
        if(voList.size()>0) {
            List<ErpPayInfoVO> erpVoList=output.getData();
            //查询单位
            Map<String, CorpDTO> corpMap = corpService.listCorpCodeByCorpIds(corpService.getUserCorpIds(ResourceConstant.ERP_PAYINFO_APPLY, user.getId()));
            //查询付款方式
            String eqs_voucherType = (String) dataTablesInput.getFormdata().get("eqs_voucherType");
            Map<String, FcVoucherTypeDTO> fcVoucherMap = fcVoucherTypeService.getFcVoucherTypeMap();
            Map<String, FcVoucherTypeDTO> fcVoucherTypeMap = fcVoucherTypeService.getFcVoucherTypeTypeMap();
            for (ErpPayInfoVO vo : voList) {
                if (StringUtils.isNotEmpty(eqs_voucherType)) {
                    FcVoucherTypeDTO voucherTypeDTO = fcVoucherMap.get(eqs_voucherType);
                    if (vo.getVoucherType().equals(voucherTypeDTO.getVoucherType())) {
                        vo.setVoucherName(voucherTypeDTO.getVoucherName());
                        vo.setCorpName(corpMap.get(vo.getCorpCode()).getName());
                        erpVoList.add(vo);
                    }
                } else {
                    FcVoucherTypeDTO voucherTypeDTO = fcVoucherTypeMap.get(vo.getVoucherType());
                    vo.setVoucherName(voucherTypeDTO.getVoucherName());
                    vo.setCorpName(corpMap.get(vo.getCorpCode()).getName());
                    erpVoList.add(vo);
                }
            }
            output.setData(erpVoList);
        }
        return output;
    }
    /**
     * 数据转移
     * @param id
     * @param user
     */
    public void transfer(String id, ICurrentUser user){
        if(StringUtils.isNotEmpty(id)){
            ErpPayInfo erpPayInfo = this.erpPayInfoDao.get(id, true);
            //未转移的才可以做转移
            if (erpPayInfo != null && !NetbankConstant.STATUE_TEMP.toString().equals(erpPayInfo.getStatus())) {
                throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, erpPayInfo.getVoucherNoErp());
            }
            //校验是否存在重复转移的erp数据
            if (erpPayInfo != null && !validSerialNoErp(erpPayInfo.getSerialNoErp())) {
                throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_SERIALNOERP_REPEAT, erpPayInfo.getVoucherNoErp());
            }
            if(NetbankConstant.STATUE_TEMP.toString().equals(erpPayInfo.getStatus())) {
                int result = this.erpPayInfoDao.updateStatus(erpPayInfo.getId());
                if(result > 0) {
                    if (NetbankConstant.PAY_TYPE_48.equals(erpPayInfo.getVoucherType())) {
                        this.toSaveNisWage(erpPayInfo, user);
                    } else {
                        this.toSaveNisBillhead(erpPayInfo, user);
                    }
                }
            }
        }
    }
    /**
     * 校验是否存在多条重复的ERP单据
     * @param serialNoErp
     * @return
     */
    private boolean validSerialNoErp(String serialNoErp) {
        List<ErpPayInfo> erpPayInfos = this.erpPayInfoDao.listBy("serialNoErp", serialNoErp);
        if(EmptyUtils.isNotEmpty(erpPayInfos) && erpPayInfos.size()>1){
            return false;
        }
        return true;
    }
    /**
     * 保存付款单
     * @param erpPayInfo
     * @param user
     */
    private void toSaveNisBillhead(ErpPayInfo erpPayInfo, ICurrentUser user){
        NisBillheadDTO nisBillheadDTO = new NisBillheadDTO();
        nisBillheadDTO.setErpId(erpPayInfo.getId());
        nisBillheadDTO.setStatus(NetbankConstant.STATUE_TEMP);
        nisBillheadDTO.setCorpId(corpService.getCorpByCode(erpPayInfo.getCorpCode()).getId());
        nisBillheadDTO.setPayeeAccName(erpPayInfo.getOppAccName());
        nisBillheadDTO.setPayeeAcc(erpPayInfo.getOppBankAcc());
        nisBillheadDTO.setPayeeBankCode(erpPayInfo.getPayeeCode());
        nisBillheadDTO.setPayeeBank(erpPayInfo.getOppBankName());
        if(StringUtils.isNotEmpty(erpPayInfo.getCurCode())){
            String currencyId= btCurrencyService.getCurrencyByCode(erpPayInfo.getCurCode()).getId();
            nisBillheadDTO.setCurId(currencyId);
        }
        nisBillheadDTO.setPayerAcc(erpPayInfo.getPayerAccNo());
        nisBillheadDTO.setPayerBank(erpPayInfo.getPayerBankName());
        nisBillheadDTO.setPayerAccName(erpPayInfo.getPayerAccName());
        FcVoucherTypeDTO fcVoucherTypeDTO = fcVoucherTypeService.getFcVoucherTypeByCode(erpPayInfo.getVoucherType());
        if(StringUtils.isNotEmpty(fcVoucherTypeDTO)) {
            nisBillheadDTO.setVoucherTypeId(fcVoucherTypeDTO.getId());
        }
        nisBillheadDTO.setBillFrom(NetbankConstant.BILLFROM_6);
        nisBillheadDTO.setDifBank(erpPayInfo.getDifBank());
        nisBillheadDTO.setAreaSign(erpPayInfo.getAreaSign());
        nisBillheadDTO.setCity(erpPayInfo.getOppCity());
        nisBillheadDTO.setProv(erpPayInfo.getOppProv());
        nisBillheadDTO.setIsForindividual(erpPayInfo.getIsforindividual());
        nisBillheadDTO.setIsPayed(erpPayInfo.getIsPayed());
        nisBillheadDTO.setPayMoney(erpPayInfo.getAmt());
        nisBillheadDTO.setIsQuick(erpPayInfo.getUrgencyFlag());
        //期望支付日期为空，则默认期望支付日期为系统当前日期
        if(erpPayInfo.getWishPayday() == null){
            nisBillheadDTO.setWishPayday(DateUtils.getDateYMD());
        }else {
            nisBillheadDTO.setWishPayday(erpPayInfo.getWishPayday());
        }
        nisBillheadDTO.setDirectPayFlag("y");
        nisBillheadDTO.setPurpose("付款");
        BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccInfo(erpPayInfo.getPayerAccNo());
        if(StringUtils.isNotEmpty(btBankAccDTO)){
            if(StringUtils.isBlank(nisBillheadDTO.getCurId())) {
                List<String> curList = btBankAccDTO.getCurList();
                if (EmptyUtils.isNotEmpty(curList) && curList.size() > 0) {
                    nisBillheadDTO.setCurId(curList.get(0));
                }
            }
            nisBillheadDTO.setBankTypeId(btBankAccDTO.getBankTypeId());
            nisBillheadDTO.setBifCode(btBankAccDTO.getBifCode());
        }else{
            if(StringUtils.isBlank(nisBillheadDTO.getCurId())) {
                BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();
                nisBillheadDTO.setCurId(natualCurrency.getId());
            }
        }
//        BtInputBankInfoVO btInputBankInfo = btInputBankInfoService.getByBankCode(erpPayInfo.getPayeeCode(),"");
//        if(StringUtils.isNotEmpty(btInputBankInfo)){
//            nisBillheadDTO.setCity(btInputBankInfo.getCity());
//            nisBillheadDTO.setProv(btInputBankInfo.getProv());
//            nisBillheadDTO.setPayeeBankCode(btInputBankInfo.getBankCode());
//            nisBillheadDTO.setPayeeBank(btInputBankInfo.getBankName());
//        }else {
//            btInputBankInfo = btInputBankInfoService.getByBankCode("",erpPayInfo.getOppBankName());
//            if(StringUtils.isNotEmpty(btInputBankInfo)){
//                nisBillheadDTO.setCity(btInputBankInfo.getCity());
//                nisBillheadDTO.setProv(btInputBankInfo.getProv());
//                nisBillheadDTO.setPayeeBankCode(btInputBankInfo.getBankCode());
//            }
//        }
        nisBillheadDTO.setAddWord(erpPayInfo.getAbs());
        //会计凭证号
        nisBillheadDTO.setBursarBillNo(erpPayInfo.getVoucherNoErp());
        //票据类型
        nisBillheadDTO.setBillType(erpPayInfo.getBillType());
        //票据流通介质
        nisBillheadDTO.setBillFlag(erpPayInfo.getBillFlag());
        nisBillheadService.saveNisBillhead(nisBillheadDTO,user);
    }
    /**
     * 保存代发工资单
     * @param erpPayInfo
     * @param user
     */
    private void toSaveNisWage(ErpPayInfo erpPayInfo, ICurrentUser user){
        List<ErpWageDetailVO> erpWageDetailVOList = this.erpPayInfoDao.getErpWageDetailBySerialNo(erpPayInfo.getSerialNoErp());
        List<NisWageDetailDTO> nisWageDetailDTOList = new ArrayList<>();
        NisWageDTO nisWageDTO = new NisWageDTO();
        nisWageDTO.setErpId(erpPayInfo.getId());
        nisWageDTO.setStatus(NetbankConstant.STATUE_TEMP);
        nisWageDTO.setCorpId(corpService.getCorpByCode(erpPayInfo.getCorpCode()).getId());
        nisWageDTO.setPayerAcc(erpPayInfo.getPayerAccNo());
        nisWageDTO.setPayerBank(erpPayInfo.getPayerBankName());
        nisWageDTO.setPayerAccName(erpPayInfo.getPayerAccName());
        nisWageDTO.setWages(erpPayInfo.getAmt());
        nisWageDTO.setWishPayday(erpPayInfo.getWishPayday());
        nisWageDTO.setVoucherStat(BdlinkConstant.PRE_SEND);
        BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccInfo(erpPayInfo.getPayerAccNo());
        String payerBankCode = "";
        if(StringUtils.isNotEmpty(btBankAccDTO)){
            List<String> curList = btBankAccDTO.getCurList();
            if(EmptyUtils.isNotEmpty(curList) && curList.size() >0) {
                nisWageDTO.setCurId(curList.get(0));
            }
            nisWageDTO.setBifCode(btBankAccDTO.getBifCode());
            nisWageDTO.setPayerBankCode(btBankAccDTO.getBankCode());
            payerBankCode = btBankAccDTO.getBankCode();
        }
        if(nisWageService.isCheckWageTitle(nisWageDTO.getCorpId(), erpPayInfo.getWageTitle())){
            throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_WAGETITLE_UNIQUE);
        }
        nisWageDTO.setWageTitle(erpPayInfo.getWageTitle());
        nisWageDTO.setWageDay(erpPayInfo.getWageDay());
        nisWageDTO.setAddWord(erpPayInfo.getAbs());
        //代发工资明细总额
        BigDecimal wagesToatl = BigDecimal.ZERO;
        if(EmptyUtils.isNotEmpty(erpWageDetailVOList)){
            for(ErpWageDetailVO erpwagedetailVO :erpWageDetailVOList) {
                if (erpwagedetailVO.getWages() != null) {
                    wagesToatl = wagesToatl.add(erpwagedetailVO.getWages());
                }
                NisWageDetailDTO nisWageDetailDTO = new NisWageDetailDTO();
                BeanUtils.copyNotNullProperties(erpwagedetailVO,nisWageDetailDTO);
//                BtInputBankInfoVO btInputBankInfo = this.btInputBankInfoService.getByBankCode(nisWageDetailDTO.getBankCode(),"");
//                if(!EmptyUtils.isEmpty(btInputBankInfo)){
//                    nisWageDetailDTO.setProv(btInputBankInfo.getProv());
//                    nisWageDetailDTO.setCity(btInputBankInfo.getCity());
//                    nisWageDetailDTO.setBankCode(btInputBankInfo.getBankCode());
//                    nisWageDetailDTO.setBankName(btInputBankInfo.getBankName());
//                }else{
//                    btInputBankInfo = this.btInputBankInfoService.getByBankCode("",nisWageDetailDTO.getBankName());
//                    if(!EmptyUtils.isEmpty(btInputBankInfo)) {
//                        nisWageDetailDTO.setProv(btInputBankInfo.getProv());
//                        nisWageDetailDTO.setCity(btInputBankInfo.getCity());
//                        nisWageDetailDTO.setBankCode(btInputBankInfo.getBankCode());
//                    }
//                }
                String payeebankCode = nisWageDetailDTO.getBankCode();
                // 初始化跨行标识、地域标识
                if (!EmptyUtils.isEmpty(payerBankCode) && !EmptyUtils.isEmpty(payeebankCode)) {
                    if (payeebankCode.substring(0, 3).equalsIgnoreCase(payerBankCode.substring(0, 3))) {
                        nisWageDetailDTO.setDifBank(NetbankConstant.DIFBANK_0);
                    } else {
                        nisWageDetailDTO.setDifBank(NetbankConstant.DIFBANK_1);
                    }
                    if (payeebankCode.substring(3,7).equalsIgnoreCase(payerBankCode.substring(3,7))) {
                        nisWageDetailDTO.setAreaSign(NetbankConstant.AREASIGN_0);
                    } else {
                        nisWageDetailDTO.setAreaSign(NetbankConstant.AREASIGN_1);
                    }
                }
                if(!EmptyUtils.isEmpty(nisWageDetailDTO.getWages())){
                    try {
                        nisWageDetailDTO.setWagesVerify(Base64Util.encodeToString((nisWageDetailDTO.getWages().toString()).getBytes("UTF-8")));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                nisWageDetailDTOList.add(nisWageDetailDTO);
            }
        }
        //工资总额与实发工资总额校验
        if (nisWageDTO.getWages() != null && nisWageDTO.getWages().compareTo(wagesToatl) != 0) {
            throw new BusinessException(NetbankMessageEnum.NIS_WAGE_MESSAGE_MONEYSUM_MISMATCH, erpPayInfo.getVoucherNoErp());
        }
        nisWageDTO.setNisWageDetailList(nisWageDetailDTOList);
        nisWageService.saveNisWage(nisWageDTO,user);
    }
    /**
     * 指令交易成功/失败消息提醒
     */
    @Override
    // @XxlJob("ErpPayInfoService")
    public void run(String arguments) {
        if (NotificationConstant.NETBANK.ERP_PAY_APPLY.equals(arguments)) {
            //付款：获取单据来源于ERP的暂存的付款数据
            List<NisBillheadDTO> listPay = nisBillheadService.listErpTodoBill();
            for(NisBillheadDTO vo:listPay){
                //消息提醒
                notificationService.complete(NotificationConstant.NETBANK.PAY_APPLY, vo.getId(), null);
                notificationService.send(vo.getId(), vo.getCorpId(), NotificationConstant.NETBANK.PAY_APPLY, vo);
            }
        } else if (NotificationConstant.NETBANK.ERP_EXPENSE_APPLY.equals(arguments)) {
            //费用报销：获取单据来源于ERP的暂存的费用报销数据
            List<NisExpenseDetailDTO> listExpense = nisExpenseDetailService.listErpTodoBill();
            for(NisExpenseDetailDTO vo:listExpense){
                //消息提醒
                notificationService.complete(NotificationConstant.NETBANK.EXPENSE_APPLY, vo.getId(), null);
                notificationService.send(vo.getId(), vo.getCorpId(), NotificationConstant.NETBANK.EXPENSE_APPLY, vo);
            }
        } else if (NotificationConstant.NETBANK.ERP_WAGE_APPLY.equals(arguments)) {
            //代发工资：获取单据来源于ERP的暂存的代发工资数据
            List<NisWageDTO> listWage = nisWageService.listErpTodoBill();
            for(NisWageDTO vo:listWage){
                //消息提醒
                notificationService.complete(NotificationConstant.NETBANK.WAGE_APPLY, vo.getId(), null);
                notificationService.send(vo.getId(), vo.getCorpId(), NotificationConstant.NETBANK.WAGE_APPLY, vo);
            }
        }
    }
}
