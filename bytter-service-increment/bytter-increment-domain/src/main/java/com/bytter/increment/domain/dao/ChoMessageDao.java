package com.bytter.increment.domain.dao;
import com.bytter.increment.domain.entity.ChoMessage;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: CHO_MESSAGE
 * @date 2017-09-23 14:53:20
 */
@Repository
public class ChoMessageDao extends BaseDao<ChoMessage, String> {
    /**
     * 日志列表
     *
     * @param billNo  外部引用ID
     * @param busType 业务类型
     * @return
     */
    public List<ChoMessage> listMessage(String billNo, String busType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id as id,c.enrol_Date as enrolDate,c.bill_No as billNo,c.op as op,c.op_Name as opName,");
        sql.append(" c.message_Type as messageType,c.message as message,c.bus_Type as busType");
        sql.append(" from Cho_Message c where c.bill_No=:billNo and c.bus_Type=:busType ");
        sql.append(" and c.message_Type not in ('4','5','26')");
        sql.append(" order by c.enrol_Date desc");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        params.put("busType", busType);
        return listBySQLAliasToBean(sql.toString(), params, ChoMessage.class);
    }
}
