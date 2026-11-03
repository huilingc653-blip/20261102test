package com.bytter.biz.finacial.interest.dao;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.interest.bean.ChoInnerFinanceBean;
import com.bytter.biz.finacial.interest.bean.InterestCommonBean;
import com.bytter.biz.finacial.interest.bean.InterestSearchBean;
import com.bytter.biz.finacial.interest.entity.*;
import com.bytter.biz.finacial.interest.vo.*;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.utils.*;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.commons.collections4.MapUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FcInterestDao extends BaseDao<FcInterestDtlTemp, String> {
    @DubboReference(check = false)
    IFcAccService iFcAccService;

    /**
     * <b>功能介绍：</b>根据预提ID查找利息明细<br>
     * <b>日期：</b>2017年11月8日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param preId
     * @return
     * @version 1.0
     */
    public List<FcInterestDtlTemp> getFcInterestDtlTempByPreId(String preId) {
        return listBy("predrawDtlId", preId);
    }

    /**
     * <b>功能介绍：</b>根据预提ID查找预提明细<br>
     * <b>日期：</b>2017年11月8日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param id
     * @return
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public FcPredrawDtlTemp getFcPredrawDtlTempById(String id) {
        StringBuilder hql = new StringBuilder("from FcPredrawDtlTemp where id = ?0 ");
        List<FcPredrawDtlTemp> list = createQuery(hql.toString(), id).list();
        if (!EmptyUtils.isEmptyList(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * <b>功能介绍：</b>查询暂存利息明细的记账日期和起息日期<br>
     * <b>日期：</b>2017年7月20日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> queryAccTypeInterestInfo(String netId, String accType, String userId, Integer[] busStatus) {
        Map<String, Object> param = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct dtl.ACC_TYPE,dtl.BLN_INST_DATE,dtl.TALLY_DATE ");
        sql.append("from FC_INTEREST_DTL_TEMP dtl ");
        sql.append("where 1=1 and dtl.NET_id = :netId ");
        if (StringUtils.isNotEmpty(accType)) {
            sql.append("and dtl.acc_type = :accType ");
            param.put("accType", accType);
        }
        sql.append("and dtl.BUS_STATUS in (:busStatus) ");
        sql.append("and dtl.USER_SESSION_ID = :userId ");
        param.put("netId", netId);
        param.put("busStatus", busStatus);
        param.put("userId", userId);
        return createSQLQuery(sql.toString(), param).list();
    }

    /**
     * <b>功能介绍：</b>查询暂存预提明细的记账日期和起息日期<br>
     * <b>日期：</b>2017年7月20日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> queryAccTypePredrawInfo(String netId, String accType, String opType) {
        Map<String, Object> param = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pre.ACC_TYPE, dtl2.bln_Inst_Date, dtl2.tally_date ");
        sql.append("from fc_predraw_dtl pre ");
        sql.append("inner join (select a.predraw_dtl_id, sum(a.strike_interest) as strike_interest, a.status ");
        sql.append("from fc_predraw_rush_dtl a where a.status in (:status) group by a.predraw_dtl_id, a.status) rush ");
        sql.append("on pre.id = rush.predraw_dtl_id and rush.strike_interest <= pre.interest ");
        sql.append("inner join (select dtl1.PREDRAW_DTL_ID, max(dtl1.BLN_INST_DATE) BLN_INST_DATE, dtl1.tally_date ");
        sql.append("from fc_interest_dtl dtl1 group by dtl1.PREDRAW_DTL_ID, dtl1.tally_date) dtl2 ");
        sql.append("on dtl2.PREDRAW_DTL_ID = pre.ID where 1 = 1 ");
        if ("initList".equals(opType)) {
            sql.append("and not exists (select 1 from (select b.predraw_dtl_id, ");
            sql.append("sum(b.strike_interest) as strike_interest from fc_predraw_rush_dtl b ");
            sql.append("where b.status in (" + InterestConstant.INTEREST_WAIT_CHECK + ", " + InterestConstant.INTEREST_STATUE_CHECK + ") group by b.predraw_dtl_id) rushdtl ");
            sql.append("where rushdtl.predraw_dtl_id = pre.id and rushdtl.strike_interest >= pre.interest) ");
            param.put("status", new Integer[]{InterestConstant.INTEREST_STATUE_TEMP, InterestConstant.INTEREST_BACK});
        } else if ("checkList".equals(opType)) {
            sql.append("and exists (select 1 ");
            sql.append("from fc_predraw_rush_dtl rushdtl where rushdtl.predraw_dtl_id = pre.id  and rushdtl.status = " + InterestConstant.INTEREST_WAIT_CHECK + ") ");
            param.put("status", new Integer[]{InterestConstant.INTEREST_WAIT_CHECK});
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append("and pre.net_id = :netId ");
            param.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(accType)) {
            sql.append("and pre.acc_type=:accType ");
            param.put("accType", accType);
        }
        sql.append("and pre.interest > 0 ");
        return createSQLQuery(sql.toString(), param).list();
    }

    /**
     * <b>功能介绍：</b>查询暂存利息明细<br>
     * <b>日期：</b>2017年11月1日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param curr
     * @param netCode
     * @param accType
     * @param opType
     * @param sessionId
     * @param pageData
     * @return
     * @version 1.0
     */
    public Map<String, Object> queryCanEditInterestDtl(int curr, String netCode, String accType,
                                                       String opType, String sessionId, Map<String, Object> pageData,
                                                       boolean zeroSign) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> dataParams = new HashMap<String, Object>();
        if (InnerAccountConstant.FCACC_LOAN.equals(pageData.get("ACCTYPE"))) {
            sql.append("select dtl.account as account,dtl.acc_name as accName,dtl.interest_start_date as interestStartDate, ");
            sql.append("dtl.bln_inst_date as blnInstDate,dtl.days as days,dtl.cumulation as cumulation,dtl.Interest_Rate as interestRate, ");
            sql.append("dtl.Interest as interest,dtl.Interest_Type as interestType,dtl.Interest_Sign as interestSign,dtl.Bus_Status as busStatus ");
            sql.append("from fc_interest_dtl_temp dtl ");
            sql.append("where 1 = 1 ");
            if (!zeroSign) {
                sql.append(" and (dtl.interest <> 0 and dtl.cumulation <> 0) ");
            }
            if (null != netCode) {
                sql.append(" and dtl.net_id = :netId ");
                dataParams.put("netId", netCode);
            }
            if (null != pageData.get("CORPID")) {
                sql.append(" and dtl.corp_id = :corpId ");
                dataParams.put("corpId", pageData.get("CORPID"));
            }
            if (null != pageData.get("account")) {
                sql.append(" and dtl.account = :account ");
                dataParams.put("account", pageData.get("account"));
            }
            if (null != pageData.get("generateMode")) {
                sql.append(" and dtl.generate_mode = :generateMode ");
                dataParams.put("generateMode", pageData.get("generateMode"));
            }
            sql.append(" and dtl.cur_id =:curId and dtl.acc_type=:accType and dtl.interest_sign = :interestSign ");
            if ("initList".equals(opType)) {
                sql.append("and ((dtl.user_session_id =:userSessionId and dtl.bus_status = :tempStatus) or (dtl.user_session_id =:userSessionId and dtl.bus_status = :backStatus)) ");
            } else {
                sql.append(" and dtl.bus_status = :busStatus and dtl.user_session_id <> :userSessionId ");
            }
            sql.append("order by dtl.account,dtl.interest_start_date");
        } else {
            sql.append("select dtl.account as account,dtl.acc_name as accName,dtl.interest_start_date as interestStartDate, ");
            sql.append("dtl.bln_inst_date as blnInstDate,dtl.days as days,dtl.cumulation as cumulation,dtl.Interest_Rate as interestRate, ");
            sql.append("dtl.Interest as interest,dtl.Interest_Type as interestType,dtl.Interest_Sign as interestSign,dtl.Bus_Status as busStatus ");
            sql.append("from fc_interest_dtl_temp dtl  ");
            sql.append("where 1 = 1 ");
            if (!zeroSign) {
                sql.append(" and (dtl.interest <> 0 or dtl.cumulation <> 0) ");
            }
            if (null != netCode) {
                sql.append(" and dtl.net_id = :netId ");
                dataParams.put("netId", netCode);
            }
            if (null != pageData.get("CORPID")) {
                sql.append(" and dtl.corp_id = :corpId ");
                dataParams.put("corpId", pageData.get("CORPID"));
            }
            if (null != pageData.get("generateMode")) {
                sql.append(" and dtl.generate_mode = :generateMode ");
                dataParams.put("generateMode", pageData.get("generateMode"));
            }
            if (null != pageData.get("account")) {
                sql.append(" and dtl.account = :account ");
                dataParams.put("account", pageData.get("account"));
            }
            sql.append(" and dtl.cur_id =:curId and dtl.acc_type=:accType and dtl.interest_sign = :interestSign ");
            if ("initList".equals(opType)) {
                sql.append("and ((dtl.user_session_id =:userSessionId and dtl.bus_status = :tempStatus) or (dtl.user_session_id =:userSessionId and dtl.bus_status = :backStatus)) ");
            } else {
                sql.append(" and dtl.bus_status = :busStatus and dtl.user_session_id <> :userSessionId ");
            }
            sql.append("order by dtl.account,dtl.interest_start_date");
        }
        dataParams.put("accType", pageData.get("ACCTYPE"));
        dataParams.put("curId", pageData.get("CURID"));
        dataParams.put("interestSign", pageData.get("INTERESTSIGN"));
        if ("initList".equals(opType)) {
            dataParams.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP.intValue());
            dataParams.put("backStatus", InterestConstant.INTEREST_BACK.intValue());
        } else {
            dataParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK.intValue());
        }
        dataParams.put("userSessionId", sessionId);
        List<FcInterestDtlTempVo> list = listBySQLAliasToBean(sql.toString(), dataParams, FcInterestDtlTempVo.class);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pageData", pageData);
        result.put("interestDtls", list);
        result.put("curr", curr);
        return result;
    }

    /**
     * <b>功能介绍：</b>查询待冲预提利息明细<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param searchBean
     * @param account
     * @param opType
     * @param pageInfo
     * @return
     * @version 1.0
     */
    public Map<String, Object> queryRushFcInterestDtls(InterestSearchBean searchBean,
                                                       String account, String opType, Map<String, String> pageInfo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select interestdtl.id,interestdtl.predraw_dtl_id,interestdtl.bln_inst_date,interestdtl.account,interestdtl.acc_name,interestdtl.cur_code,interestdtl.interest_start_date,interestdtl.interest_time,");
        sql.append("interestdtl.days,interestdtl.cumulation,interestdtl.interest_rate,interestdtl.interest,interestdtl.interest_sign,interestdtl.corp_id,interestdtl.acc_type,");
        sql.append("interestdtl.interest_type,interestdtl.repay_money,interestdtl.bus_status,interestdtl.rmk ");
        sql.append("from fc_interest_dtl interestdtl ");
//        sql.append("join fc_acc interestacc on interestdtl.account = interestacc.blnctr_acc ");
        sql.append("where interestdtl.predraw_dtl_id in (select pre.id ");
        searchBean.setAccType(pageInfo.get("accType"));
        sql.append(rushPreDrawSql(searchBean, pageInfo.get("CORPID"), pageInfo.get("CURID"), account, opType) + ") ");
        if ("initList".equals(opType)) {
            sql.append("and exists (");
            sql.append("select 1 from fc_predraw_rush_dtl rdtl where rdtl.predraw_dtl_id = interestdtl.predraw_dtl_id and rdtl.operator_code = :opCode ) ");
            params.put("status", new Integer[]{InterestConstant.INTEREST_STATUE_TEMP, InterestConstant.INTEREST_BACK});
        } else {
            sql.append("and not exists (");
            sql.append("select 1 from fc_predraw_rush_dtl rdtl where rdtl.predraw_dtl_id = interestdtl.predraw_dtl_id and rdtl.operator_code = :opCode) ");
            params.put("status", new Integer[]{InterestConstant.INTEREST_WAIT_CHECK});
        }
        sql.append(" order by interestdtl.account,interestdtl.interest_start_date");
        params.put("netId", searchBean.getNetId());
        params.put("curId", pageInfo.get("CURID"));
        params.put("corpId", pageInfo.get("CORPID"));
        params.put("endDate", searchBean.getBlnInstDate());
        params.put("accType", pageInfo.get("accType"));
        params.put("account", account);
        params.put("opCode", searchBean.getUserId());
        List<FcInterestDtlVo> list = listBySQLAliasToBean(sql.toString(), params, FcInterestDtlVo.class);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pageData", pageInfo);
        result.put("interestDtls", list);
        return result;
    }

    /**
     * <b>功能介绍:查询单位、币别、开户种类集合(批量计息)</b><br>
     * <b>日期：</b>2017年7月3日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryAccTypeBean(String netCode, String accType,
                                                      String opType, String sessionId, String account,
                                                      String generateMode, boolean zeroSign) {
        Map<String, Object> pageParams = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct fidt.CUR_ID as CURID,fidt.cur_name as CURNAME, fidt.acc_type as ACCTYPE, ");
        sql.append("fidt.interest_sign as INTERESTSIGN ");
        sql.append("from FC_INTEREST_DTL_TEMP fidt  ");
        sql.append("where fidt.net_id = :netCode ");
        if (StringUtils.isNotBlank(accType)) {
            sql.append(" and fidt.ACC_TYPE in (:accType) ");
            pageParams.put("accType", accType.split(","));
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and fidt.generate_mode = :generateMode ");
            pageParams.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and fidt.account =:account ");
            pageParams.put("account", account);
        }
        if (!zeroSign) {
            sql.append(" and (fidt.interest <> 0 or fidt.cumulation <> 0) ");
        }
        pageParams.put("netCode", netCode);
        if ("initList".equals(opType)) {
            sql.append(" and (( fidt.BUS_STATUS = :tempStatus or fidt.BUS_STATUS = :backStatus) and fidt.USER_SESSION_ID = :userSessionId ) ");
            pageParams.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP.intValue());
            pageParams.put("backStatus", InterestConstant.INTEREST_BACK.intValue());
            pageParams.put("userSessionId", sessionId);
        } else {
            sql.append(" and fidt.BUS_STATUS = :busStatus and fidt.USER_SESSION_ID <> :userSessionId ");
            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK.intValue());
            pageParams.put("userSessionId", sessionId);
        }
        sql.append("order by accType,curId,interestSign ");
        return listBySQL(sql.toString(), pageParams);
    }

    /**
     * <b>功能介绍：</b>功能介绍:查询单位、币别、开户种类集合(冲预提)<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param searchBean
     * @param account
     * @param opType
     * @return
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, String>> queryRushAccTypeBean(InterestSearchBean searchBean, String account, String opType) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pre.corp_id as corpId,pre.CORP_NAME as corpName,pre.cur_id as curId,pre.cur_name as curName,pre.acc_type as accType ");
        sql.append(rushPreDrawSql(searchBean, "", "", account, opType));
        if ("initList".equals(opType)) {
            sql.append("and exists (");
            sql.append("select 1 from fc_predraw_rush_dtl rdtl ");
            sql.append("where rdtl.predraw_dtl_id = pre.id and rdtl.operator_code = :opCode) ");
            params.put("status", new Integer[]{InterestConstant.INTEREST_STATUE_TEMP, InterestConstant.INTEREST_BACK});
        } else {
            sql.append("and not exists (");
            sql.append("select 1 from fc_predraw_rush_dtl rdtl ");
            sql.append("where rdtl.predraw_dtl_id = pre.id and rdtl.operator_code = :opCode) ");
            params.put("status", new Integer[]{InterestConstant.INTEREST_WAIT_CHECK});
        }
        sql.append("order by pre.acc_type,pre.corp_id,pre.cur_id ");
        params.put("netId", searchBean.getNetId());
        params.put("endDate", searchBean.getBlnInstDate());
        params.put("accType", searchBean.getAccType());
        params.put("account", account);
        params.put("opCode", searchBean.getUserId());
        return listBySQL(sql.toString(), params);
    }

    /**
     * <b>功能介绍：</b>冲预提明细sql拼装<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param searchBean
     * @param corpCode
     * @param curCode
     * @param account
     * @param opType
     * @return
     * @version 1.0
     */
    private String rushPreDrawSql(InterestSearchBean searchBean, String corpCode, String curCode, String account, String opType) {
        StringBuilder sql = new StringBuilder();
        sql.append("from fc_predraw_dtl pre ");
//        sql.append("left join fc_acc acc on acc.blnctr_acc = pre.account ");
//        sql.append("left join sys_corp sc on sc.id = acc.corp_id  ");
//        sql.append("left join bt_currency bc on bc.id = acc.cur_id ");
        sql.append("inner join (select a.predraw_dtl_id,sum(a.strike_interest) as strike_interest,a.status ");
        sql.append("from fc_predraw_rush_dtl a where a.status in (:status) group by a.predraw_dtl_id,a.status) rush ");
        sql.append("on pre.id = rush.predraw_dtl_id and rush.strike_interest <= pre.interest ");
        sql.append("inner join (select dtl1.predraw_dtl_id,max(dtl1.bln_inst_date) bln_inst_date,dtl1.tally_date  ");
        sql.append("from fc_interest_dtl dtl1 group by dtl1.predraw_dtl_id,dtl1.tally_date) dtl2 ");
        sql.append("on dtl2.predraw_dtl_id = pre.id ");
        sql.append("where 1 = 1 ");
        if (searchBean.getBlnInstDate() != null) {
            sql.append("and not exists (");
            sql.append("select 1 from fc_interest_dtl dtl where dtl.predraw_dtl_id = pre.id and dtl.bln_inst_date > :endDate) ");
        }
        if ("initList".equals(opType)) {
            sql.append("and not exists (");
            sql.append("select 1 from (select b.predraw_dtl_id,sum(b.strike_interest) as strike_interest ");
            sql.append("from fc_predraw_rush_dtl b where b.status in (" + InterestConstant.INTEREST_WAIT_CHECK + "," + InterestConstant.INTEREST_STATUE_CHECK + ") group by b.predraw_dtl_id) rushdtl ");
            sql.append("where rushdtl.predraw_dtl_id = pre.id and rushdtl.strike_interest >= pre.interest) ");
        } else if ("checkList".equals(opType)) {
            sql.append("and exists (");
            sql.append("select 1 from fc_predraw_rush_dtl rushdtl where rushdtl.predraw_dtl_id = pre.id and rushdtl.status = " + InterestConstant.INTEREST_WAIT_CHECK + ") ");
        }
        if (EmptyUtils.isNotEmpty(searchBean.getNetId())) {
            sql.append("and pre.net_id = :netId ");
        }
        if (EmptyUtils.isNotEmpty(corpCode)) {
            sql.append("and pre.corp_id = :corpId ");
        }
        if (EmptyUtils.isNotEmpty(curCode)) {
            sql.append("and pre.cur_id = :curId ");
        }
        if (EmptyUtils.isNotEmpty(searchBean.getAccType())) {
            sql.append("and pre.acc_type = :accType ");
            if (EmptyUtils.isNotEmpty(account)) {
                sql.append("and pre.account = :account ");
            }
        }
        sql.append("and pre.interest > 0 ");
        return sql.toString();
    }

    /**
     * <b>功能介绍:</b>删除暂存表中利息明细<br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void deleteInterest(String netCode, String sessionId, Integer busStatus, String account, String generateMode) {
        Map<String, Object> params = new HashMap<String, Object>();
        String delInterest = "delete from fc_interest_dtl_temp where 1=1 ";
        if (StringUtils.isNotBlank(netCode)) {
            delInterest += " and fc_interest_dtl_temp.net_id = :netCode ";
            params.put("netCode", netCode);
        }
        if (StringUtils.isNotBlank(sessionId)) {
            delInterest += " and fc_interest_dtl_temp.user_session_id =:sessionId ";
            params.put("sessionId", sessionId);
        }
        //产生方式 P-批量计息 I-个别计息
        if (EmptyUtils.isNotEmpty(generateMode)) {
            delInterest += " and fc_interest_dtl_temp.generate_mode =:generateMode ";
            params.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            delInterest += " and fc_interest_dtl_temp.account =:account ";
            params.put("account", account);
        }
        if (busStatus != null) {
            delInterest += " and fc_interest_dtl_temp.bus_status =:busStatus";
            params.put("busStatus", busStatus);
        }
        createSQLQuery(delInterest, params).executeUpdate();
        flush();
    }

    /**
     * <b>功能介绍：</b>删除暂存表中预提明细<br>
     * <b>日期：</b>2017年7月20日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void deletePredraw(String netCode, String sessionId, Integer busStatus, String account, String generateMode) {
        Map<String, Object> params = new HashMap<String, Object>();
        String delPredraw = "delete from fc_predraw_dtl_temp where exists (select 1 from fc_interest_dtl i"
                + "  where i.predraw_dtl_id = fc_predraw_dtl_temp.id ";
        if (StringUtils.isNotBlank(netCode)) {
            delPredraw += " and i.net_id = :netCode ";
            params.put("netCode", netCode);
        }
        if (StringUtils.isNotBlank(sessionId)) {
            delPredraw += " and i.user_session_id =:sessionId ";
            params.put("sessionId", sessionId);
        }
        //产生方式 P-批量计息 I-个别计息
        if (EmptyUtils.isNotEmpty(generateMode)) {
            delPredraw += " and i.generate_mode =:generateMode ";
            params.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            delPredraw += " and i.account =:account ";
            params.put("account", account);
        }
        if (busStatus != null) {
            delPredraw += " and i.bus_status =:busStatus";
            params.put("busStatus", busStatus);
        }
        delPredraw += ")";
        createSQLQuery(delPredraw, params).executeUpdate();
        flush();
    }

    /**
     * <b>功能介绍:</b>更新打回利息的积数<br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void updateAccuBalancesInterestSign(String netCode, String accTypeCode, String sessionId, String account, String generateMode) {
        StringBuilder sql = new StringBuilder();
        sql.append("update fc_accu_balance accu set accu.interest_sign='0',interest_time = null,accu.pre_sign = '0' ");
        sql.append(" where accu.balance_date <= (select max(temp.bln_inst_date) from fc_interest_dtl_temp temp where ");
        sql.append(" temp.account = accu.account   and temp.interest_time = accu.interest_time and ");
        sql.append(" temp.bus_status= :busStatus ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and temp.generate_mode = :generateMode");
            params.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {//个别计息-指定查询账户
            sql.append(" and temp.account = :account");
            params.put("account", account);
        } else {
            sql.append(" group by temp.account ");//批量计息-查询所有账户
        }
        sql.append(") ");
        sql.append(" and accu.balance_date >= (select min(temp2.interest_start_date) from fc_interest_dtl_temp temp2 where ");
        sql.append(" temp2.account = accu.account and temp2.interest_time = accu.interest_time and ");
        sql.append(" temp2.bus_status= :busStatus  ");
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and temp2.generate_mode = :generateMode");
            params.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {//个别计息-指定查询账户
            sql.append(" and temp2.account = :account");
            params.put("account", account);
        } else {
            sql.append(" group by temp2.account ");//批量计息-查询所有账户
        }
        sql.append(") ");
        if (EmptyUtils.isNotEmpty(account)) {//个别计息-指定查询账户
//            sql.append(" and acc.blnctr_acc = :account");
            params.put("account", account);
        }
        sql.append(" and exists (select 1 from fc_interest_dtl_temp dt where dt.interest_time = accu.interest_time");
        sql.append(" and dt.account = accu.account and dt.bus_status =:busStatus  ");
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and dt.generate_mode = :generateMode");
            params.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {//个别计息-指定查询账户
            sql.append(" and dt.account = :account");
            params.put("account", account);
        }
        sql.append(" ) ");
        sql.append(" and (accu.interest_sign = '1' or accu.pre_sign = '1') ");
        if (EmptyUtils.isNotEmpty(account)) {//个别计息-指定查询账户
            sql.append(" and accu.account = :account");
            params.put("account", account);
        }
        params.put("busStatus", InterestConstant.INTEREST_BACK);
        params.put("netCode", netCode);
        params.put("sessionId", sessionId);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * <b>功能介绍:</b>删除不足款信息<br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void deleteLessPay(List<String> accounts, String userCode) {
        /*String sql = "delete from fc_less_pay_bill where exists (select 1 from fc_acc acc "
                + " where fc_less_pay_bill.account = acc.blnctr_acc and acc.net_id = :netCode and acc.acc_type = :accTypeCode ) "
                + " and fc_less_pay_bill.status = :status and fc_less_pay_bill.OPERATOR_CODE = :userCode";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", InterestConstant.INTEREST_BACK);
        params.put("netCode", netCode);
        params.put("accTypeCode", InnerAccountConstant.FCACC_LOAN);keyi
        params.put("userCode", userCode);*/
        String sql = "delete from fc_less_pay_bill where account in (:accounts) and status = :status and OPERATOR_CODE = :userCode";

        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("account",accounts);
        params.put("status", InterestConstant.INTEREST_BACK);
        params.put("userCode", userCode);
        params.put("accounts", accounts);
        createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * <b>功能介绍:</b>删除预提和利息信息<br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void deleteInterestDtlTemp(String netId, String sessionId, String account, String generateMode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder delPredraw = new StringBuilder();
        delPredraw.append("delete from fc_predraw_dtl_temp where exists ( select 1 from fc_interest_dtl_temp i");
        delPredraw.append(" where i.predraw_dtl_id = fc_predraw_dtl_temp.id and i.net_id=:netId and ( ");
        StringBuilder delInterest = new StringBuilder();
        delInterest.append("delete from fc_interest_dtl_temp where fc_interest_dtl_temp.net_id=:netId and ( ");
        if (StringUtils.isNotBlank(sessionId)) {
            delPredraw.append(" (i.bus_status =:tempStatus) or  ");
            delInterest.append(" (fc_interest_dtl_temp.user_session_id =:sessionId and fc_interest_dtl_temp.bus_status = :tempStatus) or ");
            params.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            params.put("sessionId", sessionId);
        }
        delPredraw.append(" i.bus_status = :backStatus ");
        delInterest.append(" fc_interest_dtl_temp.bus_status = :backStatus ");
        params.put("netId", netId);
        if (EmptyUtils.isNotEmpty(account)) {
            delPredraw.append(" and i.account = :account ");
            delInterest.append(" and fc_interest_dtl_temp.account = :account ");
            params.put("account", account);
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            delInterest.append(" and fc_interest_dtl_temp.generate_mode = :generateMode ");
            params.put("generateMode", generateMode);
        }

        delPredraw.append(") )");
        delInterest.append(")");
        params.put("backStatus", InterestConstant.INTEREST_BACK);
        createSQLQuery(delInterest.toString()).setProperties(params).executeUpdate();
        params.remove("generateMode");//预提明细临时表没有产生方式[批量计息和个别计息]因此移除该查询参数
        createSQLQuery(delPredraw.toString()).setProperties(params).executeUpdate();
    }

    /**
     * <b>功能介绍：</b>删除冲预提信息<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void deletePredrawRushDtls(InterestSearchBean searchBean, String account) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
//        sql.append("delete from fc_predraw_rush_dtl a where exists (select 1 ");
//        sql.append("from  fc_predraw_dtl b where b.id = a.predraw_dtl_id  and b.net_id = :netId  ");

        sql.append("delete from fc_predraw_rush_dtl where exists (select 1 ");
        sql.append("from fc_predraw_dtl b where b.net_id = :netId  ");
        if (EmptyUtils.isNotEmpty(searchBean.getAccType())) {
            sql.append("and b.acc_Type = :accType ");
            params.put("accType", searchBean.getAccType());
            if (EmptyUtils.isNotEmpty(searchBean.getAccount())) {
                sql.append("and b.account = :account ");
                params.put("account", searchBean.getAccount());
            }
        }
//        sql.append(") and a.status in (:status) ");
        sql.append(") and status in (:status) ");
        params.put("status", new Integer[]{InterestConstant.INTEREST_STATUE_TEMP, InterestConstant.INTEREST_BACK});
        if (searchBean.getBlnInstDate() != null) {
            /*sql.append("and not exists (select 1 from fc_interest_dtl d where d.predraw_dtl_id = a.predraw_dtl_id and d.bln_inst_date > :endDate) ");
            params.put("endDate", searchBean.getBlnInstDate());*/
            sql.append("and predraw_dtl_id not in (select d.predraw_dtl_id from fc_interest_dtl d where d.bln_inst_date > :endDate) ");
            params.put("endDate", searchBean.getBlnInstDate());
        }
        params.put("netId", searchBean.getNetId());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * <b>功能介绍：</b>获取构造凭证的利息分类<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> queryVoucherHeadList(String netCode, String accType, String opType, String sessionId, boolean isAccInCenter, boolean flag, String account, String generateMode) {
        Map<String, Object> pageParams = new HashMap<String, Object>();
        StringBuilder selectExp = new StringBuilder();
        StringBuilder pagesql = new StringBuilder();
        selectExp.append("select distinct fidt.cur_id as curId,fidt.acc_type as accType,fidt.interest_sign as interestSign ");
        if (flag) {//一借一贷
            selectExp.append(",fidt.account as blnctrAcc,fidt.corp_id as corpId ");
        } else {
            if (!isAccInCenter) {
                selectExp.append(",'' as blnctrAcc,fidt.corp_id as corpId ");
            } else {
                selectExp.append(",'' as blnctrAcc,'' as corpId ");
            }
        }
        pagesql.append("  from fc_interest_dtl_temp fidt  where fidt.net_id = :netCode ");
        pageParams.put("netCode", netCode);
        if (StringUtils.isNotBlank(accType)) {
            pagesql.append(" and fidt.acc_type in (:accType) ");
            pageParams.put("accType", accType.split(","));
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            pagesql.append(" and fidt.generate_mode = :generateMode ");//利息产生方式-P-批量计息 默认查询批量计息
            pageParams.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            pagesql.append(" and fidt.account = :account ");
            pageParams.put("account", account);
        }
        if ("initList".equals(opType)) {
            pagesql.append(" and ((fidt.bus_Status = :tempStatus or fidt.bus_Status = :backStatus) and fidt.user_Session_Id = :userSessionId) and fidt.interest <> 0 ");
            pageParams.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            pageParams.put("backStatus", InterestConstant.INTEREST_BACK);
            pageParams.put("userSessionId", sessionId);
        } else if ("validList".equals(opType)) {
            pagesql.append(" and fidt.bus_Status = :busStatus and fidt.user_Session_Id = :userSessionId ");
            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
            pageParams.put("userSessionId", sessionId);
        } else {
            pagesql.append(" and fidt.bus_Status = :busStatus and fidt.user_Session_Id <> :userSessionId and fidt.interest <> 0 ");
            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
            pageParams.put("userSessionId", sessionId);
        }
//        pagesql.append(" and acc.acc_Type <> '"+InnerAccountConstant.FCACC_LOANBYMANDATE+"' ");
        pagesql.append(" order by fidt.acc_type, fidt.cur_id, fidt.interest_sign ");
        return createSQLQuery(selectExp.toString() + pagesql.toString(), pageParams).list();
    }

    /**
     * <b>功能介绍：</b>获取构造冲预提凭证的利息分类<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param searchBean
     * @param account
     * @param opType
     * @param isMoreVoucherDtl
     * @return
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, String>> queryRushInterestVoucherHead(InterestSearchBean searchBean, String account,
                                                                  String opType, boolean isMoreVoucherDtl) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pre.cur_id as curId,pre.cur_name as curName,pre.acc_type as acctype");
        if (isMoreVoucherDtl) {
            sql.append(",'' as blnctracc ");
        } else {
            // acc.blnctr_acc 关联字段
            sql.append(",pre.account as blnctracc ");
        }
        sql.append(rushPreDrawSql(searchBean, "", "", account, opType));
        if ("initList".equals(opType)) {
            sql.append("and exists (");
            sql.append("select 1 from fc_predraw_rush_dtl rdtl where rdtl.predraw_dtl_id = pre.id and rdtl.operator_code = :opCode) ");
            params.put("status", new Integer[]{InterestConstant.INTEREST_STATUE_TEMP, InterestConstant.INTEREST_BACK});
        } else {
            sql.append("and not exists (");
            sql.append("select 1 from fc_predraw_rush_dtl rdtl where rdtl.predraw_dtl_id = pre.id and rdtl.operator_code = :opCode) ");
            params.put("status", new Integer[]{InterestConstant.INTEREST_WAIT_CHECK});
        }
        sql.append("order by pre.acc_type,pre.cur_id");
        params.put("netId", searchBean.getNetId());
        params.put("endDate", searchBean.getBlnInstDate());
        params.put("accType", searchBean.getAccType());
        params.put("account", account);
        params.put("opCode", searchBean.getUserId());
        return listBySQL(sql.toString(), params);
    }

    /**
     * <b>功能介绍：</b>查询凭证数据<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<FcInterestDtlTemp> createCanEditInterestVoucher(String netCode, String accType, String curCode,
                                                                String interestSign, String opType, String sessionId, String account, String corpCode, String generateMode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql1 = new StringBuilder();
        sql1.append("select dtl.* from fc_interest_dtl_temp dtl where 1 = 1 ");
        if ("initList".equals(opType)) {
            sql1.append(" and (dtl.bus_status = :tempStatus or dtl.bus_status = :backStatus) ");
            sql1.append(" and dtl.user_session_id = :userSessionId ");
            params.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            params.put("backStatus", InterestConstant.INTEREST_BACK);
            params.put("userSessionId", sessionId);
        } else {
            sql1.append(" and dtl.bus_status = :busStatus ");
            params.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql1.append(" and dtl.generate_mode = :generateMode ");//利息产生方式-P-批量计息 默认查询批量计息
            params.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql1.append(" and dtl.account = :account ");
            params.put("account", account);
        }
        sql1.append(" and dtl.interest_sign = :interestSign ");
        sql1.append(" and dtl.net_id = :netCode ");
        sql1.append(" and dtl.acc_type = :accType ");
        if (!StringUtils.isBlank(corpCode)) {
            sql1.append(" and dtl.corp_id = :corpCode ");
            params.put("corpCode", corpCode);
        }
        sql1.append(" and dtl.cur_id = :curCode ");
        params.put("netCode", netCode);
        params.put("accType", accType);
        params.put("curCode", curCode);
        params.put("userSessionId", sessionId);
        params.put("interestSign", interestSign);
        return listBySQLAliasToBean(sql1.toString(), params, FcInterestDtlTemp.class);
    }

    /**
     * <b>功能介绍：</b>获取构造凭证的利息分类<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> queryVoucherHeadList0(String netCode, String opType, String sessionId, boolean isAccInCenter, boolean flag) {
        String selectExp = "select a.cur_id,a.acc_type,d.interest_sign";
        if (flag) {
            selectExp += ",a.blnctr_acc,a.corp_id";
        } else {
            if (!isAccInCenter) {
                selectExp += ",'' as blnctr_acc,a.corp_id";
            } else {
                selectExp += ",'' as blnctr_acc,'' as corp_id";
            }
        }
        String pageSql = " from fc_interest_dtl_temp d,fc_acc a where d.account = a.blnctr_acc and a.net_id = :netId ";
        pageSql += "  and a.acc_type = (:accType) ";
        Map<String, Object> pageParams = new HashMap<String, Object>();
        pageParams.put("netId", netCode);
        pageParams.put("accType", InnerAccountConstant.FCACC_LOANBYMANDATE);
        if ("initList".equals(opType)) {
            pageSql += " and (( d.bus_status = :tempStatus or d.bus_status = :backStatus) and d.user_session_id = :userSessionId  ) and d.interest <> 0 ";
            pageParams.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            pageParams.put("backStatus", InterestConstant.INTEREST_BACK);
            pageParams.put("userSessionId", sessionId);
        } else if ("validList".equals(opType)) {
            pageSql += " and d.bus_status = :busStatus and d.user_session_id = :userSessionId ";
            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
            pageParams.put("userSessionId", sessionId);
        } else {
            pageSql += " and d.bus_status = :busStatus and d.user_session_id <> :userSessionId and d.interest <> 0 ";
            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
            pageParams.put("userSessionId", sessionId);
        }
        pageSql += " order by a.acc_type,a.cur_id,a.interest_sign ";
        return createSQLQuery(selectExp + pageSql, pageParams).list();
    }

    /**
     * <b>功能介绍：</b>更新此次计息会话中结息账户的积数计息标志和计息时间<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void updateAccuForBatch(String sessionId, Date endInstDate, Date interestTime, String interestSign, String netId) {
        StringBuilder sql = new StringBuilder();
        sql.append("update fc_accu_balance accu set accu.interest_sign = :interestSign,");
        if (InterestConstant.INTEREST_SIGN_NO.equals(interestSign)) {
            sql.append("accu.pre_sign = '1',");
        } else {
            sql.append("accu.pre_sign = '0',");
        }
        sql.append("accu.interest_time = :interestTime where exists ");
        sql.append("(select 1 from fc_interest_dtl_temp t ");
        sql.append("where  accu.account = t.account ");
        if (InterestConstant.INTEREST_SIGN_NO.equals(interestSign)) {
            sql.append("and t.interest_sign = :interestSign  ");
        } else {
            sql.append("and t.interest_sign = :interestSign ");
        }
        if (EmptyUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id = :netId ");
        }
        sql.append("and t.user_session_id = :sessionId ");
        sql.append("and accu.balance_date >= t.interest_start_date ");
        sql.append("and accu.balance_date <= t.bln_inst_date) ");
        sql.append("and accu.balance_date < :endInstDate ");
        sql.append("and accu.interest_sign = '0' ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", netId);
        params.put("interestSign", interestSign);
        params.put("interestTime", interestTime);
        params.put("sessionId", sessionId);
        params.put("endInstDate", endInstDate);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * <b>功能介绍：</b>更新积数的计息日期(不能是已计息的数据)<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void updateAccuBalancesInterestTime(List<FcPredrawDtl> predrawDtls, Date interestTime) {
        StringBuilder sql = new StringBuilder();
        sql.append("update FC_ACCU_BALANCE a set a.interest_sign = '0', a.interest_time = :interestTime ");
        sql.append("where exists (select 1 from FC_PREDRAW_DTL b where b.interest_time = a.interest_time and b.account = a.account ");
        List<String> predrawDtlIds = new ArrayList<String>();
        for (FcPredrawDtl istFcPredrawDtl : predrawDtls) {
            predrawDtlIds.add(istFcPredrawDtl.getId());
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("interestTime", interestTime);
        params.put("predrawDtls", predrawDtlIds);
        String whereIn = "and " + getSQLIn(predrawDtlIds, 900, "b.id");
        sql.append(whereIn);
        sql.append(" and ((a.interest_sign <> '1' and b.acc_type not in ('02', '04', '06')))  or (b.acc_type in ('02', '04', '06')) ");
        sql.append(") ");
        //sql.append("where t.blnctr_acc = a.account and t.acc_type not in ('02', '04', '06'))) or ");
        //sql.append("(exists (select 1 from fc_acc t where t.blnctr_acc = a.account and t.acc_type in ('02', '04', '06')))) ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 采用hibernate进行批量插入
     */
    @SuppressWarnings("rawtypes")
    public void addBatchByHql(List params) {
        Session session = this.getSession();
        int i = 0;
        for (i = 0; i < params.size(); i++) {
            Object obj = params.get(i);
            session.saveOrUpdate(obj);
            if (i % 20 == 0) {
                session.flush();
                session.clear();
            }
        }
    }

    /**
     * <b>功能介绍：</b>不足款清单复核通过<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void checkLessPayBill(String netCode, String accType, Date endInstDate, Integer status) {
        List<String> accList = iFcAccService.getBlnctrAccByNetIdAndAccType(netCode, accType);
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("update fc_less_pay_bill b set b.status = :status ");
        sql.append("where 1 =1  ");
        if (EmptyUtils.isNotEmpty(accList)) {
            sql.append(" and b.account in(:accList) ");
            params.put("accList", accList);
        }
        sql.append(" and b.end_inst_date < :endInstDate ");
        params.put("endInstDate", endInstDate);
        params.put("netId", netCode);
        params.put("status", status);

        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * <b>功能介绍：</b>查询未冲销的预提信息<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<FcPredrawDtl> queryUnRushPredrawDtls(InterestCommonBean bean, String curCode, String opType) {
        List<String> accList = iFcAccService.getBlnctrAccByNetId(bean.getNetCode());
        if (CollectionUtils.isEmpty(accList)) {
            return Lists.newArrayList();
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer("select predrawDtl.* from fc_predraw_dtl predrawDtl ");
                //.append(" join fc_acc interestAcc on predrawDtl.account=interestAcc.blnctr_acc ")
        hql.append(" join fc_online_ledger led on predrawDtl.account=led.account");
        hql.append(" where predrawDtl.account in(:accList)");
        if (!StringUtils.isBlank(bean.getAccTypeCode())) {
            hql.append(" and predrawDtl.acc_Type = :accTypeCode");
            params.put("accTypeCode", bean.getAccTypeCode());
            if (!StringUtils.isBlank(bean.getAccount())) {
                hql.append(" and predrawDtl.account = :account ");
                params.put("account", bean.getAccount());
            }
            if (!StringUtils.isBlank(curCode)) {
                hql.append(" and predrawDtl.cur_id = :curCode ");
                params.put("curCode", curCode);
            }
        }
        if (bean.getEndInstDate() != null) {
            hql.append(" and not exists (select 1 from fc_interest_dtl dtl where dtl.predraw_dtl_id = predrawDtl.id ");
            hql.append(" and dtl.bln_Inst_Date >= TO_DATE(:endInstDate,'yyyy-MM-dd') )");
//            params.put("endInstDate", bean.getEndInstDate());
            params.put("endInstDate", DateUtils.formatDate(bean.getEndInstDate()));
        }
        if ("temp".equals(opType)) {
            hql.append(" and not exists (select 1 from ( select rush.predraw_dtl_id,sum(rush.strike_interest) interest");
            hql.append(" from fc_predraw_rush_dtl rush where rush.status in (:status)  group by rush.predraw_dtl_id ) c");
            hql.append(" where c.predraw_dtl_id = predrawDtl.id and c.interest >= predrawDtl.interest) ");
            params.put("status", new Integer[]{InterestConstant.INTEREST_WAIT_CHECK, InterestConstant.INTEREST_STATUE_CHECK});
            params.put("opCode", bean.getUserId());
        }else if ("submit".equals(opType)) {
            hql.append(" and exists (select 1 from ( select rush.predraw_dtl_id,sum(rush.strike_interest) interest")
                    .append(" from fc_predraw_rush_dtl rush where rush.status in (:status) group by rush.predraw_dtl_id ) c")
                    .append(" where c.predraw_dtl_id = predrawDtl.id and c.interest >= predrawDtl.interest) ");
            params.put("status", new Integer[]{InterestConstant.INTEREST_STATUE_TEMP, InterestConstant.INTEREST_BACK});
            params.put("opCode", bean.getUserId());
        } else if ("check".equals(opType) || "back".equals(opType)) {
            hql.append(" and exists (select 1 from fc_predraw_rush_dtl rush where rush.predraw_dtl_id = predrawDtl.id");
            hql.append(" and rush.status = :status and rush.operator_code <> :opCode)");
            params.put("status", InterestConstant.INTEREST_WAIT_CHECK);
//            params.put("opCode", bean.getUserBean().getId());
            params.put("opCode", bean.getUserId());
        }
        params.put("accList", accList);
        SQLQuery query = createSQLQuery(hql.toString());
        query.addEntity("predrawDtl", FcPredrawDtl.class);
        query.setProperties(params);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.list();
        List<FcPredrawDtl> predrawDtls = new ArrayList<FcPredrawDtl>();
        for (Map<String, Object> map : list) {
            FcPredrawDtl predrawDtl = (FcPredrawDtl) map.get("predrawDtl");
            predrawDtls.add(predrawDtl);
        }
        return predrawDtls;
    }

    /**
     * <b>功能介绍：</b>获取账户冲预提金额<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public BigDecimal getRushPreDrawMoney(String account) {
        StringBuilder sql = new StringBuilder();
        sql.append("select case when sum(pre.interest-case when rush.rushMoney is null  ");
        sql.append("then 0 else rush.rushMoney end) is null then 0  ");
        sql.append("else sum(pre.interest-case when rush.rushMoney is null then 0 else rush.rushMoney end) end  as MONEY");
        sql.append(" from (select predtl.id,sum(predtl.interest) interest  ");
        sql.append("from fc_predraw_dtl predtl where predtl.account= :account1 group by predtl.id) pre ");
        sql.append("left join (select rushdtl.predraw_dtl_id,sum(rushdtl.STRIKE_INTEREST) rushMoney ");
        sql.append("from fc_predraw_rush_dtl rushdtl where exists (select 1 from fc_interest_dtl t1 ");
        sql.append("where t1.predraw_dtl_id = rushdtl.predraw_dtl_id and rushdtl.status >= 11 and t1.account= :account2 ) ");
        sql.append("group by rushdtl.predraw_dtl_id ) rush ");
        sql.append("on rush.predraw_dtl_id=pre.id ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account1", account);
        params.put("account2", account);
        BigDecimal rushMoney = BigDecimal.ZERO;
        List list = null;
        if (StringUtils.isNotBlank(account)) {
            list = listBySQL(sql.toString(), params);
            rushMoney = (BigDecimal) ((Map) list.get(0)).get("MONEY");
        }
        return rushMoney;
    }

    /**
     * <b>功能介绍：</b>查询未复核金额<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public BigDecimal getRushUnCheckDrawMoney(String depositManagerId) {
        Map<String, Object> params = new HashMap<String, Object>();
        BigDecimal rushMoney = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(depositManagerId)) {
            //本次暂存冲销的预提
            String sqlRushed = "SELECT BT_NVL_NUM(SUM(a.strike_interest),0) FROM fc_predraw_rush_dtl a" +
                    " WHERE a.deposit_manager_id = :depositManagerId ";
            params.put("depositManagerId", depositManagerId);
            List rushedList = createSQLQuery(sqlRushed, params).list();
            if (rushedList != null && rushedList.size() > 0) {
                BigDecimal rushedMoney = new BigDecimal(rushedList.get(0).toString());
                rushMoney = rushMoney.add(rushedMoney);
            }
        }
        return rushMoney;
    }

    /**
     * <b>功能介绍：</b>批量更新冲预提状态<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void updatePredrawRushStatus(String netCode, String accTypeCode, Date endDate, Integer[] beforeStatus,
                                        Integer afterStatus, String account) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("update fc_predraw_rush_dtl rush set rush.status = :afterStatus ");
        sql.append("where exists( select 1 from fc_predraw_dtl pre where  ");
        sql.append(" rush.predraw_dtl_id= pre.id and pre.net_id = :netId ");
        sql.append("and pre.acc_type = :accType ");
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and pre.account = :account ");
            params.put("account", account);
        }
        sql.append(") and not exists ( select 1 from fc_interest_dtl dtl  ");
        sql.append("where dtl.predraw_dtl_id = rush.predraw_dtl_id and dtl.bln_Inst_Date >:endDate) ");
        sql.append("and rush.status in (:beforeStatus) ");
        params.put("netId", netCode);
        params.put("accType", accTypeCode);
        params.put("endDate", endDate);
        params.put("beforeStatus", beforeStatus);
        params.put("afterStatus", afterStatus);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * <b>功能介绍：</b>查询账户待冲销预提金额<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAccRushInterest(String netCode, String accTypeCode, Date endDate, String account) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pre.id as PREDRAW_DTL_ID,pre.interest - (case when rush.strike_interest is null then ");
        sql.append("0 else rush.strike_interest end) RUSHMONEY from fc_predraw_dtl pre ");
        sql.append(" left join (select a.predraw_dtl_id, sum(a.strike_interest) strike_interest ");
        sql.append("from fc_predraw_rush_dtl a where a.status in (95) group by a.predraw_dtl_id) rush on pre.id = rush.predraw_dtl_id ");
        sql.append("and rush.strike_interest < pre.interest where pre.acc_type = :accType and pre.net_id = :netId ");
        sql.append("and pre.interest - (case when rush.strike_interest is null then 0 else rush.strike_interest end) > 0 ");
        if (endDate != null) {
            sql.append("and not exists (select 1 from fc_interest_dtl f where f.account = pre.account ");
            sql.append("and f.interest_sign = '0' and f.bln_inst_date > :endDate) ");
            params.put("endDate", endDate);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append("and pre.account = :account ");
            params.put("account", account);
        }
        sql.append(" order by pre.id");
        params.put("netId", netCode);
        params.put("accType", accTypeCode);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
        sqlQuery.addScalar("PREDRAW_DTL_ID", StandardBasicTypes.STRING);
        sqlQuery.addScalar("RUSHMONEY", StandardBasicTypes.BIG_DECIMAL);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    /**
     * <b>功能介绍：</b>冲预提金额<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netCode
     * @param accTypeCode
     * @param curCode
     * @param endInstDate
     * @param account
     * @param opType
     * @return
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public BigDecimal getAccRushInterest(String netCode, String accTypeCode, String curCode, Date endInstDate,
                                         String account, String opType) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(a.strike_interest) rushMoney from fc_predraw_rush_dtl a ");
        sql.append("where exists (");
        sql.append("select 1 from fc_interest_dtl b where  b.predraw_dtl_id = a.predraw_dtl_id ");
        sql.append("and b.net_id = :netId and b.acc_type = :accType and b.cur_id = :curId ");
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append("and b.account = :account ");
            params.put("account", account);
        }
        sql.append(") ");
        if (endInstDate != null) {
            sql.append("and not exists (");
            sql.append("select 1 from fc_interest_dtl d where d.predraw_dtl_id = a.predraw_dtl_id and d.bln_inst_date > :endInstDate) ");
            params.put("endInstDate", endInstDate);
        }
        sql.append("and a.status in (:status) ");
        params.put("netId", netCode);
        params.put("accType", accTypeCode);
        params.put("curId", curCode);
        if ("initList".equals(opType)) {
            params.put("status", new Integer[]{InterestConstant.INTEREST_STATUE_TEMP, InterestConstant.INTEREST_BACK});
        } else {
            params.put("status", new Integer[]{InterestConstant.INTEREST_WAIT_CHECK});
        }
        List list = createSQLQuery(sql.toString(), params).list();
        BigDecimal rushMoney = BigDecimal.ZERO;
        if (list != null && list.size() > 0) {
            return (BigDecimal) list.get(0);
        }
        return rushMoney;
    }

    /**
     * <b>功能介绍：</b>查询待冲预提明细<br>
     * <b>日期：</b>2017年11月4日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netId
     * @param accType
     * @param curId
     * @param endInstDate
     * @param account
     * @param opType
     * @return
     * @version 1.0
     */
    public List<FcPredrawDtl> queryWaitRushPredrawDtls(String netId, String accType, String curId,
                                                       Date endInstDate, String account, String opType) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select pre.id,pre.interest_time,pre.order_no,pre.account,pre.cur_code,pre.interest,pre.balance,");
        sql.append("pre.strike_sign,pre.strike_date,pre.strike_interest,pre.sign from fc_predraw_dtl pre ");
        //  sql.append("left join fc_acc acc on pre.account= acc.blnctr_acc ");
        sql.append("where pre.net_id = :netId ");
        if (EmptyUtils.isNotEmpty(accType)) {
            sql.append("and pre.acc_type = :accType ");
            params.put("accType", accType);
            if (EmptyUtils.isNotEmpty(account)) {
                sql.append("and pre.account = :account ");
                params.put("account", account);
            }
            if (EmptyUtils.isNotEmpty(curId)) {
                sql.append("and pre.cur_id = :curId ");
                params.put("curId", curId);
            }
        }
        if (endInstDate != null) {
            sql.append("and not exists (");
            sql.append("select 1 from fc_interest_dtl dtl  where dtl.predraw_dtl_id = pre.id and dtl.bln_inst_date > :endInstDate) ");
            params.put("endInstDate", endInstDate);
        }
        sql.append("and exists (");
        sql.append("select 1 from fc_predraw_rush_dtl rush where rush.predraw_dtl_id = pre.id and rush.status in (:status)) ");
        sql.append("order by pre.account,pre.interest_time ");
        if ("initList".equals(opType)) {
            params.put("status", new Integer[]{InterestConstant.INTEREST_STATUE_TEMP, InterestConstant.INTEREST_BACK});
        } else {
            params.put("status", new Integer[]{InterestConstant.INTEREST_WAIT_CHECK});
        }
        params.put("netId", netId);
        return listBySQLAliasToBean(sql.toString(), params, FcPredrawDtl.class);
    }

    /**
     * <b>功能介绍：</b>查询计息账户的积数,适合账户有积数余额<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<FcAccuBalancef> queryAccuBalanceForHave(String netCode, String[] accTypeCode, Date endInstDate,
                                                        String... sysSubjectFilters) {

        Map<String, Date> fcAccMap = iFcAccService.getInterestStartDate(netCode, accTypeCode, sysSubjectFilters);
        StringBuilder sql = new StringBuilder();
        sql.append("select fab.id as id ,fab.balance_date as balanceDate,fab.account as account,fab.cur_code as curCode,fab.cumu as cumu,fab.adjust_cumu as adjustCumu, ");
        sql.append("fab.agreed_deposit_sign as agreedDepositSign,fab.interest_sign as interestSign,fab.pre_sign as preSign, ");
        sql.append("fab.interest_time as interestTime,fab.a_adjusted as aAdjusted,fab.agreed_deposit_id as agreedDepositId,fab.agreed_type as agreedType ");
        sql.append("from FC_ACCU_BALANCE fab ");
        sql.append("where  fab.interest_sign = '0' ");
        sql.append("and fab.account in (:fcAccList)   ");
        sql.append("and fab.balance_date < :endInstDate   ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("endInstDate", endInstDate);
//        params.put("fcAccList", fcAccMap.keySet());
        List<String> fcAccList = new ArrayList<>();
        for (String key : fcAccMap.keySet()) {
            fcAccList.add(key);
        }
        params.put("fcAccList", fcAccList);
        if (ListUtils.isNullOrEmpty(fcAccList)) {
            return Lists.newArrayList();
        }
//        params.put("fcAccList","222222");
        sql.append(" order by fab.account, fab.balance_date ");
        List<FcAccuBalancef> listAccuBalance = listBySQLAliasToBean(sql.toString(), params, FcAccuBalancef.class);
        List<FcAccuBalancef> newListAccuBalance = new ArrayList<>();
        for (FcAccuBalancef balancef : listAccuBalance) {
            Date balanceDate = balancef.getBalanceDate();
            Date interestStartDate = fcAccMap.get(balancef.getAccount());
            Calendar calendar = Calendar.getInstance(); //创建Calendar 的实例
            calendar.setTime(interestStartDate);
            calendar.add(Calendar.DAY_OF_MONTH, -1); //当前时间减去一天，即一天前的时间
            if (balanceDate.compareTo(calendar.getTime()) >= 0) {
                newListAccuBalance.add(balancef);
            }
        }
        return newListAccuBalance;
    }

    /**
     * <b>功能介绍：</b>查询指定计息账户的积数,适合账户有积数余额<br>
     * <b>日期：</b>2019年5月21日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public List<FcAccuBalancef> queryAccuBalanceForAcc(String account, Date endInstDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fab.id as id ,fab.balance_date as balanceDate,fab.account as account,fab.cur_code as curCode,fab.cumu as cumu,fab.adjust_cumu as adjustCumu, ");
        sql.append("fab.agreed_deposit_sign as agreedDepositSign,fab.interest_sign as interestSign,fab.pre_sign as preSign, ");
        sql.append("fab.interest_time as interestTime,fab.a_adjusted as aAdjusted,fab.agreed_deposit_id as agreedDepositId,fab.agreed_type as agreedType ");
        sql.append("from FC_ACCU_BALANCE fab ");
        sql.append("where  fab.interest_sign = '0'  ");
        sql.append("and fab.balance_date < :endInstDate ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("endInstDate", endInstDate);
        if (StringUtils.isNotBlank(account)) {
            sql.append(" and fab.account = :account ");
            params.put("account", account);
        }
        sql.append("  order by fab.account, fab.balance_date ");
        List<FcAccuBalancef> listAccuBalance = listBySQLAliasToBean(sql.toString(), params, FcAccuBalancef.class);
        List<String> accounts = listAccuBalance.stream().map(FcAccuBalancef::getAccount).collect(Collectors.toList());
        Map<String, Date> fcAccMap = iFcAccService.getInterestStartDate(accounts);
        List<FcAccuBalancef> newListAccuBalance = new ArrayList<>();
        for (FcAccuBalancef balancef : listAccuBalance) {
            Date balanceDate = balancef.getBalanceDate();
            Date interestStartDate = fcAccMap.get(balancef.getAccount());
            Calendar calendar = Calendar.getInstance(); //创建Calendar 的实例
            calendar.setTime(interestStartDate);
            calendar.add(Calendar.DAY_OF_MONTH, -1); //当前时间减去一天，即一天前的时间
            if (balanceDate.compareTo(calendar.getTime()) >= 0) {
                newListAccuBalance.add(balancef);
            }
        }
        return newListAccuBalance;
    }
    /**
     * <b>功能介绍：</b>查询计息账户的利率调整记录<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    //TODO 移动到基础模块
    /*public List<AccInstrateBean> queryInstRateAdjustInfos(String netCode, String accTypeCode, Date endInstDate,
                                                          Integer[] corpIds, String instAcc) {
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
        params.put("status", InterestConstant.INTEREST_STATUE_CHECK);
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
    }*/
//    /** 没有调用放，直接注释掉
//     * <b>功能介绍：</b>加载不足款清单<br>
//     * <b>日期：</b>2017年7月11日<br>
//     * <b>作者：</b>caozx<br>
//     *
//     * @version 1.0
//     */
//    public List<FcLessPayBillVO> createCanEditLessPay(String netCode, String opType, String sessionId) {
//        Map<String, Object> pageParams = new HashMap<String, Object>();
//        StringBuilder sql = new StringBuilder();
//
//        sql.append("select account,interest,accName,accForDeduct,balance ");
//        sql.append("from (select tt.account as account,tt.interest as interest,fc.ACC_NAME as accName,fc.ACC_FOR_DEDUCT as accForDeduct,led.check_balance balance  ");
//        sql.append("from fc_acc fc,fc_online_ledger led,(select a.account, sum(a.interest) interest from fc_interest_dtl_temp a where exists (select 1 from fc_acc acc ");
//        sql.append("where acc.blnctr_acc = a.account and acc.acc_type = :accTypeCode ) ");
//        sql.append("and a.user_session_id = :sessionId and a.bus_status = :busStatus and a.repay_money is null and a.interest_sign = '1' ");
//        sql.append("group by a.account) tt where fc.acc_for_deduct = led.account and fc.BLNCTR_ACC = tt.account and led.check_balance < tt.interest ");
//        if ("initList".equals(opType)) {
//            pageParams.put("stauts", InterestConstant.INTEREST_BACK);
//            pageParams.put("busStatus", InterestConstant.INTEREST_STATUE_TEMP);
//        } else {
//            sql.append("and 1 <> 1 ");
//            pageParams.put("stauts", InterestConstant.INTEREST_WAIT_CHECK);
//        }
//        sql.append("union select bill.account as account,bill.interest as interest,fcAcc.ACC_NAME as accName,fcAcc.ACC_FOR_DEDUCT as accForDeduct,bill.balance as balance ");
//        sql.append("from fc_less_pay_bill bill, fc_acc fcAcc where bill.account = fcAcc.Blnctr_Acc and bill.status = :stauts) ");
//        sql.append("order by account ");
//        pageParams.put("netCode", netCode);
//        pageParams.put("sessionId", sessionId);
//        pageParams.put("accTypeCode", InnerAccountConstant.FCACC_LOAN);
//        //FIXME 不足款清单subjectID设置
//        return listBySQLAliasToBean(sql.toString(), pageParams, FcLessPayBillVO.class);
//    }

    /**
     * <b>功能介绍：</b>获取积数天数(企业定期，银行定期，通知存款)<br>
     * <b>日期：</b>2017年7月12日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({"unchecked", "unchecked"})
    public Long getNoAccuBalanceCounts(String netCode, String accType, String opType, String sessionId, String account, String generateMode) {
        Map<String, Object> pageParams = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select BT_NVL_NUM(sum(t.days), 0) as DAYS from fc_interest_dtl_temp t ");
        sql.append("where  t.acc_type in ('05', '06', '08') and t.net_id = :netCode ");
        if (StringUtils.isNotBlank(accType)) {
            sql.append("and t.acc_type in (:accType) ");
            pageParams.put("accType", accType.split(","));
        }
        if (StringUtils.isNotBlank(account)) {
            sql.append(" and t.account = :account ");
            pageParams.put("account", account);
        }
        if (StringUtils.isNotBlank(generateMode)) {
            sql.append(" and t.generate_Mode = :generateMode ");
            pageParams.put("generateMode", generateMode);
        }
        if ("initList".equals(opType)) {
            sql.append("and (( t.bus_Status = :tempStatus or t.bus_Status = :backStatus ) and t.user_Session_Id = :userSessionId) ");
            pageParams.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            pageParams.put("backStatus", InterestConstant.INTEREST_BACK);
            pageParams.put("userSessionId", sessionId);
        } else {
            sql.append(" and t.bus_Status = :busStatus ");
            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        }
        pageParams.put("netCode", netCode);
        List<Map<String, Object>> list = listBySQL(sql.toString(), pageParams);
        BigDecimal count = new BigDecimal(list.get(0).get("DAYS").toString());
        return count.longValue();
    }

    /**
     * <b>功能介绍：</b>积数余额表账号总数(不包括企业定期，银行定期，通知存款)<br>
     * <b>日期：</b>2017年7月12日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public Long getAccuBalanceCounts(String netCode, String accType, String opType, String sessionId, String account, String generateMode) {
//        List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(netCode, accType);
        List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByAccountAndNetIdAndAccTypeCheckerCodeIsNotNull(account, netCode, accType);
//        Map<String, FcAccDTO> accountMap = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        StringBuilder sql = new StringBuilder();
        Map<String, Object> pageParams = new HashMap<String, Object>();
        //fc_acc 在 system 模块，现在需要连表查询统计
        List<Date> interestStartDateLists = fcAccDTOS.stream().map(FcAccDTO::getInterestStartDate).filter(Objects::nonNull).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        /*sql.append("select count(fab.ACCOUNT) from FC_ACCU_BALANCE fab, fc_acc acc where  acc.NET_id = :netCode ");
        sql.append(" and fab.ACCOUNT in(:accounts) ");
        pageParams.put("accounts", accountMap.keySet());
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and fab.account = :account ");
            pageParams.put("account", account);
        }*/
        sql.append("select count(fab.ACCOUNT) from FC_ACCU_BALANCE fab where 1=1 ");
        List<String> accounts=fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        if(accounts!=null){
            sql.append(" and ").append(SqlUtil.getInSql(accounts, "fab.ACCOUNT", true)).append(" ");
        }
        sql.append("and ( exists (select fidt.id from FC_INTEREST_DTL_TEMP fidt where fidt.account = fab.ACCOUNT ");
        if ("initList".equals(opType)) {
            sql.append("and (fidt.BUS_STATUS = :tempStatus and ");
            sql.append("fab.INTEREST_SIGN = '0' or ");
            sql.append("fidt.BUS_STATUS = :backStatus and ");
            sql.append("fab.INTEREST_TIME = fidt.INTEREST_TIME) ");
            sql.append("and fidt.USER_SESSION_ID = :userSessionId ");
            sql.append("AND fidt.net_id = :netCode");
            pageParams.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            pageParams.put("backStatus", InterestConstant.INTEREST_BACK);
            pageParams.put("userSessionId", sessionId);
            pageParams.put("netCode", netCode);
        } else {
            sql.append("and (fidt.BUS_STATUS = :busStatus and fab.INTEREST_TIME = fidt.INTEREST_TIME) ");
            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        }
        sql.append(" and fidt.INTEREST_START_DATE <= fab.BALANCE_DATE and fidt.BLN_INST_DATE >= fab.BALANCE_DATE ");
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and fidt.GENERATE_MODE =:generateMode ");
            pageParams.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and fidt.account = :account ");
            pageParams.put("account", account);
        }
        sql.append(" ))");

//        sql.append(" and (fab.BALANCE_DATE >= acc.INTEREST_START_DATE - 1) ");
        sql.append(" and not exists ( (select 1 from FC_INTEREST_DTL fid where fid.account = fab.ACCOUNT and fid.INTEREST_TIME = fab.INTEREST_TIME ");
        sql.append(" and fid.interest_sign = '1' ");
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and fid.GENERATE_MODE =:generateMode ");
            pageParams.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and fid.account = :account ");
            pageParams.put("account", account);
        }
        sql.append(" ))");
        pageParams.put("netCode", netCode);

        if (CollectionUtils.isNotEmpty(interestStartDateLists)) {
            sql.append(" and (");
            interestStartDateLists.forEach(a -> {
                if (null != a) {
//                sql.append(" (fab.BALANCE_DATE >= :a - 1) or ");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    String date = sdf.format(a);
//                    sql.append(" (fab.BALANCE_DATE >= DATE_SUB('").append(date).append("',INTERVAL 1 DAY)) or ");
                    //Oracle 不支持 DATE_SUB函数, 且日期比较使用 TO_DATE('2023-12-18','yyyy-MM-dd')，mySql和Oracle都支持
                    String newDate = this.convertUtilDateToLocalDate(a);
                    sql.append(" (fab.BALANCE_DATE >= TO_DATE('").append(newDate).append("', 'yyyy-MM-dd')) or ");
                }
            });
            int or = sql.lastIndexOf("or");
//            sql.append(" ) ");
            String substring = sql.substring(0, or) + ")";
            BigDecimal count = (BigDecimal) createSQLQuery(substring, pageParams).uniqueResult();
            return count.longValue();
        }

        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), pageParams).uniqueResult();
        return count.longValue();
    }

    private String convertUtilDateToLocalDate(Date date) {
        if (date != null) {
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return localDate.minusDays(1).toString();
        } else {
            return null;
        }
    }

    /**
     * <b>功能介绍：</b>查询积数清单数据(不包括企业定期，银行定期，通知存款)<br>
     * <b>日期：</b>2017年7月12日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<Map<String, Object>> queryCanEditAccuBalance(DataTablesInput pgd, String netCode, String accType,
                                                             String opType, String sessionId, String account, String generateMode) {
        Map<String, Object> pageParams = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select fab.balance_date as BALANCEDATE,fab.account as ACCOUNT,dtl.acc_name as ACCNAME,BT_NVL_NUM(fab.cumu, 0) as CUMU, ");
        //原本这里关联的是 fc_acc 表，拿到唯一的 account；现在改成 fc_interest_dtl_temp 的话，就会存在一个问题：当第一次日结-计息-提交-未复核，此时再进行一次 日记-计息时，利息明细临时表 中就会 有多条数据
        //所以为了去重 直接 group by 处理
        sql.append("BT_NVL_NUM(fab.adjust_cumu, 0) as ADJUSTCUMU,dtl.acc_type as ACCTYPE from fc_accu_balance fab,fc_interest_dtl_temp dtl ");
        sql.append("where fab.account = dtl.account and dtl.net_id = :netCode ");
        if (StringUtils.isNotBlank(accType)) {
            sql.append("and dtl.acc_type in (:accType) ");
            pageParams.put("accType", accType.split(","));
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and fab.account = :account ");
            pageParams.put("account", account);
        }
        sql.append("and (exists (select fidt.id from fc_interest_dtl_temp fidt where fidt.account = fab.account ");
        sql.append("and fidt.bln_inst_date >= fab.balance_date ");
        sql.append("and fidt.interest_start_date <= fab.balance_date ");
        if ("initList".equals(opType)) {
            sql.append(" and (fidt.bus_status = :tempStatus and fab.interest_sign = '0' or fidt.bus_status = :backStatus)  ");
            sql.append(" and fidt.user_session_id = :userSessionId ");
            pageParams.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            pageParams.put("backStatus", InterestConstant.INTEREST_BACK);
            pageParams.put("userSessionId", sessionId);
        } else {
            sql.append(" and (fidt.bus_status = :busStatus and fab.interest_time = fidt.interest_time) ");
            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and fidt.generate_mode=:generateMode ");
            pageParams.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and fidt.account = :account ");
            pageParams.put("account", account);
        }
        sql.append("))");
        sql.append("and (fab.balance_date >= dtl.interest_start_date - 1) ");
        sql.append("and not (exists (select 1 from fc_interest_dtl fid where fid.account = fab.account and fid.interest_time = fab.interest_time ");
        sql.append(" and fid.interest_sign = '1' ");
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and fid.generate_mode = :generateMode ");
            pageParams.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and fid.account = :account ");
            pageParams.put("account", account);
        }
        sql.append("))");
        sql.append(" group by dtl.acc_type,dtl.ACCOUNT,fab.balance_date "); // 为了去重 fc_interest_dtl_temp 表的 account
        sql.append(" ,fab.account,dtl.acc_name,fab.cumu,fab.adjust_cumu "); // 兼容Oracle数据库而添加
        sql.append("order by dtl.acc_type,dtl.account,fab.balance_date ");
        pageParams.put("netCode", netCode);
        List<Map<String, Object>> list = listBySQL(sql.toString(), pgd.getStart(), pgd.getLength(), pageParams);
        return list;
    }

    /**
     * <b>功能介绍：</b>查询积数清单数据(企业定期，银行定期，通知存款)<br>
     * <b>日期：</b>2017年7月12日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryAccountNoAccuBalance(String netCode, String account, String accType, String opType,
                                                               String sessionId, String generateMode) {
        Map<String, Object> pageParams = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct A.ACCOUNT,A.STARTDATE,A.ENDDATE,A.COUNTDAYS,B.CUMULATION,B.DAYS,ACC.ACC_NAME ACCNAME,ACC.ACC_TYPE as ACCTYPE ");
        sql.append("from (select account,t.bln_inst_date as endDate,t.interest_start_date as startDate,t.days as countDays from fc_interest_dtl_temp t ");
        sql.append("where exists (select 1 from FC_ACC fc where fc.blnctr_acc = t.account and fc.net_id = :netCode and fc.acc_Type in ('05', '06', '08') ");
        if (StringUtils.isNotBlank(accType)) {
            sql.append("and fc.acc_type in (:accType0) ");
            pageParams.put("accType0", accType.split(","));
        }
        if ("initList".equals(opType)) {
            sql.append("and ((t.bus_Status = :tempStatus and t.user_Session_Id = :userSessionId) or t.bus_Status = :backStatus) ");
        } else {
            sql.append("and t.bus_status = :busStatus ");
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and t.generate_mode = :generateMode ");
            pageParams.put("generateMode", generateMode);
        }
        sql.append(") ) a,(select aa.* from fc_interest_dtl_temp aa where 1 = 1 and exists ");
        sql.append("(select 1 from FC_ACC fcc where fcc.blnctr_acc = aa.account and fcc.net_id = :netCode and fcc.acc_Type in ('05', '06', '08') ");
        if (StringUtils.isNotBlank(accType)) {
            sql.append("and fcc.acc_type in (:accType1) ");
            pageParams.put("accType1", accType.split(","));
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and aa.generate_mode = :generateMode ");
            pageParams.put("generateMode", generateMode);
        }
        if ("initList".equals(opType)) {
            sql.append("and ((aa.bus_Status = :tempStatus and aa.user_Session_Id = :userSessionId) or aa.bus_Status = :backStatus) ");
        } else {
            sql.append("and aa.bus_status = :busStatus ");
        }
        sql.append(")) b,FC_ACC acc where a.account = b.account and acc.BLNCTR_ACC = a.account and acc.acc_Type in ('05', '06', '08', '15') and acc.net_id = :netCode ");
        if (StringUtils.isNotBlank(accType)) {
            sql.append("and acc.acc_type in (:accType2) ");
            pageParams.put("accType2", accType.split(","));
        }
        if (StringUtils.isNotBlank(account)) {
            sql.append("and a.account = :account ");
            pageParams.put("account", account);
        }
        sql.append(" order by acc.acc_Type, a.account ");
        if ("initList".equals(opType)) {
            pageParams.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            pageParams.put("backStatus", InterestConstant.INTEREST_BACK);
            pageParams.put("userSessionId", sessionId);
        } else {
            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        }
        pageParams.put("netCode", netCode);
        List<Map<String, Object>> list = listBySQL(sql.toString(), pageParams);
        return list;
    }
//    @SuppressWarnings({"unchecked", "rawtypes"})
//    public DataTablesOutput queryCanEditAccuBalanceNew(DataTablesInput input, String netCode, String accType,
//                                                       String opType, String sessionId) {
//        Map<String, Object> pageParams = new HashMap<String, Object>();
//        StringBuilder sql = new StringBuilder();
//        sql.append("select fab.balance_date as balancedate,fab.account as accor,acc.acc_name as accName,BT_NVL_NUM(fab.cumu, 0) as cumu, ");
//        sql.append("BT_NVL_NUM(fab.adjust_cumu, 0) as adjustCumu,acc.acc_type as accType ,fab.account as account from fc_accu_balance fab,fc_acc acc ");
//        sql.append("where fab.account = acc.blnctr_acc and acc.net_id = :netCode ");
//        if (StringUtils.isNotBlank(accType)) {
//            sql.append("and acc.acc_type in (:accType) ");
//            pageParams.put("accType", accType.split(","));
//        }
//        sql.append("and (exists (select fidt.id from fc_interest_dtl_temp fidt where fidt.account = fab.account ");
//        sql.append("and fidt.bln_inst_date >= fab.balance_date and fidt.interest_start_date <= fab.balance_date ");
//        if ("initList".equals(opType)) {
//            sql.append("and (fidt.bus_status = :tempStatus and fab.interest_sign = '0' or fidt.bus_status = :backStatus and ");
//            sql.append(" fab.interest_time = fidt.interest_time) and fidt.user_session_id = :userSessionId)) ");
//
//            pageParams.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
//            pageParams.put("backStatus", InterestConstant.INTEREST_BACK);
//            pageParams.put("userSessionId", sessionId);
//        } else {
//            sql.append("and (fidt.bus_status = :busStatus and fab.interest_time = fidt.interest_time) ");
//            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
//        }
//        sql.append("and (fab.balance_date >= acc.interest_start_date - 1) ");
//        sql.append("and not (exists (select 1 from fc_interest_dtl fid where fid.account = fab.account and fid.interest_time = fab.interest_time)) ");
//        pageParams.put("netCode", netCode);
//
//        StringBuilder sqltotal = new StringBuilder();
//        sqltotal.append("select null as balancedate,account as accor,null as accName,sum(cumu) as cumu,sum(adjustCumu) as adjustCumu,accType,'total' as account from (");
//        sqltotal.append(sql);
//        sqltotal.append(" ) a group by accType,account ");
//
//        StringBuilder sqlAlltotal = new StringBuilder();
//        sqlAlltotal.append("select null as balancedate,null as accout,null as accName,sum(cumu),sum(adjustCumu),null as accType,'Alltotal' as account from ( ");
//        sqlAlltotal.append(sql);
//        sqlAlltotal.append(") ");
//
//        StringBuilder selectSql = new StringBuilder();
//        selectSql.append(sqltotal.toString()).append(" union all ").append(sql.toString()).append(" union all ").append(sqlAlltotal.toString());
//        selectSql.append(" order by accType, accor,account, balancedate ");
//        String countSQL = prepareCountSQL(selectSql.toString());
//        BigDecimal count = (BigDecimal) createSQLQuery(countSQL, pageParams).uniqueResult();
//        List<AccuBillsVO> accubills = listBySQLAliasToBean(selectSql.toString(), input.getStart(), input.getLength(), pageParams, AccuBillsVO.class);
//        return new DataTablesOutput(input.getDraw(), accubills, count.intValue(), count.intValue());
//    }

    /**
     * <b>功能介绍：</b>查询积数清单数据(企业定期，银行定期，通知存款)<br>
     * <b>日期：</b>2017年7月12日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryCanEditNoAccuBalance(String netCode, String accType, String opType,
                                                               String sessionId, String generateMode) {
        Map<String, Object> pageParams = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dtl.ACCOUNT,dtl.interest_start_date as STARTDATE,dtl.bln_inst_date as ENDDATE,");
        sql.append(" dtl.CUMULATION,dtl.DAYS,dtl.ACC_NAME ACCNAME,dtl.ACC_TYPE as ACCTYPE ");
        sql.append("from fc_interest_dtl_temp dtl where dtl.acc_Type in ('05', '06', '08', '15') and dtl.net_id = :netCode");
//        sql.append("from (select account,max(t.bln_inst_date) endDate,min(t.interest_start_date) startDate,sum(t.days) countDays from fc_interest_dtl_temp t ");
//        sql.append("where exists (select 1 from FC_ACC fc where fc.blnctr_acc = t.account and fc.net_id = :netCode and fc.acc_Type in ('05', '06', '08') ");
//        if (StringUtils.isNotBlank(accType)) {
//            sql.append("and fc.acc_type in (:accType0) ");
//            pageParams.put("accType0", accType.split(","));
//        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and dtl.generate_mode = :generateMode ");
            pageParams.put("generateMode", generateMode);
        }
        if ("initList".equals(opType)) {
            sql.append("and ((dtl.bus_Status = :tempStatus and dtl.user_Session_Id = :userSessionId) or dtl.bus_Status = :backStatus) ");
        } else {
            sql.append("and dtl.bus_status = :busStatus ");
        }
//        sql.append(") group by t.account) a,(select aa.* from fc_interest_dtl_temp aa where exists ");
//        sql.append("(select 1 from FC_ACC fcc where fcc.blnctr_acc = aa.account and fcc.net_id = :netCode and fcc.acc_Type in ('05', '06', '08') ");
//        if (StringUtils.isNotBlank(accType)) {
//            sql.append("and fcc.acc_type in (:accType1) ");
//            pageParams.put("accType1", accType.split(","));
//        }
//        if (EmptyUtils.isNotEmpty(generateMode)) {
//            sql.append(" and aa.generate_mode = :generateMode ");
//            pageParams.put("generateMode", generateMode);
//        }
//        if ("initList".equals(opType)) {
//            sql.append("and ((aa.bus_Status = :tempStatus and aa.user_Session_Id = :userSessionId) or aa.bus_Status = :backStatus) ");
//        } else {
//            sql.append("and aa.bus_status = :busStatus ");
//        }
//        sql.append(")) b,FC_ACC acc where a.account = b.account and acc.BLNCTR_ACC = a.account and acc.acc_Type in ('05', '06', '08', '15') and acc.net_id = :netCode ");

//        if (StringUtils.isNotBlank(accType)) {
//            sql.append("and dtl.acc_type in (:accType2) ");
//            pageParams.put("accType2", accType.split(","));
//        }
        sql.append(" order by dtl.acc_Type, dtl.account ");
        if ("initList".equals(opType)) {
            pageParams.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            pageParams.put("backStatus", InterestConstant.INTEREST_BACK);
            pageParams.put("userSessionId", sessionId);
        } else {
            pageParams.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        }
        pageParams.put("netCode", netCode);
        List<Map<String, Object>> list = listBySQL(sql.toString(), pageParams);
        return list;
    }

    /**
     * <b>功能介绍：</b>查询不足款清单<br>
     * <b>日期：</b>2017年7月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<FcInterestVO> createCanEditLessPay(DataTablesInput pgd, String netCode, String opType,
                                                          String sessionId) {
        Map<String, Object> pageParams = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ( ");
        sql.append("select tt.account,tt.interest,led.ACC_NAME accName,fc.ACC_FOR_DEDUCT accForDeduct,led.check_balance balance ");
        sql.append("from fc_acc fc, fc_online_ledger led,(select a.account, sum(a.interest) interest from fc_interest_dtl_temp a ");
        sql.append("where  a.acc_type in :accTypeCode ");
        sql.append("and a.user_session_id = :sessionId and a.bus_status = :busStatus and a.repay_money is null and a.interest_sign = '1' ");
        if(EmptyUtils.isNotEmpty(netCode)){
            sql.append("and a.NET_ID = :netCode ");
            pageParams.put("netCode", netCode);
        }
        sql.append(" group by a.account) tt ");
        sql.append(" where fc.acc_for_deduct = led.account AND fc.BLNCTR_ACC = tt.account and led.check_balance < tt.interest ");
        if ("initList".equals(opType)) {
            pageParams.put("stauts", InterestConstant.INTEREST_BACK);
            pageParams.put("busStatus", InterestConstant.INTEREST_STATUE_TEMP);
        } else {
            sql.append("and 1 <> 1 ");
            pageParams.put("stauts", InterestConstant.INTEREST_WAIT_CHECK);
        }

        sql.append("union select bill.account, bill.interest, bill.ACC_NAME accName,bill.ACC_FOR_DEDUCT accForDeduct,bill.balance ");
        sql.append("from fc_less_pay_bill bill where bill.status = :stauts) le order by account ");
        pageParams.put("sessionId", sessionId);
        List<String> accTypeList= new ArrayList<>();
        accTypeList.add(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
        accTypeList.add(InnerAccountConstant.FCACC_LOAN);
        pageParams.put("accTypeCode",accTypeList);
//        return listBySQL(sql.toString(), pageParams);
        return listBySQLAliasToBean(sql.toString(), pageParams, FcInterestVO.class);
    }

    /**
     * <b>功能介绍：</b>获取待冲预提的账户<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPreRushAcc(String netCode, String accTypeCode, Date endDate) {
        Map<String, Integer> fcAccMap = iFcAccService.getBlnctrAccAndBalanceWayByNetIdAndNotAccType(netCode, accTypeCode);
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pre.account ACCOUNT,pre.interest - (case  when rush.strike_interest is null then ");
        sql.append("0 else  rush.strike_interest end) RUSHMONEY from (select c.account, sum(c.interest) interest ");
        sql.append("from fc_predraw_dtl c group by c.account) pre  ");
        sql.append("left join (select b.account, sum(a.strike_interest) strike_interest from fc_predraw_rush_dtl a, fc_predraw_dtl b ");
        sql.append("where a.predraw_dtl_id = b.id group by b.account) rush on pre.account = rush.account ");
        sql.append("where pre.account in(:fcAccMapKey)  and pre.interest - (case ");
        sql.append("when rush.strike_interest is null then 0 else rush.strike_interest end) <> 0 ");
        if (endDate != null) {
            sql.append("and not exists (select 1 from fc_interest_dtl f where f.account = pre.account ");
            sql.append("and f.interest_sign='0' and f.bln_inst_date > :endDate ) ");
            params.put("endDate", endDate);
        }
        sql.append(" order by pre.account");
        params.put("accType", accTypeCode);
        params.put("netId", netCode);
        params.put("fcAccMapKey", fcAccMap.keySet());
        return listBySQL(sql.toString(), params);
    }

    /**
     * <b>功能介绍：</b>查询未冲的预提数据<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryCanRushPredrawDtls(DataTablesInput pgd, String netCode, String accTypeCode,
                                                             Date endInstDate, String account, String opType, String userCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select pre.account as ACCOUNT,pre.ACC_NAME AS ACC_NAME,pre.interest_time AS INTEREST_TIME, ");
        sql.append("pre.order_no AS ORDER_NO,pre.interest AS INTEREST,rush.strike_interest AS STRIKE_INTEREST, ");
        sql.append("rush.status AS STATUS from fc_predraw_dtl pre ");
        sql.append("inner join (select a.predraw_dtl_id, sum(a.strike_interest) strike_interest,a.status ");
        sql.append("from fc_predraw_rush_dtl a where a.status in (:status) ");
        if ("initList".equals(opType)) {
            sql.append(" and a.operator_code = :opCode ");
            params.put("status", new Integer[]{InterestConstant.INTEREST_BACK, InterestConstant.INTEREST_STATUE_TEMP});
        } else if ("checkList".equals(opType)) {
            sql.append(" and a.operator_code <> :opCode ");
            params.put("status", new Integer[]{InterestConstant.INTEREST_WAIT_CHECK});
        }
        params.put("opCode", userCode);
        sql.append("group by a.predraw_dtl_id, a.status) rush on pre.id = rush.predraw_dtl_id and rush.strike_interest <= pre.interest where 1 = 1 ");
        if (EmptyUtils.isNotEmpty(accTypeCode)) {
            sql.append(" and pre.acc_Type = :accType ");
            params.put("accType", accTypeCode);
            if (EmptyUtils.isNotEmpty(account)) {
                sql.append("and pre.account = :account ");
                params.put("account", account);
            }
        }
        if (EmptyUtils.isNotEmpty(netCode)) {
            sql.append("and pre.net_id = :netId ");
            params.put("netId", netCode);
        }
        if (endInstDate != null) {
            sql.append("and not exists (select 1 from fc_interest_dtl dtl where dtl.predraw_dtl_id = pre.id and bln_Inst_Date > TO_DATE(:endInstDate,'yyyy-MM-dd'))  ");
            params.put("endInstDate", DateUtils.formatDate(endInstDate));
        }
        sql.append("order by pre.interest_time");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
        sqlQuery.addScalar("ACCOUNT", StandardBasicTypes.STRING);
        sqlQuery.addScalar("ACC_NAME", StandardBasicTypes.STRING);
        sqlQuery.addScalar("INTEREST_TIME", StandardBasicTypes.DATE);
        sqlQuery.addScalar("ORDER_NO", StandardBasicTypes.STRING);
        sqlQuery.addScalar("INTEREST", StandardBasicTypes.BIG_DECIMAL);
        sqlQuery.addScalar("STRIKE_INTEREST", StandardBasicTypes.BIG_DECIMAL);
        sqlQuery.addScalar("STATUS", StandardBasicTypes.INTEGER);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        sqlQuery.setFirstResult(pgd.getStart());
        sqlQuery.setMaxResults(sqlQuery.list().size());

        return sqlQuery.list();
    }

    /**
     * <b>功能介绍：</b>获取放款账户对应的应收利息账户<br>
     * <b>日期：</b>2017年8月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param
     * @param accType
     * @param subjectId
     * @return
     * @version 1.0
     */
    public FcAccDTO findNeedInterestAcc(String corpId, String accType, String subjectId) {
        List<FcAccDTO> acc = iFcAccService.findNeedInterestAcc(corpId, accType, subjectId);
        if (EmptyUtils.isEmptyList(acc)) {
            return null;
        } else {
            return acc.get(0);
        }
    }
//    /**
//     * <b>功能介绍：</b>根据预提主键ID查找明细<br>
//     * <b>日期：</b>2017年7月10日<br>
//     * <b>作者：</b>caozx<br>
//     *
//     * @version 1.0
//     */
//    @SuppressWarnings("unchecked")
//    public List<FcInterestDtlTemp> findByPredrawDtlId(String predrawDtlId) {
//        Query query = null;
//        StringBuilder sb = new StringBuilder("from FcInterestDtlTemp fd where fd.predrawDtlId=?0");
//        query = createQuery(sb.toString(), predrawDtlId);
//        return query.list();
//    }

    /**
     * <b>功能介绍：</b>根据流水号查询利息明细<br>
     * <b>日期：</b>2017年8月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param orderNo
     * @return
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<FcInterestDtl> getInterestsByOrderNo(String orderNo) {
        StringBuilder hql = new StringBuilder("from FcInterestDtl d where d.orderNo = ?0 ");
        return createQuery(hql.toString(), orderNo).list();
    }

    /**
     * <b>功能介绍：</b>删除已计息的冲预提信息<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param depositManagerId
     * @version 1.0
     */
    public void deleteRushPredrawByDepositMangerId(String depositManagerId) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from fc_predraw_rush_dtl where deposit_manager_id = ? ");
        createSQLQuery(sql.toString(), depositManagerId).executeUpdate();
    }

    /**
     * <b>功能介绍：</b>删除已复核的利息和预提信息<br>
     * <b>日期：</b>2017年9月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param interestDtlIds
     * @param predrawIds
     * @version 1.0
     */
    public void deleteCheckedInstsAndPres(List<String> interestDtlIds, List<String> predrawIds) {
        StringBuilder sql_pre = new StringBuilder("delete from FC_PREDRAW_DTL_TEMP where 1=1 ");
        StringBuilder sql_pre_inst = new StringBuilder("delete from FC_INTEREST_DTL_TEMP where 1=1 ");
        StringBuilder sql_inst = new StringBuilder("delete from FC_INTEREST_DTL_TEMP where 1=1 ");
        if (!EmptyUtils.isEmptyList(predrawIds)) {
            sql_pre.append(" and " + getSQLIn(predrawIds, 900, "ID"));
            sql_pre_inst.append(" and " + getSQLIn(predrawIds, 900, "PREDRAW_DTL_ID"));
            createSQLQuery(sql_pre.toString()).executeUpdate();
            createSQLQuery(sql_pre_inst.toString()).executeUpdate();
        }
        if (!EmptyUtils.isEmptyList(interestDtlIds)) {
            sql_inst.append(" and " + getSQLIn(interestDtlIds, 900, "ID"));
            createSQLQuery(sql_inst.toString()).executeUpdate();
        }
    }

    /**
     * <b>功能介绍：</b>是否存在在途利息<br>
     * <b>日期：</b>2017年9月28日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @return
     * @version 1.0
     */
    public boolean existUnOverInterest(String account) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append("from FcInterestDtlTemp dtl where dtl.account = :account and dtl.busStatus in (:status) ");
        params.put("account", account);
        params.put("status", new Integer[]{InterestConstant.INTEREST_WAIT_CHECK,
                InterestConstant.INTEREST_BACK});
        List<FcInterestDtlTemp> list = listByHQL(hql.toString(), params);
        if (EmptyUtils.isEmptyList(list)) {
            return false;
        }
        return true;
    }

    /**
     * <b>功能介绍：</b>查询待复核利息<br>
     * <b>日期：</b>2017年10月31日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netId
     * @param accTypeCode
     * @param endDate
     * @param userId
     * @return
     * @version 1.0
     */
    public List<FcInterestDtlTemp> queryFcInterestDtlTemps(String netId, String accTypeCode, Date endDate, String userId, String account, String generateMode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select dtl from FcInterestDtlTemp dtl ");
        sql.append("where dtl.netId = :netId ");
        sql.append("and dtl.busStatus = :busStatus ");
        sql.append("and dtl.blnInstDate < :endDate ");
        sql.append("and dtl.interestSign = :interestSign ");
        if (EmptyUtils.isNotEmpty(accTypeCode)) {
            sql.append("and dtl.accType in (:accTypeCode) ");
            params.put("accTypeCode", accTypeCode.split(","));
        }
        if (EmptyUtils.isNotEmpty(userId)) {
            sql.append("and dtl.userSessionId <> :userId ");
            params.put("userId", userId);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append("and dtl.account = :account ");
            params.put("account", account);
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and dtl.generateMode = :generateMode ");
            params.put("generateMode", generateMode);
        }
        params.put("netId", netId);
        params.put("endDate", endDate);
        params.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        params.put("interestSign", InterestConstant.INTEREST_SIGN_IST);
        return listByHQL(sql.toString(), params);
    }

    /**
     * <b>功能介绍：</b>查询待复核预提<br>
     * <b>日期：</b>2017年10月31日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netId
     * @param accTypeCode
     * @param endDate
     * @param userId
     * @return
     * @version 1.0
     */
    public List<FcPredrawDtlTemp> queryFcPredrawDtlTemps(String netId, String accTypeCode, Date endDate, String userId, String account) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("pre.id as id, pre.INTEREST_TIME as interestTime, pre.ORDER_NO as orderNo, pre.account as account, ");
        sql.append("pre.CUR_CODE as curCode, pre.interest as interest, pre.balance as balance, pre.STRIKE_SIGN as strikeSign, ");
        sql.append("pre.STRIKE_DATE as strikeDate, pre.STRIKE_INTEREST as strikeInterest, pre.sign as sign ");
        sql.append(" from  FC_PREDRAW_DTL_TEMP pre , FC_INTEREST_DTL_TEMP dtl ");
        sql.append("where pre.id = dtl.PREDRAW_DTL_ID ");
        sql.append("and dtl.NET_ID = :netId ");
        sql.append("and dtl.BUS_STATUS = :busStatus ");
        sql.append("and dtl.BLN_INST_DATE < :endDate ");
        sql.append("and dtl.INTEREST_SIGN = :interestSign ");
        if (EmptyUtils.isNotEmpty(accTypeCode)) {
            sql.append("and dtl.ACC_TYPE in (:accTypeCode) ");
            params.put("accTypeCode", accTypeCode.split(","));
        }
        if (EmptyUtils.isNotEmpty(userId)) {
            sql.append("and dtl.USER_SESSION_ID <> :userId ");
            params.put("userId", userId);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append("and dtl.account = :account ");
            params.put("account", account);
        }
        params.put("netId", netId);
        params.put("endDate", endDate);
        params.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        params.put("interestSign", InterestConstant.INTEREST_SIGN_PRE);
        return listBySQLAliasToBean(sql.toString(), params, FcPredrawDtlTemp.class);
    }

    /**
     * <b>功能介绍：</b>待复核的数据才打回<br>
     * <b>日期：</b>2017年10月31日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netCode
     * @param accType
     * @param endInstDate
     * @param userCode
     * @version 1.0
     */
    public void backInterestDtlTemp(String netCode, String accType, Date endInstDate, String userCode, String account, String generateMode) {
        List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(netCode, accType.split(","));
        List<String> accIds = fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("update fc_interest_dtl_temp t set t.bus_status = :busStatus ");
        sql.append("where 1 = 1 ");
        if (EmptyUtils.isNotEmpty(accIds)) {
            sql.append(" and t.account in(:accIds)");
            params.put("accIds", accIds);
        }
        sql.append(" and t.bln_inst_date < :endInstDate and t.bus_status = '" + InterestConstant.INTEREST_WAIT_CHECK + "' and t.user_session_id <> :userId ");
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append("and t.account = :account ");
            params.put("account", account);
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append("and t.generate_mode = :generateMode ");
            params.put("generateMode", generateMode);
        }
        params.put("busStatus", InterestConstant.INTEREST_BACK);
        params.put("netId", netCode);
        params.put("endInstDate", endInstDate);
        params.put("userId", userCode);

        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * <b>功能介绍：</b>处理利息电子回单号<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param orderNo
     * @param eReceiptNo
     * @version 1.0
     */
    public void processEReceiptNo(String account, String orderNo, String eReceiptNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "update fc_interest_dtl d set d.e_receipt_no = :eReceiptNo where d.account = :account and d.order_no = :orderNo ";
        params.put("account", account);
        params.put("orderNo", orderNo);
        params.put("eReceiptNo", eReceiptNo);
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * <b>功能介绍：</b>根据流水号删除利息<br>
     * <b>日期：</b>2017年12月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param orderNo
     * @version 1.0
     */
    public void deleteRepayLoanInterests(String orderNo) {
        String sql = "delete from fc_interest_dtl where order_no='" + orderNo + "'";
        createSQLQuery(sql).executeUpdate();
    }

    /**
     * <b>功能介绍：</b>查询利息清单导出数据<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netId
     * @param accType
     * @param opType
     * @param sessionId
     * @return
     * @version 1.0
     */
    public List<InterestBillExportVO> getExportXlsFcInterestData(String netId, String accType, String opType,
                                                                 String sessionId, String account, boolean zeroSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.corp_id,t.cur_id,t.cur_name,t.acc_type,");
        sql.append("t.acc_name,t.interest_sign,t.account,t.interest_start_date,t.bln_inst_date,");
        sql.append("t.days,t.cumulation,t.interest_rate,t.interest,t.interest_type,t.bus_status ");
        sql.append("from fc_interest_dtl_temp t where  t.net_id = :netId ");
//        sql.append(" and t.generate_mode = 'P' "); //个别计息 导出利息清单不能使用P
        if (!zeroSign) {
            sql.append(" and (t.interest <> 0 and t.cumulation <> 0) ");
        }
        if (EmptyUtils.isNotEmpty(accType)) {
            sql.append(" and t.acc_type in (:accType) ");
            params.put("accType", accType.split(","));
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and t.account = :account ");
            params.put("account", account);
        }
        if ("initList".equals(opType)) {
            sql.append(" and ((t.bus_status = :tempStatus or t.bus_status = :backStatus) and t.user_session_id = :userSessionId) ");
            params.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            params.put("backStatus", InterestConstant.INTEREST_BACK);
            params.put("userSessionId", sessionId);
        } else {
            sql.append(" and t.bus_status = :busStatus and t.user_session_id <> :userSessionId ");
            params.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
            params.put("userSessionId", sessionId);
        }
        sql.append(" order by t.acc_type,t.corp_id,t.cur_id,t.interest_sign,t.account,t.interest_start_date");
        params.put("netId", netId);
        return listBySQLAliasToBean(sql.toString(), params, InterestBillExportVO.class);
    }

    /**
     * <b>功能介绍：</b>查询利息清单导出数据合计<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netCode
     * @param accType
     * @param opType
     * @param sessionId
     * @return
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getExportXlsFcInterestTotal(String netCode, String accType, String opType, String sessionId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.corp_id as CORPID,t.cur_id as CURID,t.acc_type as ACCTYPE,");
        sql.append("t.interest_sign as ISTSIGN,BT_NVL_NUM(sum(t.interest),0) as TOTALIST ");
        sql.append("from fc_interest_dtl_temp t where  t.net_id = :netId ");
        params.put("netId", netCode);
        if (EmptyUtils.isNotEmpty(accType)) {
            sql.append("and t.acc_type in (:accType) ");
            params.put("accType", accType.split(","));
        }
        if ("initList".equals(opType)) {
            sql.append("and ((t.bus_status = :tempStatus or t.bus_status = :backStatus) and t.user_session_id = :userSessionId) ");
            params.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            params.put("backStatus", InterestConstant.INTEREST_BACK);
            params.put("userSessionId", sessionId);
        } else {
            sql.append("and t.bus_status = :busStatus and t.user_session_id <> :userSessionId ");
            params.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
            params.put("userSessionId", sessionId);
        }
        sql.append("group by t.acc_type,t.corp_id,t.cur_id,t.interest_sign ");
        return listBySQL(sql.toString(), params); //'tacc_type' in 'field list'
    }

    /**
     * <b>功能介绍：</b>查询利息通知单<br>
     * <b>日期：</b>2017年11月23日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param opType
     * @param netCode
     * @param accType
     * @param tallyDate
     * @param blnInstDate
     * @param userId
     * @return
     * @version 1.0
     */
    public List<InterestRateNoticeVO> getRateNoticeHead(String opType, String netCode, String accType, String tallyDate,
                                                        String blnInstDate, String userId, String account,
                                                        String generateMode, boolean zeroSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct d.corp_id,d.net_id,d.cur_id,d.account,d.acc_name from fc_interest_dtl_temp d ");
        sql.append(" where  d.net_id =:netId ");
        if (!zeroSign) {
            sql.append(" and (d.interest <> 0 and d.cumulation <> 0) ");
        }
        if (EmptyUtils.isNotEmpty(accType)) {
            sql.append(" and d.acc_type in (:accType) ");
            params.put("accType", accType);
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and d.GENERATE_MODE = :generateMode");
            params.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and d.account =:account ");
            params.put("account", account);
        }
        if ("initList".equals(opType)) {
            sql.append(" and ((d.bus_status = :tempStatus or d.bus_status = :backStatus) and d.user_session_id = :userSessionId) ");
            params.put("tempStatus", InterestConstant.INTEREST_STATUE_TEMP);
            params.put("backStatus", InterestConstant.INTEREST_BACK);
        } else {
            sql.append(" and (d.bus_status = :busStatus and d.user_session_id <> :userSessionId)");
            params.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        }
        sql.append(" and d.bln_inst_date <= :endDate ");
        params.put("netId", netCode);
        params.put("userSessionId", userId);
        params.put("endDate", DateUtils.str2Date(blnInstDate, DateUtils.date_sdf()));
        return listBySQLAliasToBean(sql.toString(), params, InterestRateNoticeVO.class);
    }

    /**
     * <b>功能介绍：</b>利息通知单利息明细<br>
     * <b>日期：</b>2017年11月23日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param curId
     * @param tallyDate
     * @return
     * @version 1.0
     */
    public List<FcInterestDtlTempVo> getInterestDtlTempByAccountTallyDate(String account, String curId,
                                                                          String tallyDate, String generateMode, boolean zeroSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.interest_start_date,t.bln_inst_date,t.interest_rate,t.interest,t.operator_code from fc_interest_dtl_temp t where 1=1 ");
        if (!zeroSign) {
            sql.append(" and (t.interest <> 0 and t.cumulation <> 0) ");
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and t.account = :account ");
            params.put("account", account);
        }
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and t.generate_mode = :generateMode");
            params.put("generateMode", generateMode);
        }
        if (EmptyUtils.isNotEmpty(tallyDate)) {
            sql.append(" and t.tally_date = :tallyDate ");
            params.put("tallyDate", DateUtils.str2Date(tallyDate, DateUtils.date_sdf()));
        }
        return listBySQLAliasToBean(sql.toString(), params, FcInterestDtlTempVo.class);
    }
//    /**
//     * <b>功能介绍：</b>查询用户名<br>
//     * <b>日期：</b>2017年11月23日<br>
//     * <b>作者：</b>caozx<br>
//     *
//     * @param userId
//     * @return
//     * @version 1.0
//     * 改为远程调用
//     */
//    public String getUserNameById(String userId) {
////        String sql = "select u.username from sys_user u where u.id = ? ";
//        String sql = " ";
//        return (String) createSQLQuery(sql, userId).uniqueResult();
//    }

    /**
     * <b>功能介绍：</b>删除委托贷款不够扣息的利息明细<br>
     * <b>日期：</b>2017年12月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param notInterestDtlId
     * @param userId
     * @version 1.0
     */
    public void filterBalanceLack(List<String> notInterestDtlId, String userId) {
        flushSession();
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "delete from fc_interest_dtl_temp t where t.id in (:notDtlId) and t.user_session_id = :userId ";
        params.put("notDtlId", notInterestDtlId);
        params.put("userId", userId);
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * <b>功能介绍：</b>修改委托贷款余额不足的积数<br>
     * <b>日期：</b>2017年12月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param notInterestDtlId
     * @param endInstDate
     * @param userId
     * @version 1.0
     */
    public void updateBalanceLackAccu(List<String> notInterestDtlId, Date endInstDate, String userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("update fc_accu_balance accu set accu.interest_sign = 0,accu.pre_sign = 0,accu.interest_time = null ");
        sql.append("where exists ( ");
        sql.append("select 1 from fc_interest_dtl_temp t where t.account = accu.account and t.id in (:ids) ");
        sql.append("and t.user_session_id = :userId ");
        sql.append("and t.interest_sign = 1 ");
        sql.append("and accu.balance_date >= t.interest_start_date ");
        sql.append("and accu.balance_date <= t.bln_inst_date ");
        sql.append(") and accu.balance_date < :endInstDate ");
        params.put("ids", notInterestDtlId);
        params.put("userId", userId);
        params.put("endInstDate", endInstDate);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 查询待复核利息数据数量
     *
     * @return
     */
    public Long queryCountFcInterestDtlTempsAll(String netId, String accTypeCode, String userId, List<String> accounts, String generateMode) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        StringBuilder sql = new StringBuilder();
//        sql.append("select count(*) as num from FcInterestDtlTemp dtl,FcAcc acc ");
//        sql.append("where dtl.account = acc.blnctrAcc ");
//        sql.append("and acc.netId = :netId ");
//        sql.append("and dtl.busStatus = :busStatus ");
//        sql.append("and dtl.interestSign = :interestSign ");
//        if (EmptyUtils.isNotEmpty(accTypeCode)) {
//            sql.append("and acc.accType in (:accTypeCode) ");
//            params.put("accTypeCode", accTypeCode.split(","));
//        }
//        if (EmptyUtils.isNotEmpty(userId)) {
//            sql.append("and dtl.userSessionId <> :userId ");
//            params.put("userId", userId);
//        }
//        if (EmptyUtils.isNotEmpty(account)) {
//            sql.append("and dtl.account = :account ");
//            params.put("account", account);
//        }
//        if (EmptyUtils.isNotEmpty(generateMode)) {
//            sql.append(" and dtl.generateMode = :generateMode ");
//            params.put("generateMode", generateMode);
//        }
//        params.put("netId", netId);
//        params.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
//        params.put("interestSign", InterestConstant.INTEREST_SIGN_IST);
//        return Long.valueOf(getByHQL(sql.toString(), params).get("num").toString());

        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
//        sql.append("select dtl.account from FcInterestDtlTemp dtl  ");
        sql.append("select count(*) as num from FcInterestDtlTemp dtl  ");
//        sql.append("where dtl.account = acc.blnctrAcc ");
        sql.append("where   1 = 1 ");
//        sql.append("and acc.netId = :netId ");
        sql.append("and dtl.busStatus = :busStatus ");
        sql.append("and dtl.interestSign = :interestSign ");
//        if (EmptyUtils.isNotEmpty(accTypeCode)) {
//            sql.append("and acc.accType in (:accTypeCode) ");
//            params.put("accTypeCode", accTypeCode.split(","));
//        }
        if (EmptyUtils.isNotEmpty(userId)) {
            sql.append("and dtl.userSessionId <> :userId ");
            params.put("userId", userId);
        }
        if (EmptyUtils.isNotEmpty(accounts)) {
            sql.append("and dtl.account in (:account) ");
            params.put("account", accounts);
        }
        if (EmptyUtils.isNotEmpty(generateMode)) {
            sql.append(" and dtl.generateMode = :generateMode ");
            params.put("generateMode", generateMode);
        }
//        params.put("netId", netId);
        params.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        params.put("interestSign", InterestConstant.INTEREST_SIGN_IST);
        return Long.valueOf(getByHQL(sql.toString(), params).get("num").toString());
    }

    /**
     * 查询待复核预提数据数量
     *
     * @param netId
     * @param accTypeCode
     * @param userId
     * @param account
     * @return
     */
    public long queryCountFcPredrawDtlTempsAll(String netId, String accTypeCode, String userId, String account) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) as num from FcPredrawDtlTemp pre,FcInterestDtlTemp dtl ");
        sql.append("where pre.id = dtl.predrawDtlId ");
        sql.append("and dtl.netId = :netId ");
        sql.append("and dtl.busStatus = :busStatus ");
        sql.append("and dtl.interestSign = :interestSign ");
        if (EmptyUtils.isNotEmpty(accTypeCode)) {
            sql.append("and dtl.accType in (:accTypeCode) ");
            params.put("accTypeCode", accTypeCode.split(","));
        }
        if (EmptyUtils.isNotEmpty(userId)) {
            sql.append("and dtl.userSessionId <> :userId ");
            params.put("userId", userId);
        }
        if (EmptyUtils.isNotEmpty(userId)) {
            sql.append("and dtl.account = :account ");
            params.put("account", account);
        }
        params.put("netId", netId);
        params.put("busStatus", InterestConstant.INTEREST_WAIT_CHECK);
        params.put("interestSign", InterestConstant.INTEREST_SIGN_PRE);
        return Long.valueOf(getByHQL(sql.toString(), params).get("num").toString());
    }

    /**
     * <b>功能介绍：</b>清空缓存<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void flushSession() {
        Session session = this.getSession();
        session.flush();
        session.clear();
    }

    /**
     * 根据指定日期查询计息账户当天的总积数
     *
     * @param account     内部账号
     * @param endInstDate 指定日期
     * @return 指定日期当天总积数
     */
    public BigDecimal queryAccuBalanceByDate(String account, Date endInstDate, Date interestStartDate) {
        Map<String, Date> fcAccDTOS = iFcAccService.getInterestStartDate(ListUtil.toList(account));
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(fab.cumu)+   sum(fab.adjust_cumu) as CUMU ");
        sql.append(" from FC_ACCU_BALANCE fab ");
        sql.append("where fab.account in(:accIds) ");
        sql.append("and fab.balance_date = :endInstDate  ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("endInstDate", endInstDate);
        params.put("accIds", fcAccDTOS.keySet());
        if (StringUtils.isNotBlank(account)) {
            sql.append(" and fab.account = :account ");
            params.put("account", account);
        }
        if (!EmptyUtils.isEmpty(interestStartDate)) {
            sql.append(" and (fab.balance_date >= :interestStartDate ) ");
            params.put("interestStartDate", DateUtils.addDay(interestStartDate, -1));
        }
        sql.append(" order by fab.account, fab.balance_date ");
        Map<String, Object> map = getBySQL(sql.toString(), params);
        if (EmptyUtils.isEmpty(map)) {
            return BigDecimal.ZERO;
        }
        if (null == map.get("CUMU")) {
            return BigDecimal.ZERO;
        }
        String cumu = map.get("CUMU").toString();
        BigDecimal totalCumu = new BigDecimal(cumu);
        return totalCumu;
    }

    /**
     * 获取账户的上次预提、结息时间(无积数)
     */
    public List<Map<String, Object>> getInterestLastTimeForNone(String netCode, String accTypeCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select acc.account as ACCOUNT, acc.interest_start_date as INTERESTSTARTDATE, pre.predate as PREDATE, ");
        sql.append(" ist.intedate as INTEDATE ");
        sql.append(" from fc_interest_dtl_temp acc ");
        sql.append(" left join (");
        sql.append(" select fc.account account,case when aaa.predate is null then bbb.predate ");
        sql.append(" when bbb.predate is null then aaa.predate ");
        sql.append(" when aaa.predate > bbb.predate then aaa.predate else bbb.predate end predate");
        sql.append(" from fc_interest_dtl_temp fc ");
        sql.append(" left join (select aa.account as account,max(aa.bln_inst_date) as predate ");
        sql.append(" from fc_interest_dtl aa, fc_interest_dtl_temp cc  where aa.account = cc.account ");
        sql.append(" and not exists (select 1 from (select predraw_dtl_id,sum(strike_interest) as rushist ");
        sql.append(" from fc_predraw_rush_dtl where status >= 11 group by predraw_dtl_id ) bb");
        sql.append(" where bb.predraw_dtl_id = aa.predraw_dtl_id and bb.rushist >= aa.interest) ");
        sql.append(" and cc.net_id = :netCode and cc.acc_type = :accTypeCode ");
        sql.append(" and aa.generate_mode = 'P' group by aa.account ) aaa ");//aaa
        sql.append(" on fc.account = aaa.account ");
        sql.append(" left join (select aa.account as account,max(aa.bln_inst_date) as predate ");
        sql.append(" from fc_interest_dtl_temp aa where ");
        sql.append(" aa.net_id = :netCode and aa.acc_type = :accTypeCode  and aa.bus_status in (:busStatus) ");
        sql.append(" group by aa.account) bbb on fc.account = bbb.account");//bbb
        sql.append(" where fc.net_id = :netCode and fc.acc_type = :accTypeCode ");
        sql.append(" ) pre ");//pre
        sql.append(" on pre.account = acc.account ");
        sql.append(" left join (");
        sql.append(" select fc.account account, case when ddd.intedate is null then eee.intedate ");
        sql.append(" when eee.intedate is null then ddd.intedate ");
        sql.append(" when ddd.intedate > eee.intedate then ddd.intedate else eee.intedate end intedate ");
        sql.append("  from fc_interest_dtl_temp fc ");
        sql.append(" left join ( ");
        sql.append(" select ee.account,max(ee.bln_inst_date) as intedate ");
        sql.append(" from fc_interest_dtl ee, fc_interest_dtl_temp dd  ");
        sql.append(" where ee.account = dd.account and ee.interest_sign = '1' ");
        sql.append(" and dd.net_id = :netCode  and dd.acc_type = :accTypeCode  and ee.generate_mode = 'P' ");
        sql.append(" group by ee.account) ddd");//ddd
        sql.append(" on fc.account = ddd.account");
        sql.append(" left join (select ee.account,  max(ee.bln_inst_date) as intedate ");
        sql.append(" from fc_interest_dtl_temp ee ");
        sql.append(" where ee.interest_sign = '1' ");
        sql.append(" and ee.net_id = :netCode  and ee.acc_type = :accTypeCode and ee.bus_status in (:busStatus) ");
        sql.append(" group by ee.account) eee ");//eee
        sql.append(" on fc.account = eee.account ");
        sql.append(" where fc.net_id = :netCode ");
        sql.append(" and fc.acc_type = :accTypeCode) ist");//ist
        sql.append(" on acc.account = ist.account ");
        sql.append(" where acc.net_id = :netCode ");
        sql.append(" and acc.acc_type = :accTypeCode ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netCode", netCode);
        params.put("accTypeCode", accTypeCode);
        params.put("busStatus", new Integer[]{InterestConstant.INTEREST_BACK, InterestConstant.INTEREST_WAIT_CHECK});
        return listBySQL(sql.toString(), params);
    }

    /**
     * 查询定期账户数据
     */
    public List<FcAccuBalancef> queryAccuBalanceForNone(String netCode, String[] accTypeCode, Date endInstDate) {
        List<Map<String, Object>> fcAccDTOS = iFcAccService.queryAccuBalanceForNone(null, netCode, accTypeCode, endInstDate);
        List<String> accountList = new ArrayList<>();
        for (Map map : fcAccDTOS) {
            String acc = (String) map.get("account");
            acc = Optional.ofNullable(acc).orElse((String) map.get("ACCOUNT")); //兼容Oracle
            accountList.add(acc);
        }
        Map<String, String> fcAccuBalancefsMap = new HashMap<>();
        if (ListUtils.isNotNullOrEmpty(accountList)) {
            StringBuilder sql = new StringBuilder();
            sql.append(" select l.account as account,l.check_balance as cumu from fc_online_ledger l where ");
            sql.append(" l.account in(:accountList) ");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("accountList", accountList);
            List<Map<String, Object>> fcAccuBalancefs = listBySQL(sql.toString(), params);
            for (Map map : fcAccuBalancefs) {
                String acc = (String) map.get("account");
                acc = Optional.ofNullable(acc).orElse((String) map.get("ACCOUNT"));
                String cumu = String.valueOf( Optional.ofNullable(map.get("cumu")).orElse(map.get("CUMU")) );
//                String cumu = String.valueOf(map.get("cumu"));
                fcAccuBalancefsMap.put(acc, cumu);
            }
        }
        List<FcAccuBalancef> list = new ArrayList<>();
        for (Map map : fcAccDTOS) {
            FcAccuBalancef balancef = new FcAccuBalancef();
            String acc = (String) map.get("account");
            acc = Optional.ofNullable(acc).orElse((String) map.get("ACCOUNT"));
            String curCode = (String) map.get("curCode");
            curCode = Optional.ofNullable(curCode).orElse((String) map.get("CURCODE"));
            String interestSign = (String) map.get("interestSign");
            interestSign = Optional.ofNullable(interestSign).orElse((String) map.get("INTERESTSIGN"));
            String preSign = (String) map.get("preSign");
            preSign = Optional.ofNullable(preSign).orElse((String) map.get("PRESIGN"));
            Date interestTime = (Date) map.get("interestTime");
            interestTime = Optional.ofNullable(interestTime).orElse((Date) map.get("INTERESTTIME"));

            balancef.setAccount(acc);
            balancef.setCurCode(curCode);
            balancef.setInterestSign(interestSign);
            balancef.setPreSign(preSign);
            balancef.setInterestTime(interestTime);
            list.add(balancef);
        }
        if (ListUtils.isNotNullOrEmpty(list) && MapUtils.isNotEmpty(fcAccuBalancefsMap)) {
            list.stream().forEach(item -> {
                if (fcAccuBalancefsMap.containsKey(item.getAccount())) {
                    item.setCumu(new BigDecimal(fcAccuBalancefsMap.get(item.getAccount())));
                }
            });
        }
        return list;
    }

    /**
     * 查询定期账户数据
     */
    public List<FcAccuBalancef> queryAccuoutBalanceForNone(String account, Date endInstDate) {
        List<FcAccuBalancef> fcAccuBalancefs = new ArrayList<>();
        List<Map<String, Object>> fcAccDTOS = iFcAccService.queryAccuBalanceForNone(account, null, null, endInstDate);
        if (CollectionUtils.isEmpty(fcAccDTOS)) {
            return fcAccuBalancefs;
        }
        List<String> accountList = new ArrayList<>();
        for (Map map : fcAccDTOS) {
            String acc = (String) map.get("account");
            acc = Optional.ofNullable(acc).orElse((String) map.get("ACCOUNT"));
            accountList.add(acc);
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select l.account as account,l.check_balance as cumu from fc_online_ledger l where ");
        sql.append(" l.account in (:accountList)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accountList", accountList);
        /*List<Map<String, Object>> fcAccuBalancefs = listBySQL(sql.toString(), params);
        Map<String, String> fcAccuBalancefsMap = new HashMap<>();
        for (Map map : fcAccuBalancefs) {
            String acc = (String) map.get("account");
            String cumu = (String) map.get("cumu");
            fcAccuBalancefsMap.put(acc, cumu);
        }*/

        fcAccuBalancefs = listBySQLAliasToBean(sql.toString(), params, FcAccuBalancef.class);
//        List<FcAccuBalancef> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(fcAccuBalancefs)) {
            for (Map map : fcAccDTOS) {
                FcAccuBalancef balancef = new FcAccuBalancef();
                String acc = (String) map.get("account");
                acc = Optional.ofNullable(acc).orElse((String) map.get("ACCOUNT"));
                String curCode = (String) map.get("curCode");
                curCode = Optional.ofNullable(curCode).orElse((String) map.get("CURCODE"));
                String interestSign = (String) map.get("interestSign");
                interestSign = Optional.ofNullable(interestSign).orElse((String) map.get("INTERESTSIGN"));
                String preSign = (String) map.get("preSign");
                preSign = Optional.ofNullable(preSign).orElse((String) map.get("PRESIGN"));
                Date interestTime = (Date) map.get("interestTime");
                interestTime = Optional.ofNullable(interestTime).orElse((Date) map.get("INTERESTTIME"));

                balancef.setAccount(acc);
                balancef.setCurCode(curCode);
                balancef.setInterestSign(interestSign);
                balancef.setPreSign(preSign);
                balancef.setInterestTime(interestTime);
                fcAccuBalancefs.add(balancef);
            }
        }

        return fcAccuBalancefs;
    }

    /**
     * 获取预约登记通知信息
     * //FcDrawNotice实体
     */
    public List<String> getNotifyTellerInfo(List<String> accounts, boolean isDraw) {
        Map<String, Object> pageParams = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        /*sql.append("select f.* from cho_inner_finance f inner join fc_acc a on f.payer_acc = a.blnctr_acc ");
        sql.append(" inner join fc_online_ledger l on  a.blnctr_acc = l.account");
        sql.append(" where f.status in (:status) and f.bus_type = :billType ");
        if (!isDraw) {
            sql.append(" and a.acc_Type=:accType  ");
            pageParams.put("accType", InnerAccountConstant.FCACC_BANK_DEPOSIT);
        }
        sql.append(" order by a.blnctr_acc desc"); //f.notify_time 通知日期
        pageParams.put("status", status);
        pageParams.put("billType", billType);*/
        sql.append("select l.account from FcOnlineLedger l ");
        sql.append(" where 1=1 ");
        // fc_online_ledger(联机分户账表) 没 acc_type 这个字段
//        if (!isDraw) {
//            sql.append(" and l.acc_type=:accType  ");
//            pageParams.put("accType", InnerAccountConstant.FCACC_BANK_DEPOSIT);
//        }
        sql.append(" and ").append(getSQLIn(accounts, 900, "l.account"));
        return listByHQL(sql.toString(), pageParams);
    }

    /**
     * 获取预约登记通知信息
     * //FcDrawNotice实体
     */
    public List<ChoInnerFinanceBean> getSingleNotifyTellerInfo(Integer[] status, String account) {
        Map<String, Object> pageParams = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select f.notice_account as payerAcc,f.book_money as payMoney,f.book_date as payDate ");
        sql.append(" from FC_INTEREST_SINGLE_NOD_BOOK f  ");
        sql.append(" where f.status in (:status) ");
        if (EmptyUtils.isNotEmpty(account)) {
            pageParams.put("account", account);
            sql.append(" and f.notice_account = :account  ");
        }
        sql.append(" and f.NOTICE_ACC_TYPE = :accType  ");
        pageParams.put("accType", InnerAccountConstant.FCACC_BANK_DEPOSIT);
        sql.append(" order by f.notice_account desc"); //f.notify_time 通知日期
        pageParams.put("status", status);
        return listBySQLAliasToBean(sql.toString(), pageParams, ChoInnerFinanceBean.class);
    }

    /**
     * 单位禁用校验使用-利息
     *
     * @return
     * @Param corpId
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(a.id)  from fc_interest_dtl_temp a ");
        sql.append("  where a.bus_status in ( 0,11,-1) ");
        sql.append("  and a.corp_id = :corpId ");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), param).uniqueResult().toString());
    }

    /**
     * <br>
     * <b>功能：</b> 查询账户剩余未冲预提 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/14 0014 <br>
     */
    public List<FcPredrawDtlVo> queryAccFcPredrawDtlVo(String account) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select pre.id,pre.account,pre.cur_code as curCode,ist.interest_time as interestTime, ");
        sql.append(" ist.interest_start_date as interestStartDate,ist.bln_inst_date as blnInstDate, ");
        sql.append(" pre.interest,bt_nvl_num(rush.strike_interest, 0) as strikeInterest ");
        sql.append(" from fc_interest_dtl ist ");
        sql.append(" inner join fc_predraw_dtl pre on pre.id = ist.predraw_dtl_id ");
        sql.append(" left join ");
        sql.append(" (select prush.predraw_dtl_id, bt_nvl_num(sum(prush.strike_interest), 0) as strike_interest from fc_predraw_rush_dtl prush ");
        sql.append(" where status = 95 ");
        sql.append(" group by prush.predraw_dtl_id) rush ");
        sql.append(" on pre.id = rush.predraw_dtl_id ");
        sql.append(" where ist.account = :account ");
        params.put("account", account);
        return listBySQLAliasToBean(sql.toString(), params, FcPredrawDtlVo.class);
    }

    /**
     * <br>
     * <b>功能：</b> 更新利息计息标识ID <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/20 0020 <br>
     */
    public void updateInterestSingle(String istId, String singleId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE fc_interest_dtl_temp set single_id = :singleId ");
        sql.append(" where id = :istId ");
        Map<String, Object> params = new HashMap<>();
        params.put("istId", istId);
        params.put("singleId", singleId);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * <br>
     * <b>功能：</b> 删除利息计息数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/20 0020 <br>
     */
    public void deleteInterestSingleDtl(String singleId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from fc_interest_dtl_temp where single_id = :singleId ");
        Map<String, Object> params = new HashMap<>();
        params.put("singleId", singleId);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    public List<FcInterestDtlVo> getInterestDtlByAccountAndDate(String blnctrAcc, Date startTime, Date endTime) {
        Map<String, Object> param = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct dtl.id, dtl.interest_time ");
        sql.append("from FC_INTEREST_DTL dtl ");
        sql.append(" inner join FC_ACC acc on acc.blnctr_acc=dtl.account ");
        sql.append("where acc.blnctr_acc = :blnctrAcc");
        if (startTime.compareTo(endTime)>0){
            sql.append(" and dtl.interest_time >= :startTime and dtl.interest_time <= :endTime");
        }else{
            sql.append(" and dtl.interest_time >= :endTime and dtl.interest_time <= :startTime");
        }
        param.put("startTime",startTime);
        param.put("endTime",endTime);
        param.put("blnctrAcc",blnctrAcc);
        return listBySQLAliasToBean(sql.toString(),param,FcInterestDtlVo.class);
    }
}
