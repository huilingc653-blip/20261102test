package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.NisProject;
import com.bytter.biz.netbank.vo.NisProjectVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description:
 * @date 2018/5/14 16:30
 */
@Repository
public class NisProjectDao extends BaseDao<NisProject, String> {
    /**
     * 查询项目维护列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput<NisProjectVO> getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.id as id, p.project_code as projectCode, p.project_name as projectNameShow,p.project_name as projectName,");
        sql.append("p.project_cdsign as projectCdSign, p.match_attr as matchAttr,p.match_content as matchContent,");
        sql.append("p.match_relation as matchRelation,p.project_sort as projectSort ");
        sql.append(" from nis_project p where 1 = 1 ");
        return getDynamicBySQL(input, sql.toString(), NisProjectVO.class, "p", false);
    }
    /**
     * 根据id获取项目实体
     *
     * @param id
     * @return
     */
    public NisProject findByid(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.id as id, p.project_code as projectCode, p.project_name as projectName,");
        sql.append("p.project_cdsign as projectCdSign, p.match_attr as matchAttr, p.match_content as matchContent,");
        sql.append("p.match_relation as matchRelation, p.project_sort as projectSort, p.rmk as rmk  ");
        sql.append(" from nis_project p where p.id = :id ");
        HashMap<String, String> params = new HashMap();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, NisProject.class);
    }
    /**
     * 根据项目代码获取项目实体
     *
     * @param projectCode
     * @return
     */
    public NisProject findProjectCode(String projectCode) {
        String sql = " from NisProject n where n.projectCode = :projectCode ";
        return (NisProject) createQuery(sql).setString("projectCode", projectCode).uniqueResult();
    }
    /**
     * 根据ID查询业务类型信息
     *
     * @param id
     * @return
     */
    public NisProjectVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.id as id, p.project_code as projectCode, p.project_name as projectName,");
        sql.append("p.project_cdsign as projectCdSign, p.match_attr as matchAttr, p.match_content as matchContent,");
        sql.append("p.match_relation as matchRelation, p.project_sort as projectSort, p.rmk as rmk  ");
        sql.append(" from nis_project p where p.id = :id ");
        HashMap<String, String> params = new HashMap();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, NisProjectVO.class);
    }
    /**
     * 根据ID删除数据
     *
     * @param id
     */
    public void deleteById(String id) {
        String sql = " delete from nis_project where id=:id ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }
    /**
     * 根据条件获取项目信息
     *
     * @param projectCdSign 收支标志
     * @return
     */
    public List<NisProject> listNisProjectByCdSign(String projectCdSign) {
        String hql = " from NisProject n where n.projectCdSign = :projectCdSign ";
        HashMap<String, String> params = new HashMap();
        params.put("projectCdSign", projectCdSign);
        return this.listByHQL(hql.toString(), params);
    }
}
