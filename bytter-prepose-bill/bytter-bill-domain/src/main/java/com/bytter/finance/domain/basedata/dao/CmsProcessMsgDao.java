package com.bytter.finance.domain.basedata.dao;
import com.bytter.finance.domain.basedata.entity.CmsProcessMsg;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: CMS_PROCESS_MSG
 * @author onlineGenerator
 * @date 2017-09-14 14:32:39
 * @version 1.0
 *
 */
@Repository
public class CmsProcessMsgDao extends BaseDao<CmsProcessMsg, String> {
    /**
     * <b>功能介绍：</b>查询所有日志<br>
     * <b>日期：</b>2017年9月14日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @param billCode
     *            多个用逗号隔开
     * @return
     */
    public DataTablesOutput listMsgData(String billCode, DataTablesInput input) {
        String[] billCodes = billCode.split(",");
        List<String> codes = new ArrayList<String>();
        for (int i = 0; i < billCodes.length; i++) {
            codes.add(billCodes[i]);
        }
        StringBuilder sql = new StringBuilder(
                "select create_Date,create_By,create_Name,message_Type,message from Cms_Process_Msg where 1=1  and ");
        sql.append(getSQLIn(codes, 900, "bill_Code"));
        sql.append(" order by create_Date desc ");
        // return listByHQL(hql.toString(), null);
        return getDynamicBySQL(input, sql.toString(), CmsProcessMsg.class, "", false);
    }
    /**
     * <b>功能介绍：</b>查询所有日志<br>
     * <b>日期：</b>2017年9月14日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @param billCode
     *            多个用逗号隔开
     * @return
     */
    public DataTablesOutput listMsgDataWithAsc(String billCode, DataTablesInput input) {
        String[] billCodes = billCode.split(",");
        List<String> codes = new ArrayList<String>();
        for (int i = 0; i < billCodes.length; i++) {
            codes.add(billCodes[i]);
        }
        StringBuilder sql = new StringBuilder(
                "select bill_code,create_Date,create_By,create_Name,message_Type,message from Cms_Process_Msg where 1=1  and ");
        sql.append(getSQLIn(codes, 900, "bill_Code"));
        sql.append(" order by create_Date ");
        // return listByHQL(hql.toString(), null);
        return getDynamicBySQL(input, sql.toString(), CmsProcessMsg.class, "", false);
    }
    /**
     * 获取业务数据ID
     *
     * @param billCode
     * @return
     */
    public List<Map<Object, Object>> getIdByBillCode(String billCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select v.id,v.ta from (  ");
        sql.append(" select s.id,'cms_loan_send' as ta  from cms_loan_send s where s.bill_code=:billCode ");
        sql.append(" union ");
        sql.append(" select i.id,'cms_loan_info' as ta from  cms_loan_info i where i.bill_code=:billCode ");
        sql.append(" union ");
        sql.append(" select id,'cms_loan_repay' as ta from cms_loan_repay where bill_code=:billCode ");
        sql.append(" union ");
        sql.append(" select id,'cms_loan_deferred' as ta from cms_loan_deferred where bill_code=:billCode ");
        sql.append(" union ");
        sql.append(" select id,'cms_provide_loan_info' as ta from cms_provide_loan_info where bill_code=:billCode ");
        sql.append(" union ");
        sql.append(" select id,'cms_provide_loan_send' as ta from cms_provide_loan_send where bill_code=:billCode ");
        sql.append(" union ");
        sql.append(" select id,'cms_provide_loan_repay' as ta from cms_provide_loan_repay where bill_code=:billCode ");
        sql.append(" union ");
        sql.append(
                " select id,'cms_provide_loan_deferred' as ta from cms_provide_loan_deferred where bill_code=:billCode ");
        sql.append(" union ");
        sql.append(
                " select id,'cms_provide_loan_abolish' as ta from cms_provide_loan_abolish where PLI_CODE=:billCode ");
        sql.append(" union ");
        sql.append(
                " select id,'cms_precative_loan_info' as ta from cms_precative_loan_info where bill_code=:billCode ");
        sql.append(" union ");
        sql.append(
                " select id,'cms_precative_loan_send' as ta from cms_precative_loan_send where bill_code=:billCode ");
        sql.append(" union ");
        sql.append(
                " select id,'cms_precative_loan_repay' as ta from cms_precative_loan_repay where bill_code=:billCode ");
        sql.append(" union ");
        sql.append(
                " select id,'cms_precative_loan_deferred' as ta from cms_precative_loan_deferred where bill_code=:billCode ");
        sql.append(" ) v ");
        Map<String, Object> map = new HashMap<>();
        map.put("billCode", billCode);
        List<Map<Object, Object>> list = this.listBySQL(sql.toString(), map);
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }
}
