package com.bytter.finance.domain.inl.service;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.biz.payment.api.service.INisReceivableService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.dao.InlInfoDao;
import com.bytter.finance.domain.inl.dao.InlRepayDao;
import com.bytter.finance.domain.inl.dao.InlSendDao;
import com.bytter.finance.domain.inl.entity.CmsInlInfo;
import com.bytter.finance.domain.inl.entity.CmsInlRepay;
import com.bytter.finance.domain.inl.entity.CmsInlSend;
import com.bytter.finance.domain.inl.vo.CmsInlRepayVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.finance.domain.provide.dao.CmsProvideInterestDao;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.finance.domain.provide.vo.CmsProvideInterestVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.commons.collections.MapUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service("inlRepayService")
@WorkFlow(InlWorkFlowConstant.CMS_INL_REPAY)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.CMS_INL_REPAY)
public class InlRepayService extends BaseService<CmsInlRepay, String> implements IWorkFlowListener, ITaskService {
    @Autowired
    private InlRepayDao inlRepayDao;
    @Autowired
    private InlInfoDao inlInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Resource
    private IConService conCommonService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private IRatCpUrationService ratCpUrationService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private INisBillheadService nisBillHeadService;
    @DubboReference(check = false)
    private INisReceivableService nisReceivableService;
    @Autowired
    private CmsProvideInterestDao cmsProvideInterestDao;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private InlSendDao inlSendDao;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IBisExcService excService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private RepayTypeService repayTypeService;

    /**
     * @param input
     * @return
     */
    public DataTablesOutput datatable(DataTablesInput input, ICurrentUser user) {
        Map<String, Object> params = input.getFormdata();
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_INL_SEND_QUERY, user.getId());
        DataTablesOutput output = inlRepayDao.datatable(input, corpIds, params);
        List<CmsInlRepayVO> cmsInlRepayVOS = output.getData();

        Map<String, CorpDTO> corpMap = corpService.getCorpMap();
        for (CmsInlRepayVO cmsInlRepayVO : cmsInlRepayVOS) {
            if ((null == params.get("ged_nextRepayDate") || StringUtils.isEmpty(params.get("ged_nextRepayDate").toString())) && (null == params.get("led_nextRepayDate") || StringUtils.isEmpty(params.get("led_nextRepayDate").toString()))) {
                if (StringUtils.isNotEmpty(cmsInlRepayVO.getSendBillNO())) {
                    List<CmsProvideInterest> cmsProvideInterests = cmsProvideInterestDao.ListInterestList(cmsInlRepayVO.getSendBillNO());
                    if (ListUtils.isNotNullOrEmpty(cmsProvideInterests)) {
                        CmsProvideInterest cmsProvideInterest = cmsProvideInterests.get(0);
                        cmsInlRepayVO.setNextRepayDate(cmsProvideInterest.getRepayDate());
                        cmsInlRepayVO.setNextRepayMoney(cmsProvideInterest.getRepayMoney().add(cmsProvideInterest.getTotalInterest()));
                    }
                }
            }
            List<CmsInlRepayVO> cmsInlRepayVOS1 = inlRepayDao.getRepayInfoBySendId(cmsInlRepayVO.getInlSendId());
            if (ListUtils.isNotNullOrEmpty(cmsInlRepayVOS1)) {
                cmsInlRepayVO.setStatus(cmsInlRepayVOS1.get(0).getStatus());
            }
            //还款方式
            if(StringUtils.isNotEmpty(cmsInlRepayVO.getRepayType())){
                RepayTypeVO repayType= repayTypeService.getRepayTypeInfoById(cmsInlRepayVO.getRepayType());
                if(!EmptyUtils.isEmpty(repayType)) {
                    cmsInlRepayVO.setRepayTypeName(repayType.getRepayName());
                }
            }
            if (StringUtils.isNotBlank(cmsInlRepayVO.getBorrowInCorp())){
                cmsInlRepayVO.setBorrowInCorpName(corpMap.get(cmsInlRepayVO.getBorrowInCorp()).getName());
            }
            if (StringUtils.isNotBlank(cmsInlRepayVO.getBorrowOutCorp())){
                cmsInlRepayVO.setBorrowOutCorpName(corpMap.get(cmsInlRepayVO.getBorrowOutCorp()).getName());
            }
            if (StringUtils.isNotBlank(cmsInlRepayVO.getCurId())){
                BtCurrencyDTO dto = btCurrencyService.getObjectById(cmsInlRepayVO.getCurId());
                cmsInlRepayVO.setCurName(dto.getCurName());
            }
        }
        output.setData(cmsInlRepayVOS);
        return output;
    }
    /**
     * @param input
     * @return
     */
    public DataTablesOutput listRepayUpdateDataTable(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_INL_REPAY_QUERY, user.getId());
        return inlRepayDao.listRepayUpdateDataTable(input, corpIds);
    }
    /**
     * 查询待审批数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(InlWorkFlowConstant.CMS_INL_REPAY, input);
        return workFlowBusiness.merge(inlRepayDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList()));
    }
    /**
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listRepayConfirmDataTable(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_INL_REPAY_QUERY, user.getId());
        if (CollectionUtils.isEmpty(corpIds)) {
            return new DataTablesOutput();
        }
        DataTablesOutput output = inlRepayDao.listRepayConfirmDataTable(input, corpIds);
        List<CmsInlRepayVO> list = output.getData();
        List<CmsInlRepayVO> listResult = new ArrayList<CmsInlRepayVO>();
        if(CollectionUtils.isEmpty(list)) {
      	  return new DataTablesOutput();
        }
        List<String> repayIds = list.stream().map(cmsInlRepayVO -> cmsInlRepayVO.getId()).distinct().collect(Collectors.toList());
        List<NisBillheadDTO> listDTO = nisBillHeadService.listByErpIds(repayIds);
        List<String> accIds = null;
        List<String> headIds = null;
        List<BtBankAccDTO> listAcc = null;
        List<BisExcDTO> listBisExc = null;
        if(!CollectionUtils.isEmpty(listDTO)) {
      	  accIds = listDTO.stream().map(NisBillheadDTO -> NisBillheadDTO.getAccId()).distinct().collect(Collectors.toList());
            headIds = listDTO.stream().map(NisBillheadDTO -> NisBillheadDTO.getId()).distinct().collect(Collectors.toList());
        }
        if(!CollectionUtils.isEmpty(accIds)) {
      	  listAcc = btBankAccService.getBankAccByIdList(accIds);
        }
        if(!CollectionUtils.isEmpty(headIds)) {
      	  listBisExc = excService.getBisExcByCbsIdsWithCondition(headIds);
        }
        //转换成MAP
        Map<String, BtBankAccDTO> acctMap = null;
        Map<String, BisExcDTO> excMap = null;
        if(!CollectionUtils.isEmpty(listAcc)) {
      	  acctMap = listAcc.stream().distinct().collect(Collectors.toMap(BtBankAccDTO::getId, Function.identity(),(key1,key2) -> key2));
        }
        if(!CollectionUtils.isEmpty(listBisExc)) {
      	  excMap = listBisExc.stream().distinct().collect(Collectors.toMap(BisExcDTO::getCbsBillId, Function.identity(),(key1,key2) -> key2));
        }

        List<String> listCmsInlRepayId = new ArrayList<String>();
        for(NisBillheadDTO dto : listDTO)
        {
      	  String voucherStat = "";
      	  String isOnline = "";
      	  String isPayed = !StringUtils.isBlank(dto.getIsPayed()) ? dto.getIsPayed() : "";
      	  Integer status = dto.getStatus() > 0 ? dto.getStatus() : 0;
      	  String downAccId = dto.getDownAccId();
      	  if(MapUtils.isNotEmpty(excMap) && excMap.get(dto.getId())!=null)
      	  {
      		  voucherStat = ((BisExcDTO)excMap.get(dto.getId())).getVoucherStat();
      	  }
      	  if(MapUtils.isNotEmpty(acctMap) && acctMap.get(dto.getAccId())!=null) {
      		  isOnline = ((BtBankAccDTO)acctMap.get(dto.getAccId())).getIsOnline();
      	  }

      	  if(95 == status && "0".equals(voucherStat) && "1".equals(isOnline)) {//银行账号在线账号付款（线上）
      		listCmsInlRepayId.add(dto.getErpId());
      	  }else if("1".equals(isPayed) && 95 == dto.getStatus() && "1".equals(isOnline)) { //银行账号在线账号付款（线下）
      		listCmsInlRepayId.add(dto.getErpId());
      	  }else if( 95 == dto.getStatus() && "0".equals(isOnline)) {//银行账号离线账号付款（线下）
      		listCmsInlRepayId.add(dto.getErpId());
      	  }else if(100 == dto.getStatus()) {//内部账号付款
      		listCmsInlRepayId.add(dto.getErpId());
      	  }else {
      		  ///do nothing
      	  }
        }
        if(CollectionUtils.isEmpty(listCmsInlRepayId)) {
      	  return new DataTablesOutput();
        }else {
      	  Map<String, CorpDTO> mapCorp = corpService.getCorpMap();
      	  Map<String, BtCurrencyDTO> mapCur = btCurrencyService.getCurrency();
      	  Map<String, String> idMap = listCmsInlRepayId.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
      	  for(CmsInlRepayVO sendVo : list)
      	  {
      		  if(idMap.containsKey(sendVo.getId())) {
      			  //sendVo.setInfoBillNo(sendVo.getBillNo());
      			  sendVo.setCurName(mapCur.get(sendVo.getCurId()).getCurName());
      			  sendVo.setBorrowInCorpName(mapCorp.get(sendVo.getBorrowInCorp()).getName());
      			  sendVo.setBorrowOutCorpName(mapCorp.get(sendVo.getBorrowOutCorp()).getName());
      			  listResult.add(sendVo);
      		  }
      	  }
        }
        if(CollectionUtils.isEmpty(listResult)) {
      	  return new DataTablesOutput();
        }
        output.setData(listResult);
        output.setRecordsTotal(listResult.size());
        return output;
    }
    /**
     * 获取可查询的资金拆借单据id
     *
     * @param corpIds 有权限的单据id
     * @return 可查询的资金拆借单据id
     */
    private List<String> getIds(List<String> corpIds) {
        List<CmsInlRepayVO> repay = inlRepayDao.getListId(corpIds);
        //获取资金才接发送表单据id
        List<String> repayIds = repay.stream().map(CmsInlRepayVO::getId).distinct().collect(Collectors.toList());
        //根据单据id获取单据信息
        List<NisBillheadDTO> nisBillHeadList = nisBillHeadService.getCmsInlSendId(repayIds);
        //根据单据信息获取账号id
        List<String> accIds = nisBillHeadList.stream().map(NisBillheadDTO::getAccId).distinct().collect(Collectors.toList());
        //封装Map
        Map<String, String> billMapByAccId = nisBillHeadList.stream().collect(Collectors.toMap(NisBillheadDTO::getAccId, NisBillheadDTO::getId,(key1,key2) -> key2));
        //获取账号id
        List<String> accIdList = btBankAccService.getBankAccByAccId(accIds);
        if (CollectionUtils.isEmpty(accIdList)) {
            return null;
        }
        //获取查询指令单据id的集合
        List<String> billIds = new ArrayList<>();
        accIdList.forEach(item -> {
            if (billMapByAccId.containsKey(item) && StringUtil.isNotEmpty(billMapByAccId.get(item))) {
                billIds.add(billMapByAccId.get(item));
            }
        });
        if (CollectionUtils.isEmpty(billIds)) {
            return null;
        }
        //必须付款单据成功,获取指令表单据数据
        List<BisExcDTO> excDtoList = excService.getBisExcByCbsId(billIds);
        if (CollectionUtils.isEmpty(excDtoList)) {
            return null;
        }
        //组装map校验数据
        Map<String, String> billMapById = nisBillHeadList.stream().collect(Collectors.toMap(NisBillheadDTO::getId, NisBillheadDTO::getErpId,(key1,key2) -> key2));
        List<String> resultList = new ArrayList<>();
        billIds.forEach(entity -> {
            if (billMapById.containsKey(entity) && StringUtil.isNotEmpty(billMapById.get(entity))) {
                resultList.add(billMapById.get(entity));
            }
        });
        return resultList;
    }
    /**
     * 删除大户暂存数据
     *
     * @param id
     */
    public void deleteById(String id) {
        CmsInlRepay cmsInlRepay = inlRepayDao.get(id);
        cmsInlRepay.setStatus(InlConstant.DELETE);
        inlRepayDao.update(cmsInlRepay);
        /**
         * 删除资金计划
         */
        deleteBudgetProcess(cmsInlRepay);
    }
    /**
     * 保存数据
     *
     * @param cmsInlRepayVO
     * @param user
     */
    public void save(CmsInlRepayVO cmsInlRepayVO, ICurrentUser user) {
        CmsInlRepay cmsInlRepay;
        if (StringUtils.isNotEmpty(cmsInlRepayVO.getId())) {
            cmsInlRepay = inlRepayDao.get(cmsInlRepayVO.getId());
            if (CommonConstant.NO.equals(cmsInlRepayVO.getIsBudget()) && CommonConstant.YES.equals(cmsInlRepay.getIsBudget())) {
                /**
                 * 删除资金计划挂接
                 */
                deleteBudgetProcess(cmsInlRepay);
            }
            cmsInlRepayVO.setUpdateBy(user.getId());
            cmsInlRepayVO.setUpdateDate(new Date());
            BeanUtils.copyNotNullProperties(cmsInlRepayVO, cmsInlRepay);
        } else {
            cmsInlRepay = new CmsInlRepay();
            cmsInlRepayVO.setCreateBy(user.getId());
            cmsInlRepayVO.setCreateDate(new Date());
            cmsInlRepayVO.setBillNo(InlUtil.getInlRepayNo());
            BeanUtils.copyNotNullProperties(cmsInlRepayVO, cmsInlRepay);
        }
        inlRepayDao.saveOrUpdate(cmsInlRepay);
        cmsInlRepayVO.setId(cmsInlRepay.getId());
        /**
         * 其他业务处理
         */
        otherHandle(cmsInlRepay, cmsInlRepayVO, user);
        if (InlConstant.COMMIT_APPROVE == cmsInlRepay.getStatus()) {
            notificationService.complete(NotificationConstant.CMS.INL_REPAY, cmsInlRepay.getId(), user.getId());
             getStartHandler().handler(InlWorkFlowConstant.CMS_INL_REPAY, cmsInlRepay.getId(), cmsInlRepay, cmsInlRepay.getBorrowInCorp(), user.getId());
        }
    }
    /**
     * 其他业务处理
     *
     * @param cmsInlRepay
     * @param cmsInlRepayVO
     * @param user
     */
    private void otherHandle(CmsInlRepay cmsInlRepay, CmsInlRepayVO cmsInlRepayVO, ICurrentUser user) {
        /**
         * 判断是否挂接挂接资金计划
         */
        if (CommonConstant.YES.equals(getIsBudget().getValue()) && CommonConstant.YES.equals(cmsInlRepay.getIsBudget())) {
            budgetHandle(cmsInlRepayVO, user);
        }
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /* 判断业务主键是否为空 */
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        CmsInlRepay cmsInlRepay = inlRepayDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        if (isPass) {
            cmsInlRepay.setStatus(InlConstant.APPROVED);
            msService.insertProcessMsg(cmsInlRepay.getBillNo(), InlConstant.CMS_INL_REPAY_NO, CmsProcessMsgConstant.MSGA_CHECKOK, "审批通过", user);
        } else {
            cmsInlRepay.setStatus(InlConstant.BACK);
            /**
             * 增加消息提醒
             */
            notificationService.send(cmsInlRepay.getId(), cmsInlRepay.getBorrowInCorp(), NotificationConstant.CMS.INL_REPAY, cmsInlRepay);
            msService.insertProcessMsg(cmsInlRepay.getBillNo(), InlConstant.CMS_INL_REPAY_NO, CmsProcessMsgConstant.MSGA_ROLLBACK, "审批打回", user);
        }
        // 资金计划挂机处理
        updateBudget(cmsInlRepay, user);
        /**
         * 审批通过 1。生成付款单 2。生成收款单 4.合同履约
         */
        if (isPass) {
            /**
             * 创建付款单
             */
            createNisBillHead(cmsInlRepay, inlInfoDao.get(cmsInlRepay.getInlInfoId()), user);
            /**
             * 创建收款单
             */
            createNisReceivable(cmsInlRepay, user);
        }
        inlRepayDao.update(cmsInlRepay);
    }
    /**
     * 更新还款计划明细是否已还标识 释放授信 释放被担保合同
     */
    public void otherDo(Date realRepayDate, String id) {
        CmsInlRepay cmsInlRepay = inlRepayDao.get(id);
        cmsInlRepay.setRealRepayDate(realRepayDate);
        cmsInlRepay.setStatus(InlConstant.CONFIRM);
        cmsInlRepay.setBizStatus(InlConstant.CONFIRM);
        // 更新利息状态
        CmsProvideInterest cmsProvideInterest = cmsProvideInterestDao.get(cmsInlRepay.getInlInterestId());
        cmsProvideInterest.setStatus(1);
        cmsProvideInterestDao.update(cmsProvideInterest);
        CmsInlSend cmsInlSend = inlSendDao.get(cmsInlRepay.getInlSendId());
        Map<String, Object> params = new HashMap<>();
        params.put("billCode", cmsInlSend.getBillNo());
        params.put("signRepayDate", DateUtils.format(cmsInlRepay.getRepayDate(), "yyyy-MM-dd"));
        cmsProvideInterestDao.updateInterestBySignRepayDate(params);
        if (cmsProvideInterest.getRepayMoney().compareTo(BigDecimal.ZERO) > 0) {
            cmsInlRepay.setInlRepayplanId(cmsProvideInterest.getRepaymentScheduleId());
        }
        inlRepayDao.update(cmsInlRepay);
        if (StringUtils.isNotEmpty(cmsInlRepay.getInlRepayplanId()) && cmsInlRepay.getRepayMoney().compareTo(BigDecimal.ZERO) == 1) {
            RepaymentSchedule repaymentSchedule = repaymentScheduleDao.get(cmsInlRepay.getInlRepayplanId());
            repaymentSchedule.setIsRepay(CommonConstant.YES);
            repaymentScheduleDao.update(repaymentSchedule);
        }
        CmsInlInfo cmsInlInfo = inlInfoDao.get(cmsInlRepay.getInlInfoId());
        /**
         * 释放授信
         */
        releaseRation(cmsInlRepay, cmsInlInfo);
        /**
         * 释放被担保合同
         */
        releaseGua(cmsInlRepay, cmsInlInfo);
        /**
         * 履约
         */
        if (CommonConstant.YES.equals(cmsInlInfo.getIsCon())) {
            performanceConOp(cmsInlRepay);
        }
        cmsProvideInterestDao.flush();
        inlRepayDao.flush();
        repaymentScheduleDao.flush();
    }
    /**
     * 履约
     */
    private void performanceConOp(CmsInlRepay cmsInlRepay) {
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL).get(ConfigConstant.INL);
        if (null != config.get(InlConstant.CMS_INL_CON_SEGMENT) && config.get(InlConstant.CMS_INL_CON_SEGMENT).toString().contains("1")) {
            CmsInlInfo cmsInlInfo = inlInfoDao.get(cmsInlRepay.getInlInfoId());
            // 判断当前业务是否挂接合同
            if (CommonConstant.YES.equals(cmsInlInfo.getIsCon())) {
                ConInfoDTO conInfoDTO = conCommonService.getConInfo(null, cmsInlInfo.getId());
                if (null != conInfoDTO) {
                    UserDTO user = userService.getByUserId(cmsInlInfo.getCreateBy());
                    ConInfoRecordDTO conInfoRecordDTO = new ConInfoRecordDTO();
                    conInfoRecordDTO.setConId(conInfoDTO.getId());
                    conInfoRecordDTO.setOuterId(cmsInlRepay.getId());
                    conInfoRecordDTO.setOpDate(new Date());
                    conInfoRecordDTO.setOpMoney(cmsInlRepay.getRepayMoney());
                    conInfoRecordDTO.setOpNo(cmsInlRepay.getBillNo());
                    conInfoRecordDTO.setOpMsg(CmsConstant.INL_CON_REPAY);
                    conInfoRecordDTO.setBusType("05");
                    conCommonService.performanceConOp(conInfoRecordDTO, user);
                    /**
                     * 判断是否全部还款完成 全部还款完成终止合同
                     */
                    BigDecimal allRepayMoney = getAllReplayMoneyByInfoId(cmsInlRepay.getInlInfoId());
                    if (allRepayMoney.add(cmsInlRepay.getRepayMoney()).compareTo(cmsInlInfo.getMoney()) >= 0) {
                        conInfoRecordDTO.setOpMsg(CmsConstant.INL_CON_REPAYOVER);
                        conInfoRecordDTO.setBusType("06");
                        conCommonService.terminateConOp(conInfoRecordDTO, user);
                    }
                }
            }
        }
    }
    /**
     * 释放授信
     *
     * @param cmsInlRepay
     */
    private void releaseRation(CmsInlRepay cmsInlRepay, CmsInlInfo cmsInlInfo) {
        // 判断当前业务是否挂授信
        if (CommonConstant.YES.equals(cmsInlInfo.getIsHangCredit())) {
            RatUrationDTO urationDTO = new RatUrationDTO();
            urationDTO.setRatCode(cmsInlInfo.getCpRatCode());
            urationDTO.setCurCode(cmsInlInfo.getCurId());// 币别ID
            urationDTO.setBillId(cmsInlInfo.getId());// 业务ID
            urationDTO.setBillNo(cmsInlInfo.getBillNo());// 业务编号
            urationDTO.setSysCode(ConfigConstant.INL);// 内部拆借
            urationDTO.setBusinessName(RationConstant.BUSINESS_NAME_10);// 内部拆借占用
            urationDTO.setBeginDate(cmsInlInfo.getStartDate());// 内部拆借日期
            urationDTO.setEndDate(cmsInlInfo.getEndDate());// 到期日期
            urationDTO.setMoney(cmsInlRepay.getRepayMoney());// 释放/占用金额
            urationDTO.setBillMoney(cmsInlInfo.getMoney());// 业务金额
            urationDTO.setCorpId(cmsInlInfo.getBorrowInCorp());// 网点/单位
            urationDTO.setRatItemCode(cmsInlInfo.getCpRatItemCode());
            /*
             * if (StringUtils.isNotEmpty(isAllRelease) && CommonConstant.YES.equals(isAllRelease)) { // 释放全部授信
             * urationDTO.setAllRelease(true); }
             */
            ratCpUrationService.releaseRation(urationDTO);
        }
    }
    /**
     * 释放担保
     *
     * @param cmsInlRepay
     */
    private void releaseGua(CmsInlRepay cmsInlRepay, CmsInlInfo cmsInlInfo) {
        // 判断当前业务是否挂接被担保合同
        if (CommonConstant.YES.equals(cmsInlInfo.getIsHangGuarantee())) {
            // 获取所有还款金额
            BigDecimal allRepayMoney = BigDecimal.valueOf(Double.valueOf(inlRepayDao.getAllReplayMoney(cmsInlRepay.getInlInfoId())));
            // 判断是否全部还款完成,加上本次还款金额判断
            if ((allRepayMoney.add(cmsInlRepay.getRepayMoney())).compareTo(cmsInlInfo.getMoney()) >= 0) {
                GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
                createGua(cmsInlInfo, guaContractOccupDTO);
                guaContractOccupServie.releaseUseGuaContractOccup(guaContractOccupDTO);
            }
        }
    }
    /**
     * 设置担保挂接数据
     *
     * @param cmsInlInfo
     * @param guaContractOccupDTO
     */
    private static void createGua(CmsInlInfo cmsInlInfo, GuaContractOccupDTO guaContractOccupDTO) {
        guaContractOccupDTO.setBusinessId(cmsInlInfo.getId());
        if (StringUtils.isNotEmpty(cmsInlInfo.getGuaContractId())) {
            if (cmsInlInfo.getGuaContractId().contains(",")) {
                guaContractOccupDTO.setContractIds(Arrays.asList(cmsInlInfo.getGuaContractId().split(",")));
            } else {
                guaContractOccupDTO.setContractIds(Collections.singletonList(cmsInlInfo.getGuaContractId()));
            }
        }
        guaContractOccupDTO.setSysCode(ConfigConstant.INL);
        guaContractOccupDTO.setItemNo(cmsInlInfo.getBillNo());
    }
    /**
     * 生成付款单数据
     *
     * @param cmsInlRepay
     * @param cmsInlInfo
     * @param user
     */
    private void createNisBillHead(CmsInlRepay cmsInlRepay, CmsInlInfo cmsInlInfo, ICurrentUser user) {
        NisBillheadDTO nisBillheadDTO = new NisBillheadDTO();
        nisBillheadDTO.setStatus(InlConstant.TMP);
        nisBillheadDTO.setCorpId(cmsInlRepay.getBorrowInCorp());
        FcAccDTO fcAccDTO = null;
        BtBankAccDTO btBankAccDTO = null;
        FcVoucherTypeDTO fcVoucherTypeDTO = null;
        if (StringUtils.isNotEmpty(cmsInlRepay.getBorrowInAccId())) {
            fcAccService.getFcAcc(cmsInlRepay.getBorrowInAccId());
            nisBillheadDTO.setAccId(cmsInlRepay.getBorrowInAccId());
            nisBillheadDTO.setPayerAcc(cmsInlRepay.getBorrowInAcc());
            // 账号设置
            if (null != fcAccDTO) {
                // fcVoucherTypeService
                nisBillheadDTO.setPayerAccName(fcAccDTO.getAccName());
                nisBillheadDTO.setPayerBank(fcAccDTO.getNetName());
                fcVoucherTypeDTO = fcVoucherTypeService.getFcVoucherTypeByCode(NetbankConstant.PAY_TYPE_05);
            } else {
                if (StringUtils.isNotEmpty(cmsInlRepay.getBorrowInAccId())) {
                    btBankAccDTO = btBankAccService.getBankAccById(cmsInlRepay.getBorrowInAccId());
                    if (null != btBankAccDTO) {
                        nisBillheadDTO.setPayerAccName(btBankAccDTO.getAccName());
                        nisBillheadDTO.setPayerBank(btBankAccDTO.getBankName());
                    }
                    fcVoucherTypeDTO = fcVoucherTypeService.getFcVoucherTypeByCode(NetbankConstant.PAY_TYPE_34);
                }
            }
        }
        /**
         * 设置付款方式
         */
        if (null != fcVoucherTypeDTO) {
            nisBillheadDTO.setVoucherTypeId(fcVoucherTypeDTO.getId());
            nisBillheadDTO.setSysVoucherCode(fcVoucherTypeDTO.getSysVoucherCode());
        } else {// 如果不选择账号则默认外外付网银
            fcVoucherTypeDTO = fcVoucherTypeService.getFcVoucherTypeByCode(NetbankConstant.PAY_TYPE_34);
            nisBillheadDTO.setVoucherTypeId(fcVoucherTypeDTO.getId());
            nisBillheadDTO.setSysVoucherCode(fcVoucherTypeDTO.getSysVoucherCode());
        }
        nisBillheadDTO.setErpId(cmsInlRepay.getId());
        nisBillheadDTO.setBillFrom(NetbankConstant.BILLFROM_8);
        nisBillheadDTO.setPayMoney(cmsInlRepay.getTotalMoney());
        nisBillheadDTO.setCurId(cmsInlRepay.getCurId());
        nisBillheadDTO.setPayeeAcc(cmsInlRepay.getBorrowOutAcc());
        // 账号设置
        if (null != fcAccDTO) {
            if (StringUtils.isNotEmpty(cmsInlRepay.getBorrowOutAccId())) {
                FcAccDTO fcAccDTO1 = fcAccService.getFcAccDTO(cmsInlRepay.getBorrowOutAccId());
                if (null != fcAccDTO1) {
                    nisBillheadDTO.setPayeeAccName(fcAccDTO1.getAccName());
                    nisBillheadDTO.setPayeeBank(fcAccDTO1.getNetName());
                }
            }
        } else {
            if (StringUtils.isNotEmpty(cmsInlRepay.getBorrowOutAccId())) {
                BtBankAccDTO btBankAccDTO1 = btBankAccService.getBankAccById(cmsInlRepay.getBorrowOutAccId());
                if (null != btBankAccDTO1) {
                    nisBillheadDTO.setPayeeBankCode(btBankAccDTO1.getBankCode());
                    nisBillheadDTO.setPayeeAccName(btBankAccDTO1.getAccName());
                    nisBillheadDTO.setPayeeBank(btBankAccDTO1.getBankName());
                    nisBillheadDTO.setProv(btBankAccDTO1.getProv());
                    nisBillheadDTO.setCity(btBankAccDTO1.getCity());
                }
                if (null != btBankAccDTO && null != btBankAccDTO1) {
                    if (StringUtils.isNotEmpty(btBankAccDTO.getBankCode()) && StringUtils.isNotEmpty(btBankAccDTO1.getBankCode()) && btBankAccDTO.getBankCode().equals(btBankAccDTO1.getBankCode())) {
                        // 同行0
                        nisBillheadDTO.setDifBank("0");
                    } else {
                        // 跨行1
                        nisBillheadDTO.setDifBank("1");
                    }
                    if (StringUtils.isNotEmpty(btBankAccDTO.getCity()) && StringUtils.isNotEmpty(btBankAccDTO1.getCity()) && btBankAccDTO.getCity().equals(btBankAccDTO1.getCity())) {
                        nisBillheadDTO.setAreaSign("0");
                    } else {
                        nisBillheadDTO.setAreaSign("1");
                    }
                }
            }
        }
        nisBillheadDTO.setWishPayday(cmsInlRepay.getRepayDate());
        nisBillheadDTO.setPurpose(cmsInlInfo.getPurpose());
        nisBillheadDTO.setIsForindividual(CommonConstant.NO);
        nisBillHeadService.saveNisBillhead(nisBillheadDTO, user);
    }
    /**
     * 生成收款单
     *
     * @param cmsInlRepay
     * @param user
     */
    private void createNisReceivable(CmsInlRepay cmsInlRepay, ICurrentUser user) {
        NisReceivableDTO nisReceivableDTO = new NisReceivableDTO();
        nisReceivableDTO.setCorpId(cmsInlRepay.getBorrowOutCorp());
        FcAccDTO fcAccDTO = null;
        if (StringUtils.isNotEmpty(cmsInlRepay.getBorrowOutAccId())) {
            nisReceivableDTO.setBankAcc(cmsInlRepay.getBorrowOutAcc());
            fcAccDTO = fcAccService.getFcAccDTO(cmsInlRepay.getBorrowOutAccId());
            if (null != fcAccDTO) {
                nisReceivableDTO.setAccName(fcAccDTO.getAccName());
                nisReceivableDTO.setBankName(fcAccDTO.getNetId());
            } else {
                BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(cmsInlRepay.getBorrowOutAccId());
                if (null != btBankAccDTO) {
                    nisReceivableDTO.setAccName(btBankAccDTO.getAccName());
                    nisReceivableDTO.setBankName(btBankAccDTO.getBankName());
                }
            }
        }
        nisReceivableDTO.setAmt(cmsInlRepay.getTotalMoney());
        nisReceivableDTO.setCurId(cmsInlRepay.getCurId());
        if (StringUtils.isNotEmpty(cmsInlRepay.getBorrowInAccId())) {
            nisReceivableDTO.setOppAccNo(cmsInlRepay.getBorrowInAcc());
            if (null != fcAccDTO) {
                FcAccDTO fcAccDTO1 = fcAccService.getFcAccDTO(cmsInlRepay.getBorrowInAccId());
                nisReceivableDTO.setOppAccName(fcAccDTO1.getAccName());
                nisReceivableDTO.setOppAccBank(fcAccDTO1.getNetName());
            } else {
                BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(cmsInlRepay.getBorrowInAccId());
                nisReceivableDTO.setOppAccName(btBankAccDTO.getAccName());
                nisReceivableDTO.setOppAccBank(btBankAccDTO.getBankName());
                nisReceivableDTO.setOppBankCode(btBankAccDTO.getBankCode());
            }
        }
        nisReceivableDTO.setStatus(InlConstant.TMP);
        nisReceivableDTO.setTransTime(cmsInlRepay.getRepayDate());
        nisReceivableDTO.setBillId(cmsInlRepay.getId());
        nisReceivableDTO.setBillFrom(NetbankConstant.BILLFROM_8);
        nisReceivableDTO.setBusBillNo(cmsInlRepay.getBillNo());
        nisReceivableService.saveNisReceivable(nisReceivableDTO, user);
    }
    /**
     * @param id
     * @return
     */
    public CmsInlRepayVO getById(String id) {
        CmsInlRepay cmsInlRepay = inlRepayDao.get(id);
        CmsInlRepayVO cmsInlRepayVO = new CmsInlRepayVO();
        BeanUtils.copyProperties(cmsInlRepay, cmsInlRepayVO);
        return cmsInlRepayVO;
    }
    /**
     * 获取所有已还款本金
     *
     * @param id
     * @return
     */
    public BigDecimal getAllReplayMoneyByInfoId(String id) {
        return BigDecimal.valueOf(Double.valueOf(inlRepayDao.getAllReplayMoneyByInfoId(id)));
    }
    /**
     * 获取所有已还款本金
     *
     * @param id
     * @return
     */
    public BigDecimal getAllReplayMoney(String id) {
        return BigDecimal.valueOf(Double.valueOf(inlRepayDao.getAllReplayMoney(id)));
    }
    /**
     * 获取所有已还款利息
     *
     * @param id
     * @return
     */
    public BigDecimal getAllReplayInterest(String id) {
        return BigDecimal.valueOf(Double.valueOf(inlRepayDao.getAllReplayInterest(id)));
    }
    /**
     * 获取所有还款在途金额(本金+利息)
     *
     * @param id
     * @return
     */
    public BigDecimal getAllReplayTotal(String id) {
        return BigDecimal.valueOf(Double.valueOf(inlRepayDao.getAllReplayTotal(id)));
    }
    /**
     * 获取所有审批通过得还款数据
     *
     * @param sendId
     * @return
     */
    public List<CmsInlRepayVO> getAllRepayInfoBySendId(String sendId) {
        Map<String, Object> params = new HashMap<>();
        params.put("sendId", sendId);
        List<CmsInlRepayVO> allRepayInfoBySendId = inlRepayDao.getAllRepayInfoBySendId(params);
        if(CollectionUtil.isEmpty(allRepayInfoBySendId)){
            return allRepayInfoBySendId;
        }
        List<String> collect = allRepayInfoBySendId.stream().map(CmsInlRepayVO::getCurId).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(collect)){
            return allRepayInfoBySendId;
        }
        Map<String, BtCurrencyDTO> currencyByIds = btCurrencyService.getCurrencyByIds(collect);
        if(CollectionUtil.isEmpty(currencyByIds)){
            return allRepayInfoBySendId;
        }
        for(CmsInlRepayVO vo : allRepayInfoBySendId){
            BtCurrencyDTO btCurrencyDTO = currencyByIds.get(vo.getCurId());
            vo.setCurName(btCurrencyDTO == null ? null : btCurrencyDTO.getCurName());
        }
        return allRepayInfoBySendId;
    }
    /**
     * 删除资金计划挂接
     *
     * @param cmsInlRepay
     */
    public void deleteBudgetProcess(CmsInlRepay cmsInlRepay) {
        if (CommonConstant.YES.equals(getIsBudget().getValue()) && CommonConstant.YES.equals(cmsInlRepay.getIsBudget())) {
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_REPAY, BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlRepay.getBillNo());
        }
    }
    /**
     * 更新资金计划挂接
     *
     * @return
     * @Param dfDirectFinanceInfo
     * @Param user
     */
    public void updateBudget(CmsInlRepay cmsInlRepay, ICurrentUser user) {
        /**
         * 判断是否挂接挂接资金计划
         */
        if (CommonConstant.YES.equals(getIsBudget().getValue()) && CommonConstant.YES.equals(cmsInlRepay.getIsBudget())) {
            if (InlConstant.APPROVED == cmsInlRepay.getStatus()) {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_REPAY, BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlRepay.getBillNo(), BudgetConstant.STATUE_CHECK, user);
            } else {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_REPAY, BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlRepay.getBillNo(), BudgetConstant.STATUE_BACK, user);
            }
        }
    }
    /**
     * 获取是否挂接资金计划配置
     *
     * @return
     */
    private ConfigDTO getIsBudget() {
        return configService.getConfig(ConfigConstant.INL, InlConstant.CMS_INL_IS_BUDGET);
    }
    /**
     * 资金计划挂接处理
     *
     * @param cmsInlRepayVO
     * @param user
     */
    private void budgetHandle(CmsInlRepayVO cmsInlRepayVO, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(cmsInlRepayVO.getItemCode())) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < cmsInlRepayVO.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                if (InlConstant.TMP == cmsInlRepayVO.getStatus()) {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);// 状态:11在途，95占用
                } else if (InlConstant.APPROVED == cmsInlRepayVO.getStatus()) {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_CHECK);// 状态:0暂存,11在途，95占用
                } else {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:0暂存,11在途，95占用
                }
                fbsBudgetProcessDTO.setBillNo(cmsInlRepayVO.getBillNo());// 业务单据号
                fbsBudgetProcessDTO.setBillId(cmsInlRepayVO.getId());
                fbsBudgetProcessDTO.setItemCode(cmsInlRepayVO.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(cmsInlRepayVO.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(cmsInlRepayVO.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(cmsInlRepayVO.getRepayDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(cmsInlRepayVO.getBorrowInCorp());// 单位id
                fbsBudgetProcessDTO.setCurId(cmsInlRepayVO.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_INL_REPAY);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_REPAY, BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlRepayVO.getBillNo());
            fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }
    /**
     * 获取是否挂接资金计划配置
     *
     * @return
     */
    private ConfigDTO getNoticeDays() {
        return configService.getConfig(ConfigConstant.INL, BaseDataConstant.NOTIFY_DAYS);
    }
    @Override
    @XxlJob("InlRepayService")
    public void run(String arguments) {
        arguments = XxlJobHelper.getJobParam();
        logger.info("定时任务接收参数：【{}】", arguments);
        ConfigDTO configDTO = getNoticeDays();
        Date startDate = DateUtils.getDate();
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", DateUtils.format(startDate, "yyyy-MM-dd"));
        if (null == configDTO) {
            /**
             * 默认5天内
             */
            params.put("endDate", DateUtils.format(DateUtils.addDay(startDate, 5), "yyyy-MM-dd"));
        } else {
            params.put("endDate", DateUtils.format(DateUtils.addDay(startDate, Integer.valueOf(configDTO.getValue())), "yyyy-MM-dd"));
        }
        List<CmsProvideInterestVO> cmsProvideInterestVOS = cmsProvideInterestDao.listNotifyInterest(params);
        if (null != cmsProvideInterestVOS && cmsProvideInterestVOS.size() > 0) {
            for (CmsProvideInterestVO cmsProvideInterestVO : cmsProvideInterestVOS) {
                CmsInlSendVO cmsInlSendVO = inlSendDao.getInterestByBillNo(cmsProvideInterestVO.getBillCode());
                if (null != cmsInlSendVO) {
                    notificationService.send(cmsProvideInterestVO.getId(), cmsInlSendVO.getBorrowInCorp(), arguments, cmsProvideInterestVO);
                }
            }
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public BaseDao getEntityDao() {
        return inlRepayDao;
    }
}
