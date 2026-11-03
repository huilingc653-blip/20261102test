package com.bytter.system.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.entity.BisBifRule;
import com.bytter.system.vo.BisBifRuleVO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 接口代码业务规则表
 * @date 2017-05-17 10:16:20
 */
@Repository
public class BisBifRuleDao extends BaseDao<BisBifRule, String> {
	/**
	 * 根据编码获取接口代码业务规则
	 *
	 * @param bifCode
	 * @return
	 */
	public BisBifRule findByBifCode(String bifCode) {
		return getUniqueBy("bifCode", bifCode);
	}
	/**
	 * 根据ID获取接口代码业务规则
	 *
	 * @param id
	 * @return
	 */
	public BisBifRuleVO getById(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append("select b.id as id,b.bifCode as bifCode,b.bifName as bifName,b.payerName as payerName,");
		hql.append(" b.payerAcc as payerAcc,b.payerBank as payerBank,b.payeeName as payeeName,b.payeeAcc as payeeAcc,b.payeeBank as payeeBank,");
		hql.append(" b.purpose as purpose,b.addWord as addWord,b.isPurpose as isPurpose,b.isBankOnline as isBankOnline,b.sendNum as sendNum,");
		hql.append(" b.isAddBillNo as isAddBillNo,b.isMustBankCode as isMustBankCode");
		hql.append(" from BisBifRule b where  b.id=:bifId");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("bifId", id);
		return this.getByHQLAliasToBean(hql.toString(), params, BisBifRuleVO.class);
	}
	/**
	 * 根据接口代码ID获取接口代码业务规则
	 *
	 * @param bifId
	 * @return
	 */
	public BisBifRuleVO findByBifId(String bifId) {
		StringBuilder hql = new StringBuilder();
		hql.append("select b.id as id,b.bifCode as bifCode,b.bifName as bifName,b.payerName as payerName,");
		hql.append(" b.payerAcc as payerAcc,b.payerBank as payerBank,b.payeeName as payeeName,b.payeeAcc as payeeAcc,b.payeeBank as payeeBank,");
		hql.append(" b.purpose as purpose,b.addWord as addWord,b.isPurpose as isPurpose,b.isBankOnline as isBankOnline,b.sendNum as sendNum,");
		hql.append(" b.isAddBillNo as isAddBillNo,b.isMustBankCode as isMustBankCode");
		hql.append(" from BisBifRule b, BisBifInit i where b.bifCode = i.bifCode and i.id=:bifId");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("bifId", bifId);
		return this.getByHQLAliasToBean(hql.toString(), params, BisBifRuleVO.class);
	}
	/**
	 * 查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.bif_Code as bifCode,b.bif_Name as bifName,i.id as bifId,b.payer_Name as payerName,");
		sql.append(" b.payer_Acc as payerAcc,b.payer_Bank as payerBank,b.payee_Name as payeeName,b.payee_Acc as payeeAcc,b.payee_Bank as payeeBank,");
		sql.append(" b.purpose as purpose,b.add_Word as addWord,b.is_Purpose as isPurpose,b.is_Bank_Online as isBankOnline,b.send_Num as sendNum,");
		sql.append(" b.create_By as createBy,b.create_Date as createDate,b.update_By as updateBy,b.update_Date as updateDate,");
		sql.append(" b.is_Add_BillNo as isAddBillNo,b.is_Must_Bank_Code as isMustBankCode");
		sql.append(" from Bis_Bif_Rule b left join Bis_Bif_Init i on b.bif_code = i.bif_code where 1=1");
		return getDynamicBySQL(dataTablesInput, sql.toString(), BisBifRuleVO.class, "b", false);
	}
	/**
	 * 根据ID获取接口规则
	 *
	 * @param id
	 * @return
	 */
	public BisBifRuleVO getByIdUnion(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.bif_Code as bifCode,b.bif_Name as bifName,i.id as bifId,b.payer_Name as payerName,");
		sql.append(" b.payer_Acc as payerAcc,b.payer_Bank as payerBank,b.payee_Name as payeeName,b.payee_Acc as payeeAcc,b.payee_Bank as payeeBank,");
		sql.append(" b.purpose as purpose,b.add_Word as addWord,b.is_Purpose as isPurpose,b.is_Bank_Online as isBankOnline,b.send_Num as sendNum,");
		sql.append(" b.create_By as createBy,b.create_Date as createDate,b.update_By as updateBy,b.update_Date as updateDate,");
		sql.append(" b.is_Add_BillNo as isAddBillNo,b.is_Must_Bank_Code as isMustBankCode");
		sql.append(" from Bis_Bif_Rule b left join Bis_Bif_Init i on b.bif_code = i.bif_code where b.id = :id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, BisBifRuleVO.class);
	}
	/**
	 * 获取所有接口代码业务规则信息
	 *
	 * @return
	 */
	public List<BisBifRuleVO> listAllUnion() {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.bif_Code as bifCode,b.bif_Name as bifName,i.id as bifId,b.payer_Name as payerName,");
		sql.append(" b.payer_Acc as payerAcc,b.payer_Bank as payerBank,b.payee_Name as payeeName,b.payee_Acc as payeeAcc,b.payee_Bank as payeeBank,");
		sql.append(" b.purpose as purpose,b.add_Word as addWord,b.is_Purpose as isPurpose,b.is_Bank_Online as isBankOnline,b.send_Num as sendNum,");
		sql.append(" b.create_By as createBy,b.create_Date as createDate,b.update_By as updateBy,b.update_Date as updateDate,");
		sql.append(" b.is_Add_BillNo as isAddBillNo,b.is_Must_Bank_Code as isMustBankCode");
		sql.append(" from Bis_Bif_Rule b left join Bis_Bif_Init i on b.bif_code = i.bif_code ");
		return listBySQLAliasToBean(sql.toString(), null, BisBifRuleVO.class);
	}
}
