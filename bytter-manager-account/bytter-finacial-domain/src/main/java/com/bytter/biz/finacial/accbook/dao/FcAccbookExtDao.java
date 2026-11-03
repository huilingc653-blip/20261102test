package com.bytter.biz.finacial.accbook.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.vo.FcAccbookExtVO;
import com.bytter.core.base.BaseDao;
/**
* @Title: Dao
* @Description: FC_ACCBOOK_EXT
* @author onlineGenerator
* @date 2017-05-31 15:03:10
* @version 1.0
*
*/
@Repository
public class FcAccbookExtDao extends BaseDao<FcAccbookExt,String>{
	
	/**
	 * 功能介绍 : 获取账务扩展信息。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public FcAccbookExt getBookExtByBookId(String bookId) {
		String hql = " select t from com.bytter.biz.finacial.accbook.entity.FcAccbookExt t where t.bookId = :bookId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookId);
		List<FcAccbookExt> list = listByHQL(hql, params);
		if (null != list && list.size() > 0) {
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
	public List<FcAccbookExt> getBookExtByBusGlobalId(String busGlobalId) {
		String hql = " select t from FcAccbookExt t where t.bookId in (select b.id from "
				+ " FcAccbook b where b.busGlobalId = :busGlobalId ) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("busGlobalId", busGlobalId);
		return listByHQL(hql, params);
	}
	
	public FcAccbookExtVO getBookExtVOByBookId(String bookId) {
		String hql = " from FcAccbookExt t where t.bookId = :bookId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookId);
		List<FcAccbookExtVO> list = listByHQLAliasToBean(hql, params, FcAccbookExtVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
