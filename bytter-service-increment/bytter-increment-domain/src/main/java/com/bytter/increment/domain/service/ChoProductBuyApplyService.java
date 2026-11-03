package com.bytter.increment.domain.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.api.constant.ChoWorkFlowConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.api.enums.ChoProductMessageEnum;
import com.bytter.increment.domain.dao.ChoProductBuyApplyDao;
import com.bytter.increment.domain.entity.ChoProduct;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.increment.domain.vo.ChoProductVo;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * 理财产品购买申请Service
 *
 * @author ouyj
 */
@WorkFlow(ChoWorkFlowConstant.CHO_PRODUCT_BUY_APPLY)
@Service
@Transactional(rollbackFor = Exception.class)
public class ChoProductBuyApplyService extends BaseService<ChoProductReq, String> implements IWorkFlowListener {
    @Autowired
    private ChoProductBuyApplyDao choProductBuyApplyDao;
    @Autowired
    private ChoProductService choProductService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @Autowired
	private IWorkFlowService workFlowService;
    @Autowired
    private ChoMessageService choMessageService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<ChoProductReq, String> getEntityDao() {
        return choProductBuyApplyDao;
    }
    /**
     * 理财产品购买申请查询页数据
     *
     * @param dataTablesInput 前端传参
     * @param user            当前用户
     * @return 理财产品购买申请查询页数据
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
        if (EmptyUtils.isEmpty(moneyUint)) {
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTBUYAPPLY_CHOPRODUCTBUYAPPLY_QUERY, user.getId()), "pr.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = choProductBuyApplyDao.getDataTablesUnion(dataTablesInput, sql.toString());
        List<ChoProductReqVo> list = output.getData();
        for (ChoProductReqVo vo : list) {
            choProductReqService.setBeanMoneyType(vo, moneyUint);
        }
        // 注入远程信息
        injectValue(list);
        output.setData(list);
        return output;
    }
    /**
     * 注入远程信息。
     *
     * @param list 理财产品列表。
     */
    private void injectValue(List<ChoProductReqVo> list){
        Optional.ofNullable(list).ifPresent(data -> {
            Map<String, DictDTO> listStatusMap = dictService.listMap("choBankFinance.status");
            Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
            Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
            data.forEach(product -> {
                if (ObjectUtil.isAllNotEmpty(product.getStatus(), listStatusMap)) {
                    Optional.ofNullable(listStatusMap.get(product.getStatus() + "")).ifPresent(dict -> product.setStatusName(dict.getName()));
                }
                if (ObjectUtil.isAllNotEmpty(product.getFinancialType(), listTypeMap)) {
                    Optional.ofNullable(listTypeMap.get(product.getFinancialType())).ifPresent(dict -> product.setFinancialTypeName(dict.getName()));
                }
                if (ObjectUtil.isAllNotEmpty(product.getPayWay(), listPayWayMap)) {
                    Optional.ofNullable(listPayWayMap.get(product.getPayWay())).ifPresent(dict -> product.setPayWayName(dict.getName()));
                }
            });
        });
    }
    /**
     * 保存ChoProductReq实体
     *
     * @param choProductReq 理财登记实体
     * @param user          当前用户
     */
    public void saveChoProductReq(ChoProductReq choProductReq, ICurrentUser user) {
        // 校验产品代码是否已存在
        ChoProductVo choProductVo = choProductService.getByCode(choProductReq.getProductCode());
        //这里把冗余的字段补全
        CorpDTO corpDTO = corpService.get(choProductReq.getCorpId());
        if (corpDTO != null){
            choProductReq.setCorpName(corpDTO.getName());
        }

        BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(choProductReq.getBankTypeId());
        choProductReq.setBankTypeName(bankTypeDTO.getTypeName());

        BtCurrencyDTO objectById = btCurrencyService.getObjectById(choProductReq.getCurId());
        choProductReq.setCurName(objectById.getCurName());
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
        choProductReq.setIsBuyApply(CommonConstant.YES);
        choProductReq.setBillNo(SequenceSingleton.getSequence(SequenceEnum.CHO_PRODUCT_REQ));
        if (StringUtils.isBlank(choProductReq.getId())) {
            choProductReq.setCreateBy(user.getLoginName());
            choProductReq.setCreateDate(DateUtils.getDate());
        }
        boolean isSubmit = ChoProductReqConstant.STATUE_WAIT_CHECK.equals(choProductReq.getBuyApplyStatus());
        choProductReq.setBizStatus(isSubmit ? ChoProductReqConstant.BIZ_STATUS14 : ChoProductReqConstant.BIZ_STATUS13);
        choProductBuyApplyDao.save(choProductReq);
        Integer messageType = ChoMessageConstant.MESSAGE_TYPE_TEMP;
        if (isSubmit) {
            messageType = ChoMessageConstant.MESSAGE_TYPE_SUBMIT;
             getStartHandler().handler(ChoWorkFlowConstant.CHO_PRODUCT_BUY_APPLY, choProductReq.getId(), choProductReq, choProductReq.getCorpId(), user.getId());
        }
        //生成日志
        choMessageService.saveMessage(choProductReq.getBillNo(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ, user);
    }
    /**
     * 修改ChoProductReq实体
     *
     * @param entity 理财登记实体
     * @param user   当前用户
     */
    public void modifyChoProductReq(ChoProductReq entity, ICurrentUser user) {
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
        boolean isSubmit = ChoProductReqConstant.STATUE_WAIT_CHECK.equals(entity.getBuyApplyStatus());
        entity.setBizStatus(isSubmit ? ChoProductReqConstant.BIZ_STATUS14 : ChoProductReqConstant.BIZ_STATUS13);
        this.saveOrUpdate(entity);
        Integer messageType = ChoMessageConstant.MESSAGE_TYPE_TEMP;
        if (isSubmit) {
            messageType = ChoMessageConstant.MESSAGE_TYPE_SUBMIT;
             getStartHandler().handler(ChoWorkFlowConstant.CHO_PRODUCT_BUY_APPLY, entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        //生成日志
        choMessageService.saveMessage(entity.getBillNo(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ, user);
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

    /**
     * 批量提交
     *
     * @param ids  id集合
     * @param user 当前用户
     */
    public void batchSubmit(String ids, ICurrentUser user) {
         for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            ChoProductReq entity = this.get(id);
            entity.setBuyApplyStatus(CommonConstant.STATUE_WAIT_CHECK);
            entity.setBatchSubmit(true);
            this.modifyChoProductReq(entity, user);
        }
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput 前端传参
     * @return 审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(ChoWorkFlowConstant.CHO_PRODUCT_BUY_APPLY, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "pr.id", true);
        DataTablesOutput<ChoProductReqVo> dp = this.choProductBuyApplyDao.dataTableApprove(dataTablesInput, businessSql);
        String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
        if (EmptyUtils.isEmpty(moneyUint)) {
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        List<ChoProductReqVo> list = dp.getData();
        for (ChoProductReqVo vo : list) {
            choProductReqService.setBeanMoneyType(vo, moneyUint);
        }
        // 注入远程信息
        injectValue(list);
        dp.setData(list);
        return workFlowBusiness.merge(dp);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        ChoProductReq entity = this.get(workFlowResult.getBusinessKey(), true);
        Integer messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        boolean isPass = workFlowResult.isPass();
        if (isPass) {
            //审批通过
            messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            entity.setBuyApplyStatus(ChoProductReqConstant.STATUE_CHECK);
            entity.setStatus(ChoProductReqConstant.STATUE_TEMP);
            entity.setBizStatus(ChoProductReqConstant.BIZ_STATUS16);
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
        } else {
            entity.setBizStatus(ChoProductReqConstant.BIZ_STATUS15);
            //审批打回
            entity.setBuyApplyStatus(ChoProductReqConstant.STATUE_BACK);
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.updateEntity(entity);
        //生成日志
        choMessageService.saveMessage(entity.getBillNo(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ, user);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 自动生成合同号（年份+月份+顺序号 一共8位，例如2022110008）
     *
     * @return 合同号
     */
    public String createContractNo() {
        // 当前日期的年月
        String contractNoPrefix = DateUtils.format(new Date(), "yyyyMM");
        List<ChoProductReq> choProductReqList = choProductBuyApplyDao.queryListWithPrefix(contractNoPrefix);
        int maxSort = 1;
        for (ChoProductReq choProductReq : choProductReqList) {
            String contractNo = choProductReq.getContractNo();
            if (StringUtils.isNotEmpty(contractNo) && contractNo.length() == 10) {
                int sort = Integer.parseInt(contractNo.substring(6, 10)) + 1;
                maxSort = Math.max(maxSort, sort);
            }
        }
        StringBuilder sort = new StringBuilder(String.valueOf(maxSort));
        // 如果顺序号长度小于4，则向前补零
        while (sort.length() < 4) {
            sort.insert(0, 0);
        }
        // 在顺序号前面拼接年月
        return sort.insert(0, contractNoPrefix).toString();
    }
}
