package com.bytter.finance.domain.lease.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.InlWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.lease.dao.LeaseUpdateObjectInfoDao;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseItemType;
import com.bytter.finance.domain.lease.entity.LeaseObjectInfo;
import com.bytter.finance.domain.lease.vo.ContractLeaseObjectInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseObjectInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseUpdateObjectInfoVO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service("leaseUpdateObjectInfoService")
@WorkFlow(InlWorkFlowConstant.LEASE_UPDATE_OBJECT_INFO)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.LEASE_UPDATE_OBJECT_INFO)
@Transactional
public class LeaseUpdateObjectInfoService extends BaseService<LeaseObjectInfo, String>
        implements IWorkFlowListener, IWorkFlowRevokeListener, IUserExcelVerifyHandler<LeaseUpdateObjectInfoVO> {
    @Autowired
    private LeaseUpdateObjectInfoDao leaseUpdateObjectInfoDao;
    @Autowired
    private LeaseObjectInfoService leaseObjectInfoService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private LeaseBasedataService leaseBasedataService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    /*
     * 查询租赁物列表
     */
    public DataTablesOutput queryLeaseObjectInfoList(DataTablesInput input, String user, String leaseId) {
        return leaseUpdateObjectInfoDao.queryLeaseObjectInfoList(input, leaseId);
    }
    public DataTablesOutput getDataleaseDatatables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        return leaseUpdateObjectInfoDao.getDataleaseDatatables(dataTablesInput, corpAuth);
    }
    public LeaseObjectInfoVO queryLeaseObjectInfo(String id) {
        return leaseUpdateObjectInfoDao.queryLeaseObjectInfo(id);
    }
    /**
     * 数据保存
     *
     * @param leaseObjectInfoVO
     * @param user
     */
    public void saveLeaseObjectInfo(LeaseObjectInfoVO leaseObjectInfoVO, ICurrentUser user) {
        LeaseObjectInfo leaseObjectInfo = new LeaseObjectInfo();
        leaseObjectInfo.setCreateBy(user.getId());
        leaseObjectInfo.setCreateDate(new Date());
        BeanUtils.copyNotNullProperties(leaseObjectInfoVO, leaseObjectInfo);
        leaseObjectInfo.setNumb(EmptyUtils.isEmpty(leaseObjectInfo.getNumb()) ? 0 : leaseObjectInfo.getNumb());
        leaseUpdateObjectInfoDao.save(leaseObjectInfo);
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(leaseObjectInfo.getLeaseId());
        cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_40);
        cmsFinancingLeaseInfoService.updateEntity(cmsFinancingLeaseInfo);
        // 打回数据角标处理
        if (CmsConstant.LEASE_COMMITAPPROVE == leaseObjectInfoVO.getStatus()) {
            CmsFinancingLeaseInfo info = cmsFinancingLeaseInfoService.get(leaseObjectInfo.getLeaseId());
            info.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_41);
            cmsFinancingLeaseInfoService.updateEntity(info);
             getStartHandler().handler(InlWorkFlowConstant.LEASE_UPDATE_OBJECT_INFO, leaseObjectInfo.getId(),
                    leaseObjectInfo, cmsFinancingLeaseInfoService.get(leaseObjectInfo.getLeaseId()).getCorpId(),
                    user.getId());
        }
    }
    public boolean checkLeaseItemCode(String leaseItemCode, String id) {
        return leaseUpdateObjectInfoDao.checkLeaseItemCode(leaseItemCode, id);
    }
    /**
     * 数据更新
     *
     * @param leaseObjectInfoVO
     * @param user
     */
    public void modifyLeaseObjectInfo(LeaseObjectInfoVO leaseObjectInfoVO, ICurrentUser user) {
        LeaseObjectInfo leaseObjectInfo = new LeaseObjectInfo();
        leaseObjectInfo.setUpdateBy(user.getId());
        leaseObjectInfo.setUpdateDate(new Date());
        BeanUtils.copyNotNullProperties(leaseObjectInfoVO, leaseObjectInfo);
        leaseObjectInfo.setNumb(EmptyUtils.isEmpty(leaseObjectInfo.getNumb()) ? 0 : leaseObjectInfo.getNumb());
        leaseUpdateObjectInfoDao.update(leaseObjectInfo);
        notificationService.complete(NotificationConstant.CMS.LEASE_UPDATE_OBJECT_INFO, leaseObjectInfo.getId(),
                user.getId());
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(leaseObjectInfo.getLeaseId());
        cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_40);
        cmsFinancingLeaseInfoService.updateEntity(cmsFinancingLeaseInfo);
        // 打回数据角标处理
        if (CmsConstant.LEASE_COMMITAPPROVE == leaseObjectInfoVO.getStatus()) {
            CmsFinancingLeaseInfo info = cmsFinancingLeaseInfoService.get(leaseObjectInfo.getLeaseId());
            info.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_41);
            cmsFinancingLeaseInfoService.updateEntity(info);
             getStartHandler().handler(InlWorkFlowConstant.LEASE_UPDATE_OBJECT_INFO, leaseObjectInfo.getId(),
                    leaseObjectInfo, cmsFinancingLeaseInfoService.get(leaseObjectInfo.getLeaseId()).getCorpId(),
                    user.getId());
        }
    }
    @Override
    public BaseDao<LeaseObjectInfo, String> getEntityDao() {
        return null;
    }
    /**
     * 暂存打回数据列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(InlWorkFlowConstant.LEASE_UPDATE_OBJECT_INFO, input);
        return workFlowBusiness
                .merge(leaseUpdateObjectInfoDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList()));
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        LeaseObjectInfo leaseObjectInfo = leaseObjectInfoService.get(workFlowResult.getBusinessKey(), true);
        Integer status = workFlowResult.isPass() ? CmsConstant.LEASE_APPROVED : CmsConstant.LEASE_BACK;
        leaseObjectInfo.setStatus(status);
        leaseObjectInfo.setUpdateBy(user.getLoginName());
        leaseObjectInfo.setUpdateDate(DateUtils.getDate());
        leaseObjectInfoService.saveOrUpdate(leaseObjectInfo);
        if (status.equals(CmsConstant.LEASE_BACK)) {
            CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService
                    .get(leaseObjectInfo.getLeaseId());
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__5);
            cmsFinancingLeaseInfoService.updateEntity(cmsFinancingLeaseInfo);
            String submitUser = StringUtils.isEmpty(leaseObjectInfo.getUpdateBy()) ? leaseObjectInfo.getCreateBy()
                    : leaseObjectInfo.getUpdateBy();// 提交人
            notificationService.sendToUser(leaseObjectInfo.getId(), submitUser,
                    NotificationConstant.CMS.LEASE_UPDATE_OBJECT_INFO, leaseObjectInfo);// 添加消息提示
        } else {
            CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService
                    .get(leaseObjectInfo.getLeaseId());
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_98);
            cmsFinancingLeaseInfoService.updateEntity(cmsFinancingLeaseInfo);
        }
    }
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        LeaseObjectInfo leaseObjectInfo = leaseObjectInfoService.get(workFlowResult.getBusinessKey());
        leaseObjectInfo.setStatus(CmsConstant.LEASE_TEMP);
        leaseObjectInfo.setUpdateBy(user.getLoginName());
        leaseObjectInfo.setUpdateDate(DateUtils.getDate());
        leaseObjectInfoService.saveOrUpdate(leaseObjectInfo);
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(leaseObjectInfo.getLeaseId());
        cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_98);
        cmsFinancingLeaseInfoService.updateEntity(cmsFinancingLeaseInfo);
    }
    /*
     * 导入保存
     */
    public void saveImport(List<ContractLeaseObjectInfoVO> list, ICurrentUser user, String leaseId) {
        // 先判断导入的列是否有租赁物编号的字段，有的话就保存后者
        List<Integer> flag = new ArrayList<Integer>();
        a: for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getLeaseItemCode().equals(list.get(j).getLeaseItemCode())) {
                    flag.add(i);
                    continue a;
                }
            }
        }
        for (int i = flag.size() - 1; i >= 0; i--) {
            int a = flag.get(i);
            list.remove(a);
        }
        List<LeaseObjectInfo> entityList = new ArrayList<>();
        for (ContractLeaseObjectInfoVO vo : list) {
            LeaseObjectInfo leaseObjectInfo = new LeaseObjectInfo();
            vo.setLeaseId(leaseId);// 租赁合同
            vo.setStatus(CmsConstant.LEASE_TEMP);// 默认暂存状态
            BeanUtils.copyNotNullProperties(vo, leaseObjectInfo);
            entityList.add(leaseObjectInfo);
            // 判断修改暂存状态是否存在重复的租赁物编号，存在就update，不存在就add
            LeaseObjectInfoVO entity = checkRepeat(vo.getLeaseItemCode());
            leaseObjectInfo.setCreateBy(user.getLoginName());
            leaseObjectInfo.setCreateDate(DateUtils.getDate());
            if (!EmptyUtils.isEmpty(entity)) {
                leaseObjectInfo.setId(entity.getId()); // 查询到的重复合同编号的id
                leaseUpdateObjectInfoDao.update(leaseObjectInfo);// 修改
            } else {
                leaseUpdateObjectInfoDao.save(leaseObjectInfo);// 暂存
            }
        }
    }
    /*
     * 唯一性校验
     */
    public LeaseObjectInfoVO checkRepeat(String itemCode) {
        return leaseUpdateObjectInfoDao.checkRepeat(itemCode);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(LeaseUpdateObjectInfoVO vo, Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<LeaseUpdateObjectInfoVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (LeaseUpdateObjectInfoVO leaseObjectInfoVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                if (EmptyUtils.isEmpty(leaseObjectInfoVO.getLeaseItemCode())) {
                    errorMsgList.add("租赁物编号不能为空");
                }
                if (EmptyUtils.isEmpty(leaseObjectInfoVO.getLeaseItemName())) {
                    errorMsgList.add("租赁物名称不能为空");
                }
                if (!EmptyUtils.isEmpty(leaseObjectInfoVO.getName())) {
                    LeaseItemType leaseItemType = leaseBasedataService.getLeaseTypebyName(leaseObjectInfoVO.getName());
                    if (EmptyUtils.isEmpty(leaseItemType)) {
                        errorMsgList.add("租赁物类别不存在");
                    } else {
                        leaseObjectInfoVO.setLeaseTypeId(leaseItemType.getId());
                    }
                }
                if (!EmptyUtils.isEmpty(leaseObjectInfoVO.getOldMoney())
                        && leaseObjectInfoVO.getOldMoney().compareTo(BigDecimal.ZERO) < 0) {
                    errorMsgList.add("原值金额不能小于0");
                }
                if (!EmptyUtils.isEmpty(leaseObjectInfoVO.getNumb()) && leaseObjectInfoVO.getNumb() < 0) {
                    leaseObjectInfoVO.setNumb(0);
                }
                if (!EmptyUtils.isEmpty(leaseObjectInfoVO.getNowMoney())) {
                    if (leaseObjectInfoVO.getNowMoney().compareTo(BigDecimal.ZERO) < 0) {
                        errorMsgList.add("现值金额不能小于0");
                    }
                } else {
                    errorMsgList.add("现值金额不能为空");
                }
                if (!EmptyUtils.isEmpty(leaseObjectInfoVO.getCurName())) {
                    String curId = this.btCurrencyService.getCurrencyByName(leaseObjectInfoVO.getCurName());
                    if (!EmptyUtils.isEmpty(curId)) {
                        leaseObjectInfoVO.setCurId(curId);
                    } else {
                        errorMsgList.add("币种不存在");
                    }
                } else {
                    errorMsgList.add("币种不能为空");
                }
                if (!ListUtils.isNullOrEmpty(errorMsgList)) {
                    leaseObjectInfoVO.addErrorMsg(errorMsgList);
                }
            }
        }
    }
}
