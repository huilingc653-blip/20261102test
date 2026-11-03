package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrTaskCorpInfo;
import com.bytter.biz.creditRating.entity.CrTaskInfo;
import com.bytter.biz.creditRating.vo.CrTaskInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 评价任务主表dao
 *
 * @author huangyl
 */
@Repository
public class CrTaskInfoDao extends BaseDao<CrTaskInfo, String> {

    public DataTablesOutput<CrTaskInfoVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser currentUser, List<String> taskNoList, List<String> taskIdList) {
        StringBuilder sql = new StringBuilder();
        getCommentSql(sql);
        if (null != taskNoList && taskNoList.size() > 0) {
            sql.append(" and ").append(SqlUtil.getInSql(taskNoList, "info.task_no", true));
        }
        if (null != taskNoList && taskIdList.size() > 0) {
            sql.append(" and ").append(SqlUtil.getInSql(taskIdList, "info.id", true));
        }
//        dataTablesInput.setOrderString(new OrderString("info.task_no", "desc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), CrTaskInfoVO.class, null, false);
    }

    public CrTaskInfoVO getTaskByNo(String taskNo) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        getCommentSql(sql);
        sql.append(" and task_no = :taskNo ");
        params.put("taskNo", taskNo);
        return getBySQLAliasToBean(sql.toString(), params, CrTaskInfoVO.class);
    }

    private void getCommentSql(StringBuilder sql) {
        sql.append("select info.ID as id,info.TASK_NO as taskNo,info.TASK_NAME as taskName,info.TEMPLATE_NAME as templateName,info.COMPUTATIONAL_MODEL_ID as computationalModelId,");
        sql.append("info.status as status,info.TASK_CYCLE as taskCycle,info.TASK_WEIGHT as taskWeight,info.RMK as rmk,info.EVALUATE_START_DATE as evaluateStartDate, ");
        sql.append("info.EVALUATE_END_DATE as evaluateEndDate,info.EVALUATE_CNT as evaluateCnt,info.ORG_TYPE as orgType,info.create_date as createDate,info.creator as creator ");
        sql.append(" from cr_task_info info ");
        sql.append(" where info.status != -2 ");
    }

    /**
     * 检查模板id是否被引用
     * @param modelId
     * @return
     */
    public Boolean isExistByModelId(String modelId) {
        String sql = "select count(1) from cr_task_info info where info.computational_model_id ='" + modelId + "'";
        BigDecimal count = (BigDecimal) createSQLQuery(sql).uniqueResult();
        return count.compareTo(BigDecimal.ZERO) > 0;
    }

    public List<CrTaskCorpInfo> getCrTaskCorpInfoByTaskNo(String taskNo){
        String sql=" SELECT * FROM cr_task_corp_info WHERE task_no=:taskNo and status= ‘95’ ";
        Map<String, Object> params = new HashMap<>();
        params.put("taskNo",taskNo);
        return listBySQLAliasToBean(sql, params, CrTaskCorpInfo.class);
    }
}
