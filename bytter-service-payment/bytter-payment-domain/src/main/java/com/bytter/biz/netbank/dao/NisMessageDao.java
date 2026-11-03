package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.NisMessage;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
* @Title: Dao
* @Description: NIS_MESSAGE
* @author onlineGenerator
* @date 2017-06-12 14:53:20
* @version 1.0
*
*/
@Repository
public class NisMessageDao extends BaseDao<NisMessage,String>{
	/**
	 * 日志列表
	 * @param billNo 外部引用ID
	 * @param busType 业务类型
	 * @return
	 */
	public List<NisMessage> listMessage(String billNo, String busType) {
		StringBuilder sql=new StringBuilder();
		sql.append("select n.id as id,n.enrol_Date as enrolDate,n.bill_No as billNo,n.op as op,n.op_Name as opName,");
		sql.append(" n.message_Type as messageType,n.message as message,n.bus_Type as busType");
		sql.append(" from Nis_Message n where n.bill_No=:billNo and n.bus_Type=:busType ");
		//审批相关日志从工作流日志表取
		sql.append(" and n.message_Type not in ('4','5','26')");
		sql.append(" order by n.enrol_Date desc");
		HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        params.put("busType", busType);
		return listBySQLAliasToBean(sql.toString(), params, NisMessage.class);
	}
}
