package com.bytter.system.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.BtNetUKey;
import com.bytter.system.vo.BtNetUKeyVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author qinl
 * @version 1.0
 * @Title: Dao
 * @date 2020-04-16
 */
@Repository
public class BtNetUKeyDao extends BaseDao<BtNetUKey, String> {
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String accCorpAuthSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" b.ukey_no as uKeyNo,b.ukey_user_id as uKeyUserId,b.ukey_user_id as ukeyUserName,b.ebank_login_name as ebankLoginName,c.bank_acc,c.acc_name,c.bank_name,");
		sql.append(" b.CORP_ID,b.BANK_TYPE_ID,b.BANK_LIMIT,b.PERSONAL_LIMIT,b.CUSTODIAN_ID,b.SUPERVISOR_ID,b.OPEN_DATE,b.EXPIRE_DATE,b.ATTACHMENT_ID,a.id");
		sql.append(" ,b.LOGIN_ACCOUNT as loginAccount,b.IP_ADDRESS as ipAddress,b.PORT as port,b.PASSWORD_NO as passwordNo,b.CIPHER_SUITE as cipherSuite");
		sql.append(" ,b.is_head_custodian isHeadCustodian");
		sql.append(" FROM bt_net_ukey_bankacc a ");
		sql.append(" LEFT JOIN bt_net_ukey b on a.ukey_id = b.id ");
		sql.append(" LEFT JOIN bt_bank_acc c on c.id = a.bank_acc_id");
		sql.append(" WHERE 1=1 and ").append(accCorpAuthSql);
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtNetUKeyVO.class, null, false);
	}
	public BtNetUKey getByUKeyNo(String uKeyNo){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" b.ukey_no as uKeyNo,b.ukey_user_id as uKeyUserId,b.ukey_user_id as ukeyUserName,b.ebank_login_name as ebankLoginName,b.id,");
		sql.append(" b.CORP_ID,b.BANK_TYPE_ID,b.BANK_LIMIT,b.PERSONAL_LIMIT,b.CUSTODIAN_ID,b.SUPERVISOR_ID,b.OPEN_DATE,b.EXPIRE_DATE,b.ATTACHMENT_ID,");
		sql.append(" b.LOGIN_ACCOUNT as loginAccount,b.IP_ADDRESS as ipAddress,b.PORT as port,b.PASSWORD_NO as passwordNo,b.CIPHER_SUITE as cipherSuite");
		sql.append(" FROM bt_net_ukey b");
		sql.append(" WHERE b.ukey_no = '").append(uKeyNo).append("'");
		return getBySQLAliasToBean(sql.toString(), null,BtNetUKey.class);
	}
	/**
	 * <p>
	 * 根据条件，查询一条记录
	 * </p>
	 *
	 * @param id [id]
	 * @return (Entity)
	 */
	public BtNetUKeyVO selectById(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" b.id,b.ukey_no as uKeyNo,b.ukey_user_id as uKeyUserId,b.ebank_login_name as ebankLoginName,b.is_head_custodian isHeadCustodian,");
		sql.append(" b.CORP_ID,b.BANK_TYPE_ID,b.BANK_LIMIT,b.PERSONAL_LIMIT,b.CUSTODIAN_ID,b.SUPERVISOR_ID,b.OPEN_DATE,b.EXPIRE_DATE,b.ATTACHMENT_ID,");
		sql.append(" b.LOGIN_ACCOUNT as loginAccount,b.IP_ADDRESS as ipAddress,b.PORT as port,b.PASSWORD_NO as passwordNo,b.CIPHER_SUITE as cipherSuite");
		sql.append(" FROM bt_net_ukey b");
		sql.append(" WHERE 1 = 1");
		sql.append(" AND b.id = '").append(id).append("'");
		return this.getBySQLAliasToBean(sql.toString(), null, BtNetUKeyVO.class);
	}
	/**
	 * 到期日期数据获取
	 * @return
	 */
	public List<BtNetUKeyVO> getExpireData(String searchDate){
		StringBuilder sql = new StringBuilder();
		sql.append(" select k.*,acc.ACC_NAME as accName,acc.BANK_ACC as bankAcc,acc.BANK_NAME as bankName ");
		sql.append(" from bt_net_ukey k ");
		sql.append(" LEFT JOIN bt_net_ukey_bankacc bt on k.id = bt.ukey_id ");
		sql.append(" LEFT JOIN bt_bank_acc acc on acc.id = bt.bank_acc_id ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND expire_date <=:searchDate");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("searchDate", DateUtils.parse(searchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
		return this.listBySQLAliasToBean(sql.toString(), params, BtNetUKeyVO.class);
	}

	/**
	 * 到期日期数据获取-日期相等
	 * @return
	 */
	public List<BtNetUKeyVO> getExpireDataEqual(String searchDate){
		StringBuilder sql = new StringBuilder();
		sql.append(" select k.*,acc.ACC_NAME as accName,acc.BANK_ACC as bankAcc,acc.BANK_NAME as bankName ");
		sql.append(" from bt_net_ukey k ");
		sql.append(" LEFT JOIN bt_net_ukey_bankacc bt on k.id = bt.ukey_id ");
		sql.append(" LEFT JOIN bt_bank_acc acc on acc.id = bt.bank_acc_id ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND expire_date =:searchDate");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("searchDate", DateUtils.parse(searchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
		return this.listBySQLAliasToBean(sql.toString(), params, BtNetUKeyVO.class);
	}
}
