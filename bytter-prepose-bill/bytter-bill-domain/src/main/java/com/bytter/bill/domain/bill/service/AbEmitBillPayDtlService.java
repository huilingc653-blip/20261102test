package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbEmitBillPayDtlDao;
import com.bytter.bill.domain.bill.entity.AbEmitBillPay;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @Title: Service
 * @Description: 票据线上兑付信息Service
 * @author onlineGenerator
 * @date 2017-11-1 16:40:01
 * @version 1.0
 *
 */
@Service("abEmitBillPayDtlService")
@Transactional
public class AbEmitBillPayDtlService extends BaseService<AbEmitBillPay, String> {
	@Autowired
	private AbEmitBillPayDtlDao abEmitBillPayDtlDao;
	/**
	 * 根据票据业务流水id获取线上兑付信息
	 * 
	 * @param aid
	 */
	public AbEmitBillPay getAbEmitBillPayByAid(String aid) {
		return abEmitBillPayDtlDao.getAbEmitBillPayByAid(aid);
	}
	/**
	 * 根据票据业务流水id删除线上兑付信息
	 * 
	 * @param aid
	 */
	public int updateValidSignByAid(String aid, String validSign, String oldValidSign, ICurrentUser userVo) {
		Map<String, Object> params = new HashMap<>();
		params.put("aid", aid);
		params.put("validSign", validSign);
		params.put("oldValidSign", oldValidSign);
		params.put("updateBy", userVo.getLoginName());
		params.put("updateName", userVo.getUsername());
		params.put("updateDate", DateUtils.getDateYMD(new Date()));
		return abEmitBillPayDtlDao.updateValidSignByAid(params);
	}
	/**
	 * 删除对象
	 * 
	 * @param entity
	 */
	public void deleteEntity(AbEmitBillPay entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 * 
	 * @param id
	 */
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 * 
	 * @param entity
	 */
	public void save(AbEmitBillPay entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public void update(AbEmitBillPay entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(AbEmitBillPay entity) {
		super.saveOrUpdate(entity);
	}
	public BaseDao<AbEmitBillPay, String> getEntityDao() {
		return abEmitBillPayDtlDao;
	}
}
