package com.bytter.finance.domain.basedata.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.guarantee.dto.RepayPlanDTO;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.vo.RepayPlanVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleDtlVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Service
 * @Description: 还款计划子表
 * @author songkk
 * @date 2017-08-01 09:58:43
 * @version 1.0
 *
 */
@Service("repaymentScheduleService")
@Transactional
public class RepaymentScheduleService extends BaseService<RepaymentSchedule, String>
        implements ITaskService, IUserExcelVerifyHandler<RepaymentScheduleVO> {
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private CmsRepaymentScheduleService cmsRepaymentScheduleService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private RepayTypeService repayTypeService;
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(RepaymentSchedule entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RepaymentSchedule, String> getEntityDao() {
        return repaymentScheduleDao;
    }
    /**
     * 根据单号获取还款计划子表信息<br>
     *
     * @param billCode
     *            单号
     * @return
     */
    @Transactional(readOnly = true)
    public List<RepaymentScheduleVO> getRepayMentScheduleByBillCode(String billCode) {
        return repaymentScheduleDao.getRepayMentScheduleByBillCode(billCode);
    }
    /**
     * 根据单号获取还款计划子表信息<br>
     *
     * @param billCode
     *            单号
     * @return
     */
    @Transactional(readOnly = true)
    public List<RepaymentScheduleVO> getRepayScheduleByBillCode(String billCode) {
        return repaymentScheduleDao.getRepayScheduleByBillCode(billCode);
    }
    /**
     * 根据id获取还款计划子表信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<RepaymentScheduleVO> listRepayMentScheduleById(String id) {
        return repaymentScheduleDao.listRepayMentScheduleById(id);
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
            return listRepayMentScheduleById(repayPlanVo.getPlanId());
        }
        List<RepaymentScheduleVO> list = new ArrayList<RepaymentScheduleVO>();
        if (StringUtils.isNotEmpty(repayPlanVo.getBillCode())){
            List<RepaymentScheduleVO> rsList = repaymentScheduleDao.getRepayMentScheduleByBillCode(repayPlanVo.getBillCode());
            BigDecimal sumRepaymentIst = BigDecimal.ZERO;
            for (RepaymentScheduleVO vo : rsList) {
                if (null != vo.getRepaymentIst()) {
                    sumRepaymentIst = sumRepaymentIst.add(vo.getRepaymentIst());
                }
            }
            for (int i = 0, length = rsList.size(); i < length; i++) {
                RepaymentScheduleVO vo = rsList.get(i);
                vo.setSumRepaymentIst(sumRepaymentIst);
                if (i == rsList.size() - 1) {
                    vo.setLastSign(true);
                }
                list.add(vo);
            }
        }
        return list;
    }
    /**
     * 通过还款计划主表id查询还款计划明细数据
     *
     * @param repayPlanVo
     * @return
     */
    public List<RepaymentScheduleDtlVO> getRepaymentScheduleDtlByPlanId(RepayPlanVO repayPlanVo) {
        List<RepaymentScheduleVO> repaymentScheduleVOS = listRepayMentScheduleById(repayPlanVo.getPlanId());
        List<RepaymentScheduleDtlVO> repaymentScheduleDtlVOList = BeanUtils.copyListProperties(repaymentScheduleVOS,
                RepaymentScheduleDtlVO.class);
        RepayType repayType = null;
        if (repayPlanVo.getRepayType().length() == 32) {
            repayType = repayTypeService.get(repayPlanVo.getRepayType());
        }
        Boolean isRepay = false;
        for (RepaymentScheduleDtlVO repaymentScheduleDtlVO : repaymentScheduleDtlVOList) {
            isRepay = CommonConstant.YES.equals(repaymentScheduleDtlVO.getIsRepay());
            if (null == repayType) {
                repaymentScheduleDtlVO.setInerestIsEdit(false);
                repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                repaymentScheduleDtlVO.setHasDelBtn(false);
                continue;
            }
            // 利息按剩余本金
            if (BaseDataConstant.CAPITAL_AND_INTEREST_1.equals(repayType.getCapitalAndInterest())) {
                if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getCapitalFrequencyNext())) {
                    repaymentScheduleDtlVO.setInerestIsEdit(false);
                    repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                    repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                    repaymentScheduleDtlVO.setHasDelBtn(false);
                } else if (BaseDataConstant.FREQUENCY_TYPE_C.equals(repayType.getCapitalFrequencyNext())) {
                    repaymentScheduleDtlVO.setInerestIsEdit(false);
                    repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                    repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(true);
                    repaymentScheduleDtlVO.setHasDelBtn(false);
                } else {
                    repaymentScheduleDtlVO.setInerestIsEdit(false);
                    repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                    repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(true);
                    // 已还则不允许修改
                    repaymentScheduleDtlVO.setHasDelBtn(isRepay ? false : true);
                }
            } else if (BaseDataConstant.CAPITAL_AND_INTEREST_2.equals(repayType.getCapitalAndInterest())) {
                // 等额本息
                repaymentScheduleDtlVO.setInerestIsEdit(false);
                repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                repaymentScheduleDtlVO.setHasDelBtn(false);
            } else {
                // 自定义
                // 本金
                if (null != repaymentScheduleDtlVO.getRepaymentMoney()) {
                    if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getCapitalFrequencyNext())) {
                        repaymentScheduleDtlVO.setInerestIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPaymentMoneyIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                        repaymentScheduleDtlVO.setHasDelBtn(false);
                    } else if (BaseDataConstant.FREQUENCY_TYPE_C.equals(repayType.getCapitalFrequencyNext())) {
                        repaymentScheduleDtlVO.setInerestIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                        repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setHasDelBtn(false);
                    } else {
                        repaymentScheduleDtlVO.setInerestIsEdit(false);
                        repaymentScheduleDtlVO.setPaymentMoneyIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(isRepay ? false : true);
                        // 已还则不允许修改
                        repaymentScheduleDtlVO.setHasDelBtn(isRepay ? false : true);
                    }
                } else if (null != repaymentScheduleDtlVO.getRepaymentIst()) {
                    // 利息
                    if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getCapitalFrequencyNext())) {
                        repaymentScheduleDtlVO.setInerestIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                        repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                        repaymentScheduleDtlVO.setHasDelBtn(false);
                    } else {
                        repaymentScheduleDtlVO.setInerestIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                        repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(isRepay ? false : true);
                        // 已还则不允许修改
                        repaymentScheduleDtlVO.setHasDelBtn(isRepay ? false : true);
                    }
                }
            }
        }
        return repaymentScheduleDtlVOList;
    }

    /**
     * 通过还款计划主表id查询还款计划明细数据
     *
     * @param repayPlanVo
     * @return
     */
    public List<RepaymentScheduleDtlVO> getRepaymentScheduleDtlByPlanId(RepayPlanVO repayPlanVo,String status) {
        List<RepaymentScheduleVO> repaymentScheduleVOS=null;
        if(EmptyUtils.isEmpty(status)){
            repaymentScheduleVOS = listRepayMentScheduleById(repayPlanVo.getPlanId());
        }else {
            repaymentScheduleVOS = repaymentScheduleDao.listApproveRepayMentScheduleById(repayPlanVo.getPlanId());
        }
        List<RepaymentScheduleDtlVO> repaymentScheduleDtlVOList = BeanUtils.copyListProperties(repaymentScheduleVOS, RepaymentScheduleDtlVO.class);
        RepayType repayType = null;
        if (repayPlanVo.getRepayType().length() == 32) {
            repayType = repayTypeService.get(repayPlanVo.getRepayType());
        }
        Boolean isRepay = false;
        for (RepaymentScheduleDtlVO repaymentScheduleDtlVO : repaymentScheduleDtlVOList) {
            isRepay = CommonConstant.YES.equals(repaymentScheduleDtlVO.getIsRepay());
            if (null == repayType) {
                repaymentScheduleDtlVO.setInerestIsEdit(false);
                repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                repaymentScheduleDtlVO.setHasDelBtn(false);
                continue;
            }
//            利息按剩余本金
            if (BaseDataConstant.CAPITAL_AND_INTEREST_1.equals(repayType.getCapitalAndInterest())) {
                if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getCapitalFrequencyNext())) {
                    repaymentScheduleDtlVO.setInerestIsEdit(false);
                    repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                    repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                    repaymentScheduleDtlVO.setHasDelBtn(false);
                } else if (BaseDataConstant.FREQUENCY_TYPE_C.equals(repayType.getCapitalFrequencyNext())) {
                    repaymentScheduleDtlVO.setInerestIsEdit(false);
                    repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                    repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(true);
                    repaymentScheduleDtlVO.setHasDelBtn(false);
                } else {
                    repaymentScheduleDtlVO.setInerestIsEdit(false);
                    repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                    repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(true);
//                    已还则不允许修改
                    repaymentScheduleDtlVO.setHasDelBtn(isRepay ? false : true);
                }
            } else if (BaseDataConstant.CAPITAL_AND_INTEREST_2.equals(repayType.getCapitalAndInterest())) {
//                等额本息
                repaymentScheduleDtlVO.setInerestIsEdit(false);
                repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                repaymentScheduleDtlVO.setHasDelBtn(false);
            } else {
//                自定义
//                本金
                if (null != repaymentScheduleDtlVO.getRepaymentMoney()) {
                    if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getCapitalFrequencyNext())) {
                        repaymentScheduleDtlVO.setInerestIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPaymentMoneyIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                        repaymentScheduleDtlVO.setHasDelBtn(false);
                    } else if (BaseDataConstant.FREQUENCY_TYPE_C.equals(repayType.getCapitalFrequencyNext())) {
                        repaymentScheduleDtlVO.setInerestIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                        repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setHasDelBtn(false);
                    } else {
                        repaymentScheduleDtlVO.setInerestIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPaymentMoneyIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(isRepay ? false : true);
//                      已还则不允许修改
                        repaymentScheduleDtlVO.setHasDelBtn(isRepay ? false : true);
                    }
                } else if (null != repaymentScheduleDtlVO.getRepaymentIst()) {
//                    利息
                    if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getCapitalFrequencyNext())) {
                        repaymentScheduleDtlVO.setInerestIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                        repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                        repaymentScheduleDtlVO.setHasDelBtn(false);
                    } else {
                        repaymentScheduleDtlVO.setInerestIsEdit(isRepay ? false : true);
                        repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                        repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(isRepay ? false : true);
//                      已还则不允许修改
                        repaymentScheduleDtlVO.setHasDelBtn(isRepay ? false : true);
                    }
                }
            }
        }
        return repaymentScheduleDtlVOList;
    }

    /**
     * 查询总的还款利息
     *
     * @param
     * @return
     */
    @Transactional(readOnly = true)
    public BigDecimal getsumRepaymentIstByBillCode(String billCode) {
        List<RepaymentScheduleVO> list = repaymentScheduleDao.getsumRepaymentIstByBillCode(billCode);
        return list.size() > 0 ? list.get(0).getRepaymentIst() : BigDecimal.ZERO;
    }
    /**
     * 保存还款计划
     *
     * @param rp
     */
    public String save(RepayPlanVO rp, ICurrentUser user) {
        String billCode = rp.getBillCode();
        String id = "";
        if (StringUtils.isNotEmpty(billCode)) {
            // 先根据单号删除之前的还款计划
            deleteRepaymentSchedule(rp.getBillCode());
            // 根据单号查询还款计划主表id
            CmsRepaymentSchedule cms = repaymentScheduleDao.getCmsRepaymentScheduleByBillCode(billCode);
            if (cms != null) {
                // 修改还款计划主表
                id = MasterUpdate(cms, rp, user);
                // 保存还款计划子表
                subSave(id, rp, user);
            } else {// 一般不会执行这种情况
                // 新增还款计划主表
                id = MasterSave(rp, user);
                // 新增还款计划子表
                subSave(id, rp, user);
            }
        } else {
            // 新增还款计划主表
            id = MasterSave(rp, user);
            // 新增还款计划子表
            subSave(id, rp, user);
        }
        return id;
    }
    /**
     * <b>功能介绍：</b>获得贷款发放次数<br>
     * <b>日期：</b>2017年8月1日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public int getSendTime(String billCode) {
        return repaymentScheduleDao.getSendTime(billCode);
    }
    /**
     * 根据单号删除之前的还款计划子表(逻辑删除)
     */
    public void deleteRepaymentSchedule(String billCode) {
        repaymentScheduleDao.deleteRepaymentSchedule(billCode);
    }
    /**
     * 根据relation_id删除之前的还款计划子表(逻辑删除)
     */
    public void deleteRepaymentScheduleById(String relationId) {
        repaymentScheduleDao.deleteRepaymentScheduleById(relationId);
    }
    /**
     * 修改还款主表
     *
     * @param rp
     */
    public String MasterUpdate(CmsRepaymentSchedule cs, RepayPlanVO rp, ICurrentUser user) {
        cs.setBillNo(rp.getBillCode());
        if (StringUtils.isEmpty(rp.getRepayBeginDate())) {
            cs.setRepayBeginDate(DateUtils.parseDate(rp.getRepayMents().get(0).getPrediceRepayDate()));
        } else {
            cs.setRepayBeginDate(DateUtils.parseDate(rp.getRepayBeginDate()));
        }
        cs.setRepayCycel(rp.getRepayCycel());
        cs.setLoanRate(rp.getLoanRate());
        cs.setLoanDate(DateUtils.str2Date(rp.getLoanDate(), DateUtils.date_sdf()));
        cs.setRepayDate(DateUtils.str2Date(rp.getRepayDate(), DateUtils.date_sdf()));
        cs.setRepayType(rp.getRepayType());
        cs.setLoanMoney(rp.getLoanMoney());
        cs.setCreateBy(user.getLoginName());
        cs.setCreateName(user.getUsername());
        cs.setCreateDate(new Date());
        cmsRepaymentScheduleService.updateEntity(cs);
        return cs.getId();
    }
    /**
     * 保存还款主表
     *
     * @param rp
     */
    public String MasterSave(RepayPlanVO rp, ICurrentUser user) {
        CmsRepaymentSchedule cs = new CmsRepaymentSchedule();
        cs.setBillNo(rp.getBillCode());
        if (StringUtils.isEmpty(rp.getRepayBeginDate())) {
            cs.setRepayBeginDate(DateUtils.parseDate(rp.getRepayMents().get(0).getPrediceRepayDate()));
        } else {
            cs.setRepayBeginDate(DateUtils.parseDate(rp.getRepayBeginDate()));
        }
        cs.setRepayCycel(rp.getRepayCycel());// 还款周期
        cs.setLoanRate(rp.getLoanRate());// 利率
        cs.setLoanDate(DateUtils.str2Date(rp.getLoanDate(), DateUtils.date_sdf()));// 开始日期
        cs.setRepayDate(DateUtils.str2Date(rp.getRepayDate(), DateUtils.date_sdf()));// 还款日期
        cs.setRepayType(rp.getRepayType());// 还款方式
        cs.setLoanMoney(rp.getLoanMoney());// 发放金额
        cs.setCreateBy(user.getLoginName());
        cs.setCreateName(user.getUsername());
        cs.setCreateDate(new Date());
        cmsRepaymentScheduleService.save(cs);
        return cs.getId();
    }
    /**
     * 保存还款计划子表
     *
     * @param id
     */
    public void subSave(String id, RepayPlanVO rp, ICurrentUser user) {
        for (int i = 0; i < rp.getRepayMents().size(); i++) {
            RepayPlanDTO rs = rp.getRepayMents().get(i);
            // 还款计划明细表
            RepaymentSchedule entity = new RepaymentSchedule();
            if (rs.getPrediceRepayDate() != null) {
                entity.setBillCode(rp.getBillCode());// 单号
                entity.setRepayCycel(rp.getRepayCycel());// 还款周期
                entity.setPrediceRepayDate(DateUtils.str2Date(rs.getPrediceRepayDate(), DateUtils.date_sdf()));// 预计还款日
                entity.setRepaymentBeginDate(DateUtils.str2Date(rp.getRepayBeginDate(), DateUtils.date_sdf()));// 还款开始日
                entity.setRepaymentMoney(rs.getRepaymentMoney());// 还款金额
                entity.setRepaymentIst(rs.getRepaymentIst());// 利息
                entity.setCurrentRepayMoney(rs.getCurrentRepayMoney());// 本期还款金额
                entity.setSurplusMoney(rs.getSurplusMoney());// 剩余本金
                entity.setIsRepay(rs.getIsRepay());// 是否已还
                entity.setRemark(rs.getRemark());// 备注
                entity.setCreateBy(user.getLoginName());
                entity.setCreateName(user.getUsername());
                entity.setCreateDate(new Date());
                entity.setValid_Sign(CmsConstant.VALIDSIGN);// 有效标志 1为有效 -2为无效
                entity.setInternalSign(rp.getInternalSign());
                entity.setRelationId(id);
                super.saveEntity(entity);
            }
        }
    }
    /**
     * 根据单号获取还款主表信息
     */
    @Transactional(readOnly = true)
    public CmsRepaymentSchedule getCmsRepaymentScheduleByBillCode(String billCode) {
        return repaymentScheduleDao.getCmsRepaymentScheduleByBillCode(billCode);
    }

    /**
     * 根据单号获取审批中还款主表信息
     */
    @Transactional(readOnly = true)
    public CmsRepaymentSchedule getApproveScheduleByBillCode(String billCode) {
        return repaymentScheduleDao.getApproveScheduleByBillCode(billCode);
    }
    /**
     * 检查除了利率之外，有没有其他的数据（金额，日期，还款方式等）发生改变
     *
     * @param repayPlanVO
     * @return
     */
    public String checkDataChange(RepayPlanVO repayPlanVO) {
        if (repayPlanVO.getPlanId() == null) {
            return null;
        }
        CmsRepaymentSchedule cms = cmsRepaymentScheduleService.findById(repayPlanVO.getPlanId());
        if (cms == null) {
            return null;
        }
        if (dateFormat(cms.getLoanDate(), repayPlanVO.getLoanDate())
                && dateFormat(cms.getRepayDate(), repayPlanVO.getRepayDate())
                && cms.getRepayType().equals(repayPlanVO.getRepayType())
                && cms.getLoanMoney().compareTo(repayPlanVO.getLoanMoney()) == 0) {
            return repayPlanVO.getPlanId();
        } else {
            return null;
        }
    }
    /**
     * 判断两个日期是否相等
     *
     * @param date1
     * @param dateString
     * @return
     */
    public boolean dateFormat(Date date1, String dateString) {
        Date date2 = DateUtils.str2Date(dateString, DateUtils.date_sdf());
        if (DateUtils.dayDiffs(date1, date2) == 0) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    @XxlJob("RepaymentScheduleService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.LOANEXPIRATIONWARN);// 内部贷款到期提醒天数
        Integer notifyDays = configDTO.toInteger(0);
        Date endDate = DateUtils.getDateYMD();
        endDate = DateUtils.addDay(endDate, notifyDays);
        // 查询到期的发放数据
        List<RepaymentSchedule> repays = repaymentScheduleDao.listMatureRepayByLoan(endDate);// 内部贷款
        if (!EmptyUtils.isEmptyList(repays)) {
            for (RepaymentSchedule repay : repays) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("repaymentMoney", NumberUtil.formatNumeric(repay.getRepaymentMoney(), "#,##0.00"));
                map.put("repaymentIst", NumberUtil.formatNumeric(repay.getRepaymentIst(), "#,##0.00"));
                notificationService.send(repay.getId(), null, NotificationConstant.CMS.LOAN_REPAY_MATURE, map);
                logger.info("您有一笔内部贷款还款即将到期，发放Id：" + repay.getBillCode() + ",还款本金：" + repay.getRepaymentMoney()
                        + ",还款利息：" + repay.getRepaymentIst());
                // repay.setRepaySign(CommonConstant.SEND_SIGN_YES);//更改发送标识
                // repay.setValid_Sign("1");
                // this.updateEntity(repay);
            }
        }
        ConfigDTO preConfigDTO = configService.getConfig(ConfigConstant.CMS,
                BaseDataConstant.PRECATIVE_CONFIG_NOTIFYDAYS);// 委贷到期提醒天数
        Integer preNotifyDays = preConfigDTO.toInteger(0);
        Date preEndDate = DateUtils.getDateYMD();
        preEndDate = DateUtils.addDay(preEndDate, preNotifyDays);
        List<RepaymentSchedule> preRepays = repaymentScheduleDao.listMatureRepayByPrecative(preEndDate);// 委托贷款
        if (!EmptyUtils.isEmptyList(preRepays)) {
            for (RepaymentSchedule r : preRepays) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("repaymentMoney", NumberUtil.formatNumeric(r.getRepaymentMoney(), "#,##0.00"));
                map.put("repaymentIst", NumberUtil.formatNumeric(r.getRepaymentIst(), "#,##0.00"));
                notificationService.send(r.getId(), null, NotificationConstant.CMS.PRECATIVE_REPAY_MATURE, map);
                logger.info("您有一笔委托贷款还款即将到期，发放Id：" + r.getBillCode() + ",还款本金：" + r.getRepaymentMoney() + ",还款利息："
                        + r.getRepaymentIst());
                // r.setRepaySign(CommonConstant.SEND_SIGN_YES);//更改发送标识
                // r.setValid_Sign("1");
                // this.updateEntity(r);
            }
        }
    }
    /**
     * 查询所有有效的还款计划数据
     *
     * @param billCode
     * @return
     */
    public List<RepaymentSchedule> listRepaymentScheduleByBillNo(String billCode) {
        return repaymentScheduleDao.listRepaymentScheduleByBillNo(billCode);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(RepaymentScheduleVO vo, Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<RepaymentScheduleVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            Map<String, Map<String, RepaymentScheduleVO>> boxMap = new HashMap<>();
            HashMap paramsMap = (HashMap) objects[1];
            String[] params = (String[]) paramsMap.get("repayTypeId");
            RepayType repayType = StringUtils.isNotEmpty(params[0]) ? repayTypeService.get(params[0],true) : null;
            for (RepaymentScheduleVO scheduleVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 校验预计还款日
                if (EmptyUtils.isEmpty(scheduleVO.getPrediceRepayDate())) {
                    errorMsgList.add(CmsConstant.CMS_REPAYMENTSCHEDULE_PREDICEREPAYDATE_ISNULL);
                }
                // 校验还款本金
                if (EmptyUtils.isEmpty(scheduleVO.getRepaymentMoney())) {
                    errorMsgList.add(CmsConstant.CMS_REPAYMENTSCHEDULE_REPAYMENTMONEY_ISNULL);
                }
                // 校验还款利息
                if (EmptyUtils.isEmpty(scheduleVO.getRepaymentIst())) {
                    errorMsgList.add(CmsConstant.CMS_REPAYMENTSCHEDULE_REPAYMENTIST_ISNULL);
                }
                if (EmptyUtils.isEmpty(scheduleVO.getRemark())) {
                    scheduleVO.setRemark("");
                }
                if (null != repayType) {
                    if (BaseDataConstant.FREQUENCY_TYPE_P.equals(repayType.getCapitalFrequencyType())) {
                        scheduleVO.setPaymentMoneyIsEdit(true);
                    } else {
                        scheduleVO.setPaymentMoneyIsEdit(false);
                    }
                    if (BaseDataConstant.FREQUENCY_TYPE_P.equals(repayType.getInterestFrequencyType())) {
                        scheduleVO.setInerestIsEdit(true);
                    } else {
                        scheduleVO.setInerestIsEdit(false);
                    }
                    scheduleVO.setPrediceRepayDateIsEdit(true);
                }
                if (!ListUtils.isNullOrEmpty(errorMsgList)) {
                    scheduleVO.addErrorMsg(errorMsgList);
                }
            }
        }
    }

    /**
     * 根据单据号获取未还款的计划明细
     *
     * @param sendId 单据号
     * @param status 是否已还标识
     * @return 明细集合
     */
    public List<RepaymentScheduleVO> getRepaymentScheduleDtlByBillCode(String sendId, String status) {
        return repaymentScheduleDao.getRepaymentScheduleDtlByBillCode(sendId, status);
    }
}
