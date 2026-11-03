package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbBillTransferDtlDao;
import com.bytter.bill.domain.bill.entity.AbBillTransferDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
* @Title: Service
* @Description: 票据下拨流水关联信息
* @author onlineGenerator
* @date 2017-11-1 16:40:01
* @version 1.0
*
*/
@Service("abBillTransferDtlService")
@Transactional
public class AbBillTransferDtlService extends BaseService<AbBillTransferDtl,String> {
	@Autowired
	private AbBillTransferDtlDao abBillTransferDtlDao;
	/**
	 * 根据下拨单id批量删除选票信息
	 */
    public int deleteByTransferId(String transferId){
		return abBillTransferDtlDao.deleteByTransferId(transferId);
	}
	/**
	 * 根据下拨单id获取选票关联信息
	 */
	public List<AbBillTransferDtl> getBillAidListByTransferId(String transferId){
		return abBillTransferDtlDao.getBillAidArrayByTransferId(transferId);
	}
	/**
	 * 根据票据流水id获取选票关联信息
	 */
	public AbBillTransferDtl getAbBillTransferDtlByAid(String billAid){
		List<AbBillTransferDtl> list = abBillTransferDtlDao.getAbBillTransferDtlByAid(billAid);
		if(list!=null && !list.isEmpty ()){
			return list.get (0);
		}
		return new AbBillTransferDtl();
	}
	/**
	 * 根据下拨单id获取选票流水id数组
	 */
	public String[] getBillAidArrayByTransferId(String transferId){
		List<AbBillTransferDtl> list = getBillAidListByTransferId(transferId);
		if(list!=null && !list.isEmpty ()){
			String[] aidArray = new String[list.size ()];
			for(int i=0;i<aidArray.length;i++){
				aidArray[i] = list.get (i).getBillAid ();
			}
			return aidArray;
		}
		return new String[0];
	}
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(AbBillTransferDtl entity){
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
 	public void save(AbBillTransferDtl entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(AbBillTransferDtl entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(AbBillTransferDtl entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<AbBillTransferDtl, String> getEntityDao() {
		return abBillTransferDtlDao;
	}
}
