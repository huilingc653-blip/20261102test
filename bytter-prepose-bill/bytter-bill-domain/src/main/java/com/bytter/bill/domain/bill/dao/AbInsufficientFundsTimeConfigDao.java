package com.bytter.bill.domain.bill.dao;

import com.bytter.bill.domain.bill.entity.AbInsufficientFundsTimeConfig;
import com.bytter.bill.domain.bill.vo.AbInsufficientFundsTimeConfigVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
* @Description 票据银行余额不足时间配置dao
* @author zhouss
* @date 2025/6/24 11:00
*/
@Repository
public class AbInsufficientFundsTimeConfigDao extends BaseDao<AbInsufficientFundsTimeConfig, String> {
    public DataTablesOutput<AbInsufficientFundsTimeConfigVO> dataTable(DataTablesInput input) {
        StringBuilder sql = new StringBuilder("select ");
        Map<String, Object> params = new HashMap<>();
        sql.append(" a.id,a.bank_type_id as bankTypeId,a.bank_type_name as bankTypeName,a.time,a.status,a.is_deleted as isDeleted ");
        sql.append(" from ab_short_funds_time_config a where a.is_deleted is null ");
        DataTablesOutput dataOutInfo = this.getDynamicBySQL(input, sql.toString(), AbInsufficientFundsTimeConfigVO.class, "a", false, params);
        return dataOutInfo;
    }

    public int getRepeatData(String bankTypeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from ab_short_funds_time_config where bank_type_id =:bankTypeId and is_deleted is null ");
        Map<String, Object> params = new HashMap<>();
        params.put("bankTypeId", bankTypeId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
}
