package com.bytter.basic.dao;
import com.bytter.basic.entity.FcInstrateAdjust;
import com.bytter.basic.vo.FcInstrateAdjustVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.bean.InstrateAdjustBean;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.bytter.system.api.bean.AccInstrateBean;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 内部利率
 * @date 2017-07-11 10:54:47
 */
@Repository
public class FcInstrateAdjustDao extends BaseDao<FcInstrateAdjust, String> {
    /**
     * 利息状态-审批通过
     */
    private static final Integer INTEREST_STATUE_CHECK = 95;
    public DataTablesOutput<FcInstrateAdjustVO> getDataTablesUnion(DataTablesInput dataTablesInput, String joinsql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.account as account,f.net_id as netId,f.cur_id as curId,cur.cur_name as curName,");
        sql.append("f.subject_id as subjectId,sub.SUBJECT_NAME subjectName,net.name netName ,t.old_instrate as oldInstrate,t.new_instrate as newInstrate,");
        sql.append("t.instrt_type as instrtType,t.changtype as changType,t.rmk as rmk,t.operator_code as operatorCode,");
        sql.append("t.adjust_time as adjustTime,t.checker_code as checkerCode,t.check_time as checkTime,t.status as status");
        sql.append(" FROM " +
                "    Fc_Instrate_Adjust t" +
                "    JOIN fc_Acc f ON t.account = f.blnctr_acc" +
                "    JOIN fc_subject sub ON sub.id = f.subject_id" +
                "    JOIN sys_corp net ON net.id = f.net_id" +
                "    LEFT JOIN bt_currency cur ON f.cur_id = cur.id");
        sql.append(" where  f.pool_sign = '0' ");
        if (StringUtils.isNotEmpty(joinsql)) {
            sql.append(" and ");
            sql.append(joinsql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcInstrateAdjustVO.class, null, false);
    }
    /**
     * 资金池利率维护列表
     *
     * @param dataTablesInput
     * @param joinsql
     * @return
     */
    public DataTablesOutput<FcInstrateAdjustVO> getDataTablesUnionForPool(DataTablesInput dataTablesInput, String joinsql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.account as account,f.net_id as netPoolId,f.ACC_NAME as netPoolName, f.cur_id as curId,");
        sql.append(" f.subject_id as subjectPoolId,(select SUBJECT_NAME  from fc_subject_pool fs where fs.ID= f.subject_id ) as subjectPoolName," );
        sql.append(" t.old_instrate as oldInstrate,t.new_instrate as newInstrate, (select CUR_NAME  from bt_currency bc where bc.id = f.cur_id ) as curName,");
        sql.append(" t.instrt_type as instrtType,t.changtype as changType,t.rmk as rmk,t.operator_code as operatorCode,");
        sql.append(" t.adjust_time as adjustTime,t.checker_code as checkerCode,t.check_time as checkTime,t.status as status");
        sql.append(" from Fc_Instrate_Adjust t , fc_Acc f");
        sql.append(" where t.account=f.blnctr_acc and f.pool_sign='1' ");
        if (StringUtils.isNotEmpty(joinsql)) {
            sql.append(joinsql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcInstrateAdjustVO.class, null, false);
    }
    /**
     * 查询所有的待复核的调整利率
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput <FcInstrateAdjustVO> getWaitCheckDataTablesUnion(DataTablesInput dataTablesInput, String loginName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.account as account,f.net_id as netId, net.name as netName,f.cur_id as curId,cur.cur_name curName,");
        sql.append("f.subject_id as subjectId,sub.subject_name as subjectName, t.old_instrate as oldInstrate,t.new_instrate as newInstrate,");
        sql.append("t.instrt_type as instrtType,t.changtype as changType,t.rmk as rmk,t.operator_code as operatorCode,");
        sql.append("t.adjust_time as adjustTime,t.checker_code as checkerCode,t.check_time as checkTime");
        sql.append(" FROM " +
                "    Fc_Instrate_Adjust t" +
                "    JOIN fc_Acc f ON t.account = f.blnctr_acc" +
                "    JOIN fc_subject sub ON sub.id = f.subject_id" +
                "    JOIN sys_corp net ON net.id = f.net_id" +
                "    LEFT JOIN bt_currency cur ON f.cur_id = cur.id");
        sql.append(" where  f.pool_sign = '0' and t.status > 10 and t.status < 95 ");
        sql.append(" and t.create_by <> '" + loginName + "'");
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcInstrateAdjustVO.class, null, false);
    }
    /**
     * 查询资金池所有的待复核的调整利率
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FcInstrateAdjustVO> getWaitCheckDataTablesPoolUnion(DataTablesInput dataTablesInput, String loginName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.account as account,f.net_id as netPoolId, f.cur_id as curId,cur.cur_name as curName,");
        sql.append("f.subject_id as subjectPoolId,t.old_instrate as oldInstrate,t.new_instrate as newInstrate,");
        sql.append("t.instrt_type as instrtType,t.changtype as changType,t.rmk as rmk,t.operator_code as operatorCode,");
        sql.append("t.adjust_time as adjustTime,t.checker_code as checkerCode,t.check_time as checkTime");
        sql.append(" FROM " +
                "    Fc_Instrate_Adjust t" +
                "    JOIN fc_Acc f ON t.account = f.blnctr_acc" +
                "    LEFT JOIN bt_currency cur ON f.cur_id = cur.id");
        sql.append(" where  f.pool_sign = '1' and t.status > 10 and t.status < 95 ");
        sql.append(" and t.create_by <> '" + loginName + "'");
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcInstrateAdjustVO.class, null, false);
    }
    /**
     * 根据参数查询可以调整利率的内部账号列表
     *
     * @param
     * @return
     */
    public List<String> getFilterAccount(String netId, String corpId, String curId, String subjectId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct f.account as account");
        sb.append(" from Fc_Instrate_Adjust f,fc_acc a  where 1=1 and f.status in(0,-1,11) and f.account=a.blnctr_acc  ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(netId)) {
            sb.append(" and a.net_id =:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sb.append(" and a.corp_id =:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            sb.append(" and a.subject_Id =:subjectId");
            params.put("subjectId", subjectId);
        }
        if (StringUtils.isNotEmpty(curId)) {
            sb.append(" and a.cur_id =:curId");
            params.put("curId", curId);
        }
        return searchBySql(sb.toString(), params);
    }
    public List searchBySql(String sql, Map params) {
        SQLQuery query = createSQLQuery(sql);
        if (params != null) {
            query.setProperties(params);
        }
        return query.list();
    }
    /**
     * 根据账号与利率类型在利率调整表中获取最新的利率
     *
     * @param blnctrAccs
     * @param instrtType
     * @return
     */
    public Hashtable<String, FcInstrateAdjust> getLastFcInsrateAdjust(
            String[] blnctrAccs, String instrtType) {
        Hashtable<String, FcInstrateAdjust> hashtableRet = new Hashtable<String, FcInstrateAdjust>();
        if (blnctrAccs == null || blnctrAccs.length < 1) {
            return hashtableRet;
        }
        List<FcInstrateAdjust> fcInstrateAdjusts = listFcInsrateAdjust(blnctrAccs, instrtType);
        if (fcInstrateAdjusts == null) {
            return hashtableRet;
        }
        for (FcInstrateAdjust fcInstrateAdjust : fcInstrateAdjusts) {
            hashtableRet.put(fcInstrateAdjust.getAccount(), fcInstrateAdjust);
        }
        return hashtableRet;
    }
    /**
     * 根据账号与利率类型在利率调整表中获取最新的利率
     *
     * @param blnctrAccs
     * @param instrtType
     * @return
     */
    public List<FcInstrateAdjust> listFcInsrateAdjust(String[] blnctrAccs, String instrtType) {
        StringBuilder sb = new StringBuilder();
        sb.append("select t.id as id, t.account as account,t.instrt_Type as InstrtType,t.new_instrate as newInstrate,t.old_Instrate as oldInstrate,");
        sb.append(" t.adjust_time as adjustTime,t.create_time as createTime,t.update_time as updateTime from Fc_Instrate_Adjust t,");
        sb.append("( select max(f.adjust_time) as adjustTime,f.account as account,f.instrt_Type as InstrtType ");
        sb.append(" from Fc_Instrate_Adjust f  where 1=1 and f.instrt_Type=");
        sb.append(instrtType);
        sb.append(" and ");
        sb.append(SqlUtil.getInSql(blnctrAccs, "f.account", true));
        sb.append(" group by  f.account,f.instrt_Type) e");
        sb.append(" where t.account=e.account and t.adjust_time=e.adjustTime");
        List<FcInstrateAdjust> fcInstrateAdjusts = listBySQLAliasToBean(sb.toString(), null, FcInstrateAdjust.class);
        return fcInstrateAdjusts;
    }
    /**
     * 调整率利复核通过 or 调整率利复核打回
     *
     * @param ids      利率调整的id
     * @param userCode 用户编码
     * @param date     调整时间
     */
    public void changeStatus(String[] ids, String userCode, Integer status, Date date) {
        StringBuilder hql = new StringBuilder("update FcInstrateAdjust set status =:status,checkerCode =:userCode,checkTime=:checkTime,");
        hql.append(" updateBy=:updateBy,updateTime=:updateTime where status > 10 and status < 95 and ");
        hql.append(SqlUtil.getInSql(ids, "id", true));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("userCode", userCode);
        map.put("checkTime", date);
        map.put("updateBy", userCode);
        map.put("updateTime", date);
        this.batchExecute(hql.toString(), map);
    }
    /**
     * 查询所有的待复核的调整利率
     *
     * @param instrateId
     * @return
     */
    public List<FcInstrateAdjustVO> getVOByInstrateId(String instrateId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.account as account,f.net_id as netId,f.cur_id as curId,");
        sql.append("f.subject_id as subjectId,t.old_instrate as oldInstrate,t.new_instrate as newInstrate,");
        sql.append("t.instrt_type as instrtType,t.changtype as changType,t.rmk as rmk,t.operator_code as operatorCode,");
        sql.append("t.adjust_time as adjustTime,t.checker_code as checkerCode,t.check_time as checkTime");
        sql.append(" from Fc_Instrate_Adjust t , fc_Acc f ");
        sql.append(" where t.account=f.blnctr_acc ");
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(instrateId)) {
            sql.append(" and t.instrate_Id =:instrateId");
            map.put("instrateId", instrateId);
        }
        return listBySQLAliasToBean(sql.toString(), map, FcInstrateAdjustVO.class);
    }
    public Map<String, InstrateAdjustBean> queryInterestAdjustsByAcc(String blnctrAcc) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select acc.blnctr_acc,ad.instrt_type,ad.new_instrate,ad.old_instrate,ad.adjust_time from FC_INSTRATE_ADJUST ad left join fc_acc acc on ad.account = acc.blnctr_acc "); //2张表都是基础模块的
        sql.append("where ad.status = :status and acc.blnctr_acc = :blnctrAcc ");
        params.put("status", CommonConstant.STATUE_CHECK);
        params.put("blnctrAcc", blnctrAcc);
        List<Object[]> adjustInfos = createSQLQuery(sql.toString(), params).list();
        Map<String, InstrateAdjustBean> result = new HashMap<String, InstrateAdjustBean>();
        for (Object[] adjustInfo : adjustInfos) {
            String account = (String) adjustInfo[0];
            String instrtType = (String) adjustInfo[1];
            InstrateAdjustBean accAdjstInfo = result.get(account);
            if (accAdjstInfo == null) {
                accAdjstInfo = new InstrateAdjustBean();
                result.put(account, accAdjstInfo);
            }
            accAdjstInfo.getAccInstratesByAdjustType(instrtType, true).add(
                    new AccInstrateBean(account, instrtType, null, (Date) adjustInfo[4], (BigDecimal)adjustInfo[2],
                            (BigDecimal)adjustInfo[3]));
        }
        return result;
    }
    public List<AccInstrateBean> queryInstRateAdjustInfos(String netCode, String accTypeCode, Date endInstDate, Integer[] corpIds, String instAcc) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select acc.blnctr_acc as blnctrAcc,fia.instrt_type as instrtType,fia.new_instrate as newInstrate,")
                .append("fia.old_instrate as oldInstrate, ");
        sql.append("fia.adjust_time as adjustTime,fia.changtype as changtype,fia.id as id from FC_INSTRATE_ADJUST fia, fc_acc acc ");
        sql.append("where fia.account = acc.blnctr_acc ");
        sql.append("and fia.status = :status and acc.interest_sign = '1' ");
        if (!StringUtils.isBlank(netCode)) {
            sql.append("and acc.net_id = :netCode ");
            params.put("netCode", netCode);
        }
        if (corpIds != null && corpIds.length > 0) {
            sql.append("and acc.corp_id in (:corpIds) ");
            params.put("corpIds", corpIds);
        }
        params.put("status", INTEREST_STATUE_CHECK);
        if (StringUtils.isNotBlank(accTypeCode)) {
            sql.append("and acc.acc_type = :accTypeCode ");
            params.put("accTypeCode", accTypeCode);
        }
        if (endInstDate != null) {
            sql.append(" and fia.adjust_time <= :endInstDate ");
            params.put("endInstDate", endInstDate);
        }
        if (!EmptyUtils.isEmpty(instAcc)) {
            sql.append(" and fia.account = :instAcc ");
            params.put("instAcc", instAcc);
        }
        sql.append(" order by acc.blnctr_acc,fia.adjust_time ");
        List<Object[]> adjustInfos = createSQLQuery(sql.toString(), params).list();
        List<AccInstrateBean> beans = new ArrayList<AccInstrateBean>();
        for (Object[] adjustInfo : adjustInfos) {
            String account = (String) adjustInfo[0];
            String instrtType = (String) adjustInfo[1];
            beans.add(new AccInstrateBean(account, instrtType, null, (Date) adjustInfo[4], (BigDecimal) adjustInfo[2],
                    (BigDecimal) adjustInfo[3], (String) adjustInfo[6], (String) adjustInfo[5]));
        }
        return beans;
    }
}
