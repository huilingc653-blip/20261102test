package com.bytter.guarantee.bond.service.impl;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.AllocationConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.bond.entity.FcBond;
import com.bytter.guarantee.bond.entity.FcBondApply;
import com.bytter.guarantee.bond.entity.FcBondBal;
import com.bytter.guarantee.bond.service.FcBondAddService;
import com.bytter.guarantee.bond.service.FcBondApplyService;
import com.bytter.guarantee.bond.service.FcBondBalService;
import com.bytter.guarantee.bond.service.FcBondDetailService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.system.api.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * @author fusb
 *	@version 1.0
 *  @Title FcBondHookServiceImp 
 *  @Description 挂接保证金FcBondHookService接口的实现类
 *  @date 2018年10月30日
 */
@Service("fcBondHookServiceImp")
@DubboService
public class FcBondHookServiceImp implements IFcBondHookService {
	private Logger logger= LoggerFactory.getLogger(FcBondHookServiceImp.class);
	@Autowired
	FcBondApplyService fcBondApplyService;
	@Autowired
	FcBondBalService fcBondBalService;
	@Autowired
	FcBondDetailService fcBondService;
   @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
  	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private FcBondAddService fcBondAddService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;

	@Override
	public List<FcBondApplyDTO>  getFcBondApplyListByItemId(String itemId){
		return fcBondApplyService.getFcBondApplyListByItemId(itemId);
	}

	@Override
	public void addFcBondApply(FcBondApplyDTO dto) { 
		if(dto.getItemId()!=null){
			if(dto.getEndDate() != null && dto.getStartDate() != null) {
				Integer itemUnit = this.monthDiffs(dto.getEndDate(), dto.getStartDate());//计算项目期限
				dto.setItemUnit(itemUnit);//设置dto中项目期限为itemUnit
			}
			FcBondApply fcBondApply = new FcBondApply();
			BeanUtils.copyNotNullProperties(dto, fcBondApply);
			fcBondApply.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_FCBOND_APPPLY));
			fcBondApplyService.saveFcBondApply(fcBondApply);
			//以下是操作保证金余额表,如果占用了保证金,那么余额表中相应的保证金在途额度累加
			FcBondBal fcBondBal = fcBondBalService.getBondBalById(dto.getBondId());//通过bondId得到相应的保证金余额信息
			BigDecimal applyMoney = fcBondApply.getApplyMoney();//得到当前的项目占用金额
			BigDecimal applyMoneyBal = fcBondBal.getApplyMoney();//得到余额表中保证金占用额度
			fcBondBal.setApplyMoney(applyMoneyBal.add(applyMoney));//更新保证金余额表中的占用额度
			fcBondBalService.updateFcBondBal(fcBondBal);//保存更新后的保证金余额表中的相应信息
		}
	}
	@Override
	public void updateFcBondApply(FcBondApplyDTO dto) {
		if(dto.getItemId()!=null){
			logger.error("dto.getStatus() = " + dto.getStatus().toString());
			if(FcBondConstant.STATUE_APPLY.equals(dto.getStatus())){
				logger.error("1111111");
				if(dto.getEndDate() != null && dto.getStartDate() != null) {
					Integer itemUnit = this.monthDiffs(dto.getEndDate(), dto.getStartDate());//计算项目期限
					dto.setItemUnit(itemUnit);//设置dto中项目期限为itemUnit
				}
				// 得到未更新前的相应保证金占用信息
				logger.error("222222222");
				FcBondApplyDTO fcBondApplyBefore = fcBondApplyService.getFcBondApplyByItemIds(dto.getItemId(),dto.getItemNo());
				logger.error("3333333");
				FcBondApply fcBondApplyAfter = new FcBondApply();// 用于接收修改后的保证金信息
				BeanUtils.copyNotNullProperties(dto, fcBondApplyAfter);// 接收修改后的保证金信息
				fcBondApplyService.updateFcBondApply(fcBondApplyAfter);// 更新
				logger.error("444444444");
				// 以下是操作保证金余额表,如果更新了占用保证金额度,那么余额表中相应的保证金占用额度也进行修改
				//更新前后保证金的bondId不一致,则还需要更新原bondId对应的保证金余额表中的信息
				if(fcBondApplyBefore!=null){
					if(!(fcBondApplyBefore.getBondId().equals(fcBondApplyAfter.getBondId()))&&StringUtils.isNotEmpty(fcBondApplyBefore.getBondId())){
						logger.error("5555555");
						//bondId不一致,则更新前后占用的不是同一笔保证金,原bondId对应的余额表中的占用额度应该减去相应的项目占用金额
						FcBondBal fcBondBalBefore = fcBondBalService.getBondBalById(fcBondApplyBefore.getBondId());// 通过bondId得到相应的保证金余额信息
						BigDecimal applyMoneyBefore = fcBondApplyBefore.getApplyMoney();//得到原来占用的保证金金额
						BigDecimal applyMoneyBalBefore = fcBondBalBefore.getApplyMoney();//得到原bondId对应的在途金额
						BigDecimal balance = applyMoneyBalBefore.subtract(applyMoneyBefore);//余额表中占用的额度减去原来项目占用的保证金金额
						fcBondBalBefore.setApplyMoney(balance);//更新余额表中的占用额度
						fcBondBalService.updateFcBondBal(fcBondBalBefore);// 保存更新后的保证金余额表中的相应信息
						FcBondBal fcBondBalAfter = fcBondBalService.getBondBalById(fcBondApplyAfter.getBondId());// 通过bondId得到本次占用的保证金余额表信息
						BigDecimal applyMoneyBalAfter = fcBondBalAfter.getApplyMoney();//得到当前bondId对应的余额表中占用额度
						BigDecimal applyMoney = applyMoneyBalAfter.add(fcBondApplyAfter.getApplyMoney());//更新当前bondId对应的占用额度
						fcBondBalAfter.setApplyMoney(applyMoney);
						fcBondBalService.updateFcBondBal(fcBondBalAfter);// 保存更新后的保证金余额表中的相应信息
						//将原来占用的保证金信息改为释放状态
						FcBondApply fcBondApply = new FcBondApply();
						BeanUtils.copyNotNullProperties(fcBondApplyBefore, fcBondApply);
						fcBondApplyService.deleteFcBondApply(fcBondApply);//删除之前的占用数据
					}else{//更新前后bondId一致则执行下面代码
						logger.error("666666");
						FcBondBal fcBondBal = fcBondBalService.getBondBalById(fcBondApplyAfter.getBondId());// 通过bondId得到相应的保证金余额信息
						BigDecimal applyMoneyBalance = fcBondApplyAfter.getApplyMoney().subtract(fcBondApplyBefore.getApplyMoney());// 变更前后占用金额的差额
						BigDecimal applyMoney = fcBondBal.getApplyMoney();// 得到余额表中保证金在途占用额度
						fcBondBal.setApplyMoney(applyMoney.add(applyMoneyBalance));// 更新保证金余额表中的在途占用金额
						fcBondBalService.updateFcBondBal(fcBondBal);// 保存更新后的保证金余额表中的相应信息
					}
				}
			}
			if(FcBondConstant.STATUE_RELEASE.equals(dto.getStatus())){
				logger.error("this.statusIsRelease(dto)");
				this.statusIsRelease(dto);
			}
		}
	}

	@Override
	public void deleteFcBondByCmbBailFlag(FcBondApplyDTO dto, ICurrentUser user) {
		if(dto.getItemId()!=null){
			FcBondApply fcBondApply = fcBondApplyService.get(dto.getId());
			FcBond fcBond = fcBondService.get(dto.getBondId(), true);
			if(null != fcBondApply){
				//更新保证金占用状态为"删除"
				fcBondApply.setStatus(FcBondConstant.STATUE_DEL);
				fcBondApplyService.updateFcBondApply(fcBondApply);
				//通过bondId得到相应的保证金余额信息
				FcBondBal fcBondBal = fcBondBalService.getBondBalById(dto.getBondId());
				if(BigDecimal.ZERO.compareTo(fcBondBal.getAddMoney()) == 0){
					fcBondBal.setBondMoney(fcBondBal.getBondMoney().subtract(fcBondApply.getApplyMoney()));
					fcBondBal.setApplyMoney(fcBondBal.getApplyMoney().subtract(fcBondApply.getApplyMoney()));
				}else{
					//保证金追加金额大于或等于占用金额则修改追加余额,小于则修改存入金额
					if(fcBondBal.getAddMoney().compareTo(fcBondApply.getApplyMoney()) >= 0){
						fcBondBal.setAddMoney(fcBondBal.getAddMoney().subtract(fcBondApply.getApplyMoney()));
						fcBondBal.setApplyMoney(fcBondBal.getApplyMoney().subtract(fcBondApply.getApplyMoney()));
						//删除追加数据
						List<FcBondAddDTO> fcBondAddDTOS = fcBondAddService.queryAddHis(dto.getBondId());
						for(FcBondAddDTO fcBondAddDTO:fcBondAddDTOS){
							if(fcBondAddDTO.getAddMoney().compareTo(fcBondApply.getApplyMoney()) == 0){
								fcBondAddService.deleteBondAdd(fcBondAddDTO.getId(),user);
								break;
							}
						}
					}else{
						fcBondBal.setBondMoney(fcBondBal.getBondMoney().subtract(fcBondApply.getApplyMoney()));
						fcBondBal.setApplyMoney(fcBondBal.getApplyMoney().subtract(fcBondApply.getApplyMoney()));
						fcBond.setBondMoney(fcBond.getBondMoney().subtract(fcBondApply.getApplyMoney()));
						fcBondService.updateEntity(fcBond);
					}
				}
				fcBondBalService.updateFcBondBal(fcBondBal);
				if(BigDecimal.ZERO.compareTo(fcBondBal.getAddMoney()) == 0 && BigDecimal.ZERO.compareTo(fcBondBal.getBondMoney()) == 0){
					//删除该保证金存入信息
					fcBondService.deleteBond(dto.getBondId(),user);
					//删除该保证金账户数据
					btBankAccService.deleteZhBailAccount(fcBond.getBankAcc(),user);
				}
			}
		}
	}

	/**
	 * 保证金占用状态为已释放,更新保证金占用信息
	 */
	public void statusIsRelease(FcBondApplyDTO dto){
		if(dto.getItemId()!=null){
			FcBondApply fcBondApply = null;
			if (StringUtils.isNotEmpty(dto.getId())) {
				logger.error("777777");
				fcBondApply = fcBondApplyService.get(dto.getId());
				logger.error("8888888");
			} else {
				FcBondApplyDTO fcBondApplyDTO = fcBondApplyService.getFcBondApplyByItemIds(dto.getItemId(), dto.getItemNo());
				if (null != fcBondApplyDTO) {
					fcBondApply = new FcBondApply();
					BeanUtils.copyNotNullProperties(fcBondApplyDTO, fcBondApply);
				}
			}
			if (null != fcBondApply) {
				logger.error("9999999");
				fcBondApply.setStatus(FcBondConstant.STATUE_RELEASE);
				fcBondApply.setReleaseDate(DateUtils.getDate());
				logger.error("0000000");
				fcBondApplyService.updateFcBondApply(fcBondApply);//更新保证金占用状态为"已释放"
				logger.error("209");
				//以下是操作保证金余额表,如果项目占用金额已释放,那么余额表中相应的保证金在占用额度也进行修改
				FcBondBal fcBondBal = fcBondBalService.getBondBalById(dto.getBondId());//通过bondId得到相应的保证金余额信息
				logger.error("212");
				BigDecimal applyMoney = fcBondApply.getApplyMoney();//得到项目占用的保证金额度
				BigDecimal applyMoneyBal = fcBondBal.getApplyMoney();//得到余额表中保证金的占用额度
				//如果保证金占用信息状态为已释放,保证金余额表中的占用金额减去项目的占用金额
				fcBondBal.setApplyMoney(applyMoneyBal.subtract(applyMoney));//余额表中占用额度减去项目占用的保证金的额度
				logger.error("217");
				fcBondBalService.updateFcBondBal(fcBondBal);//保存更新后的保证金余额表中的相应信息
				logger.error("219");
			}
		}
	}
	
	/**
	 * 修改保证金占用信息的日期</br>
	 * 该方法用于授信延期,借款展期</br>
	 * @param dto
	 */
	@Override
	public void updateFcBondApplyDate(FcBondApplyDTO dto){
		Integer itemUnit = 0;
		if(dto.getEndDate() != null && dto.getStartDate() != null) {
			itemUnit = this.monthDiffs(dto.getEndDate(), dto.getStartDate());//计算项目期限
		}
		Date reDate = dto.getEndDate();
		String loginName = dto.getUpdateBy();
		Date updateDate = dto.getUpdateDate();
		List<FcBondApplyDTO> list = fcBondApplyService.getFcBondApplyListByItemId(dto.getItemId());
		for(FcBondApplyDTO dto1:list){
			dto1.setEndDate(reDate);
			dto1.setUpdateBy(loginName);
			dto1.setUpdateDate(updateDate);
			dto1.setItemUnit(itemUnit);
			FcBondApply fcBondApply = new FcBondApply();
			BeanUtils.copyNotNullProperties(dto1, fcBondApply);
			fcBondApplyService.updateFcBondApply(fcBondApply);
		}
	}
		
	/***
	 * 计算项目期限itemUnit，单位为月
	 * @param endDate 项目结束日期
	 * @param startDate 项目开始日期
	 * @return
	 */
	public int monthDiffs(Date endDate,Date startDate ){
		Calendar end = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		end.setTime(endDate);
		start.setTime(startDate);
		//相差的月数
	    int month = (end.get(Calendar.MONTH) + ( (end.get(Calendar.YEAR) - start.get(Calendar.YEAR))*12 ) - start.get(Calendar.MONTH));
		//int month = DateUtils.dateDiff('M', end, start);//相差的月数
		int startDays = DateUtils.getDayOfMonth(startDate);//项目开始日期的相应月份共有多少天
		int endDays = DateUtils.getDayOfMonth(endDate);//项目结束日期的相应月份共有多少天
		
		if(startDays== start.get(Calendar.DAY_OF_MONTH)){//开始日期是否是月末的最后一天
			if(endDays == end.get(Calendar.DAY_OF_MONTH)){//结束日期月末最后一天
				month++;
			}else{
				if(end.get(Calendar.DAY_OF_MONTH)>start.get(Calendar.DAY_OF_MONTH)){//结束日期当月天数 > 开始日期单月天数
					month++;
				}
			}
		}else{
			if(endDays==end.get(Calendar.DAY_OF_MONTH)){//结束日期月末最后一天
				month++;
			}else{
				if(end.get(Calendar.DAY_OF_MONTH)>start.get(Calendar.DAY_OF_MONTH)){
					month++;
				}
			}
		}
		return month;
	}
	@Override
	public FcBondApplyDTO getFcBondApplyByItemIds(String itemId, String itemNo) {
		FcBondApplyDTO fcBondApplyDTO =  fcBondApplyService.getFcBondApplyByItemIds(itemId,itemNo);
		if(fcBondApplyDTO!=null){
			fcBondApplyDTO.setApplyMoneyTotal(fcBondApplyService.getFcBondApplyMoneyByItemId(itemId));
		}
		if(EmptyUtils.isEmpty(fcBondApplyDTO)){
			return null;
		}else{
			return fcBondApplyDTO;
		}
	}
	/**
	 * 授信追加时更新项目金额</br>
	 */
	@Override
	public void updateFcBondItemMoney(FcBondApplyDTO dto) {
		FcBondApply fcBondApply = new FcBondApply();
		BeanUtils.copyNotNullProperties(dto,fcBondApply);
		fcBondApplyService.updateFcBondApply(fcBondApply);
	}
	@Override
	public FcBondApplyDTO getFcBondApply(String itemId,String itemNo,String bondId) {
		FcBondApplyDTO fcBondApplyDTO =  fcBondApplyService.getFcBondApply(itemId,itemNo,bondId);
		if(fcBondApplyDTO!=null){
			fcBondApplyDTO.setApplyMoneyTotal(fcBondApplyService.getFcBondApplyMoneyByItemId(itemId));
		}
		if(EmptyUtils.isEmpty(fcBondApplyDTO)){
			return null;
		}else{
			return fcBondApplyDTO;
		}
	}
    /**
     * 删除或释放保证金的方法，删除时传入占用状态的保证金，释放时传入释放状态的保证金
     * @param dto 保证金占用DTO
     */
	@Override
	public void updateFcBondApplyData(FcBondApplyDTO dto) {
		if(dto.getItemId()!=null){
			if(FcBondConstant.STATUE_APPLY.equals(dto.getStatus())){
				// 以下是操作保证金余额表,如果更新了占用保证金额度,那么余额表中相应的保证金占用额度也进行修改
				if(StringUtils.isNotEmpty(dto.getBondId())){
					FcBondBal fcBondBalBefore = fcBondBalService.getBondBalById(dto.getBondId());// 通过bondId得到相应的保证金余额信息
					BigDecimal applyMoneyBefore = dto.getApplyMoney();//得到原来占用的保证金金额
					BigDecimal applyMoneyBalBefore = fcBondBalBefore.getApplyMoney();//得到原bondId对应的在途金额
					BigDecimal balance = applyMoneyBalBefore.subtract(applyMoneyBefore);//余额表中占用的额度减去原来项目占用的保证金金额
					fcBondBalBefore.setApplyMoney(balance);//更新余额表中的占用额度
					fcBondBalService.updateFcBondBal(fcBondBalBefore);// 保存更新后的保证金余额表中的相应信息
					//将原来占用的保证金信息删除
					FcBondApply fcBondApply = fcBondApplyService.get(dto.getId());
					fcBondApplyService.deleteFcBondApply(fcBondApply);//删除之前的占用数据
				}
			}
			if(FcBondConstant.STATUE_RELEASE.equals(dto.getStatus())){
				this.statusIsRelease(dto);
			}
		}
	}
	@Override
	public FcBondDTO getFcBondMsgById(String id) {
		FcBondDetailVO fcBondDetailVO=fcBondService.getFcBondById(id);
		FcBondDTO fcBondDTO=new FcBondDTO();
		BeanUtils.copyNotNullProperties(fcBondDetailVO,fcBondDTO);
		return fcBondDTO;
	}

	/**
	 * 根据保证金账号查询保证金信息
	 *
	 * @param bondBankAcc 保证金账号
	 */
	@Override
	public FcBondDTO getFcBondMsgByBondBankAcc(String bondBankAcc){
		FcBondDetailVO fcBondDetailVO=fcBondService.getFcBondByBondBankAcc(bondBankAcc);
		FcBondDTO fcBondDTO=null;
		if(fcBondDetailVO!=null) {
			fcBondDTO=new FcBondDTO();
			BeanUtils.copyNotNullProperties(fcBondDetailVO, fcBondDTO);
		}
		return fcBondDTO;
	}

	public FcBondDetailVO getFcBondDetailById(String id) {
		return fcBondService.getFcBondById(id);
	}
	@Override
	public FcBondApplyDTO getFcBondByParams(FcBondApplyDTO fcBondApplyDTO) {
		FcBondApplyDTO fcBondDTO =  fcBondApplyService.getFcBondApplyByParams(fcBondApplyDTO);
		if(EmptyUtils.isEmpty(fcBondDTO)){
			return null;
		}else{
			return fcBondDTO;
		}
	}

	/**
	* <br>
	* <b>功能：</b> 业务单据删除则退回保证金存入和追加信息 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2020/8/18 0018 <br>
	*/
	@Override
	public void backBondMoney(String billFrom, String bondId, BigDecimal bondMoney, ICurrentUser user) {
		if(AllocationConstant.BILL_FROM_3.equals(billFrom)){
			fcBondService.tempBond(bondId, user);
			fcBondBalService.deleteByBondId(bondId);
		}else {
			String curBondId = fcBondAddService.tempAddBond(bondId, user);
			fcBondBalService.backBondAddMoney(curBondId,bondMoney);
		}
	}
	/**
	 * 获取挂接保证金的项目ID列表
	 * @return
	 */
	@Override
	public List<String> getItemIds() {
		return fcBondApplyService.getItemIds();
	}

	@Override
	public List<FcBondApplyDTO> getFcBondApplyByItemIdsType(List<String> itemIds, String itemType) {
		return fcBondApplyService.getFcBondApplyByItemIdsType(itemIds,itemType);
	}
}
