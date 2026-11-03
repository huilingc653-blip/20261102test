package com.bytter.basic.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import com.bytter.basic.service.BtCurrencyService;
import com.bytter.basic.vo.BtCurrencyVO;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICommonUtilService;
import com.bytter.system.entity.Corp;
import com.bytter.system.service.CorpService;

@DubboService
public class CommonUtilServiceImpl implements ICommonUtilService{
	
	
	@Autowired
	@Qualifier("btCurrencyServiceBase")
    private BtCurrencyService btCurrencyServiceBase;
	
	@Autowired
	@Qualifier("corpService")
    private CorpService corpService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 单位  ID映射单位名称
	 */
	@Override
	public Map<String, String> mapListCorp() {
		String hashKey = RedisCacheKey.makeRedisKey("bytter-system", RedisCacheKey.SYS_CORP);
		Map<String, String> map = (Map<String, String>)redisTemplate.opsForValue().get(hashKey);
		if(map==null || map.size()==0) {
			List<Corp> corpDTOS = corpService.listEffectiveCorp();
	        if (EmptyUtils.isEmpty(corpDTOS)) {
	            return map;
	        }else {
	        	map = corpDTOS.stream().collect(Collectors.toMap(Corp::getId, Corp::getName));
	        	redisTemplate.opsForValue().set(hashKey, map);
	        }
		        
		}
		return map;
	}
	/**
	 *   币种ID 映射币种名称
	 */
	@Override
	public Map<String, String> mapListCurrency() {
		String hashKey = RedisCacheKey.makeRedisKey("bytter-system", RedisCacheKey.SYS_CURRENCY);
		Map<String, String> map = (Map<String, String>)redisTemplate.opsForValue().get(hashKey);
		if(map==null || map.size()==0) {
			List<BtCurrencyVO> btCurrencyDTOs = btCurrencyServiceBase.getBtCurrency();
	        if (EmptyUtils.isEmpty(btCurrencyDTOs)) {
	            return map;
	        }else {
	        	map = btCurrencyDTOs.stream().collect(Collectors.toMap(BtCurrencyVO::getId, BtCurrencyVO::getCurName));
	        	redisTemplate.opsForValue().set(hashKey, map);
	        }
		        
		}
		return map;
	}

}
