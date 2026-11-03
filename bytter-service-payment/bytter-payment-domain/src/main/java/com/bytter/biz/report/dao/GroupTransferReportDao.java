package com.bytter.biz.report.dao;

import com.bytter.biz.report.vo.GroupTransferYearVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 集团资金划拨报表dao
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/2/7
 */
@Repository
public class GroupTransferReportDao extends BaseDao {

    /***
     * 年度分析表源数据
     * @param beforeYear
     * @param queryYear
     * @param corpIds
     * @param busTypes
     * @return TODO
     */
    public List<GroupTransferYearVO> queryYearDatatable(String beforeYear, String queryYear, List<String> corpIds, List<String> busTypes) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.bus_type busType,TO_CHAR(d.trans_time,'yyyy-MM') transTime,SUM(d.amt) amt ");
        sql.append("FROM bis_acc_dtl d ");
        sql.append("JOIN bt_bank_acc a ON d.BANK_ACC = a.BANK_ACC ");
        sql.append("INNER JOIN ( SELECT ID, BUS_TYPE FROM nis_bus_type WHERE BUS_TYPE IN (:busTypes) ) t ON d.BUS_TYPE = t.ID ");
        sql.append("WHERE d.TRANS_TIME BETWEEN to_date(:beforeYear,'yyyy-MM-dd') AND TO_DATE(:queryYear,'yyyy-MM-dd') + 1 - 1/86400 ");
        sql.append("AND a.CORP_ID IN (:corpIds) ");
        sql.append("GROUP BY t.BUS_TYPE,TO_CHAR(d.TRANS_TIME,'yyyy-MM') ");
        sql.append("ORDER BY t.BUS_TYPE,TO_CHAR(d.TRANS_TIME,'yyyy-MM') ");

        params.put("beforeYear", beforeYear+"-01-01");
        params.put("queryYear", queryYear+"-12-31");
        params.put("corpIds",corpIds);
        params.put("busTypes",busTypes);

        return listBySQLAliasToBean(sql.toString(), params, GroupTransferYearVO.class);
    }
}
