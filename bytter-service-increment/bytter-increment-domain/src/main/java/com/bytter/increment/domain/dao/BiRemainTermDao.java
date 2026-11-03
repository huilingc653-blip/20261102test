package com.bytter.increment.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.domain.vo.BiRemainTermVO;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @ClassName BiRemainTermDao
 * @Description TODO
 * @Author fusb
 * @Date 2020/1/9 9:57
 * @Version 1.0
 **/
@Repository
public class BiRemainTermDao extends BaseDao {
    /**
    *@Description 查询SQL
    *@Param
    *@return java.lang.String
    *@Date 2020/1/11 13:26
    *@Author fusb
    */
    public String querySql(){
        StringBuilder sql = new StringBuilder();
        sql.append("select term.id as id,");
        sql.append("       term.term_name as termName,");
        sql.append("       term.is_valid as isValid,");
        sql.append("       term.sort as sort,");
        sql.append("       term.create_by as createBy,");
        sql.append("       term.create_date as createDate,");
        sql.append("       term.update_by as updateBy,");
        sql.append("       term.update_date as updateDate ");
        return sql.toString();
    }
    /**
    *@Description 剩余期限维护列表数据
    *@Param input 查询条件
    *@return com.bytter.core.common.datatable.DataTablesOutput
    *@Date 2020/1/9 10:39
    *@Author fusb
    */
    public DataTablesOutput remainTermTables(DataTablesInput input){
        StringBuilder sql = new StringBuilder(this.querySql());
        sql.append(" from bi_remain_term term where 1=1  ");
        input.setOrderString(new OrderString("term.sort", "asc"));
        return this.getDynamicBySQL(input,sql.toString(), BiRemainTermVO.class,null,false,null);
    }
    /**
    *@Description 查询有效的剩余期限维护信息
    *@Param
    *@return java.util.List<com.bytter.biz.cms.basedata.vo.BiRemainTermVO>
    *@Date 2020/1/15 14:45
    *@Author fusb
    */
    public List<BiRemainTermVO> getRemainTermList(){
        StringBuilder sql = new StringBuilder(this.querySql());
        sql.append(" from bi_remain_term term where term.is_valid='1' order by term.sort ");
        return this.listBySQLAliasToBean(sql.toString(),null,BiRemainTermVO.class);
    }
    /**
    *@Description 根据期限名称查询剩余期限维护信息
    *@Param termName 期限名称
    *@return com.bytter.biz.cms.basedata.vo.BiRemainTermVO
    *@Date 2020/1/10 16:48
    *@Author fusb
    */
    public BiRemainTermVO getBiRemainTermVOByTermName(String termName){
        StringBuilder sql = new StringBuilder(this.querySql());
        sql.append(" from bi_remain_term term ");
        sql.append(" where term.term_name=:termName ");
        Map<String,String> params = new HashMap<String,String>();
        params.put("termName",termName);
        return this.getBySQLAliasToBean(sql.toString(),params,BiRemainTermVO.class);
    }
    /**
    *@Description 根据id查询剩余期限维护信息
    *@Param id
    *@return com.bytter.biz.cms.basedata.vo.BiRemainTermVO
    *@Date 2020/1/11 12:15
    *@Author fusb
    */
    public BiRemainTermVO getBiRemainTermVOById(String id) {
        StringBuilder sql = new StringBuilder(this.querySql());
        sql.append(" from bi_remain_term term ");
        sql.append(" where term.id=:id ");
        Map<String,String> params = new HashMap<String,String>();
        params.put("id",id);
        return this.getBySQLAliasToBean(sql.toString(),params,BiRemainTermVO.class);
    }
    /**
     *@Description 根据剩余期限维护主表信息删除字表信息
     *@Param id
     *@return void
     *@Date 2020/1/10 18:31
     *@Author fusb
     */
    public void deleteRemainTerm (String id){
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from bi_remain_term where id=:id");
        Map<String,String> params = new HashMap<String,String>();
        params.put("id",id);
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }
    /**
     *@Description 根据剩余期限维护主表排序值
     *@Param
     *@return Integer
     *@Date 2020/1/10 18:31
     *@Author fusb
     */
    public Integer getRemainTermSort(){
        StringBuilder sql = new StringBuilder();
        sql.append("select max(b.sort) as SORT from bi_remain_term b ");
        Map<String, BigDecimal> map = this.getBySQL(sql.toString(),null);
        if (EmptyUtils.isEmpty(map)) {
            return 1;
        }
        return map.get("SORT").intValue() + 1;
    }
}
