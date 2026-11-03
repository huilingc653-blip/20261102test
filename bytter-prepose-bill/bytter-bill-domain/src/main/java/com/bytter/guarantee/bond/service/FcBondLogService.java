package com.bytter.guarantee.bond.service;


import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.MapUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.bond.dao.FcBondLogDao;
import com.bytter.guarantee.bond.entity.FcBondLog;
import com.bytter.guarantee.vo.FcBondLogVO;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author fusb
 *	@version 1.0
 *  @Title FcBondLogService 
 *  @Description 保证日志 service
 *  @date 2018年9月12日
 */
@Service("fcBondLogService")
@Transactional
public class FcBondLogService extends BaseService<FcBondLog, String>{
	
	@Autowired
	private FcBondLogDao fcBondLogDao;
	@DubboReference(check = false)
	private IDictService iDictService;
	/**
	 * 新增保证金日志
	 * @param bondId 保证金id
	 * @param money  金额
	 * @param messageType 日志类型
	 * @param message  日志内容
	 * @param user 用户
	 * @return boolean
	 */
	public boolean saveFcBondMessge(String bondId,BigDecimal money,Integer messageType,String message,ICurrentUser user){
		String msgType = messageType.toString(); 
		FcBondLog fcBondLog = new FcBondLog();
		fcBondLog.setBondId(bondId);
		fcBondLog.setCreateBy(user.getLoginName());
		fcBondLog.setCreateByName(user.getUsername());
		fcBondLog.setCreateDate(new Date());
		fcBondLog.setMessage(message);
		fcBondLog.setMsgType(msgType);
		fcBondLog.setMoney(money);
		return saveFcBondLog(fcBondLog);
	}
	/**
	 * 加载日志列表信息
	 * @param bondId
	 * @param input
	 * @return
	 */
	//TODO SYS服务提供
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public  DataTablesOutput listFcBondMsg(String bondId,DataTablesInput input){
		DataTablesOutput dataTablesOutput=fcBondLogDao.listMsgData(bondId, input);
		List<FcBondLog> logList=dataTablesOutput.getData();
		Map<String,DictDTO> dictMap=new HashMap<String,DictDTO>();
		String sql="select d.ID,d.type_code as CODE,d.type_name as NAME from sys_dict_group g,sys_dict d where g.id=d.type_group_id and g.type_group_code='cms.messageType' ";
		List<Map<String, Object>> list=iDictService.getExtendTableData(sql);
		for(Map<String, Object> map:list){
			DictDTO dto=new DictDTO();
			// Oracle 返回大写 key，MySQL 保持原样；用大小写不敏感取值避免 NPE
			Object idVal = MapUtils.getMapValueIgnoreCase(map, "ID");
			Object codeVal = MapUtils.getMapValueIgnoreCase(map, "CODE");
			Object nameVal = MapUtils.getMapValueIgnoreCase(map, "NAME");
			if (idVal == null || codeVal == null || nameVal == null) {
				continue;
			}
			dto.setId(idVal.toString());
			dto.setCode(codeVal.toString());
			dto.setName(nameVal.toString());
			dictMap.put(nameVal.toString(), dto);
		}
		for(FcBondLog log:logList){
			DictDTO dictDTO=dictMap.get(log.getMessage());
			if(null!=dictDTO){
				log.setMessage(dictDTO.getCode());
			}
		}
		return dataTablesOutput;
	}
	
	/**
	   * 根据保证金id,从日志表FC_BOND_LOG中得到相应的操作信息
	   * @param bondId
	   * @return
	   */
	@Transactional(readOnly = true)
	public List<FcBondLogVO> listOpMessage(String bondId){
		return fcBondLogDao.getListOpMesageByBondId(bondId);
	}
	
	/**
	 * 删除对象
	 * @param entity 
	 */
	@Override
	public void deleteEntity(FcBondLog entity){
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除对象
	 * @param id
	 */
	@Override
	public void delete(String id){
		super.delete(id);
	}
	/**
	 * 更新新对象
	 * @param entity
	 */
	@Override
	public void updateEntity(FcBondLog entity){
		super.updateEntity(entity);
	}
	/**
	 * 新增对象
	 * @param entity
	 */
	public void save(FcBondLog entity){
		super.saveEntity(entity);
	}
	/**
	 * 保存or新增
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FcBondLog entity){
		super.saveOrUpdate(entity);
	}
	/**
	 * 新增保证金日志
	 * @param entity
	 * @return boolean 
	 */
	public boolean saveFcBondLog(FcBondLog entity){
		if(entity == null || StringUtils.isAllBlank(entity.getBondId(),entity.getMessage())){
			return false;
		}
		this.save(entity);
		return true;
	}
	@Override
	public BaseDao<FcBondLog, String> getEntityDao() {
		return fcBondLogDao;
	}
	
}
