package com.bytter.biz.corpCashier.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.corpCashier.entity.NisCorpCashier;
import com.bytter.biz.corpCashier.vo.NisCorpCashierVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 单位出纳表dao
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/7/11
 */
@Repository
public class NisCorpCashierDao extends BaseDao<NisCorpCashier,String> {
    public DataTablesOutput<NisCorpCashierVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        Map<String, Object> params = new HashMap<>();
        String userName = (String) dataTablesInput.getFormdata().get("likes_userName");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT c.id,c.corp_id,c.corp_code,c.corp_name  ");
        sql.append("FROM nis_corp_cashier c ");
        sql.append("JOIN nis_corp_cashier_user cu ON c.ID = cu.CORP_CASHIER_ID ");
        sql.append("WHERE c.`STATUS` = '1' ");
        if (ObjectUtil.isNotEmpty(userName)){
            sql.append("and cu.USER_NAME like  :userName ");
            params.put("userName", "%" + userName + "%");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisCorpCashierVO.class, "c", false, params);
    }

    public Boolean checkCorpCashier(String corpId, String id) {
        boolean flag = false;
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        String sql = "SELECT id FROM nis_corp_cashier WHERE corp_id = :corpId ";
        if (ObjectUtil.isNotEmpty(id)){
            params.put("id", id);
            sql += " and id <> :id ";
        }
        List<NisCorpCashierVO> list = this.listBySQLAliasToBean(sql, params,NisCorpCashierVO.class);
        if (ObjectUtil.isNotEmpty(list)){
            flag = true;
        }
        return flag;
    }
}
