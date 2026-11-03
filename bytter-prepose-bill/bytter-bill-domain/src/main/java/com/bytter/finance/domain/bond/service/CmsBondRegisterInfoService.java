package com.bytter.finance.domain.bond.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.bond.dao.CmsBondRegisterInfoDao;
import com.bytter.finance.domain.bond.entity.CmsBondRegisterInfo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.BondMessageEnum;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * 债券注册Service
 *
 * @author ouyj
 */
@WorkFlow(CmsWorkFlowConstant.CMS_BOND_REGISTER_INFO)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_BOND_REGISTER_INFO)
@Service("cmsBondRegisterInfoService")
@Transactional(rollbackFor = Exception.class)
public class CmsBondRegisterInfoService extends BaseService<CmsBondRegisterInfo, String> implements IWorkFlowListener {
    @Autowired
    private CmsBondRegisterInfoDao cmsBondRegisterInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @Autowired
    private CmsProcessMsgService cmsLogService;
    @Override
    public BaseDao<CmsBondRegisterInfo, String> getEntityDao() {
        return cmsBondRegisterInfoDao;
    }
    /**
     * 根据id集合查询债券注册表数据
     *
     * @param dataTablesInput
     *            前端传参
     * @param registerIdList
     *            id集合
     * @return 债券注册表数据
     */
    public DataTablesOutput getDataTablesByIds(DataTablesInput dataTablesInput, List<String> registerIdList) {
        return cmsBondRegisterInfoDao.getDataTablesByIds(dataTablesInput, registerIdList);
    }
    /**
     * 获取债券注册列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @param user
     *            当前用户
     * @return 债券注册列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpList = corpService.getUserCorpIds(ResourceConstant.CMS_BOND_REGISTER_QUERY, user.getId());
        return cmsBondRegisterInfoDao.getDataTable(dataTablesInput, corpList);
    }
    /**
     * 新增债券注册信息
     *
     * @param cmsBondRegisterInfo
     *            债券注册实体
     * @param user
     *            当前用户
     */
    public void saveBondRegisterInfo(CmsBondRegisterInfo cmsBondRegisterInfo, ICurrentUser user) {
        if (cmsBondRegisterInfoDao.getRegisterByCodeOrFullName(cmsBondRegisterInfo.getBondCode(), null, null) != null) {
            throw new ParameterException(BondMessageEnum.CMS_BASEDATA_REGISTER_CODE_ISEXIST);
        } else if (cmsBondRegisterInfoDao.getRegisterByCodeOrFullName(null, cmsBondRegisterInfo.getBondFullName(),
                null) != null) {
            throw new ParameterException(BondMessageEnum.CMS_BASEDATA_REGISTER_FULLNAME_ISEXIST);
        }
        if(StringUtils.isNotEmpty(cmsBondRegisterInfo.getBondCur())){
            //TODO 补充币种名称相关信息
            BtCurrencyDTO currencyDTO = iBtCurrencyService.getObjectById(cmsBondRegisterInfo.getBondCur());
            Optional.ofNullable(currencyDTO).ifPresent(currency -> cmsBondRegisterInfo.setCurName(currency.getCurName()));
        }
        cmsBondRegisterInfo.setCreateBy(user.getUsername());
        cmsBondRegisterInfo.setCreateDate(new Date());
        this.saveEntity(cmsBondRegisterInfo);
        int messageType = CmsProcessMsgConstant.MSGA_INPUT;
        String msg = String.valueOf(CmsProcessMsgConstant.MSGA_INPUT);
        if (CmsConstant.COMMITAPPROVE == cmsBondRegisterInfo.getStatus()) {
            messageType = CmsProcessMsgConstant.MSGA_SUBMIT;
            msg = String.valueOf(CmsProcessMsgConstant.MSGA_SUBMIT);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_BOND_REGISTER_INFO, cmsBondRegisterInfo.getId(),
                    cmsBondRegisterInfo, cmsBondRegisterInfo.getCorpId(), user.getId());
        }
        cmsLogService.insertProcessMsg(cmsBondRegisterInfo.getId(), CmsConstant.BOND_REGISTER_BILL_CODE_PREFIX,
                messageType, msg, user);
    }
    /**
     * 修改债券注册信息
     *
     * @param cmsBondRegisterInfo
     *            债券注册实体
     * @param user
     *            当前用户
     */
    public void updateBondRegisterInfo(CmsBondRegisterInfo cmsBondRegisterInfo, ICurrentUser user) {
        if (cmsBondRegisterInfoDao.getRegisterByCodeOrFullName(cmsBondRegisterInfo.getBondCode(), null,
                cmsBondRegisterInfo.getId()) != null) {
            throw new ParameterException(BondMessageEnum.CMS_BASEDATA_REGISTER_CODE_ISEXIST);
        } else if (cmsBondRegisterInfoDao.getRegisterByCodeOrFullName(null, cmsBondRegisterInfo.getBondFullName(),
                cmsBondRegisterInfo.getId()) != null) {
            throw new ParameterException(BondMessageEnum.CMS_BASEDATA_REGISTER_FULLNAME_ISEXIST);
        }
        if(StringUtils.isNotEmpty(cmsBondRegisterInfo.getBondCur())){
            //TODO 补充币种名称相关信息
            BtCurrencyDTO currencyDTO = iBtCurrencyService.getObjectById(cmsBondRegisterInfo.getBondCur());
            Optional.ofNullable(currencyDTO).ifPresent(currency -> cmsBondRegisterInfo.setCurName(currency.getCurName()));
        }
        cmsBondRegisterInfo.setUpdateBy(user.getUsername());
        cmsBondRegisterInfo.setUpdateDate(new Date());
        this.updateEntity(cmsBondRegisterInfo);
        int messageType = CmsProcessMsgConstant.MSGA_INPUT;
        String msg = String.valueOf(CmsProcessMsgConstant.MSGA_INPUT);
        if (CmsConstant.COMMITAPPROVE == cmsBondRegisterInfo.getStatus()) {
            messageType = CmsProcessMsgConstant.MSGA_SUBMIT;
            msg = String.valueOf(CmsProcessMsgConstant.MSGA_SUBMIT);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_BOND_REGISTER_INFO, cmsBondRegisterInfo.getId(),
                    cmsBondRegisterInfo, cmsBondRegisterInfo.getCorpId(), user.getId());
        }
        cmsLogService.insertProcessMsg(cmsBondRegisterInfo.getId(), CmsConstant.BOND_REGISTER_BILL_CODE_PREFIX,
                messageType, msg, user);
    }
    /**
     * 删除债券注册信息
     *
     * @param id
     *            债券注册id
     * @param user
     *            当前用户
     */
    public void deleteBondRegisterInfo(String id, ICurrentUser user) {
        boolean flag = cmsBondRegisterInfoDao.isQuoteType(id);
        if (flag) {
            throw new BusinessException(BondMessageEnum.CMS_BASEDATA_REGISTER_ISQUOTE);
        }
        CmsBondRegisterInfo cmsBondRegisterInfo = this.get(id);
        // 修改为已删除
        cmsBondRegisterInfo.setIsDelete(CmsConstant.IS_TRUE);
        cmsBondRegisterInfo.setStatus(CmsConstant.LEASE_DELETED_2);
        cmsBondRegisterInfo.setUpdateDate(new Date());
        cmsBondRegisterInfo.setUpdateBy(user.getUsername());
        this.updateEntity(cmsBondRegisterInfo);
    }
    /**
     * 债券注册审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 债券注册审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_BOND_REGISTER_INFO, input);
        DataTablesOutput dp = cmsBondRegisterInfoDao.dataTableApprove(input, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 工作流审批结束
     *
     * @param workFlowResult
     *            工作流返回结果
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsBondRegisterInfo registerInfo = this.get(businessKey, true);
        Integer status = result ? CmsConstant.APPROVED : CmsConstant.BACK;
        int messageType = result ? CmsProcessMsgConstant.MSGA_BOND_CHECKOK : CmsProcessMsgConstant.MSGA_BOND_CHECKBACK;
        String msg = String
                .valueOf(result ? CmsProcessMsgConstant.MSGA_BOND_CHECKOK : CmsProcessMsgConstant.MSGA_BOND_CHECKBACK);
        cmsLogService.insertProcessMsg(registerInfo.getId(), CmsConstant.BOND_REGISTER_BILL_CODE_PREFIX, messageType,
                msg, user);
        registerInfo.setStatus(status);
        this.updateEntity(registerInfo);
    }
    /**
     * 工作流审批开始
     *
     * @param workFlowResult
     *            工作流返回结果
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 根据id获取详情数据
     *
     * @param id
     *            id
     * @return 债券注册vo
     */
    public CmsBondRegisterInfoVo getInfoById(String id) {
        return cmsBondRegisterInfoDao.getInfoById(id);
    }
    /**
     * 根据债券注册id获取对于债券vo信息
     *
     * @param registerId
     *            id
     * @return 债券vo信息
     */
    public CmsBondRegisterInfoVo getRegisterById(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new CmsBondRegisterInfoVo();
        }
        List<CmsBondRegisterInfoVo> list = cmsBondRegisterInfoDao.getRegisterById(registerId);
        if (ListUtils.isNullOrEmpty(list)) {
            return new CmsBondRegisterInfoVo();
        }
        return list.get(0);
    }
}
