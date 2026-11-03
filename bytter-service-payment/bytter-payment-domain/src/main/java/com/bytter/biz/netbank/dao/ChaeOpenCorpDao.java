package com.bytter.biz.netbank.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.vo.ChaeOpenCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 差额下拨启用单位
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/10/16
 */
@Repository
public class ChaeOpenCorpDao extends BaseDao {
    public DataTablesOutput<ChaeOpenCorpVO> dataTable(DataTablesInput dataTablesInput) {
        String corpId = (String) dataTablesInput.getFormdata().get("corpId");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.id,sc.ID corpId,sc.code corpCode,sc.`NAME` corpName,c.`VALUE` isChaeOpenCorp ");
        sql.append("FROM sys_config c ");
        sql.append("JOIN sys_corp sc ON c.`NAME` = sc.ID  ");
        sql.append("WHERE c.`TYPE` = 'nis-isChaeOpenCorp' ");
        if (ObjectUtil.isNotEmpty(corpId)){
            sql.append(" AND ").append(SqlUtil.getInSql(Arrays.asList(corpId.split(",")), "sc.ID", true));
        }

        return this.getDynamicBySQL(dataTablesInput, sql.toString(), ChaeOpenCorpVO.class, null, false);
    }
}
