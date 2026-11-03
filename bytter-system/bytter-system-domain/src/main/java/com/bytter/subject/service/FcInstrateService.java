package com.bytter.subject.service;


import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.subject.constant.FinacialConstant;
import com.bytter.subject.dao.FcInstrateDao;
import com.bytter.subject.entity.FcInstrate;
import com.bytter.system.api.dto.FcInstrateDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcInstrateAdjustService;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcInstrateAdjustDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.system.api.enums.ParamConfigMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Title: Service
* @Description: 科目利率表
* @author onlineGenerator
* @date 2017-05-18 15:45:32
* @version 1.0
*
*/
@Service("fcInstrateService")
@Transactional
public class FcInstrateService extends BaseService<FcInstrate,String> {
	@Autowired
	private FcInstrateDao fcInstrateDao;
	@DubboReference(check = false)
	private IInnerAccountService innerAccService;
	@DubboReference(check = false)
	private IFcInstrateAdjustService innerAdjustService;
	@DubboReference(check = false)
	private IFcSubjectService iFcSubjectService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	/**
	 * 删除对象
	 * @param dto
	 */
	public void deleteEntity(com.bytter.system.pricing.dto.FcInstrateDTO dto){
		FcInstrate entity = new FcInstrate();
		BeanUtils.copyNotNullProperties(dto,entity);
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(String id){
		super.delete(id);
	}
	/**
	 * 保存
	 * @param fcInstrateDTO
	 */
	public void save(com.bytter.system.pricing.dto.FcInstrateDTO fcInstrateDTO, String loginName){
		FcInstrate entity = new FcInstrate();
		BeanUtils.copyNotNullProperties(fcInstrateDTO,entity);
		//获取最近一次科目利率调整记录
		FcInstrate instrate = fcInstrateDao.getLastInstrate(entity.getSubjectCode(), entity.getCurCode(),
				entity.getInstrtType(),entity.getEndUnit());
		FcSubjectDTO fcSubject=iFcSubjectService.findSubjectById(entity.getSubjectCode());
		BigDecimal strate=fcSubject.getInterestRate();
		if(instrate!=null){
			Date exeDate = instrate.getExeDate();
			Date today = DateUtils.getDate();//获取当前时间
			if (today.compareTo(exeDate) < 0) {//当前科目有未执行的利率不允许新增
				throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISNOTUSED);
			} else if (exeDate.compareTo(entity.getExeDate()) < 0) {//最近科目利率执行日期小于当前执行日期
				strate = instrate.getInterestRate();// 原利率
			} else {//科目利率执行日期大于当前新增执行日期，不允许调整
				throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISEXIST);
			}
		}
		//新增的新利率INTEREST_RATE，更新到FC_SUBJECT表中对应的字段为INTEREST_RATE
		if(entity.getInterestRate()!=null){
			fcSubject.setInterestRate(entity.getInterestRate());
			iFcSubjectService.update(fcSubject,loginName);
		}
		if(strate==null){//原利率如果为空，给默认值为0
			strate = new BigDecimal(0);
		}
		entity.setOldInstrate(strate);
		entity.setStatus(Integer.valueOf(FinacialConstant.VALID_TRUE));
		super.saveEntity(entity);
		//科目利率调整完成后需根据调整科目的相关信息对所有账户的利率进行调整
		List<FcAccDTO> accList = innerAccService.listAccountByParams(
				entity.getCurCode(), null, null, entity.getSubjectCode(),
				entity.getEndUnit());
		//写入待复核的内部账户利率调整数据
		if (!ListUtils.isNullOrEmpty(accList)) {
			for (FcAccDTO acc : accList) {
				FcInstrateAdjustDTO dto = new FcInstrateAdjustDTO();
				dto.setAccount(acc.getBlnctrAcc());
				dto.setOldInstrate(strate);
				dto.setNewInstrate(entity.getInterestRate());
				dto.setInstrtType(entity.getInstrtType());
				dto.setInstrateId(entity.getId());
				dto.setStatus(CommonConstant.STATUE_WAIT_CHECK);
				dto.setChangtype(FinacialConstant.INSTRATE_CHANGT_YPE);
				dto.setAdjustTime(entity.getExeDate());
				dto.setOperatorCode(loginName);
				innerAdjustService.saveFcInstrateAdjust(dto);
			}
		}
		//1.查询复核条件的账户信息
		//FIXME
		//TODO
		//List<> fcAccList = innerAccService.getFcAccList();
		//2.生成账户的利率调整数据,方便后续计息
	}
	/**
	 * 更新
	 * @param dto
	 */
	public void update(com.bytter.system.pricing.dto.FcInstrateDTO dto){
		FcInstrate entity = new FcInstrate();
		BeanUtils.copyNotNullProperties(dto,entity);
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param fcInstrateDTO
	 */
	public void saveOrUpdate(com.bytter.system.pricing.dto.FcInstrateDTO fcInstrateDTO){
		FcInstrate entity = new FcInstrate();
		BeanUtils.copyNotNullProperties(fcInstrateDTO,entity);
		//获取最近一次科目利率调整记录
		FcInstrate instrate = fcInstrateDao.getLastInstrate(entity.getSubjectCode(), entity.getCurCode(),
				entity.getInstrtType(),entity.getEndUnit());
		if (instrate != null) {
			Date exeDate = instrate.getExeDate();
			Date today = DateUtils.getDate();// 获取当前时间
			if (today.compareTo(exeDate) > 0) {// 当前科目已执行的利率不允许修改
				throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISUSED);
			}
			//科目利率执行日期是否有存在数据
			boolean isExist = fcInstrateDao.isExistInstrate(entity.getSubjectCode(), entity.getCurCode(),
					entity.getInstrtType(),entity.getEndUnit(),entity.getExeDate(),entity.getId());
			if (isExist) {
				throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISEXIST);
			}
		}
		//根据科目id获取所有内部账号的调整利率
		List<FcInstrateAdjustDTO> dtoList = innerAdjustService
				.getDTOByInstrateId(entity.getId());
		if (!ListUtils.isNullOrEmpty(dtoList)) {
			for (FcInstrateAdjustDTO dto : dtoList) {
				dto.setOldInstrate(entity.getOldInstrate());
				dto.setNewInstrate(entity.getInterestRate());
				dto.setInstrtType(entity.getInstrtType());
				dto.setStatus(CommonConstant.STATUE_WAIT_CHECK);
				dto.setChangtype(FinacialConstant.INSTRATE_CHANGT_YPE);
				dto.setAdjustTime(entity.getExeDate());
				//innerAdjustService.updateFcInstrateAdjust(dto);
			}
		}
		super.saveOrUpdate(entity);
	}
	public BaseDao<FcInstrate, String> getEntityDao() {
		return fcInstrateDao;
	}
	/**
	 * 获取科目利率table
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput fcInstrateList(DataTablesInput dataTablesInput){
		DataTablesOutput dataOut = fcInstrateDao.fcInstrateList(dataTablesInput);
		List<FcInstrateDTO> instrateList = dataOut.getData();
		if (instrateList != null && instrateList.size() > 0) {
			List<String> subjcetIds = instrateList.stream().map(FcInstrateDTO::getSubjectId).collect(Collectors.toList());
			List<String> curIds = instrateList.stream().map(FcInstrateDTO::getCurId).collect(Collectors.toList());
			List<FcSubjectDTO> subjectDTOS = iFcSubjectService.getSubjectByIds(subjcetIds);
			Map<String, String> subjectMap = subjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, FcSubjectDTO::getSubjectName,(a,b)->a));
			Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrencyByIds(curIds);
			for (FcInstrateDTO vo : instrateList) {
				vo.setSubjectName(subjectMap.get(vo.getSubjectId()));
				vo.setCurName(curMap.get(vo.getCurId()).getCurName());
				boolean flag = false;
				boolean isInstrate = true;//是否只有一条科目利率
				String subjectCode = vo.getSubjectCode();
				String instrtType = vo.getInstrtType();
				String endUnit = vo.getEndUnit();
				String curCode = vo.getCurCode();
				for (FcInstrateDTO fc : instrateList) {
					if ((!vo.getId().equals(fc.getId()))
							&& subjectCode.equals(fc.getSubjectCode())
							&& instrtType.equals(fc.getInstrtType())
							&& endUnit.equals(fc.getEndUnit())
							&& curCode.equals(fc.getCurCode())) {//如果有多条调整记录，同科目、同币别、同利率类型、同期限单位）最后一次调整记录可修改和删除，之前的记录只能查看；
						isInstrate = false;
						if (vo.getExeDate().compareTo(fc.getExeDate()) > 0) {// 判断最后修改日期
							flag = true;
						} else {
							flag = false;
							break;
						}
					}
				}
				if (flag || isInstrate) {
					vo.setIsShow(FinacialConstant.BOOLEAN_TRUE);//可修改、删除
				}
			}
		}
		return dataOut;
	}
	/**
	 * 根据id查询科目利率详情
	 * @param id
	 * @return
	 */
	public com.bytter.system.pricing.dto.FcInstrateDTO getfcInstrate(String id){
		FcInstrateDTO vo = fcInstrateDao.getfcInstrate(id);
		String subjectName = iFcSubjectService.getSubjectNameById(vo.getSubjectId());
		BtCurrencyDTO currencyDTO = iBtCurrencyService.getObjectById(vo.getCurId());
		String curName = currencyDTO.getCurName();
		vo.setSubjectName(subjectName);
		vo.setCurName(curName);
		vo.setCurCode(currencyDTO.getId());
		com.bytter.system.pricing.dto.FcInstrateDTO dto = new com.bytter.system.pricing.dto.FcInstrateDTO();
		BeanUtils.copyNotNullProperties(vo, dto);
		return dto;
	}
	/**
	 * 删除科目利率
	 * @param id
	 */
	public void deleteFcInstrate(String id){
		FcInstrate fcInstrate = this.get(id);
		//根据科目id获取所有内部账号的调整利率
		List<FcInstrateAdjustDTO> dtoList = innerAdjustService
				.getDTOByInstrateId(id);
		if (!ListUtils.isNullOrEmpty(dtoList)) {
			boolean isCheck = false;
			for (FcInstrateAdjustDTO dto : dtoList) {// 只要有一条复核通过的调整记录不允许删除科目利率
				if (dto.getStatus() == CommonConstant.STATUE_CHECK) {
					isCheck = true;
					break;
				}
			}
			if (isCheck) {
				throw new ParameterException(
						ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISCHECK);
			}
			for (FcInstrateAdjustDTO dto : dtoList) {// 删除所有的调整记录
				//innerAdjustService.deleteFcInstrateAdjust(dto);
			}
		}
		fcInstrate.setStatus(Integer.valueOf(FinacialConstant.VALID_FALSE));
		this.updateEntity(fcInstrate);
	}
	/**
	 * 获取最近一次的科目利率
	 * @param subjectCode
	 * @param curCode
	 * @param instrtType
	 * @param endUnit
	 * @return
	 */
	public com.bytter.system.pricing.dto.FcInstrateDTO getLastInstrate(String subjectCode, String curCode, String instrtType, String endUnit) {
		FcInstrate lastInstrate = this.fcInstrateDao.getLastInstrate(subjectCode, curCode, instrtType, endUnit);
		com.bytter.system.pricing.dto.FcInstrateDTO fcInstrateDTO = new com.bytter.system.pricing.dto.FcInstrateDTO();
		if(null != lastInstrate) {
			BeanUtils.copyNotNullProperties(lastInstrate, fcInstrateDTO);
		}
		return fcInstrateDTO;
	}

}
