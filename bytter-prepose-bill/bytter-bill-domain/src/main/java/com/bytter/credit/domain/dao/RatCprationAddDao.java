package com.bytter.credit.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.domain.entity.RatBkrationAdd;
import com.bytter.credit.domain.vo.RatBkrationAddListVO;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:RatCprationAddDao
 * @Description:
 * @date 2018年11月15日 下午4:54:29
 */
@Repository
public class RatCprationAddDao extends BaseDao<RatBkrationAdd, String> {
    /**
     * 追加明细
     *
     * @param
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryRationAddData(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rd.id as id,rd.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rd.add_code as addCode,rd.flag as flag, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rd.money as money, ")
                .append(" rd.create_date as createDate ")
                .append(" from rat_bkration_add rd, RAT_BKRATION rb ")
                .append(" where rd.rat_code = rb.id and rb.status = 95 and rd.status <> -2 and rb.rat_type='1' ");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationAddListVO.class, null, false);
    }
    /**
     * 追加审批列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryRatCpAddApproveData(DataTablesInput input, String businessSql) {
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append("select rd.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rd.add_code as addCode,rd.flag as flag, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rd.status as status,rd.money as money, ")
                .append(" rd.create_date as createDate ")
//                .append(" from rat_bkration_add rd, RAT_BKRATION rb, sys_corp sc,bt_currency bc")
//                .append(" where rd.rat_code = rb.id and rd.corp_id = sc.id")
//                .append(" and rb.cur_code = bc.id and rb.status = 95 and rd.status <> -2 ");
                .append(" from rat_bkration_add rd, RAT_BKRATION rb ")
                .append(" where rd.rat_code = rb.id ")
                .append(" and rb.status = 95 and rd.status <> -2 ");
        if (EmptyUtils.isNotEmpty(businessSql)) {
            sql.append(" and " + businessSql);
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationAddListVO.class, null, false);
    }
    /**
     * 更新授信录入表中的中追加额度
     */
    public void updateRationDtlAddMoney(RatBkRationDtlVm dto) {
        StringBuilder sql = new StringBuilder();
        sql.append("  update rat_bkration_detail ");
        sql.append("  set SUPER_ADD_MONEY = :superAddMoney ");
        sql.append(" where id = :dtlId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dtlId", dto.getId());
        params.put("superAddMoney", dto.getSuperAddMoney());
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
        sql.append("select count(*) from rat_bkration_add where corp_id=:corpId and rat_type=1 ");
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
