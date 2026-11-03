package com.bytter.credit.domain.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.CopyUtil;
import com.bytter.credit.constant.BankCreditPlanResourceConstant;
import com.bytter.credit.domain.dao.YearBankCreditPlanDao;
import com.bytter.credit.domain.entity.YearBankCreditPlan;
import com.bytter.credit.domain.vo.YearBankCreditPlanVO;
import com.bytter.system.api.ICorpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行授信年度计划服务
 *
 * @author huao
 */
@Service
@RequiredArgsConstructor
public class YearBankCreditPlanService {

    private final YearBankCreditPlanDao yearBankCreditPlanDao;

    private final ICorpService corpService;

    public DataTablesOutput<YearBankCreditPlanVO> dataTableQuery(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuthList = corpService.getUserCorpIds(BankCreditPlanResourceConstant.BANK_CREDIT_PLAN_QUERY, user.getId());
        return yearBankCreditPlanDao.dataTableQuery(input, corpAuthList);
    }

    public YearBankCreditPlanVO getById(String id) {
        return yearBankCreditPlanDao.getById(id);
    }

    public void saveOrUpdate(YearBankCreditPlanVO detail, ICurrentUser currentUser) {
        if (CharSequenceUtil.isBlank(detail.getId())) {
            String corpId = detail.getCorpId();
            String bankCode = detail.getBankCode();
            Integer year =  detail.getYear();
            List<YearBankCreditPlanVO> list = yearBankCreditPlanDao.listByCorpAndYear(corpId, bankCode, year);
            if (CollUtil.isNotEmpty(list) && list.stream().anyMatch(x -> x.getCreditType().equals(detail.getCreditType()))) {
                throw new BusinessException("当前单位在本年度已存在该授信类型数据");
            }
        }
        YearBankCreditPlan entity = BeanUtil.copyProperties(detail, YearBankCreditPlan.class);
        Date now = new Date();
        if (CharSequenceUtil.isBlank(entity.getId())) {
            entity.setCreateBy(currentUser.getId()).setCreateName(currentUser.getLoginName())
                    .setCreateTime(now);
        } else {
            // 单位、授信类型不可修改
            entity.setCorpId(null).setCreditType(null);
            // 当前数据库存在的
            YearBankCreditPlan exists = yearBankCreditPlanDao.get(detail.getId(), true);
            entity = CopyUtil.replaceIfDiff(entity, exists);
        }
        // 设置更新信息
        entity.setUpdateBy(currentUser.getId()).setUpdateName(currentUser.getLoginName()).setUpdateTime(now);
        if (CharSequenceUtil.isBlank(detail.getId())) {
            yearBankCreditPlanDao.save(entity);
        } else {
            yearBankCreditPlanDao.update(entity);
        }
        yearBankCreditPlanDao.flush();
    }

    public void deleteByIds(List<String> ids) {
        yearBankCreditPlanDao.delete(ids);
        yearBankCreditPlanDao.flush();
    }

}
