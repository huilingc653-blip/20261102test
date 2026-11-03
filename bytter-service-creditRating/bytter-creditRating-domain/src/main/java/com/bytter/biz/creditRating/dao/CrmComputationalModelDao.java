package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrmComputationalModel;
import com.bytter.biz.creditRating.vo.CrmComputationalModelVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能介绍 : 计算模型处理<br>
 * <b>日期：</b>2024/8/12<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Repository
public class CrmComputationalModelDao extends BaseDao<CrmComputationalModel, String> {


    /**
     * 功能介绍 : 计算模型列表查询<br>
     */
    public DataTablesOutput getModelDataTables(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select model.id as id,model.MODEL_NAME as modelName,model.MODEL_CODE as modelCode,model.MODEL_FORMULA_NAME as modelFormulaName,");
        sql.append(" model.MODEL_DESCRIPTION as modelDescription,model.CREATE_DATE as createDate,model.VALID_SIGN as validSign ");
        sql.append(" from COMPUTATIONAL_MODEL model ");
        sql.append(" where status=95");
        dataTablesInput.setOrderString(new OrderString("model.CREATE_DATE", "desc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), CrmComputationalModelVO.class, null, false);
    }

    /**
     * 功能介绍 : 根据ID查询CrmComputationalModelVO对象<br>
     */
    public CrmComputationalModelVO getComputationalModelById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select model.*");
        sql.append(" from COMPUTATIONAL_MODEL model ");
        sql.append(" where model.id=:id");
        params.put("id", id);
        return  this.getBySQLAliasToBean(sql.toString(), params, CrmComputationalModelVO.class);
    }

    /**
     * 功能介绍 :查询所有CrmComputationalModelVO对象<br>
     */
    public List<CrmComputationalModelVO> getAllComputationalModel() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select model.*");
        sql.append(" from COMPUTATIONAL_MODEL model ");
        sql.append(" where model.status=95");
        sql.append(" and model.valid_sign='1'");
        return this.listBySQLAliasToBean(sql.toString(), null, CrmComputationalModelVO.class);
    }
}
