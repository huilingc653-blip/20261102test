package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.BisAccDtl;
import com.bytter.biz.erp.vo.request.BankAccDtlQueryReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author fangcf
 * @date 2026-05-11
 */
@Repository
public class ErpBisAccDtlDao extends BaseDao {

    /**
     * 根据条件查询银行明细
     *
     * @param reqVo 过滤条件vo
     * @return List<BisAccDtl> 银行明细
     */
    public List<BisAccDtl> listBisAccDtl(BankAccDtlQueryReqVo reqVo) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from BIS_ACC_DTL bad ");
        sql.append(" where bad.tenant_id = :tenantId and bad.bank_acc = :bankAcc ");
        if (StringUtils.isNotEmpty(reqVo.getCdSign())) {
            sql.append(" and bad.cd_sign = :cdSign ");
            params.put("cdSign", reqVo.getCdSign());
        }
        sql.append(" and bad.trans_time >= bt_to_date(:beginDate, 'yyyy-MM-dd') and bad.trans_time < bt_to_date(:endDate, 'yyyy-MM-dd') ");
        sql.append(" order by bad.trans_time ");

        params.put("tenantId", reqVo.getTenantId());
        params.put("bankAcc", reqVo.getBankAcc());
        params.put("beginDate", reqVo.getBeginDate());
        // 结束日期要+1天
        String endDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.parseDate(reqVo.getEndDate()), 1));
        params.put("endDate", endDate);

        return this.listBySQLAliasToBean(sql.toString(), params, BisAccDtl.class);
    }
}
