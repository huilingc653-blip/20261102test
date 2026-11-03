package com.bytter.biz.finacial.tally.dao;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.finacial.api.dto.FcBankAccDtlDTO;
import com.bytter.biz.finacial.tally.entity.FcBankAccDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author
 * @version 1.0
 * @Title: Dao
 * @Description: Bis_Acc_Dtl
 * @date 2019-5-14
 */
@Repository
public class FcBankAccDtlDao extends BaseDao<FcBankAccDtl, String> {
    /**
     * 查询页面数据-记账
     * menuType  up or down
     */
    public DataTablesOutput<BisAccDtlDTO> queryBisAccDtl(DataTablesInput dataTablesInput, String menuType, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.*,c.id as poolId,c.pool_name as corpName,c.corp_id as corpId,cur.cur_name as curName ,'' as accDtlId,'' as tallySign ");
        sql.append("from bis_acc_dtl a,bt_bank_acc b,fc_corp_pool c,fc_corp_pool d, bt_bank_acc e, bt_bank_acc_cur f, ");
        sql.append(" bt_currency cur ");
        sql.append(" where a.bank_acc = b.bank_acc and b.corp_id = c.corp_id and c.pool_sign = '1' and c.status = '1' ");
        sql.append(" and a.opp_acc_no = e.bank_acc and e.corp_id = d.corp_id and d.pool_sign = '0' and d.status = '1' ");
        sql.append(" and e.id = f.bank_acc_id and f.prent_acc_id = b.id  and a.cur_id = cur.id ");
        sql.append(" and c.id = d.pool_id ");
        sql.append(" and not exists (select 1 from Fc_bank_acc_dtl fbc where fbc.dtl_id = a.id )");
        if ("up".equals(menuType)) {
            sql.append(" and a.cd_sign = '0' ");
        } else {
            sql.append(" and a.cd_sign = '1' ");
        }
        Map<String, Object> search = dataTablesInput.getFormdata();
        if (search.get("ged_transTime") != null && search.get("ged_transTime") != "") {
            sql.append(" and a.trans_time >= to_date('" + search.get("ged_transTime") + " 00:00:00','yyyy-MM-dd  HH24:mi:ss') ");
        }
        if (search.get("led_transTime") != null && search.get("led_transTime") != "") {
            sql.append(" and a.trans_time <= to_date('" + search.get("led_transTime") + " 23:59:59','yyyy-MM-dd  HH24:mi:ss') ");
        }
        if (search.get("eqs_poolId") != null && search.get("eqs_poolId") != "") {
            sql.append(" and c.id = '" + search.get("eqs_poolId") + "'");
        }
        if (search.get("likes_bankAcc") != null && search.get("likes_bankAcc") != "") {
            sql.append(" and b.bank_acc like '%" + search.get("likes_bankAcc") + "%'");
        }
        if (search.get("eqs_curId") != null && search.get("eqs_curId") != "") {
            sql.append(" and a.cur_id = '" + search.get("eqs_curId") + "'");
        }
        if (search.get("ins_remark") != null && search.get("ins_remark") != "") {
            String str = search.get("ins_remark").toString();
            String[] rmk = str.split(",");
            List<String> rmkList = Arrays.asList(rmk);
            sql.append(" and ").append(getSQLIn(rmkList, 900, "a.remark"));
        }
        if (search.get("geg_amt") != null && search.get("geg_amt") != "") {
            sql.append(" and a.amt >= " + search.get("geg_amt"));
        }
        if (search.get("leg_amt") != null && search.get("leg_amt") != "") {
            sql.append(" and a.amt <= " + search.get("leg_amt"));
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "b.corp_id"));
        sql.append(" union ");
        sql.append(" select a.*,c.id as poolId,c.pool_name,c.corp_id as corpId,cur.cur_name,accdtl.id as accDtlId,accdtl.tally_status as tallySign ");
        sql.append(" from bis_acc_dtl a,bt_bank_acc b,fc_corp_pool c,fc_corp_pool d,bt_bank_acc e, bt_bank_acc_cur f,");
        sql.append(" bt_currency cur,fc_bank_acc_dtl accdtl ");
        sql.append(" where a.bank_acc = b.bank_acc  and b.corp_id = c.corp_id and c.pool_sign = '1' and a.opp_acc_no = e.bank_acc");
        sql.append(" and e.corp_id = d.corp_id  and d.pool_sign = '0' and e.id = f.bank_acc_id and f.prent_acc_id = b.id");
        sql.append(" and c.id = d.pool_id ");
        sql.append(" and a.cur_id = cur.id and accdtl.dtl_id = a.id and accdtl.tally_status = 0 ");
        if ("up".equals(menuType)) {
            sql.append(" and a.cd_sign = '0' ");
        } else {
            sql.append(" and a.cd_sign = '1' ");
        }
        String leTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        String getTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        if (StringUtils.isNotEmpty(getTime)) {
            getTime = getTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", getTime);
        }
        if (StringUtils.isNotEmpty(leTime)) {
            leTime = leTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", leTime);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlDTO.class, null, false);
    }
    /**
     *
     */
    public FcBankAccDtlDTO findByOrderNo(String orderNo) {
        String sql = "select * from Fc_Bank_Acc_Dtl where order_No = :orderNo ";
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", orderNo);
        return getBySQLAliasToBean(sql, map, FcBankAccDtlDTO.class);
    }
    public FcBankAccDtlDTO findByDtlId(String id) {
        String sql = "select * from Fc_Bank_Acc_Dtl where dtl_Id = :id ";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return getBySQLAliasToBean(sql, map, FcBankAccDtlDTO.class);
    }
    /**
     * 待记账数据
     */
    public List<BisAccDtlDTO> queryTallyBisAccDtl(String menuType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id,a.bal,c.id as poolId ");
        sql.append("from bis_acc_dtl a,bt_bank_acc b,fc_corp_pool c,fc_corp_pool d, bt_bank_acc e, bt_bank_acc_cur f, ");
        sql.append(" bt_currency cur ");
        sql.append(" where a.bank_acc = b.bank_acc and b.corp_id = c.corp_id and c.pool_sign = '1' and c.status = '1' ");
        sql.append(" and a.opp_acc_no = e.bank_acc and e.corp_id = d.corp_id and d.pool_sign = '0' and d.status = '1' ");
        sql.append(" and e.id = f.bank_acc_id and f.prent_acc_id = b.id  and a.cur_id = cur.id ");
        sql.append(" and c.id = d.pool_id ");
        sql.append(" and not exists (select 1 from Fc_bank_acc_dtl fbc where fbc.dtl_id = a.id )");
        if ("up".equals(menuType)) {
            sql.append(" and a.cd_sign = '0' ");
        } else {
            sql.append(" and a.cd_sign = '1' ");
        }
        sql.append(" and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = a.id ) ");
        sql.append(" union ");
        sql.append(" select  a.id,a.bal,c.id as poolId  ");
        sql.append(" from bis_acc_dtl a,bt_bank_acc b,fc_corp_pool c,fc_corp_pool d,bt_bank_acc e, bt_bank_acc_cur f,");
        sql.append(" bt_currency cur,fc_bank_acc_dtl accdtl ");
        sql.append(" where a.bank_acc = b.bank_acc  and b.corp_id = c.corp_id and c.pool_sign = '1' and a.opp_acc_no = e.bank_acc");
        sql.append(" and e.corp_id = d.corp_id  and d.pool_sign = '0' and e.id = f.bank_acc_id and f.prent_acc_id = b.id");
        sql.append(" and c.id = d.pool_id ");
        sql.append(" and a.cur_id = cur.id and accdtl.dtl_id = a.id and accdtl.tally_status = 0 ");
        if ("up".equals(menuType)) {
            sql.append(" and a.cd_sign = '0' ");
        } else {
            sql.append(" and a.cd_sign = '1' ");
        }
        sql.append(" and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = a.id ) ");
        return listBySQLAliasToBean(sql.toString(), null, BisAccDtlDTO.class);
    }
    public List<BisAccDtlDTO> listByBankAccOrOppAcc(List<String> bankAccList, List<String> oppAccNoList, String type, String userId) {
        StringBuilder sql = new StringBuilder("select id as id,bank_acc as bankAcc,tally_status as tallySign,dtl_id as accDtlId from fc_bank_acc_dtl where ");
        sql.append(getSQLIn(bankAccList, 900, "bank_acc")).append(" and ").append(getSQLIn(oppAccNoList, 900, "opp_acc_no"));
        if ("Edit".equals(type)) {
            sql.append(" and (tally_status in( '5' , '4' , '3' )) and tally_code = '").append(userId).append("'");
        } else {
            sql.append(" and (tally_status in( '3' )) and tally_code != '").append(userId).append("'");
        }
        return listBySQLAliasToBean(sql.toString(), null, BisAccDtlDTO.class);
    }

    public List<FcBankAccDtlDTO> listFcBankAccDtlDTOByDtlIds(List<String> dtlIds, String type, String userId) {
        StringBuilder sql = new StringBuilder("select * from fc_bank_acc_dtl where ");
        sql.append(getSQLIn(dtlIds, 900, "dtl_id"));
        if ("Edit".equals(type)) {
            sql.append(" and (tally_status in( '5' , '4' , '3' )) and tally_code = '").append(userId).append("'");
        } else {
            sql.append(" and (tally_status in( '3' )) and tally_code != '").append(userId).append("'");
        }
        return listBySQLAliasToBean(sql.toString(), null, FcBankAccDtlDTO.class);
    }
    public List<FcBankAccDtlDTO> getFcBankAccDtlsByIds(List<String> ids){
        StringBuilder sql = new StringBuilder("select * from fc_bank_acc_dtl fbad where fbad.tally_status = 0 ");
        sql.append("and "+SqlUtil.getInSql(ids, "fbad.dtl_id", true));
        return listBySQLAliasToBean(sql.toString(), null, FcBankAccDtlDTO.class);
    }

}
