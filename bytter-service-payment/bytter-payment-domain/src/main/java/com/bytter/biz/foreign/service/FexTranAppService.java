package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexTranAppDao;
import com.bytter.biz.foreign.dao.FexTranAppDtlDao;
import com.bytter.biz.foreign.dao.FexTranAppDtlTmpDao;
import com.bytter.biz.foreign.dao.FexTranInfoDao;
import com.bytter.biz.foreign.entity.FexProduct;
import com.bytter.biz.foreign.entity.FexRateInfo;
import com.bytter.biz.foreign.entity.FexTranApp;
import com.bytter.biz.foreign.entity.FexTranAppDtl;
import com.bytter.biz.foreign.entity.FexTranAppDtlTmp;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.entity.FexTranReqExt;
import com.bytter.biz.foreign.vo.*;
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
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.enums.AccebillMessageEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyPairService;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.BtCurrencyPairDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("fexTranAppService")
@WorkFlow(value = FexWorkFlowConstant.FEX_TRAN_APP)
@Transactional
public class FexTranAppService extends BaseService<FexTranApp, String> implements IWorkFlowListener, ICorpDeleteVerifyService, ICorpDisableVerifyService {

    private static final String CHINA_COUNTRY_NAME = "中国";

    @Autowired
    private FexTranAppDao fexTranAppDao;

    @Autowired
    private FexTranAppDtlTmpDao fexTranAppDtlTmpDao;

    @Autowired
    private FexTranAppDtlDao fexTranAppDtlDao;

    @Autowired
    private FexTranInfoDao fexTranInfoDao;

    @Autowired(required = false)
    private IBtBankAccService bankAccService;

    @Autowired
    private IWorkFlowService workFlowService;

    @Autowired
    private FexMessageService fexMessageService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @Autowired
    private FexTranReqService fexTranReqService;

    @DubboReference(check = false)
    private IUserService userService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @Resource
    private IMultiLangService multiLangServiceI;

    @Autowired
    private FexProductService fexProductService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Autowired
    private FexTranReqExtService fexTranReqExtService;
    @Autowired
    private FexRateInfoService fexRateInfoService;
    @DubboReference(check = false)
    private IBtCurrencyPairService btCurrencyPairService;
    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    @Autowired
    private FexTranConfigTemplateService fexTranConfigTemplateService;
    /**
     * 通过id查询FexTranApp
     * @param id
     * @return
     */
    public FexTranAppVO getFexTranAppVOById(String id) {
        FexTranApp fexTranApp = fexTranAppDao.get(id);
        FexTranAppVO fexTranAppVO = new FexTranAppVO();
        BeanUtils.copyProperties(fexTranApp, fexTranAppVO);
        CorpDTO corpDTO=   corpService.get(fexTranAppVO.getCorpId());
        if(corpDTO!=null){
            fexTranAppVO.setCorpName(corpDTO.getName());
        }
        UserDTO userDTO=   userService.getByUserId(fexTranAppVO.getCreateBy());
        if(corpDTO!=null){
            fexTranAppVO.setCreateUserName(userDTO.getUsername());
        }
        return fexTranAppVO;
    }

    /**
     *删除数据，改变状态为-2
     */
    public void delete(String id){
        FexTranApp fexTranApp = fexTranAppDao.get(id);
        fexTranApp.setStatus(ForeignConstant.STATUE_DEL);
        fexTranAppDao.update(fexTranApp);
    }

    /**
     * 需求表格列表
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput getFexTranAppDataTable(DataTablesInput input, ICurrentUser user) {
        List<String> corpArr = new ArrayList<>();
        Map<String, Object> params = input.getFormdata();
        if (null != params && null != params.get("corpIds") && StringUtils.isNotEmpty(params.get("corpIds").toString())) {
            String corpIds = params.get("corpIds").toString();
            corpArr.addAll(Arrays.asList(corpIds.split(",")));
        } else {
            corpArr.addAll(corpService.getUserCorpIds(ResourceConstant.FOREIGN_FEX_TRANAPP_QUERY, user.getId()));
        }
        return fexTranAppDao.dataTable(input, corpArr);
    }

    /**
     * 交易需求查询
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput getFexTranAppDataTableQuery(DataTablesInput input, ICurrentUser user) {
        List<String> corpArr = new ArrayList<>();
        Map<String, Object> params = input.getFormdata();
        if (null != params && null != params.get("corpIds") && StringUtils.isNotEmpty(params.get("corpIds").toString())) {
            String corpIds = params.get("corpIds").toString();
            corpArr.addAll(Arrays.asList(corpIds.split(",")));
        } else {
            corpArr.addAll(corpService.getUserCorpIds(ResourceConstant.FOREIGN_FEX_TRANAPP_QUERY, user.getId()));
        }
        return fexTranAppDao.dataTableQuery(input, corpArr);
    }

    /**
     * 交易需求待审批数据
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput getFexTranAppDataTableApprove(DataTablesInput input, ICurrentUser user) {
        List<String> ids = new ArrayList<>();
        Map<String, Object> params = input.getFormdata();
        if (null != params && null != params.get("corpIds") && StringUtils.isNotEmpty(params.get("corpIds").toString())) {
            String corpIds = params.get("corpIds").toString();
            ids.addAll(Arrays.asList(corpIds.split(",")));
        } else {
            ids.addAll(corpService.getUserCorpIds(ResourceConstant.FOREIGN_FEX_TRANAPP_QUERY, user.getId()));
        }
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(FexWorkFlowConstant.FEX_TRAN_APP, input);
        return workFlowBusiness.merge(fexTranAppDao.dataTableApprove(input, ids,workFlowBusiness.getBusinessIdList()));
    }

    /**
     * 保存临时数据
     * @param fexTranAppDtlTmpVO
     * @return
     */
    public FexTranAppDtlTmp saveDtlTmp(FexTranAppDtlTmpVO fexTranAppDtlTmpVO,ICurrentUser currentUser) {
        FexTranAppDtlTmp fexTranAppDtlTmp = new FexTranAppDtlTmp();
        BeanUtils.copyProperties(fexTranAppDtlTmpVO, fexTranAppDtlTmp);
        fillLatestTemplate(fexTranAppDtlTmpVO.getTemplateId(), fexTranAppDtlTmpVO.getCorpBankAcc(),
                fexTranAppDtlTmpVO.getBankPayTypeId(), fexTranAppDtlTmp::setTemplateId, fexTranAppDtlTmp::setTemplateName);
        //生成交易对手数据
        if (!EmptyUtils.isEmpty(fexTranAppDtlTmpVO.getOppId())) {
            fexTranAppDtlTmp.setCotpId(fexTranAppDtlTmpVO.getOppId());
        }
        //生成交易对手账号数据
        if (!EmptyUtils.isEmpty(fexTranAppDtlTmpVO.getCotpBankAcc())) {
            fexTranAppDtlTmp.setCotpBankAcc(fexTranAppDtlTmpVO.getCotpBankAcc());
            fexTranAppDtlTmp.setCotpAccId(fexTranAppDtlTmpVO.getCotpAccId());
        }
        fexTranAppDtlTmp.setCreateBy(currentUser.getUsername());
        fexTranAppDtlTmp.setCreateDate(DateUtils.getDate());
        FexTranReqVO fexTranReq=new FexTranReqVO();
        BeanUtils.copyNotNullProperties(fexTranAppDtlTmpVO,fexTranReq);
        // FPS/PAYPAL快捷支付不生成或更新通用交易对手及账户资料
        if (!StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_3)
                && !isQuickPayType(fexTranAppDtlTmpVO.getBankPayType())) {
            if(!EmptyUtils.isEmpty(fexTranAppDtlTmpVO.getFexCotpBankAccVO())){
                fexTranReq.setCotpBankName(fexTranAppDtlTmpVO.getFexCotpBankAccVO().getBankName());
            }
            //TODO 生成一笔 交易对手记录信息
            String cotpId = fexTranReqService.saveCotpRecord(fexTranReq, currentUser, false);
            if(!EmptyUtils.isEmpty(cotpId)){
                fexTranReq.setCotpId(cotpId);
            }
            //资金收付交易产生一笔 交易银行账户数据
            if (StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_4)) {

                String cotpBankAccId = fexTranReqService.saveCotpBankAcc(fexTranReq, currentUser, cotpId, false);
                fexTranAppDtlTmp.setCotpAccId(cotpBankAccId);
                if(!EmptyUtils.isEmpty(cotpBankAccId)){
                    fexTranReq.setCotpBankAcc(cotpBankAccId);
                }
                // 同步更新客商资料信息
                fexTranReqService.updateExternalCorpInfo(fexTranReq, currentUser);
            }
            fexTranAppDtlTmp.setCotpId(cotpId);
        }
        fexTranAppDtlTmpDao.save(fexTranAppDtlTmp);
        return fexTranAppDtlTmp;
    }

    /**
     * 保存正式表数据
     * @param fexTranAppDtlVO
     * @return
     */
    public FexTranAppDtl saveDtl(FexTranAppDtlVO fexTranAppDtlVO) {
        FexTranAppDtl fexTranAppDtl = new FexTranAppDtl();
        BeanUtils.copyProperties(fexTranAppDtlVO, fexTranAppDtl);
        fillLatestTemplate(fexTranAppDtlVO.getTemplateId(), fexTranAppDtlVO.getCorpBankAcc(),
                fexTranAppDtlVO.getBankPayTypeId(), fexTranAppDtl::setTemplateId, fexTranAppDtl::setTemplateName);
        //生成交易对手数据
        if (!EmptyUtils.isEmpty(fexTranAppDtlVO.getOppId())) {
            fexTranAppDtl.setCotpId(fexTranAppDtlVO.getOppId());
        }
        //生成交易对手账号数据
        if (!EmptyUtils.isEmpty(fexTranAppDtlVO.getCotpBankAcc())) {
            fexTranAppDtl.setCotpAccId(fexTranAppDtlVO.getCotpAccId());

        }
        ICurrentUser   currentUser=getCurrentUser();
        FexTranReqVO fexTranReq=new FexTranReqVO();
        BeanUtils.copyNotNullProperties(fexTranAppDtlVO,fexTranReq);
        if (!StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_3)) {
            if(!EmptyUtils.isEmpty(fexTranAppDtlVO.getFexCotpBankAccVO())){
                fexTranReq.setCotpBankName(fexTranAppDtlVO.getFexCotpBankAccVO().getBankName());
            }
            //TODO 生成一笔 交易对手记录信息
            String cotpId = fexTranReqService.saveCotpRecord(fexTranReq, currentUser, false);
            if(!EmptyUtils.isEmpty(cotpId)){
                fexTranReq.setCotpId(cotpId);
            }
            //资金收付交易产生一笔 交易银行账户数据
            if (StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_4)) {
                String cotpBankAccId = fexTranReqService.saveCotpBankAcc(fexTranReq, currentUser, cotpId, false);
                fexTranAppDtl.setCotpAccId(cotpBankAccId);
                if(!EmptyUtils.isEmpty(cotpBankAccId)){
                    fexTranReq.setCotpBankAcc(cotpBankAccId);
                }
                // 同步更新客商资料信息
                fexTranReqService.updateExternalCorpInfo(fexTranReq, currentUser);
            }
            fexTranAppDtl.setCotpId(cotpId);
        }
        fexTranAppDtlDao.save(fexTranAppDtl);
        return fexTranAppDtl;
    }

    /**
     * 按当前账户+支付类型写入最新模版名称（含支付类型部分）。
     */
    private void fillLatestTemplate(String templateId, String bankAcc, String payTypeId,
                                    Consumer<String> idSetter, Consumer<String> nameSetter) {
        FexTranTemplateConfigVO latest = fexTranConfigTemplateService.resolveLatestTemplateForSave(
                templateId, bankAcc, payTypeId);
        if (latest == null) {
            return;
        }
        if (EmptyUtils.isNotEmpty(latest.getId())) {
            idSetter.accept(latest.getId());
        }
        if (EmptyUtils.isNotEmpty(latest.getTemplateName())) {
            nameSetter.accept(latest.getTemplateName());
        }
    }

    /**
     * 删除明细数据
     * @param id
     */
    public void deleteDtlById(String id){
        if (!"0".equals(fexTranAppDtlDao.countDtl(id))) {
            FexTranAppDtl fexTranAppDtl = fexTranAppDtlDao.get(id);
            if (CommonConstant.YES.equals(fexTranAppDtl.getIsSwaptTran())) {
                fexTranAppDtlDao.delete(fexTranAppDtl.getPartnerId());
            }
            fexTranAppDtlDao.delete(id);
        }
    }

    /**
     * 查询临时表暂存数据
     * @param input
     * @return
     */
    public DataTablesOutput getDtlTmpDataTable(DataTablesInput input){
        Map<String, Object> params = input.getFormdata();
        List<String> idArr = new ArrayList<>();
        if (params != null && null != params.get("dtlIds") && StringUtils.isNotEmpty(params.get("dtlIds").toString())) {
            String ids = params.get("dtlIds").toString();
            if (ids.contains(",")) {
                idArr.addAll(Arrays.asList(ids.split(",")));
            } else {
                idArr.add(ids);
            }
        }
        input.getFormdata().remove("dtlIds");
        List<String> externalCorpIds = resolveExternalCorpIdsByName(params, "likes_cotpName");
        if (externalCorpIds == null) {
            externalCorpIds = resolveExternalCorpIdsByName(params, "eqs_cotpName");
        }
        DataTablesOutput output = fexTranAppDtlTmpDao.getDtlTmpDataTable(input, idArr, externalCorpIds);
        fillExternalCorpTmpInfo(output);
        return output;
    }

    /**
     * 根据appid查询正式表数据
     * @param input
     * @return
     */
    public DataTablesOutput getDtlDataTable(DataTablesInput input){
        List<String> externalCorpIds = resolveExternalCorpIdsByName(input == null ? null : input.getFormdata(), "likes_cotpName");
        if (externalCorpIds == null) {
            externalCorpIds = resolveExternalCorpIdsByName(input == null ? null : input.getFormdata(), "eqs_cotpName");
        }
        DataTablesOutput output = fexTranAppDtlDao.getDtlDataTable(input, externalCorpIds);
        fillExternalCorpInfo(output);
        return output;
    }

    /**
     * 获取临时表数据
     * @param id
     * @return
     */
    public FexTranAppDtlTmpVO getTranDtlTmpById(String id) {
        FexTranAppDtlTmp fexTranAppDtlTmp = fexTranAppDtlTmpDao.get(id);
        FexTranAppDtlTmpVO fexTranAppDtlTmpVO = new FexTranAppDtlTmpVO();
        BeanUtils.copyNotNullProperties(fexTranAppDtlTmp, fexTranAppDtlTmpVO);
        //币种
        if(StringUtils.isNotEmpty(fexTranAppDtlTmpVO.getCurId())) {
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(fexTranAppDtlTmpVO.getCurId());
            if (!EmptyUtils.isEmpty(currencyDTO)) {
                fexTranAppDtlTmpVO.setCurName(currencyDTO.getCurName());
                fexTranAppDtlTmpVO.setCurEnCode(currencyDTO.getEnglishCode());
            }
        }
        if(StringUtils.isNotEmpty(fexTranAppDtlTmpVO.getCotpAccCurId())) {
            BtCurrencyDTO currencyDTO1 = btCurrencyService.getObjectById(fexTranAppDtlTmpVO.getCotpAccCurId());
            if (!EmptyUtils.isEmpty(currencyDTO1)) {
                fexTranAppDtlTmpVO.setCotpAccCurName(currencyDTO1.getCurName());
                fexTranAppDtlTmpVO.setCotpAccEnglishCode(currencyDTO1.getEnglishCode());
            }
        }
        //主体单位账户币种
        if(StringUtils.isNotEmpty(fexTranAppDtlTmpVO.getCorpAccCurId())) {
            BtCurrencyDTO currencyDTO2 = btCurrencyService.getObjectById(fexTranAppDtlTmpVO.getCorpAccCurId());
            if (!EmptyUtils.isEmpty(currencyDTO2)) {
                fexTranAppDtlTmpVO.setCorpAccCurName(currencyDTO2.getCurName());
                fexTranAppDtlTmpVO.setCorpAccEnglishCode(currencyDTO2.getEnglishCode());
            }
        }
//        交易编码设置
        if (StringUtils.isNotEmpty(fexTranAppDtlTmp.getTranCode1())) {
            fexTranAppDtlTmpVO.setTranCode1Name(fexTranInfoDao.getByTranCode(fexTranAppDtlTmp.getTranCode1()).getTranName());
        }
        if (StringUtils.isNotEmpty(fexTranAppDtlTmp.getTranCode2())) {
            fexTranAppDtlTmpVO.setTranCode2Name(fexTranInfoDao.getByTranCode(fexTranAppDtlTmp.getTranCode2()).getTranName());
        }
//        交易对手设置
        if (StringUtils.isNotEmpty(fexTranAppDtlTmp.getCotpId())) {
            setExternalCorpInfo(fexTranAppDtlTmpVO, fexTranAppDtlTmp.getCotpId());
        }
//        交易对手银行账户设置
        if (StringUtils.isNotEmpty(fexTranAppDtlTmp.getCotpAccId()) || StringUtils.isNotEmpty(fexTranAppDtlTmp.getCotpBankAcc())) {
            setExternalCorpBankAccInfo(fexTranAppDtlTmpVO, fexTranAppDtlTmp.getCotpAccId());
        }
//        账户设置
        if (StringUtils.isNotEmpty(fexTranAppDtlTmp.getCorpBankAcc())) {
            BtBankAccDTO bankAccDTO = bankAccService.getBankAccById(fexTranAppDtlTmp.getCorpAccId());
            fexTranAppDtlTmpVO.setCorpBankAccName(bankAccDTO.getAccName());
        }
        return fexTranAppDtlTmpVO;
    }

    /**
     * 获取正式表数据
     * @param id
     * @return
     */
    public FexTranAppDtlVO getTranDtlById(String id) {
        FexTranAppDtl fexTranAppDtl = fexTranAppDtlDao.get(id);
        FexTranAppDtlVO fexTranAppDtlVO = new FexTranAppDtlVO();
        BeanUtils.copyNotNullProperties(fexTranAppDtl, fexTranAppDtlVO);
        fillExternalCorpInfo(fexTranAppDtlVO);
        //币种
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getCurId())) {
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(fexTranAppDtlVO.getCurId());
            if (!EmptyUtils.isEmpty(currencyDTO)) {
                fexTranAppDtlVO.setCurName(currencyDTO.getCurName());
                fexTranAppDtlVO.setCurEnCode(currencyDTO.getEnglishCode());
            }
        }
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getCotpAccCurId())) {
            BtCurrencyDTO currencyDTO1 = btCurrencyService.getObjectById(fexTranAppDtlVO.getCotpAccCurId());
            if (!EmptyUtils.isEmpty(currencyDTO1)) {
                fexTranAppDtlVO.setCotpAccCurName(currencyDTO1.getCurName());
                fexTranAppDtlVO.setCotpAccEnglishCode(currencyDTO1.getEnglishCode());
            }
        }
        //主体单位账户币种
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getCorpAccCurId())) {
            BtCurrencyDTO currencyDTO2 = btCurrencyService.getObjectById(fexTranAppDtlVO.getCorpAccCurId());
            if (!EmptyUtils.isEmpty(currencyDTO2)) {
                fexTranAppDtlVO.setCorpAccCurName(currencyDTO2.getCurName());
                fexTranAppDtlVO.setCorpAccEnglishCode(currencyDTO2.getEnglishCode());
            }
        }
//        交易编码设置
        if (StringUtils.isNotEmpty(fexTranAppDtl.getTranCode1())) {
            fexTranAppDtlVO.setTranCode1Name(fexTranInfoDao.getByTranCode(fexTranAppDtl.getTranCode1()).getTranName());
        }
        if (StringUtils.isNotEmpty(fexTranAppDtl.getTranCode2())) {
            fexTranAppDtlVO.setTranCode2Name(fexTranInfoDao.getByTranCode(fexTranAppDtl.getTranCode2()).getTranName());
        }
//        交易对手设置
        if (StringUtils.isNotEmpty(fexTranAppDtl.getCotpId())) {
            setExternalCorpInfo(fexTranAppDtlVO, fexTranAppDtl.getCotpId());
        }
//        交易对手银行账户设置
        if (StringUtils.isNotEmpty(fexTranAppDtl.getCotpAccId()) || StringUtils.isNotEmpty(fexTranAppDtl.getCotpBankAcc())) {
            setExternalCorpBankAccInfo(fexTranAppDtlVO, fexTranAppDtl.getCotpAccId());
        }
//        账户设置
        if (StringUtils.isNotEmpty(fexTranAppDtl.getCorpBankAcc())) {
            BtBankAccDTO bankAccDTO = bankAccService.getBankAccById(fexTranAppDtl.getCorpAccId());
            fexTranAppDtlVO.setCorpBankAccName(bankAccDTO.getAccName());
        }
        return fexTranAppDtlVO;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public FexTranAppDtlVO getById(String id){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        FexTranAppDtlVO fexTranAppDtlVO = fexTranAppDtlDao.getById(params);
        fillExternalCorpInfo(fexTranAppDtlVO);
        //获取所有有效币种
        List<BtCurrencyDTO> btCurrencyList = btCurrencyService.getBtCurrency();
        //业务币种Map
        Map<String, BtCurrencyDTO> busiBtCurrencyMap = btCurrencyList.stream().collect(Collectors.toMap(BtCurrencyDTO::getId, Function.identity()));
        //系统币种Map
        Map<String, BtCurrencyDTO> sysBtCurrencyMap = btCurrencyList.stream()
                .collect(Collectors.toMap(BtCurrencyDTO::getSysCurId, Function.identity(),(existing, replacement) -> existing));
        //币种
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getCurId())) {
            BtCurrencyDTO currencyDTO = busiBtCurrencyMap.get(fexTranAppDtlVO.getCurId());
            if (!EmptyUtils.isEmpty(currencyDTO)) {
                fexTranAppDtlVO.setCurName(currencyDTO.getCurName());
                fexTranAppDtlVO.setCurEnCode(currencyDTO.getEnglishCode());
            }
        }
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getCotpAccCurId())) {
            BtCurrencyDTO currencyDTO1 = busiBtCurrencyMap.get(fexTranAppDtlVO.getCotpAccCurId());
            if (!EmptyUtils.isEmpty(currencyDTO1)) {
                fexTranAppDtlVO.setCotpAccCurName(currencyDTO1.getCurName());
                fexTranAppDtlVO.setCotpAccEnglishCode(currencyDTO1.getEnglishCode());
            }
        }
        //主体单位账户币种
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getCorpAccCurId())) {
            BtCurrencyDTO currencyDTO2 = busiBtCurrencyMap.get(fexTranAppDtlVO.getCorpAccCurId());
            if (EmptyUtils.isEmpty(currencyDTO2)) {
                currencyDTO2 = sysBtCurrencyMap.get(fexTranAppDtlVO.getCorpAccCurId());
            }
            if (!EmptyUtils.isEmpty(currencyDTO2)) {
                fexTranAppDtlVO.setCorpAccCurName(currencyDTO2.getCurName());
                fexTranAppDtlVO.setCorpAccEnglishCode(currencyDTO2.getEnglishCode());
            }
        }
        //根据id获取交易货币对
        if(EmptyUtils.isNotEmpty(fexTranAppDtlVO.getTranCurId())){
            BtCurrencyPairDTO btCurrencyPairDTO = btCurrencyPairService.getById(fexTranAppDtlVO.getTranCurId());
            //设置交易货币对
            if(btCurrencyPairDTO.getCurId1().equals(btCurrencyPairDTO.getBaseCurId())){
                BtCurrencyDTO cur1 = null;
                BtCurrencyDTO cur2 = null;
                String currencyPairStr = "";
                if(busiBtCurrencyMap.containsKey(btCurrencyPairDTO.getCurId1())){
                    cur1 = busiBtCurrencyMap.get(btCurrencyPairDTO.getCurId1());
                }
                if(busiBtCurrencyMap.containsKey(btCurrencyPairDTO.getCurId2())){
                    cur2 = busiBtCurrencyMap.get(btCurrencyPairDTO.getCurId2());
                }
                if(null != cur1 && null!= cur2){
                    currencyPairStr = cur1.getEnglishCode() + "/" + cur2.getEnglishCode();
                }
                fexTranAppDtlVO.setCurrencyPair(currencyPairStr);
            }else{
                BtCurrencyDTO cur1 = null;
                BtCurrencyDTO cur2 = null;
                String currencyPairStr = "";
                if(busiBtCurrencyMap.containsKey(btCurrencyPairDTO.getCurId2())){
                    cur1 = busiBtCurrencyMap.get(btCurrencyPairDTO.getCurId2());
                }
                if(busiBtCurrencyMap.containsKey(btCurrencyPairDTO.getCurId1())){
                    cur2 = busiBtCurrencyMap.get(btCurrencyPairDTO.getCurId1());
                }
                if(null != cur1 && null!= cur2){
                    currencyPairStr = cur1.getEnglishCode() + "/" + cur2.getEnglishCode();
                }
                fexTranAppDtlVO.setCurrencyPair(currencyPairStr);
            }
        }

        //根据id获取报价方式
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getQuotationMethod())){
            FexRateInfo fexRateInfo = fexRateInfoService.get(fexTranAppDtlVO.getQuotationMethod());
            if (fexRateInfo != null) {
                //设置报价方式
                //报价方式系统币种转化为业务币种
                if (sysBtCurrencyMap.containsKey(fexRateInfo.getCur())) {
                    fexTranAppDtlVO.setQuotationMethod1(sysBtCurrencyMap.get(fexRateInfo.getCur()).getEnglishCode());
                }
                if (sysBtCurrencyMap.containsKey(fexRateInfo.getReferCur())) {
                    fexTranAppDtlVO.setQuotationMethod2(sysBtCurrencyMap.get(fexRateInfo.getReferCur()).getEnglishCode());
                }
                //设置期权费币种
            }
        }
        //inject(fexTranAppDtlVO);
        if(!EmptyUtils.isEmpty(fexTranAppDtlVO.getBuyCurId())){
            BtCurrencyDTO currencyDTO= busiBtCurrencyMap.get(fexTranAppDtlVO.getBuyCurId());
            if(currencyDTO!=null){
                fexTranAppDtlVO.setBuyCurEnCode(currencyDTO.getEnglishCode());
                fexTranAppDtlVO.setBuyCurName(currencyDTO.getCurName());
            }
        }
        if(!EmptyUtils.isEmpty(fexTranAppDtlVO.getSellCurId())){
            BtCurrencyDTO currencyDTO= busiBtCurrencyMap.get(fexTranAppDtlVO.getSellCurId());
            if(currencyDTO!=null){
                fexTranAppDtlVO.setSellCurEnCode(currencyDTO.getEnglishCode());
                fexTranAppDtlVO.setSellCurName(currencyDTO.getCurName());
            }
        }
        // 保证金币种（查看页展示 bondCurEnCode）
        if(!EmptyUtils.isEmpty(fexTranAppDtlVO.getBondCurId())){
            BtCurrencyDTO currencyDTO= busiBtCurrencyMap.get(fexTranAppDtlVO.getBondCurId());
            if(currencyDTO!=null){
                fexTranAppDtlVO.setBondCurEnCode(currencyDTO.getEnglishCode());
                fexTranAppDtlVO.setBondCurName(currencyDTO.getCurName());
            }
        }
        return fexTranAppDtlVO;
    }

    /**
     * 保存数据
     * @param fexTranAppVO
     * @param user
     */
    public void save(FexTranAppVO fexTranAppVO,ICurrentUser user){
        FexTranApp fexTranApp;

        if (StringUtils.isNotEmpty(fexTranAppVO.getId())) {
            fexTranApp = fexTranAppDao.get(fexTranAppVO.getId());
            fexTranAppVO.setUpdateBy(user.getId());
            fexTranAppVO.setUpdateDate(new Date());
        } else {
            fexTranApp = new FexTranApp();
            fexTranAppVO.setCreateBy(user.getId());
            fexTranAppVO.setCreateDate(new Date());
            fexTranAppVO.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_APP, "R"));


        }
        BeanUtils.copyNotNullProperties(fexTranAppVO,fexTranApp);
        //        检查主表单位和明细表是否一致
        checkCorp(fexTranApp,fexTranAppVO.getDtlIds());
        fexTranAppDao.saveOrUpdate(fexTranApp);
        //        处理临时表明细数据
        if (StringUtils.isEmpty(fexTranAppVO.getId())) {
            copyDtlTmp(fexTranApp, fexTranAppVO.getDtlIds());
        }
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        //        打回数据角标处理
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(fexTranApp.getStatus())) {
            messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_ADD_SUBMIT_SUCC;
            //调用审批后方法
            getStartHandler().handler(FexWorkFlowConstant.FEX_TRAN_APP, fexTranApp.getId(), fexTranApp, fexTranApp.getCorpId(), user.getId());
//            notificationService.send(fexTranApp.getId(), user.getCorpId(), NotificationConstant.FOREIGN.FEX_TRAN_APP, fexTranApp);
        }
        notificationService.complete(NotificationConstant.FOREIGN.FEX_TRAN_APP, fexTranApp.getId(), user.getId());
        //生成日志
        fexMessageService.saveMessage(fexTranApp.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_TRAN_APP, user);
    }

    /**
     * 将临时表数据复制到正式表钟
     * @param fexTranApp
     * @param ids
     */
    public void copyDtlTmp(FexTranApp fexTranApp,String ids) {
        List<String> idArray = null;
        if (StringUtils.isNotEmpty(ids)) {
            idArray = new ArrayList<>();
            if (ids.contains(",")) {
                idArray.addAll(Arrays.asList(ids.split(",")));
            } else {
                idArray.add(ids);
            }
        }
        if (null != idArray && idArray.size() > 0) {
            List<FexTranAppDtlTmpVO> fexTranAppDtlTmpVOS = fexTranAppDtlTmpDao.listDtlTmpByIds(idArray);
            List<FexTranAppDtl> fexTranAppDtls = new ArrayList<>();
            for (FexTranAppDtlTmpVO fexTranAppDtlTmpVO : fexTranAppDtlTmpVOS) {
                FexTranAppDtl fexTranAppDtl = new FexTranAppDtl();
                BeanUtils.copyProperties(fexTranAppDtlTmpVO, fexTranAppDtl);
//                fexTranAppDtl.setId(null);
                fexTranAppDtl.setAppId(fexTranApp.getId());
                fexTranAppDtls.add(fexTranAppDtl);
            }
            fexTranAppDtlDao.batchSave(fexTranAppDtls);
        }
    }

    /**
    *判断单位是否一致检查
    *@Param fexTranApp
    *@Param ids
    *@return
    */
    private void checkCorp(FexTranApp fexTranApp,String ids){
//            新增时判断单位是否一致
        if (StringUtils.isEmpty(fexTranApp.getId())) {
            List<String> idArray = null;
            if (StringUtils.isNotEmpty(ids)) {
                idArray = new ArrayList<>();
                if (ids.contains(",")) {
                    idArray.addAll(Arrays.asList(ids.split(",")));
                } else {
                    idArray.add(ids);
                }
            }
            if (null != idArray && idArray.size() > 0) {
                List<FexTranAppDtlTmpVO> fexTranAppDtlTmpVOS = fexTranAppDtlTmpDao.listDtlTmpByIds(idArray);
                for (FexTranAppDtlTmpVO fexTranAppDtlTmpVO : fexTranAppDtlTmpVOS) {
                    if (!fexTranApp.getCorpId().equals(fexTranAppDtlTmpVO.getCorpId())) {
                        throw new ParameterException(ForeignMessageEnum.FEX_TRAN_APP_CHECK_CORP);
                    }
                }
            }
        } else {
//                修改时判断单位是否一致
            Map<String, Object> params = new HashMap<>();
            params.put("id", fexTranApp.getId());
            List<FexTranAppDtlVO> fexTranAppDtlVOS = fexTranAppDtlDao.listFexTranAppDtlByBusId(params);
            for (FexTranAppDtlVO fexTranAppDtlVO : fexTranAppDtlVOS) {
                if (!fexTranApp.getCorpId().equals(fexTranAppDtlVO.getCorpId())) {
                    throw new ParameterException(ForeignMessageEnum.FEX_TRAN_APP_CHECK_CORP);
                }
            }
        }
    }

    /**
     * 通过id删除临时数据
     * @param id
     */
    public void deletDtlTmpById(String id){
        if (!"0".equals(fexTranAppDtlTmpDao.countDtlTmp(id))) {
            FexTranAppDtlTmp fexTranAppDtlTmp = fexTranAppDtlTmpDao.get(id);
            if (CommonConstant.YES.equals(fexTranAppDtlTmp.getIsSwaptTran())) {
                fexTranAppDtlTmpDao.delete(fexTranAppDtlTmp.getPartnerId());
            }
            fexTranAppDtlTmpDao.delete(id);
        }
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /*判断业务主键是否为空*/
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        FexTranApp fexTranApp = fexTranAppDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        Integer messageType = 0;
        String message = "";
        if (isPass) {
            fexTranApp.setStatus(ForeignConstant.STATUE_CHECK);
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            message = FexMessageConstant.FEX_LOG_APP_SUCC;
//            审批通过后生成交易信息
            copyTranApp2TranReq(fexTranApp);
        } else {
            fexTranApp.setStatus(ForeignConstant.STATUE_BACK);
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
            message = FexMessageConstant.FEX_LOG_APP_BACK;
        }
        //生成日志
        fexMessageService.saveMessage(fexTranApp.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_TRAN_APP, user);
        fexTranAppDao.update(fexTranApp);
    }

    /**
     *
     * @param fexTranApp
     */
    public void copyTranApp2TranReq(FexTranApp fexTranApp){
        Map<String, Object> params = new HashMap<>();
        params.put("id", fexTranApp.getId());
        List<FexTranReq> fexTranReqs = new ArrayList<>();
        List<FexTranReqExt> fexTranReqExts = new ArrayList<>();
        UserDTO userDTO = userService.getByUserId(fexTranApp.getCreateBy());
        List<FexTranAppDtlVO> fexTranAppDtlVOS = fexTranAppDtlDao.listFexTranAppDtlByBusId(params);
        for (FexTranAppDtlVO fexTranAppDtlVO : fexTranAppDtlVOS) {
            //外汇掉期交易
            if(ForeignConstant.TRAN_TYPE_9.equals(fexTranAppDtlVO.getTranType())){
                FexTranReq fexTranReq1 = new FexTranReq();
                FexTranReq fexTranReq2 = new FexTranReq();
                BeanUtils.copyProperties(fexTranAppDtlVO, fexTranReq1);
                BeanUtils.copyProperties(fexTranAppDtlVO, fexTranReq2);
                fexTranReq1.setId(null);
                fexTranReq2.setId(null);
                fexTranReq1.setTranType(ForeignConstant.TRAN_TYPE_1);
                fexTranReq2.setTranType(ForeignConstant.TRAN_TYPE_1);
                fexTranReq1.setIsSwapTran("1");
                fexTranReq2.setIsSwapTran("1");
                fexTranReq1.setSwapType(ForeignConstant.SWAP_TYPE_1);
                fexTranReq2.setSwapType(ForeignConstant.SWAP_TYPE_2);
                fexTranReq1.setFexTranType(fexTranAppDtlVO.getFexTranType());
                fexTranReq2.setFexTranType(fexTranAppDtlVO.getFexTranTypeRemoteEnd());
                fexTranReq1.setSettleDate(fexTranAppDtlVO.getSettleDate());
                fexTranReq2.setSettleDate(fexTranAppDtlVO.getEndSettleDate());
                fexTranReq1.setCorpId(fexTranApp.getCorpId());
                fexTranReq2.setCorpId(fexTranApp.getCorpId());
                /*fexTranReq1.setIsSwapTran(fexTranAppDtlVO.getIsSwaptTran());
                fexTranReq2.setIsSwapTran(fexTranAppDtlVO.getIsSwaptTran());*/
                fexTranReq1.setCreateDate(fexTranApp.getCreateDate());
                fexTranReq2.setCreateDate(fexTranApp.getCreateDate());
                fexTranReq1.setCreateBy(userDTO.getLoginName());
                fexTranReq2.setCreateBy(userDTO.getLoginName());
                fexTranReq1.setStatus(ForeignConstant.STATUE_TEMP);
                fexTranReq2.setStatus(ForeignConstant.STATUE_TEMP);
                fexTranReq1.setTranAppDtlId(fexTranAppDtlVO.getId());
                fexTranReq2.setTranAppDtlId(fexTranAppDtlVO.getId());
                fexTranReq1.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_REQ));
                fexTranReq2.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_REQ));
                fexTranReq1.setCotpId(fexTranAppDtlVO.getCotpId());
                fexTranReq2.setCotpId(fexTranAppDtlVO.getCotpId());
                if(StringUtils.isNotBlank(fexTranAppDtlVO.getCorpAccId())) {
                    BtBankAccDTO bankAcc = bankAccService.getBankAccById(fexTranAppDtlVO.getCorpAccId());
                    if(bankAcc != null) {
                        fexTranReq1.setCorpAccName(bankAcc.getAccName());
                        fexTranReq1.setAccType(bankAcc.getAccType());
                        fexTranReq1.setCorpAccCurId(bankAcc.getCurId());
                        fexTranReq2.setCorpAccName(bankAcc.getAccName());
                        fexTranReq2.setAccType(bankAcc.getAccType());
                        fexTranReq2.setCorpAccCurId(bankAcc.getCurId());
                    }
                }
                if(StringUtils.isNotBlank(fexTranAppDtlVO.getCotpId())) {
                    ExternalCorpDTO externalCorp = getExternalCorpById(fexTranAppDtlVO.getCotpId());
                    if(externalCorp != null) {
                        fillFexTranReqCotpArea(fexTranReq1, externalCorp);
                        fillFexTranReqCotpArea(fexTranReq2, externalCorp);
                    }
                }
                fexTranReq1.setCorpAddrEn(fexTranAppDtlVO.getCorpAddEn());
                if(StringUtils.isNotBlank(fexTranAppDtlVO.getCotpAccId())) {
                    ExternalCorpBankAccDTO cotpBankAcc = getExternalCorpBankAccById(fexTranAppDtlVO.getCotpAccId());
                    if(cotpBankAcc != null) {
                        fexTranReq1.setCotpAccName(cotpBankAcc.getExternalAccName());
                        fexTranReq1.setCountryId(cotpBankAcc.getBankCountryId());//交易对手银行国家
                        fexTranReq2.setCotpAccName(cotpBankAcc.getExternalAccName());
                        fexTranReq2.setCountryId(cotpBankAcc.getBankCountryId());//交易对手银行国家
                    }
                }
                //外汇期权交易
                if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReq1.getTranType())){
                    FexProduct product = fexProductService.get(fexTranReq1.getProId(), true);
                    if(product != null) {
                        fexTranReq1.setTranSettleMethod(product.getTranSettleMethod());
                        fexTranReq1.setSettleDate(DateUtils.getDateYMD());
                        fexTranReq2.setTranSettleMethod(product.getTranSettleMethod());
                        fexTranReq2.setSettleDate(DateUtils.getDateYMD());
                    }
                }
                //交易需求即期汇率写入交易的合同/协定汇率
                //fexTranReq1.setContractRate(fexTranAppDtlVO.getWishRate());
                //fexTranReq2.setContractRate(fexTranAppDtlVO.getWishRate());
                //单据来源
                fexTranReq1.setBillFrom(ForeignConstant.BILL_FROM_1);
                fexTranReq2.setBillFrom(ForeignConstant.BILL_FROM_1);

                //设置、计算交割期限
                Integer deliveryDeadline1 = DateUtils.dayDiffs(fexTranAppDtlVO.getSettleDate(),fexTranAppDtlVO.getTranDate());
                Integer deliveryDeadline2 = DateUtils.dayDiffs(fexTranAppDtlVO.getEndSettleDate(),fexTranAppDtlVO.getTranDate());
                fexTranReq1.setDeliveryDeadline(deliveryDeadline1 + "");
                fexTranReq2.setDeliveryDeadline(deliveryDeadline2 + "");
                //买卖方向
                if(ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReq1.getBusDirection())){
                    fexTranReq2.setBusDirection(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL);
                }else if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReq1.getBusDirection())){
                    fexTranReq2.setBusDirection(ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY);
                }
                //固定金额
                if(ForeignConstant.FIXED_MONEY_1.equals(fexTranReq1.getBusDirection())){
                    fexTranReq2.setFixedMoney(ForeignConstant.FIXED_MONEY_2);
                }else if(ForeignConstant.FIXED_MONEY_2.equals(fexTranReq1.getBusDirection())){
                    fexTranReq2.setFixedMoney(ForeignConstant.FIXED_MONEY_1);
                }
                fexTranReq2.setBuyCurId(fexTranReq1.getSellCurId());
                fexTranReq2.setBuyMoney(fexTranReq1.getSellMoney());
                fexTranReq2.setSellCurId(fexTranReq1.getBuyCurId());
                fexTranReq2.setSellMoney(fexTranReq1.getBuyMoney());

                //外汇业务类型 1-结汇、2-售汇、3-外汇买卖
                if(ForeignConstant.FEX_BUS_TYPE_1.equals(fexTranReq1.getFexBusType())){
                    fexTranReq2.setFexBusType(ForeignConstant.FEX_BUS_TYPE_2);
                }else if(ForeignConstant.FEX_BUS_TYPE_2.equals(fexTranReq1.getFexBusType())){
                    fexTranReq2.setFexBusType(ForeignConstant.FEX_BUS_TYPE_1);
                }

                //fexTranReqs.add(fexTranReq1);
                //fexTranReqs.add(fexTranReq2);

                fexTranReqService.saveEntity(fexTranReq1);
                fexTranReqService.saveEntity(fexTranReq2);
                this.saveFexTranReqExt(fexTranAppDtlVO, fexTranReq1, fexTranReq2);

            }else{
                FexTranReq fexTranReq = new FexTranReq();
                BeanUtils.copyProperties(fexTranAppDtlVO, fexTranReq);
                if(ForeignConstant.TRAN_TYPE_7.equals(fexTranAppDtlVO.getTranType()) || ForeignConstant.TRAN_TYPE_8.equals(fexTranAppDtlVO.getTranType())){
                    fexTranReq.setTranType(ForeignConstant.TRAN_TYPE_1);
                }
                if(ForeignConstant.TRAN_TYPE_7.equals(fexTranAppDtlVO.getTranType())){
                    fexTranReq.setFexTranType(ForeignConstant.FEX_TRAN_TYPE_0);
                }else if(ForeignConstant.TRAN_TYPE_8.equals(fexTranAppDtlVO.getTranType())){
                    fexTranReq.setFexTranType(ForeignConstant.FEX_TRAN_TYPE_1);
                }
                fexTranReq.setId(null);
                fexTranReq.setCorpId(fexTranApp.getCorpId());
                fexTranReq.setIsSwapTran("0");
                fexTranReq.setCreateDate(fexTranApp.getCreateDate());
                fexTranReq.setCreateBy(userDTO.getLoginName());
                fexTranReq.setStatus(ForeignConstant.STATUE_TEMP);
                fexTranReq.setTranAppDtlId(fexTranAppDtlVO.getId());
                fexTranReq.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_REQ));
                fexTranReq.setCotpId(fexTranAppDtlVO.getCotpId());
                if(StringUtils.isNotBlank(fexTranAppDtlVO.getCorpAccId())) {
                    BtBankAccDTO bankAcc = bankAccService.getBankAccById(fexTranAppDtlVO.getCorpAccId());
                    if(bankAcc != null) {
                        fexTranReq.setCorpAccName(bankAcc.getAccName());
                        fexTranReq.setAccType(bankAcc.getAccType());
                        fexTranReq.setCorpAccCurId(bankAcc.getCurId());
                    }
                }
                if(StringUtils.isNotBlank(fexTranAppDtlVO.getCotpId())) {
                    ExternalCorpDTO externalCorp = getExternalCorpById(fexTranAppDtlVO.getCotpId());
                    if(externalCorp != null) {
                        fillFexTranReqCotpArea(fexTranReq, externalCorp);
                    }
                }
                fexTranReq.setCorpAddrEn(fexTranAppDtlVO.getCorpAddEn());
                if(StringUtils.isNotBlank(fexTranAppDtlVO.getCotpAccId())) {
                    ExternalCorpBankAccDTO cotpBankAcc = getExternalCorpBankAccById(fexTranAppDtlVO.getCotpAccId());
                    if(cotpBankAcc != null) {
                        fexTranReq.setCotpAccName(cotpBankAcc.getExternalAccName());
                        fexTranReq.setCountryId(cotpBankAcc.getBankCountryId());//交易对手银行国家
                    }
                }
                //外汇期权交易
                if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())){
                    FexProduct product = fexProductService.get(fexTranReq.getProId(), true);
                    if(product != null) {
                        fexTranReq.setTranSettleMethod(product.getTranSettleMethod());
                        //fexTranReq.setSettleDate(DateUtils.getDateYMD());
                    }
                    //执行汇率
                    fexTranReq.setContractRate(fexTranAppDtlVO.getWishRate());
                    //障碍值
                    if(null != fexTranAppDtlVO.getExceptLowVal() && null == fexTranAppDtlVO.getExceptHignVal()){
                        fexTranReq.setExceptVal(fexTranAppDtlVO.getExceptLowVal());
                    }
                    if(null != fexTranAppDtlVO.getExceptLowVal() && null != fexTranAppDtlVO.getExceptHignVal()){
                        fexTranReq.setExceptLowVal(fexTranAppDtlVO.getExceptLowVal());
                        fexTranReq.setExceptHignVal(fexTranAppDtlVO.getExceptHignVal());
                    }
                }
                //单据来源
                fexTranReq.setBillFrom(ForeignConstant.BILL_FROM_1);

                //设置期权费币种
                fexTranReq.setPremiumCur(fexTranAppDtlVO.getCurId());
                //平均期天数
                if(null != fexTranAppDtlVO.getAvgDays()){
                    fexTranReq.setAvgDays(BigDecimal.valueOf(fexTranAppDtlVO.getAvgDays()));
                }
                ///fexTranReqs.add(fexTranReq);
                fexTranReqService.saveEntity(fexTranReq);
                this.saveFexTranReqExt(fexTranAppDtlVO, fexTranReq, null);
            }
            //FexTranReqExt fexTranReqExt = new FexTranReqExt();
            //fexTranReqExt.setReqId(fexTranReq.getid);
            //fexTranReqExts.add(fexTranReqExt);
        }
        //fexTranReqService.batchSave(fexTranReqs);
        //fexTranReqExtService.batchSave(fexTranReqExts);
    }

    /**
     * 更新掉期交易ParnerId
     * @param id1
     * @param id2
     */
    public void addUpdateParner(String id1,String id2){
        FexTranAppDtlTmp fexTranAppDtlTmp1 = fexTranAppDtlTmpDao.get(id1);
        FexTranAppDtlTmp fexTranAppDtlTmp2 = fexTranAppDtlTmpDao.get(id2);
        fexTranAppDtlTmp1.setPartnerId(id2);
        fexTranAppDtlTmp2.setPartnerId(id1);
        fexTranAppDtlTmpDao.update(fexTranAppDtlTmp1);
        fexTranAppDtlTmpDao.update(fexTranAppDtlTmp2);
    }

    /**
     * 更新掉期交易ParnerId
     * @param id1
     * @param id2
     */
    public void editUpdateParner(String id1,String id2){
        FexTranAppDtl fexTranAppDtl1 = fexTranAppDtlDao.get(id1);
        FexTranAppDtl fexTranAppDtl2 = fexTranAppDtlDao.get(id2);
        fexTranAppDtl1.setPartnerId(id2);
        fexTranAppDtl2.setPartnerId(id1);
        fexTranAppDtlTmpDao.update(fexTranAppDtl1);
        fexTranAppDtlTmpDao.update(fexTranAppDtl2);
    }


    /**
     * 通过id查询交易内所有明细
     * @param appId
     */
    public List<FexTranAppDtlVO> listByAppId(String appId){
        Map<String, Object> params = new HashMap<>();
        params.put("id", appId);
        List<FexTranAppDtlVO> fexTranAppDtlVOS = fexTranAppDtlDao.listByAppId(params);
        fillExternalCorpInfo(fexTranAppDtlVOS);
        for (FexTranAppDtlVO fexTranAppDtlVO : fexTranAppDtlVOS) {
            if (ForeignConstant.TRAN_TYPE_1.equals(fexTranAppDtlVO.getTranType()) && CommonConstant.YES.equals(fexTranAppDtlVO.getIsSwaptTran())) {
                if (StringUtils.isNotEmpty(fexTranAppDtlVO.getPartnerId())) {
                    params.put("id", fexTranAppDtlVO.getPartnerId());
                    FexTranAppDtlVO partnerDtl = fexTranAppDtlDao.getById(params);
                    fillExternalCorpInfo(partnerDtl);
                    fexTranAppDtlVO.setFexTranAppDtlVO(partnerDtl);
                }
            }
            if(ForeignConstant.TRAN_TYPE_2.equals(fexTranAppDtlVO.getTranType())){
                fexTranAppDtlVO.setFexProductVO(fexProductService.getByIdUnion(fexTranAppDtlVO.getProId()));
            }
        }
        return fexTranAppDtlVOS;
    }


    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public BaseDao<FexTranApp, String> getEntityDao() {
        return fexTranAppDao;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有交易需求单据，不允许删除！
            int num = this.fexTranAppDao.getTranAppCountByCorpId(corpId,"del");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANAPP,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.FEX_VALID_DEL_TRANAPP_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的交易需求单据，不允许禁用！
            int num = this.fexTranAppDao.getTranAppCountByCorpId(corpId,"disable");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANAPP,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.FEX_VALID_DISABLE_TRANAPP_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }

    /**
     *
     * 根据币种查货币对
     *
     * @param curId1
     * @param curId2
     * @return
     */
    public Object getCurrencyPairIdBy(String curId1, String curId2) {
        return fexTranAppDtlDao.getCurrencyPairIdBy(curId1,curId2);
    }

    /**
     * 保存外汇拓展字段
     * @param fexTranReq1
     * @param fexTranReq2
     */
    public void saveFexTranReqExt(FexTranAppDtlVO fexTranAppDtlVO,FexTranReq fexTranReq1,FexTranReq fexTranReq2){
        if(null != fexTranReq1){
            FexTranReqExt entity = packFexTranReqExt(fexTranAppDtlVO, fexTranReq1);
            fexTranReqExtService.saveEntity(entity);
        }
        if(null != fexTranReq2){
            FexTranReqExt entity = packFexTranReqExt(fexTranAppDtlVO, fexTranReq2);
            fexTranReqExtService.saveEntity(entity);
        }
    }

    /**
     * 封装外汇拓展字段
     * @param fexTranReq
     */
    public FexTranReqExt packFexTranReqExt(FexTranAppDtlVO fexTranAppDtlVO, FexTranReq fexTranReq){
        FexTranReqExt fexTranReqExt = new FexTranReqExt();
        fexTranReqExt.setReqId(fexTranReq.getId());//单据id
        fexTranReqExt.setTranCurPairId(fexTranAppDtlVO.getTranCurId());//交易货币对id
        //获取所有有效币种
        List<BtCurrencyDTO> btCurrencyList = btCurrencyService.getBtCurrency();
        //业务币种Map
        Map<String, BtCurrencyDTO> busiBtCurrencyMap = btCurrencyList.stream().collect(Collectors.toMap(BtCurrencyDTO::getId, Function.identity()));
        //系统币种Map
        Map<String, BtCurrencyDTO> sysBtCurrencyMap = btCurrencyList.stream()
                .collect(Collectors.toMap(BtCurrencyDTO::getSysCurId, Function.identity(),(existing, replacement) -> existing));

        String tranCurPairBaseId = "";
        String tranCurPairReferId = "";
        //根据id获取交易货币对
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getTranCurId())){
            BtCurrencyPairDTO btCurrencyPairDTO = btCurrencyPairService.getById(fexTranAppDtlVO.getTranCurId());
            //设置交易货币对
            if(btCurrencyPairDTO.getCurId1().equals(btCurrencyPairDTO.getBaseCurId())){
                if(busiBtCurrencyMap.containsKey(btCurrencyPairDTO.getCurId1())){
                    tranCurPairBaseId = btCurrencyPairDTO.getCurId1();
                }
                if(busiBtCurrencyMap.containsKey(btCurrencyPairDTO.getCurId2())){
                    tranCurPairReferId = btCurrencyPairDTO.getCurId2();
                }
            }else{
                if(busiBtCurrencyMap.containsKey(btCurrencyPairDTO.getCurId2())){
                    tranCurPairBaseId = btCurrencyPairDTO.getCurId2();
                }
                if(busiBtCurrencyMap.containsKey(btCurrencyPairDTO.getCurId1())){
                    tranCurPairReferId = btCurrencyPairDTO.getCurId1();
                }
            }
        }
        fexTranReqExt.setTranCurPairBaseId(tranCurPairBaseId);//交易货币对标准币种
        fexTranReqExt.setTranCurPairReferId(tranCurPairReferId);//交易货币对报价币种
        fexTranReqExt.setIsPaymentAmount(fexTranAppDtlVO.getIsPaymentAmount());//金额是否自动计算
        //fexTranReqExt.setTranRateType(null);//交易汇率类型
        String quotationCurId = "";
        String quotationCurCode = "";
        String quotationReferCurId = "";
        String quotationReferCurCode = "";
        //根据id获取报价方式
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getQuotationMethod())){
            FexRateInfo fexRateInfo = fexRateInfoService.get(fexTranAppDtlVO.getQuotationMethod());
            if(busiBtCurrencyMap.containsKey(fexRateInfo.getCur())){
                quotationCurId = busiBtCurrencyMap.get(fexRateInfo.getCur()).getId();
                quotationCurCode = busiBtCurrencyMap.get(fexRateInfo.getCur()).getEnglishCode();
            }
            if(busiBtCurrencyMap.containsKey(fexRateInfo.getReferCur())){
                quotationReferCurId = busiBtCurrencyMap.get(fexRateInfo.getReferCur()).getId();
                quotationReferCurCode = busiBtCurrencyMap.get(fexRateInfo.getReferCur()).getEnglishCode();
            }
        }
        fexTranReqExt.setQuotationMethod(quotationCurCode + "/" + quotationReferCurCode);//报价方式
        fexTranReqExt.setQuotationCurId(quotationCurId);//标准币种
        fexTranReqExt.setQuotationReferCurId(quotationReferCurId);//报价币种
        if(null != fexTranAppDtlVO.getBuyMoney()){
            fexTranReqExt.setPositionMoney(fexTranAppDtlVO.getBuyMoney());//持仓金额
        }
        //fexTranReqExt.setProfitLossCur(null);//损益币种
        //fexTranReqExt.setProfitLossMoney(null);//损益
        //fexTranReqExt.setConvertCurType(null);//折本位币报价方式
        //fexTranReqExt.setConvertCurId(null);//折本位币标准币种
        //fexTranReqExt.setConvertCurReferId(null);//折本位币报价币种
        //fexTranReqExt.setConvertRate(null);//折本位币汇率
        //fexTranReqExt.setConvertMoney(null);//折本位币损益、期权费金额
        return fexTranReqExt;
    }

    /**
     * 根据客商ID回填临时明细的客商展示信息。
     */
    private void setExternalCorpInfo(FexTranAppDtlTmpVO vo, String externalCorpId) {
        ExternalCorpDTO externalCorp = getExternalCorpById(externalCorpId);
        if (externalCorp == null) {
            return;
        }
        fillExternalCorpInfo(vo, externalCorp);
    }

    /**
     * 根据客商ID回填正式明细的客商展示信息。
     */
    private void setExternalCorpInfo(FexTranAppDtlVO vo, String externalCorpId) {
        ExternalCorpDTO externalCorp = getExternalCorpById(externalCorpId);
        if (externalCorp == null) {
            return;
        }
        fillExternalCorpInfo(vo, externalCorp);
    }

    /**
     * 根据客商账户ID回填临时明细的客商账户信息。
     */
    private void setExternalCorpBankAccInfo(FexTranAppDtlTmpVO vo, String bankAccId) {
        ExternalCorpBankAccDTO bankAcc = getExternalCorpBankAcc(bankAccId, vo.getCotpBankAcc());
        if (bankAcc == null) {
            return;
        }
        fillExternalCorpBankAccInfo(vo, bankAcc);
    }

    /**
     * 根据客商账户ID回填正式明细的客商账户信息。
     */
    private void setExternalCorpBankAccInfo(FexTranAppDtlVO vo, String bankAccId) {
        ExternalCorpBankAccDTO bankAcc = getExternalCorpBankAcc(bankAccId, vo.getCotpBankAcc());
        if (bankAcc == null) {
            return;
        }
        fillExternalCorpBankAccInfo(vo, bankAcc);
    }

    /**
     * 为分页结果批量回填客商和客商账户展示信息。
     */
    private void fillExternalCorpInfo(DataTablesOutput output) {
        if (output == null || EmptyUtils.isEmpty(output.getData())) {
            return;
        }
        fillExternalCorpInfo(output.getData());
    }

    /**
     * 为正式明细集合批量回填客商和客商账户展示信息，避免逐条远程调用。
     */
    private void fillExternalCorpInfo(List<FexTranAppDtlVO> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }
        List<String> externalCorpIds = new ArrayList<>();
        List<String> bankAccIds = new ArrayList<>();
        for (FexTranAppDtlVO vo : list) {
            addIfNotEmpty(externalCorpIds, vo.getCotpId());
            addIfNotEmpty(externalCorpIds, vo.getOppId());
            addIfNotEmpty(bankAccIds, vo.getCotpAccId());
        }
        Map<String, ExternalCorpDTO> externalCorpMap = getExternalCorpMap(externalCorpIds);
        Map<String, ExternalCorpBankAccDTO> bankAccMap = getExternalCorpBankAccMap(bankAccIds);
        for (FexTranAppDtlVO vo : list) {
            ExternalCorpDTO externalCorp = externalCorpMap.get(getExternalCorpId(vo));
            if (externalCorp != null) {
                fillExternalCorpInfo(vo, externalCorp);
            }
            ExternalCorpBankAccDTO bankAcc = bankAccMap.get(vo.getCotpAccId());
            if (bankAcc != null) {
                fillExternalCorpBankAccInfo(vo, bankAcc);
            }
        }
    }

    /**
     * 为单条正式明细回填客商和客商账户展示信息。
     */
    private void fillExternalCorpInfo(FexTranAppDtlVO vo) {
        if (vo == null) {
            return;
        }
        ExternalCorpDTO externalCorp = getExternalCorpById(getExternalCorpId(vo));
        if (externalCorp != null) {
            fillExternalCorpInfo(vo, externalCorp);
        }
        ExternalCorpBankAccDTO bankAcc = getExternalCorpBankAccById(vo.getCotpAccId());
        if (bankAcc == null) {
            bankAcc = getExternalCorpBankAccByBankAcc(vo.getCotpBankAcc());
        }
        if (bankAcc != null) {
            fillExternalCorpBankAccInfo(vo, bankAcc);
        }
    }

    /**
     * 将客商基础资料写入正式明细展示字段。
     */
    private void fillExternalCorpInfo(FexTranAppDtlVO vo, ExternalCorpDTO externalCorp) {
        vo.setOppId(externalCorp.getId());
        vo.setCotpName(externalCorp.getName());
        vo.setCotpNameEn(externalCorp.getNameEn());
        vo.setCotpPostAddr(StringUtils.isNotBlank(externalCorp.getAddressEn()) ? externalCorp.getAddressEn() : externalCorp.getAddress());
        if (isNativeExternalCorp(externalCorp)) {
            vo.setCountryArea(CHINA_COUNTRY_NAME);
        } else {
            String countryId = externalCorp.getProvince();
            vo.setCotpCountry(countryId);
            vo.setCotpAddress(externalCorp.getCity());
            vo.setCountryArea(getCountryName(countryId));
        }
    }

    /**
     * 将客商基础资料写入临时明细展示字段。
     */
    private void fillExternalCorpInfo(FexTranAppDtlTmpVO vo, ExternalCorpDTO externalCorp) {
        vo.setOppId(externalCorp.getId());
        vo.setCotpName(externalCorp.getName());
        vo.setCotpNameEn(externalCorp.getNameEn());
        vo.setCotpPostAddr(StringUtils.isNotBlank(externalCorp.getAddressEn()) ? externalCorp.getAddressEn() : externalCorp.getAddress());
        if (isNativeExternalCorp(externalCorp)) {
            vo.setCountryArea(CHINA_COUNTRY_NAME);
        } else {
            String countryId = externalCorp.getProvince();
            vo.setCotpCountry(countryId);
            vo.setCotpAddress(externalCorp.getCity());
            vo.setCountryArea(getCountryName(countryId));
        }
    }

    /**
     * 将客商账户资料写入正式明细展示字段。
     */
    private void fillExternalCorpBankAccInfo(FexTranAppDtlVO vo, ExternalCorpBankAccDTO bankAcc) {
        vo.setCotpAccId(bankAcc.getId());
        vo.setCotpBank(bankAcc.getBank());
        vo.setCotpBankName(bankAcc.getBank());
        vo.setCotpBankAcc(bankAcc.getExternalAcc());
        vo.setCotpSwiftCode(bankAcc.getSwiftCode());
        vo.setCotpTranBankName(bankAcc.getIntermediaryBank());
        vo.setCotpTranBankAddr(bankAcc.getIntermediaryBankAddress());
        vo.setCotpTranBankAcc(bankAcc.getIntermediaryBankAcc());
        vo.setLocalClearNo(bankAcc.getLocalClearNo());
        vo.setFexCotpBankAccVO(externalCorpBankAccToFexCotpBankAcc(bankAcc));
    }

    /**
     * 将客商账户资料写入临时明细展示字段。
     */
    private void fillExternalCorpBankAccInfo(FexTranAppDtlTmpVO vo, ExternalCorpBankAccDTO bankAcc) {
        vo.setCotpAccId(bankAcc.getId());
        vo.setCotpBank(bankAcc.getBank());
        vo.setCotpBankAcc(bankAcc.getExternalAcc());
        vo.setLocalClearNo(bankAcc.getLocalClearNo());
        vo.setFexCotpBankAccVO(externalCorpBankAccToFexCotpBankAcc(bankAcc));
    }

    /**
     * 按客商ID查询单条客商资料。
     */
    private ExternalCorpDTO getExternalCorpById(String id) {
        if (StringUtils.isBlank(id) || externalCorpService == null) {
            return null;
        }
        return externalCorpService.getById(id);
    }

    /**
     * 按客商账户ID查询单条账户资料。
     */
    private ExternalCorpBankAccDTO getExternalCorpBankAccById(String id) {
        if (StringUtils.isBlank(id) || externalCorpService == null) {
            return null;
        }
        Map<String, ExternalCorpBankAccDTO> bankAccMap = externalCorpService.queryFexExternalCorpBankAccMapByIds(Arrays.asList(id));
        return EmptyUtils.isEmpty(bankAccMap) ? null : bankAccMap.get(id);
    }

    /**
     * 批量查询客商资料，并对停用或批量接口未返回的数据按ID回退查询。
     */
    private Map<String, ExternalCorpDTO> getExternalCorpMap(List<String> ids) {
        Map<String, ExternalCorpDTO> externalCorpMap = new HashMap<>();
        if (EmptyUtils.isEmpty(ids) || externalCorpService == null) {
            return externalCorpMap;
        }
        Map<String, ExternalCorpDTO> batchMap = externalCorpService.queryFexExternalCorpMapByIds(ids);
        if (!EmptyUtils.isEmpty(batchMap)) {
            externalCorpMap.putAll(batchMap);
        }
        for (String id : ids) {
            if (externalCorpMap.containsKey(id)) {
                continue;
            }
            ExternalCorpDTO externalCorp = externalCorpService.getById(id);
            if (externalCorp != null && StringUtils.isNotBlank(externalCorp.getId())) {
                externalCorpMap.put(externalCorp.getId(), externalCorp);
            }
        }
        return externalCorpMap;
    }

    /**
     * 批量查询客商账户资料。
     */
    private Map<String, ExternalCorpBankAccDTO> getExternalCorpBankAccMap(List<String> ids) {
        Map<String, ExternalCorpBankAccDTO> bankAccMap = new HashMap<>();
        if (EmptyUtils.isEmpty(ids) || externalCorpService == null) {
            return bankAccMap;
        }
        Map<String, ExternalCorpBankAccDTO> batchMap = externalCorpService.queryFexExternalCorpBankAccMapByIds(ids);
        if (!EmptyUtils.isEmpty(batchMap)) {
            bankAccMap.putAll(batchMap);
        }
        return bankAccMap;
    }

    /**
     * 非空且未重复的ID加入集合。
     */
    private void addIfNotEmpty(List<String> list, String id) {
        if (StringUtils.isNotBlank(id) && !list.contains(id)) {
            list.add(id);
        }
    }

    /**
     * 获取正式明细中的客商ID，兼容历史oppId字段。
     */
    private String getExternalCorpId(FexTranAppDtlVO vo) {
        if (vo == null) {
            return null;
        }
        if (StringUtils.isNotBlank(vo.getOppId())) {
            return vo.getOppId();
        }
        return vo.getCotpId();
    }

    /**
     * 将交易对手名称筛选转换为客商ID集合。
     */
    private List<String> resolveExternalCorpIdsByName(Map<String, Object> params, String field) {
        if (params == null || StringUtils.isBlank(field)) {
            return null;
        }
        Object name = params.remove(field);
        if (name == null || StringUtils.isBlank(name.toString())) {
            return null;
        }
        if (externalCorpService == null) {
            return new ArrayList<>();
        }
        List<ExternalCorpDTO> externalCorps = externalCorpService.findExternalCorpLikeName(name.toString());
        List<String> externalCorpIds = new ArrayList<>();
        if (EmptyUtils.isEmpty(externalCorps)) {
            return externalCorpIds;
        }
        for (ExternalCorpDTO externalCorp : externalCorps) {
            if (externalCorp != null && StringUtils.isNotBlank(externalCorp.getId())
                    && !externalCorpIds.contains(externalCorp.getId())) {
                externalCorpIds.add(externalCorp.getId());
            }
        }
        return externalCorpIds;
    }

    /**
     * 将客商地区信息写入交易申请。
     */
    private void fillFexTranReqCotpArea(FexTranReq fexTranReq, ExternalCorpDTO externalCorp) {
        if (isNativeExternalCorp(externalCorp)) {
            String chinaCountryId = getChinaCountryId();
            if (StringUtils.isNotBlank(chinaCountryId)) {
                fexTranReq.setCotpCountry(chinaCountryId);
            }
            fexTranReq.setCotpProv(externalCorp.getProvince());
            fexTranReq.setCotpCity(externalCorp.getCity());
        } else {
            fexTranReq.setCotpCountry(externalCorp.getProvince());
            fexTranReq.setCotpAddress(externalCorp.getCity());
        }
    }

    /**
     * 判断客商是否为境内客商。
     */
    private boolean isNativeExternalCorp(ExternalCorpDTO externalCorp) {
        return externalCorp != null && externalCorp.getIsNative() != null && externalCorp.getIsNative() == 1;
    }

    /**
     * 查询中国国家/地区ID。
     */
    private String getChinaCountryId() {
        if (sysCountryCustService == null) {
            return null;
        }
        SysCountryCustDTO country = sysCountryCustService.getByEnCode(BdlinkConstant.COUNTRY_CODE_CHINA);
        return country == null ? null : country.getId();
    }

    /**
     * 查询国家/地区中文名称。
     */
    private String getCountryName(String id) {
        if (StringUtils.isBlank(id) || sysCountryCustService == null) {
            return "";
        }
        SysCountryCustDTO country = sysCountryCustService.getById(id);
        return country == null ? "" : country.getNameZh();
    }

    /**
     * 将客商账户资料转换为历史交易对手账户VO，兼容页面对fexCotpBankAccVO字段的读取。
     */
    private FexCotpBankAccVO externalCorpBankAccToFexCotpBankAcc(ExternalCorpBankAccDTO bankAcc) {
        FexCotpBankAccVO vo = new FexCotpBankAccVO();
        vo.setId(bankAcc.getId());
        vo.setCotpId(bankAcc.getExternalCorpId());
        vo.setBankAcc(bankAcc.getExternalAcc());
        vo.setAccName(bankAcc.getExternalAccName());
        vo.setBankName(bankAcc.getBank());
        vo.setBankId(bankAcc.getBankCountryId());
        vo.setBankRmk(getCountryName(bankAcc.getBankCountryId()));
        vo.setBicCode(bankAcc.getSwiftCode());
        vo.setPayType(bankAcc.getPayExcType());
        vo.setBankAddr(bankAcc.getAccAddress());
        vo.setTranBank(bankAcc.getIntermediaryBank());
        vo.setTranBankName(bankAcc.getIntermediaryBank());
        vo.setTranBicCode(bankAcc.getIntermediaryBankSwiftCode());
        vo.setTranBankAcc(bankAcc.getIntermediaryBankAcc());
        vo.setTranBankAddr(bankAcc.getIntermediaryBankAddress());
        vo.setCurId(bankAcc.getCurId());
        vo.setLocalClearNo(bankAcc.getLocalClearNo());
        vo.setRmk(bankAcc.getRemark());
        return vo;
    }

    /**
     * 为临时明细分页结果批量回填客商展示信息。
     */
    private void fillExternalCorpTmpInfo(DataTablesOutput output) {
        if (output == null || EmptyUtils.isEmpty(output.getData())) {
            return;
        }
        fillExternalCorpTmpInfo(output.getData());
    }

    /**
     * 为临时明细集合批量回填客商和客商账户展示信息，避免逐条远程调用。
     */
    private void fillExternalCorpTmpInfo(List<FexTranAppDtlTmpVO> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }
        List<String> externalCorpIds = new ArrayList<>();
        List<String> bankAccIds = new ArrayList<>();
        for (FexTranAppDtlTmpVO vo : list) {
            addIfNotEmpty(externalCorpIds, vo.getCotpId());
            addIfNotEmpty(externalCorpIds, vo.getOppId());
            addIfNotEmpty(bankAccIds, vo.getCotpAccId());
        }
        Map<String, ExternalCorpDTO> externalCorpMap = getExternalCorpMap(externalCorpIds);
        Map<String, ExternalCorpBankAccDTO> bankAccMap = getExternalCorpBankAccMap(bankAccIds);
        for (FexTranAppDtlTmpVO vo : list) {
            ExternalCorpDTO externalCorp = externalCorpMap.get(getExternalCorpId(vo));
            if (externalCorp != null) {
                fillExternalCorpInfo(vo, externalCorp);
            }
            ExternalCorpBankAccDTO bankAcc = bankAccMap.get(vo.getCotpAccId());
            if (bankAcc != null) {
                fillExternalCorpBankAccInfo(vo, bankAcc);
            }
        }
    }

    /**
     * 获取临时明细中的客商ID，兼容历史oppId字段。
     */
    private String getExternalCorpId(FexTranAppDtlTmpVO vo) {
        if (vo == null) {
            return null;
        }
        if (StringUtils.isNotBlank(vo.getOppId())) {
            return vo.getOppId();
        }
        return vo.getCotpId();
    }

    /**
     * 查询客商账户资料，优先按账户ID查询，账户ID为空时按账号查询。
     */
    private ExternalCorpBankAccDTO getExternalCorpBankAcc(String bankAccId, String bankAcc) {
        if (StringUtils.isNotBlank(bankAccId)) {
            return getExternalCorpBankAccById(bankAccId);
        }
        return getExternalCorpBankAccByBankAcc(bankAcc);
    }

    /**
     * 按客商账号查询单条账户资料。
     */
    private ExternalCorpBankAccDTO getExternalCorpBankAccByBankAcc(String bankAcc) {
        if (StringUtils.isBlank(bankAcc) || externalCorpService == null) {
            return null;
        }
        ExternalCorpBankAccDTO bankAccDTO = externalCorpService.queryByBankAcc(bankAcc);
        if (bankAccDTO == null || StringUtils.isBlank(bankAccDTO.getExternalAcc())) {
            return null;
        }
        return bankAccDTO;
    }

    /**
     * 判断是否为不保存通用交易对手资料的快捷支付类型。
     */
    private boolean isQuickPayType(String bankPayType) {
        return "FPS".equalsIgnoreCase(bankPayType)
                || "PAYPAL".equalsIgnoreCase(bankPayType)
                || "PALPAY".equalsIgnoreCase(bankPayType);
    }
}
