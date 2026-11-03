package com.bytter.credit.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationDisDetail;
import com.bytter.credit.domain.entity.RatDisDetailHis;
import com.bytter.credit.domain.vo.RatDisDetailHisVO;
import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatDisDetailHisDao
 * @Description:授信分配历史明细Dao
 * @date 2018年11月26日 下午6:40:14
 */
@Repository
public class RatDisDetailHisDao extends BaseDao<RatDisDetailHis, String> {
    /**
     * 根据分配id查询分配明细
     */
    public List<RatDisDetailHis> queryDisDetailHisByDisCode(String disCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rh.dis_code as disCode,rh.item_order as itemOrder,rh.item_code as itemCode")
                .append(" rh.item_flag as itemFlag,rh.money as money,rh.status as status")
                .append(" from RAT_RATION_DETAIL_DISHIS rh").append(" where rh.disCode=:disCode");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("disCode", disCode);
        return listBySQLAliasToBean(sql.toString(), params, RatDisDetailHis.class);
    }
    /**
     * 根据分配编号查询历史分配明细
     */
    public List<RatDisDetailHisVO> queryDisHisListByDisCode(String disCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rh.dis_code as disCode,rh.item_order as itemOrder,rh.item_code as itemCode")
                .append(" rc.name as itemName,rh.item_flag as itemFlag,rh.money as money,rh.status as status")
                .append(" from RAT_RATION_DETAIL_DISHIS rh,rat_item_custom rc")
                .append(" where rh.item_code = rc.id and rh.disCode=:disCode");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("disCode", disCode);
        return listBySQLAliasToBean(sql.toString(), params, RatDisDetailHisVO.class);
    }
    /**
     * 根据授信分配编号获取授信分配明细
     */
    public List<RatBkRationDtlVm> getCprationDtlByDisCodes(List<String> disCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rh.id, rh.dis_code as ratCode, rh.item_flag as itemFlag,")
                .append(" rh.item_order as itemOrder,rh.item_code as itemCode,rc.name as itemName,")
                .append(" rh.money as money from RAT_RATION_DETAIL_DISHIS rh, rat_item_custom rc")
                .append(" where rh.ITEM_CODE = rc.id and ");
        hql.append(getSQLIn(disCodes, 900, "rh.dis_Code"));
        hql.append(" order by rh.dis_Code,rh.item_flag,rh.item_Order ");
        return listBySQLAliasToBean(hql.toString(), params, RatBkRationDtlVm.class);
    }
    /**
     * 根据授信编号获取授信录入明细
     */
    public List<RatBkrationDisDtlDTO> getCprationDtlByRatCodes(List<String> ratCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.rat_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.max_money as maxMoney, rd.assurecondition as assurecondition,")
                .append(" rd.assurepercent as assurepercent, rd.interestrate as interestrate, ")
                .append(" rd.comp_sign as compSign,rd.super_Add_Money as superAddMoney, ")
                .append(" rd.super_Add_Open_Money as superAddOpenMoney,rd.super_Add_Max_Money as superAddMaxMoney, ")
                .append(" rd.super_distribute_money as disMoney,rd.super_distribute_open_money as disOpenMoney, ")
                .append(" rd.Total_Money_Add_Money as totalMoneyAddMoney,rd.total_Money_Add_Open_Money as totalMoneyAddOpenMoney ")
                .append(" from rat_bkration_detail rd,rat_item_custom rc ").append(" where rd.ITEM_CODE = rc.id and ");
        hql.append(getSQLIn(ratCodes, 900, "rd.rat_Code"));
        hql.append(" order by rd.rat_Code,rd.item_Order ");
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationDisDtlDTO.class);
    }
    /**
     * 根据授信分配编号和分配次数删除历史明细
     */
    public void deleteDisDtlHisByDisCode(RatBkrationDisListDTO disDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from RAT_RATION_DETAIL_DISHIS where dis_Code = :disCode");
        params.put("disCode", disDTO.getId());
        // params.put("disCount", disDTO.getDisCount());
        createSQLQuery(sql.toString(), params).executeUpdate();
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
        // hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
        // .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,")
        // .append(" re.assurepercent as assurepercent,re.interestrate as interestrate, ")
        // .append(" rd.super_Process_Money as superProcessMoney,")
        // .append(" rd.super_Relief_Money as superReliefMoney,")
        // .append(" rd.super_By_Relief_Money as superByReliefMoney,")
        // .append(" rd.freeze_Money as freeze_Money, ")
        // .append(" (rd.max_money - rd.process_Max_Money - rd.relief_Max_Money + by_Relief_Max_Money -
        // rd.freeze_Max_Money) as maxMoney, ")
        // .append(" re.comp_sign as compSign ")
        // .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_dis rb,rat_bkration_detail re ")
        // .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code and re.rat_code = rb.rat_code ")
        // .append(" and rb.id = rd.dis_code and rb.rat_code = :ratCode ")
        // .append(" and rb.status >= :status ");
        hql.append(" select rd.id,rd.dis_code as ratCode,rd.item_flag  as itemFlag,rd.item_order as itemOrder,")
                .append(" rd.item_code  as itemCode,rc.name as itemName,rd.money  as money,re.comp_sign  as compSign")
                .append(" rd.super_Relief_Money as superReliefMoney,")
                .append(" rd.super_By_Relief_Money as superByReliefMoney,")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc,rat_bkration_dis rb,rat_bkration_detail re")
                .append(" where rd.ITEM_CODE = rc.id and rd.item_code = re.item_code and re.rat_code = rb.rat_code ")
                .append(" and rb.id = rd.dis_code and rb.rat_code = :ratCode")
                // .append(" rh.item_code as itemCode,rc.name as itemName,rh.money as money,re.comp_sign as compSign")
                // .append(" from RAT_RATION_DETAIL_DISHIS rh,rat_item_custom rc,rat_bkration_dis rb,")
                // .append(" rat_bkration_detail re")
                // .append(" where rh.ITEM_CODE = rc.id and rh.item_code = re.item_code and re.rat_code = rb.rat_code ")
                // .append(" and rb.id = rh.dis_code and rb.rat_code = :ratCode ")
                .append(" and rb.status >= :status ");
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            hql.append(" and rd.item_flag = :itemFlag and rd.item_code = :itemCode ");
            params.put("itemCode", itemCode);// 授信品种
        } else {
            hql.append(" and rd.item_flag = :itemFlag ");
        }
        hql.append(" order by rd.dis_Code,rd.item_Order ");
        params.put("ratCode", ratCode);// 授信品种类型
        params.put("itemFlag", itemFlag);// 授信品种类型
        params.put("status", RationConstant.STATUS_WAIT_CHECK);
        return listBySQLAliasToBean(hql.toString(), params, RatBkrationCpAddDtlDTO.class);
    }
    /**
     * 查询审批通过的分配明细
     */
    public List<RatBkrationDisDetail> queryDisDetailByStatus(String disCode, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rh.dis_code,rh.item_code,rh.item_order,rh.item_flag,rh.money")
                .append(" from RAT_RATION_DETAIL_DISHIS rh,rat_bkration_dis rd ")
                .append(" where rd.id=rh.dis_Code and rd.status=:status and rh.dis_code=:disCode ");
        params.put("status", status);
        params.put("disCode", disCode);
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationDisDetail.class);
    }
    /**
     * 查询同一单位审批通过的分配明细
     */
    public List<RatBkrationDisDtlDTO> queryDisDetailsPass(List<String> ratCode, String corpId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select re.dis_code,re.item_code,re.item_flag,re.money ")
                .append(" from rat_bkration_dis rd,rat_bkration_dis_detail re  ")
                .append(" where rd.id=re.dis_code and rd.rat_type=:ratType and rd.corp_id=:corpId")
                .append(" and rd.status=:status and ");
        sql.append(getSQLIn(ratCode, 900, "rd.rat_Code"));
        params.put("corpId", corpId);
        params.put("ratType", RationConstant.RAT_TYPE_1);
        params.put("status", RationConstant.STATUS_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationDisDtlDTO.class);
    }
    /**
     * 根据分配编号和品种编号查询待分配明细
     */
    public RatDisDetailHisVO queryDisDetailApprove(String ratCode, String itemCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rh.item_code as itemCode,").append(" rc.name as itemName, ")
                .append("sum(rh.money) as money ").append("from RAT_RATION_DETAIL_DISHIS rh, rat_item_custom rc ")
                .append("where rh.item_code = rc.id ").append("and rh.dis_Code in ").append("(select dis.id ")
                .append("from rat_bkration_dis dis ").append("where dis.rat_type = '1'")
                .append("and dis.rat_code =:ratCode ").append("and dis.status = '11') ")
                .append(" and rh.item_code=:itemCode ").append("group by rh.item_code, rc.name ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("itemCode", itemCode);
        return getBySQLAliasToBean(sql.toString(), params, RatDisDetailHisVO.class);
    }
}
