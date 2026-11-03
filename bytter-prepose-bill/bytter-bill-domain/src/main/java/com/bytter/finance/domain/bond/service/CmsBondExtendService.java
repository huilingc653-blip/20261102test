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
import com.bytter.finance.domain.bond.dao.CmsBondExtendDao;
import com.bytter.finance.domain.bond.dao.CmsBondRegisterInfoDao;
import com.bytter.finance.domain.bond.entity.CmsBondExtend;
import com.bytter.finance.domain.bond.vo.CmsBondExtendVO;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 债券展期
 *
 * @author wangf
 */
@WorkFlow(CmsWorkFlowConstant.CMS_BOND_EXTEND)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_BOND_EXTEND)
@Transactional(rollbackFor = Exception.class)
@Service("cmsBondExtendService")
public class CmsBondExtendService extends BaseService<CmsBondExtend, String> implements IWorkFlowListener {
    @Autowired
    private CmsBondRegisterInfoDao cmsBondRegisterInfoDao;
    @Autowired
    private CmsBondExtendDao cmsBondExtendDao;
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Override
    public BaseDao<CmsBondExtend, String> getEntityDao() {
        return cmsBondExtendDao;
    }
    /**
     * 获取债券信息列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @param user
     *            当前用户
     * @return 债券信息列表数据
     */
    public DataTablesOutput getTransferableRegisterDataTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpList = corpService.getUserCorpIds(ResourceConstant.CMS_BOND_EXTENDMAIN_QUERY, user.getId());
        return cmsBondExtendDao.getDataTable(dataTablesInput, corpList);
    }
    /**
     * 获取债券展期列表数据
     * 
     * @param registerId
     * @param dataTablesInput
     *            前端参数
     * @return 债券展期列表数据
     */
    public DataTablesOutput getDataTable(String registerId, DataTablesInput dataTablesInput) {
        DataTablesOutput result = this.cmsBondExtendDao.getExtends(registerId, dataTablesInput);
        return result;
    }
    /**
     * 保存
     *
     * @param vo
     * @param currentUser
     */
    public void save(CmsBondExtendVO vo, String saveType, ICurrentUser currentUser) {
        CmsBondExtend cmsBondExtend = new CmsBondExtend();
        if (EmptyUtils.isNotEmpty(vo.getId())) {
            cmsBondExtend = this.cmsBondExtendDao.get(vo.getId());
            vo.setUpdateDate(DateUtils.getDate());
            vo.setUpdateBy(currentUser.getId());
        } else {
            vo.setId(null);
            vo.setCreateBy(currentUser.getId());
            vo.setCreateDate(DateUtils.getDate());
        }
        if ("1".equals(saveType)) {
            vo.setStatus(CmsConstant.COMMITAPPROVE);
        } else {
            vo.setStatus(CmsConstant.TEMP);
        }
        BeanUtils.copyNotNullProperties(vo, cmsBondExtend);
        cmsBondExtendDao.saveOrUpdate(cmsBondExtend);
        Integer msgCode = EmptyUtils.isNotEmpty(vo.getId()) ? CmsProcessMsgConstant.MSGA_BOND_TRANSFER_UPDATE_SUBMIT
                : CmsProcessMsgConstant.MSGA_BOND_TRANSFER_ADD_SUBMIT;
        String corpId = this.cmsBondRegisterInfoDao.get(vo.getRegisterId(), true).getCorpId();
        if ("1".equals(saveType)) {
            // 提交
            msService.insertProcessMsg(cmsBondExtend.getId(), CmsConstant.BOND_TRANSFER_BILL_CODE_PREFIX, msgCode,
                    msgCode.toString(), currentUser);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_BOND_EXTEND, cmsBondExtend.getId(), cmsBondExtend,
                    corpId, currentUser.getId());
        } else {
            msService.insertProcessMsg(cmsBondExtend.getId(), CmsConstant.BOND_TRANSFER_BILL_CODE_PREFIX, msgCode,
                    msgCode.toString(), currentUser);
        }
    }
    /**
     * 删除
     *
     * @param extendId
     */
    public void delete(String extendId, ICurrentUser currentUser) {
        CmsBondExtend cmsBondExtend = this.cmsBondExtendDao.get(extendId);
        cmsBondExtend.setStatus(CmsConstant.DELETED);
        cmsBondExtend.setUpdateBy(currentUser.getLoginName());
        cmsBondExtend.setUpdateDate(DateUtils.getDate());
        cmsBondExtendDao.saveOrUpdate(cmsBondExtend);
    }
    /**
     * 通过registerId查询展期信息 最后一笔
     *
     * @param registerId
     * @return
     */
    public CmsBondExtendVO getCmsBondExtendByRegisterId(String registerId) {
        return this.cmsBondExtendDao.getCmsBondExtendByRegisterId(registerId);
    }
    /**
     * 通过issueId查询审批通过的展期信息 最后一笔
     *
     * @param issueId
     * @return
     */
    public CmsBondExtendVO getCmsBondExtendByIssueId(String issueId) {
        return this.cmsBondExtendDao.getCmsBondExtendByIssueId(issueId);
    }
    /**
     * 通过id查询展期信息
     *
     * @param id
     * @return
     */
    public CmsBondExtendVO getCmsBondExtendById(String id) {
        return this.cmsBondExtendDao.getCmsBondExtendId(id);
    }
    /**
     * 展期审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 债券展期审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_BOND_EXTEND,
                input);
        DataTablesOutput<CmsBondExtendVO> dp = this.cmsBondExtendDao.dataTableApprove(input,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        CmsBondExtend cmsBondExtend = this.get(businessKey,true);
        Integer msgCode = CmsProcessMsgConstant.MSGA_BOND_TRANSFER_CHECKOK;
        if (CmsWorkFlowConstant.CMS_BOND_EXTEND.equals(processDefinitionKey)) {// 可转债审批
            if (result) {// 审批通过
                if (CmsConstant.COMMITAPPROVE == cmsBondExtend.getStatus().intValue()) {// 待审批
                    cmsBondExtend.setStatus(CmsConstant.APPROVED);
                }
            } else {// 打回
                msgCode = CmsProcessMsgConstant.MSGA_BOND_TRANSFER_CHECKBACK;
                cmsBondExtend.setStatus(CmsConstant.BACK);
            }
            this.saveOrUpdate(cmsBondExtend);
            msService.insertProcessMsg(cmsBondExtend.getId(), CmsConstant.BOND_EXTEND_BILL_CODE_PREFIX, msgCode,
                    msgCode.toString(), user);
        }
    }
    /**
     * 根据债券Id获取有效的债券发行编号
     * 
     * @param registerId
     *            债券注册id
     * @return 债券信息
     */
    public List<CmsBondIssueInfoVo> listValidBondIssueNum(String registerId) {
        List<CmsBondIssueInfoVo> issueInfoVoList = cmsBondExtendDao.listValidBondIssueNum(registerId);
        ArrayList<CmsBondIssueInfoVo> extendList = new ArrayList<>();
        for (CmsBondIssueInfoVo vo : issueInfoVoList) {
            extendList.add(vo);
            // 最大输入金额控制
            BigDecimal sendBalance = cmsBondPayService.completeSendBalance(vo.getId(), null);
            if (sendBalance.compareTo(BigDecimal.ZERO) != 0) {
                vo.setUnpaidMoney(sendBalance);
            }
        }
        return extendList;
    }
}
