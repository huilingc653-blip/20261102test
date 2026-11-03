package com.bytter.system.dao;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.entity.*;
import com.bytter.system.api.vo.BtBankAccAppVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Repository
public class BtBankAccCommonDao extends BaseDao<BtBankAcc, String> {
	/**
	 * 获取有效的账户信息
	 *
	 * @return
	 */
	public List<BtBankAcc> listEffectiveBtBankAcc() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAcc where  validSign = :validSign and status = :status ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
		params.put("status", BtBankAccConstant.STATUE_CHECK);
		return this.listByHQLAliasToBean(hql.toString(), params, BtBankAcc.class);
	}
	/**
	 * 获取有效的账户信息
	 *
	 * @return
	 */
	public List<BtBankAccDTO> listEffectiveBtBankAccs() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAcc where  validSign = :validSign and status = :status ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
		params.put("status", BtBankAccConstant.STATUE_CHECK);
		return this.listByHQLAliasToBean(hql.toString(), params, BtBankAccDTO.class);
	}
	/**
	 * 获取银行账户表的币别信息
	 *
	 * @param bankAccId 账户ID
	 * @return
	 */
	public List<BtBankAccCur> listBtBankAccCurById(String bankAccId) {
		StringBuilder hql = new StringBuilder();
		Map<String, String> params = new HashMap();
		hql.append(" from BtBankAccCur where bankAccId = :bankAccId ");
		params.put("bankAccId", bankAccId);
		return this.listByHQLAliasToBean(hql.toString(), params, BtBankAccCur.class);
	}
	/**
	 * 获取银行账户申请表的币别信息
	 *
	 * @param accAppId 账户ID
	 * @return
	 */
	public List<BtBankAccAppCur> listBtBankAccAppCurById(String accAppId) {
		StringBuilder hql = new StringBuilder();
		Map<String, String> params = new HashMap();
		hql.append(" from BtBankAccAppCur where appId = :accAppId ");
		params.put("accAppId", accAppId);
		return this.listByHQLAliasToBean(hql.toString(), params, BtBankAccAppCur.class);
	}
	/**
	 * 根据id得到币别
	 *
	 * @param id
	 * @return
	 * @author zuoy 2018-02-28
	 */
	public List<BtCurrencyDTO> listCurAccById(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cur.cur_name as curname,cur.id as id ,cur.cur_code as curcode FROM bt_bank_acc_cur acccur, bt_currency cur ");
		sql.append(" WHERE acccur.cur_id = cur.id AND acccur.bank_acc_id = :id ORDER BY cur.cur_code ");
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id);
		return listBySQLAliasToBean(sql.toString(), param, BtCurrencyDTO.class);
	}
	/**
	 * 根据id得到币别
	 *
	 * @param id
	 * @return
	 * @author zuoy 2018-02-28
	 */
	public List<BtCurrencyDTO> listCurAccAppById(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cur.cur_name as curName,cur.id as id ,cur.cur_code as curCode FROM bt_bank_acc_app_cur appcur, bt_currency cur ");
		sql.append(" WHERE appcur.cur_id = cur.id AND appcur.app_id = :id ORDER BY cur.cur_code ");
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id);
		return listBySQLAliasToBean(sql.toString(), param, BtCurrencyDTO.class);
	}
	/**
	 * 根据id得到entity
	 *
	 * @param id
	 * @return
	 */
	public BtBankAcc getBtBankAcc(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAcc where id = :id ");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return this.getUnique(hql.toString(), params);
	}
	/**
	 * 根据id得到entity
	 *
	 * @param id
	 * @return
	 */
	public BtBankAccApp getBtBankAccApp(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAccApp where id = :id ");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return this.getUnique(hql.toString(), params);
	}
	/**
	 * 根据账号得到银行账号(开户申请的账号，不包含变更后的)
	 *
	 * @param bankAcc
	 * @return
	 */
	public BtBankAccApp getBtBankAccAppByBankAcc(String bankAcc) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAccApp where bankAcc = :bankAcc and busType = :busType ");
		Map<String, String> params = new HashMap<String, String>();
		params.put("bankAcc", bankAcc);
		params.put("busType", BtBankAccConstant.BUSTYPE_1);
		return this.getUnique(hql.toString(), params);
	}
	/**
	 * 根据账号得到银行账号信息
	 *
	 * @param bankAcc
	 * @return
	 */
	public BtBankAcc getBtBankAccByBankAcc(String bankAcc) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAcc where bankAcc = :bankAcc ");
		Map<String, String> params = new HashMap<String, String>();
		params.put("bankAcc", bankAcc);
		return this.getUnique(hql.toString(), params);
	}
	/**
	 * 根据id查找银行信息
	 * zuoy 2017-06-22
	 *
	 * @param id
	 * @return BtBankAccVO
	 */
	public BtBankAccVO getBtBankAccVOById(String id, String curId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName, acc.parent_sign as parentSign,acc.corp_id as corpId,sc.name AS corpName, acc.bank_org_code as bankOrgCode,");
		sql.append(" acc.acc_type as accType,att.id as attributeId, att.attribute_name as attributeName, nat.id as natureId, nat.nature_name as natureName, ");
		sql.append(" banktype.id as bankTypeId, banktype.type_name as bankTypeName, acc.bank_code as bankCode, acc.bank_name as bankName, ");
		sql.append(" acc.prov as prov,acc.city as city,acc.bif_code as bifCode, bif.name as bifName, acc.electric_bill as electricBill, ");
		sql.append(" acc.regulars, acc.regular_date as regularDate, acc.notice_type as noticeType, acc.close_Date as closeDate, ");
		sql.append(" acc.agr_money as agrMoney, acc.agr_interest as agrInterest, acc.agr_contract as agrContract, acc.agr_begin_date as agrBeginDate, acc.agr_end_date as agrEndDate, ");
		sql.append(" acc.rates as rates, acc.rates_float as ratesFloat, acc.rates_float_per as ratesFloatPer, ");
		sql.append(" acc.volume_num as volumeNum, acc.bankbook_num as bankbookNum, acc.interest_cycle as interestCycle, acc.interest_date as interestDate, ");
		sql.append(" acc.reg_date as regDate, acc.private_no as privateNo, acc.is_online as isOnline,acc.is_offshore_account as isOffshoreAccount, bt_nvl_char(acc.scan_flag,'0') as scanFlag,  ");
		sql.append(" acc.init_balance_date as initBalanceDate, acc.init_balance as initBalance, acc.rmk as rmk, acc.valid_sign as validSign, ");
		sql.append(" acc.create_by as createBy, acc.create_date as createDate, acc.update_by as updateBy, acc.update_date as updateDate, ");
		sql.append(" acc.status as status, acc.entity_acc_id as entityAccId, acc.is_capital_pool as isCapitalPool, acc.circulation_mode as circulationMode ");
		sql.append(" ,acc.check_expire_date as checkExpireDate,acc.is_domestic_bank as isDomesticBank,acc.seal_directory,acc.seal_person,acc.seal_phone,acc.seal_email,acc.mgt_department");
		sql.append(" ,acc.u_key_admin,acc.u_key_operator,acc.u_key_reviewers1,acc.u_key_reviewers2,acc.u_key_reviewers3,acc.u_key_function,acc.is_online_handle,acc.receipt_code as receiptCode, acc.FC_ACC_GROUP_CODE as fcAccGroupCode ");
		sql.append(" ,acc.attachment_id as attachmentId,acc.IS_DEMAND as isDemand,acc.binding_by_name bindingByName,acc.binding_by_mobile bindingByMobile ");
		sql.append(" ,acc.login_password loginPassword,acc.pay_password payPassword ");
		if ("null".equals(curId) || EmptyUtils.isBlank(curId)) {
			sql.append(" from bt_bank_acc acc ");
		} else {
			sql.append(" ,cur.cur_id curId, cur.counter_acc as counterAccId, cur.prent_acc_id as prentAccId ");
			sql.append(" from bt_bank_acc acc left join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id ");
		}
		sql.append(" left join sys_corp sc ON sc.id=acc.corp_id ");

		sql.append(" left join bis_acc_attribute att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
		sql.append(" where acc.id = :id ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		if(!"null".equals(curId) && !EmptyUtils.isBlank(curId)){
			sql.append(" and cur.cur_id = :curId ");
			params.put("curId", curId);
		}
		return getBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
	}
	/**
	 * 显示详情
	 *
	 * @param id
	 * @return
	 */
	public BtBankAccAppVO getBtBankAccAppVOById(String id, String curId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select app.id as id,app.acc_no as accNo,app.bank_acc as bankAcc,app.acc_name as accName,app.corp_id as corpId, ");
		sql.append(" app.acc_type as accType,att.id as attributeId,att.attribute_name as attributeName,app.bus_type busType, ");
		sql.append(" nat.id as natureId,nat.nature_name as natureName,banktype.id as bankTypeId, banktype.type_name as bankTypeName,app.bank_name as bankName, ");
		sql.append(" app.prov as prov,app.city as city,app.electric_bill as electricBill,app.op_ression as opRession,app.attachment_id as attachmentId, ");
		sql.append(" app.regulars as regulars,app.agr_money as agrMoney,app.agr_contract as agrContract,app.agr_interest as agrInterest,app.bank_code as bankCode,app.is_offshore_account as isOffshoreAccount, ");
		sql.append(" app.agr_begin_date as agrBeginDate,app.agr_end_date as agrEndDate,app.notice_type as noticeType,app.entity_acc_id as entityAccId,app.circulation_mode as circulationMode,app.is_domestic_bank as isDomesticBank");
		if ("null".equals(curId) || EmptyUtils.isBlank(curId)) {
			sql.append(" from bt_bank_acc_app app ");
		} else {
			sql.append(" ,cur.cur_id curId ");
			sql.append(" from bt_bank_acc_app app left join bt_bank_acc_app_cur cur on app.id = cur.app_id ");
		}
		sql.append(" left join bis_acc_attribute att on app.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on app.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on app.bank_type_id = banktype.id ");
		sql.append(" where app.id = :id ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		if(!"null".equals(curId) && !EmptyUtils.isBlank(curId)){
			sql.append(" and cur.cur_id = :curId ");
			params.put("curId", curId);
		}
		return this.getBySQLAliasToBean(sql.toString(), params, BtBankAccAppVO.class);
	}
	/**
	 * 获取有效的父帐号银行信息
	 * zuoy 2017-06-22
	 *
	 * @return
	 */
	public List<BtBankAccVO> listPrentBankAcc() {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append(" from BtBankAcc where ");
		sql.append(" where parentSign = '1' and validSign = :validSign and status = :status ");
		params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
		params.put("status", BtBankAccConstant.STATUE_CHECK);
		return listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
	}
	/**
	 * 获取银行账号申请表币别中间表
	 *
	 * @param accAppId
	 * @param curId
	 * @return
	 */
	public BtBankAccAppCur getBtBankAccAppCur(String accAppId, String curId) {
		StringBuilder hql = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>();
		hql.append(" from BtBankAccAppCur cur where cur.appId = :accAppId and cur.curId = :curId ");
		params.put("accAppId", accAppId);
		params.put("curId", curId);
		return (BtBankAccAppCur) this.createQuery(hql.toString()).setProperties(params).uniqueResult();
	}
	/**
	 * 获取银行账号表币别中间表
	 *
	 * @param bankAccId
	 * @param curId
	 * @return
	 */
	public BtBankAccCur getBtBankAccCur(String bankAccId, String curId) {
		StringBuilder hql = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>();
		hql.append(" from BtBankAccCur cur where cur.bankAccId = :bankAccId and cur.curId = :curId ");
		params.put("bankAccId", bankAccId);
		params.put("curId", curId);
		return (BtBankAccCur) this.createQuery(hql.toString()).setProperties(params).uniqueResult();
	}
	/**
	 * 是否存在子账户
	 *
	 * @param prentAccId
	 * @return
	 */
	public boolean isChildBankAccByPrentAccId(String prentAccId) {
		StringBuilder hql = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>();
		hql.append(" from BtBankAccCur cur where cur.prentAccId = :prentAccId ");
		params.put("prentAccId", prentAccId);
		List list = this.listByHQL(hql.toString(), params);
		return EmptyUtils.isEmptyList(list);
	}
	/**
	 * 获取账户性质
	 *
	 * @param natureName
	 * @return
	 */
	public String getNatureId(String natureName) {
		StringBuilder sql = new StringBuilder(" select id from bt_acc_nature where valid_sign = 1 and nature_name = :natureName");
		Map<String, Object> params = new HashMap();
		params.put("natureName", natureName);
		List<BtAccNature> btAccNature = this.listBySQLAliasToBean(sql.toString(),0,1,params,BtAccNature.class);
		if(EmptyUtils.isEmptyList(btAccNature)){
			return null;
		}
		return btAccNature.get(0).getId();
	}
	/**
	 * 获取账户性质
	 *
	 * @param id
	 * @return
	 */
	public String getNatureNameById(String id) {
		StringBuilder sql = new StringBuilder(" select nature_name from bt_acc_nature where valid_sign = 1 and id = :id");
		Map<String, Object> params = new HashMap();
		params.put("id", id);
		List<BtAccNature> btAccNature = this.listBySQLAliasToBean(sql.toString(),0,1,params,BtAccNature.class);
		if(EmptyUtils.isEmptyList(btAccNature)){
			return null;
		}
		return btAccNature.get(0).getNatureName();
	}
	/**
	 * 获取账户属性
	 *
	 * @param attributeName
	 * @return
	 */
	public String getAttributeId(String attributeName) {
		StringBuilder sql = new StringBuilder(" select id from bis_acc_attribute where valid_sign = 1 and attribute_name = :attributeName ");
		Map<String, Object> params = new HashMap();
		params.put("attributeName", attributeName);
		List<BisAccAttribute> bisAccAttribute = this.listBySQLAliasToBean(sql.toString(),0,1,params,BisAccAttribute.class);
		if(EmptyUtils.isEmptyList(bisAccAttribute)){
			return null;
		}
		return bisAccAttribute.get(0).getId();
	}
	/**
	 * 获取账户属性
	 *
	 * @param id
	 * @return
	 */
	public String getAttributeNameById(String id) {
		StringBuilder sql = new StringBuilder(" select attribute_name from bis_acc_attribute where valid_sign = 1 and id = :id ");
		Map<String, Object> params = new HashMap();
		params.put("id", id);
		List<BisAccAttribute> bisAccAttribute = this.listBySQLAliasToBean(sql.toString(),0,1,params,BisAccAttribute.class);
		if(EmptyUtils.isEmptyList(bisAccAttribute)){
			return null;
		}
		return bisAccAttribute.get(0).getAttributeName();
	}
	/**
	 * 获取银行账号
	 *
	 * @return
	 */
	public List<String> getByBankAccs(Set<String> bankAccs) {
		StringBuffer sql = new StringBuffer("select bank_acc from BT_BANK_ACC where bank_acc in :bankAccs");
		Map<String,Object> params=new HashMap<>();
		params.put("bankAccs",bankAccs);
		SQLQuery sqlQuery = createSQLQuery(sql.toString(),params);
		return sqlQuery.list();
	}
	/**
	 * 获取所有的AccNature
	 *
	 * @return
	 */
	public List<BtAccNature> getAllBtAccNature() {
		String sql = " select id,nature_name from bt_acc_nature where valid_sign = 1 ";
		List<Map> resultMapList=listBySQL(sql,null);
		if (EmptyUtils.isEmpty(resultMapList)) {
			return null;
		}
		return BeanUtils.copyMapListProperties(resultMapList, BtAccNature.class);
	}
	/**
	 * 获取所有账户属性
	 *
	 * @return
	 */
	public List<BisAccAttribute> getAllBisAccAttributes() {
		String sql = " select id,attribute_name from bis_acc_attribute where valid_sign = 1 ";
		List<Map> resultMapList=listBySQL(sql,null);
		if (EmptyUtils.isEmpty(resultMapList)) {
			return null;
		}
		return BeanUtils.copyMapListProperties(resultMapList, BisAccAttribute.class);
	}
	public Long countEffectiveBtBankAcc() {
		String sql="select count(1) from BtBankAcc where  validSign = :validSign and status = :status ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
		params.put("status", BtBankAccConstant.STATUE_CHECK);
		Long count = (Long)createQuery(sql,params).uniqueResult();
		return count;
	}

    public List<BtBankAccVO> getBtBankAccVOByReceiptType(String receiptCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName, acc.parent_sign as parentSign,acc.corp_id as corpId,sc.name AS corpName, acc.bank_org_code as bankOrgCode,");
		sql.append(" acc.acc_type as accType,att.id as attributeId, att.attribute_name as attributeName, nat.id as natureId, nat.nature_name as natureName, ");
		sql.append(" banktype.id as bankTypeId, banktype.type_name as bankTypeName, acc.bank_code as bankCode, acc.bank_name as bankName, ");
		sql.append(" acc.prov as prov,acc.city as city,acc.bif_code as bifCode, bif.name as bifName, acc.electric_bill as electricBill, ");
		sql.append(" acc.regulars, acc.regular_date as regularDate, acc.notice_type as noticeType, acc.close_Date as closeDate, ");
		sql.append(" acc.agr_money as agrMoney, acc.agr_interest as agrInterest, acc.agr_contract as agrContract, acc.agr_begin_date as agrBeginDate, acc.agr_end_date as agrEndDate, ");
		sql.append(" acc.rates as rates, acc.rates_float as ratesFloat, acc.rates_float_per as ratesFloatPer, ");
		sql.append(" acc.volume_num as volumeNum, acc.bankbook_num as bankbookNum, acc.interest_cycle as interestCycle, acc.interest_date as interestDate, ");
		sql.append(" acc.reg_date as regDate, acc.private_no as privateNo, acc.is_online as isOnline,acc.is_offshore_account as isOffshoreAccount, bt_nvl_char(acc.scan_flag,'0') as scanFlag,  ");
		sql.append(" acc.init_balance_date as initBalanceDate, acc.init_balance as initBalance, acc.rmk as rmk, acc.valid_sign as validSign, ");
		sql.append(" acc.create_by as createBy, acc.create_date as createDate, acc.update_by as updateBy, acc.update_date as updateDate, ");
		sql.append(" acc.status as status, acc.entity_acc_id as entityAccId, acc.is_capital_pool as isCapitalPool, acc.circulation_mode as circulationMode ");
		sql.append(" ,acc.check_expire_date as checkExpireDate,acc.is_domestic_bank as isDomesticBank,acc.seal_directory,acc.seal_person,acc.seal_phone,acc.seal_email,acc.mgt_department");
		sql.append(" ,acc.u_key_admin,acc.u_key_operator,acc.u_key_reviewers1,acc.u_key_reviewers2,acc.u_key_reviewers3,acc.u_key_function,acc.is_online_handle,acc.receipt_code as receiptCode, acc.FC_ACC_GROUP_CODE as fcAccGroupCode");
		sql.append(" ,acc.attachment_id as attachmentId ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join sys_corp sc ON sc.id=acc.corp_id ");
		sql.append(" left join bis_acc_attribute att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
		sql.append(" where acc.receipt_code like '%"+receiptCode+"%'");
		HashMap<String, Object> params = new HashMap<String, Object>();
		return listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }

	public List<BtBankAccVO> getBtBankAccVOByAccGroupCode(String accGroupCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName, acc.parent_sign as parentSign,acc.corp_id as corpId,sc.name AS corpName, acc.bank_org_code as bankOrgCode,");
		sql.append(" acc.acc_type as accType,att.id as attributeId, att.attribute_name as attributeName, nat.id as natureId, nat.nature_name as natureName, ");
		sql.append(" banktype.id as bankTypeId, banktype.type_name as bankTypeName, acc.bank_code as bankCode, acc.bank_name as bankName, ");
		sql.append(" acc.prov as prov,acc.city as city,acc.bif_code as bifCode, bif.name as bifName, acc.electric_bill as electricBill, ");
		sql.append(" acc.regulars, acc.regular_date as regularDate, acc.notice_type as noticeType, acc.close_Date as closeDate, ");
		sql.append(" acc.agr_money as agrMoney, acc.agr_interest as agrInterest, acc.agr_contract as agrContract, acc.agr_begin_date as agrBeginDate, acc.agr_end_date as agrEndDate, ");
		sql.append(" acc.rates as rates, acc.rates_float as ratesFloat, acc.rates_float_per as ratesFloatPer, ");
		sql.append(" acc.volume_num as volumeNum, acc.bankbook_num as bankbookNum, acc.interest_cycle as interestCycle, acc.interest_date as interestDate, ");
		sql.append(" acc.reg_date as regDate, acc.private_no as privateNo, acc.is_online as isOnline,acc.is_offshore_account as isOffshoreAccount, bt_nvl_char(acc.scan_flag,'0') as scanFlag,  ");
		sql.append(" acc.init_balance_date as initBalanceDate, acc.init_balance as initBalance, acc.rmk as rmk, acc.valid_sign as validSign, ");
		sql.append(" acc.create_by as createBy, acc.create_date as createDate, acc.update_by as updateBy, acc.update_date as updateDate, ");
		sql.append(" acc.status as status, acc.entity_acc_id as entityAccId, acc.is_capital_pool as isCapitalPool, acc.circulation_mode as circulationMode ");
		sql.append(" ,acc.check_expire_date as checkExpireDate,acc.is_domestic_bank as isDomesticBank,acc.seal_directory,acc.seal_person,acc.seal_phone,acc.seal_email,acc.mgt_department");
		sql.append(" ,acc.u_key_admin,acc.u_key_operator,acc.u_key_reviewers1,acc.u_key_reviewers2,acc.u_key_reviewers3,acc.u_key_function,acc.is_online_handle,acc.receipt_id as receiptId, acc.FC_ACC_GROUP_CODE as fcAccGroupCode");
		sql.append(" ,acc.attachment_id as attachmentId ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join sys_corp sc ON sc.id=acc.corp_id ");
		sql.append(" left join bis_acc_attribute att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
		sql.append(" where acc.FC_ACC_GROUP_CODE like '%"+accGroupCode+"%'");
		HashMap<String, Object> params = new HashMap<String, Object>();
		return listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
	}
}