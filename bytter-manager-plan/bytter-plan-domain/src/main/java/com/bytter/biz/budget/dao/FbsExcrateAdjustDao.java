package com.bytter.biz.budget.dao;
import com.bytter.biz.budget.entity.FbsExcrateAdjust;
import com.bytter.biz.budget.vo.FbsExcrateAdjustVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Dao
 * @Description: 计划汇率调整表
 * @date 2019-08-15 10:51:48
 */
@Repository
public class FbsExcrateAdjustDao extends BaseDao<FbsExcrateAdjust, String> {
	public String getCommonSql(){
		StringBuilder sql = new StringBuilder();
		/*sql.append("select f.id as id, f.cur_id as curId, b.cur_code as curCode, b.cur_name as curName, ");
		sql.append("b.english_code as englishCode, f.refer_cur_id as referCurId, c.cur_code as referCurCode,");
		sql.append("c.english_code as referEnglishCode, c.cur_name as referCurName,f.exchange_rate as exchangeRate, ");
		sql.append("f.pre_time as preTime, f.example as example, f.rmk as rmk, f.create_by as createBy, ");
		sql.append("f.create_date as createDate, f.update_by as updateBy, f.update_date as updateDate ");
		sql.append(" from fbs_excrate_adjust f left join bt_currency b on f.cur_id = b.id ");
		sql.append(" left join bt_currency c on f.refer_cur_id = c.id where 1=1 ");*/
		sql.append("select f.id as id, f.cur_id as curId, f.refer_cur_id as referCurId, f.exchange_rate as exchangeRate, ");
		sql.append("f.pre_time as preTime, f.example as example, f.rmk as rmk, f.create_by as createBy, ");
		sql.append("f.create_date as createDate, f.update_by as updateBy, f.update_date as updateDate ");
		sql.append(" from fbs_excrate_adjust f");
		sql.append(" where 1=1 ");
		return sql.toString();
	}
	/**
	 * 根据原始货币id和目标货币id和日期查找最新一笔计划汇率调整表信息
	 * @param curId 原始货币id
	 * @param referCurId 目标货币id
	 * @param endDate 结束日期
	 * @return
	 */
	public List<FbsExcrateAdjustVO> getFbsExcrateAdjustByCurReferDate(String curId, String referCurId, Date endDate){
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		HashMap<String, Object> params = new HashMap<String, Object>();
		sql.append(" and f.cur_id =:curId and f.refer_cur_id =:referCurId ");
		if (!EmptyUtils.isEmpty(endDate)) {
			sql.append(" and bt_to_char(f.pre_time,'yyyy-MM-dd') <= :endDate ");
			params.put("endDate", DateUtils.formatDate(endDate));
		}
		sql.append(" order by f.pre_time desc");
		params.put("curId", curId);
		params.put("referCurId", referCurId);
		return listBySQLAliasToBean(sql.toString(), params, FbsExcrateAdjustVO.class);
	}
	/**
	 * 根据id查找计划汇率调整表信息
	 * @param id
	 * @return
	 */
	public FbsExcrateAdjustVO getByIdUnion(String id){
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		sql.append(" and f.id =:id ");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, FbsExcrateAdjustVO.class);
	}
	/**
	 * 查询计划汇率调整表信息
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		return getDynamicBySQL(dataTablesInput, sql.toString(), FbsExcrateAdjustVO.class, "f", false);
	}
}
