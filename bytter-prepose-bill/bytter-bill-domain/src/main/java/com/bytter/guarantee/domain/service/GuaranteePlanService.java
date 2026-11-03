package com.bytter.guarantee.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.dao.GuaranteePlanDao;
import com.bytter.guarantee.domain.entity.GuaranteePlan;
import com.bytter.guarantee.domain.vo.GuaContractInfoPlanVO;
import com.bytter.guarantee.domain.vo.RepayPlanVO;
import com.bytter.guarantee.domain.vo.RepaymentScheduleVO;
import com.bytter.guarantee.dto.RepayPlanDTO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Service("guaranteePlanService")
@Transactional
public class GuaranteePlanService extends BaseService<GuaranteePlan, String>
        implements ITaskService, IUserExcelVerifyHandler<GuaContractInfoPlanVO> {
    @Autowired
    private GuaranteePlanDao guaranteePlanDao;
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存还款计划
     *
     * @param entity
     */
    public void save(GuaranteePlan entity) {
        super.saveEntity(entity);
    }
    @Override
    public BaseDao<GuaranteePlan, String> getEntityDao() {
        return guaranteePlanDao;
    }
    /**
     * 保存还款计划
     * 
     * @param rp
     */
    public String save(RepayPlanVO rp, ICurrentUser user) {
        String billCode = rp.getBillCode();
        String ids = rp.getPlanId();
        String id = StringUtils.getUUId();
        if (StringUtils.isNotEmpty(billCode)) {
            // 先根据单号删除之前的还款计划
            deleteRepaymentSchedule(billCode, ids);
            subSave(billCode, rp, user);
            id = billCode;
            // id = guaranteePlanDao.getPlanIds(billCode);
        } else if (StringUtils.isNotEmpty(ids)) {
            // 先根据单号删除之前的还款计划
            deleteRepaymentSchedule(billCode, ids);
            subSave(id, rp, user);
        } else {
            subSave(id, rp, user);
        }
        return id;
    }
    /**
     * 保存还款计划表
     * 
     * @param id
     */
    public void subSave(String id, RepayPlanVO rp, ICurrentUser user) {
        if (rp != null && EmptyUtils.isNotEmpty(rp.getRepayMents())) {
            for (int i = 0; i < rp.getRepayMents().size(); i++) {
                RepayPlanDTO rs = rp.getRepayMents().get(i);
                // 还款计划明细表
                GuaranteePlan entity = new GuaranteePlan();
                if (rs.getPrediceRepayDate() != null) {
                    entity.setBillCode(id);// 单号
                    entity.setIds(id);
                    entity.setPrediceRepayDate(DateUtils.parse(rs.getPrediceRepayDate(), "yyyy-MM-dd"));// 预计还款日
                    entity.setRepaymentMoney(rs.getRepaymentMoney());// 还款金额
                    entity.setRepaymentIst(rs.getRepaymentIst());// 利息
                    entity.setCurrentRepayMoney(rs.getCurrentRepayMoney());// 本期还款金额
                    entity.setIsRepay(rs.getIsRepay());// 是否已还
                    entity.setRemark(rs.getRemark());// 备注
                    entity.setCreateBy(user.getLoginName());
                    entity.setCreateName(user.getUsername());
                    entity.setCreateDate(new Date());
                    saveEntity(entity);
                }
            }
        }
    }
    /**
     * 查询还款计划数据
     * 
     * @param repayPlanVo
     * @return
     */
    @Transactional(readOnly = true)
    public List<RepaymentScheduleVO> listRepayMentSchedule(RepayPlanVO repayPlanVo) {
        // planId非空：则回显还款计划数据
        if (!StringUtils.isEmpty(repayPlanVo.getPlanId())) {
            return listRepayMentScheduleById(repayPlanVo.getPlanId(), "");
        } else {
            return listRepayMentScheduleById("", repayPlanVo.getBillCode());
        }
    }
    /**
     * 根据id获取还款计划
     *
     * @param id
     * @return
     */
    public List<RepaymentScheduleVO> listRepayMentScheduleById(String id, String billNo) {
        return guaranteePlanDao.listRepayMentScheduleById(id, billNo);
    }
    public void deleteRepaymentSchedule(String billCode, String ids) {
        guaranteePlanDao.deleteRepaymentSchedule(billCode, ids);
    }
    /**
     * 根据还款计划批次号修改还款计划对应的业务单据id
     *
     * @param billCode
     *            单号
     */
    public void updateBillCodeByIds(String ids, String billCode) {
        guaranteePlanDao.updateBillCodeByIds(ids, billCode);
    }
    @Override
    @XxlJob("GuaranteePlanService")
    public void run(String arguments) {
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(GuaContractInfoPlanVO obj, Object... user) {
        return null;
    }
    @Override
    public void verifyHandler(List<GuaContractInfoPlanVO> list, Object... user) {
        if (!ListUtils.isNullOrEmpty(list)) {
            Map<String, Map<String, GuaContractInfoPlanVO>> boxMap = new HashMap<>();
            for (GuaContractInfoPlanVO scheduleVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 校验预计还款日
                if (EmptyUtils.isEmpty(scheduleVO.getPrediceRepayDate())) {
                    errorMsgList.add(GuaranteeConstant.CMS_REPAYMENTSCHEDULE_PREDICEREPAYDATE_ISNULL);
                }
                // 校验还款本金
                if (EmptyUtils.isEmpty(scheduleVO.getRepaymentMoney())) {
                    errorMsgList.add(GuaranteeConstant.CMS_REPAYMENTSCHEDULE_REPAYMENTMONEY_ISNULL);
                }
                // 校验还款利息
                if (EmptyUtils.isEmpty(scheduleVO.getRepaymentIst())) {
                    errorMsgList.add(GuaranteeConstant.CMS_REPAYMENTSCHEDULE_REPAYMENTIST_ISNULL);
                }
                // 校验是否已还标识
                if (EmptyUtils.isEmpty(scheduleVO.getIsRepay())) {
                    errorMsgList.add(GuaranteeConstant.CMS_REPAYMENTSCHEDULE_ISREPAY_ISNULL);
                } else {
                    if (!scheduleVO.getIsRepay().equals("是") && !scheduleVO.getIsRepay().equals("否")) {
                        errorMsgList.add(GuaranteeConstant.CMS_REPAYMENTSCHEDULE_ISREPAY_ERROR);
                    }
                }
                if (EmptyUtils.isEmpty(scheduleVO.getRemark())) {
                    scheduleVO.setRemark("");
                }
                if (!ListUtils.isNullOrEmpty(errorMsgList)) {
                    scheduleVO.addErrorMsg(errorMsgList);
                }
            }
        }
    }
}
