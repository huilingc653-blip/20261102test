package com.bytter.system.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.core.annotation.IgnoreField;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.page.IPagePara;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.AccountSearchDTO;
import com.bytter.system.api.dto.BtBankAccCurDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.*;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.entity.BtBankAccApp;
import com.bytter.system.entity.BtBankAccCur;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
//import com.bytter.system.vo.BtBankAccAppVO;
//import com.bytter.system.vo.BtBankAccBalVO;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Repository
public class BtBankAccDao extends BaseDao<BtBankAcc, String> {
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.acc_no as accNo,acc.acc_name as accName,acc.corp_id as corpId, acc.status as status ,");
        sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, ");
        sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
        sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill ");
        sql.append(" from bt_bank_acc_app acc ");
        sql.append(" left join bis_acc_attribute att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" where acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.bus_type = " + BtBankAccConstant.BUSTYPE_1 + " ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccAppVO.class, "", false);
    }

    /**
     * <br>
     * <b>功能：</b> 查询银行账户余额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/6/10 0010 <br>
     */
    public List<BtBankAccBalVO> queryBankAccBalanceById(List<String> bankAccs, List<String> curIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select bal.BANK_ACC as bankAcc, bal.CUR_ID as curId,bt_nvl_num(avail_Bal,0) as bal ");
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

    /**
     * 得到开户申请的id
     *
     * @param id
     * @return
     */
    public BtBankAccApp getBtbankAccApp(String id) {
        return this.load(BtBankAccApp.class, id);
    }
    /**
     * 根据多个单位id得到银行账号列表
     *
     * @param corpIds
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public List<BtBankAcc> getListByCorpIds(List<String> corpIds) {
        StringBuilder hql = new StringBuilder();
        hql.append("select distinct id as id, corp_id as corpId, bank_acc as bankAcc,acc_name as accName,bank_name as bankName,rates as rates from bt_bank_acc where  valid_Sign = :validSign and status = :status ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        hql.append(" and ").append(getSQLIn(corpIds, 1000, "corp_Id"));
        List<BtBankAcc> list = this.listBySQLAliasToBean(hql.toString(),params,BtBankAcc.class);
        return list;
    }
    /**
     * 根据多个单位id得到银行账号列表
     *
     * @param bankTypeIds
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public List<BtBankAcc> queryListByBankTypeIds(List<String> bankTypeIds) {
        StringBuilder hql = new StringBuilder();
        hql.append("select distinct corp_id as corpId, bank_acc as bankAcc,acc_name as accName,bank_name as bankName from bt_bank_acc where  valid_Sign = :validSign and status = :status ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        hql.append(" and ").append(getSQLIn(bankTypeIds, 1000, "bank_type_id"));
        List<BtBankAcc> list = this.listBySQLAliasToBean(hql.toString(),params,BtBankAcc.class);
        return list;
    }
    /**
     * 根据VO所传的条件获取有效的银行账号集合
     */
    public List<BtBankAccVO> getListByVO(BtBankAccVO vo, StringBuilder corpSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id,b.attribute_id,b.acc_type,b.bank_type_id,b.bank_acc,b.valid_sign,b.status,a.cur_id");
        sql.append(" from bt_bank_acc_cur a");
        sql.append(" left join bt_bank_acc b on b.id = a.bank_acc_id");
        sql.append(" left join bt_bank_type c on c.id = b.bank_type_id where b.status = :status ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        if (StringUtils.isNotBlank(vo.getCorpId())) {
            String[] ids = vo.getCorpId().split(",");
            sql.append(" AND").append(SqlUtil.getInSql(ids, "b.corp_id", true));
        }
        if (StringUtils.isNotBlank(vo.getAttributeId())) {
            sql.append(" AND b.attribute_id = '").append(vo.getAttributeId()).append("'");
        }
        if (StringUtils.isNotBlank(vo.getAccType())) {
            sql.append(" AND b.acc_type = '").append(vo.getAccType()).append("'");
        }
        if (StringUtils.isNotBlank(vo.getBankAcc())) {
            sql.append(" AND b.bank_acc like '%").append(vo.getBankAcc()).append("%'");
        }
        if (StringUtils.isNotBlank(vo.getValidSign())) {
            sql.append(" AND b.valid_sign = '").append(vo.getValidSign()).append("'");
        }
        sql.append(corpSql);
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }
    /**
     * 分页获取银行账户列表
     *
     * @param pagePara 分页对象
     * @param vo       vo
     * @param corpSql  corp
     * @return 银行账户列表
     */
    public IPageResult<BtBankAccVO> getPageListByVO(IPagePara pagePara, BtBankAccVO vo, StringBuilder corpSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id,b.attribute_id,b.acc_type,b.bank_type_id,b.bank_acc,b.valid_sign,b.status,a.cur_id,c.type_name as bankTypeName,d.cur_name,e.attribute_name");
        sql.append(" from bt_bank_acc_cur a");
        sql.append(" left join bt_bank_acc b on b.id = a.bank_acc_id");
        sql.append(" left join bt_bank_type c on c.id = b.bank_type_id ");
        sql.append(" left join bt_currency d on d.id = a.cur_id ");
        sql.append(" left join bis_acc_attribute e on e.id = b.attribute_id ");
        sql.append("  where b.status = :status ");

        Map<String, Object> params = new HashMap<>();
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        if (StringUtils.isNotBlank(vo.getCorpId())) {
            String[] ids = vo.getCorpId().split(",");
            sql.append(" AND").append(SqlUtil.getInSql(ids, "b.corp_id", true));
        }
        if (StringUtils.isNotBlank(vo.getAttributeId())) {
            sql.append(" AND b.attribute_id = '").append(vo.getAttributeId()).append("'");
        }
        if (StringUtils.isNotBlank(vo.getAccType())) {
            sql.append(" AND b.acc_type = '").append(vo.getAccType()).append("'");
        }
        if (StringUtils.isNotBlank(vo.getBankTypeId())){
            sql.append(" AND b.bank_type_id = '").append(vo.getBankTypeId()).append("'");
        }
        if (StringUtils.isNotBlank(vo.getIsOnline())){
            sql.append(" AND b.is_online = '").append(vo.getIsOnline()).append("'");
        }
        if (StringUtils.isNotBlank(vo.getBankAcc())) {
            sql.append(" AND b.bank_acc like '%").append(vo.getBankAcc()).append("%'");
        }
        if (StringUtils.isNotBlank(vo.getValidSign())) {
            sql.append(" AND b.valid_sign = '").append(vo.getValidSign()).append("'");
        }
        sql.append(corpSql);
        return this.getPageResultBySQL(pagePara, sql.toString(), params, BtBankAccVO.class);
    }
    /**
     * 根据id查找银行信息
     * zuoy 2017-06-22
     *
     * @param id
     * @param curId 可为null
     * @return BtBankAccVO
     */
    public BtBankAccVO getByUnion(String id, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id, acc.bank_acc, acc.acc_name,  acc.corp_id, acc.bank_name, acc.bank_code, acc.prov, acc.city ");
        sql.append(" ,acc.bif_code,bif.name bifName,acc.bank_type_id as bankTypeId ");
        if (EmptyUtils.isBlank(curId)) {
            sql.append(" from bt_bank_acc acc ");
        } else {
            sql.append(" ,cur.cur_id curId, cur.counter_acc as counterAccId, cur.prent_acc_id as prentAccId ");
            sql.append(" from bt_bank_acc acc left join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id ");
        }
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" where acc.id=:id and acc.valid_sign= :validSign and acc.status= :status ");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        if (!EmptyUtils.isBlank(curId)) {
            sql.append(" and cur.cur_id =:curId ");
            params.put("curId", curId);
        }
        return getBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }
    /**
     * 根据银行账号各个条件查询银行账号列表
     *
     * @param dto
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({"unchecked", "rawtypes", "deprecation"})
    public List<BtBankAccDTO> getBankAccDTOList(BtBankAccDTO dto) {
        StringBuilder sql = new StringBuilder();
        // 如果借款银行行号不为空，则需要优先显示相同开户行名称的账号
        if (StringUtils.isNotEmpty(dto.getBankCodeStr())) {
            sql.append(" select temp.*, case when temp.BANKCODE = '").append(dto.getBankCodeStr()).append("' then 1 else 0 end as ind from ( ");
        }
        sql.append(" select ");
        sql.append(" acc.ID as ID, ");
        sql.append(" acc.BANK_ACC as BANKACC, ");
        sql.append(" acc.ACC_NAME as ACCNAME, ");
        sql.append(" acc.PARENT_SIGN as PARENTSIGN, ");
        sql.append(" acc.CORP_ID as CORPID, ");
        sql.append(" acc.ACC_TYPE as ACCTYPE, ");
        sql.append(" acc.ATTRIBUTE_ID as ATTRIBUTEID, ");
        sql.append(" acc.NATURE_ID as NATUREID, ");
        sql.append(" acc.BANK_TYPE_ID as BANKTYPEID, ");
        sql.append(" acc.BANK_CODE as BANKCODE, ");
        sql.append(" acc.BANK_NAME as BANKNAME, ");
        sql.append(" acc.PROV as PROV, ");
        sql.append(" acc.CITY as CITY, ");
        sql.append(" acc.BIF_CODE as BIFCODE, ");
        sql.append(" acc.ELECTRIC_BILL as ELECTRICBILL, ");
        sql.append(" acc.REGULARS as REGULARS, ");
        sql.append(" acc.REGULAR_DATE as REGULARDATE, ");
        sql.append(" acc.NOTICE_TYPE as NOTICETYPE, ");
        sql.append(" acc.AGR_MONEY as AGRMONEY, ");
        sql.append(" acc.AGR_INTEREST as AGRINTEREST, ");
        sql.append(" acc.AGR_CONTRACT as AGRCONTRACT, ");
        sql.append(" acc.AGR_BEGIN_DATE as AGRBEGINDATE, ");
        sql.append(" acc.AGR_END_DATE as AGRENDDATE, ");
        sql.append(" acc.RATES as RATES, ");
        sql.append(" acc.RATES_FLOAT as RATESFLOAT, ");
        sql.append(" acc.RATES_FLOAT_PER as RATESFLOATPER, ");
        sql.append(" acc.VOLUME_NUM as VOLUMENUM, ");
        sql.append(" acc.BANKBOOK_NUM as BANKBOOKNUM, ");
        sql.append(" acc.INTEREST_CYCLE as INTERESTCYCLE, ");
        sql.append(" acc.INTEREST_DATE as INTERESTDATE, ");
        sql.append(" acc.REG_DATE as REGDATE, ");
        sql.append(" acc.PRIVATE_NO as PRIVATENO, ");
        sql.append(" acc.IS_ONLINE as ISONLINE, ");
        sql.append(" acc.SCAN_FLAG as SCANFLAG, ");
        sql.append(" acc.INIT_BALANCE_DATE as INITBALANCEDATE, ");
        sql.append(" acc.INIT_BALANCE as INITBALANCE, ");
        sql.append(" acc.RMK as RMK, ");
        sql.append(" acc.VALID_SIGN as VALIDSIGN, ");
        sql.append(" acc.STATUS as STATUS, ");
        sql.append(" acc.ORG_CODE as ORGCODE, ");
        sql.append(" acc.ATTACHMENT_ID as ATTACHMENTID, ");
        sql.append(" acc.CREATE_DATE as CREATEDATE, ");
        sql.append(" acc.CREATE_BY as CREATEBY, ");
        sql.append(" acc.UPDATE_DATE as UPDATEDATE, ");
        sql.append(" acc.UPDATE_BY as UPDATEBY, ");
        sql.append(" acc.ENTITY_ACC_ID as ENTITYACCID, ");
        sql.append(" acc.IS_CAPITAL_POOL as ISCAPITALPOOL, ");
        sql.append(" acc.CIRCULATION_MODE as CIRCULATIONMODE, ");
        sql.append(" acc.END_DATE as ENDDATE, ");
        sql.append(" acc.BANK_ORG_CODE as BANKORGCODE, ");
        sql.append(" acc.CHECK_EXPIRE_DATE as CHECKEXPIREDATE, ");
        sql.append(" acc.SEAL_DIRECTORY as SEALDIRECTORY, ");
        sql.append(" acc.SEAL_PERSON as SEALPERSON, ");
        sql.append(" acc.SEAL_PHONE as SEALPHONE, ");
        sql.append(" acc.SEAL_EMAIL as SEALEMAIL, ");
        sql.append(" acc.MGT_DEPARTMENT as MGTDEPARTMENT, ");
        sql.append(" acc.IS_DOMESTIC_BANK as ISDOMESTICBANK, ");
        sql.append(" acc.U_KEY_ADMIN as UKEYADMIN, ");
        sql.append(" acc.U_KEY_OPERATOR as UKEYOPERATOR, ");
        sql.append(" acc.U_KEY_REVIEWERS1 as UKEYREVIEWERS1, ");
        sql.append(" acc.U_KEY_REVIEWERS2 as UKEYREVIEWERS2, ");
        sql.append(" acc.U_KEY_REVIEWERS3 as UKEYREVIEWERS3, ");
        sql.append(" acc.U_KEY_FUNCTION as UKEYFUNCTION, ");
        sql.append(" acc.IS_LIMIT_QUOTA as ISLIMITQUOTA, ");
        sql.append(" acc.DAY_LIMIT_QUOTA as DAYLIMITQUOTA, ");
        sql.append(" acc.MONTH_LIMIT_QUOTA as MONTHLIMITQUOTA, ");
        sql.append(" acc.PAY_LIMIT_QUOTA as PAYLIMITQUOTA, ");
        sql.append(" acc.OVER_LIMIT_QUOTA as OVERLIMITQUOTA, ");
        sql.append(" acc.U_KEY_FUNCTION_OTHER as UKEYFUNCTIONOTHER, ");
        sql.append(" acc.acc_use_code as accUseCode, ");
        sql.append(" sc.name as corpName,");
        sql.append(" bac.cur_id as curId, ");
        sql.append(" cur.cur_name as curName, ");
        sql.append(" cur.english_code as curEnglishCode, ");
        sql.append(" bktype.type_name as bankTypeName, ");
        sql.append(" bktype.bank_prefix as bankPrefix, ");
        sql.append(" bktype.bank_name_prefix as bankNamePrefix,acc.binding_bank_acc as bindingBankAcc, ");
        sql.append(" att.attribute_name as attributeName,att.foreign_des as foreignDes,nat.nature_name as natureName,");
        sql.append(" bac.counter_acc as counterAccId ,bac.prent_acc_id as prentAccId,bac.super_counter_acc as superCounterAcc ");
        sql.append(" from BT_BANK_ACC acc ");
        sql.append(" left join BT_BANK_ACC_CUR bac on acc.id=bac.bank_acc_id ");
        sql.append(" left join bt_currency cur on cur.id=bac.cur_id ");
        sql.append(" left join BT_BANK_TYPE bktype on bktype.id=acc.bank_type_id ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join sys_corp sc on acc.corp_id = sc.id ");
        sql.append(" where acc.status=:status and acc.valid_sign=:validSign ");
        //根据ID集合获取账号
        if (!EmptyUtils.isEmpty(dto.getIdList())) {
            sql.append(" and ").append(SqlUtil.getInSql(dto.getIdList(), "acc.Id", true));
        }
        Map<String, Object> params = BeanUtils.beanToMap(dto);
        for (String key : params.keySet()) {
            if (params.get(key) != null && params.get(key) instanceof String) {
                if ("bankCodeStr".equals(key)) {
                    // 跳过此字段，此字段用于还款账号排序
                    continue;
                }
                String valueInfo = params.get(key).toString();
                if ("prentAccId".equals(key)) {
                    sql.append(" and bac.").append(StringUtils.getTableFieldToEntityField(key)).append("=:").append(key);
                } else if ("curId".equals(key)) {
                    sql.append(" and bac.cur_id=:").append(key);
                } else if ("corpId".equals(key) && valueInfo.indexOf(",") > -1) {
                    // 存在多个单位时  zuoy 2017-09-15
                    String[] corpIds = valueInfo.split(",");
                    sql.append(" and ").append(SqlUtil.getInSql(corpIds, "acc.corp_id", true));
                } else {
                    sql.append(" and acc.").append(StringUtils.getTableFieldToEntityField(key)).append("=:").append(key);
                }
            }
        }
        // 如果借款银行行号不为空，则需要优先显示相同开户行名称的账号
        if (StringUtils.isNotEmpty(dto.getBankCodeStr())) {
            // 使用新加排序字段ind排序即可
            sql.append(" ) temp ORDER BY ind desc ");
        }
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        //关联余额默认查近三个月的余额
        Date balDate = DateUtils.addMonth(DateUtils.getDateYMD(), -3);
        params.put("balDate", balDate);
        List<BtBankAccDTO> accList = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccDTO.class);
        return accList;
    }
    /**
     * 根据银行账号各个条件查询银行账号列表(不关联余额)
     *
     * @param dto
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({"unchecked", "rawtypes", "deprecation"})
    public List<BtBankAccDTO> getBankAccNotUnionByIdList(BtBankAccDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" acc.ID as ID, ");
        sql.append(" acc.BANK_ACC as BANKACC, ");
        sql.append(" acc.ACC_NAME as ACCNAME, ");
        sql.append(" acc.PARENT_SIGN as PARENTSIGN, ");
        sql.append(" acc.CORP_ID as CORPID, ");
        sql.append(" acc.ACC_TYPE as ACCTYPE, ");
        sql.append(" acc.ATTRIBUTE_ID as ATTRIBUTEID, ");
        sql.append(" acc.NATURE_ID as NATUREID, ");
        sql.append(" acc.BANK_TYPE_ID as BANKTYPEID, ");
        sql.append(" acc.BANK_CODE as BANKCODE, ");
        sql.append(" acc.BANK_NAME as BANKNAME, ");
        sql.append(" acc.PROV as PROV, ");
        sql.append(" acc.CITY as CITY, ");
        sql.append(" acc.BIF_CODE as BIFCODE, ");
        sql.append(" acc.ELECTRIC_BILL as ELECTRICBILL, ");
        sql.append(" acc.REGULARS as REGULARS, ");
        sql.append(" acc.REGULAR_DATE as REGULARDATE, ");
        sql.append(" acc.NOTICE_TYPE as NOTICETYPE, ");
        sql.append(" acc.AGR_MONEY as AGRMONEY, ");
        sql.append(" acc.AGR_INTEREST as AGRINTEREST, ");
        sql.append(" acc.AGR_CONTRACT as AGRCONTRACT, ");
        sql.append(" acc.AGR_BEGIN_DATE as AGRBEGINDATE, ");
        sql.append(" acc.AGR_END_DATE as AGRENDDATE, ");
        sql.append(" acc.RATES as RATES, ");
        sql.append(" acc.RATES_FLOAT as RATESFLOAT, ");
        sql.append(" acc.RATES_FLOAT_PER as RATESFLOATPER, ");
        sql.append(" acc.VOLUME_NUM as VOLUMENUM, ");
        sql.append(" acc.BANKBOOK_NUM as BANKBOOKNUM, ");
        sql.append(" acc.INTEREST_CYCLE as INTERESTCYCLE, ");
        sql.append(" acc.INTEREST_DATE as INTERESTDATE, ");
        sql.append(" acc.REG_DATE as REGDATE, ");
        sql.append(" acc.PRIVATE_NO as PRIVATENO, ");
        sql.append(" acc.IS_ONLINE as ISONLINE, ");
        sql.append(" acc.SCAN_FLAG as SCANFLAG, ");
        sql.append(" acc.INIT_BALANCE_DATE as INITBALANCEDATE, ");
        sql.append(" acc.INIT_BALANCE as INITBALANCE, ");
        sql.append(" acc.RMK as RMK, ");
        sql.append(" acc.VALID_SIGN as VALIDSIGN, ");
        sql.append(" acc.STATUS as STATUS, ");
        sql.append(" acc.ORG_CODE as ORGCODE, ");
        sql.append(" acc.ATTACHMENT_ID as ATTACHMENTID, ");
        sql.append(" acc.CREATE_DATE as CREATEDATE, ");
        sql.append(" acc.CREATE_BY as CREATEBY, ");
        sql.append(" acc.UPDATE_DATE as UPDATEDATE, ");
        sql.append(" acc.UPDATE_BY as UPDATEBY, ");
        sql.append(" acc.ENTITY_ACC_ID as ENTITYACCID, ");
        sql.append(" acc.IS_CAPITAL_POOL as ISCAPITALPOOL, ");
        sql.append(" acc.CIRCULATION_MODE as CIRCULATIONMODE, ");
        sql.append(" acc.END_DATE as ENDDATE, ");
        sql.append(" acc.BANK_ORG_CODE as BANKORGCODE, ");
        sql.append(" acc.CHECK_EXPIRE_DATE as CHECKEXPIREDATE, ");
        sql.append(" acc.SEAL_DIRECTORY as SEALDIRECTORY, ");
        sql.append(" acc.SEAL_PERSON as SEALPERSON, ");
        sql.append(" acc.SEAL_PHONE as SEALPHONE, ");
        sql.append(" acc.SEAL_EMAIL as SEALEMAIL, ");
        sql.append(" acc.MGT_DEPARTMENT as MGTDEPARTMENT, ");
        sql.append(" acc.IS_DOMESTIC_BANK as ISDOMESTICBANK, ");
        sql.append(" acc.U_KEY_ADMIN as UKEYADMIN, ");
        sql.append(" acc.U_KEY_OPERATOR as UKEYOPERATOR, ");
        sql.append(" acc.U_KEY_REVIEWERS1 as UKEYREVIEWERS1, ");
        sql.append(" acc.U_KEY_REVIEWERS2 as UKEYREVIEWERS2, ");
        sql.append(" acc.U_KEY_REVIEWERS3 as UKEYREVIEWERS3, ");
        sql.append(" acc.U_KEY_FUNCTION as UKEYFUNCTION, ");
        sql.append(" acc.IS_LIMIT_QUOTA as ISLIMITQUOTA, ");
        sql.append(" acc.DAY_LIMIT_QUOTA as DAYLIMITQUOTA, ");
        sql.append(" acc.MONTH_LIMIT_QUOTA as MONTHLIMITQUOTA, ");
        sql.append(" acc.PAY_LIMIT_QUOTA as PAYLIMITQUOTA, ");
        sql.append(" acc.OVER_LIMIT_QUOTA as OVERLIMITQUOTA, ");
        sql.append(" acc.U_KEY_FUNCTION_OTHER as UKEYFUNCTIONOTHER, ");
        sql.append(" bac.cur_id as curId, ");
        sql.append(" bktype.type_name as bankTypeName, ");
        sql.append(" bktype.bank_prefix as bankPrefix, ");
        sql.append(" bktype.bank_name_prefix as bankNamePrefix, ");
        sql.append(" att.attribute_name as attributeName,att.foreign_des as foreignDes,nat.nature_name as natureName,");
        sql.append(" bac.counter_acc as counterAccId ,bac.prent_acc_id as prentAccId,bac.super_counter_acc as superCounterAcc ");
        sql.append(" from BT_BANK_ACC acc ");
        sql.append(" left join BT_BANK_ACC_CUR bac on acc.id=bac.bank_acc_id ");
        sql.append(" left join BT_BANK_TYPE bktype on bktype.id=acc.bank_type_id ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" where acc.status=:status and acc.valid_sign=:validSign ");
        //根据ID集合获取账号
        if (!EmptyUtils.isEmpty(dto.getIdList())) {
            sql.append(" and ").append(SqlUtil.getInSql(dto.getIdList(), "acc.Id", true));
        }
        Map<String, Object> params = BeanUtils.beanToMap(dto);
        for (String key : params.keySet()) {
            if (params.get(key) != null && params.get(key) instanceof String) {
                String valueInfo = params.get(key).toString();
                if ("prentAccId".equals(key)) {
                    sql.append(" and bac.").append(StringUtils.getTableFieldToEntityField(key)).append("=:").append(key);
                } else if ("curId".equals(key)) {
                    sql.append(" and bac.cur_id=:").append(key);
                } else if ("corpId".equals(key) && valueInfo.indexOf(",") > -1) {
                    // 存在多个单位时  zuoy 2017-09-15
                    String[] corpIds = valueInfo.split(",");
                    sql.append(" and ").append(SqlUtil.getInSql(corpIds, "acc.corp_id", true));
                } else {
                    sql.append(" and acc.").append(StringUtils.getTableFieldToEntityField(key)).append("=:").append(key);
                }
            }
        }
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        List<BtBankAccDTO> accList = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccDTO.class);
        return accList;
    }
    /**
     * 根据银行账号各个条件查询银行账号列表
     *
     * @param dto
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({"unchecked", "rawtypes", "deprecation"})
    public List<BtBankAcc> getBankAccList(BtBankAccDTO dto) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from BtBankAcc acc left join fetch acc.curList as cur where acc.validSign = " + BtBankAccConstant.VALIDSIGN_1 + " and acc.status = " + BtBankAccConstant.STATUE_CHECK + " ");
        /**
         * 获取实体类的所有属性，返回Field数组
         */
        Field[] field = dto.getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            /**
             *  获取属性的名字
             */
            String name = field[i].getName();
            /**
             *  将属性的首字符大写，方便构造get，set方法
             */
            String upName = name;
            if (!upName.contains("uKey")) {
                upName = name.substring(0, 1).toUpperCase() + name.substring(1);
            }

            /**
             *  获取属性的类型
             */
            String type = field[i].getGenericType().toString();
            /**
             *  如果type是类类型，则前面包含"class "，后面跟类名
             */
            try {
                if ("class java.lang.String".equals(type)) {
                    Method m = dto.getClass().getMethod("get" + upName);
                    // 调用getter方法获取属性值
                    String value = (String) m.invoke(dto);
                    if (!EmptyUtils.isBlank(value)) {
                        if ("prentAccId".equals(name)) {
                            hql.append(" and cur.prentAccId = '" + value + "'");
                        } else if ("curId".equals(name)) {
                            hql.append(" and cur.curId = '" + value + "'");
                        } else if ("corpId".equals(name) && value.indexOf(",") > -1) {
                            // 存在多个单位时  zuoy 2017-09-15  update by liusy  改为sqlin 方式
                            String[] corpIds = value.split(",");
                            hql.append(" and (" + getSQLIn(Arrays.asList(corpIds), 900, "acc.corpId") + ") ");
                        } else {
                            hql.append(" and acc." + name + " = '" + value + "'");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(BtBankAccConstant.BANKACCLIST_EXCEPTION);
            }
        }
        Map params = new HashMap();
        List<BtBankAcc> accList = this.createQuery(hql.toString()).setProperties(params).list();
        return accList;
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion1(DataTablesInput dataTablesInput) {
        String sql = " select distinct acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId, " + " acc.acc_type as accType,att.attribute_name as attributeName, " + " nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, " + " acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,bif.name as bifName " + " from bt_bank_acc acc " + " left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id " + " left join bt_acc_nature nat on acc.nature_id = nat.id " + " left join bt_bank_type banktype on acc.bank_type_id = banktype.id " + " left join bis_bif_init bif on acc.bif_code = bif.id " + " left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id " + " where acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.is_online = " + BtBankAccConstant.ISONLINE_1 + " and acc.valid_sign = " + BtBankAccConstant.VALIDSIGN_1 + " ";
        return getDynamicBySQL(dataTablesInput, sql, BtBankAccVO.class, "acc", false);
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BtBankAccVO> getDataTablesUnion2(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,acccur.cur_id as curId, ");
        sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, ");
        sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
        sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,bif.name as bifName ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join bis_acc_attribute att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" where acc.status = " + com.bytter.core.constant.BtBankAccConstant.STATUE_CHECK + " and acc.valid_sign = " + com.bytter.core.constant.BtBankAccConstant.VALIDSIGN_1 + " ");
        if (!EmptyUtils.isEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "acc", false);
    }
    /**
     * 根据条件查询分页列表 离线账号，在线账号都可以显示
     *
     * @param dataTablesInput 表格封装信息
     * @return 表格封装信息
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnionAll(DataTablesInput dataTablesInput) {
        String sql = " select distinct acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId, " + " acc.acc_type as accType,att.attribute_name as attributeName, " + " nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, " + " acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,bif.name as bifName,acccur.cur_id curId" + " from bt_bank_acc acc " + " left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id " + " left join bt_acc_nature nat on acc.nature_id = nat.id " + " left join bt_bank_type banktype on acc.bank_type_id = banktype.id " + " left join bis_bif_init bif on acc.bif_code = bif.id " + " left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id " + " where acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.valid_sign = " + BtBankAccConstant.VALIDSIGN_1 + " ";
        return getDynamicBySQL(dataTablesInput, sql, BtBankAccVO.class, "acc", false);
    }
    /**
     * 根据idlist 得到银行账户列表
     *
     * @param idList
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BtBankAcc> getBankAccByIdList(List<String> idList) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("idList", idList);
        List<BtBankAcc> accList = this.createQuery(" from BtBankAcc acc left join fetch acc.curList as cur where cur.bankAccId in (:idList) and acc.status = " + BtBankAccConstant.STATUE_CHECK).setProperties(params).list();
        return accList;
    }
    /**
     * 根据idlist 得到银行账户列表
     *
     * @param idList
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BtBankAccVO> getBankAccVOByIdList(List<String> idList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId, ");
        sql.append(" acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,bif.name as bifName, ");
        sql.append(" acc.electric_bill as electricBill,acccur.cur_id as curId,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  ");
        sql.append(" acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName,acc.bank_org_code as bankOrgCode,acc.is_domestic_bank as isDomesticBank,");
        sql.append(" acccur.counter_acc as counterAccId,att.foreign_des as foreignDes,bt_nvl_num((fc.check_Balance-fc.freeze_Money),0) as fcBalance ");
//        sql.append(" (select bt_nvl_num(avail_Bal,0) from bis_acc_bal bal where bal.bank_acc=acc.bank_acc and bal.cur_id=acccur.cur_id and bal.bal_Date=(select max(bal_Date) from bis_acc_bal bal1 where bal.bank_acc=bal1.bank_acc and bal.cur_id=bal1.cur_id)) as balance");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id ");
        sql.append(" left join FC_ONLINE_LEDGER fc on fc.account=acccur.counter_acc");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        if (!EmptyUtils.isEmpty(idList)) {
            sql.append(" and ").append(SqlUtil.getInSql(idList, "acc.Id", true));
        }
        return this.listBySQLAliasToBean(sql.toString(), null, BtBankAccVO.class);
    }
    /**
     * 根据内部账号和币别，获取银行账号集合
     *
     * @param countAcc
     * @param curId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BtBankAcc> getBankAccByCountAcc(String countAcc, String curId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("counterAcc", countAcc);
        params.put("curId", curId);
        return this.createQuery(" from BtBankAcc acc left join fetch acc.curList as cur where cur.counterAcc = :counterAcc and cur.curId = :curId ").setProperties(params).list();
    }
    /**
     * 根据联行号，获取银行账号集合
     *
     * @param bankCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BtBankAcc> getBankAccByBankCode(String bankCode) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("bankCode", bankCode);
        return this.createQuery(" from BtBankAcc acc where bankCode = :bankCode ").setProperties(params).list();
    }
    /**
     * 根据银行账号id和币别，获取账号信息
     *
     * @param bankAccId
     * @param curId
     * @return
     */
    public BtBankAcc getBankAccByIdAndCurId(String bankAccId, String curId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("bankAccId", bankAccId);
        params.put("curId", curId);
        String sql = " SELECT acc.* FROM BT_BANK_ACC acc LEFT JOIN BT_BANK_ACC_CUR cur ON acc.id = cur.BANK_ACC_ID  where acc.id = :bankAccId and cur.cur_id = :curId ";
        //ORACLE不支持    return (BtBankAcc) this.createQuery(" from BtBankAcc acc left join fetch acc.curList as cur where acc.id = :bankAccId and cur.curId = :curId ").setProperties(params).uniqueResult();
        return getBySQLAliasToBean(sql.toString(), params, BtBankAcc.class);
    }
    /**
     * 根据条件查询账户信息
     * zuoy 2017-07-14
     *
     * @param accountSearchDTO
     * @return
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<BtBankAccVO> getBankAccByAccountDTO(AccountSearchDTO accountSearchDTO) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId, ");
        sql.append(" acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,bbt.type_name as bankTypeName,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,bif.name as bifName, ");
        sql.append(" acc.electric_bill as electricBill,acccur.cur_id as curId,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  ");
        sql.append(" acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName");
        sql.append(" ,acccur.counter_acc as counterAccId,att.foreign_des as foreignDes ");
        //sql.append(" ,bal2.avail_Bal as balance ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id ");
        sql.append(" left join bt_bank_type bbt on bbt.id = acc.bank_type_id ");
        //sql.append(" left join FC_ONLINE_LEDGER fc on fc.account=acccur.counter_acc ");
//        sql.append(" LEFT JOIN (select  bal.BANK_ACC,bal.CUR_ID,bt_nvl_num(avail_Bal,0) as avail_Bal");
//        sql.append(" from bis_acc_bal bal inner join ");
//        sql.append(" (select bank_acc,cur_id,max(bal_Date) from bis_acc_bal group by bank_acc, cur_id) bal1 ");
//        sql.append(" on bal.bank_acc=bal1.bank_acc and bal.cur_id=bal1.cur_id ) bal2 on bal2.BANK_ACC = acc.BANK_ACC and bal2.CUR_ID = acccur.CUR_ID");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "'");
        //查询结果是否要包含已注销的账号
        if (!EmptyUtils.isEmpty(accountSearchDTO.getContainDelBankAcc()) && accountSearchDTO.getContainDelBankAcc()) {
            sql.append(" and acc.status in (" + BtBankAccConstant.STATUE_CHECK + "," + BtBankAccConstant.STATUE_DEL + ")");
        } else {
            sql.append(" and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        }
        if (StringUtils.isNotBlank(accountSearchDTO.getAccountType())) {
            if (accountSearchDTO.getAccountType().split(CommonConstant.COMMA_SPLIT).length == 1 && accountSearchDTO.getAccountType().equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                // 仅查询在线账户
                sql.append(" and acc.is_online = 1 ");
            } else if (accountSearchDTO.getAccountType().split(CommonConstant.COMMA_SPLIT).length == 1 && accountSearchDTO.getAccountType().equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                // 仅查询离线账户
                sql.append(" and acc.is_online = 0 ");
            }
        }
        if (!EmptyUtils.isEmpty(accountSearchDTO.getBankTypeId()) && "-8500".equals(accountSearchDTO.getBankTypeId())){
            // 排除银行类型8500的
            sql.append(" and bbt.bank_type <> '8500' ");
            accountSearchDTO.setBankTypeId("");
        }

        // 排除银行类别
        if (ObjectUtil.isNotEmpty(accountSearchDTO.getExcludeBankTypeCode())){
            sql.append(" and bbt.bank_type <> :bankTypeCode ");
            params.put("bankTypeCode", accountSearchDTO.getExcludeBankTypeCode());
            accountSearchDTO.setExcludeBankTypeCode("");
        }
        // 排除账号类别
        if (ObjectUtil.isNotEmpty(accountSearchDTO.getExcludeAccTypeCode())){
            sql.append(" and acc.acc_type <> :accTypeCode ");
            params.put("accTypeCode", accountSearchDTO.getExcludeAccTypeCode());
            accountSearchDTO.setExcludeAccTypeCode("");
        }

        if (ObjectUtil.isNotEmpty(accountSearchDTO.getIsOnline())){
            if (CommonConstant.YES.equals(accountSearchDTO.getIsOnline())){
                sql.append(" and acc.is_online = 1 ");
            }
            if (CommonConstant.NO.equals(accountSearchDTO.getIsOnline())){
                sql.append(" and acc.is_online = 0 ");
            }
        }
        if(ObjectUtil.isNotEmpty(accountSearchDTO.getAccType())){
            sql.append(" and acc.acc_type = '").append(accountSearchDTO.getAccType()).append("'");
        }
        /**
         * 获取实体类的所有属性，返回Field数组
         */
        Field[] fields = accountSearchDTO.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 过滤不需要查询的属性值
            Annotation annotation = field.getAnnotation(IgnoreField.class);
            if (annotation != null) {
                continue;
            }
            String name = field.getName();
            String upName = name.substring(0, 1).toUpperCase() + name.substring(1);
            Class type = field.getType();
            try {
                if (type == String.class) {
                    Method m = accountSearchDTO.getClass().getMethod("get" + upName);
                    // 调用getter方法获取属性值
                    String value = (String) m.invoke(accountSearchDTO);
                    if (!EmptyUtils.isBlank(value)) {
                        if ("curId".equalsIgnoreCase(name)) {
                            sql.append(" and acccur.cur_Id =:curId");
                            params.put("curId",value);
                        } else if ("corpId".equalsIgnoreCase(name)) {
                            sql.append(" and ").append(SqlUtil.getInSql(value.split(CommonConstant.COMMA_SPLIT), "acc.corp_Id", true));
                        } else if ("electricBill".equalsIgnoreCase(name)) {
                            sql.append(" and acc.electric_bill =:electricBill");
                            params.put("electricBill",value);
                        }else if ("bankTypeId".equalsIgnoreCase(name)) {
                            sql.append(" and acc.bank_type_id in (:bankTypeId) ");
                            params.put("bankTypeId",value.split(CommonConstant.COMMA_SPLIT));
                        } else {
                            sql.append(" and acc.").append(StringUtils.camelToUnderline(name)).append(" = '" + value + "'");
                        }
                    }
                } else if (type == List.class) {
                    if ("accountGrantList".equalsIgnoreCase(name)) {
                        if (!EmptyUtils.isEmptyList(accountSearchDTO.getAccountGrantList())) {
                            sql.append(" and ").append(SqlUtil.getInSql(accountSearchDTO.getAccountGrantList(), "acccur.bank_Acc_Id", true));
                        }
                    } else if ("corpGrantList".equalsIgnoreCase(name)) {
                        if (!EmptyUtils.isEmptyList(accountSearchDTO.getCorpGrantList())) {
                            sql.append(" and ").append(SqlUtil.getInSql(accountSearchDTO.getCorpGrantList(), "acc.corp_Id", true));
                        }
                    } else {
                        Method m = accountSearchDTO.getClass().getMethod("get" + upName + "ToString");
                        // 调用getter方法获取属性值
                        String value = (String) m.invoke(accountSearchDTO);
                        if (!EmptyUtils.isEmpty(value)) {
                            if ("accountGrantList".equalsIgnoreCase(name)) {
                                sql.append(" and acccur.bank_Acc_Id in (" + value + ")");
                            }
                        }
                    }
                } else if (type == Boolean.class) {
                    Method m = accountSearchDTO.getClass().getMethod("get" + upName);
                    // 调用getter方法获取属性值
                    Boolean value = (Boolean) m.invoke(accountSearchDTO);
                    if (!EmptyUtils.isEmpty(value)) {
                        // 仅查询存在子帐号信息的账户数据
                        if ("existChilds".equalsIgnoreCase(name) && value) {
                            sql.append(" and acc.parent_Sign = 1 ");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.toString());
                ExceptionStackUtils.getStack(e);
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        logger.info("查询账户信息sql：{}", sql.toString());
        logger.info("查询账户信息params：{}", gson.toJson(params));
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }
    /**
     * 获取实体账号
     *
     * @param btBankAccApp
     * @return
     */
    public List<BtBankAccVO> getEntityAcc(BtBankAccApp btBankAccApp) {
        String hql = " from BtBankAcc acc  where acc.validSign = " + BtBankAccConstant.VALIDSIGN_1 + " and acc.status = " + BtBankAccConstant.STATUE_CHECK + " " + " and acc.corpId=:corpId and acc.bankCode = :bankCode and acc.accType = '" + BtBankAccConstant.ACCTYPE_01 + "'";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", btBankAccApp.getCorpId());
        params.put("bankCode", btBankAccApp.getBankCode());
        List<BtBankAcc> accList = super.listByHQL(hql, params);
        List<BtBankAccVO> list = BeanUtils.copyListProperties(accList, BtBankAccVO.class);
        for(BtBankAccVO vo :list){
            vo.setCurList(null);
        }
        return list;
    }

    /**
     * 获取绑定账号
     *
     * @param btBankAccApp
     * @return
     */
    public List<BtBankAccVO> getBindingBankAcc(BtBankAccApp btBankAccApp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId, ");
        sql.append(" acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,bif.name as bifName, ");
        sql.append(" acc.electric_bill as electricBill,acccur.cur_id as curId,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  ");
        sql.append(" acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName,acc.bank_org_code as bankOrgCode,acc.is_domestic_bank as isDomesticBank,");
        sql.append(" acccur.counter_acc as counterAccId,att.foreign_des as foreignDes,cur.cur_name as curName ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id ");
        sql.append(" left join bt_currency cur on acccur.cur_id = cur.id ");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        sql.append(" and acc.corp_id=:corpId and acc.acc_type = '" + BtBankAccConstant.ACCTYPE_01 + "'");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", btBankAccApp.getCorpId());
        List<BtBankAccVO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
        return list;
    }
    /**
     * 根据账户或户名查询账户简略信息
     *
     * @param bankAcc
     * @param accName
     * @return
     * @author zuoy 2018-03-19
     */
    public List<BtBankAcc> listBtBankAccBriefInfoBy(String bankAcc, String accName) {
        Map<String, Object> param = new HashMap<>();
        StringBuilder hql = new StringBuilder();
        hql.append(" from BtBankAcc acc  where acc.validSign = :validSign and acc.status <> :status ");
        param.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        param.put("status", BtBankAccConstant.STATUE_DEL);
        if (!EmptyUtils.isBlank(bankAcc)) {
            hql.append(" and acc.bankAcc = :bankAcc ");
            param.put("bankAcc", bankAcc);
        }
        if (!EmptyUtils.isBlank(accName)) {
            hql.append(" and acc.accName = :accName ");
            param.put("accName", accName);
        }
        return this.listByHQL(hql.toString(), param);
    }
    /**
     * 修改银行流水截至时间
     *
     * @param bankAccId
     * @param curId
     * @param endDtlDate
     * @author zuoy 2018-04-04
     */
    public void updateEndDtlDate(String bankAccId, String curId, Date endDtlDate) {
        Map<String, Object> param = new HashMap<>();
        param.put("bankAccId", bankAccId);
        param.put("curId", curId);
        param.put("endDtlDate", endDtlDate);
        this.batchExecute(" update BtBankAccCur set endDtlDate = :endDtlDate where bankAccId = :bankAccId and curId = :curId", param);
    }
    /**
     * 获取最近一次银行流水截至时间
     *
     * @param bankAccId
     * @param curId
     * @author zuoy 2018-04-04
     */
    public Date getLastEndDtlDate(String bankAccId, String curId) {
        Map<String, Object> param = new HashMap<>();
        param.put("bankAccId", bankAccId);
        param.put("curId", curId);
        List<BtBankAccCur> list = this.listByHQL(" from BtBankAccCur where bankAccId = :bankAccId and curId = :curId order by endDtlDate desc ", param);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0).getEndDtlDate();
    }
    /**
     * 根据银行账号和币别，获取账号信息
     *
     * @param bankAcc
     * @param curId
     * @return
     * @author zuoy 2018-05-29
     */
    public BtBankAcc getBankAccByBankAccAndCurId(String bankAcc, String curId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        return (BtBankAcc) this.createQuery(" from BtBankAcc acc left join fetch acc.curList as cur where acc.bankAcc = :bankAcc and cur.curId = :curId ").setProperties(params).uniqueResult();
    }
    /**
     * 功能介绍 ： 根据父账号和子账号对应的内部账号取具有支出属性的子账号。<br>
     * <b>日期：</b>2018-05-19<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<BtBankAccDTO> getOutPayByParentAcc(String prentAccId, String curId, String counterAcc) {
        String hql = " from BtBankAcc acc  where acc.validSign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.isOnline = '" + BtBankAccConstant.ISONLINE_1 + "' and exists " + " (select 1 from BtBankAccCur cur where cur.bankAccId = acc.id and cur.curId = :curId " + " and cur.counterAcc = :counterAcc and cur.prentAccId = :prentAccId) and exists ( select 1 from BisAccAttribute ba  " + " where ba.id = acc.attributeId and ba.attributeDes > '133') order by acc.bankAcc ";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("prentAccId", prentAccId);
        params.put("curId", curId);
        params.put("counterAcc", counterAcc);
        List<BtBankAcc> accList = listByHQL(hql, params);
        List<BtBankAccDTO> list = BeanUtils.copyListProperties(accList, BtBankAccDTO.class);
        if (!list.isEmpty()) {
            for (BtBankAccDTO bankAccDTO : list) {
                bankAccDTO.setCurId(curId);
            }
        }
        //sssinject(list);
        return list;
    }
    /**
     * 获取下属账户单位集合
     *
     * @param prentAccId 父类账户ID
     * @return
     * @author zuoy 2018-06-12
     */
    public List<String> listChildCorpIdsByParent(String prentAccId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String sql = "select distinct acc.corp_Id from bt_bank_acc acc left join  bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id " + " where acc.valid_sign = :validSign and acc.status <> :status and acccur.prent_acc_id = :prentAccId ";
        params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        params.put("status", BtBankAccConstant.STATUE_DEL);
        params.put("prentAccId", prentAccId);
        return this.getSession().createSQLQuery(sql).setProperties(params).list();
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTableOldBankAcc(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,corp.name as corpName, ");
        sql.append(" su.username as createBy,acc.REG_DATE as regDate,acc.VALID_SIGN as validSign, ");
        sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, acc.IS_ONLINE as isOnline,acc.RATES as rates, ");
        sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
        sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" left join sys_user su on su.login_name = acc.create_by ");
        sql.append(" left join sys_corp corp on acc.CORP_ID  = corp.ID  ");
        sql.append(" where 1=1 and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
        sql.append(" and acc.valid_sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false);
    }
    /**
     * 获取即将到期和已到期的账户信息
     *
     * @return
     * @author zuoy 2018-11-07
     */
    public List<BtBankAccVO> listBankAccByEndDate(String searchDate) {
        String sql = " select acc.id, acc.bank_acc, acc.acc_name, acc.corp_id,acc.create_by, acc.end_date from bt_bank_acc acc " + " where acc.valid_sign = '" + BtBankAccConstant.VALIDSIGN_1 + "'" + " and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.end_date is not null" + " and acc.end_date <=:searchDate";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("searchDate", DateUtils.parse(searchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return listBySQLAliasToBean(sql, params, BtBankAccVO.class);
    }

    /**
     * 获取到期的账户信息
     *
     * @return
     * @author zuoy 2018-11-07
     */
    public List<BtBankAccDTO> listBankAccByDate(String searchDate) {
        String sql = " select acc.id, acc.bank_acc, acc.acc_name, acc.corp_id,acc.create_by, acc.end_date from bt_bank_acc acc " + " where acc.valid_sign = '" + BtBankAccConstant.VALIDSIGN_1 + "'" + " and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.end_date is not null" + " and acc.end_date =:searchDate";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("searchDate", DateUtils.parse(searchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return listBySQLAliasToBean(sql, params, BtBankAccDTO.class);
    }
    /**
     * <br>
     * <b>功能：</b>查询单位/网点可用余额（企e金服接口）<br>
     * <b>作者：</b>黄茜<br>
     * <b>日期：</b>2018-11-25<br>
     *
     * @param corpflag
     * @param corpCode
     * @return
     */
    public BigDecimal queryBankAccAvailableBal(String corpflag, String corpCode) {
        StringBuilder sql = new StringBuilder("select bt_nvl(sum(bt_nvl((select distinct bal.avail_bal from bis_acc_bal bal,(select bank_acc, max(bal_date) maxdate from bis_acc_bal group by bank_acc) abc");
        sql.append(" where bal.bank_acc = abc.bank_acc and bal.bal_date = abc.maxdate and (bal.status is null or bal.status >= 95) and bal.bank_acc = bt_bank_acc.bank_acc), 0)), 0) AS balance");
        sql.append(" from bt_bank_acc left join sys_corp on bt_bank_acc.corp_id = sys_corp.id where bt_bank_acc.valid_sign = '1' and bt_bank_acc.is_online='1'");
        //1-网点；2-网点+单位
        if ("1".equals(corpflag)) {
            sql.append(" and sys_corp.code = :corpCode");
        } else {
            sql.append(" and sys_corp.net_id = (select id from sys_corp where code = :corpCode)");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corpCode", corpCode);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal balance = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(result)) {
            balance = (BigDecimal) result;
        }
        return balance;
    }
    /**
     * 根据bankAcc查询账户简略信息，无币别，不考虑是否有效
     *
     * @param bankAcc
     * @return
     * @author zuoy 2019-02-26
     */
    public BtBankAcc getByBankAcc(String bankAcc) {
        Map<String, Object> params = new HashMap<>();
        params.put("bankAcc", bankAcc);
        return (BtBankAcc) this.createQuery(" from BtBankAcc acc  where acc.bankAcc = :bankAcc ").setProperties(params).uniqueResult();
    }
    /**
     * @param @param  accountSearchDTO
     * @param @return 参数
     * @return List<BtBankAcc>    返回类型
     * @throwsgetBankAccDTOById
     * @Title: getBankRasAccByAccountDTO
     * @Description: TODO(增加稽查银行账户信息查询Dao方法)
     */
    public List<String[]> getBankRasAccByAccountDTO(AccountSearchDTO accountSearchDTO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select");
        sql.append(" bta.id as bankAccId, bta.bank_acc as bankAcc,bta.acc_name  as AccName,");
        sql.append(" bta.bank_name as bankAccName,bta.entity_acc_id as entityAccId,ebac.bank_acc as entityAcc");
        sql.append(" from BT_BANK_ACC bta");
        sql.append(" left join BT_BANK_ACC_CUR bac");
        sql.append(" on bac.bank_acc_id=bta.id");
        sql.append(" left join BT_BANK_ACC ebac");
        sql.append(" on ebac.id = bta.entity_acc_id");
        sql.append(" where 1=1");
        sql.append(" and bta.valid_sign=1");
        sql.append(" and bta.status=95");
        if (!StringUtils.isBlank(accountSearchDTO.getCorpId())) {
            sql.append(" and bta.corp_id=:btaCorpId");
            params.put("btaCorpId", accountSearchDTO.getCorpId());
        }
        if (!StringUtils.isBlank(accountSearchDTO.getCurId())) {
            sql.append(" and bac.cur_id=:btaCurId");
            params.put("btaCurId", accountSearchDTO.getCurId());
        }
        if (!StringUtils.isBlank(accountSearchDTO.getIsVirtualAcc()) && accountSearchDTO.getIsVirtualAcc().equals("1")) {
            sql.append(" and bta.entity_acc_id is not null");
        } else {
            sql.append(" and bta.entity_acc_id is null");
        }
        @SuppressWarnings("unchecked") List<String[]> accList = createSQLQuery(sql.toString(), params).list();
        return accList;
    }
    /**
     * 查询正常状态在线和离线账号列表
     *
     * @param isOnline 1-在线账号，0-离线账号
     * @return 账号集合
     */
    @SuppressWarnings("unchecked")
    public List<BtBankAccDTO> getBankAccListByIsOnline(String isOnline) {
        String sql = "select acc.id as id, acc.bank_acc as bankAcc, acc.acc_name as accName, acc.corp_id as corpId," + " acc.bank_type_id as bankTypeId, acc.bank_code as bankCode, acc.bank_name as bankname," + " acc.bif_code as bifCode, acc.reg_date as regDate, acc.init_balance_date as initBalanceDate," + " acc.init_balance as initBalance, acc.status as status, acc.end_date as endDate, cur.cur_id as curId" + " from bt_bank_acc acc left join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id" + " where acc.status = :status and acc.is_online = :isOnline order by acc.bank_acc";
        Map<String, Object> map = new HashMap<>();
        map.put("status", BtBankAccConstant.STATUE_CHECK);
        map.put("isOnline", isOnline);
        return listBySQLAliasToBean(sql, map, BtBankAccDTO.class);
    }
    /**
     * 查询正常状态离线账号列表（带分页自动生成余额专用）
     *
     * @param parameterInput
     * @return
     */
    public IPageResult<BtBankAccDTO> getBankAccListByIsOnlineResult(IParameterInput parameterInput) {
        String sql = "select acc.id as id, acc.bank_acc as bankAcc, acc.acc_name as accName, acc.corp_id as corpId," + " acc.bank_type_id as bankTypeId, acc.bank_code as bankCode, acc.bank_name as bankname," + " acc.bif_code as bifCode, acc.reg_date as regDate, acc.init_balance_date as initBalanceDate," + " acc.init_balance as initBalance, acc.status as status, acc.end_date as endDate, cur.cur_id as curId" + " from bt_bank_acc acc left join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id" + " where acc.status = :status and acc.is_online = :isOnline order by acc.bank_acc";
        Map<String, Object> map = new HashMap<>();
        map.put("status", BtBankAccConstant.STATUE_CHECK);
        map.put("isOnline", BtBankAccConstant.ISONLINE_0);
        return getPageResultBySQL(parameterInput, sql, map, BtBankAccDTO.class);
    }
    /**
     * 根据ID获取账号
     *
     * @param ids
     * @return
     */
    public List<BtBankAccVO> listByIds(String[] ids, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id, acc.bank_acc, acc.acc_name,  acc.corp_id, acc.bank_name, acc.bank_code, acc.prov, acc.city ");
        sql.append(" ,acc.bif_code,bif.name bifName,acc.acc_type,acc.bank_type_id ");
        if (EmptyUtils.isBlank(curId)) {
            sql.append(" from bt_bank_acc acc ");
        } else {
            sql.append(" ,cur.cur_id curId, cur.counter_acc as counterAccId, cur.prent_acc_id as prentAccId ");
            sql.append(" from bt_bank_acc acc left join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id ");
        }
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" where 1=1 ");
        sql.append(" and acc.id in (:ids)");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        if (!EmptyUtils.isBlank(curId)) {
            sql.append(" and cur.cur_id =:curId ");
            params.put("curId", curId);
        }
        return listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }
    /**
     * 根据单位统计银行账户
     *
     * @param corpId
     * @param busType del:单位删除 disable:禁用 change:上级变更
     * @return
     */
    public int getBankAccCountByCorpId(String corpId, String busType) {
        String sql = "select count(*) from bt_bank_acc where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        if ("disable".equals(busType)) {
            //95-审批通过
            sql += " and status='" + BtBankAccConstant.STATUE_CHECK + "'";
        }
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    /**
     * 根据用户获取银行账号
     *
     * @param isDomesticBank 是否境内银行 1-境内，0-境外
     * @param joinSql        用户权限
     * @return
     */
    public List<BtBankAccVO> getBankAccVOByuserId(String isDomesticBank, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId, ");
        sql.append(" acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,bif.name as bifName, ");
        sql.append(" acc.electric_bill as electricBill,acccur.cur_id as curId,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  ");
        sql.append(" acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName,acc.bank_org_code as bankOrgCode,acc.IS_DOMESTIC_BANK as isDomesticBank");
        sql.append(" ,acccur.counter_acc as counterAccId,att.foreign_des as foreignDes,corp.name as corpName,cur.cur_name as curName ");
//        sql.append(" (select bt_nvl_num(avail_Bal,0) from bis_acc_bal bal where bal.bank_acc=acc.bank_acc and bal.cur_id=acccur.cur_id and bal.bal_Date=(select max(bal_Date) from bis_acc_bal bal1 where bal.bank_acc=bal1.bank_acc and bal.cur_id=bal1.cur_id)) as balance");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id ");
        sql.append(" left join sys_corp corp on acc.corp_id = corp.id ");
        sql.append(" left join bt_currency cur on cur.id = acccur.cur_id ");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!EmptyUtils.isEmpty(isDomesticBank)) {
            sql.append(" and acc.IS_DOMESTIC_BANK = :isDomesticBank ");
            params.put("isDomesticBank", isDomesticBank);
        }
        sql.append(joinSql);
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }
    /**
     * 根据用户权限获取有效的银行账号
     *
     * @param corpId  单位id
     * @param curId   币别
     * @param accType 账户类别 可多选用,隔开
     * @return
     */
    public List<BtBankAccVO> getBankAccVOByuserIdCurId(String corpId, String curId, String accType, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId, ");
        sql.append(" acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,bif.name as bifName, ");
        sql.append(" acc.electric_bill as electricBill,acccur.cur_id as curId,cur.cur_name as curName,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  ");
        sql.append(" acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName,acc.bank_org_code as bankOrgCode,acc.IS_DOMESTIC_BANK as isDomesticBank");
        sql.append(" ,acccur.counter_acc as counterAccId,att.foreign_des as foreignDes ");
//        sql.append(" ,acccur.counter_acc as counterAccId,att.foreign_des as foreignDes,bt_nvl_num((fc.check_Balance-fc.freeze_Money),0) as fcBalance, ");
//        sql.append(" (select bt_nvl_num(avail_Bal,0) from bis_acc_bal bal where bal.bank_acc=acc.bank_acc and bal.cur_id=acccur.cur_id and bal.bal_Date=(select max(bal_Date) from bis_acc_bal bal1 where bal.bank_acc=bal1.bank_acc and bal.cur_id=bal1.cur_id)) as balance");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_currency cur on cur.id = acccur.cur_id ");
        sql.append(" left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id ");
//        sql.append(" left join FC_ONLINE_LEDGER fc on fc.account=acccur.counter_acc");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!EmptyUtils.isEmpty(corpId)) {
            sql.append(" and acc.corp_id = :corpId ");
            params.put("corpId", corpId);
        }
        if (!EmptyUtils.isEmpty(curId)) {
            sql.append(" and acccur.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(accType)) {
            String[] accTypes = accType.split(",");
            sql.append(" and  " + SqlUtil.getInSql(accTypes, "acc.acc_type", true));
        }
        sql.append(joinSql);
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }
    /**
     * 获取所有父账号集合
     *
     * @return
     */
    public List<BtBankAccVO> getAllParentBankAcc() {
        String sql = " select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId, " + " acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, " + " acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city," +
                " acc.bif_code as bifCode,bif.name as bifName, " + " acc.electric_bill as electricBill,acccur.cur_id as curId,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  " + " acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName,acc.bank_org_code as bankOrgCode,acc.IS_DOMESTIC_BANK as isDomesticBank" + " ,acccur.counter_acc as counterAccId,att.foreign_des as foreignDes" +
                //sql.append(" (select bt_nvl_num(avail_Bal,0) from bis_acc_bal bal where bal.bank_acc=acc.bank_acc and bal.cur_id=acccur.cur_id and bal.bal_Date=(select max(bal_Date) from bis_acc_bal bal1 where bal.bank_acc=bal1.bank_acc and bal.cur_id=bal1.cur_id)) as balance");
                " from bt_bank_acc acc " + " left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id " + " left join bt_acc_nature nat on acc.nature_id = nat.id " + " left join bis_bif_init bif on acc.bif_code = bif.id " + " left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id " + " left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id " +
//                " left join FC_ONLINE_LEDGER fc on fc.account=acccur.counter_acc" +
                " where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.PARENT_SIGN ='" + BtBankAccConstant.PARENTSIGN_1 + "'";
        HashMap<String, Object> params = new HashMap<String, Object>();
        return this.listBySQLAliasToBean(sql, params, BtBankAccVO.class);
    }
    /**
     * 根据单位集合 得到银行账户列表
     *
     * @param corpIdList
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BtBankAccVO> getBankAccVOByCorpIdList(List<String> corpIdList, String joinSql, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId, ");
        sql.append(" acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,bif.name as bifName, ");
        sql.append(" acc.electric_bill as electricBill,acccur.cur_id as curId,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  ");
        sql.append(" acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName,acc.bank_org_code as bankOrgCode,acc.is_domestic_bank as isDomesticBank,");
        sql.append(" acccur.counter_acc as counterAccId,att.foreign_des as foreignDes,corp.name as corpName,cur.cur_name as curName,type.type_name as bankTypeName ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id ");
        sql.append(" left join sys_corp corp on acc.corp_id = corp.id ");
        sql.append(" left join bt_currency cur on cur.id = acccur.cur_id ");
        sql.append(" left join bt_bank_type type on type.id = acc.bank_type_id ");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        if (!EmptyUtils.isEmpty(corpIdList)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpIdList, "acc.corp_id", true));
        }
        sql.append(joinSql);
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }
    /**
     * 根据单位集合 得到银行账户列表(不关联账户余额)
     *
     * @param corpIdList
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BtBankAccVO> getBankAccByCorpIdList(List<String> corpIdList, String joinSql, Map<String,Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId, corp.name as corpName,");
        sql.append(" acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,bif.name as bifName, ");
        sql.append(" acc.electric_bill as electricBill,acccur.cur_id as curId,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  ");
        sql.append(" acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName,acc.bank_org_code as bankOrgCode,acc.is_domestic_bank as isDomesticBank,");
        sql.append(" bbt.BANK_NAME_PREFIX AS bankNamePrefix, ");
        sql.append(" acccur.counter_acc as counterAccId,att.foreign_des as foreignDes,acc.status,acc.rates as rates ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id ");
        sql.append(" left join sys_corp corp on corp.id = acc.corp_id ");
        sql.append(" left join bt_bank_type bbt on acc.bank_type_id = bbt.id ");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        if (!EmptyUtils.isEmpty(corpIdList)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpIdList, "acc.corp_id", true));
        }if(!StringUtils.isEmpty(joinSql)){
            sql.append(joinSql);
        }
        if (params.containsKey("accType")){
            sql.append(" and acc.acc_type = :accType");
        }
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }

    public List<BtBankAccVO> getBankAccByBankAccIds(List<String> bankAccIds) {
        String sql = " select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName," +
                "acc.parent_sign as parentSign,acc.corp_id as corpId, " +
                " acc.acc_type as accType,acc.attribute_id as attributeId,acc.nature_id as natureId, "
                + " acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName," +
                "acc.prov as prov,acc.city as city,acc.bif_code as bifCode,acc.electric_bill as electricBill " +
                " from bt_bank_acc acc " + " where acc.id in (:bankAccIds) " + " and acc.valid_Sign = '" +
                BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK;
        Map<String, Object> params = new HashMap<>();
        params.put("bankAccIds", bankAccIds);
        List mapList = listBySQL(sql, params);
        if (EmptyUtils.isEmpty(mapList)) {
            return null;
        }
        return BeanUtils.copyMapListProperties(mapList, BtBankAccVO.class);
    }
    /**
     * 根据账号集合查询账户信息，包括省市信息
     *
     * @param bankAccs 账号集合
     * @return 账户信息
     */
    public List<BtBankAccDTO> getBankAccByInfoAccs(List<String> bankAccs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId,corp.name AS corpName, ");
        sql.append(" acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,bif.name as bifName, ");
        sql.append(" acc.electric_bill as electricBill,acccur.cur_id as curId,cur.cur_name AS curName,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  ");
        sql.append(" acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName,acc.bank_org_code as bankOrgCode,acc.is_domestic_bank as isDomesticBank,");
        sql.append(" acccur.counter_acc as counterAccId,att.foreign_des as foreignDes,acc.status,");
        sql.append(" p.PROV_INPUT_NAME as provName,c.NAME as cityName ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id ");
        sql.append(" left join sys_corp corp on acc.corp_id = corp.id ");
        sql.append(" left join bt_currency cur on cur.id = acccur.cur_id ");
        sql.append(" LEFT JOIN sys_region p ON acc.PROV = p.ID ");
        sql.append(" LEFT JOIN sys_region c ON acc.CITY = c.ID ");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        String bankCodeSql = SqlUtil.getInSql(bankAccs, "acc.bank_acc", true);
        sql.append(" and ").append(bankCodeSql).append(" ");
        return this.listBySQLAliasToBean(sql.toString(), null, BtBankAccDTO.class);
    }
    /**
     * 根据账号集合查询账户信息，包括省市信息
     *
     * @param bankAccs 账号集合
     * @return 账户信息
     */
    public List<BtBankAccDTO> getBankAccByInfoAccs(List<String> bankAccs,DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId,corp.name AS corpName, ");
        sql.append(" acc.acc_type as accType,acc.attribute_id as attributeId,att.attribute_name as attributeName,acc.nature_id as natureId,nat.nature_name as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,bif.name as bifName, ");
        sql.append(" acc.electric_bill as electricBill,acccur.cur_id as curId,cur.cur_name AS curName,acccur.counter_Acc as counterAcc,acccur.super_counter_acc as superCounterAcc,acc.circulation_mode as circulationMode,  ");
        sql.append(" acccur.prent_Acc_Id as prentAccId,pacc.bank_acc as prentBankAcc,pacc.acc_name as prentAccName,acc.bank_org_code as bankOrgCode,acc.is_domestic_bank as isDomesticBank,");
        sql.append(" acccur.counter_acc as counterAccId,att.foreign_des as foreignDes,acc.status");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init bif on acc.bif_code = bif.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_bank_acc pacc on pacc.id = acccur.prent_Acc_Id ");
        sql.append(" left join sys_corp corp on acc.corp_id = corp.id ");
        sql.append(" left join bt_currency cur on cur.id = acccur.cur_id ");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        String bankCodeSql = SqlUtil.getInSql(bankAccs, "acc.bank_acc", true);
        sql.append(" and ").append(bankCodeSql).append(" ");
        if (ObjectUtil.isEmpty(input)){
            return this.listBySQLAliasToBean(sql.toString(), null, BtBankAccDTO.class);
        }
        //账户类别
        String ins_accType= (String) input.getFormdata().get("ins_accType");
        List<String> accTypeList = Lists.newArrayList();
        if(StringUtils.isNotBlank(ins_accType)){
            accTypeList = Arrays.stream(ins_accType.split(CommonConstant.COMMA_SPLIT)).collect(Collectors.toList());
        }
        //银行类型
        String ins_bankTypeId= (String) input.getFormdata().get("ins_bankTypeId");
        List<String> bankTypeIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(ins_bankTypeId)){
            bankTypeIdList = Arrays.stream(ins_bankTypeId.split(CommonConstant.COMMA_SPLIT)).collect(Collectors.toList());
        }
        //账户性质
        String ins_natrueId= (String) input.getFormdata().get("ins_natureId");
        List<String> natureIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(ins_natrueId)){
            natureIdList = Arrays.stream(ins_natrueId.split(CommonConstant.COMMA_SPLIT)).collect(Collectors.toList());
        }
        //账户属性
        String ins_attributeId= (String) input.getFormdata().get("ins_attributeId");
        List<String> attributeIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(ins_attributeId)){
            attributeIdList = Arrays.stream(ins_attributeId.split(CommonConstant.COMMA_SPLIT)).collect(Collectors.toList());
        }
        String eqi_status= (String) input.getFormdata().get("eqi_status");
        String eqs_curId= (String) input.getFormdata().get("eqs_curId");
        String eqs_isOnline= (String) input.getFormdata().get("eqs_isOnline");
        String returnCode= (String) input.getFormdata().get("returnCode");
        if( !CollectionUtils.isEmpty(bankTypeIdList) ){
            sql.append(" and ").append(SqlUtil.getInSql(bankTypeIdList, "acc.bank_type_id", true));
        }
        if( !CollectionUtils.isEmpty(natureIdList) ){
            sql.append(" and ").append(SqlUtil.getInSql(natureIdList, "acc.nature_id", true));
        }
        if( !CollectionUtils.isEmpty(attributeIdList) ){
            sql.append(" and ").append(SqlUtil.getInSql(attributeIdList, "acc.attribute_id", true));
        }
        if( !CollectionUtils.isEmpty(accTypeList)  ){
            sql.append(" and ").append(SqlUtil.getInSql(accTypeList, "acc.acc_type", true));
        }
        if( StringUtils.isNotBlank(eqi_status) ){
            sql.append(" and acc.status='"+eqi_status+"'");
        }
        if( StringUtils.isNotBlank(eqi_status) ){
            sql.append(" and acc.status='"+eqi_status+"'");
        }
        if( StringUtils.isNotBlank(eqs_curId) ){
            sql.append(" and acccur.cur_id='"+eqs_curId+"'");
        }
        if(StringUtils.isNotBlank(eqs_isOnline) ){
            sql.append(" and acc.is_online='"+eqs_isOnline+"'");
        }
        return this.listBySQLAliasToBean(sql.toString(), null, BtBankAccDTO.class);
    }
    /**
     * 根据账号id集合查询账户信息，包括省市信息
     *
     * @param ids 账号id集合
     * @return 账户信息
     */
    public List<BtBankAccDTO> getBankAccUnByIds(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName, ");
        sql.append(" acc.parent_sign as parentSign,acc.corp_id as corpId, acc.acc_type as accType,acc.attribute_id as attributeId,");
        sql.append(" baa.ATTRIBUTE_NAME as attributeName,acc.nature_id as natureId,ban.NATURE_NAME as natureName, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov, ");
        sql.append(" acc.city as city,acc.bif_code as bifCode,acc.electric_bill as electricBill,sc.`NAME` corpName ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join bt_acc_nature ban on acc.NATURE_ID = ban.ID  ");
        sql.append(" left join bis_acc_attribute baa on acc.ATTRIBUTE_ID = baa.ID ");
        sql.append(" LEFT JOIN sys_corp sc ON acc.CORP_ID = sc.ID ");
        sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        String bankCodeSql = SqlUtil.getInSql(ids, "acc.id", true);
        sql.append(" and ").append(bankCodeSql).append(" ");
        return this.listBySQLAliasToBean(sql.toString(), null, BtBankAccDTO.class);
    }
    /**
     * 根据id查询账户信息
     *
     * @param id
     * @return 账户信息
     */
    public List<BtBankAccDTO> getBankAccDTOById(String id) {
        String sql = " select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId, " + " acc.acc_type as accType,acc.attribute_id as attributeId,acc.nature_id as natureId, " + " acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,acc.electric_bill as electricBill " + " from bt_bank_acc acc " + " where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.id='" + id + "'";
        return this.listBySQLAliasToBean(sql, null, BtBankAccDTO.class);
    }

    /**
     * 根据银行帐号获取帐户信息。
     *
     * @param bankAccList 帐号集合。
     * @return 账户信息。
     */
    public List<BtBankAccDTO> getBankAccInfoByBankAccList(List<String> bankAccList) {
        String sql = "select corp_id as corpId,bank_acc as bankAcc,acc_name as accName,bank_code as bankCode,bank_name as bankName " +
                " from bt_bank_acc where valid_sign = '1' ";
        if (ObjectUtil.isNotEmpty(bankAccList)) {
            String accSql = SqlUtil.getInSql(bankAccList, "bank_acc", true);
            sql += " and " + accSql;
        }
        return this.listBySQLAliasToBean(sql, null, BtBankAccDTO.class);
    }

    public String getBifCodeByBankAcc(String bankAcc) {
        String sql = "select bi.BIF_CODE as bifCode from bt_bank_acc ba left join bis_bif_init bi on ba.bif_code=bi.id " +
                " where BANK_ACC = ? and ba.valid_sign = '1' ";
        List<String> list = this.createSQLQuery(sql, bankAcc).addScalar("bifCode", StandardBasicTypes.STRING).list();
        return ObjectUtil.isNotEmpty(list) ? list.get(0) : "";
    }

    /**
     * 根据账户ID集合获取账号信息
     *
     * @param idList
     * @return
     */
    public List<String> getBankAccByIds(List<String> idList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.bank_acc as bankAcc from bt_bank_acc acc where 1=1");
        String accIdSql = SqlUtil.getInSql(idList, "acc.id", true);
        sql.append(" and ").append(accIdSql);
        return this.createSQLQuery(sql.toString()).list();
    }
    public List<String> getBankAccByAccId(List<String> accIds) {
        List<BtBankAccDTO> accList = listBySQLAliasToBean(" SELECT bankAcc.id as id FROM bt_bank_acc bankAcc where " + getSQLIn(accIds, 900, "bankAcc.id") + " UNION SELECT fcAcc.id as id FROM fc_acc fcAcc where " + getSQLIn(accIds, 900, "fcAcc.id"), null, BtBankAccDTO.class);
        if (CollectionUtils.isEmpty(accIds)) {
            return null;
        }
        return accList.stream().map(BtBankAccDTO::getId).collect(Collectors.toList());
    }
    /**
     * 根据银行账号查询线上票据专户。
     *
     * @param bankAccList 银行账号列表。
     * @return 线上票据专户。
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBankModelByBankAcc(List<String> bankAccList) {
        String sql = "select id,electric_bill as electricBill,circulation_mode as circulationMode,bank_acc as bankAcc " + " from bt_bank_acc where bank_acc in ( :bankAccList) and electric_bill='1' and circulation_mode='1'";
        Map<String, Object> params = new HashMap<>();
        params.put("bankAccList", bankAccList);
        return listBySQL(sql, params);
    }

    /**
     * 根据银行账号查询票据专户。
     *
     * @param bankAccList 银行账号列表。
     * @return 票据专户。
     */
    @SuppressWarnings("unchecked")
    public Map<String, BtBankAccDTO> getBillBankAccByBankAccList(List<String> bankAccList) {
        String sql = "select acc.id,electric_bill as electricBill,acc.circulation_mode as circulationMode,acc.bank_acc as bankAcc, type.type_name accTypeName, acc.bank_name bankName "
                + " from bt_bank_acc acc join bt_bank_type type on type.id = acc.BANK_TYPE_ID where  acc.electric_bill='1' ";
        Map<String, Object> params = new HashMap<>();
        if(EmptyUtils.isNotEmpty(bankAccList) && bankAccList.size() >0) {
            sql += " and acc.bank_acc in ( :bankAccList) ";
            params.put("bankAccList", bankAccList);
        }
        List<BtBankAccDTO> btBankAccDTOS = listBySQLAliasToBean(sql, params, BtBankAccDTO.class);
        if(CollectionUtil.isEmpty(btBankAccDTOS)){
            return MapUtil.empty();
        }
        Map<String, BtBankAccDTO> collect = btBankAccDTOS.stream().collect(Collectors.toMap(BtBankAccDTO::getBankAcc, Function.identity()));
        return collect;
    }

    public List<Map<String, Object>> queryVAccListVo(String bankAcc) {
        String sql = "SELECT C.ID AS CORPID, C.ACCOUNT_LICENCE AS ACCOUNTLICENCE, B.BANK_CODE AS BANKCODE,B.ACC_NAME ACCNAME From sys_corp c,bt_bank_acc b where c.id=b.corp_id and b.bank_acc='" + bankAcc + "'";
        List<Map<String, Object>> objStr = this.listBySQL(sql, null);
        return objStr;
    }
    public List<VAccListVo> queryVAccListVoNew(String bifCode) {
        String sql = "select a.bank_acc as bankAcc," + "a.acc_name as accName,"
                // +
                // "(select b.bank_acc from bt_bank_acc b  where b.parent_sign='1' and a.prent_acc_id=b.id) prentAcc,"
                + "a.corp_id as corpId," + "d.cur_code as curCode," + "c.cur_id as curId," + "c.counter_acc as counterAcc," + "c.super_counter_acc as superCounterAcc," + "a.parent_sign as parentSign," + "a.acc_type as accType," + "a.bank_code as bankCode," + "a.bank_name as bankName," + "e.name as prov," + "f.name as city," + "a.private_no as privateNo," + "a.org_code as orgCode," + "b.bif_code as bifCode," + "a.is_online as isOnline," + "a.valid_sign as validSign " + "from bt_bank_acc a,bis_bif_init b,bt_bank_acc_cur c,bt_currency d," + "sys_region e ,sys_region f " + "where a.bif_code=b.id " + "and a.id=c.bank_acc_id " + "and c.cur_id=d.id " + "and a.prov=e.id " + "and a.city=f.id " + "and a.electric_bill='1' " + "and a.CIRCULATION_MODE='1' " + "and a.valid_sign='1' and d.cur_name='人民币' and b.bif_code='" + bifCode + "' ";
        return this.listBySQLAliasToBean(sql, null, VAccListVo.class);
    }
    public List<VAccListVo> queryVAccListVoNewByCorp(String bifCode, List<String> poolCorpId) {
        StringBuffer sql = new StringBuffer("select bba.bank_acc as bankAcc,");
        sql.append(" bba.acc_name as accName, ");
        sql.append(" bba.corp_id as corpId, ");
        sql.append(" bba.parent_sign as parentSign, ");
        sql.append(" bba.acc_type as accType, ");
        sql.append(" bba.bank_code as bankCode, ");
        sql.append(" bba.bank_name as bankName, ");
        sql.append(" bba.private_no as privateNo, ");
        sql.append(" bba.org_code as orgCode, ");
        sql.append(" bba.is_online as isOnline, ");
        sql.append(" bba.valid_sign as validSign  ");
        sql.append(" bba.BANK_ORG_CODE as bankOrgCode  ");
        sql.append(" bbi.BIF_CODE as bifCode  ");
        sql.append(" from bt_bank_acc bba  ");
        sql.append("left join BIS_BIF_INIT bbi on pi.bif_code=bbi.bif_code ");
        sql.append(" where bbi.bif_code=:bifCode ");
        sql.append(" and bba.electric_bill = '1' and bba.circulation_mode = '1' ");
        Map<String, Object> param = new HashMap<>();
        param.put("bifCode", bifCode);
        if (poolCorpId != null && poolCorpId.size() != 0) {
            sql.append(" and bba.corp_id in (:poolCorpId) ");
            param.put("poolCorpId", poolCorpId);
        }
        return this.listBySQLAliasToBean(sql.toString(), param, VAccListVo.class);
    }
    public boolean getReqAndRecCorpIdById(String id) {
        boolean iscorp = false;
        String sql = "SELECT CASE (select b.corp_id from BT_BANK_ACC b WHERE b.bank_acc=a.req_acct_id) WHEN (select b.corp_id from BT_BANK_ACC b WHERE b.bank_acc=a.rec_acct_id) THEN '1' ELSE '0' END as iscorp FROM AB_ACCRECORDS a WHERE a.id='" + id + "'";
        List<Map<String, Object>> objStr = this.listBySQL(sql, null);
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = (Map) object;
                if ("1".equals(ary.get("ISCORP").toString())) {
                    iscorp = true;
                }
            }
        }
        return iscorp;
    }
    /**
     * <b>功能：</b>根据账号查询是银行账号信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     *
     * @param acctId 账号
     */
    public BillBankBankAccVO getBankAccByAcctId(String acctId) {
        List<BillBankBankAccVO> bankAccList = new ArrayList<BillBankBankAccVO>();
        String sql = "select t.BANK_ACC,t.BANK_NAME,t.ACC_NAME,t.CORP_ID,t.BANK_CODE,i.id as BIF_ID,i.BIF_CODE " + " from BT_BANK_ACC t inner join bis_bif_init i on t.bif_code=i.id" + " WHERE t.bank_acc='" + acctId + "'";
        List<Map<String, Object>> objStr = this.listBySQL(sql, null);
        bankAccList = this.getBeanByBankAcc(objStr);
        return bankAccList.size() > 0 ? bankAccList.get(0) : null;
    }
    /**
     * <b>功能：</b>封装银行账号信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     */
    private List<BillBankBankAccVO> getBeanByBankAcc(List<Map<String, Object>> objStr) {
        List<BillBankBankAccVO> list = new ArrayList<BillBankBankAccVO>();
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = (Map) object;
                BillBankBankAccVO bankAcc = new BillBankBankAccVO();
                bankAcc.setBankAcc(ary.get("BANK_ACC") == null ? "" : ary.get("BANK_ACC").toString().trim());
                bankAcc.setBankName(ary.get("BANK_NAME") == null ? "" : ary.get("BANK_NAME").toString().trim());
                bankAcc.setAcc_name(ary.get("ACC_NAME") == null ? "" : ary.get("ACC_NAME").toString().trim());
                bankAcc.setCorp_id(ary.get("CORP_ID") == null ? "" : ary.get("CORP_ID").toString().trim());
                bankAcc.setBankCode(ary.get("BANK_CODE") == null ? "" : ary.get("BANK_CODE").toString().trim());
                bankAcc.setBifCode(ary.get("BIF_CODE") == null ? "" : ary.get("BIF_CODE").toString().trim());
                bankAcc.setBifId(ary.get("BIF_ID") == null ? "" : ary.get("BIF_ID").toString().trim());
                list.add(bankAcc);
            }
        }
        return list;
    }
    /**
     * 获取银行账号关联的内部账号
     */
    public List<BtBankAccCurDTO> getCountertAcc(String bankAcc) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sb = " select b.counter_acc as counterAcc , b.super_counter_acc as superCounterAcc " + "  from bt_bank_acc a inner join bt_bank_acc_cur b on a.id = b.bank_acc_id  where a.bank_acc =:bankAcc ";
        params.put("bankAcc", bankAcc);
        return listBySQLAliasToBean(sb, params, BtBankAccCurDTO.class);
    }
    public List<BtBankAccDTO> listByIds(List<String> bankAccIds, List<String> oppAccNoList) {
        StringBuilder sql = new StringBuilder(" select a.corp_id as corpId from bt_bank_acc a where a.parent_sign = '1' ");
        if (!CollectionUtils.isEmpty(bankAccIds)) {
            sql.append(" and ").append(getSQLIn(bankAccIds, 900, "a.id"));
        }
        if (!CollectionUtils.isEmpty(oppAccNoList)) {
            sql.append(" and ").append(getSQLIn(oppAccNoList, 900, "a.bank_acc"));
        }
        return listBySQLAliasToBean(sql.toString(), null, BtBankAccDTO.class);
    }
    /**
     * 根据客商账号获取账号银行性质类型
     *
     * @param accList 客商账号
     * @return
     */
    public List<String> getBankClassByExtAcc(List<String> accList) {
        String sql = " select bt.bank_class from sys_external_corp_bankacc e " + " inner join BT_INPUT_BANK_INFO ibi on ibi.sys_bank_code = e.bank_sourcecode " + " left JOIN BT_BANK_TYPE bt ON ibi.BANK_PREFIX = bt.BANK_PREFIX" + " and (bt.bank_name_Prefix is null or ibi.bank_name like bt_concat3('%', bt.bank_name_Prefix, '%')) " + " where e.external_acc in (:acc) ";
        Map<String, Object> params = new HashMap<>();
        params.put("acc", accList);
        SQLQuery sqlQuery = createSQLQuery(sql, params);
        return sqlQuery.list();
    }
    /**
     * 根据单位账号获取账号银行性质类型
     *
     * @param accList 单位账号
     * @return
     */
    public List<String> getBankClassByBankAcc(List<String> accList) {
        String sql = " select bt.bank_class from bt_bank_acc acc " + " left join bt_bank_type bt on acc.bank_type_id = bt.id " + " where acc.bank_acc in (:acc) ";
        Map<String, Object> params = new HashMap<>();
        params.put("acc", accList);
        SQLQuery sqlQuery = createSQLQuery(sql, params);
        return sqlQuery.list();
    }
    public List<BtBankAccDTO> listByFcBankAccDtl(List<String> corpAuth) {
        String sql = " SELECT b.bank_acc as bankAcc,e.bank_acc as oppAccNo,f.CUR_ID as curId,f.cur_name as curName,c.pool_name AS corpName FROM " +
                " bt_bank_acc b,fc_corp_pool c,fc_corp_pool d,bt_bank_acc e,bt_bank_acc_cur f,bt_currency cur " +
                " WHERE b.corp_id = c.corp_id AND c.pool_sign = '1' AND e.corp_id = d.corp_id AND e.id = " +
                " f.bank_acc_id AND f.prent_acc_id = b.id AND c.id = d.pool_id and f.cur_id = cur.id " +
                " and " + getSQLIn(corpAuth, 900, "b.corp_id");
        return listBySQLAliasToBean(sql, null, BtBankAccDTO.class);
    }
    /**
     * 根据银行接口代码得到银行账号，用与银企直联
     * BT_BANK_ACC table列表
     *
     * @return
     */
    public DataTablesOutput<BtBankAccVO> getDataTableByBifCode(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,acccur.cur_id as curId,cur.cur_name as curName, ");
        sql.append(" acc.acc_type as accType,att.attribute_name as attributeName,acc.parent_sign as parentSign,acc.bif_Code as bifCode,t.name as bifName, ");
        sql.append(" nat.nature_name as natureName,acc.bank_name as bankName, c.name as corpName,");
        sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,acc.is_online as isOnline ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join bt_bank_acc_cur acccur on acccur.bank_acc_id = acc.id ");
        sql.append(" left join bt_currency cur on acccur.cur_id = cur.id ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init t on acc.bif_Code = t.id");
        sql.append(" left join sys_corp c on acc.corp_id = c.id");
        sql.append(" where 1=1 and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.is_online = " + BtBankAccConstant.ISONLINE_1);
        //sql.append(" and acc.bank_acc not in (select bank_acc from bt_bank_acc_app where status <> " + BtBankAccConstant.STATUE_CANCELLATION + " and bank_acc is not null ) ");
        String bifCode = (String) dataTablesInput.getFormdata().get("likes_bifCode");
        if (StringUtils.isNotEmpty(bifCode)) {
            dataTablesInput.getFormdata().put("likes_bifCode", bifCode);
        } else {
            dataTablesInput.getFormdata().put("likes_bifCode", "--");
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false);
    }
    /**
     * 账户明细截至日期列表
     * zuoy 2017-07-10
     *
     * @param corpId
     * @param bankAcc
     * @param bifCode
     * @return
     */
    public List<BtBankAccVO> listBankAccByEnd(String corpId, String bankAcc, String bifCode, String curId,String joinSql) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select acc.id,acc.corp_id,ac.name corpName,acc.bank_acc,acc.acc_name,acc.bank_name,acc.bif_code,bt_to_char(cur.end_dtl_date, 'yyyy-MM-dd HH24:mi:ss') as endDtlDate");
        sql.append(" ,(select bif.name as bifName from bis_bif_init bif where bif.id = acc.bif_code ) bifName, cur.cur_id,bc.cur_name AS curName from bt_bank_acc acc ");
        sql.append(" left join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id ");
        sql.append(" LEFT JOIN sys_corp ac ON acc.corp_id=ac.id ");
        sql.append(" LEFT JOIN bt_currency bc ON cur.cur_id=bc.id ");
        sql.append(" where acc.valid_Sign = :validSign and acc.status = :status ");
        sql.append(" and acc.is_online = :isOnline");
        params.put("validSign", com.bytter.core.constant.BtBankAccConstant.VALIDSIGN_1);
        params.put("status", com.bytter.core.constant.BtBankAccConstant.STATUE_CHECK);
        params.put("isOnline", com.bytter.core.constant.BtBankAccConstant.ISONLINE_0);
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if (!EmptyUtils.isBlank(bankAcc)) {
            sql.append(" and acc.bank_acc like :bankAcc ");
            params.put("bankAcc", "%" + bankAcc + "%");
        }
        if (!EmptyUtils.isBlank(corpId)) {
            List<String> corpIdList = Arrays.asList(corpId.split(","));
            sql.append(" and "+getSQLIn(corpIdList,900,"acc.corp_id"));
        }
        if (!EmptyUtils.isBlank(bifCode)) {
            sql.append(" and acc.bif_code = :bifCode ");
            params.put("bifCode", bifCode);
        }
        if (!EmptyUtils.isBlank(curId)) {
            sql.append(" and cur.cur_id = :curId ");
            params.put("curId", curId);
        }
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }
    public List<BtBankAccVO> listBankAccBySql(String sql,Map<String, Object> params ) {
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }

    public List<BtBankAccVO> listBankAccBySqlNew(String sql,Map<String, Object> params ) {
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccVO.class);
    }

    /**
     * <b>功能介绍：</b>获取银行账号<br>
     * <b>日期：</b>2017年9月26日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param bankAcc 账号
     * @param netCode 网点
     * @param natureCode 账户性质
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getBankAccByOpType(String bankAcc,String netCode,String natureCode){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select bbac.id,fa.blnctr_acc as account,bba.bank_acc as bankAcc,bba.bank_name as bankName,bba.acc_name as accName,bc.cur_name as curName,bc.id as curId,bba.id as bankAccId from bt_bank_acc_cur bbac ");
        sql.append("left join bt_bank_acc bba on bbac.bank_acc_id = bba.id ");
        sql.append("left join fc_acc fa on bbac.counter_acc = fa.blnctr_acc ");
        sql.append("left join bt_currency bc on bbac.cur_id = bc.id ");
        sql.append("left join fc_subject fs on fs.id = fa.subject_id ");
        sql.append("where bbac.counter_acc is not null ");
        //sql.append(" and fs.subject_code = '");
        //sql.append(SubjectConstant.SysSubject.INTEREST_BANK_CURRENT_DEPOSIT.get() + "' ");//银行活期存款
        if(EmptyUtils.isNotEmpty(bankAcc)){
            sql.append("and bba.bank_acc = :bankAcc ");
            params.put("bankAcc", bankAcc);
        }
        if(EmptyUtils.isNotEmpty(netCode)){
            sql.append("and fa.net_id = :netCode ");
            params.put("netCode", netCode);
        }
        if(EmptyUtils.isNotEmpty(natureCode)){
            sql.append("and bba.NATURE_ID = :natureCode  ");
            params.put("natureCode", natureCode);
        }
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),params);
        sqlQuery.addScalar("id", StandardBasicTypes.STRING);
        sqlQuery.addScalar("account", StandardBasicTypes.STRING);
        sqlQuery.addScalar("bankAcc", StandardBasicTypes.STRING);
        sqlQuery.addScalar("bankName", StandardBasicTypes.STRING);
        sqlQuery.addScalar("accName", StandardBasicTypes.STRING);
        sqlQuery.addScalar("curName", StandardBasicTypes.STRING);
        sqlQuery.addScalar("curId", StandardBasicTypes.STRING);
        sqlQuery.addScalar("bankAccId", StandardBasicTypes.STRING);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    /**
     * 查询ERP在离线账号
     * @param dataTablesInput
     * @param userId
     * @param bankList
     * @return
     */
    //TODO SYS服务提供
    public DataTablesOutput getBankAccsErpData(DataTablesInput dataTablesInput,String userId,List<String> bankList){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params=new HashMap<String, Object>();
        sql.append(" select distinct acc.bank_name bankName, acc.bank_acc bankAcc,accCur.cur_id as curId,cp.name as corpName,acc.corp_id as corpId,acc.acc_name as accName,  ");
        sql.append(" cur.cur_name as curName,acc.IS_ONLINE as isOnline,banktype.type_name as bankTypeName  ");
        sql.append(" from bt_bank_acc acc  ");
        sql.append(" left join bt_bank_acc_cur accCur on acc.id = accCur.bank_acc_id  ");
        sql.append(" left join bt_currency cur on accCur.cur_id=cur.id ");
        sql.append(" left join SYS_ACCOUNT_GRANT ACCOUNT_GRANT on ACCOUNT_GRANT.ACCOUNT_ID = acc.id");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" left join sys_corp cp on acc.corp_id=cp.id ");
        sql.append(" where 1=1  ");
        sql.append(" and acc.valid_sign =:validSign and acc.status=:status ");
        sql.append(" and ACCOUNT_GRANT.CAN_QUERY_DETAIL = '1' and ACCOUNT_GRANT.ACCOUNT_TYPE in('2','3') ");
        sql.append(" and ACCOUNT_GRANT.USER_ID = :userId");
        sql.append(" and "+SqlUtil.getInSql(bankList,"acc.id",true));
        params.put("validSign", com.bytter.system.api.dto.BtBankAccConstant.VALIDSIGN_1);
        params.put("status", com.bytter.system.api.dto.BtBankAccConstant.STATUE_CHECK);
        params.put("userId", userId);
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false,params);
    }

    public List<BtBankAccDTO> getByGroupCode(String accGroupCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params=new HashMap<String, Object>();
        sql.append(" select  * from  bt_bank_acc acc where acc.FC_ACC_GROUP_CODE like :accGroupCode");
        params.put("accGroupCode", accGroupCode);

        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccDTO.class);
    }

    /**
     * 根据银行账号id 注销银行账号
     * @param ids
     * @param closeDate
     * @return
     */
    public int updateBankAccByIds(List<String> ids,Date closeDate) {
        String sql = " update bt_bank_acc set status = :status,valid_sign =:validSign,cancel_reason =:cancelReason,close_date =:closeDate where id in (:ids)";
        Map<String, Object> params = new HashMap<>();
        params.put("status", BtBankAccConstant.ACCSTATUS_2);
        params.put("validSign", BtBankAccConstant.VALIDSIGN_0);
        params.put("cancelReason", BtBankAccConstant.CANCEL_REASON);
        params.put("closeDate", closeDate);
        params.put("ids", ids);
        return createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 批量设置稽查账户
     * @param ids
     * @param checkSign
     */
    public int setCheckBankAccSave(String ids, String checkSign){
        StringBuilder hql = new StringBuilder();
        hql.append("update BtBankAcc set isCheck=:checkSign where id in (:ids)");
        Map<String,Object> params = new HashMap<>();
        params.put("checkSign", checkSign);
        params.put("ids", ids.split(CommonConstant.COMMA_SPLIT));
        return this.batchExecute(hql.toString(), params);
    }

    public DataTablesOutput bindingBankAccDatatable(DataTablesInput dataTablesInput, String joinSql,String leftSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,acccur.cur_id as curId,cur.cur_name as curName, ");
        sql.append(" acc.acc_type as accType,att.attribute_name as attributeName,acc.parent_sign as parentSign,acc.bif_Code as bifCode,t.name as bifName, ");
        sql.append(" nat.nature_name as natureName,acc.bank_name as bankName, c.name as corpName,bankType.type_name as bankTypeName, ");
        sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,acc.is_online as isOnline,a.sign as sign ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join bt_bank_acc_cur acccur on acccur.bank_acc_id = acc.id ");
        sql.append(" left join bt_currency cur on acccur.cur_id = cur.id ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bis_bif_init t on acc.bif_Code = t.id");
        sql.append(" left join sys_corp c on acc.corp_id = c.id");
        sql.append(" left join bt_bank_type bankType on acc.bank_type_id = bankType.id");
        if (StringUtils.isNotEmpty(leftSql)) {
            sql.append(leftSql);
        }
        sql.append(" where 1=1 and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        sql.append(" and acc.acc_type in ('"+BtBankAccConstant.ACCTYPE_01+"', '"+BtBankAccConstant.ACCTYPE_10+"') ");
        sql.append(" and bankType.id != '").append(BtBankAccConstant.BANK_TYPE_THIRD_PARTY).append("' ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false);
    }

    /**
     * 获取过期的保证金账户
     * @param curDate
     * @return
     */
    public List<BtBankAccDTO> getExpireBailBankAcc(String curDate) {
        String sql = " select acc.id, acc.bank_acc, acc.acc_name, acc.corp_id,acc.create_by, acc.end_date from bt_bank_acc acc " + " where acc.acc_type='"+BtBankAccConstant.ACCTYPE_04+"'  and acc.valid_sign = '" + BtBankAccConstant.VALIDSIGN_1 + "'" + " and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.end_date is not null" + " and acc.end_date < :searchDate";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("searchDate", DateUtils.parse(curDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return listBySQLAliasToBean(sql, params, BtBankAccDTO.class);
    }

    public Integer queryBankAccList(String accUseCode, String id, String corpId) {
        Integer result = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(128);
        sql.append("select acc.id, acc.bank_acc, acc.acc_name ");
        sql.append("from bt_bank_acc acc ");
        sql.append("where acc.valid_sign = :validSign ");
        sql.append("and acc.status <> :status ");
        sql.append("and acc.status <> :status ");
        sql.append("and acc.acc_use_code LIKE BT_CONCAT3('%',:useCode,'%') ");
        if (ObjectUtil.isNotEmpty(id)){
            sql.append("and acc.id <> :id ");
            params.put("id",id);
        }
        if (StringUtils.isNotBlank(corpId)) {
            sql.append("and acc.corp_id = :corpId ");
            params.put("corpId",corpId);
        }
        params.put("useCode",accUseCode);
        params.put("status", BtBankAccConstant.ACCSTATUS_2);
        params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        List<BtBankAccDTO> list = listBySQLAliasToBean(sql.toString(), params, BtBankAccDTO.class);
        if (ObjectUtil.isNotEmpty(list)){
            return list.size();
        }
        return result;
    }

    /**
     * 根据单位获取有效的营收账号
     * @param corpIds
     * @return
     */
    public List<BtBankAccDTO> listBankAcc1004(List<String> corpIds) {
        String sql = " select acc.id as id,acc.bank_acc as bankAcc,cur.cur_id as curId,acc.corp_id as corpId  ";
        sql += " from bt_bank_acc acc left join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id ";
        sql += " where acc.acc_use_code LIKE '%1004%' and acc.valid_sign = '" + BtBankAccConstant.VALIDSIGN_1 + "'" + " and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.corp_id in (:corpId) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corpId",corpIds);
        return listBySQLAliasToBean(sql, params, BtBankAccDTO.class);
    }

    public void updateBankAccClose(String bankAcc, String isClose) {
        String sql = "update bt_bank_acc set IS_CLOSE = :isClose where BANK_ACC = :bankAcc ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isClose", isClose);
        params.put("bankAcc", bankAcc);
        this.createSQLQuery(sql, params).executeUpdate();
    }
}
