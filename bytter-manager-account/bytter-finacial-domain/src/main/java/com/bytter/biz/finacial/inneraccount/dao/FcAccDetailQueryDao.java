package com.bytter.biz.finacial.inneraccount.dao;

import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.api.dto.FcAccDetailDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Dao
 * @Description: 内部账户信息表
 * @date 2017-05-25 19:36:12
 */
@Repository
public class FcAccDetailQueryDao extends BaseDao<FcAccbook, String> {

//	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,String joinSql,Map<String,Object> param) {
		StringBuilder sql = new StringBuilder();
		StringBuilder str = new StringBuilder();
		Map<String, Object> paramMoney = new HashMap<>();
		Map<String,Object> map1 = new HashMap<>();
		String minMoney = dataTablesInput.getPara("minMoney");
		String maxMoney = dataTablesInput.getPara("maxMoney");
		StringBuilder sDebit = new StringBuilder();//支出
		StringBuilder bCredit = new StringBuilder();//收入
		boolean flag = false;//金额是否过滤
		if(StringUtils.isNotBlank(minMoney)){
			sDebit.append("s.debitMoney >=:geDmoney");
			bCredit.append("s.creditMoney >=:geCmoney");
			map1.put("geDmoney", minMoney);
			map1.put("geCmoney", minMoney);
			flag = true;
		}
		if(StringUtils.isNotBlank(maxMoney)){
			if(sDebit.length()>0){
				sDebit.append(" and ").append(" s.debitMoney<=:leDmoney");
			}else{
				sDebit.append(" s.debitMoney<=:leDmoney");
			}
			if(bCredit.length()>0){
				bCredit.append(" and ").append(" s.creditMoney<=:leCmoney");
			}else{
				bCredit.append(" s.creditMoney<=:leCmoney");
			}
			map1.put("leDmoney", maxMoney);
			map1.put("leCmoney", maxMoney);
			flag = true;
		}
		if(flag){
			//这不是sql注入，金额查询特殊处理，条件是由上面拼接出来的
			str.append(" and ((").append(sDebit.toString()).append(") or (").append(bCredit.toString()).append(")) ");
			paramMoney.putAll(map1);
		}
		//前台过滤查询字段拼接及排序字段拼接
		String[] needParams = {"corpId","voucherType","account","accName","curId","externalName","summaryContent","interestStartDate","tallyDate","debitMoney","creditMoney","balance","origVoucherNo","voucherNo","accType"};
		Map map = super.getWhereConditionAndValues(dataTablesInput, new FcAccDetailDTO(), "s", false, needParams);
		Map<String, Object> params = (Map<String, Object>)map.get("value");
		params.putAll(paramMoney);
		params.putAll(param);
		str.append(map.get("where"));
		sql.append(getCommonSearchSql());
		sql.append(getCommonUnionSearchSql(joinSql));
		sql.append(" ) s ");
		sql.append(" where 1=1 ");
		sql.append(str.toString());
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, FcAccDetailDTO.class);
	}

	private String getCommonSearchSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("select s.account as account,s.curId as curId,s.interestStartDate as interestStartDate,");
		sql.append("s.tallyDate as tallyDate,s.voucherNo as voucherNo,s.summaryContent as summaryContent,s.debitMoney as debitMoney,");
		sql.append("s.creditMoney as creditMoney,s.voucherType as voucherType,s.balance as balance,s.externalName as externalName,");
		sql.append("s.externalAcc as externalAcc,s.origVoucherNo as origVoucherNo,s.accName as accName,s.corpId as corpId,s.accType as accType from (");
		return sql.toString();
	}
	/**
	 * 查询字段
	 * 2017/12/22 add by zxb
	 * @return
	 */
	private String getCommonSearch(){
		StringBuilder sql = new StringBuilder();
		sql.append(" accbook.id as id, detail.account as account, ");
		sql.append(" detail.cur_code as curId,detail.summary_content as summaryContent, detail.debit_money as debitMoney,");
		sql.append(" detail.credit_money as creditMoney,detail.balance as balance,detail.orig_voucher_no as origVoucherNo,");
		sql.append(" accbook.interest_start_date as interestStartDate,accbook.tally_date as tallyDate, accbook.voucher_no as voucherNo,");
		sql.append(" accbook.voucher_type as voucherType,accbook.generate_mode as generateMode, accbook.voucher_state as voucherState,");
		sql.append(" ext.external_corp as externalName,ext.external_acc as externalAcc,detail.acc_name as accName,f.corp_id as corpId,f.acc_type as accType ");
		return sql.toString();
	}
	/**
	 * 查询条件
	 *  2017/12/22 add by zxb
	 * @return
	 */
	private String getCommonSearchCondition(String whereSql){
		StringBuilder sql = new StringBuilder();
		sql.append(" where accbook.id = detail.book_id and (accbook.voucher_no is not null) ");
		sql.append(" and (accbook.voucher_state <> 'B') and (accbook.generate_mode <> 'V') ");
		sql.append( whereSql );
		return sql.toString();
	}
	/**
	 * 查询UnionSql
	 * 2017/12/22 add by zxb
	 * @return
	 */
	private String getCommonUnionSearchSql(String whereSql){
		StringBuilder sql = new StringBuilder("select ");
		sql.append(getCommonSearch());
		sql.append(" from fc_accbook accbook" );
		sql.append(" left join fc_accbook_ext ext on accbook.id = ext.book_id" );
		sql.append(" ,fc_accbook_dtl  detail ");
		sql.append(" left join fc_acc f on detail.account = f.blnctr_acc ");
		sql.append(getCommonSearchCondition(whereSql));
		sql.append(" union ");
		sql.append(" select ").append(getCommonSearch());
		sql.append(" from fc_accbook_blned accbook" );
		sql.append(" left join fc_accbook_blned_ext ext on accbook.id = ext.book_id" );
		sql.append(" ,fc_accbook_blned_dtl  detail");
		sql.append(" left join fc_acc f on detail.account = f.blnctr_acc ");
		sql.append(getCommonSearchCondition(whereSql));
		return sql.toString();
	}
}
