package com.bytter.system.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccMessageService;
import com.bytter.system.api.dto.BtBankAccMessageDTO;
import com.bytter.system.dao.BtBankAccMessageDao;
import com.bytter.system.entity.BtBankAccMessage;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* @Title: Service
* @Description: btBankAccMessageService
* @author onlineGenerator
* @date 2017-06-12 14:53:19
* @version 1.0
*
*/
@Service("btBankAccMessageService")
@DubboService
@Transactional
public class BtBankAccMessageService extends BaseService<BtBankAccMessage,String> implements IBtBankAccMessageService {
	@Autowired
	private BtBankAccMessageDao btBankAccMessageDao;

	@Override
	public BaseDao<BtBankAccMessage, String> getEntityDao() {
		return btBankAccMessageDao;
	}

	@Override
	public void save(BtBankAccMessageDTO dto) {
		BtBankAccMessage entity = new BtBankAccMessage();
		BeanUtils.copyNotNullProperties(dto, entity);
        super.saveEntity(entity);
	}

	@Override
	public void update(BtBankAccMessageDTO dto) {
		BtBankAccMessage entity = new BtBankAccMessage();
		BeanUtils.copyNotNullProperties(dto, entity);
		super.updateEntity(entity);
	}

	@Override
	public void saveOrUpdate(BtBankAccMessageDTO dto) {
		BtBankAccMessage entity = new BtBankAccMessage();
		BeanUtils.copyNotNullProperties(dto, entity);
		super.saveOrUpdate(entity);
	}

	@Override
	public void deleteEntity(BtBankAccMessageDTO dto) {
		BtBankAccMessage entity = new BtBankAccMessage();
		BeanUtils.copyNotNullProperties(dto, entity);
		super.deleteEntity(entity);
	}

	@Override
	public void batchSaveEntity(List<BtBankAccMessageDTO> entityList) {
		for(BtBankAccMessageDTO dto:entityList){
			BtBankAccMessage entity = new BtBankAccMessage();
			BeanUtils.copyNotNullProperties(dto, entity);
			this.saveEntity(entity);
		}
	}

	@Override
	public boolean saveMessage(BtBankAccMessageDTO dto) {
		if(dto==null||StringUtils.isAllBlank(dto.getBillNo(),dto.getMessage())){
			return false;
		}
		this.save(dto);
		return true;
	}

	@Override
	public boolean saveMessage(String billNo, Integer messageType, String message, String busType, ICurrentUser user) {
		return saveMessage(billNo, messageType, message, busType, user.getLoginName(), user.getUsername());
	}

	@Override
	public boolean saveMessage(String billNo, Integer messageType, String message, String busType, String userCode, String userName) {
		BtBankAccMessageDTO dto = new BtBankAccMessageDTO();
		dto.setBillNo(billNo);
		dto.setEnrolDate(new Date());
		dto.setMessageType(messageType);
		dto.setMessage(message);
		dto.setMessage(message);
		dto.setBusType(busType);
		dto.setOp(userCode);
		dto.setOpName(userName);
		return saveMessage(dto);
	}

	@Override
	public BtBankAccMessageDTO buildMessage(String billNo, Integer messageType, String message, String busType, ICurrentUser user) {
		return buildMessage(billNo, messageType, message, busType, user.getLoginName(), user.getUsername());
	}

	@Override
	public BtBankAccMessageDTO buildMessage(String billNo, Integer messageType, String message, String busType, String userCode, String userName) {
		BtBankAccMessageDTO dto = new BtBankAccMessageDTO();
		dto.setBillNo(billNo);
		dto.setEnrolDate(new Date());
		dto.setMessageType(messageType);
		dto.setMessage(message);
		dto.setMessage(message);
		dto.setBusType(busType);
		dto.setOp(userCode);
		dto.setOpName(userName);
		if(StringUtils.isAllBlank(dto.getBillNo(),dto.getMessage())){
			return null;
		}
		return dto;
	}

	@Override
	public List<BtBankAccMessageDTO> listMessage(String billNo, String busType) {
		return btBankAccMessageDao.listMessage(billNo,busType);
	}
}