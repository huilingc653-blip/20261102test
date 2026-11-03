package com.bytter.report.domain.stock.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.api.constant.CapitalStockConstant;
import com.bytter.report.domain.stock.vo.AbBillVO;
import com.bytter.report.domain.stock.vo.CapitalStockWidgetVO;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 资金存量报表票据查询Dao
 */
@Repository
public class AbBillCapitalStockDao extends BaseDao<AbBillVO, String> {
	/**
	 * 根据查询条件生成报表查询语句,存量种类为应收票据,币别为人民币
	 *
	 * @param reportType 报表类型
	 * @return String
	 */
	public String getQueryAbBillSql(String reportType,String corpSql,String netId) {
		StringBuilder sql = new StringBuilder();
		if (CapitalStockConstant.REPORT_TYPE_NET.equalsIgnoreCase(reportType)) {
			//一级报表，按网点分组
			sql.append(" select info.netId as netId ");
		} else if (CapitalStockConstant.REPORT_TYPE_CORP.equalsIgnoreCase(reportType)) {
			//二级报表, 根据网点查询，按单位分组
			sql.append(" select info.corpId as corpId ");
		} else if (CapitalStockConstant.REPORT_TYPE_BANK_ACC.equalsIgnoreCase(reportType)) {
			//三级报表, 根据网点和单位查询，按照银行类别分组
			sql.append(" select info.bankTypeId as bankTypeId, info.bankTypeName bankTypeName ");
		}
		sql.append(" ,'2' as stockType,BT_NVL_NUM(info.beginBal, 0) as beginBal,");
		sql.append(" BT_NVL_NUM(info.inAmt, 0) as inAmt,BT_NVL_NUM(info.outAmt, 0) as outAmt,BT_NVL_NUM(info.beginBal + inAmt - outAmt, 0) as endBal, ");
		sql.append(" BT_NVL_NUM(info.freezeMoney, 0) freezeMoney,BT_NVL_NUM(info.beginBal + inAmt - outAmt - freezeMoney, 0) availBal from ( ");
		//查询网点下面单位的期初余额
		//取网点下面所有单位包括网点本身所对应的票据账号的历史余额的累加和(排除网点的下级网点,只取网点本身和直属下级单位的余额)
		if (CapitalStockConstant.REPORT_TYPE_NET.equalsIgnoreCase(reportType)) {
			//一级报表，按网点分组
			sql.append(" select netId ");
			sql.append(" , SUM(beginBal) as beginBal,SUM(outAmt) as outAmt,SUM(inAmt) as inAmt,SUM(freezeMoney) as freezeMoney from (  ");
			sql.append(" select (case when sc.type = '0' and BT_NVL_CHAR(sc.parent_id,' ')<>' ' then sc.id else sc.net_id end) as netId, ");
			sql.append(" aba.bal as beginBal ,0 as inAmt,0 as outAmt,0 as freezeMoney from ab_bill_acc_bal aba ");
			sql.append(" left join bt_bank_acc bba on bba.bank_acc = aba.bank_acc ");
			sql.append(" left join sys_corp sc on sc.id = bba.corp_id ");
			sql.append(" where aba.bal_date >= :beginDate and aba.bal_date <= :endDate");
			if(EmptyUtils.isNotEmpty(netId)) {
				sql.append(" and ( (sc.type='1' and sc.net_id = :netId ) or (sc.type='0' and sc.id = :netId) ) ");
			}
			if (StringUtils.isNotEmpty(corpSql)) {
				sql.append(" and "+corpSql);
			}
			sql.append(" union all");
//			sql.append(" select sc.net_id as netId ");
			sql.append(" select wdsc.id as netId ");
		} else if (CapitalStockConstant.REPORT_TYPE_CORP.equalsIgnoreCase(reportType)) {
			//二级报表, 根据网点查询，按单位分组
			sql.append(" select corpId ");
			sql.append(" , SUM(beginBal) as beginBal,SUM(outAmt) as outAmt,SUM(inAmt) as inAmt,SUM(freezeMoney) as freezeMoney from (  ");
			sql.append(" select sc.id as corpId, ");
			sql.append(" aba.bal as beginBal ,0 as inAmt,0 as outAmt,0 as freezeMoney from ab_bill_acc_bal aba ");
			sql.append(" left join bt_bank_acc bba on bba.bank_acc = aba.bank_acc ");
			sql.append(" left join sys_corp sc on sc.id = bba.corp_id ");
			sql.append(" where aba.bal_date >= :beginDate and aba.bal_date <= :endDate");
			//sql.append(" and sc.net_id = :netId ");
			sql.append(" and ( (sc.type='1' and sc.net_id = :netId) or (sc.type='0' and sc.id = :netId) ) ");
			if (StringUtils.isNotEmpty(corpSql)) {
				sql.append(" and "+corpSql);
			}
			sql.append(" union all ");
			sql.append(" select sc.id as corpId ");
		} else if (CapitalStockConstant.REPORT_TYPE_BANK_ACC.equalsIgnoreCase(reportType)) {
			//三级报表, 根据网点和单位查询，按照银行类别分组
			sql.append(" select bankTypeId, bankTypeName");
			sql.append(" , SUM(beginBal) as beginBal,SUM(inAmt) as inAmt,SUM(outAmt) as outAmt,SUM(freezeMoney) as freezeMoney from (  ");
			sql.append(" select bba.bank_type_id as bankTypeId,bbt.type_name as  bankTypeName, ");
			sql.append(" aba.bal as beginBal ,0 as inAmt,0 as outAmt,0 as freezeMoney from ab_bill_acc_bal aba ");
			sql.append(" left join bt_bank_acc bba on bba.bank_acc = aba.bank_acc ");
			sql.append(" left join sys_corp sc on sc.id = bba.corp_id ");
			sql.append(" left join bt_bank_type bbt on bbt.id = bba.bank_type_id ");
			sql.append(" where aba.bal_date >= :beginDate and aba.bal_date <= :endDate");
			sql.append(" and sc.id = :corpId ");
			sql.append(" union all");
			sql.append(" select bba.bank_type_id as bankTypeId,bbt.type_name as  bankTypeName ");
		}
		sql.append(" ,0 as beginBal, ");
		//查询资金增加额
		//排除来源为质押且票据未到期的收票登记(20210324 zhengg 质押进来也算余额 解除质押余额减少)
		//来源为贴现的6001回购式贴现签收暂未考虑
		//sql.append("  (case when ((aa.brief_id = '6001' and avv.bill_source='12' and av.end_date < :curdate ) ");
		//sql.append("   or ( aa.brief_id = '6001' and avv.bill_source<>'12' ) ");
		//sql.append("   or aa.brief_id in ('9003', '9010', '9012', '9018', '6212','9019','6019')) ");
		sql.append("  (case when aa.brief_id in ('9003', '9010', '9012', '9018','6001', '6212','9019','6019') ");
		sql.append("  and aa.status = '95' and opt_status = '19' and aa.valid_sign in ('1','2') ");
		sql.append("  then av.bill_money else 0 end) as inAmt, ");
		//查询资金减少
		//质押到期日后才减少 (---6201、6202、6204审批中或完成与背书010重复时,010流水会隐藏----) (20210324 zhengg 质押进来也算余额 解除质押余额减少)
		//sql.append("  (case when aa.status = '95' and aa.valid_sign in ('1','2') and aa.opt_status = '19' ");
		//sql.append("   and ( aa.brief_id in('010','011','012','020','021','6004','6201','6202','6204','9023') ");
		//sql.append("   or (aa.brief_id = '018' and av.end_date < :curdate )) ");
		//sql.append("   then av.bill_money else 0 end) as outAmt, ");
		sql.append("  (case when aa.status = '95' and aa.valid_sign in ('1','2') and aa.opt_status = '19' ");
		sql.append("   and aa.brief_id in('010','011','012','018','8018','020','021','6004','6201','6202','6204','9023') ");
		sql.append("   then av.bill_money else 0 end) as outAmt, ");
		//查询占用资金
		/*
		占用增加:
        010 011 012 018 020 021 6004 6201 6202 6204 审批中
        6011 非隐藏的流水(贴现办理提交时贴现申请流水隐藏，贴现办理打回时，贴现申请取消隐藏，所以此状态包含了贴现办理打回)
//        018 通过但未到期 (20210324 zhengg 质押进来也算余额 解除质押余额减少)
        占用减少:
        9019审批通过，非拒签:opt_status=19   6019审批通过 (20210324 zhengg 质押进来也算余额 解除质押余额减少)
		*/
		//排除下拨申请单和下拨单的操作流水(aa.vid不为空)
		sql.append(" (case when aa.vid is not null ");
//		sql.append(" and((aa.brief_id in ('011','012','018','020','021','6004','6201','6202','6204') and aa.status = '11' and aa.valid_sign = '0') ");
		sql.append(" and((aa.brief_id in ('010','011','012','018','020','021','6004','6201','6202','6204') and (aa.status not in (-1,-2,95) or aa.opt_status not in ('19','-2','-3'))) ");
		sql.append("     or (aa.brief_id = '6011' and (aa.is_hide= '0' or aa.is_hide is null)  and aa.status<>'-2' )  ");
//		sql.append("     or (aa.brief_id = '018' and aa.status = '95' and aa.opt_status='19' and av.end_date >= :curdate ) ");
		sql.append("    ) then av.bill_money");
//		sql.append("    when aa.brief_id in('9019','6019') and aa.status = '95' and aa.opt_status='19' ");
//		sql.append("    then -av.bill_money else 0 end) as freezeMoney ");
		sql.append("    else 0 end) as freezeMoney ");
		sql.append("   from ab_accrecords aa ");
		sql.append("   left join ab_voucherinfo_vice avv on avv.id = aa.vvid ");
		sql.append("   left join ab_voucherinfo av on av.id = aa.vid ");
		sql.append("   left join sys_corp sc on sc.id = avv.corp_id ");
		if(CapitalStockConstant.REPORT_TYPE_NET.equalsIgnoreCase(reportType)){
			sql.append("   right join sys_corp wdsc on (wdsc.id = sc.net_id or wdsc.id=sc.id ) ");
		}
		if (CapitalStockConstant.REPORT_TYPE_BANK_ACC.equalsIgnoreCase(reportType)) {
			sql.append(" left join bt_bank_acc bba ");
			sql.append(" on avv.bank_acc = bba.bank_acc ");
			sql.append(" left join bt_bank_type bbt on bbt.id = bba.bank_type_id ");
		}
		sql.append("   where aa.business_date  >= :beginDate ");
		sql.append("   and aa.business_date < :endDateAddOne ");
		sql.append("and (aa.is_hide<>'"+AccebillConstant.AB_IS_HIDE_Y+"' or aa.is_hide is null) ");
		sql.append("and aa.status not in("+ AccebillConstant.AB_BILL_BUS_STATUS_2+") ");
		sql.append("and aa.opt_status not in('"+AccebillConstant.AB_BILL_OPT_STATUS_3+"','"+AccebillConstant.AB_BILL_OPT_STATUS_4+"') ");
		if (StringUtils.isNotEmpty(corpSql)) {
			sql.append(" and "+corpSql);
		}
		if (CapitalStockConstant.REPORT_TYPE_NET.equalsIgnoreCase(reportType)) {
			//一级报表，按网点分组
			sql.append("   and wdsc.type='0' and (sc.type='1' or wdsc.id=sc.id ) ");
			if(EmptyUtils.isNotEmpty(netId)) {
				sql.append(" and ( (sc.type='1' and sc.net_id = :netId ) or (sc.type='0' and sc.id = :netId) ) ");
			}
			sql.append("   ) billInfo where ");
			sql.append("   billInfo.netId is not null group by billInfo.netId  )info ");
		} else if (CapitalStockConstant.REPORT_TYPE_CORP.equalsIgnoreCase(reportType)) {
			//二级报表, 根据网点查询，按单位分组
			sql.append(" and ( (sc.type='1' and sc.net_id = :netId) or (sc.type='0' and sc.id = :netId) ) ");
			sql.append("   ) billInfo where ");
			sql.append("   billInfo.corpId is not null group by billInfo.corpId  )info ");
		} else if (CapitalStockConstant.REPORT_TYPE_BANK_ACC.equalsIgnoreCase(reportType)) {
			//三级报表, 根据网点和单位查询，按照银行类别分组
			sql.append(" and sc.id = :corpId ");
			sql.append("   ) billInfo where ");
			sql.append("   billInfo.bankTypeId is not null group by billInfo.bankTypeId,billInfo.bankTypeName  )info ");
		}
		return sql.toString();
	}
	/**
	 * 应收票据明细查询列表
	 *
	 * @param dataTablesInput
	 * @param bankTypeId      银行类型ID
	 * @param beginDate       查询开始日期
	 * @param endDate         查询结束日期
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getAbBilldtl(DataTablesInput dataTablesInput, String bankTypeId, Date beginDate, Date endDate, String reportType,String corpId) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" dtl.vid as vid,dtl.vvid as vvid,dtl.id as id,dtl.recid as recid,dtl.type as type,");
		sql.append(" dtl.corpId as corpId,dtl.corpName as corpName,dtl.bankAcc as bankAcc,");
		sql.append(" dtl.busType as busType,dtl.source as source,dtl.origNo as origNo,dtl.billFlag as billFlag,");
		sql.append(" dtl.billType as billType,dtl.billMoney as billMoney,dtl.applyName as applyName,");
		sql.append(" dtl.applyDate as applyDate,dtl.businessDate as businessDate,dtl.startDate as startDate,");
		sql.append(" dtl.endDate as endDate,dtl.interest as interest,dtl.interestRates as interestRates,");
		sql.append(" dtl.payer as payer,dtl.payee as payee,dtl.cnAccrecords as cnAccrecords,");
		sql.append(" dtl.rearAccrecords as rearAccrecords,dtl.busStatus as busStatus,dtl.inoptStatus as inoptStatus ");
		Map<String, Object> params = new HashMap<>();
		sql.append(" from (select v.id as vid,vv.id as vvid,a.ID as id,a.recid as recid,vv.type as type,");
		sql.append("c.id as corpId,BT_CONCAT4('(',c.code,')',c.name) as corpName,vv.bank_acc as bankAcc,");
		sql.append("bs.bus_type as busType,sd.TYPE_CODE as source,v.orig_no as origNo,");
		sql.append("v.bill_flag as billFlag,v.bill_type as billType,");
		sql.append("v.bill_money as billMoney,BT_CONCAT4('(',a.create_by,')',a.create_name) as applyName,");
		sql.append("a.apply_date as applyDate,a.business_date as businessDate,");
		sql.append("v.start_date as startDate,v.end_date as endDate,d.interest as interest,");
		sql.append("d.interest_rates as interestRates,v.payer as payer,v.payee as payee,");
		//前手
		sql.append("case ");
		sql.append("when bs.bus_type in ('6001','9020','9023') then a.req_nm ");
		sql.append("else '' end as cnAccrecords, ");
		//后手
		sql.append("case ");
		sql.append("when bs.bus_type in ('017','002','003','010','018','019','020','011','012','022','6201','6203','6004','023') then a.rec_nm ");
		sql.append("else '' end as rearAccrecords, ");
		sql.append("a.status as busStatus,a.opt_status as inoptStatus ");
		sql.append("from ab_voucherinfo_vice vv ");
		sql.append("left join ab_voucherinfo v on vv.vid=v.id ");
		sql.append("left join ab_accrecords a on a.vid=v.id and a.vvid=vv.id ");
		sql.append("left join ab_discount d on d.record_id=a.id ");
		sql.append("left join ab_brief_status bs on bs.status=a.brief_id ");
		sql.append("left join sys_corp c on c.id=vv.corp_id ");
		sql.append("left join bt_bank_acc bba on bba.bank_acc=vv.bank_acc ");
		sql.append("left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=a.brief_id "
				+ "and (vv.source=sd.source "
				+ "or (vv.source is null and vv.bill_source=sd.source) "
				+ "or (vv.source is null and vv.bill_source is null and sd.source is null))");
//				+ "and BT_NVL_CHAR(BT_NVL_CHAR(vv.source,vv.bill_source),' ')=BT_NVL_CHAR(sd.source,' ') ");
		sql.append("where 1=1 ");
		//应收票据明细
		sql.append("and (a.is_hide <>'"+AccebillConstant.AB_IS_HIDE_Y+"' or a.is_hide is null) ");
		sql.append("and a.status not in("+AccebillConstant.AB_BILL_BUS_STATUS_2+") ");
		sql.append("and a.opt_status not in('"+AccebillConstant.AB_BILL_OPT_STATUS_3+"','"+AccebillConstant.AB_BILL_OPT_STATUS_4+"') ");
		if (CapitalStockConstant.REPORT_TYPE_ABBILLDTL.equalsIgnoreCase(reportType)) {
			sql.append("and vv.type = '1' ");
		} else if (CapitalStockConstant.REPORT_TYPE_ABBILLFREEZE.equalsIgnoreCase(reportType)) {
			//只查询应收票据占用明细
			sql.append(" and a.vid is not null ");
//			sql.append(" and((a.brief_id in ('011','012','018','020','021','6004','6201','6202','6204') and a.status = '11' and a.valid_sign = '0') ");
			sql.append(" and((a.brief_id in ('010','011','012','018','020','021','6004','6201','6202','6204') and (a.status not in (-1,-2,95) or a.opt_status not in ('19','-2','-3'))) ");
			sql.append("     or (a.brief_id = '6011' and (a.is_hide = '0' or a.is_hide is null) ) and a.status<>'-2' ");
			sql.append("     ) ");
//			sql.append("     or (a.brief_id = '018' and a.status = '95' and a.opt_status='19' and v.end_date >= :curdate ) ) ");
		}
		sql.append("and a.business_date >= :beginDate ");
		sql.append("and a.business_date < :endDateAddOne ");
		sql.append("and vv.corp_id = :corpId ");
		sql.append("and bba.bank_type_id = :bankTypeId ");
		sql.append("and bs.bus_type is not null ");
		sql.append("and a.id is not null ");
		sql.append(" ) dtl ");
		params.put("beginDate", new java.sql.Date(beginDate.getTime ()));
		params.put("bankTypeId", bankTypeId);
		params.put("endDateAddOne", DateUtils.addDay(DateUtils.getDateYMD(endDate),1));
		params.put("corpId", corpId);
		params.put("curdate",new java.sql.Date (System.currentTimeMillis()));
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, AbBillVO.class);
	}
	/**
	 * 根据网点ID和日期获取票据可用资金余额
	 * 可用资金余额 = 期初资金余额+本期资金增加额-本期资金减少额-本期不可动用资金
	 */
	public CapitalStockWidgetVO getBillAvailBal(Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		if (!params.containsKey("netId")) {
			sql.append (" select SUM(billAvailBal) as billAvailBal from( ");
		}
		//查询网点下面单位的期初余额
		sql.append(" select BT_NVL_NUM(info.beginBal + inAmt - outAmt - freezeMoney, 0) billAvailBal from ( ");
		sql.append(" select netId  ");
		sql.append(" , SUM(beginBal) as beginBal,SUM(outAmt) as outAmt,SUM(inAmt) as inAmt,SUM(freezeMoney) as freezeMoney from (  ");
		sql.append(" select (case when sc.type = '0' and sc.parent_id is not null then sc.id else sc.net_id end) as netId,");
		sql.append(" aba.bal as beginBal ,0 as inAmt,0 as outAmt,0 as freezeMoney from ab_bill_acc_bal aba ");
		sql.append(" left join bt_bank_acc bba on bba.bank_acc = aba.bank_acc ");
		sql.append(" left join sys_corp sc on sc.id = bba.corp_id ");
		sql.append(" where aba.bal_date = :balDate ");
		if (params.containsKey("netId")) {
			sql.append (" and( (sc.type='1' and sc.net_id = :netId ) or (sc.type='0' and sc.id = :netId) ) ");
		}
		sql.append(" union all");
		sql.append(" select wdsc.id as netId, ");
		sql.append(" 0 as beginBal, ");
		//查询资金增加额
		sql.append(" (case when ((aa.brief_id = '6001' and avv.bill_source='12' and av.end_date < :curdate ) ");
		sql.append("   or ( aa.brief_id = '6001' and avv.bill_source<>'12' ) ");
		sql.append("   or aa.brief_id in ('9003', '9010', '9012', '9018', '6212','9019','6019')) ");
		sql.append("  and aa.status = '95' and opt_status = '19' and aa.valid_sign in ('1','2') ");
		sql.append("  then av.bill_money else 0 end) as inAmt, ");
		//查询资金减少
		//质押到期日后才减少 (---6201、6202、6204审批中或完成与背书010重复时,010流水会隐藏----)
		sql.append("  (case when aa.status = '95' and aa.valid_sign in ('1','2') and aa.opt_status = '19' ");
		sql.append("   and ( aa.brief_id in('010','011','012','020','021','6004','6201','6202','6204','9023') ");
		sql.append("   or (aa.brief_id = '018' and av.end_date < :curdate )) ");
		sql.append("   then av.bill_money else 0 end) as outAmt, ");
		//查询占用资金
		sql.append(" (case when aa.vid is not null ");
		sql.append(" and((aa.brief_id in ('011','012','018','020','021','6004','6201','6202','6204') and aa.status = '11' and aa.valid_sign = '0') ");
		sql.append("     or (aa.brief_id = '6011' and (aa.is_hide= '0' or aa.is_hide is null) and aa.status<>'-2')  ");
		sql.append("     or (aa.brief_id = '018' and aa.status = '95' and aa.opt_status='19' and av.end_date >= :curdate ) ");
		sql.append("    ) then av.bill_money");
		sql.append("    when aa.brief_id in('9019','6019') and aa.status = '95' and aa.opt_status='19' ");
		sql.append("    then -av.bill_money else 0 end) as freezeMoney ");
		sql.append("   from ab_accrecords aa ");
		sql.append("   left join ab_voucherinfo_vice avv on avv.id = aa.vvid ");
		sql.append("   left join ab_voucherinfo av on av.id = aa.vid ");
		sql.append("   left join sys_corp sc on sc.id = avv.corp_id ");
		sql.append("   right join sys_corp wdsc on (wdsc.id = sc.net_id or wdsc.id=sc.id ) ");
		sql.append("   where aa.business_date >= :beginDate ");
		sql.append("   and aa.business_date <= :endDate ");
		sql.append("and (aa.is_hide<>'"+AccebillConstant.AB_IS_HIDE_Y+"' or aa.is_hide is null) ");
		sql.append("and aa.status not in("+AccebillConstant.AB_BILL_BUS_STATUS_2+") ");
		sql.append("and aa.opt_status not in('"+AccebillConstant.AB_BILL_OPT_STATUS_3+"','"+AccebillConstant.AB_BILL_OPT_STATUS_4+"') ");
		if (params.containsKey("netId")) {
			sql.append (" and( (sc.type='1' and sc.net_id = :netId ) or (sc.type='0' and sc.id = :netId) ) ");
		}
		sql.append("   and wdsc.type='0' and (sc.type='1' or wdsc.id=sc.id ) ");
		sql.append("   ) billInfo where ");
		sql.append("   billInfo.netId is not null group by billInfo.netId  )info ");
		if (!params.containsKey("netId")) {
			sql.append (" ) info ");
		}
		params.put("curdate",new java.sql.Date (System.currentTimeMillis()));
		List<CapitalStockWidgetVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, CapitalStockWidgetVO.class);
		if (listInfo != null && !listInfo.isEmpty()) {
			return listInfo.get(0);
		}
		return null;
	}
	/**
	 * 根据网点ID和日期获取票据可用资金余额查询SQL
	 */
	public String getBillAvailBalSql(String netId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select info.netId as netId ");
		sql.append(" ,'2' as stockType,(select cur.id from bt_currency cur where cur.cur_name='人民币' ) as curId,0 as accAvailBal,");
		sql.append(" BT_NVL_NUM(info.beginBal + inAmt - outAmt - freezeMoney, 0) billAvailBal from ( ");
		//查询网点下面单位的期初余额
		sql.append(" select netId  ");
		sql.append(" , SUM(beginBal) as beginBal,SUM(outAmt) as outAmt,SUM(inAmt) as inAmt,SUM(freezeMoney) as freezeMoney from (  ");
		sql.append(" select (case when sc.type = '0' and sc.parent_id is not null then sc.id else sc.net_id end) as netId,");
		sql.append(" aba.bal as beginBal ,0 as inAmt,0 as outAmt,0 as freezeMoney from ab_bill_acc_bal aba ");
		sql.append(" left join bt_bank_acc bba on bba.bank_acc = aba.bank_acc ");
		sql.append(" left join sys_corp sc on sc.id = bba.corp_id ");
		sql.append(" where aba.bal_date = :balDate ");
		if (StringUtils.isNotEmpty(netId)) {
			sql.append (" and( (sc.type='1' and sc.net_id = :netId ) or (sc.type='0' and sc.id = :netId) ) ");
		}
		sql.append(" union all");
		sql.append(" select wdsc.id as netId, ");
		sql.append(" 0 as beginBal, ");
		//查询资金增加额
		sql.append("  (case when ((aa.brief_id = '6001' and avv.bill_source='12' and av.end_date < :curdate ) ");
		sql.append("   or ( aa.brief_id = '6001' and avv.bill_source<>'12' ) ");
		sql.append("   or aa.brief_id in ('9003', '9010', '9012', '9018', '6212','9019','6019')) ");
		sql.append("  and aa.status = '95' and opt_status = '19' and aa.valid_sign in ('1','2') ");
		sql.append("  then av.bill_money else 0 end) as inAmt, ");
		//查询资金减少
		//质押到期日后才减少 (---6201、6202、6204审批中或完成与背书010重复时,010流水会隐藏----)
		sql.append("  (case when aa.status = '95' and aa.valid_sign in ('1','2') and aa.opt_status = '19' ");
		sql.append("   and ( aa.brief_id in('010','011','012','020','021','6004','6201','6202','6204','9023') ");
		sql.append("   or (aa.brief_id = '018' and av.end_date < :curdate )) ");
		sql.append("   then av.bill_money else 0 end) as outAmt, ");
		//查询占用资金
		sql.append(" (case when aa.vid is not null ");
		sql.append(" and((aa.brief_id in ('011','012','018','020','021','6004','6201','6202','6204') and aa.status = '11' and aa.valid_sign = '0') ");
		sql.append("     or (aa.brief_id = '6011' and (aa.is_hide = '0' or aa.is_hide is null )) and aa.status<>'-2' ");
		sql.append("     or (aa.brief_id = '018' and aa.status = '95' and aa.opt_status='19' and av.end_date >= :curdate ) ");
		sql.append("    ) then av.bill_money");
		sql.append("    when aa.brief_id in('9019','6019') and aa.status = '95' and aa.opt_status='19' ");
		sql.append("    then -av.bill_money else 0 end) as freezeMoney ");
		sql.append("   from ab_accrecords aa ");
		sql.append("   left join ab_voucherinfo_vice avv on avv.id = aa.vvid ");
		sql.append("   left join ab_voucherinfo av on av.id = aa.vid ");
		sql.append("   left join sys_corp sc on sc.id = avv.corp_id ");
		sql.append("   right join sys_corp wdsc on (wdsc.id = sc.net_id or wdsc.id=sc.id ) ");
		sql.append("   where aa.business_date  >= :beginDate ");
		sql.append("   and aa.business_date <= :endDate ");
		sql.append("and (aa.is_hide<>'"+AccebillConstant.AB_IS_HIDE_Y+"' or aa.is_hide is null) ");
		sql.append("and aa.status not in("+AccebillConstant.AB_BILL_BUS_STATUS_2+") ");
		sql.append("and aa.opt_status not in('"+AccebillConstant.AB_BILL_OPT_STATUS_3+"','"+AccebillConstant.AB_BILL_OPT_STATUS_4+"') ");
		if (StringUtils.isNotEmpty(netId)) {
			sql.append (" and( (sc.type='1' and sc.net_id = :netId ) or (sc.type='0' and sc.id = :netId) ) ");
		}
		sql.append("   and wdsc.type='0' and (sc.type='1' or wdsc.id=sc.id ) ");
		sql.append("   ) billInfo where ");
		sql.append("   billInfo.netId is not null group by billInfo.netId  )info ");
		return sql.toString();
	}
}
