package com.bytter.system.dao;
import com.bytter.system.entity.BtAccNature;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * @author huangx
 * @Description 账户性质维护
 * @date 2017.9.14
 */
@Repository
public class BtAccNatureDao extends BaseDao<BtAccNature, String> {
	/**
	 * 根据编码获取账户性质
	 *
	 * @param natureCode
	 * @return
	 */
	public BtAccNature findByCode(String natureCode) {
		return getUniqueBy("natureCode", natureCode);
	}
	/**
	 * 获取有效的账户属性
	 *
	 * @return
	 */
	public List<BtAccNature> getValidAccNature() {
		return listBy("validSign", CommonConstant.VALID_SIGN_YES);
	}
}
