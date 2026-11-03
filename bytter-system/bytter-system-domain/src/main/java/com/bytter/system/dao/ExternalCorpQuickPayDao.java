package com.bytter.system.dao;

import com.bytter.system.entity.ExternalCorpQuickPay;
import com.bytter.system.constants.ExternalCorpConstants;
import com.bytter.system.vo.ExternalCorpBankAccVO;
import com.bytter.system.vo.ExternalCorpQuickPayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客商快捷支付信息dao
 */
@Repository
public class ExternalCorpQuickPayDao extends BaseDao<ExternalCorpQuickPay, String> implements Serializable {

    /**
     * 根据客商信息id获取快捷支付信息集合
     *
     * @param externalCorpId 客商id
     * @return
     */
    public List<ExternalCorpQuickPay> listQuickPayByExternalCorpId(String externalCorpId) {
        String hql = "from ExternalCorpQuickPay ecqp where ecqp.externalCorpId=?0";
        return createQuery(hql, externalCorpId).list();
    }

    /**
     * 根据客商信息id删除快捷支付信息
     *
     * @param externalCorpId 客商id
     */
    public void deleteQuickPayByExternalCorpId(String externalCorpId) {
        String hql = "delete from ExternalCorpQuickPay where externalCorpId=?0 ";
        createQuery(hql, externalCorpId).executeUpdate();
    }

	/**
	 * 根据客商信息id获取快捷支付信息集合
	 * @param externalCorpId 客商id
	 * @return
	 */
	public List<ExternalCorpQuickPayVO> listByExternalCorpId(String externalCorpId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select secqp.id as id,secqp.PRO_ID as proId,fp.pro_name as proName,secqp.country_code as countryCode,");
		sql.append(" secqp.tel as tel,secqp.email as email,secqp.fps_id as fpsId,secqp.credit_card as creditCard,secqp.hk_id as hkId,");
		sql.append(" secqp.is_default_acc as isDefaultAcc from sys_external_corp_quick_pay secqp ");
		sql.append(" left join fex_product fp on fp.id = secqp.pro_id ");
		sql.append(" where secqp.external_corp_id = :externalCorpId ");
		params.put("externalCorpId", externalCorpId);
		return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpQuickPayVO.class);
	}

	/**
	 * 根据客商ID集合批量查询关联交付产品名称。
	 *
	 * @param externalCorpIds 客商ID集合
	 * @return 客商快捷支付信息集合
	 */
	public List<ExternalCorpQuickPayVO> listByExternalCorpIds(List<String> externalCorpIds) {
		if (EmptyUtils.isEmptyList(externalCorpIds)) {
			return Collections.emptyList();
		}
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select secqp.external_corp_id as externalCorpId, fp.pro_name as proName ");
		sql.append(" from sys_external_corp_quick_pay secqp ");
		sql.append(" left join fex_product fp on fp.id = secqp.pro_id ");
		sql.append(" where ");
		sql.append(SqlUtil.getInSqlByParams(externalCorpIds, "secqp.external_corp_id", params));
		return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpQuickPayVO.class);
	}

	/**
	 * 根据客商id获取所有关联产品
	 * @param externalId
	 * @return
	 */
	public List<String> getProductNameByExternalId(String externalId){
		String sql = " select distinct f.pro_Name from sys_external_corp_quick_pay sp ";
		sql+=" left join fex_product f on sp.PRO_ID=f.id where sp.EXTERNAL_CORP_ID = :externalId and f.pro_Name is not null";
		HashMap<String, String> params = new HashMap<>();
		params.put("externalId",externalId);
		return this.createSQLQuery(sql.toString(),params).list();
	}


    /**
     * 所有客商fps信息组件取数
     */
    public DataTablesOutput fpsExternalBankAccDate(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select ec.id as id,sp.EXTERNAL_CORP_ID as externalCorpId, ec.code as ecCode,ec.name as ecName,ec.NAME_EN as ecNameEn,ec.bw_type as bwType, ec.ADDRESS as addressEn,");
        sql.append(" sp.FPS_ID as fpsId,sp.EMAIL as email,sp.TEL as tel,sp.CREDIT_CARD creditCard,sp.HK_ID as hkId, sp.country_code as countryCode, ");
        sql.append(" sp.IS_DEFAULT_ACC as isDefaultAcc, ec.province as prov, ec.city as city,");
        //国家地区补充
        sql.append(" ec.PROVINCE as countryId, p.name_en as countryName, p.name_en as provName, c.name_en as cityName ");
        sql.append(" from sys_external_corp_quick_pay sp");
        sql.append(" left join sys_external_corp ec on sp.EXTERNAL_CORP_ID=ec.id  ");
        sql.append(" left join sys_country_cust p on p.id=ec.province ");
        sql.append(" left join sys_country_cust c on c.id=ec.city ");
        sql.append(" where ec.AUDIT_STATUS=95 and sp.PRO_ID=:proId");
        params.put("proId", ExternalCorpConstants.getExternalCorpQuickPayFpsId());
        return this.getDynamicBySQL(input, sql.toString(), ExternalCorpQuickPayVO.class, "ec", false, params);
    }

    /**
     * 所有客商payPal信息组件取数
     */
    public DataTablesOutput payPalExternalBankAccDate(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select ec.code as ecCode,sp.EXTERNAL_CORP_ID as externalCorpId,ec.name as ecName,ec.NAME_EN as ecNameEn,ec.bw_type as bwType,ec.ADDRESS as addressEn,");
        sql.append(" sp.EMAIL as email,sp.TEL as tel,sp.IS_DEFAULT_ACC as isDefaultAcc, sp.country_code as countryCode,");
        sql.append(" ec.province as prov, ec.city as city,");
        //国家地区补充
        sql.append(" ec.PROVINCE as countryId, p.name_en as countryName, p.name_en as provName, c.name_en as cityName ");
        sql.append(" from sys_external_corp_quick_pay sp");
        sql.append(" left join sys_external_corp ec on sp.EXTERNAL_CORP_ID=ec.id  ");
        sql.append(" left join sys_country_cust p on p.id=ec.province ");
        sql.append(" left join sys_country_cust c on c.id=ec.city ");
        sql.append(" where ec.AUDIT_STATUS=95 and sp.PRO_ID=:proId");
        params.put("proId", ExternalCorpConstants.getExternalCorpQuickPayPaypalId());
        return this.getDynamicBySQL(input, sql.toString(), ExternalCorpQuickPayVO.class, "ec", false, params);
    }

    /**
     * 资金收付收款账户类型对应数据
     */
    public List<ExternalCorpQuickPayVO> fexPayPayModelList(String externalId, String proId,String field) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<>();

        sql.append("select ");
        sql.append(" bt_nvl(sp.FPS_ID,'') as fpsId,bt_nvl(sp.EMAIL,'') as email,bt_nvl(sp.TEL,'') as tel,bt_nvl(sp.country_code,'') as countryCode, ");
        sql.append(" bt_nvl(sp.CREDIT_CARD,'') creditCard,bt_nvl(sp.HK_ID,'') as hkId");
        sql.append(" from sys_external_corp_quick_pay sp");
        sql.append(" left join sys_external_corp ec on sp.EXTERNAL_CORP_ID=ec.id ");
        sql.append(" where ec.AUDIT_STATUS=95 ");
        if (!EmptyUtils.isEmpty(externalId)) {
            sql.append(" and sp.EXTERNAL_CORP_ID=:externalId");
            params.put("externalId", externalId);
        }
        if (!EmptyUtils.isEmpty(proId)) {
            sql.append(" and sp.PRO_ID=:proId");
            params.put("proId", ExternalCorpConstants.getCurrentTenantQuickPayProductId(proId));
        }
        if(!EmptyUtils.isEmpty(field)){
            if("fpsId".equals(field)){
                sql.append(" and sp.FPS_ID is not null");
            }else if("email".equals(field)){
                sql.append(" and sp.EMAIL is not null");
            }else if("phone".equals(field)){
                sql.append(" and sp.TEL is not null and sp.country_code is not null ");
            }else if("creditCard".equals(field)){
                sql.append(" and sp.CREDIT_CARD is not null");
            }else if("hkId".equals(field)){
                sql.append(" and sp.HK_ID is not null");
            }
        }
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpQuickPayVO.class);
    }
}
