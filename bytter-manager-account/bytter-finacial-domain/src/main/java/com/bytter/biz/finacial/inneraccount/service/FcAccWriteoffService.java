package com.bytter.biz.finacial.inneraccount.service;
import com.bytter.biz.finacial.inneraccount.entity.FcAccWriteoff;
import com.bytter.biz.finacial.inneraccount.dao.FcAccWriteoffDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
/**
* @Title: Service
* @Description: FC_ACC_WRITEOFF
* @author onlineGenerator
* @date 2017-06-12 10:08:33
* @version 1.0
*
*/
@Service("fcAccWriteoffService")
@Transactional
public class FcAccWriteoffService extends BaseService<FcAccWriteoff,String> {
	@Autowired
	private FcAccWriteoffDao fcAccWriteoffDao;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(FcAccWriteoff entity){
		super.deleteEntity(entity);
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
	* @param entity
	*/
 	public void save(FcAccWriteoff entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccWriteoff entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(FcAccWriteoff entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<FcAccWriteoff, String> getEntityDao() {
		return fcAccWriteoffDao;
	}
	/**
	 * 添加账户注销记录
	 * @param blnctrAcc
	 */
	public String saveObject(String blnctrAcc,String transferAcc,Date dayOver,String loginName){
		FcAccWriteoff waiteOff = this.getEntityDao().getUniqueBy("writeoffAcc",blnctrAcc);
		if(waiteOff != null){
			waiteOff.setTallyDate(dayOver);
			waiteOff.setUpdateBy(loginName);
			waiteOff.setUpdateTime(DateUtils.getDate());
			waiteOff.setTransferAcc(transferAcc);
			return waiteOff.getId();
		}else {
			FcAccWriteoff writeOff = new FcAccWriteoff();
			Date date = DateUtils.getDate();
			writeOff.setTallyDate(dayOver);
			writeOff.setCreateBy(loginName);
			writeOff.setCreateTime(date);
			writeOff.setWriteoffAcc(blnctrAcc);
			writeOff.setTransferAcc(transferAcc);
			writeOff.setTransactor(loginName);
			writeOff.setWriteoffTime(date);
			this.getEntityDao().save(writeOff);
			this.getEntityDao().flush();
			return writeOff.getId();
		}
	}
	/**
	 * 修改全局id
	 * @param blnctrAcc
	 */
	public FcAccWriteoff getFcAccWriteoff(String blnctrAcc){
		FcAccWriteoff waiteOff = this.getEntityDao().getUniqueBy("writeoffAcc",blnctrAcc);
		return waiteOff;
	}
	/**
	 * 修改全局id
	 * @param blnctrAcc
	 */
	public void updateBusGlobalId(String blnctrAcc,String busGlobalId){
		FcAccWriteoff waiteOff = this.getEntityDao().getUniqueBy("writeoffAcc",blnctrAcc);
		waiteOff.setBusGlobalId(busGlobalId);
		this.update(waiteOff);
	}
	/**
	 * 修改账户注销记录
	 * @param blnctrAcc
	 */
	public void updateObject(String blnctrAcc,String loginName){
		FcAccWriteoff waiteOff = this.getEntityDao().getUniqueBy("writeoffAcc",blnctrAcc);
		waiteOff.setCheckerCode(loginName);
		waiteOff.setCheckTime(new Date());
		this.update(waiteOff);
	}
	/**
	 * 通过内部账号删除账户销户明细
	 * @param blnctrAcc
	 * @return
	 */
	public int delWriteOffByBlnctrAcc(String blnctrAcc) {
		return fcAccWriteoffDao.delWriteOffByBlnctrAcc(blnctrAcc);
	}
}
