package com.bytter.increment.domain.dao;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * 理财产品购买申请dao
 *
 * @author ouyj
 */
@Repository
public class ChoProductBuyApplyDao extends BaseDao<ChoProductReq, String> {
    /**
     * 公共查询列sql
     *
     * @return 公共查询列sql
     */
    private StringBuilder getCommonColumn() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pr.id as id, pr.corp_id as corpId, corp.name as corpName, pr.bank_info_id as bankInfoId, pr.bank_name as bankName, ");
        sql.append(" pr.product_code as productCode, pt.product_name as productName, pr.product_type as productType,pr.bank_Type_Id as bankTypeId,bt.type_name as bankTypeName,");
        sql.append(" pr.INCOME_CLAUSE as incomeClause,pr.bank_acc as bankAcc,pr.buy_money as buyMoney,pr.cur_id as curId, cur.cur_name as curName, ");
        sql.append(" pr.interest_start_date as interestStartDate, pr.Deadline_Date as deadlineDate,pr.refer_income_rate as referIncomeRate, ");
        sql.append(" pr.is_move_up as isMoveUp, pr.status as status, pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, ");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" pr.IS_MAINTAIN_VAL as isMaintainVal, pr.END_REFER_INCOME_MONEY as endReferIncomeMoney, pr.END_REFER_INCOME_RATE as endReferIncomeRate, ");
        sql.append(" pr.REFER_INCOME_MONEY as referIncomeMoney, pr.move_up_rmk as moveUpRmk,pr.rmk as rmk,  pr.charge_money as chargeMoney, ");
        sql.append(" pr.sub_fee_money as subFeeMoney, pr.attachment_id as attachmentId, pr.is_guaranteed as isGuaranteed,pr.capital_source as capitalSource,");
        sql.append(" pr.daily_net_val as dailyNetVal, pr.portion as portion,");
        sql.append(" pr.buy_date as buyDate ");
        return sql;
    }
    /**
     * 理财产品购买申请查询页数据
     *
     * @param dataTablesInput 前端传参
     * @param joinSql         单位权限sql
     * @return 理财产品购买申请查询页数据
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = getCommonColumn();
        sql.append(" , pr.buy_apply_status as buyApplyStatus ");
        sql.append(" from cho_product_req pr ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" left join sys_corp corp on corp.id = pr.corp_id ");
        sql.append(" left join bt_bank_type bt on bt.id = pr.bank_Type_Id ");
        sql.append(" left join bt_currency cur on cur.id = pr.cur_id");
        sql.append(" where 1 = 1 ");
        sql.append(" and pr.buy_apply_status in ('0','-1','11') ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoProductReqVo.class, "pr", false);
    }
    /**
     * 理财产品购买审批数据
     *
     * @param dataTablesInput 前端传参
     * @param businessSql     业务sql
     * @return 理财产品购买审批数据
     */
    public DataTablesOutput<ChoProductReqVo> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = getCommonColumn();
        sql.append(" from cho_product_req pr ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" left join sys_corp corp on corp.id = pr.corp_id ");
        sql.append(" left join bt_bank_type bt on bt.id = pr.bank_Type_Id ");
        sql.append(" left join bt_currency cur on cur.id = pr.cur_id");
        sql.append(" where 1 = 1 ");
        sql.append(" and pr.buy_apply_status = '11' ");
        sql.append(" and " + businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoProductReqVo.class, "f", false);
    }
    /**
     * 根据合同号前缀（年份月份）查询对应的理财登记集合
     *
     * @param contractNoPrefix 合同号前缀（年份月份）
     * @return 理财登记集合
     */
    public List<ChoProductReq> queryListWithPrefix(String contractNoPrefix) {
        StringBuilder sql = getCommonColumn();
        sql.append(" from cho_product_req pr ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" left join sys_corp corp on corp.id = pr.corp_id ");
        sql.append(" left join bt_bank_type bt on bt.id = pr.bank_Type_Id ");
        sql.append(" left join bt_currency cur on cur.id = pr.cur_id");
        sql.append(" where 1 = 1 ");
        sql.append(" and pr.contract_no like :contractNoPrefix ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("contractNoPrefix", contractNoPrefix + "%");
        return listBySQLAliasToBean(sql.toString(), params, ChoProductReq.class);
    }
}
