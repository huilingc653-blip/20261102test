package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbBillSignStrategyData;
import com.bytter.bill.domain.bill.vo.AbBillSignStrategyDataVO;
import com.bytter.core.base.BaseDao;
import com.bytter.system.api.constant.SysBlackWhiteConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: AbBillSignStrategyDataDao
 * @Description: 电票自动签收策略选择数据Dao
 * @author wangzy
 * @date 2020/7/14 11:50
 *
 */
@Repository
public class AbBillSignStrategyDataDao extends BaseDao<AbBillSignStrategyData,String> {
	@SuppressWarnings("unchecked")
	public List<AbBillSignStrategyData> getAbBillSignStrategyData(String ssId){
		String hql = " from AbBillSignStrategyData t where t.ssId =:ssId";
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("ssId", ssId);
		return this.listByHQL(hql, params);
	}
	
	public int delAbBillSignStrategyData(String ssId){
		String hql = "delete from AbBillSignStrategyData t where t.ssId =:ssId";
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("ssId", ssId);
		return  this.batchExecute(hql, params);
	}
	/**
	 * 获取全部有效的策略明细数据
	 * zhengg 20200721
	 * @return 策略明细数据信息
	 */
	public List<AbBillSignStrategyDataVO> getAllValidBillSignStrategyData(){
		StringBuilder sql=new StringBuilder("select id,dataVal,ssId,dataType,orgTag  From (");
		//取出票人、承兑人、发起人单位名称信息
		sql.append("select sd.id,sd.data_val as dataVal,sd.ss_id as ssId,sd.data_type as dataType ,sd.org_tag as orgTag from ab_bill_sign_strategy_data sd ");
		// sql.append("inner join sys_corp sc on sd.data_val=sc.id ");
		sql.append("inner join ab_bill_sign_strategy ss on ss.id=sd.ss_id and ss.valid_sign=:validSign ");
		sql.append("where sd.org_tag=:orgTagCorp ");
		sql.append("union all ");
		//取承兑人银行类型信息
		sql.append("select sd.id,sd.data_val,sd.ss_id,sd.data_type,sd.org_tag from ab_bill_sign_strategy_data sd ");
		// sql.append("inner join bt_bank_type bt on sd.data_val=bt.id ");
		sql.append("inner join ab_bill_sign_strategy ss on ss.id=sd.ss_id and ss.valid_sign=:validSign ");
		sql.append("where sd.data_type=:dataType and sd.org_tag is null ");
		sql.append("union all ");
		//取出票人、承兑人、发起人客商名称信息
		sql.append("select sd.id,sd.data_val,sd.ss_id,sd.data_type,sd.org_tag from ab_bill_sign_strategy_data sd ");
		// sql.append("inner join sys_external_corp ec on sd.data_val=ec.id and ec.bw_type not in (:bwType) ");
		sql.append("inner join ab_bill_sign_strategy ss on ss.id=sd.ss_id and ss.valid_sign=:validSign ");
		sql.append("where sd.org_tag=:orgTagExternal ");
		sql.append(")tmp order by ssId,id ");
		Map<String,Object> params=new HashMap<>();
		params.put("bwType", new String[]{SysBlackWhiteConstant.EXTERNALCORP_BWTYPE_01});
		params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("dataType", AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_02);
		params.put("orgTagCorp", AccebillConstant.BILL_SIGN_STRATEGY_ORG_TAG_01);
		params.put("orgTagExternal", AccebillConstant.BILL_SIGN_STRATEGY_ORG_TAG_02);
		return this.listBySQLAliasToBean(sql.toString(),params,AbBillSignStrategyDataVO.class);
	}
}
