package com.bytter.biz.netbank.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.entity.NisRelatedParty;
import com.bytter.biz.netbank.vo.NisRelatedPartyReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 关联方dao
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/10/17
 */
@Repository
public class NisRelatedPartyDao extends BaseDao<NisRelatedParty, String> {
    public boolean checkCode(String relatedCode, String id) {
        Map<String,Object> params = new HashMap<>();
        String hql = "from NisRelatedParty where relatedCode = :relatedCode ";
        if (ObjectUtil.isNotEmpty(id)){
            hql += "and id != :id ";
            params.put("id", id);
        }
        params.put("relatedCode", relatedCode);
        List<NisRelatedParty> list = this.listByHQL(hql, params);
        if (list != null && list.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    public DataTablesOutput<NisRelatedPartyReportVO> reportDatatable(DataTablesInput dataTablesInput, List<String> corpIds) {
        Map<String, Object> params = new HashMap<>();
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        params.put("beginTime", DateUtils.getIntegralStartTime(DateUtils.parseDate(gedTransTime)));
        params.put("endTime", DateUtils.getIntegralEndTime(DateUtils.parseDate(ledTransTime)));


        // 使用 StringBuilder 构建 SQL 语句
        StringBuilder sqlBuilder = new StringBuilder();
        // 第一部分 支出部分
        sqlBuilder.append("SELECT d.ID, sc.`NAME` corpName, d.BANK_ACC bankAcc, acc.ACC_NAME bankAccName, ");
        sqlBuilder.append("acc.BANK_NAME bankName, d.OPP_ACC_NO oppAccNo, d.OPP_ACC_NAME oppAccName, ");
        sqlBuilder.append("d.OPP_ACC_BANK oppAccBank, d.TRANS_TIME transTime, d.CD_SIGN cdSign, d.AMT amt, ");
        sqlBuilder.append("n.bursar_bill_no gxBillNo, u.username createUserName, d.remark, d.uses purpose, ");
        sqlBuilder.append("d.postscript,sc.id corpId,acc.bank_type_id bankTypeId,d.bank_serial_no bankSerialNo ");
        sqlBuilder.append("FROM nis_bill_bank_serial_no nd ");
        sqlBuilder.append("JOIN nis_billhead n ON n.ID = nd.BILL_ID ");
        sqlBuilder.append("join bis_acc_dtl d ON nd.DTL_ID = d.ID ");
        sqlBuilder.append("LEFT JOIN bt_bank_acc acc ON d.BANK_ACC = acc.BANK_ACC ");
        // sqlBuilder.append("LEFT JOIN bt_bank_type bbt ON acc.BANK_TYPE_ID = bbt.ID ");
        sqlBuilder.append("LEFT JOIN sys_corp sc ON acc.CORP_ID = sc.ID ");
        sqlBuilder.append("LEFT JOIN sys_user u ON n.OP_CODE = u.LOGIN_NAME ");
        sqlBuilder.append("WHERE d.CD_SIGN = '1' AND d.TRANS_TIME BETWEEN :beginTime AND :endTime ");
        sqlBuilder.append("AND d.OPP_ACC_NAME IN (SELECT RELATED_NAME FROM nis_related_party) ");
        if (ObjectUtil.isNotEmpty(dataTablesInput.getFormdata().get("eqs_id"))) {
            sqlBuilder.append("and d.id = :id ");
            params.put("id", dataTablesInput.getFormdata().get("eqs_id"));
        }
        if (ObjectUtil.isNotEmpty(corpIds)){
            sqlBuilder.append(" and ").append(SqlUtil.getInSql(corpIds, "sc.id", true));
        }
                // UNION ALL 部分
        sqlBuilder.append("UNION ALL ");
                // 第二部分 收入部分
        sqlBuilder.append("SELECT d.ID, sc.`NAME` corpName, d.BANK_ACC bankAcc, acc.ACC_NAME bankAccName, ");
        sqlBuilder.append("acc.BANK_NAME bankName, d.OPP_ACC_NO oppAccNo, d.OPP_ACC_NAME oppAccName, ");
        sqlBuilder.append("d.OPP_ACC_BANK oppAccBank, d.TRANS_TIME transTime, d.CD_SIGN cdSign, d.AMT amt, ");
        sqlBuilder.append("'' gxBillNo, '' createUserName, d.remark, d.uses purpose,  ");
        sqlBuilder.append("d.postscript,sc.id corpId,acc.bank_type_id bankTypeId,d.bank_serial_no bankSerialNo ");
        sqlBuilder.append("FROM bis_acc_dtl d ");
        sqlBuilder.append("LEFT JOIN bt_bank_acc acc ON d.BANK_ACC = acc.BANK_ACC ");
        // sqlBuilder.append("LEFT JOIN bt_bank_type bbt ON acc.BANK_TYPE_ID = bbt.ID ");
        sqlBuilder.append("LEFT JOIN sys_corp sc ON acc.CORP_ID = sc.ID ");
        sqlBuilder.append("WHERE d.CD_SIGN = '0' AND d.TRANS_TIME BETWEEN :beginTime AND :endTime ");
        sqlBuilder.append("AND d.OPP_ACC_NAME IN (SELECT RELATED_NAME FROM nis_related_party) ");
        if (ObjectUtil.isNotEmpty(dataTablesInput.getFormdata().get("eqs_id"))) {
            sqlBuilder.append("and d.id = :id ");
            params.put("id", dataTablesInput.getFormdata().get("eqs_id"));
        }
        if (ObjectUtil.isNotEmpty(corpIds)){
            sqlBuilder.append(" and ").append(SqlUtil.getInSql(corpIds, "sc.id", true));
        }

        // 获取最终的 SQL 字符串
        StringBuilder finalSql = new StringBuilder();
        finalSql.append("select * from ( ");
        finalSql.append(sqlBuilder.toString());
        finalSql.append(") as t ");
        finalSql.append("where 1=1 ");


        return this.getDynamicBySQL(dataTablesInput, finalSql.toString(), NisRelatedPartyReportVO.class, "t", false, params);
    }
}
