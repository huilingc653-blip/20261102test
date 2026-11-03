package com.bytter.biz.erp.dao;

import com.bytter.biz.erp.entity.ErpExternalCorpAccQueryView;
import com.bytter.biz.erp.vo.request.ExternalCorpAccQueryRequestVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客商账号信息查询 DAO。
 *
 * @author zx
 * @date 2026/4/29
 */
@Repository
public class ExternalCorpAccQueryDao extends BaseDao {

    /**
     * 按租户与可选条件查询客商账号信息。
     *
     * @param vo       查询参数
     * @param tenantId 租户 ID
     * @return 客商账号视图行列表
     */
    public List<ErpExternalCorpAccQueryView> queryExternalCorpAccList(ExternalCorpAccQueryRequestVO vo, String tenantId) {
        StringBuilder sql = new StringBuilder(
                " SELECT * FROM ERP_EXTERNAL_CORP_ACC_QUERY_VIEW WHERE tenantId = :tenantId ");
        Map<String, Object> params = new HashMap<String, Object>(8);
        params.put("tenantId", tenantId);

        if (!StringUtil.isBlank_new(vo.getCode())) {
            sql.append(" AND code = :code ");
            params.put("code", vo.getCode());
        }
        if (!StringUtil.isBlank_new(vo.getName())) {
            sql.append(" AND name LIKE :name ");
            params.put("name", "%" + vo.getName() + "%");
        }
        if (!StringUtil.isBlank_new(vo.getExternalAcc())){
            sql.append(" AND externalAcc = :externalAcc ");
            params.put("externalAcc", vo.getExternalAcc());
        }
        return listBySQLAliasToBean(sql.toString(), params, ErpExternalCorpAccQueryView.class);
    }
}
