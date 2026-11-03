package com.bytter.tally.dao;

import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.BtNetChalkDTO;
import com.bytter.biz.finacial.tally.entity.BtNetChalk;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.tally.vo.BisAccDtlTallyVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BisAccDtlTallyDao
 * @Author zx
 * @Description 下拨手工记账Dao
 * @Date 2024/10/29 15:21
 */
@Repository
public class BisAccDtlTallyDao extends BaseDao<BisAccDtlTallyVO, String> {

    public String queryCommonSql(){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT dtl.id AS id,dtl.TRANS_TIME AS transTime,dtl.BANK_NAME AS bankName,sc.net_id AS corpId,");
        sql.append(" dtl.BANK_ACC AS bankAcc,fa.BLNCTR_ACC AS cntAccount,dtl.ACC_NAME AS accName,");
        sql.append(" fa.ACC_NAME as cntAccName,");
        sql.append(" dtl.CUR_ID AS curID,dtl.amt AS money,dtl.OPP_ACC_NO AS oppAccNo,");
        sql.append(" dtl.OPP_ACC_NAME AS oppAccName,dtl.OPP_ACC_BANK AS oppBankName,");
        sql.append(" fa2.BLNCTR_ACC AS oppCntAccount,fa2.ACC_NAME as oppCntAccName,");
        sql.append(" book.id as bookId,dtl.BUS_GLOBAL_ID as busGlobalId,");
        sql.append(" book.ORDER_NO AS orderNo,dtl.REMARK AS remark,dtl.POSTSCRIPT AS postscript,");
        sql.append(" dtl.USES AS purpose,dtl.ESTATE AS estate");
        //银行流水本方账户是父账户且关联内部账户
        sql.append(" FROM bis_acc_dtl dtl INNER JOIN bt_bank_acc acc ON acc.BANK_ACC = dtl.BANK_ACC ");
        sql.append(" LEFT JOIN sys_corp sc ON sc.id = acc.corp_id");
        sql.append(" INNER JOIN bt_bank_acc_cur acccur ON acccur.BANK_ACC_ID = acc.ID");
        sql.append(" INNER JOIN fc_acc fa ON fa.BLNCTR_ACC = acccur.COUNTER_ACC");
        //银行流水对方账户是子账户且关联内部账户
        sql.append(" INNER JOIN bt_bank_acc acc2 ON acc2.BANK_ACC = dtl.OPP_ACC_NO ");
        sql.append(" INNER JOIN bt_bank_acc_cur acccur2 ON acccur2.BANK_ACC_ID = acc2.ID");
        sql.append(" INNER JOIN fc_acc fa2 ON fa2.BLNCTR_ACC = acccur2.COUNTER_ACC");
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
        sql.append(" and exists (select bb.prent_acc_id from bt_bank_acc_cur bb, bt_bank_acc bc ");
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


    /**
     * 更新下拨流水记账状态
     *
     * @param entity
     * @param idList
     * @param eState
     * @return
     */
    public int updateTallySign(BisAccDtlTallyVO entity, List<String> idList, String eState) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update BIS_ACC_DTL set BUS_GLOBAL_ID = :busGlobalId, ESTATE = :eState, ");
        sb.append(" book_id = :bookId, ");
        sb.append(" OPERATOR_CODE = :operatorCode ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (ESTATE = '0' or ESTATE = '6' or ESTATE = '' or ESTATE is null ) ");
        sb.append(" and id in (:id)  ");

        Map<String, Object> param = new HashMap<>();
        param.put("operatorCode", entity.getOperatorCode());
        param.put("busGlobalId", entity.getBusGlobalId());
        param.put("bookId", entity.getBookId());
        param.put("eState", eState);
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }


    /**
     * 改账自动复核
     *
     * @param entity
     * @param idList
     * @return
     */
    public int updateEditAutoCheckSign(BisAccDtlTallyVO entity, List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update BIS_ACC_DTL set operator_code = :operatorCode, Bus_Global_Id = :busGlobalId, ");
        sb.append(" book_id = :bookId, ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '3' or eState = '4' or eState = '5') ");
        sb.append(" and id in (:id) ");

        Map<String, Object> param = new HashMap<>();
        param.put("operatorCode", entity.getOperatorCode());
        param.put("busGlobalId", entity.getBusGlobalId());
        param.put("bookId", entity.getBookId());
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }


    /**
     * 改账提交
     *
     * @param idList
     * @param eState
     * @return
     */
    public int updateEditPassSign(List<String> idList, String eState) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update BIS_ACC_DTL set eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '3' or eState = '4' or eState = '5') ");
        sb.append(" and id in (:id)  ");

        Map<String, Object> param = new HashMap<>();
        param.put("eState", eState);
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * 下拨流水记账自动复核
     *
     * @param entity
     * @param idList
     * @return
     */
    public int updateAutoCheckSign(BisAccDtlTallyVO entity, List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update BIS_ACC_DTL set operator_code = :operatorCode, Bus_Global_Id = :busGlobalId, ");
        sb.append(" book_id = :bookId, ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '0' or eState = '6' or eState = '' or eState is null ) ");
        sb.append(" and id in (:id)  ");

        Map<String, Object> param = new HashMap<>();
        param.put("operatorCode", entity.getOperatorCode());
        param.put("busGlobalId", entity.getBusGlobalId());
        param.put("bookId", entity.getBookId());
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * 取消
     *
     * @param bisAccDtlTallyVO
     * @return
     */
    public int updateBisAccDtlEState(BisAccDtlTallyVO bisAccDtlTallyVO) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update BIS_ACC_DTL set eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and id = :id  ");

        Map<String, Object> param = new HashMap<>();
        param.put("eState", bisAccDtlTallyVO.getEstate());
        param.put("id", bisAccDtlTallyVO.getId());
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }


    /**
     * 下拨流水改账打回
     *
     * @param id
     * @param oriEState
     * @return
     */
    public int updateEditBackSign(String id, String oriEState) {
        String sb = " update BIS_ACC_DTL set  " +
                " operator_code = null, Bus_Global_Id = null, book_id = null," +
                " eState = :eState " +
                " where 1 = 1  " +
                " and (eState = :oriEState ) " +
                " and id = :id  ";
        Map<String, Object> param = new HashMap<>();
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_BACK.getValue());
        param.put("oriEState", oriEState);
        param.put("id", id);
        return createSQLQuery(sb, param).executeUpdate();
    }

    /**
     * 下拨流水复核打回
     *
     * @param id
     * @return
     */
    public int updateCheckBackSign(String id) {
        String sb = " update BIS_ACC_DTL set eState = :eState " +
                " where 1 = 1  " +
                " and eState = '3' " +
                " and id = :id  ";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
        return createSQLQuery(sb, param).executeUpdate();
    }

    /**
     * 下拨流水合并打回
     *
     * @param idList
     * @return
     */
    public int mergeUpdateEditBackSign(List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update BIS_ACC_DTL set ");
        sb.append(" operator_code = null, Bus_Global_Id = null,book_id = null, ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '3' or eState = '4' or eState = '5') ");
        sb.append(" and id in (:idList)  ");

        Map<String,Object> param = new HashMap<>();
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_BACK.getValue());
        param.put("idList", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * 下拨流水合并复核打回
     *
     * @param idList
     * @return
     */
    public int mergeUpdateCheckBackSign(List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update BIS_ACC_DTL set eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and eState = '3' ");
        sb.append(" and id in (:id)  ");

        Map<String,Object> param = new HashMap<>();
        param.put("id", idList);
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * 通过bookId查询下拨流水
     *
     * @param bookId
     * @return
     */
    public List<String> listByBookId(String bookId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select dtl.id from BIS_ACC_DTL dtl LEFT JOIN fc_accbook book ON book.BUS_GLOBAL_ID = dtl.BUS_GLOBAL_ID where book.id = :bookId";
        params.put("bookId", bookId);
        List<BtNetChalk> list = this.listBySQLAliasToBean(sql, params, BtNetChalk.class);
        List<String> idList = Lists.newArrayList();
        list.forEach(btNetChalk -> idList.add(btNetChalk.getId()));
        return idList;
    }

    /**
     * 复核下拨流水记账
     *
     * @param entity
     * @param idList
     * @return
     */
    public int updateCheckSign(BisAccDtlTallyVO entity, List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update BIS_ACC_DTL set ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and eState = '3' ");
        sb.append(" and id in (:id) ");

        Map<String, Object> param = new HashMap<>();
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

}
