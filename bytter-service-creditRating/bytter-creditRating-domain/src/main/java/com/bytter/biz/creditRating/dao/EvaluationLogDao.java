package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.EvaluationLog;
import com.bytter.biz.creditRating.vo.EvaluationLogVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

/**
 * @Title: Dao
 * @Description: 信用评估操作日志
 * @author chenqin
 * @date 2024-08-28
 *
 */
@Repository
public class EvaluationLogDao extends BaseDao<EvaluationLog, String> {

    /**
     * 获取信用评估操作日志数据列表
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql=new StringBuilder();
        sql.append(" select el.id as id, el.quote_id as quoteId, el.log_type as logType, el.create_name as createName,");
        sql.append(" el.msg as msg, el.create_by as createBy, el.create_date as createDate");
        sql.append(" from evaluation_log el ");
        sql.append(" where 1 = 1");

        //dataTablesInput.setOrderString(new OrderString("el.create_date","desc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), EvaluationLogVO.class, null, false, null);
    }

}
