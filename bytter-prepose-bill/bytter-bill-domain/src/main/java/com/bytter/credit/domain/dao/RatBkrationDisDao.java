package com.bytter.credit.domain.dao;
import com.bytter.credit.domain.entity.RatBkrationDis;
import com.bytter.credit.domain.vo.RatBkrationDisVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.domain.vo.RatItemDisVO;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 授信分配信息表
 * @date 2017-07-31 16:07:30
 */
@Repository
public class RatBkrationDisDao extends BaseDao<RatBkrationDis, String> {
    /**
     * 功能介绍 : 查询授信录入复核通过待分配数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput listDisOutputData(DataTablesInput input, String corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rb.rat_mode as ratMode,rb.rat_type as ratType, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money,rb.bank_type_name as bankTypeName ")
                .append(" from RAT_BKRATION rb ")
                .append(" where rb.status <> -2 and rb.rat_type = '0' ")
                .append("  and not exists (select 1 from rat_bkration_dis rd where rd.rat_code = rb.id ")
                .append("  and rd.distribute_way = '").append(RationConstant.DIS_TYPE_AUTO).append("' ) ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            sql.append(" and ").append(corpAuthSql);
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false);
    }
    /**
     * 功能介绍 : 根据授信单号查询所有授信分配数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput queryRationDisData(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rd.id as id,rb.rat_code as ratCode,rb.net_Code as netCode,rb.corp_id as ratCorpId, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName,rb.corp_name as ratCorpName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rd.dis_Code as disCode,rb.bank_type_name as bankTypeName, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rb.money as money, ")
                .append(" rd.corp_id as corpId,rd.distribute_Way as distributeWay, rd.corp_name as corpName ")
                .append(" from rat_bkration_dis rd, RAT_BKRATION rb ")
                .append(" where rd.rat_code = rb.id and rb.status <> -2 and rb.rat_type = '0' and rd.status <> -2 ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationDisVO.class, null, false);
    }
    /**
     * 功能介绍 : 删除原授信分配数据。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteBkrationDisByRatCode(String ratCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from rat_bkration_dis where rat_Code = :ratCode ");
        params.put("ratCode", ratCode);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 查询授信分配审批数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput queryRationDisApproveData(DataTablesInput input, String businessSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称补充
        sql.append("select rd.id as id,rb.rat_code as ratCode,rb.corp_id as ratCorpId,rb.corp_name as ratCorpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rd.dis_Code as disCode, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rd.money as money, ")
                .append(" rd.corp_id as corpId,rd.distribute_Way as distributeWay, rd.corp_name as corpName, ")
                .append(" rd.create_date as createDate ")
//                .append(" from rat_bkration_dis rd, RAT_BKRATION rb,sys_corp sc,bt_bank bb,bt_currency bc,sys_corp sp ")
//                .append(" where rd.rat_code = rb.id and rb.corp_id = sc.id and rb.bank_code = bb.sys_bank_code ")
//                .append(" and rd.corp_id = sp.id and rb.cur_code = bc.id and rb.status <> -2 and rd.status <> -2 ");
                .append(" from rat_bkration_dis rd, RAT_BKRATION rb ")
                .append(" where rd.rat_code = rb.id and rb.status <> -2 and rb.rat_type = '0' and rd.status <> -2 ");
        if (EmptyUtils.isNotEmpty(businessSql)){
            sql.append(" and " + businessSql);
        }
        // sql.append(getWhereCondition(input, new RatBkrationVO(), null, false));
        // params.putAll(input.getSearchValues());
        return getDynamicBySQL(input, sql.toString(), RatBkrationDisVO.class, null, false);
    }
    /**
     * 功能介绍 : 查询银行授信可调剂、占用数据(已分配)。<br>
     * <b>日期：</b>2017年8月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput queryRationDisCheckData(DataTablesInput input, List<String> itemCodeList, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ");
        sql.append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ");
        sql.append(" rb.cur_code as curCode,rb.cur_name as curName,rb.rat_mode as ratMode,rb.rat_type as ratType, ");
        sql.append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ");
        sql.append(" from RAT_BKRATION rb ");
        sql.append(" where rb.status = 95 and rb.RAT_TYPE = '0' ");
        sql.append(" and exists ( select 1 from rat_bkration_dis rd where rd.rat_code = rb.id and rd.status = 95 )  ");

        if (!CollectionUtils.isEmpty(itemCodeList)) {
            sql.append(" and rb.id in (select distinct rbd.rat_code from RAT_BKRATION_DETAIL rbd where  1 = 1 "
                    + "and  ( "+ getSQLIn(itemCodeList, 900, "rbd.item_code") + "))");
            input.getFormdata().remove("itemCode");
        }

        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false);
    }
    /**
     * 功能介绍 : 查询授信录入信息已分配过授信。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDis> queryCorpRationDis(RatBkrationDisListDTO disDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd from RatBkrationDis rd where rd.ratCode = :ratCode  ");
        sql.append(" and rd.corpId = :corpId and rd.status not in (-3,-2) ");
        // sql.append(" and rd.type = :type ");
        if (!StringUtils.isEmpty(disDTO.getId())) {
            sql.append(" and rd.id <> :disId ");
            params.put("disId", disDTO.getId());
        }
        params.put("ratCode", disDTO.getRatCode());// 授信录入编号
        params.put("corpId", disDTO.getCorpId());// 单位
        params.put("type", disDTO.getType());// 额度类型
        // params.put("status", CommonConstant.STATUE_DELETE);
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 查询授信录入信息已分配过授信。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDis> getRatBkDisWayByRatCode(String ratCode, String corpId, String disWay) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select rd from RatBkrationDis rd where rd.ratCode = :ratCode  ");
        sql.append(" and rd.status <> :status ");
        if (!StringUtils.isEmpty(corpId)) {
            sql.append(" and rd.corpId = :corpId ");
            params.put("corpId", corpId);
        }
        if (!StringUtils.isEmpty(disWay)) {
            sql.append(" and rd.distributeWay = :disWay ");
            params.put("disWay", disWay);
        }
        sql.append(" order by rd.updateDate,rd.createDate desc ");
        params.put("ratCode", ratCode);
        params.put("corpId", corpId);
        params.put("status", CommonConstant.STATUE_DELETE);
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 查询其他单位授信录入信息已分配过授信。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDis> getRatOtherCorpDisByRatCode(String ratCode, String corpId, String disWay) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select rd from RatBkrationDis rd where rd.ratCode = :ratCode  ");
        sql.append(" and rd.status = :status ");
        if (!StringUtils.isEmpty(corpId)) {
            sql.append(" and rd.corpId <> :corpId ");
            params.put("corpId", corpId);
        }
        if (!StringUtils.isEmpty(disWay)) {
            sql.append(" and rd.distributeWay = :disWay ");
            params.put("disWay", disWay);
        }
        params.put("ratCode", ratCode);
        params.put("corpId", corpId);
        params.put("status", RationConstant.STATUS_CHECK);
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 获取总额共享下其他单位的品种分配详情。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDis> getOtherRationDisByDisCode(String ratCode, String disCode, String itemFlag,
                                                           String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select dis from RatBkrationDis dis where  ");
        hql.append(" exists ( select 1 from RatBkrationDis rb,RatBkrationDisDetail rd ");
        hql.append(" where rb.id = dis.id and rb.id = rd.disCode and rb.status <> :status ");
        hql.append(" and rb.ratCode = :ratCode and rb.distributeWay = :disWay and rb.id <> :disCode  ");
        if (!StringUtils.isEmpty(itemFlag)) {
            if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                hql.append(" and rd.itemCode = :itemCode ");
                params.put("itemCode", itemCode);
            }
            hql.append(" and rd.itemFlag = :itemFlag ");
            params.put("itemFlag", itemFlag);
        }
        hql.append(" ) ");
        params.put("disCode", disCode);
        params.put("ratCode", ratCode);
        params.put("disWay", RationConstant.DIS_TYPE_AMOUNT_SHARE);// 总额共享
        params.put("status", RationConstant.STATUS_DELETE);
        hql.append(" order by dis.disCode ");
        return listByHQL(hql.toString(), params);
    }
    /**
     * 功能介绍 : 查询该分配信息以外的其他授信录入信息已分配过授信。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDis> getOtherRatDisByRatCode(String ratCode, String corpId, String disWay, String disId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select rd from RatBkrationDis rd where rd.ratCode = :ratCode  ");
        sql.append(" and rd.status <> :status ");
        if (!StringUtils.isEmpty(corpId)) {
            sql.append(" and rd.corpId <> :corpId ");
            params.put("corpId", corpId);
        }
        if (!StringUtils.isEmpty(disWay)) {
            sql.append(" and rd.distributeWay = :disWay ");
            params.put("disWay", disWay);
        }
        if (!StringUtils.isEmpty(disId)) {
            sql.append(" and rd.id <> :disId ");
            params.put("disId", disId);
        }
        params.put("ratCode", ratCode);
        params.put("corpId", corpId);
        params.put("status", CommonConstant.STATUE_DELETE);
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 银行授信是否进行了总额共享分配 。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDis> rationIsTotalMoneyDis(String ratCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd from RatBkrationDis rd where rd.ratCode = :ratCode  ");
        sql.append(" and rd.distributeWay in (:disWay) ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("disWay", new String[]{RationConstant.DIS_TYPE_OWN, RationConstant.DIS_TYPE_AMOUNT_SHARE});
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 查询授信录入信息已审批通过的分配授信。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDis> getRatBkDisByRatCode(String ratCode, String corpId, String disWay) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd from RatBkrationDis rd where rd.ratCode = :ratCode  ");
        sql.append(" and rd.status in (95,-3) ");// 审批通过和已作废的数据
        if (!StringUtils.isEmpty(corpId)) {
            sql.append(" and rd.corpId = :corpId ");
            params.put("corpId", corpId);
        }
        if (!StringUtils.isEmpty(disWay)) {
            sql.append(" and rd.distributeWay = :disWay ");
            params.put("disWay", disWay);
        }
        params.put("ratCode", ratCode);
        // params.put("status", CommonConstant.STATUE_CHECK);
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍: 查询已审批通过的授信分配信息。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationCpAddListDTO> queryPassRationDisData(String ratCode, String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as ratCorpId,rb.corp_name as ratCorpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName,rb.RAT_MODE as ratMode, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rd.id as disCode,rb.bank_type_name as bankTypeName, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rb.money as money, ")
                .append(" rd.corp_id as corpId,rd.distribute_Way as distributeWay, rd.corp_name as corpName ")
                .append(" from rat_bkration_dis rd inner join RAT_BKRATION rb on rd.rat_code = rb.id  ")
                // 查询审批通过和分配作废的数据
                .append(" where rb.status = :status and rb.rat_type = '0' and rd.status in(95,-3) and rd.rat_code = :ratCode and rd.corp_id = :corpId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("corpId", corpId);
        params.put("status", CommonConstant.STATUE_CHECK);// 审批通过
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationCpAddListDTO.class);
    }

    /**
     * 根据授信id和分配单位查询授信分配信息
     *
     * @author fangcf
     * @date 20240-07-01
     */
    public List<RatBkrationCpAddListDTO> queryAllRationDisData(String ratCode, String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as ratCorpId,rb.corp_name as ratCorpName, ");
        sql.append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName,rb.RAT_MODE as ratMode, ");
        sql.append(" rb.cur_code as curCode,rb.cur_name as curName,rd.id as disCode,rb.bank_type_name as bankTypeName, ");
        sql.append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rb.money as money, ");
        sql.append(" rd.corp_id as corpId,rd.distribute_Way as distributeWay, rd.corp_name as corpName ");
        sql.append(" from rat_bkration_dis rd inner join RAT_BKRATION rb on rd.rat_code = rb.id  ");
        sql.append(" where rb.status <> :status and rb.rat_type = '0' and rd.rat_code = :ratCode and rd.corp_id = :corpId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("corpId", corpId);
        params.put("status", CommonConstant.STATUE_DELETE);
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationCpAddListDTO.class);
    }

    /**
     * 功能介绍 : 查询授信录入信息已分配过授信。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDis> getRatBkDisByRatCode(String ratCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select rd from RatBkrationDis rd where rd.ratCode = :ratCode  ");
        sql.append(" and rd.status <> :status ");
        params.put("ratCode", ratCode);
        params.put("status", CommonConstant.STATUE_DELETE);
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 获取授信分配单位的授信品种类型。<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatItemDisVO> getCorpItemFlags(String ratCode, String corpId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select distinct rd.item_flag as itemFlag ");
        sql.append(" from RAT_BKRATION_DIS rb,RAT_BKRATION_DIS_DETAIL rd,RAT_ITEM_CUSTOM rc ");
        sql.append(" where rb.id = rd.dis_code and rd.item_Code = rc.id and rb.status = :status ");
        sql.append(" and rb.RAT_CODE = :ratCode and rb.CORP_ID = :corpId ");
        sql.append(" order by rd.item_flag ");
        params.put("ratCode", ratCode);
        params.put("corpId", corpId);
        params.put("status", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RatItemDisVO.class);
    }
    /**
     * 功能介绍 : 获取授信分配单位的授信品种。<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatItemDisVO> getItemCustomDTOs(String ratCode, String corpId, String itemFlag) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select rb.id as disCode,rc.id as itemCode,rc.NAME as itemName, ");
        sql.append(" rd.item_flag as itemFlag,rd.item_order as itemOrder, ");
        sql.append(" rd.money as money,rd.open_money as openMoney, ");
        sql.append(
                " (rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money - rd.freeze_Max_Money - rd.virtual_Max_Money) as maxMoney, ");
        sql.append(" re.assurepercent as assurepercent,re.interestrate as interestrate,re.comp_sign as compSign, ");
        sql.append(" case when rd.credit_type is null then '1' else rd.credit_type end as creditType ");
        sql.append(" from RAT_BKRATION_DIS rb,RAT_BKRATION_DIS_DETAIL rd,RAT_ITEM_CUSTOM rc,rat_bkration_detail re ");
        sql.append(" where rb.id = rd.dis_code and rd.item_Code = rc.id  ");
        sql.append(" and re.rat_code = rb.rat_code and rd.item_code = re.item_code ");
        sql.append(" and rb.status = :status and rb.RAT_CODE = :ratCode ");
        sql.append(" and rb.CORP_ID = :corpId and rd.ITEM_FLAG = :itemFlag ");
        sql.append(" order by rd.item_order ");
        params.put("ratCode", ratCode);
        params.put("corpId", corpId);
        params.put("itemFlag", itemFlag);
        params.put("status", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RatItemDisVO.class);
    }
    /**
     * 功能介绍 : 查询授信分配单位。<br>
     * <b>日期：</b>2017年9月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<CorpDTO> queryRationCorp(String corpSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
//        sql.append("select distinct sc.id as id,sc.name as name, sc.code as code ")
//                .append(" from rat_bkration_dis rb, sys_corp sc ")
        sql.append("select distinct rb.corp_id as id,rat.corp_name as name, rat.corp_code as code ")
                .append(" from rat_bkration_dis rb join rat_bkration rat on rat.id = rb.rat_code and rb.corp_id = rat.corp_id")
                .append(" where rb.status = :status ");
        sql.append(" order by rat.corp_code ");
        params.put("status", RationConstant.STATUS_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, CorpDTO.class);
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
        sql.append(
                "select count(*) from RAT_BKRATION_DIS where  corp_id=:corpId and rat_type=0 and DISTRIBUTE_WAY<>4 ");
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
     * 根据单位查询分配数据
     *
     * @param corpId
     * @return
     */
    public List<RatBkrationDis> queryDisDataByCorpId(String corpId, String disWay) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select rd from RatBkrationDis rd where rd.status<>-2 and rd.corpId=:corpId and rd.ratType=0 ");
        if (EmptyUtils.isNotEmpty(disWay)) {
            sql.append(" and rd.distributeWay=:disWay ");
            params.put("disWay", disWay);
        }
        params.put("corpId", corpId);
        return listByHQL(sql.toString(), params);
    }

    /**
     * 总额共享方式已分配的单位查看
     *
     * @param ratCode   授信主键id
     * @param ratCorpId 授信单位id
     */
    public List<String> distributeWayView(String ratCode, String ratCorpId) {
        String sql = " select dis.CORP_ID from RAT_BKRATION_DIS dis WHERE dis.RAT_TYPE = 0 and dis.STATUS <> -2 and dis.RAT_CODE = :ratCode and dis.RAT_CORP_ID = :ratCorpId and dis.DISTRIBUTE_WAY = '2' ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("ratCode", ratCode);
        params.put("ratCorpId", ratCorpId);
        return createSQLQuery(sql, params).list();
    }
}
