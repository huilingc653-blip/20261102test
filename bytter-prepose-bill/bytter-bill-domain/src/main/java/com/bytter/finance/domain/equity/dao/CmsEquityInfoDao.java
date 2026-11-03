package com.bytter.finance.domain.equity.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.equity.entity.CmsEquityInfo;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Dao
 * @Description: CMS_EQUITY_INFO
 * @date 2022-06-08
 */
@Repository
public class CmsEquityInfoDao extends BaseDao<CmsEquityInfo, String> {
    /**
     * 根据id获取信息
     */
    public CmsEquityInfoVO getById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select einfo.* ");
        sql.append(" from CMS_EQUITY_INFO einfo ");
        sql.append(" where 1=1 ");
        sql.append(" and einfo.id =:id");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, CmsEquityInfoVO.class);
    }
    /**
     * 功能介绍 : 股权融资列表数据查询（审批通过）
     */
    public DataTablesOutput getDataEquityPassTable(DataTablesInput dataTablesInput, List<String> corpAuth,
            List<String> bizStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append("select einfo.id,einfo.EQUITY_TYPE_CODE as equityTypeCode,t.TYPE_NAME AS equityTypeName,einfo.ISDOMESTIC,einfo.CORP_ID as corpId,einfo.CUR_ID as curId, ");
        sql.append(" einfo.MONEY,einfo.ACCOUNT,einfo.ACC_NAME as accName,einfo.BANK_NAME as bankName,einfo.FINANCING_DATE as financingDate,einfo.cur_name as curName,einfo.corp_name as corpName,");
        sql.append(" einfo.DELIVERY_DATE as deliveryDate,einfo.FINANCING_USE as financingUse,einfo.RMK,einfo.UPLOAD_ID as uploadId,einfo.status");
        sql.append(" from CMS_EQUITY_INFO einfo ");
        sql.append(" left join CMS_EQUITY_TYPES t on einfo.EQUITY_TYPE_CODE=t.TYPE_CODE");
        sql.append(" where 1=1 ");
        sql.append(" and einfo.status  in (:status95)");
        sql.append("  and exists (select 1 from CMS_EQUITY_INFO_DETAIL eid where eid.equity_id=einfo.id and eid.biz_status in (:bizStatus)) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "einfo.corp_id"));
        OrderString orderString = new OrderString("einfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status95", CmsConstant.APPROVED);
        params.put("bizStatus", bizStatus);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsEquityInfoVO.class, null, false, params);
    }
    /**
     * 功能介绍 : 股权融资列表数据查询
     */
    public DataTablesOutput getDataEquityTable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select einfo.id,einfo.EQUITY_TYPE_CODE as equityTypeCode,t.TYPE_NAME AS equityTypeName,einfo.ISDOMESTIC,einfo.CORP_ID as corpId,einfo.CUR_ID as curId, ");
        //TODO 补充名称信息
        sql.append(" einfo.cur_name as curName,einfo.corp_name as corpName, ");
        sql.append(" einfo.MONEY,einfo.ACCOUNT,einfo.ACC_NAME as accName,einfo.BANK_NAME as bankName,einfo.FINANCING_DATE as financingDate,");
        sql.append(" einfo.DELIVERY_DATE as deliveryDate,einfo.FINANCING_USE as financingUse,einfo.RMK,einfo.UPLOAD_ID as uploadId,einfo.status");
        sql.append(" from CMS_EQUITY_INFO einfo ");
        sql.append(" left join CMS_EQUITY_TYPES t on einfo.EQUITY_TYPE_CODE=t.TYPE_CODE");
        sql.append(" where 1=1 ");
        sql.append(" and einfo.status not in (-2,95)");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "einfo.corp_id"));
        OrderString orderString = new OrderString("einfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsEquityInfoVO.class, null, false);
    }
    /**
     * 功能介绍 : 股权融资列表数据查询
     */
    public DataTablesOutput getDataEquityQueryTable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select einfo.id,einfo.EQUITY_TYPE_CODE as equityTypeCode,t.TYPE_NAME AS equityTypeName,einfo.ISDOMESTIC,einfo.CORP_ID as corpId,einfo.CUR_ID as curId, ");
        sql.append(" einfo.MONEY,einfo.ACCOUNT,einfo.ACC_NAME as accName,einfo.BANK_NAME as bankName,einfo.FINANCING_DATE as financingDate,einfo.cur_name as curName,einfo.corp_name as corpName,");
        sql.append(" einfo.DELIVERY_DATE as deliveryDate,einfo.FINANCING_USE as financingUse,einfo.RMK,einfo.UPLOAD_ID as uploadId,einfo.status");
        sql.append(" from CMS_EQUITY_INFO einfo ");
        sql.append(" left join CMS_EQUITY_TYPES t on einfo.EQUITY_TYPE_CODE=t.TYPE_CODE");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "einfo.corp_id"));
        OrderString orderString = new OrderString("einfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsEquityInfoVO.class, null, false);
    }
    /**
     * 功能介绍 : 股权融资列表数据查询
     */
    public DataTablesOutput getDatatableApprove(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select einfo.id,einfo.EQUITY_TYPE_CODE as equityTypeCode,t.TYPE_NAME AS equityTypeName,einfo.ISDOMESTIC,einfo.CORP_ID as corpId,einfo.CUR_ID as curId, ");
        //TODO 补充名称信息
        sql.append(" einfo.cur_name as curName,einfo.corp_name as corpName, ");
        sql.append(
                " einfo.MONEY,einfo.ACCOUNT,einfo.ACC_NAME as accName,einfo.BANK_NAME as bankName,einfo.FINANCING_DATE as financingDate,");
        sql.append(
                " einfo.DELIVERY_DATE as deliveryDate,einfo.FINANCING_USE as financingUse,einfo.RMK,einfo.UPLOAD_ID as uploadId,einfo.status");
        sql.append(" from CMS_EQUITY_INFO einfo ");
        sql.append(" left join CMS_EQUITY_TYPES t on einfo.EQUITY_TYPE_CODE=t.TYPE_CODE");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "einfo.id")).append(" ");
        }
        OrderString orderString = new OrderString("einfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsEquityInfoVO.class, null, false);
    }
    /**
     * 功能介绍 : 股权融资明细列表数据查询
     */
    public List<CmsEquityInfoDetailVO> getDataEquityDetailTable(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select det.* ");
        sql.append(" from CMS_EQUITY_INFO_DETAIL det ");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(ids, 900, "det.id"));
        sql.append(" order by det.create_date desc");
        return listBySQLAliasToBean(sql.toString(), null, CmsEquityInfoDetailVO.class);
    }
    /**
     * 功能介绍 :根据融资编号查询股权融资信息
     */
    public CmsEquityInfoVO getCmsEquityInfoByEquityCode(String equityCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select einfo.* ");
        sql.append(" from CMS_EQUITY_INFO einfo,CMS_EQUITY_INFO_DETAIL det ");
        sql.append(" where einfo.id=det.EQUITY_ID and det.EQUITY_CODE=:equityCode");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("equityCode", equityCode);
        List<CmsEquityInfoVO> listInfo = listBySQLAliasToBean(sql.toString(), params, CmsEquityInfoVO.class);
        if (listInfo != null && !listInfo.isEmpty()) {
            return listInfo.get(0);
        }
        return null;
    }
}
