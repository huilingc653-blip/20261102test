package com.bytter.biz.finacial.paramconfig.service;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.paramconfig.dao.FcBusRacePoolDao;
import com.bytter.biz.finacial.paramconfig.entity.FcBusRacePool;
import com.bytter.biz.finacial.paramconfig.entity.FcSysBusRacePool;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRacePoolVo;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRaceVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
* @Title: Service
* @Description: FC_BUS_RACE
* @author zhengxn
* @date 2019-5-6
* @version 1.0
*
*/
@Service("fcBusRacePoolService")
@Transactional
public class FcBusRacePoolService extends BaseService<FcBusRacePool,String> {
	@Autowired
	private FcBusRacePoolDao fcBusRacePoolDao;
	@DubboReference(check = false)
	private Ii18nService ii18nService;
	@DubboReference(check = false)
	private IFcCorpPoolService iFcCorpPoolService;
	/**
	 * 删除对象----
	 * @param entity
	 */
	public void deleteEntity(FcBusRacePool entity){
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除----
	 * @param id
	 */
	public void delete(String id){
		super.delete(id);
	}
	/**
	 * 保存-----
	 * @param entity
	 */
	public void save(FcBusRacePool entity, String userId){
		entity.setCreateBy(userId);
		entity.setCreateTime(new Date());
		super.saveEntity(entity);
	}
	/**
	 * 更新------
	 * @param fcBusRace
	 */
	public void update(FcBusRacePool fcBusRace, String userId){
		FcBusRacePool newBusRace = new FcBusRacePool();
		BeanUtils.copyNotNullProperties(fcBusRace, newBusRace);
		//判断fcbusrace是否重复
		checkRaceCode(newBusRace);
		newBusRace.setUpdateBy(userId);
		newBusRace.setUpdateTime(new Date());
		super.updateEntity(newBusRace);
		String locale =  LocaleContextHolder.getLocale().toString();
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(newBusRace.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(newBusRace.getRaceName());
		ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	/**
	 * 批量更新网点-----
	 * @param idArr 业务总类id数组
	 * @param poolId 资金池
	 */
	public void batchUpdate(String[] idArr, String poolId, String userId){
		String[]netCodeArray = new String[1];
		netCodeArray[0] = poolId;
		List<FcBusRacePool> fcBusRacePools = fcBusRacePoolDao.findByIds(idArr);
		//获取当前网点下所有的业务总类并存在map中
		List<FcBusRacePool> poolBusRaces = fcBusRacePoolDao.findByPoolIds(netCodeArray);
		Map<String, String> busRaceMap = new HashMap<String, String>();
		for (FcBusRacePool fcBusRace : poolBusRaces) {
			String key = fcBusRace.getPoolId() + "_" + fcBusRace.getRaceCode();
			busRaceMap.put(key, key);
        }
		for (FcBusRacePool fcBusRacePool : fcBusRacePools) {
            String key = poolId + "_" + fcBusRacePool.getRaceCode();
            if (!busRaceMap.containsKey(key)) {
				fcBusRacePool.setPoolId(poolId);
				fcBusRacePool.setUpdateBy(userId);
				fcBusRacePool.setUpdateTime(new Date());
                super.updateEntity(fcBusRacePool);
                busRaceMap.put(key, key);
            }else{
				throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_UNIQUE_POOL);
            }
        }
	}
	/**
	 * 批量复制----
	 * @param ids 多个业务种类ID
	 * @param poolIds 多个资金池
	 */
	public void copyFcBusRacePool(String ids ,String poolIds, String userId){
		String[] busRaceArray = ids.split(",");
		String[] poolIdArray = poolIds.split(",");
		List<FcBusRacePool> copyBusRaces = new ArrayList<FcBusRacePool>();
		List<FcBusRacePool> fcBusRaces = fcBusRacePoolDao.findByIds(busRaceArray);
		//获取当前网点下所有的业务总类并存在map中
		List<FcBusRacePool> poolBusRaces = fcBusRacePoolDao.findByPoolIds(poolIdArray);
		Map<String, String> busRaceMap = new HashMap<String, String>();
		for (FcBusRacePool fcBusRace : poolBusRaces) {
			String key = fcBusRace.getPoolId() + "_" + fcBusRace.getRaceCode();
			busRaceMap.put(key, key);
        }
		//判断每一个需要保存的业务种类是否已存在
		for (FcBusRacePool fcBusRace : fcBusRaces) {
            for (String poolId : poolIdArray) {
				String netCodeTemp = fcBusRace.getPoolId();
                if (poolId==netCodeTemp) {
                    continue;
                }
				FcBusRacePool fcBusRaceTemp = new FcBusRacePool();
                BeanUtils.copyNotNullProperties(fcBusRace,fcBusRaceTemp);
                fcBusRaceTemp.setPoolId(poolId);
                fcBusRaceTemp.setId(null);
                String key = fcBusRaceTemp.getPoolId() + "_" + fcBusRaceTemp.getRaceCode();
                if (!busRaceMap.containsKey(key)) {
					fcBusRaceTemp.setCreateBy(userId);
					fcBusRaceTemp.setCreateTime(new Date());
					fcBusRaceTemp.setValidSign(FinacialConstant.VALID_TRUE);
					copyBusRaces.add(fcBusRaceTemp);
                    busRaceMap.put(key, key);
                }else{
					throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_UNIQUE_POOL);
				}
            }
        }
		batchSave(copyBusRaces);
	}
	/**
	 * 保存or更新------
	 * @param entity
	 */
	public void saveOrUpdate(FcBusRacePool entity){
		super.saveOrUpdate(entity);
	}
	public BaseDao<FcBusRacePool, String> getEntityDao() {
		return fcBusRacePoolDao;
	}
	/**
	 * 根据资金池Id和业务总类代码查询fcbusracepool --------
	 * @param raceCode 业务总类编号
	 * @param poolId 网点
	 */
	@Transactional(readOnly = true)
	public List<FcBusRacePool> findAllBy(String id , String raceCode,String poolId){
		return fcBusRacePoolDao.findAllBy(id , raceCode, poolId);
	}
	/**
	 * 判断fcbusrace是否重复------
	 * @param fcBusRacePool
	 * @return
	 */
	public void checkRaceCode(FcBusRacePool fcBusRacePool) {
        String checkCode = fcBusRacePool.getRaceCode();
        String poolId = fcBusRacePool.getPoolId();
        List<FcBusRacePool> races = findAllBy( fcBusRacePool.getId(),checkCode,poolId);
        if(!races.isEmpty() && races.size() > 0){
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_UNIQUE_POOL);
        }
    }
	/**
	 * 保存fcbusrace------------
	 * @param fcBusRacePool
	 * @param poolIds 资金池id
	 * @return
	 */
	public void save(FcBusRacePool fcBusRacePool,String[] poolIds, String userId){
		for (String poolId : poolIds) {
			FcBusRacePool tempFcBusRacePool = new FcBusRacePool();
			BeanUtils.copyNotNullProperties(fcBusRacePool,tempFcBusRacePool);
			tempFcBusRacePool.setPoolId(poolId);
            //检查是否重复
			checkRaceCode(tempFcBusRacePool);
			tempFcBusRacePool.setCreateBy(userId);
			tempFcBusRacePool.setCreateTime(new Date());
			tempFcBusRacePool.setValidSign(FinacialConstant.VALID_TRUE);
            super.saveEntity(tempFcBusRacePool);
		}
		fcBusRacePoolDao.flush();
	}
	/**
	 * 系统业务种类--------
	 */
	@Transactional(readOnly = true)
	public List<FcSysBusRacePool> getListBySysRaceCode(){
		return fcBusRacePoolDao.getListBySysRaceCode();
	}
	/*
	* 资金池业务种类列表-------
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput findListBysql(DataTablesInput input){
		DataTablesOutput dataTablesOutput = fcBusRacePoolDao.findListBysql(input);
		List data = dataTablesOutput.getData();
		ArrayList<String> poolIds = new ArrayList<>();
		ArrayList<FcBusRacePoolVo> fcBusRacePoolVos = new ArrayList<>();
		for (Object datum : data) {
			FcBusRacePoolVo vo = (FcBusRacePoolVo)datum;
			poolIds.add(vo.getPoolId());
			fcBusRacePoolVos.add(vo);
		}
		Map<String, String> poolNameMap = iFcCorpPoolService.getPoolNameByIds(poolIds);
		fcBusRacePoolVos.forEach( a -> {
			a.setPoolName(poolNameMap.get(a.getPoolId()));
		});
		dataTablesOutput.setData(fcBusRacePoolVos);
		return dataTablesOutput;
	}
	/**
	 * 根据poolIds查询------
	 * @param poolIds
	 */
	@Transactional(readOnly = true)
	public List<FcBusRacePool> findByPoolIds(String[] poolIds){
		return fcBusRacePoolDao.findByPoolIds(poolIds);
	}
	/**
	 * 功能介绍 : 根据系统品种代码获取品种代码。<br>-----
	 */
	@Transactional(readOnly = true)
	public List<FcBusRacePool> findBySysRaceCode(String sysRaceCode) {
		return fcBusRacePoolDao.findBySysRaceCode(sysRaceCode);
	}
	/**
	 * 功能介绍 : 根据系统品种代码和资金池获取品种代码。<br>------
	 */
	@Transactional(readOnly = true)
	public List<FcBusRacePool> findByPoolIdSysRaceCode(String sysRaceCode, String poolId) {
		return fcBusRacePoolDao.findByPoolIdSysRaceCode(sysRaceCode, poolId);
	}
	/**
	 * 功能介绍 : 获取资金池的系统业务品种，如果不存在则新增。<br>-----
	 */
	public String getAndSavePoolBusRace(String poolIds, ConstantBean<String> sysRaceCode, String userId) {
		//获取系统业务品种对应的品种代码
        List<FcBusRacePool> raceList = findBySysRaceCode(sysRaceCode.get());
        
        //比较网点是否相同，相同则取网点维护的业务品种ID，否则取
		FcBusRacePool tempRace = null;
        String busRaceId = "";
        for (FcBusRacePool busRace : raceList) {
			String netId = busRace.getPoolId();
			if (!StringUtils.isEmpty(netId) && poolIds.equals(netId)) {
				tempRace = busRace;
				busRaceId = busRace.getId();
				break;
			}
		}
        if (null == tempRace) {
			if (raceList.isEmpty() || raceList.size() == 0) {
				tempRace = new FcBusRacePool();
				tempRace.setPoolId(poolIds);
				tempRace.setRaceCode(sysRaceCode.get());
				tempRace.setRaceName(sysRaceCode.getName());
				tempRace.setSysRaceCode(sysRaceCode.get());
				save(tempRace, new String[]{poolIds}, userId);
				raceList = findByPoolIdSysRaceCode(sysRaceCode.get(), poolIds);
				busRaceId = raceList.get(0).getId();
			} else {
				busRaceId = raceList.get(0).getId();
			}
		}
        return busRaceId;
	}
	/**
	 * 功能介绍 : 根据id查询业务种类详情-查看页面。<br>----
	 */
	@Transactional(readOnly = true)
	public FcBusRaceVo findFcBusRacePoolById(String id) {
		FcBusRaceVo vo = fcBusRacePoolDao.findFcBusRacePoolById(id);
		Map<String, String> map = iFcCorpPoolService.getPoolNameByIds(Lists.newArrayList(vo.getPoolId()));
		vo.setNetName(map.get(vo.getPoolId()));
		return vo;
	}
	/**
	 * 删除业务总类  判断fc_accbook fc_accbook_blned是否引用------
	 */
	public void deleteFcbusRacePool(String id){
		FcBusRacePool fcBusRace = this.get(id);
		//判断fcBusRace是否被引用
		if(fcBusRacePoolDao.isUsed(id,fcBusRace.getPoolId(),null,null)){
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_ISUSED);
		}
		fcBusRace.setValidSign(FinacialConstant.VALID_FALSE);
		this.updateEntity(fcBusRace);
		ii18nService.deleteBusinessBaseContext(fcBusRace.getId());
	}
	/**
	 * 获取资金池拥有的业务总类-------
	 */
	@Transactional(readOnly = true)
	public List<FcBusRacePool> getPoolIdRaceCode(String poolId) {
		return fcBusRacePoolDao.getPoolIdRaceCode(poolId);
	}
	/**初始化------
	 * **/
	public void initData(){
		List<FcBusRacePool> racePools = fcBusRacePoolDao.getFcBusRacePool();
		if(racePools == null || racePools.size() < 1) {
			//fcBusRacePoolDao.deleteFcBusRacePool();--不让直接删除
			fcBusRacePoolDao.importSysBusRacePool(null);
		}else{
			List<String> ids = new ArrayList<String>();
			Set<String> raceCode = new HashSet<String>();
			for (FcBusRacePool r:racePools) {
				if("0".equals(r.getValidSign())) {
					ids.add(r.getId());
					continue;
				}
				raceCode.add(r.getRaceCode());
			}
			fcBusRacePoolDao.deleteFcBusRacePool(ids);
			fcBusRacePoolDao.importSysBusRacePool(raceCode);
		}
	}
	public boolean isUsed(String id,String poolId,String curId,String voucherType){
		return fcBusRacePoolDao.isUsed(id,poolId,curId,voucherType);
	}
} 
