package com.bytter.biz.finacial.paramconfig.dao;
import com.bytter.biz.finacial.paramconfig.entity.FcBusRace;
import com.bytter.biz.finacial.paramconfig.entity.FcSysBusRace;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRaceVo;
import com.bytter.biz.finacial.paramconfig.vo.FcSysBusRaceVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_BUS_RACE
* @author onlineGenerator
* @date 2017-05-17 10:17:16
* @version 1.0
*
*/
@Repository
public class FcBusRaceDao extends BaseDao<FcBusRace,String>{
	
	/***
	 * 根据网点和业务总类名称查询业务总类
	 */
	public List<FcBusRace> findAllBy(String id ,String raceCode,String netId){
		Map<String, Object> params = new HashMap<String, Object>(); 	
		StringBuilder sb = new StringBuilder();
		sb.append(" from FcBusRace where raceCode=:raceCode and netId=:netId and validSign= '1' ");
		if(!EmptyUtils.isEmpty(id)){
			sb.append(" and id<>:id ");
			params.put("id",id);
		}
		sb.append(" order by raceCode,netId ");
		params.put("raceCode",raceCode);
		params.put("netId",netId);
		return createQuery(sb.toString(),params).list();
	}
	
	/**
	* 根据ids查询所有的业务总类
	*/
	public List<FcBusRace> findByIds(String[] ids){
		List<FcBusRace>list = new ArrayList<FcBusRace>();
		String hql = "select a from FcBusRace a where a.id in (:ids) and a.validSign= '1' order by a.netId,a.raceCode asc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
		return createQuery(hql,params).list();
	}
	
	/**
	* 根据netids查询
	* @param netIds
	*/
	public List<FcBusRaceVo> findByNetIds(String[] netIds){
		String hql = "select a.* from Fc_Bus_Race a where a.net_Id in (:netIds) and valid_Sign= '1' order by a.net_Id,a.race_Code asc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netIds", netIds);
        return listBySQLAliasToBean(hql.toString(),params,FcBusRaceVo.class);
	}
	/**
	 * 获取有效的业务种类
	 */
	public List<FcBusRaceVo> listAllValid(){
		String hql = "select a.* from Fc_Bus_Race a where valid_Sign= '1' order by a.net_Id,a.race_Code asc";
		return listBySQLAliasToBean(hql.toString(),null,FcBusRaceVo.class);
	}
	
	/**
     * 获取系统业务品种表FC_SYS_BUS_RACE
     * @param
     */
    public List<FcSysBusRaceVO> getListBySysRaceCode(){
    	String sql = "select a.sys_Race_Code as sysRaceCode ,a.sys_Race_Name as sysRaceName,a.valid_Sign as validSign, a.rmk as rmk from Fc_Sys_Bus_Race a order by a.sys_Race_Code asc";
        return listBySQLAliasToBean(sql.toString(),null,FcSysBusRaceVO.class);  
    }
    
    /**
	 * 查询业务种类
	 * @param input
	 * @return
	 */
	public DataTablesOutput<FcBusRaceVo> findListBysql(DataTablesInput input){
		StringBuilder sb = new StringBuilder();
		sb.append(" select fc.id as id , fc.race_code as raceCode , fc.race_name as raceName , fc.race_name as raceNameShow, ");
		sb.append(" fc.sys_race_code as sysRaceCode , fc.net_id as netId , fc.valid_sign as validSign ,fc.father_id as fatherId");
		sb.append(" from fc_bus_race fc where  1=1 ");
	    return getDynamicBySQL(input, sb.toString(), FcBusRaceVo.class, "fc", false);
	}
	
	/**
	 * 功能介绍 : 根据系统品种代码获取品种代码。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcBusRaceVo> findBySysRaceCode(String sysRaceCode) {
		String hql = "select a.* from Fc_Bus_Race a where a.sys_Race_Code = (:sysRaceCode) and valid_Sign= '1' order by a.net_Id,a.race_Code asc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sysRaceCode", sysRaceCode);
        return listBySQLAliasToBean(hql,params,FcBusRaceVo.class);
	}
	
	/**
	 * 功能介绍 : 根据系统品种代码和网点获取品种代码。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcBusRaceVo> findByNetIdSysRaceCode(String sysRaceCode, String netId) {
        Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select a.* from Fc_Bus_Race a where 1 = 1 ";
		if (!EmptyUtils.isEmpty(sysRaceCode)) {
		    hql += " and a.sys_Race_Code = :sysRaceCode ";
            params.put("sysRaceCode", sysRaceCode);
        }
        hql += " and a.net_Id = :netId order by a.net_Id,a.race_Code asc";
        params.put("netId", netId);
        return listBySQLAliasToBean(hql,params,FcBusRaceVo.class);
	}
	
	/**
	 * 功能介绍 : 根据id查看业务种类详情。<br>
	 * <b>日期：</b>2017年08月25日<br>
	 * @version 1.0
	 * @author yangl 
	 */
	public FcBusRaceVo findFcBusRaceById(String id){
		StringBuilder sql=new StringBuilder();
		/*sql.append(" select fc.id as id,br.id as sysRaceId,fc.race_code as raceCode,fc.race_name as raceName, ");
		sql.append(" br.sys_race_code as sysRaceCode,br.sys_race_name as sysRaceName, ");
		sql.append(" fc.net_id as netId,fc.father_id as fatherId,sc.name as netName ,fc.valid_sign as validSign  ");
		sql.append(" from fc_bus_race fc left join sys_corp sc on fc.net_id = sc.id ");
		sql.append(" left join fc_sys_bus_race br on fc.sys_race_code=br.sys_race_code where fc.id=:id ");*/
		// sc.name as netName
		sql.append(" select fc.id as id, fc.net_id as netId, br.id as sysRaceId,fc.race_code as raceCode,fc.race_name as raceName, ");
		sql.append(" br.sys_race_code as sysRaceCode,br.sys_race_name as sysRaceName, ");
		sql.append(" fc.father_id as fatherId ,fc.valid_sign as validSign  ");
		sql.append(" from fc_bus_race fc ");
		sql.append(" left join fc_sys_bus_race br on fc.sys_race_code = br.sys_race_code where fc.id=:id ");
		Map<String, Object> params = new HashMap<String, Object>(); 	
		params.put("id",id);
		FcBusRaceVo vo = getBySQLAliasToBean(sql.toString(), params, FcBusRaceVo.class);
		return vo;
	}
	
	/***
	 * 判断是否被引用
	 */
	public boolean isUsed(String id) {
		StringBuilder sql=new StringBuilder();
		sql.append(" select 1 from fc_accbook b inner join fc_bus_race a on a.id = b.bus_race_id where a.id=:id ");
		sql.append(" union all ");
		sql.append(" select 1 from fc_accbook_blned c inner join fc_bus_race a on a.id = c.bus_race_id where a.id=:id ");
		Map<String, Object> params = new HashMap<String, Object>(); 	
		params.put("id",id);
		List list = createSQLQuery(sql.toString(),params).list();
		if(!EmptyUtils.isEmptyList(list)){
			return true;
		}
		return false;
	}
	public List<FcBusRace> getNetRaceCode(String netId) {
		return listBy("netId" , netId);
	}
	
	public void deleteFcBusRace(){
		StringBuilder sql = new StringBuilder();
		sql.append("delete from fc_bus_race");
		createSQLQuery(sql.toString()).executeUpdate();
	}
	
	public void importSysBusRace(){
		String querySql = " select r.sys_race_code as sysRaceCode,r.sys_race_name as sysRaceName,r.valid_sign as validSign from fc_sys_bus_race r where r.valid_sign = '1' and r.sys_race_code not in(select race_code from fc_bus_race) and r.sys_race_name not in(select race_name from fc_bus_race) ";
		List<FcSysBusRace> fcSysBusRaces = listBySQLAliasToBean(querySql, null, FcSysBusRace.class);
		List<FcBusRace> fcBusRaces = new ArrayList<>();
		for (FcSysBusRace fc:fcSysBusRaces){
			FcBusRace race = new FcBusRace();
			race.setRaceCode(fc.getSysRaceCode());
			race.setRaceName(fc.getSysRaceName());
			race.setSysRaceCode(fc.getSysRaceCode());
			race.setValidSign(fc.getValidSign());
			fcBusRaces.add(race);
		}
		this.batchSave(fcBusRaces);
	}
	
	public FcBusRaceVo queryById(String id){
		String sql = " select * from FC_BUS_RACE where id = :id ";
		Map<String,String> param = new HashMap<>();
		param.put("id",id);
		return getBySQLAliasToBean(sql,param,FcBusRaceVo.class);
	}
    /**
     * 功能介绍 : 根据获取所有结算中心的品种代码。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh
     */
    public List<FcBusRaceVo> findAllRaceCode() {
        StringBuilder sql=new StringBuilder();
        sql.append(" select fc.id as id,fc.race_code as raceCode,fc.race_name as raceName, ");
        sql.append(" fc.net_id as netId,fc.valid_sign as validSign  ");
        sql.append(" from fc_bus_race fc where valid_Sign= '1' order by fc.net_Id,fc.race_Code asc  ");
        Map<String,String> param = new HashMap<>();
        return listBySQLAliasToBean(sql.toString(), param, FcBusRaceVo.class);
    }
}
