package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.BtPayType;
import com.bytter.biz.netbank.vo.BtPayTypeVO;
import com.bytter.biz.payment.api.dto.BtPayTypeDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @Description 系统结算方式
 * @date 2017.9.14
 */
@Repository
public class BtPayTypeDao extends BaseDao<BtPayType, String> {
	/**
	 * 获取有效的系统结算方式
	 *
	 * @return
	 */
	public List<BtPayTypeVO> getValidPayType() {
		String sql = " select * from BT_PAY_TYPE b where b.VALID_SIGN = :validSign";
		Map<String,Object> params = new HashMap<>();
		params.put("validSign",CommonConstant.VALID_SIGN_YES);
		return listBySQLAliasToBean(sql,params,BtPayTypeVO.class);
	}

	/**
	 * 根据名称获取系统结算方式
	 *
	 * @param voucherName 结算方式名称
	 * @return 结算方式
	 */
	public BtPayTypeDTO getPayTypeByVoucherName(String voucherName) {
		String sql = "select ID, SYS_VOUCHER_CODE as sysVoucherCode,SYS_VOUCHER_NAME as sysVoucherName, rmk,valid_sign as validSign  from BT_PAY_TYPE b where b.VALID_SIGN = :validSign and b.SYS_VOUCHER_NAME = :voucherName";
		Map<String, Object> params = new HashMap<>();
		params.put("validSign", CommonConstant.VALID_SIGN_YES);
		params.put("voucherName", voucherName);
		return getBySQLAliasToBean(sql, params, BtPayTypeDTO.class);
	}
}
