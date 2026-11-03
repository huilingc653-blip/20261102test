package com.bytter.tally.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.tally.vo.BisAccDtlTallyVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BisAccDtlTallyOtherPayDao
 * @Author zx
 * @Description 其他付款手工记账Dao
 * @Date 2024/11/14 9:30
 */
@Repository
public class BisAccDtlTallyOtherPayDao extends BaseDao<BisAccDtlTallyVO, String> {

    public String queryCommonSql(){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT dtl.id AS id,dtl.TRANS_TIME AS transTime,dtl.BANK_NAME AS bankName,sc.net_id AS corpId,");
        sql.append(" dtl.BANK_ACC AS bankAcc,dtl.ACC_NAME AS accName,");
        sql.append(" dtl.CUR_ID AS curID,bc.cur_name as curName,dtl.amt AS money,dtl.OPP_ACC_NO AS oppAccNo,");
        sql.append(" dtl.OPP_ACC_NAME AS oppAccName,dtl.OPP_ACC_BANK AS oppBankName,");
        sql.append(" book.id as bookId,dtl.BUS_GLOBAL_ID as busGlobalId,");
        sql.append(" fa.BLNCTR_ACC AS cntAccount,fa.ACC_NAME as cntAccName,");
        sql.append(" book.ORDER_NO AS orderNo,dtl.REMARK AS remark,dtl.POSTSCRIPT AS postscript,");
        sql.append(" dtl.USES AS purpose,dtl.ESTATE AS estate,sc.name as corpName ");
        sql.append(" FROM bis_acc_dtl dtl left JOIN bt_bank_acc acc ON acc.BANK_ACC = dtl.BANK_ACC ");
        sql.append(" LEFT JOIN bt_bank_acc_cur acccur ON acccur.BANK_ACC_ID = acc.ID");
        sql.append(" LEFT JOIN fc_acc fa ON fa.BLNCTR_ACC = acccur.COUNTER_ACC");
        sql.append(" LEFT JOIN sys_corp sc ON sc.id = acc.corp_id");
        sql.append(" LEFT JOIN bt_currency bc on bc.id = dtl.cur_id ");
        sql.append(" LEFT JOIN fc_accbook book ON book.BUS_GLOBAL_ID = dtl.BUS_GLOBAL_ID");
        return sql.toString();
    }

    /**
     * 查询下拨手工记账数据
     *
     * @param dataTablesInput
     * @param type
     * @param userId
     * @param corpAuth
     * @return
     */
    public DataTablesOutput<BisAccDtlTallyVO> queryBisAccDtlTally(DataTablesInput dataTablesInput, String type, String userId, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(queryCommonSql());
        //只取付款流水
        sql.append(" WHERE 1=1 AND dtl.CD_SIGN = '1' and sc.type = 0 ");
        if ("Init".equals(type)) {
            sql.append(" AND (dtl.estate = '0' OR dtl.estate = '6' OR dtl.estate IS NULL OR dtl.estate='')");
        } else if ("Edit".equals(type)) {
            sql.append(" and (dtl.estate in( '5' , '4' , '3' )) and dtl.operator_code = '").append(userId).append("'");
        } else if ("Check".equals(type)) {
            sql.append(" and (dtl.estate = '3' ) and dtl.operator_code != '").append(userId).append("'");
        }
        sql.append(" and not exists (select bb.prent_acc_id from bt_bank_acc_cur bb, bt_bank_acc bc ");
        sql.append(" where bb.bank_acc_id = bc.id and bc.bank_acc = dtl.opp_Acc_No and acc.id = bb.prent_acc_id) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_id"));
        dataTablesInput.setOrderString(new OrderString("dtl.trans_time", "asc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlTallyVO.class, null, false);
    }

    /**
     * 根据流水Id查询记账数据
     *
     * @param id
     * @return
     */
    public BisAccDtlTallyVO getBisAccDtlInfoById(String id){
        StringBuilder sql=new StringBuilder();
        sql.append(queryCommonSql());
        sql.append(" where 1=1 and dtl.id = :id");
        Map<String,Object> params = new HashMap<>();
        params.put("id",id);
        return getBySQLAliasToBean(sql.toString(), params, BisAccDtlTallyVO.class);
    }

    /**
     * 根据流水Id查询记账数据List
     *
     * @param idList
     * @return
     */
    public List<BisAccDtlTallyVO> listBisAccDtlInfo(List<String> idList) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql=new StringBuilder();
        sql.append(queryCommonSql());
        sql.append(" where 1=1 and dtl.id in (:id)");
        params.put("id", idList);
        return this.listBySQLAliasToBean(sql.toString(), params, BisAccDtlTallyVO.class);
    }

}
