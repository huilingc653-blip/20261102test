package com.bytter.biz.finacial.accbook.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.accbook.dao.FcAccbookExtDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.vo.FcAccbookExtVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: FC_ACCBOOK_EXT
* @author onlineGenerator
* @date 2017-05-31 15:03:10
* @version 1.0
*
*/
@Service("fcAccbookExtService")
@Transactional
public class FcAccbookExtService extends BaseService<FcAccbookExt,String> {
	@Autowired
	private FcAccbookExtDao fcAccbookExtDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcAccbookExt entity){
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
 	public void save(FcAccbookExt entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccbookExt entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcAccbookExt entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcAccbookExt, String> getEntityDao() {
		return fcAccbookExtDao;
	}
	
	/**
	 * 功能介绍 : 获取账务扩展信息。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public FcAccbookExt getBookExtByBookId(String bookId) {
		return fcAccbookExtDao.getBookExtByBookId(bookId);
	}
	
	/**
	 * 功能介绍 : 获取账务扩展信息。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<FcAccbookExt> getBookExtByBusGlobalId(String busGlobalId) {
		return fcAccbookExtDao.getBookExtByBusGlobalId(busGlobalId);
	}
	
	@Transactional(readOnly = true)
	public FcAccbookExtVO getBookExtVOByBookId(String bookId) {
		return fcAccbookExtDao.getBookExtVOByBookId(bookId);
	}
}
