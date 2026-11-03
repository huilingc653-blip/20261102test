package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCorpPool;
import com.bytter.biz.foreign.vo.FexCorpPoolBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangzx
 * @version 1.0
 * @Title: dao
 * @Description: 外汇资金池表
 * @date 2020-02-12 14:31:56
 */
@Repository
public class FexCorpPoolDao extends BaseDao<FexCorpPool, String> {

    /**
     * 外汇资金池表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.pool_id as poolId,f.pool_nature as poolNature,f.is_limit_control as isLimitControl,f.cur_id as curId,");
        sql.append("f.rate_type as rateType,b.type_name as typeName,f.rmk as rmk,f.valid_sign as validSign,f.create_by as createBy,f.create_date as createDate,");
        sql.append("f.update_by as updateBy,f.update_date as updateDate,c.pool_code as poolCode,c.pool_name as poolName,c.corp_id as corpId,");
        sql.append("c.pool_sign as poolSign, cur.english_code as englishCode from fex_corp_pool f left join fc_corp_pool c on f.pool_id = c.id ");
        sql.append(" left join bt_currency cur on f.cur_id = cur.id left join bt_rate_type b on f.rate_type = b.id where 1=1 ");
        return sql.toString();
    }

     /**
      * 根据资金池id,单位id,币别id查询外汇资金池列表数据
     * @param poolId
     * @param corpId
     * @param curId
     * @return
     */
    public List<FexCorpPoolBankAccVO> listAllPool(String poolId, String corpId, String curId){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        Map<String, Object> params = new HashMap<>();
        if(StringUtils.isNotBlank(poolId)){
            sql.append(" and f.pool_id = :poolId");
            params.put("poolId",poolId);
        }
        if(StringUtils.isNotBlank(corpId)){
            sql.append(" and c.corp_id = :corpId");
            params.put("corpId",corpId);
        }
        if(StringUtils.isNotBlank(curId)){
            sql.append(" and f.cur_id = :curId");
            params.put("curId",curId);
        }
        sql.append(" order by c.pool_code desc");
        return listBySQLAliasToBean(sql.toString(), params, FexCorpPoolBankAccVO.class);
    }

    /**
     * 获取有效外汇资金池
     * @param joinSql
     * @return
     */
    public List<FexCorpPoolBankAccVO> getFexCorpPools(String joinSql){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and f.valid_sign =:validSign ");
        sql.append(joinSql);
        Map<String,Object> params = new HashMap<>();
        params.put("validSign",CommonConstant.VALID_SIGN_YES);
        return listBySQLAliasToBean(sql.toString(), params, FexCorpPoolBankAccVO.class);
    }

    /**
     * 获取有效外汇资金池(去重)
     * @param joinSql
     * @return
     */
    public List<FexCorpPoolBankAccVO> getPoolAccUniq(String joinSql){
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as poolId, f.corp_id as corpId, f.pool_code as poolCode, f.pool_name as poolName");
        sql.append(" from fc_corp_pool f where f.pool_sign = '1' and f.status = '1'");
        sql.append(" and f.id in (select p.pool_id from fex_corp_pool p where p.valid_sign =:validSign)");
        sql.append(joinSql);
        Map<String,Object> params = new HashMap<>();
        params.put("validSign",CommonConstant.VALID_SIGN_YES);
        return listBySQLAliasToBean(sql.toString(), params, FexCorpPoolBankAccVO.class);
    }

    /**
     * 根据资金池表id得到外汇资金池信息
     *
     * @param id
     * @return
     */
    public FexCorpPoolBankAccVO getFexCorpPoolBankAccVOById(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        Map<String, Object> params = new HashMap<>();
        if(StringUtils.isNotBlank(id)){
            sql.append(" and f.id = :id");
            params.put("id",id);
        }
        return getBySQLAliasToBean(sql.toString(), params, FexCorpPoolBankAccVO.class);
    }
}
