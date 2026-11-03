package com.bytter.credit.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.entity.RatBkrationAdjust;
import com.bytter.credit.domain.vo.RatBkrationAdjustVO;
import com.bytter.credit.domain.vo.RatBkrationDisVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 授信调剂数据访问层
 *
 * @author luxy Title: RatBkrationAdjustDao
 */
@Repository
public class RatBkrationAdjustDao extends BaseDao<RatBkrationAdjust, String> {
    /**
     * 根据条件查询授信录入调整数据
     *
     * @param input       查询条件
     * @param corpAuthSql 网点
     * @return 授信录入调整数据
     */
    public DataTablesOutput getRatInputAdjustList(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ra.id as id,ra.adjust_code as adjustCode,rb.corp_id as corpId,rb.bank_name as bankName,rb.cur_code as curCode,")
                .append(" rb.corp_name as corpName, rb.cur_name as curName,")
                .append(" ra.business_id  as businessId,ra.business_type  as businessType,ra.status as status,ra.create_date as createDate ")
                .append(" from rat_bkration rb,RAT_BKRATION_ADJUST ra")
                .append(" where rb.id=ra.business_id and ra.status <> -2 ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationAdjustVO.class, null, false);
    }

    /**
     * 根据条件查询授信分配调整数据
     *
     * @param input       输入条件
     * @param corpAuthSql 网点
     * @return 授信分配调整数据
     */
    public DataTablesOutput getRatDisAdjustList(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                        "select ra.id as id,ra.adjust_code as adjustCode,rd.corp_id as corpId,rb.bank_name as bankName,rb.cur_code as curCode,")
                //TODO 补充相关名称信息
                .append(" rd.corp_name as corpName,rb.cur_name as curName, ")
                .append(" ra.business_id  as businessId,ra.business_type  as businessType,ra.status as status,ra.create_date as createDate ")
                .append(" from rat_bkration rb,RAT_BKRATION_ADJUST ra,rat_bkration_dis rd ")
                .append(" where rd.id = ra.business_id and rb.id=rd.rat_code and ra.status <> -2");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationAdjustVO.class, null, false);
    }

    /**
     * 查询授信录入可调整数据
     *
     * @param input       查询条件
     * @param corpAuthSql 网点
     * @return 授信录入可调整数据
     */
    public DataTablesOutput ratBkInputListData(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.net_code as netCode,rb.corp_id as corpId,rb.rat_type as ratType,rb.corp_name as corpName, ");
        sql.append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ");
        sql.append(" rb.cur_code as curCode,rb.cur_name as curName,rb.rat_mode as ratMode, ");
        sql.append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money, ");
        sql.append(" rb.gua_contract as guaContract,rb.is_hang_guarantee as isHangGuarantee ");
        sql.append(" from RAT_BKRATION rb ");
        sql.append(" where rb.status <> -2 and rb.RAT_TYPE = '0' ");
        sql.append(" and rb.id in (SELECT rbd.rat_code FROM RAT_BKRATION_DIS rbd LEFT JOIN RAT_BKRATION_DIS_DETAIL rbdd ON rbdd.dis_code = rbd.id WHERE rbdd.comp_sign = 1)");
        sql.append(" and rb.id not in(select relief.rat_code  from rat_bkration_relief relief where relief.status in(0,-1,11))");
        sql.append(" and rb.id not in(select rba.rat_code from rat_bkration_add rba where  rba.status in(0,-1,11))");
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
     * 查询授信分配可调整数据
     *
     * @param input       查询条件
     * @param corpAuthSql 网点
     * @return 授信分配可调整数据
     */
    public DataTablesOutput ratBkDisListData(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rd.id as id,rb.rat_code as ratCode,rb.net_Code as netCode,rb.corp_id as ratCorpId,rb.corp_name as ratCorpName, ");
        sql.append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ");
        sql.append(" rb.cur_code as curCode,rb.cur_name as curName,rd.dis_Code as disCode, ");
        sql.append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rb.money as money, ");
        sql.append(" rd.corp_id as corpId,rd.distribute_Way as distributeWay,rd.corp_name as corpName ");
        sql.append(" from rat_bkration_dis rd, RAT_BKRATION rb ");
        sql.append(" where rd.rat_code = rb.id ");
        sql.append(" and rb.status <> -2 and rb.rat_type = '0' and rd.status = 95 ");// 授信分配审批通过的数据才可调整额度
        sql.append(" and rb.id not in(select relief.rat_code from rat_bkration_relief relief where relief.status in(0,-1,11))");
        sql.append(" and rb.id not in(select rba.rat_code from rat_bkration_add rba where  rba.status in(0,-1,11))");
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
        return getDynamicBySQL(input, sql.toString(), RatBkrationDisVO.class, null, false);
    }

    /**
     * 根据id删除授信调整信息
     *
     * @param id
     * @return
     */
    public int delRatAdjust(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM  RAT_BKRATION_ADJUST WHERE id=:id ");
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        return createSQLQuery(sql.toString(), map).executeUpdate();
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
        sql.append("select count(*) from RAT_BKRATION_ADJUST where corp_id=:corpId ");
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
