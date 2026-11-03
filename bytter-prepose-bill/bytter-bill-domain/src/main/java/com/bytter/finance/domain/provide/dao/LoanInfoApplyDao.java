package com.bytter.finance.domain.provide.dao;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.system.api.dto.BtBankDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
@Repository
public class LoanInfoApplyDao extends BaseDao<CmsProvideLoanInfo, String> {
    /**
     * 功能介绍 : 查询银行。<br>
     * <b>日期：</b>2017年9月14日<br>
     * 
     * @version 1.0
     * @author tongqr
     */
	//走IBtBankService类的 getByBankName外调方法
//    public BtBankDTO getBanlBybankName(String bankName) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        StringBuilder sql = new StringBuilder();
//        sql.append(
//                "select ibi.id    as id, ibi.SYS_BANK_CODE  as bankCode,ibi.BANK_NAME  as bankName, ibi.BANK_PREFIX  as bankPrefix,");
//        sql.append("  ibi.BANK_CITY_CODE as bankCityCode, r.PARENT_ID  as prov, r.ID   as city");
//        sql.append(
//                "   FROM BT_INPUT_BANK_INFO ibi  INNER JOIN SYS_BANK_REGION br  ON ibi.BANK_CITY_CODE = br.BANK_CITY");
//        sql.append(
//                "  INNER JOIN SYS_REGION r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY  where 1 = 1 and ibi.BANK_NAME=:bankName ");
//        params.put("bankName", bankName);
//
//        return getBySQLAliasToBean(sql.toString(), params, BtBankDTO.class);
//    }
}
