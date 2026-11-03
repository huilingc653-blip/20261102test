package com.bytter.credit.domain.dao;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.dto.RatUrationBeanDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @version 1.0
 * @Title:RatCprationDao
 * @Description:
 * @date 2018年11月13日 下午5:41:30
 */
@Repository
public class RatCprationDao extends BaseDao<RatBkration, String> {
    /**
     * 查询内部授信列表
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listOutputData(DataTablesInput input, List<String> corpIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.rat_type as ratType, ")
                .append(" rb.corp_name as corpName,rb.contract_no as contractNo, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ")
                .append(" from RAT_BKRATION rb ")
                .append(" where rb.status <> -2 and rb.rat_type='1' ");
        if (ObjectUtil.isNotEmpty(corpIdList)){
            sql.append(" and ").append(getSQLIn(corpIdList, 900, "rb.corp_id"));
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false);
    }
    /**
     * 根据合同号查询授信信息
     *
     * @param contractNo 授信合同号
     * @author luxy
     */
    @SuppressWarnings("unchecked")
    public List<RatBkration> checkContractNo(String contractNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select t from RatBkration t where t.contractNo = :contractNo and t.ratType=:ratType ");
        hql.append(" and t.status <> :status ");
        params.put("contractNo", contractNo);
        params.put("ratType", RationConstant.RAT_TYPE_1);
        params.put("status", RationConstant.STATUS_DELETE);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 根据id查询授信信息
     *
     * @param id 授信id
     * @author luxy
     */
    public RatBkrationPageVO getCpRationById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rb.bank_type_name as typeName, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money, ")
                .append(" rb.include_bailmoney as includeBailmoney,rb.remark as remark,rb.rat_type as ratType,rb.con_id as conId ")
                .append(" from RAT_BKRATION rb ")
                .append(" where rb.status <> -2 ")
                .append(" and rb.id = :id ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        List<RatBkrationPageVO> list = listBySQLAliasToBean(sql.toString(), params, RatBkrationPageVO.class);
        if (null == list) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 获取单位已录入的授信
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RatBkRationVm> getCorpBkrationListDTOs(RatBkRationVm bkrationDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rb from RatBkration rb where rb.status <> :status  ");
        hql.append(" and rb.corpId = :corpId and rb.curCode = :curCode ");
        hql.append(" and rb.ratType=:ratType");
        // 过滤当前校验数据
        if (!StringUtils.isEmpty(bkrationDTO.getId())) {
            hql.append(" and rb.id <> :ratCode ");
            params.put("ratCode", bkrationDTO.getId());
        }
        hql.append(" order by rb.createDate ");
        params.put("corpId", bkrationDTO.getCorpId());
        params.put("curCode", bkrationDTO.getCurCode());
        params.put("status", CommonConstant.STATUE_DELETE);
        params.put("ratType", RationConstant.RAT_TYPE_1);
        List<RatBkration> list = listByHQL(hql.toString(), params);
        return copyListAndInject(list, RatBkRationVm.class);
    }
    /**
     * 获取待审批列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getApproveDataTable(DataTablesInput input, String businessSql) {
        //TODO 名称调整
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
//        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,sc.name as corpName, ")
//                .append(" rb.contract_no as contractNo, ")
//                .append(" rb.cur_code as curCode,bc.cur_name as curName,rb.create_date as createDate, ")
//                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ")
//                .append(" from RAT_BKRATION rb, sys_corp sc,BT_CURRENCY bc ").append(" where rb.corp_id = sc.id ")
//                .append("  and rb.cur_code = bc.id ");
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName,rb.create_date as createDate, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ")
                .append(" from RAT_BKRATION rb ").append(" where 1=1 ");
        if (EmptyUtils.isNotEmpty(businessSql)) {
            sql.append(" and " + businessSql);
        }
        Object corpIdObj = input.getFormdata().get("corpId");
        if (StringUtils.isNotEmpty(corpIdObj) && !StringUtils.isEmpty(corpIdObj.toString())) {
            String corpId = corpIdObj.toString();
            sql.append(" and rb.corp_Id = :corpId ");
            params.put("corpId", corpId);
        }
        return getDynamicBySQL(input, sql.toString(), RatBkrationVO.class, null, false,
                DynamicallyObject.getInstance(params));
    }
    /**
     * 获取同单位同币别的其他授信分配信息
     */
    public List<RatBkRationVm> queryCorpOtherRation(RatUrationBeanDTO uration) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId, ")
                //TODO 补充币种名称
                .append(" rb.contract_no as contractNo,rb.cur_code as curCode, rb.cur_name as curName,")
                .append(" rb.begindate as begindate,rb.enddate as enddate,rb.status as status,rb.money as money ")
                .append(" from RAT_BKRATION rb ").append(" where rb.begindate >:beginDate   ")
                .append("  and rb.begindate <=:endDate   ").append("  and rb.cur_code = :curCode ")
                .append("  and rb.status = :status ");
        sql.append(" and exists ( select 1 from RAT_BKRATION_DIS rd where rb.id = rd.rat_code ");
        sql.append(" and rd.corp_id = :corpId and rd.status = :status  ) ");
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            sql.append(" and exists (select 1 from RAT_BKRATION_DETAIL rt,RAT_ITEM_CUSTOM rc ");
            sql.append(" where rt.item_code = rc.id and rb.id = rt.rat_code ");
            if (!StringUtils.isEmpty(uration.getItemCode())) {
                sql.append(" and rt.item_code = :itemCode ");
                params.put("itemCode", uration.getItemCode());// 授信品种
            }
            if (!StringUtils.isEmpty(uration.getType())) {
                sql.append(" and rc.sys_item_code = :type  ");
                params.put("type", uration.getType());// 系统品种
            }
            sql.append(" ) ");
        }
        if (!uration.getRatCode().isEmpty()) {
            String ratCode = uration.getRatCode();
            sql.append("  and rb.id not in('" + ratCode + "')");
        }
        sql.append(" order by rb.begindate desc ");
        params.put("corpId", uration.getCorpId());
        params.put("curCode", uration.getCurCode());
        params.put("beginDate", uration.getBeginDate());
        params.put("endDate", uration.getEndDate());
        params.put("status", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RatBkRationVm.class);
    }
    /**
     * 根据授信额度单ID获取授信信息
     */
    public List<RatBkRationVm> queryRationByRatCode(RatUrationBeanDTO uration) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId, ")
                //TODO 补充币种名称
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.cur_code as curCode, rb.cur_name as curName,")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ")
                .append(" from RAT_BKRATION rb ").append(" where rb.begindate > :beginDate   ")
                .append("  and rb.cur_code = :curCode ").append("  and rb.status = :status ")
                .append("  and rb.id = :ratCode ");
        sql.append(" order by rb.begindate desc ");
        params.put("corpId", uration.getCorpId());
        params.put("curCode", uration.getCurCode());
        params.put("ratCode", uration.getNewRatCode());
        params.put("beginDate", uration.getBeginDate());
        params.put("status", CommonConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, RatBkRationVm.class);
    }
    /**
     * 查询内部授信到期数据
     */
    @SuppressWarnings("unchecked")
    public List<RatBkration> queryCpRatList(Date endDate) {
        // Map<String, Object> params = new HashMap<String, Object>();
        // StringBuilder hql = new StringBuilder();
        // hql.append(" select r from RatBkration r where r.status <>:status ")
        // .append(" and r.endDate<=:endDate and r.ratType=:ratType ")
        // .append(" order by r.endDate");
        // params.put("status", RationConstant.STATUS_DELETE);
        // params.put("endDate", endDate);
        // params.put("ratType", RationConstant.RAT_TYPE_1);
        // return listByHQL(hql.toString(), params);
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select rb from RatBkration rb where rb.status = :status  ");
        hql.append(" and (rb.sendSign = :sendSign or rb.sendSign is null) and rb.enddate <= :endDate ");
        hql.append(" and rb.ratType=:ratType ");
        hql.append(" order by rb.enddate ");
        params.put("sendSign", CommonConstant.SEND_SIGN_NO);
        params.put("status", RationConstant.STATUS_CHECK);
        params.put("endDate", endDate);
        params.put("ratType", RationConstant.RAT_TYPE_1);
        return listByHQL(hql.toString(), params);
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
        sql.append("select count(*) from RAT_BKRATION where  corp_id=:corpId and rat_type=1");
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in (:status) ");
            params.put("status", status.split(","));
        } else {
            sql.append(" and status<>-2 ");
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
}
