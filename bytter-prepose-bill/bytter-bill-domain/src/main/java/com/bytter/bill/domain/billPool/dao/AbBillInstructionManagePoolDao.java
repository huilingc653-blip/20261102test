package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbBillInstructionManagePoolVO;
import com.bytter.bill.domain.billPool.vo.AbPoolBailDtlVO;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitFinaApplyVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 票据池指令处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@Repository
public class AbBillInstructionManagePoolDao extends BaseDao<AbBillInstructionManagePoolVO, String> {
	/**
	 * 加载测试票据池保证金表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listAbBillInstructionManageTestPoolBail(DataTablesInput dataTablesInput,String corpAuthSql) {
		String etime = (String)dataTablesInput.getFormdata().get("led_appDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_appDate", etime);
        }
		StringBuilder sql = new StringBuilder();
        sql.append(" select dtl.id as id,pb.BANK_ACC as bankAcc,BT_CONCAT4('(',pb.CORP_CODE,')',pb.CORP_NAME) as corpCodeAndName,dtl.MONEY as money,");
        sql.append(" BT_CONCAT4('(',p.BP_CODE,')',p.BP_NAME) as pbCodeAndName,dtl.APP_DATE as appDate,dtl.OPP_ACC as oppAcc, ");
        sql.append(" dtl.STATUS as status,dtl.OPT_STATUS as optStatus,dtl.opp_acc_name as oppAccName,dtl.bail_money as bailMoney, ");
        sql.append(" dtl.type,dtl.opp_bank_name as oppBankName,pb.bail_date as bailDate,dtl.RETURN_MSG as returnMsg,e.re_code as returnCode ");
        sql.append(" from AB_POOL_BAIL_DTL dtl left join AB_POOL_BAIL pb on dtl.BAIL_ID=pb.id ");
//        sql.append(" left join sys_corp s on s.id=pb.corp_id  ");
        sql.append(" left join AB_POOL p on p.id=pb.BP_ID  ");
        sql.append(" left join AB_POOL_BANK_EXC e on dtl.id=e.id  ");
        sql.append(" where 1=1 ")
		.append(" and dtl.STATUS='"+AccebillConstant.AB_BILL_BUS_STATUS95+"' ")
		.append(" and dtl.OPT_STATUS in('"+
				AccebillConstant.AB_BILL_OPT_STATUS16+"','"+
				AccebillConstant.AB_BILL_OPT_STATUS17+"','"+
				AccebillConstant.AB_BILL_OPT_STATUS18
				+"') ")
		.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolBailDtlVO.class, null, false);
	}
	
	/**
	 * 加载测试 票据池额度表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbPoolLimitAllotVO> listAbBillInstructionManageTestPoolLimit(DataTablesInput dataTablesInput,String corpAuthSql) {
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String createTime = (String) formdata.get("led_createTime");
        //手动拼接时间条件，时间戳类型
        if (StringUtils.isNotEmpty(createTime)) {
            createTime += " 23:59:59";
            formdata.put("led_createTime", createTime);
            dataTablesInput.setFormdata(formdata);
        }
		 StringBuilder sql = new StringBuilder(" select APLA.ID AS id,APLA.type,APLA.Corp_Id as corpId,APLA.Bp_Id as bpId," +
				 "APLA.Bustype_Id as busTypeId,APLA.Limt_Type as limtType,APLA.Apply_Money as applyMoney,APLA.End_Date as endDate," +
				 "APLA.CREATE_TIME as createTime,APLA.Opt_Status as optStatus,APLA.Status as status,APLA.Rmk as rmk," +
				 "APLA.check_type as checkType,APLA.check_msg as checkMsg, ");
		 sql.append(" AP.BP_NAME as bpName, APC.PARENT_ID as parentId,APC.Bustype_Level as bustypeLevel,APC.CORP_CODE as corpCode," +
				 "APC.CORP_NAME as corpName,PPC.CORP_NAME as parentName,APB.BUSTYPE_NAME as busTypeName, ");
		 sql.append(" e.re_code as returnCode,e.re_msg as returnMsg ");
		 sql.append(" from ( ");
		 sql.append(" select a.ID,'allot' as type,a.Corp_Id,a.Bp_Id,a.Bustype_Id,a.Limt_Type,a.Apply_Money,a.End_Date," +
				 "a.CREATE_TIME,a.Opt_Status,a.Status,a.Rmk,null as check_type,null as check_msg ");
		 sql.append(" from AB_POOL_LIMIT_ALLOT a  ");
		 sql.append(" union all ");
		 sql.append(" select a.ID,'allotCheck' as type,a.Corp_Id,a.Bp_Id,a.Bustype_Id,a.Limt_Type,a.Apply_Money,a.End_Date," +
				 "a.CREATE_TIME,a.Opt_Status,a.Status,a.Rmk,a.check_type,a.check_msg ");
		 sql.append(" from AB_POOL_LIMIT_ALLOTcheck a  ");
		 sql.append(" union all ");
		 sql.append(" select a.ID,'apply' as type,a.Corp_Id,a.Bp_Id,a.Bustype_Id,a.Limt_Type,a.Apply_Money,a.End_Date," +
				 "a.CREATE_TIME,a.Opt_Status,a.Status,a.Rmk,null as check_type,null as check_msg ");
		 sql.append(" from AB_POOL_LIMIT_apply a  ");
		 sql.append(" union all ");
		 sql.append(" select a.ID,'applyCheck' as type,a.Corp_Id,a.Bp_Id,a.Bustype_Id,a.Limt_Type,a.Apply_Money,a.End_Date," +
				 "a.CREATE_TIME,a.Opt_Status,a.Status,a.Rmk,a.check_type,a.check_msg ");
		 sql.append(" from AB_POOL_LIMIT_applycheck a  ");
		 sql.append(" ) APLA ");
		 sql.append(" LEFT JOIN AB_POOL AP ");
		 sql.append(" ON AP.ID = APLA.BP_ID ");
		 sql.append(" LEFT JOIN AB_POOL_CORP APC ");
		 sql.append(" ON APC.CORP_ID = APLA.CORP_ID and APC.valid_Sign='1' ");
		 sql.append(" LEFT JOIN AB_POOL_CORP PPC ");
		 sql.append(" ON PPC.ID = APC.PARENT_ID and PPC.valid_Sign='1' ");

		 sql.append(" LEFT JOIN AB_POOL_BUSTYPE APB ");
		 sql.append(" ON APB.ID = APLA.BUSTYPE_ID ");
		 sql.append(" left join AB_POOL_BANK_EXC e on e.id=APLA.Id ");
		 sql.append(" WHERE 1 = 1 ").append(" and APLA.STATUS='" + AccebillConstant.AB_BILL_BUS_STATUS95 + "' ")
		.append(" and APLA.OPT_STATUS in('"+
				AccebillConstant.AB_BILL_OPT_STATUS16+"','"+
				AccebillConstant.AB_BILL_OPT_STATUS17+"','"+
				AccebillConstant.AB_BILL_OPT_STATUS18
				+"') ")
		.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolLimitAllotVO.class, null, false);
	}
	
	/**
	 * 加载测试 票据池合同管理表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listAbBillInstructionManageTestPoolCon(DataTablesInput dataTablesInput,String corpAuthSql) {
		Map<String, Object> params = new HashMap<>();
		String etime = (String)dataTablesInput.getFormdata().get("led_startDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_startDate", etime);
        }
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" BT_CONCAT4('(',apc.corp_code,')',apc.corp_name) as corpCodeAndName,dtl.id as dtlId,v.bill_Type as billType, v.bill_Flag as billFlag, ");
        sql.append(" v.bill_Money as billMoney,v.banendrsmtmk as banendrsmtmk,v.start_Date as startDate,v.end_Date as endDate,v.payer as payer, ");
        sql.append(" v.PAYERACC as payeracc, v.acceptor as acceptor,BT_CONCAT4('(',p.BP_CODE,')',p.BP_NAME) as pbCodeAndName,v.payee as payee, ");
        sql.append(" dtl.STATUS as status,dtl.OPT_STATUS as optStatus,vv.corp_Id as corpId,dtl.APP_NO as appNo,abCon.status as conStatus,a.id as aid, ");
        sql.append(" vv.abs as abs,v.payeeacc as payeeacc,v.ACCEPTOR_BANK_NAME as acceptorBankName,abCon.id as conId,abCon.FILE_ID as fileId,dtl.bp_Id as bpId, ");
        sql.append(" v.PAYEE_BANK_NAME as payeeBankName,v.PAYEEBANKNO as payeebankno,v.ACCEPTORACC as acceptoracc,v.ACCEPTORBANKNO as acceptorbankno, ");
        sql.append(" abCon.STATUS as conOpType,abCon.OPT_STATUS as conOptStatus,e.re_code as returnCode,e.re_msg as returnMsg ");
        sql.append(" from ab_pool_dtl dtl left join AB_VOUCHERINFO_VICE vv on vv.id=dtl.vv_id left join AB_VOUCHERINFO v on v.id=vv.vid ");
        sql.append(" left join ab_accrecords a on a.pd_id=dtl.id left join ab_pool p on p.id=dtl.bp_id  ");
        sql.append(" left join ab_pool_corp apc on vv.corp_id = apc.corp_id and apc.valid_sign=1 ");
        sql.append(" left join AB_POOL_CONTRACT abCon on dtl.id=abCon.dtl_Id  ");
		sql.append(" left join AB_POOL_BANK_EXC e on e.id=abCon.Id ");
        sql.append(" where dtl.valid_Sign=:validSign ")
		.append(" and abCon.OPT_STATUS in ('"+
				AccebillConstant.AB_BILL_OPT_STATUS16+"','"+
				AccebillConstant.AB_BILL_OPT_STATUS17+"','"+
				AccebillConstant.AB_BILL_OPT_STATUS18
				+"') ")
		.append(" and ").append(corpAuthSql).append(" ");
        sql.append(" and dtl.brief_Id=:briefId ");
        params.put("briefId", AbPoolConstant.AB_POOL_BRIEF_8001);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolEmitFinaApplyVO.class, null, false,params);
	}
	/**
	 * 根据id修改表状态
	 * @param params
	 * @return
	 */
	public int updateStatusByParams(String tableName,Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update " + tableName + " set  updateTime = :updateTime ");
		if(params.containsKey("updateId")){
			hql.append(",updateId = :updateId ");
		}
		if(params.containsKey("status")){
			hql.append(",status = :status ");
		}
		if (params.containsKey("optStatus")) {
			hql.append(",optStatus = :optStatus ");
		}
		hql.append(" where 1=1 ");
		if(params.containsKey("id")){
			hql.append(" and ").append(" id=:id");
		}
		if(params.containsKey("status")){
			hql.append(" and status<>:status ");
		}
		if (params.containsKey("optStatus")) {
			hql.append(" and optStatus<>:optStatus ");
		}
		//指令重发修改票据池指令表的return_code为3
		if(params.containsKey("id")
				&&AbPoolConstant.AB_POOL_OPT_STATUS16.equals(params.get("optStatus"))){
			params.put("returnCode",AccebillConstant.AB_BILL_RETURN_CODE_3);
			this.batchSQLExecute(" update AB_POOL_BANK_EXC set RE_CODE=:returnCode where id=:id ",params);
		}
		return this.batchExecute(hql.toString(), params);
	}
}
