package com.bytter.finance.domain.provide.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.finance.domain.provide.entity.CmsFloatingMode;
import com.bytter.finance.domain.provide.vo.CmsFloatingModeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
/**
 * @Title: Dao
 * @Description: CMS_FOLATING_MODE
 * @author songkk
 * @date 2017-08-24 11:09:43
 * @version 1.0
 *
 */
@Repository
public class CmsFloatingModeDao extends BaseDao<CmsFloatingMode, String> {
    /**
     * 查询CmsFloatingMode DataTable数据
     * 
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listCmsFloatingMode(DataTablesInput dataTablesInput, String billCode) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(
                "floating_mode as floatingMode,float_mode_next as floatModeNext,enter_date as enterDate,floating_modename as floatingModeName ");
        sql.append("from cms_floating_mode where bill_Code=:billCode order by create_Date asc");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billCode", billCode);
        return getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, CmsFloatingModeVO.class);
    }
    /**
     * 根据单号按日期降序查询
     * 
     * @param id
     * @return
     */
    public List<CmsFloatingModeVO> getCmsFloatingMode(String id) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("floating_mode as floatingMode,float_mode_next as floatModeNext,floating_modename as floatingModeName ,enter_date as enterDate ");
        sql.append(" ,repay_cycle as repayCycle, points_text as pointsText  ");
        sql.append("from cms_floating_mode where bill_Code=:id order by create_Date desc");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, CmsFloatingModeVO.class);
    }
}
