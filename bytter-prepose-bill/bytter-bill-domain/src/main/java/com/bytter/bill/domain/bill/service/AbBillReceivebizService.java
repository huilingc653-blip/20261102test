package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillReceivebizDao;
import com.bytter.bill.domain.bill.dao.AbDraftExcDao;
import com.bytter.bill.domain.bill.entity.*;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import com.bytter.bill.domain.bill.vo.AbBillSignStrategyVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
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
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 票据签收信息
 * @date 2017-06-21 15:36:42
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_SIGN)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_SIGN)
@Service("abBillReceivebizService")
@Transactional
public class AbBillReceivebizService extends BaseService<AbBillReceivebiz, String>
        implements IAccebillBaseService, IWorkFlowListener, ITaskService {
    @Autowired
    private AbBillReceivebizDao abBillReceivebizDao;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbInputBankInfoService abInputBankInfoService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbFbsBudgetProcessService abFbsBudgetProcessService;
    @Autowired
    private AbBillNotificationService abBillNotificationService;
    @Autowired
    private AbBtBankAccService abBtBankAccService;
    @Autowired
    private AbRatUrationService abRatUrationService;
    @Autowired
    private AbUserService abUserService;
    @Autowired
    private AbBillSignStrategyService abBillSignStrategyService;
    @DubboReference(check = false)
    private IBlackWhiteService blackWhiteService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @DubboReference(check = false)
    private ICorpService corpService;
//	@Autowired
//	private BtInputBankInfoService btInputBankInfoService;

    @Autowired
    private AbDraftExcService abDraftExcService;

    @Autowired
    private AbDraftExcDao abDraftExcDao;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(AbBillReceivebiz entity) {
        super.deleteEntity(entity);
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
    public void save(AbBillReceivebiz entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(AbBillReceivebiz entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(AbBillReceivebiz entity) {
        super.saveOrUpdate(entity);
    }

    public BaseDao<AbBillReceivebiz, String> getEntityDao() {
        return abBillReceivebizDao;
    }

    /**
     * 根据IDS查询票据签收表信息
     *
     * @param ids
     * @return
     */
    @Transactional(readOnly = true)
    public List<AbBillReceivebiz> listAbBillReceivebizByIds(String[] ids, AbBillReceivebizVO billReceivebizVo) {
        return abBillReceivebizDao.listAbBillReceivebizByIds(ids, billReceivebizVo);
    }

    /**
     * 展示票据签收信息查询信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillReceivebizInfo(DataTablesInput input) {
        List<String> corpBankAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_RECEIVEBIZ_BANKQUERY, input.getCurrentUserId());
        List<String> corpMerchAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_RECEIVEBIZ_MERCHQUERY, input.getCurrentUserId());
        List<String> corpOtherAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_RECEIVEBIZ_OTHERQUERY, input.getCurrentUserId());
        String corpIds = input.getFormdata().get("ins_recCorpId") == null ? null : String.valueOf(input.getFormdata().get("ins_recCorpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(input, AccebillConstant.AB_BILL_RESOURCE_RECEIVEBIZ_BANKQUERY, corpIds);
        accountGrantService.setElectricBillAccount(input, AccebillConstant.AB_BILL_RESOURCE_RECEIVEBIZ_MERCHQUERY, corpIds);
        accountGrantService.setElectricBillAccount(input, AccebillConstant.AB_BILL_RESOURCE_RECEIVEBIZ_OTHERQUERY, corpIds);
        DataTablesOutput<AbBillReceivebizVO> out = abBillReceivebizDao.listAbBillReceivebizInfo(input, corpBankAuthList, corpMerchAuthList, corpOtherAuthList);

        if (!(input.getFormdata() != null && StringUtils.isNotEmpty(input.getFormdata().get("payerbankName"))) && out != null && !CollectionUtils.isEmpty(out.getData())) {
            for (AbBillReceivebizVO vo : out.getData()) {
                if (StringUtils.isNotBlank(vo.getPayerbankName()) || StringUtils.isBlank(vo.getPayerbankno())) {
                    continue;
                }

                InputBankInfoDTO bankInfoDTO = abInputBankInfoService.getBtInputBankInfo(vo.getPayerbankno());
                if (bankInfoDTO == null) {
                    continue;
                }
                vo.setPayerbankName(bankInfoDTO.getBankName());
            }
        }
        if (!(input.getFormdata() != null && StringUtils.isNotEmpty(input.getFormdata().get("acceptorbankName"))) && out != null && !CollectionUtils.isEmpty(out.getData())) {
            for (AbBillReceivebizVO vo : out.getData()) {
                if (StringUtils.isNotBlank(vo.getAcceptorbankName()) || StringUtils.isBlank(vo.getAcceptorbankno())) {
                    continue;
                }
                InputBankInfoDTO bankInfoDTO = abInputBankInfoService.getBtInputBankInfo(vo.getAcceptorbankno());
                if (bankInfoDTO == null) {
                    continue;
                }
                vo.setAcceptorbankName(bankInfoDTO.getBankName());
            }
        }
        /*if (!(input.getFormdata() != null && StringUtils.isNotEmpty(input.getFormdata().get("recBankTypeName"))) && out != null && !CollectionUtils.isEmpty(out.getData())) {
            for (AbBillReceivebizVO vo : out.getData()) {
                if (StringUtils.isNotBlank(vo.getRecBankTypeName()) || StringUtils.isBlank(vo.getRecAcctId())) {
                    continue;
                }
                BtBankAccDTO bankAccDTO = bankAccService.getBankAccInfo(vo.getRecAcctId());
                InputBankInfoDTO bankInfoDTO = abInputBankInfoService.getBtInputBankInfo(bankAccDTO.getBankCode());
                if (bankInfoDTO == null) {
                    continue;
                }
                vo.setRecBankTypeName(bankInfoDTO.getBankTypeName());
            }
        }*/
        for (AbBillReceivebizVO vo : out.getData()) {
            if(vo.getCorpCode()==null){
                CorpDTO corpDTO=corpService.get(vo.getRecCorpId());
                vo.setCorpCode(corpDTO.getCode());
                vo.setCorpName(corpDTO.getName());
            }
            if(StringUtils.isNotBlank(vo.getBusCorpId())){
                CorpDTO corpDTO=corpService.get(vo.getBusCorpId());
                vo.setBusCorpName(corpDTO.getName());
            }
        }
        // 冗余数据
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_ROLE", "AB_BILL_SGNUPMK", "AB_BILL_DSHNRCD"
                , "AB_BILL_RECEIVEBIZ_STATUS", "AB_BILL_SIGNERTYPE", "AB_BILL_TYPE", "AB_BILL_UCONDLPRMSMRK", "AB_STTLMMK", "AB_BANEM", "AB_RCRS_TP"
                , "AB_RCRSRSNCD", "ACCEBILL_ABRECEIVE_INSIDESING", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
        List<AbBillReceivebizVO> data = out.getData();
        if (CollectionUtil.isNotEmpty(data)){
            out.getData().stream().forEach(vo -> {
                injectValues(vo, listDictMap);
            });
        }
        return out;
    }

    /**
     * 根据ID查询签收信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public AbBillReceivebizVO getAbBillReceivebizVOById(String id) {
        AbBillReceivebiz billReInfo = abBillReceivebizDao.get(id, true);
        AbAccrecords accrecords = null;
        if (billReInfo == null) {
            accrecords = abAccrecordsService.get(id, true);
            if (accrecords != null) {
                if (StringUtils.isNotEmpty(accrecords.getRecid())) {
                    billReInfo = abBillReceivebizDao.get(accrecords.getRecid(), true);
					AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecords.getVid(), true);
					if(billReInfo != null){
						billReInfo.setVouInfo(vouInfo);
					}
                } else {
                    billReInfo = new AbBillReceivebiz();
                    AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecords.getVid(), true);
                    BeanUtils.copyNotNullProperties(vouInfo, billReInfo);
                    BeanUtils.copyNotNullProperties(accrecords, billReInfo);
                    billReInfo.setId(null);
                    billReInfo.setMsgtype(accrecords.getBriefId().substring(1));
                    billReInfo.setSignUpFlag(AccebillConstant.AB_SGNUPMK_Y);
                    billReInfo.setSgnupmk(AccebillConstant.AB_SGNUPMK_Y);
                    billReInfo.setBussdt(accrecords.getBusinessDate());
                    billReInfo.setReplyDt(accrecords.getBusinessDate());
                    billReInfo.setRecRole(accrecords.getReqRole());
                    billReInfo.setRecNm(accrecords.getReqNm());
                    billReInfo.setRecCmonid(accrecords.getReqCmonid());
                    billReInfo.setRecAcctId(accrecords.getReqAcctId());
                    billReInfo.setRecAcctsvcr(accrecords.getReqAcctsvcr());
                    billReInfo.setRecAcctsvcrAg(accrecords.getReqAcctsvcrAg());
                    billReInfo.setReqRole(accrecords.getRecRole());
                    billReInfo.setReqNm(accrecords.getRecNm());
                    billReInfo.setReqCmonid(accrecords.getRecCmonid());
                    billReInfo.setReqAcctId(accrecords.getRecAcctId());
                    billReInfo.setReqAcctsvcr(accrecords.getRecAcctsvcr());
					billReInfo.setReqAcctsvcrAg(accrecords.getRecAcctsvcrAg());
					billReInfo.setVouInfo(vouInfo);
                }
            }
        }
		if(billReInfo!=null){
			AbVoucherinfoVO vouInfo = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(billReInfo.getOrigNo(),billReInfo.getSbbllRngStrtSn(),billReInfo.getSbbllRngEndSn());
			if(vouInfo!=null){
				billReInfo.setBillType(billReInfo.getBillType() == null ? vouInfo.getBillType() : billReInfo.getBillType());
				AbVoucherinfo vf = new AbVoucherinfo();
				BeanUtils.copyNotNullProperties(vouInfo,vf);
				billReInfo.setVouInfo(vf);
			}
		}
        setBillReceivebizBankName(billReInfo, null);
        AbBillReceivebizVO reInfo = new AbBillReceivebizVO();
        BeanUtils.copyNotNullProperties(billReInfo, reInfo);
		if(billReInfo.getVouInfo() != null){
			reInfo.setPayerCdtRatgs(billReInfo.getVouInfo().getPayerCdtRatgs());
			reInfo.setPayerCdtRatgGgcy(billReInfo.getVouInfo().getPayerCdtRatgGgcy());
			reInfo.setPayerCdtRatgDuedt(billReInfo.getVouInfo().getPayerCdtRatgDuedt());
		}
        if (StringUtils.isEmpty(reInfo.getUcondlprmsmrk())
                && AccebillConstant.AB_MSGTYPE_ACCEPT.equals(reInfo.getMsgtype())) {
            reInfo.setUcondlprmsmrk(AccebillConstant.UCONDLCONSGNMTMRK);
        }
        if (accrecords == null) {
            // 从表id
            List<AbAccrecords> listInfo = abAccrecordsService.listAbAccrecordsByRecId(reInfo.getId());
            if (listInfo != null && !listInfo.isEmpty()) {
                accrecords = listInfo.get(0);
            }
        }
        if (accrecords != null) {
            // 设置业务人员、业务单位
            AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(accrecords.getVvid(), true);
            reInfo.setBusCorpId(abVoucherinfoVice.getBusCorpId());
            reInfo.setBusPersonnel(abVoucherinfoVice.getBusPersonnel());
            reInfo.setVvid(accrecords.getVvid());
            reInfo.setCreateBy(accrecords.getCreateBy());
            reInfo.setCreateName(accrecords.getCreateName());
            reInfo.setBriefId(accrecords.getBriefId());
            reInfo.setAcid(accrecords.getId());
            reInfo.setApplyCorpId(abVoucherinfoVice.getApplyCorpId());
			if(StrUtil.isEmpty(reInfo.getOldDraftId())){
				reInfo.setOldDraftId(abVoucherinfoVice.getOldDraftId());
			}
        } else {
            reInfo.setBriefId(reInfo.getMsgtype());
        }
        //背书签收与收票签收
        if (AccebillConstant.AB_MSGTYPE_RECBILL.equals(reInfo.getMsgtype())
                || AccebillConstant.AB_MSGTYPE_RECITE.equals(reInfo.getMsgtype())
                || AccebillConstant.AB_MSGTYPE_PLEDGE.equals(reInfo.getMsgtype())
                || AccebillConstant.AB_MSGTYPE_PLEDGE_RELIEVE.equals(reInfo.getMsgtype())) {
            reInfo.setInsideSing(Integer.valueOf(AccebillConstant.AB_INSIDE_SING_N));
            if (StringUtils.isNotEmpty(reInfo.getReqAcctId()) || StringUtils.isNotEmpty(reInfo.getReqNm())) {
                CorpDTO dto = this.getCorpDTOByAcctIdOrReqNm(reInfo.getReqAcctId(), reInfo.getReqNm());
                if (dto != null) {
                    reInfo.setInsideSing(Integer.valueOf(AccebillConstant.AB_INSIDE_SING_Y));
                    reInfo.setCorpId(dto.getId());
                    reInfo.setCorpName(dto.getName());
                    reInfo.setCorpCode(dto.getCode());
                    reInfo.setSocCode(dto.getSocCode());
                }
            }
        }
        if (StringUtils.isEmpty(reInfo.getCorpId())) {
            String corpId = null;
            if (accrecords != null) {
                corpId = abVoucherinfoViceService.get(accrecords.getVvid()).getCorpId();
            } else {
                corpId = reInfo.getRecCorpId();
            }
            // 根据单位id获取单位信息
            CorpDTO corpInfo = abCorpService.get(corpId);
            if (corpInfo != null) {
                reInfo.setCorpName(corpInfo.getName());
                reInfo.setCorpCode(corpInfo.getCode());
				reInfo.setApplyCorpName(corpInfo.getName());
				reInfo.setSocCode(corpInfo.getSocCode());
            }
        }
        if (StringUtils.isEmpty(reInfo.getApplyCorpName())) {
            reInfo.setApplyCorpName(reInfo.getCorpName());
        }
        if (accrecords != null && StringUtil.isNotEmpty(accrecords.getId())) {
            AbVoucherinfoVO voucher = abVoucherinfoService.getAbVoucherinfoVOByAccId(accrecords.getId());
            if (voucher != null) {
                reInfo.setPayerCdtRatgDuedt(voucher.getPayerCdtRatgDuedt());
                reInfo.setPayerCdtRatgGgcy(voucher.getPayerCdtRatgGgcy());
                reInfo.setPayerCdtRatgs(voucher.getPayerCdtRatgs());
                reInfo.setNewStmIssuofpprInd(voucher.getNewStmIssuofpprInd());
                reInfo.setNewStmIssuofpprIndName(voucher.getNewStmIssuofpprIndName());
                reInfo.setBlgrdbagcrclPrmtInd(voucher.getBlgrdbagcrclPrmtInd());
                reInfo.setBlgrdbagcrclPrmtIndName(voucher.getBlgrdbagcrclPrmtIndName());
                reInfo.setSbbllRngStrtSn(voucher.getSbbllRngStrtSn());
                reInfo.setSbbllRngEndSn(voucher.getSbbllRngEndSn());
                reInfo.setBlStdAmt(voucher.getBlStdAmt());
                reInfo.setBlRmtrUnnIdNo(voucher.getBlRmtrUnnIdNo());
                reInfo.setBillMoney(voucher.getBillMoney());
            }
        }
//		this.inject(reInfo);
        dictTranslation(reInfo);
        return reInfo;
    }

    private void dictTranslation(AbBillReceivebizVO reInfo) {

        //回复标志
        if (reInfo.getSgnupmk() != null && StringUtils.isBlank(reInfo.getSgnupmkName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_BILL_SGNUPMK).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setSgnupmkName(collect.get(reInfo.getSgnupmk()));
        }
        //拒付理由代码
        if (reInfo.getDshnrcd() != null && StringUtils.isBlank(reInfo.getDshnrcdName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_BILL_DSHNRCD).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setDshnrcdName(collect.get(reInfo.getDshnrcd()));
        }

        //处理状态
        if (reInfo.getStatus() != null && StringUtils.isBlank(reInfo.getStatusName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setStatusName(collect.get(reInfo.getStatus()));
        }

        //报文编号
        if (reInfo.getMsgtype() != null && StringUtils.isBlank(reInfo.getSignerType())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_BILL_SIGNERTYPE).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setSignerType(collect.get(reInfo.getMsgtype()));
        }

        //票据种类：10 银票；11 商票
        if (reInfo.getBillType() != null && StringUtils.isBlank(reInfo.getBillTypeName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_BILL_TYPE_DICT).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setBillTypeName(collect.get(reInfo.getBillType()));
        }

        //到期无条件支付承诺 承兑回复必填 CC00含委托/承诺兑付
        if (reInfo.getUcondlprmsmrk() != null && StringUtils.isBlank(reInfo.getUcondlprmsmrkName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_BILL_UCONDLPRMSMRK).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setUcondlprmsmrkName(collect.get(reInfo.getUcondlprmsmrk()));
        }
        //线上清算标记： SM00 开启线上清算；SM01 关闭线上清算
        if (reInfo.getUcondlprmsmrk() != null && StringUtils.isBlank(reInfo.getUcondlprmsmrkName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_STTLMMK_DICT).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setUcondlprmsmrkName(collect.get(reInfo.getUcondlprmsmrk()));
        }
        //不得转让标记：EM00 可再转让；EM01 不得转让
        if (reInfo.getBankendrsmtmk() != null && StringUtils.isBlank(reInfo.getBankendrsmtmkName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_BANEM_DICT).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setBankendrsmtmkName(collect.get(reInfo.getBankendrsmtmk()));
        }

        //追索种类：RT00 拒付追索；RT01 非拒付追索
        if (reInfo.getRcrsTp() != null && StringUtils.isBlank(reInfo.getRcrsTpName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_RCRS_TP_DICT).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setRcrsTpName(collect.get(reInfo.getRcrsTp()));
        }
        //追索理由代码:RC00承兑人被依法宣告破产RC01承兑人因违法被责令终止活动（非拒付追索时填写）
        if (reInfo.getRcrsrsncd() != null && StringUtils.isBlank(reInfo.getRcrsrsncdName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_RCRSRSNCD_DICT).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setRcrsrsncdName(collect.get(reInfo.getRcrsrsncd()));
        }
        //        内部业务标志：0  否；1 是
        if (reInfo.getInsideSing() != null && StringUtils.isBlank(reInfo.getInsideSingName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.ACCEBILL_ABRECEIVE_INSIDESING_DICT).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setInsideSingName(collect.get(reInfo.getInsideSing()));
        }
        //出票标志
        if (reInfo.getNewStmIssuofpprInd() != null && StringUtils.isBlank(reInfo.getNewStmIssuofpprIndName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_BILL_NEW_STM_ISSUOFPPR_IND_DICT).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setNewStmIssuofpprIndName(collect.get(reInfo.getNewStmIssuofpprInd()));
        }
        //出票标志
        if (reInfo.getBlgrdbagcrclPrmtInd() != null && StringUtils.isBlank(reInfo.getBlgrdbagcrclPrmtIndName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_BILL_BLGRDBAGCRCL_PRMT_IND_DICT).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setBlgrdbagcrclPrmtIndName(collect.get(reInfo.getBlgrdbagcrclPrmtInd()));
        }
        //票据类型
        if (reInfo.getBillType() != null && StringUtils.isBlank(reInfo.getBillTypeName())) {
            Map<String, String> collect = dictService.list(AccebillConstant.AB_BILL_TYPE_DICT).stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            reInfo.setBillTypeName(collect.get(reInfo.getBillType()+""));
        }

    }

    /**
     * 冗余Dict数据。
     *
     * @param bill        票据签收信息。
     * @param listDictMap 字典表数据。
     */
    private void injectValues(AbBillReceivebizVO bill, Map<String, Map<String, DictDTO>> listDictMap) {
        // 业务申请人类别
        Optional.ofNullable(bill.getReqRole()).ifPresent(reqRole -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ROLE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(reqRole)))
                    .ifPresent(dict -> bill.setReqRoleName(dict.getName()));
        });
        // 业务申请人类别
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
        // 处理状态
        Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_RECEIVEBIZ_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
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
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
                    .ifPresent(dict -> bill.setBillTypeName(dict.getName()));
        });
        // 到期无条件支付承诺
        Optional.ofNullable(bill.getUcondlprmsmrk()).ifPresent(ucondlprmsmrk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_UCONDLPRMSMRK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(ucondlprmsmrk)))
                    .ifPresent(dict -> bill.setUcondlprmsmrkName(dict.getName()));
        });
        // 到期无条件支付承诺
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
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
                    .ifPresent(dict -> bill.setInsideSingName(dict.getName()));
        });
        //出票标志: 1 新系统出票 、0 老系统出票
        Optional.ofNullable(bill.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
                    .ifPresent(dict -> bill.setNewStmIssuofpprIndName(dict.getName()));
        });
        //是否允许分包：0 否、 1 是 blgrdbagcrclPrmtIndName
        Optional.ofNullable(bill.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtIndName -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtIndName)))
                    .ifPresent(dict -> bill.setBlgrdbagcrclPrmtIndName(dict.getName()));
        });

        if (StringUtils.isNotBlank(bill.getRecAcctId())) {
            BtBankAccDTO bankAccInfo = bankAccService.getBankAccInfo(bill.getRecAcctId());
            Optional.ofNullable(bankAccInfo).ifPresent(bankAcc -> {
                CorpDTO corpDTO = abCorpService.get(bankAcc.getCorpId());
                Optional.ofNullable(corpDTO).ifPresent(corp -> {
                    bill.setApplyCorpName(corp.getName());
                    bill.setApplyCorpId(corp.getId());
                    bill.setApplyCorpCode(corp.getCode());
                    bill.setBusCorpId(corp.getId());
                    bill.setBusCorpName(corp.getName());
                    bill.setCorpCode(corp.getCode());
                    bill.setCorpId(corp.getId());
                    bill.setCorpName(corp.getName());
                });
            });
        }

    }

    /**
     * 根据账户账号或名称获取账户信息
     *
     * @param acctId
     * @param reqNm
     * @return
     */
    public CorpDTO getCorpDTOByAcctIdOrReqNm(String acctId, String reqNm) {
        if (StringUtils.isEmpty(acctId) && StringUtils.isEmpty(reqNm)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        //获取账户信息
        BtBankAccDTO params = new BtBankAccDTO();
        String reqCorpId = "";
        CorpDTO dto = null;
        if (StringUtils.isNotEmpty(acctId)) {
            params.setBankAcc(acctId);
            List<BtBankAccDTO> listAcc = abBtBankAccService.getBankAccList(params);
            if (listAcc != null && !listAcc.isEmpty()) {
                reqCorpId = listAcc.get(0).getCorpId();
            }
            if (StringUtils.isNotEmpty(reqCorpId)) {
                dto = abCorpService.get(reqCorpId);
            }
        } else {
            dto = abCorpService.getCorpIdByName(reqNm);
        }
        return dto;
    }

    /**
     * 进行票据签收操作
     *
     * @param reqInfo
     * @return
     */
    public void billSign(AbBillReceivebizVO reqInfo, ICurrentUser userVo) {
        try {
            reqInfo.setReplyDt(new Date());
            String id = reqInfo.getId();
            if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
            } else {
                String[] ids = StringUtils.split(id, ",");
                //进行先数据锁定避免并发
                AbBillReceivebizVO reqInfoSych=new AbBillReceivebizVO();
                batchUpdateAbBillReceivebiz(ids, reqInfoSych, null);
                reqInfo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK);
                List<AbBillReceivebiz> billReceivebizList = abBillReceivebizDao.listAbBillReceivebizByIds(ids, reqInfo);
                if (ids.length != billReceivebizList.size()) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                if(StringUtils.isNotBlank(reqInfo.getSgnupmk()) && StringUtils.isBlank(reqInfo.getSgnupmkName())){
                    reqInfo.setSgnupmkName(dictService.getByName(AccebillConstant.AB_BILL_SGNUPMK, reqInfo.getSgnupmk()));
                }
                //只针对兴业银行和提示付款拒签才需要拒签理由
				//拒签理由校验 start
				if("SU01".equals(reqInfo.getSgnupmk())){
					for(AbBillReceivebiz abr : billReceivebizList){
						if(AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(abr.getNewStmIssuofpprInd())){
							if((StringUtil.isEmpty(reqInfo.getNewDshnrcd()) && (abr.getRecAcctsvcr().startsWith("309")
																				||AccebillConstant.BILL_SIGNERTYPE_020.equals(abr.getMsgtype())
																				||AccebillConstant.BILL_SIGNERTYPE_021.equals(abr.getMsgtype())))
									|| (StringUtil.isNotEmpty(reqInfo.getNewDshnrcd()) && !reqInfo.getNewDshnrcd().startsWith("CP"))){
								throw new BusinessException(AccebillMessageEnum.COMMON_MESSAGE_CONFIRM_NEWSELECTDSHNR);
							}
							if("CP06".equals(reqInfo.getNewDshnrcd()) && StringUtil.isBlank_new(reqInfo.getDshnrrsn())){
								throw new BusinessException(AccebillMessageEnum.COMMON_MESSAGE_CONFIRM_SELECTDSHNRRSN);
							}
						}else {
							if(StringUtil.isEmpty(reqInfo.getDshnrcd())&&(abr.getRecAcctsvcr().startsWith("309")
																		||AccebillConstant.BILL_SIGNERTYPE_020.equals(abr.getMsgtype())
																		||AccebillConstant.BILL_SIGNERTYPE_021.equals(abr.getMsgtype()))){
								throw new BusinessException(AccebillMessageEnum.COMMON_MESSAGE_CONFIRM_SELECTDSHNR);
							}
							if("DC09".equals(reqInfo.getDshnrcd()) && StringUtil.isBlank_new(reqInfo.getDshnrrsn())){
								throw new BusinessException(AccebillMessageEnum.COMMON_MESSAGE_CONFIRM_SELECTDSHNRRSN);
							}
						}
					}
				}
                //拒签理由校验 end
                if (billReceivebizList != null && !billReceivebizList.isEmpty()) {
                    List<AbAccrecords> accrecordslist = new ArrayList<AbAccrecords>();
                    batchSaveVoucherInfo(billReceivebizList, reqInfo, userVo);
                    AbVoucherinfoVO voucherinfoVO = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(reqInfo.getOrigNo(), reqInfo.getSbbllRngStrtSn(), reqInfo.getSbbllRngEndSn());
                    if (voucherinfoVO != null && StringUtil.isNotEmpty(voucherinfoVO.getOrigNo())) {
                        abVoucherinfoService.updatePayerCdtByOrigNo(voucherinfoVO.getOrigNo(), reqInfo.getPayerCdtRatgDuedt(),
                                reqInfo.getPayerCdtRatgGgcy(), reqInfo.getPayerCdtRatgs(), voucherinfoVO.getSbbllRngStrtSn(), voucherinfoVO.getSbbllRngEndSn());
                    }
                    int addAccrecordsSize = billReceivebizToAccrecords(accrecordslist, billReceivebizList, reqInfo, userVo);
                    int updateBillReceivebizSize = batchUpdateAbBillReceivebiz(ids, reqInfo, userVo);
                    if (billReceivebizList.size() != addAccrecordsSize || addAccrecordsSize != updateBillReceivebizSize) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                    String[] msgTypes = null;
                    if (StringUtils.isNotEmpty(reqInfo.getMsgtype())) {
                        msgTypes = reqInfo.getMsgtype().split(",");
                    }
                    for (int i = 0; i < addAccrecordsSize; i++) {
                        AbAccrecords accrecInfo = accrecordslist.get(i);
                        abBillNotificationService.completeMsg(accrecInfo, userVo);
                        // 再次提交需要把打回从表的BackBriefId为空
                        abVoucherinfoViceService.updateBackBriefIdIsNull(accrecInfo.getVvid());
                        String msg = "进行【" + reqInfo.getSgnupmk() + reqInfo.getSgnupmkName() + "】，签收完成！";
                        AbBillReceivebiz recInfo = billReceivebizList.get(i);
                        AbBillReceivebizVO variables = new AbBillReceivebizVO();
                        BeanUtils.copyNotNullProperties(recInfo.getVouInfo(), variables);
                        BeanUtils.copyNotNullProperties(recInfo, variables);
                        BeanUtils.copyNotNullProperties(accrecInfo, variables);
                        // 资金计划
                        if (msgTypes != null && ((AccebillConstant.AB_BUSTYPE_RECBILL).equals(msgTypes[i])
                                || (AccebillConstant.AB_BUSTYPE_RECITE).equals(msgTypes[i]))
                                && !CollectionUtils.isEmpty(reqInfo.getBudgetList())
                                && reqInfo.getBudgetList().get(0) != null) {
                            if (msgTypes.length > 1) {
                                reqInfo.getBudgetList().get(0).setProcessMoney(recInfo.getBillMoney());
                            }
                            batchWriteBudget(reqInfo, recInfo, userVo);
                        }
                        String userId = userVo.getId();
                        if (reqInfo.isTask()) {
                            abLogService.saveAbLog(accrecInfo.getVvid(), accrecInfo.getId(), accrecInfo.getBriefId(),
                                    AccebillConstant.ABLOG_LOGTYPE_TASK, msg, userVo);
                        } else {
                            abLogService.saveAbLog(accrecInfo.getVvid(), accrecInfo.getId(), accrecInfo.getBriefId(),
                                    AccebillConstant.ABLOG_LOGTYPE_SIGN, msg, userVo);
                        }
                        getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_SIGN, accrecInfo.getId(), variables,
                                recInfo.getRecCorpId(), userId);
                    }
                    //工作流不通，先直接过，到审批通过步骤
//					approveOver(id,true, "跳过审批，自定义同意", userVo);
//					 /**
//					 * 默认审批通过
//					 */
//					 batchApprove(accrecordslist, "审批通过！", true);
                }
            }
        } catch (BusinessException e) {
            if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISBILLNO);
            } else {
                throw e;
            }
        }
    }

    /**
     * 审批完成处理
     *
     * @param buId
     * @param isPassOK
     * @param appMsg
     */
    public void approveOver(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
        if (StringUtils.isEmpty(buId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbAccrecords info = abAccrecordsService.get(buId, true);
        if (info == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        Integer status = isPassOK ? AccebillConstant.AB_BILL_BUS_STATUS95 : AccebillConstant.AB_BILL_BUS_STATUS_2;
        int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
        // 签收业务不需要做票据主表状态处理
        // int upBoxStatusNum =
        // abVoucherinfoService.updateBoxStatus(info.getVid(),
        // AccebillConstant.AB_BOX_STATUS_Y);
        if (appNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        String msg = appMsg;
        String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
        if (!isPassOK) {
            AbBillReceivebizVO billReceivebizVo = new AbBillReceivebizVO();
            billReceivebizVo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE);
            String[] ids = {info.getRecid()};
            batchUpdateAbBillReceivebiz(ids, billReceivebizVo, userVo);
            abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
        }
        //浦发银行发送电票需要上传最新的申请日期
        if (isPassOK && "9020".equals(info.getBifCode()) && AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSign())) {
            abAccrecordsService.updateApplyDateById(info.getId(), userVo);
        }
        abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
        // 资金计划处理
        Integer budgetStatus = isPassOK ? BudgetConstant.STATUE_CHECK : BudgetConstant.STATUE_BACK;
        abFbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_REC,
                BudgetConstant.PROCESS_SYSNAME_AB, info.getVvid(), budgetStatus, userVo);
    }

    /**
     * ERP票据签收操作
     *
     * @param reqInfo
     * @return
     */
    public void billSignOpErp(AbBillReceivebizVO reqInfo, ICurrentUser userVo) {
        reqInfo.setReplyDt(new Date());
        String id = reqInfo.getId();
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        } else {
            String[] ids = StringUtils.split(id, ",");
            reqInfo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK);
            //查询待签收数据
            List<AbBillReceivebiz> billReceivebizList = abBillReceivebizDao.listAbBillReceivebizByIds(ids, reqInfo);
            if (ids.length != billReceivebizList.size()) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            if (billReceivebizList != null && !billReceivebizList.isEmpty()) {
                List<AbAccrecords> accrecordslist = new ArrayList<AbAccrecords>();
                //保存票据主表和从表数据
                batchSaveVoucherInfo(billReceivebizList, reqInfo, userVo);
                //保存流水
                int addAccrecordsSize = billReceivebizToAccrecords(accrecordslist, billReceivebizList, reqInfo,
                        userVo);
                //修改签收表数据
                int updateBillReceivebizSize = batchUpdateAbBillReceivebiz(ids, reqInfo, userVo);
                if (billReceivebizList.size() != addAccrecordsSize
                        || addAccrecordsSize != updateBillReceivebizSize) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                for (int i = 0; i < addAccrecordsSize; i++) {
                    AbAccrecords accrecInfo = accrecordslist.get(i);
                    //修改流水表状态为通过有效
                    abAccrecordsService.approve(accrecInfo.getId(), AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
                    abBillNotificationService.sendMsg(accrecInfo.getId());
                    // 再次提交需要把打回从表的BackBriefId为空
                    abVoucherinfoViceService.updateBackBriefIdIsNull(accrecInfo.getVvid());
                    String msg = "进行【" + reqInfo.getSgnupmk() + reqInfo.getSgnupmkName() + "】，签收完成！";
                    abLogService.saveAbLog(accrecInfo.getVvid(), accrecInfo.getId(), accrecInfo.getBriefId(),
                            AccebillConstant.ABLOG_LOGTYPE_SIGN, msg, userVo);
                }
            }
        }
    }

    /**
     * 票据池电票签收操作
     *
     * @param reqInfo 签收信息
     * @return 签收信息
     */
    public AbBillReceivebizVO billPoolSign(AbBillReceivebizVO reqInfo, ICurrentUser userVo) {
        try {
            reqInfo.setReplyDt(new Date());
            String id = reqInfo.getId();
            if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
            } else {
                String[] ids = StringUtils.split(id, ",");
                reqInfo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK);
                List<AbBillReceivebiz> billReceivebizList = abBillReceivebizDao.listAbBillReceivebizByIds(ids, reqInfo);
                if (ids.length != billReceivebizList.size()) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                if (billReceivebizList != null && !billReceivebizList.isEmpty()) {
                    //List<AbAccrecords> accrecordslist = new ArrayList<AbAccrecords>();
                    batchSaveVoucherInfo(billReceivebizList, reqInfo, userVo);
					/*int addAccrecordsSize = billReceivebizToAccrecords(accrecordslist, billReceivebizList, reqInfo,
							userVo);*/
                    int updateBillReceivebizSize = batchUpdateAbBillReceivebiz(ids, reqInfo, userVo);
                    if (billReceivebizList.size() != updateBillReceivebizSize) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                    for (int i = 0; i < billReceivebizList.size(); i++) {
                        AbBillReceivebiz recInfo = billReceivebizList.get(i);
						/*AbAccrecords accrecInfo = accrecordslist.get(i);
						abBillNotificationService.completeMsg(accrecInfo,userVo);
						// 再次提交需要把打回从表的BackBriefId为空
						abVoucherinfoViceService.updateBackBriefIdIsNull(accrecInfo.getVvid());*/
                        String msg = "进行【" + reqInfo.getSgnupmk() + reqInfo.getSgnupmkName() + "】，签收完成！";
                        reqInfo.setVvid(recInfo.getVvid());
                        reqInfo.setOrigNo(recInfo.getOrigNo());
                        reqInfo.setCorpId(recInfo.getRecCorpId());
                        reqInfo.setApplyCorpId(recInfo.getRecCorpId());
                        reqInfo.setMsgtype(recInfo.getMsgtype());
						/*	abLogService.saveAbLog(accrecInfo.getVvid(), accrecInfo.getId(), accrecInfo.getBriefId(),
								AccebillConstant.ABLOG_LOGTYPE_SIGN, msg, userVo);*/
                        // 资金计划
						/*if ((AccebillConstant.AB_BUSTYPE_RECBILL).equals(recInfo.getMsgtype())
								|| (AccebillConstant.AB_BUSTYPE_RECITE).equals(recInfo.getMsgtype())) {
							batchWriteBudget(reqInfo, recInfo, userVo);
						}*/
					/*	 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_SIGN, accrecInfo.getId(), variables,
								recInfo.getRecCorpId(),userVo.getId());*/
                    }
                }
            }
        } catch (BusinessException e) {
            if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISBILLNO);
            } else {
                throw e;
            }
        }
        return reqInfo;
    }

    /**
     * 票据池-电票签收操作新增流水
     *
     * @param reqInfo 签收信息
     * @param valid   有效标识
     * @return 票据从表id
     */
    public AbBillReceivebizVO billPoolSignAccrecords(AbBillReceivebizVO reqInfo, String valid, ICurrentUser userVo) {
        try {
            String id = reqInfo.getId();
            if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
            } else {
                String[] ids = StringUtils.split(id, ",");
                List<AbBillReceivebiz> billReceivebizList = abBillReceivebizDao.listAbBillReceivebizByIds(ids);
                if (ids.length != billReceivebizList.size()) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                if (billReceivebizList != null && !billReceivebizList.isEmpty()) {
                    List<AbAccrecords> accrecordslist = new ArrayList<AbAccrecords>();
                    int addAccrecordsSize = billReceivebizToAccrecordsByPoolSign(accrecordslist, billReceivebizList, reqInfo,
                            userVo, valid);
                    if (billReceivebizList.size() != addAccrecordsSize) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                    if (AccebillConstant.AB_BILL_VALID_SIGN_1.equals(valid)) {
                        for (int i = 0; i < billReceivebizList.size(); i++) {
                            AbAccrecords accrecInfo = accrecordslist.get(i);
                            abBillNotificationService.completeMsg(accrecInfo, userVo);
                            abLogService.saveAbLog(accrecInfo.getVvid(), accrecInfo.getId(), accrecInfo.getBriefId(),
                                    AccebillConstant.ABLOG_LOGTYPE_SIGN, AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
                        }
                    }
                }
            }
        } catch (BusinessException e) {
            if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISBILLNO);
            } else {
                throw e;
            }
        }
        return reqInfo;
    }

    /**
     * 批量新增票据信息
     *
     * @param billReceivebizList
     * @return
     */
    public void batchSaveVoucherInfo(List<AbBillReceivebiz> billReceivebizList, AbBillReceivebizVO billReceivebizVo,
                                     ICurrentUser userVo) {
        Map<String, InputBankInfoDTO> bankMap = new HashMap<String, InputBankInfoDTO>();
        for (AbBillReceivebiz info : billReceivebizList) {
            setBillReceivebizBankName(info, bankMap);
            AbVoucherinfo vouInfo = billReceivebizToVoucherInfo(info, billReceivebizVo);
            if (AccebillConstant.BILL_SIGNERTYPE_023.equals(info.getMsgtype())) {
                abVoucherinfoService.saveOrUpdateAbVoucherinfoAnt(vouInfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, userVo);
            } else {
                abVoucherinfoService.saveOrUpdateAbVoucherinfo(vouInfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, userVo);
            }
            AbVoucherinfoVice viceInfo = billReceivebizToVouicherViceInfo(vouInfo, info);
            abVoucherinfoViceService.saveVoucherinfoVice(viceInfo, userVo);
            info.setVvid(viceInfo.getId());
            info.setVid(vouInfo.getId());
            info.setVouInfo(vouInfo);
            info.setVouViceInfo(viceInfo);
        }
    }

    /**
     * 批量新增票据信息
     *
     * @param billReceivebizList
     * @return
     */
    public void batchSaveVoucherInfoAnt(List<AbBillReceivebiz> billReceivebizList, AbBillReceivebizVO billReceivebizVo,
                                        ICurrentUser userVo) {
        Map<String, InputBankInfoDTO> bankMap = new HashMap<String, InputBankInfoDTO>();
        for (AbBillReceivebiz info : billReceivebizList) {
            setBillReceivebizBankName(info, bankMap);
            AbVoucherinfo vouInfo = billReceivebizToVoucherInfo(info, billReceivebizVo);
            abVoucherinfoService.saveOrUpdateAbVoucherinfoAnt(vouInfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, userVo);
            AbVoucherinfoVice viceInfo = billReceivebizToVouicherViceInfo(vouInfo, info);
            abVoucherinfoViceService.saveVoucherinfoVice(viceInfo, userVo);
            info.setVvid(viceInfo.getId());
            info.setVid(vouInfo.getId());
            info.setVouInfo(vouInfo);
            info.setVouViceInfo(viceInfo);
        }
    }

    /**
     * 设置签收表根据银行行号设置行名信息
     *
     * @param info    签收表信息
     * @param bankMap 银行信息缓存map
     */
    @Transactional(readOnly = true)
    public void setBillReceivebizBankName(AbBillReceivebiz info, Map<String, InputBankInfoDTO> bankMap) {
        if (bankMap == null) {
            bankMap = new HashMap<String, InputBankInfoDTO>();
        }
        if (StringUtils.isNotEmpty(info.getPayerbankno())
                && StringUtils.isEmpty(info.getPayerbankName())) {
            if (bankMap.containsKey(info.getPayerbankno())) {
                info.setPayerbankName(bankMap.get(info.getPayerbankno()).getBankName());
            } else {
                InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankCode(info.getPayerbankno());
                if (bankInfo != null) {
                    bankMap.put(info.getPayerbankno(), bankInfo);
                    info.setPayerbankName(bankMap.get(info.getPayerbankno()).getBankName());
                }
            }
        }
        if (StringUtils.isEmpty(info.getPayerbankno())
                && StringUtils.isNotEmpty(info.getPayerbankName())) {
            if (bankMap.containsKey(info.getPayerbankName())) {
                info.setPayerbankno(bankMap.get(info.getPayerbankName()).getBankCode());
            } else {
                InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankName(info.getPayerbankName());
                if (bankInfo != null) {
                    bankMap.put(info.getPayerbankName(), bankInfo);
                    info.setPayerbankno(bankMap.get(info.getPayerbankName()).getBankCode());
                }
            }
        }
        if (StringUtils.isNotEmpty(info.getPayeebankno())
                && StringUtils.isEmpty(info.getPayeebankName())) {
            if (bankMap.containsKey(info.getPayeebankno())) {
                info.setPayeebankName(bankMap.get(info.getPayeebankno()).getBankName());
            } else {
                InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankCode(info.getPayeebankno());
                if (bankInfo != null) {
                    bankMap.put(info.getPayeebankno(), bankInfo);
                    info.setPayeebankName(bankMap.get(info.getPayeebankno()).getBankName());
                }
            }
        }
        if (StringUtils.isNotEmpty(info.getAcceptorbankno())
                && StringUtils.isEmpty(info.getAcceptorbankName())) {
            if (bankMap.containsKey(info.getAcceptorbankno())) {
                info.setAcceptorbankName(bankMap.get(info.getAcceptorbankno()).getBankName());
            } else {
                InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankCode(info.getAcceptorbankno());
                if (bankInfo != null) {
                    bankMap.put(info.getAcceptorbankno(), bankInfo);
                    info.setAcceptorbankName(bankMap.get(info.getAcceptorbankno()).getBankName());
                }
            }
        }
    }

    /**
     * 根据签收流水数据数据设置票据主信息
     *
     * @param info
     */
    public AbVoucherinfo billReceivebizToVoucherInfo(AbBillReceivebiz info, AbBillReceivebizVO abBillReceivebizVO) {
        AbVoucherinfo vouInfo = new AbVoucherinfo();
        BeanUtils.copyNotNullProperties(info, vouInfo, "id");
        vouInfo.setId(null);
        vouInfo.setPayerBankName(info.getPayerbankName());
        vouInfo.setPayeeBankName(info.getPayeebankName());
        vouInfo.setAcceptorBankName(info.getAcceptorbankName());
        vouInfo.setAcontractNo(info.getTxlctrctnb());
        vouInfo.setAinvcnb(info.getInvcnb());
        vouInfo.setBanendrsmtmk(info.getBankendrsmtmk());
        vouInfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
        vouInfo.setBillFlag(Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));
        vouInfo.setSourceType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        if (AccebillConstant.AB_MSGTYPE_ACCEPT.equals(info.getMsgtype())) {
            vouInfo.setPayerCdtRatgs(info.getCdtratgs());
            vouInfo.setPayerCdtRatgGgcy(info.getCdtratgagcy());
            vouInfo.setPayerCdtRatgDuedt(info.getCdtratgduedt());
        } else {
            vouInfo.setPayerCdtRatgs(abBillReceivebizVO.getPayerCdtRatgs());
            vouInfo.setPayerCdtRatgGgcy(abBillReceivebizVO.getPayerCdtRatgGgcy());
            vouInfo.setPayerCdtRatgDuedt(abBillReceivebizVO.getPayerCdtRatgDuedt());
        }
        return vouInfo;
    }

    /**
     * 根据签收流水数据数据设置票据从表信息
     *
     * @param vouInfo
     * @param info
     */
    public AbVoucherinfoVice billReceivebizToVouicherViceInfo(AbVoucherinfo vouInfo, AbBillReceivebiz info) {
        AbVoucherinfoVice viceInfo = new AbVoucherinfoVice();
        viceInfo.setVid(vouInfo.getId());
        viceInfo.setCorpId(info.getRecCorpId());
        viceInfo.setApplyCorpId(info.getRecCorpId());
        viceInfo.setBankAcc(info.getRecAcctId());
        viceInfo.setBillSource(AccebillConstant.AB_BILL_SOURCE_SIGN);
        viceInfo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
        viceInfo.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        viceInfo.setEnterStock(AccebillConstant.AB_ENTER_STOCK_CORP);
        //判断发起为是否为内部单位，是则设置内部标识
        CorpDTO corpDTO = abCorpService.getCorpIdByName(info.getReqNm());
        if (corpDTO != null) {
            viceInfo.setInsideSing(Integer.valueOf(AccebillConstant.AB_INSIDE_SING_Y));
        }
        return viceInfo;
    }

    /**
     * 签收表转流水表业务处理处理
     *
     * @param billReceivebizList
     * @return
     */
    public int billReceivebizToAccrecords(List<AbAccrecords> accrecordslist, List<AbBillReceivebiz> billReceivebizList,
                                          AbBillReceivebizVO billReceivebizVo, ICurrentUser userVo) {
        for (AbBillReceivebiz info : billReceivebizList) {
            AbAccrecords accrecords = new AbAccrecords();
            accrecords.setRecid(info.getId());
            accrecords.setVid(info.getVid());
            accrecords.setVvid(info.getVvid());
            if (info.getMsgtype().equals(AccebillConstant.BRIEF_STATUS_017)) {
                AbAccrecords accrecords1 = abAccrecordsService.getBillByvid(info.getVid(), info.getMsgtype());
                accrecords.setBailType(accrecords1.getBailType());
            }
            accrecords.setValidSign("0");
            accrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
            accrecords.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
            accrecords.setBriefId("9" + info.getMsgtype());
            accrecords.setSttlmmk(info.getSttlmmk());
            accrecords.setBailType(accrecords.getBailType());
            // accrecords.setApplyDate(info.getReplyDt());
			accrecords.setApplyDate(new Date());
			accrecords.setBusinessDate(accrecords.getApplyDate());
//			accrecords.setBusinessDate(DateUtils.getDateYMD());
			info.setReplyDt(accrecords.getBusinessDate());
            accrecords.setSignUpFlag(billReceivebizVo.getSgnupmk());
            //新旧票拒签理由代码
            if ("0".equals(info.getNewStmIssuofpprInd())) {
                accrecords.setRsn(billReceivebizVo.getDshnrcd());
            } else {
                accrecords.setRsn(billReceivebizVo.getNewDshnrcd());
                billReceivebizVo.setDshnrcd(billReceivebizVo.getNewDshnrcd());
            }
            accrecords.setRmk(billReceivebizVo.getDshnrrsn());
            accrecords.setCdtRatgs(billReceivebizVo.getCdtratgs());
            accrecords.setCdtRatgGgcy(billReceivebizVo.getCdtratgagcy());
            accrecords.setCdtRatgDuedt(billReceivebizVo.getCdtratgduedt());
//			accrecords.setReqRole(info.getReqRole());
//			accrecords.setReqNm(info.getReqNm());
//			accrecords.setReqCmonid(info.getReqCmonid());
//			accrecords.setReqAcctId(info.getReqAcctId());
//			accrecords.setReqAcctsvcr(info.getReqAcctsvcr());
//			accrecords.setReqAcctsvcrAg(info.getReqAcctsvcrAg());
//			
//			accrecords.setRecRole(info.getRecRole());
//			accrecords.setRecNm(info.getRecNm());
//			accrecords.setRecCmonid(info.getRecCmonid());
//			accrecords.setRecAcctId(info.getRecAcctId());
//			accrecords.setRecAcctsvcr(info.getRecAcctsvcr());
//			accrecords.setRecAcctsvcrAg(info.getRecAcctsvcrAg());
            accrecords.setRecRole(info.getReqRole());
            accrecords.setRecNm(info.getReqNm());
            accrecords.setRecCmonid(info.getReqCmonid());
            accrecords.setRecAcctId(info.getReqAcctId());
            accrecords.setRecAcctsvcr(info.getReqAcctsvcr());
            accrecords.setRecAcctsvcrAg(info.getReqAcctsvcrAg());
            accrecords.setReqRole(info.getRecRole());
            accrecords.setReqNm(info.getRecNm());
            accrecords.setReqCmonid(info.getRecCmonid());
            accrecords.setReqAcctId(info.getRecAcctId());
            accrecords.setReqAcctsvcr(info.getRecAcctsvcr());
            accrecords.setReqAcctsvcrAg(info.getRecAcctsvcrAg());
            accrecords.setBillFlag(Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));
            accrecords.setBillId(info.getBillId());
            accrecords.setBankendrsmtmk(info.getBankendrsmtmk());
            accrecords.setReqSocNo(info.getBlRcvUnnIdNo());
            accrecords.setRecSocNo(info.getBlReqUnnIdNo());
            // 资金计划科目代码
            if ((AccebillConstant.AB_SGNUPMK_Y).equals(billReceivebizVo.getSgnupmk())
                    && StringUtils.isNotEmpty(billReceivebizVo.getItemCodeStr())) {
                accrecords.setBudgetCode(billReceivebizVo.getItemCodeStr());
            }
            if (billReceivebizVo.isTask()) {
                accrecords.setIsAutosign(AccebillConstant.AB_BILL_ISAUTOSIGN_Y);
            } else {
                accrecords.setIsAutosign(AccebillConstant.AB_BILL_ISAUTOSIGN_N);
            }
            accrecordslist.add(accrecords);
        }
        return abAccrecordsService.batchSaveAccrecords(accrecordslist, userVo);
    }

	/**
	 * 票据池-电票签收-签收表转流水表业务处理
	 *
	 * @param billReceivebizList
	 * @return
	 */
	public int billReceivebizToAccrecordsByPoolSign(List<AbAccrecords> accrecordslist, List<AbBillReceivebiz> billReceivebizList,
													AbBillReceivebizVO billReceivebizVo, ICurrentUser userVo,String valid) {
		for (AbBillReceivebiz info : billReceivebizList) {
			//根据传的从表id查询主表id
			AbVoucherinfoVice vice = abVoucherinfoViceService.get(billReceivebizVo.getVvid());
			if(vice == null){
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
			}
			AbAccrecords accrecords = new AbAccrecords();
			accrecords.setRecid(info.getId());
			accrecords.setVid(vice.getVid());
			accrecords.setVvid(vice.getId());
			accrecords.setValidSign(valid);
			accrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
			accrecords.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
			accrecords.setBriefId("9" + info.getMsgtype());
			accrecords.setApplyDate(new Date());
			accrecords.setBusinessDate(accrecords.getApplyDate());
//			accrecords.setBusinessDate(DateUtils.getDateYMD());
			info.setReplyDt(accrecords.getBusinessDate());
			accrecords.setSignUpFlag(billReceivebizVo.getSgnupmk());
			accrecords.setRmk(billReceivebizVo.getDshnrrsn());
			accrecords.setCdtRatgs(billReceivebizVo.getCdtratgs());
			accrecords.setCdtRatgGgcy(billReceivebizVo.getCdtratgagcy());
			accrecords.setCdtRatgDuedt(billReceivebizVo.getCdtratgduedt());
			accrecords.setRecRole(info.getReqRole());
			accrecords.setRecNm(info.getReqNm());
			accrecords.setRecCmonid(info.getReqCmonid());
			accrecords.setRecAcctId(info.getReqAcctId());
			accrecords.setRecAcctsvcr(info.getReqAcctsvcr());
			accrecords.setRecAcctsvcrAg(info.getReqAcctsvcrAg());

			accrecords.setReqRole(info.getRecRole());
			accrecords.setReqNm(info.getRecNm());
			accrecords.setReqCmonid(info.getRecCmonid());
			accrecords.setReqAcctId(info.getRecAcctId());
			accrecords.setReqAcctsvcr(info.getRecAcctsvcr());
			accrecords.setReqAcctsvcrAg(info.getRecAcctsvcrAg());

			accrecords.setBillFlag(Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));
			accrecords.setBillId(info.getBillId());
			accrecords.setBankendrsmtmk(info.getBankendrsmtmk());
			accrecords.setPdId(billReceivebizVo.getPdId());
			// 资金计划科目代码
			if ((AccebillConstant.AB_SGNUPMK_Y).equals(billReceivebizVo.getSgnupmk())
					&& StringUtils.isNotEmpty(billReceivebizVo.getItemCodeStr())) {
				accrecords.setBudgetCode(billReceivebizVo.getItemCodeStr());
			}
			accrecordslist.add(accrecords);
		}
		return abAccrecordsService.batchSaveAccrecords(accrecordslist, userVo);
	}

    /**
     * 根据要求修改票据签收表信息
     *
     * @param billReceivebizVo
     * @param userVo
     * @return 返回成功成功几笔数据
     */
    public int updateAbBillReceivebiz(AbBillReceivebizVO billReceivebizVo, ICurrentUser userVo) {
        String[] ids = {billReceivebizVo.getId()};
        return batchUpdateAbBillReceivebiz(ids, billReceivebizVo, userVo);
    }

    /**
     * 根据要求修改票据签收表信息
     *
     * @param ids              签收表ID
     * @param billReceivebizVo
     * @return 返回成功成功几笔数据
     */
    public int batchUpdateAbBillReceivebiz(String[] ids, AbBillReceivebizVO billReceivebizVo, ICurrentUser userVo) {
        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        if (billReceivebizVo.getUpdateDate() == null) {
            billReceivebizVo.setUpdateDate(new Date());
        }
        if (userVo != null) {
            if (StringUtils.isEmpty(billReceivebizVo.getUpdateBy())) {
                billReceivebizVo.setUpdateBy(userVo.getLoginName());
            }
            if (StringUtils.isEmpty(billReceivebizVo.getUpdateName())) {
                billReceivebizVo.setUpdateName(userVo.getUsername());
            }
        }
        return abBillReceivebizDao.batchUpdateAbBillReceivebiz(ids, billReceivebizVo);
    }

    /**
     * 根据id批量修改状态
     *
     * @param ids       id数组
     * @param oldStatus 原状态
     * @param newStatus 状态
     * @return 返回成功成功几笔数据
     */
    public int batchUpdateStatus(String[] ids, Integer oldStatus, Integer newStatus) {
        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        return abBillReceivebizDao.batchUpdateStatus(ids, oldStatus, newStatus);
    }

    @Transactional(readOnly = true)
    public AbBillReceivebiz getAbBillReceivebizByMsgId(String msgID) {
        return abBillReceivebizDao.getAbBillReceivebizByMsgId(msgID);
    }

    /**
     * 审批回调方法
     */
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
        this.approveOver(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
    }

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
        WorkFlowBusiness workFlowBusiness = abWorkFlowService.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_SIGN,
                dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput<AbBillReceivebizVO> dp = abBillReceivebizDao.dataTableApprove(dataTablesInput, businessList);
        List<AbBillReceivebizVO> list = dp.getData();
        if (!EmptyUtils.isEmptyList(list)) {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_ROLE", "AB_BILL_SGNUPMK", "AB_BILL_DSHNRCD"
                    , "AB_BILL_RECEIVEBIZ_STATUS", "AB_BILL_SIGNERTYPE", "AB_BILL_TYPE", "AB_BILL_UCONDLPRMSMRK", "AB_STTLMMK", "AB_BANEM", "AB_RCRS_TP"
                    , "AB_RCRSRSNCD", "ACCEBILL_ABRECEIVE_INSIDESING", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            for (AbBillReceivebizVO billReceivebizVo : list) {
                injectValues(billReceivebizVo, listDictMap);
                if(billReceivebizVo.getCorpCode()==null){
                    CorpDTO corpDTO=corpService.get(billReceivebizVo.getRecCorpId());
                    billReceivebizVo.setCorpCode(corpDTO.getCode());
                    billReceivebizVo.setCorpName(corpDTO.getName());
                }
                if(StringUtils.isNotBlank(billReceivebizVo.getBusCorpId())){
                    CorpDTO corpDTO=corpService.get(billReceivebizVo.getBusCorpId());
                    billReceivebizVo.setBusCorpName(corpDTO.getName());
                }
            }
        }
        return workFlowBusiness.merge(dp);
    }

    /**
     * 保存预算信息
     *
     * @param reqInfo
     * @param recInfo
     * @param userVo
     */
    public void batchWriteBudget(AbBillReceivebizVO reqInfo, AbBillReceivebiz recInfo, ICurrentUser userVo) {
        // 删除资金计划
        abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_REC,
                BudgetConstant.PROCESS_SYSNAME_AB, recInfo.getVvid());
        if ((AccebillConstant.AB_SGNUPMK_Y).equals(reqInfo.getSgnupmk())) {
//			// 获取账户信息
//			BtBankAccDTO bankAccInfo = abAccrecordsService.getBtBankAccDTO(recInfo.getRecAcctId());
            // 是否挂资金计划
            boolean isBudget = abVoucherinfoViceService.isBudget(recInfo.getRecCorpId(),
                    AccebillConstant.BRIEF_STATUS_6001);
            List<FbsBudgetProcessDTO> budgetlist = reqInfo.getBudgetList();
            if (isBudget) {
                if (budgetlist != null && !budgetlist.isEmpty()) {
                    for (FbsBudgetProcessDTO budget : budgetlist) {
                        //TODO 添加内转判断标识 innerSign  1-内转 0-非内转
                        if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(reqInfo.getInsideSing()))) {
                            budget.setInnerSign("1");
                        } else {
                            budget.setInnerSign("0");
                        }
                        budget.getItemCode();
                        budget.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:0,录入,11在途,95占用
                        budget.setCorpId(recInfo.getRecCorpId());// 单位id
                        budget.setProcessDate(recInfo.getReplyDt());// 执行日期
                        if (budget.getProcessDate() == null) {
                            budget.setProcessDate(DateUtils.getDateYMD());// 执行日期
                        }
                        budget.setBillNo(recInfo.getOrigNo());//票据号码
                        budget.setBillId(recInfo.getVvid());// 从表id
                        budget.setCurId(abVoucherinfoViceService.getCurIdByCNY());// 币别取人民币
//						if (bankAccInfo.getCurList() != null && !bankAccInfo.getCurList().isEmpty()) {
//							budget.setCurId(bankAccInfo.getCurList().get(0));// 币别取人民币
//						}
						budget.setBusinessName(BudgetConstant.BUSINESS_NAME_REC);// 执行业务名称
						budget.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);// 执行系统名称
						budget.setCreateBy(userVo.getLoginName());// 创建人登录名称
						budget.setCreateDate(new Date());// 创建日期
						//本方账号-业务接收人账号
						budget.setPayerAcc(recInfo.getRecAcctId());
						//对方账号-业务申请人账号
						budget.setPayeeAcc(recInfo.getReqAcctId());
						//对方账户名-业务申请人名称
						budget.setPayeeAccName(recInfo.getReqNm());
						//对方开户行-业务申请人开户行名称
						if (!StringUtil.isBlank(recInfo.getReqAcctsvcr())) {
							InputBankInfoDTO bankInfoDTO = abInputBankInfoService.getByBankCode(recInfo.getReqAcctsvcr());
							if (bankInfoDTO != null) {
								budget.setPayeeBank(bankInfoDTO.getBankName());
							}
						}
						//用途-备注
						budget.setPurpose(recInfo.getRmk());
					}
					abFbsBudgetProcessService.writeBudgetProcess(budgetlist,userVo);
				}
			}
		}
	}

    @Override
    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        return this.getAbBillReceivebizVOById(aid);
    }

    /**
     * 判断单位是否为同一网点下
     *
     * @param corpId1
     * @param corpId2
     * @return
     */
    public boolean isTheSameNetByCorpId(String corpId1, String corpId2) {
        return abCorpService.isTheSameNetByCorpId(corpId1, corpId2);
    }

    /**
     * 批量已撤回操作
     *
     * @param reqInfo
     * @param userVo
     * @return
     */
    public void billRecall(AbBillReceivebizVO reqInfo, ICurrentUser userVo) {
        String id = reqInfo.getId();
        String[] ids = StringUtils.split(id, ",");
        reqInfo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_RECALL);
        int updateBillReceivebizSize = batchUpdateAbBillReceivebiz(ids, reqInfo, userVo);
        if (updateBillReceivebizSize != ids.length) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        //去除消息提醒代办
        for (String recId : ids) {
            if (StringUtils.isNotEmpty(id)) {
                AbAccrecords acInfo = new AbAccrecords();
                acInfo.setRecid(recId);
                acInfo.setBriefId(AccebillConstant.BRIEF_STATUS_9003);
                abBillNotificationService.completeMsg(acInfo, userVo);
            }
        }
    }

    /**
     * 拒签后释放电票占用资源(保证金，授信，担保，资金计划)
     */
    @Override
    @XxlJob("AbBillReceivebizService")
    public void run(String arguments) {
        arguments = XxlJobHelper.getJobParam();
        logger.info("定时任务接收参数：【{}】", arguments);
        try {
            if (AccebillConstant.AB_TIMEDTASK_AUTO_SIGN_STRATEGY.equals(arguments)) {
//			autoSignStrategy自动策略签收
                autoSignStrategyTimedTask();
            } else if (AccebillConstant.AB_TIMEDTASK_AUTO_SIGN_ACCEPT.equals(arguments)) {
//			autoSignAccept 自动商票提示承兑出票人和承兑人一致自动签收
                autoSignAcceptTimedTask();
            } else {
                this.logger.info("---启动定时任务拒签后查询挂接数据，开始执行定时任务---");
                //获取指令拒签(提示收票签收、背书申请、背书申请签收)成功需要释放的数据
                List<AbAccrecords> reSelfList = abBillReceivebizDao.queryRefuseAutoData(AccebillConstant.AB_REFUSE_FBS);
                if (null == reSelfList || reSelfList.isEmpty()) {
                    this.logger.info("---启动定时任务拒签后查询挂接数据，数据不存在---");
                } else {
                    //执行拒绝签收操作 释放资金计划的,
                    this.logger.info("---执行拒绝签收操作 释放资金计划的数据---笔数【" + reSelfList.size() + "】");
                    for (AbAccrecords info : reSelfList) {
                        String briefId = null;
                        if (info.getBriefId().equals("010") || info.getBriefId().equals("9010")) {
                            briefId = BudgetConstant.BUSINESS_NAME_ENDORSE;
                        } else if (info.getBriefId().equals("9003")) {
                            briefId = BudgetConstant.BUSINESS_NAME_REC;
                        } else {
                            briefId = BudgetConstant.BUSINESS_NAME_EMIT;
                        }
                        abFbsBudgetProcessService.releaseBudgetProcessForBill(briefId,
                                BudgetConstant.PROCESS_SYSNAME_AB, info.getVvid());
                    }
                }
                //获取商票贴现拒绝签收释放授信数据
                List<AbAccrecords> ratList = abBillReceivebizDao.queryRefuseAutoData(AccebillConstant.AB_REFUSE_RAT);
                if (null == ratList || ratList.isEmpty()) {
                    this.logger.info("---执行商票贴现释放操作任务，数据不存在---");
                } else {
                    ICurrentUser userVo = abUserService.getScheduler();
                    //执行拒绝签收操作,释放商票贴现授信的数据
                    this.logger.info("---执行拒绝签收操作,释放商票贴现授信---笔数【" + ratList.size() + "】");
                    for (AbAccrecords accRecords : ratList) {
                        abRatUrationService.opRatUration(accRecords, AccebillConstant.BILL_OPERATE_REPULSE, userVo);
                    }
                }
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * autoSignAccept 自动商票提示承兑出票人和承兑人一致自动签收
     */
    public void autoSignAcceptTimedTask() {
        this.logger.info("---启动定时任务自动商票提示承兑出票人和承兑人一致自动签收，开始执行定时任务---");
        //获取商票提示承兑待签收数据且出票人和承兑人一致
        List<AbBillReceivebizVO> signVOList = this.abBillReceivebizDao.getAutoSignAcceptData();
        //进行自动签收操作
        AbBillReceivebizVO abBillReceivebizVO = null;
        if (ListUtils.isNullOrEmpty(signVOList)) {
            this.logger.info("---执行自动商票提示承兑出票人和承兑人一致自动签收，无待签收数据---");
        } else {
            ICurrentUser userVo = abUserService.getScheduler();
            this.logger.info("---执行自动商票提示承兑出票人和承兑人一致自动签收---笔数【" + signVOList.size() + "】");
            for (AbBillReceivebizVO signVO : signVOList) {
                abBillReceivebizVO = new AbBillReceivebizVO();
                abBillReceivebizVO.setId(signVO.getId());
                abBillReceivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_Y);
                abBillReceivebizVO.setTask(true);
                this.billSign(abBillReceivebizVO, userVo);
            }
        }
        this.logger.info("---启动定时任务自动商票提示承兑出票人和承兑人一致自动签收，结束执行定时任务---");
    }

    /**
     * autoSignStrategy自动策略签收
     */
    public void autoSignStrategyTimedTask() {
        this.logger.info("---启动定时任务自动策略签收，开始执行定时任务---");
        //查询签收数据参数条件
//		Map<String,Object> params=new HashMap<>();
//		//所有签收策略信息缓存
//		Map<String,Object> signStrategyInfo=new HashMap<>();
//		//进行策略数据初始化
//		abBillSignStrategyService.initAbBillSignStrategy(params,signStrategyInfo);
        Map<String, Object> signStrategyInfo = abBillSignStrategyService.getAllAbBillSignStrategy();
        Map<String, Object> params = (Map<String, Object>) signStrategyInfo.get("params");
        //获取符合策略基本要求的待签收数据
        List<AbBillReceivebizVO> signVOList = this.abBillReceivebizDao.getAutoSignStrategyData(params);
        if (ListUtils.isNullOrEmpty(signVOList)) {
            this.logger.info("---执行自动策略签收，无待签收数据---");
        } else {
//			//进行策略出票人、承兑人、收票人数据初始化
//			abBillSignStrategyService.initAbBillSignStrategyData(signStrategyInfo);
            ICurrentUser userVo = abUserService.getScheduler();
            this.logger.info("---执行自动策略签收---查询到笔数【" + signVOList.size() + "】");
            int i = 0;
            //进行自动签收操作
            AbBillReceivebizVO abBillReceivebizVO = null;
            for (AbBillReceivebizVO signVO : signVOList) {
                if (checkSignInfo(signVO, signStrategyInfo)) {
                    abBillReceivebizVO = new AbBillReceivebizVO();
                    abBillReceivebizVO.setId(signVO.getId());
                    abBillReceivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_Y);
                    abBillReceivebizVO.setTask(true);
                    this.billSign(abBillReceivebizVO, userVo);
                    i++;
                }
            }
            this.logger.info("---执行自动策略签收---自动签收匹配完成笔数【" + i + "】");
        }
        this.logger.info("---启动定时任务自动策略签收，结束执行定时任务---");
    }

    /**
     * 对签收数据进行签收策略匹配 zhengg 20200721
     *
     * @param signVO           签收数据
     * @param signStrategyInfo 签收数据策略功能
     * @return
     */
    public boolean checkSignInfo(AbBillReceivebizVO signVO, Map<String, Object> signStrategyInfo) {
        String key = signVO.getRecCorpId() + "_" + signVO.getBillType() + "_" + signVO.getMsgtype();
        if (signStrategyInfo.containsKey(key)) {
            List<AbBillSignStrategyVO> strategyVOList = (List<AbBillSignStrategyVO>) signStrategyInfo.get(key);
            for (AbBillSignStrategyVO strategyVO : strategyVOList) {
                //对票据金额的匹配
                if (checkSignInfoBillMoney(signVO.getBillMoney(), strategyVO)) {
                    //转让标记匹配(策略为空为真、不为背书签收收票签收为真、如果设置了转让标记则背书签收收票签收必须匹配才为真)
                    if (StringUtil.isEmpty(strategyVO.getBankendrsmtmk())
                            || !(AccebillConstant.AB_MSGTYPE_RECBILL.equals(signVO.getMsgtype())
                            || AccebillConstant.AB_MSGTYPE_RECITE.equals(signVO.getMsgtype()))
                            || strategyVO.getBankendrsmtmk().equals(signVO.getBankendrsmtmk())
                    ) {
                        //匹配出票人
                        boolean payerBoo = checkSignInfoBillName(strategyVO, signVO.getPayer(), AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_01);
                        if (!payerBoo) {
                            return false;
                        }
                        //匹配承兑人
                        boolean acceptorBoo = false;
                        if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(signVO.getBillType()))) {
                            acceptorBoo = checkSignInfoBillName(strategyVO, signVO.getBankTypeId(), AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_02);
                            if (acceptorBoo) {
                                //承兑人黑名单判断
                                acceptorBoo = !blackWhiteService.checkBankAcceptanceByName(signVO.getAcceptor(), signVO.getRecCorpId());
                            }
                        } else {
                            acceptorBoo = checkSignInfoBillName(strategyVO, signVO.getAcceptor(), AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_02);
                        }
                        if (!acceptorBoo) {
                            return false;
                        }
                        if (!acceptorBoo) {
                            return false;
                        }
                        //匹配收票人
                        boolean reqNmBoo = checkSignInfoBillName(strategyVO, signVO.getReqNm(), AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_03);
                        return reqNmBoo;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 通过名称匹配
     *
     * @param strategyVO 策略信息
     * @param name       票据匹配名称
     * @param dataType   数据类型 01出票人，02承兑人，03申请人
     * @return
     */
    public boolean checkSignInfoBillName(AbBillSignStrategyVO strategyVO, String name, String dataType) {
        Map<String, Object> nameMap = null;
        if (AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_01.equals(dataType)) {
            nameMap = strategyVO.getPayerMap();
        } else if (AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_02.equals(dataType)) {
            nameMap = strategyVO.getAcceptorMap();
        } else if (AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_03.equals(dataType)) {
            nameMap = strategyVO.getApplyMap();
        }
        if (nameMap == null || nameMap.isEmpty()) {
            return true;
        } else {
            if (StringUtils.isEmpty(name)) {
                return false;
            }
            return nameMap.containsKey(name);
        }
    }

    /**
     * 对票据金额的验证
     *
     * @param billMoney
     * @param strategyVO
     * @return
     */
    public boolean checkSignInfoBillMoney(BigDecimal billMoney, AbBillSignStrategyVO strategyVO) {
        if (billMoney != null) {
            //策略匹配金额为空为true
            if (strategyVO.getBeginNum() == null && strategyVO.getEndNum() == null) {
                return true;
            }
            boolean begin = true;//开始金额为空则为真
            if (strategyVO.getBeginNum() != null) {
                int num = billMoney.compareTo(strategyVO.getBeginNum());
                begin = checkSignInfoBillMoney(num, strategyVO.getBeginOperator());
            }
            boolean end = true;//结束金额为空则为真
            if (strategyVO.getEndNum() != null) {
                int num = billMoney.compareTo(strategyVO.getEndNum());
                end = checkSignInfoBillMoney(num, strategyVO.getEndOperator());
            }
            if (AccebillConstant.BILL_SIGN_STRATEGY_OPERATOR_OR.equals(strategyVO.getNumOperator())) {
                return begin || end;
            } else if (AccebillConstant.BILL_SIGN_STRATEGY_OPERATOR_AND.equals(strategyVO.getNumOperator())) {
                return begin && end;
            }
        }
        return false;
    }

    /**
     * 和关系运算符匹配是否为真
     *
     * @param compareToNum 1 大于 0等于 -1小于
     * @param Operator     关系运算符>\>=\<\<=
     * @return
     */
    public boolean checkSignInfoBillMoney(int compareToNum, String Operator) {
        if (AccebillConstant.BILL_SIGN_STRATEGY_OPERATOR_GT.equals(Operator) && compareToNum > 0) {
            return true;
        } else if (AccebillConstant.BILL_SIGN_STRATEGY_OPERATOR_GTEQ.equals(Operator) && compareToNum >= 0) {
            return true;
        } else if (AccebillConstant.BILL_SIGN_STRATEGY_OPERATOR_LT.equals(Operator) && compareToNum < 0) {
            return true;
        } else return AccebillConstant.BILL_SIGN_STRATEGY_OPERATOR_LTEQ.equals(Operator) && compareToNum <= 0;
    }

    /**
     * 获取可以签收的数据列表
     *
     * @return 返回签收列表
     */
    @Transactional(readOnly = true)
    public List<AbBillReceivebiz> getAbBillReceivebizByFaisable() {
        Map<String, Object> params = new HashMap<>();
        params.put("statusArray", new Integer[]{AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING, AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE});
        return abBillReceivebizDao.getAbBillReceivebizByParams(params);
    }

	/**
	 * 根据参数获取可以签收的数据列表
	 * @param id  签收表ID
	 * @param signmsgType 签收类型
	 * @param acctId 签收人账号
	 * @return 返回签收列表
	 */
	@Transactional(readOnly = true)
	public List<AbBillReceivebiz> queryReceiveBizByParames(String id, String signmsgType, String acctId){
		Map<String,Object> params=new HashMap<>();
		params.put("id",id);
		params.put("msgtype",signmsgType);
		params.put("acctId",acctId);
		params.put("statusArray", new Integer[]{AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING,AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE});
		return abBillReceivebizDao.getAbBillReceivebizByParams(params);
	}

    /**
     * 把所有待签收票据变为已撤回操作
     *
     * @return 返回成功笔数
     */
    public int updateAbBillReceivebizByPendingToRecall() {
        Map<String, Object> params = new HashMap<>();
        params.put("status", AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_RECALL);
        params.put("statusOlds", new Integer[]{AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING, AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE});
        return abBillReceivebizDao.batchUpdateAbBillReceivebizByParams(params);
    }

    /**
     * 根据map查询 签收表VO数据
     *
     * @param params 查询参数
     * @return 返回签收VO list数据
     */
    public List<AbBillReceivebizVO> getAbBillReceivebizVOByParams(Map<String, Object> params) {
        return abBillReceivebizDao.getAbBillReceivebizVOByParams(params);
    }

    /**
     * 批量发送指令
     * @param payerAcc
     * @param msgType
     * @param billType
     * @param signType
     * @return
     */
    public AjaxJson sendCommand(String payerAcc, String msgType, String billType, String signType) {
        int msg = 0;
        String message = null;
        AjaxJson json = new AjaxJson();
        boolean flag = false;
        Map<String, Object> messMsg = new HashMap<String, Object>();

        if (StringUtil.isEmpty(payerAcc)) {
            msg = -1;
            message = AccebillConstant.PAYERACC_IS_NUL;
        }
        if (!StringUtil.isEmpty(payerAcc) && StringUtil.isEmpty(billType)) {
//			throw new BusinessException(AccebillMessageExtEnum.BILLTYPE_IS_NULL);
            msg = -1;
            message = AccebillConstant.BILLTYPE_IS_NUL;
        }
        if (!StringUtil.isEmpty(payerAcc) && !StringUtil.isEmpty(billType)
                && StringUtil.isEmpty(signType)) {
//			throw new BusinessException(AccebillMessageExtEnum.SIGNTYPE_IS_NULL);
            msg = -1;
            message = AccebillConstant.SIGNTYPE_IS_NUL;
        }

        //根据账号获得银行接口代码
        String bifCode = abDraftExcService.getBankBifCodeByBankAcc(payerAcc);
        if (!StringUtil.isEmpty(payerAcc) && StringUtils.isBlank(bifCode)) {
            msg = -1;
            message = "nis.netbank.message.bankaccinfo.null";
        }

        if (msg == 0) {
            List<AbDraftExc> listAbDraftExc=new ArrayList<>();
            AbDraftExc eDraftExc = new AbDraftExc();
            eDraftExc.setPayerAcc(payerAcc);//查询账号
            eDraftExc.setBifCode(bifCode);//银行接口代码
            //默认子票区间为 0
            eDraftExc.setSbbllRngStrtSn("0");
            eDraftExc.setSbbllRngEndSn("0");
            eDraftExc.setMsgType(msgType);//消息类型
            eDraftExc.setOperDate(new Date());//操作日期
            eDraftExc.setStatus(16);//状态 16待发送
            List<String> billTypes=StrUtil.split(billType,",");
            List<String> signTypes=StrUtil.split(signType,",");
            billTypes.forEach(val->{
                signTypes.forEach(val2->{
                    AbDraftExc eDraftExcNew = new AbDraftExc();
                    BeanUtils.copyNotNullProperties(eDraftExc,eDraftExcNew);
                    eDraftExcNew.setBillType(val);//票据类型
                    eDraftExcNew.setSignType(val2);//签收类型
                    listAbDraftExc.add(eDraftExcNew);
                });
            });

            abDraftExcDao.batchSave(listAbDraftExc);
//			msg = 1;
            message = AccebillConstant.AB_DRAFT_EXC_SEND_SUCESS_MSG;
            flag = true;
        }

        messMsg.put("MSG_INFO", message);
        json.setAttributes(messMsg);
        json.setSuccess(flag);

        return json;
    }
}