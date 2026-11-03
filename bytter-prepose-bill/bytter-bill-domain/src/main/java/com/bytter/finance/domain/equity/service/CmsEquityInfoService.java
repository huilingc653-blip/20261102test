package com.bytter.finance.domain.equity.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.EquityConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.directFinance.dao.DfIntermediateCostDao;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.equity.dao.CmsEquityInfoDao;
import com.bytter.finance.domain.equity.dao.CmsEquityInfoDetailDao;
import com.bytter.finance.domain.equity.entity.CmsEquityInfo;
import com.bytter.finance.domain.equity.entity.CmsEquityInfoDetail;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoVO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 股权融资登记
 *
 * @author yangl
 */
@Service("cmsEquityInfoService")
@WorkFlow(CmsWorkFlowConstant.CMS_EQUITY_INFO)
@DubboService(interfaceClass = IWorkFlowListener.class, group = CmsWorkFlowConstant.CMS_EQUITY_INFO)
@Transactional
public class CmsEquityInfoService extends BaseService<CmsEquityInfo, String>
        implements IWorkFlowListener, IUserExcelVerifyHandler<CmsEquityInfoDetailVO> {
    @Autowired
    private CmsEquityInfoDao cmsEquityInfoDao;
    @Autowired
    private CmsEquityInfoDetailDao cmsEquityInfoDetailDao;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private DfIntermediateCostDao dfIntermediateCostDao;
    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 根据id获取信息
     */
    public CmsEquityInfoVO getById(String id) {
        return cmsEquityInfoDao.getById(id);
    }

    /**
     * 融资申请查询列表 - 数据(通过)
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataEquityPassTable(DataTablesInput dataTablesInput, ICurrentUser user, String resource,
                                                   List<String> bizStatus) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
        return cmsEquityInfoDao.getDataEquityPassTable(dataTablesInput, corpAuth, bizStatus);
    }

    /**
     * 融资申请查询列表 - 数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataEquityTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_EQUITY_INFO_QUERY, user.getId());
        return cmsEquityInfoDao.getDataEquityTable(dataTablesInput, corpAuth);
    }

    /**
     * 融资申请查询列表 - 数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataEquityQueryTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_EQUITY_QUERY_VIEW, user.getId());
        return cmsEquityInfoDao.getDataEquityQueryTable(dataTablesInput, corpAuth);
    }

    /**
     * 融资明细列表 - 数据
     */
    @Transactional(readOnly = true)
    public List<CmsEquityInfoDetailVO> getDataEquityDetailTable(List<String> ids) {
        return cmsEquityInfoDao.getDataEquityDetailTable(ids);
    }

    /**
     * 融资明细列表 - 数据
     */
    @Transactional(readOnly = true)
    public List<CmsEquityInfoDetailVO> getDetails(String id, List<String> bizStatuss) {
        List<CmsEquityInfoDetailVO> detailVOS = cmsEquityInfoDetailDao.getDetails(id, bizStatuss);
        return detailVOS;
    }

    /**
     * 融资申请查询列表 - 数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDatatableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_EQUITY_INFO,
                dataTablesInput);
        DataTablesOutput dp = cmsEquityInfoDao.getDatatableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }

    /**
     * <b>功能介绍：</b>生成股权融资单号<br>
     * <b>日期：</b>2022年4月8日<br>
     * <b>作者：</b>tongqr<br>
     *
     * @version 1.0
     */
    public String getEquityCode() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_EQUITY_CODE, EquityConstant.CMS_EQUITY_INFO_DETAIL_CODE);
    }

    /**
     * 功能介绍 : 提交或暂存<br>
     *
     * @param vo
     * @param saveType 1提交，0暂存 <b>日期：</b>2000-06-10<br>
     *                 <br>
     * @author tongqr
     */
    public void tempOrSubmit(CmsEquityInfoVO vo, String saveType, ICurrentUser user, String ids) {
        CmsEquityInfo cmsEquityInfo = new CmsEquityInfo();
        BeanUtils.copyNotNullProperties(vo, cmsEquityInfo);
        if ("1".equals(saveType)) {// 提交
            cmsEquityInfo.setStatus(CmsConstant.COMMITAPPROVE);
        } else {// 暂存
            cmsEquityInfo.setStatus(CmsConstant.TEMP);
        }
        CmsEquityInfo info = null;
        if (StringUtils.isEmpty(cmsEquityInfo.getId())) {
            info = cmsEquityInfo;
            this.save(info, user);
        } else {
            info = this.get(cmsEquityInfo.getId());
            BeanUtils.copyNotNullProperties(cmsEquityInfo, info);
            this.update(info, user);
        }
        String[] idArray = ids.split(",");
        List<String> idList = new ArrayList<>();
        for (String id : idArray) {
            idList.add(id);
        }
        cmsEquityInfoDetailDao.updateEquityId(idList, cmsEquityInfo.getId());
        cmsEquityInfoDetailDao.deleteOtherDetail(); // 删除没有挂接的明细
        // 保存第三方费率信息
        if (vo.getCostTypeDtl().size() > 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("id", vo.getId());
            dfIntermediateCostDao.deleteIntermediateCost(param);
            List<DfIntermediateCost> costList = new ArrayList<>();
            for (DfIntermediateCost cost : vo.getCostTypeDtl()) {
                if (!cost.getCostTypeId().trim().equals("")) {
                    cost.setDirectFinanceId(info.getId());
                    costList.add(cost);
                }
            }
            dfIntermediateCostDao.batchSave(costList);
        }
        // notificationService.complete(NotificationConstant.CMS.EQUITY_INFO, info.getId(), user.getId());
        if ("1".equals(saveType)) {// 提交 启动审批流程
            getStartHandler().handler(CmsWorkFlowConstant.CMS_EQUITY_INFO, info.getId(), info, info.getCorpId(),
                    user.getId());
        }
    }

    /**
     * 功能介绍 : 修改或者新增<br>
     */
    public AjaxJson saveDetail(CmsEquityInfoDetail cmsEquityInfoDetail) {
        AjaxJson ajaxJson = new AjaxJson();
        if (StringUtils.isEmpty(cmsEquityInfoDetail.getId())) {
            cmsEquityInfoDetailDao.save(cmsEquityInfoDetail);
        } else {
            cmsEquityInfoDetailDao.update(cmsEquityInfoDetail);
        }
        ajaxJson.setObj(cmsEquityInfoDetail.getId());
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }

    /*
     * 根据股权编号得到对应的股权明细
     */
    public CmsEquityInfoDetailVO getDetail(String equityCode) {
        CmsEquityInfoDetailVO vo = cmsEquityInfoDetailDao.getDetailByCode(equityCode);
        if (!EmptyUtils.isEmpty(vo)) {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("cms.equity.financingWay", "cms.equity.invWay", "cms.equity.detail.bizStatus");
            // 融资方式
            Optional.ofNullable(vo.getFinancingWay()).ifPresent(financingWay -> {
                Map<String, DictDTO> dictMap = listDictMap.get("cms.equity.financingWay");
                Optional.ofNullable(dictMap).flatMap(map -> Optional.ofNullable(map.get(financingWay))).ifPresent(dict -> vo.setFinancingWayName(dict.getName()));
            });
            // 出资方式
            Optional.ofNullable(vo.getInvWay()).ifPresent(invWay -> {
                Map<String, DictDTO> dictMap = listDictMap.get("cms.equity.invWay");
                Optional.ofNullable(dictMap).flatMap(map -> Optional.ofNullable(map.get(invWay))).ifPresent(dict -> vo.setInvWayName(dict.getName()));
            });
            // 业务状态
            Optional.ofNullable(vo.getBizStatus()).ifPresent(bizStatus -> {
                Map<String, DictDTO> dictMap = listDictMap.get("cms.equity.detail.bizStatus");
                Optional.ofNullable(dictMap).flatMap(map -> Optional.ofNullable(map.get(bizStatus))).ifPresent(dict -> vo.setBizStatusName(dict.getName()));
            });
        }
        return vo;
    }

    public void deleteDetail(String id) {
        cmsEquityInfoDetailDao.delete(id);
    }

    /**
     * 功能介绍 : 删除融资租赁申请<br>
     */
    public void deleteLease(String id, ICurrentUser user) {
        CmsEquityInfo info = this.get(id);
        info.setStatus(CmsConstant.DELETED);
        List<CmsEquityInfoDetailVO> detailVOS = cmsEquityInfoDetailDao.getDetails(id, null);
        for (CmsEquityInfoDetailVO detailVO : detailVOS) {
            cmsEquityInfoDetailDao.delete(detailVO.getId());
        }
        this.update(info, user);
    }

    /**
     * 功能介绍 : 根据股权id查找对应的股权明细id<br>
     */
    public String getDetailIds(String equityId) {
        String detailIds = null;
        List<CmsEquityInfoDetailVO> detailVOS = cmsEquityInfoDetailDao.getDetails(equityId, null);
        for (CmsEquityInfoDetailVO cmsEquityInfoDetailVO : detailVOS) {
            if (detailIds == null) {
                detailIds = cmsEquityInfoDetailVO.getId();
            } else {
                detailIds = detailIds + "," + cmsEquityInfoDetailVO.getId();
            }
        }
        return detailIds;
    }

    /**
     * 取费率信息
     */
    public List<DfIntermediateCostVO> listIntermediateCostByDfId(String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("directFinanceId", id);
        return dfIntermediateCostDao.listIntermediateCostByDfId(param);
    }

    @Override
    public BaseDao<CmsEquityInfo, String> getEntityDao() {
        return cmsEquityInfoDao;
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        CmsEquityInfo info = this.get(businessKey, true);
        if (result) {// 审批通过
            info.setStatus(CmsConstant.APPROVED);
            cmsEquityInfoDetailDao.updateBizStatus(info.getId(), CmsConstant.EQUITY_DETAIL_BIZSTATUS_0);
        } else {// 打回
            info.setStatus(CmsConstant.BACK);
            // String submitUser = StringUtils.isEmpty(info.getUpdateBy()) ? info.getCreateBy() :
            // info.getUpdateBy();//提交人
            // notificationService.sendToUser(info.getId(), submitUser, NotificationConstant.CMS.EQUITY_INFO,
            // info);//添加股权融资登记的消息提示
        }
        this.saveOrUpdate(info);
        this.getSession().flush();
        this.getSession().clear();
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }

    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsEquityInfo entity, ICurrentUser user) {
        entity.setCreateDate(new Date());
        entity.setCreateBy(user.getId());
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsEquityInfo entity, ICurrentUser user) {
        entity.setUpdateDate(new Date());
        entity.setUpdateBy(user.getId());
        super.updateEntity(entity);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(CmsEquityInfoDetailVO cmsEquityInfoDetailVO, Object... objects) {
        return null;
    }

    @Override
    public void verifyHandler(List<CmsEquityInfoDetailVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (CmsEquityInfoDetailVO detailVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                if (!EmptyUtils.isEmpty(detailVO.getFinancingWayName())) {
                    if (detailVO.getFinancingWayName().equals("转让")) {
                        detailVO.setFinancingWay("1");
                    } else if (detailVO.getFinancingWayName().equals("增资")) {
                        detailVO.setFinancingWay("2");
                    } else {
                        errorMsgList.add(CmsConstant.CMS_EQUITY_TRUE_FINANCINGWAY);
                    }
                } else {
                    errorMsgList.add(CmsConstant.CMS_EQUITY_FINANCINGWAY_ISNULL);
                }
                if (EmptyUtils.isEmpty(detailVO.getInvestor())) {
                    errorMsgList.add(CmsConstant.CMS_EQUITY_INVESTOR_ISNULL);
                }
                if (!EmptyUtils.isEmpty(detailVO.getInvWayName())) {
                    if (detailVO.getInvWayName().equals("现金")) {
                        detailVO.setInvWay("1");
                    } else if (detailVO.getInvWayName().equals("实物资产")) {
                        detailVO.setInvWay("2");
                        if (EmptyUtils.isEmpty(detailVO.getAssetName())) {
                            errorMsgList.add(CmsConstant.CMS_EQUITY_ASSETNAME_ISNULL);
                        }
                    } else {
                        errorMsgList.add(CmsConstant.CMS_EQUITY_TRUE_INVWAY);
                    }
                } else {
                    errorMsgList.add(CmsConstant.CMS_EQUITY_INVWAY_ISNULL);
                }
                if (EmptyUtils.isEmpty(detailVO.getInvMoney())) {
                    errorMsgList.add(CmsConstant.CMS_EQUITY_INVMONEY_ISNULL);
                }
                if (!EmptyUtils.isEmpty(detailVO.getEquityPro())) {
                    BigDecimal s = new BigDecimal(100);
                    detailVO.setEquityPro(detailVO.getEquityPro().divide(s));
                }
                detailVO.addErrorMsg(errorMsgList);
            }
        }
    }

    public AjaxJson saveImport(List<CmsEquityInfoDetailVO> list) {
        AjaxJson ajaxJson = new AjaxJson();
        String addIds = null;
        for (CmsEquityInfoDetailVO detailVO : list) {
            CmsEquityInfoDetail detail = new CmsEquityInfoDetail();
            BeanUtils.copyNotNullProperties(detailVO, detail);
            String equityCode = this.getEquityCode();
            detail.setEquityCode(equityCode);
            this.saveDetail(detail);
            if (addIds == null) {
                addIds = detail.getId();
            } else {
                addIds = addIds + "," + detail.getId();
            }
        }
        ajaxJson.setObj(addIds);
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }

    /**
     * 功能介绍 :根据融资编号查询股权融资信息
     */
    public CmsEquityInfoVO getCmsEquityInfoByEquityCode(String equityCode) {
        if (StringUtils.isNotEmpty(equityCode)) {
            return cmsEquityInfoDao.getCmsEquityInfoByEquityCode(equityCode);
        }
        return null;
    }

    /**
     * 根据融资明细id更新融资明细状态
     *
     * @param equityId  融资明细id
     * @param bizStatus 融资明细状态
     */
    public void updateBizStatus(String equityId, String bizStatus) {
        if (StringUtils.isNotEmpty(equityId) && StringUtils.isNotEmpty(bizStatus)) {
            cmsEquityInfoDetailDao.updateBizStatus(equityId, bizStatus);
        }
    }

    /**
     * 根据融资编号更新融资明细状态
     *
     * @param equityCode 融资编号
     * @param bizStatus  融资明细状态
     */
    public void updateBizStatusByEquityCode(String equityCode, String bizStatus) {
        if (StringUtils.isNotEmpty(equityCode) && StringUtils.isNotEmpty(bizStatus)) {
            cmsEquityInfoDetailDao.updateBizStatusByEquityCode(equityCode, bizStatus);
        }
    }
}
