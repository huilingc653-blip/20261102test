package com.bytter.credit.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.credit.domain.entity.RatCreditType;
import com.bytter.credit.domain.vo.RatCreditTypeVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
/**
 * @Title: Dao
 * @Description: 授信额度类型
 * @author onlineGenerator
 * @date 2017-07-20 14:54:51
 * @version 1.0
 *
 */
@Repository
public class RatCreditTypeDao extends BaseDao<RatCreditType, String> {
    /**
     * 查询数据列表
     */
    public DataTablesOutput dataTables(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id, ").append(" a.code,a.name,a.name as typeName,a.status, ")
                .append("a.create_name as createName,").append("a.create_by as createBy,")
                .append("a.create_date as createDate,").append("a.update_name as updateName,")
                .append("a.update_by as updateBy,").append("a.update_date as updateDate ")
                .append("from rat_credit_type a where a.status<>-2 ");
        return this.getDynamicParamsBySQL(input, sql.toString(), RatCreditTypeVO.class, "", false, null);
    }
    /**
     * 功能介绍 : 获取授信额度类型列表。<br>
     * <b>日期：</b>2017年8月1日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatCreditTypeVO> getAllCreditType() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id, ").append(" a.code,a.name,a.status, ").append("a.create_name as createName,")
                .append("a.create_by as createBy,").append("a.create_date as createDate,")
                .append("a.update_name as updateName,").append("a.update_by as updateBy,")
                .append("a.update_date as updateDate ").append("from rat_credit_type a ")
                .append("where a.status <> :status ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", CommonConstant.STATUE_DELETE);
        return this.listBySQLAliasToBean(sql.toString(), params, RatCreditTypeVO.class);
    }
    /**
     * 功能介绍 : 获取授信额度类型列表。<br>
     * <b>日期：</b>2017年8月1日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatCreditType> getCreditTypeByCode(String code) {
        String hql = " from RatCreditType a where a.status <> :status and code = :code ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", CommonConstant.STATUE_DELETE);
        params.put("code", code);
        return listByHQL(hql, params);
    }
}
