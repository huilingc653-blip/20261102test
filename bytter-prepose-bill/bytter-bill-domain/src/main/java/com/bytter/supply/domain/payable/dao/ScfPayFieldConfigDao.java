package com.bytter.supply.domain.payable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.payable.entity.ScfPayFieldConfig;
import com.bytter.supply.domain.payable.vo.ScfPayFieldConfigVO;
import org.springframework.stereotype.Repository;

@Repository
public class ScfPayFieldConfigDao extends BaseDao<ScfPayFieldConfig, String> {

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT f.* FROM scf_pay_field_config f WHERE 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfPayFieldConfigVO.class, "f", false);
    }
}
