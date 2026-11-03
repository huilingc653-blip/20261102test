package com.bytter.increment.domain.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.api.constant.ChoWorkFlowConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.domain.dao.ChoProductRenewalDao;
import com.bytter.increment.domain.entity.ChoProductRenewal;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductRedeemVo;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * 理财产品续期service
 *
 * @author ouyj
 */
@WorkFlow(ChoWorkFlowConstant.CHO_PRODUCT_RENEWAL)
@Service
@Transactional
public class ChoProductRenewalService extends BaseService<ChoProductRenewal, String> implements IWorkFlowListener {
    @Autowired
    private ChoProductRenewalDao choProductRenewalDao;
    @Autowired
    private ChoProductReqService choProductReqService;
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @Autowired
    private ChoMessageService choMessageService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<ChoProductRenewal, String> getEntityDao() {
        return choProductRenewalDao;
    }
    /**
     * 保存对象
     *
     * @param entity
     */
    public void save(ChoProductRenewal entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新对象
     *
     * @param entity
     */
    public void update(ChoProductRenewal entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存或更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(ChoProductRenewal entity) {
        super.saveOrUpdate(entity);
    }
    /**
     * 删除对象  逻辑删除
     *
     * @param entity
     */
    @Override
    public void deleteEntity(ChoProductRenewal entity) {
        entity.setStatus(ChoProductReqConstant.STATUE_DEL);
        choProductReqService.updateBizStatus(entity.getRedeemId(),
                ChoProductReqConstant.BIZ_STATUS8);
        this.saveOrUpdate(entity);
    }
    /**
     * 根据Id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        ChoProductRenewal entity = get(id);
        entity.setStatus(ChoProductReqConstant.STATUE_DEL);
        this.saveOrUpdate(entity);
    }
    /**
     * 保存对象
     *
     * @param entity 理财赎回对象
     * @param user   用户
     */
    public void saveChoProductRedeem(ChoProductRenewal entity, ICurrentUser user) {
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        choProductRenewalDao.save(entity);
        boolean isCheck = ChoProductReqConstant.STATUE_WAIT_CHECK.equals(entity.getStatus());
        Integer messageType = isCheck ? ChoMessageConstant.MESSAGE_TYPE_SUBMIT :ChoMessageConstant.MESSAGE_TYPE_TEMP;
        Integer bizStatus = isCheck ? ChoProductReqConstant.BIZ_STATUS10 : ChoProductReqConstant.BIZ_STATUS9;
        ChoProductReq choProductReq = this.choProductReqService.get(entity.getRedeemId(), true);
        choProductReq.setBizStatus(bizStatus);
        choProductReqService.merge(choProductReq);
        this.getSession().flush();
        this.getSession().clear();
        //生成日志
        choMessageService.saveMessage(entity.getId(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_RENEWAL, user);
        if (isCheck) {
             getStartHandler().handler(ChoWorkFlowConstant.CHO_PRODUCT_RENEWAL, entity.getId(), entity, entity.getCorpId(), user.getId());
        }
    }
    /**
     * 获取列表数据
     *
     * @param dataTablesInput 前端参数
     * @param user            当前用户
     * @return 列表数据
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
        boolean isRenewal = StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("isRenewal"));
        // 移除非SQL条件
        dataTablesInput.getFormdata().remove("isRenewal");
        if (!isRenewal && EmptyUtils.isEmpty(moneyUint)) {
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTRENEWAL_CHOPRODUCTRENEWAL_QUERY, user.getId()), "pr.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = choProductRenewalDao.getDataTablesUnion(dataTablesInput, sql.toString(),isRenewal);
        // 注入远程信息
        Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
        Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
        if (!isRenewal) {
            List<ChoProductRedeemVo> list = output.getData();
            for (ChoProductRedeemVo vo : list) {
                setBeanMoneyType(vo, moneyUint);
                injectValues(listTypeMap, listPayWayMap, vo);
            }
            output.setData(list);
        }else {
            // 注入远程数据
            List<ChoProductReqVo> list = output.getData();
            for (ChoProductReqVo vo : list) {
                injectValues(listTypeMap, listPayWayMap, vo);
            }
        }
        return output;
    }
    /**
     * 注入远程数据。
     *
     * @param listTypeMap   理财类型数据。
     * @param listPayWayMap 付息方式数据。
     * @param product       理财产品信息。
     */
    private void injectValues(Map<String, DictDTO> listTypeMap, Map<String, DictDTO> listPayWayMap, ChoProductRedeemVo product) {
        if (ObjectUtil.isAllNotEmpty(product.getFinancialType(), listTypeMap)) {
            Optional.ofNullable(listTypeMap.get(product.getFinancialType())).ifPresent(dict -> product.setFinancialTypeName(dict.getName()));
        }
        if (ObjectUtil.isAllNotEmpty(product.getPayWay(), listPayWayMap)) {
            Optional.ofNullable(listPayWayMap.get(product.getPayWay())).ifPresent(dict -> product.setPayWayName(dict.getName()));
        }
    }
    /**
     * 注入远程数据。
     *
     * @param listTypeMap   理财类型数据。
     * @param listPayWayMap 付息方式数据。
     * @param product       理财产品信息。
     */
    private void injectValues(Map<String, DictDTO> listTypeMap, Map<String, DictDTO> listPayWayMap, ChoProductReqVo product) {
        if (ObjectUtil.isAllNotEmpty(product.getFinancialType(), listTypeMap)) {
            Optional.ofNullable(listTypeMap.get(product.getFinancialType())).ifPresent(dict -> product.setFinancialTypeName(dict.getName()));
        }
        if (ObjectUtil.isAllNotEmpty(product.getPayWay(), listPayWayMap)) {
            Optional.ofNullable(listPayWayMap.get(product.getPayWay())).ifPresent(dict -> product.setPayWayName(dict.getName()));
        }
    }
    /**
     * 获取列表数据
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForSelect(DataTablesInput dataTablesInput, ICurrentUser user) {
        String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
        if (EmptyUtils.isEmpty(moneyUint)) {
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTREDEEM_CHOPRODUCTREDEEM_QUERY, user.getId()), "prd.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = choProductRenewalDao.getDataTablesUnionForSelect(dataTablesInput, sql.toString());
        List<ChoProductRedeemVo> list = output.getData();
        for (ChoProductRedeemVo vo : list) {
            setBeanMoneyType(vo, moneyUint);
        }
        output.setData(list);
        return output;
    }
    /**
     * 批量提交记录
     *
     * @param ids
     * @param
     */
    public void batchSubmit(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            ChoProductRenewal entity = this.get(id, true);
            entity.setStatus(ChoProductReqConstant.STATUE_WAIT_CHECK);
            entity.setOldId(id);
            this.toModify(entity, user);
        }
    }
    /**
     * 更新对象
     *
     * @param entity 理财续期实体
     * @param user   用户
     */
    public void toModify(ChoProductRenewal entity, ICurrentUser user) {
        ChoProductRenewal t = this.get(entity.getId(), true);
        if (entity.getOldId().equals(entity.getId())) {
            t.setUpdateBy(user.getLoginName());
            t.setUpdateDate(DateUtils.getDate());
            t.setReferIncomeMoney(entity.getReferIncomeMoney());
            t.setDeadlineDate(entity.getDeadlineDate());
            t.setDayLimit(entity.getDayLimit());
            t.setStatus(entity.getStatus());
        } else {
            ChoProductRenewal oldEntity = this.get(entity.getOldId(), true);
            oldEntity.setStatus(ChoProductReqConstant.STATUE_DEL);
            update(oldEntity);
            t = new ChoProductRenewal();
            BeanUtils.copyProperties(entity, t);
            t.setId(null);
            t.setCreateBy(user.getLoginName());
            t.setCreateDate(DateUtils.getDate());
        }
        this.saveOrUpdate(t);
        boolean isCheck = ChoProductReqConstant.STATUE_WAIT_CHECK.equals(entity.getStatus());
        Integer bizStatus = isCheck ? ChoProductReqConstant.BIZ_STATUS10 : ChoProductReqConstant.BIZ_STATUS9;
        Integer messageType = isCheck ? ChoMessageConstant.MESSAGE_TYPE_SUBMIT : ChoMessageConstant.MESSAGE_TYPE_TEMP;
        ChoProductReq choProductReq = this.choProductReqService.get(entity.getRedeemId(), true);
        choProductReq.setBizStatus(bizStatus);
        choProductReqService.merge(choProductReq);
        this.getSession().flush();
        this.getSession().clear();
        //生成日志
        choMessageService.saveMessage(entity.getId(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_RENEWAL, user);
        if (isCheck) {
             getStartHandler().handler(ChoWorkFlowConstant.CHO_PRODUCT_RENEWAL, entity.getId(), entity, choProductReq.getCorpId(), user.getId());
        }
    }
    public ChoProductRedeemVo getChoProductRedeemVoById(String id) {
        ChoProductRedeemVo redeemVo = choProductRenewalDao.getChoProductRedeemVoById(id);
        Optional.ofNullable(redeemVo).ifPresent(redeem -> {
            // 注入远程信息
            Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
            Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
            injectValues(listTypeMap, listPayWayMap, redeemVo);
        });
        return redeemVo;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        ChoProductRenewal entity = this.get(workFlowResult.getBusinessKey(), true);
        Integer messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        Integer bizStatus = ChoProductReqConstant.BIZ_STATUS12;
        if (workFlowResult.isPass()) {
            ChoProductReq choProductReq = choProductReqService.get(entity.getRedeemId(), true);
            choProductReq.setDayLimit(entity.getDayLimit());
            choProductReq.setReferIncomeMoney(entity.getReferIncomeMoney());
            choProductReq.setDeadlineDate(entity.getDeadlineDate());
            choProductReqService.update(choProductReq);
            messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            //审批通过
            entity.setStatus(ChoProductReqConstant.STATUE_CHECK);
        } else {
            //审批打回
            bizStatus = ChoProductReqConstant.BIZ_STATUS11;
            entity.setStatus(ChoProductReqConstant.STATUE_BACK);
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        // 修改业务状态
        choProductReqService.updateBizStatus(entity.getRedeemId(), bizStatus);
        choMessageService.saveMessage(entity.getId(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_RENEWAL, user);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批列表数据
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(ChoWorkFlowConstant.CHO_PRODUCT_RENEWAL, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "pw.id", true);
        DataTablesOutput dp = this.choProductRenewalDao.dataTableApprove(dataTablesInput, businessSql);
        String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
        if (EmptyUtils.isEmpty(moneyUint)) {
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        // 注入远程信息
        Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
        Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
        List<ChoProductRedeemVo> list = dp.getData();
        for (ChoProductRedeemVo vo : list) {
            setBeanMoneyType(vo, moneyUint);
            injectValues(listTypeMap, listPayWayMap, vo);
        }
        dp.setData(list);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 金额单位转换
     *
     * @param ratVO
     * @param moneyType
     */
    private void setBeanMoneyType(ChoProductRedeemVo ratVO, String moneyType) {
        if (ChoProductReqConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
            return;
        }
        Class userCla = (Class) ratVO.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); //设置些属性是可以访问的
                Object val = f.get(ratVO);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                String name = f.getName();//得到属性的名称
                if (type.endsWith("BigDecimal") && name.endsWith("Money")) {
                    if (null == val) {
                        f.set(ratVO, BigDecimal.ZERO);
                    } else {
                        BigDecimal value = (BigDecimal) val;
                        f.set(ratVO, value.divide(BigDecimal.valueOf(Integer.valueOf(ChoProductReqConstant.COMMON_MONEYUNIT_10000)), 6, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据登记id查询续期列表
     *
     * @param id 登记id
     * @return 续期数据
     */
    public List<ChoProductRenewal> listChoProductRenewalByReqId(String id) {
        return choProductRenewalDao.listChoProductRenewalByReqId(id);
    }
}
