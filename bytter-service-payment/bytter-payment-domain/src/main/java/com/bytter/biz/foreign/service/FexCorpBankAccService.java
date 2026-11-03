package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCorpBankAccDao;
import com.bytter.biz.foreign.entity.FexCorpBankAcc;
import com.bytter.biz.foreign.vo.FexCorpBankAccDtlVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
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
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Title: FexCorpBankAccService
 * @Description: 主体单位默认银行账户service
 * @author fusb
 * @date 2020-02-17 10:31:56
 * @version 1.0
 */
@WorkFlow(FexWorkFlowConstant.FEX_CORP_BANK_ACC)
@Service("fexCorpBankAccService")
@Transactional
public class FexCorpBankAccService extends BaseService<FexCorpBankAcc,String> implements IWorkFlowListener {
    @Autowired
    private FexCorpBankAccDao fexCorpBankAccDao;
    @Autowired
    private FexCorpBankAccDtlService fexCorpBankAccDtlService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired(required = false)
    private FexMessageService fexMessageService;
    @DubboReference(check = false)
    private IExternalCorpService iExternalCorpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 主体单位默认银行账户列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        // 根据资源权限获取单位
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.FOREIGN_FEX_CORP_BANKACC_QUERY, user.getId());
        Map<String, Object> params = dataTablesInput.getFormdata();
        if (null != params && null != params.get("eqs_corpId") &&
                StringUtils.isNotEmpty(params.get("eqs_corpId").toString())) {
            corpAuth = new ArrayList<>();
            corpAuth.addAll(Arrays.asList(params.get("eqs_corpId").toString().split(",")));
        }
        params.remove("eqs_corpId");
        dataTablesInput.setFormdata(params);
        List<String> externalCorpIds = resolveExternalCorpIdsByName(params, "likes_oppCorpName");
        this.setDataTablesInput(dataTablesInput,"ins_oppCorpId");
        DataTablesOutput output = fexCorpBankAccDao.getDataTablesUnion(dataTablesInput, corpAuth,
                ForeignConstant.FEX_BUS_TYPE_CORP, externalCorpIds);
        fillCurrencyInfo(output);
        fillExternalCorpInfo(output);
        return output;
    }

    /**
     * 为列表数据批量回填币种名称。
     *
     * @param output 主体单位默认银行账户列表
     */
    private void fillCurrencyInfo(DataTablesOutput output) {
        if (output == null || EmptyUtils.isEmptyList(output.getData())) {
            return;
        }
        Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
        if (EmptyUtils.isEmpty(currencyMap)) {
            return;
        }
        for (Object data : output.getData()) {
            FexCorpBankAccVO vo = (FexCorpBankAccVO) data;
            fillCurrencyInfo(vo, currencyMap);
        }
    }

    /**
     * 根据币种缓存为主体单位默认银行账户回填币种名称。
     * 优先使用币种英文编码，英文编码为空时使用币种名称。
     *
     * @param vo 主体单位默认银行账户
     * @param currencyMap 以币种ID为键的币种缓存
     */
    private void fillCurrencyInfo(FexCorpBankAccVO vo, Map<String, BtCurrencyDTO> currencyMap) {
        if (vo == null || EmptyUtils.isEmpty(currencyMap)) {
            return;
        }
        BtCurrencyDTO currency = currencyMap.get(vo.getCurId());
        if (currency != null) {
            vo.setCurName(StringUtils.isNotBlank(currency.getEnglishCode())
                    ? currency.getEnglishCode()
                    : currency.getCurName());
        }
    }

    /**
     * 通过id查询主体单位默认银行账户
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public FexCorpBankAccVO getFexCorpBankAccById(String id){
        FexCorpBankAccVO fexCorpBankAccVO = fexCorpBankAccDao.getFexCorpBankAcc(id);
        fillCurrencyInfo(fexCorpBankAccVO, btCurrencyService.getCurrencyMap());
        fillExternalCorpInfoById(fexCorpBankAccVO);
        List<FexCorpBankAccDtlVO> fexCorpBankAccDtlVO = fexCorpBankAccDtlService.getFecFexCorpBankAccDtlVOById(id);
        fexCorpBankAccVO.setFexCorpBankAccDtl(fexCorpBankAccDtlVO);
        return fexCorpBankAccVO;
    }

    /**
     * 保存主体单位默认银行账户
     * @param fexCorpBankAccVO
     * param user
     * @return
     */
    public void saveFexCorpBankAcc(FexCorpBankAccVO fexCorpBankAccVO, ICurrentUser user) {
        saveExternalCorpIfNotExist(fexCorpBankAccVO);
        FexCorpBankAcc entity = new FexCorpBankAcc();
        BeanUtils.copyNotNullProperties(fexCorpBankAccVO, entity);

        if (StringUtils.isBlank(fexCorpBankAccVO.getId())) { //新增
            entity.setCreateBy(user.getId());
            entity.setCreateDate(DateUtils.getDate());

        } else { //修改
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());

        }
        if (!EmptyUtils.isEmpty(fexCorpBankAccVO.getOppId())) {
            entity.setOppCorpId(fexCorpBankAccVO.getOppId());
        }
        //判断交易对手与单位共享
        if(ForeignConstant.TRAN_TYPE_4.equals(fexCorpBankAccVO.getTranType()) && !EmptyUtils.isEmpty(entity.getCorpId()) && !EmptyUtils.isEmpty(fexCorpBankAccVO.getOppCorpName())) {
            this.makeExternalWithCorp(fexCorpBankAccVO.getOppId(), entity.getCorpId(), fexCorpBankAccVO.getOppCorpName());
        }
        //保存或更新
        this.saveOrUpdate(entity);
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        //保存子表信息
        fexCorpBankAccDtlService.saveFexBankAccDtl(fexCorpBankAccVO,entity.getId());
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_ADD_SUBMIT_SUCC;
            fexCorpBankAccVO.setId(entity.getId());
            fexCorpBankAccVO.setOppCorpId(entity.getOppCorpId());
            //调用审批后方法
            getStartHandler().handler(FexWorkFlowConstant.FEX_CORP_BANK_ACC, entity.getId(), fexCorpBankAccVO,
                    entity.getCorpId(), user.getId());
            //完成通知提醒
            notificationService.complete(NotificationConstant.FOREIGN.FEX_CORP_BANKACC_BACK,entity.getId(),user.getId());
        }
        if(user !=null) {
            //生成日志
            fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_CORP_BANKACC_0, user);
        }
    }

    /**
     * 检查资金收付交易的客商是否存在，不存在时新增客商及其银行账户，
     * 并将客商ID回填到主体单位默认银行账户。
     *
     * @param vo 主体单位默认银行账户
     */
    private void saveExternalCorpIfNotExist(FexCorpBankAccVO vo) {
        if (vo == null || !ForeignConstant.TRAN_TYPE_4.equals(vo.getTranType())
                || StringUtils.isBlank(vo.getCorpId()) || StringUtils.isBlank(vo.getOppCorpName())) {
            return;
        }
        ExternalCorpDTO externalCorp = iExternalCorpService.findByCorpIdAndName(vo.getCorpId(), vo.getOppCorpName());
        if (externalCorp == null) {
            ExternalCorpDTO externalCorpDTO = buildExternalCorpDTO(vo);
            if (EmptyUtils.isEmptyList(externalCorpDTO.getExternalCorpBankAccList())) {
                return;
            }
            iExternalCorpService.saveIfNotExist(externalCorpDTO);
            externalCorp = iExternalCorpService.findByCorpIdAndName(vo.getCorpId(), vo.getOppCorpName());
        }
        if (externalCorp != null && StringUtils.isNotBlank(externalCorp.getId())) {
            vo.setOppId(externalCorp.getId());
            vo.setOppCorpId(externalCorp.getId());
        }
    }

    /**
     * 根据主体单位默认银行账户信息组装待新增的客商资料。
     *
     * @param vo 主体单位默认银行账户
     * @return 客商资料
     */
    private ExternalCorpDTO buildExternalCorpDTO(FexCorpBankAccVO vo) {
        ExternalCorpDTO externalCorpDTO = new ExternalCorpDTO();
        externalCorpDTO.setCorpId(vo.getCorpId());
        externalCorpDTO.setName(vo.getOppCorpName());
        externalCorpDTO.setNameEn(vo.getCotpNameEn());
        externalCorpDTO.setStatus(1);
        externalCorpDTO.setExternalType(CommonConstant.NO);
        externalCorpDTO.setIsCommon(CommonConstant.NO);
        List<ExternalCorpBankAccDTO> bankAccList = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(vo.getFexCorpBankAccDtl())) {
            for (FexCorpBankAccDtlVO detail : vo.getFexCorpBankAccDtl()) {
                if (detail == null || StringUtils.isBlank(detail.getBankAcc())) {
                    continue;
                }
                ExternalCorpBankAccDTO bankAcc = new ExternalCorpBankAccDTO();
                bankAcc.setExternalAcc(detail.getBankAcc());
                bankAcc.setExternalAccName(detail.getAccName());
                bankAcc.setBank(detail.getBankTypeName());
                bankAcc.setBankType(detail.getBankType());
                bankAcc.setCurId(detail.getCurId());
                bankAcc.setLocalClearNo(detail.getLocalClearNo());
                bankAcc.setAccProvince(detail.getProv());
                bankAcc.setAccAddress(detail.getBankAdd());
                bankAcc.setIsDefaultAcc(CommonConstant.NO);
                bankAccList.add(bankAcc);
            }
        }
        externalCorpDTO.setExternalCorpBankAccList(bankAccList);
        return externalCorpDTO;
    }

    public void makeExternalWithCorp(String externalId,String corpId,String externalName){
       List<String> corpList= iExternalCorpService.getCorpIdsByExternalCorpId(externalId);
        if(!corpList.contains(corpId)){
            CorpDTO corpDTO = corpService.get(corpId);
            String corpName = null == corpDTO ? corpId : corpDTO.getName();
            throw new ParameterException(ForeignMessageEnum.FEX_EXTERNAL_NOT_WITH_CORP,externalName,corpName);
        }
    }

    /**
     * 删除主体单位默认银行账户
     * @param id
     * @return
     */
    public void deleteFexCorpBankAcc(String id){
        fexCorpBankAccDao.deleteFexCorpBankAcc(id);
        fexCorpBankAccDtlService.deleteFexCorpBankAccDtl(id);
    }

    /**
     * 修改主体单位默认银行账户状态
     * @param id
     * @param status
     */
    public void updateFexCorpBankAccSatus(String id,Integer status){
        fexCorpBankAccDao.updateFexCorpBankAccSatus(id,status);
    }

    /**
     * 查询主体单位默认银行账户审批列表
     *
     * @param input
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getApproveDataTables(DataTablesInput input,ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.
                getWorkFlowBusiness(FexWorkFlowConstant.FEX_CORP_BANK_ACC, input);
        List<String> corpIds = new ArrayList<>();
        Map<String, Object> params = input.getFormdata();
        if (null != params && null != params.get("eqs_corpId") &&
                StringUtils.isNotEmpty(params.get("eqs_corpId").toString()) &&
                !params.get("eqs_corpId").toString().contains("all")) {
            corpIds.addAll(Arrays.asList(params.get("eqs_corpId").toString().split(",")));
        }
        params.remove("eqs_corpId");
        input.setFormdata(params);
        List<String> externalCorpIds = resolveExternalCorpIdsByName(params, "likes_oppCorpName");
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        this.setDataTablesInput(input,"ins_oppCorpId");
        DataTablesOutput dp = fexCorpBankAccDao.getApproveDataTables(input, businessSql,
                ForeignConstant.FEX_BUS_TYPE_CORP, corpIds, externalCorpIds);
        fillCurrencyInfo(dp);
        fillExternalCorpInfo(dp);
        return workFlowBusiness.merge(dp);
    }

    /**
     * 审批完成后调用函数
     * @param workFlowResult
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String bussinessKey = workFlowResult.getBusinessKey();
        FexCorpBankAcc fexCorpBankAcc = this.get(bussinessKey);
        boolean result = workFlowResult.isPass();// 是否通过
        ICurrentUser user = workFlowResult.getUser();
        Integer messageType = 0;
        String message = "";
        if (result) {
            fexCorpBankAcc.setStatus(ForeignConstant.STATUE_CHECK);
            //审批通过时,发出通知告知已审批通过
            notificationService.send(fexCorpBankAcc.getId(),fexCorpBankAcc.getCorpId(), NotificationConstant.FOREIGN.FEX_CORP_BANKACC_PASS, fexCorpBankAcc);
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            message = FexMessageConstant.FEX_LOG_APP_SUCC;
        } else {
            fexCorpBankAcc.setStatus(ForeignConstant.STATUE_BACK);
            //审批未通过时,发出待处理消息告知已打回
            notificationService.send(fexCorpBankAcc.getId(),fexCorpBankAcc.getCorpId(),NotificationConstant.FOREIGN.FEX_CORP_BANKACC_BACK, fexCorpBankAcc);
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
            message = FexMessageConstant.FEX_LOG_APP_BACK;
        }
        //生成日志
        fexMessageService.saveMessage(fexCorpBankAcc.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_CORP_BANKACC_0, user);
        this.updateEntity(fexCorpBankAcc);
    }

    /**
     * 获取主体单位默认银行信息下拉框数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<FexCorpBankAccVO> getSelectFexCorpBankAcc(String corpId){
        List<FexCorpBankAccVO> list = fexCorpBankAccDao.getSelectFexCorpBankAcc(corpId,ForeignConstant.FEX_BUS_TYPE_CORP);
        fillExternalCorpInfo(list);
        return list;
    }

    /**
     *根据单位id，别种，银行类别查询主体单位默认银行信息
     *@Param corpId
     *@Param curCode
     *@Param bankTypeId
     *@return
     */
    public  List<FexCorpBankAccVO> getFexCorpBankAccList(String corpId,String curCode,String bankTypeId ){
        List<FexCorpBankAccVO> list = fexCorpBankAccDao.getFexCorpBankAccList(corpId,curCode);
        fillExternalCorpInfo(list);
        for (int i = 0;i< list.size();i++) {
            list.get(i).setFexCorpBankAccDtl(fexCorpBankAccDtlService.getFecFexCorpBankAccDtlList(list.get(i).getId(),bankTypeId));
        }
        return list;
    }

    /**
     * 将页面上的交易对手名称筛选转换成 system 外部单位 ID 筛选。
     * 这样分页和总数仍由 payment 本服务 SQL 计算，同时避免跨服务直接查 sys_external_corp。
     */
    private List<String> resolveExternalCorpIdsByName(Map<String, Object> params, String field) {
        if (params == null || StringUtils.isBlank(field)) {
            return null;
        }
        Object name = params.remove(field);
        if (name == null || StringUtils.isBlank(name.toString())) {
            return null;
        }
        List<ExternalCorpDTO> externalCorps = iExternalCorpService.findFexCounterpartyLikeName(name.toString());
        List<String> ids = new ArrayList<String>();
        if (EmptyUtils.isEmpty(externalCorps)) {
            return ids;
        }
        for (ExternalCorpDTO externalCorp : externalCorps) {
            if (externalCorp != null && StringUtils.isNotBlank(externalCorp.getId())
                    && !ids.contains(externalCorp.getId())) {
                ids.add(externalCorp.getId());
            }
        }
        return ids;
    }

    /**
     * 为分页结果补充交易对手名称等展示字段。
     */
    private void fillExternalCorpInfo(DataTablesOutput output) {
        if (output == null) {
            return;
        }
        fillExternalCorpInfo(output.getData());
    }

    /**
     * 为单条银行账户配置补充交易对手名称等展示字段。
     */
    private void fillExternalCorpInfo(FexCorpBankAccVO vo) {
        if (vo == null) {
            return;
        }
        List<FexCorpBankAccVO> list = new ArrayList<FexCorpBankAccVO>();
        list.add(vo);
        fillExternalCorpInfo(list);
    }

    /**
     * 详情页按数据源ID查询交易对手，兼容客商、单位、境内银行和境外银行。
     */
    private void fillExternalCorpInfoById(FexCorpBankAccVO vo) {
        String externalCorpId = getExternalCorpId(vo);
        if (StringUtils.isBlank(externalCorpId)) {
            return;
        }
        Map<String, ExternalCorpDTO> counterpartyMap = iExternalCorpService.
                queryFexCounterpartyMapByIds(Arrays.asList(externalCorpId));
        ExternalCorpDTO externalCorp = EmptyUtils.isEmpty(counterpartyMap) ? null : counterpartyMap.get(externalCorpId);
        if (externalCorp != null && StringUtils.isNotBlank(externalCorp.getId())) {
            vo.setOppId(externalCorp.getId());
            vo.setOppCorpName(externalCorp.getName());
            vo.setCotpNameEn(externalCorp.getNameEn());
        }
    }

    /**
     * 批量从 bytter-system 查询交易对手展示信息，避免逐条 Dubbo 调用。
     */
    private void fillExternalCorpInfo(List<FexCorpBankAccVO> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }
        List<String> ids = new ArrayList<String>();
        for (FexCorpBankAccVO vo : list) {
            String externalCorpId = getExternalCorpId(vo);
            if (StringUtils.isNotBlank(externalCorpId) && !ids.contains(externalCorpId)) {
                ids.add(externalCorpId);
            }
        }
        if (EmptyUtils.isEmpty(ids)) {
            return;
        }
        Map<String, ExternalCorpDTO> externalCorpMap = iExternalCorpService.queryFexCounterpartyMapByIds(ids);
        for (FexCorpBankAccVO vo : list) {
            String externalCorpId = getExternalCorpId(vo);
            ExternalCorpDTO externalCorp = EmptyUtils.isEmpty(externalCorpMap) ? null : externalCorpMap.get(externalCorpId);
            if (externalCorp != null && StringUtils.isNotBlank(externalCorp.getId())) {
                vo.setOppId(externalCorp.getId());
                vo.setOppCorpName(externalCorp.getName());
                vo.setCotpNameEn(externalCorp.getNameEn());
            }
        }
    }

    /**
     * 获取外部单位 ID；历史 VO 中 oppId 和 oppCorpId 都可能承载该值。
     */
    private String getExternalCorpId(FexCorpBankAccVO vo) {
        if (vo == null) {
            return null;
        }
        if (StringUtils.isNotBlank(vo.getOppCorpId())) {
            return vo.getOppCorpId();
        }
        return vo.getOppId();
    }

    /**
     * 设置查询条件
     * @param input
     * @return
     */
    public void setDataTablesInput(DataTablesInput input,String cotp){
        if (input == null || EmptyUtils.isEmpty(input.getFormdata()) || StringUtils.isBlank(cotp)) {
            return;
        }
        Object cotpValue = input.getFormdata().get(cotp);
        if (cotpValue == null || StringUtils.isBlank(cotpValue.toString())) {
            return;
        }
        List<String> ids = new ArrayList<String>();
        String[] cotpIds = cotpValue.toString().split(",");
        for (String cotpId : cotpIds) {
            if (StringUtils.isNotBlank(cotpId)) {
                ids.add(cotpId.trim());
            }
        }
        if (EmptyUtils.isEmpty(ids)) {
            return;
        }
        Map<String, ExternalCorpBankAccDTO> bankAccMap = iExternalCorpService.queryFexExternalCorpBankAccMapByIds(ids);
        if (EmptyUtils.isEmpty(bankAccMap)) {
            return;
        }
        List<String> externalCorpIds = new ArrayList<String>();
        for (String id : ids) {
            ExternalCorpBankAccDTO bankAcc = bankAccMap.get(id);
            if (bankAcc != null && StringUtils.isNotBlank(bankAcc.getExternalCorpId())
                    && !externalCorpIds.contains(bankAcc.getExternalCorpId())) {
                externalCorpIds.add(bankAcc.getExternalCorpId());
            }
        }
        if (!EmptyUtils.isEmpty(externalCorpIds)) {
            input.getFormdata().put(cotp, join(externalCorpIds));
        }
    }

    private String join(List<String> list) {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            value.append(list.get(i));
            if (i < list.size() - 1) {
                value.append(",");
            }
        }
        return value.toString();
    }

    /**
     * 审批前调用函数
     * @param workFlowResult
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(FexCorpBankAcc entity) {
        super.deleteEntity(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }

    /**
     * 保存
     *
     * @param entity
     */
    public void save(FexCorpBankAcc entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(FexCorpBankAcc entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FexCorpBankAcc entity) {
        super.saveOrUpdate(entity);
    }

    @Override
    public BaseDao<FexCorpBankAcc, String> getEntityDao() {
        return fexCorpBankAccDao;
    }

}
