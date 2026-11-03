package com.bytter.biz.finacial.closedeal.dao;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.vo.FcCarryVO;
import com.bytter.biz.finacial.closedeal.entity.FcCarry;
import com.bytter.biz.finacial.closedeal.vo.FcAccVoucherVO;
import com.bytter.biz.finacial.closedeal.vo.FcCarryVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
 * @Description: 结转记录表
 * @date 2017-06-27 09:55:51
 */
@Repository
public class FcCarryDao extends BaseDao<FcCarry, String> {
    /**
     * *查询日结日志
     *
     * @param dataTablesInput
     */
    public DataTablesOutput getFcCarryLog(DataTablesInput dataTablesInput, List<String> corpAuth) {
        /*sql.append(" select a.net_id as netId , (case  when a.operator_Code=s.id then s.login_name end) as operatorCode ,a.carry_Time as carryTime, ");
        sql.append(" a.voucher_Start_Date as voucherStartDate, a.voucher_End_Date as voucherEndDate , a.restore_Carry_Time as restoreCarryTime , (case when a.restore_Carry_Optor=s.id then s.login_name end) as restoreCarryOptor	");
        sql.append(" from fc_carry a  left join sys_user s on s.id=a.operator_Code left join sys_corp sc on a.net_Id = sc.id where 1=1  ");
        sql.append(" and carry_type= 'D' ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "sc.id"));*/
        String sql = " select a.net_id as netId , a.operator_Code as operatorCode ,a.carry_Time as carryTime, " +
                " a.voucher_Start_Date as voucherStartDate, a.voucher_End_Date as voucherEndDate , a.restore_Carry_Time as restoreCarryTime , a.restore_Carry_Optor as restoreCarryOptor " +
                " from fc_carry a where 1=1  " +
                " and a.carry_type = 'D' " +
                " and " + getSQLIn(corpAuth, 900, "a.net_Id");
        return getDynamicBySQL(dataTablesInput, sql, FcCarryVo.class, null, false);
    }
    /**
     * 查询出某一天日结/月结的网点列表，供日结月结状态查询服务使用
     */
    public List<FcCarryVo> findCarryForStatus(Map<String, Object> params, List<String> corpAuth) {
        /*StringBuffer sql = new StringBuffer("select sc.name as netName , fc.net_id as netId , fc.carry_time as carryTime , ");
        sql.append(" su.login_name as operatorCode ,fc.restore_carry_time as RestoreCarryTime , ");*/
        StringBuffer sql = new StringBuffer("select fc.net_id as netId , fc.carry_time as carryTime , ");
        sql.append(" fc.operator_code as operatorCode ,fc.restore_carry_time as RestoreCarryTime , ");
        sql.append(" fc.voucher_Start_Date as voucherStartDate ,  fc.voucher_End_Date as voucherEndDate ,");
        sql.append(" fc.carry_Type as carryType ");
        /*sql.append(" from fc_carry fc left join sys_corp sc on fc.net_Id = sc.id ");
        sql.append(" left join sys_user su on fc.operator_code=su.id where 1=1 ");*/
        sql.append(" from fc_carry fc ");
        sql.append("  where 1=1 ");
        if (params != null) {
            if (params.containsKey("carryType")) {
                sql.append(" and fc.carry_Type = :carryType");
            }
            if (params.containsKey("netId")) {
                sql.append(" and fc.net_Id = :netId");
            }
            if (params.containsKey("opDate")) {
                sql.append(" and fc.carry_Time = :opDate");
                if (params.get("opDate") instanceof java.util.Date) {
                    params.put("op_Date", new java.sql.Date(((java.util.Date) params.get("opDate")).getTime()));
                }
            }
            if (params.containsKey("startDate")) {
                sql.append(" and fc.carry_Time >= :startDate");
                if (params.get("startDate") instanceof java.util.Date) {
                    params.put("startDate", new java.sql.Date(((java.util.Date) params.get("startDate")).getTime()));
                }
            }
            if (params.containsKey("endDate")) {
                sql.append(" and fc.carry_Time <= :endDate");
                if (params.get("endDate") instanceof java.util.Date) {
                    params.put("endDate", new java.sql.Date(((java.util.Date) params.get("endDate")).getTime()));
                }
            }
            if (params.containsKey("voucherStartDate")) {
                sql.append(" and fc.voucher_Start_Date <= :startDate");
                if (params.get("voucherStartDate") instanceof java.util.Date) {
                    params.put("startDate", new java.sql.Date(((java.util.Date) params.get("voucherStartDate")).getTime()));
                }
            }
            if (params.containsKey("voucherEndDate")) {
                sql.append(" and fc.voucher_End_Date >= :endDate");
                if (params.get("voucherEndDate") instanceof java.util.Date) {
                    params.put("endDate", new java.sql.Date(((java.util.Date) params.get("voucherEndDate")).getTime()));
                }
            }
        }
//        sql.append(" and ").append(getSQLIn(corpAuth, 900, "sc.id"));
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "fc.net_Id"));
        sql.append(" and fc.restore_Carry_Time is null order by fc.carry_Time ");
        return listBySQLAliasToBean(sql.toString(), params, FcCarryVo.class);
    }
    /**
     * 功能介绍 : 损益类科目账号发生额查询。<br>
     * <b>日期：</b>2018年1月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput<FcAccVoucherVO> findProfitYear(Map<String, Object> param, DataTablesInput input, List<String> accList) {
        return getDataTablesOutputBySQLAliasToBean(input, getProfitYearSql(param, accList).toString(), param, FcAccVoucherVO.class);
    }
    /**
     * 功能介绍 : 查询最近一次未反结转的结转数据。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public FcCarry queryNoReverseFcCarries(String netCode, String carryType) {
        String hql = "from FcCarry WHERE 1=1 " + " and carryType = :carryType" +
                " and netId = :netCode and restoreCarryOptor is null" +
                " order by voucherEndDate desc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netCode", netCode);
        params.put("carryType", carryType);
        return (FcCarry) createQuery(hql, params).setMaxResults(1).uniqueResult();
    }
    /**
     * 功能介绍 : 查询所有损益类科目账号发生额明细。<br>
     * <b>日期：</b>2018年1月19日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccVoucherVO> queryProfitYear(Map<String, Object> param, List<String> accList) {
        return listBySQLAliasToBean(getProfitYearSql(param, accList).toString(), param, FcAccVoucherVO.class);
    }
    /**
     * 功能介绍 : 获取损益类科目账户余额查询sql。<br>
     * <b>日期：</b>2018年1月19日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public StringBuffer getProfitYearSql(Map<String, Object> param, List<String> accList) {
        Date startDate = (Date) param.get("startDate");
        //查询损益类科目在记账区间内的发生额合计
        StringBuffer bookSql = new StringBuffer();
        bookSql.append(" select b.account,b.debit_money,b.credit_money,b.ACC_NAME,b.subject_id,b.subject_name,b.cur_code,b.cur_name,a.tally_date,b.balance_way ")
                .append(" from fc_accbook a, fc_accbook_dtl b, fc_bus_race c where a.id = b.book_id and a.bus_race_id = c.id ")
                .append(" and a.generate_mode <> 'P' and c.Sys_Race_Code not in ('3005', '3006') ")
                .append(" and a.voucher_state = 'N' AND a.tally_date <= :opDate ");
        if (null != startDate) {
            bookSql.append(" and a.tally_date >= :startDate ");
        }
        bookSql.append(" union all select bb.account,bb.debit_money,bb.credit_money,bb.ACC_NAME,bb.subject_id,bb.subject_name,bb.cur_code,bb.cur_name,aa.tally_date, bb.balance_way ")
                .append(" from fc_accbook_blned aa, fc_accbook_blned_dtl bb,fc_bus_race cc where aa.id = bb.book_id and aa.bus_race_id = cc.id ")
                .append(" and aa.generate_mode <> 'P' and cc.Sys_Race_Code not in ('3005', '3006') ")
                .append(" and aa.voucher_state = 'N' AND aa.tally_date <= :opDate ");
        if (null != startDate) {
            bookSql.append(" and aa.tally_date >= :startDate ");
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT bal.account AS account,bal.acc_name AS accName,bal.subject_id AS subjectCode, bal.subject_name as subjectName, ")
                .append("sum( bal.debit_money - bal.credit_money ) AS balance,bal.cur_name AS curName,bal.cur_code AS curId, bal.balance_way as balanceWay")
                .append(" FROM (").append(bookSql).append(") bal ")
                .append(" WHERE " + SqlUtil.getInSql(accList, "bal.account", true))
                .append(" group by bal.account,bal.acc_name,bal.subject_id,bal.cur_code,bal.subject_name,bal.cur_name,bal.cur_code,bal.balance_way ")
                .append(" having (sum(bal.debit_money - bal.credit_money)  ) != 0 ")
                .append(" order by bal.account,bal.acc_name ");
        return sql;
    }
    /**
     * 查询待复核损益结转本年利润数据
     *
     * @param params
     * @return
     */
    public List<FcAccbook> getCheckProfitYear(Map<String, Object> params) {
        String voucherState = null;
        if (params != null) {
            voucherState = params.get("voucherState") != null ? params.get("voucherState").toString() : null;
        } else {
            return null;
        }
        StringBuilder hql = new StringBuilder();
        hql.append(" from FcAccbook accBook");
        hql.append(" where 1=1");
        hql.append(" and accBook.busOriType = :busOriType");
        hql.append(" and accBook.redblueSign = :redblueSign");
        hql.append(" and accBook.netId = :netId");
        if (FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.getValue().equals(params.get(voucherState))) {
            hql.append(" and accBook.operatorCode <> :operatorCode");
        }
        hql.append(" and accBook.voucherState in ( :voucherState )");
        return listByHQLAliasToBean(hql.toString(), params, FcAccbook.class);
    }
    /**
     * 查询账户信息
     *
     * @param params
     * @return
     */
    public List<FcCarry> getFcCarryByNetId(Map<String, Object> params) {
        return listByHQLAliasToBean("from FcCarry where netId = :netId and carryType = :carryType order by carryTime desc", params, FcCarry.class);
    }
    public List<FcCarry> getFcCarry(String netId, String carryType, Date date) {
        String sql = "SELECT fc.ID AS id, fc.CARRY_TYPE AS carryType, fc.NET_ID as netID, fc.OPERATOR_CODE AS operatorCode FROM FC_CARRY fc " + "WHERE fc.CARRY_TYPE=:carryType " +
                "AND fc.RESTORE_CARRY_TIME IS NULL " +
                "AND fc.NET_ID=:netId " +
                "AND (fc.VOUCHER_START_DATE = :date OR fc.VOUCHER_END_DATE = :date) " +
                "OR (fc.VOUCHER_START_DATE >= :date AND fc.VOUCHER_END_DATE <= :date) ";
        Map<String, Object> params = new HashMap<>();
        params.put("carryType", carryType);
        params.put("netId", netId);
        params.put("date", date);
        return listBySQLAliasToBean(sql, params, FcCarry.class);
    }
    public List<FcCarryVO> getFcCarryList(FcCarryVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT fc.ID AS id, fc.CARRY_TYPE AS carryType, fc.NET_ID AS netID, fc.OPERATOR_CODE AS operatorCode,fc.voucher_start_date AS voucherStartDate,fc.voucher_end_date AS voucherEndDate");
        sql.append(" FROM FC_CARRY fc  WHERE  fc.RESTORE_CARRY_TIME IS NULL ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(vo.getOperatorCode())) {
            sql.append(" and fc.OPERATOR_CODE=:operatorCode");
            params.put("operatorCode", vo.getOperatorCode());
        }
        if (StringUtils.isNotEmpty(vo.getCarryType())) {
            sql.append(" and fc.CARRY_TYPE=:carryType");
            params.put("carryType", vo.getCarryType());
        }
        if (StringUtils.isNotEmpty(vo.getNetId())) {
            sql.append(" and fc.NET_ID=:netId ");
            params.put("netId", vo.getNetId());
        }
        if (StringUtils.isNotEmpty(vo.getVoucherStartDate())) {
            sql.append(" and fc.VOUCHER_START_DATE<=:voucherStartDate ");
            params.put("voucherStartDate", vo.getVoucherStartDate());
        }
        if (StringUtils.isNotEmpty(vo.getVoucherEndDate())) {
            sql.append(" and fc.VOUCHER_END_DATE>=:voucherEndDate ");
            params.put("voucherEndDate", vo.getVoucherEndDate());
        }
        return listBySQLAliasToBean(sql.toString(), params, FcCarryVO.class);
    }
}
