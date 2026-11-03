package com.bytter.finance.domain.basedata.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.basedata.entity.AccrualType;
import com.bytter.finance.domain.basedata.vo.AccrualTypeVO;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: CMS_ACCRUAL_TYPE
 * @date 2022-08-10 14:32:39
 */
@Repository
public class AccrualTypeDao extends BaseDao<AccrualType, String> {
    /**
     * 查询计提方式
     ***/
    public DataTablesOutput queryAccrualType(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select t.id, t.accrual_code as accrualCode, t.accrual_name as accrualName, t.frequency_type as frequencyType, t.frequency_daily_date as frequencyDailyDate, ");
        sql.append(" t.rmk as rmk, t.valid_sign as validSign, t.status as status ");
        sql.append(" from cms_accrual_type t where t.status<> " + BaseDataConstant.STATUS_DELETE);
        return getDynamicBySQL(input, sql.toString(), AccrualTypeVO.class, "", false);
    }
    /**
     * 修改计提方式
     ***/
    public int updateAccrualType(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update AccrualType " + " set " + "updateDate = :updateDate ");
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy ");
        }
        if (params.containsKey("accrualCode")) {
            hql.append(",accrualCode = :accrualCode ");
        }
        if (params.containsKey("accrualName")) {
            hql.append(",accrualName = :accrualName ");
        }
        if (params.containsKey("frequencyType")) {
            hql.append(",frequencyType = :frequencyType ");
        }
        if (params.containsKey("frequencyDailyDate")) {
            hql.append(",frequencyDailyDate = :frequencyDailyDate ");
        }
        if (params.containsKey("rmk")) {
            hql.append(",rmk = :rmk ");
        }
        if (params.containsKey("validSign")) {
            hql.append(",validSign = :validSign ");
        }
        hql.append(" where id = :id ");
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 删除计提方式
     */
    public int deleteAccrualType(String id) {
        String sql = "update cms_accrual_type set status =:status where id=:id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("status", BaseDataConstant.STATUS_DELETE);
        return createSQLQuery(sql, param).executeUpdate();
    }
    /**
     * 查询删除类型是否被引用
     **/
    public BigDecimal judgeAccrualTypeByCode(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1)  ");
        sql.append("from (  ");
        sql.append("         select info.ACCRUAL_TYPE accrualType  ");
        sql.append("         from CMS_PROVIDE_LOAN_INFO info  ");
        sql.append("         where info.ACCRUAL_TYPE = :id  ");
        sql.append("               and info.status <> :status");
        sql.append("         union  ");
        sql.append("         select send.ACCRUAL_TYPE accrualType  ");
        sql.append("         from CMS_PROVIDE_LOAN_SEND send  ");
        sql.append("         where send.ACCRUAL_TYPE = :id  ");
        sql.append("               and send.status <> :status");
        sql.append("     ) t ");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("status", BaseDataConstant.STATUS_DELETE);
        return (BigDecimal) createSQLQuery(sql.toString(), param).uniqueResult();
    }
    /**
     * 查询类型编号是否存在
     **/
    public boolean judgeTypeByCode(String accrualCode, String accrualName, String id) {
        String sql = "select id,accrual_code as accrualCode,accrual_name as accrualName from cms_accrual_type where (accrual_code = :code or accrual_name = :name) and status <> -2";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("code", accrualCode);
        param.put("name", accrualName);
        List<AccrualType> infoList = listBySQLAliasToBean(sql, param, AccrualType.class);
        if (infoList != null && infoList.size() > 0) {
            if (EmptyUtils.isNotEmpty(id)) {// 修改
                for (AccrualType accrualType : infoList) {
                    if (accrualType.getId().equals(id)) {// 未修改值 提交
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }
    /**
     * 功能说明：根据类型code查询计提方式
     *
     * @param accrualCode
     */
    public AccrualTypeVO queryAccrualTypeByCode(String accrualCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select t.id, t.accrual_code as accrualCode, t.accrual_name as accrualName, t.frequency_type as frequencyType, t.frequency_daily_date as frequencyDailyDate, ");
        sql.append(" t.rmk as rmk, t.valid_sign as validSign, t.status as status ");
        sql.append(" from cms_accrual_type t where t.status <> -2 and t.accrual_code=:accrualCode ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accrualCode", accrualCode);
        return getBySQLAliasToBean(sql.toString(), map, AccrualTypeVO.class);
    }
    /**
     * 功能说明：根据类型accrualName查询计提方式
     *
     * @param accrualName
     */
    public AccrualTypeVO queryAccrualTypeByName(String accrualName) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select t.id, t.accrual_code as accrualCode, t.accrual_name as accrualName, t.frequency_type as frequencyType, t.frequency_daily_date as frequencyDailyDate, ");
        sql.append(" t.rmk as rmk, t.valid_sign as validSign, t.status as status ");
        sql.append(" from cms_accrual_type t where t.status <> -2 and t.accrual_name=:accrualName ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accrualName", accrualName);
        return getBySQLAliasToBean(sql.toString(), map, AccrualTypeVO.class);
    }
    /**
     * 获取所有非删除状态数据和非禁用状态数据
     * 
     * @return
     */
    public List<AccrualTypeVO> listAccrualTypeVO() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.* ");
        sql.append(" from cms_accrual_type t ");
        sql.append(" where 1=1 and t.status <> :status and t.valid_sign = :validSign ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", BaseDataConstant.STATUS_DELETE);
        params.put("validSign", CommonConstant.YES);
        return this.listBySQLAliasToBean(sql.toString(), params, AccrualTypeVO.class);
    }
}
