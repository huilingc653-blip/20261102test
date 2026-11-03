package com.bytter.biz.budget.dao;
import com.bytter.biz.budget.entity.FbsMessage;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FBS_MESSAGE
 * @date 2017-06-12 14:53:20
 */
@Repository
public class FbsMessageDao extends BaseDao<FbsMessage, String> {
    /**
     * 日志列表
     *
     * @param billId  外部引用ID
     * @param busName 业务类型
     * @return
     */
    public List<FbsMessage> listMessage(String billId, String busName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.enter_Date as enterDate,f.bill_Id as billId,f.op as op,f.op_Name as opName,");
        sql.append(" f.message_Type as messageType,f.message as message,f.bus_Name as busName");
        sql.append(" from Fbs_Message f where f.bill_Id=:billId and f.bus_Name=:busName ");
        //审批相关日志从工作流日志表取
        sql.append(" and f.message_Type not in ('4','5')");
        sql.append(" order by f.enter_Date desc");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billId", billId);
        params.put("busName", busName);
        return listBySQLAliasToBean(sql.toString(), params, FbsMessage.class);
    }
}
