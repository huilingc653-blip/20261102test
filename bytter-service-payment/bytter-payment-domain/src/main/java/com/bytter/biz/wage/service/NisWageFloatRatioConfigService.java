package com.bytter.biz.wage.service;

import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.wage.dao.NisWageFloatRatioConfigDao;
import com.bytter.biz.wage.entity.NisWageFloatRatioConfig;
import com.bytter.biz.wage.vo.NisWageFloatRatioConfigVO;
import com.bytter.biz.wage.vo.NisWageFloatRatioMessageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.enums.NetbankMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 代发工资浮动比率配置业务层
 *
 * @author fangcf
 * @date 2025-11-20
 */
@Service("nisWageFloatRatioConfigService")
@Transactional
public class NisWageFloatRatioConfigService extends BaseService<NisWageFloatRatioConfig, String> {

    @Autowired
    private NisWageFloatRatioConfigDao nisWageFloatRatioConfigDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private NisMessageService nisMessageService;

    @Override
    public BaseDao<NisWageFloatRatioConfig, String> getEntityDao() {
        return nisWageFloatRatioConfigDao;
    }

    /**
     * 查询银行计划开票金额维护列表
     *
     * @param input 入参
     * @return 出参
     */
    public DataTablesOutput<NisWageFloatRatioConfigVO> dataTable(DataTablesInput input) {
        return nisWageFloatRatioConfigDao.dataTable(input);
    }

    /**
     * 保存代发工资浮动比率配置
     *
     * @param form 表单数据
     * @param user 用户对象
     */
    public void saveWageFloatRatio(NisWageFloatRatioConfig form, ICurrentUser user) {
        // 数据校验
        this.dataCheck(form);
        // 默认未删除
        form.setIsDeleted(CommonConstant.NO);
        form.setCreateBy(user.getId());
        form.setCreateTime(DateUtils.getDate());
        form.setUpdateBy(user.getId());
        form.setUpdateTime(DateUtils.getDate());
        this.saveEntity(form);

        //操作记录
        String message = "浮动比率" + this.convertStr(form.getFloatRate());
        //生成操作记录
        nisMessageService.saveMessage(form.getId(), NisMessageConstant.MESSAGE_TYPE_ADD, message, NisMessageConstant.BUS_TYPE_WAGE_FLOAT_RATIO, user);
    }

    /**
     * 保存代发工资浮动比率配置
     *
     * @param form 表单数据
     * @param user 用户对象
     */
    public void updateWageFloatRatio(NisWageFloatRatioConfig form, ICurrentUser user) {
        // 数据校验
        this.dataCheck(form);
        BigDecimal newFloatRate = form.getFloatRate();
        // 原值
        NisWageFloatRatioConfig entity = this.get(form.getId(), true);
        BigDecimal oldFloatRate = entity.getFloatRate();
        // 修改值覆盖原值
        BeanUtils.copyNotNullProperties(form, entity);
        form.setUpdateBy(user.getId());
        form.setUpdateTime(DateUtils.getDate());
        this.saveOrUpdate(entity);

        //操作记录
        String message = "浮动比率从" + this.convertStr(oldFloatRate) + "修改成" + this.convertStr(newFloatRate);
        //生成操作记录
        nisMessageService.saveMessage(form.getId(), NisMessageConstant.MESSAGE_TYPE_MODIFY, message, NisMessageConstant.BUS_TYPE_WAGE_FLOAT_RATIO, user);
    }

    /**
     * 删除代发工资浮动比率配置
     *
     * @param id   主键id
     * @param user 用户对象
     */
    public void delWageFloatRatio(String id, ICurrentUser user) {
        NisWageFloatRatioConfig entity = this.get(id, true);
        // 已删除
        entity.setIsDeleted(CommonConstant.YES);
        this.saveOrUpdate(entity);

        //操作记录
        String message = "删除浮动比率";
        //生成操作记录
        nisMessageService.saveMessage(id, NisMessageConstant.MESSAGE_TYPE_DELETE, message, NisMessageConstant.BUS_TYPE_WAGE_FLOAT_RATIO, user);
    }

    /**
     * 根据月份获取代发工资浮动比率配置表
     *
     * @param month 月份
     * @return 对应月份代发工资浮动比率配置表
     */
    public NisWageFloatRatioConfig getByMonth(String month) {
        return nisWageFloatRatioConfigDao.getByMonth(month);
    }

    /**
     * 数据校验
     *
     * @param form 表单数据
     */
    private void dataCheck(NisWageFloatRatioConfig form) {
        // 1、非空校验
        if (StringUtils.isEmpty(form.getMonth())) {
            throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_MONTH_NULL);
        }
        if (EmptyUtils.isEmpty(form.getFloatRate())) {
            throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_FLOATRATE_NULL);
        }
        // 2、同一月份只有一条数据
        boolean flag = nisWageFloatRatioConfigDao.checkUnique(form.getMonth(), form.getId());
        if (!flag) {
            throw new BusinessException(NetbankMessageEnum.NIS_WAGE_MESSAGE_MONTH_NOT_UNIQUE);
        }
    }

    /**
     * 代发工资浮动比率转换成字符串
     *
     * @param floatRate 浮动比率
     * @return 字符串
     */
    private String convertStr(BigDecimal floatRate) {
        return NumberUtil.formatNumeric(floatRate.multiply(new BigDecimal("100")), 2) + "%";
    }

    /**
     * 操作记录查询列表
     *
     * @return 操作记录列表
     */
    public DataTablesOutput<NisWageFloatRatioMessageVO> datatableMessage(DataTablesInput input) {
        return nisWageFloatRatioConfigDao.datatableMessage(input);
    }
}
