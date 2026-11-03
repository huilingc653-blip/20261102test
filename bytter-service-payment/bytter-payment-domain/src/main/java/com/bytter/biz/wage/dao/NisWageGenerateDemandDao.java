package com.bytter.biz.wage.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 工资自动生成需求清单
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/11/23
 */
@Repository
public class NisWageGenerateDemandDao extends BaseDao {

    /**
     * 获取工资配置表待生成需求清单数据
     * @param demandDate
     * @return TODO
     */
    public List<NisDemandVO> listSalaryConfigData(Date demandDate,List<String> accIds) {
        Map<String, Object> params = new HashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT c.corp_id corpId,c.bank_acc bankAcc,acc.ID bankAccId,c.bank_acc_name accName,c.bank_name bankName,");
        sql.append("'04' demandType,c.release_amount demandMoney,acur.PRENT_ACC_ID superBankAccId,c.id payBillId ");
        sql.append("FROM nis_wage_release_config c ");
        sql.append("JOIN bt_bank_acc acc ON c.bank_acc = acc.BANK_ACC ");
        sql.append("LEFT JOIN bt_bank_acc_cur acur ON  acc.ID = acur.BANK_ACC_ID ");
        sql.append("WHERE c.`status` = 1 AND c.generate_require = 0 ");
        sql.append("AND DATE_FORMAT(c.release_date, '%Y-%m') = :demandMonth ");
        sql.append(" and ").append(SqlUtil.getInSql(accIds,"acc.id",true));
        params.put("demandMonth", DateUtils.format(demandDate,"yyyy-MM"));
        return this.listBySQLAliasToBean(sql.toString(), params, NisDemandVO.class);
    }

    /**
     * 获取工资表待生成需求清单数据
     * @param demandDate
     * @return TODO
     */
    public List<NisDemandVO> listSalaryTableData(Date demandDate,List<String> accIds) {
        Map<String, Object> params = new HashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT w.CORP_ID  corpId,w.PAYER_ACC bankAcc,acc.ID bankAccId,w.PAYER_ACC_NAME accName,acc.BANK_NAME bankName,");
        sql.append("'05' demandType,w.WAGES demandMoney,acur.PRENT_ACC_ID superBankAccId,w.ID payBillId,w.WISH_PAYDAY payBillDate,w.WAGE_NO payBillNo ");
        sql.append("FROM nis_wage w ");
        sql.append("JOIN bt_bank_acc acc ON w.PAYER_ACC = acc.BANK_ACC ");
        sql.append("LEFT JOIN bt_bank_acc_cur acur ON  acc.ID = acur.BANK_ACC_ID ");
        sql.append("WHERE w.`STATUS` != -2 AND DATE_FORMAT(w.WISH_PAYDAY, '%Y-%m') = :demandMonth  ");
        sql.append(" and ").append(SqlUtil.getInSql(accIds,"acc.id",true));
        params.put("demandMonth", DateUtils.format(demandDate,"yyyy-MM"));
        return this.listBySQLAliasToBean(sql.toString(), params, NisDemandVO.class);
    }

    /**
     * 根据单位ID账号日期获取工资配置表中已生成需求清单的数据
     * @param corpId
     * @param bankAcc
     * @param demandDate
     * @return TODO
     */
    public List<NisDemandVO> getSalaryConfigData(String corpId, String bankAcc, Date demandDate) {
        Map<String, Object> params = new HashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT c.corp_id corpId,c.bank_acc bankAcc,acc.ID bankAccId,c.bank_acc_name accName,c.bank_name bankName,");
        sql.append("'04' demandType,c.release_amount demandMoney,BT_NVL_NUM(c.total_release_money,0) demandAllocationMoney,c.id payBillId  ");
        sql.append("FROM nis_wage_release_config c ");
        sql.append("JOIN bt_bank_acc acc ON c.bank_acc = acc.BANK_ACC ");
        sql.append("WHERE c.`status` = 1 AND c.generate_require = 1 ");
        sql.append("AND DATE_FORMAT(c.release_date, '%Y-%m') = :demandMonth ");
        sql.append("AND c.corp_id = :corpId ");
        sql.append("AND c.bank_acc = :bankAcc ");
        params.put("demandMonth", DateUtils.format(demandDate,"yyyy-MM"));
        params.put("corpId", corpId);
        params.put("bankAcc", bankAcc);
        return this.listBySQLAliasToBean(sql.toString(), params, NisDemandVO.class);
    }

    /**
     * 根据ID更新工资配置表
     * 如果 demandMoney为空则更新 generate_require = 1, generate_time = now()
     * 否则 更新 total_release_money = total_release_money + demandMoney
     * @param configId
     * @param demandMoney
     * @return TODO
     */
    public void updateConfigGenerateRequire(String configId, BigDecimal demandMoney) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE nis_wage_release_config SET ");

        if (ObjectUtil.isEmpty(demandMoney)) {
            sqlBuilder.append("generate_require = 1, generate_time = now() ");
        } else {
            sqlBuilder.append("total_release_money = COALESCE(total_release_money, 0) + :demandMoney ");
            params.put("demandMoney", demandMoney);
        }
        sqlBuilder.append(" WHERE id = :configId");
        params.put("configId", configId);

        String sql = sqlBuilder.toString();
        this.createSQLQuery(sql, params).executeUpdate();
    }
}
