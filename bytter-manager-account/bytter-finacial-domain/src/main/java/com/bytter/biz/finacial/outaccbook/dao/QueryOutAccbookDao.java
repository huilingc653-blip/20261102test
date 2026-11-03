package com.bytter.biz.finacial.outaccbook.dao;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbook;
import com.bytter.biz.finacial.outaccbook.vo.FcOutAccbookVo;
import com.bytter.biz.finacial.paramconfig.entity.FcSubject;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.FcAccDTO;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 表外流水账主档表
 * @date 2017-07-05 15:40:08
 */
@Repository
public class QueryOutAccbookDao extends BaseDao<FcOutAccbook, String> {
    /**
     * 功能介绍 :流水账查询<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public DataTablesOutput listAll(DataTablesInput dataTablesInput, List<String> accounts) {
        Map<String, Object> map1 = dataTablesInput.getFormdata();
        String direction = (String) map1.get("eqs_direction");// 方向
        String smoney = (String) map1.get("eqg_smoney");
        String emoney = (String) map1.get("eqg_emoeny");
        String moneyCode = "";
        if (!checkIsNull(direction)) {
            // 借方
            if (direction.equals("1")) {
                moneyCode += " and d.debit_money !=0 ";
                if (EmptyUtils.isNotEmpty(smoney)) {
                    moneyCode += " and d.debit_money >=" + smoney;
                }
                if (EmptyUtils.isNotEmpty(emoney)) {
                    moneyCode += " and d.debit_money <=" + emoney;
                }
            }
            // 贷方
            if (direction.equals("-1")) {
                moneyCode += " and d.credit_money != 0";
                if (EmptyUtils.isNotEmpty(smoney)) {
                    moneyCode += " and d.credit_money >=" + smoney;
                }
                if (EmptyUtils.isNotEmpty(emoney)) {
                    moneyCode += " and d.credit_money <=" + emoney;
                }
            }
        } else {
            // 借方
            String debitmoney = "";
            // 贷方
            String creditmoney = "";
            if (!checkIsNull(smoney)) {
                debitmoney += " d.debit_money >=" + smoney;
                creditmoney += " d.credit_money >= " + smoney;
            }
            if (!checkIsNull(emoney)) {
                if (EmptyUtils.isNotEmpty(debitmoney)) {
                    debitmoney += " and ";
                    creditmoney += " and ";
                }
                debitmoney += " d.debit_money <=" + emoney;
                creditmoney += " d.credit_money <=" + emoney;
            }
            if (!checkIsNull(debitmoney)) {
                moneyCode += " and" + debitmoney + "and d.debit_money != 0 ";
            }
            if (!checkIsNull(creditmoney)) {
                moneyCode += " or" + creditmoney + "and d.credit_money != 0 ";
            }
        }
		/*StringBuilder sql = new StringBuilder();
		sql.append("select o.id as id,o.tally_date as tallyDate,o.voucher_no as voucherNo,o.order_no as orderNo,d.account as account,");
		sql.append("a.acc_name as accName,d.debit_money as debitMoney,d.credit_money as creditMoney,s.subject_name as subjectName,");
		sql.append("s.measure_unit as measureUnit,d.summary_content as summaryContent,o.status as status,u.username as username,");
		sql.append("o.operator_code as operatorCode");
		sql.append(" from fc_out_accbook o inner join FC_OUT_ACCBOOK_DTL d on o.id = d.out_accbook_id");
		sql.append(" left join FC_ACC a on a.blnctr_acc = d.account");
		sql.append(" left join  FC_SUBJECT s on a.subject_id  = s.id");
		sql.append(" left join sys_user u on o.operator_code = u.id");
		sql.append(" where 1=1");
		sql.append(moneyCode);
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_Id"));*/
        // s.measure_unit as measureUnit
        String sql = "select o.id as id,o.tally_date as tallyDate,o.voucher_no as voucherNo,o.order_no as orderNo,d.account as account," +
                "d.debit_money as debitMoney,d.credit_money as creditMoney," +
                "d.summary_content as summaryContent,o.status as status,o.operator_code as operatorCode" +
                " from fc_out_accbook o inner join FC_OUT_ACCBOOK_DTL d on o.id = d.out_accbook_id" +
                " where 1=1" +
                moneyCode +
                " and " + getSQLIn(accounts, 900, "d.account");
        return getDynamicBySQL(dataTablesInput, sql, FcOutAccbookVo.class, "o", false);
    }
    /**
     * 功能介绍 :查看凭证<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public FcOutAccbookVo view(String id) {
        /*sql.append("select s.measure_unit as measureUnit,o.order_no as orderNo,d.account as account,o.voucher_no as voucherNo,");
		sql.append("o.bills_no as billsNo,o.end_date as endDate, d.summary_content as summaryContent,a.acc_name as accName,");
		sql.append("d.debit_money as debitMoney,d.credit_money as creditMoney,o.checker_code as checkerCode,c.id as curid, ");
		sql.append(" sc.userName as userName, su.userName as checkerName ");
		sql.append(" o.operator_code as userName, o.checker_code as checkerName ");
		sql.append(" from fc_out_accbook o");
		sql.append(" inner join FC_OUT_ACCBOOK_DTL d on o.id = d.out_accbook_id");
		sql.append(" left join FC_ACC a on a.blnctr_acc = d.account");
		sql.append(" left join  FC_SUBJECT s on a.subject_id  = s.id");
		sql.append(" left join bt_currency  c on c.id = a.cur_id");
		sql.append(" left join sys_user sc on sc.id = o.operator_code");
		sql.append(" left join sys_user su on su.id = o.checker_code");
		sql.append(" where o.id =:id");*/
        String sql = "select o.order_no as orderNo,d.account as account,o.voucher_no as voucherNo," +
                "o.bills_no as billsNo,o.end_date as endDate, d.summary_content as summaryContent," +
                "d.debit_money as debitMoney,d.credit_money as creditMoney,o.checker_code as checkerCode, " +
                " o.operator_code as userName, o.checker_code as checkerName " +
                " from fc_out_accbook o" +
                " inner join FC_OUT_ACCBOOK_DTL d on o.id = d.out_accbook_id" +
                " where o.id =:id";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return getBySQLAliasToBean(sql, map, FcOutAccbookVo.class);
    }
    /**
     * 功能介绍 :分户账查询<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public Map<String, Object> divided(Integer pageNum, FcOutAccbookVo vo, List<String> corpAuth, List<FcAccDTO> fcAccList) {
        Map<String, Object> result = new HashMap<String, Object>();// 保存结果
        // 查询所有账户
        StringBuilder account_sql = new StringBuilder();
        account_sql.append("select d.account as account,MIN(o.tally_date) as startDate, MAX(o.tally_date) as endDate,");
        account_sql.append(" d.corp_id as corpId,d.corp_name as corpName,d.cur_id as curId,d.cur_name as curName,d.acc_name as accName from fc_out_accbook_dtl d");
        account_sql.append(" left join fc_out_accbook o on o.id = d.out_accbook_id");
        account_sql.append(" where 1=1 ");
        String geDate = "";
        String leDate = "";
        if (vo.getGe_tallyDate() != null && vo.getLe_tallyDate() != null) {
            geDate = DateUtils.formatDate(vo.getGe_tallyDate(), "yyyy-MM-dd");
            leDate = DateUtils.formatDate(vo.getLe_tallyDate(), "yyyy-MM-dd");
            account_sql.append(" and o.tally_date>=BT_TO_DATE(:ge_tallyDate,'yyyy-mm-dd') and o.tally_date<=BT_TO_DATE(:le_tallyDate,'yyyy-mm-dd')");
        }
        if (!checkIsNull(vo.getAccount())) {
            account_sql.append(" and d.account like :account ");
        }
        account_sql.append(" and ").append(getSQLIn(corpAuth, 900, "d.corp_id"));
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("netId", vo.getNetId());
        param.put("corpId", vo.getCorpId());
        param.put("ge_tallyDate", geDate);
        param.put("le_tallyDate", leDate);
        param.put("account", vo.getAccount());
        param.put("subjectSort", vo.getSubjectSort());
        account_sql.append(" group by d.account,d.corp_id,d.cur_id,d.acc_name");
        List<FcOutAccbookVo> listAccount = listBySQLAliasToBean(account_sql.toString(), param, FcOutAccbookVo.class);
        if (listAccount.isEmpty()) {
            result.put("res", new ArrayList<FcOutAccbookVo>());
            result.put("dataCount", 0);
        } else {
            // 根据页数取第几个账号
            FcOutAccbookVo res = listAccount.get(pageNum - 1);
            // 保存总页数和账号，前台要用
            result.put("accObj", res);// 保存根据页码显示的账号
            result.put("accSize", listAccount.size());
            // 查询列表
            StringBuilder sql = new StringBuilder();
            sql.append("select o.tally_date as tallyDate,d.subject_name as subjectName,o.voucher_no as voucherNo,");
            sql.append("d.summary_content as summaryContent,o.bills_no as billsNo,o.contract_no as contractNo,o.end_date as endDate,");
            sql.append("d.credit_money as creditMoney, d.debit_money as debitMoney,d.balance_way as balanceWay,d.corp_id as corpId,");
            sql.append("d.cur_id as curId,d.acc_name as accName ");
            sql.append(" from Fc_out_Accbook o");
            sql.append(" left join fc_out_accbook_dtl d on o.id = d.out_accbook_id");
            sql.append(" where d.account =" + res.getAccount());
            if (vo.getGe_tallyDate() != null && vo.getLe_tallyDate() != null) {
                geDate = DateUtils.formatDate(vo.getGe_tallyDate(), "yyyy-MM-dd");
                leDate = DateUtils.formatDate(vo.getLe_tallyDate(), "yyyy-MM-dd");
                sql.append(
                        " and o.tally_date>=BT_TO_DATE(:ge_tallyDate,'yyyy-mm-dd') and o.tally_date<=BT_TO_DATE(:le_tallyDate,'yyyy-mm-dd')");
            }
            if (!checkIsNull(vo.getAccount())) {
                sql.append(" and d.account like :account ");
            }
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));
            sql.append("  order by  o.tally_date asc ");
            List<FcOutAccbookVo> resultData = listBySQLAliasToBean(sql.toString(), param, FcOutAccbookVo.class);
            result.put("res", resultData);
            result.put("dataCount", resultData.size());
        }
        return result;
    }
    /**
     * 功能介绍 :余额查询<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public DataTablesOutput queryBalance(DataTablesInput dataTablesInput, List<String> accounts) {
        /*sql.append(" select b.opt_date as balanceOptDate, b.account as account,a.acc_name as accName, ");
		sql.append(" s.subject_name as subjectName,s.balance_way as balanceWay,BT_NVL_NUM(b.t_balance,0) as tBalance ");
		sql.append(" from  FC_OUT_ACC_BALANCE b");
		sql.append(" left join FC_ACC a");
		sql.append(" on b.account = a.blnctr_acc");
		sql.append(" left join FC_SUBJECT s");
		sql.append(" on a.Subject_id = s.id");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));*/
        String sql = " select b.opt_date as balanceOptDate, b.account as account, " +
                " BT_NVL_NUM(b.t_balance,0) as tBalance " +
                " from  FC_OUT_ACC_BALANCE b where 1=1" +
                " and " + getSQLIn(accounts, 900, "b.account");
        return getDynamicBySQL(dataTablesInput, sql, FcOutAccbookVo.class, "b", true);
    }
    /**
     * 功能介绍 :总账查询<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public List<FcOutAccbookVo> querySumAccount(Integer pageNum, FcOutAccbookVo vo, List<String> corpAuth) {
        Map<String, Object> param = new HashMap<String, Object>();// 保存sql注入参数
        //查询网点 下全部会计科目的日结数据
        StringBuilder tj_sql = new StringBuilder();
        tj_sql.append(" select * from (");
        tj_sql.append(" select distinct sub.subject_code as subjectCode,sub.subject_name as subjectName,sub.id as subjectId,");
        tj_sql.append(" net.id as netId, net.name as netName,cur.cur_code as curCode,cur.id as curId,cur.cur_name as curName,");
        tj_sql.append(" MIN(daySub.Opt_Date) as startDate, MAX(daySub.Opt_Date) as endDate ");
        tj_sql.append(" from fc_out_subject_day daySub, fc_subject sub, sys_corp net, bt_currency cur");
        //and sub.level_num = 0
        tj_sql.append(" where daySub.net_id = net.id and net.type = " + CorpConstant.CORP_TYPE_CODE_NET + "  and daySub.cur_code = cur.id");//0代表网点
        tj_sql.append("  and sub.id = daySub.subject_code ");
        if (!checkIsNull(vo.getNetId())) {
            tj_sql.append(" and daySub.net_id =:netId ");
        }
        if (!checkIsNull(vo.getCurId())) {
            tj_sql.append(" and daySub.cur_code = :curId");
        }
        if (!checkIsNull(vo.getSubjectSort())) {
            tj_sql.append(" and sub.subject_sort =:subjectSort");
        }
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        if (vo.getStartDate() != null && vo.getEndDate() != null) {
            tj_sql.append(" and daySub.Opt_Date >= BT_TO_DATE(:startDate,'yyyy-MM-dd')");
            tj_sql.append(" and daySub.Opt_Date <= BT_TO_DATE(:endDate,'yyyy-MM-dd')");
        }
        tj_sql.append(" and ").append(getSQLIn(corpAuth, 900, "net.id"));
        tj_sql.append(" group by sub.subject_code,sub.subject_name,net.id,net.name,cur.cur_code,cur.cur_name,sub.id,cur.id ");
        tj_sql.append(" union ");
        tj_sql.append(" select distinct father.subject_code as subjectCode,father.subject_name as subjectName, father.id as subjectId,");
        tj_sql.append(" net.id as netId, net.name as netName,cur.cur_code as curCode,cur.id as curId,cur.cur_name as curName,");
        tj_sql.append(" MIN(daySub.Opt_Date) as startDate, MAX(daySub.Opt_Date) as endDate ");
        tj_sql.append(" from fc_out_subject_day daySub,fc_subject sub,fc_subject father, sys_corp net, bt_currency cur");
        tj_sql.append(" where daySub.net_id = net.id and net.type = " + CorpConstant.CORP_TYPE_CODE_NET + " and daySub.cur_code = cur.id and sub.FATHER_ID = father.id");
        tj_sql.append(" and sub.id = daySub.subject_code");
        if (!checkIsNull(vo.getNetId())) {
            tj_sql.append(" and daySub.net_id =:netId ");
        }
        if (!checkIsNull(vo.getCurId())) {
            tj_sql.append(" and daySub.cur_code =:curId ");
        }
        if (!checkIsNull(vo.getSubjectSort())) {
            tj_sql.append(" and sub.subject_sort = :subjectSort");
        }
        if (vo.getStartDate() != null && vo.getEndDate() != null) {
            tj_sql.append(" and daySub.Opt_Date >= BT_TO_DATE(:startDate,'yyyy-MM-dd')");
            tj_sql.append(" and daySub.Opt_Date <= BT_TO_DATE(:endDate,'yyyy-MM-dd')");
        }
        tj_sql.append(" and ").append(getSQLIn(corpAuth, 900, "net.id"));
        tj_sql.append(" group by father.subject_code,father.subject_name,net.id,net.name,cur.cur_code,cur.cur_name,father.id,cur.id ");
        tj_sql.append(" ) order by subjectCode, curId ");
        param.put("netId", vo.getNetId());
        param.put("curId", vo.getCurId());
        param.put("subjectSort", vo.getSubjectSort());
        param.put("startDate", sim.format(vo.getStartDate()));
        param.put("endDate", sim.format(vo.getEndDate()));
        List<FcOutAccbookVo> searchList = listBySQLAliasToBean(tj_sql.toString(), param, FcOutAccbookVo.class);// 集合
        return searchList;
    }
    /**
     * 功能介绍 :总账查询列表<br>
     * <b>日期：</b>2017年9月7日<br>
     *
     * @version 1.0
     * @author zhengxn
     **/
    public List querySumAccountList(FcOutAccbookVo res, List<String> list) {
        String sql = "select d.opt_date as ledgerOptDate," +
                // 收方发生额
                " BT_NVL_NUM(sum(d.debit_count),0) as debitCount,BT_NVL_NUM(sum(d.debit_money),0) as debitMoney, " +
                // 付方发生额
                " BT_NVL_NUM(sum(d.credit_count),0) as creditCount, BT_NVL_NUM(sum(d.credit_money),0) as creditMoney, " +
                // 余额
                " BT_NVL_NUM(sum(d.t_count),0) as tCount,BT_NVL_NUM(sum(d.t_balance),0) as tBalance " +
                " from fc_out_subject_day d  left join fc_subject s on s.id = d.subject_code " +
                " where 1 = 1  and d.SUBJECT_CODE in(:subjectCode) and d.net_id = :netId and d.cur_code = :curCode " +
//		sql.append(" and exists (select 1 from fc_subject ft where (ft.father_id = :subjectCode or ft.id = :subjectCode) ");
//		sql.append(" and ft.id =  d.subject_code)");
                " group by d.opt_date " +
                " order by d.opt_date ";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("subjectCode", list);
        param.put("netId", res.getNetId());
        param.put("curCode", res.getCurId());
        return listBySQL(sql, param);
    }
    /**
     * 功能介绍 :查询期初余额<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public BigDecimal fundInitBalanByAccount(String account, Date date) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.y_balance from fc_out_acc_balance b where b.account=:account");
        sql.append(" and b.opt_date=(select min(opt_date) from fc_out_acc_balance a where b.account=:account ");
        map.put("account", account);
        if (date != null) {
            sql.append(" and a.opt_date >=:ged_tallyDate ");
        }
        if (date != null) {
            sql.append(" and a.opt_date<=:endDate )");
        }
        map.put("ged_tallyDate", date);
        map.put("endDate", date);
        List list = listBySQL(sql.toString(), map);
        double a = 0.00;
        if (list.isEmpty()) {
            return BigDecimal.valueOf(a);
        } else {
            Map<String, Object> result = (Map<String, Object>) list.get(0);
            return (BigDecimal) result.get("Y_BALANCE");
        }
    }
    /**
     * 功能介绍 :查询期初笔数和余额<br>
     * <b>日期：</b>2017年9月7日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public Map<String, Object> getInitDataBySubject(FcOutAccbookVo res) {
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();// 保存sql注入参数的
        // 查询所有科目
        //String subCodeSql = "select id from fc_subject where subject_code='"+ res.getSubjectCode()+"'";
        BigDecimal count = BigDecimal.ZERO;
        double a = 0.00;
        Object money = BigDecimal.valueOf(a);
        // 取小于开始日期的所有笔数作为期初笔数
        StringBuilder sb = new StringBuilder();
        sb.append(" select BT_NVL_NUM(sum(a.t_count),0) as TCOUNT  from fc_out_subject_day a where a.net_id = :netId ");
        sb.append(" and a.cur_code = :curCode   and a.subject_code in ('" + res.getSubjectCode() + "')");
        sb.append(" and a.opt_date <= (select max(opt_date)");
        sb.append(" from fc_out_subject_day c  where 1 = 1  and a.SUBJECT_CODE = c.SUBJECT_CODE");
        if (res.getStartDate() != null) {
            sb.append(" and c.opt_date < :startDate ");
        }
        sb.append(" ) ");
        param.put("netId", res.getNetId());
        param.put("curCode", res.getCurId());
        //	param.put("subjectCodes", subjectIds);
        param.put("startDate", res.getStartDate());
        Map rs = getBySQL(sb.toString(), param);
        if (rs == null) {
            count = BigDecimal.ZERO;
        } else {
            count = new BigDecimal(rs.get("TCOUNT").toString());
        }
		/*if (!numList.isEmpty()) {
			count = (BigDecimal) numList.get(0);
		}*/
        // 取距离开始日期最近的余额作为期初余额
        StringBuilder bal_sql = new StringBuilder();
        bal_sql.append(" select BT_NVL_NUM(sum(a.t_balance),0) as TBALANCE   from fc_out_subject_day a where a.net_id = :netId and a.cur_code = :curCode");
        bal_sql.append(" and a.subject_code in ('" + res.getSubjectCode() + "')  and a.opt_date = (select max(opt_date) from fc_out_subject_day c where 1 = 1");
        bal_sql.append(" and a.SUBJECT_CODE = c.SUBJECT_CODE ");
        if (res.getStartDate() != null) {
            bal_sql.append(" and c.opt_date < :startDate ");
        }
        bal_sql.append(" ) ");
        Map qcblance = getBySQL(bal_sql.toString(), param);
        if (qcblance == null) {
            money = BigDecimal.ZERO;//((BigDecimal) blanceList.get(0));
        } else {
            money = new BigDecimal(qcblance.get("TBALANCE").toString());
        }
        data.put("count", count.intValue());// 期初笔数
        data.put("money", money);// 期初余额
        return data;
    }
    /**
     * 功能介绍 :科目余额汇总表<br>
     * <b>日期：</b>2017年9月9日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public List<FcOutAccbookVo> querySubjectBalance(Integer pageNum, FcOutAccbookVo entity, List<String> corpAuth) {
        String date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append(" select daysub.opt_date as startDate,bc.cur_code as curCode,bc.cur_name as curName,net.id as netId,net.name as netName");
        sql.append(" from fc_out_subject_day daysub, sys_corp net, bt_currency bc,fc_subject sub ");
        sql.append(" where 1 = 1  and daysub.cur_code = bc.id  and daysub.net_id = net.id");
        sql.append(" and sub.id = daysub.subject_code");
        if (!checkIsNull(entity.getNetId())) {
            sql.append(" and net.id = :netId");
        }
        if (!checkIsNull(entity.getCurId())) {
            sql.append(" and bc.cur_code = :curCode");
        }
        if (!checkIsNull(entity.getSubjectSort())) {
            sql.append(" and sub.subject_sort =:subjectSort");
        }
        if (entity.getStartDate() != null) {
            date = sdf.format(entity.getStartDate());
            sql.append(" and daysub.opt_date = BT_TO_DATE(:startDate,'yyyy-MM-dd')");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "net.id"));
        sql.append("  group by daysub.opt_date,bc.cur_code,bc.cur_name, net.id,net.name ");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("netId", entity.getNetId());
        param.put("curCode", entity.getCurId());
        param.put("subjectSort", entity.getSubjectSort());
        param.put("startDate", date);
        // sql.append("");网点控制，后期加
        // sql.append("");权限控制，后期加
        List<FcOutAccbookVo> searchList = listBySQLAliasToBean(sql.toString(), param, FcOutAccbookVo.class);
        return searchList;
    }
    /**
     * 功能介绍 :科目余额汇总table<br>
     * <b>日期：</b>2017年9月9日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public List<FcOutAccbookVo> querySubjectBalanceList(FcOutAccbookVo head) {
        String date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT sub.subject_code AS subjectCode,sub.subject_name AS subjectName,BT_NVL_NUM(daysub.y_balance, 0) AS yBalance,");
        sql.append(" BT_NVL_NUM(daysub.debit_money, 0) AS debitMoney, BT_NVL_NUM(daysub.credit_money, 0) AS creditMoney, sub.balance_way AS balanceWay,");
        sql.append(" BT_NVL_NUM(daysub.t_balance, 0) AS tBalance  FROM fc_out_subject_day daysub LEFT JOIN fc_subject sub");
        sql.append(" ON daysub.subject_code = sub.id LEFT JOIN bt_currency bc  ON daysub.cur_code = bc.id");
        sql.append(" WHERE 1 = 1");
        // 获取有权限的网点，查询的话，加在这个位置，后期加
        if (head.getStartDate() != null) {
            date = sdf.format(head.getStartDate());
            sql.append(" AND daysub.opt_date =BT_TO_DATE(:startDate,'yyyy-MM-dd')");
        }
        if (!checkIsNull(head.getCurId())) {
            sql.append(" AND daysub.cur_code = :curCode");
        }
        if (!checkIsNull(head.getNetId())) {
            sql.append(" AND daysub.net_id = :netCode");
        }
        if (!checkIsNull(head.getSubjectSort())) {
            sql.append(" AND sub.subject_Sort = :subjectSort");
        }
        sql.append(" ORDER BY daysub.subject_code ");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("startDate", date);
        param.put("curCode", head.getCurId());
        param.put("netCode", head.getNetId());
        param.put("subjectSort", head.getSubjectSort());
        return listBySQLAliasToBean(sql.toString(), param, FcOutAccbookVo.class);
    }
    /**
     * 功能介绍 :表外日计账查询<br>
     * <b>日期：</b>2017年9月13日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public List<FcOutAccbookVo> queryOuterDayCharge(FcOutAccbookVo vo, Integer page, List<String> corpAuth) {
        return queryOuterCharge(vo, true, page, corpAuth);// true代表日计账查询
    }
    /**
     * 功能介绍 :表外日/月计账查询<br>
     * <b>日期：</b>2017年9月13日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public List<FcOutAccbookVo> queryOuterCharge(FcOutAccbookVo vo, boolean isDayCharge, Integer page, List<String> corpAuth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = null;
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct net.name as netName,net.id  as netId,cur.id as curId,cur.cur_name as curName,daySub.opt_date as startDate ");
        sql.append(" from fc_out_subject_day daySub, fc_subject sub, sys_corp net,bt_currency cur");
        sql.append(" where daySub.subject_code = sub.id");
        sql.append(" and daySub.net_id = net.id");
        sql.append(" and net.type = " + CorpConstant.CORP_TYPE_CODE_NET);
        sql.append(" and daySub.cur_code = cur.id");
        if (!checkIsNull(vo.getNetId())) {
            sql.append(" and daySub.net_id = :netId");
        }
        if (!checkIsNull(vo.getCurId())) {
            sql.append(" and daySub.cur_code = :curId");
        }
        if (vo.getStartDate() != null) {
            date = sdf.format(vo.getStartDate());
            sql.append(" and daySub.opt_date = BT_TO_DATE(:startDate,'yyyy-MM-dd')");
        }
        if (!checkIsNull(vo.getMeasureUnit())) {
            sql.append(" and sub.measure_unit = :measureUnit");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "net.id"));
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("netId", vo.getNetId());
        param.put("curId", vo.getCurId());
        param.put("startDate", date);
        param.put("measureUnit", vo.getMeasureUnit());
        List<FcOutAccbookVo> res = listBySQLAliasToBean(sql.toString(), param, FcOutAccbookVo.class);
        return res;
    }
    /**
     * 功能介绍 :日计表table<br>
     * <b>日期：</b>2017年9月13日<br>
     * param vo代表查询的头
     *
     * @version 1.0
     * @author zhengxn
     */
    public List<FcOutAccbookVo> queryOuterChargeTable(FcOutAccbookVo vo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = null;
        StringBuilder sql = new StringBuilder();
        sql.append(" select sub.subject_code,sub.subject_name,(BT_NVL_NUM(tt.tCount,0) - BT_NVL_NUM(daysub.t_count, 0)) as yCount,");//期初笔数
        sql.append(" BT_NVL_NUM(daysub.y_balance,0)  as yBalance,");//期初金额
        sql.append(" case sub.measure_unit when 'N' then BT_NVL_NUM(daysub.y_count,0) else 0 end as yNumber,");//个数
        sql.append(" case sub.measure_unit when 'T' then BT_NVL_NUM(daysub.y_count,0)  else 0 end as yTicket,");//张数
        sql.append(" BT_NVL_NUM(daysub.debit_count,0) as debitCount,");//收入笔数
        sql.append(" BT_NVL_NUM(daysub.debit_money,0) as debitMoney,");//收入金额
        sql.append(" case sub.measure_unit when 'N' then BT_NVL_NUM(daysub.debit_count,0)  else 0 end as debitNumber,");//收入个数
        sql.append(" case sub.measure_unit when 'T' then BT_NVL_NUM(daysub.debit_count,0) else 0 end  as debitTicket,");//收入张数
        sql.append(" BT_NVL_NUM(daysub.credit_count,0) as creditCount,");//支出笔数
        sql.append(" BT_NVL_NUM(daysub.credit_money,0)  as creditMoney,");//支出金额
        sql.append(" case sub.measure_unit when 'N' then BT_NVL_NUM(daysub.credit_count,0) else 0 end  as creditNumber,");//支出个数
        sql.append(" case sub.measure_unit when 'T' then BT_NVL_NUM(daysub.credit_count,0) else 0 end  as creditTicket,");//支出张数
        sql.append(" BT_NVL_NUM(daysub.t_count,0) as tCount,");//期末笔数
        sql.append(" BT_NVL_NUM(daysub.t_balance,0)  as tBalance,");//期末金额
        sql.append(" case sub.measure_unit when 'N' then BT_NVL_NUM(daysub.t_count,0) else 0 end as tNumber,");//期末个数
        sql.append(" case sub.measure_unit when 'T' then BT_NVL_NUM(daysub.t_count,0) else 0 end as tTicket");//期末张数
        sql.append(" FROM fc_out_subject_day daysub,fc_subject sub,");
        sql.append(" (SELECT fs.net_id,fs.cur_code,fs.subject_code,SUM(fs.t_count) AS tCount FROM fc_out_subject_day fs");
        sql.append(" WHERE 1 = 1 ");
        if (!checkIsNull(vo.getNetId())) {
            sql.append(" and fs.net_id = :netId");
        }
        if (vo.getStartDate() != null) {
            date = sdf.format(vo.getStartDate());
            sql.append(" and fs.opt_date <= BT_TO_DATE(:startDate,'yyyy-MM-dd')");
        }
        if (!checkIsNull(vo.getCurId())) {
            sql.append(" and fs.cur_code = :curId");
        }
        sql.append(" and fs.t_count > 0");
        sql.append(" GROUP BY fs.net_id, fs.cur_code, fs.subject_code) tt");
        sql.append(" WHERE daysub.subject_code = sub.id AND SUB.id = tt.SUBJECT_CODE");
        sql.append(" AND DAYSUB.NET_id = tt.net_id AND daySUB.CUR_CODE = tt.cur_code");
        if (!checkIsNull(vo.getNetId())) {
            sql.append(" and daysub.net_id = :netId");
        }
        if (vo.getStartDate() != null) {
            date = sdf.format(vo.getStartDate());
            sql.append(" and daysub.opt_date = BT_TO_DATE(:startDate,'yyyy-MM-dd')");
        }
        if (!checkIsNull(vo.getCurId())) {
            sql.append(" and daysub.cur_code = :curId");
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("netId", vo.getNetId());
        param.put("startDate", date);
        param.put("curId", vo.getCurId());
        return listBySQLAliasToBean(sql.toString(), param, FcOutAccbookVo.class);
    }
    /**
     * 功能介绍 :月计表table<br>
     * <b>日期：</b>2017年9月13日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public List<FcOutAccbookVo> queryMonthChargeTable(FcOutAccbookVo vo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append(" select sub.subject_code, sub.subject_name,initsub.y_count as yCount,");// 期初笔数
        sql.append(" case max(sub.measure_unit) when 'M' then initsub.y_balance else 0 end as yBalance,");// 期初金额
        sql.append(" case max(sub.measure_unit) when 'N' then initsub.y_balance else 0 end as yNumber,");// 期初个数
        sql.append(" case max(sub.measure_unit) when 'T' then initsub.y_balance else 0 end as yTicket,");// 期初张数
        sql.append(" BT_NVL_NUM(sum(daysub.debit_count),0) as debitCount,");// 收入笔数
        sql.append(" sum(case sub.measure_unit when 'M' then daysub.debit_money else 0 end) as debitMoney,");// 收入金额
        sql.append(" sum(case sub.measure_unit when 'N' then daysub.debit_money else 0 end) as debitNumber,");// 收入个数
        sql.append(" sum(case sub.measure_unit when 'T' then daysub.debit_money else 0 end) as debitTicket,");// 收入张数
        sql.append(" BT_NVL_NUM(sum(daysub.credit_count),0) as creditCount,");// 支出笔数
        sql.append(" sum(case sub.measure_unit when 'M' then daysub.credit_money else 0 end) as creditMoney,");// 支出金额
        sql.append(" sum(case sub.measure_unit when 'N' then daysub.credit_money else 0 end) as creditNumber,");// 支出个数
        sql.append(" sum(case sub.measure_unit when 'T' then daysub.credit_money else 0 end) as creditTicket,");// 支出张数
        sql.append(" (BT_NVL_NUM(initsub.y_count,0) + BT_NVL_NUM(sum(daysub.debit_count),0) + BT_NVL_NUM(sum(daysub.credit_count),0)) as tCount,");// 期末笔数
        sql.append(" (initsub.y_balance + sum(case sub.measure_unit when 'M' then daysub.debit_money else 0 end)");
        sql.append(" - sum(case sub.measure_unit when 'M' then daysub.credit_money else 0 end)) as tBalance,");// 期末金额
        sql.append(" (initsub.y_balance + sum(case sub.measure_unit when 'N' then daysub.debit_money else 0 end)");
        sql.append(" - sum(case sub.measure_unit when 'N' then daysub.credit_money else 0 end)) as tNumber,");// 期末个数
        sql.append(" (initsub.y_balance + sum(case sub.measure_unit when 'T' then daysub.debit_money else 0 end)");
        sql.append(" - sum(case sub.measure_unit when 'T' then daysub.credit_money else 0 end)) as tTicket");// 期末张数
        sql.append(" from  fc_out_subject_day daySub,fc_out_subject_day initSub,fc_subject sub");
        sql.append(" where daySub.subject_code=sub.subject_code");
        sql.append(" and daySub.subject_code=initSub.subject_code");
        if (EmptyUtils.isNotEmpty(vo.getNetId()) && !" ".equals(vo.getNetId())) {
            sql.append(" and daySub.net_id = :netId");
        }
        if (vo.getStartDate() != null) {
            sql.append(" and daySub.opt_date = BT_TO_DATE(:startDate,'yyyy-MM-dd')");
        }
        if (EmptyUtils.isNotEmpty(vo.getCurId()) && !" ".equals(vo.getCurId())) {
            sql.append(" and daySub.cur_code = :curId");
        }
        sql.append(" and initSub.opt_date=(select min(opt_date) from fc_out_subject_day minSub where 1=1");
        sql.append(" and minSub.subject_code=daySub.subject_code");
        sql.append(" and minSub.cur_code=daySub.cur_code");
        sql.append(" and minSub.net_id=daySub.net_id )");
        sql.append(" group by sub.subject_code,sub.subject_name,initSub.y_balance,initSub.y_count");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("netId", vo.getNetId());
        param.put("startDate", sdf.format(vo.getStartDate()));
        param.put("curId", vo.getCurId());
        return listBySQLAliasToBean(sql.toString(), param, FcOutAccbookVo.class);
    }
    /**
     * 功能介绍 :查询所有表外科目<br>
     * <b>日期：</b>2017年9月13日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public List<FcSubject> getAllOuterFcSubjects() {
        String sql = " SELECT fs.subject_code AS subjectCode, fs.subject_name AS subjectName, fs.father_id  AS fatherId " +
                "  FROM fc_subject fs WHERE fs.subject_sort = 'G' AND fs.valid_sign = '1' AND fs.measure_unit = 'M' ";
        return listBySQLAliasToBean(sql, null, FcSubject.class);
    }
    private static boolean checkIsNull(String str) {
        return EmptyUtils.isEmpty(str) || "".equals(str.trim()) || "undefined".equals(str);
    }
}
