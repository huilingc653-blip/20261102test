package com.bytter.credit.domain.dao;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.GuaContractOccupVO;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.dto.GuaContractOccupDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.credit.dto.RatUrationBeanDTO;
import com.bytter.system.api.dto.BtBankDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 授信额度录入表
 * @date 2017-07-21 14:35:43
 */
@Slf4j
@Repository
public class RatBkrationDao extends BaseDao<RatBkration, String> {
    /**
     * 功能介绍 : 获取单位已录入的授信。<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationVm> getCorpBkrationListDTOs(RatBkRationVm bkrationDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rb from RatBkration rb where rb.status <> :status  ");
        hql.append(" and rb.corpId = :corpId and rb.bankCode = :bankCode and rb.curCode = :curCode ");
        hql.append(" and rb.ratType=:ratType");
        // 过滤当前校验数据
        if (!StringUtils.isEmpty(bkrationDTO.getId())) {
            hql.append(" and rb.id <> :ratCode ");
            params.put("ratCode", bkrationDTO.getId());
        }
        hql.append(" order by rb.createDate ");
        params.put("corpId", bkrationDTO.getCorpId());
        params.put("bankCode", bkrationDTO.getBankCode());
        params.put("curCode", bkrationDTO.getCurCode());
        params.put("status", CommonConstant.STATUE_DELETE);
        params.put("ratType", RationConstant.RAT_TYPE_0);
        List<RatBkration> list = listByHQL(hql.toString(), params);
        return copyListAndInject(list, RatBkRationVm.class);
    }
    /**
     * 功能介绍 : 查询授信录入数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @param
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput listOutputData(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rb.id as id,rb.rat_code as ratCode, rb.net_code as netCode,rb.corp_id as corpId, ");
        sql.append(" rb.rat_type as ratType,rb.corp_name as corpName,rb.contract_no as contractNo, rb.BANK_CODE as bankCode, ");
        sql.append(" rb.BANK_NAME as bankName,bankType.type_name as bankTypeName,rb.cur_code as curCode, ");
        sql.append(" rb.cur_name as curName,rb.rat_mode as ratMode,rb.begindate as beginDate,rb.enddate as endDate, ");
        sql.append(" rb.status as status,rb.money as money,rb.gua_contract  as guaContract,rb.user_name as username, ");
        sql.append("  rb.is_hang_guarantee as isHangGuarantee,rb.GUARANTEE_BEGIN_DATE as guaranteeBeginDate,rb.GUARANTEE_END_DATE as guaranteeEndDate ");
        sql.append(" from RAT_BKRATION rb ");
        sql.append(" left join bt_input_bank_info bank on bank.sys_bank_code = rb.bank_code ");
        sql.append(" inner join bt_bank_type bankType on bank.BANK_PREFIX = bankType.BANK_PREFIX and (bankType.bank_name_Prefix is null or bank.bank_name like bt_concat3('%',bankType.bank_name_Prefix,'%')) ");

        sql.append(" where 1 = 1 ");
        sql.append("   and rb.status <> -2 ");
        Map<String, Object> params = input.getFormdata();
        if (null != params.get("itemFlag") && StringUtils.isNotEmpty(params.get("itemFlag").toString())) {
            sql.append("   and rb.id in (select distinct rat_code from RAT_BKRATION_DETAIL where ITEM_FLAG = '"
                    + params.get("itemFlag").toString() + "')");
            input.getFormdata().remove("itemFlag");
        }
        if (null != params.get("itemCode") && StringUtils.isNotEmpty(params.get("itemCode").toString())) {
            List<String> itemCodeList = Arrays.asList(params.get("itemCode").toString().split(CommonConstant.COMMA_SPLIT));
            sql.append(" and rb.id in (select distinct rat_code from RAT_BKRATION_DETAIL where 1 = 1 ");
            sql.append(" and ").append(SqlUtil.getInSql(itemCodeList, "ITEM_CODE", true)).append(") ");
            input.getFormdata().remove("itemCode");
        }
        if ((null != params.get("gMoney") && StringUtils.isNotEmpty(params.get("gMoney").toString()))
                || (null != params.get("lMoney") && StringUtils.isNotEmpty(params.get("lMoney").toString()))) {
            sql.append(
                    "   and rb.id in (select distinct rat_code from RAT_BKRATION_DETAIL where (ITEM_FLAG = '0' or (ITEM_FLAG = '1' and COMP_SIGN = '1')) ");
            if (null != params.get("gMoney") && StringUtils.isNotEmpty(params.get("gMoney").toString())) {
                sql.append(" and (money+super_add_money) >= " + params.get("gMoney").toString());
                input.getFormdata().remove("gMoney");
            }
            if (null != params.get("lMoney") && StringUtils.isNotEmpty(params.get("lMoney").toString())) {
                sql.append(" and (money+super_add_money) <= " + params.get("lMoney").toString());
                input.getFormdata().remove("lMoney");
            }
            sql.append(" ) ");
        }
        sql.append("   and ( " + getSQLIn(ids, 900, "rb.corp_id") + ") ");
        OrderString order = new OrderString("rb.CREATE_DATE", "desc");
        input.setOrderString(order);
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false);
    }
    /**
     * 功能介绍 : 查询授信录入审批数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput listApproveOutputData(DataTablesInput input, String businessSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称信息更改
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ");
        sql.append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ");
        sql.append(" rb.cur_code as curCode,rb.cur_name as curName,rb.create_date as createDate, ");
        sql.append(" rb.user_name userName, ");
        sql.append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ");
        sql.append(" from RAT_BKRATION rb ");
//        sql.append(" left join sys_corp sc on rb.corp_id = sc.id");
//        sql.append(" left join bt_bank bb on rb.bank_code = bb.sys_bank_code ");
//        sql.append(" left join BT_CURRENCY bc on rb.cur_code = bc.id ");
//        sql.append(" left join sys_user su on rb.create_by = su.id");
        sql.append(" where 1 = 1 ");
        if (EmptyUtils.isNotEmpty(businessSql)){
            sql.append(" and " + businessSql);
        }
        Object corpIdObj = input.getFormdata().get("corpId");
        if (StringUtils.isNotEmpty(corpIdObj) && !StringUtils.isEmpty(corpIdObj.toString())) {
            String corpId = corpIdObj.toString();
            sql.append(" and rb.corp_Id = :corpId ");
            params.put("corpId", corpId);
        }
        // sql.append(getWhereCondition(input, new RatBkrationVO(), null, false));
        // params.putAll(input.getSearchValues());
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false,
                DynamicallyObject.getInstance(params));
    }
    /**
     * 功能介绍 : 查询授信合同号是否存在。<br>
     * <b>日期：</b>2017年9月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkration> checkContractNo(String contractNo, String ratCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select t from RatBkration t where t.contractNo = :contractNo and t.status <> :status ");
        if (!EmptyUtils.isEmpty(ratCode)) {
            hql.append(" and t.id <> :ratCode ");
            params.put("ratCode", ratCode);
        }
        params.put("contractNo", contractNo);
        params.put("status", RationConstant.STATUS_DELETE);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 功能介绍 : 获取授信录入关联信息。<br>
     * <b>日期：</b>2017年7月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationPageVO getBkRationBeanById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.net_code as netCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rb.rat_mode as ratMode, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money, ")
                .append(" rb.include_bailmoney as includeBailmoney,rb.gua_type as guaType,rb.remark as remark, ")
                .append(" rb.gua_contract as guaContract,rb.gua_corp_id as guaCorpId,rb.openmoney as openMoney, ")
                .append(" rb.attachfilepath as attachfilepath,rb.contract_attachfilepath as contractAttachfilepath,rb.guarantee_rmk as guaranteeRmk, ")
                .append(" rb.user_name userName,rb.bank_type_name as bankTypeName,rb.is_hook_bond as isHookBond, ")
                .append(" rb.IS_HANG_GUARANTEE as  isHangGuarantee,rb.con_id as conId,rb.OLD_RAT_CODE as oldRatCode,rb.GUARANTEE_BEGIN_DATE as guaranteeBeginDate,rb.GUARANTEE_END_DATE as guaranteeEndDate ")
                .append(" from RAT_BKRATION rb where rb.status <> -2 and rb.id = :id ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        List<RatBkrationPageVO> list = listBySQLAliasToBean(sql.toString(), params, RatBkrationPageVO.class);
        if (null == list || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 功能介绍 : 获取已分配的授信录入明细。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationVm> getBkRationDised(RatBkrationDisListDTO disDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId, ")
                //TODO 补充币种名称
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.cur_code as curCode, rb.cur_name as curName,")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ")
                .append(" from RAT_BKRATION rb, RAT_BKRATION_DIS rd ")
                .append(" where rb.id = rd.rat_code and rb.status = :status  ")
                .append("  and rb.bank_code = :bankCode and rb.cur_code = :curCode ")
                .append("  and rd.corp_id = :corpId and rd.rat_Code = :ratCode  ")
                .append("  and rd.status not in(-2,-3) ");
        if (!StringUtils.isEmpty(disDTO.getId())) {
            sql.append(" and rd.id <> :disId ");
            params.put("disId", disDTO.getId());
        }
        params.put("status", CommonConstant.STATUE_CHECK);
        // params.put("statusDel", CommonConstant.STATUE_DELETE);
        params.put("bankCode", disDTO.getBankCode());
        params.put("curCode", disDTO.getCurCode());
        params.put("corpId", disDTO.getCorpId());
        params.put("ratCode", disDTO.getRatCode());
        return listBySQLAliasToBean(sql.toString(), params, RatBkRationVm.class);
    }
    /**
     * 功能介绍 : 根据授信额度单ID获取授信信息。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationVm> queryRationByRatCode(RatUrationBeanDTO uration) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId, ")
                //TODO 补充名称信息
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.cur_code as curCode, rb.cur_name as curName,")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ")
                .append(" from RAT_BKRATION rb ").append(" where rb.begindate > :beginDate   ")
                .append("  and rb.bank_code = :bankCode and rb.cur_code = :curCode ")
                .append("  and rb.status = :status ").append("  and rb.id = :ratCode ");
        sql.append(" order by rb.begindate desc ");
        params.put("corpId", uration.getCorpId());
        params.put("curCode", uration.getCurCode());
        params.put("ratCode", uration.getNewRatCode());
        params.put("bankCode", uration.getBankCode());
        params.put("beginDate", uration.getBeginDate());
        params.put("status", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RatBkRationVm.class);
    }
    /*
     * 获取所有的授信录入通过的授信单
     */
    public List<RatBkRationVm> queryRationAll(RatBkRationVm uration) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId, ")
                //TODO 补充币种名称
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.cur_code as curCode, rb.cur_name as curName,")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ")
                .append(" from RAT_BKRATION rb ").append(" where  rb.corp_id= :corpId  ")
                .append("  and  rb.cur_code = :curCode ").append("  and rb.BANK_TYPE = :bankType ")
                .append(" and rb.RAT_MODE='1' ")
                .append(" and not exists(select 1 from RAT_BKRATION rb1 where rb1.status<>-2 and rb1.OLD_RAT_CODE=rb.id ");
        if (StringUtils.isNotEmpty(uration.getRatCode())) {
            sql.append(" and rb1.rat_code<>:ratCode)");
            sql.append(" and rb.rat_code<>:ratCode");
            params.put("ratCode", uration.getRatCode());
        } else {
            sql.append(" )");
        }
        sql.append("  and rb.status = :status ");
        sql.append(" order by rb.begindate desc ");
        params.put("corpId", uration.getCorpId());
        params.put("curCode", uration.getCurCode());
        params.put("bankType", uration.getBankType());
        params.put("status", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RatBkRationVm.class);
    }
    /**
     * 功能介绍 : 获取相同单位银行币别的其他已分配授信录入信息。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationVm> queryCorpOtherRation(RatUrationBeanDTO uration, String changeType) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.cur_code as curCode, rb.cur_name as curName,")
                .append(" rb.begindate as begindate,rb.enddate as enddate,rb.status as status,rb.money as money ")
                .append(" from RAT_BKRATION rb ")
                // 只需要满足新的授信结束日期大于旧授信结束日期即可
                .append(" where rb.enddate > :endDate ")
                .append(" and rb.status = :status and rb.cur_code = :curCode ")
                .append(" and LEFT(rb.bank_code, 3) = :bankCode ")
                .append(" and exists ( select 1 from RAT_BKRATION_DIS rd where rb.id = rd.rat_code ")
                .append(" and rd.corp_id = :corpId and rd.status = :status ) ");
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            sql.append(" and exists (select 1 from RAT_BKRATION_DETAIL rt ");
            sql.append(" inner join RAT_ITEM_CUSTOM rc on rt.item_code = rc.id and rc.status = 0 ");
            sql.append(" where rb.id = rt.rat_code ");
            sql.append(" and bt_nvl_char(rt.credit_type, '1') <> 0 ");//过滤不循环授信
            if (!StringUtils.isEmpty(uration.getItemCode())) {
                sql.append(" and rt.item_code = :itemCode ");
                params.put("itemCode", uration.getItemCode());//授信品种
            }
            if (!StringUtils.isEmpty(uration.getType())) {
                sql.append(" and rc.sys_item_code = :type  ");
                params.put("type", uration.getType());//系统品种
            }
            if (EmptyUtils.isNotEmpty(changeType) && "3".equals(changeType)) {
                sql.append(" and rt.item_flag = :itemFlag  ");
                params.put("itemFlag", RationConstant.RAT_COMFLAG_1);// 系统品种
            } else if (EmptyUtils.isNotEmpty(changeType) && "2".equals(changeType)) {
                return null;// 单品种不能实现综合转非综合，返回空值
            } else if (EmptyUtils.isNotEmpty(changeType) && "1".equals(changeType)) {
                sql.append(" and rt.item_flag = :itemFlag");
                params.put("itemFlag", uration.getItemFlag());
            }
            sql.append(" ) ");
        } else {
            sql.append(" and exists (select 1 from RAT_BKRATION_DETAIL rt ");
            sql.append(" inner join RAT_ITEM_CUSTOM rc on rt.item_code = rc.id and rc.status = 0 ");
            sql.append(" where rb.id = rt.rat_code ");
            if (!StringUtils.isEmpty(uration.getItemCode())) {
                sql.append(" and rt.item_code = :itemCode ");
                params.put("itemCode", uration.getItemCode());//授信品种
            }
            if (EmptyUtils.isNotEmpty(changeType) && "3".equals(changeType)) {
                return null;//综合品种不能实现非综合转综合，返回空值
            }
            if (EmptyUtils.isNotEmpty(changeType) && "2".equals(changeType)) {
                sql.append(" and rt.item_flag = :itemFlag  ");
                params.put("itemFlag", RationConstant.RAT_COMFLAG_0);//系统品种
            } else if (EmptyUtils.isNotEmpty(changeType) && "1".equals(changeType)) {
                sql.append(" and rt.item_flag = :itemFlag");
                params.put("itemFlag", uration.getItemFlag());
            }
            sql.append(" ) ");
        }
        sql.append(" order by rb.begindate asc ");

        params.put("corpId", uration.getCorpId());
        params.put("curCode", uration.getCurCode());
        params.put("ratCode", uration.getRatCode());
        params.put("bankCode", uration.getBankCode().substring(0, 3));
        params.put("endDate", uration.getEndDate());
        params.put("ratMode", uration.getRatMode());
        params.put("status", CommonConstant.STATUE_CHECK);

        log.info("查询可结转的授信额度单，sql：{}", sql.toString());
        log.info("查询可结转的授信额度单，params：{}", params);
        return listBySQLAliasToBean(sql.toString(), params, RatBkRationVm.class);
    }
    /**
     * 功能介绍 : 查询授信录入银行。<br>
     * <b>日期：</b>2017年9月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<BtBankDTO> queryRationBank() {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct rb.bank_code as bankCode, rb.bank_name as bankName ");
        sql.append(" from rat_bkration rb ");
        sql.append(" where rb.status <> :status and rat_type = :rattype");
        sql.append(" order by rb.bank_code ");
        params.put("status", RationConstant.STATUS_DELETE);
        params.put("rattype",RationConstant.RAT_TYPE_0);
        return listBySQLAliasToBean(sql.toString(), params, BtBankDTO.class);
    }
    /**
     * 功能介绍 : 查询到期日之前的授信合同。<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkration> queryBkrationListDTOs(Date endDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rb from RatBkration rb where rb.status = :status  ");
        hql.append(" and (rb.sendSign = :sendSign or rb.sendSign is null) and rb.enddate <= :endDate ");
        hql.append(" and rb.ratType=:ratType ");
        hql.append(" order by rb.enddate ");
        params.put("sendSign", CommonConstant.SEND_SIGN_NO);
        params.put("status", RationConstant.STATUS_CHECK);
        params.put("endDate", endDate);
        params.put("ratType", RationConstant.RAT_TYPE_0);
        return listByHQL(hql.toString(), params);
    }

    /**
     * 根据到期日期查询授信
     *
     * @param endDate 到期日期
     * @return 授信集合
     */
    public List<RatBkration> queryBkrationByEndDate(Date endDate) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rb from RatBkration rb where rb.status = :status ");
        hql.append(" and rb.enddate = :endDate ");
        hql.append(" and rb.ratType = :ratType ");
        hql.append(" order by rb.enddate ");
        params.put("status", RationConstant.STATUS_CHECK);
        params.put("endDate", endDate);
        params.put("ratType", RationConstant.RAT_TYPE_0);
        return listByHQL(hql.toString(), params);
    }

    /**
     * 获取银行授信审批通过的数据
     *
     * @param input
     * @return
     * @author luxy
     */
    public DataTablesOutput ratBkData(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        String nowDate = DateUtils.date2Str(DateUtils.getDate(), DateUtils.date_sdf());
        sql.append(
                "select rb.id as id,rb.rat_code as ratCode,rb.net_code as netCode,rb.corp_id as corpId,rb.rat_type as ratType,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rb.rat_mode as ratMode, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money, ")
                .append(" rb.gua_contract as guaContract,rb.is_hang_guarantee as isHangGuarantee ")
//                .append(" from RAT_BKRATION rb, sys_corp sc,bt_bank bb,BT_CURRENCY bc ")
//                .append(" where rb.corp_id = sc.id and rb.bank_code = bb.sys_bank_code ")
//                .append("  and rb.cur_code = bc.id and rb.rat_type=0 and rb.status=95 ")
                .append(" from RAT_BKRATION rb ")
                .append(" where rb.rat_type=0 and rb.status=95 ")
                .append("  and rb.id in(select rat_code from rat_bkration_dis where status=95)  and rb.enddate>bt_to_date('"
                        + nowDate + "','yyyy-MM-dd')");
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false);
    }
    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String status) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(*) from RAT_BKRATION where corp_id=:corpId and rat_type=0");
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in (:status) ");
            params.put("status", status.split(","));
        } else {
            sql.append(" and status<>-2 ");
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
    /*
     * 授信录入是否关联其他业务，true 关联 false 不关联
     */
    public boolean checkBkrationInother(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select rb.id as id,rb.rat_code as ratCode from RAT_BKRATION rb ");
        sql.append(" where 1=1 and rb.status <> -2");
        sql.append(" and not exists (select 1 from RAT_URATION ru where ru.RAT_CODE=rb.id )");
        sql.append(" and not exists (select 1 from RAT_BKRATION_ADD rba where rba.RAT_CODE=rb.id )");
        sql.append(" and not exists (select 1 from RAT_BKRATION_RELIEF rbr where rbr.RAT_CODE=rb.id )");
        sql.append(" and not exists (select 1 from RAT_BKRATION_ADJUST rba where rba.BUSINESS_ID=rb.id )");
        sql.append(" and not exists (select 1 from RAT_BKRATION_DELAY rbd where rbd.RAT_CODE=rb.id )");
        sql.append(" and rb.id=:id");
        params.put("id", id);
        List<RatBkrationVO> list = listBySQLAliasToBean(sql.toString(), params, RatBkrationVO.class);
        return CollectionUtils.isEmpty(list);
    }
    public DataTablesOutput<GuaContractOccupVO> getGuaForRat(DataTablesInput input, List<String> corpIds, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select n.rat_code as itemNo, ");
        sql.append(" n.id as id,n.contract_no as ratContractNo,n.rat_mode as ratMode,n.money as money, ");
        //TODO 补充名称信息
        sql.append(" n.cur_name as curName, n.corp_name as corpName, ");
        sql.append(" n.begindate as beginDate,n.enddate as endDate,n.corp_id as corpId,n.cur_code as curId ");
        sql.append(" from rat_bkration n where n.is_hang_guarantee = '1' ");
        if (EmptyUtils.isNotEmpty(corpIds)) {
            sql.append(" and " + getSQLIn(ids, 900, "n.id"));
        }
        if (EmptyUtils.isNotEmpty(ids)) {
            sql.append(" and " + getSQLIn(corpIds, 900, "n.corp_id"));
        }
        return getDynamicBySQL(input, sql.toString(), GuaContractOccupVO.class, "n", false);
    }
    public List<GuaContractOccupDTO> getUseContractList() {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.CONTRACT_NO as ratContractNo, ");
        sql.append(" n.RAT_MODE as ratMode, ");
        sql.append(" n.MONEY as money, ");
        sql.append(" n.BEGINDATE as beginDate, ");
        sql.append(" n.ENDDATE as endDate, n.ID as id");
        sql.append(" from rat_bkration n ");
        sql.append("where n.IS_HANG_GUARANTEE = '1' ");
        return this.listBySQLAliasToBean(sql.toString(), null, GuaContractOccupDTO.class);
    }
}
