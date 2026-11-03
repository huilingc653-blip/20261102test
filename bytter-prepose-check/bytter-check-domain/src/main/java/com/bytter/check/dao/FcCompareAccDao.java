package com.bytter.check.dao;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.check.constant.FinacialConstant;
import com.bytter.check.dto.FcCompareAccDTO;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_COMPARE_ACC
* @author onlineGenerator
* @date 2017-06-02 17:21:47
* @version 1.0
*
*/
@Repository
public class FcCompareAccDao extends BaseDao<FcCompareAcc,String>{
    /**
    * <br>
    * <b>功能：</b> 删除中心对账单 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/6/3 0003 <br>
    */
    public int deleteCompareAcc(FcCompareAcc entity) {
        String sql = " delete from fc_compare_acc where id = :id and comp_acc = :compAcc and compare_sign = :compareSign "
                + " and compare_Sign in (:oldSign) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", entity.getId());
        params.put("compAcc", entity.getCompAcc());
        params.put("compareSign", entity.getCompareSign());
        params.put("oldSign", new String[]{FinacialConstant.COMPARE_SIGN_YES, FinacialConstant.COMPARE_SIGN_NO});
        return createSQLQuery(sql, params).executeUpdate();
    }

    public void deleteFcCompareAccByVoucherNoAndCloseDate(List<String> voucherNos, Date closeDate) {
        String sql = " delete from fc_compare_acc where tally_date = :closeDate and voucher_no in (:voucherNos) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("voucherNos", voucherNos);
        params.put("closeDate", closeDate);
        createSQLQuery(sql, params).executeUpdate();
    }

    public List<FcCompareAccDTO> getBalanceByCondition(List<FcCompareAccDTO> fcCompareAccDTOList) {
        //	2.select b.id, b.BALANCE from FC_COMPARE_ACC b where b.TALLY_DATE = :closeDate
        //		AND b.COMP_ACC = FC_ACCBOOK_DTL.ACCOUNT
        //		AND b.LINE_NO = FC_ACCBOOK_DTL.LINE_NO
        //		AND b.BALANCE <> FC_ACCBOOK_DTL.BALANCE
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.* from fc_compare_acc b where 1=1 ");
        if (CollectionUtils.isNotEmpty(fcCompareAccDTOList)){
            fcCompareAccDTOList.forEach(a -> {
                sql.append(" and b.tally_date = ").append(a.getTallyDate());
                sql.append(" and b.comp_acc = ").append(a.getCompAcc());
                sql.append(" and b.line_no = ").append(a.getLineNo());
                sql.append(" and b.balance <> ").append(a.getBalance());
            });
        }
        return listBySQLAliasToBean(sql.toString(), null, FcCompareAccDTO.class);
    }

    public List<FcCompareAccDTO> getFcCompareAccDTOBySql(String sql, Map<String, Object> params){
        return listBySQLAliasToBean(sql, params, FcCompareAccDTO.class);
    }

    /**
     * 功能介绍 : 按记账日期区间更新对账单余额<br>
     * <b>日期：</b>2022年4月29日<br>
     * @version 1.0
     * @author chenf
     */
    public void updateCompareAccBalance(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE FC_COMPARE_ACC ")
                .append(" SET BALANCE =:bal ")
                .append(" WHERE FC_COMPARE_ACC.VOUCHER_NO =:voucherNo ")
                .append(" AND FC_COMPARE_ACC.COMP_ACC =:compAcc ")
                .append(" AND FC_COMPARE_ACC.LINE_NO =:lineNo ")
                .append(" AND FC_COMPARE_ACC.TALLY_DATE >=:tallyStartDate ")
                .append(" AND FC_COMPARE_ACC.TALLY_DATE <:tallyEndDate ")
                .append(" AND FC_COMPARE_ACC.BALANCE !=:balance ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
