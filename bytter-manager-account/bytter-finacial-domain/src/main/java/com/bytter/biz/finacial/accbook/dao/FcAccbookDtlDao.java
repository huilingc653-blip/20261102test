package com.bytter.biz.finacial.accbook.dao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.vo.FcAccbookDtlVO;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_ACCBOOK_DTL
* @author onlineGenerator
* @date 2017-05-31 15:02:54
* @version 1.0
*
*/
@Repository
public class FcAccbookDtlDao extends BaseDao<FcAccbookDtl,String>{
	
	/**
	 * 功能介绍 : 根据账务流水ID获取流水明细。<br>
	 * <b>日期：</b>2017年6月7日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcAccbookDtl> getAccbookDtlsByBookId(String bookId) {
//		String hql = "select t,item.itemName as cashFluxName from FcAccbookDtl t left join FcCashItem item on t.cashFlux = item.id where t.bookId = :bookId order by t.lineNo";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookId);
//		return listByHQL(hql, params);
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.* ,item.id cashFlux,item.item_name cashFluxName from FC_ACCBOOK_DTL t left join FC_CASH_ITEM item on t.cash_flux = item.id where t.book_id = :bookId  order by t.line_No");
		return listBySQLAliasToBean(sql.toString(), params, FcAccbookDtl.class);
	}
	
	/**
	 * 功能介绍 : 获取账务明细信息。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcAccbookDtl> getBookDtlsByBusGlobalId(String busGlobalId) {
		String hql = " select t from FcAccbookDtl t where t.bookId in (select b.id from "
				+ " FcAccbook b where b.busGlobalId = :busGlobalId ) order by t.lineNo asc";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("busGlobalId", busGlobalId);
		return listByHQL(hql, params);
	}
	
	/**
	 * 功能介绍 : 根据账务流水ID获取流水详细明细。<br>
	 * <b>日期：</b>2017年6月20日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcAccbookDtlVO> getPageDtlsByBookId(String bookId) {
		StringBuilder sql = new StringBuilder();
		/*sql.append(" select a.id as id,a.book_id as book_id,a.line_no as line_no,a.summary_content as summary_content, ");
		sql.append(" a.account as account,b.acc_name as acc_name,c.subject_name as subject_name,a.debit_money as debit_money, ");
		sql.append(" a.credit_money as credit_money,a.balance as balance,a.orig_voucher_no as orig_voucher_no,");
		sql.append(" a.cash_flux cashFlux, item.item_name cashFluxName, ");
		sql.append(" a.orig_voucher_id as orig_voucher_id, c.subject_code as subjectCode ");
		sql.append(" from fc_accbook_dtl a  ");
		sql.append("      inner join fc_acc b ");
		sql.append("            on a.account = b.blnctr_acc");
		sql.append("      inner join fc_subject c ");
		sql.append("            on b.subject_id = c.id ");
		sql.append("      left join FC_CASH_ITEM item ");
		sql.append("            on a.cash_flux = item.id");
		sql.append(" where 1 = 1 ");
		sql.append(" and a.book_id = :bookId ");
		sql.append(" order by a.line_no ");*/
		sql.append(" select a.id as id,a.book_id as book_id,a.line_no as line_no,a.summary_content as summary_content, ");
		sql.append(" a.account as account,a.acc_name as acc_name,a.subject_name as subject_name,a.debit_money as debit_money, ");
		sql.append(" a.credit_money as credit_money,a.balance as balance,a.orig_voucher_no as orig_voucher_no,");
		sql.append(" a.cash_flux cashFlux, item.item_name cashFluxName, ");
		sql.append(" a.orig_voucher_id as orig_voucher_id, a.sys_subject_code as subjectCode ");
		sql.append(" from fc_accbook_dtl a  "); // fc_accbook_dtl 冗余 acc_name、subject_name、subject_code
		sql.append(" left join FC_CASH_ITEM item ");
		sql.append(" on a.cash_flux = item.id");
		sql.append(" where 1 = 1 ");
		sql.append(" and a.book_id = :bookId ");
		sql.append(" order by a.line_no ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookId);
		return listBySQLAliasToBean(sql.toString(), params, FcAccbookDtlVO.class);
	}
	/**
	 * 功能介绍 : 根据账务流水ID获取流水详细明细--资金池。<br>
	 * <b>日期：</b>2017年6月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcAccbookDtlDTO> getPoolPageDtlsByBookId(String bookId) {
		StringBuilder sql = new StringBuilder();
		/*sql.append(" select a.id as id,a.book_id as book_id,a.line_no as line_no,a.summary_content as summary_content, ")
				.append(" a.account as account,b.acc_name as acc_name,c.subject_name as subject_name,a.debit_money as debit_money, ")
				.append(" a.credit_money as credit_money,a.balance as balance,a.orig_voucher_no as orig_voucher_no,")
				.append(" a.orig_voucher_id as orig_voucher_id, c.subject_code as subjectCode ")
				.append(" from fc_accbook_dtl a, fc_acc b, fc_subject_pool c ")
				.append(" where a.account = b.blnctr_acc")
				.append(" and b.subject_id = c.id ")
				.append(" and a.book_id = :bookId ")
				.append(" order by a.line_no ");*/
		// fc_accbook_dtl 冗余 acc_name、subject_name、subject_code
		sql.append(" select a.id as id,a.book_id as book_id,a.line_no as line_no,a.summary_content as summary_content, ")
				.append(" a.account as account,a.acc_name as acc_name,a.subject_name as subject_name,a.debit_money as debit_money, ")
				.append(" a.credit_money as credit_money,a.balance as balance,a.orig_voucher_no as orig_voucher_no,")
				.append(" a.orig_voucher_id as orig_voucher_id, a.subject_code as subjectCode ")
				.append(" from fc_accbook_dtl a ")
				.append(" where 1=1")
				.append(" and a.book_id = :bookId ")
				.append(" order by a.line_no ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookId);
		return listBySQLAliasToBean(sql.toString(), params, FcAccbookDtlDTO.class);
	}
	/**
	 * 通过id获取
	 * @param params
	 * @return
	 */
	public FcAccbookDtl getFcAccbookDtlById(Map<String, Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append("select * from FC_ACCBOOK_DTL where id = :id ");
		return getBySQLAliasToBean(sql.toString(), params, FcAccbookDtl.class);
	}
	/**
	 * 更新现金流项目
	 * @param params
	 */
	public void updateCashFlux(Map<String,Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append(" update FC_ACCBOOK_DTL set CASH_FLUX = :cashflux where id = :id");
		createSQLQuery(sql.toString(), params).executeUpdate();
	}

	public List<FcAccbookDtl> getFcAccbookDtlByNetId(String netId) {
		String sql = "select * from fc_accbook_dtl where net_id = :netId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		return listBySQLAliasToBean(sql, params, FcAccbookDtl.class);
	}
}
