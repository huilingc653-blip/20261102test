package com.bytter.biz.inneraccount.dao;
import com.bytter.biz.inneraccount.entity.BisAccGatherPolicy;
import com.bytter.biz.inneraccount.vo.BisAccGatherPolicyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银行账号关系策略维护表
 * @date 2017-06-20 16:44:38
 */
@Repository
public class BisAccGatherPolicyDao extends BaseDao<BisAccGatherPolicy, String> {
	/**
	 * 获取调拨策略关系
	 *
	 * @param gatherPolicyId 调拨策略ID
	 * @return
	 */
	public List<BisAccGatherPolicyVO> listByPrentBankAcc(String gatherPolicyId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct g.gather_policy_id as gatherPolicyId,g.parent_acc_id as parentAccId");
		sql.append(" from bis_acc_gather_policy g");
		sql.append(" where g.gather_policy_id = :gatherPolicyId");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("gatherPolicyId", gatherPolicyId);
		return listBySQLAliasToBean(sql.toString(), params, BisAccGatherPolicyVO.class);
	}
	/**
	 * 获取调拨策略关系
	 *
	 * @param gatherPolicyId 调拨策略ID
	 * @param bankAccId      账号
	 * @param prentAccId     父账号
	 * @return
	 */
	public List<BisAccGatherPolicyVO> listByBankAcc(String gatherPolicyId, String bankAccId, String prentAccId, String inSql) {
		StringBuilder sql = new StringBuilder();
		HashMap<String, String> params = new HashMap<String, String>();
		sql.append("select g.id as id,g.bank_acc_id as bankAccId,g.gather_policy_id as gatherPolicyId,g.bif_code as bifCode,g.parent_acc_id as parentAccId,");
		sql.append(" g.first_time as firstTime,g.next_time as nextTime,g.app_next_time as appNextTime,g.bank_sourcecode as bankSourcecode");
		sql.append(" from bis_acc_gather_policy g");
		sql.append(" where g.gather_policy_id = :gatherPolicyId ");
		params.put("gatherPolicyId", gatherPolicyId);
		sql.append(" and ");
		sql.append(inSql);
		if (!StringUtils.isEmpty(bankAccId)) {
			sql.append(" and g.bank_acc_id=:bankAccId");
			params.put("bankAccId", bankAccId);
		}
		if (!StringUtils.isEmpty(prentAccId)) {
			sql.append(" and g.parent_acc_id=:prentAccId");
			params.put("prentAccId", prentAccId);
		}
		return listBySQLAliasToBean(sql.toString(), params, BisAccGatherPolicyVO.class);
	}
	/**
	 * 调拨策略删除
	 *
	 * @param bankAccId
	 * @param prentAccId
	 * @param gatherPolicyId
	 */
	public void deleteBisGatherPolicy(String bankAccId, String prentAccId, String gatherPolicyId) {
		if (!StringUtils.isEmpty(prentAccId)) {
			//删除底层子账号
			createSQLQuery("delete from bis_acc_gather_policy where bank_acc_id=? and parent_acc_id=? and gather_policy_id=?",
					bankAccId, prentAccId, gatherPolicyId).executeUpdate();
		} else {
			//删除父账号对应的所有子账号
			createSQLQuery("delete from bis_acc_gather_policy where parent_acc_id=? and gather_policy_id=?",
					bankAccId, gatherPolicyId).executeUpdate();
		}
	}
// 已迁移到基础服务接口
//	/**
//	 * 获取账号
//	 * @param dataTablesInput
//	 * @return
//	 */
//	@SuppressWarnings("rawtypes")
//	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select distinct acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,acccur.cur_id as curId, ");
//		sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, ");
//		sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
//		sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,bif.name as bifName ");
//		sql.append(" from bt_bank_acc acc ");
//		sql.append(" left join bis_acc_attribute att on acc.attribute_id = att.id ");
//		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
//		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
//		sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
//		sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
//		sql.append(" where acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.valid_sign = " + BtBankAccConstant.VALIDSIGN_1 + " ");
//		//调拨策略
//		String gatherPolicyId = (String) dataTablesInput.getFormdata().get("gatherPolicyId");
//		//父账号
//		String prentAccId = (String) dataTablesInput.getFormdata().get("eqs_prentAccId");
//		//查父账号列表
//		if (StringUtils.isEmpty(prentAccId)) {
//		    //父账号必须是在线账号
//            sql.append(" and acc.is_online = " + BtBankAccConstant.ISONLINE_1);
//			sql.append(" and acc.id not in(select parent_acc_id from bis_acc_gather_policy where gather_policy_id='" + gatherPolicyId + "' and parent_acc_id is not null)");
//			//过滤出必须是父账号
//			sql.append(" and acc.parent_sign='1'");
//		} else {
//			sql.append(" and acccur.prent_acc_id = '" + prentAccId + "'");
//			sql.append(" and acc.id not in(select bank_acc_id from bis_acc_gather_policy where gather_policy_id='" + gatherPolicyId + "' and parent_acc_id='" + prentAccId + "')");
//            //调拨类型：0-定时归集，1-实时归集，2-定时下拨
//            //定时下拨子账号允许为离线账号
//            String gatherType = (String) dataTablesInput.getFormdata().get("gatherType");
//            if(!AllocationConstant.GATHERTYPE_2.equals(gatherType)){
//                sql.append(" and acc.is_online = " + BtBankAccConstant.ISONLINE_1);
//            }
//		}
//		//银行账号
//		String bankAcc = (String) dataTablesInput.getFormdata().get("likes_bankAcc");
//		if (!StringUtils.isEmpty(bankAcc)) {
//			sql.append(" and acc.bank_acc like '%" + bankAcc + "%'");
//		}
//		//银行类别
//		String bankTypeId = (String) dataTablesInput.getFormdata().get("eqs_bankTypeId");
//		if (!StringUtils.isEmpty(bankTypeId)) {
//			sql.append(" and acc.bank_type_id = '" + bankTypeId + "'");
//		}
//		//账户属性
//		String attributeId = (String) dataTablesInput.getFormdata().get("eqs_attributeId");
//		if (!StringUtils.isEmpty(attributeId)) {
//			sql.append(" and acc.attribute_id = '" + attributeId + "'");
//		}
//		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccDTO.class, "acc", false);
//	}
	/**
	 * 调拨策略删除
	 *
	 * @param bankAccId
	 * @param prentAccId
	 */
	public void deleteBisGatherPolicyByCurId(String bankAccId, String prentAccId, String curId) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from bis_acc_gather_policy where bank_acc_id=? and parent_acc_id=? ");
		sql.append(" and gather_policy_id in(select id from bis_gather_policy  where cur_id =? )");
		//删除底层子账号
		createSQLQuery(sql.toString(),
				bankAccId, prentAccId, curId).executeUpdate();
	}
	/**
	 * 根据策略ID更新下一次执行日期
	 * @param policyId
	 */
	public void updateNextTimeByPolicyId(String policyId) {
		StringBuilder hql = new StringBuilder();
		hql.append("update BisAccGatherPolicy set nextTime=:nextTime where gatherPolicyId=:policyId ");
		Map<String,Object> params = new HashMap<>();
		params.put("nextTime", null);
		params.put("policyId", policyId);
		this.batchExecute(hql.toString(), params);
	}
	public List<String> getParentAccIdsByGatherPolicyId(String gatherPolicyId) {
		StringBuffer sql = new StringBuffer("select parent_acc_id from bis_acc_gather_policy where gather_policy_id = :gatherPolicyId and parent_acc_id is not null");
		Map<String, Object> map = new HashMap<>();
		map.put("gatherPolicyId", gatherPolicyId);
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), map);
		return sqlQuery.list();
	}
	public List<String> getBankAccIdsByGatherPolicyIdAndParentAccId(String gatherPolicyId, String parentAccId) {
		StringBuffer sql = new StringBuffer("select bank_acc_id from bis_acc_gather_policy where gather_policy_id = :gatherPolicyId and parent_acc_id = :parentAccId");
		Map<String, Object> map = new HashMap<>();
		map.put("gatherPolicyId", gatherPolicyId);
		map.put("parentAccId", parentAccId);
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), map);
		return sqlQuery.list();
	}
}
