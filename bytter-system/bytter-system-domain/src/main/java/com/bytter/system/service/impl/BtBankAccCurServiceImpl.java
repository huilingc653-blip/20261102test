package com.bytter.system.service.impl;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccCurService;
import com.bytter.system.api.dto.BtBankAccCurDTO;
import com.bytter.system.api.vo.BtBankAccCurVO;
import com.bytter.system.dao.BtBankAccCurDao;
import com.bytter.system.entity.BtBankAccCur;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
/**
* @Title: Service
* @Description: BT_BANK_ACC_CUR
* @author onlineGenerator
* @date 2017-06-02 18:03:22
* @version 1.0
*
*/
@Service("btBankAccCurServiceI")
@Transactional
@DubboService
public class BtBankAccCurServiceImpl extends BaseService<BtBankAccCur,String> implements IBtBankAccCurService{
	@Autowired
	private BtBankAccCurDao btBankAccCurDao;
	@Override
	public BaseDao<BtBankAccCur, String> getEntityDao() {
		return btBankAccCurDao;
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
	* 删除对象
	* @param cur
 	 * @throws InvocationTargetException
 	 * @throws IllegalAccessException
	*/
	@Override
	public void deleteEntity(BtBankAccCurVO cur) {
		com.bytter.system.entity.BtBankAccCur entity = new com.bytter.system.entity.BtBankAccCur();
		BeanUtils.copyProperties(cur, entity);
		super.deleteEntity(entity);
	}
	@Override
	public void save(BtBankAccCurVO cur) {
		com.bytter.system.entity.BtBankAccCur entity = new com.bytter.system.entity.BtBankAccCur();
		BeanUtils.copyProperties(cur, entity);
		super.saveEntity(entity);

	}
	@Override
	public void update(BtBankAccCurVO cur) {
		com.bytter.system.entity.BtBankAccCur entity = new com.bytter.system.entity.BtBankAccCur();
		BeanUtils.copyProperties(cur, entity);
		super.updateEntity(entity);

	}
	@Override
	public void saveOrUpdate(BtBankAccCurVO cur) {
		com.bytter.system.entity.BtBankAccCur entity = new com.bytter.system.entity.BtBankAccCur();
		BeanUtils.copyProperties(cur, entity);
		super.saveOrUpdate(entity);

	}
	@Override
	public void batchSaveEntity(List<BtBankAccCurVO> list) {
		if(!CollectionUtils.isEmpty(list)) {
			List<com.bytter.system.entity.BtBankAccCur> listvo = BeanUtils.copyListProperties(list, com.bytter.system.entity.BtBankAccCur.class);
			super.batchSave(listvo);
		}
	}
	@Override
	public List<String> getBankAccIdsByCurId(String curId) {
		List<String> bankAccIds = new ArrayList<String>();
		if(!EmptyUtils.isEmpty(curId)){
			bankAccIds = btBankAccCurDao.getBankAccIdsByCurId(curId);
		}
		return bankAccIds;
	}

	@Override
	public BtBankAccCurDTO getBankAccCurByBankAccId(String BankAccId, String curId) {
		return btBankAccCurDao.getBankAccCurByBankAccId(BankAccId, curId);
	}

	@Override
	public List<BtBankAccCurDTO> getBankAccCurByBankAccId(String BankAccId) {
		return btBankAccCurDao.getBankAccCurByBankAccId(BankAccId);
	}
}
