package com.bytter.basic.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.basic.entity.BtInputBankInfo;
import com.bytter.basic.vo.BtInputBankInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.bytter.system.entity.Region;
import com.google.common.collect.Maps;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银行行号维护
 * @date 2017-05-23 19:06:25
 */
@Repository
public class BtInputBankInfoDao extends BaseDao<BtInputBankInfo, String> {
    /**
     * 使用hql语句进行分页查询
     * <br>
     * <b>作者：</b>周亮<br>
     * <b>日期：</b>2017年6月29日<br>
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
        final Table table = Region.class.getAnnotation(Table.class);
        String regionTable = table.name();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<String, Object>();
        String bankCode = (String) dataTablesInput.getFormdata().get("bankCode");
        String bankName = (String) dataTablesInput.getFormdata().get("bankName");
        String bankType = (String) dataTablesInput.getFormdata().get("bankType");
        sql.append("select ");
        sql.append(" ibi.id as id,");
        sql.append("ibi.SYS_BANK_CODE as bankCode,");
        sql.append("ibi.BANK_NAME as bankName,");
        sql.append("ibi.BANK_PREFIX as bankPrefix,");
        sql.append("ibi.BANK_CITY_CODE as bankCityCode,");
        sql.append("r.PARENT_ID as prov,");
        sql.append("r.ID as city,");
        sql.append("ibi.update_Date as updateDate,");
        sql.append("bt.ID as bankTypeId");
        sql.append(" FROM BT_INPUT_BANK_INFO ibi ");
        sql.append(" INNER JOIN SYS_BANK_REGION br ON ibi.BANK_CITY_CODE = br.BANK_CITY ");
        sql.append(" INNER JOIN ").append(regionTable).append(" r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY ");
        sql.append(" LEFT JOIN BT_BANK_TYPE bt ON ibi.BANK_TYPE_ID = bt.id  ");
        // sql.append(" LEFT JOIN BIS_BIF_INIT bi ON bt.ID = bi.BANK_TYPE_ID ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotBlank(bankCode)) {
            sql.append(" and ibi.SYS_BANK_CODE like :bankCode ");
            String bankCodeReplace = bankCode.replace(" ", "%");
            map.put("bankCode", "%" + bankCodeReplace + "%");
        }
        if (StringUtils.isNotBlank(bankName)) {
            sql.append(" and ibi.BANK_NAME like :bankName ");
            map.put("bankName", "%" + bankName + "%");
        }
        if (StringUtils.isNotBlank(bankType)) {
            sql.append(" and bt.ID = :bankType ");
            map.put("bankType", bankType);
        }
        String province = (String) dataTablesInput.getFormdata().get("province");
        String city = (String) dataTablesInput.getFormdata().get("city");
        if (StringUtils.isNotBlank(province) && StringUtils.isNotBlank(city)) {
            sql.append(" and r.ID = :regionId ");
            map.put("regionId", city);
        } else if (StringUtils.isNotBlank(province) && StringUtils.isBlank(city)) {
            sql.append(" and r.PARENT_ID = :regionId ");
            map.put("regionId", province);
        }
        return getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), map, BtInputBankInfoVO.class);
    }

    public List<BtInputBankInfoVO> getByBankCodeOrBankNames(String bankCode, List<String> bankName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ibi.id as id,ibi.sys_bank_code  as bankcode,ibi.bank_name as bankname,ibi.bank_prefix as bankprefix, bt.id as bankTypeId, bt.type_name as bankTypeName, ");
        sql.append(" ibi.bank_city_code as bankcitycode,r.parent_id as prov, r.id as city ,r.prov_input_name provName,r.name cityName, bs.swift_code as swiftCode");
        sql.append(" from bt_input_bank_info ibi inner JOIN BT_BANK_TYPE bt ON ibi.BANK_TYPE_ID = bt.id  ");
        sql.append(" inner join sys_bank_region br on ibi.bank_city_code = br.bank_city inner join sys_region r on br.bank_input_city = r.bank_input_city");
        sql.append(" left join bt_input_bank_swift bs on ibi.sys_bank_code = bs.bank_code");
        sql.append(" where  1=1 ");
        HashMap<String, Object> params = new HashMap();
        if (StringUtils.isNotBlank(bankCode)) {
            sql.append(" and ibi.sys_bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        if (CollectionUtil.isNotEmpty(bankName)) {
            sql.append(" and ibi.bank_name in (:bankName)");
            params.put("bankName", bankName);
        }
        List<BtInputBankInfoVO> bySQLAliasToBean = listBySQLAliasToBean(sql.toString(), params, BtInputBankInfoVO.class);
        if(!CollectionUtil.isEmpty(bySQLAliasToBean)){
            return bySQLAliasToBean;
        }
        //防止BANK_TYPE_ID为空，导致匹配不上数据
        return getByBankCodeOrBankNames_backup(bankCode, bankName);
    }

    public List<BtInputBankInfoVO> getByBankCodeOrBankNames_backup(String bankCode, List<String> bankName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ibi.id as id,ibi.sys_bank_code  as bankcode,ibi.bank_name as bankname,ibi.bank_prefix as bankprefix, bt.id as bankTypeId, bt.type_name as bankTypeName, ");
        sql.append(" ibi.bank_city_code as bankcitycode,r.parent_id as prov, r.id as city ,r.prov_input_name provName,r.name cityName, bs.swift_code as swiftCode");
        sql.append(" from bt_input_bank_info ibi inner JOIN BT_BANK_TYPE bt ON ibi.bank_name like concat(bt.bank_name_prefix,'%') and bt.bank_prefix = ibi.bank_prefix ");
        sql.append(" inner join sys_bank_region br on ibi.bank_city_code = br.bank_city inner join sys_region r on br.bank_input_city = r.bank_input_city");
        sql.append(" left join bt_input_bank_swift bs on ibi.sys_bank_code = bs.bank_code");
        sql.append(" where  1=1 ");
        HashMap<String, Object> params = new HashMap();
        if (StringUtils.isNotBlank(bankCode)) {
            sql.append(" and ibi.sys_bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        if (CollectionUtil.isNotEmpty(bankName)) {
            sql.append(" and ibi.bank_name = :bankName");
            params.put("bankName", bankName);
        }
        return listBySQLAliasToBean(sql.toString(), params, BtInputBankInfoVO.class);
    }


    /**
     * 根据联行号或开户行名称获取联行号信息
     *
     * @param bankCode
     * @param bankName
     * @return
     */
    public BtInputBankInfoVO getByBankCodeOrBankName(String bankCode, String bankName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ibi.id as id,ibi.sys_bank_code  as bankcode,ibi.bank_name as bankname,ibi.bank_prefix as bankprefix, bt.id as bankTypeId, bt.type_name as bankTypeName, ");
        sql.append(" ibi.bank_city_code as bankcitycode,r.parent_id as prov, r.id as city ,r.prov_input_name provName,r.name cityName, bs.swift_code as swiftCode");
        sql.append(" from bt_input_bank_info ibi inner JOIN BT_BANK_TYPE bt ON ibi.BANK_TYPE_ID = bt.id  ");
        sql.append(" inner join sys_bank_region br on ibi.bank_city_code = br.bank_city inner join sys_region r on br.bank_input_city = r.bank_input_city");
        sql.append(" left join bt_input_bank_swift bs on ibi.sys_bank_code = bs.bank_code");
        sql.append(" where  1=1 ");
        HashMap<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(bankCode)) {
            sql.append(" and ibi.sys_bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        if (StringUtils.isNotBlank(bankName)) {
            sql.append(" and ibi.bank_name = :bankName");
            params.put("bankName", bankName);
        }
        BtInputBankInfoVO bySQLAliasToBean = getBySQLAliasToBean(sql.toString(), params, BtInputBankInfoVO.class);
        if(bySQLAliasToBean != null){
            return bySQLAliasToBean;
        }
        //防止BANK_TYPE_ID为空，导致匹配不上数据
        return getByBankCodeOrBankName_backup(bankCode, bankName);
    }

    public BtInputBankInfoVO getByBankCodeOrBankName_backup(String bankCode, String bankName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ibi.id as id,ibi.sys_bank_code  as bankcode,ibi.bank_name as bankname,ibi.bank_prefix as bankprefix, bt.id as bankTypeId, bt.type_name as bankTypeName, ");
        sql.append(" ibi.bank_city_code as bankcitycode,r.parent_id as prov, r.id as city ,r.prov_input_name provName,r.name cityName, bs.swift_code as swiftCode");
        sql.append(" from bt_input_bank_info ibi inner JOIN BT_BANK_TYPE bt ON ibi.bank_name like concat(bt.bank_name_prefix,'%') and bt.bank_prefix = ibi.bank_prefix ");
        sql.append(" inner join sys_bank_region br on ibi.bank_city_code = br.bank_city inner join sys_region r on br.bank_input_city = r.bank_input_city");
        sql.append(" left join bt_input_bank_swift bs on ibi.sys_bank_code = bs.bank_code");
        sql.append(" where  1=1 ");
        HashMap<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(bankCode)) {
            sql.append(" and ibi.sys_bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        if (StringUtils.isNotBlank(bankName)) {
            sql.append(" and ibi.bank_name = :bankName");
            params.put("bankName", bankName);
        }
        return getBySQLAliasToBean(sql.toString(), params, BtInputBankInfoVO.class);
    }
    /**
     * 查询银行行号信息
     *
     * @param input
     * @return
     */
    public DataTablesOutput findListBysql(DataTablesInput input) {
        String sql = "select info.id,info.sys_bank_code,info.bank_name,info.prov,info.city from bt_input_bank_info info";
        return getDynamicBySQL(input, sql, BtInputBankInfoVO.class, "info", Boolean.TRUE, Boolean.TRUE);
    }
    /**
     * 根据id查找银行行号信息
     *
     * @param id
     * @return
     */
    public BtInputBankInfoVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ibi.id as id,ibi.sys_bank_code  as bankcode,ibi.bank_name as bankname,ibi.bank_prefix as bankprefix,");
        sql.append(" ibi.bank_city_code as bankcitycode,r.parent_id as prov, r.prov_input_name provName,r.id AS city ,r.name cityName,");
        sql.append(" ibi.create_By as createBy,ibi.create_Date as createDate,ibi.update_By as updateBy,ibi.update_Date as updateDate");
        sql.append(" from bt_input_bank_info ibi inner join sys_bank_region br on ibi.bank_city_code = br.bank_city inner join sys_region r");
        sql.append(" on br.bank_input_city = r.bank_input_city where ibi.id =:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BtInputBankInfoVO.class);
    }
    /**
     * 根据银行行号查找是否已维护些账号
     *
     * @param bankCode
     * @return
     */
    public BtInputBankInfo getByBankInfo(String bankCode) {
        return (BtInputBankInfo) createQuery("from BtInputBankInfo b where b.bankCode =:bankCode").setString("bankCode", bankCode).setCacheable(true).uniqueResult();
    }
    /**
     * 根据联行号查找对应id
     *
     * @param bankCode
     * @return
     */
    public String getSysBankInfoId(String bankCode) {
        return (String) createQuery("select b.id from BtInputBankInfo b where b.bankCode =:bankCode").setString("bankCode", bankCode).uniqueResult();
    }
    /**
     * 根据联行号查找对应的所有信息
     *
     * @param bankCode
     * @return
     */
    public List<BtInputBankInfo> getBtInputBankInfo(String bankCode) {
        return listBy("bankCode", bankCode);
    }
    public BtInputBankInfo getBtInputBankInfoByParams(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder();
        if (params == null || params.isEmpty()) {
            return null;
        }
        hql.append("from BtInputBankInfo ibi where 1=1 ");
        Iterator<String> paramsKeys = params.keySet().iterator();
        while (paramsKeys.hasNext()) {
            String key = paramsKeys.next();
            hql.append("and ibi." + key + " = :" + key + " ");
        }
        Query query = createQuery(hql.toString(), params);
        Object inputBankInfo = query.uniqueResult();
        if (inputBankInfo != null) {
            return (BtInputBankInfo) inputBankInfo;
        }
        return null;
    }
    /**
     * BT_BANK_TYPE 的前缀必须符合
     * Created by chenlj 2018/4/17
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesPrefix(DataTablesInput dataTablesInput) {
        final Table table = Region.class.getAnnotation(Table.class);
        String regionTable = table.name();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<String, Object>();
        String bankCode = (String) dataTablesInput.getFormdata().get("bankCode");
        String bankName = (String) dataTablesInput.getFormdata().get("bankName");
        String bankType = (String) dataTablesInput.getFormdata().get("bankType");
        String swiftCode = (String) dataTablesInput.getFormdata().get("swiftCode");
        String joinBankType = String.valueOf(dataTablesInput.getFormdata().get("joinBankType"));
        boolean isBankType = "true".equalsIgnoreCase(joinBankType);
        boolean isbankType = !EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("bankType"))
                && !EmptyUtils.isBlank(dataTablesInput.getFormdata().get("bankType").toString());
        sql.append("select ibi.id as id,ibi.SYS_BANK_CODE as bankCode,ibi.BANK_NAME as bankName,");
        sql.append("ibi.BANK_PREFIX as bankPrefix,ibi.BANK_CITY_CODE as bankCityCode,");
        if (isBankType || isbankType) {
            sql.append("bt.ID as bankTypeId,bt.type_name as bankTypeName,bt.CREDIT_CONTRACT_REQUIRED as creditContractRequired,bt.is_third_party as isthirdparty, ");
        }
        sql.append("ibi.update_Date as updateDate,r.PARENT_ID as prov,r.prov_input_name provName,r.ID AS city ,r.name cityName,");
        sql.append("bs.SWIFT_CODE as swiftCode ");
        sql.append(" FROM BT_INPUT_BANK_INFO ibi ");
        sql.append(" INNER JOIN SYS_BANK_REGION br ON ibi.BANK_CITY_CODE = br.BANK_CITY ");
        sql.append(" INNER JOIN ").append(regionTable).append(" r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY ");
        if (isBankType || isbankType) {
            sql.append(" LEFT JOIN BT_BANK_TYPE bt ON ibi.BANK_TYPE_ID = bt.id  ");
        }
        // sql.append(" LEFT JOIN BIS_BIF_INIT bi ON bt.ID = bi.BANK_TYPE_ID ");
        // 关联境内银行Swift，按开户行行号取 swiftCode
        sql.append(" LEFT JOIN BT_INPUT_BANK_SWIFT bs ON ibi.SYS_BANK_CODE = bs.BANK_CODE ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotBlank(bankCode)) {
            sql.append(" and ibi.SYS_BANK_CODE like :bankCode ");
            String bankCodeReplace = bankCode.replace(" ", "%");
            map.put("bankCode", "%" + bankCodeReplace + "%");
        }
        if (StringUtils.isNotBlank(swiftCode)) {
            sql.append(" and bs.SWIFT_CODE like :swiftCode ");
            map.put("swiftCode", "%" + swiftCode.replace(" ", "%") + "%");
        }
        if (StringUtils.isNotBlank(bankName)) {
            String[] split = bankName.split(" ");
            if (split.length > 0) {
                sql.append("and ");
                for (int i = 0; i < split.length; i++) {
                    sql.append(" ibi.BANK_NAME like :bankName").append(i).append(" ");
                    map.put("bankName" + i, "%" + split[i] + "%");
                    if (i != split.length - 1) {
                        sql.append(" and ");
                    }
                }
            } else {
                sql.append(" and ibi.BANK_NAME like :bankName ");
                map.put("bankName", "%" + bankName + "%");
            }
        }
        // 需要关联银行类型时，增加bank_name_Prefix过滤条件，避免313和404出现重复数据 zuoy 20200320
        /*if (isBankType || isbankType) {
            sql.append(" and (bt.bank_name_Prefix is null or ibi.bank_name like concat('%', bt.bank_name_Prefix,'%')) ");
        }*/
        // 银行类型下拉选项
        if (isbankType) {
            sql.append(" and bt.ID = :bankType ");
            map.put("bankType", bankType);
        }
        String province = (String) dataTablesInput.getFormdata().get("province");
        String city = (String) dataTablesInput.getFormdata().get("city");
        if (StringUtils.isNotBlank(province) && StringUtils.isNotBlank(city)) {
            sql.append(" and r.ID = :regionId ");
            map.put("regionId", city);
        } else if (StringUtils.isNotBlank(province) && StringUtils.isBlank(city)) {
            sql.append(" and r.PARENT_ID = :regionId ");
            map.put("regionId", province);
        }
        //列表排序 默认的排序没有效果，手动增加排序
//        int orderSize =dataTablesInput.getOrder().size();
//        int columnSize = dataTablesInput.getColumns().size();
//        for(int i=0;i<orderSize;i++) {
//            Order order = dataTablesInput.getOrder().get(i);
//            //前后端分离的排序 只有1列的时候，才会有排序
//            if(columnSize == 1){
//                if("asc".equals(order.getDir())){
//                    sql.append(" order by ibi.sys_bank_code asc ");
//                }else{
//                    sql.append(" order by ibi.sys_bank_code desc ");
//                }
//            }
//            if (1 == order.getColumn() && "asc".equals(order.getDir())) {
//                sql.append(" order by ibi.sys_bank_code asc ");
//            } else if (1 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
//                sql.append(" order by ibi.sys_bank_code desc ");
//            }
//        }
        return this.getDynamicBySQL(dataTablesInput, sql.toString(), BtInputBankInfoVO.class, "t", false, map);
        //return getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), map, BtInputBankInfoVO.class);
    }
    /**
     * Created by chenlj 2018/4/20
     * 获取所有的维护了前缀的银行信息
     * update by shenc
     * 传入开户行号直接查询是否存在,避免返回结果过多
     *
     * @return
     */
    public BtInputBankInfoVO getBankPrefix(String bankSourceCode) {
        final Table table = Region.class.getAnnotation(Table.class);
        String regionTable = table.name();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" ibi.id as id,");
        sql.append(" ibi.SYS_BANK_CODE as bankCode, ");
        sql.append(" ibi.BANK_NAME as bankName, ");
        sql.append(" ibi.BANK_PREFIX as bankPrefix, ");
        sql.append(" ibi.BANK_CITY_CODE as bankCityCode, ");
        sql.append(" r.PARENT_ID as prov, ");
        sql.append(" r.ID as city, ");
        sql.append(" bt.ID as bankTypeId ");
        sql.append(" FROM BT_INPUT_BANK_INFO ibi ");
        sql.append(" INNER JOIN SYS_BANK_REGION br ON ibi.BANK_CITY_CODE = br.BANK_CITY ");
        sql.append(" INNER JOIN ").append(regionTable).append(" r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY ");
        sql.append(" INNER JOIN BT_BANK_TYPE bt ON ibi.BANK_TYPE_ID = bt.id  ");
        sql.append(" where 1=1 and ibi.SYS_BANK_CODE = :bankCode");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("bankCode", bankSourceCode);
        return getBySQLAliasToBean(sql.toString(), params, BtInputBankInfoVO.class);
    }
    /**
     * 根据联行号或开户行名称获取联行号信息
     *
     * @param bankCode
     * @param bankName
     * @return
     */
    public BtInputBankInfoVO getByBankCodeOrBankNameAndImport(String bankCode, String bankName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ibi.id as id,ibi.sys_bank_code as bankcode,ibi.bank_name as bankname,ibi.bank_prefix as bankprefix, ");
        sql.append(" ibi.bank_city_code as bankcitycode,r.parent_id as prov, r.id as city, bt.id as bankTypeId, bt.type_name as bankTypeName, bs.swift_code as swiftCode ");
        sql.append(" from bt_input_bank_info ibi ");
        sql.append(" left JOIN BT_BANK_TYPE bt ON ibi.BANK_TYPE_ID = bt.id  ");
        sql.append(" inner join sys_bank_region br on ibi.bank_city_code = br.bank_city ");
        sql.append(" inner join sys_region r on br.bank_input_city = r.bank_input_city  ");
        sql.append(" left join bt_input_bank_swift bs on ibi.sys_bank_code = bs.bank_code");
        sql.append(" where 1=1 ");
        HashMap<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(bankCode)) {
            sql.append(" and ibi.sys_bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        if (StringUtils.isNotBlank(bankName)) {
            sql.append(" and ibi.bank_name = :bankName");
            params.put("bankName", bankName);
        }
        logger.info("代发工资导入sql:"+sql.toString());
        return getBySQLAliasToBean(sql.toString(), params, BtInputBankInfoVO.class);
    }
    /**
     * 根据联行号信息表对客商账户表相同银行行号不同银行名称，客商账户表银行行名修改为联行号表中的银行名称
     *
     * @return
     */
    public int updateExternalAccBankNameByInputBankInfo() {
        StringBuilder sql = new StringBuilder();
        sql.append(" update sys_external_corp_bankacc ");
        sql.append(" set bank=(select bi.bank_name from bt_input_bank_info bi ");
        sql.append(" where bank_sourcecode=bi.sys_bank_code and bi.bank_city_code is not null ) ");
        sql.append(" where id in ( ");
        sql.append(" select tmp.id from ( ");
        sql.append(" select eb.id from sys_external_corp_bankacc eb ");
        sql.append(" left join bt_input_bank_info bi on eb.bank_sourcecode=bi.sys_bank_code and bi.bank_city_code is not null ");
        sql.append(" where eb.bank!=bi.bank_name ) tmp ) ");
        return this.batchSQLExecute(sql.toString());
    }
    /**
     * 根据联行号信息表对银行账户表相同银行行号不同银行名称，银行账户表银行行名修改为联行号表中的银行名称
     *
     * @return
     */
    public int updateBankAccBankNameByInputBankInfo() {
        StringBuilder sql = new StringBuilder();
        sql.append(" update bt_bank_acc ");
        sql.append(" set bank_name=(select bi.bank_name from bt_input_bank_info bi ");
        sql.append(" where bank_code=bi.sys_bank_code and bi.bank_city_code is not null ) ");
        sql.append(" where id in ( ");
        sql.append(" select tmp.id from ( ");
        sql.append(" select ba.id from bt_bank_acc ba ");
        sql.append(" left join bt_input_bank_info bi on ba.bank_code=bi.sys_bank_code and bi.bank_city_code is not null ");
        sql.append(" where ba.bank_name!=bi.bank_name ) tmp ) ");
        return this.batchSQLExecute(sql.toString());
    }
    public List<BtInputBankInfo> getBankInfoForNoBTBank(List<String> bankCodes) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select bi.id,bi.sys_bank_code as bankCode,bi.bank_name,bi.bank_prefix,bi.bank_city_code,bi.ibps_code,bi.prov,bi.city,bi.create_date,bi.create_by,bi.update_date,bi.update_by ");
        sql.append(" from Bt_Input_Bank_Info bi ");
        sql.append(" LEFT JOIN Bt_Bank b ON bi.sys_bank_code = b.sys_bank_code ");
        sql.append(" where b.id is null ");
        sql.append(" and bi.SYS_BANK_CODE in :bankCodes");
        Map<String, Object> params = Maps.newHashMap();
        params.put("bankCodes", bankCodes);
        List mapList = listBySQL(sql.toString(), params);
        if (EmptyUtils.isEmpty(mapList)) {
            return null;
        }
        return BeanUtils.copyMapListProperties(mapList, BtInputBankInfo.class);
    }
    public List<InputBankInfoDTO> getBankCodeInfoByBankCodes(List<String> bankCodes) {
        final Table table = Region.class.getAnnotation(Table.class);
        String regionTable = table.name();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" ibi.id as id,");
        sql.append(" ibi.SYS_BANK_CODE as bankCode, ");
        sql.append(" ibi.BANK_NAME as bankName, ");
        sql.append(" ibi.BANK_PREFIX as bankPrefix, ");
        sql.append(" ibi.BANK_CITY_CODE as bankCityCode, ");
        sql.append(" ibi.swift_Code as swiftCode,");
        sql.append(" r.PARENT_ID as prov, ");
        sql.append(" r.PROV_INPUT_NAME as provName, ");
        sql.append(" r.ID as city, ");
        sql.append(" r.Name as cityName, ");
        sql.append(" bt.ID as bankTypeId ");
        sql.append(" FROM BT_INPUT_BANK_INFO ibi ");
        sql.append(" INNER JOIN SYS_BANK_REGION br ON ibi.BANK_CITY_CODE = br.BANK_CITY ");
        sql.append(" INNER JOIN ").append(regionTable).append(" r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY ");
        sql.append(" left JOIN BT_BANK_TYPE bt ON ibi.BANK_TYPE_ID = bt.id ");
        sql.append(" where 1=1 ");
        String bankCodeSql = SqlUtil.getInSql(bankCodes, "ibi.SYS_BANK_CODE", true);
        sql.append(" and ").append(bankCodeSql).append(" ");
        return listBySQLAliasToBean(sql.toString(), null, InputBankInfoDTO.class);
    }

    public List<InputBankInfoDTO> getBankCodeInfoByBankTypeIds(List<String> bankTypeIds) {
        final Table table = Region.class.getAnnotation(Table.class);
        String regionTable = table.name();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" ibi.id as id,");
        sql.append(" ibi.SYS_BANK_CODE as bankCode, ");
        sql.append(" ibi.BANK_NAME as bankName, ");
        sql.append(" ibi.BANK_PREFIX as bankPrefix, ");
        sql.append(" ibi.BANK_CITY_CODE as bankCityCode, ");
        sql.append(" r.PARENT_ID as prov, ");
        sql.append(" r.PROV_INPUT_NAME as provName, ");
        sql.append(" r.ID as city, ");
        sql.append(" r.Name as cityName, ");
        sql.append(" bt.ID as bankTypeId ");
        sql.append(" FROM BT_INPUT_BANK_INFO ibi ");
        sql.append(" INNER JOIN SYS_BANK_REGION br ON ibi.BANK_CITY_CODE = br.BANK_CITY ");
        sql.append(" INNER JOIN ").append(regionTable).append(" r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY ");
        sql.append(" left JOIN BT_BANK_TYPE bt ON ibi.BANK_TYPE_ID = bt.id  ");
        sql.append(" where 1=1 ");
        String bankCodeSql = SqlUtil.getInSql(bankTypeIds, "bt.id", true);
        sql.append(" and ").append(bankCodeSql).append(" ");
        return listBySQLAliasToBean(sql.toString(), null, InputBankInfoDTO.class);
    }
    
    public List<InputBankInfoDTO> getBankCodeInfoByBankNames(List<String> bankNames) {
        final Table table = Region.class.getAnnotation(Table.class);
        String regionTable = table.name();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" ibi.id as id,");
        sql.append(" ibi.SYS_BANK_CODE as bankCode, ");
        sql.append(" ibi.BANK_NAME as bankName, ");
        sql.append(" ibi.BANK_PREFIX as bankPrefix, ");
        sql.append(" ibi.BANK_CITY_CODE as bankCityCode, ");
        sql.append(" r.PARENT_ID as prov, ");
        sql.append(" r.PROV_INPUT_NAME as provName, ");
        sql.append(" r.ID as city, ");
        sql.append(" r.Name as cityName, ");
        sql.append(" bt.ID as bankTypeId ");
        sql.append(" FROM BT_INPUT_BANK_INFO ibi ");
        sql.append(" INNER JOIN SYS_BANK_REGION br ON ibi.BANK_CITY_CODE = br.BANK_CITY ");
        sql.append(" INNER JOIN ").append(regionTable).append(" r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY ");
        sql.append(" INNER JOIN BT_BANK_TYPE bt ON ibi.BANK_TYPE_ID = bt.id  ");
        sql.append(" where 1=1 ");
        String bankCodeSql = SqlUtil.getInSql(bankNames, "ibi.BANK_NAME", true);
        sql.append(" and ").append(bankCodeSql).append(" ");
        return listBySQLAliasToBean(sql.toString(), null, InputBankInfoDTO.class);
    }
    
    public List<InputBankInfoDTO> getBankListCheck(List<String> bankCodes, String bankPrefix) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" SELECT ");
        sql.append(" ibi.id as id,");
        sql.append(" ibi.SYS_BANK_CODE as bankCode, ");
        sql.append(" ibi.BANK_NAME as bankName, ");
        sql.append(" ibi.BANK_PREFIX as bankPrefix, ");
        sql.append(" ibi.BANK_CITY_CODE as bankCityCode, ");
        sql.append(" bt.ID as bankTypeId, ");
        sql.append(" bt.type_name as bankTypeName ");
        sql.append(" FROM bt_bank_type bt INNER JOIN bt_input_bank_info ibi ON ibi.BANK_TYPE_ID = bt.id  ");
        String bankCodeSql = SqlUtil.getInSql(bankCodes, "ibi.SYS_BANK_CODE", true);
        sql.append(" where ").append(bankCodeSql).append(" ");
        if (!EmptyUtils.isEmpty(bankPrefix)) {
            sql.append(" and bt.id in (:bankTypeId) ");
            params.put("bankTypeId", bankPrefix.split(","));
            return listBySQLAliasToBean(sql.toString(), params, InputBankInfoDTO.class);
        }else {
            return listBySQLAliasToBean(sql.toString(), null, InputBankInfoDTO.class);
        }

    }
    public BtBankDTO getByBankName(String bankName) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select ibi.id as id, ibi.SYS_BANK_CODE as bankCode,ibi.BANK_NAME as bankName, ibi.BANK_PREFIX as bankPrefix,");
        sql.append("  ibi.BANK_CITY_CODE as bankCityCode, r.PARENT_ID as prov, r.ID as city");
        sql.append(
                "   FROM BT_INPUT_BANK_INFO ibi  INNER JOIN SYS_BANK_REGION br  ON ibi.BANK_CITY_CODE = br.BANK_CITY");
        sql.append(
                "  INNER JOIN SYS_REGION r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY  where 1 = 1 and ibi.BANK_NAME=:bankName ");
        params.put("bankName", bankName);
        return getBySQLAliasToBean(sql.toString(), params, BtBankDTO.class);
    }
    public BtBankDTO getBankName(String bankCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select ibi.id as id, ibi.SYS_BANK_CODE as bankCode,ibi.BANK_NAME as bankName, ibi.BANK_PREFIX as bankPrefix,");
        sql.append("  ibi.BANK_CITY_CODE as bankCityCode, r.PARENT_ID as prov, r.ID as city");
        sql.append(
                "   FROM BT_INPUT_BANK_INFO ibi  INNER JOIN SYS_BANK_REGION br  ON ibi.BANK_CITY_CODE = br.BANK_CITY");
        sql.append(
                "  INNER JOIN SYS_REGION r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY  where 1 = 1 and ibi.SYS_BANK_CODE=:bankCode ");
        params.put("bankCode", bankCode);
        return getBySQLAliasToBean(sql.toString(), params, BtBankDTO.class);
    }
    public List<Map<String, Object>> queryBankNameByPaysyBbankCode(String paysysbankcode) {
        String sql = "select t.BANK_NAME,t.IBPS_CODE from bt_input_bank_info t where t.SYS_BANK_CODE='"
                + paysysbankcode + "'";
        List<Map<String, Object>> objStr = this.listBySQL(sql, null);
        return objStr;
    }
    
    public List<BtInputBankInfoVO> getByBankCodesList(List<String> bankCodes) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ibi.id as id,ibi.sys_bank_code as bankcode,ibi.bank_name as bankname,ibi.bank_prefix as bankprefix, ");
        sql.append(" ibi.bank_city_code as bankcitycode,r.parent_id as prov, r.id as city, bt.id as bankTypeId, bt.type_name as bankTypeName ");
        sql.append(" from bt_input_bank_info ibi ");
        sql.append(" left JOIN BT_BANK_TYPE bt ON ibi.BANK_TYPE_ID = bt.id  ");
        sql.append(" inner join sys_bank_region br on ibi.bank_city_code = br.bank_city ");
        sql.append(" inner join sys_region r on br.bank_input_city = r.bank_input_city where 1=1 ");
        if(CollectionUtils.isEmpty(bankCodes)) {
        	sql.append(" limit 10 ");
        }else {
            sql.append(" and ").append(getSQLIn(bankCodes, 900, "ibi.sys_bank_code"));//超过1000出错
            }
        logger.info("代发工资导入sql:"+sql.toString());
        return listBySQLAliasToBean(sql.toString(), null, BtInputBankInfoVO.class);
    }
}
