package com.bytter.system.api;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.ErpBasicDataVOVO;
import java.util.List;
import java.util.Map;
public interface ISystemForeignService {
    /**
     * 参考 BalanceQueryDao.queryAccBalanceAndBank()
     * @param dto
     * @param userCode
     * @param corpAuth
     * @param aTrue
     * @return
     */
    List<BalanceQueryDTO> queryAccBalanceAndBank(BalanceQueryDTO dto, String userCode, List<String> corpAuth, String... aTrue);
	/**
	 * 参考SQL
	 * sql.append(" select a.blnctr_acc as blnctrAcc, a.cur_id as curId, a.overdraft_sign as overdraftSign, a.overdraft_money as overdraftMoney, a.balance_way as balanceWay, " );
	 * 		sql.append(" a.net_id as netId, a.acc_type as accType, a.charge_acc as chargeAcc, a.subject_id as subjectId, b.code as netCode, c.cur_code as curCode from fc_acc a ");
	 * 		sql.append(" join sys_corp b on a.net_id = b.id ");
	 * 		sql.append(" join bt_currency c on a.cur_id = c.id ");
	 * 		sql.append(" where 1=1 and ").append(getSQLIn(accList, 900, "a.blnctr_acc"));
	 * @param accounts
	 * @return
	 */
	List<FcAccCorpCurrencyDTO> getAccCorpCurDataByAccounts(List<String> accounts);
	public List listBySQL(String sql, final Map<String, ?> params);
	public <B> List<B> listBySQLAliasToBean(String sql, final Map<String, ?> params, final Class<B> clazz);
	public List<ErpBasicDataVOVO> listErpBasicDataVOVO(String sql);
}
