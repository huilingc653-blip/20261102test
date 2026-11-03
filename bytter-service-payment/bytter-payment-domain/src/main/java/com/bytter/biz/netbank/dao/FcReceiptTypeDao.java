package com.bytter.biz.netbank.dao;

import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.vo.BtPayTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linqp
 * @Description 单据类型编码方式
 */
@Repository
public class FcReceiptTypeDao extends BaseDao<FcReceiptType, String> {
	/**
	 * 获取有效的系统结算方式
	 *
	 * @return
	 */
	public List<FcReceiptType> getValidFcReceiptType() {
		String sql = " select * from FC_RECEIPT_TYPE b where b.VALID_SIGN = :validSign";
		Map<String,Object> params = new HashMap();
		params.put("validSign", CommonConstant.VALID_SIGN_YES);
		return listBySQLAliasToBean(sql,params,FcReceiptType.class);
	}

    public FcReceiptType getById(String id) {
		String sql = " select * from FC_RECEIPT_TYPE b where b.id = :id";
		Map<String,Object> params = new HashMap();
		params.put("id", id);
		return getBySQLAliasToBean(sql,params,FcReceiptType.class);
    }
	public FcReceiptType getByCode(String code) {
		String sql = " select * from FC_RECEIPT_TYPE b where b.code = :code";
		Map<String,Object> params = new HashMap();
		params.put("code", code);
		return getBySQLAliasToBean(sql,params,FcReceiptType.class);
	}
}
