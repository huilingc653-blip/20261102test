package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.NisPersonnelDetail;
import com.bytter.biz.netbank.vo.NisPersonnelDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 职工信息维护表
 * @date 2017-05-26 09:31:22
 */
@Repository
public class NisPersonnelDetailDao extends BaseDao<NisPersonnelDetail, String> {
	/**
	 * 根据id查找职工信息
	 *
	 * @param id
	 * @return
	 */
	public NisPersonnelDetail getById(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.id, p.work_no, p.user_name, p.idcard, p.sex, p.dept_name, p.mobile, p.corp_id, p.bank_acc,");
		sql.append(" p.acc_name, p.bif_code, p.bank_code, p.bank_name, p.card_type, p.prov, p.city , p.rmk from nis_personnel_detail p");
		sql.append(" where p.id = :id ");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, NisPersonnelDetail.class);
	}
	/**
	 * 查询职工信息列表
	 *
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<NisPersonnelDetailVO> getDataTablesUnion(DataTablesInput input, String corpAuthSql, Map params) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.id, p.work_no, p.user_name, p.idcard, p.sex, p.dept_name, p.mobile, p.corp_id, p.bank_acc,");
		sql.append(" p.acc_name, p.bif_code, p.bank_code, p.bank_name, p.card_type, p.prov, p.city from nis_personnel_detail p");
		sql.append(" where 1 = 1 ");
		sql.append(" and ").append(corpAuthSql);
		return getDynamicBySQL(input, sql.toString(), NisPersonnelDetailVO.class, "p", false, params);
	}
	/**
	 * 根据id查找职工信息
	 *
	 * @param id
	 * @return
	 */
	public NisPersonnelDetailVO getByUnion(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.id, p.work_no, p.user_name, p.idcard, p.sex, p.dept_name, p.mobile, p.corp_id, p.bank_acc,");
		sql.append(" p.acc_name, p.bif_code, p.bank_code, p.bank_name, p.card_type, p.prov, p.city, p.rmk from nis_personnel_detail p");
//		sql.append(" left join bis_bif_init f on p.bif_code = f.id ");
		sql.append(" where p.id = :id ");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, NisPersonnelDetailVO.class);
	}
	/**
	 * 根据单位统计职工信息
	 * @param corpId
	 * @return
	 */
	public int getPersonnelCountByCorpId(String corpId){
		String sql="select count(*) from nis_personnel_detail where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	/**
	 * 根据ID删除数据
	 *
	 * @param id
	 */
	public void deleteById(String id) {
		String sql = " delete from nis_personnel_detail where id=:id ";
		HashMap<String, Object> params = new HashMap<>();
		params.put("id", id);
		this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
	}
}
