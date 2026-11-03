package com.bytter.biz.finacial.accbook.service;
import com.bytter.biz.finacial.accbook.dao.FcAccbookBlnedDtlDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookBlnedDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Service
* @Description: FC_ACCBOOK_BLNED_DTL
* @author onlineGenerator
* @date 2017-06-19 11:28:25
* @version 1.0
*
*/
@Service("fcAccbookBlnedDtlService")
@Transactional
public class FcAccbookBlnedDtlService extends BaseService<FcAccbookBlnedDtl,String> {
	@Autowired
	private FcAccbookBlnedDtlDao fcAccbookBlnedDtlDao;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(FcAccbookBlnedDtl entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	@Override
	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(FcAccbookBlnedDtl entity){
		FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(entity.getAccount());
		entity.setSubjectId(fcAccDTO.getSubjectId());
		entity.setSubjectName(fcAccDTO.getSubjectName());
		entity.setBankCode(fcAccDTO.getBankCode());
		entity.setNetId(fcAccDTO.getNetId());
		entity.setBalanceWay(fcAccDTO.getBalanceWay());
		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccbookBlnedDtl entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(FcAccbookBlnedDtl entity){
		FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(entity.getAccount());
		entity.setSubjectId(fcAccDTO.getSubjectId());
		entity.setSubjectName(fcAccDTO.getSubjectName());
		entity.setBankCode(fcAccDTO.getBankCode());
		entity.setNetId(fcAccDTO.getNetId());
		entity.setBalanceWay(fcAccDTO.getBalanceWay());
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<FcAccbookBlnedDtl, String> getEntityDao() {
		return fcAccbookBlnedDtlDao;
	}
	/**
	 * 功能介绍 : 根据账务流水ID获取已日结流水明细。<br>
	 * <b>日期：</b>2017年6月7日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public List<FcAccbookBlnedDtl> getAccbookDtlsByBookId(String bookId) {
		return fcAccbookBlnedDtlDao.getAccbookDtlsByBookId(bookId);
	}
	/**
	 *更新现金流项目
	 * @param id
	 * @param cashflux
	 */
	public void updateCashFlux(String id,String cashflux){
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("cashflux", cashflux);
		fcAccbookBlnedDtlDao.updateCashFlux(params);
	}
	/*public String getAccountByBookId(String bookId) {
		return fcAccbookBlnedDtlDao.getAccountByBookId(bookId);
	}*/
}
