package com.bytter.biz.inneraccount.dao;

import com.bytter.biz.inneraccount.entity.BisCorpPoolConfigRule;
import com.bytter.biz.inneraccount.vo.BisCorpPoolConfigRuleVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 多级单位余额核算-单位归集配置规则表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Repository
public class BisCorpPoolConfigRuleDao extends BaseDao<BisCorpPoolConfigRule, String> {

    /**
     * 根据业务id获取所有业务规则
     *
     * @param configId
     * @return
     */
    public List<BisCorpPoolConfigRuleVO> getConfigRuleByConfigId(String configId) {
        String sql = " select * from BIS_CORP_POOL_CONFIG_RULE where config_id = :configId order by sort ";
        Map<String, Object> params = new HashMap<>();
        params.put("configId", configId);
        return this.listBySQLAliasToBean(sql, params, BisCorpPoolConfigRuleVO.class);
    }

    /**
     * 根据业务类别id删除业务规则
     *
     * @param configId
     */
    public void deleteByConfigId(String configId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from BIS_CORP_POOL_CONFIG_RULE where config_id = '" + configId + "'");
        createSQLQuery(sql.toString()).executeUpdate();
    }
}
