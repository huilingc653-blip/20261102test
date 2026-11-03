package com.bytter.biz.finacial.closedeal.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.biz.finacial.closedeal.dao.ClFcAccBalanceDao;
import com.bytter.biz.finacial.closedeal.dao.ClFcAccuBalanceDao;
import com.bytter.biz.finacial.closedeal.entity.ClFcAccuBalance;
import com.bytter.biz.finacial.closedeal.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.dto.FcAgreeDepositDetailDTO;
import com.bytter.increment.api.dto.FcAgreedDepositDTO;
import com.bytter.increment.api.service.IFcAgreedDepositService;
import com.bytter.increment.api.service.IFcAgreedDetailService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: FC_ACCU_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:03:48
* @version 1.0
*
*/
@Service("clFcAccuBalanceService")
@Transactional
public class ClFcAccuBalanceService extends BaseService<ClFcAccuBalance,String> {
	@Autowired
	private ClFcAccuBalanceDao balanceDao;
	@Autowired
	private ClFcAccBalanceDao accBalanceDao;
    @DubboReference(check = false)
	private IFcAgreedDetailService agreedDetailService;
    @DubboReference(check = false)
	private IFcAgreedDepositService iFcAgreedDepositService;
    @Autowired
	private FcDayOverService fcDayOverService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	private int lastAgreedType = 0;
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(ClFcAccuBalance entity){
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(String id){
		super.delete(id);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(ClFcAccuBalance entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(ClFcAccuBalance entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(ClFcAccuBalance entity){
		super.saveOrUpdate(entity);
	}
	public BaseDao<ClFcAccuBalance, String> getEntityDao() {
		return balanceDao;
	}
	/**
	 * 功能介绍 : 获取需要调整积数的明细账。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<AccAdjustDtlBean> queryAdjustBookDtl(String netId, Date tallyDate) {
		List<String> accounts = iFcAccService.getBlnctrAccByNetId(netId);
		if (CollectionUtil.isEmpty(accounts)) {
			return Lists.newArrayList();
		}
		return balanceDao.queryAdjustBookDtl(netId, tallyDate, accounts);
	}
	/**
	 * 功能介绍 : 查询账户日结区间的积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcAccuBalanceBean> queryAccountAccuBalance(String account, Date balanceDate) {
		return balanceDao.queryAccountAccuBalanceBySql(account, balanceDate);
	}
	/**
	 * 功能介绍 : 查询日结当日已产生的积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	private Map<String, List<FcAccuBalanceBean>> queryNetAccuBalance(FcDayCloseVO dayClose, List<String> accountList) {
		List<FcAccuBalanceBean> accuBeans = balanceDao.queryNetAccuBalance(dayClose, accountList);//查询所有已产生的积数
		Map<String, List<FcAccuBalanceBean>> accuMap = new HashMap<String, List<FcAccuBalanceBean>>();
		if (null == accuBeans) {
			return accuMap;
		}
		for (FcAccuBalanceBean accuBean : accuBeans) {
			String account = accuBean.getAccount();
			List<FcAccuBalanceBean> beans = accuMap.get(account);
			if (null == beans ) {
				beans = new ArrayList<FcAccuBalanceBean>();
				accuMap.put(account, beans);
			}
			beans.add(accuBean);
		}
		return accuMap;
	}
	/**
	 * 功能介绍 : 查询账号指定日期的积数。<br>
	 * <b>日期：</b>2017年11月17日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcAccuBalanceBean> getAccuBalance(String account, Date balanceDate,
												  Map<String, List<FcAccuBalanceBean>> accuMap) {
		List<FcAccuBalanceBean> result = new ArrayList<FcAccuBalanceBean>();
		List<FcAccuBalanceBean> accuBeans = accuMap.get(account);
		if (EmptyUtils.isEmptyList(accuBeans)) {
			return null;
		}
		for (FcAccuBalanceBean balanceBean : accuBeans) {
			Date balDate = balanceBean.getBalanceDate();// 积数日期
			if (DateUtils.dayDiffs(balanceDate, balDate) == 0) {
				result.add(balanceBean);
			}
		}
		return result;
	}
    /**
     * <br>
     * <b>功能：</b> 批量更新账户积数 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/7/9 <br>
     */
	public void batchUpdateAccuBalance(List<FcAccuBalanceBean> accuBalBeans) {
		if (EmptyUtils.isEmptyList(accuBalBeans)) {
			return;
        }
		List<String> idList = new ArrayList<>();
		Map<String, FcAccuBalanceBean> accuMap = new HashMap<String, FcAccuBalanceBean>();
		for(FcAccuBalanceBean accuBean : accuBalBeans){
			idList.add(accuBean.getId());
            accuMap.put(accuBean.getId(), accuBean);
        }
        List<ClFcAccuBalance> accuBalances = balanceDao.getAccuList(idList);//获取账户积数
        for(ClFcAccuBalance accuBalance : accuBalances) {
            String id = accuBalance.getId();
            FcAccuBalanceBean accuBean = accuMap.get(id);
            if (null != accuBean) {
                accuBalance.setCumu(accuBean.getCumu());//更新未计息未预提的积数
            }
        }
        batchUpdate(accuBalances);
    }
    /**
    * <br>
    * <b>功能：</b> 批量新增非协定账户积数 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/7/9 <br>
    */
    public void batchInsertAccuBalance(List<FcAccuBalanceBean> accuBalBeans, ICurrentUser user) {
		if (EmptyUtils.isEmpty(accuBalBeans)) {
			return;
        }
        List<ClFcAccuBalance> accuBalances = new ArrayList<ClFcAccuBalance>();
        for (FcAccuBalanceBean accuBean : accuBalBeans) {
            ClFcAccuBalance accuBalance = new ClFcAccuBalance();
            accuBalance.setBalanceDate(accuBean.getBalanceDate());//积数日期
            accuBalance.setAccount(accuBean.getAccount());//账户
            accuBalance.setAgreedType(Integer.valueOf(accuBean.getAgreeType()));//协定段
            accuBalance.setInterestSign(CloseConstant.INTEREST_SIGN_N);
            accuBalance.setPreSign(CloseConstant.INTEREST_SIGN_N);
            accuBalance.setCurCode(accuBean.getCurId());
            accuBalance.setCumu(accuBean.getCumu());
            accuBalance.setAdjustCumu(BigDecimal.ZERO);
            accuBalance.setAgreedDepositSign("0");
            accuBalance.setAgreedDepositId(accuBean.getAgreeDepositId());//协定ID
            accuBalance.setCreateTime(DateUtils.getDate());
            accuBalance.setCreateBy(user.getLoginName());
            accuBalances.add(accuBalance);
        }
        batchSave(accuBalances);
    }
	/**
	 * 功能介绍 : 产生积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void generateAccuBalance(FcDayCloseVO dayClose, ICurrentUser user) {
//		List<AccuBalanceDtlBean> dtlBeans = balanceDao.queryCumuDtl(dayClose);//获取账户每日余额
		List<AccuBalanceDtlBean> dtlBeans = this.dealDate(dayClose);//获取账户每日余额
		if (null == dtlBeans) {
			return;
		}
		List<String> accountList = dtlBeans.stream().map(AccuBalanceDtlBean::getAccount).collect(Collectors.toList());
		Map<String, List<FcAccuBalanceBean>> accuMap = queryNetAccuBalance(dayClose,accountList);//查询所有已产生的积数


		List<FcAccuBalanceBean> existAccuBalBeans = new ArrayList<FcAccuBalanceBean>();//已存在的积数
        List<FcAccuBalanceBean> newAccuBalBeans = new ArrayList<FcAccuBalanceBean>();//需要新插入的积数
		for (AccuBalanceDtlBean dtlBean : dtlBeans) {
			Date accStartDate = dtlBean.getAccStartDate();//账号启用日期
			Date balanceDate = dtlBean.getBalanceDate();//余额日期
			String account = dtlBean.getAccount();//账号
			String curId = dtlBean.getCurId();//币别
			//积数日期必须大于等于开户日期才可以重新生成积数表资料,避免初始化积数被更新为0
			if (accStartDate.compareTo(balanceDate) > 0) {
				continue;
			}
			BigDecimal balance = dtlBean.getBalance();//产生积数的总余额
			List<FcAgreeDepositDetailDTO> agreeBeans = checkAgreeDeposit(account, balanceDate,false);
			FcAgreedDepositDTO agreed = iFcAgreedDepositService.getFcAgreedDepositByBlnctrAcc(account,balanceDate);
			if (EmptyUtils.isEmptyList(agreeBeans) && agreed == null) {
				//非协定账号
				List<FcAccuBalanceBean> accuBeans = getAccuBalance(account, balanceDate, accuMap);//获取余额日期的积数
				//获取已计息的积数总和
				BigDecimal alreadySum = BigDecimal.ZERO;
				if (null == accuBeans) {
					accuBeans = new ArrayList<FcAccuBalanceBean>();
					accuMap.put(account, accuBeans);
				}
				if (!EmptyUtils.isEmptyList(accuBeans)) {
					for (FcAccuBalanceBean accubean : accuBeans) {
						//已计息或已预提
						if (CloseConstant.INTEREST_SIGN_Y.equals(accubean.getInterestSign())
								|| CloseConstant.INTEREST_SIGN_Y.equals(accubean.getPreSign())) {
							alreadySum = alreadySum.add(accubean.getCumu());//已计息或预提积数
						}
					}
				}
				balance = balance.subtract(alreadySum);//剩余未计息积数
				boolean updateFlag = false;//未计息积数标识
				if (null != accuBeans && !accuBeans.isEmpty() && accuBeans.size() > 0) {
					for (FcAccuBalanceBean accubean : accuBeans) {
						//未计息且未预提
						if (CloseConstant.INTEREST_SIGN_N.equals(accubean.getInterestSign())
								&& CloseConstant.INTEREST_SIGN_N.equals(accubean.getPreSign())) {
							updateFlag = true;//存在未计息未预提的积数,可直接更新积数
							if (balance.compareTo(BigDecimal.ZERO) != 0) {
								accubean.addCumu(balance);
								//updateAccuCumu(accubean.getId(), accubean.getCumu());//更新未计息未预提的剩余积数
                                existAccuBalBeans.add(accubean);//更新未计息未预提的剩余积数
							}
							break;
						}
					}
				}
				if (!updateFlag) {
					//未计息未预提的积数
					FcAccuBalanceBean accubean = new FcAccuBalanceBean();
					accubean.setAccount(account);
					accubean.setBalanceDate(balanceDate);
					accubean.setCumu(balance);
					accubean.setAdjustCumu(BigDecimal.ZERO);
					accubean.setAdjustedCumu(BigDecimal.ZERO);
					accubean.setAgreeType(CloseConstant.DEFAULT_AGREE_TYPE);
                    accubean.setCurId(curId);
					accuBeans.add(accubean);
                    newAccuBalBeans.add(accubean);
					//insertFcAccuBanalce(accubean, CloseConstant.AGREE_SIGN_NO, curId, user,false,false);//非协定积数
				}
			} else {
				//协定账号积数处理
				processAgreeAccuBalance(account, balanceDate, dtlBean, accuMap.get(account), agreeBeans, user);
			}
		}
        batchUpdateAccuBalance(existAccuBalBeans);//批量更新积数
        batchInsertAccuBalance(newAccuBalBeans, user);//批量新增积数
        //积数生成完毕
        getEntityDao().flush();
        getEntityDao().clear();
		calcAdjustCumu(dayClose, user, false);//计算账户调整积数,非反结转
	}
	private List<AccuBalanceDtlBean> dealDate(FcDayCloseVO dayClose){
		//TODO 获取计息、账户类型不包含'05', '06', '08' ,'15','16',为该单位网点下的  账户列表
		String[] accTypeCode = {"05", "06", "08", "15", "16"};
		List<FcAccDTO> accountsList = iFcAccService.getFcAccByIdAndNotAccType(dayClose.getNetCode(), accTypeCode);
		if (EmptyUtils.isEmpty(accountsList) || accountsList.size() < 1) {
			return null;
		}
		Map<String, FcAccDTO> accountMap = accountsList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
		List<String> accounts = accountsList.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
		List<AccuBalanceDtlBean> dtlBeans = balanceDao.queryCumuDtl(dayClose, accounts);//获取账户每日余额
		if (!EmptyUtils.isEmpty(dtlBeans) && dtlBeans.size() > 0) {
			dtlBeans.forEach(a -> {
				FcAccDTO fcAccDTO = accountMap.get(a.getAccount());
				a.setAccType(fcAccDTO.getAccType());
				a.setCurId(fcAccDTO.getCurId());
				a.setAccStartDate(fcAccDTO.getAccStartDate());
			});
		}
		return dtlBeans;
	}
	/**
	 * <br>
	 * <b>功能：</b> 计算账户调整积数 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/15 <br>
	 * @param  reOverFlag : 反结转标识，还原调整额度
	 */
	private void calcAdjustCumu(FcDayCloseVO dayClose, ICurrentUser user, boolean reOverFlag) {
		//查询调整积数
		List<AccAdjustDtlBean> accAdjustDtlBeans = queryAdjustBookDtl(dayClose.getNetCode(), dayClose.getCloseDate());
		if (CollectionUtil.isEmpty(accAdjustDtlBeans)) {
			return;
		}
		//计算起息日到记账日每天的累计调整积数
		Map<String, Map<String, AccAdjustDtlVO>> adjustMap = calcAccAdjust(accAdjustDtlBeans, reOverFlag);
		for (Map.Entry<String, Map<String, AccAdjustDtlVO>> entry : adjustMap.entrySet()) {
			String account = entry.getKey();
			Map<String, AccAdjustDtlVO> accAdjustMap = entry.getValue();//账户调整积数
			Date startDate = getAccAdjustStartDate(accAdjustMap);
			//查询起息日到记账日之间的每日余额
			Map<String, AccBalanceBean> accBalMap = queryAccDaysBalance(account, startDate, dayClose.getCloseDate());
			//循环调整账号分别计算是否需要调整
			for (Map.Entry<String, AccAdjustDtlVO> adjustEntry : accAdjustMap.entrySet()) {
				AccAdjustDtlVO adjustDtlBean = adjustEntry.getValue();
				BigDecimal adjustCumu = adjustDtlBean.getAdjustCumu();//调整积数
				Date tempDate = adjustDtlBean.getAdjustDate();//调整日期
				List<FcAgreeDepositDetailDTO> agreeBeans = checkAgreeDeposit(account, tempDate,true);
				boolean agreeSign = true;//协定标识
				if (EmptyUtils.isEmptyList(agreeBeans)) {
					agreeSign = false;
				}
				//更新累计调整积数
				List<FcAccuBalanceBean> accuBeans = queryAccountAccuBalance(account, tempDate);
				if (!agreeSign) {//非协定
					FcAccuBalanceBean unAaccuBean = null;
					for (FcAccuBalanceBean accuBean : accuBeans) {
						if (CloseConstant.INTEREST_SIGN_N.equals(accuBean.getInterestSign())
								&& CloseConstant.INTEREST_SIGN_N.equals(accuBean.getPreSign())) {
							unAaccuBean = accuBean;
							break;
						}
					}
					if (null != unAaccuBean) {
						//当天调整积数=当天调整积数+累计调整积数
						unAaccuBean.addAdjustCumu(adjustCumu);
						updateAccuAdjustCumu(unAaccuBean.getId(), unAaccuBean.getAdjustCumu());
					} else {
						//没有未计息未预提的积数
						FcAccuBalanceBean accubean = new FcAccuBalanceBean();
						accubean.setAccount(account);
						accubean.setBalanceDate(tempDate);//调整积数日期从记账起息日期开始
						accubean.setCumu(BigDecimal.ZERO);
						accubean.setAdjustCumu(adjustCumu);
						accubean.setAdjustedCumu(BigDecimal.ZERO);
						accubean.setAgreeType(CloseConstant.DEFAULT_AGREE_TYPE);//默认非协定
						accuBeans.add(accubean);
						insertFcAccuBanalce(accubean, CloseConstant.AGREE_SIGN_NO, adjustDtlBean.getCurId(), user,false,reOverFlag);//非协定积数
					}
				} else {
					adjustAgreeAccuBalance(adjustDtlBean, accBalMap, accuBeans, agreeBeans, user, reOverFlag);
				}
			}
			getEntityDao().flush();//刷新提交，多次调整能取到上次提交的记录
		}
	}
	/**
	 * <br>
	 * <b>功能：</b> 获取账户开始日到结束日之间的账户余额 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/15 <br>
	 */
	private Map<String, AccBalanceBean> queryAccDaysBalance(String account, Date startDate, Date endDate) {
		List<AccBalanceBean> accBalanceBeans = accBalanceDao.queryAccDaysBalance(account, startDate, endDate);
		if(EmptyUtils.isEmptyList(accBalanceBeans)){
			return new HashMap<>();
		}
		Map<String, AccBalanceBean> accBalMap = new HashMap<>();
		for(AccBalanceBean accBean: accBalanceBeans) {
			accBalMap.put(DateUtils.formatDate(accBean.getBalanceDate()), accBean);
		}
		return accBalMap;
	}
	/**
	 * <br>
	 * <b>功能：</b> 获取账户积数调整最小的起息日 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/15 <br>
	 */
	private Date getAccAdjustStartDate(Map<String, AccAdjustDtlVO> accAdjustMap) {
		Set<String> dateSet = accAdjustMap.keySet();
		Object[] dateArray = dateSet.toArray();
		Arrays.sort(dateArray);
		String startDate = (String) dateArray[0];
		return DateUtils.str2Date(startDate, new SimpleDateFormat("yyyy-MM-dd"));//取最小的起息日期
	}
	/**
	 * <br>
	 * <b>功能：</b> 计算每个账户每天从起息日期到记账日期的累计调整积数 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/15 <br>
	 */
	private Map<String, Map<String, AccAdjustDtlVO>> calcAccAdjust(List<AccAdjustDtlBean> accAdjustDtlBeans, boolean reOverFlag) {
		Map<String, Map<String, AccAdjustDtlVO>> adjustMap = new HashMap<>();
		for(AccAdjustDtlBean adjustBean : accAdjustDtlBeans) {
			String account = adjustBean.getAccount();
			Map<String, AccAdjustDtlVO> accAdjustMap = adjustMap.get(account);
			if (EmptyUtils.isEmpty(accAdjustMap)) {
				accAdjustMap = new HashMap<>();
				adjustMap.put(account, accAdjustMap);
			}
			//每笔明细计算账户每天的累计调整积数
			generateAccDayAdjust(adjustBean, accAdjustMap, reOverFlag);
		}
		return adjustMap;
	}
	/**
	 * <br>
	 * <b>功能：</b> 按照起息日期到记账日期计算账户累计调整积数 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/15 <br>
	 */
	private void generateAccDayAdjust(AccAdjustDtlBean adjustBean, Map<String, AccAdjustDtlVO> adjustMap, boolean reOverFlag) {
		BigDecimal adjustMoney = adjustBean.getMoney();//调整金额
		if (reOverFlag) {
			adjustMoney = adjustMoney.negate();//反结转为调整相反的额度
		}
		Date startDate = adjustBean.getInterestStartDate();//起息日期
		Date tallyDate = adjustBean.getTallyDate();//记账日期
		Date tempDate = startDate;
		while(tempDate.compareTo(tallyDate) < 0) {
			//计算每天的累计调整积数
			String tempDateStr = DateUtils.formatDate(tempDate);
			AccAdjustDtlVO adjustDtlVO = adjustMap.get(tempDateStr);
			if (null == adjustDtlVO) {
				adjustDtlVO = new AccAdjustDtlVO(adjustBean.getAccount(), adjustBean.getCurId(), tempDate,
						adjustBean.getBalanceWay());
				adjustDtlVO.setAdjustCumu(BigDecimal.ZERO);//0调整积数
				adjustMap.put(tempDateStr, adjustDtlVO);
			}
			adjustDtlVO.setAdjustCumu(adjustDtlVO.getAdjustCumu().add(adjustMoney));//调整积数累计
            tempDate = DateUtils.addDay(tempDate, 1);
		}
	}
	/**
	 * <br>
	 * <b>功能：</b> 从账户余额集合中获取账户当日余额 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/15 <br>
	 */
	private AccBalanceBean getAccBalance(String account, Date balanceDate, List<AccBalanceBean> accBalanceBeans, Date endDate) {
		if (!EmptyUtils.isEmptyList(accBalanceBeans)) {
			for (AccBalanceBean bean: accBalanceBeans) {
				if(DateUtils.dayDiffs(balanceDate, bean.getBalanceDate()) == 0) {//指定日期
					return bean;
				}
			}
		}
		accBalanceBeans = accBalanceDao.queryAccDaysBalance(account, balanceDate, endDate);
		return getFcAccBalance(account, balanceDate);
	}
	/**
	 * <br>
	 * <b>功能：</b> 根据指定日期获取账户当日余额 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/15 <br>
	 */
	public AccBalanceBean getFcAccBalance(String account, Date balanceDate) {
		List<AccBalanceBean> accBals = accBalanceDao.queryBalanceByAccDate(account, balanceDate);//获取余额
		if (EmptyUtils.isEmptyList(accBals)) {
			return null;
		}
		return accBals.get(0);//获取账户余额
	}
	/**
	 * 功能介绍 : 更新账户积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	private void updateAccuCumu(String id, BigDecimal cumu) {
		balanceDao.updateAccuCumu(id, cumu);
	}
	/**
	 * 功能介绍 : 更新账户当天累计调整积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	private void updateAccuAdjustedCumu(String id, BigDecimal adjustedCumu) {
		balanceDao.updateAccuAdjustedCumu(id, adjustedCumu);
	}
	/**
	 * 功能介绍 : 更新账户当天调整积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	private void updateAccuAdjustCumu(String id, BigDecimal adjustCumu) {
		balanceDao.updateAccuAdjustCumu(id, adjustCumu);
	}
	/**
	 * 功能介绍 : 生成账户积数信息。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	private void insertFcAccuBanalce(FcAccuBalanceBean accuBean, String agreeSign, String curId, ICurrentUser user,boolean isLast,boolean reOverFlag) {
		ClFcAccuBalance accuBalance = new ClFcAccuBalance();
		accuBalance.setBalanceDate(accuBean.getBalanceDate());
		accuBalance.setAccount(accuBean.getAccount());
		accuBalance.setAgreedType(Integer.valueOf(accuBean.getAgreeType()));//协定段
		accuBalance.setInterestSign(CloseConstant.INTEREST_SIGN_N);
		accuBalance.setPreSign(CloseConstant.INTEREST_SIGN_N);
		accuBalance.setCurCode(curId);
		List<ClFcAccuBalance> accuList = balanceDao.queryUnIstAccountAccu(accuBalance);
		if (EmptyUtils.isEmptyList(accuList)) {//不存在未计息的积数，则新增
			accuBalance.setCumu(accuBean.getCumu());
			if(isLast){
				accuBalance.setAdjustCumu(accuBean.getAdjustCumu());
			}else{
				accuBalance.setAdjustCumu(BigDecimal.ZERO);
			}
			if(lastAgreedType != 0 || accuBalance.getAgreedType() == 40){
				accuBalance.setAgreedType(lastAgreedType);
				lastAgreedType = 0;
			}
			accuBalance.setAgreedDepositSign(agreeSign);
			accuBalance.setAgreedDepositId(accuBean.getAgreeDepositId());//协定ID
			accuBalance.setCreateTime(DateUtils.getDate());
			accuBalance.setCreateBy(user.getLoginName());
			balanceDao.save(accuBalance);
			accuBean.setId(accuBalance.getId());
		} else {//存在未计息的积数，则更新账户积数值
			ClFcAccuBalance accBal = accuList.get(0);
			if (reOverFlag) {//资金池反日结
				accBal.setCumu(accBal.getCumu().add(accuBean.getCumu()));
				for (ClFcAccuBalance accuItem:accuList) {
					if (!accuItem.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y)) {//未计息
						if (accuBean.getAdjustCumu().compareTo(BigDecimal.ZERO) < 0) {
							if (accuItem.getCumu().compareTo(BigDecimal.ZERO) == 0 && accuBean.getAdjustCumu().compareTo(BigDecimal.ZERO) < 0) {//调增
								if (accuBean.getAdjustCumu().compareTo(accuItem.getAdjustCumu().negate()) == 0) {
									balanceDao.delete(accuItem.getId());
									accuBean.setId(accBal.getId());
									return;
								}else{
									accuItem.setAdjustCumu(accuBean.getAdjustCumu().add(accuItem.getAdjustCumu()));//要调整的积数减原有的积数
									balanceDao.update(accuItem);
								}
							} else {
								accBal.setAdjustCumu(accuBean.getAdjustCumu().add(accuItem.getAdjustCumu()));//要调整的积数减原有的积数
							}
						} else {
							if (accuItem.getCumu().compareTo(BigDecimal.ZERO) == 0 && accuBean.getAdjustCumu().compareTo(BigDecimal.ZERO) > 0) {//调减
								if (accuBean.getAdjustCumu().compareTo(accuItem.getAdjustCumu().negate()) == 0 || accuItem.getAdjustCumu().compareTo(BigDecimal.ZERO) < 0) {
									balanceDao.delete(accuItem.getId());
									accuBean.setId(accBal.getId());
									return;
								}else {
									//超出段生成了好几段，反日结的时候把调整积数把之前的正常积数减掉
									BigDecimal leftAdjust = BigDecimal.ZERO;
									if(accuList.size()>1){
										for (ClFcAccuBalance accu:accuList) {
											if(accu.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y)){//已计息
												leftAdjust = accu.getCumu().add(accu.getAdjustCumu()).add(leftAdjust);
											}
										}
										accuItem.setAdjustCumu(accuBean.getAdjustCumu().subtract(leftAdjust));
									}
								}
							} else {
								accBal.setAdjustCumu(accuBean.getAdjustCumu().subtract(accBal.getCumu()));
							}
						}
					}
				}
			}else {
				Boolean isNormalAccu = false;//计息后，反日结，记账后，再日结 生成的正常积数，没有调整
				//调增、调减 都是直接跟在后面
				if(accuList.size()>1){
					Collections.sort(accuList, new Comparator<ClFcAccuBalance>() {
						public int compare(ClFcAccuBalance o1, ClFcAccuBalance o2) {
							int i = o1.getCreateTime().compareTo(o2.getCreateTime());
							if(i==0){
								return o1.getBalanceDate().compareTo(o2.getBalanceDate());
							}
							return i;
						}
					});
					accBal = accuList.get(accuList.size()-1);
				}
				if (!accBal.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y)){//未计息
					accBal.setCumu(accBal.getCumu().add(accuBean.getCumu()));
					if (accuBean.getAdjustCumu().compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal total = BigDecimal.ZERO;
						//调增
						for (ClFcAccuBalance accuItem : accuList) {
							total = total.add(accuItem.getCumu().add(accuItem.getAdjustCumu()));
						}
						//如果之前进行了调减，当前段积数+调整积数 =0
						if (total.compareTo(BigDecimal.ZERO) == 0 && accuList.get(0).getCumu().compareTo(BigDecimal.ZERO) != 0){
							//如果当前调整积数 > 生成的正常积数
							if(accuBean.getAdjustCumu().compareTo(accBal.getCumu()) > 0){
								accBal.setAdjustCumu(accuBean.getAdjustCumu().subtract(accBal.getCumu()));
							}else{
								accBal.setAdjustCumu(accBal.getAdjustCumu().add(accuBean.getAdjustCumu()));
							}
						}else if (total.compareTo(accuBean.getAdjustCumu()) != 0) {//不相等
							accBal.setAdjustCumu(accuBean.getAdjustCumu().subtract(accBal.getCumu()));
						}
					} else {
						//调减
						accBal.setAdjustCumu(accuBean.getAdjustCumu().add(accBal.getAdjustCumu()));
					}
					//如果调减后积数和调整积数都等于0 删掉这条数据
					if(accBal.getCumu().compareTo(BigDecimal.ZERO) == 0 && accBal.getAdjustCumu().compareTo(BigDecimal.ZERO) ==0){
						balanceDao.delete(accBal.getId());
					}else {
						balanceDao.update(accBal);
					}
					accuBean.setId(accBal.getId());
				}else {
					if(accuBean.getAdjustCumu().equals(BigDecimal.ZERO) || accuBean.getAdjustCumu() == null) {//不是调整积数
						if (StringUtils.isNotEmpty(accBal.getAgreedDepositSign()) && !accBal.getAgreedDepositSign().equals(0)
								&& accBal.getCumu().compareTo(accuBean.getCumu()) == 0) {//是协定分段计息并且已计息
							return;
						}
						if(!accuBean.getAgreeType().equals(CloseConstant.AGREE_SIGN_NO) && StringUtils.isEmpty(accuBean.getAgreeDepositId())){//协定超出段
							if(accuList.size() > 1) {
								BigDecimal accu = BigDecimal.ZERO;
								for (ClFcAccuBalance a : accuList) {
									accu = accu.add(a.getCumu());
								}
								if(accuBean.getCumu().compareTo(accu) != 0) {
									accuBean.setCumu(accuBean.getCumu().subtract(accu));
								}else {
									return;
								}
							}else {
								accuBean.setCumu(accuBean.getCumu().subtract(accBal.getCumu()));
							}
						}
						isNormalAccu = true;
					}
				}
				if(isLast || accBal.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y)) {
					//如果没计息、没调整之前的协定段有超出的，不新增
					if((accBal.getCumu().add(accBal.getAdjustCumu())).compareTo(accuBean.getAdjustCumu()) == 0){
						return;
					}
					lastAgreedType = accBal.getAgreedType().intValue();
					Integer aType = accBal.getAgreedType().intValue() + 40;//积数表里就不会存在这条数据
					accuBean.setAgreeType(aType.toString());
					accuBean.setInterestSign("0");
					if(!isNormalAccu){
						accuBean.setCumu(BigDecimal.ZERO);
					}else{
						//是否协定正常积数变动
						if(StringUtils.isNotEmpty(accuBean.getAgreeDepositId())) {
							if (accBal.getCumu().compareTo(accuBean.getCumu()) != 0) {
								BigDecimal cumu = accuBean.getCumu().subtract(accBal.getCumu());
								accuBean.setCumu(cumu);
							}
						}
					}
					if (accuBean.getAdjustCumu().compareTo(BigDecimal.ZERO) > 0) {
						if (accBal.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y) && !isLast) {//是计息后的调整，但不是最后一笔
							BigDecimal total = BigDecimal.ZERO;
							for (ClFcAccuBalance accuItem : accuList) {
								total = total.add(accuItem.getCumu().add(accuItem.getAdjustCumu()));
							}
							if (total.compareTo(accuBean.getAdjustCumu()) != 0) {
								accuBean.setAdjustCumu(accuBean.getAdjustCumu().subtract((accBal.getCumu().add(accBal.getAdjustCumu()))));
							} else {
								lastAgreedType = 0;
								return;
							}
						} else if (accBal.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y) && isLast) {//是计息后的调整，是最后一笔
							accuBean.setAdjustCumu(accuBean.getAdjustCumu().subtract(accBal.getCumu().add(accBal.getAdjustCumu())));
						} else {
							accuBean.setAdjustCumu(accuBean.getAdjustCumu().subtract(accBal.getAdjustCumu()));
						}
					}else{//调减
						accuBean.setAdjustCumu(accuBean.getAdjustCumu());
					}
					insertFcAccuBanalce(accuBean, agreeSign, curId, user,true,reOverFlag);
				}
			}
		}
	}
	/**
	 * 功能介绍: 查询账户是否协定账户及协定区间。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	private List<FcAgreeDepositDetailDTO> checkAgreeDeposit(String account, Date balanceDate,boolean isAdjust) {
		//账户协定区间查询，是否协定账号，获取协定区间
        return getAgreeDepositDetail(account, balanceDate,isAdjust);//获取协定明细区间
	}
	/**
	 * 功能介绍  : 产生协定积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	private void processAgreeAccuBalance(String account, Date balanceDate, AccuBalanceDtlBean dtlBean,
         List<FcAccuBalanceBean> accuBeans, List<FcAgreeDepositDetailDTO> agreeDetailDTOS, ICurrentUser user) {
        if (EmptyUtils.isEmptyList(accuBeans)) {
            accuBeans = new ArrayList<>();
        }
        BigDecimal accBalance = dtlBean.getBalance();//账户余额
		String curId = dtlBean.getCurId();//账户币别
		//是否转入协定账号
		FcAgreedDepositDTO isChangeAcc = iFcAgreedDepositService.getFcAgreedDepositByBlnctrAcc(account,balanceDate);
		if ("1".equals(isChangeAcc.getIsAgreedDepositAcc())){
			saveAgreeAccCumu(account,curId,isChangeAcc,accBalance,balanceDate,user);
			return;
		}
		//1、取已计息的协定积数
        Map<String, BigDecimal> agreeIstMap = getInterestAgreeAccuBalance(accuBeans);
        BigDecimal istAccu = agreeIstMap.get(CloseConstant.AGREE_SIGN_NO);//协定前
        if (null == istAccu) {
            istAccu = BigDecimal.ZERO;
        }
        FcAgreeDepositDetailDTO firstDetailDTO = agreeDetailDTOS.get(0);//协定第一段
        BigDecimal agreeMinMoney = firstDetailDTO.getMinMoney();//协定起始金额
		//生成协定起始剩余的正常积数
		BigDecimal normalAccu = agreeMinMoney.subtract(istAccu);//正常积数
		if(normalAccu.compareTo(BigDecimal.ZERO) > 0) {//未计息积数不等于协定起始段
			//以账户余额减去已计息后为正常积数,保存正常计息部分积数
			FcAccuBalanceBean accuBean = new FcAccuBalanceBean();
            accuBean.setAccount(account);
            accuBean.setBalanceDate(balanceDate);
			if(accBalance.compareTo(BigDecimal.ZERO) != 0 && accBalance.compareTo(normalAccu) > 0) {
                accuBean.setCumu(normalAccu);//正常未计息积数 
			}else {
                accuBean.setCumu(accBalance);//正常未计息积数
			}
            accuBean.setAdjustCumu(BigDecimal.ZERO);
            accuBean.setAdjustedCumu(BigDecimal.ZERO);
            accuBean.setAgreeType(CloseConstant.DEFAULT_AGREE_TYPE);//默认协定段
			accuBeans.add(accuBean);
			insertFcAccuBanalce(accuBean, CloseConstant.AGREE_SIGN_NO, curId, user,false,false);//非协定积数
		}else{
			if(EmptyUtils.isNotEmpty(accuBeans) && accuBeans.size() > 0){
                FcAccuBalanceBean accu = accuBeans.get(accuBeans.size()-1);
                if(dtlBean.getBalance().compareTo(accu.getCumu()) < 0 && accu.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y)){
                    FcAccuBalanceBean accuBean = new FcAccuBalanceBean();
                    BeanUtils.copyProperties(accu,accuBean);
                    accuBean.setCumu(dtlBean.getBalance().subtract(accuBean.getCumu()));
                    accuBean.setInterestSign(CloseConstant.INTEREST_SIGN_N);
                    accuBean.setPreSign(CloseConstant.INTEREST_SIGN_N);
                    insertFcAccuBanalce(accuBean, CloseConstant.AGREE_SIGN_NO, curId, user,false,false);//非协定积数
                }
            }
        }
		if(accBalance.compareTo(agreeMinMoney) >= 0) {//余额大于等于协定第一段的金额
			BigDecimal leftMoney = accBalance.subtract(agreeMinMoney);
			Integer maxAgreeType = Integer.valueOf(agreeDetailDTOS.size() + 1);
			//不转协定账号-正常协定
			for (int i = 0, length = agreeDetailDTOS.size(); i < length; i++) {
				FcAgreeDepositDetailDTO detailDTO = agreeDetailDTOS.get(i);
				String lineNo = detailDTO.getLineNo().toString();//协定段
				BigDecimal istAccuBal = agreeIstMap.get(lineNo);//协定段已计息积数
				if (null == istAccuBal) {
					istAccu = BigDecimal.ZERO;
				}
				BigDecimal minMoney = detailDTO.getMinMoney();//下限金额
				BigDecimal maxMoney = detailDTO.getMaxMoney();//上限金额
				BigDecimal agreeMoney = BigDecimal.ZERO;//区间金额
				BigDecimal aCumu = BigDecimal.ZERO;
				if (null == maxMoney) {//最后一段
					agreeMoney = leftMoney;//区间金额
					leftMoney = BigDecimal.ZERO;
				} else {
					agreeMoney = maxMoney.subtract(minMoney);//区间金额
					if (accBalance.compareTo(maxMoney) >= 0) {
						leftMoney = accBalance.subtract(maxMoney);
					} else {
						agreeMoney = leftMoney;//剩余金额全部作为积数,也可以=accBalance.subtract(minMoney)
						leftMoney = BigDecimal.ZERO;
					}
				}
				aCumu = agreeMoney.subtract(istAccu);
				if (aCumu.compareTo(BigDecimal.ZERO) != 0) {//未计息积数不等于协定起始段
					//以账户余额减去已计息后为正常积数,保存正常计息部分积数
					FcAccuBalanceBean accubean = new FcAccuBalanceBean();
					accubean.setAccount(account);
					accubean.setBalanceDate(balanceDate);
					accubean.setCumu(aCumu);//正常未计息积数
					accubean.setAdjustCumu(BigDecimal.ZERO);
					accubean.setAdjustedCumu(BigDecimal.ZERO);
					accubean.setAgreeType(lineNo);//协定段
					accubean.setAgreeDepositId(detailDTO.getAgreeDepositId());
					accuBeans.add(accubean);
					insertFcAccuBanalce(accubean, CloseConstant.AGREE_SIGN_YES, curId, user, false, false);//协定积数
				}else {
					//协定金额为0 判断是否存在已计息协定积数
					if(accuBeans.size()>1) {
						for (FcAccuBalanceBean accu : accuBeans) {
							if (StringUtils.isNotEmpty(accu.getAgreedType()) && accu.getAgreedType().equals(agreeDetailDTOS.get(i).getLineNo()) && accu.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y)) {
								FcAccuBalanceBean accuBean = new FcAccuBalanceBean();
								BeanUtils.copyProperties(accu, accuBean);
								accuBean.setCumu(BigDecimal.ZERO);
								accuBean.setInterestSign(CloseConstant.INTEREST_SIGN_N);
								accuBean.setPreSign(CloseConstant.INTEREST_SIGN_N);
								accuBean.setAgreeType(accu.getAgreedType().toString());
								accuBean.setAgreeDepositId(accu.getAgreedDepositId());
								insertFcAccuBanalce(accuBean, CloseConstant.AGREE_SIGN_YES, curId, user, false, false);//协定积数
							}
						}
					}
				}
			}
			if (leftMoney.compareTo(BigDecimal.ZERO) != 0) {
				//以账户余额减去已计息后为正常积数,保存正常计息部分积数
				FcAccuBalanceBean accubean = new FcAccuBalanceBean();
				accubean.setAccount(account);
				accubean.setBalanceDate(balanceDate);
				accubean.setCumu(leftMoney);//正常未计息积数
				accubean.setAdjustCumu(BigDecimal.ZERO);
				accubean.setAdjustedCumu(BigDecimal.ZERO);
				accubean.setAgreeType(maxAgreeType.toString());//协定段 + 1
				accuBeans.add(accubean);
				insertFcAccuBanalce(accubean, CloseConstant.AGREE_SIGN_NO, curId, user,false,false);//非协定积数
			}else {
				//协定金额为0 判断是否存在已计息协定积数
				if(accuBeans.size()>1) {
					for (FcAccuBalanceBean accu : accuBeans) {
						if (StringUtils.isNotEmpty(accu.getAgreedType()) && accu.getAgreedType()==maxAgreeType && accu.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y)) {
							FcAccuBalanceBean accuBean = new FcAccuBalanceBean();
							BeanUtils.copyProperties(accu, accuBean);
							accuBean.setCumu(BigDecimal.ZERO);
							accuBean.setInterestSign(CloseConstant.INTEREST_SIGN_N);
							accuBean.setPreSign(CloseConstant.INTEREST_SIGN_N);
							accuBean.setAgreeType(accu.getAgreedType().toString());
							accuBean.setAgreeDepositId(accu.getAgreedDepositId());
							insertFcAccuBanalce(accuBean, CloseConstant.AGREE_SIGN_YES, curId, user, false, false);//协定积数
						}
					}
				}
			}
		}else{
			if(accuBeans.size()>1) {
				for (FcAccuBalanceBean accu : accuBeans) {
					for (int i = 0, length = agreeDetailDTOS.size(); i < length; i++) {
						if(accu.getAgreedDepositId()!=null){
							if(accu.getAgreedType().equals(agreeDetailDTOS.get(i).getLineNo()) && accu.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y)){
								FcAccuBalanceBean accuBean = new FcAccuBalanceBean();
								BeanUtils.copyProperties(accu,accuBean);
								accuBean.setCumu(BigDecimal.ZERO);
								accuBean.setInterestSign(CloseConstant.INTEREST_SIGN_N);
								accuBean.setPreSign(CloseConstant.INTEREST_SIGN_N);
								accuBean.setAgreeType(accu.getAgreedType().toString());
								accuBean.setAgreeDepositId(accu.getAgreedDepositId());
								insertFcAccuBanalce(accuBean, CloseConstant.AGREE_SIGN_YES, curId, user,false,false);//协定积数
							}
						}
					}
				}
			}
		}
	}
	/**
	 * <br>
	 * <b>功能：</b> 获取已计息或已预提的积数 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/14 <br>
	 */
    private Map<String, BigDecimal> getInterestAgreeAccuBalance(List<FcAccuBalanceBean> accuBeans) {
        if (EmptyUtils.isEmptyList(accuBeans)) {
            return new HashMap<>();
        }
        List<FcAccuBalanceBean> beans = new ArrayList<>();
        for (FcAccuBalanceBean accuBean : accuBeans) {
            if (EmptyUtils.isEmpty(accuBean.getAgreeDepositId())) {
                accuBean.setAgreeType(CloseConstant.AGREE_SIGN_NO);//正常段
            }
            if (CommonConstant.YES.equals(accuBean.getInterestSign())
                    || CommonConstant.YES.equals(accuBean.getPreSign())) {
				beans.add(accuBean);
            }
        }
        //统计各协定段已计息金额
        Map<String, BigDecimal> agreeInterestMap = new HashMap<>();
        for (FcAccuBalanceBean accuBean : beans) {
            BigDecimal interestMoney = agreeInterestMap.get(accuBean.getAgreeType());
            if (null == interestMoney) {
                interestMoney = BigDecimal.ZERO;//默认0
            }
            interestMoney = interestMoney.add(accuBean.getCumu());
            agreeInterestMap.put(accuBean.getAgreeType(), interestMoney);//已计息金额
        }
        return agreeInterestMap;
    }
	/**
	 * <br>
	 * <b>功能：</b> 获取已计息或已预提的积数 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/14 <br>
	 */
	private List<FcAccuBalanceBean> getAgreeAccuBalByAgreeType(List<FcAccuBalanceBean> accuBeans, String agreeType) {
		if (EmptyUtils.isEmptyList(accuBeans)) {
			return new ArrayList<>();
		}
		List<FcAccuBalanceBean> beans = new ArrayList<>();
		for (FcAccuBalanceBean accuBean : accuBeans) {
			if (EmptyUtils.isEmpty(accuBean.getAgreeDepositId())) {
				accuBean.setAgreeType(CloseConstant.AGREE_SIGN_NO);//正常段
			}
			if (accuBean.getAgreeType().equals(agreeType)) {
				beans.add(accuBean);
			}
		}
		return beans;
	}
	/**
	 * <br>
	 * <b>功能：</b> 获取账户协定详情 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/14 <br>
	 */
	public List<FcAgreeDepositDetailDTO> getAgreeDepositDetail(String account, Date balanceDate,boolean isAdjust) {
		return agreedDetailService.getFcAgreeDepositDetail(account, balanceDate,isAdjust);
	}
	/**
	 * 功能介绍 : 查询账号是否协定存款。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	private boolean checkAccountAgreeDeposit(String account, Date balanceDate,boolean isAdjust) {
		List<FcAgreeDepositDetailDTO> agreeBeans = checkAgreeDeposit(account, balanceDate,isAdjust);
		if (EmptyUtils.isEmptyList(agreeBeans)) {
			return false;//非协定
		}
		return true;//协定
	}
	/**
	 * <br>
	 * <b>功能：</b> 获取产生协定积数最大区间 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/15 <br>
	 */
	private String getAccMaxAgreeType(List<FcAccuBalanceBean> accuBeans) {
		if(EmptyUtils.isEmptyList(accuBeans)){
			return CloseConstant.AGREE_SIGN_NO;//不满足协定分段其实金额
		}
		String defaultAgreeType = CloseConstant.AGREE_SIGN_NO;
		for(FcAccuBalanceBean accuBean : accuBeans) {
			String agreeType = accuBean.getAgreeType();
			if (EmptyUtils.isEmpty(agreeType)) {
				agreeType = CloseConstant.AGREE_SIGN_NO;
			}
			if (Integer.valueOf(agreeType).compareTo(Integer.valueOf(defaultAgreeType)) > 0) {
				defaultAgreeType = agreeType;
			}
		}
		return defaultAgreeType;
	}
	/**
	 * <br>
	 * <b>功能：</b> 更新协定账户调整积数 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/15 <br>
	 */
	private void updateSaveAccAdjustCumu(AccAdjustDtlVO adjustDtlVO, List<FcAccuBalanceBean> accuBeans,String agreeSign, String agreeType,
										 BigDecimal adjustCumu, String depositId, ICurrentUser user,boolean isLast,boolean reOverFlag) {
		if(adjustCumu.compareTo(BigDecimal.ZERO) == 0) {//本次调整额度不等于0才保存或更新
			return;
		}
		if (!EmptyUtils.isEmptyList(accuBeans)) {
			for(FcAccuBalanceBean accuBean : accuBeans) {
				//存在未计息的积数
				if (CloseConstant.INTEREST_SIGN_N.equals(accuBean.getInterestSign())
						&& !CloseConstant.INTEREST_SIGN_N.equals(accuBean.getPreSign())) {
					updateAccuAdjustCumu(accuBean.getId(), adjustCumu);//直接更新调整积数
					return;
				}
			}
		}
		//新增一条调整积数
		FcAccuBalanceBean accubean = new FcAccuBalanceBean();
		accubean.setAccount(adjustDtlVO.getAccount());
		accubean.setBalanceDate(adjustDtlVO.getAdjustDate());
		accubean.setCumu(BigDecimal.ZERO);//正常未计息积数
		accubean.setAdjustCumu(adjustCumu);//调整积数
		accubean.setAdjustedCumu(BigDecimal.ZERO);
		accubean.setAgreeType(agreeType);//协定段
		accubean.setAgreeDepositId(depositId);
		accuBeans.add(accubean);
		insertFcAccuBanalce(accubean, agreeSign, adjustDtlVO.getCurId(), user,isLast,reOverFlag);//协定积数
	}
	/**
	 * 功能介绍  : 调整协定存款指定日期的积数。<br>
	 * <b>日期：</b>2019年5月15日<br>
	 * @version 1.0
	 * @author liyh
	 */
	private void adjustAgreeAccuBalance(AccAdjustDtlVO adjustDtlVO, Map<String, AccBalanceBean> accBalMap,
										List<FcAccuBalanceBean> accuBeans, List<FcAgreeDepositDetailDTO> agreeDetailDTOS, ICurrentUser user,boolean reOverFlag) {
		BigDecimal adjustCumu = adjustDtlVO.getAdjustCumu();//当天需要调整的总积数
		BigDecimal leftAdjustCumu = adjustCumu;
		Date adjustDate = adjustDtlVO.getAdjustDate();//调整日期
		AccBalanceBean accBalBean = accBalMap.get(DateUtils.formatDate(adjustDate));//获取调整日期的账户余额
		Boolean isLast1 = false;
		//是否转入协定账号
		FcAgreedDepositDTO isChangeAcc = iFcAgreedDepositService.getFcAgreedDepositByBlnctrAcc(accBalBean.getAccount(),adjustDate);
		if ("1".equals(isChangeAcc.getIsAgreedDepositAcc())){
			updateSaveAgreeAccAdjustCumu(adjustDtlVO,adjustCumu,accBalBean.getBalance(),isChangeAcc.getRetainedBal(),isChangeAcc.getAgrAcc(),user.getLoginName());
			return;
		}
		//调增
		if (adjustCumu.compareTo(BigDecimal.ZERO) > 0) {
			//1、取账户所有的协定积数
			List<FcAccuBalanceBean> firstAccuBalList = getAgreeAccuBalByAgreeType(accuBeans, CloseConstant.DEFAULT_AGREE_TYPE);
			FcAgreeDepositDetailDTO firstDetailDTO = agreeDetailDTOS.get(0);//协定第一段
			BigDecimal agreeMinMoney = firstDetailDTO.getMinMoney();//协定起始金额
			BigDecimal fistAccAdjustCumu = BigDecimal.ZERO;//本次调整额度
			//如果账户余额+调整后的余额超过协定起始余额
			if (adjustCumu.add(accBalBean.getBalance()).compareTo(agreeMinMoney) > 0) {
				//取协定前所有积数和
				BigDecimal existCumu = BigDecimal.ZERO;//已生成的积数
				for(FcAccuBalanceBean  accuBalBean : firstAccuBalList) {
					existCumu = existCumu.add(accuBalBean.getCumu().add(accuBalBean.getAdjustCumu()));//累加已生成的积数
				}
				fistAccAdjustCumu = agreeMinMoney.subtract(existCumu);//本次调整额度=协定起始值-已生成的积数
				leftAdjustCumu = leftAdjustCumu.subtract(fistAccAdjustCumu);//剩余可调整额度
			} else {
				fistAccAdjustCumu = adjustCumu;//最多调整额度
				leftAdjustCumu = BigDecimal.ZERO;//剩余调整额度为0
			}
			if(fistAccAdjustCumu.compareTo(BigDecimal.ZERO) > 0) {//本次调整额度不等于才保存或更新
				//如果存在未计息的积数，则更新调整积数，否则新增一条调整积数数据
				updateSaveAccAdjustCumu(adjustDtlVO, firstAccuBalList, CloseConstant.AGREE_SIGN_NO,
                        CloseConstant.DEFAULT_AGREE_TYPE, fistAccAdjustCumu, null, user,false,reOverFlag);
			}
			if (leftAdjustCumu.compareTo(BigDecimal.ZERO) <= 0) {
				return;
			}
			for (int i = 0, length = agreeDetailDTOS.size(); i < length; i++) {
				if (leftAdjustCumu.compareTo(BigDecimal.ZERO) <= 0) {
					break;
				}
				FcAgreeDepositDetailDTO detailDTO = agreeDetailDTOS.get(i);
				String agreeType = detailDTO.getLineNo().toString();//协定段
				BigDecimal accAdjustCumu = BigDecimal.ZERO;//本次调整额度
				//取本次协定段所有积数和
				List<FcAccuBalanceBean> accuBalList = getAgreeAccuBalByAgreeType(accuBeans, agreeType);
				BigDecimal minMoney = detailDTO.getMinMoney();//下限金额
				BigDecimal maxMoney = detailDTO.getMaxMoney();//上限金额
				if(null == maxMoney) {
					//作为最大的协定段
					accAdjustCumu = leftAdjustCumu;
					leftAdjustCumu = BigDecimal.ZERO;
					if(accAdjustCumu.compareTo(BigDecimal.ZERO) > 0) {//本次调整额度不等于才保存或更新
						//如果存在未计息的积数，则更新调整积数，否则新增一条调整积数数据
						updateSaveAccAdjustCumu(adjustDtlVO, accuBalList, CloseConstant.AGREE_SIGN_YES, agreeType, accAdjustCumu, detailDTO.getAgreeDepositId(), user,true,reOverFlag);
					}
					break;
				}
				BigDecimal agreeMoney = maxMoney.subtract(minMoney);//区间金额
                BigDecimal money = BigDecimal.ZERO;
                if(reOverFlag){//反日结的时候根据协定段的最小值对比，不然生成的积数不对
                    money = minMoney;
                }else {
                    money = maxMoney;
                }
				if (accBalBean.getBalance().add(adjustCumu).compareTo(money) > 0) {//大于本协定段的最大值
					//取协定前所有积数和
					BigDecimal existCumu = BigDecimal.ZERO;//已生成的积数
					for(FcAccuBalanceBean accuBalBean : accuBalList) {
						existCumu = existCumu.add(accuBalBean.getCumu().add(accuBalBean.getAdjustCumu()));//累加已生成的积数
					}
					accAdjustCumu = agreeMoney.subtract(existCumu);//本次调整额度=区间段-已生成的积数
					leftAdjustCumu = leftAdjustCumu.subtract(accAdjustCumu);//剩余可调整额度
				} else {
					accAdjustCumu = leftAdjustCumu;//最多调整额度
					leftAdjustCumu = BigDecimal.ZERO;//剩余调整额度为0
				}
				if(accAdjustCumu.compareTo(BigDecimal.ZERO) > 0) {//本次调整额度不等于才保存或更新
					//如果存在未计息的积数，则更新调整积数，否则新增一条调整积数数据
					Boolean isLast = false;
					if(i==firstAccuBalList.size()-1 || isLast1 == true){
						isLast = true;
						isLast1 = true;
					}
					updateSaveAccAdjustCumu(adjustDtlVO, accuBalList, CloseConstant.AGREE_SIGN_YES, agreeType, accAdjustCumu, detailDTO.getAgreeDepositId(), user,isLast,reOverFlag);
				}
			}
			if (leftAdjustCumu.compareTo(BigDecimal.ZERO) > 0) {
				Integer maxAgreeType = Integer.valueOf(agreeDetailDTOS.size() + 1);//最大段
				//如果协定调整完后还存在剩余未调整的额度，则更新或新增协定前正常的调整积数
				updateSaveAccAdjustCumu(adjustDtlVO, firstAccuBalList, CloseConstant.AGREE_SIGN_NO, maxAgreeType.toString(), leftAdjustCumu, null, user,true,reOverFlag);
			}
			isLast1 = false;
		} else {//调减
			//先判断是否大于最大区间值
			FcAgreeDepositDetailDTO lastDetailDTO = agreeDetailDTOS.get(agreeDetailDTOS.size() - 1);//协定最后一段
			BigDecimal agreeMaxMoney = lastDetailDTO.getMaxMoney();//协定最大金额
			BigDecimal fistAccAdjustCumu = BigDecimal.ZERO;//本次调整额度
			Integer maxAgreeType = Integer.valueOf(lastDetailDTO.getLineNo() + 1);
			//1、取账户所有正常的协定积数
			List<FcAccuBalanceBean> firstAccuBalList = null;
			BigDecimal existCumu = BigDecimal.ZERO;//已生成的积数
//			if(!reOverFlag) {
//				firstAccuBalList = getAgreeAccuBalByAgreeType(accuBeans, maxAgreeType.toString());
//				existCumu = accBalBean.getBalance();
//			}else{
			firstAccuBalList = getAgreeAccuBalByAgreeType(accuBeans, CloseConstant.DEFAULT_AGREE_TYPE);
			for(FcAccuBalanceBean  accuBalBean : firstAccuBalList) {
				existCumu = existCumu.add(accuBalBean.getCumu().add(accuBalBean.getAdjustCumu()));//累加已生成的积数
			}
//			}
			if (agreeMaxMoney != null) {
				if (existCumu.compareTo(agreeMaxMoney) > 0) {//accBalBean.getBalance()
					//如果账户余额+调整后的余额超过协定最大余额
					if (adjustCumu.add(existCumu).compareTo(agreeMaxMoney) > 0) {//accBalBean.getBalance()
						fistAccAdjustCumu = adjustCumu;//最多调整额度
						leftAdjustCumu = BigDecimal.ZERO;//剩余调整额度为0
					} else {
						fistAccAdjustCumu = agreeMaxMoney.subtract(existCumu);//本次调整额度=最大值 - 账户余额accBalBean.getBalance()
						//取协定前所有积数和
//						if(!reOverFlag) {
//							leftAdjustCumu = leftAdjustCumu.add(fistAccAdjustCumu);//剩余可调整额度
//						}else{
						leftAdjustCumu = leftAdjustCumu.subtract(fistAccAdjustCumu);//剩余可调整额度
						existCumu = existCumu.add(fistAccAdjustCumu);
//						}
					}
					if (fistAccAdjustCumu.compareTo(BigDecimal.ZERO) < 0) {//本次调整额度不等于才保存或更新
						//如果存在未计息的积数，则更新调整积数，否则新增一条调整积数数据
						updateSaveAccAdjustCumu(adjustDtlVO, firstAccuBalList, CloseConstant.AGREE_SIGN_NO, maxAgreeType.toString(),
								fistAccAdjustCumu, null, user,false,reOverFlag);
					}
				}
			}
			if (leftAdjustCumu.compareTo(BigDecimal.ZERO) >= 0) {
				return;
			}
			BigDecimal leftAdjustBal = existCumu;//剩余可调整的账户余额accBalBean.getBalance()
			//按协定区间倒序取值
			for (int i = agreeDetailDTOS.size() - 1; i >= 0; i--) {
				if (leftAdjustCumu.compareTo(BigDecimal.ZERO) >= 0) {
					break;
				}
				FcAgreeDepositDetailDTO detailDTO = agreeDetailDTOS.get(i);
				String agreeType = detailDTO.getLineNo().toString();//协定段
				BigDecimal accAdjustCumu = BigDecimal.ZERO;//本次调整额度
				//取本次协定段所有积数和
				List<FcAccuBalanceBean> accuBalList = getAgreeAccuBalByAgreeType(accuBeans, agreeType);
				BigDecimal minMoney = detailDTO.getMinMoney();//下限金额
				BigDecimal maxMoney = detailDTO.getMaxMoney();//上限金额
				if (existCumu.add(adjustCumu).compareTo(minMoney) > 0) {//大于本协定段的最小值accBalBean.getBalance()
					if(null != maxMoney) {
						if(existCumu.add(adjustCumu).compareTo(maxMoney) > 0) {//accBalBean.getBalance()
							continue;
						} else {
							Boolean term = false;
							if(!reOverFlag){
								term = accBalBean.getBalance().compareTo(maxMoney) > 0;
							}else {
								term = existCumu.compareTo(maxMoney) < 0;
							}
							if(term) {//调整前大于最大值，现在小于，则用最大值减去本次调整值accBalBean.getBalance()
								accAdjustCumu = (accBalBean.getBalance().add(adjustCumu)).subtract(maxMoney);//existCumu.add(adjustCumu).subtract(maxMoney);//循序换了一下accBalBean.getBalance()
								leftAdjustCumu = BigDecimal.ZERO;//剩余调整为0
							} else {
								accAdjustCumu = leftAdjustCumu;
								leftAdjustCumu = BigDecimal.ZERO;//剩余调整为0
							}
						}
					} else {
						accAdjustCumu = leftAdjustCumu;//全部调整
						leftAdjustCumu = BigDecimal.ZERO;//剩余调整为0
					}
				} else {
					if(existCumu.compareTo(minMoney) <= 0) {//下一段去调整accBalBean.getBalance()
						continue;
					} else {
						accAdjustCumu = minMoney.subtract(leftAdjustBal);//本次调整值为超过部分,不能超过区间值
						//如果调减的剩余积数在当前段的范围内
						if(accAdjustCumu.negate().compareTo(leftAdjustCumu.negate()) > 0){
							accAdjustCumu = leftAdjustCumu;
							leftAdjustCumu = BigDecimal.ZERO;
						}else {
							leftAdjustBal = minMoney;//下次剩余可调整的余额不能超过协定的最小值
							leftAdjustCumu = leftAdjustCumu.subtract(accAdjustCumu);//剩余调整减去已调整额度
						}
					}
				}
				if(accAdjustCumu.compareTo(BigDecimal.ZERO) != 0) {//本次调整额度不等于才保存或更新
					//如果存在未计息的积数，则更新调整积数，否则新增一条调整积数数据
					updateSaveAccAdjustCumu(adjustDtlVO, accuBalList, CloseConstant.AGREE_SIGN_YES, agreeType, accAdjustCumu, detailDTO.getAgreeDepositId(), user,false,reOverFlag);
				}
			}
			if (leftAdjustCumu.compareTo(BigDecimal.ZERO) < 0) {
				//如果协定调整完后还存在剩余未调整的额度，则更新或新增协定前正常的调整积数
				updateSaveAccAdjustCumu(adjustDtlVO, firstAccuBalList, CloseConstant.AGREE_SIGN_NO, CloseConstant.DEFAULT_AGREE_TYPE, leftAdjustCumu, null, user,false,reOverFlag);
			}
		}
	}
	/**
	 * 功能介绍 : 恢复已调整积数累计。<br>
	 * <b>日期：</b>2017年7月14日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void recoverAdjAccuBalance(FcDayCloseVO dayClose, ICurrentUser user,boolean isPool) {
		calcAdjustCumu(dayClose, user, true);//反结转调整积数
        /*List<AccAdjustDtlBean> adjustDtlBeans = queryAdjustBookDtl(dayClose.getNetCode(), dayClose.getCloseDate());
		if (null == adjustDtlBeans) {
			return;
		}
		
		//循环调整账号
		for (AccAdjustDtlBean adjustDtlBean : adjustDtlBeans) {
			String account = adjustDtlBean.getAccount();
			String curId = adjustDtlBean.getCurId();
			BigDecimal adjustCumu = adjustDtlBean.getAdjustCumu();
			
			Date tempDate = adjustDtlBean.getInterestStartDate();
			boolean agreeSign = checkAccountAgreeDeposit(account, tempDate);//协定标识
			
			while(tempDate.compareTo(adjustDtlBean.getTallyDate()) < 0) {
				if (!agreeSign) {
					//更新累计调整积数
					List<FcAccuBalanceBean> accuBeans = queryAccountAccuBalance(account, tempDate);
					FcAccuBalanceBean unAaccuBean = null;
					if (!EmptyUtils.isEmptyList(accuBeans)) {
						for (FcAccuBalanceBean accuBean : accuBeans) {
							if (CloseConstant.INTEREST_SIGN_N.equals(accuBean.getInterestSign())
									&& CloseConstant.INTEREST_SIGN_N.equals(accuBean.getPreSign())) {
								unAaccuBean = accuBean;
								break;
							}
						}
					}
					if (null != unAaccuBean) {
						//当天调整积数=当天调整积数-累计调整积数
						unAaccuBean.addAdjustCumu(adjustCumu.negate());//还原调整积数
						updateAccuAdjustCumu(unAaccuBean.getId(), unAaccuBean.getAdjustCumu());
					} else {
						//没有未计息未预提的积数
						FcAccuBalanceBean accubean = new FcAccuBalanceBean();
						accubean.setAccount(account);
						accubean.setBalanceDate(dayClose.getCloseDate());
						accubean.setCumu(BigDecimal.ZERO);
						accubean.setAdjustCumu(adjustCumu.negate());
						accubean.setAdjustedCumu(BigDecimal.ZERO);
						accubean.setAgreeType(CloseConstant.DEFAULT_AGREE_TYPE);//默认积数类型
						accuBeans.add(accubean);
						insertFcAccuBanalce(accubean, CloseConstant.AGREE_SIGN_NO, adjustDtlBean.getCurId(), user);//非协定积数
					}
				} else {
					adjustAgreeAccuBalance(adjustDtlBean);
				}
				tempDate = DateUtils.addDay(tempDate, 1);//循环递增
			}
		}*/
	}
	/**保存活期转入协定账号积数*/
	public void saveAgreeAccCumu(String account,String curId,FcAgreedDepositDTO agreeDto,BigDecimal accBalance,Date balanceDate,ICurrentUser user) {
		FcAccuBalanceBean accubean = new FcAccuBalanceBean();
		accubean.setBalanceDate(balanceDate);
		accubean.setAdjustCumu(BigDecimal.ZERO);
		accubean.setAdjustedCumu(BigDecimal.ZERO);
		accubean.setInterestSign(CloseConstant.INTEREST_SIGN_N);
		accubean.setPreSign(CloseConstant.INTEREST_SIGN_N);
		accubean.setAgreeType("0");
		accubean.setAgreeDepositId("");
		//转入协定账号 把超过留存的金额存到协定账号里
		BigDecimal agreeCumu = BigDecimal.ZERO;
		accubean.setAccount(account);//活期账号
		if (accBalance.compareTo(agreeDto.getRetainedBal()) >= 0){
			accubean.setCumu(agreeDto.getRetainedBal());//留存的积数
			agreeCumu = accBalance.subtract(agreeDto.getRetainedBal());
		}else {
			ClFcAccuBalance accuBalance = new ClFcAccuBalance();
			accuBalance.setBalanceDate(balanceDate);
			accuBalance.setAccount(account);
			accuBalance.setAgreedType(Integer.valueOf(0));//协定段
			accuBalance.setCurCode(curId);
			accuBalance.setInterestSign(CloseConstant.INTEREST_SIGN_N);
			accuBalance.setPreSign(CloseConstant.INTEREST_SIGN_N);
			List<ClFcAccuBalance> accuList = balanceDao.queryUnIstAccountAccu(accuBalance);
			if(accuList.size() > 0){
				BigDecimal itsCumu = BigDecimal.ZERO;
				for (ClFcAccuBalance accu:accuList) {
					if(accu.getInterestSign().equals("1")){
						itsCumu = itsCumu.add(accu.getCumu());
					}
				}
				accubean.setCumu(accBalance.subtract(itsCumu));//留存的积数
			}else {
				accubean.setCumu(accBalance);//留存的积数
			}
		}
		insertFcAccuBanalce(accubean, CloseConstant.AGREE_SIGN_NO, curId, user,false,false);//非协定积数
		accubean.setAccount(agreeDto.getAgrAcc());
		accubean.setCumu(agreeCumu);//正常未计息积数
		accubean.setAgreeType("1");//协定段
		accubean.setAgreeDepositId(agreeDto.getId());
		insertFcAccuBanalce(accubean, CloseConstant.AGREE_SIGN_YES, curId, user,false,false);//转入协定账号积数
	}
	/**修改协定调整积数*/
	public void updateSaveAgreeAccAdjustCumu(AccAdjustDtlVO adjustDtlVO,BigDecimal adjustCumu,BigDecimal accBalance,BigDecimal retainedBal,String agrAcc,String user){
		if(adjustCumu.add(accBalance).compareTo(retainedBal) >= 0){
			List<ClFcAccuBalance> accu = balanceDao.queryAgreeAccu(agrAcc,adjustDtlVO.getAdjustDate(),1);
			for (ClFcAccuBalance a : accu){
				if(a.getInterestSign().equals(CloseConstant.INTEREST_SIGN_Y)){
					ClFcAccuBalance newAccu = new ClFcAccuBalance();
					BeanUtils.copyProperties(a,newAccu);
					newAccu.setId("");
					newAccu.setCumu(BigDecimal.ZERO);
					newAccu.setAdjustCumu(a.getAdjustCumu().add(adjustCumu));
					newAccu.setInterestSign(CloseConstant.INTEREST_SIGN_N);
					newAccu.setPreSign(CloseConstant.INTEREST_SIGN_N);
					newAccu.setInterestTime(null);
					newAccu.setCreateTime(DateUtils.getDate());
					newAccu.setCreateBy(user);
				}else{
					a.setAdjustCumu(a.getAdjustCumu().add(adjustCumu));
				}
				balanceDao.saveOrUpdate(a);
			}
		}
	}
	/**处理日初*/
	public void disposeAgreeAccu(FcDayCloseVO closeVO, ICurrentUser user) {
//		List<AccuBalanceDtlBean> dtlBeans = balanceDao.queryCumuDtl(closeVO);//获取账户每日余额
		List<AccuBalanceDtlBean> dtlBeans = this.dealDate(closeVO);//获取账户每日余额
		if (null == dtlBeans) {
			return;
		}
		List<ClFcAccuBalance> agrList = new ArrayList<>();
		List<ClFcAccuBalance> accuList = new ArrayList<>();
		for (AccuBalanceDtlBean dtlBean : dtlBeans) {
			Date accStartDate = dtlBean.getAccStartDate();//账号启用日期
			Date balanceDate = dtlBean.getBalanceDate();//余额日期
			String account = dtlBean.getAccount();//账号
			//积数日期必须大于等于开户日期才可以重新生成积数表资料,避免初始化积数被更新为0
			if (accStartDate.compareTo(balanceDate) > 0) {
				continue;
			}
			/**是否协定登记*/
			List<FcAgreeDepositDetailDTO> agreeBeans = checkAgreeDeposit(account, balanceDate,false);
			if (!EmptyUtils.isEmptyList(agreeBeans)) {
				FcAgreedDepositDTO isChangeAcc = iFcAgreedDepositService.getFcAgreedDepositByBlnctrAcc(account,balanceDate);//是否转入协定账号
				//是否转入协定账号
				if("1".equals(isChangeAcc.getIsAgreedDepositAcc())) {
					List<ClFcAccuBalance> agreeAccu = balanceDao.queryAgreeAccu(isChangeAcc.getAgrAcc(), balanceDate, 1);
					for (ClFcAccuBalance agr : agreeAccu) {
						ClFcAccuBalance newAccu = new ClFcAccuBalance();
						BeanUtils.copyProperties(agr,newAccu);
						newAccu.setId(StringUtils.getUUId());
						newAccu.setAccount(account);
						newAccu.setUpdateBy(user.getLoginName());
						newAccu.setUpdateTime(DateUtils.getDate());
						accuList.add(newAccu);
						//清空协定账号积数信息
						agr.setCumu(BigDecimal.ZERO);
						agr.setAdjustCumu(BigDecimal.ZERO);
						agr.setAgreedDepositId("");
						agr.setAgreedDepositSign("");
						agr.setInterestSign("0");
						agr.setInterestTime(null);
						agr.setPreSign("0");
						agr.setAgreedType(null);
						agr.setUpdateBy(user.getLoginName());
						agr.setUpdateTime(DateUtils.getDate());
						agrList.add(agr);
					}
				}
			}
		}
		if(agrList.size() > 0 && accuList.size() > 0) {
			balanceDao.batchUpdate(agrList);
			balanceDao.batchSave(accuList);
		}
	}
	/**处理反日初*/
	public void ReverseDayStartAgreeAccu(FcDayCloseVO closeVO, ICurrentUser user) {
//		List<AccuBalanceDtlBean> dtlBeans = balanceDao.queryCumuDtl(closeVO);//获取账户每日余额
		List<AccuBalanceDtlBean> dtlBeans = this.dealDate(closeVO);//获取账户每日余额
		if (null == dtlBeans) {
			return;
		}
		List<ClFcAccuBalance> agrList = new ArrayList<>();
		for (AccuBalanceDtlBean dtlBean : dtlBeans) {
			Date accStartDate = dtlBean.getAccStartDate();//账号启用日期
			Date balanceDate = dtlBean.getBalanceDate();//余额日期
			String account = dtlBean.getAccount();//账号
			//积数日期必须大于等于开户日期才可以重新生成积数表资料,避免初始化积数被更新为0
			if (accStartDate.compareTo(balanceDate) > 0) {
				continue;
			}
			/**是否协定登记*/
			List<FcAgreeDepositDetailDTO> agreeBeans = checkAgreeDeposit(account, balanceDate,false);
			if (!EmptyUtils.isEmptyList(agreeBeans)) {
				FcAgreedDepositDTO isChangeAcc = iFcAgreedDepositService.getFcAgreedDepositByBlnctrAcc(account,balanceDate);//是否转入协定账号
				//是否转入协定账号
				if("1".equals(isChangeAcc.getIsAgreedDepositAcc())) {
					//活期协定积数
					List<ClFcAccuBalance> accu = balanceDao.queryAgreeAccu(account, balanceDate, 1);
					//协定账号积数
					List<ClFcAccuBalance> agreeAccu = balanceDao.queryAgreeAccu(isChangeAcc.getAgrAcc(), balanceDate, null);
					for (int i=0;i< accu.size();i++) {
						for(int j=i;j<agreeAccu.size();j++) {
							BeanUtils.copyProperties(accu.get(i),agreeAccu.get(j),"id");
							agreeAccu.get(j).setAccount(isChangeAcc.getAgrAcc());
							agreeAccu.get(j).setUpdateBy(user.getLoginName());
							agreeAccu.get(j).setUpdateTime(DateUtils.getDate());
							agrList.add(agreeAccu.get(j));
							continue;
						}
						balanceDao.deleteArggerAccu(accu.get(i).getId());
					}
				}
				if(agrList.size() > 0){
					balanceDao.batchUpdate(agrList);
				}
			}
		}
	}
}
