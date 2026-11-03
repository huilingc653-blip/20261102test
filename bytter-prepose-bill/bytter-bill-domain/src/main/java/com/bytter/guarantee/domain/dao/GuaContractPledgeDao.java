package com.bytter.guarantee.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.guarantee.domain.vo.GuaContractPledgeVO;
import org.springframework.stereotype.Repository;
import com.bytter.guarantee.domain.entity.GuaContractPledge;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaContractPledgeDao
 * @Description 担保合同关联担抵质押物Dao
 * @date 2018年12月20日
 */
@Repository
public class GuaContractPledgeDao extends BaseDao<GuaContractPledge, String> {
    /**
     * 通过担保合同id查询担保合同关联抵质押物信息表
     * 
     * @param id
     * @return
     */
    public List<GuaContractPledge> getGuaContractPledge(String guaId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT A.ID AS id,A.GUA_CONTRACT_WAY_ID AS guaContractWayId,A.PLEDGE_ID AS pledgeId,A.GUA_MONEY AS guaMoney  ");
        sql.append("FROM GUA_CONTRACT_WAY B  LEFT JOIN  GUA_CONTRACT_PLEDGE A  ON B.ID=A.GUA_CONTRACT_WAY_ID  ");
        sql.append("WHERE B.GUA_ID=:guaId");
        Map<String, String> map = new HashMap<String, String>();
        map.put("guaId", guaId);
        return listBySQLAliasToBean(sql.toString(), map, GuaContractPledge.class);
    }
    /**
     * 通过担保合同关联担保方式id删除相应的担保合同关联抵质押物信息表
     * 
     * @param id
     * @return
     */
    public int delGuaContractPledge(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM  GUA_CONTRACT_PLEDGE WHERE GUA_CONTRACT_WAY_ID=:id ");
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        return createSQLQuery(sql.toString(), map).executeUpdate();
    }
    /**
     * 通过抵质押物id查询担保合同关联抵质押物的信息
     * 
     * @param pledgeId
     * @return
     */
    public List<GuaContractPledge> getGuaContractPledgeByPledgeId(String pledgeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT A.ID AS id,A.GUA_CONTRACT_WAY_ID AS guaContractWayId,A.PLEDGE_ID AS pledgeId,A.GUA_MONEY AS guaMoney  ");
        sql.append("FROM  GUA_CONTRACT_PLEDGE A  WHERE A.PLEDGE_ID=:pledgeId ");
        Map<String, String> map = new HashMap<String, String>();
        map.put("pledgeId", pledgeId);
        return listBySQLAliasToBean(sql.toString(), map, GuaContractPledge.class);
    }
    /**
     * 通过担保合同关联的担保方式id查询抵质押物信息表
     * 
     * @param guaWayId
     * @return
     */
    public List<GuaContractPledge> queryGuaContractPledge(String guaWayId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT A.ID AS id,A.GUA_CONTRACT_WAY_ID AS guaContractWayId,A.PLEDGE_ID AS pledgeId,A.GUA_MONEY AS guaMoney  ");
        sql.append("FROM  GUA_CONTRACT_PLEDGE A  WHERE A.GUA_CONTRACT_WAY_ID=:guaContractWayId ");
        Map<String, String> map = new HashMap<String, String>();
        map.put("guaContractWayId", guaWayId);
        return listBySQLAliasToBean(sql.toString(), map, GuaContractPledge.class);
    }
    /**
     * 通过担保合同id查询担保合同关联抵质押物信息表
     * 
     * @return
     */
    public List<GuaContractPledgeVO> getGuaContractPledgeList(String guaId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT A.ID AS id,A.GUA_CONTRACT_WAY_ID AS guaContractWayId,A.PLEDGE_ID AS pledgeId,A.GUA_MONEY AS guaMoney, B.WAY_CODES  as guaWayId ");
        sql.append("FROM  GUA_CONTRACT_PLEDGE A LEFT JOIN    GUA_CONTRACT_WAY B ON B.ID=A.GUA_CONTRACT_WAY_ID  ");
        sql.append("WHERE B.GUA_ID=:guaId");
        Map<String, String> map = new HashMap<String, String>();
        map.put("guaId", guaId);
        return listBySQLAliasToBean(sql.toString(), map, GuaContractPledgeVO.class);
    }
}
