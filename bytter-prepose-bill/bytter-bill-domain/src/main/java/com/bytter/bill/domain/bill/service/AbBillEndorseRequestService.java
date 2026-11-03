package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillEndorseRequestDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillEndorseVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * 背书请求Service
 *
 * @author ouyj
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE_REQUEST)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE_REQUEST)
@Service("abBillEndorseRequestService")
@Transactional
public class AbBillEndorseRequestService extends BaseService<AbVoucherinfo, String>
        implements IWorkFlowListener, IAccebillBaseService {
    @Autowired
    private AbBillEndorseRequestDao abBillEndorseRequestDao;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbReceiveBillService abReceiveBillService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbInputBankInfoService abInputBankInfoService;
    @Autowired
    private AbBillAccBalService abBillAccBalService;
    @Autowired
    private AbBillNotificationService abBillNotificationService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @Autowired
    private AbConfigService abConfigService;
    @DubboReference(check = false)
    private IDictService dictService;
    public void update(AbVoucherinfo entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(AbVoucherinfo entity) {
        super.saveOrUpdate(entity);
    }
    /**
     * 加载背书数据
     *
     * @param dataTablesInput 前端传参
     * @param user            当前用户
     * @return 前端数据集合
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbBillEndorseVo> listAbBillEndorseRequest(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuthList = Lists.newArrayList(user.getCorpId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_ENDORSEREQUEST_QUERY, corpIds);
        DataTablesOutput<AbBillEndorseVo> dataTablesOutput = abBillEndorseRequestDao.listAbBillEndorseRequest(dataTablesInput, corpAuthList);
        Set<String> corpIdSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            // 添加冗余数据
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_AbBillEndorse_STATUS"
                    ,"AB_ENTER_STOCK","AB_BANEM","AB_BILL_SEND_SIGN","AB_BILL_BUSSTATUS","AB_BILL_INSOPTSTATUS", "AB_BRIEF_STATUS");
            for (AbBillEndorseVo bill : listData){
                bill.injectValues(listDictMap);
                if(StrUtil.isNotBlank(bill.getApplyCorpId())){
                    corpIdSet.add(bill.getApplyCorpId());
                }
                if(StrUtil.isNotBlank(bill.getCorpId())){
                    corpIdSet.add(bill.getCorpId());
                }
                if(StrUtil.isNotBlank(bill.getBusCorpId())){
                    corpIdSet.add(bill.getBusCorpId());
                }
            }
        });
        if(corpIdSet.size() <= 0){
            return dataTablesOutput;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return dataTablesOutput;
        }
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            for (AbBillEndorseVo bill : listData){
                CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
                bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
                bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
            }
        });
        return dataTablesOutput;
    }
    /**
     * 背书请求
     *
     * @param eo     前端vo
     * @param userVo 当前用户
     */
    public void endorsementRequest(AbBillEndorseVo eo, ICurrentUser userVo) {
        // 获取需要修改的条数
        String[] ids = eo.getIds().split(",");
        // 修改指令状态为发送中，审批状态为审批中
        List<AbAccrecords> accrecordsList = abAccrecordsService.listAbAccrecordsByIds(ids);
        AbAccrecordsVO accrecordsVO = new AbAccrecordsVO();
        accrecordsVO.setIdArray(ids);
        accrecordsVO.setStatusNew(AccebillConstant.AB_BILL_BUS_STATUS11);
        accrecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS16);
        int num = abAccrecordsService.batchUpdate(accrecordsVO, userVo);
        if (accrecordsList.size() != num) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 挂审批流程
        for (AbAccrecords abs : accrecordsList) {
//            abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
            AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abs.getVid());
            AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abs.getVvid());
            AbBillEndorseVo variables = new AbBillEndorseVo();
            BeanUtils.copyNotNullProperties(abVoucherinfo, variables);
            BeanUtils.copyNotNullProperties(abs, variables);
             getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE_REQUEST, abs.getId(), variables,
                    abVoucherinfoVice.getApplyCorpId(), userVo.getId());
        }
    }
    /**
     * 批量设置新增流水集合
     *
     * @param eo vo
     * @return 新增流水信息
     */
    protected List<AbAccrecords> getAbAccrecords(AbBillEndorseVo eo) {
        List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
        // 流水表id数组
        String[] aids = eo.getIds().split(",");
        for (String id : aids) {
            // 获取流水表信息
            AbAccrecords abAccrecords = abAccrecordsService.get(id);
            // 获取当前票据主，从表信息
            AbVoucherinfo ab = abVoucherinfoService.get(abAccrecords.getVid());
            AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
            // 新的流水数据
            AbAccrecords entity = new AbAccrecords();
            // 设置数据来源信息
            if (StringUtils.isNotEmpty(eo.getBillSource())) {
                entity.setBillSource(eo.getBillSource());
            }
            // 票据主表ID
            entity.setVid(ab.getId());
            // 票据从表ID
            entity.setVvid(abVice.getId());
            entity.setVouInfo(ab);
            entity.setVouViceInfo(abVice);
            // 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
            entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
            entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);
            // 指令状态:15待发送
            entity.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS15);
            // 业务类型
            entity.setBriefId(AccebillConstant.BRIEF_STATUS_013);
            entity.setLastid(id);
            entity.setLastbillcode(ab.getBillStatus());
            // 业务归属日期
            entity.setBusinessDate(new Date());
            // 申请日期
            entity.setApplyDate(new Date());
            // 流水号
            entity.setSerialNo(SequenceSingleton.getSequence(SequenceEnum.BILL_ENDORSE_REQUEST_SERIAL_NO));
            if (StringUtils.isEmpty(abVice.getBankAcc())) {
                // 业务接收人账号
                entity.setRecAcctId(abAccrecords.getReqAcctId());
                // 业务接收人名称
                entity.setRecNm(abAccrecords.getReqNm());
                // 业务接收人开户行号
                entity.setRecAcctsvcr(abAccrecords.getReqAcctsvcr());
            } else {
                // 业务接收人账号
                entity.setRecAcctId(abVice.getBankAcc());
                // 业务接收人名称
                entity.setRecNm(abVice.getBankName());
                // 业务接收人开户行号
                entity.setRecAcctsvcr(abVice.getBankCode());
            }
            if (StringUtils.isEmpty(entity.getRecNm())) {
                CorpDTO corpInfo = abCorpService.get(abVice.getCorpId());
                if (corpInfo != null) {
                    // 业务接收人名称
                    entity.setRecNm(corpInfo.getName());
                }
            }
            // 业务申请人名称
            entity.setReqNm(eo.getPledgeeName());
            // 业务申请人账号
            entity.setReqAcctId(eo.getPledgeeAcc());
            // 业务申请人开户行号
            entity.setReqAcctsvcr(eo.getPledgeeBankAcc());
            entity.setBillFlag(ab.getBillFlag());
            entity.setRmk(eo.getRmk());
            entity.setInsideSing(Integer.valueOf(AccebillConstant.AB_INSIDE_SING_Y));
            entity.setAdr(eo.getCorpId());
            if (StringUtils.isEmpty(eo.getBankendrSmtmk())) {
                entity.setBankendrsmtmk(ab.getBanendrsmtmk());
            } else if ("1".equals(eo.getBankendrSmtmk())
                    || AccebillConstant.AB_BANEM_Y.equals(eo.getBankendrSmtmk())) {
                entity.setBankendrsmtmk(AccebillConstant.AB_BANEM_Y);
            } else {
                entity.setBankendrsmtmk(AccebillConstant.AB_BANEM_N);
            }
//            if (StringUtils.isNotEmpty(eo.getIsHide())) {
//                // 是否隐藏流水
//                entity.setIsHide(eo.getIsHide());
//            }
            //领票人
            entity.setTrader(eo.getTrader());
            abList.add(entity);
        }
        return abList;
    }
    @Override
    public BaseDao<AbVoucherinfo, String> getEntityDao() {
        return null;
    }
    /**
     * 审批回调方法
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        boolean isPass = workFlowResult.isPass();
        AbAccrecords abAccrecords = abAccrecordsService.get(workFlowResult.getBusinessKey(), true);
        Integer briefStatus = isPass ? AccebillConstant.AB_BILL_BUS_STATUS95 : AccebillConstant.AB_BILL_BUS_STATUS_1;
        String optStatus = isPass ? AccebillConstant.AB_BILL_OPT_STATUS17 : AccebillConstant.AB_BILL_OPT_STATUS_2;
        String[] ids = {abAccrecords.getId()};
        // 修改指令状态为发送中，审批状态为审批中
        List<AbAccrecords> accrecordsList = abAccrecordsService.listAbAccrecordsByIds(ids);
        AbAccrecordsVO accrecordsVO = new AbAccrecordsVO();
        accrecordsVO.setIdArray(ids);
        accrecordsVO.setStatusNew(briefStatus);
        accrecordsVO.setOptStatusNew(optStatus);
        int num = abAccrecordsService.batchUpdate(accrecordsVO, user);
        if (accrecordsList.size() != num) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
        AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid(),true);
        if (isPass) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("billMoney", abVoucherinfo.getBillMoney());
            messageData.put("billType", abVoucherinfo.getBillType());
            messageData.put("billFlag", abVoucherinfo.getBillFlag());
            logger.info("背书请求审批通过，发送通知，messageData:{}", messageData);
            notificationService.send(abAccrecords.getId(), abVoucherinfoVice.getApplyCorpId(),
                    NotificationConstant.ACCEBILL.BILL_ENDORSE_RESPONSE, messageData);
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput 前端传参
     * @return 审批列表数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = abWorkFlowService
                .getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE_REQUEST, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput<AbBillEndorseVo> dataTablesOutput = abBillEndorseRequestDao.dataTableApprove(dataTablesInput, businessList);
        Set<String> corpIdSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            // 添加冗余数据
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_AbBillEndorse_STATUS"
                    ,"AB_ENTER_STOCK","AB_BANEM","AB_BILL_SEND_SIGN","AB_BILL_BUSSTATUS","AB_BILL_INSOPTSTATUS", "AB_BRIEF_STATUS");
            for (AbBillEndorseVo bill : listData){
                bill.injectValues(listDictMap);
                if(StrUtil.isNotBlank(bill.getApplyCorpId())){
                    corpIdSet.add(bill.getApplyCorpId());
                }
                if(StrUtil.isNotBlank(bill.getCorpId())){
                    corpIdSet.add(bill.getCorpId());
                }
                if(StrUtil.isNotBlank(bill.getBusCorpId())){
                    corpIdSet.add(bill.getBusCorpId());
                }
            }
        });
        if(corpIdSet.size() <= 0){
            return dataTablesOutput;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return dataTablesOutput;
        }
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            for (AbBillEndorseVo bill : listData){
                CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
                bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
                bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
            }
        });
        return workFlowBusiness.merge(dataTablesOutput);
    }
    /**
     * 获票据信息
     *
     * @param id
     * @return
     */
    public AbBillEndorseVo getAbBillEndorseVoById(String id) {
        AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
        AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
        AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(accrecordsInfo.getVvid());// 票据从表信息
        AbBillEndorseVo reInfo = new AbBillEndorseVo();
        BeanUtils.copyNotNullProperties(vouInfo, reInfo);
        BeanUtils.copyNotNullProperties(abVoucherinfoVice, reInfo);
        BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
        reInfo.setBankendrSmtmk(accrecordsInfo.getBankendrsmtmk());
        // 根据单位id获取单位信息
        // 根据单位id获取单位信息
        if (StringUtils.isEmpty(reInfo.getCorpName())) {
            CorpDTO corpInfo = abCorpService.get(abVoucherinfoVice.getCorpId());
            if (corpInfo != null) {
                reInfo.setCorpName(corpInfo.getName());
            }
        }
        if (StringUtils.isEmpty(reInfo.getApplyCorpName()) && StringUtils.isNotEmpty(abVoucherinfoVice.getApplyCorpId())) {
            CorpDTO applyCorpInfo = abCorpService.get(abVoucherinfoVice.getApplyCorpId());
            if (applyCorpInfo != null) {
                reInfo.setApplyCorpName(applyCorpInfo.getName());
            }
        }
        if (StringUtils.isEmpty(reInfo.getBusCorpName()) && StringUtils.isNotEmpty(abVoucherinfoVice.getBusCorpId())) {
            CorpDTO busCorpInfo = abCorpService.get(abVoucherinfoVice.getBusCorpId());
            if (busCorpInfo != null) {
                reInfo.setBusCorpName(busCorpInfo.getName());
            }
        }
        // 根据银行账号获取银行信息
        /*
         * BtBankAccDTO bankAccDTO =
         * getBtBankAccDTO(accrecordsInfo.getReqAcctId()); if(bankAccDTO!=null){
         * reInfo.setAccName(bankAccDTO.getAccName()); }
         */
        // 根据银行行号得到银行账号信息
        InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankCode(accrecordsInfo.getRecAcctsvcr());
        if (bankInfo != null) {
            reInfo.setPledgeeBankName(bankInfo.getBankName());
        }
        if (AccebillConstant.AB_INSIDE_SING_Y.equals(
                accrecordsInfo.getInsideSing() != null ? String.valueOf(accrecordsInfo.getInsideSing()) : null)) {
            // 根据银行账号获取银行信息
            BtBankAccDTO recBankAcc = abAccrecordsService.getBtBankAccInfo(accrecordsInfo.getRecAcctId());
            if (recBankAcc != null) {
                if (StringUtils.isEmpty(reInfo.getPledgeeBankName())) {
                    reInfo.setPledgeeBankName(recBankAcc.getBankName());
                }
                // 根据银行账号获取单位信息
                CorpDTO recCorpInfo = abCorpService.get(recBankAcc.getCorpId());
                if (recCorpInfo != null) {
                    reInfo.setEndorseCorpName("(" + recCorpInfo.getCode() + ")" + recCorpInfo.getName());
                }
            } else if (StringUtils.isNotEmpty(accrecordsInfo.getAdr())) {//背书业务时adr为内部单位id
                // 根据银行账号获取单位信息
                CorpDTO recCorpInfo = abCorpService.get(accrecordsInfo.getAdr());
                if (recCorpInfo != null) {
                    reInfo.setEndorseCorpName("(" + recCorpInfo.getCode() + ")" + recCorpInfo.getName());
                }
            } else {
                reInfo.setEndorseCorpName(accrecordsInfo.getRecNm());
            }
        }
        // 添加冗余数据
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_AbBillEndorse_STATUS"
                ,"AB_ENTER_STOCK","AB_BANEM","AB_BILL_SEND_SIGN","AB_BILL_BUSSTATUS","AB_BILL_INSOPTSTATUS");
        reInfo.injectValues(listDictMap);
        return reInfo;
    }
    @Override
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        return this.getAbBillEndorseVoById(aid);
    }
    /**
     * 背书请求（付款办理）
     *
     * @param eo   背书vo
     * @param user 当前用户
     */
    public void endorseRequestWithPay(AbBillEndorseVo eo, ICurrentUser user) {
        List<AbAccrecords> abList = getAbAccrecords(eo);
        int num = abAccrecordsService.batchSaveAccrecords(abList, user);
        if (num != abList.size()) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
    }
}
