package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrDataSource;
import com.bytter.biz.creditRating.vo.CrDataSourceVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据源来源
 */
@Repository
public class CrDataSourceDao extends BaseDao<CrDataSource, String> {


    public List<CrDataSourceVo> getData(String dataSourceName) {

        //隐藏03和04
        StringBuilder sql = new StringBuilder();
        sql.append("select dt.ID as id,dt.DATA_SOURCE_CODE as dataSourceCode,dt.DATA_SOURCE_NAME as dataSourceName,dt.DATA_SOURCE_ORIGIN as dataSourceOrigin ");
        sql.append(" from CR_DATA_SOURCE dt where 1=1 AND dt.DATA_SOURCE_CODE not IN ('03','04') ");
        if (EmptyUtils.isNotEmpty(dataSourceName)){
            sql.append("and DATA_SOURCE_NAME like '%"+dataSourceName+"%'");
        }
        return listBySQLAliasToBean(sql.toString(),null,CrDataSourceVo.class);
    }
}
