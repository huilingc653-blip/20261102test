package com.bytter.biz.wage.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.wage.entity.NisWageReleaseConfig;
import com.bytter.biz.wage.vo.NisWageReleaseBatchUpdateVO;
import com.bytter.biz.wage.vo.NisWageReleaseConfigVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 代发工资下拨金额配置Dao
 *
 * @author loveCamille
 */
@Repository
public class NisWageReleaseConfigDao extends BaseDao<NisWageReleaseConfig, String> {

    /**
     * 查询表格泪飙
     * @author loveCamille
     * @param input 查询条件
     * @param corpAuthList 单位列表
     * @return {@link DataTablesOutput<NisWageReleaseConfigVO>}
    */
    public DataTablesOutput<NisWageReleaseConfigVO> dataTableQuery(DataTablesInput input, List<String> corpAuthList) {
        String sql =
                " SELECT " +
                        "    t1.id, " +
                        "    t2.ID AS corpId, " +
                        "    t2.CODE AS corpCode, " +
                        "    t2.NAME AS corpName, " +
                        "    t1.bank_acc AS bankAcc, " +
                        "    t1.bank_acc_name AS bankAccName, " +
                        "    t1.bank_code AS bankCode, " +
                        "    t1.bank_name AS bankName, " +
                        "    BT_TO_CHAR(t1.release_date,'yyyy-mm-dd') AS releaseDate, " +
                        "    t1.release_amount AS releaseAmount, " +
                        "    t1.last_month_release_amount AS lastMonthReleaseAmount, " +
                        "    t1.total_release_money AS totalReleaseMoney, " +
                        "    t1.status AS status, " +
                        "    t1.confirm_id AS confirmId, " +
                        "    t1.confirm_name AS confirmName, " +
                        "    t1.confirm_time AS confirmTime, " +
                        "    t1.generate_require AS generateRequire, " +
                        "    t1.generate_time AS generateTime " +
                        " FROM " +
                        "    nis_wage_release_config t1 " +
                        "    LEFT JOIN sys_corp t2 ON t2.id = t1.corp_id " +
                        " WHERE" +
                        "    1 = 1 ";

        Map<String, Object> param = new HashMap<>();
        if (CollUtil.isNotEmpty(corpAuthList)) {
            sql += " AND t1.corp_id IN (:corpIds)";
            param.put("corpIds", corpAuthList);
        }
        Map<String, Object> formdata = input.getFormdata();
        if (ObjectUtil.isNotEmpty(formdata.get("leg_releaseAmount"))) {
            sql += " AND t1.release_amount <= :le_releaseAmount";
            param.put("le_releaseAmount", formdata.get("leg_releaseAmount"));
        }
        if (ObjectUtil.isNotEmpty(formdata.get("geg_releaseAmount"))) {
            sql += " AND t1.release_amount >= :ge_releaseAmount";
            param.put("ge_releaseAmount", formdata.get("geg_releaseAmount"));
        }
        if(formdata.get("eqs_releaseDate")!=null){
            String releaseDate = formdata.get("eqs_releaseDate").toString();
            if (StrUtil.isNotBlank(releaseDate)) {
                sql += " AND t1.release_date >= BT_TO_DATE(:ged_releaseDate,'yyyy-mm-dd') ";
                sql += " AND t1.release_date < BT_TO_DATE(:led_releaseDate,'yyyy-mm-dd') ";
                String startDate = releaseDate + "-01";
                param.put("ged_releaseDate", startDate);
                DateTime nextMonth = DateUtil.offsetMonth(DateUtil.parse(startDate, DatePattern.NORM_DATE_FORMAT), 1);
                param.put("led_releaseDate", DateUtil.format(nextMonth, DatePattern.NORM_DATE_FORMAT));
            }
        }

        return getDynamicBySQL(input, sql, NisWageReleaseConfigVO.class, "", false, param);
    }

    public List<BtBankAccDTO> getBankAccQuery(List<String> corpIdList) {
        String sql =
                "   SELECT  " +
                        "  t2.id, " +
                        "  t2.BANK_ACC AS bankAcc, " +
                        "  t2.ACC_NAME AS accName, " +
                        "  t2.BANK_CODE  AS bankCode, " +
                        "  t2.BANK_NAME  AS bankName, " +
                        "  t3.CUR_ID AS curId, " +
                        "  t4.CUR_NAME AS curName " +
                        " FROM " +
                        "  bt_bank_type t1 " +
                        "  LEFT JOIN bt_bank_acc t2 ON t2.BANK_TYPE_ID = t1.ID " +
                        "  LEFT JOIN bt_bank_acc_cur t3 ON t3.BANK_ACC_ID = t2.ID " +
                        "  LEFT JOIN bt_currency t4 ON t4.ID = t3.CUR_ID " +
                        " WHERE " +
                        "  1 = 1 " +
                        "  AND t1.BANK_TYPE != '8500'  " +
                        "  AND t2.ACC_TYPE != '04' ";
        sql += " AND " + getSQLIn(corpIdList, 1000, "t2.corp_id");
        return listBySQLAliasToBean(sql, new HashMap<>(), BtBankAccDTO.class);
    }

    public void batchDelete(List<String> idList) {
        String currentMonthDate = DateUtil.format(DateTime.now(), DatePattern.NORM_MONTH_FORMAT) + "-01";
        String nextMonthDate = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(currentMonthDate, DatePattern.NORM_DATE_FORMAT), 1), DatePattern.NORM_DATE_FORMAT);
        // 只能删除当月、状态为暂存的数据
        String sql =
                " DELETE FROM " +
                "   NisWageReleaseConfig " +
                " WHERE " +
                "   1 = 1 " +
                "   AND status = 0 " +
                "   AND release_date >= '" + currentMonthDate + "' " +
                "   AND release_date < '"  + nextMonthDate + "' ";
        sql +=  "   AND " + getSQLIn(idList, 1000, "id");
        batchExecute(sql, Collections.emptyMap());
    }

    public NisWageReleaseConfig getOne(String corpId, String bankAcc, String monthDate) {
        String nextMonthDate = DateUtil.offsetMonth(DateUtil.parse(monthDate, DatePattern.NORM_DATE_FORMAT), 1).toDateStr();
        String sql =
                " SELECT " +
                "   id, corp_id AS corpId, bank_acc AS bankAcc " +
                " FROM " +
                "   nis_wage_release_config " +
                " WHERE " +
                "   1 = 1 " +
                "   AND corp_id = :corpId "
                + " AND bank_acc = :bankAcc "
                + " AND BT_TO_CHAR(release_date, 'yyyy-MM-dd') >= :monthDate "
                + " AND BT_TO_CHAR(release_date, 'yyyy-MM-dd') < :nextMonthDate ";

        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        param.put("bankAcc", bankAcc);
        param.put("monthDate", monthDate);
        param.put("nextMonthDate", nextMonthDate);
        return getBySQLAliasToBean(sql, param, NisWageReleaseConfig.class);
    }

    public void batchUpdateAmount(NisWageReleaseBatchUpdateVO updateVO) {
        List<String> idList = Arrays.asList(updateVO.getIds().split(","));
        String sql =
                " UPDATE NisWageReleaseConfig SET ";
        if (updateVO.getType() == 0) {
            sql += " release_amount = release_amount + " +  updateVO.getAmount();
        } else {
            sql += " release_amount = release_amount * ( 1 + " + updateVO.getRatio() + " ) ";
        }
        sql += " WHERE 1 = 1 AND " + getSQLIn(idList, 1000, "id");
        sql += " AND status != 1 ";
        batchExecute(sql, Collections.emptyMap());
    }
}