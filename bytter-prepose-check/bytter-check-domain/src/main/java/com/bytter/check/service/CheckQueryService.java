package com.bytter.check.service;
import com.bytter.check.dao.CheckQueryDao;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.vo.FcCompareAccTempVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccSubjectDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
* @Title: Service
* @Description: FC_COMPARE_ACC
* @author onlineGenerator
* @date 2017-06-02 17:21:47
* @version 1.0
*
*/
@Service("checkQueryService")
@Transactional
public class CheckQueryService extends BaseService<FcCompareAcc,String> {
	private static final FcCompareAcc FcCompareAcc = null;
	@Autowired
	private CheckQueryDao checkQueryDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	@Override
	public BaseDao<FcCompareAcc, String> getEntityDao() {
		return checkQueryDao;
	}
	/**查询erp对账table表**/
	@Transactional(readOnly = true)
	public DataTablesOutput listByTable(DataTablesInput input,String resource,String userId,String type) {
		List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
		//查询账户
		List<String> accS=new ArrayList<>();
		//单位查询条件
//		String corpId = input.getFormdata().get("eqs_corpId").toString();
		String corpId = input.getCurrentUser().getCorpId();
		if(EmptyUtils.isNotEmpty(corpId)){
//			corpAuth = new ArrayList<>();
			corpAuth.add(corpId);
			//清除掉单位查询条件
//			input.getFormdata().remove("eqs_corpId");
		}
		if ("1".equals(type)) {
			List<FcAccSubjectDTO> fcAccList=fcAccService.getAccCurDataByCorpIds(corpAuth);
			for(FcAccSubjectDTO fcAcc:fcAccList){
				accS.add(fcAcc.getBlnctrAcc());
			}
		} else {
			List<BtBankAccDTO> accList=btBankAccService.getListByCorpIds(corpAuth);
			for(BtBankAccDTO acc:accList){
				accS.add(acc.getBankAcc());
			}
		}
		DataTablesOutput output = checkQueryDao.listByTable(input, accS, type);
		List<FcCompareAccTempVo> voList=output.getData();
		if(voList.size()>0) {
			Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
			for (FcCompareAccTempVo vo : voList) {
				if(StringUtils.isNotEmpty(vo.getCurCode())) {
					vo.setCurName(curMap.get(vo.getCurCode()).getCurName());
				}
			}
		}
		return output;
	}
	@Transactional(readOnly = true)
	public List<FcCompareAcc> findListBySql(String eqs_compAcc,Date ged_tallyDate,Date led_tallyDate2,String eqs_compareSign){
		return checkQueryDao.findListBySql(eqs_compAcc,ged_tallyDate,led_tallyDate2,eqs_compareSign);
	}
	
	@Transactional(readOnly = true)
	public List<FcCompareAcc> listQueryAll(){
		return checkQueryDao.listQueryAll();
	}
	 
	
}
