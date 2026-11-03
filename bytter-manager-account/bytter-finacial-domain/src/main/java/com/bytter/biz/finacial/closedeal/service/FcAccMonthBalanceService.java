package com.bytter.biz.finacial.closedeal.service;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.closedeal.dao.FcAccMonthBalanceDao;
import com.bytter.biz.finacial.closedeal.entity.FcAccMonthBalance;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import java.util.List;
/**
* @Title: Service
* @Description: FC_ACC_MONTH_BALANCE
* @author onlineGenerator
* @date 2017-07-17 18:38:13
* @version 1.0
*
*/
@Service("fcAccMonthBalanceService")
@Transactional
public class FcAccMonthBalanceService extends BaseService<FcAccMonthBalance,String> {
	@Autowired
	private FcAccMonthBalanceDao fcAccMonthBalanceDao;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcAccMonthBalance entity){
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
 	public void save(FcAccMonthBalance entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccMonthBalance entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcAccMonthBalance entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcAccMonthBalance, String> getEntityDao() {
		return fcAccMonthBalanceDao;
	}
	
	/**
	 * 功能介绍 : 转移月总账总量数据。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void deleteAccMonthBalance(FcMonthCloseDTO closeDTO) {
		List<String> blnctrAccList = iFcAccService.getBlnctrAccByNetId(closeDTO.getNetCode());
		fcAccMonthBalanceDao.deleteAccMonthBalance(closeDTO, blnctrAccList);
	}
}
