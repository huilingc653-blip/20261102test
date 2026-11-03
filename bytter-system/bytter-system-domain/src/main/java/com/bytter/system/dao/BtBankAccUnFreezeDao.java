package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.entity.BtBankAccFreeze;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: BT_BANK_ACC_FREEZE
 * @date 2017-06-14 14:26:46
 */
@Repository
public class BtBankAccUnFreezeDao extends BaseDao<BtBankAccFreeze, String> {

	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,corp.name as corpName, ");
		sql.append(" acccur.cur_id as curId, c.cur_name curName, ");
		sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, ");
		sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName,acc.reg_date, ");
		sql.append(" acc.electric_bill as electricBill ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join sys_corp corp on acc.corp_id = corp.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
		sql.append(" left join bt_currency c on c.id = acccur.cur_id ");
		sql.append(" where 1=1 and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.VALID_SIGN = " + BtBankAccConstant.VALIDSIGN_1 + " and acc.IS_ONLINE = " + BtBankAccConstant.ISONLINE_0 + " ");
		sql.append(" and exists (select 1 from BT_BANK_ACC_FREEZE where 1 = 1 and bt_nvl_num((select sum(freeze_money) from BT_BANK_ACC_FREEZE where uf_flag = " + BtBankAccConstant.UFFLAG_0 + " and status = " + BtBankAccConstant.STATUE_CHECK + " and bank_acc_id = acc.id and cur_id = acccur.cur_id ),");
		sql.append("   0) - bt_nvl_num((select sum(freeze_money) from BT_BANK_ACC_FREEZE  where uf_flag = " + BtBankAccConstant.UFFLAG_1 + " and status = " + BtBankAccConstant.STATUE_CHECK + " and bank_acc_id = acc.id and cur_id = acccur.cur_id ), 0) > 0)");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		if (EmptyUtils.isEmptyList(dataTablesInput.getOrder())) {
			dataTablesInput.setOrderString(new OrderString("acc.reg_date", "desc"));
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false);
	}

	/**
	 * 账户全部冻结解冻
	 *
	 * @param accId
	 * @param curId
	 * @return
	 */
	public List<BtBankAccFreezeDTO> getBtBankAccFreeze(String accId, String curId) {
		List<BtBankAccFreezeDTO> list = new ArrayList<BtBankAccFreezeDTO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAccFreeze bf where bf.bankAccId = :bankAccId and bf.curId = :curId and bf.status = " + BtBankAccConstant.STATUE_CHECK + "");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAccId", accId);
		params.put("curId", curId);
		list = this.listByHQLAliasToBean(hql.toString(), params, BtBankAccFreezeDTO.class);
		return list;
	}
	/**
	 * 账户解冻暂存
	 *
	 * @param accId
	 * @param curId
	 * @return
	 */
	public List<BtBankAccFreezeDTO> getBtBankAccFreezeTemp(String accId, String curId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAccFreeze bf where bf.bankAccId = :bankAccId and bf.curId = :curId and (bf.status = " + BtBankAccConstant.STATUE_TEMP + " or bf.status = " + BtBankAccConstant.STATUE_BACK + ") ");
		hql.append(" and bf.ufFlag = " + BtBankAccConstant.UFFLAG_1 + " ");
		hql.append(" order by bf.freezeNo desc ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAccId", accId);
		params.put("curId", curId);
		List<BtBankAccFreezeDTO> list = this.listByHQLAliasToBean(hql.toString(), params, BtBankAccFreezeDTO.class);
		return list;
	}
	/**
	 * 账户冻结提交
	 *
	 * @param accId
	 * @param curId
	 * @return
	 */
	public List<BtBankAccFreezeDTO> getBtBankAccUnFreeze(String accId, String curId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAccFreeze bf where bf.bankAccId = :bankAccId and bf.curId = :curId and bf.status = " + BtBankAccConstant.STATUE_CHECK + " ");
		hql.append(" and bf.ufFlag = " + BtBankAccConstant.UFFLAG_0 + " ");
		hql.append(" order by bf.freezeNo desc ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAccId", accId);
		params.put("curId", curId);
		List<BtBankAccFreezeDTO> list = this.listByHQLAliasToBean(hql.toString(), params, BtBankAccFreezeDTO.class);
		return list;
	}
	/**
	 * 得到账户冻结金额
	 *
	 * @param accId
	 * @param curId
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public BigDecimal getFreezeMoney(String accId, String curId) {
		BigDecimal freezeNum = BigDecimal.ZERO;
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(bf.freeze_money) as freezeMoney from BT_BANK_ACC_FREEZE bf ");
		sql.append(" where bf.BANK_ACC_ID = :bankAccId and bf.cur_id = :curId and bf.uf_flag = " + BtBankAccConstant.UFFLAG_0 + " and bf.status = " + BtBankAccConstant.STATUE_CHECK + " ");
		Map params = new HashMap();
		params.put("bankAccId", accId);
		params.put("curId", curId);
		List<BtBankAccFreezeDTO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccFreezeDTO.class);
		if (null != list && list.size() > 0 && null != list.get(0).getFreezeMoney()) {
			freezeNum = list.get(0).getFreezeMoney();
		}
		return freezeNum;
	}
	/**
	 * 得到账户解冻金额
	 *
	 * @param accId
	 * @param curId
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public BigDecimal getUfFreezeMoney(String accId, String curId) {
		BigDecimal ufFreezeNum = BigDecimal.ZERO;
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(bf.freeze_money) as freezeMoney from BT_BANK_ACC_FREEZE bf ");
		sql.append(" where bf.BANK_ACC_ID = :bankAccId and bf.cur_id = :curId and bf.uf_flag = " + BtBankAccConstant.UFFLAG_1 + " and status = " + BtBankAccConstant.STATUE_CHECK + " ");
		Map params = new HashMap();
		params.put("bankAccId", accId);
		params.put("curId", curId);
		List<BtBankAccFreezeDTO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccFreezeDTO.class);
		if (null != list && list.size() > 0 && null != list.get(0).getFreezeMoney()) {
			ufFreezeNum = list.get(0).getFreezeMoney();
		}
		return ufFreezeNum;
	}
	/**
	 * 得到账户已解冻金额
	 *
	 * @param accId
	 * @param curId
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public BigDecimal getUfFreezeMoney(String accId, String curId, String uufid) {
		BigDecimal ufFreezeNum = BigDecimal.ZERO;
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(bf.freeze_money) as freezeMoney from BT_BANK_ACC_FREEZE bf ");
		sql.append(" where bf.BANK_ACC_ID = :bankAccId and bf.cur_id = :curId and bf.F_UUID = :uufid and  bf.uf_flag = " + BtBankAccConstant.UFFLAG_1 + " and status = " + BtBankAccConstant.STATUE_CHECK + " ");
		Map params = new HashMap();
		params.put("bankAccId", accId);
		params.put("curId", curId);
		params.put("uufid", uufid);
		List<BtBankAccFreezeDTO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccFreezeDTO.class);
		if (null != list && list.size() > 0 && null != list.get(0).getFreezeMoney()) {
			ufFreezeNum = list.get(0).getFreezeMoney();
		}
		return ufFreezeNum;
	}
	/**
	 * 得到账户在途解冻冻金额
	 *
	 * @param accId
	 * @param curId
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public BigDecimal getUfWaitFreezeMoney(String accId, String curId, String uufid) {
		BigDecimal ufFreezeNum = BigDecimal.ZERO;
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(bf.freeze_money) as freezeMoney from BT_BANK_ACC_FREEZE bf ");
		sql.append(" where bf.BANK_ACC_ID = :bankAccId and bf.cur_id = :curId and bf.F_UUID = :uufid and  bf.uf_flag = " + BtBankAccConstant.UFFLAG_1 + "  and (status = " + BtBankAccConstant.STATUE_BACK + " " +
				 " or status = " + BtBankAccConstant.STATUE_WAIT_CHECK + ") ");
		Map params = new HashMap();
		params.put("bankAccId", accId);
		params.put("curId", curId);
		params.put("uufid", uufid);
		List<BtBankAccFreezeDTO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccFreezeDTO.class);
		if (null != list && list.size() > 0 && null != list.get(0).getFreezeMoney()) {
			ufFreezeNum = list.get(0).getFreezeMoney();
		}
		return ufFreezeNum;
	}
	/**
	 * 得到账户冻结金额
	 *
	 * @param id
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public BigDecimal getFreezeMoney(String id) {
		BigDecimal ufFreezeNum = BigDecimal.ZERO;
		StringBuilder sql = new StringBuilder();
		sql.append(" select bf.freeze_money as freezeMoney from BT_BANK_ACC_FREEZE bf ");
		sql.append(" where bf.id = :id  and bf.uf_flag = " + BtBankAccConstant.UFFLAG_0 + " and status = " + BtBankAccConstant.STATUE_CHECK + " ");
		Map params = new HashMap();
		params.put("id", id);
		List<BtBankAccFreezeDTO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccFreezeDTO.class);
		if (null != list && list.size() > 0 && null != list.get(0).getFreezeMoney()) {
			ufFreezeNum = list.get(0).getFreezeMoney();
		}
		return ufFreezeNum;
	}
	/**
	 * 得到账户解冻金额
	 *
	 * @param accId
	 * @param curId
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public BigDecimal getUfFreezeMoneyForTemp(String accId, String curId, String uufid) {
		BigDecimal ufFreezeNum = BigDecimal.ZERO;
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(bf.freeze_money) as freezeMoney from BT_BANK_ACC_FREEZE bf ");
		sql.append(" where bf.BANK_ACC_ID = :bankAccId and bf.cur_id = :curId and bf.F_UUID = :uufid and bf.uf_flag = " + BtBankAccConstant.UFFLAG_1 + " and status <> " + BtBankAccConstant.STATUE_DEL + " ");
		Map params = new HashMap();
		params.put("bankAccId", accId);
		params.put("curId", curId);
		params.put("uufid", uufid);
		List<BtBankAccFreezeDTO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccFreezeDTO.class);
		if (null != list && list.size() > 0 && null != list.get(0).getFreezeMoney()) {
			ufFreezeNum = list.get(0).getFreezeMoney();
		}
		return ufFreezeNum;
	}
	/**
	 * 查询审核列表信息
	 *
	 * @param dataTablesInput
	 * @param businessSql
	 * @return
	 */
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select freeze.id as id,freeze.FREEZE_NO as freezeNo,freeze.corp_id as corpId,freeze.cur_id as curId,cur.cur_name as curName,");
		sql.append(" freeze.FREEZE_MONEY as freezeMoney,freeze.FREEZE_DATE as freezeDate, freeze.bank_acc_id AS bankAccId ");
		sql.append(" from bt_bank_acc_freeze freeze ");
		sql.append(" LEFT JOIN bt_currency cur ON freeze.cur_id = cur.id");
		sql.append(" where 1=1");
		sql.append(" and ").append(businessSql);
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccFreezeDTO.class, "freeze", false);
	}
}
