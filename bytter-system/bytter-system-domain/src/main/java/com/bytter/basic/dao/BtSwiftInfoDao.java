package com.bytter.basic.dao;
import com.bytter.basic.entity.BtSwiftInfo;
import com.bytter.basic.vo.BtSwiftInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 境外银行信息数据层
 *
 * @author qinl
 * @date 2019-07-19
 */
@Repository
public class BtSwiftInfoDao extends BaseDao<BtSwiftInfo, String> {
    /**
     * @return 通用sql查询语句
     */
    private StringBuilder sqlForSelectAll() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" t.id as id,t.swift_code as swiftCode,t.bank_name as bankName,t.country_code as countryCode,t.city_name as cityName,t.bank_add as bankAdd");
        sql.append(" ,t.create_by as createBy,t.create_date as createDate,t.update_by as updateBy,t.update_date as updateDate");
        sql.append(" ,c.name_zh as nameZh,c.name_en as nameEn,a.name_zh as cityNameZh,t.cls_Flag as clsFlag,t.local_Clear_No as localClearNo");
        sql.append(" ,t.bank_type_id as bankTypeId,bbt.type_name as bankTypeName ");
        sql.append(" FROM bt_swift_info t");
        sql.append(" LEFT JOIN sys_country_cust c");
        sql.append(" ON t.country_code=c.id");
        sql.append(" LEFT JOIN sys_country_cust a");
        sql.append(" ON t.city_name=a.id");
        sql.append(" LEFT JOIN bt_bank_type bbt ON t.bank_type_id = bbt.id");
        sql.append(" WHERE 1 = 1");
        return sql;
    }
    /**
     * <p>
     * 查询列表数据
     * </p>
     *
     * @param dataTablesInput [条件]
     * @return DataTablesOutput数据源
     */
    public DataTablesOutput selectDataTable(DataTablesInput dataTablesInput) {
        HashMap<String, String> params = new HashMap<>();
        String sql = sqlForSelectAll().toString();
        return getDynamicBySQL(dataTablesInput, sql, BtSwiftInfoVO.class, "t", false, params);
    }
    /**
     * <p>
     * 查询所有记录集合
     * </p>
     *
     * @return 境外银行信息集合
     */
    public List<BtSwiftInfo> selectList() {
        return listBySQLAliasToBean(sqlForSelectAll().toString(), null, BtSwiftInfo.class);
    }
    /**
     * <p>
     * 根据条件，查询一条记录
     * </p>
     *
     * @param btSwiftInfo [条件集合]
     * @return (Entity)
     */
    public BtSwiftInfo selectOne(BtSwiftInfo btSwiftInfo) {
        if (btSwiftInfo == null) {
            return null;
        }
        StringBuilder sql = sqlForSelectAll();
        HashMap<String, String> params = new HashMap<>();
        //拼接条件
        if (!conditionSplice(sql, params, btSwiftInfo)) {
            return null;
        }
        return this.getBySQLAliasToBean(sql.toString(), params, BtSwiftInfo.class);
    }
    /**
     * <p>
     * 根据条件，查询一条记录
     * </p>
     *
     * @param btSwiftInfoVO [条件集合]
     * @return (VO)
     */
    public BtSwiftInfoVO selectOne(BtSwiftInfoVO btSwiftInfoVO) {
        if (btSwiftInfoVO == null) {
            return null;
        }
        StringBuilder sql = sqlForSelectAll();
        Map<String, String> params = new HashMap<>();
        //拼接条件
        if (!conditionSplice(sql, params, btSwiftInfoVO)) {
            return null;
        }
        return this.getBySQLAliasToBean(sql.toString(), params, BtSwiftInfoVO.class);
    }
    /**
     * <p>
     * 根据 <b>id</b> ，查询一条记录
     * </p>
     *
     * @param id [id]
     * @return (Entity)
     */
    public BtSwiftInfo selectById(String id) {
        StringBuilder sql = sqlForSelectAll();
        HashMap<String, String> params = new HashMap<>();
        sql.append(" AND t.id = :id ");
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, BtSwiftInfo.class);
    }
    /**
     * <p>
     * 根据 <b>id</b> ，查询一条记录
     * </p>
     *
     * @param id [id]
     * @return (VO)
     */
    public BtSwiftInfoVO selectVOById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        StringBuilder sql = sqlForSelectAll();
        Map<String, String> params = new HashMap<>();
        sql.append(" AND t.id = :id ");
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, BtSwiftInfoVO.class);
    }
    /**
     * <p>
     * 公用拼接sql语句
     * </p>
     *
     * @param stringBuilder [sql语句]
     * @param params        [sql条件参数]
     * @param btSwiftInfoVO [参数值]
     * @return boolean
     */
    private boolean conditionSplice(StringBuilder stringBuilder, Map<String, String> params, BtSwiftInfoVO btSwiftInfoVO) {
        //条件拼接
        boolean checkFlag = false;
        if (StringUtils.isNotEmpty(btSwiftInfoVO.getSwiftCode())) {
            stringBuilder.append(" AND t.swift_code = :swift_code ");
            params.put("swift_code", btSwiftInfoVO.getSwiftCode());
            checkFlag = true;
        }
        if (StringUtils.isNotEmpty(btSwiftInfoVO.getBankName())) {
            stringBuilder.append(" AND t.bank_name = :bank_name ");
            params.put("bank_name", btSwiftInfoVO.getBankName());
            checkFlag = true;
        }
        return checkFlag;
    }
    /**
     * <p>
     * 公用拼接sql语句
     * </p>
     *
     * @param stringBuilder [sql语句]
     * @param params        [sql条件参数]
     * @param btSwiftInfo   [参数值]
     * @return boolean
     */
    private boolean conditionSplice(StringBuilder stringBuilder, Map<String, String> params, BtSwiftInfo btSwiftInfo) {
        //条件拼接
        boolean checkFlag = false;
        if (StringUtils.isNotEmpty(btSwiftInfo.getBankName())) {
            stringBuilder.append(" AND t.bank_name = :bank_name ");
            params.put("bank_name", btSwiftInfo.getBankName());
            checkFlag = true;
        }
        if (StringUtils.isNotEmpty(btSwiftInfo.getSwiftCode())) {
            stringBuilder.append(" AND t.swift_code = :swift_code ");
            params.put("swift_code", btSwiftInfo.getSwiftCode());
            checkFlag = true;
        }
        return checkFlag;
    }

    public List<BtSwiftInfoVO> selectByCountryCode(String countryCode) {
        StringBuilder sql = sqlForSelectAll();
        HashMap<String, String> params = new HashMap<>();
        sql.append(" AND t.country_code = :countryCode ");
        params.put("countryCode", countryCode);
        return listBySQLAliasToBean(sql.toString(), params, BtSwiftInfoVO.class);
    }
}
