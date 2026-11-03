package com.bytter.credit.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import org.springframework.stereotype.Repository;
import com.bytter.credit.domain.entity.RatBkrationReliefDetail;
import com.bytter.credit.domain.vo.RatUrationQueryVO;
import com.bytter.credit.domain.vo.RatUrationUseBeanVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
/**
 * @author fusb
 * @version 1.0
 * @Title RatCprationReliefDetailDao
 * @Description
 * @date 2018年11月24日
 */
@Repository
public class RatCprationReliefDetailDao extends BaseDao<RatBkrationReliefDetail, String> {
    /**
     * 功能介绍 : 获取调剂明细。
     * 
     * @param refCode
     * @param refType
     * @return
     */
    public List<RatBkRationDtlVm> getRationReliefDtl(String refCode, String refType) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.relief_Item_Code as itemCode, rs.comp_sign as compSign,rd.relief_Max_Money as money, ")
                .append(" rd.id as id,rd.relief_money as reliefMoney ")
                .append(" from RAT_BKRATION_RELIEF_DETAIL rd,RAT_BKRATION_DIS_DETAIL rs ")
                .append(" where rd.relief_code = rs.dis_code and rd.relief_item_code = rs.item_code  ")
                .append(" and rd.ref_Type = :refType and ref_code = :refCode and rd.item_flag = :itemFlag ");
        params.put("refCode", refCode);
        params.put("refType", refType);
        params.put("itemFlag", RationConstant.RAT_COMFLAG_1);
        return listBySQLAliasToBean(hql.toString(), params, RatBkRationDtlVm.class);
    }
    /**
     * 功能介绍 : 获取调剂明细。
     * 
     * @param refCode
     * @return
     */
    public List<RatBkrationReliefDetail> getRationReliefDtlByRefCode(String refCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd ").append(" from RatBkrationReliefDetail rd ").append(" where rd.refCode = :refCode ")
                .append(" order by rd.refType,rd.reliefItemCode ");
        params.put("refCode", refCode);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 功能介绍 : 删除原授信调剂明细。
     * 
     * @param refCode
     */
    public void deleteCprationRefDtlByCode(String refCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from RAT_BKRATION_RELIEF_DETAIL where ref_Code = :refCode ");
        params.put("refCode", refCode);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 更新调剂实际最高调剂额度。
     * 
     * @param dtlDTO
     */
    public void updateBkrationReliefMaxMoney(RatBkRationDtlVm dtlDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" update RAT_BKRATION_RELIEF_DETAIL rd set rd.relief_money = :reliefMoney ");
        sql.append(" where rd.id = :id ");
        params.put("id", dtlDTO.getId());
        params.put("reliefMoney", dtlDTO.getReliefMoney());// 实际调剂金额
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 查询授信调剂在审占用金额。
     * 
     * @param queryVO
     * @return
     */
    public List<RatUrationUseBeanVO> queryRationReliefUseDtl(RatUrationQueryVO queryVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(" select ru.id as id, ru.relief_item_flag as itemFlag, ru.corp_id as corpId, rb.corp_name as corpName, ");
        sql.append(" '06' as businessName, ");
        sql.append(
                " rb.cur_code as curCode, rb.cur_name as curName, rb.cur_code as initCurCode, rb.cur_name as initCurName, ");
        if (RationConstant.RAT_COMFLAG_1.equals(queryVO.getItemFlag()) && !EmptyUtils.isEmpty(queryVO.getItemCode())) {// 查询综合品种明细
            sql.append(
                    " rf.relief_item_code as itemCode, rc.name as itemName, rf.relief_max_money as money, rf.relief_max_money as convertPreMoney, ");
        } else {
            sql.append(
                    " ru.relief_item_code as itemCode, rm.name as itemName, ru.relief_money as money, ru.relief_money as convertPreMoney, ");
        }
//        sql.append(" bc.cur_exrate as exchangeRate, ru.create_date as beginDate, rb.enddate as endDate, ");
        //TODO 程序补充利率
        sql.append(" ru.create_date as beginDate, rb.enddate as endDate, ");
        sql.append(" ru.id  as billId, ru.ref_code as billNo, ru.status as status ");
        sql.append(" from rat_bkration_relief ru ");
        if (RationConstant.RAT_COMFLAG_1.equals(queryVO.getItemFlag()) && !EmptyUtils.isEmpty(queryVO.getItemCode())) {// 查询综合品种明细
            sql.append(" left join rat_bkration_relief_detail rf on rf.ref_code = ru.id ");
        }
        sql.append(" left join rat_bkration rb on rb.id = ru.rat_code ");
//        sql.append(" left join sys_corp sc on ru.relief_corp = sc.id ");
//        sql.append(" left join bt_currency bc on ru.cur_code = bc.id ");
        if (RationConstant.RAT_COMFLAG_1.equals(queryVO.getItemFlag()) && !EmptyUtils.isEmpty(queryVO.getItemCode())) {// 查询综合品种明细
            sql.append(" left join rat_item_custom rc on rf.relief_item_code = rc.id ");
        } else {
            sql.append(" left join rat_item_custom rm on ru.relief_item_code = rm.id ");
        }
        sql.append(
                " where rb.rat_type = '0' and ru.relief_corp = :corpId and ru.rat_code = :ratCode and ru.status = :status ");
        if (RationConstant.RAT_COMFLAG_1.equals(queryVO.getItemFlag()) && !EmptyUtils.isEmpty(queryVO.getItemCode())) {// 查询综合品种明细
            sql.append(" and rf.ref_type = :refType ");
            params.put("refType", RationConstant.RAT_REF_TYPE_0);// 调剂方
        }
        if (EmptyUtils.isNotEmpty(queryVO.getCurCode())) {
            sql.append(" and ru.cur_code = :curCode ");
            params.put("curCode", queryVO.getCurCode());
        }
        if (EmptyUtils.isNotEmpty(queryVO.getItemFlag())) {
            sql.append(" and ru.relief_item_flag = :itemFlag ");
            params.put("itemFlag", queryVO.getItemFlag());
            if (RationConstant.RAT_COMFLAG_0.equals(queryVO.getItemFlag())) {
                if (EmptyUtils.isNotEmpty(queryVO.getItemCode())) {
                    sql.append(" and ru.relief_item_code = :itemCode ");
                    params.put("itemCode", queryVO.getItemCode());
                }
            } else {
                if (EmptyUtils.isNotEmpty(queryVO.getItemCode())) {
                    sql.append(" and rf.relief_item_code = :itemCode and rf.relief_max_money > 0 ");
                    params.put("itemCode", queryVO.getItemCode());
                }
            }
        }
        sql.append(" order by ru.create_date desc ");
        params.put("corpId", queryVO.getCorpId());
        params.put("status", RationConstant.STATUS_WAIT_CHECK);
        params.put("ratCode", queryVO.getRatCode());
        return listBySQLAliasToBean(sql.toString(), params, RatUrationUseBeanVO.class);
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
                .append(" re.comp_sign as compSign ")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_dis rb,rat_bkration_detail re ")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code and re.rat_code = rb.rat_code ")
                .append(" and rb.id = rd.dis_code and rb.id = :disId ").append(" and rb.status >= :status ");
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
}
