package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCotpDefaultAccDao;
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
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Title: FexCorpBankAccService
 * @Description: 交易对手默认银行账户service
 * @author fusb
 * @date 2020-02-17 10:31:56
 * @version 1.0
 */
@WorkFlow(FexWorkFlowConstant.FEX_COTP_DEFAULT_ACC)
@Service("fexCotpDefaultAccService")
@Transactional
public class FexCotpDefaultAccService extends BaseService<FexCorpBankAcc,String> implements IWorkFlowListener {
    @Autowired
    private FexCotpDefaultAccDao fexCotpDefaultAccDao;
    @Autowired
    private FexCorpBankAccDtlService fexCorpBankAccDtlService;
    @Autowired
    private FexCorpBankAccService fexCorpBankAccService;
    @Autowired(required = false)
    private ICorpService corpService;
    @Autowired(required=false)
    private IWorkFlowService workFlowService;
    @Autowired(required = false)
    private INotificationService notificationService;
    @Autowired(required = false)
    private FexMessageService fexMessageService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 交易对手默认银行账户列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = new ArrayList<>();
        Map<String, Object> params = dataTablesInput.getFormdata();
        if (null != params && null != params.get("eqs_oppCorpId") && StringUtils.isNotEmpty(params.get("eqs_oppCorpId").toString())) {
            corpAuth.addAll(Arrays.asList(params.get("eqs_oppCorpId").toString().split(",")));
        }
        params.remove("eqs_oppCorpId");
        dataTablesInput.setFormdata(params);
        List<String> externalCorpIds = resolveExternalCorpIdsByName(params, "likes_corpName");
//        fexCorpBankAccService.setDataTablesInput(dataTablesInput,"ins_corpId");
        DataTablesOutput output = fexCotpDefaultAccDao.getDataTablesUnion(dataTablesInput, corpAuth,
                ForeignConstant.FEX_BUS_TYPE_COTP, externalCorpIds);
        fillExternalCorpInfo(output);
        return output;
    }

    /**
     * 通过id查询交易对手默认银行账户
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public FexCorpBankAccVO getFexCorpBankAccById(String id){
        FexCorpBankAccVO fexCorpBankAccVO = fexCotpDefaultAccDao .getFexCorpBankAcc(id);
        fillExternalCorpInfoById(fexCorpBankAccVO);
        List<FexCorpBankAccDtlVO> fexCorpBankAccDtlVO = fexCorpBankAccDtlService.getFecFexCorpBankAccDtlVOById(id);
        fexCorpBankAccVO.setFexCorpBankAccDtl(fexCorpBankAccDtlVO);
        return fexCorpBankAccVO;
    }

    /**
     * 保存交易对手默认银行账户
     * @param fexCorpBankAccVO
     * param user
     * @return
     */
    public void saveFexCorpBankAcc(FexCorpBankAccVO fexCorpBankAccVO, ICurrentUser user) {
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
            entity.setCorpId(fexCorpBankAccVO.getOppId());
        }
        //判断交易对手与单位共享
        if(fexCorpBankAccVO.getTranType().equals("4") && !EmptyUtils.isEmpty(entity.getOppCorpId()) && !EmptyUtils.isEmpty(fexCorpBankAccVO.getCorpName())) {
            fexCorpBankAccService.makeExternalWithCorp(fexCorpBankAccVO.getOppId(), entity.getOppCorpId(), fexCorpBankAccVO.getCorpName());
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
            //调用审批后方法
            getStartHandler().handler(FexWorkFlowConstant.FEX_COTP_DEFAULT_ACC, entity.getId(), entity, user.getCorpId(), user.getId());
            // 完成通知提醒
            notificationService.complete(NotificationConstant.FOREIGN.FEX_COTP_DEFAULTACC_BACK,entity.getId(),user.getId());
        }
        if(user !=null) {
            //生成日志
            fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_CORP_BANKACC_1, user);
        }
    }

    /**
     * 删除交易对手默认银行账户
     * @param id
     * @return
     */
    public void deleteFexCorpBankAcc(String id){
        fexCotpDefaultAccDao .deleteFexCorpBankAcc(id);
        fexCorpBankAccDtlService.deleteFexCorpBankAccDtl(id);
    }

    /**
     * 查询交易对手默认银行账户审批列表
     *
     * @param input
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getApproveDataTables(DataTablesInput input,ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.
                getWorkFlowBusiness(FexWorkFlowConstant.FEX_COTP_DEFAULT_ACC, input);
        Map<String, Object> params = input.getFormdata();
        List<String> corpIds = new ArrayList<>();
        if (null != params && null != params.get("eqs_oppCorpId") &&
                StringUtils.isNotEmpty(params.get("eqs_oppCorpId").toString()) &&
                !params.get("eqs_oppCorpId").toString().contains("all")) {
            corpIds.addAll(Arrays.asList(params.get("eqs_oppCorpId").toString().split(",")));
        }
        params.remove("eqs_oppCorpId");
        List<String> externalCorpIds = resolveExternalCorpIdsByName(params, "likes_corpName");
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        DataTablesOutput dp = fexCotpDefaultAccDao.getApproveDataTables(input, businessSql,
                ForeignConstant.FEX_BUS_TYPE_COTP, corpIds, externalCorpIds);
        fillExternalCorpInfo(dp);
        return workFlowBusiness.merge(dp);
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
        List<ExternalCorpDTO> externalCorps = externalCorpService.findExternalCorpLikeName(name.toString());
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
     * 为分页结果补充交易对手名称和币种名称。
     */
    private void fillExternalCorpInfo(DataTablesOutput output) {
        if (output == null) {
            return;
        }
        fillExternalCorpInfo(output.getData());
    }

    /**
     * 详情页按 ID 查询外部单位，不按启用状态过滤，避免历史或停用客商无法回显名称。
     */
    private void fillExternalCorpInfoById(FexCorpBankAccVO vo) {
        String externalCorpId = getExternalCorpId(vo);
        if (StringUtils.isBlank(externalCorpId)) {
            fillCurrencyName(vo);
            return;
        }
        ExternalCorpDTO externalCorp = externalCorpService.getById(externalCorpId);
        fillExternalCorpInfo(vo, externalCorp);
        fillCurrencyName(vo);
    }

    /**
     * 批量从 bytter-system 查询外部单位和币种，避免逐条 Dubbo 调用。
     * 批量接口只返回启用客商，未命中的历史或停用客商再按 ID 回退查询，保证名称可回显。
     */
    private void fillExternalCorpInfo(List<FexCorpBankAccVO> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }
        List<String> ids = new ArrayList<String>();
        List<String> curIds = new ArrayList<String>();
        for (FexCorpBankAccVO vo : list) {
            String externalCorpId = getExternalCorpId(vo);
            if (StringUtils.isNotBlank(externalCorpId) && !ids.contains(externalCorpId)) {
                ids.add(externalCorpId);
            }
            if (vo != null && StringUtils.isNotBlank(vo.getCurId()) && !curIds.contains(vo.getCurId())) {
                curIds.add(vo.getCurId());
            }
        }
        Map<String, ExternalCorpDTO> externalCorpMap = EmptyUtils.isEmpty(ids)
                ? Collections.<String, ExternalCorpDTO>emptyMap()
                : externalCorpService.queryFexExternalCorpMapByIds(ids);
        Map<String, BtCurrencyDTO> currencyMap = EmptyUtils.isEmpty(curIds)
                ? Collections.<String, BtCurrencyDTO>emptyMap()
                : btCurrencyService.getCurrencyByIds(curIds);
        for (FexCorpBankAccVO vo : list) {
            String externalCorpId = getExternalCorpId(vo);
            ExternalCorpDTO externalCorp = EmptyUtils.isEmpty(externalCorpMap) ? null : externalCorpMap.get(externalCorpId);
            if (externalCorp == null && StringUtils.isNotBlank(externalCorpId)) {
                externalCorp = externalCorpService.getById(externalCorpId);
            }
            fillExternalCorpInfo(vo, externalCorp);
            fillCurrencyName(vo, currencyMap);
        }
    }

    /**
     * 交易对手默认账户中 corpId 承载 system 外部单位 ID。
     */
    private String getExternalCorpId(FexCorpBankAccVO vo) {
        if (vo == null) {
            return null;
        }
        if (StringUtils.isNotBlank(vo.getCorpId())) {
            return vo.getCorpId();
        }
        return vo.getOppId();
    }

    /**
     * 根据外部单位信息回填交易对手展示字段。
     */
    private void fillExternalCorpInfo(FexCorpBankAccVO vo, ExternalCorpDTO externalCorp) {
        if (vo == null || externalCorp == null) {
            return;
        }
        vo.setOppId(externalCorp.getId());
        vo.setCorpName(externalCorp.getName());
        vo.setCotpNameEn(externalCorp.getNameEn());
    }

    /**
     * 按单条记录的币种 ID 补充币种名称，供详情场景使用。
     */
    private void fillCurrencyName(FexCorpBankAccVO vo) {
        if (vo == null || StringUtils.isBlank(vo.getCurId())) {
            return;
        }
        List<String> curIds = new ArrayList<String>();
        curIds.add(vo.getCurId());
        fillCurrencyName(vo, btCurrencyService.getCurrencyByIds(curIds));
    }

    /**
     * 根据已查询的币种 Map 回填展示名称。
     */
    private void fillCurrencyName(FexCorpBankAccVO vo, Map<String, BtCurrencyDTO> currencyMap) {
        if (vo == null || EmptyUtils.isEmpty(currencyMap)) {
            return;
        }
        BtCurrencyDTO currency = currencyMap.get(vo.getCurId());
        if (currency != null) {
            vo.setCurName(StringUtils.isNotBlank(currency.getEnglishCode()) ? currency.getEnglishCode() : currency.getCurName());
        }
    }

    /**
     * 修改交易对手默认银行账户信息状态
     * @param id
     * @param status
     */
    public void updateFexCotpDefaultAccSatus(String id,Integer status){
        fexCotpDefaultAccDao.updateFexCotpDefaultAccSatus(id,status);
    }

    /**
     * 审批完成后调用函数
     * @param workFlowResult
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String bussinessKey = workFlowResult.getBusinessKey();
        FexCorpBankAcc fexCotpBankAcc = this.get(bussinessKey);
        boolean result = workFlowResult.isPass();// 是否通过
        ICurrentUser user = workFlowResult.getUser();
        Integer messageType = 0;
        String message = "";
        if (result) {
            fexCotpBankAcc.setStatus(ForeignConstant.STATUE_CHECK);
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            message = FexMessageConstant.FEX_LOG_APP_SUCC;
            //审批通过时,发出通知告知已审批通过
            notificationService.send(fexCotpBankAcc.getId(),fexCotpBankAcc.getCorpId(), NotificationConstant.FOREIGN.FEX_COTP_DEFAULTACC_PASS, fexCotpBankAcc);
        } else {
            fexCotpBankAcc.setStatus(ForeignConstant.STATUE_BACK);
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
            message = FexMessageConstant.FEX_LOG_APP_BACK;
            //审批未通过时,发出待处理消息告知已打回
            notificationService.send(fexCotpBankAcc.getId(),fexCotpBankAcc.getCorpId(),NotificationConstant.FOREIGN.FEX_COTP_DEFAULTACC_BACK, fexCotpBankAcc);
        }
        //生成日志
        fexMessageService.saveMessage(fexCotpBankAcc.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_CORP_BANKACC_1, user);
        this.updateEntity(fexCotpBankAcc);
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
        return fexCotpDefaultAccDao ;
    }

}
