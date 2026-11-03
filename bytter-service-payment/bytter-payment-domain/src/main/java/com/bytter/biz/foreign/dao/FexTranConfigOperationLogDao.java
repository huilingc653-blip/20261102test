package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranConfigOperationLog;
import com.bytter.biz.foreign.vo.FexTranConfigOperationLogVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import org.springframework.stereotype.Repository;

/**
 * @author ZZF
 * @version 1.0
 * @Title: Dao
 * @Description: 交易模板配置操作日志Dao
 * @date 2025-11-17 15:20:33
 */
@Repository
public class FexTranConfigOperationLogDao extends BaseDao<FexTranConfigOperationLog, String> {
    /**
     * 外汇产品表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, operation_page, operation_content, before_data, after_data, create_by, create_name, create_date," +
                " is_delete,TO_CHAR(create_date, 'YYYY-MM-DD HH24:MI:SS') as createDateStr " +
                "from  fex_tran_config_operation_log p where is_delete=0");
        return sql.toString();
    }

    /**
     * 外汇产品列表查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        dataTablesInput.setOrderString(new OrderString("create_date", "desc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexTranConfigOperationLogVo.class, "p", false);
    }


}
