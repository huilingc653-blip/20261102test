package com.bytter.biz.finacial.closedeal.service;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.closedeal.dao.FcCashFlowDao;
import com.bytter.biz.finacial.closedeal.entity.FcCashFlow;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
* @Title: Service
* @Description: FC_CASH_FLOW
* @author onlineGenerator
* @date 2017-07-17 18:36:08
* @version 1.0
*
*/
@Service("fcCashFlowService")
@Transactional
public class FcCashFlowService extends BaseService<FcCashFlow,String> {
	@Autowired
	private FcCashFlowDao fcCashFlowDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcCashFlow entity){
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
 	public void save(FcCashFlow entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcCashFlow entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcCashFlow entity){
 		super.saveOrUpdate(entity);
 	}
 	public void deleteByMonth(String netId, Date startDate, Date endDate){
		Map<String, Object> params = new HashMap<>();
		params.put("netId", netId);
		params.put("startDate", DateUtils.format(startDate,"yyyy-MM-dd"));
		params.put("endDate", DateUtils.format(endDate,"yyyy-MM-dd"));
		fcCashFlowDao.deleteByMonth(params);
	}
	public BaseDao<FcCashFlow, String> getEntityDao() {
		return fcCashFlowDao;
	}
}
