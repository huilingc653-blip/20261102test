package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.FcAccForBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.constants.InnerAccountConstant;
import com.bytter.system.entity.BtBankAcc;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
* @Title: Dao
* @Description: BT_BANK_ACC
* @author onlineGenerator
* @date 2017-06-02 17:50:16
* @version 1.0
*
*/
@Repository
public class BtBankAccMaintainDao extends BaseDao<BtBankAcc,String>{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,String joinSql) {
		StringBuilder sql=new StringBuilder();
		//这里查询的output关联的BtBankAccVO与其他地方的有冲突 这里关联关系做特殊处理
		if(dataTablesInput.getOrder()!=null && dataTablesInput.getOrder().size()>0){
			//单独处理人民币排序
			Integer curNameOrderIndex = dataTablesInput.getColumnIndex("curName");
			if(curNameOrderIndex == dataTablesInput.getOrder().get(0).getColumn()){
				Order order = dataTablesInput.getOrder().get(0);
				dataTablesInput.setOrderString(new OrderString("acccur.cur_id", order.getDir()));
				dataTablesInput.setOrder(null);
			}
		}
		Map map = super.getWhereConditionAndValues(dataTablesInput, new BtBankAccVO(), "", false);
		Map params = (Map<String, Object>)map.get("value");
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,corp.name as corpName, ");
		sql.append(" acccur.counter_acc as counterAcc ,acccur.super_counter_acc as superCurrentAcc, acccur.cur_id as curId,bac.cur_name AS curName, ");
		sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, acc.corp_id as corpId,corp.parent_id as netId, ");
		sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName,acc.bank_code as bankCode,acc.reg_date as  regDate,");
		sql.append(" acc.electric_bill as electricBill,acc.is_online as isOnline,acc.valid_sign as validSign,bif.name as bifName ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join sys_corp corp on acc.corp_id = corp.id ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
		sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
		sql.append(" LEFT JOIN bt_currency bac ON bac.id=acccur.cur_id ");
		sql.append(" where acc.status = "+BtBankAccConstant.STATUE_CHECK+"  and acc.valid_sign = "+BtBankAccConstant.VALIDSIGN_1+" " );
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(joinSql);
		}
		/**
		 * 是否相联
		 */
		String isRelation = dataTablesInput.getPara("isRelation");
		  if(!StringUtils.isEmpty(isRelation) && BtBankAccConstant.RELATION_NO.equals(isRelation)){
			  sql.append(" and acccur.counter_acc is null ");
	      }else if(!StringUtils.isEmpty(isRelation) && BtBankAccConstant.RELATION_YES.equals(isRelation)){
			  sql.append(" and acccur.counter_acc is not null ");
	      }
		/**
		 * 网点查询条件
		 */
		String netId = dataTablesInput.getPara("netId");
		if(!StringUtils.isEmpty(netId.trim())){
			sql.append(" and (acc.corp_id =:corpId or corp.net_id =:netId) ");
			params.put("corpId", netId);
			params.put("netId", netId);
		}
		String poolSign = dataTablesInput.getFormdata().get("poolSign")+"";
		if(!StringUtils.isEmpty(poolSign.trim())){
			sql.append(" and acc.is_capital_pool =:poolSign");
			params.put("poolSign", poolSign);
		}
		sql.append(map.get("where"));
		return  this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, BtBankAccVO.class);
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableForCounterAcc(DataTablesInput dataTablesInput,String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.BLNCTR_ACC as blnctrAcc,acc.ACC_NAME as accName,acc.acc_type as accType, ");
		sql.append(" acc.net_id AS netId,nc.name AS netName, acc.corp_id AS corpId,sc.name AS corpName, acc.cur_id AS curId,ac.cur_name AS curName,");
		sql.append(" acc.ACC_OPEN_TIME as accOpenTime,acc.pool_sign as poolSign");
		sql.append(" from fc_acc acc left join fc_subject f on acc.subject_id=f.id ");
		sql.append(" LEFT JOIN sys_corp nc ON nc.id=acc.net_id LEFT JOIN sys_corp sc ON sc.id=acc.corp_id LEFT JOIN bt_currency ac ON ac.id=acc.cur_id ");
		sql.append(" where acc.pool_sign<>'1'");
		sql.append(" and acc.acc_state not in ('" +
				InnerAccountConstant.ACC_STATE_DESTORY+"'," +
				"'"+InnerAccountConstant.ACC_STATE_BACK+"'," +
				"'"+InnerAccountConstant.ACC_STATE_NEW+"'," +
				"'"+InnerAccountConstant.ACC_STATE_TEMP+"'," +
				"'"+InnerAccountConstant.ACC_STATE_Z+"'," +
				"'"+InnerAccountConstant.ACC_STATE_U+"'," +
				"'"+InnerAccountConstant.ACC_STATE_REQUISITION+"') ");
		//账户类别过滤
		if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqs_accType"))
				&& !EmptyUtils.isBlank(dataTablesInput.getFormdata().get("eqs_accType").toString()) ){
			sql.append(" and acc.acc_type = '").append(dataTablesInput.getFormdata().get("eqs_accType")).append("'");
		}
		//内部账号类别过滤
		if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_blnctrAcc"))
				&& !EmptyUtils.isBlank(dataTablesInput.getFormdata().get("likes_blnctrAcc").toString()) ){
			sql.append(" and acc.blnctr_acc like '%").append(dataTablesInput.getFormdata().get("likes_blnctrAcc")).append("%'");
		}
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput,sql.toString(),FcAccForBankAccDTO.class,"acc",false);
	}
	/**
	 * 资金池内部账号选择
	 * @param dataTablesInput
	 * @param joinSql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableForPoolCounterAcc(DataTablesInput dataTablesInput,String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.BLNCTR_ACC as blnctrAcc,acc.ACC_NAME as accName,acc.acc_type as accType, ");
		sql.append(" acc.net_id AS netId,nc.name AS netName,acc.net_id AS netPoolId,nc.name AS netPoolName,acc.corp_id as corpId,sc.name AS corpName,acc.cur_id as curId,bcur.cur_name AS curName,acc.ACC_OPEN_TIME as accOpenTime,acc.pool_sign as poolSign");
		sql.append(" from fc_acc acc left join sys_corp sc ON sc.id=acc.corp_id LEFT JOIN sys_corp nc ON nc.id=sc.net_id ");
		sql.append("  LEFT JOIN bt_currency bcur ON bcur.id=acc.cur_id left join fc_subject_pool f on acc.subject_id=f.id where 1=1 ");

		sql.append( " and acc.pool_sign='1'");
		sql.append(" and acc.acc_state not in ('" +
				InnerAccountConstant.ACC_STATE_DESTORY+"'," +
				"'"+InnerAccountConstant.ACC_STATE_BACK+"'," +
				"'"+InnerAccountConstant.ACC_STATE_NEW+"'," +
				"'"+InnerAccountConstant.ACC_STATE_TEMP+"'," +
				"'"+InnerAccountConstant.ACC_STATE_Z+"'," +
				"'"+InnerAccountConstant.ACC_STATE_U+"'," +
				"'"+InnerAccountConstant.ACC_STATE_REQUISITION+"') ");
		//新增银行活期只能对应一个真实账号的过滤
		sql.append(" and acc.blnctr_acc not in (select b.counter_acc from bt_bank_acc_cur b where b.counter_acc like '%1020%') ");
		//账户类别过滤
//		if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqs_accType"))
//				&& !EmptyUtils.isBlank(dataTablesInput.getFormdata().get("eqs_accType").toString()) ){
//			sql.append(" and acc.acc_type = '").append(dataTablesInput.getFormdata().get("eqs_accType")).append("'");
//		}
//		//内部账号类别过滤
//		if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_blnctrAcc"))
//				&& !EmptyUtils.isBlank(dataTablesInput.getFormdata().get("likes_blnctrAcc").toString()) ){
//			sql.append(" and acc.blnctr_acc like '%").append(dataTablesInput.getFormdata().get("likes_blnctrAcc")).append("%'");
//		}
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(joinSql);
		}


		return getDynamicBySQL(dataTablesInput,sql.toString(),FcAccForBankAccDTO.class,"acc",false);
	}
}
