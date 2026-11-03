package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolLimit;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @version V1.0
 * @Title: 额度信息业务Dao
 * @Description: 额度信息业务数据操作类
 * @author: zhenghj
 * @date: 2020-2-20
 */
@Repository
public class AbPoolLimitDao extends BaseDao<AbPoolLimit, String> {

	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;

    /**
     * 获取查询的sql语句
     *
     * @return
     */
    private StringBuilder getQuerySql() {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" APL.ID AS id,APL.Corp_Id as corpId,APL.Bp_Id as bpId,APL.PLEDGE_MONEY AS pledgeMoney,")
                .append("APL.BAIL_MONEY AS bailMoney,APL.POOL_MONEY AS poolMoney,APL.VALID_MONEY AS validMoney,APL.LOCK_MONEY AS lockMoney,")
                .append("APL.VALID_SIGN AS VALIDsIGN,APL.LIMIT_DATE AS limitDate,APL.CREATE_TIME AS createTime ")
                .append(" FROM AB_POOL_LIMIT APL  ");
        return sql;
    }
    /**
     * 额度信息查询
     * @param corpId 单位ID
     * @param bpId 票据池ID
     * @return 额度信息
     */
    public AbPoolLimitVO getLimitInfo(String corpId, String bpId,String bankAcc) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" where APL.corp_Id=:corpId and APL.VALID_SIGN=:validSign ");
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
		params.put("corpId", corpId);
        if(StringUtils.isNotEmpty(bpId)){
        	sql.append("and APL.BP_ID=:bpId ");
			params.put("bpId", bpId);
		}
		// 暂无BankAcc不为空的查询
        if(StringUtils.isNotEmpty(bankAcc)){
			String bifCode = btBankAccService.getBifCodeByBankAcc(bankAcc);
			sql.append("and APL.BP_ID in ( ");
			sql.append("select ap.id from Ab_Pool ap ");
			sql.append("left join ab_poolif api on api.id=ap.pif_id ");
			// sql.append("left join bis_bif_init bi on bi.bif_code=api.bif_code ");
			// sql.append("left join bt_bank_acc ba on ba.bif_code=bi.id ");
			sql.append("where api.BIF_CODE = :bifCode ");
			sql.append(") ");
			params.put("bifCode", bifCode);
		}
		return getBySQLAliasToBean(sql.toString(), params, AbPoolLimitVO.class);
    }
    
    
	public String getQueryListSql(){
		StringBuilder sql = new StringBuilder("select ");
		//sql.append("l.id,l.bp_id as bpId,sc.id as corpId,sc.code as corpCode,sc.name as corpName,l.pledge_money as pledgeMoney,");
		sql.append("l.id,l.bp_id as bpId,pc.corp_id as corpId,pc.corp_code as corpCode,pc.corp_name as corpName,l.pledge_money as pledgeMoney,");
		sql.append("l.bail_money as bailMoney,l.pool_money as poolMoney,l.lock_money as lockMoney,l.valid_money as validMoney, ");
		sql.append("l.valid_sign as validSign,l.limit_date as limitDate,p.bp_code as poolNo,p.bp_name as poolName,l.create_time as createTime,");
		sql.append("l.LOWRISK_MONEY as lowRiskMoney,l.INLOWRISK_MONEY as inLowRiskMoney,l.USED_LOWRISK_MONEY as usedLowRiskMoney,");
		sql.append("l.USED_INLOWRISK_MONEY as usedInLowRiskMoney,l.VALID_LOWRISK_MONEY as validLowRiskMoney,l.VALID_INLOWRISK_MONEY as validInLowRiskMoney ");
		sql.append("From Ab_Pool_Limit l  ");
		sql.append("left join ab_pool p on l.bp_id=p.id "); 
		sql.append("left join ab_pool_corp pc on pc.corp_id=l.corp_id and pc.valid_sign=:validSign ");
		//sql.append("left join sys_corp sc on sc.id=pc.corp_id  ");
		sql.append("where l.valid_sign=:validSign ");
		return sql.toString();
	}
	
	/**
	 * "查询票据池票据池额度信息列表数据
	 * @param dataTablesInput
	 * @param corpAuthList
	 * @return
	 */
	public DataTablesOutput<AbPoolLimitVO> datatableQuery(DataTablesInput dataTablesInput,List<String> corpAuthList) {
		Map<String,Object> params=new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder(getQueryListSql());
		params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "pc.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolLimitVO.class, null, false, params);
	}
	
	/**
	 * 根据ID获取票据池额度信息
	 * @param id
	 * @return
	 */
	public List<AbPoolLimitVO> getAbPoolLimitVOById(String id) {
		if(StringUtils.isEmpty(id)){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder(getQueryListSql());
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        sql.append(" and l.id=:id");
        params.put("id", id);
		return this.listBySQLAliasToBean(sql.toString(), params, AbPoolLimitVO.class);
	}
	/**
	 * 获取入池票据资产情况信息
	 * @param params
	 * @return
	 */
	public Map<String,Object> getHoldMoneyInfo(Map<String,Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("sum(v.BILL_MONEY) as sumholdmoney, ");
		sql.append("sum(case when v.end_date<=:datem1 then v.BILL_MONEY else 0 end) as sumholdmoney1, ");
		sql.append("sum(case when v.end_date>:datem1 and v.end_date<=:datem2 then v.BILL_MONEY else 0 end) as sumholdmoney2, ");
		sql.append("sum(case when v.end_date>:datem2 and v.end_date<=:datem3 then v.BILL_MONEY else 0 end) as sumholdmoney3, ");
		sql.append("sum(case when v.end_date>:datem3 and v.end_date<=:datem6 then v.BILL_MONEY else 0 end) as sumholdmoney6, ");
		sql.append("sum(case when v.end_date>:datem6 then v.BILL_MONEY else 0 end) as sumholdmoney7 ");
		sql.append("from AB_POOL_DTL pd ");
		sql.append("left join AB_VOUCHERINFO v on pd.ORIG_NO=v.ORIG_NO ");
		sql.append("left join AB_VOUCHERINFO_VICE vv on vv.vid=v.id and pd.VV_ID=vv.id ");
		sql.append("where pd.STATUS=95 and pd.VALID_SIGN='1' and pd.OPT_STATUS='19' ");
		sql.append("and vv.CORP_ID =:corpId ");
		sql.append("and pd.BP_ID=:bpId ");
		List list=this.listMapBySQL(sql.toString(),params);
		if(ListUtils.isNotNullOrEmpty(list)){
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}
	/**
	 * 获取池内开票资产情况信息
	 * @param params
	 * @return
	 */
	public Map<String,Object> getEmitMoneyInfo(Map<String,Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("sum(v.BILL_MONEY) as sumemitmoney, ");
		sql.append("sum(case when v.end_date<=:datem1 then v.BILL_MONEY else 0 end) as sumemitmoney1, ");
		sql.append("sum(case when v.end_date>:datem1 and v.end_date<=:datem2 then v.BILL_MONEY else 0 end) as sumemitmoney2, ");
		sql.append("sum(case when v.end_date>:datem2 and v.end_date<=:datem3 then v.BILL_MONEY else 0 end) as sumemitmoney3, ");
		sql.append("sum(case when v.end_date>:datem3 and v.end_date<=:datem6 then v.BILL_MONEY else 0 end) as sumemitmoney6, ");
		sql.append("sum(case when v.end_date>:datem6 then v.BILL_MONEY else 0 end) as sumemitmoney7 ");
		sql.append("from AB_ACCRECORDS acc ");
		sql.append("left join AB_VOUCHERINFO v on acc.vid=v.id  ");
		sql.append("left join AB_VOUCHERINFO_VICE vv on vv.vid=v.id and acc.vvid=vv.id  ");
		sql.append("left join AB_ACCRECORDS pb on pb.vid=acc.vid and pb.vvid=acc.vvid and pb.status>=95 and pb.brief_id='6120' and pb.opt_status='19' ");
		// sql.append("left join bis_bif_init bi on bi.id=vv.bif_code ");
		sql.append("left join ab_poolif api on api.bif_id=vv.bif_code ");
		sql.append("left join ab_pool ap on api.id=ap.pif_id ");
		sql.append("where pb.id is null ");
		sql.append("and acc.STATUS=95  ");
		sql.append("and acc.OPT_STATUS='19'  ");
		sql.append("and acc.BRIEF_ID='003'  ");
		sql.append("and vv.emit_type='10' ");
		sql.append("and vv.CORP_ID =:corpId ");
		sql.append("and ap.id=:bpId ");
		List list=this.listMapBySQL(sql.toString(),params);
		if(ListUtils.isNotNullOrEmpty(list)){
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}
}
