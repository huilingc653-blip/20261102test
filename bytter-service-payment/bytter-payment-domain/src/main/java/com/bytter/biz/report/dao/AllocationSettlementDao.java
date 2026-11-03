package com.bytter.biz.report.dao;

import com.bytter.biz.bdlink.api.constant.BisAccBalConstant;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.report.vo.AllocationSettlementDtlVO;
import com.bytter.biz.report.vo.AllocationSettlementVO;
import com.bytter.biz.report.vo.CalculateGetMoneyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.google.common.base.Splitter;
import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhoup
 * @Description 结算量统计表
 * @date 2018.4.18
 */
@Repository
public class AllocationSettlementDao extends BaseDao<BisAccDtlDTO, String> {
    /**
     * 通过单位查询银行账户明细
     * 查询规则为查出明细表以及余额表里面存在的有效数据，并不以任何表为主体
     * 在取期初期末余额时先去掉已删除或无效账号
     * 期初余额为余额表里按照初始时间查询并以corpId以及CurId分组，且对应账号为有效账号以及账号没有删除的余额(bal)之和，如何初始查询时间查询余额为空，那么取离初始最近一边的余额，若无数据，则为空
     * 期末余额取当天的值
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataByCorp(AllocationSettlementVO asv, String joinSql) {
        String dialect = ((SessionImpl) getSession()).getFactory().getDialect().getClass().getName();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", DateUtils.formatDate(asv.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
        params.put("beginDates", DateUtils.formatDate(asv.getBeginDate()));
        params.put("endDate", DateUtils.formatDate(asv.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
        params.put("endDates", DateUtils.formatDate(asv.getEndDate()));
        sql.append("SELECT hcorpId as corpId,sc.name AS corpName,sc.code AS code,bc.CUR_NAME AS curName,hcurId as curId,sum( startMoney ) AS startMoney,sum( getamt ) AS getMoney,sum( getnumbers ) AS getNumbers,");
        sql.append("sum( borrowamt ) AS borrowMoney,sum( borrownumbers ) AS borrowNumbers,sum( endMoney ) AS endMoney FROM(");
        //收入
        sql.append("SELECT acc.corp_id AS hcorpId,d.cur_id AS hcurId,0 AS startMoney,sum( CASE WHEN d.rb_sign = '2' OR d.rb_sign IS NULL THEN d.amt ELSE - d.amt END ) AS getamt,");
        sql.append("count( * ) AS getnumbers,0 AS borrowamt,0 AS borrownumbers,0 AS endMoney FROM bis_acc_dtl d LEFT JOIN bt_bank_acc acc ON d.bank_acc = acc.bank_acc ");
        sql.append(" where acc.status <> -2 and acc.valid_sign = 1");
        sql.append(" and d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) AND d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" and d.cd_sign='0' and acc.corp_id is not null group by acc.corp_id,d.cur_id");
        sql.append(" UNION ALL ");
        //支出
        sql.append("SELECT acc.corp_id AS hcorpId,d.cur_id AS hcurId,0 AS startMoney,0 AS getamt,0 AS getnumbers,");
        sql.append("sum( CASE WHEN d.rb_sign = '2' OR d.rb_sign IS NULL THEN d.amt ELSE - d.amt END ) AS borrowamt,count( * ) AS borrownumbers,0 AS endMoney  FROM bis_acc_dtl d LEFT JOIN bt_bank_acc acc ON d.bank_acc = acc.bank_acc ");
        sql.append(" where acc.status <> -2 and acc.valid_sign = 1");
        sql.append(" and d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) AND d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" and d.cd_sign='1' and acc.corp_id is not null group by acc.corp_id,d.cur_id");
        sql.append(" UNION ALL ");
        //期初
        sql.append("SELECT b.corp_id AS hcorpId,b.cur_id AS hcurId,sum( b.bal ) AS startMoney,0 AS getamt,0 AS getnumbers,0 AS borrowamt,0 AS borrownumbers,0 AS endMoney");
        sql.append(" from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 and ");
        if (dialect.toLowerCase().contains("oracle")) {//如果是oracle数据库
            sql.append(" b.bal_date=(select  max(bal_date) from bis_acc_bal bal where bal_date < bt_to_date(:beginDates,'YYYY-MM-DD') and bal.bank_acc=b.bank_acc and bal.cur_id=b.cur_id) and b.bal is not null group by b.corp_id,b.cur_id");
        } else {
            sql.append(" b.bal_date=(select  bal_date from (select b.CORP_ID,b.CUR_ID,b.BANK_ACC,max(b.BAL_DATE) as BAL_DATE from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 and b.bal_date < bt_to_date(:beginDates,'YYYY-MM-DD') group by b.corp_id,b.cur_id,b.BANK_ACC) bal where   bal.bank_acc=b.bank_acc and bal.cur_id=b.cur_id limit 1) and b.bal is not null group by b.corp_id,b.cur_id");
        }
        sql.append(" UNION ALL ");
        //期末
        sql.append("SELECT b.corp_id AS hcorpId,b.cur_id AS hcurId,0 AS startMoney,0 AS getamt,0 AS getnumbers,0 AS borrowamt,0 AS borrownumbers,sum( b.bal ) AS endMoney");
        sql.append(" from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 and ");
        if (dialect.toLowerCase().contains("oracle")) {
            sql.append(" b.bal_date=(select  max(bal_date) from bis_acc_bal bal where bal_date <= bt_to_date(:endDates,'YYYY-MM-DD') and bal.bank_acc=b.bank_acc and bal.cur_id=b.cur_id) and b.bal is not null group by b.corp_id,b.cur_id");
        } else {
            sql.append(" b.bal_date=(select  bal_date from (select b.CORP_ID,b.CUR_ID,b.BANK_ACC,max(b.BAL_DATE) as BAL_DATE from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 and b.bal_date <= bt_to_date(:endDates,'YYYY-MM-DD') group by b.corp_id,b.cur_id,b.BANK_ACC) bal where   bal.bank_acc=b.bank_acc and bal.cur_id=b.cur_id limit 1) and b.bal is not null group by b.corp_id,b.cur_id");
        }
        sql.append(" ) hl left join sys_corp sc ON hl.hcorpId = sc.id left join bt_currency bc ON hl.hcurId = bc.id where 1=1");
        if (!EmptyUtils.isBlank(asv.getCorpId())) {
            List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(asv.getCorpId()));
            String inStr = getSQLIn(result, 1000, "hl.hcorpId");
            sql.append(" and " + inStr);
        }
        if (!EmptyUtils.isBlank(asv.getCurId())) {
            List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(asv.getCurId()));
            String inStr = getSQLIn(result, 1000, "hl.hcurId");
            sql.append(" and " + inStr);
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(" and ");
            sql.append(joinSql);
        }
        sql.append(" GROUP BY hl.hcorpId, hl.hcurId,sc.code,sc.name,bc.CUR_NAME");
        if (StringUtils.isNotEmpty(asv.getOrderPara()) && StringUtils.isNotEmpty(asv.getOrderByPara())) {
            sql.append(" order by " + asv.getOrderPara() + " " + asv.getOrderByPara());
        } else {
            sql.append(" order by sc.code asc,hcurId desc");
        }
        List<AllocationSettlementVO> lists = listBySQLAliasToBean(sql.toString(), params, AllocationSettlementVO.class);
        List<AllocationSettlementVO> list = new ArrayList<>();
        int length = 0;
        if (asv.getStart() + asv.getLength() <= lists.size()) {
            length = asv.getStart() + asv.getLength();
        } else {
            length = lists.size();
        }
        for (int i = asv.getStart(); i < length; i++) {
            list.add(lists.get(i));
        }
//        List<AllocationSettlementVO> list = listBySQL(sql.toString(), asv.getStart(), asv.getLength(), params);
//        list = BeanUtils.copyMapListProperties(list, AllocationSettlementVO.class);
//        ////inject(list);
        List<AllocationSettlementVO> listOver = this.dealData(lists, list, asv.getStart(), asv.getLength());
        long count;
        if (lists.size() > 0) {
            count = (long) lists.size();
        } else {
            count = 0L;
        }
        return new DataTablesOutput(asv.getDraw(), listOver, count, count);
    }

    /**
     * 通过银行账号查询银行账户明细
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataByBankAcc(AllocationSettlementVO asv, String joinSql) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", DateUtils.formatDate(asv.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
        params.put("beginDates", DateUtils.formatDate(asv.getBeginDate()));
        params.put("endDate", DateUtils.formatDate(asv.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
        params.put("endDates", DateUtils.formatDate(asv.getEndDate()));
        sql.append("select acc.bank_acc as bankAcc, acur.cur_Id as curId, bc.cur_name as curName,");
        sql.append(" acc.acc_type as accType, acc.attribute_id as attributeId, att.attribute_name as attributeName,");
        sql.append(" acc.nature_id as natureId, n.nature_name as natureName,");
        sql.append(" (SELECT bt_nvl_num(sum( CASE WHEN d.rb_sign = '2' OR d.rb_sign IS NULL THEN d.amt ELSE - d.amt END ) ,0) FROM bis_acc_dtl d ");
        sql.append(" WHERE d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) AND d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" AND d.cd_sign = '0' AND acc.bank_acc = d.bank_acc  AND acur.cur_Id = d.cur_id");
        sql.append(" GROUP BY d.bank_acc, d.cur_id ) AS getMoney,");
        sql.append(" (SELECT bt_nvl_num(count( * ) ,0) FROM bis_acc_dtl d ");
        sql.append(" WHERE d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) AND d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" AND d.cd_sign = '0' AND acc.bank_acc = d.bank_acc  AND acur.cur_Id = d.cur_id");
        sql.append(" GROUP BY d.bank_acc, d.cur_id ) AS getNumbers,");
        sql.append(" (SELECT bt_nvl_num(sum( CASE WHEN d.rb_sign = '2' OR d.rb_sign IS NULL THEN d.amt ELSE - d.amt END ) ,0) FROM bis_acc_dtl d ");
        sql.append(" WHERE d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) AND d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" AND d.cd_sign = '1' AND acc.bank_acc = d.bank_acc  AND acur.cur_Id = d.cur_id");
        sql.append(" GROUP BY d.bank_acc, d.cur_id ) AS borrowMoney,");
        sql.append(" (SELECT bt_nvl_num(count( * ) ,0) FROM bis_acc_dtl d ");
        sql.append(" WHERE d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) AND d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" AND d.cd_sign = '1' AND acc.bank_acc = d.bank_acc  AND acur.cur_Id = d.cur_id");
        sql.append(" GROUP BY d.bank_acc, d.cur_id ) AS borrowNumbers,");
//        sql.append(" (SELECT bt_nvl_num(sum( b.bal ) ,0) FROM bis_acc_bal b  ");
//        sql.append(" WHERE b.bal_date = ( SELECT  bal_date  FROM (select b.CORP_ID,b.CUR_ID,b.BANK_ACC,max(b.BAL_DATE) as BAL_DATE from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 and b.bal_date  < bt_to_date(:beginDates,'yyyy-MM-dd') group by b.corp_id,b.cur_id,b.BANK_ACC) bal WHERE   bal.bank_acc=b.bank_acc and bal.cur_id=b.cur_id limit 1 ) ");
//        sql.append(" AND acc.bank_acc = b.bank_acc AND acur.cur_Id = b.cur_id ) AS startMoney,");
        sql.append(" bt_nvl_num(bal_start.startMoney,0) as  startMoney, ");
//        sql.append(" (SELECT bt_nvl_num(sum( b.bal ) ,0) FROM bis_acc_bal b  ");
//        sql.append(" WHERE b.bal_date = ( SELECT bal_date  FROM (select b.CORP_ID,b.CUR_ID,b.BANK_ACC,max(b.BAL_DATE) as BAL_DATE from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 and b.bal_date  <= bt_to_date(:endDates,'yyyy-MM-dd') group by b.corp_id,b.cur_id,b.BANK_ACC) bal WHERE  bal.bank_acc=b.bank_acc and bal.cur_id=b.cur_id limit 1 ) ");
//        sql.append(" AND acc.bank_acc = b.bank_acc AND acur.cur_Id = b.cur_id ) AS endMOney");
        sql.append(" bt_nvl_num(bal_end.endMoney,0) as endMoney ");
        sql.append(" from bt_bank_acc acc left join bt_bank_acc_cur acur on acc.id = acur.bank_acc_id");
        sql.append(" left join bt_currency bc on acur.cur_id = bc.id");
        sql.append(" left join bis_acc_attribute att on acc.attribute_id = att.id");
        sql.append(" left join bt_acc_nature n on acc.nature_id = n.id");

        //期初取值改成left join 之前那种在查询里面写sql性能太慢 而且用的limit 1 Oracle兼容 xiangs
        sql.append(" LEFT JOIN (SELECT b.bank_acc,b.cur_id,MAX(b.bal) AS startMoney ");
        sql.append("    FROM bis_acc_bal b ");
        sql.append("     INNER JOIN (  ");
        sql.append("         SELECT  corp_id, cur_id, bank_acc, MAX(bal_date) AS bal_date ");
        sql.append("         FROM bis_acc_bal b2 ");
        sql.append("         WHERE b2.bal_date < bt_to_date(:beginDates, 'yyyy-MM-dd')  ");
        sql.append("         GROUP BY corp_id, cur_id, bank_acc  ");
        sql.append("     ) t ON b.corp_id = t.corp_id AND b.cur_id = t.cur_id AND b.bank_acc = t.bank_acc AND b.bal_date = t.bal_date  ");
        sql.append("     GROUP BY b.bank_acc, b.cur_id  ");
        sql.append(" ) bal_start ON acc.bank_acc = bal_start.bank_acc AND acur.cur_Id = bal_start.cur_id ");
        //期末
        sql.append(" LEFT JOIN ( SELECT b.bank_acc, b.cur_id, MAX(b.bal) AS endMoney ");
        sql.append("     FROM bis_acc_bal b ");
        sql.append("     INNER JOIN ( ");
        sql.append("         SELECT  corp_id, cur_id, bank_acc, MAX(bal_date) AS bal_date ");
        sql.append("         FROM bis_acc_bal b2 ");
        sql.append("         WHERE b2.bal_date <= bt_to_date(:endDates, 'yyyy-MM-dd')  ");
        sql.append("         GROUP BY corp_id, cur_id, bank_acc ");
        sql.append("     ) t ON b.corp_id = t.corp_id AND b.cur_id = t.cur_id AND b.bank_acc = t.bank_acc AND b.bal_date = t.bal_date ");
        sql.append("     GROUP BY b.bank_acc, b.cur_id ");
        sql.append(" ) bal_end ON acc.bank_acc = bal_end.bank_acc AND acur.cur_Id = bal_end.cur_id ");


        sql.append(" where acc.status <> -2 and acc.valid_sign = 1 ");
        if (!EmptyUtils.isBlank(asv.getBankAcc())) {
            sql.append(" and acc.bank_acc like :bankAcc");
            params.put("bankAcc", '%' + asv.getBankAcc() + '%');
        }
        if (!EmptyUtils.isBlank(asv.getCurId())) {
            List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(asv.getCurId()));
            String inStr = getSQLIn(result, 1000, "acur.cur_id");
            sql.append(" and " + inStr);
        }
        //账户属性
        if (!EmptyUtils.isBlank(asv.getAttributeId())) {
            List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(asv.getAttributeId()));
            String inStr = getSQLIn(result, 1000, "acc.attribute_id");
            sql.append(" and " + inStr);
        }
        //账户性质
        if (!EmptyUtils.isBlank(asv.getNatureId())) {
            List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(asv.getNatureId()));
            String inStr = getSQLIn(result, 1000, "acc.nature_id");
            sql.append(" and " + inStr);
        }
        //账户类别
        if (!EmptyUtils.isBlank(asv.getAccType())) {
            List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(asv.getAccType()));
            String inStr = getSQLIn(result, 1000, "acc.acc_type");
            sql.append(" and " + inStr);
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(" and " + joinSql);
        }
        List<AllocationSettlementVO> lists = listBySQLAliasToBean(sql.toString(), params, AllocationSettlementVO.class);
        List<AllocationSettlementVO> list = new ArrayList<>();
        int length = 0;
        if (asv.getStart() + asv.getLength() <= lists.size()) {
            length = asv.getStart() + asv.getLength();
        } else {
            length = lists.size();
        }
        for (int i = asv.getStart(); i < length; i++) {
            list.add(lists.get(i));
        }
//        List<AllocationSettlementVO> list = listBySQL(sql.toString(), asv.getStart(), asv.getLength(), params);
//        list = BeanUtils.copyMapListProperties(list, AllocationSettlementVO.class);
//        ////inject(list);
        List<AllocationSettlementVO> listOver = this.dealData(lists, list, asv.getStart(), asv.getLength());
        long count;
        if (lists.size() > 0) {
            count = (long) lists.size();
        } else {
            count = 0L;
        }
        return new DataTablesOutput(asv.getDraw(), listOver, count, count);
    }

    /**
     * 通过开户行查询银行账户明细
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataByBankName(AllocationSettlementVO asv, String joinSql) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", DateUtils.formatDate(asv.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
        params.put("beginDates", DateUtils.formatDate(asv.getBeginDate()));
        params.put("endDate", DateUtils.formatDate(asv.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
        params.put("endDates", DateUtils.formatDate(asv.getEndDate()));
        sql.append("select acc.bank_Name as bankName,acc.bank_code as bankCode,acur.cur_Id as curId,bbt.type_name as bankType ");
        sql.append(" from bt_Bank_acc acc");
        sql.append(" LEFT JOIN bt_bank_acc_cur acur ON acc.id = acur.bank_acc_id");
        sql.append(" LEFT JOIN bt_bank_type bbt ON acc.bank_type_Id = bbt.id ");
        sql.append(" where acc.status <> -2 and acc.valid_sign = 1 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(" and ");
            sql.append(joinSql);
        }
        if (!EmptyUtils.isBlank(asv.getBankName())) {
            sql.append(" and acc.bank_Name like :bankName");
            params.put("bankName", "%" + asv.getBankName() + "%");
        }
        if (!EmptyUtils.isBlank(asv.getCurId())) {
            List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(asv.getCurId()));
            String inStr = getSQLIn(result, 1000, "acur.cur_Id");
            sql.append(" and " + inStr);
        }
        sql.append(" group by acc.bank_name ,acc.bank_code,acur.cur_id,bbt.type_name");
        if (StringUtils.isNotEmpty(asv.getOrderPara()) && StringUtils.isNotEmpty(asv.getOrderByPara())) {
            sql.append(" order by " + asv.getOrderPara() + " " + asv.getOrderByPara());
        } else {
            sql.append(" order by acc.bank_name,bbt.type_name desc");
        }
        List<AllocationSettlementVO> lists = listBySQLAliasToBean(sql.toString(), params, AllocationSettlementVO.class);
        //上面注释部分逻辑在存储单独执行
        lists = calculateGetMoneyAndNumbersAndCdSignByBankName(params, lists);
        lists = calculateStartMoneyAndEndMoneyByBankName(params, lists);
        List<AllocationSettlementVO> list = new ArrayList<>();
        int length = 0;
        if (asv.getStart() + asv.getLength() <= lists.size()) {
            length = asv.getStart() + asv.getLength();
        } else {
            length = lists.size();
        }
        for (int i = asv.getStart(); i < length; i++) {
            list.add(lists.get(i));
        }
        List<AllocationSettlementVO> listOver = this.dealData(lists, list, asv.getStart(), asv.getLength());
        long count;
        if (lists.size() > 0) {
            count = (long) lists.size();
        } else {
            count = 0L;
        }
        return new DataTablesOutput(asv.getDraw(), listOver, count, count);
    }

    private List<AllocationSettlementVO> calculateStartMoneyAndEndMoneyByBankName(Map<String, Object> params, List<AllocationSettlementVO> lists) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT '0' as beginAndEndFlag ,bt_nvl_num(sum( b.bal ) ,0) as money, bc.bank_name as bankName,b.cur_id as curId");
        sql.append(" FROM bis_acc_bal b LEFT join bt_bank_acc bc ON b.bank_acc = bc.bank_acc ");
        sql.append(" WHERE b.bal_date = ( SELECT  bal_date  FROM (select b.CORP_ID,b.CUR_ID,b.BANK_ACC,max(b.BAL_DATE) as BAL_DATE from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 and b.bal_date  < bt_to_date(:beginDates,'yyyy-MM-dd') group by b.corp_id,b.cur_id,b.BANK_ACC) bal WHERE   bal.bank_acc=b.bank_acc and bal.cur_id=b.cur_id and bal.corp_id = b.corp_id ) ");
        sql.append(" GROUP BY bc.bank_name,b.cur_id ");

        sql.append(" UNION ALL  ");

        sql.append(" SELECT '1' as beginAndEndFlag ,bt_nvl_num(sum( b.bal ) ,0) as money, bc.bank_name as bankName,b.cur_id as curId");
        sql.append(" FROM bis_acc_bal b LEFT join bt_bank_acc bc ON b.bank_acc = bc.bank_acc ");
        sql.append(" WHERE b.bal_date = ( SELECT  bal_date  FROM (select b.CORP_ID,b.CUR_ID,b.BANK_ACC,max(b.BAL_DATE) as BAL_DATE from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 and b.bal_date  < bt_to_date(:endDates,'yyyy-MM-dd') group by b.corp_id,b.cur_id,b.BANK_ACC) bal WHERE   bal.bank_acc=b.bank_acc and bal.cur_id=b.cur_id and bal.corp_id = b.corp_id ) ");
        sql.append(" GROUP BY bc.bank_name,b.cur_id ");


        List<CalculateGetMoneyVO> getMoneyVOList = listBySQLAliasToBean(sql.toString(), params, CalculateGetMoneyVO.class);
        Map<String, CalculateGetMoneyVO> moneyVOMap = getMoneyVOList.stream().collect(Collectors.toMap(item -> {
            return item.getBankName() + "===" + item.getCurId() + "===" + item.getBeginAndEndFlag();
        }, Function.identity()));

        for (AllocationSettlementVO vo : lists) {
            vo.setStartMoney(BigDecimal.ZERO);
            vo.setEndMoney(BigDecimal.ZERO);
            String keyBegin = vo.getBankName() + "===" + vo.getCurId() + "===" + CalculateGetMoneyVO.BEGINANDENDFLAG_BEGIN;
            String keyEnd = vo.getBankName() + "===" + vo.getCurId() + "===" + CalculateGetMoneyVO.BEGINANDENDFLAG_END;
            if (moneyVOMap.containsKey(keyBegin)) {
                vo.setStartMoney(moneyVOMap.get(keyBegin).getMoney());
            }
            if (moneyVOMap.containsKey(keyEnd)) {
                vo.setEndMoney(moneyVOMap.get(keyEnd).getMoney());
            }
        }
        return lists;
    }

    private List<AllocationSettlementVO> calculateGetMoneyAndNumbersAndCdSignByBankName(Map<String, Object> params, List<AllocationSettlementVO> lists) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select '0' as cdSign, count(*) as numbers,sum(d.amt) as money,d.cur_id as curId,d.bank_name as bankName");
        sql.append(" from bis_acc_dtl d where d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) and d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" and d.cd_sign = '0' group by d.cur_id,d.bank_name  ");
        sql.append(" UNION ALL  ");
        sql.append(" select '1' as cdSign,  count(*) as numbers,sum(d.amt) as money,d.cur_id as curId,d.bank_name as bankName");
        sql.append(" from bis_acc_dtl d where d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) and d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" and d.cd_sign = '1' group by d.cur_id,d.bank_name  ");

        List<CalculateGetMoneyVO> getMoneyVOList = listBySQLAliasToBean(sql.toString(), params, CalculateGetMoneyVO.class);
        Map<String, CalculateGetMoneyVO> moneyVOMap = getMoneyVOList.stream().collect(Collectors.toMap(item -> {
            return item.getBankName() + "===" + item.getCurId() + "===" + item.getCdSign();
        }, Function.identity()));

        for (AllocationSettlementVO vo : lists) {
            //先给默认值
            vo.setGetMoney(BigDecimal.ZERO);
            vo.setGetNumbers(0);
            vo.setBorrowMoney(BigDecimal.ZERO);
            vo.setBorrowNumbers(0);

            String keySignIn = vo.getBankName() + "===" + vo.getCurId() + "===" + BisAccBalConstant.CDSIGN_IN;
            String keySignOut = vo.getBankName() + "===" + vo.getCurId() + "===" + BisAccBalConstant.CDSIGN_OUT;
            //借方
            if (moneyVOMap.containsKey(keySignIn)) {
                CalculateGetMoneyVO cVO = moneyVOMap.get(keySignIn);
                vo.setGetMoney(cVO.getMoney());
                vo.setGetNumbers(cVO.getNumbers());

            }
            //贷方
            if (moneyVOMap.containsKey(keySignOut)) {
                CalculateGetMoneyVO cVO = moneyVOMap.get(keySignOut);
                vo.setBorrowMoney(cVO.getMoney());
                vo.setBorrowNumbers(cVO.getNumbers());
            }
        }
        return lists;
    }

    /**
     * 通过银行类型查询银行账户明细
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataByBankType(AllocationSettlementVO asv, String joinSql) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", DateUtils.formatDate(asv.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
        params.put("beginDates", DateUtils.formatDate(asv.getBeginDate()));
        params.put("endDate", DateUtils.formatDate(asv.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
        params.put("endDates", DateUtils.formatDate(asv.getEndDate()));
        sql.append("select acur.cur_Id as curId,bbt.type_name as bankType,acc.bank_type_Id as bankTypeId ");
        sql.append(" from bt_Bank_acc acc");
        sql.append(" LEFT JOIN bt_bank_acc_cur acur ON acc.id = acur.bank_acc_id");
        sql.append(" LEFT JOIN bt_bank_type bbt ON acc.bank_type_Id = bbt.id ");
        sql.append(" where acc.status <> -2 and acc.valid_sign = 1 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(" and ");
            sql.append(joinSql);
        }
        if (!EmptyUtils.isBlank(asv.getBankType())) {
            List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(asv.getBankType()));
            String inStr = getSQLIn(result, 1000, "bbt.type_name");
            sql.append(" and " + inStr);
        }
        if (!EmptyUtils.isBlank(asv.getCurId())) {
            List<?> result = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(String.valueOf(asv.getCurId()));
            String inStr = getSQLIn(result, 1000, "acur.cur_Id");
            sql.append(" and " + inStr);
        }
        sql.append(" group by acur.cur_Id ,bbt.type_name,acc.bank_type_id");
        if (StringUtils.isNotEmpty(asv.getOrderPara()) && StringUtils.isNotEmpty(asv.getOrderByPara())) {
            sql.append(" order by " + asv.getOrderPara() + " " + asv.getOrderByPara());
        } else {
            sql.append(" order by acur.cur_Id desc ,bbt.type_name desc");
        }
        List<AllocationSettlementVO> lists = listBySQLAliasToBean(sql.toString(), params, AllocationSettlementVO.class);
        //上面注释部分逻辑在存储单独执行
        lists = calculateGetMoneyAndNumbersAndCdSign(params, lists);
        lists = calculateStartMoneyAndEndMoney(params, lists);
        List<AllocationSettlementVO> list = new ArrayList<>();
        int length = 0;
        if (asv.getStart() + asv.getLength() <= lists.size()) {
            length = asv.getStart() + asv.getLength();
        } else {
            length = lists.size();
        }
        for (int i = asv.getStart(); i < length; i++) {
            list.add(lists.get(i));
        }
        List<AllocationSettlementVO> listOver = this.dealData(lists, list, asv.getStart(), asv.getLength());
        long count;
        if (lists.size() > 0) {
            count = (long) lists.size();
        } else {
            count = 0L;
        }
        return new DataTablesOutput(asv.getDraw(), listOver, count, count);
    }

    private List<AllocationSettlementVO> calculateStartMoneyAndEndMoney(Map<String, Object> params, List<AllocationSettlementVO> lists) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select '0' as beginAndEndFlag , bt_nvl_num(sum(b.bal) , 0) as money, bc.BANK_TYPE_ID as bankTypeId, b.cur_id as curId ");
        sql.append(" from bis_acc_bal b left join bt_bank_acc bc on	b.bank_acc = bc.bank_acc left join (select b.CORP_ID, b.CUR_ID, b.BANK_ACC, max(b.BAL_DATE) as BAL_DATE from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 and b.bal_date < bt_to_date(:beginDates,'yyyy-MM-dd') group by b.corp_id, b.cur_id, b.BANK_ACC) bal on bal.bank_acc = b.bank_acc and bal.cur_id = b.cur_id and bal.corp_id = b.corp_id ");
        sql.append(" where b.bal_date = bal.BAL_DATE group by bc.BANK_TYPE_ID, b.cur_id ");
        sql.append(" union all ");
        sql.append(" select '1' as beginAndEndFlag , bt_nvl_num(sum(b.bal) , 0) as money, bc.BANK_TYPE_ID as bankTypeId, b.cur_id as curId ");
        sql.append(" from bis_acc_bal b left join bt_bank_acc bc on b.bank_acc = bc.bank_acc left join (select b.CORP_ID, b.CUR_ID, b.BANK_ACC, max(b.BAL_DATE) as BAL_DATE from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc where acc.status <> -2 and acc.valid_sign = 1 and b.bal_date < bt_to_date(:endDates,'yyyy-MM-dd') group by b.corp_id, b.cur_id, b.BANK_ACC) bal on bal.bank_acc = b.bank_acc and bal.cur_id = b.cur_id and bal.corp_id = b.corp_id ");
        sql.append(" where b.bal_date = bal.BAL_DATE group by bc.BANK_TYPE_ID, b.cur_id ");

        List<CalculateGetMoneyVO> getMoneyVOList = listBySQLAliasToBean(sql.toString(), params, CalculateGetMoneyVO.class);
        Map<String, CalculateGetMoneyVO> moneyVOMap = getMoneyVOList.stream().collect(Collectors.toMap(item -> {
            return item.getBankTypeId() + "===" + item.getCurId() + "===" + item.getBeginAndEndFlag();
        }, Function.identity()));

        for (AllocationSettlementVO vo : lists) {
            vo.setStartMoney(BigDecimal.ZERO);
            vo.setEndMoney(BigDecimal.ZERO);
            String keyBegin = vo.getBankTypeId() + "===" + vo.getCurId() + "===" + CalculateGetMoneyVO.BEGINANDENDFLAG_BEGIN;
            String keyEnd = vo.getBankTypeId() + "===" + vo.getCurId() + "===" + CalculateGetMoneyVO.BEGINANDENDFLAG_END;
            if (moneyVOMap.containsKey(keyBegin)) {
                vo.setStartMoney(moneyVOMap.get(keyBegin).getMoney());
            }
            if (moneyVOMap.containsKey(keyEnd)) {
                vo.setEndMoney(moneyVOMap.get(keyEnd).getMoney());
            }
        }
        return lists;
    }

    private List<AllocationSettlementVO> calculateGetMoneyAndNumbersAndCdSign(Map<String, Object> params, List<AllocationSettlementVO> lists) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select '0' as cdSign, count(*) as numbers,sum(d.amt) as money,d.cur_id as curId,bc.BANK_TYPE_ID as bankTypeId");
        sql.append(" from bis_acc_dtl d left join bt_bank_acc bc on d.bank_acc = bc.bank_acc ");
        sql.append("  where d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) and d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" and d.cd_sign = '0' group by d.cur_id,bc.BANK_TYPE_ID  ");
        sql.append(" UNION ALL  ");
        sql.append(" select '1' as cdSign,  count(*) as numbers,sum(d.amt) as money,d.cur_id as curId,bc.BANK_TYPE_ID as bankTypeId");
        sql.append(" from bis_acc_dtl d left join bt_bank_acc bc on d.bank_acc = bc.bank_acc ");
        sql.append("  where d.trans_time >= bt_to_date ( :beginDate, 'YYYY-MM-DD HH24:MI:SS' ) and d.trans_time <= bt_to_date ( :endDate, 'YYYY-MM-DD HH24:MI:SS' ) ");
        sql.append(" and d.cd_sign = '1' group by d.cur_id,bc.BANK_TYPE_ID  ");

        List<CalculateGetMoneyVO> getMoneyVOList = listBySQLAliasToBean(sql.toString(), params, CalculateGetMoneyVO.class);
        Map<String, CalculateGetMoneyVO> moneyVOMap = getMoneyVOList.stream().collect(Collectors.toMap(item -> {
            return item.getBankTypeId() + "===" + item.getCurId() + "===" + item.getCdSign();
        }, Function.identity()));

        for (AllocationSettlementVO vo : lists) {
            //先给默认值
            vo.setGetMoney(BigDecimal.ZERO);
            vo.setGetNumbers(0);
            vo.setBorrowMoney(BigDecimal.ZERO);
            vo.setBorrowNumbers(0);

            String keySignIn = vo.getBankTypeId() + "===" + vo.getCurId() + "===" + BisAccBalConstant.CDSIGN_IN;
            String keySignOut = vo.getBankTypeId() + "===" + vo.getCurId() + "===" + BisAccBalConstant.CDSIGN_OUT;
            //借方
            if (moneyVOMap.containsKey(keySignIn)) {
                CalculateGetMoneyVO cVO = moneyVOMap.get(keySignIn);
                vo.setGetMoney(cVO.getMoney());
                vo.setGetNumbers(cVO.getNumbers());

            }
            //贷方
            if (moneyVOMap.containsKey(keySignOut)) {
                CalculateGetMoneyVO cVO = moneyVOMap.get(keySignOut);
                vo.setBorrowMoney(cVO.getMoney());
                vo.setBorrowNumbers(cVO.getNumbers());
            }
        }
        return lists;
    }


    /**
     * 按照计算要求处理数据
     *
     * @param lists
     * @param list
     * @return
     */
    private List<AllocationSettlementVO> dealData(List<AllocationSettlementVO> lists, List<AllocationSettlementVO> list, Integer start, Integer length) {
        List<AllocationSettlementVO> listOver = new ArrayList<>();//最后数据处理完输出的集合
        List<AllocationSettlementVO> listRate = new ArrayList<>();//存储计算误差率而处理数据的集合
        List<Integer> listlocal = new ArrayList<>();//存储期末余额为正数时的下标位置的集合
        BigDecimal sumEndMoney = BigDecimal.ZERO;//期末余额总和
        BigDecimal errorRate = BigDecimal.ZERO;//百分比误差
        BigDecimal sumRate = BigDecimal.ZERO;//总百分比
        for (AllocationSettlementVO vo : lists) {
            if (!StringUtils.isNotEmpty(vo.getGetMoney())) {
                vo.setGetMoney(BigDecimal.ZERO);
            }
            if (!StringUtils.isNotEmpty(vo.getBorrowMoney())) {
                vo.setBorrowMoney(BigDecimal.ZERO);
            }
            if (StringUtils.isNotEmpty(vo.getStartMoney())) {
                vo.setEndMoney(vo.getStartMoney().add(vo.getGetMoney()).subtract(vo.getBorrowMoney()));
            } else {
                vo.setEndMoney(vo.getGetMoney().subtract(vo.getBorrowMoney()));
            }
            sumEndMoney = sumEndMoney.add(vo.getEndMoney()); //求总的期末余额
            listRate.add(vo);
        }
        //先求总的百分比，然后求总的误差率（由四舍五入引起）
        int i = 0;
        for (AllocationSettlementVO vo : listRate) {
            if (vo.getEndMoney().signum() == 1) {
                listlocal.add(i);//取出所有期末余额为正的位置下标
            }
            if (sumEndMoney.signum() != 0) {
                sumRate = sumRate.add(vo.getEndMoney().multiply(new BigDecimal(100)).divide(sumEndMoney, 2, BigDecimal.ROUND_HALF_UP));//求总的百分比
            } else {
                sumRate = BigDecimal.ZERO;
            }
            if (i == (listRate.size() - 1)) {
                errorRate = new BigDecimal(100).subtract(sumRate);//求误差
            }
            i++;
        }
        //按规则计算期初期末余额，当期初期末余额为负数时输出“--”,所以最后期初期末余额输出为负数，datatable可以数算数字字符串，并默认“--”为0
        //startMoneyStr为期初余额字符串，endMoney为期末余额字符串
        int n = 0;
        for (AllocationSettlementVO vo : list) {
            AllocationSettlementVO copyVo = new AllocationSettlementVO();//临时类，只用来计算
            //设置借款贷款为空时数值为0，防止Bigdecimal运算时有空值报错
            BeanUtils.copyProperties(vo, copyVo);
            if (!StringUtils.isNotEmpty(copyVo.getGetMoney())) {
                copyVo.setGetMoney(BigDecimal.ZERO);
            }
            if (!StringUtils.isNotEmpty(copyVo.getBorrowMoney())) {
                copyVo.setBorrowMoney(BigDecimal.ZERO);
            }
            //计算期末余额
            if (StringUtils.isNotEmpty(vo.getStartMoney())) {
                vo.setEndMoney(vo.getStartMoney().add(copyVo.getGetMoney()).subtract(copyVo.getBorrowMoney()));
                vo.setEndMoneyStr((vo.getStartMoney().add(copyVo.getGetMoney()).subtract(copyVo.getBorrowMoney())).toString());
            } else {
                vo.setEndMoney(copyVo.getGetMoney().subtract(copyVo.getBorrowMoney()));
                vo.setEndMoneyStr((copyVo.getGetMoney().subtract(copyVo.getBorrowMoney())).toString());
            }
            vo.setStartMoneyStr(vo.getStartMoney().toString());
            vo.setEndMoneyStr(vo.getEndMoney().toString());
            //设置最后一项期末余额不为负数和0的占比为自身比率+误差率，从而确保最终百分比之和为100%，其余为负数或0的占比为0，正数比率为自身比率
            if (sumEndMoney.signum() != 0) {
                if (EmptyUtils.isNotEmpty(listlocal) && Collections.max(listlocal) == (start * length + n)) {
                    vo.setRate(vo.getEndMoney().multiply(new BigDecimal(100)).divide(sumEndMoney, 2, BigDecimal.ROUND_HALF_UP).add(errorRate));
                } else {
                    vo.setRate(vo.getEndMoney().multiply(new BigDecimal(100)).divide(sumEndMoney, 2, BigDecimal.ROUND_HALF_UP));
                }
            } else {
                vo.setRate(BigDecimal.ZERO);
            }
            //默认为期末余额为负数时占比为0
            if (vo.getRate() == null || vo.getRate().compareTo(BigDecimal.ZERO) < 0) {
                vo.setRate(BigDecimal.ZERO);
            }
            listOver.add(vo);
            n++;
        }
        return listOver;
    }

    /**
     * 根据条件查询收支明细详情
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataDtlByType(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select d.trans_time as transTime, acc.corp_id as corpId, d.bank_acc as bankAcc, d.acc_name as accName,");
        sql.append(" d.bank_name as bankName, acc.attribute_id as attributeId, b.attribute_name as attributeName, d.cd_sign as cdSign,");
        sql.append(" d.cur_id as curId, d.amt as amt, d.bal as bal, d.opp_acc_no as oppAccNo, d.opp_acc_name as oppAccName, d.opp_acc_bank as oppAccBank,");
        sql.append(" d.remark as remark, d.project_remark as projectRemark,acc.bank_type_id as bankTypeId, bt.type_name as typeName ");
        sql.append(" from bis_acc_dtl d left join bt_bank_acc acc on d.bank_acc = acc.bank_acc ");
        sql.append(" left join bis_acc_attribute b on acc.attribute_id = b.id ");
        sql.append(" left join bt_bank_type bt on acc.bank_type_id = bt.id ");
        sql.append(" where acc.status <> -2 and acc.valid_sign = 1 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(" and ");
            sql.append(joinSql);
        }
        //申请日期
        String gedBeginDate = (String) dataTablesInput.getFormdata().get("ged_beginDate");
        String ledEndDate = (String) dataTablesInput.getFormdata().get("led_endDate");
        if (StringUtils.isNotEmpty(gedBeginDate)) {
            gedBeginDate = gedBeginDate + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_beginDate", gedBeginDate);
        }
        if (StringUtils.isNotEmpty(ledEndDate)) {
            ledEndDate = ledEndDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_endDate", ledEndDate);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AllocationSettlementDtlVO.class, "d", false);
    }
}
