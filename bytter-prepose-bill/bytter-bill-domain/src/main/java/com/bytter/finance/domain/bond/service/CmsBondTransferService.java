package com.bytter.finance.domain.bond.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.bond.dao.CmsBondIssueInfoDao;
import com.bytter.finance.domain.bond.dao.CmsBondRegisterInfoDao;
import com.bytter.finance.domain.bond.dao.CmsBondTransferDao;
import com.bytter.finance.domain.bond.entity.CmsBondIssueInfo;
import com.bytter.finance.domain.bond.entity.CmsBondTransfer;
import com.bytter.finance.domain.bond.vo.CmsBondTransferVO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
@WorkFlow(CmsWorkFlowConstant.CMS_BOND_TRENSFER)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_BOND_TRENSFER)
@Transactional(rollbackFor = Exception.class)
@Service("cmsBondTransferService")
public class CmsBondTransferService extends BaseService<CmsBondTransfer, String>
        implements IWorkFlowListener, IUserExcelVerifyHandler<CmsBondTransferVO> {
    @Autowired
    private CmsBondRegisterInfoDao cmsBondRegisterInfoDao;
    @Autowired
    private CmsBondTransferDao cmsBondTransferDao;
    @Autowired
    private CmsBondIssueInfoDao cmsBondIssueInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Override
    public BaseDao<CmsBondTransfer, String> getEntityDao() {
        return cmsBondTransferDao;
    }
    /**
     * 获取可转债的债券信息列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @param user
     *            当前用户
     * @return 可转债的债券信息列表数据
     */
    public DataTablesOutput getTransferableRegisterDataTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpList = corpService.getUserCorpIds(ResourceConstant.CMS_BOND_TRANSFER_QUERY, user.getId());
        return cmsBondRegisterInfoDao.getTransferableDataTable(dataTablesInput, corpList, true);
    }
    /**
     * 获取可转债列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @return 可转债列表数据
     */
    public DataTablesOutput<CmsBondTransfer> getDataTable(DataTablesInput dataTablesInput, String issueId) {
        DataTablesOutput<CmsBondTransfer> result = this.cmsBondTransferDao.getTransfers(dataTablesInput, issueId);
        return result;
    }
    /**
     * 保存
     * 
     * @param cmsBondTransferVO
     * @param currentUser
     */
    public void save(CmsBondTransferVO cmsBondTransferVO, String saveType, ICurrentUser currentUser) {
        CmsBondTransfer cmsBondTransfer = new CmsBondTransfer();
        if (EmptyUtils.isNotEmpty(cmsBondTransferVO.getId())) {
            cmsBondTransfer = this.cmsBondTransferDao.get(cmsBondTransferVO.getId());
            cmsBondTransferVO.setUpdateDate(DateUtils.getDate());
            cmsBondTransferVO.setUpdateBy(currentUser.getId());
        } else {
            cmsBondTransferVO.setId(null);
            cmsBondTransferVO.setCreateBy(currentUser.getId());
            cmsBondTransferVO.setCreateDate(DateUtils.getDate());
        }
        if ("1".equals(saveType)) {
            cmsBondTransferVO.setStatus(CmsConstant.COMMITAPPROVE);
        } else {
            cmsBondTransferVO.setStatus(CmsConstant.TEMP);
        }
        BeanUtils.copyNotNullProperties(cmsBondTransferVO, cmsBondTransfer);
        this.cmsBondTransferDao.saveOrUpdate(cmsBondTransfer);
        Integer msgCode = EmptyUtils.isNotEmpty(cmsBondTransferVO.getId())
                ? CmsProcessMsgConstant.MSGA_BOND_TRANSFER_UPDATE_SUBMIT
                : CmsProcessMsgConstant.MSGA_BOND_TRANSFER_ADD_SUBMIT;
        if ("1".equals(saveType)) {// 提交
            String corpId = this.cmsBondRegisterInfoDao.getRegisterCorpByIssueId(cmsBondTransfer.getIssueId());
            msService.insertProcessMsg(cmsBondTransfer.getId(), CmsConstant.BOND_TRANSFER_BILL_CODE_PREFIX, msgCode,
                    msgCode.toString(), currentUser);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_BOND_TRENSFER, cmsBondTransfer.getId(), cmsBondTransfer,
                    corpId, currentUser.getId());
        } else {
            msService.insertProcessMsg(cmsBondTransfer.getId(), CmsConstant.BOND_TRANSFER_BILL_CODE_PREFIX, msgCode,
                    msgCode.toString(), currentUser);
        }
    }
    /**
     * 删除
     * 
     * @param id
     */
    public void delete(String id, ICurrentUser currentUser) {
        CmsBondTransfer cmsBondTransfer = this.cmsBondTransferDao.get(id);
        cmsBondTransfer.setStatus(CmsConstant.DELETED);
        this.cmsBondTransferDao.saveOrUpdate(cmsBondTransfer);
        Integer msgCode = CmsProcessMsgConstant.MSGA_BOND_TRANSFER_DEL;
        msService.insertProcessMsg(id, CmsConstant.BOND_TRANSFER_BILL_CODE_PREFIX, msgCode, msgCode.toString(),
                currentUser);
    }
    /**
     * 通过id查询可转债信息
     * 
     * @param id
     * @return
     */
    public CmsBondTransferVO getCmsBondTransferById(String id) {
        CmsBondTransfer cmsBondTransfer = this.cmsBondTransferDao.get(id, true);
        CmsBondTransferVO result = new CmsBondTransferVO();
        if (!EmptyUtils.isEmpty(cmsBondTransfer)) {
            BeanUtils.copyNotNullProperties(cmsBondTransfer, result);
        }
        return result;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(CmsBondTransferVO obj, Object... user) {
        return null;
    }
    /**
     * 校验导入数据
     * 
     * @param list
     *            导入数据集合
     * @param user
     *            用户
     */
    @Override
    public void verifyHandler(List<CmsBondTransferVO> list, Object... user) {
        if (!CollectionUtils.isEmpty(list)) {
            for (CmsBondTransferVO cmsBondTransferVO : list) {
                List<String> errorMsg = new ArrayList<>();
                List<CmsBondIssueInfo> cmsBondIssueInfos = cmsBondIssueInfoDao
                        .listByBondCode(cmsBondTransferVO.getBondCode(), "可转债");
                // 判断债券代码是否有债券发行审批通过的数据
                if (CollectionUtils.isEmpty(cmsBondIssueInfos)) {
                    errorMsg.add(CmsConstant.BOND_EXCEL_ERROR_BONDCODE_ISERROR);
                } else {
                    cmsBondTransferVO.setIssueId(cmsBondIssueInfos.get(0).getId());
                }
                // 添加错误信息到vo
                if (!errorMsg.isEmpty()) {
                    cmsBondTransferVO.addErrorMsg(errorMsg);
                }
            }
        }
    }
    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<CmsBondTransferVO> list, ICurrentUser userVo) {
        if (!CollectionUtils.isEmpty(list)) {
            for (CmsBondTransferVO vo : list) {
                // 暂存状态
                this.save(vo, CmsConstant.IS_FALSE, userVo);
            }
        }
    }
    /**
     * 转债审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 债券注册审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_BOND_TRENSFER,
                input);
        DataTablesOutput<CmsBondTransferVO> dp = this.cmsBondTransferDao.dataTableApprove(input,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        CmsBondTransfer cmsBondTransfer = this.get(businessKey, true);
        int msgCode = CmsProcessMsgConstant.MSGA_BOND_TRANSFER_CHECKOK;
        if (CmsWorkFlowConstant.CMS_BOND_TRENSFER.equals(processDefinitionKey)) {
            // 可转债审批
            if (result) {
                // 审批通过
                if (CmsConstant.COMMITAPPROVE == cmsBondTransfer.getStatus()) {
                    // 待审批
                    cmsBondTransfer.setStatus(CmsConstant.APPROVED);
                }
            } else {// 打回
                msgCode = CmsProcessMsgConstant.MSGA_BOND_TRANSFER_CHECKBACK;
                cmsBondTransfer.setStatus(CmsConstant.BACK);
            }
            this.saveOrUpdate(cmsBondTransfer);
            msService.insertProcessMsg(cmsBondTransfer.getId(), CmsConstant.BOND_TRANSFER_BILL_CODE_PREFIX, msgCode,
                    Integer.toString(msgCode), user);
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
