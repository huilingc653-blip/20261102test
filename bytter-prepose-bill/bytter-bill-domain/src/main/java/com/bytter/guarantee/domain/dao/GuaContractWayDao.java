package com.bytter.guarantee.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.guarantee.domain.vo.GuaContractWayVO;
import org.springframework.stereotype.Repository;
import com.bytter.guarantee.domain.entity.GuaContractWay;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaContractWayDao
 * @Description 担保合同关联担保方式Dao
 * @date 2018年12月20日
 */
@Repository
public class GuaContractWayDao extends BaseDao<GuaContractWay, String> {
    /**
     * 通过担保合同id查询担保合同关联担保方式信息
     *
     * @param id
     * @return
     */
    public List<GuaContractWayVO> getGuaContractWay(String guaId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT b.id,b.gua_id as guaId,b.way_codes as wayCodes,b.money FROM GUA_CONTRACT_WAY B WHERE B.GUA_ID=:id ");
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", guaId);
        return listBySQLAliasToBean(sql.toString(), map, GuaContractWayVO.class);
    }
    /**
     * 通过id查询担保合同关联担保方式信息
     *
     * @param id
     * @return
     */
    public GuaContractWay getGuaContractWayById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT B.ID AS id,B.GUA_ID AS guaId,B.WAY_CODES AS wayCodes FROM GUA_CONTRACT_WAY B WHERE B.ID=:id ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaContractWay.class);
    }
    /**
     * 通过担保合同id删除相应的担保合同关联担保方式信息
     *
     * @param id
     * @return
     */
    public int delGuaContractWay(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM  GUA_CONTRACT_WAY WHERE GUA_ID=:id ");
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        return createSQLQuery(sql.toString(), map).executeUpdate();
    }
    /**
     * 通过担保合同id查询担保合同关联担保方式信息
     *
     * @param id
     * @return
     */
    public List<GuaContractWayVO> getGuaContractWayVO(String guaId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT B.id as id,B.GUA_ID as guaId,B.WAY_CODES as wayCodes,B.MONEY AS money,A.CODE AS guaWayCode,A.NAME as guaWayName ")
                .append(" FROM GUA_CONTRACT_WAY B LEFT JOIN gua_way A").append(" on b.way_codes=a.id")
                .append(" WHERE B.GUA_ID=:id");
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", guaId);
        return listBySQLAliasToBean(sql.toString(), map, GuaContractWayVO.class);
    }
    public List<GuaContractWayVO> getListByInfoIds(List<String> ids) {
        StringBuilder sql = new StringBuilder("SELECT b.id,b.gua_id as guaId,b.way_codes as wayCodes,b.money FROM GUA_CONTRACT_WAY B WHERE B.GUA_ID in (:ids) ");
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("ids", ids);
        return listBySQLAliasToBean(sql.toString(), map, GuaContractWayVO.class);
    }
}
