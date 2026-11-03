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
import com.bytter.finance.domain.bond.dao.CmsBondSwapDao;
import com.bytter.finance.domain.bond.entity.CmsBondIssueInfo;
import com.bytter.finance.domain.bond.entity.CmsBondSwap;
import com.bytter.finance.domain.bond.vo.CmsBondSwapVO;
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
@WorkFlow(CmsWorkFlowConstant.CMS_BOND_SWAP)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_BOND_SWAP)
@Transactional(rollbackFor = Exception.class)
@Service("cmsBondSwapService")
public class CmsBondSwapService extends BaseService<CmsBondSwap, String>
        implements IWorkFlowListener, IUserExcelVerifyHandler<CmsBondSwapVO> {
    @Autowired
    private CmsBondRegisterInfoDao cmsBondRegisterInfoDao;
    @Autowired
    private CmsBondSwapDao cmsBondSwapDao;
    @Autowired
    private CmsBondIssueInfoDao cmsBondIssueInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Override
    public BaseDao<CmsBondSwap, String> getEntityDao() {
        return cmsBondSwapDao;
    }
    /**
     * 获取可交债的债券信息列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @param user
     *            当前用户
     * @return 可交债的债券信息列表数据
     */
    public DataTablesOutput getSwapRegisterDataTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpList = corpService.getUserCorpIds(ResourceConstant.CMS_BOND_SWAP_QUERY, user.getId());
        return cmsBondRegisterInfoDao.getTransferableDataTable(dataTablesInput, corpList, false);
    }
    /**
     * 获取可交债列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @return 可交债列表数据
     */
    public DataTablesOutput<CmsBondSwap> getDataTable(DataTablesInput dataTablesInput, String issueId) {
        DataTablesOutput<CmsBondSwap> result = this.cmsBondSwapDao.getSwaps(dataTablesInput, issueId);
        return result;
    }
    /**
     * 保存
     *
     * @param cmsBondSwapVO
     * @param currentUser
     */
    public void save(CmsBondSwapVO cmsBondSwapVO, String saveType, ICurrentUser currentUser) {
        CmsBondSwap cmsBondTransfer = new CmsBondSwap();
        if (EmptyUtils.isNotEmpty(cmsBondSwapVO.getId())) {
            cmsBondTransfer = this.cmsBondSwapDao.get(cmsBondSwapVO.getId());
            cmsBondSwapVO.setUpdateDate(DateUtils.getDate());
            cmsBondSwapVO.setUpdateBy(currentUser.getId());
        } else {
            cmsBondSwapVO.setId(null);
            cmsBondSwapVO.setCreateBy(currentUser.getId());
            cmsBondSwapVO.setCreateDate(DateUtils.getDate());
        }
        if ("1".equals(saveType)) {
            cmsBondSwapVO.setStatus(CmsConstant.COMMITAPPROVE);
        } else {
            cmsBondSwapVO.setStatus(CmsConstant.TEMP);
        }
        BeanUtils.copyNotNullProperties(cmsBondSwapVO, cmsBondTransfer);
        this.cmsBondSwapDao.saveOrUpdate(cmsBondTransfer);
        Integer msgCode = EmptyUtils.isNotEmpty(cmsBondSwapVO.getId())
                ? CmsProcessMsgConstant.MSGA_BOND_TRANSFER_UPDATE_SUBMIT
                : CmsProcessMsgConstant.MSGA_BOND_TRANSFER_ADD_SUBMIT;
        if ("1".equals(saveType)) {// 提交
            String corpId = this.cmsBondRegisterInfoDao.getRegisterCorpByIssueId(cmsBondTransfer.getIssueId());
            msService.insertProcessMsg(cmsBondTransfer.getId(), CmsConstant.BOND_SWAP_BILL_CODE_PREFIX, msgCode,
                    msgCode.toString(), currentUser);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_BOND_SWAP, cmsBondTransfer.getId(), cmsBondTransfer,
                    corpId, currentUser.getId());
        } else {
            msService.insertProcessMsg(cmsBondTransfer.getId(), CmsConstant.BOND_SWAP_BILL_CODE_PREFIX, msgCode,
                    msgCode.toString(), currentUser);
        }
    }
    /**
     * 删除
     *
     * @param id
     */
    public void delete(String id, ICurrentUser currentUser) {
        CmsBondSwap cmsBondSwap = this.cmsBondSwapDao.get(id);
        cmsBondSwap.setStatus(CmsConstant.DELETED);
        this.cmsBondSwapDao.saveOrUpdate(cmsBondSwap);
        Integer msgCode = CmsProcessMsgConstant.MSGA_BOND_TRANSFER_DEL;
        msService.insertProcessMsg(id, CmsConstant.BOND_SWAP_BILL_CODE_PREFIX, msgCode, msgCode.toString(),
                currentUser);
    }
    /**
     * 通过id查询可交债信息
     *
     * @param id
     * @return
     */
    public CmsBondSwapVO getCmsBondSwapById(String id) {
        CmsBondSwap cmsBondSwap = this.cmsBondSwapDao.get(id, true);
        CmsBondSwapVO result = new CmsBondSwapVO();
        if (!EmptyUtils.isEmpty(cmsBondSwap)) {
            BeanUtils.copyNotNullProperties(cmsBondSwap, result);
        }
        return result;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(CmsBondSwapVO obj, Object... user) {
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
    public void verifyHandler(List<CmsBondSwapVO> list, Object... user) {
        if (!CollectionUtils.isEmpty(list)) {
            for (CmsBondSwapVO cmsBondSwapVO : list) {
                List<String> errorMsg = new ArrayList<>();
                List<CmsBondIssueInfo> cmsBondIssueInfos = cmsBondIssueInfoDao
                        .listByBondCode(cmsBondSwapVO.getBondCode(), "可交债");
                // 判断债券代码是否有债券发行审批通过的数据
                if (CollectionUtils.isEmpty(cmsBondIssueInfos)) {
                    errorMsg.add(CmsConstant.BOND_EXCEL_ERROR_BONDCODE_ISERROR);
                } else {
                    cmsBondSwapVO.setIssueId(cmsBondIssueInfos.get(0).getId());
                }
                // 添加错误信息到vo
                if (!errorMsg.isEmpty()) {
                    cmsBondSwapVO.addErrorMsg(errorMsg);
                }
            }
        }
    }
    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<CmsBondSwapVO> list, ICurrentUser userVo) {
        if (!CollectionUtils.isEmpty(list)) {
            for (CmsBondSwapVO vo : list) {
                // 暂存状态
                this.save(vo, CmsConstant.IS_FALSE, userVo);
            }
        }
    }
    /**
     * 交债审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 债券注册审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_BOND_SWAP,
                input);
        DataTablesOutput<CmsBondSwapVO> dp = this.cmsBondSwapDao.dataTableApprove(input,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        CmsBondSwap cmsBondSwap = this.get(businessKey,true);
        int msgCode = CmsProcessMsgConstant.MSGA_BOND_TRANSFER_CHECKOK;
        if (CmsWorkFlowConstant.CMS_BOND_SWAP.equals(processDefinitionKey)) {
            if (result) {// 审批通过
                if (CmsConstant.COMMITAPPROVE == cmsBondSwap.getStatus()) {// 待审批
                    cmsBondSwap.setStatus(CmsConstant.APPROVED);
                }
            } else {// 打回
                msgCode = CmsProcessMsgConstant.MSGA_BOND_TRANSFER_CHECKBACK;
                cmsBondSwap.setStatus(CmsConstant.BACK);
            }
            this.saveOrUpdate(cmsBondSwap);
            msService.insertProcessMsg(cmsBondSwap.getId(), CmsConstant.BOND_SWAP_BILL_CODE_PREFIX, msgCode,
                    Integer.toString(msgCode), user);
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
