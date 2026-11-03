package com.bytter.biz.bdlinks.dao;

import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlinks.entity.BisGatherCmd;
import com.bytter.biz.bdlinks.vo.BisGatherCmdVO;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 收款指令表
* @author onlineGenerator
* @date 2017-07-04 15:57:19
* @version 1.0
*
*/
@Repository
public class BisGatherCmdDao extends BaseDao<BisGatherCmd,String>{
	/**
	 * 收款指令表通用查@param sqlWhere //TODO 数据同步到 bis_gather_cmd
	 *   --table bt_bank_acc
	 	*alter table Bis_Gather_Cmd  add  CHILD_ACC varchar(60) DEFAULT '';
		 alter table Bis_Gather_Cmd  add  CHILD_ACC_NAME varchar(140) DEFAULT '';
		 alter table Bis_Gather_Cmd  add  CHILD_BANK varchar(150) DEFAULT '';
		 alter table Bis_Gather_Cmd  add  CHILD_PROV varchar(32) DEFAULT '';
		 alter table Bis_Gather_Cmd  add  CHILD_CITY varchar(32) DEFAULT '';
		 alter table Bis_Gather_Cmd  add  PARENT_ACC varchar(60) DEFAULT '';
		 alter table Bis_Gather_Cmd  add  PARENT_ACC_NAME varchar(140) DEFAULT '';
		 alter table Bis_Gather_Cmd  add  PARENT_BANK varchar(150) DEFAULT '';
		 alter table Bis_Gather_Cmd  add  PARENT_PROV varchar(32) DEFAULT '';
		 alter table Bis_Gather_Cmd  add  PARENT_CITY varchar(32) DEFAULT '';
		 alter table Bis_Gather_Cmd  add  CORP_ID varchar(32) DEFAULT '';
		 --bis_bif_init
		 alter table bis_query_cmd  add  BIF_NAME varchar(30) DEFAULT '';
	 * @return
	 */
	private String getCommonSql() {
		StringBuilder sql=new StringBuilder();
		sql.append(" select b.id as id,b.voucher_No as voucherNo,b.cbs_Bill_Id as cbsBillId,b.cbs_Bill_No as cbsBillNo,b.bif_Code as bifCode,b.child_Acc_Id as childAccId, ");
		sql.append(" b.parent_Acc_Id as parentAccId,b.cur_Id as curId,b.gather_Amt as gatherAmt,b.gather_Policy_Id as gatherPolicyId,b.run_Time as runTime,b.is_Quick as isQuick, ");
		sql.append(" b.cmd_Type as cmdType,b.dtl_Id as dtlId,b.bank_Serial_No as bankSerialNo,b.bus_Ori_Type AS busOriType,purpose,add_Word AS addWord,");
		sql.append(" b.cmd_Stat as cmdStat,b.trans_Time as transTime,b.return_Time as returnTime,b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg, ");
		sql.append(" b.resend_Sign as resendSign,b.notice_Sign as noticeSign,b.verify_Code as verifyCode,b.voucher_From AS voucherFrom,");
		sql.append(" b.child_acc as childAcc,b.child_acc_Name as childAccName,b.child_bank as childBank,b.child_city as childCity,b.child_prov as childProv,");
		sql.append(" b.parent_acc as parentAcc,b.parent_acc_Name as parentAccName,b.parent_bank as parentBank,b.parent_city as parentCity, b.parent_prov as parentProv");
		sql.append(" from Bis_Gather_Cmd b");
		sql.append(" left join bt_bank_acc c on b.child_acc_Id = c.id");
		sql.append(" left join bt_bank_acc p on b.parent_acc_Id = p.id");
		sql.append(" where 1=1 ");
		return sql.toString();
	}
	/**
	 * 指令列表查询
	 *  //DOTO冗余字段数据同步
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,String joinSql) {
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(joinSql);
		}
		//申请日期
		String gedRunTime = (String) dataTablesInput.getFormdata().get("ged_runTime");
		String ledRunTime = (String) dataTablesInput.getFormdata().get("led_runTime");
		if(StringUtils.isNotEmpty(gedRunTime)){
			gedRunTime = gedRunTime + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_runTime", gedRunTime);
		}
 		if(StringUtils.isNotEmpty(ledRunTime)){
			ledRunTime = ledRunTime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_runTime", ledRunTime);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BisGatherCmdVO.class, "b", false);
	}
	/**
	 * 根据ID获取指令 
	 *  //TODO冗余字段数据同步
	 * @param id
	 * @return
	 */
	public BisGatherCmdVO getByIdUnion(String id){
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		sql.append(" and b.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        BisGatherCmdVO bisVo = getBySQLAliasToBean(sql.toString(), params, BisGatherCmdVO.class);
        return bisVo;
	}
	/**
	 * 根据ID获取指令
	 *  //TODO冗余字段数据同步
	 * @param cbsBillId
	 * @return
	 */
	public BisGatherCmdVO getByCbsBillIdUnion(String cbsBillId){
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		sql.append(" and b.cbs_Bill_Id=:cbsBillId");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cbsBillId", cbsBillId);
		BisGatherCmdVO bisVo = getBySQLAliasToBean(sql.toString(), params, BisGatherCmdVO.class);
		return bisVo;
	}
	/**
	 * 根据指定字段查询指令
	 * @param voucherNo
	 * @return
	 */
	public BisGatherCmdVO getByVoucherNo(String voucherNo){
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		sql.append(" and b.voucher_No=:voucherNo");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("voucherNo", voucherNo);
		return getBySQLAliasToBean(sql.toString(), params, BisGatherCmdVO.class);
	}
	
	/**
	 * 获取交易成功/失败的指令（供指令成功/失败提醒）
	 * @param flag true-成功 false-失败
	 * @return
	 */
	public List<BisGatherCmdVO> listByVoucherStat(boolean flag){
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		if(flag){
			sql.append(" and b.cmd_Stat in ('"+BdlinkConstant.SUCCESS+"')");
		}else{
			sql.append(" and b.cmd_Stat in ('"+BdlinkConstant.BANK_FAILED+"','"+BdlinkConstant.INTERFACE_FAILED+"')");
		}
		//取未生成消息提醒的指令
		sql.append(" and (b.notice_sign <> '1' OR b.notice_sign is null or b.notice_sign='')");
		return listBySQLAliasToBean(sql.toString(), null, BisGatherCmdVO.class);
	}
	/**
	 * 根据单位统计收款指令
	 * @param corpId
	 * @param busType auto:自动 hand:手动
	 * @return
	 */
	public int getBisGatherCmdCountByCorpId(String corpId, String busType){
		String sql="select count(*) from bis_gather_cmd b ";
		if("hand".equals(busType)){
			sql+=" where b.corp_id=:corpId and b.cbs_bill_id is not null ";
		}else{
			sql+="  where b.corp_id=:corpId and b.cbs_bill_id is null ";
		}
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		//6-待发送、3-准备发送、1-等待结果、2-银行处理失败、9-接口处理失败
		sql += " and cmd_Stat in ('1','3','6','2','9')";
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	/**
	 * 指令状态更新（针对指令发送、指令退回、指令重发、指令作废等操作，带状态更新指令）
	 * @param bisGatherCmd
	 * @param cmdStat
	 * @return
	 */
	public int updateBisGatherCmdVoucherStat(BisGatherCmd bisGatherCmd, String cmdStat){
		StringBuilder hql=new StringBuilder("update BisGatherCmd set cmdStat = :cmdStat");
		Map<String,Object> params = new HashMap<>();
		if(BdlinkConstant.DELETE.equals(cmdStat)) {
			//指令重发
			hql.append(",cbsBillId=:cbsBillId,cbsBillNo=:cbsBillNo");
			if (EmptyUtils.isBlank(bisGatherCmd.getCbsBillId())) {
				//业务ID为空，则取指令本身的ID为业务ID
				params.put("cbsBillId", bisGatherCmd.getId() + "_D");
				params.put("cbsBillNo", bisGatherCmd.getVoucherNo() + "_D");
			} else {
				params.put("cbsBillId", bisGatherCmd.getCbsBillId() + "_D");
				params.put("cbsBillNo", bisGatherCmd.getCbsBillNo() + "_D");
			}
		}
		if(BdlinkConstant.PRE_SEND.equals(cmdStat)){
			//指令发送，状态从w-待发送更新为3-准备发送
			hql.append(",runTime=:runTime");
			//更新指令申请日期
			params.put("runTime", DateUtils.getDate());
		}
		hql.append(" where id=:id");
		if(BdlinkConstant.PRE_SEND.equals(cmdStat)){
			//指令发送，状态从w-待发送更新为3-准备发送
			hql.append(" and cmdStat = '"+BdlinkConstant.WAIT_FILL+"'");
		}
		if(BdlinkConstant.DELETE.equals(cmdStat) || BdlinkConstant.INVALIDATE.equals(cmdStat)) {
			//指令重发/作废，更新状态控制必须是9-接口处理失败或者2-银行处理失败
			hql.append(" and cmdStat in ('"+BdlinkConstant.INTERFACE_FAILED+"','"+BdlinkConstant.BANK_FAILED+"')");
		}
		if(BdlinkConstant.BACK.equals(cmdStat)){
			//指令退回，更新状态控制必须是6-待发送或者9-接口处理失败或者2-银行处理失败
			hql.append(" and cmdStat in ('"+BdlinkConstant.WAIT_FILL+"','"+BdlinkConstant.INTERFACE_FAILED+"','"+BdlinkConstant.BANK_FAILED+"')");
		}
		params.put("id", bisGatherCmd.getId());
		params.put("cmdStat", cmdStat);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param bisGatherCmd
	 * @return
	 */
	public int updateBisGatherCmdCbsBillNo(BisGatherCmd bisGatherCmd){
		StringBuilder hql=new StringBuilder("update BisGatherCmd set cbsBillId=:cbsBillId,cbsBillNo=:cbsBillNo");
		hql.append(" where id=:id and cmdStat in ('"+BdlinkConstant.BACK+"','"+BdlinkConstant.INVALIDATE+"')");
		Map<String,Object> params = new HashMap<>();
		params.put("id", bisGatherCmd.getId());
		params.put("cbsBillId", bisGatherCmd.getCbsBillId() + "_D");
		params.put("cbsBillNo", bisGatherCmd.getCbsBillNo() + "_D");
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 批量修改提醒标志
	 * @param ids
	 */
	public void updateNoticeSign(List<String> ids){
		StringBuilder sql=new StringBuilder();
		sql.append("update bis_gather_cmd set notice_sign=:noticeSign");
		sql.append(" where").append(SqlUtil.getInSql(ids, "id", true));
		HashMap<String, String> params = new HashMap<>();
		params.put("noticeSign", CommonConstant.SEND_SIGN_YES);
		this.batchSQLExecute(sql.toString(), params);
	}
	/**
	 * 根据业务ID集合和指令来源，获取指令集合
	 * @param voucherFrom
	 * @return
	 */
	public List<BisGatherCmdDTO> getBisGatherCmdByCbsBillIds(List<String> billIds, String voucherFrom) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.voucher_No as voucherNo,b.cmd_Stat as cmdStat,b.cbs_Bill_Id as cbsBillId from bis_gather_cmd b");
		sql.append(" where ").append(SqlUtil.getInSql(billIds, "b.cbs_Bill_Id", true));
		if (!StringUtil.isBlank(voucherFrom)) {
			sql.append(" and ").append(SqlUtil.getInSql(voucherFrom.split(CommonConstant.COMMA_SPLIT), "b.voucher_From", true));
		}
		return listBySQLAliasToBean(sql.toString(), null, BisGatherCmdDTO.class);
	}
	/**
	 * 根据业务单号集合和指令来源，获取指令集合
	 * @param voucherFrom
	 * @return
	 */
	public List<BisGatherCmdDTO> getBisGatherCmdByCbsBillNos(List<String> billNos, String voucherFrom) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.voucher_No as voucherNo,b.cmd_Stat as cmdStat from bis_gather_cmd b");
		sql.append(" where ").append(SqlUtil.getInSql(billNos, "b.cbs_Bill_No", true));
		if (!StringUtil.isBlank(voucherFrom)) {
			sql.append(" and ").append(SqlUtil.getInSql(voucherFrom.split(CommonConstant.COMMA_SPLIT), "b.voucher_From", true));
		}
		return listBySQLAliasToBean(sql.toString(), null, BisGatherCmdDTO.class);
	}
}
