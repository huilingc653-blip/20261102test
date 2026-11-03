package com.bytter.bill.domain.bill.service;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.billbank.util.StringUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDisDTO;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
/**
 * 票据授信挂接服务类
 * @author zhengg
 *
 */
@Service("abRatUrationService")
@Transactional
public class AbRatUrationService extends BaseService<String, AbAccrecords> {
	@Autowired
	private IRatUrationService ratUrationService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbConfigService abConfigService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 挂授信返回true不挂返回false
	 * @return 返回是否授信标志
	 */
	@Transactional(readOnly = true)
	public boolean isRelateRat(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISCREDIT).toBoolean(false);
	}
	/**
	 * 是否启用授信敞口(前置：挂接时候显示)，挂接返回1不挂返回0
	 * @return 返回是否授信标志
	 *
	 */
	@Transactional(readOnly = true)
	public Integer isOpenRat(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISOPENRAT).toInteger(0);
	}
	/**
	 * 挂授信返回true不挂返回false
	 * @return 返回商票贴现是否挂银行授信模式
	 */
	@Transactional(readOnly = true)
	public Integer isDisCountCredit(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISDISCOUNTCREDIT).toInteger(0);
	}
	/**
	 * 挂授信返回true不挂返回false
	 * @return 返回第三方票据贴现是否挂银行授信模式
	 */
	@Transactional(readOnly = true)
	public Integer isDisThirdBillCredit(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISDISTHIRDBILLCREDIT).toInteger(0);
	}
	/**
	 * 挂授信返回false不挂返回true
	 * @return 返回是否授信标志
	 */
	@Transactional(readOnly = true)
	public boolean isNotRelateRat(){
		return !isRelateRat();
	}
	/**
	 * 挂授信返回false不挂返回true
	 * @return 返回是否授信标志
	 */
	@Transactional(readOnly = true)
	public boolean isNotDisCountCredit(){
		return isDisCountCredit()>=1?false:true;
	}
	/**
	 * 是否启用授信敞口(前置：挂接时候显示)，挂接返回true不挂返回false
	 * @return 返回是否授信标志
	 */
	@Transactional(readOnly = true)
	public boolean isNotOpenRat(){
		return isOpenRat()>=1?true:false;
	}
	/**
	 * 操作挂接授信
	 * @param accRecords 挂接流水
	 * @param opType 操作类型 新增 修改 提交 打回
	 */
	public void opRatUration(AbAccrecords accRecords, String opType, ICurrentUser userVo){
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
		if(AccebillConstant.BRIEF_STATUS_011.equals(accRecords.getBriefId())){
			if(isNotDisCountCredit()){
				return;
			}
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
		RatUrationDTO urationDTO=setRatUrationDTO(accRecords, opType);
		if(userVo!=null){
			urationDTO.setUserId(userVo.getId());
		}
		if(isNotRelateRat()){
			//对已挂暂存的数据进行删除操作
			ratUrationService.deleteUseRation(urationDTO);
			return;
		}
		//如果设置单笔业务不挂授信处理
		if(!AccebillConstant.AB_BILL_RATMODEL_Y.equals(viceInfo.getRatmodel())){
			if(AccebillConstant.BILL_OPERATE_ADD.equals(opType)
					||AccebillConstant.BILL_OPERATE_EDIT.equals(opType)
					||AccebillConstant.BILL_OPERATE_SUBMIT.equals(opType)){
				//对已挂暂存的数据进行删除操作
				ratUrationService.deleteUseRation(urationDTO);
			}else if(AccebillConstant.BILL_OPERATE_SUPPLY.equals(opType)){
				//补录时进行退回再删除
				ratUrationService.backUseRation(urationDTO);
				ratUrationService.deleteUseRation(urationDTO);
			}
			return;
		}
		//进行真实业务处理功能
		if(AccebillConstant.BILL_OPERATE_ADD.equals(opType)
				||AccebillConstant.BILL_OPERATE_EDIT.equals(opType)){
			ratUrationService.tempUseRation(urationDTO);
		}else if(AccebillConstant.BILL_OPERATE_SUBMIT.equals(opType)){
			ratUrationService.addUseRation(urationDTO);
		}else if(AccebillConstant.BILL_OPERATE_BATCHSUBMIT.equals(opType)){
			ratUrationService.submitUseRation(urationDTO);
		}else if(AccebillConstant.BILL_OPERATE_BACK.equals(opType)){
			ratUrationService.backUseRation(urationDTO);
		}else if(AccebillConstant.BILL_OPERATE_APPOK.equals(opType)){
			ratUrationService.passUseRation(urationDTO);
		}else if(AccebillConstant.BILL_OPERATE_REPULSE.equals(opType)){
			urationDTO.setAllRelease(true);
			ratUrationService.releaseRation(urationDTO);
		}else if(AccebillConstant.BILL_OPERATE_DELETE.equals(opType)){
			ratUrationService.deleteUseRation(urationDTO);
		}else if(AccebillConstant.BILL_OPERATE_SUPPLY.equals(opType)){
			ratUrationService.backUseRation(urationDTO);
			ratUrationService.addUseRation(urationDTO);
			//华南城授信挂接新需求
		}else if(AccebillConstant.BILL_OPERATE_OPTBACK.equals(opType)){
			urationDTO.setAllRelease(true);
			urationDTO.setReleaseNonRecycleRat(true);
			ratUrationService.releaseRation(urationDTO);
		}else if(AccebillConstant.BILL_INSTRUCTION_OPTBACK.equals(opType)){
			//票据审批通过后已占用额度,指令失败打回
			ratUrationService.billReleaseRation(urationDTO);
		}
	}
	/**
	 * 设置挂接授信的DTO值
	 * @param accRecords 挂接流水
	 * @param opType 操作类型 新增 修改 提交 打回
	 * @return 返回授信挂接实体
	 */
	@Transactional(readOnly = true)
	public RatUrationDTO setRatUrationDTO(AbAccrecords accRecords,String opType){
		// todo:数据转换
		RatUrationDTO ruDto = accRecords.getRatUrationDTO();
//		RatUrationDTO ruDto= BeanUtils.copyNewInstanceProperties(ratUrationDTO, RatUrationDTO.class);
		if(ruDto==null){
			ruDto=new RatUrationDTO();
		}
		if(AccebillConstant.BILL_OPERATE_ADD.equals(opType)
				||AccebillConstant.BILL_OPERATE_EDIT.equals(opType)
				||AccebillConstant.BILL_OPERATE_SUBMIT.equals(opType)
				||AccebillConstant.BILL_OPERATE_SUPPLY.equals(opType)){
			//判断挂银行授信
			if(AccebillConstant.AB_BILL_RATMODEL_Y.equals(accRecords.getVouViceInfo().getRatmodel())){
				ruDto.setCorpId(accRecords.getVouViceInfo().getRatBankCorpId());//银行授信单位CorpId
			}else{
				ruDto.setCorpId(accRecords.getVouViceInfo().getCorpId());
			}
			if(StringUtil.isEmpty(ruDto.getCorpId())){
				ruDto.setCorpId(accRecords.getVouViceInfo().getCorpId());
			}
			ruDto.setBillNo(accRecords.getVouInfo().getOrigNo());
			ruDto.setContactNo(accRecords.getVouInfo().getAcontractNo());
			ruDto.setBeginDate(accRecords.getVouInfo().getStartDate());
			ruDto.setEndDate(accRecords.getVouInfo().getEndDate());
			ruDto.setMoney(accRecords.getVouInfo().getBillMoney());
			ruDto.setIsOpenRat(accRecords.getRatUrationDTO()!=null&& StrUtil.isNotEmpty(accRecords.getRatUrationDTO().getIsOpenRat())
					?accRecords.getRatUrationDTO().getIsOpenRat():"0");
			ruDto.setBailRate(accRecords.getRatUrationDTO()!=null&&accRecords.getRatUrationDTO().getBailRate()!=null
					?accRecords.getRatUrationDTO().getBailRate():new BigDecimal("0.00"));
			ruDto.setBondMoney(accRecords.getRatUrationDTO()!=null&&accRecords.getRatUrationDTO().getBondMoney()!=null
					?accRecords.getRatUrationDTO().getBondMoney():new BigDecimal("0.00"));
		}
		if(StrUtil.isEmpty(ruDto.getBondCurId())){
			ruDto.setBondCurId(ruDto.getInitCurCode());
		}
		ruDto.setBillId(accRecords.getVvid());
		ruDto.setSysCode(ConfigConstant.ACCEBILL);
		ruDto.setBusinessName(RationConstant.BUSINESS_NAME_04);
		return ruDto;
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

	/**
	 * 根据授信编号和授信品种类型获取授信信息
	 * @param corpId 授信单位ID
	 * @param contractNo 授信合同编号
	 * @param itemName 授信品种名称
	 * @return 授信信息
	 */
	public RatUrationDisDTO getRatUrationDTOByParams(String corpId, String contractNo, String itemName){
		return ratUrationService.getRatUrationDTOByParams(corpId,contractNo,itemName);
	}
}
