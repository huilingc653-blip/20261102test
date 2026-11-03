package com.bytter.system.inneraccount.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.vo.FcAccHistoryPoolVO;
import com.bytter.system.api.vo.FcAccHistoryVO;
import com.bytter.system.constants.InnerAccountConstant;
import com.bytter.system.inneraccount.entity.FcAccHistory;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: FC_ACC_HISTORY
 * @author onlineGenerator
 * @date 2017-06-07 14:30:19
 * @version 1.0
 *
 */
@Repository
public class FcAccHistoryDao extends BaseDao<FcAccHistory,String>{
    /**
     * 待提交的可改户申请的数据列表
     * @param dataTablesInput
     * @param sqlWhere
     * @param loginName
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String sqlWhere,String loginName,Map<String,Object> params) {
        StringBuilder sb=new StringBuilder();
        StringBuilder sb1=new StringBuilder();
        StringBuilder sb2=new StringBuilder();
        sb1.append("select t.id as id,t.blnctr_Acc,");
        sb1.append("t.acc_Name,t.net_Id,");
        sb1.append("t.corp_Id,t.cur_Id,net.name as net_name,corp.name as corp_name,cur.cur_name as cur_name,");
        sb1.append("t.subject_Id,t.acc_Type,");
        sb1.append("t.balance_Way,t.acc_State,'' as fcaccId, t.overdraft_sign, t.overdraft_money, ");
        sb1.append("f.subject_name,t.POOL_SIGN");
        sb1.append(" from Fc_Acc t ");
        sb1.append(" LEFT JOIN fc_Subject f  ON t.subject_id = f.id  ");
        sb1.append(" LEFT JOIN sys_corp net  ON t.net_id = net.id ");
        sb1.append(" LEFT JOIN sys_corp corp  ON t.corp_id = corp.id ");
        sb1.append(" LEFT JOIN bt_currency cur  ON t.cur_id = cur.id ");
        sb1.append(" where 1=1  ");
        // 区分是否为资金池
        sb1.append(" and (t.pool_sign <> '1' or t.pool_sign is null)  ");
        String accName = dataTablesInput.getFormdata().get("likes_accName").toString();
        dataTablesInput.getFormdata().put("likes_accName", "");
        if (StringUtils.isNotEmpty(accName)) {
            sb1.append(" and t.acc_Name like '%").append(accName).append("%'");
        }
        sb1.append(sqlWhere);
        sb2.append("select t.id,t.blnctr_Acc,");
        sb2.append("t.acc_Name,t.net_Id,");
        sb2.append("t.corp_Id,t.cur_Id,net.name as net_name,corp.name as corp_name,cur.cur_name as cur_name,");
        sb2.append("t.subject_Id,t.acc_Type,");
        sb2.append("t.balance_Way,t.acc_State,t.fcacc_id as fcaccId, t.overdraft_sign, t.overdraft_money,");
        sb2.append("f.subject_name,h.POOL_SIGN");
        sb2.append(" from Fc_Acc h,Fc_Acc_History t ");
        sb2.append(" LEFT JOIN fc_Subject f  ON t.subject_id = f.id  ");
        sb2.append(" LEFT JOIN sys_corp net  ON t.net_id = net.id ");
        sb2.append(" LEFT JOIN sys_corp corp  ON t.corp_id = corp.id ");
        sb2.append(" LEFT JOIN bt_currency cur  ON t.cur_id = cur.id ");
        sb2.append(" where t.fcacc_id=h.id  ");
        // 区分是否为资金池
        sb2.append(" and (t.pool_sign <> '1' or t.pool_sign is null)  ");
        if (StringUtils.isNotEmpty(accName)) {
            sb2.append(" and t.acc_Name like '%").append(accName).append("%'");
        }
        sb2.append(sqlWhere);
        String status="";
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("accState"))) {
            status=dataTablesInput.getFormdata().get("accState").toString();
        }
        sb.append(" select t.id as id,t.blnctr_Acc as blnctrAcc,t.acc_name as accName, t.net_Id as netId,t.corp_Id as corpId, t.net_Name as netName, t.corp_Name as corpName, t.cur_Name as curName," );
        sb.append(" t.subject_Id as subjectId,t.acc_Type as accType,t.balance_Way as balance_Way,t.cur_Id as curId,");
        sb.append(" t.acc_State as accState,t.fcaccId as fcaccId, t.overdraft_sign as overdraftSign, t.overdraft_money as overdraftMoney, t.subject_Name as subjectName,t.pool_Sign as poolSign from (");
        if(InnerAccountConstant.ACC_STATE_NORMAL.equals(status)) {
            sb.append(sb1).append(" and t.acc_State=:accState");
            params.put("accState",InnerAccountConstant.ACC_STATE_NORMAL);
        }else if(InnerAccountConstant.ACC_STATE_G.equals(status) || InnerAccountConstant.ACC_STATE_F.equals(status)){
            sb.append(sb2).append(" and t.acc_State in (:accState) and t.create_by =:loginName");
            params.put("accState",new String[]{InnerAccountConstant.ACC_STATE_G,InnerAccountConstant.ACC_STATE_F});
            params.put("loginName",loginName);
        }else{
            sb.append(sb1).append(" and t.acc_State=:accStateN")
                    .append(" union ").append(sb2).append( " and t.acc_State in (:accState) and t.create_by =:loginName ");
            params.put("accStateN",InnerAccountConstant.ACC_STATE_NORMAL);
            params.put("accState",new String[]{InnerAccountConstant.ACC_STATE_G,InnerAccountConstant.ACC_STATE_F});
            params.put("loginName",loginName);
        }
        sb.append(" ) t where 1=1 ");
        DataTablesOutput dtop= getDynamicBySQL(dataTablesInput, sb.toString(), FcAccHistoryVO.class, "", false,params);
        return dtop;
    }
    /**
     * 待提交的可改户申请的数据列表
     * @param dataTablesInput
     * @param sqlWhere
     * @param loginName
     * @return
     */
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String sqlWhere,String loginName,Map<String,Object> params) {
        StringBuilder sb=new StringBuilder();
        StringBuilder sb1=new StringBuilder();
        StringBuilder sb2=new StringBuilder();
        sb1.append("select t.id as id,t.blnctr_Acc as blnctrAcc,");
        sb1.append("t.acc_Name as accName,t.net_Id as netId,");
        sb1.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sb1.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sb1.append("t.balance_Way as balanceWay,t.acc_State as accState,'' as fcaccId, t.overdraft_sign as overdraftSign, t.overdraft_money as overdraftMoney, ");
        sb1.append("f.subject_name as subjectName,t.POOL_SIGN as poolSign");
        sb1.append(" from Fc_Acc t, fc_Subject_pool f where t.subject_id=f.id ");
        // 区分是否为资金池
        sb1.append(" and t.pool_sign='1' ");
        sb1.append(sqlWhere);
        sb2.append("select t.id as id,t.blnctr_Acc as blnctrAcc,");
        sb2.append("t.acc_Name as accName,t.net_Id as netId,");
        sb2.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sb2.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sb2.append("t.balance_Way as balanceWay,t.acc_State as accState,t.fcacc_id as fcaccId, t.overdraft_sign as overdraftSign, t.overdraft_money as overdraftMoney, ");
        sb2.append("f.subject_name as subjectName,h.POOL_SIGN as poolSign");
        sb2.append(" from Fc_Acc_History t,Fc_Acc h,fc_Subject_pool f where t.subject_id=f.id and t.fcacc_id=h.id ");
        // 区分是否为资金池
        sb2.append(" and t.pool_sign='1' ");
        sb2.append(sqlWhere);
        String status="";
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("accState"))) {
            status=dataTablesInput.getFormdata().get("accState").toString();
        }
        sb.append(" select s.id as id,s.blnctrAcc as blnctrAcc,s.accName as accName, s.netId as netId,s.corpId as corpId," );
        sb.append(" s.subjectId as subjectId,s.accType as accType,s.balanceWay as balanceWay,s.curId as curId,");
        sb.append(" s.accState as accState,s.fcaccId as fcaccId, s.overdraftSign as overdraftSign, s.overdraftMoney as overdraftMoney,s.subjectName as subjectName,s.poolSign as poolSign from (");
        if(InnerAccountConstant.ACC_STATE_NORMAL.equals(status)) {
            sb.append(sb1).append(" and t.acc_State=:accState");
            params.put("accState",InnerAccountConstant.ACC_STATE_NORMAL);
        }else if(InnerAccountConstant.ACC_STATE_G.equals(status) || InnerAccountConstant.ACC_STATE_F.equals(status)){
            sb.append(sb2).append(" and t.acc_State in (:accState) and t.create_by =:loginName");
            params.put("accState",new String[]{InnerAccountConstant.ACC_STATE_G,InnerAccountConstant.ACC_STATE_F});
            params.put("loginName",loginName);
        }else{
            sb.append(sb1).append(" and t.acc_State=:accStateN")
                    .append(" union ").append(sb2).append( " and t.acc_State in (:accState) and t.create_by =:loginName ");
            params.put("accStateN",InnerAccountConstant.ACC_STATE_NORMAL);
            params.put("accState",new String[]{InnerAccountConstant.ACC_STATE_G,InnerAccountConstant.ACC_STATE_F});
            params.put("loginName",loginName);
        }
        sb.append(" ) s where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sb.toString(), FcAccHistoryPoolVO.class, "", false,params);
    }
    /**
     * 复核数据列表
     * @param dataTablesInput
     * @param sqlWhere
     * @param loginName
     * @return
     */
    public DataTablesOutput getCheckDataTable(DataTablesInput dataTablesInput, String sqlWhere,String loginName,Map<String,Object> params) {
        StringBuilder sb=new StringBuilder();
        sb.append("select t.id as id,t.blnctr_Acc as blnctrAcc,");
        sb.append("t.acc_Name as accName,t.net_Id as netId,net.name AS netName,");
        sb.append("t.corp_Id as corpId,corp.name AS corpName,t.cur_Id as curId,cur.cur_name AS curName,");
        sb.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sb.append("t.balance_Way as balanceWay,t.acc_State as accState,t.fcacc_id as fcaccId,");
        sb.append("f.subject_name as subjectName");
        sb.append(" from Fc_Acc_History t ");
        sb.append(" LEFT JOIN fc_Subject f ON t.subject_id=f.id");
        sb.append(" LEFT JOIN sys_corp net ON t.net_id=net.id ");
        sb.append(" LEFT JOIN sys_corp corp ON t.corp_id=corp.id ");
        sb.append(" LEFT JOIN bt_currency cur ON t.cur_id=cur.id ");
        sb.append(" where t.acc_State='E' and t.create_by !=:loginName");
        // 区分是否为资金池
        sb.append(" and (t.pool_sign <> '1' or t.pool_sign is null) ");
        sb.append(sqlWhere);
        params.put("loginName",loginName);
        return getDynamicBySQL(dataTablesInput, sb.toString(), FcAccHistoryVO.class, "", false,params);
    }
    /**
     * 复核数据列表
     * @param dataTablesInput
     * @param sqlWhere
     * @param loginName
     * @return
     */
    public DataTablesOutput getCheckDataTableForPool(DataTablesInput dataTablesInput, String sqlWhere,String loginName,Map<String,Object> params) {
        StringBuilder sb=new StringBuilder();
        sb.append(" select s.id as id,s.blnctrAcc as blnctrAcc,s.accName as accName, s.netId as netId,s.corpId as corpId," );
        sb.append(" s.subjectId as subjectId,s.accType as accType,s.balanceWay as balanceWay,s.curId as curId,");
        sb.append(" s.accState as accState,s.fcaccId as fcaccId,s.subjectName as subjectName from (");
        sb.append("select t.id as id,t.blnctr_Acc as blnctrAcc,");
        sb.append("t.acc_Name as accName,t.net_Id as netId,");
        sb.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sb.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sb.append("t.balance_Way as balanceWay,t.acc_State as accState,t.fcacc_id as fcaccId,");
        sb.append("f.subject_name as subjectName");
        sb.append(" from Fc_Acc_History t,fc_Subject_pool f where t.subject_id=f.id and t.acc_State='E' ");
        sb.append(" and t.create_by !=:loginName");
        // 区分是否为资金池
        sb.append(" and t.pool_sign='1' ");
        sb.append(sqlWhere);
        sb.append(" ) s where 1=1 ");
        params.put("loginName",loginName);
        return getDynamicBySQL(dataTablesInput, sb.toString(), FcAccHistoryPoolVO.class, "", false,params);
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonUnion());
        sql.append(" where t.id=:id ");
        Map map = new HashMap();
        map.put("id", id);
        return getBySQLAliasToBean(sql.toString(), map, FcAccHistoryVO.class);
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOByIdForPool(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonUnionForPool());
        sql.append(" where t.id=:id ");
        Map map = new HashMap();
        map.put("id", id);
        return getBySQLAliasToBean(sql.toString(), map, FcAccHistoryVO.class);
    }
    /**
     * 内部账号表All
     * @return
     */
    public String getCommonUnion(){
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.update_Date as updateDate,t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,net.name AS netName,");
        sql.append("t.corp_Id as corpId,corp.name AS corpName,t.cur_Id as curId,cur.cur_name AS curName,");
        sql.append("t.subject_Id as subjectId,f.sys_subject_code as sysSubjectCode,f.subject_name as subjectName,f.subject_name as subjectPoolName,t.acc_Type as accType,");
        sql.append("t.attribute_Id as attributeId,t.nature_Code_Id as natureCodeId,");
        sql.append("a.ATTRIBUTE_NAME as attributeName,n.NATURE_NAME as natureName,");
        sql.append("t.bank_Code as bankCode,t.Delay_Rate as DelayRate,");
        sql.append("t.balance_Way as balanceWay,t.interest_Sign as interestSign,");
        sql.append("t.interest_Start_Date as interestStartDate,t.interest_Rate as interestRate,");
        sql.append("t.overdraft_Sign as overdraftSign,");
        sql.append("t.overdraft_Rate as overdraftRate,t.overdraft_Money as overdraftMoney,");
        sql.append("t.contract_No as contractNo,t.deadline_Date as deadlineDate,");
        sql.append("t.delay_Deadline_Date as delayDeadlineDate,t.overdue_Rate as overdueRate,");
        sql.append("t.acc_For_Deduct as accForDeduct,t.deduct_Mode as deductMode,");
        sql.append("t.notify_Type as notifyType,t.acc_State as accState,");
        sql.append("t.agreed_Deposit_Sign as agreedDepositSign,t.predraw_Sign as predrawSign,");
        sql.append("t.if_Compare as ifCompare,t.acc_Id as accId,");
        sql.append("t.acc_Start_Date as accStartDate,t.operator_Code as operatorCode,");
        sql.append("t.acc_Open_Time as accOpenTime,t.handle_Charge as handleCharge,");
        sql.append("t.shortcut as shortcut,t.union_Net as unionNet,");
        sql.append("t.union_Acc as unionAcc,t.precation_Acc as precationAcc,");
        sql.append("t.precation_Corp as precationCorp,t.poundage_Rate as poundageRate,");
        sql.append("t.charge_Acc as chargeAcc,t.transact_Way as transactWay,");
        sql.append("t.valid_Sign as validSign,t.check_Back_Reason as checkBackReason,");
        sql.append("t.end_Unit as endUnit,t.certificate_Way as certificateWay,");
        sql.append("t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,");
        sql.append("t.reference_rate_type as referenceRateType,");
        sql.append("t.acc_Cussent as accCussent,t.fcacc_id as fcaccId,t.pool_sign as poolSign,t.RELATION_ACC as relationAcc,t.rmk as rmk, ");
        sql.append("t.interest_rec_pay as interestRecPay,t.interest_in_exp as interestInExp ");
        sql.append(" from Fc_Acc_history t ");
        sql.append(" left join fc_Subject f on t.subject_id=f.id ");
        sql.append(" left join BT_ACC_NATURE n on t.nature_Code_Id=n.id ");
        sql.append(" left join BIS_ACC_ATTRIBUTE a on t.attribute_Id=a.id ");
        sql.append(" LEFT JOIN sys_corp net ON t.net_id=net.id ");
        sql.append(" LEFT JOIN sys_corp corp ON t.corp_id=corp.id ");
        sql.append(" LEFT JOIN bt_currency cur ON t.cur_id=cur.id ");
        return sql.toString();
    }
    /**
     * 内部账号表All
     * @return
     */
    public String getCommonUnionForPool(){
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.update_Date as updateDate,t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,net.pool_name as netPoolName,");
        sql.append("t.corp_Id as corpId,corp.code corpCode,corp.name corpName,t.cur_Id as curId,cur.cur_name AS curName,");
        sql.append("t.subject_Id as subjectPoolId,f.subject_name as subjectName,f.subject_name as subjectPoolName,t.acc_Type as accType,");
        sql.append("t.attribute_Id as attributeId,t.nature_Code_Id as natureCodeId,");
        sql.append("a.ATTRIBUTE_NAME as attributeName,n.NATURE_NAME as natureName,");
        sql.append("t.bank_Code as bankCode,bi.bank_name as bankName,t.Delay_Rate as DelayRate,");
        sql.append("t.balance_Way as balanceWay,t.interest_Sign as interestSign,");
        sql.append("t.interest_Start_Date as interestStartDate,t.interest_Rate as interestRate,");
        sql.append("t.overdraft_Sign as overdraftSign,");
        sql.append("t.overdraft_Rate as overdraftRate,t.overdraft_Money as overdraftMoney,");
        sql.append("t.contract_No as contractNo,t.deadline_Date as deadlineDate,");
        sql.append("t.delay_Deadline_Date as delayDeadlineDate,t.overdue_Rate as overdueRate,");
        sql.append("t.acc_For_Deduct as accForDeduct,t.deduct_Mode as deductMode,");
        sql.append("t.notify_Type as notifyType,t.acc_State as accState,");
        sql.append("t.agreed_Deposit_Sign as agreedDepositSign,t.predraw_Sign as predrawSign,");
        sql.append("t.if_Compare as ifCompare,t.acc_Id as accId,");
        sql.append("t.acc_Start_Date as accStartDate,t.operator_Code as operatorCode,");
        sql.append("t.acc_Open_Time as accOpenTime,t.handle_Charge as handleCharge,");
        sql.append("t.shortcut as shortcut,t.union_Net as unionNet,");
        sql.append("t.union_Acc as unionAcc,t.precation_Acc as precationAcc,");
        sql.append("t.precation_Corp as precationCorp,t.poundage_Rate as poundageRate,");
        sql.append("t.charge_Acc as chargeAcc,t.transact_Way as transactWay,");
        sql.append("t.valid_Sign as validSign,t.check_Back_Reason as checkBackReason,");
        sql.append("t.end_Unit as endUnit,t.certificate_Way as certificateWay,");
        sql.append("t.reference_rate_type as referenceRateType,");
        sql.append("t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,");
        sql.append("t.acc_Cussent as accCussent,t.fcacc_id as fcaccId,t.pool_sign as poolSign ");
        sql.append(" from Fc_Acc_history t ");
        sql.append(" left join fc_Subject_pool f on t.subject_id=f.id ");
        sql.append(" left join BT_ACC_NATURE n on t.nature_Code_Id=n.id ");
        sql.append(" left join BIS_ACC_ATTRIBUTE a on t.attribute_Id=a.id ");
        sql.append(" LEFT JOIN fc_corp_pool net ON t.net_id=net.id ");
        sql.append(" LEFT JOIN sys_corp corp ON t.corp_id=corp.id ");
        sql.append(" left join bt_currency cur ON t.cur_id=cur.id ");
        sql.append(" LEFT JOIN bt_input_bank_info bi ON t.bank_code = bi.sys_bank_code ");
        return sql.toString();
    }
    /**
     * 根据账号查询历史账户对象
     *
     * @param blnctrAcc
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOByBlnctrAcc(String blnctrAcc) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonUnion());
        sql.append(" where t.blnctr_acc=:blnctrAcc ");
        sql.append(" order by t.create_date desc,t.update_date desc ");
        Map map = new HashMap();
        map.put("blnctrAcc", blnctrAcc);
        List<FcAccHistoryVO> list = listBySQLAliasToBean(sql.toString(), map, FcAccHistoryVO.class);
        if(EmptyUtils.isEmptyList(list)){
            return null;
        }
        return list.get(0);
    }
    /**
     * 根据账号信息查找计息账号列表
     * @param vo
     * @return
     */
    public List<FcAccHistoryVO> getAccListByExpPay(FcAccHistoryVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonUnion());
        Map<String,Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(vo.getBlnctrAcc())) {
            sql.append(" where (t.interest_in_exp =:blnctrAcc or t.interest_rec_pay =:blnctrAcc)");
            params.put("blnctrAcc", vo.getBlnctrAcc());
        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccHistoryVO.class);
    }
    /**
     * 根据账号查询历史账户对象
     *
     * @param vo
     * @return
     */
    public FcAccHistoryVO getFcAccHistoryVO(FcAccHistoryVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonUnion());
        Map map = new HashMap();
        sql.append(" where 1=1 ");
        if(StringUtils.isNotEmpty(vo.getBlnctrAcc())){
            sql.append(" and t.blnctr_acc=:blnctrAcc ");
            map.put("blnctrAcc", vo.getBlnctrAcc());
        }
        sql.append(" order by t.create_date desc,t.update_date desc ");
        List<FcAccHistoryVO> list = listBySQLAliasToBean(sql.toString(), map, FcAccHistoryVO.class);
        if(EmptyUtils.isEmptyList(list)){
            return null;
        }
        return list.get(0);
    }
}
