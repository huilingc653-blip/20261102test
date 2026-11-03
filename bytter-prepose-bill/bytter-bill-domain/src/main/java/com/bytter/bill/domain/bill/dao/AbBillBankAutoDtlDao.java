package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbBillBankAutoDtl;
import com.bytter.bill.domain.bill.vo.AbBillBankAutoDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 票据银行对于自动业务配置
 * @author zhenghj
 * @date 2017-09-28
 * @version 1.0
 *
 */
@Repository
public class AbBillBankAutoDtlDao extends BaseDao<AbBillBankAutoDtl, String> {
    /**
     * 列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillBankAutoDtlVO> listAbBillBankAutoDtl(DataTablesInput dataTablesInput) {
//        StringBuilder sql = new StringBuilder("SELECT a.new_Bill_Flg as newBillFlg,a.id AS id,T.id as bifId,T.bif_code AS bifCode," );
        StringBuilder sql = new StringBuilder("SELECT a.new_Bill_Flg as newBillFlg,a.id AS id,a.bif_id as bifId,a.new_bif_code AS bifCode," );
                sql.append("a.auto_accept AS autoAccept,a.POOL_ENDORSE as poolEndorse,a.POOL_EMITBILL as poolEmitBill,");
//        sql.append("a.auto_invoice AS autoInvoice,a.auto_pay AS autoPay,a.valid_sign AS validSign,b.id as bankTypeId,b.bank_type AS bankType,b.type_name AS typeName,T.NAME AS NAME ");
        sql.append("a.auto_invoice AS autoInvoice,a.auto_pay AS autoPay,a.valid_sign AS validSign,a.bank_type_id as bankTypeId,a.bank_type AS bankType,a.type_name AS typeName,a.bif_name AS NAME ");
        sql.append(" FROM AB_BILL_BANK_AUTO_DTL a ");
//        sql.append(" LEFT JOIN BIS_BIF_INIT T ON A.BIF_CODE=T.ID");
//        sql.append(" LEFT JOIN BT_BANK_TYPE B ON T.BANK_TYPE_ID=B.ID");
        sql.append(" where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillBankAutoDtlVO.class, "", false);
    }
    /**
     * 根据银行接口类型Id判断是否存在多条
     * @param bifCode
     * @return
     */
    public List<AbBillBankAutoDtl> listByBifCode(String bifCode) {
//        List<AbBillBankAutoDtl> list = listBy("bifCode",bifCode);
        Map<String,Object> params=new HashMap<>();
        String hql = "from AbBillBankAutoDtl where bifCode = :bifCode ";
        params.put("bifCode", bifCode);
        return this.listByHQL(hql.toString(), params);
    }
    
    /**
     * 根据参数查询数据
     * @param params
     * @return
     */
    public List<AbBillBankAutoDtl> listAbBillBankAutoDtlByParams(Map<String,Object> params){
    	StringBuilder hql = new StringBuilder("from AbBillBankAutoDtl where 1=1 ");
    	if(params.containsKey("validSign")){
    		hql.append("and validSign=:validSign ");
    	}
    	if(params.containsKey("autoAccept")){
    		hql.append("and autoAccept=:autoAccept ");
    	}
    	if(params.containsKey("autoInvoice")){
    		hql.append("and autoInvoice=:autoInvoice ");
    	}
    	if(params.containsKey("autoPay")){
    		hql.append("and autoPay=:autoPay ");
    	}
    	if(params.containsKey("poolEndorse")){
    		hql.append(" and poolEndorse=:poolEndorse ");
    	}
        if(params.containsKey("poolEmitBill")){
            hql.append(" and poolEmitBill=:poolEmitBill ");
        }
        if(params.containsKey("bankTypeId")){
            hql.append(" and bankTypeId=:bankTypeId ");
        }
    	return this.listByHQL(hql.toString(), params);
    }
}
