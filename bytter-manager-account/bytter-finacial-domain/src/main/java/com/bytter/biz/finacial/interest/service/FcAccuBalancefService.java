package com.bytter.biz.finacial.interest.service;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.finacial.interest.dao.FcAccuBalancefDao;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.vo.FcAccuBalancefVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.ISystemForeignService;
import com.bytter.system.api.dto.FcAccSubjectDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: FC_ACCU_BALANCE
* @author onlineGenerator
* @date 2017-06-28 14:27:04
* @version 1.0
*
*/
@Service("fcAccuBalancefService")
@Transactional
public class FcAccuBalancefService extends BaseService<FcAccuBalancef,String> {
	@Autowired
	private FcAccuBalancefDao fcAccuBalanceDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private ISystemForeignService iSystemForeignService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;

	@SuppressWarnings("unchecked")
	public FcAccuBalancef getFcAccuBalancefById(String id) {
		return fcAccuBalanceDao.getFcAccuBalancefById(id);
	}
	@Transactional(readOnly = true)
	public  DataTablesOutput listAll(DataTablesInput input,String resource,String userId,boolean isPool){
		List<String> corpAuth = corpService.getUserCorpIds(resource,userId);
		List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccAndSubjectData(corpAuth, isPool);
		Map<String, FcAccSubjectDTO> accountMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity(), (key1, key2) -> key2));
		List<String> accounts = fcAccSubjectDTOS.stream().map(FcAccSubjectDTO::getBlnctrAcc).collect(Collectors.toList());
		DataTablesOutput dataTablesOutput = fcAccuBalanceDao.listAll(input, accounts, isPool);
		if (CollectionUtils.isEmpty(accountMap)) return dataTablesOutput;
		List<FcAccuBalancefVo> data = dataTablesOutput.getData();
		data.forEach(a -> {
			FcAccSubjectDTO fcAccSubjectDTO = accountMap.get(a.getAccount());
			a.setSubjectId(fcAccSubjectDTO.getSubjectId());
			if(isPool){
				a.setSubjectPoolName(fcAccSubjectDTO.getSubjectPoolName());
			}else {
				a.setSubjectName(fcAccSubjectDTO.getSubjectName());
			}

			a.setAccname(fcAccSubjectDTO.getAccName());
		});

		return dataTablesOutput;
	}
	public List excel(FcAccuBalancefVo vo,String resource,String userId){
		List<String> corpAuth = corpService.getUserCorpIds(resource,userId);
		//拆 fc_acc、fc_subject ，4个条件 查询 blnctr_acc, acc_name, subject_name
		List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccNameAndSubjectName(vo.getEqs_netId(), vo.getIns_accType(), corpAuth);
		List<FcAccuBalancefVo> fcAccBalancefVos = fcAccuBalanceDao.excel(vo, corpAuth);
		Map<String, FcAccSubjectDTO> accoutMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity(), (a,b) -> b));
		fcAccBalancefVos.forEach(a -> {
			FcAccSubjectDTO fcAccSubjectDTO = accoutMap.get(a.getAccount());
			a.setAccname(fcAccSubjectDTO.getAccName());
			a.setSubjectName(fcAccSubjectDTO.getSubjectName());
		});
		return fcAccBalancefVos;
	}
	/**调整积数表的全部数据**/
	@Transactional(readOnly = true)
	public  DataTablesOutput getAll(DataTablesInput input,String resource,String userId){
		List<String> corpAuth = corpService.getUserCorpIds(resource,userId);
		String curId=(String)input.getFormdata().get("eqs_curCode");
		String netId=(String)input.getFormdata().get("eqs_netId");
		String subjectSort=(String)input.getFormdata().get("eqs_subjectSort");
		input.getFormdata().remove("eqs_curCode");
		input.getFormdata().remove("eqs_netId");
		input.getFormdata().remove("eqs_subjectSort");
		FcAccSubjectDTO dto = new FcAccSubjectDTO();
		dto.setCurId(curId);
		dto.setNetId(netId);
		dto.setSubjectSort(subjectSort);
		List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccAndSubject(corpAuth,dto, Boolean.FALSE);
		Map<String, FcAccSubjectDTO> accountMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity(),(a,b) -> b));
		Set<String> accounts = accountMap.keySet();
		DataTablesOutput dataTablesOutput = fcAccuBalanceDao.getAll(input, Lists.newArrayList(accounts));
		List data = dataTablesOutput.getData();
		ArrayList<FcAccuBalancefVo> fcAccuBalancefVos = new ArrayList<>();
		for (Object datum : data) {
			FcAccuBalancefVo vo = (FcAccuBalancefVo)datum;
			vo.setAccname(accountMap.get(vo.getAccount()).getAccName());
			vo.setSubjectName(accountMap.get(vo.getAccount()).getSubjectName());
			fcAccuBalancefVos.add(vo);
		}
		dataTablesOutput.setData(fcAccuBalancefVos);
		return dataTablesOutput;
	}
	/**保存数据**/
	public void saves(String ids,String loginName){
		FcAccuBalancef fcAccuBalancef = null;
		for (String id : ids.split(",")) {
			fcAccuBalancef = this.getFcAccuBalancefById(id);
			String sign = fcAccuBalancef.getInterestSign();
			if("0".equals(sign)){//未计息
				fcAccuBalancef.setInterestSign("1");//已计息
				fcAccuBalancef.setInterestTime(new Date());
			}else{//已计息
				fcAccuBalancef.setInterestSign("0");//未计息
				fcAccuBalancef.setInterestTime(null);
			}
			fcAccuBalancef.setUpdateBy(loginName);
			updateEntity(fcAccuBalancef);
		}
	}
	@Override
	public BaseDao<FcAccuBalancef, String> getEntityDao() {
		// TODO Auto-generated method stub
		return fcAccuBalanceDao;
	}
}
