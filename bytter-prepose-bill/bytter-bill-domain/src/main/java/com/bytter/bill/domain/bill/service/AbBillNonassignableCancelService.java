package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbBackbillinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.enums.CheckMessageEnum;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.google.common.collect.Lists;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

@WorkFlow(BillWorkFlowConstant.BILL_NONASSIGNABLE_CANCEL)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_NONASSIGNABLE_CANCEL)
@Service("abBillNonassignableCancelService")
@Transactional
public class AbBillNonassignableCancelService extends BaseService<AbVoucherinfoVice, String> implements IAccebillAccbookService
        , IWorkFlowListener, IUserExcelVerifyHandler<AbBillNonassignableCancelVo>, ITaskService {
    @Autowired
    private AbVoucherinfoViceDao abVoucherinfoViceDao;

    @Autowired
    private AbLogService abLogService;

    @Autowired
    private AbBillNotificationService abBillNotificationService;

    @Autowired
    private AbWorkFlowService abWorkFlowService;

    @Autowired
    private AbBackbillinfoService abBackbillinfoService;

    @Autowired
    private AbVoucherinfoDao abVoucherinfoDao;

    @Autowired
    private AbVoucherinfoService abVoucherinfoService;

    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    @Autowired
    private AbAccrecordsService abAccrecordsService;

    @Autowired
    private AbBtBankAccService btBankAccService;

    @Autowired
    private AbCorpService abCorpService;

    @Autowired
    private AbInputBankInfoService abInputBankInfoService;

    @Autowired
    private AbAccrecordsDao abAccrecordsDao;

    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 不可转让撤销查询
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listAbVoucherinfoViceQuery(DataTablesInput input) {
        DataTablesOutput<AbBillNonassignableCancelVo> dataTablesOutput=abVoucherinfoViceDao.nonassignableCancelQuery(input);
        Set<String> corpIdSet = new HashSet<>();
        // 冗余数据
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
                    ,"AB_BILL_ACCEPT_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE","AB_BILL_ONLINE_OFFLINE_TYPE","AB_BILL_UCONDLPRMSMRK", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            for (AbBillNonassignableCancelVo bill : listData){
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
            for (AbBillNonassignableCancelVo bill : listData){
                CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
                bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
            }
        });
        return dataTablesOutput;
    }
    /**
     * 不可转让撤销申请
     */
    public void nonassignableCancelApplication(AbBillNonassignableCancelAppVo vo, ICurrentUser userVo) {
        int len = vo.getAccIds().split(",").length;
        //批量处理主表 box_status
        String[] split = vo.getInfoIds().split(",");
        List<String> infoIdsList = Lists.newArrayList();
        for (String s : split) {
            infoIdsList.add(s);
        }
        List<AbVoucherinfo> listDb = abVoucherinfoDao.list(infoIdsList);
        for (AbVoucherinfo abVoucherinfo : listDb) {
            abVoucherinfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);
            abVoucherinfo.setUpdateDate(new Date());
            abVoucherinfo.setUpdateBy(userVo.getLoginName());
        }
        int infoUpNum = abVoucherinfoDao.batchUpdate(listDb, listDb.size());
        if (len != infoUpNum) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 批量修改从表box_status
        int num = abVoucherinfoViceService.batchSubmit(vo.getViceIds().split(","), userVo);
        if (num != len) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 批量插入流水表数据
        List<AbAccrecords> abList = getAbAccrecords(vo, true, userVo);
        num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
        if (num != len) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 批量写日志
        abLogService.batchSaveAbLog(vo.getViceIds().split(","), vo.getAccIds().split(","), AccebillConstant.BRIEF_STATUS_024,
                "02", "不可转让撤销申请业务", userVo);
        // 挂审批流程
        for (AbAccrecords abs : abList) {
            abBillNotificationService.completeMsg(abs, userVo);
            updateBackBriefIdIsNull(abs.getVvid());
            AbBillNonassignableCancelAppVo variables = new AbBillNonassignableCancelAppVo();
            BeanUtils.copyNotNullProperties(abs.getVouInfo(), variables);
            BeanUtils.copyNotNullProperties(abs, variables);
             getStartHandler().handler(BillWorkFlowConstant.BILL_NONASSIGNABLE_CANCEL, abs.getId(), variables,
                    abs.getVouViceInfo().getApplyCorpId(), userVo.getId());
        }
    }
    //处理流水表记录
    protected List<AbAccrecords> getAbAccrecords(AbBillNonassignableCancelAppVo vo, boolean flag, ICurrentUser userVo) {
        List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
        String[] accIds = vo.getAccIds().split(",");
        int length = accIds.length;
        for (int i = 0; i < length; i++) {
            String id = accIds[i];
            // 获取流水表信息
            AbAccrecords abAccrecords = abAccrecordsService.get(id);
            // 获取当前票据主，从表信息
            AbVoucherinfo info = abVoucherinfoService.get(abAccrecords.getVid());
            AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
            //判断是否是线上票据
            BtBankAccDTO bank = btBankAccService.getBankAccInfo(abVice.getBankAcc());
            if (ObjectUtils.isEmpty(bank)) {
                throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_BANKACC_ISNULL);//TODO 后期再处理
            }
            // 新的流水数据
            AbAccrecords accrecordsEntity = new AbAccrecords();
            BeanUtils.copyNotNullProperties(abAccrecords, accrecordsEntity, "id","signUpFlag","returnCode","returnMsg","sendSign","billSource","recid","returnCode");
            accrecordsEntity.setReturnCode(null);
            if(AccebillConstant.BRIEF_STATUS_6001.equals(abAccrecords.getBriefId()) && AccebillConstant.AB_BILL_SOURCE_REC.equals(abAccrecords.getBillSource())){
                accrecordsEntity.setRecNm(info.getPayer());
                //accrecordsEntity.setRecCmonid(voucherinfo.getPayerCdtRatgGgcy());
                accrecordsEntity.setRecAcctId(info.getPayeracc());
                accrecordsEntity.setRecAcctsvcr(info.getPayerbankno());
                accrecordsEntity.setRecSocNo(info.getBlRmtrUnnIdNo());
                //accrecordsEntity.setRecAcctsvcrAg(abAccrecords.getRecAcctsvcrAg());
            }else {
                //线下
                if (AccebillConstant.CIRCULATION_MODE_0.equals(bank.getCirculationMode())) {
                    //CorpDTO corpDTO = abCorpService.getCorpIdByName(abVice.getCnAccrecords());
                    accrecordsEntity.setRecNm(abVice.getCnAccrecords());//业务接收人名称
                    //accrecordsEntity.setRecCmonid("");//业务接收人组织机构代码
                    //accrecordsEntity.setRecAcctId(info.getPayeeacc());//业务接收人账号
                    //accrecordsEntity.setRecAcctsvcr(info.getPayerbankno());//业务接收人开户行号
                    //accrecordsEntity.setRecAcctsvcrAg("");//业务接收人承接行号
                    //accrecordsEntity.setRecSocNo();
                }
                //线上
                if (AccebillConstant.CIRCULATION_MODE_1.equals(bank.getCirculationMode())) {
                    List<String> statusArr = new ArrayList<>();
                    statusArr.add(AccebillConstant.BRIEF_STATUS_9003);//收票签收
                    statusArr.add(AccebillConstant.BRIEF_STATUS_9010);//背书签收
                    if(statusArr.contains(abAccrecords.getBriefId())){
                        accrecordsEntity.setRecNm(abAccrecords.getRecNm());
                        accrecordsEntity.setRecCmonid(abAccrecords.getRecCmonid());
                        accrecordsEntity.setRecAcctId(abAccrecords.getRecAcctId());
                        accrecordsEntity.setRecAcctsvcr(abAccrecords.getRecAcctsvcr());
                        accrecordsEntity.setRecAcctsvcrAg(abAccrecords.getRecAcctsvcrAg());
                    } else {
                        List<AbBackbillinfo> abBackbillinfoDB = abBackbillinfoService.listAbBackbillinfoByOrigNoAndsbbllRngSn(info, "003,010");
                        if (ObjectUtils.isEmpty(abBackbillinfoDB)) {
                            throw new ParameterException(CommonMessageEnum.BILL_BACKINFO_SYNC_PRO_ERROR);//TODO 后期再处理
                        }
                        AbBackbillinfo abBackbillinfo = abBackbillinfoDB.get(0);
                        accrecordsEntity.setRecNm(abBackbillinfo.getReqNm());
                        accrecordsEntity.setRecCmonid(abBackbillinfo.getReqCmonid());
                        accrecordsEntity.setRecAcctId(abBackbillinfo.getReqAcctId());
                        accrecordsEntity.setRecAcctsvcr(abBackbillinfo.getReqAcctsvcr());
                        accrecordsEntity.setRecAcctsvcrAg(abBackbillinfo.getReqAcctsvcrAg());
                        accrecordsEntity.setRecSocNo(abBackbillinfo.getBlReqUnnIdNo());
                    }
                }
            }
            // 票据主表ID
            accrecordsEntity.setVid(info.getId());
            accrecordsEntity.setVvid(abVice.getId());
            accrecordsEntity.setVouInfo(info);
            accrecordsEntity.setVouViceInfo(abVice);
            // 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
            if (flag) {
                accrecordsEntity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
                accrecordsEntity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
            } else {
                accrecordsEntity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
                accrecordsEntity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
            }
            // 业务类型
            accrecordsEntity.setBriefId(AccebillConstant.BRIEF_STATUS_024);
            accrecordsEntity.setLastid(id);
            accrecordsEntity.setLastbillcode(info.getBillStatus());
            // 申请日期
            accrecordsEntity.setBusinessDate(new Date());// 业务归属日期
            accrecordsEntity.setApplyDate(new Date());// 申请日期
            CorpDTO corpInfo = abCorpService.get(abVice.getCorpId());
            if (StringUtils.isEmpty(accrecordsEntity.getReqNm()) && null != corpInfo) {
                accrecordsEntity.setReqNm(corpInfo.getName());// 业务申请人名称
            }
            accrecordsEntity.setReqSocNo(abVice.getBlHoldUnnIdNo());// 业务申请人社会统一信用代码
            accrecordsEntity.setBillFlag(info.getBillFlag());
            accrecordsEntity.setCreateDate(new Date());
            accrecordsEntity.setUpdateDate(new Date());
            accrecordsEntity.setCreateBy(userVo.getUsername());
            accrecordsEntity.setUpdateBy(userVo.getLoginName());
            accrecordsEntity.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS15);
            accrecordsEntity.setLastCirculateCode(info.getEbillBlCrclIndc());
            List<AbAccrecords> updList = new ArrayList<>();
            //暂时设置上一条流水为已完成，防止列表查询提交后数据还能查询
            abAccrecords.setValidSign("2");
            abAccrecords.setUpdateDate(new Date());
            abAccrecords.setUpdateName(userVo.getLoginName());
            abAccrecords.setUpdateBy(userVo.getLoginName());
            updList.add(abAccrecords);
            abAccrecordsService.batchSaveOrUpdate(updList);
            abList.add(accrecordsEntity);
        }
        return abList;
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = abWorkFlowService
                .getWorkFlowBusiness(BillWorkFlowConstant.BILL_NONASSIGNABLE_CANCEL, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput<AbBillNonassignableCancelVo> dp = abVoucherinfoViceDao.dataTableApprove(dataTablesInput, businessList);
        Optional.ofNullable(dp.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
                    ,"AB_BILL_ACCEPT_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE","AB_BILL_ONLINE_OFFLINE_TYPE","AB_BILL_UCONDLPRMSMRK", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            for (AbBillNonassignableCancelVo bill : listData){
                bill.injectValues(listDictMap);
            }
        });
        return workFlowBusiness.merge(dp);
    }
    /**
     * 重新做业务时设置从表打回业务类型为空
     * 此方法一定要在推送消息处理了以后再调用
     *
     * @param id 从表ID
     * @return 修改成功的笔数
     */
    public int updateBackBriefIdIsNull(String id) {
        return updateBackBriefId(id, "", "");
    }
    /**
     * 审批打回或指令打回时设置从表打回业务类型
     *
     * @param id      从表ID
     * @param briefId 被修改的打回业务类型值
     * @param acid    被修改的打回业务流水ID
     * @return 修改成功的笔数
     */
    public int updateBackBriefId(String id, String briefId, String acid) {
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        return abVoucherinfoViceDao.updateBackBriefId(id, briefId, acid);
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
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY","AB_BANEM"
                ,"AB_BILL_ACCEPT_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE","AB_BILL_ONLINE_OFFLINE_TYPE","AB_BILL_UCONDLPRMSMRK", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
        reInfo.injectValues(listDictMap);
        Set<String> corpIdSet = new HashSet<>();
        if(StrUtil.isNotBlank(reInfo.getApplyCorpId())){
            corpIdSet.add(reInfo.getApplyCorpId());
        }
        if(StrUtil.isNotBlank(reInfo.getCorpId())){
            corpIdSet.add(reInfo.getCorpId());
        }
        if(StrUtil.isNotBlank(reInfo.getBusCorpId())){
            corpIdSet.add(reInfo.getBusCorpId());
        }
      //  this.inject(reInfo);
        if(corpIdSet.size() <= 0){
            return null;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return null;
        }
        CorpDTO corpDTO = stringCorpDTOMap.get(reInfo.getApplyCorpId());
        CorpDTO corpDTO2 = stringCorpDTOMap.get(reInfo.getCorpId());
        CorpDTO corpDTO3 = stringCorpDTOMap.get(reInfo.getBusCorpId());
        reInfo.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
        reInfo.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
        reInfo.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
        reInfo.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
        reInfo.setMsgtype("024");
        return reInfo;
    }
    /**
     * 新增查询银行账户信息
     */
    public Object findBankTypeNameWith(String vvid) {
        if (StringUtils.isBlank(vvid)) {
            return null;
        }
        AbVoucherinfoVice vice = abVoucherinfoViceDao.get(vvid, true);
        if (StringUtils.isNotEmpty(vice) && !StringUtils.isBlank(vice.getBankAcc())) {
            BtBankAccDTO bank = btBankAccService.getBankAccInfo(vice.getBankAcc());
            return StringUtils.isNotEmpty(bank) ? bank.getBankTypeName() : null;
        }
        return null;
    }
    @Override
    public void busOverOp(AbAccrecords info, String logType, String appMsg, ICurrentUser userVo) {
        String id = info.getId();
        List<String> queryDBList = Lists.newArrayList();
        queryDBList.add(id);
        List<AbAccrecords> abAccrecordsDb = abAccrecordsDao.list(queryDBList);
        if (CollectionUtils.isEmpty(abAccrecordsDb)) {
            throw new ParameterException("数据已经刷新请核实在提交");
        }
        AbAccrecords accrecords = abAccrecordsDb.get(0);
        //处理主表
        String vid = accrecords.getVid();
        List<String> vids = Lists.newArrayList(vid);
        List<AbVoucherinfo> list = abVoucherinfoDao.list(vids);
        if (CollectionUtils.isEmpty(list)) {
            throw new ParameterException("数据已经刷新请核实在提交");
        }
        AbVoucherinfo abVoucherinfo = list.get(0);
        abVoucherinfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
        abVoucherinfo.setUpdateBy(userVo.getLoginName());
        abVoucherinfo.setUpdateDate(new Date());
        abVoucherinfo.setUpdateName(userVo.getLoginName());
        List<AbVoucherinfo> abVoucherinfoList = Lists.newArrayList(abVoucherinfo);
        abVoucherinfoDao.batchUpdate(abVoucherinfoList);
        //处理从表
        String vvid = accrecords.getVvid();
        List<String> vvids = Lists.newArrayList(vvid);
        List<AbVoucherinfoVice> list1 = abVoucherinfoViceDao.list(vvids);
        if (CollectionUtils.isEmpty(list1)) {
            throw new ParameterException("数据已经刷新请核实在提交");
        }
        AbVoucherinfoVice abVoucherinfoVice = list1.get(0);
        abVoucherinfoVice.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
        abVoucherinfoVice.setUpdateBy(userVo.getLoginName());
        abVoucherinfoVice.setUpdateDate(new Date());
        abVoucherinfoVice.setUpdateName(userVo.getLoginName());
        List<AbVoucherinfoVice> abVoucherinfoViceList = Lists.newArrayList(abVoucherinfoVice);
        abVoucherinfoViceDao.batchUpdate(abVoucherinfoViceList);
        //处理流水表
        //判断是否是线上票据
        BtBankAccDTO bank = btBankAccService.getBankAccInfo(abVoucherinfoVice.getBankAcc());
        if (ObjectUtils.isEmpty(bank)) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_BANKACC_ISNULL);
        }
        accrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
        //线下
        if (AccebillConstant.CIRCULATION_MODE_0.equals(bank.getCirculationMode())) {
            accrecords.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
            accrecords.setBankendrsmtmk(AccebillConstant.AB_BANEM_Y);
            accrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
        }
        //线上
        if (AccebillConstant.CIRCULATION_MODE_1.equals(bank.getCirculationMode())) {
            accrecords.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS16);
            accrecords.setBankendrsmtmk(AccebillConstant.AB_BANEM_N);
            accrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
        }
        // 修改流水表数据（修改status状态为95：审批通过、修改VALID_SIGN为1：可用）
        //abAccrecordsService.approve(info.getId(), AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
        accrecords.setUpdateBy(userVo.getLoginName());
        accrecords.setUpdateDate(new Date());
        accrecords.setUpdateName(userVo.getUsername());
        List<AbAccrecords> saveData = Lists.newArrayList(accrecords);
        abAccrecordsService.batchUpdate(saveData);
    }
    @Override
    public void busRepulseOp(AbAccrecords info, String logType, String appMsg, ICurrentUser userVo) {
        String id = info.getId();
        List<String> queryDBList = Lists.newArrayList();
        queryDBList.add(id);
        List<AbAccrecords> abAccrecordsDb = abAccrecordsDao.list(queryDBList);
        if (CollectionUtils.isEmpty(abAccrecordsDb)) {
            throw new ParameterException("数据已经刷新请核实在提交");
        }
        AbAccrecords accrecords = abAccrecordsDb.get(0);
        //1、处理主表
        String vid = accrecords.getVid();
        List<String> vids = Lists.newArrayList(vid);
        List<AbVoucherinfo> list = abVoucherinfoDao.list(vids);
        if (CollectionUtils.isEmpty(list)) {
            throw new ParameterException("数据已经刷新请核实在提交");
        }
        AbVoucherinfo abVoucherinfo = list.get(0);
        abVoucherinfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
        abVoucherinfo.setUpdateBy(userVo.getLoginName());
        abVoucherinfo.setUpdateDate(new Date());
        abVoucherinfo.setUpdateName(userVo.getLoginName());
        List<AbVoucherinfo> abVoucherinfoList = Lists.newArrayList(abVoucherinfo);
        abVoucherinfoDao.batchUpdate(abVoucherinfoList);
        //2、处理从表
        String vvid = accrecords.getVvid();
        List<String> vvids = Lists.newArrayList(vvid);
        List<AbVoucherinfoVice> list1 = abVoucherinfoViceDao.list(vvids);
        if (CollectionUtils.isEmpty(list1)) {
            throw new ParameterException("数据已经刷新请核实在提交");
        }
        AbVoucherinfoVice abVoucherinfoVice = list1.get(0);
        abVoucherinfoVice.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
        abVoucherinfoVice.setUpdateBy(userVo.getLoginName());
        abVoucherinfoVice.setUpdateDate(new Date());
        abVoucherinfoVice.setUpdateName(userVo.getLoginName());
        List<AbVoucherinfoVice> abVoucherinfoViceList = Lists.newArrayList(abVoucherinfoVice);
        abVoucherinfoViceDao.batchUpdate(abVoucherinfoViceList);
        //3、处理流水表
        //3.1 处理原流水记录
        List<String> queryDBListByIds = Lists.newArrayList();
        queryDBListByIds.add(accrecords.getLastid());
        List<AbAccrecords> lastDbs=  abAccrecordsDao.list(queryDBListByIds);
        if (CollectionUtils.isEmpty(lastDbs)) {
            throw new ParameterException("数据已经刷新请核实在提交");
        }
        AbAccrecords result = lastDbs.get(0);
        result.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
        result.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
        result.setValidSign(AccebillConstant.VALID_SIGN_TRUE);
        result.setUpdateName(userVo.getLoginName());
        result.setUpdateDate(new Date());
        result.setUpdateBy(userVo.getUsername());
        //3.2 处理不可转让撤销流水记录
        accrecords.setValidSign(AccebillConstant.VALID_SIGN_DEL);
        accrecords.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS_2);
        accrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS_1);
        accrecords.setUpdateName(userVo.getLoginName());
        accrecords.setUpdateDate(new Date());
        accrecords.setUpdateBy(userVo.getUsername());
        accrecords.setRecallType(AccebillConstant.RECALL_TYPE_024);
        List<AbAccrecords> saveData = Lists.newArrayList();
        saveData.add(result);
        saveData.add(accrecords);
        abAccrecordsDao.batchUpdate(saveData, saveData.size());
    }
    @Override
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        return getAbBillEndorseVoById(aid);
    }
    /**
     * 审批回调方法
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
        if (AccebillConstant.LANG_LOG_APP_SUCC.equals(appMsg)) {
            //审批通过
            String businessKey = workFlowResult.getBusinessKey();
            AbAccrecords cords = new AbAccrecords();
            cords.setId(businessKey);
            busOverOp(cords, "02", null, workFlowResult.getUser());
        } else {
//             审批不通过
            String businessKey = workFlowResult.getBusinessKey();
            AbAccrecords cords = new AbAccrecords();
            cords.setId(businessKey);
            busRepulseOp(cords, "02", null, workFlowResult.getUser());
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    @XxlJob("AbBillNonassignableCancelService")
    public void run(String arguments) {
    }
    @Override
    public BaseDao<AbVoucherinfoVice, String> getEntityDao() {
        return null;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(AbBillNonassignableCancelVo abBillNonassignableCancelVo, Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<AbBillNonassignableCancelVo> list, Object... objects) {
    }
}
