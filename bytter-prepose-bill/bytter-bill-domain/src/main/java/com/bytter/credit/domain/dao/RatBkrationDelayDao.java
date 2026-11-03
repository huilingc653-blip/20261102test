package com.bytter.credit.domain.dao;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import org.springframework.stereotype.Repository;
import com.bytter.credit.domain.entity.RatBkrationDelay;
import com.bytter.credit.domain.vo.RatBkrationDelayVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 授信延期表
 * @date 2017-08-26 09:13:31
 */
@Repository
public class RatBkrationDelayDao extends BaseDao<RatBkrationDelay, String> {
    /**
     * 功能介绍 : 查询授信待延期数据。<br>
     * <b>日期：</b>2017年8月26日<br>
     *
     * @param corpAuthSql
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput listDelayOutputData(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ");
        sql.append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ");
        sql.append(" rb.cur_code as curCode,rb.cur_name as curName,rb.rat_mode as ratMode, rb.rat_type as ratType,");
        sql.append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money, ");
        sql.append(" rd.delayStatus as delayStatus,rd.delayDate as delayDate, rd.delayCode as delayCode ");
        sql.append(" from RAT_BKRATION rb ");
        sql.append(" left join (select t.id as delayCode,t.status as delayStatus,t.delay_date as delayDate, t.rat_code as ratCode ");
        sql.append(" from rat_bkration_delay t where t.status <> -2 and t.delay_sign = '0' ) rd  ");
        sql.append(" on rb.id = rd.ratCode ");
        sql.append(" where rb.status = 95 ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        Map<String, Object> params = input.getFormdata();
        if (null != params.get("itemCode") && StringUtils.isNotEmpty(params.get("itemCode").toString())) {
            List<String> itemCodeList = Arrays.asList(params.get("itemCode").toString().split(CommonConstant.COMMA_SPLIT));
            sql.append(" and rb.id in (select distinct rat_code from RAT_BKRATION_DETAIL where 1 = 1 ");
            sql.append(" and ").append(SqlUtil.getInSql(itemCodeList, "ITEM_CODE", true)).append(") ");
            input.getFormdata().remove("itemCode");
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false);
    }
    /**
     * 功能介绍 : 查询授信待延期数据。<br>
     * <b>日期：</b>2017年8月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput listDelayApproveData(DataTablesInput input, String businessSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 补充名称信息
        sql.append("select rd.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ");
        sql.append(" rd.begindate as beginDate,rd.enddate as endDate,rd.status as status, ");
        sql.append(" rd.delay_Date as delayDate,rd.create_date as createDate ");
//        sql.append(" from rat_bkration_delay rd, rat_bkration rb,sys_corp sc");
        sql.append(" from rat_bkration_delay rd, rat_bkration rb ");
        sql.append(" where rb.id = rd.rat_Code ");
//        sql.append(" and rb.corp_id = sc.id and rb.status = 95 ");
        sql.append(" and rb.status = 95 ");
        if (EmptyUtils.isNotEmpty(businessSql)) {
            sql.append(" and " + businessSql);
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationDelayVO.class, null, false);
    }
    /**
     * 功能介绍 : 获取延期历史记录。<br>
     * <b>日期：</b>2017年8月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDelay> getRationDelayHisList(String ratCode, String delaySign) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd from RatBkrationDelay rd where rd.ratCode = :ratCode ");
        hql.append(" and rd.status <> :status and rd.delaySign = :delaySign ");
        hql.append(" order by rd.createDate desc ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("status", RationConstant.STATUS_DELETE);
        params.put("delaySign", delaySign);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String status) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(*) from rat_bkration_delay where corp_id=:corpId ");
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in (:status) ");
            params.put("status", status.split(","));
        } else {
            sql.append(" and status<>-2 ");
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
}
