package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.bill.dao.AbBillSignSupplyDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * 签收补录Service
 *
 * @author ouyj
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_SIGNSUPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_SIGNSUPPLY)
@Service("abBillSignSupplyService")
@Transactional
public class AbBillSignSupplyService extends BaseService<AbBillSignSupplyVo, String>
        implements IAccebillBaseService, IWorkFlowListener {
    @Autowired
    private AbBillSignSupplyDao abBillSignSupplyDao;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbBillNotificationService abBillNotificationService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @Autowired
    private AbBillReceivebizService abBillReceivebizService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<AbBillSignSupplyVo, String> getEntityDao() {
        return abBillSignSupplyDao;
    }
    /**
     * 加载AbBillSignSupplyDao表数据
     *
     * @param dataTablesInput 前端传参
     * @return table数据
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillSignSupplyDaoInfo(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_SIGNSUPPLY_QUERY, dataTablesInput.getCurrentUserId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_SIGNSUPPLY_QUERY, corpIds);
        DataTablesOutput<AbBillSignSupplyVo> dataTablesOutput = abBillSignSupplyDao.listAbBillSignSupply(dataTablesInput, corpAuthList);
        Set<String> corpIdSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_ROLE","AB_BILL_SGNUPMK","AB_BILL_DSHNRCD"
                    ,"AB_BILL_SIGNSUPPLY_STATUS","AB_BILL_SIGNERTYPE","AB_BILL_TYPE","AB_BILL_UCONDLPRMSMRK","AB_STTLMMK","AB_BANEM","AB_RCRS_TP"
                    ,"AB_RCRSRSNCD","ACCEBILL_ABRECEIVE_INSIDESING");
            for (AbBillSignSupplyVo bill : listData){
                injectValues(bill, listDictMap);
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
            for (AbBillSignSupplyVo bill : listData){
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
    private void injectValues(AbBillSignSupplyVo bill, Map<String, Map<String, DictDTO>> listDictMap){
        // 业务申请人类别
        Optional.ofNullable(bill.getReqRole()).ifPresent(reqRole -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ROLE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(reqRole)))
                    .ifPresent(dict -> bill.setReqRoleName(dict.getName()));
        });
        // 业务接收人类别
        Optional.ofNullable(bill.getRecRole()).ifPresent(recRole -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ROLE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(recRole)))
                    .ifPresent(dict -> bill.setRecRoleName(dict.getName()));
        });
        // 回复标志
        Optional.ofNullable(bill.getSgnupmk()).ifPresent(sgnupmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SGNUPMK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sgnupmk)))
                    .ifPresent(dict -> bill.setSgnupmkName(dict.getName()));
        });
        // 拒付理由代码
        Optional.ofNullable(bill.getDshnrcd()).ifPresent(dshnrcd -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_DSHNRCD");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(dshnrcd)))
                    .ifPresent(dict -> bill.setDshnrcdName(dict.getName()));
        });
        // 补录业务处理状态
        Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SIGNSUPPLY_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status+"")))
                    .ifPresent(dict -> bill.setStatusName(dict.getName()));
        });
        // 报文编号
        Optional.ofNullable(bill.getMsgtype()).ifPresent(msgtype -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SIGNERTYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(msgtype)))
                    .ifPresent(dict -> bill.setSignerType(dict.getName()));
        });
        // 票据种类
        Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType+"")))
                    .ifPresent(dict -> bill.setBillTypeName(dict.getName()));
        });
        // 到期无条件支付承诺
        Optional.ofNullable(bill.getUcondlprmsmrk()).ifPresent(ucondlprmsmrk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_UCONDLPRMSMRK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(ucondlprmsmrk)))
                    .ifPresent(dict -> bill.setUcondlprmsmrkName(dict.getName()));
        });
        // 线上清算标记
        Optional.ofNullable(bill.getSttlmmk()).ifPresent(sttlmmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_STTLMMK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sttlmmk)))
                    .ifPresent(dict -> bill.setSttlmmkName(dict.getName()));
        });
        // 不得转让标记
        Optional.ofNullable(bill.getBankendrsmtmk()).ifPresent(bankendrsmtmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(bankendrsmtmk)))
                    .ifPresent(dict -> bill.setBankendrsmtmkName(dict.getName()));
        });
        // 追索种类
        Optional.ofNullable(bill.getRcrsTp()).ifPresent(rcrsTp -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_RCRS_TP");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(rcrsTp)))
                    .ifPresent(dict -> bill.setRcrsTpName(dict.getName()));
        });
        // 追索理由代码
        Optional.ofNullable(bill.getRcrsrsncd()).ifPresent(rcrsrsncd -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_RCRSRSNCD");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(rcrsrsncd)))
                    .ifPresent(dict -> bill.setRcrsrsncdName(dict.getName()));
        });
        // 内部业务标志
        Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("ACCEBILL_ABRECEIVE_INSIDESING");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing+"")))
                    .ifPresent(dict -> bill.setInsideSingName(dict.getName()));
        });
    }
    /**
     * 审批完成处理
     *
     * @param buId     业务id
     * @param isPassOk 是否审批通过
     * @param appMsg   日志类型
     * @param userVo   用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void approveOver(String buId, boolean isPassOk, String appMsg, ICurrentUser userVo) {
        if (StringUtils.isEmpty(buId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbAccrecords info = abAccrecordsService.get(buId, true);
        if (info == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        // 将该流水置位无效
        Integer status = AccebillConstant.AB_BILL_BUS_STATUS_2;
        int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
        if (isPassOk) {
            // 恢复上条流水为有效
            abAccrecordsService.updatAppPass(info.getLastid(), false, userVo);
        } else {
            int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
            if (reNum != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            // 将业务单位、业务人员清空
            AbVoucherinfoViceVO abVoucherinfoViceVO = new AbVoucherinfoViceVO();
            abVoucherinfoViceVO.setIds(info.getVvid());
            abVoucherinfoViceVO.setBusCorpId(null);
            abVoucherinfoViceVO.setBusPersonnel(null);
            abVoucherinfoViceService.updateBusCorpSupply(abVoucherinfoViceVO);
        }
        // 修改从表是否签收补录完成
        abVoucherinfoViceService.updateIsSignSupply(info.getVvid(), isPassOk ? 1 : 0);
        // 修改审批状态
        abBillReceivebizService.batchUpdateStatus(new String[]{abAccrecordsService.get(info.getLastid()).getRecid()},
                AccebillConstant.AB_BILL_BUS_STATUS11, isPassOk ? AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK : AccebillConstant.AB_BILL_BUS_STATUS_1);
        String logType = isPassOk ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
        int upBoxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
                userVo);
        if (appNum != 1 || appNum != upBoxStatusNum) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, appMsg, userVo);
    }
    /**
     * 审批回调方法
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
        this.approveOver(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = abWorkFlowService
                .getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_SIGNSUPPLY, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput<AbBillSignSupplyVo> dp = abBillSignSupplyDao.dataTableApprove(dataTablesInput, businessList);
        Optional.ofNullable(dp.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_ROLE","AB_BILL_SGNUPMK","AB_BILL_DSHNRCD"
                    ,"AB_BILL_SIGNSUPPLY_STATUS","AB_BILL_SIGNERTYPE","AB_BILL_TYPE","AB_BILL_UCONDLPRMSMRK","AB_STTLMMK","AB_BANEM","AB_RCRS_TP"
                    ,"AB_RCRSRSNCD","ACCEBILL_ABRECEIVE_INSIDESING");
            for (AbBillSignSupplyVo bill : listData){
                injectValues(bill, listDictMap);
            }
        });
        return workFlowBusiness.merge(dp);
    }
    /**
     * 获票据信息
     *
     * @param id id
     * @return 票据签收数据
     */
    @Transactional(readOnly = true)
    public AbBillSignSupplyVo getAbBillSignSupplyVoById(String id) {
        AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
        AbAccrecordsVO accrecordsVo = new AbAccrecordsVO();
        BeanUtils.copyNotNullProperties(accrecordsInfo, accrecordsVo);
        AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
        AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accrecordsInfo.getVvid(), true);
        AbBillSignSupplyVo reInfo = new AbBillSignSupplyVo();
        BeanUtils.copyNotNullProperties(vouInfo, reInfo);
        BeanUtils.copyNotNullProperties(viceInfo, reInfo);
        BeanUtils.copyNotNullProperties(accrecordsVo, reInfo);
        return reInfo;
    }
    @Override
    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        return abBillReceivebizService.getAbBillInfoById(aid);
    }
    /**
     * 签收补录
     *
     * @param abBillSignSupplyVo 签收补录vo
     * @param user               当前用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void signSupply(AbBillSignSupplyVo abBillSignSupplyVo, ICurrentUser user) {
        String accIds = abBillSignSupplyVo.getIds();
        String recids = abBillSignSupplyVo.getRecids();
        String vvids = abBillSignSupplyVo.getVvids();
        if (StringUtils.isEmpty(accIds) || StringUtils.isEmpty(recids) || StringUtils.isEmpty(vvids)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        } else {
            String[] aids = accIds.split(",");
            String[] vvidArr = vvids.split(",");
            String[] recidArr = recids.split(",");
            int len = aids.length;
            // 批量修改主表box_status
            int num = abVoucherinfoViceService.batchSubmit(vvidArr, user);
            if (num != len) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            // 批量插入流水表数据
            List<AbAccrecords> abList = getAbAccrecords(aids, recidArr);
            num = abAccrecordsService.batchSaveAccrecords(abList, user);
            if (num != len) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            // 修改签收表状态为审批中
            num = abBillReceivebizService.batchUpdateStatus(recidArr, null, AccebillConstant.AB_BILL_BUS_STATUS11);
            if (num != len) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            // 批量设置业务单位、业务人员
            AbVoucherinfoViceVO abVoucherinfoViceVO = new AbVoucherinfoViceVO();
            abVoucherinfoViceVO.setIds(vvids);
            abVoucherinfoViceVO.setBusCorpId(abBillSignSupplyVo.getBusCorpIdSupply());
            if (StringUtils.isNotBlank(abBillSignSupplyVo.getBusCorpIdSupply())){
                CorpDTO corpDTO = abCorpService.get(abBillSignSupplyVo.getBusCorpIdSupply());
                abVoucherinfoViceVO.setBusCorpName(corpDTO.getName());
                abVoucherinfoViceVO.setBusSocCode(corpDTO.getSocCode());
            }
            abVoucherinfoViceVO.setBusPersonnel(abBillSignSupplyVo.getBusPersonnelSupply());
            abVoucherinfoViceService.updateBusCorpSupply(abVoucherinfoViceVO);
            // 保存日志信息
            abLogService.batchSaveAbLog(vvidArr, aids, AccebillConstant.BRIEF_STATUS_9101,
                    AccebillConstant.ABLOG_LOGTYPE_SUBMIT, AccebillConstant.LANG_LOG_SUBMIT_SUCC, user);
            for (AbAccrecords abs : abList) {
                abBillNotificationService.completeMsg(abs, user);
                abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
                AbBillSignSupplyVo variables = new AbBillSignSupplyVo();
                BeanUtils.copyNotNullProperties(abs.getVouInfo(), variables);
                BeanUtils.copyNotNullProperties(abs, variables);
                 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_SIGNSUPPLY, abs.getId(), variables,
                        abs.getVouViceInfo().getApplyCorpId(), user.getId());
            }
        }
    }
    /**
     * 设置新增流水
     *
     * @param aids     流水id数组
     * @param recidArr 签收id数组
     * @return 流水集合
     */
    private List<AbAccrecords> getAbAccrecords(String[] aids, String[] recidArr) {
        List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
        for (int i = 0; i < aids.length; i++) {
            // 获取流水表信息
            AbAccrecords abAccrecords = abAccrecordsService.get(aids[i], true);
            // 获取当前票据主，从表信息
            AbVoucherinfo ab = abVoucherinfoService.get(abAccrecords.getVid(), true);
            AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);
            // 新的流水数据
            AbAccrecords entity = new AbAccrecords();
            // 票据主表ID
            entity.setVid(ab.getId());
            // 票据从表ID
            entity.setVvid(abVice.getId());
            entity.setVouInfo(ab);
            entity.setVouViceInfo(abVice);
            // 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
            entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
            entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
            // 从表清除打回标记 挪到批量处理中
            // 业务类型
            entity.setBriefId(AccebillConstant.BRIEF_STATUS_9101);
            entity.setLastid(aids[i]);
            entity.setLastbillcode(abVoucherinfoService.get(abAccrecords.getVid()).getBillStatus());
            entity.setBankendrsmtmk(abAccrecords.getBankendrsmtmk());
            // 业务归属日期
            entity.setBusinessDate(new Date());
            // 申请日期
            entity.setApplyDate(new Date());
            entity.setBillFlag(ab.getBillFlag());
            // 设置签收表id
            entity.setRecid(recidArr[i]);
            // 业务接收人名称
            entity.setRecNm(abAccrecords.getRecNm());
            abList.add(entity);
        }
        return abList;
    }
}
