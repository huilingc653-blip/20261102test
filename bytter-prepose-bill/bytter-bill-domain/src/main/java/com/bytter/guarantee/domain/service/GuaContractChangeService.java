package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.GuaWorkFlowConstant;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.dao.GuaContractChangeDao;
import com.bytter.guarantee.domain.dao.GuaPledgeInfoDao;
import com.bytter.guarantee.domain.entity.*;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaContractQueryDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.DictDTO;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 被担保合同service
 *
 * @author yangl
 */
@Service("guaContractChangeService")
@Transactional
@WorkFlow(value = {GuaWorkFlowConstant.GUA_CONTRACT_CHANGE_REGIST})
public class GuaContractChangeService extends BaseService<GuaContractInfo, String> implements ITaskService {
    @Autowired
    private GuaContractChangeDao guaContractChangeDao;
    @Autowired
    private GuaPledgeInfoDao guaPledgeInfoDao;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private GuaPledgeInfoService guaPledgeInfoService;
    @Autowired
    private GuaContractPledgeService guaContractPledgeService;
    @Autowired
    private GuaContractWayService guaContractWayService;
    @Autowired
    private GuaLogService guaLogService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService guaPledgeOccupyService;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<GuaContractInfo, String> getEntityDao() {
        return guaContractChangeDao;
    }
    /**
     * 担保合同变更列表
     *
     * @param input 查询参数
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<GuaContractInfoVO> queryContractChangeList(DataTablesInput input, String resource, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
        DataTablesOutput<GuaContractInfoVO> output = guaContractChangeDao.queryContractChangeList(input, corpAuth);
        output.setData(guaContractInfoService.setGuaWayName(output.getData()));
        output.setData(guaContractInfoService.setDebtorPerson(output.getData()));
        return output;
    }
    /**
     * 查询担保详情
     *
     * @param id
     * @return
     */
    public GuaContractInfoVO queryContractInfoById(String id) {
        return guaContractInfoService.getGuaContractInfoById(id);
    }
    /**
     * 获取担保方式
     *
     * @param id
     * @return
     */
    public String setGuaWayS(String id) {
        return guaContractInfoService.setGuaWayS(id);
    }
    /**
     * 保存担保变更信息
     *
     * @param contractVO
     * @param user
     */
    public void tempOrSubmit(GuaContractInfoVO contractVO, ICurrentUser user) {
        checkDate(contractVO);// 校验担保日期
        BigDecimal guaMoney = BigDecimal.ZERO;
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        for (GuaContractWayVO guaContractWayVO : guaDtls) {
            guaMoney = guaMoney.add(guaContractWayVO.getMoney());
        }
        GuaContractInfoVO vo = this.queryContractInfoById(contractVO.getId());
        GuaContractInfo contractInfo = new GuaContractInfo();
        BeanUtils.copyNotNullProperties(vo, contractInfo);
        BeanUtils.copyProperties(contractVO, contractInfo, "id","createName", "createDate", "createBy",
                "conId", "conTypeId","corpName","curName", "businessType");
        //当前端未传值取原来值
        if (StringUtils.isEmpty(contractVO.getInnerGua())) {
            contractInfo.setInnerGua(vo.getInnerGua());
        }
        contractInfo.setGuaMoney(guaMoney);
        contractInfo.setStatus(GuaranteeConstant.STATUS_CHECK);
        contractInfo.setUpdateBy(user.getId());
        contractInfo.setUpdateDate(DateUtils.getDate());
        contractInfo.setIsChange(GuaranteeConstant.GUA_YES_CHANGE);
        // 生成担保合同占用、释放轨迹
        createContractChangeTrack(contractVO, user, GuaranteeConstant.CON_TYPE_0);
        // 更新抵质押物信息
        if (!StringUtils.isEmpty(contractInfo.getId())) {// 修改担保合同，更新抵质押物信息
            // 先根据担保合同id查询原抵押物信息
            List<GuaContractPledge> pledgeList = guaContractPledgeService.getGuaContractPledge(contractInfo.getId());
            // 还原抵质押物占用金额
            if (pledgeList != null && pledgeList.size() > 0) {
                convertPledgeMoney(pledgeList);
            }
        }
        for (GuaContractWayVO guaContractWayVO : guaDtls) {
            if (StringUtils.isNotEmpty(guaContractWayVO.getPledgeIds())) {
                this.savePledgeInfo(guaContractWayVO.getPledgeIds());
            }
        }
        // 如果是集团内担保单位解析单位id保存单位名称
        guaContractInfoService.setGuaPerson(contractInfo);
        guaContractInfoService.saveOrUpdate(contractInfo);
        // 保存原担保合同关联担保方式表信息
        List<GuaContractWayVO> listGuaContractWay = guaContractWayService.getGuaContractWay(contractInfo.getId());
        // 更新担保合同关联担保方式表
        // guaContractInfoService.reletedGuaWay(contractInfo.getId(), guaTypes);
        guaContractInfoService.saveGuaContractWay(contractVO);
        // 更新担保合同关联抵质押物表
        // guaContractInfoService.reletedGuaContractPledge(contractInfo.getId(),listGuaContractWay, contractVO);
        guaContractInfoService.saveGuaWayPledge(contractVO, listGuaContractWay);
        // 日志记录
        guaLogService.saveGuaLog(contractInfo.getId(), contractInfo.getGuaMoney(),
                GuaranteeConstant.MESSAGE_TYPE_CHANGE, GuaranteeConstant.MSGA_CHANGE, user);
    }
    /**
     * 根据担保合同Id生成轨迹
     *
     * @param contractVO
     * @param guaTypes
     * @param contractInfo
     * @param user
     */
    public void createContractChangeTrack(GuaContractInfoVO contractVO, List<String> guaTypes,
                                          GuaContractInfo contractInfo, ICurrentUser user) {
        String guaId = contractVO.getId();
        List<GuaConrrantChangeTrack> trackList = new ArrayList<GuaConrrantChangeTrack>();
        if (StringUtils.isNotEmpty(guaId)) {
            // 原抵质押物信息
            List<GuaContractPledge> oldPledgeList = guaContractPledgeService.getGuaContractPledge(guaId);
            String guaDZids = contractVO.getpDids() + contractVO.getpZids();// 变更后抵押、质押信息
            List<GuaContractPledge> pledgeList = new ArrayList<GuaContractPledge>();
            if (StringUtils.isNotEmpty(guaDZids)) {// 抵质押物不为空
                String[] ids = null;
                String[] dtls = null;
                String newPledgeId = null;
                BigDecimal newPledgeMoney = BigDecimal.ZERO;
                ids = guaDZids.substring(0, guaDZids.length() - 1).split(";");
                for (String dtlId : ids) {// 遍历变更后的抵质押物id
                    dtls = dtlId.split(",");
                    newPledgeId = dtls[0];
                    newPledgeMoney = new BigDecimal(dtls[1]);
                    comparePledgeIdAddList(oldPledgeList, false, contractInfo, trackList, user, pledgeList, newPledgeId,
                            newPledgeMoney);
                }
            }
            comparePledgeIdAddList1(oldPledgeList, pledgeList, contractInfo, trackList, user, contractVO);
        }
        if (trackList.size() > 0) {
            guaContractChangeDao.batchSave(trackList);
        }
    }
    /**
     * 变更后无抵质押物信息，只有保证\删除了抵质押物信息
     *
     * @param oldPledgeList
     * @param contractInfo
     * @param trackList
     * @param user
     * @param contractVO
     */
    public void comparePledgeIdAddList1(List<GuaContractPledge> oldPledgeList, List<GuaContractPledge> pledgeList,
                                        GuaContractInfo contractInfo, List<GuaConrrantChangeTrack> trackList, ICurrentUser user,
                                        GuaContractInfoVO contractVO) {
        boolean isGua_BZ = false;
        for (GuaContractPledge pledge : oldPledgeList) {
            if (pledgeList.contains(pledge)) {// 已存在，不需要做处理
                continue;
            }
            // 担保合同关联担保方式的id得到担保合同关联担保方式信息
            GuaContractWay guaContractWay = guaContractWayService.getGuaContractWayById(pledge.getGuaContractWayId());
            if (StringUtils.isNotEmpty(guaContractWay)) {
                // 通过担保合同关联担保方式中的担保方式id,way_codes
                GuaWayVO guaWay = guaContractInfoService.getGuaWayById(guaContractWay.getWayCodes());
                String code = guaWay.getCode();
                if (code.equals(GuaranteeConstant.GUA_TYPE_BZ)) {// 原有保证
                    if (!StringUtils.isNotEmpty(contractVO.getMoney2())) {// 则判断，变更后是否有保证
                        releaseAndOccupyTrackList(trackList, contractInfo, user, null, pledge.getGuaMoney(),
                                GuaranteeConstant.GUA_TRACK_SF);
                    } else {
                        isGua_BZ = true;
                        compareMoneyAddList(contractVO.getMoney2(), pledge.getGuaMoney(), trackList, contractInfo, user,
                                null, null);
                    }
                } else if (GuaranteeConstant.GUA_TYPE_DY.equals(code) || GuaranteeConstant.GUA_TYPE_ZY.equals(code)) {// 释放抵质押物轨迹
                    releaseAndOccupyTrackList(trackList, contractInfo, user, pledge.getPledgeId(), pledge.getGuaMoney(),
                            GuaranteeConstant.GUA_TRACK_SF);
                }
            }
        }
        if (!isGua_BZ && StringUtils.isNotEmpty(contractVO.getMoney2())) {// 原来没有保证
            releaseAndOccupyTrackList(trackList, contractInfo, user, null, contractVO.getMoney2(),
                    GuaranteeConstant.GUA_TRACK_ZY);
        }
    }
    /**
     * 变更后抵质押物不为空
     *
     * @param oldPledgeList
     * @param flag
     * @param contractInfo
     * @param trackList
     * @param user
     * @param pledgeList
     * @param newPledgeId
     * @param newPledgeMoney
     */
    public void comparePledgeIdAddList(List<GuaContractPledge> oldPledgeList, boolean flag,
                                       GuaContractInfo contractInfo, List<GuaConrrantChangeTrack> trackList, ICurrentUser user,
                                       List<GuaContractPledge> pledgeList, String newPledgeId, BigDecimal newPledgeMoney) {
        String oldPledgeId = null;
        BigDecimal oldPledgeMoney = BigDecimal.ZERO;
        String code = null;
        for (GuaContractPledge pledge : oldPledgeList) {
            if (pledgeList.contains(pledge)) {// 已做处理
                continue;
            }
            oldPledgeMoney = pledge.getGuaMoney();// 原抵质押金额
            oldPledgeId = pledge.getPledgeId();
            // 担保合同关联担保方式的id得到担保合同关联担保方式信息
            GuaContractWay guaContractWay = guaContractWayService.getGuaContractWayById(pledge.getGuaContractWayId());
            if (guaContractWay != null) {
                // 通过担保合同关联担保方式中的担保方式id,way_codes
                GuaWayVO guaWay = guaContractInfoService.getGuaWayById(guaContractWay.getWayCodes());
                code = guaWay.getCode();
                // 抵质押物一致，判断金额是否一致，
                if (GuaranteeConstant.GUA_TYPE_DY.equals(code) || GuaranteeConstant.GUA_TYPE_ZY.equals(code)) {
                    if (newPledgeId.equals(pledge.getPledgeId())) {// 抵质押物一致，判断金额是否一致，
                        flag = true;
                        compareMoneyAddList(newPledgeMoney, oldPledgeMoney, trackList, contractInfo, user, oldPledgeId,
                                newPledgeId);
                        pledgeList.add(pledge);// 区分变更后没有原抵质押物信息
                        break;
                    }
                }
            }
        }
        if (!flag) {// 新增抵质押物
            releaseAndOccupyTrackList(trackList, contractInfo, user, newPledgeId, newPledgeMoney,
                    GuaranteeConstant.GUA_TRACK_ZY);// 占用轨迹
        }
    }
    /**
     * 占用、释放轨迹
     *
     * @param newMoney
     * @param oldMoney
     * @param trackList
     * @param contractInfo
     * @param user
     * @param oldPledgeId
     * @param pledgeId
     */
    public void compareMoneyAddList(BigDecimal newMoney, BigDecimal oldMoney, List<GuaConrrantChangeTrack> trackList,
                                    GuaContractInfo contractInfo, ICurrentUser user, String oldPledgeId, String pledgeId) {
        if (newMoney.subtract(oldMoney).compareTo(BigDecimal.ZERO) > 0
                || newMoney.subtract(oldMoney).compareTo(BigDecimal.ZERO) < 0) {// 变更后金额>原来的金额\变更后金额<原来的金额
            releaseAndOccupyTrackList(trackList, contractInfo, user, oldPledgeId, oldMoney,
                    GuaranteeConstant.GUA_TRACK_SF);
            releaseAndOccupyTrackList(trackList, contractInfo, user, pledgeId, newMoney,
                    GuaranteeConstant.GUA_TRACK_ZY);
        }
    }
    /**
     * 增加释放轨迹
     *
     * @param trackList
     * @param contractInfo
     * @param user
     * @param oldMoney
     */
    public void releaseAndOccupyTrackList(List<GuaConrrantChangeTrack> trackList, GuaContractInfo contractInfo,
                                          ICurrentUser user, String pledgeId, BigDecimal oldMoney, String trackType) {
        GuaConrrantChangeTrack guaTrack = new GuaConrrantChangeTrack();
        guaTrack.setGuaId(contractInfo.getId());
        guaTrack.setGuaNo(contractInfo.getGuaNo());
        guaTrack.setContractCode(contractInfo.getContractCode());
        guaTrack.setPledgeId(pledgeId);
        guaTrack.setMoney(oldMoney);
        if (GuaranteeConstant.GUA_TRACK_SF.equals(trackType)) {
            guaTrack.setTrackType(GuaranteeConstant.GUA_TRACK_SF);// 释放
        } else {
            guaTrack.setTrackType(GuaranteeConstant.GUA_TRACK_ZY);// 占用
        }
        guaTrack.setCreateBy(user.getId());
        guaTrack.setCreateTime(DateUtils.getDate());
        trackList.add(guaTrack);
    }
    /**
     * 担保合同变更日期校验
     *
     * @param contractVO
     */
    public void checkDate(GuaContractInfoVO contractVO) {
        // 根据抵质押物id查询是否关联了担保合同
        List<GuaContractPledge> list = guaContractPledgeService.getGuaContractPledge(contractVO.getId());
        for (GuaContractPledge obj : list) {
            // 查询抵质押物信息
            GuaPledgeInfo info = null;
            if (obj.getPledgeId() != null) {
                info = guaPledgeInfoService.get(obj.getPledgeId());
            }
            if (info != null) {
                // 判断:1当变更后的起始日期小于抵质押物的起始日期、或结束日期大于抵质押物的结束日期
                if ((contractVO.getStartDate().before(info.getStartDate()))
                        || (info.getEndDate().before(contractVO.getEndDate()))) {
                    // 抛出异常
                    throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUACONTRACTCHANGE_DATEERROR);
                }
            }
        }
    }
    /**
     * 担保总额
     *
     * @param contractVO
     * @return
     */
    public BigDecimal sumGuaMoney(GuaContractInfoVO contractVO) {
        BigDecimal guaMoney = BigDecimal.ZERO;
        if (contractVO.getMoney0() != null) {
            guaMoney = guaMoney.add(contractVO.getMoney0());
        }
        if (contractVO.getMoney1() != null) {
            guaMoney = guaMoney.add(contractVO.getMoney1());
        }
        if (contractVO.getMoney2() != null) {
            guaMoney = guaMoney.add(contractVO.getMoney2());
        }
        return guaMoney;
    }
    /**
     * 查看担保合同历史轨迹
     *
     * @param guaId
     * @return
     */
    public List<GuaConrrantChangeTrackVO> queryGuaContractHistory(String guaId) {
        List<GuaConrrantChangeTrackVO> trackVOS = guaContractChangeDao.queryGuaContractHistory(guaId);
        setDictValue(trackVOS);
        return trackVOS;
    }

    private void setDictValue(List<GuaConrrantChangeTrackVO> trackVOS) {
        Map<String, DictDTO> dictDTOMap = dictService.listMap("Gua.conType");
        for (GuaConrrantChangeTrackVO trackVO : trackVOS) {
            trackVO.setConTypeName(dictDTOMap.getOrDefault(trackVO.getConType(), DictDTO.EMPTY).getName());
        }
    }

    /**
     * 保存抵质押信息
     *
     * @param guaId
     */
    public void saveOrUpdatePledgeInfo(String guaId, String pDids, String pZdis, GuaContractInfo contractInfo) {
        String pzIds = pDids + pZdis;
        if (!StringUtils.isEmpty(guaId)) {// 修改担保合同，更新抵质押物信息
            // 先根据担保合同id查询原抵押物信息
            List<GuaContractPledge> pledgeList = guaContractPledgeService.getGuaContractPledge(guaId);
            // 还原抵质押物占用金额
            convertPledgeMoney(pledgeList);
        }
        // 根据id更新修改后的在途金额
        if (StringUtils.isNotEmpty(pzIds)) {
            pzIds = pzIds.substring(0, pzIds.length() - 1);// 截去最后一个，
            String[] ids = pzIds.split(";");
            for (String str : ids) {// 遍历id,
                String[] tempStr = str.split(",");
                GuaPledgeInfo info = guaPledgeInfoService.get(tempStr[0]);
                if (info != null) {
                    if (info.getOccupyMoney() != null) {
                        // 2.抵质押物占用额度=占有额度+此次担保合同占用的额度
                        info.setOccupyMoney(info.getOccupyMoney().add(new BigDecimal(tempStr[1])));
                    }
                    info.setUpdateBy(contractInfo.getUpdateBy());
                    guaPledgeInfoService.update(info);
                }
            }
        }
    }
    /**
     * 还原抵质押物占用金额
     *
     * @return
     */
    public void convertPledgeMoney(List<GuaContractPledge> pledgeList) {
        for (GuaContractPledge pledge : pledgeList) {
            BigDecimal guaMoney = pledge.getGuaMoney();// 原金额
            if (StringUtils.isNotEmpty(pledge.getPledgeId())) {
                GuaPledgeInfo info = guaPledgeInfoService.get(pledge.getPledgeId());
                if (info != null) {
                    if (info.getOccupyMoney() != null && (info.getOccupyMoney().compareTo(BigDecimal.ZERO) == 1)) {
                        info.setOccupyMoney(info.getOccupyMoney().subtract(guaMoney));
                    }
                    guaPledgeInfoService.update(info);
                }
            }
        }
        guaPledgeInfoDao.flush();
    }
    /**
     * 根据担保合同Id生成变更轨迹
     *
     * @author luxy
     */
    public void createContractChangeTrack(GuaContractInfoVO contractVO, ICurrentUser user, String conType) {
        String guaId = contractVO.getId();
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        List<GuaConrrantChangeTrack> trackList = new ArrayList<GuaConrrantChangeTrack>();
        if (StringUtils.isNotEmpty(guaId)) {
            // 原抵质押物信息
            List<GuaContractPledgeVO> oldPledgeList = guaContractPledgeService.getGuaContractPledgeList(guaId);
            List<GuaContractPledgeVO> newPledgeList = new ArrayList<GuaContractPledgeVO>();
            for (GuaContractWayVO guaWay : guaDtls) {
                if (StringUtils.isNotEmpty(guaWay.getPledgeIds())) {
                    // 去掉最后一个分号
                    String infoStr = guaWay.getPledgeIds();
                    infoStr = infoStr.substring(0, infoStr.length() - 1);
                    String[] idAndMoney = infoStr.split(";");
                    for (String ids : idAndMoney) {
                        // GuaContractPledge guaContractPledge = new GuaContractPledge();
                        GuaContractPledgeVO guaContractPledge = new GuaContractPledgeVO();
                        String[] tempStr = ids.split(",");
                        guaContractPledge.setGuaContractWayId(guaWay.getId());
                        if (StringUtils.isNotEmpty(tempStr[0])) {
                            guaContractPledge.setPledgeId(tempStr[0]);
                        }
                        guaContractPledge.setGuaMoney(new BigDecimal(tempStr[1]));
                        guaContractPledge.setGuaWayId(guaWay.getWayCodes());
                        newPledgeList.add(guaContractPledge);
                    }
                } else {
                    GuaContractPledgeVO guaContractPledge = new GuaContractPledgeVO();
                    if (StringUtils.isNotEmpty(guaWay.getId())) {
                        guaContractPledge.setGuaContractWayId(guaWay.getId());
                    }
                    guaContractPledge.setGuaMoney(guaWay.getMoney());
                    guaContractPledge.setGuaWayId(guaWay.getWayCodes());
                    newPledgeList.add(guaContractPledge);
                }
            }
            setTrackList(oldPledgeList, newPledgeList, trackList, contractVO, user);// 根据数据设置参数
        }
        if (trackList.size() > 0) {
            for (GuaConrrantChangeTrack track : trackList) {
                track.setConType(conType);
            }
            guaContractChangeDao.batchSave(trackList);
        }
    }
    public void setTrackList(List<GuaContractPledgeVO> oldPledgeList, List<GuaContractPledgeVO> newPledgeList,
                             List<GuaConrrantChangeTrack> trackList, GuaContractInfoVO contractVO, ICurrentUser user) {
        Map<String, GuaContractPledgeVO> mapOld = new HashMap<String, GuaContractPledgeVO>();
        for (GuaContractPledgeVO pledge : oldPledgeList) {
            if (null != pledge.getGuaContractWayId()) {
                mapOld.put(pledge.getGuaContractWayId() + pledge.getPledgeId(), pledge);
            }
        }
        for (GuaContractPledgeVO newPledge : newPledgeList) {
            GuaContractPledgeVO tempPledge = new GuaContractPledgeVO();
            tempPledge = mapOld.get(newPledge.getGuaContractWayId() + newPledge.getPledgeId());
            if (null == tempPledge) {// 变更后有数据增加
                releaseAndOccupyTrackList(newPledge, contractVO, trackList, user, GuaranteeConstant.GUA_TRACK_ZY);// 增加占用轨迹
            } else {// 比较金额是否发生变化
                if (tempPledge.getGuaMoney().compareTo(newPledge.getGuaMoney()) != 0) {// 金额发生变化
                    releaseAndOccupyTrackList(tempPledge, contractVO, trackList, user, GuaranteeConstant.GUA_TRACK_SF);// 增加释放轨迹
                    releaseAndOccupyTrackList(newPledge, contractVO, trackList, user, GuaranteeConstant.GUA_TRACK_ZY);// 增加占用轨迹
                }
            }
        }
        Map<String, GuaContractPledgeVO> mapNew = new HashMap<String, GuaContractPledgeVO>();
        for (GuaContractPledgeVO pledgeN : newPledgeList) {
            if (null != pledgeN.getGuaContractWayId()) {
                mapNew.put(pledgeN.getGuaContractWayId() + pledgeN.getPledgeId(), pledgeN);
            }
        }
        for (GuaContractPledgeVO oldPledge : oldPledgeList) {
            GuaContractPledgeVO tempPledge = new GuaContractPledgeVO();
            if (StringUtils.isNotEmpty(oldPledge.getGuaContractWayId())) {
                tempPledge = mapNew.get(oldPledge.getGuaContractWayId() + oldPledge.getPledgeId());
            }
            if (null == tempPledge) {// 变更后有数据减少
                releaseAndOccupyTrackList(oldPledge, contractVO, trackList, user, GuaranteeConstant.GUA_TRACK_SF);// 增加释放轨迹
            }
            // else{//比较金额是否发生变化
            // if(tempPledge.getGuaMoney().compareTo(oldPledge.getGuaMoney())!=0){//金额发生变化
            // releaseAndOccupyTrackList(tempPledge, contractVO, trackList,user,
            // GuaranteeConstant.GUA_TRACK_SF);//增加释放轨迹
            // releaseAndOccupyTrackList(oldPledge, contractVO,trackList, user, GuaranteeConstant.GUA_TRACK_ZY);//增加占用轨迹
            // }
            // }
        }
    }
    public void releaseAndOccupyTrackList(GuaContractPledgeVO changePledge, GuaContractInfoVO contractVO,
                                          List<GuaConrrantChangeTrack> trackList, ICurrentUser user, String trackType) {
        GuaConrrantChangeTrack guaTrack = new GuaConrrantChangeTrack();
        guaTrack.setGuaId(contractVO.getId());
        guaTrack.setGuaNo(contractVO.getGuaNo());
        guaTrack.setContractCode(contractVO.getContractCode());
        guaTrack.setPledgeId(changePledge.getPledgeId());
        guaTrack.setMoney(changePledge.getGuaMoney());
        guaTrack.setGuaWayId(changePledge.getGuaWayId());// 担保方式id
        if (GuaranteeConstant.GUA_TRACK_SF.equals(trackType)) {
            guaTrack.setTrackType(GuaranteeConstant.GUA_TRACK_SF);// 释放
            guaPledgeOccupyService.updatePledgeOccupy(contractVO.getId(), changePledge.getPledgeId());// 更改抵质押物占用表中的状态为释放
        } else {
            guaTrack.setTrackType(GuaranteeConstant.GUA_TRACK_ZY);// 占用
            // 保存抵质押物占用信息到占用表
            List<GuaPledgeOccupyDTO> occupyDTOS = guaContractInfoService.setPledgeOccupyInfo(contractVO,
                    ConfigConstant.GUACONTRACT);
            if (null != occupyDTOS && occupyDTOS.size() > 0) {
                guaPledgeOccupyService.addPledgeOccupy(occupyDTOS);
            }
        }
        guaTrack.setCreateBy(user.getId());
        guaTrack.setCreateTime(DateUtils.getDate());
        trackList.add(guaTrack);
    }
    /**
     * 保存抵质押物占用信息
     *
     * @param pledgeIds
     */
    public void savePledgeInfo(String pledgeIds) {
        String pzIds = pledgeIds;
        // 根据id更新修改后的占用金额
        if (StringUtils.isNotEmpty(pzIds)) {
            pzIds = pzIds.substring(0, pzIds.length() - 1);// 截去最后一个，
            String[] ids = pzIds.split(";");
            for (String str : ids) {// 遍历id,
                String[] tempStr = str.split(",");
                GuaPledgeInfo info = guaPledgeInfoService.get(tempStr[0]);
                if (info != null) {
                    if (info.getOccupyMoney() != null) {
                        // 抵质押物占用额度=占有额度+此次担保合同占用的额度
                        info.setOccupyMoney(info.getOccupyMoney().add(new BigDecimal(tempStr[1])));
                    } else {
                        info.setOccupyMoney(new BigDecimal(tempStr[1]));
                    }
                    guaPledgeInfoService.update(info);
                }
            }
        }
    }
    public void batchSaveTrackList(List<GuaConrrantChangeTrack> trackList, String conType) {
        for (GuaConrrantChangeTrack track : trackList) {
            track.setConType(conType);
        }
        guaContractChangeDao.batchSave(trackList);
    }
    /**
     * 定时任务，被担保到期提醒
     */
    @Override
    @XxlJob("GuaContractChangeService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.GUA, GuaranteeConstant.COVERCON_REMIND_DAY);// 被担保到期提醒天数
        Integer notifyDays = configDTO.toInteger(0);
        Date startDate = DateUtils.getDateYMD();
        Date endDate = DateUtils.addDay(startDate, notifyDays);
        // 查询被担保到期数据
        List<GuaContractInfoVO> conts = guaContractChangeDao.endContracts(null, startDate, endDate);
        if (!EmptyUtils.isEmptyList(conts)) {
            Map<String, Object> param = new HashMap<>();
            int count = conts.size();
            param.put("count", count);
            notificationService.send("", "", NotificationConstant.CMS.GUA_COVERCON_REMIND, param);
        }
    }
    /**
     * 通过业务id及编码查询担保方式
     *
     * @param businessIds
     * @param sysCode
     * @return
     */
    @Transactional(readOnly = true)
    public List<GuaContractQueryDTO> listGuaWayName(List<String> businessIds, String sysCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("businessIds", businessIds);
        params.put("sysCode", sysCode);
        return guaContractChangeDao.listGuaWayName(params);
    }

    public List<GuaContractOccupDTO> getGua(String sysCode, String startDate, String endDate, String status,String contractId) {
        return guaContractChangeDao.getGua(sysCode, startDate, endDate, status,contractId);
    }
}
