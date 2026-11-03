package com.bytter.erp.service;
import com.bytter.erp.dto.ErpDynamicDataDTO;
import java.util.List;
import java.util.Map;
/**
 * 数据交换-自定义映射接口
 *
 * @author zuoy 2018-05-17
 */
public interface IErpDynamicDataService {
	/**
	 * 获取自定义数据映射
	 *
	 * @param typeCode     对应接口类型
	 * @param dateTypeCode 对应数据类型
	 * @return
	 * @author zuoy 2018-05-17
	 */
	List<ErpDynamicDataDTO> listErpDynamicDataByTypeCode(String typeCode, String dateTypeCode);
	/**
	 * 获取自定义数据映射
	 *
	 * @param typeCode     对应接口类型
	 * @param dateTypeCode 对应数据类型
	 * @return Map<String, ErpDynamicDataDTO> Map<内部系统名称，自定义数据DTO>
	 * @author zuoy 2018-05-17
	 */
	Map<String, ErpDynamicDataDTO> findErpDynamicDataByTypeCode(String typeCode, String dateTypeCode);
}
