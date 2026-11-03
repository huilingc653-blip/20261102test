package com.bytter.biz.erp.dao;

import com.bytter.biz.erp.entity.ErpBankInputQueryView;
import com.bytter.biz.erp.vo.request.BankInputQueryRequestVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行联行号查询DAO。
 *
 * @author zx
 * @date 2026/4/29
 */
@Repository
public class BankInputQueryDao extends BaseDao {

    /**
     * 查询银行联行号信息。
     *
     * @param vo 查询参数
     * @param tenantId 租户ID
     * @return 联行号列表
     */
    public List<ErpBankInputQueryView> queryBankInputList(BankInputQueryRequestVO vo, String tenantId) {
        StringBuilder sql = new StringBuilder(" SELECT * FROM ERP_BANK_INPUT_QUERY_VIEW WHERE 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>(16);
        if (!StringUtil.isBlank_new(vo.getBankCode())) {
            sql.append(" AND bankCode = :bankCode ");
            params.put("bankCode", vo.getBankCode());
        }
        if (!StringUtil.isBlank_new(vo.getBankName())) {
            sql.append(" AND bankName LIKE :bankName ");
            params.put("bankName", "%" + vo.getBankName() + "%");
        }
        if (!StringUtil.isBlank_new(vo.getUpdateDate())) {
            sql.append(" AND updateDate >= :updateDate ");
            params.put("updateDate", vo.getUpdateDate());
        }
        return listBySQLAliasToBean(sql.toString(), params, ErpBankInputQueryView.class);
    }

}
