package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.ErpBisAccBalView;
import com.bytter.biz.erp.vo.request.BankAccBalQueryReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author fangcf
 * @date 2026-05-12
 */
@Repository
public class ErpBisAccBalDao extends BaseDao {

    /**
     * 根据条件查询银行账户余额
     *
     * @param reqVo 过滤条件vo
     * @return List<ErpBisAccBalView> 银行账号余额
     */
    public List<ErpBisAccBalView> listBisAccBal(BankAccBalQueryReqVo reqVo) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" select v.SERIAL_ID as serialId, v.TENANT_ID as tenantId, v.BANK_ACC as bankAcc, v.BAL_DATE as balDate, ");
        sql.append(" v.BAL as bal, v.AVAIL_BAL as availBal, v.FREEZE_BAL as freezeBal, ");
        sql.append(" v.CORP_CODE as corpCode, v.CORP_NAME as corpName, v.CUR_CODE as curCode, v.CUR_NAME as curName ");
        sql.append(" from ERP_BIS_ACC_BAL_VIEW v ");
        sql.append(" where v.tenant_id = :tenantId and v.bank_acc = :bankAcc ");
        sql.append(" and v.bal_date >= bt_to_date(:beginDate, 'yyyy-MM-dd') and v.bal_date < bt_to_date(:endDate, 'yyyy-MM-dd') ");
        sql.append(" order by v.bal_date ");

        params.put("tenantId", reqVo.getTenantId());
        params.put("bankAcc", reqVo.getBankAcc());
        params.put("beginDate", reqVo.getBeginDate());
        // 结束日期要+1天
        String endDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.parseDate(reqVo.getEndDate()), 1));
        params.put("endDate", endDate);

        return this.listBySQLAliasToBean(sql.toString(), params, ErpBisAccBalView.class);
    }
}
