package com.bytter.biz.dataexchange.basic.dao;

import com.bytter.biz.dataexchange.basic.entity.DSLInterfaceLog;
import com.bytter.biz.dataexchange.basic.vo.DSLInterfaceLogVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import jodd.util.StringUtil;
import oracle.sql.CLOB;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author lijinquan
 * @description:
 * @date 2024-04-29 15:44
 */

@Repository("dslInterfaceLogDao2")
public class DSLInterfaceLogDao2 extends BaseDao<DSLInterfaceLog,String> {

    /**
     * 查询ERP接口日志信息列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select l.id,l.SOURCE_ID as sourceId,l.type, l.interface_name,l.interface_code,l.log_time,to_char(l.log_time,'yyyy-mm-dd hh24:mi:ss') as logTimeStr ");
        sql.append(" from DSL_INTERFACE_LOG l where 1 = 1 ");
        DataTablesOutput output = this.getDynamicBySQL(input, sql.toString(), DSLInterfaceLogVo.class, "l", false);
        return output;
    }

    /**
     * 根据日志时间删除接口日志数据
     * @param beforeLogTimeStr  删除日志时间
     * @return
     */
    public int deletehreeMonthsAgoLogs(Date threeMonthsAgo){
        int i = createSQLQuery("DELETE FROM DSL_INTERFACE_LOG l WHERE l.log_time <= ?", threeMonthsAgo).executeUpdate();
        return i;
    }

}
