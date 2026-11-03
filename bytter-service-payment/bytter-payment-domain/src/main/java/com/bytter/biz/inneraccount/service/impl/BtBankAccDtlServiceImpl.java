//package com.bytter.biz.inneraccount.service.impl;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeMap;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.bytter.core.base.BaseDao;
//import com.bytter.core.base.BaseService;
//import com.bytter.core.base.entity.ICurrentUser;
//import com.bytter.core.bpm.annotation.WorkFlow;
//import com.bytter.core.bpm.persistence.IWorkFlowResult;
//import com.bytter.core.bpm.persistence.WorkFlowBusiness;
//import com.bytter.core.common.datatable.DataTablesInput;
//import com.bytter.core.common.datatable.DataTablesOutput;
//import com.bytter.core.common.exception.ParameterException;
//import com.bytter.core.constant.CommonConstant;
//import com.bytter.core.enums.MatchTypeEnum;
//import com.bytter.core.utils.BeanUtils;
//import com.bytter.core.utils.DateUtils;
//import com.bytter.core.utils.EmptyUtils;
//import com.bytter.core.utils.ListUtils;
//import com.bytter.core.utils.SqlUtil;
//import com.bytter.core.utils.StringUtils;
//import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
//import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
//import com.bytter.system.api.IAccountGrantService;
//import com.bytter.biz.payment.api.service.IBtBankAccDtlService;
//import com.bytter.system.api.IBtBankAccService;
//import com.bytter.system.api.IBtCurrencyService;
//import com.bytter.system.api.ICorpDisableVerifyService;
//import com.bytter.system.api.ICorpService;
//import com.bytter.system.api.IMultiLangService;
//import com.bytter.system.api.INotificationService;
//import com.bytter.system.api.constant.AccountGrantConstant;
//import com.bytter.system.api.constant.CorpVerifyMessageConstant;
//import com.bytter.system.api.constant.NotificationConstant;
//import com.bytter.system.api.constant.SysConfigConstant;
//import com.bytter.system.api.dto.BtBankAccConstant;
//import com.bytter.system.api.dto.BtBankAccDTO;
//import com.bytter.system.api.dto.BtBankAccMessageConstant;
//import com.bytter.system.api.dto.CorpVerifyErrorDTO;
//import com.bytter.system.api.enums.BankAccDtlMessageEnum;
//import com.bytter.core.sequence.SequenceSingleton;
//import com.bytter.system.sequence.enums.SequenceEnum;
//import com.bytter.system.constants.BisAccBalConstant;
//import com.bytter.system.constants.NetbankConstant;
//import com.bytter.system.constants.NisWorkFlowConstant;
//import com.bytter.system.constants.ResourceConstant;
//import com.bytter.biz.inneraccount.dao.BtBankAccDtlDao;
//import com.bytter.biz.inneraccount.entity.BisAccBal;
//import com.bytter.biz.inneraccount.entity.BisAccBalAuto;
//import com.bytter.biz.inneraccount.entity.BisAccDtl;
//import com.bytter.biz.inneraccount.entity.BtBankAccDtl;
//import com.bytter.system.service.BisAccBalAutoService;
//import com.bytter.system.service.BisAccBalService;
//import com.bytter.system.service.BisAccDtlService;
//import com.bytter.system.service.BtBankAccMessageService;
//import com.bytter.system.vo.BtBankAccDtlVO;
//import com.bytter.core.bpm.IWorkFlowListener;
//import com.bytter.core.bpm.IWorkFlowRevokeListener;
//import com.bytter.workflow.api.IWorkFlowService;
///**
// * @author onlineGenerator
// * @version 1.0
// * @Title: Service
// * @Description: 银行账号明细录入表
// * @date 2017-06-29 13:51:01
// */
//@WorkFlow(NisWorkFlowConstant.BT_BANKACC_DTL)
//@Service("btBankAccDtlService")
//@Transactional
//public class BtBankAccDtlServiceImpl extends BaseService<BtBankAccDtl, String> implements IWorkFlowListener, IUserExcelVerifyHandler<BtBankAccDtlVO>, IWorkFlowRevokeListener, ICorpDisableVerifyService,IBtBankAccDtlService {
//	@Autowired
//	private BtBankAccDtlDao btBankAccDtlDao;
//	@Autowired(required=false)
//	private IBtBankAccService iBtBankAccService;
//	@Autowired
//	private BisAccBalService bisAccBalService;
//	@Autowired
//	private BisAccDtlService bisAccDtlService;
//	@Autowired(required=false)
//	private ICorpService corpService;
//	@Autowired(required=false)
//	private IWorkFlowService workFlowService;
//	@Autowired(required=false)
//	private IBtCurrencyService btCurrencyService;
//	@Autowired
//	private BtBankAccMessageService btBankAccMessageService;
//	@Autowired(required=false)
//	private INotificationService notificationService;
//	@Autowired
//	private IAccountGrantService accountGrantService;
//	@Autowired(required = false)
//	private IMultiLangService multiLangService;
//	@Autowired(required = false)
//	private BisAccBalAutoService bisAccBalAutoService;
//
//	/**
//	 * 删除对象
//	 *
//	 * @param entity
//	 */
//	@Override
//	public void deleteEntity(BtBankAccDtl entity) {
//		super.deleteEntity(entity);
//	}
//
//	/**
//	 * 根据id删除
//	 *
//	 * @param id
//	 */
//	@Override
//	public void delete(String id) {
//		super.delete(id);
//	}
//
//	/**
//	 * 保存
//	 *
//	 * @param entity
//	 */
//	public void save(BtBankAccDtl entity) {
//		super.saveEntity(entity);
//	}
//
//	/**
//	 * 更新
//	 *
//	 * @param entity
//	 */
//	public void update(BtBankAccDtl entity) {
//		super.updateEntity(entity);
//	}
//
//	/**
//	 * 保存or更新
//	 *
//	 * @param entity
//	 */
//	@Override
//	public void saveOrUpdate(BtBankAccDtl entity) {
//		super.saveOrUpdate(entity);
//	}
//
//
//	@Override
//	public BaseDao<BtBankAccDtl, String> getEntityDao() {
//		return btBankAccDtlDao;
//	}
//
//	/**
//	 * 保存银行账户流水录入信息
//	 *
//	 * @param entity
//	 */
//	public void saveBtBankAccDtl(BtBankAccDtl entity, ICurrentUser user) {
//		if (this.checkSave(entity)) {
//			// 设置单号
//			String accNo = SequenceSingleton.getSequence(SequenceEnum.BT_BANK_ACC_DTL);
//			entity.setAccNo(accNo);
//			// 设置排序号
//			if(EmptyUtils.isEmpty(entity.getOrderNo())) {
//				Integer orderNo = this.btBankAccDtlDao.getOrderNo(entity.getTransTime(), entity.getBankAcc(), entity.getCurId());
//				entity.setOrderNo(orderNo + 1);
//			}
//			if(entity.getBal() == null) {
//				BigDecimal newBal = this.getDownBankBal(entity.getCdSign(), entity.getRbSign(), entity.getBankAcc(), entity.getAmt().toString(), entity.getTransTime(), entity.getCurId(), null);
//				entity.setBal(newBal);
//			}
//			// 当前操作人信息
//			Date curDate = DateUtils.getDate();
//			String loginName = user.getLoginName();
//			entity.setCreateBy(loginName);
//			entity.setCreateDate(curDate);
//			entity.setUpdateBy(loginName);
//			entity.setUpdateDate(curDate);
//			this.save(entity);
//
//			this.updateAfterBtBankAccDtl(entity);
//			// 挂接工作流
//			if (BisAccBalConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
//				 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_DTL, entity.getId(), entity, entity.getCorpId(), user.getId());
//				//生成日志
//				btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//			} else {
//				//生成日志
//				btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_ADD, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//			}
//		}
//	}
//
//	/**
//	 * 保存银行账户流水录入信息
//	 *
//	 * @param entity
//	 */
//	public void updateBtBankAccDtl(BtBankAccDtl entity, ICurrentUser user) {
//		if (this.checkSave(entity)) {
//			entity.setUpdateBy(user.getLoginName());
//			entity.setUpdateDate(DateUtils.getDate());
//			if(entity.getBal() == null) {
//				BigDecimal newBal = this.getDownBankBal(entity.getCdSign(), entity.getRbSign(), entity.getBankAcc(), entity.getAmt().toString(), entity.getTransTime(), entity.getCurId(), entity.getOrderNo());
//				entity.setBal(newBal);
//			}
//			this.saveOrUpdate(entity);
//			//this.updateAfterBtBankAccDtl(entity);
//			Integer oldStatus = entity.getStatus();
//			BisAccDtl bisAccDtl = bisAccDtlService.getBisAccDtlByAccNo(entity.getAccNo());
//			if(bisAccDtl != null && (bisAccDtl.getStatus() == null ||
//					 BisAccBalConstant.STATUE_CHECK.toString().equals(bisAccDtl.getStatus().toString()))){
//				//获取对应的流水更新为删除（界面不可见）
//				bisAccDtl.setStatus(BtBankAccConstant.STATUE_DEL);
//				bisAccDtl.setUpdateBy(user.getLoginName());
//				bisAccDtl.setUpdateDate(DateUtils.getDate());
//				this.getSession().merge(bisAccDtl);
//				oldStatus = BisAccBalConstant.STATUE_CHECK;
//			}
//			// 挂接工作流
//			if (!BisAccBalConstant.STATUE_TEMP.equals(entity.getStatus())) {
//				 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_DTL, entity.getId(), entity, entity.getCorpId(), user.getId());
//				//生成日志
//				Integer messageType = BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT;
//				if(BisAccBalConstant.STATUE_CHECK.toString().equals(oldStatus.toString())){
//					messageType = BtBankAccMessageConstant.MESSAGE_TYPE_CHANGE_SUBMIT;
//				}
//				btBankAccMessageService.saveMessage(entity.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//			} else {
//				//生成日志
//				Integer messageType = BtBankAccMessageConstant.MESSAGE_TYPE_UPDATE;
//				if(BisAccBalConstant.STATUE_CHECK.toString().equals(oldStatus.toString())){
//					messageType = BtBankAccMessageConstant.MESSAGE_TYPE_CHANGE_UPDATE;
//				}
//				btBankAccMessageService.saveMessage(entity.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//			}
//		}
//	}
//
//	/**
//	 * 将审批通过后的流水录入信息保存到流水明细表中
//	 *
//	 * @param entity
//	 */
//	public void saveBisAccDtl(BtBankAccDtl entity) {
//		BisAccDtl bisAccDtl = this.bisAccDtlService.getBisAccDtlByAccNo(entity.getAccNo());
//		if(bisAccDtl == null) {
//			bisAccDtl = new BisAccDtl();
//			BeanUtils.copyNotNullProperties(entity, bisAccDtl);
//			bisAccDtl.setStatus(null);
//			bisAccDtl.setId(null);
//			//勾兑标志默认为0-未勾兑
//			bisAccDtl.setBlendSign(CommonConstant.NO);
//			bisAccDtl.setFbsStatus(CommonConstant.NO);
//			this.bisAccDtlService.save(bisAccDtl);
//		} else {
//			bisAccDtl.setOppAccNo(entity.getOppAccNo());
//			bisAccDtl.setOppAccName(entity.getOppAccName());
//			bisAccDtl.setOppAccBank(entity.getOppAccBank());
//			bisAccDtl.setCdSign(entity.getCdSign());
//			bisAccDtl.setAmt(entity.getAmt());
//			bisAccDtl.setBal(entity.getBal());
//			bisAccDtl.setTransTime(entity.getTransTime());
//			bisAccDtl.setPostscript(entity.getPostscript());
//			bisAccDtl.setProjectRemark(entity.getProjectRemark());
//			bisAccDtl.setBankSerialNo(entity.getBankSerialNo());
//			bisAccDtl.setRmk(entity.getRemark());
//			bisAccDtl.setStatus(BtBankAccConstant.STATUE_CHECK);
//			this.getSession().merge(bisAccDtl);
//		}
//		//this.bisAccDtlService.updateAfterBtBankAccDtl(bisAccDtl, entity.getOrderNo());
//		//更新自动更新离线账户历史余额记录表
//		BtBankAccDTO bankAcc = iBtBankAccService.getBankAccInfo(bisAccDtl.getBankAcc());
//		//先根据账号id和币别id取余额自动计算信息表信息
//		BisAccBalAuto auto = bisAccBalAutoService.getBisAccBalAutoByAccCurId(bankAcc.getId(), bisAccDtl.getCurId());
//		if (auto != null && auto.getEndDate().after(bisAccDtl.getTransTime())) {
//			auto.setEndDate(DateUtils.parseDate(DateUtils.format(bisAccDtl.getTransTime(),DateUtils.PATTERN_CLASSICAL_SIMPLE)));
//			bisAccBalAutoService.updateEntity(auto);
//		}
//	}
//
//	/**
//	 * 银行账户流水管理
//	 *
//	 * @param dataTablesInput
//	 * @param user
//	 * @return
//	 */
//	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
//		StringBuilder sql = new StringBuilder();
//		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_MANAGERQUERY, user.getId()), "t.corp_id", true);
//		sql.append(" and ").append(corpAuthSql);
//		sql.append(" and t.create_by ='").append(user.getLoginName()).append("'");
//		return this.btBankAccDtlDao.getDataTablesUnion(dataTablesInput, sql.toString());
//	}
//
//
//	public boolean checkSave(BtBankAccDtl entity) {
//		if (entity.getBal().compareTo(BigDecimal.ZERO) < -1) {
//			throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_AMT_NOTZERO);
//		}
//		BtBankAccDTO btBankAccDTO = iBtBankAccService.getBankAccInfo(entity.getBankAcc());
//		if (EmptyUtils.isEmpty(btBankAccDTO) || EmptyUtils.isEmpty(btBankAccDTO.getRegDate())) {
//			throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_BANKACC_REGDATE_NULL);
//		}
//		if (!btBankAccDTO.getRegDate().before(entity.getTransTime())) {
//			throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_TRANSTIME_LT_ACCREDDATE_DTL, entity.getBankAcc(),DateUtils.formatDate(entity.getTransTime()));
//		}
//		//流水交易日期大于系统当前日期
//		if (DateUtils.getDate().before(entity.getTransTime())) {
//			throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_TRANSTIME_LT_SYSDATE);
//		}
//		Date endDtlDate = this.iBtBankAccService.getLastEndDtlDate(btBankAccDTO.getId(), entity.getCurId());
//		if (!EmptyUtils.isEmpty(endDtlDate)) {
//			if (!endDtlDate.before(entity.getTransTime())) {
//				throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_BALDATE_LT_COMPLETEDATE, entity.getBankAcc(), DateUtils.format(endDtlDate));
//			}
//		}
//		return true;
//	}
//
//	/**
//	 * 根据id查找银行账户流水信息
//	 * zuoy 2017-07-03
//	 *
//	 * @param id
//	 * @return BtBankAccDtlVO
//	 */
//	public com.bytter.system.api.vo.BtBankAccDtlVO getByUnion(String id) {
//		com.bytter.system.vo.BtBankAccDtlVO vo = this.btBankAccDtlDao.getByUnion(id);
//		com.bytter.system.api.vo.BtBankAccDtlVO bvo = new com.bytter.system.api.vo.BtBankAccDtlVO();
//		BeanUtils.copyProperties(vo, bvo);
//		return bvo;
//	}
//
//	/**
//	 * 获取交易日期
//	 *
//	 * @param bankAcc   账户
//	 * @param curId     币别ID
//	 * @param transTime 比较日期，为null则查询最后一次
//	 * @param operator  跟transTime组合使用，日期比较运算符，参照MatchTypeEnum
//	 * @return
//	 */
//	public Date getLastTransTime(String bankAcc, String curId, Date transTime, String operator) {
//		return this.btBankAccDtlDao.getLastTransTime(bankAcc, curId, transTime, operator);
//	}
//
//	/**
//	 * 获取最近一次的余额
//	 *
//	 * @param bankAcc 账户
//	 * @param curId   币别ID
//	 * @return
//	 */
//	public BigDecimal getLastAmtByTransTime(String bankAcc, String curId) {
//		return this.getAmtByTransTime(bankAcc, curId, null, null);
//	}
//
//	/**
//	 * 获取指定日期的余额
//	 *
//	 * @param bankAcc   账户
//	 * @param curId     币别ID
//	 * @param transTime 日期
//	 * @return
//	 */
//	public BigDecimal getAmtByTransTime(String bankAcc, String curId, Date transTime) {
//		if (EmptyUtils.isEmpty(transTime)) {
//			return null;
//		}
//		return this.getAmtByTransTime(bankAcc, curId, transTime, MatchTypeEnum.EQ.getValue());
//	}
//
//	/**
//	 * 获取指定日期的余额，若指定日期不存在余额，则查询最近一次
//	 *
//	 * @param bankAcc   账户
//	 * @param curId     币别ID
//	 * @param transTime 日期
//	 * @return
//	 */
//	public BigDecimal getCurAmtOrLastAmt(String bankAcc, String curId, Date transTime) {
//		BigDecimal curAmt = this.getAmtByTransTime(bankAcc, curId, transTime);
//		if (!EmptyUtils.isEmpty(curAmt)) {
//			return curAmt;
//		}
//		return this.getLastAmtByTransTime(bankAcc, curId);
//	}
//
//	/**
//	 * 获取指定日期的余额
//	 *
//	 * @param bankAcc   账户
//	 * @param curId     币别ID
//	 * @param transTime 比较日期,为null则查询最后一次
//	 * @param operator  跟transTime组合使用，日期比较运算符，参照MatchTypeEnum
//	 * @return
//	 */
//	public BigDecimal getAmtByTransTime(String bankAcc, String curId, Date transTime, String operator) {
//		if (EmptyUtils.isNotEmpty(operator) && EmptyUtils.isEmpty(transTime)) {
//			return null;
//		}
//		return this.btBankAccDtlDao.getAmtByTransTime(bankAcc, curId, transTime, operator);
//	}
//
//	/**
//	 * 计算余额
//	 *
//	 * @param cdSign    借贷标志 0-收入 1-支出
//	 * @param rbSign    红蓝标志 1-红 2-蓝
//	 * @param bankAcc   银行账户
//	 * @param amt       交易金额
//	 * @param transTime 交易时间
//	 * @param curId     币别ID
//	 * @return
//	 */
//	public BigDecimal getDownBankBal(String cdSign, String rbSign, String bankAcc, String amt, Date transTime, String curId, Integer orderNo) {
//		// 当前输入交易金额
//		BigDecimal curAmt = EmptyUtils.isNumeric(amt) ? new BigDecimal(amt) : BigDecimal.ZERO;
//		if (EmptyUtils.isEmpty(orderNo)) {
//			orderNo = this.btBankAccDtlDao.getOrderNo(transTime, bankAcc, curId);
//			orderNo = orderNo + 1;
//		}
//		// 最近一笔流水余额
//		BigDecimal lastBal = this.btBankAccDtlDao.lastByTransTime(bankAcc, curId, transTime, orderNo);
//		if (EmptyUtils.isEmpty(lastBal)) {
//			//第一笔流水则取初始化余额
//			BtBankAccDTO bankAccDTO = iBtBankAccService.getBankAccInfo(bankAcc);
//			if(bankAccDTO !=null && bankAccDTO.getInitBalance()!=null && DateUtils.dayDiffs(transTime,bankAccDTO.getInitBalanceDate())>0){
//				lastBal = bankAccDTO.getInitBalance();
//			}else{
//				lastBal = BigDecimal.ZERO;
//			}
//		}
//		boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(cdSign) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(rbSign);
//		boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(cdSign) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(rbSign);
//		if (outBlue || inRed) {
//			//支出(蓝) 收入(红)
//			return lastBal.subtract(curAmt);
//		} else {    //收入(蓝) 支出(红)
//			return lastBal.add(curAmt);
//		}
//
//	}
//
//	/**
//	 * 查询某账户本月未提交的流水记录
//	 *
//	 * @param bankAcc 账户
//	 * @param curId   币别ID
//	 * @param month   月份
//	 * @return
//	 */
//	public List<BtBankAccDtlVO> getTempDtlByMonth(String bankAcc, String curId, Integer month) {
//		return this.btBankAccDtlDao.getTempDtlByMonth(bankAcc, curId, month);
//	}
//
//	/**
//	 * 批量提交暂存的流水记录
//	 *
//	 * @param btBankAccDtlVO
//	 * @return
//	 */
//	public void batchSubmitTempDtl(BtBankAccDtlVO btBankAccDtlVO, ICurrentUser user) {
//		List<Map<String, Object>> tempBtBankAccDtlList = btBankAccDtlVO.getTempBtBankAccDtlList();
//		if (!ListUtils.isNullOrEmpty(tempBtBankAccDtlList)) {
//			Set<String> btBankAccDtlIds=new HashSet<>();
//			for (Map<String, Object> map : tempBtBankAccDtlList) {
//				String tempDtlId = String.valueOf(map.get("tempDtlId"));
//				if (!EmptyUtils.isBlank(tempDtlId)) {
//					btBankAccDtlIds.add(tempDtlId);
//				}
//			}
//			for (String id : btBankAccDtlIds) {
//				BtBankAccDtl entity = this.get(id);
//				entity.setStatus(BisAccBalConstant.STATUE_WAIT_CHECK);
//				// 挂接工作流
//				if (!BisAccBalConstant.STATUE_TEMP.equals(entity.getStatus())) {
//					 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_DTL, entity.getId(), entity, entity.getCorpId(), user.getId());
//					//生成日志
//					btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 调整排序号
//	 *
//	 * @param btBankAccDtlVO
//	 */
//	public void updateOrderNo(BtBankAccDtlVO btBankAccDtlVO) {
//		List<Map<String, Object>> tempBtBankAccDtlList = btBankAccDtlVO.getTempBtBankAccDtlList();
//		if (!EmptyUtils.isEmptyList(tempBtBankAccDtlList)) {
//			List<BtBankAccDtl> afterList = new ArrayList<>();
//			BtBankAccDtl tempDtl = null;
//			for (Map<String, Object> map : tempBtBankAccDtlList) {
//				String tempDtlId = String.valueOf(map.get("tempDtlId"));
//				String orderNo = String.valueOf(map.get("tempOrderNo"));
//				if (!EmptyUtils.isBlank(tempDtlId)) {
//					BtBankAccDtl entity = this.get(tempDtlId);
//					if (EmptyUtils.isBlank(orderNo)) {
//						throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_ORDERNO_NULL);
//					}
//					if (!EmptyUtils.isNumeric(orderNo)) {
//						throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_ORDERNO_NUMBER);
//					}
//					entity.setOrderNo(Integer.parseInt(orderNo));
//					afterList.add(entity);
//					if (EmptyUtils.isEmpty(tempDtl)) {
//						tempDtl = entity;
//					} else {
//						if (tempDtl.getOrderNo() > entity.getOrderNo()) {
//							tempDtl = entity;
//						}
//					}
//				}
//			}
//			Collections.sort(afterList, new Comparator<BtBankAccDtl>() {
//				@Override
//				public int compare(BtBankAccDtl o1, BtBankAccDtl o2) {
//					// 按照学生的年龄进行降序排列
//					if (o2.getOrderNo() > o1.getOrderNo()) {
//						return -1;
//					}
//					return 1;
//				}
//			});
//
//			// 更新余额
//			if (!EmptyUtils.isEmptyList(afterList)) {
//				BigDecimal lastBal = this.btBankAccDtlDao.lastByTransTime(tempDtl.getBankAcc(), tempDtl.getCurId(), tempDtl.getTransTime(), tempDtl.getOrderNo());
//				if (EmptyUtils.isEmpty(lastBal)) {
//					lastBal = this.bisAccBalService.getLastByBalDate(tempDtl.getBankAcc(), tempDtl.getCurId(), true);
//				}
//				if (EmptyUtils.isEmpty(lastBal)) {
//					lastBal = BigDecimal.ZERO;
//				}
//				List<BtBankAccDtl> newList = new ArrayList<>();
//				for (BtBankAccDtl dtl : afterList) {
//					boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
//					boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
//					if (outBlue || inRed) {
//						//支出(蓝) 收入(红)
//						dtl.setBal(lastBal.subtract(dtl.getAmt()));
//					} else {
//						//收入(蓝) 支出(红)
//						dtl.setBal(lastBal.add(dtl.getAmt()));
//					}
//					lastBal = dtl.getBal();
//					newList.add(dtl);
//				}
//			}
//		}
//	}
//
//	@Override
//	public void flowStarted(IWorkFlowResult workFlowResult) {
//
//	}
//
//	@Override
//	public void flowCompleted(IWorkFlowResult workFlowResult) {
//		ICurrentUser user = workFlowResult.getUser();
//		BtBankAccDtl entity = this.get(workFlowResult.getBusinessKey());
//		Integer status = workFlowResult.isPass() ? BisAccBalConstant.STATUE_CHECK : BisAccBalConstant.STATUE_BACK;
//		entity.setStatus(status);
//		entity.setUpdateBy(user.getLoginName());
//		entity.setUpdateDate(DateUtils.getDate());
//		this.saveOrUpdate(entity);
//		// 审批结束保存到流水记录表
//		if (BisAccBalConstant.STATUE_CHECK.intValue() == status.intValue()) {
//			saveBisAccDtl(entity);
//		}
//		Integer messageType = workFlowResult.isPass() ? BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_PASS : BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_BACK;
//		//生成日志
//		btBankAccMessageService.saveMessage(entity.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//	}
//
//	/**
//	 * 获取银行账户流水审批列表信息
//	 *
//	 * @param dataTablesInput
//	 * @return
//	 */
//	public DataTablesOutput getDataTablesApprove(DataTablesInput dataTablesInput) {
//		WorkFlowBusiness workFlowBusiness = this.workFlowService.getWorkFlowBusiness(NisWorkFlowConstant.BT_BANKACC_DTL, dataTablesInput);
//		String idsSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "t.id", true);
//		DataTablesOutput dp = this.btBankAccDtlDao.getDataTablesApprove(dataTablesInput, idsSql);
//		return workFlowBusiness.merge(dp);
//	}
//
//	/**
//	 * 导入功能
//	 *
//	 * @param list
//	 */
//	public void saveImport(List<BtBankAccDtlVO> list, ICurrentUser user) {
//		String loginName = user.getLoginName();
//		Date curDate = DateUtils.getDate();
//		Map<String, Integer> orderMap = new HashMap<>();
//		List<BtBankAccDtl> newList = new ArrayList<>();
//		// 设置值
//		for (BtBankAccDtlVO vo : list) {
//			BtBankAccDtl btBankAccDtl = new BtBankAccDtl();
//			BeanUtils.copyNotNullProperties(vo, btBankAccDtl);
//			// 设置单号
//			String accNo = SequenceSingleton.getSequence(SequenceEnum.BT_BANK_ACC_DTL);
//			btBankAccDtl.setAccNo(accNo);
//			if(vo.getOrderNo() != null){
//				orderMap.put(btBankAccDtl.getBankAcc() + DateUtils.format(btBankAccDtl.getTransTime()) + btBankAccDtl.getCurId(), vo.getOrderNo());
//			} else {
//				// 设置排序号
//				Integer orderNo = this.btBankAccDtlDao.getOrderNo(btBankAccDtl.getTransTime(), btBankAccDtl.getBankAcc(), btBankAccDtl.getCurId());
//				if (orderMap.containsKey(btBankAccDtl.getBankAcc() + DateUtils.format(btBankAccDtl.getTransTime()) + btBankAccDtl.getCurId())) {
//					btBankAccDtl.setOrderNo(orderMap.get(btBankAccDtl.getBankAcc() + DateUtils.format(btBankAccDtl.getTransTime()) + btBankAccDtl.getCurId()) + 1);
//					orderMap.put(btBankAccDtl.getBankAcc() + DateUtils.format(btBankAccDtl.getTransTime()) + btBankAccDtl.getCurId(), btBankAccDtl.getOrderNo());
//				} else {
//					btBankAccDtl.setOrderNo(orderNo + 1);
//					orderMap.put(btBankAccDtl.getBankAcc() + DateUtils.format(btBankAccDtl.getTransTime()) + btBankAccDtl.getCurId(), btBankAccDtl.getOrderNo());
//				}
//			}
//			btBankAccDtl.setRbSign(BisAccBalConstant.RBSIGN_BLUE);
//			if(EmptyUtils.isEmpty(btBankAccDtl.getBal())) {
//				btBankAccDtl.setBal(BigDecimal.ZERO);
//			}
//			btBankAccDtl.setStatus(NetbankConstant.STATUE_TEMP);
//			btBankAccDtl.setCreateBy(loginName);
//			btBankAccDtl.setCreateDate(curDate);
//			newList.add(btBankAccDtl);
//		}
//		//导入先不计算余额，直接入库
//		this.batchSave(newList);
//		//batchUpdateAfterBtBankAccDtl(newList, user);
//	}
//
//	@Override
//	public ExcelVerifyHanlderResult verifyHandler(BtBankAccDtlVO vo,Object... user) {
//		return null;
//	}
//
//	@Override
//	public void verifyHandler(List<BtBankAccDtlVO> list,Object... user) {
//		if (!EmptyUtils.isEmptyList(list)) {
//			//银行账号集合
//			HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
//			//币种集合
//			HashMap<String, String> curMap = new HashMap<>();
//			//用户
//			ICurrentUser userInfo=(ICurrentUser)user[0];
//			BtBankAccDTO bankAcc = null;
//			for(BtBankAccDtlVO vo:list){
//				List<String> msgList = new ArrayList<>();
//				// 币别不为空
//				if (EmptyUtils.isBlank(vo.getCurName())) {
//					msgList.add("nis.netbank.message.curname.null");
//				} else {
//					// 判断币别是否在系统中存在
//					String curId = curMap.get(vo.getCurName());
//					if (StringUtils.isBlank(curId)) {
//						curId = btCurrencyService.getCurrencyByName(vo.getCurName());
//						curMap.put(vo.getCurName(), curId);
//					}
//					if (EmptyUtils.isBlank(curId)) {
//						msgList.add("nis.netbank.message.cur.null");
//					} else {
//						vo.setCurId(curId);
//					}
//				}
//				// 银行账号不为空
//				if (EmptyUtils.isBlank(vo.getBankAcc())) {
//					msgList.add("nis.netbank.message.dtl.bankacc.null");
//				} else {
//					//币别不为空
//					if(StringUtils.isNotBlank(vo.getCurId())) {
//						if (bankAccMap.containsKey(vo.getBankAcc()+CommonConstant.COMMA_SPLIT+vo.getCurId())) {
//							bankAcc = bankAccMap.get(vo.getBankAcc()+CommonConstant.COMMA_SPLIT+vo.getCurId());
//							vo.setCorpId(bankAcc.getCorpId());
//							vo.setBifCode(bankAcc.getBifCode());
//							vo.setBankName(bankAcc.getBankName());
//							vo.setAccName(bankAcc.getAccName());
//						} else {
//							// 判断银行账号是否在系统中存在
//							bankAcc = this.iBtBankAccService.getBankAccByBankAccAndCurId(vo.getBankAcc(), vo.getCurId());
//							if (EmptyUtils.isEmpty(bankAcc)) {
//								msgList.add("nis.netbank.message.dtl.bankaccinfo.null");
//							} else if (BtBankAccConstant.ISONLINE_1.equalsIgnoreCase(bankAcc.getIsOnline())) {
//								// 只能导入离线账号
//								msgList.add("nis.netbank.message.dtl.online.no");
//							} else {
//								Boolean flag = accountGrantService.checkPermissionByUserAndAccount(userInfo.getId(), vo.getBankAcc(), AccountGrantConstant.OFFLINE_BANK_TYPE,
//										AccountGrantConstant.QUERY_DETAIL, ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY);
//								//为空说明无权限
//								if (flag) {
//									msgList.add("nis.netbank.message.bankacc.detail");
//								} else {
//									vo.setCorpId(bankAcc.getCorpId());
//									vo.setBifCode(bankAcc.getBifCode());
//									vo.setBankName(bankAcc.getBankName());
//									vo.setAccName(bankAcc.getAccName());
//									bankAccMap.put(vo.getBankAcc()+CommonConstant.COMMA_SPLIT+vo.getCurId(), bankAcc);
//								}
//							}
//						}
//					}
//				}
//
//				// 收支标志
//				if (EmptyUtils.isEmpty(vo.getCdSign())) {
//					msgList.add("nis.netbank.message.cdsign.null");
//				} else if (!BisAccBalConstant.CDSIGN_OUT.equals(vo.getCdSign())
//						&& !BisAccBalConstant.CDSIGN_IN.equals(vo.getCdSign())) {
//					msgList.add("nis.netbank.message.cdsign.format");
//				}
//				// 交易时间不为空
//				if (EmptyUtils.isEmpty(vo.getTransTime())) {
//					msgList.add("nis.netbank.message.transtime.null");
//				} else {
//					Calendar c = Calendar.getInstance();
//					c.setTime(vo.getTransTime());
//					vo.setMonths(c.get(Calendar.MONTH) + 1);
//					//校验交易日期不能大于系统当前日期
//					if(vo.getTransTime().after(DateUtils.getDate())){
//						msgList.add("bt.bankaccdtl.message.transtime.lt.sysdate");
//					}
//				}
//				//明细截止时间校验
//				if (bankAcc != null && StringUtils.isNotBlank(bankAcc.getId()) && vo.getTransTime() != null) {
//					Date endDtlDate = this.iBtBankAccService.getLastEndDtlDate(bankAcc.getId(), vo.getCurId());
//					if (!EmptyUtils.isEmpty(endDtlDate)) {
//						if (!endDtlDate.before(vo.getTransTime())) {
//							msgList.add("bt.bankaccdtl.import.message.baldate.lt.completedate");
//						}
//					}
//				}
//				// 金额不能为空
//				if (EmptyUtils.isEmpty(vo.getAmt())) {
//					msgList.add("nis.netbank.message.amt.null");
//				}
//				vo.addErrorMsg(msgList);
//			}
//		}
//	}
//
//	/**
//	 * 获取调整列表的数据
//	 *
//	 * @param dataTablesInput
//	 * @return
//	 */
//	public DataTablesOutput getDataTablesAdjust(DataTablesInput dataTablesInput) {
//		return this.btBankAccDtlDao.getDataTablesAdjust(dataTablesInput);
//	}
//
//	/**
//	 * 删除
//	 *
//	 * @param id
//	 */
//	public void deleteBtBankAccDtl(String id, ICurrentUser user) {
//		BtBankAccDtl btBankAccDtl = this.get(id);
//		btBankAccDtl.setStatus(BtBankAccConstant.STATUE_DEL);
//		btBankAccDtl.setUpdateBy(user.getLoginName());
//		btBankAccDtl.setUpdateDate(DateUtils.getDate());
//		this.saveOrUpdate(btBankAccDtl);
//		this.updateAfterBtBankAccDtl(btBankAccDtl);
//		Integer messageType = BtBankAccMessageConstant.MESSAGE_TYPE_DELETE;
//		//同步更新银行流水表数据的状态
//		BisAccDtl bisAccDtl = bisAccDtlService.getBisAccDtlByAccNo(btBankAccDtl.getAccNo());
//		if(bisAccDtl != null){
//			bisAccDtl.setStatus(BtBankAccConstant.STATUE_DEL);
//			bisAccDtl.setUpdateBy(user.getLoginName());
//			bisAccDtl.setUpdateDate(DateUtils.getDate());
//			this.bisAccDtlService.saveOrUpdate(bisAccDtl);
//			messageType = BtBankAccMessageConstant.MESSAGE_TYPE_DEFEAT;
//		}
//		//生成日志
//		btBankAccMessageService.saveMessage(btBankAccDtl.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//		//完成通知提醒
//		notificationService.complete(NotificationConstant.NETBANK.BANKACC_DTL, btBankAccDtl.getId(), user.getId());
//	}
//
//	/**
//	 * 批量删除
//	 *
//	 * @param ids
//	 */
//	public void batchDeleteBtBankAccDtl(String ids, String opType, ICurrentUser user) {
//		String[] dtlIds = ids.split(CommonConstant.COMMA_SPLIT);
//		for (String id : dtlIds) {
//			BtBankAccDtl btBankAccDtl = this.get(id);
//			if(CommonConstant.YES.equals(opType)) {
//				//作废
//				if (BtBankAccConstant.STATUE_CHECK.intValue() != btBankAccDtl.getStatus().intValue()) {
//					throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_DELETE_CHECK);
//				}
//			} else {
//				if (!(BtBankAccConstant.STATUE_TEMP.intValue() == btBankAccDtl.getStatus().intValue()
//						|| BtBankAccConstant.STATUE_BACK.intValue() == btBankAccDtl.getStatus().intValue())) {
//					throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_DELETE_TEMP_OR_BACK);
//				}
//			}
//		}
//		List<BtBankAccDtl> newList = new ArrayList<>();
//		Integer messageType = BtBankAccMessageConstant.MESSAGE_TYPE_DELETE;
//		for (String id : dtlIds) {
//			BtBankAccDtl btBankAccDtl = this.get(id);
//			btBankAccDtl.setUpdateBy(user.getLoginName());
//			btBankAccDtl.setUpdateDate(DateUtils.getDate());
//			btBankAccDtl.setStatus(BtBankAccConstant.STATUE_DEL);
//			newList.add(btBankAccDtl);
//			//同步更新银行流水表数据的状态
//			BisAccDtl bisAccDtl = bisAccDtlService.getBisAccDtlByAccNo(btBankAccDtl.getAccNo());
//			if(bisAccDtl != null){
//				bisAccDtl.setStatus(BtBankAccConstant.STATUE_DEL);
//				bisAccDtl.setUpdateBy(user.getLoginName());
//				bisAccDtl.setUpdateDate(DateUtils.getDate());
//				this.bisAccDtlService.saveOrUpdate(bisAccDtl);
//				messageType = BtBankAccMessageConstant.MESSAGE_TYPE_DEFEAT;
//			}
//			//生成日志
//			btBankAccMessageService.saveMessage(btBankAccDtl.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//			//完成通知提醒
//			notificationService.complete(NotificationConstant.NETBANK.BANKACC_DTL, btBankAccDtl.getId(), user.getId());
//		}
//		batchUpdateAfterBtBankAccDtl(newList, user);
//	}
//
//	/**
//	 * 银行账号明细补录
//	 *
//	 * @param bisAccDtl
//	 * @return
//	 */
//	public void modifyMakeup(BisAccDtl bisAccDtl) {
//		BtBankAccDtl btBankAccDtl = this.btBankAccDtlDao.getUniqueBy("accNo", bisAccDtl.getAccNo());
//		if (!EmptyUtils.isEmpty(btBankAccDtl)) {
//			btBankAccDtl.setOppAccNo(bisAccDtl.getOppAccNo());
//			btBankAccDtl.setOppAccName(bisAccDtl.getOppAccName());
//			btBankAccDtl.setOppAccBank(bisAccDtl.getOppAccBank());
//			btBankAccDtl.setUses(bisAccDtl.getUses());
//			btBankAccDtl.setRemark(bisAccDtl.getRemark());
//			this.btBankAccDtlDao.saveOrUpdate(btBankAccDtl);
//		}
//	}
//
//	/**
//	 * 更新指定交易时间后的所有余额
//	 *
//	 * @param entity
//	 */
//	public void updateAfterBtBankAccDtl(BtBankAccDtl entity) {
//		getSession().flush();
//		getSession().clear();
//		List<BtBankAccDtl> afterList = this.btBankAccDtlDao.listAfterBtBankAccDtl(entity.getBankAcc(), entity.getCurId(), entity.getTransTime(), entity.getOrderNo(), null);
//		// 更新余额
//		if (!EmptyUtils.isEmptyList(afterList)) {
//			BigDecimal lastBal = entity.getBal();
//			if (BtBankAccConstant.STATUE_DEL.intValue() == entity.getStatus().intValue()) {
//				if(entity.getCdSign().equalsIgnoreCase(BisAccBalConstant.CDSIGN_IN)) {
//					lastBal = entity.getBal().subtract(entity.getAmt());
//				}else{
//					lastBal = entity.getBal().add(entity.getAmt());
//				}
//			}
//			List<BtBankAccDtl> newList = new ArrayList<>();
//			for (BtBankAccDtl dtl : afterList) {
//				boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
//				boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
//				if (outBlue || inRed) {
//					//支出(蓝) 收入(红)
//					dtl.setBal(lastBal.subtract(dtl.getAmt()));
//				} else {
//					//收入(蓝) 支出(红)
//					dtl.setBal(lastBal.add(dtl.getAmt()));
//				}
//				lastBal = dtl.getBal();
//				newList.add(dtl);
//			}
//			getSession().clear();
//			this.batchSaveOrUpdate(newList);
//		}
//	}
//
//	/**
//	 * 更新指定交易时间后的所有余额（刷新余额）
//	 *
//	 * @param entity
//	 * @param endDate 截止日期
//	 */
//	public void updateAfterBtBankAccDtlForFlush(BtBankAccDtl entity, Date endDate) {
//		List<BtBankAccDtl> afterList = this.btBankAccDtlDao.listAfterBtBankAccDtl(entity.getBankAcc(), entity.getCurId(), entity.getTransTime(), entity.getOrderNo(), endDate);
//		List<BtBankAccDtl> newList = new ArrayList<>();
//		if(EmptyUtils.isEmpty(entity.getBal()) || entity.getBal().compareTo(BigDecimal.ZERO) == 0 ){
//			BisAccBal bisAccBal = bisAccBalService.getInitBisAccBal(entity.getBankAcc(), entity.getCurId());
//			BigDecimal oldBal = BigDecimal.ZERO;
//			if(bisAccBal != null && !EmptyUtils.isEmpty(bisAccBal.getBalDate()) && bisAccBal.getBalDate().before(entity.getTransTime())){
//				oldBal = bisAccBal.getBal();
//			}
//			if(entity.getCdSign().equalsIgnoreCase(BisAccBalConstant.CDSIGN_IN)) {
//				entity.setBal(oldBal.add(entity.getAmt()));
//			}else{
//				entity.setBal(oldBal.subtract(entity.getAmt()));
//			}
//			newList.add(entity);
//		}
//		// 更新余额
//		if (!EmptyUtils.isEmptyList(afterList)) {
//			BigDecimal lastBal = entity.getBal();
//			if (BtBankAccConstant.STATUE_DEL.intValue() == entity.getStatus().intValue()) {
//				if(entity.getCdSign().equalsIgnoreCase(BisAccBalConstant.CDSIGN_IN)) {
//					lastBal = entity.getBal().add(entity.getAmt());
//				}else{
//					lastBal = entity.getBal().subtract(entity.getAmt());
//				}
//			}
//			for (BtBankAccDtl dtl : afterList) {
//				boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
//				boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
//				if (outBlue || inRed) {
//					//支出(蓝) 收入(红)
//					dtl.setBal(lastBal.subtract(dtl.getAmt()));
//				} else {
//					//收入(蓝) 支出(红)
//					dtl.setBal(lastBal.add(dtl.getAmt()));
//				}
//				//审批通过的数据，同步更新银行账户流水表
//				if(CommonConstant.STATUE_CHECK.toString().equals(dtl.getStatus().toString())){
//					bisAccDtlService.updateBal(dtl);
//				}
//				lastBal = dtl.getBal();
//				newList.add(dtl);
//			}
//			getSession().clear();
//			this.batchSaveOrUpdate(newList);
//		}
//	}
//
//	public void batchUpdateAfterBtBankAccDtl(List<BtBankAccDtl> list, ICurrentUser user) {
//		// 获取每个账户最小的流水数据
//		List<BtBankAccDtl> importList = new ArrayList<>();
//		Map<String, BtBankAccDtl> minMap = new HashMap<>();
//		for (BtBankAccDtl btBankAccDtl : list) {
//		    //通过账号和币别确定账号唯一
//			if (minMap.containsKey(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId())) {
//				BtBankAccDtl oldDtl = minMap.get(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId());
//				if (btBankAccDtl.getTransTime().compareTo(oldDtl.getTransTime()) == -1) {
//					importList.add(minMap.get(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId()));
//					minMap.put(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId(), btBankAccDtl);
//				} else if (btBankAccDtl.getTransTime().compareTo(oldDtl.getTransTime()) == 0
//						&& btBankAccDtl.getOrderNo() < oldDtl.getOrderNo()) {
//					importList.add(minMap.get(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId()));
//					minMap.put(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId(), btBankAccDtl);
//				} else {
//					importList.add(btBankAccDtl);
//				}
//			} else {
//				importList.add(btBankAccDtl);
//				minMap.put(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId(), btBankAccDtl);
//			}
//		}
//		// 循环计算余额
//		for (Map.Entry<String, BtBankAccDtl> entry : minMap.entrySet()) {
//			BtBankAccDtl minDtl = entry.getValue();
//			List<BtBankAccDtl> afterList = this.btBankAccDtlDao.listAfterBtBankAccDtl(minDtl.getBankAcc(), minDtl.getCurId(), minDtl.getTransTime(), minDtl.getOrderNo(), null);
//			Map<BigDecimal, BtBankAccDtl> newMap = new TreeMap<>(
//					new Comparator<BigDecimal>() {
//						@Override
//						public int compare(BigDecimal obj1, BigDecimal obj2) {
//							// 降序排序
//							return obj1.compareTo(obj2);
//						}
//					});
//			for (BtBankAccDtl dtl : importList) {
//				if (dtl.getBankAcc().equalsIgnoreCase(minDtl.getBankAcc()) && dtl.getCurId().equalsIgnoreCase(minDtl.getCurId())) {
//					newMap.put(new BigDecimal(dtl.getTransTime().getTime() + "" + dtl.getOrderNo()), dtl);
//				}
//			}
//			if (!EmptyUtils.isEmptyList(afterList)) {
//				for (BtBankAccDtl dtl : afterList) {
//					if (!newMap.containsKey(new BigDecimal(dtl.getTransTime().getTime() + "" + dtl.getOrderNo()))) {
//						newMap.put(new BigDecimal(dtl.getTransTime().getTime() + "" + dtl.getOrderNo()), dtl);
//					}
//				}
//			}
//			// 更新余额
//			if (!EmptyUtils.isEmpty(newMap) && newMap.size() > 0) {
//				BigDecimal bal = this.getDownBankBal(minDtl.getCdSign(), minDtl.getRbSign(), minDtl.getBankAcc(), minDtl.getAmt().toString(), minDtl.getTransTime(), minDtl.getCurId(), minDtl.getOrderNo());
//				minDtl.setBal(bal);
//				BigDecimal lastBal = minDtl.getBal();
//				//if (BtBankAccConstant.STATUE_DEL.intValue() == minDtl.getStatus().intValue()) {
//				if(minDtl.getCdSign().equalsIgnoreCase(BisAccBalConstant.CDSIGN_IN)) {
//					lastBal = minDtl.getBal().subtract(minDtl.getAmt());
//				}else{
//					lastBal = minDtl.getBal().add(minDtl.getAmt());
//				}
//				//}
//				for (Map.Entry<BigDecimal, BtBankAccDtl> entryDtl : newMap.entrySet()) {
//					BtBankAccDtl dtl = entryDtl.getValue();
//					if (BtBankAccConstant.STATUE_DEL.intValue() == dtl.getStatus().intValue()) {
//						continue;
//					}
//					boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
//					boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
//					if (outBlue || inRed) {
//						//支出(蓝) 收入(红)
//						dtl.setBal(lastBal.subtract(dtl.getAmt()));
//						lastBal = lastBal.subtract(dtl.getAmt());
//					} else {
//						//收入(蓝) 支出(红)
//						dtl.setBal(lastBal.add(dtl.getAmt()));
//						lastBal = lastBal.add(dtl.getAmt());
//					}
//					if (EmptyUtils.isEmpty(dtl.getId())) {
//						dtl.setBal(lastBal);
//						//生成日志
//						this.save(dtl);
//						btBankAccMessageService.saveMessage(dtl.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_ADD, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//					} else {
//						this.update(dtl);
//					}
//				}
//				this.saveOrUpdate(minDtl);
//			}
//		}
//	}
//
//
//	/**
//	 * 批量提交
//	 *
//	 * @param ids
//	 * @return
//	 */
//	public void batchSubmitBtBankAccDtl(String ids, ICurrentUser user) {
//		String[] dtlIds = ids.split(CommonConstant.COMMA_SPLIT);
//		for (String id : dtlIds) {
//			BtBankAccDtl btBankAccDtl = this.get(id);
//			if (!(BtBankAccConstant.STATUE_TEMP.intValue() == btBankAccDtl.getStatus().intValue()
//					|| BtBankAccConstant.STATUE_BACK.intValue() == btBankAccDtl.getStatus().intValue())) {
//				throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_SUBMIT_TEMP_OR_BACK);
//			}
//		}
//		for (String id : dtlIds) {
//			BtBankAccDtl entity = this.get(id);
//			entity.setStatus(BisAccBalConstant.STATUE_WAIT_CHECK);
//			// 挂接工作流
//			if (!BisAccBalConstant.STATUE_TEMP.equals(entity.getStatus())) {
//				 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_DTL, entity.getId(), entity, entity.getCorpId(), user.getId());
//				//生成日志
//				btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//			}
//		}
//	}
//
//	/**
//	 * 撤回
//	 *
//	 * @param workFlowResult
//	 */
//	@Override
//	public void flowRevoke(IWorkFlowResult workFlowResult) {
//		ICurrentUser user = workFlowResult.getUser();
//		BtBankAccDtl entity = this.get(workFlowResult.getBusinessKey(), true);
//		entity.setStatus(CommonConstant.STATUE_TEMP);
//		entity.setUpdateBy(user.getLoginName());
//		entity.setUpdateDate(DateUtils.getDate());
//		this.saveOrUpdate(entity);
//		//生成日志
//		btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
//	}
//
//	/**
//	 * 刷新余额
//	 * @param list 更新余额
//	 */
//	public void flushBal(List<BtBankAccDtlVO> list){
//		//直接刷新余额
//		Map<String,BtBankAccDtlVO> map = new HashMap();
//		for(BtBankAccDtlVO vo :list){
//			map.put(vo.getCurId()+vo.getAccName(),vo);
//		}
//
//		for(String key:map.keySet()){
//			BtBankAccDtlVO vo = map.get(key);
//			List<BtBankAccDtl> earliestDtl = this.btBankAccDtlDao.getEarliestDtl(vo.getBankAcc(), vo.getCurId());
//			if(earliestDtl.size()>0){
//				this.updateAfterBtBankAccDtlForFlush(earliestDtl.get(0), null);
//			}
//		}
//	}
//
//	/**
//	 * 刷新余额
//	 */
//	public void flushBal(){
//		List<BtBankAccDtl> list = getListForFlush();
//		for(BtBankAccDtl data:list){
//			List<BtBankAccDtl> earliestDtl = this.btBankAccDtlDao.getEarliestDtl(data.getBankAcc(), data.getCurId());
//			if(earliestDtl.size()>0){
//				this.updateAfterBtBankAccDtlForFlush(earliestDtl.get(0), null);
//			}
//		}
//	}
//
//	public List<BtBankAccDtl> getListForFlush() {
//		return this.btBankAccDtlDao.getListForFlush();
//	}
//
//	@Override
//	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
//		if(StringUtils.isNotEmpty(corpId)){
//			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
//			//单位有在办的银行账户流水信息，不允许禁用！
//			int num = this.btBankAccDtlDao.getBankAccDtlCountByCorpId(corpId);
//			if(num>0){
//				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
//						SysConfigConstant.Module.NIS,
//						SysConfigConstant.BusType.NIS_BANKACC_DTL,
//						multiLangService.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_BANKACC_DTL_ERROR)));
//			}
//			return verifyErrorDTOS;
//		}
//		return null;
//	}
//
//	/**
//	 * 刷新余额
//	 * @param bankAccDtlVO 更新余额
//	 */
//	public void flushBal(BtBankAccDtlVO bankAccDtlVO){
//		if(EmptyUtils.isNotEmpty(bankAccDtlVO.getBtBankAccList())) {
//			for (Map<String, Object> map : bankAccDtlVO.getBtBankAccList()) {
//				String curId = String.valueOf(map.get("accCurId"));
//				String bankAcc = String.valueOf(map.get("bankAcc"));
//				Date searchDate = null;
//				if(bankAccDtlVO.getBeginDate() != null){
//					searchDate = DateUtils.addDay(bankAccDtlVO.getBeginDate(), -1);
//				}
//				searchDate = DateUtils.parse(DateUtils.format(searchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE) + " 23:59:59", DateUtils.PATTERN_CLASSICAL);
//				List<BtBankAccDtl> earliestDtlList = this.btBankAccDtlDao.getEarliestDtl(bankAcc, curId, searchDate);
//				BtBankAccDtl earliestDtl = null;
//				if(EmptyUtils.isNotEmpty(earliestDtlList)){
//					earliestDtl = earliestDtlList.get(0);
//				} else {
//					earliestDtl = new BtBankAccDtl();
//					earliestDtl.setBankAcc(bankAcc);
//					earliestDtl.setCurId(curId);
//					earliestDtl.setBal(BigDecimal.ZERO);
//					earliestDtl.setAmt(BigDecimal.ZERO);
//					earliestDtl.setCdSign(BisAccBalConstant.CDSIGN_IN);
//					earliestDtl.setTransTime(searchDate);
//					earliestDtl.setOrderNo(0);
//					earliestDtl.setStatus(CommonConstant.STATUE_TEMP);
//				}
//				this.updateAfterBtBankAccDtlForFlush(earliestDtl, bankAccDtlVO.getEndDate());
//			}
//		}
//	}
//
//	@Override
//	public void saveBtBankAccDtl(com.bytter.biz.payment.api.vo.BtBankAccDtl entity, ICurrentUser user) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void updateBtBankAccDtl(com.bytter.biz.payment.api.vo.BtBankAccDtl entity, ICurrentUser user) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void saveBisAccDtl(com.bytter.biz.payment.api.vo.BtBankAccDtl entity) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean checkSave(com.bytter.biz.payment.api.vo.BtBankAccDtl entity) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public com.bytter.biz.payment.api.vo.BtBankAccDtlVO getByUnion(String id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void batchSubmitTempDtl(com.bytter.biz.payment.api.vo.BtBankAccDtlVO btBankAccDtlVO, ICurrentUser user) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void updateOrderNo(com.bytter.biz.payment.api.vo.BtBankAccDtlVO btBankAccDtlVO) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void saveImport(List<com.bytter.biz.payment.api.vo.BtBankAccDtlVO> list, ICurrentUser user) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void modifyMakeup(com.bytter.biz.payment.api.vo.BisAccDtl bisAccDtl) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void updateAfterBtBankAccDtl(com.bytter.biz.payment.api.vo.BtBankAccDtl entity) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void updateAfterBtBankAccDtlForFlush(com.bytter.biz.payment.api.vo.BtBankAccDtl entity, Date endDate) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void batchUpdateAfterBtBankAccDtl(List<com.bytter.biz.payment.api.vo.BtBankAccDtl> list, ICurrentUser user) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void flushBal(List<com.bytter.biz.payment.api.vo.BtBankAccDtlVO> list) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void flushBal(com.bytter.biz.payment.api.vo.BtBankAccDtlVO bankAccDtlVO) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public List<com.bytter.biz.payment.api.vo.BtBankAccDtl> getEarliestDtl(String bankAcc, String curId,Date searchDate) {
//		List<com.bytter.biz.inneraccount.entity.BtBankAccDtl> earliestDtlList = this.btBankAccDtlDao.getEarliestDtl(bankAcc, curId, searchDate);
//		List<com.bytter.biz.payment.api.vo.BtBankAccDtl> list = BeanUtils.copyListProperties(earliestDtlList, com.bytter.biz.payment.api.vo.BtBankAccDtl.class);
//		return list;
//	}
//
//	@Override
//	public com.bytter.biz.payment.api.dto.BtBankAccDTO getBankAccByBankAccAndCurId(String aAcc, String curId) {
//		com.bytter.system.api.dto.BtBankAccDTO bankAcc = this.iBtBankAccService.getBankAccByBankAccAndCurId(aAcc, curId);
//		com.bytter.biz.payment.api.dto.BtBankAccDTO dto = new com.bytter.biz.payment.api.dto.BtBankAccDTO();
//		BeanUtils.copyProperties(bankAcc, dto);
//		return dto;
//	}
//
//	
//}
