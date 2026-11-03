package com.bytter.credit.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.credit.constant.RationConstant;
import org.springframework.stereotype.Repository;
import com.bytter.credit.domain.entity.RatBkrationRelief;
import com.bytter.credit.domain.vo.RatBkrationReliefListVO;
import com.bytter.credit.domain.vo.RatBkrationReliefPageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.dto.CorpDTO;
/**
 * @Title: Dao
 * @Description: 授信额度调剂表
 * @author onlineGenerator
 * @date 2017-07-31 16:10:53
 * @version 1.0
 *
 */
@Repository
public class RatBkrationReliefDao extends BaseDao<RatBkrationRelief, String> {
    /**
     * 功能介绍 : 查询调剂列表数据。<br>
     * <b>日期：</b>2017年8月13日<br>
     *
     * @version 1.0
     * @author liyh
     * @param corpAuthSql
     */
    public DataTablesOutput queryRatBkRationReliefByRatCode(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rb.id as id,rb.ref_code as refCode,rb.rat_code as ratCode, ");
        sql.append(" rt.corp_name as corpName, rt.bank_name as bankName,rt.cur_name as curName, ");
        sql.append(" rb.relief_corp_name as reliefCorpName,rb.by_relief_corp_name as byreliefCorpName, ");
        sql.append(" rs.name as reliefItemName,rc.name as byreliefItemName, ");
        sql.append(" rb.relief_Item_Flag as reliefItemFlag,rb.by_relief_Item_Flag as byReliefItemFlag, ");
        sql.append(" rb.relief_money as reliefMoney,rb.status as status ");
        sql.append(" from rat_bkration_relief rb left join rat_item_custom rs on rb.relief_item_code = rs.id ");
        sql.append(" left join rat_item_custom rc on rb.by_relief_item_code = rc.id ");
        sql.append(" left join rat_bkration rt on rb.rat_code = rt.id ");
        sql.append(" where rb.status <> -2 ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationReliefListVO.class, null, false);
    }
    /**
     * 功能介绍 : 查询调剂审批列表数据。<br>
     * <b>日期：</b>2017年8月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput queryRationReliefApproveData(DataTablesInput input, String businessSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 补充名称相关信息
        sql.append(" select rb.id as id,rb.ref_code as refCode,rb.rat_code as ratCode, ");
        sql.append(" rt.corp_name as corpName, rt.bank_name as bankName,rt.cur_name as curName, ");
        sql.append(" rb.relief_corp_name as reliefCorpName,rb.by_relief_corp_name as byreliefCorpName, ");
        sql.append(" rs.name as reliefItemName,rc.name as byreliefItemName, ");
        sql.append(" rb.relief_Item_Flag as reliefItemFlag,rb.by_relief_Item_Flag as byReliefItemFlag, ");
        sql.append(" rb.relief_money as reliefMoney,rb.status as status, rb.create_date as createDate ");
        sql.append(" from rat_bkration_relief rb left join rat_item_custom rs on rb.relief_item_code = rs.id ");
        sql.append(" left join rat_item_custom rc on rb.by_relief_item_code = rc.id ");
//        sql.append(" left join sys_corp sp on rb.corp_id = sp.id ");
//        sql.append(" left join sys_corp sc on rb.relief_corp = sc.id ");
//        sql.append(" left join sys_corp sb on rb.by_relief_corp = sb.id ");
        sql.append(" left join rat_bkration rt on rb.rat_code = rt.id ");
//        sql.append(" left join bt_bank bb on rt.bank_code = bb.sys_bank_code ");
//        sql.append(" left join bt_currency bc on rt.cur_code = bc.id where rb.status <> -2 ");
        sql.append(" where rb.status <> -2 ");
        if (EmptyUtils.isNotEmpty(businessSql)) {
            sql.append(" and " + businessSql);
        }
        // sql.append(getWhereCondition(input, new RatBkrationVO(), null, false));
        // params.putAll(input.getSearchValues());
        return getDynamicBySQL(input, sql.toString(), RatBkrationReliefListVO.class, null, false);
    }
    /**
     * 功能介绍 : 获取所有分配的二次授信单位。<br>
     * <b>日期：</b>2017年8月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<CorpDTO> getAllRatDisCorpByRatCode(String ratCode, String corpId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd.corp_id as id,rd.corp_name as name ")
                .append(" from RAT_BKRATION_DIS rd ")
                .append(" where rd.status = :status ").append(" and rd.rat_code = :ratCode ");
        if (!StringUtils.isEmpty(corpId)) {
            sql.append(" and rd.corp_id = :corpId ");
            params.put("corpId", corpId);
        }
        params.put("ratCode", ratCode);
        params.put("status", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, CorpDTO.class);
    }
    /**
     * 功能介绍 : 非总额共享下的授信分配单位(自用+金额共享)。<br>
     * <b>日期：</b>2017年8月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<CorpDTO> getNotAmountDisCorpByRatCode(String ratCode, String corpId) {
        Map<String, Object> params = new HashMap<String, Object>();
        //TODO 名称调整
        StringBuilder sql = new StringBuilder();
//        sql.append(" select sc.id as id,sc.CODE as code,sc.name as name ")
//                .append(" from RAT_BKRATION_DIS rd, sys_corp sc ")
//                .append(" where rd.CORP_ID = sc.ID and rd.distribute_way <> :disWay and rd.status = :status ")
        sql.append(" select rd.corp_id as id,rd.corp_code as code,rd.corp_name as name ")
                .append(" from RAT_BKRATION_DIS rd ")
                .append(" where rd.distribute_way <> :disWay and rd.status = :status ")
                .append(" and rd.rat_code = :ratCode ");
        if (!StringUtils.isEmpty(corpId)) {
            sql.append(" and rd.corp_id = :corpId ");
            params.put("corpId", corpId);
        }
        params.put("disWay", RationConstant.DIS_TYPE_AMOUNT_SHARE);
        params.put("status", CommonConstant.STATUE_CHECK);
        params.put("ratCode", ratCode);
        return listBySQLAliasToBean(sql.toString(), params, CorpDTO.class);
    }
    /**
     * 功能介绍 : 根据总额共享（网点）+金额共享的情况下获取授信分配单位。<br>
     * <b>日期：</b>2017年8月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<CorpDTO> getMixCorpByRatCodeDisWay(String ratCode, String corpId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
//        sql.append(" select sc.id as id,sc.CODE as code,sc.name as name ")
//                .append(" from RAT_BKRATION_DIS rd, sys_corp sc ")
        //TODO 名称调整
        sql.append(" select rd.corp_id as id,rd.corp_code as code,rd.corp_name as name ")
                .append(" from RAT_BKRATION_DIS rd ")
                .append(" where rd.status = :status ")
                .append(" and (rd.distribute_way = :disWays or rd.corp_id = :corpId ) ")
                .append(" and rd.rat_code = :ratCode ");
        params.put("status", CommonConstant.STATUE_CHECK);
        params.put("disWays", RationConstant.DIS_TYPE_MONEY_SHARE);
        params.put("ratCode", ratCode);
        params.put("corpId", corpId);
        return listBySQLAliasToBean(sql.toString(), params, CorpDTO.class);
    }
    public List<RatBkrationReliefPageVO> queryRatBkRationReliefById(String refId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rb.id as id,rb.ref_code as refCode,rb.rat_code as ratCode, ");
        sql.append(" rt.corp_name as corpName, rt.bank_name as bankName,rt.cur_name as curName, ");
        sql.append(" rb.relief_Code as reliefCode,rb.by_relief_Code as byReliefCode, ");
        sql.append(" rb.relief_Corp as reliefCorp,rb.by_relief_Corp as byReliefCorp, ");
        sql.append(" rb.relief_corp_name as reliefCorpName,rb.by_relief_corp_name as byreliefCorpName, ");
        sql.append(" rs.id as reliefItemCode,rc.id as byreliefItemCode, ");
        sql.append(" rs.name as reliefItemName,rc.name as byreliefItemName, ");
        sql.append(" rb.relief_Item_Flag as reliefItemFlag,rb.by_relief_Item_Flag as byReliefItemFlag, ");
        sql.append(" rb.relief_money as reliefMoney,rb.relief_open_money as reliefOpenMoney,rb.status as status, ");
        sql.append(" rb.corp_id as corpId,rb.cur_code as curCode,rb.ATTACHMENT_ID as attachmentId  ");
        sql.append(" from rat_bkration_relief rb left join rat_item_custom rs on rb.relief_item_code = rs.id ");
        sql.append(" left join rat_bkration rt on rb.rat_code = rt.id ");
        sql.append(" left join rat_item_custom rc on rb.by_relief_item_code = rc.id ");
        sql.append(" where rb.id = :refId ");
        params.put("refId", refId);
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationReliefPageVO.class);
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
        sql.append("select count(*) from RAT_BKRATION_RELIEF where corp_id=:corpId and rat_type=0 ");
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
