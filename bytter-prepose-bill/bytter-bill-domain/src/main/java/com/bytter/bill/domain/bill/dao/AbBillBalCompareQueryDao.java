package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.vo.AbBillBalCompareDetailedVO;
import com.bytter.bill.domain.bill.vo.AbBillBalCompareVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: dao
 * @Description: 票据余额对比分析
 * @author zhenghj
 * @date 2020-01-15
 * @version 1.0
 *
 */
@Repository
public class AbBillBalCompareQueryDao extends BaseDao<AbBillBalCompareVO,String> {
    /**
     * 票据余额对比分析数据
     * @param input
     * @param params
     * @param sql
     * @return
     */
    public DataTablesOutput getDataTable(DataTablesInput input,Map<String,Object> params,String sql){
        return getDynamicBySQL(input, sql, AbBillBalCompareVO.class, "", false,params);
    }
    /**
     * 票据余额对比分析明细数据
     *
     * @param dataTablesInput 查询条件
     * @return 返回列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listBalCompareDetailed(DataTablesInput dataTablesInput) {
        Map<String,Object> params=new HashMap<>();
        Date statisticsMonth = new Date();
        if(StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("statisticsMonth"))){
            statisticsMonth = DateUtils.str2Date(dataTablesInput.getFormdata().get("statisticsMonth").toString(),DateUtils.date_sdf());
        }
        dataTablesInput.getFormdata().remove("statisticsMonth");
        StringBuilder sql = new StringBuilder("select a.orig_no as origNo,b.bank_acc as bankAcc,banktype.type_name as bankType,")
                .append("a.bill_type as billType,a.bill_flag as billFlag,a.start_date as startDate, a.end_date as endDate," )
                .append("a.bill_money as billMoney,a.payer,a.payee,a.acceptor ");
        sql.append(" from ab_voucherinfo_vice b ")
                .append(" inner join ab_voucherinfo a on a.id = b.vid " )
                .append(" inner join ab_accrecords l on l.vvid=b.id and l.status=95  and l.opt_status=19  and l.brief_id in (9003,9010,9012,9019,6019,6001,6212)")
                .append(" left join ab_accrecords l2 on l.id=l2.lastid and l2.status=95  and l2.opt_status=19  and l2.brief_id in (010,020,021,018,6004,6011)")
                .append(" left join ab_accrecords l3 on l2.id=l3.lastid and l3.status=95  and l3.opt_status=19  and l3.brief_id in (011)");
//                .append(" left join bt_bank_acc acc on b.bank_acc=acc.bank_acc ")
//                .append(" left join  bt_bank_type banktype on acc.bank_type_id = banktype.id");
        sql.append(" where  b.type=1 ");
        sql.append(" and (l.business_date<=:statisticsMonth and (l2.id is null or (:statisticsMonth<l2.business_date or (l2.brief_id=6011 and (l3.id is null or :statisticsMonth<l3.business_date))))) ");
        params.put("statisticsMonth",statisticsMonth);
        DataTablesOutput dataOutInfo = getDynamicBySQL(dataTablesInput, sql.toString(),AbBillBalCompareDetailedVO.class,"",false,params);
        return dataOutInfo;
    }
    /**
     * 获取指定日期到期金额
     * @param params
     * @return
     */
    public BigDecimal appointeDayMoney(List<String> corpAuthList, Map<String, Object> params){
    	StringBuilder sql = new  StringBuilder();
		sql.append("SELECT bt_nvl_num (SUM(a.bill_money), 0) AS totalMoney ")
				.append("FROM ab_voucherinfo_vice b ")
				.append("INNER JOIN ab_voucherinfo a ON a.id = b.vid ")
				.append("INNER JOIN ab_accrecords l ON l.vvid = b.id ")
//				.append("LEFT JOIN sys_corp s  ON b.corp_id = s.id ")
				.append("WHERE b.valid_sign = 1 ")
				.append("AND a.end_date > bt_to_date (:timeBegin, 'yyyy-MM-dd') ")
				.append("AND a.end_date <= bt_to_date (:timeEnd, 'yyyy-MM-dd') ")
				.append("AND b.type = 1 ")
				.append("AND (( l.valid_sign = 1 AND l.status = 95 AND l.opt_status = 19 AND l.brief_id IN ( ")
				.append("9003,9010,9012,9018,9019,6019,6001,6212)) ")
				.append("OR (a.bill_flag = 11 AND l.send_sign = 1 AND l.brief_id IN (010, 011, 018, 020) ")
				.append("AND l.status = 95 AND l.opt_status IN (16, 17, 18, - 1))) ")
				.append("AND ").append(SqlUtil.getInSql(corpAuthList, "b.corp_id", true))
				.append(" GROUP BY b.corp_id ");
    	BigDecimal totalMoney = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
		return totalMoney==null?BigDecimal.ZERO:totalMoney;
    }
}
