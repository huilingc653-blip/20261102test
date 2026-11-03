package com.bytter.bill.domain.bill.dao;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbBillAccBal;
import com.bytter.bill.domain.bill.service.AbBtBankAccService;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.vo.AbBillAccBalVO;
import com.bytter.bill.domain.bill.vo.AbBillAccDtlBalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhengg
 * @version 1.0
 * @Title: 票据账户余额Dao
 * @Description: AB_BILL_ACC_BAL
 * @date 2017-09-28 19:20:01
 */
@Repository
public class AbBillAccBalDao extends BaseDao<AbBillAccBal, String> {
    @Autowired
    private AbBtBankAccService abBtBankAccService;
    /**
     * 根据账户ID和余额日期获取 小于等于余额日期最近一条余额数据
     *
     * @param bankId  账户ID
     * @param corpId  单位ID
     * @param balDate 余额日期
     * @return
     */
    public AbBillAccBal getAbBillAccBalLeft1ByBalDate(String bankId, String corpId, Date balDate) {
        String hql = "from AbBillAccBal where ";
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(bankId)) {
            hql += " bankId=:bankId ";
            params.put("bankId", bankId);
        } else {
            hql += " bankId is null and corpId=:corpId ";
            params.put("corpId", corpId);
        }
        hql += " and balDate<=:balDate order by balDate desc";
        params.put("balDate", balDate);
        Query query = this.createQuery(hql, params).setMaxResults(1);
        return (AbBillAccBal) query.uniqueResult();
    }
    /**
     * 根据账户ID和余额日期获取 大于余额日期最近一条余额数据
     *
     * @param bankId  账户ID
     * @param balDate 余额日期
     * @return
     */
    public AbBillAccBal getAbBillAccBalRight1ByBalDate(String bankId, String corpId, Date balDate) {
        String hql = "from AbBillAccBal where ";
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(bankId)) {
            hql += " bankId=:bankId ";
            params.put("bankId", bankId);
        } else {
            hql += " bankId is null and corpId=:corpId ";
            params.put("corpId", corpId);
        }
        hql += " and balDate>:balDate order by balDate asc";
        params.put("balDate", balDate);
        Query query = this.createQuery(hql, params).setMaxResults(1);
        return (AbBillAccBal) query.uniqueResult();
    }
    /**
     * 修改时间区间的余额
     *
     * @param billAccBal
     * @return
     */
    public int updateBankAccBillBal(AbBillAccBalVO billAccBal) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder hql = new StringBuilder("update AbBillAccBal ");
        hql.append("set bal=bal+:money ");
        hql.append(",availBal=availBal+:availMoney ");
        hql.append(",tiedupBal=tiedupBal+:tiedupMoney ");
        hql.append(",spBal=spBal+:spMoney ");
        hql.append(",spAvailBal=spAvailBal+:spAvailMoney ");
        hql.append(",spTiedupBal=spTiedupBal+:spTiedupMoney ");
        hql.append(",ypBal=ypBal+:ypMoney ");
        hql.append(",ypAvailBal=ypAvailBal+:ypAvailMoney ");
        hql.append(",ypTiedupBal=ypTiedupBal+:ypTiedupMoney ");
        if (StringUtils.isNotEmpty(billAccBal.getUpdateBy())) {
            hql.append(",updateBy=:updateBy ");
            params.put("updateBy", billAccBal.getUpdateBy());
        }
        if (billAccBal.getUpdateDate() != null) {
            hql.append(",updateDate=:updateDate ");
            params.put("updateDate", billAccBal.getUpdateDate());
        }
        //Mysql并发锁表，先查询再更新
        StringBuilder sql = new StringBuilder("select id from AB_BILL_ACC_BAL ");
        sql.append("where 1=1 ");
        if (StringUtils.isNotBlank(billAccBal.getBankId())) {
            sql.append("and bank_Id=:bankId ");
            params.put("bankId", billAccBal.getBankId());
        } else {
            sql.append("and bank_Id is null and corp_Id=:corpId ");
            params.put("corpId", billAccBal.getCorpId());
        }
        if (billAccBal.getBeginDate() != null) {
            sql.append("and bal_Date>=:beginBalDate ");
            params.put("beginBalDate", billAccBal.getBeginDate());
        }
        List<String> list = this.createSQLQuery(sql.toString(), params).list();
        if (EmptyUtils.isNotEmpty(list)) {
            hql.append(" where ").append(SqlUtil.getInSql(list, "id", true));
            params.put("money", billAccBal.getMoney());
            params.put("availMoney", billAccBal.getAvailMoney());
            params.put("tiedupMoney", billAccBal.getTiedupMoney());
            params.put("spMoney", billAccBal.getSpMoney());
            params.put("spAvailMoney", billAccBal.getSpAvailMoney());
            params.put("spTiedupMoney", billAccBal.getSpTiedupMoney());
            params.put("ypMoney", billAccBal.getYpMoney());
            params.put("ypAvailMoney", billAccBal.getYpAvailMoney());
            params.put("ypTiedupMoney", billAccBal.getYpTiedupMoney());
            return this.batchExecute(hql.toString(), params);
        }
        return 0;
    }
    /**
     * 列表显示票据账户余额信息
     *
     * @param input
     * @param corpAuthList
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillAccBal(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT BT_CONCAT2(b.bank_acc,b.bal_date) as id,");
        sql.append(" b.bank_id AS bankId,b.bank_acc AS bankAcc,b.corp_id AS corpId,b.cur_id AS curId,b.bal_date AS balDate, ");
        sql.append(" b.bal AS bal,b.avail_bal AS availBal,b.tiedup_bal AS tiedupBal, ");
        sql.append(" b.yp_bal AS ypBal,b.yp_avail_bal AS ypAvailBal,b.yp_tiedup_bal AS ypTiedupBal, ");
        sql.append(" b.sp_bal AS spBal,b.sp_avail_bal AS spAvailBal,b.sp_tiedup_bal AS spTiedupBal, ");
        sql.append(" c.code as corpCode,c.name as corpName ");//TOOD
//        sql.append(" FROM AB_BILL_ACC_BAL b LEFT JOIN SYS_CORP C ON  b.corp_id= C.id ");
        sql.append(" FROM AB_BILL_ACC_BAL b  ");
//        sql.append(" left join bt_currency r on b.cur_id=r.id ");
        sql.append(" where 1=1 ");
//        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.id", params);
//        sql.append(" and ").append(corpAuthSql).append(" ");
        if(input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (b.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.bank_acc", "bankAccs", params)).append(")");
        }
        return this.dealDynamicBySQL(input, sql.toString(), AbBillAccBalVO.class, "", false, params);
    }
    /**
     * 查询余额有账户数据
     *
     * @return
     */
    public List<String> getBankAccListHasAcc() {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(" b.bank_acc from ab_bill_acc_bal b where b.bal_date>=:bal_date and b.bank_id is not null group by b.bank_acc");
        Date bal_date = DateUtils.addDay(new Date(), -1* AccebillConstant.AB_BILLBAL_LASTDAY);
        params.put("bal_date", bal_date);
        return createSQLQuery(sql.toString(), params).list();
    }
    /**
     * 查询无账户余额数据
     *
     * @return
     */
    public List<String> getCorpIdListNotAcc() {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(" b.corp_id from ab_bill_acc_bal b where b.bal_date>=:bal_date and b.bank_id is null  group by b.corp_id ");
        Date bal_date = DateUtils.addDay(new Date(),-1* AccebillConstant.AB_BILLBAL_LASTDAY);
        params.put("bal_date", bal_date);
        return createSQLQuery(sql.toString(), params).list();
    }
    
    /**
     * 根据参数生成票据余额
     * @param params 传递参数
     * @return
     */
    public int deleteAbBillAccBal(Map<String,Object> params,List<String> corpQueryList){
    	if(params==null){
    		return 0;
    	}
    	StringBuilder sql = new StringBuilder();
    	sql.append("delete from ab_bill_acc_bal where 1=1 ");
    	if(params.containsKey("beginDate")){
            sql.append(" and bal_date>=:beginDate ");
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpQueryList, "corp_id", "corpAuth", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
    	return this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 根据参数删除指定票据余额数据
     * @param params 传递参数
     * @return
     */
    public int deleteAbBillAccBalByBalDate(Map<String,Object> params,List<String> corpQueryList){
        if(params==null){
            return 0;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ab_bill_acc_bal where 1=1 ");
        if(params.containsKey("beginDate")){
            sql.append(" and bal_date=:beginDate ");
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpQueryList, "corp_id", "corpAuth", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return this.batchSQLExecute(sql.toString(), params);
    }

    /**
     * 根据条件获取票据余额明细情况数据
     * @param params 过滤条件
     * @param corpQueryList 有权限的单位
     * @return
     */
    public List<AbBillAccDtlBalVO> listAbBillAccDtlBal(Map<String,Object> params, List<String> corpQueryList) {
        StringBuilder endDate = new StringBuilder();
        endDate.append(" ( ");
        endDate.append(" case when a1.brief_id='6011' and a2.status=95 and a2.opt_status='19' then a2.business_date ");
        endDate.append(" when a1.brief_id!='6011' and a1.status=95 and a1.opt_status='19' then a1.business_date ");
        endDate.append(" else BT_TO_DATE('").append(DateUtils.format(DateUtils.addDay(new Date(),1),DateUtils.PATTERN_YYYYMMDD)).append("','yyyyMmdd') ");
        endDate.append(" end ");
        endDate.append(" ) ");
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(" vv.corp_id as corpId,a.req_acct_id as bankAcc,a.req_Acct_Id as bankAccId,v.bill_money as billMoney,a.business_date as beginDate,v.BILL_FLAG as billFlag,");
        sql.append(" a.id as accId,a.send_sign,a.opt_status as optStatus,a1.id as accIdOut,a1.send_sign as sendSignOut,a2.id as accIdDiscount,a2.send_sign as sendSignDiscount,");
        sql.append(endDate);
        sql.append(" as endDate ");
        sql.append(" ,a.business_date as inDate,a1.business_date as outDate,a1.status as outstatus,a1.opt_status as outOptStatus,a2.status as outDiscountStatus," +
                "a2.opt_status as outDiscountOptStatus,a2.business_date as outDiscountDate,v.bill_type,a.brief_id as briefIdIn,a1.brief_id as briefIdOut ");
        sql.append(" from ab_voucherinfo v  ");
        sql.append(" inner join ab_voucherinfo_vice vv on v.id=vv.vid and vv.type='1' and (v.OLD_DRAFT_ID is null or v.OLD_DRAFT_ID not like '%-del')");
        sql.append(" inner join ab_accrecords a on a.vvid=vv.id and a.brief_id in ('6001','9003','9010','9012','9018','9019','6019','6212') and a.opt_status='19' and a.status=95 ");
        sql.append(" left join ab_accrecords a1 on a.id=a1.lastid and a1.status not in (-2) and a1.opt_status not in ('-3','-4') and a1.brief_id not in ('6007','6008') ");
        sql.append(" left join ab_accrecords a2 on a1.id=a2.lastid and a2.status not in (-2) and a2.brief_id='011' and a2.opt_status not in ('-3','-4') ");
        //sql.append(" left join bt_bank_acc ba on ba.bank_acc=a.req_acct_id ");
        //sql.append(" left join sys_corp sc on sc.id=vv.corp_id ");
        //sql.append(" left join bt_bank_type bkt on bkt.id=ba.BANK_TYPE_ID ");
        sql.append(" where 1=1  ");
        if(params.containsKey("beginDate")){
            sql.append(" and :beginDate<=").append(endDate);
        }
        if(params.containsKey("endDate")){
            params.put("endDateStr",DateUtils.format((Date)params.get("endDate"),DateUtils.PATTERN_YYYYMMDD));
            sql.append(" and BT_TO_CHAR(a.business_date,'yyyyMmdd')<=:endDateStr ");
        }
        if(params.containsKey("accountGrantList")){
            List<String> accountGrantList = params.get("accountGrantList") == null ? null : (List<String>) params.get("accountGrantList");
            sql.append(" and (ba.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "ba.bank_acc", "bankAccs", params)).append(")");
        }

        String corpAuthSql = SqlUtil.getInSqlByParams(corpQueryList, "vv.corp_id", "corpAuth", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        sql.append(" order by a.business_date,vv.bank_acc ");
        List<AbBillAccDtlBalVO> abBillAccDtlBalVOS = this.listBySQLAliasToBean(sql.toString(), params, AbBillAccDtlBalVO.class);
        if(CollectionUtil.isEmpty(abBillAccDtlBalVOS)){
            return abBillAccDtlBalVOS;
        }
        List<String> reqBankAccList = abBillAccDtlBalVOS.stream().map(AbBillAccDtlBalVO::getBankAccId).collect(Collectors.toList());
        Map<String, BtBankAccDTO> billBankAccByBankAccList = abBtBankAccService.getBillBankAccByBankAccList(reqBankAccList);
        for(AbBillAccDtlBalVO billAccDtlBalVO : abBillAccDtlBalVOS){
            if(billBankAccByBankAccList.containsKey(billAccDtlBalVO.getBankAcc())
                    && billBankAccByBankAccList.get(billAccDtlBalVO.getBankAcc())!=null) {
                billAccDtlBalVO.setBankAccId(billBankAccByBankAccList.get(billAccDtlBalVO.getBankAcc()).getId());
                billAccDtlBalVO.setBankName(billBankAccByBankAccList.get(billAccDtlBalVO.getBankAcc()).getBankName());
                billAccDtlBalVO.setTypeName(billBankAccByBankAccList.get(billAccDtlBalVO.getBankAcc()).getAccTypeName());
            }
        }
        return abBillAccDtlBalVOS;
    }
    /**
     * 根据条件获取票据余额明细情况数据
     * @param params 过滤条件
     * @param corpQueryList 有权限的单位
     * @return
     */
    public List<AbBillAccDtlBalVO> getAbBillAccDtlBalList(Map<String,Object> params, List<String> corpQueryList, Object accountGrantObj) {
        StringBuilder endDate = new StringBuilder();
        endDate.append(" bt_nvl_date( ");
        endDate.append(" ( ");
        endDate.append(" case when a1.brief_id='6011' and a2.status=95 and a2.opt_status='19' then a2.business_date ");
        endDate.append(" when a1.brief_id!='6011' and a1.status=95 and a1.opt_status='19' then a1.business_date ");
        endDate.append(" else null ");
        endDate.append(" end ");
        endDate.append(" ) ");
        endDate.append(" ,bt_sysdate())");
        StringBuilder sql = new StringBuilder("SELECT ");
//        sql.append(" vv.corp_id as corpId,a.req_acct_id as bankAcc,ba.id as bankAccId,v.bill_money as billMoney,a.business_date as beginDate,v.BILL_FLAG as billFlag,");
        sql.append(" vv.corp_id as corpId,a.req_acct_id as bankAcc, v.bill_money as billMoney,a.business_date as beginDate,v.BILL_FLAG as billFlag,");
        sql.append(" a.id as accId,a.send_sign,a.opt_status as optStatus,a1.id as accIdOut,a1.send_sign as sendSignOut,a2.id as accIdDiscount,a2.send_sign as sendSignDiscount,");
        sql.append(endDate);
        sql.append(" as endDate ");
        sql.append(" ,a.business_date as inDate,a1.business_date as outDate,a1.status as outstatus,a1.opt_status as outOptStatus,a2.status as outDiscountStatus," +
                "a2.opt_status as outDiscountOptStatus,a2.business_date as outDiscountDate,v.bill_type,a.brief_id as briefIdIn,a1.brief_id as briefIdOut ");
        sql.append(" from ab_voucherinfo v  ");
        sql.append(" inner join ab_voucherinfo_vice vv on v.id=vv.vid and vv.type='1' ");
        sql.append(" inner join ab_accrecords a on a.vvid=vv.id and a.brief_id in ('6001','9003','9010','9012','9018','9019','6019','6212') and a.opt_status='19' and a.status=95 ");
        sql.append(" left join ab_accrecords a1 on a.id=a1.lastid and a1.status not in (-2) and a1.opt_status not in ('-3','-4') and a1.brief_id not in ('6007','6008') ");
        sql.append(" left join ab_accrecords a2 on a1.id=a2.lastid and a2.status not in (-2) and a2.brief_id='011' and a2.opt_status not in ('-3','-4') ");
//        sql.append(" left join bt_bank_acc ba on ba.bank_acc=a.req_acct_id ");
        sql.append(" where 1=1  ");
        if(params.containsKey("beginDate")){
//            sql.append(" and a.business_date<=:beginDate and :beginDate<=").append(endDate);
            sql.append(" and :beginDate<=").append(endDate);
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpQueryList, "vv.corp_id", "corpAuth", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if(!ObjectUtils.isEmpty(accountGrantObj)) {
            //添加账户授权控制
            List<String> accountGrantList =  (List<String>) accountGrantObj;
            if(accountGrantList.size() > 0){
                sql.append(" and (a.req_acct_id is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "a.req_acct_id", "bankAccs", params)).append(")");
            }
        }
        //sql.append(" order by a.business_date ");
        return this.listBySQLAliasToBean(sql.toString(), params, AbBillAccDtlBalVO.class);
    }
}
