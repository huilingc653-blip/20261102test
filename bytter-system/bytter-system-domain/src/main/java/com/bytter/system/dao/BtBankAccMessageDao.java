package com.bytter.system.dao;
import com.bytter.system.api.dto.BtBankAccMessageDTO;
import com.bytter.system.entity.BtBankAccMessage;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
* @Title: Dao
* @Description: BtBankAccMessage
* @author onlineGenerator
* @date 2017-06-12 14:53:20
* @version 1.0
*
*/
@Repository
public class BtBankAccMessageDao extends BaseDao<BtBankAccMessage,String>{
	/**
	 * 日志列表
	 * @param billNo 外部引用ID
	 * @param busType 业务类型
	 * @return
	 */
	public List<BtBankAccMessageDTO> listMessage(String billNo, String busType) {
		StringBuilder sql=new StringBuilder();
		sql.append("select n.id as id,n.enrol_Date as enrolDate,n.bill_No as billNo,n.op as op,n.op_Name as opName,");
		sql.append(" n.message_Type as messageType,n.message as message,n.bus_Type as busType");
		sql.append(" from bt_bankacc_message n where n.bill_No=:billNo and n.bus_Type=:busType ");
		//审批相关日志从工作流日志表取
		sql.append(" and n.message_Type not in ('4','5','26')");
		sql.append(" order by n.enrol_Date desc");
		HashMap<String, String> params = new HashMap<>();
        params.put("billNo", billNo);
        params.put("busType", busType);
		return listBySQLAliasToBean(sql.toString(), params, BtBankAccMessageDTO.class);
	}
	
	public boolean saveMessage(String billNo, Integer messageType, String message, String busType, ICurrentUser user){
		return saveMessage(billNo, messageType, message, busType, user.getLoginName(), user.getUsername());
	}
	
	/**
	 * 写入日志信息表
	 * @param billNo 外部引用ID
	 * @param messageType 日志类型
	 * @param message 日志信息
	 * @param busType 业务类型
	 * @param userCode 操作人代码
	 * @param userName 操作人名称
	 * @return
	 */
	public boolean saveMessage(String billNo, Integer messageType, String message, String busType, String userCode, String userName){
		BtBankAccMessage msg = new BtBankAccMessage();
		msg.setBillNo(billNo);
		msg.setEnrolDate(new Date());
		msg.setMessageType(messageType);
		msg.setMessage(message);
		msg.setMessage(message);
		msg.setBusType(busType);
		msg.setOp(userCode);
		msg.setOpName(userName);
		return saveMessage(msg);
	}
	
	public boolean saveMessage(BtBankAccMessage msg){
		if(msg==null||StringUtils.isAllBlank(msg.getBillNo(),msg.getMessage())){
			return false;
		}
		this.save(msg);
		return true;
	}
}
