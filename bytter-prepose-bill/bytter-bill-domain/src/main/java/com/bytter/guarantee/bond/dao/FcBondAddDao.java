package com.bytter.guarantee.bond.dao;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.bond.entity.FcBondAdd;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.vo.FcBondAccVO;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:FcBondAddDao
 * @Description:
 * @date 2018年9月12日 上午10:57:11
 */
@Repository
public class FcBondAddDao extends BaseDao<FcBondAdd, String> {
	/**
	 * 查询追加保证金列表
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<FcBondAddDTO> getBondAdditionDataTables(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select m.id as id, m.bond_id as bondId, m.bill_no as billNo,b.corp_id as corpId,b.bank_acc_id as bankAccId, ");
		sql.append(" b.bond_money as bondMoney,m.add_money as addMoney,b.cur_Id as curId, ");
		sql.append(" m.create_date as createDate,b.is_payed as isPayed, m.status  as status ");
		sql.append(" from FC_BOND b, FC_BOND_ADD m where 1=1");
		sql.append(" and b.id = m.bond_id and m.status in (0 ,-1,11) ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondAddDTO.class, "", false);
	}

	/**
	 * 查询存出保证金的账户列表
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<FcBondAccVO> queryFcBondVO(DataTablesInput dataTablesInput, List<String> bankAccIds) {
		/*StringBuilder sql = new StringBuilder();
		sql.append("select a.id as bondId,b.bank_acc as bankAcc,b.corp_id as corpId,");
		sql.append(" b.acc_name as accName,b.bank_name as bankName,");
		sql.append(" a.bond_money as bondMoney,a.cur_id as curId, (c.apply_money+c.freeze_money) as applyMoney,");
		sql.append(" (c.bond_money+c.add_money-c.back_money) as accBalance,");
		sql.append(" (c.bond_money+c.add_money-c.back_money-c.apply_money-c.freeze_money) as availMoney,");
		sql.append(" a.start_date as bondDate from fc_bond a,bt_bank_acc b,fc_bond_bal c");
		sql.append(" where a.bank_acc_id=b.id and a.id=c.bond_id and a.is_regular <> 1 ");
		sql.append("  and not exists  (select 1  from nis_allocation_req n ");
		sql.append("  left join bis_exc e on n.id = e.cbs_bill_id where a.id = n.erp_id and n.status <> -2 ");
		sql.append("  and ((n.status in (95,100) and e.voucher_stat <> '0') or (n.status not in (95,100)))  and n.bill_from = '3' )");*/
		StringBuilder sql = new StringBuilder();
		sql.append("select a.id as bondId,a.bank_acc_id as bankAccId,a.bank_acc AS bankAcc,");
		sql.append(" a.bond_money as bondMoney,a.cur_id as curId, (c.apply_money+c.freeze_money) as applyMoney,");
		sql.append(" (c.bond_money+c.add_money-c.back_money) as accBalance,");
		sql.append(" (c.bond_money+c.add_money-c.back_money-c.apply_money-c.freeze_money) as availMoney,");
		sql.append(" a.start_date as bondDate from fc_bond a,fc_bond_bal c");
		sql.append(" where a.id=c.bond_id  ");
//		sql.append(" and not exists  (select 1  from nis_allocation_req n ");
//		sql.append(" where a.id = n.erp_id and n.status <> -2 ");
//		//TODO 表nis_allocation_req中的n.voucher_stat 字段，需要通过消息同步 bis_exc
//		sql.append(" and ((n.status in (95,100) and n.voucher_stat <> '0') or (n.status not in (95,100)))  and n.bill_from = '3' )");
		String erpIds=dataTablesInput.getFormdata().get("erpIds")+"";
		if(!"null".equals(erpIds)){
			String[] ids = erpIds.split(",");
			List<String> idList = Arrays.asList(ids);
			sql.append(" and ").append(getSQLNotIn(idList, 900, "b.id"));
		}
        if (CollectionUtils.isNotEmpty(bankAccIds)) {
			sql.append(" and ").append(getSQLIn(bankAccIds,900,"a.bank_acc_id"));
        }else {
			sql.append(" and 1<>1 ");
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondAccVO.class, "n", false);
	}
	/**
	 * 查询待审批的追加保证金列表
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getAddApproveDataTables(DataTablesInput dataTablesInput, String businessSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select m.id as id, m.bond_id as bondId, m.bill_no as billNo,b.corp_id as corpId,sc.name as corpName,n.bank_Name as bankName,");
		sql.append(" n.bank_Acc as bankAcc,n.acc_Name as accName,b.bond_money as bondMoney,m.add_money as addMoney,b.cur_Id as curId,bc.cur_name as curName,  ");
		sql.append(" m.create_date as createDate,b.is_payed as isPayed, m.status  as status ");
		sql.append(" from FC_BOND b, BT_BANK_ACC n, FC_BOND_ADD m,");
		sql.append(" sys_corp sc, ");
		sql.append("  bt_currency bc ");
		sql.append(" WHERE b.bank_acc_id = n.id and b.id = m.bond_id  and m.status='11'");
		sql.append(" and sc.id=b.corp_id   ");
		sql.append(" and b.cur_id = bc.id  ");
		sql.append(" and ").append(businessSql);
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondAddDTO.class, "n", false);
	}
	/**
	 * 根据id查询追加保证金详细信息
	 */
	public FcBondAddDTO queryAddVOById(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.id as id,a.bill_no as billNo,a.bond_id as bondId,a.add_date as addDate,");
		sql.append(" a.is_payed as isPayed,a.add_money as addMoney,a.remark as Remark,a.status as status,");
		sql.append(" a.create_by as createBy,a.create_date as createDate,c.add_money as sumAddMoney,");
		sql.append(" a.out_bank_acc as outBankAcc,a.purpose");
		sql.append(" from fc_bond_add a inner join fc_bond b on a.bond_id = b.id ");
		sql.append(" inner join fc_bond_bal c on b.id = c.bond_id ");
//		sql.append(" left join bt_bank_acc o on a.out_bank_acc = o.bank_acc");
		sql.append(" where a.id=:id ");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, FcBondAddDTO.class);
	}
	/**
	 * 查询历史追加明细
	 */
	public List<FcBondAddDTO> queryAddHis(String bondId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select B.id as id,B.CREATE_BY as createBy,B.ADD_DATE as addDate,B.ADD_MONEY as addMoney,B.STATUS as status,b.out_bank_acc as outBankAcc ");
		sql.append(" from FC_BOND_ADD B WHERE B.STATUS='95' and B.BOND_ID=:bondId");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("bondId", bondId);
		return listBySQLAliasToBean(sql.toString(), params, FcBondAddDTO.class);
	}

	/**
	 * 查询历史追加明细
	 */
	public DataTablesOutput<FcBondAddDTO> addHisDatatable(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select B.id as id,B.CREATE_BY as createBy,B.ADD_DATE as addDate,B.ADD_MONEY as addMoney,B.STATUS as status,b.out_bank_acc as outBankAcc ");
		sql.append(" from FC_BOND_ADD B WHERE B.STATUS='95'  ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondAddDTO.class, "", false);
	}

}
