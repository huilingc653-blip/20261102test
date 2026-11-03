package com.bytter.finance.domain.lease.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.LeaseConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.lease.dao.LeaseCapitalPlanDelDao;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlanDel;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanDelVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanQueryVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 租赁计划子表
 * @date 2022-04-02 11:09:43
 */
@Service("leaseCapitalPlanDelService")
@Transactional
public class LeaseCapitalPlanDelService extends BaseService<LeaseCapitalPlanDel, String>
        implements IUserExcelVerifyHandler<LeaseCapitalPlanDelVO> {
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private LeasePayService leasePayService;
    @Autowired
    private LeaseCapitalPlanDelDao leaseCapitalPlanDelDao;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<LeaseCapitalPlanDel, String> getEntityDao() {
        return leaseCapitalPlanDelDao;
    }
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput) {
        // 获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(ResourceConstant.CMS_LEASE_LEASECAPITALPLANQUERY_QUERY,
                dataTablesInput.getCurrentUserId());
        DataTablesOutput<LeaseCapitalPlanQueryVO> output = leaseCapitalPlanDelDao.getDataTablesQuery(dataTablesInput, corpAuthList);
        setDictField(output.getData());
        return output;
    }

    private void setDictField(List<?> vos) {
        Map<String, DictDTO> dictDTOMap = dictService.listMap("sf_yn");
        for (Object vo : vos) {
            if(vo instanceof LeaseCapitalPlanQueryVO){
                LeaseCapitalPlanQueryVO queryVO = (LeaseCapitalPlanQueryVO) vo;
                queryVO.setIsRepayName(dictDTOMap.getOrDefault(queryVO.getIsRepay(), DictDTO.EMPTY).getName());
            }else if(vo instanceof LeaseCapitalPlanDelVO){
                LeaseCapitalPlanDelVO planDelVO = (LeaseCapitalPlanDelVO) vo;
                planDelVO.setIsRepayName(dictDTOMap.getOrDefault(planDelVO.getIsRepay(), DictDTO.EMPTY).getName());
            }
        }
    }

    public LeaseCapitalPlanDelVO getInfoById(String id) {
        return leaseCapitalPlanDelDao.getInfoById(id);
    }
    public List<LeaseCapitalPlanDelVO> getByLeasePlanId(String leasePlanId) {
        List<LeaseCapitalPlanDelVO> list = leaseCapitalPlanDelDao.getByLeasePlanId(leasePlanId);
        return list;
    }
    public Map<String, LeaseCapitalPlanDelVO> getLeasePlanIdMap(String leasePlanId) {
        List<LeaseCapitalPlanDelVO> list = leaseCapitalPlanDelDao.getByLeasePlanId(leasePlanId);
        Map<String, LeaseCapitalPlanDelVO> map = new HashMap<>();
        if (!EmptyUtils.isEmptyList(list)) {
            for (LeaseCapitalPlanDelVO vo : list) {
                map.put(vo.getId(), vo);
            }
        }
        return map;
    }
    /**
     * 根据租赁id查询租金计划明细
     *
     * @param leaseId
     *            租赁id
     * @return 租金计划明细
     */
    public List<LeaseCapitalPlanDelVO> getInfoByLeaseId(String leaseId) {
        List<LeaseCapitalPlanDelVO> list = leaseCapitalPlanDelDao.getInfoByLeaseId(leaseId);
        setDictField(list);
        return list;
    }
    /**
     * 根据单号删除之前的还款计划子表(逻辑删除)
     */
    public void deleteLeaseCapitalPlanDel(String leasePlanId) {
        leaseCapitalPlanDelDao.deleteLeaseCapitalPlanDel(leasePlanId);
    }
    /**
     * 保存租金计划子表
     *
     * @param id
     */
    public void subSave(String id, LeaseCapitalPlanVO vo, ICurrentUser user) {
        if (EmptyUtils.isEmptyList(vo.getLeaseCapitalPlanDels())) {
            throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASECAPITALPLAN_EMPTY);
        }
        // 查询现有租金计划数据
        Map<String, LeaseCapitalPlanDelVO> planDelVOMap = this.getLeasePlanIdMap(id);
        // 先根据租赁计划主表id号删除之前的租金计划
        this.deleteLeaseCapitalPlanDel(id);
        for (int i = 0; i < vo.getLeaseCapitalPlanDels().size(); i++) {
            LeaseCapitalPlanDelVO delVo = vo.getLeaseCapitalPlanDels().get(i);
            // 查询支付申请数据
            /*
             * LeasePayVO leasePayVO = leasePayService.getByLeasePlanDelId(delVo.getId()); if
             * (!EmptyUtils.isEmpty(leasePayVO) && CmsConstant.REPAYMENT_SCHEDULE_ISREPAYYES.equals(delVo.getIsRepay()))
             * { continue; }
             */
            if (CmsConstant.REPAYMENT_SCHEDULE_ISREPAYYES.equals(delVo.getIsRepay())) {
                // 判断之前的是不是已经存在,并且状态为已还
                if (StringUtils.isNotEmpty(planDelVOMap) && planDelVOMap.containsKey(delVo.getId())) {
                    LeaseCapitalPlanDelVO oldPlanDelVO = planDelVOMap.get(delVo.getId());
                    if (CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO.equals(oldPlanDelVO.getIsRepay())) {
                        LeaseCapitalPlanDel entity = new LeaseCapitalPlanDel();
                        entity.setLeasePlanId(id);
                        entity.setPayDate(delVo.getPayDate());// 付租日期
                        entity.setPayPlanDate(delVo.getPayPlanDate());// 计划付租日
                        entity.setLeaseMonry(delVo.getLeaseMonry());// 租赁本金
                        entity.setLeaseInterest(delVo.getLeaseInterest());// 租赁利息
                        entity.setThisMoney(delVo.getThisMoney());// 本期租金
                        entity.setThisCost(delVo.getThisCost());// 本期费用
                        entity.setBailMoney(delVo.getBailMoney());// 保证金金额
                        entity.setSurplusMoney(delVo.getSurplusMoney());// 剩余本金
                        entity.setIsRepay(delVo.getIsRepay());// 是否已还
                        entity.setValidSign(CmsConstant.VALIDSIGN);// 有效标志
                        entity.setRmk(delVo.getRmk());// 备注
                        entity.setCreateBy(user.getLoginName());
                        entity.setCreateDate(new Date());
                        this.saveOrUpdate(entity);
                    }
                } else {
                    if (delVo.getPayPlanDate() != null) {
                        LeaseCapitalPlanDel entity = new LeaseCapitalPlanDel();
                        entity.setLeasePlanId(id);
                        entity.setPayDate(delVo.getPayDate());// 付租日期
                        entity.setPayPlanDate(delVo.getPayPlanDate());// 计划付租日
                        entity.setLeaseMonry(delVo.getLeaseMonry());// 租赁本金
                        entity.setLeaseInterest(delVo.getLeaseInterest());// 租赁利息
                        entity.setThisMoney(delVo.getThisMoney());// 本期租金
                        entity.setThisCost(delVo.getThisCost());// 本期费用
                        entity.setBailMoney(delVo.getBailMoney());// 保证金金额
                        entity.setSurplusMoney(delVo.getSurplusMoney());// 剩余本金
                        entity.setIsRepay(delVo.getIsRepay());// 是否已还
                        entity.setValidSign(CmsConstant.VALIDSIGN);// 有效标志
                        entity.setRmk(delVo.getRmk());// 备注
                        entity.setCreateBy(user.getLoginName());
                        entity.setCreateDate(new Date());
                        this.saveOrUpdate(entity);
                    }
                }
            } else {
                if (delVo.getPayPlanDate() != null) {
                    LeaseCapitalPlanDel entity = new LeaseCapitalPlanDel();
                    entity.setLeasePlanId(id);
                    entity.setPayDate(delVo.getPayDate());// 付租日期
                    entity.setPayPlanDate(delVo.getPayPlanDate());// 计划付租日
                    entity.setLeaseMonry(delVo.getLeaseMonry());// 租赁本金
                    entity.setLeaseInterest(delVo.getLeaseInterest());// 租赁利息
                    entity.setThisMoney(delVo.getThisMoney());// 本期租金
                    entity.setThisCost(delVo.getThisCost());// 本期费用
                    entity.setBailMoney(delVo.getBailMoney());// 保证金金额
                    entity.setSurplusMoney(delVo.getSurplusMoney());// 剩余本金
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
    /**
     * 导入校验方法
     *
     * @return
     */
    @Override
    public ExcelVerifyHanlderResult verifyHandler(LeaseCapitalPlanDelVO leaseCapitalPlanDelVo, Object... objects) {
        return null;
    }
    /**
     * 导入校验方法
     *
     * @param list
     *            当前集合对象 最后将有错误的信息调用T里的addErrorMsg方法
     */
    @Override
    public void verifyHandler(List<LeaseCapitalPlanDelVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            Map<String, Map<String, LeaseCapitalPlanDelVO>> map = new HashMap<>();
            for (LeaseCapitalPlanDelVO vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 校验计划付租日
                if (EmptyUtils.isEmpty(vo.getPayPlanDate())) {
                    errorMsgList.add(LeaseConstant.CMS_LEASECAPITALPLANDEL_PAYPLANDATE_ISNULL);
                }
                // 校验租赁本金
                if (EmptyUtils.isEmpty(vo.getLeaseMonry())) {
                    errorMsgList.add(LeaseConstant.CMS_LEASECAPITALPLANDEL_LEASEMONRY_ISNULL);
                }
                // 校验租赁利息
                if (EmptyUtils.isEmpty(vo.getLeaseInterest())) {
                    errorMsgList.add(LeaseConstant.CMS_LEASECAPITALPLANDEL_LEASEINTEREST_ISNULL);
                }
                // 校验本期费用
                if (EmptyUtils.isEmpty(vo.getThisCost())) {
                    errorMsgList.add(LeaseConstant.CMS_LEASECAPITALPLANDEL_THISCOST_ISNULL);
                }
                // 校验保证金
                if (EmptyUtils.isEmpty(vo.getBailMoney())) {
                    errorMsgList.add(LeaseConstant.CMS_LEASECAPITALPLANDEL_BAILMONEY_ISNULL);
                }
                // 校验是否已还/抵扣
                if (EmptyUtils.isEmpty(vo.getIsRepayName())) {
                    errorMsgList.add(LeaseConstant.CMS_LEASECAPITALPLANDEL_ISREPAY_ISNULL);
                } else {
                    if (vo.getIsRepayName().equals("是")) {
                        vo.setIsRepay(CmsConstant.IS_TRUE);
                    } else if (vo.getIsRepayName().equals("否")) {
                        vo.setIsRepay(CmsConstant.IS_FALSE);
                    } else {
                        errorMsgList.add(LeaseConstant.CMS_LEASECAPITALPLANDEL_ISREPAY_ERROR);
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
}
