package com.bytter.letter.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.entity.GuaGuaranteeInfo;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeInfoDao
 * @Description:
 * @date 2019年1月7日 下午3:53:24
 */
@Repository
public class GuaGuaranteeInfoDao extends BaseDao<GuaGuaranteeInfo, String> {
    /**
     * 获取保函申请列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput LgDatatable(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.GUARANTEE_NO AS guaranteeNo,B.GUARANTEE_TYPE AS guaranteeType,");
        sql.append("B.CORP_ID AS corpId,B.OPEN_CORP_TYPE AS openCorpType,B.OPEN_WAY AS openWay,B.OPEN_TYPE AS openType,");
        sql.append("B.OPEN_CORP AS openCorp,B.INCOME_CORP AS incomeCorp,B.CUR_ID AS curId,B.BANK_NAME AS openCorpName,");
        sql.append("B.GUARANTEE_MONEY AS guaranteeMoney,B.GUARANTEE_ATTR AS guaranteeAttr,B.GUA_ITEM AS guaItem,");
        sql.append("B.EXPECT_END_DATE AS expectEndDate,B.START_DATE AS startDate,B.END_DATE AS endDate,");
        sql.append("B.TURN_BANK AS turnBank,B.STATUS AS status,B.ATTACHMENT_ID AS attachmentId,B.HANDLE_RATE AS handleRate,");
        sql.append("B.HANDLE_CHARGE AS handleCharge,B.BOND_PERCENT AS bondPercent,B.BOND_MONEY AS bondMoney,");
        sql.append("B.BOND_ACC AS bondAcc,B.CREATE_DATE AS createDate,B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,");
        sql.append("B.UPDATE_BY AS updateBy,b.corp_name as corpName,b.cur_name as curName FROM GUA_GUARANTEE_INFO B ");
        sql.append("WHERE 1=1 AND B.STATUS in(0,11,-1) ");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "b.CORP_ID"));
        }
        return getDynamicBySQL(input, sql.toString(), GuaGuaranteeInfoVO.class, "", false);
    }
    /**
     * 根据id查询保函申请数据
     */
    public GuaGuaranteeInfoVO getLgInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, String> params = new HashMap<String, String>();
        sql.append(" select b.id as id,b.guarantee_no as guaranteeNo,b.guarantee_type as guaranteeType,").append(
                " b.corp_id as corpId,b.open_corp as openCorp,b.BANK_NAME AS openCorpName,b.open_way as openWay,")
                .append(" b.open_type as openType,b.open_corp_type as openCorpType,b.income_corp as incomeCorp,b.cur_id as curId,")
                .append(" b.guarantee_money as guaranteeMoney,b.guarantee_attr as guaranteeAttr,b.gua_item as guaItem,")
                .append(" b.start_Date as startDate,b.end_date as endDate,b.expect_end_date as expectEndDate,b.status as status, ")
                .append(" b.turn_Bank as turnBank,b.attachment_Id as attachmentId,b.bond_money as bondMoney,b.logout_date as logoutDate, ")
                .append(" b.ratmodel as ratmodel,b.receiver as receiver,b.receive_date as receiveDate,B.UPDATE_BY AS updateBy, ")
                .append(" B.IS_HOOK_BOND AS isHookBond,b.IS_RMB_RAT as isRmbRat,b.CORP_NAME as corpName,b.CUR_NAME AS curName, ")
                .append(" B.BOND_CURID  AS bondCurId,B.BOND_CONTRACT AS bondContract,b.bond_acc  as bondAcc ,b.bond_rate as bondRate,b.is_open_rat isOpenRat ")
                .append(" from gua_guarantee_info b ")
                .append(" where b.id=:id");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaGuaranteeInfoVO.class);
    }
    /**
     * 根据保函编号查询保函申请
     */
    public List<GuaGuaranteeInfoVO> queryLgByNo(String guaranteeNo) {
        StringBuilder sql = new StringBuilder();
        Map<String, String> params = new HashMap<String, String>();
        sql.append(" select b.id as id,b.guarantee_no as guaranteeNo,b.guarantee_type as guaranteeType,").append(
                " b.corp_id as corpId,b.open_corp as openCorp,b.BANK_NAME AS openCorpName,b.open_way as openWay,")
                .append(" b.open_type as openType,b.open_corp_type as openCorpType,b.income_corp as incomeCorp,b.cur_id as curId,")
                //TODO 补充名称相关信息
                .append(" b.CORP_NAME as corpName,b.CUR_NAME AS curName,")
                .append(" b.guarantee_money as guaranteeMoney,b.guarantee_attr as guaranteeAttr,b.gua_item as guaItem,")
                .append(" b.start_Date as startDate,b.end_date as endDate,b.expect_end_date as expectEndDate,b.status as status, ")
                .append(" b.turn_Bank as turnBank,b.attachment_Id as attachmentId  ")
//                .append(" from gua_guarantee_info b ,BT_INPUT_BANK_INFO E where B.OPEN_CORP=E.SYS_BANK_CODE ")
                .append(" from gua_guarantee_info b where ")
                .append(" b.guarantee_No=:guaranteeNo");
        params.put("guaranteeNo", guaranteeNo);
        return listBySQLAliasToBean(sql.toString(), params, GuaGuaranteeInfoVO.class);
    }
    /**
     * 根据保函申请单位和保函申请编号查询保函申请
     */
    public List<GuaGuaranteeInfoVO> queryLgByCorpId(GuaGuaranteeInfoVO infoVO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select b.id as id,b.guarantee_no as guaranteeNo,b.guarantee_type as guaranteeType,")
           .append(" b.corp_id as corpId,b.open_corp as openCorp,b.BANK_NAME AS openCorpName,b.open_way as openWay,")
           .append(" b.open_type as openType,b.open_corp_type as openCorpType,b.income_corp as incomeCorp,b.cur_id as curId,")
           .append(" b.guarantee_money as guaranteeMoney,b.guarantee_attr as guaranteeAttr,b.gua_item as guaItem,")
           .append(" b.start_Date as startDate,b.end_date as endDate,b.expect_end_date as expectEndDate,b.status as status, ")
           .append(" b.turn_Bank as turnBank,b.attachment_Id as attachmentId,b.CORP_NAME as corpName,b.CUR_NAME AS curName ")
           .append(" from gua_guarantee_info b where (b.end_date>=:nowdate or b.expect_end_date>=:nowdate) and b.status in (96) and b.corp_id=:corpId ");
        params.put("nowdate", new java.sql.Date(System.currentTimeMillis()));
        params.put("corpId", infoVO.getCorpId());
        if (StringUtils.isNotEmpty(infoVO.getGuaranteeNo())) {
            sql.append(" or  b.guarantee_no=:guaranteeNo ");
            params.put("guaranteeNo", infoVO.getGuaranteeNo());
        }
        return listBySQLAliasToBean(sql.toString(), params, GuaGuaranteeInfoVO.class);
    }
    /**
     * 获取保函申请待审批列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getApproveDataTables(DataTablesInput input, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.GUARANTEE_NO AS guaranteeNo,B.GUARANTEE_TYPE AS guaranteeType,");
        sql.append(
                "B.CORP_ID AS corpId,B.OPEN_CORP_TYPE AS openCorpType,B.OPEN_WAY AS openWay,B.OPEN_TYPE AS openType,");
        sql.append(
                "B.OPEN_CORP AS openCorp,B.INCOME_CORP AS incomeCorp,B.CUR_ID AS curId,B.BANK_NAME AS openCorpName,");
        //TODO 补充名称相关信息
        sql.append("B.CORP_NAME as corpName,B.CUR_NAME AS curName,");
        sql.append("B.GUARANTEE_MONEY AS guaranteeMoney,B.GUARANTEE_ATTR AS guaranteeAttr,B.GUA_ITEM AS guaItem,");
        sql.append("B.EXPECT_END_DATE AS expectEndDate,B.START_DATE AS startDate,B.END_DATE AS endDate,");
        sql.append(
                "B.TURN_BANK AS turnBank,B.STATUS AS status,B.ATTACHMENT_ID AS attachmentId,B.HANDLE_RATE AS handleRate,");
        sql.append("B.HANDLE_CHARGE AS handleCharge,B.BOND_PERCENT AS bondPercent,B.BOND_MONEY AS bondMoney,");
        sql.append(
                "B.BOND_ACC AS bondAcc,B.CREATE_DATE AS createDate,B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,");
//        sql.append("B.UPDATE_BY AS	updateBy  FROM GUA_GUARANTEE_INFO B LEFT JOIN SYS_CORP C ON B.CORP_ID=C.ID ");
//        sql.append(
//                " LEFT JOIN BT_CURRENCY D ON B.CUR_ID=D.ID LEFT JOIN BT_INPUT_BANK_INFO E ON B.OPEN_CORP=E.SYS_BANK_CODE WHERE 1=1 ");
        sql.append("B.UPDATE_BY AS	updateBy  FROM GUA_GUARANTEE_INFO B WHERE 1=1 ");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" AND B.STATUS =11");
        }
        if (EmptyUtils.isNotEmpty(businessSql)){
            sql.append(" AND ").append(businessSql);
        }
        return getDynamicBySQL(input, sql.toString(), GuaGuaranteeInfoVO.class, "", false);
    }
    /**
     * 获取保函到期列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput logoutDatatable(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.GUARANTEE_NO AS guaranteeNo,B.GUARANTEE_TYPE AS guaranteeType,");
        sql.append("B.CORP_ID AS corpId,B.OPEN_CORP_TYPE AS openCorpType,B.OPEN_WAY AS openWay,B.OPEN_TYPE AS openType,");
        sql.append("B.OPEN_CORP AS openCorp,B.INCOME_CORP AS incomeCorp,B.CUR_ID AS curId,B.BANK_NAME AS openCorpName,");
        sql.append("B.GUARANTEE_MONEY AS guaranteeMoney,B.GUARANTEE_ATTR AS guaranteeAttr,B.GUA_ITEM AS guaItem,");
        sql.append("B.EXPECT_END_DATE AS expectEndDate,B.START_DATE AS startDate,B.END_DATE AS endDate,");
        sql.append("B.TURN_BANK AS turnBank,B.STATUS AS status,B.ATTACHMENT_ID AS attachmentId,B.HANDLE_RATE AS handleRate,");
        sql.append("B.HANDLE_CHARGE AS handleCharge,B.BOND_PERCENT AS bondPercent,B.BOND_MONEY AS bondMoney,B.corp_name as corpName,B.cur_name as curName,");
        sql.append("B.BOND_ACC AS bondAcc,B.CREATE_DATE AS createDate,B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,");
        sql.append("B.UPDATE_BY AS	updateBy  FROM GUA_GUARANTEE_INFO B WHERE 1=1 ");
        sql.append("AND B.STATUS in ( ");
        sql.append(LetterConstant.GUARANTEE_RECEIVED).append(",").append(LetterConstant.GUARANTEE_REGISTERED);
        sql.append(") ");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "b.CORP_ID"));
        }
        return getDynamicBySQL(input, sql.toString(), GuaGuaranteeInfoVO.class, "", false);
    }
    /**
     * 保函查询列表
     *
     * @param input
     * @param corpAuth
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput loadAllDatatable(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select id,guarantee_no as guaranteeNo,guarantee_type as guaranteeType,corp_id as corpId,");
        sql.append("open_corp_type as openCorpType,open_way as openWay,open_type as openType,open_corp as openCorp,");
        sql.append("income_corp as incomeCorp,cur_id as curId,guarantee_money as guaranteeMoney,guarantee_attr as guaranteeAttr,");
        sql.append("start_date as startDate,end_date as endDate,bond_money as bondMoney,bond_acc as bondAcc,");
        sql.append("attachment_id as attachmentId,logout_date as logoutDate,bank_name AS openCorpName,status,receiver as receiver,receive_date as  receiveDate, ");
        sql.append(" b.gua_item as guaItem,b.contract_no as contractNo,b.handle_charge as handleCharge,b.cur_name as curName,b.corp_name as corpName ");
        sql.append("from (");
        sql.append("select gua.id,gua.guarantee_no,gua.guarantee_type,gua.corp_id,");
        sql.append("gua.open_corp_type,gua.open_way,gua.open_type,gua.open_corp,");
        sql.append("gua.income_corp,gua.cur_id,gua.guarantee_money,gua.guarantee_attr,");
        sql.append("gua.start_date,gua.end_date,gua.bond_money,gua.bond_acc,");
        sql.append("gua.attachment_id,gua.logout_date,gua.gua_item,gua.bank_name,f.contract_no, ");
        sql.append("(case when F.Status='98' then F.Status else gua.status end) as status,");
        sql.append("gua.receiver,gua.receive_date,gua.handle_charge,gua.corp_name,gua.cur_name ");
        sql.append("from GUA_GUARANTEE_INFO gua left join GUA_PERFORM_REG F ON gua.Id=F.Guara_Id ");
        sql.append(") b where 1=1 ");
        sql.append("and status <>").append(LetterConstant.STATUS_LOGOUT);
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "b.corp_id"));
        }
        return getDynamicBySQL(input, sql.toString(), GuaGuaranteeInfoVO.class, "", false);
    }
    /**
     * 查询快到期的保函信息
     *
     * @return
     */
    public List<GuaGuaranteeInfo> getGuaGuaranteeMature(Date matureDate) {
        StringBuilder hql = new StringBuilder();
        hql.append("select * from GuaGuaranteeInfo info where info.status in (95,96,97,103	) ");
        hql.append("and (info.endDate<=:matureDate or info.expectEndDate<=:matureDate)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("matureDate", matureDate);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String status) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(*) from GUA_GUARANTEE_INFO  where corp_id=:corpId ");
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in (:status) ");
            params.put("status", status.split(","));
        } else {
            sql.append(" and status<>-2 ");
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
    public BigDecimal queryGuaranteeUseRatMoney(String billId, Date calEndDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("  select gua.guarantee_money as money ")
                .append(" from gua_guarantee_info gua ")
                .append("  where gua.status in(11,95,96,97,98,103)  ")
                // .append(" and gua.status < 100")
                .append(" and gua.id = :billId ")
                .append(" and (gua.end_date <=:calEndDate or ")
                .append(" gua.expect_end_date <=:calEndDate ) ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billId", billId);
        params.put("calEndDate", calEndDate);
        Map<String, Object> result = getBySQL(sql.toString(), params);
        BigDecimal calReleaseMoney = BigDecimal.ZERO;
        if (null != result && null != result.get("MONEY")) {
            calReleaseMoney = new BigDecimal(result.get("MONEY").toString());
        }
        return calReleaseMoney;
    }
}
