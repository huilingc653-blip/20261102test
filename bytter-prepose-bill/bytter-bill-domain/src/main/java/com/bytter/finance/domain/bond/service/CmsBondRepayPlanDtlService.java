package com.bytter.finance.domain.bond.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.BondConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.bond.dao.CmsBondRepayPlanDtlDao;
import com.bytter.finance.domain.bond.entity.CmsBondIssueInfo;
import com.bytter.finance.domain.bond.entity.CmsBondPay;
import com.bytter.finance.domain.bond.entity.CmsBondRepayPlan;
import com.bytter.finance.domain.bond.entity.CmsBondRepayPlanDtl;
import com.bytter.finance.domain.bond.utils.BondUtils;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanDtlVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanVo;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 债券还款计划子表
 * @date 2022-06-06 10:07:46
 */
@Service("cmsBondRepayPlanDtlService")
@Transactional
public class CmsBondRepayPlanDtlService extends BaseService<CmsBondRepayPlanDtl, String>
        implements IUserExcelVerifyHandler<CmsBondRepayPlanDtlVo> {
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRepayPlanDtlDao cmsBondRepayPlanDtlDao;
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupyService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Override
    public BaseDao<CmsBondRepayPlanDtl, String> getEntityDao() {
        return cmsBondRepayPlanDtlDao;
    }
    /**
     * 债券还款计划模块加载还款计划
     *
     * @param registerId
     *            债劵注册id
     * @return
     */
    public List<CmsBondRepayPlanDtlVo> getByRegisterId(String registerId) {
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlDao.getByRegisterId(registerId);
        return list;
    }
    /**
     * 债券还款计划模块加载还款计划
     *
     * @param issueId
     *            债券发行Id
     * @return
     */
    public List<CmsBondRepayPlanDtlVo> listPlanDtlByIssueId(String issueId) {
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlDao.getByIssueId(issueId);
        return list;
    }
    /**
     * 债券还款计划模块加载还款计划
     *
     * @param planId
     *            计划主表Id
     * @return
     */
    public List<CmsBondRepayPlanDtlVo> getByPlanId(String planId) {
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlDao.getByPlanId(planId);
        return list;
    }
    public CmsBondRepayPlanDtlVo getByPayId(String payId) {
        return cmsBondRepayPlanDtlDao.getByPayId(payId);
    }
    public void subSave(CmsBondRepayPlanVo vo, Map<String, List<CmsBondRepayPlanDtlVo>> planDtlVoMap,
            Map<String, CmsBondRepayPlan> cmsBondRepayPlanMap, List<String> planIds, ICurrentUser user) {
        // 查询现有还款计划数据
        Map<String, CmsBondRepayPlanDtlVo> planDtlVoAllMap = this.getBondRepayPlanIdMap(vo.getRegisterId());
        // 先根据债券发行表id号删除之前的还款计划
        deleteBondRepayPlanDtl(planIds);
        for (String key : planDtlVoMap.keySet()) {
            List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList = planDtlVoMap.get(key);
            if (EmptyUtils.isEmptyList(cmsBondRepayPlanDtlVoList)) {
                continue;
            }
            for (int i = 0; i < cmsBondRepayPlanDtlVoList.size(); i++) {
                CmsBondRepayPlanDtlVo delVo = cmsBondRepayPlanDtlVoList.get(i);
                if (CmsConstant.REPAYMENT_SCHEDULE_ISREPAYYES.equals(delVo.getIsRepay())) {
                    CmsBondIssueInfo issueInfo = cmsBondIssueInfoService.get(vo.getIssueId(), true);
                    // 未还本金
                    BigDecimal unpaidMoney = cmsBondPayService.completeSendBalance(vo.getIssueId(), null);
                    // 判断之前的是不是已经存在,并且状态为已还
                    if (StringUtils.isNotEmpty(planDtlVoAllMap) && planDtlVoAllMap.containsKey(delVo.getId())) {
                        CmsBondRepayPlanDtlVo oldPlanDtlVO = planDtlVoAllMap.get(delVo.getId());
                        if (CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO.equals(oldPlanDtlVO.getIsRepay())) {
                            CmsBondRepayPlanDtl entity = new CmsBondRepayPlanDtl();
                            CmsBondRepayPlan cmsBondRepayPlan = cmsBondRepayPlanMap.get(delVo.getBondIssueNum());
                            CmsBondPay cmsBondPay = addCmsBondPayInfo(vo, user, delVo, cmsBondRepayPlan);
                            String planId = cmsBondRepayPlan.getId();
                            entity.setPayId(cmsBondPay.getId());
                            entity.setPlanId(planId);
                            entity.setRepayDate(delVo.getRepayDate());// 还款日期
                            entity.setPlanRepayDate(delVo.getPlanRepayDate());// 计划还款日
                            entity.setRepayMoney(delVo.getRepayMoney());// 还款本金值
                            entity.setRemainMoney(delVo.getRemainMoney());// 剩余本金值
                            entity.setRepayAmount(delVo.getRepayAmount());// 本次还款金额
                            entity.setRepayRate(delVo.getRepayRate());// 本次偿还占发行总额比例%
                            entity.setIsRepay(delVo.getIsRepay());// 是否已还
                            entity.setValidSign(CmsConstant.VALIDSIGN);// 有效标志
                            entity.setRmk(delVo.getRmk());// 备注
                            entity.setCreateBy(user.getLoginName());
                            entity.setCreateDate(new Date());
                            this.saveOrUpdate(entity);
                            // 全部还款完则释放额度
                            if (BigDecimal.ZERO.compareTo(unpaidMoney.subtract(delVo.getRepayAmount())) == 0) {
                                // 如果挂接了保证金,则修改保证金占用信息的状态为"释放"
                                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(issueInfo.getId(),
                                        issueInfo.getBondIssueNum());
                                if (dto != null) {
                                    // 不为空说明挂接了保证金
                                    dto.setStatus(FcBondConstant.STATUE_RELEASE);
                                    dto.setUpdateBy(user.getLoginName());
                                    dto.setUpdateDate(DateUtils.getDate());
                                    fcBondHookService.updateFcBondApply(dto);
                                }
                                // 抵质押物处理,释放冻结额度
                                if (EmptyUtils.isNotEmpty(issueInfo.getIsHookPledge())
                                        && CmsConstant.IS_TRUE.equals(issueInfo.getIsHookPledge())) {
                                    pledgeOccupyService.releasePledgeOccupy(issueInfo.getId(),
                                            issueInfo.getBondIssueNum());
                                }
                                // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                                BondUtils.handleGua(issueInfo, null, guaContractOccupyService, 3,
                                        issueInfo.getGuaranteeContract());
                            }
                        }
                    } else {
                        if (delVo.getPlanRepayDate() != null) {
                            CmsBondRepayPlanDtl entity = new CmsBondRepayPlanDtl();
                            CmsBondRepayPlan cmsBondRepayPlan = cmsBondRepayPlanMap.get(delVo.getBondIssueNum());
                            CmsBondPay cmsBondPay = addCmsBondPayInfo(vo, user, delVo, cmsBondRepayPlan);
                            String planId = cmsBondRepayPlanMap.get(delVo.getBondIssueNum()).getId();
                            entity.setPlanId(planId);
                            entity.setPayId(cmsBondPay.getId());
                            entity.setRepayDate(delVo.getRepayDate());// 还款日期
                            entity.setPlanRepayDate(delVo.getPlanRepayDate());// 计划还款日
                            entity.setRepayMoney(delVo.getRepayMoney());// 还款本金值
                            entity.setRemainMoney(delVo.getRemainMoney());// 剩余本金值
                            entity.setRepayAmount(delVo.getRepayAmount());// 本次还款金额
                            entity.setRepayRate(delVo.getRepayRate());// 本次偿还占发行总额比例%
                            entity.setIsRepay(delVo.getIsRepay());// 是否已还
                            entity.setValidSign(CmsConstant.VALIDSIGN);// 有效标志
                            entity.setRmk(delVo.getRmk());// 备注
                            entity.setCreateBy(user.getLoginName());
                            entity.setCreateDate(new Date());
                            this.saveOrUpdate(entity);
                            // 全部还款完则释放额度
                            if (BigDecimal.ZERO.compareTo(unpaidMoney.subtract(delVo.getRepayAmount())) == 0) {
                                // 如果挂接了保证金,则修改保证金占用信息的状态为"释放"
                                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(issueInfo.getId(),
                                        issueInfo.getBondIssueNum());
                                if (dto != null) {
                                    // 不为空说明挂接了保证金
                                    dto.setStatus(FcBondConstant.STATUE_RELEASE);
                                    dto.setUpdateBy(user.getLoginName());
                                    dto.setUpdateDate(DateUtils.getDate());
                                    fcBondHookService.updateFcBondApply(dto);
                                }
                                // 抵质押物处理,释放冻结额度
                                if (EmptyUtils.isNotEmpty(issueInfo.getIsHookPledge())
                                        && CmsConstant.IS_TRUE.equals(issueInfo.getIsHookPledge())) {
                                    pledgeOccupyService.releasePledgeOccupy(issueInfo.getId(),
                                            issueInfo.getBondIssueNum());
                                }
                                // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                                BondUtils.handleGua(issueInfo, null, guaContractOccupyService, 3,
                                        issueInfo.getGuaranteeContract());
                            }
                        }
                    }
                } else {
                    if (delVo.getPlanRepayDate() != null) {
                        CmsBondRepayPlanDtl entity = new CmsBondRepayPlanDtl();
                        String planId = cmsBondRepayPlanMap.get(delVo.getBondIssueNum()).getId();
                        entity.setPlanId(planId);
                        entity.setRepayDate(delVo.getRepayDate());// 还款日期
                        entity.setPlanRepayDate(delVo.getPlanRepayDate());// 计划还款日
                        entity.setRepayMoney(delVo.getRepayMoney());// 还款本金值
                        entity.setRemainMoney(delVo.getRemainMoney());// 剩余本金值
                        entity.setRepayAmount(delVo.getRepayAmount());// 本次还款金额
                        entity.setRepayRate(delVo.getRepayRate());// 本次偿还占发行总额比例%
                        entity.setIsRepay(delVo.getIsRepay());// 是否已还
                        entity.setValidSign(CmsConstant.VALIDSIGN);// 有效标志
                        entity.setRmk(delVo.getRmk());// 备注
                        entity.setCreateBy(user.getLoginName());
                        entity.setCreateDate(new Date());
                        this.saveOrUpdate(entity);
                    }
                }
            }
        }
    }
    /**
     * 新增还款信息
     *
     * @param vo
     *            还款计划vo
     * @param user
     *            当前用户
     * @param delVo
     *            还款计划明细vo
     * @param cmsBondRepayPlan
     *            还款计划
     * @return 还款处理信息
     */
    private CmsBondPay addCmsBondPayInfo(CmsBondRepayPlanVo vo, ICurrentUser user, CmsBondRepayPlanDtlVo delVo,
            CmsBondRepayPlan cmsBondRepayPlan) {
        // 新增还款处理数据
        CmsBondPay cmsBondPay = new CmsBondPay();
        cmsBondPay.setRegisterId(cmsBondRepayPlan.getRegisterId());
        cmsBondPay.setIssueId(cmsBondRepayPlan.getIssueId());
        cmsBondPay.setOnlineSign(CmsConstant.IS_FALSE);
        cmsBondPay.setRepayMoney(delVo.getRepayAmount());
        cmsBondPay.setRepayTotalMoney(delVo.getRepayAmount());
        cmsBondPay.setStatus(CmsConstant.APPROVED);
        cmsBondPay.setIsPayInterest(CmsConstant.IS_FALSE);
        cmsBondPay.setPayDate(delVo.getRepayDate());
        cmsBondPay.setCreateBy(user.getLoginName());
        cmsBondPay.setCreateDate(new Date());
        cmsBondPayService.saveEntity(cmsBondPay);
        return cmsBondPay;
    }
    /**
     * 债券发行保存还款计划明细数据
     *
     * @param vo
     *            还款计划明细实体
     * @param user
     *            用户
     */
    public void issueSave(CmsBondRepayPlanVo vo, ICurrentUser user) {
        // 根据债券发行表id号删除之前的还款计划
        deleteBondRepayPlanDtl(Lists.newArrayList(vo.getId()));
        List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtls = vo.getCmsBondRepayPlanDtls();
        ArrayList<CmsBondRepayPlanDtl> bondRepayPlanDtlList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmsBondRepayPlanDtls)) {
            for (CmsBondRepayPlanDtlVo planDtl : cmsBondRepayPlanDtls) {
                if (planDtl.getRepayDate() == null) {
                    continue;
                }
                CmsBondRepayPlanDtl cmsBondRepayPlan = BeanUtils.copyNewInstanceProperties(planDtl,
                        CmsBondRepayPlanDtl.class);
                cmsBondRepayPlan.setPlanId(vo.getId());
                cmsBondRepayPlan.setValidSign(CmsConstant.VALIDSIGN_1);
                cmsBondRepayPlan.setCreateBy(user.getId());
                cmsBondRepayPlan.setCreateDate(new Date());
                bondRepayPlanDtlList.add(cmsBondRepayPlan);
            }
        }
        if (!CollectionUtils.isEmpty(bondRepayPlanDtlList)) {
            this.batchSave(bondRepayPlanDtlList);
        }
    }
    private Map<String, CmsBondRepayPlanDtlVo> getBondRepayPlanIdMap(String registerId) {
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlDao.getByRegisterId(registerId);
        Map<String, CmsBondRepayPlanDtlVo> map = new HashMap<>();
        if (!EmptyUtils.isEmptyList(list)) {
            for (CmsBondRepayPlanDtlVo vo : list) {
                map.put(vo.getId(), vo);
            }
        }
        return map;
    }
    public void deleteBondRepayPlanDtl(List<String> planIds) {
        cmsBondRepayPlanDtlDao.deleteBondRepayPlanDtl(planIds);
    }
    /**
     * 导入校验方法
     *
     * @return
     */
    @Override
    public ExcelVerifyHanlderResult verifyHandler(CmsBondRepayPlanDtlVo cmsBondRepayPlanDtlVo, Object... objects) {
        return null;
    }
    /**
     * 导入校验方法
     *
     * @param list
     *            当前集合对象 最后将有错误的信息调用T里的addErrorMsg方法
     */
    @Override
    @Transactional(readOnly = true)
    public void verifyHandler(List<CmsBondRepayPlanDtlVo> list, Object... obj) {
        if (!ListUtils.isNullOrEmpty(list)) {
            String bondIssueNum = "";
            if (!EmptyUtils.isEmpty(obj) && !EmptyUtils.isEmpty(obj[1])) {
                Map<String, String[]> params = (Map<String, String[]>) obj[1];
                if (params.size() > 0) {
                    bondIssueNum = params.get("bondIssueNum")[0];
                }
            }
            Map<String, Map<String, CmsBondRepayPlanDtlVo>> map = new HashMap<>();
            for (CmsBondRepayPlanDtlVo vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 校验债券发行编号
                if (EmptyUtils.isEmpty(vo.getBondIssueNum())) {
                    errorMsgList.add(BondConstant.CMS_CMSBONDREPAYPLANDTL_BONDISSUENUM_ISNULL);
                } else {
                    if (StringUtils.isNotEmpty(bondIssueNum) && !bondIssueNum.equals(vo.getBondIssueNum())) {
                        errorMsgList.add("债券发行编号需与页面保持一致");
                    }
                }
                // 校验计划还款日
                if (EmptyUtils.isEmpty(vo.getPlanRepayDate())) {
                    errorMsgList.add(BondConstant.CMS_CMSBONDREPAYPLANDTL_PLANREPAYDATE_ISNULL);
                }
                // 校验还款本金值
                if (EmptyUtils.isEmpty(vo.getRepayMoney())) {
                    errorMsgList.add(BondConstant.CMS_CMSBONDREPAYPLANDTL_REPAYMONEY_ISNULL);
                }
                // 校验本次还款金额
                if (EmptyUtils.isEmpty(vo.getRepayAmount())) {
                    errorMsgList.add(BondConstant.CMS_CMSBONDREPAYPLANDTL_REPAYAMOUNT_ISNULL);
                }
                // 校验是否已还/抵扣
                if (EmptyUtils.isEmpty(vo.getIsRepayName())) {
                    errorMsgList.add(BondConstant.CMS_CMSBONDREPAYPLANDTL_ISREPAY_ISNULL);
                } else {
                    if (vo.getIsRepayName().equals("是")) {
                        vo.setIsRepay(CmsConstant.IS_TRUE);
                    } else if (vo.getIsRepayName().equals("否")) {
                        vo.setIsRepay(CmsConstant.IS_FALSE);
                    } else {
                        errorMsgList.add(BondConstant.CMS_CMSBONDREPAYPLANDTL_ISREPAY_ERROR);
                    }
                }
                if (EmptyUtils.isEmpty(vo.getRmk())) {
                    vo.setRmk("");
                }
                if (!ListUtils.isNullOrEmpty(errorMsgList)) {
                    vo.addErrorMsg(errorMsgList);
                }
            }
        }
    }
    /**
     * 根据id修改付款处理id
     *
     * @param id
     *            id
     * @param payId
     *            付款处理id
     */
    public void updatePayId(String id, String payId) {
        cmsBondRepayPlanDtlDao.updatePayId(id, payId);
    }
}
