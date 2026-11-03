package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.vo.FexBankAccBalReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 外汇存量币种分布统计。
 *
 * @author qinl
 * @Description 外汇存量币种分布统计
 * @date 2020.03.19
 */
@Repository
public class FexBankAccBalReportDao extends BaseDao<FexBankAccBalReportVO, String> {

    /**
     * 根据日期查询境外账号余额
     * @return 银行余额列表
     */
    public List<FexBankAccBalReportVO> selectBalance(String balDate,String curId){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" a.cur_id,a.bal_date,a.bal as bal");
        sql.append(" FROM bis_acc_bal a");
        sql.append(" LEFT JOIN bt_bank_acc b ON b.bank_acc = a.bank_acc");
        sql.append(" LEFT JOIN bt_currency c on c.id = a.cur_id");
        sql.append(" WHERE 1 = 1");
        sql.append(" AND c.cur_code != '01'");
        sql.append(" and bt_to_char ( bal_date , 'YYYY-MM-DD' ) = :balDate ");

        Map<String,String> map = new HashMap<>();
        map.put("domestic","0");
        map.put("curCode","01");
        map.put("balDate",balDate);
        if(StringUtils.isNotBlank(curId)){
            sql.append(" and c.id = :curId ");
            map.put("curId",curId);
        }
        return listBySQLAliasToBean(sql.toString(),map,FexBankAccBalReportVO.class);
    }
}
