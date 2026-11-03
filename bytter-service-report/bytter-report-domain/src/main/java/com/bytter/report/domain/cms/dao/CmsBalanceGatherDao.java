package com.bytter.report.domain.cms.dao;

import com.bytter.report.domain.cms.entity.CmsLoanInfo;
import com.bytter.report.domain.cms.vo.CmsBalanceGatherVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 余额汇总表查询
 * @author lvwl
 */
@Repository
public class CmsBalanceGatherDao extends BaseDao<CmsLoanInfo,String>{

	/**获取贷款sql*/
	private StringBuilder getLoanSql(CmsBalanceGatherVO vo,List<String> corpIds){
		//查询列
		StringBuilder selectFrom = new StringBuilder();
		selectFrom.append(" select '01' as loan_sign,BT_NVL_CHAR(beformTable.corp_id,earningDefrayTable.corp_id) as corp_id,BT_NVL_CHAR(beformTable.cur_id,earningDefrayTable.cur_id) as cur_id,");
		selectFrom.append(" BT_NVL_NUM(beformTable.beform_money,0) as beform_money,BT_NVL_NUM(earningDefrayTable.earning_money,0) as earning_money,BT_NVL_NUM(earningDefrayTable.defray_money,0) as defray_money from ");

		//发放单的期初值
		StringBuilder beformTableSql = new StringBuilder();
		beformTableSql.append(" (select info.corp_id as corp_id,info.cur_code as cur_id,BT_NVL_NUM(sum(send.loan_money),0) - BT_NVL_NUM(sum(repaySum.repay_money),0) as beform_money");
		beformTableSql.append(" from cms_loan_info info inner join cms_loan_send send on send.loan_code = info.bill_code");
		beformTableSql.append(" left join (select repay.send_code,sum(repay.repay_money) as repay_money from cms_loan_repay repay where repay.status > 95");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			beformTableSql.append(" and repay.repay_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");   // 还款日期
		}
		beformTableSql.append(" group by repay.send_code) repaySum on repaySum.send_code = send.bill_code where send.status > 95 and info.status > 95");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			beformTableSql.append(" and send.loan_date < BT_TO_DATE(:startDate,'yyyy-MM-dd')");   // 发放日期
			beformTableSql.append(" and send.repay_date > BT_TO_DATE(:startDate,'yyyy-MM-dd') ");    // 发放规定的还款日期要大于我们查询的起始日期
		}
		//if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){
			//beformTableSql.append(" and info.corp_id in (:corpId)");
			beformTableSql.append(" and ").append(getSQLIn(corpIds, 900, "info.corp_id"));
		//}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			beformTableSql.append(" and info.cur_code in (:curId)");
		}
		beformTableSql.append(" group by info.corp_id,info.cur_code) beformTable ");

		//收入支出查询列
		StringBuilder earningDefraySelectFrom = new StringBuilder();    // 查询出时间段内，还款总金额defray_money，发放总金额earning_money
		earningDefraySelectFrom.append(" select BT_NVL_CHAR(earningTable.corp_id,defrayTable.corp_id) as corp_id,BT_NVL_CHAR(earningTable.cur_id,defrayTable.cur_id) as cur_id,BT_NVL_NUM(earningTable.earning_money,0) as earning_money,BT_NVL_NUM(defrayTable.defray_money,0) as defray_money from ");

		//收入sql
		StringBuilder earningTableSql = new StringBuilder();
		earningTableSql.append(" (select info.corp_id as corp_id,info.cur_code as cur_id,BT_NVL_NUM(sum(send.loan_money),0) as earning_money from cms_loan_info info");  // 时间段发放总金额
		earningTableSql.append(" inner join cms_loan_send send on send.loan_code = info.bill_code where send.status > 95 and info.status > 95 ");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			earningTableSql.append(" and send.loan_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");  // 大于等于发放日期时间段初
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			earningTableSql.append(" and send.loan_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");     // 小于等于发放日期时间段末
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){
			//earningTableSql.append(" and info.corp_id in (:corpId)");
			earningTableSql.append(" and ").append(getSQLIn(corpIds, 900, "info.corp_id"));
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			earningTableSql.append(" and info.cur_code in (:curId)");
		}
		earningTableSql.append(" group by info.corp_id,info.cur_code) earningTable ");

		//支出sql
		StringBuilder defrayTableSql = new StringBuilder();
		defrayTableSql.append(" (select info.corp_id as corp_id,info.cur_code as cur_id,sum(repay.repay_money) as defray_money from cms_loan_info info inner join cms_loan_send send on send.loan_code = info.bill_code ");
		defrayTableSql.append(" inner join cms_loan_repay repay on repay.send_code = send.bill_code where repay.status > 95 and info.status > 95 ");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			defrayTableSql.append(" and repay.repay_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");  // 大于等于还款日期时间段初
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			defrayTableSql.append(" and repay.repay_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");    // 小于等于还款日期时间端末
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){
			//defrayTableSql.append(" and info.corp_id in (:corpId)");
			defrayTableSql.append(" and ").append(getSQLIn(corpIds, 900, "info.corp_id"));
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			defrayTableSql.append(" and info.cur_code in (:curId)");
		}
		defrayTableSql.append(" group by info.corp_id,info.cur_code) defrayTable ");

		//使用 left join union right join 代替 full outer join    // 将将还款总金额以及发放总金额的sql查询条件组合起来
		StringBuilder earningDefrayTableSql = new StringBuilder();
		earningDefrayTableSql.append(" (");
		earningDefrayTableSql.append(earningDefraySelectFrom);
		earningDefrayTableSql.append(earningTableSql);
		earningDefrayTableSql.append(" left join ");
		earningDefrayTableSql.append(defrayTableSql);
		earningDefrayTableSql.append(" on defrayTable.corp_id = earningTable.corp_id and defrayTable.cur_id = earningTable.cur_id");
		earningDefrayTableSql.append(" union ");
		earningDefrayTableSql.append(earningDefraySelectFrom);
		earningDefrayTableSql.append(earningTableSql);
		earningDefrayTableSql.append(" right join ");
		earningDefrayTableSql.append(defrayTableSql);
		earningDefrayTableSql.append(" on defrayTable.corp_id = earningTable.corp_id and defrayTable.cur_id = earningTable.cur_id");
		earningDefrayTableSql.append(" ) earningDefrayTable ");

		//使用 left join union right join 代替 full outer join   将期初贷款余额，时间段内还款总金额，时间段内发放总金额的查询sql组合起来
		StringBuilder sql = new StringBuilder();
		sql.append(selectFrom);
		sql.append(beformTableSql);
		sql.append(" left join ");
		sql.append(earningDefrayTableSql);
		sql.append(" on earningDefrayTable.corp_id = beformTable.corp_id and earningDefrayTable.cur_id = beformTable.cur_id");
		sql.append(" union ");
		sql.append(selectFrom);
		sql.append(beformTableSql);
		sql.append(" right join ");
		sql.append(earningDefrayTableSql);
		sql.append(" on earningDefrayTable.corp_id = beformTable.corp_id and earningDefrayTable.cur_id = beformTable.cur_id");

		return sql;
	}
	/**银行借款*/
	private StringBuilder getProvideSql(CmsBalanceGatherVO vo,List<String> corpIds){
		//查询列
		StringBuilder selectFrom = new StringBuilder();
		selectFrom.append(" select '02' as loan_sign,BT_NVL_CHAR(beformTable.corp_id,earningDefrayTable.corp_id) as corp_id,BT_NVL_CHAR(beformTable.cur_id,earningDefrayTable.cur_id) as cur_id,");
		selectFrom.append(" BT_NVL_NUM(beformTable.beform_money,0) as beform_money,BT_NVL_NUM(earningDefrayTable.earning_money,0) as earning_money,BT_NVL_NUM(earningDefrayTable.defray_money,0) as defray_money from ");

		//发放单的期初值
		StringBuilder beformTableSql = new StringBuilder();
		beformTableSql.append(" (select info.corp_id as corp_id,info.cur_id as cur_id,BT_NVL_NUM(sum(send.money),0) - BT_NVL_NUM(sum(repaySum.repay_money),0) as beform_money from ");
		beformTableSql.append(" cms_provide_loan_info info inner join cms_provide_loan_send send on send.loan_code = info.bill_code left join (select repay.send_code,sum(repay.repay_money) as repay_money from cms_provide_loan_repay repay where repay.status > 95 ");
		// 小于还款日期
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			beformTableSql.append(" and repay.repay_date < BT_TO_DATE(:startDate,'yyyy-MM-dd')");
		}
		beformTableSql.append(" group by repay.send_code ) repaySum on repaySum.send_code = send.bill_code where send.status > 95 and info.status > 95 ");
		// 小于发放日期
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			beformTableSql.append(" and send.send_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
			beformTableSql.append(" and send.repay_date > BT_TO_DATE(:startDate,'yyyy-MM-dd') ");    // 发放规定的还款日期要大于我们查询的起始日期
		}
		//if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){
			//beformTableSql.append(" and info.corp_id in (:corpId)");
			beformTableSql.append(" and ").append(getSQLIn(corpIds, 900, "info.corp_id"));
		//}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			beformTableSql.append(" and info.cur_id in (:curId)");
		}
		beformTableSql.append(" group by info.corp_id,info.cur_id) beformTable ");

		//收入支出查询列
		StringBuilder earningDefraySelectFrom = new StringBuilder();
		earningDefraySelectFrom.append(" select BT_NVL_CHAR(earningTable.corp_id,defrayTable.corp_id) as corp_id,BT_NVL_CHAR(earningTable.cur_id,defrayTable.cur_id) as cur_id,BT_NVL_NUM(earningTable.earning_money,0) as earning_money,BT_NVL_NUM(defrayTable.defray_money,0) as defray_money from ");

		//收入sql
		StringBuilder earningTableSql = new StringBuilder();
		earningTableSql.append(" (select info.corp_id as corp_id,info.cur_id as cur_id,BT_NVL_NUM(sum(send.money),0) as earning_money from cms_provide_loan_info info inner join cms_provide_loan_send send on send.loan_code = info.bill_code");
		earningTableSql.append(" where send.status > 95 and info.status > 95 ");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			earningTableSql.append(" and send.send_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");   // 时间段内发放的金额
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			earningTableSql.append(" and send.send_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){
			//earningTableSql.append(" and info.corp_id in (:corpId)");
			earningTableSql.append(" and ").append(getSQLIn(corpIds, 900, "info.corp_id"));
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			earningTableSql.append(" and info.cur_id in (:curId)");
		}
		earningTableSql.append(" group by info.corp_id,info.cur_id ) earningTable ");

		//支出sql
		StringBuilder defrayTableSql = new StringBuilder();
		defrayTableSql.append(" (select info.corp_id as corp_id,info.cur_id as cur_id,sum(repay.repay_money) as defray_money from cms_provide_loan_info info inner join cms_provide_loan_send send on send.loan_code = info.bill_code");
		defrayTableSql.append(" inner join cms_provide_loan_repay repay on repay.send_code = send.bill_code where repay.status > 95 and info.status > 95 ");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			defrayTableSql.append(" and repay.repay_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");    // 时间段内还款的金额
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			defrayTableSql.append(" and repay.repay_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){
			//defrayTableSql.append(" and info.corp_id in (:corpId)");
			defrayTableSql.append(" and ").append(getSQLIn(corpIds, 900, "info.corp_id"));
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			defrayTableSql.append(" and info.cur_id in (:curId)");
		}
		defrayTableSql.append("  group by info.corp_id,info.cur_id) defrayTable ");

		//使用 left join union right join 代替 full outer join
		StringBuilder earningDefrayTableSql = new StringBuilder();
		earningDefrayTableSql.append(" (");
		earningDefrayTableSql.append(earningDefraySelectFrom);
		earningDefrayTableSql.append(earningTableSql);
		earningDefrayTableSql.append(" left join ");
		earningDefrayTableSql.append(defrayTableSql);
		earningDefrayTableSql.append(" on defrayTable.corp_id = earningTable.corp_id and defrayTable.cur_id = earningTable.cur_id");
		earningDefrayTableSql.append(" union ");
		earningDefrayTableSql.append(earningDefraySelectFrom);
		earningDefrayTableSql.append(earningTableSql);
		earningDefrayTableSql.append(" right join ");
		earningDefrayTableSql.append(defrayTableSql);
		earningDefrayTableSql.append(" on defrayTable.corp_id = earningTable.corp_id and defrayTable.cur_id = earningTable.cur_id");
		earningDefrayTableSql.append(" ) earningDefrayTable ");

		//使用 left join union right join 代替 full outer join
		StringBuilder sql = new StringBuilder();
		sql.append(selectFrom);
		sql.append(beformTableSql);
		sql.append(" left join ");
		sql.append(earningDefrayTableSql);
		sql.append(" on earningDefrayTable.corp_id = beformTable.corp_id and earningDefrayTable.cur_id = beformTable.cur_id");
		sql.append(" union ");
		sql.append(selectFrom);
		sql.append(beformTableSql);
		sql.append(" right join ");
		sql.append(earningDefrayTableSql);
		sql.append(" on earningDefrayTable.corp_id = beformTable.corp_id and earningDefrayTable.cur_id = beformTable.cur_id");
		return sql;
	}

	/**委托贷款*/
	private StringBuilder getPrecativeSql(CmsBalanceGatherVO vo,List<String> corpIds){
		//查询列
		StringBuilder selectFrom = new StringBuilder();
		selectFrom.append(" select '03' as loan_sign,BT_NVL_CHAR(beformTable.corp_id,earningDefrayTable.corp_id) as corp_id,BT_NVL_CHAR(beformTable.cur_id,earningDefrayTable.cur_id) as cur_id,");
		selectFrom.append(" BT_NVL_NUM(beformTable.beform_money, 0) as beform_money,BT_NVL_NUM(earningDefrayTable.earning_money, 0) as earning_money,BT_NVL_NUM(earningDefrayTable.defray_money, 0) as defray_money from ");

		//发放单的期初值
		StringBuilder beformTableSql = new StringBuilder();
		beformTableSql.append(" (select info.loan_opp as corp_id, info.cur_id as cur_id,BT_NVL_NUM(sum(send.money), 0) - BT_NVL_NUM(sum(repay_money), 0) as beform_money from ");
		beformTableSql.append("cms_precative_loan_info info inner join cms_precative_loan_send send on send.father_code = info.bill_code left join (select repay.send_code, sum(repay.repay_money) as repay_money from cms_precative_loan_repay repay where  repay.status > 95 ");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			beformTableSql.append(" and repay.repay_date <  BT_TO_DATE(:startDate, 'yyyy-MM-dd') ");
		}
		beformTableSql.append(" group by repay.send_code ) repaySum on repaySum.send_code = send.bill_code where send.status > 95  and info.status > 95 ");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			beformTableSql.append(" and send.send_date < BT_TO_DATE(:startDate, 'yyyy-MM-dd') ");
			beformTableSql.append(" and send.deadLine_date > BT_TO_DATE(:startDate,'yyyy-MM-dd') ");    // 发放规定的还款日期要大于我们查询的起始日期
		}
		//if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){
			//beformTableSql.append(" and info.loan_opp in (:corpId)");
			beformTableSql.append(" and ").append(getSQLIn(corpIds, 900, "info.loan_opp"));
		//}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			beformTableSql.append(" and info.cur_id in (:curId)");
		}
		beformTableSql.append(" group by info.loan_opp , info.cur_id ) beformTable ");

		//收入支出查询列
		StringBuilder earningDefraySelectFrom = new StringBuilder();
		earningDefraySelectFrom.append(" select BT_NVL_CHAR(earningTable.corp_id, defrayTable.corp_id) as corp_id, BT_NVL_CHAR(earningTable.cur_id, defrayTable.cur_id) as cur_id,BT_NVL_NUM(earningTable.earning_money, 0) as earning_money, BT_NVL_NUM(defrayTable.defray_money, 0) as defray_money from ");

		//收入sql
		StringBuilder earningTableSql = new StringBuilder();
		earningTableSql.append(" (select info.loan_opp as corp_id,info.cur_id as cur_id,BT_NVL_NUM(sum(send.money), 0) as earning_money from cms_precative_loan_info info inner join cms_precative_loan_send send on send.father_code = info.bill_code where send.status > 95 and info.status > 95 ");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			earningTableSql.append(" and send.send_date >=  BT_TO_DATE(:startDate, 'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			earningTableSql.append(" and send.send_date <= BT_TO_DATE(:endDate, 'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){
			//earningTableSql.append(" and info.loan_opp in (:corpId)");
			earningTableSql.append(" and ").append(getSQLIn(corpIds, 900, "info.loan_opp"));
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			earningTableSql.append(" and info.cur_id in (:curId)");
		}
		earningTableSql.append(" group by info.loan_opp, info.cur_id ) earningTable ");

		//支出sql
		StringBuilder defrayTableSql = new StringBuilder();
		defrayTableSql.append(" (select info.loan_opp as corp_id, info.cur_id as cur_id,sum(repay.repay_money) as defray_money from cms_precative_loan_info info inner join cms_precative_loan_send send  on send.father_code = info.bill_code");
		defrayTableSql.append(" inner join cms_precative_loan_repay repay on repay.send_code = send.bill_code where repay.status > 95 and info.status > 95 ");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			defrayTableSql.append(" and repay.repay_date >= BT_TO_DATE(:startDate, 'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			defrayTableSql.append(" and repay.repay_date <= BT_TO_DATE(:endDate, 'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){
			//defrayTableSql.append(" and info.loan_opp in (:corpId)");
			defrayTableSql.append(" and ").append(getSQLIn(corpIds, 900, "info.loan_opp"));
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			defrayTableSql.append(" and info.cur_id in (:curId)");
		}
		defrayTableSql.append(" group by info.loan_opp, info.cur_id ) defrayTable ");
		//使用 left join union right join 代替 full outer join
		StringBuilder earningDefrayTableSql = new StringBuilder();
		earningDefrayTableSql.append(" (");
		earningDefrayTableSql.append(earningDefraySelectFrom);
		earningDefrayTableSql.append(earningTableSql);
		earningDefrayTableSql.append(" left join ");
		earningDefrayTableSql.append(defrayTableSql);
		earningDefrayTableSql.append(" on defrayTable.corp_id = earningTable.corp_id and defrayTable.cur_id = earningTable.cur_id");
		earningDefrayTableSql.append(" union ");
		earningDefrayTableSql.append(earningDefraySelectFrom);
		earningDefrayTableSql.append(earningTableSql);
		earningDefrayTableSql.append(" right join ");
		earningDefrayTableSql.append(defrayTableSql);
		earningDefrayTableSql.append(" on defrayTable.corp_id = earningTable.corp_id and defrayTable.cur_id = earningTable.cur_id");
		earningDefrayTableSql.append(" ) earningDefrayTable ");

		//使用 left join union right join 代替 full outer join
		StringBuilder sql = new StringBuilder();
		sql.append(selectFrom);
		sql.append(beformTableSql);
		sql.append(" left join ");
		sql.append(earningDefrayTableSql);
		sql.append(" on earningDefrayTable.corp_id = beformTable.corp_id and earningDefrayTable.cur_id = beformTable.cur_id");
		sql.append(" union ");
		sql.append(selectFrom);
		sql.append(beformTableSql);
		sql.append(" right join ");
		sql.append(earningDefrayTableSql);
		sql.append(" on earningDefrayTable.corp_id = beformTable.corp_id and earningDefrayTable.cur_id = beformTable.cur_id");
		return sql;
	}

	/**余额汇总表sql*/
	private StringBuilder getBalanceSql(CmsBalanceGatherVO vo,List<String> corpIds){
		StringBuilder sql = new StringBuilder();
		sql.append("select balance.*,corp.code as corp_code,corp.name as corp_name,cur.cur_name as cur_name,cur.natural_sign as natural_sign,cur.cur_exrate from (");

		sql.append(this.getLoanSql(vo,corpIds));    // 查询出期初贷款余额，时间段内还款总金额，发放总金额
		sql.append(" union all ");
		sql.append(this.getProvideSql(vo,corpIds));
		sql.append(" union all ");
		sql.append(this.getPrecativeSql(vo,corpIds));

		sql.append(" ) balance ");
		sql.append(" left join sys_corp corp on corp.id = balance.corp_id ");
		sql.append(" left join bt_currency cur on cur.id = balance.cur_id ");
		return sql;
	}

	/**查询余额汇总数据*/
	public List queryGatherList(CmsBalanceGatherVO vo){
		List<String> corpIds = new ArrayList<>();
		if(StringUtils.isNotEmpty(vo.getCorpId())) {
			String[] corpId = vo.getCorpId().split(",");
			corpIds = Arrays.asList(corpId);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = this.getBalanceSql(vo,corpIds);
		if(EmptyUtils.isNotEmpty(vo.getLoanSign()) && !"".equals(vo.getLoanSign().trim())){
			sql.append(" where balance.loan_sign in (:loanSign)");
			params.put("loanSign", vo.getLoanSign().split(","));
		}
		sql.append(" order by corp.code ");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){//查询语句里开始结束逻辑搞错了，这里错着查
			params.put("startDate", DateUtils.formatDate(vo.getStartDate()));
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			params.put("endDate",DateUtils.formatDate(vo.getEndDate()));
		}

		//params.put("corpId", vo.getCorpId());
		if(!EmptyUtils.isEmpty(vo.getCurId())) {
			params.put("curId", vo.getCurId());
		}
		return this.listBySQLAliasToBean(sql.toString(),params, CmsBalanceGatherVO.class);
	}


	/**
	 * 获取明余额明细sql 贷款部分
	 * @param vo
	 * @return
	 */
	private StringBuilder getLoanBalanceDetailSql(CmsBalanceGatherVO vo){
		StringBuilder sql = new StringBuilder();
		sql.append(" select '01' as loan_sign,infoDetail.corp_id as corp_id,infoDetail.cur_code as cur_id,corp.code as corp_code,corp.name as corp_name,cur.cur_name as cur_name,infoDetail.bill_code as info_code, BT_NVL_NUM(infoDetail.loan_money,0) as pl_money,gatherDetail.send_code as send_code,");
		sql.append(" infoDetail.loan_date as pl_start_date,infoDetail.repay_date as pl_end_date, BT_NVL_NUM(gatherDetail.send_money,0) as send_money,gatherDetail.send_date as send_date, BT_NVL_NUM(gatherDetail.repay_money,0) as repay_money,gatherDetail.repay_date as repay_date, BT_NVL_NUM(lbeformDetail.beform_money,0) as beform_money");
		sql.append(" from (select BT_NVL_CHAR(sendDetail.info_code,repayDetail.info_code) as info_code,BT_NVL_CHAR(sendDetail.send_code,repayDetail.send_code) as send_code,BT_NVL_NUM(sendDetail.send_money,0) as send_money,");
		sql.append(" sendDetail.send_date as send_date,BT_NVL_NUM(repayDetail.repay_money,0) as repay_money,repayDetail.repay_date as repay_date from (select send.loan_code as info_code,send.bill_code as send_code,");
		sql.append(" send.loan_money as send_money,send.loan_date as send_date from cms_loan_send send where send.status > 95 ");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and send.loan_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and send.loan_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
			sql.append(" and send.corp_id in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and send.cur_code in (:curId)");
		}

		sql.append(" ) sendDetail left join (select send.loan_code as info_code,send.bill_code as send_code,repay.repay_money as repay_money,repay.repay_date as repay_date from cms_loan_repay repay inner join cms_loan_send send ");
		sql.append(" on send.bill_code = repay.send_code where  repay.status > 95 and send.status > 95 ");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and repay.repay_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and repay.repay_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
			sql.append(" and send.corp_id in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and send.cur_code in (:curId)");
		}

		sql.append(" ) repayDetail on repayDetail.send_code = sendDetail.send_code ");
		sql.append("union");
		sql.append(" select BT_NVL_CHAR(sendDetail.info_code,repayDetail.info_code) as info_code,BT_NVL_CHAR(sendDetail.send_code,repayDetail.send_code) as send_code,BT_NVL_NUM(sendDetail.send_money,0) as send_money,");
		sql.append(" sendDetail.send_date as send_date,BT_NVL_NUM(repayDetail.repay_money,0) as repay_money,repayDetail.repay_date as repay_date from (select send.loan_code as info_code,send.bill_code as send_code,");
		sql.append(" send.loan_money as send_money,send.loan_date as send_date from cms_loan_send send where send.status > 95 ");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and send.loan_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and send.loan_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
			sql.append(" and send.corp_id in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and send.cur_code in (:curId)");
		}

		sql.append(" ) sendDetail right join (select send.loan_code as info_code,send.bill_code as send_code,repay.repay_money as repay_money,repay.repay_date as repay_date from cms_loan_repay repay inner join cms_loan_send send ");
		sql.append(" on send.bill_code = repay.send_code where  repay.status > 95 and send.status > 95 ");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and repay.repay_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and repay.repay_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
			sql.append(" and send.corp_id in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and send.cur_code in (:curId)");
		}

		sql.append(" ) repayDetail on repayDetail.send_code = sendDetail.send_code ) gatherDetail");

		sql.append(" left join (select info.bill_code as info_code,send.bill_code as send_code,BT_NVL_NUM(send.loan_money,0) - BT_NVL_NUM(repay_money,0) as beform_money from ");
		sql.append(" cms_loan_info info inner join cms_loan_send send on send.loan_code = info.bill_code left join (select repay.send_code,sum(repay.repay_money) as repay_money from cms_loan_repay repay where repay.status > 95 ");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and repay.repay_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}

		sql.append(" group by repay.send_code ) repaySum on repaySum.send_code = send.bill_code where send.status > 95 and info.status > 95");



		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and send.loan_date < BT_TO_DATE(:startDate,'yyyy-MM-dd')");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
			sql.append(" and send.corp_id in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and send.cur_code in (:curId)");
		}
		sql.append(" ) lbeformDetail on lbeformDetail.send_code = gatherDetail.send_code ");
//		sql.append(" right join (select info.bill_code as info_code,send.bill_code as send_code,BT_NVL_NUM(send.loan_money,0) - BT_NVL_NUM(repay_money,0) as beform_money from ");
//		sql.append(" cms_loan_info info inner join cms_loan_send send on send.loan_code = info.bill_code left join (select repay.send_code,sum(repay.repay_money) as repay_money from cms_loan_repay repay where repay.status > 95 ");
//
//		if(!EmptyUtils.isEmpty(vo.getStartDate())){
//			sql.append(" and repay.repay_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
//		}
//
//		sql.append(" group by repay.send_code ) repaySum on repaySum.send_code = send.bill_code where send.status > 95 and info.status > 95");
//
//
//
//		if(!EmptyUtils.isEmpty(vo.getStartDate())){
//			sql.append(" and send.loan_date < BT_TO_DATE(:startDate,'yyyy-MM-dd')");
//		}
//		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
//			sql.append(" and send.corp_id in (:corpId)");
//		}
//		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
//			sql.append(" and send.cur_code in (:curId)");
//		}
//		sql.append(" ) rbeformDetail on rbeformDetail.send_code = gatherDetail.send_code ");
		sql.append("left join cms_loan_info infoDetail on infoDetail.bill_code = BT_NVL_CHAR(gatherDetail.info_code,lbeformDetail.info_code)");
		sql.append(" left join sys_corp corp on corp.id = infoDetail.corp_id ");
		sql.append(" left join bt_currency cur on cur.id = infoDetail.cur_code ");
		sql.append(" order by infoDetail.corp_id,infoDetail.cur_code");
		return sql;
	}

	/**
	 * 获取明余额明细sql 借款部分
	 * @param vo
	 * @return
	 */
	private StringBuilder getProvideBalanceDetailSql(CmsBalanceGatherVO vo){
		StringBuilder sql = new StringBuilder();
		sql.append(" select '02' as loan_sign,infoDetail.corp_id as corp_id,infoDetail.cur_id as cur_id,corp.code as corp_code,corp.name as corp_name,cur.cur_name as cur_name,infoDetail.bill_code as info_code, BT_NVL_NUM(infoDetail.pl_money,0) as pl_money,gatherDetail.send_code as send_code,");
		sql.append(" infoDetail.pl_date as pl_start_date,infoDetail.repay_date as pl_end_date, BT_NVL_NUM(gatherDetail.send_money,0) as send_money,gatherDetail.send_date as send_date, BT_NVL_NUM(gatherDetail.repay_money,0) as repay_money,gatherDetail.repay_date as repay_date, BT_NVL_NUM(lbeformDetail.beform_money,0) as beform_money");
		sql.append(" from (select BT_NVL_CHAR(sendDetail.info_code,repayDetail.info_code) as info_code,BT_NVL_CHAR(sendDetail.send_code,repayDetail.send_code) as send_code,BT_NVL_NUM(sendDetail.send_money,0) as send_money,sendDetail.send_date as send_date,");
		sql.append(" BT_NVL_NUM(repayDetail.repay_money,0) as repay_money,repayDetail.repay_date as repay_date from (select send.loan_code as info_code,send.bill_code as send_code,send.money as send_money,send.send_date as send_date");
		sql.append(" from cms_provide_loan_send send where send.status > 95");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and send.send_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and send.send_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
			sql.append(" and send.corp_id in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and send.cur_code in (:curId)");
		}

		sql.append(" ) sendDetail left join (select send.loan_code as info_code,send.bill_code as send_code,repay.repay_money as repay_money,repay.repay_date as repay_date from cms_provide_loan_repay repay ");
		sql.append(" inner join cms_provide_loan_send send on send.bill_code = repay.send_code where repay.status > 95 and send.status > 95");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and repay.repay_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and repay.repay_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
			sql.append(" and send.corp_id in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and send.cur_code in (:curId)");
		}

		sql.append(" ) repayDetail on repayDetail.send_code = sendDetail.send_code ");
		sql.append("union ");
		sql.append("select BT_NVL_CHAR(sendDetail.info_code,repayDetail.info_code) as info_code,BT_NVL_CHAR(sendDetail.send_code,repayDetail.send_code) as send_code,BT_NVL_NUM(sendDetail.send_money,0) as send_money,sendDetail.send_date as send_date,");
		sql.append(" BT_NVL_NUM(repayDetail.repay_money,0) as repay_money,repayDetail.repay_date as repay_date from (select send.loan_code as info_code,send.bill_code as send_code,send.money as send_money,send.send_date as send_date");
		sql.append(" from cms_provide_loan_send send where send.status > 95");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and send.send_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and send.send_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
			sql.append(" and send.corp_id in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and send.cur_code in (:curId)");
		}

		sql.append(" ) sendDetail right join (select send.loan_code as info_code,send.bill_code as send_code,repay.repay_money as repay_money,repay.repay_date as repay_date from cms_provide_loan_repay repay ");
		sql.append(" inner join cms_provide_loan_send send on send.bill_code = repay.send_code where repay.status > 95 and send.status > 95");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and repay.repay_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and repay.repay_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
			sql.append(" and send.corp_id in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and send.cur_code in (:curId)");
		}

		sql.append(" ) repayDetail on repayDetail.send_code = sendDetail.send_code ) gatherDetail ");
		sql.append("left join ( select info.bill_code as info_code,send.bill_code as send_code,BT_NVL_NUM(send.money,0) - BT_NVL_NUM(repay_money,0) as beform_money");
		sql.append(" from cms_provide_loan_info info inner join  cms_provide_loan_send send on send.loan_code = info.bill_code left join (select repay.send_code,sum(repay.repay_money) as repay_money from cms_provide_loan_repay repay where repay.status > 95 ");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and repay.repay_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}

		sql.append(" group by repay.send_code ) repaySum on repaySum.send_code = send.bill_code where send.status > 95 and info.status > 95 ");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and send.send_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
			sql.append(" and send.corp_id in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and send.cur_code in (:curId)");
		}
		sql.append(" ) lbeformDetail on lbeformDetail.send_code = gatherDetail.send_code");
//		sql.append(" right join ( select info.bill_code as info_code,send.bill_code as send_code,BT_NVL_NUM(send.money,0) - BT_NVL_NUM(repay_money,0) as beform_money");
//		sql.append(" from cms_provide_loan_info info inner join  cms_provide_loan_send send on send.loan_code = info.bill_code left join (select repay.send_code,sum(repay.repay_money) as repay_money from cms_provide_loan_repay repay where repay.status > 95 ");
//
//		if(!EmptyUtils.isEmpty(vo.getStartDate())){
//			sql.append(" and repay.repay_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
//		}
//
//		sql.append(" group by repay.send_code ) repaySum on repaySum.send_code = send.bill_code where send.status > 95 and info.status > 95 ");
//
//		if(!EmptyUtils.isEmpty(vo.getStartDate())){
//			sql.append(" and send.send_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
//		}
//		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//从底层嵌套开始过滤数据
//			sql.append(" and send.corp_id in (:corpId)");
//		}
//		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
//			sql.append(" and send.cur_code in (:curId)");
//		}
//		sql.append(" ) rbeformDetail on rbeformDetail.send_code = gatherDetail.send_code");

		sql.append(" left join cms_provide_loan_info infoDetail on infoDetail.bill_code = BT_NVL_CHAR(gatherDetail.info_code," +
				"lbeformDetail.info_code)");
		sql.append(" left join sys_corp corp on corp.id = infoDetail.corp_id ");
		sql.append(" left join bt_currency cur on cur.id = infoDetail.cur_id ");
		sql.append(" order by infoDetail.corp_id,infoDetail.cur_id");
		return sql;
	}

	/**
	 * 获取明余额明细sql 委贷部分
	 * @param vo
	 * @return
	 */
	private StringBuilder getPrecativeBalanceDetailSql(CmsBalanceGatherVO vo){
		StringBuilder sql = new StringBuilder();
		sql.append(" select '03' as loan_sign,infoDetail.corp_id as corp_id,infoDetail.cur_id as cur_id,corp.code as corp_code,corp.name as corp_name,cur.cur_name as cur_name,infoDetail.bill_code as info_code, BT_NVL_NUM(infoDetail.money,0) as pl_money,gatherDetail.send_code as send_code,");
		sql.append(" infoDetail.start_date as pl_start_date,infoDetail.end_date as pl_end_date, BT_NVL_NUM(gatherDetail.send_money,0) as send_money,gatherDetail.send_date as send_date, BT_NVL_NUM(gatherDetail.repay_money,0) as repay_money,gatherDetail.repay_date as repay_date, BT_NVL_NUM(lbeformDetail.beform_money,0) as beform_money");
		sql.append(" from (select BT_NVL_CHAR(sendDetail.info_code,repayDetail.info_code) as info_code,BT_NVL_CHAR(sendDetail.send_code,repayDetail.send_code) as send_code,BT_NVL_NUM(sendDetail.send_money,0) as send_money,sendDetail.send_date as send_date,BT_NVL_NUM(repayDetail.repay_money,0) as repay_money,");
		sql.append(" repayDetail.repay_date as repay_date from ( select send.father_code as info_code,send.bill_code as send_code,send.money as send_money,send.send_date as send_date from cms_precative_loan_send send where send.status > 95 ");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and send.send_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and send.send_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}

		sql.append(" ) sendDetail left join ( select send.father_code as info_code,send.bill_code as send_code,repay.repay_money as repay_money,repay.repay_date as repay_date from cms_precative_loan_repay repay inner join cms_precative_loan_send send on send.bill_code = repay.send_code");
		sql.append(" where repay.status > 95 and send.status > 95");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and repay.repay_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and repay.repay_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}

		sql.append(" ) repayDetail on repayDetail.send_code = sendDetail.send_code ");
		sql.append("union");
		sql.append(" select BT_NVL_CHAR(sendDetail.info_code,repayDetail.info_code) as info_code,BT_NVL_CHAR(sendDetail.send_code,repayDetail.send_code) as send_code,BT_NVL_NUM(sendDetail.send_money,0) as send_money,sendDetail.send_date as send_date,BT_NVL_NUM(repayDetail.repay_money,0) as repay_money,");
		sql.append(" repayDetail.repay_date as repay_date from ( select send.father_code as info_code,send.bill_code as send_code,send.money as send_money,send.send_date as send_date from cms_precative_loan_send send where send.status > 95 ");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and send.send_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and send.send_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}

		sql.append(" ) sendDetail right join ( select send.father_code as info_code,send.bill_code as send_code,repay.repay_money as repay_money,repay.repay_date as repay_date from cms_precative_loan_repay repay inner join cms_precative_loan_send send on send.bill_code = repay.send_code");
		sql.append(" where repay.status > 95 and send.status > 95");

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and repay.repay_date >= BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append(" and repay.repay_date <= BT_TO_DATE(:endDate,'yyyy-MM-dd') ");
		}

		sql.append(" ) repayDetail on repayDetail.send_code = sendDetail.send_code ) gatherDetail ");

		sql.append(" left join (select info.bill_code as info_code,send.bill_code as send_code,BT_NVL_NUM(send.money,0) - BT_NVL_NUM(repay_money,0) as beform_money from cms_precative_loan_info info inner join cms_precative_loan_send send on send.father_code = info.bill_code");
		sql.append(" left join (select repay.send_code,sum(repay.repay_money) as repay_money from cms_precative_loan_repay repay where repay.status > 95 ");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and repay.repay_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		sql.append(" group by repay.send_code  ) repaySum on repaySum.send_code = send.bill_code where send.status > 95 and info.status > 95");
		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			sql.append(" and send.send_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
		}
		sql.append(" ) lbeformDetail on lbeformDetail.send_code = gatherDetail.send_code");

//		sql.append(" right join (select info.bill_code as info_code,send.bill_code as send_code,BT_NVL_NUM(send.money,0) - BT_NVL_NUM(repay_money,0) as beform_money from cms_precative_loan_info info inner join cms_precative_loan_send send on send.father_code = info.bill_code");
//		sql.append(" left join (select repay.send_code,sum(repay.repay_money) as repay_money from cms_precative_loan_repay repay where repay.status > 95 ");
//		if(!EmptyUtils.isEmpty(vo.getStartDate())){
//			sql.append(" and repay.repay_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
//		}
//		sql.append(" group by repay.send_code  ) repaySum on repaySum.send_code = send.bill_code where send.status > 95 and info.status > 95");
//		if(!EmptyUtils.isEmpty(vo.getStartDate())){
//			sql.append(" and send.send_date < BT_TO_DATE(:startDate,'yyyy-MM-dd') ");
//		}
//		sql.append(" ) rbeformDetail on rbeformDetail.send_code = gatherDetail.send_code");
		sql.append(" left join cms_precative_loan_info infoDetail on infoDetail.bill_code = BT_NVL_CHAR(gatherDetail.info_code, lbeformDetail.info_code)");
		sql.append(" left join sys_corp corp on corp.id = infoDetail.corp_id ");
		sql.append(" left join bt_currency cur on cur.id = infoDetail.cur_id ");

		sql.append(" where 1=1 ");

		if(!EmptyUtils.isEmpty(vo.getCorpId()) && !"".equals(vo.getCorpId().trim())){//委贷从最上层过滤单位币别
			sql.append(" and infoDetail.loan_opp in (:corpId)");
		}
		if(!EmptyUtils.isEmpty(vo.getCurId()) && !"".equals(vo.getCurId().trim())){
			sql.append(" and infoDetail.cur_id in (:curId)");
		}
		sql.append(" order by infoDetail.corp_id,infoDetail.cur_id");
		return sql;
	}

	/**查询余额明细数据*/
	public Object queryDetailList(CmsBalanceGatherVO vo,DataTablesInput dataTablesInput){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		if(EmptyUtils.isNotEmpty(vo.getLoanSign()) && "01".equals(vo.getLoanSign())){//贷款
			sql.append(this.getLoanBalanceDetailSql(vo));
		}
		if(EmptyUtils.isNotEmpty(vo.getLoanSign()) && "02".equals(vo.getLoanSign())){//借款
			sql.append(this.getProvideBalanceDetailSql(vo));
		}
		if(EmptyUtils.isNotEmpty(vo.getLoanSign()) && "03".equals(vo.getLoanSign())){//委贷
			sql.append(this.getPrecativeBalanceDetailSql(vo));
		}

		if(!EmptyUtils.isEmpty(vo.getStartDate())){
			params.put("startDate", DateUtils.formatDate(vo.getStartDate()));
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			params.put("endDate",  DateUtils.formatDate(vo.getEndDate()));
		}

		params.put("corpId", vo.getCorpId());
		params.put("curId", vo.getCurId());

		if(dataTablesInput == null){
			return listBySQLAliasToBean(sql.toString(), params, CmsBalanceGatherVO.class);
		}
		return getDynamicParamsBySQL(dataTablesInput,sql.toString(),CmsBalanceGatherVO.class,null,false, DynamicallyObject.getInstance(params),null);
	}
}
