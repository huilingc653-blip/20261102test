package com.bytter.biz.finacial.interest.service;
import com.bytter.biz.finacial.interest.entity.FcLoanBalanceDtl;
import com.bytter.biz.finacial.interest.dao.FcLoanBalanceDtlDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
* @Title: Service
* @Description: FC_LOAN_BALANCE_DTL
* @author onlineGenerator
* @date 2017-08-26 15:09:34
* @version 1.0
*
*/
@Service("fcLoanBalanceDtlService")
@Transactional
public class FcLoanBalanceDtlService extends BaseService<FcLoanBalanceDtl,String> {
	@Autowired
	private FcLoanBalanceDtlDao fcLoanBalanceDtlDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcLoanBalanceDtl entity){
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
	* @param entity
	*/
 	public void save(FcLoanBalanceDtl entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcLoanBalanceDtl entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcLoanBalanceDtl entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcLoanBalanceDtl, String> getEntityDao() {
		return fcLoanBalanceDtlDao;
	}
	
	/**
	 * <b>功能介绍：</b>根据还款ID查找所有对应还款数据<br>
	 * <b>日期：</b>2017年9月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param repayId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<FcLoanBalanceDtl> getFcLoanBalanceDtlByLoanRepayId(String repayId){
		return fcLoanBalanceDtlDao.getFcLoanBalanceDtlByLoanRepayId(repayId);
	}
}
