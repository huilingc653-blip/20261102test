package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.domain.vo.ConTypeFieldVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @Title: 合同公共功能数据操作类
 * @Package com.bytter.biz.contract.dao
 * @author: zhengg
 * @date: 2019-11-6 下午2:19:43
 * @version V1.0
 */
@Repository
public class ConCommonDao extends BaseDao<T, String> {
    /**
     * 选择合同 获取合同 table列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<ConInfoVO> getConSelectData(DataTablesInput input, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("ci.ID,ci.con_no as conNo,ci.con_name as conName,ci.corp_id as corpId,ci.sign_type as signType,ci.type_id as typeId,");
        sql.append("ci.begin_date as beginDate,ci.end_date as endDate,ci.DRAFT_DATE as draftDate,ci.SIGN_DATE as signDate,");
        sql.append("ci.status as status,ci.attachment_Id as attachmentId,ct.CON_NAME as typeName, ");
        sql.append("ci.corp_code as corpCode,ci.corp_name as corpName ");
        sql.append(" from CON_INFO ci ");
        sql.append(" left join CON_TYPE ct on ci.type_id = ct.id ");
        sql.append(" WHERE ci.status = :status ");
        Map<String, Object> params = new HashMap<>();
        params.put("status", ConConstant.CON_INFO_STATUS_50);
        String corpId = input.getFormdata().get("corpId") == null ? null : input.getFormdata().get("corpId").toString();
        if (StringUtils.isNotEmpty(corpId)) {
            String[] corpSelectList = StringUtils.split(corpId, ",");
            String corpInSql = SqlUtil.getInSqlByParams(corpSelectList, "ci.corp_id", params);
            sql.append(" and ").append(corpInSql).append("  ");
        } else if (ListUtils.isNotNullOrEmpty(corpAuthList)) {
            String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "ci.corp_id", params);
            sql.append(" and ").append(corpAuthSql).append("  ");
        } else {
            sql.append(" and 1!=1 ");
        }
        // 针对业务选合同的场景需要排除掉已选过的合同
        if (StringUtils.isNotEmpty(input.getFormdata().get("isOutSystem"))) {
            sql.append(" and not exists(select 1 from Con_Bus_Hook cbh where ci.id=cbh.con_Id and cbh.VALID_SIGN=:hookValidSign) ");
            params.put("hookValidSign", ConConstant.CON_BUSHOOK_VALID_SIGN_1);
        }
        input.addOrder("conName", true);
        return getDynamicBySQL(input, sql.toString(), ConInfoVO.class, "", false, params);
    }
    /**
     * 获取合同明细信息和对应实体字段
     *
     * @param input
     * @return
     */
    public List<ConTypeFieldVO> getConTypeFieldVOAndPdDtlCode(String typeId, String pdCode) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(
                "ctf.field_name as fieldName,ctf.field_type as fieldType,ctf.required_sign as requiredSign,ctf.sort_num as sortNum,cpmd.pd_dtlcode as pdDtlcode ");
        sql.append(" from CON_TYPE_FIELD ctf ");
        sql.append("left join con_pd_map cpm on ctf.type_id=cpm.type_id ");
        sql.append("left join con_pd_mapdtl cpmd on cpm.id=cpmd.map_id and cpmd.type_dtlid=ctf.id ");
        sql.append("where ctf.type_id=:typeId and cpm.pd_code=:pdCode and ctf.valid_sign=:validSign ");
        sql.append("order by ctf.sort_num asc ");
        Map<String, Object> params = new HashMap<>();
        params.put("typeId", typeId);
        params.put("pdCode", pdCode);
        params.put("validSign", ConConstant.CON_VALID_SIGN_1);
        return this.listBySQLAliasToBean(sql.toString(), params, ConTypeFieldVO.class);
    }
    /**
     * 根据单位id获取此单位下有没有处理中的合同
     *
     * @param corpId
     * @return 有效合同笔数
     */
    public int getConInfoByCorpId(String corpId) {
        String sql = "select count(*) from CON_INFO where corp_id=:corpId and STATUS not in (:status)";
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("status", new Integer[] { ConConstant.CON_INFO_STATUS_90, ConConstant.CON_INFO_STATUS_99 });
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
}
