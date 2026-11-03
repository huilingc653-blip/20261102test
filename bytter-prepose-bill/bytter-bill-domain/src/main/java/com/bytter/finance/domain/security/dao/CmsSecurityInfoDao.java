package com.bytter.finance.domain.security.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.security.entity.CmsSecurityInfo;
import com.bytter.finance.domain.security.vo.CmsSecurityInfoVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CmsSecurityInfoDao extends BaseDao<CmsSecurityInfo, String> {
    /**
     * 功能介绍 : 产证券化登记主信息查询
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput, List<String> corpAuth, String flag) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select info.id,info.SECURITY_TYPE_ID as securityTypeId,type.TYPE_NAME as securityTypeName,info.SECURITY_CODE as securityCode,");
        sql.append(
                " info.SECURITY_SNAME as securitySname,info.SECURITY_NAME as securityName,info.CUR_ID as curId,info.CORP_ID as corpId,info.MONEY,info.Status,");
        //TODO 补充名称相关信息
        sql.append(" info.CORP_NAME as corpName,info.CUR_NAME as curName ");
        sql.append(" from CMS_SECURITY_INFO info ");
        sql.append(" left join CMS_SECURITY_TYPE type on info.SECURITY_TYPE_ID=type.id ");
        sql.append(" where 1=1 ");
        if (StringUtils.isEmpty(flag)) {// 标记为空则是未完成的，为query的为发行查询的，则是登记完成的数据
            sql.append(" and info.status in (-1,0,11)");
        } else {
            sql.append(" and info.status = 95");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        OrderString orderString = new OrderString("info.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsSecurityInfoVO.class, null, false);
    }
    /**
     * 债券注册审批列表数据
     *
     * @param input
     *            前端查询参数
     * @param businessIdList
     *            业务id集合
     * @return 债券注册审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessIdList) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select info.id,info.SECURITY_TYPE_ID as securityTypeId,type.TYPE_NAME as securityTypeName,info.SECURITY_CODE as securityCode,");
        sql.append(
                " info.SECURITY_SNAME as securitySname,info.SECURITY_NAME as securityName,info.CUR_ID as curId,info.CORP_ID as corpId,info.MONEY,info.Status,");
        //TODO 补充名称相关信息
        sql.append(" info.CORP_NAME as corpName,info.CUR_NAME as curName ");
        sql.append(" from CMS_SECURITY_INFO info ");
        sql.append(" left join CMS_SECURITY_TYPE type on info.SECURITY_TYPE_ID=type.id ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(businessIdList) && businessIdList.size() > 0) {
            String businessSql = SqlUtil.getInSqlByParams(businessIdList, "info.id", params);
            sql.append(" and ").append(businessSql).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsSecurityInfoVO.class, "", false, null, params);
    }
    /**
     * 功能介绍 : 发行主页面产证券化登记主信息审批通过数据
     */
    public DataTablesOutput getDataTableForissue(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select info.id,info.SECURITY_TYPE_ID as securityTypeId,type.TYPE_NAME as securityTypeName,info.SECURITY_CODE as securityCode,");
        sql.append(
                " info.SECURITY_SNAME as securitySname,info.SECURITY_NAME as securityName,info.CUR_ID as curId,info.CORP_ID as corpId,info.MONEY,info.Status,");
        //TODO 补充名称相关信息
        sql.append(" info.CORP_NAME as corpName,info.CUR_NAME as curName ");
        sql.append(" from CMS_SECURITY_INFO info ");
        sql.append(" left join CMS_SECURITY_TYPE type on info.SECURITY_TYPE_ID=type.id ");
        sql.append(" where 1=1 ");
        sql.append(" and info.status = :status");
        // sql.append(" and not exists (select 1 from CMS_SECURITY_ISSUE iss where iss.SECURITY_INFO_ID=info.id and
        // iss.biz_status not in (:bizStatus) ) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        OrderString orderString = new OrderString("info.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", CmsConstant.APPROVED);
        // params.put("bizStatus", bizStatus);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsSecurityInfoVO.class, null, false, params);
    }
    /**
     * 根据id获取详情数据
     *
     * @param id
     *            id
     * @return 债券注册vo
     */
    public CmsSecurityInfoVO getInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_SECURITY_INFO");
        sql.append(" where 1 = 1 and id = :id  ");
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, CmsSecurityInfoVO.class);
    }
}
