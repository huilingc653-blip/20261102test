package com.bytter.credit.domain.dao;
import com.bytter.credit.domain.entity.RatBkrationAddDetail;
import com.bytter.credit.domain.vo.RatUrationCurVO;
import com.bytter.credit.domain.vo.RatUrationQueryVO;
import com.bytter.credit.domain.vo.RatUrationUseBeanVO;
import com.bytter.credit.domain.vo.RationDisBean;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationAddVm;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 授信追加明细表
 * @author onlineGenerator
 * @date 2017-07-31 16:00:34
 * @version 1.0
 *
 */
@Repository
public class RatBkrationAddDetailDao extends BaseDao<RatBkrationAddDetail, String> {
    /**
     * 功能介绍 : 获取追加表中未审批通过的追加额度。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getAddUnCheckMoney(String ratCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select rd.item_code as itemCode, sum(rd.money) as money, sum(rd.open_money) as openMoney, sum(rd.max_money) as maxMoney ");
        sql.append(" from rat_bkration_add rb, rat_bkration_add_detail rd ");
        sql.append(" where rb.id = rd.add_code and rb.status >= :status0 and rb.status < :status1 ");
        sql.append(" and rb.flag = '1' and rb.rat_code = :ratCode group by rd.item_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("status0", CommonConstant.STATUE_BACK);
        params.put("status1", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 获取追加表中未审批通过的追加额度。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getEditAddUnCheckMoney(String ratCode, String addId) {
        if (StringUtils.isEmpty(addId)) {
            return new ArrayList<RationDisBean>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select rd.item_code as itemCode, sum(rd.money) as money, sum(rd.open_money) as openMoney, sum(rd.max_money) as maxMoney ");
        sql.append(" from rat_bkration_add rb, rat_bkration_add_detail rd ");
        sql.append(" where rb.id = rd.add_code and rb.status >= :status0 and rb.status < :status1 and rd.money < 0 ");
        sql.append(" and rb.flag = '1' and rb.rat_code = :ratCode and rb.id = :addId group by rd.item_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratCode", ratCode);
        params.put("addId", addId);
        params.put("status0", CommonConstant.STATUE_BACK);
        params.put("status1", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RationDisBean.class);
    }
    /**
     * 功能介绍 : 根据授信追加编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年7月28日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationAddDtlVm> getBkrationDtlByRatCodes(List<String> addCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.add_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.max_money as maxMoney, ")
                .append(" case when rd.credit_type is null then '1' else rd.credit_type end as creditType ")
                .append(" from rat_bkration_add_detail rd,rat_item_custom rc ")
                .append(" where rd.ITEM_CODE = rc.id and ");
        hql.append(getSQLIn(addCodes, 900, "rd.add_Code"));
        hql.append(" order by rd.add_Code,rd.item_Order ");
        return listBySQLAliasToBean(hql.toString(), params, RatBkRationAddDtlVm.class);
    }
    /**
     * 功能介绍 : 删除原授信追加明细。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void deleteBkrationDisDtlByDisCode(RatBkRationAddVm disDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from rat_bkration_add_detail where add_Code = :ratCode ");
        params.put("ratCode", disDTO.getId());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 查询集团授信追加的授信品种明细。<br>
     * <b>日期：</b>2017年7月28日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationAddDtlVm> getBkrationAddDtlByRatCode(String ratCode, String corpId, String itemFlag,
                                                                String itemCode, String flag) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rd.id,rd.add_code as ratCode,rd.item_flag as itemFlag,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,rd.open_money as openMoney, ")
                .append(" rd.max_money as maxMoney, rb.flag as flag ")
                .append(" from rat_bkration_add_detail rd,rat_item_custom rc, rat_bkration_add rb ")
                .append(" where rd.add_code = rb.id and rd.ITEM_CODE = rc.id and rb.status = :status ")
                .append(" and rb.rat_code = :ratCode  ");
        if (!EmptyUtils.isEmpty(corpId)) {
            hql.append(" and rb.corp_id = :corpId ");
            params.put("corpId", corpId);// 追加单位
        }
        if (!EmptyUtils.isEmpty(flag)) {
            hql.append(" and rb.flag = :flag ");
            params.put("flag", flag);// 追加方式
        }
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            hql.append(" and rd.item_flag = :itemFlag and rd.item_code = :itemCode ");
            params.put("itemCode", itemCode);// 授信品种
        } else {
            hql.append(" and rd.item_flag = :itemFlag ");
        }
        hql.append(" order by rd.add_Code,rd.item_Order ");
        params.put("itemFlag", itemFlag);// 授信品种类型
        params.put("ratCode", ratCode);// 授信品种
        params.put("status", RationConstant.STATUS_WAIT_CHECK);// 追加
        // params.put("flag", RationConstant.RAT_ADD_FLAG_GROUP);//集团追加
        return listBySQLAliasToBean(hql.toString(), params, RatBkRationAddDtlVm.class);
    }
    /**
     * 功能介绍 : 查询集团授信追减在途额度明细。<br>
     * <b>日期：</b>2017年9月30日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationUseBeanVO> queryRationSubAddDtl(RatUrationQueryVO queryVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru.id as id, rd.item_flag as itemFlag, ru.corp_id as corpId,rb.corp_name as corpName, ");
        sql.append(" '07' as businessName, rb.cur_code as curCode,rb.cur_name as curName,rb.cur_code as initCurCode, ");
        sql.append(" rb.cur_name as initCurName, rd.item_code as itemCode,rm.name as itemName, ");
        if (RationConstant.RAT_COMFLAG_1.equals(queryVO.getItemFlag()) && !EmptyUtils.isEmpty(queryVO.getItemCode())) {// 查询综合品种明细
            sql.append("  (-1) * rd.max_money as money, (-1) * rd.max_money as convertPreMoney, ");
        } else {
            sql.append("  (-1) * rd.money as money, (-1) * rd.money as convertPreMoney, ");
        }
        sql.append(
                " ru.create_date as beginDate, rb.enddate as endDate, ru.id as billId, ");
        sql.append(" ru.add_code as billNo, ru.status as status ");
        sql.append(" from RAT_BKRATION_ADD ru ");
        sql.append(" left join RAT_BKRATION_ADD_detail rd on rd.ADD_CODE = ru.id ");
        sql.append(" left join rat_bkration rb on rb.id = ru.rat_code ");
        sql.append(" left join RAT_BKRATION_detail rbd on rbd.rat_code = rb.id and rbd.ITEM_CODE = rd.ITEM_CODE ");
//        sql.append(" left join sys_corp sc on ru.corp_id = sc.id ");
//        sql.append(" left join bt_currency bc on rb.cur_code = bc.id ");
        sql.append(" left join rat_item_custom rm on rd.ITEM_CODE = rm.id ");
        sql.append(" where rb.rat_type = '0'  and ru.FLAG = :flag ");
        sql.append(" and ru.corp_id = :corpId and ru.rat_code = :ratCode and ru.status = :status ");
        if (EmptyUtils.isNotEmpty(queryVO.getCurCode())) {
            sql.append(" and rb.cur_code = :curCode ");
            params.put("curCode", queryVO.getCurCode());
        }
        if (EmptyUtils.isNotEmpty(queryVO.getItemFlag())) {
            sql.append(" and rd.item_flag = :itemFlag ");
            params.put("itemFlag", queryVO.getItemFlag());
            if (RationConstant.RAT_COMFLAG_0.equals(queryVO.getItemFlag())) {
                if (EmptyUtils.isNotEmpty(queryVO.getItemCode())) {
                    sql.append(" and rd.item_code = :itemCode and rd.money < 0 ");
                    params.put("itemCode", queryVO.getItemCode());
                }
            } else {
                if (EmptyUtils.isNotEmpty(queryVO.getItemCode())) {
                    sql.append(" and rd.item_code = :itemCode and rd.max_money < 0 ");
                    params.put("itemCode", queryVO.getItemCode());
                } else {
                    sql.append(" and rbd.comp_sign = :compSign and rd.money < 0 ");
                    params.put("compSign", RationConstant.RAT_COMFLAG_1);
                }
            }
        }
        sql.append(" order by ru.create_date desc ");
        params.put("corpId", queryVO.getCorpId());
        params.put("flag", RationConstant.RAT_ADD_FLAG_GROUP);// 集团授信
        params.put("status", RationConstant.STATUS_WAIT_CHECK);
        params.put("ratCode", queryVO.getRatCode());
        return listBySQLAliasToBean(sql.toString(), params, RatUrationUseBeanVO.class);
    }
    /**
     * 功能介绍 : 查询集团授信追减在途额度明细 <br>
     * <b>日期：</b> 2020/11/18 0018 <br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationCurVO> queryRationSubAddFreeze(List<String> ratCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru.rat_code as ratCode, rbk.cur_code as curCode, ");
        sql.append(" rd.id as disCode, ");
        sql.append(" ru.corp_id as corpId, ");
        sql.append(" rb.item_code as itemCode, ");
        sql.append(" rb.item_flag as itemFlag, ");
        sql.append(" (-1) * rb.money as billMoney ");
        sql.append(" from rat_bkration_add ru ");
        sql.append(" inner join rat_bkration_add_detail rb on rb.add_code = ru.id ");
        sql.append(" inner join rat_bkration_dis rd on ru.rat_code = rd.rat_code and ru.corp_id = rd.corp_id ");
        sql.append(" inner join rat_bkration rbk on ru.rat_code = rbk.id  ");
        sql.append(" where rb.money < 0 and rb.item_flag = '0' ");
        sql.append(" and ru.flag = :flag and ru.status = :status ");
        sql.append(" and ").append(getSQLIn(ratCodes, 900, "ru.rat_code"));
        sql.append(" union all ");
        sql.append(" select ru.rat_code as ratCode, rbk.cur_code as curCode, ");
        sql.append(" rd.id as disCode, ");
        sql.append(" ru.corp_id as corpId, ");
        sql.append(" rb.item_code as itemCode, ");
        sql.append(" rb.item_flag as itemFlag, ");
        sql.append(" (-1) * rb.max_money as billMoney ");
        sql.append(" from rat_bkration_add ru ");
        sql.append(" inner join rat_bkration_add_detail rb on rb.add_code = ru.id ");
        sql.append(" inner join rat_bkration_dis rd on ru.rat_code = rd.rat_code and ru.corp_id = rd.corp_id ");
        sql.append(" inner join rat_bkration rbk on ru.rat_code = rbk.id  ");
        sql.append(" where rb.max_money < 0 and rb.item_flag = '1' ");
        sql.append(" and ru.flag = :flag and ru.status = :status ");
        sql.append(" and ").append(getSQLIn(ratCodes, 900, "ru.rat_code"));
        params.put("flag", RationConstant.RAT_ADD_FLAG_GROUP);// 集团授信
        params.put("status", RationConstant.STATUS_WAIT_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RatUrationCurVO.class);
    }
}
