package com.bytter.guarantee.domain.service;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.guarantee.domain.dao.GuaContractInfoDao;
import com.bytter.guarantee.domain.entity.*;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.guarantee.constant.*;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.*;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.api.IConService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author luxy
 * @version 1.0
 * @Title:GuaContractInfoService
 * @Description:被担保合同管理业务逻辑层
 * @date 2018年12月12日 下午1:34:09
 */
@Service("guaContractInfoService")
@Transactional
@WorkFlow(value = {GuaWorkFlowConstant.GUA_CONTRACT_REGIST})
@DubboService(interfaceClass = IWorkFlowListener.class,group = GuaWorkFlowConstant.GUA_CONTRACT_REGIST)
public class GuaContractInfoService extends BaseService<GuaContractInfo, String> implements IWorkFlowListener,
        IUserExcelVerifyHandler<GuaContractExcelVO>, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private GuaContractInfoDao guaContractInfoDao;
   @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private GuaPledgeInfoService guaPledgeInfoService;
    @Autowired
    private GuaContractWayService guaContractWayService;
    @Autowired
    private GuaContractPledgeService guaContractPledgeService;
    @DubboReference
    private ICorpService corpService;
    @DubboReference
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private GuaLogService guaLogService;
    @DubboReference
    private Ii18nService ii18nService;
    @DubboReference
    private IConfigService configService;
    @Resource
    private IConService conCommonService;
    @DubboReference
    private IUserService userService;
    @Autowired
    private GuaContractChangeService changeService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService guaPledgeOccupyServiceImpl;

    @DubboReference(check = false)
    private IDictService iDictService;
    /**
     * 担保合同信息列表
     *
     * @param input 查询参数
     * @param user  用户信息
     * @return 分页数据
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<GuaContractInfoVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(GuaranteeConstant.GUA_CONTRACT_INFO_QUERY, user.getId());
        DataTablesOutput<GuaContractInfoVO> data = guaContractInfoDao.getDataTablesUnion(input, corpAuth);
        data.setData(this.setGuaWayName(data.getData()));
        data.setData(this.setDebtorPerson(data.getData()));
        return data;
    }
    /**
     * 被担保合同查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput searchGuaContractTables(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(GuaranteeConstant.GUA_CONTRACT_INFO_QUERY, user.getId());
        DataTablesOutput data = guaContractInfoDao.searchGuaContractTables(input, corpAuth);
        data.setData(this.setGuaWayName(data.getData()));
        data.setData(this.setDebtorPerson(data.getData()));
        List<GuaContractInfoVO> contractInfoVOList = data.getData();
        contractInfoVOList.forEach(a -> {
            String s = iDictService.get("guaContract.status", a.getStatus());
            a.setStatusName(s);
        });
        return data;
    }
    /**
     * 通过担保合同id查询担保合同信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public GuaContractInfoVO getGuaContractInfoById(String id) {
        GuaContractInfoVO vo = guaContractInfoDao.getGuaContractInfoById(id);
        if (null != vo) {
            List<GuaContractWayVO> list = guaContractWayService.getGuaContractWayVO(id);
            setPledgeInfo(list);// 设置担保方式关联的抵质押物信息
            vo.setGuaDtls(list);
            String DYids = "";
            for (GuaContractWayVO guaContractWayVO : list) {
                if (StringUtils.isNotEmpty(guaContractWayVO.getpDids())) {
                    DYids += guaContractWayVO.getpDids();
                }
            }
            vo.setpDids(DYids);
            if (StringUtils.isNotEmpty(vo.getDebtorPerson())) {
                CorpDTO corpDTO = corpService.get(vo.getDebtorPerson());
                if (!EmptyUtils.isEmpty(corpDTO)) {
                    vo.setDebtorPerson(corpDTO.getName());
                }
            }
            vo.setDebtorPersonName(vo.getDebtorPerson());
        }
        return vo;
    }
    /**
     * 暂存或提交担保合同信息
     */
    public void tempOrSubmitInfo(GuaContractInfoVO contractVO, ICurrentUser user) {
        BigDecimal guaMoney = BigDecimal.ZERO;
        GuaContractInfo contractInfo = null;
        //TODO 补充名称信息
        CorpDTO corpDTO = corpService.get(contractVO.getCorpId());
        Optional.ofNullable(corpDTO).ifPresent(corpDO -> contractVO.setCorpName(corpDO.getName()));
        BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(contractVO.getCurId());
        Optional.ofNullable(currencyDTO).ifPresent(currencyDO -> contractVO.setCurName(currencyDO.getCurName()));
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        for (GuaContractWayVO guaContractWayVO : guaDtls) {
            guaMoney = guaMoney.add(guaContractWayVO.getMoney());
        }
        String message = "";// 日志信息
        String msgType = "";// 日志类型
        if (StringUtils.isEmpty(contractVO.getId())) {// 新增数据
            // 新增时校验担保编号是否重复
            List<GuaContractInfo> guaContractInfoList = guaContractInfoDao.findAllByGuaNo(contractVO.getGuaNo(), contractVO.getId());
            if (!EmptyUtils.isEmptyList(guaContractInfoList)) {
                throw new BusinessException(GuaranteeMessageEnum.CMS_EXGUARANTEE_REPEATED);
            }
            contractInfo = new GuaContractInfo();
            BeanUtils.copyNotNullProperties(contractVO, contractInfo);
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
            // 新增时校验担保编号是否重复
            List<GuaContractInfo> guaContractInfoList = guaContractInfoDao.findAllByGuaNo(contractVO.getGuaNo(), contractVO.getId());
            if (!EmptyUtils.isEmptyList(guaContractInfoList)) {
                throw new BusinessException(GuaranteeMessageEnum.CMS_EXGUARANTEE_REPEATED);
            }
            BeanUtils.copyProperties(contractVO, contractInfo, "id", "createDate", "createBy","createName");
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
            contractInfo.setUpdateBy(user.getId());
            contractInfo.setUpdateDate(DateUtils.getDate());
        }
        // 更新抵质押物信息
        // guaPledgeInfoService.saveOrUpdatePledgeInfo(contractInfo.getId(),contractVO.getpDids(),contractVO.getpZids());
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
        // 如果是集团内担保单位解析单位id保存单位名称
        this.setGuaPerson(contractInfo);
        this.saveOrUpdate(contractInfo);
        this.getSession().flush();
        // 查询原担保合同关联担保方式表信息
        List<GuaContractWayVO> listGuaContractWay = guaContractWayService.getGuaContractWay(contractInfo.getId());
        // 更新担保合同关联担保方式表
        contractVO.setId(contractInfo.getId());
        this.saveGuaContractWay(contractVO);
        // this.reletedGuaWay(contractInfo.getId(),guaTypes);
        // 更新担保方式关联抵质押物表
        this.saveGuaWayPledge(contractVO, listGuaContractWay);
        // this.reletedGuaContractPledge(contractInfo.getId(),listGuaContractWay,contractVO);
        // 保存抵质押物占用信息到占用表
        List<GuaPledgeOccupyDTO> occupyDTOS = this.setPledgeOccupyInfo(contractVO, ConfigConstant.GUACONTRACT);
        guaPledgeOccupyServiceImpl.freezePledgeOccupy(occupyDTOS);
        // 挂接审批
        if (GuaranteeConstant.STATUS_WAIT_CHECK.equals(contractInfo.getStatus())) {
             getStartHandler().handler(GuaWorkFlowConstant.GUA_CONTRACT_REGIST, contractInfo.getId(), contractInfo,
                    contractInfo.getCorpId(), user.getId());
        }
        // 日志记录
        guaLogService.saveGuaLog(contractInfo.getId(), contractInfo.getGuaMoney(), msgType, message, user);
    }
    /**
     * 担保合同关联担保方式
     *
     * @param id       担保合同id
     * @param guaTypes 担保方式id
     */
    public void reletedGuaWay(String id, List<String> guaTypes) {
        List<GuaContractWayVO> list = guaContractWayService.getGuaContractWay(id);
        if (list != null && list.size() > 0) {
            int n = guaContractWayService.delGuaContractWay(id);
        }
        if (guaTypes != null && guaTypes.size() > 0) {
            for (String wayCodes : guaTypes) {
                GuaContractWay guaContractWay = new GuaContractWay();
                guaContractWay.setGuaId(id);
                guaContractWay.setWayCodes(wayCodes);
                guaContractWayService.saveOrUpdate(guaContractWay);
            }
        }
    }
    /**
     * 担保合同关联抵质押物信息
     *
     * @param id
     */
    public void reletedGuaContractPledge(String id, List<GuaContractWay> list, GuaContractInfoVO vo) {
        // 担保合同关联担保方方式，类型为抵押时对应的id
        String dYid = "";
        // 担保合同关联担保方方式，类型为质押时对应的id
        String zYid = "";
        // 担保合同关联担保方方式，类型为保证时对应的id
        String bZid = "";
        if (list != null && list.size() > 0) {
            for (GuaContractWay obj : list) {
                // 先删掉原担保合同关联抵质押物信息
                int n = guaContractPledgeService.delGuaContractPledge(obj.getId());
            }
        }
        List<GuaContractWayVO> guaContractWayList = guaContractWayService.getGuaContractWay(id);
        for (GuaContractWayVO obj : guaContractWayList) {
            GuaWayVO guaWay = guaContractInfoDao.getGuaWayById(obj.getWayCodes());
            if (guaWay.getCode().equals(GuaranteeConstant.GUA_TYPE_DY)) {
                dYid = obj.getId();
            } else if (guaWay.getCode().equals(GuaranteeConstant.GUA_TYPE_ZY)) {
                zYid = obj.getId();
            } else if (guaWay.getCode().equals(GuaranteeConstant.GUA_TYPE_BZ)) {
                bZid = obj.getId();
            }
        }
        // 抵押信息
        if (StringUtils.isNotEmpty(vo.getpDids())) {
            this.upDateGuaContractPledge(dYid, vo.getpDids());
        }
        // 质押信息
        if (StringUtils.isNotEmpty(vo.getpZids())) {
            this.upDateGuaContractPledge(zYid, vo.getpZids());
        }
        if (vo.getMoney2() != null) {// 如果保证的金额不为0,存入gua_contract_pledge表中，pledge_id为空
            GuaContractPledge guaContractPledge = new GuaContractPledge();
            guaContractPledge.setGuaContractWayId(bZid);
            guaContractPledge.setGuaMoney(vo.getMoney2());
            // 如果为担保方式为保证,不用设置pledge_id字段中的值
            guaContractPledgeService.saveOrUpdate(guaContractPledge);
        }
    }
    /**
     * 修改担保合同关联抵质押物中的信息
     *
     * @param guaContractWayId
     * @param infoStr
     */
    public void upDateGuaContractPledge(String guaContractWayId, String infoStr) {
        if (StringUtils.isNotEmpty(infoStr)) {
            // 去掉最后一个分号
            infoStr = infoStr.substring(0, infoStr.length() - 1);
            String[] idAndMoney = infoStr.split(";");
            for (String ids : idAndMoney) {
                String[] tempStr = ids.split(",");
                GuaContractPledge guaContractPledge = new GuaContractPledge();
                guaContractPledge.setGuaContractWayId(guaContractWayId);
                if (StringUtils.isNotEmpty(tempStr[0])) {
                    guaContractPledge.setPledgeId(tempStr[0]);
                }
                guaContractPledge.setGuaMoney(new BigDecimal(tempStr[1]));
                guaContractPledgeService.saveOrUpdate(guaContractPledge);
            }
        }
    }
    /**
     * 查询担保方式
     *
     * @return List<GuaWay>
     */
    @Transactional(readOnly = true)
    public List<GuaWayVO> getListGuaWay() {
        return guaContractInfoDao.getListGuaWay();
    }
    /**
     * 查询抵质押物信息
     *
     * @param pledgeIds
     * @return
     */
    @Transactional(readOnly = true)
    public List<GuaPledgeInfoVO> getListGuaPledgeInfo(List<String> pledgeIds) {
        return guaContractInfoDao.getListGuaPledgeInfo(pledgeIds);
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
     * 审批启动前调用的方法
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    /**
     * 审批后的回调函数
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String bussinessKey = workFlowResult.getBusinessKey();
        GuaContractInfo info = this.get(bussinessKey,true);
        boolean result = workFlowResult.isPass();// 是否通过
        if (result) {
            info.setStatus(GuaranteeConstant.STATUS_CHECK);
            // 如果担保对接了合同管理，生成相应的关联信息
            this.toRelateCon(info);
        } else {
            info.setStatus(GuaranteeConstant.STATUS_BACK);
        }
        this.updateEntity(info);
        if (result) {
            // 审批通过后,将抵质押物表中在途的金额改为占用的金额
            updateGuaPledgeInfoMoney(info);
            createContractUseTrack(info, workFlowResult.getUser(), GuaranteeConstant.CON_TYPE_0);// 合同类型为被担保
            // 将抵质押物占用状态修改为占用
            List<GuaPledgeOccupyDTO> occupyDTOS = guaPledgeOccupyServiceImpl.getPledgeOccupy(info.getId(),
                    info.getGuaNo());
            guaPledgeOccupyServiceImpl.addPledgeOccupy(occupyDTOS);
        }
    }
    // 审批通过后,将抵质押物表中在途的金额改为占用的金额
    public void updateGuaPledgeInfoMoney(GuaContractInfo contractInfo) {
        // 得到担保合同关联抵质押物中的信息
        List<GuaContractPledge> list = guaContractPledgeService.getGuaContractPledge(contractInfo.getId());
        for (GuaContractPledge obj : list) {
            GuaPledgeInfoVO vo = guaPledgeInfoService.getGuaPledgeInfoVOById(obj.getPledgeId());
            GuaPledgeInfo info = new GuaPledgeInfo();
            if (vo != null) {
                BeanUtils.copyNotNullProperties(vo, info);
            }
            if (StringUtils.isNotEmpty(info.getId())) {
                // 以下是修改抵质押信息表中的在途和占用金额
                // 1.抵质押物在途额度=在途的额度-此次担保合同占用的额度
                if (info.getFreezeMoney() != null && (info.getFreezeMoney().compareTo(BigDecimal.ZERO) == 1)) {
                    if (GuaranteeConstant.GUA_TYPE_DY.equals(info.getPledgeAttr())) {
                        List<GuaPledgeOccupyDTO> guaPledgeOccupyDTOS = guaPledgeOccupyServiceImpl
                                .getPledgeOccupyListFrozen(info.getId());
                        if (guaPledgeOccupyDTOS.size() <= 1) {
                            info.setFreezeMoney(BigDecimal.ZERO);
                        }
                    } else {
                        info.setFreezeMoney(info.getFreezeMoney().subtract(obj.getGuaMoney()));
                    }
                }
                // 2.抵质押物占用额度=占有额度+此次担保合同占用的额度
                if (GuaranteeConstant.GUA_TYPE_DY.equals(info.getPledgeAttr())) {
                    info.setOccupyMoney(info.getPledgeMoney());
                } else {
                    info.setOccupyMoney(info.getOccupyMoney().add(obj.getGuaMoney()));
                }
                if (contractInfo.getUpdateBy() != null) {
                    info.setUpdateBy(contractInfo.getUpdateBy());
                } else {
                    info.setUpdateBy(contractInfo.getCreateBy());
                }
                guaPledgeInfoService.saveOrUpdate(info);
            }
        }
    }
    /**
     * 设置担保方式
     *
     * @param guaId
     * @return
     */
    public String setGuaWayS(String guaId) {
        StringBuilder guaWays = new StringBuilder();
        // 根据担保合同id,得到担保方式id,用于初始化复选框
        List<GuaContractWayVO> guaContractWayList = guaContractWayService.getGuaContractWay(guaId);
        if (!EmptyUtils.isEmptyList(guaContractWayList)) {
            for (GuaContractWayVO obj : guaContractWayList) {
                if (EmptyUtils.isBlank(guaWays.toString())) {
                    guaWays.append(obj.getWayCodes());
                } else {
                    guaWays.append(CommonConstant.COMMA_SPLIT).append(obj.getWayCodes());
                }
            }
        }
        return guaWays.toString();
    }
    /**
     * 设置列表上显示的担保方式名称
     *
     * @param list 担保合同信息集合
     * @return 担保合同信息集合
     */
    public List<GuaContractInfoVO> setGuaWayName(List<GuaContractInfoVO> list) {
        if (EmptyUtils.isNotEmpty(list)) {
            List<String> ids = list.stream().map(GuaContractInfoVO::getId).collect(Collectors.toList());
            List<GuaContractWayVO> guaContractWayVoList = guaContractWayService.getListByInfoIds(ids);
            if (guaContractWayVoList.size() < 1) {
                return list;
            }
            Map<String, List<GuaContractWayVO>> guaContractWayMap = guaContractWayVoList.stream().collect(Collectors.groupingBy(GuaContractWayVO::getGuaId));
            List<String> wayCodes = guaContractWayVoList.stream().map(GuaContractWayVO::getWayCodes).collect(Collectors.toList());
            List<GuaWayVO> wayList = guaContractInfoDao.getGuaWayByIds(wayCodes);
            Map<String, GuaWayVO> wayVoMap = wayList.stream().collect(Collectors.toMap(GuaWayVO::getId, Function.identity()));
            list.forEach(item -> {
                StringBuilder guaWayName = new StringBuilder();
                StringBuilder guaWayCode = new StringBuilder();
                // 根据担保合同id,得到担保方式id,用于初始化复选框
                List<GuaContractWayVO> guaContractWayList = guaContractWayMap.get(item.getId());
                if (EmptyUtils.isNotEmpty(guaContractWayList)) {
                    guaContractWayList.forEach(bean -> {
                        GuaWayVO guaWay = wayVoMap.get(bean.getWayCodes());
                        if (EmptyUtils.isBlank(guaWayName.toString())) {
                            guaWayName.append(guaWay.getName());
                            guaWayCode.append(guaWay.getCode());
                        } else {
                            guaWayName.append("、").append(guaWay.getName());
                            guaWayCode.append(",").append(guaWay.getCode());
                        }
                    });
                }
                item.setGuaTypeName(guaWayName.toString());
                item.setGuaWayCode(guaWayCode.toString());
            });
        }
        return list;
    }
    /**
     * 删除担保合同信息,并且修改与之相关的信息
     *
     * @param id 担保合同id
     */
    public void delGuaContractInfo(String id, ICurrentUser user) {
        // 1.修改担保合同信息为删除状态
        GuaContractInfo info = this.get(id);
        info.setStatus(GuaranteeConstant.STATUS_DELETE);
        info.setUpdateBy(user.getId());
        info.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(info);// 保存修改后的信息
        // 2.还原抵质押物信息
        List<GuaContractPledge> list = guaContractPledgeService.getGuaContractPledge(id);
        if (list != null && list.size() > 0) {
            guaPledgeInfoService.convertPledgeMoney(list);
        }
        // 3.删除担保合同关联抵质押物信息表
        for (GuaContractPledge obj : list) {
            guaContractPledgeService.delGuaContractPledge(obj.getGuaContractWayId());
        }
        // 4.删除担保合同关联担保方式信息
        guaContractWayService.delGuaContractWay(id);
        // 删除抵质押物占用表信息
        guaPledgeOccupyServiceImpl.deletePledgeOccupy(info.getId(), info.getGuaNo());
    }
    /**
     * 查询担保合同登记审批列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getApproveDataTables(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(GuaWorkFlowConstant.GUA_CONTRACT_REGIST, input);
        String businessSql = null;
        if(!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())){
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        }
        DataTablesOutput dp = guaContractInfoDao.getApproveDataTables(input, businessSql);
        dp.setData(this.setGuaWayName(dp.getData()));
        dp.setData(this.setDebtorPerson(dp.getData()));
        return workFlowBusiness.merge(dp);
    }
    /**
     * 查询审批通过的担保合同
     *
     * @param dataTablesInput 查询参数
     * @param resource        资源权限
     * @param user            用户信息
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<GuaContractInfoVO> getApprovedDataTables(DataTablesInput input, String resource, ICurrentUser user) {
        List<String> corpAuth = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(resource)) {
            corpAuth = corpService.getUserCorpIds(resource, user.getId());
        }
        DataTablesOutput<GuaContractInfoVO> data = guaContractInfoDao.getApprovedDataTables(input, corpAuth);
        data.setData(this.setGuaWayName(data.getData()));
        data.setData(this.setDebtorPerson(data.getData()));
        return data;
    }
    /**
     * 通过担保合同id和担保方式查询抵质押物信息
     *
     * @param guaId
     * @param pledgeAttr
     * @return
     */
    public List<GuaPledgeInfoVO> toOccupyPage(String guaId, String pledgeAttr) {
        List<GuaPledgeInfoVO> infoList = new ArrayList<GuaPledgeInfoVO>();
        // 得到担保合同关联抵质押物中的信息
        List<GuaContractPledge> list = guaContractPledgeService.getGuaContractPledge(guaId);
        for (GuaContractPledge obj : list) {
            GuaPledgeInfoVO info = guaPledgeInfoService.getPledgeInfoByIdAndPledgeAttr(obj.getPledgeId(), pledgeAttr);
            if (info != null) {
                info.setGuaMoney(obj.getGuaMoney());
                infoList.add(info);
            }
        }
        if (infoList != null && infoList.size() > 0) {
            return infoList;
        } else {
            return null;
        }
    }
    /**
     * 接收前端传来的拼接信息，查询抵质押信息
     *
     * @param str
     * @return
     */
    public List<GuaPledgeInfoVO> convertPledgeInfoList(String str) {
        if (StringUtils.isNotEmpty(str)) {
            List<GuaPledgeInfoVO> infoList = new ArrayList<GuaPledgeInfoVO>();
            // 去掉最后一个分号
            String strIds = str.substring(0, str.length() - 1);
            String[] idAndMoney = strIds.split(";");
            for (String ids : idAndMoney) {
                String[] tempStr = ids.split(",");
                GuaPledgeInfoVO info = guaPledgeInfoService.getGuaPledgeInfoVOById(tempStr[0]);
                String m = tempStr[1];
                info.setGuaMoney(new BigDecimal(tempStr[1]));
                infoList.add(info);
            }
            if (infoList != null && infoList.size() > 0) {
                return infoList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    public List<GuaContractInfoVO> getContractList(ParamsVO vo) {
        List<String> contractIds = new ArrayList<String>();
        List<GuaContractInfoVO> list = new ArrayList<GuaContractInfoVO>();
        if (vo.getContractIds() != null && !(vo.getContractIds().equals(""))) {
            String[] ids = vo.getContractIds().split(",");
            for (String id : ids) {
                contractIds.add(id);
            }
        }
        if (contractIds != null && contractIds.size() > 0) {
            for (String id : contractIds) {
                GuaContractInfoVO guaContractInfoVO = new GuaContractInfoVO();
                guaContractInfoVO = this.getGuaContractInfoById(id);
                list.add(guaContractInfoVO);
            }
        }
        list = this.setGuaWayName(list);
        return list;
    }
    @Override
    public BaseDao<GuaContractInfo, String> getEntityDao() {
        return guaContractInfoDao;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(GuaContractExcelVO guaContractExcelVO, Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<GuaContractExcelVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (GuaContractExcelVO vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 单位
                if (EmptyUtils.isEmpty(vo.getCorpName())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_CORP_ISNULL);
                } else {
                    CorpDTO corpDTO = corpService.getCorpIdByName(vo.getCorpName());
                    if (EmptyUtils.isEmpty(corpDTO)) {
                        errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_CORP_ERROR);
                    } else {
                        vo.setCorpId(corpDTO.getId());
                    }
                }
                // 担保编号
                if (EmptyUtils.isEmpty(vo.getGuaNo())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_GUANO_ISNULL);
                }
                // 币种
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
                // 担保日期
                if (EmptyUtils.isEmpty(vo.getStartDate())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_STARTDATE_ISNULL);
                }
                if (EmptyUtils.isEmpty(vo.getEndDate())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_ENDDATE_ISNULL);
                }
                if (!EmptyUtils.isEmpty(vo.getStartDate()) && !EmptyUtils.isEmpty(vo.getEndDate())) {
                    if (vo.getStartDate().compareTo(vo.getEndDate()) > 0) {
                        errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_DATE_ERROR);
                    }
                }
                // 债务人
                if (EmptyUtils.isEmpty(vo.getDebtorPerson())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_DEBTORPERSON_ISNULL);
                }
                // 债权人
                if (EmptyUtils.isEmpty(vo.getCreditorPerson())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_CREDITORPERSON_ISNULL);
                }
                // 是否是集团内担保，校验通过后将"是/否"转换为"1/0"
                if (EmptyUtils.isEmpty(vo.getInnerGua())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_INNERGUA_ISNULL);
                } else if ("是".equals(vo.getInnerGua())) {
                    vo.setInnerGua(GuaranteeConstant.INNER_GUA_Y);
                } else if ("否".equals(vo.getInnerGua())) {
                    vo.setInnerGua(GuaranteeConstant.INNER_GUA_N);
                } else {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_INNERGUA_ERROR);
                }
                // 担保单位
                if (EmptyUtils.isEmpty(vo.getGuaPerson())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_GUAPERSON_ISNULL);
                } else if (!EmptyUtils.isEmpty(vo.getInnerGua())) {
                    if (GuaranteeConstant.INNER_GUA_Y.equals(vo.getInnerGua())) {
                        CorpDTO corpDTO = corpService.getCorpIdByName(vo.getGuaPerson());
                        if (EmptyUtils.isEmpty(corpDTO)) {
                            errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_GUAPERSON_ERROR);
                        }
                    }
                }
                // 合同编号
                if (EmptyUtils.isEmpty(vo.getContractCode())) {
                    errorMsgList.add(GuaranteeConstant.CMS_GUACONTRACT_CONTRACTCODE_ISNULL);
                } else {
                    // 担保对接合同管理
                    String relateCon = this.relateCon();
                    if (GuaranteeConstant.RELATE_CON_YES.equals(relateCon)) {
                        String conType = this.conType();
                        if (GuaranteeConstant.CON_TYPE_SELECT.equals(conType)) {
                            vo.setContractCode("");// 业务选择合同情况下手动选择相应的合同
                        }
                    }
                }
                vo.addErrorMsg(errorMsgList);
            }
        }
    }
    public void saveImport(GuaContractExcelVO guaContractExcelVO, ICurrentUser user) {
        // 是否是集团内担保，需在拷贝属性前转换，否则入库的是"是/否"。
        // 页面回传的既可能是excel原文的"是/否"，也可能是校验接口转换后的"1/0"，两种都兼容
        String innerGua = guaContractExcelVO.getInnerGua();
        if (EmptyUtils.isEmpty(innerGua)) {
            throw new BusinessException(GuaranteeConstant.CMS_GUACONTRACT_INNERGUA_ISNULL);
        } else if ("是".equals(innerGua)) {
            guaContractExcelVO.setInnerGua(GuaranteeConstant.INNER_GUA_Y);
        } else if ("否".equals(innerGua)) {
            guaContractExcelVO.setInnerGua(GuaranteeConstant.INNER_GUA_N);
        } else if (!GuaranteeConstant.INNER_GUA_Y.equals(innerGua)
                && !GuaranteeConstant.INNER_GUA_N.equals(innerGua)) {
            throw new BusinessException(GuaranteeConstant.CMS_GUACONTRACT_INNERGUA_ERROR);
        }
        GuaContractInfo contractInfo = new GuaContractInfo();
        BeanUtils.copyNotNullProperties(guaContractExcelVO, contractInfo);
        contractInfo.setStatus(GuaranteeConstant.STATUS_TEMP);
        contractInfo.setBusinessType("0");
        contractInfo.setIsChange("0");
        contractInfo.setCreateName(user.getUsername());
        contractInfo.setCreateBy(user.getId());
        contractInfo.setCreateDate(DateUtils.getDate());
        this.saveOrUpdate(contractInfo);
    }
    /**
     * 保存担保合同关联担保方式
     *
     * @author luxy
     */
    public void saveGuaContractWay(GuaContractInfoVO contractVO) {
        List<GuaContractWayVO> list = guaContractWayService.getGuaContractWay(contractVO.getId());
        if (list != null && list.size() > 0) {
            guaContractWayService.delGuaContractWay(contractVO.getId());
        }
        List<GuaContractWayVO> wayList = contractVO.getGuaDtls();
        if (wayList != null && wayList.size() > 0) {
            for (GuaContractWayVO guaWay : wayList) {
                GuaContractWay guaContractWay = new GuaContractWay();
                guaContractWay.setGuaId(contractVO.getId());
                guaContractWay.setWayCodes(guaWay.getWayCodes());
                guaContractWay.setMoney(guaWay.getMoney());
                guaContractWayService.saveOrUpdate(guaContractWay);
                guaWay.setId(guaContractWay.getId());
            }
        }
    }
    /**
     * 保存担保方式关联抵质押物信息
     *
     * @author luxy
     */
    public void saveGuaWayPledge(GuaContractInfoVO vo, List<GuaContractWayVO> listPre) {
        List<GuaContractWayVO> wayList = vo.getGuaDtls();// 担保合同新关联的担保方式
        if (listPre != null && listPre.size() > 0) {
            for (GuaContractWayVO obj : listPre) {
                // 先删掉原担保方式关联抵质押物信息
                guaContractPledgeService.delGuaContractPledge(obj.getId());
            }
        }
        for (GuaContractWayVO guaWay : wayList) {
            upDateGuaWayPledge(guaWay);
        }
    }
    /**
     * 修改担保方式关联抵质押物中的信息
     *
     * @author luxy
     */
    public void upDateGuaWayPledge(GuaContractWayVO guaWay) {
        if (StringUtils.isNotEmpty(guaWay.getPledgeIds())) {
            // 去掉最后一个分号
            String infoStr = guaWay.getPledgeIds();
            infoStr = infoStr.substring(0, infoStr.length() - 1);
            String[] idAndMoney = infoStr.split(";");
            for (String ids : idAndMoney) {
                GuaContractPledge guaContractPledge = new GuaContractPledge();
                String[] tempStr = ids.split(",");
                guaContractPledge.setGuaContractWayId(guaWay.getId());
                if (StringUtils.isNotEmpty(tempStr[0])) {
                    guaContractPledge.setPledgeId(tempStr[0]);
                }
                guaContractPledge.setGuaMoney(new BigDecimal(tempStr[1]));
                guaContractPledgeService.saveOrUpdate(guaContractPledge);
            }
        } else {// 如果为担保方式非抵质押性质,不用设置pledge_id字段中的值
            GuaContractPledge guaContractPledge = new GuaContractPledge();
            guaContractPledge.setGuaContractWayId(guaWay.getId());
            guaContractPledge.setGuaMoney(guaWay.getMoney());
            guaContractPledgeService.saveOrUpdate(guaContractPledge);
        }
    }
    /**
     * 根据担保合同关联的担保方式查询抵质押信息
     *
     * @param list 担保合同关联的担保方式
     * @return
     */
    public void setPledgeInfo(List<GuaContractWayVO> list) {
        for (GuaContractWayVO guaContractWayVO : list) {// 设置担保方式关联的抵质押物信息
            List<GuaContractPledge> pledgeList = guaContractPledgeService
                    .queryGuaContractPledge(guaContractWayVO.getId());
            String pledges = "";
            String pDids = "";
            for (GuaContractPledge guaContractPledge : pledgeList) {
                if (null != guaContractPledge.getPledgeId()
                        || StringUtils.isNotEmpty(guaContractPledge.getPledgeId())) {
                    pledges += guaContractPledge.getPledgeId() + "," + guaContractPledge.getGuaMoney() + ";";
                    pDids += guaContractPledge.getPledgeId() + ",";
                }
            }
            guaContractWayVO.setPledgeIds(pledges);
            guaContractWayVO.setpDids(pDids);
        }
    }
    /**
     * @return java.lang.String
     * @Description 查询系统参数，担保合同是否对接合同管理
     * @Param []
     * @Date 2019/11/18 19:29
     * @Author fusb
     */
    public String relateCon() {
        return configService.getConfig(ConfigConstant.GUA, GuaranteeConstant.GUA_RELATE_CON).getValue();
    }
    /**
     * @return java.lang.String
     * @Description 查询系统参数，担保合同对接合同管理模式
     * @Param []
     * @Date 2019/11/18 19:30
     * @Author fusb
     */
    public String conType() {
        return configService.getConfig(ConfigConstant.GUA, GuaranteeConstant.GUA_CON_TYPE).getValue();
    }
    /**
     * @return void
     * @Description 担保对接合同管理
     * @Param [info, user]
     * @Date 2019/11/18 20:07
     * @Author fusb
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
            List<GuaContractInfoVO> list = new ArrayList<GuaContractInfoVO>(1);
            list.add(this.getGuaContractInfoById(info.getId()));
            GuaContractInfoVO vo = this.setGuaWayName(list).get(0);
            ConInfoDTO conDTO = new ConInfoDTO();
            conDTO.setCorpId(info.getCorpId());
            conDTO.setBusId(info.getId());
            conDTO.setConNo(info.getContractCode());
            conDTO.setConName(vo.getCorpName() + GuaranteeConstant.CON_NAME);
            conDTO.setTypeId(info.getConTypeId());
            conDTO.setBeginDate(info.getStartDate());
            conDTO.setEndDate(info.getEndDate());
            conDTO.setOutBusData(vo);
            conCommonService.registerConOpGua(conDTO, user);
            info.setConId(conDTO.getId());
        }
    }
    /**
     * @return void
     * @Description 集团内担保单位，担保单位id转换成单位名称保存
     * @Param [entity]
     * @Date 2019/12/20 11:33
     * @Author fusb
     */
    public void setGuaPerson(GuaContractInfo entity) {
        if (GuaranteeConstant.INNER_GUA_Y.equals(entity.getInnerGua())) {
            List<CorpDTO> corpList = corpService.listCorpDTOByCorpIds2(Arrays.asList(entity.getGuaPerson().split(",")));
            String guaPersonName = "";
            if (!EmptyUtils.isEmpty(corpList)) {
                for (CorpDTO corpDTO : corpList) {
                    guaPersonName += corpDTO.getName() + "、";
                }
            }
            entity.setGuaPerson(guaPersonName.substring(0, guaPersonName.length() - 1));
        }
    }
    /**
     * @return void
     * @Description 将担保单位名称转换为单位id
     * @Param [infoVO]
     * @Date 2019/12/20 12:10
     * @Author fusb
     */
    public void getGuaPerson(GuaContractInfoVO infoVO) {
        if (GuaranteeConstant.INNER_GUA_Y.equals(infoVO.getInnerGua())) {
            String[] corpNames = infoVO.getGuaPerson().split("、");
            String guaPerson = "";
            for (String corpName : corpNames) {
                CorpDTO corpDTO = corpService.getCorpIdByName(corpName);
                if (!EmptyUtils.isEmpty(corpDTO)) {
                    guaPerson += corpDTO.getId() + ",";
                }
            }
            if (StringUtils.isNotEmpty(guaPerson)) {
                infoVO.setGuaPerson(guaPerson.substring(0, guaPerson.length() - 1));
            }
        }
    }
    /**
     * @return java.util.List<com.bytter.biz.cms.guarantee.vo.GuaContractInfoVO>
     * @Description 设置债务人
     * @Param [list]
     * @Date 2019/12/20 13:40
     * @Author fusb
     */
    public List<GuaContractInfoVO> setDebtorPerson(List<GuaContractInfoVO> list) {
        if (EmptyUtils.isNotEmpty(list)) {
            List<String> corpIds = list.stream().map(GuaContractInfoVO::getCorpId).collect(Collectors.toList());
            Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpIds);
            list.forEach(item -> {
                if (corpMap.containsKey(item.getDebtorPerson())) {
                    item.setDebtorPerson(corpMap.get(item.getDebtorPerson()).getName());
                }
            });
        }
        return list;
    }
    /**
     * 生成担保方式使用轨迹
     *
     * @param info
     * @param user
     * @param conType
     */
    public void createContractUseTrack(GuaContractInfo info, ICurrentUser user, String conType) {
        String guaId = info.getId();
        List<GuaContractWayVO> guaDtls = guaContractWayService.getGuaContractWayVO(guaId);// 担保合同关联的担保方式
        List<GuaConrrantChangeTrack> trackList = new ArrayList<GuaConrrantChangeTrack>();
        // 抵质押物信息
        List<GuaContractPledge> pledgeList = new ArrayList<>();
        for (GuaContractWayVO guaWay : guaDtls) {
            pledgeList = guaContractPledgeService.queryGuaContractPledge(guaWay.getId());
            if (null != pledgeList && pledgeList.size() > 0) {
                for (GuaContractPledge pledge : pledgeList) {
                    GuaConrrantChangeTrack guaTrack = new GuaConrrantChangeTrack();
                    guaTrack.setGuaId(info.getId());
                    guaTrack.setGuaNo(info.getGuaNo());
                    guaTrack.setContractCode(info.getContractCode());
                    guaTrack.setPledgeId(pledge.getPledgeId());
                    guaTrack.setMoney(pledge.getGuaMoney());
                    guaTrack.setGuaWayId(guaWay.getWayCodes());// 担保方式id
                    guaTrack.setTrackType(GuaranteeConstant.GUA_TRACK_ZY);// 占用
                    guaTrack.setCreateBy(user.getId());
                    guaTrack.setCreateTime(DateUtils.getDate());
                    trackList.add(guaTrack);
                }
            }
        }
        if (trackList.size() > 0) {
            changeService.batchSaveTrackList(trackList, conType);
        }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = guaContractInfoDao.queryBusBillByCorpId(corpId, GuaranteeConstant.CON_TYPE_0, null);
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.GUA_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.GUA_VALID_DEL_CONTRACT_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = guaContractInfoDao.queryBusBillByCorpId(corpId, GuaranteeConstant.CON_TYPE_0, "0,-1,11");
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.GUA_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.GUA_VALID_DISABLE_CONTRACT_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
    /**
     * 将抵质押物信息存入抵质押物占用表
     *
     * @param contractVO
     * @return
     */
    public List<GuaPledgeOccupyDTO> setPledgeOccupyInfo(GuaContractInfoVO contractVO, String sysCode) {
        List<GuaPledgeOccupyDTO> pledgeOccupyDTOS = new ArrayList<>();
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        for (GuaContractWayVO guaContractWayVO : guaDtls) {
            if (StringUtils.isNotEmpty(guaContractWayVO.getPledgeIds())) {
                String pledgeIds = guaContractWayVO.getPledgeIds();
                if (StringUtils.isNotEmpty(pledgeIds)) {
                    pledgeIds = pledgeIds.substring(0, pledgeIds.length() - 1);// 截去最后一个，
                    String[] ids = pledgeIds.split(";");
                    for (String str : ids) {// 遍历id,
                        String[] tempStr = str.split(",");
                        GuaPledgeOccupyDTO pledgeOccupyDTO = new GuaPledgeOccupyDTO();
                        pledgeOccupyDTO.setSysCode(sysCode);
                        pledgeOccupyDTO.setBusinessId(contractVO.getId());
                        pledgeOccupyDTO.setItemNo(contractVO.getGuaNo());
                        pledgeOccupyDTO.setStartDate(contractVO.getStartDate());
                        pledgeOccupyDTO.setEndDate(contractVO.getEndDate());
                        pledgeOccupyDTO.setPledgeId(tempStr[0]);
                        pledgeOccupyDTO.setOccupyMoney(new BigDecimal(tempStr[1]));
                        pledgeOccupyDTO.setCurId(contractVO.getCurId());
                        pledgeOccupyDTOS.add(pledgeOccupyDTO);
                    }
                }
            }
        }
        return pledgeOccupyDTOS;
    }
    /**
     * 检查是否存在挂接的担保合同
     * @param corpId
     * @return
     */
    public boolean checkContractList(String corpId) {
        return guaContractInfoDao.checkContractList(corpId);
    }
}
