package com.bytter.biz.wage.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.wage.vo.NisWageAllocationSyncVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 定时任务-代发下拨金额自动同步
 * 代发工资取值逻辑：
 * 1、期望付款日期为上个月，如2025.10.01-2025.10.31；
 * 2、按照付款账号进行代发金额汇总，统计支付成功的金额；
 * 3、汇总金额根据“代发工资浮动比率配置”进行调整；
 * 4、最终计算出来的金额按照万元向上进位；
 * 5、同步时相关字段默认值如下：
 * 1)状态：已确认；
 * 2)确认人：默认为admin；
 * 3)确认时间：默认为当前存入的时间，到时分秒；
 * 4)本月单据累计下拨金额：0；
 *
 * @author fangcf
 * @date 2025-11-21
 */
@Repository
public class AutoSyncWageAllocationDao extends BaseDao<NisWageAllocationSyncVO, String> {

    /**
     * 获取下周一至周日到期应付票据，挂接保证金非100%
     *
     * @param startDateStr 开始日期
     * @param endDateStr   结束日期
     */
    public List<NisWageAllocationSyncVO> listWages(String startDateStr, String endDateStr) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select nw.CORP_ID as corpId, nw.PAYER_ACC as bankAcc, nw.PAYER_ACC_NAME as bankAccName, nw.CUR_ID as curId, ");
        sql.append(" bba.BANK_CODE as bankCode, bba.BANK_NAME as bankName, nwd.WAGES_VERIFY as wagesVerify ");
        sql.append(" from nis_wage nw ");
        sql.append(" inner join nis_wage_detail nwd on nwd.WAGE_ID = nw.ID ");
        sql.append(" left join bt_bank_acc bba on bba.BANK_ACC = nw.PAYER_ACC ");
        sql.append(" left join sys_corp sc on sc.ID = nw.CORP_ID ");
        // 有效单位和有效账户
        sql.append(" where sc.STATUS = 1 and bba.VALID_SIGN = '1' and bba.STATUS = 95 ");
        // 审批通过且支付成功
        sql.append(" and nw.STATUS >= 95 and nwd.VOUCHER_STAT = '0' ");
        // 获取时间范围为：期望支付日期为上个月
        sql.append(" and BT_TO_CHAR(nw.WISH_PAYDAY, 'yyyy-MM-dd') >= :startDateStr and BT_TO_CHAR(nw.WISH_PAYDAY, 'yyyy-MM-dd') <= :endDateStr ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("startDateStr", startDateStr);
        params.put("endDateStr", endDateStr);
        return this.listBySQLAliasToBean(sql.toString(), params, NisWageAllocationSyncVO.class);
    }

    /**
     * 根据下拨日期，查询代发工资下拨金额配置表。（单位+银行账号+月份三者合起来只能有一条唯一数据）
     *
     * @param ym 年月
     * @return 代发工资下拨金额配置表
     */
    public List<NisWageAllocationSyncVO> listByReleaseDate(String ym) {
        String sql = " select c.CORP_ID as corpId, c.BANK_ACC as bankAcc from nis_wage_release_config c where DATE_FORMAT(c.RELEASE_DATE, '%Y-%m') = :ym ";

        Map<String, Object> params = Maps.newHashMap();
        params.put("ym", ym);
        return this.listBySQLAliasToBean(sql, params, NisWageAllocationSyncVO.class);
    }
}