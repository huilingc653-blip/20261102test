package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.dao.AbPoolPledgeeDao;
import com.bytter.bill.domain.billPool.entity.AbPoolPledgee;
import com.bytter.bill.domain.billPool.vo.AbPoolPledgeeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 
* @Title:  票据池质权人Dao
* @Package com.bytter.biz.billPool.entity   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-12-30 下午5:30:29   
* @version V1.0
 */
@Service("abPoolPledgeeService")
@Transactional
public class AbPoolPledgeeService extends BaseService<AbPoolPledgee, String>{
	@Autowired
	private AbPoolPledgeeDao abPoolPledgeeDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;
	
	
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		// 获取该用户有权限的单位id集
		List<String> corpAuthList = corpService.getUserCorpIds(AbPoolConstant.AB_POLL_PLEDGEE_RESOURCE_OP,
				input.getCurrentUserId());
		DataTablesOutput<AbPoolPledgeeVO> dataTablesOutput = abPoolPledgeeDao.listAbPoolPledgee(input, corpAuthList);
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_POOL_BP_TYPE","AB_POOL_VALID_SIGN");
			listData.forEach(item -> Optional.ofNullable(listDictMap).ifPresent(listMap -> {
				redundantValues(listDictMap, item);
			}));
		});
		return dataTablesOutput;
	}

	private static void redundantValues(Map<String, Map<String, DictDTO>> listDictMap, AbPoolPledgeeVO item) {
		Optional.ofNullable(item.getBpType()).flatMap(bpType -> Optional.ofNullable(listDictMap.get("AB_BILL_POOL_BP_TYPE"))
						.flatMap(dictMap -> Optional.ofNullable(dictMap.get(bpType))))
				.ifPresent(dict -> item.setBpTypeName(dict.getName()));
		Optional.ofNullable(item.getValidSign()).flatMap(validSign -> Optional.ofNullable(listDictMap.get("AB_POOL_VALID_SIGN"))
						.flatMap(dictMap -> Optional.ofNullable(dictMap.get(validSign))))
				.ifPresent(dict -> item.setValidSignName(dict.getName()));
	}


	public AbPoolPledgeeVO getAbPoolPledgeeVOByBpId(String bpId){
		AbPoolPledgeeVO pledgeeVO = abPoolPledgeeDao.getAbPoolPledgeeVOByBpId(bpId);
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_POOL_BP_TYPE", "AB_POOL_VALID_SIGN");
		Optional.ofNullable(listDictMap).ifPresent(listDict -> redundantValues(listDictMap, pledgeeVO));
		return pledgeeVO;
	}
	
	
	public AjaxJson settingPoolPledgeeVO(AbPoolPledgeeVO abPoolPledgeeVO,ICurrentUser user){
		AjaxJson reJson = new AjaxJson();
		//判断是否重复
		if(abPoolPledgeeDao.checkRepeat(abPoolPledgeeVO)){
			reJson.setMessageEnum(AbPoolMessageEnum.BILLPOOL_PLEDGEE_CHECK_REPEAT);
			reJson.setSuccess(false);
			return reJson;
		}
		AbPoolPledgee abPoolPledgee = new AbPoolPledgee();
		BeanUtils.copyNotNullProperties(abPoolPledgeeVO, abPoolPledgee);
		abPoolPledgee.setBpId(abPoolPledgeeVO.getpId());
		abPoolPledgee.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
		abPoolPledgee.setCreateId(user.getId());
		abPoolPledgee.setCreateTime(new Date());
		abPoolPledgeeDao.updatePoolPledgeeVO(abPoolPledgee);
		reJson.setMessageEnum(AbPoolMessageEnum.AB_POOL_MESSAGE_SUCCESS);
		reJson.setSuccess(true);
		return reJson;
	}
	
	public void updateDispose(String id,Integer opt,ICurrentUser user){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("updateTime", new Date());
		params.put("updateId", user.getId());
		params.put("id", id);
		params.put("validSign",  String.valueOf(opt));
		abPoolPledgeeDao.updateEntityByParams(params);
	}
	
	public AbPoolPledgeeVO getAbPoolPledgeeByBpId(String bpid){
		AbPoolPledgeeVO pledgeeVO = abPoolPledgeeDao.getAbPoolPledgeeByBpId(bpid);
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_POOL_BP_TYPE", "AB_POOL_VALID_SIGN");
		Optional.ofNullable(listDictMap).ifPresent(listDict -> redundantValues(listDictMap, pledgeeVO));
		return pledgeeVO;
	}
	
	
	@Override
	public BaseDao<AbPoolPledgee, String> getEntityDao() {
		return null;
	}
	
}
