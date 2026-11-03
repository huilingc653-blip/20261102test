package com.bytter.supply.domain.payable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.payable.entity.ScfPayConfigVersion;
import com.bytter.supply.domain.payable.vo.ScfPayConfigVersionVO;
import org.springframework.stereotype.Repository;

@Repository
public class ScfPayConfigVersionDao extends BaseDao<ScfPayConfigVersion, String> {

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT v.* FROM scf_pay_config_version v WHERE 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfPayConfigVersionVO.class, "v", false);
    }
}
