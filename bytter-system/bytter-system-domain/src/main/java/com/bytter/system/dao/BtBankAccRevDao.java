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
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银行账号变更
 * @date 2017-05-23 17:37:39
 */
@Repository
public class BtBankAccRevDao extends BaseDao<BtBankAccApp, String> {
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		String likes_accName = (String) dataTablesInput.getFormdata().get("likes_accName");
		dataTablesInput.getFormdata().remove("likes_accName");

		// 开户行 likes_bankName
		String bankName = (String) dataTablesInput.getFormdata().get("likes_bankName");
		dataTablesInput.getFormdata().remove("likes_bankName");

		StringBuilder sql = new StringBuilder();
		sql.append("select acc.id, acc.bank_acc, acc.acc_name, acc.corp_id, acc.acc_type,  ");
		sql.append("att.id as attributeId, nat.id as natureId, acc.bank_type_id as bankTypeId, ");
		sql.append("acc.bank_name, acc.status, acc.prov, acc.city, acc.electric_bill, acc.isForm,");
		sql.append(" acc.is_domestic_bank as isDomesticBank,nat.nature_name as natureName, att.attribute_name as attributeName,c.name as corpName,t.type_name as bankTypeName from ( ");
		sql.append(" select acc.id, acc.bank_acc, acc.acc_name, acc.corp_id, acc.status, acc.acc_type, acc.attribute_id, acc.nature_id, acc.bank_type_id, acc.bank_name, acc.prov, acc.city, acc.electric_bill, 'acc' as isForm, acc.is_domestic_bank ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" where acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.valid_sign = " + BtBankAccConstant.VALIDSIGN_1 + " ");
		sql.append(" and acc.bank_acc not in (select bank_acc from bt_bank_acc_app where status <> " + BtBankAccConstant.STATUE_CANCELLATION + " and bank_acc is not null ) ");
		if(EmptyUtils.isNotEmpty(likes_accName)){
			sql.append(" and acc.acc_name like '%"+likes_accName+"%' ");
		}
		if (ObjectUtil.isNotEmpty(bankName)){
			sql.append(" and acc.bank_name like '%"+bankName+"%' ");
		}
		sql.append(" union ");
		sql.append(" select acc.id, acc.bank_acc, acc.acc_name, acc.corp_id, acc.status,acc.acc_type, acc.attribute_id, acc.nature_id, acc.bank_type_id, acc.bank_name, acc.prov, acc.city, acc.electric_bill, 'app' as isForm, acc.is_domestic_bank ");
		sql.append(" from bt_bank_acc_app acc ");
		sql.append(" where (acc.status = " + BtBankAccConstant.STATUE_TEMP + " or acc.status = " + BtBankAccConstant.STATUE_BACK + ")  and acc.bus_type = " + BtBankAccConstant.BUSTYPE_2 + " ");
		if(EmptyUtils.isNotEmpty(likes_accName)){
			sql.append(" and acc.acc_name like '%"+likes_accName+"%' ");
		}
		if (ObjectUtil.isNotEmpty(bankName)){
			sql.append(" and acc.bank_name like '%"+bankName+"%' ");
		}
		sql.append(" ) acc ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join sys_corp c on acc.corp_id = c.id");
		sql.append(" left join bt_bank_type t on acc.bank_type_id = t.id");
		sql.append(" where 1 = 1 ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return this.dealDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccAppDTO.class, "", false);
	}
	/**
	*@Description 查询银行账号变更记录
	*@Param dataTablesInput
	*@Param joinSql
	*@return DataTablesOutput
	*/
	public DataTablesOutput getChangeDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select acc.id as id,");
		sql.append("	   'app' as isForm,");
		sql.append("	   acc.bank_acc as bankAcc,");
		sql.append("	   acc.acc_name as accName,");
		sql.append("	   acc.corp_id as corpId,");
		sql.append("	   acc.status as status,");
		sql.append("	   acc.acc_type as accType,");
		sql.append("	   acc.bank_type_id as bankTypeId,");
		sql.append("	   acc.bank_name as bankName,");
		sql.append("	   acc.attribute_Id as attributeId,");
		sql.append("	   acc.nature_Id as natureId,");
		sql.append("	   acc.op_ression as opRession,");
		sql.append("	   acc.create_by as createBy,");
		sql.append("	   acc.create_date as createDate,");
		sql.append("	   acc.update_by as updateBy,");
		sql.append("	   acc.update_date as updateDate,");
		sql.append("	   acc.attachment_id as attachmentId ");
		sql.append("	   from bt_bank_acc_app acc");
		sql.append("	   where acc.status in("+BtBankAccConstant.STATUE_BACK+",");
		sql.append(		   BtBankAccConstant.STATUE_TEMP+","+BtBankAccConstant.STATUE_WAIT_CHECK+","+BtBankAccConstant.STATUE_CHECK);
		sql.append(		   ","+BtBankAccConstant.STATUE_CANCELLATION+")");
		sql.append("	   and acc.bus_type = " + BtBankAccConstant.BUSTYPE_2 );
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return this.dealDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccAppDTO.class, "", false);
	}
}
