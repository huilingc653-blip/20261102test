package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranGroup;
import com.bytter.biz.foreign.vo.FexTranGroupVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @Title: Dao
 * @Description: 交易组
 * @author huangx
 * @date 2020-02-27 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexTranGroupDao extends BaseDao<FexTranGroup,String> {
    /**
     * 交易组表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql=new StringBuilder();
        sql.append("select t.id as id,t.bill_no as billNo,t.group_Name as groupName,t.group_Desc as groupDesc,t.pro_Id as proId,f.pro_Name as proName,");
        sql.append(" t.rule_Id as ruleId,r.rule_Name as ruleName,t.valid_Sign as validSign,t.rmk as rmk,t.main_Info as mainInfo,");
        sql.append(" t.create_By as createBy, t.create_Date as createDate, t.update_By as updateBy, t.update_Date as updateDate ");
        sql.append(" from fex_tran_group t left join fex_product f on t.pro_id = f.id ");
        sql.append(" left join fex_tran_group_rule r on t.rule_Id = r.id");
        sql.append(" where 1=1");
        return sql.toString();
    }

    /**
     * 交易组列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexTranGroupVO.class, "t", false);
    }

    /**
     * 根据ID获取交易组信息
     * @param id
     * @return
     */
    public FexTranGroupVO getByIdUnion(String id){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexTranGroupVO.class);
    }

    /**
     * 根据交易组名称获取交易组信息
     * @param groupName
     * @param id
     * @return
     */
    public List<FexTranGroupVO> listByGroupName(String groupName, String id) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.group_Name=:groupName");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("groupName", groupName);
        if(StringUtils.isNotBlank(id)){
            sql.append("  and t.id<>:id");
            params.put("id", id);
        }
        return listBySQLAliasToBean(sql.toString(), params, FexTranGroupVO.class);
    }

    /**
     * 根据交易类型获取有效的交易组
     * @param tranType
     * @param proId
     * @return
     */
    public List<FexTranGroupVO> listTranGroup(String tranType, String proId) {
        StringBuilder sql=new StringBuilder("select distinct t.id,t.group_name from fex_tran_group t");
        sql.append(" left join fex_tran_group_rule r on t.rule_id = r.id");
        sql.append(" left join fex_tran_group_rule_dtl d on r.id = d.rule_id");
        sql.append(" where t.valid_sign=:validSign");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("validSign", CommonConstant.YES);
        if(StringUtils.isNotBlank(tranType)){
            sql.append(" and d.tran_type=:tranType");
            params.put("tranType", tranType);
        }
        if(StringUtils.isNotBlank(proId)){
            sql.append(" and d.pro_Id=:proId");
            params.put("proId", proId);
        }
        return listBySQLAliasToBean(sql.toString(), params, FexTranGroupVO.class);
    }
}
