package com.bytter.biz.bdlinks.service.impl;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlinks.entity.BisForeignExc;
import com.bytter.biz.bdlinks.service.BisForeignExcService;
import com.bytter.biz.bdlinks.vo.BisForeignExcVO;
import com.bytter.biz.foreign.service.impl.FexMessageServiceImpl;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.biz.payment.api.service.IBisForeignExcService;
import com.bytter.biz.payment.api.service.IFexMessageService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.FexMessageConstant;
import com.bytter.core.constant.ForeignConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.erp.constant.NotificationConstant;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtSwiftInfoService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtSwiftInfoDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: Service
 * @Description: 汇款指令
 * @author huangx
 * @date 2019-07-19 11:08:59
 * @version 1.0
 *
 */
@Service("bisForeignExcServiceImpl")
@Transactional
@DubboService
public class BisForeignExcServiceImpl implements IBisForeignExcService {
    @Autowired
    BisForeignExcService bisForeignExcService;

    @Autowired(required=false)
    private FexMessageServiceImpl fexMessageServiceImpl;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @Autowired(required=false)
    private IBtSwiftInfoService btSwiftInfoService;

    @Autowired(required=false)
    private IBtBankAccService bankAccService;

    /**
     * 新增汇款指令
     * @param bisForeignExc
     */
    @Override
    public void addBisForeignExc(BisForeignExcDTO bisForeignExc, ICurrentUser user) {
        this.updateOldBisForeignExc(bisForeignExc.getCbsBillId(), user);
        BisForeignExc entity = new BisForeignExc();
        BeanUtils.copyNotNullProperties(bisForeignExc, entity);
        entity.setVoucherStat(BdlinkConstant.WAIT_FILL);
        entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_FOREIGN_EXC));
        entity.setNoticeSign(CommonConstant.SEND_SIGN_NO);
        //加密信息
        entity.setVerifyCode(bisForeignExcService.setVerifyCode(entity));
        //宁波银行特殊处理
        if (StringUtils.isNotBlank(bisForeignExc.getPayerBank()) && bisForeignExc.getPayerBank().indexOf("宁波银行") > -1) {
            //单位证件信息
            CorpDTO corp = corpService.get(bisForeignExc.getCorpId());
            entity.setCertType(corp.getCertType());
            entity.setCertNo(corp.getCertNo());
            if (StringUtils.isBlank(entity.getCertType()) || StringUtils.isBlank(entity.getCertNo())) {
                //单位证件信息
                throw new ParameterException(ForeignMessageEnum.BIS_FOREIGN_EXC_MESSAGE_CERTTYPE_NULL);
            }
            //开户银行代码类型
            if (ForeignConstant.FROM_FOREIGN.equals(bisForeignExc.getVoucherFrom())) {
                if (StringUtils.isNotBlank(entity.getPayeeSwiftCode())) {
                    BtSwiftInfoDTO swiftInfoDTO = btSwiftInfoService.getByCode(entity.getPayeeSwiftCode());
                    if (swiftInfoDTO != null) {
                        entity.setClsFlag(swiftInfoDTO.getClsFlag());
                    }
                }
            }
            if (StringUtils.isBlank(entity.getClsFlag())) {
                //开户银行代码类型
                if (ForeignConstant.FROM_FOREIGN.equals(entity.getVoucherFrom())) {
                    throw new ParameterException(ForeignMessageEnum.BIS_FOREIGN_EXC_MESSAGE_CLSFLAG_NULL_FOREIGN);
                } else {
                    throw new ParameterException(ForeignMessageEnum.BIS_FOREIGN_EXC_MESSAGE_CLSFLAG_NULL_TRAN);
                }
            }
        }
        //付款方国别默认中国
        entity.setPayerCountryCode(BdlinkConstant.COUNTRY_CODE_CHINA);
        BtBankAccDTO payerAcc = bankAccService.getBankAccInfo(entity.getPayerAcc());
        //是否境内银行 1-境内，0-境外
        if (payerAcc != null && CommonConstant.NO.equals(payerAcc.getIsDomesticBank())) {
            if (StringUtils.isNotBlank(payerAcc.getBankCode()) && payerAcc.getBankCode().length()>6) {
                entity.setPayerCountryCode(payerAcc.getBankCode().substring(4,6));
            }
            //存储境外银行swift code
            entity.setPayerSwiftCode(payerAcc.getBankCode());
        }
        //指令编号重复校验
        if(bisForeignExcService.getByVoucherNo(entity.getVoucherNo()) != null){
            throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_VOUCHERNO_REPAY, entity.getVoucherNo());
        }else {
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
            //签名
            entity.setSignatureCode(this.bisForeignExcService.getSignatureCode(entity));
            bisForeignExcService.save(entity);
            //日志记录
            fexMessageServiceImpl.saveMessage(entity.getId(), FexMessageConstant.MESSAGE_TYPE_AUTO_ADD, "", FexMessageConstant.BUS_TYPE_EXC, user.getLoginName(), user.getUsername());
            //消息提醒
            notificationService.send(entity.getId(), entity.getCorpId(), NotificationConstant.FOREIGN.BISFOREIGNEXC_SEND, entity);
        }
    }

    /**
     * 根据单号更新旧指令
     *
     * @param cbsBillId
     * @param user
     * @author zuoy 2018-08-07
     */
    private void updateOldBisForeignExc(String cbsBillId, ICurrentUser user) {
        //根据业务Id获取指令
        BisForeignExc oldExc = bisForeignExcService.getByCbsBillId(cbsBillId);
        if (!EmptyUtils.isEmpty(oldExc)) {
            //指令状态为B-指令退回或者7-指令无效
            if (BdlinkConstant.BACK.equals(oldExc.getVoucherStat()) || BdlinkConstant.INVALIDATE.equals(oldExc.getVoucherStat())) {
                int result = bisForeignExcService.updateBisForeignExcCbsBillNo(oldExc);
                if(result<1) {
                    throw new ParameterException(NetbankMessageEnum.NIS_BISEXC_MESSAGE_EXIST);
                }
            } else {
                throw new ParameterException(NetbankMessageEnum.NIS_BISEXC_MESSAGE_EXIST);
            }
        }
    }

    /**
     * 根据指令单号获取指令信息
     * @param voucherNo
     * @return
     */
    @Override
    public BisForeignExcDTO getBisForeignExcByVoucherNo(String voucherNo) {
        BisForeignExcVO entity = this.bisForeignExcService.getByVoucherNo(voucherNo);
        BisForeignExcDTO bisForeignExcDTO = new BisForeignExcDTO();
        BeanUtils.copyNotNullProperties(entity, bisForeignExcDTO);
        return bisForeignExcDTO;
    }

    /**
     * 根据业务ID获取指令信息
     * @param cbsBillId
     * @return
     */
    @Override
    public BisForeignExcDTO getByCbsBillId(String cbsBillId) {
        BisForeignExc entity = this.bisForeignExcService.getByCbsBillId(cbsBillId);
        BisForeignExcDTO bisForeignExcDTO = new BisForeignExcDTO();
        if(entity != null) {
            BeanUtils.copyNotNullProperties(entity, bisForeignExcDTO);
        }
        return bisForeignExcDTO;
    }

    /**
     * 汇款指令发送
     * @param bisForeignExc
     * @author zuoy 2018-05-16
     */
    @Override
    public void autoSendBisForeignExc(BisForeignExcDTO bisForeignExc, ICurrentUser user) {
        this.updateOldBisForeignExc(bisForeignExc.getCbsBillId(), user);
        BisForeignExc entity = new BisForeignExc();
        BeanUtils.copyNotNullProperties(bisForeignExc, entity);
        if (EmptyUtils.isBlank(entity.getVoucherNo())) {
            entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_FOREIGN_EXC));
        }
        entity.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
        //加密信息
        entity.setVerifyCode(bisForeignExcService.setVerifyCode(entity));
        //宁波银行特殊处理
        if (StringUtils.isNotBlank(bisForeignExc.getPayerBank()) && bisForeignExc.getPayerBank().indexOf("宁波银行") > -1) {
            //单位证件信息
            CorpDTO corp = corpService.get(bisForeignExc.getCorpId());
            entity.setCertType(corp.getCertType());
            entity.setCertNo(corp.getCertNo());
            if (StringUtils.isBlank(entity.getCertType()) || StringUtils.isBlank(entity.getCertNo())) {
                //单位证件信息
                throw new ParameterException(ForeignMessageEnum.BIS_FOREIGN_EXC_MESSAGE_CERTTYPE_NULL);
            }
            //开户银行代码类型
            if (ForeignConstant.FROM_FOREIGN.equals(bisForeignExc.getVoucherFrom())) {
                if (StringUtils.isNotBlank(entity.getPayeeSwiftCode())) {
                    BtSwiftInfoDTO swiftInfoDTO = btSwiftInfoService.getByCode(entity.getPayeeSwiftCode());
                    if (swiftInfoDTO != null) {
                        entity.setClsFlag(swiftInfoDTO.getClsFlag());
                    }
                }
            }
            if (StringUtils.isBlank(entity.getClsFlag())) {
                //开户银行代码类型
                if (ForeignConstant.FROM_FOREIGN.equals(entity.getVoucherFrom())) {
                    throw new ParameterException(ForeignMessageEnum.BIS_FOREIGN_EXC_MESSAGE_CLSFLAG_NULL_FOREIGN);
                } else {
                    throw new ParameterException(ForeignMessageEnum.BIS_FOREIGN_EXC_MESSAGE_CLSFLAG_NULL_TRAN);
                }
            }
        }
        //付款方国别默认中国
        entity.setPayerCountryCode(BdlinkConstant.COUNTRY_CODE_CHINA);
        BtBankAccDTO payerAcc = bankAccService.getBankAccInfo(entity.getPayerAcc());
        //是否境内银行 1-境内，0-境外
        if (payerAcc != null && CommonConstant.NO.equals(payerAcc.getIsDomesticBank())) {
            if (StringUtils.isNotBlank(payerAcc.getBankCode()) && payerAcc.getBankCode().length()>6) {
                entity.setPayerCountryCode(payerAcc.getBankCode().substring(4,6));
            }
            //存储境外银行swift code
            entity.setPayerSwiftCode(payerAcc.getBankCode());
        }
        this.bisForeignExcService.sendBisForeignExc(entity, user);
    }

    /**
     * 生成加密信息
     * @param bisForeignExc
     * @return
     */
    @Override
    public String setVerifyCode(BisForeignExcDTO bisForeignExc){
        BisForeignExc entity = new BisForeignExc();
        BeanUtils.copyNotNullProperties(bisForeignExc, entity);
        return this.bisForeignExcService.setVerifyCode(entity);
    }

    /**
     * 生成签名
     * @param bisForeignExc
     * @return
     */
    @Override
    public String getSignatureCode(BisForeignExcDTO bisForeignExc){
        BisForeignExc entity = new BisForeignExc();
        BeanUtils.copyNotNullProperties(bisForeignExc, entity);
        return this.bisForeignExcService.getSignatureCode(entity);
    }

    @Override
    public List<String> listFexTranReqIdsByStatus(String voucherStat, List<String> tranBillIdList) {
        return this.bisForeignExcService.listFexTranReqIdsByStatus(voucherStat,tranBillIdList);
    }

    @Override
    public BisForeignExcDTO getByVoucherNo(String voucherNo) {
        return null;
    }

    @Override
    public void backBisForeignExcForCashFlow(String id, ICurrentUser user) {
        this.bisForeignExcService.backBisForeignExcForCashFlow(id,user);
    }


}
