package com.bytter.bill.domain.bill.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbIntellectGroupVO;
import com.bytter.bill.domain.bill.vo.AbIntellectVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 智能选票dao
 *
 * @author zhenghj
 */
@Repository
public class AbIntellectDao extends BaseDao<AbIntellectVO, String> {
    /**
     * 根据规则id获取有效的规则明细及要素信息
     *
     * @param ruleId 规则id
     */
    public List<AbIntellectVO> listRuleDtlByRuleId(String ruleId) {
        Map<String, Object> params = new HashMap<>(16);
        StringBuilder sql = new StringBuilder();
        sql.append("select r.id as ruleId,r.rule_code,r.rule_name as ruleName,r.screen_num as screenNum,rd.element_id as elementId,rd.element_name as elementName,rd.parent_id as parentId,rd.parent_name as parentName,rd.screen_type as screenType,rd.sort_order as sortOrder,");
        sql.append(" rd.scope_mode as scopeMode,rd.value as value,rd.start_value as startValue,rd.end_value as endValue,rd.start_relation as startRelation,rd.end_relation as endRelation,rd.relation as relation,rd.serialno as serialno,");
        sql.append(" e.source_type as sourceType,e.source_name as sourceName,e.dict_group as dictGroup,e.data_type as dataType,e.table_name as tableName,e.field_name as fieldName,e.compute_mode as computeMode,e.source_element_name1 as sourceElementName1,");
        sql.append(" e.source_element_name2 as sourceElementName2, rd.VALUE_IS_ELEMENT valueIsElement");
        sql.append(" from AB_INTELLECT_RULE r ");
        sql.append(" left join AB_INTELLECT_RULE_DTL rd on rd.rule_code=r.rule_code ");
        sql.append(" left join AB_INTELLECT_ELEMENT e on e.id=rd.element_id ");
        sql.append(" where r.valid_sign=:VALID_SIGN_1 and e.valid_sign=:VALID_SIGN_1 ");
        params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        sql.append(" and r.id=:ruleId ");
        params.put("ruleId", ruleId);
        sql.append(" order by rd.serialno");
        return this.listBySQLAliasToBean(sql.toString(), params, AbIntellectVO.class);
    }
    /**
     * 根据规则组id获取有效的规则组明细
     *
     * @param ruleGroupId 规则组id
     */
    public List<AbIntellectGroupVO> listRuleGroupDtlByRuleGroupId(String ruleGroupId) {
        Map<String, Object> params = new HashMap<>(2);
        StringBuilder sql = new StringBuilder();
        sql.append("select rg.id as ruleGroupId,rg.rule_group_code as ruleGroupCode,rg.rule_group_name as ruleGroupName,");
        sql.append(" rg.valid_sign as validSign,rg.rmk as rmk,rg.screen_num as screenNum,rg.relation as relation,");
        sql.append(" rg.apply_no as applyNo,rgd.id as ruleGroupDtlId,rgd.rule_id as ruleId,rgd.rule_name as ruleName,");
        sql.append(" rgd.dtl_group_code as dtlGroupCode,rgd.relation as dtlGroupRelation,rgd.serialno as serialNo ");
        sql.append(" from AB_INTELLECT_RULE_GROUP rg ");
        sql.append(" left join AB_INTELLECT_RULE_GROUP_DTL rgd on rgd.rule_group_code=rg.rule_group_code ");
        sql.append(" where rg.valid_sign=:VALID_SIGN_1 and rg.id=:ruleGroupId ");
        params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("ruleGroupId", ruleGroupId);
        sql.append(" order by rgd.serialno ");
        return this.listBySQLAliasToBean(sql.toString(), params, AbIntellectGroupVO.class);
    }
    /**
     * 开票需要补录时：应付票据：开票补录审批通过的票据
     * 开票不需要补录时：应付票据：开票完成（线下电票）提示付款完成（线上电票）
     *
     * @param dataTablesInput 查询
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAccebillEmitInfoDataTable1(DataTablesInput dataTablesInput, List<String> corpAuthList, Map<String, Object> whereVOMap) {
        boolean isEmitMake = dataTablesInput.getFormdata().containsKey(AccebillConstant.AB_CONFIG_ISEMITMAKE)
                && "true".equals(String.valueOf(dataTablesInput.getFormdata().get(AccebillConstant.AB_CONFIG_ISEMITMAKE)));
        dataTablesInput.getFormdata().remove(AccebillConstant.AB_CONFIG_ISEMITMAKE);
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append("  z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
                .append("z.bill_type as billType,z.bill_flag as billFlag,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,z.bill_money as searchForMoney,")
                .append("c.corp_id as corpId,c.CORP_NAME as corpName,l.opt_status as optStatus,l.brief_id as briefId,")
                .append("sd.TYPE_CODE as source ");
        sql.append(" from AB_VOUCHERINFO_VICE c ");
        sql.append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ");
        sql.append(" left join AB_ACCRECORDS l on c.id=l.vvid ");
//        sql.append(" left join sys_corp s on c.corp_id = s.id ");
        sql.append(" left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=l.brief_id "
                + "and (c.source=sd.source "
                + "or (c.source is null and l.bill_source=sd.source) "
                + "or (c.source is null and l.bill_source is null and sd.source is null )"
                + ") ");
        if (whereVOMap.containsKey("table")) {
            sql.append(whereVOMap.get("table"));
        }
        sql.append(" where 1=1 ");
        sql.append(" and ( ");
        //是否已付款
        //if("1".equals(dataTablesInput.getFormdata().get("isPayed"))) {
        //在线票据：对方已收票/未在付款办理挂接（票据在承兑汇票中已开出）。
        //在线票据：对方已收票/已在付款办理挂接。
        //离线票据：已登记（已录入或已导入）/未在付款办理挂接。
        //离线票据：已登记（已录入或已导入）/已在付款办理挂接。
        //离线票据：未登记(不支持）
        sql.append(" ((C.IS_PAY = 0 OR C.IS_PAY IS NULL) AND C.VOUCHER_ID IS NULL )  ");
        String oldIds = dataTablesInput.getFormdata().get("oldIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("oldIds"));
        //针对修改历史已选数据进行支持显示
        if (StrUtil.isNotEmpty(oldIds)) {
            sql.append(" or ").append(SqlUtil.getInSqlByParams(oldIds.split(","), "l.id", "oldIds", params));
        }
        sql.append(" ) ");
        params.put("C_VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
        // 从表 :处理状态 box_status -- 处理完成 11
        // 流水表 :处理标记 valid_sign -- 可用 1
        // 操作状态 opt_status -- 已签收19
        sql.append(" and c.box_status=:C_BOX_STATUS "
                + " and l.valid_sign= :L_Valid_Sign "
                + " and l.status= :L_STATUS95 "
                + " and l.opt_status=:L_OPT_STATUS ");
        if(corpAuthList!=null){
            sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList,"c.corp_id","corpAuthIds",params));
        }
        if(dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
        params.put("C_BOX_STATUS", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("L_Valid_Sign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("L_STATUS95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("L_OPT_STATUS", AccebillConstant.AB_BILL_OPT_STATUS19);
      /*  params.put("L_BRIEF_ID", new String[]{AccebillConstant.BRIEF_STATUS_001,
                AccebillConstant.BRIEF_STATUS_002,
        });*/
        if(isEmitMake){//系统需要开票补录功能时 业务类型 brief_id -- 6101(补录)
            sql.append(" and l.brief_id=:L_BRIEF_ID6101 ");
            params.put("L_BRIEF_ID6101", AccebillConstant.BRIEF_STATUS_6101);
        }else{//系统不需要开票补录功能时
            // 指令发送提示收票完成(线上)和开票登记完成(线下)就可以做 选择 不需要补录
            sql.append(" and (l.brief_id=:L_BRIEF_ID6101 or (l.brief_id in (:briefId003) and l.send_sign=:sendSign1 ) or (l.brief_id in (:briefId001) and l.send_sign=:sendSign0 ) ) ");
            params.put("sendSign0",new String[]{AccebillConstant.AB_BILL_SEND_SIGN_N});
            params.put("sendSign1",new String[]{AccebillConstant.AB_BILL_SEND_SIGN_Y});
            params.put("briefId001",new String[]{AccebillConstant.BRIEF_STATUS_001});
            params.put("briefId003",new String[]{AccebillConstant.BRIEF_STATUS_003});
            params.put("L_BRIEF_ID6101", AccebillConstant.BRIEF_STATUS_6101);
        }
        //已选数据进行过滤显示
        String ids=dataTablesInput.getFormdata().get("selectIds")==null?null:String.valueOf(dataTablesInput.getFormdata().get("selectIds"));
        if(StrUtil.isNotEmpty(ids)){
            sql.append(" and ").append(SqlUtil.getInSqlByParams(ids.split(","),"l.id","selectIds",params));
            dataTablesInput.getFormdata().remove("selectIds");
        }
        //完善多个票据号同时查询的场景
        String origNo=dataTablesInput.getFormdata().get("origNo")==null?null:String.valueOf(dataTablesInput.getFormdata().get("origNo"));
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        String oldCorpId = dataTablesInput.getFormdata().get("oldCorpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("oldCorpId"));
        if (StringUtils.isNotEmpty(corpIds)) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(corpIds.split(","), "c.CORP_ID", "corpId", params));
        } else if (StringUtils.isNotEmpty(oldCorpId)) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(oldCorpId.split(","), "c.CORP_ID", "corpId", params));
        }
        dataTablesInput.getFormdata().remove("ins_corpId");
        if (whereVOMap.containsKey("where")) {
            sql.append(whereVOMap.get("where"));
        }
        if (whereVOMap.containsKey("order")) {
            sql.append(whereVOMap.get("order"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbVoucherinfoViceVO.class, "info", true, params);
    }
    /**
     * 应收智能选票
     *
     * @param dataTablesInput 查询
     * @return
     * todo 这个sql好像是没用的  先不管他
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAccebillInfoDataTable1(DataTablesInput dataTablesInput, List<String> corpAuthList, Map<String, Object> whereVOMap) {
        Map<String, Object> params = new HashMap<>();
      /*  StringBuilder sqlOne = new StringBuilder("select ");
        sqlOne.append(" info.vid as vid,info.vvid as vvid,info.id as id,info.recid as recid,")
                .append(" info.billType as billType,info.billFlag as billFlag,info.payer as payer,")
                .append(" info.acceptor as acceptor,info.origNo as origNo,info.billMoney as billMoney,")
                .append(" info.startDate as startDate,info.endDate as endDate,info.payee as payee,")
                .append(" info.billStatus as billStatus,info.insideSing as insideSing,")
                .append(" info.signUpFlag as signUpFlag,info.applyDate as applyDate,info.reqNm as reqNm,")
                .append(" info.recNm as recNm,info.recAcctId as recAcctId,info.bankAcc as bankAcc,")
                .append(" info.searchForMoney as searchForMoney,info.corpId as corpId,info.corpName as corpName,")
                .append(" info.optStatus as optStatus,info.briefId as briefId,info.source as source");
*/
        StringBuilder sqlTwo = new StringBuilder("  select ");
        sqlTwo.append(" z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,z.bill_type as billType,z.bill_flag as billFlag,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,z.bill_money as searchForMoney,")
                .append("c.corp_id as corpId,c.corp_name as corpName,l.opt_status as optStatus,l.brief_id as briefId, ")
                .append("sd.TYPE_CODE as source ");
        StringBuilder sqlThree = new StringBuilder("");
        sqlThree.append(" from AB_VOUCHERINFO_VICE c ")
                .append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ")
                .append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
                .append(" left join AB_ACCRECORDS l2 on l2.lastid = l.id and l2.brief_id='013'")
                .append(" left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=l.brief_id "
                        + "and (c.source=sd.source "
                        + "or (c.source is null and l.bill_source=sd.source) "
                        + "or (c.source is null and l.bill_source is null and sd.source is null )"
                        + ") ");
        if ("1".equals(dataTablesInput.getFormdata().get("isPayed"))) {
            sqlThree.append(" left join nis_bill nb on nb.bill_Id=l.id ");
            sqlThree.append(" left join nis_billhead nbh on nbh.id=nb.pay_id ");
            sqlThree.append(" LEFT JOIN (SELECT MAX(ID) AS ID, BILL_ID FROM NIS_BILL GROUP BY BILL_ID) NB2 ON NB2.BILL_ID = NB.BILL_ID ");
        }
        if (whereVOMap.containsKey("table")) {
            sqlThree.append(whereVOMap.get("table"));
        }
        sqlThree.append(" where 1=1 and l.STATUS=:STATUS and l.OPT_STATUS =:OPT_STATUS and c.ENTER_STOCK in (:enterStock) ")
                .append(" and c.box_status=:box_status ")
                .append(" and BT_NVL_CHAR(l.BANKENDRSMTMK, z.BANENDRSMTMK)=:BANENDRSMTMK ")
                .append(" and ( ")
                .append(" ( ")
                //线上电票可以业务
                .append(" l.SEND_SIGN=:SEND_SIGN1 ")
                .append(" and z.bill_flag =:bill_flag ")
                .append(" and z.end_date>=:end_date ")
                .append(" and l.brief_id in (:brief_id1) ) ")
                //纸票或者线下电票可用业务
                .append(" or   ")
                //收票登记非质押来源
                .append(" ( (")
                .append(" l.brief_id in (:brief_id2) ")
                .append(" and c.BILL_SOURCE <> :BILL_SOURCE ")
                .append(" ) or ")
                .append(" l.brief_id in (:brief_id3)")
                .append(" ) ")
                .append(" ) ");
        sqlThree.append(" and ((l.valid_sign =:valid_sign and c.valid_sign=:valid_sign) ");
        String oldIds=dataTablesInput.getFormdata().get("oldIds")==null?null:String.valueOf(dataTablesInput.getFormdata().get("oldIds"));
        //针对修改历史已选数据进行支持显示
        if (StrUtil.isNotEmpty(oldIds)) {
            sqlThree.append(" or ").append(SqlUtil.getInSqlByParams(oldIds.split(","), "l.id", "oldIds", params));
        }
        sqlThree.append(" ) ");
        params.put("SEND_SIGN1", AccebillConstant.AB_BILL_SEND_SIGN_Y);
        params.put("bill_flag", AccebillConstant.AB_BILL_FLAG_ELECT);
        params.put("end_date", DateUtils.getDateYMD());
        params.put("box_status", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("SEND_SIGN2", AccebillConstant.AB_BILL_SEND_SIGN_N);
        params.put("brief_id1", new String[]{
                AccebillConstant.BRIEF_STATUS_9003,
                AccebillConstant.BRIEF_STATUS_9010,
                AccebillConstant.BRIEF_STATUS_9012,
                AccebillConstant.BRIEF_STATUS_9019});
        params.put("brief_id2", new String[]{AccebillConstant.BRIEF_STATUS_6001});
        params.put("BILL_SOURCE", AccebillConstant.AB_BILL_SOURCE_PLEDGE);
        params.put("brief_id3", new String[]{
                AccebillConstant.BRIEF_STATUS_6019,
                AccebillConstant.BRIEF_STATUS_6212});
        params.put("BANENDRSMTMK", AccebillConstant.AB_BANEM_Y);
        //已选数据进行过滤显示
        String ids = dataTablesInput.getFormdata().get("selectIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("selectIds"));
        if (StrUtil.isNotEmpty(ids)) {
            sqlThree.append(" and ").append(SqlUtil.getInSqlByParams(ids.split(","), "l.id", "selectIds", params));
        }
        params.put("enterStock", new String[]{AccebillConstant.AB_ENTER_STOCK_CORP, AccebillConstant.AB_ENTER_STOCK_NET});
        params.put("valid_sign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("STATUS", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("OPT_STATUS", AccebillConstant.AB_BILL_OPT_STATUS19);
        dataTablesInput.getFormdata().remove("isPayed");
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        String oldCorpId = dataTablesInput.getFormdata().get("oldCorpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("oldCorpId"));
        if (StringUtils.isNotEmpty(corpIds)) {
            sqlThree.append(" and ").append(SqlUtil.getInSqlByParams(corpIds.split(","), "c.CORP_ID", "corpId", params));
        } else if (StringUtils.isNotEmpty(oldCorpId)) {
            sqlThree.append(" and ").append(SqlUtil.getInSqlByParams(oldCorpId.split(","), "c.CORP_ID", "corpId", params));
        }
        dataTablesInput.getFormdata().remove("ins_corpId");
        //显示没做背书请求或者最新的背书是拒签状态
        sqlThree.append(" and (l2.id is null or (l2.opt_status=-4 and l2.serial_no = (select max(ss.serial_no) from ab_accrecords ss where ss.lastid = l.id and ss.brief_id='013'))) ");
        if (whereVOMap.containsKey("where")) {
            sqlThree.append(whereVOMap.get("where"));
        }
        //sqlThree.append(" ) info ");
        if (whereVOMap.containsKey("order")) {
            sqlThree.append(whereVOMap.get("order"));
        }
        //sqlOne.append("from (");
        sqlTwo.append(sqlThree);
        return getDynamicBySQL(dataTablesInput, sqlTwo.toString(), AbVoucherinfoViceVO.class, "", false, params);
    }
    /**
     * 根据客商账号获取账号银行性质类型
     * @param accList 客商账号
     * @return
     *     todo 这个sql好像是没用的  先不管他
     */
    public List<String> getBankClassByExtacc(List<String> accList){
        StringBuffer sql = new StringBuffer(" select bt.bank_class from sys_external_corp_bankacc e ");
        sql.append(" inner join BT_INPUT_BANK_INFO ibi on ibi.sys_bank_code = e.bank_sourcecode ");
        sql.append(" left JOIN BT_BANK_TYPE bt ON ibi.BANK_PREFIX = bt.BANK_PREFIX");
        sql.append(" and (bt.bank_name_Prefix is null or ibi.bank_name like bt_concat3('%', bt.bank_name_Prefix, '%')) ");
        sql.append(" where e.external_acc in (:acc) ");
        Map<String,Object> params=new HashMap<>();
        params.put("acc",accList);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),params);
        return sqlQuery.list();
    }
    /**
     * 根据单位账号获取账号银行性质类型
     * @param accList 单位账号
     * @return
     *  todo 这个sql好像是没用的  先不管他
     */
    public List<String> getBankClassByBankAcc(List<String> accList){
        StringBuffer sql = new StringBuffer(" select bt.bank_class from bt_bank_acc acc ");
        sql.append(" left join bt_bank_type bt on acc.bank_type_id = bt.id ");
        sql.append(" where acc.bank_acc in (:acc) ");
        Map<String,Object> params=new HashMap<>();
        params.put("acc",accList);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),params);
        return sqlQuery.list();
    }
    /**
     * 开票需要补录时：应付票据：开票补录审批通过的票据
     * 开票不需要补录时：应付票据：开票完成（线下电票）提示付款完成（线上电票）
     *
     * @param dataTablesInput 查询
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAccebillEmitInfoDataTable(DataTablesInput dataTablesInput, List<String> corpAuthList, Map<String, Object> whereVOMap) {
        boolean isEmitMake = dataTablesInput.getFormdata().containsKey(AccebillConstant.AB_CONFIG_ISEMITMAKE)
                && "true".equals(String.valueOf(dataTablesInput.getFormdata().get(AccebillConstant.AB_CONFIG_ISEMITMAKE)));
        dataTablesInput.getFormdata().remove(AccebillConstant.AB_CONFIG_ISEMITMAKE);
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append("  z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
                .append("z.bill_type as billType,z.bill_flag as billFlag,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,z.bill_money as searchForMoney,")
                .append("c.corp_id as corpId,c.CORP_NAME as corpName,l.opt_status as optStatus,l.brief_id as briefId,")
                .append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel,")
                .append("sd.TYPE_CODE as source, ");
        sql.append(" CASE WHEN M.BALANCE IS NULL THEN Z.BILL_MONEY ");
        String oldIds = dataTablesInput.getFormdata().get("oldIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("oldIds"));
        if (StringUtil.isNotEmpty(oldIds)) {
            sql.append(" WHEN l.id in (:oldIds) then m.balance + m.pay_money ");
        }
        sql.append(" ELSE M.BALANCE END AS balance ");
        sql.append(" from AB_VOUCHERINFO_VICE c ");
        sql.append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ");
        sql.append(" left join AB_ACCRECORDS l on c.id=l.vvid ");
//        sql.append(" left join sys_corp s on c.corp_id = s.id ");
//        sql.append(" left join sys_corp s2 on c.apply_corp_id = s2.id ");
        sql.append(" left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=l.brief_id "
                + "and (c.source=sd.source "
                + "or (c.source is null and l.bill_source=sd.source) "
                + "or (c.source is null and l.bill_source is null and sd.source is null )"
                + ") ");
        sql.append(" left join AB_BILL_PAY_MIDDLE m on c.id = m.bill_id ");
        sql.append(" left join (SELECT MAX(CREATE_DATE) AS CREATE_DATE, BILL_ID FROM AB_BILL_PAY_MIDDLE GROUP BY BILL_ID) MM ON MM.BILL_ID = M.BILL_ID  ");
        if (whereVOMap.containsKey("table")) {
            sql.append(whereVOMap.get("table"));
        }
        sql.append(" where 1=1 ");
        sql.append(" and ((M.CREATE_DATE = MM.CREATE_DATE AND M.BILL_ID = MM.BILL_ID) OR m.create_date IS NULL) ");
        sql.append(" and ( ");
        //是否已付款
        //if("1".equals(dataTablesInput.getFormdata().get("isPayed"))) {
        //在线票据：对方已收票/未在付款办理挂接（票据在承兑汇票中已开出）。
        //在线票据：对方已收票/已在付款办理挂接。
        //离线票据：已登记（已录入或已导入）/未在付款办理挂接。
        //离线票据：已登记（已录入或已导入）/已在付款办理挂接。
        //离线票据：未登记(不支持）
        sql.append(" ((((C.IS_PAY = 0 OR C.IS_PAY IS NULL) AND C.VOUCHER_ID IS NULL  and (m.balance > 0 or m.balance is null))) or (c.is_pay = 1 and (m.balance > 0 or m.balance is null))) ");
        //针对修改历史已选数据进行支持显示
        if (StringUtil.isNotEmpty(oldIds)) {
            sql.append(" or ").append(SqlUtil.getInSqlByParams(oldIds.split(","), "l.id", "oldIds", params));
        }
        sql.append(" ) ");
        params.put("C_VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
        if (corpAuthList != null) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "c.APPLY_CORP_ID", "corpAuthIds", params));
        }
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
        //是否已付款为否时
        //在线票据：在线票据：已开票登记，对方未收票。
        //离线票据：不支持，即离线票据即为已付款票据
        sql.append(" and (((l.brief_id=:briefId001 ");
        sql.append(" and c.box_status=:C_BOX_STATUS ");
        sql.append(" and l.valid_sign= :L_Valid_Sign ");
        sql.append(" and l.status= :L_STATUS95 ");
        sql.append(" and l.opt_status=:L_OPT_STATUS ");
        sql.append(")  ");
        //提示承兑或提示收票在途的票据
        sql.append(" or (l.brief_id in (:briefId23) and l.valid_sign in(:validsignAll) and l.status in (:statusAll) and l.opt_status in (:optStatusAll)))");
        sql.append(" and z.bill_status in (:billStatusList) and l.send_sign=:sendSign1");
        sql.append(") ");
        params.put("C_BOX_STATUS", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("L_Valid_Sign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("L_STATUS95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("L_OPT_STATUS", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("briefId23", new String[]{AccebillConstant.BRIEF_STATUS_002, AccebillConstant.BRIEF_STATUS_003});
        params.put("validsignAll", new String[]{AccebillConstant.AB_BILL_VALID_SIGN_1, AccebillConstant.AB_BILL_VALID_SIGN_0});
        params.put("statusAll", new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS95, AccebillConstant.AB_BILL_BUS_STATUS11, AccebillConstant.AB_BILL_BUS_STATUS94, AccebillConstant.AB_BILL_BUS_STATUS_1});
        params.put("optStatusAll", new String[]{AccebillConstant.AB_BILL_OPT_STATUS16, AccebillConstant.AB_BILL_OPT_STATUS18, AccebillConstant.AB_BILL_OPT_STATUS19,});
        params.put("sendSign1", new String[]{AccebillConstant.AB_BILL_SEND_SIGN_Y});
        params.put("L_BRIEF_ID6101", AccebillConstant.BRIEF_STATUS_6101);
        params.put("briefId001", AccebillConstant.BRIEF_STATUS_001);
        params.put("billStatusList", new String[]{AccebillConstant.AB_BILL_STATUS_010004, AccebillConstant.AB_BILL_STATUS_020001,
                AccebillConstant.AB_BILL_STATUS_020006, AccebillConstant.AB_BILL_STATUS_030001});
        //已选数据进行过滤显示
        String ids = dataTablesInput.getFormdata().get("selectIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("selectIds"));
        if (StringUtil.isNotEmpty(ids)) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(ids.split(","), "l.id", "selectIds", params));
            dataTablesInput.getFormdata().remove("selectIds");
        }
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        if (StringUtils.isNotEmpty(corpIds)) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(corpIds.split(","), "c.CORP_ID", "corpId", params));
        }
        dataTablesInput.getFormdata().remove("ins_corpId");
        if (whereVOMap.containsKey("where")) {
            sql.append(whereVOMap.get("where"));
        }
        if (whereVOMap.containsKey("order")) {
            sql.append(whereVOMap.get("order"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbVoucherinfoViceVO.class, "info", true, params);
    }
    /**
     * 智能
     *
     * @param dataTablesInput 查询
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbVoucherinfoViceVO> listAccebillInfoDataTable(DataTablesInput dataTablesInput, List<String> corpAuthList, Map<String, Object> whereVOMap) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlOne = new StringBuilder("select ");
        sqlOne.append(" info.vid as vid,info.vvid as vvid,info.id as id,info.recid as recid,")
                .append(" info.billType as billType,info.billFlag as billFlag,info.payer as payer,")
                .append(" info.acceptor as acceptor,info.origNo as origNo,info.billMoney as billMoney,")
                .append(" info.startDate as startDate,info.endDate as endDate,info.payee as payee,")
                .append(" info.billStatus as billStatus,info.insideSing as insideSing,")
                .append(" info.signUpFlag as signUpFlag,info.applyDate as applyDate,info.reqNm as reqNm,")
                .append(" info.recNm as recNm,info.recAcctId as recAcctId,info.bankAcc as bankAcc,")
                .append(" info.searchForMoney as searchForMoney,info.corpId as corpId,info.corpName as corpName,")
                .append(" info.optStatus as optStatus,info.briefId as briefId,info.source as source, ")
                .append(" info.poolStatus as poolStatus,info.bpName as bpName,info.inoutType as inoutType,info.lastId as lastId,info.bifCode as bifCode,")
                .append(" info.applyCorpId,info.applyCorpName,info.applyCorpCode,info.busCorpId,info.busPersonnel,info.balance as balance from (");
        StringBuilder sqlTwo = new StringBuilder("  select ");
        sqlTwo.append(" z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,z.bill_type as billType,z.bill_flag as billFlag,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,z.bill_money as searchForMoney,")
                .append("c.corp_id as corpId,c.corp_name as corpName,l.opt_status as optStatus,l.brief_id as briefId, ")
                .append("sd.TYPE_CODE as source,bt_nvl_char(c.pool_status,'0')  as poolStatus, '' as bpName,'' as inoutType,'' as lastId,''  as bifCode,  ")
                .append(" CASE WHEN M.BALANCE IS NULL THEN Z.BILL_MONEY ");
        String oldIds = dataTablesInput.getFormdata().get("oldIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("oldIds"));
        //针对修改历史已选数据进行支持显示
        if (StrUtil.isNotEmpty(oldIds)) {
            sqlTwo.append(" WHEN l.id in (:oldIds) then m.balance + m.pay_money ");
        }
        sqlTwo.append(" ELSE M.BALANCE END AS balance, ");
        sqlTwo.append("c.APPLY_CORP_ID as applyCorpId,c.apply_corp_name as applyCorpName,c.apply_corp_code as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ");
        StringBuilder sqlThree = new StringBuilder();
        sqlThree.append(" from AB_VOUCHERINFO_VICE c ")
                .append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ")
                .append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
                .append(" left join AB_ACCRECORDS l2 on l2.lastid = l.id and l2.brief_id='013'")
                .append(" left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=l.brief_id "
                        + "and (c.source=sd.source "
                        + "or (c.source is null and l.bill_source=sd.source) "
                        + "or (c.source is null and l.bill_source is null and sd.source is null )"
                        + ") ")
                .append(" LEFT JOIN AB_BILL_PAY_MIDDLE M ON C.ID = M.BILL_ID ")
                .append(" left join (SELECT MAX(CREATE_DATE) AS CREATE_DATE, BILL_ID FROM AB_BILL_PAY_MIDDLE ")
                .append(" GROUP BY BILL_ID) MM ON MM.BILL_ID = M.BILL_ID ");
        //todo  好像没有用到这里面的字段
//        if ("1".equals(dataTablesInput.getFormdata().get("isPayed"))) {
//            sqlThree.append(" left join nis_bill nb on nb.bill_Id=l.id ");
//            sqlThree.append(" left join nis_billhead nbh on nbh.id=nb.pay_id ");
//            sqlThree.append(" LEFT JOIN (SELECT MAX(ID) AS ID, BILL_ID FROM NIS_BILL GROUP BY BILL_ID) NB2 ON NB2.BILL_ID = NB.BILL_ID ");
//        }
        if (whereVOMap.containsKey("table")) {
            sqlThree.append(whereVOMap.get("table"));
        }
        sqlThree.append(" where 1=1 and l.STATUS=:STATUS and l.OPT_STATUS =:OPT_STATUS and c.ENTER_STOCK in (:enterStock) ")
                .append(" and c.box_status=:box_status ")
                .append(" and BT_NVL_CHAR(l.BANKENDRSMTMK, z.BANENDRSMTMK)=:BANENDRSMTMK ")
                .append(" and ( ")
                .append(" ( ")
                //线上电票可以业务
                .append(" l.SEND_SIGN=:SEND_SIGN1 ")
                .append(" and z.bill_flag =:bill_flag ")
                .append(" and z.end_date>=:end_date ")
                .append(" and l.brief_id in (:brief_id1) ) ")
                //纸票或者线下电票可用业务
                .append(" or   ")
                //收票登记非质押来源
                .append(" ( (")
                .append(" l.brief_id in (:brief_id2) ")
                .append(" and c.BILL_SOURCE <> :BILL_SOURCE ")
                .append(" ) or ")
                .append(" l.brief_id in (:brief_id3)")
                .append(" ) ")
                .append(" ) ");
        sqlThree.append(" and ((l.valid_sign =:valid_sign and c.valid_sign=:valid_sign) ");
        sqlThree.append(" or (C.VALID_SIGN = 0 AND L.VALID_SIGN = :valid_sign  ");
        sqlThree.append(" AND ((M.CREATE_DATE = MM.CREATE_DATE AND M.BILL_ID = MM.BILL_ID) OR M.CREATE_DATE IS NULL) ");
        sqlThree.append(" AND ((C.IS_PAY = 0 OR C.IS_PAY IS NULL) AND C.VOUCHER_ID IS NULL ");
        sqlThree.append(" AND (M.BALANCE IS NULL OR M.BALANCE != 0))) ");
        //针对修改历史已选数据进行支持显示
        if (StrUtil.isNotEmpty(oldIds)) {
            sqlThree.append(" or ").append(SqlUtil.getInSqlByParams(oldIds.split(","), "l.id", "oldIds", params));
        }
        sqlThree.append(" ) ");
        params.put("SEND_SIGN1", AccebillConstant.AB_BILL_SEND_SIGN_Y);
        params.put("bill_flag", AccebillConstant.AB_BILL_FLAG_ELECT);
        params.put("end_date", DateUtils.getDateYMD());
        params.put("box_status", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("SEND_SIGN2", AccebillConstant.AB_BILL_SEND_SIGN_N);
        params.put("brief_id1", new String[]{
                AccebillConstant.BRIEF_STATUS_9003,
                AccebillConstant.BRIEF_STATUS_9010,
                AccebillConstant.BRIEF_STATUS_9012,
                AccebillConstant.BRIEF_STATUS_9019});
        params.put("brief_id2", new String[]{AccebillConstant.BRIEF_STATUS_6001});
        params.put("BILL_SOURCE", AccebillConstant.AB_BILL_SOURCE_PLEDGE);
        params.put("brief_id3", new String[]{
                AccebillConstant.BRIEF_STATUS_6019,
                AccebillConstant.BRIEF_STATUS_6212});
        params.put("BANENDRSMTMK", AccebillConstant.AB_BANEM_Y);
        //已选数据进行过滤显示
        String ids = dataTablesInput.getFormdata().get("selectIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("selectIds"));
        if (StrUtil.isNotEmpty(ids)) {
            sqlThree.append(" and ").append(SqlUtil.getInSqlByParams(ids.split(","), "l.id", "selectIds", params));
        }
        params.put("enterStock", new String[]{AccebillConstant.AB_ENTER_STOCK_CORP, AccebillConstant.AB_ENTER_STOCK_NET});
        params.put("valid_sign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("STATUS", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("OPT_STATUS", AccebillConstant.AB_BILL_OPT_STATUS19);
        dataTablesInput.getFormdata().remove("isPayed");
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        String oldCorpId = dataTablesInput.getFormdata().get("oldCorpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("oldCorpId"));
        if (StringUtils.isNotEmpty(corpIds)) {
            sqlThree.append(" and ").append(SqlUtil.getInSqlByParams(corpIds.split(","), "c.CORP_ID", "corpId", params));
        } else if (StringUtils.isNotEmpty(oldCorpId)) {
            sqlThree.append(" and ").append(SqlUtil.getInSqlByParams(oldCorpId.split(","), "c.CORP_ID", "corpId", params));
        }
        dataTablesInput.getFormdata().remove("ins_corpId");
        //显示没做背书请求或者最新的背书是拒签状态
        sqlThree.append(" and (l2.id is null or (l2.opt_status=-4 and l2.serial_no = (select max(ss.serial_no) from ab_accrecords ss where ss.lastid = l.id and ss.brief_id='013'))) ");
        if (whereVOMap.containsKey("where")) {
            sqlThree.append(whereVOMap.get("where"));
        }
        //sqlThree.append(" ) info ");
        if (whereVOMap.containsKey("order")) {
            sqlThree.append(whereVOMap.get("order"));
        }
        //sqlOne.append(sqlTwo);
        sqlTwo.append(sqlThree);
        return getDynamicBySQL(dataTablesInput, sqlTwo.toString(), AbVoucherinfoViceVO.class, "", true, params);
    }
}
