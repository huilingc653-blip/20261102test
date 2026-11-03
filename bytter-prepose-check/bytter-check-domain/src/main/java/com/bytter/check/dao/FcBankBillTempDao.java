package com.bytter.check.dao;
import com.bytter.check.entity.FcBankBillTemp;
import com.bytter.core.base.BaseDao;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 银行未达账初始化
* @author onlineGenerator
* @date 2017-06-08 09:16:58
* @version 1.0
*
*/
@Repository
public class FcBankBillTempDao extends BaseDao<FcBankBillTemp,String>{
	/**
	 * 清空临时表数据
	 */
	public void deleteTemp() {
        String sql = "delete from fc_bank_bill_temp";
        createSQLQuery(sql).executeUpdate();
	}
	
	/**
	 * 查询当前用户有权限的银行账号
	 */
	//TODO SYS服务提供
	public List<BtBankAccDTO> getGenBankAcc(String userId){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select id,bank_acc from bt_bank_acc where id in (  ");
		sb.append(" select DISTINCT t.account_id  from sys_account_grant t  inner join bt_bank_acc acc on t.account_id = acc.id  ");
		sb.append("  where 1=1 and (is_online = 0 or is_online = 1) and ( 1 = 1 or account_type = 2 or account_type = 3) and can_query_detail = 1 and t.user_id=:userId) ");//
		params.put("userId", userId);
		return listBySQLAliasToBean(sb.toString(), params, BtBankAccDTO.class);
	}
}
