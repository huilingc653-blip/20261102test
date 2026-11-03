package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpTemplate;
import com.bytter.biz.transfer.swap.vo.ErpTemplateSourceVO;
import com.bytter.check.dto.ErpDataTypeDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/23.
 */
@Repository
public class ErpTemplateDao extends BaseDao<ErpTemplate,String> {
    public List<ErpTemplateSourceVO> selectSourceByCode(ErpDataTypeDTO erpDataTypeDTO) {
        String code = erpDataTypeDTO.getKeyField();
        String name = erpDataTypeDTO.getValueField();
        String table = erpDataTypeDTO.getTableName();
        String where = erpDataTypeDTO.getQueryCondition();
        String groupCode = erpDataTypeDTO.getCode();
        String sql = "select "+code+" as code,"+name+" as name,'"+groupCode+"' as groupCode from "+table;
        if(StringUtils.isNotBlank(where)){
            sql+= " where "+ where;
        }
        return listBySQLAliasToBean(sql,null,ErpTemplateSourceVO.class);
    }
    /**
     * 获取所有的已生效的补录配置
     * @return
     */
    public List<ErpTemplateSourceVO> selectDataSource() {
        String sql = "select field_code as code,data_source as name,type as groupCode from erp_inter_config_fill where valid_sign ='1'";
        return listBySQLAliasToBean(sql,null,ErpTemplateSourceVO.class);
    }
    /**
     * 跳转弹窗选择数据加载列表
     * @return
     */
    public DataTablesOutput inputDataTable(DataTablesInput dataTablesInput,ErpDataTypeDTO erpDataTypeDTO,String inputId) {
        String code = erpDataTypeDTO.getKeyField();
        String name = erpDataTypeDTO.getValueField();
        String table = erpDataTypeDTO.getTableName();
        String where = erpDataTypeDTO.getQueryCondition();
        String groupCode = erpDataTypeDTO.getCode();
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String sName = (String)formdata.get("likes_name");
        String sCode = (String)formdata.get("likes_code");
        StringBuffer sql = new StringBuffer();
        sql.append("select "+code+" as code,"+name+" as name,'"+groupCode+"' as groupCode,'"+inputId+"' as inputId from "+table+"");
        //避免erpDataType.getCondition()也存在“1=1”，执行sql会报错，导致页面错乱
        if(StringUtils.isNotEmpty(where)){
            sql.append(" where ").append(where);
        }else{
            sql.append(" where 1=1 ");
        }
        if(StringUtils.isNotBlank(sName)){
            sql.append(" and "+name+" like '%"+sName+"%'");
        }
        if(StringUtils.isNotBlank(sCode)){
            sql.append(" and "+code+" like '%"+sCode+"%'");
        }
        return getDynamicBySQL(dataTablesInput,sql.toString() , ErpTemplateSourceVO.class, "", false);
    }
    /**
     * 根据弹窗控件的code找到对应的name
     * @param code 对应值
     * @return
     */
    public Object getInputDate(String code,ErpDataTypeDTO erpDataTypeDTO) {
        String name = erpDataTypeDTO.getValueField();
        String codeField = erpDataTypeDTO.getKeyField();
        String table = erpDataTypeDTO.getTableName();
        String sql = "select "+name+" from "+table+" where "+codeField+"=:code";
        Map<String,String> map = new HashMap<>();
        map.put("code",code);
        List<Map<Object,Object>> vos = listBySQL(sql, map);
        if(vos.size()>0){
            return vos.get(0).get(name.toUpperCase());
        }
        return "";
    }
    /**
     * 校验该模板类型下是否存在模板
     * @param id
     */
    public boolean checkDateByTemplateTypeId(String id) {
        String hql = " from ErpTemplate where templateTypeId=?0";
        List<ErpTemplate> list = this.listByHQL(hql, id);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }
    /**
     * 校验该模板否存被使用
     * @param id
     */
    public boolean checkDateByTemplateId(String id) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        String sql = "select * from Erp_Template_Store where template_id=:id";
        List list = this.listBySQL(sql, map);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }
}
