package com.bytter.finance.domain.security.service;
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
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.security.dao.CmsSecurityInfoDao;
import com.bytter.finance.domain.security.dao.CmsSecurityLevelInfoDao;
import com.bytter.finance.domain.security.entity.CmsSecurityInfo;
import com.bytter.finance.domain.security.entity.CmsSecurityLevelInfo;
import com.bytter.finance.domain.security.vo.CmsSecurityInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.SecurityMessageEnum;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
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
import java.util.Optional;
/**
 * 资产证券化登记主信息
 *
 * @author tongqr
 */
@WorkFlow(CmsWorkFlowConstant.CMS_SECURITY_INFO)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_SECURITY_INFO)
@Service("cmsSecurityInfoService")
@Transactional(rollbackFor = Exception.class)
public class CmsSecurityInfoService extends BaseService<CmsSecurityInfo, String>
        implements IWorkFlowListener, IUserExcelVerifyHandler<CmsSecurityLevelInfoVO> {
    @Autowired
    private CmsSecurityInfoDao cmsSecurityInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    @Autowired
    private CmsProcessMsgService cmsLogService;
    @Autowired
    private CmsSecurityLevelInfoDao cmsSecurityLevelInfoDao;
    @Autowired
    private CmsSecurityLevelService cmsSecurityLevelService;
    @Override
    public BaseDao<CmsSecurityInfo, String> getEntityDao() {
        return cmsSecurityInfoDao;
    }
    /**
     * 资产证券化登记主信息查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput, ICurrentUser user, String flag) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_SECURITY_INFO_QUERY, user.getId());
        return cmsSecurityInfoDao.getDataTable(dataTablesInput, corpAuth, flag);
    }
    /**
     * 资产证券化登记主信息查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTableForissue(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_SECURITY_INFO_QUERY, user.getId());
        return cmsSecurityInfoDao.getDataTableForissue(dataTablesInput, corpAuth);
    }
    /**
     * 资产证券化登记审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 资产证券化登记审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_SECURITY_INFO,
                input);
        DataTablesOutput dp = cmsSecurityInfoDao.dataTableApprove(input, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 根据id获取详情数据
     *
     * @param id
     *            id
     * @return 债券注册vo
     */
    public CmsSecurityInfoVO getInfoById(String id) {
        return cmsSecurityInfoDao.getInfoById(id);
    }
    /**
     * 新增资产证券化登记主信息
     *
     * @param cmsSecurityInfoVO
     *            资产证券化登记主信息
     * @param user
     *            当前用户
     */
    public void save(CmsSecurityInfoVO cmsSecurityInfoVO, ICurrentUser user) {
        CmsSecurityInfo cmsSecurityInfo = new CmsSecurityInfo();
        //TODO 补充单位名称及币种
        if(StringUtils.isNotEmpty(cmsSecurityInfoVO.getCorpId())){
            CorpDTO corpDTO = corpService.get(cmsSecurityInfoVO.getCorpId());
            Optional.ofNullable(corpDTO).ifPresent(corp -> cmsSecurityInfoVO.setCorpName(corp.getName()));
        }
        if (StringUtils.isNotEmpty(cmsSecurityInfoVO.getCurId())){
            BtCurrencyDTO currencyDTO = btCurrencyServiceImpl.getObjectById(cmsSecurityInfoVO.getCurId());
            Optional.ofNullable(currencyDTO).ifPresent(currencyDO -> cmsSecurityInfoVO.setCurName(currencyDO.getCurName()));
        }
        BeanUtils.copyNotNullProperties(cmsSecurityInfoVO, cmsSecurityInfo);
        cmsSecurityInfo.setCreateBy(user.getUsername());
        cmsSecurityInfo.setCreateDate(new Date());
        this.saveEntity(cmsSecurityInfo);
        // 资产层级信息保存
        this.saveLevelInfo(cmsSecurityInfoVO.getLevelVOList(), cmsSecurityInfo.getMoney(), cmsSecurityInfo.getId());
        int messageType = CmsProcessMsgConstant.MSGA_INPUT;
        String msg = String.valueOf(CmsProcessMsgConstant.MSGA_INPUT);
        if (CmsConstant.COMMITAPPROVE == cmsSecurityInfo.getStatus()) {
            messageType = CmsProcessMsgConstant.MSGA_SUBMIT;
            msg = String.valueOf(CmsProcessMsgConstant.MSGA_SUBMIT);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_SECURITY_INFO, cmsSecurityInfo.getId(), cmsSecurityInfo,
                    cmsSecurityInfo.getCorpId(), user.getId());
        }
        cmsLogService.insertProcessMsg(cmsSecurityInfo.getId(), SecurityConstant.CMS_SECURITY_INFO_CODE, messageType,
                msg, user);
    }
    public void saveLevelInfo(List<CmsSecurityLevelInfoVO> levelInfoVOS, BigDecimal money, String id) {
        List<CmsSecurityLevelInfo> cmsSecurityLevelInfos = new ArrayList<>();
        BigDecimal summoney = new BigDecimal(0);
        for (CmsSecurityLevelInfoVO vo : levelInfoVOS) {
            CmsSecurityLevelInfo cmsSecurityLevelInfo = new CmsSecurityLevelInfo();
            BeanUtils.copyProperties(vo, cmsSecurityLevelInfo);
            cmsSecurityLevelInfo.setSecurityInfoId(id);
            cmsSecurityLevelInfos.add(cmsSecurityLevelInfo);
            summoney = summoney.add(cmsSecurityLevelInfo.getExpectIssSize());
        }
        if (money.compareTo(summoney) != 0) {
            throw new ParameterException(SecurityMessageEnum.CMS_BASEDATA_SECURITY_ISSETMONEY_NOEQUITY);
        }
        // 删除之前得计划
        cmsSecurityLevelInfoDao.deleteLevelInfoById(id);
        cmsSecurityLevelInfoDao.batchSave(cmsSecurityLevelInfos);
    }
    /**
     * 修改资产证券化登记主信息
     *
     * @param cmsSecurityInfoVO
     *            资产证券化登记主信息
     * @param user
     *            当前用户
     */
    public void modify(CmsSecurityInfoVO cmsSecurityInfoVO, ICurrentUser user) {
        CmsSecurityInfo cmsSecurityInfo = new CmsSecurityInfo();
        //TODO 补充单位名称及币种
        if(StringUtils.isNotEmpty(cmsSecurityInfoVO.getCorpId())){
            CorpDTO corpDTO = corpService.get(cmsSecurityInfoVO.getCorpId());
            Optional.ofNullable(corpDTO).ifPresent(corp -> cmsSecurityInfoVO.setCorpName(corp.getName()));
        }
        if (StringUtils.isNotEmpty(cmsSecurityInfoVO.getCurId())){
            BtCurrencyDTO currencyDTO = btCurrencyServiceImpl.getObjectById(cmsSecurityInfoVO.getCurId());
            Optional.ofNullable(currencyDTO).ifPresent(currencyDO -> cmsSecurityInfoVO.setCurName(currencyDO.getCurName()));
        }
        BeanUtils.copyNotNullProperties(cmsSecurityInfoVO, cmsSecurityInfo);
        cmsSecurityInfo.setUpdateBy(user.getUsername());
        cmsSecurityInfo.setUpdateDate(new Date());
        this.updateEntity(cmsSecurityInfo);
        // 资产层级信息保存
        this.saveLevelInfo(cmsSecurityInfoVO.getLevelVOList(), cmsSecurityInfo.getMoney(), cmsSecurityInfo.getId());
        int messageType = CmsProcessMsgConstant.MSGA_INPUT;
        String msg = String.valueOf(CmsProcessMsgConstant.MSGA_INPUT);
        if (CmsConstant.COMMITAPPROVE == cmsSecurityInfo.getStatus()) {
            messageType = CmsProcessMsgConstant.MSGA_SUBMIT;
            msg = String.valueOf(CmsProcessMsgConstant.MSGA_SUBMIT);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_SECURITY_INFO, cmsSecurityInfo.getId(), cmsSecurityInfo,
                    cmsSecurityInfo.getCorpId(), user.getId());
        }
        cmsLogService.insertProcessMsg(cmsSecurityInfo.getId(), SecurityConstant.CMS_SECURITY_INFO_CODE, messageType,
                msg, user);
    }
    /**
     * 删除资产证券化登记信息
     *
     * @param id
     *            资产证券化登记id
     * @param user
     *            当前用户
     */
    public void delete(String id, ICurrentUser user) {
        CmsSecurityInfo cmsSecurityInfo = this.get(id);
        // 修改为已删除
        cmsSecurityInfo.setStatus(CmsConstant.LEASE_DELETED_2);
        cmsSecurityInfo.setUpdateDate(new Date());
        cmsSecurityInfo.setUpdateBy(user.getUsername());
        // 删除之前得计划
        cmsSecurityLevelInfoDao.deleteLevelInfoById(id);
        this.updateEntity(cmsSecurityInfo);
    }
    /**
     * 通过主信息id删除资产层级信息
     */
    public void deleteLevelInfoById(String securityInfoId) {
        cmsSecurityLevelInfoDao.deleteLevelInfoById(securityInfoId);
    }
    /**
     * 通过主表获取全部层级信息
     */
    public List<CmsSecurityLevelInfoVO> listLevelInfo(String securityInfoId) {
        return cmsSecurityLevelInfoDao.listLevelInfo(securityInfoId);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsSecurityInfo cmsSecurityInfo = this.get(businessKey, true);
        Integer status = result ? CmsConstant.APPROVED : CmsConstant.BACK;
        int messageType = result ? CmsProcessMsgConstant.MSGA_SECURITY_CHECKOK
                : CmsProcessMsgConstant.MSGA_SECURITY_CHECKOK;
        String msg = String.valueOf(
                result ? CmsProcessMsgConstant.MSGA_SECURITY_CHECKBACK : CmsProcessMsgConstant.MSGA_SECURITY_CHECKBACK);
        cmsLogService.insertProcessMsg(cmsSecurityInfo.getId(), SecurityConstant.CMS_SECURITY_INFO_CODE, messageType,
                msg, user);
        cmsSecurityInfo.setStatus(status);
        this.updateEntity(cmsSecurityInfo);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(CmsSecurityLevelInfoVO cmsSecurityLevelInfoVO, Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<CmsSecurityLevelInfoVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (CmsSecurityLevelInfoVO vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                if (EmptyUtils.isEmpty(vo.getLevelName())) {
                    errorMsgList.add("层级不能为空");
                } else {
                    CmsSecurityLevelVO securityLevelVO = cmsSecurityLevelService.getLevelByName(vo.getLevelName());
                    if (EmptyUtils.isEmpty(securityLevelVO)) {
                        errorMsgList.add("没有匹配得层级信息");
                    } else {
                        vo.setLevelId(securityLevelVO.getId());
                    }
                }
                if (EmptyUtils.isEmpty(vo.getProductName())) {
                    errorMsgList.add("产品名称不能为空");
                }
                if (EmptyUtils.isEmpty(vo.getTimeLimit())) {
                    errorMsgList.add("期限不能为空");
                }
                vo.addErrorMsg(errorMsgList);
            }
        }
    }
}
