package com.bytter.subject.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.subject.entity.FcInstrate;
import com.bytter.system.api.dto.FcInstrateDTO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* @Title: Dao
* @Description: 科目利率表
* @author onlineGenerator
* @date 2017-05-18 15:45:32
* @version 1.0
*
*/
@Repository
public class FcInstrateDao extends BaseDao<FcInstrate,String> {
	/**
	 * 获取科目利率
	 * @param
	 * @return
	 */
	public DataTablesOutput fcInstrateList(DataTablesInput input){
		StringBuilder sql=new StringBuilder();
		sql.append("select fc.id as id,fc.subject_code as subjectCode,fc.instrt_type as instrtType,");
		sql.append("fc.old_instrate as oldInstrate,fc.interest_rate as interestRate,fc.exe_date as exeDate,fc.end_unit as endUnit,");
		sql.append("fc.cur_code as curId,fc.status as status,fc.cur_code as curCode,fc.subject_code as subjectId ");
		sql.append("from FC_INSTRATE fc ");
		return dealDynamicBySQL(input,sql.toString(), FcInstrateDTO.class,"",true);
	}
	
	/**
	 * 根据id获取科目利率详情
	 * @param id
	 * @return
	 */
	public FcInstrateDTO getfcInstrate(String id){
		StringBuilder sql=new StringBuilder();
		sql.append("select fc.id as id,fc.subject_code as subjectCode,fc.instrt_type as instrtType,");
		sql.append("fc.old_instrate as oldInstrate,fc.interest_rate as interestRate,fc.exe_date as exeDate,fc.end_unit as endUnit,");
		sql.append("fc.status as status,fc.subject_code as subjectId,fc.cur_code as curId  ");
		sql.append("from FC_INSTRATE fc ");
//		sql.append("left join bt_currency bt on fc.cur_code=bt.id ");
		sql.append("where fc.id = :id");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(),params, FcInstrateDTO.class);
	}
	
	/**
	 * 功能介绍 : 获取最近一次科目利率调整记录。<br>
	 * <b>日期：</b>2017年9月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public FcInstrate getLastInstrate(String subjectCode, String curCode, String instrtType, String endUnit) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select t from FcInstrate t where t.subjectCode = :subjectCode ");
		sql.append(" and t.curCode = :curCode and t.instrtType = :instrtType and t.endUnit = :endUnit order by exeDate desc");
		params.put("subjectCode", subjectCode);
		params.put("curCode", curCode);
		params.put("instrtType", instrtType);
		params.put("endUnit", endUnit);
		List<FcInstrate> instrates = listByHQL(sql.toString(), params);
		if (!EmptyUtils.isEmptyList(instrates)) {
			return instrates.get(0);
		}
		return null;
	}
	
	/**
	 * 判断当前科目利率执行日期是否有存在数据
	 * @param subjectCode
	 * @param curCode
	 * @param instrtType
	 * @param endUnit
	 * @param exeDate
	 * @return
	 */
	public boolean isExistInstrate(String subjectCode, String curCode, String instrtType, String endUnit, Date exeDate, String id){
		HashMap<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select 1 from FC_INSTRATE t where t.subject_code = :subjectCode ");
		sql.append(" and t.cur_code = :curCode and t.instrt_type = :instrtType and t.end_unit = :endUnit ");
		sql.append(" and t.exe_date >=BT_TO_DATE(:exeDate, 'YYYY-MM-DD') ");
		sql.append(" and t.id <> :id and t.status=1 ");
		params.put("subjectCode", subjectCode);
		params.put("curCode", curCode);
		params.put("instrtType", instrtType);
		params.put("endUnit", endUnit);
		params.put("exeDate", DateUtils.date2Str(exeDate,DateUtils.date_sdf()));
		params.put("id", id);
		List instrates = createSQLQuery(sql.toString(), params).list();
		if (!EmptyUtils.isEmptyList(instrates)) {
			return true;
		}
		return false;
	}
	
}
