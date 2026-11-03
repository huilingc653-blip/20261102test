package com.bytter.finance.domain.inl.service;
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
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.dao.InlSendDao;
import com.bytter.finance.domain.inl.entity.CmsInlInfo;
import com.bytter.finance.domain.inl.entity.CmsInlSend;
import com.bytter.finance.domain.inl.service.inlProcess.InterestWayHandler;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.finance.domain.provide.dao.CmsProvideInterestDao;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.finance.domain.provide.service.CmsProvideInterestService;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
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

import org.apache.commons.collections.MapUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service("inlSendService")
@WorkFlow(InlWorkFlowConstant.CMS_INL_SEND)
public class InlSendService extends BaseService<CmsInlSend, String> implements IWorkFlowListener {
    @Autowired
    public InlSendDao inlSendDao;
    @Autowired
    public InlInfoService inlInfoService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired(required = false)
    public ICorpService corpService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private INisBillheadService nisBillHeadService;
    @DubboReference(check = false)
    private INisReceivableService nisReceivableService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private CmsProvideInterestDao cmsProvideInterestDao;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private InlAdjustrateService inlAdjustrateService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IBisExcService excService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private RepayTypeService repayTypeService;
    @Autowired
    private InlDeferredService inlDeferredService;

    /**
     * 数据保存
     *
     * @param cmsInlSendVO
     * @param user
     */
    public void save(CmsInlSendVO cmsInlSendVO, ICurrentUser user) {
        CmsInlSend cmsInlSend = null;
        if (StringUtils.isNotEmpty(cmsInlSendVO.getId())) {
            cmsInlSend = inlSendDao.get(cmsInlSendVO.getId());
        }
        if (cmsInlSend != null) {
            cmsInlSendVO.setUpdateBy(user.getId());
            cmsInlSendVO.setUpdateDate(new Date());
            if (CommonConstant.NO.equals(cmsInlSendVO.getIsBudget())
                    && CommonConstant.YES.equals(cmsInlSend.getIsBudget())) {
                /**
                 * 删除资金计划挂接
                 */
                deleteBudgetProcess(cmsInlSend);
            }
            String billNo = cmsInlSend.getBillNo();
            BeanUtils.copyNotNullProperties(cmsInlSendVO, cmsInlSend);
            cmsInlSend.setBillNo(billNo);
        } else {
            cmsInlSend = new CmsInlSend();
            cmsInlSendVO.setId(null);
            cmsInlSendVO.setCreateBy(user.getId());
            cmsInlSendVO.setCreateDate(new Date());
            cmsInlSendVO.setBillNo(nextUniqueInlSendBillNo());
            BeanUtils.copyNotNullProperties(cmsInlSendVO, cmsInlSend);
        }
        cmsInlSend.setBizStatus(cmsInlSend.getStatus());
        inlSendDao.saveOrUpdate(cmsInlSend);
        cmsInlSendVO.setId(cmsInlSend.getId());
        /**
         * 其他挂接处理
         */
        otherHandle(cmsInlSend, cmsInlSendVO, user);
        // 打回数据角标处理
        if (InlConstant.COMMIT_APPROVE == cmsInlSendVO.getStatus()) {
            notificationService.complete(NotificationConstant.CMS.INL_SEND, cmsInlSend.getId(), user.getId());
             getStartHandler().handler(InlWorkFlowConstant.CMS_INL_SEND, cmsInlSend.getId(), cmsInlSend,
                    cmsInlSend.getBorrowInCorp(), user.getId());
        }
    }

    private String nextUniqueInlSendBillNo() {
        for (int i = 0; i < 20; i++) {
            String billNo = InlUtil.getInlSendNo();
            if (!inlSendDao.existsBillNo(billNo)) {
                return billNo;
            }
        }
        throw new BusinessException("拆借放款编号生成失败，请重试");
    }

    /**
     * 其他业务处理
     *
     * @param cmsInlSend
     * @param cmsInlSendVO
     * @param user
     */
    private void otherHandle(CmsInlSend cmsInlSend, CmsInlSendVO cmsInlSendVO, ICurrentUser user) {
        // 更新还款计划
        updateRepayPlan(cmsInlSendVO);
        /**
         * 判断是否挂接挂接资金计划
         */
        if (CommonConstant.YES.equals(getIsBudget().getValue())
                && CommonConstant.YES.equals(cmsInlSend.getIsBudget())) {
            budgetHandle(cmsInlSendVO, user);
        }
    }
    /**
     * 资金计划挂接处理
     *
     * @param cmsInlSendVO
     * @param user
     */
    private void budgetHandle(CmsInlSendVO cmsInlSendVO, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(cmsInlSendVO.getItemCode())) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < cmsInlSendVO.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                if (InlConstant.TMP == cmsInlSendVO.getStatus()) {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);// 状态:11在途，95占用
                } else if (InlConstant.APPROVED == cmsInlSendVO.getStatus()) {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_CHECK);// 状态:0暂存,11在途，95占用
                } else {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:0暂存,11在途，95占用
                }
                fbsBudgetProcessDTO.setBillNo(cmsInlSendVO.getBillNo());// 业务单据号
                fbsBudgetProcessDTO.setBillId(cmsInlSendVO.getId());
                fbsBudgetProcessDTO.setItemCode(cmsInlSendVO.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(cmsInlSendVO.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(cmsInlSendVO.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(cmsInlSendVO.getSendDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(cmsInlSendVO.getBorrowInCorp());// 单位id
                fbsBudgetProcessDTO.setCurId(cmsInlSendVO.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_INL_SEND);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_SEND,
                    BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlSendVO.getBillNo());
            fbsBudgetProcessService.writeBudgetProcess(list, user);
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
     * 还款计划数据更新
     *
     * @param cmsInlSendVO
     */
    public void updateRepayPlan(CmsInlSendVO cmsInlSendVO) {
        repaymentScheduleDao.updateRepayMentBill(cmsInlSendVO.getPlanId(), CmsConstant.INL_SEND_BILL_CODE_PREFIX,
                cmsInlSendVO.getId());
    }
    /**
     * 还款计划主表
     *
     * @param busId
     */
    public CmsRepaymentSchedule getRepayPlan(String busId) {
        return repaymentScheduleService.getCmsRepaymentScheduleByBillCode(busId);
    }
    /**
     * 查询待发放数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listInlInfoApproved(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_INL_INFO_QUERY, user.getId());
        return inlSendDao.listInlInfoApproved(input, corpIds);
    }
    /**
     * 查询打回和暂存数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listSendUpdateDataTable(DataTablesInput input, ICurrentUser user) {
        remapBillNoToInfoBillNo(input);
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_INL_SEND_QUERY, user.getId());
        return inlSendDao.listSendUpdateDataTable(input, corpIds);
    }

    /**
     * 内部拆借编号条件 likes_billNo 实际打到 send.bill_no；列表展示的是 info.bill_no。
     */
    private void remapBillNoToInfoBillNo(DataTablesInput input) {
        Map<String, Object> formdata = input.getFormdata();
        if (formdata == null) {
            return;
        }
        Object billNo = formdata.remove("likes_billNo");
        if (billNo == null) {
            return;
        }
        String value = billNo.toString().trim();
        if (StringUtils.isNotEmpty(value) && EmptyUtils.isEmpty(formdata.get("likes_infoBillNo"))) {
            formdata.put("likes_infoBillNo", value);
        }
    }
    /**
     * 查询待审批数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(InlWorkFlowConstant.CMS_INL_SEND,
                input);
        return workFlowBusiness.merge(inlSendDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList()));
    }
    /**
     * 查询待发放确认数据
     *
     * @param input 查询参数
     * @param user  用户
     * @return 分页数据
     */
    public DataTablesOutput listSendConfirmDataTable(DataTablesInput input, ICurrentUser user) {
      List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_INL_SEND_QUERY, user.getId());
      if (CollectionUtils.isEmpty(corpIds)) {
         return new DataTablesOutput();
      }
      DataTablesOutput output = inlSendDao.listSendConfirmDataTable(input, corpIds);
      List<CmsInlSendVO> list = output.getData();
      List<CmsInlSendVO> listResult = new ArrayList<CmsInlSendVO>();
      if(CollectionUtils.isEmpty(list)) {
    	  return new DataTablesOutput();
      }
      List<String> ids = list.stream().map(cmsInlSendVO -> cmsInlSendVO.getId()).distinct().collect(Collectors.toList());
      List<NisBillheadDTO> listDTO = nisBillHeadService.listByErpIds(ids);
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

      List<String> listCmsInlSendId = new ArrayList<String>();//NisBillheadDTO的ErpId-->CmsInlSendVO的id
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

    	  if("0".equals(isPayed) && 95 == status && "0".equals(voucherStat) && "1".equals(isOnline)) {//银行账号在线账号付款（线上）
    		  listCmsInlSendId.add(dto.getErpId());
    	  }else if("1".equals(isPayed) && 95 == dto.getStatus() && "1".equals(isOnline)) { //银行账号在线账号付款（线下）
    		  listCmsInlSendId.add(dto.getErpId());
    	  }else if( 95 == dto.getStatus() && "0".equals(isOnline)) {//银行账号离线账号付款（线下）
    		  listCmsInlSendId.add(dto.getErpId());
    	  }else if(100 == dto.getStatus() && StringUtils.isEmpty(downAccId)) {//内部账号付款
    		  listCmsInlSendId.add(dto.getErpId());
    	  }else if("0".equals(isPayed) && "0".equals(voucherStat) && 100 == status && StringUtils.isNotEmpty(downAccId)) { //内部账号付款（线上），实际银行账号付款（转付）
    		  listCmsInlSendId.add(dto.getErpId());
    	  }else if("1".equals(isPayed)  && 100 == status && StringUtils.isNotEmpty(downAccId)) {//内部账号付款（线下）（转付）
    		  listCmsInlSendId.add(dto.getErpId());
    	  }else {
    		  ///do nothing
    	  }
      }
      if(CollectionUtils.isEmpty(listCmsInlSendId)) {
    	  return new DataTablesOutput();
      }else {
    	  Map<String, CorpDTO> mapCorp = corpService.getCorpMap();
    	  Map<String, BtCurrencyDTO> mapCur = btCurrencyService.getCurrency();
    	  Map<String, String> idMap = listCmsInlSendId.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
    	  for(CmsInlSendVO sendVo : list)
    	  {
    		  if(idMap.containsKey(sendVo.getId())) {
    			  sendVo.setInfoBillNo(sendVo.getBillNo());
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
        List<CmsInlSendVO> inl = inlSendDao.getListId(corpIds);
        //获取资金才接发送表单据id
        List<String> ids = inl.stream().map(CmsInlSendVO::getId).collect(Collectors.toList());
        //根据单据id获取单据信息
        List<NisBillheadDTO> nisBillHeadList = nisBillHeadService.getCmsInlSendId(ids);
        //根据单据信息获取账号id
        List<String> accIds = nisBillHeadList.stream().map(NisBillheadDTO::getAccId).collect(Collectors.toList());
        //封装Map
        Map<String, String> billMapByAccId = nisBillHeadList.stream().collect(Collectors.toMap(NisBillheadDTO::getAccId, NisBillheadDTO::getId));
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
        Map<String, String> billMapById = nisBillHeadList.stream().collect(Collectors.toMap(NisBillheadDTO::getId, NisBillheadDTO::getErpId));
        List<String> resultList = new ArrayList<>();
        billIds.forEach(entity -> {
            if (billMapById.containsKey(entity) && StringUtil.isNotEmpty(billMapById.get(entity))) {
                resultList.add(billMapById.get(entity));
            }
        });
        return resultList;
    }
    /**
     * 通过申请id查询相关信息
     *
     * @param id
     * @return
     */
    public CmsInlInfoVO getInlInfoById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<CmsInlInfoVO> cmsInlInfoVOS = inlSendDao.getByInfoId(params);
        if (ListUtils.isNullOrEmpty(cmsInlInfoVOS)) {
            return null;
        }
        return cmsInlInfoVOS.get(0);
    }
    /**
     * 通过发放id查询相关信息
     *
     * @param id
     * @return
     */
    public CmsInlSendVO getInlSendById(String id) {
        CmsInlSend cmsInlSend = inlSendDao.get(id);
        CmsInlSendVO cmsInlSendVO = new CmsInlSendVO();
        BeanUtils.copyProperties(cmsInlSend, cmsInlSendVO);
        BtCurrencyDTO objectById = btCurrencyService.getObjectById(cmsInlSend.getCurId());
        cmsInlSendVO.setCurName(objectById == null ? "" : objectById.getCurName());
        return cmsInlSendVO;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /* 判断业务主键是否为空 */
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        CmsInlSend cmsInlSend = inlSendDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        if (isPass) {
            cmsInlSend.setStatus(DfConstant.APPROVED);
            msService.insertProcessMsg(cmsInlSend.getBillNo(), InlConstant.CMS_INL_SEND_NO,
                    CmsProcessMsgConstant.MSGA_CHECKOK, "审批通过", user);
        } else {
            cmsInlSend.setStatus(DfConstant.BACK);
            /**
             * 增加消息提醒
             */
            notificationService.send(cmsInlSend.getId(), cmsInlSend.getBorrowInCorp(),
                    NotificationConstant.CMS.INL_SEND, cmsInlSend);
            msService.insertProcessMsg(cmsInlSend.getBillNo(), InlConstant.CMS_INL_SEND_NO,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, "审批打回", user);
        }
        // 资金计划挂机处理
        // updateBudget(dfDirectFinanceInfo, user);
        cmsInlSend.setBizStatus(cmsInlSend.getStatus());
        /**
         * 审批通过 1生成付款单 2生成收款单 3.创建还款计划
         */
        if (isPass) {
            /**
             * 创建付款单
             */
            createNisBillHead(cmsInlSend, inlInfoService.get(cmsInlSend.getInlInfoId()), user);// 付款单
            /**
             * 创建收款单
             */
            createNisReceivable(cmsInlSend, inlInfoService.get(cmsInlSend.getInlInfoId()));// 收款单
            /**
             * 构建还款计划利息明细
             */
            adjustProvideInterest(cmsInlSend, user);
        }
        /**
         * 跟新资金计划
         */
        updateBudget(cmsInlSend, user);
        inlSendDao.update(cmsInlSend);
    }
    /**
     * @param id
     * @param realSendDate
     */
    public void sendConfirm(String id, Date realSendDate) {
        CmsInlSend cmsInlSend = inlSendDao.get(id);
        cmsInlSend.setStatus(InlConstant.CONFIRM);
        cmsInlSend.setBizStatus(InlConstant.CONFIRM);
        cmsInlSend.setRealSendDate(realSendDate);
        inlSendDao.update(cmsInlSend);
        // 放款履约
        performanceConOp(cmsInlSend);
        inlSendDao.flush();
    }
    /**
     * 履约
     */
    private void performanceConOp(CmsInlSend cmsInlSend) {
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL)
                .get(ConfigConstant.INL);
        if (null != config && null != config.get(InlConstant.CMS_INL_CON_IS_CON)
                && config.get(InlConstant.CMS_INL_CON_IS_CON).toString().equals(CommonConstant.YES)) {
            if (null != config.get(InlConstant.CMS_INL_CON_SEGMENT)
                    && config.get(InlConstant.CMS_INL_CON_SEGMENT).toString().contains("0")) {
                CmsInlInfo cmsInlInfo = (CmsInlInfo) inlInfoService.getEntityDao().get(cmsInlSend.getInlInfoId());
                if (CommonConstant.YES.equals(cmsInlInfo.getIsCon())) {
                    ConInfoDTO conInfoDTO = conCommonService.getConInfo(null, cmsInlInfo.getId());
                    if (null != conInfoDTO) {
                        UserDTO user = userService.getByUserId(cmsInlInfo.getCreateBy());
                        ConInfoRecordDTO conInfoRecordDTO = new ConInfoRecordDTO();
                        conInfoRecordDTO.setConId(conInfoDTO.getId());
                        conInfoRecordDTO.setOuterId(cmsInlSend.getId());
                        conInfoRecordDTO.setOpMsg(CmsConstant.INL_CON_SEND);
                        conInfoRecordDTO.setOpDate(new Date());
                        conInfoRecordDTO.setOpMoney(cmsInlSend.getSendMoney());
                        conInfoRecordDTO.setBusType("05");
                        conInfoRecordDTO.setOpNo(cmsInlSend.getBillNo());
                        conCommonService.performanceConOp(conInfoRecordDTO, user);
                    }
                }
            }
        }
    }
    /**
     * 构建还款计划利息明细
     *
     * @param cmsInlSend
     * @param user
     */
    public void adjustProvideInterest(CmsInlSend cmsInlSend, ICurrentUser user) {
        cmsProvideInterestDao.flush();
        InterestWayHandler interestHanlder = this.getInterestAdjustDetail(cmsInlSend);// 获取生成利息明细的必要资源
        // 1、根据参数生成分段数据 计息
        List<CmsProvideInterest> instList = interestHanlder.createInterestDetail();
        Date interestTime = DateUtils.getDate();
        for (CmsProvideInterest interest : instList) {
            interest.setInterestTime(interestTime);
        }
        cmsProvideInterestDao.batchSave(instList);
    }

    /**
     * 通过发放单号查询发放数据
     *
     * @param sendBillCode
     */
    public CmsInlSendVO getSendByBillNo(String sendBillCode){
        return inlSendDao.getInterestByBillNo(sendBillCode);
    }

    /**
     * 获取调整明细
     *
     * @return
     */
    private InterestWayHandler getInterestAdjustDetail(CmsInlSend cmsInlSend) {
        String curCode = cmsInlSend.getCurId();
        String repayCycle = cmsInlSend.getRepayCycle();
        Set<String> periodSet = new HashSet<String>();
        periodSet.add(repayCycle + "," + curCode + "," + cmsInlSend.getReferenceRateType());
        String sendId = cmsInlSend.getId();
        String sendCode = cmsInlSend.getBillNo();
        CmsInlInfo info = inlInfoService.get(cmsInlSend.getInlInfoId());

        InterestWayHandler interestHanlder = new InterestWayHandler(cmsInlSend, info);
        // cmsProvideInterestDao.deleteProvideInterest(sendCode,periodSet);//删除未还的测算利息
        inlSendDao.deleteInterestByBillNo(sendCode);// 删除未还的测算利息
        interestHanlder.setDeferredList(inlDeferredService.getAllCmsInlDeferredBySendId(sendId));
        interestHanlder.setScheduleList(cmsProvideInterestDao.getRepayMentScheduleByBillCode(sendId));// 全部还款计划
        interestHanlder.setCmsRepaymentSchedule(repaymentScheduleService.getCmsRepaymentScheduleByBillCode(cmsInlSend.getId()));//还款计划主表数据
        RepayType repayType = repayTypeService.get(cmsInlSend.getRepayType(),true);
        if(!EmptyUtils.isEmpty(repayType)){
            interestHanlder.setRepayType(repayType);//设置还款方式
        }

        // 无计息方式调整
        // interestHanlder.setIstWayList(cmsProvideInterestDao.getInterestWaysByBillCode(sendId));//计息方式调整
        /**
         * 设置最大还款开始日期
         */
        interestHanlder.setRepayStartDate(cmsProvideInterestService.getMaxRepayedDate(cmsInlSend.getBillNo(), cmsInlSend.getSendDate()));
        if ("01".equals(cmsInlSend.getFloatWay())) {// 固定利率
            interestHanlder.setRateAdjustList(inlAdjustrateService.getRateAdjustBeanVOBySendId(cmsInlSend.getId()));// 查询利率调整
        } else {// 浮动利率
            interestHanlder.setBaseRateBeanList(cmsProvideInterestDao.getLoanRateMaintence(periodSet));// 基准利率调整记录
            interestHanlder.setFloatTypeList(cmsProvideInterestDao.getInterestFloatsByBillCode(sendId));// 浮动方式调整
        }
        return interestHanlder;
    }
    /**
     * 查询内部拆借申请信息，包含所有发放金额合计
     *
     * @param id
     * @return
     */
    public CmsInlInfoVO getSendMoneyByInfoId(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<CmsInlInfoVO> cmsInlInfoVOS = inlSendDao.getSendMoneyByInfoId(params);
        CmsInlInfoVO cmsInlInfoVO = null;
        if (ListUtils.isNotNullOrEmpty(cmsInlInfoVOS)) {
            cmsInlInfoVO = cmsInlInfoVOS.get(0);
        }
        return cmsInlInfoVO;
    }
    /**
     * 生成付款单数据
     *
     * @param cmsInlSend
     * @param user
     */
    public void createNisBillHead(CmsInlSend cmsInlSend, CmsInlInfo cmsInlInfo, ICurrentUser user) {
        NisBillheadDTO nisBillheadDTO = new NisBillheadDTO();
        nisBillheadDTO.setStatus(InlConstant.TMP);
        nisBillheadDTO.setCorpId(cmsInlSend.getBorrowOutCorp());
        FcAccDTO fcAccDTO = fcAccService.getFcAccDTO(cmsInlSend.getBorrowOutAccId());
        BtBankAccDTO btBankAccDTO = null;
        nisBillheadDTO.setAccId(cmsInlSend.getBorrowOutAccId());
        nisBillheadDTO.setPayerAcc(cmsInlSend.getBorrowOutAcc());
        FcVoucherTypeDTO fcVoucherTypeDTO;
        // 账号设置
        if (null != fcAccDTO) {
            // fcVoucherTypeService
            nisBillheadDTO.setPayerAccName(fcAccDTO.getAccName());
            nisBillheadDTO.setPayerBank(fcAccDTO.getNetName());
            fcVoucherTypeDTO = fcVoucherTypeService.getFcVoucherTypeByCode(NetbankConstant.PAY_TYPE_05);
        } else {
            btBankAccDTO = btBankAccService.getBankAccById(cmsInlSend.getBorrowOutAccId());
            if (null != btBankAccDTO) {
                nisBillheadDTO.setPayerAccName(btBankAccDTO.getAccName());
                nisBillheadDTO.setPayerBank(btBankAccDTO.getBankName());
            }
            fcVoucherTypeDTO = fcVoucherTypeService.getFcVoucherTypeByCode(NetbankConstant.PAY_TYPE_34);
        }
        /**
         * 设置付款方式
         */
        nisBillheadDTO.setVoucherTypeId(fcVoucherTypeDTO.getId());
        nisBillheadDTO.setSysVoucherCode(fcVoucherTypeDTO.getSysVoucherCode());
        nisBillheadDTO.setErpId(cmsInlSend.getId());
        nisBillheadDTO.setBillFrom(NetbankConstant.BILLFROM_4);
        nisBillheadDTO.setPayMoney(cmsInlSend.getSendMoney());
        nisBillheadDTO.setCurId(cmsInlSend.getCurId());
        nisBillheadDTO.setPayeeAcc(cmsInlSend.getBorrowInAcc());
        // 账号设置
        if (null != fcAccDTO) {
            if (StringUtils.isNotEmpty(cmsInlSend.getBorrowInAccId())) {
                FcAccDTO fcAccDTO1 = fcAccService.getFcAccDTO(cmsInlSend.getBorrowInAccId());
                if (null != fcAccDTO1) {
                    nisBillheadDTO.setPayeeAccName(fcAccDTO1.getAccName());
                    nisBillheadDTO.setPayeeBank(fcAccDTO1.getNetName());
                }
            }
        } else {
            if (StringUtils.isNotEmpty(cmsInlSend.getBorrowInAccId())) {
                BtBankAccDTO btBankAccDTO1 = btBankAccService.getBankAccById(cmsInlSend.getBorrowInAccId());
                if (null != btBankAccDTO1) {
                    nisBillheadDTO.setPayeeBankCode(btBankAccDTO1.getBankCode());
                    nisBillheadDTO.setPayeeAccName(btBankAccDTO1.getAccName());
                    nisBillheadDTO.setPayeeBank(btBankAccDTO1.getBankName());
                    nisBillheadDTO.setProv(btBankAccDTO1.getProv());
                    nisBillheadDTO.setCity(btBankAccDTO1.getCity());
                }
                if (null != btBankAccDTO && null != btBankAccDTO1) {
                    if (StringUtils.isNotEmpty(btBankAccDTO.getBankCode())
                            && StringUtils.isNotEmpty(btBankAccDTO1.getBankCode())
                            && btBankAccDTO.getBankCode().equals(btBankAccDTO1.getBankCode())) {
                        // 同行0
                        nisBillheadDTO.setDifBank("0");
                    } else {
                        // 跨行1
                        nisBillheadDTO.setDifBank("1");
                    }
                    if (StringUtils.isNotEmpty(btBankAccDTO.getCity())
                            && StringUtils.isNotEmpty(btBankAccDTO1.getCity())
                            && btBankAccDTO.getCity().equals(btBankAccDTO1.getCity())) {
                        nisBillheadDTO.setAreaSign("0");
                    } else {
                        nisBillheadDTO.setAreaSign("1");
                    }
                }
            }
        }
        nisBillheadDTO.setWishPayday(cmsInlSend.getSendDate());
        nisBillheadDTO.setPurpose(cmsInlInfo.getPurpose());
        nisBillheadDTO.setIsForindividual(CommonConstant.NO);
        nisBillHeadService.saveNisBillhead(nisBillheadDTO, user);
    }
    /**
     * 生成收款单
     *
     * @param cmsInlSend
     * @param cmsInlInfo
     */
    public void createNisReceivable(CmsInlSend cmsInlSend, CmsInlInfo cmsInlInfo) {
        NisReceivableDTO nisReceivableDTO = new NisReceivableDTO();
        nisReceivableDTO.setCorpId(cmsInlSend.getBorrowInCorp());
        nisReceivableDTO.setBankAcc(cmsInlSend.getBorrowInAcc());
        FcAccDTO fcAccDTO = fcAccService.getFcAccDTO(cmsInlSend.getBorrowInAccId());
        if (null != fcAccDTO) {
            nisReceivableDTO.setAccName(fcAccDTO.getAccName());
            nisReceivableDTO.setBankName(fcAccDTO.getNetId());
        } else {
            BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(cmsInlSend.getBorrowInAccId());
            if (null != btBankAccDTO) {
                nisReceivableDTO.setAccName(btBankAccDTO.getAccName());
                nisReceivableDTO.setBankName(btBankAccDTO.getBankName());
            }
        }
        nisReceivableDTO.setAmt(cmsInlSend.getSendMoney());
        nisReceivableDTO.setCurId(cmsInlSend.getCurId());
        nisReceivableDTO.setOppAccNo(cmsInlSend.getBorrowOutAcc());
        if (StringUtils.isNotEmpty(cmsInlSend.getBorrowOutAccId())) {
            if (null != fcAccDTO) {
                FcAccDTO fcAccDTO1 = fcAccService.getFcAccDTO(cmsInlSend.getBorrowOutAccId());
                nisReceivableDTO.setOppAccName(fcAccDTO1.getAccName());
                nisReceivableDTO.setOppAccBank(fcAccDTO1.getNetName());
            } else {
                BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(cmsInlSend.getBorrowOutAccId());
                nisReceivableDTO.setOppAccName(btBankAccDTO.getAccName());
                nisReceivableDTO.setOppAccBank(btBankAccDTO.getBankName());
                nisReceivableDTO.setOppBankCode(btBankAccDTO.getBankCode());
            }
        }
        nisReceivableDTO.setStatus(InlConstant.TMP);
        nisReceivableDTO.setTransTime(cmsInlSend.getSendDate());
        nisReceivableDTO.setBillId(cmsInlSend.getId());
        nisReceivableDTO.setBillFrom(NetbankConstant.BILLFROM_4);
        nisReceivableDTO.setBusBillNo(cmsInlSend.getBillNo());
        UserDTO userDTO = userService.getByUserId(cmsInlInfo.getCreateBy());
        nisReceivableService.saveNisReceivable(nisReceivableDTO, userDTO);
    }
    /**
     * 获取所有发放数据
     *
     * @param id
     * @return
     */
    public List<CmsInlSendVO> getSendsByInfoId(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return inlSendDao.getSendsByInfoId(params);
    }
    /**
     * 修改还款方式
     *
     * @param id
     * @param repayType
     */
    public void updateRepayType(String id, String repayType) {
        inlSendDao.updateRepayType(id, repayType);
    }
    /**
     * 逻辑删除
     *
     * @param id
     */
    public void removeById(String id) {
        CmsInlSend cmsInlSend = inlSendDao.get(id);
        cmsInlSend.setStatus(InlConstant.DELETE);
        cmsInlSend.setBizStatus(InlConstant.DELETE);
        inlSendDao.update(cmsInlSend);
        /**
         * 删除资金计划
         */
        deleteBudgetProcess(cmsInlSend);
    }
    /**
     * 删除资金计划挂接
     *
     * @param cmsInlSend
     */
    public void deleteBudgetProcess(CmsInlSend cmsInlSend) {
        if (CommonConstant.YES.equals(getIsBudget().getValue())
                && CommonConstant.YES.equals(cmsInlSend.getIsBudget())) {
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_SEND,
                    BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlSend.getBillNo());
        }
    }
    /**
     * 更新资金计划挂接
     *
     * @return
     * @Param dfDirectFinanceInfo
     * @Param user
     */
    public void updateBudget(CmsInlSend cmsInlSend, ICurrentUser user) {
        /**
         * 判断是否挂接挂接资金计划
         */
        if (CommonConstant.YES.equals(getIsBudget().getValue())
                && CommonConstant.YES.equals(cmsInlSend.getIsBudget())) {
            if (InlConstant.APPROVED == cmsInlSend.getStatus()) {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_SEND,
                        BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlSend.getBillNo(), BudgetConstant.STATUE_CHECK, user);
            } else {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_SEND,
                        BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlSend.getBillNo(), BudgetConstant.STATUE_BACK, user);
            }
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public BaseDao getEntityDao() {
        return inlSendDao;
    }
}
