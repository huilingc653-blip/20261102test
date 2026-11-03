package com.bytter.increment.domain.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.api.constant.ChoWorkFlowConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.api.dto.ChoProductReqDTO;
import com.bytter.increment.api.enums.ChoProductMessageEnum;
import com.bytter.increment.domain.dao.ChoProductReqDao;
import com.bytter.increment.domain.entity.ChoProduct;
import com.bytter.increment.domain.entity.ChoProductNetVal;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductDetailedVo;
import com.bytter.increment.domain.vo.ChoProductReqImportVo;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.increment.domain.vo.ChoProductVo;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shenc
 * @version 1.0
 * @Title: Service
 * @Description: 理财产品
 * @date 2019-03-26 14:03:30
 */
@WorkFlow(ChoWorkFlowConstant.CHO_PRODUCT_REQ)
@Service
@Transactional
public class ChoProductReqService extends BaseService<ChoProductReq, String>
        implements IWorkFlowListener, IWorkFlowRevokeListener, ICorpDeleteVerifyService,
        ICorpDisableVerifyService, IExcelVerifyHandler<ChoProductReqImportVo>  , DictInjectProvider {
    @Autowired
    private ChoProductReqDao choProductReqDao;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private ChoMessageService choMessageService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired(required = false)
    private ChoProductRedeemService choProductRedeemService;
    @Autowired(required = false)
    private ChoProductRenewalService choProductRenewalService;
    @Autowired
    private ChoProductService choProductService;
    @Autowired
    private ChoProductNetValService choProductNetValService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @Override
    public BaseDao<ChoProductReq, String> getEntityDao() {
        return choProductReqDao;
    }

    /**
     * 保存对象
     *
     * @param entity
     */
    public void save(ChoProductReq entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新对象
     *
     * @param entity
     */
    public void update(ChoProductReq entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存或更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(ChoProductReq entity) {
        super.saveOrUpdate(entity);
    }

    /**
     * 删除对象  逻辑删除
     *
     * @param entity
     */
    @Override
    public void deleteEntity(ChoProductReq entity) {
        entity.setStatus(ChoProductReqConstant.STATUE_DEL);
        this.saveOrUpdate(entity);
    }

    /**
     * 根据Id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        ChoProductReq entity = get(id);
        entity.setStatus(ChoProductReqConstant.STATUE_DEL);
        this.saveOrUpdate(entity);
    }

    /**
     * 打回到购买申请
     *
     * @param id id
     */
    public void back(String id) {
        ChoProductReq entity = this.get(id);
        entity.setStatus(null);
        entity.setBuyApplyStatus(ChoProductReqConstant.STATUE_BACK);
        entity.setBizStatus(ChoProductReqConstant.BIZ_STATUS15);
        this.updateEntity(entity);
    }

    /**
     * 保存ChoProductReq实体
     *
     * @param choProductReq
     * @param user
     */
    public void saveChoProductReq(ChoProductReq choProductReq, ICurrentUser user) {
        // 校验产品代码是否已存在
        ChoProductVo choProductVo = choProductService.getByCode(choProductReq.getProductCode());
        if (!StringUtils.isBlank(choProductReq.getCurId())) {
            BtCurrencyDTO currencyDTO = currencyService.getObjectById(choProductReq.getCurId());
            Optional.ofNullable(currencyDTO).ifPresent(currencyDO -> choProductReq.setCurName(currencyDO.getCurName()));
        }
        if (!StringUtils.isBlank(choProductReq.getCorpId())) {
            CorpDTO corpDTO = corpService.get(choProductReq.getCorpId());
            Optional.ofNullable(corpDTO).ifPresent(corpDO -> choProductReq.setCorpName(corpDO.getName()));
        }
        if (!StringUtils.isBlank(choProductReq.getBankTypeId())) {
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(choProductReq.getBankTypeId());
            Optional.ofNullable(bankTypeDTO).ifPresent(bankTypeDO -> choProductReq.setBankTypeName(bankTypeDO.getTypeName()));
        }
        if (choProductVo == null) {
            // 不存在则新增一个产品类型
            ChoProduct saveProduct = BeanUtils.copyNewInstanceProperties(choProductReq, ChoProduct.class);
            // 先设置为无效的，审批通过后修改为有效
            saveProduct.setValidSign(CommonConstant.VALID_SIGN_NO);
            saveProduct.setIsApprove(CommonConstant.VALID_SIGN_NO);
            saveProduct.setId(null);
            choProductService.save(saveProduct);
        } else {
            // 产品代码已存在，则校验产品代码、产品类型、产品名称、银行类别、币种是否一致
            ChoProductVo checkVo = choProductService.checkIsRepeat(choProductReq);
            // 如果为null，则表示产品代码一致，但是产品类型、产品名称、银行类别、币种不一致
            if (checkVo == null) {
                throw new ParameterException(ChoProductMessageEnum.CHO_PRODUCT_TYPE_CODE_UNIQUE);
            }
        }
        choProductReq.setBillNo(SequenceSingleton.getSequence(SequenceEnum.CHO_PRODUCT_REQ));
        if (StringUtils.isBlank(choProductReq.getId())) {
            choProductReq.setCreateBy(user.getLoginName());
            choProductReq.setCreateDate(DateUtils.getDate());
        }
        boolean isSubmit = ChoProductReqConstant.STATUE_WAIT_CHECK.equals(choProductReq.getStatus());
        choProductReq.setBizStatus(isSubmit ? ChoProductReqConstant.BIZ_STATUS2 : ChoProductReqConstant.BIZ_STATUS1);
        save(choProductReq);
        Integer messageType = ChoMessageConstant.MESSAGE_TYPE_TEMP;
        if (isSubmit) {
            messageType = ChoMessageConstant.MESSAGE_TYPE_SUBMIT;
            getStartHandler().handler(ChoWorkFlowConstant.CHO_PRODUCT_REQ, choProductReq.getId(), choProductReq, choProductReq.getCorpId(), user.getId());
        }
        notificationService.complete(NotificationConstant.CHOWMATISTIC.CHO_PRODUCT_REQ, choProductReq.getId(), user.getId());
        //生成日志
        choMessageService.saveMessage(choProductReq.getBillNo(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ, user);
    }

    /**
     * 更新对象
     *
     * @param entity
     * @param user
     */
    public void toModify(ChoProductReq entity, ICurrentUser user) {
        if (!StringUtils.isBlank(entity.getCurId())) {
            BtCurrencyDTO currencyDTO = currencyService.getObjectById(entity.getCurId());
            Optional.ofNullable(currencyDTO).ifPresent(currencyDO -> entity.setCurName(currencyDO.getCurName()));
        }
        if (!StringUtils.isBlank(entity.getCorpId())) {
            CorpDTO corpDTO = corpService.get(entity.getCorpId());
            Optional.ofNullable(corpDTO).ifPresent(corpDO -> entity.setCorpName(corpDO.getName()));
        }
        if (!StringUtils.isBlank(entity.getBankTypeId())) {
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(entity.getBankTypeId());
            Optional.ofNullable(bankTypeDTO).ifPresent(bankTypeDO -> entity.setBankTypeName(bankTypeDO.getTypeName()));
        }
        // 批量提交不需要进行该校验
        if (entity.getBatchSubmit() == null) {
            // 修改前的产品代码信息
            String lastProductCode = entity.getLastProductCode();
            // 修改后的产品代码信息
            String productCode = entity.getProductCode();
            if (StringUtil.isNotEmpty(lastProductCode)) {
                ChoProductVo lastVo = choProductService.getByCode(lastProductCode);
                boolean isNotApprove = CommonConstant.VALID_SIGN_NO.equals(lastVo.getValidSign()) && CommonConstant.VALID_SIGN_NO.equals(lastVo.getIsApprove());
                // 修改前的产品代码与本次提交的产品代码相等
                if (lastProductCode.equals(productCode)) {
                    if (isNotApprove) { //如果是理财登记新增的理财类型
                        // 修改产品类型信息
                        ChoProduct choProduct = choProductService.get(lastVo.getId());
                        choProduct.setProductName(entity.getProductName());
                        choProduct.setProductType(entity.getProductType());
                        choProduct.setFinancialType(entity.getFinancialType());
                        choProduct.setCurId(entity.getCurId());
                        choProduct.setBankTypeId(entity.getBankTypeId());
                        choProductService.update(choProduct);
                    } else {
                        // 产品代码已存在，则校验产品代码、产品类型、产品名称、银行类别、币种是否一致
                        ChoProductVo checkVo = choProductService.checkIsRepeat(entity);
                        // 如果为null，则表示产品代码一致，但是产品类型、产品名称、银行类别、币种不一致
                        if (checkVo == null) {
                            throw new ParameterException(ChoProductMessageEnum.CHO_PRODUCT_TYPE_CODE_UNIQUE);
                        }
                    }
                } else { // 产品代码和上一次的不相等
                    // 判断修改前的产品代码是否为理财登记新增的
                    if (isNotApprove) {
                        // 是新增的，则直接删除对应的理财类型
                        choProductService.delByCode(lastProductCode);
                    }
                    // 产品代码和上一次的不相等
                    this.checkIsSave(productCode, entity);
                }
            } else {
                // erp来的数据，理财产品可以为空。所以修改前的产品代码为空
                this.checkIsSave(productCode, entity);
            }
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        boolean isSubmit = ChoProductReqConstant.STATUE_WAIT_CHECK.equals(entity.getStatus());
        entity.setBizStatus(isSubmit ? ChoProductReqConstant.BIZ_STATUS2 : ChoProductReqConstant.BIZ_STATUS1);
        this.saveOrUpdate(entity);
        Integer messageType = ChoMessageConstant.MESSAGE_TYPE_TEMP;
        if (isSubmit) {
            messageType = ChoMessageConstant.MESSAGE_TYPE_SUBMIT;
            getStartHandler().handler(ChoWorkFlowConstant.CHO_PRODUCT_REQ, entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        //生成日志
        choMessageService.saveMessage(entity.getBillNo(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ, user);
        notificationService.complete(NotificationConstant.CHOWMATISTIC.CHO_PRODUCT_REQ, entity.getId(), user.getId());
    }

    /**
     * 理财登记修改时，修改前后的产品代码不相等时，需要判断是否新增产品类型，还是提示
     *
     * @param productCode 产品代码
     * @param entity      实体对象
     */
    private void checkIsSave(String productCode, ChoProductReq entity) {
        // 校验产品代码是否已存在
        ChoProductVo choProductVo = choProductService.getByCode(productCode);
        if (choProductVo == null) {
            // 不存在则新增一个产品类型
            ChoProduct saveProduct = BeanUtils.copyNewInstanceProperties(entity, ChoProduct.class);
            // 先设置为无效的，审批通过后修改为有效
            saveProduct.setValidSign(CommonConstant.VALID_SIGN_NO);
            saveProduct.setIsApprove(CommonConstant.VALID_SIGN_NO);
            saveProduct.setId(null);
            choProductService.save(saveProduct);
        } else {
            // 产品代码已存在，则校验产品代码、产品类型、产品名称、银行类别、币种是否一致
            ChoProductVo checkVo = choProductService.checkIsRepeat(entity);
            // 如果为null，则表示产品代码一致，但是产品类型、产品名称、银行类别、币种不一致
            if (checkVo == null) {
                throw new ParameterException(ChoProductMessageEnum.CHO_PRODUCT_TYPE_CODE_UNIQUE);
            }
        }
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
        if (EmptyUtils.isEmpty(moneyUint)) {
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTREQ_CHOPRODUCTREQ_QUERY, user.getId()), "pr.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = choProductReqDao.getDataTablesUnion(dataTablesInput, sql.toString());
        // 注入远程信息
        Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
        Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
        List<ChoProductReqVo> list = output.getData();
        List<String> corpIds = list.stream().map(ChoProductReqVo::getCorpId).collect(Collectors.toList());
        List<CorpDTO> corpDTOS = corpService.listCorpDTOByCorpIds(corpIds);
        Map<String, CorpDTO> collect = Maps.newHashMap();
        if (corpDTOS != null || !corpDTOS.isEmpty()) {
            collect = corpDTOS.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity(), (key1, key2) -> key2));
        }
        for (ChoProductReqVo vo : list) {
            setBeanMoneyType(vo, moneyUint);
            injectValues(listTypeMap, listPayWayMap, vo);
            if (vo.getCorpName() == null) {
                vo.setCorpName(collect.get(vo.getCorpId()).getName());
            }

        }
        output.setData(list);
        return output;
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

    public ChoProductReqVo getChoProductReqVoById(String id) {
        ChoProductReqVo reqVo = choProductReqDao.getChoProductReqVoById(id);
        Optional.ofNullable(reqVo).ifPresent(req -> {
            // 注入远程信息
            Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
            Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
            injectValues(listTypeMap, listPayWayMap, reqVo);
        });
        return reqVo;
    }

    /**
     * 更新登记的产品的最新一次赎回记录的状态
     *
     * @param id
     * @param redeemStatus
     */
    public void updateRedeemStatus(String id, Integer redeemStatus) {
        ChoProductReq entity = choProductReqDao.get(id);
        entity.setRedeemStatus(redeemStatus);
        choProductReqDao.update(entity);
    }

    /**
     * 更新理财登记表的业务状态
     *
     * @param id        登记id
     * @param bizStatus 业务状态
     */
    public void updateBizStatus(String id, Integer bizStatus) {
        ChoProductReq entity = choProductReqDao.get(id);
        entity.setBizStatus(bizStatus);
        choProductReqDao.update(entity);
    }

    /**
     * 更新理财登记表的业务状态
     *
     * @param choProductReq 登记实体
     */
    public void updateChoProductReq(ChoProductReq choProductReq) {
        choProductReqDao.updateChoProductReq(choProductReq);
    }

    /**
     * 批量提交记录
     *
     * @param ids
     * @param
     */
    public void batchSubmit(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            ChoProductReq entity = this.get(id);
            entity.setStatus(11);
            entity.setBatchSubmit(true);
            this.toModify(entity, user);
        }
    }

    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(ChoWorkFlowConstant.CHO_PRODUCT_REQ, dataTablesInput);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "pr.id", true);
        }
        DataTablesOutput<ChoProductReqVo> dp = this.choProductReqDao.dataTableApprove(dataTablesInput, businessSql);
        String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
        if (EmptyUtils.isEmpty(moneyUint)) {
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        // 注入远程信息
        Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
        Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
        List<ChoProductReqVo> list = dp.getData();
        for (ChoProductReqVo vo : list) {
            setBeanMoneyType(vo, moneyUint);
            injectValues(listTypeMap, listPayWayMap, vo);
        }
        dp.setData(list);
        return workFlowBusiness.merge(dp);
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        ChoProductReq entity = this.get(workFlowResult.getBusinessKey(), true);
        Integer messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        boolean isPass = workFlowResult.isPass();
        entity.setBizStatus(isPass ? ChoProductReqConstant.BIZ_STATUS4 : ChoProductReqConstant.BIZ_STATUS3);
        if (isPass) {
            //审批通过
            messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            entity.setStatus(ChoProductReqConstant.STATUE_CHECK);
            // 不设置审批流，自动审批通过时，不刷新则无法获取到新增的ChoProductReq
            this.getSession().flush();
            this.getSession().clear();
            // 审批通过后，如果产品类型为无效则修改为有效
            ChoProductVo choProductVo = choProductService.getByCode(entity.getProductCode());
            if (CommonConstant.VALID_SIGN_NO.equals(choProductVo.getValidSign())) {
                ChoProduct choProduct = choProductService.get(choProductVo.getId(), true);
                choProduct.setValidSign(CommonConstant.VALID_SIGN_YES);
                choProduct.setIsApprove(CommonConstant.VALID_SIGN_YES);
                choProductService.updateEntity(choProduct);
            }
            ChoProductReqVo choProductReqVo = this.getChoProductReqVoById(entity.getId());// 不设置审批流，自动审批通过时，不刷新则无法获取到新增的ChoProductReq
            this.getSession().flush();
            this.getSession().clear();
            // 非保本浮动型,且维护日净值
            if (ChoProductReqConstant.FINANCIAL_TYPE2.equals(choProductReqVo.getFinancialType())) {
                // 新增净值维护数据
                ChoProductNetVal choProductNetVal = new ChoProductNetVal();
                choProductNetVal.setReqId(entity.getId());
                choProductNetVal.setNetValDate(entity.getBuyDate());
                choProductNetVal.setBuyMoney(entity.getBuyMoney());
                choProductNetVal.setBuyPortion(entity.getPortion());
                choProductNetVal.setDailyNetVal(entity.getDailyNetVal());
                choProductNetVal.setBal(entity.getBuyMoney());
                BigDecimal zero = new BigDecimal(0);
                choProductNetVal.setTenThousandIncome(zero);
                choProductNetVal.setActualIncome(zero);
                choProductNetVal.setDayIncome(zero);
                choProductNetVal.setCreateDate(new Date());
                choProductNetVal.setCreateBy(user.getLoginName());
                choProductNetValService.save(choProductNetVal);
            }
        } else {
            //审批打回
            entity.setStatus(ChoProductReqConstant.STATUE_BACK);
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //生成日志
        choMessageService.saveMessage(entity.getBillNo(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ, user);
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }

    /**
     * 金额单位转换
     *
     * @param ratVO
     * @param moneyType
     */
    public void setBeanMoneyType(ChoProductReqVo ratVO, String moneyType) {
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
                        f.set(ratVO, value.divide(BigDecimal.valueOf(Integer.parseInt(moneyType)), 6, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataTablesOutput getDataTablesValidSign(DataTablesInput dataTablesInput, Date deadlineDate, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTREDEEM_CHOPRODUCTREDEEM_REQQUERY, user.getId()), "pr.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        // 判断是否查询的是理财续期数据
        DataTablesOutput out = StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("isRenewal")) ?
                choProductRenewalService.getDataTablesUnion(dataTablesInput, user) :
                choProductReqDao.getDataTablesValidSign(dataTablesInput, deadlineDate, sql.toString());
        if (EmptyUtils.isEmpty(out)) {
            return out;
        }
        List<ChoProductReqVo> choProductReqVoList = out.getData();
        if (EmptyUtils.isEmpty(choProductReqVoList)) {
            return out;
        }
        // 注入远程信息
        Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
        Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
        Set<String> reqIds = Sets.newHashSet();
        for (ChoProductReqVo choProductReqVo : choProductReqVoList) {
            reqIds.add(choProductReqVo.getId());
            injectValues(listTypeMap, listPayWayMap, choProductReqVo);
        }
        List<ChoProductRedeem> productRedeems = choProductRedeemService.listRedeemedApprovedByReqIds(reqIds);
        Map<String, BigDecimal> reqId2redeemedMoney = Maps.newHashMap();
        if (EmptyUtils.isNotEmpty(productRedeems)) {
            for (ChoProductRedeem choProductRedeem : productRedeems) {
                reqId2redeemedMoney.put(choProductRedeem.getReqId(), choProductRedeem.getRedeemMoney());
            }
        }
        for (ChoProductReqVo choProductReqVo : choProductReqVoList) {
            BigDecimal redeemedMoney = reqId2redeemedMoney.get(choProductReqVo.getId()) == null ? BigDecimal.ZERO : reqId2redeemedMoney.get(choProductReqVo.getId());
            BigDecimal waitRedeemMoney = choProductReqVo.getBuyMoney().subtract(redeemedMoney);
            choProductReqVo.setRedeemedMoney(redeemedMoney == null ? BigDecimal.ZERO : redeemedMoney);
            choProductReqVo.setWaitRedeemMoney(waitRedeemMoney);
        }
        return out;
    }

    /**
     * 撤回
     *
     * @param workFlowResult
     */
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        ChoProductReq entity = this.get(workFlowResult.getBusinessKey(), true);
        entity.setStatus(ChoProductReqConstant.STATUE_TEMP);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //生成日志
        choMessageService.saveMessage(entity.getBillNo(), ChoMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ, user);
    }

    /**
     * 获取所有审批通过的产品登记记录
     *
     * @return
     */
    public List<ChoProductReqVo> listChoProducted(List<String> corpIds, ChoProductDetailedVo detailedVo) {
        return choProductReqDao.listChoProducted(corpIds, detailedVo);
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有理财登记的记录，不允许删除
            int num = this.choProductReqDao.getProductReqCountByCorpId(corpId, "del");
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_PRODUCTREQ,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DEL_PRODUCTREQ_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的理财登记单据，不允许禁用
            int num = this.choProductReqDao.getProductReqCountByCorpId(corpId, "disable");
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_PRODUCTREQ,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_PRODUCTREQ_ERROR)));
            }
            //单位有未全部赎回的理财登记单据，不允许禁用
            num = this.choProductReqDao.getProductReqRedeemCountByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_PRODUCTREQ,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_PRODUCTREQ_REDEEM_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }

    /**
     * 下载理财登记模板
     *
     * @param request  requst
     * @param response response
     * @param vo       前端传参
     * @param model    model
     * @param user     用户
     **/
    public void downloadReqModel(HttpServletRequest request, HttpServletResponse response,
                                 ChoProductReqVo vo, Model model, ICurrentUser user) throws IOException {
        String financialType = vo.getFinancialType();
        // 导出的文件名
        String fileName = ChoProductReqConstant.FINANCIAL_TYPE0.equals(financialType) ? "保本收益型.xls" :
                ChoProductReqConstant.FINANCIAL_TYPE1.equals(financialType) ? "保本浮动收益型.xls" : "非保本浮动收益型.xls";
        //判断浏览器类型
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        //导出数据
        HSSFWorkbook workbook = createUnitCountExcelContent(vo, model, user);
        //设置导出流
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 创建理财登记模板
     *
     * @param vo    登记对象
     * @param model model
     * @param user  用户
     * @return 导出对象
     */
    public HSSFWorkbook createUnitCountExcelContent(ChoProductReqVo vo, Model model, ICurrentUser user) {
        String financialType = vo.getFinancialType();
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 根据理财类型创建对应模板
        this.createExcelModel(workbook, financialType);
        // 创建指引工作表
        this.createHelpSheet(workbook, financialType);
        return workbook;
    }

    /**
     * 创建指引工作表
     *
     * @param workbook      excel
     * @param financialType 理财类型
     **/
    private void createExcelModel(HSSFWorkbook workbook, String financialType) {
        String content = ChoProductReqConstant.FINANCIAL_TYPE0.equals(financialType) ? "保本收益型" :
                ChoProductReqConstant.FINANCIAL_TYPE1.equals(financialType) ? "保本浮动收益型" : "非保本浮动收益型";
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet(content);
        // 创建表头
        HSSFRow rowHead = sheet.createRow(0);
        //设置单元格样式
        HSSFCellStyle cellStyleHead = workbook.createCellStyle();
        //定义字体
        HSSFFont redFont = workbook.createFont();
        redFont.setColor(HSSFColor.RED.index);
        //设置单元格颜色
        cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
        cellStyleHead.setFillPattern(FillPatternType.forInt(1));
        // 创建列名
        createHeadCell(rowHead, cellStyleHead, redFont, "*单位", 0, true);
        HSSFCell cell1 = createHeadCell(rowHead, cellStyleHead, redFont, "*产品代码", 1, true);
        createHeadCell(rowHead, cellStyleHead, redFont, "*币别", 2, true);
        if (ChoProductReqConstant.FINANCIAL_TYPE0.equals(financialType)) {
            createHeadCell(rowHead, cellStyleHead, redFont, "理财账号", 3, false);
        } else {
            createHeadCell(rowHead, cellStyleHead, redFont, "*理财账号", 3, true);
        }
        createHeadCell(rowHead, cellStyleHead, redFont, "*购买金额", 4, true);
        HSSFCell cell5 = createHeadCell(rowHead, cellStyleHead, redFont, "*收益条款", 5, true);
        createHeadCell(rowHead, cellStyleHead, redFont, "*购买日期", 6, true);
        createHeadCell(rowHead, cellStyleHead, redFont, "*起息日期", 7, true);
        createHeadCell(rowHead, cellStyleHead, redFont, "兑付日期", 9, false);
        createHeadCell(rowHead, cellStyleHead, redFont, "申购费", 10, false);
        createHeadCell(rowHead, cellStyleHead, redFont, "其他手续费", 11, false);
        HSSFCell cell12 = createHeadCell(rowHead, cellStyleHead, redFont, "资金来源", 12, false);
        HSSFCell cell13 = createHeadCell(rowHead, cellStyleHead, redFont, "是否提前赎回", 13, false);
        createHeadCell(rowHead, cellStyleHead, redFont, "提前回购条约", 14, false);
        createHeadCell(rowHead, cellStyleHead, redFont, "备注", 15, false);
        // 添加单元格注释
        // 创建HSSFPatriarch对象,HSSFPatriarch是所有注释的容器.
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置
        HSSFFont commentFormatter = workbook.createFont();
        commentFormatter.setFontName("宋体");
        commentFormatter.setFontHeightInPoints((short) 9);
        // 设置注释内容
        addCellComment(patriarch, commentFormatter, cell1, "请根据《指引》查看产品代码");
        addCellComment(patriarch, commentFormatter, cell5, "360天收益\n365天收益");
        addCellComment(patriarch, commentFormatter, cell12, "自有资金\n募集资金\n专项贷款\n虚拟账户\n银行代扣");
        addCellComment(patriarch, commentFormatter, cell13, "是/否");
        // 保本浮动收益型
        if (ChoProductReqConstant.FINANCIAL_TYPE1.equals(financialType)) {
            createHeadCell(rowHead, cellStyleHead, redFont, "*到期日期", 8, true);
            HSSFCell cell16 = createHeadCell(rowHead, cellStyleHead, redFont, "*参考收益率", 16, true);
            createHeadCell(rowHead, cellStyleHead, redFont, "参考收益", 17, false);
            addCellComment(patriarch, commentFormatter, cell16, "0.1-0.3");
        } else if (ChoProductReqConstant.FINANCIAL_TYPE0.equals(financialType)) {
            // 保本收益型
            createHeadCell(rowHead, cellStyleHead, redFont, "*到期日期", 8, true);
            HSSFCell cell16 = createHeadCell(rowHead, cellStyleHead, redFont, "到期是否赎回", 16, false);
            addCellComment(patriarch, commentFormatter, cell16, "是/否");
            HSSFCell cell17 = createHeadCell(rowHead, cellStyleHead, redFont, "*参考收益率", 17, true);
            HSSFCell cell18 = createHeadCell(rowHead, cellStyleHead, redFont, "*付息方式", 18, true);
            HSSFCell cell19 = createHeadCell(rowHead, cellStyleHead, redFont, "*收益类型", 19, true);
            createHeadCell(rowHead, cellStyleHead, redFont, "合同号", 20, false);
            HSSFCell cell20 = createHeadCell(rowHead, cellStyleHead, redFont, "*合同利率", 21, true);
            addCellComment(patriarch, commentFormatter, cell18, "一次性付息\n分段付息\n同步在赎回\n");
            addCellComment(patriarch, commentFormatter, cell19, "保本收益型\n保本浮动收益型\n非保本浮动收益型\n");
            addCellComment(patriarch, commentFormatter, cell20, "10%-50%");
            addCellComment(patriarch, commentFormatter, cell17, "0.1-0.3");
        } else {
            // 非保本浮动收益型
            createHeadCell(rowHead, cellStyleHead, redFont, "到期日期", 8, false);
            createHeadCell(rowHead, cellStyleHead, redFont, "*购买日净值", 16, true);
            createHeadCell(rowHead, cellStyleHead, redFont, "份额", 17, false);
            HSSFCell cell18 = createHeadCell(rowHead, cellStyleHead, redFont, "*收益类型", 18, true);
            createHeadCell(rowHead, cellStyleHead, redFont, "合同号", 19, false);
            HSSFCell cell20 = createHeadCell(rowHead, cellStyleHead, redFont, "合同利率", 20, false);
            addCellComment(patriarch, commentFormatter, cell18, "保本收益型\n保本浮动收益型\n非保本浮动收益型\n");
            addCellComment(patriarch, commentFormatter, cell20, "10%-50%");
            createHeadCell(rowHead, cellStyleHead, redFont, "是否维护日净值", 21, false);
        }
        // 文本格式化
        HSSFCellStyle textStyle = workbook.createCellStyle();
        textStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));
        // 金额格式化
        HSSFCellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setAlignment(HorizontalAlignment.RIGHT);
        moneyStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        // 百分比格式化
        HSSFCellStyle percentStyle = workbook.createCellStyle();
        percentStyle.setAlignment(HorizontalAlignment.RIGHT);
        percentStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
        // 日期格式化
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        HSSFDataFormat format = workbook.createDataFormat();
        dateStyle.setDataFormat(format.getFormat("yyyy-m-d"));
        // 保留六位小数数据格式化
        HSSFCellStyle sixPointStyle = workbook.createCellStyle();
        percentStyle.setAlignment(HorizontalAlignment.RIGHT);
        percentStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.000000"));
        // 创建一行模板数据
        HSSFRow modelRow = sheet.createRow(1);
        createBodyCell(0, "测试单位", textStyle, modelRow);
        createBodyCell(1, "1001", textStyle, modelRow);
        createBodyCell(2, "人民币", textStyle, modelRow);
        createBodyCell(3, "1231313131313", textStyle, modelRow);
        createBodyCell(4, "100.00", moneyStyle, modelRow);
        createBodyCell(5, "360天收益", textStyle, modelRow);
        createBodyCell(6, "2022-1-1", dateStyle, modelRow);
        createBodyCell(7, "2022-1-1", dateStyle, modelRow);
        createBodyCell(8, "2022-1-28", dateStyle, modelRow);
        createBodyCell(9, null, dateStyle, modelRow);
        createBodyCell(10, null, moneyStyle, modelRow);
        createBodyCell(11, null, moneyStyle, modelRow);
        createBodyCell(12, null, textStyle, modelRow);
        createBodyCell(13, null, textStyle, modelRow);
        createBodyCell(14, null, textStyle, modelRow);
        createBodyCell(15, null, textStyle, modelRow);
        // 保本浮动收益型
        if (ChoProductReqConstant.FINANCIAL_TYPE1.equals(financialType)) {
            createBodyCell(16, "0.1-0.3", percentStyle, modelRow);
            createBodyCell(17, null, moneyStyle, modelRow);
        } else if (ChoProductReqConstant.FINANCIAL_TYPE0.equals(financialType)) {
            // 保本收益型
            createBodyCell(16, "否", percentStyle, modelRow);
            createBodyCell(17, "0.1-0.3", percentStyle, modelRow);
            createBodyCell(18, null, percentStyle, modelRow);
            createBodyCell(19, null, textStyle, modelRow);
            createBodyCell(20, null, textStyle, modelRow);
            createBodyCell(21, null, textStyle, modelRow);
        } else {
            createBodyCell(16, "1.0084", sixPointStyle, modelRow);
            createBodyCell(17, null, sixPointStyle, modelRow);
            createBodyCell(18, null, textStyle, modelRow);
            createBodyCell(19, null, textStyle, modelRow);
            createBodyCell(20, null, textStyle, modelRow);
            createBodyCell(21, "是", textStyle, modelRow);
        }
        // 设置每列单元格宽度
        for (int i = 0; i < 20; i++) {
            sheet.setColumnWidth(i, 12 * 256);
        }
    }

    /**
     * 根据样式、值创建单元格
     *
     * @param index    下标
     * @param val      值
     * @param style    样式
     * @param modelRow 行
     */
    private void createBodyCell(int index, Object val, HSSFCellStyle style, HSSFRow modelRow) {
        HSSFCell modelCell = modelRow.createCell(index);
        modelCell.setCellStyle(style);
        if (val != null) {
            modelCell.setCellValue((String) val);
        }
    }

    /**
     * 添加单元格注释
     *
     * @param patriarch        注释容器
     * @param commentFormatter 注释字体
     * @param cell             单元格
     * @param content          注释内容
     **/
    private void addCellComment(HSSFPatriarch patriarch, HSSFFont commentFormatter, HSSFCell cell, String content) {
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 7, 7));
        HSSFRichTextString rtf = new HSSFRichTextString(content);
        rtf.applyFont(commentFormatter);
        comment.setString(rtf);
        // 设置注释内容
        cell.setCellComment(comment);
    }

    /**
     * 创建表头
     *
     * @param rowHead       row
     * @param cellStyleHead 表头样式
     * @param font          字体
     * @param content       文本内容
     * @param index         索引
     * @param isNeed        是否必填
     * @return 单元格对象
     **/
    private HSSFCell createHeadCell(HSSFRow rowHead, HSSFCellStyle cellStyleHead, HSSFFont font, String content, int index, boolean isNeed) {
        HSSFCell hssfCell = rowHead.createCell(index);
        HSSFRichTextString richString = new HSSFRichTextString(content);
        if (isNeed) {
            richString.applyFont(0, 1, font);
        }
        hssfCell.setCellStyle(cellStyleHead);
        hssfCell.setCellValue(richString);
        return hssfCell;
    }

    /**
     * 创建指引工作表
     *
     * @param workbook      excel
     * @param financialType 理财类型
     **/
    private void createHelpSheet(HSSFWorkbook workbook, String financialType) {
        List<ChoProductVo> choProductVoList = choProductService.listValidSignAll(financialType);
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet("指引");
        // 创建行
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle cellStyle = this.createExcelBodyStyle(workbook);
        // 创建列名
        row.createCell(0).setCellValue("产品代码");
        row.createCell(1).setCellValue("产品类型");
        row.createCell(2).setCellValue("产品名称");
        row.createCell(3).setCellValue("银行类别");
        row.setRowStyle(cellStyle);
        // 创建表格数据
        for (int i = 0; i < choProductVoList.size(); i++) {
            HSSFRow hssfRow = sheet.createRow(i + 1);
            ChoProductVo choProductVo = choProductVoList.get(i);
            hssfRow.createCell(0).setCellValue(choProductVo.getProductCode());
            hssfRow.createCell(1).setCellValue(choProductVo.getFinancialTypeName());
            hssfRow.createCell(2).setCellValue(choProductVo.getProductName());
            hssfRow.createCell(3).setCellValue(choProductVo.getBankTypeName());
            hssfRow.setRowStyle(cellStyle);
        }
        //调整宽度
        sheet.autoSizeColumn((short) 0);
        sheet.autoSizeColumn((short) 1);
        sheet.autoSizeColumn((short) 2);
        sheet.autoSizeColumn((short) 3);
    }

    /**
     * 创建Excel内容样式
     *
     * @param workbook 工作表对象
     * @return Excel内容样式
     */
    private HSSFCellStyle createExcelBodyStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cs = workbook.createCellStyle();
        //垂直居中对齐
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        //水平居中对齐
        cs.setAlignment(HorizontalAlignment.CENTER);
        return cs;
    }

    /**
     * 导入
     *
     * @param list 校验后的登记集合
     * @param user 用户
     * @return 是否导入成功
     */
    public AjaxJson saveImport(List<ChoProductReqImportVo> list, ICurrentUser user) {
        if (!CollectionUtils.isEmpty(list)) {
            for (ChoProductReqImportVo importVo : list) {
                String isMoveUp = importVo.getIsMoveUp();
                if (StringUtils.isEmpty(isMoveUp) || "是".equals(isMoveUp)) {
                    importVo.setIsMoveUp("1");
                } else {
                    importVo.setIsMoveUp("0");
                }
                String isStore = importVo.getIsStore();
                if (StringUtils.isEmpty(isStore) || "是".equals(isStore)) {
                    importVo.setIsStore("1");
                } else {
                    importVo.setIsStore("0");
                }
                String isMaintainVal = importVo.getIsMaintainVal();
                if (StringUtils.isEmpty(isMaintainVal) || "是".equals(isMaintainVal)) {
                    importVo.setIsMaintainVal("1");
                } else {
                    importVo.setIsMaintainVal("0");
                }
                //如果参考收益率大于1,将参考收益率除以100转成小数
                if (importVo.getReferIncomeRate().compareTo(new BigDecimal(1)) > 0) {//参考收益率
                    importVo.setReferIncomeRate(importVo.getReferIncomeRate().divide(new BigDecimal(100)));
                    importVo.setReferIncomeMoney(importVo.getReferIncomeMoney().divide(new BigDecimal(100)));
                }
                saveChoProductReq(BeanUtils.copyNewInstanceProperties(importVo, ChoProductReq.class), user);
            }
        }
        return new AjaxJson(true);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(ChoProductReqImportVo obj) {
        return null;
    }

    @Override
    public void verifyHandler(List<ChoProductReqImportVo> list) {
        if (!CollectionUtils.isEmpty(list)) {
            List<String> errorMsgList;
            // 付息方式字典
            Map<String, DictDTO> payWayDict = dictService.listMap("financial.payWay");
            // 资金来源字典
            Map<String, DictDTO> capitalSourceDict = dictService.listMap("choProduct.capitalSource");
            // 收益条款字典
            Map<String, DictDTO> incomeClauseDict = dictService.listMap("choProduct.incomeClause");
            // 理财类型（收益类型）字典
            Map<String, DictDTO> financialTypeDict = dictService.listMap("financial.type");
            for (ChoProductReqImportVo importVo : list) {
                errorMsgList = new ArrayList<>();
                // 单位名称校验
                String corpName = importVo.getCorpName();
                if (StringUtils.isEmpty(corpName)) {
                    errorMsgList.add("单位名称不能为空");
                } else {
                    CorpDTO corpDTO = corpService.getCorpIdByName(corpName);
                    if (corpDTO == null) {
                        errorMsgList.add("单位名称不存在");
                    } else {
                        importVo.setCorpId(corpDTO.getId());
                    }
                }
                // 产品代码校验
                ChoProduct choProduct = null;
                String productCode = importVo.getProductCode();
                if (StringUtils.isEmpty(productCode)) {
                    errorMsgList.add("产品代码不能为空");
                } else {
                    choProduct = choProductService.findProductByCode(productCode);
                    if (choProduct == null) {
                        errorMsgList.add("产品代码不存在");
                    } else {
                        importVo.setFinancialType(choProduct.getFinancialType());
                        importVo.setProductName(choProduct.getProductName());
                        importVo.setProductType(choProduct.getProductType());
                        importVo.setBankTypeId(choProduct.getBankTypeId());
                    }
                }
                // 币别校验
                String curName = importVo.getCurName();
                if (StringUtils.isEmpty(curName)) {
                    errorMsgList.add("币别不能为空");
                } else {
                    String curId = currencyService.getCurrencyByName(curName);
                    if (StringUtils.isEmpty(curId)) {
                        errorMsgList.add("币别不存在");
                    } else {
                        importVo.setCurId(curId);
                    }
                }
                // 理财账号校验
                if (choProduct != null) {
                    //保本收益型 理财账号非必填
                    if (ChoProductReqConstant.FINANCIAL_TYPE0.equals(choProduct.getFinancialType())) {
                        String bankAcc = importVo.getBankAccBreak();
                        if (StringUtils.isNotEmpty(bankAcc)) {
                            BtBankAccDTO bankAccInfo = bankAccService.getBankAccInfo(bankAcc);
                            if (bankAccInfo == null) {
                                errorMsgList.add("理财账号不存在");
                            } else {
                                importVo.setBankAcc(bankAcc);
                                importVo.setBankTypeId(bankAccInfo.getBankTypeId());
                                importVo.setBankInfoId(bankAccInfo.getBankCode());
                                importVo.setBankName(bankAccInfo.getBankName());
                            }
                        }
                    } else {
                        String bankAcc = importVo.getBankAcc();
                        if (StringUtils.isEmpty(bankAcc)) {
                            errorMsgList.add("理财账号不能为空");
                        } else {
                            BtBankAccDTO bankAccInfo = bankAccService.getBankAccInfo(bankAcc);
                            if (bankAccInfo == null) {
                                errorMsgList.add("理财账号不存在");
                            } else {
                                importVo.setBankTypeId(bankAccInfo.getBankTypeId());
                                importVo.setBankInfoId(bankAccInfo.getBankCode());
                                importVo.setBankName(bankAccInfo.getBankName());
                            }
                        }
                    }
                }
                // 购买金额校验
                if (importVo.getBuyMoney() == null) {
                    errorMsgList.add("购买金额不能为空");
                }
                // 收益条款校验
                String incomeClauseName = importVo.getIncomeClauseName();
                if (StringUtils.isEmpty(incomeClauseName)) {
                    errorMsgList.add("收益条款不能为空");
                } else {
                    if (incomeClauseDict.containsKey(incomeClauseName)) {
                        importVo.setIncomeClause(Integer.valueOf(incomeClauseDict.get(incomeClauseName).getCode()));
                    } else {
                        errorMsgList.add("收益条款不存在");
                    }
                }
                // 日期校验
                Date buyDate = importVo.getBuyDate();
                if (buyDate == null) {
                    errorMsgList.add("购买日期不能为空");
                }
                Date interestStartDate = importVo.getInterestStartDate();
                if (interestStartDate == null) {
                    errorMsgList.add("起息日期不能为空");
                }
                Date deadlineDate = importVo.getDeadlineDate();
                // 非保本浮动收益型到期日期不必填
                String financialType = importVo.getFinancialType();
                if (deadlineDate == null && (ChoProductReqConstant.FINANCIAL_TYPE1.equals(financialType)
                        || ChoProductReqConstant.FINANCIAL_TYPE0.equals(financialType))) {
                    errorMsgList.add("到期日期不能为空");
                }
                if (buyDate != null && interestStartDate != null) {
                    if (interestStartDate.before(buyDate)) {
                        errorMsgList.add("起息日期必须大于等于购买日期");
                    }
                    if (deadlineDate != null && deadlineDate.before(interestStartDate)) {
                        errorMsgList.add("到期日期必须大于等于起息日期");
                    }
                }
                // 资金来源校验
                String capitalSourceName = importVo.getCapitalSourceName();
                if (StringUtils.isNotEmpty(capitalSourceName)) {
                    if (capitalSourceDict.containsKey(capitalSourceName)) {
                        importVo.setCapitalSource(Integer.valueOf(capitalSourceDict.get(capitalSourceName).getCode()));
                    } else {
                        errorMsgList.add("资金来源不存在");
                    }
                }
                // 理财类型校验
                // 保本浮动收益型不需要填写收益类型
                if ((ChoProductReqConstant.FINANCIAL_TYPE2.equals(financialType)
                        || ChoProductReqConstant.FINANCIAL_TYPE0.equals(financialType))) {
                    // 收益类型校验
                    String incomeTypeName = importVo.getIncomeTypeName();
                    if (incomeTypeName == null) {
                        errorMsgList.add("收益类型不能为空");
                    }
                    if (financialTypeDict.containsKey(incomeTypeName)) {
                        importVo.setIncomeType(financialTypeDict.get(incomeTypeName).getCode());
                    } else {
                        errorMsgList.add("收益类型不存在");
                    }
                }
                verifyWithFinancialType(errorMsgList, payWayDict, importVo, financialType);
                if ((ChoProductReqConstant.FINANCIAL_TYPE0.equals(financialType)
                        || ChoProductReqConstant.FINANCIAL_TYPE1.equals(financialType))) {
                    // 参考收益率校验
                    String referIncomeRateRange = importVo.getReferIncomeRateRange();

                    if (StringUtils.isEmpty(referIncomeRateRange)) {
                        errorMsgList.add("参考收益率不能为空");
                    } else {
                        String[] referIncomeRateRangeArr = referIncomeRateRange.split("-");
                        if (referIncomeRateRangeArr.length < 2) {
                            errorMsgList.add("参考收益率格式错误");
                        } else {
                            BigDecimal referIncomeRate = BigDecimal.valueOf(Double.parseDouble(referIncomeRateRangeArr[0]));
                            BigDecimal endReferIncomeRate = BigDecimal.valueOf(Double.parseDouble(referIncomeRateRangeArr[1]));
                            importVo.setReferIncomeRate(referIncomeRate);
                            importVo.setEndReferIncomeRate(endReferIncomeRate);
                            // 计算参考收益
                            BigDecimal buyMoney = importVo.getBuyMoney();
                            Integer incomeClause = importVo.getIncomeClause();
                            if (interestStartDate != null && deadlineDate != null && buyMoney != null && incomeClause != null) {
                                // 期限
                                int dayLimit = (int) ((deadlineDate.getTime() - interestStartDate.getTime()) / (1000 * 60 * 60 * 24));
                                // 参考收益
                                BigDecimal referIncomeMoney = referIncomeRate.multiply(buyMoney)
                                        .multiply(new BigDecimal(dayLimit))
                                        .divide(new BigDecimal(incomeClause), 2, BigDecimal.ROUND_HALF_UP);
                                BigDecimal endReferIncomeMoney = endReferIncomeRate.multiply(buyMoney)
                                        .multiply(new BigDecimal(dayLimit))
                                        .divide(new BigDecimal(incomeClause), 2, BigDecimal.ROUND_HALF_UP);
                                importVo.setDayLimit(dayLimit);
                                importVo.setReferIncomeMoney(referIncomeMoney);
                                importVo.setEndReferIncomeMoney(endReferIncomeMoney);
                            }
                        }
                    }
                }
                // 是否提前赎回校验
                String isMoveUp = importVo.getIsMoveUp();
                if (StringUtils.isNotEmpty(isMoveUp) && !"是".equals(isMoveUp) && !"否".equals(isMoveUp)) {
                    errorMsgList.add("是否提前赎回只能填写是或否");
                }
                // 到期是否续存
                String isStore = importVo.getIsStore();
                if (StringUtils.isNotEmpty(isStore) && !"是".equals(isStore) && !"否".equals(isStore)) {
                    errorMsgList.add("到期是否续存只能填写是或否");
                } else {
                    importVo.setIsStore("否");
                }
                // 是否维护日净值校验
                String isMaintainVal = importVo.getIsMaintainVal();
                if (StringUtils.isNotEmpty(isMaintainVal) && !"是".equals(isMaintainVal) && !"否".equals(isMaintainVal)) {
                    errorMsgList.add("是否维护日净值填写是或否");
                }
                // 设置默认值
                // 状态为暂存
                importVo.setStatus(ChoProductReqConstant.STATUE_TEMP);
                importVo.addErrorMsg(errorMsgList);
            }
        }
    }

    /**
     * 根据理财类型进行校验
     *
     * @param errorMsgList  错误集合
     * @param payWayDict    付息方式字典
     * @param importVo      导入vo
     * @param financialType 理财类型
     */
    private void verifyWithFinancialType(List<String> errorMsgList, Map<String, DictDTO> payWayDict,
                                         ChoProductReqImportVo importVo, String financialType) {
        if (ChoProductReqConstant.FINANCIAL_TYPE0.equals(financialType)) {
            // 付息方式校验
            String payWayName = importVo.getPayWayName();
            if (StringUtils.isEmpty(payWayName)) {
                errorMsgList.add("付息方式不能为空");
            } else {
                DictDTO dictDTO = payWayDict.get(payWayName);
                if (dictDTO == null) {
                    errorMsgList.add("付息方式不存在");
                } else {
                    importVo.setPayWay(dictDTO.getCode());
                }
            }
            // 合同利率校验
            String contractRate = importVo.getContractRate();
            if (StringUtils.isEmpty(contractRate)) {
                errorMsgList.add("合同利率不能为空");
            } else {
                transferContractRate(errorMsgList, importVo, contractRate);
            }
        } else if (ChoProductReqConstant.FINANCIAL_TYPE2.equals(financialType)) {
            // 购买日净值校验
            BigDecimal dailyNetVal = importVo.getDailyNetVal();
            if (dailyNetVal == null) {
                errorMsgList.add("购买日净值不能为空");
            } else {
                // 计算份额
                BigDecimal buyMoney = importVo.getBuyMoney();
                if (buyMoney != null) {
                    importVo.setPortion(buyMoney.divide(dailyNetVal, 6, BigDecimal.ROUND_HALF_UP));
                }
            }
            // 合同利率校验
            String contractRate = importVo.getNonContractRate();
            if (StringUtils.isNotEmpty(contractRate)) {
                transferContractRate(errorMsgList, importVo, contractRate);
            }
            // 到期日期
            importVo.setDeadlineDate(importVo.getNonDeadlineDate());
        }
    }

    /**
     * 合同利率转化处理
     *
     * @param errorMsgList 错误信息集合
     * @param importVo     导入vo
     * @param contractRate 导入的合同利率
     */
    private void transferContractRate(List<String> errorMsgList, ChoProductReqImportVo importVo, String contractRate) {
        String[] contractRateArr = contractRate.split("-");
        if (contractRateArr.length != 2) {
            errorMsgList.add("合同利率格式错误");
        } else {
            String startStr = contractRateArr[0].substring(0, contractRateArr[0].length() - 1);
            String endStr = contractRateArr[1].substring(0, contractRateArr[1].length() - 1);
            importVo.setStartContractRate(BigDecimal.valueOf(Double.parseDouble(startStr) / 100));
            importVo.setEndContractRate(BigDecimal.valueOf(Double.parseDouble(endStr) / 100));
        }
    }

    /**
     * 根据单位id，到期日期获取理财产品信息
     *
     * @param corpId
     * @param deadlineDate
     * @return
     */
    public List<ChoProductReqDTO> listChoProductReqByDate(String corpId, Date deadlineDate) {
        return choProductReqDao.listChoProductReqByDate(corpId, deadlineDate);
    }

    /**
     * 更新对象附件ID
     *
     * @param entity
     * @param user
     */
    public void modifyAttachmentId(ChoProductReq entity, ICurrentUser user) {
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
    }

    public List<CorpDTO> getFilterBySource(String corpVOList) {
        return corpService.getFilterBySource(corpVOList, this.getCorpIds());
    }

    /**
     * 单位id列表-根据理财产品登记表中资金来源获取
     *
     * @return
     */
    public List<String> getCorpIds() {
        return choProductReqDao.getCorpIds();
    }

    /**
     * 银行类型维护列表-根据理财产品登记表中银行类型获取
     *
     * @return
     */
    public List<BankTypeDTO> getBankTypeJson() {
        return choProductReqDao.getBankTypeJson();
    }

    /**
     * 购买银行列表-根据理财产品登记表中购买银行获取，可根据银行类型联动查询
     *
     * @return
     */
    public List<ChoProductReqVo> getBankInfoJson(String bankTypeIds) {
        String bankTypeIdsSql = "";
        if (EmptyUtils.isNotEmpty(bankTypeIds)) {
            String[] bankTypeIdsS = bankTypeIds.split(",");
            if (bankTypeIdsS.length > 0) {
                List<String> bankTypeIdList = new ArrayList<String>(bankTypeIdsS.length);
                Collections.addAll(bankTypeIdList, bankTypeIdsS);
                bankTypeIdsSql = SqlUtil.getInSql(bankTypeIdList, "cpr.BANK_TYPE_ID", true);
            }
        }
        return choProductReqDao.getBankInfoJson(bankTypeIdsSql);
    }

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
