package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO;
import com.bytter.bill.api.bill.dto.CorpDTO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbAccbookInfoDao;
import com.bytter.bill.domain.bill.entity.AbAccbookInfo;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.InnerAccountConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 票据记账挂接服务类
 * @author zhengg
 *
 */
@Service("abAccbookService")
@Transactional
public class AbAccbookService extends BaseService<AbAccbookInfo, String> {
	@Autowired
	private AbAccbookInfoDao abAccbookInfoDao;
	@DubboReference(check = false)
	private IFcAccbookService fcAccbookService;
	@DubboReference(check = false)
	private ICorpService corpService;
	//fc_tally_remind_dtl IFcTallyRemindDtl
	@Autowired
	private AbFcBusinessRuleDefineService abFcBusinessRuleDefineService;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbInnerAccountService abInnerAccountService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbFcSubjectService abFcSubjectService;
	@Autowired
	private AbDiscountService abDiscountService;
	@Autowired
	private AbFcBusRaceService abFcBusRaceService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IFcAccbookService iFcAccbookService;
	@Override
	public BaseDao<AbAccbookInfo, String> getEntityDao() {
		return abAccbookInfoDao;
	}
	/**
	 * 新增记账或修改记账信息表
	 * @param recordInfo 流水信息
	 */
	public void addAbAccbookInfo(AbAccrecords recordInfo){
		addAbAccbookInfo(recordInfo, "1");
	}
	/**
	 * 新增记账或修改记账信息表
	 * @param recordInfo 流水信息
	 * @param isBack 是否可以打回 1可以打回0不可以
	 */
	public void addAbAccbookInfo(AbAccrecords recordInfo,String isBack){
		if (recordInfo != null && recordInfo.getBusOriType() != null
				&& StringUtils.isNotEmpty(recordInfo.getSysRaceCode())) {
			AbAccbookInfo accbookInfo = abAccbookInfoDao.getAbAccbookInfoByAid(recordInfo.getId());
			if (accbookInfo == null) {
				accbookInfo = new AbAccbookInfo();
				accbookInfo.setAid(recordInfo.getId());
				accbookInfo.setCreateDate(new Date());
			} else {
				accbookInfo.setUpdateDate(new Date());
			}
			accbookInfo.setBusOriType(Integer.valueOf(recordInfo.getBusOriType().getValue()));
			accbookInfo.setSysRaceCode(recordInfo.getSysRaceCode());
			accbookInfo.setCorpId(recordInfo.getCorpId());
			accbookInfo.setIsBack(isBack);
			List<String> corpIds = new ArrayList<>();
			if (accbookInfo.getCorpId() == null && recordInfo.getCorpInfo() != null) {
				accbookInfo.setCorpId(recordInfo.getCorpInfo().getId());
				corpIds.add(accbookInfo.getCorpId());
			}
			accbookInfo.setNetId(recordInfo.getNetId());
			if (ObjectUtil.isNotEmpty(recordInfo.getNetId())){
				corpIds.add(recordInfo.getNetId());
			}
			if (ObjectUtil.isNotEmpty(corpIds)){
				Map<String, com.bytter.system.api.dto.CorpDTO> listCorp = corpService.listCorpDtoByCorpIds(corpIds);
				if (ObjectUtil.isAllNotEmpty(accbookInfo.getCorpId())){
					com.bytter.system.api.dto.CorpDTO corpDTO = listCorp.get(accbookInfo.getCorpId());
					if (ObjectUtil.isNotEmpty(corpDTO)){
						accbookInfo.setCorpName(corpDTO.getName());
					}
				}
				if (ObjectUtil.isAllNotEmpty(accbookInfo.getNetId())){
					com.bytter.system.api.dto.CorpDTO corpDTO = listCorp.get(accbookInfo.getNetId());
					if (ObjectUtil.isNotEmpty(corpDTO)){
						accbookInfo.setNetName(corpDTO.getName());
					}
				}
			}
			abAccbookInfoDao.saveOrUpdate(accbookInfo);
		}
	}
	/**
	 * 挂记账返回true不挂返回false
	 * @return 返回是否记账标志
	 */
	public boolean isAccbook(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISACCBOOK).toBoolean(false);
	}
	/**
	 * 挂记账返回false不挂返回true
	 * @return 返回是否记账标志
	 */
	public boolean isNotAccbook(){
		return !isAccbook();
	}
	/**
	 * 系统自动记账返回true不挂返回false
	 * @return 返回是否记账标志
	 */
	public boolean isAutoAccbook(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISAUTOACCBOOK).toBoolean(false);
	}
	/**
	 * 系统自动记账返回false手工记账返回true
	 * @return 返回是否记账标志
	 */
	public boolean isNotAutoAccbook(){
		return !isAutoAccbook();
	}
	/**
	 * 获取记账操作信息
	 * @return 0 不记账 1自动记账 2手动记账
	 */
	public int accbookInfo(){
		int num=0;
		if(isAccbook()){
			if(isAutoAccbook()){
				num=1;
			}else{
				num=2;
			}
		}
		return num;
	}
	/**
	 * 流水挂记账
	 * @param accRecords
	 */
	public void accbookVoucher(AbAccrecords accRecords,ICurrentUser userVo){
		if(isNotAccbook()
				||accRecords==null
				||!AccebillConstant.AB_BILL_OPT_STATUS19.equals(accRecords.getOptStatus())
				||StringUtils.isBlank(accRecords.getBriefId())){
			return;
		}
		//判断自动记账进行自动记账处理
		if(isAutoAccbook()){
			autoAccbookVoucher(accRecords,userVo);
		}
	}
	/**
	 * 判断流水是否记账
	 * @param accRecords
	 * @return
	 */
	public boolean checkBuIsAccbook(AbAccrecords accRecords){
		AbAccrecords accRecordsNew=this.autoAccbookInit(accRecords);
		if(StringUtils.isNotEmpty(accRecordsNew.getSysRaceCode())
				&&accRecordsNew.getBusOriType()!=null){
			return true;
		}
		return false;
	}
	/**
	 * 自动记账初始化业务数据信息
	 * @param accRecords
	 * @return
	 */
	private AbAccrecords autoAccbookInit(AbAccrecords accRecords){
		if(accRecords.getVouViceInfo()==null){
			accRecords.setVouViceInfo(abVoucherinfoViceService.get(accRecords.getVvid(), true));
		}
		AbAccrecords accRecordsNew=new AbAccrecords();
		BeanUtils.copyProperties(accRecords, accRecordsNew);
		if(accRecordsNew.getVouInfo()==null){
			accRecordsNew.setVouInfo(abVoucherinfoService.get(accRecordsNew.getVid(), true));
		}
		//判断非资金调拨的电票不需要记账操作
		if(accRecordsNew.getVouInfo()!=null
				&&AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(accRecordsNew.getVouInfo().getBillFlag()))
				&&!(AccebillConstant.BRIEF_STATUS_6201.equals(accRecordsNew.getBriefId())
				||AccebillConstant.BRIEF_STATUS_6202.equals(accRecordsNew.getBriefId())
				||AccebillConstant.BRIEF_STATUS_6204.equals(accRecordsNew.getBriefId()))
		){
			return accRecordsNew;
		}
		CorpDTO corpInfo=accRecordsNew.getCorpInfo();
		if(corpInfo==null){
			// todo:数据转换
			com.bytter.system.api.dto.CorpDTO sysCorpInfo=abCorpService.get(accRecordsNew.getVouViceInfo().getCorpId());
			corpInfo = BeanUtils.copyNewInstanceProperties(sysCorpInfo,CorpDTO.class);
		}
		if(accRecordsNew.getBusOriType()==null||StringUtils.isEmpty(accRecordsNew.getSysRaceCode())){
			CorpDTO netInfo=null;
			String netId="";
			if(StringUtils.isNotEmpty(corpInfo.getNetId())){
				// todo:数据转换
				com.bytter.system.api.dto.CorpDTO sysNetInfo=abCorpService.get(corpInfo.getNetId());
				netInfo = BeanUtils.copyNewInstanceProperties(sysNetInfo,CorpDTO.class);
				if(netInfo!=null){
					netId=netInfo.getId();
				}
			}
			ConstantBean<Integer> busOriType=null;
			String sysRaceCode=null;
			String otherCorpId=null;
			if(AccebillConstant.BRIEF_STATUS_6001.equals(accRecordsNew.getBriefId())
					&&AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getVouViceInfo().getEnterStock())){
				//单位入库中心-入库
				busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_IN_NET;
				sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_IN_NET.get();
			}else if(AccebillConstant.BRIEF_STATUS_6005.equals(accRecordsNew.getBriefId())){
				//票据移存
				if(AccebillConstant.AB_ENTER_STOCK_CORP.equals(accRecordsNew.getEnterStockOld())
						&&AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getEnterStockNew())){
					//单位移存中心-移存
					busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_MOVE_NET;
					sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_MOVE_NET.get();
				}else if(AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getEnterStockOld())
						&&AccebillConstant.AB_ENTER_STOCK_BANK.equals(accRecordsNew.getEnterStockNew())){
					//中心移存银行-移存
					busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_MOVE_BANK;
					sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_MOVE_BANK.get();
				}
			}else if(AccebillConstant.BRIEF_STATUS_6006.equals(accRecordsNew.getBriefId())){
				//票据取票
				if(AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getEnterStockOld())
						&&AccebillConstant.AB_ENTER_STOCK_CORP.equals(accRecordsNew.getEnterStockNew())){
					//单位票据取回-取票
					busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_GET;
					sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_GET.get();
				}else if(AccebillConstant.AB_ENTER_STOCK_BANK.equals(accRecordsNew.getEnterStockOld())
						&&AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getEnterStockNew())){
					//银行取回中心-取票
					busOriType=FinaBookConstant.BusOriType.NB_BILL_BANK_TO_NET_GET;
					sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_BANK_TO_NET_GET.get();
				}
			}else if(AccebillConstant.BRIEF_STATUS_6004.equals(accRecordsNew.getBriefId())
					&&AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getVouViceInfo().getEnterStock())){
				//中心代管-退票
				busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_BACEBILL;
				sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_BACEBILL.get();
			}else if(AccebillConstant.BRIEF_STATUS_020.equals(accRecordsNew.getBriefId())
					||AccebillConstant.BRIEF_STATUS_021.equals(accRecordsNew.getBriefId())){
				if(AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getVouViceInfo().getEnterStock())){
					//中心代管-托收在途
					busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_COLLECTION;
					sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_COLLECTION.get();
				}else if(AccebillConstant.AB_ENTER_STOCK_CORP.equals(accRecordsNew.getVouViceInfo().getEnterStock())
						&&CorpConstant.CORP_TYPE_CODE_NET.equals(corpInfo.getType())){
					if(AccebillConstant.AB_BILL_SOURCE_DISCOUNT.equals(accRecordsNew.getVouViceInfo().getBillSource())){
						//中心自办-托收在途（来源贴现）
						busOriType=FinaBookConstant.BusOriType.NB_BILL_NET_COLLECTION_TX;
						sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_NET_COLLECTION_TX.get();
						netId=corpInfo.getId();
					}else{
						//中心自办-托收在途（来源库存）
						busOriType=FinaBookConstant.BusOriType.NB_BILL_NET_COLLECTION;
						sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_NET_COLLECTION.get();
						netId=corpInfo.getId();
					}
				}
			}else if(AccebillConstant.BRIEF_STATUS_6020.equals(accRecordsNew.getBriefId())){
				if(AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getVouViceInfo().getEnterStock())){
					//中心代管-托收到账
					busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_COLLECTION_DAOZHANG;
					sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_COLLECTION_DAOZHANG.get();
				}else if(AccebillConstant.AB_ENTER_STOCK_CORP.equals(accRecordsNew.getVouViceInfo().getEnterStock())
						&&CorpConstant.CORP_TYPE_CODE_NET.equals(corpInfo.getType())){
					//中心自办-托收到账
					busOriType=FinaBookConstant.BusOriType.NB_BILL_NET_COLLECTION_DAOZHANG;
					sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_NET_COLLECTION_DAOZHANG.get();
					netId=corpInfo.getId();
				}
			}else if(AccebillConstant.BRIEF_STATUS_010.equals(accRecordsNew.getBriefId())
					&&AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getVouViceInfo().getEnterStock())){
				//中心代管-背书
				if(AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(accRecordsNew.getInsideSing()))){
					com.bytter.system.api.dto.CorpDTO otherCorpInfo=abAccrecordsService.getCorpInfo(accRecordsNew.getRecAcctId(),accRecordsNew.getRecNm());
					if(otherCorpInfo==null){
						return accRecordsNew;
					}
					if(otherCorpInfo.getNetId().equals(netId)||otherCorpInfo.getId().equals(netId)){
						//对内同一网点背书
						busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_IN_ENDORSEMENT;
						sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_IN_ENDORSEMENT.get();
						otherCorpId=otherCorpInfo.getId();
					}else{
						//对内不同网点背书
						busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_OUT_ENDORSEMENT;
						sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_OUT_ENDORSEMENT.get();
					}
				}else if(AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(accRecordsNew.getInsideSing()))){
					//对外
					busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_OUT_ENDORSEMENT;
					sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_OUT_ENDORSEMENT.get();
				}
			}else if((AccebillConstant.BRIEF_STATUS_018.equals(accRecordsNew.getBriefId())
					&&AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(accRecordsNew.getInsideSing())))
					&&netInfo!=null){
				com.bytter.system.api.dto.CorpDTO otherCorpInfo=abAccrecordsService.getCorpInfo(accRecordsNew.getRecAcctId(),accRecordsNew.getRecNm());
				if(otherCorpInfo==null){
					return accRecordsNew;
				}
				if(otherCorpInfo!=null
						&&netInfo.getId().equals(otherCorpInfo.getId())){
					//上级中心质押
					if(AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getVouViceInfo().getEnterStock())){
						//中心库存质押到中心
						busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_PLEDGE_NET;
						sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_PLEDGE_NET.get();
					}else if(AccebillConstant.AB_ENTER_STOCK_CORP.equals(accRecordsNew.getVouViceInfo().getEnterStock())){
						//单位库存质押到中心
						busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_PLEDGE_NET;
						sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_PLEDGE_NET.get();
					}
				}
			}
			else if(AccebillConstant.BRIEF_STATUS_019.equals(accRecordsNew.getBriefId())){
				if(AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(accRecordsNew.getVouInfo().getBillFlag())) && AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(accRecordsNew.getInsideSing()))){
					// 获取质押来源票据登记流水表信息
					AbAccrecords lastAccrecords = abAccrecordsService.get(accRecordsNew.getLastid());
					if(StringUtils.isNotEmpty(lastAccrecords.getLastid())){
						AbAccrecords recordsPledgeApply = abAccrecordsService.get(lastAccrecords.getLastid(),true);
						AbVoucherinfoVice pledgeApplyVice = abVoucherinfoViceService.get(recordsPledgeApply.getVvid(), true);
						// todo:数据转换
						com.bytter.system.api.dto.CorpDTO corpDTO = abCorpService.get(pledgeApplyVice.getCorpId());
						corpInfo = BeanUtils.copyNewInstanceProperties(corpDTO, CorpDTO.class);
						if(accRecordsNew.getVouViceInfo()!=null
								&&accRecordsNew.getVouViceInfo().getCorpId().equals(corpInfo.getNetId())){
							netId=corpInfo.getNetId();
							//上级中心质押解除
							if(AccebillConstant.AB_ENTER_STOCK_NET.equals(pledgeApplyVice.getEnterStock())){
								//中心库存质押到中心
								busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_PLEDGE_NETGET;
								sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_PLEDGE_NETGET.get();
							}else if(AccebillConstant.AB_ENTER_STOCK_CORP.equals(pledgeApplyVice.getEnterStock())){
								//单位库存质押到中心
								busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_PLEDGE_NETGET;
								sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_PLEDGE_NETGET.get();
							}
						}
					}
				}
			}
			else if((AccebillConstant.BRIEF_STATUS_6201.equals(accRecordsNew.getBriefId())
					||AccebillConstant.BRIEF_STATUS_6202.equals(accRecordsNew.getBriefId()))
					&&AccebillConstant.AB_ENTER_STOCK_CORP.equals(accRecordsNew.getVouViceInfo().getEnterStock())){
				//单位库存票据归集或者上划到中心
				busOriType=FinaBookConstant.BusOriType.NB_BILL_DELIVER;
				sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_DELIVER.get();
			}else if(AccebillConstant.BRIEF_STATUS_6204.equals(accRecordsNew.getBriefId())){
				//中心自己库存票据下拨到单位库存
				busOriType=FinaBookConstant.BusOriType.NB_BILL_ALLOCATE;
				sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_ALLOCATE.get();
				netId=corpInfo.getId();
				// todo:数据转换
				com.bytter.system.api.dto.CorpDTO info = abAccrecordsService.getCorpInfo(accRecordsNew.getRecAcctId(), accRecordsNew.getRecNm());
				corpInfo = BeanUtils.copyNewInstanceProperties(info, CorpDTO.class);
			}else if(AccebillConstant.BRIEF_STATUS_011.equals(accRecordsNew.getBriefId())){
				//贴现业务
				if(AccebillConstant.AB_ENTER_STOCK_CORP.equals(accRecordsNew.getVouViceInfo().getEnterStock())){
					if(CorpConstant.CORP_TYPE_CODE_NET.equals(corpInfo.getType())){
						com.bytter.system.api.dto.CorpDTO otherCorpInfo=abAccrecordsService.getCorpInfo(accRecordsNew.getRecAcctId(),accRecordsNew.getRecNm());
						if(otherCorpInfo!=null
								&&String.valueOf(netInfo.getId()).equals(otherCorpInfo.getId())){
							//单位库存贴现到上级中心
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accRecordsNew.getId());
							if(AccebillConstant.AB_DISCOUNT_RPDMK_BUYOUT.equals(discount.getRpdmk())){
								//买断
								busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_DISCOUNT_NET_MD;
								sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_MD.get();
							}else if(AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK.equals(discount.getRpdmk())){
								//回购式
								busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_DISCOUNT_NET_SH;
								sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_SH.get();
							}
						}else{
							netId=corpInfo.getId();
							if(AccebillConstant.AB_BILL_SOURCE_DISCOUNT.equals(accRecordsNew.getVouViceInfo().getBillSource())){
								//中心自办贴现在途（来源贴现）
								busOriType=FinaBookConstant.BusOriType.NB_BILL_NET_DISCOUNT_BANK_TX;
								sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_TX.get();
							}else{
								//中心自办贴现在途(来源库存)
								busOriType=FinaBookConstant.BusOriType.NB_BILL_NET_DISCOUNT_BANK;
								sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK.get();
							}
						}
					}else{
						com.bytter.system.api.dto.CorpDTO otherCorpInfo=abAccrecordsService.getCorpInfo(accRecordsNew.getRecAcctId(),accRecordsNew.getRecNm());
						if(otherCorpInfo!=null
								&&netInfo.getId().equals(otherCorpInfo.getId())){
							//单位库存贴现到上级中心
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accRecordsNew.getId());
							if(AccebillConstant.AB_DISCOUNT_RPDMK_BUYOUT.equals(discount.getRpdmk())){
								//买断
								busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_DISCOUNT_NET_MD;
								sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_MD.get();
							}else if(AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK.equals(discount.getRpdmk())){
								//回购式
								busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_DISCOUNT_NET_SH;
								sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_SH.get();
							}
						}
					}
				}else if(AccebillConstant.AB_ENTER_STOCK_NET.equals(accRecordsNew.getVouViceInfo().getEnterStock())){
					com.bytter.system.api.dto.CorpDTO otherCorpInfo=abAccrecordsService.getCorpInfo(accRecordsNew.getRecAcctId(),accRecordsNew.getRecNm());
					if(otherCorpInfo!=null
							&&netInfo.getId().equals(otherCorpInfo.getId())){
						//中心库存贴现到上级中心
						AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accRecordsNew.getId());
						if(AccebillConstant.AB_DISCOUNT_RPDMK_BUYOUT.equals(discount.getRpdmk())){
							//买断
							busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD;
							sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD.get();
						}else if(AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK.equals(discount.getRpdmk())){
							//回购式
							busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH;
							sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH.get();
						}
					}
				}
			}else if(AccebillConstant.BRIEF_STATUS_6012.equals(accRecordsNew.getBriefId())
					&&AccebillConstant.AB_ENTER_STOCK_CORP.equals(accRecordsNew.getVouViceInfo().getEnterStock())
					&&CorpConstant.CORP_TYPE_CODE_NET.equals(corpInfo.getType())
					&&AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(accRecordsNew.getInsideSing()))){
				//中心自办贴现到账
				netId=corpInfo.getId();
				busOriType=FinaBookConstant.BusOriType.NB_BILL_NET_DISCOUNT_BANK_DAOZHANG;
				sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_DAOZHANG.get();
			}else if(AccebillConstant.BRIEF_STATUS_6212.equals(accRecordsNew.getBriefId())
					&&AccebillConstant.AB_ENTER_STOCK_CORP.equals(accRecordsNew.getVouViceInfo().getEnterStock())
					&&CorpConstant.CORP_TYPE_CODE_NET.equals(corpInfo.getType())){
				//中心自办银行贴现-贴现赎回
				netId=corpInfo.getId();
				if(AccebillConstant.AB_BILL_SOURCE_DISCOUNT.equals(accRecordsNew.getVouViceInfo().getBillSource())){
					//中心自办银行贴现-贴现赎回（来源贴现）
					busOriType=FinaBookConstant.BusOriType.NB_BILL_NET_DISCOUNT_BANK_GET_TX;
					sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_GET_TX.get();
				}else{
					//中心自办银行贴现-贴现赎回（来源库存）
					busOriType=FinaBookConstant.BusOriType.NB_BILL_NET_DISCOUNT_BANK_GET;
					sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_GET.get();
				}
			}else if(AccebillConstant.BRIEF_STATUS_012.equals(accRecordsNew.getBriefId())){
				netId=corpInfo.getId();
				AbAccrecords accrecordsFather=abAccrecordsService.get(accRecordsNew.getLastid());
				if(accrecordsFather==null||StringUtils.isEmpty(accrecordsFather.getLastid())){
					return accRecordsNew;
				}
				accrecordsFather=abAccrecordsService.get(accrecordsFather.getLastid());
				if(accrecordsFather==null||StringUtils.isEmpty(accrecordsFather.getVvid())){
					return accRecordsNew;
				}
				accrecordsFather.setVouViceInfo(abVoucherinfoViceService.get(accrecordsFather.getVvid(), true));
				if(accrecordsFather.getVouViceInfo()==null){
					return accRecordsNew;
				}
				// todo:数据转换
				com.bytter.system.api.dto.CorpDTO corpDTO = abCorpService.get(accrecordsFather.getVouViceInfo().getCorpId());
				corpInfo = BeanUtils.copyNewInstanceProperties(corpDTO,CorpDTO.class);
				if(corpInfo==null){
					return accRecordsNew;
				}
				//内部上级中心贴现赎回申请
				if(netId.equals(corpInfo.getNetId())){
					if(AccebillConstant.AB_ENTER_STOCK_CORP.equals(accrecordsFather.getVouViceInfo().getEnterStock())){
						busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_DISCOUNT_NET_GET;
						sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_GET.get();
					}else if(AccebillConstant.AB_ENTER_STOCK_NET.equals(accrecordsFather.getVouViceInfo().getEnterStock())){
						busOriType=FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET;
						sysRaceCode=FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET.get();
					}
				}
			}
			accRecordsNew.setNetId(netId);
			accRecordsNew.setOtherCorpId(otherCorpId);
			// todo:数据转换
			com.bytter.core.constant.ConstantBean constantBean = BeanUtils.copyNewInstanceProperties(busOriType, com.bytter.core.constant.ConstantBean.class);
//			accRecordsNew.setBusOriType(constantBean);
			accRecordsNew.setBusOriType(busOriType);
			accRecordsNew.setSysRaceCode(sysRaceCode);
			accRecordsNew.setCorpInfo(corpInfo);
			accRecords.setNetId(netId);
			accRecords.setOtherCorpId(otherCorpId);
//			accRecords.setBusOriType(constantBean);
			accRecords.setBusOriType(busOriType);
			accRecords.setSysRaceCode(sysRaceCode);
			accRecords.setCorpInfo(corpInfo);
		}
		return accRecordsNew;
	}
	/**
	 * 流水挂记账
	 * @param accRecords
	 */
	private void autoAccbookVoucher(AbAccrecords accRecords,ICurrentUser userVo){
		AbAccrecords accRecordsNew=this.autoAccbookInit(accRecords);
		if(StringUtils.isNotEmpty(accRecordsNew.getSysRaceCode())
				&&accRecordsNew.getBusOriType()!=null){
			// todo:数据转换
			ConstantBean constantBean = new ConstantBean<Integer>();
			BeanUtils.copyNotNullProperties(accRecordsNew.getBusOriType(), constantBean);

			accbookVoucher(accRecordsNew, accRecordsNew.getNetId(), accRecordsNew.getOtherCorpId(), constantBean, accRecordsNew.getSysRaceCode(), accRecordsNew.getCorpInfo(),userVo);
			if(StringUtils.isNotEmpty(accRecordsNew.getBusGlobalId())){
				int size=abAccrecordsService.setBusGlobalIdById(accRecordsNew.getId(),accRecordsNew.getBusGlobalId(),userVo);
				if(size!=1){
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_BUSGLOBALID_ERROR);
				}
			}else{
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_ERROR);
			}
		}
	}
	/**
	 * 票据挂记账实现
	 * @param accrecords 业务流水信息
	 * @param netId 网点ID
	 * @param otherCorpId 其他单位ID
	 * @param busOriTypeBean 业务类型
	 * @param sysRaceCode 记账规则代码
	 * @param corpInfo 本单位信息
	 */
	private void accbookVoucher(AbAccrecords accrecords, String netId, String otherCorpId, ConstantBean<Integer> busOriTypeBean, String sysRaceCode, CorpDTO corpInfo, ICurrentUser userVo){
		Integer busOriType=busOriTypeBean.get();
		FcBusinessRuleDefineDTO accbookRule=this.getAccBookRule(netId,sysRaceCode);
		String voucherType=accbookRule.getVoucherType();
		String busRaceId = abFcBusRaceService.getNetBusRace(sysRaceCode, netId, userVo.getId());
		FcAccbookDTO accBook = new FcAccbookDTO();
		accBook.setNetId(netId);
		accBook.setBusRaceId(busRaceId);
		accBook.setBusOriType(busOriType);
		accBook.setVoucherType(voucherType);
//		accBook.setTallyDate(accrecords.getBusinessDate());
		//记账日期约定就是当前系统日期
		accBook.setTallyDate(DateUtils.getDateYMD());
		accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_F);
		List<FcAccbookDtlDTO> accbookDtls=new ArrayList<>();
		List<FcVoucherEntryDefineDTO> vouchers = accbookRule.getVouchers();
		// todo:数据转换
		List<FcVoucherEntryDefineDTO> voucherDtls = BeanUtils.copyListProperties(vouchers,FcVoucherEntryDefineDTO.class);
		for(FcVoucherEntryDefineDTO voucherDtl:voucherDtls ){
			if (StringUtils.isEmpty(voucherDtl.getSubjectCode())) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_SUBJECTCODE_ISNULL,accbookRule.getSysRaceCode());
			}
			voucherDtl.setCurCode(accbookRule.getCurCode());
			FcAccbookDtlDTO accbookDtl=new FcAccbookDtlDTO();
			accbookDtl.setCreditMoney(BigDecimal.ZERO);
			accbookDtl.setDebitMoney(BigDecimal.ZERO);
			if(FinacialConstant.ENTRY_TYPE_10.equals(voucherDtl.getEntryType())){
				if(FinacialConstant.BALANCE_WAY_DEBIT.equals(voucherDtl.getBalanceWay().toString())){
					accbookDtl.setDebitMoney(accrecords.getVouInfo().getBillMoney());
					if(voucherDtl.getGroupNum()==1){
						if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_GET.get().equals(sysRaceCode)){
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecords.getId());
							if(discount!=null){
								accbookDtl.setDebitMoney(accrecords.getVouInfo().getBillMoney().subtract(discount.getInterest()));
							}
						}else if(FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_DAOZHANG.get().equals(sysRaceCode)){
							AbAccrecords accrecordsFather=abAccrecordsService.get(accrecords.getLastid());
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecordsFather.getId());
							accbookDtl.setDebitMoney(discount.getRealMoney());
						}
					}else if(voucherDtl.getGroupNum()==3){
						if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET.get().equals(sysRaceCode)){
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecords.getId());
							if(discount!=null){
								accbookDtl.setDebitMoney(accrecords.getVouInfo().getBillMoney().subtract(discount.getInterest()));
							}
						}
					}
				}else if(FinacialConstant.BALANCE_WAY_CREDIT.equals(voucherDtl.getBalanceWay().toString())){
					accbookDtl.setCreditMoney(accrecords.getVouInfo().getBillMoney());
					if(voucherDtl.getGroupNum()==4){
						if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD.get().equals(sysRaceCode)
								||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH.get().equals(sysRaceCode)){
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecords.getId());
							accbookDtl.setCreditMoney(discount.getRealMoney());
						}
					}else if(voucherDtl.getGroupNum()==2){
						if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_MD.get().equals(sysRaceCode)
								||FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_SH.get().equals(sysRaceCode)){
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecords.getId());
							accbookDtl.setCreditMoney(discount.getRealMoney());
						}else if(FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_GET.get().equals(sysRaceCode)
								||FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_GET_TX.get().equals(sysRaceCode)){
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecords.getId());
							if(discount!=null){
								accbookDtl.setCreditMoney(accrecords.getVouInfo().getBillMoney().subtract(discount.getInterest()));
							}
						}
					}
				}
			}else if(FinacialConstant.ENTRY_TYPE_40.equals(voucherDtl.getEntryType())){
				if(FinacialConstant.BALANCE_WAY_DEBIT.equals(voucherDtl.getBalanceWay().toString())){
					if(voucherDtl.getGroupNum()==2){
						if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_GET.get().equals(sysRaceCode)){
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecords.getId());
							if(discount!=null){
								accbookDtl.setDebitMoney(discount.getInterest());
							}
						}else if(FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_DAOZHANG.get().equals(sysRaceCode)){
							AbAccrecords accrecordsFather=abAccrecordsService.get(accrecords.getLastid());
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecordsFather.getId());
							accbookDtl.setDebitMoney(accrecords.getVouInfo().getBillMoney().subtract(discount.getRealMoney()));
						}
					}else if(voucherDtl.getGroupNum()==4){
						if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET.get().equals(sysRaceCode)){
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecords.getId());
							if(discount!=null){
								accbookDtl.setDebitMoney(discount.getInterest());
							}
						}
					}
				}else if(FinacialConstant.BALANCE_WAY_CREDIT.equals(voucherDtl.getBalanceWay().toString())){
					if(voucherDtl.getGroupNum()==5){
						if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD.get().equals(sysRaceCode)
								||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH.get().equals(sysRaceCode)){
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecords.getId());
							accbookDtl.setCreditMoney(accrecords.getVouInfo().getBillMoney().subtract(discount.getRealMoney()));
						}
					}else if(voucherDtl.getGroupNum()==3){
						if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_MD.get().equals(sysRaceCode)
								||FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_SH.get().equals(sysRaceCode)){
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecords.getId());
							accbookDtl.setCreditMoney(accrecords.getVouInfo().getBillMoney().subtract(discount.getRealMoney()));
						}else if(FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_GET.get().equals(sysRaceCode)
								||FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_GET_TX.get().equals(sysRaceCode)){
							AbDiscount discount=abDiscountService.getAbDiscountByRecordId(accrecords.getId());
							if(discount!=null){
								accbookDtl.setCreditMoney(discount.getInterest());
							}
						}
					}
				}
			}
			FcAccDTO fcaccDTO=getFcAccDTO(corpInfo, netId, otherCorpId, sysRaceCode, voucherDtl, accBook.getTallyDate(),accrecords.getRecAcctsvcr(),userVo);
			accbookDtl.setAccName(fcaccDTO.getAccName());
			accbookDtl.setAccount(fcaccDTO.getBlnctrAcc());
			accbookDtl.setLineNo(voucherDtl.getGroupNum());
			accbookDtl.setCurCode(voucherDtl.getCurCode());
			accbookDtl.setOrigVoucherId(accrecords.getId());
			accbookDtl.setOrigVoucherNo(accrecords.getVouInfo().getOrigNo());
			accbookDtl.setSummaryContent(busOriTypeBean.getName());
			if(StringUtils.isNotEmpty(voucherDtl.getSummary())){
				accbookDtl.setSummaryContent(accbookDtl.getSummaryContent()+" "+voucherDtl.getSummary());
			}
			accbookDtls.add(accbookDtl);
		}
		FcAccbookVoucherDTO voucherInfo=new FcAccbookVoucherDTO();
		voucherInfo.setAccbook(accBook);
		voucherInfo.setBookDtls(accbookDtls);
//		FcAccbookExtDTO accbookExt=new FcAccbookExtDTO();
//		voucherInfo.setBookExt(accbookExt);
		VoucherWrapperDTO voucher=new VoucherWrapperDTO();
		voucher.addTarget(voucherInfo);
		voucher.setCharge(false);
		voucher.setProcessType(ProcessType.PROCESS_COMMON);
		voucher.setUserCode(userVo.getId());//设置用户
		voucher.setAutoCheck(true);
		voucher = fcAccbookService.submitVoucher(voucher);
		accrecords.setBusGlobalId(voucher.getBusGlobalId());
	}
	/**
	 * 根据规则代码获取记账规则
	 * @param netId 网点ID
	 * @param sysRaceCode 记账规则代码
	 * @return 返回记账规则信息
	 */
	@Transactional(readOnly = true)
	public FcBusinessRuleDefineDTO getAccBookRule(String netId, String sysRaceCode){
		Map<String, Object> map = new HashMap<>();
		map.put("netId", netId);
		map.put("sysRaceCode", sysRaceCode);
		return abFcBusinessRuleDefineService.getBusinessRuleExp(map);
	}
	/**
	 * 获取内部账户信息，如果账户不存在则自动开户
	 * @param corpInfo 单位信息
	 * @param netId 网点ID
	 * @param otherCorpId 其他单位ID
	 * @param sysRaceCode 记账规则代码
	 * @param voucherDtl 记账规则明细
	 * @param tallyDate 记账日期
	 * @return 返回记账账户
	 */
	private FcAccDTO getFcAccDTO(CorpDTO corpInfo,String netId,String otherCorpId,String sysRaceCode,FcVoucherEntryDefineDTO voucherDtl,Date tallyDate,String bankType,ICurrentUser user){
		String corpId=getFcAccCorpId(netId, corpInfo.getId(),otherCorpId, sysRaceCode, voucherDtl);
		com.bytter.system.api.dto.CorpDTO accCorp=abCorpService.get(corpId);
		List<FcAccDTO> list = null;
		if(voucherDtl == null){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_SUBJECTCODE_ISNULL,"");
		}
		FcSubjectDTO fcSubject = abFcSubjectService.findSubjectById(voucherDtl.getSubjectCode());
        if(fcSubject == null){
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_SUBJECTCODE_ISNULL,"");
        }
		if(SubjectConstant.SysSubject.AB_INTEREST_INCOME.getValue().equals(fcSubject.getSysSubjectCode()) ||
				SubjectConstant.SysSubject.AB_BANK_INTEREST_EXPENDITURE.getValue().equals(fcSubject.getSysSubjectCode()) ||
				SubjectConstant.SysSubject.AB_INTEREST_PROFIT.getValue().equals(fcSubject.getSysSubjectCode())){//501004,521005,2060
			if(SubjectConstant.SysSubject.AB_BANK_INTEREST_EXPENDITURE.getValue().equals(fcSubject.getSysSubjectCode())){//521005
				list = fcAccbookService.getInterestAcc(voucherDtl.getCurCode(),netId,corpInfo.getId(), voucherDtl.getSubjectCode(),"ab",bankType,user.getId(),"银行贴现利息支出");
			}else {
				list = fcAccbookService.getInterestAcc(voucherDtl.getCurCode(),netId,corpInfo.getId(), voucherDtl.getSubjectCode(),"","",user.getId(),"贴现利息收入");
			}
		}else {
			// todo:数据转换
			List<com.bytter.system.api.dto.FcAccDTO> listData = abInnerAccountService.listAccountByParams(voucherDtl.getCurCode(), netId, corpId, voucherDtl.getSubjectCode());
			list = BeanUtils.copyListProperties(listData, FcAccDTO.class);
		}
		if(!EmptyUtils.isEmptyList(list)){
			return list.get(0);
		}
		com.bytter.system.api.dto.FcAccDTO fcaccDto = new com.bytter.system.api.dto.FcAccDTO();
		fcaccDto.setNetId(netId);
		fcaccDto.setCorpId(corpId);
//		fcaccDto.setCurId(voucherDtl.getCurCode());
		fcaccDto.setCurId(abVoucherinfoViceService.getCurIdByCNY());
		FcSubjectDTO subjectDTO=abFcSubjectService.findSubjectById(voucherDtl.getSubjectCode());
		if(subjectDTO == null){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_SUBJECTCODE_ISNULL,"");
		}
		//账户类型
		fcaccDto.setAccType(subjectDTO.getAccType());
		fcaccDto.setSubjectId(subjectDTO.getId());
		//自动复核
		fcaccDto.setAutoCheck(true);
		//账户状态
		fcaccDto.setAccName(accCorp.getName()+"-"+subjectDTO.getSubjectName());
		//计息标志
		fcaccDto.setInterestSign(subjectDTO.getInterestSign());
		fcaccDto.setPredrawSign(subjectDTO.getPredrawSign());
		fcaccDto.setOverdraftSign(InnerAccountConstant.OVERDRAFT_SIGN_N);
		fcaccDto.setBalanceWay(subjectDTO.getBalanceWay());
		//开户日期
		fcaccDto.setAccOpenTime(tallyDate);
		//账号启用日期
		fcaccDto.setAccStartDate(tallyDate);
		String blnctracc = abInnerAccountService.saveOrUpdate(fcaccDto,user);
		fcaccDto.setBlnctrAcc(blnctracc);
		// todo:数据转换
		return BeanUtils.copyNewInstanceProperties(fcaccDto,FcAccDTO.class);
	}
	/**
	 * 获取账户的开户所属单位
	 * @param netId 所属网点ID
	 * @param corpId 业务操作单位ID
	 * @param sysRaceCode 记账规则
	 * @param voucherDtl 记账凭证分录
	 * @return 获取对应的单位ID
	 */
	@Transactional(readOnly = true)
	public String getFcAccCorpId(String netId,String corpId,String otherCorpId,String sysRaceCode,FcVoucherEntryDefineDTO voucherDtl){
		if(FinacialConstant.BALANCE_WAY_DEBIT.equals(voucherDtl.getBalanceWay().toString())){
			if(voucherDtl.getGroupNum()==2){
				if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_GET.get().equals(sysRaceCode)){
					return netId;
				}
			}else if(voucherDtl.getGroupNum()==3){
				if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_COLLECTION_DAOZHANG.get().equals(sysRaceCode)){
					return netId;
				}
			}else if(voucherDtl.getGroupNum()==4){
				if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET.get().equals(sysRaceCode)){
					return netId;
				}
			}else if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_IN_NET.get().equals(sysRaceCode)
					||FinaBookConstant.SysRaceCode.NB_BILL_CORP_MOVE_NET.get().equals(sysRaceCode)
					||FinaBookConstant.SysRaceCode.NB_BILL_BANK_TO_NET_GET.get().equals(sysRaceCode)
					||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_COLLECTION.get().equals(sysRaceCode)
					||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_PLEDGE_NET.get().equals(sysRaceCode)
					||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_PLEDGE_NETGET.get().equals(sysRaceCode)
					||FinaBookConstant.SysRaceCode.NB_BILL_CORP_PLEDGE_NET.get().equals(sysRaceCode)
					||FinaBookConstant.SysRaceCode.NB_BILL_DELIVER.get().equals(sysRaceCode)){
				return netId;
			}else if(voucherDtl.getGroupNum()==1){
				if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_MD.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_SH.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET.get().equals(sysRaceCode)){
					return netId;
				}
			}
		}else if(FinacialConstant.BALANCE_WAY_CREDIT.equals(voucherDtl.getBalanceWay().toString())){
			if(voucherDtl.getGroupNum()==4){
				if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_COLLECTION_DAOZHANG.get().equals(sysRaceCode)){
					return netId;
				}
			}else if(voucherDtl.getGroupNum()==3){
				if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_MD.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_SH.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_GET.get().equals(sysRaceCode)){
					return netId;
				}
			}else if(voucherDtl.getGroupNum()==5){
				if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET.get().equals(sysRaceCode)){
					return netId;
				}
			}else{
				if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_MOVE_BANK.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_GET.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_BACEBILL.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_COLLECTION.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_OUT_ENDORSEMENT.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_IN_ENDORSEMENT.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_PLEDGE_NET.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_PLEDGE_NETGET.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_CORP_PLEDGE_NETGET.get().equals(sysRaceCode)
						||FinaBookConstant.SysRaceCode.NB_BILL_ALLOCATE.get().equals(sysRaceCode)){
					return netId;
				}
//				else if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_IN_ENDORSEMENT.get().equals(sysRaceCode)){
//	    			return otherCorpId;
//	    		}
				else if(voucherDtl.getGroupNum()==2){
					if(FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD.get().equals(sysRaceCode)
							||FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH.get().equals(sysRaceCode)
							/*||FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_MD.get().equals(sysRaceCode)
							||FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_SH.get().equals(sysRaceCode)*/
					){
						return netId;
					}
				}
			}
		}
		return corpId;
	}
	/**
	 * 查询手工记账数据信息
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getBillAccbookDataTables(DataTablesInput input, ICurrentUser user) {
		List<String> corpAuthList=null;
		if(StringUtils.isNotEmpty(input.getResource())){
			corpAuthList=abCorpService.getUserCorpIds(input.getResource(),input.getCurrentUserId());
		}
		//当复核或者记账时 需要查询FC_ACCBOOK的状态
		String voucherState = (String) input.getFormdata().get("ins_voucherState");
		List<String> busGloabIds = new ArrayList<>();
		List<FcAccbookDTO> fcAccbookDTOS = null;
		if (StringUtils.isNotEmpty(voucherState)) {
			fcAccbookDTOS = fcAccbookService.listByInCorpIdAndBusOriTypeAndInState(corpAuthList, String.valueOf(input.getFormdata().get("eqi_busOriType")),
					Lists.newArrayList(voucherState.split(CommonConstant.COMMA_SPLIT)));
			busGloabIds = fcAccbookDTOS.stream().map(FcAccbookDTO::getBusGlobalId).collect(Collectors.toList());
		}
		// 跨表获取数据
		DataTablesOutput<AbBillAccbookInfoDTO> dataTablesOutput = abAccbookInfoDao.getAbAccbookInfoList(input, corpAuthList, user,busGloabIds);
		//设置数据状态
		if(CollectionUtil.isNotEmpty(fcAccbookDTOS)){
			Map<String, String> voucherStatMap = fcAccbookDTOS.stream().collect(Collectors.toMap(FcAccbookDTO::getBusGlobalId, FcAccbookDTO::getVoucherState, (pre, next) -> next));
			for (AbBillAccbookInfoDTO infoDTO : dataTablesOutput.getData()) {
				infoDTO.setVoucherState(voucherStatMap.get(infoDTO.getBusGlobalId()));
			}
		}
		return dataTablesOutput;
	}
	private void injectValues(AbBillAccbookInfoDTO bill, Map<String, Map<String, DictDTO>> listDictMap){
		// 票据类型：10 银票、11 商票
		Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
					.ifPresent(dict -> bill.setBillTypeName(dict.getName()));
		});
		// 流通介质：10 纸票、11 电票
		Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
					.ifPresent(dict -> bill.setBillFlagName(dict.getName()));
		});
		// 是否发送指令标志：0否； 1是 ；
		Optional.ofNullable(bill.getSendSign()).ifPresent(sendSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
					.ifPresent(dict -> bill.setSendSignName(dict.getName()));
		});
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
	}
	public AbBillAccbookInfoDTO getAbBillInfoById(String id){
		return abAccbookInfoDao.getAbBillInfoById(id);
	}
	/**
	 * 根据流水ID获取此流水对应记账信息
	 * @param aid
	 * @return
	 */
	public AbAccbookInfo getAbAccbookInfoByAid(String aid){
		return abAccbookInfoDao.getAbAccbookInfoByAid(aid);
	}
	/**待记账查询*/
	public List<AbBillAccbookInfoDTO> queryTallyAbAccbook(Integer busOriType) {
		List<AbBillAccbookInfoDTO> results = Lists.newArrayList();
		List<AbBillAccbookInfoDTO> abBillAccbookInfoDTOS = abAccbookInfoDao.queryTallyAbAccbook(busOriType);
		List<String> aidList = abBillAccbookInfoDTOS.stream().map(AbBillAccbookInfoDTO::getAid).collect(Collectors.toList());
		//判断 billId 是否在fc_tally_remind_dtl中，如果存在则排除之后返回
		List<String> tallyRecordListId = fcAccbookService.getTallyRecordListId(aidList);
		if (ObjectUtil.isNotEmpty(tallyRecordListId)){
			results = abBillAccbookInfoDTOS.stream().filter(dtos -> {
				if (aidList.contains(dtos.getAid())) {
					return false;
				}else {
					return true;
				}
			}).collect(Collectors.toList());
		}
		if (ObjectUtil.isEmpty(tallyRecordListId)){
			results.addAll(abBillAccbookInfoDTOS);
		}
		return results;
	}
	/**
	 * 根据单位id查询票据待记账数据
	 * @param corpId 单位id
	 * @return true 有待记账数据  false 无待记账数据
	 */
	public boolean checkToBeBookByCorpId(String corpId){
		if(StringUtils.isNotEmpty(corpId)){
			return abAccbookInfoDao.checkToBeBookByCorpId(corpId);
		}
		return false;
	}
}
