package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.vo.TpBillJobVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author 郑刚
 * @version 1.0
 * @Description 商户信息对账单Dao
 * @date 2019-09-30 15:43:05
 */
@Repository
public class TpBillJobDao extends BaseDao<T, String> {
    /**
     * 商户信息对账单信息查询列表
     *
     * @param input        前端输入参数
     * @param corpAuthList 有权限的单位集合
     * @return DataTablesOutput    返回类型
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" j.bill_date as billDate,j.create_time as createTime,j.id,m.corp_id as corpId, ");
        sql.append(" m.PAY_TYPE as PAYTYPE,m.merchant_no as merchantNo,j.status,mv.var_value_str as appId ");
        sql.append(" from tp_bill_job j ");
        sql.append(" left join tp_merchant m on j.merchant_no = m.merchant_no and m.valid_sign = :validSign ");
        sql.append(" left join tp_merchant_var mv on mv.var_code in ( :appIds ) and mv.mch_id = m.id ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appIds", TtPayConstant.APPIDS);
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        if (corpAuthList == null || corpAuthList.size() < 1) {
            sql.append(" and 1!=1 ");
        } else {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "m.corp_id", params));
        }
        return getDynamicBySQL(input, sql.toString(), TpBillJobVO.class, "", false, params);
    }
}
