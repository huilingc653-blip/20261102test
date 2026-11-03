package com.bytter.bill.domain.billbank.pool.dao;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolBankExc;
import com.bytter.bill.domain.billbank.dao.BaseDao;
import org.springframework.stereotype.Repository;
/**
 * <br>
 * <b>功能：</b>池信息DAO<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-19 <br>
 */
@Repository("billPoolBankExcImpDao")
public class BillPoolBankExcImpDao extends BaseDao<AbPoolBankExc,String> {
	private static final long serialVersionUID = 1L;
}
