package com.bytter.biz.netbank.dao;

import com.bytter.biz.netbank.entity.BisBankExecutionStrategy;
import com.bytter.biz.netbank.vo.BisBankExecutionStrategyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
/**
 * 三单关联执行策略
 */
@Repository
public class BisBankExecutionStrategyDao extends BaseDao<BisBankExecutionStrategy, String> {
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisBankExecutionStrategyVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.bank_type_id as bankTypeId,b.type as type,b.is_enable as isEnable,");
        sql.append(" b.IS_EXIST_ID as isExistId,b.rules as rules,b.rules_name as rulesName,b.bill_only_id as billOnlyId,");
        sql.append(" b.dtl_only_id as dtlOnlyId,b.receipt_only_id as receiptOnlyId,execute_sort as executeSort, ");
        sql.append(" b.create_by AS createBy,b.create_date AS createDate,b.update_by AS updateBy,b.update_date AS updateDate ");
        sql.append(" from bis_bank_execution_strategy b where 1=1 and b.type <> '2'");
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisBankExecutionStrategyVO.class, "b", false);
    }
    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    @SuppressWarnings("rawtypes")
    public BisBankExecutionStrategyVO getById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.bank_type_id as bankTypeId,b.type as type,b.is_enable as isEnable,");
        sql.append(" b.IS_EXIST_ID as isExistId,b.rules as rules,b.rules_name as rulesName,b.bill_only_id as billOnlyId,");
        sql.append(" b.dtl_only_id as dtlOnlyId,b.receipt_only_id as receiptOnlyId,execute_sort as executeSort, ");
        sql.append(" b.create_by AS createBy,b.create_date AS createDate,b.update_by AS updateBy,b.update_date AS updateDate, bank.type_name as bankTypeName, ");
        sql.append(" b.trans_Days as transDays ");
        sql.append(" from bis_bank_execution_strategy b left join bt_bank_type bank on b.bank_type_id = bank.id where 1=1");
        sql.append(" and B.ID=:id ");
        Map<String,String> params = new HashMap<String,String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(),params,BisBankExecutionStrategyVO.class);
    }
    /**
     * 根据type查询数据总数
     *
     * @param type
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean CheckxecuteSort(String type,String executeSort,String bankTypeId,String rules,String isExistId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select COUNT(*) as count");
        sql.append(" from bis_bank_execution_strategy b left join bt_bank_type bank on b.bank_type_id = bank.id");
        sql.append(" where B.TYPE=:type and B.BANK_TYPE_ID = :bankTypeId and B.IS_EXIST_ID = :isExistId ");
        Map<String,Object> params = new HashMap<>();
        params.put("type", type);
        params.put("bankTypeId", bankTypeId);
        params.put("isExistId", isExistId);
        if(EmptyUtils.isNotEmpty(executeSort)){
            sql.append(" and B.EXECUTE_SORT=:executeSort ");
            params.put("executeSort", executeSort);
        }
        if(EmptyUtils.isNotEmpty(rules)){
            sql.append(" and B.rules=:rules ");
            params.put("rules", rules);
        }
        Map map=getBySQL(sql.toString(), params);
        if(!map.isEmpty()){
            Number count = (Number)map.get("COUNT");
            if(EmptyUtils.isEmpty(count)){
                count = (Number)map.get("count");
            }
            if(count!=null){
                BigDecimal temp = new BigDecimal(count.intValue());
                int i=temp.compareTo(BigDecimal.ZERO);
                if(i==0){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 根据type查询数据总数（修改）
     *
     * @param type
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean checkExecuteUpdateSort(String type,String executeSort,String id,String bankTypeId,String rules,String isExistId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select COUNT(*) as count");
        sql.append(" from bis_bank_execution_strategy b left join bt_bank_type bank on b.bank_type_id = bank.id");
        sql.append(" where B.TYPE=:type  and B.id  <> :id and B.BANK_TYPE_ID=:bankTypeId and B.IS_EXIST_ID = :isExistId ");
        Map<String,String> params = new HashMap<String,String>();
        params.put("type", type);
        params.put("id", id);
        params.put("bankTypeId", bankTypeId);
        params.put("isExistId", isExistId);
        if(EmptyUtils.isNotEmpty(executeSort)){
            sql.append(" and B.EXECUTE_SORT=:executeSort ");
            params.put("executeSort", executeSort);
        }
        if(EmptyUtils.isNotEmpty(rules)){
            sql.append(" and B.rules=:rules ");
            params.put("rules", rules);
        }
        Map map=getBySQL(sql.toString(), params);
        if(!map.isEmpty()){
            Number count = (Number)map.get("COUNT");
            if(EmptyUtils.isEmpty(count)){
                count = (Number)map.get("count");
            }
            if(count!=null){
                BigDecimal temp = new BigDecimal(count.intValue());
                int i=temp.compareTo(BigDecimal.ONE);
                if(i==-1){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisBankExecutionStrategyVO> getDataTablesUnionRefund(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.bank_type_id as bankTypeId,b.type as type,b.is_enable as isEnable,");
        sql.append(" b.IS_EXIST_ID as isExistId,b.rules as rules,b.rules_name as rulesName,b.bill_only_id as billOnlyId,");
        sql.append(" b.dtl_only_id as dtlOnlyId,b.receipt_only_id as receiptOnlyId,execute_sort as executeSort, ");
        sql.append(" b.create_by AS createBy,b.create_date AS createDate,b.update_by AS updateBy,b.update_date AS updateDate, bank.type_name as bankTypeName, ");
        sql.append(" b.trans_Days as transDays");
        sql.append(" from bis_bank_execution_strategy b left join bt_bank_type bank on b.bank_type_id = bank.id where 1=1 and b.type = '2' ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisBankExecutionStrategyVO.class, "b", false);
    }

}
