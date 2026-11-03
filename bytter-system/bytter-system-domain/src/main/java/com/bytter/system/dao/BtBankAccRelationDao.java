package com.bytter.system.dao;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.constants.BtBankAccConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Repository
public class BtBankAccRelationDao extends BaseDao<BtBankAcc, String> {
	/**
     * modify by qijh 子账号可以为离线账号，在线账号
	 * @param dataTablesInput
     * @param joinSql
     * @return
     */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankacc,acc.acc_name as accname,acc.corp_id as corpid,sc.name AS corpName, ");
		sql.append(" acc.acc_type as acctype,att.attribute_name as attributename, acccur.cur_id as curId,bcur.cur_name AS curName, ");
		sql.append(" nat.nature_name as naturename,banktype.type_name as banktypename,acc.bank_name as bankname, ");
		sql.append(" acc.prov as prov,acc.city as city, bif.name as bifname ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
		sql.append(" left join bt_currency bcur ON bcur.id=acccur.cur_id ");
		sql.append(" left join bis_acc_attribute att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
		sql.append(" left join sys_corp sc ON sc.id=acc.corp_id");
		sql.append(" where acc.status = " + BtBankAccConstant.STATUE_CHECK + "  and acc.valid_sign = " + BtBankAccConstant.VALIDSIGN_1);
		sql.append(" and acccur.prent_acc_id is null ");
		sql.append(" and acc.id not in (select prent_acc_id from bt_bank_acc_cur where bank_acc_id = :neid  and prent_acc_id is not null ) ");
		//过滤掉账户属性对上级无权限的账号
		sql.append(" and att.super_des<>'0'");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "acc", false);
	}
	/**
	 * 根据单位id得到在线银行账号
	 *
	 * @param corpId
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public List<BtBankAccVO> listBankAccInfosByCorpId(String corpId, String curId, String isOnline) {
		StringBuilder sql = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.bank_name as bankName,acccur.cur_id as curId,acccur.prent_acc_id as prentAccId,acc.corp_id as corpId,c.cur_name as curName ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
		sql.append(" left join bt_currency c on acccur.cur_id = c.id ");
		sql.append(" where acc.valid_sign = :validSign and acc.status = :status  ");
		if (!EmptyUtils.isBlank(corpId)) {
			sql.append(" and acc.corp_id = :corpId ");
			params.put("corpId", corpId);
		}
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and acccur.cur_id = :curId ");
			params.put("curId", curId);
		}
		if(!EmptyUtils.isBlank(isOnline)){
			sql.append(" and acc.is_online = :isOnline ");
			params.put("isOnline", isOnline);
		}
		params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
		params.put("status", BtBankAccConstant.STATUE_CHECK);
		sql.append(" order by acc.corp_id, acc.bank_acc");
		List<Map> mapResult = this.listBySQL(sql.toString(),params);
		List<BtBankAccVO> result= null;
		if(EmptyUtils.isNotEmpty(mapResult)){
			result=BeanUtils.copyMapListProperties(mapResult, BtBankAccVO.class);
		}
		return result;
//		return listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
	}
	/**
	 * 根据单位id得到在线银行账号
	 *
	 * @param corpId
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public List<BtBankAccVO> listBankAccByCorpId(String corpId, String curId, String isOnline,String isCapitalPool) {
		StringBuilder sql = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName, acc.parent_sign as parentSign,acc.corp_id as corpId, acc.acc_type as accType, ");
		sql.append(" acc.bank_code as bankCode, acc.bank_name as bankName, ");
		sql.append(" acc.prov as prov,acc.city as city,acc.bif_code as bifCode, acc.electric_bill as electricBill, ");
		sql.append(" acc.regulars, acc.regular_date as regularDate, acc.notice_type as noticeType,  ");
		sql.append(" acc.agr_money as agrMoney, acc.agr_interest as agrInterest, acc.agr_contract as agrContract, acc.agr_begin_date as agrBeginDate, acc.agr_end_date as agrEndDate, ");
		sql.append(" acc.rates as rates, acc.rates_float as ratesFloat, acc.rates_float_per as ratesFloatPer, ");
		sql.append(" acc.volume_num as volumeNum, acc.bankbook_num as bankbookNum, acc.interest_cycle as interestCycle, acc.interest_date as interestDate, ");
		sql.append(" acc.reg_date as regDate, acc.private_no as privateNo, acc.is_online as isOnline, case when acc.scan_flag is null then '0' else acc.scan_flag end  as scanFlag,  ");
		sql.append(" acc.init_balance_date as initBalanceDate, acc.init_balance as initBalance, acc.rmk as rmk, acc.valid_sign as validSign, ");
		sql.append(" acc.create_by as createBy, acc.create_date as createDate, acc.update_by as updateBy, acc.update_date as updateDate, ");
		sql.append(" acc.status as status, acc.entity_acc_id as entityAccId, acc.is_capital_pool as isCapitalPool, acc.circulation_mode as circulationMode, ");
		sql.append(" acccur.prent_acc_id as prentAccId, acccur.cur_id as curId ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
		sql.append(" where acc.valid_sign = :validSign and acc.status = :status  ");
		if (!EmptyUtils.isBlank(corpId)) {
			sql.append(" and acc.corp_id = :corpId ");
			params.put("corpId", corpId);
		}
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and acccur.cur_id = :curId ");
			params.put("curId", curId);
		}
		if(!EmptyUtils.isBlank(isOnline)){
			sql.append(" and acc.is_online = :isOnline ");
			params.put("isOnline", isOnline);
		}
		if(!EmptyUtils.isBlank(isCapitalPool)){
			sql.append(" and acc.is_capital_pool = :isCapitalPool ");
			params.put("isCapitalPool", isCapitalPool);
		}
		params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
		params.put("status", BtBankAccConstant.STATUE_CHECK);
		sql.append(" order by acc.corp_id, acc.bank_acc");
		return listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
	}
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<BtBankAccVO> getChildBankAccList(String prentAccId, String curId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName, acc.parent_sign as parentSign,acc.corp_id as corpId, acc.acc_type as accType, ");
		sql.append(" acc.bank_code as bankCode, acc.bank_name as bankName, ");
		sql.append(" acc.prov as prov,acc.city as city,acc.bif_code as bifCode, acc.electric_bill as electricBill, ");
		sql.append(" acc.regulars, acc.regular_date as regularDate, acc.notice_type as noticeType,  ");
		sql.append(" acc.agr_money as agrMoney, acc.agr_interest as agrInterest, acc.agr_contract as agrContract, acc.agr_begin_date as agrBeginDate, acc.agr_end_date as agrEndDate, ");
		sql.append(" acc.rates as rates, acc.rates_float as ratesFloat, acc.rates_float_per as ratesFloatPer, ");
		sql.append(" acc.volume_num as volumeNum, acc.bankbook_num as bankbookNum, acc.interest_cycle as interestCycle, acc.interest_date as interestDate, ");
		sql.append(" acc.reg_date as regDate, acc.private_no as privateNo, acc.is_online as isOnline, bt_nvl_char(acc.scan_flag,'0') as scanFlag,  ");
		sql.append(" acc.init_balance_date as initBalanceDate, acc.init_balance as initBalance, acc.rmk as rmk, acc.valid_sign as validSign, ");
		sql.append(" acc.create_by as createBy, acc.create_date as createDate, acc.update_by as updateBy, acc.update_date as updateDate, ");
		sql.append(" acc.status as status, acc.entity_acc_id as entityAccId, acc.is_capital_pool as isCapitalPool, acc.circulation_mode as circulationMode, ");
		sql.append(" acccur.prent_acc_id as prentAccId, acccur.cur_id as curId ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
		sql.append(" where acc.valid_sign = :validSign and acc.status = :status and acc.is_online = :isOnline ");
		sql.append(" and acccur.prent_acc_id = :prentAccId and acccur.cur_id = :curId ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("prentAccId", prentAccId);
		params.put("curId", curId);
		params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
		params.put("status", BtBankAccConstant.STATUE_CHECK);
		params.put("isOnline", BtBankAccConstant.ISONLINE_1);
		sql.append(" order by acc.corp_id, acc.bank_acc");
		return listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
	}
}
