package com.bytter.guarantee.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.domain.entity.ConType;
import com.bytter.guarantee.domain.vo.ConTypeVO;
import com.bytter.guarantee.enums.PermissionEnum;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @Title: 合同类型VO
 * @Package com.bytter.biz.contract.dao
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-5 下午6:12:52
 * @version V1.0
 */
@Repository
public class ConTypeDao extends BaseDao<ConType, String> {
    public DataTablesOutput listContractTypeDate(DataTablesInput input, List<String> corpAuthList) {
        String isCommon = (String) input.getFormdata().get("isCommon");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ct.id as id,");
        sql.append(" ct.corp_Name as corpName,ct.corp_code as corpCode,");
        sql.append(" ct.con_code as typeCode,");
        sql.append(" ct.con_name as name,");
        sql.append(" ct.rmk as rmk,");
        sql.append(" ct.valid_sign as validSign,");
        sql.append(" ct.share_sign as shareSign,");
        sql.append(" ct.corp_id as corpId,");
        sql.append("  bt_nvl((select count(ci.id) from CON_INFO ci where ci.type_id=ct.id),0) as delFlag");
        sql.append(" from CON_TYPE ct");
        sql.append(" where 1=1");
        if (!StringUtils.isBlank(isCommon)) {
            if (isCommon.equals(PermissionEnum.all.name()) || isCommon.equals(PermissionEnum.common.name())) {
                sql.append(" and ct.share_sign = " + ConConstant.CONTRACT_TYPE_COMMON_SHARESIGN);
            } else {
                sql.append(" and ct.share_sign = " + ConConstant.CONTRACT_TYPE_CORP_SHARESIGN);
                String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "ct.corp_id", params);
                sql.append(" and ").append(corpAuthSql).append(" ");
            }
        }
        return this.getDynamicBySQL(input, sql.toString(), ConTypeVO.class, "ct", false, params);
    }
    /**
     * 功能介绍 : 根据查询条件获取合同类型<br>
     * <b>日期：</b>2019年11月08日<br>
     *
     * @author zhengg
     */
    public List<ConTypeVO> getAllConType(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select ct.id,ct.CON_CODE as typeCode,ct.CON_NAME as typeName,ct.corp_id as corpId,ct.SHARE_SIGN as shareSign,ct.RMK from CON_TYPE ct ");
        sql.append(" where 1=1 ");
        if (params.containsKey("validSign")) {
            // 针对已失效合同类型起草和登记暂存的处理
            if (params.containsKey("typeIdVal")) {
                sql.append(" and (ct.VALID_SIGN=:validSign or ct.id=:typeIdVal )");
            } else {
                sql.append(" and ct.VALID_SIGN=:validSign");
            }
        }
        if (params.containsKey("corpId")) {
            String[] corpIds = StringUtils.split(params.get("corpId").toString(), ",");
            String corpSql = SqlUtil.getInSqlByParams(corpIds, "ct.corp_id", "paramsCorpId", params);
            sql.append(" and (ct.corp_Id is null or ct.corp_Id = '' or ").append(corpSql).append(" ) ");
        }
        if (params.containsKey("pdCode")) {
            sql.append(" and exists(select 1 from con_pd_map cpm where cpm.type_id=ct.id and cpm.pd_code=:pdCode ) ");
        }
        if (params.containsKey("corpAuthList")) {
            @SuppressWarnings("unchecked")
            List<String> corpAuthList = (List<String>) params.get("corpAuthList");
            if (ListUtils.isNullOrEmpty(corpAuthList)) {
                sql.append(" and 1!=1 ");
            } else {
                String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "ct.corp_id", params);
                sql.append(" and (ct.corp_Id is null or ct.corp_Id = '' or ").append(corpAuthSql).append(" ) ");
            }
        }
        sql.append(" order by ct.CON_NAME ");
        return listBySQLAliasToBean(sql.toString(), params, ConTypeVO.class);
    }
    /**
     *
     * @Title: queryConTractIsExist @Description: TODO(查询合同代码是否已经存在) @param @param conCode @param @return 参数 @return
     *         boolean 返回类型 @throws
     */
    public boolean queryConTractIsExist(String contractCode, String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<String, Object>();
        sql.append(" from ConType t where 1=1  ");
        if (!StringUtils.isBlank(contractCode)) {
            sql.append(" and t.typeCode=:typeCode");
            map.put("typeCode", contractCode);
        }
        if (!StringUtils.isBlank(id)) {
            sql.append(" and t.id <>:id");
            map.put("id", id);
        }
        Query query = createQuery(sql.toString(), map);
        return null != query && query.list().size() > 0;
    }
    public void delConTypeById(String id) {
        createSQLQuery("delete from CON_TYPE where id=?", id).executeUpdate();
    }
    public ConTypeVO getConTypeVOByID(String id) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select ct.id as id,");
        hql.append(" ct.corp_name as corpName,ct.corp_code as corpCode,");
        hql.append(" ct.con_code as typeCode,");
        hql.append(" ct.con_name as name,");
        hql.append(" ct.rmk as rmk,");
        hql.append(" ct.valid_sign as validSign,");
        hql.append(" ct.share_sign as shareSign,");
        hql.append(" ct.corp_id as corpId");
        hql.append(" from CON_TYPE ct");
        hql.append(" where ct.id=:id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        List<ConTypeVO> list = listBySQLAliasToBean(hql.toString(), params, ConTypeVO.class);
        return null != list && list.size() > 0 ? list.get(0) : null;
    }
    /**
     *
     * @param parmsList
     * @return
     */
    public boolean vnGtOccupy(List<String> parmsList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) from CON_INFO t where t.type_id in (:refIds)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("refIds", parmsList);
        return ((Number) this.createSQLQuery(sql.toString(), params).uniqueResult()).intValue() > 0;
    }
}
