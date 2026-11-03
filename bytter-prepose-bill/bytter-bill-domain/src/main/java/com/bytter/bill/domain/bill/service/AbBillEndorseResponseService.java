package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillEndorseResponseDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
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
import com.bytter.core.constant.CommonConstant;
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
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * 背书请求应答Service
 *
 * @author ouyj
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE_RESPONSE)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE_RESPONSE)
@Service("abBillEndorseResponseService")
@Transactional(rollbackFor = Exception.class)
public class AbBillEndorseResponseService extends BaseService<AbVoucherinfo, String>
        implements IWorkFlowListener, IAccebillBaseService {
    @Autowired
    private AbBillEndorseResponseDao abBillEndorseResponseDao;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbInputBankInfoService abInputBankInfoService;
    @Autowired
    private AbBillNotificationService abBillNotificationService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @Autowired
    private AbBillEndorseService abBillEndorseService;
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
    public DataTablesOutput listAbBillEndorseResponse(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_ENDORSERESPONSE_QUERY, user.getId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_ENDORSERESPONSE_QUERY, corpIds);
        DataTablesOutput<AbBillEndorseVo> dataTablesOutput = abBillEndorseResponseDao.listAbBillEndorseResponse(dataTablesInput, corpAuthList);
        Set<String> corpIdSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            // 添加冗余数据
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_AbBillEndorse_STATUS"
                    , "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_SEND_SIGN", "AB_BILL_BUSSTATUS", "AB_BILL_INSOPTSTATUS", "AB_BRIEF_STATUS");
            for (AbBillEndorseVo bill : listData) {
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
     * 背书请求应答
     *
     * @param eo     前端vo
     * @param userVo 当前用户
     */
    public void endorseResponse(AbBillEndorseVo eo, ICurrentUser userVo) {
        // 获取需要修改的条数
        int len = eo.getIds().split(",").length;
        // 批量插入流水表数据
        List<AbAccrecords> abList = getAbAccrecords(eo, userVo);
        int num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
        if (num != len) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 挂审批流程
        for (AbAccrecords abs : abList) {
            // 修改从表的BACK_BRIEF_ID为空
            abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
            AbBillEndorseVo variables = new AbBillEndorseVo();
            BeanUtils.copyNotNullProperties(abs.getVouInfo(), variables);
            BeanUtils.copyNotNullProperties(abs, variables);
             getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE_RESPONSE, abs.getId(), variables,
                    abs.getVouViceInfo().getApplyCorpId(), userVo.getId());
        }
    }
    /**
     * 批量设置新增流水集合
     *
     * @param eo   vo
     * @param user 当前用户
     * @return 新增流水信息
     */
    protected List<AbAccrecords> getAbAccrecords(AbBillEndorseVo eo, ICurrentUser user) {
        List<AbAccrecords> abList = new ArrayList<>();
        List<AbAccrecords> updList = new ArrayList<>();
        // 流水表id数组
        String[] aids = eo.getIds().split(",");
        for (String id : aids) {
            // 获取流水表信息
            AbAccrecords abAccrecords = abAccrecordsService.get(id, true);
            String briefId = abAccrecords.getBriefId();
            String lastId = abAccrecords.getId();
            if (AccebillConstant.BRIEF_STATUS_013.equals(briefId)) {
                // 设置背书请求流水为0
                abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
                updList.add(abAccrecords);
                notificationService.complete(NotificationConstant.ACCEBILL.BILL_ENDORSE_RESPONSE, id, user.getId());
            } else {
                // 如果是背书请求应答流水，则上一笔流水取它的上一笔流水即背书请求流水
                lastId = abAccrecords.getLastid();
            }
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
            entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
            entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
            // 业务类型
            entity.setBriefId(AccebillConstant.BRIEF_STATUS_014);
            entity.setLastid(lastId);
            entity.setLastbillcode(ab.getBillStatus());
            // 业务归属日期
            entity.setBusinessDate(new Date());
            // 申请日期
            entity.setApplyDate(new Date());
            // 是否同意背书
            entity.setIsEndorse(eo.getIsEndorse());
            // 意见或拒绝理由
            entity.setRmk(eo.getRmk());
            // 业务申请人信息和业务接收人信息要和背书请求流水中的数据进行调换
            // 业务申请人名称
            entity.setReqNm(abAccrecords.getRecNm());
            // 业务申请人账号
            entity.setReqAcctId(abAccrecords.getRecAcctId());
            // 业务申请人开户行号
            entity.setReqAcctsvcr(abAccrecords.getRecAcctsvcr());
            // 业务接收人账号
            entity.setRecAcctId(abAccrecords.getReqAcctId());
            // 业务接收人名称
            entity.setRecNm(abAccrecords.getReqNm());
            // 业务接收人开户行号
            entity.setRecAcctsvcr(abAccrecords.getReqAcctsvcr());
            if (ab.getBillFlag() == 11) {
                entity.setReqAcctId(abVice.getBankAcc());
            }
            entity.setBankendrsmtmk(abAccrecords.getBankendrsmtmk());
            entity.setBillFlag(ab.getBillFlag());
            entity.setInsideSing(abAccrecords.getInsideSing());
            abList.add(entity);
        }
        abAccrecordsService.batchUpdate(updList);
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
        // 修改背书请求应答状态
        Integer status = isPass ? AccebillConstant.AB_BILL_BUS_STATUS95 : AccebillConstant.AB_BILL_BUS_STATUS_2;
        abAccrecords.setStatus(status);
        if (isPass) {
            // 是否同意背书请求
            boolean isAgree = AccebillConstant.AB_BILL_VALID_SIGN_1.equals(abAccrecords.getIsEndorse());
            // 修改背书请求流水的指令状态
            String optStatus = isAgree ? AccebillConstant.AB_BILL_OPT_STATUS19 : AccebillConstant.AB_BILL_OPT_STATUS_4;
            // 修改指令状态为发送中，审批状态为审批中
            AbAccrecords lastAbAccrecords = abAccrecordsService.get(abAccrecords.getLastid(), true);
            lastAbAccrecords.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
            lastAbAccrecords.setRmk(abAccrecords.getRmk());
            abAccrecordsService.updateEntity(lastAbAccrecords);
            abAccrecords.setOptStatus(optStatus);
            abAccrecordsService.updateEntity(abAccrecords);
            // 同意且审批通过发起背书申请
            if (isAgree) {
                AbBillEndorseVo abBillEndorseVo = new AbBillEndorseVo();
                abBillEndorseVo.setIds(abAccrecords.getId());
                abBillEndorseVo.setVids(abAccrecords.getVid());
                abBillEndorseVo.setVvids(abAccrecords.getVvid());
                //背入人
                abBillEndorseVo.setPledgeeName(abAccrecords.getRecNm());
                //背入人账号
                abBillEndorseVo.setPledgeeAcc(abAccrecords.getRecAcctId());
                //背入人开户行号
                abBillEndorseVo.setPledgeeBankAcc(abAccrecords.getRecAcctsvcr());
                //内部业务标志
                abBillEndorseVo.setInteriorApply(AccebillConstant.AB_INSIDE_SING_Y);
                //缺少金额 后续会抛异常
                AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());
                abBillEndorseVo.setTransMoney(abVoucherinfo.getBillMoney());
                abBillEndorseVo.setTransMoneys(abVoucherinfo.getBillMoney().toPlainString());
                // 如果为线上电票，则修改该票据对应的应收票据流水为无效
                if (CommonConstant.YES.equals(abAccrecords.getSendSign())) {
                    abAccrecordsService.updateWithEndorse(abAccrecords.getVid());
                }
                abBillEndorseService.batchEndorse(abBillEndorseVo, true, user);
            }
        } else {
            abAccrecordsService.updateEntity(abAccrecords);
            int reNum = abVoucherinfoViceService.updateBackBriefId(abAccrecords.getVvid(), abAccrecords.getBriefId(), abAccrecords.getId());
            if (reNum != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            // 上一条流水VALID_SIGN=1
            reNum = abAccrecordsService.updatAppPass(abAccrecords.getLastid(), false, user);
            if (reNum != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
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
                .getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE_RESPONSE, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput<AbBillEndorseVo> dataTablesOutput = abBillEndorseResponseDao.dataTableApprove(dataTablesInput, businessList);
        Set<String> corpIdSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            // 添加冗余数据
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_AbBillEndorse_STATUS"
                    , "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_SEND_SIGN", "AB_BILL_BUSSTATUS", "AB_BILL_INSOPTSTATUS", "AB_BRIEF_STATUS", "sf_yn");
            for (AbBillEndorseVo bill : listData) {
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
               ,"AB_BANEM" ,"AB_ENTER_STOCK","AB_BANEM","AB_BILL_SEND_SIGN","AB_BILL_BUSSTATUS","AB_BILL_INSOPTSTATUS");
        reInfo.injectValues(listDictMap);
        return reInfo;
    }
    @Override
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        return this.getAbBillEndorseVoById(aid);
    }
}
