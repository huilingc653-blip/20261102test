package com.bytter.biz.finacial.paramconfig.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.FcBusRaceDTO;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.paramconfig.dao.FcBusRaceDao;
import com.bytter.biz.finacial.paramconfig.entity.FcBusRace;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRaceVo;
import com.bytter.biz.finacial.paramconfig.vo.FcSysBusRaceVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
* @Title: Service
* @Description: FC_BUS_RACE
* @author lixi
* @date 2017-05-17 10:17:16
* @version 1.0
*
*/
@Service("fcBusRaceService")
@Transactional
public class FcBusRaceService extends BaseService<FcBusRace,String> {
	@Autowired
	private FcBusRaceDao fcBusRaceDao;
	@DubboReference(check = false)
	private Ii18nService ii18nService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcBusRace entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(FcBusRace entity, String userId){
 		entity.setCreateBy(userId);
 		entity.setCreateTime(new Date());
 		super.saveEntity(entity);
 	}
 	
	/**
	* 更新
	* @param fcBusRace
	*/
	public void update(FcBusRace fcBusRace, String userId){
		FcBusRace newBusRace = new FcBusRace();
		BeanUtils.copyNotNullProperties(fcBusRace, newBusRace);
		//判断fcbusrace是否重复
		checkRaceCode(newBusRace);
		newBusRace.setUpdateBy(userId);
		newBusRace.setUpdateTime(new Date());
		super.updateEntity(newBusRace);
		 
		String locale = LocaleContextHolder.getLocale().toString();
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(newBusRace.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(newBusRace.getRaceName());
		ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	
	/**
	* 批量更新网点
	* @param idArr 业务总类id数组
	* @param netId 网点
	*/
	public void batchUpdate(String[] idArr, String netId, String userId){
		String[]netCodeArray = new String[1];
		netCodeArray[0] = netId;
		List<FcBusRace> batchBusRaces = new ArrayList<FcBusRace>();
	    List<FcBusRace> fcBusRaces = fcBusRaceDao.findByIds(idArr);
	    //获取当前网点下所有的业务总类并存在map中
	    List<FcBusRaceVo> netBusRaces = fcBusRaceDao.findByNetIds(netCodeArray);
	    Map<String, String> busRaceMap = new HashMap<String, String>();
	    for (FcBusRaceVo fcBusRace : netBusRaces) {
	        String key = fcBusRace.getNetId() + "_" + fcBusRace.getRaceCode();
	        busRaceMap.put(key, key);
        }
	    for (FcBusRace fcBusRace : fcBusRaces) {
            String key = netId + "_" + fcBusRace.getRaceCode();
            if (!busRaceMap.containsKey(key)) {
                fcBusRace.setNetId(netId);
                fcBusRace.setUpdateBy(userId);
                fcBusRace.setUpdateTime(new Date());
                super.updateEntity(fcBusRace);
                busRaceMap.put(key, key);
            }else{
            	throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_UNIQUE);
            }
        }
	    
	}
	
	/**
	* 批量复制
	* @param ids 多个业务种类ID
	* @param netIds 多个网点
	*/
	public void copyFcBusRace(String ids ,String netIds, String userId){
	    String[] busRaceArray = ids.split(",");
	    String[] netCodeArray = netIds.split(",");
	    List<FcBusRace> copyBusRaces = new ArrayList<FcBusRace>();
	    List<FcBusRace> fcBusRaces = fcBusRaceDao.findByIds(busRaceArray);
	    //获取当前网点下所有的业务总类并存在map中
	    List<FcBusRaceVo> netBusRaces = fcBusRaceDao.findByNetIds(netCodeArray);
	    Map<String, String> busRaceMap = new HashMap<String, String>();
	    for (FcBusRaceVo fcBusRace : netBusRaces) {
	        String key = fcBusRace.getNetId() + "_" + fcBusRace.getRaceCode();
	        busRaceMap.put(key, key);
        }
	    //判断每一个需要保存的业务种类是否已存在
	    for (FcBusRace fcBusRace : fcBusRaces) {
            for (String netId : netCodeArray) {
            	String netCodeTemp = fcBusRace.getNetId();
                if (netId==netCodeTemp) {
                    continue;
                }
                FcBusRace fcBusRaceTemp = new FcBusRace();
                BeanUtils.copyNotNullProperties(fcBusRace,fcBusRaceTemp);
                fcBusRaceTemp.setNetId(netId);
                fcBusRaceTemp.setId(null);
                String key = fcBusRaceTemp.getNetId() + "_" + fcBusRaceTemp.getRaceCode();
                if (!busRaceMap.containsKey(key)) {
                	fcBusRaceTemp.setCreateBy(userId);
                	fcBusRaceTemp.setCreateTime(new Date());
                	fcBusRaceTemp.setValidSign(FinacialConstant.VALID_TRUE);
                	copyBusRaces.add(fcBusRaceTemp);
                    busRaceMap.put(key, key);
                }else{
                	throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_UNIQUE);
                	
                }
            }
        }
	    batchSave(copyBusRaces);
	}
	
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcBusRace entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcBusRace, String> getEntityDao() {
		return fcBusRaceDao;
	}
	
	/**
	* 根据网点和业务总类代码查询fcbusrace
	* @param raceCode 业务总类编号
	* @param netId 网点
	*/
	@Transactional(readOnly = true)
	public List<FcBusRace> findAllBy(String id , String raceCode,String netId){
		return fcBusRaceDao.findAllBy(id , raceCode, netId);
	}
	
	/**
	 * 判断fcbusrace是否重复
	 * @param fcBusRace
	 * @return
	 */
	public void checkRaceCode(FcBusRace fcBusRace) {
        String checkCode = fcBusRace.getRaceCode();
        String netId = fcBusRace.getNetId();
        List<FcBusRace> races = findAllBy( fcBusRace.getId(),checkCode,netId);
        if(!races.isEmpty() && races.size() > 0){
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_UNIQUE);
        }
    }
	
	/**
	 * 保存fcbusrace
	 * @param fcBusRace
	 * @param netCodeArr 网点数组
	 * @return
	 */	//TODO 月结参数设置为日期区间，则必须要设置月结区间才能月结。
	public void save(FcBusRace fcBusRace,String[] netCodeArr, String userId){
		for (String netCode : netCodeArr) {
			FcBusRace tempFcBusRace = new FcBusRace();
			BeanUtils.copyNotNullProperties(fcBusRace,tempFcBusRace);
			tempFcBusRace.setNetId(netCode);
            //检查是否重复
			checkRaceCode(tempFcBusRace);
            tempFcBusRace.setCreateBy(userId);
            tempFcBusRace.setCreateTime(new Date());
            tempFcBusRace.setValidSign(FinacialConstant.VALID_TRUE);
            super.saveEntity(tempFcBusRace);
		}
		fcBusRaceDao.flush();
	}
	
	/**
	 * 系统业务种类
	 */
	@Transactional(readOnly = true)
	public List<FcSysBusRaceVO> getListBySysRaceCode(){
		return fcBusRaceDao.getListBySysRaceCode();
	}
	
	@Transactional(readOnly = true)
	public DataTablesOutput findListBysql(DataTablesInput input){
		DataTablesOutput<FcBusRaceVo> dataList = fcBusRaceDao.findListBysql(input);
		Optional.ofNullable(dataList.getData()).ifPresent(list -> {
			List<String> corpIdList = list.stream().map(FcBusRaceVo::getNetId).filter(StrUtil::isNotBlank).collect(Collectors.toList());
			Map<String, String> corpNameMap = iCorpService.getNameByIds(corpIdList);
			if (ObjectUtil.isNotEmpty(corpNameMap)){
				list.forEach(item-> Optional.ofNullable(corpNameMap.get(item.getNetId())).ifPresent(item::setNetName));
			}
		});
		return dataList;
	}
	
	/**
	* 根据netids查询
	* @param netIds
	*/
	@Transactional(readOnly = true)
	public List<FcBusRaceVo> findByNetIds(String[] netIds){
		return fcBusRaceDao.findByNetIds(netIds);
	}
	/**
	 * 根据netids查询
	 */
	@Transactional(readOnly = true)
	public List<FcBusRaceVo> listAllValid(){
		return fcBusRaceDao.listAllValid();
	}
	
	/**
	 * 功能介绍 : 根据系统品种代码获取品种代码。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<FcBusRaceVo> findBySysRaceCode(String sysRaceCode) {
		return fcBusRaceDao.findBySysRaceCode(sysRaceCode);
	}
	
	/**
	 * 功能介绍 : 根据系统品种代码和网点获取品种代码。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<FcBusRaceVo> findByNetIdSysRaceCode(String sysRaceCode, String netId) {
		return fcBusRaceDao.findByNetIdSysRaceCode(sysRaceCode, netId);
	}
	public FcBusRaceVo queryById(String id){
		return  fcBusRaceDao.queryById(id);
	}
	
	/**
	 * 功能介绍 : 获取网点的系统业务品种，如果不存在则新增。<br>
	 * <b>日期：</b>2017年7月17日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public String getAndSaveNetBusRace(String netCode, ConstantBean<String> sysRaceCode, String userId) {
		//获取系统业务品种对应的品种代码
        List<FcBusRaceVo> raceList = findBySysRaceCode(sysRaceCode.get());
        
        //比较网点是否相同，相同则取网点维护的业务品种ID，否则取
		FcBusRaceVo tempRace = null;
        String busRaceId = "";
        for (FcBusRaceVo busRace : raceList) {
			String netId = busRace.getNetId();
//			if(StringUtils.isEmpty(netId) || StringUtils.isEmpty(netCode)) {
//				throw new BusinessException(NetbankMessageEnum.BUSSINESSKIND_MESSAGE_NEED_NETCORP);
//			}
        	if (!StringUtils.isEmpty(netId) && !StringUtils.isEmpty(netCode)) {
        		if(netCode.equals(netId)) {
        			tempRace = busRace;
            		busRaceId = busRace.getId();
            		break;
        		}
			}
		}
        if (null == tempRace) {
			if (raceList.isEmpty() || raceList.size() == 0) {
				tempRace = new FcBusRaceVo();
				tempRace.setNetId(netCode);
				tempRace.setRaceCode(sysRaceCode.get());
				tempRace.setRaceName(sysRaceCode.getName());
				tempRace.setSysRaceCode(sysRaceCode.get());
				FcBusRace busRace = new FcBusRace();
				BeanUtils.copyProperties(tempRace,busRace);
				save(busRace, new String[]{netCode}, userId);
				raceList = findByNetIdSysRaceCode(sysRaceCode.get(), netCode);
				busRaceId = raceList.get(0).getId();
			} else {
				busRaceId = raceList.get(0).getId();
			}
		}
        return busRaceId;
	}
	
	/**
	 * 功能介绍 : 根据id查询业务种类详情。<br>
	 * <b>日期：</b>2017年8月25日<br>
	 * @version 1.0
	 * @author yangl 
	 */
	@Transactional(readOnly = true)
	public FcBusRaceVo findFcBusRaceById(String id) {
		FcBusRaceVo vo = fcBusRaceDao.findFcBusRaceById(id);
		if(null==vo){
			return null;
		}
		if(StringUtils.isNotBlank(vo.getNetId())){
			CorpDTO corpDTO = iCorpService.get(vo.getNetId());
			vo.setNetName(corpDTO.getName());
		}
		return vo;
	}
	/**
	 * 删除业务总类  判断fc_accbook fc_accbook_blned是否引用
	 */
	public void deleteFcbusRace(String id){
		FcBusRace fcBusRace = this.get(id);
		//判断fcBusRace是否被引用
		if(fcBusRaceDao.isUsed(id)){
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_ISUSED);
		}
		fcBusRace.setValidSign(FinacialConstant.VALID_FALSE);
		this.updateEntity(fcBusRace);
		ii18nService.deleteBusinessBaseContext(fcBusRace.getId());
	}
	
	/**
	 * 获取网点拥有的业务总类
	 */
	@Transactional(readOnly = true)
	public List<FcBusRace> getNetRaceCode(String netId) {
		return fcBusRaceDao.getNetRaceCode(netId);
	}
	
	/**初始化**/
	public void initData(){
		//fcBusRaceDao.deleteFcBusRace();
		fcBusRaceDao.importSysBusRace();
	}
    /**
     * 功能介绍 : 根据获取所有结算中心的品种代码。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh
     */
    public List<FcBusRaceDTO> findAllRaceCode() {
        List<FcBusRaceVo> list = fcBusRaceDao.findAllRaceCode();
        return BeanUtils.copyListProperties(list, FcBusRaceDTO.class);
    }
}
