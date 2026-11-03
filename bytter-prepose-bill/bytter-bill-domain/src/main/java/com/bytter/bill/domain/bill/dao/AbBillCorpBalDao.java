package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbBillAccBal;
import com.bytter.bill.domain.bill.vo.AbBillCorpBalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: 票据单位余额Dao
 * @Description: AB_BILL_ACC_BAL
 * @author zhengg
 * @date 2017-09-28 19:20:01
 * @version 1.0
 *
 */
@Repository
public class AbBillCorpBalDao extends BaseDao<AbBillAccBal,String> {
    /**
     * 列表显示票据单位余额信息
     * @param input
     * @param  corpAuthList
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillCorpBal(DataTablesInput input, List<String> corpAuthList) {
        Map<String,Object> params=new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT BT_CONCAT3(d.corp_id,d.cur_id,d.bal_date) as id,");
        sql.append(" d.corp_id AS corpId,d.cur_id AS curId,d.bal_date AS baldate,");
        sql.append(" d.bal AS bal,d.avail_bal AS availbal,d.tiedup_bal AS tiedupbal,");
        sql.append(" d.spBal,d.spAvailBal,d.spTiedupBal,");
        sql.append(" d.ypBal,d.ypAvailBal,d.ypTiedupBal,");
        sql.append(" d.corpCode, d.corpName  ");
        sql.append(" from ");
        sql.append(" (select b.corp_id AS corp_id,b.cur_id AS cur_id,b.bal_date AS bal_date, b.CORP_CODE as corpCode, b.CORP_NAME as corpName, ");
        sql.append(" sum(b.bal) AS bal,sum(b.avail_bal) AS avail_bal,sum(b.tiedup_bal) AS tiedup_bal, ");
        sql.append(" sum(b.sp_bal) AS spBal,sum(b.sp_avail_bal) AS spAvailBal,sum(b.sp_tiedup_bal) AS spTiedupBal, ");
        sql.append(" sum(b.yp_bal) AS ypBal,sum(b.yp_avail_bal) AS ypAvailBal,sum(b.yp_tiedup_bal) AS ypTiedupBal ");
        sql.append(" FROM AB_BILL_ACC_BAL b ");
        if(input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" where (b.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.bank_acc", "bankAccs", params)).append(")");
        }
        sql.append(" group by b.corp_id,b.bal_date,b.cur_id,b.CORP_CODE,b.CORP_NAME) d");
//        sql.append(" left join sys_corp c on d.corp_Id=c.id ");
//        sql.append(" left join bt_currency r on d.cur_Id=r.id ");
        sql.append(" where 1=1 ");
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "d.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return this.dealDynamicBySQL(input, sql.toString(), AbBillCorpBalVO.class, "", false,params);
    }
}
