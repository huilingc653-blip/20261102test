package com.bytter.finance.domain.equity.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.equity.entity.CmsEquityRepoPlan;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanVO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 股权融资回购计划登记Dao
 * @Description: 股权融资回购计划登记
 * @date 2022-06-29
 */
@Repository
public class CmsEquityRepoPlanDao extends BaseDao<CmsEquityRepoPlan, String> {
    /**
     * 根据计划主表id或融资编号获取信息
     */
    public CmsEquityRepoPlanVO getRepoPlanByEquityCodeOrId(String id, String equityCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        //TODO 补充单位名称
        sql.append("select pp.*,einfo.corp_name as corpName ");
        sql.append(" from CMS_EQUITY_REPO_PLAN pp ");
        sql.append(" left join CMS_EQUITY_INFO_DETAIL eid on eid.EQUITY_CODE=pp.EQUITY_CODE");
        sql.append(" left join CMS_EQUITY_INFO einfo on einfo.id=eid.EQUITY_ID ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and pp.id =:id");
            params.put("id", id);
        }
        if (StringUtils.isNotEmpty(equityCode)) {
            sql.append(" and pp.equity_Code =:equityCode");
            params.put("equityCode", equityCode);
        }
        return getBySQLAliasToBean(sql.toString(), params, CmsEquityRepoPlanVO.class);
    }
    /**
     * 功能介绍 : 股权融资列表数据查询（计划登记查询）
     */
    public DataTablesOutput getDataEquityRepoPlanTable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select einfo.id,einfo.EQUITY_TYPE_CODE as equityTypeCode,t.TYPE_NAME AS equityTypeName,einfo.ISDOMESTIC,einfo.CORP_ID as corpId,einfo.CUR_ID as curId, ");
        sql.append(" einfo.MONEY,einfo.ACCOUNT,einfo.ACC_NAME as accName,einfo.BANK_NAME as bankName,einfo.FINANCING_DATE as financingDate,einfo.cur_name as curName,einfo.corp_name as corpName,");
        sql.append(" einfo.DELIVERY_DATE as deliveryDate,einfo.FINANCING_USE as financingUse,einfo.RMK,einfo.UPLOAD_ID as uploadId,einfo.status");
        sql.append(" from CMS_EQUITY_INFO einfo ");
        sql.append(" left join CMS_EQUITY_TYPES t on einfo.EQUITY_TYPE_CODE=t.TYPE_CODE");
        sql.append(" where 1=1 ");
        sql.append(" and einfo.status in (:status95)");
        sql.append("  and exists (select 1 from CMS_EQUITY_INFO_DETAIL eid where eid.equity_id=einfo.id and eid.biz_status in (:bizStatus)) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "einfo.corp_id"));
        OrderString orderString = new OrderString("einfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status95", CmsConstant.APPROVED);
        params.put("bizStatus", new String[] { CmsConstant.EQUITY_DETAIL_BIZSTATUS_30,
                CmsConstant.EQUITY_DETAIL_BIZSTATUS_31, CmsConstant.EQUITY_DETAIL_BIZSTATUS_35 });
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsEquityInfoVO.class, null, false, params);
    }
    /**
     * 功能介绍 : 股权融资列表数据查询(计划修改查询)
     */
    public DataTablesOutput getDataEquityRepoPlanUpdateTable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select einfo.id,einfo.EQUITY_TYPE_CODE as equityTypeCode,t.TYPE_NAME AS equityTypeName,einfo.ISDOMESTIC,einfo.CORP_ID as corpId,einfo.CUR_ID as curId, ");
        //TODO 补充单位名称
        sql.append(" einfo.cur_name as curName,einfo.corp_name as corpName, ");
        sql.append(
                " einfo.MONEY,einfo.ACCOUNT,einfo.ACC_NAME as accName,einfo.BANK_NAME as bankName,einfo.FINANCING_DATE as financingDate,");
        sql.append(
                " einfo.DELIVERY_DATE as deliveryDate,einfo.FINANCING_USE as financingUse,einfo.RMK,einfo.UPLOAD_ID as uploadId,einfo.status");
        sql.append(" from CMS_EQUITY_INFO einfo ");
        sql.append(" left join CMS_EQUITY_TYPES t on einfo.EQUITY_TYPE_CODE=t.TYPE_CODE");
        sql.append(" where 1=1 ");
        sql.append(" and einfo.status in (:status95)");
        sql.append(
                "  and exists (select 1 from CMS_EQUITY_INFO_DETAIL eid where eid.equity_id=einfo.id and eid.biz_status in (:bizStatus)) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "einfo.corp_id"));
        OrderString orderString = new OrderString("einfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status95", CmsConstant.APPROVED);
        params.put("bizStatus",
                new String[] { CmsConstant.EQUITY_DETAIL_BIZSTATUS_40, CmsConstant.EQUITY_DETAIL_BIZSTATUS_41,
                        CmsConstant.EQUITY_DETAIL_BIZSTATUS_45, CmsConstant.EQUITY_DETAIL_BIZSTATUS_50,
                        CmsConstant.EQUITY_DETAIL_BIZSTATUS_56 });
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsEquityInfoVO.class, null, false, params);
    }
    /**
     * 功能介绍 : 股权融资列表数据查询
     */
    public DataTablesOutput getDatatableApprove(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        //TODO 补充单位名称
        sql.append(
                "select pp.*, einfo.corp_name as corpName, eid.ASSET_NAME as assetName,eid.financing_Way as financingWay,eid.inv_Money as invMoney,eid.equity_Pro as equityPro ");
        sql.append(" from CMS_EQUITY_REPO_PLAN pp ");
        sql.append(" left join CMS_EQUITY_INFO_DETAIL eid on eid.EQUITY_CODE=pp.EQUITY_CODE");
        sql.append(" left join CMS_EQUITY_INFO einfo on einfo.id=eid.EQUITY_ID ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "pp.id")).append(" ");
        }
        OrderString orderString = new OrderString("pp.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsEquityRepoPlanVO.class, null, false);
    }
}
