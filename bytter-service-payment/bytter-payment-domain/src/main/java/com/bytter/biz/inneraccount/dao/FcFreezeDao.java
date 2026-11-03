package com.bytter.biz.inneraccount.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.inneraccount.entity.FcFreeze;
import com.bytter.biz.inneraccount.vo.FcFreezeListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeSearchListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.dto.FcFreezeDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 内部账号冻结解冻
 * @date 2017-06-16 17:31:26
 */
@Repository
public class FcFreezeDao extends BaseDao<FcFreeze, String> {
    /**
     * 冻结
     *
     * @param dataTablesInput
     * @param loginName
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String loginName, String joinSql, Map<String, Object> params) {
        Map<String, Object> paramsAll = new HashMap<String, Object>();
        StringBuilder condition = new StringBuilder();
        condition.append(joinSql);
        String opType = "S";
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("opType"))) {
            opType = dataTablesInput.getFormdata().get("opType").toString();
        }
        String sql = findByOpType(condition, opType,dataTablesInput);
        if (CommonConstant.SUBMIT_SEARCH.equals(opType)) {
            if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqi_status"))) {
                paramsAll.put("STATUS",  dataTablesInput.getFormdata().get("eqi_status").toString());
            }else {
                paramsAll.put("STATUS", new Integer[]{CommonConstant.STATUE_WAIT_CHECK, CommonConstant.STATUE_CHECK});
            }
            paramsAll.put("operator", loginName);
        } else if (CommonConstant.SUBMIT_CHECK.equals(opType)) {
            paramsAll.put("STATUS", CommonConstant.STATUE_WAIT_CHECK);
            paramsAll.put("operator", loginName);
        }
        paramsAll.put("freezeBusType", InnerAccountConstant.BUS_TYPE_FREEZE);
        paramsAll.putAll(params);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcFreezeListVO.class, null, false, paramsAll);
    }
    /**
     * 冻结及冻结复核列表sql
     *
     * @param condition
     * @param opType
     * @return
     */
    private String findByOpType(StringBuilder condition, String opType,DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonSql());
        if (CommonConstant.SUBMIT_SEARCH.equals(opType)) {
            sql.append(" FROM FC_FREEZE FREEZE left join fc_acc fa on FREEZE.BLNCTR_ACC = fa.BLNCTR_ACC WHERE ");
            if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqi_status"))) {
                sql.append(" FREEZE.STATUS =:STATUS ");
            }else {
                sql.append(" FREEZE.STATUS NOT IN(:STATUS) ");
            }
            sql.append(" AND FREEZE.FREEZE_OPERATOR=:operator AND FREEZE.FREEZE_SIGN=:freezeBusType ");
        } else if (CommonConstant.SUBMIT_CHECK.equals(opType)) {
            sql.append(" FROM FC_FREEZE FREEZE WHERE FREEZE.STATUS=:STATUS");
            sql.append(" AND FREEZE.FREEZE_OPERATOR!=:operator AND FREEZE.FREEZE_SIGN=:freezeBusType ");
        }
        sql.append(getCanFreezeFromSQL(condition.toString(), false, false));
        return sql.toString();
    }
    private String searchForCommonByType(StringBuilder condition, String opType) {
        StringBuilder sql = new StringBuilder();
        String fromExp = "";
        StringBuilder conditionEqual = new StringBuilder(" AND MAINDATA.blnctrAcc=FREEZE.BLNCTR_ACC AND FREEZE.FREEZE_OPERATOR=:operator AND FREEZE.FREEZE_SIGN=:freezeBusType ");
        String tableTemp = ") MAINDATA,FC_FREEZE FREEZE WHERE ";
        if (CommonConstant.SUBMIT_ADD.equals(opType)) {//冻结页面查询
            condition.append(" AND NOT EXISTS(SELECT * FROM FC_FREEZE FREEZE WHERE (FREEZE.STATUS != " + CommonConstant.STATUE_CHECK + " OR FREEZE.FREEZE_TYPE='" + InnerAccountConstant.FREEZE_TYPE_ALL + "') AND FREEZE_END_SIGN = "
                    + InnerAccountConstant.BOOLEAN_TYPE_NO + " AND fcAcc.blnctr_acc = FREEZE.BLNCTR_ACC AND FREEZE.FREEZE_SIGN=:freezeBusType ) ");
            fromExp = getCanFreezeFromSQL(condition.toString(), true, true);
            sql.append(getFreezePreQueryExpression());//"SELECT blnctrAcc ,accName ,netId,curId,corpId");
            sql.append(fromExp);
        } else {
            fromExp = getCanFreezeFromSQL(null, false, false);
        }
        if (CommonConstant.SUBMIT_MODIFY.equals(opType)) {//冻结修改页面查询
            sql.append(getCommonSql());
            sql.append(fromExp);
            sql.append(tableTemp);
            sql.append(" FREEZE.STATUS NOT IN(:STATUS)");
            sql.append(conditionEqual);
            if (StringUtils.isNotEmpty(condition)) {
                sql.append(condition);
            }
        } else if (CommonConstant.SUBMIT_CHECK.equals(opType)) {//冻结复核页面查询
            sql.append(getCommonSql());
            sql.append(fromExp);
            sql.append(") MAINDATA,FC_FREEZE FREEZE WHERE FREEZE.STATUS=:STATUS");
            sql.append(" AND MAINDATA.blnctrAcc=FREEZE.BLNCTR_ACC AND FREEZE.FREEZE_OPERATOR!=:operator AND FREEZE.FREEZE_SIGN=:freezeBusType ");
            if (StringUtils.isNotEmpty(condition)) {
                sql.append(condition);
            }
        } else if (CommonConstant.SUBMIT_SEARCH.equals(opType)) {
            sql.append("select MAINDATA.accName as accName,MAINDATA.blnctrAcc as blnctrAcc,netId,curId,corpId,FREEZE.id as id,FREEZE.FREEZE_MONEY as freezeMoney,");
            sql.append("MAINDATA.freezeMoney as accuFreezeMoney,FREEZE.FREEZE_TYPE as freezeType,FREEZE.BEGIN_DATE as beginDate,FREEZE.END_DATE as endDate,FREEZE.Status ");
            sql.append(" FROM (SELECT fcAcc.* ");
            sql.append(fromExp);
            if (StringUtils.isNotEmpty(condition)) {
                sql.append(condition);
            }
            sql.append(tableTemp);
            sql.append("FREEZE.STATUS NOT IN(:STATUS)");
            sql.append(conditionEqual);
        } else if (CommonConstant.SUBMIT_VIEW.equals(opType)) {
            sql.append(getCommonSql());
            sql.append(fromExp);
            sql.append(") MAINDATA,FC_FREEZE FREEZE WHERE 1=1 ");
            sql.append(" AND MAINDATA.blnctrAcc=FREEZE.BLNCTR_ACC");
            if (StringUtils.isNotEmpty(condition)) {
                sql.append(condition);
            }
        }
        return sql.toString();
    }
    /**
     * 冻结通用查询表达式
     *
     * @return
     */
    private String getFreezePreQueryExpression() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT fcAcc.blnctr_acc as blnctrAcc,fcAcc.acc_name as accName,fcAcc.net_id as netId,fcAcc.cur_id as curId,fcAcc.corp_id as corpId ");
        sb.append(" from fc_acc fcAcc where 1 = 1  ");
        //sb.append("activeMoney,freezeMoney ");
        return sb.toString();
    }
    /**
     * 通用部分
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select FREEZE.BLNCTR_ACC as blnctrAcc,FREEZE.id as id,FREEZE.FREEZE_MONEY as freezeMoney,FREEZE.freeze_No as freezeNo,");
        sql.append("FREEZE.FREEZE_TYPE as freezeType,FREEZE.BEGIN_DATE as beginDate,FREEZE.END_DATE as endDate,FREEZE.Status, ");
        sql.append("FREEZE.EXEC_OFFICIAL_NO as execOfficialNo,FREEZE.EXEC_CARD_NO as execCardNo,FREEZE.FREEZE_REASON as freezeReason,FREEZE.freeze_Org as freezeOrg,FREEZE.REFUSE_WHY as refuseWhy ");
        return sql.toString();
    }
    private String getCanFreezeFromSQL(String condition, boolean isOrderBy, boolean isState) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(condition)) {
            sb.append(condition);
        }
        if (isOrderBy) {
            sb.append(" ORDER BY blnctr_Acc ");
        }
        return sb.toString();
    }
    /**
     * 查询冻结数据
     *
     * @return
     */
    public List<FcFreezeListVO> getAllFreeBydBusType(String busType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("freezeBusType", busType);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM FC_FREEZE FREEZE WHERE (FREEZE.STATUS != " + CommonConstant.STATUE_CHECK + " OR FREEZE.FREEZE_TYPE='" +
                InnerAccountConstant.FREEZE_TYPE_ALL + "') AND FREEZE_END_SIGN = "
                + InnerAccountConstant.BOOLEAN_TYPE_NO + " AND FREEZE.FREEZE_SIGN=:freezeBusType ");
        return listBySQLAliasToBean(sql.toString(), params, FcFreezeListVO.class);
    }
    /**
     * 根据账号查
     *
     * @param vo
     * @param sql
     * @param params
     * @return
     */
    public List<FcFreezeVO> getFcFreezeVOList(FcFreezeVO vo,String sql,Map<String,Object> params) {
        StringBuilder hql = new StringBuilder(" select t.id as id,t.freeze_no as freezeNo,t.begin_date as beginDate,t.end_date as endDate,t.freeze_money as freezeMoney,t.freeze_type as freezeType,t.freeze_reason as freezeReason ");
        hql.append(" from Fc_freeze t where 1=1 ");
        if (StringUtils.isNotEmpty(sql)) {
            hql.append(sql);
        }
        if(StringUtils.isNotEmpty(vo.getBlnctrAcc())){
            hql.append(" and t.blnctr_acc=:blnctrAcc");
            params.put("blnctrAcc", vo.getBlnctrAcc());
        }
        if(StringUtils.isNotEmpty(vo.getStatus())){
            hql.append(" and t.status=:status");
            params.put("status", vo.getStatus());
        }
        if(StringUtils.isNotEmpty(vo.getFreezeSign())){
            hql.append(" and t.freeze_sign=:freezeSign");
            params.put("freezeSign", vo.getFreezeSign());
        }
        List<FcFreezeVO> list = listBySQLAliasToBean(hql.toString(), params, FcFreezeVO.class);
        return list;
    }
    /**
     * 根据账号查历史记录
     *
     * @param account
     * @return
     */
    public List<FcFreezeVO> getHisListByAccount(String account) {
        StringBuilder hql = new StringBuilder(" select t.freezeNo as freezeNo,t.beginDate as beginDate,t.endDate as endDate,t.freezeMoney as freezeMoney,t.freezeType as freezeType,t.freezeReason as freezeReason from FcFreeze t");
        hql.append(" where t.blnctrAcc=:blnctrAcc and t.status=:status and t.freezeSign=:freezeSign");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("blnctrAcc", account);
        params.put("status", CommonConstant.STATUE_CHECK);
        params.put("freezeSign", InnerAccountConstant.BUS_TYPE_FREEZE);
        List<FcFreezeVO> list = listByHQLAliasToBean(hql.toString(), params, FcFreezeVO.class, false);
        return list;
    }
    public FcFreezeListVO getEntityById(String id) {
        StringBuilder condition = new StringBuilder();
        condition.append(" and FREEZE.id=:id");
        String sql = searchForCommonByType(condition, CommonConstant.SUBMIT_VIEW);
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql, params, FcFreezeListVO.class);
    }
    /**
     * 根据id查当前实体对象
     *
     * @param id
     * @param opType
     * @param loginName
     * @return
     */
    public FcFreezeListVO getEntityById(String id, String opType, String loginName) {
        StringBuilder condition = new StringBuilder();
        condition.append(" and FREEZE.id=:id");
        String sql = searchForCommonByType(condition, opType);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("operator", loginName);
        params.put("freezeBusType", InnerAccountConstant.BUS_TYPE_FREEZE);
        if (CommonConstant.SUBMIT_CHECK.equals(opType)) {
            params.put("STATUS", CommonConstant.STATUE_WAIT_CHECK);
        } else {
            params.put("STATUS", new Integer[]{CommonConstant.STATUE_WAIT_CHECK, CommonConstant.STATUE_CHECK});
        }
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FcFreezeListVO.class);
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcFreezeVO getById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id ,t.status as status,t.un_freeze_id as unFreezeId,t.un_freeze_money as unFreezeMoney,");
        sql.append("t.freeze_no as freezeNo,t.blnctr_acc as blnctrAcc,t.freeze_sign as freezeSign,t.freeze_Type as freezeType,");
        sql.append("t.freeze_money as freezeMoney,t.accu_freeze_money as accuFreezeMoney,t.create_by as createBy,t.freeze_reason as freezeReason,");
        sql.append("t.checker_code as checkerCode,t.begin_Date as beginDate ,t.end_date as endDate,t.operate_time as operateTime,t.freeze_operator as freezeOperator,");
        sql.append("t.freeze_org as freezeOrg,t.exec_official_no as execOfficialNo,t.exec_card_no as execCardNo,t.cur_acc_state as curAccState,");
        sql.append("t.freeze_end_sign as freezeEndSign,t.create_date as createDate ");
        sql.append(" FROM Fc_Freeze t where 1=1 ");
//        sql.append(" and t.freeze_Sign = 'C' "); id精确查询不需要状态
        sql.append(" and t.id=:id ");
        Map map = new HashMap();
        map.put("id", id);
        return getBySQLAliasToBean(sql.toString(), map, FcFreezeVO.class);
    }
    /**
     * freeze通知书重复校验
     *
     * @param freeze
     * @return
     */
    public List<FcFreeze> checkNotifyNo(FcFreeze freeze) {
        StringBuilder hql = new StringBuilder(" from FcFreeze where freezeNo=:freezeNo and freezeSign=:freezeSign");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("freezeNo", freeze.getFreezeNo());
        params.put("freezeSign", freeze.getFreezeSign());
        if (StringUtils.isNotEmpty(freeze.getId())) {
            hql.append(" and id!=:id");
            params.put("id", freeze.getId());
        }
        return listByHQL(hql.toString(), params);
    }
    /**
     * 复核通过或者失败
     *
     * @param fcFreeze
     * @param status    复核通过  ,复核打回
     * @param oldStatus 复核前的状态
     * @return
     */
    public int check(FcFreeze fcFreeze, Integer status, Integer oldStatus) {
        StringBuilder hql = new StringBuilder();
        hql.append(" update FcFreeze set checkerCode=:checkerCode ,checkTime =:checkTime,status=:status ,");
        hql.append(" refuseWhy=:refuseWhy,accuFreezeMoney=:accuFreezeMoney,updateBy=:updateBy,");
        hql.append(" updateDate=:updateDate where id=:id and status=:oldStatus");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("checkerCode", fcFreeze.getCheckerCode());
        params.put("checkTime", fcFreeze.getCheckTime());
        params.put("refuseWhy", fcFreeze.getRefuseWhy());
        params.put("accuFreezeMoney", fcFreeze.getAccuFreezeMoney());
        params.put("status", status);
        params.put("id", fcFreeze.getId());
        params.put("oldStatus", oldStatus);
        params.put("updateBy", fcFreeze.getCheckerCode());
        params.put("updateDate", fcFreeze.getCheckTime());
        return getSession().createQuery(hql.toString()).setProperties(params).executeUpdate();
    }
    /**
     * 内部账号冻结解冻查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTableAll(DataTablesInput dataTablesInput, String joinSql, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id ,t.status as status,");
        sql.append(" t.freeze_No as freezeNo,ut.freeze_no as oldFreezeNo,");
        sql.append(" t.blnctr_acc as blnctrAcc,t.freeze_sign as freezeSign,t.freeze_Type as freezeType,");
        sql.append(" t.freeze_money as freezeMoney,t.accu_freeze_money as accuFreezeMoney,t.create_by as createBy,");
        sql.append(" t.checker_code as checkerCode,t.begin_Date as beginDate ,t.end_date as endDate,t.operate_time as operateTime");
        sql.append(" FROM Fc_Freeze t ");
        sql.append(" left join Fc_Freeze ut on t.un_freeze_id= ut.id ");
        sql.append(" left join fc_acc f on t.BLNCTR_ACC = f.BLNCTR_ACC  ");
        sql.append("where 1=1 ");
        sql.append(joinSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcFreezeSearchListVO.class, null, false, params);
    }
    /**
     * 查询网点冻结 到期账户
     *
     * @param blnctrAccs
     * @param endDate
     * @param deductSign
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<FcFreezeSearchListVO> getList(List<String> blnctrAccs, Date endDate, String deductSign) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id ,t.status as status,");
        sql.append("t.freeze_no as freezeNo,t.blnctr_acc as blnctrAcc,t.freeze_sign as freezeSign,t.freeze_Type as freezeType,");
        sql.append("t.freeze_money as freezeMoney,t.accu_freeze_money as accuFreezeMoney,t.create_by as createBy,");
        sql.append("t.checker_code as checkerCode,t.begin_Date as beginDate ,t.end_date as endDate,t.operate_time as operateTime");
        sql.append(" FROM Fc_Freeze t where 1=1 ");
        sql.append(" and t.freeze_Sign = 'C' ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (CollectionUtils.isNotEmpty(blnctrAccs)) {
            sql.append(" and t.blnctr_acc in (:blnctrAccs)");
            params.put("blnctrAccs", blnctrAccs);
        }
        if (StringUtils.isNotEmpty(endDate)) {
            sql.append(" and t.end_date <= :endDate");
            params.put("endDate", endDate);
        }
        if (CommonConstant.SEND_SIGN_NO.equals(deductSign)) {
            sql.append(" and (t.deduct_Sign is null or t.deduct_Sign = '0')");
        }
        sql.append(" order by t.end_date asc");
        return listBySQLAliasToBean(sql.toString(), params, FcFreezeSearchListVO.class);
    }


    public List<FcFreezeSearchListVO> getFreezeDueList(String netId, Date endDate, String deductSign) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id ,f.corp_id as corpId,f.net_id as netId,f.cur_id as curId,t.status as status,");
        sql.append("f.acc_name as accName,t.freeze_no as freezeNo,t.blnctr_acc as blnctrAcc,t.freeze_sign as freezeSign,t.freeze_Type as freezeType,");
        sql.append("t.freeze_money as freezeMoney,t.accu_freeze_money as accuFreezeMoney,t.create_by as createBy,");
        sql.append("t.checker_code as checkerCode,t.begin_Date as beginDate ,t.end_date as endDate,t.operate_time as operateTime");
        sql.append(" FROM Fc_Freeze t inner join fc_Acc f on t.blnctr_acc=f.blnctr_acc where 1=1 ");
        sql.append(" and t.freeze_Sign = 'C' ");
        Map<String, Object> params = new HashMap<String, Object>();


        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and f.net_Id = :netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(endDate)) {
            sql.append(" and t.end_date <= :endDate");
            params.put("endDate", endDate);
        }
        if (CommonConstant.SEND_SIGN_NO.equals(deductSign)) {
            sql.append(" and (t.deduct_Sign is null or t.deduct_Sign = '0')");
        }

        sql.append(" order by t.end_date asc");
        return listBySQLAliasToBean(sql.toString(), params, FcFreezeSearchListVO.class);
    }
    // FcUnFreezeDao----------------------
    /**
     * 解冻查询列表
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput findCanModifyUnFreezes(DataTablesInput dataTablesInput, String joinSql,Map<String,Object> params) {
        StringBuilder sb = new StringBuilder(" AND FREEZE.FREEZE_SIGN=:freezeSign AND FREEZE.STATUS NOT IN(:status) ");
        sb.append(joinSql);
        params.put("freezeSign",InnerAccountConstant.BUS_TYPE_UNFREEZE);
        params.put("status",new Integer[]{CommonConstant.STATUE_CHECK,CommonConstant.STATUE_WAIT_CHECK});
        String sql = unFreezeQuery(sb.toString());
        return getDynamicBySQL(dataTablesInput, sql, FcFreezeVO.class, "", false,params);
    }
    private String unFreezeQuery(String unFreezeCondition) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT  ID as id,UN_FREEZE_ID as unFreezeId,BLNCTR_ACC as blnctrAcc,");
        sb.append("FREEZE_MONEY as freezeMoney,FREEZE_TYPE as freezeType,BEGIN_DATE as beginDate, END_DATE as endDate, ACCU_FREEZE_MONEY as accuFreezeMoney,");
        sb.append(" FREEZE_NO as freezeNo,STATUS as status,UN_FREEZE_MONEY as unFreezeMoney, ");
        sb.append(" EXEC_OFFICIAL_NO as execOfficialNo,EXEC_CARD_NO as execCardNo,FREEZE_REASON as freezeReason,FREEZE_ORG as freezeOrg");
        sb.append(" FROM (select FREEZE.ID,FREEZE.UN_FREEZE_ID,FREEZE.BLNCTR_ACC,");
        sb.append(" FREEZE.FREEZE_MONEY,FREEZE.exec_Official_No, FREEZE.exec_Card_No, FREEZE.freeze_Reason, FREEZE.freeze_Org,");
        sb.append("(FREEZE.FREEZE_MONEY - (SELECT BT_NVL_NUM(SUM(F.FREEZE_MONEY),0) FROM FC_FREEZE F WHERE F.FREEZE_SIGN = 'T' AND F.UN_FREEZE_ID = FREEZE.Id)) AS UN_FREEZE_MONEY, ");
        sb.append("FREEZE.FREEZE_TYPE,FREEZE.BEGIN_DATE,FREEZE.END_DATE,FREEZE.ACCU_FREEZE_MONEY,FREEZE.FREEZE_NO,FREEZE.STATUS " +
                "FROM FC_FREEZE FREEZE");
        sb.append(" WHERE 1=1 ");
        sb.append(unFreezeCondition);
        sb.append(") fcAcc WHERE 1=1 ");
        return sb.toString();
    }
    /**
     * 查询可解冻记录
     *
     * @param accList
     * @return
     */
    public List<FcFreezeListVO> findCanUnFreezes( List<String> accList) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder("AND FREEZE.FREEZE_SIGN=:freezeSign AND FREEZE.STATUS=:status AND FREEZE.FREEZE_END_SIGN !=:freezeEndSign ");
        if (CollectionUtil.isNotEmpty(accList)) {
            sb.append(" and ").append(SqlUtil.getInSql(accList, "FREEZE.BLNCTR_ACC", true));
        }
        sb.append(" AND (FREEZE.DEDUCT_SIGN !=:deductSign or FREEZE.DEDUCT_SIGN is null)");
        sb.append(" AND NOT EXISTS(SELECT * FROM FC_FREEZE SUB WHERE SUB.UN_FREEZE_ID = FREEZE.ID AND SUB.STATUS!=:status)");
        sb.append(" AND NOT EXISTS(SELECT * FROM FC_FREEZE SUB WHERE SUB.blnctr_acc = FREEZE.blnctr_acc AND SUB.STATUS!=:status");
        sb.append(" and sub.freeze_sign=:freezeSignUn and SUB.freeze_type in(:freezeType) )");

        params.put("freezeSign",InnerAccountConstant.BUS_TYPE_FREEZE);
        params.put("status",CommonConstant.STATUE_CHECK);
        params.put("freezeEndSign",InnerAccountConstant.BOOLEAN_TYPE_YES);
        params.put("deductSign",InnerAccountConstant.BOOLEAN_TYPE_YES);
        params.put("freezeSignUn",InnerAccountConstant.BUS_TYPE_UNFREEZE);
        params.put("freezeType",new String[]{InnerAccountConstant.FREEZE_TYPE_ALL,InnerAccountConstant.FREEZE_TYPE_S});
        String sql = unFreezeQuery(sb.toString());
        return listBySQLAliasToBean(sql, params, FcFreezeListVO.class);
    }
    /**
     * 解冻复核列表查询
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput findCanCheckUnFreezes(DataTablesInput dataTablesInput, String joinSql,Map<String,Object> params) {
        StringBuilder sb = new StringBuilder("AND FREEZE.FREEZE_SIGN =:freezeSign AND FREEZE.STATUS =:status ");
        sb.append(joinSql);
        String sql = unFreezeQuery(sb.toString());
        params.put("freezeSign",InnerAccountConstant.BUS_TYPE_UNFREEZE);
        params.put("status",CommonConstant.STATUE_WAIT_CHECK);
        return getDynamicBySQL(dataTablesInput, sql, FcFreezeVO.class, "", false,params);
    }
    /**
     * 根据相关条件查当前实体对象
     *
     * @param freeze
     * @return
     */
    public List<FcFreezeVO> findAllBy(FcFreezeVO freeze) {
        StringBuilder sb = new StringBuilder("select id as id,blnctrAcc as blnctrAcc,freezeType as freezeType from FcFreeze where blnctrAcc=:blnctrAcc and freezeSign=:freezeSign ");
        sb.append(" and freezeEndSign=:freezeEndSign and status=:status and id!=:id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("blnctrAcc", freeze.getBlnctrAcc());
        params.put("freezeSign", InnerAccountConstant.BUS_TYPE_FREEZE);
        params.put("freezeEndSign", InnerAccountConstant.BOOLEAN_TYPE_NO);
        params.put("status", CommonConstant.STATUE_CHECK);
        params.put("id", freeze.getUnFreezeId());
        return listByHQLAliasToBean(sb.toString(), params, FcFreezeVO.class);
    }
    /**
     * 编辑界面查询
     *
     * @param unFreezeCondition
     * @return
     */
    private String unFreezeEditQuery(String unFreezeCondition) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT  ID as id,UN_FREEZE_ID as unFreezeId, BLNCTR_ACC as blnctrAcc, ACC_NAME as accName, NET_ID as netId, CUR_ID as curId, CORP_ID as corpId,");
        sb.append("FREEZE_MONEY as freezeMoney,FREEZE_TYPE as freezeType,BEGIN_DATE as beginDate, END_DATE as endDate, ACCU_FREEZE_MONEY as accuFreezeMoney,");
        sb.append(" FREEZE_NO as freezeNo,STATUS as status,UN_FREEZE_MONEY as unFreezeMoney,REFUSE_WHY as refuseWhy, ");
        sb.append(" EXEC_OFFICIAL_NO as execOfficialNo,EXEC_CARD_NO as execCardNo,FREEZE_REASON as freezeReason,FREEZE_ORG as freezeOrg");
        sb.append(" FROM (select FREEZE.ID,FREEZE.UN_FREEZE_ID,fcAcc.BLNCTR_ACC ,fcAcc.ACC_NAME,fcAcc.NET_Id ,fcAcc.CUR_Id , fcAcc.CORP_ID ,");
        sb.append(" FREEZE.FREEZE_MONEY,FREEZE.exec_Official_No, FREEZE.exec_Card_No, FREEZE.freeze_Reason, FREEZE.freeze_Org,");
        sb.append(" FREEZE.UN_FREEZE_MONEY,FREEZE.REFUSE_WHY, ");
        sb.append("FREEZE.FREEZE_TYPE,FREEZE.BEGIN_DATE,FREEZE.END_DATE,FREEZE.ACCU_FREEZE_MONEY,FREEZE.FREEZE_NO,FREEZE.STATUS FROM FC_FREEZE FREEZE, FC_ACC fcAcc ");
        sb.append("WHERE FREEZE.BLNCTR_ACC = fcAcc.BLNCTR_ACC ");
        sb.append(unFreezeCondition);
        sb.append(") fcAcc WHERE 1=1 ");
        sb.append(" order by BLNCTRACC");
        return sb.toString();
    }

    /**
     * 功能介绍 : 存在冻结或解冻扣划未复核的记录
     *
     * @param closeDate
     * @param accList
     * @return
     */
    public List<FcFreezeDTO> findNocheckFreeze(Date closeDate, List<String> accList) {
        StringBuilder sql = new StringBuilder();
		/*sql.append(" select a.BLNCTR_ACC as blnctrAcc,b.ACC_NAME as accName,a.FREEZE_SIGN as freezeSign, ")
			.append(" a.FREEZE_TYPE as freezeType,a.BEGIN_DATE as beginDate,a.END_DATE as endDate,")
			.append(" a.FREEZE_MONEY as freezeMoney,a.ACCU_FREEZE_MONEY as accuFreezeMoney, ")
			.append(" a.STATUS as status,c.username as freezeOperator,a.OPERATE_TIME as operateTime ")
			.append(" from fc_freeze a, fc_acc b,sys_user c ")
			.append(" where a.BLNCTR_ACC = b.BLNCTR_ACC ")
			.append(" and a.status>=-1 and a.status<=11 and a.freeze_Sign in ('C','T') ")
			.append(" and a.FREEZE_OPERATOR = c.LOGIN_NAME ")
			.append(" and b.NET_ID = :netId ")
			.append(" and a.OPERATE_TIME < :endDate ");*/

        sql.append(" select a.BLNCTR_ACC as blnctrAcc,a.FREEZE_SIGN as freezeSign,a.FREEZE_OPERATOR as freezeOperator, ")
                .append(" a.FREEZE_TYPE as freezeType,a.BEGIN_DATE as beginDate,a.END_DATE as endDate,")
                .append(" a.FREEZE_MONEY as freezeMoney,a.ACCU_FREEZE_MONEY as accuFreezeMoney, ")
                .append(" a.STATUS as status,a.OPERATE_TIME as operateTime ")
                .append(" from fc_freeze a ")
                .append(" where a.status>=-1 and a.status<=11 and a.freeze_Sign in ('C','T') ")
                .append(" and a.OPERATE_TIME < :endDate ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("endDate", DateUtils.addDay(closeDate, 1));
        if (CollectionUtil.isNotEmpty(accList)) {
            sql.append(" and ").append(SqlUtil.getInSql(accList, "a.BLNCTR_ACC", true));
        }
        return listBySQLAliasToBean(sql.toString(), params, FcFreezeDTO.class);
    }


}
