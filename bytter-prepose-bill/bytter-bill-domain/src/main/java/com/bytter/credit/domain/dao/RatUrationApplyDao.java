package com.bytter.credit.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.credit.domain.vo.RatUrationDisBean;
import com.bytter.credit.dto.RatUrationApplyListDTO;
import org.springframework.stereotype.Repository;
import com.bytter.credit.domain.entity.RatUrationApply;
import com.bytter.credit.domain.vo.RatUrationListVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
/**
 * @Title: Dao
 * @Description: 授信占用申请表
 * @author onlineGenerator
 * @date 2017-08-19 11:36:28
 * @version 1.0
 *
 */
@Repository
public class RatUrationApplyDao extends BaseDao<RatUrationApply, String> {
    /**
     * 功能介绍 : 获取授信占用列表。<br>
     * <b>日期：</b>2017年8月19日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput queryURationApplyData(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru.id as id,ru.type as type,ru.corp_id as corpId,rd.corp_name as corpName, ");
        sql.append(" ru.rat_code as ratCode,ru.item_code as itemCode,rc.name as itemName, ru.rat_type as ratType, ");
        sql.append(" ru.item_flag as itemFlag,ru.cur_code as curCode,ru.cur_name as curName, ");
        sql.append(" ru.money as money,ru.open_money as openMoney,ru.begin_date as beginDate,ru.end_date as endDate, ");
        sql.append(" ru.status as status,ru.apply_code as applyCode,  ");
        sql.append(" case when ru.rat_credit_type is null then '1' else ru.rat_credit_type end as creditType ");
        sql.append(" from rat_uration_apply ru left join rat_bkration_dis rd on ru.dis_code = rd.id ");
        sql.append(" left join rat_item_custom rc on ru.item_code = rc.id ");
        sql.append(" where ru.status <> -2 ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatUrationListVO.class, null, false);
    }
    /**
     * 功能介绍 : 根据占用Id查询占用详情。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationApplyListDTO> queryRatUrationById(String urationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select ru.id as id,ru.type as type,ru.corp_id as corpId, rb.race_name  as billName,rb.id as billNameId, ");// rs.name
                                                                                                                             // as
                                                                                                                             // typeName,去掉系统授信品种.增加业务名称
        sql.append(
                " ru.rat_code as ratCode,ru.dis_code as disCode,ru.item_code as singItemCode,rc.name as singItemName, ");
        //TODO 补充币种名称
        sql.append(" ru.item_flag as itemFlag,ru.cur_code as curCode,ru.cur_name as curName,ru.money as money,ru.open_money as openMoney,");
        sql.append(" ru.begin_date as beginDate,ru.end_date as endDate,ru.status as status,");
        sql.append(" ru.interestrate as interestrate,ru.assurepercent as assurepercent, ");
        sql.append(" ru.contact_No as contactNo,ru.bill_No as billNo,ru.lc_no as lcNo,ru.remark as remark, ");
        sql.append(" ru.bill_money as billMoney,ru.bond_curid as bondCurId,ru.bond_money as bondMoney,");
        //TODO 保证金币种暂用同一个
        sql.append(" ru.cur_name as bondCurName,ru.cur_name as initCurName,");
        sql.append(
                " ru.gua_way as guaWay,ru.gua_rmk as guaRmk,ru.is_open_rat as isOpenRat,ru.init_cur_code as initCurCode,ru.ATTACHMENT_ID as attachmentId, ");
        sql.append(" case when ru.rat_credit_type is null then '1' else ru.rat_credit_type end as ratCreditType ");
        sql.append(" from rat_uration_apply ru left join rat_item_custom rc on ru.item_code = rc.id ");
        // sql.append(" inner join rat_sys_item rs on ru.type = rs.code ");
        sql.append(" left join rat_business_race  rb on ru.bill_name = rb.id ");
        sql.append(" where ru.id = :urationId ");
        params.put("urationId", urationId);
        return listBySQLAliasToBean(sql.toString(), params, RatUrationApplyListDTO.class);
    }
    /**
     * 功能介绍 : 查询占用待审批数据。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput getDataApplyApproveTables(DataTablesInput input, String businessSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(" select ru.id as id,ru.type as type,ru.corp_id as corpId,dis.corp_name as corpName, ");
        sql.append(" ru.rat_code as ratCode,ru.item_code as itemCode,rc.name as itemName, ");
        sql.append(" ru.item_flag as itemFlag,ru.cur_code as curCode,ru.cur_name as curName, ");
        sql.append(" ru.money as money,ru.open_money as openMoney,ru.begin_date as beginDate,ru.end_date as endDate, ");
        sql.append(" ru.create_date as createDate,ru.status as status, ");
        sql.append(" case when ru.rat_credit_type is null then '1' else ru.rat_credit_type end as creditType ");
        sql.append(" from rat_uration_apply ru left join rat_item_custom rc on ru.item_code = rc.id ");
//        sql.append(" sys_corp sc,bt_currency bc ");
        sql.append(" left join rat_bkration_dis dis on ru.dis_code = dis.id ");
//        sql.append(" where ru.corp_id = sc.id and ru.cur_code = bc.id ");
//        sql.append(" and ru.status <> -2 ");
        sql.append(" where ru.status <> -2 ");
        if (EmptyUtils.isNotEmpty(businessSql)) {
            sql.append(" and " + businessSql);
        }
        // sql.append(getWhereCondition(input, new RatUrationListVO(), null, false));
        // params.putAll(input.getSearchValues());
        return getDynamicBySQL(input, sql.toString(), RatUrationListVO.class, null, false);
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
        sql.append("select count(*) from rat_uration_apply where corp_id=:corpId and rat_type=0");
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in (:status) ");
            params.put("status", status.split(","));
        } else {
            sql.append(" and status<>-2 ");
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
    /**
     * 功能介绍 : 查询所有授信集合数据 <br>
     * <b>日期：</b>2021年12月02日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<String> queryAllRatDisList() {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT distinct rat.id ");
        sql.append(" rat.dis_Code as disCode ");
        sql.append(" from rat_bkration rat,rat_bkration_dis rbd ");
        sql.append(" where rat.id = rbd.rat_code ");
        sql.append(" and rat.rat_type = '0' ");
        sql.append(" and rbd.status = 95 and rat.status = 95 ");
        sql.append("  order by rat.rat_Code ");
        return listBySQLAliasToBean(sql.toString(), params, String.class);
    }
    /**
     * 功能介绍 : 查询单位授信额度及占用情况。<br>
     * <b>日期：</b>2021年12月02日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationDisBean> queryCorpRatDisList(List<String> ratCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(" SELECT RBDD.id, ");
        sql.append(" rat.corp_id as corpId, ");
        sql.append(" rat.corp_Name as corpName, ");
        sql.append(" RBD.rat_Code as ratCode, ");
        sql.append(" RBD.id as disCode, ");
        sql.append(" rat.beginDate as beginDate, ");
        sql.append(" rat.enddate as endDate, ");
        sql.append(" BT_NVL(RBDD.credit_Type, '1') as creditType, ");
        sql.append(" RBD.distribute_way as disWay, ");
        sql.append(" rat.rat_mode as ratMode, ");
        sql.append(" rat.cur_code as curCode, ");
        sql.append(" rat.cur_name as curName, ");
        sql.append(" rat.is_hang_guarantee as isHangGuarantee, ");
        sql.append(" rat.status, ");
        sql.append(" rbdd.item_code as itemcode, ");
        sql.append(" rbdd.item_flag as itemflag, ");
        sql.append(" rbdd.item_order as itemorder, ");
        sql.append(" rbdd.comp_sign as compsign, ");
        sql.append(" (rbdd.money + rbdd.super_by_relief_money - rbdd.super_relief_money) as money, ");
        sql.append(" (rbdd.max_money + rbdd.by_relief_max_money -  rbdd.relief_max_money) as maxmoney ");
        sql.append(" from rat_bkration_dis rbd, rat_bkration_dis_detail rbdd, rat_bkration rat ");
        sql.append(" where rat.id = rbd.rat_code ");
        sql.append(" and rbd.id = rbdd.dis_code ");
        sql.append(" and rat.rat_type = '0' ");
        sql.append(" and rbd.status = 95 and rat.status = 95 ");
        if (!EmptyUtils.isEmptyList(ratCodes)) {
            sql.append(" and rat.id in (:ratCode) ");
            params.put("ratCode", ratCodes);
        }
        sql.append("  order by rbd.corp_id,rat.rat_Code,rbd.id,rbdd.item_order ");
        return listBySQLAliasToBean(sql.toString(), params, RatUrationDisBean.class);
    }
}
