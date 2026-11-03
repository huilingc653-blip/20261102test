package com.bytter.biz.netbank.dao;

import com.bytter.biz.netbank.entity.BisPayPolicy;
import com.bytter.biz.netbank.vo.BisPayPolicyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 自动支付策略表
 * @date 2020-04-17 14:38:52
 */
@Repository
public class BisPayPolicyDao extends BaseDao<BisPayPolicy, String> {

    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    /**
     * 查询自动支付策略
     *
     * @param input        入参
     * @param authCorpList 有权限的单位集合
     * @return 出参
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> authCorpList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select b.id as id,b.corp_Id as corpId,b.policy_Code as policyCode,b.policy_Name as policyName, ");
        sql.append(" b.cur_Id as curId,b.range1 as range1,b.range_Val1 as rangeVal1,b.range_Join as rangeJoin, ");
        sql.append(" b.range2 as range2,b.range_Val2 as rangeVal2,b.pay_Total as payTotal,b.valid_Sign as validSign, ");
        sql.append(" b.create_By as createBy,b.create_Date as createDate,b.update_By as updateBy,b.update_Date as updateDate, ");
        sql.append(" b.is_work_day as isWorkDay, b.NO_PAY_PURPOSE as noPayPurpose ");
        sql.append(" from bis_pay_policy b where 1 = 1 ");
        //权限单位
        String corpAuthSql = SqlUtil.getInSql(authCorpList, "b.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        //支付收款人
        String payeeName = (String) input.getFormdata().get("likes_payeeName");
        if (StringUtils.isNotEmpty(payeeName)) {
            List<ExternalCorpDTO> listCorp = externalCorpService.findExternalCorpLikeName(payeeName);
            if (CollectionUtils.isEmpty(listCorp)) {
                return new DataTablesOutput();
            }
            List<String> ecIds = listCorp.stream().map(ExternalCorpDTO::getId).collect(Collectors.toList());
            sql.append(" and b.id in (select p.POLICY_ID from BIS_PAY_POLICY_PAYEE p where (p.IS_PAYEE = 1 OR p.IS_PAYEE IS NULL) and ");
            sql.append(SqlUtil.getInSql(ecIds, "p.payee_id ", true)).append(")");
        }
        //不支付收款人
        String noPayeeName = (String) input.getFormdata().get("likes_noPayeeName");
        if (StringUtils.isNotEmpty(noPayeeName)) {
            sql.append(" and b.id in (select p.POLICY_ID from BIS_PAY_POLICY_PAYEE p where p.IS_PAYEE = 0 and p.PAYEE_NAME like :noPayeeName) ");
            params.put("noPayeeName", "%" + noPayeeName + "%");
        }
        //业务类别
        String busTypeName = (String) input.getFormdata().get("likes_busTypeName");
        if (StringUtils.isNotEmpty(busTypeName)) {
            sql.append(" and b.id in (select t.POLICY_ID from BIS_PAY_POLICY_BUSTYPE t left join NIS_BUS_TYPE n on t.BUS_TYPE_ID = n.ID where n.BUS_TYPE_NAME like :busTypeName)");
            params.put("busTypeName", "%" + busTypeName + "%");
        }
        //共享业务类别
        String shareBusTypeName = (String) input.getFormdata().get("likes_shareBusTypeName");
        if (StringUtils.isNotEmpty(shareBusTypeName)) {
            sql.append(" and b.id in (select t.POLICY_ID from BIS_PAY_POLICY_BUSTYPE t left join NIS_SHARE_BUS_TYPE nsbt on t.SHARE_BUS_TYPE_ID = nsbt.ID where nsbt.SHARE_BUS_TYPE_NAME like :shareBusTypeName)");
            params.put("shareBusTypeName", "%" + shareBusTypeName + "%");
        }
        return getDynamicBySQL(input, sql.toString(), BisPayPolicyVO.class, "b", false, params);
    }

    /**
     * 根据ID查询交易失败原因
     *
     * @param id
     * @return
     */
    public BisPayPolicyVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.corp_Id as corpId,b.policy_Code as policyCode,b.policy_Name as policyName, ");
        sql.append(" b.cur_Id as curId,b.range1 as range1,b.range_Val1 as rangeVal1,b.range_Join as rangeJoin, ");
        sql.append(" b.range2 as range2,b.range_Val2 as rangeVal2,b.pay_Total as payTotal,b.valid_Sign as validSign, ");
        sql.append(" b.create_By as createBy,b.create_Date as createDate,b.update_By as updateBy,b.update_Date as updateDate, ");
        sql.append(" b.is_work_day as isWorkDay, b.NO_PAY_PURPOSE as noPayPurpose ");
        sql.append(" from bis_pay_policy b where b.id = :id ");
        HashMap<String, String> params = new HashMap();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisPayPolicyVO.class);
    }

    /**
     * 重复校验
     *
     * @param propertyName
     * @param propertyValue
     * @return
     */
    public BisPayPolicy validByPropertyName(String propertyName, String propertyValue, String id) {
        BisPayPolicy reason = getUniqueBy(propertyName, propertyValue);
        if (reason != null && StringUtils.isNotEmpty(id) && reason.getId().equals(id)) {
            return null;
        }
        return reason;
    }

    /**
     * 校验同单位同币别不允许有同时启用的策略
     *
     * @param searchVO 查询条件
     * @return 策略集合
     */
    public List<BisPayPolicyVO> listByPolicy(BisPayPolicyVO searchVO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.corp_Id as corpId,b.policy_Code as policyCode,b.policy_Name as policyName, ");
        sql.append(" b.cur_Id as curId,b.range1 as range1,b.range_Val1 as rangeVal1,b.range_Join as rangeJoin, ");
        sql.append(" b.range2 as range2,b.range_Val2 as rangeVal2,b.pay_Total as payTotal,b.valid_Sign as validSign ");
        sql.append(" from bis_pay_policy b ");
        sql.append(" left join bis_pay_policy_payee p on p.policy_Id = b.id");
        sql.append(" where 1 = 1 and b.corp_Id = :corpId and b.cur_Id = :curId and b.valid_sign = :validSign ");
        HashMap<String, String> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(searchVO.getId())) {
            sql.append(" and b.id <> :policyId");
            params.put("policyId", searchVO.getId());
        }
        if (StringUtils.isNotBlank(searchVO.getPayeeId())) {
            sql.append(" and p.payee_Id = :payeeId ");
            params.put("payeeId", searchVO.getPayeeId());
        }
        params.put("corpId", searchVO.getCorpId());
        params.put("curId", searchVO.getCurId());
        params.put("validSign", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, BisPayPolicyVO.class);
    }

    /**
     * 获取启动的策略
     *
     * @return 自动支付策略集合
     */
    public List<BisPayPolicyVO> getValidBisPayPolicy() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.corp_Id as corpId,b.policy_Code as policyCode,b.policy_Name as policyName, ");
        sql.append(" b.cur_Id as curId,b.range1 as range1,b.range_Val1 as rangeVal1,b.range_Join as rangeJoin, ");
        sql.append(" b.range2 as range2,b.range_Val2 as rangeVal2,b.pay_Total as payTotal,b.valid_Sign as validSign, ");
        sql.append(" b.create_By as createBy,b.create_Date as createDate,b.update_By as updateBy,b.update_Date as updateDate, ");
        sql.append(" b.is_work_day as isWorkDay, b.NO_PAY_PURPOSE as noPayPurpose ");
        sql.append(" from bis_pay_policy b where b.valid_Sign = :validSign ");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("validSign", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, BisPayPolicyVO.class);
    }
}
