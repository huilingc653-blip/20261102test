package com.bytter.credit.domain.dao;
import com.bytter.credit.domain.entity.RatBkrationDetail;
import com.bytter.credit.domain.vo.RatItemDisVO;
import com.bytter.credit.domain.vo.RationDisBean;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 授信录入明细表
 * @author onlineGenerator
 * @date 2017-07-21 14:35:53
 * @version 1.0
 *
 */
@Repository
public class RatBkrationDetailDao extends BaseDao<RatBkrationDetail, String> {
    /**
     * 功能介绍 : 获取单位已录入的授信明细。<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationDtlVm> getCorpBkrationDtlDTOs(RatBkRationVm bkrationDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd from RatBkrationDetail rd where exists ");
        hql.append(" (select 1 from RatBkration rb where rb.id = rd.ratCode and rb.status <> :status  ");
        hql.append(" and rb.corpId = :corpId and rb.bankCode = :bankCode and rb.curCode = :curCode ) ");
        // 过滤当前校验数据
        if (!StringUtils.isEmpty(bkrationDTO.getId())) {
            hql.append(" and rd.ratCode <> :ratCode ");
            params.put("ratCode", bkrationDTO.getId());
        }
        hql.append(" order by rd.ratCode,rd.itemOrder ");
        params.put("corpId", bkrationDTO.getCorpId());
        params.put("bankCode", bkrationDTO.getBankCode());
        params.put("curCode", bkrationDTO.getCurCode());
        params.put("status", CommonConstant.STATUE_DELETE);
        List<RatBkrationDetail> list = listByHQL(hql.toString(), params);
        return copyListAndInject(list, RatBkRationDtlVm.class);
    }
    /**
     * 功能介绍 : 删除原授信明细。<br>
     * <b>日期：</b>2017年7月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteBkrationDtlByRatCode(RatBkRationVm bkrationDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from RAT_BKRATION_DETAIL where rat_Code = :ratCode ");
        params.put("ratCode", bkrationDTO.getId());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 根据授信编号获取单位已录入的授信明细。<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationDtlVm> getBkrationDtlDTOByRatCode(String ratCode, String itemFlag) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd from RatBkrationDetail rd where 1 = 1 ");
        // 授信编号
        if (!StringUtils.isEmpty(ratCode)) {
            hql.append(" and rd.ratCode = :ratCode ");
            params.put("ratCode", ratCode);
        }
        // 授信品种类型
        if (!StringUtils.isEmpty(itemFlag)) {
            hql.append(" and rd.itemFlag = :itemFlag ");
            params.put("itemFlag", itemFlag);
        }
        hql.append(" order by rd.ratCode,rd.itemOrder ");
        List<RatBkrationDetail> list = listByHQL(hql.toString(), params);
        return copyListAndInject(list, RatBkRationDtlVm.class);
    }
    /**
     * 功能介绍 : 根据授信编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationDtlVm> getBkrationDtlByRatCodes(List<String> ratCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.rat_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ");
        hql.append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ");
        hql.append(" rd.max_money as maxMoney, rd.assurecondition as assurecondition,");
        hql.append(" rd.assurepercent as assurepercent, rd.interestrate as interestrate, ");
        hql.append(" rd.comp_sign as compSign,rd.super_Add_Money as superAddMoney, ");
        hql.append(" rd.super_Add_Open_Money as superAddOpenMoney,rd.super_Add_Max_Money as superAddMaxMoney, ");
        hql.append(" rd.super_distribute_money as disMoney,rd.super_distribute_open_money as disOpenMoney, ");
        hql.append(" rd.Total_Money_Add_Money as totalMoneyAddMoney,rd.total_Money_Add_Open_Money as totalMoneyAddOpenMoney, ");
        hql.append(" case when rd.credit_type is null then '1' else rd.credit_type end as creditType ");
        hql.append(" from rat_bkration_detail rd,rat_item_custom rc ");
        hql.append(" where rd.ITEM_CODE = rc.id and ");
        hql.append(getSQLIn(ratCodes, 900, "rd.rat_Code"));
        hql.append(" order by rd.rat_Code,rd.item_Order ");
        return listBySQLAliasToBean(hql.toString(), params, RatBkRationDtlVm.class);
    }
    /**
     * 功能介绍 : 获取所有可分配的金额之和（录入+追加）。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getRationDisMoney(String ratCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd.item_code as itemCode, sum(rd.money + rd.super_add_money) as money, ");
        sql.append(" sum(rd.open_money + rd.super_add_open_money) as open_money, ");
        sql.append(" sum(rd.max_money + rd.super_add_max_money) as max_money ");
        sql.append("  from rat_bkration_detail rd  ");
        sql.append(" where rd.rat_code = :ratCode group by rd.item_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 获取自用模式下所有可分配的金额之和（录入+追加-分配）。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getOwnRationDisMoney(String ratCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select rd.item_code as itemCode, sum(rd.money + rd.super_add_money - rd.super_distribute_money) as money, ");
        sql.append(" sum(rd.open_money + rd.super_add_open_money - rd.super_distribute_open_money) as open_money, ");
        sql.append(" sum(rd.max_money + rd.super_add_max_money) as max_money ");
        sql.append("  from rat_bkration_detail rd  ");
        sql.append(" where rd.rat_code = :ratCode group by rd.item_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 获取所有可分配的金额（录入+追加）。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getRationDisMoneyList(String ratCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd.item_code as itemCode, sum(rd.money + rd.super_add_money) as money, ");
        sql.append(" sum(rd.open_money + rd.super_add_open_money) as open_money ");
        sql.append("  from rat_bkration_detail rd  ");
        sql.append(" where rd.rat_code = :ratCode group by rd.item_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 更新原授信中追加额度。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDtlAddMoney(RatBkRationDtlVm dto) {
        StringBuilder sql = new StringBuilder();
        sql.append("  update rat_bkration_detail ");
        sql.append("  set SUPER_ADD_MONEY = :superAddMoney, super_add_open_money = :superAddOpenMoney,  ");
        sql.append("  super_add_max_money = :superAddMaxMoney,  ");
        sql.append(
                "  total_money_add_money = :totalMoneyAddMoney, total_money_add_open_money = :totalMoneyAddOpenMoney,  ");
        sql.append(
                "  super_distribute_money = :superDistributeMoney, super_distribute_open_money = :superDistributeOpenMoney , ");
        sql.append(" CREDIT_TYPE=:creditType ");
        sql.append(" where id = :dtlId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dtlId", dto.getId());
        params.put("superAddMoney", dto.getSuperAddMoney());
        params.put("superAddOpenMoney", dto.getSuperAddOpenMoney());
        params.put("superAddMaxMoney", dto.getSuperAddMaxMoney());
        params.put("totalMoneyAddMoney", dto.getTotalMoneyAddMoney());
        params.put("totalMoneyAddOpenMoney", dto.getTotalMoneyAddOpenMoney());
        params.put("superDistributeMoney", dto.getSuperDistributeMoney());
        params.put("superDistributeOpenMoney", dto.getSuperDistributeOpenMoney());
        params.put("creditType", dto.getCreditType());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 查询授信品种的保证金比例或费率。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatItemDisVO> getItemAssure(String ratCode, String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append("select rd.id as id, rd.ratCode as ratCode, rd.itemFlag as itemFlag, rd.itemOrder as itemOrder, " );
        hql.append(" rd.money as money, rd.openMoney as openMoney, rd.superDistributeMoney as superDistributeMoney,  ");
        hql.append(" rd.superDistributeOpenMoney as superDistributeOpenMoney, rd.superAddMoney as superAddMoney, ");
        hql.append(" rd.superAddOpenMoney as superAddOpenMoney, rd.assurecondition as assurecondition, rd.assurepercent as assurepercent, ");
        hql.append(" rd.interestrate as interestrate, rd.totalMoneyAddMoney as totalMoneyAddMoney, rd.totalMoneyAddOpenMoney as totalMoneyAddOpenMoney, ");
        hql.append(" rd.maxMoney as maxMoney, rd.compSign as compSign, rd.superAddMaxMoney as superAddMaxMoney, rd.bondMoney as bondMoney, ");
        hql.append(" rd.procedureFeeRate as procedureFeeRate, rd.procedureFee as procedureFee, rd.creditType as creditType ");
        hql.append(" from RatBkrationDetail rd ");
        //TODO 暂时补充名称相关信息
        hql.append(" left join RatBkration info on info.id = rd.ratCode ");
        hql.append(" where 1 =1 ");
        if (StringUtils.isNotEmpty(ratCode)) {
            hql.append(" and rd.ratCode = :ratCode ");
        }
        if (StringUtils.isNotEmpty(itemCode)) {
            hql.append("  and rd.itemCode = :itemCode ");
        }
        params.put("ratCode", ratCode);
        params.put("itemCode", itemCode);
        List<RatBkrationDetail> list = listByHQLAliasToBean(hql.toString(), params,RatBkrationDetail.class);
        return copyListAndInject(list, RatItemDisVO.class);
    }
    /**
     * 功能介绍 : 更新原授信中调剂额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDtlReliefMoney(RatBkRationDtlVm dto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update rat_bkration_detail rd  ");
        sql.append(" set money = :money, open_money = :openMoney,  ");
        sql.append(" super_distribute_money = :superDistributeMoney,");
        sql.append(" super_distribute_open_money = :superDistributeOpenMoney  ");
        sql.append(" where rd.id = :dtlId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dtlId", dto.getId());
        params.put("money", dto.getMoney());
        params.put("openMoney", dto.getOpenMoney());
        params.put("superDistributeMoney", dto.getSuperDistributeMoney());
        params.put("superDistributeOpenMoney", dto.getSuperDistributeOpenMoney());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 更新原授信中最高额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDtlMaxMoney(RatBkRationDtlVm dto) {
        StringBuilder sql = new StringBuilder();
        sql.append("  update rat_bkration_detail ");
        sql.append("  set max_money = :maxMoney ");
        sql.append(" where id = :dtlId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dtlId", dto.getId());
        params.put("maxMoney", dto.getMaxMoney());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 根据授信编号获取单位已录入的授信明细。<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDetail> getBkrationDtlByRatCode(String ratCode, String itemFlag, String itemCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd from RatBkrationDetail rd where 1 = 1 ");
        // 授信编号
        if (!StringUtils.isEmpty(ratCode)) {
            hql.append(" and rd.ratCode = :ratCode ");
            params.put("ratCode", ratCode);
        }
        // 授信品种类型
        if (!StringUtils.isEmpty(itemFlag)) {
            if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                hql.append(" and rd.itemFlag = :itemFlag and rd.itemCode = :itemCode ");
                params.put("itemCode", itemCode);// 授信品种
            } else {
                hql.append(" and rd.itemFlag = :itemFlag ");
            }
            params.put("itemFlag", itemFlag);
        }
        hql.append(" order by rd.ratCode,rd.itemOrder ");
        return listByHQL(hql.toString(), params);
    }
    /**
     * 授信明细
     *
     * @param ratCode
     * @param iteamCode
     * @return
     */
    public List<RatBkRationDtlVm> getBkrationDtlByRatAndIteamCode(String ratCode, String iteamCode) {
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
        hql.append("rd.rat_Code=:ratCode and rd.item_code=:iteamCode ");
        hql.append(" order by rd.rat_Code,rd.item_Order ");
        params.put("ratCode", ratCode);
        params.put("iteamCode", iteamCode);
        return listBySQLAliasToBean(hql.toString(), params, RatBkRationDtlVm.class);
    }
}
