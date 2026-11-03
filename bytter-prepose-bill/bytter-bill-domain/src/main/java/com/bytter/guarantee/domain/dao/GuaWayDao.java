package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.GuaWay;
import com.bytter.guarantee.domain.vo.GuaWayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class GuaWayDao extends BaseDao<GuaWay, String> {
    public DataTablesOutput<GuaWayVO> dataTable(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select g.id as id,g.code as code,g.name as name,g.name as guaWayName,g.is_pledge as isPledge,g.pledge_property as pledgeProperty ");
        sql.append(" from gua_way g ");
        sql.append(" where 1 = 1");
        return getDynamicBySQL(input, sql.toString(), GuaWayVO.class, null, false);
    }
    /***
     * 根据担保方式code查询是否有相同 -----
     */
    public List<GuaWay> findAllByCode(String code, String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" from GuaWay where 1 = 1 and code=:code ");
        if (EmptyUtils.isNotEmpty(id)) {
            sb.append(" and id <> :id ");
            params.put("id", id);
        }
        params.put("code", code);
        return createQuery(sb.toString(), params).list();
    }
    /***
     * 根据担保方式name查询是否有相同 -----
     */
    public List<GuaWay> findAllByName(String name, String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" from GuaWay where 1 = 1 and name=:name ");
        if (EmptyUtils.isNotEmpty(id)) {
            sb.append(" and id <> :id ");
            params.put("id", id);
        }
        params.put("name", name);
        return createQuery(sb.toString(), params).list();
    }
    /**
     * 判断是否被引用
     */
    public boolean isUsed(String wayId) {
        String sql = "select 1 from gua_contract_way where way_codes = :wayId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("wayId", wayId);
        List<String> list = listBySQL(sql, param);
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 判断担保方式是否被担保合同登记应用
     */
    public boolean isRegister(String wayId) {
        String sql = "select 1 from gua_contract_info i,gua_contract_way w where i.id = w.gua_id "
                + "and w.way_codes = :wayId and i.status = 95 ";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("wayId", wayId);
        List<String> list = listBySQL(sql, param);
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    /***
     * 根据担保方式code查询担保方式 -----
     */
    public List<GuaWay> queryAllByCodes(List<String> codeList) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" from GuaWay where 1 = 1 and code in(:codes) ");
        params.put("codes", codeList);
        return createQuery(sb.toString(), params).list();
    }
    /**
     * 根据担保合同id获取所有担保合同挂接得担保方式
     *
     * @param params
     * @return
     */
    public List<GuaWayVO> getGuaWayByConstractIds(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select id,code,name,is_pledge isPledge, pledge_property pledgeProperty,rmk");
        sql.append("  from gua_way ");
        sql.append(" where id in ");
        sql.append("       (select distinct way_codes ");
        sql.append("          from gua_contract_way ");
        sql.append("         where gua_id in (:contractIds))");
        return listBySQLAliasToBean(sql.toString(), params, GuaWayVO.class);
    }
}
