package com.bytter.guarantee.bond.service;


import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BaseDataConstant;
import com.bytter.core.constant.DepositReportConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.guarantee.bond.constant.CmsProcessMsgConstant;
import com.bytter.guarantee.bond.dao.FcBondDao;
import com.bytter.guarantee.bond.entity.FcBond;
import com.bytter.guarantee.bond.entity.FcBondAdd;
import com.bytter.guarantee.bond.entity.FcBondBal;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fusb
 * @version 1.0
 * @Title FcBondDetailService
 * @Description 保证金表
 * @date 2018年9月6日
 */
@Service("fcBondDetailService")
@WorkFlow(value = {FcBondConstant.FC_FCBONDDETAIL_FLOW})
@Transactional
public class FcBondDetailService extends BaseService<FcBond, String> implements IWorkFlowListener, ITaskService, IExcelVerifyHandler<FcBondDetailVO>, ICorpDeleteVerifyService {
    @Autowired
    private FcBondDao fcBondDao;
    @Autowired
	private IWorkFlowService workFlowService;
    @Autowired
    private FcBondLogService fcBondLogService;
    @DubboReference(check = false)
	private INotificationService notificationService;

    @Autowired
    private FcBondBalService fcBondBalService;
    @Autowired
    private FcBondAddService fcBondAddService;

    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService rateAdjustService;
    @DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    /**
     * 查询保证金表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<FcBondDetailVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = FcBondConstant.NETBANK_FCBOND_DEPOSITOUT_QUERY;//查询资源
        List<String> corpList = corpService.getUserCorpIds(resource, user.getId());
        if (EmptyUtils.isNotEmpty(corpList)) {
            String corpAuthSql = SqlUtil.getInSql(corpList, "b.corp_id", true);
            sql.append(" and ").append(corpAuthSql);
        } else {
            sql.append(" and 1 <> 1");
        }
        //账号条件
        String likes_bankName = (String) dataTablesInput.getFormdata().get("likes_bankName");
        String likes_bankAcc = (String) dataTablesInput.getFormdata().get("likes_bankAcc");
        String likes_accName = (String) dataTablesInput.getFormdata().get("likes_accName");
        if (!StringUtil.isBlank_new(likes_bankName) ||!StringUtil.isBlank_new(likes_bankAcc)||!StringUtil.isBlank_new(likes_accName)) {
            StringBuilder accSql = new StringBuilder();
            Map<String, Object> params = new HashMap();
            accSql.append(" SELECT a.id FROM bt_bank_acc a WHERE 1=1");
            if (!StringUtil.isBlank_new(likes_bankName)){
                accSql.append(" and a.bank_name like :bankName");
                String bankName = "%" + likes_bankName + "%";
                params.put("bankName", bankName);
            }
            if (!StringUtil.isBlank_new(likes_bankAcc)){
                accSql.append(" and a.bank_acc like :bankAcc");
                String bankAcc = "%" + likes_bankAcc + "%";
                params.put("bankAcc", bankAcc);
            }
            if (!StringUtil.isBlank_new(likes_accName)){
                accSql.append(" and a.acc_name like '%" + likes_accName + "%'");
                String accName = "%" + likes_accName + "%";
                params.put("accName", accName);
            }
            List<BtBankAccDTO> accDTOList = iBtBankAccService.listBankAccBySql(accSql.toString(), params);
            if (accDTOList.size() > 0) {
                List<String> accList = accDTOList.stream().map(BtBankAccDTO::getId).collect(Collectors.toList());
                if (accList != null && accList.size() > 0) {
                    sql.append(" and ").append(SqlUtil.getInSql(accList, " B.BANK_ACC_ID", true));
                }
                dataTablesInput.getFormdata().remove("likes_bankName");
                dataTablesInput.getFormdata().remove("likes_bankAcc");
                dataTablesInput.getFormdata().remove("likes_accName");
            } else {
                return new DataTablesOutput<>();
            }
        }
        DataTablesOutput output= fcBondDao.getDataTablesUnion(dataTablesInput, sql.toString());
        List<FcBondDetailVO> list = output.getData();
        if(list.size()>0) {
            Map<String, DictDTO> dtoMap = dictService.listMap("common.status");
            //单位
            Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpList);
            for (FcBondDetailVO vo : list) {
                if(vo.getStatus()!=null){
                    if(dtoMap.get(String.valueOf(vo.getStatus())) !=null){
                        vo.setStatusName(dtoMap.get(String.valueOf(vo.getStatus())).getName());
                    }
                }
                vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
                //账号
                setVoBankAcc(vo);
            }
        }
        return output;
    }
    /**
     * 新增保证金
     *
     * @param fcBond
     * @param user
     */
    public void saveFcBondDetail(FcBondDetailVO fcBond, ICurrentUser user) {
        FcBond entity = new FcBond();
        BeanUtils.copyNotNullProperties(fcBond, entity);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_FCBOND_DETAIL));
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(new Date());
        fullBondPropert(entity);
        super.saveEntity(entity);
        Integer messageType = FcBondConstant.MESSAGE_TYPE_TEMP;//暂存
        //启动审批
        if (FcBondConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {//判断是否待审批
            messageType = FcBondConstant.MESSAGE_TYPE_SUBMIT;//提交
            //表示审批的业务，id为fcBond的id，entity为fcBond，单位编号，userId用户编号
             getStartHandler().handler(FcBondConstant.FC_FCBONDDETAIL_FLOW, entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        String msg = "";
        if (FcBondConstant.MESSAGE_TYPE_TEMP.equals(messageType)) {
            msg = CmsProcessMsgConstant.MSGA_INPUT + "";//"录入暂存"
        }
        if (FcBondConstant.MESSAGE_TYPE_SUBMIT.equals(messageType)) {
            msg = CmsProcessMsgConstant.MSGA_INPUT_SUBMIT;//"录入提交"
        }
        //日志记录
        fcBondLogService.saveFcBondMessge(entity.getId(), entity.getBondMoney(), messageType, msg, user);
    }
    private void fullBondPropert(FcBond entity) {
        if (EmptyUtils.isEmpty(entity)) {
            return;
        }
        //TODO 需要补充相关名称信息，暂时后台查补充（前端有值传入）
        if (!StringUtils.isBlank(entity.getBankAccId())) {
            BtBankAccDTO btBankAccDTO = iBtBankAccService.getBankAccById(entity.getBankAccId());
            Optional.ofNullable(btBankAccDTO).ifPresent(btBankAccDO -> {
                entity.setBankAcc(btBankAccDO.getBankAcc());
                entity.setAccName(btBankAccDO.getAccName());
                entity.setBankCode(btBankAccDO.getBankCode());
                entity.setBankName(btBankAccDO.getBankName());
            });
        }
        if (!StringUtils.isBlank(entity.getCorpId())) {
            CorpDTO corpDTO = corpService.get(entity.getCorpId());
            Optional.ofNullable(corpDTO).ifPresent(corpDO -> entity.setCorpName(corpDO.getName()));
        }
        if (!StringUtils.isBlank(entity.getCurId())) {
            BtCurrencyDTO currencyDTO = currencyService.getObjectById(entity.getCurId());
            Optional.ofNullable(currencyDTO).ifPresent(currencyDO -> entity.setCurName(currencyDO.getCurName()));
        }
    }
    /**
     * fusb 2018-09-13
     * 修改存出保证金
     *
     * @param fcBond
     * @param user
     */
    public void updateFcBondDetail(FcBondDetailVO fcBond, ICurrentUser user) {
        FcBond entity = new FcBond();
        BeanUtils.copyNotNullProperties(fcBond, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        fullBondPropert(entity);
        this.saveOrUpdate(entity);
        Integer messageType = FcBondConstant.MESSAGE_TYPE_TEMP;
        //启动审批
        if (FcBondConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = FcBondConstant.MESSAGE_TYPE_SUBMIT;
            getStartHandler().handler(FcBondConstant.FC_FCBONDDETAIL_FLOW, entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        //生成日志记录
        String msg = "";
        if (FcBondConstant.MESSAGE_TYPE_TEMP.equals(messageType)) {
            msg = CmsProcessMsgConstant.MSGA_MODIFY + "";//"修改暂存"
        }
        if (FcBondConstant.MESSAGE_TYPE_SUBMIT.equals(messageType)) {
            msg = CmsProcessMsgConstant.MSGA_REPAIR_MODIFY + "";//"修改提交"
        }
        fcBondLogService.saveFcBondMessge(entity.getId(), entity.getBondMoney(), messageType, msg, user);
        //完成通知提醒
        notificationService.complete(NotificationConstant.NETBANK.FCBOND_APPLY_BACK, entity.getId(), user.getId());
    }
    /**
     * 获取审批列表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(FcBondConstant.FC_FCBONDDETAIL_FLOW, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        DataTablesOutput dp = this.fcBondDao.dataTableApprove(dataTablesInput, businessSql);
        List<FcBondDetailVO> voList = dp.getData();
        if (voList.size() > 0) {
            List<String> accS = new ArrayList<>();
            for (FcBondDetailVO vo : voList) {
                accS.add(vo.getBankAcc());
            }
            Map<String, BtBankAccDTO> accMap = iBtBankAccService.getBankAccByInfoAccs(accS);
            for (FcBondDetailVO vo : voList) {
                BtBankAccDTO accDTO = accMap.get(vo.getBankAcc());
//				N.BANK_NAME  AS bankName,N.BANK_ACC AS bankAcc , N.ACC_NAME AS acc_Name ,
                vo.setBankAcc(accDTO.getBankAcc());
                vo.setBankName(accDTO.getBankName());
                vo.setAccName(accDTO.getAccName());
                vo.setCorpName(accDTO.getCorpName());
                vo.setCurName(accDTO.getCurName());
            }
        }


        return workFlowBusiness.merge(dp);
    }

    /**
     * fusb,2018-9-12添加
     * 根据保证金id,得到保证金信息
     *
     * @param id
     * @return
     */
    public FcBondDetailVO getFcBondById(String id) {
        return getFcBond(id,null);
    }

    /**
     * 根据保证金账号,得到保证金信息
     * @param bondBankAcc
     * @return
     */
    public FcBondDetailVO getFcBondByBondBankAcc(String bondBankAcc) {
        return getFcBond(null,bondBankAcc);
    }

    /**
     * fusb,2018-9-12添加
     * 根据保证金id,得到保证金信息
     *
     * @param id
     * @return
     */
    public FcBondDetailVO getFcBond(String id,String bondBankAcc) {
        FcBondDetailVO vo = fcBondDao.getBondDetail(id,bondBankAcc);
        if(null != vo) {
            //单位
            CorpDTO corp = corpService.get(vo.getCorpId());
            if (null != corp)
                vo.setCorpName(corp.getName());
            setVoBankAcc(vo);
            vo.setInterestRateName(dictService.get("interestRateType", vo.getInterestRate()));
            vo.setCurName(currencyService.getObjectById(vo.getCurId()).getCurName());
        }
        return vo;
    }

    /**
     * 为vo增加账号字段
     * @param vo
     */
    private void setVoBankAcc(FcBondDetailVO vo) {
        if (!StringUtil.isBlank(vo.getBankAccId())) {
            //账号
            BtBankAccDTO bankAccDTO = iBtBankAccService.getBankAccById(vo.getBankAccId());
            if(null != bankAccDTO) {
                vo.setBankAcc(bankAccDTO.getBankAcc());
                vo.setAccName(bankAccDTO.getAccName());
                vo.setBankName(bankAccDTO.getBankName());
                vo.setCurName(bankAccDTO.getCurName());
            }
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    /**
     * 审批回调函数
     *
     * @param workFlowResult
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        FcBond fcBond = this.get(workFlowResult.getBusinessKey());
        FcBondBal fcbondbal = new FcBondBal();
        Integer status = workFlowResult.isPass() ? FcBondConstant.STATUE_CHECK : FcBondConstant.STATUE_BACK;
        fcBond.setStatus(status);
        fcBond.setUpdateBy(workFlowResult.getUser().getLoginName());
        fcBond.setUpdateDate(DateUtils.getDate());
        //审批通过后将数据存入到保证金余额表
        if (status.equals(FcBondConstant.STATUE_CHECK)) {
            fcbondbal.setBondId(fcBond.getId());
            fcbondbal.setBondMoney(fcBond.getBondMoney());
            BigDecimal initMoney = new BigDecimal("0");
            fcbondbal.setAddMoney(initMoney);
            fcbondbal.setApplyMoney(initMoney);
            fcbondbal.setFreezeMoney(initMoney);
            fcbondbal.setBackMoney(initMoney);
            fcBondBalService.saveEntity(fcbondbal);
            //以下是保证金未付款生成付款单的操作，暂时不做
	      /*if(fcBond.getIsPayed().equals(FcBondConstant.PAYED_NOT)){
	         //生成付款单
	        FcBondDetailVO fcBondVO = getFcBondById(fcBond.getId());
	        NisBillheadVO nisBillhead=new NisBillheadVO();
	        //获取网点对应的付款方式
	        List<BtAssignPayWayVO> list=btAssignPayWayService.getByCorpId(fcBondVO.getCorpId());
	        nisBillhead.setCorpId(fcBondVO.getCorpId());//单位
	        nisBillhead.setPayeeAcc(fcBondVO.getBankAcc());//账户
	        nisBillhead.setPayeeAccName(fcBondVO.getAccName());//账户名称
	        nisBillhead.setPayerBank(fcBondVO.getBankName());//开户行名称
	        nisBillhead.setPayMoney(fcBondVO.getBondMoney());//金额
	        nisBillhead.setWishPayday(fcBondVO.getStartDate());//支付日期
	        nisBillhead.setVoucherTypeId(list.get(0).getDefVoucherCode());//付款方式
	        nisBillhead.setPurpose(fcBondVO.getRemark());//用途
	        nisBillhead.setStatus(ExpenseConstant.STATUE_TEMP);//状态
	        nisBillhead.setBillFrom(ExpenseConstant.BILLFROM_1);//单据来源:1-支付结算
	        nisBillhead.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
	        nisBillheadService.saveNisBillhead(nisBillhead, user);
	      }*/
	        //若存出账号为在线且未付款时，需要生成一条暂存状态的头寸调拨单
            if(StringUtils.isNotEmpty(fcBond.getOutBankAcc())) {
                BtBankAccDTO bankDto = iBtBankAccService.getBankAccInfo(fcBond.getOutBankAcc());
                if ("1".equals(bankDto.getIsOnline()) && "0".equals(fcBond.getIsPayed())) {
//                    saveNisAllocationReq(fcBond, bankDto, user);
                }
            }
            //存出保证金审批通过时,发出通知告知已审批通过
            notificationService.send(fcBond.getId(), fcBond.getCorpId(), NotificationConstant.NETBANK.FCBOND_APPLY_PASS, fcBond);
        }
        if (FcBondConstant.STATUE_BACK.equals(status)) {//打回
            //存出保证金审批未通过时,发出待处理消息告知已打回
            notificationService.send(fcBond.getId(), fcBond.getCorpId(), NotificationConstant.NETBANK.FCBOND_APPLY_BACK, fcBond);
        }
        Integer messageType = workFlowResult.isPass() ? FcBondConstant.MESSAGE_TYPE_CHECK_PASS : FcBondConstant.MESSAGE_TYPE_CHECK_BACK;
        String msg = "";
        if (messageType == 5) {
            msg = CmsProcessMsgConstant.MSGA_CHECKOK + "";//审批通过
        } else if (messageType == 4) {
            msg = CmsProcessMsgConstant.MSGA_CHECKBACK;//审批打回
        }
        //日志记录
        fcBondLogService.saveFcBondMessge(fcBond.getId(), fcBond.getBondMoney(), messageType, msg, user);
    }
    /**
     * 查询保证金账户是否已存出
     */
    public boolean queryBondAcc(String bankAccId) {
        return fcBondDao.isBondAccExist(bankAccId);
    }
    /**
     * 改变保证金的状态为删除
     */
    public void deleteBond(String id, ICurrentUser user) {
        FcBond fcBond = this.get(id);
        fcBond.setStatus(FcBondConstant.STATUE_DEL);
        fcBond.setUpdateBy(user.getLoginName());
        fcBond.setUpdateDate(DateUtils.getDate());
        super.updateEntity(fcBond);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(FcBondDetailVO obj) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void verifyHandler(List<FcBondDetailVO> list) {
        // TODO Auto-generated method stub
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FcBond entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<FcBond, String> getEntityDao() {
        return fcBondDao;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = fcBondDao.queryBusBillByCorpId(corpId);
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.BOND_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.BOND_VALID_DEL_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
    /**
     * 根据用户及资源获取保证金额度
     *
     * @param user
     * @param resource
     * @return
     */
    public DepositReportCommonDTO getBondAccBal(ICurrentUser user, String resource, String corpId) {
        DepositReportCommonDTO balDTO = new DepositReportCommonDTO();
        List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
        List<FcBondDetailVO> curMonthData = fcBondDao.getBondAccBal(corpAuth, DateUtils.getDate());
        BigDecimal curBal = this.dealBondBalance(curMonthData, DateUtils.getDate(), corpId);
        Date lastMonthDay = DateUtils.getLastMonthDate(DateUtils.addMonth(DateUtils.getDate(), -1));
        List<FcBondDetailVO> lastMonthData = fcBondDao.getBondAccBal(corpAuth, lastMonthDay);
        BigDecimal lastBal = this.dealBondBalance(lastMonthData, lastMonthDay, corpId);
        balDTO.setBalance(curBal);
        balDTO.setBusType(DepositReportConstant.BUSTYPE_4);
        //月环比=（本期-上期）/上期
        BigDecimal monthOnMonth = NumberUtil.calculateMonthOnMonth(curBal, lastBal);
        balDTO.setMonthOnMonth(monthOnMonth);
        return balDTO;
    }
    /**
     * 保证金额度进行汇率折算
     *
     * @param balList
     * @param preDate
     * @param corpId  当前用户所属单位ID
     * @return
     */
    public BigDecimal dealBondBalance(List<FcBondDetailVO> balList, Date preDate, String corpId) {
        //当前用户下的单位
        CorpDTO corpDTO = corpService.get(corpId);
        String curId = corpDTO.getCurId();
        BigDecimal bondBal = BigDecimal.ZERO;
        for (FcBondDetailVO fcBondBal : balList) {
            //币别相同时不折算
            if (fcBondBal.getCurId().equals(curId)) {
                bondBal = bondBal.add(fcBondBal.getBalMoney());
            } else {
                BigDecimal exchangeRate = rateAdjustService.getExchangeRateBy(fcBondBal.getCurId(), curId, preDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                bondBal = bondBal.add(fcBondBal.getBalMoney().multiply(exchangeRate));
            }
        }
        return bondBal;
    }
    /**
     * 定时任务 到期提醒
     */
    @Override
    @XxlJob("FcBondDetailService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.BOND, BaseDataConstant.EXPIRE_REMIND_DAY);// 期提醒天数
        Integer notifyDays = configDTO.toInteger();
        Date startDate = DateUtils.getDateYMD();
        Date endDate = DateUtils.addDay(startDate, notifyDays);
        //查询到期数据
        List<FcBondDetailVO> bonds = fcBondDao.getBondByDate(startDate,endDate);
        if (!EmptyUtils.isEmptyList(bonds)) {
            for (FcBondDetailVO bond : bonds) {
                notificationService.send(bond.getId(), bond.getCorpId(),
                        NotificationConstant.NETBANK.FCBOND_EXPIRE_REMIND, bond);
            }
        }
    }
    /**
    * <br>
    * <b>功能：</b> 删除头寸调拨单打回保证金 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/8/18 0018 <br>
    */
    public void tempBond(String id, ICurrentUser user) {
        FcBond fcBond = this.get(id);
        fcBond.setStatus(FcBondConstant.STATUE_BACK);//头寸删除后打回保证金状态
        fcBond.setUpdateBy(user.getId());
        fcBond.setUpdateDate(DateUtils.getDate());
        super.updateEntity(fcBond);
        //生成日志记录
        Integer messageType = FcBondConstant.MESSAGE_TYPE_BACK;
        String msg = CmsProcessMsgConstant.MSGA_DELETE_BACK + "";//业务删除打回
        fcBondLogService.saveFcBondMessge(fcBond.getId(), fcBond.getBondMoney(), messageType, msg, user);
    }

    /**
     * 新增保证金-不过审批，系统直接新增
     *
     * @param fcBond
     * @param user
     */
    public FcBondDetailVO addFcBondDetail(FcBondDetailVO fcBond, ICurrentUser user) {
        FcBond entity = new FcBond();
        BeanUtils.copyNotNullProperties(fcBond, entity);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_FCBOND_DETAIL));
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(new Date());
        fullBondPropert(entity);
        entity.setStatus(FcBondConstant.STATUE_CHECK);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        super.saveEntity(entity);
        fcBond.setId(entity.getId());

        //将数据存入到保证金余额表
        FcBondBal fcbondbal = new FcBondBal();
        fcbondbal.setBondId(fcBond.getId());
        fcbondbal.setBondMoney(fcBond.getBondMoney());
        BigDecimal initMoney = new BigDecimal("0");
        fcbondbal.setAddMoney(initMoney);
        fcbondbal.setApplyMoney(initMoney);
        fcbondbal.setFreezeMoney(initMoney);
        fcbondbal.setBackMoney(initMoney);
        fcBondBalService.saveEntity(fcbondbal);

        return fcBond;
    }

    /**
     * 同批导入的保证金汇总到一起
     *
     * @param fcBond
     * @param user
     */
    public void updateExistFcBondDetail(FcBondDetailVO fcBond,BigDecimal bailMoney, ICurrentUser user) {
        FcBond en = new FcBond();
        BeanUtils.copyNotNullProperties(fcBond, en);
        en.setBondMoney(en.getBondMoney().add(bailMoney));
        en.setUpdateDate(new Date());
        en.setUpdateBy(user.getId());
        super.updateEntity(en);
        //汇总余额
        FcBondBal fcBondBal = fcBondBalService.getBondBalById(fcBond.getId());
        fcBondBal.setBondMoney(fcBondBal.getBondMoney().add(bailMoney));
        fcBondBalService.updateEntity(fcBondBal);
    }

    /**
     * 追加保证金-不过审批，系统直接新增
     *
     * @param fcBond
     * @param user
     */
    public void addFcBondAddData(FcBondDetailVO fcBond,BigDecimal bailMoney, ICurrentUser user) {
        FcBondAdd fcBondAdd = new FcBondAdd();
        fcBondAdd.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_FCBOND_ADD));
        fcBondAdd.setBondId(fcBond.getId());
        fcBondAdd.setAddDate(new Date());
        fcBondAdd.setIsPayed("1");
        fcBondAdd.setAddMoney(bailMoney);
        fcBondAdd.setStatus(95);
        fcBondAdd.setCreateBy(user.getLoginName());
        fcBondAdd.setCreateDate(new Date());
        fcBondAdd.setUpdateBy(user.getLoginName());
        fcBondAdd.setUpdateDate(new Date());
        fcBondAddService.saveEntity(fcBondAdd);
        //余额表修改追加金额
        FcBondBal fcBondBal = fcBondBalService.getBondBalById(fcBond.getId());
        fcBondBal.setAddMoney(fcBondBal.getAddMoney().add(bailMoney));
        fcBondBalService.updateEntity(fcBondBal);
    }
}
