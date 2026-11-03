package com.bytter.biz.fundwarn.dao;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.fundwarn.entity.BtWarnData;
import com.bytter.biz.fundwarn.entity.BtWarnDataDtl;
import com.bytter.biz.fundwarn.spi.constant.FundWarnConstant;
import com.bytter.biz.fundwarn.vo.BtWarnDataInfoVO;
import com.bytter.biz.fundwarn.vo.BtWarnStrategyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.InnerAccountConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.CorpConstant;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 预警策略解析数据库操作
 * @author zhengg
 *
 */
@SuppressWarnings("rawtypes")
@Repository
public class FundWarnStrategyAnalysisDao extends BaseDao {
    /**
     * 根据查询条件加载 符合条件的预警策略信息
     * @param params 查询条件
	 *
     * @return 返回列表数据
     */
    public List<BtWarnStrategyVO> getTimelyWarnStrategyByParams(Map<String,Object> params) {
        StringBuilder sql = new StringBuilder("select s.id,s.net_id as netId,s.code,s.name,s.cur_id as curId,s.type_code as typeCode,s.dimension_code as dimensionCode," +
                "s.status,s.begin_num as beginNum,s.end_num as endNum,s.begin_operator as beginOperator,s.end_operator as endOperator,s.num_operator " +
                "as numOperator,s.task_id as taskId,s.task_cron as taskCron,s.forward_time as forwardTime,s.run_date as runDate," +
                "s.create_name as createName,s.create_by as createBy,s.create_date as createDate,s.update_name as updateName," +
                "s.update_by as updateBy,s.update_date as updateDate from bt_warn_strategy s ");
        if(params.containsKey("dimensionDataType")&&params.containsKey("dimensionDataTypeValue")){
//          --对应预警主体，针对【大额账户支出】特殊处理
			/*String dimensionDataType = Convert.toStr(params.get("dimensionDataType"));
			if (ObjectUtil.equal("bankAcc",dimensionDataType)){
				sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=:dimensionDataType and (c.data_value=:dimensionDataTypeValue or c.data_value =:netId ) ");
			}else {
				sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=:dimensionDataType and (c.data_value=:dimensionDataTypeValue or c.data_value is null ) ");
			}*/

			sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=:dimensionDataType and (c.data_value=:dimensionDataTypeValue or c.data_value is null ) ");
        }
        if((FundWarnConstant.TYPECODE_EQCORPPAY.equals(params.get("typeCode"))
        		||FundWarnConstant.TYPECODE_DAYSUMPAY.equals(params.get("typeCode")))
        		&&params.containsKey("bankAcc")){
//          --单位主体时候需要判断账号的业务状态
          sql.append(" inner join bt_warn_condition ca on s.id=ca.warn_id and ca.data_type='bankAccAttribute' and (ca.data_value=:attributeId or ca.data_value is null ) ");
          sql.append(" inner join bt_warn_condition ct on s.id=ct.warn_id and ct.data_type='bankAccType' and (ct.data_value=:accType or ct.data_value is null ) ");
          sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='bankAccNature' and (cn.data_value=:isOnline or cn.data_value is null ) ");
        }
        sql.append(" where 1=1 ");
        if(params.containsKey("status")){
        	sql.append(" and s.status=:status ");
        }
        if(params.containsKey("typeCode")){
        	sql.append(" and s.type_code=:typeCode ");
        }
        if(params.containsKey("curId")){
            sql.append(" and s.cur_id=:curId ");
        }
        List<BtWarnStrategyVO> list = listBySQLAliasToBean(sql.toString(), params, BtWarnStrategyVO.class);
        return list;
    }
    
    /**
     * 根据params获取付款表nis_billhead相同公司付款明细数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnDataDtl> getEqcorppayDtlDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select nb.bill_no as no,nb.payer_acc as payerAcc, ");
    	sql.append(" nb.payer_acc_name as payerName,nb.payer_bank as payerBankName, ");
    	sql.append(" nb.pay_money as payMoney,nb.wish_payday as transDate, ");
    	sql.append(" nb.payee_acc as payeeAcc,nb.payee_acc_name as payeeName,nb.payee_bank as payeeBankName, ");
    	sql.append(" nb.purpose from nis_billhead nb ");
    	sql.append(" inner join bt_bank_acc ba on ba.bank_acc=nb.payer_acc ");
    	sql.append(" inner join sys_corp sc on nb.corp_id=sc.id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=sc.id ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" inner join bt_warn_condition ca on s.id=ca.warn_id and ca.data_type='bankAccAttribute' and (ca.data_value=ba.attribute_id or ca.data_value is null ) "); 
    	sql.append(" inner join bt_warn_condition ct on s.id=ct.warn_id and ct.data_type='bankAccType' and (ct.data_value=ba.acc_type or ct.data_value is null )  ");
    	sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='bankAccNature' and (cn.data_value=ba.is_online or cn.data_value is null )  ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	
//    	  where status not in (-2) 
//    	  and corp_id='402880425ba2e512015ba2f616f30001' 
//    	  and payee_acc_name='第一个单位-企业活期'
//    	  and cur_id='4028802e5c346968015c346c18850008'
//    	  and pay_money=1
//    	  and wish_payday=to_date('2018/06/06','yyyy/mm/dd');
    	
    	params.put("noStatust", new Integer[]{-2});
    	sql.append(" where nb.status not in (:noStatust) ").append(sqlW);
    	sql.append(" order by nb.create_date ");
    	List<BtWarnDataDtl> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnDataDtl.class);
    	return list;
    }
    
    /**
     * 根据params获取付款表nis_billhead累计支付预警数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getDaySumPayDataByParams(Map<String,Object> params,String wMoney,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select info.triggerNum from ( ");
    	sql.append(" select sum(nb.pay_money) as triggerNum from nis_billhead nb ");
    	sql.append(" inner join bt_bank_acc ba on ba.bank_acc=nb.payer_acc ");
    	sql.append(" inner join sys_corp sc on nb.corp_id=sc.id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=sc.id ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" inner join bt_warn_condition ca on s.id=ca.warn_id and ca.data_type='bankAccAttribute' and (ca.data_value=ba.attribute_id or ca.data_value is null ) "); 
    	sql.append(" inner join bt_warn_condition ct on s.id=ct.warn_id and ct.data_type='bankAccType' and (ct.data_value=ba.acc_type or ct.data_value is null )  ");
    	sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='bankAccNature' and (cn.data_value=ba.is_online or cn.data_value is null )  ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	
    	params.put("noStatust", new Integer[]{-2,0,-1});
    	sql.append(" where (nb.status not in (:noStatust) or nb.bill_no=:billNo ) ").append(sqlW);
    	sql.append(" ) info where ").append(wMoney);
    	List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
    
    /**
     * 根据params获取账户余额超标-单位余额统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getBaloverCorpDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select info.bal as triggerNum,info.corp_id as corpId from ( ");
    	sql.append(" select sum(ab.bal) bal,ba.corp_id  ");
    	sql.append(" from bis_acc_bal ab ");
    	sql.append(" inner join bt_bank_acc ba on ba.bank_acc=ab.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and ab.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=sc.id ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" inner join bt_warn_condition ca on s.id=ca.warn_id and ca.data_type='bankAccAttribute' and (ca.data_value=ba.attribute_id or ca.data_value is null ) "); 
    	sql.append(" inner join bt_warn_condition ct on s.id=ct.warn_id and ct.data_type='bankAccType' and (ct.data_value=ba.acc_type or ct.data_value is null )  ");
    	sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='bankAccNature' and (cn.data_value=ba.is_online or cn.data_value is null )  ");
    	sql.append(" where ab.status in(:balStatus) ");
    	sql.append(" and ab.bal_date=:balDate ");
    	sql.append(" group by ba.corp_id ");
    	sql.append(" ) info ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	params.put("balStatus", new Integer[]{95});
    	sql.append(" where ").append(sqlW);
    	List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }

	/**
	 * 根据params获取账户余额超标-银行账户余额统计数据数据
	 * @param params 入参
	 * @param sqlW 过滤条件
	 * @return
	 */
    public List<BtWarnData> getBaloverBankAccDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select ab.bal as triggerNum,ba.corp_id as corpId,ab.bank_acc as bankAcc ");
    	sql.append(" from bis_acc_bal ab ");
    	sql.append(" inner join bt_bank_acc ba on ba.bank_acc=ab.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and ab.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=ab.bank_acc ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" where ab.status in (:balStatus) ");
    	sql.append(" and BT_TO_CHAR(ab.bal_date, 'yyyy-MM-dd')=:balDateStr ");

    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	params.put("balStatus", new Integer[]{95});
    	sql.append(" and ").append(sqlW);
    	List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
    /**
     * 根据params获取账户余额超标-银行账户余额统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getBaloverBankAccDataByParamsBack(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select ab.bal as triggerNum,ba.corp_id as corpId,ab.bank_acc as bankAcc ");
    	sql.append(" from bis_acc_bal ab ");
    	sql.append(" inner join bt_bank_acc ba on ba.bank_acc=ab.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and ab.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=ab.bank_acc ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" where ab.status in (:balStatus) ");
    	sql.append(" and ab.bal_date=:balDate ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	params.put("balStatus", new Integer[]{95});
    	sql.append(" and ").append(sqlW);
    	List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
    /**
     * 根据params获取库存现金超标-单位余额统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getCashoverCorpDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select info.bal as triggerNum,info.corp_id as corpId from ( ");
    	sql.append(" select sum(ab.check_balance) as bal,fa.corp_id ");
    	sql.append(" from fc_online_ledger ab ");
    	sql.append(" inner join fc_acc fa on ab.account=fa.blnctr_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=fa.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and ab.cur_code=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=fa.corp_id ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus ) ) ");
    	sql.append(" inner join bt_warn_condition cs on s.id=cs.warn_id and cs.data_type='accSubject' and (cs.data_value=fa.subject_id or cs.data_value is null ) "); 
    	sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='fcAccNature' and (cn.data_value=fa.nature_code_id or cn.data_value is null )  ");
    	sql.append(" where fa.acc_State in (:accState) ");
    	sql.append(" group by fa.corp_id ");
    	sql.append(" ) info ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accState", new String[]{"N","P","S"});
    	sql.append(" where ").append(sqlW);
		List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
    
    /**
     * 根据params获取库存现金超标-内部账户余额统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getCashoverFcAccDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select ab.check_balance as triggerNum,fa.corp_id as corpId,ab.account as fcAcc ");
    	sql.append(" from fc_online_ledger ab ");
    	sql.append(" inner join fc_acc fa on ab.account=fa.blnctr_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=fa.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and ab.cur_code=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=ab.account ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus ) ) ");
    	sql.append(" where fa.acc_State in (:accState) ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accState", new String[]{"N","P","S"});
    	sql.append(" and ").append(sqlW);
		List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
    /**
     * 根据params获取净流入透支-单位统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getInflowOverCorpDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select aInfo.oneNum,aInfo.twoNum,aInfo.triggerNum,aInfo.corpId from ( ");
    	sql.append(" select sum(info.jAmt) oneNum, sum(info.dAmt) twoNum,sum(info.jAmt)- sum(info.dAmt) triggerNum, ");
    	sql.append(" info.corp_id as corpId from ( ");
    	sql.append(" select case dtl.cd_sign when '0' then dtl.amt else 0 end jAmt, ");
    	sql.append(" case dtl.cd_sign when '1' then dtl.amt else 0 end dAmt ,sc.id corp_id ");
    	sql.append(" from bis_acc_dtl dtl ");
    	sql.append(" inner join bt_bank_acc ba on ba.bank_acc=dtl.bank_acc ");
    	sql.append(" inner join sys_corp sc on ba.corp_id=sc.id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and dtl.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=sc.id ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" inner join bt_warn_condition ca on s.id=ca.warn_id and ca.data_type='bankAccAttribute' and (ca.data_value=ba.attribute_id or ca.data_value is null ) "); 
    	sql.append(" inner join bt_warn_condition ct on s.id=ct.warn_id and ct.data_type='bankAccType' and (ct.data_value=ba.acc_type or ct.data_value is null )  ");
    	sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='bankAccNature' and (cn.data_value=ba.is_online or cn.data_value is null )  ");
    	sql.append(" where 1=1 ");
    	sql.append(" and dtl.trans_time>=:beginDate ");
    	sql.append(" and dtl.trans_time<:endDate ");
    	sql.append(" ) info ");
    	sql.append(" group by info.corp_id ");
    	sql.append(" ) aInfo ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	sql.append(" where ").append(sqlW);
    	List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
    
    /**
     * 根据params获取净流入透支-银行账户统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getInflowOverBankAccDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select aInfo.oneNum,aInfo.twoNum,aInfo.triggerNum,aInfo.bankAcc,aInfo.corpId from ( ");
    	sql.append(" select sum(info.jAmt) oneNum, sum(info.dAmt) twoNum,sum(info.jAmt)- sum(info.dAmt) triggerNum, ");
    	sql.append(" info.bank_acc as bankAcc,min(info.corp_id) as corpId from ( ");
    	sql.append(" select case dtl.cd_sign when '0' then dtl.amt else 0 end jAmt, ");
    	sql.append(" case dtl.cd_sign when '1' then dtl.amt else 0 end dAmt ,sc.id corp_id ,dtl.bank_acc ");
    	sql.append(" from bis_acc_dtl dtl ");
    	sql.append(" inner join bt_bank_acc ba on ba.bank_acc=dtl.bank_acc ");
    	sql.append(" inner join sys_corp sc on ba.corp_id=sc.id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and dtl.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=dtl.bank_acc ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" where 1=1 ");
    	sql.append(" and dtl.trans_time>=:beginDate ");
    	sql.append(" and dtl.trans_time<:endDate ");
    	sql.append(" ) info ");
    	sql.append(" group by info.bank_acc ");
    	sql.append(" ) aInfo ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	sql.append(" where ").append(sqlW);
    	List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
    /**
     * 根据params获取 系统外付款-单位模式明细数据 统计数据数据
     * @param params 入参
     * @return
     */
    public List<BtWarnDataDtl> getOutPayCorpDataByParams(Map<String,Object> params){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select sc.id as corpId,ba.bank_acc as payerAcc,dtl.trans_time as transDate,dtl.amt as payMoney, ");
    	sql.append(" dtl.opp_acc_no as payeeAcc,dtl.opp_acc_name as payeeName,dtl.opp_acc_bank as payeeBankName,bt_concat2(dtl.uses,dtl.remark) as purpose ");
    	sql.append(" from bis_acc_dtl dtl ");
    	sql.append(" inner join bt_bank_acc ba on dtl.bank_acc=ba.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and dtl.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=sc.id ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" inner join bt_warn_condition ca on s.id=ca.warn_id and ca.data_type='bankAccAttribute' and (ca.data_value=ba.attribute_id or ca.data_value is null ) "); 
    	sql.append(" inner join bt_warn_condition ct on s.id=ct.warn_id and ct.data_type='bankAccType' and (ct.data_value=ba.acc_type or ct.data_value is null ) ");
    	sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='bankAccNature' and (cn.data_value=ba.is_online or cn.data_value is null ) ");
    	sql.append(" left join bis_exc exc on exc.bank_serial_no is not null and dtl.bank_serial_no=exc.bank_serial_no and exc.voucher_stat='0' and  dtl.bank_acc=exc.payer_acc ");
    	sql.append(" where dtl.cd_sign='1' and exc.id is null ");
    	sql.append(" and dtl.trans_time>=:beginDate ");
    	sql.append(" and dtl.trans_time<:endDate ");
    	sql.append(" order by sc.id,ba.bank_acc,dtl.trans_time asc ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	List<BtWarnDataDtl> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnDataDtl.class);
    	return list;
    }
    
    /**
     * 根据params获取 系统外付款-银行账户模式明细数据 统计数据数据
     * @param params 入参
     * @return
     */
    public List<BtWarnDataDtl> getOutPayBankAccDataByParams(Map<String,Object> params){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select sc.id as corpId,ba.bank_acc as payerAcc,dtl.trans_time as transDate,dtl.amt as payMoney, ");
    	sql.append(" dtl.opp_acc_no as payeeAcc,dtl.opp_acc_name as payeeName,dtl.opp_acc_bank as payeeBankName,bt_concat2(dtl.uses,dtl.remark) as purpose ");
    	sql.append(" from bis_acc_dtl dtl ");
    	sql.append(" inner join bt_bank_acc ba on dtl.bank_acc=ba.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and dtl.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=ba.bank_acc ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" left join bis_exc exc on exc.bank_serial_no is not null and dtl.bank_serial_no=exc.bank_serial_no and exc.voucher_stat='0' and  dtl.bank_acc=exc.payer_acc ");
    	sql.append(" where dtl.cd_sign='1' and exc.id is null ");
    	sql.append(" and dtl.trans_time>=:beginDate ");
    	sql.append(" and dtl.trans_time<:endDate ");
    	sql.append(" order by sc.id,ba.bank_acc,dtl.trans_time asc ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	List<BtWarnDataDtl> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnDataDtl.class);
    	return list;
    }
    /**
     * 根据params获取 支出户流入-单位模式主数据 统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getExpendInflowCorpDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select info.corpId,info.triggerNum,info.sizeNum from ( ");
    	sql.append(" select sc.id as corpId,sum(dtl.amt) as triggerNum,count(ba.bank_acc) as sizeNum ");
    	sql.append(" from bis_acc_dtl dtl ");
    	sql.append(" inner join bt_bank_acc ba on dtl.bank_acc=ba.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and dtl.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=sc.id ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" inner join bt_warn_condition ca on s.id=ca.warn_id and ca.data_type='bankAccAttribute' and (ca.data_value=ba.attribute_id or ca.data_value is null ) "); 
    	sql.append(" inner join bt_warn_condition ct on s.id=ct.warn_id and ct.data_type='bankAccType' and (ct.data_value=ba.acc_type or ct.data_value is null ) ");
    	sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='bankAccNature' and (cn.data_value=ba.is_online or cn.data_value is null ) ");
    	sql.append(" where dtl.cd_sign='0' ");
    	sql.append(" and dtl.trans_time>=:beginDate ");
    	sql.append(" and dtl.trans_time<:endDate ");
    	sql.append(" group by sc.id ) info where ").append(sqlW);
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
    
    /**
     * 根据params获取 支出户流入-银行账户模式主数据 统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getExpendInflowBankAccDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select info.bankAcc,info.corpId,info.triggerNum,info.sizeNum from ( ");
    	sql.append(" select ba.bank_acc as bankAcc,min(sc.id) as corpId,sum(dtl.amt) as triggerNum,count(ba.bank_acc) as sizeNum ");
    	sql.append(" from bis_acc_dtl dtl ");
    	sql.append(" inner join bt_bank_acc ba on dtl.bank_acc=ba.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and dtl.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=ba.bank_acc ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" where dtl.cd_sign='0' ");
    	sql.append(" and dtl.trans_time>=:beginDate ");
    	sql.append(" and dtl.trans_time<:endDate ");
    	sql.append(" group by ba.bank_acc ) info where ").append(sqlW);
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
    
    /**
     * 根据params获取 支出户流入-单位模式明细数据 统计数据数据
     * @param params 入参
     * @return
     */
    public List<BtWarnDataDtl> getExpendInflowCorpDataDtlByParams(Map<String,Object> params){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select sc.id as corpId,ba.bank_acc as payerAcc,dtl.trans_time as transDate,dtl.amt as payMoney,ba.acc_name as payerName,ba.bank_name payerBankName, ");
    	sql.append(" dtl.opp_acc_no as payeeAcc,dtl.opp_acc_name as payeeName,dtl.opp_acc_bank as payeeBankName,bt_concat2(dtl.uses,dtl.remark) as purpose ");
    	sql.append(" from bis_acc_dtl dtl ");
    	sql.append(" inner join bt_bank_acc ba on dtl.bank_acc=ba.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and dtl.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=sc.id ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" inner join bt_warn_condition ca on s.id=ca.warn_id and ca.data_type='bankAccAttribute' and (ca.data_value=ba.attribute_id or ca.data_value is null ) "); 
    	sql.append(" inner join bt_warn_condition ct on s.id=ct.warn_id and ct.data_type='bankAccType' and (ct.data_value=ba.acc_type or ct.data_value is null ) ");
    	sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='bankAccNature' and (cn.data_value=ba.is_online or cn.data_value is null ) ");
    	sql.append(" where dtl.cd_sign='0' ");
    	sql.append(" and dtl.trans_time>=:beginDate ");
    	sql.append(" and dtl.trans_time<:endDate ");
    	sql.append(" and sc.id=:corpId ");
    	sql.append(" order by sc.id,ba.bank_acc,dtl.trans_time asc ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	List<BtWarnDataDtl> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnDataDtl.class);
    	return list;
    }
    
    /**
     * 根据params获取 支出户流入-银行账户模式明细数据 统计数据数据
     * @param params 入参
     * @return
     */
    public List<BtWarnDataDtl> getExpendInflowBankAccDataDtlByParams(Map<String,Object> params){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select sc.id as corpId,ba.bank_acc as payerAcc,dtl.trans_time as transDate,dtl.amt as payMoney,ba.acc_name as payerName,ba.bank_name payerBankName, ");
    	sql.append(" dtl.opp_acc_no as payeeAcc,dtl.opp_acc_name as payeeName,dtl.opp_acc_bank as payeeBankName,bt_concat2(dtl.uses,dtl.remark) as purpose ");
    	sql.append(" from bis_acc_dtl dtl ");
    	sql.append(" inner join bt_bank_acc ba on dtl.bank_acc=ba.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and dtl.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=ba.bank_acc ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" where dtl.cd_sign='0' ");
    	sql.append(" and dtl.trans_time>=:beginDate ");
    	sql.append(" and dtl.trans_time<:endDate ");
    	sql.append(" and ba.bank_acc=:bankAcc ");
    	sql.append(" order by sc.id,ba.bank_acc,dtl.trans_time asc ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	List<BtWarnDataDtl> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnDataDtl.class);
    	return list;
    }
    
//
//    /**
//     * 根据params获取 支出户流入-单位模式明细数据 统计数据数据
//     * @param params 入参
//     * @param sqlW 过滤条件
//     * @return
//     */
//    public List<BtWarnDataDtl> getExpendInflowCorpDataByParams(Map<String,Object> params,String sqlW){
//    	StringBuilder sql=new StringBuilder();
//    	sql.append(" select sc.id as corpId,ba.bank_acc as payerAcc,dtl.trans_time as transDate,dtl.amt as payMoney,ba.acc_name as payerName,ba.bank_name payerBankName, ");
//    	sql.append(" dtl.opp_acc_no as payeeAcc,dtl.opp_acc_name as payeeName,dtl.opp_acc_bank as payeeBankName,bt_concat2(dtl.uses,dtl.remark) as purpose ");
//    	sql.append(" from bis_acc_dtl dtl ");
//    	sql.append(" inner join bt_bank_acc ba on dtl.bank_acc=ba.bank_acc ");
//    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
//    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and dtl.cur_id=s.cur_id ");
//    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=sc.id ");
//    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
//    	sql.append(" inner join bt_warn_condition ca on s.id=ca.warn_id and ca.data_type='bankAccAttribute' and (ca.data_value=ba.attribute_id or ca.data_value is null ) "); 
//    	sql.append(" inner join bt_warn_condition ct on s.id=ct.warn_id and ct.data_type='bankAccType' and (ct.data_value=ba.acc_type or ct.data_value is null ) ");
//    	sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='bankAccNature' and (cn.data_value=ba.is_online or cn.data_value is null ) ");
//    	sql.append(" where dtl.cd_sign='0' ");
//    	sql.append(" and dtl.trans_time>=:beginDate ");
//    	sql.append(" and dtl.trans_time<:endDate ");
//    	sql.append(" and ").append(sqlW);
//    	sql.append(" order by sc.id,ba.bank_acc,dtl.trans_time asc ");
//    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
//    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
//    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
//    	List<BtWarnDataDtl> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnDataDtl.class);
//    	return list;
//    }
//    
//
//    /**
//     * 根据params获取 支出户流入-银行账户模式明细数据 统计数据数据
//     * @param params 入参
//     * @param sqlW 过滤条件
//     * @return
//     */
//    public List<BtWarnDataDtl> getExpendInflowBankAccDataByParams(Map<String,Object> params,String sqlW){
//    	StringBuilder sql=new StringBuilder();
//    	sql.append(" select sc.id as corpId,ba.bank_acc as payerAcc,dtl.trans_time as transDate,dtl.amt as payMoney,ba.acc_name as payerName,ba.bank_name payerBankName, ");
//    	sql.append(" dtl.opp_acc_no as payeeAcc,dtl.opp_acc_name as payeeName,dtl.opp_acc_bank as payeeBankName,bt_concat2(dtl.uses,dtl.remark) as purpose ");
//    	sql.append(" from bis_acc_dtl dtl ");
//    	sql.append(" inner join bt_bank_acc ba on dtl.bank_acc=ba.bank_acc ");
//    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
//    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and dtl.cur_id=s.cur_id ");
//    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=ba.bank_acc ");
//    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
//    	sql.append(" where dtl.cd_sign='0' ");
//    	sql.append(" and dtl.trans_time>=:beginDate ");
//    	sql.append(" and dtl.trans_time<:endDate ");
//    	sql.append(" and ").append(sqlW);
//    	sql.append(" order by sc.id,ba.bank_acc,dtl.trans_time asc ");
//    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
//    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
//    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
//    	List<BtWarnDataDtl> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnDataDtl.class);
//    	return list;
//    }
    /**
     * 根据params获取存贷比超标-单位余额统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getDepositLoanOverCorpDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select info.corpId,info.triggerNum,info.oneNum,info.twoNum from ( ");
    	sql.append(" select sum(aInfo.oneNum) oneNum,sum(aInfo.twoNum) twoNum,(case when sum(aInfo.twoNum)=0 then 0 else (sum(aInfo.oneNum)/sum(aInfo.twoNum)*100) end) triggerNum,aInfo.corp_id as corpId from ( ");
    	sql.append(" select (case BT_RTRIM(fs.acc_type) when :faccLoan then 0 else ab.check_balance end) oneNum,");
    	sql.append(" (case BT_RTRIM(fs.acc_type) when :faccLoan then ab.check_balance else 0 end) twoNum,fa.corp_id ");
    	sql.append(" from fc_online_ledger ab ");
    	sql.append(" inner join fc_acc fa on ab.account=fa.blnctr_acc ");
    	// 科目表 acc_type 为 CHAR(6)，Oracle 会自动补空格，需先 BT_RTRIM 再与 2 位开户种类编码比较，否则条件恒不成立、查询结果为空
    	sql.append(" inner join fc_subject fs on fs.id=fa.subject_id ");
    	sql.append(" inner join sys_corp sc on sc.id=fa.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and ab.cur_code=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=fa.corp_id ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus ) ) ");
    	sql.append(" where fa.acc_State in (:accState) ");
    	sql.append(" and BT_RTRIM(fs.acc_type) in (:subjectAccType) ");
    	sql.append(" ) aInfo ");
    	sql.append(" group by aInfo.corp_id ");
    	sql.append(" ) info ");
    	params.put("faccLoan", InnerAccountConstant.FCACC_LOAN);
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("subjectAccType", new String[]{InnerAccountConstant.FCACC_BUSINESS_ACCOUNT,
    			InnerAccountConstant.FCACC_BANK_DEPOSIT,
    			InnerAccountConstant.FCACC_BUSINESS_BEGULAR,
    			InnerAccountConstant.FCACC_LOAN});
    	params.put("accState", new String[]{"N","P","S"});
    	sql.append(" where ").append(sqlW);
		List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
    /**
     * 根据params获取指定日期的账户余额-单位余额统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnDataDtl> getBalDownCorpDataDtlByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select sum(ab.bal) as excessMoney,ba.corp_id as corpId  ");
    	sql.append(" from bis_acc_bal ab ");
    	sql.append(" inner join bt_bank_acc ba on ba.bank_acc=ab.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and ab.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=sc.id ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign) ) ");
    	sql.append(" inner join bt_warn_condition ca on s.id=ca.warn_id and ca.data_type='bankAccAttribute' and (ca.data_value=ba.attribute_id or ca.data_value is null ) "); 
    	sql.append(" inner join bt_warn_condition ct on s.id=ct.warn_id and ct.data_type='bankAccType' and (ct.data_value=ba.acc_type or ct.data_value is null )  ");
    	sql.append(" inner join bt_warn_condition cn on s.id=cn.warn_id and cn.data_type='bankAccNature' and (cn.data_value=ba.is_online or cn.data_value is null )  ");
    	sql.append(" where ab.status in(:balStatus) ");
    	sql.append(" and ab.bal_date>=:beginDate ");
    	sql.append(" and ab.bal_date<:endDate ");
    	if(StringUtils.isNotEmpty(sqlW)){
        	sql.append(" and ").append(sqlW);
    	}
    	sql.append(" group by ba.corp_id ");
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	params.put("balStatus", new Integer[]{95});
    	List<BtWarnDataDtl> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnDataDtl.class);
    	return list;
    }
    
    
    /**
     * 根据params获取指定日期的账户余额-银行账户余额统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnDataDtl> getBalDownBankAccDataDtlByParams(Map<String,Object> params,String sqlW,boolean isSum){
    	StringBuilder sql=new StringBuilder();
    	if(isSum){
        	sql.append(" select sum(ab.bal) as excessMoney,min(ba.corp_id) as corpId,ab.bank_acc as payerAcc  ");
    	}else{
    		sql.append(" select ab.bal as excessMoney,ba.corp_id as corpId,ab.bank_acc as payerAcc,ba.acc_name as payerName,ba.bank_name as payerBankName ");
    	}
    	sql.append(" from bis_acc_bal ab ");
    	sql.append(" inner join bt_bank_acc ba on ba.bank_acc=ab.bank_acc ");
    	sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
    	sql.append(" inner join bt_warn_strategy s on s.id=:warnId and ab.cur_id=s.cur_id ");
    	sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=ab.bank_acc ");
    	sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" where ab.status in (:balStatus) ");
    	sql.append(" and ab.bal_date>=:beginDate ");
    	sql.append(" and ab.bal_date<:endDate ");
    	if(StringUtils.isNotEmpty(sqlW)){
        	sql.append(" and ").append(sqlW);
    	}
    	if(isSum){
        	sql.append(" group by ab.bank_acc ");
    	}
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
    	params.put("balStatus", new Integer[]{95});
    	List<BtWarnDataDtl> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnDataDtl.class);
    	return list;
    }
    /**
     * 根据params获取 久悬户预警-银行账户维度策略处理 统计数据数据
     * @param params 入参
     * @param sqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getExpendOldBankAccByBankAccDataByParams(Map<String,Object> params,String sqlW){
    	StringBuilder sql=new StringBuilder();
    	sql.append(" select info.corpId,info.bankAcc,info.triggerNum,ab.bal as oneNum from ( ");
    	sql.append(" select max(sc.id) as corpId,ba.bank_acc as bankAcc,case when max(dtl.cur_id) is null then 0 else count(ba.bank_acc) end as triggerNum,max(s.cur_id) as cur_id ");
    	sql.append(" from  bt_bank_acc ba ");
        sql.append(" inner join sys_corp sc on sc.id=ba.corp_id ");
        sql.append(" inner join bt_warn_strategy s on s.id=:warnId ");
        sql.append(" inner join bt_warn_condition c on s.id=c.warn_id and c.data_type=s.dimension_code and (c.data_value=ba.bank_acc ");
        sql.append(" or (c.data_value is null and (s.net_id=sc.id or s.net_id=sc.net_id) and sc.status=:corpStatus and ba.status<>:accStatus and ba.valid_sign=:accValidsign ) ) ");
    	sql.append(" left join bis_acc_dtl dtl on dtl.bank_acc=ba.bank_acc and dtl.trans_time>=:beginDate and dtl.trans_time<:endDate and dtl.cur_id = s.cur_id ");
    	sql.append(" group by ba.bank_acc ) info ");
    	sql.append(" left join bis_acc_bal ab on ab.bank_acc=info.bankAcc and (ab.cur_id=info.cur_id or info.cur_id is null) ");
    	sql.append(" and ab.bal_date>=:balBeginDate ");
    	sql.append(" and ab.bal_date<:balEndDate ");
    	sql.append(" where ").append(sqlW);
    	params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
    	params.put("accStatus", BtBankAccConstant.STATUE_DEL);
    	params.put("accValidsign", BtBankAccConstant.VALIDSIGN_1);
		//处理oracle时间参数 隐式转换的问题
		if (params.containsKey("beginDate")){
			Date begin = DateUtils.parse(params.get("beginDate").toString(),"yyyy-MM-dd HH:mm:ss");
			params.put("beginDate", begin);
		}
		if (params.containsKey("endDate")){
			Date endDate = DateUtils.parse(params.get("endDate").toString(),"yyyy-MM-dd HH:mm:ss");
			params.put("endDate", endDate);
		}
		if (params.containsKey("balBeginDate")){
			Date balBeginDate = DateUtils.parse(params.get("balBeginDate").toString(),"yyyy-MM-dd HH:mm:ss");
			params.put("balBeginDate", balBeginDate);
		}
		if (params.containsKey("balEndDate")){
			Date balEndDate = DateUtils.parse(params.get("balEndDate").toString(),"yyyy-MM-dd HH:mm:ss");
			params.put("balEndDate", balEndDate);
		}

    	List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
    	return list;
    }
	/**
	 * 根据params获取 预算执行进度预警数据
	 * @param params 入参
	 * @param sqlW 过滤条件
	 * @return
	 */
	public List<BtWarnData> getBudgetProgressListDataByParams(Map<String,Object> params,String sqlW){
		String wFields="nb.cur_id";
		String sqlwhere = SqlUtil.getWhereByParamsAndField(wFields, params);
		StringBuilder sql=new StringBuilder();
		sql.append(" select info.triggerNum,info.corp_id as corpId from ( ");
		sql.append(" select sum(nb.pay_money) as triggerNum,nb.corp_id from nis_billhead nb ");
		params.put("notStatus", new Integer[]{-2,0,-1});
		sql.append(" where (nb.status not in (:notStatus) ) ").append(sqlwhere);
		sql.append(" group by nb.corp_id ");
		sql.append(" ) info where ").append(sqlW);
		List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
		return list;
	}


	/**
	 * 根据params获取 预算执行进度预警数据
	 * @param params 入参
	 * @return
	 */
	public List<BtWarnData> getBudgetProgressListDataByTableName(Map<String,Object> params, String tableName){
		String wFields="b.cur_id";
		String sqlwhere = SqlUtil.getWhereByParamsAndField(wFields, params);
		StringBuilder sql=new StringBuilder();
		sql.append(" select b.CORP_ID as corpId,sum(B_MONEY + BT_NVL_NUM(BSUPER_MONEY, 0)) as triggerNum from "+tableName+" m ");
		sql.append(" left join fbs_budget b on m.BUDGET_ID = b.ID ");
		sql.append(" where m.ITEM_CODE = '400' and b.STATUS = '95' ");
		sql.append(" and b.BEGIN_DATE >= :beginDate and b.END_DATE <= :endDate ").append(sqlwhere);
		sql.append(" group by b.corp_id ");
		List<BtWarnData> list = this.listBySQLAliasToBean(sql.toString(), params, BtWarnData.class);
		return list;
	}

	/**
	 * 根据params获取 预算执行警数据
	 * @param params 入参
	 * @return
	 */
	public List<BtWarnData> getProcessDataByTableName(Map<String,Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append(" select p.CORP_ID as corpId,sum(p.PROCESS_CUR_MONEY) as triggerNum from FBS_BUDGET_PROCESS p ");
		sql.append(" where p.ITEM_CODE like '400%' and p.STATUS = '95'  ");
		// sql.append(" and p.CUR_ID = :cur_id ");
		sql.append(" and p.PROCESS_DATE >= :beginDate and p.PROCESS_DATE <= :endDate ");
		sql.append(" group by p.corp_id ");
		List<BtWarnData> list = this.listBySQLAliasToBean(sql.toString(), params, BtWarnData.class);
		return list;
	}





	/**
	 * 根据params获取 预算执行进度预警数据
	 * @param params 入参
	 * @param sqlW 过滤条件
	 * @return
	 */
	public BtWarnData getBudgetProgressDataByParams(Map<String,Object> params,String sqlW){
		String wFields="nb.corp_id,nb.cur_id";
		String sqlwhere = SqlUtil.getWhereByParamsAndField(wFields, params);
		StringBuilder sql=new StringBuilder();
		sql.append(" select info.triggerNum from ( ");
		sql.append(" select sum(nb.pay_money) as triggerNum from nis_billhead nb ");
		params.put("noStatust", new Integer[]{-2,0,-1});
		sql.append(" where (nb.status not in (:noStatust)  ) ").append(sqlwhere);
		sql.append(" ) info where ").append(sqlW);
		List<BtWarnData> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnData.class);
		if(!ListUtils.isNullOrEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}
