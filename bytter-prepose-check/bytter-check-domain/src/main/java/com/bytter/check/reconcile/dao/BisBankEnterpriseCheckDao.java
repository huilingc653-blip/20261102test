package com.bytter.check.reconcile.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.check.reconcile.vo.BisBankEnterpriseCheckVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BisBankEnterpriseCheckDao extends BaseDao<BisBankEnterpriseCheckVo, String>{
	private static StringBuilder initSql(){
		return new StringBuilder().append(" select   ").
				append(" bcd.id as id,bcd.CORP_ID as corpId,BT_CONCAT4('(',s.code,')',s.name) as corpCodeAndName,").
				append(" (select bbt.type_Name from bt_bank_type bbt where bbt.id=bba.bank_type_id) as bankTypeName,bba.bank_type_id as bankTypeId,").
				append(" bcd.BILL_NO as billNo,bba.bank_name as bankName,bcd.BANK_ACC as bankAcc,bcd.ACC_NAME as accName,").
				append(" bcd.CUR_ID as curId,c.CUR_NAME as curName,bcd.REASON_ID as appScope,bba.acc_type as accType,").
				append(" bcd.CHECK_DATE as checkDate,bcd.BAL as bal,bcd.TOBILL_DATE as toBillDate,n.nature_name as accNature,").
				append(" bcd.VOUCHER_STAT as voucherStat,bcd.status as status,case when bcd.expand_Fields is null then bcd.bank_acc else bcd.expand_Fields end as expandFields ").
				append(" from bis_check_dtl bcd").
				append(" left join sys_corp s on bcd.corp_id = s.id").
				append(" left join BT_CURRENCY c on c.id = bcd.cur_Id").
				append(" left join bt_bank_acc bba on bba.bank_acc = bcd.BANK_ACC").
				append(" left join bt_acc_nature n on bba.nature_id = n.id ").
				append(" where 1=1");
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BisBankEnterpriseCheckVo> getDataTablesUnion(DataTablesInput input, List<String> corpAuthList,List<String> accList) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder(initSql())
				.append(" and bcd.voucher_stat in (:voucherStat)");
		String time = (String) input.getFormdata().get("tobillDate");
		if (StringUtils.isNotBlank(time)) {
			time = time + "-01";
			sql.append(" and bcd.TOBILL_DATE >= :tobillDateBegin");
			sql.append(" and bcd.TOBILL_DATE <= :tobillDateEnd");
			params.put("tobillDateBegin", DateUtils.getFirstMonthDate(DateUtils.parseDate(time)));
			params.put("tobillDateEnd", DateUtils.getLastMonthDate(DateUtils.parseDate(time)));
		}
		// 对账账户
		String checkAccount = (String) input.getFormdata().get("checkAccount");
		if (StringUtils.isNotBlank(checkAccount)) {
			sql.append(" and case when bcd.expand_Fields is null then bcd.bank_acc like :checkAccount else bcd.expand_Fields like :checkAccount end ");
			params.put("checkAccount", "%" + checkAccount + "%");
		}
		// 是否保证金
		String isBail = (String) input.getFormdata().get("isBail");
		if (StringUtils.isNotBlank(isBail)) {
			if (CommonConstant.YES.equals(isBail)) {
				sql.append(" and UPPER(bcd.expand_Fields) like :isBail ");
				params.put("isBail", "%|Y%");
			}
			if (CommonConstant.NO.equals(isBail)) {
				sql.append(" and (bcd.expand_Fields is null or UPPER(bcd.expand_Fields) not like :isBail) ");
				params.put("isBail", "%|Y%");
			}
		}
		params.put("voucherStat", new String[]{
				BdlinkConstant.BANK_FAILED,
				BdlinkConstant.WAIT_FILL,
				BdlinkConstant.INTERFACE_FAILED
		});
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "bcd.CORP_ID", "corpIdList", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if (accList.size() > 0) {
			String accSql = SqlUtil.getInSqlByParams(accList, "bcd.BANK_ACC", "bankAccList", params);
			sql.append(" and ").append(accSql).append(" ");
		}
		return getDynamicBySQL(input, sql.toString(), BisBankEnterpriseCheckVo.class, "", false, params);
	}

	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BisBankEnterpriseCheckVo> getCheckBusData(DataTablesInput input, List<String> corpAuthList,String bankTypeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder(initSql());
		// 对账账户
		String checkAccount = (String) input.getFormdata().get("checkAccount");
		if (StringUtils.isNotBlank(checkAccount)) {
			sql.append(" and case when bcd.expand_Fields is null then bcd.bank_acc like :checkAccount else bcd.expand_Fields like :checkAccount end ");
			params.put("checkAccount", "%" + checkAccount + "%");
		}
		// 是否保证金
		String isBail = (String) input.getFormdata().get("isBail");
		if (StringUtils.isNotBlank(isBail)) {
			if (CommonConstant.YES.equals(isBail)) {
				sql.append(" and UPPER(bcd.expand_Fields) like :isBail ");
				params.put("isBail", "%|Y%");
			}
			if (CommonConstant.NO.equals(isBail)) {
				sql.append(" and (bcd.expand_Fields is null or UPPER(bcd.expand_Fields) not like :isBail) ");
				params.put("isBail", "%|Y%");
			}
		}
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "bcd.corp_id","corpId", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if (ObjectUtil.isNotEmpty(bankTypeId)){
			sql.append(" AND bba.BANK_TYPE_ID = :bankTypeId ");
			params.put("bankTypeId", bankTypeId);
		}
		return getDynamicBySQL(input, sql.toString(), BisBankEnterpriseCheckVo.class, "bcd", false, params);
	}



	public List<BisBankEnterpriseCheckVo> queryList(Map<String,Object> params){
		StringBuilder sql = new StringBuilder(initSql());
		if(params.containsKey("ids")){
			sql.append(" and bcd.id in (:ids)");
		}
   		List<BisBankEnterpriseCheckVo> listInfo = this.listBySQLAliasToBean(sql.toString(), params, BisBankEnterpriseCheckVo.class);
   		return listInfo;
	}
}
