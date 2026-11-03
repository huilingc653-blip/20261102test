package com.bytter.biz.erp.dao;

import com.bytter.biz.erp.entity.ErpItemQueryView;
import com.bytter.biz.erp.vo.request.ItemQueryRequestVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ItemQueryDao
 * @Author zx
 * @Description 预算科目查询接口Dao
 * @Date 2026/4/28 15:29
 */
@Repository
public class ItemQueryDao extends BaseDao {

    /**
     * 预算科目查询
     *
     * @param vo
     * @param tenantId
     * @return
     */
    public List<ErpItemQueryView> queryItemList(ItemQueryRequestVO vo, String tenantId) {
        StringBuilder sql = new StringBuilder(" SELECT * FROM ERP_ITEM_QUERY_VIEW  where tenantId = :tenantId ");
        Map<String, Object> params = new HashMap<String, Object>(16);
        params.put("tenantId", tenantId);

        if (!StringUtil.isBlank_new(vo.getItemCode())) {
            sql.append(" and itemCode = :itemCode ");
            params.put("itemCode", vo.getItemCode());
        }
        if (!StringUtil.isBlank_new(vo.getItemName())) {
            sql.append(" and itemName like :itemName ");
            params.put("itemName", "%" + vo.getItemName() + "%");
        }
        return listBySQLAliasToBean(sql.toString(),params, ErpItemQueryView.class);
    }

}
