package com.bytter.system.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.system.api.vo.ExternalCorpBankAccDetailDto;
import com.bytter.system.entity.ExternalCorp;
import com.bytter.system.vo.ExternalCorpBankAccDetailVO;
import com.bytter.system.vo.ExternalCorpBankAccVO;
import com.bytter.system.vo.ExternalCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 客商资料Dao
 *
 * @author zhoul on 2017-06-21
 */
@Repository
public class ExternalCorpDao extends BaseDao<ExternalCorp, String> implements Serializable {
    public IPageResult listByExternalDetail(IParameterInput input,String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        IParameter parameter = input.getParameter();
        String name = parameter.get("name");
        String externalAcc = parameter.get("externalAcc");
        String bwType = parameter.get("bwType");
        String corpId = parameter.get("corpId");
        //类型 0 国外 1 国内 2 国内外全部
        String fex = StrUtil.toStringOrNull(parameter.get("fex"));
        //是否展示没有银行账户的客商 1-是
        String flag = parameter.get("flag");
        StringBuilder sql = new StringBuilder();
        sql.append("select e.id,e.name as name, e.name_en as nameEn, ");
        sql.append(" ecba.bank as bank, ");
        sql.append(" ecba.bank_type as bankType, ");
        sql.append(" ecba.external_acc as externalAcc, ");
        sql.append(" ecba.bank_sourceCode as bankSourceCode, ");
        sql.append(" ecba.acc_province as accProvince, ");
        sql.append(" ecba.acc_city as accCity,");
        sql.append(" ap.prov_input_name as accProvinceFexName, ");
        sql.append(" ac.name as accCityFexName, ");
        sql.append(" ap.prov_input_name as accProvinceName, ");
        sql.append(" ac.name as accCityName, ");
        sql.append(" case when ecba.is_default_acc is not null then ecba.is_default_acc else '0' end as isDefaultAcc, ");
        sql.append(" ecba.remark as remarks, ");
        sql.append(" e.province as province, ");
        sql.append(" e.city as city, ");
        sql.append(" p.prov_input_name as provinceFexName, ");
        sql.append(" c.name as cityFexName, ");
        sql.append(" e.address as address, ");
        sql.append(" e.address_en as addressEn, ");
        sql.append(" s.bank_add as accAddress, ");
        sql.append(" e.bw_type as bwType, ");
        sql.append(" e.soc_code as socCode, ");
        sql.append(" e.external_type as externalType ");
        sql.append(" from (select ec.id, ec.status, ec.name, ec.name_en, ec.address_en, ec.audit_status, ec.province, ec.city, ec.address, ec.bw_type, ec.soc_code,ec.external_type ");
        sql.append(" from sys_externalcorp_corp ecc ");
        sql.append(" left join sys_external_corp ec on ecc.externalcorp_id = ec.id where 1 = 1 ");
        if (StringUtils.isNotBlank(fex) && "0".equals(fex)) {
            sql.append(" and ec.is_native = 0 ");
        } else if (StringUtils.isNotBlank(fex) && "2".equals(fex)) {
            //类型=2 国内外全部：不添加 is_native 过滤条件
        } else {
            sql.append(" and (ec.is_native = 1 or ec.is_native is null) ");
        }
        if (StringUtils.isNotBlank(corpId)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpId.split(","), "ecc.corp_id", true));
        }
        sql.append(" union select ec2.id, ec2.status, ec2.name, ec2.name_en, ec2.address_en, ec2.audit_status, ec2.province, ec2.city, ec2.address, ec2.bw_type, ec2.soc_code,ec2.external_type ");
        sql.append(" from sys_external_corp ec2  left join sys_externalcorp_corp ecc2 on ec2.id = ecc2.externalcorp_id ");
        sql.append(" where (ec2.is_common = 1 or ec2.is_common is null) and ecc2.id is null ");
        if (StringUtils.isNotBlank(fex) && "0".equals(fex)) {
            sql.append(" and ec2.is_native = 0 ");
        } else if (StringUtils.isNotBlank(fex) && "2".equals(fex)) {
            //类型=2 国内外全部：不添加 is_native 过滤条件
        } else {
            sql.append(" and (ec2.is_native = 1 or ec2.is_native is null) ");
        }
        sql.append(" ) e ");
        if (StringUtils.isNotBlank(flag) && "1".equals(flag)){
            sql.append(" left join sys_external_corp_bankacc ecba on ecba.external_corp_id = e.id ");
        }else {
            sql.append(" inner join sys_external_corp_bankacc ecba on ecba.external_corp_id = e.id ");
        }
        sql.append(" left join bt_swift_info s on ecba.bank_sourcecode = s.swift_code ");
        sql.append(" left join sys_region p on p.id = e.province");
        sql.append(" left join sys_region c on c.id = e.city");
        sql.append(" left join sys_region ap on ap.id = ecba.acc_province");
        sql.append(" left join sys_region ac on ac.id = ecba.acc_city");
        sql.append(" where e.status = 1 and e.audit_status = 95");
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and e.name like :name ");
            map.put("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(externalAcc)) {
            sql.append(" and ecba.external_Acc like :externalAcc ");
            map.put("externalAcc", "%" + externalAcc + "%");
        }
        if (StringUtils.isNotBlank(bwType)) {
            sql.append(" and e.bw_type = :bwType ");
            map.put("bwType", bwType);
        }
        if(StringUtils.isNotEmpty(orderBySql)){
            sql.append(orderBySql);
        }else {
            sql.append(" order by e.name asc ");
        }
        return getPageResultBySQL(input, sql.toString(), map, ExternalCorpBankAccDetailVO.class);
    }

    public ExternalCorpBankAccDetailDto listByExternalDetailForBill(IParameterInput input) {
        Map<String, Object> map = new HashMap<String, Object>();
        IParameter parameter = input.getParameter();
        String name = parameter.get("name");
        String externalAcc = parameter.get("externalAcc");
        String bwType = parameter.get("bwType");
        String corpId = parameter.get("corpId");
        //类型 0 国外 1 国内 2 国内外全部
        String fex = StrUtil.toStringOrNull(parameter.get("fex"));
        StringBuilder sql = new StringBuilder();
        sql.append("select e.id,e.name as name, e.name_en as nameEn, ");
        sql.append(" ecba.bank as bank, ");
        sql.append(" ecba.bank_type as bankType, ");
        sql.append(" ecba.external_acc as externalAcc, ");
        sql.append(" ecba.bank_sourceCode as bankSourceCode, ");
        sql.append(" ecba.acc_province as accProvince, ");
        sql.append(" ecba.acc_city as accCity,");
        sql.append(" ap.name_zh as accProvinceFexName, ");
        sql.append(" ac.name_zh as accCityFexName, ");
        sql.append(" ecba.is_default_acc as isDefaultAcc, ");
        sql.append(" ecba.remark as remarks, ");
        sql.append(" e.province as province, ");
        sql.append(" e.city as city, ");
        sql.append(" p.name_zh as provinceFexName, ");
        sql.append(" c.name_zh as cityFexName, ");
        sql.append(" e.address as address, ");
        sql.append(" e.address_en as addressEn, ");
        sql.append(" s.bank_add as accAddress, ");
        sql.append(" e.bw_type as bwType, ");
        sql.append(" e.soc_code as socCode ");
        sql.append(" from (select ec.id, ec.status, ec.name, ec.name_en, ec.address_en, ec.audit_status, ec.province, ec.city, ec.address, ec.bw_type, ec.soc_code ");
        sql.append(" from sys_externalcorp_corp ecc ");
        sql.append(" left join sys_external_corp ec on ecc.externalcorp_id = ec.id where 1 = 1 ");
        if (StringUtils.isNotBlank(fex) && "0".equals(fex)) {
            sql.append(" and ec.is_native = 0 ");
        } else if (StringUtils.isNotBlank(fex) && "2".equals(fex)) {
            //类型=2 国内外全部：不添加 is_native 过滤条件
        } else {
            sql.append(" and (ec.is_native = 1 or ec.is_native is null) ");
        }
        if (StringUtils.isNotBlank(corpId)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpId.split(","), "ecc.corp_id", true));
        }
        sql.append(" union select ec2.id, ec2.status, ec2.name, ec2.name_en, ec2.address_en, ec2.audit_status, ec2.province, ec2.city, ec2.address, ec2.bw_type, ec2.soc_code ");
        sql.append(" from sys_external_corp ec2  left join sys_externalcorp_corp ecc2 on ec2.id = ecc2.externalcorp_id ");
        sql.append(" where (ec2.is_common = 1 or ec2.is_common is null) and ecc2.id is null ");
        if (StringUtils.isNotBlank(fex) && "0".equals(fex)) {
            sql.append(" and ec2.is_native = 0 ");
        } else if (StringUtils.isNotBlank(fex) && "2".equals(fex)) {
            //类型=2 国内外全部：不添加 is_native 过滤条件
        } else {
            sql.append(" and (ec2.is_native = 1 or ec2.is_native is null) ");
        }
        sql.append(" ) e ");
        sql.append(" inner join sys_external_corp_bankacc ecba on ecba.external_corp_id = e.id ");
        sql.append(" left join bt_swift_info s on ecba.bank_sourcecode = s.swift_code ");
        sql.append(" left join sys_country_cust p on p.id = e.province");
        sql.append(" left join sys_country_cust c on c.id = e.city");
        sql.append(" left join sys_country_cust ap on ap.id = ecba.acc_province");
        sql.append(" left join sys_country_cust ac on ac.id = ecba.acc_city");
        sql.append(" where e.status = 1 and e.audit_status = 95");
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and e.name like :name ");
            map.put("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(externalAcc)) {
            sql.append(" and ecba.external_Acc like :externalAcc ");
            map.put("externalAcc", "%" + externalAcc + "%");
        }
        if (StringUtils.isNotBlank(bwType)) {
            sql.append(" and e.bw_type = :bwType ");
            map.put("bwType", bwType);
        }
        sql.append(" order by e.name asc ");
        IPageResult<ExternalCorpBankAccDetailVO> pageResultBySQL = getPageResultBySQL(input, sql.toString(), map, ExternalCorpBankAccDetailVO.class);
        ExternalCorpBankAccDetailDto dto = new ExternalCorpBankAccDetailDto();
        if (pageResultBySQL != null && !CollectionUtils.isEmpty(pageResultBySQL.getList())){
            ExternalCorpBankAccDetailVO externalCorpBankAccDetailVO = pageResultBySQL.getList().get(0);

            BeanUtils.copyNotNullProperties(externalCorpBankAccDetailVO, dto);

        }
        return  dto;
    }

    public ExternalCorp findByCorpIdAndName(String corpId, String name) {
        return findByCorpIdAndName(name);
    }
    public ExternalCorp findByCorpIdAndName(String name) {
        List<ExternalCorp> externalCorps = this.listBy("name", name);
        if (EmptyUtils.isEmptyList(externalCorps)) {
            return null;
        } else {
            return externalCorps.get(0);
        }
    }
    public List<ExternalCorp> findExternalCorpsByCorpIdAndName(String name) {
        List<ExternalCorp> externalCorps = this.listBy("name", name);
        return externalCorps;
    }
    /**
     * 根据条件查询
     *
     * @param corpId 单位ID
     * @param code   编码
     * @return
     */
    public ExternalCorp getByCorpIdAndCode(String corpId, String code) {
        return getByCorpIdAndCode(code);
    }
    public ExternalCorp getIdByCorpIdAndCode(String corpId, String code) {
        String sql = " select ec.id as id, ec.name_en as nameEn, ec.address_en as addressEn from sys_external_corp ec  left join sys_externalcorp_corp ecc on ecc.externalcorp_id = ec.id " +
                " where ec.code = :code and ecc.corp_id = :corpId ";
        Map<String, String> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("code", code);
        List<ExternalCorp> externalCorps = this.listBySQLAliasToBean(sql, params, ExternalCorp.class);
        if (EmptyUtils.isEmptyList(externalCorps)) {
            return null;
        } else {
            return externalCorps.get(0);
        }
    }
    public ExternalCorp findIdByCorpIdAndName(String corpId, String name) {
        String sql = " select ec.id as id, ec.name_en as nameEn, ec.address_en as addressEn from sys_external_corp ec  left join sys_externalcorp_corp ecc on ecc.externalcorp_id = ec.id " +
                " where ec.name = :name and ecc.corp_id = :corpId ";
        Map<String, String> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("name", name);
        List<ExternalCorp> externalCorps = this.listBySQLAliasToBean(sql, params, ExternalCorp.class);
        if (EmptyUtils.isEmptyList(externalCorps)) {
            return null;
        } else {
            return externalCorps.get(0);
        }
    }
    public List<ExternalCorp> findExternalCorpLikeName(String name) {
        String sql = "from ExternalCorp where name like :name ";
        Map<String, String> params = new HashMap<>();
        params.put("name", "%" + name + "%");
       return this.listByHQL(sql, params);
    }
    /**
     * 根据条件查询
     *
     * @param code 编码
     * @return
     */
    public ExternalCorp getByCorpIdAndCode(String code) {
        List<ExternalCorp> externalCorps = this.listBy("code", code);
        if (EmptyUtils.isEmptyList(externalCorps)) {
            return null;
        } else {
            return externalCorps.get(0);
        }
    }
    /**
     * 根据ID查询
     *
     * @param id
     * @return
     * @description last update by lifh 查询结果添加IS_NATIVE字段
     */
    public ExternalCorpVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select ");
        sql.append(" t.id,t.code,t.name,t.abbreviate,t.external_type, t.supplier_id, s.supplier_name, t.province,t.city,t.link_man,t.tel,t.fax,t.email, ");
        sql.append(" t.address,t.status,t.source_system,t.is_common,t.is_native,t.attachment_id,p.name_zh as provinceFexName, ");
        sql.append(" c.name_zh as cityFexName,t.bw_type as bwType, t.soc_code as socCode, ");
        sql.append(" t.name_en as nameEn, t.address_en as addressEn, t.address1 as address1, t.address2 as address2, ");
        sql.append(" t.province_state as provinceState, t.zip_code as zipCode from sys_external_corp t  ");
        sql.append(" left join sys_supplier_type s on t.supplier_id = s.id");
        sql.append(" left join sys_country_cust p on p.id = t.province");
        sql.append(" left join sys_country_cust c on c.id = t.city");
        sql.append(" where t.id = :id");
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, ExternalCorpVO.class);
    }

    public DataTablesOutput listExternalCorp(DataTablesInput input,String joinSql) {
        StringBuilder sql = new StringBuilder();
        String auditStatus = (String) input.getFormdata().get("auditStatus");
        String commitBy = (String) input.getFormdata().get("commitBy");
        String proId = (String) input.getFormdata().get("eqs_proId");
        input.getFormdata().remove("eqs_proId");
        sql.append("select distinct ec.code as code, ");
        sql.append(" ec.id as id, ");
        sql.append(" ec.name as name, ");
        sql.append(" ec.address_en as addressEn, ");
        sql.append(" ec.abbreviate as abbreviate, ");
        sql.append(" ec.is_native as isNative, ");
        sql.append(" ec.source_System as sourceSystem, ");
        sql.append(" ec.status as status, ec.audit_status as auditStatus, ec.is_common as isCommon, ");
        sql.append(" ec.external_type as externalType, ec.supplier_id as supplierId, st.supplier_name as supplierName,ec.bw_type as bwType, ec.soc_code socCode, ");
        sql.append(" ec.name_en as nameEn ");
        sql.append("from sys_external_corp ec ");
        sql.append(" left join sys_supplier_type st on st.id = ec.supplier_id ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNoneBlank(auditStatus)) {
            sql.append(" and ec.audit_status = '" + auditStatus + "' ");
        }
        //客商资料审核列表，只能审核非当前用户提交的资料
        if (StringUtils.isNoneBlank(commitBy)) {
            sql.append(" and (ec.commit_by != '" + commitBy + "' or ec.commit_by is null)");
        }
        String corpId = (String) input.getFormdata().get("corpId");
        if(EmptyUtils.isNotEmpty(joinSql) || EmptyUtils.isNotEmpty(corpId)){
            sql.append(" and (exists (select distinct ecc.externalcorp_id from sys_externalcorp_corp ecc ");
            sql.append(" where 1 = 1 ");
            if(EmptyUtils.isNotEmpty(joinSql)){
                sql.append(" and " + joinSql );
            }
            if(EmptyUtils.isNotEmpty(corpId)){
                String corpSql = SqlUtil.getInSql(corpId.split(","), "ecc.corp_id", true);
                sql.append(" and ");
                sql.append(corpSql);
            }
            sql.append(" and ec.id = ecc.externalcorp_id )");
            if(EmptyUtils.isEmpty(corpId)){
                sql.append(" or not exists (select 1 from sys_externalcorp_corp ecc where ec.id = ecc.externalcorp_id )");
            }
            sql.append(" )");
        }

        if(EmptyUtils.isNotEmpty(proId)){
            sql.append(" and exists (select distinct secqp.external_corp_id from sys_external_corp_quick_pay secqp ");
            sql.append(" where 1=1 ");
            sql.append(" and secqp.pro_id = '").append(proId).append("'");
            sql.append(" and ec.id = secqp.external_corp_id) ");
        }

        return this.getDynamicBySQL(input, sql.toString(), ExternalCorpVO.class, "ec", false);
    }

    /**
     * 根据单位查询绑定的客商,从客商的ID集合中得到客商实体
     *
     * @param externalCorpIds
     * @return
     */
    public List<ExternalCorp> listExternalCorpByIds(List<String> externalCorpIds) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<>();
        sql.append(" select e.code as code,e.id as id, e.name as name, e.name_en as nameEn, ");
        sql.append(" e.abbreviate as abbreviate, e.source_System as source_System, e.status as status,e.bw_type as bwType ");
        sql.append(" from sys_external_corp e where 1 = 1 ");
        if (!EmptyUtils.isEmptyList(externalCorpIds)) {
            sql.append(" and (").append(SqlUtil.getInSql(externalCorpIds, "e.id", true)).append(" and e.is_common <> 1 )");
            sql.append(" or e.is_common = 1 ");
        } else {
            sql.append(" and e.is_common = 1 ");
        }
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorp.class);
    }
    /**
     * 下拉框客商组件查询
     *
     * @param corpId
     * @return
     * @author zuoy 2018-07-04
     */
    public IPageResult listByInputExternalCorp(IParameterInput input, String corpId, String seach) {
        StringBuilder sql = new StringBuilder();
        //类型 0 国外 1 国内 2 国内外全部
        String fex = StrUtil.toStringOrNull(input.getParameter().get("fex"));
        HashMap<String, String> params = new HashMap<>();
        sql.append("select (ecba.external_acc || ' ' || e.name) as inputExternalText, (ecba.external_acc || '_' || e.name) as inputExternalValue, ");
        sql.append("  e.name as name, e.name_en as nameEn, e.address_en as addressEn, ecba.bank as bank, ecba.bank_type as bankType, ecba.external_acc as externalAcc, ");
        sql.append(" ecba.bank_sourceCode as bankSourceCode, ecba.acc_province as accProvince, ecba.acc_city as accCity, ecba.is_default_acc as isDefaultAcc, ");
        sql.append(" ap.name_zh as accProvinceFexName, ");
        sql.append(" ac.name_zh as accCityFexName, ");
        sql.append(" e.province as province, ");
        sql.append(" e.city as city, ");
        sql.append(" p.name_zh as provinceFexName, ");
        sql.append(" c.name_zh as cityFexName, ");
        sql.append(" e.address as address, ");
        sql.append(" s.bank_add as accAddress ");
        sql.append(" from (select ec.id, ec.status, ec.name, ec.name_en, ec.address_en, ec.audit_status, ec.province, ec.city, ec.address ");
        sql.append(" from Sys_Externalcorp_Corp ecc ");
        sql.append(" left join sys_external_corp ec on ecc.externalcorp_id = ec.id where 1 = 1 ");
        if (StringUtils.isNotBlank(fex) && "0".equals(fex)) {
            sql.append(" and ec.is_native = 0 ");
        } else if (StringUtils.isNotBlank(fex) && "2".equals(fex)) {
            //类型=2 国内外全部：不添加 is_native 过滤条件
        } else {
            sql.append(" and (ec.is_native = 1 or ec.is_native is null) ");
        }
        if (StringUtils.isNotBlank(corpId)) {
            sql.append(" and ecc.corp_Id = :corpId ");
        }
        sql.append(" union select ec2.id, ec2.status, ec2.name, ec2.name_en, ec2.address_en, ec2.audit_status, ec2.province, ec2.city, ec2.address ");
        sql.append(" from sys_external_corp ec2 ");
        sql.append(" where (ec2.is_common = 1 or ec2.is_common is null) ");
        if (StringUtils.isNotBlank(fex) && "0".equals(fex)) {
            sql.append(" and ec2.is_native = 0 ");
        } else if (StringUtils.isNotBlank(fex) && "2".equals(fex)) {
            //类型=2 国内外全部：不添加 is_native 过滤条件
        } else {
            sql.append(" and (ec2.is_native = 1 or ec2.is_native is null) ");
        }
        sql.append(" ) e ");
        sql.append(" inner join sys_external_corp_bankacc ecba on ecba.external_corp_id = e.id ");
        sql.append(" left join bt_swift_info s on ecba.bank_sourcecode = s.swift_code ");
        sql.append(" left join sys_country_cust p on p.id = e.province");
        sql.append(" left join sys_country_cust c on c.id = e.city");
        sql.append(" left join sys_country_cust ap on ap.id = ecba.acc_province");
        sql.append(" left join sys_country_cust ac on ac.id = ecba.acc_city");
        sql.append(" where e.status = 1 and e.audit_status = 95");
        params.put("corpId", corpId);
        if (StringUtils.isNotBlank(seach)) {
            sql.append(" and (e.name like :name ");
            params.put("name", "%" + seach + "%");
            sql.append(" or ecba.external_Acc like :externalAcc) ");
            params.put("externalAcc", "%" + seach + "%");
        }
        sql.append(" order by e.name asc ");
        return getPageResultBySQL(input, sql.toString(), params, ExternalCorpBankAccDetailVO.class);
    }
    /**
     * 根据id集合获取数据
     *
     * @param externalCorpIds
     * @return
     */
    public List<ExternalCorp> getExternalCorpByIds(List<String> externalCorpIds) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<>();
        sql.append(" select ");
        sql.append(" e.id as id                ,");
        sql.append(" e.name as name                ,");
        sql.append(" e.name_en as nameEn          ,");
        sql.append(" e.abbreviate as abbreviate    ,");
        sql.append(" e.province as province        ,");
        sql.append(" e.city as city                ,");
        sql.append(" e.address as address          ,");
        sql.append(" e.link_Man as linkMan          ,");
        sql.append(" e.tel as tel                  ,");
        sql.append(" e.fax as fax                  ,");
        sql.append(" e.email as email              ,");
        sql.append(" e.source_System as sourceSystem,");
        sql.append(" e.status as status            ,");
        sql.append(" e.audit_Status as auditStatus  ,");
        sql.append(" e.code as code                ,");
        sql.append(" e.is_Common as isCommon        ,");
        sql.append(" e.is_Native as isNative        ,");
        sql.append(" e.external_type as externalType   ,     ");
        sql.append(" e.supplier_Id as supplierId        ,");
        sql.append(" e.attachment_Id as attachmentId    ,    ");
        sql.append(" e.commit_By as commitBy,        ");
        sql.append(" e.bw_type as bwType,        ");
        sql.append(" e.soc_code as socCode,        ");
        sql.append(" e.address1 as address1,        ");
        sql.append(" e.address2 as address2,        ");
        sql.append(" e.province_state as provinceState,        ");
        sql.append(" e.zip_code as zipCode        ");
        sql.append(" from sys_external_corp e where 1 = 1 ");
        sql.append(" and ").append(SqlUtil.getInSql(externalCorpIds, "e.id", true));
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorp.class);
    }
    /**
     * 根据客商表Id和客商银行账号查找客商账号信息
     *
     * @param externalCorpId
     * @param bankAcc
     * @return
     */
    public ExternalCorpBankAccVO getExternalCorpBankAccByIdAndAcc(String externalCorpId, String bankAcc) {
        StringBuilder sql = new StringBuilder();
        sql.append("select e.id as id, e.external_corp_id as externalCorpId,e.external_acc as externalAcc,");
        sql.append(" e.bank as bank, e.bank_sourcecode as bankSourceCode,");
        sql.append(" e.acc_province as accProvince, e.acc_city as accCity , e.remark as remark,s.bank_add as accAddress ");
        sql.append(" from sys_external_corp_bankacc e left join bt_swift_info s on e.bank_sourcecode = s.swift_code where 1=1 ");
        HashMap<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(externalCorpId)) {
            sql.append(" and e.external_corp_id =:externalCorpId ");
            params.put("externalCorpId", externalCorpId);
        }
        if (StringUtils.isNotBlank(bankAcc)) {
            sql.append(" and e.external_acc =:bankAcc");
            params.put("bankAcc", bankAcc);
        }
        return getBySQLAliasToBean(sql.toString(), params, ExternalCorpBankAccVO.class);
    }
    /**
     * 根据ID获取客商
     *
     * @param ids
     * @return
     */
    public List<ExternalCorpVO> listByIds(String[] ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.code,t.name,t.name_en as nameEn,t.abbreviate,t.external_type, t.supplier_id, s.supplier_name, t.province,t.city,t.link_man,t.tel,t.fax,t.email, ");
        sql.append(" t.address,t.status,t.source_system,t.is_common,t.is_native,t.attachment_id,p.name_zh as provinceFexName,c.name_zh as cityFexName,t.bw_type as bwType, ");
        sql.append(" t.soc_code as socCode, t.address1 as address1, t.address2 as address2, t.province_state as provinceState, t.zip_code as zipCode from sys_external_corp t  ");
        sql.append(" left join sys_supplier_type s on t.supplier_id = s.id");
        sql.append(" left join sys_country_cust p on p.id = t.province");
        sql.append(" left join sys_country_cust c on c.id = t.city");
        sql.append(" where 1=1 ");
        sql.append(" and " + SqlUtil.getInSql(ids, "t.id", true));
        HashMap<String, Object> params = new HashMap<String, Object>();
        return listBySQLAliasToBean(sql.toString(), params, ExternalCorpVO.class);
    }
    public DataTablesOutput<ExternalCorpVO> dataTableForSelect(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select DISTINCT ");
        sql.append(" ec.id as id                ,");
        sql.append(" ec.name as name                ,");
        sql.append(" ec.name_en as nameEn          ,");
        sql.append(" ec.abbreviate as abbreviate    ,");
        sql.append(" ec.province as province        ,");
        sql.append(" ec.city as city                ,");
        sql.append(" p.name_zh as provinceFexName   ,");
        sql.append(" c.name_zh as cityFexName       ,");
        sql.append(" ec.address as address          ,");
        sql.append(" ec.address_en as addressEn     ,");
        sql.append(" ec.link_Man as linkMan          ,");
        sql.append(" ec.tel as tel                  ,");
        sql.append(" ec.fax as fax                  ,");
        sql.append(" ec.email as email              ,");
        sql.append(" ec.source_System as sourceSystem,");
        sql.append(" ec.status as status            ,");
        sql.append(" ec.audit_Status as auditStatus  ,");
        sql.append(" ec.code as code                ,");
        sql.append(" ec.is_Common as isCommon        ,");
        sql.append(" ec.is_Native as isNative        ,");
        sql.append(" ec.external_type as externalType   ,     ");
        sql.append(" ec.supplier_Id as supplierId        ,");
        sql.append(" ec.attachment_Id as attachmentId    ,    ");
        sql.append(" ec.commit_By as commitBy,        ");
        sql.append(" ec.bw_type as bwType ,     ");
        sql.append(" sst.supplier_name as supplierName ,     ");
        sql.append(" ec.soc_code as socCode        ");
        sql.append(" from sys_external_corp ec ");
        sql.append(" left join sys_externalcorp_corp ecc on ecc.externalcorp_id = ec.id ");
        sql.append(" left join sys_country_cust p on p.id = ec.province");
        sql.append(" left join sys_country_cust c on c.id = ec.city");
        sql.append(" left join sys_supplier_type sst on sst.id = ec.supplier_id");
        sql.append(" where 1 = 1 ");

        Map<String, Object> params = new HashMap<>();
        String corpId = (String) input.getFormdata().get("corpId");
        if (!StringUtils.isBlank(corpId)) {
            sql.append(" and (ec.is_common='1' or ecc.corp_id in (:corpIds) )");
            params.put("corpIds", corpId.split(","));
        }
        //针对前端选择已选数据筛选过滤 zhengg 20200716
        String isSelct = input.getParameter().get("isSelect");
        String ids = input.getParameter().get("ids");
        String[] idsArray = null;
        if (StringUtils.isNotEmpty(ids)) {
            idsArray = ids.split(",");
        }

        if ("1".equals(isSelct)) {
            input.getFormdata().remove("eqi_status");//查看已选的时候需要把已禁用的已选也要显示
            sql.append(" and ").append(SqlUtil.getInSqlByParams(idsArray, "ec.id", params));
        } else if ("0".equals(isSelct)) {
            sql.append(" and ").append(SqlUtil.getNotInSqlByParams(idsArray, "ec.id", params));
        }
        return this.getDynamicBySQL(input, sql.toString(), ExternalCorpVO.class, "ec", false, params);
    }
    /**
     * 根据ID查询
     *
     * @param userId
     * @return
     * @description
     */
    public DataTablesOutput getAllDatas(DataTablesInput input, String userId) {
        StringBuilder sql = new StringBuilder();
        String commitBy = (String) input.getFormdata().get("commitBy");
        String proId = (String) input.getFormdata().get("eqs_proId");
        input.getFormdata().remove("eqs_proId");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(userId)) {
            commitBy = userId;
        }
        sql.append("select distinct ec.code as code, ");
        sql.append(" ec.id as id, ");
        sql.append(" ec.name as name, ec.name_en as nameEn, ec.address_en as addressEn,");
        sql.append(" ec.abbreviate as abbreviate, ");
        sql.append(" BT_NVL_NUM(ec.is_native,1) as isNative, ");
        sql.append(" ec.source_System as sourceSystem, ");
        sql.append(" ec.status as status, ec.audit_status as auditStatus, ec.is_common as isCommon, ");
        sql.append(" ec.external_type as externalType, ec.supplier_id as supplierId, st.supplier_name as supplierName,ec.bw_type as bwType, ");
        sql.append(" ec.SOC_CODE as socCode ");
        sql.append("from sys_external_corp ec ");
        sql.append(" left join sys_externalcorp_corp ecc on ecc.externalcorp_id = ec.id ");
        sql.append(" left join sys_supplier_type st on st.id = ec.supplier_id ");
        sql.append(" where 1=1 and ec.audit_status = '11' ");
        //客商资料审核列表，只能审核非当前用户提交的资料
        if (StringUtils.isNoneBlank(commitBy)) {
            sql.append(" and (ec.commit_by != '").append(commitBy).append("' or ec.commit_by is null)");
        }
        String isCommon = StrUtil.toStringOrNull(input.getFormdata().get("isCommon"));
        if (StrUtil.isNotBlank(isCommon)) {
            sql.append(" and ec.is_common = ").append(isCommon).append(" ");
        }
        String corpId = (String) input.getFormdata().get("corpId");
        if (!StringUtils.isBlank(corpId)) {
            sql.append(" and (ec.is_common='1' or ecc.corp_id in (:corpIds) )");
            params.put("corpIds", corpId.split(","));
        }
        // 按资金收付产品过滤：产品信息在快捷支付明细表中，不能交给动态条件按 ec.pro_id 拼接。
        if (EmptyUtils.isNotEmpty(proId)) {
            sql.append(" and exists (select 1 from sys_external_corp_quick_pay secqp ");
            sql.append(" where secqp.external_corp_id = ec.id ");
            sql.append(" and secqp.pro_id = :proId) ");
            params.put("proId", proId);
        }
        return this.getDynamicBySQL(input, sql.toString(), ExternalCorpVO.class, "ec", false, params);
    }



    /**
     * 根据供应商类别获取客商信息
     * @param externalCorpId 客商Id
     */
    public List<Map> queryExternalCorpAndCorpId(String externalCorpId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select scc.CORP_ID from sys_external_corp sec ");
        sql.append(" inner join sys_externalcorp_corp scc on sec.id = scc.EXTERNALCORP_ID ");
        sql.append(" where sec.id =:externalCorpId ");
        params.put("externalCorpId", externalCorpId);
        return listBySQL(sql.toString(),params,1);
    }

    public List<ExternalCorp> queryAll(){
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<>();
        sql.append(" select e.code as code,e.id as id, e.name as name, e.name_en as nameEn, ");
        sql.append(" e.abbreviate as abbreviate, e.source_System as source_System, e.status as status,e.bw_type as bwType ");
        sql.append(" from sys_external_corp e where e.STATUS = 1");
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorp.class);
    }

    public List<ExternalCorpDTO> queryInId(List<String> ids){
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        sql.append(" select e.code as code,e.id as id, e.name as name, e.name_en as nameEn ");
        sql.append(" from sys_external_corp e where e.STATUS = 1");
        sql.append(" and e.id in (:ids)");
        params.put("ids",ids);
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpDTO.class);
    }

    public List<ExternalCorpDTO> queryFexExternalCorpByIds(List<String> ids) {
        if (EmptyUtils.isEmpty(ids)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        sql.append(" select e.id as id,e.name as name,e.abbreviate as abbreviate,");
        sql.append(" e.province as province,e.city as city,e.address as address,");
        sql.append(" e.name_en as nameEn,e.address_en as addressEn,e.address1 as address1,e.address2 as address2,");
        sql.append(" e.province_state as provinceState,e.status as status,e.external_type as externalType,e.is_native as isNative ");
        sql.append(" from sys_external_corp e where e.STATUS = 1");
        sql.append(" and e.id in (:ids)");
        params.put("ids", ids);
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpDTO.class);
    }

    /**
     * 根据外汇交易对手数据源ID批量查询展示信息。
     *
     * @param ids 交易对手数据源ID集合
     * @return 交易对手展示信息
     */
    public List<ExternalCorpDTO> queryFexCounterpartyByIds(List<String> ids) {
        if (EmptyUtils.isEmpty(ids)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        sql.append(" select e.id as id,e.name as name,e.name_en as nameEn from sys_external_corp e where e.id in (:ids)");
        sql.append(" union all select c.id as id,c.name as name,c.name_en as nameEn from sys_corp c where c.id in (:ids)");
        sql.append(" union all select i.id as id,i.bank_name as name,null as nameEn from bt_input_bank_info i where i.id in (:ids)");
        sql.append(" union all select s.id as id,s.bank_name as name,null as nameEn from bt_swift_info s where s.id in (:ids)");
        params.put("ids", ids);
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpDTO.class);
    }

    /**
     * 根据名称模糊查询外汇统一交易对手。
     *
     * @param name 交易对手名称
     * @return 交易对手信息
     */
    public List<ExternalCorpDTO> findFexCounterpartyLikeName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        sql.append(" select e.id as id,e.name as name,e.name_en as nameEn from sys_external_corp e");
        sql.append(" where e.name like :name or e.name_en like :name");
        sql.append(" union all select c.id as id,c.name as name,c.name_en as nameEn from sys_corp c");
        sql.append(" where c.name like :name or c.name_en like :name");
        sql.append(" union all select i.id as id,i.bank_name as name,null as nameEn from bt_input_bank_info i");
        sql.append(" where i.bank_name like :name");
        sql.append(" union all select s.id as id,s.bank_name as name,null as nameEn from bt_swift_info s");
        sql.append(" where s.bank_name like :name");
        params.put("name", "%" + name + "%");
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpDTO.class);
    }

    public List<ExternalCorpDTO> queryInExternalType(List<String> externalType){
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        sql.append(" select e.code as code,e.id as id, e.name as name, e.name_en as nameEn ");
        sql.append(" from sys_external_corp e where e.STATUS = 1");
        sql.append(" and e.EXTERNAL_TYPE in (:externalType)");
        params.put("externalType",externalType);
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpDTO.class);
    }


    /**
     * 所有客商信息组件取数
     */
    public DataTablesOutput selectAllExternalDate(DataTablesInput input){
        StringBuilder sql = new StringBuilder();
        String corpIds=(String) input.getFormdata().get("corpIds");
        String paymentProducts=(String) input.getFormdata().get("paymentProducts");
        sql.append(" select ec.*, ec.name_en as nameEn ");
        sql.append(" from sys_external_corp ec");
        sql.append(" where ec.AUDIT_STATUS=95");
        if(!EmptyUtils.isEmpty(corpIds)){
            List<String> corpIdList= Arrays.asList(corpIds.split(","));
            sql.append(" and exists (select 1 from sys_externalcorp_corp ecorp where ec.id=ecorp.EXTERNALCORP_ID");
            sql.append(" and ").append(getSQLIn(corpIdList, 900, "ecorp.CORP_ID"));
            sql.append(" )");
        }
        if(!EmptyUtils.isEmpty(paymentProducts)){
            List<String> productList= Arrays.asList(paymentProducts.split(","));
            sql.append(" and exists (select 1 from sys_external_corp_quick_pay epay where ec.id=epay.EXTERNAL_CORP_ID ");
            sql.append(" and ").append(getSQLIn(productList, 900, "epay.PRO_ID"));
            sql.append(" )");
        }
        return this.getDynamicBySQL(input, sql.toString(), ExternalCorpVO.class, "ec", false);
    }
}
