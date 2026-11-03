package com.bytter.increment.domain.dao;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
/**
 * 理财维护dao
 *
 * @author ouyj
 */
@Repository
public class ChoProductMaintainDao extends BaseDao<ChoProductReq, String> {
    /**
     * 理财维护查询页数据
     *
     * @param dataTablesInput 前端传参
     * @param joinSql         单位权限sql
     * @return 前端数据
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pr.id as id, pr.corp_id as corpId, pr.corp_name as corpName, pr.bank_info_id as bankInfoId, pr.bank_name as bankName, ");
        sql.append(" pr.product_code as productCode, pt.product_name as productName, pr.product_type as productType,pr.bank_Type_Id as bankTypeId,pr.bank_type_name as bankTypeName,");
        sql.append(" pr.INCOME_CLAUSE as incomeClause,pr.bank_acc as bankAcc,pr.buy_money as buyMoney,pr.cur_id as curId, pr.cur_name as curName, ");
        sql.append(" pr.interest_start_date as interestStartDate, pr.Deadline_Date as deadlineDate,pr.refer_income_rate as referIncomeRate, ");
        sql.append(" pr.is_move_up as isMoveUp, pr.status as status, pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, ");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" pr.IS_MAINTAIN_VAL as isMaintainVal, pr.END_REFER_INCOME_MONEY as endReferIncomeMoney, pr.END_REFER_INCOME_RATE as endReferIncomeRate, ");
        sql.append(" pr.REFER_INCOME_MONEY as referIncomeMoney, pr.move_up_rmk as moveUpRmk,pr.rmk as rmk,  pr.charge_money as chargeMoney, ");
        sql.append("  pr.sub_fee_money as subFeeMoney, pr.attachment_id as attachmentId, pr.is_guaranteed as isGuaranteed,pr.capital_source as capitalSource,");
        sql.append(" pr.daily_net_val as dailyNetVal, pr.portion as portion,");
        sql.append(" pr.buy_date as buyDate, ");
        sql.append(" rc.rcount,pn.ncount ");
        sql.append(" from cho_product_req pr ");
        sql.append(" LEFT JOIN (SELECT COUNT(*) AS rcount, req_id FROM CHO_PRODUCT_REDEEM R WHERE r.status != -2 GROUP BY (R.REQ_ID)) rc ON rc.req_id = pr.id ");
        sql.append(" LEFT JOIN (SELECT COUNT(*) AS ncount, req_id FROM Cho_Product_Netval n GROUP BY (n.req_id)) pn ON pn.req_id = pr.id ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" left join sys_corp corp on corp.id = pr.corp_id ");
        sql.append(" left join bt_bank_type bt on bt.id = pr.bank_Type_Id ");
        sql.append(" left join bt_currency cur on cur.id = pr.cur_id");
        sql.append(" where 1 = 1 ");
        sql.append(" and pr.status = '95' ");
        sql.append(" AND rcount IS NULL AND (ncount IS NULL OR ncount = 1) ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoProductReqVo.class, "pr", false);
    }
}
