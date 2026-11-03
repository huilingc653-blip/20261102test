package com.bytter.biz.erp.dao;

import com.bytter.biz.erp.entity.ErpCorpQueryView;
import com.bytter.biz.erp.vo.request.CorpQueryRequestVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单位信息查询接口DAO。
 *
 * @author zx
 * @date 2026/4/29
 */
@Repository
public class CorpQueryDao extends BaseDao {

    /**
     * 查询单位信息。
     *
     * @param vo 查询参数
     * @param tenantId 租户ID
     * @return 单位信息列表
     */
    public List<ErpCorpQueryView> queryCorpList(CorpQueryRequestVO vo, String tenantId) {
        StringBuilder sql = new StringBuilder(" SELECT * FROM ERP_CORP_QUERY_VIEW WHERE 1=1 AND tenantId = :tenantId ");
        Map<String, Object> params = new HashMap<String, Object>(16);
        params.put("tenantId", tenantId);

        if (!StringUtil.isBlank_new(vo.getCode())) {
            sql.append(" AND code = :code ");
            params.put("code", vo.getCode());
        }
        if (!StringUtil.isBlank_new(vo.getName())) {
            sql.append(" AND name LIKE :name ");
            params.put("name", "%" + vo.getName() + "%");
        }
        if (!StringUtil.isBlank_new(vo.getUpdateDate())) {
            sql.append(" AND updateDate >= :updateDate ");
            params.put("updateDate", vo.getUpdateDate());
        }
        return listBySQLAliasToBean(sql.toString(), params, ErpCorpQueryView.class);
    }
}
