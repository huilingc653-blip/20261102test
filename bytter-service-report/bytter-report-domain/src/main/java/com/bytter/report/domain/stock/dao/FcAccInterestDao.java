package com.bytter.report.domain.stock.dao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.report.domain.stock.vo.FcAccInterestVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.google.common.base.Splitter;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FcAccInterestDao extends BaseDao<FcAccInterestVO, String>{
	/**
	 * 应收实收直接取计息表中数据，不再实时计算
	 * @param input
	 * @param joinsql
	 * @return
	 */
	public DataTablesOutput getInterest(DataTablesInput input,String joinsql) {
		String beginDate=(String)input.getFormdata().get("beginDate") + " 00:00:00";
		String endDate=(String)input.getFormdata().get("endDate") + " 23:59:59";
		String curId=(String)input.getFormdata().get("ins_curId");
		String corpId=(String)input.getFormdata().get("ins_corpId");
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql=new StringBuilder();
		sql.append(" select i.netId as netId,i.corpId as corpId,i.curId as curId,i.accType,sum(i.dInterest) as dInterest,sum(i.tInterest) as tInterest");
		sql.append(" from ( ");
		sql.append(" select  f.net_id as netId,f.corp_id as corpId,f.cur_id as curId, ");
		sql.append(" f.acc_type as accType,d.interest as dInterest,bt_nvl(0,0) as tInterest ");
		sql.append(" from fc_interest_dtl d ");
		sql.append(" inner join fc_acc f on d.account = f.blnctr_acc ");
		sql.append(" where  1=1 and d.interest_sign = '1' and d.bus_status = 95 and f.acc_type in ('01','03','05','08','02','04') ");
		if(beginDate != null){
			sql.append(" and d.interest_time >= bt_to_date('"+beginDate+"','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(endDate != null){
			sql.append(" and d.interest_time <= bt_to_date('"+endDate+"','yyyy-MM-dd hh24:mi:ss') ");
		}
		sql.append(" union all ");
		sql.append(" select f.net_id as netId,f.corp_id as corpId,f.cur_id as curId, ");
		sql.append(" f.acc_type as accType,0 as dInterest, bt_nvl(fpd.interest, 0) as tInterest ");
		sql.append(" from fc_interest_dtl t ");
		sql.append(" left join (select pd.id,(pd.interest - prd.strike_interest) as interest from fc_predraw_dtl pd ");
		sql.append(" left join (select rd.predraw_dtl_id, bt_nvl(sum(rd.strike_interest), 0) as strike_interest from fc_predraw_rush_dtl rd");
		sql.append(" group by rd.predraw_dtl_id) prd on prd.predraw_dtl_id = pd.id and pd.interest - prd.strike_interest > 0) fpd");
		sql.append("  on fpd.id = t.predraw_dtl_id");
		sql.append(" inner join fc_acc f on t.account = f.blnctr_acc  ");
		sql.append(" where  t.interest_sign = '0' and t.bus_status = 95 and f.acc_type in ('01','03','05','08','02','04') ");
		if(beginDate != null){
			sql.append(" and t.interest_time >= bt_to_date('"+beginDate+"','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(endDate != null){
			sql.append(" and t.interest_time <= bt_to_date('"+endDate+"','yyyy-MM-dd hh24:mi:ss') ");
		}
		sql.append(" ) i");
		if(StringUtils.isNotEmpty(joinsql)){
			sql.append(" where ");
			sql.append(joinsql);
		}
		if(StringUtils.isNotEmpty(corpId)){
			List<String> corpIds = StringUtils.splitToList(",",corpId);
			sql.append(" and ").append(getSQLIn(corpIds, 900, "i.corpId"));
		}
		if(StringUtils.isNotEmpty(curId)){
			List<String> curIds = StringUtils.splitToList(CommonConstant.COMMA_SPLIT,curId);
			sql.append(" and ").append(getSQLIn(curIds, 900, "i.curId"));
		}
		sql.append(" group by i.netId,i.corpId,i.curId,i.accType ");
		return getDynamicBySQL(input,sql.toString(),FcAccInterestVO.class,null,false);
	}
	public DataTablesOutput getDtlView(DataTablesInput input){
		String beginDate=(String)input.getFormdata().get("beginDate");
		Date startDate = DateUtils.parse(beginDate,"yyyy-MM-dd");
		beginDate = DateUtils.date2Str(DateUtils.addDay(startDate,-1),DateUtils.date_sdf);
		String endDate=(String)input.getFormdata().get("endDate");
		Date endDate1 = DateUtils.parse(endDate,"yyyy-MM-dd");
		endDate = DateUtils.date2Str(DateUtils.addDay(endDate1,1),DateUtils.date_sdf);
		String status=(String)input.getFormdata().get("status");
		String curId=(String)input.getFormdata().get("curId");
		String corpId=(String)input.getFormdata().get("corpId");
		String accType=(String)input.getFormdata().get("accType");
		StringBuilder sql=new StringBuilder();
		sql.append(" select  f.net_id as netId,f.corp_id as corpId,f.cur_id as curId, ");
		sql.append(" d.account as blnctrAcc,f.acc_name as accName,f.acc_start_date as accStartDate,");
		sql.append(" f.acc_type as accType,d.interest_start_date as interestStartDate, ");
		sql.append(" d.bln_inst_date as interestEndDate,d.days as dayNum,d.interest_rate as interestRate,");
		if(!"receivable".equals(status)){
			sql.append(" d.interest as interest,f.predraw_sign as predrawSign, ");
			sql.append(" '1' as interestSign ");
			sql.append(" from fc_interest_dtl d ");
		}else {
			sql.append(" bt_nvl(fpd.interest, 0) as interest,f.predraw_sign as predrawSign, ");
			sql.append(" '0' as interestSign ");
			sql.append(" from fc_interest_dtl d ");
			sql.append(" left join (select pd.id,(pd.interest - prd.strike_interest) as interest from fc_predraw_dtl pd ");
			sql.append(" left join (select rd.predraw_dtl_id, bt_nvl(sum(rd.strike_interest), 0) as strike_interest from fc_predraw_rush_dtl rd");
			sql.append(" group by rd.predraw_dtl_id) prd on prd.predraw_dtl_id = pd.id and pd.interest - prd.strike_interest > 0) fpd");
			sql.append("  on fpd.id = d.predraw_dtl_id");
		}
		sql.append(" inner join fc_acc f on d.account = f.blnctr_acc ");
		sql.append(" where d.bus_status = 95  ");
		if(!"receivable".equals(status)){
			sql.append(" and d.interest_sign = '1' ");
		}else {
			sql.append(" and d.interest_sign = '0' ");
		}
		if(beginDate != null){
			sql.append(" and d.interest_time >= bt_to_date('"+beginDate+"','yyyy-MM-dd') ");
		}
		if(endDate != null){
			sql.append(" and d.interest_time <= bt_to_date('"+endDate+"','yyyy-MM-dd') ");
		}
		if(StringUtils.isNotEmpty(corpId)){
			sql.append(" and f.corp_Id = '"+ corpId +"'");
		}
		if(StringUtils.isNotEmpty(curId)){
			sql.append(" and f.cur_Id = '"+ curId +"'");
		}
		if(StringUtils.isNotEmpty(accType)){
			sql.append(" and f.acc_type = '"+ accType +"'");
		}
		return getDynamicBySQL(input, sql.toString(), FcAccInterestVO.class, null, false);
	}
	/**
	 * 应收包括已计息跟未计息，实收只有已计息
	 * @param input
	 * @param joinsql
	 * @return
	 */
	public DataTablesOutput getFcAccInterest(DataTablesInput input,String joinsql){
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql=new StringBuilder();
		String beginDate=(String)input.getFormdata().get("beginDate");
		String sqlBeginDate="bt_to_date('"+beginDate+"','yyyy-MM-dd')";
		String endDate=(String)input.getFormdata().get("endDate");
		String sqlEndDate="bt_to_date('"+endDate+"','yyyy-MM-dd')";
		String curId=(String)input.getFormdata().get("ins_curId");
		String corpId=(String)input.getFormdata().get("ins_corpId");
		sql.append(" select fa.netId as netId,fa.corpId as corpId,fa.curId as curId,bt_nvl_num(den.interestReceivable,0) as interestReceivable,bt_nvl_num(den.paidInterest,0) as paidInterest");
		sql.append(" from (select f.net_Id as netId,f.corp_id as corpId,f.cur_id as curId from  fc_acc f  where f.acc_type in ('01','02')  and f.INTEREST_SIGN='1' group by f.net_id,f.corp_id,f.cur_id) fa "
				+ "  left join(");
		sql.append(" select de.netId as netId,de.corpId as corpId,de.curId as curId,");
		sql.append(" sum( de.interestRate*de.cumu*de.dayNum/360 ) +sum(de.balance*de.dayNum*de.interestRate/360) as interestReceivable,");
		sql.append(" sum(case when de.interestSign='1' then de.interestRate*de.cumu*de.dayNum/360 else 0 end) as paidInterest");
		sql.append(" from ("+ getFcAccInterestDetail(beginDate,endDate));
		sql.append(" )de where 1=1");
		if(StringUtils.isNotEmpty(beginDate)){
//			sql.append(" and bt_nvl_date(b.balance_date,a.starttime) >= :beginDate");
			sql.append(" and case when de.balanceDate is null then "+sqlBeginDate+" else de.balanceDate end >= :beginDate  and case when de.interestEndDate is null then"
					+ " "+sqlBeginDate+" else de.interestEndDate end >= :beginDate");
			params.put("beginDate",DateUtils.str2Date(beginDate,DateUtils.date_sdf));
		}
		if(StringUtils.isNotEmpty(endDate)){
//			sql.append(" and bt_nvl_date(b.balance_date,a.starttime) <= :endDate");
			sql.append(" and case when de.balanceDate is null then "+sqlEndDate+" else de.balanceDate end <= :endDate  and de.interestStartDate < :endDate ");
			params.put("endDate", DateUtils.str2Date(endDate,DateUtils.date_sdf));
		}
		sql.append(" group by de.netId,de.corpId,de.curId");
		sql.append(" )den on fa.netId=den.netId and fa.corpId=den.corpId and fa.curId=den.curId where 1=1");
		if(StringUtils.isNotEmpty(joinsql)){
			sql.append(" and ");
			sql.append(joinsql);
		}
		if(StringUtils.isNotEmpty(curId)){
			List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(curId));
			String inStr = getSQLIn(result, 1000, "fa.curId");
			sql.append(" and "+inStr);
		}
		if(StringUtils.isNotEmpty(corpId)){
			List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(corpId));
			String inStr = getSQLIn(result, 1000, "fa.corpId");
			sql.append(" and "+inStr);
		}
		return getDynamicBySQL(input, sql.toString(), FcAccInterestVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	/**
	 * 根据虚表进行关联计算,日结之前未计息在积数余额表（fc_accu_balance）计算，日结之前已计息在利息明细表（fc_interest_dtl）计算，日结之后在联机表（FC_ONLINE_LEDGER）计算
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getFcAccInterestDetail(String beginDate,String endDate){
		String sqlBeginDate="bt_to_date('"+beginDate+"','yyyy-MM-dd')";
		String sqlEndDate="bt_to_date('"+endDate+"','yyyy-MM-dd')";
		String startDate="case when case when a.starttime=f.ACC_START_DATE then a.starttime-1 else a.starttime end<= c.close_date and case when a.endtime is null then c.close_date else  a.endtime end >=c.close_date then c.close_date+1 else a.starttime end";
		StringBuilder sql=new StringBuilder();
//日结之前（包含日结）未计息
		sql.append(" select f.net_id as netId,f.corp_id as corpId,f.cur_id as curId,f.blnctr_acc as blnctrAcc,f.ACC_START_DATE as accStartDate,");
		sql.append(" b.balance_date as balanceDate,c.close_date as closeDate,a.newInstrate as interestRate,");
		sql.append(" case when a.endtime is null then (case when (case when a.starttime=f.ACC_START_DATE then a.starttime-1 else a.starttime end)<=c.close_date then");
		sql.append(" (case when "+sqlBeginDate+"> c.close_date then c.close_date else "+sqlBeginDate+" end) else a.starttime end) else ");
		sql.append(" a.starttime end as interestStartDate,");
		sql.append(" case when a.endtime is null then (case when (case when a.starttime=f.ACC_START_DATE then a.starttime-1 else a.starttime end)<=c.close_date then");
		sql.append(" (case when "+sqlEndDate+"> c.close_date then c.close_date else "+sqlEndDate+" end) else a.endtime end) else ");
		sql.append(" (case when(case when a.starttime=f.ACC_START_DATE then a.starttime-1 else a.starttime end)<=c.close_date and a.endtime>=c.close_date then c.close_date else a.endtime end) end as interestEndDate,");
		sql.append(" f.ACC_TYPE as accType,f.ACC_NAME as accName,b.INTEREST_SIGN as interestSign,");
		sql.append(" case when c.close_date >= b.balance_date then b.cumu else 0 end as cumu,");
		sql.append(" 0 as balance,");
		sql.append(" 1 as dayNum");
		sql.append(" from fc_acc f");
		sql.append(" left join ("+getAdjustInterst());
		sql.append(" ) a  on f.blnctr_acc=a.account");
		sql.append(" left join fc_accu_balance b ");
		sql.append(" on f.blnctr_acc=b.account and f.cur_id=b.cur_code and b.balance_date>= a.starttime and case when a.endtime is null then b.balance_date else a.endtime end >=b.balance_date");
		sql.append(" left join  FC_DAY_CLOSE c on f.net_id=c.net_code");
		sql.append(" where case when a.starttime=f.ACC_START_DATE then a.starttime-1 else a.starttime end <= c.close_date ");
		sql.append(" and "+sqlBeginDate+"<= c.close_date");
		sql.append(" and f.acc_type in ('01','02') and c.over_type='D' and f.INTEREST_SIGN='1' and b.INTEREST_SIGN='0'");
		sql.append(" union ");
//日结之后（统一为未计息）
		sql.append(" select f.net_id as netId,f.corp_id as corpId,f.cur_id as curId,f.blnctr_acc as blnctrAcc,f.ACC_START_DATE as accStartDate,");
		sql.append(" case when "+sqlBeginDate+"> c.close_date then "+sqlBeginDate+" else c.close_date end as balanceDate,c.close_date as closeDate,a.newInstrate as interestRate,");
		sql.append(" case when "+sqlBeginDate+"> c.close_date then case when ("+startDate+")<="+sqlBeginDate+" and "+sqlBeginDate+"<= case when a.endtime is null then "+sqlBeginDate+" else a.endtime end");
		sql.append(" then "+sqlBeginDate+" else ("+startDate+") end else ("+startDate+") end as interestStartDate,");
		sql.append(" case when ("+startDate+")<="+sqlEndDate+" and "+sqlEndDate+"<= case when a.endtime is null then "+sqlEndDate+" else a.endtime end");
		sql.append(" then "+sqlEndDate+" else (case when a.endtime is null then "+sqlEndDate+" else a.endtime end) end  as interestEndDate,");
		sql.append(" f.ACC_TYPE as accType,");
		sql.append(" f.ACC_NAME as accName,'0' as interestSign, 0 as cumu ,fab.CHECK_BALANCE as balance,");
		sql.append(" case when a.endtime is null then (case when ("+startDate+") <="+sqlBeginDate+" then "+sqlEndDate+"-"+sqlBeginDate+"+1 else"
				+ "  case when ("+startDate+")<="+sqlEndDate+" then "+sqlEndDate+"-("+startDate+")+1 else null end end) else (");
		sql.append(" case when ("+startDate+") <="+sqlEndDate+" and a.endtime>="+sqlEndDate+" then ("+sqlEndDate+"-("+startDate+")+1)"
				+ "  when ("+startDate+") <="+sqlBeginDate+" and a.endtime>="+sqlBeginDate+" then (a.endtime-"+sqlBeginDate+"+1)"
				+ "  else a.endtime-("+startDate+")+1 end) end as dayNum");
		sql.append(" from fc_acc f");
		sql.append(" left join ("+getAdjustInterst());
		sql.append(" ) a  on f.blnctr_acc=a.account");
		sql.append(" left join  FC_DAY_CLOSE c on f.net_id=c.net_code");
		sql.append(" left join  FC_ONLINE_LEDGER fab on f.blnctr_acc=fab.account and f.cur_id=fab.cur_code");
		sql.append(" where f.acc_type in ('01','02') and c.over_type='D' and f.INTEREST_SIGN='1' and ");
		sql.append(" (case when a.starttime=f.ACC_START_DATE then a.starttime-1 else a.starttime end >= c.close_date  or (case when a.endtime is null then c.close_date else a.endtime end >= c.close_date and a.starttime<=  c.close_date))");
		sql.append(" and "+sqlEndDate+">c.close_date");
		sql.append(" union ");
//日结之前（包含日结）已计息
		sql.append(" select f.net_id as netId,f.corp_id as corpId,f.cur_id as curId,f.blnctr_acc as blnctrAcc,f.ACC_START_DATE as accStartDate,");
		sql.append(" null as balanceDate,c.close_date as closeDate,fc.INTEREST_RATE as interestRate,");
		sql.append(" case when fc.INTEREST_START_DATE <="+sqlBeginDate+" and fc.BLN_INST_DATE>="+sqlBeginDate+" then "+sqlBeginDate+" else fc.INTEREST_START_DATE end as interestStartDate,");
		sql.append(" case when fc.INTEREST_START_DATE <="+sqlEndDate+" and fc.BLN_INST_DATE>="+sqlEndDate+" then "+sqlEndDate+" else fc.BLN_INST_DATE end as interestEndDate,");
		sql.append(" f.ACC_TYPE as accType,f.ACC_NAME as accName,fc.INTEREST_SIGN as interestSign,");
		sql.append(" fc.CUMULATION/fc.days as cumu,");
		sql.append(" 0 as balance,");
		sql.append(" case when fc.INTEREST_START_DATE <="+sqlEndDate+" and fc.BLN_INST_DATE>="+sqlEndDate+" then "+sqlEndDate+" else fc.BLN_INST_DATE end -");
		sql.append(" case when fc.INTEREST_START_DATE <="+sqlBeginDate+" and fc.BLN_INST_DATE>="+sqlBeginDate+" then "+sqlBeginDate+" else fc.INTEREST_START_DATE end+1 ");
		sql.append(" as dayNum");
		sql.append(" from fc_acc f left join fc_interest_dtl fc on f.blnctr_acc=fc.account and f.cur_id=fc.cur_code");
		sql.append(" left join  FC_DAY_CLOSE c on f.net_id=c.net_code");
		sql.append(" where f.acc_type in ('01','02') and c.over_type='D' and f.INTEREST_SIGN='1' and fc.INTEREST_SIGN='1'");
		return sql.toString();
	}
	//获取利率调整的虚表
	public String getAdjustInterst(){
		StringBuilder sql=new StringBuilder();
		sql.append(" select a.account as account,a.old_instrate as oldInstrate,a.new_instrate as newInstrate,a.adjust_time as starttime,i.adjust_time-1 as endtime from fc_instrate_adjust a");
		sql.append(" left join fc_instrate_adjust i on a.account=i.account and a.new_instrate=i.old_instrate and a.id <> i.id and i.check_time>a.check_time");
		sql.append(" where  (i.check_time=(select min(fia.check_time) from fc_instrate_adjust fia where fia.account=a.account and fia.old_instrate=a.new_instrate and fia.check_time> a.check_time ) or");
		sql.append(" i.check_time is null) and a.status='95'");
		sql.append(" union");
		sql.append(" select  fi.account,0,fi.old_instrate,f.interest_start_date,fi.adjust_time-1 from fc_instrate_adjust fi  left join fc_acc f on fi.account=f.blnctr_acc");
		sql.append(" where fi.adjust_time=(select min(fc.adjust_time) from fc_instrate_adjust fc where fc.account=fi.account)  and fi.status='95'");
		sql.append(" union");
		sql.append(" select  fa.blnctr_acc,0,fa.interest_rate,fa.interest_start_date,null from fc_acc fa left join fc_instrate_adjust fia on fa.blnctr_acc=fia.account");
		sql.append(" where fia.id is null");
		return sql.toString();
	}
	/**
	 * 获取分条是数据信息
	 */
	public DataTablesOutput getDetail(DataTablesInput input){
		StringBuilder sql=new StringBuilder();
		String beginDate=(String)input.getFormdata().get("beginDate");
		String sqlBeginDate="bt_to_date('"+beginDate+"','yyyy-MM-dd')";
		String endDate=(String)input.getFormdata().get("endDate");
		String sqlEndDate="bt_to_date('"+endDate+"','yyyy-MM-dd')";
		String status=(String)input.getFormdata().get("status");
		String curId=(String)input.getFormdata().get("curId");
		String corpId=(String)input.getFormdata().get("corpId");
		Map<String, Object> params = new HashMap<>();
		String interestStartDate=" case when den.interestStartDate<fa.ACC_START_DATE and den.interestEndDate>fa.ACC_START_DATE then fa.ACC_START_DATE else den.interestStartDate end ";
		String reInterestEndDate=" case when den.interestEndDate is null then "+sqlEndDate+" else den.interestEndDate end ";
		String paInterestEndDate=" case when den.closeDate< den.interestEndDate and den.closeDate > den.interestStartDate then den.closeDate else den.interestEndDate end ";
		sql.append(" select n.netId as netId,n.corpId as corpId,n.curId as curId,n.blnctrAcc as blnctrAcc,n.accName as accName,n.accStartDate as accStartDate,"
				+ "n.closeDate as closeDate,n.interestSign as interestSign,n.accType as accType,n.interestRate as interestRate,n.predrawSign as predrawSign,");
		sql.append(" min(n.interestStartDate) as interestStartDate,max(interestEndDate) as interestEndDate,sum(dayNum) as dayNum ,sum(interest) as interest from(");
		sql.append(" select fa.net_id as netId,fa.corp_id as corpId,fa.cur_id as curId,den.blnctrAcc as blnctrAcc,fa.acc_name as accName,fa.ACC_START_DATE as accStartDate,den.closeDate as closeDate,");
		if("receivable".equals(status)){
			sql.append(" den.interestSign as interestSign, ");
			sql.append(" case when den.interestStartDate<fa.ACC_START_DATE and den.interestEndDate>fa.ACC_START_DATE then fa.ACC_START_DATE else den.interestStartDate end as interestStartDate,");
			sql.append(" case when den.interestEndDate is null then "+sqlEndDate+" else den.interestEndDate end  as interestEndDate,");
			sql.append(" case when den.interestEndDate is null then "+sqlEndDate+" else den.interestEndDate end"
					+ "-case when den.interestStartDate<fa.ACC_START_DATE and (case when den.interestEndDate is null then "+sqlEndDate+" else den.interestEndDate end)>fa.ACC_START_DATE then fa.ACC_START_DATE else den.interestStartDate end+1 as dayNum,");
			sql.append(" case when "+interestStartDate+"=den.closeDate+1 then den.closeDate when "+reInterestEndDate+"=den.closeDate then den.closeDate else "+interestStartDate+" end as isMerge," );
		}else{
			sql.append(" den.interestSign as interestSign,");
			sql.append(" case when den.interestStartDate<fa.ACC_START_DATE and den.interestEndDate>fa.ACC_START_DATE then fa.ACC_START_DATE else den.interestStartDate end as interestStartDate,");
			sql.append(" case when den.closeDate< den.interestEndDate and den.closeDate > den.interestStartDate then den.closeDate else den.interestEndDate end as interestEndDate,");
			sql.append(" case when den.closeDate< den.interestEndDate and den.closeDate > den.interestStartDate then den.closeDate else den.interestEndDate end-");
			sql.append(" case when den.interestStartDate<fa.ACC_START_DATE and den.interestEndDate>fa.ACC_START_DATE then fa.ACC_START_DATE else den.interestStartDate end+1 as dayNum,");
			sql.append(" case when "+interestStartDate+"=den.closeDate+1 then den.closeDate when "+paInterestEndDate+"=den.closeDate then den.closeDate else "+interestStartDate+" end as isMerge," );
		}
		sql.append(" den.interest as interest,");
		sql.append(" fa.acc_type as accType,");
		sql.append(" den.interestRate as interestRate, fa.PREDRAW_SIGN as predrawSign");
		sql.append(" from(select de.netId as netId,de.corpId as corpId,de.curId as curId,de.blnctrAcc as blnctrAcc,de.interestRate as interestRate,de.closeDate as closeDate,");
		sql.append(" de.interestSign as interestSign,");
		sql.append(" case when de.interestStartDate<=de.closeDate and de.interestSign='0' then min(de.balanceDate) else de.interestStartDate end as interestStartDate,");
		sql.append(" case when de.interestEndDate<=de.closeDate and de.interestSign='0' then max(de.balanceDate) else de.interestEndDate end as interestEndDate,");
		sql.append(" sum(de.dayNum) as dayNum,");
		if("receivable".equals(status)){
			sql.append(" sum( de.interestRate*de.cumu*de.dayNum/360 ) +sum(de.balance*de.dayNum*de.interestRate/360)  as interest");
		}else{
			sql.append(" sum(case when de.interestSign='1' then de.interestRate*de.cumu*de.dayNum/360 else 0 end) as interest");
		}
		sql.append(" from("+getFcAccInterestDetail(beginDate,endDate));
		sql.append(" )de where 1=1");
		if("paid".equals(status)){
			sql.append(" and de.interestSign='1'");
		}
		if(StringUtils.isNotEmpty(beginDate)){
			sql.append(" and case when de.balanceDate is null then "+sqlBeginDate+" else de.balanceDate end >= :beginDate  and case when de.interestEndDate is null then"
					+ " "+sqlBeginDate+" else de.interestEndDate end >= :beginDate");
			params.put("beginDate",DateUtils.str2Date(beginDate,DateUtils.date_sdf));
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql.append(" and case when de.balanceDate is null then "+sqlEndDate+" else de.balanceDate end <= :endDate  and de.interestStartDate < :endDate ");
			params.put("endDate", DateUtils.str2Date(endDate,DateUtils.date_sdf));
		}
		sql.append(" group by de.netId,de.corpId ,de.curId,de.blnctrAcc,de.interestRate,de.interestStartDate,de.interestEndDate,de.closeDate,de.interestSign) den");
		sql.append(" left join fc_acc fa on fa.BLNCTR_ACC=den.blnctrAcc and fa.cur_id=den.curId and fa.corp_id=den.corpId where 1=1");
		sql.append(" and den.interestSign is not null");
		if("receivable".equals(status)){
			sql.append(" and case when den.interestEndDate is null then "+sqlEndDate+" else den.interestEndDate end >=fa.ACC_START_DATE");
		}else{
			sql.append(" and case when den.closeDate< den.interestEndDate and den.closeDate > den.interestStartDate then den.closeDate else den.interestEndDate end >=fa.ACC_START_DATE");
		}
		if(StringUtils.isNotEmpty(curId)){
			sql.append(" and fa.cur_id= :curId");
			params.put("curId", curId);
		}
		if(StringUtils.isNotEmpty(corpId)){
			sql.append(" and fa.corp_id= :corpId");
			params.put("corpId", corpId);
		}
		sql.append(" )n group by n.netId,n.corpId,n.curId,n.blnctrAcc,n.accName,n.accStartDate,n.closeDate,n.interestSign,n.accType,n.interestRate,n.predrawSign,n.isMerge");
		sql.append(" order by n.blnctrAcc asc,min(n.interestStartDate) asc");
		System.out.println(sql.toString());
		return getDynamicBySQL(input, sql.toString(), FcAccInterestVO.class, null, false, DynamicallyObject.getInstance(params));
	}
}
