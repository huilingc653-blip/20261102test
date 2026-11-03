package com.bytter.biz.receipt.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.receipt.dao.EReceiptTypeDao;
import com.bytter.biz.receipt.entity.EReceiptType;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
/**
* @Title: Service
* @Description: 电子回单类型维护
* @author onlineGenerator
* @date 2017-06-05 10:52:28
* @version 1.0
*
*/
@Service("eReceiptTypeService")
@Transactional
public class EReceiptTypeService extends BaseService<EReceiptType,String> {
	@Autowired
	private EReceiptTypeDao eReceiptTypeDao;
	@Override
	public BaseDao<EReceiptType, String> getEntityDao() {
		return eReceiptTypeDao;
	}
	/**
	 * 查询电子回单类型列表
	 * @param validSign 可空
	 * @param billType 可空
	 * @return
	 */
	public List<EReceiptType> listReceiptType(String validSign,String billType){
		return eReceiptTypeDao.listReceiptType(validSign,billType);
	}
	/**
	 * 查询电子回单类型
	 * @param billType
	 * @return
	 */
	public EReceiptType getReceiptType(String billType){
		if(StringUtils.isEmpty(billType)){
			return null;
		}
		return eReceiptTypeDao.getReceiptType(billType);
	}
}
