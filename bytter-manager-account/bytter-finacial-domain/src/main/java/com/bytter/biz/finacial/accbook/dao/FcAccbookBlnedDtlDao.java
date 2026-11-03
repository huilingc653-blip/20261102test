package com.bytter.biz.finacial.accbook.dao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookBlnedDtl;
import com.bytter.core.base.BaseDao;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * @Title: Dao
 * @Description: FC_ACCBOOK_BLNED_DTL
 * @author onlineGenerator
 * @date 2017-06-19 11:28:25
 * @version 1.0
 *
 */
@Repository
public class FcAccbookBlnedDtlDao extends BaseDao<FcAccbookBlnedDtl, String> {
	/**
	 * 功能介绍 : 根据账务流水ID获取已日结流水明细。<br>
	 * <b>日期：</b>2017年6月7日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcAccbookBlnedDtl> getAccbookDtlsByBookId(String bookId) {
		String hql = "  from FcAccbookBlnedDtl t where t.bookId = :bookId order by t.lineNo";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookId);
		return listByHQL(hql, params);
	}
	/**
	 * 更新现金流项目
	 * @param params
	 */
	public void updateCashFlux(Map<String,Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append(" update FC_ACCBOOK_BLNED_DTL set CASH_FLUX = :cashflux where id = :id");
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	/*public String getAccountByBookId(String bookId) {
		String sql = " select account from fc_accbook_blned_dtl where book_id = :bookId";
		HashMap<String, String> params = new HashMap<>();
		params.put("bookId",bookId);
		SQLQuery sqlQuery = createSQLQuery(sql, params);
		return sqlQuery.uniqueResult().toString();
	}*/
}
