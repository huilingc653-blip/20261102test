package com.bytter.biz.report.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.report.vo.AutoBillBalFreezeVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 定时任务-自动新增票据计划冻结申请
 * 取值逻辑：获取下周一至周日到期应付票据，挂接保证金非100%。按照票据账号和到期日提前两个工作日进行汇总
 *
 * @author fangcf
 * @date 2025-10-22
 */
@Repository
public class AutoBillBalFreezeDao extends BaseDao<AutoBillBalFreezeVO, String> {

    /**
     * 获取下周一至周日到期应付票据，挂接保证金非100%
     *
     * @param startDateStr 开始日期
     * @param endDateStr   结束日期
     */
    public List<AutoBillBalFreezeVO> listBill(String startDateStr, String endDateStr) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select avv.CORP_ID as corpId, su.ID as createBy, sctln.LOGIN_NAME as loginName, ");
        sql.append(" avv.BANK_ACC as bankAcc, acc.ID as bankAccId, acc.BANK_TYPE_ID as bankTypeId, bbt.TYPE_NAME as bankTypeName, ");
        sql.append(" BT_TO_CHAR(av.END_DATE, 'yyyy-MM-dd') as endDate, sum(BT_NVL_NUM(av.BILL_MONEY, 0)) as billMoney ");
        sql.append(" from ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        // 业务类型为：出票登记
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id = '001' ");
        sql.append(" left join bt_bank_acc acc on acc.BANK_ACC = avv.BANK_ACC ");
        sql.append(" left join sys_corp sc on sc.id = avv.CORP_ID ");
        // 单位代码与登录账号关联表：一个单位只对应一个用户
        sql.append(" left join sys_corp_to_login_name sctln on sctln.CODE = sc.CODE ");
        sql.append(" left join sys_user su on su.LOGIN_NAME = sctln.LOGIN_NAME  ");
        sql.append(" left join bt_bank_type bbt on bbt.id = acc.BANK_TYPE_ID ");
        // 应付票据
        sql.append(" where avv.type = '0' ");
        sql.append(" and av.orig_no is not null and av.orig_no <> '' ");
        // 有效票据
        sql.append(" and avv.VALID_SIGN = '1' and a.opt_status = '19' and a.status = 95 ");
        // 必须挂接了保证金且保证金非100%
//        sql.append(" and avv.BAIL_MODEL = '1' and avv.BAIL_PERCENT <> 1 ");
        sql.append(" and avv.BAIL_PERCENT <> 1 ");
        // 有效且为票据专户
        sql.append(" and acc.VALID_SIGN = '1' and acc.STATUS = 95 and acc.ELECTRIC_BILL = '1' ");
        // 排除业务类型为应付退票的数据
        sql.append(" and not exists ( select 1 from ab_accrecords ba where avv.id = ba.vvid and ba.brief_id in ('004','6120') and ba.opt_status = '19' and ba.status >= 95 ) ");
        // 获取时间范围为：下周一至下周日
        sql.append(" and BT_TO_CHAR(av.end_date, 'yyyy-MM-dd') >= :startDateStr and BT_TO_CHAR(av.end_date, 'yyyy-MM-dd') <= :endDateStr ");
        // 同账号同一天到期日期则汇总
        sql.append(" group by avv.CORP_ID, su.ID, sctln.LOGIN_NAME, avv.BANK_ACC, acc.ID, acc.BANK_TYPE_ID, bbt.TYPE_NAME, BT_TO_CHAR(av.end_date, 'yyyy-MM-dd') ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("startDateStr", startDateStr);
        params.put("endDateStr", endDateStr);
        return this.listBySQLAliasToBean(sql.toString(), params, AutoBillBalFreezeVO.class);
    }
}