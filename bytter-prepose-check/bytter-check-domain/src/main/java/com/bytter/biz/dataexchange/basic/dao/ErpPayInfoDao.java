package com.bytter.biz.dataexchange.basic.dao;
import com.bytter.biz.dataexchange.basic.entity.ErpPayInfo;
import com.bytter.biz.dataexchange.basic.vo.ErpPayInfoVO;
import com.bytter.biz.dataexchange.basic.vo.ErpWageDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author qijh
 */
@Repository
public class ErpPayInfoDao extends BaseDao<ErpPayInfo,String> {
    /**
     * 获取Erp中间表数据
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        //交易时间
        String gedCreateDate = (String) dataTablesInput.getFormdata().get("ged_createDate");
        String ledCreateDate = (String) dataTablesInput.getFormdata().get("led_createDate");
        if (StringUtils.isNotEmpty(gedCreateDate)) {
            gedCreateDate = gedCreateDate + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_createDate", gedCreateDate);
        }
        if (StringUtils.isNotEmpty(ledCreateDate)) {
            ledCreateDate = ledCreateDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_createDate", ledCreateDate);
        }
        sql.append(" select e.id as id,e.voucher_no_erp as voucherNoErp,e.amt as amt,e.wish_payday as wishPayday, ");
        sql.append(" e.payer_acc_no as payerAccNo,e.payer_acc_name as payerAccName,e.payer_bank_name as payerBankName,e.status as status, ");
        sql.append(" e.opp_bank_acc as oppBankAcc,e.opp_acc_name as oppAccName, ");
        sql.append(" e.opp_bank_name as oppBankName,e.data_source as dataSource,e.urgency_flag as urgencyFlag, ");
        sql.append(" e.abs as abs,e.create_date as createDate, ");
        sql.append(" e.CUR_CODE as curCode,e.BILL_TYPE as billType,e.BILL_FLAG as billFlag ");
        sql.append(" from ERP_PAYINFO e ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), ErpPayInfoVO.class, "e", false);
    }
    /**
     * 代发工资根据主表id查询明细
     * @param serialNo
     * @return
     */
    public List<ErpWageDetailVO> getErpWageDetailBySerialNo(String serialNo){
        String hql = " from ErpWageDetail e where e.serialNo = :serialNo ";
        Map<String,String> params = new HashMap<>();
        params.put("serialNo",serialNo);
        return listByHQLAliasToBean(hql,params, ErpWageDetailVO.class);
    }
    /**
     * 更新ERP付款单状态
     * @param id
     * @return
     */
    public int updateStatus(String id){
        int result = 0;
        String sql = " update erp_payinfo set status = 1 where id= :id  and status = 0 ";
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        result = this.batchSQLExecute(sql,map);
        return result;
    }
}
