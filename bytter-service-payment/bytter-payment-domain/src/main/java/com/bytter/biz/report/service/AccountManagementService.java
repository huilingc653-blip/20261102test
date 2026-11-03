package com.bytter.biz.report.service;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.report.dao.AccountManagementDao;
import com.bytter.biz.report.vo.NisAllocationReqVO;
import com.bytter.biz.report.vo.NisCreateDate;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhuop
 * @Description 资金归集、下拨情况汇总
 * @date 2018.6.1
 */
@Service("accountManagementService")
@Transactional
public class AccountManagementService extends BaseService<NisAllocationReqVO, String>{
	@Autowired
	private AccountManagementDao accountManagementDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
	@Override
	public BaseDao<NisAllocationReqVO, String> getEntityDao() {
		return accountManagementDao;
	}
	
	/**
	 * 功能介绍 : 资金归集、下拨明细。<br>
	 * <b>日期：</b>2018年5月30日<br>
	 * @version 1.0
	 * @author zhoup
	 */
	public DataTablesOutput getNisAllocationReq(DataTablesInput input,String resource,ICurrentUser user){
		List<String> corpAuth=corpService.getUserCorpIds(resource, user.getId());
		DataTablesOutput datas = accountManagementDao.listData(input, corpAuth);
		String changeCur=(String)input.getFormdata().get("EQs_changeCur");
		String reducedCurId=(String)input.getFormdata().get("EQs_reducedCurId");
		String rateType=(String)input.getFormdata().get("EQs_rateType");
		Date rateDate=DateUtils.getDateTime();
		if(StringUtils.isNotEmpty(input.getFormdata().get("LEd_rateDate"))){
			rateDate=DateUtils.getIntegralEndTime(DateUtils.parse((String)input.getFormdata().get("LEd_rateDate"), "yyyy-MM-dd"));
		}
		//获取汇率
		if (datas != null && datas.getData() != null) {
			List<NisAllocationReqVO> list = datas.getData();
			HashMap<String, NisAllocationReqVO> dataMap = new HashMap<>();
			for(NisAllocationReqVO vo:list){
				if (dataMap.containsKey(vo.getCurId())) {
					NisAllocationReqVO nisAllocationReqVO = dataMap.get(vo.getCurId());
					//上划
					nisAllocationReqVO.setOnDraw(nisAllocationReqVO.getOnDraw().add(vo.getOnDraw()));
					nisAllocationReqVO.setHadDraw(nisAllocationReqVO.getHadDraw().add(vo.getHadDraw()));
					//上缴
					nisAllocationReqVO.setOnHanded(nisAllocationReqVO.getOnHanded().add(vo.getOnHanded()));
					nisAllocationReqVO.setHadHanded(nisAllocationReqVO.getHadHanded().add(vo.getHadHanded()));
					//下拨
					nisAllocationReqVO.setOnDial(nisAllocationReqVO.getOnDial().add(vo.getOnDial()));
					nisAllocationReqVO.setHadDial(nisAllocationReqVO.getHadDial().add(vo.getHadDial()));
					//请款
					nisAllocationReqVO.setOnRequest(nisAllocationReqVO.getOnRequest().add(vo.getOnRequest()));
					nisAllocationReqVO.setHadRequest(nisAllocationReqVO.getHadRequest().add(vo.getHadRequest()));
				} else {
					NisAllocationReqVO nisAllocationReqVO = new NisAllocationReqVO();
					BeanUtils.copyNotNullProperties(vo, nisAllocationReqVO);
					dataMap.put(vo.getCurId(), nisAllocationReqVO);
				}
			}
			//折合币种合计
			NisAllocationReqVO totalVO = new NisAllocationReqVO();
			totalVO.setCorpName("");
			totalVO.setCurName("");
			totalVO.setCorpId("");
			for (String key : dataMap.keySet()) {
				NisAllocationReqVO vo = dataMap.get(key);
				vo.setCorpName("");
				vo.setCorpId("");
				vo.setNetName("");
				list.add(vo);
				if(CommonConstant.YES.equals(changeCur)) {
					BigDecimal curExrate = fcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), reducedCurId, DateUtils.setSpecifiedDateOfTime(rateDate), rateType);
					if(curExrate!=null) {
						//上划
						totalVO.setOnDraw(totalVO.getOnDraw() == null ? vo.getOnDraw().multiply(curExrate) : totalVO.getOnDraw().add((vo.getOnDraw().multiply(curExrate))));
						totalVO.setHadDraw(totalVO.getHadDraw() == null ? vo.getHadDraw().multiply(curExrate) : totalVO.getHadDraw().add((vo.getHadDraw().multiply(curExrate))));
						//上缴
						totalVO.setOnHanded(totalVO.getOnHanded() == null ? vo.getOnHanded().multiply(curExrate) : totalVO.getOnHanded().add((vo.getOnHanded().multiply(curExrate))));
						totalVO.setHadHanded(totalVO.getHadHanded() == null ? vo.getHadHanded().multiply(curExrate) : totalVO.getHadHanded().add((vo.getHadHanded().multiply(curExrate))));
						//下拨
						totalVO.setOnDial(totalVO.getOnDial() == null ? vo.getOnDial().multiply(curExrate) : totalVO.getOnDial().add((vo.getOnDial().multiply(curExrate))));
						totalVO.setHadDial(totalVO.getHadDial() == null ? vo.getHadDial().multiply(curExrate) : totalVO.getHadDial().add((vo.getHadDial().multiply(curExrate))));
						//请款
						totalVO.setOnRequest(totalVO.getOnRequest() == null ? vo.getOnRequest().multiply(curExrate) : totalVO.getOnRequest().add((vo.getOnRequest().multiply(curExrate))));
						totalVO.setHadRequest(totalVO.getHadRequest() == null ? vo.getHadRequest().multiply(curExrate) : totalVO.getHadRequest().add((vo.getHadRequest().multiply(curExrate))));
					}
				}
			}
			if (CommonConstant.YES.equals(changeCur)) {
				list.add(totalVO);
			}
		}
		return datas;
	}
	
	public DataTablesOutput getViewDetail(DataTablesInput dataTablesInput) {
		Map<String, Object> map=dataTablesInput.getSearchValues();
		NisCreateDate nd=new NisCreateDate();
		StringBuilder corpId= new StringBuilder((String) dataTablesInput.getFormdata().get("corpId"));
		//合计查询
		if (StringUtils.isEmpty(corpId.toString())) {
			List<String> corpAuth=corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY, dataTablesInput.getCurrentUserId());
			for (String id:corpAuth) {
				corpId.append(id).append(CommonConstant.COMMA_SPLIT);
			}
		}
		String curId=(String) dataTablesInput.getFormdata().get("curId");
		String type=(String) dataTablesInput.getFormdata().get("type");
		String status=(String) dataTablesInput.getFormdata().get("statuss");
		Date ledCreateDate = null ;
		Date gedCreateDate = null;
		Date createDate =null;
		String ledCreateDateStr = (String) dataTablesInput.getFormdata().get("ledCreateDate");
		String gedCreateDateStr = (String) dataTablesInput.getFormdata().get("gedCreateDate");
		String createDateStr = (String) dataTablesInput.getFormdata().get("createDate");
		if(StringUtils.isNotEmpty(ledCreateDateStr)){
			ledCreateDate = DateUtils.getIntegralEndTime(DateUtils.parseDate(ledCreateDateStr));
		}
		if(StringUtils.isNotEmpty(gedCreateDateStr)){
			gedCreateDate = DateUtils.getIntegralStartTime(DateUtils.parseDate(gedCreateDateStr));
		}
		if(StringUtils.isNotEmpty(createDateStr)){
			createDate = DateUtils.parseDate(createDateStr);
		}	
		nd.setCreateDate(createDate);
		nd.setLedCreateDate(ledCreateDate);
		nd.setGedCreateDate(gedCreateDate);
		return accountManagementDao.getViewDetail(nd, corpId.toString(), type, status,curId,dataTablesInput);
	}

}
