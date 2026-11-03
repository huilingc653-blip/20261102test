package com.bytter.credit.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatUrationApply;
import com.bytter.credit.dto.RatSysItemDTO;
import com.bytter.credit.dto.RatUrationApplyListDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatCprationApplyDao
 * @Description:
 * @date 2018年11月21日 上午9:19:33
 */
@Repository
public class RatCprationApplyDao extends BaseDao<RatUrationApply, String> {
    /**
     * 获取企业授信已分配授信品种对应的系统品种
     * 
     * @param disCode
     *            分配id
     * @return
     */
    public List<RatSysItemDTO> getCpRationDisSysItem(String disCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.code as code, a.name as name, a.type as type, ")
                .append(" a.flag as flag from rat_sys_item a where a.type = :type ");
        if (!EmptyUtils.isEmpty(disCode)) {
            sql.append(" and exists ( select 1 from rat_bkration_dis_detail rd,rat_item_custom rc  ");
            sql.append(" where rd.item_code = rc.id and a.code = rc.sys_item_code and rd.dis_code = :disCode ) ");
        }
        sql.append(" order by a.code ");
        params.put("type", RationConstant.RAT_TYPE_1);// 企业授信
        params.put("disCode", disCode);
        return listBySQLAliasToBean(sql.toString(), params, RatSysItemDTO.class);
    }
    /**
     * 查询已审批通过的占用信息
     */
    public List<RatUrationApplyListDTO> queryPassRationApplyData(String disCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru.id as id,ru.type as type,rs.name as typeName,ru.corp_id as corpId, ");
        sql.append(
                " ru.rat_code as ratCode,ru.dis_code as disCode,ru.item_code as singItemCode,rc.name as singItemName, ");
        //TODO 补充币种名称
        sql.append(" ru.item_flag as itemFlag,ru.cur_code as curCode,ru.cur_name as curName,ru.money as money,");
        sql.append(" ru.begin_date as beginDate,ru.end_date as endDate,ru.status as status,");
        sql.append(" ru.interestrate as interestrate, ru.item_code as itemCode,");
        sql.append(" ru.contact_No as contactNo,ru.remark as remark ");
        sql.append(" from rat_uration_apply ru left join rat_item_custom rc on ru.item_code = rc.id ");
        sql.append(" inner join rat_sys_item rs on ru.type = rs.code ");
        sql.append(" where ru.dis_Code = :disCode and ru.status=95");
        params.put("disCode", disCode);
        return listBySQLAliasToBean(sql.toString(), params, RatUrationApplyListDTO.class);
    }
    /**
     * 通过id获取授信占用信息
     */
    public RatUrationApplyListDTO queryRatApplyrationById(String applyId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru.id as id,ru.type as type,rs.name as typeName,ru.corp_id as corpId, ");
        sql.append(
                " ru.rat_code as ratCode,ru.dis_code as disCode,ru.item_code as singItemCode,rc.name as singItemName, ");
        //TODO 补充币种名称
        sql.append(" ru.item_flag as itemFlag,ru.cur_code as curCode,ru.cur_name as curName,ru.money as money,");
        sql.append(" ru.begin_date as beginDate,ru.end_date as endDate,ru.status as status,");
        sql.append(" ru.interestrate as interestrate,ru.assurepercent as assurepercent, ");
        sql.append(" ru.contact_No as contactNo,ru.bill_No as billNo,ru.lc_no as lcNo,ru.remark as remark ");
        sql.append(" from rat_uration_apply ru left join rat_item_custom rc on ru.item_code = rc.id ");
        sql.append(" inner join rat_sys_item rs on ru.type = rs.code ");
        sql.append(" where ru.id = :applyId ");
        params.put("applyId", applyId);
        return getBySQLAliasToBean(sql.toString(), params, RatUrationApplyListDTO.class);
    }
    /**
     * 根据分配编号查询授信占用列表
     */
    public List<RatUrationApplyListDTO> queryRationApplyPass(String disCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rs.name  as typeName,ru.item_code as singItemCode,rc.name as singItemName,")
                .append(" sum(ru.money) as money,ru.item_code as itemCode from rat_uration ru")
                .append(" left join rat_item_custom rc on ru.item_code = rc.id inner join rat_sys_item rs")
                .append(" on ru.type = rs.code").append(" where ru.dis_Code = :disCode and ru.status in(1,2) ")
                .append(" group by rs.name,ru.item_code,rc.name,ru.item_code");
        params.put("disCode", disCode);
        return listBySQLAliasToBean(sql.toString(), params, RatUrationApplyListDTO.class);
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
        sql.append("select count(*) from rat_uration_apply where corp_id=:corpId and rat_type=1 ");
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
