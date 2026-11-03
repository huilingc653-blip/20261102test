package com.bytter.biz.finacial.accbook.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.accbook.dao.FcAccbookBlnedExtDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookBlnedExt;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.vo.FcAccbookExtVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: FC_ACCBOOK_BLNED_EXT
* @author onlineGenerator
* @date 2017-06-19 11:28:35
* @version 1.0
*
*/
@Service("fcAccbookBlnedExtService")
@Transactional
public class FcAccbookBlnedExtService extends BaseService<FcAccbookBlnedExt,String> {
	@Autowired
	private FcAccbookBlnedExtDao fcAccbookBlnedExtDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcAccbookBlnedExt entity){
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
 	public void save(FcAccbookBlnedExt entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccbookBlnedExt entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcAccbookBlnedExt entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcAccbookBlnedExt, String> getEntityDao() {
		return fcAccbookBlnedExtDao;
	}
	
	/**
	 * 功能介绍 : 获取附加账务信息。<br>
	 * <b>日期：</b>2017年6月22日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public FcAccbookBlnedExt getBookBlnedExtByBookId(String bookId) {
		return fcAccbookBlnedExtDao.getBookBlnedExtByBookId(bookId);
	}
	
	/**
	 * 功能介绍 : 获取账务扩展信息。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<FcAccbookBlnedExt> getBookExtByBusGlobalId(String busGlobalId) {
		return fcAccbookBlnedExtDao.getBookExtByBusGlobalId(busGlobalId);
	}
	
	@Transactional(readOnly = true)
	public FcAccbookExtVO getBookExtVOByBookId(String bookId) {
		return fcAccbookBlnedExtDao.getBookExtVOByBookId(bookId);
	}
}
