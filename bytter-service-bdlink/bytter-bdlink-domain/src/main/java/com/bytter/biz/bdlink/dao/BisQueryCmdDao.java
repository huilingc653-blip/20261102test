package com.bytter.biz.bdlink.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import com.bytter.biz.bdlink.entity.BisQueryCmd;
import com.bytter.biz.bdlink.vo.BisQueryCmdVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
/**
* @Title: Dao
* @Description: BIS_GATHER_CMD
* @author onlineGenerator
* @date 2017-12-28 16:31:56
* @version 1.0
*
*/
@Repository
public class BisQueryCmdDao extends BaseDao<BisQueryCmd,String> {
	
	static Map querymap = new  HashMap();
	/**
	 * 指令列表查询
	 * @param dataTablesInput
	 * @return
	 * select b.id as id,b.voucher_No as voucherNo,b.trans_Id as transId,b.send_Time as sendTime,b.bank_Acc_Id as bankAccId,b.bank_Acc as bankAcc,
		b.cur_Id as curId,b.start_Time as startTime,b.end_Time as endTime,b.bif_Code as bifCode,b.cmd_Type as cmdType,b.cmd_Stat as cmdStat,b.trans_Time as transTime,
		b.return_Time as returnTime,b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg,
		c.corp_Id as corpId,c.acc_Name as accName,c.bank_Name as bankName,
		t.name as bifName,
		from bis_query_cmd b left join bt_bank_acc c on  b.bank_Acc_Id = c.id
		left join bis_bif_init t on b.bif_Code = t.id
		where b.id=:id
		//TODO 如果 bt_bank_acc ,bis_bif_init 的对应字段又发生变化则消息同步数据
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,String joinSql) {
		StringBuilder sql=new StringBuilder();
		sql.append("select b.id as id,b.voucher_No as voucherNo,b.trans_Id as transId,b.send_Time as sendTime,b.bank_Acc_Id as bankAccId,b.bank_Acc as bankAcc,b.acc_Name as accName,b.bank_Name as bankName,");
		sql.append(" b.cur_Id as curId,b.start_Time as startTime,b.end_Time as endTime,b.bif_Code as bifCode,b.bif_name as bifName,b.cmd_Type as cmdType,b.cmd_Stat as cmdStat,b.trans_Time as transTime,");
		sql.append(" b.return_Time as returnTime,b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg,b.corp_Id as corpId, b.corp_name as corpName");
		sql.append(" from bis_query_cmd b  where 1=1");
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(joinSql);
		}
		//发送日期
		String gedSendTime = (String) dataTablesInput.getFormdata().get("ged_sendTime");
		String ledSendTime = (String) dataTablesInput.getFormdata().get("led_sendTime");
		if(StringUtils.isNotEmpty(gedSendTime)){
			gedSendTime = gedSendTime + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_sendTime", gedSendTime);
		}
		if(StringUtils.isNotEmpty(ledSendTime)){
			ledSendTime = ledSendTime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_sendTime", ledSendTime);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BisQueryCmdVO.class, "b", false);
	}
	
	/**
	 * 根据ID获取指令
	 * @param id
	 * @return
	 */
	public BisQueryCmdVO getByIdUnion(String id){
		StringBuilder sql=new StringBuilder();
		sql.append("select b.id as id,b.voucher_No as voucherNo,b.trans_Id as transId,b.send_Time as sendTime,b.bank_Acc_Id as bankAccId,b.bank_Acc as bankAcc,b.acc_Name as accName,b.bank_Name as bankName, ");
		sql.append(" b.cur_Id as curId,b.start_Time as startTime,b.end_Time as endTime,b.bif_Code as bifCode,b.cmd_Type as cmdType,b.cmd_Stat as cmdStat,b.trans_Time as transTime,");
		sql.append(" b.return_Time as returnTime,b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg,b.corp_Id as corpId ");
		sql.append(" from bis_query_cmd b ");
		sql.append(" where b.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisQueryCmdVO.class);
	}
	/**
	 * 根据单位统计查询指令
	 * @param corpId
	 * @return
	 */
	public int getBisQueryCmdCountByCorpId(String corpId){
		String sql="select count(*) from bis_query_cmd b left join bt_bank_acc c on b.bank_acc_id = c.id where c.corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		//6-待发送、3-准备发送、1-等待结果、2-银行处理失败、9-接口处理失败
		sql += " and cmd_Stat in ('1','3','6','2','9')";
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	
	/**
	 * 根据单位统计查询指令
	 * @param bankAccIds
	 * @return
	 */
	public int getBisQueryCmdCountByBankAccIds(List<String> bankAccIds){
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from bis_query_cmd b where 1=1 ");
		//6-待发送、3-准备发送、1-等待结果、2-银行处理失败、9-接口处理失败
		sql.append(" and b.cmd_Stat in ('1','3','6','2','9') ");
		if(!CollectionUtils.isEmpty(bankAccIds)) {
			sql.append(" and ").append(getSQLIn(bankAccIds, 1000, "bank_acc_id"));
		}
		//避免每次查询都创建querymap
		return ((Number)this.createSQLQuery(sql.toString(),querymap).uniqueResult()).intValue();
	}
}
