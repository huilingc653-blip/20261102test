package com.bytter.cashVoucher.dao;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.bill.api.bill.enums.BlankvouMessageEnum;
import com.bytter.cashVoucher.entity.BvVoucherType;
import com.bytter.cashVoucher.vo.BvVoucherTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 凭证类型维护
 * @date 2017-05-25 14:20:46
 */
@Repository
public class BvVoucherTypeDao extends BaseDao<BvVoucherType, String> {
    // 通用数据库关键字（可根据需要扩展）
    private static final Set<String> DB_KEYWORDS = new HashSet<>();
    static {
        // 基础关键字
        DB_KEYWORDS.add("SELECT");
        DB_KEYWORDS.add("INSERT");
        DB_KEYWORDS.add("UPDATE");
        DB_KEYWORDS.add("DELETE");
        DB_KEYWORDS.add("TABLE");
        DB_KEYWORDS.add("VIEW");
        DB_KEYWORDS.add("DATABASE");
        DB_KEYWORDS.add("SCHEMA");
        DB_KEYWORDS.add("INDEX");
        DB_KEYWORDS.add("PRIMARY");
        DB_KEYWORDS.add("FOREIGN");
        DB_KEYWORDS.add("KEY");
        DB_KEYWORDS.add("WHERE");
        DB_KEYWORDS.add("FROM");
        DB_KEYWORDS.add("JOIN");
    }
    /**
     * 查询所有凭证类型
     *
     * @return
     */
    public List<BvVoucherType> queryVoucherType() {
        Map<String, Object> params = new HashMap<>();
        String[] validSigns = {"0", "1"};
        params.put("validSign", validSigns);
        String hql = "from BvVoucherType where validSign in(:validSign)";
        return this.listByHQL(hql, params);
    }

    /**
     * 查询所有有效凭证类型
     *
     * @return
     */
    public List<BvVoucherType> queryVoucherTypeValid() {
        return listBy("validSign", "1");
    }

    /**
     * 获取需要入库且有效的凭证类型
     *
     * @return
     */
    public List<BvVoucherType> queryVoucherTypeValidByInbox() {
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", "1");
        params.put("isInbox", "1");
        String hql = "from BvVoucherType where validSign=:validSign and isInbox=:isInbox";
        return this.listByHQL(hql, params);
    }

    /**
     * 判断输入的表或视图是否存在
     *
     * @param tableName
     * @return
     */
    public boolean isExistTable(String tableName) {
        if(!isValidGenericObjectName(tableName)){
            throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ERROR_CHECKDATASOURCE, BlankvouConstant.BLANKVOU_MESSAGE_ERROR_CHECKDATASOURCE);
        }
        SQLQuery query = createSQLQuery("select id,corp_id from " + tableName);
        try {
            query.list();
        } catch (Exception e) {
            if(e.getCause()!=null){ //获取错误信息
                throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ERROR_CHECKDATASOURCE,e.getCause().getMessage());
            }else{ //未知错误,请检查!
                throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ERROR_CAUSEISNULL, BlankvouConstant.BLANKVOU_MESSAGE_ERROR_CHECKDATASOURCE);
            }
        }
        return true;
    }

    /**
     * 通用表/视图名称校验（兼容多数数据库）
     * @param name 待校验的名称
     * @return true=合法，false=非法
     */
    private static boolean isValidGenericObjectName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimName = name.trim();
        // 长度限制（取通用值50，可自定义）
        if (trimName.length() > 50) {
            return false;
        }
        if (DB_KEYWORDS.contains(trimName.toUpperCase())) {
            return false;
        }
        // 通用正则：首字符字母/下划线，后续字母/数字/下划线
        String genericRegex = "^[a-zA-Z_][a-zA-Z0-9_]*$";
        return trimName.matches(genericRegex);
    }

    /**
     * 判断字段在表或视图中是否存在
     *
     * @param columnName tableName
     * @return
     */
    public boolean isExistColumn(String columnName, String tableName) {
        // todo:暂时不做校验，待调整
        // SQLQuery query = createSQLQuery("select " + columnName + " as "+ columnName+  " from " + tableName);
        // try {
        // 	query.list();
        // } catch (Exception e) {
        // 	throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ERROR_CHECKFIELDCODE,columnName,tableName);
        // }
        return true;
    }

    /**
     * 根据条件查询凭证种类信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listVoucherType(DataTablesInput dataTablesInput) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" V.ID as id,V.CODE as code,V.NAME as name,V.IS_INBOX as isInbox,V.DATA_SOURCE as dataSource,V.RMK as rmk,V.VALID_SIGN as validSign");
        sql.append(" from BV_VOUCHER_TYPE V");
        sql.append(" WHERE V.VALID_SIGN in(:VALID_SIGN)");
        params.put("VALID_SIGN", new String[]{"0", "1"});
        return getDynamicBySQL(dataTablesInput, sql.toString(), BvVoucherTypeVO.class, "V", false, params);
    }

    /**
     * 根据id查询凭证种类信息
     *
     * @param id
     * @return
     */
    public BvVoucherTypeVO getVoucherTypeById(String id) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" V.ID as id,V.CODE as code,V.NAME as name,V.IS_INBOX as isInbox,V.DATA_SOURCE as dataSource,V.RMK as rmk,V.VALID_SIGN as validSign");
        sql.append(" from BV_VOUCHER_TYPE V");
        sql.append(" WHERE V.VALID_SIGN in(:validSigns)");
        sql.append(" AND V.ID=:ID");
        String[] validSigns = {"0", "1"};
        params.put("validSigns", validSigns);
        params.put("ID", id);
        BvVoucherTypeVO bvVoucherTypeVo = this.getBySQLAliasToBean(sql.toString(), params, BvVoucherTypeVO.class);
        return bvVoucherTypeVo;
    }

    /**
     * 校验输入的凭证种类代码是否重复
     *
     * @param opType
     * @param code
     * @param id
     * @return
     */
    public int checkVoucherTypeCode(String opType, String code, String id) {
        StringBuilder sql = new StringBuilder("select V.id as id from BV_VOUCHER_TYPE V where V.valid_sign<>:validSign");
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("validSign", "2");
        if ("update".equals(opType) && StringUtils.isNotBlank(id)) {
            sql.append(" and V.id<>:id");
            params.put("id", id);
        }
        if (StringUtils.isNotBlank(code)) {
            sql.append(" and V.code=:code");
            params.put("code", code);
        }
        @SuppressWarnings("rawtypes")
        List list = this.listBySQL(sql.toString(), params);
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
