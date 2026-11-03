package com.bytter.increment.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.domain.entity.FcAgreedDeposit;
import com.bytter.increment.domain.vo.FcAccVO;
import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import org.springframework.stereotype.Repository;
import java.util.Date;
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
public class ChoInnerAgreedDepositDao extends BaseDao<FcAgreedDeposit, String> {
    /**
     * 协定存款登记表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select d.id as id, d.net_id as netId, d.corp_id as corpId, d.corp_name as corpName, d.acc_id as accId,d.net_name as netName, ");
        sql.append("d.cur_id as curId, d.cur_name as curName, d.blnctr_acc as blnctrAcc, d.acc_name as accName,");
        sql.append("d.begin_date as beginDate, d.end_date as endDate, d.contract_no as contractNo, d.status as status,");
        sql.append("d.bill_status as billStatus, d.bus_type as busType, d.agreed_deposit_ori_id as agreedDepositOriId,");
        sql.append("d.stop_date as stopDate, d.checker_by as checkerBy, d.check_date as checkDate, d.pool_sign as poolSign,");
        sql.append("d.create_by as createBy, d.username as userName, d.create_date as createDate, d.update_by as updateBy, d.update_date as updateDate,");
        sql.append("d.bill_no as billNo,d.certificate_way as certificateWay,d.is_agreed_deposit_acc as isAgreedDepositAcc,");
        sql.append("d.agr_interest as agrInterest,d.agr_acc_id as agrAccId,d.agr_acc as agrAcc,d.retained_bal as retainedBal,");
        sql.append("case when d.bus_type = '1' then (select a.end_date from fc_agreed_deposit a where a.id = d.agreed_deposit_ori_id) " +
                "end oldEndDate ");
        sql.append(" from fc_agreed_deposit d where 1=1 ");
        return sql.toString();
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
    /**
     * 根据协定id查找协定延期信息
     *
     * @param depositOriId
     * @return
     */
    public FcAgreedDeposit getInfoByDepositOriId(String depositOriId, String busType, Integer status) {
        StringBuilder sql = new StringBuilder(getCommonSql());
        sql.append("and  d.agreed_deposit_ori_id =:depositOriId  and d.status <> -2 ");
        Map<String, Object> params = new HashMap<>();
        params.put("depositOriId", depositOriId);
        if (StringUtils.isNotBlank(busType)) {
            sql.append(" and d.bus_Type =:busType ");
            params.put("busType", busType);
        }
        if (!EmptyUtils.isEmpty(status)) {
            sql.append(" and d.status =:status ");
            params.put("status", status);
        }
        List<FcAgreedDeposit> fcAgreedDeposit = listBySQLAliasToBean(sql.toString(), params, FcAgreedDeposit.class);
        if (EmptyUtils.isEmpty(fcAgreedDeposit)) {
            return null;
        }
        return fcAgreedDeposit.get(0);
    }
    /**
     * 内部存款-协定存款登记表查询列表
     *
     * @param input
     * @param user
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, ICurrentUser user, String joinSql) {
        String opType = (String) input.getFormdata().get("opType");
        // 移除无关的非SQL条件
        input.getFormdata().remove("opType");
        StringBuilder sql = new StringBuilder(getCommonSql());
        sql.append(" and d.pool_sign='" + InnerFinanceConstant.POOL_SIGN_0 + "'");
        sql.append(" and d.status in (").append(InnerFinanceConstant.STATUE_TEMP).append(",").append(InnerFinanceConstant.STATUE_BACK).append(")");
        /*登记*/
        if (opType.equals(InnerFinanceConstant.BUS_TYPE_0)) {
            sql.append(" and d.create_by= '" + user.getLoginName() + "' and d.bus_type='" + InnerFinanceConstant.BUS_TYPE_0 + "'");
        }
        /*延期*/
        if (opType.equals(InnerFinanceConstant.BUS_TYPE_1)) {
            sql.append(" and d.create_by= '" + user.getLoginName() + "' and d.bus_type='" + InnerFinanceConstant.BUS_TYPE_1 + "'");
        }
        /*终止*/
        if (opType.equals(InnerFinanceConstant.BUS_TYPE_2)) {
            sql.append(" and d.create_by= '" + user.getLoginName() + "' and d.bus_type='" + InnerFinanceConstant.BUS_TYPE_2 + "'");
        }
        sql.append(joinSql);
        return getDynamicBySQL(input, sql.toString(), FcAgreedDepositVO.class, "d", false);
    }
    // /**
    //  * 根据开户单位,单据类型查找正常状态非资金池内部账号
    //  *
    //  * @param corpId
    //  * @param busType
    //  * @param accAuthSql
    //  * @return
    //  */
    // public List<FcAccVO> getBlnctrAccByCorpId(String corpId, String busType, String accAuthSql) {
    //     Map<String, Object> params = new HashMap<>();
    //     StringBuilder sql = new StringBuilder();
    //     sql.append(" select f.id as id, f.blnctr_acc as blnctrAcc, f.acc_name as accName, f.cur_id as curId from fc_acc f ");
    //     sql.append(" where f.acc_state='N' and (f.pool_sign = '0' or f.pool_sign is null ) and f.acc_type = '01'");
    //     if (busType.equals(InnerFinanceConstant.BUS_TYPE_0)) {
    //         /*登记查有效的企业活期账号*/
    //         sql.append(" and f.id not in (select d.acc_id from fc_agreed_deposit d where d.bill_status = 10 and d.status > 0 and d.bus_type = 0)");
    //         sql.append("and f.id not in (select d.acc_id from fc_agreed_deposit d where d.bill_status = 11");
    //         sql.append(" and d.status = 95 and d.id in (select a.agreed_deposit_ori_id from fc_agreed_deposit a");
    //         sql.append(" where a.status < 95 and a.status <>-2 and a.bus_type = '2'))");
    //     } else if (busType.equals(InnerFinanceConstant.BUS_TYPE_1)) {
    //         /*延期查登记审批通过的企业活期账号*/
    //         sql.append(" and f.id in (select d.acc_id from fc_agreed_deposit d where d.pool_sign = 0");
    //         sql.append(" and ((d.bus_type = '0' and d.status = 95 and d.bill_status = 10)");
    //         sql.append(" or (d.bus_type = '1' and d.status = 95 and d.bill_status = 10)");
    //         sql.append(" or (d.bus_type = '1' and d.status in (0,-1) and d.bill_status = 10))");
    //         sql.append(" and bt_to_char(d.end_date, 'yyyy-MM-dd') >= :endDate)");
    //     } else {
    //         /*终止查登记审批通过的企业活期账号或延期审批通过的企业活期账号*/
    //         sql.append(" and f.id in (select d.acc_id from fc_agreed_deposit d");
    //         sql.append(" where ((d.bill_status = 10 and d.status = 95 and d.bus_type in ('0', '1')) ");
    //         sql.append(" or (d.bus_type = '2' and d.status in (0,-1) and d.bill_status = 10 ))");
    //         sql.append(" and bt_to_char(d.end_date, 'yyyy-MM-dd') >= :endDate)");
    //     }
    //     sql.append(" and ").append(accAuthSql);
    //     if (StringUtils.isNotBlank(corpId)) {
    //         sql.append(" and f.corp_id = :corpId");
    //         params.put("corpId", corpId);
    //     }
    //     if (InnerFinanceConstant.BUS_TYPE_1.equals(busType) || InnerFinanceConstant.BUS_TYPE_2.equals(busType)) {
    //         params.put("endDate", DateUtils.formatDate(DateUtils.getDate()));
    //     }
    //     return listBySQLAliasToBean(sql.toString(), params, FcAccVO.class);
    // }
    /**
     * 根据账户id,单据类型查找协定主信息
     *
     * @param accId
     * @return
     */
    public List<FcAgreedDepositVO> getChoAgreeInfo(String accId, String busType) {
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
    * <br>
    * <b>功能：</b> 查询协定存款信息 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/9/4 <br>
    */
    private String getAgreeSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select d.id as id, d.net_id as netId, d.corp_id as corpId, d.corp_name as corpName, d.acc_id as accId,d.net_name as netName, ");
        sql.append("d.cur_id as curId, d.cur_name as curName, d.blnctr_acc as blnctrAcc, d.acc_name as accName,");
        sql.append("d.begin_date as beginDate, d.end_date as endDate, d.contract_no as contractNo, d.status as status,");
        sql.append("d.bill_status as billStatus, d.bus_type as busType, d.agreed_deposit_ori_id as agreedDepositOriId, ");
        sql.append("d.stop_date as stopDate, d.checker_by as checkerBy, d.check_date as checkDate, d.pool_sign as poolSign, ");
        sql.append(" d.is_agreed_deposit_acc as isAgreedDepositAcc, d.retained_Bal as retainedBal,d.agr_Acc as agrAcc ");
        sql.append(" from fc_agreed_deposit d where 1=1 ");
        return sql.toString();
    }
    /**
     * 根据账号和日期查找协定信息
     *
     * @param blnctrAcc
     * @return
     */
    public List<FcAgreedDepositVO> getChoAgreeInfoByAccout(String blnctrAcc,Date queryDate) {
        StringBuilder sql = new StringBuilder(getAgreeSql());
        sql.append(" and d.blnctr_acc =:blnctrAcc ");
        Map<String, Object> params = new HashMap<>();
        /*取单据状态为登记的数据*/
        sql.append(" and d.bill_status = :billStatus and d.status = 95 ");
        sql.append(" and bt_to_char(d.begin_date, 'yyyy-MM-dd') <= :queryDate");
        sql.append(" and bt_to_char(d.end_date, 'yyyy-MM-dd') >= :queryDate");
        sql.append(" and ((d.bus_type = 2 and bt_to_char(d.stop_date, 'yyyy-MM-dd') > :queryDate) or d.stop_date is null)");
        params.put("blnctrAcc", blnctrAcc);
        params.put("billStatus", InnerFinanceConstant.BILL_STATUS_10);
        params.put("queryDate", DateUtils.formatDate(queryDate));
        return listBySQLAliasToBean(sql.toString(), params, FcAgreedDepositVO.class);
    }
    /**
     * 获取审批列表
     *
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput<FcAgreedDepositVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder(getCommonSql());
        sql.append(" and " + businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAgreedDepositVO.class, "", false);
    }
    /**
     * 内部存款-协定存款查询列表
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput getDataTableQuery(DataTablesInput input, ICurrentUser user, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.id as id, d.corp_id as corpId, d.corp_name as corpName, d.acc_id as accId,");
        sql.append("d.acc_name as accName, d.blnctr_acc as blnctrAcc, d.cur_id as curId, d.cur_name as curName,");
        sql.append("d.contract_no as contractNo, d.begin_date as beginDate, d.end_date as oldEndDate,");
        sql.append("e.end_date as endDate,d.bus_type as busType,e.certificate_way as certificateWay");
        sql.append(" from fc_agreed_deposit d left join (select a.id, a.agreed_deposit_ori_id, a.begin_date,");
        sql.append(" a.end_date, a.certificate_way, a.bus_type from fc_agreed_deposit a where a.bus_type = '1' and a.status <> -2) e");
        sql.append(" on d.id = e.agreed_deposit_ori_id ");
        sql.append(" where d.pool_sign = '0' and d.bus_type = '0' and d.status <> -2");
        sql.append(joinSql);
        return getDynamicBySQL(input, sql.toString(), FcAgreedDepositVO.class, "d", false);
    }
    /**
     * 内部存款-协定存款明细查询列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput getAgreedDepositDtlById(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        String id = (String) input.getFormdata().get("id");
        List<String> oriIds = (List) input.getFormdata().get("oriIds");
        String inSql = SqlUtil.getInSql(oriIds, "b.agreed_deposit_ori_id", true);
        sql.append("select d.id as id, d.agreed_deposit_ori_id as agreedDepositOriId,d.bill_no as billNo,");
        sql.append(" d.bus_type as busType, d.begin_date as beginDate,");
        /*结束日期:登记结束日期为结束日期*/
        sql.append(" case when d.bus_type = '0' then d.end_date ");
        /*结束日期:延期/终止 原结束日期为结束日期*/
        sql.append(" else (select a.end_date from fc_agreed_deposit a where a.id = d.agreed_deposit_ori_id) end oldEndDate,");
        /*新结束日期：登记新结束日期为空,延期新结束日期为延期数据的结束日期*/
        sql.append(" case when d.bus_type = '0' then null when d.bus_type = '1' then d.end_date");
        /*新结束日期：终止新结束日期为终止日期*/
        /*取该登记下所有延期/终止单的终止日期最大值：延期单stop_date为空，max会忽略空值，
          结果即终止单的终止日期；用聚合函数保证只返回一行，不依赖 rownum/limit 等方言语法*/
        sql.append(" when d.bus_type = '2' then (select max(a.stop_date) from fc_agreed_deposit a");
        sql.append(" where a.agreed_deposit_ori_id = d.agreed_deposit_ori_id and a.status <> -2) end endDate,");
        sql.append(" d.certificate_way as certificateWay, d.status as status, d.create_date as createDate, d.create_by as createBy");
        sql.append(" from (select a.id, a.bill_no, a.bus_type, a.begin_date, a.end_date, a.certificate_way, a.status,");
        sql.append(" a.create_date, a.create_by, a.agreed_deposit_ori_id");
        sql.append(" from fc_agreed_deposit a where a.id ='" + id + "' and a.bus_type = '0' and a.status <> -2");
        sql.append(" union all ");
        sql.append("select b.id, b.bill_no, b.bus_type, b.begin_date, b.end_date, b.certificate_way, b.status,");
        sql.append(" b.create_date, b.create_by, b.agreed_deposit_ori_id");
        sql.append(" from fc_agreed_deposit b where b.bus_type in ('1','2') and b.status <> -2 and " + inSql + ") d ");
//        sql.append(" order by d.bus_type desc,d.create_date desc");
        return getDynamicBySQL(input, sql.toString(), FcAgreedDepositVO.class, "d", false);
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
     * 查询审批通过的登记和延期数据
     *
     * @param
     * @return
     */
    public List<FcAgreedDepositVO> getPassAgreedInfo() {
        StringBuilder sql = new StringBuilder(getCommonSql());
        sql.append(" and ((d.bus_type = '0' and d.status = 95 and d.bill_status = 10) ");
        sql.append(" or (d.bus_type = '1' and d.status = 95 and d.bill_status = 10))");
        return listBySQLAliasToBean(sql.toString(), null, FcAgreedDepositVO.class);
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
     * 根据协定存款协定账号和日期查找协定信息
     *
     * @param agrAcc
     * @return
     */
    public FcAgreedDepositVO getChoAgreeInfoByAgrAcc(String agrAcc,Date queryDate) {
        StringBuilder sql = new StringBuilder(getCommonSql());
        sql.append(" and d.agr_acc =:agrAcc ");
        Map<String, Object> params = new HashMap<>();
        /*取单据状态为登记的数据*/
        /*取单据状态为登记的数据*/
        sql.append(" and d.bill_status = :billStatus and d.status <> -2 ");
        sql.append(" and bt_to_char(d.begin_date, 'yyyy-MM-dd') <= :queryDate");
        sql.append(" and bt_to_char(d.end_date, 'yyyy-MM-dd') >= :queryDate");
        sql.append(" and ((bus_type = 2 and bt_to_char(d.stop_date, 'yyyy-MM-dd') > :queryDate) or d.stop_date is null)");
        params.put("agrAcc", agrAcc);
        params.put("billStatus", InnerFinanceConstant.BILL_STATUS_10);
        params.put("queryDate", DateUtils.formatDate(queryDate));
        List<FcAgreedDepositVO> vos = listBySQLAliasToBean(sql.toString(), params, FcAgreedDepositVO.class);
        if(EmptyUtils.isEmptyList(vos)){
            return null;
        }
        return vos.get(0);
    }



    /**
     * 根据开户单位,单据类型查找正常状态非资金池内部账号
     *
     * @param corpId
     * @param busType
     * @param accAuthSql
     * @return
     */
    public List<FcAccVO> getBlnctrAccByCorpId(String corpId, String busType, String accAuthSql) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.id as id, f.blnctr_acc as blnctrAcc, f.acc_name as accName, f.cur_id as curId from fc_acc f ");
        sql.append(" where f.acc_state='N' and (f.pool_sign = '0' or f.pool_sign is null ) and f.acc_type = '01'");
        if (busType.equals(InnerFinanceConstant.BUS_TYPE_0)) {
            /*登记查有效的企业活期账号*/
            sql.append(" and f.id not in (select d.acc_id from fc_agreed_deposit d where d.bill_status = 10 and d.status > 0 and d.bus_type = 0)");
            sql.append("and f.id not in (select d.acc_id from fc_agreed_deposit d where d.bill_status = 11");
            sql.append(" and d.status = 95 and d.id in (select a.agreed_deposit_ori_id from fc_agreed_deposit a");
            sql.append(" where a.status < 95 and a.status <>-2 and a.bus_type = '2'))");
        } else if (busType.equals(InnerFinanceConstant.BUS_TYPE_1)) {
            /*延期查登记审批通过的企业活期账号*/
            sql.append(" and f.id in (select d.acc_id from fc_agreed_deposit d where d.pool_sign = 0");
            sql.append(" and ((d.bus_type = '0' and d.status = 95 and d.bill_status = 10)");
            sql.append(" or (d.bus_type = '1' and d.status = 95 and d.bill_status = 10)");
            sql.append(" or (d.bus_type = '1' and d.status in (0,-1) and d.bill_status = 10))");
            sql.append(" and bt_to_char(d.end_date, 'yyyy-MM-dd') >= :endDate)");
        } else {
            /*终止查登记审批通过的企业活期账号或延期审批通过的企业活期账号*/
            sql.append(" and f.id in (select d.acc_id from fc_agreed_deposit d");
            sql.append(" where ((d.bill_status = 10 and d.status = 95 and d.bus_type in ('0', '1')) ");
            sql.append(" or (d.bus_type = '2' and d.status in (0,-1) and d.bill_status = 10 ))");
            sql.append(" and bt_to_char(d.end_date, 'yyyy-MM-dd') >= :endDate)");
        }
        sql.append(" and ").append(accAuthSql);
        if (StringUtils.isNotBlank(corpId)) {
            sql.append(" and f.corp_id = :corpId");
            params.put("corpId", corpId);
        }
        if (InnerFinanceConstant.BUS_TYPE_1.equals(busType) || InnerFinanceConstant.BUS_TYPE_2.equals(busType)) {
            params.put("endDate", DateUtils.formatDate(DateUtils.getDate()));
        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccVO.class);
    }
}
