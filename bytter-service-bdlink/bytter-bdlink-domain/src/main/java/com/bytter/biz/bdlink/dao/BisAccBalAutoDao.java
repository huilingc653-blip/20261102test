package com.bytter.biz.bdlink.dao;

import com.bytter.biz.bdlink.api.dto.BisAccBalAutoDTO;
import com.bytter.biz.bdlink.entity.BisAccBalAuto;
import com.bytter.biz.bdlink.vo.BisAccBalAutoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Dao
 * @Description: BIS_ACC_BAL_AUTO
 * @date 2019-08-08 15:02:30
 */
@Repository
public class BisAccBalAutoDao extends BaseDao<BisAccBalAuto, String> {
	/**
	 * 根据账号id币别查询离线账户余额自动计算信息表
	 *
	 * @param bankAccId 账户ID
	 * @param curId   币别ID
	 * @return BisAccBalAutoVO
	 */
	public BisAccBalAutoVO getBisAccBalAutoVOByAccCurId(String bankAccId, String curId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select b.id as id, b.bank_acc_id as bankAccId, b.cur_id as curId, b.end_date as endDate ");
		sql.append(" from bis_acc_bal_auto b where b.bank_acc_id =:bankAccId and b.cur_id = :curId");
		HashMap<String, Object> params = new HashMap<>();
		params.put("bankAccId", bankAccId);
		params.put("curId", curId);
		return getBySQLAliasToBean(sql.toString(), params, BisAccBalAutoVO.class);
	}
	/**
	 * 根据账号id币别id查询离线账户余额自动计算信息表
	 *
	 * @param bankAccId 账户ID
	 * @param curId   币别ID
	 * @return BisAccBalAuto
	 */
	public BisAccBalAuto getBisAccBalAutoByAccCurId(String bankAccId,String curId) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		hql.append(" from BisAccBalAuto bal where bal.bankAccId =:bankAccId and bal.curId =:curId ");
		params.put("bankAccId", bankAccId);
		params.put("curId", curId);
		List<BisAccBalAuto> auto = this.listByHQL(hql.toString(),0,1,params);
		if(EmptyUtils.isEmptyList(auto)){
			return null;
		}
		return auto.get(0);
	}
}
