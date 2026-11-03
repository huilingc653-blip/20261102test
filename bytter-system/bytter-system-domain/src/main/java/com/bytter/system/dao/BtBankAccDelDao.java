package com.bytter.system.dao;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.entity.BtBankAccApp;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银行账号变更
 * @date 2017-05-23 17:37:39
 */
@Repository
public class BtBankAccDelDao extends BaseDao<BtBankAccApp, String> {
	@SuppressWarnings({"rawtypes", "unchecked"})
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		// 户名 likes_accName
		String accName = (String) dataTablesInput.getFormdata().get("likes_accName");
		dataTablesInput.getFormdata().remove("likes_accName");

		// 开户行 likes_bankName
		String bankName = (String) dataTablesInput.getFormdata().get("likes_bankName");
		dataTablesInput.getFormdata().remove("likes_bankName");
		Map<String,Object> params = new HashMap<>();

		StringBuilder sql = new StringBuilder();
		sql.append("select acc.id, acc.bank_acc, acc.acc_name, acc.corp_id, acc.acc_type, att.id as attributeId, nat.id as natureId, ");
		sql.append(" acc.bank_type_id as bankTypeId, acc.bank_name, acc.status, acc.prov, acc.city, acc.electric_bill, acc.isForm,");
		sql.append(" acc.is_domestic_bank as isDomesticBank,nat.nature_name as natureName, att.attribute_name as attributeName,c.name as corpName,t.type_name as bankTypeName  from ( ");
		sql.append(" select acc.id, acc.bank_acc, acc.acc_name, acc.corp_id, acc.status, acc.acc_type, acc.attribute_id, acc.nature_id, acc.bank_type_id, acc.bank_name, acc.prov, acc.city, acc.electric_bill, 'acc' as isForm, acc.is_domestic_bank ");
		sql.append(" from bt_bank_acc acc left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
		// 过滤内部账户父子关系维护
		sql.append(" where (acccur.counter_acc is null or acccur.counter_acc = '' ) and (acccur.super_counter_acc is null or acccur.super_counter_acc ='')  " );
		// 过滤银行账户父子关系维护
		sql.append(" and not exists (select 1 from bt_bank_acc_cur t where t.prent_acc_id = acc.id ) ");
		sql.append(	"and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.valid_sign = " + BtBankAccConstant.VALIDSIGN_1 + " ");
		sql.append(" and acc.bank_acc not in (select bank_acc from bt_bank_acc_app where status <> " + BtBankAccConstant.STATUE_CANCELLATION + " and bank_acc is not null ) ");
		if(ObjectUtil.isNotEmpty(accName)){
			sql.append(" and acc.acc_name LIKE BT_CONCAT3('%',:accName,'%') ");
			params.put("accName",accName);
		}
		if (ObjectUtil.isNotEmpty(bankName)){
			sql.append(" and acc.bank_name LIKE BT_CONCAT3('%',:bankName,'%')  ");
			params.put("bankName",bankName);
		}
		sql.append(" union ");
		sql.append(" select acc.id, acc.bank_acc, acc.acc_name, acc.corp_id, acc.status,acc.acc_type, acc.attribute_id, acc.nature_id, acc.bank_type_id, acc.bank_name, acc.prov, acc.city, acc.electric_bill, 'app' as isForm, acc.is_domestic_bank ");
		sql.append(" from bt_bank_acc_app acc  ");
		sql.append(" where (acc.status = " + BtBankAccConstant.STATUE_TEMP + " or acc.status = " + BtBankAccConstant.STATUE_BACK + ")  and acc.bus_type = " + BtBankAccConstant.BUSTYPE_3 + " ");
		if(ObjectUtil.isNotEmpty(accName)){
			sql.append(" and acc.acc_name LIKE BT_CONCAT3('%',:accName,'%') ");
			params.put("accName",accName);
		}
		if (ObjectUtil.isNotEmpty(bankName)){
			sql.append(" and acc.bank_name LIKE BT_CONCAT3('%',:bankName,'%')  ");
			params.put("bankName",bankName);
		}
		sql.append(" ) acc ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join sys_corp c on acc.corp_id = c.id");
		sql.append(" left join bt_bank_type t on acc.bank_type_id = t.id");
		sql.append(" where 1 = 1 ");
		// 过滤实体账户引用
		sql.append(" and not exists (select 1 from bt_bank_acc a where a.entity_acc_id = acc.id) ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return this.dealDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccAppDTO.class, "", false,params);
	}
}
