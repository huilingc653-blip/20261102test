package com.bytter.letter.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.letter.domain.entity.GuaGuaranteeChangeInfo;
import com.bytter.letter.domain.vo.GuaGuaranteeChangeInfoVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeChangeInfoDao
 * @Description:保函更改申请数据访问层
 * @date 2019年1月9日 上午11:00:11
 */
@Repository
public class GuaGuaranteeChangeInfoDao extends BaseDao<GuaGuaranteeChangeInfo, String> {
    /**
     * 获取保函申请更改列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput datatableList(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.guara_id as guaraId,a.guarantee_no as guaranteeNo,b.guara_change_no as guaraChangeNo, ")
                .append(" b.change_count as changeCount,a.guarantee_type as guaranteeType,a.corp_id as corpId,a.open_corp_type as openCorpType,")
                .append(" a.open_way as openWay,a.open_type as openType,a.open_corp as openCorp,A.BANK_NAME AS openCorpName,")
                .append(" a.income_corp as incomeCorp,a.guarantee_attr as guaranteeAttr,a.cur_id as curId,b.gua_change_type as guaChangeType,")
                .append(" b.gua_change_money as guaChangeMoney,b.guarantee_money as guaranteeMoney,b.expect_end_date as expectEndDate,")
                .append(" b.start_date as startDate,b.end_date as endDate,b.status as status,b.handle_charge as handleCharge,")
                .append(" b.bond_change_type as bondChangeType,b.bond_change_money as bondChangeMoney,b.bond_money as bondMoney,")
                .append(" b.change_date as changeDate,b.change_term as changeTerm,b.change_reason as changeReason,b.operator as operator, ")
                .append(" a.CORP_NAME as corpName,a.CUR_NAME AS curName ")
                .append(" from GUA_GUARANTEE_CHANGE_INFO b,GUA_GUARANTEE_INFO a where b.status in(0,11,-1,102) ")
                .append(" and b.guara_id=a.id ");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "a.CORP_ID"));
        }
        return getDynamicBySQL(input, sql.toString(), GuaGuaranteeChangeInfoVO.class, "", false);
    }
    /**
     * 根据保函申请id查询保函更改列表(状态为101-完成更改)
     */
    public List<GuaGuaranteeChangeInfoVO> queryChangeListByGuaId(String guaranId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select b.id as id,b.guara_id as guaraId,a.guarantee_no as guaranteeNo,b.guara_change_no as guaraChangeNo, ")
                .append(" b.change_count as changeCount,a.guarantee_type as guaranteeType,a.corp_id as corpId,a.open_corp_type as openCorpType,")
                .append(" a.open_way as openWay,a.open_type as openType,a.open_corp as openCorp,a.BANK_NAME AS openCorpName,")
                .append(" a.income_corp as incomeCorp,a.guarantee_attr as guaranteeAttr,a.cur_id as curId,b.gua_change_type as guaChangeType,")
                .append(" b.gua_change_money as guaChangeMoney,b.guarantee_money as guaranteeMoney,b.expect_end_date as expectEndDate,")
                .append(" b.start_date as startDate,b.end_date as endDate,b.status as status,b.handle_charge as handleCharge,")
                .append(" b.bond_change_type as bondChangeType,b.bond_change_money as bondChangeMoney,b.bond_money as bondMoney,a.CORP_NAME AS corpName,")
                .append(" b.change_date as changeDate,b.change_term as changeTerm,b.change_reason as changeReason,b.operator as operator,a.CUR_NAME AS curName ")
                .append(" from GUA_GUARANTEE_CHANGE_INFO b,GUA_GUARANTEE_INFO a ")
                .append(" where b.guara_id=a.id and b.status in (101,102) and b.guara_id=:guaranId ")
                .append(" order by b.change_count desc ");
        params.put("guaranId", guaranId);
        return listBySQLAliasToBean(sql.toString(), params, GuaGuaranteeChangeInfoVO.class);
    }
    /**
     * 根据id查询保函更改申请信息
     */
    public GuaGuaranteeChangeInfoVO queryChangeInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select b.id as id,b.guara_id as guaraId,c.guarantee_no as guaranteeNo,b.guara_change_no as guaraChangeNo, ")
                .append(" b.change_count as changeCount,c.guarantee_type as guaranteeType,c.corp_id as corpId,c.open_corp_type as openCorpType,")
                .append(" c.open_way as openWay,c.open_type as openType,c.open_corp as openCorp,C.BANK_NAME AS openCorpName,")
                .append(" c.income_corp as incomeCorp,c.guarantee_attr as guaranteeAttr,c.cur_id as curId,")
                .append(" b.gua_change_type as guaChangeType,b.gua_change_money as guaChangeMoney,")
                .append(" b.guarantee_money as guaranteeMoney,b.expect_end_date as expectEndDate,")
                .append(" c.start_date as startDate,b.end_date as endDate,b.status as status,b.handle_charge as handleCharge,")
                .append(" c.expect_end_date as expectEndDateOld,c.end_date as endDateOld,c.ratmodel as ratmodel,")
                .append(" b.bond_change_type as bondChangeType,b.bond_change_money as bondChangeMoney,b.bond_money as bondMoney,")
                .append(" c.guarantee_money as guaranteeMoneyOld,c.bond_money as bondMoneyOld,")
                .append("c.IS_HOOK_BOND AS isHookBond,c.bond_acc as bondAcc,c.BOND_CONTRACT AS bondContract, ")
                .append(" b.change_date as changeDate,b.change_term as changeTerm,b.change_reason as changeReason,b.operator as operator,c.is_open_rat as isOpenRat, ")
                .append(" c.is_rmb_rat as isRmbRat,c.bond_curid AS bondCurId,c.corp_name as corpName,c.cur_name as curName ")
                .append(" from GUA_GUARANTEE_CHANGE_INFO b, GUA_GUARANTEE_INFO c   ")
                .append(" where b.guara_id=c.id and b.status <> -2 and b.id=:id ")
                .append(" order by b.change_count desc ");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaGuaranteeChangeInfoVO.class);
    }
    /**
     * 获取保函申请更改待审批列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput approveDatatableList(DataTablesInput input, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select b.id as id,b.guara_id as guaraId,a.guarantee_no as guaranteeNo,b.guara_change_no as guaraChangeNo, ")
                .append(" b.change_count as changeCount,a.guarantee_type as guaranteeType,a.corp_id as corpId,a.open_corp_type as openCorpType,")
                .append(" a.open_way as openWay,a.open_type as openType,a.open_corp as openCorp,a.BANK_NAME AS openCorpName,")
                .append(" a.income_corp as incomeCorp,a.guarantee_attr as guaranteeAttr,a.cur_id as curId,b.gua_change_type as guaChangeType,")
                .append(" b.gua_change_money as guaChangeMoney,b.guarantee_money as guaranteeMoney,b.expect_end_date as expectEndDate,")
                //TODO 补充名称信息
                .append(" a.corp_name as corpName,a.cur_name as curName,")
                .append(" b.start_date as startDate,b.end_date as endDate,b.status as status,b.handle_charge as handleCharge,")
                .append(" b.bond_change_type as bondChangeType,b.bond_change_money as bondChangeMoney,b.bond_money as bondMoney,")
                .append(" b.change_date as changeDate,b.change_term as changeTerm,b.change_reason as changeReason,b.operator as operator")
//                .append(" from GUA_GUARANTEE_CHANGE_INFO b,GUA_GUARANTEE_INFO a,BT_INPUT_BANK_INFO E where 1=1 ")
                .append(" from GUA_GUARANTEE_CHANGE_INFO b,GUA_GUARANTEE_INFO a where b.guara_id=a.id ");
        if (EmptyUtils.isNotEmpty(businessSql)){
            sql.append(" AND ").append(businessSql);
        }
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append("and b.status in (11)");
        }
        return getDynamicBySQL(input, sql.toString(), GuaGuaranteeChangeInfoVO.class, "", false);
    }
}
