package com.bytter.increment.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.increment.api.constant.AgreedConstant;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.domain.entity.FcAgreedDeposit;
import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Dao
 * @Description: 协定存款登记表
 * @date 2019-05-10 16:44:38
 */
@Repository
public class FcAgreedDepositDao extends BaseDao<FcAgreedDeposit, String> {
    /**
     * 协定存款登记表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select d.id as id, d.net_id as netId,d.net_name as netName,d.corp_id as corpId, d.corp_name as corpName, d.acc_id as accId,");
        sql.append("d.cur_id as curId,d.cur_name as curName, d.blnctr_acc as blnctrAcc,d.acc_name as accName,");
        sql.append("d.begin_date as beginDate, d.end_date as endDate, d.contract_no as contractNo, d.status as status,");
        sql.append("d.bill_status as billStatus, d.bus_type as busType, d.agreed_deposit_ori_id as agreedDepositOriId,");
        sql.append("d.stop_date as stopDate, d.checker_by as checkerBy, d.check_date as checkDate, d.pool_sign as poolSign,");
        sql.append("d.create_by as createBy, d.create_date as createDate, d.update_by as updateBy, d.update_date as updateDate,");
        sql.append("case when bus_type = '1' then (select a.begin_date from fc_agreed_deposit a where a.id = d.agreed_deposit_ori_id) " +
                "end oldBeginDate,");
        sql.append("case when bus_type = '1' then (select a.end_date from fc_agreed_deposit a where a.id = d.agreed_deposit_ori_id) " +
                "end oldEndDate");
        sql.append(" from fc_agreed_deposit d ");
        sql.append("  where 1=1 and d.pool_sign = '1'");
        return sql.toString();
    }
    /**
     * 查询协定存款登记表维护列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput findListBysql(DataTablesInput input, ICurrentUser user, String joinSql) {
        String opType = (String) input.getFormdata().get("opType");
        StringBuilder sql = new StringBuilder(getCommonSql());
        //登记
        if(opType.equals(AgreedConstant.BUS_TYPE_0)){
            sql.append(" and d.status <> " + AgreedConstant.STATUE_DEL + " and d.create_by= '" +user.getLoginName() + "' and d.bus_type='0'");
        }
        //延期
        if(opType.equals(AgreedConstant.BUS_TYPE_1)){
            sql.append(" and d.status <> " + AgreedConstant.STATUE_DEL + " and d.create_by= '" +user.getLoginName() + "' and d.bus_type='1'");
        }
        //终止
        if(opType.equals(AgreedConstant.BUS_TYPE_2)){
            sql.append(" and d.status <> " + AgreedConstant.STATUE_DEL + " and d.create_by= '" +user.getLoginName() + "' and d.bus_type='2'");
        }
        sql.append(joinSql);

        return getDynamicBySQL(input, sql.toString(), FcAgreedDepositVO.class, "d", false);
    }
    /**
     * 查询协定存款登记表复核维护列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput checkListBysql(DataTablesInput input, ICurrentUser user, String joinSql) {
        StringBuilder sql = new StringBuilder(getCommonSql());
        String opType = (String) input.getFormdata().get("opType");
        sql.append(" and d.status = " + AgreedConstant.STATUE_WAIT_CHECK + " and d.create_by <> '" +user.getLoginName() + "'");
        //登记
        if(opType.equals(AgreedConstant.BUS_TYPE_0)){
            sql.append(" and d.bus_type= " + AgreedConstant.BUS_TYPE_0);
        }
        //延期
        if(opType.equals(AgreedConstant.BUS_TYPE_1)){
            sql.append(" and d.bus_type= " + AgreedConstant.BUS_TYPE_1);
        }
        //终止
        if(opType.equals(AgreedConstant.BUS_TYPE_2)){
            sql.append(" and d.bus_type= " + AgreedConstant.BUS_TYPE_2);
        }
        sql.append(joinSql);
        return getDynamicBySQL(input, sql.toString(), FcAgreedDepositVO.class, "d", false);
    }
    /**
     * 根据ID查询协定存款登记信息
     *
     * @param id
     * @return
     */
    public FcAgreedDepositVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder(getCommonSql());
        sql.append(" and d.id = :id ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FcAgreedDepositVO.class);
    }
    // /**
    //  * 根据资金池或开户单位查找内部账号
    //  * @param poolId
    //  * @param corpId
    //  * @return
    //  */
    // public List<FcAccVO> getBlnctrAcc(String poolId, String corpId, String busType, String accAuthSql){
    //     StringBuilder sql = new StringBuilder();
    //     sql.append(" select f.id as id, f.blnctr_acc as blnctrAcc, f.acc_name as accName, f.cur_id as curId from fc_acc f ");
    //     sql.append(" where f.acc_state='N' and f.pool_sign = '1' and f.acc_type = '01'");
    //     //登记只查有效的账号
    //     if (busType.equals(AgreedConstant.BUS_TYPE_0)) {
    //         /*登记查有效的企业活期账号*/
    //         sql.append(" and f.id not in (select d.acc_id from fc_agreed_deposit d where d.bill_status = 10 and d.status > 0 and d.bus_type = 0)");
    //         sql.append("and f.id not in (select d.acc_id from fc_agreed_deposit d where d.bill_status = 11");
    //         sql.append(" and d.status = 95 and d.id in (select a.agreed_deposit_ori_id from fc_agreed_deposit a");
    //         sql.append(" where a.status < 95 and a.status <>-2 and a.bus_type = '2'))");
    //     } else if (busType.equals(AgreedConstant.BUS_TYPE_1)) {
    //         /*延期查登记审批通过的企业活期账号*/
    //         sql.append(" and f.id in (select d.acc_id from fc_agreed_deposit d where d.pool_sign = 1");
    //         sql.append(" and ((d.bus_type = '0' and d.status = 95 and d.bill_status = 10)");
    //         sql.append(" or (d.bus_type = '1' and d.status in (0,-1) and d.bill_status = 10))");
    //         sql.append(" and bt_to_char(d.end_date, 'yyyy-MM-dd') >= :endDate)");
    //     } else {
    //         /*终止查登记审批通过的企业活期账号或延期审批通过的企业活期账号*/
    //         sql.append(" and f.id in (select d.acc_id from fc_agreed_deposit d");
    //         sql.append(" where ((d.bill_status = 10 and d.status = 95 and d.bus_type in ('0', '1')) ");
    //         sql.append(" or (d.bus_type = '2' and d.status in (0,-1) and d.bill_status = 10 ))");
    //         sql.append(" and bt_to_char(d.end_date, 'yyyy-MM-dd') >= :endDate)");
    //     }
    //
    //     sql.append(" and ").append(accAuthSql);
    //     Map<String,Object> params = new HashMap<>();
    //     if(StringUtils.isNotBlank(poolId)){
    //         sql.append(" and f.net_id = :poolId");
    //         params.put("poolId",poolId);
    //     }
    //     if(StringUtils.isNotBlank(corpId)){
    //         sql.append(" and f.corp_id = :corpId");
    //         params.put("corpId",corpId);
    //     }
    //     if (InnerFinanceConstant.BUS_TYPE_1.equals(busType) || InnerFinanceConstant.BUS_TYPE_2.equals(busType)) {
    //         params.put("endDate", DateUtils.formatDate(DateUtils.getDate()));
    //     }
    //     return listBySQLAliasToBean(sql.toString(),params,FcAccVO.class);
    // }
    /**
     * 获取企业活期账号。
     *
     * @param busType 单据类型。
     * @return 企业活期账号。
     */
    public List<String> getAccIdForDeposit(String busType) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        if (busType.equals(AgreedConstant.BUS_TYPE_0)) {
            /*登记查有效的企业活期账号 union 去重*/
            sql.append(" select d.acc_id from fc_agreed_deposit d where d.bill_status = 10 and d.status > 0 and d.bus_type = 0");
            sql.append(" union ");
            sql.append(" select d.acc_id from fc_agreed_deposit d where d.bill_status = 11");
            sql.append(" and d.status = 95 and d.id in (select a.agreed_deposit_ori_id from fc_agreed_deposit a");
            sql.append(" where a.status < 95 and a.status <>-2 and a.bus_type = '2') ");
        } else if (busType.equals(AgreedConstant.BUS_TYPE_1)) {
            /*延期查登记审批通过的企业活期账号*/
            sql.append(" select d.acc_id from fc_agreed_deposit d where d.pool_sign = 1");
            sql.append(" and ((d.bus_type = '0' and d.status = 95 and d.bill_status = 10)");
            sql.append(" or (d.bus_type = '1' and d.status in (0,-1) and d.bill_status = 10))");
            sql.append(" and bt_to_char(d.end_date, 'yyyy-MM-dd') >= :endDate ");
        } else {
            /*终止查登记审批通过的企业活期账号或延期审批通过的企业活期账号*/
            sql.append(" select d.acc_id from fc_agreed_deposit d");
            sql.append(" where ((d.bill_status = 10 and d.status = 95 and d.bus_type in ('0', '1')) ");
            sql.append(" or (d.bus_type = '2' and d.status in (0,-1) and d.bill_status = 10 ))");
            sql.append(" and bt_to_char(d.end_date, 'yyyy-MM-dd') >= :endDate ");
        }
        if (InnerFinanceConstant.BUS_TYPE_1.equals(busType) || InnerFinanceConstant.BUS_TYPE_2.equals(busType)) {
            params.put("endDate", DateUtils.formatDate(DateUtils.getDate()));
        }
        return createSQLQuery(sql.toString(), params).list();
    }
    /**
     * 根据账户id查找协定主信息
     * @param accId
     * @return
     */
    public List<FcAgreedDepositVO> getAgreeInfo(String accId,String busType){
        StringBuilder sql = new StringBuilder(getCommonSql());
        sql.append(" and d.acc_id =:accId ");
        Map<String, Object> params = new HashMap<>();
        if (InnerFinanceConstant.BUS_TYPE_1.equals(busType) || InnerFinanceConstant.BUS_TYPE_2.equals(busType)) {
            sql.append("and bt_to_char(d.end_date,'yyyy-MM-dd') >= :endDate");
            params.put("endDate", DateUtils.formatDate(DateUtils.getDate()));
        }
        /*取单据状态为登记的数据*/
        sql.append(" and d.bill_status = :billStatus and d.status <> -2");
        params.put("accId", accId);
        params.put("billStatus", InnerFinanceConstant.BILL_STATUS_10);
        return listBySQLAliasToBean(sql.toString(), params, FcAgreedDepositVO.class);
    }
    /**
     * 根据账户id查找协定主信息
     * @param accId
     * @return
     */
    public List<FcAgreedDeposit> getInfoByAcc(String accId, String busType, Integer status){
        StringBuilder sql = new StringBuilder();
        sql.append("select * from fc_agreed_Deposit where acc_Id = :accId and status <> :status and bus_Type =:busType");
        sql.append(" and id not in (select a.agreed_deposit_ori_id from fc_agreed_deposit a");
        sql.append(" where a.bus_type in ('" + AgreedConstant.BUS_TYPE_1 + "','" + AgreedConstant.BUS_TYPE_2 + "')");
        sql.append(" and bill_status = 10)");
        Map<String,Object> params = new HashMap<>();
        params.put("accId",accId);
        params.put("status", status);
        params.put("busType", busType);
        return listBySQLAliasToBean(sql.toString(),params,FcAgreedDeposit.class);
    }
    /**
     * 根据合同号查找协定信息
     *
     * @param contractNo
     * @return
     */
    public List<FcAgreedDepositVO> getInfoByContractNo(String contractNo) {
        StringBuilder sql = new StringBuilder(getCommonSql());
        sql.append(" and d.contract_no =:contractNo and d.status <> -2");
        Map<String, Object> params = new HashMap<>();
        params.put("contractNo", contractNo);
        return listBySQLAliasToBean(sql.toString(), params, FcAgreedDepositVO.class);
    }
    /**
     * 根据账户id,单据类型,状态查找协定信息
     *
     * @param accId
     * @return
     */
    public List<FcAgreedDepositVO> getAgreedInfo(String accId, String busType, List<Integer> status) {
        StringBuilder sql = new StringBuilder(getCommonSql());
        Map<String, Object> params = new HashMap<>();
        sql.append(" and d.acc_id = :accId and d.bill_status = 10 and d.bus_type =:busType and d.status in (:status) ");
        params.put("accId", accId);
        params.put("busType", busType);
        params.put("status", status);
        return listBySQLAliasToBean(sql.toString(), params, FcAgreedDepositVO.class);
    }
    /**
     * 根据单位统计协定存款单
     * @param corpId
     * @return
     */
    public int getAgreedDepositCountByCorpId(String corpId){
        String sql="select count(*) from fc_agreed_Deposit where corp_id=:corpId";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql+=" and status in ('0','11','-1')";
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
    /**
     * 根据单位统计未终止的协定存款单
     * @param corpId
     * @return
     */
    public int getAgreedDepositNotEndCountByCorpId(String corpId){
        String sql="select count(*) from fc_agreed_Deposit f left join fc_agreed_Deposit d on f.id=d.agreed_deposit_ori_id where f.bus_type in ('0','1') ";
        sql += " and f.corp_id=:corpId and f.status=:status";
        sql += " and (d.id is null or (d.bus_type=:busType and d.status<>:status)) ";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("status", AgreedConstant.STATUE_CHECK);
        params.put("busType", AgreedConstant.BUS_TYPE_2);
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
}
