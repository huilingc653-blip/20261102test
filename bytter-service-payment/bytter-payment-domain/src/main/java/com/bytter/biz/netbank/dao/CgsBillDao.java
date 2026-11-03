package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.CgsBill;
import com.bytter.biz.netbank.vo.CgsBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.biz.payment.api.constant.CgsBillConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 委托收款
 * @date 2017-05-26 11:14:59
 */
@Repository
public class CgsBillDao extends BaseDao<CgsBill, String> {
	/**
	 * 查询内部托收单据列表
	 *
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<CgsBillVO> findListBysql(DataTablesInput input, String joinSql) {
		String opType = (String) input.getFormdata().get("opType");
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id as id,c.consign_date as consignDate,c.consign_code as consignCode,c.payer_corp_id as payerCorpId,c.payee_corp_id as payeeCorpId,");
		sql.append("c.payee_acc as payeeAcc,c.money as money,c.pact_code as pactCode,c.abs as abs,c.status as status,c.attachment_id as attachmentId ");
		sql.append("from cgs_bill c  where 1=1");
		if ("list".equals(opType)) {
			sql.append(" and c.status in (" + CgsBillConstant.STATUE_TEMP + "," + CgsBillConstant.STATUE_BACK + ")");
		}
		sql.append(" and ").append(joinSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), CgsBillVO.class, "c", false);
	}
	/**
	 * 根据id查找内部托收单据
	 *
	 * @param id
	 * @return
	 */
	public CgsBillVO getByUnion(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id as id,c.consign_date as consignDate,c.consign_code as consignCode,c.payer_corp_id as payerCorpId,");
		sql.append("c.payee_corp_id as payeeCorpId,c.payee_acc as payeeAcc,c.payer_acc as payerAcc,c.money as money,c.pact_code as pactCode,");
		sql.append("c.invoice_code as invoiceCode,c.charge_money as chargeMoney,c.abs as abs,c.status as status,c.create_by as createBy,c.attachment_id as attachmentId ");
		sql.append("from cgs_bill c where c.id=:id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, CgsBillVO.class);
	}
	@SuppressWarnings("unchecked")
	public DataTablesOutput<CgsBillVO> dataTableApprove(DataTablesInput dataTablesInput, List<String> ids) {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id as id,c.consign_date as consignDate,c.consign_code as consignCode,c.payer_corp_id as payerCorpId,c.payee_corp_id as payeeCorpId,");
		sql.append("c.payee_acc as payeeAcc,c.money as money,c.pact_code as pactCode,c.abs as abs,c.status as status,c.attachment_id as attachmentId ");
		sql.append("from cgs_bill c  where 1=1");
		sql.append(" and ").append(getSQLIn(ids, 900, "c.id"));
		return getDynamicBySQL(dataTablesInput, sql.toString(), CgsBillVO.class, "c", false);
	}
}
