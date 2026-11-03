package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 内部授信
 * @author caojy
 *
 */
@Service("abRatCpUrationService")
@Transactional
public class AbRatCpUrationService extends BaseService<String, AbAccrecords>{
    @Autowired
	private IRatCpUrationService ratUrationService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbConfigService abConfigService;
    /**
     * 是否启用内部授信
     * @return 返回是否启用内部授信标志
     */
    @Transactional(readOnly = true)
    public boolean isRelateCpRat(){
        return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISOPENCPRAT).toBoolean(false);
    }
    @Override
    public BaseDao<String, AbAccrecords> getEntityDao() {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     * 挂接内部授信
     * @param userVo
     */
    @Transactional(readOnly = true)
    public void  ratCpLink(String opType,AbAccrecords accRecords,ICurrentUser userVo){
        if(accRecords==null){
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbVoucherinfo vouInfo=accRecords.getVouInfo();
        if(vouInfo==null){
            if(StringUtils.isNotEmpty(accRecords.getVid())){
                vouInfo=abVoucherinfoService.get(accRecords.getVid(),true);
                accRecords.setVouInfo(vouInfo);
            }
        }
        if(vouInfo==null){
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_RATURATION_MESSAGE_VOUINFONOTNULL);
        }
        AbVoucherinfoVice viceInfo=accRecords.getVouViceInfo();
        if(viceInfo==null){
            if(StringUtils.isNotEmpty(accRecords.getVvid())){
                viceInfo=abVoucherinfoViceService.get(accRecords.getVvid(),true);
                accRecords.setVouViceInfo(viceInfo);
            }
        }
        if(viceInfo==null){
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_RATURATION_MESSAGE_VOUVICEINFONOTNULL);
        }
        RatUrationDTO urationDTO = AbRatCpUrationService.appendVO(accRecords,opType);
        if(userVo!=null){
            urationDTO.setUserId(userVo.getId());
        }
        if(!StringUtils.isBlank(viceInfo.getInsideRatModel())&& viceInfo.getInsideRatModel().equals(AccebillConstant.AB_BILL_RATMODEL_N)){
            //对已挂暂存的数据进行删除操作
            ratUrationService.deleteUseCpRation(urationDTO);
            return ;
        }
        //如果设置单笔业务不挂授信处理
        if(!AccebillConstant.AB_BILL_RATMODEL_Y.equals(viceInfo.getInsideRatModel())){
            if(AccebillConstant.BILL_OPERATE_ADD.equals(opType)
                    ||AccebillConstant.BILL_OPERATE_EDIT.equals(opType)
                    ||AccebillConstant.BILL_OPERATE_SUBMIT.equals(opType)){
                //对已挂暂存的数据进行删除操作
                ratUrationService.deleteUseCpRation(urationDTO);
            }else if(AccebillConstant.BILL_OPERATE_SUPPLY.equals(opType)){
                //补录时进行退回再删除
                ratUrationService.backUseCpRation(urationDTO);
                ratUrationService.deleteUseCpRation(urationDTO);
            }
            return;
        }
        if(!isRelateCpRat()){
            //对已挂暂存的数据进行删除操作
            ratUrationService.deleteUseCpRation(urationDTO);
            return;
        }
        if(AccebillConstant.BILL_OPERATE_ADD.equals(opType)
                ||AccebillConstant.BILL_OPERATE_EDIT.equals(opType)){
            ratUrationService.tempUseCpRation(urationDTO);
        }else if(AccebillConstant.BILL_OPERATE_SUBMIT.equals(opType)){
            ratUrationService.addUseCpRation(urationDTO);
        }else if(AccebillConstant.BILL_OPERATE_BATCHSUBMIT.equals(opType)){
            ratUrationService.submitUseRation(urationDTO);
        }else if(AccebillConstant.BILL_OPERATE_BACK.equals(opType)){
            ratUrationService.backUseCpRation(urationDTO);
        }else if(AccebillConstant.BILL_OPERATE_APPOK.equals(opType)){
            ratUrationService.passUseCpRation(urationDTO);
        }else if(AccebillConstant.BILL_OPERATE_REPULSE.equals(opType)){
            urationDTO.setAllRelease(true);
            ratUrationService.releaseRation(urationDTO);
        }else if(AccebillConstant.BILL_OPERATE_DELETE.equals(opType)){
            ratUrationService.deleteUseCpRation(urationDTO);
        }else if(AccebillConstant.BILL_OPERATE_SUPPLY.equals(opType)){
            ratUrationService.backUseCpRation(urationDTO);
            ratUrationService.addUseCpRation(urationDTO);
        }
    }
    //拼接VO参数
    protected static RatUrationDTO appendVO(AbAccrecords accRecords, String opType){
        RatUrationDTO ratDTO  = new RatUrationDTO();
        if(accRecords != null && accRecords.getRatCpUrationDTO() != null){
            ratDTO = accRecords.getRatUrationDTO();
            BeanUtils.copyNotNullProperties(accRecords.getRatCpUrationDTO(),ratDTO);// 票据信息填充到审批对象
        }
        if(AccebillConstant.BILL_OPERATE_ADD.equals(opType)
                ||AccebillConstant.BILL_OPERATE_EDIT.equals(opType)
                ||AccebillConstant.BILL_OPERATE_SUBMIT.equals(opType)
                ||AccebillConstant.BILL_OPERATE_SUPPLY.equals(opType)){
            ratDTO.setUrationId(accRecords.getRatCpUrationDTO() == null?null:accRecords.getRatCpUrationDTO().getCpUrationId());
            ratDTO.setRatCode(accRecords.getRatCpUrationDTO() == null?null:accRecords.getRatCpUrationDTO().getCpRatCode());
            ratDTO.setRatItemName(accRecords.getRatCpUrationDTO() == null?null:accRecords.getRatCpUrationDTO().getCpRatCode());
            ratDTO.setRatItemCode(accRecords.getRatCpUrationDTO() == null?null:accRecords.getRatCpUrationDTO().getCpRatItemCode());
            ratDTO.setCorpId(accRecords.getVouViceInfo().getCorpId());
            ratDTO.setBillNo(accRecords.getVouInfo().getOrigNo());
            ratDTO.setBeginDate(accRecords.getVouInfo().getStartDate());
            ratDTO.setEndDate(accRecords.getVouInfo().getEndDate());
            ratDTO.setMoney(accRecords.getVouInfo().getBillMoney());
        }
        ratDTO.setBillId(accRecords.getVvid());
        ratDTO.setSysCode(ConfigConstant.ACCEBILL);
        ratDTO.setBusinessName(RationConstant.BUSINESS_NAME_04);
        return ratDTO;
    }
    /**
     * 根据从表ID获取挂接的授信信息
     * @param vvid 从表ID
     * @return 挂接的授信信息
     */
    @Transactional(readOnly = true)
    public RatUrationDTO getRatUrationDTOByBillId(String vvid){
        return ratUrationService.getRatUrationDTOByBillId(ConfigConstant.ACCEBILL, vvid);
    }
}
