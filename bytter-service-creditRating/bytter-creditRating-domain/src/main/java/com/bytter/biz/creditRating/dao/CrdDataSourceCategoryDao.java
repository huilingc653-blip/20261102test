package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrdDataSourceCategory;
import com.bytter.biz.creditRating.vo.CrdDataSourceCategoryVo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class CrdDataSourceCategoryDao extends BaseDao<CrdDataSourceCategory, String> {

    public List<CrdDataSourceCategoryVo> dataTable(){
        StringBuilder sql = new StringBuilder();
        sql.append(" select id as id, DATA_SOURCE_CATEGORY_NAME as name, DATA_SOURCE_CATEGORY_CODE as code from DATA_SOURCE_CATEGORY ");
        return listBySQLAliasToBean(sql.toString(), new HashMap<>(), CrdDataSourceCategoryVo.class);
    }
}
