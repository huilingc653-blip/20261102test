package com.bytter.finance.domain.security.dao;
import com.bytter.finance.domain.security.entity.CmsSecurityLevelInfo;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelInfoVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CmsSecurityLevelInfoDao extends BaseDao<CmsSecurityLevelInfo, String> {
    /**
     * 通过主信息id删除资产层级信息
     */
    public void deleteLevelInfoById(String securityInfoId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from CMS_SECURITY_LEVEL_INFO where SECURITY_INFO_ID = :securityInfoId ");
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("securityInfoId", securityInfoId);
        createSQLQuery(sql.toString(), parms).executeUpdate();
    }
    /**
     * 通过主表获取全部层级信息
     */
    public List<CmsSecurityLevelInfoVO> listLevelInfo(String securityInfoId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sl.*,l.LEVEL_NAME as levelName ");
        sql.append(" from CMS_SECURITY_LEVEL_INFO sl left join CMS_SECURITY_LEVEL l  on sl.LEVEL_ID=l.ID");
        sql.append(" where  sl.SECURITY_INFO_ID=:securityInfoId order by sl.CREATE_DATE");
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("securityInfoId", securityInfoId);
        return listBySQLAliasToBean(sql.toString(), parms, CmsSecurityLevelInfoVO.class);
    }
}
