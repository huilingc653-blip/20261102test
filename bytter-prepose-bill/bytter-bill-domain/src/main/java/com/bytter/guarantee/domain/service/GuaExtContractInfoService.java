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
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.GuaWorkFlowConstant;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.dao.GuaContractInfoDao;
import com.bytter.guarantee.domain.dao.GuaExguaranteeCorpDao;
import com.bytter.guarantee.domain.dao.GuaExtContractInfoDao;
import com.bytter.guarantee.domain.entity.GuaContractInfo;
import com.bytter.guarantee.domain.entity.GuaContractPledge;
import com.bytter.guarantee.domain.entity.GuaContractWay;
import com.bytter.guarantee.domain.entity.GuaExguaranteeAmount;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author luxy
 * @Title: GuaExtContractInfoService
 * @ProjectName T3(3.2)
 * @Description: 对外担保合同登记服务层
 * @date 2020/5/9 9:42
 */
@Service("guaExtContractInfoService")
@Transactional
@WorkFlow(value = { GuaWorkFlowConstant.GUA_EXTCONTRACT_REGIST })
@DubboService(interfaceClass = IWorkFlowListener.class,group = GuaWorkFlowConstant.GUA_EXTCONTRACT_REGIST )
public class GuaExtContractInfoService extends BaseService<GuaContractInfo, String>
        implements IWorkFlowListener, ITaskService, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private GuaExtContractInfoDao guaExtContractInfoDao;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private GuaPledgeInfoService guaPledgeInfoService;
    @Autowired
    private GuaContractWayService guaContractWayService;
    @Autowired
    private GuaContractPledgeService guaContractPledgeService;
    @Autowired
    private GuaLogService guaLogService;
    @Autowired
    private GuaContractInfoDao guaContractInfoDao;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IConService conCommonService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IUserService userService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService guaPledgeOccupyServiceImpl;
    @Autowired
    private GuaExguaranteeAmountService guaExguaranteeAmountService;
    @Autowired
    private GuaExguaranteeCorpDao guaExguaranteeCorpDao;
    @Override
    public BaseDao<GuaContractInfo, String> getEntityDao() {
        return guaExtContractInfoDao;
    }
    /**
     * 获取列表数据
     */
    public DataTablesOutput getDataTables(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(GuaranteeConstant.GUA_EXTCONTRACT_INFO_QUERY, user.getId());
        DataTablesOutput data = guaExtContractInfoDao.extContractInfoData(input, corpAuth);
        data.setData(this.setGuaWayName(data.getData()));
        return data;
    }
    /**
     * 保存担保合同数据
     */
    public void tempOrSubmit(GuaContractInfoVO contractVO, ICurrentUser user) {
        GuaContractInfo contractInfo = null;
        BigDecimal guaMoney = BigDecimal.ZERO;
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        for (GuaContractWayVO guaContractWayVO : guaDtls) {
            guaMoney = guaMoney.add(guaContractWayVO.getMoney());
        }
        if (StringUtils.equals(contractVO.getInnerGua(), "1")) {
            CorpDTO corpDTO = corpService.get(contractVO.getDebtorPerson());
            Optional.ofNullable(corpDTO).ifPresent(corpDO -> contractVO.setDebtorPerson(corpDO.getName()));
        }
        String message = "";// 日志信息
        String msgType = "";// 日志类型
        if (StringUtils.isEmpty(contractVO.getId())) {// 新增数据
            contractInfo = new GuaContractInfo();
            BeanUtils.copyNotNullProperties(contractVO, contractInfo);
            contractInfo.setDrawMoneyDate(contractVO.getDrawMoneyDate());
            contractInfo.setGuaMoney(guaMoney);
            contractInfo.setIsChange(GuaranteeConstant.GUA_NO_CHANGE);// 未变更
            if (contractVO.getStatus().equals(GuaranteeConstant.STATUS_TEMP)) {// 暂存
                contractInfo.setStatus(GuaranteeConstant.STATUS_TEMP);
                msgType = GuaranteeConstant.MESSAGE_TYPE_TEMP;// 暂存
                message = GuaranteeConstant.MSGA_INPUT + "";// 录入暂存
            } else {// 提交
                contractInfo.setStatus(GuaranteeConstant.STATUS_WAIT_CHECK);
                msgType = GuaranteeConstant.MESSAGE_TYPE_SUBMIT;// 提交
                message = GuaranteeConstant.MSGA_INPUT_SUBMIT;// 录入提交
            }
            contractInfo.setCreateBy(user.getId());
            contractInfo.setCreateName(user.getUsername());
            contractInfo.setCreateDate(DateUtils.getDate());
        } else {// 修改数据
            contractInfo = this.get(contractVO.getId());
            BeanUtils.copyProperties(contractVO, contractInfo, "id", "createDate", "createBy");
            contractInfo.setDrawMoneyDate(contractVO.getDrawMoneyDate());
            contractInfo.setGuaMoney(guaMoney);
            if (contractVO.getStatus().equals(GuaranteeConstant.STATUS_TEMP)) {// 暂存
                contractInfo.setStatus(GuaranteeConstant.STATUS_TEMP);
                msgType = GuaranteeConstant.MESSAGE_TYPE_TEMP;// 暂存
                message = GuaranteeConstant.MSGA_MODIFY + "";// 修改暂存
            } else {// 提交
                contractInfo.setStatus(GuaranteeConstant.STATUS_WAIT_CHECK);
                msgType = GuaranteeConstant.MESSAGE_TYPE_SUBMIT;// 提交
                message = GuaranteeConstant.MSGA_REPAIR_MODIFY + "";// 修改提交
            }
            //暂存的时候不再更新数据 这个字段被变更 最后一次更新时间使用
//            contractInfo.setUpdateBy(user.getId());
//            contractInfo.setUpdateDate(DateUtils.getDate());
            contractInfo.setIsChange(GuaranteeConstant.GUA_NO_CHANGE);// 未变更
        }
        // 更新抵质押物信息
        if (!StringUtils.isEmpty(contractInfo.getId())) {// 修改担保合同，更新抵质押物信息
            // 先根据担保合同id查询原抵押物信息
            List<GuaContractPledge> pledgeList = guaContractPledgeService.getGuaContractPledge(contractInfo.getId());
            // 还原抵质押物在途金额
            if (null != pledgeList && pledgeList.size() > 0) {
                guaPledgeInfoService.convertPledgeMoney(pledgeList);
            }
        }
        for (GuaContractWayVO guaContractWayVO : guaDtls) {// 冻结抵质押物额度
            if (StringUtils.isNotEmpty(guaContractWayVO.getPledgeIds())) {
                guaPledgeInfoService.savePledgeInfo(guaContractWayVO.getPledgeIds());
            }
        }
        if (GuaranteeConstant.RELATE_CON_NO.equals(this.relateCon())) { // 担保对接合同管理为否
            contractInfo.setConId("");
            contractInfo.setConTypeId("");
        }
        //单位名称与币别
        if (StringUtils.isNotEmpty(contractInfo.getCorpId()) && StringUtils.isEmpty(contractInfo.getCorpName())) {
            contractInfo.setCorpName(corpService.get(contractInfo.getCorpId()).getName());
        }
        if (StringUtils.isNotEmpty(contractInfo.getCurId()) && StringUtils.isEmpty(contractInfo.getCurName())) {
            BtCurrencyDTO currencyDTO = currencyService.getObjectById(contractInfo.getCurId());
            contractInfo.setCurName(currencyDTO.getCurName());
        }
        this.saveOrUpdate(contractInfo);
        this.getSession().flush();
        // 查询原担保合同关联担保方式表信息
        List<GuaContractWayVO> listGuaContractWay = guaContractWayService.getGuaContractWay(contractInfo.getId());
        // 更新担保合同关联担保方式表
        contractVO.setId(contractInfo.getId());
        guaContractInfoService.saveGuaContractWay(contractVO);
        // 更新担保方式关联抵质押物表
        guaContractInfoService.saveGuaWayPledge(contractVO, listGuaContractWay);
        // 抵质押物占用表数据存储
        List<GuaPledgeOccupyDTO> occupyDTOS = guaContractInfoService.setPledgeOccupyInfo(contractVO,
                ConfigConstant.EXTGUACONTRACT);
        guaPledgeOccupyServiceImpl.freezePledgeOccupy(occupyDTOS);
        // 日志记录
        guaLogService.saveGuaLog(contractInfo.getId(), contractInfo.getGuaMoney(), msgType, message, user);
        // 挂接审批
        if (GuaranteeConstant.STATUS_WAIT_CHECK.equals(contractInfo.getStatus())) {
             getStartHandler().handler(GuaWorkFlowConstant.GUA_EXTCONTRACT_REGIST, contractInfo.getId(), contractInfo,
                    contractInfo.getCorpId(), user.getId());
        }
    }
    /**
     * 删除担保合同
     */
    public void deleteContract(String id, ICurrentUser user) {
        guaContractInfoService.delGuaContractInfo(id, user);
    }
    /**
     * 获取待审批数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getApproveDataTables(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(GuaWorkFlowConstant.GUA_EXTCONTRACT_REGIST, input);
        String businessSql = null;
        if(!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())){
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        }
        DataTablesOutput dp = guaExtContractInfoDao.getApproveDataTables(input, businessSql);
        dp.setData(this.setGuaWayName(dp.getData()));
        return workFlowBusiness.merge(dp);
    }

    public void checkExCorpAmount(GuaContractInfo info) {
        BigDecimal allMoney = BigDecimal.ZERO;
        List<String> corplist = new ArrayList<>();
        corplist.add(info.getCorpId());
        List<GuaExtContractInfoVO> infoVOList = guaExtContractInfoDao.queryExCorpAmount(corplist, info.getId());
        for (GuaExtContractInfoVO infoVO : infoVOList) {
            allMoney = allMoney.add(infoVO.getGuaMoney());
        }
        List<GuaExguaranteeCorpVO> exguaranteeCorpVOS = guaExguaranteeCorpDao.checkGuaranteeCorp(info.getCorpId(), info.getCurId(), null);
        if (!exguaranteeCorpVOS.isEmpty()) {
            GuaExguaranteeAmount amount = guaExguaranteeAmountService.get(exguaranteeCorpVOS.get(0).getExAmountId());
            BigDecimal amountMoney = amount.getAssets().multiply(amount.getProportion());
            if (amountMoney.compareTo(allMoney.add(info.getGuaMoney())) < 0) {// 限制额度小于已担保金额加上此次担保金额则报错
                throw new ParameterException(GuaranteeMessageEnum.CMS_EXGUARANTEE_CORP_AMOUNT_OUT);
            }
        }
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String bussinessKey = workFlowResult.getBusinessKey();
        GuaContractInfo info = this.get(bussinessKey,true);
        boolean result = workFlowResult.isPass();// 是否通过
        if (result) {
            // 限额检查
            checkExCorpAmount(info);
            info.setStatus(GuaranteeConstant.STATUS_CHECK);
            // 如果担保对接了合同管理，生成相应的关联信息
            this.toRelateCon(info);
        } else {
            info.setStatus(GuaranteeConstant.STATUS_BACK);
        }
        this.updateEntity(info);
        if (result) {
            // 审批通过后,将抵质押物表中在途的金额改为占用的金额
            guaContractInfoService.updateGuaPledgeInfoMoney(info);
            //TODO 重复产生了2笔 对内担保合同数据
//            guaContractInfoService.createContractUseTrack(info, workFlowResult.getUser(), GuaranteeConstant.CON_TYPE_1);
            // 抵质押物占用表数据改为占用状态
            List<GuaPledgeOccupyDTO> occupyDTOS = guaPledgeOccupyServiceImpl.getPledgeOccupy(info.getId(),
                    info.getGuaNo());
            guaPledgeOccupyServiceImpl.addPledgeOccupy(occupyDTOS);
            saveInnerGuaContractInfo(info);// 生成对外担保合同信息
        }
    }
    /**
     * <br>
     * <b>功能：</b> 生成对内担保单位的担保合同信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/10/28 0028 <br>
     */
    public void saveInnerGuaContractInfo(GuaContractInfo contractInfo) {
        String autoExtGuaCon = CommonConstant.NO;// 是否生成被担保合同
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.GUA, GuaranteeConstant.AUTO_EXT_GUA_CON);
        if (!EmptyUtils.isEmpty(configDTO)) {
            autoExtGuaCon = configDTO.getValue();
        }
        if (!CommonConstant.YES.equals(autoExtGuaCon)) {
            return;
        }
        String innerGua = contractInfo.getInnerGua();// 对内担保标识
        if (EmptyUtils.isEmpty(innerGua)) {
            innerGua = CommonConstant.NO;
        }
        if (CommonConstant.NO.equals(innerGua)) {
            return;
        }
        GuaContractInfo conInfo = new GuaContractInfo();
        BeanUtils.copyProperties(contractInfo, conInfo);
        conInfo.setId(null);
        CorpDTO corpDTO = corpService.getCorpIdByName(contractInfo.getDebtorPerson());
//        CorpDTO corpDTO = corpService.get(contractInfo.getDebtorPerson());// 债务人
        if (EmptyUtils.isEmpty(corpDTO)) {
            return;
        }
        conInfo.setCorpId(corpDTO.getId());// 被担保合同的融资主体
        conInfo.setDebtorPerson(corpDTO.getName());// 被担保合同的债务人
        CorpDTO guaCorpDTO = corpService.get(contractInfo.getCorpId());// 担保单位
        conInfo.setGuaPerson(guaCorpDTO.getName());// 担保单位
        conInfo.setStatus(CommonConstant.STATUE_TEMP);// 暂存状态
        conInfo.setBusinessType(GuaranteeConstant.CON_TYPE_0);// 担保合同类型为被担保合同
        conInfo.setCreateBy(null);
        conInfo.setCreateDate(null);
        saveEntity(conInfo);// 保存对外担保信息
        this.getSession().flush();
        //TODO 保存对外担保信息
        List<GuaContractWayVO> guaContractWayList = guaContractWayService.getGuaContractWayVO(contractInfo.getId());
        for (GuaContractWayVO guaContractWay : guaContractWayList) {
            GuaContractWay guaContractDO = new GuaContractWay();
            guaContractDO.setGuaId(conInfo.getId());
            guaContractDO.setWayCodes(guaContractWay.getWayCodes());
            guaContractDO.setMoney(guaContractWay.getMoney());
            guaContractWayService.saveOrUpdate(guaContractDO);
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 对外担保是否对接合同管理参数
     *
     * @return
     */
    public String relateCon() {
        return configService.getConfig(ConfigConstant.GUA, GuaranteeConstant.EXTCON_RELATE_CON).getValue();
    }
    /**
     * 查询系统参数，对外担保合同对接合同管理模式
     */
    public String conType() {
        return configService.getConfig(ConfigConstant.GUA, GuaranteeConstant.EXTCON_CON_TYPE).getValue();
    }
    /**
     * 获取对外担保终止列表数据
     */
    public DataTablesOutput endDataTables(DataTablesInput input, ICurrentUser user) {
        List<String> corps = null;
        DataTablesOutput data = guaExtContractInfoDao.endDataTables(input, corps);
        data.setData(this.setGuaWayName(data.getData()));
        return data;
    }
    /**
     * 获取对外担保终止待审批列表数据
     */
    public DataTablesOutput endApproveData(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(GuaWorkFlowConstant.GUA_EXTCONTRACT_END,
                input);
        List<String> corps = null;
        DataTablesOutput dp = guaExtContractInfoDao.endApproveData(input, corps);
        dp.setData(this.setGuaWayName(dp.getData()));
        return workFlowBusiness.merge(dp);
    }
    /**
     * 通过担保合同id查询担保合同信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public GuaExtContractInfoVO getGuaExtContractInfoById(String id) {
        GuaExtContractInfoVO vo = guaExtContractInfoDao.getGuaExtContractInfoById(id);
        List<GuaContractWayVO> list = guaContractWayService.getGuaContractWayVO(id);
        return getGuaExtContractInfoVO(vo, list);
    }

    private GuaExtContractInfoVO toVo(GuaContractInfo contractInfo) {
        GuaExtContractInfoVO vo = new GuaExtContractInfoVO();
        BeanUtils.copyProperties(contractInfo, vo);
        List<GuaContractWayVO> list = guaContractWayService.getGuaContractWayVO(contractInfo.getId());
        return getGuaExtContractInfoVO(vo, list);
    }

    @NotNull
    private GuaExtContractInfoVO getGuaExtContractInfoVO(GuaExtContractInfoVO vo, List<GuaContractWayVO> list) {
        guaContractInfoService.setPledgeInfo(list);// 设置担保方式关联的抵质押物信息
        vo.setGuaDtls(list);
        String DYids = "";
        for (GuaContractWayVO guaContractWayVO : list) {
            if (StringUtils.isNotEmpty(guaContractWayVO.getpDids())) {
                DYids += guaContractWayVO.getpDids();
            }
        }
        vo.setpDids(DYids);
        return vo;
    }

    /**
     * 担保合同查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput searchDataTables(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds("gua:contract:infoplan:query", user.getId());
        DataTablesOutput data = guaExtContractInfoDao.searchDataTables(input, corpAuth);
        data.setData(this.setGuaWayName(data.getData()));
        return data;
    }
    /**
     * 生成合同
     *
     * @param info
     */
    public void toRelateCon(GuaContractInfo info) {
        // 业务发起人
        UserDTO user = null;
        if (StringUtils.isNotEmpty(info.getUpdateBy())) {
            user = userService.getByUserId(info.getUpdateBy());
        } else {
            user = userService.getByUserId(info.getCreateBy());
        }
        if (StringUtils.isNotEmpty(info.getConId())) { // 业务调用合同
            conCommonService.hookConOp(info.getConId(), info.getId(), user);
        }
        if (StringUtils.isNotEmpty(info.getConTypeId())) { // 业务生成合同
            List<GuaExtContractInfoVO> list = new ArrayList<GuaExtContractInfoVO>(1);
            list.add(this.toVo(info));
            GuaExtContractInfoVO vo = this.setGuaWayName(list).get(0);
            ConInfoDTO conDTO = new ConInfoDTO();
            conDTO.setCorpId(info.getCorpId());
            conDTO.setBusId(info.getId());
            conDTO.setConNo(info.getContractCode());
            conDTO.setConName(vo.getCorpName() + GuaranteeConstant.CON_NAME);
            conDTO.setTypeId(info.getConTypeId());
            conDTO.setBeginDate(info.getStartDate());
            conDTO.setEndDate(info.getEndDate());
            conDTO.setOutBusData(vo);
            conCommonService.registerConOpExtGua(conDTO, user);
            info.setConId(conDTO.getId());
        }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = guaContractInfoDao.queryBusBillByCorpId(corpId, GuaranteeConstant.CON_TYPE_1, null);
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.GUA_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.GUA_VALID_DEL_EXTCONTRACT_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
    // 设置列表上显示的担保方式名称
    public List<GuaExtContractInfoVO> setGuaWayName(List<GuaExtContractInfoVO> list) {
        for (int i = 0; i < list.size(); i++) {
            StringBuilder guaWayName = new StringBuilder();
            StringBuilder guaWayCode = new StringBuilder();
            // 根据担保合同id,得到担保方式id,用于初始化复选框
            List<GuaContractWayVO> guaContractWayList = guaContractWayService.getGuaContractWay(list.get(i).getId());
            for (GuaContractWayVO way : guaContractWayList) {
                GuaWayVO guaWay = this.getGuaWayById(way.getWayCodes());
                if (EmptyUtils.isBlank(guaWayName.toString())) {
                    guaWayName.append(guaWay.getName());
                    guaWayCode.append(guaWay.getCode());
                } else {
                    guaWayName.append("、").append(guaWay.getName());
                    guaWayCode.append(",").append(guaWay.getCode());
                }
            }
            list.get(i).setGuaTypeName(guaWayName.toString());
            list.get(i).setGuaWayCode(guaWayCode.toString());
        }
        return list;
    }
    /**
     * 通过id查询担保方式
     *
     * @return
     */
    public GuaWayVO getGuaWayById(String id) {
        return guaContractInfoDao.getGuaWayById(id);
    }
    /**
     * 定时任务，对外担保到期提醒
     */
    @Override
    @XxlJob("GuaExtContractInfoService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.GUA, GuaranteeConstant.EXTCON_REMIND_DAY);// 对外到期提醒天数
        Integer notifyDays = configDTO.toInteger(0);
        Date startDate = DateUtils.getDateYMD();
        Date endDate = DateUtils.addDay(startDate, notifyDays);
        // 查询对外担保到期数据
        List<GuaExtContractInfoVO> conts = guaExtContractInfoDao.endContracts(null, startDate, endDate);
        if (!EmptyUtils.isEmptyList(conts)) {
            Map<String, Object> param = new HashMap<>();
            int count = conts.size();
            param.put("count", count);
            notificationService.send("", "", NotificationConstant.CMS.GUA_EXTCON_REMIND, param);
        }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = guaContractInfoDao.queryBusBillByCorpId(corpId, GuaranteeConstant.CON_TYPE_1, "0,-1,11");
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.GUA_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.GUA_VALID_DISABLE_EXTCONTRACT_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
}
