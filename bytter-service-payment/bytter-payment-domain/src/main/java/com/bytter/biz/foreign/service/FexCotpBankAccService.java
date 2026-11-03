package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCotpBankAccDao;
import com.bytter.biz.foreign.entity.FexCotpBankAcc;
import com.bytter.biz.foreign.entity.FexCotpBankAccCur;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.foreign.vo.FexCotpBankAccVO;
import com.bytter.biz.foreign.vo.FexCotpVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.system.api.vo.ExternalCorpBankAccDetailDto;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.core.common.page.IPageResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author fusb
 *	@version 1.0
 *  @Title FexCotpBankAccService
 *  @Description 交易对手银行账户管理service
 *  @date 2020年2月11日
 */
@Service("fexCotpBankAccService")
@Transactional
@WorkFlow(value = FexWorkFlowConstant.FEX_COTP_BANK_ACC)
public class FexCotpBankAccService extends BaseService<FexCotpBankAcc,String> implements IWorkFlowListener {
    @Autowired
    private FexCotpBankAccDao fexCotpBankAccDao;
    @Autowired(required=false)
    private IWorkFlowService workFlowService;
    @Autowired(required = false)
    private ICorpService corpService;
    @Autowired(required = false)
    private INotificationService notificationService;
    @Autowired(required = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private FexCotpService fexCotpService;
    @Autowired
    private FexMessageService fexMessageService;
    @Autowired
    private FexCotpBankAccCurService fexCotpBankAccCurService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    public String saveCotpBankAcc(FexTranReqVO fexTranReq, ICurrentUser user, String cotpId, boolean editFlag) {
        ExternalCorpBankAccDTO bankAccDTO = externalCorpService.getExternalCorpBankAccByIdAndAcc(cotpId, fexTranReq.getCotpBankAcc());
        if (bankAccDTO != null && StringUtils.isNotBlank(bankAccDTO.getId())) {
            return bankAccDTO.getId();
        }
        String cotpName = getCotpName(fexTranReq, cotpId);
        if (StringUtils.isBlank(cotpName)) {
            return fexTranReq.getCotpAccId();
        }
        ExternalCorpDTO externalCorpDTO = fexCotpService.buildExternalCorpDTO(fexTranReq, cotpName);
        externalCorpDTO.setId(cotpId);
        externalCorpService.saveIfNotExist(externalCorpDTO);
        bankAccDTO = externalCorpService.getExternalCorpBankAccByIdAndAcc(cotpId, fexTranReq.getCotpBankAcc());
        return bankAccDTO == null ? fexTranReq.getCotpAccId() : bankAccDTO.getId();
    }

    private String getCotpName(FexTranReqVO fexTranReq, String cotpId) {
        if (StringUtils.isNotBlank(fexTranReq.getCotpName())) {
            return fexTranReq.getCotpName();
        }
        if (StringUtils.isNotBlank(fexTranReq.getCotpAccName())) {
            return fexTranReq.getCotpAccName();
        }
        FexCotpVO cotp = fexCotpService.getFexcotpById(cotpId);
        return cotp == null ? null : cotp.getCotpName();
    }

    /**
     * 交易对手银行账户列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        if (externalCorpService == null) {
            return new DataTablesOutput<FexCotpBankAccVO>(dataTablesInput.getDraw(), new ArrayList<FexCotpBankAccVO>(), 0, 0);
        }
        IPageResult<ExternalCorpBankAccDetailDto> pageResult = externalCorpService.listByExternalDetail(dataTablesInput);
        List<FexCotpBankAccVO> list = new ArrayList<>();
        if (pageResult != null && !EmptyUtils.isEmpty(pageResult.getList())) {
            for (Object item : pageResult.getList()) {
                list.add(externalDetailToFexCotpBankAcc((ExternalCorpBankAccDetailDto) item));
            }
        }
        long total = pageResult == null ? list.size() : pageResult.getTotalRow();
        return new DataTablesOutput<FexCotpBankAccVO>(dataTablesInput.getDraw(), list, total, total);
    }

    /**
     * 设置关联信息
     * @param output
     * @return
     */
    private DataTablesOutput setJoinInfo(DataTablesOutput output) {
        //获取银行账号主信息列表
        List<FexCotpBankAccVO> data = output.getData();
        if (!EmptyUtils.isEmptyList(data)) {
            // 获取所有币种
            Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrencyMap();
            for (FexCotpBankAccVO vo : data) {
                List<FexCotpBankAccCur> listBankAccCur = fexCotpBankAccCurService.listByBankAccId(vo.getId());
                if (!EmptyUtils.isEmptyList(listBankAccCur)) {
                    String curId = "";
                    String curName = "";
                    for (FexCotpBankAccCur cur : listBankAccCur) {
                        BtCurrencyDTO currencyDTO = curMap.get(cur.getCurId());
                        if (currencyDTO != null) {
                            if (StringUtils.isBlank(curId)) {
                                curId = cur.getCurId();
                                curName = currencyDTO.getEnglishCode();
                            } else {
                                curId = curId + CommonConstant.COMMA_SPLIT + cur.getCurId();
                                curName = curName + CommonConstant.COMMA_SPLIT + currencyDTO.getEnglishCode();
                            }
                        }
                    }
                    vo.setCurId(curId);
                    vo.setCurName(curName);
                }
            }
            output.setData(data);
        }
        return output;
    }

    /**
     * 保存交易对手银行账户信息
     * @param fexCotpBankAccVO
     * @return
     */
    public void saveFexCotpBankAcc(FexCotpBankAccVO fexCotpBankAccVO,ICurrentUser user){
        // 数据校验
        checkData(fexCotpBankAccVO);
        FexCotpBankAcc entity = new FexCotpBankAcc();
        BeanUtils.copyNotNullProperties(fexCotpBankAccVO,entity);
        if (StringUtils.isEmpty(fexCotpBankAccVO.getId())) { // 新增
            entity.setCreateBy(user.getId());
            entity.setCreateDate(DateUtils.getDate());
        } else { // 修改
            entity.setUpdateBy(user.getId());
            entity.setUpdateDate(DateUtils.getDate());
        }
        entity.setCorpId(user.getCorpId());
        entity.setCurId("");
        this.saveOrUpdate(entity);
        //保存多币种
        this.saveCur(fexCotpBankAccVO.getCurId(), entity.getId());
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        // 数据状态为提交时，挂接审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())){
            messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_ADD_SUBMIT_SUCC;
            //inject(fexCotpBankAccVO);
            fexCotpBankAccVO.setCotpName(fexCotpService.get(fexCotpBankAccVO.getCotpId()).getCotpName());
            getStartHandler().handler(FexWorkFlowConstant.FEX_COTP_BANK_ACC, entity.getId(), fexCotpBankAccVO, user.getCorpId(), user.getId());
            // 完成通知提醒
            notificationService.complete(NotificationConstant.FOREIGN.FEX_COTP_BANKACC,entity.getId(),user.getId());
        }
        if(user !=null) {
            //生成日志
            fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_COTP_BANKACC, user);
        }

    }

    /**
     * 保存币别
     *
     * @param curIdStr
     * @param id
     */
    public void saveCur(String curIdStr, String id) {
        //先删除币种
        fexCotpBankAccCurService.deleteByBankAccId(id);
        if (StringUtils.isNotBlank(curIdStr)) {
            String[] curIds = curIdStr.split(CommonConstant.COMMA_SPLIT);
            List<FexCotpBankAccCur> listCur = new ArrayList<>();
            for (int i = 0; i < curIds.length; i++) {
                if ("all".equals(curIds[i])) {
                    continue;
                }
                FexCotpBankAccCur accCur = new FexCotpBankAccCur();
                accCur.setBankAccId(id);
                accCur.setCurId(curIds[i]);
                listCur.add(accCur);
            }
            fexCotpBankAccCurService.batchSave(listCur);
        }
    }

    /**
     * 设置币种信息
     * @param vo
     */
    public void setCurInfo(FexCotpBankAccVO vo) {
        if (vo == null) {
            return;
        }
        List<FexCotpBankAccCur> listBankAccCur = fexCotpBankAccCurService.listByBankAccId(vo.getId());
        if (!EmptyUtils.isEmptyList(listBankAccCur)) {
            // 获取所有币种
            Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrencyMap();
            String curId = "";
            String curName = "";
            for (FexCotpBankAccCur cur : listBankAccCur) {
                BtCurrencyDTO currencyDTO = curMap.get(cur.getCurId());
                if (currencyDTO != null) {
                    if (StringUtils.isBlank(curId)) {
                        curId = cur.getCurId();
                        curName = currencyDTO.getEnglishCode();
                    } else {
                        curId = curId + CommonConstant.COMMA_SPLIT + cur.getCurId();
                        curName = curName + CommonConstant.COMMA_SPLIT + currencyDTO.getEnglishCode();
                    }
                }
            }
            vo.setCurId(curId);
            vo.setCurName(curName);
        }
    }

    /**
     * 获取交易对手银行账户信息
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public FexCotpBankAccVO getFexFexCotpBankAcc(String id){
        return getExternalCorpBankAcc(id);
    }

    /**
     * 获取交易对手账户数据
     *
     * @param id
     * @return
     */
    public FexCotpBankAccVO getCotpBankAccById(String id) {
        return getExternalCorpBankAcc(id);
    }

    /**
     * 修改交易对手银行账户信息状态
     * @param id
     * @param status
     * @return
     */
    public void updateFexCotpBankAccSatus(String id,Integer status){
        fexCotpBankAccDao.updateFexCotpBankAccSatus(id,status);
    }

    /**
     * 查询主交易对手银行账户审批列表
     *
     * @param input
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getApproveDataTables(DataTablesInput input) {
        return new DataTablesOutput<FexCotpBankAccVO>(input.getDraw(), new ArrayList<FexCotpBankAccVO>(), 0, 0);
    }

    /**
     * 审批通过调用方法
     * @param workFlowResult
     * @return
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String bussinessKey = workFlowResult.getBusinessKey();
        FexCotpBankAcc fexCotpBankAcc = this.get(bussinessKey);
        boolean result = workFlowResult.isPass();// 是否通过
        ICurrentUser user = workFlowResult.getUser();
        Integer messageType = 0;
        String message = "";
        if (result) {
            fexCotpBankAcc.setStatus(ForeignConstant.STATUE_CHECK);
            //审批通过时,发出通知告知已审批通过
            notificationService.send(fexCotpBankAcc.getId(),fexCotpBankAcc.getCorpId(), NotificationConstant.FOREIGN.FEX_COTP_BANKACC_PASS, fexCotpBankAcc);
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            message = FexMessageConstant.FEX_LOG_APP_SUCC;
        } else {
            fexCotpBankAcc.setStatus(ForeignConstant.STATUE_BACK);
            //审批未通过时,发出待处理消息告知已打回
            notificationService.send(fexCotpBankAcc.getId(),fexCotpBankAcc.getCorpId(),NotificationConstant.FOREIGN.FEX_COTP_BANKACC_BACK, fexCotpBankAcc);
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
            message = FexMessageConstant.FEX_LOG_APP_BACK;
        }
        //生成日志
        fexMessageService.saveMessage(fexCotpBankAcc.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_COTP_BANKACC, user);
        this.updateEntity(fexCotpBankAcc);
    }

    /**
     * 获取交易对手银行账户下拉框数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<FexCotpBankAccVO> getSelectFexCotpBankAcc(String cotpId,String curCode,String bankId){
        return getExternalCorpBankAccList(cotpId, curCode, bankId);
    }

    /**
     * 获取交易对手银行账户下拉框数据
     * @return
     */
    @Transactional(readOnly = true)
    public FexCotpBankAccVO getFexCotpBankAccByCotpId(String cotpId){
        List<FexCotpBankAccVO> list = getExternalCorpBankAccList(cotpId, null, null);
        return EmptyUtils.isEmptyList(list) ? new FexCotpBankAccVO() : list.get(0);
    }

    /**
     * 获取交易对手银行账户下拉框数据（关联币种表）
     * @return
     */
    @Transactional(readOnly = true)
    public List<FexCotpBankAccVO> getSelectFexCotpBankAccJoinCur(String cotpId,String curCode,String bankId){
        return getExternalCorpBankAccList(cotpId, curCode, bankId);
    }

    /**
     * 数据校验
     * @param vo
     */
    public void checkData(FexCotpBankAccVO vo){
        if (externalCorpService == null || StringUtils.isBlank(vo.getCotpId()) || StringUtils.isBlank(vo.getBankAcc())) {
            return;
        }
        ExternalCorpBankAccDTO bankAcc = externalCorpService.getExternalCorpBankAccByIdAndAcc(vo.getCotpId(), vo.getBankAcc());
        if (bankAcc != null && StringUtils.isNotBlank(bankAcc.getId()) && !StringUtils.equals(vo.getId(), bankAcc.getId())) {
            throw new ParameterException(ForeignMessageEnum.FEX_COTP_BANKACC_REPEAT);
        }
    }

    /**
     * 保存交易对手账户数据
     *
     * @param cotpBankAccDO
     * @return
     */
    public String saveCotpBankAcc(FexCotpBankAcc cotpBankAccDO, Boolean editFlag) {
        if (!editFlag) {
            cotpBankAccDO.setId(null);
        }
        if (StringUtils.isNotBlank(cotpBankAccDO.getId())) {
            fexCotpBankAccDao.delete(cotpBankAccDO.getId());
        }
        FexCotpBankAcc saveCotpDO = cotpBankAccDO;
        fexCotpBankAccDao.save(saveCotpDO);
        return saveCotpDO.getId();
    }

    /**
     * 审批前调用方法
     * @param workFlowResult
     * @return
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }

    /**
     * 保存
     * @param entity
     */
    public void save(FexCotpBankAcc entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     * @param entity
     */
    public void update(FexCotpBankAcc entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     * @param entity
     */
    public void saveOrUpdate(FexCotpBankAcc entity) {
        super.saveOrUpdate(entity);
    }

    @Override
    public BaseDao<FexCotpBankAcc, String> getEntityDao() {
        return fexCotpBankAccDao;
    }

    private FexCotpBankAccVO getExternalCorpBankAcc(String id) {
        if (StringUtils.isBlank(id) || externalCorpService == null) {
            return null;
        }
        List<String> ids = new ArrayList<>();
        ids.add(id);
        Map<String, ExternalCorpBankAccDTO> map = externalCorpService.queryFexExternalCorpBankAccMapByIds(ids);
        return EmptyUtils.isEmpty(map) ? null : externalCorpBankAccToFexCotpBankAcc(map.get(id));
    }

    private List<FexCotpBankAccVO> getExternalCorpBankAccList(String cotpId, String curCode, String bankId) {
        if (StringUtils.isBlank(cotpId) || externalCorpService == null) {
            return new ArrayList<>();
        }
        List<ExternalCorpBankAccDTO> bankAccList = getExternalCorpBankAccList(cotpId);
        List<FexCotpBankAccVO> list = new ArrayList<>();
        if (EmptyUtils.isEmpty(bankAccList)) {
            return list;
        }
        for (ExternalCorpBankAccDTO bankAcc : bankAccList) {
            FexCotpBankAccVO vo = externalCorpBankAccToFexCotpBankAcc(bankAcc);
            if (vo == null) {
                continue;
            }
            if (StringUtils.isNotBlank(curCode) && StringUtils.isNotBlank(vo.getCurId())
                    && !vo.getCurId().contains(curCode)) {
                continue;
            }
            if (StringUtils.isNotBlank(bankId) && StringUtils.isNotBlank(vo.getBankId())
                    && !bankId.equals(vo.getBankId())) {
                continue;
            }
            list.add(vo);
        }
        return list;
    }

    private FexCotpBankAccVO externalCorpBankAccToFexCotpBankAcc(ExternalCorpBankAccDTO dto) {
        if (dto == null) {
            return null;
        }
        FexCotpBankAccVO vo = new FexCotpBankAccVO();
        vo.setId(dto.getId());
        vo.setCotpId(dto.getExternalCorpId());
        vo.setBankAcc(dto.getExternalAcc());
        vo.setAccName(dto.getExternalAccName());
        vo.setBankName(dto.getBank());
        vo.setBankAddr(dto.getBankAddr());
        vo.setBankId(dto.getBankCountryId());
        vo.setBicCode(dto.getSwiftCode());
        vo.setPayType(dto.getPayExcType());
        vo.setTranBank(dto.getIntermediaryBank());
        vo.setTranBankName(dto.getIntermediaryBank());
        vo.setTranBicCode(dto.getIntermediaryBankSwiftCode());
        vo.setTranBankAcc(dto.getIntermediaryBankAcc());
        vo.setTranBankAddr(dto.getIntermediaryBankAddress());
        vo.setCurId(dto.getCurId());
        vo.setCurName(getCurName(dto.getCurId()));
        vo.setLocalClearNo(dto.getLocalClearNo());
        vo.setRmk(dto.getRemark());
        return vo;
    }

    private FexCotpBankAccVO externalDetailToFexCotpBankAcc(ExternalCorpBankAccDetailDto dto) {
        FexCotpBankAccVO vo = new FexCotpBankAccVO();
        vo.setId(dto.getId());
        vo.setCotpId(dto.getExternalCorpId());
        vo.setCotpName(dto.getName());
        vo.setCorpId(dto.getCorpId());
        vo.setCorpName(dto.getCorpName());
        vo.setBankAcc(dto.getExternalAcc());
        vo.setBankName(dto.getBank());
        vo.setBankId(dto.getBankType());
        return vo;
    }

    private String getCurName(String curId) {
        if (StringUtils.isBlank(curId) || btCurrencyService == null) {
            return null;
        }
        Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrencyMap();
        StringBuilder curName = new StringBuilder();
        for (String id : curId.split(CommonConstant.COMMA_SPLIT)) {
            BtCurrencyDTO currencyDTO = curMap.get(id);
            if (currencyDTO != null) {
                if (curName.length() > 0) {
                    curName.append(CommonConstant.COMMA_SPLIT);
                }
                curName.append(currencyDTO.getEnglishCode());
            }
        }
        return curName.toString();
    }

    private List<ExternalCorpBankAccDTO> getExternalCorpBankAccList(String externalCorpId) {
        List<ExternalCorpDTO> externalCorpList = externalCorpService.queryAll();
        if (EmptyUtils.isEmpty(externalCorpList)) {
            return new ArrayList<>();
        }
        for (ExternalCorpDTO externalCorp : externalCorpList) {
            if (externalCorp != null && externalCorpId.equals(externalCorp.getId())) {
                return externalCorp.getExternalCorpBankAccList() == null
                        ? new ArrayList<ExternalCorpBankAccDTO>() : externalCorp.getExternalCorpBankAccList();
            }
        }
        return new ArrayList<>();
    }

    public void updateExternalCorpInfo(FexCashFlowVO fexCashFlow, ICurrentUser user) {
        if (fexCashFlow == null || StringUtils.isBlank(fexCashFlow.getCotpId()) || externalCorpService == null) {
            return;
        }
        ExternalCorpDTO dto = new ExternalCorpDTO();
        dto.setId(fexCashFlow.getCotpId());
        ExternalCorpBankAccDTO bankAccDTO = new ExternalCorpBankAccDTO();
        bankAccDTO.setExternalAcc(fexCashFlow.getCotpBankAcc());
        bankAccDTO.setExternalAccName(fexCashFlow.getCotpAccName());
        bankAccDTO.setBank(fexCashFlow.getCotpBank());
        bankAccDTO.setSwiftCode(fexCashFlow.getBicCode());
        bankAccDTO.setPayExcType(fexCashFlow.getPayType());
        bankAccDTO.setIntermediaryBank(fexCashFlow.getTranBank());
        bankAccDTO.setIntermediaryBankSwiftCode(fexCashFlow.getTranBicCode());
        bankAccDTO.setIntermediaryBankAddress(fexCashFlow.getTranBankAddr());
        bankAccDTO.setIntermediaryBankAcc(fexCashFlow.getTranBankAcc());
        bankAccDTO.setCurId(fexCashFlow.getCurId());
        externalCorpService.updateExternalCorpInfo(dto, bankAccDTO, user);
    }
}
