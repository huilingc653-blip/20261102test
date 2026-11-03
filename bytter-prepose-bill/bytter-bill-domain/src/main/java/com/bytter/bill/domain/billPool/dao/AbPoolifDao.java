package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolif;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolifVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @version V1.0
 * @Title: 票据池接口业务Dao
 * @Description: 票据池接口业务数据操作类
 * @author: zhenghj
 * @date: 2019-12-30
 */
@Repository
public class AbPoolifDao extends BaseDao<AbPoolif, String> {
    /**
     * 获取查询的sql语句
     *
     * @return
     */
    private StringBuilder getQuerySql() {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("AP.ID AS id,AP.PIF_CODE AS pifCode,AP.PIF_NAME AS pifName,AP.BIF_CODE AS bifCode,AP.RMK AS rmk,AP.VALID_SIGN AS validSign  ");
        sql.append(" from AB_POOLIF AP  ");
        return sql;
    }
    /**
     * 票据池接口列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbPoolifVO> getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE AP.VALID_SIGN in (:validSign)  ");
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        return getDynamicBySQL(input, sql.toString(), AbPoolifVO.class, "", false, params);
    }
    /**
     * 根据参数查询票据池接口信息
     *
     * @param params 查询参数
     * @return
     */
    public List<AbPoolifVO> getAbPoolifVOByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE 1=1  ");
        if (params.containsKey("id")) {
            sql.append(" and AP.id=:id ");
        }
        if (params.containsKey("validSign")) {
            sql.append(" and AP.VALID_SIGN in (:validSign) ");
        }
        if (params.containsKey("pifCode")) {
            sql.append(" and AP.PIF_CODE=:pifCode ");
        }
        if (params.containsKey("pifName")) {
            sql.append(" and AP.PIF_NAME=:pifName ");
        }
        if (params.containsKey("notId")) {
            sql.append(" and AP.id <> :notId ");
        }
        if (params.containsKey("bifCode")) {
            sql.append(" and AP.bif_Code=:bifCode ");
        }
        return this.listBySQLAliasToBean(sql.toString(), params, AbPoolifVO.class);
    }
    /**
     * 根据ids批量修改票据池接口数据
     *
     * @param params
     * @return
     */
    public int batchUpdate(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update AbPoolif "
                + " set "
                + "updateId = :updateId "
                + ",updateTime = :updateTime ");
        if (params.containsKey("pifCode")) {
            hql.append(",pifCode = :pifCode ");
        }
        if (params.containsKey("pifName")) {
            hql.append(",pifName = :pifName ");
        }
        if (params.containsKey("bifId")) {
            hql.append(",bifId = :bifId ");
        }
        if (params.containsKey("bifCode")) {
            hql.append(",bifCode = :bifCode ");
        }
        if (params.containsKey("rmk")) {
            hql.append(",rmk = :rmk ");
        }
        if (params.containsKey("validSign")) {
            hql.append(",validSign = :validSign ");
        }
        hql.append(" where ").append(SqlUtil.getInSqlByParams("ids","id",params));
        if (params.containsKey("validSign")) {
            hql.append(" and validSign<>:validSign ");
        }
        return this.batchExecute(hql.toString(), params);
    }
}
