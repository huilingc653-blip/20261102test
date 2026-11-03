package com.bytter.biz.finacial.accbook.dao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookBlnedExt;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.vo.FcAccbookExtVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_ACCBOOK_BLNED_EXT
* @author onlineGenerator
* @date 2017-06-19 11:28:35
* @version 1.0
*
*/
@Repository
public class FcAccbookBlnedExtDao extends BaseDao<FcAccbookBlnedExt,String>{
	
	/**
	 * 功能介绍 : 获取附加账务信息。<br>
	 * <b>日期：</b>2017年6月22日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public FcAccbookBlnedExt getBookBlnedExtByBookId(String bookId) {
		String hql = " select t from FcAccbookBlnedExt t where t.bookId = :bookId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookId);
		List<FcAccbookBlnedExt> list = listByHQL(hql, params);
		if (null != list && list.size() > 0 ) {
			return list.get(0);
		}
		return null;
		
	}
	
	/**
	 * 功能介绍 : 获取账务扩展信息。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcAccbookBlnedExt> getBookExtByBusGlobalId(String busGlobalId) {
		String hql = " select t from FcAccbookBlnedExt t where t.bookId in (select b.id from "
				+ " FcAccbook b where b.busGlobalId = :busGlobalId ) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("busGlobalId", busGlobalId);
		return listByHQL(hql, params);
	}
	
	public FcAccbookExtVO getBookExtVOByBookId(String bookId) {
		String hql = " select t from FcAccbookBlnedExt t where t.bookId = :bookId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookId);
		List<FcAccbookExtVO> list = listByHQLAliasToBean(hql, params, FcAccbookExtVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
		
	}
}
