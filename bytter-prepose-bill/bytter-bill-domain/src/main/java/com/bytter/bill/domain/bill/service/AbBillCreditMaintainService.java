package com.bytter.bill.domain.bill.service;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillCreditMaintainDao;
import com.bytter.bill.domain.bill.entity.AbBillCreditInfoMaintain;
import com.bytter.bill.domain.bill.vo.AbBillCreditInfoMaintainVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 银行开票授信资料维护业务层
 *
 * @author fangcf
 * @date 2025-06-26
 */
@Service("abBillCreditMaintainService")
@Transactional
public class AbBillCreditMaintainService extends BaseService<AbBillCreditInfoMaintain, String> implements IUserExcelVerifyHandler<AbBillCreditInfoMaintainVO> {

    @Autowired
    private AbBillCreditMaintainDao abBillCreditMaintainDao;
    @Autowired
    private AbCorpService abCorpService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;

    @Override
    public BaseDao<AbBillCreditInfoMaintain, String> getEntityDao() {
        return abBillCreditMaintainDao;
    }

    /**
     * 查询银行开票授信资料维护列表
     *
     * @param input 入参
     * @param user  用户对象
     * @return 出参
     */
    public DataTablesOutput dataTable(DataTablesInput input, ICurrentUser user) {
        List<String> authCorpList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_CREDIT_MAINTAIN_QUERY, user.getId());
        if (CollectionUtils.isEmpty(authCorpList)) {
            return new DataTablesOutput();
        }
        return abBillCreditMaintainDao.dataTable(input, authCorpList);
    }

    /**
     * 保存银行开票授信资料提交或暂存
     *
     * @param form 产品赎回表单
     * @param user 用户对象
     */
    public void saveCreditInfo(AbBillCreditInfoMaintain form, ICurrentUser user) {
        // 数据校验
        this.dataCheck(form);
        if (StringUtils.isEmpty(form.getIsUsed())) {
            form.setIsUsed(CommonConstant.YES);
        }
        form.setCreateBy(user.getId());
        form.setCreateTime(DateUtils.getDate());
        form.setUpdateBy(user.getId());
        form.setUpdateTime(DateUtils.getDate());
        this.saveEntity(form);
    }

    /**
     * 保存银行开票授信资料提交或暂存
     *
     * @param form 产品赎回表单
     * @param user 用户对象
     */
    public void updateCreditInfo(AbBillCreditInfoMaintain form, ICurrentUser user) {
        // 数据校验
        this.dataCheck(form);
        // 原值
        AbBillCreditInfoMaintain entity = this.get(form.getId(), true);
        // 修改值覆盖原值
        BeanUtils.copyNotNullProperties(form, entity);
        form.setUpdateBy(user.getId());
        form.setUpdateTime(DateUtils.getDate());
        this.saveOrUpdate(entity);
    }

    /**
     * 数据校验
     *
     * @param form 表单数据
     */
    private void dataCheck(AbBillCreditInfoMaintain form) {
        // 1、非空校验
        if (StringUtils.isEmpty(form.getCorpId())) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_CREDITMAINTAIN_CORPID_NULL);
        }
        if (StringUtils.isEmpty(form.getBankTypeId())) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_CREDITMAINTAIN_BANKTYPEID_NULL);
        }
        if (StringUtils.isEmpty(form.getCreditType())) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_CREDITMAINTAIN_CREDITTYPE_NULL);
        }
        // 2、单位 + 银行 + 授信类型只能有一条记录
        boolean flag = abBillCreditMaintainDao.checkUnique(form.getCorpId(), form.getBankTypeId(), form.getCreditType(), form.getId());
        if (!flag) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_CREDITMAINTAIN_NOT_UNIQUE);
        }
    }

    /**
     * 银行开票授信资料删除
     *
     * @param id 产品赎回主键id
     */
    public void delCreditInfo(String id) {
        // 删除前判断该条数据是否被引用到
        // throw new BusinessException(SystemMessageEnum.DATA_REFERENCED_EXIST);
        AbBillCreditInfoMaintain entity = this.get(id, true);
        this.deleteEntity(entity);

    }

    /**
     * 银行开票授信资料导入
     *
     * @param list 导入数据集合
     * @param user 用户对象
     */
    public void saveImport(List<AbBillCreditInfoMaintain> list, ICurrentUser user) {
        list.forEach(entity -> this.saveCreditInfo(entity, user));
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(AbBillCreditInfoMaintainVO obj, Object... user) {
        return null;
    }

    @Override
    public void verifyHandler(List<AbBillCreditInfoMaintainVO> list, Object... user) {
        if (!org.springframework.util.CollectionUtils.isEmpty(list)) {
            String userId = "";
            if (user[0] != null) {
                ICurrentUser userInfo = (ICurrentUser) user[0];
                userId = userInfo.getId();
            }
            for (AbBillCreditInfoMaintainVO vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 单位
                if (StringUtils.isEmpty(vo.getCorpName())) {
                    errorMsgList.add("单位名称不能为空");
                } else {
                    CorpDTO corp = abCorpService.getCorpIdByName(vo.getCorpName());
                    if (EmptyUtils.isEmpty(corp)) {
                        // 单位名称在资金系统未维护
                        errorMsgList.add("单位名称在资金系统未维护");
                    } else {
                        // 该用户是否存在单位权限
                        // 获取资源权限单位
                        List<String> authCorpList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_CREDIT_MAINTAIN_QUERY, userId);
                        // 判断该单位id是否有资源权限
                        if (CollectionUtils.isEmpty(authCorpList) || !authCorpList.contains(corp.getId())) {
                            // 用户不存在该单位权限
                            errorMsgList.add("用户不存在该单位权限");
                        } else {
                            vo.setCorpId(corp.getId());
                        }
                    }
                }
                // 银行名称
                if (StringUtils.isEmpty(vo.getBankTypeName())) {
                    errorMsgList.add("银行名称不能为空");
                } else {
                    List<BankTypeDTO> bankTypeList = bankTypeService.listByValidSign(CommonConstant.YES);
                    List<BankTypeDTO> filter = bankTypeList.stream().filter(item -> vo.getBankTypeName().equals(item.getTypeName())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(filter)) {
                        errorMsgList.add("银行名称在资金系统未维护");
                    } else {
                        vo.setBankTypeId(filter.get(0).getId());
                    }
                }
                // 授信类型
                if (StringUtils.isEmpty(vo.getCreditTypeName())) {
                    errorMsgList.add("授信类型不能为空");
                } else {
                    String code = dictService.getCodeByName("bill:accebill.creditType", vo.getCreditTypeName());
                    if (StringUtils.isEmpty(code)) {
                        errorMsgList.add("授信类型不存在");
                    } else {
                        vo.setCreditType(code);
                    }
                }
                // 单位 + 银行 + 授信类型只能有一条记录
                if (StringUtils.isNotEmpty(vo.getCorpId()) && StringUtils.isNotEmpty(vo.getBankTypeId()) && StringUtils.isNotEmpty(vo.getCreditType())) {
                    boolean flag = abBillCreditMaintainDao.checkUnique(vo.getCorpId(), vo.getBankTypeId(), vo.getCreditType(), "");
                    if (!flag) {
                        errorMsgList.add("银行开票授信资料在相同单位、相同银行、相同授信类型下必须唯一");
                    }
                }
                // 保证金比例
                BigDecimal bondRatio = vo.getBondRatio();
                if (!EmptyUtils.isEmpty(bondRatio)) {
                    // 保证金比例格式
                    if (bondRatio.compareTo(BigDecimal.ZERO) <= 0) {
                        // 保证金小于0则设置为0
                        bondRatio = BigDecimal.ZERO;
                    } else if (bondRatio.compareTo(new BigDecimal(100)) > 0) {
                        bondRatio = new BigDecimal(100);
                    }
                    vo.setBondRatio(bondRatio.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));
                }
                // 开票手续费
                BigDecimal openBillRate = vo.getOpenBillRate();
                if (!EmptyUtils.isEmpty(openBillRate)) {
                    // 保证金比例格式
                    if (openBillRate.compareTo(BigDecimal.ZERO) <= 0) {
                        // 保证金小于0则设置为0
                        openBillRate = BigDecimal.ZERO;
                    } else if (openBillRate.compareTo(new BigDecimal(100)) > 0) {
                        openBillRate = new BigDecimal(100);
                    }
                    vo.setOpenBillRate(openBillRate.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));
                }
                // 保证金利率
                if (StringUtils.isNotEmpty(vo.getBondRate()) && vo.getBondRate().length() > 200) {
                    errorMsgList.add("保证金利率字符长度最大不能超过200");
                }
                vo.addErrorMsg(errorMsgList);
            }
            // 过滤出合格数据
            List<AbBillCreditInfoMaintainVO> successList = list.stream().filter(item -> StringUtils.isEmpty(item.getErrorMsg())).collect(Collectors.toList());
            // 根据单位 + 银行 + 授信类型分组
            Map<String, List<AbBillCreditInfoMaintainVO>> groupList = successList
                    .stream()
                    .collect(Collectors.groupingBy(item -> item.getCorpId() + CommonConstant.COMMA_SPLIT + item.getBankTypeId() + CommonConstant.COMMA_SPLIT + item.getCreditType()));

            groupList.forEach((k, v) -> {
                if (v.size() > 1) {
                    v.forEach(item -> item.setErrorMsg("银行开票授信资料在相同单位、相同银行、相同授信类型下必须唯一"));
                }
            });
        }
    }
}
