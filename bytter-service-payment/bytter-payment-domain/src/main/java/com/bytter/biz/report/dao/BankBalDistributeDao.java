package com.bytter.biz.report.dao;
import com.bytter.biz.report.vo.BankBalDistributeVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author shenc
 * @Description 银行余额统计报表
 * @date 2018.12.14
 */
@Repository
public class BankBalDistributeDao extends BaseDao<BankBalDistributeVo,String> {
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        String corpIds = (String) input.getFormdata().get("ins_corpId");
        String curId = (String) input.getFormdata().get("eqs_curId");
        String ledRegDate = (String) input.getFormdata().get("led_regDate");
        try {
            Class<BankBalDistributeVo> voClazz = BankBalDistributeVo.class;
            sql.append(" select bankbal.corp_id as corpId, ");
            sql.append("        bankbal.bank_type_id as bankTypeId, ");
            sql.append("        t.type_name as bankTypeName, ");
            sql.append("        bankbal.acc_type as accType, ");
            sql.append("        bankbal.nature_id as natureId, ");
            sql.append("        n.nature_name as natureName, ");
            sql.append("        bankbal.cur_id as curId, ");
            sql.append("        sum(balMoney) as balMoney ");
            sql.append("   from (select b.bank_acc, ");
            sql.append("                         b.corp_id, ");
            sql.append("                         b.bank_type_id, ");
            sql.append("                         c.cur_id, ");
            sql.append("                         b.acc_type, ");
            sql.append("                         b.nature_id, ");
            sql.append("                         (select a.avail_bal ");
            sql.append("                            from bis_acc_bal a, ");
            sql.append("                                 (select bank_acc, max(bal_date) maxdate ");
            sql.append("                                    from bis_acc_bal ab ");
            sql.append("                                   where 1 = 1 ");
            //过滤加入查询时间
            if (!EmptyUtils.isBlank(ledRegDate)){
                sql.append(" and bal_date <= :leregDate  ");
                params.put("leregDate",ledRegDate.trim());
            }
            //加入单位过滤条件
            if (!EmptyUtils.isBlank(corpIds.trim())) {
                sql.append("and " + SqlUtil.getInSql(corpIds.trim().trim().split(","), "ab.corp_id", true));
            }
            //加入币别过滤条件
            if (!EmptyUtils.isBlank(curId.trim())) {
                sql.append(" and ab.cur_id = '" + curId + "' ");
            }
            sql.append(" and (ab.status >= '95' or ab.status is null) ");
            sql.append(" group by bank_acc) abc ");
            sql.append("                           where a.bank_acc = abc.bank_acc ");
            sql.append("                             and a.bal_date = abc.maxdate ");
            sql.append("                             and a.cur_id = c.cur_id ");
            sql.append("                             and a.bank_acc = b.bank_acc ");
            sql.append("                             and (a.status is null or a.status >= 95)) as balMoney ");
            sql.append("           from bt_bank_acc b ");
            sql.append("          inner join bt_bank_acc_cur c ");
            sql.append("             on c.bank_acc_id = b.id ");
            sql.append("          where 1 = 1 ");
            sql.append("            and b.status <> '-2' ");
            sql.append("            and b.valid_sign = '1' ");
            //加入币别过滤
            sql.append(getWhereAllCondition(input, voClazz.newInstance(), "", false, null));
            sql.append(" ) bankbal ");
            sql.append("  left join bt_bank_type t on t.id = bankbal.bank_type_id ");
            sql.append("   left join bt_acc_nature n on n.id = bankbal.nature_id ");
            sql.append("  where bankbal.balMoney is not null group by bankbal.corp_id, bankbal.bank_type_id,t.type_name,bankbal.acc_type,bankbal.nature_id,n.nature_name,bankbal.cur_id ");
            Map<String, Object> values = input.getSearchValues();
            String countSQL = prepareCountSQL(sql.toString());
            BigDecimal count = (BigDecimal) createSQLQuery(countSQL, values).uniqueResult();
            List list;
            if (count.compareTo(BigDecimal.ZERO) > 0) {
                list = listBySQL(sql.toString(), input.getStart(), input.getLength(), values);
                list = BeanUtils.copyMapListProperties(list, voClazz);
                if (null != voClazz) {
                    ////inject(list);
                }
            } else {
                list = new ArrayList(0);
            }
            return new DataTablesOutput<BankBalDistributeVo>(input.getDraw(), list, count.intValue(), count.intValue());
        }catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(e);
        }
    }
    public List<BankBalDistributeVo> getBankBal(BankBalDistributeVo vo) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        sql.append(" select bankbal.bank_type_id as bankTypeId, ");
        sql.append("        t.type_name as bankTypeName, ");
        sql.append("        sum(balMoney) as balMoney ");
        sql.append("   from (select b.bank_acc, ");
        sql.append("                         b.bank_type_id, ");
        sql.append("                         (select a.avail_bal ");
        sql.append("                            from bis_acc_bal a, ");
        sql.append("                                 (select bank_acc, max(bal_date) maxdate ");
        sql.append("                                    from bis_acc_bal ab ");
        sql.append("                                   where 1 = 1 ");
        //过滤单位
        if (!StringUtils.isBlank(vo.getCorpId().trim())) {
            sql.append("and " + SqlUtil.getInSql(vo.getCorpId().trim().trim().split(","), "ab.corp_id", true));
        }
        //过滤币别
        if (!StringUtils.isBlank(vo.getCurId().trim())) {
            sql.append(" and ab.cur_id = '" + vo.getCurId().trim() + "' ");
        }
        //过滤加入查询时间
        if (null != vo.getEndDate()){
            sql.append(" and bal_date <= :endDate  ");
            params.put("endDate",vo.getEndDate());
        }
        sql.append(" and (ab.status >= '95' or ab.status is null) ");
        sql.append(" group by bank_acc) abc ");
        sql.append(" where a.bank_acc = abc.bank_acc ");
        sql.append("                             and a.bal_date = abc.maxdate ");
        sql.append("                             and a.cur_id = c.cur_id ");
        sql.append("                             and a.bank_acc = b.bank_acc ");
        sql.append("                             and (a.status is null or a.status >= 95)) as balMoney ");
        sql.append("           from bt_bank_acc b ");
        sql.append("          inner join bt_bank_acc_cur c ");
        sql.append("             on c.bank_acc_id = b.id ");
        sql.append("          where 1 = 1 ");
        sql.append("            and b.status <> '-2' ");
        sql.append("            and b.valid_sign = '1' ");
        //过滤单位
        if (!StringUtils.isBlank(vo.getCorpId().trim())) {
            sql.append("and " + SqlUtil.getInSql(vo.getCorpId().trim().trim().split(","), "b.corp_id", true));
        }
        //过滤加入银行类别
        if (null != vo.getBankTypeId() && !StringUtils.isBlank(vo.getBankTypeId().trim())){
            sql.append("and "+SqlUtil.getInSql(vo.getBankTypeId().trim().split(","),"b.bank_type_id",true));
        }
        //过滤币别
        if (!StringUtils.isBlank(vo.getCurId().trim())) {
            sql.append(" and c.cur_id = '" + vo.getCurId().trim() + "' ");
        }
        //过滤加入账户性质
        if (null != vo.getNatureId() && !StringUtils.isBlank(vo.getNatureId().trim())){
            sql.append(" and b.nature_id = :natureId  ");
            params.put("natureId",vo.getNatureId().trim());
        }
        //过滤加入账户类别
        if (null != vo.getAccType() && !StringUtils.isBlank(vo.getAccType().trim())){
            sql.append(" and b.acc_type = :accType  ");
            params.put("accType",vo.getAccType().trim());
        }
        sql.append(" ) bankbal ");
        sql.append(" left join bt_bank_type t on t.id = bankbal.bank_type_id ");
        sql.append("  where bankbal.balMoney is not null ");
        sql.append("  group by bankbal.bank_type_id, t.type_name ");
        sql.append("  order by balMoney desc ");
        return this.listBySQLAliasToBean(sql.toString(),params,BankBalDistributeVo.class);
    }
}
