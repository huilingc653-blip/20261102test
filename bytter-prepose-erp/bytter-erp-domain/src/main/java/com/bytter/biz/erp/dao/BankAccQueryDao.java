package com.bytter.biz.erp.dao;

import com.bytter.biz.erp.entity.ErpBankAccQueryView;
import com.bytter.biz.erp.vo.request.BankAccQueryRequestVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BankAccQueryDao
 * @Author zx
 * @Description 银行账户查询接口Dao
 * @Date 2026/4/28 15:29
 */
@Repository
public class BankAccQueryDao extends BaseDao {

    /**
     * 银行账户查询
     *
     * @param vo
     * @param tenantId
     * @return
     */
    public List<ErpBankAccQueryView> queryBankAccList(BankAccQueryRequestVO vo, String tenantId) {
        StringBuilder sql = new StringBuilder(" SELECT * FROM ERP_BANK_ACC_QUERY_VIEW  where tenantId = :tenantId ");
        Map<String, Object> params = new HashMap<String, Object>(16);
        params.put("tenantId", tenantId);

        if (!StringUtil.isBlank_new(vo.getBankAcc())) {
            sql.append(" and bankAcc = :bankAcc ");
            params.put("bankAcc", vo.getBankAcc());
        }
        if (!StringUtil.isBlank_new(vo.getAccName())) {
            sql.append(" and accName like :accName ");
            params.put("accName", "%" + vo.getAccName() + "%");
        }
        if (!StringUtil.isBlank_new(vo.getCorpCode())) {
            sql.append(" and corpCode = :corpCode ");
            params.put("corpCode", vo.getCorpCode());
        }
        if (!StringUtil.isBlank_new(vo.getCorpName())) {
            sql.append(" and corpName like :corpName ");
            params.put("corpName", "%" + vo.getCorpName() + "%");
        }
        if (!StringUtil.isBlank_new(vo.getUpdateDate())) {
            sql.append(" and updateDate >= :updateDate ");
            params.put("updateDate", vo.getUpdateDate());
        }
        return listBySQLAliasToBean(sql.toString(),params, ErpBankAccQueryView.class);
    }

}
