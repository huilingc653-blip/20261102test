package com.bytter.biz.inneraccount.service;

import cn.hutool.core.date.StopWatch;
import com.bytter.biz.bdlink.api.dto.BisAccBalAutoDTO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlExportDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalAutoService;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisAccDetailVO;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.inneraccount.dao.BtBankAccDtlDao;
import com.bytter.biz.inneraccount.entity.BtBankAccDtl;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlRpaVO;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlVO;
import com.bytter.biz.payment.api.dto.FcBankAccGroupDTO;
import com.bytter.biz.payment.api.service.IFcBankAccGroupService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.*;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.BankAccDtlMessageEnum;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行账号明细录入表
 * @date 2017-06-29 13:51:01
 */
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_DTL)
@DubboService(interfaceClass = IWorkFlowListener.class, group = NisWorkFlowConstant.BT_BANKACC_DTL)
@Service("btBankAccDtlService")
@Transactional
public class BtBankAccDtlService extends BaseService<BtBankAccDtl, String> implements IWorkFlowListener, IUserExcelVerifyHandler<BtBankAccDtlVO>, IWorkFlowRevokeListener, ICorpDisableVerifyService {
	@Autowired
	private BtBankAccDtlDao btBankAccDtlDao;
	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
	@DubboReference(check = false)
	private IBisBifInitService iBisBifInitService;
	@DubboReference(check = false,timeout = 120000)
	private IBisAccBalService bisAccBalService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Autowired
	private BtBankAccMessageService btBankAccMessageService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IBisAccBalAutoService bisAccBalAutoService;
	@Autowired(required = false)
	private IBtBankAccService btBankAccService;
	@Autowired(required=false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IBtCurrencyService currencyService;
	@Autowired(required = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IFcBankAccGroupService fcBankAccGroupService;

	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(BtBankAccDtl entity) {
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
	public void save(BtBankAccDtl entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(BtBankAccDtl entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BtBankAccDtl entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<BtBankAccDtl, String> getEntityDao() {
		return btBankAccDtlDao;
	}
	/**
	 * 保存银行账户流水录入信息
	 *
	 * @param entity
	 */
	public void saveBtBankAccDtl(BtBankAccDtl entity, ICurrentUser user) {
		if (this.checkSave(entity)) {
			// 设置单号
			String accNo = SequenceSingleton.getSequence(SequenceEnum.BT_BANK_ACC_DTL);
			entity.setAccNo(accNo);
			// 设置排序号
			if(EmptyUtils.isEmpty(entity.getOrderNo())) {
				Integer orderNo = this.btBankAccDtlDao.getOrderNo(entity.getTransTime(), entity.getBankAcc(), entity.getCurId());
				entity.setOrderNo(orderNo + 1);
			}
//			if(entity.getBal() == null) {
//				BigDecimal newBal = this.getDownBankBal(entity.getCdSign(), entity.getRbSign(), entity.getBankAcc(), entity.getAmt().toString(), entity.getTransTime(), entity.getCurId(), null);
//				entity.setBal(newBal);
//			}
			// 当前操作人信息
			Date curDate = DateUtils.getDate();
			String loginName = user.getLoginName();
			entity.setCreateBy(loginName);
			entity.setCreateDate(curDate);
			entity.setUpdateBy(loginName);
			entity.setUpdateDate(curDate);
			this.save(entity);
//			//银行流水号为空则更新为id
//			if(EmptyUtils.isEmpty(entity.getBankSerialNo())){
//				entity.setBankSerialNo(entity.getId());
//			}
			this.update(entity);
//			this.updateAfterBtBankAccDtl(entity);
			// 挂接工作流
			if (BisAccBalConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
				 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_DTL, entity.getId(), entity, entity.getCorpId(), user.getId());
				//生成日志
				btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
			} else {
				//生成日志
				btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_ADD, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
			}
		}
	}
	/**
	 * 保存银行账户流水录入信息
	 *
	 * @param entity
	 */
	public void updateBtBankAccDtl(BtBankAccDtl entity, ICurrentUser user) {
		if (this.checkSave(entity)) {
			entity.setUpdateBy(user.getLoginName());
			entity.setUpdateDate(DateUtils.getDate());
			if(entity.getBal() == null) {
				BigDecimal newBal = this.getDownBankBal(entity.getCdSign(), entity.getRbSign(), entity.getBankAcc(), entity.getAmt().toString(), entity.getTransTime(), entity.getCurId(), entity.getOrderNo());
				entity.setBal(newBal);
			}
			this.saveOrUpdate(entity);
			//this.updateAfterBtBankAccDtl(entity);
			Integer oldStatus = entity.getStatus();
			BisAccDtlVO bisAccDtl = bisAccDtlService.getBisAccDtlByAccNo(entity.getAccNo());
			if(bisAccDtl != null && (bisAccDtl.getStatus() == null ||
					 BisAccBalConstant.STATUE_CHECK.toString().equals(bisAccDtl.getStatus().toString()))){
				//获取对应的流水更新为删除（界面不可见）
				bisAccDtl.setStatus(BtBankAccConstant.STATUE_DEL);
				bisAccDtl.setUpdateBy(user.getLoginName());
				bisAccDtl.setUpdateDate(DateUtils.getDate());
				// this.getSession().merge(bisAccDtl);
				BisAccDtlDTO dtlDTO = BeanUtils.copyNewInstanceProperties(bisAccDtl, BisAccDtlDTO.class);
				bisAccDtlService.updateDtlByDto(dtlDTO);
				oldStatus = BisAccBalConstant.STATUE_CHECK;
			}
			// 挂接工作流
			if (!BisAccBalConstant.STATUE_TEMP.equals(entity.getStatus())) {
				 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_DTL, entity.getId(), entity, entity.getCorpId(), user.getId());
				//生成日志
				Integer messageType = BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT;
				if(BisAccBalConstant.STATUE_CHECK.toString().equals(oldStatus.toString())){
					messageType = BtBankAccMessageConstant.MESSAGE_TYPE_CHANGE_SUBMIT;
				}
				btBankAccMessageService.saveMessage(entity.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
			} else {
				//生成日志
				Integer messageType = BtBankAccMessageConstant.MESSAGE_TYPE_UPDATE;
				if(BisAccBalConstant.STATUE_CHECK.toString().equals(oldStatus.toString())){
					messageType = BtBankAccMessageConstant.MESSAGE_TYPE_CHANGE_UPDATE;
				}
				btBankAccMessageService.saveMessage(entity.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
			}
		}
	}

	public void saveBisAccDtl(List<BtBankAccDtlVO> list) {
		if(EmptyUtils.isNotEmpty(list)){
			for(BtBankAccDtlVO vo : list){
				BisAccDtlVO bisAccDtl = new BisAccDtlVO();
				BeanUtils.copyNotNullProperties(vo, bisAccDtl);
				bisAccDtl.setStatus(95);
				bisAccDtl.setId(null);
				//勾兑标志默认为0-未勾兑
				bisAccDtl.setBlendSign(CommonConstant.NO);
				bisAccDtl.setFbsStatus(CommonConstant.NO);
				this.bisAccDtlService.save(bisAccDtl);
			}
		}
	}
	/**
	 * 将审批通过后的流水录入信息保存到流水明细表中
	 *
	 * @param entity
	 */
	public void saveBisAccDtl(BtBankAccDtl entity,ICurrentUser user) {
		BisAccDtlVO bisAccDtl = bisAccDtlService.getBisAccDtlByAccNo(entity.getAccNo());
		if (bisAccDtl == null || bisAccDtl.getId() == null) {
			bisAccDtl = new BisAccDtlVO();
			BeanUtils.copyNotNullProperties(entity, bisAccDtl);
			bisAccDtl.setStatus(null);
			bisAccDtl.setId(null);
			//勾兑标志默认为0-未勾兑
			bisAccDtl.setBlendSign(CommonConstant.NO);
			bisAccDtl.setFbsStatus(CommonConstant.NO);
			this.bisAccDtlService.save(bisAccDtl);
		} else {
//			BeanUtils.copyNotNullProperties(entity, bisAccDtl);
			bisAccDtl.setOppAccNo(entity.getOppAccNo());
			bisAccDtl.setOppAccName(entity.getOppAccName());
			bisAccDtl.setOppAccBank(entity.getOppAccBank());
			bisAccDtl.setCdSign(entity.getCdSign());
			bisAccDtl.setAmt(entity.getAmt());
			bisAccDtl.setBal(entity.getBal());
			bisAccDtl.setTransTime(entity.getTransTime());
			bisAccDtl.setPostscript(entity.getPostscript());
			bisAccDtl.setProjectRemark(entity.getProjectRemark());
			bisAccDtl.setBankSerialNo(entity.getBankSerialNo());
			bisAccDtl.setRmk(entity.getRemark());
			bisAccDtl.setStatus(BtBankAccConstant.STATUE_CHECK);
			// this.getSession().merge(bisAccDtl);
			bisAccDtlService.saveOrUpdate(bisAccDtl);
		}
//		this.bisAccDtlService.updateAfterBtBankAccDtl(bisAccDtl, entity.getOrderNo());
		//更新自动更新离线账户历史余额记录表
		BtBankAccDTO bankAcc = iBtBankAccService.getBankAccInfo(bisAccDtl.getBankAcc());
		Date beginDate = bisAccDtl.getTransTime();
		Date endDate = bisAccDtl.getTransTime();
		com.bytter.biz.bdlink.api.dto.BtBankAccDTO bankAccDTO = new com.bytter.biz.bdlink.api.dto.BtBankAccDTO();
		BeanUtils.copyNotNullProperties(bankAcc,bankAccDTO);
		bankAccDTO.setCurId(bisAccDtl.getCurId());
		bisAccBalService.saveBisAccBalByDtl(bankAccDTO,beginDate,endDate,user);
		//先根据账号id和币别id取余额自动计算信息表信息
		BisAccBalAutoDTO auto = bisAccBalAutoService.getBisAccBalAutoByAccCurId(bankAcc.getId(), bisAccDtl.getCurId());
		if (auto != null && !EmptyUtils.isEmpty(auto.getEndDate()) && auto.getEndDate().after(bisAccDtl.getTransTime())) {
			auto.setEndDate(DateUtils.parseDate(DateUtils.format(bisAccDtl.getTransTime(),DateUtils.PATTERN_CLASSICAL_SIMPLE)));
			bisAccBalAutoService.updateEntity(auto);
		}
	}

	/**
	 * 将审批通过后的流水录入信息保存到流水明细表中
	 *
	 * @param entity
	 */
	public void saveBisAccDtlForBatch(BtBankAccDtl entity,ICurrentUser user) {
		BisAccDtlVO bisAccDtl = null;
		bisAccDtl = new BisAccDtlVO();
		BeanUtils.copyNotNullProperties(entity, bisAccDtl);
		bisAccDtl.setStatus(null);
		bisAccDtl.setId(null);
		//勾兑标志默认为0-未勾兑
		bisAccDtl.setBlendSign(CommonConstant.NO);
		bisAccDtl.setFbsStatus(CommonConstant.NO);
		this.bisAccDtlService.save(bisAccDtl);
	}
	/**
	 * 银行账户流水管理
	 *
	 * @param dataTablesInput
	 * @param user
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		List<String> corpIdS = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_MANAGERQUERY, user.getId());
		//查询账号
		List<BtBankAccDTO> bankAccDTOList =this.iBtBankAccService.getBankAccNotUnionByCorpIdList(corpIdS,"", new HashMap<>());
		StringBuilder sql = new StringBuilder("");
		if(!CollectionUtils.isEmpty(bankAccDTOList)) {
			sql.append(" and t.bank_acc in (");
			for(BtBankAccDTO acc:bankAccDTOList){
				sql.append("'"+acc.getBankAcc()+"',");
			}
			sql.deleteCharAt(sql.length() - 1); // 删除最后一个逗号
			sql.append(")");
		}
		String corpAuthSql = SqlUtil.getInSql(corpIdS, "t.corp_id", true);
		sql.append(" and ").append(corpAuthSql);
		sql.append(" and t.create_by ='").append(user.getLoginName()).append("'");
		DataTablesOutput dataTablesOutput = this.btBankAccDtlDao.getDataTablesUnion(dataTablesInput, sql.toString());
		//填充属性值
		this.setAttributes(dataTablesOutput);
		return dataTablesOutput;
	}
	public boolean checkSave(BtBankAccDtl entity) {
		if (entity.getBal().compareTo(BigDecimal.ZERO) < -1) {
			throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_AMT_NOTZERO);
		}
		BtBankAccDTO btBankAccDTO = iBtBankAccService.getBankAccInfo(entity.getBankAcc());
		if (EmptyUtils.isEmpty(btBankAccDTO) || EmptyUtils.isEmpty(btBankAccDTO.getRegDate())) {
			throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_BANKACC_REGDATE_NULL);
		}
		if (!btBankAccDTO.getRegDate().before(entity.getTransTime())) {
			throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_TRANSTIME_LT_ACCREDDATE_DTL, entity.getBankAcc(),DateUtils.formatDate(entity.getTransTime()));
		}
		//流水交易日期大于系统当前日期
		if (DateUtils.getDate().before(entity.getTransTime())) {
			throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_TRANSTIME_LT_SYSDATE);
		}
		Date endDtlDate = this.iBtBankAccService.getLastEndDtlDate(btBankAccDTO.getId(), entity.getCurId());
		if (!EmptyUtils.isEmpty(endDtlDate)) {
			if (!endDtlDate.before(entity.getTransTime())) {
				throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_BALDATE_LT_COMPLETEDATE, entity.getBankAcc(), DateUtils.format(endDtlDate));
			}
		}
		return true;
	}
	/**
	 * 根据id查找银行账户流水信息
	 * zuoy 2017-07-03
	 *
	 * @param id
	 * @return BtBankAccDtlVO
	 */
	public BtBankAccDtlVO getByUnion(String id) {
		BtBankAccDtlVO vo = this.btBankAccDtlDao.getByUnion(id);
		BisBifInitDTO bisBifInitDTO =iBisBifInitService.getObjectById(vo.getBifCode());
		vo.setBifName(bisBifInitDTO.getName());
		BtCurrencyDTO currencyDTO=btCurrencyService.getObjectById(vo.getCurId());
		vo.setCurName(currencyDTO.getCurName());
		return vo;
	}
	/**
	 * 获取交易日期
	 *
	 * @param bankAcc   账户
	 * @param curId     币别ID
	 * @param transTime 比较日期，为null则查询最后一次
	 * @param operator  跟transTime组合使用，日期比较运算符，参照MatchTypeEnum
	 * @return
	 */
	public Date getLastTransTime(String bankAcc, String curId, Date transTime, String operator) {
		return this.btBankAccDtlDao.getLastTransTime(bankAcc, curId, transTime, operator);
	}
	/**
	 * 获取最近一次的余额
	 *
	 * @param bankAcc 账户
	 * @param curId   币别ID
	 * @return
	 */
	public BigDecimal getLastAmtByTransTime(String bankAcc, String curId) {
		return this.getAmtByTransTime(bankAcc, curId, null, null);
	}
	/**
	 * 获取指定日期的余额
	 *
	 * @param bankAcc   账户
	 * @param curId     币别ID
	 * @param transTime 日期
	 * @return
	 */
	public BigDecimal getAmtByTransTime(String bankAcc, String curId, Date transTime) {
		if (EmptyUtils.isEmpty(transTime)) {
			return null;
		}
		return this.getAmtByTransTime(bankAcc, curId, transTime, MatchTypeEnum.EQ.getValue());
	}
	/**
	 * 获取指定日期的余额，若指定日期不存在余额，则查询最近一次
	 *
	 * @param bankAcc   账户
	 * @param curId     币别ID
	 * @param transTime 日期
	 * @return
	 */
	public BigDecimal getCurAmtOrLastAmt(String bankAcc, String curId, Date transTime) {
		BigDecimal curAmt = this.getAmtByTransTime(bankAcc, curId, transTime);
		if (!EmptyUtils.isEmpty(curAmt)) {
			return curAmt;
		}
		return this.getLastAmtByTransTime(bankAcc, curId);
	}
	/**
	 * 获取指定日期的余额
	 *
	 * @param bankAcc   账户
	 * @param curId     币别ID
	 * @param transTime 比较日期,为null则查询最后一次
	 * @param operator  跟transTime组合使用，日期比较运算符，参照MatchTypeEnum
	 * @return
	 */
	public BigDecimal getAmtByTransTime(String bankAcc, String curId, Date transTime, String operator) {
		if (EmptyUtils.isNotEmpty(operator) && EmptyUtils.isEmpty(transTime)) {
			return null;
		}
		return this.btBankAccDtlDao.getAmtByTransTime(bankAcc, curId, transTime, operator);
	}
	/**
	 * 计算余额
	 *
	 * @param cdSign    借贷标志 0-收入 1-支出
	 * @param rbSign    红蓝标志 1-红 2-蓝
	 * @param bankAcc   银行账户
	 * @param amt       交易金额
	 * @param transTime 交易时间
	 * @param curId     币别ID
	 * @return
	 */
	public BigDecimal getDownBankBal(String cdSign, String rbSign, String bankAcc, String amt, Date transTime, String curId, Integer orderNo) {
		// 当前输入交易金额
		BigDecimal curAmt = EmptyUtils.isNumeric(amt) ? new BigDecimal(amt) : BigDecimal.ZERO;
		if (EmptyUtils.isEmpty(orderNo)) {
			orderNo = this.btBankAccDtlDao.getOrderNo(transTime, bankAcc, curId);
			orderNo = orderNo + 1;
		}
		// 最近一笔流水余额
		BigDecimal lastBal = this.btBankAccDtlDao.lastByTransTime(bankAcc, curId, transTime, orderNo);
		if (EmptyUtils.isEmpty(lastBal)) {
			//第一笔流水则取初始化余额
			com.bytter.biz.bdlink.api.dto.BisAccBalDTO initBisAccBal = bisAccBalService.getInitBisAccBal(bankAcc, curId);
			if(initBisAccBal !=null && initBisAccBal.getId()!=null && DateUtils.dayDiffs(transTime,initBisAccBal.getBalDate())>=0){
				lastBal = initBisAccBal.getBal();
			}else{
				lastBal = BigDecimal.ZERO;
			}
		}
		boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(cdSign) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(rbSign);
		boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(cdSign) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(rbSign);
		if (outBlue || inRed) {
			//支出(蓝) 收入(红)
			return lastBal.subtract(curAmt);
		} else {    //收入(蓝) 支出(红)
			return lastBal.add(curAmt);
		}
	}
	/**
	 * 查询某账户本月未提交的流水记录
	 *
	 * @param bankAcc 账户
	 * @param curId   币别ID
	 * @param month   月份
	 * @return
	 */
	public List<BtBankAccDtlVO> getTempDtlByMonth(String bankAcc, String curId, Integer month) {
		return this.btBankAccDtlDao.getTempDtlByMonth(bankAcc, curId, month);
	}
	/**
	 * 批量提交暂存的流水记录
	 *
	 * @param btBankAccDtlVO
	 * @return
	 */
	public void batchSubmitTempDtl(BtBankAccDtlVO btBankAccDtlVO, ICurrentUser user) {
		List<Map<String, Object>> tempBtBankAccDtlList = btBankAccDtlVO.getTempBtBankAccDtlList();
		if (!ListUtils.isNullOrEmpty(tempBtBankAccDtlList)) {
			Set<String> btBankAccDtlIds=new HashSet<>();
			for (Map<String, Object> map : tempBtBankAccDtlList) {
				String tempDtlId = String.valueOf(map.get("tempDtlId"));
				if (!EmptyUtils.isBlank(tempDtlId)) {
					btBankAccDtlIds.add(tempDtlId);
				}
			}
			for (String id : btBankAccDtlIds) {
				BtBankAccDtl entity = this.get(id);
				entity.setStatus(BisAccBalConstant.STATUE_WAIT_CHECK);
				// todo:挂接工作流
				if (!BisAccBalConstant.STATUE_TEMP.equals(entity.getStatus())) {
					 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_DTL, entity.getId(), entity, entity.getCorpId(), user.getId());
					//生成日志
					btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
				}
			}
		}
	}
	/**
	 * 调整排序号
	 *
	 * @param btBankAccDtlVO
	 */
	public void updateOrderNo(BtBankAccDtlVO btBankAccDtlVO) {
		List<Map<String, Object>> tempBtBankAccDtlList = btBankAccDtlVO.getTempBtBankAccDtlList();
		if (!EmptyUtils.isEmptyList(tempBtBankAccDtlList)) {
			List<BtBankAccDtl> afterList = new ArrayList<>();
			BtBankAccDtl tempDtl = null;
			for (Map<String, Object> map : tempBtBankAccDtlList) {
				String tempDtlId = String.valueOf(map.get("tempDtlId"));
				String orderNo = String.valueOf(map.get("tempOrderNo"));
				if (!EmptyUtils.isBlank(tempDtlId)) {
					BtBankAccDtl entity = this.get(tempDtlId);
					if (EmptyUtils.isBlank(orderNo)) {
						throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_ORDERNO_NULL);
					}
					if (!EmptyUtils.isNumeric(orderNo)) {
						throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_ORDERNO_NUMBER);
					}
					entity.setOrderNo(Integer.parseInt(orderNo));
					afterList.add(entity);
					if (EmptyUtils.isEmpty(tempDtl)) {
						tempDtl = entity;
					} else {
						if (tempDtl.getOrderNo() > entity.getOrderNo()) {
							tempDtl = entity;
						}
					}
				}
			}
			Collections.sort(afterList, new Comparator<BtBankAccDtl>() {
				@Override
				public int compare(BtBankAccDtl o1, BtBankAccDtl o2) {
					// 按照学生的年龄进行降序排列
					if (o2.getOrderNo() > o1.getOrderNo()) {
						return -1;
					}
					return 1;
				}
			});
			// 更新余额
			if (!EmptyUtils.isEmptyList(afterList)) {
				BigDecimal lastBal = this.btBankAccDtlDao.lastByTransTime(tempDtl.getBankAcc(), tempDtl.getCurId(), tempDtl.getTransTime(), tempDtl.getOrderNo());
				if (EmptyUtils.isEmpty(lastBal)) {
					lastBal = this.bisAccBalService.getLastByBalDate(tempDtl.getBankAcc(), tempDtl.getCurId(), true);
				}
				if (EmptyUtils.isEmpty(lastBal)) {
					lastBal = BigDecimal.ZERO;
				}
				List<BtBankAccDtl> newList = new ArrayList<>();
				for (BtBankAccDtl dtl : afterList) {
					boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
					boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
					if (outBlue || inRed) {
						//支出(蓝) 收入(红)
						dtl.setBal(lastBal.subtract(dtl.getAmt()));
					} else {
						//收入(蓝) 支出(红)
						dtl.setBal(lastBal.add(dtl.getAmt()));
					}
					lastBal = dtl.getBal();
					newList.add(dtl);
				}
			}
		}
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		BtBankAccDtl entity = this.get(workFlowResult.getBusinessKey());
		Integer status = workFlowResult.isPass() ? BisAccBalConstant.STATUE_CHECK : BisAccBalConstant.STATUE_BACK;
		entity.setStatus(status);
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
		// 审批结束保存到流水记录表
		if (BisAccBalConstant.STATUE_CHECK.intValue() == status.intValue()) {
			saveBisAccDtl(entity,user);
		}
		Integer messageType = workFlowResult.isPass() ? BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_PASS : BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		//生成日志
		btBankAccMessageService.saveMessage(entity.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
	}
	/**
	 * 获取银行账户流水审批列表信息
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = this.workFlowService.getWorkFlowBusiness(NisWorkFlowConstant.BT_BANKACC_DTL, dataTablesInput);
		String idsSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "t.id", true);
		DataTablesOutput dp = this.btBankAccDtlDao.getDataTablesApprove(dataTablesInput, idsSql);
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 导入功能
	 *
	 * @param list
	 */
	public void saveImport(List<BtBankAccDtlVO> list, ICurrentUser user) {
		String loginName = user.getLoginName();
		Date curDate = DateUtils.getDate();
		Map<String, Integer> orderMap = new HashMap<>();
		List<BtBankAccDtl> newList = new ArrayList<>();
		// 设置值
		for (BtBankAccDtlVO vo : list) {
			BtBankAccDtl btBankAccDtl = new BtBankAccDtl();
			BeanUtils.copyNotNullProperties(vo, btBankAccDtl);
			// 设置单号
			String accNo = SequenceSingleton.getSequence(SequenceEnum.BT_BANK_ACC_DTL);
			btBankAccDtl.setAccNo(accNo);
			if(vo.getOrderNo() != null){
				orderMap.put(btBankAccDtl.getBankAcc() + DateUtils.format(btBankAccDtl.getTransTime()) + btBankAccDtl.getCurId(), vo.getOrderNo());
			} else {
				// 设置排序号
				Integer orderNo = this.btBankAccDtlDao.getOrderNo(btBankAccDtl.getTransTime(), btBankAccDtl.getBankAcc(), btBankAccDtl.getCurId());
				if (orderMap.containsKey(btBankAccDtl.getBankAcc() + DateUtils.format(btBankAccDtl.getTransTime()) + btBankAccDtl.getCurId())) {
					btBankAccDtl.setOrderNo(orderMap.get(btBankAccDtl.getBankAcc() + DateUtils.format(btBankAccDtl.getTransTime()) + btBankAccDtl.getCurId()) + 1);
					orderMap.put(btBankAccDtl.getBankAcc() + DateUtils.format(btBankAccDtl.getTransTime()) + btBankAccDtl.getCurId(), btBankAccDtl.getOrderNo());
				} else {
					btBankAccDtl.setOrderNo(orderNo + 1);
					orderMap.put(btBankAccDtl.getBankAcc() + DateUtils.format(btBankAccDtl.getTransTime()) + btBankAccDtl.getCurId(), btBankAccDtl.getOrderNo());
				}
			}
			btBankAccDtl.setRbSign(BisAccBalConstant.RBSIGN_BLUE);
			if(EmptyUtils.isEmpty(btBankAccDtl.getBal())) {
				btBankAccDtl.setBal(BigDecimal.ZERO);
			}
			if("支出".equals(btBankAccDtl.getCdSign())){
				btBankAccDtl.setCdSign(BisAccBalConstant.CDSIGN_OUT);
			}else if("收入".equals(btBankAccDtl.getCdSign())) {
				btBankAccDtl.setCdSign(BisAccBalConstant.CDSIGN_IN);
			}
			btBankAccDtl.setStatus(NetbankConstant.STATUE_TEMP);
			btBankAccDtl.setCreateBy(loginName);
			btBankAccDtl.setCreateDate(curDate);
			newList.add(btBankAccDtl);
		}
		//导入先不计算余额，直接入库
		this.batchSave(newList);
		for(BtBankAccDtl entity : newList){
			//银行流水号为空则更新为id
			if(EmptyUtils.isEmpty(entity.getBankSerialNo())){
				entity.setBankSerialNo(entity.getId());
			}
		}
		this.batchUpdate(newList);
		this.getSession().flush();
		this.batchSubmitBtBankAccDtl(newList,user);
		//batchUpdateAfterBtBankAccDtl(newList, user);
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(BtBankAccDtlVO vo,Object... user) {
		return null;
	}
	@Override
	public void verifyHandler(List<BtBankAccDtlVO> list,Object... user) {
		if (!EmptyUtils.isEmptyList(list)) {
			//银行账号集合
			HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
			//币种集合
			HashMap<String, String> curMap = new HashMap<>();
			//用户
			ICurrentUser userInfo=(ICurrentUser)user[0];
			BtBankAccDTO bankAcc = null;
			Map<String,BtBankAccDtlVO> map = new HashMap<>();
			for(BtBankAccDtlVO vo:list){
				List<String> msgList = new ArrayList<>();
				// 币别不为空
				if (EmptyUtils.isBlank(vo.getCurName())) {
					msgList.add("nis.netbank.message.curname.null");
				} else {
					// 判断币别是否在系统中存在
					String curId = curMap.get(vo.getCurName());
					if (StringUtils.isBlank(curId)) {
						curId = btCurrencyService.getCurrencyByName(vo.getCurName());
						curMap.put(vo.getCurName(), curId);
					}
					if (EmptyUtils.isBlank(curId)) {
						msgList.add("nis.netbank.message.cur.null");
					} else {
						vo.setCurId(curId);
					}
				}
				// 银行账号不为空
				if (EmptyUtils.isBlank(vo.getBankAcc())) {
					msgList.add("nis.netbank.message.dtl.bankacc.null");
				} else {
					//币别不为空
					if(StringUtils.isNotBlank(vo.getCurId())) {
						if (bankAccMap.containsKey(vo.getBankAcc()+CommonConstant.COMMA_SPLIT+vo.getCurId())) {
							bankAcc = bankAccMap.get(vo.getBankAcc()+CommonConstant.COMMA_SPLIT+vo.getCurId());
							vo.setCorpId(bankAcc.getCorpId());
							vo.setBifCode(bankAcc.getBifCode());
							vo.setBankName(bankAcc.getBankName());
							vo.setAccName(bankAcc.getAccName());
						} else {
							// 判断银行账号是否在系统中存在
							bankAcc = this.iBtBankAccService.getBankAccByBankAccAndCurId(vo.getBankAcc(), vo.getCurId());
							if (EmptyUtils.isEmpty(bankAcc)) {
								msgList.add("nis.netbank.message.dtl.bankaccinfo.null");
							} else if (BtBankAccConstant.ISONLINE_1.equalsIgnoreCase(bankAcc.getIsOnline())) {
								// 只能导入离线账号
								msgList.add("nis.netbank.message.dtl.online.no");
							} else {
								Boolean flag = accountGrantService.checkPermissionByUserAndAccount(userInfo.getId(), vo.getBankAcc(), AccountGrantConstant.OFFLINE_BANK_TYPE,
										AccountGrantConstant.QUERY_DETAIL, ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY);
								//为空说明无权限
								if (flag) {
									msgList.add("nis.netbank.message.bankacc.detail");
								} else {
									vo.setCorpId(bankAcc.getCorpId());
									vo.setBifCode(bankAcc.getBifCode());
									vo.setBankName(bankAcc.getBankName());
									vo.setAccName(bankAcc.getAccName());
									bankAccMap.put(vo.getBankAcc()+CommonConstant.COMMA_SPLIT+vo.getCurId(), bankAcc);
								}
							}
						}
					}
				}
				// 收支标志
				if (EmptyUtils.isEmpty(vo.getCdSign())) {
					msgList.add("nis.netbank.message.cdsign.null");
				} else if (!BisAccBalConstant.CDSIGN_OUT.equals(vo.getCdSign())
						&& !BisAccBalConstant.CDSIGN_IN.equals(vo.getCdSign())) {
					msgList.add("nis.netbank.message.cdsign.format");
				}else{
					if(BisAccBalConstant.CDSIGN_OUT.equals(vo.getCdSign())){
						vo.setCdSign("支出");
					}else{
						vo.setCdSign("收入");
					}
				}
				// 交易时间不为空
				if (EmptyUtils.isEmpty(vo.getTransTime())) {
					msgList.add("nis.netbank.message.transtime.null");
				} else {
					Calendar c = Calendar.getInstance();
					c.setTime(vo.getTransTime());
					vo.setMonths(c.get(Calendar.MONTH) + 1);
					//校验交易日期不能大于系统当前日期
					if(vo.getTransTime().after(DateUtils.getDate())){
						msgList.add("bt.bankaccdtl.message.transtime.lt.sysdate");
					}
				}
				//明细截止时间校验
				if (bankAcc != null && StringUtils.isNotBlank(bankAcc.getId()) && vo.getTransTime() != null) {
					Date endDtlDate = this.iBtBankAccService.getLastEndDtlDate(bankAcc.getId(), vo.getCurId());
					if (!EmptyUtils.isEmpty(endDtlDate)) {
						if (!endDtlDate.before(vo.getTransTime())) {
							msgList.add("bt.bankaccdtl.import.message.baldate.lt.completedate");
						}
					}
				}
				// 金额不能为空
				if (EmptyUtils.isEmpty(vo.getAmt())) {
					msgList.add("nis.netbank.message.amt.null");
				}
				//判断导入集合是否重复
				if(vo.getTransTime() != null){
					String key = vo.getBankAcc()+vo.getOppAccNo()+vo.getCdSign()+DateUtils.formatDate(vo.getTransTime(),"yyyy-MM-dd HH:mm:ss")
							+vo.getRemark()+vo.getAmt()+vo.getBal();
					if(map.containsKey(key)){
						msgList.add("nis.netbank.message.isRepeat.null");
					}else{
						map.put(key,vo);
					}
				}


				//本方账号、对方账号、借贷标志、交易时间、金额、摘要 不能重复 增加余额
				boolean isRepeat = btBankAccDtlDao.isRepeat(vo.getBankAcc(),vo.getOppAccNo(),vo.getCdSign(), vo.getTransTime(), vo.getAmt(), vo.getRemark(),vo.getBal());
				if(isRepeat){
					msgList.add("nis.netbank.message.isRepeat.null");
				}
				vo.addErrorMsg(msgList);
			}
		}
	}
	/**
	 * 获取调整列表的数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesAdjust(DataTablesInput dataTablesInput) {
		return this.btBankAccDtlDao.getDataTablesAdjust(dataTablesInput);
	}
	/**
	 * 删除
	 *
	 * @param id
	 */
	public void deleteBtBankAccDtl(String id, ICurrentUser user) {
		BtBankAccDtl btBankAccDtl = this.get(id);
		btBankAccDtl.setStatus(BtBankAccConstant.STATUE_DEL);
		btBankAccDtl.setUpdateBy(user.getLoginName());
		btBankAccDtl.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(btBankAccDtl);
		this.updateAfterBtBankAccDtl(btBankAccDtl);
		Integer messageType = BtBankAccMessageConstant.MESSAGE_TYPE_DELETE;
		//同步更新银行流水表数据的状态
		BisAccDtlVO bisAccDtl = bisAccDtlService.getBisAccDtlByAccNo(btBankAccDtl.getAccNo());
		if(bisAccDtl != null){
			bisAccDtl.setStatus(BtBankAccConstant.STATUE_DEL);
			bisAccDtl.setUpdateBy(user.getLoginName());
			bisAccDtl.setUpdateDate(DateUtils.getDate());
			bisAccDtlService.saveOrUpdate(bisAccDtl);
			messageType = BtBankAccMessageConstant.MESSAGE_TYPE_DEFEAT;
		}
		//更新自动更新离线账户历史余额记录表
		BtBankAccDTO bankAcc = iBtBankAccService.getBankAccInfo(btBankAccDtl.getBankAcc());
		Date beginDate = btBankAccDtl.getTransTime();
		Date endDate = DateUtils.addDay(DateUtils.getDateYMD(),1);
		com.bytter.biz.bdlink.api.dto.BtBankAccDTO bankAccDTO = new com.bytter.biz.bdlink.api.dto.BtBankAccDTO();
		BeanUtils.copyNotNullProperties(bankAcc,bankAccDTO);
		bankAccDTO.setCurId(btBankAccDtl.getCurId());
		bisAccBalService.saveBisAccBalByDtl(bankAccDTO,beginDate,endDate,user);
		//生成日志
		btBankAccMessageService.saveMessage(btBankAccDtl.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
		//完成通知提醒
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_DTL, btBankAccDtl.getId(), user.getId());
		this.updateAfterBtBankAccDtlForFlush(btBankAccDtl,null);
	}
	/**
	 * 批量删除
	 *
	 * @param ids
	 */
	public void batchDeleteBtBankAccDtl(String ids, String opType, ICurrentUser user) {
		String[] dtlIds = ids.split(CommonConstant.COMMA_SPLIT);
		for (String id : dtlIds) {
			BtBankAccDtl btBankAccDtl = this.get(id);
			if(CommonConstant.YES.equals(opType)) {
				//作废
				if (BtBankAccConstant.STATUE_CHECK.intValue() != btBankAccDtl.getStatus().intValue()) {
					throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_DELETE_CHECK);
				}
			} else {
				if (!(BtBankAccConstant.STATUE_TEMP.intValue() == btBankAccDtl.getStatus().intValue()
						|| BtBankAccConstant.STATUE_BACK.intValue() == btBankAccDtl.getStatus().intValue())) {
					throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_DELETE_TEMP_OR_BACK);
				}
			}
		}
		List<BtBankAccDtl> newList = new ArrayList<>();
		Integer messageType = BtBankAccMessageConstant.MESSAGE_TYPE_DELETE;
		for (String id : dtlIds) {
			BtBankAccDtl btBankAccDtl = this.get(id);
			btBankAccDtl.setUpdateBy(user.getLoginName());
			btBankAccDtl.setUpdateDate(DateUtils.getDate());
			btBankAccDtl.setStatus(BtBankAccConstant.STATUE_DEL);
			newList.add(btBankAccDtl);
			//同步更新银行流水表数据的状态
			BisAccDtlVO bisAccDtl = bisAccDtlService.getBisAccDtlByAccNo(btBankAccDtl.getAccNo());
			if(bisAccDtl != null){
				bisAccDtl.setStatus(BtBankAccConstant.STATUE_DEL);
				bisAccDtl.setUpdateBy(user.getLoginName());
				bisAccDtl.setUpdateDate(DateUtils.getDate());
				bisAccDtlService.saveOrUpdate(bisAccDtl);
				messageType = BtBankAccMessageConstant.MESSAGE_TYPE_DEFEAT;
			}
			if(!EmptyUtils.isEmpty(bisAccDtl)) {
				//更新自动更新离线账户历史余额记录表
				BtBankAccDTO bankAcc = iBtBankAccService.getBankAccInfo(bisAccDtl.getBankAcc());
				if (!EmptyUtils.isEmpty(bankAcc)) {
					Date beginDate = bisAccDtl.getTransTime();
					Date endDate = DateUtils.addDay(DateUtils.getDateYMD(),1);
					com.bytter.biz.bdlink.api.dto.BtBankAccDTO bankAccDTO = new com.bytter.biz.bdlink.api.dto.BtBankAccDTO();
					BeanUtils.copyNotNullProperties(bankAcc, bankAccDTO);
					bankAccDTO.setCurId(bisAccDtl.getCurId());
					bisAccBalService.saveBisAccBalByDtl(bankAccDTO, beginDate, endDate, user);
				}
			}
			//生成日志
			btBankAccMessageService.saveMessage(btBankAccDtl.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
			//完成通知提醒
			notificationService.complete(NotificationConstant.NETBANK.BANKACC_DTL, btBankAccDtl.getId(), user.getId());
		}
		batchUpdateAfterBtBankAccDtl(newList, user);
	}
	/**
	 * 银行账号明细补录
	 *
	 * @param bisAccDtl
	 * @return
	 */
//	public void modifyMakeup(BisAccDtl bisAccDtl) {
	public void modifyMakeup(BisAccDtlDTO bisAccDtl) {
		List<BtBankAccDtl> list = this.btBankAccDtlDao.listBy("accNo", bisAccDtl.getAccNo());
		for (BtBankAccDtl btBankAccDtl:list) {
			btBankAccDtl.setOppAccNo(bisAccDtl.getOppAccNo());
			btBankAccDtl.setOppAccName(bisAccDtl.getOppAccName());
			btBankAccDtl.setOppAccBank(bisAccDtl.getOppAccBank());
			btBankAccDtl.setUses(bisAccDtl.getUses());
			btBankAccDtl.setRemark(bisAccDtl.getRemark());
			btBankAccDtl.setBankSerialNo(bisAccDtl.getBankSerialNo());
			btBankAccDtl.setProjectRemark(bisAccDtl.getProjectRemark());
			this.btBankAccDtlDao.update(btBankAccDtl);
		}
	}
	/**
	 * 更新指定交易时间后的所有余额
	 *
	 * @param entity
	 */
	public void updateAfterBtBankAccDtl(BtBankAccDtl entity) {
		getSession().flush();
		getSession().clear();
		List<BtBankAccDtl> afterList = this.btBankAccDtlDao.listAfterBtBankAccDtl(entity.getBankAcc(), entity.getCurId(), entity.getTransTime(), entity.getOrderNo(), null);
		// 更新余额
		if (!EmptyUtils.isEmptyList(afterList)) {
			BigDecimal lastBal = entity.getBal();
			if (BtBankAccConstant.STATUE_DEL.intValue() == entity.getStatus().intValue()) {
				if(entity.getCdSign().equalsIgnoreCase(BisAccBalConstant.CDSIGN_IN)) {
					lastBal = entity.getBal().subtract(entity.getAmt());
				}else{
					lastBal = entity.getBal().add(entity.getAmt());
				}
			}
			List<BtBankAccDtl> newList = new ArrayList<>();
			for (BtBankAccDtl dtl : afterList) {
				boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
				boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
				if (outBlue || inRed) {
					//支出(蓝) 收入(红)
					dtl.setBal(lastBal.subtract(dtl.getAmt()));
				} else {
					//收入(蓝) 支出(红)
					dtl.setBal(lastBal.add(dtl.getAmt()));
				}
				lastBal = dtl.getBal();
				newList.add(dtl);
			}
			getSession().clear();
			this.batchSaveOrUpdate(newList);
		}
	}
	/**
	 * 更新指定交易时间后的所有余额（刷新余额）
	 *
	 * @param entity
	 * @param endDate 截止日期
	 */
	public void updateAfterBtBankAccDtlForFlush(BtBankAccDtl entity, Date endDate) {
		List<BtBankAccDtl> afterList = this.btBankAccDtlDao.listAfterBtBankAccDtl(entity.getBankAcc(), entity.getCurId(), entity.getTransTime(), entity.getOrderNo(), endDate);
		List<BtBankAccDtl> newList = new ArrayList<>();
		if(EmptyUtils.isEmpty(entity.getBal()) || entity.getBal().compareTo(BigDecimal.ZERO) == 0 ){
			com.bytter.biz.bdlink.api.dto.BisAccBalDTO bisAccBal =  bisAccBalService.getInitBisAccBal(entity.getBankAcc(), entity.getCurId());
			BigDecimal oldBal = BigDecimal.ZERO;
			if(bisAccBal != null && !EmptyUtils.isEmpty(bisAccBal.getBalDate()) && bisAccBal.getBalDate().before(entity.getTransTime())){
				oldBal = bisAccBal.getBal();
			}
			if(entity.getCdSign().equalsIgnoreCase(BisAccBalConstant.CDSIGN_IN)) {
				entity.setBal(oldBal.add(entity.getAmt()));
			}else{
				entity.setBal(oldBal.subtract(entity.getAmt()));
			}
			newList.add(entity);
		}
		// 更新余额
		if (!EmptyUtils.isEmptyList(afterList)) {
			BigDecimal lastBal = entity.getBal();
			if (BtBankAccConstant.STATUE_DEL.intValue() == entity.getStatus().intValue()) {
				if(entity.getCdSign().equalsIgnoreCase(BisAccBalConstant.CDSIGN_IN)) {
					lastBal = entity.getBal().add(entity.getAmt());
				}else{
					lastBal = entity.getBal().subtract(entity.getAmt());
				}
			}
			for (BtBankAccDtl dtl : afterList) {
				//如果包含自己就跳过。防止重新计算
				if(dtl.getId().equals(entity.getId())){
					continue;
				}
				boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
				boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
				if (outBlue || inRed) {
					//支出(蓝) 收入(红)
					dtl.setBal(lastBal.subtract(dtl.getAmt()));
				} else {
					//收入(蓝) 支出(红)
					dtl.setBal(lastBal.add(dtl.getAmt()));
				}
				//审批通过的数据，同步更新银行账户流水表
				if(CommonConstant.STATUE_CHECK.toString().equals(dtl.getStatus().toString())){
//					bisAccDtlService.updateBal(dtl);
				}
				lastBal = dtl.getBal();
				newList.add(dtl);
			}
			getSession().clear();
			this.batchSaveOrUpdate(newList);
		}
	}
	public void batchUpdateAfterBtBankAccDtl(List<BtBankAccDtl> list, ICurrentUser user) {
		// 获取每个账户最小的流水数据
		List<BtBankAccDtl> importList = new ArrayList<>();
		Map<String, BtBankAccDtl> minMap = new HashMap<>();
		for (BtBankAccDtl btBankAccDtl : list) {
		    //通过账号和币别确定账号唯一
			if (minMap.containsKey(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId())) {
				BtBankAccDtl oldDtl = minMap.get(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId());
				if (btBankAccDtl.getTransTime().compareTo(oldDtl.getTransTime()) == -1) {
					importList.add(minMap.get(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId()));
					minMap.put(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId(), btBankAccDtl);
				} else if (btBankAccDtl.getTransTime().compareTo(oldDtl.getTransTime()) == 0
						&& btBankAccDtl.getOrderNo() < oldDtl.getOrderNo()) {
					importList.add(minMap.get(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId()));
					minMap.put(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId(), btBankAccDtl);
				} else {
					importList.add(btBankAccDtl);
				}
			} else {
				importList.add(btBankAccDtl);
				minMap.put(btBankAccDtl.getBankAcc()+btBankAccDtl.getCurId(), btBankAccDtl);
			}
		}
		// 循环计算余额
		for (Map.Entry<String, BtBankAccDtl> entry : minMap.entrySet()) {
			BtBankAccDtl minDtl = entry.getValue();
			List<BtBankAccDtl> afterList = this.btBankAccDtlDao.listAfterBtBankAccDtl(minDtl.getBankAcc(), minDtl.getCurId(), minDtl.getTransTime(), minDtl.getOrderNo(), null);
			Map<BigDecimal, BtBankAccDtl> newMap = new TreeMap<>(
					new Comparator<BigDecimal>() {
						@Override
						public int compare(BigDecimal obj1, BigDecimal obj2) {
							// 降序排序
							return obj1.compareTo(obj2);
						}
					});
			for (BtBankAccDtl dtl : importList) {
				if (dtl.getBankAcc().equalsIgnoreCase(minDtl.getBankAcc()) && dtl.getCurId().equalsIgnoreCase(minDtl.getCurId())) {
					newMap.put(new BigDecimal(dtl.getTransTime().getTime() + "" + dtl.getOrderNo()), dtl);
				}
			}
			if (!EmptyUtils.isEmptyList(afterList)) {
				for (BtBankAccDtl dtl : afterList) {
					if (!newMap.containsKey(new BigDecimal(dtl.getTransTime().getTime() + "" + dtl.getOrderNo()))) {
						newMap.put(new BigDecimal(dtl.getTransTime().getTime() + "" + dtl.getOrderNo()), dtl);
					}
				}
			}
			// 更新余额
			if (!EmptyUtils.isEmpty(newMap) && newMap.size() > 0) {
				BigDecimal bal = this.getDownBankBal(minDtl.getCdSign(), minDtl.getRbSign(), minDtl.getBankAcc(), minDtl.getAmt().toString(), minDtl.getTransTime(), minDtl.getCurId(), minDtl.getOrderNo());
				minDtl.setBal(bal);
				BigDecimal lastBal = minDtl.getBal();
				//if (BtBankAccConstant.STATUE_DEL.intValue() == minDtl.getStatus().intValue()) {
				if(minDtl.getCdSign().equalsIgnoreCase(BisAccBalConstant.CDSIGN_IN)) {
					lastBal = minDtl.getBal().subtract(minDtl.getAmt());
				}else{
					lastBal = minDtl.getBal().add(minDtl.getAmt());
				}
				//}
				for (Map.Entry<BigDecimal, BtBankAccDtl> entryDtl : newMap.entrySet()) {
					BtBankAccDtl dtl = entryDtl.getValue();
					if (BtBankAccConstant.STATUE_DEL.intValue() == dtl.getStatus().intValue()) {
						continue;
					}
					boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
					boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
					if (outBlue || inRed) {
						//支出(蓝) 收入(红)
						dtl.setBal(lastBal.subtract(dtl.getAmt()));
						lastBal = lastBal.subtract(dtl.getAmt());
					} else {
						//收入(蓝) 支出(红)
						dtl.setBal(lastBal.add(dtl.getAmt()));
						lastBal = lastBal.add(dtl.getAmt());
					}
					if (EmptyUtils.isEmpty(dtl.getId())) {
						dtl.setBal(lastBal);
						//生成日志
						this.save(dtl);
						btBankAccMessageService.saveMessage(dtl.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_ADD, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
					} else {
						this.update(dtl);
					}
				}
				this.saveOrUpdate(minDtl);
			}
		}
	}
	/**
	 * 批量提交
	 *
	 * @param ids
	 * @return
	 */
	public void batchSubmitBtBankAccDtl(String ids, ICurrentUser user) {
		String[] dtlIds = ids.split(CommonConstant.COMMA_SPLIT);
		for (String id : dtlIds) {
			BtBankAccDtl btBankAccDtl = this.get(id);
			if (!(BtBankAccConstant.STATUE_TEMP.intValue() == btBankAccDtl.getStatus().intValue()
					|| BtBankAccConstant.STATUE_BACK.intValue() == btBankAccDtl.getStatus().intValue())) {
				throw new ParameterException(BankAccDtlMessageEnum.BT_BANKACCDTL_MESSAGE_SUBMIT_TEMP_OR_BACK);
			}
		}
		for (String id : dtlIds) {
			BtBankAccDtl entity = this.get(id);
			entity.setStatus(BisAccBalConstant.STATUE_WAIT_CHECK);
			// 挂接工作流
			if (!BisAccBalConstant.STATUE_TEMP.equals(entity.getStatus())) {
				 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_DTL, entity.getId(), entity, entity.getCorpId(), user.getId());
				//生成日志
				btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
			}
		}
	}

	/**
	 * 批量提交
	 *
	 * @param newList
	 * @return
	 */
	public void batchSubmitBtBankAccDtl(List<BtBankAccDtl> newList, ICurrentUser user) {
		Map<String, List<BtBankAccDtl>> mapList = new HashMap<>();
		for (BtBankAccDtl  entity : newList) {
			//保存流水
			saveBisAccDtlForBatch(entity, user);
			String key = entity.getBankAcc() + "," + entity.getCurId();
			if (mapList.containsKey(key)) {
				List<BtBankAccDtl> list = mapList.get(key);
				if (EmptyUtils.isNotEmpty(list)) {
					list.add(entity);
				} else {
					list = new ArrayList<>();
					list.add(entity);
				}
				mapList.put(key, list);
			} else {
				List<BtBankAccDtl> list = new ArrayList<>();
				list.add(entity);
				mapList.put(key, list);
			}
		}

		//计算余额
		for (String key : mapList.keySet()) {
			String[] val = key.split(",");
			if (val.length == 2) {
				String acc = val[0];
				String curId = val[1];
				Date beginDate = DateUtils.getDateYMD();
				Date endDate = DateUtils.getDateYMD();
				endDate = DateUtils.addDay(endDate,1);
				List<BtBankAccDtl> list = mapList.get(key);
				for (BtBankAccDtl btBankAccDtl : list) {
					if (!EmptyUtils.isEmpty(btBankAccDtl)) {
						if (btBankAccDtl.getTransTime().before(beginDate)) {
							beginDate = btBankAccDtl.getTransTime();
						}
//						if (btBankAccDtl.getTransTime().after(endDate)) {
//							endDate = btBankAccDtl.getTransTime();
//						}
					}
				}
				BtBankAccDTO bankAcc = iBtBankAccService.getBankAccInfo(acc);
				com.bytter.biz.bdlink.api.dto.BtBankAccDTO bankAccDTO = new com.bytter.biz.bdlink.api.dto.BtBankAccDTO();
				BeanUtils.copyNotNullProperties(bankAcc, bankAccDTO);
				bankAccDTO.setCurId(curId);
				bisAccBalService.saveBisAccBalByDtl(bankAccDTO, beginDate, endDate, user);
				//先根据账号id和币别id取余额自动计算信息表信息
//				BisAccBalAutoDTO auto = bisAccBalAutoService.getBisAccBalAutoByAccCurId(bankAcc.getId(), curId);
//				if (auto != null && !EmptyUtils.isEmpty(auto.getEndDate()) && auto.getEndDate().after(endDate)) {
//					auto.setEndDate(DateUtils.parseDate(DateUtils.format(endDate, DateUtils.PATTERN_CLASSICAL_SIMPLE)));
//					bisAccBalAutoService.updateEntity(auto);
//				}
			}
		}
	}
	/**
	 * 撤回
	 *
	 * @param workFlowResult
	 */
	@Override
	public void flowRevoke(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		BtBankAccDtl entity = this.get(workFlowResult.getBusinessKey(), true);
		entity.setStatus(CommonConstant.STATUE_TEMP);
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
		//生成日志
		btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, "", BtBankAccMessageConstant.BUS_TYPE_ACCDTL, user);
	}
	/**
	 * 刷新余额
	 * @param list 更新余额
	 */
	public void flushBal(List<BtBankAccDtlVO> list){
		//直接刷新余额
		Map<String,BtBankAccDtlVO> map = new HashMap();
		for(BtBankAccDtlVO vo :list){
			map.put(vo.getCurId()+vo.getAccName(),vo);
		}
		for(String key:map.keySet()){
			BtBankAccDtlVO vo = map.get(key);
			List<BtBankAccDtl> earliestDtl = this.btBankAccDtlDao.getEarliestDtl(vo.getBankAcc(), vo.getCurId());
			if(earliestDtl.size()>0){
				this.updateAfterBtBankAccDtlForFlush(earliestDtl.get(0), null);
			}
		}
	}
	/**
	 * 刷新余额
	 */
	public void flushBal(){
		List<BtBankAccDtl> list = getListForFlush();
		for(BtBankAccDtl data:list){
			List<BtBankAccDtl> earliestDtl = this.btBankAccDtlDao.getEarliestDtl(data.getBankAcc(), data.getCurId());
			if(earliestDtl.size()>0){
				this.updateAfterBtBankAccDtlForFlush(earliestDtl.get(0), null);
			}
		}
	}
	public List<BtBankAccDtl> getListForFlush() {
		return this.btBankAccDtlDao.getListForFlush();
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有在办的银行账户流水信息，不允许禁用！
			int num = this.btBankAccDtlDao.getBankAccDtlCountByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_BANKACC_DTL,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_BANKACC_DTL_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
	/**
	 * 刷新余额
	 * @param bankAccDtlVO 更新余额
	 */
	public void flushBal(BtBankAccDtlVO bankAccDtlVO){
		if(EmptyUtils.isNotEmpty(bankAccDtlVO.getBtBankAccList())) {
			for (Map<String, Object> map : bankAccDtlVO.getBtBankAccList()) {
				String curId = String.valueOf(map.get("accCurId"));
				String bankAcc = String.valueOf(map.get("bankAcc"));
				Date searchDate = null;
				if(bankAccDtlVO.getBeginDate() != null){
					searchDate = DateUtils.addDay(bankAccDtlVO.getBeginDate(), -1);
				}
				searchDate = DateUtils.parse(DateUtils.format(searchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE) + " 23:59:59", DateUtils.PATTERN_CLASSICAL);
				List<BtBankAccDtl> earliestDtlList = this.btBankAccDtlDao.getEarliestDtl(bankAcc, curId, searchDate);
				BtBankAccDtl earliestDtl = null;
				if(EmptyUtils.isNotEmpty(earliestDtlList)){
					earliestDtl = earliestDtlList.get(0);
				} else {
					earliestDtl = new BtBankAccDtl();
					earliestDtl.setBankAcc(bankAcc);
					earliestDtl.setCurId(curId);
					earliestDtl.setBal(BigDecimal.ZERO);
					earliestDtl.setAmt(BigDecimal.ZERO);
					earliestDtl.setCdSign(BisAccBalConstant.CDSIGN_IN);
					earliestDtl.setTransTime(searchDate);
					earliestDtl.setOrderNo(0);
					earliestDtl.setStatus(CommonConstant.STATUE_TEMP);
				}
				this.updateAfterBtBankAccDtlForFlush(earliestDtl, bankAccDtlVO.getEndDate());
			}
		}
	}

	/**
	 * 填充属性值
	 */
	public void setAttributes(DataTablesOutput dataTablesOutput){
		List<BtBankAccDtlVO> btBankAccDtlList = dataTablesOutput.getData();
		if(btBankAccDtlList.size() > 0){
			//获取币种信息
			Map<String, BtCurrencyDTO> btCurrencyMap= btCurrencyService.getCurrency();
			//获取单位信息
			Map<String, CorpDTO> corpMap = corpService.getCorpMap();
			//拼接银行帐号
			List<String> bankAccS=new ArrayList<>();
			for(BtBankAccDtlVO vo:btBankAccDtlList){
				bankAccS.add(vo.getBankAcc());
			}
			//遍历填充属性值
			for(BtBankAccDtlVO vo : btBankAccDtlList){
				//单位名称
				if(corpMap != null){
					if(corpMap.containsKey(vo.getCorpId())){
						String corpName  = corpMap.get(vo.getCorpId()).getName();
						vo.setCorpName(corpName);
					}
				}
				//币别
				if(btCurrencyMap != null){
					if(btCurrencyMap.containsKey(vo.getCurId())){
						String curName  = btCurrencyMap.get(vo.getCurId()).getCurName();
						vo.setCurName(curName);
					}
				}
				if(bankAccS.size()>0) {
					//获取银行账户信息
					Map<String, BtBankAccDTO> accMap = btBankAccService.getBankAccByInfoAccs(bankAccS);
					if(!EmptyUtils.isEmpty(accMap)){
						if(accMap.containsKey(vo.getBankAcc())){
							BtBankAccDTO dto = accMap.get(vo.getBankAcc());
							if(!EmptyUtils.isEmpty(dto)){
								//账户类别id
								vo.setAccTypeId(dto.getAccType());
								//账户属性id
								vo.setAttributeId(dto.getAttributeId());
								//账户属性
								vo.setAttributeName(dto.getAttributeName());
								//账户性质id
								vo.setNatureId(dto.getNatureId());
								//账户性质
								vo.setNatureName(dto.getNatureName());
							}
						}
					}

				}
			}
		}
	}

	public void saveRpaImport(List<BtBankAccDtlRpaVO> rpalist, ICurrentUser currentUser) {
		List<BtBankAccDtlVO> dtlList = converDTO(rpalist);
		saveImport(dtlList,currentUser);
		//重新刷新余额
//		this.flushBal(dtlList);
	}

	private List<BtBankAccDtlVO> converDTO(List<BtBankAccDtlRpaVO> rpalist) {
		List<BtBankAccDtlVO> dtlList = new ArrayList<>();
		//币种
//		String rmbId = btCurrencyService.getCurrencyByName("人民币");
		for(BtBankAccDtlRpaVO rpaVO : rpalist){
			if(EmptyUtils.isEmpty(rpaVO.getCdSign())){
				continue;
			}
			BtBankAccDtlVO dtlVO = new BtBankAccDtlVO();
			BeanUtils.copyProperties(rpaVO,dtlVO);

			//交易时间
			Calendar cal = Calendar.getInstance();
			cal.setTime(rpaVO.getTransDate());
			cal.add(Calendar.HOUR, rpaVO.getTransDtlTime().getHours());
			cal.add(Calendar.MINUTE, rpaVO.getTransDtlTime().getMinutes());
			cal.add(Calendar.SECOND, rpaVO.getTransDtlTime().getSeconds());
			dtlVO.setTransTime(cal.getTime());

			dtlList.add(dtlVO);
		}
		return dtlList;
	}

	public DataTablesOutput getDataTablesUnionForDtlImport(DataTablesInput dataTablesInput, ICurrentUser user) {
		StopWatch stopWatch = new StopWatch("银行流水查询效率");
		stopWatch.start("getCorpIdsByUserResource");
		StringBuilder sql = new StringBuilder();
		List<String> corpList = corpService.getCorpIdsByUserResource(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, user.getId());
		stopWatch.stop();
		stopWatch.start("listByMultiValAccs");
		// 权限控制类型  1-单位级 2-账号级
		List<String> accList = new ArrayList<String>();
		String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
		if (!CommonConstant.YES.equals(controlType)) {
			accList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
		}
		stopWatch.stop();
		//根据查询条件及账户数据进行一次过滤
		stopWatch.start("getAcclistByCondition");
		List<String> accList2 = btBankAccDtlDao.getAcclistByCondition(dataTablesInput, corpList, accList);
		stopWatch.stop();
		String accAuthSql = SqlUtil.getInSql(accList2, "t.bank_acc", true);
		sql.append(" and ").append(accAuthSql);
		stopWatch.start("getDataTablesUnionV2");
		DataTablesOutput<BisAccDetailVO> result = this.btBankAccDtlDao.getDataTablesUnionV2(dataTablesInput, sql.toString());
		stopWatch.stop();
		List<BisAccDetailVO> data = result.getData();
		stopWatch.start("getNameByIds");
		Map<String, String> corpNameMap = corpService.getNameByIds(data.stream().map(BisAccDetailVO::getCorpId).collect(Collectors.toList()));
		stopWatch.stop();
		stopWatch.start("getCurrencyNameByIds");
		Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(data.stream().map(BisAccDetailVO::getCurId).collect(Collectors.toList()));
		stopWatch.stop();
		stopWatch.start("listDictMap");
		Map<String, Map<String, DictDTO>> dictMap = dictService.listDictMap("btBankAccDtl.cdSign", "btBankAcc.accType", "sf_yn");
		stopWatch.stop();
		stopWatch.start("组装数据");
		//查询所有账户组
		List<FcBankAccGroupDTO> validPayType = fcBankAccGroupService.getValidPayType();
		HashMap<String, String> map = new HashMap<>();
		for (FcBankAccGroupDTO fcBankAccGroup : validPayType) {
			map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
		}
		for (BisAccDetailVO vo : data) {
			vo.setCorpName(corpNameMap.get(vo.getCorpId()));
			vo.setCurName(currencyNameMap.get(vo.getCurId()));
			if(StringUtils.isNotEmpty(vo.getCdSign())){
				vo.setCdSignName(dictMap.get("btBankAccDtl.cdSign").getOrDefault(vo.getCdSign(),DictDTO.EMPTY).getName());
			}
			if(StringUtils.isNotEmpty(vo.getAccType())){
				vo.setAccTypeName(dictMap.get("btBankAcc.accType").getOrDefault(vo.getAccType(),DictDTO.EMPTY).getName());
			}
			if(StringUtils.isNotEmpty(vo.getIsOnline())){
				vo.setIsOnlineStr(dictMap.get("sf_yn").getOrDefault(vo.getIsOnline(),DictDTO.EMPTY).getName());
			}
			//查询账户组名称
			String fcAccGroupCode = vo.getFcAccGroupCode();
			if (EmptyUtils.isNotEmpty(fcAccGroupCode)){
				List<String> codes = Arrays.asList(fcAccGroupCode.split(","));
				StringBuilder accGroupName = new StringBuilder();
				for(int i = 0;i<=codes.size()-1;i++){
					if (i==codes.size()-1){
						if (map.containsKey(codes.get(i))){
							accGroupName.append(map.get(codes.get(i)));
						}
					}else{
						if (map.containsKey(codes.get(i))){
							accGroupName.append(map.get(codes.get(i))+",");
						}
					}
				}
				vo.setFcAccGroupName(accGroupName.toString());
			}

			// 推送状态
			String tempStatus = "否";
			if (!EmptyUtils.isEmpty(vo.getSyncStatus()) && "S".equals(vo.getSyncStatus())){
				tempStatus = "是";
			}
			vo.setSyncStatus(tempStatus);
		}
		stopWatch.stop();
		logger.error(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
		return result;
	}

	public List<BisAccDtlExportDTO> listExport(DataTablesInput dataTablesInput, ICurrentUser user) {
		// dataTablesInput.setPageSize(1000000);
		DataTablesOutput<BisAccDetailVO> data = getDataTablesUnionForDtlImport(dataTablesInput,user);
		List<BisAccDetailVO> bisAccDetailVO = data.getData();
		List<BisAccDtlExportDTO> dtlList = BeanUtils.copyListProperties(bisAccDetailVO,BisAccDtlExportDTO.class);
		return dtlList;
	}

	//查询是否存在流水记录
	public Integer selectDtlCount(String bankAcc, String accCurId) {
		return btBankAccDtlDao.selectDtlCount(bankAcc, accCurId);
	}
}
