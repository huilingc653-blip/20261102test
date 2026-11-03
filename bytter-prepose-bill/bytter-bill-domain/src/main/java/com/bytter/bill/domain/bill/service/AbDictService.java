package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.ListUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 字典服务类
 * @author zhenghj
 *
 */
@Service("abDictService")
@Transactional
public class AbDictService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IDictService dictService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 根据字典代码字典信息
	 * @param code
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<DictDTO> list(String code){
		return dictService.list(code);
	}
	/**
	 * 根据字典代码字典信息
	 * @param code
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String,DictDTO> listMap(String code){
		Map<String,DictDTO> dictMap=new HashMap<>();
		List<DictDTO> list=list(code);
		if(ListUtils.isNotNullOrEmpty(list)){
			for(DictDTO dictDTO:list){
				dictMap.put(dictDTO.getCode(), dictDTO);
				dictMap.put(dictDTO.getName(), dictDTO);
			}
		}
		return dictMap;
	}
	
}
