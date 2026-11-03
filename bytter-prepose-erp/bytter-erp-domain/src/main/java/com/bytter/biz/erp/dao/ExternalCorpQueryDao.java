package com.bytter.biz.erp.dao;

import com.bytter.biz.erp.entity.ErpExternalCorpQueryView;
import com.bytter.biz.erp.vo.request.ExternalCorpQueryRequestVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客商信息查询 DAO。
 *
 * @author zx
 * @date 2026/4/29
 */
@Repository
public class ExternalCorpQueryDao extends BaseDao {

    /**
     * 按租户与可选条件查询客商信息。
     *
     * @param vo       查询参数
     * @param tenantId 租户 ID
     * @return 客商视图行列表
     */
    public List<ErpExternalCorpQueryView> queryExternalCorpList(ExternalCorpQueryRequestVO vo, String tenantId) {
        StringBuilder sql = new StringBuilder(
                " SELECT * FROM ERP_EXTERNAL_CORP_QUERY_VIEW WHERE tenantId = :tenantId ");
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
        return listBySQLAliasToBean(sql.toString(), params, ErpExternalCorpQueryView.class);
    }
}
