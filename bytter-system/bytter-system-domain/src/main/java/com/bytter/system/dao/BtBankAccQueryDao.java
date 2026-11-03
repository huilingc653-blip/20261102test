package com.bytter.system.dao;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.dto.BisAccBalDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.vo.BtBankAccGroupVO;
import com.bytter.system.vo.BtBankAccQueryVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.constants.BtBankAccConstant;
import org.redisson.misc.Hash;
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
public class BtBankAccQueryDao extends BaseDao<BtBankAcc, String> {
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BtBankAccVO> getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql,String unitCm) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,c.name as corpName, ");
		sql.append(" su.username as createBy,acc.REG_DATE as regDate,acc.VALID_SIGN as validSign, ");
		sql.append(" acc.acc_type as accType,att.id as attributeId,att.attribute_name as attributeName, acc.IS_ONLINE as isOnline,acc.RATES as rates, ");
		sql.append(" nat.id as natureId,nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
		sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,acc.circulation_mode as circulationMode,acc.is_capital_pool as isCapitalPool ");
		sql.append(" ,acc.check_expire_date as checkExpireDate,acc.is_domestic_bank as isDomesticBank,acc.status as status,acc.rmk as rmk ");
		sql.append(" ,acc.close_Date as closeDate, acc.update_by as updateBy, acc.update_date as updateDate,(case when c.unit_Cm=:unitCm then cp.name else null end) as parentArea,c.operationArea_ID as operationAreaID, ");
//		//补充账户组
//		sql.append(" acc.fc_acc_group_code as fcAccGroupCode, ");
		//新增附件id
		sql.append(" acc.attachment_Id as attachmentId,bindingBankAcc.bank_acc as bindingBankAccStr,bindingCorp.name as bindingBankAccCorpName, ");
		sql.append(" bindingBankAcc.acc_name as bindingBankAccAccName,bindingBankAcc.bank_name as bindingBankAccBankName,sem.equity_name as shareholding  ");
		sql.append(" from bt_bank_acc acc ");
	//	String curId = String.valueOf(dataTablesInput.getFormdata().get("eqs_curId"));
	//	if((!EmptyUtils.isBlank(curId) && !"null".equalsIgnoreCase(curId))){
			sql.append(" left join bt_bank_acc_cur acccur on acccur.BANK_ACC_ID=acc.id");
	//	}
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" left join sys_user su on su.login_name = acc.create_by ");
		sql.append(" left join sys_corp c on acc.corp_id = c.id");
		sql.append(" left join sys_equity_maintenance sem on sem.Equity_code = c.equityMaintenance_Id and sem.status = '1' ");
		sql.append(" left join sys_corp cp on cp.id = c.parent_id ");
		sql.append(" left join nis_binding_bankacc nbb on nbb.bank_id = acc.id");
		sql.append(" left join bt_bank_acc bindingBankAcc on bindingBankAcc.id = nbb.binding_bank_id ");
		sql.append(" left join sys_corp bindingCorp on bindingBankAcc.corp_id = bindingCorp.id");
//		sql.append(" left join (select b1.bank_acc as bankAcc,b1.bal_date as balDate,b1.bal as bal from bis_acc_bal b1 ");
//		sql.append(" join ( select bank_acc as bankAcc , MAX(bal_date)AS max_date from bis_acc_bal group by bank_acc ) b2 ");
//		sql.append(" on b1.bank_acc = b2.bankAcc and b1.BAL_DATE = b2.max_date ) bab");
//		sql.append(" on bab.bankAcc = acc.bank_acc ");
		sql.append(" where 1=1  ");
		if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("accStatus"))) {
			String accStatus = dataTablesInput.getFormdata().get("accStatus").toString();
			if(accStatus.equals(BtBankAccConstant.ACCSTATUS_1)){
				sql.append(" and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
			}else if(accStatus.equals(BtBankAccConstant.ACCSTATUS_2)){
				sql.append(" and acc.status = " + BtBankAccConstant.STATUE_DEL + " ");
			}
		}

		String likes_bindingBankAcc = (String)dataTablesInput.getFormdata().get("likes_bindingBankAcc");
		if(EmptyUtils.isNotEmpty(likes_bindingBankAcc)){
			sql.append(" and bindingBankAcc.bank_acc like '%"+likes_bindingBankAcc+"%' ");
		}
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}

		String AttachmentSign =(String)dataTablesInput.getFormdata().get("attachmentSign");
		dataTablesInput.getFormdata().remove("attachmentSign");
		if(StringUtils.isNotBlank(AttachmentSign)){
			if(BtBankAccConstant.ATTACHMENT_SIGN_0.equals(AttachmentSign)){
				sql.append(" and (acc.attachment_id is null or acc.attachment_id ='')");
			}else{
				sql.append(" and acc.attachment_id is not null and acc.attachment_id !=''");
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("unitCm",unitCm);
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false,map);
	}


	public DataTablesOutput<BtBankAccVO> getDataTablesUnionWithAllCurId(DataTablesInput dataTablesInput, String joinSql,String unitCm) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,c.name as corpName, ");
		sql.append(" su.username as createBy,acc.REG_DATE as regDate,acc.VALID_SIGN as validSign, ");
		sql.append(" acc.acc_type as accType,att.id as attributeId,att.attribute_name as attributeName, acc.IS_ONLINE as isOnline,acc.RATES as rates, ");
		sql.append(" nat.id as natureId,nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
		sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,acc.circulation_mode as circulationMode,acc.is_capital_pool as isCapitalPool ");
		sql.append(" ,acc.check_expire_date as checkExpireDate,acc.is_domestic_bank as isDomesticBank,acc.status as status,acc.rmk as rmk ");
		sql.append(" ,acc.close_Date as closeDate, acc.update_by as updateBy, acc.update_date as updateDate,(case when c.unit_Cm=:unitCm then cp.name else null end) as parentArea,c.operationArea_ID as operationAreaID, ");
//		//补充账户组
//		sql.append(" acc.fc_acc_group_code as fcAccGroupCode, ");
		//新增附件id
		sql.append(" acc.attachment_Id as attachmentId,bindingBankAcc.bank_acc as bindingBankAccStr,bindingCorp.name as bindingBankAccCorpName, ");
		sql.append(" bindingBankAcc.acc_name as bindingBankAccAccName,bindingBankAcc.bank_name as bindingBankAccBankName,sem.equity_name as shareholding  ");
		sql.append(" from bt_bank_acc acc ");
		String curId = String.valueOf(dataTablesInput.getFormdata().get("eqs_curId"));
			if((!EmptyUtils.isBlank(curId) && !"null".equalsIgnoreCase(curId))){
				sql.append(" left join bt_bank_acc_cur acccur on acccur.BANK_ACC_ID=acc.id");
			}
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" left join sys_user su on su.login_name = acc.create_by ");
		sql.append(" left join sys_corp c on acc.corp_id = c.id");
		sql.append(" left join sys_equity_maintenance sem on sem.Equity_code = c.equityMaintenance_Id and sem.status = '1' ");
		sql.append(" left join sys_corp cp on cp.id = c.parent_id ");
		sql.append(" left join nis_binding_bankacc nbb on nbb.bank_id = acc.id");
		sql.append(" left join bt_bank_acc bindingBankAcc on bindingBankAcc.id = nbb.binding_bank_id ");
		sql.append(" left join sys_corp bindingCorp on bindingBankAcc.corp_id = bindingCorp.id");
//		sql.append(" left join (select b1.bank_acc as bankAcc,b1.bal_date as balDate,b1.bal as bal from bis_acc_bal b1 ");
//		sql.append(" join ( select bank_acc as bankAcc , MAX(bal_date)AS max_date from bis_acc_bal group by bank_acc ) b2 ");
//		sql.append(" on b1.bank_acc = b2.bankAcc and b1.BAL_DATE = b2.max_date ) bab");
//		sql.append(" on bab.bankAcc = acc.bank_acc ");
		sql.append(" where 1=1  ");
		if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("accStatus"))) {
			String accStatus = dataTablesInput.getFormdata().get("accStatus").toString();
			if(accStatus.equals(BtBankAccConstant.ACCSTATUS_1)){
				sql.append(" and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
			}else if(accStatus.equals(BtBankAccConstant.ACCSTATUS_2)){
				sql.append(" and acc.status = " + BtBankAccConstant.STATUE_DEL + " ");
			}
		}

		String likes_bindingBankAcc = (String)dataTablesInput.getFormdata().get("likes_bindingBankAcc");
		if(EmptyUtils.isNotEmpty(likes_bindingBankAcc)){
			sql.append(" and bindingBankAcc.bank_acc like '%"+likes_bindingBankAcc+"%' ");
		}
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}

		String AttachmentSign =(String)dataTablesInput.getFormdata().get("attachmentSign");
		dataTablesInput.getFormdata().remove("attachmentSign");
		if(StringUtils.isNotBlank(AttachmentSign)){
			if(BtBankAccConstant.ATTACHMENT_SIGN_0.equals(AttachmentSign)){
				sql.append(" and (acc.attachment_id is null or acc.attachment_id ='')");
			}else{
				sql.append(" and acc.attachment_id is not null and acc.attachment_id !=''");
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("unitCm",unitCm);
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false,map);
	}
	/**
	 *
	 * @param bankAcc
	 * @return
	 */
	public List<BisAccBalDTO> getByBankAcc (List<String> bankAccList){
		if (ObjectUtil.isEmpty(bankAccList)){
			return ListUtil.empty();
		}
		BisAccBalDTO bisAccBalDTO = new BisAccBalDTO();
		String sql = "select b1.bank_acc as bankAcc,\n" +
				"       b1.CUR_ID   as curId,\n" +
				"       b1.bal_date as balDate,\n" +
				"       b1.bal      as bal\n" +
				"from bis_acc_bal b1\n" +
				"         join (select bank_acc      as bankAcc,\n" +
				"                      MAX(bal_date) as max_date\n" +
				"               from bis_acc_bal\n" +
				"               group by bank_acc) b2 on\n" +
				"            b1.bank_acc = b2.bankAcc\n" +
				"        and b1.BAL_DATE = b2.max_date" +
				" where " + SqlUtil.getInSql(bankAccList,"b1.bank_acc",true) ;
		Map<String, Object> param = new HashMap<>();
		// param.put("bankAcc",bankAcc);
		// param.put("curId",curId);
		// List<BisAccBalDTO> list = this.listBySQLAliasToBean(sql,param,BisAccBalDTO.class);
		// if(EmptyUtils.isNotEmpty(list) && list.size() >1 ){
		// 	bisAccBalDTO = list.get(0);
		// }
		return this.listBySQLAliasToBean(sql,param,BisAccBalDTO.class);
	}
	/**
	 * 根据条件查询没有删除且是有效的银行账户
	 *
	 * @param dataTablesInput
	 * @param joinSql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getNotDeleteAndValid(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId, acccur.cur_id,");
		sql.append(" acc.REG_DATE as regDate,acc.VALID_SIGN as validSign, ");
		sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, acc.IS_ONLINE as isOnline,acc.RATES as rates, ");
		sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName,acc.bank_code as bankCode, ");
		sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill ");
		sql.append(" ,acc.check_expire_date as checkExpireDate,acc.is_domestic_bank as isDomesticBank,sy.name as corpName,bc.cur_name as curName ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" left join bt_bank_acc_cur acccur on acccur.BANK_ACC_ID=acc.id");
		sql.append(" left join sys_corp sy on sy.id=acc.corp_id");
		sql.append(" left join bt_currency bc on bc.id=acccur.CUR_ID");
		sql.append(" where 1=1 and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
		sql.append(" and acc.valid_sign=" + BtBankAccConstant.VALIDSIGN_1 + " ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false);
	}
	/**
	 * 根据账号id查币别
	 *
	 * @param joinSql
	 * @return
	 */
	public List<BtBankAccVO> queryMultiCurByAccId(String joinSql, Map<String, Object> params) {
		StringBuilder sql = new StringBuilder(" select bank_acc_id as id ,cur_id as curId ,c.cur_name as curName ");
		sql.append(" from bt_bank_acc_cur acccur left join bt_currency c on c.id = acccur.cur_id where 1=1 ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(" and ");
			sql.append(joinSql);
		}
		List<BtBankAccVO> list = listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
		return list;
	}
	/**
	 * 根据账号查询账号对应的流水信息（开户、变更、注销、冻结、解冻）
	 *
	 * @param bankAcc
	 * @return
	 */
	public List<BtBankAccQueryVO> listReqByBankAcc(String bankAcc) {
		StringBuilder sql = new StringBuilder("select t.*,cu.username as createName,uu.username as updateName from(");
		sql.append(" select id as id,'1' as opType,bank_acc as bankAcc,status,bus_type as reqType,create_by as createBy,create_date as createDate,update_by as updateBy,update_date as updateDate from bt_bank_acc_app union all ");
		sql.append(" select id as id,'2' as opType,bank_acc as bankAcc,status,uf_flag as reqType,create_by as createBy,create_date as createDate,update_by as updateBy,update_date as updateDate from bt_bank_acc_freeze ");
		sql.append(" ) t left join sys_user cu on t.createBy=cu.login_name left join sys_user uu on t.updateBy=uu.login_name");
		sql.append(" where t.status <> :status");
		HashMap<String, Object> params = new HashMap<>();
		params.put("status", CommonConstant.STATUE_DELETE);
		if (StringUtils.isNotEmpty(bankAcc)) {
			sql.append(" and t.bankAcc=:bankAcc");
			params.put("bankAcc", bankAcc);
		}
		sql.append(" order by t.createDate");
		return listBySQLAliasToBean(sql.toString(), params, BtBankAccQueryVO.class);
	}

	/**
	 * 添加附件
	 * @param id 银行账号id
	 * @param attachmentId 附件id
	 */
	public void addAttachment(String id, String attachmentId){
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("attachmentId", attachmentId);
		sql.append(" update bt_bank_acc set attachment_id =:attachmentId where id =:id");
		createSQLQuery(sql.toString(), params).executeUpdate();
	}

	public List<BtBankAccVO> getAllBankAccToStatistics(List<String> corpIds, String bankTypeId, String bankAcc, String status, String bindingBankAcc){
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,c.name as corpName, ");
		sql.append(" su.username as createBy,acc.REG_DATE as regDate,acc.VALID_SIGN as validSign,acccur.cur_id as curId,bc.cur_name as curName, ");
		sql.append(" acc.acc_type as accType,att.id as attributeId,att.attribute_name as attributeName, acc.IS_ONLINE as isOnline,acc.RATES as rates, ");
		sql.append(" nat.id as natureId,nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
		sql.append(" acc.prov as prov,acc.city as city,r.NAME as cityName,r.PROV_INPUT_NAME as provName,acc.electric_bill as electricBill,acc.circulation_mode as circulationMode,acc.is_capital_pool as isCapitalPool ");
		sql.append(" ,acc.check_expire_date as checkExpireDate,acc.is_domestic_bank as isDomesticBank,acc.status as status,acc.rmk as rmk ");
		sql.append(" ,acc.close_Date as closeDate, acc.update_by as updateBy, acc.update_date as updateDate,c.operationArea_ID as operationAreaID ");
		//新增附件id
		sql.append(" ,acc.attachment_Id as attachmentId,bindingBankAcc.bank_acc as bindingBankAcc,bindingCorp.name as bindingBankAccCorpName,bindingBanktype.type_name as bindingBankTypeName, ");
		sql.append(" bindingBankAcc.acc_name as bindingBankAccAccName,bindingBankAcc.bank_name as bindingBankAccBankName,sem.equity_name as shareholding,acc.FC_ACC_GROUP_CODE as fcAccGroupCode, ");
		sql.append(" bindingBankAcc.nature_id as bindingNatureId,bindingNat.nature_name as bindingNatureName, ");
		sql.append(" bindingBankAcc.BINDING_BY_NAME as bindingByName,bindingBankAcc.BINDING_BY_MOBILE as bindingByMobile,bindingBankAcc.LOGIN_PASSWORD as loginPassword,bindingBankAcc.PAY_PASSWORD as payPassword, ");
		sql.append(" bindingBankAcc.fc_acc_group_code as bindingAccGroupCode,c.parent_ids as corpParentIds,c.province_id sysProvinceId,acc.acc_use_code accUseCode ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join bt_bank_acc_cur acccur on acccur.BANK_ACC_ID=acc.id");
		sql.append(" left join bt_currency bc  on bc.id = acccur.cur_id ");
		sql.append(" left join SYS_REGION r  on acc.city = r.id ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" left join sys_user su on su.login_name = acc.create_by ");
		sql.append(" left join sys_corp c on acc.corp_id = c.id");
		sql.append(" left join sys_equity_maintenance sem on sem.Equity_code = c.equityMaintenance_Id ");
		sql.append(" left join sys_corp cp on cp.id = c.parent_id ");
		sql.append(" left join nis_binding_bankacc nbb on nbb.BINDING_BANK_ID = acc.id");
		sql.append(" left join bt_bank_acc bindingBankAcc on bindingBankAcc.id = nbb.BANK_ID ");
		sql.append(" left join bt_bank_type bindingBanktype on bindingBankAcc.bank_type_id = bindingBanktype.id ");
		sql.append(" left join bt_acc_nature bindingNat on bindingBankAcc.nature_id = bindingNat.id ");
		sql.append(" left join sys_corp bindingCorp on bindingBankAcc.corp_id = bindingCorp.id");
		sql.append(" where 1=1 and banktype.BANK_TYPE != '8500' ");

		sql.append(" and  ").append(getSQLIn(corpIds, 900, "acc.corp_id"));

		Map<String, Object> params = new HashMap<>();

		if(!EmptyUtils.isBlank(bankTypeId)){
			sql.append(" and  ").append(SqlUtil.getInSql(bankTypeId.split(","), "acc.bank_type_id", true));
		}

		if(!EmptyUtils.isBlank(bankAcc)){
			sql.append(" and acc.bank_acc = :bankAcc ");
			params.put("bankAcc", bankAcc);
		}

		if(!EmptyUtils.isBlank(status)){
			if(status.equals(BtBankAccConstant.ACCSTATUS_1)){
				sql.append(" and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
			}else if(status.equals(BtBankAccConstant.ACCSTATUS_2)){
				sql.append(" and acc.status = " + BtBankAccConstant.STATUE_DEL + " ");
			}
		}

		if(!EmptyUtils.isBlank(bindingBankAcc)){
			sql.append(" and bindingBankAcc.bank_acc = :bindingBankAcc ");
			params.put("bindingBankAcc", bindingBankAcc);
		}

		return listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
	}

	/**
	 * 账户组数据
	 *
	 * @return
	 */
	public List<BtBankAccGroupVO> getBankAccGroupData() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select fbag.id as id,fbag.acc_group_code as accGroupCode,fbag.ACC_GROUP_NAME as accGroupName ");
		sql.append(" from FC_BANK_ACC_GROUP fbag");
		return listBySQLAliasToBean(sql.toString(), new HashMap<>(), BtBankAccGroupVO.class);
	}
}
