package com.bytter.biz.netbank.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.bytter.bill.api.bill.dto.NisBillDTO;
import com.bytter.biz.netbank.dao.NisBillDao;
import com.bytter.biz.netbank.entity.NisBill;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 付款票据关联表
 * @date 2017-05-23 15:20:33
 */
@Service("nisBillService")
@Transactional
public class NisBillService extends BaseService<NisBill, String> {
	@Autowired
	private NisBillDao nisBillDao;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(NisBill entity) {
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
	public void save(NisBill entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(NisBill entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(NisBill entity) {
		nisBillDao.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<NisBill, String> getEntityDao() {
		return nisBillDao;
	}
	/**
	 * 根据付款ID获取付款票据关联信息
	 *
	 * @param payId
	 * @return
	 */
	public String getBillIds(String payId) {
		List<NisBill> list = nisBillDao.listByPayId(payId);
		StringBuilder billIds = new StringBuilder();
		if (!EmptyUtils.isEmptyList(list)) {
			for (NisBill bill : list) {
				//排除BillId为null的情况，by qinl
				if(StringUtils.isEmpty(bill.getBillId())){
					continue;
				}
				if (EmptyUtils.isBlank(billIds.toString())) {
					billIds.append(bill.getBillId());
				} else {
					billIds.append(CommonConstant.COMMA_SPLIT).append(bill.getBillId());
				}
			}
		}
		return billIds.toString();
	}

	/**
	 * 根据票据流水id获取对应的挂接付款金额
	 *
	 * @param billIdAr
	 * @param payId
	 * @return
	 */
	public String getBillBalances(String[] billIdAr,String payId) {
		StringBuilder billBalances = new StringBuilder();
		for (String billId : billIdAr) {
			NisBill nisBill = nisBillDao.getByBillId(billId, payId);
			if (nisBill.getBalance() == null) {
				nisBill.setBalance(BigDecimal.ZERO);
			}
			if (!ObjectUtils.isEmpty(nisBill)) {
				if (EmptyUtils.isBlank(billBalances.toString())) {
					billBalances.append(nisBill.getBalance().toString());
				} else {
					billBalances.append(CommonConstant.COMMA_SPLIT).append(nisBill.getBalance().toString());
				}
			}
		}
		return billBalances.toString();
	}

	/**
	 * 根据票据流水id获取对应的挂接付款金额
	 * @param billIds
	 * @param payId
	 * @return
	 */
	public String getBillBalances(String billIds,String payId) {
		String[] billIdAr = billIds.split(CommonConstant.COMMA_SPLIT);
		return getBillBalances(billIdAr, payId);
	}

	/**
	 * 新增或更新
	 *
	 * @param payId
	 * @param billIds
	 * @return
	 */
	public void saveOrUpdate(String payId, String billIds) {
		//先删除再新增
		List<NisBill> list = nisBillDao.listByPayId(payId);
		if (!EmptyUtils.isEmptyList(list)) {
			for (NisBill bill : list) {
				this.deleteEntity(bill);
			}
		}
		String[] billIdAr = billIds.split(CommonConstant.COMMA_SPLIT);
		for (String billId : billIdAr) {
			NisBill bill = new NisBill();
			bill.setPayId(payId);
			bill.setBillId(billId);
			this.saveOrUpdate(bill);
		}
	}
	/**
	 * 新增或更新
	 *
	 * @param payId 付款单id
	 * @param billIds 票据流水id
	 * @param billBalances 票据挂接付款金额
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdate(String payId, String billIds, String billBalances) {
		//先删除再新增
		List<NisBill> list = nisBillDao.listByPayId(payId);
		if (!EmptyUtils.isEmptyList(list)) {
			for (NisBill bill : list) {
				this.deleteEntity(bill);
			}
		}
		String[] billIdAr = billIds.split(CommonConstant.COMMA_SPLIT);
		String[] billBalanceAr = billBalances.split(CommonConstant.COMMA_SPLIT);
		for (int a = 0; a < billIdAr.length; a++) {
			NisBill bill = new NisBill();
			bill.setPayId(payId);
			bill.setBillId(billIdAr[a]);
			bill.setBalance(new BigDecimal(billBalanceAr[a]));
			nisBillDao.save(bill);
			nisBillDao.flush();
			nisBillDao.clear();
		}
	}

	/**
	 * 新增或更新(同步方法)
	 * @param payId 付款单id
	 * @param billIds 票据流水id
	 * @param billBalances 票据挂接付款金额
	 */
	public void saveOrUpdateSync(String payId, String billIds, String billBalances) {
		//先删除再新增
		List<NisBill> list = nisBillDao.listByPayId(payId);
		if (!EmptyUtils.isEmptyList(list)) {
			for (NisBill bill : list) {
				this.deleteEntity(bill);
			}
		}
		String[] billIdAr = billIds.split(CommonConstant.COMMA_SPLIT);
		String[] billBalanceAr = null;
		if(billBalances != null){
			billBalanceAr = billBalances.split(CommonConstant.COMMA_SPLIT);
		}
		for (int a = 0; a < billIdAr.length; a++) {
			NisBill bill = new NisBill();
			bill.setPayId(payId);
			bill.setBillId(billIdAr[a]);
			if(billBalanceAr != null){
				bill.setBalance(new BigDecimal(billBalanceAr[a]));
			}
			this.saveOrUpdate(bill);
		}
	}
	/**
	 * 根据付款单id和票据流水id删除
	 *
	 * @param billIds 票据流水id
	 * @param payIds  付款单id
	 */
	public void delByPayAndBillId(String[] billIds, String[] payIds) {
		nisBillDao.delByPayAndBillId(billIds, payIds);
	}
	/**
	 * 根据票据流水id获取对应的挂接付款金额
	 *
	 * @param billIds
	 * @return
	 */
	public String getBillBalances(String billIds) {
		String[] billIdAr = billIds.split(CommonConstant.COMMA_SPLIT);
		StringBuilder billBalances = new StringBuilder();
		for (String billId : billIdAr) {
			NisBill nisBill = nisBillDao.getByBillId(billId);
			if (!ObjectUtils.isEmpty(nisBill)) {
				if (EmptyUtils.isBlank(billBalances.toString())) {
					billBalances.append(nisBill.getBalance().toString());
				} else {
					billBalances.append(CommonConstant.COMMA_SPLIT).append(nisBill.getBalance().toString());
				}
			}
		}
		return billBalances.toString();
	}
	
	

	/**
	 * 根据付款ID获取付款票据关联信息
	 * @param payId
	 * @return
	 */
	public List<NisBill> listByPayId(String payId){
		return nisBillDao.listByPayId(payId);
	}

	public List<NisBillDTO> listNisBillVoByPayId(String payId){
		List<NisBill> nisBillList = listByPayId(payId);
		return BeanUtil.copyToList(nisBillList,NisBillDTO.class);
	}


	/**
	 * 根据付款Id删除关联信息
	 * @param payId
	 */
	public void delByPayId(String payId) {
		nisBillDao.delByPayIds(ListUtil.toList(payId));
	}

	/**
	 * 根据付款Ids删除关联信息
	 * @param payIds
	 */
	public void delByPayIds(List<String> payIds) {
		nisBillDao.delByPayIds(payIds);
	}
}
