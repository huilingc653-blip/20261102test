package com.bytter.guarantee.bond.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.guarantee.bond.dao.FcBondApplyDao;
import com.bytter.guarantee.bond.entity.FcBondApply;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author fusb
 *	@version 1.0
 *  @Title FcBondApplyService 
 *  @Description 保证金占用service
 *  @date 2018年10月30日
 */
@Service("fcBondApplyService")
@Transactional
public class FcBondApplyService  extends BaseService<FcBondApply, String>{
	
	@Autowired
	FcBondApplyDao fcBondApplyDao;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccServiceImpl;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	
	@Transactional(readOnly = true)
	public List<FcBondApplyDTO>  getFcBondApplyListByItemId(String itemId){
		List<FcBondApplyDTO> fcBondApplyList = fcBondApplyDao.getFcBondApplyListByItemId(itemId);
		if (fcBondApplyList.size() < 1) {
			return fcBondApplyList;
		}
		//TODO 补充相关字段
		List<String> bankAccIdList = fcBondApplyList.stream().map(FcBondApplyDTO::getBankAccId).collect(Collectors.toList());
		List<BtBankAccDTO> bankAccList = btBankAccServiceImpl.getBankAccByBankAccIds(bankAccIdList);
		if (bankAccList.size() < 1) {
			return fcBondApplyList;
		}
		Map<String, BtBankAccDTO> collect = bankAccList.stream().collect(Collectors.toMap(btBankAccDO -> btBankAccDO.getId(), Function.identity()));
		for (FcBondApplyDTO fcBondApplyDTO : fcBondApplyList) {
			fcBondApplyDTO.setBankName(collect.get(fcBondApplyDTO.getBankAccId()).getBankName());
			fcBondApplyDTO.setBankAcc(collect.get(fcBondApplyDTO.getBankAccId()).getBankAcc());
			fcBondApplyDTO.setAccName(collect.get(fcBondApplyDTO.getBankAccId()).getAccName());
		}
		return fcBondApplyList;
	}
	
	@Transactional(readOnly = true)
	public FcBondApplyDTO getFcBondApplyByItemIds(String itemId, String itemNo){
		FcBondApplyDTO fcBondApplyDTO = fcBondApplyDao.getFcBondApplyByItemIds(itemId, itemNo);
		// TODO 补充相关字段
		if(fcBondApplyDTO != null){
			redundantValues(fcBondApplyDTO);
		}
		return fcBondApplyDTO;
	}

	private void redundantValues(FcBondApplyDTO fcBondApplyDTO) {
		Optional.ofNullable(fcBondApplyDTO.getBankAccId()).ifPresent(bankAccId -> {
			BtBankAccDTO bankAccDTO = btBankAccServiceImpl.getBankAccById(bankAccId);
			Optional.ofNullable(bankAccDTO).ifPresent(bankAccDO -> {
				fcBondApplyDTO.setBankName(bankAccDO.getBankName());
				fcBondApplyDTO.setBankAcc(bankAccDO.getBankAcc());
				fcBondApplyDTO.setAccName(bankAccDO.getAccName());
				fcBondApplyDTO.setBondRate(bankAccDO.getRates());
			});
		});
		// 冗余单位
		Optional.ofNullable(fcBondApplyDTO.getCorpId())
				.flatMap(corpId -> Optional.ofNullable(corpService.get(corpId)))
				.ifPresent(corp -> fcBondApplyDTO.setCorpName(corp.getName()));
		// 冗余币种
		Optional.ofNullable(fcBondApplyDTO.getCurId())
				.flatMap(curId -> Optional.ofNullable(btCurrencyService.getObjectById(curId)))
				.ifPresent(corp -> fcBondApplyDTO.setCurName(corp.getCurName()));
		// 冗余保证金类型
		Optional.ofNullable(fcBondApplyDTO.getBailType())
				.flatMap(bailType -> Optional.ofNullable(dictService.listMap("AB_BILL_BAIL_TYPE"))
						.flatMap(dictMap -> Optional.ofNullable(dictMap.get(bailType))))
				.ifPresent(dict -> fcBondApplyDTO.setBailTypeName(dict.getName()));
	}

	@Transactional(readOnly = true)
	public BigDecimal getFcBondApplyMoneyByItemId(String itemId){
		return fcBondApplyDao.getFcBondApplyMoneyByItemId(itemId);
	}
	@Transactional(readOnly = true)
	public FcBondApplyDTO getFcBondApply(String itemId,String itemNo,String bondId){
		FcBondApplyDTO fcBondApplyDTO = fcBondApplyDao.getFcBondApply(itemId, itemNo, bondId);
		if(fcBondApplyDTO != null){
			redundantValues(fcBondApplyDTO);
		}
		return fcBondApplyDTO;
	}
	@Transactional(readOnly = true)
	public FcBondApplyDTO getFcBondApplyByParams(FcBondApplyDTO fcBondApplyDTO){
		return fcBondApplyDao.getFcBondApplyByparams(fcBondApplyDTO);
	}
	/**
	 * 新增保证金占用信息<br/>
	 * @param fcBondApply
	 */
	public void saveFcBondApply(FcBondApply fcBondApply){
		super.saveEntity(fcBondApply);
		this.getSession().flush();
	}
	
	/**
	 * 更新保证金占用的相关信息
	 * @param fcBondApply
	 */
	public void updateFcBondApply(FcBondApply fcBondApply){
		this.saveOrUpdate(fcBondApply);
		this.getSession().flush();
	}
	
	/**
	 * 删除保证金占用的相关信息
	 */
	public void deleteFcBondApply(FcBondApply fcBondApply){
		super.deleteEntity(fcBondApply);
	}
	
	@Override
	public BaseDao<FcBondApply, String> getEntityDao() {
		return fcBondApplyDao;
	}
	/**
	 * 获取挂接保证金的项目ID列表
	 * @return
	 */
	public List<String> getItemIds() {
		return fcBondApplyDao.getItemIds();
	}

	@Transactional(readOnly = true)
	public List<FcBondApplyDTO>  getFcBondApplyByItemIdsType(List<String> itemIds,String itemType){
		return fcBondApplyDao.getFcBondApplyByItemIdsType(itemIds,itemType);
	}
}
