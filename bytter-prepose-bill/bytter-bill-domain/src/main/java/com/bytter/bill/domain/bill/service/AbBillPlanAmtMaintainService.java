package com.bytter.bill.domain.bill.service;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillPlanAmtMaintainDao;
import com.bytter.bill.domain.bill.entity.AbBillPlanAmtMaintain;
import com.bytter.bill.domain.bill.vo.AbBillPlanAmtMaintainVO;
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

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 银行计划开票金额维护业务层
 *
 * @author fangcf
 * @date 2025-06-30
 */
@Service("abBillPlanAmtMaintainService")
@Transactional
public class AbBillPlanAmtMaintainService extends BaseService<AbBillPlanAmtMaintain, String> implements IUserExcelVerifyHandler<AbBillPlanAmtMaintainVO> {

    @Autowired
    private AbBillPlanAmtMaintainDao abBillPlanAmtMaintainDao;
    @Autowired
    private AbCorpService abCorpService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;

    @Override
    public BaseDao<AbBillPlanAmtMaintain, String> getEntityDao() {
        return abBillPlanAmtMaintainDao;
    }

    /**
     * 查询银行计划开票金额维护列表
     *
     * @param input 入参
     * @param user  用户对象
     * @return 出参
     */
    public DataTablesOutput dataTable(DataTablesInput input, ICurrentUser user) {
        List<String> authCorpList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_PLANAMT_MAINTAIN_QUERY, user.getId());
        if (CollectionUtils.isEmpty(authCorpList)) {
            return new DataTablesOutput();
        }
        return abBillPlanAmtMaintainDao.dataTable(input, authCorpList);
    }

    /**
     * 保存银行计划开票金额提交或暂存
     *
     * @param form 产品赎回表单
     * @param user 用户对象
     */
    public void savePlanAmt(AbBillPlanAmtMaintain form, ICurrentUser user) {
        // 数据校验
        this.dataCheck(form);

        form.setCreateBy(user.getId());
        form.setCreateTime(DateUtils.getDate());
        form.setUpdateBy(user.getId());
        form.setUpdateTime(DateUtils.getDate());
        this.saveEntity(form);
    }

    /**
     * 保存银行计划开票金额提交或暂存
     *
     * @param form 产品赎回表单
     * @param user 用户对象
     */
    public void updatePlanAmt(AbBillPlanAmtMaintain form, ICurrentUser user) {
        // 数据校验
        this.dataCheck(form);
        // 原值
        AbBillPlanAmtMaintain entity = this.get(form.getId(), true);
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
    private void dataCheck(AbBillPlanAmtMaintain form) {
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
        // 周开始日
        Date weekStartDay = form.getWeekStartDay();
        if (EmptyUtils.isEmpty(weekStartDay)) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_WEEKSTARTDAY_NULL);
        }
        // 周结束日
        Date weekEndDay = form.getWeekEndDay();
        if (EmptyUtils.isEmpty(weekEndDay)) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_WEEKENDDAY_NULL);
        }
        if (EmptyUtils.isEmpty(form.getCurrWeekBillMoney())) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_CURRWEEKBILLMONEY_NULL);
        }
        if (EmptyUtils.isEmpty(form.getNextWeekBillMoney())) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_NEXTWEEKBILLMONEY_NULL);
        }
        // 周开始日必须是周一
        LocalDate startLocalDate = Instant.ofEpochMilli(weekStartDay.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        if (startLocalDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_MUST_MONDAY);
        }
        // 周结束日必须是对应周开始日的周日
        LocalDate endLocalDate = Instant.ofEpochMilli(weekEndDay.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        if (startLocalDate.plusDays(6).getDayOfWeek() != endLocalDate.getDayOfWeek() || endLocalDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_MUST_SUNDAY);
        }
        // 2、单位 + 银行 + 周开始日 + 周结束日只能有一条相同的记录
        boolean flag = abBillPlanAmtMaintainDao.checkUnique(form.getCorpId(), form.getBankTypeId(), form.getWeekStartDay(), form.getWeekEndDay(), form.getId());
        if (!flag) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_NOT_UNIQUE);
        }
    }

    /**
     * 银行计划开票金额删除
     *
     * @param id 产品赎回主键id
     */
    public void delPlanAmt(String id) {
        // 删除前判断该条数据是否被引用到
        // throw new BusinessException(SystemMessageEnum.DATA_REFERENCED_EXIST);
        AbBillPlanAmtMaintain entity = this.get(id, true);
        this.deleteEntity(entity);

    }

    /**
     * 银行计划开票金额导入
     *
     * @param list 导入数据集合
     * @param user 用户对象
     */
    public void saveImport(List<AbBillPlanAmtMaintain> list, ICurrentUser user) {
        list.forEach(entity -> this.savePlanAmt(entity, user));
    }

    /**
     * 获取2000年-2099年所有周一日期，然后日期字符串拼接
     *
     * @return 所有周一日期字符串
     */
    public String getMondays() {
        // “周开始日”必须是周一，确定取值范围
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2099, 12, 31);

        List<String> mondays = Lists.newArrayList();
        // 日期格式为yyyy-MM-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 找到第一个周一
        LocalDate firstMonday = start.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        // 从第一个周一开始，每周增加7天，直到超过结束日期
        for (LocalDate date = firstMonday; !date.isAfter(end); date = date.plusWeeks(1)) {
            mondays.add(date.format(formatter));
        }
        // 拼接日期字符串
        StringBuilder sb = new StringBuilder();
        mondays.forEach(monday -> sb.append(monday).append(CommonConstant.SEMI_SPLIT));

        String showAllMonday = "";
        if (StringUtils.isNotEmpty(sb.toString())) {
            showAllMonday = sb.toString().substring(0, sb.toString().length() - 1);
        }
        return showAllMonday;
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(AbBillPlanAmtMaintainVO obj, Object... user) {
        return null;
    }

    @Override
    public void verifyHandler(List<AbBillPlanAmtMaintainVO> list, Object... user) {
        if (!CollectionUtils.isEmpty(list)) {
            String userId = "";
            if (user[0] != null) {
                ICurrentUser userInfo = (ICurrentUser) user[0];
                userId = userInfo.getId();
            }
            for (AbBillPlanAmtMaintainVO vo : list) {
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
                        List<String> authCorpList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_PLANAMT_MAINTAIN_QUERY, userId);
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
                // 周开始日
                Date weekStartDay = vo.getWeekStartDay();
                if (EmptyUtils.isEmpty(weekStartDay)) {
                    errorMsgList.add("周开始日不能为空");
                } else {
                    // 周开始日必须是周一
                    LocalDate startLocalDate = Instant.ofEpochMilli(weekStartDay.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                    if (startLocalDate.getDayOfWeek() != DayOfWeek.MONDAY) {
                        errorMsgList.add("周开始日必须是周一");
                    }
                }
                // 周结束日
                Date weekEndDay = vo.getWeekEndDay();
                if (EmptyUtils.isEmpty(weekEndDay)) {
                    errorMsgList.add("周结束日不能为空");
                }
                // 周结束日必须是对应周开始日的周日
                if (!EmptyUtils.isEmpty(weekStartDay) && !EmptyUtils.isEmpty(weekEndDay)) {
                    LocalDate startLocalDate = Instant.ofEpochMilli(weekStartDay.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate endLocalDate = Instant.ofEpochMilli(weekEndDay.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                    if (startLocalDate.plusDays(6).getDayOfWeek() != endLocalDate.getDayOfWeek() || endLocalDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                        errorMsgList.add("周结束日必须是对应周开始日的周日");
                    }
                }
                // 本周计划开票金额
                if (EmptyUtils.isEmpty(vo.getCurrWeekBillMoney())) {
                    errorMsgList.add("本周计划开票金额不能为空");
                }
                // 下周计划开票金额
                if (EmptyUtils.isEmpty(vo.getNextWeekBillMoney())) {
                    errorMsgList.add("下周计划开票金额不能为空");
                }
                // 单位 + 银行 + 周开始日 + 周结束日只能有一条相同的记录
                if (StringUtils.isNotEmpty(vo.getCorpId()) && StringUtils.isNotEmpty(vo.getBankTypeId()) && !EmptyUtils.isEmpty(weekStartDay) && !EmptyUtils.isEmpty(weekEndDay)) {
                    boolean flag = abBillPlanAmtMaintainDao.checkUnique(vo.getCorpId(), vo.getBankTypeId(), weekStartDay, weekEndDay, "");
                    if (!flag) {
                        errorMsgList.add("银行计划开票金额在相同单位、相同银行、相同周开始日、相同周结束日下必须唯一");
                    }
                }

                vo.addErrorMsg(errorMsgList);
            }
            // 过滤出合格数据
            List<AbBillPlanAmtMaintainVO> successList = list.stream().filter(item -> StringUtils.isEmpty(item.getErrorMsg())).collect(Collectors.toList());
            // 根据单位 + 银行 + 周开始日 + 周结束日分组
            Map<String, List<AbBillPlanAmtMaintainVO>> groupList = successList
                    .stream()
                    .collect(Collectors.groupingBy(item -> item.getCorpId() + CommonConstant.COMMA_SPLIT
                            + item.getBankTypeId() + CommonConstant.COMMA_SPLIT
                            + DateUtils.format(item.getWeekStartDay(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + CommonConstant.COMMA_SPLIT
                            + DateUtils.format(item.getWeekEndDay(), DateUtils.PATTERN_CLASSICAL_SIMPLE)));

            groupList.forEach((k, v) -> {
                if (v.size() > 1) {
                    v.forEach(item -> item.setErrorMsg("银行计划开票金额在相同单位、相同银行、相同周开始日、相同周结束日下必须唯一"));
                }
            });
        }
    }
}
