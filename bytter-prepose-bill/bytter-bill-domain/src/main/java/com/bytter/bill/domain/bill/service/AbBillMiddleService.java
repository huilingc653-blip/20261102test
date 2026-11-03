package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbBillMiddleDao;
import com.bytter.bill.domain.bill.entity.AbBillMiddle;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* @Title: Service
* @Description: 票据中间表
* @author onlineGenerator
* @date 2017-11-13 11:00:24
* @version 1.0
*
*/
@Service("abBillMiddleService")
@Transactional
public class AbBillMiddleService extends BaseService<AbBillMiddle,String> {
	@Autowired
	private AbBillMiddleDao abBillMiddleDao;
	public BaseDao<AbBillMiddle, String> getEntityDao() {
		return abBillMiddleDao;
	}
	
	/**
	 * 新增中间表数据
	 * @param buId 业务id
	 * @param middleType 中间表类型
	 */
	public void saveBillMiddle(String buId,String middleType,ICurrentUser userVo){
		AbBillMiddle billMiddle=new AbBillMiddle();
		billMiddle.setBuId(buId);
		billMiddle.setType(middleType);
		billMiddle.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		if(userVo!=null){
			billMiddle.setCreateBy(userVo.getLoginName());
		}
		billMiddle.setCreateDate(DateUtils.getDate());
		abBillMiddleDao.save(billMiddle);
	}
	/**
	 * 通过业务id和删除票据中间表数据
	 * @param buId 业务ID
	 * @param type 中间表类型
	 */
	public int deleteBillMiddleByBuIdAndType(String buId, String type) {
		if (StringUtils.isNotEmpty(buId) && StringUtils.isNotEmpty(type)) {
			return abBillMiddleDao.deleteBillMiddleByBuIdAndType(buId, type);
		}
		return 0;
	}
}
