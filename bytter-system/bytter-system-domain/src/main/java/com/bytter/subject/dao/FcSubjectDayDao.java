package com.bytter.subject.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.SubjectConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.subject.entity.FcSubjectDay;
import com.bytter.subject.vo.FcSubjectDayVo;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import com.bytter.system.api.dto.FcSubjectDayBeanDTO;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 科目日结单表
* @author onlineGenerator
* @date 2017-06-27 16:27:23
* @version 1.0
* TODO 迁移到基础模块
*/
@Repository
public class FcSubjectDayDao extends BaseDao<FcSubjectDay,String>{
	/***
	 * 科目日结单查询
	 * @param
	 * @return
	 */
	public DataTablesOutput findSubjectDayGroupBySubject( DataTablesInput dataTablesInput,List<String> corpAuth) {
		StringBuffer sql = new StringBuffer();
		/*sql.append(" select* from ( select fsd.net_id as netId , sc.name as netName , ");
		sql.append(" fsd.opt_date as optDate, fsd.cur_id as curId, bc.cur_name as curName , fs.subject_name as subjectName, ");
		sql.append(" fsd.subject_id as subjectId,fs.subject_Code as subjectCode,  BT_NVL_NUM(sum(fsd.tr_credit_cnt) ,0) as trCreditCntSum, ");
		sql.append(" BT_NVL_NUM(sum(fsd.tr_credit_money),0) as trCreDitMoneySum, BT_NVL_NUM(sum(fsd.tr_debit_cnt),0) as trDebitCntSum, ");
		sql.append(" BT_NVL_NUM(sum(fsd.tr_debit_money),0) as trDebitMoneySum, BT_NVL_NUM(sum(fsd.cash_credit_cnt),0) as cashCreditCntSum, ");
		sql.append(" BT_NVL_NUM(sum(fsd.cash_credit_money),0) as cashCreditMoneySum, BT_NVL_NUM(sum(fsd.cash_debit_cnt),0) as cashDebitCntSum, ");
		sql.append(" BT_NVL_NUM(sum(fsd.cash_debit_money),0) as cashDebitMoneySum, BT_NVL_NUM(sum(fsd.yest_balance),0) as yestBalanceSum, ");
		sql.append(" BT_NVL_NUM(sum(fsd.today_balance),0) as todayBalanceSum ");
		sql.append(" from fc_subject_day fsd inner join  fc_subject fs on fsd.subject_id = fs.id  ");
		sql.append(" inner join  sys_corp sc on fsd.net_id = sc.id inner join bt_currency bc on fsd.cur_id = bc.id "); // 条件查询出 sys_corp 的id,name; 条件查询出 fc_subject_day 的数据，拿到 fsd.cur_id ，再查 bt_currency
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "sc.id"));
		sql.append(" group by fsd.net_id, sc.name , fsd.subject_id, fs.subject_name, fs.subject_code,fsd.cur_id, fsd.opt_date , bc.cur_name ");
		sql.append(" order BY fsd.net_id , fsd.cur_id , fsd.opt_date , fsd.subject_id) fs");*/
		// fc_subject_day 新增2个冗余字段 net_name,cur_name
		sql.append(" select * from ( ");
		sql.append(" select fsd.net_id as netId , sc.name as netName, bc.cur_name as curName, ");
		sql.append(" fsd.opt_date as optDate, fsd.cur_id as curId , fs.subject_name as subjectName, ");
		sql.append(" fsd.subject_id as subjectId,fs.subject_Code as subjectCode,  BT_NVL_NUM(sum(fsd.tr_credit_cnt) ,0) as trCreditCntSum, ");
		sql.append(" BT_NVL_NUM(sum(fsd.tr_credit_money),0) as trCreDitMoneySum, BT_NVL_NUM(sum(fsd.tr_debit_cnt),0) as trDebitCntSum, ");
		sql.append(" BT_NVL_NUM(sum(fsd.tr_debit_money),0) as trDebitMoneySum, BT_NVL_NUM(sum(fsd.cash_credit_cnt),0) as cashCreditCntSum, ");
		sql.append(" BT_NVL_NUM(sum(fsd.cash_credit_money),0) as cashCreditMoneySum, BT_NVL_NUM(sum(fsd.cash_debit_cnt),0) as cashDebitCntSum, ");
		sql.append(" BT_NVL_NUM(sum(fsd.cash_debit_money),0) as cashDebitMoneySum, BT_NVL_NUM(sum(fsd.yest_balance),0) as yestBalanceSum, ");
		sql.append(" BT_NVL_NUM(sum(fsd.today_balance),0) as todayBalanceSum ");
		sql.append(" from fc_subject_day fsd ");
		sql.append(" inner join  fc_subject fs on fsd.subject_id = fs.id ");
		sql.append(" inner join  sys_corp sc on fsd.net_id = sc.id inner join bt_currency bc on fsd.cur_id = bc.id ");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "fsd.net_id"));
		sql.append(" group by fsd.net_id, fsd.net_name , fsd.subject_id, fs.subject_name, fs.subject_code,fsd.cur_id, fsd.opt_date , fsd.cur_name, sc.name, bc.cur_name ");
		sql.append(" order BY fsd.net_id , fsd.cur_id , fsd.opt_date , fsd.subject_id ");
		sql.append(" ) fs");
		return getDynamicBySQL(dataTablesInput,sql.toString(), FcSubjectDayVo.class,"", true);
	}
	
	/**
	 * 科目汇总表头查询
	 */
	public List<Map<String,Object>> getSubjectSumHead(FcSubjectDayVo fsd,List<String> corpAuth) {
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer("");
		sb.append(" select fs.net_id as NETID , fs.cur_Id as CURID , s.name as NETNAME ,c.cur_name as CURNAME , fs.opt_date as OPTDATE , ");
		// sb.append(" select fs.net_id as NETID , fs.cur_Id as CURID , fs.net_name as NETNAME ,fs.cur_name as CURNAME , fs.opt_date as OPTDATE , ");
		sb.append(" BT_NVL_NUM(sum(fs.tr_debit_money),0) as TRDEBITMONEYSUM, BT_NVL_NUM(sum(fs.tr_debit_cnt),0) as TRDEBITCNTSUM, BT_NVL_NUM(sum(fs.tr_credit_money),0) as TRCREDITMONEYSUM,BT_NVL_NUM(sum(fs.tr_credit_cnt),0) as TRCREDITCNTSUM,");
		sb.append(" BT_NVL_NUM(sum(fs.cash_debit_money),0) as CASHDEBITMONEYSUM,BT_NVL_NUM(sum(fs.cash_debit_cnt),0) as CASHDEBITCNTSUM,BT_NVL_NUM(sum(fs.cash_credit_money),0) as CASHCREDITMONEYSUM,BT_NVL_NUM(sum(fs.cash_credit_cnt),0) as CASHCREDITCNTSUM ");
		sb.append(" from fc_subject_day fs left join sys_corp s on  fs.net_id = s.id left join bt_currency c on fs.cur_id = c.id ");
		// sb.append(" from fc_subject_day fs ");
		sb.append(" where 1=1 ");
		if(EmptyUtils.isNotEmpty(fsd.getCurId()) && !" ".equals(fsd.getCurId())){
			sb.append(" and fs.cur_id = :curId ");
			params.put("curId", fsd.getCurId());
		}
		if(EmptyUtils.isNotEmpty(fsd.getNetId()) && !" ".equals(fsd.getNetId())){
			sb.append(" and fs.net_id = :netId ");
			params.put("netId", fsd.getNetId());
		}
		if(!EmptyUtils.isEmpty(fsd.getOptDate())){
			sb.append(" and fs.opt_date=:optdate ");
			params.put("optdate", fsd.getOptDate());
		}
//		sb.append(" and ").append(getSQLIn(corpAuth, 900, "s.id"));
		sb.append(" and ").append(getSQLIn(corpAuth, 900, "fs.net_id"));
		sb.append(" group by fs.net_Id,fs.cur_Id, s.name ,c.cur_name,fs.opt_date ");
		// sb.append(" group by fs.net_Id,fs.cur_Id, fs.net_name, fs.cur_name, fs.opt_date ");
		sb.append(" order by fs.net_Id , fs.cur_Id ");
		//return listBySQLAliasToBean(sb.toString(),params,FcSubjectDayVo.class);
		return listBySQL(sb.toString(), params);
	}
	
	/**
	 * 科目汇总表查询
	 */
	public Map<String, Object> findSubjectDay(Map<String,Object> pageData , int curr,List<String> corpAuth) {
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select fsd.id as id , fcs.id as subjectId,fcs.subject_code as subjectCode,fsd.net_Id as netId ,s.name as netName, fsd.cash_credit_cnt as cashCreditCnt , fsd.cash_credit_money as cashCreditMoney ,fsd.cash_debit_cnt as cashDebitCnt,");
		sb.append(" fsd.cash_debit_money as cashDebitMoney , fsd.cur_Id   as curId,c.cur_name as curName,fsd.opt_date  as optdate, fsd.tr_credit_cnt as trCreditCnt , ");
		sb.append(" fsd.tr_credit_money as trCreditMoney , fsd.tr_debit_cnt as trDebitCnt , fsd.tr_debit_money as trDebitMoney , fcs.subject_name as subjectName ");
		sb.append(" from fc_subject_day fsd inner join  fc_subject  fcs on fsd.subject_id = fcs.id " );
		sb.append(" left join sys_corp s on  fsd.net_id = s.id");
		sb.append(" left join bt_currency c on fsd.cur_id = c.id ");
		sb.append(" where 1=1 and fcs.subject_sort <> 'G' " );
		//sb.append(" and fsd.net_id = :netId and fsd.opt_date=:optdate  ");  //and fs.cur_id = :curId
		String curId = (String) pageData.get("CURID");
		String netId = (String) pageData.get("NETID");
		Date optDate = (Date) pageData.get("OPTDATE");
		if(EmptyUtils.isNotEmpty(curId)){
			sb.append(" and fsd.cur_id = :curId ");
			params.put("curId", curId);
		}
		if(EmptyUtils.isNotEmpty(netId)){
			sb.append(" and fsd.net_id = :netId ");
			params.put("netId", netId);
		}
		if(!EmptyUtils.isEmpty(optDate)){
			sb.append(" and fsd.opt_date=:optdate ");
			params.put("optdate", optDate);
		}
//		sb.append(" and ").append(getSQLIn(corpAuth, 900, "s.id"));
		sb.append(" and ").append(getSQLIn(corpAuth, 900, "fsd.net_Id"));
        sb.append(" GROUP BY fsd.net_Id, fsd.opt_date, fsd.subject_Id, fsd.id,fcs.id,fcs.subject_code,s.name,fsd.cash_credit_cnt," +
				"fsd.cash_credit_money,fsd.cash_debit_cnt,fsd.cash_debit_money,fsd.cur_Id,c.cur_name,fsd.tr_credit_cnt," +
				"fsd.tr_credit_money,fsd.tr_debit_cnt,fsd.tr_debit_money,fcs.subject_name");//增加分组过滤重复
        sb.append(" order by fsd.net_Id,fsd.opt_date,fsd.subject_Id");
		List<FcSubjectDayVo> list = listBySQLAliasToBean(sb.toString(),params,FcSubjectDayVo.class);
		Map<String, Object> result = new HashMap<String, Object>();
        result.put("head", pageData);
        result.put("curName", pageData.get("CURNAME"));
        Object obj = pageData.get("OPTDATE");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String oDate = sdf.format(obj);
        result.put("optDate",oDate);
        result.put("netId", pageData.get("NETID"));
        result.put("netName", pageData.get("NETNAME"));
        result.put("dataList", list);
        result.put("curr", curr);
		return result;
	}
	
	/**
	 * 功能介绍 : 查询月结区间内的科目日结单。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcSubjectDayBeanDTO> querySetMonthSubDay(FcMonthCloseDTO closeDTO) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select cf from FcSubjectDay cf where cf.netId = :netId ");
		hql.append(" and cf.optDate >= :startDate and cf.optDate <= :endDate order by cf.netId,cf.curId,cf.subjectId,cf.optDate ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", closeDTO.getCloseDate());
		params.put("endDate", closeDTO.getEndDate());
		params.put("netId", closeDTO.getNetCode());
		List<FcSubjectDay> subjectDays = listByHQL(hql.toString(), params);
		return copyListAndInject(subjectDays, FcSubjectDayBeanDTO.class);
	}

	/**
	 * 功能介绍 : 删除科目日结单当天数据。<br>
	 * <b>日期：</b>2017年7月14日<br>
	 *
	 * @version 1.0
	 * @author liyh
	 */
	public boolean deleteSubjectDay(FcDayCloseDTO dayClose) {
		String sql = "DELETE from fc_subject_day WHERE net_id = :netId " +
				" AND opt_date > :startDate AND opt_date <= :endDate ";
		Map<String, Object> params = getCloseParams(dayClose);
		return createSQLQuery(sql, params).executeUpdate() > 0;
	}

	private Map<String, Object> getCloseParams(FcDayCloseDTO dayClose) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", dayClose.getNetCode());
		Date closeDate = dayClose.getCloseDate();
		String string = closeDate.toString();
		//不确定其它地方传入的具体是什么类型。在 反日结 中，传到dao层 closeDate已经是 Tue Dec 19 00:00:00 CST 2023 这种格式
		//猜测之前 closeDate 可能是 String类型，但现在有出现 date类型，所以无需 parse
		if (string.contains("CST")){
			params.put("endDate", closeDate);
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parse = null;
		try {
			parse = simpleDateFormat.parse(string);
			params.put("endDate", parse);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		Date lastCloseDate = dayClose.getLastCloseDate();
		params.put("startDate", lastCloseDate);
		return params;
	}

	/**
	 * 功能介绍 : 是否存在损益类科目不为零。<br>
	 * <b>日期：</b>2017年7月17日<br>
	 *
	 * @version 1.0
	 * @author liyh
	 */
	public boolean isIncomBlanceZero(FcMonthCloseDTO closeDTO) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netCode", closeDTO.getNetCode());
		params.put("endDate", closeDTO.getEndDate());
		params.put("subjectSort", SubjectConstant.SubjectSort.SUBJECT_SORT_A.get());
		String sql = "select 1 from Fc_Subject fc,Fc_Subject_Day fd " +
				" where fc.subject_Sort = :subjectSort " +
				" and fc.id = fd.subject_id and fd.today_balance <> 0 " +
				" and fd.opt_date = :endDate and fd.net_id = :netCode ";
		return CollectionUtil.isNotEmpty(this.listBySQL(sql, params));
	}

}
