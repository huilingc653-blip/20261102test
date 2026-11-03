package com.bytter.biz.fundwarn.dao;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.fundwarn.entity.BtWarnDataDtl;
import com.bytter.biz.fundwarn.vo.BtWarnDataDtlInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
/**
 * @Title: Dao
 * @Description: 预警策略数据明细信息
 * @author zhengg
 * @date 2018-10-12
 *
 */
@Repository
public class BtWarnDataDtlDao extends BaseDao<BtWarnDataDtl,String> {
	/**
	 * 预警策略数据列表数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		Map<String, Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder();
		sql.append("select  ");
		sql.append("a.payer_acc as payeracc,b.CORP_ID as corpId,b.CORP_NAME as corpName,b.cur_id as curId,a.payer_name as payername, ");
		sql.append("a.payer_bank_name as payerbankname,a.trans_date as transdate,a.pay_money as paymoney, ");
		sql.append("a.payee_acc as payeeacc,a.payee_name as payeename,a.payee_bank_name as payeebankname, ");
		sql.append("a.month_num as monthnum,a.excess_money as excessmoney,a.purpose,a.id,a.data_id as dataId,a.no ");
		sql.append("From bt_warn_data_dtl a ");
		sql.append("left join bt_warn_data b on a.data_id=b.id ");
		sql.append(" where 1=1 ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtWarnDataDtlInfoVO.class, null, false, params);
	}
	
	/**
	 * 根据条件删除预警明细信息
	 * @param params
	 */
	public void deleteBtWarnDataDtlByParams(Map<String,Object> params,String hqlW){
		if(StringUtils.isEmpty(hqlW)){
			return;
		}
		StringBuilder hql=new StringBuilder("delete from BtWarnDataDtl where 1=1 ");
		hql.append(hqlW);
		this.batchExecute(hql.toString(), params);
	}
}
