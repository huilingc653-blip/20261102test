package com.bytter.bill.domain.bill.service;

import com.bytter.bill.domain.bill.dao.AbBackbillinfoDao;
import com.bytter.bill.domain.bill.entity.AbBackbillinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBackbillinfoVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 票据背面信息
 * @date 2017-06-21 15:36:23
 */
@Service("abBackbillinfoService")
@Transactional
public class AbBackbillinfoService extends BaseService<AbBackbillinfo, String> {
    @Autowired
    private AbBackbillinfoDao abBackbillinfoDao;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;

    @Override
    public BaseDao<AbBackbillinfo, String> getEntityDao() {
        return abBackbillinfoDao;
    }

	private List<AbBackbillinfoVO> dealSelListAndGetResult(boolean isDesc,List<AbAccrecordsVO> endorseAccList,List<AbBackbillinfo> listInfo,AbVoucherinfoVO vouInfoVo){
		if(isDesc && !CollectionUtils.isEmpty(endorseAccList)){
			List<AbBackbillinfo> listInfoNew=new ArrayList<AbBackbillinfo>();
			for(AbAccrecordsVO endorseAcc: endorseAccList){
				AbBackbillinfo backbillinfo=new AbBackbillinfo();
				backbillinfo.setId(endorseAcc.getId());
				backbillinfo.setOrigNo(endorseAcc.getOrigNo());
				backbillinfo.setBankendrsmtmk(endorseAcc.getBankendrsmtmk());
				backbillinfo.setBussdt(endorseAcc.getBusinessDate());
				backbillinfo.setBustype(AccebillConstant.AB_BUSTYPE_RECITE);
				backbillinfo.setReqNm(endorseAcc.getReqNm());
				backbillinfo.setRecNm(endorseAcc.getRecNm());
				listInfoNew.add(backbillinfo);
			}
			listInfoNew.addAll(listInfo);
			listInfo=listInfoNew;
		}else if(!CollectionUtils.isEmpty(endorseAccList)){
			for(AbAccrecordsVO endorseAcc: endorseAccList){
				AbBackbillinfo backbillinfo=new AbBackbillinfo();
				backbillinfo.setId(endorseAcc.getId());
				backbillinfo.setOrigNo(endorseAcc.getOrigNo());
				backbillinfo.setBankendrsmtmk(endorseAcc.getBankendrsmtmk());
				backbillinfo.setBussdt(endorseAcc.getBusinessDate());
				backbillinfo.setBustype(AccebillConstant.AB_BUSTYPE_RECITE);
				backbillinfo.setReqNm(endorseAcc.getReqNm());
				backbillinfo.setRecNm(endorseAcc.getRecNm());
				listInfo.add(backbillinfo);
			}
		}

		List<AbBackbillinfoVO> backVoList=BeanUtils.copyListProperties(listInfo, AbBackbillinfoVO.class);
//		this.inject(backVoList);
		StringBuilder strbuilder=new StringBuilder();
		for(int i=0;i<backVoList.size();i++){
			AbBackbillinfoVO info=backVoList.get(i);
			String bustType=info.getBustype();
			if(AccebillConstant.AB_BUSTYPE_DISCOUNT.equals(bustType)
					||AccebillConstant.AB_BUSTYPE_DISCOUNT_TRANF.equals(bustType)
					||AccebillConstant.AB_BUSTYPE_DISCOUNT_AGAIN.equals(bustType)){
				bustType+=info.getRpdmk();
				info.setBustype(bustType);
			}else if(vouInfoVo!=null&&
					(AccebillConstant.AB_BUSTYPE_ACCEPT.equals(bustType)||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET01.equals(bustType))){
				vouInfoVo.setAcceptorInfo(info);
			}else if(vouInfoVo!=null&&
					(AccebillConstant.AB_BUSTYPE_GUARGN.equals(bustType)||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET02.equals(bustType))){
				if(i==0){//开票保证
					vouInfoVo.setPayerBail(info);
				}else{
					AbBackbillinfoVO infoOld=backVoList.get(i-1);
					if(AccebillConstant.AB_BUSTYPE_ACCEPT.equals(infoOld.getBustype())
						||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET01.equals(infoOld.getBustype())){
						vouInfoVo.setAcceptorBail(info);//背书保证
					}
				}
			}
			strbuilder.append(bustType).append(",");
		}
//		010\011RM00\011RM01\012\013RM00\013RM01\014\015RM00\015RM01\016\017\018\018\025\020\021\022
		String[] bustTypes = null;
		//区别新老系统票据在某些业务上的bustype不同导致背面信息重复展示
		if(AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(vouInfoVo.getNewStmIssuofpprInd())){
			bustTypes= new String[]{"ET05", "011RM00", "ET14", "ET15", "013RM00", "013RM01", "014", "015RM00", "015RM01", "016", "ET02", "ET03", "ET04", "025", "ET09", "021", "ET10"
					, "ET06", "ET07", "ET08", "ET11", "ET12", "ET13", "ET17", "ET18", "ET19", "ET20", "ET21", "ET22"};
		}else{
			bustTypes= new String[]{"010", "011RM00", "011RM01", "012", "013RM00", "013RM01", "014", "015RM00", "015RM01", "016", "017", "018", "019", "025", "020", "021", "022"
					, "ET06", "ET07", "ET08", "ET11", "ET12", "ET13", "ET17", "ET18", "ET19", "ET20", "ET21", "ET22"};
		}
		for(String bustType:bustTypes){
			if(strbuilder.length()==0||strbuilder.indexOf(bustType+",")==-1 ){
				AbBackbillinfoVO info=new AbBackbillinfoVO();
				info.setBustype(bustType);
				info.setIsNull("1");
				backVoList.add(info);
			}
		}
		return backVoList;
	}

	/**
	 * 根据票据号获取票据背面信息
	 * @param origNo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbBackbillinfoVO> listAbBackbillinfoVOByOrigNo(AbVoucherinfoVO vouInfoVo,String origNo,String sbbllRngStrtSn, String sbbllRngEndSn){
		List<AbBackbillinfo> listInfo=null;
		boolean isDesc=abVoucherinfoViceService.isBillBackDesc();
		if(StringUtils.isNotEmpty(origNo)){
			listInfo=abBackbillinfoDao.listAbBackbillinfoByOrigNo(origNo,isDesc,sbbllRngStrtSn,sbbllRngEndSn);
		}else{
			listInfo=new ArrayList<AbBackbillinfo>();
		}
		//华兴玻璃-工商银行票据背书给外部单位不显示后手信息，由本地获取数据显示,根据票据号银行接口为工商银行、线上外部背书业务完成的数据
		List<AbAccrecordsVO> endorseAccList = abAccrecordsService.getAbAccrecordsVOByOrigNo(origNo,sbbllRngStrtSn,sbbllRngEndSn);
		if(CollectionUtils.isEmpty(listInfo)){
			return dealSelListAndGetResult(isDesc,endorseAccList,listInfo,vouInfoVo);
		}
		//排除重复数据
		List<AbAccrecordsVO> endorseAccList2 = new ArrayList<>();
		Set<String> set = new HashSet<>();
		for(AbBackbillinfo info : listInfo){
			set.add(ObjectUtils.nullSafeToString(info.getReqNm()) + ObjectUtils.nullSafeToString(info.getRecNm())
					+ ObjectUtils.nullSafeToString(info.getBankendrsmtmk()) + DateUtils.getDateYMD(info.getBussdt()));
		}
		for(AbAccrecordsVO endorseAcc: endorseAccList){
			if(set.contains(ObjectUtils.nullSafeToString(endorseAcc.getReqNm()) + ObjectUtils.nullSafeToString(endorseAcc.getRecNm())
					+ ObjectUtils.nullSafeToString(endorseAcc.getBankendrsmtmk()) + DateUtils.getDateYMD(endorseAcc.getBusinessDate()))){
				continue;
			}
			endorseAccList2.add(endorseAcc);
		}
		endorseAccList = endorseAccList2;
		return dealSelListAndGetResult(isDesc,endorseAccList,listInfo,vouInfoVo);
	}

	/**
	 * 根据票据号和子区间号获取票据背面信息
	 * @param: origNO
	 * @param: sbbllRngStrtSn
	 * @param: sbbllRngEndSn
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbBackbillinfo> listAbBackbillinfoByOrigNoAndsbbllRngSn(AbVoucherinfo info, String bustype){
		boolean isDesc=abVoucherinfoViceService.isBillBackDesc();
		return abBackbillinfoDao.listAbBackbillinfoByOrigNoAndsbbllRngSn(info ,isDesc , bustype);
	}
}
