package com.bytter.system.inneraccount.service.impl;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcAccLogService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcAccLogDTO;
import com.bytter.system.api.vo.FcAccLogVO;
import com.bytter.system.inneraccount.dao.FcAccLogDao;
import com.bytter.system.inneraccount.entity.FcAccLog;
import com.bytter.system.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内部账号日志
 */
@Service("fcAccLogServiceI")
@DubboService
@Transactional
public class FcAccLogServiceImpl extends BaseService<com.bytter.system.inneraccount.entity.FcAccLog,String> implements IFcAccLogService{
	@Autowired
	private FcAccLogDao fcAccLogDao;
	@Autowired
	private IDictService dictServiceI;
	@Autowired
	private UserService userService;
	/**
	* 删除对象
	* @param dto
	*/
	@Override
	public void deleteEntity(FcAccLogDTO dto){
		FcAccLog log = new FcAccLog();
		BeanUtils.copyProperties(dto, log);
		super.deleteEntity(log);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	@Override
	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param dto
	*/
 	public void save(FcAccLogDTO dto){
 		FcAccLog log = new FcAccLog();
		BeanUtils.copyProperties(dto, log);
 		super.saveEntity(log);
 	}
	/**
	* 更新
	* @param dto
	*/
	public void update(FcAccLogDTO dto){
		FcAccLog log = new FcAccLog();
		BeanUtils.copyProperties(dto, log);
		super.updateEntity(log);
	}
	/**
	* 保存or更新
	* @param dto
	*/
 	@Override
	public void saveOrUpdate(FcAccLogDTO dto){
 		FcAccLog log = new FcAccLog();
		BeanUtils.copyProperties(dto, log);
 		super.saveOrUpdate(log);
 	}
	@Override
	public BaseDao<com.bytter.system.inneraccount.entity.FcAccLog, String> getEntityDao() {
		return fcAccLogDao;
	}
	/**
	 * 根据业务表单据Id查询日志信息
	 * @param bizId 业务表单据Id
	 * @return
	 */
	public List<FcAccLogDTO> listByBizId(String bizId) {
		List<FcAccLog> list = fcAccLogDao.listByBizId(bizId);
		List<FcAccLogDTO> listDTO = BeanUtils.copyListProperties(list, FcAccLogDTO.class);
		return listDTO;
	}
	/**
	 * 查询日志信息
	 * @param bizId 业务表单据Id
	 * @param account 账号
	 * @param bizType 10开户，11改户，12初始化，13销户，14冻结，15解冻
	 * @return
	 */
	public List<FcAccLogVO> listByParams(String bizId, String account, String bizType) {
		List<FcAccLogVO> fcAccLogVOS = fcAccLogDao.listByParams(bizId, account, bizType);
		Map<String, Map<String, DictDTO>> dictMap = dictServiceI.listDictMap("fcAcc.bizType", "fcAcc.opType");
		Map<String, String> userNameMap = userService.getUserNameByLoginNames(fcAccLogVOS.stream().map(item -> item.getOpCode()).collect(Collectors.toList()));
		return fcAccLogVOS.stream().peek(item->{
			if (StringUtils.isNotEmpty(item.getBizType())) {
				item.setBizTypeName(dictMap.get("fcAcc.bizType").getOrDefault(item.getBizType(), DictDTO.EMPTY).getName());
			}
			if (StringUtils.isNotEmpty(item.getBizType())) {
				item.setOpTypeName(dictMap.get("fcAcc.opType").getOrDefault(item.getOpType(), DictDTO.EMPTY).getName());
			}
			item.setOpName(userNameMap.get(item.getOpCode()));
		}).collect(Collectors.toList());
	}
	public void saveLog(FcAccLogDTO dto, String loginName){
		if(dto != null){
			dto.setOpCode(loginName);
			dto.setOpTime(new Timestamp(System.currentTimeMillis()));
			this.save(dto);
		}
	}
}