package com.bytter.biz.finacial.paramconfig.dao;
import com.bytter.biz.finacial.paramconfig.entity.FcBusRace;
import com.bytter.biz.finacial.paramconfig.entity.FcBusRacePool;
import com.bytter.biz.finacial.paramconfig.entity.FcSysBusRacePool;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRacePoolVo;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRaceVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
* @Title: Dao
* @Description: FC_BUS_RACE
* @author onlineGenerator
* @date 2017-05-17 10:17:16
* @version 1.0
*
*/
@Repository
public class FcBusRacePoolDao extends BaseDao<FcBusRacePool,String>{
	
	/***
	 * 根据资金池id和业务总类名称查询业务总类 --------
	 */
	public List<FcBusRacePool> findAllBy(String id ,String raceCode,String poolId){
		Map<String, Object> params = new HashMap<String, Object>(); 	
		StringBuilder sb = new StringBuilder();
		sb.append(" from FcBusRacePool where raceCode=:raceCode and poolId=:poolId and validSign= '1' ");
		if(!EmptyUtils.isEmpty(id)){
			sb.append(" and id<>:id ");
			params.put("id",id);
		}
		sb.append(" order by raceCode,poolId ");
		params.put("raceCode",raceCode);
		params.put("poolId",poolId);
		return createQuery(sb.toString(),params).list();
	}
	
	/**
	* 根据ids查询所有的业务总类------
	*/
	public List<FcBusRacePool> findByIds(String[] ids){
		List<FcBusRace>list = new ArrayList<FcBusRace>();
		String hql = " from FcBusRacePool a where a.id in (:ids) and validSign= '1' order by a.poolId,a.raceCode asc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
       /* for (String id : ids) {
			FcBusRace fc = get(id);
			list.add(fc);
		}*/
		return createQuery(hql,params).list();
	}
	
	/**
	* 根据poolIds查询-------
	* @param poolIds
	*/
	public List<FcBusRacePool> findByPoolIds(String[] poolIds){
		String hql = " from FcBusRacePool a where a.poolId in (:poolIds) and validSign= '1' order by a.poolId,a.raceCode asc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("poolIds", poolIds);
        return createQuery(hql,params).list();
	}
	
	/**
     * 获取系统业务品种表FC_SYS_BUS_RACE_Pool -----
     * @param
     */
    public List<FcSysBusRacePool> getListBySysRaceCode(){
    	String sql = "select a.sys_Race_Code as sysRaceCode ,a.sys_Race_Name as sysRaceName,a.valid_Sign as validSign, a.rmk as rmk from Fc_Sys_Bus_Race_pool a order by a.sys_Race_Code asc";
        return listBySQLAliasToBean(sql.toString(),null,FcSysBusRacePool.class);  
    }
    
    /**
	 * 查询业务种类列表-----
	 * @param input
	 * @return
	 */
	public DataTablesOutput findListBysql(DataTablesInput input){
		StringBuilder sb = new StringBuilder();
		/*sb.append(" select fc.id as id , fc.race_code as raceCode , fc.race_name as raceName , c.pool_name as poolName,fc.race_name as raceNameShow,");
		sb.append(" fc.sys_race_code as sysRaceCode , fc.pool_id as poolId , fc.valid_sign as validSign ,fc.father_id as fatherId");
		sb.append(" from fc_bus_race_pool fc  ");
		sb.append(" left join fc_corp_pool c on fc.pool_id = c.id");
		sb.append(" where  1=1 ");*/
		// c.pool_name as poolName
		sb.append(" select fc.id as id , fc.race_code as raceCode , fc.race_name as raceName ,fc.race_name as raceNameShow,");
		sb.append(" fc.sys_race_code as sysRaceCode , fc.pool_id as poolId , fc.valid_sign as validSign ,fc.father_id as fatherId");
		sb.append(" from fc_bus_race_pool fc  ");
		sb.append(" where  1=1 ");
	    return getDynamicBySQL(input, sb.toString(), FcBusRacePoolVo.class, "fc", false);
	}
	
	/**
	 * 功能介绍 : 根据系统品种代码获取品种代码。<br>------
	 */
	public List<FcBusRacePool> findBySysRaceCode(String sysRaceCode) {
		String hql = "select a from FcBusRacePool a where a.sysRaceCode = (:sysRaceCode) and validSign= '1' order by a.poolId,a.raceCode asc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sysRaceCode", sysRaceCode);
        return createQuery(hql,params).list();
	}
	
	/**
	 * 功能介绍 : 根据系统品种代码和网点获取品种代码。<br>--------
	 */
	public List<FcBusRacePool> findByPoolIdSysRaceCode(String sysRaceCode, String poolId) {
		String hql = "select a from FcBusRacePool a where a.sysRaceCode = (:sysRaceCode) "
				+ " and a.poolId = :poolId order by a.poolId,a.raceCode asc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sysRaceCode", sysRaceCode);
        params.put("poolId", poolId);
        return createQuery(hql,params).list();
	}
	
	/**
	 * 功能介绍 : 根据id查看业务种类详情。<br>----
	 */
	public FcBusRaceVo findFcBusRacePoolById(String id){
		StringBuilder sql=new StringBuilder();
		/*sql.append(" select fc.id as id,fc.race_code as raceCode,fc.race_name as raceName, fc.race_name as raceNameShow,");
		sql.append(" br.sys_race_code as sysRaceCode,br.sys_race_name as sysRaceName, ");
		sql.append(" fc.pool_id as poolId,fc.father_id as fatherId,sc.pool_name as netName ,fc.valid_sign as validSign  ");
		sql.append(" from fc_bus_race_pool fc left join fc_corp_pool sc on fc.pool_id = sc.id ");
		sql.append(" left join fc_sys_bus_race_pool br on fc.sys_race_code=br.sys_race_code where fc.id=:id ");*/
		//sc.pool_name as netName
		sql.append(" select fc.id as id,fc.race_code as raceCode,fc.race_name as raceName, fc.race_name as raceNameShow,");
		sql.append(" br.sys_race_code as sysRaceCode,br.sys_race_name as sysRaceName, ");
		sql.append(" fc.pool_id as poolId,fc.father_id as fatherId,fc.valid_sign as validSign  ");
		sql.append(" from fc_bus_race_pool fc ");
		sql.append(" left join fc_sys_bus_race_pool br on fc.sys_race_code=br.sys_race_code where fc.id=:id ");
		Map<String, Object> params = new HashMap<String, Object>(); 	
		params.put("id",id);
		return getBySQLAliasToBean(sql.toString(),params,FcBusRaceVo.class);
	}
	
	/***
	 * 判断是否被引用------
	 */
	public boolean isUsed(String id,String poolId,String curId,String voucherType) {
		StringBuilder sql=new StringBuilder();
		sql.append(" select 1 from fc_accbook b inner join fc_bus_race_pool a on a.id = b.bus_race_id where a.id=:id and b.net_id = :poolId ");
		if(curId != null && voucherType != null){
			sql.append(" and b.cur_code = :curId and b.voucher_type = :voucherType");
		}
		sql.append(" union all ");
		sql.append(" select 1 from fc_accbook_blned c inner join fc_bus_race_pool a on a.id = c.bus_race_id where a.id=:id and c.net_id = :poolId ");
		if(curId != null && voucherType != null){
			sql.append(" and c.cur_code = :curId and c.voucher_type = :voucherType");
		}
		Map<String, Object> params = new HashMap<String, Object>(); 	
		params.put("id",id);
		params.put("poolId",poolId);
		params.put("curId",curId);
		params.put("voucherType",voucherType);
		List list = createSQLQuery(sql.toString(),params).list();
		if(!EmptyUtils.isEmptyList(list)){
			return true;
		}
		return false;
	}
	/**
	 * 获取资金池拥有的业务总类------
	 */
	public List<FcBusRacePool> getPoolIdRaceCode(String poolId) {
		return listBy("poolId" , poolId);
	}
	
	/*
	 *删除资金池业务种类表------
	 */
	public void deleteFcBusRacePool(List<String> ids){
		StringBuilder sql = new StringBuilder();
		sql.append("delete from fc_bus_race_pool where 1=1 ");
		sql.append("and").append(getSQLIn(ids, 900, " id"));
		createSQLQuery(sql.toString()).executeUpdate();
	}
	public List<FcBusRacePool> getFcBusRacePool(){
		StringBuilder sql = new StringBuilder();
		sql.append("select * from fc_bus_race_pool ");
		return listBySQLAliasToBean(sql.toString(),null,FcBusRacePool.class);
	}
	/**
	 * 导入资金池系统业务品种表-----
	 */
	public void importSysBusRacePool(Set<String> raceCode){
		
		String querySql = " select r.sys_race_code as sysRaceCode,r.sys_race_name as sysRaceName,r.valid_sign as validSign from fc_sys_bus_race_pool r where r.valid_sign = '1' ";
		if(raceCode != null){
			List<String> raceCodes = new ArrayList<String>(raceCode);
			querySql += "and "+ getSQLIn(raceCodes, 900, " r.sys_race_code not ");
		}
		List<FcSysBusRacePool> fcSysBusRacePools = listBySQLAliasToBean(querySql, null, FcSysBusRacePool.class);
		List<FcBusRacePool> fcBusRacePools = new ArrayList<>();
		for (FcSysBusRacePool fc:fcSysBusRacePools){
			FcBusRacePool race = new FcBusRacePool();
			race.setRaceCode(fc.getSysRaceCode());
			race.setRaceName(fc.getSysRaceName());
			race.setSysRaceCode(fc.getSysRaceCode());
			race.setValidSign(fc.getValidSign());
			fcBusRacePools.add(race);
		}
		this.batchSave(fcBusRacePools);
	}
}
