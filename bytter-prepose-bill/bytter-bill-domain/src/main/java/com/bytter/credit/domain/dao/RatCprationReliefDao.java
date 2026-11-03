package com.bytter.credit.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationRelief;
import com.bytter.credit.domain.vo.RatBkrationReliefListVO;
import com.bytter.credit.domain.vo.RatBkrationReliefPageVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.domain.vo.RatItemDisVO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author fusb
 * @version 1.0
 * @Title RatCprationReliefDao
 * @Description 授信额度调剂表
 * @date 2018年11月23日
 */
@Repository
public class RatCprationReliefDao extends BaseDao<RatBkrationRelief, String> {
    /**
     * 查询内部授信可调剂、占用数据(已分配)
     *
     * @param input       查询参数
     * @param corpAuthSql 有权限的id字符串拼接
     * @return 列表数据
     */
    public DataTablesOutput queryRationDisCheckData(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.cur_code as curCode,")
                .append(" rb.cur_name as curName,rb.rat_mode as ratMode, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ")
                .append(" from RAT_BKRATION rb ")
                .append(" where rb.status = 95 ")
                .append(" and exists ( select 1 from rat_bkration_dis rd where rd.rat_code = rb.id  ");
        sql.append(" and rd.status = 95 ) and rb.rat_type='1' ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false);
    }
    /**
     * 获取内部授信分配单位的授信品种类型
     *
     * @param ratCode
     * @param corpId
     * @return
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
     * 获取授信分配单位的授信品种
     *
     * @param ratCode
     * @param corpId
     * @param itemFlag
     * @return
     */
    public List<RatItemDisVO> getItemCustomDTOs(String ratCode, String corpId, String itemFlag) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select rb.id as disCode,rc.id as itemCode,rc.NAME as itemName, ");
        sql.append(" rd.item_flag as itemFlag,rd.item_order as itemOrder, ");
        sql.append(" rd.money as money,rd.open_money as openMoney, ");
        sql.append(
                " (rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money - rd.freeze_Max_Money - rd.virtual_Max_Money) as maxMoney, ");
        sql.append(" re.assurepercent as assurepercent,re.interestrate as interestrate,re.comp_sign as compSign ");
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
     * 查询调剂列表数据。
     *
     * @param input
     * @param corpAuthSql
     * @return
     */
    public DataTablesOutput queryRatCpRationReliefByRatCode(DataTablesInput input, String... corpAuthSql) {
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
     * 功能介绍 : 查询调剂审批列表数据。
     *
     * @param input
     * @param businessSql
     * @return
     */
    public DataTablesOutput queryRationReliefApproveData(DataTablesInput input, String businessSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
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
        sql.append(" left join rat_bkration rt on rb.rat_code = rt.id and rb.corp_id = rt.corp_id ");
//        sql.append(" left join bt_bank bb on rt.bank_code = bb.sys_bank_code ");
//        sql.append(" left join bt_currency bc on rt.cur_code = bc.id where rb.status <> -2 ");
        sql.append(" where rb.status <> -2 ");
        if (EmptyUtils.isNotEmpty(businessSql)) {
            sql.append(" and " + businessSql);
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationReliefListVO.class, null, false);
    }
    /**
     * 功能介绍 : 获取所有分配的二次授信单位。
     *
     * @param ratCode
     * @param corpId
     * @return
     */
    public List<CorpDTO> getAllRatDisCorpByRatCode(String ratCode, String corpId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd.corp_id as id,rd.corp_name as name ")
                .append(" from RAT_BKRATION_DIS rd ")
                .append(" where rd.status = :status ").append(" and rd.rat_code = :ratCode ")
                .append(" group by rd.corp_id,rd.corp_name ");
        if (!StringUtils.isEmpty(corpId)) {
            sql.append(" and rb.corp_id = :corpId ");
            params.put("corpId", corpId);
        }
        params.put("ratCode", ratCode);
        params.put("status", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, CorpDTO.class);
    }
    /**
     * 功能介绍 : 非总额共享下的授信分配单位(自用+金额共享)。
     *
     * @param ratCode
     * @param corpId
     * @return
     */
    public List<CorpDTO> getNotAmountDisCorpByRatCode(String ratCode, String corpId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(" select rd.id as id,rd.corp_name as name ")
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
     * 功能介绍 : 根据总额共享（网点）+金额共享的情况下获取授信分配单位。
     *
     * @param ratCode
     * @param corpId
     * @return
     */
    public List<CorpDTO> getMixCorpByRatCodeDisWay(String ratCode, String corpId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(" select rd.id as id,rd.corp_name as name ")
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
    /**
     * 查询授信对象
     *
     * @param refId
     * @return
     */
    public List<RatBkrationReliefPageVO> queryRatBkRationReliefById(String refId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(" select rb.id as id,rb.ref_code as refCode,rb.rat_code as ratCode, ");
        sql.append(" rt.corp_name as corpName, rt.bank_name as bankName,rt.cur_name as curName, ");
        sql.append(" rb.relief_Code as reliefCode,rb.by_relief_Code as byReliefCode, ");
        sql.append(" rb.relief_Corp as reliefCorp,rb.by_relief_Corp as byReliefCorp, ");
        sql.append(" rb.relief_corp_name as reliefCorpName,rb.by_relief_corp_name as byreliefCorpName, ");
        sql.append(" rs.id as reliefItemCode,rc.id as byreliefItemCode, ");
        sql.append(" rs.name as reliefItemName,rc.name as byreliefItemName, ");
        sql.append(" rb.relief_Item_Flag as reliefItemFlag,rb.by_relief_Item_Flag as byReliefItemFlag, ");
        sql.append(" rb.relief_money as reliefMoney,rb.relief_open_money as reliefOpenMoney,rb.status as status, ");
        sql.append(" rb.corp_id as corpId,rb.cur_code as curCode ");
        sql.append(" from rat_bkration_relief rb left join rat_item_custom rs on rb.relief_item_code = rs.id ");
        sql.append(" left join rat_item_custom rc on rb.by_relief_item_code = rc.id ");
//        sql.append(" left join sys_corp sp on rb.corp_id = sp.id ");
//        sql.append(" left join sys_corp sc on rb.relief_corp = sc.id ");
//        sql.append(" left join sys_corp sb on rb.by_relief_corp = sb.id ");
        sql.append(" left join rat_bkration rt on rb.rat_code = rt.id ");
//        sql.append(" left join bt_bank bb on rt.bank_code = bb.sys_bank_code ");
//        sql.append(" left join bt_currency bc on rt.cur_code = bc.id ");
        sql.append(" where rb.id = :refId ");
        params.put("refId", refId);
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationReliefPageVO.class);
    }
    public BigDecimal getDisItemMoney(String ratCode, String corpId, String itemFlag, String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select (sum(a.money)+sum(a.super_by_relief_money)-sum(a.freeze_money)-sum(a.super_process_money)-sum(a.super_relief_money)) as sumMoney ");
        sql.append(" from  rat_bkration_dis_detail a left join  rat_item_custom  b on a.item_code=b.id ");
        sql.append(" left join rat_bkration_dis c on  a.dis_code=c.id  where c.status='95' and c.rat_type=1 and c.rat_code=:ratCode  and ");
        sql.append("c.corp_id=:corpId and a.item_flag=:itemFlag and a.item_code=:itemCode ");
        params.put("ratCode", ratCode);
        params.put("corpId", corpId);
        params.put("itemFlag", itemFlag);
        params.put("itemCode", itemCode);
        Map map = getBySQL(sql.toString(), params);
        if (!map.isEmpty()) {
            BigDecimal sumMoney = (BigDecimal) map.get("sumMoney");
            return sumMoney = Objects.isNull(sumMoney) ? (BigDecimal) map.get("SUMMONEY") : sumMoney;
        } else {
            return null;
        }
    }
    public BigDecimal getReliefMoney(String ratCode, String corpId, String itemFlag, String itemCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select sum(b.relief_money) as reliefMoney from RAT_BKRATION_RELIEF b  where  b.rat_code=:ratCode  and");
        sql.append(
                " b.relief_corp=:corpId and b.relief_item_flag=:itemFlag and b.relief_item_code=:itemCode and b.status in(0,11,95)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("corpId", corpId);
        params.put("itemFlag", itemFlag);
        params.put("itemCode", itemCode);
        Map map = getBySQL(sql.toString(), params);
        if (!map.isEmpty()) {
            BigDecimal reliefMoney = (BigDecimal) map.get("RELIEFMONEY");
            return reliefMoney;
        } else {
            return null;
        }
    }
    public BigDecimal getReliefMoney2(String ratCode, String corpId, String itemFlag, String itemCode) {
        return null;
    }
    /**
     * 更新原授信分配中冻结额度
     *
     * @param dto
     */
    public void updateRationDisFreezeMoney(RatBkrationDisDtlDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append("  update rat_bkration_dis_detail rd");
        sql.append("  set freeze_money = :freezeMoney, freeze_open_money = :freezeOpenMoney, ");
        sql.append(
                "  virtual_Relief_Money = :virtualReliefMoney, virtual_Relief_Open_Money = :virtualReliefOpenMoney, ");
        sql.append("  super_Relief_Money = :superReliefMoney, super_Relief_Open_Money = :superReliefOpenMoney, ");
        sql.append(
                "  super_By_Relief_Money = :superByReliefMoney, super_By_Relief_Open_Money = :superByReliefOpenMoney ");
        sql.append(" where rd.id = :dtlId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dtlId", dto.getId());
        params.put("freezeMoney", dto.getFreezeMoney());
        params.put("freezeOpenMoney", dto.getFreezeOpenMoney());
        params.put("virtualReliefMoney", dto.getVirtualReliefMoney());
        params.put("virtualReliefOpenMoney", dto.getVirtualReliefOpenMoney());
        params.put("superReliefMoney", dto.getSuperReliefMoney());
        params.put("superReliefOpenMoney", dto.getSuperReliefOpenMoney());
        params.put("superByReliefMoney", dto.getSuperByReliefMoney());
        params.put("superByReliefOpenMoney", dto.getSuperByReliefOpenMoney());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 更新原授信分配中最高额度
     *
     * @param dto
     */
    public void updateRationDisMaxMoney(RatBkrationDisDtlDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append("  update rat_bkration_dis_detail ");
        sql.append("  set max_MONEY = :maxMoney, process_Max_Money = :processMaxMoney,  ");
        sql.append("  relief_Max_Money = :reliefMaxMoney, by_relief_Max_Money = :byReliefMaxMoney,  ");
        sql.append("  freeze_Max_Money = :freezeMaxMoney, virtual_max_money = :virtualMaxMoney ");
        sql.append(" where id = :dtlId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dtlId", dto.getId());
        params.put("maxMoney", dto.getMaxMoney());
        params.put("processMaxMoney", dto.getProcessMaxMoney());
        params.put("reliefMaxMoney", dto.getReliefMaxMoney());
        params.put("byReliefMaxMoney", dto.getByReliefMaxMoney());
        params.put("freezeMaxMoney", dto.getFreezeMaxMoney());
        params.put("virtualMaxMoney", dto.getVirtualMaxMoney());
        createSQLQuery(sql.toString(), params).executeUpdate();
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
        sql.append("select count(*) from RAT_BKRATION_RELIEF where corp_id=:corpId and rat_type=1 ");
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
