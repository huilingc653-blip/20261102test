package com.bytter.system.dao;
import com.bytter.system.entity.BlackWhiteList;
import com.bytter.system.api.constant.SysBlackWhiteConstant;
import com.bytter.system.vo.BlackWhiteListVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class BlackWhiteListDao extends BaseDao<BlackWhiteList, String> {
    private StringBuilder initSql() {
        return new StringBuilder().append("select ")
                .append(" ss.id as id,ss.CORP_ID as corpId,ss.CODE as code,ss.name as name,ss.bw_Type as bwType,")
                .append(" (case when ss.corp_id IS NOT NULL then BT_CONCAT4('(', s.code, ')', s.name) else null end ) as corpCodeName,")
                .append(" ss.valid_Sign as validSign,ss.list_type as listType,ss.IS_COMMON as isCommon,")
                .append(" ss.DATA_SOURCES as dataSources,ss.country_id as countryId,ss.EXTERNAL_ID as externalId,")
                .append(" ss.RMK as rmk,sc.name_zh as countryName")
                .append(" from SYS_BLACK_WHITE_LIST ss ")
                .append(" left join sys_corp s on ss.corp_id = s.id ")
                .append(" left join sys_country_cust sc on ss.country_id = sc.id ")
                .append(" WHERE 1=1 ");
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BlackWhiteListVO> listObject(DataTablesInput input) {
        Map<String, Object> params = new HashMap<>();
        StringBuffer sql = new StringBuffer()
                .append(this.initSql())
                .append("and ss.valid_sign in (:validSign)");
        params.put("validSign", new String[]{AccebillConstant.AB_BILL_VALID_SIGN_1, AccebillConstant.AB_BILL_VALID_SIGN_0});
//        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "s.id", params);
//        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), BlackWhiteListVO.class, "", false, params);
    }
    public BlackWhiteListVO getBlackWhiteListVO(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(this.initSql());
        sql.append(" and ss.id =:ssId");
        params.put("ssId", id);
        List<BlackWhiteListVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, BlackWhiteListVO.class);
        return ListUtils.isNotNullOrEmpty(listInfo) ? listInfo.get(0) : null;
    }
    public int blackWithDel(Object ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = " update BlackWhiteList set validSign =:validSign where id in (:id)";
        params.put("id", ids);
        params.put("validSign", AccebillConstant.AB_BILL_OPT_STATUS_2);
        return this.batchExecute(hql, params);
    }
    /**
     * 通过map参数获黑白名单列表数据
     *
     * @param params
     * @return
     */
    public List<BlackWhiteList> getBlackWhiteListByParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder hql = new StringBuilder("from BlackWhiteList where validSign!=:validSign");
        params.put("validSign", SysBlackWhiteConstant.SYS_BLACK_WHITE_VALID_SIGN_2);
        if (params.containsKey("externalId")) {
            hql.append(" and externalId=:externalId ");
        }
        if (params.containsKey("externalIdNot")) {
            hql.append(" and externalId!=:externalIdNot ");
        }
        if (params.containsKey("name")) {
            hql.append(" and name=:name ");
        }
        if (params.containsKey("corpId")) {
            String corpId = params.get("corpId").toString();
            if (corpId.indexOf(",") > 0) {
                String[] corpIds = corpId.split(",");
                hql.append(" and (corpId is null or ");
                hql.append(SqlUtil.getInSqlByParams(corpIds, "corpId", params));
                hql.append(" ) ");
            } else {
                hql.append(" and (corpId is null or corpId=:corpId) ");
            }
        }
        if (params.containsKey("bwType")) {
            hql.append(" and bwType=:bwType ");
        }
        if (params.containsKey("listType")) {
            hql.append(" and listType=:listType ");
        }
        return this.listByHQL(hql.toString(), params);
    }
    /**
     * 根据外部ID删除黑白名单信息（逻辑删除validSign=-2）
     *
     * @param externalId
     */
    public void deleteBlackWhiteListByExternalId(String externalId, ICurrentUser user) {
        if (StringUtils.isEmpty(externalId)) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("update SYS_BLACK_WHITE_LIST ");
        sql.append("set valid_sign=:validSign ");
        sql.append(",update_time=:updateTime ");
        if (user != null) {
            sql.append(",update_id=:updateId ");
            params.put("updateId", user.getId());
        }
        sql.append(" where valid_sign!=:validSign and external_id=:externalId ");
        params.put("validSign", SysBlackWhiteConstant.SYS_BLACK_WHITE_VALID_SIGN_2);
        params.put("updateTime", new Date());
        params.put("externalId", externalId);
        this.batchSQLExecute(sql.toString(), params);
    }
    public boolean repeatFegin(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder(" from BlackWhiteList t where 1=1")
                .append(" and t.name =:name ")
                .append(" and t.validSign in (:validSign) ");
        params.put("validSign", new String[]{AccebillConstant.AB_BILL_VALID_SIGN_1, AccebillConstant.AB_BILL_VALID_SIGN_0});
        if (params.containsKey("id")) {
            hql.append(" and t.id <>:id ");
        }
        @SuppressWarnings("unchecked")
        List<BlackWhiteList> list = this.listByHQL(hql.toString(), params);
        return ListUtils.isNotNullOrEmpty(list) ? true : false;
    }
}
