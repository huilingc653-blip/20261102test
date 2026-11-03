package com.bytter.finance.domain.provide.dao;
import com.bytter.finance.domain.provide.entity.CmsInterestWay;
import com.bytter.finance.domain.provide.vo.CmsInterestWayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: CMS_INTEREST_WAY
 * @author onlineGenerator
 * @date 2017-08-24 11:09:43
 * @version 1.0
 *
 */
@Repository
public class CmsInterestWayDao extends BaseDao<CmsInterestWay, String> {
    /**
     * 查询CmsInterestWay DataTable数据
     * 
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listCmsInterestWay(DataTablesInput dataTablesInput, String sendCode) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(
                "interest_Way as interestWay,expiry_Day as expiryDay,withholding_Day as withholdingDay,active_Date as activeDate, ");
        sql.append(" INTEREST_FREQUENCY_TYPE,INTEREST_FREQUENCY_NEXT,INTEREST_FREQUENCY,INTEREST_FREQUENCY_NUM ");
        sql.append("from Cms_Interest_Way where send_Code=:sendCode order by create_Date asc");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sendCode", sendCode);
        return getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, CmsInterestWayVO.class);
    }
    /**
     * 根据单号按日期降序查询
     * 
     * @param sendCode
     * @return
     */
    public List<CmsInterestWayVO> getCmsInterestWay(String id) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(
                "interest_Way as interestWay,expiry_Day as expiryDay,withholding_Day as withholdingDay,active_Date as activeDate, ");
        sql.append("create_Date as lastUpdateTime from Cms_Interest_Way where send_Code=:id order by create_Date desc");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, CmsInterestWayVO.class);
    }
}
