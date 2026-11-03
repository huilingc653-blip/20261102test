package com.bytter.bill.domain.bill.service;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbEmitBillSupplyVo;
import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import com.bytter.bill.domain.bill.vo.CmbBailAccountInfoVo;
import com.bytter.bill.domain.bill.vo.CmbcBailAccountInfoVo;
import com.bytter.biz.payment.api.service.IBtBankAccHandlePaymentService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondDetailService;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 票据保证金挂接服务类
 * @author zhenghj
 *
 */
@Service("abFcBondHookService")
@Transactional
public class AbFcBondHookService extends BaseService<String, AbAccrecords> {
	@Resource(name = "fcBondHookServiceImp")
	private IFcBondHookService IFcBondHookService;
	@Autowired
	private IFcBondDetailService fcBondDetailService;
	@DubboReference(check = false)
	private IBtBankAccHandlePaymentService iBtBankAccHandlePaymentService;
	@DubboReference(check = false)
	private IInputBankInfoService iInputBankInfoService;
	@DubboReference(check = false)
	private IBisBifInitService iBisBifInitService;

	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbBtBankAccService abBtBankAccService;
	@Autowired
	private AbEmitBillService abEmitBillService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 是否挂保证金
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isBail(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBAIL).toBoolean(true);
	}
	/**
	 * 挂保证金银行类别控制 0银行行号 1银行类别
	 * @return
	 */
	@Transactional(readOnly = true)
	public String bailBankControlType(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_BAILBANKCONTROLTYPE).getValue();
	}
	/**
	 * 开票登记新增保证金占用
	 * @param abEmitBillVo
	 * @param user
	 * @param status
	 */
	public void addBailByRegister(AbEmitBillVo abEmitBillVo, ICurrentUser user, Integer status) {
		if(FcBondConstant.IS_HOOK_Y.equals(abEmitBillVo.getBailmodel())){
			//如不存在保证金额度单，需新增保证金额度单
			if(EmptyUtils.isEmpty(abEmitBillVo.getBondId())){
				String bailType = null;
				Date mutDate = null;
				//如果不存在类型为保证金的有效银行账户，需开户
				BtBankAccDTO bankAccDTO = abBtBankAccService.getBankAccInfo(abEmitBillVo.getBailAcc());
				if(EmptyUtils.isEmpty(bankAccDTO) || EmptyUtils.isEmpty(bankAccDTO.getAccType()) || !bankAccDTO.getAccType().equals(BtBankAccConstant.ACCTYPE_04)){
					com.bytter.biz.payment.api.dto.BtBankAccDTO btBankAccDTO = new com.bytter.biz.payment.api.dto.BtBankAccDTO();
					BtBankAccDTO btBankAccDTO1 = abBtBankAccService.getBankAccInfo(abEmitBillVo.getPayeracc());
					if(!EmptyUtils.isEmpty(btBankAccDTO1)){
						btBankAccDTO.setProv(btBankAccDTO1.getProv());
						btBankAccDTO.setCity(btBankAccDTO1.getCity());
						btBankAccDTO.setAccName(btBankAccDTO1.getAccName());
						btBankAccDTO.setBankTypeId(btBankAccDTO1.getBankTypeId());
						btBankAccDTO.setBankCode(btBankAccDTO1.getBankCode());
						btBankAccDTO.setBankName(btBankAccDTO1.getBankName());
						btBankAccDTO.setBifCode(btBankAccDTO1.getBifCode());
					}

					btBankAccDTO.setCorpId(abEmitBillVo.getCorpId());
					btBankAccDTO.setBankAcc(abEmitBillVo.getBailAcc());
					btBankAccDTO.setRegDate(abEmitBillVo.getStartDate());
					//招行保证金列表选择的数据处理
					if(AccebillConstant.CMB_BAIL_FLAG.equals(abEmitBillVo.getCmbBailFlag()) || AccebillConstant.CMBC_BAIL_FLAG.equals(abEmitBillVo.getCmbBailFlag())){
						if(AccebillConstant.CMB_BAIL_FLAG.equals(abEmitBillVo.getCmbBailFlag())){
							CmbBailAccountInfoVo cmbBailAccountInfoVo = abEmitBillService.getZhBondByAcc(abEmitBillVo.getBailAcc());
							bailType = cmbBailAccountInfoVo.getHmgtyp();
							mutDate = cmbBailAccountInfoVo.getMutdat();
							btBankAccDTO.setAccName(cmbBailAccountInfoVo.getAccnam());
							btBankAccDTO.setRegDate(cmbBailAccountInfoVo.getOpndat());
							btBankAccDTO.setEndDate(cmbBailAccountInfoVo.getMutdat());
						}
						if(AccebillConstant.CMBC_BAIL_FLAG.equals(abEmitBillVo.getCmbBailFlag())){
							CmbcBailAccountInfoVo cmbBailAccountInfoVo = abEmitBillService.getMsBondByAcc(abEmitBillVo.getBailAcc());
							bailType = cmbBailAccountInfoVo.getTimTypeValue();
							mutDate = cmbBailAccountInfoVo.getExpireDate();
							btBankAccDTO.setAccName(cmbBailAccountInfoVo.getAcctName());
							btBankAccDTO.setRegDate(cmbBailAccountInfoVo.getOpenDate());
							btBankAccDTO.setEndDate(cmbBailAccountInfoVo.getExpireDate());
						}
						InputBankInfoDTO inputBankInfoDTO = iInputBankInfoService.getByBankCode(abEmitBillVo.getAcceptorbankno());
						btBankAccDTO.setProv(inputBankInfoDTO.getProv());
						btBankAccDTO.setCity(inputBankInfoDTO.getCity());
						btBankAccDTO.setBankTypeId(inputBankInfoDTO.getBankTypeId());
						btBankAccDTO.setBankCode(abEmitBillVo.getAcceptorbankno());
						btBankAccDTO.setBankName(abEmitBillVo.getAcceptorBankName());
						Map<String, BisBifInitDTO> bisBifInitBankTypeIdMap = iBisBifInitService.getBisBifInitBankTypeIdMap();
						BisBifInitDTO bisBifInitDTO = bisBifInitBankTypeIdMap.get(inputBankInfoDTO.getBankTypeId());
						btBankAccDTO.setBifCode(bisBifInitDTO!=null?bisBifInitDTO.getId():null);
					}
					btBankAccDTO = iBtBankAccHandlePaymentService.saveBtBankAcc(btBankAccDTO, user);

					if(EmptyUtils.isEmpty(bankAccDTO)){
						bankAccDTO = new BtBankAccDTO();
					}
					bankAccDTO.setId(btBankAccDTO.getId());
					bankAccDTO.setCurList(Arrays.asList(btBankAccDTO.getCurId()));
				}
				//根据保证金帐号查询保证金信息
				FcBondDetailVO fcBondDetailVO = fcBondDetailService.getFcBondByBankAcc(abEmitBillVo.getBailAcc());
				if(fcBondDetailVO!=null){
					//同一批导入保证金帐号已存入需汇总到一笔
					fcBondDetailService.updateExistFcBondDetail(fcBondDetailVO,abEmitBillVo.getBailmoney(),user);
					abEmitBillVo.setBondId(fcBondDetailVO.getId());
				}else{
					FcBondDetailVO fcBond = new FcBondDetailVO();
					fcBond.setBankAccId(bankAccDTO.getId());
					fcBond.setIsPayed("1");
					fcBond.setCorpId(abEmitBillVo.getCorpId());
					fcBond.setBankAcc(abEmitBillVo.getBailAcc());
					fcBond.setCurId(bankAccDTO.getCurList().get(0));
					fcBond.setStartDate(abEmitBillVo.getStartDate());
					fcBond.setBondMoney(abEmitBillVo.getBailmoney());
					if(StringUtils.isNotEmpty(bailType)){
						fcBond.setIsRegular(bailType);
						fcBond.setEndDate(mutDate);
					}else{
						fcBond.setIsRegular("0");
					}
					fcBond.setInterestRate(abEmitBillVo.getBailrate());
					fcBond = fcBondDetailService.addFcBondDetail(fcBond, user); //保证金管理 保证金额度
					abEmitBillVo.setBondId(fcBond.getId());
				}
			}else{
				//付款办理自开票据及开票登记选择招行保证金列表时存在保证金帐号直接追加保证金
				if("0".equals(abEmitBillVo.getIsImport()) || (AccebillConstant.CMB_BAIL_FLAG.equals(abEmitBillVo.getCmbBailFlag()) || AccebillConstant.CMBC_BAIL_FLAG.equals(abEmitBillVo.getCmbBailFlag()) )){
					FcBondDetailVO fcBond = fcBondDetailService.getFcBondById(abEmitBillVo.getBondId());
					fcBondDetailService.addFcBondAddData(fcBond,abEmitBillVo.getBailmoney(),user);
				}
			}
		}

		FcBondApplyDTO dto = null;
		if(IFcBondHookService !=null){
			dto= IFcBondHookService.getFcBondApplyByItemIds(abEmitBillVo.getVvid(), abEmitBillVo.getAppNo());
		}
		//不存在保证金挂接数据且系统参数为挂保证金且页面是否挂保证金为是且选择保证金额度单的保证金id不为空，新增挂接保证金数据
		if (dto == null
				&& isBail()
				&& FcBondConstant.IS_HOOK_Y.equals(abEmitBillVo.getBailmodel()) ) {
			FcBondApplyDTO appDto = setFcBondApplyDTO(abEmitBillVo,user,status);
			IFcBondHookService.addFcBondApply(appDto);
			//存在保证金挂接数据且系统参数为挂保证金
		}else if(dto != null){
			FcBondApplyDTO appDto = setFcBondApplyDTO(abEmitBillVo,user,status);
			//页面是否挂保证金为否时则修改挂接保证金业务状态为已删除
			if( FcBondConstant.IS_HOOK_N.equals(abEmitBillVo.getBailmodel())) {
				dto.setStatus(FcBondConstant.STATUE_RELEASE);
				IFcBondHookService.updateFcBondApply(dto);
			}
			//页面是否挂保证金为是时且选择保证金额度单的保证金id不为空时，更新保证金数据
			if(FcBondConstant.IS_HOOK_Y.equals(abEmitBillVo.getBailmodel()) && !StringUtils.isEmpty(abEmitBillVo.getBondId())) {
				if(abEmitBillVo.getBondId().equals(dto.getBondId())) {
					appDto.setId(dto.getId());
					appDto.setBillNo(dto.getBillNo());
					IFcBondHookService.updateFcBondApply(appDto);
				}else{
					dto.setStatus(FcBondConstant.STATUE_RELEASE);
					IFcBondHookService.updateFcBondApply(dto);
					FcBondApplyDTO appDto2 = setFcBondApplyDTO(abEmitBillVo,user,status);
					IFcBondHookService.addFcBondApply(appDto2);
				}
			}
		}
	}
	/**
	 * 开票补录新增或修改保证金占用
	 * @param abEmitBillSupplyVo
	 * @param user
	 * @param status
	 */
	public void addOrUpateBailBySupply(AbEmitBillSupplyVo abEmitBillSupplyVo, ICurrentUser user, Integer status) {
		FcBondApplyDTO dto = null;
		if(IFcBondHookService !=null){
			dto = IFcBondHookService.getFcBondApplyByItemIds(abEmitBillSupplyVo.getVvid(), abEmitBillSupplyVo.getAppNo());
		}
		//不存在保证金挂接数据且系统参数为挂保证金且页面是否挂保证金为是且选择保证金额度单的保证金id不为空，新增挂接保证金数据
		if (dto == null && isBail() && FcBondConstant.IS_HOOK_Y.equals(abEmitBillSupplyVo.getBailmodel()) && !StringUtils.isEmpty(abEmitBillSupplyVo.getBondId())) {
			FcBondApplyDTO appDto = setFcBondApplyDTO(abEmitBillSupplyVo,user,status);
			IFcBondHookService.addFcBondApply(appDto);
			//存在保证金挂接数据且系统参数为挂保证金
		}else if(dto != null){
			FcBondApplyDTO appDto = setFcBondApplyDTO(abEmitBillSupplyVo,user,status);
			//页面是否挂保证金为否时则修改挂接保证金业务状态为已删除
			if( FcBondConstant.IS_HOOK_N.equals(abEmitBillSupplyVo.getBailmodel())) {
				dto.setStatus(FcBondConstant.STATUE_RELEASE);
				IFcBondHookService.updateFcBondApply(dto);
			}
			//页面是否挂保证金为是时且选择保证金额度单的保证金id不为空时，更新保证金数据
			if( FcBondConstant.IS_HOOK_Y.equals(abEmitBillSupplyVo.getBailmodel()) && !StringUtils.isEmpty(abEmitBillSupplyVo.getBondId())) {
				if(abEmitBillSupplyVo.getBondId().equals(dto.getBondId())) {
					appDto.setId(dto.getId());
					appDto.setBillNo(dto.getBillNo());
					IFcBondHookService.updateFcBondApply(appDto);
				}else{
					dto.setStatus(FcBondConstant.STATUE_RELEASE);
					IFcBondHookService.updateFcBondApply(dto);
					FcBondApplyDTO appDto2 = setFcBondApplyDTO(abEmitBillSupplyVo,user,status);
					IFcBondHookService.addFcBondApply(appDto2);
				}
			}
		}
	}
	/**
	 * 修改保证金状态
	 * @param abVoucherinfoVice
	 * @param user
	 * @param status
	 */
	public void updateFcBondApply(AbVoucherinfoVice abVoucherinfoVice , ICurrentUser user, Integer status){
		FcBondApplyDTO dto = null;
		if(IFcBondHookService !=null){
			dto = IFcBondHookService.getFcBondApplyByItemIds(abVoucherinfoVice.getId(), abVoucherinfoVice.getAppNo());
		}
		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abVoucherinfoVice.getVid(), true);
		if(dto != null){
			if(AccebillConstant.CMB_BAIL_FLAG.equals(abVoucherinfo.getCmbBailFlag()) || AccebillConstant.CMBC_BAIL_FLAG.equals(abVoucherinfo.getCmbBailFlag())){
				//招行关联保证金列表开票的存入和账户删除
				IFcBondHookService.deleteFcBondByCmbBailFlag(dto,user);
			}else{
				dto.setStatus(status);
				dto.setUpdateBy(user.getLoginName());//当前登录名
				dto.setUpdateDate(DateUtils.getDate());//当前时间
				IFcBondHookService.updateFcBondApply(dto);
			}
		}
	}
	/**
	 * 释放保证金
	 * @param abVoucherinfoVice
	 * @param user
	 */
	public void freedFcBondApply(AbVoucherinfoVice abVoucherinfoVice , ICurrentUser user){
		FcBondApplyDTO dto = null;
		if(IFcBondHookService !=null){
			dto = IFcBondHookService.getFcBondApplyByItemIds(abVoucherinfoVice.getId(), abVoucherinfoVice.getAppNo());
		}
		if(dto != null){
			if(FcBondConstant.STATUE_TEMP.equals(dto.getStatus()) || FcBondConstant.STATUE_BACK.equals(dto.getStatus()) ){
				//暂存数据则直接修改成删除状态
				dto.setStatus(FcBondConstant.STATUE_DEL);
			}else if(FcBondConstant.STATUE_APPLY.equals(dto.getStatus())){
				//占用状态则修改成释放状态
				dto.setStatus(FcBondConstant.STATUE_RELEASE);
			}
			dto.setUpdateBy(user.getLoginName());//当前登录名
			dto.setUpdateDate(DateUtils.getDate());//当前时间
			IFcBondHookService.updateFcBondApply(dto);
		}
	}
	/**
	 * 根据项目id和项目单号查询项目占用的保证金信息
	 * @param itemId
	 * @param itemNo
	 * @return
	 */
	public FcBondApplyDTO getFcBondApplyByItemIds(String itemId, String itemNo){
		if(IFcBondHookService ==null){
			return null;
		}
		return IFcBondHookService.getFcBondApplyByItemIds(itemId, itemNo);
	}
	/**
	 * 获取是否存在挂保证金信息
	 * @param itemId
	 * @param itemNo
	 * @return
	 */
	public boolean getIsFcBondApplyByItemIds(String itemId,String itemNo){
		FcBondApplyDTO fcBondApplyDTO = getFcBondApplyByItemIds(itemId,itemNo);
		if(fcBondApplyDTO!=null){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 设置FcBondApplyDTO各属性值
	 * @param abEmitBillVo 票据登记vo
	 * @param user 用户
	 * @param status 状态
	 * @return
	 */
	public FcBondApplyDTO setFcBondApplyDTO(AbEmitBillVo abEmitBillVo, ICurrentUser user, Integer status){
		FcBondApplyDTO dto = new FcBondApplyDTO();
		//占用类型0:业务占用，1手工占用
		dto.setApplyType(FcBondConstant.APPLY_TYPE_BUSINESS);
		//0银行授信1银行借款2承兑汇票
		dto.setItemType(FcBondConstant.ITEM_TYPE_BILL);
		//项目单号
		dto.setItemNo(abEmitBillVo.getAppNo());
		//占用保证金的项目Id
		dto.setItemId(abEmitBillVo.getVvid());
		//单位id
		dto.setCorpId(abEmitBillVo.getCorpId());
		//联行号
		dto.setBankId(abEmitBillVo.getAcceptorbankno());
		dto.setBankName(abEmitBillVo.getAcceptorBankName());
		//项目金额
		dto.setItemMoney(abEmitBillVo.getBillMoney());
		//保证金比例
		dto.setBondPercent(abEmitBillVo.getBailpercent());
		//项目开始时间
		dto.setStartDate(abEmitBillVo.getStartDate());
		//项目结束时间
		dto.setEndDate(abEmitBillVo.getEndDate());
		//项目期限
		//dto.setItemUnit(bkrationDTO.getItemUnit());
		//保证金合同
		dto.setBondContract(abEmitBillVo.getBailContract());
		//保证金id
		dto.setBondId(abEmitBillVo.getBondId());
		//保证金金额,即占用金额
		dto.setApplyMoney(abEmitBillVo.getBailmoney());
		//备注
		dto.setRemark(abEmitBillVo.getRmk());
		//0暂存,11待审批,-1打回,95占用,-2删除,2已释放
		dto.setStatus(status);
		//创建人登录名称
		dto.setCreateBy(user.getLoginName());
		//保证金账号
		dto.setBankAcc(abEmitBillVo.getBailAcc());
		//保证金账号
		dto.setAccName(abEmitBillVo.getPayer());
		//创建日期
		dto.setCreateDate(DateUtils.getDate());
		return dto;
	}
	/**
	 * 设置FcBondApplyDTO各属性值
	 * @param abEmitBillSupplyVo 票据补录vo
	 * @param user 用户
	 * @param status 状态
	 * @return
	 */
	public FcBondApplyDTO setFcBondApplyDTO(AbEmitBillSupplyVo abEmitBillSupplyVo, ICurrentUser user, Integer status){
		FcBondApplyDTO dto = new FcBondApplyDTO();
		//占用类型0:业务占用，1手工占用
		dto.setApplyType(FcBondConstant.APPLY_TYPE_BUSINESS);
		//0银行授信1银行借款2承兑汇票
		dto.setItemType(FcBondConstant.ITEM_TYPE_BILL);
		//项目单号
		dto.setItemNo(abEmitBillSupplyVo.getAppNo());
		//占用保证金的项目Id
		dto.setItemId(abEmitBillSupplyVo.getVvid());
		//单位id
		dto.setCorpId(abEmitBillSupplyVo.getCorpId());
		//联行号
		dto.setBankId(abEmitBillSupplyVo.getAcceptorbankno());
		//项目金额
		dto.setItemMoney(abEmitBillSupplyVo.getBillMoney());
		//保证金比例
		dto.setBondPercent(abEmitBillSupplyVo.getBailpercent());
		//项目开始时间
		dto.setStartDate(abEmitBillSupplyVo.getStartDate());
		//项目结束时间
		dto.setEndDate(abEmitBillSupplyVo.getEndDate());
		//项目期限
		//dto.setItemUnit(bkrationDTO.getItemUnit());
		//保证金合同
		dto.setBondContract(abEmitBillSupplyVo.getBailContract());
		//保证金id
		dto.setBondId(abEmitBillSupplyVo.getBondId());
		//保证金金额,即占用金额
		dto.setApplyMoney(abEmitBillSupplyVo.getBailmoney());
		//备注
		dto.setRemark(abEmitBillSupplyVo.getRmk());
		//0暂存,11待审批,-1打回,95占用,-2删除,2已释放
		dto.setStatus(status);
		//创建人登录名称
		dto.setCreateBy(user.getLoginName());
		//创建日期
		dto.setCreateDate(DateUtils.getDate());
		return dto;
	}

	/**
	 * 根据保证金账号查询保证金信息
	 *
	 * @param bondBankAcc 保证金账号
	 */
	public FcBondDTO getFcBondMsgByBondBankAcc(String bondBankAcc){
		return IFcBondHookService.getFcBondMsgByBondBankAcc(bondBankAcc);
	}
}
