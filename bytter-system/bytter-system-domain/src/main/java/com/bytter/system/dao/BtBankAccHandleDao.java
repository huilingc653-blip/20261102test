package com.bytter.system.dao;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.vo.BtBankAccHandleVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.entity.BtBankAcc;
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
public class BtBankAccHandleDao extends BaseDao<BtBankAcc, String> {
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String appSql,String accSql, String busType) {
        String accName = (String) dataTablesInput.getFormdata().get("likes_accName");
        // 是否开户办理
        String isTransact = "";
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqs_isTransact"))) {
            isTransact = (String) dataTablesInput.getFormdata().get("eqs_isTransact");
        }
        String likesBankName = (String) dataTablesInput.getFormdata().get("likes_bankName");
        String accUseCode = (String) dataTablesInput.getFormdata().get("accUseCode");

        // bt_bank_acc 部分
        StringBuilder accQuery = new StringBuilder();
        accQuery.append("select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,ac.name AS corpName, acc.status as status , ");
        accQuery.append(" acc.acc_type as accType,acc.bank_type_id as bankTypeId, nat.nature_name as natureName,att.attribute_name as attributeName,");
        accQuery.append(" acc.nature_id as natureId,acc.attribute_id as attributeId,acc.bank_name as bankName, acc.prov as prov,acc.city as city,");
        accQuery.append(" acc.electric_bill as electricBill, acc.circulation_mode as circulationMode,acc.is_online as isOnline,acc.valid_sign as validSign,'1' as isFrom ");
        accQuery.append(" ,acc.check_expire_date as checkExpireDate,acc.is_domestic_bank as isDomesticBank,acc.reg_date as regDate,acc.receipt_code as receiptCode, ");
        accQuery.append(" acc.acc_use_code as accUseCode,acc.rates as rates,acc.is_close isClose ");
        accQuery.append(" from bt_bank_acc acc ");
        accQuery.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        accQuery.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        accQuery.append(" LEFT JOIN sys_corp ac ON ac.id=acc.corp_id ");
        accQuery.append(" where 1=1 and acc.status <> ").append(BtBankAccConstant.STATUE_DEL)
                .append(" and acc.status <> ").append(BtBankAccConstant.STATUE_WAIT_CHECK).append(" ");
        accQuery.append(" and acc.bank_acc not in (select bank_acc from bt_bank_acc_app where status <> ")
                .append(BtBankAccConstant.STATUE_CANCELLATION)
                .append(" and bank_acc is not null AND bus_type <> '3' ) ");

        if (StringUtils.isNotEmpty(likesBankName)) {
            accQuery.append(" and acc.bank_name like '%").append(likesBankName).append("%' ");
        }
        if (StringUtils.isNotEmpty(accName)) {
            accQuery.append(" and acc.acc_name like '%").append(accName).append("%' ");
        }
        if (!EmptyUtils.isEmpty(isTransact) && "1".equals(isTransact)) {
            accQuery.append(" and 1<>1 ");
        }
        // 账号用途 —— 只在 bt_bank_acc 表上应用
        if (CharSequenceUtil.isNotBlank(accUseCode)) {
            String accUseRegex = accUseCode.replace(",", "|");
            accQuery.append(" and acc.acc_use_code REGEXP '(^|,)(").append(accUseRegex).append(")(,|$)' ");
        }
        if (StringUtils.isNotEmpty(accSql)) {
            accQuery.append(accSql);
        }

        // bt_bank_acc_app 部分，仅当 accUseCode 为空时拼接（因为该表没有 acc_use_code 字段）
        StringBuilder appQuery = new StringBuilder();
        if (CharSequenceUtil.isBlank(accUseCode)) {
            appQuery.append("select acc.id as id,'' as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,ac.name AS corpName, acc.status as status ,");
            appQuery.append(" acc.acc_type as accType,acc.bank_type_id as bankTypeId, nat.nature_name as natureName,att.attribute_name as attributeName,");
            appQuery.append(" acc.nature_id as natureId,acc.attribute_id as attributeId,acc.bank_name as bankName, acc.prov as prov,acc.city as city,");
            appQuery.append(" acc.electric_bill as electricBill, acc.circulation_mode as circulationMode, '' as isOnline,'' as validSign,'0' as isFrom ");
            appQuery.append(" ,null AS checkExpireDate,acc.is_domestic_bank as isDomesticBank, null as regDate,'' as receiptCode,'' accUseCode,null as rates,'0' isClose  ");
            appQuery.append(" from bt_bank_acc_app acc ");
            appQuery.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
            appQuery.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
            appQuery.append(" LEFT JOIN sys_corp ac ON ac.id=acc.corp_id ");
            appQuery.append(" where acc.status in (").append(BtBankAccConstant.STATUE_CHECK).append(") and acc.bus_type = ").append(busType).append(" ");
            if (StringUtils.isNotEmpty(likesBankName)) {
                appQuery.append(" and acc.bank_name like '%").append(likesBankName).append("%' ");
            }
            if (StringUtils.isNotEmpty(accName)) {
                appQuery.append(" and acc.acc_name like '%").append(accName).append("%' ");
            }
            if (!EmptyUtils.isEmpty(isTransact) && "0".equals(isTransact)) {
                appQuery.append(" and 1<>1 ");
            }
            if (StringUtils.isNotEmpty(appSql)) {
                appQuery.append(appSql);
            }
        }

        // 最终拼接：如果 appQuery 存在则用 UNION，否则只用 accQuery
        StringBuilder finalSql = new StringBuilder(" select * from ( ");
        if (appQuery.length() > 0) {
            finalSql.append(appQuery).append(" UNION ");
        }
        finalSql.append(accQuery).append(" ) bankacc where 1 = 1 ");

        dataTablesInput.getFormdata().remove("likes_bankName");
        dataTablesInput.getFormdata().remove("likes_accName");
        return getDynamicBySQL(dataTablesInput, finalSql.toString(), BtBankAccHandleVO.class, "", false);
	}
	@SuppressWarnings("unchecked")
	public List<BtBankAcc> getBankaccList() {
		return this.getSession().createQuery(" from BtBankAcc where validSign = " + BtBankAccConstant.VALIDSIGN_1 + " ").list();
	}
	/**
	 * 根据银行接口代码得到银行账号，用与银企直联
	 * BT_BANK_ACC table列表
	 *
	 * @return
	 */
	public DataTablesOutput getDataTableByBifCode(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,acccur.cur_id as curId,cur.cur_name as curName, ");
		sql.append(" acc.acc_type as accType,att.attribute_name as attributeName,acc.parent_sign as parentSign,acc.bif_Code as bifCode,t.name as bifName, ");
		sql.append(" nat.nature_name as natureName,acc.bank_name as bankName, ");
		sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,acc.is_online as isOnline ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join bt_bank_acc_cur acccur on acccur.bank_acc_id = acc.id ");
		sql.append(" left join bt_currency cur on acccur.cur_id = cur.id ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bis_bif_init t on acc.bif_Code = t.id");
		sql.append(" where 1=1 and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.is_online = " + BtBankAccConstant.ISONLINE_1);
		sql.append(" and acc.bank_acc not in (select bank_acc from bt_bank_acc_app where status <> " + BtBankAccConstant.STATUE_CANCELLATION + " and bank_acc is not null ) ");
		String bifCode = (String) dataTablesInput.getFormdata().get("likes_bifCode");
		if (StringUtils.isNotEmpty(bifCode)) {
			dataTablesInput.getFormdata().put("likes_bifCode", bifCode);
		} else {
			dataTablesInput.getFormdata().put("likes_bifCode", "--");
		}
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false);
	}
	/**
	 * 查询账号是否已发生业务
	 *
	 * @param bankAcc
	 * @return
	 */
	public boolean checkQuoteBusiness(String bankAcc) {
		StringBuilder sql = new StringBuilder();
		Map<String, String> params = new HashMap<>();
		sql.append(" select * from bt_bank_acc acc where acc.bank_acc = :bankAcc ");
		sql.append(" and ( exists (select 1 from nis_billhead b where b.payer_acc = acc.bank_acc) or exists (select 1 from nis_allocation_req req where req.payer_acc = acc.bank_acc) )");
		params.put("bankAcc", bankAcc);
		List list = listBySQL(sql.toString(), params);
		return EmptyUtils.isEmptyList(list);
	}
	/**
	 * 查询账号是否存在银行账户父子关系
	 *
	 * @param id
	 * @return
	 */
	public boolean checkQuotePrentBankAcc(String id) {
		StringBuilder sql = new StringBuilder();
		Map<String, String> params = new HashMap<>();
		sql.append(" select * from bt_bank_acc_cur acccur where acccur.prent_acc_id = :id ");
		params.put("id", id);
		List list = listBySQL(sql.toString(), params);
		return EmptyUtils.isEmptyList(list);
	}
	/**
	 * 查询账号是否存在内部账户父子关系
	 *
	 * @param id
	 * @return
	 */
	public boolean checkQuoteFcAcc(String id) {
		StringBuilder sql = new StringBuilder();
		Map<String, String> params = new HashMap<>();
		sql.append(" select * from bt_bank_acc_cur cur where (cur.counter_acc is not null or cur.super_counter_acc is not null) and  cur.bank_acc_id = :id ");
		params.put("id", id);
		List list = listBySQL(sql.toString(), params);
		return EmptyUtils.isEmptyList(list);
	}
	/**
	 * 查询账号是否作为实体账号被引用
	 *
	 * @param id
	 * @return
	 */
	public boolean checkQuoteEntityBankAcc(String id) {
		StringBuilder sql = new StringBuilder();
		Map<String, String> params = new HashMap<>();
		sql.append(" select * from bt_bank_acc acc where acc.entity_acc_id = :id ");
		params.put("id", id);
		List list = listBySQL(sql.toString(), params);
		return EmptyUtils.isEmptyList(list);
	}
	/**
	 * 是否有父账号
	 *
	 * @param id
	 * @return
	 */
	public boolean isHasParentAccById(String id) {
		StringBuilder sql = new StringBuilder();
		Map<String, String> params = new HashMap<>();
		sql.append(" select * from bt_bank_acc_cur acccur where acccur.bank_acc_id  = :id and acccur.prent_acc_id is not null ");
		params.put("id", id);
		List list = listBySQL(sql.toString(), params);
		return !EmptyUtils.isEmptyList(list);
	}
	/**
	 * 是否有子账号
	 *
	 * @param id
	 * @return
	 */
	public boolean isHasChildAccById(String id) {
		StringBuilder sql = new StringBuilder();
		Map<String, String> params = new HashMap<>();
		sql.append(" select * from bt_bank_acc_cur acccur where acccur.prent_acc_id  = :id  ");
		params.put("id", id);
		List list = listBySQL(sql.toString(), params);
		return !EmptyUtils.isEmptyList(list);
	}
	/**
	 * 根据账户定义的年检到期日获取到期时间内的
	 *
	 * @author qinl
	 * @date 2019-07-04
	 * @param searchDate [账户年检到期提醒提前天数]
	 * @return 年检到期的账户集合
	 */
	public List<BtBankAccVO> listByExpireDate(String searchDate){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT");
		sql.append(" ACC.ID,ACC.BANK_ACC,ACC.ACC_NAME,ACC.CHECK_EXPIRE_DATE,ACC.STATUS,ACC.VALID_SIGN,ACC.CREATE_BY");
		sql.append(" FROM BT_BANK_ACC ACC ");
		sql.append(" WHERE ACC.VALID_SIGN = '").append(BtBankAccConstant.VALIDSIGN_1).append("'");
		sql.append(" AND ACC.STATUS = ").append(BtBankAccConstant.STATUE_CHECK);
		sql.append(" AND ACC.CHECK_EXPIRE_DATE IS NOT NULL");
		sql.append(" and ACC.CHECK_EXPIRE_DATE <=:searchDate");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("searchDate", DateUtils.parse(searchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
		return listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
	}
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,ac.name AS corpName, acc.status as status , ");
		sql.append(" acc.acc_type as accType,acc.bank_type_id as bankTypeId, nat.nature_name as natureName,att.attribute_name as attributeName,");
		sql.append(" acc.nature_id as natureId,acc.attribute_id as attributeId,acc.bank_name as bankName, acc.prov as prov,acc.city as city,");
		sql.append(" acc.electric_bill as electricBill, acc.circulation_mode as circulationMode,acc.is_online as isOnline,acc.valid_sign as validSign ");
		sql.append(" ,acc.check_expire_date as checkExpireDate,acc.is_domestic_bank as isDomesticBank,acc.acc_use_code as accUseCode ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" LEFT JOIN sys_corp ac ON ac.id=acc.corp_id ");
		sql.append(" where 1=1 ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(" and ").append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "acc", false);
	}

	public List<BtBankAcc> listByEndDate(String accType,String searchDate){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT");
		sql.append(" ACC.* ");
		sql.append(" FROM BT_BANK_ACC ACC ");
		sql.append(" WHERE ACC.VALID_SIGN = '").append(BtBankAccConstant.VALIDSIGN_1).append("'");
		sql.append(" AND ACC.STATUS = ").append(BtBankAccConstant.STATUE_CHECK);
		sql.append(" and acc.acc_type = :accType ");
		sql.append(" AND ACC.end_date IS NOT NULL");
		sql.append(" and ACC.end_date <=:searchDate");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("accType",accType);
		params.put("searchDate", DateUtils.parse(searchDate, DateUtils.PATTERN_CLASSICAL));
		return listBySQLAliasToBean(sql.toString(), params, BtBankAcc.class);
	}
}
