package com.bytter.credit.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationAdd;
import com.bytter.credit.domain.vo.RatBkrationAddListVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 授信追加信息表
 * @date 2017-07-31 16:00:15
 */
@Repository
public class RatBkrationAddDao extends BaseDao<RatBkrationAdd, String> {
    /**
     * 功能介绍 : 根据授信单号查询所有授信分配数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @param corpAuthSql
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput queryRationAddData(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rd.id as id,rd.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rd.add_code as addCode,rd.flag as flag, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rd.money as money, ")
                .append(" rd.create_date as createDate ")
                .append(" from rat_bkration_add rd, RAT_BKRATION rb")
                .append(" where rd.rat_code = rb.id and rb.status = 95 and rb.rat_type = '0' and rd.status <> -2 ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationAddListVO.class, null, false);
    }

    /**
     * 功能介绍 : 根据授信单号查询所有授信分配数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput queryRationAddApproveData(DataTablesInput input, String businessSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append("select rd.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rd.add_code as addCode,rd.flag as flag, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rd.money as money, ")
                .append(" rd.create_date as createDate ")
//                .append(" from rat_bkration_add rd, RAT_BKRATION rb, sys_corp sc,bt_bank bb,bt_currency bc")
//                .append(" where rd.rat_code = rb.id and rd.corp_id = sc.id and rb.bank_code = bb.sys_bank_code ")
//                .append(" and rb.cur_code = bc.id and rb.status = 95 and rd.status <> -2 ");
                .append(" from rat_bkration_add rd, RAT_BKRATION rb ")
                .append(" where rd.rat_code = rb.id and rb.status = 95 and rb.rat_type = '0' and rd.status <> -2");
        if (EmptyUtils.isNotEmpty(businessSql)) {
            sql.append(" and " + businessSql);
        }
        // sql.append(getWhereCondition(input, new RatBkrationVO(), null, false));
        // params.putAll(input.getSearchValues());
        return getDynamicBySQL(input, sql.toString(), RatBkrationAddListVO.class, null, false);
    }

    /**
     * 功能介绍 : 查询集团授信可追加数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput queryCpRationAddData(DataTablesInput input, String... corpAuthSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rb.rat_mode as ratMode, rb.rat_type as ratType,")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money, ")
                .append(" rd.corp_Id as disCorp,rd.corp_name as disCorpName,rd.id as disCode ")
                .append(" from RAT_BKRATION rb,rat_bkration_dis rd ")
                .append(" where rd.rat_code = rb.id and rb.status = 95 and rb.rat_type = '0' and rd.status in (95,-3) ");

        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        sql.append(" and rd.distribute_Way <> :disWay ");
        params.put("disWay", RationConstant.DIS_TYPE_AUTO);// 非自主授信的分配明细
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false, DynamicallyObject.getInstance(params));
    }

    /**
     * 功能介绍 : 查询授信可占用数据（已分配）。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput queryApplyRationAddData(DataTablesInput input, String corpId, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rb.rat_mode as ratMode, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money, ")
                .append(" rbd.corp_Id as disCorp, rbd.corp_name as disCorpName, rbd.id as disCode ")
                .append(" from RAT_BKRATION rb,rat_bkration_dis rbd ")
                .append(" where rbd.rat_code = rb.id ")
                .append(" and rb.status = 95 and rb.rat_type = '0' and rbd.corp_Id = :corpId ")
                .append(" and exists ( select 1 from rat_bkration_dis rd where rd.rat_code = rb.id  ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(corpId)) {
            sql.append(" and rd.corp_Id = :corpId ");
            params.put("corpId", corpId);
        }
        sql.append(" and rd.status = 95 ) ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false,
                DynamicallyObject.getInstance(params));
    }

    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String status, String flag) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(*) from rat_bkration_add where corp_id=:corpId and rat_type=0 ");
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in (:status) ");
            params.put("status", status.split(","));
        } else {
            sql.append(" and status<>-2 ");
        }
        if (EmptyUtils.isNotEmpty(flag)) {
            sql.append(" and flag =:flag ");
            params.put("flag", flag);
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
}
