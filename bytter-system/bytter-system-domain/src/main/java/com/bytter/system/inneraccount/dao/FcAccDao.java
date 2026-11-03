package com.bytter.system.inneraccount.dao;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.InnerAccountConstant;
import com.bytter.core.constant.SubjectConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.FcAccListVO;
import com.bytter.system.api.vo.FcAccPlusVO;
import com.bytter.system.api.vo.FcAccVO;
import com.bytter.system.inneraccount.entity.FcAcc;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 内部账户信息表
 * @date 2017-05-25 19:36:12
 */
@Repository
public class FcAccDao extends BaseDao<FcAcc, String> {
    /**
     * 查找该网点的、币别的所有账户
     *
     * @param netId
     * @param curId
     * @param accType
     * @return
     */
    public List<FcAcc> queryMultiAccAll(String netId, String curId, String accType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accType", accType);
        params.put("netId", netId);
        params.put("curId", curId);
        List<FcAcc> list = listByHQLAliasToBean(" select t.blnctrAcc as blnctrAcc from FcAcc t" + " where t.accType=:accType and t.netId=:netId and t.curId=:curId", params, FcAcc.class);
        return list;
    }
    public List<FcAcc> listByBlnctrAcc(String blnctrAcc) {
        List<FcAcc> list = listBy("blnctrAcc", blnctrAcc);
        return list;
    }
    /**
     * 根据账号查对象
     *
     * @param blnctrAcc
     * @return
     */
    public FcAcc findFcAccByBlnctrAcc(String blnctrAcc) {
        return getUniqueBy("blnctrAcc", blnctrAcc);
    }
    public FcAcc getAccDTOByAccount(String account) {
        return getUniqueBy("blnctrAcc", account);
    }
    /**
     * 更改帐户利率
     *
     * @param blnctrAcc
     * @param colName
     * @param instrate
     */
    public void updateRate(String blnctrAcc, String colName, BigDecimal instrate) {
        String hql = "update FcAcc set " + colName + "=:interestRate where blnctrAcc=:blnctrAcc";
        this.getSession().createQuery(hql).setBigDecimal("interestRate", instrate).setString("blnctrAcc", blnctrAcc).executeUpdate();
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccVO findFcAccById(String id) {
        String sql = getCommonFcAccUnion() + " where t.id=:id ";
        Map map = new HashMap();
        map.put("id", id);
        return getBySQLAliasToBean(sql, map, FcAccVO.class);
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccVO findFcAccByIdForPool(String id) {
        String sql = getCommonFcAccUnionForPool() + " where t.id=:id ";
        Map map = new HashMap();
        map.put("id", id);
        return getBySQLAliasToBean(sql, map, FcAccVO.class);
    }
    /**
     * 判断账户是否被其它账户引用
     *
     * @param blnctrAcc
     * @return
     */
    public boolean isOtherAccountQuote(String blnctrAcc) {
        String hql = "from FcAcc t where (t.accForDeduct='" + blnctrAcc + "' or t.chargeAcc='" + blnctrAcc + "') and t.blnctrAcc!='" + blnctrAcc + "' and t.accState<>'D'";
        List list = this.listByHQL(hql);
        return list != null && !list.isEmpty();
    }
    /**
     * 查询存在放款帐号对应的应收预期利息余额>0
     *
     * @param blnctrAcc
     * @return
     */
    public boolean isExistInterestPayable(String blnctrAcc) {
        String sql = "select count(1) as CN from Fc_Acc t, fc_subject f where t.acc_State = 'N' and t.acc_Type = :accType" + " and f.sys_Subject_Code = :sysSubjectCode and t.acc_For_Deduct = :blnctrAcc";
        Map<String, Object> params = new HashMap<>();
        params.put("blnctrAcc", blnctrAcc);
        params.put("sysSubjectCode", SubjectConstant.SysSubject.INTEREST_PAYABLE_OWE_INNERACC.get());
        params.put("accType", InnerAccountConstant.FCACC_INNER_ACCOUNT);
        Map rs = getBySQL(sql, params);
        Integer count = new Integer(rs.get("CN").toString());
        return count == null || count <= 0;
    }
    /**
     * 注销转出账号,单位企业活期账号，不包含自己的活期账号
     *
     * @param blnctrAcc
     * @param netId
     * @return
     */
    public List<FcAccVO> transferOutAcc(String blnctrAcc, String netId) {
        String sql = getCommonFcAccUnion() + " where 1=1  and t.acc_State='N' " + " and t.acc_Type='01' and t.net_Id=:netId  " + " and (t.subject_id in (select fs.id from Fc_Subject fs where fs.sys_Subject_Code=:sysSubjectCode)) " +//现金
                " and t.blnctr_Acc!=:blnctrAcc" + " and t.cur_id in (select cur_id from fc_acc where blnctr_acc =:blnctrAcc)";
        Map map = new HashMap();
        map.put("netId", netId);
        map.put("sysSubjectCode", SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue());
        map.put("blnctrAcc", blnctrAcc);
        return listBySQLAliasToBean(sql, map, FcAccVO.class);
    }
    /**
     * 获取FcAccVO
     *
     * @param netId
     * @param corpId
     * @param subjectId
     * @param curId
     * @return
     */
    public FcAccPlusVO getFcAccVOByParams(String netId, String corpId, String subjectId, String curId, String sqlUnion) {
        List<FcAccPlusVO> list = this.getAccountByParams(curId, netId, corpId, subjectId, sqlUnion, null, null);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 根据账号查对象
     *
     * @param blnctrAcc
     * @return
     */
    public FcAccVO getFcAccVOByBlnctrAcc(String blnctrAcc) {
        String sql = getCommonFcAccUnion() + " where t.blnctr_Acc=:blnctrAcc ";
        Map map = new HashMap();
        map.put("blnctrAcc", blnctrAcc);
        return getBySQLAliasToBean(sql, map, FcAccVO.class);
    }
    /**
     * 根据条件查对象
     *
     * @param fcAcc
     * @return
     */
    public List<FcAccVO> findFcAcc(FcAccVO fcAcc, String sql, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(getCommonFcAccUnion());
        sb.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(sql)) {
            sb.append(sql);
        }
        if (StringUtils.isNotEmpty(fcAcc.getAccState())) {
            sb.append(" and t.acc_State =:acc_State");
            params.put("acc_State", fcAcc.getAccState());
        }
        if (StringUtils.isNotEmpty(fcAcc.getNetId())) {
            sb.append(" and t.net_id =:netId");
            params.put("netId", fcAcc.getNetId());
        }
        if (StringUtils.isNotEmpty(fcAcc.getCorpId())) {
            String[] corpIdS = fcAcc.getCorpId().split(",");
            sb.append(" and t.corp_id in (:corpId)");
            params.put("corpId", corpIdS);
        }
        if (StringUtils.isNotEmpty(fcAcc.getSubjectId())) {
            sb.append(" and t.subject_Id =:subjectId");
            params.put("subjectId", fcAcc.getSubjectId());
        }
        if (StringUtils.isNotEmpty(fcAcc.getCurId())) {
            sb.append(" and t.cur_id =:curId");
            params.put("curId", fcAcc.getCurId());
        }
        if (StringUtils.isNotEmpty(fcAcc.getAccType())) {
            if("!14".equals(fcAcc.getAccType())){
                sb.append(" and t.acc_type !=:accType");
                params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);
            }else {
                sb.append(" and t.acc_type =:accType");
                params.put("accType", fcAcc.getAccType());
            }
        }
        if (StringUtils.isNotEmpty(fcAcc.getPoolSign())) {
            sb.append(" and t.pool_sign =:poolSign");
            params.put("poolSign", fcAcc.getPoolSign());
        }
        if (StringUtils.isNotEmpty(fcAcc.getBlnctrAcc())) {
            sb.append(" and t.blnctr_Acc like :blnctrAcc");
            params.put("blnctrAcc", "%" + fcAcc.getBlnctrAcc() + "%");
        }
        return listBySQLAliasToBean(sb.toString(), params, FcAccVO.class);
    }
    /**
     * 根据条件查对象
     *
     * @param fcAcc
     * @param sql
     * @return
     */
    public List<FcAccVO> findFcAcc(FcAcc fcAcc, String sql) {
        StringBuilder sb = new StringBuilder();
        sb.append("select a.blnctr_acc as blnctrAcc,a.interest_Rate as interestRate,a.interest_start_date as interestStartDate,a.ACC_START_DATE as accStartDate,");
        sb.append("a.acc_name as accName,a.Overdue_Rate as OverdueRate,a.Overdraft_Rate as OverdraftRate,a.Delay_Rate as DelayRate,a.acc_type as accType ");
        sb.append(" from Fc_Acc a where a.acc_State<>'D' and a.acc_State<>'W' and a.acc_State<>'R' and a.acc_State<>'T' and a.acc_State<>'B'");
        sb.append("and a.interest_Sign='1'");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(fcAcc.getNetId())) {
            sb.append(" and a.net_id =:netId");
            params.put("netId", fcAcc.getNetId());
        }
        if (StringUtils.isNotEmpty(fcAcc.getCorpId())) {
            sb.append(" and a.corp_id =:corpId");
            params.put("corpId", fcAcc.getCorpId());
        }
        if (StringUtils.isNotEmpty(fcAcc.getSubjectId())) {
            sb.append(" and a.subject_Id =:subjectId");
            params.put("subjectId", fcAcc.getSubjectId());
        }
        if (StringUtils.isNotEmpty(fcAcc.getCurId())) {
            sb.append(" and a.cur_id =:curId");
            params.put("curId", fcAcc.getCurId());
        }
        if (StringUtils.isNotEmpty(fcAcc.getAccType())) {
            sb.append(" and a.acc_type =:accType");
            params.put("accType", fcAcc.getAccType());
        }
        if (StringUtils.isNotEmpty(fcAcc.getBlnctrAcc())) {
            sb.append(" and a.blnctr_Acc like :blnctrAcc");
            params.put("blnctrAcc", "%" + fcAcc.getBlnctrAcc() + "%");
        }
        if (StringUtils.isNotEmpty(sql)) {
            sb.append(sql);
        }
        return listBySQLAliasToBean(sb.toString(), params, FcAccVO.class);
    }
    /**
     * 内部账号表All
     *
     * @return
     */
    public String getCommonFcAccUnion() {
        StringBuilder sb = new StringBuilder();
        sb.append( "select t.id as id, t.update_Date as updateDate,t.blnctr_Acc as blnctrAcc, t.acc_Name as accName,t.net_Id as netId,sn.name netName," );
        sb.append( "t.corp_Id as corpId,sc.code corpCode,sc.name corpName, t.cur_Id as curId,cur.cur_name AS curName, ");
        sb.append( "t.subject_Id as subjectId,f.subject_code as subjectCode,f.sys_subject_code as sysSubjectCode,f.subject_name as subjectName," );
        sb.append( "t.acc_Type as accType,t.attribute_Id as attributeId,t.nature_Code_Id as natureCodeId," );
        sb.append( "a.ATTRIBUTE_NAME as attributeName,n.NATURE_NAME as natureName, t.bank_Code as bankCode, bi.bank_name as bankName,t.Delay_Rate as DelayRate,");
        sb.append( "t.balance_Way as balanceWay,t.interest_Sign as interestSign," );
        sb.append( "t.interest_Start_Date as interestStartDate,t.interest_Rate as interestRate, t.overdraft_Sign as overdraftSign," );
        sb.append( "t.overdraft_Rate as overdraftRate,t.overdraft_Money as overdraftMoney, t.contract_No as contractNo,t.deadline_Date as deadlineDate,");
        sb.append( "t.delay_Deadline_Date as delayDeadlineDate,t.overdue_Rate as overdueRate, t.acc_For_Deduct as accForDeduct,t.deduct_Mode as deductMode," );
        sb.append( "t.notify_Type as notifyType,t.acc_State as accState, t.agreed_Deposit_Sign as agreedDepositSign,t.predraw_Sign as predrawSign," );
        sb.append( "t.if_Compare as ifCompare,t.acc_Id as accId, t.acc_Start_Date as accStartDate,t.operator_Code as operatorCode,");
        sb.append( "t.acc_Open_Time as accOpenTime,t.handle_Charge as handleCharge, t.shortcut as shortcut,t.union_Net as unionNet," );
        sb.append( "t.union_Acc as unionAcc,t.precation_Acc as precationAcc, t.precation_Corp as precationCorp,t.poundage_Rate as poundageRate," );
        sb.append( "t.charge_Acc as chargeAcc,t.transact_Way as transactWay, t.valid_Sign as validSign,t.check_Back_Reason as checkBackReason," );
        sb.append( "t.end_Unit as endUnit,t.certificate_Way as certificateWay,t.check_time as checkTime," );
        sb.append( "t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,t.checker_code as checkerCode," );
        sb.append( "t.acc_Cussent as accCussent,f.measure_Unit as measureUnit, t.pool_Sign as poolSign,t.RELATION_ACC as relationAcc,t.rmk as rmk, ");
        sb.append( "t.interest_in_exp as interestInExp,t.interest_rec_pay as interestRecPay, " );
        sb.append( "t.reference_Rate_Type as referenceRateType");
        sb.append( " from Fc_Acc t ");
        sb.append(" left join bt_currency cur  ON t.cur_id = cur.id ");
        sb.append( " LEFT JOIN sys_corp sn ON sn.id=t.net_id ");
        sb.append( " LEFT JOIN sys_corp sc ON sc.id=t.corp_id ");
        sb.append( " left join BT_ACC_NATURE n on t.nature_Code_Id=n.id ");
        sb.append( " left join BIS_ACC_ATTRIBUTE a on t.attribute_Id=a.id " );
        sb.append( " left join fc_subject f on t.subject_id = f.id ");
        sb.append(" LEFT JOIN bt_input_bank_info bi ON t.bank_code = bi.sys_bank_code ");
        return sb.toString();
    }
    /**
     * 开户
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String sqlWhere, Map<String, Object> params) {
        String sql = "SELECT t.id AS id,t.blnctr_Acc AS blnctrAcc," + "t.acc_Name AS accName,t.net_Id AS netId," +
                "t.corp_Id AS corpId,t.cur_Id AS curId," +
                "t.subject_Id AS subjectId,t.acc_Type AS accType," +
                "t.balance_Way AS balanceWay,corp.name AS corpName,t.acc_open_time AS accOpenTime,t.acc_start_date AS accStartDate," +
                "t.interest_start_date AS interestStartDate,t.deadline_date AS deadlineDate,net.name AS netName,t.acc_State AS accState," +
                "t.pool_sign as poolSign,f.subject_name AS subjectName,cur.cur_name AS curName" +
                " from Fc_Acc t " +
                " LEFT JOIN fc_Subject f ON t.subject_id=f.id" +
                " LEFT JOIN sys_corp net ON t.net_id=net.id " +
                " LEFT JOIN sys_corp corp ON t.corp_id=corp.id " +
                " LEFT JOIN bt_currency cur ON t.cur_id=cur.id " +
                sqlWhere +
                " and (t.pool_sign <> '1' or t.pool_sign is null) ";
        return getDynamicBySQL(dataTablesInput, sql, FcAccListVO.class, "t", false, params);
    }
    /**
     * 开户
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String sqlWhere, Map<String, Object> params) {
        String sql = "select t.id as id," + "t.blnctr_Acc as blnctrAcc," + "t.acc_Name as accName,t.net_Id as netPoolId," + "t.corp_Id as corpId,t.cur_Id as curId," + "t.subject_Id as subjectPoolId,t.acc_Type as accType," + "t.balance_Way as balanceWay,t.acc_State as accState," + "f.subject_name as subjectName,t.pool_sign as poolSign" + " from Fc_Acc t left join fc_Subject_pool f on t.subject_id=f.id " + sqlWhere + " and t.pool_sign='1'";
        return getDynamicBySQL(dataTablesInput, sql, FcAccVO.class, "t", false, params);
    }
    /**
     * 复核通过或者失败
     *
     * @param fcAcc
     * @param accState    复核通过  ,复核打回
     * @param oldAccState 复核前的状态
     * @return
     */
    public int check(FcAcc fcAcc, String accState, String oldAccState) {
        String hql = " update FcAcc set checkerCode=:checkerCode ,checkTime =:checkTime,checkBackReason=:checkBackReason ,accState =:accStateNew where id=:id and accState=:oldAccState";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("checkerCode", fcAcc.getCheckerCode());
        params.put("checkTime", fcAcc.getCheckTime());
        params.put("checkBackReason", fcAcc.getCheckBackReason());
        params.put("accStateNew", accState);
        params.put("id", fcAcc.getId());
        params.put("oldAccState", oldAccState);
        return getSession().createQuery(hql).setProperties(params).executeUpdate();
    }
    /**
     * 功能介绍 : 获取有效的内部账户。
     * 以后扩展为有权限的内部账户<br>
     * <b>日期：</b>2017年6月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccPlusVO> getValidAccount(String userCode) {
        return getValidAccount(null, null, null, userCode);
    }
    /**
     * 功能介绍 : 获取有效的内部账户。
     *
     * @param curId   币别
     * @param netId   网点
     * @param netId   单位
     * @param joinSql 权限sql
     * @return
     */
    public List<FcAccPlusVO> getValidAccount(String curId, String netId, String corpId, String joinSql) {
        return getAccountByParams(curId, netId, corpId, null, joinSql, null, null);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param subjectId
     * @param joinSql        权限sql
     * @param sysSubjectCode
     * @return
     */
    public List<FcAccPlusVO> getAccountByParams(String curId, String netId, String corpId, String subjectId, String joinSql, String sysSubjectCode, String bankCode) {
        return getAccountByParams(curId, netId, corpId, subjectId, null, joinSql, sysSubjectCode, bankCode, false);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param subjectId
     * @param joinSql        权限sql
     * @param sysSubjectCode
     * @return
     */
    public List<FcAccPlusVO> getAccountByParamsForPool(String curId, String netId, String corpId, String subjectId, String joinSql, String sysSubjectCode, String bankCode) {
        return getAccountByParamsForPool(curId, netId, corpId, subjectId, null, joinSql, sysSubjectCode, bankCode);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @param joinSql        权限sql
     * @return
     */
    public List<FcAccPlusVO> getAccountByParams(String curId, String netId, String corpId, String subjectId, String endUnit, String joinSql, String sysSubjectCode, String bankCode, Boolean isHavDel) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sql.append("t.balance_Way as balanceWay,corp.name as corpName,t.acc_open_time as accOpenTime,t.acc_start_date as accStartDate, ");
        sql.append("t.interest_start_date as interestStartDate,t.deadline_date as deadlineDate,net.name as netName,t.acc_State as accState,");
        sql.append("f.subject_name as subjectName,cur.cur_name as curName ");
        sql.append(" from Fc_Acc t left join fc_Subject f on t.subject_id=f.id ");
        sql.append(" left join sys_corp net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
        sql.append(" left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        if (isHavDel) {
            sql.append(" and t.acc_state in('N','P','D')");
        } else {
            sql.append(" and t.acc_state in('N','P')");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id=:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id=:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(endUnit)) {
            sql.append(" and t.end_unit=:endUnit");
            params.put("endUnit", endUnit);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_Id=:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            sql.append(" and t.Subject_id=:subjectId");
            params.put("subjectId", subjectId);
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.sys_Subject_Code = :sysSubjectCode");
            params.put("sysSubjectCode", sysSubjectCode);
        }
        if (StringUtils.isNotEmpty(bankCode)) {
            sql.append(" and t.bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        sql.append(" order by t.blnctr_Acc asc ,t.ACC_OPEN_TIME desc,t.CREATE_DATE ");
        return listBySQLAliasToBean(sql.toString(), params, FcAccPlusVO.class);
    }
    /**
     * 根据参数筛选 内部账号（资金池）
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @param joinSql        权限sql
     * @return
     */
    public List<FcAccPlusVO> getAccountByParamsForPool(String curId, String netId, String corpId, String subjectId, String endUnit, String joinSql, String sysSubjectCode, String bankCode) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sql.append("t.balance_Way as balanceWay,corp.name as corpName,t.acc_open_time as accOpenTime,t.acc_start_date as accStartDate, ");
        sql.append("t.interest_start_date as interestStartDate,t.deadline_date as deadlineDate,net.name as netName,t.acc_State as accState,");
        sql.append("f.subject_name as subjectName,cur.cur_name as curName ");
        sql.append(" from Fc_Acc t left join fc_Subject_pool f on t.subject_id=f.id ");
        sql.append(" left join sys_corp net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
        sql.append(" left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        sql.append(" and t.acc_state in('N','P')");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id=:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id=:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(endUnit)) {
            sql.append(" and t.end_unit=:endUnit");
            params.put("endUnit", endUnit);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_Id=:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            sql.append(" and t.Subject_id=:subjectId");
            params.put("subjectId", subjectId);
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.subject_code = :sysSubjectCode");
            params.put("sysSubjectCode", sysSubjectCode);
        }
        if (StringUtils.isNotEmpty(bankCode)) {
            sql.append(" and t.bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        sql.append(" order by t.blnctr_Acc asc ,t.ACC_OPEN_TIME desc,t.CREATE_DATE ");
        return listBySQLAliasToBean(sql.toString(), params, FcAccPlusVO.class);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @param accList
     * @param bankTypeId
     * @return
     */
    public List<FcAccPlusVO> getAccountByParamsAndGrant(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, List<String> accList, String bankTypeId) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sql.append("t.balance_Way as balanceWay,corp.name as corpName,t.acc_open_time as accOpenTime, ");
        sql.append("net.name as netName,t.acc_State as accState,");
        sql.append("f.subject_name as subjectName,cur.cur_name as curName ");
        sql.append(" from Fc_Acc t left join fc_Subject f on t.subject_id=f.id ");
        sql.append(" left join sys_corp net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
        sql.append(" left join bt_currency cur on t.cur_id=cur.id ");
        sql.append(" left join bt_input_bank_info bi on t.bank_code = bi.sys_bank_code ");
        sql.append(" left join bt_bank_type bbt on (bi.bank_prefix = bbt.bank_prefix and (bbt.bank_name_prefix is null or bi.bank_name like bt_concat2(bbt.bank_name_prefix,'%'))) ");
        sql.append(" where 1=1 and t.acc_state in('N','P')");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id=:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id=:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_Id=:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            sql.append(" and t.Subject_id=:subjectId");
            params.put("subjectId", subjectId);
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.sys_Subject_Code = :sysSubjectCode");
            params.put("sysSubjectCode", sysSubjectCode);
        }
        if (StringUtils.isNotEmpty(bankTypeId)) {
            sql.append(" and bbt.id = :bankTypeId");
            params.put("bankTypeId", bankTypeId);
        }
        if (!EmptyUtils.isEmptyList(accList)) {
            sql.append(" and t.id in (:accList)");
            params.put("accList", accList);
        }
        sql.append(" order by t.blnctr_Acc asc ,t.ACC_OPEN_TIME desc,t.CREATE_DATE ");
        return listBySQLAliasToBean(sql.toString(), params, FcAccPlusVO.class);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @return
     */
    public List<FcAccPlusVO> getAccountByParamsAndGrantForPool(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, List<String> accList, String bankCode) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sql.append("t.balance_Way as balanceWay,corp.name as corpName,t.acc_open_time as accOpenTime, ");
        sql.append("net.pool_Name as netName,t.acc_State as accState,");
        sql.append("f.subject_name as subjectName,cur.cur_name as curName ");
        sql.append(" from Fc_Acc t left join fc_Subject_pool f on t.subject_id=f.id ");
        sql.append(" left join fc_corp_pool net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
        sql.append(" left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        sql.append(" and t.acc_state in('N','P')");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id=:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id=:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_Id=:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            sql.append(" and t.Subject_id=:subjectId");
            params.put("subjectId", subjectId);
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.sys_Subject_Code = :sysSubjectCode");
            params.put("sysSubjectCode", sysSubjectCode);
        }
        if (StringUtils.isNotEmpty(bankCode)) {
            sql.append(" and t.bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        if (!EmptyUtils.isEmptyList(accList)) {
            sql.append(" and t.id in (:accList)");
            params.put("accList", accList);
        }
        sql.append(" order by t.blnctr_Acc asc ,t.ACC_OPEN_TIME desc,t.CREATE_DATE ");
        return listBySQLAliasToBean(sql.toString(), params, FcAccPlusVO.class);
    }
    /**
     * 根据( 账号种类/状态为正常/部分冻结/只收不支)参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @return
     */
    public List<FcAccListVO> getAccountByParams(String[] accTypes, String curId, String netId, String corpId) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sql.append("t.balance_Way as balanceWay,corp.name as corpName,");
        sql.append("net.name as netName,t.acc_State as accState,");
        sql.append("f.subject_name as subjectName,cur.cur_name as curName ");
        sql.append(" from Fc_Acc t left join fc_Subject f on t.subject_id=f.id ");
        sql.append(" left join sys_corp net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
        sql.append(" left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        sql.append(" and t.acc_state in('N','P','S')");
        Map<String, Object> params = new HashMap<String, Object>();
        //去掉按账户类型过滤，除表外账户，其他所有账户类型都可以查询
        if (accTypes != null && accTypes.length > 0) {
            sql.append("  and t.ACC_TYPE in(:accTypes) ");
            params.put("accTypes", accTypes);
        } else {
            sql.append("  and t.ACC_TYPE <> :accType ");
            params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id=:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id=:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_Id=:corpId");
            params.put("corpId", corpId);
        }
        sql.append(" order by t.ACC_OPEN_TIME desc");
        return listBySQLAliasToBean(sql.toString(), params, FcAccListVO.class);
    }
    /**
     * 内部账号查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTableAll(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params) {
        String sql = "select t.id as id," + "t.blnctr_Acc as blnctrAcc," + "t.acc_Name as accName,t.net_Id as netId," + "t.corp_Id as corpId,t.cur_Id as curId," + "t.acc_Type as accType," + "t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime,t.pool_sign as poolSign" + " from Fc_Acc t  where 1=1 " +
                //过滤掉资金池内部账号
                " and (t.pool_sign <> '1' or t.pool_sign is null) " + " and " + corpAuthSql;
        return getDynamicBySQL(dataTablesInput, sql, FcAccListVO.class, "t", false, params);
    }
    /**
     * 资金池内部账号查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTableAllForPool(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params) {
        String sql = "select t.id as id," + "t.blnctr_Acc as blnctrAcc," + "t.acc_Name as accName,t.net_Id as netPoolId," + "t.corp_Id as corpId,t.cur_Id as curId," + "t.acc_Type as accType," + "t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime,t.pool_sign as poolSign" + " from Fc_Acc t  where 1=1 " +
                //查询资金池内部账号
                " and t.pool_sign='1'" + " and " + corpAuthSql;
        return getDynamicBySQL(dataTablesInput, sql, FcAccListVO.class, "t", false, params);
    }
    /**
     * 内部账号查询状态正常
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FcAccListVO> getDataTableAllNormal(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.acc_Type as accType,");
        sql.append("t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime");
        sql.append(" from Fc_Acc t  where 1=1 ");
        //过滤掉资金池内部账号
        sql.append(" and (t.pool_sign <> '1' or t.pool_sign is null) ");
        if (StringUtils.isNotEmpty(corpAuthSql)) {
            sql.append(" and ").append(corpAuthSql);
        }
        //已注销的内部账号不能授权可做账户授权-云南交投
        sql.append(" and ( t.acc_State = 'N' or t.acc_State = 'P' or t.acc_State = 'S' or t.acc_State = 'D' ) ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAccListVO.class, "t", false, params);
    }
    /**
     * 根据账号ID列表查询账号列表
     *
     * @param ids
     * @return
     */
    public List<FcAccDTO> listFcAccInfo(List<String> ids, List<String> accState) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 ");
        if (ids != null && !ids.isEmpty()) {
            sql.append(" and ");
            sql.append(getSQLIn(ids, 900, "t.id"));
        }
        if (accState != null && !accState.isEmpty()) {
            sql.append(" and ");
            sql.append(getSQLIn(accState, 900, "t.acc_State"));
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccDTO.class);
    }
    /**
     * 根据账号ID列表查询账号列表
     *
     * @param ids
     * @return
     */
    public List<FcAccDTO> listFcAccInfoForPool(List<String> ids, List<String> accState) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnionForPool());
        sql.append(" where 1=1 ");
        if (ids != null && !ids.isEmpty()) {
            sql.append(" and ");
            sql.append(getSQLIn(ids, 900, "t.id"));
        }
        if (accState != null && !accState.isEmpty()) {
            sql.append(" and ");
            sql.append(getSQLIn(accState, 900, "t.acc_State"));
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccDTO.class);
    }
    /**
     * 根据单位ID列表查询账号列表
     *
     * @param corpIds
     * @param accState
     * @return
     */
    public List<FcAccDTO> listFcAccInFoByCorpIds(List<String> corpIds, String accState) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 and ");
        Map map = new HashMap();
        if (StringUtils.isNotEmpty(accState)) {
            sql.append("  t.acc_State =:accState and ");
            map.put("accState", accState);
        }
        String where = getSQLIn(corpIds, 900, "t.corp_Id");
        return listBySQLAliasToBean(sql.append(where).toString(), map, FcAccDTO.class);
    }
    /**
     * 功能介绍 : 判断账户是否同币别。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public boolean getFcAccCurCode(String blnctrAcc, String chargeAccount) {
        String[] accs = new String[]{blnctrAcc, chargeAccount};
        String hql = " select curId from FcAcc where blnctrAcc in (:accs) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accs", accs);
        List<String> curCodes = this.listByHQL(hql, params);
        if (null == curCodes || curCodes.isEmpty() || curCodes.size() < 2) {
            return false;
        }
        String curCode0 = curCodes.get(0);
        String curCode1 = curCodes.get(1);
        return curCode0.equals(curCode1);
    }
    /**
     * 功能介绍  ： 查询相关科目账号。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getFcAccBySubjectCode(String chargeAccount, String netId, String subjectCode) {
        String hql = " select fa from FcAcc fa where fa.accState in ('N','P','S') and fa.subjectId = :subjectCode and fa.netId = :netId " + " and exists (select 1 from FcAcc fc where fc.blnctrAcc = :chargeAccount and fc.curId = fa.curId) ";
        Map<String, String> params = new HashMap<String, String>();
        params.put("subjectCode", subjectCode);
        params.put("chargeAccount", chargeAccount);
        params.put("netId", netId);
        List<FcAcc> list = listByHQL(hql, params);
        return copyListAndInject(list, FcAccDTO.class);
    }
    /**
     * <b>功能介绍：</b>根据系统科目查询账户<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<FcAccDTO> findBySysSubjectId(String sysSubjectId, String netIds, Set<String> corpIds, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 and t.acc_state = '" + InnerAccountConstant.ACC_STATE_NORMAL + "' ");
        if (StringUtils.isNotEmpty(sysSubjectId)) {
            sql.append("and  f.sys_subject_code = '");
            sql.append(sysSubjectId);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(netIds)) {
            sql.append("and  t.net_Id = '");
            sql.append(netIds);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append("and  t.cur_Id = '");
            sql.append(curId);
            sql.append("' ");
        }
        List<String> corpIdss = new ArrayList<String>(corpIds);
        String where = getSQLIn(corpIdss, 900, "t.corp_Id");
        return listBySQLAliasToBean(sql.append("and " + where).toString(), null, FcAccDTO.class);
    }

    public List<FcAccDTO> findBySysSubject(String sysSubjectId, String netIds, List<String> corpIds, List<String> curIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 and t.acc_state = '" + InnerAccountConstant.ACC_STATE_NORMAL + "' ");
        if (StringUtils.isNotEmpty(sysSubjectId)) {
            sql.append("and  f.sys_subject_code = '");
            sql.append(sysSubjectId);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(netIds)) {
            sql.append("and  t.net_Id = '");
            sql.append(netIds);
            sql.append("' ");
        }
        if (ObjectUtil.isNotEmpty(curIds)){
            sql.append(" and " + getSQLIn(curIds, 900, "t.cur_Id"));
        }
        if (ObjectUtil.isNotEmpty(corpIds)){
            sql.append(" and " + getSQLIn(corpIds, 900, "t.corp_Id"));
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccDTO.class);
    }

    /**
     * <b>功能介绍：</b>根据科目id,系统科目code查询账户(过滤掉关联了银行代码)<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<FcAccDTO> findBySysSubjectIdFilterBankCode(String subjectId, String sysSubjectCode, String netIds, Set<String> corpIds, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 and t.acc_state = '" + InnerAccountConstant.ACC_STATE_NORMAL + "' ");
        //sql.append(" and t.bank_code is null ");
        if (StringUtils.isNotEmpty(subjectId)) {
            sql.append(" and f.id = '");
            sql.append(subjectId);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.sys_subject_code = '");
            sql.append(sysSubjectCode);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(netIds)) {
            sql.append(" and t.net_Id = '");
            sql.append(netIds);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_Id = '");
            sql.append(curId);
            sql.append("' ");
        }
        List<String> corpIdss = new ArrayList<String>(corpIds);
        String where = getSQLIn(corpIdss, 900, "t.corp_Id");
        return listBySQLAliasToBean(sql.append("and " + where).toString(), null, FcAccDTO.class);
    }
    /**
     * 功能介绍  ： 查询相关科目账号。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getFcAccByCorpSubjectCode(String netId, String corpId, String subjectCode) {
        Map<String, String> params = new HashMap<String, String>();
        StringBuffer hql = new StringBuffer();
        hql.append(" select fa from FcAcc fa where fa.accState in ('N','P','S') ");
        if (!StringUtils.isEmpty(netId)) {
            hql.append(" and fa.netId = :netId ");
            params.put("netId", netId);
        }
        if (!StringUtils.isEmpty(corpId)) {
            hql.append(" and fa.corpId = :corpId ");
            params.put("corpId", corpId);
        }
        if (!StringUtils.isEmpty(subjectCode)) {
            hql.append(" and fa.subjectId = :subjectCode ");
            params.put("subjectCode", subjectCode);
        }
        List<FcAcc> list = listByHQL(hql.toString(), params);
        return copyListAndInject(list, FcAccDTO.class);
    }
    /**
     * 功能介绍 : 开户未复核通过的账户。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> findNoCheckVFcAcc(FcDayCloseDTO dayClose) {
        //依次为新开、改户、开户小账户、休眠、注销未复核
        String sql = "select t.id as id," + " t.blnctr_Acc as blnctrAcc," + " t.acc_Name as accName,t.net_Id as netId," + " t.corp_Id as corpId,t.cur_Id as curId," + " t.subject_Id as subjectId,t.acc_Type as accType," + " t.balance_Way as balanceWay,corp.name as corpName," + " net.name as netName,t.acc_State as accState," + " f.subject_name as subjectName,cur.cur_name as curName," + " su.username as operatorCode,t.acc_start_date as accStartDate,t.acc_open_time as accOpenTime " + " from Fc_Acc t left join fc_Subject f on t.subject_id=f.id " + " left join sys_corp corp on t.net_id=corp.id " + " left join sys_corp net on t.corp_id=net.id " + " left join bt_currency cur on t.cur_id=cur.id" + " left join sys_user su on t.operator_Code=su.login_name" + " where t.acc_state in ('W','E','Q','C','R','U') " + " and t.net_Id = :netId " + " and t.acc_Start_Date < :accStartDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("accStartDate", DateUtils.addDay(dayClose.getCloseDate(), 1));
        return listBySQLAliasToBean(sql, params, FcAccDTO.class);
    }
    /**
     * 功能介绍 : 查询账户集合。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getAccSet(List<String> accList) {
        String sql = getCommonFcAccUnion() + " where 1=1 and " + getSQLIn(accList, 900, "t.blnctr_acc");
        return listBySQLAliasToBean(sql, null, FcAccDTO.class);
    }
    /**
     * 功能介绍 : 查询表外账户集合。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getOutAccTypeAccs() {
        String sql = getCommonFcAccUnion() + " where t.acc_type = :accType and t.acc_State = 'N' order by t.blnctr_acc ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);
        return listBySQLAliasToBean(sql, params, FcAccDTO.class);
    }
    /**
     * 功能介绍 : 获取可记账内部账号(非表外)。<br>
     * <b>日期：</b>2017年11月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccVO> getTallyAccTypeAccs(List<String> ids) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.id as id,t.blnctr_acc as blnctrAcc,t.acc_name as accName, ");
        sql.append(" t.cur_Id as curId, bc.cur_name as curName, f.subject_name as subjectName,f.id as subjectId ");
        sql.append(" from Fc_Acc t left join fc_Subject f on t.subject_id=f.id ");
        sql.append(" left join bt_currency bc on t.cur_id = bc.id ");
        sql.append(" where t.acc_type <> :accType and t.acc_State in ('N','P','S')  ");
        if (ids != null && !ids.isEmpty()) {
            sql.append(" and ");
            sql.append(getSQLIn(ids, 900, "t.id"));
        }
        sql.append(" order by t.blnctr_acc ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);
        return listBySQLAliasToBean(sql.toString(), params, FcAccVO.class);
    }
    /**
     * 根据科目 判断是否存在使用的账号
     *
     * @param subujectId
     * @return
     */
    public boolean isExsits(String subujectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("subujectId", subujectId);
        params.put("accState", InnerAccountConstant.ACC_STATE_DESTORY);
        Map rs = getBySQL(" select count(1) as CN from fc_acc f where f.subject_id = :subujectId and acc_State <> :accState", params);
        Integer count = new Integer(rs.get("CN").toString());
        return count != null && count > 0;
    }
    /**
     * 根据内部账号搜索是否有关联的银行账号存在
     *
     * @param blnctrAcc
     * @return
     */
    public boolean isExsitsForValidBankAccByInnerAcc(String blnctrAcc) {
        String sql = " select count(1) as CN from bt_bank_acc acc ,bt_bank_acc_cur cur,fc_acc fc " + "  where acc.id = cur.bank_acc_id  and cur.counter_acc=fc.blnctr_acc" + " and fc.blnctr_acc =:blnctrAcc and acc.valid_sign =:validSign and acc.status =:status ";
        Map<String, Object> params = new HashMap<>();
        params.put("blnctrAcc", blnctrAcc);
        params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        params.put("status", CommonConstant.STATUE_CHECK);
        Map rs = getBySQL(sql, params);
        Integer count = new Integer(rs.get("CN").toString());
        return count != null && count > 0;
    }
    /**
     * 通过父银行账号 找子账号对应的内部账号【N,P,S】
     *
     * @param parentBankAccId
     * @param curId
     * @param ids
     * @return
     */
    public List<FcAccPlusVO> getSubAccFcAccByParentBankAcc(String parentBankAccId, String curId, List<String> ids) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  distinct fc.blnctr_Acc as blnctrAcc,fc.acc_name as accName,fc.acc_Type as accType, ");
        sql.append(" cur.cur_id as curId,bc.cur_name as curName,fcs.subject_name as subjectName,fcs.id as subjectId ");
        sql.append(" from bt_bank_acc acc ,bt_bank_acc_cur cur,fc_subject fcs,bt_currency bc,fc_acc fc ");
        sql.append(" where acc.id = cur.bank_acc_id  and cur.cur_id = bc.id and fc.acc_state in('N','P','S') ");
        sql.append(" and fc.subject_id = fcs.id and cur.counter_acc = fc.blnctr_acc");
        sql.append(" and cur.prent_acc_id = :parentBankAccId and cur.cur_id= :curId ");
        if (ids != null && !ids.isEmpty()) {
            sql.append(" and ");
            sql.append(getSQLIn(ids, 900, "fc.id"));
        }
        sql.append(" order by fc.blnctr_acc ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentBankAccId", parentBankAccId);
        params.put("curId", curId);
        return listBySQLAliasToBean(sql.toString(), params, FcAccPlusVO.class);
    }
    /**
     * 资金池账号查询语句
     *
     * @return
     */
    public String getCommonFcAccUnionForPool() {//f.sys_subject_code as sysSubjectCode,f.measure_Unit as measureUnit,
        String sql = "select t.id as id," + "t.update_Date as updateDate,t.blnctr_Acc as blnctrAcc,t.acc_Name as accName,t.net_Id as netPoolId,sn.POOL_NAME as netPoolName,"
                + "t.corp_Id as corpId,sc.code corpCode,sc.name corpName,t.cur_Id as curId,cur.cur_name AS curName,t.subject_Id as subjectPoolId,f.subject_name as subjectPoolName,f.subject_name as subjectName,"
                + "t.acc_Type as accType,t.attribute_Id as attributeId,t.nature_Code_Id as natureCodeId," + "a.ATTRIBUTE_NAME as attributeName,n.NATURE_NAME as natureName,"
                + "t.bank_Code as bankCode,bi.bank_Name AS bankName,t.Delay_Rate as DelayRate," + "t.balance_Way as balanceWay,t.interest_Sign as interestSign,"
                + "t.interest_Start_Date as interestStartDate,t.interest_Rate as interestRate," + "t.overdraft_Sign as overdraftSign,"
                + "t.overdraft_Rate as overdraftRate,t.overdraft_Money as overdraftMoney," + "t.contract_No as contractNo,t.deadline_Date as deadlineDate,"
                + "t.delay_Deadline_Date as delayDeadlineDate,t.overdue_Rate as overdueRate," + "t.acc_For_Deduct as accForDeduct,t.deduct_Mode as deductMode,"
                + "t.notify_Type as notifyType,t.acc_State as accState," + "t.agreed_Deposit_Sign as agreedDepositSign,t.predraw_Sign as predrawSign,"
                + "t.if_Compare as ifCompare,t.acc_Id as accId," + "t.acc_Start_Date as accStartDate,t.operator_Code as operatorCode,"
                + "t.acc_Open_Time as accOpenTime,t.handle_Charge as handleCharge," + "t.shortcut as shortcut,t.union_Net as unionNet,"
                + "t.union_Acc as unionAcc,t.precation_Acc as precationAcc," + "t.precation_Corp as precationCorp,t.poundage_Rate as poundageRate,"
                + "t.charge_Acc as chargeAcc,t.transact_Way as transactWay," + "t.valid_Sign as validSign,t.check_Back_Reason as checkBackReason,"
                + "t.end_Unit as endUnit,t.certificate_Way as certificateWay,t.check_time as checkTime," + "t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,t.checker_code as checkerCode,"
                + "t.acc_Cussent as accCussent,t.pool_Sign as poolSign "
                + " from Fc_Acc t "
                + " LEFT JOIN fc_corp_pool sn ON sn.id=t.net_id "
                + " LEFT JOIN sys_corp sc ON sc.id=t.corp_id "
                + " LEFT JOIN bt_currency cur  ON t.cur_id = cur.id  "
                + " LEFT JOIN bt_input_bank_info bi ON t.bank_code = bi.sys_bank_code   "
                + " left join BT_ACC_NATURE n on t.nature_Code_Id=n.id "
                + " left join BIS_ACC_ATTRIBUTE a on t.attribute_Id=a.id "
                + " left join fc_subject_pool f on t.subject_id = f.id ";
        return sql;
    }
    /**
     * 根据账号查对象(资金池)
     *
     * @param blnctrAcc
     * @return
     */
    public FcAccVO getPoolFcAccVOByBlnctrAcc(String blnctrAcc) {
        String sql = getCommonFcAccUnionForPool() + " where t.blnctr_Acc=:blnctrAcc ";
        Map map = new HashMap();
        map.put("blnctrAcc", blnctrAcc);
        return getBySQLAliasToBean(sql, map, FcAccVO.class);
    }
    /**
     * 内部账号查询状态正常（资金池）
     *
     * @param dataTablesInput
     * @return
     * @author zuoy 2019-05-16
     */
    public DataTablesOutput<FcAccListVO> getPoolDataTableAllNormal(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id, t.blnctr_Acc as blnctrAcc, t.acc_Name as accName, t.net_Id as netPoolId, ");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.acc_Type as accType,");
        sql.append("t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime");
        sql.append(" from Fc_Acc t  where 1=1 ");
        if (StringUtils.isNotEmpty(corpAuthSql)) {
            sql.append(" and ").append(corpAuthSql);
        }
        //已注销的内部账号不能授权可做账户授权-云南交投
        sql.append(" and ( t.acc_State = 'N' or t.acc_State = 'P' or t.acc_State = 'S' or t.acc_State = 'D' ) ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAccListVO.class, "t", false, params);
    }
    /**
     * <b>功能介绍：</b>获取网点下计息的资金池账号<br>
     * <b>日期：</b>2019年5月21日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public List<FcAccDTO> getNetAccByPoolSign(String netId, String accType, String interestSign, String poolSign) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id, t.blnctr_Acc as blnctrAcc, t.acc_Name as accName, t.net_Id as netId, ");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId, ");
        sql.append("t.acc_Type as accType, sb.subject_name as subjectName,sb.id as subjectId, ");
        sql.append("t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime ");
        sql.append(" from Fc_Acc t, fc_subject_pool sb  where t.subject_id = sb.id ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (EmptyUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id = :netId ");
            params.put("netId", netId);
        }
        if (EmptyUtils.isNotEmpty(accType)) {
            sql.append(" and t.acc_Type = :accType ");
            params.put("accType", accType);
        }
        if (EmptyUtils.isNotEmpty(interestSign)) {
            sql.append(" and t.interest_Sign = :interestSign ");
            params.put("interestSign", interestSign);
        }
        if (EmptyUtils.isNotEmpty(poolSign)) {
            sql.append(" and t.pool_Sign = :poolSign ");
            params.put("poolSign", poolSign);
        }
        sql.append(" and ( t.acc_State = 'N' or t.acc_State = 'P' or t.acc_State = 'S' ) order by t.blnctr_Acc ");
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }
    /**
     * <b>功能介绍：</b>查询资金池科目的资金池账号<br>
     * <b>日期：</b>2019年5月22日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public List<FcAccDTO> getNetPoolAccBySubject(String netId, String curId, String subjectId, String poolSign) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id, t.blnctr_Acc as blnctrAcc, t.acc_Name as accName, t.net_Id as netId, ");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId, ");
        sql.append("t.acc_Type as accType, sb.subject_name as subjectName,sb.id as subjectId, ");
        sql.append("t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime");
        sql.append(" from Fc_Acc t, fc_subject_pool sb  where t.subject_id = sb.id ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (EmptyUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id = :netId ");
            params.put("netId", netId);
        }
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (EmptyUtils.isNotEmpty(subjectId)) {
            sql.append(" and t.subject_id = :subjectId ");
            params.put("subjectId", subjectId);
        }
        if (EmptyUtils.isNotEmpty(poolSign)) {
            sql.append(" and t.pool_Sign = :poolSign ");
            params.put("poolSign", poolSign);
        }
        sql.append(" and ( t.acc_State = 'N' or t.acc_State = 'P' or t.acc_State = 'S' ) order by t.acc_start_date desc ");
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }
    /**
     * 根据单位ID列表查询账号列表
     *
     * @param corpIds
     * @param accState
     * @return
     */
    public List<FcAccDTO> listFcAccPoolByCorpIds(List<String> corpIds, String accState, String poolSign) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnionForPool());
        sql.append(" where 1=1 and ");
        Map map = new HashMap();
        if (StringUtils.isNotEmpty(accState)) {
            sql.append("  t.acc_State =:accState and ");
            map.put("accState", accState);
        }
        if (StringUtils.isNotEmpty(poolSign)) {
            sql.append("  t.pool_sign =:poolSign and ");
            map.put("poolSign", poolSign);
        }
        String where = getSQLIn(corpIds, 900, "t.corp_Id");
        return listBySQLAliasToBean(sql.append(where).toString(), map, FcAccDTO.class);
    }
    /**
     * 根据资金池单位查询账号(开户待复核、开户复核打回、改户待复核、改户复核打回、销户待复核、销户复核打回)
     *
     * @param netId    单位id
     * @param corpId   单位id
     * @param isFilter 是否过滤状态 true过滤，flase 不过滤
     * @return
     */
    public List<FcAccDTO> listFcAccPoolByCorpId(String netId, String corpId, boolean isFilter) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnionForPool());
        sql.append(" where 1=1 ");
        if (isFilter) {
            sql.append(" and t.acc_State in ('W','B','E','F','R','Z') ");
        }
        Map map = new HashMap();
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id =:netId");
            map.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_id =:corpId");
            map.put("corpId", corpId);
        }
        return listBySQLAliasToBean(sql.toString(), map, FcAccDTO.class);
    }
    /**
     * 根据单位币别筛选有效的企业活期账号
     *
     * @param corpId
     * @param curId
     * @return
     */
    public List<FcAccVO> listFcAccByCorpId(String corpId, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 ");
        Map map = new HashMap();
        sql.append(" and t.acc_state in('N','P')  ");
        //企业活期账号
        sql.append(" and f.acc_type = '01' and f.sys_subject_code = '2120'  ");
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_id =:corpId");
            map.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id =:curId");
            map.put("curId", curId);
        }
        return listBySQLAliasToBean(sql.toString(), map, FcAccVO.class);
    }
    /**
     * 根据银行号获取银行类别信息(因构建报循环调用问题，先放到此处，后面再行优化)
     *
     * @param bankCode [银行号]
     * @return 银行类别信息
     * @author wangzx
     * @date 2020-05-07
     */
    public BankTypeDTO getInfoByBankCode(String bankCode) {
        String sql = " select t.id as id, t.bank_type as bankType, t.type_name as typeName from bt_input_bank_info i " + " inner join bt_bank_type t on i.bank_prefix=t.bank_prefix " + " and (t.bank_name_prefix is null or i.bank_name like bt_concat2(t.bank_name_prefix,'%')) " + " where i.sys_bank_code = :bankCode ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bankCode", bankCode);
        return getBySQLAliasToBean(sql, params, BankTypeDTO.class);
    }
    /**
     * 根据单位统计内部账户
     *
     * @param corpId
     * @param busType del:单位删除 disable:禁用 change:上级变更
     * @return
     */
    public int getFcAccCountByCorpId(String corpId, String busType) {
        String sql = "select count(*) from fc_acc where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        if ("disable".equals(busType)) {
            //N-正常状态
            sql += " and acc_State='" + InnerAccountConstant.ACC_STATE_NORMAL + "'";
        }
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    /**
     * 根据网点单位科目代码币种银行类型
     *
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @param curId
     * @param bankTypeId
     * @return
     */
    public List<FcAccVO> getAccListByInfo(String netId, String corpId, String sysSubjectCode, String curId, String bankTypeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" left join bt_currency cur on t.cur_id = cur.id ");
        sql.append(" left join bt_input_bank_info bi on t.bank_code = bi.sys_bank_code ");
        sql.append(" left join bt_bank_type bbt on (bi.bank_prefix = bbt.bank_prefix ");
        sql.append(" and (bbt.bank_name_prefix is null or bi.bank_name like bt_concat2(bbt.bank_name_prefix, '%')))");
        sql.append(" where t.acc_state = 'N' ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id =:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_id =:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.sys_subject_code =:sysSubjectCode");
            params.put("sysSubjectCode", sysSubjectCode);
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id =:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(bankTypeId)) {
            sql.append(" and bbt.id = :bankTypeId");
            params.put("bankTypeId", bankTypeId);
        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccVO.class);
    }
    /**
     * 查看计息账号数据
     *
     * @param dataTablesInput 查询
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listFcAccInfoDataTable(DataTablesInput dataTablesInput, String joinSql) {
        String netId = (String) dataTablesInput.getFormdata().get("netId");
        String corpId = (String) dataTablesInput.getFormdata().get("corpId");
        String curId = (String) dataTablesInput.getFormdata().get("curId");
//		String sysSubjectCode = (String)dataTablesInput.getFormdata().get("sysSubjectCode");
        String bankTypeId = (String) dataTablesInput.getFormdata().get("bankTypeId");
        String accType = (String) dataTablesInput.getFormdata().get("accType");
        String bankAcc = (String) dataTablesInput.getFormdata().get("bankAcc");
        String accName = (String) dataTablesInput.getFormdata().get("accName");
        String interestAcc = (String) dataTablesInput.getFormdata().get("interestAcc");
        String interestType = (String) dataTablesInput.getFormdata().get("interestType");
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.acc_Type as accType,");
        sql.append("t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime,t.pool_sign as poolSign");
        sql.append(" from Fc_Acc t ");
        sql.append(" left join fc_subject f on t.subject_id = f.id ");
        sql.append(" left join bt_input_bank_info bi on t.bank_code = bi.sys_bank_code ");
        sql.append(" left join bt_bank_type bbt on (bi.bank_prefix = bbt.bank_prefix ");
        sql.append(" and (bbt.bank_name_prefix is null or bi.bank_name like bt_concat2(bbt.bank_name_prefix, '%')))");
        sql.append(" where t.acc_state = 'N' ");
        //过滤掉资金池内部账号
        sql.append(" and (t.pool_sign <> '1' or t.pool_sign is null) ");
        if (StringUtils.isNotEmpty(interestType)) {
            if ("pay".equals(interestType)) {
                //新增过滤掉已经绑定过计息账户的账户
                if (StringUtils.isNotEmpty(interestAcc)) {
                    String accSql = SqlUtil.getInSql(interestAcc.split(CommonConstant.SEMI_SPLIT), "fa.blnctr_acc", true);
                    sql.append(" and exists (select 1 from fc_acc fa where t.id = fa.id ");
                    sql.append(" and ( ").append(accSql).append(" or fa.interest_rec_pay is null))");
                } else {
                    sql.append(" and exists (select 1 from fc_acc fa where t.id = fa.id and fa.interest_rec_pay is null) ");
                }
            } else if ("exp".equals(interestType)) {
                //新增过滤掉已经绑定过计息账户的账户
                if (StringUtils.isNotEmpty(interestAcc)) {
                    String accSql = SqlUtil.getInSql(interestAcc.split(CommonConstant.SEMI_SPLIT), "fa.blnctr_acc", true);
                    sql.append(" and exists (select 1 from fc_acc fa where t.id = fa.id ");
                    sql.append(" and ( ").append(accSql).append(" or fa.interest_in_exp is null))");
                } else {
                    sql.append(" and exists (select 1 from fc_acc fa where t.id = fa.id and fa.interest_in_exp is null) ");
                }
            }
        } else {
            sql.append(" and exists (select 1 from fc_acc fa where t.id = fa.id and fa.interest_in_exp is null and fa.interest_rec_pay is null) ");
        }
        if (StringUtils.isNotBlank(joinSql)) {
            sql.append(joinSql);
        }
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id =:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_id =:corpId");
            params.put("corpId", corpId);
        }
        /*if (StringUtils.isNotEmpty(sysSubjectCode)) {
			sql.append(" and f.sys_subject_code =:sysSubjectCode");
			params.put("sysSubjectCode", sysSubjectCode);
		}*/
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id =:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(bankTypeId)) {
            sql.append(" and bbt.id = :bankTypeId");
            params.put("bankTypeId", bankTypeId);
        }
        if (StringUtils.isNotEmpty(accType)) {
            sql.append(" and t.acc_Type = :accType");
            params.put("accType", accType);
        }
        if (StringUtils.isNotEmpty(bankAcc)) {
            sql.append(" and t.blnctr_acc like :bankAcc");
            params.put("bankAcc", "%" + bankAcc + "%");
        }
        if (StringUtils.isNotEmpty(accName)) {
            sql.append(" and t.acc_name like :accName");
            params.put("accName", "%" + accName + "%");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAccListVO.class, "t", false, params);
    }
    /**
     * 根据账号信息查找计息账号列表
     *
     * @param vo
     * @return
     */
    public List<FcAccVO> getAccListByExpPay(FcAccVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(vo.getBlnctrAcc())) {
            sql.append(" where (t.interest_in_exp =:blnctrAcc or t.interest_rec_pay =:blnctrAcc)");
            sql.append(" and t.acc_state <> 'D'");
            params.put("blnctrAcc", vo.getBlnctrAcc());
        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccVO.class);
    }
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    public List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, List<String> accState, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(corpIdList)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpIdList, "t.corp_id", true));
        }
        if (!EmptyUtils.isEmpty(accState)) {
            sql.append(" and ").append(SqlUtil.getInSql(accState, "t.acc_State", true));
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccDTO.class);
    }
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    public List<String> getFcAccsByCorpIdList(List<String> corpIdList, List<String> accState, String joinSql) {
        StringBuilder sql = new StringBuilder("select t.blnctr_Acc as blnctrAcc");
        sql.append(" from Fc_Acc t ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(corpIdList)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpIdList, "t.corp_id", true));
        }
        if (!EmptyUtils.isEmpty(accState)) {
            sql.append(" and ").append(SqlUtil.getInSql(accState, "t.acc_State", true));
        }
        sql.append(joinSql);
        return this.createSQLQuery(sql.toString()).list();
    }
    public List<FcAccDTO> getBlnctrAcc(String agreedSql, String accAuthSql, String poolId, String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.id as id, f.blnctr_acc as blnctrAcc, f.acc_name as accName, f.cur_id as curId from fc_acc f ");
        sql.append(" where f.acc_state='N' and f.pool_sign = '1' and f.acc_type = '01'");
        // 企业活期账号条件
        sql.append(" and ").append(agreedSql);
        // 账户信息条件
        sql.append(" and ").append(accAuthSql);
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(poolId)) {
            sql.append(" and f.net_id = :poolId");
            params.put("poolId", poolId);
        }
        if (StringUtils.isNotBlank(corpId)) {
            sql.append(" and f.corp_id = :corpId");
            params.put("corpId", corpId);
        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }
    public List<FcAccDTO> getBlnctrAccByCorpId(String agreedSql, String accAuthSql, String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.id as id, f.blnctr_acc as blnctrAcc, f.acc_name as accName, f.cur_id as curId from fc_acc f ");
        sql.append(" where f.acc_state='N' and (f.pool_sign = '0' or f.pool_sign is null ) and f.acc_type = '01'");
        // 企业活期账号条件
        sql.append(" and ").append(agreedSql);
        // 账户信息条件
        sql.append(" and ").append(accAuthSql);
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(corpId)) {
            sql.append(" and f.corp_id = :corpId");
            params.put("corpId", corpId);
        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }
    /**
     * 更新贷款账号展期信息
     *
     * @param loanAcc
     * @param deferredDate
     * @param deferredRate
     */
    public void updateLoanAccDeferredDate(String loanAcc, Date deferredDate, BigDecimal deferredRate) {
        String sql = "update fc_acc a set a.delay_deadline_date = :deferredDate,a.delay_rate = :deferredRate where a.blnctr_acc = :loanAcc ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deferredRate", deferredRate);
        params.put("deferredDate", deferredDate);
        params.put("loanAcc", loanAcc);
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 根据查询条件获取内部账号信息和科目代码信息
     *
     * @param fcAccDto 查询条件实体类
     * @return 内部账户集合
     */
    public List<FcAccDTO> listByCondition(FcAccDTO fcAccDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.blnctr_acc AS blnctrAcc,b.acc_Name as accName ,b.acc_type as accType ,b.cur_Id as curId ,f.subject_name as subjectName, ");
        sql.append(" b.balance_way as balanceWay ,b.subject_id as subjectId ,b.corp_id as corpId , b.net_id as netId ");
        sql.append(" from fc_acc b left join fc_subject f on  f.id = b.subject_id ");
        sql.append(" where 1=1 ");
        // todo:未见到哪里有赋这个值？
        if (StrUtil.equals(fcAccDto.getOperateService(), "accountBalance")) {
            sql.append(" and b.acc_State in ('N', 'P', 'S') ");
        }
        Map<String, Object> param = new HashMap<String, Object>(5);
        if (StringUtils.isNotEmpty(fcAccDto.getNetId())) {
            sql.append(" and net_Id =:netId");
            param.put("netId", fcAccDto.getNetId());
        }
        if (EmptyUtils.isNotEmpty(fcAccDto.getCorpId())) {
            sql.append(" and corp_Id =:corpId");
            param.put("corpId", fcAccDto.getCorpId());
        }
        if (StringUtils.isNotEmpty(fcAccDto.getCurId())) {
            sql.append(" and b.cur_Id =:curId");
            param.put("curId", fcAccDto.getCurId());
        }
        if (StringUtils.isNotEmpty(fcAccDto.getSubjectId())) {
            sql.append(" and b.subject_id =:subjectId");
            param.put("subjectId", fcAccDto.getSubjectId());
        }
        if (StringUtils.isNotEmpty(fcAccDto.getSubjectSort())) {
            sql.append(" and f.Subject_sort =:subjectSort");
            param.put("subjectSort", fcAccDto.getSubjectSort());
        }
        if (StringUtils.isNotEmpty(fcAccDto.getAuthSql())) {
            sql.append(fcAccDto.getAuthSql());
        }
        return listBySQLAliasToBean(sql.toString(), param, FcAccDTO.class);
    }
    public List<FcAccDTO> getFcCarryList(FcAccDTO fcAccDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.blnctr_acc AS blnctrAcc,b.acc_Name as accName ,b.acc_type as accType ,b.cur_Id as curId ,f.subject_name as subjectName, ");
        sql.append(" b.balance_way as balanceWay ,b.subject_id as subjectId ,b.corp_id as corpId , b.net_id as netId ");
        sql.append(" from fc_acc b left join fc_subject f on  f.id = b.subject_id ");
        sql.append(" where b.acc_state = 'N' AND b.checker_code IS NOT NULL AND f.subject_sort = 'I' ");
        Map<String, Object> param = new HashMap<String, Object>();
        if (EmptyUtils.isNotEmpty(fcAccDto.getNetId())) {
            sql.append(" and b.net_Id =:netId");
            param.put("netId", fcAccDto.getNetId());
        }
        List<FcAccDTO> list = listBySQLAliasToBean(sql.toString(), param, FcAccDTO.class);
        return list;
    }
    public List<FcAccCorpCurrencyDTO> getAccCorpCurDataByAccounts(List<String> accounts) {
        String sql = " select a.blnctr_acc as blnctrAcc, a.cur_id as curId, a.overdraft_sign as overdraftSign, a.overdraft_money as overdraftMoney, a.balance_way as balanceWay, " +
                " a.net_id as netId, a.acc_type as accType, a.charge_acc as chargeAcc, a.subject_id as subjectId, b.code as netCode, c.cur_code as curCode from fc_acc a " +
                " join sys_corp b on a.net_id = b.id " +
                " join bt_currency c on a.cur_id = c.id " +
                " where 1=1 and " + getSQLIn(accounts, 900, "a.blnctr_acc");
        return listBySQLAliasToBean(sql, null, FcAccCorpCurrencyDTO.class);
    }
    public List<BalanceQueryDTO> queryAccBalanceAndBank(BalanceQueryDTO dto, String userCode, List<String> corpAuth, String... aTrue) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.*,c.name as corpName, s.subject_name,t.subject_id as subjectId, f.typeName,s.subject_code as subjectCode,f.bank_type as bankType ");
        sql.append(" from fc_acc t ");
        sql.append(" left join fc_subject s on s.id = t.subject_id ");
        sql.append(" left join sys_corp c on c.id = t.corp_id");
        sql.append("  left join (SELECT z.COUNTER_ACC, d.type_name as typeName, b.bank_type_id,d.bank_type ");
        sql.append("  FROM BT_BANK_ACC b,bt_bank_acc_cur z,bt_bank_type d,");
        sql.append("  (SELECT z.counter_acc  FROM BT_BANK_ACC c");
        sql.append("  inner join bt_bank_acc_cur z");
        sql.append(" on c.id = z.bank_acc_id WHERE z.counter_acc IS NOT NULL");
        sql.append(" GROUP BY z.counter_acc  HAVING COUNT(1) = 1) e ");
        sql.append("  WHERE b.bank_type_id = d.id and b.id = z.bank_acc_id ");
        sql.append(" AND z.counter_acc = e.counter_acc) f ");
        sql.append(" on t.blnctr_acc = f.counter_acc ");
        sql.append("  where 1 = 1  and t.acc_State <> 'T' ");
        sql.append(" and exists (select 1 from sys_account_grant g where t.id = g.ACCOUNT_ID");
        sql.append(" and g.CAN_QUERY_BALANCE = '1'");
        sql.append(" and g.USER_ID = :userId)");
        if (aTrue == null || aTrue.length == 0) {//账户到期明细表查询
            sql.append(" and t.acc_State != 'D' ");
            sql.append(" and t.deadline_Date <= :deadlineDate ");
            if (EmptyUtils.isBlank(dto.getAccType().trim())) {
                sql.append(" and t.acc_Type_Code in ('03','04','05','06','09','11','12')");
            }
        }
        if (StringUtils.isNotEmpty(dto.getCorpId())) {
            sql.append(" and t.corp_Id = :corpId");
        }
        if (StringUtils.isNotEmpty(dto.getNetId().trim())) {
            sql.append(" and t.net_Id = :netId");
        }
        if (StringUtils.isNotEmpty(dto.getBlnctrAcc().trim())) {
            sql.append(" and t.blnctr_acc = :blnctrAcc");
        }
        if (StringUtils.isNotEmpty(dto.getAccState().trim())) {
            sql.append(" and t.acc_state = :accState");
        }
        if (StringUtils.isNotEmpty(dto.getAccType().trim())) {
            if (aTrue == null || aTrue.length == 0) {//账户到期明细表查询
                sql.append(" and t.acc_Type in ('03','04','05','06','09','11','12')");
            }
            sql.append(" and t.acc_type = :accType");
        }
        if (StringUtils.isNotEmpty(dto.getSubjectId().trim())) {
            sql.append(" and t.subject_Id = :subjectId");
        }
        if (StringUtils.isNotEmpty(dto.getCurId().trim())) {
            sql.append(" and t.cur_Id = :curId");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "t.corp_Id"));
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("corpId", dto.getCorpId());
        param.put("netId", dto.getNetId());
        param.put("blnctrAcc", dto.getBlnctrAcc());
        param.put("accState", dto.getAccState());
        param.put("accType", dto.getAccType());
        param.put("subjectId", dto.getSubjectId());
        param.put("curId", dto.getCurId());
        param.put("userId", userCode);
        sql.append(" order by s.subject_code, f.bank_type,t.blnctr_Acc  ");
        List<BalanceQueryDTO> list = listBySQLAliasToBean(sql.toString(), param, BalanceQueryDTO.class);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }
    /**
     * 根据 netId , 查询 fc_acc 表，返回 blnctr_acc 结算中心账号
     * 除了 netId，没有其它条件
     *
     * @param netId
     * @return
     */
    public List<String> getBlnctrAccByNetId2(String netId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("netId", netId);
        return listBySQL("select blnctr_acc from fc_acc where net_id = :netId", params);
    }
    public List<FcAccSubjectDTO> getAccAndSubjectData(List<String> corpAuth, boolean isPool) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.blnctr_acc as blnctrAcc, a.acc_name as accName,  s.id as subjectId ");
        if (isPool) {
            sql.append(" ,s.subject_name as subjectPoolName ");
        } else {
            sql.append(" ,s.subject_name as subjectName ");
        }
        sql.append(" from fc_acc a ");
        if (isPool) {
            sql.append(" left join fc_subject_pool s on a.subject_id = s.id ");
            sql.append(" where 1=1 and a.pool_sign = '1' ");
        } else {
            sql.append(" left join fc_subject s on a.subject_id = s.id ");
            sql.append(" where 1=1 and a.pool_sign != '1' ");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));
        return listBySQLAliasToBean(sql.toString(), null, FcAccSubjectDTO.class);
    }
    public List<FcAccSubjectDTO> getAccAndSubject(List<String> corpAuth,FcAccSubjectDTO dto, boolean isPool) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.blnctr_acc as blnctrAcc, a.acc_name as accName,  s.id as subjectId ");
        if (isPool) {
            sql.append(" ,s.subject_name as subjectPoolName ");
        } else {
            sql.append(" ,s.subject_name as subjectName ");
        }
        sql.append(" from fc_acc a ");
        if (isPool) {
            sql.append(" left join fc_subject_pool s on a.subject_id = s.id ");
            sql.append(" where 1=1 and a.pool_sign = '1' ");
        } else {
            sql.append(" left join fc_subject s on a.subject_id = s.id ");
            sql.append(" where 1=1 and a.pool_sign != '1' ");
        }
        if (StringUtil.isNotBlank(dto.getNetId())) {
            sql.append(" and a.net_id = '"+dto.getNetId()+"'");
        }
        if (StringUtil.isNotBlank(dto.getSubjectSort())) {
            sql.append(" and s.subject_sort = '"+dto.getSubjectSort()+"'");
        }
        if (StringUtil.isNotBlank(dto.getSubjectId())) {
            sql.append(" and a.subject_id = '"+dto.getSubjectId()+"'");
        }
        if (StringUtil.isNotBlank(dto.getCurId())) {
            sql.append(" and a.cur_id = '"+dto.getCurId()+"'");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));
        return listBySQLAliasToBean(sql.toString(), null, FcAccSubjectDTO.class);
    }
    public List<FcAccSubjectDTO> getAccNameAndSubjectName(String netId, String accType, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.acc_name as accName, b.subject_name as subjectName from fc_acc a");
        sql.append(" join fc_subject b on a.subject_id = b.id ");
        sql.append(" where 1=1 and a.pool_sign != '1' ");
        if (StringUtil.isNotBlank(netId)) {
            sql.append(" and a.net_id = :netId");
        }
        if (StringUtil.isNotBlank(accType)) {
            sql.append(" and a.acc_type = :accType ");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("netId", netId);
        param.put("accType", accType);
        return listBySQLAliasToBean(sql.toString(), param, FcAccSubjectDTO.class);
    }
    public List<FcAccSubjectDTO> getAccAndSubjectAndCurData(Map<String, Object> params, List<String> blnctrAccLists) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.net_id as netId, a.acc_name as accName, a.subject_id as subjectId, a.acc_type as accType, c.cur_name as curName, fs.subject_name as subjectName ");
        sql.append(" from fc_acc a ");
        sql.append(" join bt_currency c on a.cur_id = c.id ");
        sql.append(" join fc_subject fs on a.subject_id = fs.id ");
        sql.append(" where 1 = 1");
        sql.append(" and a.pool_sign != '1' ");
        sql.append(" and a.blnctr_acc is not null");
        if (null != params && StringUtils.isNotEmpty(params.get("accName"))) {
            sql.append(" and a.acc_name=:accName");
        }
        if (null != params && StringUtils.isNotEmpty(params.get("eqs_netId"))) {
            sql.append(" and a.net_id = :eqs_netId ");
        }
        if (null != params && StringUtils.isNotEmpty(params.get("ins_accType"))) {
            sql.append(" and a.acc_type in (:ins_accType) ");
        }
        if (ListUtils.isNotNullOrEmpty(blnctrAccLists)) {
            sql.append(" and ").append(getSQLIn(blnctrAccLists, 900, "a.blnctr_acc"));
        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccSubjectDTO.class);
    }
    /**
     * 根据 corpIds 查询 fc_acc a join fc_subject b on a.subject_id  = b.id where a.corp_id in (:corpIds) ; 返回 a.blnctr_acc、a.acc_name、b.measure_unit
     *
     * @param corpIds
     * @return
     */
    public List<FcAccSubjectDTO> getAccNameAndMeasureUnitByCorpId(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.blnctr_acc as blnctrAcc, a.acc_name as accName, b.measure_unit as measureUnit from fc_acc a join fc_subject b on a.subject_id  = b.id where 1=1");
        if (ListUtils.isNotNullOrEmpty(corpIds)) {
            sql.append(" and ").append(getSQLIn(corpIds, 900, "a.corp_id"));
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccSubjectDTO.class);
    }
    public List<FcAccSubjectDTO> getAccSubjectDateByCorpIds(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.acc_name as accName, s.subject_name as subjectName, s.balance_way as balanceWay from fc_acc a ");
        sql.append(" join fc_subject s on a.subject_id = s.id where 1=1 ");
        if (ListUtils.isNotNullOrEmpty(corpIds)) {
            sql.append(" and ").append(getSQLIn(corpIds, 900, "a.corp_id"));
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccSubjectDTO.class);
    }
    public FcAccSubjectDTO getAccSubjectCurDateByAccount(String account) {
        String sql = " select a.acc_name as accName, b.measure_unit as measureUnit, a.cur_id as curId " +
                " from fc_acc a join fc_subject s on a.subject_id = s.id" +
                " where a.blnctr_acc = :account ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", account);
        return getBySQLAliasToBean(sql, params, FcAccSubjectDTO.class);
    }
    /**
     * 根据 account ，返回 measure_unit acc_name cur_id
     *
     * @param accounts
     * @return
     */
    public List<FcAccSubjectDTO> getAccSubjectCurDateByAccounts(List<String> accounts) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.acc_name as accName, b.measure_unit as measureUnit, a.cur_id as curId ");
        sql.append(" from fc_acc a ");
        sql.append(" join fc_subject b on a.subject_id = b.id where 1=1");
        if (ListUtils.isNotNullOrEmpty(accounts)) {
            sql.append(" and ").append(getSQLIn(accounts, 900, "a.blnctr_acc"));
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccSubjectDTO.class);
    }
    /**
     * 根据 accounts 查询 fc_acc 、 fc_corp_pool  、bt_currency
     *
     * @param accounts
     * @return
     */
    public List<FcAccCorpCurrencyDTO> getAccCorpPoolCurDataByAccounts(List<String> accounts) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.cur_id as curId, a.overdraft_sign as overdraftSign, a.overdraft_money as overdraftMoney, a.balance_way as balanceWay, ");
        sql.append(" a.net_id as netId, a.acc_type as accType, a.charge_acc as chargeAcc, a.subject_id as subjectId, b.pool_code as netCode, c.cur_code as curCode from fc_acc a ");
        sql.append(" join fc_corp_pool b on a.net_id = b.id");
        sql.append(" join bt_currency c on a.cur_id = c.id where 1=1");
        if (ListUtils.isNotNullOrEmpty(accounts)) {
            sql.append(" and ").append(getSQLIn(accounts, 900, "a.blnctr_acc"));
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccCorpCurrencyDTO.class);
    }
    /**
     * 根据 corpIds, isPool 查询返回 a.blnctr a.acc_name b.cur_name
     *
     * @param corpIds
     * @param isPool
     * @return
     */
    public List<FcAccSubjectDTO> getAccCurDateByCorpIdsAndPool(List<String> corpIds, String isPool) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.acc_name, b.cur_name from fc_acc a ");
        sql.append(" join bt_currency b on a.cur_id = b.id ");
        sql.append(" where 1=1 ");
        if (ListUtils.isNotNullOrEmpty(corpIds)) {
            sql.append(" and ").append(getSQLIn(corpIds, 900, "a.corp_id"));
        }
        if ("1".equals(isPool)) {
            sql.append(" and a.pool_sign = '1' ");
        } else {
            sql.append(" and a.pool_sign != '1' ");
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccSubjectDTO.class);
    }
    /**
     * 根据 corpIds, isPool 查询返回 a.blnctr_acc as blnctrAcc, a.acc_name as accName, a.corp_id as corpId, a.acc_type as accType, b.name as corpName
     *
     * @param corpIds
     * @param isPool
     * @return
     */
    public List<FcAccSubjectDTO> getAccSysCorpDateByCorpIdsAndPool(List<String> corpIds, String isPool) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.acc_name as accName, a.corp_id as corpId, a.acc_type as accType, b.name as corpName from fc_acc a ");
        sql.append(" join sys_corp b on a.corp_id = b.id where 1=1 ");
        if (ListUtils.isNotNullOrEmpty(corpIds)) {
            sql.append(" and ").append(getSQLIn(corpIds, 900, "a.corp_id"));
        }
        if ("1".equals(isPool)) {
            sql.append(" and a.pool_sign = '1' ");
        } else {
            sql.append(" and a.pool_sign != '1' ");
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccSubjectDTO.class);
    }
    /**
     * 根据 account, isPool 查询返回 a.blnctr a.acc_name b.cur_name
     *
     * @param account
     * @param isPool
     * @return
     */
    public List<FcAccSubjectDTO> getAccCurDateByAccountAndPool(String account, String isPool) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.acc_name, b.cur_name from fc_acc a ");
        sql.append(" join bt_currency b on a.cur_id = b.id ");
        sql.append(" where 1=1 ");
        sql.append(" and a.blnctr_acc = :account ");
        if ("1".equals(isPool)) {
            sql.append(" and a.pool_sign = '1' ");
        } else {
            sql.append(" and a.pool_sign != '1' ");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("account", account);
        return listBySQLAliasToBean(sql.toString(), map, FcAccSubjectDTO.class);
    }
    /**
     * 根据 corpIds 查询返回 a.blnctr a.acc_name b.cur_name
     *
     * @param corpIds
     * @return
     */
    public List<FcAccSubjectDTO> getAccCurDataByCorpIds(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.acc_name as accName, b.cur_name as curName from fc_acc a ");
        sql.append(" join bt_currency b on a.cur_id = b.id where 1=1 ");
        if (ListUtils.isNotNullOrEmpty(corpIds)) {
            sql.append(" and ").append(getSQLIn(corpIds, 900, "a.corp_id"));
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccSubjectDTO.class);
    }
    /**
     * 根据 corpIds 查询返回 a.blnctr a.acc_name b.cur_name
     *
     * @param netIds
     * @return
     */
    public List<FcAccSubjectDTO> getAccCurDataByNetIds(List<String> netIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.acc_name as accName, b.cur_name as curName from fc_acc a ");
        sql.append(" join bt_currency b on a.cur_id = b.id where 1=1 ");
        if (ListUtils.isNotNullOrEmpty(netIds)) {
            sql.append(" and ").append(getSQLIn(netIds, 900, "a.net_id"));
        }
        return listBySQLAliasToBean(sql.toString(), null, FcAccSubjectDTO.class);
    }
    public List<FcAccDTO> getNoticeAccount(String corpId, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.update_date as updateDate,t.blnctr_acc as blnctrAcc,");
        sql.append("t.acc_name as accName,t.net_id as netId,");
        sql.append("t.corp_id as corpId,t.cur_id as curId,");
        sql.append("t.subject_id as subjectId, f.sys_subject_code as sysSubjectCode, f.subject_name as subjectName,");
        sql.append("t.acc_type as accType, t.attribute_id as attributeId, t.nature_code_id as natureCodeId,");
        sql.append("t.bank_code as bankCode, t.delay_rate as DelayRate,");
        sql.append("t.balance_way as balanceWay, t.interest_sign as interestSign,");
        sql.append("t.interest_start_date as interestStartDate, t.interest_rate as interestRate,");
        sql.append("t.overdraft_sign as overdraftSign,");
        sql.append("t.overdraft_rate as overdraftRate,t.overdraft_money as overdraftMoney,");
        sql.append("t.contract_no as contractNo, t.deadline_date as deadlineDate,");
        sql.append("t.delay_deadline_date as delayDeadlineDate, t.overdue_rate as overdueRate,");
        sql.append("t.acc_for_deduct as accForDeduct, t.deduct_mode as deductMode,");
        sql.append("t.notify_type as notifyType, t.acc_state as accState,");
        sql.append("t.agreed_deposit_sign as agreedDepositSign, t.predraw_sign as predrawSign,");
        sql.append("t.if_compare as ifCompare, t.acc_id as accId,");
        sql.append("t.acc_start_date as accStartDate, t.operator_code as operatorCode,");
        sql.append("t.acc_open_time as accOpenTime, t.handle_charge as handleCharge,");
        sql.append("t.shortcut as shortcut, t.union_net as unionNet,");
        sql.append("t.union_acc as unionAcc, t.precation_acc as precationAcc,");
        sql.append("t.precation_corp as precationCorp, t.poundage_rate as poundageRate,");
        sql.append("t.charge_acc as chargeAcc, t.transact_way as transactWay ");
        sql.append(" from fc_acc t left join fc_subject f on t.subject_id=f.id ");
        sql.append(" where 1=1 ");
        sql.append(" and t.acc_type = '08' ");
        Map<String, String> params = Maps.newHashMap();
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_id = :corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id = :curId");
            params.put("curId", curId);
        }
        sql.append(" order by t.blnctr_acc ");
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }
    public List<FcAccDTO> getBlnctrAcc(String userId, String poolId, String corpId, String busType,
                                       List<String> listAccId,String accAuthSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id ,a.blnctr_acc as blnctrAcc, acc_name as accName,a.cur_id AS curId,bc.cur_name AS curName");
        sql.append(" from fc_acc a ");
        sql.append(" LEFT JOIN bt_currency bc ON bc.id=a.cur_id");
        sql.append(" where 1=1 ");
//        if (ListUtils.isNotNullOrEmpty(listAccId)) {
//            sql.append(" and ").append(getSQLIn(listAccId, 900, "a.id"));
//        }
        if (busType.equals("0")) {
            /*登记查有效的企业活期账号*/
            sql.append(" and not ").append(getSQLIn(listAccId, 900, "a.id"));
            //N-正常状态
            sql.append(" and acc_State='" + InnerAccountConstant.ACC_STATE_NORMAL + "'");
        } else if (busType.equals("1")) {
            /*延期查登记审批通过的企业活期账号*/
            sql.append(" and ").append(getSQLIn(listAccId, 900, "a.id"));
        } else {
            /*终止查登记审批通过的企业活期账号或延期审批通过的企业活期账号*/
            sql.append(" and ").append(getSQLIn(listAccId, 900, "a.id"));
        }
        sql.append(" and ").append(accAuthSql);
        HashMap<String, Object> params = Maps.newHashMap();
//        if (StringUtils.isNotEmpty(userId)) {
//            sql.append(" and a.create_by = :userId");
//            params.put("userId", userId);
//        }
//        if (StringUtils.isNotEmpty(poolId)) {
//            sql.append(" and t.pool_id = :poolId");
//            params.put("poolId", poolId);
//        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and a.corp_id = :corpId");
            params.put("corpId", corpId);
        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }
    public List<FcAccDTO> getBlnctrAccByCorpId(String userName, String corpId, String busType, List<String> listAccId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" fa.id, fa.CUR_ID  as curId, fa.blnctr_acc as blnctrAcc, fa.acc_name as accName, bc.CUR_NAME  as curName ");
        sql.append(" from fc_acc fa left join bt_currency  bc on fa.CUR_ID  = bc.ID ");
        sql.append(" where 1=1 and fa.ACC_TYPE ='01' ");
        if (CollectionUtil.isNotEmpty(listAccId)) {
            if("0".equals(busType)){
                sql.append(" and ").append(getSQLNotIn(listAccId, 900, "fa.id"));
                sql.append(" and fa.acc_State='" + InnerAccountConstant.ACC_STATE_NORMAL + "'");
            }else {
                sql.append(" and ").append(getSQLIn(listAccId, 900, "fa.id"));
                sql.append(" and fa.acc_State='" + InnerAccountConstant.ACC_STATE_NORMAL + "'");
            }
        }
        HashMap<String, Object> params = Maps.newHashMap();
//        if (StringUtils.isNotEmpty(userName)) {
//
//            sql.append(" and trim(fa.create_by) = :username");
//            params.put("username", userName);
//        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and fa.corp_id = :corpId");
            params.put("corpId", corpId);
        }
        if ("disable".equals(busType)) {
            //N-正常状态
            sql.append(" and fa.acc_State='" + InnerAccountConstant.ACC_STATE_NORMAL + "'");
        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }
    /**
     * 根据 netId blnctrAccs 查询返回 k=blnctrAcc, v=accName
     *
     * @param netId
     * @param blnctrAccs
     * @return
     */
    public List<FcAccDTO> getAccNameByNetIdAndBlnctrAccs(String netId, List<String> blnctrAccs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, acc_name as accName from fc_acc a ");
        sql.append(" where a.net_id = :netId ");
        if (ListUtils.isNotNullOrEmpty(blnctrAccs)) {
            sql.append(" and ").append(getSQLIn(blnctrAccs, 900, "a.blnctr_acc"));
        }
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("netId", netId);
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }

    public List<FcAccDTO> getBlnctrAccByCorpId(String userId, String corpId, String accountType, String permission) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, acc_name as accName from fc_acc a ");
        sql.append(" where 1=1");
        HashMap<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(userId)) {
            sql.append(" and a.create_by = :userId ");
            params.put("userId", userId);
        }
        if (StringUtils.isNotBlank(corpId)) {
            sql.append(" and a.corpId = :corpId ");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotBlank(accountType)) {
            sql.append(" and a.acc_type = :accountType ");
            params.put("accountType", accountType);
        }
//        if (StringUtils.isNotBlank(permission)) {
//            sql.append(" and a.permission = :permission ");
//            params.put("permission", permission);
//        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }


    /**
     * 根据 netId accType 查询 fc_acc, 返回 blnctrAcc 集合
     *
     * @param netId
     * @param accType
     * @return
     */
    public List<String> getBlnctrAccByNetIdAndAccType(String netId, String accType) {
        String sql = " select a.blnctr_acc from fc_acc a where 1=1 " +
                " and a.pool_sign != '1' " +
                " and a.blnctr_acc is not null" +
                " and a.net_id = :netId " ;

        Map<String, Object> params = new HashMap<>();
        params.put("netId", netId);
        if(!StringUtils.isBlank(accType)){
            sql = sql +  " and a.acc_type = :accType";
            params.put("accType", accType);
        }
        return createSQLQuery(sql, params).list();
    }
    /**
     * 根据netId 和 checker_code is not null，查询 fc_acc a
     *
     * @param netId
     * @return
     */
    public List<FcAccDTO> getFcAccByNetIdAndCheckerCodeIsNotNull(String netId) {
        String sql = " select a.blnctr_acc as blnctrAcc, a.cur_id as curId, a.subject_id as subjectId, a.acc_name as accName, a.balance_way as balanceWay, a.acc_start_date as accStartDate from fc_acc a " +
                " where a.checker_code is not null " +
                " and a.net_id = :netId ";
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("netId", netId);
        return listBySQLAliasToBean(sql, params, FcAccDTO.class);
    }
    /**
     * 根据netId 和 accType checker_code is not null，查询 fc_acc a
     *
     * @param netId
     * @param accType
     * @return
     */
    public List<FcAccDTO> getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(String netId, String[] accType) {
        HashMap<String, Object> params = Maps.newHashMap();
        String sql = " select a.blnctr_acc as blnctrAcc, a.cur_id as curId, a.subject_id as subjectId, a.acc_name as accName, a.balance_way as balanceWay, a.acc_start_date as accStartDate from fc_acc a " +
                " where a.checker_code is not null " +
                " and a.net_id = :netId ";
        if(accType.length > 0){
            sql = sql + " and a.acc_type in(:accType)";
            params.put("accType", accType);
        }
        params.put("netId", netId);
        return listBySQLAliasToBean(sql, params, FcAccDTO.class);
    }

    public List<FcAccDTO> getFcAccByAccountAndNetIdAndAccTypeCheckerCodeIsNotNull(String account, String netId, String accType){
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from fc_acc a where a.checker_code is not null ");
        sql.append(" and a.net_id = :netId ");
        HashMap<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(account)){
            sql.append(" and a.blnctr_acc = :account");
            params.put("account",account);
        }
        if (StringUtils.isNotBlank(accType)){
            sql.append(" and a.acc_type = :accType");
            params.put("accType", accType);
        }
        params.put("netId", netId);

        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }

    public List<FcAccDTO> getFcAccByIdAndNotAccType(String netId, String[] accType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.cur_id as curId, a.subject_id as subjectId, a.acc_name as accName, ")
                .append(" a.balance_way as balanceWay, a.acc_type as accType, a.acc_start_date as accStartDate from fc_acc a ")
                .append(" where a.acc_state <> 'D' and a.interest_sign = '1' ")
                .append(" and a.net_id = :netCode ");
        if (!EmptyUtils.isEmpty(accType)) {
            sql.append(" and ").append(SqlUtil.getNotinSql(accType, "a.acc_type", true));
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netCode", netId);
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }

    public List<FcAccDTO> getFcAccByNetId(String netId) {
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("netId", netId);
        return listBySQLAliasToBean(" select * from fc_acc a where a.net_id = :netId ", params, FcAccDTO.class);
    }
    public List<String> getFcAccCurByNetId(String netId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("netId", netId);
        return createSQLQuery("select distinct CUR_ID from fc_acc where net_id = :netId order by CUR_ID asc").setProperties(params).list();
    }
    public List<Map<String,String>> getFcAccCurByNetIds(String netId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("netId", netId);
        return listBySQL("select distinct cur_id from fc_acc where net_id = :netId order by CUR_ID asc", params);
    }
    /**
     * 根据 netId and acc_state <> 'D' and interest_sign = '1' , 查询 fc_acc , 返回 blnctrAcc 集合
     *
     * @param netId
     * @return
     */
    public List<String> getBlnctrAccByNetId(String netId) {
        String sql = " select a.blnctr_acc as blnctrAcc from fc_acc a " +
                " where a.net_id = :netId and acc_state <> 'D' and interest_sign = '1' ";
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("netId", netId);
        List<FcAccDTO> fcAccDTOS = listBySQLAliasToBean(sql, params, FcAccDTO.class);
        List<String> result = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(fcAccDTOS)){
            fcAccDTOS.forEach(vo ->{
                if (!result.contains(vo.getBlnctrAcc())){
                    result.add(vo.getBlnctrAcc());
                }
            });
        }
        return result;
    }
    /**
     * 根据 netId and a.Interest_Sign = '1' and a.Acc_Type Not In ('05', '06', '08' ,'15','16'), 查询 fc_acc a,
     * 返回 a.blnctr_acc as blnctrAcc, a.Acc_Start_Date as accStartDate, a.cur_id as curId, a.acc_type as accType
     *
     * @param netId
     * @return
     */
    public List<FcAccDTO> getFcAccByNetIdAndOther(String netId) {
        String sql = "select a.blnctr_acc as blnctrAcc, a.Acc_Start_Date as accStartDate, a.cur_id as curId, a.acc_type as accType from fc_acc a" +
                "where a.Interest_Sign = '1' and a.Acc_Type Not In ('05', '06', '08' ,'15','16') and a.net_id = :netId ";
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("netId", netId);
        return listBySQLAliasToBean(sql, params, FcAccDTO.class);
    }
    public List<FcAccDTO> getBlnctrAccAndAccNameByNetId(String netId) {
        String sql = " select a.blnctr_acc as blnctrAcc, acc_name as accName from fc_acc a " +
                " where a.net_id = :netId ";
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("netId", netId);
        return listBySQLAliasToBean(sql, params, FcAccDTO.class);
    }
    /**
     * 根据条件 c.acc_type <> :accType and c.NET_ID = :netId,查询 fc_acc c ，返回 c.BLNCTR_ACC c.BALANCE_WAY
     *
     * @param netId
     * @param accType
     * @return
     */
    public List<FcAccDTO> getBlnctrAccAndBalanceWayByNetIdAndNotAccType(String netId, String accType) {
        String sql = " select a.blnctr_acc as blnctrAcc, balance_way as balanceWay from fc_acc a " +
                " where a.acc_type <> :accType and a.NET_ID = :netId ";
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("netId", netId);
        params.put("accType", accType);
        return listBySQLAliasToBean(sql, params, FcAccDTO.class);
    }
    /**
     * 根据 netId、compare, 查询返回 blnctrAcc 集合
     *
     * @param netId
     * @param compare
     * @return
     */
    public List<String> getBlnctrAccByNetIdAndIfCompare(String netId, String compare) {
        String sql = " select a.blnctr_acc as blnctrAcc from fc_acc a " +
                " where a.if_compare = :compare and a.NET_ID = :netId ";
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("netId", netId);
        params.put("compare", compare);
        return createSQLQuery(sql.toString()).setProperties(params).list();
    }
    public List<FcAccDTO> getInterestStartDate(List accountList) {
        String sql = " select a.blnctr_acc as blnctrAcc, a.interest_start_date as interestStartDate from fc_acc a " +
                " where a.acc_state <> 'D' and a.interest_sign = '1' " +
                " and " + getSQLIn(accountList, 900, "a.blnctr_acc");
        return listBySQLAliasToBean(sql, null, FcAccDTO.class);
    }
    public List<FcAccDTO> getInterestStartDate(String netId, String[] accTypeCode, String... sysSubjectFilters) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as blnctrAcc, a.interest_start_date as interestStartDate from fc_acc a ");
        sql.append(" where a.acc_state <> 'D' and a.interest_sign = '1' ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(netId)) {
            sql.append(" and a.net_id = :netCode ");
            params.put("netCode", netId);
        }
        if (accTypeCode != null && accTypeCode.length > 0) {
            sql.append(" and a.acc_type in (:accTypeCode) ");
            params.put("accTypeCode", accTypeCode);
        }
        if (sysSubjectFilters != null && sysSubjectFilters.length > 0) {
            sql.append(" and a.subject_id in (:subjectCodes) ");
            params.put("subjectCodes", sysSubjectFilters);
        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }
    public FcAccDTO getFcAccByCorpIdAndBlnctrAcc(String corpId, String blnctrAcc) {
        String sql = " select a.cur_id as curId, a.net_id as netId, a.pool_sign as poolSign from fc_acc a " +
                " where corp_Id = :corpId and a.blnctr_Acc = :blnctrAcc ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
        params.put("blnctrAcc", blnctrAcc);
        return getBySQLAliasToBean(sql, params, FcAccDTO.class);
    }
    public List<FcAccDTO> getFcAccByIds(List<String> ids) {
        String sql = " select * from fc_acc a where 1=1 " +
                " and " + getSQLIn(ids, 900, "a.id");
        return listBySQLAliasToBean(sql, null, FcAccDTO.class);
    }
    public List<FcAccDTO> getFcAccByBlnctrAccs(List<String> blnctrAccs) {
        String sql = " select * from fc_acc a  where 1=1 " +
                " and " + getSQLIn(blnctrAccs, 900, "a.blnctr_acc");
        return listBySQLAliasToBean(sql, null, FcAccDTO.class);
    }

    public List<FcAccDTO> getFcAccByBlnctrAccsAndAcctype(List<String> blnctrAccs, Map<String, String> queryParams) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select * from fc_acc a  where 1=1 " );
        if (ListUtils.isNotNullOrEmpty(blnctrAccs)){
            sql.append("and a.blnctr_acc in (:blnctrAccs)");
            params.put("blnctrAccs", blnctrAccs);
        }
        String accTypeCode = queryParams.get("accTypeCode");
        if (EmptyUtils.isNotEmpty(queryParams.get("accTypeCode"))) {
            sql.append("and a.acc_type in (:accTypeCode) ");
            params.put("accTypeCode", accTypeCode.split(","));
        }
        String netId = queryParams.get("netId");
        if(StringUtils.isNotBlank(netId) ){
            sql.append("and a.net_id = :netId ");
            params.put("netId", netId);

        }
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }
    public List<Map<String, Object>> getLoanAccByCorpCode(String corpId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isEmpty(corpId)) {
            return null;
        }
        sql.append("select acc.blnctr_acc as ACC from fc_acc acc where acc.acc_type=:accType ");
        sql.append("and acc.corp_id = :corpId ");
        sql.append("and acc.acc_state= :accState ");
        params.put("accType", InnerAccountConstant.FCACC_LOAN);
        params.put("corpId", corpId);
        params.put("accState", InnerAccountConstant.ACC_STATE_NORMAL);
        return createSQLQuery(sql.toString(), params).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
    public List<Map<String, Object>> getCorpCurAccInfo(String loanAcc) {
        if (StringUtils.isEmpty(loanAcc)) {
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select a.blnctr_acc as account from fc_acc a, fc_acc b where a.corp_id = b.corp_id " +
                "and a.cur_id = b.cur_id " +
                "and a.acc_State in ('" + InnerAccountConstant.ACC_STATE_NORMAL + "', '" + InnerAccountConstant.ACC_STATE_PART_FREEZE + "') " +
                "and a.acc_type = :accType " +
                "and b.blnctr_acc = :loanAcc ";
        params.put("accType", InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
        params.put("loanAcc", loanAcc);
        return createSQLQuery(sql, params).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
    public List<String> getBlnctrAccByNetCodeAndAccType(String netCode, String accType) {
        String sql = "select a.blnctr_acc from fc_acc a where a.net_id = :netCode and a.acc_type = :accTypeCode";
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("netCode", netCode);
        params.put("accTypeCode", accType);
        return createSQLQuery(sql).setProperties(params).list();
    }
    /**
     * @param corpId
     * @param accType
     * @param subjectId
     * @return
     */
    public List<FcAccDTO> findNeedInterestAcc(String corpId, String accType, String subjectId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("subjectId", subjectId);
        params.put("accType", accType);
        params.put("corpId", corpId);
        params.put("accState", InnerAccountConstant.ACC_STATE_NORMAL);
        List<FcAccDTO> acc = listBySQLAliasToBean("select acc.* from fc_acc acc where acc.subject_id = :subjectId and acc.acc_type = :accType and acc.corp_id = :corpId and acc.acc_state = :accState ", params, FcAccDTO.class);
        return acc;
    }
    /**
     *
     * @param netCode
     * @param accTypeCode
     * @param endInstDate
     * @return
     */
    public List<Map<String, Object>> queryAccuBalanceForNone(String account, String netCode, String[] accTypeCode, Date endInstDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc as account, a.cur_id as curCode,a.interest_sign as interestSign,");
        sql.append(" a.predraw_sign as preSign,a.interest_start_date as interestTime ");
        sql.append(" FROM fc_acc a ");
        /*sql.append(" left join sys_corp s on a.net_id = s.id ");
        sql.append(" left join bt_currency cur on a.cur_id = cur.id ");
        sql.append(" left join bt_bank bank  on bank.id = a.bank_code ");*/
        sql.append(" where  a.acc_State  IN  ('N','I')  and  a.interest_Sign  =  '1'  and  a.acc_Start_Date  <  :endInstDate  ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("endInstDate", endInstDate);
        if (StringUtils.isNotBlank(netCode)) {
            sql.append("  and  a.net_id  =  :netCode  ");
            params.put("netCode", netCode);
        }
        if (StringUtils.isNotBlank(account)) {
            sql.append("  and  a.blnctr_acc  =  :account  ");
            params.put("account", account);
        }
        if (accTypeCode != null && accTypeCode.length > 0) {
            sql.append("  and  a.acc_Type  in  (:accTypeCode)  ");
            params.put("accTypeCode", accTypeCode);
        }
        return listBySQL(sql.toString(), params);
    }
    public List<FcAccPlusDTO> getTallyAccTypeAccList(String netId, String corpId, String curId, List<String> accList) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.blnctr_acc,t.acc_name,cur.cur_name,f.sys_subject_code,f.subject_name ");
        sql.append("from fc_acc t left join  fc_Subject f on t.subject_id=f.id ");
        sql.append("left join sys_corp net on t.net_id=net.id "); // 放到基础模块
        sql.append("left join sys_corp corp on t.corp_id=corp.id "); // 放到基础模块
        sql.append("left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        sql.append("and t.acc_state in('N','P','S') and f.sys_Subject_Code = :sysSubjectCode ");
        if (EmptyUtils.isNotEmpty(netId)) {
            sql.append("and t.net_id=:netId ");
            params.put("netId", netId);
        }
        if (EmptyUtils.isNotEmpty(corpId)) {
            sql.append("and t.corp_Id=:corpId ");
            params.put("corpId", corpId);
        }
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append("and t.cur_id=:curId ");
            params.put("curId", curId);
        }
        sql.append("and " + getSQLIn(accList, 900, "t.id"));
        sql.append(" order by t.blnctr_acc,t.acc_open_time ");
        params.put("sysSubjectCode",SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get());
        return listBySQLAliasToBean(sql.toString(), params, FcAccPlusDTO.class);
    }
    public List<FcAccDTO> queryBySomeCondPgd(String condition) {
        String sb = " select t.id , t.blnctr_acc as blnctrAcc,t.acc_name as accName,t.acc_type as accType,s.id as corpId,s.code AS corpCode,s.name AS corpName,t.net_id as netId,t.interest_Start_Date as interestStartDate, t.deadline_Date as deadlineDate," +
                " t.cur_id AS curId,bc.cur_code as curCode,bc.cur_name as curName,f.sys_subject_code as sysSubjectCode, f.subject_name as subjectName, t.shortCut as shortCut,t.notify_Type as notifyType,t.interest_rate as interestRate " +
                " from Fc_Acc t left join fc_Subject f on t.subject_id = f.id " +
                " left join bt_currency bc on t.cur_id = bc.id " +
                " left join sys_corp s on s.id = t.corp_id " +
                " left join bis_acc_attribute ba on t.attribute_id = ba.id " +
                " where 1 = 1 " +
                condition;
        return listBySQLAliasToBean(sb, null, FcAccDTO.class);
    }

    public List<FcAccDTO> listFcAccByNetId(String netId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct a.blnctr_acc as blnctrAcc,a.acc_name as accName,a.balance_way as balanceWay,");
        sql.append(" a.net_id as netId,a.subject_id as subjectId,a.cur_id as curId ");
        sql.append(" from fc_acc a ");
        sql.append(" where a.net_id = :netId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", netId);
        return listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }

    public List<FcAccDTO> listAllFcAcc() {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct a.blnctr_acc as blnctrAcc,a.acc_name as accName,a.balance_way as balanceWay,");
        sql.append(" a.net_id as netId,a.subject_id as subjectId,a.cur_id as curId ");
        sql.append(" from fc_acc a ");
        return listBySQLAliasToBean(sql.toString(), null, FcAccDTO.class);
    }
    public List<FcAccDTO> getBlnctrAccByUserId(String userId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> param = Maps.newHashMap();
        sql.append("select distinct fa.BLNCTR_ACC as  blnctrAcc , fa.NET_ID as netId ");
        sql.append(" from fc_acc fa left join sys_user su on fa.CORP_ID = su.CORP_ID ");
        sql.append(" where fa.ACC_STATE <> :accState");
        sql.append(" and su.ID = :userId");
        param.put("userId",userId);
        param.put("accState",InnerAccountConstant.ACC_STATE_DESTORY);
        return listBySQLAliasToBean(sql.toString(), param, FcAccDTO.class);
    }

    public List<String> getFcAccByLikeBlnctrAccAndNetIdAndCorpIdAndAccType(String blnctrAccParam, String netId, String[] corpIds, String accType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.blnctr_acc from fc_acc a where 1=1 and a.pool_sign != '1' ");
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotBlank(blnctrAccParam)){
            sql.append(" and a.blnctr_acc like '%" + blnctrAccParam + "%'");
        }
        if (StringUtils.isNotBlank(netId) ){
            sql.append(" and a.net_id = :netId ");
            params.put("netId", netId);
        }

        if (corpIds != null && corpIds.length > 0) {
            sql.append(" and ").append(SqlUtil.getInSql(corpIds,"a.corp_Id", true));
        }

        if(!StringUtils.isBlank(accType)){
            sql.append(" and a.acc_type = :accType ");
            params.put("accType", accType);
        }
        return createSQLQuery(sql.toString(), params).list();
    }

    public List<FcAccDTO> getFcAccByInterest() {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.acc_Type as accType,");
        sql.append("t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime,t.pool_sign as poolSign ");
        sql.append(" from fc_acc t where t.INTEREST_SIGN ='1' and t.acc_state not in ('D')");
        return listBySQLAliasToBean(sql.toString(), null, FcAccDTO.class);
    }
}
