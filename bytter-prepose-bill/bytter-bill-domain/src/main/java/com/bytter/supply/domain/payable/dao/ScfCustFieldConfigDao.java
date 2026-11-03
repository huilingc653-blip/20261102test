package com.bytter.supply.domain.payable.dao;


import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.payable.entity.ScfCustFieldConfig;
import com.bytter.supply.domain.payable.vo.ScfCustFieldConfigVO;
import org.springframework.stereotype.Repository;

@Repository
public class ScfCustFieldConfigDao extends BaseDao<ScfCustFieldConfig, String> {

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.* FROM scf_cust_field_config c WHERE 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfCustFieldConfigVO.class, "c", false);
    }

    public DataTablesOutput getDataTablesUnionValid(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.* FROM scf_cust_field_config c WHERE 1=1 and c.valid_sign = 1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfCustFieldConfigVO.class, "c", false);
    }
}
