package com.bytter.system.dao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.FbsBudgetDetailDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import com.bytter.system.entity.ExternalCorpBankAcc;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.vo.ExternalCorpBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017-06-21
 */
@Repository
public class ExternalCorpBankACCDao extends BaseDao<ExternalCorpBankAcc, String> implements Serializable {
    public List<ExternalCorpBankAcc> listBankAccByExternalCorpId(String externalCorpId) {
        String hql = "from ExternalCorpBankAcc ecb where ecb.externalCorpId=?0";
        return createQuery(hql, externalCorpId).list();
    }
    public void deleteBankAccByExternalCorpId(String externalCorpId) {
        String hql = "delete from ExternalCorpBankAcc where externalCorpId=?0 ";
        createQuery(hql, externalCorpId).executeUpdate();
    }
    /**
     * 根据客商ID查询
     *
     * @param externalCorpId 客商ID
     * @return List<ExternalCorpBankAccVO>
     * @author zuoy 2017/11/04
     */
    public List<ExternalCorpBankAccVO> listByExternalCorpId(String externalCorpId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select t.id, t.external_corp_id, t.external_acc, t.bank, t.bank_type, bankType.Type_Name as bankTypeName,");
        sql.append(" t.bank_sourcecode as bankSourceCode, t.is_default_acc, t.acc_province as accProvince,p.name_zh as accProvinceFexName, ");
        sql.append(" t.acc_city,c.name_zh as accCityFexName, t.agents, t.remark, t.is_sign_acc, t.op_time,t.IDENTIFY_TYPE identifyType, ");
        sql.append(" bc.cur_name as curName,t.external_acc_name as externalAccName, t.cur_id as curId, bt_nvl(t.pay_exc_type, '') as payExcType, t.intermediary_bank as intermediaryBank, ");
        sql.append(" t.intermediary_bank_swift_code as intermediaryBankSwiftCode, t.intermediary_bank_acc as intermediaryBankAcc, t.swift_code as swiftCode, t.local_clear_no as localClearNo, ");
        sql.append(" t.intermediary_bank_address as intermediaryBankAddress,t.bank_country_id as bankCountryId,scc.name_Zh as bankCountry ");
        sql.append(" from sys_external_corp_bankacc t ");
        sql.append(" left join bt_bank_type bankType on t.bank_type = bankType.id ");
        sql.append(" left join sys_country_cust p on t.acc_province = p.id ");
        sql.append(" left join sys_country_cust c on t.acc_city = c.id ");
        sql.append(" left join bt_currency bc on t.cur_id = bc.id ");
        sql.append(" left join sys_country_cust scc on t.bank_country_id = scc.id ");
        sql.append(" where t.external_corp_id = :externalCorpId ");
        params.put("externalCorpId", externalCorpId);
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpBankAccVO.class);
    }
    /**
     * 根据客户银行账号查询客商与银行账号关系表
     *
     * @param externalAcc 客户银行账号
     * @return
     */
    public ExternalCorpBankAcc findByExternalAcc(String externalAcc) {
        List<ExternalCorpBankAcc> externalCorpBankAccs = this.listBy("externalAcc", externalAcc);
        if (EmptyUtils.isEmptyList(externalCorpBankAccs)) {
            return null;
        } else {
            return externalCorpBankAccs.get(0);
        }
    }
    /**
     * 根据客商银行账号集合查询客商账号信息，包括省市信息
     *
     * @param bankAccs 客商银行账号集合
     * @return 客商账号信息
     */
    public List<ExternalCorpBankAccDTO> getBankAccByInfoAccs(List<String> bankAccs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.external_corp_id, t.external_acc, t.bank, t.bank_type, bankType.Type_Name as bankTypeName, t.bank_sourcecode as bankSourceCode, t.is_default_acc, t.acc_province as accProvince,p.name_zh as accProvinceFexName, ");
        sql.append(" t.acc_city,c.name_zh as accCityFexName, t.agents, t.remark, t.is_sign_acc, t.op_time ");
        sql.append(" from sys_external_corp_bankacc t  ");
        sql.append(" left join bt_bank_type bankType on t.bank_type = bankType.id ");
        sql.append(" left join sys_country_cust p on t.acc_province = p.id ");
        sql.append(" left join sys_country_cust c on t.acc_city = c.id ");
        sql.append(" where 1=1 ");
        String bankCodeSql = SqlUtil.getInSql(bankAccs, "t.external_acc", true);
        sql.append(" and ").append(bankCodeSql).append(" ");
        return this.listBySQLAliasToBean(sql.toString(), null, ExternalCorpBankAccDTO.class);
    }

    public int isExternal(String payeeAccName, String accCorpId){
        String sql = "SELECT COUNT(*) FROM sys_external_corp sec LEFT JOIN sys_externalcorp_corp secc ON sec.id = secc.EXTERNALCORP_ID WHERE sec.NAME= :payeeAccName AND secc.corp_id= :accCorpId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("payeeAccName", payeeAccName);
        params.put("accCorpId", accCorpId);
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }

    /**
     * 所有境内客商账号信息组件取数
     */
    public DataTablesOutput selectInnerExternalBankAccDate(DataTablesInput input, SysCountryCustDTO custDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.EXTERNAL_CORP_ID as externalCorpId,ec.code as ecCode,ec.name as ecName,t.EXTERNAL_ACC as externalAcc,t.EXTERNAL_ACC_NAME as externalAccName,");
        sql.append(" t.CUR_ID as curId,t.BANK as bank, t.BANK_SOURCECODE as bankSourceCode,ec.NAME_EN as ecNameEn,ec.bw_type as bwType,");
        sql.append(" t.REMARK as remark,t.IS_DEFAULT_ACC as isDefaultAcc,t.INTERMEDIARY_BANK as intermediaryBank,");
        sql.append(" t.SWIFT_CODE as swiftCode,t.LOCAL_CLEAR_NO as localClearNo,t.INTERMEDIARY_BANK_ADDRESS as intermediaryBankAddress,");
        sql.append(" t.INTERMEDIARY_BANK_SWIFT_CODE as intermediaryBankSwiftCode,t.INTERMEDIARY_BANK_ACC as intermediaryBankAcc, ");
        sql.append(" ec.ADDRESS_EN as address, ");
        sql.append("'").append(custDTO.getId()).append("'").append(" as countryId, ");
        sql.append("'").append(custDTO.getNameEn()).append("'").append(" as country, ");
        sql.append("'").append(custDTO.getNameEn()).append("'").append(" as bankCountry, ");
        sql.append(" ec.province as externalPro,ec.city as externalCity ");
        sql.append(" from sys_external_corp_bankacc t");
        sql.append(" left join sys_external_corp ec on t.EXTERNAL_CORP_ID=ec.id ");
        sql.append(" where ec.AUDIT_STATUS=95 and ec.is_native=1 ");
        return this.getDynamicBySQL(input, sql.toString(), ExternalCorpBankAccVO.class, "ec", false);
    }

    /**
     * 所有境外客商账号信息组件取数
     */
    public DataTablesOutput selectOuterExternalBankAccDate(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.EXTERNAL_CORP_ID as externalCorpId,ec.code as ecCode,ec.name as ecName,t.EXTERNAL_ACC as externalAcc,t.EXTERNAL_ACC_NAME as externalAccName,");
        sql.append(" t.CUR_ID as curId,t.BANK as bank, t.BANK_SOURCECODE as bankSourceCode,ec.NAME_EN as ecNameEn,ec.bw_type as bwType,");
        sql.append(" t.REMARK as remark,t.IS_DEFAULT_ACC as isDefaultAcc,t.INTERMEDIARY_BANK as intermediaryBank,");
        sql.append(" t.INTERMEDIARY_BANK_SWIFT_CODE as intermediaryBankSwiftCode,t.INTERMEDIARY_BANK_ACC as intermediaryBankAcc, ");
        sql.append(" t.SWIFT_CODE as swiftCode,t.LOCAL_CLEAR_NO as localClearNo,t.INTERMEDIARY_BANK_ADDRESS as intermediaryBankAddress,");
        sql.append(" ec.ADDRESS1 as address1,ec.PROVINCE as countryId,ec.city as address, p.name_en as country, swp.name_en as bankCountry, sw.bank_add  as bankAddress, ec.address as externalAddress,");
        sql.append(" ec.province as outExternalPro, ec.city as outExternalCity, p.name_en as outExternalProName, c.name_en as outExternalCityName, ");
        sql.append(" t.bank_Country_id as bankOutCountryId, sw.country_code as bankOutExternalPro, sw.city_name as bankOutExternalCity, swp.name_en as bankOutExternalProName, swc.name_en as bankOutExternalCityName ");
        sql.append(" from sys_external_corp_bankacc t");
        sql.append(" left join sys_external_corp ec on t.EXTERNAL_CORP_ID=ec.id ");
        sql.append(" left join sys_country_cust p on p.id=ec.province ");
        sql.append(" left join sys_country_cust c on c.id=ec.city ");
        sql.append(" left join bt_swift_info sw on t.SWIFT_CODE=sw.swift_code");
        sql.append(" left join sys_country_cust swp on swp.id=sw.country_code ");
        sql.append(" left join sys_country_cust swc on swc.id=sw.city_name ");
        sql.append(" where ec.AUDIT_STATUS=95 and ec.is_native=0 ");
        return this.getDynamicBySQL(input, sql.toString(), ExternalCorpBankAccVO.class, "ec", false);
    }


    public ExternalCorpBankAccDTO findAccInfoAndBankByExternalAcc(FbsBudgetDetailDTO budgetDetail) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT e.name AS NAME ,ecba.EXTERNAL_ACC AS externalAcc,ecba.BANK AS bank,");
        sql.append(" ecba.bank_sourcecode AS bankSourceCode,ecba.acc_province AS accProvince,");
        sql.append(" ecba.acc_city AS accCity ");
        sql.append("  FROM sys_external_corp e INNER JOIN sys_external_corp_bankacc ecba ");
        sql.append("  ON ecba.external_corp_id = e.id ");
        sql.append("  WHERE ecba.external_acc = :externalAcc and e.is_native = 1 ");
        sql.append("  AND e.status = 1 AND e.audit_status = 95");
        if (StringUtils.isNotBlank(budgetDetail.getBusCorpId())) {
            //共享客商授权表无数据
            sql.append(" and (");
            sql.append(" ((e.is_common = 1 or e.is_common is null ) and not exists(select max(1) from sys_externalcorp_corp ecc where ecc.externalcorp_id = e.id ))");
            //有权限的单位
            sql.append("  or exists(select max(1) from sys_externalcorp_corp ecc where ecc.externalcorp_id = e.id and ")
                    .append(" ecc.corp_id = '" +budgetDetail.getBusCorpId()).append("')");
            sql.append(" ) ");
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("externalAcc", budgetDetail.getPayeeAcc());
        return getBySQLAliasToBean(sql.toString(), params, ExternalCorpBankAccDTO.class);
    }

    public ExternalCorpBankAccVO queryByBankAcc(String externalAcc) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.external_corp_id as externalCorpId,ec.code as ecCode,ec.name as ecName,t.external_acc as externalAcc,t.EXTERNAL_ACC_NAME as externalAccName,");
        sql.append(" t.CUR_ID as curId,t.BANK as bank, t.BANK_SOURCECODE as bankSourceCode,ec.NAME_EN as ecNameEn,ec.bw_type as bwType,");
        sql.append(" t.REMARK as remark,t.IS_DEFAULT_ACC as isDefaultAcc,t.INTERMEDIARY_BANK as intermediaryBank,");
        sql.append(" t.INTERMEDIARY_BANK_SWIFT_CODE as intermediaryBankSwiftCode,t.INTERMEDIARY_BANK_ACC as intermediaryBankAcc, ");
        sql.append(" t.SWIFT_CODE as swiftCode,t.LOCAL_CLEAR_NO as localClearNo,t.INTERMEDIARY_BANK_ADDRESS as intermediaryBankAddress,");

        sql.append(" ec.ADDRESS1 as address1,ec.PROVINCE as countryId,ec.city as address, p.name_en as country, swp.name_en as bankCountry, sw.bank_add  as bankAddress, ");
        sql.append(" ec.province as outExternalPro, ec.city as outExternalCity, p.name_en as outExternalProName, c.name_en as outExternalCityName, ");
        sql.append(" t.bank_Country_id as bankOutCountryId, sw.country_code as bankOutExternalPro, sw.city_name as bankOutExternalCity, swp.name_en as bankOutExternalProName, swc.name_en as bankOutExternalCityName ");
        sql.append(" from sys_external_corp_bankacc t");
        sql.append(" left join sys_external_corp ec on t.external_corp_id=ec.id ");
        sql.append(" left join sys_country_cust p on p.id=ec.province ");
        sql.append(" left join sys_country_cust c on c.id=ec.city ");
        sql.append(" left join bt_swift_info sw on t.SWIFT_CODE=sw.swift_code");
        sql.append(" left join sys_country_cust swp on swp.id=sw.country_code ");
        sql.append(" left join sys_country_cust swc on swc.id=sw.city_name ");
        sql.append(" where ec.AUDIT_STATUS=95 and t.external_acc = :externalAcc");
        HashMap<String, Object> params = new HashMap<>();
        params.put("externalAcc", externalAcc);
        return getBySQLAliasToBean(sql.toString(), params, ExternalCorpBankAccVO.class);
    }

    public ExternalCorpBankAccVO queryByAccAndCurId(String externalAcc, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.external_corp_id as externalCorpId,ec.code as ecCode,ec.name as ecName,t.external_acc as externalAcc,t.EXTERNAL_ACC_NAME as externalAccName,");
        sql.append(" t.CUR_ID as curId,t.BANK as bank, t.BANK_SOURCECODE as bankSourceCode,ec.NAME_EN as ecNameEn,ec.bw_type as bwType,");
        sql.append(" t.REMARK as remark,t.IS_DEFAULT_ACC as isDefaultAcc,t.INTERMEDIARY_BANK as intermediaryBank,");
        sql.append(" t.INTERMEDIARY_BANK_SWIFT_CODE as intermediaryBankSwiftCode,t.INTERMEDIARY_BANK_ACC as intermediaryBankAcc, ");
        sql.append(" t.SWIFT_CODE as swiftCode,t.LOCAL_CLEAR_NO as localClearNo,t.INTERMEDIARY_BANK_ADDRESS as intermediaryBankAddress,");
        sql.append(" ec.ADDRESS1 as address1,ec.PROVINCE as countryId,ec.city as address, p.name_en as country, swp.name_en as bankCountry, sw.bank_add  as bankAddress, ");
        sql.append(" ec.province as outExternalPro, ec.city as outExternalCity, p.name_en as outExternalProName, c.name_en as outExternalCityName, ");
        sql.append(" t.bank_Country_id as bankOutCountryId, sw.country_code as bankOutExternalPro, sw.city_name as bankOutExternalCity, swp.name_en as bankOutExternalProName, swc.name_en as bankOutExternalCityName ");
        sql.append(" from sys_external_corp_bankacc t");
        sql.append(" left join sys_external_corp ec on t.external_corp_id=ec.id ");
        sql.append(" left join sys_country_cust p on p.id=ec.province ");
        sql.append(" left join sys_country_cust c on c.id=ec.city ");
        sql.append(" left join bt_swift_info sw on t.SWIFT_CODE=sw.swift_code");
        sql.append(" left join sys_country_cust swp on swp.id=sw.country_code ");
        sql.append(" left join sys_country_cust swc on swc.id=sw.city_name ");
        sql.append(" where ec.AUDIT_STATUS=95 and t.external_acc = :externalAcc");
        HashMap<String, Object> params = new HashMap<>();
        params.put("externalAcc", externalAcc);
        if (StringUtils.isNotEmpty(curId)) {
            params.put("curId", curId);
            sql.append(" and t.CUR_ID = :curId");
        }
        return getBySQLAliasToBean(sql.toString(), params, ExternalCorpBankAccVO.class);
    }

    public List<ExternalCorpBankAccDTO> queryFexExternalCorpBankAccByIds(List<String> ids) {
        if (EmptyUtils.isEmpty(ids)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        sql.append(" select t.id as id,t.external_corp_id as externalCorpId,t.external_acc as externalAcc,");
        sql.append(" t.external_acc_name as externalAccName,t.bank as bank,t.bank_type as bankType,");
        sql.append(" t.bank_sourcecode as bankSourceCode,t.is_default_acc as isDefaultAcc,t.remark as remark,");
        sql.append(" t.cur_id as curId,NULLIF(t.pay_exc_type, '') as payExcType,t.swift_code as swiftCode,t.local_clear_no as localClearNo,");
        sql.append(" t.intermediary_bank as intermediaryBank,t.intermediary_bank_swift_code as intermediaryBankSwiftCode,");
        sql.append(" t.intermediary_bank_acc as intermediaryBankAcc,t.intermediary_bank_address as intermediaryBankAddress,");
        sql.append(" t.bank_addr as bankAddr,t.bank_country_id as bankCountryId ");
        sql.append(" from sys_external_corp_bankacc t where t.id in (:ids)");
        params.put("ids", ids);
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpBankAccDTO.class);
    }

    public List<ExternalCorpBankAccDTO> queryFexExternalCorpBankAccByExternalCorpId(String externalCorpId) {
        if (StringUtils.isBlank(externalCorpId)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        sql.append(" select t.id as id,t.external_corp_id as externalCorpId,t.external_acc as externalAcc,");
        sql.append(" t.external_acc_name as externalAccName,t.bank as bank,t.bank_type as bankType,");
        sql.append(" t.bank_sourcecode as bankSourceCode,t.is_default_acc as isDefaultAcc,t.remark as remark,");
        sql.append(" t.cur_id as curId,NULLIF(t.pay_exc_type, '') as payExcType,t.swift_code as swiftCode,t.local_clear_no as localClearNo,");
        sql.append(" t.intermediary_bank as intermediaryBank,t.intermediary_bank_swift_code as intermediaryBankSwiftCode,");
        sql.append(" t.intermediary_bank_acc as intermediaryBankAcc,t.intermediary_bank_address as intermediaryBankAddress,");
        sql.append(" t.bank_addr as bankAddr,t.bank_country_id as bankCountryId ");
        sql.append(" from sys_external_corp_bankacc t where t.external_corp_id = :externalCorpId");
        params.put("externalCorpId", externalCorpId);
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpBankAccDTO.class);
    }
}
