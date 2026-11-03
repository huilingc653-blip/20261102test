package com.bytter.basic.dao;
import com.bytter.basic.entity.BankType;
import com.bytter.basic.vo.BankTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.BankTypeByAccDTO;
import com.bytter.system.entity.BankRegion;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银行类型维护
 * @date 2017-05-17 18:07:34
 */
@Repository
public class BtBankTypeDao extends BaseDao<BankType, String> {
    /**
     * 列表查询
     *
     * @param dataTablesInput
     * @param sqlWhere
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String sqlWhere) {
        StringBuilder sql = new StringBuilder("select t.id as id, ");
        sql.append(" t.bank_Type as bankType,type_Name as typeName,t.bank_Prefix as bankPrefix, ");
        sql.append(" t.rmk as rmk,t.valid_Sign as validSign,t.create_By as createBy,t.is_system as isSystem, ");
        sql.append(" t.is_domestic_bank as isDomesticBank, t.bank_name_Prefix as bankNamePrefix, ");
        sql.append(" t.create_Date as createDate,t.update_By as updateBy,t.update_Date as updateDate, ");
        sql.append(" t.netbank_cfca_date as netbankCFCADate, t.bank_kind as bankKind,t.remit_Method as remitMethod,t.whether_connect_bank as whetherConnectBank ");
        sql.append(" from bt_Bank_Type t where 1=1 ");
        sql.append(sqlWhere);
        return getDynamicBySQL(dataTablesInput, sql.toString(), BankTypeVO.class, "t", false);
    }
    /**
     * 根据编码获取付款方式
     *
     * @param bankType
     * @return
     */
    public BankType findByType(String bankType) {
        return getUniqueBy("bankType", bankType);
    }
    public List<BankType> listByBankType(String bankType) {
        List<BankType> list = listBy("bankType", bankType);
        return list;
    }
    public List<BankType> listByBankPrefix(String bankPrefix, String bankNamePrefix) {
        String hql = " from BankType where bankPrefix =:bankPrefix and bankNamePrefix =:bankNamePrefix";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("bankPrefix", bankPrefix);
        param.put("bankNamePrefix", bankNamePrefix);
        return listByHQL(hql, param);
    }
    public List<BankType> listBySignOrPrefix(String validSign, String bankPrefix) {
        String hql = " from BankType where validSign =:validSign and bankPrefix =:bankPrefix";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("validSign", validSign);
        param.put("bankPrefix", bankPrefix);
        return listByHQL(hql, param);
    }
    public BankType getByIdCache(String id) {
        String hql = "from BankType where id = ?0";
        Query query = createQuery(hql, id);
        query.setCacheable(true);
        return (BankType) query.uniqueResult();
    }
    /**
     * 可能存在多个人行行号前缀,所以去除根据人行行号前缀查询银行类型判断
     *
     * @param prefix
     * @return
     * @author zuoy 2017/11/27
     * @deprecated
     */
    @Deprecated
    public BankType getByPrefixCache(String prefix) {
        String hql = "from BankType where bankPrefix = ?0";
        Query query = createQuery(hql, prefix);
        query.setCacheable(true);
        return (BankType) query.uniqueResult();
    }
    /**
     * 判断银行城市id是否存在
     **/
    public boolean bankCityCodeIsExist(String bankCityCode) {
        String hql = " from BankRegion where bankCity=:bankCityCode";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("bankCityCode", bankCityCode);
        List<BankRegion> list = createQuery(hql, param).list();
        return !EmptyUtils.isEmpty(list) ? true : false;
    }
    /**
     * 根据用户定义的年检到期日获取到期时间内的
     *
     * @param searchDate [网银证书到期提醒提前天数]
     * @return 网银证书到期银行类型集合
     * @author qinl
     * @date 2019-07-04
     */
    public List<BankType> listByCfcaDateAlertDays(String searchDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" T.ID,T.VALID_SIGN,T.CREATE_BY,T.NETBANK_CFCA_DATE");
        sql.append(" FROM BT_BANK_TYPE T ");
        sql.append(" WHERE T.VALID_SIGN = '1'");
        sql.append(" AND T.NETBANK_CFCA_DATE <=:searchDate");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("searchDate", DateUtils.parse(searchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return listBySQLAliasToBean(sql.toString(), params, BankType.class);
    }
    /**
     * 根据账号过滤银行类别
     *
     * @return
     */
    public List<BankTypeVO> listBankTypeFilterByBankAcc() {
        StringBuilder sql = new StringBuilder("select distinct t.id as id, ");
        sql.append(" t.bank_Type as bankType,type_Name as typeName,t.bank_Prefix as bankPrefix, ");
        sql.append(" t.rmk as rmk,t.valid_Sign as validSign,t.create_By as createBy,t.is_system as isSystem, ");
        sql.append(" t.is_domestic_bank as isDomesticBank, t.bank_name_Prefix as bankNamePrefix, ");
        sql.append(" t.netbank_cfca_date as netbankCFCADate,t.remit_Method as remitMethod ");
        sql.append(" from bt_bank_type t where t.valid_sign = '1'");
        sql.append(" and (t.id in (select bank_type_id from bt_bank_acc)  or t.id in (select bank_type_id from bt_bank_acc_app))");
        sql.append(" order by t.bank_type ");
        return listBySQLAliasToBean(sql.toString(), null, BankTypeVO.class);
    }
    public List<BankType> listByBankTypeNames(Set<String> bankTypeNames) {
        StringBuffer sql = new StringBuffer("select t.id,t.is_domestic_bank,t.type_Name ");
        sql.append(" from BT_BANK_TYPE t where t.type_name in :bankTypeNames ");
        Map<String, Object> params = new HashMap<>();
        params.put("bankTypeNames", bankTypeNames);
        List mapList = listBySQL(sql.toString(), params);
        if (EmptyUtils.isEmpty(mapList)) {
            return null;
        }
        return BeanUtils.copyMapListProperties(mapList, BankType.class);
    }
    /**
     * 根据id查找银行类别名称
     * @param ids
     * @return
     */
    public List<String> getBankTypeNameByIds(List<String> ids) {
        return  createSQLQuery("select type_name from bt_bank_type where 1=1 and "+getSQLIn(ids, 900, "id")).list();
    }
    public List<BankType> getBankTypeNameAndBankTypeByIds(List<String> ids) {
        StringBuffer sql = new StringBuffer("select t.id, t.bank_type, t.type_name ");
        sql.append(" from BT_BANK_TYPE t where t.id in (:ids) ");
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        List mapList = listBySQL(sql.toString(), params);
        if (EmptyUtils.isEmpty(mapList)) {
            return null;
        }
        return BeanUtils.copyMapListProperties(mapList, BankType.class);
    }
    public List<BankTypeByAccDTO> listBankTypeByAcc(List<String> accList){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT bba.bank_acc as bankAcc,bba.ACC_NAME AS accName,bba.BANK_CODE AS bankCode,bba.BANK_NAME AS bankName,bbt.ID AS bankTypeId,bbt.TYPE_NAME AS bankTypeName ");
        sql.append(" FROM bt_bank_acc bba LEFT JOIN bt_bank_type bbt ON bba.BANK_TYPE_ID=bbt.id  ");
        sql.append(" WHERE bba.bank_acc IN (:accList) ");
        Map<String, Object> params = new HashMap<>();
        params.put("accList", accList);
        List mapList = listBySQL(sql.toString(), params);
        if (EmptyUtils.isEmpty(mapList)) {
            return null;
        }
        return BeanUtils.copyMapListProperties(mapList, BankTypeByAccDTO.class);
    }
}
