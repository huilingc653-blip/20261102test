package com.bytter.biz.finacial.accbook.service;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.finacial.accbook.dao.FcAccbookBlnedDao;
import com.bytter.biz.finacial.accbook.dao.FcAccbookDao;
import com.bytter.biz.finacial.accbook.entity.*;
import com.bytter.biz.finacial.accbook.util.FcAccbookUtils;
import com.bytter.biz.finacial.accbook.vo.*;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: FC_ACCBOOK_BLNED
* @author onlineGenerator
* @date 2017-06-19 11:28:14
* @version 1.0
*
*/
@Service("fcAccbookBlnedService")
@Transactional
public class FcAccbookBlnedService extends BaseService<FcAccbookBlned,String> {
	@Autowired
	private FcAccbookBlnedDao fcAccbookBlnedDao;
	@Autowired
	private FcAccbookDtlService bookDtlservice;

	@Autowired
	private FcAccbookDao fcAccbookDao;
	@Autowired
	private FcAccbookBlnedExtService bookBlnedExtService;
	@Autowired
	private FcAccbookExtService bookExtservice;
	@Autowired
	private FcAccbookBlnedDtlService bookBlnedDtlservice;
	@DubboReference(check = false)
	private IFcVoucherTypeService iFcVoucherTypeService;

	@DubboReference(check = false)
	private IUserService userService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
	@DubboReference(check = false)
	private IFcVoucherTypeService voucherTypeService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@DubboReference(check = false)
	private IBtCurrencyService currencyService;
	@Autowired
	private IFcBusRaceService busRaceService;

	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcAccbookBlned entity){
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
 	public void save(FcAccbookBlned entity){
		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccbookBlned entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcAccbookBlned entity){
		 super.saveOrUpdate(entity);
 	}
 	public BaseDao<FcAccbookBlned, String> getEntityDao() {
		return fcAccbookBlnedDao;
	}

 	/**
	 * 功能介绍 ：获取冲账凭证列表数据。<br>
	 * <b>日期：</b>2017年5月31日<br>
	 * @version 1.0
	 * @author liyh
	 */
 	@Transactional(readOnly = true)
	public DataTablesOutput getCanRushFcAccbookVoList(DataTablesInput input, String resource, String userId) {
		input.addFilter(MatchTypeEnum.EQ, "rushSign", FinacialConstant.RUSH_SIGN_N);//未冲
		input.addFilter(MatchTypeEnum.NE, "generateMode", FinaBookConstant.GenerateMode.BANK_COLLECT_BOOK.get());//非银企收款记账
		input.addFilter(MatchTypeEnum.NE, "busOriType", FinaBookConstant.BusOriType.IST_INTEREST_BOOK.get());//非利息凭证
		input.addFilter(MatchTypeEnum.EQ, "operatorCode", userId);//是记账人
		// 状态记账打回、暂存、待复核
		input.addFilter(MatchTypeEnum.EQ, "voucherState",
						FinaBookConstant.VoucherStatus.STATUS_CHECK.get() );//正常凭证
		//非存款管理凭证、手续费计提、银企收款记账、利息
		input.addFilter(MatchTypeEnum.IN, "generateMode", FinaBookConstant.GenerateMode.TAILLY.get() + ","
				+ FinaBookConstant.GenerateMode.UNION_ACCBOOK.get() + "," + FinaBookConstant.GenerateMode.EXCHANGE.get()
				+ "," + FinaBookConstant.GenerateMode.PROFIT_BOOK.get() + "," + FinaBookConstant.GenerateMode.TAX.get()
				+ "," + FinaBookConstant.GenerateMode.INNER_PAY_BOOK.get() + ","
				+ FinaBookConstant.GenerateMode.ENTRUST_COLLECT_BOOK.get());

		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
		DataTablesOutput dataTablesOutput = fcAccbookBlnedDao.queryCanEditAccbooks(input, false, corpAuthSql);
		List<FcAccbookListVO> data = dataTablesOutput.getData();
		List<String> ids = data.stream().map(FcAccbookListVO::getVoucherType).collect(Collectors.toList());
		List<FcVoucherTypeDTO> dtos = iFcVoucherTypeService.getFcVoucherTypeByIds(ids);
		Map<String, String> map = dtos.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
		if (CollectionUtils.isNotEmpty(dtos)){
			data.forEach(a -> {
				a.setVoucherName(map.get(a.getVoucherType()));
				a.setVoucherId(a.getVoucherType());
			});
		}
		return dataTablesOutput;
	}

	/**
	 * 功能介绍 ：获取冲账修改凭证列表数据。<br>
	 * <b>日期：</b>2017年6月19日<br>
	 * @version 1.0
	 * @author liyh
	 */
 	@Transactional(readOnly = true)
	public DataTablesOutput getEditRushFcAccbookVoList(DataTablesInput input, String resource, String userId) {
		input.addFilter(MatchTypeEnum.EQ, "busOriType", FinaBookConstant.BusOriType.RUSH.get());//冲账凭证
		input.addFilter(MatchTypeEnum.EQ, "redblueSign", FinacialConstant.RB_SIGN_R);//冲销凭证
		input.addFilter(MatchTypeEnum.EQ, "operatorCode", userId);//是记账人
		// 状态记账打回、暂存、待复核
		input.addFilter(MatchTypeEnum.IN, "voucherState",
						FinaBookConstant.VoucherStatus.STATUS_BACK.get() + "," +
								FinaBookConstant.VoucherStatus.STATUS_TEMP.get() + ","+
								FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get() );
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
		DataTablesOutput dataOut = fcAccbookDao.queryCanEditAccbooks(input, false, corpAuthSql);
		List<FcAccbookListVO> voList = dataOut.getData();
		List<String> ids = voList.stream().map(FcAccbookListVO::getVoucherType).collect(Collectors.toList());
		List<FcVoucherTypeDTO> dtos = iFcVoucherTypeService.getFcVoucherTypeByIds(ids);
		Map<String, String> map = dtos.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
		if (CollectionUtils.isNotEmpty(dtos)){
			voList.forEach(a -> {
				a.setVoucherName(map.get(a.getVoucherType()));
				a.setVoucherId(a.getVoucherType());
			});
		}
		for (FcAccbookListVO vo : voList) {
			FcAccbookVO accbook = fcAccbookDao.getAccbookVoById(vo.getOrigVoucherNo(),false);
			if(accbook==null){
				accbook = fcAccbookBlnedDao.getAccbookVoById(vo.getOrigVoucherNo(),false);
			}
			Date dayOverDate = dayCloseService.getDayCloseFinalDate(vo.getNetId());// 根据网点获取已日结日期
			if (dayOverDate.compareTo(accbook.getTallyDate()) < 0) {// 日结日期小于冲账原凭证记账日期
				vo.setIsOperation("N");//不能进行冲账操作
			} else {
				vo.setIsOperation("Y");
			}
		}
		return dataOut;
		//return fcAccbookDao.queryCanEditAccbooks(input);
	}

	/**
	 * 功能介绍 ：获取冲账复核凭证列表数据。<br>
	 * <b>日期：</b>2017年6月19日<br>
	 * @version 1.0
	 * @author liyh
	 */
 	@Transactional(readOnly = true)
	public DataTablesOutput getCheckRushFcAccbookVoList(DataTablesInput input, String resource, String userId) {
		input.addFilter(MatchTypeEnum.EQ, "busOriType", FinaBookConstant.BusOriType.RUSH.get());//冲账凭证
		input.addFilter(MatchTypeEnum.EQ, "redblueSign", FinacialConstant.RB_SIGN_R);//冲销凭证
		input.addFilter(MatchTypeEnum.NE, "operatorCode", userId);//FIXME 不能是记账人

		// 状态记账打回、暂存、待复核
		input.addFilter(MatchTypeEnum.EQ, "voucherState", FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get());
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
		DataTablesOutput dataOut = fcAccbookDao.queryCanEditAccbooks(input, false, corpAuthSql);
		List<FcAccbookListVO> voList = dataOut.getData();
		List<String> ids = voList.stream().map(FcAccbookListVO::getVoucherType).collect(Collectors.toList());
		List<FcVoucherTypeDTO> dtos = iFcVoucherTypeService.getFcVoucherTypeByIds(ids);
		Map<String, String> map = dtos.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
		if (CollectionUtils.isNotEmpty(dtos)){
			voList.forEach(a -> {
				a.setVoucherName(map.get(a.getVoucherType()));
				a.setVoucherId(a.getVoucherType());
			});
		}
		for (FcAccbookListVO vo : voList) {
			FcAccbookVO accbook = fcAccbookDao.getAccbookVoById(vo.getOrigVoucherNo(),false);
			if(accbook==null){
				accbook = fcAccbookBlnedDao.getAccbookVoById(vo.getOrigVoucherNo(),false);
			}
			Date dayOverDate = dayCloseService.getDayCloseFinalDate(accbook.getNetId());// 根据网点获取已日结日期
			if (dayOverDate.compareTo(accbook.getTallyDate()) < 0) {// 日结日期小于冲账原凭证记账日期
				vo.setIsOperation("N");//不能进行冲账操作
			} else {
				vo.setIsOperation("Y");
			}
		}
		return dataOut;
		//return fcAccbookDao.queryCanEditAccbooks(input);
	}

	/**
	 * 功能介绍 : 获取页面显示凭证，附带已日结凭证相关信息。<br>
	 * <b>日期：</b>2017年6月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
 	@Transactional(readOnly = true)
	public FcAccbookPageVO getFcAccbookBlnedPageVOById(String id,boolean isPool) {
		FcAccbookPageVO pageVO = new FcAccbookPageVO();
		FcAccbookVO accbook = fcAccbookBlnedDao.getAccbookVoById(id,isPool);
		if (null == accbook){
			throw new ParameterException("凭证数据不存在,请做打回处理！");
		}
		if(FinaBookConstant.GenerateMode.DEPOSIT.get().equals(accbook.getGenerateMode())
				|| FinaBookConstant.GenerateMode.INTEREST.get().equals(accbook.getGenerateMode())
				|| FinaBookConstant.GenerateMode.CREDIT_BOOK.get().equals(accbook.getGenerateMode())){
			if(accbook.getAffixAmount() == null){
				accbook.setAffixAmount(0);
			}
		}
		BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(accbook.getCurCode());
		accbook.setCurName(btCurrencyDTO.getCurName());
		FcBusRaceDTO busRaceDTO = busRaceService.findFcBusRaceById(accbook.getBusRaceId());
		if(busRaceDTO!=null){
			accbook.setRaceName(busRaceDTO.getRaceName());
		}
		Map<String, String> map = userService.getUserNameByIds(Lists.newArrayList(accbook.getOperatorCode(), accbook.getCheckerCode()));
		accbook.setOperatorName(map.get(accbook.getOperatorCode()));
		accbook.setCheckerName(map.get(accbook.getCheckerCode()));
		pageVO.setAccbook(accbook);
		pageVO.setBookDtls(fcAccbookBlnedDao.getPageDtlsByBookId(id,isPool));
		//TODO 凭证附加信息
		FcAccbookBlnedExt accbookExt = bookBlnedExtService.getBookBlnedExtByBookId(id);
		FcAccbookExtVO extVO = new FcAccbookExtVO();
		if (null != accbookExt) {
			BeanUtils.copyProperties(accbookExt, extVO);
		}
		pageVO.setBookExt(extVO);
		return pageVO;
	}
	/**
	 * 功能介绍 : 获取页面显示凭证，附带已日结凭证相关信息。<br>
	 * <b>日期：</b>2017年6月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public FcAccbookPageVO getFcAccbookBlnedPageVOByVoucherNo(String voucherNo,boolean isPool) {
		FcAccbookPageVO pageVO = new FcAccbookPageVO();
		FcAccbookVO accbook = fcAccbookBlnedDao.getAccbookVoByVoucherNo(voucherNo, isPool);
		String voucherType = accbook.getVoucherType();
		FcVoucherTypeDTO dto = iFcVoucherTypeService.getFcVoucherTypeById(voucherType);
		if (ObjectUtils.isNotEmpty(dto) && dto.getVoucherName() != null) {
			accbook.setVoucherName(dto.getVoucherName());
		}
		if(FinaBookConstant.GenerateMode.DEPOSIT.get().equals(accbook.getGenerateMode())
				|| FinaBookConstant.GenerateMode.INTEREST.get().equals(accbook.getGenerateMode())
				|| FinaBookConstant.GenerateMode.CREDIT_BOOK.get().equals(accbook.getGenerateMode())){
			if(accbook.getAffixAmount() == null){
				accbook.setAffixAmount(0);
			}
		}
		pageVO.setAccbook(accbook);
		pageVO.setBookDtls(fcAccbookBlnedDao.getPageDtlsByBookId(pageVO.getAccbook().getId(),isPool));
		//TODO 凭证附加信息
		FcAccbookBlnedExt accbookExt = bookBlnedExtService.getBookBlnedExtByBookId(pageVO.getAccbook().getId());
		FcAccbookExtVO extVO = new FcAccbookExtVO();
		if (null != accbookExt) {
			BeanUtils.copyProperties(accbookExt, extVO);
		}
		pageVO.setBookExt(extVO);
		return pageVO;
	}

	/**
	 * 功能介绍 : 查询关联手续费凭证。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh
	 */
 	@Transactional(readOnly = true)
	public List findTargetChargeAccbooksBy(String bookId) {
		return fcAccbookBlnedDao.findTargetChargeAccbooksBy(bookId);
	}

	/**
	 * 数据校验
	 */
	public void checkAccbookBlnedData(FcAccbookVoucherVO voucherVO){
		FcAccbook accbook = voucherVO.getAccbook();
		if (EmptyUtils.isEmpty(accbook.getVoucherType().trim())) {//凭证种类
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHERTYPE_NO);
    	}
		if (EmptyUtils.isEmpty(accbook.getBusRaceId().trim())) {//业务种类
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_BUSRACE_NO);
		}
		if (EmptyUtils.isEmpty(accbook.getTallyDate())) {//记账日期
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_TALLYDATE_NO);
    	}
		if (EmptyUtils.isEmpty(accbook.getInterestStartDate())) {//起息日期
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_INTERESTSTARTDATE_NO);
    	}
		if (accbook.getInterestStartDate().compareTo(accbook.getTallyDate()) > 0) {
        	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_INTERESTSTART_AFTER);
        }
		if (EmptyUtils.isEmpty(accbook.getRmk().trim())) {//备注
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_RMK_NO);
    	}
		List<FcAccbookDtl> bookDtl = voucherVO.getBookDtls();//明细
		if(ListUtils.isNullOrEmpty(bookDtl)){
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
		}
		for (FcAccbookDtl dtl : bookDtl) {
			if (EmptyUtils.isEmpty(dtl.getSummaryContent())) {//摘要
				throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_SUMMARYCONTENT_NO);
	    	}
			if (EmptyUtils.isEmpty(dtl.getAccount())) {//帐号
				throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_NO);
	    	}
			BigDecimal debitMoney = dtl.getDebitMoney();
	        BigDecimal creditMoney = dtl.getCreditMoney();
	        if (debitMoney == null && creditMoney == null) {
	        	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_MONEY_BOTH_NO);
	        }
	        if (debitMoney != null && creditMoney != null) {
	            if (BigDecimal.ZERO.compareTo(debitMoney) == 0 && BigDecimal.ZERO.compareTo(creditMoney) == 0) {
	            	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_MONEY_BOTH_ZERO);
	            }
	        }
		}
	}

	/**
	 * 功能介绍 : 获取同一全局ID的所有已日结的账务信息。<br>
	 * <b>日期：</b>2017年11月8日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public List<FcAccbookBlned> getBooksByBusGlobalId(String busGlobalId) {
		return fcAccbookBlnedDao.getBooksByBusGlobalId(busGlobalId);
	}

	public String getRushAccbookState(String bookId) {
		FcAccbookVO accbookVO = fcAccbookDao.getAccbookVOByOrigVoucherNo(bookId);
		if (null == accbookVO) {//已日结
			accbookVO = fcAccbookBlnedDao.getAccbookVOByOrigVoucherNo(bookId);
			//判断冲账状态
			if (null != accbookVO) {
				if (FinaBookConstant.VoucherStatus.STATUS_CHECK.get().equals(accbookVO.getVoucherState())) {//复核通过
					return FinacialConstant.DEAL_FLAG_0;//已冲账处理
				}
			}
		} else {
			if (FinaBookConstant.VoucherStatus.STATUS_BACK.get().equals(accbookVO.getVoucherState())
					|| FinaBookConstant.VoucherStatus.STATUS_TEMP.get().equals(accbookVO.getVoucherState())
				|| FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get().equals(accbookVO.getVoucherState())) {
				return FinacialConstant.DEAL_FLAG_1;//冲账中
			} else if (FinaBookConstant.VoucherStatus.STATUS_CHECK.get().equals(accbookVO.getVoucherState())) {
				return FinacialConstant.DEAL_FLAG_0;//冲账已处理
			}
		}
		return FinacialConstant.DEAL_FLAG_3;//可冲账处理
	}

	/**
	 * 功能介绍 : 查询凭证冲账或者作废状态。<br>
	 * <b>日期：</b>2017年11月9日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public VoucherWrapperDTO getVoucherByVoucherNo(String voucherNo) {
		VoucherWrapperDTO voucherDTO = new VoucherWrapperDTO();
		FcAccbookVO accbookVO = fcAccbookDao.getAccbookVOByVoucherNo(voucherNo);
		if (EmptyUtils.isEmpty(accbookVO)) {//日结前记录无
			accbookVO = fcAccbookBlnedDao.getAccbookVOByVoucherNo(voucherNo);//查询已日结记录
			if (!EmptyUtils.isEmpty(accbookVO)) {
				voucherDTO.setFlag(FinacialConstant.OVER_SIGN_YES);//已日结标识
				voucherDTO.setBusGlobalId(accbookVO.getBusGlobalId());//全局业务ID
				FcAccbookVoucherDTO voucher = new FcAccbookVoucherDTO();
				FcAccbookDTO accbookDTO = new FcAccbookDTO();
				BeanUtils.copyProperties(accbookVO, accbookDTO);
				FcVoucherTypeDTO voucherTypeDTO = voucherTypeService.getFcVoucherTypeById(accbookVO.getVoucherType());
				accbookDTO.setVoucherName(voucherTypeDTO.getVoucherName());//凭证种类名称
				voucher.setAccbook(accbookDTO);//账务主表数据

				String id = accbookVO.getId();
				List<FcAccbookBlnedDtl> accbookDtls = bookBlnedDtlservice.getAccbookDtlsByBookId(id);
				voucher.setBookDtls(BeanUtils.copyListProperties(accbookDtls, FcAccbookDtlDTO.class));//账务明细

				FcAccbookBlnedExt accbookExt = bookBlnedExtService.getBookBlnedExtByBookId(id);
				FcAccbookExtDTO accbookExtDTO = new FcAccbookExtDTO();
				if(!EmptyUtils.isEmpty(accbookExt)){
					BeanUtils.copyProperties(accbookExt, accbookExtDTO);
					voucher.setBookExt(accbookExtDTO);//账务外单位其他信息
				}
				voucherDTO.addTarget(voucher);
				return voucherDTO;
			}
		} else {
			voucherDTO.setFlag(FinacialConstant.OVER_SIGN_NO);//未日结标识
			voucherDTO.setBusGlobalId(accbookVO.getBusGlobalId());//全局业务ID

			FcAccbookVoucherDTO voucher = new FcAccbookVoucherDTO();
			FcAccbookDTO accbookDTO = new FcAccbookDTO();
			BeanUtils.copyProperties(accbookVO, accbookDTO);
			FcVoucherTypeDTO voucherTypeDTO = voucherTypeService.getFcVoucherTypeById(accbookVO.getVoucherType());
			accbookDTO.setVoucherName(voucherTypeDTO.getVoucherName());//凭证种类名称
			voucher.setAccbook(accbookDTO);//账务主表数据

			String id = accbookVO.getId();
			List<FcAccbookDtl> accbookDtls = bookDtlservice.getAccbookDtlsByBookId(id);
			voucher.setBookDtls(BeanUtils.copyListProperties(accbookDtls, FcAccbookDtlDTO.class));//账务明细

			FcAccbookExt accbookExt = bookExtservice.getBookExtByBookId(id);
			FcAccbookExtDTO accbookExtDTO = new FcAccbookExtDTO();
			if(!EmptyUtils.isEmpty(accbookExt)){
				BeanUtils.copyProperties(accbookExt, accbookExtDTO);
				voucher.setBookExt(accbookExtDTO);//账务外单位其他信息
			}
			voucherDTO.addTarget(voucher);
			return voucherDTO;
		}
		return voucherDTO;
	}

	/**
	 * 功能介绍 : 查询凭证冲账或者作废状态。<br>
	 * <b>日期：</b>2017年11月9日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public FcAccbookCloseDTO geAccbookCloseDTOByBillId(String billId, Integer busOriType) {
		FcAccbookCloseDTO closeDTO = new FcAccbookCloseDTO();
		FcAccbookVO accbookVO = fcAccbookDao.getAllAccbookVOByBillId(billId);
		if (EmptyUtils.isEmpty(accbookVO)) {//日结前记录无
			accbookVO = fcAccbookBlnedDao.getAllAccbookVOByBillId(billId);//查询已日结记录
			if (!EmptyUtils.isEmpty(accbookVO)) {
				closeDTO.setCloseFlag(true);//已日结
				String bookId = accbookVO.getId();//冲账凭证ID
				if (FinacialConstant.RUSH_SIGN_NO.equals(accbookVO.getRushSign())) {//未冲账
					//判断未冲账是否已作废
					if (FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get().equals(accbookVO.getVoucherState())) {
						closeDTO.setDealFlag(FinacialConstant.DEAL_FLAG_0);//作废已处理
					} else {
						closeDTO.setDealFlag(FinacialConstant.DEAL_FLAG_3);//可冲账处理
					}
				} else {
					closeDTO.setDealFlag(getRushAccbookState(bookId));//设置冲账状态
				}
			}
		} else {
			closeDTO.setCloseFlag(false);
			if (FinaBookConstant.VoucherStatus.STATUS_INVALID_BACK.get().equals(accbookVO.getVoucherState())
					|| FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get().equals(accbookVO.getVoucherState())
				|| FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get().equals(accbookVO.getVoucherState())) {
				closeDTO.setDealFlag(FinacialConstant.DEAL_FLAG_1);//作废中
			} else if (FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get().equals(accbookVO.getVoucherState())) {
				closeDTO.setDealFlag(FinacialConstant.DEAL_FLAG_0);//作废已处理
			} else {
				closeDTO.setDealFlag(FinacialConstant.DEAL_FLAG_3);//可作废处理
			}
		}
		if (!EmptyUtils.isEmpty(accbookVO)) {
			closeDTO.setBookId(accbookVO.getId());
			closeDTO.setBusGlobalId(accbookVO.getBusGlobalId());//全局ID
			closeDTO.setTallyDate(accbookVO.getTallyDate());//记账日期
			String netId = accbookVO.getNetId();
			Date tallyDate = dayCloseService.getDayCloseFinalDate(netId);
			closeDTO.setCloseDate(tallyDate);//已日结日期
		}
		return closeDTO;
	}
	/**
	 * 作废前校验凭证状态
	 * @param pageType
	 * @param bookId
	 */
	public void validVoucher(String pageType, String bookId, String validType) {
		if (EmptyUtils.isEmpty(bookId)) {
			return;
		}
		//检查凭证状态，非暂存、打回、待复核状态不允许修改保存或提交
		if (FinacialConstant.PAGE_INIT.equals(pageType)) {//冲账提交
			FcAccbook accbook = null;
			if (CommonConstant.VALID_SIGN_YES.equals(validType)) {//校验数据
				accbook = fcAccbookBlnedDao.getAccbookById(bookId);
			} else {
				accbook = fcAccbookBlnedDao.getAccbookByBusGlobalId(bookId);
			}
			if (EmptyUtils.isEmpty(accbook)) {
				throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE);//状态已改变，刷新页面重试
			}
			FcAccbookUtils.validVoucherStatus(accbook.getRushSign(), new String[]{FinacialConstant.RUSH_SIGN_N});
		}else {
			FcAccbook accbook = null;
			if (CommonConstant.VALID_SIGN_YES.equals(validType)) {//校验数据
				accbook = fcAccbookDao.getAccbookById(bookId);
			} else {
				accbook = fcAccbookDao.getAccbookByBusGlobalId(bookId);
			}
			if (EmptyUtils.isEmpty(accbook)) {
				throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE);//状态已改变，刷新页面重试
			}
			String voucherStatus = accbook.getVoucherState();
			if (FinacialConstant.PAGE_EDIT.equals(pageType)) {//冲账修改
				FcAccbookUtils.validVoucherStatus(voucherStatus, new String[]{FinaBookConstant.VoucherStatus.STATUS_BACK.get(),
						FinaBookConstant.VoucherStatus.STATUS_TEMP.get(), FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get()});
			} else if (FinacialConstant.PAGE_CHECK.equals(pageType)) {//冲账复核
				FcAccbookUtils.validVoucherStatus(voucherStatus, new String[]{FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get()});
			}
		}
	}
	/**
	 * 更新是否修改过现金流项目
	 */
	public void updateChangeCashflux(String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		fcAccbookBlnedDao.updateChangeCashflux(params);
	}
	/**
	 * 对账查询日结后账务数据 用于中心对账
	 * @param dataTablesInput
	 * @param accList
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput listGenAcc(DataTablesInput dataTablesInput, List<String> accList) {
		return fcAccbookBlnedDao.listGenAcc(dataTablesInput,accList);
	}
	/**
	 * 内部账号凭证保存为对账单数据
	 * @param params
	 * @param accList
	 * @param orderNos
	 * @return
	 */
	public List<FcCompareAccDTO> queryCompareAcc(Map<String,Object> params, List<String> accList, List<String> orderNos){
		return fcAccbookBlnedDao.queryCompareAcc(params,accList,orderNos);
	}
	public FcAccbookDTO queryCompareAcc(FcAccbookDTO dto){
		return fcAccbookBlnedDao.queryCompareAcc(dto);
	}

	@Transactional(readOnly = true)
	public List<FcCompareAccDTO> getAccbookDtlsByOrigVoucherNo(String origVoucherNo){
		return fcAccbookBlnedDao.getAccbookDtlsByOrigVoucherNo(origVoucherNo);
	}
}
