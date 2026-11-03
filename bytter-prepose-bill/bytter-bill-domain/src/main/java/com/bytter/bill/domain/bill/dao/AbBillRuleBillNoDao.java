package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbBillRuleBillNo;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillNoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author wanggl
 * @version 1.0
 * @Title: Dao
 * @Description: 票号规则查询
 * @date 2022-03-03
 */
@Repository
public class AbBillRuleBillNoDao extends BaseDao<AbBillRuleBillNo, String> {
    /**
     * 获取有效的票号规则
     *
     * @return 票号规则列表
     */
    public List<AbBillRuleBillNo> listValidInfo() {
        return this.listBy("mark", "1");
    }
    /**
     * 获取查询的sql语句
     *
     * @param input        查询参数
     * @param corpAuthList 有权限单位
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> formData = input.getFormdata();
        sql.append("abr.id as Id,abr.bank_type_id as bankTypeId,abr.busType as busType,abr.bill_number_field as billNumField,");
        sql.append("abr.relation_matching as relation,abr.billno_separated as separated,abr.valid_mark as mark,");
        sql.append("abr.bank_type_name as bankTypeName");
        sql.append(" from AB_BILL_RULEBILLNO abr ");
        sql.append(" where 1=1 ");
        DataTablesOutput dataOutInfo = this.getDynamicBySQL(input, sql.toString(), AbBillRuleBillNoVO.class, "abr", false, params);
        return dataOutInfo;
    }
    public void delete(DataTablesInput input, String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("delete from AB_BILL_RULEBILLNO where id = : id");
        params.put("id",id);
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }
    public List<AbBillRuleBillNoVO> getByRepeat(AbBillRuleBillNoVO abBillRuleBillNoVO) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("abr.id as Id,abr.bank_type_id as bankTypeId,abr.busType as busType,abr.bill_number_field as billNumField,");
        sql.append("abr.relation_matching as relation,abr.billno_separated as separated,abr.valid_mark as mark,");
        sql.append("abr.bank_type_name as bankTypeName");
        sql.append(" from AB_BILL_RULEBILLNO abr ");
        sql.append(" where 1=1 ");
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append(" AND abr.bank_type_id = :bankTypeId");
        params.put("bankTypeId", abBillRuleBillNoVO.getBankTypeId());
        sql.append(" AND abr.busType = :busType");
        params.put("busType", abBillRuleBillNoVO.getBusType());
        List<AbBillRuleBillNoVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbBillRuleBillNoVO.class);
        return listInfo;
    }
}
