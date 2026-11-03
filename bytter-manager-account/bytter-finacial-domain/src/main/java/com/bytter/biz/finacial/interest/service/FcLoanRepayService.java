package com.bytter.biz.finacial.interest.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.finacial.accbook.dao.FcAccbookDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.EmergencySign;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.finacial.interest.bean.LoanRepayAddBean;
import com.bytter.biz.finacial.interest.dao.FcLoanRepayDao;
import com.bytter.biz.finacial.interest.dto.FcLoanRepayDto;
import com.bytter.biz.finacial.interest.entity.FcLoanBalanceDtl;
import com.bytter.biz.finacial.interest.entity.FcLoanRepay;
import com.bytter.biz.finacial.interest.vo.FcLessPayBillVO;
import com.bytter.biz.finacial.interest.vo.FcLoanRepayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
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
* @Description: FC_LOAN_REPAY
* @author onlineGenerator
* @date 2017-08-31 10:08:43
* @version 1.0
*
*/
@Service("fcLoanRepayService")
@Transactional
public class FcLoanRepayService extends BaseService<FcLoanRepay,String> {
	@Autowired
	private FcLoanRepayDao fcLoanRepayDao;
	@Autowired
	private FcAccbookDao fcAccbookDao;
	@Autowired
	private FcOnlineLedgerService fcOnlineLedgerService;
	@DubboReference(check = false)
	private IFcBusRaceService fcBusRaceService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IFcAccbookService iFcAccbookService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@DubboReference(check = false)
	private IFcDayCloseService IFcDayCloseService;

	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Autowired
	private FcLoanBalanceDtlService fcLoanBalanceDtlService;
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(FcLoanRepay entity){
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
	public void save(FcLoanRepay entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcLoanRepay entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcLoanRepay entity){
		super.saveOrUpdate(entity);
	}
	public BaseDao<FcLoanRepay, String> getEntityDao() {
		return fcLoanRepayDao;
	}
	/**
	 * <b>功能介绍：</b>放款利息还款列表暂存数据<br>
	 * <b>日期：</b>2017年9月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param dataTablesInput
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput listFcLoanRepay(DataTablesInput dataTablesInput,String userId){
		List<String> corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_INTEREST_FCLOANREPAY_HAND,userId);
		DataTablesOutput dataTablesOutput = fcLoanRepayDao.listFcLoanRepay(dataTablesInput, corpAuth, userId);
		List<FcLoanRepayVO> data = dataTablesOutput.getData();
		ArrayList<FcLoanRepayVO> fcLoanRepayVOArrayList = new ArrayList<FcLoanRepayVO>();
		ArrayList<String> accounts = new ArrayList<>();
		Map<String, String> nameByIds = Maps.newHashMap();
		Map<String, String> netNameMap = Maps.newHashMap();
		Map<String, String> curNameMap = Maps.newHashMap();
		if (CollectionUtil.isNotEmpty(data)){
			List<String> corpIds = data.stream().map(FcLoanRepayVO::getCorpId).collect(Collectors.toList());
			List<String> netIds = data.stream().map(FcLoanRepayVO::getNetId).collect(Collectors.toList());
			List<String> curIds = data.stream().map(FcLoanRepayVO::getCurCode).collect(Collectors.toList());
			netNameMap = iCorpService.getNameByNetIds(netIds);
			nameByIds = iCorpService.getNameByIds(corpIds);
			curNameMap = btCurrencyService.getCurrencyNameByIds(curIds);
		}
		for (FcLoanRepayVO datum : data) {
			datum.setCorpName(nameByIds.get(datum.getCorpId()));
			datum.setNetName(netNameMap.get(datum.getNetId()));
			datum.setCurName(curNameMap.get(datum.getCurCode()));
			fcLoanRepayVOArrayList.add(datum);
			accounts.add(datum.getLoanAcc());
		}
		if(!accounts.isEmpty()){
			List<FcAccDTO> fcAccByBlnctrAccs = iFcAccService.getFcAccByBlnctrAccs(accounts);
			Map<String, String> map = fcAccByBlnctrAccs.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, FcAccDTO::getAccName));
			fcLoanRepayVOArrayList.forEach( a -> {
				a.setAccName(map.get(a.getLoanAcc()));
			});
		}
		dataTablesOutput.setData(fcLoanRepayVOArrayList);
		return dataTablesOutput;
	}
	/**
	 * <b>功能介绍：</b>放款利息还款列表待复核数据<br>
	 * <b>日期：</b>2017年9月7日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param dataTablesInput
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput listCheckFcLoanRepay(DataTablesInput dataTablesInput,ICurrentUser user){
		List<String> corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_INTEREST_FCLOANREPAY_CHECK,user.getId());
		DataTablesOutput dataTablesOutput = fcLoanRepayDao.listCheckFcLoanRepay(dataTablesInput, user.getLoginName(), corpAuth);
		List<FcLoanRepayVO> data = dataTablesOutput.getData();
		ArrayList<String> accounts = new ArrayList<>();
		ArrayList<FcLoanRepayVO> fcLoanRepayVOS = new ArrayList<>();
		Map<String, String> nameByIds = Maps.newHashMap();
		Map<String, String> netNameMap = Maps.newHashMap();
		Map<String, String> curNameMap = Maps.newHashMap();
		if (CollectionUtil.isNotEmpty(data)){
			List<String> corpIds = data.stream().map(FcLoanRepayVO::getCorpId).collect(Collectors.toList());
			List<String> netIds = data.stream().map(FcLoanRepayVO::getNetId).collect(Collectors.toList());
			List<String> curIds = data.stream().map(FcLoanRepayVO::getCurCode).collect(Collectors.toList());
			netNameMap = iCorpService.getNameByNetIds(netIds);
			nameByIds = iCorpService.getNameByIds(corpIds);
			curNameMap = btCurrencyService.getCurrencyNameByIds(curIds);
		}
		for (FcLoanRepayVO datum : data) {
			datum.setCorpName(nameByIds.get(datum.getCorpId()));
			datum.setNetName(netNameMap.get(datum.getNetId()));
			datum.setCurName(curNameMap.get(datum.getCurCode()));
			accounts.add(datum.getLoanAcc());
			fcLoanRepayVOS.add(datum);
		}
		List<FcAccDTO> fcAccByBlnctrAccs = iFcAccService.getFcAccByBlnctrAccs(accounts);
		Map<String, FcAccDTO> accountMap = fcAccByBlnctrAccs.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
		fcLoanRepayVOS.forEach( a -> {
			FcAccDTO fcAccDTO = accountMap.get(a.getLoanAcc());
			a.setAccName(fcAccDTO.getAccName());
		});
		dataTablesOutput.setData(fcLoanRepayVOS);
		return dataTablesOutput;
	}
	/**
	 * <b>功能介绍：</b>保存放款利息应还<br>
	 * <b>日期：</b>2017年9月5日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param loanRepayBean
	 */
	public void saveFcLoanRepay(LoanRepayAddBean loanRepayBean,ICurrentUser user){
		if(loanRepayBean.getInterest().compareTo(new BigDecimal(0))==0){//利息不能为零
			logger.debug("应还利息不能为零");
			throw new ParameterException(InterestMessageEnum.FIN_LOANREPAY_INTERET_ISZERO);
		}
		FcLoanRepay loanRepay = new FcLoanRepay();
		setFcLoanRepayData(loanRepay,loanRepayBean);
		loanRepay.setOperatorCode(user.getLoginName());
		loanRepay.setCreateBy(user.getId());
		loanRepay.setCreateTime(DateUtils.getDate());
		loanRepay.setEnterDate(DateUtils.getDate());
		loanRepay.setTallyDate(loanRepayBean.getTallyDate());
		if(loanRepayBean.getOpType()==0){//暂存
			loanRepay.setStatus(CommonConstant.STATUE_TEMP);
		}else{//提交
			loanRepay.setStatus(CommonConstant.STATUE_WAIT_CHECK);
		}
		FcAccbookVoucherDTO fcAccbook = createVoucher(loanRepayBean);
		VoucherWrapperDTO voucher = new VoucherWrapperDTO();//凭证封装对象
		voucher = handleVoucher(loanRepay,fcAccbook,voucher,OperateType.ADD,user.getId());//提交凭证数据
		loanRepay.setInterestOrderNo(voucher.getTarget().get(0).getAccbook().getOrderNo());
		fcLoanRepayDao.saveOrUpdate(loanRepay);//保存放款利息应还
		fcLoanRepayDao.updateFcLessPallBillRepay(loanRepayBean.getRepayId(),InterestConstant.INTEREST_REPAY_YES);//更新不足款清单为已还
		saveLoanBalanceDtl(loanRepay,loanRepayBean,user.getId());//保存还款对应关系
	}
	/**
	 * <b>功能介绍：</b>利息还款表设置值<br>
	 * <b>日期：</b>2017年9月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param loanRepay
	 * @param loanRepayBean
	 */
	public void setFcLoanRepayData(FcLoanRepay loanRepay,LoanRepayAddBean loanRepayBean){
		FcAccDTO loanAcc = iFcAccService.getAccDTOByAccount(loanRepayBean.getLoanAcc());
		loanRepay.setBusType("1");//应收利息
		loanRepay.setCorpId(loanRepayBean.getCorpId());
		CorpDTO  corp = iCorpService.get(loanRepayBean.getCorpId());
		if(CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())){
			loanRepay.setNetId(loanRepayBean.getCorpId());
		}else{
			loanRepay.setNetId(iCorpService.getNetByCorpId(loanRepayBean.getCorpId()).getId());
		}
		Date dayColose = IFcDayCloseService.getDayCloseFinalDate(loanRepay.getNetId());
		if(DateUtils.dayDiffs(dayColose,loanRepayBean.getTallyDate())>=0){//网点[测试],日期[20117-12-04]已日结，请重新选择记账日期
			throw new ParameterException(InterestMessageEnum.FIN_LOANREPAY_INTERET_NETISDAYCLOSE,
					iCorpService.get(loanRepay.getNetId()).getName(),
					DateUtils.date2Str(loanRepayBean.getTallyDate(), DateUtils.date_sdf()));
		}
		loanRepay.setCurCode(loanAcc.getCurId());
		loanRepay.setLoanAcc(loanRepayBean.getLoanAcc());
		loanRepay.setDetailAcc(loanRepayBean.getLoanAcc());
		loanRepay.setRepayAcc(loanRepayBean.getRepayAcc());
		loanRepay.setNeedInterestAcc(loanRepayBean.getNeedinterestAcc());//应收利息账号
		loanRepay.setRepayType("3");//归还利息
		loanRepay.setInterest(loanRepayBean.getInterest());//利息
	}
	/**
	 * <b>功能介绍：</b>构建凭证传输对象  <br>
	 * <b>日期：</b>2017年9月5日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param loanRepayBean
	 * @return
	 */
	public FcAccbookVoucherDTO createVoucher(LoanRepayAddBean loanRepayBean){
		FcAccbookVoucherDTO vourcher = new FcAccbookVoucherDTO();
		FcAccbookDTO accBook = new FcAccbookDTO();
		accBook.setInterestStartDate(loanRepayBean.getTallyDate());
		accBook.setTallyDate(loanRepayBean.getTallyDate());
		accBook.setEnrolTime(DateUtils.getDate());
		accBook.setBusRaceId(getFcBusRaceId(loanRepayBean.getCorpId(),FinaBookConstant.SysRaceCode.IST_INTEREST));//设置业务种类ID
		accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_GENERAL.getValue());//加急标志
		accBook.setVoucherState(FinaBookConstant.VoucherStatus.STATUS_TEMP.get());//凭证暂存
		accBook.setGenerateMode(FinaBookConstant.GenerateMode.INTEREST.get());//产生方式
		accBook.setBusOriType(FinaBookConstant.BusOriType.IST_INTEREST_BOOK.get());//业务来源
		accBook.setVoucherType(loanRepayBean.getAccbookDtls().get(0).getVoucherType());//凭证种类
		vourcher.setAccbook(accBook);
		List<FcAccbookDtl> dtls = loanRepayBean.getAccbookDtls();
		List<FcAccbookDtlDTO> dtlsD = BeanUtils.copyListProperties(dtls, FcAccbookDtlDTO.class);
		vourcher.setBookDtls(dtlsD);
		return vourcher;
	}
	/**
	 * <b>功能介绍：</b>获取业务种类ID<br>
	 * <b>日期：</b>2017年9月5日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param netCode
	 * @param sysBean
	 * @return
	 */
	public String getFcBusRaceId(String corpId , ConstantBean<String> sysBean){
		String netCode ="";//网点代码
		CorpDTO  corp = iCorpService.get(corpId);
		if(CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())){
			netCode = corpId;
		}else{
			netCode = iCorpService.getNetByCorpId(corpId).getId();
		}
		List<FcBusRaceDTO> busRace = fcBusRaceService.findBySysRaceCode(sysBean.get());
		if(EmptyUtils.isEmptyList(busRace)){
			FcBusRaceDTO tempRace = new FcBusRaceDTO();
			tempRace.setNetId(netCode);
			tempRace.setRaceCode(sysBean.get());
			tempRace.setRaceName(sysBean.getName());
			tempRace.setSysRaceCode(sysBean.get());
			fcBusRaceService.save(tempRace, new String[]{netCode},"");
			busRace = fcBusRaceService.findByNetIdSysRaceCode(sysBean.get(), netCode);
		}
		return busRace.get(0).getId();
    }
	/**
	 * <b>功能介绍：</b>保存还款关联信息<br>
	 * <b>日期：</b>2017年9月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param loanRepay
	 * @param loanRepayBean
	 */
	public void saveLoanBalanceDtl(FcLoanRepay loanRepay,LoanRepayAddBean loanRepayBean,String userId){
		FcLoanBalanceDtl dtl = new FcLoanBalanceDtl();
		dtl.setLoanRepayId(loanRepay.getId());//还款ID,保存放款利息还款表ID
		dtl.setBalanceId(loanRepayBean.getRepayId());//积数ID,保存不足款还款ID
		dtl.setSysCode(InterestConstant.INTEREST_BUSINESS_CMS_CODE);//系统代码
		dtl.setCreateBy(userId);
		dtl.setCreateTime(DateUtils.getDate());
		fcLoanBalanceDtlService.save(dtl);
	}
	/**
	 * <b>功能介绍：</b>删除应还利息数据<br>
	 * <b>日期：</b>2017年9月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param loanRepay
	 */
	public void deleteFcLoanRepay(FcLoanRepay loanRepay,String userId){
		VoucherWrapperDTO  wrapper = new VoucherWrapperDTO();
		//1.删除凭证
		handleVoucher(loanRepay, null, wrapper, OperateType.DELETE,userId);
		List<FcLoanBalanceDtl> loanDtls = fcLoanBalanceDtlService.getFcLoanBalanceDtlByLoanRepayId(loanRepay.getId());//所有还款对应信息
		for (FcLoanBalanceDtl dtl : loanDtls) {
			//2.更改不足款清单为未还
			fcLoanRepayDao.updateFcLessPallBillRepay(dtl.getBalanceId(),InterestConstant.INTEREST_REPAY_NO);//更新不足款清单为未还
			//3.删除还款对应关系
			fcLoanBalanceDtlService.delete(dtl.getId());
		}
		//4.删除还款数据
		delete(loanRepay.getId());
		getEntityDao().flush();
	}
	/**
	 * <b>功能介绍：</b>复核应还利息数据<br>
	 * <b>日期：</b>2017年9月8日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param loanRepay
	 */
	public void checkFcLoanRepay(FcLoanRepay loanRepay,String userId){
		if(loanRepay.getStatus()==CommonConstant.STATUE_CHECK){//复核通过，凭证复核
			VoucherWrapperDTO  wrapper = new VoucherWrapperDTO();
			handleVoucher(loanRepay, null, wrapper, OperateType.CHECK,userId);
		}
		//应收利息还款复核(打回)操作
		this.saveOrUpdate(loanRepay);
	}
	/**
	 * <b>功能介绍：</b>凭证操作(新增、删除、复核)<br>
	 * <b>日期：</b>2017年9月8日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param loanRepay
	 * @param fcAccbook
	 * @param wrapper
	 * @param opType
	 */
	public VoucherWrapperDTO handleVoucher(FcLoanRepay loanRepay,FcAccbookVoucherDTO fcAccbook,VoucherWrapperDTO wrapper,OperateType opType,String userId){
		FcAccbookDTO book = findAccbookByOrderNo(loanRepay.getInterestOrderNo());//根据流水号获取账务数据
		wrapper.setAutoAccounter(false);//业务上处理记账
		wrapper.setUserCode(userId);
		if(opType == OperateType.ADD){
			wrapper.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
			wrapper.setOriType(FinaBookConstant.BusOriType.IST_INTEREST_BOOK.get());
			wrapper.addTarget(fcAccbook);
			wrapper = iFcAccbookService.submitVoucher(wrapper);//提交凭证
		}else if(opType == OperateType.DELETE){
			if(!EmptyUtils.isEmpty(book)){
				wrapper.setBusGlobalId(book.getBusGlobalId());
				wrapper = iFcAccbookService.deleteVoucher(wrapper);//删除凭证
			}
		}else if(opType == OperateType.CHECK){
			if(!EmptyUtils.isEmpty(book)){
				wrapper.setBusGlobalId(book.getBusGlobalId());
				wrapper.setCheckUser(userId);//复核人
				wrapper = iFcAccbookService.checkVoucher(wrapper);//复核凭证
			}
		}
		return wrapper;
	}
	/**
	 * <b>功能介绍：</b>根据流水号获取账务数据<br>
	 * <b>日期：</b>2017年9月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param orderNo
	 * @return
	 */
	public FcAccbookDTO findAccbookByOrderNo(String orderNo){
		List<FcAccbook> books = fcAccbookDao.getBooksByOrderNo(orderNo);
		FcAccbookDTO bookDTO = new FcAccbookDTO();
		if(EmptyUtils.isEmptyList(books)){
			return null;
		}else{
			BeanUtils.copyProperties(books.get(0), bookDTO);
			return bookDTO;
		}
	}
	/**
	 * <b>功能介绍：</b>根据还款信息查询不足款清单<br>
	 * <b>日期：</b>2017年9月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param loanRepayId
	 * @return
	 */
	@Transactional(readOnly = true)
	public FcLessPayBillVO getFcLessPayBillByFcLoanRepay(String loanRepayId){
		List<FcLoanBalanceDtl> loanDtls = fcLoanBalanceDtlService.getFcLoanBalanceDtlByLoanRepayId(loanRepayId);//所有还款对应信息
		return fcLoanRepayDao.getFcLessPayBillByFcLoanRepay(loanDtls.get(0).getBalanceId());
	}
	/**
	 * <b>功能介绍：</b>根据ID获取放款利息还款数据<br>
	 * <b>日期：</b>2017年9月7日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public FcLoanRepayVO getFcLoanRepayById(String id){
		FcLoanRepayVO vo = fcLoanRepayDao.getFcLoanRepayById(id);
		FcAccDTO accDTOByAccount = iFcAccService.getAccDTOByAccount(vo.getLoanAcc());
		vo.setAccName(accDTOByAccount.getAccName());
		CorpDTO corpDTO = iCorpService.get(vo.getCorpId());
		vo.setCorpName(corpDTO.getName());
		return vo;
	}
	/**
	 * <b>功能介绍：</b>初始化网点<br>
	 * <b>日期：</b>2017年8月31日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	public CorpDTO getNet(){
		return fcLoanRepayDao.listNet().get(0);
	}
	/**
	 * <b>功能介绍：</b>根据单位查询放款账号<br>
	 * <b>日期：</b>2017年9月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param corpId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String,Object>> getLoanAccByCorpCode(String corpId){
		//调用 基础模块的服务
//		return fcLoanRepayDao.getLoanAccByCorpCode(corpId);
		return iFcAccService.getLoanAccByCorpCode(corpId);
	}
	/**
	 * <b>功能介绍：</b>根据贷款账号查询单位活期存款账户<br>
	 * <b>日期：</b>2017年9月4日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param loanAcc
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String,Object>> getCorpCurAccInfo(String loanAcc){
		List<Map<String, Object>> corpCurAccInfo = iFcAccService.getCorpCurAccInfo(loanAcc);
//		return fcLoanRepayDao.getCorpCurAccInfo(loanAcc);
		//兼容Oracle key转小写
		List<Map<String, Object>> lists = new ArrayList<>();
		for (Map<String, Object> map : corpCurAccInfo) {
			HashMap<String, Object> dataMap = new HashMap<>();
			map.forEach((key1, value) -> {
				String key = key1.toLowerCase();
				dataMap.put(key, value);
			});
			lists.add(dataMap);
		}
		return lists;
	}
	/**
	 * <b>功能介绍：</b>查询待归还账户明细<br>
	 * <b>日期：</b>2017年9月1日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param acc
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<FcLessPayBillVO> queryRepayDetailByAcc(String acc){
		return fcLoanRepayDao.queryRepayDetailByAcc(acc);
	}
	/**
	 * <b>功能介绍：</b>获取账户余额<br>
	 * <b>日期：</b>2017年9月4日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param account
	 * @return
	 */
	public BigDecimal getAccountBalance(String account){
		BigDecimal checkBalance = fcOnlineLedgerService.getByBlnctrAcc(account).getCheckBalance();
		return checkBalance;
	}
	/**
	 * <b>功能介绍：</b>根据账号获取账号户名<br>
	 * <b>日期：</b>2017年9月7日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param account
	 * @return
	 */
	public String getaccNameByaccount(String account){
		return iFcAccService.getAccDTOByAccount(account).getAccName();
	}
	/**
	 * <b>功能介绍：</b>获取网点日结日期<br>
	 * <b>日期：</b>2017年12月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param netId
	 * @return
	 */
	public Date getNetDayOverDate(String netId){
		CorpDTO net = iCorpService.getNetByCorpId(netId);
		return IFcDayCloseService.getDayCloseFinalDate(net.getId());
	}

	public FcLoanRepayDto queryById(String id){
		FcLoanRepayDto fcLoanRepayDto = new FcLoanRepayDto();
		FcLoanRepay fcLoanRepay = fcLoanRepayDao.get(id);
		BeanUtils.copyNotNullProperties(fcLoanRepay,fcLoanRepayDto);
		CorpDTO corpDTO = iCorpService.get(fcLoanRepayDto.getCorpId());
		fcLoanRepayDto.setCorpName(corpDTO.getName());
		return fcLoanRepayDto;
	}
}
