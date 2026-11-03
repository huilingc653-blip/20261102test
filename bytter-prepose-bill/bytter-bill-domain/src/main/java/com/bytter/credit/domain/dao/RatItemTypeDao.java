package com.bytter.credit.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.credit.domain.entity.RatItemType;
import com.bytter.credit.dto.RatItemTypeDTO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
/**
 * @Title: Dao
 * @Description: 授信品种
 * @author onlineGenerator
 * @date 2017-07-20 14:52:52
 * @version 1.0
 *
 */
@Repository
public class RatItemTypeDao extends BaseDao<RatItemType, String> {
    /**
     * 查询数据列表
     * 
     * @param input
     * @return
     */
    public DataTablesOutput dataTables(DataTablesInput input) {
        String sql = " select a.id as id, a.rit_code as ritCode, a.rit_name as ritName,a.rit_name as itemTypeName,a.RIT_DESC as ritDesc "
                + " from RAT_ITEM_TYPE a where a.status<>-2 order by rit_code   ";
        return this.dealDynamicBySQL(input, sql.toString(), RatItemTypeDTO.class, "", false, null);
    }
    /**
     * 功能介绍 : 获取有效的授信品种类型。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatItemTypeDTO> getAllRatItemType() {
        String sql = " select a.id as id, a.rit_code as ritCode, a.rit_name as ritName "
                + " from RAT_ITEM_TYPE a where a.status <> -2 order by rit_code ";
        return listBySQLAliasToBean(sql, null, RatItemTypeDTO.class);
    }
    /**
     * 功能介绍 : 获取授信品种类型列表。<br>
     * <b>日期：</b>2017年8月1日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatItemType> getItemTypeByCode(String ritCode) {
        String hql = " from RatItemType a where a.status <> :status and ritCode = :ritCode ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", CommonConstant.STATUE_DELETE);
        params.put("ritCode", ritCode);
        return listByHQL(hql, params);
    }
}
