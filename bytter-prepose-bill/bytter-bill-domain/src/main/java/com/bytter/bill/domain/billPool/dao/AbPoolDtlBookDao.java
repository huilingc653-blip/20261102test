package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolDtlBookVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 票据池明细账簿信息DO
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
@Repository
public class AbPoolDtlBookDao extends BaseDao<AbPoolDtlBookVO, String> {
	public String getBaseSql(boolean isBusQuery,Map<String,Object> params){
		StringBuilder sql = new StringBuilder("select ");
		//默认取账簿明细表数据
		//sql.append("d.id,sc.id as corpId,sc.code as corpCode,sc.name as corpName,v.bill_type as billType,v.bill_flag as billFlag,d.orig_no as origNo,d.create_time as createTime,v.bill_money as billMoney,v.start_date as startDate,v.end_date as endDate, ");
		sql.append("d.id,pc.corp_id as corpId,pc.corp_code as corpCode,pc.corp_name as corpName,v.bill_type as billType,v.bill_flag as billFlag,d.orig_no as origNo,d.create_time as createTime,v.bill_money as billMoney,v.start_date as startDate,v.end_date as endDate, ");
		sql.append("v.payer,v.payee,v.acceptor,p.bp_code as poolNo,p.bp_name as poolName,d.bankendrsmtmk,d.sttlmmk,d.rsn,d.update_time as updateTime,d.rec_name as recName,d.inout_type as inoutType,d.brief_id as briefId ");
		sql.append("From ");
		if(isBusQuery){
			//支持去业务数据表信息
			sql.append("(select d.id,vv.corp_id,d.bp_id,d.brief_id, bt_nvl_char(d.orig_no,v.orig_no) as orig_no,a.bankendrsmtmk,a.sttlmmk,a.rsn,d.create_time as update_time,null as create_time,a.rec_nm as rec_Name,d.inout_type ");  
			sql.append("from ab_pool_dtl d  ");
			sql.append("left join ab_accrecords a on d.id=a.pd_id ");
			sql.append("left join ab_voucherinfo_vice vv on a.vvid=vv.id ");
			sql.append("left join ab_voucherinfo v on a.vid = v.id  ");
			sql.append("where a.brief_id in ('001','010','011','020','8019','8118', '8218','8018','018')  and d.opt_status='19' and a.opt_status='19'  ) ");
		}else{
			//默认取账簿明细表数据
			//支持去业务数据表信息
			sql.append("(select d.id,vv.corp_id,d.bp_id,d.brief_id, bt_nvl_char(d.orig_no,v.orig_no) as orig_no,a.bankendrsmtmk,a.sttlmmk,a.rsn,d.create_time as update_time,null as create_time,a.rec_nm as rec_Name,d.inout_type ");  
			sql.append("from ab_pool_dtl d  ");
			sql.append("left join ab_accrecords a on d.id=a.pd_id ");
			sql.append("left join ab_voucherinfo_vice vv on a.vvid=vv.id ");
			sql.append("left join ab_voucherinfo v on a.vid = v.id  ");
			sql.append("left join ab_pool pl on d.bp_id = pl.id  ");
			sql.append("left join ab_poolif pli on pl.pif_id = pli.id  ");
			sql.append("where 1=1 ");
			sql.append(" and d.opt_status='19' and a.opt_status='19'  ");
			//针对兴业银行背书和贴现业务去取业务操作数据
			sql.append(" and pli.bif_code in (:bankBifCode) and a.brief_id in ('010','011') ");
			sql.append(" union all ");
			sql.append(" select d.id,d.corp_id,d.bp_id,d.brief_id,d.orig_no,d.bankendrsmtmk,d.sttlmmk,d.rsn,d.update_time,d.create_time,d.rec_Name,d.inout_type  from ab_pool_dtl_book d ");
			sql.append(" )");
			params.put("bankBifCode", new String[]{AbPoolConstant.AB_POOL_INTO_APPLE_9015});
		}
		sql.append(" d ");
		sql.append("left join ab_voucherinfo v on v.orig_no=d.orig_no ");
		sql.append("left join ab_pool p on d.bp_id=p.id ");
		sql.append("left join ab_pool_corp pc on pc.corp_id=d.corp_id and pc.valid_Sign=:validSign ");
		//sql.append("left join sys_corp sc on sc.id=pc.corp_id  ");
		sql.append("where 1=1 ");
		return sql.toString();
	}
	
	/**
	 * "查询票据池明细账簿信息列表数据
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput datatableQuery(DataTablesInput dataTablesInput,List<String> corpAuthList) {
		Map<String,Object> params=new HashMap<String, Object>();
		String stime = (String)dataTablesInput.getFormdata().get("led_startDate");
		if(StringUtils.isNotBlank(stime)){
			stime = stime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_startDate", stime);
		}
		String etime = (String)dataTablesInput.getFormdata().get("led_endDate");
		if(StringUtils.isNotBlank(etime)){
			etime = etime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_endDate", etime);
		}
		StringBuilder sql = new StringBuilder(getBaseSql(false,params));
		params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "pc.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolDtlBookVO.class, null, false, params);
	}
	
	/**
	 * 根据ID获取明细账簿信息
	 * @param entity
	 * @return
	 */
	public List<AbPoolDtlBookVO> getAbPoolDtlBookVOById(String id) {
		if(StringUtils.isEmpty(id)){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder(getBaseSql(false,params));
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        sql.append(" and d.id=:id");
        params.put("id", id);
		return this.listBySQLAliasToBean(sql.toString(), params, AbPoolDtlBookVO.class);
	}
	
}
