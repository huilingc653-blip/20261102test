package com.bytter.biz.finacial.interest.service;
import com.bytter.biz.finacial.interest.dao.FcPredrawDtlDao;
import com.bytter.biz.finacial.interest.entity.FcPredrawDtl;
import com.bytter.biz.finacial.interest.vo.FcPredrawDtlVo;
import com.bytter.biz.finacial.interest.vo.InterestFactSheetVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.ISystemForeignService;
import com.bytter.system.api.dto.FcAccSubjectDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: FC_PREDRAW_DTL
* @author onlineGenerator
* @date 2017-06-28 13:55:58
* @version 1.0
*
*/
@Service("fcPredrawDtlService")
@Transactional
public class FcPredrawDtlService extends BaseService<FcPredrawDtl,String> {
	@Autowired
	private FcPredrawDtlDao fcPredrawDtlDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private ISystemForeignService iSystemForeignService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@Transactional(readOnly = true)
	public DataTablesOutput listAll(DataTablesInput input,String resource,String userId){
		List<String> corpAuth = corpService.getUserCorpIds(resource,userId);
		List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccCurDateByCorpIdsAndPool(corpAuth, "2");
//		Map<String, String> map = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, FcAccSubjectDTO::getAccName));
		Map<String, String> map = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, FcAccSubjectDTO::getAccName,(key1, key2) -> key2));
		DataTablesOutput dataTablesOutput = fcPredrawDtlDao.listAll(input, corpAuth);
		List data = dataTablesOutput.getData();
		ArrayList<FcPredrawDtlVo> fcPredrawDtlVos = new ArrayList<>();
		for (Object datum : data) {
			FcPredrawDtlVo vo = (FcPredrawDtlVo)datum;
			vo.setAccName(map.get(vo.getAccount()));
			fcPredrawDtlVos.add(vo);
		}
		dataTablesOutput.setData(fcPredrawDtlVos);
		return dataTablesOutput;
	}
	public BaseDao<FcPredrawDtl, String> getEntityDao() {
		return fcPredrawDtlDao;
	}
	/**利息预提情况表
     *<b>日期：</b>2017年10月10日<br>
	 * @version 1.0
	 * @author zhengxn
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryFactSheet(DataTablesInput input,String resource,String userId){
		List<String> corpAuth = corpService.getUserCorpIds(resource,userId);
		List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccSysCorpDateByCorpIdsAndPool(corpAuth, "2");
		List<String> accounts = fcAccSubjectDTOS.stream().map(FcAccSubjectDTO::getBlnctrAcc).collect(Collectors.toList());
		Map<String, FcAccSubjectDTO> accountMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity(),(key1 , key2)-> key2));
		DataTablesOutput dataTablesOutput = fcPredrawDtlDao.queryFactSheet(input, accounts);
		ArrayList<InterestFactSheetVo> interestFactSheetVos = new ArrayList<>();
		List data = dataTablesOutput.getData();
		for (Object datum : data) {
			InterestFactSheetVo vo = (InterestFactSheetVo)datum;
			FcAccSubjectDTO fcAccSubjectDTO = accountMap.get(vo.getBlnctrAcc());
			vo.setAccName(fcAccSubjectDTO.getAccName());
			vo.setCorpId(fcAccSubjectDTO.getCorpId());
			vo.setCorpName(fcAccSubjectDTO.getCorpName());
			vo.setAccType(fcAccSubjectDTO.getAccType());
			interestFactSheetVos.add(vo);
		}
		dataTablesOutput.setData(interestFactSheetVos);
		return dataTablesOutput;
	}
	/**
	 * 根据币别id得到名称
	 * 功能介绍。<br>
	 * <b>日期：</b>2017年10月10日<br>
	 * @version 1.0
	 * @author zhengxn
	 */
	public String getCurNameById(String currencyId) {
		return fcPredrawDtlDao.getCurNameById(currencyId);
	}
}
