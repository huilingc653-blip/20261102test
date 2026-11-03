package com.bytter.biz.finacial.paramconfig.service.impl;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.dto.FcBusRaceDTO;
import com.bytter.biz.finacial.api.dto.FcBusRacePoolDTO;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.paramconfig.entity.FcBusRace;
import com.bytter.biz.finacial.paramconfig.entity.FcBusRacePool;
import com.bytter.biz.finacial.paramconfig.service.FcBusRacePoolService;
import com.bytter.biz.finacial.paramconfig.service.FcBusRaceService;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRaceVo;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
* @Title: Service
* @Description: FC_BUS_RACE
* @author lixi
* @date 2017-05-17 10:17:16
* @version 1.0
*
*/
@Service("fcBusRaceServiceImpl")
@Transactional
@DubboService
public class FcBusRaceServiceImpl implements IFcBusRaceService {
	@Autowired
	private FcBusRaceService fcBusRaceService;
	@Autowired
	private FcBusRacePoolService fcBusRacePoolService;
	
	
	/**
	 * 功能介绍 : 根据系统品种代码获取品种代码。<br>
	 */
	@Override
    public List<FcBusRaceDTO> findBySysRaceCode(String sysRaceCode) {
		List<FcBusRaceVo> list = fcBusRaceService.findBySysRaceCode(sysRaceCode);
		return	BeanUtils.copyListProperties(list, FcBusRaceDTO.class);
	}
	
	/**
	 * 保存fcbusrace
	 */	
	public void save(FcBusRaceDTO dto, String[] netCodeArr, String userId) {
		FcBusRace fc = new FcBusRace();
		BeanUtils.copyNotNullProperties(dto, fc);
		fcBusRaceService.save(fc, netCodeArr, userId);
	}
	
	/**
	 * 功能介绍 : 根据系统品种代码和网点获取品种代码。<br>
	 */
	@Override
	public List<FcBusRaceDTO> findByNetIdSysRaceCode(String sysRaceCode , String netId) {
		List<FcBusRaceVo> list = fcBusRaceService.findByNetIdSysRaceCode(sysRaceCode, netId);
		return	BeanUtils.copyListProperties(list, FcBusRaceDTO.class);
	}
	@Override
	public String getNetBusRace(String sysRaceCode, String netId, String userId) {
		ConstantBean<String> raceCode = FinaBookConstant.getSysRaceBean(sysRaceCode);
		if (null != raceCode) {
			return fcBusRaceService.getAndSaveNetBusRace(netId, raceCode, userId);
		}
		return "";
	}
	@Override
	public FcBusRaceDTO findFcBusRaceById(String busRaceId) {
		FcBusRaceVo busRaceVo = fcBusRaceService.findFcBusRaceById(busRaceId);
		if (null != busRaceVo) {
			return BeanUtils.copyNewInstanceProperties(busRaceVo, FcBusRaceDTO.class);
		}
		return null;
	}
	@Override
	public List<FcBusRacePoolDTO> findByPoolSysRaceCode(String sysRaceCode) {
		List<FcBusRacePool> fcBusRacePool =  fcBusRacePoolService.findBySysRaceCode(sysRaceCode);
		return	BeanUtils.copyListProperties(fcBusRacePool, FcBusRacePoolDTO.class); 
	}
	@Override
	public void saveRacePool(FcBusRacePoolDTO fcBusRacePoolDTO, String[] poolIds, String userId) {
		FcBusRacePool fc = new FcBusRacePool();
		BeanUtils.copyNotNullProperties(fcBusRacePoolDTO, fc);
		fcBusRacePoolService.save(fc, poolIds, userId);
	}
    /**
     * 功能介绍 : 根据获取所有结算中心的品种代码。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh
     */
    @Override
    public List<FcBusRaceDTO> findAllRaceCode() {
        return fcBusRaceService.findAllRaceCode();
    }
	@Override
	public FcBusRaceDTO get(String busRaceId) {
		FcBusRaceDTO fcBusRaceDTO = new FcBusRaceDTO();
		FcBusRace fcBusRace = fcBusRaceService.get(busRaceId);
		BeanUtils.copyProperties(fcBusRace,fcBusRaceDTO);
		return fcBusRaceDTO;
	}
	@Override
	public FcBusRaceDTO get(String busRaceId, boolean eager) {
		FcBusRaceDTO fcBusRaceDTO = new FcBusRaceDTO();
		FcBusRace fcBusRace = fcBusRaceService.get(busRaceId,eager);
		BeanUtils.copyProperties(fcBusRace,fcBusRaceDTO);
		return fcBusRaceDTO;
	}
}
