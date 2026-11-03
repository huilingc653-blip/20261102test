package com.bytter.finance.domain.lease.dao;
import com.bytter.finance.domain.lease.entity.LeaseItemType;
import com.bytter.finance.domain.lease.vo.LeaseItemTypeVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author wanggl
 * @version 1.0
 * @Title: Dao
 * @Description: 融资租赁基础数据
 * @date 2022-4-2
 */
@Repository
public class LeaseTypeDao extends BaseDao<LeaseItemType, String> {
    public DataTablesOutput listMsgData(DataTablesInput input) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select ID as id,code ,name,rmk,status from CMS_TENANCY_ITEMS_TYPES c where c.status = :status ");
        params.put("status", CmsConstant.APPROVED);
        return getDynamicBySQL(input, sql.toString(), LeaseItemTypeVO.class, "c", false, null, params);
    }
    public LeaseItemType getLeaseTypebyName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ID as id,code ,name,rmk,status from CMS_TENANCY_ITEMS_TYPES c where c.status = '95' ");
        sql.append("and c.name=:name");
        params.put("name", name);
        return getBySQLAliasToBean(sql.toString(), params, LeaseItemType.class);
    }
    /**
     * 计数
     *
     * @param params
     * @return
     */
    public List<LeaseItemTypeVO> getCountByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" id , ");
        sql.append(" code ");
        sql.append(" from CMS_TENANCY_ITEMS_TYPES c");
        sql.append(" where 1 =1 ");
        sql.append(" and c.code = :code ");
        return listBySQLAliasToBean(sql.toString(), params, LeaseItemTypeVO.class);
    }
    /**
     * 查询编码+名称数量
     *
     * @param
     * @return
     */
    public List<LeaseItemType> getCountLeaseTypeById(LeaseItemType entity) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append(
                " select ID as id,code ,name,rmk,status from CMS_TENANCY_ITEMS_TYPES  where code = :code and name = :name and status = '95' ");
        params.put("code", entity.getCode());
        params.put("name", entity.getName());
        List<LeaseItemType> listInfo = this.listBySQLAliasToBean(sql.toString(), params, LeaseItemType.class);
        return listInfo;
    }
    /**
     * 查询类型编号是否存在
     **/
    public boolean judgeTypeByCode(String id, String code, String name) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select id,code ,name,status from CMS_TENANCY_ITEMS_TYPES  where 1=1");
        sql.append(" and status = :status and(code = :code or name = :name) ");
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and id <> :id");
            params.put("id", id);
        }
        params.put("status", CmsConstant.APPROVED);
        params.put("code", code);
        params.put("name", name);
        List<LeaseItemType> infoList = listBySQLAliasToBean(sql.toString(), params, LeaseItemType.class);
        if (infoList != null && infoList.size() > 0) {
            return false;
        }
        return true;
    }
    /**
     * 查询是否有占用
     */
    public boolean isOccupy(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                " select M.id from CMS_LEASE_SUB_MATER M left join CMS_TENANCY_ITEMS_TYPES T on M.LEASE_TYPE_ID = T.id ");
        sql.append(" where (M.status = 0 or M.status = 11 or M.status = -1) and T.ID = :id");
        params.put("id", id);
        List<LeaseItemType> infoList = listBySQLAliasToBean(sql.toString(), params, LeaseItemType.class);
        if (infoList != null && infoList.size() > 0) {
            return false;
        }
        return true;
    }
}
