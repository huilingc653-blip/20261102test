package com.bytter.biz.finacial.api.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.biz.finacial.api.dto.FcBusRaceDTO;
import com.bytter.biz.finacial.api.dto.FcBusRacePoolDTO;
/**
 * 柜台账务对外接口 
 * @author lixi
 * @since 2017-8-16
 */
public interface IFcBusRaceService {
	
	/**
	 * 功能介绍 : 根据系统品种代码获取品种代码。
	 * @param sysRaceCode 系统品种代码
	 * @return List&lt;FcBusRaceDTO&gt; 业务种类
	 */
	List<FcBusRaceDTO> findBySysRaceCode(String sysRaceCode);
	
	/**
	 * 保存fcbusrace
	 * @param fcBusRace 业务种类对象
	 * @param netCodeArr 网点Id
	 * @param userId 用户Id
	 */
	void save(FcBusRaceDTO fcBusRace,String[] netCodeArr, String userId);
	
	/**
	 * 功能介绍 : 根据系统品种代码和网点获取品种代码。
	 * @param sysRaceCode 系统业务种类编号
	 * @param netId 网点Id
	 * @return List&lt;FcBusRaceDTO&gt; 业务种类
	 */
	List<FcBusRaceDTO> findByNetIdSysRaceCode(String sysRaceCode, String netId);
	
	
	/**
	 * 功能介绍 : 自动写入网点的业务种类。
	 * @param sysRaceCode 系统业务种类编号
	 * @param netId 网点Id
	 * @param userId 用户Id
	 * @return 业务品种
	 */
	String getNetBusRace(String sysRaceCode, String netId, String userId);
	
	/**
	 * 功能介绍 ： 根据ID获取业务种类详细。
	 * @param busRaceId 业务种类id
	 * @return FcBusRaceDTO 业务种类
	 */
	FcBusRaceDTO findFcBusRaceById(String busRaceId);
	/**
	 * 获取资金池系统业务种类
	 * @param sysRaceCode 系统业务种类编号
	 * @return  List&lt;FcBusRacePoolDTO&gt; 资金池业务种类
	 */
	List<FcBusRacePoolDTO> findByPoolSysRaceCode(String sysRaceCode);
	/**
	 * 保存资金池fcbusracepool
	 * @param fcBusRacePool 系统业务种类编号
	 * @param poolIds 资金池I
	 * @param userId 用户I
	 */
	void saveRacePool(FcBusRacePoolDTO fcBusRacePool,String[] poolIds, String userId);
    /**
     * 功能介绍 : 根据获取所有结算中心的品种代码。<br>
     * <b>日期：</b>2022年3月1日<br>
     * @version 1.0
     * @author liyh
     */
    public List<FcBusRaceDTO> findAllRaceCode();
	/**
	 * 查询使用的记账规则
	 * @param busRaceId
	 * @return
	 */
	FcBusRaceDTO get(String busRaceId);
	/**
	 * 根据主键获取实体
	 * @param busRaceId  主键
	 * @param eager
	 * 是否急切获取对象，true是，false否为懒加载
	 * @return 实体对象DTO
	 */
	FcBusRaceDTO get(String busRaceId, boolean eager);
}
