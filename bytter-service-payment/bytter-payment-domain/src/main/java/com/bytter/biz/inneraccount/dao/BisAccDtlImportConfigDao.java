package com.bytter.biz.inneraccount.dao;
import com.bytter.biz.inneraccount.entity.BisAccDtlImportConfig;
import com.bytter.biz.inneraccount.vo.BisAccDtlImportConfigVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: 离线流水智能导入配置表Dao
 * @Description: BIS_ACC_DTL_IMPORT_CONFIG
 * @date 2020-07-31 15:02:30
 */
@Repository
public class BisAccDtlImportConfigDao extends BaseDao<BisAccDtlImportConfig, String> {
    /**
     * 外汇产品表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.bank_Type_Id as bankTypeId,b.file_Type as fileType,b.dtl_Start_Line as dtlStartLine, b.read_End_Content as readEndContent ,");
        sql.append(" b.cd_Sign as cdSign,b.date_Row as dateRow,");
        sql.append(" b.dtl_Separator as dtlSeparator,b.create_By as createBy, b.create_Date as createDate, b.update_By as updateBy, b.update_Date as updateDate  ");
        sql.append(" from bis_acc_dtl_import_config b ");
        sql.append(" where 1=1");
        return sql.toString();
    }
    /**
     * 列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisAccDtlImportConfigVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlImportConfigVO.class, "", false);
    }
    /**
     * 根据ID获取离线流水智能导入配置信息
     *
     * @param id
     * @return
     */
    public BisAccDtlImportConfigVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and b.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisAccDtlImportConfigVO.class);
    }
    /**
     * 根据银行类型和文件类型获取导入配置信息
     *
     * @param bankTypeId
     * @param fileType
     * @param id
     * @return
     */
    public List<BisAccDtlImportConfigVO> listByBankTypeIdAndFileType(String bankTypeId, String fileType, String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and b.bank_Type_Id=:bankTypeId and b.file_Type=:fileType");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("bankTypeId", bankTypeId);
        params.put("fileType", fileType);
        if (StringUtils.isNotBlank(id)) {
            sql.append("  and b.id<>:id");
            params.put("id", id);
        }
        return listBySQLAliasToBean(sql.toString(), params, BisAccDtlImportConfigVO.class);
    }
    /**
     * 查询离线流水导入配置信息
     * @param bisAccDtlImportConfigVO
     * @return
     */
    public List<BisAccDtlImportConfigVO> listByUnion(BisAccDtlImportConfigVO bisAccDtlImportConfigVO){
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.bank_Type_Id as bankTypeId,b.file_Type as fileType,b.dtl_Start_Line as dtlStartLine, b.read_End_Content as readEndContent ,");
        sql.append(" b.dtl_Separator as dtlSeparator,b.create_By as createBy, b.create_Date as createDate, b.update_By as updateBy, b.update_Date as updateDate,  ");
        sql.append(" d.id as ricdId,d.field_Key as fieldKey,d.start_Val as startVal,d.end_Val as endVal");
        sql.append(" from bis_acc_dtl_import_config b left join bis_acc_dtl_import_configdtl d on b.id=d.ic_id");
        sql.append(" where 1=1");
        Map<String, Object> params = new HashMap<String, Object>();
        if(!StringUtils.isBlank(bisAccDtlImportConfigVO.getFileType())){
            sql.append(" and b.file_Type=:fileType");
            params.put("fileType",bisAccDtlImportConfigVO.getFileType());
        }
        if(!StringUtils.isBlank(bisAccDtlImportConfigVO.getBankTypeId())){
            sql.append(" and b.bank_Type_Id=:bankTypeId");
            params.put("bankTypeId",bisAccDtlImportConfigVO.getBankTypeId());
        }
        if(!StringUtils.isBlank(bisAccDtlImportConfigVO.getId())){
            sql.append(" and b.id=:id");
            params.put("id",bisAccDtlImportConfigVO.getId());
        }
        return this.listBySQLAliasToBean(sql.toString(), params, BisAccDtlImportConfigVO.class);
    }
}
