package com.bytter.credit.domain.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vo.RatUrationDisBean;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.*;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import com.bytter.credit.domain.entity.RatBkrationDisDetail;
import com.bytter.credit.domain.vo.RatItemDisVO;
import com.bytter.credit.domain.vo.RationDisBean;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
/**
 * @Title: Dao
 * @Description: 授信分配明细表
 * @author onlineGenerator
 * @date 2017-07-31 16:07:49
 * @version 1.0
 *
 */
@Repository
public class RatBkrationDisDetailDao extends BaseDao<RatBkrationDisDetail, String> {
    /**
     * 功能介绍 : 获取已分配的金额之和。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getRationDisMoney(String ratCode, String disWay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd.item_code as itemCode, ");
        sql.append(" sum(rd.money + rd.super_by_relief_money - rd.super_relief_money) as money, ");
        sql.append(" sum(rd.open_money + rd.super_by_relief_open_money - rd.super_relief_open_money) as open_money ");
        sql.append("  from rat_bkration_dis_detail rd  ");
        sql.append(" where exists (select 1 from rat_bkration_dis rb where rb.id = rd.dis_code ");
        sql.append(
                " and status >= '-1' and status in (95,-3) and rb.distribute_way = :disWay and rb.rat_code = :ratCode) ");
        sql.append(" group by rd.item_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("disWay", disWay);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 获取总额共享的占用的金额。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getURationDisMoney(String ratCode, String disWay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.item_code as itemCode, sum(b.money) as money, sum(b.open_money) as openMoney ").append(
                " from rat_uration b, rat_uration_apply_dtl a where b.id = a.apply_code and b.rat_code = :ratCode ")
                .append(" and b.status >= '-1'and b.status <= '95'  ")
                .append(" and exists ( select 1 from rat_bkration_dis dis where dis.rat_code = b.rat_code ")
                .append(" and dis.corp_id = b.corp_id and dis.rat_code = :ratCode and dis.distribute_way = :disWay )  ")
                .append(" group by a.item_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("disWay", disWay);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 获取总额共享的单品种占用的金额。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author shenc
     */
    public List<RationDisBean> getURationDisMoneySingle(String ratCode, String disWay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.item_code as itemCode,sum(b.money) as money,sum(b.open_money) as openMoney ")
                .append(" from rat_uration_apply b where 1 = 1 and b.rat_code = :ratCode ")
                .append(" and b.status >= '11' and b.status <= '95' and b.item_flag = '0' ")
                .append(" and exists (select 1 from rat_bkration_dis dis where dis.rat_code = b.rat_code ")
                .append(" and dis.corp_id = b.corp_id and dis.rat_code = :ratCode and dis.distribute_way = :disWay)  ")
                .append(" group by b.item_code  ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("disWay", disWay);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 获取分配表中未审批通过的分配额度。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getDisUnCheckMoney(String ratCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select rd.item_code as itemCode, sum(rd.money) as money, sum(rd.open_money) as openMoney,sum(rd.max_money) as maxMoney  ");
        sql.append(" from rat_bkration_dis rb, rat_bkration_dis_detail rd ");
        sql.append(" where rb.id = rd.dis_code and rb.status >= :status0 and rb.status < :status1 ");
        sql.append(" and rb.rat_code = :ratCode group by rd.item_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("status0", CommonConstant.STATUE_BACK);
        params.put("status1", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 获取当前授信分配信息中已分配的最高额度。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getRationDisMaxMoney(String ratCode, String disId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd.item_code as itemCode, ");
        sql.append(
                " sum(rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money - rd.freeze_Max_Money - rd.virtual_Max_Money) as maxMoney  ");
        sql.append(" from rat_bkration_dis rb, rat_bkration_dis_detail rd ");
        sql.append(" where rb.id = rd.dis_code and rb.status = :status0 ");
        sql.append(" and rb.rat_code = :ratCode and rb.distribute_way <> :disWay ");
        if (!StringUtils.isEmpty(disId)) {
            sql.append(" and rb.id = :disId ");
        }
        sql.append(" group by rd.item_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("disId", disId);
        params.put("disWay", RationConstant.DIS_TYPE_AMOUNT_SHARE);
        params.put("status0", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 获取当前授信分配信息中未审批通过的分配额度。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getEditDisUnCheckMoney(String ratCode, String disId) {
        if (StringUtils.isEmpty(disId)) {
            return new ArrayList<RationDisBean>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select rd.item_code as itemCode, sum(rd.money) as money, sum(rd.open_money) as openMoney, sum(rd.max_money) as maxMoney  ");
        sql.append(" from rat_bkration_dis rb, rat_bkration_dis_detail rd ");
        sql.append(" where rb.id = rd.dis_code and rb.status >= :status0 and rb.status < :status1 ");
        sql.append(" and rb.rat_code = :ratCode and rb.id = :disId group by rd.item_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("disId", disId);
        params.put("status0", CommonConstant.STATUE_BACK);
        params.put("status1", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年7月28日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationDtlVm> getBkrationDtlByRatCodes(List<String> disCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, rd.comp_sign as compSign, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.super_relief_money as superReliefMoney, rd.super_by_relief_money as superByReliefMoney, ")
                .append(" rd.super_relief_open_money as superReliefOpenMoney, rd.super_by_relief_open_money as superByReliefOpenMoney, ")
                .append(" rd.relief_max_money as reliefMaxMoney, rd.by_relief_max_money as byReliefMaxMoney, ")
                .append(" rd.max_money as maxMoney, ")
                .append(" case when rd.credit_type is null then '1' else rd.credit_type end as creditType ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc ")
                .append(" where rd.ITEM_CODE = rc.id and ");
        hql.append(getSQLIn(disCodes, 900, "rd.dis_Code"));
        hql.append(" order by rd.dis_Code,rd.item_flag,rd.item_Order ");
        return listBySQLAliasToBean(hql.toString(), params, RatBkRationDtlVm.class);
    }
    /**
     * 功能介绍 : 删除原授信分配明细。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void deleteBkrationDisDtlByDisCode(RatBkrationDisListDTO disDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from rat_bkration_dis_detail where dis_Code = :ratCode ");
        params.put("ratCode", disDTO.getId());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 删除原授信分配明细
     * @author fangcf
     */
    public void deleteBkrationDisDtlByRatCode(String ratCode) {
        Map<String, Object> params = Maps.newHashMap();
        String sql = "delete from rat_bkration_dis_detail where dis_Code in (select id from rat_bkration_dis where rat_code = :ratCode )";
        params.put("ratCode", ratCode);
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 更新原授信分配中追加额度。<br>
     * <b>日期：</b>2017年8月7日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void updateRationDtlDisMoney(RatBkrationDisDtlDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append("  update rat_bkration_dis_detail ");
        sql.append("  set MONEY = :money, open_money = :openMoney, max_money = :maxMoney,  ");
        sql.append("  freeze_money = :freezeMoney, freeze_open_money = :freezeOpenMoney, ");
        sql.append("  virtual_Uration = :virtualUration, virtual_Open_Uration = :virtualOpenUration, ");
        sql.append("  freeze_max_money = :freezeMaxMoney, virtual_Max_Money = :virtualMaxMoney ");
        sql.append(" where id = :dtlId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dtlId", dto.getId());
        params.put("money", dto.getMoney());
        params.put("openMoney", dto.getOpenMoney());
        params.put("maxMoney", dto.getMaxMoney());
        params.put("freezeMoney", dto.getFreezeMoney());
        params.put("freezeOpenMoney", dto.getFreezeOpenMoney());
        params.put("virtualUration", dto.getVirtualUration());
        params.put("virtualOpenUration", dto.getVirtualOpenUration());
        params.put("freezeMaxMoney", dto.getFreezeMaxMoney());
        params.put("virtualMaxMoney", dto.getVirtualMaxMoney());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 更新原授信分配中冻结额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void updateRationDisFreezeMoney(RatBkrationDisDtlDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append("  update rat_bkration_dis_detail rd ");
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
     * 功能介绍 : 根据授信分配编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年8月7日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDisDtlDTO> getBkrationDisDtlByRatCodes(List<String> disCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as disCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.super_Process_Money as superProcessMoney,rd.super_Process_Open_Money as superProcessOpenMoney, ")
                .append(" rd.super_Relief_Money as superReliefMoney,rd.super_Relief_Open_Money as superReliefOpenMoney, ")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,rd.super_By_Relief_Open_Money as superByReliefOpenMoney, ")
                .append(" rd.freeze_Money as freeze_Money,rd.freeze_Open_Money as freezeOpenMoney, ")
                .append(" rd.virtual_Relief_Money as virtualReliefMoney,rd.virtual_Relief_Open_Money as virtualReliefOpenMoney, ")
                .append(" rd.virtual_Uration as virtualUration,rd.virtual_Open_Uration as virtualOpenUration, ")
                .append(" rd.relief_And_Uration_Money as reliefAndUrationMoney,rd.uration_Open_Money as urationOpenMoney, ")
                .append(" rd.max_money as maxMoney,case when rd.credit_type is null then '1' else rd.credit_type end as creditType, ")
                .append(" rd.process_Max_Money as processMaxMoney,rd.relief_Max_Money as reliefMaxMoney, ")
                .append(" rd.by_Relief_Max_Money as byReliefMaxMoney,rd.freeze_Max_Money as freezeMaxMoney, rd.virtual_Max_Money as virtualMaxMoney ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc ")
                .append(" where rd.ITEM_CODE = rc.id and ");
        hql.append(getSQLIn(disCodes, 900, "rd.dis_Code"));
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationDisDtlDTO.class);
    }
    /**
     * 功能介绍 : 获取总额共享下授信单的所有授信分配明细。<br>
     * <b>日期：</b>2017年8月10日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDisDtlDTO> getAllShareCorpRationDisDtl(String ratCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as disCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.super_Process_Money as superProcessMoney,rd.super_Process_Open_Money as superProcessOpenMoney, ")
                .append(" rd.super_Relief_Money as superReliefMoney,rd.super_Relief_Open_Money as superReliefOpenMoney, ")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,rd.super_By_Relief_Open_Money as superByReliefOpenMoney, ")
                .append(" rd.freeze_Money as freeze_Money,rd.freeze_Open_Money as freezeOpenMoney, ")
                .append(" rd.virtual_Relief_Money as virtualReliefMoney,rd.virtual_Relief_Open_Money as virtualReliefOpenMoney, ")
                .append(" rd.virtual_Uration as virtualUration,rd.virtual_Open_Uration as virtualOpenUration, ")
                .append(" rd.relief_And_Uration_Money as reliefAndUrationMoney,rd.uration_Open_Money as urationOpenMoney, ")
                .append(" rd.max_money as maxMoney, ")
                .append(" rd.process_Max_Money as processMaxMoney,rd.relief_Max_Money as reliefMaxMoney, ")
                .append(" rd.by_Relief_Max_Money as byReliefMaxMoney,rd.freeze_Max_Money as freezeMaxMoney, rd.virtual_Max_Money as virtualMaxMoney ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc ").append(" where rd.ITEM_CODE = rc.id ");
        hql.append(" and exists ( select 1 from rat_bkration_dis rb ");
        hql.append(
                " where rb.id = rd.dis_code and rb.distribute_way = :disWay and rb.status <> :status and rb.rat_code = :ratCode )");
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        params.put("ratCode", ratCode);
        params.put("disWay", RationConstant.DIS_TYPE_AMOUNT_SHARE);// 总额共享
        params.put("status", RationConstant.STATUS_DELETE);// 非删除的
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationDisDtlDTO.class);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年8月10日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationCpAddDtlDTO> getCprationDisDtlByDisCodes(List<String> disCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" re.assurepercent as assurepercent,re.interestrate as interestrate, ")
                .append(" rd.super_Process_Money as superProcessMoney,rd.super_Process_Open_Money as superProcessOpenMoney, ")
                .append(" rd.super_Relief_Money as superReliefMoney,rd.super_Relief_Open_Money as superReliefOpenMoney, ")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,rd.super_By_Relief_Open_Money as superByReliefOpenMoney, ")
                .append(" rd.freeze_Money as freeze_Money,rd.freeze_Open_Money as freezeOpenMoney, ")
                .append(" rd.virtual_Relief_Money as virtualReliefMoney,rd.virtual_Relief_Open_Money as virtualReliefOpenMoney, ")
                .append(" rd.virtual_Uration as virtualUration,rd.virtual_Open_Uration as virtualOpenUration, ")
                .append(" rd.relief_And_Uration_Money as reliefAndUrationMoney,rd.uration_Open_Money as urationOpenMoney, ")
                .append(" (rd.max_money - rd.relief_Max_Money + by_Relief_Max_Money) as maxMoney, ")
                .append(" (rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money - rd.freeze_Max_Money - rd.virtual_Max_Money) as allDisMaxMoney, ")
                .append(" re.comp_sign as compSign,case when rd.credit_type is null then '1' else rd.credit_type end as creditType ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_dis rb,rat_bkration_detail re ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code and re.rat_code = rb.rat_code ")
                .append(" and rb.id = rd.dis_code and ");
        hql.append(getSQLIn(disCodes, 900, "rd.dis_Code"));
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationCpAddDtlDTO.class);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已分配的授信品种额度。<br>
     * <b>日期：</b>2017年8月15日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationCpAddDtlDTO> getRationDisDtlByItemCode(String ratCode, String corpId, String itemFlag,
            String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as disCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" re.assurepercent as assurepercent,re.interestrate as interestrate, ")
                .append(" rd.super_Process_Money as superProcessMoney,rd.super_Process_Open_Money as superProcessOpenMoney, ")
                .append(" rd.super_Relief_Money as superReliefMoney,rd.super_Relief_Open_Money as superReliefOpenMoney, ")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,rd.super_By_Relief_Open_Money as superByReliefOpenMoney, ")
                .append(" rd.freeze_Money as freeze_Money,rd.freeze_Open_Money as freezeOpenMoney, ")
                .append(" rd.virtual_Relief_Money as virtualReliefMoney,rd.virtual_Relief_Open_Money as virtualReliefOpenMoney, ")
                .append(" rd.virtual_Uration as virtualUration,rd.virtual_Open_Uration as virtualOpenUration, ")
                .append(" rd.relief_And_Uration_Money as reliefAndUrationMoney,rd.uration_Open_Money as urationOpenMoney, ")
                .append(" (rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money - rd.freeze_Max_Money - rd.virtual_Max_Money) as maxMoney, ")
                .append(" case when rd.credit_type is null then '1' else rd.credit_type end as creditType, ")
                .append(" re.comp_sign as compSign ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_dis rb,rat_bkration_detail re ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code and re.rat_code = rb.rat_code ")
                .append(" and rb.id = rd.dis_code and rb.rat_code = :ratCode ")
                .append(" and rb.corp_id = :corpId and rb.status = :status ");
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            hql.append(" and rd.item_flag = :itemFlag and rd.item_code = :itemCode ");
            params.put("itemCode", itemCode);// 授信品种
        } else {
            hql.append(" and rd.item_flag = :itemFlag and re.comp_sign = :compSign ");
            params.put("compSign", RationConstant.RAT_COMP_SIGN_1);// 综合授信使用品种标识
        }
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        params.put("ratCode", ratCode);// 授信录入编号
        params.put("corpId", corpId);// 单位
        params.put("itemFlag", itemFlag);// 授信品种类型
        params.put("status", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationCpAddDtlDTO.class);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已分配的授信品种额度。<br>
     * <b>日期：</b>2017年8月15日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationCpAddDtlDTO> getRationDisDtlByDisDtlId(String disId, String itemFlag, String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" re.assurepercent as assurepercent,re.interestrate as interestrate, ")
                .append(" rd.super_Process_Money as superProcessMoney,rd.super_Process_Open_Money as superProcessOpenMoney, ")
                .append(" rd.super_Relief_Money as superReliefMoney,rd.super_Relief_Open_Money as superReliefOpenMoney, ")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,rd.super_By_Relief_Open_Money as superByReliefOpenMoney, ")
                .append(" rd.freeze_Money as freeze_Money,rd.freeze_Open_Money as freezeOpenMoney, ")
                .append(" rd.virtual_Relief_Money as virtualReliefMoney,rd.virtual_Relief_Open_Money as virtualReliefOpenMoney, ")
                .append(" rd.virtual_Uration as virtualUration,rd.virtual_Open_Uration as virtualOpenUration, ")
                .append(" rd.relief_And_Uration_Money as reliefAndUrationMoney,rd.uration_Open_Money as urationOpenMoney, ")
                .append(" (rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money - rd.freeze_Max_Money - rd.virtual_Max_Money) as maxMoney, ")
                .append(" re.comp_sign as compSign,rb.status ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_dis rb,rat_bkration_detail re ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code and re.rat_code = rb.rat_code ")
                .append(" and rb.id = rd.dis_code and rb.id = :disId ")
                .append(" and (rb.status >= :status or rb.status=-3) ");
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            hql.append(" and rd.item_flag = :itemFlag and rd.item_code = :itemCode ");
            params.put("itemCode", itemCode);// 授信品种
        } else {
            hql.append(" and rd.item_flag = :itemFlag ");
        }
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        params.put("disId", disId);// 授信品种类型
        params.put("itemFlag", itemFlag);// 授信品种类型
        params.put("status", RationConstant.STATUS_WAIT_CHECK);
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationCpAddDtlDTO.class);
    }
    /**
     * 功能介绍 : 根据授信编号获取单位已分配的授信品种额度。<br>
     * <b>日期：</b>2017年8月15日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationCpAddDtlDTO> getRationDisDtlByRatCode(String ratCode, String itemFlag, String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" re.assurepercent as assurepercent,re.interestrate as interestrate, ")
                .append(" rd.super_Process_Money as superProcessMoney,rd.super_Process_Open_Money as superProcessOpenMoney, ")
                .append(" rd.super_Relief_Money as superReliefMoney,rd.super_Relief_Open_Money as superReliefOpenMoney, ")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,rd.super_By_Relief_Open_Money as superByReliefOpenMoney, ")
                .append(" rd.freeze_Money as freeze_Money,rd.freeze_Open_Money as freezeOpenMoney, ")
                .append(" rd.virtual_Relief_Money as virtualReliefMoney,rd.virtual_Relief_Open_Money as virtualReliefOpenMoney, ")
                .append(" rd.virtual_Uration as virtualUration,rd.virtual_Open_Uration as virtualOpenUration, ")
                .append(" rd.relief_And_Uration_Money as reliefAndUrationMoney,rd.uration_Open_Money as urationOpenMoney, ")
                .append(" (rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money - rd.freeze_Max_Money - rd.virtual_Max_Money) as maxMoney, ")
                .append(" re.comp_sign as compSign ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_dis rb,rat_bkration_detail re ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code and re.rat_code = rb.rat_code ")
                .append(" and rb.id = rd.dis_code and rb.rat_code = :ratCode ")
                .append(" and (rb.status >= :status or rb.status=-3) and rb.distribute_way <> :disWay ");
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            hql.append(" and rd.item_flag = :itemFlag and rd.item_code = :itemCode ");
            params.put("itemCode", itemCode);// 授信品种
        } else {
            hql.append(" and rd.item_flag = :itemFlag ");
        }
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        params.put("ratCode", ratCode);// 授信品种类型
        params.put("itemFlag", itemFlag);// 授信品种类型
        params.put("disWay", RationConstant.DIS_TYPE_AMOUNT_SHARE);// 非总额共享
        params.put("status", RationConstant.STATUS_WAIT_CHECK);
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationCpAddDtlDTO.class);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年8月16日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDisDtlDTO> getRationDisDtlByRatCodes(String disCode, String itemFlag, String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.max_money as maxMoney,rd.freeze_money as freezeMoney,rd.freeze_open_money as freezeOpenMoney, ")
                .append(" rd.virtual_Relief_Money as virtualReliefMoney,rd.virtual_Relief_Open_Money as virtualReliefOpenMoney, ")
                .append(" rd.super_Relief_Money as superReliefMoney,rd.super_Relief_Open_Money as superReliefOpenMoney, ")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,rd.super_By_Relief_Open_Money as superByReliefOpenMoney, ")
                .append(" re.comp_sign as compSign  ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_detail re, rat_bkration_dis rb ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code ")
                .append(" and re.rat_code = rb.rat_code and rd.dis_code = rb.id ")
                .append(" and rd.dis_code = :disCode ");
        if (!StringUtils.isEmpty(itemFlag)) {
            if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                hql.append(" and rd.item_code = :itemCode ");
                params.put("itemCode", itemCode);
            } else {
                hql.append(" and re.comp_sign = :compSign ");
                params.put("compSign", RationConstant.RAT_COMP_SIGN_1);
            }
            hql.append(" and re.item_flag = :itemFlag ");
            params.put("itemFlag", itemFlag);
        }
        params.put("disCode", disCode);
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationDisDtlDTO.class);
    }
    /**
     * 功能介绍 : 获取总额共享下其他授信分配单位的同品种额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDisDtlDTO> getAmountShareRationDisDtl(String ratCode, String disCode, String itemFlag,
            String itemCode, String corpId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.max_money as maxMoney,rd.freeze_money as freezeMoney,rd.freeze_open_money as freezeOpenMoney, ")
                .append(" rd.super_Process_Money as superProcessMoney,rd.super_Process_Open_Money as superProcessOpenMoney, ")
                .append(" rd.super_Relief_Money as superReliefMoney,rd.super_Relief_Open_Money as superReliefOpenMoney, ")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,rd.super_By_Relief_Open_Money as superByReliefOpenMoney, ")
                .append(" rd.virtual_Relief_Money as virtualReliefMoney,rd.virtual_Relief_Open_Money as virtualReliefOpenMoney, ")
                .append(" rd.virtual_Uration as virtualUration,rd.virtual_Open_Uration as virtualOpenUration, ")
                .append(" rd.relief_And_Uration_Money as reliefAndUrationMoney,rd.uration_Open_Money as urationOpenMoney, ")
                .append(" re.comp_sign as compSign ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_detail re, rat_bkration_dis rb ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code ")
                .append(" and re.rat_code = rb.rat_code and rd.dis_code = rb.id and rb.corp_id <> :corpId ");
        if (!StringUtils.isEmpty(itemFlag)) {
            if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                hql.append(" and rd.item_code = :itemCode ");
                params.put("itemCode", itemCode);
            } else {
                hql.append(" and re.comp_sign = :compSign ");
                params.put("compSign", RationConstant.RAT_COMP_SIGN_1);
            }
            hql.append(" and re.item_flag = :itemFlag ");
            params.put("itemFlag", itemFlag);
        }
        hql.append(
                " and exists (select 1 from rat_bkration_dis rbd where rbd.id = rd.dis_code and rbd.status <> :status ");
        hql.append(
                " and rbd.rat_code = :ratCode and rbd.corp_id <> :corpId and rbd.id <> :disCode and rb.distribute_way = :disWay ) ");
        hql.append(
                " and exists (select 1 from rat_bkration_dis dis where dis.rat_code = rb.rat_code and dis.distribute_way = :disWay ");
        hql.append(" and dis.id = :disCode ) ");
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        params.put("ratCode", ratCode);
        params.put("disCode", disCode);
        params.put("corpId", corpId);
        params.put("status", CommonConstant.STATUE_DELETE);
        params.put("disWay", RationConstant.DIS_TYPE_AMOUNT_SHARE);// 总额共享
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationDisDtlDTO.class);
    }
    /**
     * 功能介绍 : 更新原授信分配中最高额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void updateRationDisMaxMoney(RatBkrationDisDtlDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append("  update rat_bkration_dis_detail  ");
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
     * 功能介绍 : 根据系统授信品种获取已分配授信的授信品种。<br>
     * <b>日期：</b>2017年8月22日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatItemDisVO> getItemCustomDTOBySysItem(String disCode, String sysItemCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select rb.id as disCode, rb.rat_code as ratCode, ");
        sql.append(" rd.item_flag as itemFlag, rd.item_order as itemOrder, rd.money as money, rd.open_money as openMoney, ");
        sql.append(" (rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money - rd.freeze_Max_Money - rd.virtual_max_money) as maxMoney, ");
        sql.append(" case when rd.credit_type is null then '1' else rd.credit_type end as creditType, ");
        // 录入明细表保证金字段
        sql.append(" re.BOND_MONEY as bondMoney, re.assurepercent as assurepercent, re.interestrate as interestrate, re.comp_sign as compSign, ");
        // 授信品种字段
        sql.append(" rc.id as itemCode, rc.NAME as itemName, rc.sys_item_code as sysItemCode ");
        sql.append(" from RAT_BKRATION_DIS rb ");
        sql.append(" inner join rat_bkration_dis_detail rd on rb.id = rd.dis_code ");
        sql.append(" inner join rat_bkration_detail re on rb.rat_code = re.rat_code and rd.item_code = re.item_code ");
        sql.append(" inner join rat_item_custom rc on rd.item_Code = rc.id and rc.status = 0 ");
        sql.append(" where rd.dis_code = :disCode ");
        if (StringUtils.isNotEmpty(sysItemCode)) {
            sql.append(" and rc.sys_item_code = :sysItemCode ");
            params.put("sysItemCode", sysItemCode);
        }
        sql.append(" order by rd.item_order ");
        params.put("disCode", disCode);
        return listBySQLAliasToBean(sql.toString(), params, RatItemDisVO.class);
    }
    /**
     * 功能介绍 : 根据系统授信品种获取已分配授信的授信品种。<br>
     * <b>日期：</b>2017年8月22日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatItemDisVO> getRationDisCompItemCustom(String disCode, String itemFlag) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select rb.id as disCode,rc.id as itemCode,rc.NAME as itemName, ");
        sql.append(" rd.item_flag as itemFlag,rd.item_order as itemOrder, ");
        sql.append(" rd.money as money,rd.open_money as openMoney, rd.max_money as maxMoney, ");
        sql.append(" re.assurepercent as assurepercent,re.interestrate as interestrate,re.comp_sign as compSign ");
        sql.append(" from RAT_BKRATION_DIS rb,RAT_BKRATION_DIS_DETAIL rd,RAT_ITEM_CUSTOM rc,rat_bkration_detail re ");
        sql.append(" where rb.id = rd.dis_code and rd.item_Code = rc.id  ");
        sql.append(" and re.rat_code = rb.rat_code and rd.item_code = re.item_code ");
        sql.append(" and rd.dis_code = :disCode ");
        sql.append(" and re.item_flag = :itemFlag ");
        sql.append(" order by rd.item_order ");
        params.put("disCode", disCode);
        params.put("itemFlag", itemFlag);
        return listBySQLAliasToBean(sql.toString(), params, RatItemDisVO.class);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年8月24日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDisDetail> getRationDisDtlByDisCode(String disCode, String itemFlag, String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd.id as id,rd.DIS_CODE as disCode,rd.ITEM_CODE as itemCode,rd.ITEM_FLAG as itemFlag,");
        sql.append(" rd.MONEY as money,rd.OPEN_MONEY as openMoney,rd.SUPER_PROCESS_MONEY as superProcessMoney,");
        sql.append(" rd.SUPER_RELIEF_MONEY as superReliefMoney,rd.SUPER_BY_RELIEF_MONEY as superByReliefMoney,");
        sql.append(
                " rd.FREEZE_MONEY as freezeMoney,rd.RELIEF_AND_URATION_MONEY as reliefAndUrationMoney,rd.FREEZE_OPEN_MONEY as freezeOpenMoney,");
        sql.append(
                " rd.SUPER_BY_RELIEF_OPEN_MONEY as superByReliefOpenMoney,rd.SUPER_RELIEF_OPEN_MONEY as superReliefOpenMoney,");
        sql.append(
                " rd.SUPER_PROCESS_OPEN_MONEY as superProcessOpenMoney,rd.VIRTUAL_RELIEF_OPEN_MONEY as virtualReliefOpenMoney,");
        sql.append(
                " rd.VIRTUAL_RELIEF_MONEY as virtualReliefMoney,rd.VIRTUAL_URATION as virtualUration,rd.URATION_OPEN_MONEY as urationOpenMoney,");
        sql.append(
                " rd.ITEM_ORDER as itemOrder,rd.MAX_MONEY as maxMoney,rd.VIRTUAL_OPEN_URATION as virtualOpenUration,");
        sql.append(
                " rd.PROCESS_MAX_MONEY as processMaxMoney,rd.RELIEF_MAX_MONEY as reliefMaxMoney,rd.BY_RELIEF_MAX_MONEY as byReliefMaxMoney,");
        sql.append(
                " rd.FREEZE_MAX_MONEY as freezeMaxMoney, rd.COMP_SIGN as compSign,rd.VIRTUAL_MAX_MONEY as virtualMaxMoney,");
        sql.append(" rd.CREDIT_TYPE as creditType ");
        sql.append(" from RAT_BKRATION_DIS_DETAIL rd where rd.DIS_CODE = :disCode ");
        if (!StringUtils.isEmpty(itemFlag)) {
            if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                sql.append(" and rd.ITEM_CODE = :itemCode ");
                params.put("itemCode", itemCode);
            }
            sql.append(" and rd.ITEM_FLAG = :itemFlag ");
            params.put("itemFlag", itemFlag);
        }
        params.put("disCode", disCode);
        sql.append(" order by rd.DIS_CODE,rd.ITEM_ORDER ");
        sql.append(" for update ");
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationDisDetail.class);
    }
    /**
     * 功能介绍 : 获取总额共享下其他单位的品种分配详情。<br>
     * <b>日期：</b>2017年8月24日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDisDetail> getOtherRationDisDtlByDisCode(String ratCode, String disCode, String itemFlag,
            String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd from RatBkrationDisDetail rd where  ");
        hql.append(" exists ( select 1 from RatBkrationDis rb where rb.id = rd.disCode and rb.status = :status ");
        hql.append(" and rb.ratCode = :ratCode and rb.distributeWay = :disWay and rb.id <> :disCode ) ");
        if (!StringUtils.isEmpty(itemFlag)) {
            if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                hql.append(" and rd.itemCode = :itemCode ");
                params.put("itemCode", itemCode);
            }
            hql.append(" and rd.itemFlag = :itemFlag ");
            params.put("itemFlag", itemFlag);
        }
        params.put("disCode", disCode);
        params.put("ratCode", ratCode);
        params.put("disWay", RationConstant.DIS_TYPE_AMOUNT_SHARE);// 总额共享
        params.put("status", CommonConstant.STATUE_CHECK);
        hql.append(" order by rd.disCode,rd.itemOrder ");
        return listByHQL(hql.toString(), params);
    }
    /**
     * 功能介绍 : 根据授信信息获取单位已分配的授信明细集合。<br>
     * <b>日期：</b>2017年8月29日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDisDtlDTO> getRationDisDtlByCorpItem(String ratCode, String corpId, String itemFlag,
            String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as disCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.max_money as maxMoney,re.assurepercent as assurepercent,re.interestrate as interestrate, ")
                .append(" rd.super_Process_Money as superProcessMoney,rd.super_Process_Open_Money as superProcessOpenMoney, ")
                .append(" rd.super_Relief_Money as superReliefMoney,rd.super_Relief_Open_Money as superReliefOpenMoney, ")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,rd.super_By_Relief_Open_Money as superByReliefOpenMoney, ")
                .append(" rd.freeze_Money as freeze_Money,rd.freeze_Open_Money as freezeOpenMoney, ")
                .append(" rd.virtual_Relief_Money as virtualReliefMoney,rd.virtual_Relief_Open_Money as virtualReliefOpenMoney, ")
                .append(" rd.virtual_Uration as virtualUration,rd.virtual_Open_Uration as virtualOpenUration, ")
                .append(" rd.relief_And_Uration_Money as reliefAndUrationMoney,rd.uration_Open_Money as urationOpenMoney, ")
                .append(" rd.process_Max_Money as processMaxMoney,rd.relief_Max_Money as reliefMaxMoney, ")
                .append(" rd.by_Relief_Max_Money as byReliefMaxMoney,rd.freeze_Max_Money as freezeMaxMoney, ")
                .append(" rd.virtual_max_money as virtualMaxMoney,re.comp_sign as compSign, ")
                .append(" case when rd.credit_type is null then '1' else rd.credit_type end as creditType ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_dis rb,rat_bkration_detail re ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code and re.rat_code = rb.rat_code ")
                .append(" and rb.id = rd.dis_code and rb.rat_code = :ratCode ")
                .append(" and rb.corp_id = :corpId and rb.status = :status ")
                .append(" and bt_nvl_char(rd.credit_type, '1') <>0 ");// 过滤不循环授信
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            hql.append(" and rd.item_flag = :itemFlag and rd.item_code = :itemCode ");
            params.put("itemCode", itemCode);// 授信品种
        } else {
            hql.append(" and rd.item_flag = :itemFlag ");
            params.put("compSign", RationConstant.RAT_COMP_SIGN_1);// 综合授信使用品种标识
        }
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        params.put("ratCode", ratCode);// 授信录入编号
        params.put("corpId", corpId);// 单位
        params.put("itemFlag", itemFlag);// 授信品种类型
        params.put("status", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationDisDtlDTO.class);
    }
    /**
     * 功能介绍 : 获取银行授信已分配授信品种对应的系统品种。<br>
     * <b>日期：</b>2017年10月11日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatSysItemDTO> getRationDisSysItem(String disCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.code as code, a.name as name, a.type as type, ")
                .append(" a.flag as flag from rat_sys_item a where a.type = :type ");
        if (!EmptyUtils.isEmpty(disCode)) {
            sql.append(" and exists ( select 1 from rat_bkration_dis_detail rd,rat_item_custom rc  ");
            sql.append(" where rd.item_code = rc.id and a.code = rc.sys_item_code and rd.dis_code = :disCode ) ");
        }
        sql.append(" order by a.code ");
        params.put("type", RationConstant.RAT_TYPE_0);// 银行授信
        params.put("disCode", disCode);
        return listBySQLAliasToBean(sql.toString(), params, RatSysItemDTO.class);
    }
    /**
     * 获取内部授信分配明细
     * 
     * @author luxy
     * @param
     * @return
     */
    public List<RatBkrationCpAddDtlDTO> getCpRationDisDtlByRatCode(String ratCode, String itemFlag, String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,")
                .append(" re.assurepercent as assurepercent,re.interestrate as interestrate, ")
                .append(" rd.super_Process_Money as superProcessMoney,")
                .append(" rd.super_Relief_Money as superReliefMoney,")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,").append(" rd.freeze_Money as freeze_Money, ")
                .append(" (rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money - rd.freeze_Max_Money) as maxMoney, ")
                .append(" re.comp_sign as compSign ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_dis rb,rat_bkration_detail re ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code and re.rat_code = rb.rat_code ")
                .append(" and rb.id = rd.dis_code and rb.rat_code = :ratCode ").append(" and rb.status >= :status ");
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            hql.append(" and rd.item_flag = :itemFlag and rd.item_code = :itemCode ");
            params.put("itemCode", itemCode);// 授信品种
        } else {
            hql.append(" and rd.item_flag = :itemFlag ");
        }
        hql.append(" and rb.distribute_way !=:distributeWay ");
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        params.put("ratCode", ratCode);// 授信品种类型
        params.put("itemFlag", itemFlag);// 授信品种类型
        params.put("status", RationConstant.STATUS_WAIT_CHECK);
        params.put("distributeWay", RationConstant.DIS_WAY_AUTO);// 过滤自动分配的那笔数据
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationCpAddDtlDTO.class);
    }
    /**
     * 获取内部授信自动分配明细
     * 
     * @author luxy
     * @param
     * @return
     */
    public RatBkrationCpAddDtlDTO getCpRationDisDtl(String ratCode, String itemFlag, String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,")
                .append(" re.assurepercent as assurepercent,re.interestrate as interestrate, ")
                .append(" rd.super_Process_Money as superProcessMoney,")
                .append(" rd.super_Relief_Money as superReliefMoney,")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,").append(" rd.freeze_Money as freeze_Money, ")
                .append(" (rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money - rd.freeze_Max_Money) as maxMoney, ")
                .append(" re.comp_sign as compSign ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_dis rb,rat_bkration_detail re ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code and re.rat_code = rb.rat_code ")
                .append(" and rb.id = rd.dis_code and rb.rat_code = :ratCode ").append(" and rb.status >= :status ");
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            hql.append(" and rd.item_flag = :itemFlag and rd.item_code = :itemCode ");
            params.put("itemCode", itemCode);// 授信品种
        } else {
            hql.append(" and rd.item_flag = :itemFlag ");
        }
        hql.append(" and rb.distribute_way =:distributeWay ");
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        params.put("ratCode", ratCode);// 授信品种类型
        params.put("itemFlag", itemFlag);// 授信品种类型
        params.put("status", RationConstant.STATUS_WAIT_CHECK);
        params.put("distributeWay", RationConstant.DIS_WAY_AUTO);// 自动分配的那笔数据
        return getBySQLAliasToBean(hql.toString(), params, RatBkrationCpAddDtlDTO.class);
    }
    /**
     * 根据授信录入编号和分配方式获取已分配的金额之和
     * 
     * @param ratCode
     *            授信录入id
     * @param disWay
     *            授信分配方式
     * @return
     */
    public List<RationDisBean> getRationDisMoneySum(String ratCode, String disWay) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select rd.item_code as itemCode, ");
        sql.append(" (CASE WHEN rd.item_flag=0 THEN");
        sql.append(" sum(rd.money + rd.super_by_relief_money - rd.super_relief_money) ");
        sql.append(" ELSE ");
        sql.append(" sum(rd.max_Money + rd.super_by_relief_money - rd.super_relief_money) ");
        sql.append(" END) as money, ");
        sql.append(" sum(rd.open_money + rd.super_by_relief_open_money - rd.super_relief_open_money) as open_money ");
        sql.append("  from rat_bkration_dis_detail rd  ");
        sql.append(" where exists (select 1 from rat_bkration_dis rb where rb.id = rd.dis_code ");
        sql.append(" and status >= '-1' and status <= '95' and rb.rat_code = :ratCode) ");
        if (StringUtils.isNotEmpty(disWay)) {
            sql.append(" and rb.distribute_way = :disWay ");
            params.put("disWay", disWay);
        }
        sql.append(" group by rd.item_code,rd.item_flag ");
        params.put("ratCode", ratCode);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 根据条件查询授信分配明细
     * 
     * @param disCode
     * @param itemFlag
     * @param itemCode
     * @return
     */
    public List<RatBkrationDisDtlDTO> getRationDisDtlByParams(String disCode, String itemFlag, String itemCode,
            String compSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.max_money as maxMoney,rd.freeze_money as freezeMoney,rd.freeze_open_money as freezeOpenMoney, ")
                .append(" rd.virtual_Relief_Money as virtualReliefMoney,rd.virtual_Relief_Open_Money as virtualReliefOpenMoney, ")
                .append(" rd.super_Relief_Money as superReliefMoney,rd.super_Relief_Open_Money as superReliefOpenMoney, ")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,rd.super_By_Relief_Open_Money as superByReliefOpenMoney, ")
                .append(" re.comp_sign as compSign, ").append(" rd.relief_and_uration_money as reliefAndUrationMoney, ")
                .append(" rd.virtual_uration as virtualUration, ")
                .append(" rd.super_process_open_money as superProcessOpenMoney, ")
                .append(" rd.uration_open_money as urationOpenMoney, ")
                .append(" rd.virtual_open_uration as virtualOpenUration,rd.super_process_money as superProcessMoney,rd.process_max_money as processMaxMoney, ")
                .append(" rd.relief_max_money reliefMaxMoney,rd.freeze_max_money as freezeMaxMoney,rd.virtual_max_money as virtualMaxMoney,rd.by_relief_max_money as byReliefMaxMoney ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_detail re, rat_bkration_dis rb ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code ")
                .append(" and re.rat_code = rb.rat_code and rd.dis_code = rb.id ")
                .append(" and rd.dis_code = :disCode ");
        if (EmptyUtils.isNotEmpty(itemCode)) {
            hql.append(" and rd.item_code = :itemCode ");
            params.put("itemCode", itemCode);
        }
        if (!StringUtils.isEmpty(itemFlag)) {
            hql.append(" and re.item_flag = :itemFlag ");
            params.put("itemFlag", itemFlag);
        }
        if (!StringUtils.isEmpty(compSign)) {
            hql.append(" and rd.COMP_SIGN = :compSign ");
            params.put("compSign", compSign);
        }
        params.put("disCode", disCode);
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationDisDtlDTO.class);
    }
    /**
     * 功能介绍 : 根据授信ID获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2021年12月18日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationDtlVm> getBkrationDtlByRatCode(String ratCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.super_relief_money as superReliefMoney, rd.super_by_relief_money as superByReliefMoney, ")
                .append(" rd.super_relief_open_money as superReliefOpenMoney, rd.super_by_relief_open_money as superByReliefOpenMoney, ")
                .append(" rd.relief_max_money as reliefMaxMoney, rd.by_relief_max_money as byReliefMaxMoney, ")
                .append(" rd.max_money as maxMoney ").append(" from rat_bkration_dis_detail rd, rat_bkration_dis rb ")
                .append(" where rd.dis_code = rb.id and rb.status = 95 and rb.rat_code = :ratCode ");
        sql.append(" order by rd.dis_Code,rd.item_order ");
        params.put("ratCode", ratCode);
        return listBySQLAliasToBean(sql.toString(), params, RatBkRationDtlVm.class);
    }
    /**
     * <br>
     * <b>功能：</b> 更新授信分配明细占用和在途金额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/12/20 0020 <br>
     */
    public void updateRationDisDetail(RatUrationDisBean disBean) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " update rat_bkration_dis_detail set super_process_money = :useMoney, freeze_Money = :freezeMoney, ");
        sql.append(" relief_and_uration_money = :reliefAndUrationMoney, virtual_uration = :virtualUration ");
        sql.append(" where id = :id ");
        params.put("id", disBean.getId());
        params.put("useMoney", disBean.getUseMoney());
        params.put("freezeMoney", disBean.getFreezeMoney());// 冻结
        params.put("reliefAndUrationMoney", disBean.getReliefAndUrationMoney());// 虚拟占用
        params.put("virtualUration", disBean.getVirtualUration());// 虚拟在途占用
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * <br>
     * <b>功能：</b> 更新综合授信分配明细占用和在途金额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/12/20 0020 <br>
     */
    public void updateCompRationDisDetail(RatUrationUseDtlVO disBean) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" update rat_bkration_dis_detail set ");
        sql.append(
                " process_max_money = :processMaxMoney,freeze_max_money = :freezeMaxMoney,virtual_max_money = :virtualMaxMoney ");
        sql.append(" where id = :id ");
        params.put("id", disBean.getId());
        params.put("processMaxMoney", disBean.getUseMoney());
        params.put("freezeMaxMoney", disBean.getApproveMoney());// 冻结
        params.put("virtualMaxMoney", disBean.getVirtualMaxMoney());// 虚拟占用或虚拟在途占用
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * <br>
     * <b>功能：</b> 更新综合授信分配明细占用和在途金额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/12/20 0020 <br>
     */
    public void updateFirstCompRationDisDetail(RatUrationUseDtlVO disBean) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" update rat_bkration_dis_detail set ");
        if (RationConstant.RAT_COMFLAG_1.equals(disBean.getCompSign())) {
            sql.append(" super_process_money = :useMoney, freeze_Money = :freezeMoney, ");
            sql.append(" relief_and_uration_money = :reliefAndUrationMoney, virtual_uration = :virtualUration, ");
            params.put("useMoney", disBean.getUseMoney());
            params.put("freezeMoney", disBean.getApproveMoney());// 冻结
            params.put("reliefAndUrationMoney", disBean.getVirtualUrationMaxMoney());// 虚拟占用
            params.put("virtualUration", disBean.getVirtualMaxMoney());// 虚拟在途占用
        }
        sql.append(
                " process_max_money = :processMaxMoney,freeze_max_money = :freezeMaxMoney,virtual_max_money = :virtualMaxMoney ");
        sql.append(" where id = :id ");
        params.put("id", disBean.getId());
        params.put("processMaxMoney", disBean.getUseMoney());
        params.put("freezeMaxMoney", disBean.getApproveMoney());// 冻结
        params.put("virtualMaxMoney", disBean.getVirtualMaxMoney());// 虚拟占用或虚拟在途占用
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
