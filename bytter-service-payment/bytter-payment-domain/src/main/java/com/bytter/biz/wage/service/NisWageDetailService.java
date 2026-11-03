package com.bytter.biz.wage.service;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.wage.dao.NisWageDetailDao;
import com.bytter.biz.wage.entity.NisWageDetail;
import com.bytter.biz.wage.vo.NisWageDetailVO;
import com.bytter.biz.wage.vo.NisWageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: NIS_WAGE_DETAIL
 * @date 2018-01-08 17:19:59
 */
@Service("nisWageDetailService")
@Transactional
public class NisWageDetailService extends BaseService<NisWageDetail, String> {
	@Autowired
	private NisWageDetailDao nisWageDetailDao;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(NisWageDetail entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(NisWageDetail entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(NisWageDetail entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(NisWageDetail entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<NisWageDetail, String> getEntityDao() {
		return nisWageDetailDao;
	}
	/**
	 * 查询代发工资明细
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		DataTablesOutput output = nisWageDetailDao.getDataTablesUnion(dataTablesInput);
		List<NisWageDetailVO> list = output.getData();
		if (EmptyUtils.isEmptyList(list)) {
			return output;
		}
		Map<String, DictDTO> dictDTOMap = dictService.listMap("bisExc.voucherStat");
		for (NisWageDetailVO vo : list) {
			BigDecimal wages = null;
			DictDTO dictDTO = dictDTOMap.getOrDefault(vo.getVoucherStat(), DictDTO.EMPTY);
			vo.setVoucherStatName(dictDTO.getName());
			try {
				if(StringUtils.isNotEmpty(vo.getWagesVerify())) {
					wages = new BigDecimal(new String(Base64Util.decodeFromString(vo.getWagesVerify()), "UTF-8"));
				}
			} catch (UnsupportedEncodingException e) {
				logger.error("类型转换异常::", e);
			}
			vo.setWages(wages);
		}
		return output;
	}
	/**
	 * 保存
	 *
	 * @param nisWageDetail
	 */
	public void saveNisWageDetail(NisWageDetailVO nisWageDetail, ICurrentUser user) {
		NisWageDetail entity = new NisWageDetail();
		BeanUtils.copyNotNullProperties(nisWageDetail, entity);
		entity.setWageDtlNo(SequenceSingleton.getSequence(SequenceEnum.NIS_WAGE_DETAIL));
		entity.setCreateBy(user.getLoginName());
		entity.setCreateDate(DateUtils.getDate());
		this.save(entity);
	}
	
	/**
	 * 批量保存
	 *
	 * @param nisWageDetail
	 */
	public void saveBatchNisWageDetail(List<NisWageDetailVO> nisWageDetails, ICurrentUser user) {
		if(CollectionUtils.isNotEmpty(nisWageDetails)) {
			List<NisWageDetail>  entityList = new ArrayList<NisWageDetail>();
			for(NisWageDetailVO nisWageDetail : nisWageDetails) {
				NisWageDetail entity = new NisWageDetail();
				BeanUtils.copyNotNullProperties(nisWageDetail, entity);
				entity.setWageDtlNo(SequenceSingleton.getSequence(SequenceEnum.NIS_WAGE_DETAIL));
				entity.setCreateBy(user.getLoginName());
				entity.setCreateDate(DateUtils.getDate());
				entityList.add(entity);
			}
			this.batchSave(entityList,1000);
		}
	}
	/**
	 * 更新
	 *
	 * @param nisWageDetail
	 */
	public void updateNisWageDetail(NisWageDetailVO nisWageDetail, ICurrentUser user) {
		NisWageDetail entity = this.get(nisWageDetail.getId());
		BigDecimal wages = new BigDecimal(StrUtil.isBlank(entity.getWagesVerify()) ? "0" : new String(Base64Util.decodeFromString(entity.getWagesVerify())));
		try {
			if(StrUtil.isNotBlank(nisWageDetail.getWagesVerify())){
				wages = new BigDecimal(new String(Base64Util.decodeFromString(nisWageDetail.getWagesVerify()), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("updateNisWageDetail====",e);
		}
		//校验是否存在账号+金额+用途相同的工资明细
		List<NisWageDetail> list = nisWageDetailDao.listRepeatData(nisWageDetail.getId(), entity.getWageId(), nisWageDetail.getBankAcc(), nisWageDetail.getPurpose());
		if (!EmptyUtils.isEmptyList(list)) {
			for (NisWageDetail dtl : list) {
				BigDecimal wagesNew = null;
				try {
					wagesNew = new BigDecimal(new String(Base64Util.decodeFromString(dtl.getWagesVerify()), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error("updateNisWageDetail====",e);
				}
				if (wages.compareTo(wagesNew) == 0) {
					throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_BANKACC_REPEAT);
				}
			}
		}
		BeanUtils.copyNotNullProperties(nisWageDetail, entity);
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
	}
	/**
	 * 根据工资Id获取工资明细列表
	 *
	 * @param wageId
	 * @return
	 */
	public List<NisWageDetailVO> listByWageId(String wageId) {
		return nisWageDetailDao.listByWageId(wageId);
	}
	/**
	 * 根据工资Id获取非交易成功的工资明细
	 *
	 * @param wageId
	 * @return
	 */
	public List<NisWageDetailVO> listByWageIdNotFailure(String wageId) {
		return nisWageDetailDao.listByWageIdNotFailure(wageId);
	}
	/**
	 * 根据工资Id获取交易成功的工资明细
	 *
	 * @param wageId
	 * @return
	 */
	public List<NisWageDetailVO> listByWageIdSuccess(String wageId) {
		return nisWageDetailDao.listByWageIdSuccess(wageId);
	}
	/**
	 * 设置代发工资的支付结果
	 * @param nisWage
	 * @return
	 */
	public void setNisWagePayResult(NisWageVO nisWage) {
		//交易成功
		List<NisWageDetailVO> listWageDetailSuccess = this.listByWageIdSuccess(nisWage.getId());
		nisWage.setSuccessWages(BigDecimal.ZERO);
		nisWage.setSuccessNum(0);
		if (!EmptyUtils.isEmpty(listWageDetailSuccess)) {
			for (NisWageDetailVO dtl : listWageDetailSuccess) {
				BigDecimal wages = null;
				try {
					wages = new BigDecimal(new String(Base64Util.decodeFromString(dtl.getWagesVerify()), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				nisWage.setSuccessWages(nisWage.getSuccessWages().add(wages));
			}
			nisWage.setSuccessNum(listWageDetailSuccess.size());
		}
		//交易失败
		List<NisWageDetailVO> listWageDetailFail = this.listByWageIdNotFailure(nisWage.getId());
		nisWage.setFailWages(BigDecimal.ZERO);
		nisWage.setFailNum(0);
		if (!EmptyUtils.isEmpty(listWageDetailFail)) {
			for (NisWageDetailVO dtl : listWageDetailFail) {
				BigDecimal wages = null;
				try {
					wages = new BigDecimal(new String(Base64Util.decodeFromString(dtl.getWagesVerify()), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				nisWage.setFailWages(nisWage.getFailWages().add(wages));
			}
			nisWage.setFailNum(listWageDetailFail.size());
		}
		//等待结果
		List<NisWageDetailVO> listWageDetailWait = this.listByWageIdWaitResult(nisWage.getId());
		nisWage.setWaitWages(BigDecimal.ZERO);
		nisWage.setWaitNum(0);
		if (!EmptyUtils.isEmpty(listWageDetailWait)) {
			for (NisWageDetailVO dtl : listWageDetailWait) {
				BigDecimal wages = null;
				try {
					wages = new BigDecimal(new String(Base64Util.decodeFromString(dtl.getWagesVerify()), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				nisWage.setWaitWages(nisWage.getWaitWages().add(wages));
			}
			nisWage.setWaitNum(listWageDetailWait.size());
		}
	}
	/**
	 * 获取代发工资明细总额
	 * @param wageId
	 * @return
	 */
	public BigDecimal getSumWages(String wageId) {
		//交易成功
		List<NisWageDetailVO> listWageDetail = this.listByWageId(wageId);
		BigDecimal result = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(listWageDetail)) {
			for (NisWageDetailVO dtl : listWageDetail) {
				try {
					if(StringUtils.isNotEmpty(dtl.getWagesVerify())) {
						result = result.add(new BigDecimal(new String(Base64Util.decodeFromString(dtl.getWagesVerify()), "UTF-8")));
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	/**
	 * 带原交易状态更新代发工资明细单
	 * @param vo 代发工资对象
	 * @return
	 */
	public void updateNisWageDetailVoucherStat(NisWageDetailVO vo) {
		int result = this.nisWageDetailDao.updateNisWageDetailVoucherStat(vo);
		if(result<1){
			throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getWageDtlNo());
		}
	}
	/**
	 * 根据工资Id获取等待结果的代发工资明细
	 *
	 * @param wageId
	 * @return
	 */
	public List<NisWageDetailVO> listByWageIdWaitResult(String wageId) {
		return nisWageDetailDao.listByWageIdWaitResult(wageId);
	}
	/**
	 * 根据指令编号获取代发工资明细
	 * @param voucherNo
	 * @param wageId 代发工资ID
	 * @return
	 */
	public List<NisWageDetailVO> listByVoucherNo(String voucherNo, String wageId) {
		return nisWageDetailDao.listByVoucherNo(voucherNo, wageId);
	}
	/**
	 * 根据指令编号更新代发工资明细
	 * @param voucherNo 指令编号
	 * @param wageId 代发工资ID
	 * @return
	 */
	public int updateVoucherStatByVoucherNo(String voucherNo, String wageId, String newVoucherNo) {
		return nisWageDetailDao.updateVoucherStatByVoucherNo(voucherNo, wageId, newVoucherNo);
	}
	/**
	 * 根据指令编号更新代发工资明细
	 * @param wageId 代发工资ID
	 * @return
	 */
	public int updateIdNo(String wageId){
		return nisWageDetailDao.updateIdNo(wageId);
	}
	/**
	 * 根据指令编号和代发工资ID获取等待结果的代发工资明细列表
	 * @param voucherNo 付款指令编号
	 * @param wageId 代发工资ID
	 * @return 代发工资明细集合
	 */
	public List<NisWageDetailVO> listByVoucherNoWaitResult(String voucherNo, String wageId) {
		return nisWageDetailDao.listByVoucherNoWaitResult(voucherNo, wageId);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param voucherNo 付款指令编号
	 * @param wageId 代发工资ID
	 * @param voucherStat 修改的指令
	 * @return
	 */
	public int updateVoucherStatById(String voucherNo, String wageId, String voucherStat){
		return this.nisWageDetailDao.updateVoucherStatById(voucherNo, wageId, voucherStat);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param voucherNo 付款指令编号
	 * @param wageId 代发工资ID
	 * @param voucherStat 修改的指令
	 * @param dtlIds 修改的明细单号
	 * @param isIn
	 * @return
	 */
	public int updateVoucherStatById(String voucherNo, String wageId, String voucherStat, String dtlIds, boolean isIn){
		String[] dtlId = dtlIds.split(CommonConstant.COMMA_SPLIT);
		List<String> dtlIdList = new ArrayList<>();
		for(String id:dtlId){
			dtlIdList.add(id);
		}
		return this.nisWageDetailDao.updateVoucherStatById(voucherNo, wageId, voucherStat, dtlIdList, isIn);
	}
}
