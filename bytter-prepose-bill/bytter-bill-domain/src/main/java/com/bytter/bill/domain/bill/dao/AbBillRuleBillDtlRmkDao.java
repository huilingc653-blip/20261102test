package com.bytter.bill.domain.bill.dao;

import com.bytter.bill.domain.bill.entity.AbBillRuleBillDtlRmk;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillDtlRmkDtlVO;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillDtlRmkVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.DateUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description 票据流水备注规则dao
* @author zhous
* @date 2025/2/25 8:48
*/
@Repository
public class AbBillRuleBillDtlRmkDao extends BaseDao<AbBillRuleBillDtlRmk, String> {


    /**
    * @Description 获取票据流水备注规则主表信息
    * @author zhous
    * @date 2025/2/25 8:44
    */
    public DataTablesOutput<AbBillRuleBillDtlRmkVO> getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        Map<String, Object> params = new HashMap<>();
        sql.append("id as Id,bank_type_id as bankTypeId,bank_type_name as bankTypeName,busType as busType,status ");
        sql.append(" from ab_bill_rulebilldtlrmk ");
        sql.append(" where 1=1 ");
        DataTablesOutput dataOutInfo = this.getDynamicBySQL(input, sql.toString(), AbBillRuleBillDtlRmkVO.class, "", false, params);
        return dataOutInfo;
    }

    /**
    * @Description 根据银行类型和业务类型获取数据
    * @author zhous
    * @date 2025/2/25 8:47
    */
    public List<AbBillRuleBillDtlRmkVO> getByRepeat(AbBillRuleBillDtlRmkVO abBillRuleBillDtlRmkVO,boolean isSave) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("id as Id,bank_type_id as bankTypeId,bank_type_name as bankTypeName,bustype as busType,status");
        sql.append(" from ab_bill_rulebilldtlrmk ");
        sql.append(" where 1=1 ");
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append(" AND bank_type_id = :bankTypeId");
        params.put("bankTypeId", abBillRuleBillDtlRmkVO.getBankTypeId());
        sql.append(" AND bustype = :busType");
        params.put("busType", abBillRuleBillDtlRmkVO.getBustype());
        sql.append(" AND status = :status");
        params.put("status", AccebillConstant.AB_BILL_VALID_SIGN_1);
        if(!isSave){
            sql.append(" AND id != :id");
            params.put("id", abBillRuleBillDtlRmkVO.getId());
        }
        List<AbBillRuleBillDtlRmkVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbBillRuleBillDtlRmkVO.class);
        return listInfo;
    }

    /**
    * @Description 禁用
    * @author zhous
    * @date 2025/2/25 8:47
    */
    public void disable(String ids, ICurrentUser user) {
        Map<String, Object> params = new HashMap<>();
        List<String> strings = Arrays.asList(ids.split(","));
        params.put("ids", strings);
        params.put("updateDate", DateUtils.getDate());
        params.put("updateBy", user.getLoginName());
        params.put("status", AccebillConstant.AB_BILL_VALID_SIGN_0);
        StringBuilder sql = new StringBuilder();
        sql.append("update ab_bill_rulebilldtlrmk set status =:status,update_date = :updateDate,update_by = :updateBy where id in (:ids)");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
    * @Description 根据银行类型和业务类型获取对应的规则明细
    * @author zhous
    * @date 2025/2/25 14:50
    */
    public List<AbBillRuleBillDtlRmkDtlVO> getRulesByBankTypeIdAndBusType(String bankTypeId, String busType) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" b.id as id,b.bus_id as busId,b.match_rule as matchRule,b.match_elements as matchElements,b.match_relations as matchRelations,b.match_content as matchContent,");
        sql.append(" b.sort as sort,b.left_parenthesis as leftParenthesis,b.right_parenthesis as rightParenthesis");
        sql.append(" from ab_bill_rulebilldtlrmk a ");
        sql.append(" left join ab_bill_rulebilldtlrmk_dtl b on a.id = b.bus_id ");
        sql.append(" where 1=1 and a.status = 1 ");
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append(" AND a.bank_type_id = :bankTypeId");
        params.put("bankTypeId", bankTypeId);
        sql.append(" AND a.bustype = :busType");
        params.put("busType", busType);
        sql.append(" order by b.SORT asc ");
        List<AbBillRuleBillDtlRmkDtlVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbBillRuleBillDtlRmkDtlVO.class);
        return listInfo;
    }

    /**
    * @Description 获取有效的票据流水备注规则
    * @author zhous
    * @date 2025/2/25 14:50
    */
    public List<AbBillRuleBillDtlRmkVO> listValidInfo() {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("id as Id,bank_type_id as bankTypeId,bank_type_name as bankTypeName,bustype as busType,status");
        sql.append(" from ab_bill_rulebilldtlrmk ");
        sql.append(" where 1=1 ");
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append(" AND status = :status");
        params.put("status", AccebillConstant.AB_BILL_VALID_SIGN_1);
        List<AbBillRuleBillDtlRmkVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbBillRuleBillDtlRmkVO.class);
        return listInfo;
    }
}
