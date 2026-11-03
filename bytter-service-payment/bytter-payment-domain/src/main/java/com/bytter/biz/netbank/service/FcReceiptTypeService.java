package com.bytter.biz.netbank.service;

import com.bytter.biz.inneraccount.service.BtBankAccHandleService;
import com.bytter.biz.netbank.dao.FcReceiptTypeDao;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author linqp
 * @Description 单据类型Service
 * @date 2017.9.14
 */
@Service("fcReceiptTypeService")
@Transactional
public class FcReceiptTypeService extends BaseService<FcReceiptType, String> {
	@Autowired
	private FcReceiptTypeDao fcReceiptDao;
	@Autowired
	private BtBankAccHandleService btBankAccHandleService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FcReceiptType entity) {
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
	public void save(FcReceiptType entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FcReceiptType entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FcReceiptType entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FcReceiptType, String> getEntityDao() {
		return fcReceiptDao;
	}

	public FcReceiptType getById(String id){
		return fcReceiptDao.getById(id);
	}
	public FcReceiptType getByCode(String code){
		return fcReceiptDao.getByCode(code);
	}

	/**
	 * 获取有效的系统结算方式
	 *
	 * @return
	 */
	public List<FcReceiptType> getValidFcReceiptType() {
		return fcReceiptDao.getValidFcReceiptType();
	}

	/**
	 * 逻辑删除
	 * @param id
	 */
	public void deleteById(String id) {
		FcReceiptType fcReceiptType = this.get(id,true);
		List<BtBankAccVO> bankAccVOList = btBankAccHandleService.getBtBankAccVOByReceiptType(fcReceiptType.getReceiptCode());
		if(CollectionUtils.isNotEmpty(bankAccVOList)){
			List<String> bankAccs = bankAccVOList.stream().map(BtBankAccVO::getBankAcc).collect(Collectors.toList());
			throw new BusinessException("["+ StringUtils.join(bankAccs,",")+"]"+"银行账号已经关联了该单据类型，不允许删除");
		}

		fcReceiptType.setValidSign("0");
		this.update(fcReceiptType);
	}

	public Map<String, String> getFcReceiptTypeNameMap() {
		Map<String,String> map = new HashMap<>();
		List<FcReceiptType> validFcReceiptType = getValidFcReceiptType();
		if(EmptyUtils.isNotEmpty(validFcReceiptType)){
			for(FcReceiptType obj:validFcReceiptType){
				map.put(obj.getReceiptCode(),obj.getReceiptName());
			}
		}
		return map;
	}
}
