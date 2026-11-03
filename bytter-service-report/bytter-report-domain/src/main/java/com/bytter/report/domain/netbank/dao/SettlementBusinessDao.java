package com.bytter.report.domain.netbank.dao;
import com.bytter.report.domain.netbank.vo.SettlementBusinessVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.Date;
/**
 * 结算业务统计表Dao
 *
 * @author qijh 2022-11-30
 */
@Repository
public class SettlementBusinessDao extends BaseDao<SettlementBusinessVO, String> {
    /**
     * 查询日期条件拼接
     * @param dataTablesInput
     * @param filedName 字段名称
     * @return
     */
    public String getSqlWhereBySearchDate(DataTablesInput dataTablesInput, String filedName){
        StringBuilder sql = new StringBuilder();
        //查询时间
        String gedSearchDate = (String) dataTablesInput.getFormdata().get("ged_searchDate");
        String ledSearchDate = (String) dataTablesInput.getFormdata().get("led_searchDate");
        if(StringUtils.isNotBlank(gedSearchDate)){
            Date searchDateBegin  = DateUtils.parse(gedSearchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            sql.append(" and " + filedName + " >= bt_to_date('").append(DateUtils.format(searchDateBegin,DateUtils.PATTERN_CLASSICAL_SIMPLE)).append("','yyyy-MM-dd') ");
        }
        if(StringUtils.isNotBlank(ledSearchDate)){
            Date searchDateEnd  = DateUtils.parse(ledSearchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            sql.append(" and " + filedName + " <= bt_to_date('").append(DateUtils.format(searchDateEnd,DateUtils.PATTERN_CLASSICAL_SIMPLE)).append("','yyyy-MM-dd') ");
        }
        return sql.toString();
    }
    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        //网点
        String netId = (String) dataTablesInput.getFormdata().get("netId");
        if(StringUtils.isNotBlank(netId)){
            joinSql+=" and c.net_id = '" + netId + "'";
        }
        //单位
        String corpId = (String) dataTablesInput.getFormdata().get("corpId");
        if(StringUtils.isNotBlank(corpId)){
            joinSql+=" and c.corp_Id = '" + corpId + "'";
        }
        sql.append("select t.voucherTypeId,t.curId,sum(tallyNumbers) as tallyNumbers,sum(tallyMoney) as tallyMoney from (");
        //1.日结前
        sql.append(" select d.id as voucherTypeId,c.cur_id as curId,count(*) as tallyNumbers,sum(b.debit_money + b.credit_money) as tallyMoney ");
        sql.append(" from fc_accbook a left join fc_accbook_dtl b on a.id = b.book_id ");
        sql.append(" left join fc_acc c on b.account = c.blnctr_acc ");
        sql.append(" left join fc_voucher_type d on a.voucher_type = d.id ");
        sql.append("  where a.voucher_state = 'N' ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "a.tally_date"));
        sql.append(" group by d.id, c.cur_id");
        sql.append(" union all ");
        //2.日结后
        sql.append(" select d.id as voucherTypeId,c.cur_id as curId,count(*) as tallyNumbers,sum(b.debit_money + b.credit_money) as tallyMoney ");
        sql.append(" from fc_accbook_blned a left join fc_accbook_blned_dtl b on a.id = b.book_id ");
        sql.append(" left join fc_acc c on b.account = c.blnctr_acc ");
        sql.append(" left join fc_voucher_type d on a.voucher_type = d.id ");
        sql.append("  where a.voucher_state = 'N' ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "a.tally_date"));
        sql.append(" group by d.id, c.cur_id");
        sql.append(" ) t where 1=1");
        //币种
        String curId = (String) dataTablesInput.getFormdata().get("curId");
        if(StringUtils.isNotBlank(curId)){
            sql.append(" and t.curId = '" + curId + "'");
        }
        //付款方式
        String voucherTypeIdStr = (String) dataTablesInput.getFormdata().get("voucherTypeId");
        if(StringUtils.isNotBlank(voucherTypeIdStr)){
            String[] voucherTypeIds = voucherTypeIdStr.split(CommonConstant.COMMA_SPLIT);
            sql.append(" and ").append(SqlUtil.getInSql(Arrays.asList(voucherTypeIds), "t.voucherTypeId", true));
        }
        sql.append(" group by voucherTypeId, curId");
        return getDynamicBySQL(dataTablesInput, sql.toString(), SettlementBusinessVO.class, "t", false);
    }
}
