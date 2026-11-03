package com.bytter.guarantee.domain.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.guarantee.domain.entity.GuaLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
@Repository
public class GuaLogDao extends BaseDao<GuaLog, String> {
    /**
     * 通过担保id查询担保的日志信息
     * 
     * @param guaId
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listGuaLog(String guaId, DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT BT.ID,BT.GUA_ID,BT.MSG_TYPE,BT.MONEY,BT.CREATE_BY,BT.CREATEBY_NAME as createByName,");
        sql.append("BT.CREATE_DATE,BT.MESSAGE FROM GUA_LOG  BT  WHERE  1=1 AND  ");
        sql.append("  BT.GUA_ID=:guaId");
        sql.append("  ORDER BY  CREATE_DATE DESC ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("guaId", guaId);
        return getDynamicBySQL(input, sql.toString(), GuaLog.class, "", false, params);
    }
    /**
     * 通过担保id查询担保的日志信息
     * 
     * @param id
     * @return
     */
    public List<GuaLog> getListOpMesageByBondId(String guaId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT BT.ID,BT.GUA_ID,BT.MSG_TYPE,BT.MONEY,BT.CREATE_BY,BT.CREATEBY_NAME,");
        sql.append("BT.CREATE_DATE,BT.MESSAGE FROM GUA_LOG  BT  WHERE  1=1 AND  ");
        sql.append("  BT.GUA_ID=:guaId");
        sql.append("  ORDER BY  CREATE_DATE DESC ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("guaId", guaId);
        return listBySQLAliasToBean(sql.toString(), params, GuaLog.class);
    }
}
