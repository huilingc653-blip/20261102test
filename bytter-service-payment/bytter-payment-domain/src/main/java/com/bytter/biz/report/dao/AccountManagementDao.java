package com.bytter.biz.report.dao;

import com.bytter.biz.report.vo.NisAllocationReqVO;
import com.bytter.biz.report.vo.NisCreateDate;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhuop
 * @Description 资金归集、下拨情况汇总
 * @date 2018.6.1
 */
@Repository
public class AccountManagementDao extends BaseDao<NisAllocationReqVO, String> {
	
	
	public DataTablesOutput listData(DataTablesInput input,List<String> corpAuth){
		Map<String, Object> params = input.getSearchValues();
		StringBuilder sql = new StringBuilder();
		try {
			sql.append(getAccountManagementHeadsql());
			sql.append(getAccountManagementAndCorpNamesql());
			sql.append(getAccountIsOnAndHad(input, corpAuth));
			sql.append(getWhereAllCondition(input, NisAllocationReqVO.class.newInstance(), "", false));
			input.setOrderString(new OrderString("netId,corpId", "asc"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getDynamicBySQL(input, sql.toString(), NisAllocationReqVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	
	
	public String getAccountManagementHeadsql(){
		StringBuilder sql=new StringBuilder();
		sql.append(" select sc.name as netName,m.netId as netId,m.corpName as corpName,");
		sql.append(" m.corpId as corpId, m.curId as curId,bc.cur_name as curName,m.onDraw as onDraw,m.hadDraw as hadDraw,");
		sql.append(" m.onHanded as onHanded,m.hadHanded as hadHanded,m.onDial as onDial,");
		sql.append(" m.hadDial as hadDial,m.onRequest as onRequest,m.hadRequest as hadRequest from (");
		return sql.toString();
	}
	
	public String getAccountManagementAndCorpNamesql(){
		StringBuilder sql=new StringBuilder();
		sql.append(" select sy.net_id as netId,sy.name as corpName,n.corpId as corpId,n.curId as curId,");
		sql.append(" n.onDraw as onDraw,n.hadDraw as hadDraw, n.onHanded as onHanded,n.hadHanded as hadHanded,");
		sql.append(" n.onDial as onDial,n.hadDial as hadDial,n.onRequest as onRequest,n.hadRequest as hadRequest from (");
		return sql.toString();
		
	}
	
	public String getAccountIsOnAndHad(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		String lecreateDate=DateUtils.formatDate(DateUtils.getIntegralEndTime((Date)params.get("lecreateDate")),"yyyy-MM-dd HH:mm:ss");
		String gecreateDate=DateUtils.date2Str((Date)params.get("gecreateDate"),DateUtils.date_sdf());
		try {
		sql.append(" select corpId,curId,");
		sql.append(" sum(bt_nvl_num(onDraw,0)) as onDraw,sum(bt_nvl_num(hadDraw,0)) as hadDraw,");
		sql.append(" sum(bt_nvl_num(onHanded,0)) as onHanded,sum(bt_nvl_num(hadHanded,0)) as hadHanded,");
		sql.append(" sum(bt_nvl_num(onDial,0)) as onDial,sum(bt_nvl_num(hadDial,0)) as hadDial,");
		sql.append(" sum(bt_nvl_num(onRequest,0)) as onRequest,sum(bt_nvl_num(hadRequest,0)) as hadRequest");
		sql.append(" from (");
		sql.append(" (select a.corpId as corpId, a.curId  as curId,sum(a.onDraw) as onDraw,sum(a.onHanded) as onHanded,");
		sql.append(" sum(a.onDial) as onDial,sum(a.onRequest) as onRequest,");
		sql.append(" 0 as hadDraw,0 as hadHanded,0 as hadDial,0 as hadRequest from (");
		sql.append(" (select nis.corp_id as corpId, nis.cur_id as curId,case nis.type when 10 then sum(bt_nvl_num(nis.money,0)) else 0 end as onDraw,");
		sql.append(" case nis.type when 14 then sum(bt_nvl_num(nis.money,0)) else 0 end as onHanded,");
		sql.append(" case nis.type when 11 then sum(bt_nvl_num(nis.money,0))  else 0 end as onDial,");
		sql.append(" case nis.type when 12 then sum(bt_nvl_num(nis.money,0))  else 0 end as onRequest ");
		sql.append(" from nis_allocation_req nis left join bis_exc be on nis.id= be.cbs_bill_id  left join bis_gather_cmd gc on nis.id=gc.cbs_bill_id where 1=1 ");
		sql.append(" and nis.status<>'"+ CommonConstant.STATUE_DELETE +"'");
	    sql.append(getWhereAllCondition(input, NisCreateDate.class.newInstance(), "", false));
	    sql.append(" and ((be.voucher_stat <> '0' and be.voucher_stat <> '7') or be.voucher_stat is null)  and ((gc.CMD_STAT <>'0' and gc.CMD_STAT <>'7') or gc.CMD_STAT is null) group by nis.corp_id,nis.type,nis.cur_id)"
	    		+ " union( select bs.corp_id as corpId, bs.cur_id  as curId,  0 as onDraw, 0 as onHanded,sum(bt_nvl_num(bs.pay_money, 0)) as onDial, 0 as onRequest"
	    		+ " from bis_exc bs where bs.policy_id is not null ");
	    		if(StringUtils.isNotEmpty(lecreateDate)){
	    			sql.append(" and bs.CREATE_DATE <=bt_to_date('"+lecreateDate+"','yyyy-MM-dd hh24:mi:ss')");
	    		}
	    		if(StringUtils.isNotEmpty(gecreateDate)){
	    			sql.append(" and bs.CREATE_DATE >=bt_to_date('"+gecreateDate+"','yyyy-MM-dd') ");
	    		}
	    		sql.append(" and bs.voucher_from='2' and ((bs.voucher_stat <> '0' and bs.voucher_stat <> '7') or bs.voucher_stat is null)"
	    		+ " group by bs.corp_id, bs.voucher_from,bs.cur_id)"
	    		+ " union(select bbc.corp_id as corpId,bgc.cur_id  as curId,sum(bt_nvl_num(bgc.GATHER_AMT, 0)) as onDraw, 0 as onHanded, 0 as onDial, 0 as onRequest"
	    		+ " from  bis_gather_cmd bgc left join bt_bank_acc bbc on bgc.child_acc_id =bbc.id where bgc.gather_policy_id is not null");
	    		if(StringUtils.isNotEmpty(lecreateDate)){
	    			sql.append(" and bgc.CREATE_DATE <=bt_to_date('"+lecreateDate+"','yyyy-MM-dd hh24:mi:ss')");
	    		}
	    		if(StringUtils.isNotEmpty(gecreateDate)){
	    			sql.append(" and bgc.CREATE_DATE >=bt_to_date('"+gecreateDate+"','yyyy-MM-dd') and bgc.voucher_from='5'");
	    		}
	    		sql.append(" and ((bgc.cmd_stat <> '0' and bgc.cmd_stat <> '7') or bgc.cmd_stat is null)  group by bbc.corp_id, bgc.voucher_from,bgc.cur_id)"
	    		+ " ) a group by a.corpId,a.curId)");
	    sql.append(" union ");
	    sql.append(" (select b.narCorpId as narCorpId, b.curId as curId,");
		sql.append("  0 as onDraw,0 as onHanded,0 as onDial,0 as onRequest, ");
	    sql.append(" sum(b.hadDraw) as hadDraw,sum(b.hadHanded) as hadHanded,sum(b.hadDial) as hadDial,sum(b.hadRequest) as hadRequest from (");
	    sql.append(" (select nis.corp_id as narCorpId, nis.cur_id as curId,case nis.type when 10 then sum(bt_nvl_num(nis.money,0)) else 0 end as hadDraw,");
	    sql.append(" case nis.type when 14 then sum(bt_nvl_num(nis.money,0)) else 0 end as hadHanded,");
	    sql.append(" case nis.type when 11 then sum(bt_nvl_num(nis.money,0)) else 0 end as hadDial,");
	    sql.append(" case nis.type when 12 then sum(bt_nvl_num(nis.money,0)) else 0 end as hadRequest");
	    sql.append(" from nis_allocation_req nis  left join bis_exc be on nis.id= be.cbs_bill_id left join bis_gather_cmd gc on nis.id=gc.cbs_bill_id where 1=1 ");
		sql.append(" and nis.status<>'"+ CommonConstant.STATUE_DELETE +"'");
	    sql.append(getWhereAllCondition(input, NisCreateDate.class.newInstance(), "", false));
	    sql.append(" and (be.voucher_stat = '0' or gc.CMD_STAT ='0') group by nis.corp_id,nis.type,nis.cur_id)"
	    		+ " union( select bs.corp_id as corpId, bs.cur_id  as curId,  0 as hadDraw, 0 as hadHanded,sum(bt_nvl_num(bs.pay_money, 0)) as hadDial, 0 as hadRequest"
	    		+ " from bis_exc bs where bs.policy_id is not null ");
	            if(StringUtils.isNotEmpty(lecreateDate)){
	            	sql.append(" and bs.CREATE_DATE <=bt_to_date('"+lecreateDate+"','yyyy-MM-dd hh24:mi:ss')");
	            }
	    		if(StringUtils.isNotEmpty(gecreateDate)){
	    			sql.append(" and bs.CREATE_DATE >=bt_to_date('"+gecreateDate+"','yyyy-MM-dd')");
	    		}
	    		sql.append(" and bs.voucher_from='2' and bs.voucher_stat = '0' "
	    		+ " group by bs.corp_id, bs.voucher_from,bs.cur_id)"
	    		+ " union(select bbc.corp_id as corpId,bgc.cur_id  as curId,sum(bt_nvl_num(bgc.GATHER_AMT, 0)) as hadDraw, 0 as hadHanded, 0 as hadDial, 0 as hadRequest"
	    		+ " from  bis_gather_cmd bgc left join bt_bank_acc bbc on bgc.child_acc_id =bbc.id where bgc.gather_policy_id is not null");
	    		 if(StringUtils.isNotEmpty(lecreateDate)){
	    			 sql.append(" and bgc.CREATE_DATE <=bt_to_date('"+lecreateDate+"','yyyy-MM-dd hh24:mi:ss')");
	    		 }
	    		if(StringUtils.isNotEmpty(gecreateDate)){
	    			sql.append(" and bgc.CREATE_DATE >=bt_to_date('"+gecreateDate+"','yyyy-MM-dd')");
	    		}
	    		sql.append(" and bgc.voucher_from='5'"
	    		+ " and bgc.cmd_stat = '0'  group by bbc.corp_id, bgc.voucher_from,bgc.cur_id)"
	    		+ ") b group by b.narCorpId,b.curId)) temp ");
	    sql.append(" group by corpId,curId) n");
	    sql.append(" left join sys_corp sy  on n.corpId= sy.id ) m");
	    sql.append(" left join sys_corp sc on m.netId=sc.id  left join bt_currency bc on m.curId=bc.id where 1=1");
	    if (!EmptyUtils.isEmptyList(corpAuth)) {
			sql.append(" and ").append(getSQLIn(corpAuth,900,"m.netId"));
		}
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return sql.toString();
	}
	
	public DataTablesOutput getViewDetail(NisCreateDate nd,String corpId,String type,String status,String curId,DataTablesInput inputs){
		Map<String, Object> params=new HashMap<String, Object>();
		DataTablesInput input=new DataTablesInput();
		input.setStart(inputs.getStart());
		input.setLength(inputs.getLength());
		Map<String, Object> param=input.getSearchValues();
		StringBuilder sql=new StringBuilder();
		sql.append(" select aa.billNo as billNo,aa.type as type, aa.curId as curId, bc.cur_name as curName,aa.money as money,"
				+ " aa.status as status,aa.tallySign as tallySign,aa.voucherState as voucherState,");
		sql.append(" aa.payeracc as payerAcc,acc.acc_name as payerAccName,acc.bank_name as payerBankName,");
		sql.append(" aa.payeeacc as payeeAcc,bba.acc_name as payeeAccName,bba.bank_name as payeeBankName");
		sql.append(" from((select nis.bill_no as billNo,nis.type as type,"
				+ "  nis.status as status,nis.tally_sign as tallySign,"
				+ " case when be.VOUCHER_STAT is null  then gc.cmd_stat else be.VOUCHER_STAT end as voucherState,"
				+ " nis.cur_Id as curId,nis.MONEY as Money,nis.payer_acc as payerAcc,nis.payee_acc as payeeAcc");
		sql.append(" from nis_allocation_req nis left join bis_exc be on nis.id=be.cbs_bill_id left join bis_gather_cmd gc on nis.id=gc.cbs_bill_id where nis.type=:type ");
		sql.append(" and nis.status<>'"+ CommonConstant.STATUE_DELETE +"'");
		if(StringUtils.isNotEmpty(corpId)){
			String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
			sql.append(" and " + SqlUtil.getInSql(corpIds, "nis.corp_id", true));
		}
		if(StringUtils.isNotEmpty(curId)){
			params.put("curId", curId);
			sql.append(" and nis.cur_Id = :curId");
		}
		if(StringUtils.isNotEmpty( nd.getGedCreateDate())){
			params.put("gedCreateDate", nd.getGedCreateDate());
			sql.append(" and nis.CREATE_DATE >= :gedCreateDate ");
		}
		if(StringUtils.isNotEmpty( nd.getLedCreateDate())){
			params.put("ledCreateDate", nd.getLedCreateDate());
			sql.append(" and nis.CREATE_DATE <= :ledCreateDate");
		}
		if("on".equals(status)){
			sql.append(" and ((be.voucher_stat <> '0' and be.voucher_stat <> '7') or be.voucher_stat is null) and ((gc.CMD_STAT <>'0' and gc.CMD_STAT <>'7') or gc.CMD_STAT is null))");
		}
		if("had".equals(status)){
			sql.append(" and (be.voucher_stat = '0' or gc.CMD_STAT ='0'))");
		}
		if("11".equals(type)){
			sql.append(" union (select bs.voucher_no as voucherNo,11 as voucherFrom,bs.status as status,bs.tally_sign as tallySign,"
					+ "  bs.VOUCHER_STAT voucherState,bs.cur_Id as curId,bs.pay_Money as PAYMONEY,bs.payer_acc as payerAcc, "
					+ "  bs.payee_acc as payeeAcc from bis_exc bs where bs.voucher_from='2' and bs.policy_id is not null");
			if(StringUtils.isNotEmpty(corpId)){
				String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
				sql.append(" and " + SqlUtil.getInSql(corpIds, "bs.corp_id", true));
			}
			if(StringUtils.isNotEmpty(curId)){
				sql.append(" and bs.cur_Id = :curId");
			}
			if(StringUtils.isNotEmpty( nd.getGedCreateDate())){
				sql.append(" and bs.CREATE_DATE >= :gedCreateDate ");
			}
			if(StringUtils.isNotEmpty( nd.getLedCreateDate())){
				sql.append(" and bs.CREATE_DATE <= :ledCreateDate");
			}
			if("on".equals(status)){
				sql.append(" and (bs.voucher_stat <> '0' and bs.voucher_stat <> '2' or bs.voucher_stat is null))");
			}
			if("had".equals(status)){
				sql.append(" and bs.voucher_stat = '0')");
			}
		}
		if("10".equals(type)){
			sql.append(" union (select bgc.voucher_no as voucherNo,10 as voucherFrom,999 as status,'k' as tallySign,bgc.CMD_STAT as cmdState,"
					+ "  bgc.cur_Id as curId,bgc.GATHER_AMT as GATHERAMT,bt.bank_acc as payerAcc,ba.bank_acc as payeeAcc"
					+ "  from bis_gather_cmd bgc left join bt_bank_acc bt on bt.id=bgc.child_acc_id"
					+ "  left join bt_bank_acc ba on ba.id=bgc.parent_acc_id  where bgc.voucher_from='5' and bgc.gather_policy_id is not null");
			if(StringUtils.isNotEmpty(corpId)){
				String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
				sql.append(" and " + SqlUtil.getInSql(corpIds, "bt.corp_id", true));
			}
			if(StringUtils.isNotEmpty(curId)){
				sql.append(" and bgc.cur_Id = :curId");
			}
			if(StringUtils.isNotEmpty( nd.getGedCreateDate())){
				sql.append(" and bgc.CREATE_DATE >= :gedCreateDate ");
			}
			if(StringUtils.isNotEmpty( nd.getLedCreateDate())){
				sql.append(" and bgc.CREATE_DATE <= :ledCreateDate");
			}
			if("on".equals(status)){
				sql.append(" and (bgc.cmd_stat <> '0' or bgc.cmd_stat is null))");
			}
			if("had".equals(status)){
				sql.append(" and bgc.cmd_stat = '0')");
			}
		}
		sql.append(" ) aa");
		sql.append(" left join bt_bank_acc acc on acc.bank_acc=aa.payeracc");
		sql.append(" left join bt_bank_acc bba on bba.bank_acc=aa.payeeacc");
		sql.append(" left join bt_currency bc on  aa.curId=bc.id");
		if(StringUtils.isNotEmpty(inputs.getOrderPara()) && StringUtils.isNotEmpty(inputs.getOrderByPara())){
	    	 sql.append(" order by "+inputs.getOrderPara()+" "+inputs.getOrderByPara());
	     }else{
	    	 sql.append(" order by billNo asc"); 
	     }
		params.put("type", type);
	    BigDecimal count = new BigDecimal(createSQLQuery(prepareCountSQL(sql.toString()), params).uniqueResult().toString());
	    List list = listBySQL(sql.toString() , inputs.getStart(), inputs.getLength(), params);
             list = BeanUtils.copyMapListProperties(list, NisAllocationReqVO.class);
	    ////inject(list);
		return new DataTablesOutput(inputs.getDraw(),list,count.intValue(),count.intValue());
		
	}
	
}
