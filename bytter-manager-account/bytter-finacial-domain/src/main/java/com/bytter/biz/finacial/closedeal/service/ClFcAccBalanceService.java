package com.bytter.biz.finacial.closedeal.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.finacial.closedeal.dao.ClFcAccBalanceDao;
import com.bytter.biz.finacial.closedeal.entity.ClFcAccBalance;
import com.bytter.biz.finacial.closedeal.entity.FcCashFlow;
import com.bytter.biz.finacial.closedeal.vo.AccBalanceBean;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: FC_ACC_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:04:02
* @version 1.0
*
*/
@Service("clFcAccBalanceService")
@Transactional
public class ClFcAccBalanceService extends BaseService<ClFcAccBalance,String> {
	@Autowired
	private ClFcAccBalanceDao fcAccBalanceDao;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;

	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(ClFcAccBalance entity){
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
	public void save(ClFcAccBalance entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(ClFcAccBalance entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(ClFcAccBalance entity){
		super.saveOrUpdate(entity);
	}
	public BaseDao<ClFcAccBalance, String> getEntityDao() {
		return fcAccBalanceDao;
	}
	/**
	 * 初始化余额
	 * @param balanceDate 余额日期
	 * @param blnctrAcc 账号
	 * @param accName 户名
	 */
	public ClFcAccBalance initAccountBalance(Date balanceDate, String blnctrAcc, String accName,String curId, ICurrentUser user){
		ClFcAccBalance fcAccBalance = new ClFcAccBalance();
		fcAccBalance.setBalanceDate(balanceDate);
		fcAccBalance.setAccount(blnctrAcc);
		fcAccBalance.setAccName(accName);
		fcAccBalance.setCreditMoney(BigDecimal.ZERO);
		fcAccBalance.setDebitMoney(BigDecimal.ZERO);
		fcAccBalance.setRushCnt(0);
		fcAccBalance.setRushCreditMoney(BigDecimal.ZERO);
		fcAccBalance.setRushDebitMoney(BigDecimal.ZERO);
		fcAccBalance.setTallyDaySign("0");
		fcAccBalance.setTrCnt(0);
		fcAccBalance.setCurId(curId);
		fcAccBalance.setTrCreditMoney(BigDecimal.ZERO);
		fcAccBalance.setTrDebitMoney(BigDecimal.ZERO);
		fcAccBalance.setYesterBalance(BigDecimal.ZERO);
		fcAccBalance.setCreateTime(DateUtils.getDate());
		fcAccBalance.setCreateBy(user.getLoginName());
		return fcAccBalance;
	}
    /**
     * 功能介绍 : 保存账户每日余额数据。<br>
     * <b>日期：</b>2017年7月10日<br>
     * @version 1.0
     * @author liyh
     */
    public void saveAccBalance(List<AccBalanceBean> balanceBeans, Date balDate, ICurrentUser user) {
        List<ClFcAccBalance> beans = fcAccBalanceDao.queryAccBalanceByBalDate(balDate);//根据余额日期获取所有账户的每日余额
        Map<String, ClFcAccBalance> balMap = new HashMap<>();
        if (EmptyUtils.isNotEmpty(beans)) {
            for(ClFcAccBalance bean : beans) {
                balMap.put(bean.getAccount(), bean);
            }
        }
        List<ClFcAccBalance> accBalances = new ArrayList<ClFcAccBalance>();
        for(AccBalanceBean balanceBean : balanceBeans) {
            String account = balanceBean.getAccount();
            ClFcAccBalance fcAccBalance = balMap.get(account);
            if (null != fcAccBalance) {
                fcAccBalance.setTrCnt(fcAccBalance.getTrCnt() + balanceBean.getTrCnt());
                fcAccBalance.setRushCnt(fcAccBalance.getRushCnt() + balanceBean.getRushCnt());
                fcAccBalance.setCnt(fcAccBalance.getCnt() + balanceBean.getCnt());
                fcAccBalance.setDebitCnt(fcAccBalance.getDebitCnt() + balanceBean.getDebitCnt());
                fcAccBalance.setCreditCnt(fcAccBalance.getCreditCnt() + balanceBean.getCreditCnt());
                fcAccBalance.setTrDebitMoney(fcAccBalance.getTrDebitMoney().add(balanceBean.getTrDebitMoney()));
                fcAccBalance.setRushDebitMoney(fcAccBalance.getRushDebitMoney().add(balanceBean.getRushDebitMoney()));
                fcAccBalance.setDebitMoney(fcAccBalance.getDebitMoney().add(balanceBean.getDebitMoney()));
                fcAccBalance.setTrCreditMoney(fcAccBalance.getTrCreditMoney().add(balanceBean.getTrCreditMoney()));
                fcAccBalance.setRushCreditMoney(fcAccBalance.getRushCreditMoney().add(balanceBean.getRushCreditMoney()));
                fcAccBalance.setCreditMoney(fcAccBalance.getCreditMoney().add(balanceBean.getCreditMoney()));
                fcAccBalance.setYesterBalance(fcAccBalance.getYesterBalance().add(balanceBean.getYesterBalance()));
                fcAccBalance.setBalance(fcAccBalance.getBalance().add(balanceBean.getBalance()));
                BeanUtils.copyProperties(fcAccBalance, balanceBean);
            } else {
                fcAccBalance = new ClFcAccBalance();
                BeanUtils.copyProperties(balanceBean, fcAccBalance);
                fcAccBalance.setCreateTime(DateUtils.getDate());
                fcAccBalance.setCreateBy(user.getLoginName());
                fcAccBalance.setTallyDaySign(balanceBean.getTallyDaySign());
            }
            accBalances.add(fcAccBalance);
        }
        batchSaveOrUpdate(accBalances);
    }
	/**
	 * 功能介绍 : 保存账户每日余额数据。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public ClFcAccBalance saveAccBalance(AccBalanceBean balanceBean, ICurrentUser user){
		ClFcAccBalance fcAccBalance = new ClFcAccBalance();
		List<ClFcAccBalance> beans = fcAccBalanceDao.queryAccBalanceByAccDate(balanceBean.getAccount(), balanceBean.getBalanceDate());
		if (null != beans && !beans.isEmpty()) {
			fcAccBalance = beans.get(0);
			fcAccBalance.setTrCnt(fcAccBalance.getTrCnt() + balanceBean.getTrCnt());
			fcAccBalance.setRushCnt(fcAccBalance.getRushCnt() + balanceBean.getRushCnt());
			fcAccBalance.setCnt(fcAccBalance.getCnt() + balanceBean.getCnt());
			fcAccBalance.setDebitCnt(fcAccBalance.getDebitCnt() + balanceBean.getDebitCnt());
			fcAccBalance.setCreditCnt(fcAccBalance.getCreditCnt() + balanceBean.getCreditCnt());
			fcAccBalance.setTrDebitMoney(fcAccBalance.getTrDebitMoney().add(balanceBean.getTrDebitMoney()));
			fcAccBalance.setRushDebitMoney(fcAccBalance.getRushDebitMoney().add(balanceBean.getRushDebitMoney()));
			fcAccBalance.setDebitMoney(fcAccBalance.getDebitMoney().add(balanceBean.getDebitMoney()));
			fcAccBalance.setTrCreditMoney(fcAccBalance.getTrCreditMoney().add(balanceBean.getTrCreditMoney()));
			fcAccBalance.setRushCreditMoney(fcAccBalance.getRushCreditMoney().add(balanceBean.getRushCreditMoney()));
			fcAccBalance.setCreditMoney(fcAccBalance.getCreditMoney().add(balanceBean.getCreditMoney()));
			fcAccBalance.setYesterBalance(fcAccBalance.getYesterBalance().add(balanceBean.getYesterBalance()));
			fcAccBalance.setBalance(fcAccBalance.getBalance().add(balanceBean.getBalance()));
			fcAccBalanceDao.update(fcAccBalance);
			BeanUtils.copyProperties(fcAccBalance, balanceBean);//相加后的数据重新赋值给balanceBean
		} else {
			BeanUtils.copyProperties(balanceBean, fcAccBalance);
			fcAccBalance.setCreateTime(DateUtils.getDate());
			fcAccBalance.setCreateBy(user.getLoginName());
			fcAccBalance.setTallyDaySign(balanceBean.getTallyDaySign());
			fcAccBalanceDao.save(fcAccBalance);
		}
		return fcAccBalance;
	}
	/**
	 * 功能介绍 : 根据余额日期获取网点的账号每日余额。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<AccBalanceBean> queryBalanceByNetDate(String netId, Date balanceDate) {
		List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByNetIdAndCheckerCodeIsNotNull(netId);
		if(!fcAccDTOS.isEmpty()) {
			List<String> accounts = fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
			Map<String, FcAccDTO> accountMap = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
			List<AccBalanceBean> accBalanceBeans = fcAccBalanceDao.queryBalanceByNetDate(netId, balanceDate, accounts);
			if (!EmptyUtils.isEmpty(accBalanceBeans) && accBalanceBeans.size() > 0) {
				accBalanceBeans.forEach(a -> {
					FcAccDTO fcAccDTO = accountMap.get(a.getAccount());
					a.setAccName(fcAccDTO.getAccName());
					a.setCurId(fcAccDTO.getCurId());
					a.setSubjectId(fcAccDTO.getSubjectId());
					a.setAccStartDate(fcAccDTO.getAccStartDate());
				});
			}
			return accBalanceBeans;
		}
		return null;
	}
	/**
	 * 功能介绍 : 根据账号和余额日期获取账号每日余额。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<AccBalanceBean> queryBalanceByAccDate(String account, Date balanceDate) {
		return fcAccBalanceDao.queryBalanceByAccDate(account, balanceDate);
	}
	/**
	 * 功能介绍 : 查询月结区间内的账户每日余额数据。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<AccBalanceBean> querySetMonthBalance(FcMonthCloseDTO closeDTO) {
		List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByNetIdAndCheckerCodeIsNotNull(closeDTO.getNetCode());
		if (CollectionUtil.isEmpty(fcAccDTOS)) {
			return new ArrayList<>();
		}
		List<String> accounts = fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
		//带上帐号查询 应该使用fcc表作为主表
		List<AccBalanceBean> accBalanceBeans = fcAccBalanceDao.querySetMonthBalance(closeDTO, accounts);
		Map<String, AccBalanceBean> accBalanceBeanMap = accBalanceBeans.stream().collect(Collectors.toMap(AccBalanceBean::getAccount, item -> item, (pre, next) -> next));
		return fcAccDTOS.stream().map(fcAccDTO -> {
			AccBalanceBean accBalanceBean = new AccBalanceBean();
			BeanUtils.copyProperties(fcAccDTO, accBalanceBean);
			if (accBalanceBeanMap.containsKey(fcAccDTO.getBlnctrAcc())) {
				AccBalanceBean accBalanceBean1 = accBalanceBeanMap.get(fcAccDTO.getBlnctrAcc());
				BeanUtils.copyNotNullProperties(accBalanceBean1, accBalanceBean);
			}
			accBalanceBean.setAccount(fcAccDTO.getBlnctrAcc());
			return accBalanceBean;
		}).collect(Collectors.toList());
	}
	/**
	 * 查询日结数据
	 * @param netId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<FcCashFlow> listDataByMonth(String netId,Date startDate,Date endDate){
		FcVoucherTypeDTO sysFcVoucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_05);
		Map<String, Object> params = new HashMap<>();
		params.put("netId", netId);
		params.put("startDate", DateUtils.format(startDate,"yyyy-MM-dd"));
		params.put("endDate", DateUtils.format(endDate,"yyyy-MM-dd"));
		params.put("voucherType", sysFcVoucherType.getVoucherType());
		return fcAccBalanceDao.listDataByMonth(params);
	}
	/**
	 * 判断月账单是否可以核对
	 *
	 * @param date 月账单
	 * @return true 存在，false 不存在
	 */
	public boolean existBalance(String date) {
		return fcAccBalanceDao.existBalance(date);
	}
}
