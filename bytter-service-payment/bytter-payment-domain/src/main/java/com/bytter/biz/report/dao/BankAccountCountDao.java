package com.bytter.biz.report.dao;
import com.bytter.biz.report.vo.BankAccountCountVO;
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
import java.util.*;
/**
 * @author shenc
 * @Description 银行账号数量统计表
 * @date 2018.12.1
 */
@Repository
public class BankAccountCountDao extends BaseDao<BankAccountCountVO, String> {
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        String status = (String) dataTablesInput.getFormdata().get("eqs_status");
        try {
            Class<BankAccountCountVO> voClazz = BankAccountCountVO.class;
            sql.append(" select t1.count      as count, ");
            sql.append(" t1.corpId     as corpId, ");
            sql.append(" t1.bankTypeId as bankTypeId, ");
            sql.append(" t1.accType    as accType, ");
            sql.append(" t1.natureId   as natureId, ");
            sql.append(" t1.curId   as curId, ");
            sql.append(" n.nature_name as natureName, ");
            sql.append(" p.type_name   as bankTypeName ");
            sql.append(" from (select count(*) as count, ");
            sql.append(" t.corpId as corpId, ");
            sql.append(" t.bankTypeId as bankTypeId, ");
            sql.append(" t.accType as accType, ");
            sql.append(" t.curId as curId, ");
            sql.append(" t.natureId as natureId ");
            sql.append(" from (select b.bank_acc as bankAcc, ");
            sql.append(" b.corp_id as corpId, ");
            sql.append(" b.bank_type_id as bankTypeId, ");
            sql.append(" b.acc_type as accType, ");
            sql.append(" c.cur_id as curId, ");
            sql.append(" b.nature_id as natureId ");
            sql.append(" from bt_bank_acc b ");
            sql.append(" inner join bt_bank_acc_cur c ");
            sql.append(" on c.bank_acc_id = b.id ");
            sql.append(" where 1 = 1 ");
            if (!StringUtils.isBlank(status)){
                if ("N".equalsIgnoreCase(status)){
                    sql.append(" and b.status <> '-2' ");
                }else{
                    sql.append(" and b.status = '-2' ");
                }
            }
            sql.append(getWhereAllCondition(dataTablesInput, voClazz.newInstance(), "", false, null));
            sql.append(" ) t ");
            sql.append(" group by t.corpId, t.bankTypeId, t.accType, t.natureId,t.curId) t1 ");
            sql.append(" left join bt_acc_nature n ");
            sql.append(" on n.id = t1.natureId ");
            sql.append(" left join bt_bank_type p ");
            sql.append(" on p.id = t1.bankTypeId ");
            sql.append(" left join sys_corp c ");
            sql.append(" on c.id = t1.corpId ");
            sql.append(" order by count desc,c.sort,c.name");
            Map<String, Object> values = dataTablesInput.getSearchValues();
            String countSQL = prepareCountSQL(sql.toString());
            BigDecimal count = (BigDecimal) createSQLQuery(countSQL, values).uniqueResult();
            List list;
            if (count.compareTo(BigDecimal.ZERO) > 0) {
                list = listBySQL(sql.toString(), dataTablesInput.getStart(), dataTablesInput.getLength(), values);
                list = BeanUtils.copyMapListProperties(list, voClazz);
                if (null != voClazz) {
                    ////inject(list);
                }
            } else {
                list = new ArrayList(0);
            }
            return new DataTablesOutput<BankAccountCountVO>(dataTablesInput.getDraw(), list, count.intValue(), count.intValue());
        }catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(e);
        }
    }
    /**
     * 根据银行账号类别获取银行账号数量
     * @param vo
     * @return
     */
    public List<BankAccountCountVO> getBankCountByBankType(BankAccountCountVO vo){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        sql.append("  select t1.count      as count,  ");
        sql.append("  t1.bankTypeId as bankTypeId,  ");
        sql.append("  p.type_name   as bankTypeName  ");
        sql.append("  from (select count(*) as count, t.bankTypeId as bankTypeId  ");
        sql.append("  from (select b.bank_acc as bankAcc,  ");
        sql.append("  b.corp_id as corpId,  ");
        sql.append("  b.bank_type_id as bankTypeId,  ");
        sql.append("  b.acc_type as accType,  ");
        sql.append("  c.cur_id as curId,  ");
        sql.append("  b.nature_id as natureId  ");
        sql.append("  from bt_bank_acc b  ");
        sql.append("  inner join bt_bank_acc_cur c  ");
        sql.append("  on c.bank_acc_id = b.id  ");
        sql.append("  where 1 = 1  ");
        if (!StringUtils.isBlank(vo.getStatus().trim())){
            if ("N".equalsIgnoreCase(vo.getStatus())){
                sql.append(" and b.status <> '-2' ");
            }else{
                sql.append(" and b.status = '-2' ");
            }
        }
        //过滤加入单位
        if (null !=vo.getCorpId() && !StringUtils.isBlank(vo.getCorpId().trim())){
            sql.append(" and "+SqlUtil.getInSql(vo.getCorpId().trim().split(","),"b.corp_id",true));
        }
        //过滤加入银行类别
        if (null != vo.getBankTypeId() && !StringUtils.isBlank(vo.getBankTypeId().trim())){
            sql.append(" and "+SqlUtil.getInSql(vo.getBankTypeId().trim().split(","),"b.bank_type_id",true));
        }
        //过滤加入账户性质
        if (null != vo.getNatureId() && !StringUtils.isBlank(vo.getNatureId().trim())){
            sql.append(" and b.nature_id = :natureId  ");
            params.put("natureId",vo.getNatureId().trim());
        }
        //过滤加入查询时间
        if (null != vo.getStartDate()){
            sql.append(" and b.reg_date >= :startDate  ");
            params.put("startDate",vo.getStartDate());
        }
        if (null != vo.getEndDate()){
            sql.append(" and b.reg_date <= :endDate  ");
            params.put("endDate",vo.getEndDate());
        }
        //过滤加入账户类别
        if (null != vo.getAccType() && !StringUtils.isBlank(vo.getAccType().trim())){
            sql.append(" and b.acc_type = :accType  ");
            params.put("accType",vo.getAccType().trim());
        }
        //过滤加入币别
        if (null != vo.getCurId() && !StringUtils.isBlank(vo.getCurId().trim())){
            sql.append(" and c.cur_id = :curId  ");
            params.put("curId",vo.getCurId().trim());
        }
        sql.append("  ) t  ");
        sql.append("  group by t.bankTypeId) t1  ");
        sql.append("  left join bt_bank_type p  ");
        sql.append("  on p.id = t1.bankTypeId  ");
        sql.append("  order by count desc, bankTypeName ");
        return this.listBySQLAliasToBean(sql.toString(),params,BankAccountCountVO.class);
    }
    /**
     * 根据银行账号性质分类获取银行账号数量
     * @param vo
     * @return
     */
    public List<BankAccountCountVO> getBankCountDataByNature(BankAccountCountVO vo){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        sql.append("  select t1.count      as count,  ");
        sql.append("  t1.natureId   as natureId,  ");
        sql.append("  n.nature_name as natureName  ");
        sql.append("  from (select count(*) as count, t.natureId as natureId  ");
        sql.append("  from (select b.bank_acc as bankAcc,  ");
        sql.append("  b.corp_id as corpId,  ");
        sql.append("  b.nature_id as natureId  ");
        sql.append("  from bt_bank_acc b  ");
        sql.append("  inner join bt_bank_acc_cur c  ");
        sql.append("  on c.bank_acc_id = b.id  ");
        sql.append("  where 1 = 1  ");
        if (!StringUtils.isBlank(vo.getStatus())){
            if ("N".equalsIgnoreCase(vo.getStatus())){
                sql.append(" and b.status <> '-2' ");
            }else{
                sql.append(" and b.status = '-2' ");
            }
        }
        //过滤加入单位
        if (null !=vo.getCorpId() && !StringUtils.isBlank(vo.getCorpId().trim())){
            sql.append(" and "+SqlUtil.getInSql(vo.getCorpId().trim().split(","),"b.corp_id",true));
        }
        //过滤加入银行类别
        if (null != vo.getBankTypeId() && !StringUtils.isBlank(vo.getBankTypeId().trim())){
            sql.append(" and "+SqlUtil.getInSql(vo.getBankTypeId().trim().split(","),"b.bank_type_id",true));
        }
        //过滤加入账户性质
        if (null != vo.getNatureId() && !StringUtils.isBlank(vo.getNatureId().trim())){
            sql.append(" and b.nature_id = :natureId  ");
            params.put("natureId",vo.getNatureId().trim());
        }
        //过滤加入查询时间
        if (null != vo.getStartDate()){
            sql.append(" and b.reg_date >= :startDate  ");
            params.put("startDate",vo.getStartDate());
        }
        if (null != vo.getEndDate()){
            sql.append(" and b.reg_date <= :endDate  ");
            params.put("endDate",vo.getEndDate());
        }
        //过滤加入账户类别
        if (null != vo.getAccType() && !StringUtils.isBlank(vo.getAccType().trim())){
            sql.append(" and b.acc_type = :accType  ");
            params.put("accType",vo.getAccType().trim());
        }
        //过滤加入币别
        if (null != vo.getCurId() && !StringUtils.isBlank(vo.getCurId().trim())){
            sql.append(" and c.cur_id = :curId  ");
            params.put("curId",vo.getCurId().trim());
        }
        sql.append("  ) t  ");
        sql.append("  group by t.natureId) t1  ");
        sql.append("  left join bt_acc_nature n  ");
        sql.append("  on n.id = t1.natureId  ");
        sql.append("  order by count desc  ");
        return this.listBySQLAliasToBean(sql.toString(),params,BankAccountCountVO.class);
    }
    /**
     * 获取指定类型的已注销的银行账号数量
     * @param vo
     * @param startDate
     * @param endDate
     * @return
     */
    public Integer getCancelBankCount(BankAccountCountVO vo, Date startDate, Date endDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        Integer num = 0;
        sql.append(" select count(*) as num ");
        sql.append("   from bt_bank_acc b ");
        sql.append("  inner join bt_bank_acc_cur c ");
        sql.append("     on c.bank_acc_id = b.id ");
        sql.append("  where b.corp_id = :corpId ");
        sql.append("    and b.bank_type_id = :bankTypeId ");
        sql.append("    and b.acc_type = :accType ");
        sql.append("    and b.nature_id = :natureId ");
        sql.append("    and c.cur_id = :curId ");
        if (!EmptyUtils.isEmpty(startDate)) {
            sql.append("    and b.reg_date >= :startDate ");
            params.put("startDate",startDate);
        }
        if (!EmptyUtils.isEmpty(endDate)) {
            sql.append("    and b.reg_date <= :endDate ");
            params.put("endDate",endDate);
        }
        sql.append("    and b.status = '-2' ");
        params.put("corpId",vo.getCorpId());
        params.put("bankTypeId",vo.getBankTypeId());
        params.put("accType",vo.getAccType());
        params.put("natureId",vo.getNatureId());
        params.put("curId",vo.getCurId());
        Map reInfo=this.getBySQL(sql.toString(), params);
        if(reInfo.containsKey("num")){
            num=Integer.parseInt(String.valueOf(reInfo.get("num")));
        }
        if(reInfo.containsKey("NUM")){
            num=Integer.parseInt(String.valueOf(reInfo.get("NUM")));
        }
        return num;
    }
}
