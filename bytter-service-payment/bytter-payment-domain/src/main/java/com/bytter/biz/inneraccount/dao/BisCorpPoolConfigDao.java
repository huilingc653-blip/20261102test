package com.bytter.biz.inneraccount.dao;

import com.bytter.biz.inneraccount.entity.BisCorpPoolConfig;
import com.bytter.biz.inneraccount.vo.BisCorpPoolConfigVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Title: Dao
 * @Description: 多级单位余额核算-单位归集配置表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Repository
public class BisCorpPoolConfigDao extends BaseDao<BisCorpPoolConfig, String> {

    /**
     * 查询 单位归集配置 列表数据
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id as id,c.config_code as configCode,c.config_name as configName,c.bus_type as busType,");
        sql.append(" c.valid_sign as validSign from BIS_CORP_POOL_CONFIG c where 1=1 ");
        return getDynamicBySQL(input, sql.toString(), BisCorpPoolConfigVO.class, "c", false);
    }

    /***
     * 获取 单位归集配置有效的数据
     * @return
     */
    public List<BisCorpPoolConfigVO> getCorpPoolConfigList() {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id as id,c.config_code as configCode,c.config_name as configName,c.bus_type as busType,");
        sql.append(" c.valid_sign as validSign from BIS_CORP_POOL_CONFIG c where c.valid_sign = '1' ");
        return this.listBySQLAliasToBean(sql.toString(), null, BisCorpPoolConfigVO.class);
    }
}
