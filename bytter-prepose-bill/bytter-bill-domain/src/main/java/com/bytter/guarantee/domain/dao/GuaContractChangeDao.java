package com.bytter.guarantee.domain.dao;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.entity.GuaContractInfo;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaContractQueryDTO;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * 担保合同变更dao
 *
 * @author yangl
 */
@Repository
public class GuaContractChangeDao extends BaseDao<GuaContractInfo, String> {
    /**
     * 担保合同变更列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<GuaContractInfoVO> queryContractChangeList(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        //TODO 程序未补充名称信息
        sql.append("SELECT B.ID AS id,B.CORP_ID AS corpId,B.GUA_NO AS guaNo,B.GUA_TYPE AS guaType,B.GUA_PERSON AS guaPerson,");
        sql.append("B.DEBTOR_PERSON AS debtorPerson,B.CREDITOR_PERSON AS creditorPerson,B.CUR_ID AS curId,B.GUA_MONEY AS guaMoney,");
        sql.append("B.CONTRACT_CODE AS contractCode,B.START_DATE AS startDate,B.END_DATE AS endDate,B.STATUS AS status,");
        sql.append("B.WARR AS warr,B.RMK AS rmk,B.ATTACHMENT_ID AS attachmentId,B.CREATE_BY AS createBy,B.CREATE_DATE AS createDate,");
        sql.append("B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updateDate,BT_NVL(B.INNER_GUA,'0') AS innerGua, ");
        sql.append("b.cur_name as curName,b.create_name as operator,b.corp_name as corpName ");
        sql.append(" FROM GUA_CONTRACT_INFO B ");
        sql.append(" WHERE (B.STATUS=95 and (B.IS_CHANGE='0' or B.IS_CHANGE='1') or (B.STATUS=11 and B.IS_CHANGE='1')) ");
        if (null != input.getFormdata().get("ins_guaType")) {
            // 担保方式查询条件
            String guaType = input.getFormdata().get("ins_guaType").toString();
            if (StringUtils.isNotEmpty(guaType)) {
                sql.append(" AND B.ID IN (SELECT E.GUA_ID FROM GUA_CONTRACT_WAY E WHERE ");
                sql.append(getSQLIn(Arrays.asList(guaType.split(",")), 900, "E.WAY_CODES") + ") ");
            }
        }
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        }
        return getDynamicBySQL(input, sql.toString(), GuaContractInfoVO.class, "", false);
    }
    /**
     * 查看担保合同历史轨迹
     *
     * @param guaId
     * @return
     */
    public List<GuaConrrantChangeTrackVO> queryGuaContractHistory(String guaId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select t.guaid,t.guano,t.contractcode,t.createtime,t.money,t.pledgeno,t.pledgename,t.pledgemoney,t.startdate,");
        sql.append(" t.enddate,t.tracktype,t.guawayid,d.name as guaWayName,t.pledgeattr,t.conType ");
        sql.append(" from ( ");
        sql.append(
                " select a.gua_id  as guaId, a.gua_no as guaNo,a.contract_code as contractCode,a.create_time   as createTime,");
        sql.append(
                " a.money as money,b.pledge_no as pledgeNo,b.pledge_name as pledgeName,b.pledge_money as pledgeMoney, ");
        sql.append(
                " b.start_date as startDate,b.end_date as endDate,a.track_type as trackType,a.gua_way_id as guaWayId,b.pledge_attr as pledgeAttr,  ");
        sql.append(" case when a.con_type is null then '0' else a.con_type end as conType ");
        sql.append(" from GUA_CONRRANT_CHANGE_TRACK a ");
        sql.append(" left join GUA_PLEDGE_INFO b");
        sql.append(" on a.pledge_id = b.id) t ");
        sql.append(" left join GUA_WAY d on t.guawayid = d.id");
        sql.append("  where guaId =:guaId");
        sql.append(" order by createTime desc ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("guaId", guaId);
        return listBySQLAliasToBean(sql.toString(), map, GuaConrrantChangeTrackVO.class);
    }
    /**
     * @return
     */
    public String getGuaContractTrackSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select guaId,guaNo,contractCode,createTime,money,pledgeNo,pledgeName,pledgeMoney,");
        sql.append("startDate,endDate,trackType,d.name as guaWayName ");
        return sql.toString();
    }
    /**
     * 获取挂接担保的业务id
     *
     * @param sysCode   查询业务种类
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param status    担保状态
     * @return 业务id集合，
     */
    public List<GuaContractOccupDTO> getGua(String sysCode, String startDate, String endDate, String status,String contractId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>(1);
        sql.append("select max(m.use_date) useDate,m.status status,m.business_id as businessId,m.sysCode as sysCode from gua_contract_occup m ");
        sql.append(" where m.sysCode = :sysCode ");
        if (StringUtils.isNotEmpty(startDate)) {
            sql.append(" and m.use_date >= :startDate ");
            params.put("startDate", DateUtil.beginOfDay(DateUtil.parse(startDate,"yyyy-MM-dd")).toJdkDate());
        }
        if (StringUtils.isNotEmpty(endDate)) {
            sql.append(" and :endDate >= m.use_date ");
            params.put("endDate", DateUtil.endOfDay(DateUtil.parse(endDate,"yyyy-MM-dd")).toJdkDate());
        }
        if (StringUtils.isNotEmpty(status)) {
            sql.append(" and m.status = :status ");
            params.put("status", status);
        }
        if(StrUtil.isNotBlank(contractId)){
            sql.append(" and m.contract_id = :contractId ");
            params.put("contractId", contractId);
        }
        sql.append(" group by m.business_id,m.status,m.syscode ");
        params.put("sysCode", sysCode);
        return listBySQLAliasToBean(sql.toString(), params, GuaContractOccupDTO.class);
    }
    /**
     * 内部贷款挂接担保查询
     *
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
    //TODO 未使用且SQL有问题
//    public DataTablesOutput getGuaForloan(DataTablesInput dataTablesInput, List<String> corpIds) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select cli.bill_code as billCode, ");
//        sql.append("	   cli.id as id, ");
//        sql.append("       cli.name as loanGenre, ");
//        sql.append("       cli.contract_code as contractCode, ");
//        sql.append("       cli.cur_code as curCode, ");
//        // sql.append(" cur.cur_name as curName, ");
//        sql.append("       cli.loan_money as loanMoney, ");
//        sql.append("       cli.loan_date as loanDate, ");
//        sql.append("       cli.repay_date as repayDate, ");
//        sql.append("       gco.use_date as useDate, ");
//        sql.append("       gco.status as status,");
//        sql.append("       cli.corp_id as corpId");
//        sql.append(
//                "  from ( select loan.*,lType.name  from  CMS_LOAN_INFO loan left join cms_loan_type lType on loan.loan_genre=lType.code  where lType.type=1) cli ");
//        sql.append(
//                "  inner join  (select max(use_date) use_date,status,business_id,syscode from gua_contract_occup  group by business_id,status,syscode) gco ");
//        sql.append("    on gco.business_id = cli.id and cli.is_hang_guarantee = '1'");
//        sql.append("  left join BT_CURRENCY cur ");
//        sql.append("    on cli.cur_code = cur.id ");
//        sql.append("where gco.status in(" + GuaranteeConstant.GUA_CONTRACT_USED + ")");
//        if (EmptyUtils.isNotEmpty(corpIds)) {
//            sql.append(" and " + getSQLIn(corpIds, 900, "cli.corp_id"));
//        } else {
//            sql.append(" and 1 <> 1 ");
//        }
//        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsLoanVO.class, "gco", false);
//    }
    /**
     * 被担保合同到期提醒
     *
     * @param
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<GuaContractInfoVO> endContracts(List<String> corpAuth, Date startDate, Date endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT B.ID AS id,B.CORP_ID AS corpId,B.GUA_NO AS guaNo,B.GUA_TYPE AS guaType,B.GUA_PERSON AS guaPerson,");
        //TODO 补充相关名称信息
        sql.append("B.CORP_NAME AS corpName,B.CUR_NAME AS curName,");
        sql.append(
                "B.DEBTOR_PERSON AS debtorPerson,B.CREDITOR_PERSON AS creditorPerson,B.CUR_ID AS curId,B.GUA_MONEY AS guaMoney,");
        sql.append(
                "B.CONTRACT_CODE AS contractCode,B.START_DATE AS startDate,B.END_DATE AS endDate,B.STATUS AS status,");
        sql.append(
                "B.WARR AS warr,B.RMK AS rmk,B.ATTACHMENT_ID AS attachmentId,B.CREATE_BY AS createBy,B.CREATE_DATE AS createDate,");
        sql.append("B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updateDate,BT_NVL(B.INNER_GUA,'0') AS innerGua ");
//        sql.append(
//                "FROM GUA_CONTRACT_INFO B LEFT JOIN SYS_CORP C ON B.CORP_ID=C.ID LEFT JOIN BT_CURRENCY D ON B.CUR_ID=D.ID ");
        sql.append(
                "FROM GUA_CONTRACT_INFO B ");
        sql.append("WHERE (B.STATUS=95 and (B.IS_CHANGE='0' or B.IS_CHANGE='1')) ");
        sql.append(" and b.BUSINESS_TYPE = '0' ");// 担保合同类型为被担保合同
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        }
        sql.append(" and (b.end_date between to_date(:startDate, 'yyyy-MM-dd') and to_date(:endDate, 'yyyy-MM-dd')) ");
        Map<String, Object> param = new HashMap<>();
        param.put("startDate", DateUtils.format(startDate, "yyyy-MM-dd"));
        param.put("endDate", DateUtils.format(endDate, "yyyy-MM-dd"));
        return listBySQLAliasToBean(sql.toString(), param, GuaContractInfoVO.class);
    }
    /**
     * 通过业务id及编码查询担保方式
     *
     * @param params
     * @return
     */
    public List<GuaContractQueryDTO> listGuaWayName(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select gco.BUSINESS_ID as businessId, gci.id as guaId, gco.SYSCODE, gco.STATUS, gw.ID as guaWay, gw.NAME as guaWayName, ");
        sql.append(
                " gci.contract_code as contractCode,gci.start_date as startDate, gci.end_date as endDate,gci.gua_money as guaMoney, ");
        sql.append(" gci.rmk as guaRmk ");
        sql.append(" from gua_contract_occup gco ");
        sql.append("          left join GUA_CONTRACT_INFO gci on gco.CONTRACT_ID = gci.ID ");
        sql.append("          left join GUA_CONTRACT_WAY gcw on gci.ID = gcw.GUA_ID ");
        sql.append("          left join GUA_WAY gw on gcw.WAY_CODES = gw.id ");
        sql.append(" where 1 = 1 ");
        sql.append("   and gco.SYSCODE = :sysCode ");
        sql.append("   and gco.BUSINESS_ID in (:businessIds) ");
        return listBySQLAliasToBean(sql.toString(), params, GuaContractQueryDTO.class);
    }
    /**
     * <br>
     * <b>功能：</b> 通过业务id及编码查询授信挂接的担保方式 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/11 0011 <br>
     */
//    public List<GuaContractQueryDTO> listRatGuaWayName(List<String> businessIds, String sysCode) {
//        StringBuilder sql = new StringBuilder();
//        sql.append(
//                " select ru.bill_id as businessId, gci.id as guaId, gco.SYSCODE, gco.STATUS, gw.ID as guaWay, gw.NAME as guaWayName, ");
//        sql.append(
//                " gci.contract_code as contractCode,gci.start_date as startDate, gci.end_date as endDate,gci.gua_money as guaMoney, ");
//        sql.append(" gci.rmk as guaRmk ");
//        sql.append(" from RAT_URATION ru ");
//        sql.append("  inner join RAT_BKRATION rb on ru.RAT_CODE = rb.ID ");
//        sql.append("  inner join gua_contract_occup gco on rb.ID = gco.BUSINESS_ID ");
//        sql.append("  inner join GUA_CONTRACT_INFO gci on gco.CONTRACT_ID = gci.ID ");
//        sql.append("  inner join GUA_CONTRACT_WAY gcw on gci.ID = gcw.GUA_ID ");
//        sql.append("  inner join GUA_WAY gw on gcw.WAY_CODES = gw.id ");
//        sql.append("  where  gco.SYSCODE = 'rat' ");
//        sql.append("   and ru.SYSCODE = :sysCode ");
//        sql.append("   and ru.bill_id in (:businessIds) ");
//        sql.append("  order by ru.bill_id ");
//        Map<String, Object> params = new HashMap<>();
//        params.put("businessIds", businessIds);
//        params.put("sysCode", sysCode);
//        return listBySQLAliasToBean(sql.toString(), params, GuaContractQueryDTO.class);
//    }
    public List<GuaContractQueryDTO> listRatGuaWayName(List<String> businessIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select gco.BUSINESS_ID as businessId, gci.id as guaId, gco.SYSCODE, gco.STATUS, gw.ID as guaWay, gw.NAME as guaWayName, ");
        sql.append(
                " gci.contract_code as contractCode,gci.start_date as startDate, gci.end_date as endDate,gci.gua_money as guaMoney, ");
        sql.append(" gci.rmk as guaRmk ");
        sql.append(" from gua_contract_occup gco ");
        sql.append("  inner join GUA_CONTRACT_INFO gci on gco.CONTRACT_ID = gci.ID ");
        sql.append("  inner join GUA_CONTRACT_WAY gcw on gci.ID = gcw.GUA_ID ");
        sql.append("  inner join GUA_WAY gw on gcw.WAY_CODES = gw.id ");
        sql.append("  where gco.SYSCODE = 'rat' ");
        String businessSql = SqlUtil.getInSql(businessIds, "gco.business_id", true);
        sql.append(" and ").append(businessSql).append("  ");
        return listBySQLAliasToBean(sql.toString(), null, GuaContractQueryDTO.class);
    }
}
