package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 票据单位处理服务类
 * @author zhenghj
 *
 */
@Service("abCorpService")
@Transactional
public class AbCorpService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private ICorpService corpService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 通过ID查找网点、单位
	 *
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public CorpDTO get(String id){
		return corpService.get(id);
	}
	/**
	 * 获取当前用户对应资源的单位或网点id
	 *
	 * @param resource 资源
	 * @param userId   用户ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<String> getUserCorpIds(String resource, String userId){
		return corpService.getUserCorpIds(resource,userId);
	}
	/**
	 * 根据网点ID，获取该网点下直属的单位(不跨网点获取)
	 *
	 * @param netId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<CorpDTO> listUnderCorpByNetId(String netId){
		return corpService.listUnderCorpByNetId(netId);
	}
	/**
	 * 根据单位名称查找单位
	 *
	 * @param name
	 * @return
	 */
	@Transactional(readOnly = true)
	public CorpDTO getCorpIdByName(String name){
		return corpService.getCorpIdByName(name);
	}
	/**
	 * 根据单位代码查找单位信息
	 *
	 * @param code
	 * @return
	 */
	@Transactional(readOnly = true)
	public CorpDTO getCorpByCode(String code){
		return corpService.getCorpByCode(code);
	}
	/**
	 * 判断两个单位是否所属同一网点下
	 *
	 * @param corpId1
	 * @param corpId2
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isTheSameNetByCorpId(String corpId1, String corpId2){
		return corpService.isTheSameNetByCorpId(corpId1,corpId2);
	}
	/**
	 * 根据单位ID，获取该单位的直属网点
	 *
	 * @param corpId
	 * @return
	 */
	@Transactional(readOnly = true)
	public CorpDTO getNetByCorpId(String corpId){
		return corpService.getNetByCorpId(corpId);
	}
	/**
	 * 获取资源下的单位ID（添加所用账号单位）
	 *
	 * @param resource 资源
	 * @param userId 用户id
	 * @return
	 * @author zhenghj 2022-5-31
	 */
	public List<String> getUserCorpIdsByUseAccount(String resource, String userId) {
		return corpService.getUserCorpIdsByUseAccount(resource, userId);
	}
	public List<Map<String,Object>> getCorpAndNetInfoByCorpId(List<String> corpIdList){
		if(CollUtil.isNotEmpty(corpIdList)){
			corpIdList=corpIdList.stream().filter(s-> StrUtil.isNotEmpty(s) && s.length()==32).distinct().collect(Collectors.toList());
		}
		if(CollUtil.isEmpty(corpIdList)){
			return new ArrayList<>();
		}
		return corpService.getCorpAndNetInfoByCorpId(corpIdList);
	}
	public Map<String, CorpDTO> listCorpDtoByCorpIds(List<String> corpIds){
		if(CollUtil.isNotEmpty(corpIds)){
			corpIds=corpIds.stream().filter(StrUtil::isNotEmpty).distinct().collect(Collectors.toList());
		}
		if(CollUtil.isEmpty(corpIds)){
			return new HashMap<>();
		}
		return corpService.listCorpDtoByCorpIds(corpIds);
	}
}
