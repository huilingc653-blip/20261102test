package com.bytter.biz.finacial.outaccbook.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.bytter.core.constant.InnerAccountConstant;
import com.bytter.core.utils.ListUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.closedeal.vo.AccbookBalanceVO;
import com.bytter.biz.finacial.closedeal.vo.FcOutSubjectDayBean;
import com.bytter.biz.finacial.closedeal.vo.OutAccBalanceBean;
import com.bytter.biz.finacial.closedeal.vo.OutAccbookBalanceVO;
import com.bytter.biz.finacial.outaccbook.dao.ClFcOutAccBalanceDao;
import com.bytter.biz.finacial.outaccbook.entity.ClFcOutAccBalance;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.biz.finacial.api.enums.closedeal.CloseMessageEnum;
/**
* @Title: Service
* @Description: FC_OUT_ACC_BALANCE
* @author onlineGenerator
* @date 2017-07-19 16:01:24
* @version 1.0
*
*/
@Service("clFcOutAccBalanceService")
@Transactional
public class ClFcOutAccBalanceService extends BaseService<ClFcOutAccBalance,String> {
	@Autowired
	private ClFcOutAccBalanceDao outBalanceDao;
	@Autowired
	private FcOutSubjectDayService outSubjectService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(ClFcOutAccBalance entity){
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
	public void save(ClFcOutAccBalance entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(ClFcOutAccBalance entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(ClFcOutAccBalance entity){
		super.saveOrUpdate(entity);
	}
	public BaseDao<ClFcOutAccBalance, String> getEntityDao() {
		return outBalanceDao;
	}
	/**
	 * 功能介绍：表外账务凭证处理。<br>
	 * <b>日期：</b>2017年7月19日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void generateOutAccBalance(FcDayCloseDTO closeDTO, ICurrentUser user) {
		Map<String, String> map = iFcAccService.getBlnctrAccAndAccNameByNetId(closeDTO.getNetCode());
		//存在未复核的表外凭证
		List<OutAccbookBalanceVO> unCheckList = outBalanceDao.queryUnCheckOutAccbook(closeDTO, Lists.newArrayList(map.keySet()));
		if (null != unCheckList && !unCheckList.isEmpty()) {
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_EXIST_CHECK_OUTACCBOOK);
		}
		//查询
		List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByNetId(closeDTO.getNetCode());
		List<String> accounts = fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
		Map<String, FcAccDTO> accountMap = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
		List<OutAccbookBalanceVO> outAccbookBalanceVOs = outBalanceDao.queryOutAccbookBalance(closeDTO.getNetCode(), closeDTO.getCloseDate(), accounts);
		if (ListUtils.isNotNullOrEmpty(outAccbookBalanceVOs)){
			outAccbookBalanceVOs.forEach( a -> {
				FcAccDTO fcAccDTO = accountMap.get(a.getAccount());
				a.setAccName(fcAccDTO.getAccName());
				a.setCurId(fcAccDTO.getCurId());
				a.setBalanceWay(fcAccDTO.getBalanceWay());
				a.setSubjectId(fcAccDTO.getSubjectId());
			});
		}
		if (null == outAccbookBalanceVOs) {
			outAccbookBalanceVOs = new ArrayList<OutAccbookBalanceVO>();
		}
		generateAccBalance(closeDTO.getNetCode(), closeDTO.getCloseDate(), closeDTO.getLastCloseDate(),
				outAccbookBalanceVOs, user);
		outBalanceDao.updateOutAccbookOverSign(closeDTO);
	}
	/**
	 * 功能介绍 : 表外账户每日余额生成。<br>
	 * <b>日期：</b>2017年7月19日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void generateAccBalance(String netId, Date closeDate, Date lastDate, List<OutAccbookBalanceVO> outAccbookBalanceVOs, ICurrentUser user) {
		List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(netId, InnerAccountConstant.FCACC_OFFBALANCE);
		List<String> accounts = fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
		Map<String, FcAccDTO> accountMap = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
		//获取表外昨日余额
		List<OutAccBalanceBean> balanceList = outBalanceDao.queryBalanceByNetDate(netId, lastDate, accounts);
		if (ListUtils.isNotNullOrEmpty(balanceList)){
			balanceList.forEach( a -> {
				FcAccDTO fcAccDTO = accountMap.get(a.getAccount());
				a.setAccName(fcAccDTO.getAccName());
				a.setSubjectId(fcAccDTO.getSubjectId());
				a.setCurId(fcAccDTO.getCurId());
				a.setBalanceWay(fcAccDTO.getBalanceWay());
				a.setAccStartDate(fcAccDTO.getAccStartDate());
			});
		}
		if (null == balanceList) {
			balanceList = new ArrayList<OutAccBalanceBean>();
		}
		//账务数据按照日期分类
		Map<String, List<OutAccbookBalanceVO>> dateMap = new HashMap<String, List<OutAccbookBalanceVO>>();
		for (OutAccbookBalanceVO balanceVO : outAccbookBalanceVOs) {
			Date balanceDate = balanceVO.getTallyDate();
			String balanceDateStr = DateUtils.formatDate(balanceDate);
			List<OutAccbookBalanceVO> balanceVOs = dateMap.get(balanceDateStr);
			if (null == balanceVOs) {
				balanceVOs = new ArrayList<OutAccbookBalanceVO>();
				dateMap.put(balanceDateStr, balanceVOs);
			}
			balanceVOs.add(balanceVO);
		}
		//循环余额日期
		Date tempDate = lastDate;
		List<OutAccBalanceBean> dayBalList = balanceList;
		while(tempDate.compareTo(closeDate) < 0){
			String balanceDateStr = DateUtils.formatDate(DateUtils.addDay(tempDate, 1));//取下一日的账务明细
			List<OutAccbookBalanceVO> balanceVOs = dateMap.get(balanceDateStr);
			if (null == balanceVOs) {
				balanceVOs = new ArrayList<OutAccbookBalanceVO>();
			}
			//递归调用
			dayBalList = geneAccBalance(tempDate, netId, dayBalList, balanceVOs, closeDate, user);
			tempDate = DateUtils.addDay(tempDate, 1);
		}
	}
	/**
	 * 功能介绍 : 根据昨日余额+当天的账务金额生成当天的余额；
	 *        昨日余额和当日余额表数据汇总生成科目日结单。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh
	 * @param netId
	 * @param closeDate
	 */
	public List<OutAccBalanceBean> geneAccBalance(Date yesterDate, String netId, List<OutAccBalanceBean> balanceList,
												  List<OutAccbookBalanceVO> OutAccbookBalanceVOs, Date closeDate, ICurrentUser user) {
		List<OutAccBalanceBean> dayBalList = new ArrayList<OutAccBalanceBean>();
		// 获取相关余额信息
		Map<String, FcOutSubjectDayBean> subjectMap = new HashMap<String, FcOutSubjectDayBean>();
		Map<String, OutAccBalanceBean> balanceMap = new HashMap<String, OutAccBalanceBean>();
		for (OutAccbookBalanceVO balanceVO : OutAccbookBalanceVOs) {
			String account = balanceVO.getAccount();
			OutAccBalanceBean balanceBean = balanceMap.get(account);
			if (null == balanceBean) {
				balanceBean = new OutAccBalanceBean();
				balanceBean.setAccount(account);
				balanceBean.setAccName(balanceVO.getAccName());
				balanceBean.setCurId(balanceVO.getCurId());
				balanceBean.setOpDate(DateUtils.addDay(yesterDate, 1));
				balanceBean.setBalanceWay(balanceVO.getBalanceWay());//余额方向
				balanceBean.setSubjectId(balanceVO.getSubjectId());//科目ID
				balanceMap.put(account, balanceBean);
			}
			balanceBean.addDebitBalance(balanceVO.getDebitMoney());// 收入金额
			balanceBean.addCreditBalance(balanceVO.getCreditMoney());// 付出金额
			balanceBean.addDebitNum(balanceVO.getDebitNum());//收入数量
			balanceBean.addCreditNum(balanceVO.getCreditNum());//付出数量
			BigDecimal balance = balanceVO.getDebitMoney().subtract(balanceVO.getCreditMoney()).multiply(new BigDecimal(balanceVO.getBalanceWay()));
			balanceBean.addTBalance(balance);//设置账户余额为发生额相加
			Integer num = (balanceVO.getDebitNum() - balanceVO.getCreditNum()) * balanceVO.getBalanceWay();
			balanceBean.addTNum(num);//设置账户笔数为发生笔数相加
			if (BigDecimal.ZERO != balanceVO.getDebitMoney()) {
				balanceBean.addDebitCount(1);// 收方笔数增加
			} else {
				balanceBean.addCreditCount(1);// 付方笔数增加
			}
			balanceBean.addTCount(1);//今日笔数+1
			/**
			 * 科目日结单数据
			 */
			String subjectId = balanceVO.getSubjectId();//科目
			String curId = balanceVO.getCurId();//币别
			String subDayKey = subjectId + "_" + curId;
			FcOutSubjectDayBean subDayBean = subjectMap.get(subDayKey);
			if (null == subDayBean) {
				subDayBean = new FcOutSubjectDayBean(netId, subjectId, curId);
				subDayBean.setOptDate(DateUtils.addDay(yesterDate, 1));
				subjectMap.put(subDayKey, subDayBean);
			}
			if (BigDecimal.ZERO != balanceVO.getDebitMoney()) {
				subDayBean.addDebitCount(1);// 收方笔数增加
				subDayBean.addDebitMoney(balanceVO.getDebitMoney());//收方金额
			} else {
				subDayBean.addCreditCount(1);// 付方笔数增加
				subDayBean.addCreditMoney(balanceVO.getCreditMoney());//付方金额
			}
			subDayBean.addDebitNum(balanceVO.getDebitNum());//收方数量
			subDayBean.addCreditNum(balanceVO.getCreditNum());//付方数量
		}
		// 循环账户每日余额数据
		for (OutAccBalanceBean accBalance : balanceList) {
			Date accStartDate = accBalance.getAccStartDate();//账户启用日期
			Date cancelDate = accBalance.getCancelDate();//账户注销日期
			Date balanceDate = DateUtils.addDay(yesterDate, 1);// 下一日的余额
			String account = accBalance.getAccount();
			BigDecimal balance = accBalance.gettBalance();// 账户余额
			OutAccBalanceBean balanceBean = balanceMap.get(account);
			if (null == balanceBean) {
				balanceBean = new OutAccBalanceBean();
				balanceBean.setOpDate(balanceDate);
				balanceBean.setAccount(account);
				balanceBean.setAccName(accBalance.getAccName());
				balanceBean.setCurId(accBalance.getCurId());
				balanceBean.setBalanceWay(accBalance.getBalanceWay());
				balanceBean.setSubjectId(accBalance.getSubjectId());//科目
			}
			if (accStartDate.compareTo(balanceDate) > 0) {//账户启用日期大于余额日期
				balanceBean.setSaveFlag(false);//不保存账户余额
			}
			if (null != cancelDate && cancelDate.compareTo(yesterDate) <= 0) {//账户注销日期小于余额日期
				balanceBean.setSaveFlag(false);//不保存账户余额
			}
			balanceBean.setAccStartDate(accBalance.getAccStartDate());
			balanceBean.setCancelDate(accBalance.getCancelDate());
			balanceBean.setyCount(accBalance.gettCount());//昨日笔数取上次笔数
			balanceBean.setyBalance(balance);//昨日余额取上次余额
			balanceBean.addTBalance(balance);// 新增账户余额
			balanceBean.setyNum(accBalance.gettNum());//昨日数量取上次数量
			balanceBean.addTNum(accBalance.gettNum());// 新增账户笔数
			if (DateUtils.dayDiffs(balanceDate, closeDate) == 0) {
				balanceBean.setTallyDaySign("1");//记账日标识
			}
			dayBalList.add(balanceBean);//新的每日余额
			if (balanceBean.isSaveFlag()) {
				saveAccBalance(balanceBean, user);
			}
			/**
			 * 科目日结单数据来源于账户余额表，统计账户余额表数据也可以生成科目日结单
			 */
			String subjectId = accBalance.getSubjectId();//科目
			String curId = accBalance.getCurId();//币别
			String subDayKey = subjectId + "_" + curId;
			FcOutSubjectDayBean subDayBean = subjectMap.get(subDayKey);
			if (null == subDayBean) {
				subDayBean = new FcOutSubjectDayBean(netId, subjectId, curId);
				subDayBean.setOptDate(balanceDate);
				subjectMap.put(subDayKey, subDayBean);
			}
			//y_Count  t_Count   y_Num  t_Num y_Balance t_Balance
			subDayBean.addYCount(balanceBean.getyCount());//昨日笔数
			subDayBean.addTCount(balanceBean.gettCount());//今日笔数
			subDayBean.addYNum(balanceBean.getyCount());//昨日数量
			subDayBean.addTNum(balanceBean.gettNum());//今日数量
			subDayBean.addYBalance(balanceBean.getyBalance().multiply(new BigDecimal(accBalance.getBalanceWay())));//昨日余额
			subDayBean.addTBalance(balanceBean.gettBalance().multiply(new BigDecimal(accBalance.getBalanceWay())));//今日余额
		}
		//保存科目日结单数据，插入账号没有发生账务的科目余额表数据
		outSubjectService.saveOutSubjectDayBeans(new ArrayList<FcOutSubjectDayBean>(subjectMap.values()));
		return dayBalList;
	}
	/**
	 * 功能介绍 : 保存账户每日余额数据。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public ClFcOutAccBalance saveAccBalance(OutAccBalanceBean balanceBean, ICurrentUser user){
		ClFcOutAccBalance fcAccBalance = new ClFcOutAccBalance();
		List<ClFcOutAccBalance> beans = outBalanceDao.queryAccBalanceByAccDate(balanceBean.getAccount(),
				balanceBean.getOpDate());
		if (null != beans && !beans.isEmpty()) {
			fcAccBalance = beans.get(0);
			fcAccBalance.setOptDate(balanceBean.getOpDate());
			fcAccBalance.setYBalance(fcAccBalance.getYBalance().add(balanceBean.getyBalance()));
			fcAccBalance.setTBalance(fcAccBalance.getTBalance().add(balanceBean.gettBalance()));
			fcAccBalance.setDebitNum(fcAccBalance.getDebitNum() + balanceBean.getDebitNum());
			fcAccBalance.setCreditNum(fcAccBalance.getCreditNum() + balanceBean.getCreditNum());
			fcAccBalance.setYNum(fcAccBalance.getYNum() + balanceBean.getyNum());
			fcAccBalance.setTNum(fcAccBalance.getTNum() + balanceBean.gettNum());
			fcAccBalance.setDebitMoney(fcAccBalance.getDebitMoney().add(balanceBean.getDebitMoney()));
			fcAccBalance.setCreditMoney(fcAccBalance.getCreditMoney().add(balanceBean.getCreditMoney()));
			fcAccBalance.setDebitCount(fcAccBalance.getDebitCount() + balanceBean.getDebitCount());
			fcAccBalance.setCreditCount(fcAccBalance.getCreditCount() + balanceBean.getCreditCount());
			fcAccBalance.setYCount(fcAccBalance.getYCount() + balanceBean.getyCount());
			fcAccBalance.setTCount(fcAccBalance.getTCount() + balanceBean.gettCount());
			outBalanceDao.update(fcAccBalance);
			BeanUtils.copyProperties(fcAccBalance, balanceBean);//相加后的数据重新赋值给balanceBean
		} else {
			BeanUtils.copyProperties(balanceBean, fcAccBalance);
			fcAccBalance.setOptDate(balanceBean.getOpDate());
			fcAccBalance.setCurCode(balanceBean.getCurId());
			fcAccBalance.setCreateTime(DateUtils.getDate());
			fcAccBalance.setCreateBy(user.getLoginName());
			outBalanceDao.save(fcAccBalance);
		}
		return fcAccBalance;
	}
	/**
	 * 功能介绍 : 还原日结标识为已结标识。<br>
	 * <b>日期：</b>2017年7月19日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void reverseOutAccbookOverSign(FcDayCloseDTO closeDTO) {
		outBalanceDao.reverseOutAccbookOverSign(closeDTO);
	}
}
