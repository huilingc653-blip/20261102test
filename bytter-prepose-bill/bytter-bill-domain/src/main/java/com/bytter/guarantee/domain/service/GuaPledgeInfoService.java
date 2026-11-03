package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.guarantee.constant.GuaWorkFlowConstant;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.dao.GuaPledgeInfoDao;
import com.bytter.guarantee.domain.entity.*;
import com.bytter.guarantee.domain.vo.GuaContractInfoVO;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.guarantee.domain.vo.GuaPledgeOccupyInfoVO;
import com.bytter.guarantee.domain.vo.GuaPledgeOccupyVO;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaPledgeTypeService
 * @Description 抵质押物类型维护Service
 * @date 2018年12月11日
 */
@Service("guaPledgeInfoService")
@Transactional
@WorkFlow(value = {GuaWorkFlowConstant.GUA_PLEDGE_REGIST})
@DubboService(interfaceClass = IWorkFlowListener.class,group = GuaWorkFlowConstant.GUA_PLEDGE_REGIST)
public class GuaPledgeInfoService extends BaseService<GuaPledgeInfo, String> implements IWorkFlowListener,
        IUserExcelVerifyHandler<GuaPledgeInfoVO>, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private GuaPledgeInfoDao guaPledgeInfoDao;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private GuaContractPledgeService guaContractPledgeService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private GuaLogService guaLogService;
    @Autowired
    private GuaPledgeChangeService guaPledgeChangeService;
    @Autowired
    private GuaContractWayService guaContractWayService;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired(required = false)
    private GuaPledgeOccupyService guaPledgeOccupyService;
    @Autowired
    private GuaPledgeTypeService guaPledgeTypeService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    /**
     * 地址押物登记页面列表查询
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<GuaPledgeInfoVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(GuaranteeConstant.GUA_PLEDGE_INFO_QUERY, user.getId());
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        return guaPledgeInfoDao.getDataTablesUnion(input, corpAuth, configDTO);
    }
    /**
     * 抵质押物查询
     *
     * @param input 查询参数
     * @param user  用户信息
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<GuaPledgeInfoVO> searchDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(GuaranteeConstant.GUA_PLEDGE_INFO_QUERY, user.getId());
        // 获取是否允许多次抵押
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        return guaPledgeInfoDao.searchDataTablesUnion(input, corpAuth, configDTO);
    }
    /**
     * 查询抵质押物登记审批列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getApproveDataTables(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(GuaWorkFlowConstant.GUA_PLEDGE_REGIST, input);
        String businessSql = null;
        if(!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())){
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        }
        DataTablesOutput dp = guaPledgeInfoDao.getApproveDataTables(input, businessSql);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 查询审批通过的抵质押物信息
     *
     * @param input
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput querypledgeInfoPassList(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(GuaranteeConstant.GUA_PLEDGE_INFO_QUERY, user.getId());
        return guaPledgeInfoDao.querypledgeInfoPassList(input, corpAuth);
    }
    /**
     * 查询审批通过的抵质押物信息且可用金额大于0
     *
     * @param input
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput querypledgeInfoPassTables(DataTablesInput input, List<String> pDids) {
        // 获取是否允许多次抵押
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        DataTablesOutput dataTablesOutput = guaPledgeInfoDao.querypledgeInfoPassTables(input, pDids, configDTO);
        return dataTablesOutput;
    }
    /**
     * 通过id查询抵质押物信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public GuaPledgeInfoVO getGuaPledgeInfoVOById(String id) {
        // 获取是否允许多次抵押
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        return guaPledgeInfoDao.getGuaPledgeInfoVOById(id, configDTO);
    }
    /**
     * 通过id查询抵质押物信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public GuaPledgeInfoVO getPledgeInfoByIdAndPledgeAttr(String id, String pledgeAttr) {
        // 获取是否允许多次抵押
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        return guaPledgeInfoDao.getPledgeInfoByIdAndPledgeAttr(id, pledgeAttr, configDTO);
    }
    /**
     * 暂存或提交抵质押物信息
     */
    public void tempOrSubmitInfo(GuaPledgeInfoVO pledgeInfoVO, ICurrentUser user) {
        GuaPledgeInfo pledgeInfo = null;
        String message = "";// 日志信息
        String msgType = "";// 日志类型
        List<String> corpIds = new ArrayList<>();
        corpIds.add(pledgeInfoVO.getCorpId());
        if (StringUtil.isNotEmpty(pledgeInfoVO.getRegCorp())) {
            corpIds.add(pledgeInfoVO.getRegCorp());
        }
        if (StringUtils.isEmpty(pledgeInfoVO.getId())) {// 新增数据
            pledgeInfo = new GuaPledgeInfo();
            BeanUtils.copyNotNullProperties(pledgeInfoVO, pledgeInfo);
            pledgeInfo.setPledgeNo(this.getPledgeNo(SequenceEnum.CMS_GUA_PLEDGE_NO, GuaranteeConstant.GUA_PLEDGE_CODE));
            if (pledgeInfoVO.getStatus().equals(GuaranteeConstant.STATUS_TEMP)) {// 暂存
                pledgeInfo.setStatus(GuaranteeConstant.STATUS_TEMP);
                msgType = GuaranteeConstant.MESSAGE_TYPE_TEMP;// 暂存
                message = GuaranteeConstant.MSGA_INPUT + "";// 录入暂存
            } else {// 提交
                pledgeInfo.setStatus(GuaranteeConstant.STATUS_WAIT_CHECK);
                msgType = GuaranteeConstant.MESSAGE_TYPE_SUBMIT;// 提交
                message = GuaranteeConstant.MSGA_INPUT_SUBMIT;// 录入提交
            }
            pledgeInfo.setCreateBy(user.getUsername());
            pledgeInfo.setCreateDate(DateUtils.getDate());
        } else {// 修改数据
            pledgeInfo = this.get(pledgeInfoVO.getId());
            BeanUtils.copyProperties(pledgeInfoVO, pledgeInfo, "id", "pledgeNo", "createDate", "createBy");
            if (pledgeInfoVO.getStatus().equals(GuaranteeConstant.STATUS_TEMP)) {// 暂存
                pledgeInfo.setStatus(GuaranteeConstant.STATUS_TEMP);
                msgType = GuaranteeConstant.MESSAGE_TYPE_TEMP;// 暂存
                message = GuaranteeConstant.MSGA_MODIFY + "";// 修改暂存
            } else {// 提交
                pledgeInfo.setStatus(GuaranteeConstant.STATUS_WAIT_CHECK);
                msgType = GuaranteeConstant.MESSAGE_TYPE_SUBMIT;// 提交
                message = GuaranteeConstant.MSGA_REPAIR_MODIFY + "";// 修改提交
            }
            pledgeInfo.setUpdateBy(user.getUsername());
            pledgeInfo.setUpdateDate(DateUtils.getDate());
        }
        pledgeInfo.setCorpName(getCorpName(pledgeInfo.getCorpId()));
        pledgeInfo.setCurName(getCurName(pledgeInfo.getCurId()));
        pledgeInfo.setRegCorpName(getCorpName(pledgeInfo.getRegCorp()));
        this.saveOrUpdate(pledgeInfo);
        // todo:挂接审批
        if (GuaranteeConstant.STATUS_WAIT_CHECK.equals(pledgeInfo.getStatus())) {
             getStartHandler().handler(GuaWorkFlowConstant.GUA_PLEDGE_REGIST, pledgeInfo.getId(), pledgeInfo,
                    pledgeInfo.getCorpId(), user.getId());
        }
        // 日志记录
        guaLogService.saveGuaLog(pledgeInfo.getId(), pledgeInfo.getPledgeMoney(), msgType, message, user);
    }
    public void saveImport(GuaPledgeInfoVO vo, ICurrentUser user) {
        String msgType = GuaranteeConstant.MESSAGE_TYPE_TEMP;// 暂存
        String message = GuaranteeConstant.MSGA_INPUT + "";// 录入暂存
        GuaPledgeInfo pledgeInfo = new GuaPledgeInfo();
        BeanUtils.copyNotNullProperties(vo, pledgeInfo);
        pledgeInfo.setPledgeNo(this.getPledgeNo(SequenceEnum.CMS_GUA_PLEDGE_NO, GuaranteeConstant.GUA_PLEDGE_CODE));
        pledgeInfo.setStatus(GuaranteeConstant.STATUS_TEMP);
        pledgeInfo.setCreateBy(user.getUsername());
        pledgeInfo.setCreateDate(DateUtils.getDate());
        this.save(pledgeInfo);
        // 日志记录
        guaLogService.saveGuaLog(pledgeInfo.getId(), pledgeInfo.getPledgeMoney(), msgType, message, user);
    }
    /**
     * 批量提交
     *
     * @param ids  id集合
     * @param user 用户信息
     */
    public void batchSubmit(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            GuaPledgeInfo pledgeInfo = this.get(id);
            pledgeInfo.setStatus(GuaranteeConstant.STATUS_WAIT_CHECK);
            String msgType = GuaranteeConstant.MESSAGE_TYPE_SUBMIT;// 提交
            String message = GuaranteeConstant.MSGA_REPAIR_MODIFY + "";// 修改提交
            this.update(pledgeInfo);
            // 挂接审批
             getStartHandler().handler(GuaWorkFlowConstant.GUA_PLEDGE_REGIST, pledgeInfo.getId(), pledgeInfo,
                    pledgeInfo.getCorpId(), user.getId());
            // 日志记录
            guaLogService.saveGuaLog(pledgeInfo.getId(), pledgeInfo.getPledgeMoney(), msgType, message, user);
        }
    }
    /**
     * 变更后更新抵质押物信息
     */
    public void updateGuaPledgeInfo(GuaPledgeInfoVO pledgeInfoVO, ICurrentUser user) {
        // 抵质押物变更时,检查变更的日期是否在合理范围内
        this.checkDate(pledgeInfoVO);
        GuaPledgeInfo pledgeInfo = this.get(pledgeInfoVO.getId());
        BigDecimal pledgeMoney = pledgeInfo.getPledgeMoney();// 保存抵质押物未变更前的评估值
        BeanUtils.copyProperties(pledgeInfoVO, pledgeInfo, "id", "corpId", "pledgeNo", "curId", "status", "createDate",
                "createBy");
        pledgeInfo.setUpdateBy(user.getId());
        pledgeInfo.setUpdateDate(DateUtils.getDate());
        // 根据抵质押物id查询是否关联了担保合同
        List<GuaContractPledge> list = guaContractPledgeService.getGuaContractPledgeByPledgeId(pledgeInfoVO.getId());
        if (list != null && list.size() > 0) {
            // 1.如果是抵押，变更后金额变化了担保合同占用的抵押金额要发生变化
            if (GuaranteeConstant.GUA_TYPE_DY.equals(pledgeInfoVO.getPledgeAttr())) {
                GuaContractPledge guaContractPledge = list.get(0);
                guaContractPledge.setGuaMoney(pledgeInfoVO.getPledgeMoney());
                guaContractPledgeService.saveOrUpdate(guaContractPledge);
                // 查询担包合同关联担保方式信息
                GuaContractWay guaContractWay = guaContractWayService.get(guaContractPledge.getGuaContractWayId());
                // 查询担保合同信息
                if (guaContractWay != null) {
                    GuaContractInfo guaContractInfo = guaContractInfoService.get(guaContractWay.getGuaId());
                    if (guaContractInfo != null) {
                        if (GuaranteeConstant.STATUS_TEMP.equals(guaContractInfo.getStatus())
                                || GuaranteeConstant.STATUS_BACK.equals(guaContractInfo.getStatus())
                                || GuaranteeConstant.STATUS_WAIT_CHECK.equals(guaContractInfo.getStatus())) {// 担保合同的状态为暂存或打回或待审批
                            // 更新抵质押物的在途金额
                            pledgeInfo.setFreezeMoney(pledgeInfoVO.getPledgeMoney());
                        }
                        if (GuaranteeConstant.STATUS_CHECK.equals(guaContractInfo.getStatus())) {
                            // 更新占用金额
                            pledgeInfo.setOccupyMoney(pledgeInfo.getPledgeMoney());
                        }
                        // 更新担保合同的担保额度=担保额度-原评估值+变更后的评估值
                        BigDecimal guaMoney = guaContractInfo.getGuaMoney();
                        guaContractInfo.setGuaMoney(guaMoney.subtract(pledgeMoney).add(pledgeInfo.getPledgeMoney()));
                        guaContractInfoService.updateEntity(guaContractInfo);
                    }
                }
            }
            // 2.如果是质押，变更后金额发生了变化
            if (GuaranteeConstant.GUA_TYPE_ZY.equals(pledgeInfoVO.getPledgeAttr())) {
                BigDecimal temp = new BigDecimal(0);
                for (GuaContractPledge info : list) {
                    temp = temp.add(info.getGuaMoney());
                }
                // 2.1评估值较原来减少了，评估值小于担保合同占用的金额，抛出异常提示
                if (temp.compareTo(pledgeInfo.getPledgeMoney()) == 1) {
                    // 抛出异常
                    throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUAPLEDGECHANGE_MONEYERROR);
                } // 2.2评估值变化了，评估值大于担保和同占用的金额总和(直接变更)
            }
        }
        this.updateEntity(pledgeInfo);
    }
    /**
     * 根据id删除，改变状态为删除
     *
     * @param id
     */
    public void deleteGuaPledgeInfo(String id, ICurrentUser user) {
        GuaPledgeInfo pledgeInfo = this.get(id);
        pledgeInfo.setUpdateBy(user.getId());
        pledgeInfo.setUpdateDate(DateUtils.getDate());
        pledgeInfo.setStatus(GuaranteeConstant.STATUS_DELETE);
        this.updateEntity(pledgeInfo);
    }
    /**
     * 保存抵质押信息
     *
     * @param guaId
     */
    public void saveOrUpdatePledgeInfo(String guaId, String pDids, String pZdis) {
        String pzIds = pDids + pZdis;
        if (!StringUtils.isEmpty(guaId)) {// 修改担保合同，更新抵质押物信息
            // 先根据担保合同id查询原抵押物信息
            List<GuaContractPledge> pledgeList = guaContractPledgeService.getGuaContractPledge(guaId);
            // 还原抵质押物在途金额
            convertPledgeMoney(pledgeList);
        }
        // 根据id更新修改后的在途金额
        if (StringUtils.isNotEmpty(pzIds)) {
            pzIds = pzIds.substring(0, pzIds.length() - 1);// 截去最后一个，
            String[] ids = pzIds.split(";");
            for (String str : ids) {// 遍历id,
                String[] tempStr = str.split(",");
                GuaPledgeInfo info = this.get(tempStr[0]);
                if (info != null) {
                    if (info.getFreezeMoney() != null) {
                        info.setFreezeMoney(info.getFreezeMoney().add(new BigDecimal(tempStr[1])));
                    } else {
                        info.setFreezeMoney(new BigDecimal(tempStr[1]));
                    }
                    this.update(info);
                }
            }
        }
    }
    /**
     * 保存抵质押信息
     *
     * @param pledgeIds 抵质押物相关信息
     * @author luxy
     */
    public void savePledgeInfo(String pledgeIds) {
        // 根据id更新修改后的在途金额
        if (StringUtils.isNotEmpty(pledgeIds)) {
            pledgeIds = pledgeIds.substring(0, pledgeIds.length() - 1);// 截去最后一个，
            String[] ids = pledgeIds.split(";");
            for (String str : ids) {// 遍历id,
                String[] tempStr = str.split(",");
                GuaPledgeInfo info = this.get(tempStr[0]);
                if (info != null) {
                    if (info.getFreezeMoney() != null) {
                        if (GuaranteeConstant.GUA_TYPE_DY.equals(info.getPledgeAttr())) {
                            info.setFreezeMoney(info.getPledgeMoney());
                        } else {
                            info.setFreezeMoney(info.getFreezeMoney().add(new BigDecimal(tempStr[1])));
                        }
                    } else {
                        info.setFreezeMoney(new BigDecimal(tempStr[1]));
                    }
                    this.update(info);
                }
            }
        }
    }
    /**
     * 还原抵质押物在途金额
     *
     * @return
     */
    public void convertPledgeMoney(List<GuaContractPledge> pledgeList) {
        for (GuaContractPledge pledge : pledgeList) {
            BigDecimal guaMoney = pledge.getGuaMoney();// 原金额
            if (StringUtils.isNotEmpty(pledge.getPledgeId())) {
                GuaPledgeInfo info = this.get(pledge.getPledgeId());
                if (info != null) {
                    if (null != info.getFreezeMoney() && (info.getFreezeMoney().compareTo(BigDecimal.ZERO) == 1)) {
                        if (GuaranteeConstant.GUA_TYPE_DY.equals(info.getPledgeAttr())) {
                            List<GuaPledgeOccupyVO> guaPledgeOccupyVOS = guaPledgeOccupyService
                                    .getPledgeOccupyListFrozen(info.getId());
                            if (guaPledgeOccupyVOS.size() <= 1) {
                                info.setFreezeMoney(BigDecimal.ZERO);
                            }
                        } else {
                            info.setFreezeMoney(info.getFreezeMoney().subtract(guaMoney));
                        }
                    }
                    this.update(info);
                }
            }
        }
        this.guaPledgeInfoDao.flush();
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(GuaPledgeInfo entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(GuaPledgeInfo entity) {
        super.updateEntity(entity);
        this.getSession().flush();
        this.getSession().clear();
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(GuaPledgeInfo entity) {
        super.saveOrUpdate(entity);
        this.getSession().flush();
        this.getSession().clear();
    }
    @Override
    public BaseDao<GuaPledgeInfo, String> getEntityDao() {
        return guaPledgeInfoDao;
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    /**
     * 审批完成回调函数
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String bussinessKey = workFlowResult.getBusinessKey();
        GuaPledgeInfo pledgeInfo = this.get(bussinessKey,true);
        ICurrentUser user = workFlowResult.getUser();
        String message = "";// 日志信息
        String msgType = "";// 日志类型
        boolean result = workFlowResult.isPass();// 是否通过
        if (result) {
            pledgeInfo.setStatus(GuaranteeConstant.STATUS_CHECK);
            msgType = GuaranteeConstant.MESSAGE_TYPE_CHECK_PASS;// 审批通过
            message = GuaranteeConstant.MSGA_CHECKOK + "";
            GuaPledgeInfoHis guaPledgeHis = new GuaPledgeInfoHis();
            BeanUtils.copyNotNullProperties(pledgeInfo, guaPledgeHis);
            guaPledgeHis.setPledgeId(pledgeInfo.getId());
            guaPledgeHis.setId(null);
            guaPledgeChangeService.saveEntity(guaPledgeHis);
        } else {
            pledgeInfo.setStatus(GuaranteeConstant.STATUS_BACK);
            msgType = GuaranteeConstant.MESSAGE_TYPE_CHECK_BACK;// 审批打回
            message = GuaranteeConstant.MSGA_CHECKBACK;
        }
        this.updateEntity(pledgeInfo);
        // 日志记录
        guaLogService.saveGuaLog(pledgeInfo.getId(), pledgeInfo.getPledgeMoney(), msgType, message, user);
    }
    /**
     * 生成抵质押物编号
     *
     * @author luxy
     */
    protected String getPledgeNo(SequenceEnum senum, String prefix) {
        return SequenceSingleton.getSequence(senum, prefix);
    }
    /**
     * 通过抵质押id得到抵质押物被担保合同占用的信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<GuaPledgeOccupyInfoVO> getGuaPledgeOccupyInfo(String id) {
        return guaPledgeInfoDao.getGuaPledgeOccupyInfo(id);
    }
    /**
     * 抵质押物变更时,检查变更的日期是否在合理范围内
     *
     * @param guaPledgeInfoVO
     */
    public void checkDate(GuaPledgeInfoVO guaPledgeInfoVO) {
        // 根据抵质押物id查询是否关联了担保合同
        List<GuaContractPledge> list = guaContractPledgeService.getGuaContractPledgeByPledgeId(guaPledgeInfoVO.getId());
        for (GuaContractPledge obj : list) {
            // 查询担保合同关联担保方式信息
            GuaContractWay guaContractWay = guaContractWayService.getGuaContractWayById(obj.getGuaContractWayId());
            // 通过id得到担保合同信息
            if (guaContractWay != null) {
                GuaContractInfoVO guaContractInfoVO = guaContractInfoService
                        .getGuaContractInfoById(guaContractWay.getGuaId());
                // 判断:1当变更后的起始日期大于担保合同的起始日期,提示：变更的抵质押物开始日期大于挂接的担保合同的开始日期，请先变更担保合同的日期
                if (guaContractInfoVO.getStartDate().before(guaPledgeInfoVO.getStartDate())) {
                    // 抛出异常
                    throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUAPLEDGECHANGE_DATEERROR);
                }
                // 判断:2当变更后的结束日期小于担保合同的结束日期,提示：变更的抵质押物结束日期小于挂接的担保合同的结束日期，请先变更担保合同的日期
                if (guaPledgeInfoVO.getEndDate().before(guaContractInfoVO.getEndDate())) {
                    // 抛出异常
                    throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUAPLEDGECHANGE_DATEERROR);
                }
            }
        }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = guaPledgeInfoDao.queryBusBillByCorpId(corpId, null);
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.GUA_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.GUA_VALID_DEL_PLEDGE_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 抵质押物暂存，打回，待审批的数据
            int dataSize = guaPledgeInfoDao.queryBusBillByCorpId(corpId, "0,-1,11");
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.GUA_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.GUA_VALID_DISABLE_PLEDGE_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(GuaPledgeInfoVO guaPledgeInfoVO, Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<GuaPledgeInfoVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (GuaPledgeInfoVO vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 单位
                if (EmptyUtils.isEmpty(vo.getCorpName())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_CORP_ISNULL);
                } else {
                    CorpDTO corpDTO = corpService.getCorpIdByName(vo.getCorpName());
                    if (EmptyUtils.isEmpty(corpDTO)) {
                        errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_CORP_ERROR);
                    } else {
                        vo.setCorpId(corpDTO.getId());
                    }
                }
                // 抵质押物性质
                String typeAttr = null;
                if (EmptyUtils.isEmpty(vo.getPledgeAttrName())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_ATTR_ISNULL);
                } else {
                    if (vo.getPledgeAttrName().equals("抵押")) {
                        vo.setPledgeAttr("0");
                        typeAttr = "0";
                    } else if (vo.getPledgeAttrName().equals("质押")) {
                        vo.setPledgeAttr("1");
                        typeAttr = "1";
                    } else {
                        errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_ATTR_ERROR);
                    }
                }
                // 抵质押时间
                if (EmptyUtils.isEmpty(vo.getStartDate())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_STARTDATE_ISNULL);
                } else {
                    if (EmptyUtils.isEmpty(vo.getEndDate())) {
                        errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_ENDDATE_ISNULL);
                    } else if (vo.getStartDate().compareTo(vo.getEndDate()) > 0) {
                        errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_DATE_ERROR);
                    }
                }
                // 抵质押物类型
                if (EmptyUtils.isEmpty(vo.getTypeName())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_TYPE_ISNULL);
                } else {
                    if (!EmptyUtils.isEmpty(typeAttr)) {
                        GuaPledgeType guaPledgeType = guaPledgeTypeService.getListByTypeCode(vo.getTypeName(),
                                typeAttr);
                        if (EmptyUtils.isEmpty(guaPledgeType)) {
                            errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_TYPE_ERROR);
                        } else {
                            vo.setPledgeType(guaPledgeType.getId());
                        }
                    }
                }
                // 抵质押物名称
                if (EmptyUtils.isEmpty(vo.getPledgeName())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_NAME_ISNULL);
                }
                // 抵(质)押权人
                if (EmptyUtils.isEmpty(vo.getPledgeOrg())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_ORG_ISNULL);
                }
                // 币别
                if (EmptyUtils.isEmpty(vo.getCurName())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_CUR_ISNULL);
                } else {
                    String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
                    if (EmptyUtils.isEmpty(curId)) {
                        errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_CUR_ERROR);
                    } else {
                        vo.setCurId(curId);
                    }
                }
                // 评估值
                if (EmptyUtils.isEmpty(vo.getPledgeMoney())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_MONEY_ISNULL);
                }
                // 市场价值
                if (EmptyUtils.isEmpty(vo.getPledgeNetWorth())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_NETWORTH_ISNULL);
                }
                // 填报单位
                if (!EmptyUtils.isEmpty(vo.getRegCorpName())) {
                    CorpDTO corpDTO = corpService.getCorpIdByName(vo.getRegCorpName());
                    if (EmptyUtils.isEmpty(corpDTO)) {
                        errorMsgList.add(GuaranteeConstant.CMS_GUAPLEDGE_REGCORP_ERROR);
                    } else {
                        vo.setRegCorp(corpDTO.getId());
                    }
                }
                vo.addErrorMsg(errorMsgList);
            }
        }
    }
    /**
     * 获取单位直属网点
     */
    public Map<String, String> getCenNetId(String corpId) {
        Map<String, String> corp = new HashMap<String, String>();
        // 获取单位直属网点
        CorpDTO netCorp = iCorpService.getNetByCorpId(corpId);
        if (StringUtils.isNotEmpty(netCorp)) {
            corp.put("corpId", netCorp.getId());
            corp.put("corpName", netCorp.getName());
            return corp;
        }
        // 如果没有直属网点就是本网点
        corp.put("corpId", corpId);
        CorpDTO corpDTO = iCorpService.get(corpId);
        Optional.ofNullable(corpDTO).ifPresent(corpDO -> corp.put("corpName", corpDO.getName()));
        return corp;
    }
    private String getCorpName(String corpId) {
        if (StringUtils.isEmpty(corpId)) {
            return null;
        }
        return iCorpService.getCorpName(corpId);
    }
    private String getCurName(String curId) {
        if (StringUtils.isEmpty(curId)) {
            return null;
        }
        BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(curId);
        return EmptyUtils.isEmpty(currencyDTO) ? null : currencyDTO.getCurName();
    }
}
