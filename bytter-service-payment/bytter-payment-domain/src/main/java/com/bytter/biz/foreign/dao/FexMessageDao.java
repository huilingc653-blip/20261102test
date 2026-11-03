package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexMessage;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 外汇管理表
 * @date 2019-07-15 15:20:33
 */
@Repository
public class FexMessageDao extends BaseDao<FexMessage, String> {

    /**
     * 日志列表
     *
     * @param billNo  外部引用ID
     * @param busType 业务类型
     * @return
     */
    public List<FexMessage> listMessage(String billNo, String busType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.enrol_Date as enrolDate,f.bill_No as billNo,f.op as op,f.op_Name as opName,");
        sql.append(" f.message_Type as messageType,f.message as message,f.bus_Type as busType");
        sql.append(" from Fex_Message f where f.bill_No=:billNo and f.bus_Type=:busType ");
        sql.append(" and f.message_Type not in ('4','5','26')");
        sql.append(" order by f.enrol_Date desc");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        params.put("busType", busType);
        return listBySQLAliasToBean(sql.toString(), params, FexMessage.class);
    }

}
