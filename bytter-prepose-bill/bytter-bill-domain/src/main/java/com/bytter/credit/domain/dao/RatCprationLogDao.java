package com.bytter.credit.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.credit.domain.entity.RatProcessMsg;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatCprationLogDao
 * @Description:授信日志Dao
 * @date 2018年11月24日 上午9:14:21
 */
@Repository
public class RatCprationLogDao extends BaseDao<RatProcessMsg, String> {
    /**
     * 通过id查询得到日志列表
     * 
     * @param rarionIds
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listMsgData(String rarionIds, DataTablesInput input) {
        String[] rationId = rarionIds.split(",");
        List<String> ration_id = new ArrayList<String>();
        for (int i = 0; i < rationId.length; i++) {
            ration_id.add(rationId[i]);
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT BT.ID,BT.RATION_ID,BT.MSG_TYPE,BT.MONEY,BT.CREATE_BY,BT.CREATEBY_NAME as createByName,");
        sql.append("BT.CREATE_DATE,BT.MESSAGE FROM RAT_RATION_LOG  BT  WHERE  1=1 AND  ");
        sql.append(getSQLIn(ration_id, 900, "RATION_ID"));
        sql.append("  ORDER BY  CREATE_DATE DESC ");
        return getDynamicBySQL(input, sql.toString(), RatProcessMsg.class, "", false);
    }
}
