package com.bytter.finance.domain.directFinance.dao;
import com.bytter.finance.domain.directFinance.entity.DfThirdPartyAgency;
import com.bytter.finance.domain.directFinance.vo.DfThirdPartyAgencyVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-12 15:04
 * @Version 1.0
 */
@Repository
public class DfThirdPartyAgencyDao extends BaseDao<DfThirdPartyAgency, String> {
    /**
     * 通过主表id删除第三方机构数据
     * 
     * @param params
     */
    public void deleteThirdPartyAgency(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from DF_THIRD_PARTY_AGENCY where DIRECT_FINANCE_ID = :id ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 获取第三方机构
     * 
     * @param params
     * @return
     */
    public List<DfThirdPartyAgencyVO> listThirdPartyAgencyByDfId(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" AGENCY_TYPE agencyType, ");
        sql.append(" AGENCY_NAME agencyName ");
        sql.append(" from  DF_THIRD_PARTY_AGENCY");
        sql.append(" where DIRECT_FINANCE_ID = :directFinanceId ");
        return listBySQLAliasToBean(sql.toString(), params, DfThirdPartyAgencyVO.class);
    }
}
