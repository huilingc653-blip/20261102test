package com.bytter.credit.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationDis;
import com.bytter.credit.domain.vo.RatBkrationDisVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @version 1.0
 * @Title:RatCprationDisDao
 * @Description:
 * @date 2018年11月19日 下午13:41:25
 */
@Repository
public class RatCprationDisDao extends BaseDao<RatBkrationDis, String> {
    /**
     * 查询待分配数据列表
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listDisOutputData(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rb.rat_mode as ratMode, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ")
                .append(" from RAT_BKRATION rb ").append(" where rb.status <> -2 and rb.rat_type='1'");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false);
    }
    /**
     * 查询符合条件的授信分配数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryRationDisData(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select rd.id as id,rb.rat_code as ratCode,rb.corp_id as ratCorpId,rb.corp_name as ratCorpName, ")
                .append(" rb.contract_no as contractNo,rd.rat_Type as ratType,")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rd.dis_Code as disCode, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rb.money as money, ")
                .append(" rd.corp_id as corpId,rd.distribute_Way as distributeWay, rd.corp_name as corpName ")
                .append(" from rat_bkration_dis rd, RAT_BKRATION rb ")
                .append(" where rd.rat_code = rb.id and rb.status <> -2 and rd.status <> -2 ")
                .append(" and rd.distribute_way !=:distributeWay and rb.rat_type='1' ");
        params.put("distributeWay", RationConstant.DIS_WAY_AUTO);// 过滤自动分配的那笔数据
        return getDynamicBySQL(input, sql.toString(), RatBkrationDisVO.class, null, false,
                DynamicallyObject.getInstance(params));
    }
    /**
     * 查询内部授信分配待审批数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryRationDisApproveData(DataTablesInput input, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rd.id as id,rb.rat_code as ratCode,rb.corp_id as ratCorpId,rb.corp_name as ratCorpName, ")
                .append(" rb.contract_no as contractNo,")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rd.dis_Code as disCode, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rd.money as money, ")
                .append(" rd.corp_id as corpId,rd.distribute_Way as distributeWay, rd.corp_name as corpName, ")
                .append(" rd.create_date as createDate ")
//                .append(" from rat_bkration_dis rd, RAT_BKRATION rb,sys_corp sc,bt_currency bc,sys_corp sp ")
//                .append(" where rd.rat_code = rb.id and rb.corp_id = sc.id")
//                .append(" and rd.corp_id = sp.id and rb.cur_code = bc.id and rb.status <> -2 and rd.status <> -2 ");
        //TODO 名称信息调整
                .append(" from rat_bkration_dis rd, RAT_BKRATION rb ")
                .append(" where rd.rat_code = rb.id ")
                .append(" and rb.status <> -2 and rd.status <> -2 ");
        if (EmptyUtils.isNotEmpty(businessSql)) {
            sql.append(" and " + businessSql);
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationDisVO.class, null, false);
    }
    /**
     * 根据条件查询是否存在分配数据
     */
    @SuppressWarnings("unchecked")
    public List<RatBkrationDisListDTO> getRatCpRationDisList(RatBkrationDisListDTO disDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append("select rd from RatBkrationDis rd")
                .append(" where rd.ratType=:ratType and rd.corpId=:corpId and rd.ratCode=:ratCode")
                .append(" and rd.status=:status");
        params.put("corpId", disDTO.getCorpId());
        params.put("ratCode", disDTO.getRatCode());
        params.put("ratType", RationConstant.RAT_TYPE_1);
        params.put("status", RationConstant.STATUS_WAIT_CHECK);
        List<RatBkrationDis> list = listByHQL(hql.toString(), params);
        return copyListAndInject(list, RatBkrationDisListDTO.class);
    }
    /**
     * 查询审批通过的内部授信分配数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryCpRationDisData(DataTablesInput input, String corpId, String... corpAuthSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.cur_code as curCode,rb.cur_name as curName,rb.rat_mode as ratMode, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money, ")
                .append(" rd.corp_id as disCorp,rd.corp_name as disCorpName,rd.id as disCode,rd.rat_type as ratType ")
                .append(" from RAT_BKRATION rb,rat_bkration_dis rd ")
                .append(" where rd.rat_code = rb.id ")
                .append(" and rb.status = 95 and rd.status = 95 and rb.rat_type='1' ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false,
                DynamicallyObject.getInstance(params));
    }
    /**
     * 根据id查询内部授信分配数据（审批通过）
     */
    public RatBkrationDisVO queryCpRationDisDataById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id, rd.id as disCode,rb.rat_code as ratCode,rb.id as ratId,rd.corp_id as corpId,rd.corp_name as corpName,")
                .append(" rd.rat_type as ratType,rb.cur_code as curCode,rb.cur_name as curName,rd.distribute_way as distributeWay, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rd.money as money,")
                .append(" rb.corp_id as ratCorpId,rb.corp_name as ratCorpName,rb.contract_no as contractNo")
                .append(" from rat_bkration_dis rd inner join RAT_BKRATION rb")
                .append(" on rd.rat_code = rb.id ")
                .append(" where rb.status = :status and rd.status = :status and rd.id = :id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("status", CommonConstant.STATUE_CHECK);// 审批通过
        return getBySQLAliasToBean(sql.toString(), params, RatBkrationDisVO.class);
    }
    /**
     * 根据授信分配id查询授信分配明细
     */
    public List<RatBkrationDisDtlDTO> queryDisDetailByDisCode(String disCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd.id as id,rd.dis_code as disCode,rd.item_order as itemOrder, ")
                .append(" rd.item_code as itemCode,rc.name as itemName,rd.money as money,")
                .append(" rd.item_flag as itemFlag,rd.super_Process_Money as superProcessMoney")
                .append(" from rat_bkration_dis_detail rd,rat_item_custom rc, rat_bkration_dis rb ")
                .append(" where rd.ITEM_CODE = rc.id ").append(" and rb.id = rd.dis_code and rb.rat_type = :ratType ")
                .append(" and rd.dis_code=:disCode");
        sql.append(" order by rd.dis_Code,rd.item_Order ");
        params.put("disCode", disCode);
        params.put("ratType", RationConstant.RAT_TYPE_1);
        List<RatBkrationDisDtlDTO> list = new ArrayList<RatBkrationDisDtlDTO>();
        list = listBySQLAliasToBean(sql.toString(), params, RatBkrationDisDtlDTO.class);
        return list;
    }
    /**
     * @param
     * @return
     */
    public List<RatBkrationDis> getRatCpDisByRatCode(String ratCode, String corpId, String disWay) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rd from RatBkrationDis rd where rd.ratCode = :ratCode  ");
        sql.append(" and rd.status = :status ");
        if (!StringUtils.isEmpty(corpId)) {
            sql.append(" and rd.corpId = :corpId ");
            params.put("corpId", corpId);
        }
        if (!StringUtils.isEmpty(disWay)) {
            sql.append(" and rd.distributeWay = :disWay ");
            params.put("disWay", disWay);
        }
        params.put("ratCode", ratCode);
        params.put("status", CommonConstant.STATUE_CHECK);
        return listByHQL(sql.toString(), params);
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
        sql.append("select count(*) from RAT_BKRATION_DIS where corp_id=:corpId and rat_type=1 and DISTRIBUTE_WAY<>5 ");
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
