package com.bytter.credit.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatItemCustom;
import com.bytter.credit.domain.vo.RatItemCustomVO;
import com.bytter.credit.dto.RatSysItemDTO;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
/**
 * @Title: Dao
 * @Description: 授信品种
 * @author onlineGenerator
 * @date 2017-07-20 15:46:08
 * @version 1.0
 *
 */
@Repository
public class RatItemCustomDao extends BaseDao<RatItemCustom, String> {
    /**
     * 功能介绍 : 根据授信业务主体类型获取对应的系统授信品种。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatSysItemDTO> getSysRatItemByType(String type, String code) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.code as code, a.name as name, a.type as type, ")
                .append(" a.flag as flag from rat_sys_item a where a.type = :type ");
        if (!StringUtils.isEmpty(code)) {
            sql.append(" and a.code = :code ");
            params.put("code", code);
        }
        sql.append(" order by a.code ");
        params.put("type", type);
        return listBySQLAliasToBean(sql.toString(), params, RatSysItemDTO.class);
    }
    /**
     * 功能介绍 : 根据系统授信品种和授信业务主体获取维护的自定义品种。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatItemCustom> getRatItemBySysItemAndType(String sysItemCode, Integer type) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select a from RatItemCustom a where a.type = :type and a.status <> :status ");
        if (!StringUtils.isEmpty(sysItemCode)) {
            sql.append(" and a.sysItemCode = :sysItemCode ");
            params.put("sysItemCode", sysItemCode);
        }
        params.put("type", type);
        params.put("status", CommonConstant.STATUE_DELETE);
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 根据系统授信品种和授信业务主体获取维护的自定义品种。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatItemCustom> getRatItemByItemNameAndType(String itemName, Integer type) {
        String sql = " select a from RatItemCustom a where a.name = :itemName and a.type = :type and a.status <> :status ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("itemName", itemName);
        params.put("type", type);
        params.put("status", CommonConstant.STATUE_DELETE);
        return listByHQL(sql, params);
    }
    /**
     * 功能介绍 : 根据授信品种类型获取维护的自定义品种。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatItemCustom> getRatItemByCustomType(String ritCode) {
        String sql = " select a from RatItemCustom a where a.ritCode = :ritCode and status = 0 ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ritCode", ritCode);
        return listByHQL(sql, params);
    }
    /**
     * 功能介绍 : 查询授信品种维护列表数据。<br>
     * <b>日期：</b>2017年7月21日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput<RatItemCustomVO> listOutputData(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rt.id as id,rt.NAME as name,rt.name as itemName,rt.FLAG as flag, ")
                .append(" rt.TYPE as type,rt.SYS_ITEM_CODE as sysItemCode, rs.NAME as sysItemName,rs.id as sysItemId, ")
                .append(" rt.REMARK as remark,rt.RIT_CODE as ritCode,ri.RIT_NAME as ritName,ri.id as ritId ")
                .append(" from RAT_ITEM_CUSTOM rt,RAT_SYS_ITEM rs,RAT_ITEM_TYPE ri ")
                .append(" where rt.SYS_ITEM_CODE = rs.CODE ").append(" and rt.RIT_CODE = ri.id ")
                .append(" and rt.STATUS <> -2 ");
        return getDynamicBySQL(input, sql.toString(), RatItemCustomVO.class, null, false);
    }
    /**
     * 根据系统编号获取授信品种
     */
    public List<RatSysItemDTO> getRatSysItemByCode(String code) {
        String sql = " select * from rat_sys_item a where a.code = :code and a.type = :type ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        params.put("type", RationConstant.RAT_TYPE_0);// 银行授信
        return listBySQLAliasToBean(sql.toString(), params, RatSysItemDTO.class);
    }
    /**
     * 功能介绍 : 获取银行授信品种。<br>
     */
    public List<RatItemCustomVO> getRatCustomByBank(String customId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select c.id as id,c.name as name, c.type as type,t.rit_name as ritName from rat_item_custom c "
                + " left join RAT_ITEM_TYPE t on c.rit_code = t.id" + " where c.type = 0 and c.status = 0 ";
        if (EmptyUtils.isNotEmpty(customId)) {
            sql += " and c.id = :customId";
            params.put("customId", customId);
        }
        return listBySQLAliasToBean(sql, params, RatItemCustomVO.class);
    }
}
