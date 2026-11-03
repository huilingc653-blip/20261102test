package com.bytter.system.dao;
import com.bytter.system.entity.CellManage;
import com.bytter.system.api.constant.SysOrgantypeConstant;
import com.bytter.system.vo.CellManageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author wangzy
 * @Title: CellManageDao
 * @Description: 管理单元Dao
 * @date 2020/5/12 11:46
 */
@Repository
public class CellManageDao extends BaseDao<CellManage, String> {
    /**
     * 管理单元查询
     *
     * @param params 查询条件
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<CellManageVO> listCellManageVO(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("select s.id as id,BT_CONCAT4('(',s.cell_Code,')',s.cell_Name) as cellCodeAndName,");
        sql.append(" s.rmk as rmk,s.valid_Sign as validSign,s.cell_Code as cellCode,s.cell_Name as cellName,s.parent_Id as parentId,d.type_name as validSignName ");
        sql.append(" from SYS_CELL_MANAGE s ");
        sql.append(" left join sys_Dict d on d.type_group_id=:dictGroupId and d.type_code = s.valid_Sign");
        sql.append(" where 1=1 ");
        if (params.containsKey("cellCodeAndName")) {
            sql.append(" and (s.cell_Code like :cellCodeAndName or s.cell_Name like :cellCodeAndName) ");
        }
        if (params.containsKey("validSign")) {
            sql.append(" and s.valid_sign =:validSign ");
        }
        sql.append(" order by s.cell_Code ");
        return listBySQLAliasToBean(sql.toString(), params, CellManageVO.class);
    }
    /**
     * 获取所有父类Id
     *
     * @return 单位id
     * @author
     */
    public List<String> getAllCellParentIds(String validSign) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.PARENT_ID as parentId from SYS_CELL_MANAGE s ");
        sql.append(" where s.VALID_SIGN =:validSign and s.PARENT_ID is not null ");
        params.put("validSign", validSign);
        Query query = createSQLQuery(sql.toString(), params);
        List<String> list = query.list();
        return list;
    }
    /**
     * 管理单元信息查询
     *
     * @param id 管理单元ID
     * @return
     */
    @SuppressWarnings("rawtypes")
    public CellManageVO getCellManageVOInfo(String id) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" case when p.id is not null then BT_CONCAT4('(',p.cell_Code,')',p.cell_Name) else null end as parentCell, ");
        sql.append(" s.cell_Code as cellCode,s.cell_Name as cellName,s.valid_sign as validSign,s.parent_id as parentId,s.rmk as rmk ");
        sql.append(" from SYS_CELL_MANAGE s left join SYS_CELL_MANAGE p on s.parent_id=p.id ");
        sql.append(" where s.id=:id");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, CellManageVO.class);
    }
    /**
     * 判断单元是否重复
     *
     * @param cellCode 管理单元code
     * @return
     */
    public String checkCellCode(String cellCode) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select cell_Code from SYS_CELL_MANAGE where cell_code=:cellCode and VALID_SIGN in (:validSign) ");
        params.put("cellCode", cellCode);
        params.put("validSign", new String[]{SysOrgantypeConstant.SYS_ORGANTYPE_VALID_SIGN_0,
                SysOrgantypeConstant.SYS_ORGANTYPE_VALID_SIGN_1});
        return (String) this.createSQLQuery(sql.toString(), params).uniqueResult();
    }
    /**
     * 管理单元修改无效检查
     *
     * @param id 管理单元ID
     * @return
     */
    @SuppressWarnings("rawtypes")
    public CellManageVO checkCellManageVOInfo(String id) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" m.CM_ID as id,p.cell_code as cellCode,s.rmk as rmk ");
        sql.append(" from SYS_CELL_MANAGE s left join SYS_CELL_MANAGE p on s.id=p.parent_id and p.VALID_SIGN =:validSign ");
        sql.append(" left join SYS_CORP_CM_MAP m on m.CM_ID=s.id ");
        sql.append(" where s.id=:id");
        params.put("id", id);
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        List<CellManageVO> list = listBySQLAliasToBean(sql.toString(), params, CellManageVO.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 根据管理单元代码或者名称获取管理单元信息
     */
    public CellManage getCellManageByCodeOrName(String code, String name) {
        CellManage info = null;
        if (StringUtils.isEmpty(code) && StringUtils.isEmpty(name)) {
            return info;
        }
        StringBuilder hql = new StringBuilder("from CellManage where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(code)) {
            hql.append(" and cellCode=:code ");
            params.put("code", code);
        }
        if (StringUtils.isNotEmpty(name)) {
            hql.append(" and cellName=:name ");
            params.put("name", name);
        }
        hql.append(" order by createTime desc ");
        List list = this.listByHQL(hql.toString(), params);
        if (list.size() > 0) {
            info = (CellManage) list.get(0);
        }
        return info;
    }
}
