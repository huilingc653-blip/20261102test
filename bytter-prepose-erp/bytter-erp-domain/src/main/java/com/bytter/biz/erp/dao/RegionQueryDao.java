package com.bytter.biz.erp.dao;

import com.bytter.biz.erp.entity.ErpRegionQueryView;
import com.bytter.biz.erp.vo.request.RegionRequestVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RegionQueryDao
 * @Author zx
 * @Description 省市查询接口Dao
 * @Date 2026/4/28 15:29
 */
@Repository
public class RegionQueryDao extends BaseDao {

    /**
     * 省市查询
     *
     * @param vo
     * @param tenantId
     * @return
     */
    public List<ErpRegionQueryView> queryRegionList(RegionRequestVO vo, String tenantId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id as id, bank_input_city as cityCode, ");
        sql.append(" name as cityName, bank_prov as provCode, prov_input_name as provName FROM SYS_REGION where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>(16);

        if (!StringUtil.isBlank_new(vo.getCityCode())) {
            sql.append(" and bank_input_city = :cityCode ");
            params.put("cityCode", vo.getCityCode());
        }
        if (!StringUtil.isBlank_new(vo.getCityName())) {
            sql.append(" and name like :cityName ");
            params.put("cityName", "%" + vo.getCityName() + "%");
        }
        if (!StringUtil.isBlank_new(vo.getProvCode())) {
            sql.append(" and bank_prov = :provCode ");
            params.put("provCode", vo.getProvCode());
        }
        if (!StringUtil.isBlank_new(vo.getProvName())) {
            sql.append(" and prov_input_name like :provName ");
            params.put("provName", "%" + vo.getProvName() + "%");
        }
        return listBySQLAliasToBean(sql.toString(),params, ErpRegionQueryView.class);
    }

}
