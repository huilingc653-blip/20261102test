package com.bytter.biz.report.dao;
import com.bytter.biz.report.vo.NisBillheadVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.google.common.base.Splitter;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @author zhuop
 * @Description 客商收入资金量统计情况
 * @date 2018.8.15
 */
@Repository
public class MerchantReceiptDao extends BaseDao<NisBillheadVO, String>{
	/**
	 * 获取分组数据
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public DataTablesOutput getMerchantData(DataTablesInput input,List<String> corpAuth){
		Map<String, Object> params = input.getSearchValues();
		String startDate=(String)input.getFormdata().get("startDate");
		String endDate=(String)input.getFormdata().get("endDate");
		String curId=(String)input.getFormdata().get("ins_curId");
		String merchantName=(String)input.getFormdata().get("likes_merchantName");
		String corpId=(String)input.getFormdata().get("ins_corpId");
		if(StringUtils.isNotEmpty(startDate)){
			input.getFormdata().remove(startDate);
			params.put("startTime",DateUtils.getIntegralStartTime(DateUtils.parseDate(startDate)));
			params.put("startDate",DateUtils.parseDate(startDate));
		}
		if(StringUtils.isNotEmpty(endDate)){
			input.getFormdata().remove(endDate);
			params.put("endTime", DateUtils.getIntegralEndTime(DateUtils.parseDate(endDate)));
			params.put("endDate",DateUtils.parseDate(endDate));
		}
		StringBuilder sql=new StringBuilder();
		sql.append("select syc.name as corpName,d.corpId as corpId,sc.id as id,sc.code as code ,sc.NAME as merchantName,sc.abbreviate as abbreviate,bt.cur_name as curName,");
		sql.append(" d.curId as curId,sum(d.payMoney) as payMoney,count(*) as payNumber from ");
		sql.append(this.mainData("merchantData",input,corpAuth));
		input.getFormdata().clear();
		if(StringUtils.isNotEmpty(curId)){
			 List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(curId));
             String inStr = getSQLIn(result, 1000, "d.curId");
	    	 sql.append(" and "+inStr);	
		}
		if(StringUtils.isNotEmpty(corpId)){
			 List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(corpId));
            String inStr = getSQLIn(result, 1000, "d.corpId");
	    	 sql.append(" and "+inStr);
		}
		if(StringUtils.isNotEmpty(merchantName)){
			 sql.append(" and sc.NAME like :merchantName");
	    	 params.put("merchantName", '%'+merchantName+'%');
		}
		sql.append(" group by  syc.name,d.corpId,sc.id,sc.code,sc.NAME,sc.abbreviate,bt.cur_name,d.curId");
		
		
		return getDynamicBySQL(input, sql.toString(), NisBillheadVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	/**
	 * 获取每一类分组数据后面的详情
	 * @param input
	 * @param corpAuth
	 * @return
	 */
    public DataTablesOutput getDetailData(DataTablesInput input,List<String> corpAuth){
    	Map<String, Object> params = input.getSearchValues();
    	String startDate=(String)input.getFormdata().get("startDate");
		String endDate=(String)input.getFormdata().get("endDate");
		if(StringUtils.isNotEmpty(startDate)){
			input.getFormdata().remove(startDate);
			params.put("startTime",DateUtils.getIntegralStartTime(DateUtils.parseDate(startDate)));
			params.put("startDate",DateUtils.parseDate(startDate));
		}
		if(StringUtils.isNotEmpty(endDate)){
			input.getFormdata().remove(endDate);
			params.put("endTime", DateUtils.getIntegralEndTime(DateUtils.parseDate(endDate)));
			params.put("endDate",DateUtils.parseDate(endDate));
		}
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select d.payerAcc as payerAcc,d.payerAccName as payerAccName,d.payerBank as payerBank,d.curId as curId,bt.cur_name as curName,d.voucherName as voucherName, ");
		sql.append(" d.payMoney as payMoney,d.payeeAcc as payeeAcc,d.payeeAccName as payeeAccName,d.payeeBank  as payeeBank,d.createDate as createDate,");
		sql.append(" d.wishPayDay as wishPayDay from");
		sql.append(this.mainData("detailData",input,corpAuth));
		
		return getDynamicBySQL(input, sql.toString(), NisBillheadVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	/**
	 * 要查询付款通知单的内容
	 * @param type
	 * @return
	 */
	public String needNisBillHeadData(String type){
		StringBuilder sql=new StringBuilder();
		
		if("merchantData".equals(type)){
			sql.append(" select  n.corp_id as corpId,n.payee_acc as payeeAcc,n.cur_id as curId,n.pay_money as payMoney");
		}
		if("detailData".equals(type)){
			sql.append(" select n.payer_acc as payerAcc,n.payer_acc_name as payerAccName,n.payer_bank as payerBank,n.cur_id as curId,n.voucher_type_id as voucherTypeId, ");
			sql.append(" f.VOUCHER_NAME as voucherName,n.pay_money as payMoney,n.payee_acc as payeeAcc,n.payee_acc_name as payeeAccName,n.payee_bank  as payeeBank,n.create_date as createDate,");
			sql.append(" n.wish_payday as wishPayDay,n.corp_id as corpId");
		}
		return sql.toString();
	}
	/**
	 * 要查询付款指令表的内容
	 * @param type
	 * @return
	 */
	public String needBisExcData(String type){
		StringBuilder sql=new StringBuilder();
		if("merchantData".equals(type)){
		   sql.append(" select b.corp_id as corpId,b.payee_acc as payeeAcc,b.cur_id as curId,b.pay_money as payMoney");
		}
       if("detailData".equals(type)){
    	   sql.append(" select b.payer_acc as payerAcc,b.payer_acc_name as payerAccName,b.payer_bank as payerBank,b.cur_id as curId,b.voucher_type_id as voucherTypeId, ");
		   sql.append(" f.VOUCHER_NAME as voucherName,b.pay_money as payMoney,b.payee_acc as payeeAcc,b.payee_acc_name as payeeAccName,b.payee_bank  as payeeBank,b.create_date as createDate,");
		   sql.append(" b.wish_payday as wishPayDay,b.corp_id as corpId");
		}
		return sql.toString();
		
	}
	/**
	 * 查询数据主体
	 * @param type
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	//TODO 整体改造  从sys查出结果到sys再做筛选
	public String mainData(String type,DataTablesInput input,List<String> corpAuth){
		String startDate=(String)input.getFormdata().get("startDate");
		String endDate=(String)input.getFormdata().get("endDate");
		StringBuilder sql=new StringBuilder();
		sql.append(" (( ");
		sql.append(this.needNisBillHeadData(type));
		sql.append(" from nis_billhead n");
		sql.append(" left join fc_voucher_type f on f.id=n.voucher_type_id");
		sql.append(" left join bis_exc b on n.id=b.cbs_bill_id and b.VOUCHER_FROM= '1'");
		sql.append(" where n.is_payed='0' and f.SYS_VOUCHER_CODE in('02','33','35','34')  and b.VOUCHER_STAT='0' and n.IS_FORINDIVIDUAL='0'");
		if(StringUtils.isNotEmpty(startDate)){
			sql.append(" and b.TRANS_TIME>=:startTime");
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql.append(" and b.TRANS_TIME <=:endTime");
		}
		sql.append(" )union all(");
		sql.append(this.needNisBillHeadData(type));
		sql.append(" from nis_billhead n ");
		sql.append(" left join fc_voucher_type f on f.id = n.voucher_type_id ");
		sql.append(" left join bt_bank_acc acc on n.payer_acc=acc.bank_acc");
		sql.append(" where (acc.is_online='0' or n.is_payed='1')");
		sql.append(" and n.status>= 95 and n.IS_FORINDIVIDUAL='0'");
		if(StringUtils.isNotEmpty(startDate)){
			sql.append(" and  n.wish_payday>=:startDate");
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql.append(" and  n.wish_payday <=:endDate");
		}
		sql.append(" ) union all(");
		sql.append(this.needBisExcData(type));
		sql.append(" from bis_exc b ");
		sql.append(" left join fc_voucher_type f on f.id = b.VOUCHER_TYPE_ID ");
		sql.append(" where b.VOUCHER_FROM= '11' and b.VOUCHER_STAT='0' ");
		if(StringUtils.isNotEmpty(startDate)){
			sql.append(" and b.TRANS_TIME>=:startTime");
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql.append(" and b.TRANS_TIME <=:endTime");
		}
		sql.append(") ) d");
		//分页查出nis_billhead上面对应的结果，取每页多少条数，通过corpId到sys查询结果, 然后根据结果筛选
		sql.append(" left join sys_external_corp_bankacc s on s.EXTERNAL_ACC=d.payeeAcc");
		sql.append(" left join SYS_EXTERNAL_CORP sc on s.EXTERNAL_CORP_ID= sc.id");
		sql.append(" left join sys_corp syc on syc.id=d.corpId");
		sql.append(" left join bt_currency bt on bt.id=d.curId");
		sql.append(" where  sc.name is not null");
		 if (!EmptyUtils.isEmptyList(corpAuth)) {
				sql.append(" and ").append(getSQLIn(corpAuth,900,"d.corpId"));
			}
		
		return sql.toString();
	}
	
	
	
}
