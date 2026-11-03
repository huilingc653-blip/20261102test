package com.bytter.basic.dao;

import com.bytter.basic.entity.BtInputBankSwift;
import com.bytter.basic.vo.BtInputBankSwiftVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName BtInputBankSwiftDao
 * @Author zx
 * @Description 境内银行Swift Code管理Dao
 * @Date 2026/1/21 13:53
 */
@Repository
public class BtInputBankSwiftDao extends BaseDao<BtInputBankSwift, String> {

    /**
     * 境内银行Swift Code管理数据查询
     *
     * @param input
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.BANK_CODE as bankCode,b.BANK_NAME as bankName,b.SWIFT_CODE as swiftCode ");
        sql.append(" FROM BT_INPUT_BANK_SWIFT b ");
        sql.append(" where 1=1 ");
        return getDynamicBySQL(input, sql.toString(), BtInputBankSwiftVO.class, "b", false);
    }

    /**
     * 根据开户行行号判断是否已存在对应的SWIFT CODE映射
     *
     * @param bankCode
     * @param excludeId
     * @return
     */
    public BtInputBankSwiftVO getBtInputBankSwiftByBankCode(String bankCode, String excludeId) {
        HashMap<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT b.* FROM BT_INPUT_BANK_SWIFT b ");
        sql.append(" WHERE b.BANK_CODE = :bankCode ");
        params.put("bankCode", bankCode);
        if (StringUtils.isNotBlank(excludeId)) {
            sql.append(" AND b.id <> :id ");
            params.put("id", excludeId);
        }
        List<BtInputBankSwiftVO> list = listBySQLAliasToBean(sql.toString(), params, BtInputBankSwiftVO.class);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

}
