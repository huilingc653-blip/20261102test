package com.bytter.biz.bdlink.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.entity.BisAccBal;
import com.bytter.biz.bdlink.vo.BisAccBalVO;
import com.bytter.biz.bdlink.vo.BisAccDtlBillVO;
import com.bytter.biz.bdlink.vo.BtBankAccBalVO;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.DataTableHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.persistence.PropertyFilter;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.constant.BtCurrencyConstant;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: BIS_ACC_BAL
 * @date 2017-06-23 15:02:30
 */
@Repository
public class BisAccBalDao extends BaseDao<BisAccBal, String> {
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id,b.acc_no,b.bank_acc,b.corp_id,b.cur_id,b.get_date as getDate,b.bal_date as balDate,b.bal,b.avail_bal,b.frz_bal,b.status ");
        sql.append(" ,b.return_Msg as returnMsg");
        sql.append(" from bis_acc_bal b where 1=1 ");
//        sql.append(" left join bt_bank_acc a on b.bank_acc = a.bank_acc where a.status <> -2 and a.valid_sign = 1 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccBalVO.class, "b", false);
    }

    /**
     * 根据id查找银行余额信息
     * zuoy 2017-06-23
     *
     * @param id
     * @return BisAccBalVO
     */
    public BisAccBalVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id,b.acc_no,b.bank_acc,b.corp_id,b.cur_id,b.get_date,b.bal_date,b.bal,b.avail_bal,b.frz_bal,b.status,b.tenant_id as tenantId ");
        sql.append(" ,b.return_Msg as returnMsg,b.CREATE_BY as createBy   from bis_acc_bal b");
        sql.append(" where b.id = :id ");
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        //params.put("validSign", CommonConstant.VALID_SIGN_YES);
        return getBySQLAliasToBean(sql.toString(), params, BisAccBalVO.class);
    }

    /**
     * 银行账户余额查询
     * zuoy 2017-06-29
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.acc_no as accNo,b.bank_acc as bankAcc,b.corp_id as corpId,b.cur_id as curId,b.get_date as getDate,"
                + " b.bal_date as balDate,b.bal as bal,b.avail_bal as availBal,b.frz_bal as frzBal,b.status as status, b.create_by as createBy ");
        sql.append(" ,b.return_Msg as returnMsg");
        sql.append(" from bis_acc_bal b  ");
        //无需过滤有效状态的账号，注销的账户也允许查看历史数据
        sql.append(" where  1=1 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        String returnCode = (String) dataTablesInput.getFormdata().get("returnCode");
        if (!EmptyUtils.isEmpty(returnCode)) {
            if (returnCode.equals("0")) {
                sql.append(" and ((b.acc_no is null and b.return_Code='0') or b.acc_no is not null) ");
            }
            if (returnCode.equals("2")) {
                sql.append(" and (b.return_Code in ('2','9','null')  or(b.acc_no is null and b.return_Code is null))");
            }
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccBalVO.class, "b", false);
    }

    /**
     * 余额查询导出全部
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public List listExport(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.acc_no as accNo,b.bank_acc as bankAcc,b.corp_id as corpId,corp.name as corp_name,cur.cur_name as curName,b.cur_id as curId,b.get_date as getDate,"
                + " b.bal_date as balDate,b.bal as bal,b.avail_bal as availBal,b.frz_bal as frzBal,b.status as status, b.create_by as createBy ");
        sql.append(" ,a.acc_name as accName,a.bank_name as bankName, a.attribute_Id as attributeId,att.attribute_name as attributeName, a.nature_id as natureId, n.nature_name  as natureName, a.acc_type as accType ");
        sql.append(" ,b.return_Msg as returnMsg,dict.type_name as accTypeName,dict1.type_name as statusLang ");
        sql.append(" from bis_acc_bal b left join bt_bank_acc a on b.bank_acc = a.bank_acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on a.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature n on a.nature_id = n.id");
        sql.append(" left join sys_corp corp on b.corp_id = corp.id ");
        sql.append(" left join bt_currency cur on b.cur_id = cur.id ");
        sql.append(" left join (select * from sys_dict where type_group_id = (select id from sys_dict_group where type_group_code = 'btBankAcc.accType')) dict on a.acc_type = dict.type_code ");
        sql.append(" left join (select * from sys_dict where type_group_id = (select id from sys_dict_group where type_group_code = 'cgsBill.status')) dict1 on b.status = dict1.type_code ");
        //无需过滤有效状态的账号，注销的账户也允许查看历史数据
        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        String returnCode = (String) dataTablesInput.getFormdata().get("returnCode");
        if (!EmptyUtils.isEmpty(returnCode)) {
            if (returnCode.equals("0")) {
                sql.append(" and ((b.acc_no is null and b.return_Code='0') or b.acc_no is not null) ");
            }
            if (returnCode.equals("2")) {
                sql.append(" and (b.return_Code in ('2','9','null')  or(b.acc_no is null and b.return_Code is null))");
            }
        }
        return exportBySQL(dataTablesInput, sql.toString(), BisAccBalVO.class, "b", false);
    }

    /**
     * 处理 自动拼接条件和排序
     * <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param input    dataTable请求对象
     * @param sql      SQL
     * @param voClazz  转换为VO
     * @param alias    别名
     * @param hasWhere 是否存在where
     * @return dataTable响应对象
     */
    protected List exportBySQL(DataTablesInput input, String sql, Class<?> voClazz, String alias, Boolean hasWhere) {
        Object bean = null;
        try {
            if (null != voClazz) {
                bean = voClazz.newInstance();
            }
            //组合附带传值问题
            List<Object> tempList = new ArrayList<Object>();
            Object[] needList = null;
            Map<String, Object> paramMap = new HashMap<String, Object>();
            if (tempList.size() > 0) {
                needList = tempList.toArray(new Object[tempList.size()]);
            }
            String allCondition = getWhereAllCondition(input, bean, alias, hasWhere, needList);
            Map<String, Object> values = getSearchValues(input, needList);
            //赋值
            if (values == null) {
                values = new HashMap<>();
            }
            values.putAll(paramMap);
            Integer fetchSize = 100000;
            List list = listBySQL(sql + allCondition, input.getStart(), input.getLength(), values, fetchSize);
            if (null != voClazz) {
                list = BeanUtils.copyMapListProperties(list, voClazz);
            }
            return list;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new ParameterException(e);
        }
    }

    /**
     * map to bean
     *
     * @return 搜索到的结果集
     */
    public Map<String, Object> getSearchValues(DataTablesInput input, Object... needParams) {
        Map<String, Object> beanMap = Maps.newHashMap();
        //表单搜索元素
        List<PropertyFilter> propertyFilters = DataTableHelper.searchWithIn(input);
        if (!propertyFilters.isEmpty()) {
            for (PropertyFilter filter : propertyFilters) {
                if (null != needParams && needParams.length > 0) {
                    List<Object> objects = Arrays.asList(needParams);
                    if (!objects.contains(filter.getPropertyName())) {
                        continue;
                    }
                }
                Object value = filter.getMatchValue();
                MatchTypeEnum matchTypeEnum = filter.getMatchType();
                if (MatchTypeEnum.LIKE.getValue().equalsIgnoreCase(filter.getMatchType().getValue())) {
                    //zhoujian 2018年6月14日15:52:08 B20180600221新增支持中间带空格模糊搜索
                    String val = String.valueOf(filter.getMatchValue());
                    if (StringUtils.isNotBlank(val)) {
                        val = val.trim().replaceAll("\\s+", "%");
                    }
                    value = "%" + val + "%";
                    //value = "%" + String.valueOf(filter.getMatchValue()) + "%";
                } else if (MatchTypeEnum.LLIKE.getValue().equalsIgnoreCase(filter.getMatchType().getValue())) {
                    matchTypeEnum = MatchTypeEnum.LIKE;
                    value = "%" + String.valueOf(filter.getMatchValue());
                } else if (MatchTypeEnum.RLIKE.getValue().equalsIgnoreCase(filter.getMatchType().getValue())) {
                    matchTypeEnum = MatchTypeEnum.LIKE;
                    value = String.valueOf(filter.getMatchValue()) + "%";
                }
                if (!filter.hasMultiProperties()) { //只有一个属性需要比较的情况.
                    beanMap.put(matchTypeEnum.name().toLowerCase() + filter.getPropertyName(), value);
                } else {//包含多个属性需要比较的情况,进行or处理.
                    for (String propertyName : filter.getPropertyNames()) {
                        beanMap.put("or" + matchTypeEnum.name().toLowerCase() + propertyName, value);
                    }
                }
            }
        }
        return beanMap;
    }

    /**
     * 根据条件查询银行账户余额
     *
     * @param bankAcc 账户
     * @param curId   币别ID，可为null
     * @return List<BisAccBalVO>
     */
    public List<BisAccBalVO> listBisAccBalBy(String bankAcc, String curId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select b.id,b.acc_no,b.bank_acc,b.corp_id,b.cur_id,b.get_date,b.bal_date,b.bal,b.avail_bal,b.frz_bal,b.status ");
        sql.append(" ,a.acc_name as accName,a.bank_name as bankName from bis_acc_bal b ");
        sql.append(" left join bt_bank_acc a on b.bank_acc = a.bank_acc ");
        sql.append(" where a.valid_sign = :validSign and b.bank_acc  = :bankAcc and b.status = :status ");
        params.put("bankAcc", bankAcc);
        params.put("status", BisAccBalConstant.STATUE_CHECK);
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        if (!EmptyUtils.isBlank(curId)) {
            sql.append(" and b.cur_id = :curId ");
            params.put("curId", curId);
        }
        sql.append(" order by b.bal_date desc ");
        return listBySQLAliasToBean(sql.toString(), params, BisAccBalVO.class);
    }

    /**
     * 判断某天账号余额是否已经审批（审批过程中）.如已经审批则不能修改余额.
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期
     * @return
     */
    public boolean isApproved(String bankAcc, String curId, Date balDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) from bis_acc_bal bal where status >= :startStatus and status < :endStatus ");
        sql.append(" and bal.bank_acc = :bankAcc and bal.cur_id = :curId and bal.bal_date = :balDate ");
        Map<String, Object> params = new HashMap<>();
        params.put("startStatus", BisAccBalConstant.STATUE_WAIT_CHECK);
        params.put("endStatus", BisAccBalConstant.STATUE_CHECK);
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        params.put("balDate", balDate);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        // 统计出来的信息大于0
        if (count.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取余额日期
     *
     * @param bankAcc  账户
     * @param curId    币别ID
     * @param balDate  比较日期，为null则查询最后一次
     * @param operator 跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
     * @param status   状态，true - 查询已完成 false - 查询未删除
     * @return
     */
    @SuppressWarnings("unchecked")
    public Date getLastBalDate(String bankAcc, String curId, Date balDate, String operator, boolean status) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        hql.append(" select");
        hql.append(" bal.id,bal.acc_no,bal.bank_acc,bal.corp_id,bal.cur_id,bal.BIF_CODE,bal.GET_DATE,bal.BAL_DATE,bal.BAL,bal.AVAIL_BAL,bal.frz_bal,bal.RETURN_CODE,bal.RETURN_MSG,BAL.STATUS,bal.IS_COMPLETE");
        hql.append(" from Bis_Acc_Bal bal where bal.bank_Acc = :bankAcc and bal.cur_Id = :curId ");
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        if (!EmptyUtils.isEmpty(balDate)) {
            hql.append("and bt_to_char(bal.bal_Date,'yyyy-MM-dd') " + MatchTypeEnum.getTypeName(operator).getValue().toLowerCase() + " :balDate ");
            params.put("balDate", DateUtils.formatDate(balDate));
        }
        hql.append(this.getCommonStatus(status));
        hql.append(" order by bal.bal_Date desc");
//		List<BisAccBal> list = this.listByHQL(hql.toString(), params);
        List<BisAccBal> list = listBySQLAliasToBean(hql.toString(), params, BisAccBal.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0).getBalDate();
    }

    /**
     * 获取指定日期的可用余额
     *
     * @param bankAcc  账户
     * @param curId    币别ID
     * @param balDate  比较日期
     * @param operator 跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
     * @param status   状态，true - 查询已完成 false - 查询未删除
     * @return
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getBalByBalDate(String bankAcc, String curId, Date balDate, String operator, boolean status) {
        return getCurrentBalByBalDate(bankAcc, curId, balDate, operator, status, null);
    }


    @SuppressWarnings("unchecked")
    public Map<String, BigDecimal> BalByBalDateForBatch(List<String> bankAccs, List<String> curIds, List<Date> balDates, String operator, boolean status) {
        return getCurrentBalByBalDateForBatch(bankAccs, curIds, balDates, operator, status, "true");
    }

    /**
     * 获取指定日期的账户余额
     *
     * @param bankAcc  账户
     * @param curId    币别ID
     * @param balDate  比较日期
     * @param operator 跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
     * @param status   状态，true - 查询已完成 false - 查询未删除
     * @return
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getBankBalByBalDate(String bankAcc, String curId, Date balDate, String operator, boolean status) {
        return getCurrentBalByBalDate(bankAcc, curId, balDate, operator, status, CommonConstant.VALID_SIGN_YES);
    }

    /**
     * 获取指定日期的余额
     *
     * @param bankAcc  账户
     * @param curId    币别ID
     * @param balDate  比较日期
     * @param operator 跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
     * @param status   状态，true - 查询已完成 false - 查询未删除
     * @return
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getCurrentBalByBalDate(String bankAcc, String curId, Date balDate, String operator, boolean status, String balFlag) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        hql.append(" select ");
        hql.append(" bal.id,bal.BAL,bal.AVAIL_BAL as availBal,bal.frz_bal ");
        hql.append(" from Bis_Acc_Bal bal where bal.bank_Acc = :bankAcc and bal.cur_Id = :curId ");
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        if (!EmptyUtils.isEmpty(balDate)) {
            hql.append("and bt_to_char(bal.bal_Date,'yyyy-MM-dd') " + MatchTypeEnum.getTypeName(operator).getValue().toLowerCase() + " :balDate ");
            params.put("balDate", DateUtils.formatDate(balDate));
        }
        hql.append(this.getCommonStatus(status));
        hql.append(" order by bal.bal_Date desc");
//		List<BisAccBal> list = this.listByHQL(hql.toString(), params);
        List<BisAccBal> list = listBySQLAliasToBean(hql.toString(), params, BisAccBal.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        if (EmptyUtils.isNotEmpty(balFlag)) {
            return list.get(0).getBal();//账户实际余额
        }
        return list.get(0).getAvailBal();
    }

    /**
     * 批量获取账户余额
     *
     * @param operator
     * @param status
     * @param balFlag
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, BigDecimal> getCurrentBalByBalDateForBatch(List<String> listBankAcc, List<String> listCurId, List<Date> listBalDate, String operator, boolean status, String balFlag) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        hql.append(" select ");
        hql.append(" bal.cur_id as curId,bal.id,bal.BAL,bal.AVAIL_BAL,bal.frz_bal,bal.bank_acc as bankAcc,bal.bal_date as balDate ");
        hql.append(" from Bis_Acc_Bal bal  where 1=1 ");
        if (!CollectionUtils.isEmpty(listBankAcc)) {
            hql.append(" and bal.bank_Acc in (:bankAcc) ");
            //String bankAccs = String.join(",", listBankAcc);
            params.put("bankAcc", listBankAcc);
        }
        if (!CollectionUtils.isEmpty(listCurId)) {
            hql.append(" and bal.cur_Id in (:curId) ");
            //String curIds= String.join(",", listCurId);
            params.put("curId", listCurId);
        }
        if (!CollectionUtils.isEmpty(listBalDate)) {
            hql.append("and bt_to_char(bal.bal_Date,'yyyy-MM-dd') in (:balDate) ");
            List<String> balDateStr = new ArrayList<String>();
            for (Date bdate : listBalDate) {
                balDateStr.add(DateUtils.formatDate(bdate));
            }
            //String dateStr = String.join(",", balDateStr);
            params.put("balDate", balDateStr);
        }
        hql.append(this.getCommonStatus(status));
        hql.append(" order by bal.bal_Date desc");
//		List<BisAccBal> list = this.listByHQL(hql.toString(), params);
        List<BisAccBal> list = listBySQLAliasToBean(hql.toString(), params, BisAccBal.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        for (BisAccBal bisbal : list) {
            if (EmptyUtils.isNotEmpty(balFlag)) {
                if (!map.containsKey(bisbal.getBankAcc() + bisbal.getCurId() + DateUtils.formatDate(bisbal.getBalDate()))) {
                    map.put(bisbal.getBankAcc() + bisbal.getCurId() + DateUtils.formatDate(bisbal.getBalDate()), bisbal.getBal());
                }
            } else {
                if (!map.containsKey(bisbal.getBankAcc() + bisbal.getCurId() + DateUtils.formatDate(bisbal.getBalDate()))) {
                    map.put(bisbal.getBankAcc() + bisbal.getCurId() + DateUtils.formatDate(bisbal.getBalDate()), bisbal.getAvailBal());
                }
            }
        }
        return map;
    }

    /**
     * 获取指定日期的余额
     *
     * @param bankAcc  账户
     * @param curId    币别ID
     * @param balDate  比较日期
     * @param operator 跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
     * @param status   状态，true - 查询已完成 false - 查询未删除
     * @return
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getBalByBalDateNew(String bankAcc, String curId, Date balDate, String operator, boolean status) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        hql.append("select");
        hql.append(" bal.id,bal.acc_no,bal.bank_acc,bal.corp_id,bal.cur_id,bal.BIF_CODE,bal.GET_DATE,bal.BAL_DATE,bal.BAL,bal.AVAIL_BAL,bal.frz_bal,bal.RETURN_CODE,bal.RETURN_MSG,BAL.STATUS,bal.IS_COMPLETE");
        hql.append(" from Bis_Acc_Bal bal where bal.bank_Acc = :bankAcc and bal.cur_Id = :curId");
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        if (!EmptyUtils.isEmpty(balDate)) {
            hql.append(" and bt_to_char(bal.bal_Date,'yyyy-MM-dd') " + MatchTypeEnum.getTypeName(operator).getValue().toLowerCase() + " :balDate ");
            params.put("balDate", DateUtils.formatDate(balDate));
        }
        hql.append(this.getCommonStatus(status));
        hql.append(" order by bal.bal_Date desc");
//		List<BisAccBal> list = this.listByHQL(hql.toString(), params);
        List<BisAccBal> list = listBySQLAliasToBean(hql.toString(), params, BisAccBal.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0).getBal();
    }

    /**
     * 根据条件查询余额表
     *
     * @param bankAcc      账户
     * @param curId        币别ID
     * @param balBeginDate 开始日期,不包含
     * @param balEndDate   结束日期,不包含
     * @param status       状态，true - 查询已完成 false - 查询未删除
     * @return BisAccBal
     */
    public List getBisAccBalBy(String bankAcc, String curId, Date balBeginDate, Date balEndDate, boolean status) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        hql.append(" select ");
        hql.append(" distinct bal.BAL_DATE");
        hql.append(" from Bis_Acc_Bal bal where bal.bank_Acc =:bankAcc and bal.cur_Id =:curId ");
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        if (!EmptyUtils.isEmpty(balBeginDate) && !EmptyUtils.isEmpty(balEndDate)) {
            hql.append("and bt_to_char(bal.bal_Date,'yyyy-MM-dd') >:balBeginDate ");
            hql.append("and bt_to_char(bal.bal_Date,'yyyy-MM-dd') <:balEndDate ");
            params.put("balBeginDate", DateUtils.formatDate(balBeginDate));
            params.put("balEndDate", DateUtils.formatDate(balEndDate));
        }
        hql.append(this.getCommonStatus(status));
        return createSQLQuery(hql.toString(), params).list();
    }

    /**
     * 根据条件查询余额表
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期,为null则查询最后一次余额
     * @param status  状态，true - 查询已完成 false - 查询未删除
     * @return BisAccBal
     */
    public BisAccBal getBisAccBalBy(String bankAcc, String curId, Date balDate, boolean status) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        //这里可能也会有显示问题
        hql.append(" select ");
        hql.append(" bal.tenant_id as tenantId,bal.id,bal.acc_no,bal.bank_acc,bal.corp_id,bal.cur_id,bal.BIF_CODE,bal.GET_DATE,");
        hql.append(" bal.BAL_DATE,bal.BAL,bal.AVAIL_BAL as availBal,bal.frz_bal,bt_nvl_num(bal.sys_frz_bal, 0) as sysFrzBal,bal.init_sign as initSign,");
        hql.append(" bt_nvl(bal.RETURN_CODE,'') as RETURN_CODE,bt_nvl(bal.RETURN_MSG,'') as RETURN_MSG,BAL.STATUS,bt_nvl(bal.IS_COMPLETE,'') as IS_COMPLETE");
        hql.append(" from Bis_Acc_Bal bal where bal.bank_Acc =:bankAcc and bal.cur_Id =:curId ");
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        if (!EmptyUtils.isEmpty(balDate)) {
            hql.append("and bt_to_char(bal.bal_Date,'yyyy-MM-dd') = :balDate ");
            params.put("balDate", DateUtils.formatDate(balDate));
        }
        hql.append(this.getCommonStatus(status));
        hql.append(" order by bal.bal_Date desc ");
//		List<BisAccBal> list = this.listByHQL(hql.toString(),0,1,params);
        List<BisAccBal> list = listBySQLAliasToBean(hql.toString(), params, BisAccBal.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }


    public List<BisAccBal> getBisAccBalByList(List<String> bankAcc, List<String> curId, Date balDate, boolean status) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        hql.append(" select ");
        hql.append(" bal.tenant_id as tenantId,bal.id,bal.acc_no,bal.bank_acc,bal.corp_id,bal.cur_id,bal.BIF_CODE,bal.GET_DATE,");
        hql.append(" bal.BAL_DATE,bal.BAL,bal.AVAIL_BAL as availBal,bal.frz_bal,bt_nvl_num(bal.sys_frz_bal, 0) as sysFrzBal,bal.init_sign as initSign,");
        hql.append(" bt_nvl(bal.RETURN_CODE,'') as RETURN_CODE,bt_nvl(bal.RETURN_MSG,'') as RETURN_MSG,BAL.STATUS,bt_nvl(bal.IS_COMPLETE,'') as IS_COMPLETE");
        hql.append(" from Bis_Acc_Bal bal where 1=1  ");
        if(!CollectionUtils.isEmpty(bankAcc)) {
        	String accresult = bankAcc.stream()
                    .filter(s -> s != null && !"".equals(s))            // 过滤空值
                    .map(s -> "'" + s + "'")           // 添加单引号
                    .collect(Collectors.joining(","));
        	hql.append(" and bal.bank_Acc in ("+accresult+")");
        }
        if(!CollectionUtils.isEmpty(curId)) {
        	String curIdresult = curId.stream()
                    .filter(s -> s != null && !"".equals(s))            // 过滤空值
                    .map(s -> "'" + s + "'")           // 添加单引号
                    .collect(Collectors.joining(","));
        	hql.append(" and bal.cur_Id in ("+curIdresult+")");
        }
        if (!EmptyUtils.isEmpty(balDate)) {
            hql.append("and bt_to_char(bal.bal_Date,'yyyy-MM-dd') = :balDate ");
            params.put("balDate", DateUtils.formatDate(balDate));
        }
        hql.append(this.getCommonStatus(status));
        hql.append(" order by bal.bal_Date desc ");
        List<BisAccBal> list = listBySQLAliasToBean(hql.toString(), params, BisAccBal.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list;
    }


    public static void main(String[] args) {
		List<String>  ll = new ArrayList<String>();
		ll.add("1");
		ll.add("2");
		ll.add("3");
		ll.add("4");
		ll.add("5");
		System.out.println("sss=="+StringUtils.join(ll, ","));
	}

    private String getCommonStatus(boolean status) {
        StringBuilder sql = new StringBuilder();
        if (status) {
            sql.append(" and (status is null or status = 95 ) ");
        } else {
            sql.append(" and status <> -2 ");
        }
        return sql.toString();
    }

    public DataTablesOutput getDataTablesApprove(DataTablesInput dataTablesInput, String idsSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id,b.acc_no,b.bank_acc,b.corp_id,b.cur_id,b.get_date,b.bal_date,b.bal,b.avail_bal,b.frz_bal,b.status ");
        sql.append(" ,a.acc_name as accName,a.bank_name as bankName ");
        sql.append(" from bis_acc_bal b ");
        sql.append(" left join bt_bank_acc a on b.bank_acc = a.bank_acc where a.status <> -2 and a.valid_sign = 1 ");
        sql.append("and" + idsSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccBalVO.class, "b", false);
    }

    /**
     * 根据单位统计银行账户余额
     *
     * @param corpId
     * @return
     */
    public int getBankAccBalCountByCorpId(String corpId) {
        String sql = "select count(*) from bis_acc_bal where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql += " and status in ('0','11','-1')";
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }

    /**
     * 查询某个时间账户余额按照币别汇总（APP首页）
     *
     * @param searchDate 查询日期
     * @param corpIds    单位集合
     * @return 余额信息集合
     */
    public List<BisAccBalVO> listBalSumByCurId(Date searchDate, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT bt_nvl_num(sum(bal.bal), 0) as bal, bal.cur_id as curId, acc.acc_type as accType ");
        sql.append(" FROM Bis_Acc_Bal bal ");
        sql.append(" inner join bt_bank_acc acc on bal.bank_acc = acc.bank_acc and acc.acc_type is not null ");
        sql.append(" WHERE bal.bal_Date >= TO_DATE(:startDate, 'yyyy-MM-dd') and bal.bal_Date < TO_DATE(:endDate, 'yyyy-MM-dd') ");
        sql.append(" and (BT_NVL(bal.status,95) = 95) ");
        sql.append(" and ").append(SqlUtil.getInSql(corpIds, "bal.corp_Id", true));
        sql.append(" group by acc.acc_type, bal.cur_id ");
        sql.append(" order by acc.acc_type");

        HashMap<String, Object> params = new HashMap<>();
        params.put("startDate", DateUtils.formatDate(searchDate));
        params.put("endDate", DateUtils.formatDate(DateUtils.addDay(searchDate, 1)));
        return this.listBySQLAliasToBean(sql.toString(), params, BisAccBalVO.class);
    }

    /**
     * 根据条件查询余额表的数量
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期,为null则查询最后一次余额
     * @param status  状态，true - 查询已完成 false - 查询未删除
     * @return BisAccBal
     */
    public Long countBisAccBalBy(String bankAcc, String curId, Date balDate, boolean status) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        hql.append(" select ");
        hql.append(" count(1) ");
        hql.append(" from Bis_Acc_Bal bal where bal.bank_Acc =:bankAcc and bal.cur_Id =:curId ");
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        if (!EmptyUtils.isEmpty(balDate)) {
            hql.append("and bt_to_char(bal.bal_Date,'yyyy-MM-dd') = :balDate ");
            params.put("balDate", DateUtils.formatDate(balDate));
        }
        hql.append(this.getCommonStatus(status));
        List list = createSQLQuery(hql.toString(), params).list();
        Long result = 0L;
        if (EmptyUtils.isNotEmpty(list) && EmptyUtils.isEmpty(list.get(0))) {
            result = ((BigDecimal) list.get(0)).longValue();
        }
        return result;
    }

    /**
     * 获取账号初始化余额信息
     *
     * @param bankAcc
     * @param curId
     * @return
     */
    public BisAccBal getInitBisAccBal(String bankAcc, String curId) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        hql.append(" select ");
        hql.append(" bal.id,bal.acc_no,bal.bank_acc,bal.corp_id,bal.cur_id,bal.BIF_CODE,bal.GET_DATE,");
        hql.append(" bal.BAL_DATE,bal.BAL,bal.AVAIL_BAL,bal.frz_bal,bal.init_sign as initSign,");
        hql.append(" bt_nvl(bal.RETURN_CODE,'') as RETURN_CODE,bt_nvl(bal.RETURN_MSG,'') as RETURN_MSG,BAL.STATUS,bt_nvl(bal.IS_COMPLETE,'') as IS_COMPLETE");
        hql.append(" from Bis_Acc_Bal bal where bal.bank_Acc =:bankAcc and bal.cur_Id =:curId ");
        hql.append(" and bal.init_sign=:initSign");
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        params.put("initSign", CommonConstant.YES);
        List<BisAccBal> list = listBySQLAliasToBean(hql.toString(), params, BisAccBal.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * <br>
     * <b>功能：</b> 查询银行账户余额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/6/10 0010 <br>
     *///TODO修改
    public List<BtBankAccBalVO> queryBankAccBalanceById(List<String> bankAccs, List<String> curIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select bal.BANK_ACC as bankAcc, bal.CUR_ID as curId,(bt_nvl_num(bal.AVAIL_BAL,0) - bt_nvl_num(bal.sys_frz_bal, 0)) as bal ");
        sql.append(" from bis_acc_bal bal inner join ");
        sql.append(" (select bank_acc,cur_id,max(bal_Date) as bal_Date from bis_acc_bal group by bank_acc, cur_id) bal1 ");
        sql.append(" on bal.bank_acc=bal1.bank_acc and bal.cur_id = bal1.cur_id and bal.bal_date = bal1.bal_date ");
        sql.append(" where 1 = 1  ");
        if (EmptyUtils.isNotEmpty(bankAccs)) {
            sql.append(" and ").append(getSQLIn(bankAccs, 900, "bal.bank_acc"));
        }
        if (EmptyUtils.isNotEmpty(curIds)) {
            sql.append(" and ").append(getSQLIn(curIds, 900, "bal.cur_id"));
        }
        return this.listBySQLAliasToBean(sql.toString(), null, BtBankAccBalVO.class);
    }

    public BtBankAccBalVO getFexCorpPoolBankAccBalance(Date balDate, String bankAcc, String curId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select bal.* from (");
        sql.append(" select bab.bank_acc as bankAcc,bab.bal_date as balDate,bab.cur_id as curId,bab.bal as bal from bis_acc_bal bab,");
        sql.append(" (select ba.bank_acc as bankAcc, ba.cur_id as curId, max(ba.bal_date) as balDate from bis_acc_bal ba");
        sql.append(" where bt_to_char(ba.bal_Date, 'yyyy-MM-dd') <= :balDate and ba.status = 95 group by ba.bank_acc, ba.cur_id) bb");
        sql.append(" where bab.bank_acc = bb.bankAcc and bab.cur_id = bb.curId and bab.bal_date = bb.balDate) bal");
        sql.append(" where 1=1 ");
        if (!StringUtils.isBlank(bankAcc)) {
            sql.append(" and bal.bankAcc = :bankAcc");
            params.put("bankAcc", bankAcc);
        }
        if (!StringUtils.isBlank(curId)) {
            sql.append(" and bal.curId = :curId");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(balDate)) {
            params.put("balDate", DateUtils.formatDate(balDate));
        } else {
            params.put("balDate", DateUtils.formatDate(DateUtils.getDate()));
        }
        List<BtBankAccBalVO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccBalVO.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }

    public List<BisAccBalDTO> getBankBal(String bankAcc, String balanceDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT fcb.bank_acc as bankAcc,fcb.cur_id as curId,fcb.bal,fcb.AVAIL_BAL as availBal,fcb.bal_date as balDate FROM bis_acc_bal fcb ");
        sql.append(" left join (SELECT bank_acc,cur_id,MAX(c.bal_date) AS bal_date");
        sql.append(" FROM bis_acc_bal c");
        sql.append(" WHERE  1=1 ");
        if (!EmptyUtils.isEmpty(bankAcc)) {    //account 拆分为list，拼接查询条件，避免in条件超长
            List<String> accList = StringUtils.splitToList(",", bankAcc);
            sql.append(" and ").append(getSQLIn(accList, 900, "bank_acc"));
        }
        if (!EmptyUtils.isEmpty(balanceDate)) {    //account 拆分为list，拼接查询条件，避免in条件超长
            sql.append(" and bal_date <='").append(balanceDate).append("'");
        }
        sql.append(" GROUP BY bank_acc,cur_id) fb");
        sql.append(" ON fb.bank_acc = fcb.bank_acc AND fb.cur_id = fcb.cur_id");
        sql.append(" WHERE  fb.bal_date = fcb.bal_date");
        return this.listBySQLAliasToBean(sql.toString(), null, BisAccBalDTO.class);
    }

    /**
     * 票据认领待确认或已确认银行流水列表
     *
     * @param dataTablesInput 查询参数
     * @param corpAuthSql     单位权限
     * @return 前端列表数据
     **/
    //todo BIS_ACC_DTL 离线账号流水补录入口必须处理新增冗余字段  ， 在线账号银企同步入口也要处理冗余字段
    public DataTablesOutput<BisAccDtlBillVO> queryBisAccDtl(DataTablesInput dataTablesInput, String corpAuthSql, List<String> claimIdList, String accStrSql, List<BisExcDTO> allBisExcBankSerialNo) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id, acc.trans_time AS transTime, b.corp_id AS corpId, ACC.corp_name AS corpName,acc.bank_acc AS bankAcc, acc.acc_name AS accName,acc.BANK_NAME AS bankName,");
        sql.append(" bankType.id AS bankTypeId, bankType.type_name as bankTypeName,acc.cd_sign AS cdSign, acc.cur_id AS curId, '人民币' as curName, ");
        sql.append(" acc.remark AS remark,acc.uses AS uses, acc.postscript AS postscript, acc.bank_serial_no AS bankSerialNo, acc.claim_id as claimId,");
        sql.append(" acc.OPP_ACC_NO AS oppAccNo, acc.OPP_ACC_NAME AS oppAccName, acc.OPP_ACC_BANK AS oppAccBank, acc.amt AS amt, acc.bal AS bal ");
        sql.append(" from BIS_ACC_DTL ACC ");
        sql.append(" LEFT JOIN AB_CLAIM_MIDDLE m ON m.id = acc.claim_id ");
        sql.append(" LEFT JOIN bt_bank_acc b ON b.bank_acc = acc.bank_acc ");
        sql.append(" LEFT JOIN BT_BANK_TYPE bankType on bankType.id = b.bank_type_id ");
        sql.append(" WHERE (ACC.STATUS <> :STATUS_2 OR ACC.STATUS IS NULL) ");
        sql.append(" AND ACC.CUR_ID = :RMB ");
        if (!CollectionUtils.isEmpty(claimIdList)) {
            sql.append(" AND ACC.claim_id in (:claimIdList) ");
            params.put("claimIdList", claimIdList);
        } else {
            String claimStatus = (String) dataTablesInput.getFormdata().get("claimStatus");
            if (AccebillConstant.BILL_CLAIM_STATUS94.equals(claimStatus)) {
                sql.append(" and m.claim_status in (:claimStatus) ");
                params.put("claimStatus", new String[]{AccebillConstant.BILL_CLAIM_STATUS94});
            } else if (AccebillConstant.BILL_CLAIM_STATUS95.equals(claimStatus)) {
                sql.append(" and m.claim_status in (:claimStatus) ");
                params.put("claimStatus", new String[]{AccebillConstant.BILL_CLAIM_STATUS95});
            } else {
                sql.append(" and (m.claim_status in (:claimStatus) or m.claim_status is null) ");
                params.put("claimStatus", new String[]{AccebillConstant.BILL_CLAIM_STATUS_1, AccebillConstant.BILL_CLAIM_STATUS11});
            }
        }
        sql.append(" AND (ACC.BLEND_SIGN = :BLEND_SIGN0 or ACC.BLEND_SIGN is NULL ) ");
        if (!CollectionUtils.isEmpty(allBisExcBankSerialNo)) {
            List<String> bankSerialNos = allBisExcBankSerialNo.stream().map(BisExcDTO::getBankSerialNo).collect(Collectors.toList());
            sql.append(" AND (ACC.BANK_SERIAL_NO is null or ACC.BANK_SERIAL_NO = '' or " + getSQLNotIn(bankSerialNos, 900, "ACC.BANK_SERIAL_NO") + ") ");
        }

        //sql.append(" (SELECT distinct ACC2.ID FROM BIS_ACC_DTL ACC2 where ACC2.BANK_SERIAL_NO is NULL or ACC2.BANK_SERIAL_NO='') ");
//        if (corpAuthSql != null) {
//            sql.append(" and ").append(corpAuthSql).append(" ");
//        }
        if (accStrSql != null) {
            sql.append(" and ").append(accStrSql).append(" ");
        }
        params.put("RMB", BtCurrencyConstant.RMBID);
        params.put("STATUS_2", CommonConstant.STATUE_DELETE);
        params.put("BLEND_SIGN0", CommonConstant.NO);
        return this.getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlBillVO.class, "", false, params);
    }

    public List<BisAccBalDTO> listBisAccBalBySql(String sql, Map<String, Object> params) {
        return this.listBySQLAliasToBean(sql.toString(), params, BisAccBalDTO.class);
    }

    /**
     * 根据日期查询境外账号余额
     *
     * @return 银行余额列表
     */
    public List<BisAccBalDTO> selectBalance(String balDate, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" a.cur_id,a.bal_date,a.bal as bal");
        sql.append(" FROM bis_acc_bal a");
        sql.append(" WHERE 1 = 1");
        sql.append(" AND a.cur_id != '4028802e5c346968015c346c18850008'");
        sql.append(" and bt_to_char ( a.bal_date , 'YYYY-MM-DD' ) = :balDate ");
        Map<String, String> map = new HashMap<>();
        map.put("balDate", balDate);
        if (StringUtils.isNotBlank(curId)) {
            sql.append(" and a.cur_id = :curId ");
            map.put("curId", curId);
        }
        return listBySQLAliasToBean(sql.toString(), map, BisAccBalDTO.class);
    }

    public DataTablesOutput getBankAccMapByAccsAndCurId(DataTablesInput input, String authSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,corp.name AS corpName, ");
        sql.append(" b.acc_no as accNo,b.bank_acc as bankAcc,b.corp_id as corpId,b.get_date as getDate,b.bal_date as balDate,b.bal as bal,b.avail_bal as availBal,b.frz_bal as frzBal,b.init_sign as initSign,bt_nvl_num(b.sys_frz_bal, 0) as sysFrzBal,b.status as status, b.create_by as createBy, ");
        sql.append(" b.return_Msg as returnMsg,acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,bif.name as bifName, ");
        sql.append(" acc.electric_bill as electricBill,acccur.cur_id as curId,cur.cur_name AS curName,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  ");
        sql.append(" acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName,acc.bank_org_code as bankOrgCode,acc.is_domestic_bank as isDomesticBank,");
        sql.append(" acccur.counter_acc as counterAccId,att.foreign_des as foreignDes ");
        sql.append(" from bt_bank_acc acc  ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join  bis_acc_bal b on acc.bank_acc = b.bank_acc and acccur.CUR_ID  = b.CUR_ID ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id ");
        sql.append(" left join sys_corp corp on acc.corp_id = corp.id ");
        sql.append(" left join bt_currency cur on cur.id = acccur.cur_id ");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' ");
        if (ObjectUtil.isEmpty(input)) {
            return getDynamicBySQL(input, sql.toString(), BisAccBalVO.class, "b", false);
        }
        //账户类别
        String ins_accType = (String) input.getFormdata().get("ins_accType");
        List<String> accTypeList = Lists.newArrayList();
        if (StringUtils.isNotBlank(ins_accType)) {
            accTypeList = Arrays.stream(ins_accType.split(CommonConstant.COMMA_SPLIT)).collect(Collectors.toList());
        }
        //银行类型
        String ins_bankTypeId = (String) input.getFormdata().get("ins_bankTypeId");
        List<String> bankTypeIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(ins_bankTypeId)) {
            bankTypeIdList = Arrays.stream(ins_bankTypeId.split(CommonConstant.COMMA_SPLIT)).collect(Collectors.toList());
        }
        //账户性质
        String ins_natrueId = (String) input.getFormdata().get("ins_natureId");
        List<String> natureIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(ins_natrueId)) {
            natureIdList = Arrays.stream(ins_natrueId.split(CommonConstant.COMMA_SPLIT)).collect(Collectors.toList());
        }
        //账户属性
        String ins_attributeId = (String) input.getFormdata().get("ins_attributeId");
        List<String> attributeIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(ins_attributeId)) {
            attributeIdList = Arrays.stream(ins_attributeId.split(CommonConstant.COMMA_SPLIT)).collect(Collectors.toList());
        }
        String eqs_curId = (String) input.getFormdata().get("eqs_curId");
        String eqs_isOnline = (String) input.getFormdata().get("eqs_isOnline");
        String returnCode=(String) input.getFormdata().get("returnCode");
        if(!EmptyUtils.isEmpty(returnCode)){
            if(returnCode.equals("0")){
                sql.append(" and ((b.acc_no is null and b.return_Code='0') or b.acc_no is not null) ");
            }
            if(returnCode.equals("2")){
                sql.append(" and b.acc_no is null and (b.return_Code <> '0' or b.return_Code is null or b.return_Code ='') ");
            }

        }
        if (!com.alibaba.excel.util.CollectionUtils.isEmpty(bankTypeIdList)) {
            sql.append(" and ").append(SqlUtil.getInSql(bankTypeIdList, "acc.bank_type_id", true));
        }
        if (!com.alibaba.excel.util.CollectionUtils.isEmpty(natureIdList)) {
            sql.append(" and ").append(SqlUtil.getInSql(natureIdList, "acc.nature_id", true));
        }
        if (!com.alibaba.excel.util.CollectionUtils.isEmpty(attributeIdList)) {
            sql.append(" and ").append(SqlUtil.getInSql(attributeIdList, "acc.attribute_id", true));
        }
        if (!com.alibaba.excel.util.CollectionUtils.isEmpty(accTypeList)) {
            sql.append(" and ").append(SqlUtil.getInSql(accTypeList, "acc.acc_type", true));
        }
        if (!EmptyUtils.isEmpty(input.getFormdata().get("accStatus"))) {
            String accStatus = input.getFormdata().get("accStatus").toString();
            if (accStatus.equals(BtBankAccConstant.ACCSTATUS_1)) {
                sql.append(" and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
            } else if (accStatus.equals(BtBankAccConstant.ACCSTATUS_2)) {
                sql.append(" and acc.status = " + BtBankAccConstant.STATUE_DEL + " ");
            }
        }
        if (!EmptyUtils.isEmpty(input.getFormdata().get("accGroup"))) {
            String accGroup = input.getFormdata().get("accGroup").toString();
            if (!StringUtils.isBlank(accGroup)) {
                sql.append(" and ").append(SqlUtil.getInSql(accGroup.split(","), "acc.fc_acc_group_code", true));
            }
        }
        if (StringUtils.isNotBlank(eqs_curId)) {
            sql.append(" and acccur.cur_id='" + eqs_curId + "'");
        }
        if (StringUtils.isNotBlank(eqs_isOnline)) {
            sql.append(" and acc.is_online='" + eqs_isOnline + "'");
        }
        if (EmptyUtils.isNotEmpty(authSql)) {
            sql.append(authSql);
        }
        return getDynamicBySQL(input, sql.toString(), BisAccBalVO.class, "b", false);
    }

    /**
     * 账户余额
     * @param corpIds
     * @param balDate
     * @param bankTypeId
     * @param bankCode
     * @return
     */
    public BigDecimal getBankAccBalForReportDate(List<String> corpIds, Date balDate, String bankTypeId, String bankCode){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(bt_nvl_num(bal.bal,0)) money");
        sql.append(" from bis_acc_bal bal ");
        sql.append(" left join bt_bank_acc acc on bal.bank_Acc = acc.bank_acc ");
        sql.append(" left join bt_bank_acc_cur bac on acc.id = bac.bank_acc_id and bal.cur_Id = bac.cur_id ");
        sql.append(" left join bt_currency btcur on bac.cur_id = btcur.id ");
        sql.append(" where bal.bal_date=:balDate ");
        sql.append(" and btcur.CUR_NAME='人民币' ");
        sql.append(" and "+SqlUtil.getInSql(corpIds, "acc.corp_id", true));
        if(StringUtils.isNotEmpty(bankTypeId)){
            sql.append(" and acc.BANK_TYPE_ID= :bankTypeId ");
            params.put("bankTypeId", bankTypeId);
        }
        if(StringUtils.isNotEmpty(bankCode)){
            sql.append(" and acc.BANK_CODE= :bankCode");
            params.put("bankCode", bankCode);
        }
        params.put("balDate",balDate);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal balance = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(result)) {
            balance = (BigDecimal) result;
        }
        return balance;
    }
}
