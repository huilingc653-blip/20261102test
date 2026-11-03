package com.bytter.biz.finacial.accbook.dao;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bytter.biz.finacial.accbook.entity.FcChargeAccrue;
import com.bytter.biz.finacial.accbook.vo.FcChargeAccruePageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.*;
/**
* @Title: Dao
* @Description: FC_CHARGE_ACCRUE
* @author onlineGenerator
* @date 2017-07-04 15:14:11
* @version 1.0
*
*/
@Repository
public class FcChargeAccrueDao extends BaseDao<FcChargeAccrue,String>{
	/**
	 * 功能介绍 : 查询合计手续费清单数据。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh
	 * TODO 没有被使用 暂注释
	 */
	/*public DataTablesOutput<FcChargeAccruePageVO> querySumChargeAccrue(DataTablesInput input){
		try {
			StringBuilder sql = new StringBuilder();
			Class<FcChargeAccruePageVO> voClazz = FcChargeAccruePageVO.class;
			
		    sql.append("select charge.account as account, acc.acc_name as accname, charge.tally_sign as tallysign,");
    		sql.append("count(charge.account) as cnt,sum(charge.charge_money) as chargemoney ");
		    sql.append("from fc_charge_accrue charge ");
			sql.append("left join fc_accbook book on book.order_no = charge.order_no");
			sql.append(",fc_acc acc where charge.account=acc.blnctr_acc ");
			sql.append(getWhereAllCondition(input, voClazz.newInstance(), "", false));
			sql.append(" group by charge.account,acc.acc_name,charge.tally_sign ");
			sql.append(" order by charge.account");
		    
            Map<String, Object> values = input.getSearchValues();
            String countSQL = prepareCountSQL(sql.toString());
            BigDecimal count = (BigDecimal) createSQLQuery(countSQL, values).uniqueResult();
            List list;
            if (count.compareTo(BigDecimal.ZERO) > 0) {
                list = listBySQL(sql.toString(), input.getStart(), input.getLength(), values);
                list = BeanUtils.copyMapListProperties(list, voClazz);
                if (null != voClazz) {
                    //inject(list);
                }
            } else {
                list = new ArrayList(0);
            }
            return new DataTablesOutput<FcChargeAccruePageVO>(input.getDraw(), list, count.intValue(), count.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(e);
        }
	}*/
	
	/**
	 * 功能介绍 : 查询手续费清单数据。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public DataTablesOutput<FcChargeAccruePageVO> queryChargeAccrue(DataTablesInput input, List<String> voucherIds,List<String> accounts){
		try {
			StringBuilder sql = new StringBuilder();
			Class<FcChargeAccruePageVO> voClazz = FcChargeAccruePageVO.class;
			
		    /*sql.append("select charge.id as id,charge.account as account,BT_NVL_CHAR(blned.voucher_no,book.voucher_no) as voucherNo, acc.acc_name as accname, ");
    		sql.append("charge.tally_sign as tallysign,vou.id as voucherType,vou.voucher_name as voucherName,charge_money as chargemoney ");
		    sql.append("from fc_charge_accrue charge "); // FC_CHARGE_ACCRUE 冗余字段 acc_name
			sql.append("left join fc_accbook book on book.id = charge.target_order_no ");
			sql.append("left join fc_accbook_blned blned on blned.id= charge.target_order_no ");
			sql.append("left join fc_voucher_type vou on vou.id = charge.voucher_type ");
			sql.append(",fc_acc acc where charge.account=acc.blnctr_acc ");*/
			sql.append("select charge.id as id,charge.account as account,BT_NVL_CHAR(blned.voucher_no,book.voucher_no) as voucherNo, charge.acc_name as accname, ");
			sql.append("charge.tally_sign as tallysign,charge.voucher_type as voucherType,charge_money as chargemoney ");
			sql.append("from fc_charge_accrue charge "); // FC_CHARGE_ACCRUE 冗余字段 acc_name
			sql.append("left join fc_accbook book on book.id = charge.target_order_no ");
			sql.append("left join fc_accbook_blned blned on blned.id= charge.target_order_no ");
			sql.append(" where 1=1 ");
			if (CollectionUtils.isNotEmpty(voucherIds)){
				sql.append("and ").append(getSQLIn(voucherIds, 900, "charge.voucher_type"));
			}
			sql.append(" and (charge.status = '").append(FinacialConstant.CHARGE_STATUS_1).append("' or charge.status is null) ");
			sql.append(" and " + getSQLIn(accounts, 900, "charge.account"));
//			sql.append(" where 1 = 1 ");
			sql.append(getWhereAllCondition(input, voClazz.newInstance(), "", false));
			sql.append(" order by charge.account,voucherNo");
			input.addOrder("account",false);
			input.addOrder("voucherNo",false);

//			return getDynamicBySQL(input,sql.toString(),FcChargeAccruePageVO.class,"",false);
            Map<String, Object> values = input.getSearchValues();
            String countSQL = prepareCountSQL(sql.toString());
            BigDecimal count = (BigDecimal) createSQLQuery(countSQL, values).uniqueResult();
            List list;
            if (count.compareTo(BigDecimal.ZERO) > 0) {
                list = listBySQL(sql.toString(), input.getStart(), input.getLength(), values);
                list = BeanUtils.copyMapListProperties(list, voClazz);
                if (null != voClazz) {
                    //inject(list);
                }
            } else {
                list = new ArrayList(0);
            }
            return new DataTablesOutput<FcChargeAccruePageVO>(input.getDraw(), list, count.intValue(), count.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(e);
        }
	}
	
	/**
	 * 功能介绍 : 根据账号查询未计提记录。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcChargeAccrue> queryChargeAccrueById(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select t from FcChargeAccrue t where t.id = :id and t.tallySign = :tallySign ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("tallySign", FinacialConstant.TALLY_SIN_N);//未记账
		return listByHQL(hql.toString(), params);
	}
	
	/**
	 * 功能介绍 : 根据账号查询未计提记录。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcChargeAccrue> queryChargeAccrueByAccounts(String[] accounts) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select t from FcChargeAccrue t where t.account in (:accounts) and t.tallySign = :tallySign ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accounts", accounts);
		params.put("tallySign", FinacialConstant.TALLY_SIN_N);//未记账
		
		return listByHQL(hql.toString(), params);
	}
	
	/**
	 * 功能介绍 : 根据凭证流水号查询已计提记录。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcChargeAccrue> queryChargeAccrueByOrderNo(Set<String> orderNos) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select t from FcChargeAccrue t where t.orderNo in (:orderNos) and t.tallySign = :tallySign ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNos", orderNos);
		params.put("tallySign", FinacialConstant.TALLY_SIN_Y);//已记账
		return listByHQL(hql.toString(), params);
	}
	
	/**
	 * 功能介绍 : 未入账手续费。<br>
	 * <b>日期：</b>2017年7月17日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcChargeAccrue> queryNoTallyChargeAccrue(FcMonthCloseDTO closeDTO,List<String> accounts) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select t from FcChargeAccrue t where t.tallySign = :tallySign ");
		hql.append(" and t.account in (:accounts) ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tallySign", FinacialConstant.TALLY_SIN_N);//未入账
		params.put("accounts", accounts);//网点
		return listByHQL(hql.toString(), params);
	}
	
	/**
     * <br>
     * <b>功能：</b>获取未结转手续费账户金额<br>
     * <b>作者：</b>yangl<br>
     * <b>日期：</b> 2017-09-28 <br>
     * @param accounts
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String, BigDecimal> getNotOverChargeByAccount(String accounts) {
    	StringBuilder hql = new StringBuilder();
    	hql.append("select account,sum(charge_money) as money from fc_charge_accrue where tally_sign = :tallySign ");
    	hql.append("and account in (:accounts) group by account");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tallySign", FinacialConstant.TALLY_SIN_N);//未入账
        params.put("accounts", accounts);
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        List<Object[]> accNotOverMoneyList=this.createSQLQuery(hql.toString(), params).list();
        for (Object[] accountNotOverMoney : accNotOverMoneyList) {
            result.put((String) accountNotOverMoney[0], (BigDecimal) accountNotOverMoney[1]);
        }
        return result;
    }
    
    /**
     * 功能介绍 : 标识计提记录为删除。<br>
     * <b>日期：</b>2017年11月8日<br>
     * @version 1.0
     * @author liyh 
     */
    public void invalidChargeAccrue(String chargeId) {
    	StringBuilder hql = new StringBuilder();
    	hql.append("update fc_charge_accrue fa set status = :status where id = :chargeId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("chargeId", chargeId);
        params.put("status", FinacialConstant.CHARGE_STATUS_0);//标识删除
        createSQLQuery(hql.toString(), params).executeUpdate();
    }
}
