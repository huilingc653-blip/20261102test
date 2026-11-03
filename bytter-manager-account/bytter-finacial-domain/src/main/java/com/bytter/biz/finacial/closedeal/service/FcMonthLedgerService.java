package com.bytter.biz.finacial.closedeal.service;

import com.bytter.biz.finacial.closedeal.dao.FcMonthLedgerDao;
import com.bytter.biz.finacial.closedeal.entity.FcAccMonthBalance;
import com.bytter.biz.finacial.closedeal.entity.FcMonthLedger;
import com.bytter.biz.finacial.closedeal.vo.AccBalanceBean;
import com.bytter.biz.finacial.closedeal.vo.FcAccMonthBalBean;
import com.bytter.biz.finacial.closedeal.vo.FcMonthLedgerBean;
import com.bytter.biz.finacial.closedeal.vo.FcSubjectKeyBean;
import com.bytter.biz.payment.api.dto.FcInitAccDTO;
import com.bytter.biz.payment.api.service.IFcInitAccService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IFcSubjectDayService;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import com.bytter.system.api.dto.FcSubjectDayBeanDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
/**
 * @Title: Service
 * @Description: FC_MONTH_LEDGER
 * @author onlineGenerator
 * @date 2017-07-17 18:36:41
 * @version 1.0
 *
 */
@Service("fcMonthLedgerService")
@Transactional
public class FcMonthLedgerService extends BaseService<FcMonthLedger, String> {
	@Autowired
	private FcMonthLedgerDao fcMonthLedgerDao;
	@Autowired
	private FcAccMonthBalanceService monBalService;
	@Autowired
	private ClFcAccBalanceService balanceService;
	@DubboReference(check = false)
	private IFcSubjectDayService iFcSubjectDayService;
	@DubboReference(check = false)
	private IFcInitAccService fcInitAccService;
	/**
	 * 删除对象
	 * 
	 * @param entity
	 */
	public void deleteEntity(FcMonthLedger entity) {
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
	public void save(FcMonthLedger entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public void update(FcMonthLedger entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(FcMonthLedger entity) {
		super.saveOrUpdate(entity);
	}
	public BaseDao<FcMonthLedger, String> getEntityDao() {
		return fcMonthLedgerDao;
	}
	/**
	 * 功能介绍 : 产生月总账数据。<br>
	 * <b>日期：</b>2017年7月17日<br>
	 * 
	 * @version 1.0
	 * @author liyh
	 * @return 
	 */
	public List<FcMonthLedger> generateMonthLedger(FcMonthCloseDTO closeDTO) {
		//获取月结区间的科目日结单数据
		List<FcSubjectDayBeanDTO> fcSubjectDayBeanDTOS = iFcSubjectDayService.querySetMonthSubDay(closeDTO);
		Map<FcSubjectKeyBean, FcMonthLedgerBean> ledgerMap = new HashMap<FcSubjectKeyBean, FcMonthLedgerBean>();
		Date opDate = null;
		for (FcSubjectDayBeanDTO dayBean : fcSubjectDayBeanDTOS) {
			String netId = dayBean.getNetId();
			String curId = dayBean.getCurId();
			String subjectId = dayBean.getSubjectId();
			FcSubjectKeyBean keyBean = new FcSubjectKeyBean(netId, subjectId, curId);
			FcMonthLedgerBean ledgerBean = ledgerMap.get(keyBean);
			if (null == ledgerBean) {
				ledgerBean = new FcMonthLedgerBean(keyBean);
				ledgerBean.setYearMonth2(closeDTO.getYearMonth2());
				ledgerBean.setBalanceDate(dayBean.getOptDate());// 科目余额的日期
				ledgerBean.setBalance(dayBean.getTodayBalance());// 当日余额
				ledgerMap.put(keyBean, ledgerBean);
			}
			ledgerBean.addDebitMoney(dayBean.getTrDebitMoney());// 转账借方金额
			ledgerBean.addDebitMoney(dayBean.getCashDebitMoney());// 现金借方金额
			ledgerBean.addCreditMoney(dayBean.getTrCreditMoney());// 转账贷方金额
			ledgerBean.addCreditMoney(dayBean.getCashCreditMoney());// 现金贷方金额
			ledgerBean.addDebitCnt(dayBean.getTrDebitCnt());// 转账借方笔数
			ledgerBean.addDebitCnt(dayBean.getCashDebitCnt());// 现金借方笔数
			ledgerBean.addCreditCnt(dayBean.getTrCreditCnt());// 转账贷方笔数
			ledgerBean.addCreditCnt(dayBean.getCashCreditCnt());// 现金贷方笔数
			// 取最大日期的今日余额作为余额
			if (null != opDate) {
				// 后面余额日期大于前面的余额日期
				if (DateUtils.dayDiffs(opDate, ledgerBean.getBalanceDate()) > 0) {
					ledgerBean.setBalance(dayBean.getTodayBalance());// 取最大日期的今日余额作为余额
				}
			}
			opDate = dayBean.getOptDate();
		}
		// 更新初始化余额
		// 上一月结周期的月总账数据
		List<FcMonthLedger> ledgers = fcMonthLedgerDao.queryMaxMonthLedger(closeDTO);
		if (null == ledgers || ledgers.isEmpty()) {
			// 根据网点和日期获取初始化数据
//			List<FcInitAccDTO> initAccDTOs = fcMonthLedgerDao.queryNetInitAcc(closeDTO);
			com.bytter.biz.payment.api.dto.FcMonthCloseDTO fcMonthCloseDTO = new com.bytter.biz.payment.api.dto.FcMonthCloseDTO();
			BeanUtils.copyProperties(closeDTO,fcMonthCloseDTO);
			List<FcInitAccDTO> initAccDTOs = fcInitAccService.queryInitAccByNetId(fcMonthCloseDTO.getNetCode());
			if (null != initAccDTOs && !initAccDTOs.isEmpty()) {
				for (FcInitAccDTO initAccDTO : initAccDTOs) {
					String netId = initAccDTO.getNetId();
					String curId = initAccDTO.getCurId();
					String subjectId = initAccDTO.getSubjectId();
					FcSubjectKeyBean keyBean = new FcSubjectKeyBean(netId, subjectId, curId);
					FcMonthLedgerBean ledgerBean = ledgerMap.get(keyBean);
					if (null == ledgerBean) {
						ledgerBean = new FcMonthLedgerBean(keyBean);
						ledgerBean.setYearMonth2(closeDTO.getYearMonth2());
						ledgerMap.put(keyBean, ledgerBean);
					}
					ledgerBean.addInitBalance(initAccDTO.getBalance());
				}
			}
		} else {
			for (FcMonthLedger ledger : ledgers) {
				String netId = ledger.getNetCode();
				String curId = ledger.getCurrencyCode();
				String subjectId = ledger.getSubjectCode();
				FcSubjectKeyBean keyBean = new FcSubjectKeyBean(netId, subjectId, curId);
				FcMonthLedgerBean ledgerBean = ledgerMap.get(keyBean);
				if (null == ledgerBean) {
					ledgerBean = new FcMonthLedgerBean(keyBean);
					ledgerBean.setYearMonth2(closeDTO.getYearMonth2());
					ledgerMap.put(keyBean, ledgerBean);
				}
				ledgerBean.addInitBalance(ledger.getBalance());
				//// 是否年初
				if (closeDTO.getCloseMonth().intValue() == 1) {
					// 年初累计借贷方发生额 = 上期(去年年末)的借贷方金额
					ledgerBean.addTotalDebitMoney(ledger.getDebitMoney());// 累计借方金额
					ledgerBean.addTotalCreditMoney(ledger.getCreditMoney());// 累计贷方金额
				} else {
					// 非年初的累计借贷方发生额 = 上期的累计借贷方金额 + 本期的借贷方金额
					ledgerBean.addTotalDebitMoney(ledgerBean.getDebitMoney());// 本期的借方金额
					ledgerBean.addTotalDebitMoney(ledger.getTotalDebitMoney());// 上期累计借方金额
					ledgerBean.addTotalCreditMoney(ledgerBean.getCreditMoney());// 本期贷方金额
					ledgerBean.addTotalCreditMoney(ledger.getTotalCreditMoney());// 上期累计贷方金额
				}
			}
		}
		// 保存月总账数据
		List<FcMonthLedger> monthLedgers = new ArrayList<FcMonthLedger>();
		for (Entry<FcSubjectKeyBean, FcMonthLedgerBean> entry : ledgerMap.entrySet()) {
			FcMonthLedgerBean value = entry.getValue();
			FcMonthLedger ledger = new FcMonthLedger();
			BeanUtils.copyProperties(value, ledger);
			monthLedgers.add(ledger);
			this.saveEntity(ledger);
		}
		// 月总量表
		//账户月结区间的余额数据
		List<AccBalanceBean> balanceBeans = balanceService.querySetMonthBalance(closeDTO);
		Map<String, FcAccMonthBalBean> balMap = new HashMap<String, FcAccMonthBalBean>();
		if (null != balanceBeans && !balanceBeans.isEmpty()) {
			for (AccBalanceBean balanceBean : balanceBeans) {
				String account = balanceBean.getAccount();
				Date balanceDate = balanceBean.getBalanceDate();
				FcAccMonthBalBean balBean = balMap.get(account);
				if (null == balBean) {
					balBean = new FcAccMonthBalBean();
					balBean.setAccount(account);
					balBean.setAccName(balanceBean.getAccName());
					balBean.setYearMonth2(closeDTO.getYearMonth2());
					balBean.setBalance(new BigDecimal(balanceBean.getBalanceWay()));
					balBean.setTrDebitMoney(BigDecimal.ZERO);
					balBean.setTrCreditMoney(BigDecimal.ZERO);
					balBean.setRushDebitMoney(BigDecimal.ZERO);
					balBean.setRushCreditMoney(BigDecimal.ZERO);
					balBean.setDebitMoney(BigDecimal.ZERO);
					balBean.setCreditMoney(BigDecimal.ZERO);
					balMap.put(account, balBean);
				}
				if (DateUtils.dayDiffs(closeDTO.getCloseDate(), balanceDate) == 0) {// 期初余额
					balBean.addPreBalance(balanceBean.getYesterBalance());// 昨日余额
				} else {
					balBean.addTrCnt(balanceBean.getTrCnt());// 内转笔数
					balBean.addRushCnt(balanceBean.getRushCnt());// 冲账笔数
					//取最大的笔数
					if (null != balBean.getCnt()) {
						if (null != balanceBean.getCnt()) {
							if (balBean.getCnt() < balanceBean.getCnt()) {
								balBean.setCnt(balanceBean.getCnt());//取最大的笔数
							}
						}
					} else {
						balBean.setCnt(balanceBean.getCnt());//设置笔数
					}
					balBean.addTrDebitMoney(balanceBean.getTrDebitMoney());// 内转借方金额
					balBean.addTrCreditMoney(balanceBean.getTrCreditMoney());// 内转贷方金额
					balBean.addRushDebitMoney(balanceBean.getRushDebitMoney());// 冲账借方金额
					balBean.addRushCreditMoney(balanceBean.getRushCreditMoney());// 冲账借方金额
					balBean.addDebitMoney(balanceBean.getDebitMoney());// 总借方金额
					balBean.addCreditMoney(balanceBean.getCreditMoney());// 总借方金额
				}
			}
		}
		List<FcAccMonthBalBean> beans = new ArrayList<FcAccMonthBalBean>(balMap.values());
		for (FcAccMonthBalBean balBean : beans) {
			// 发生额
			BigDecimal balance = (balBean.getDebitMoney().subtract(balBean.getCreditMoney())
					.multiply(balBean.getBalance()));
			balBean.setBalance(balBean.getPreBalance().add(balance));
			FcAccMonthBalance monthBalance = new FcAccMonthBalance();
			BeanUtils.copyNotNullProperties(balBean, monthBalance);
			if (null == monthBalance.getCnt()) {
//				笔数
				monthBalance.setCnt(0);
			}
			if (null == monthBalance.getRushCnt()) {
//				冲账笔数
				monthBalance.setRushCnt(0);
			}
			if (null == monthBalance.getTrCnt()) {
//				内转笔数
				monthBalance.setTrCnt(0);
			}
			monBalService.saveEntity(monthBalance);
		}
		getEntityDao().flush();
		return monthLedgers;
	}
	
	/**
	 * 功能介绍 : 转移月总账数据。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void reverseMonthLedger(FcMonthCloseDTO closeDTO) {
		fcMonthLedgerDao.deleteMonthLedger(closeDTO);
		monBalService.deleteAccMonthBalance(closeDTO);
	}
}
