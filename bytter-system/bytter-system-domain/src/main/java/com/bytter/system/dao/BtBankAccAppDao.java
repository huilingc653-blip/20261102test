package com.bytter.system.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.vo.BtBankAccAppVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.entity.BtBankAccApp;
import com.bytter.system.service.LicenseService;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银行账号申请表
 * @date 2017-05-23 17:37:39
 */
@Repository
public class BtBankAccAppDao extends BaseDao<BtBankAccApp, String> {
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql, String busType) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.acc_no as accNo,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId, acc.status as status ,");
		sql.append(" acc.acc_type as accType,att.id as attributeId,nat.nature_name as natureName, att.attribute_name as attributeName,");
		sql.append(" nat.id as natureId,acc.bank_name as bankName,acc.bank_type_id as bankTypeId, c.name as corpName,t.type_Name as bankTypeName,");
		sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,acc.op_ression as opRession, ");
		sql.append(" acc.attachment_id as attachmentId, 'app' as isForm, acc.is_domestic_bank as isDomesticBank, ");
		sql.append(" b.acc_use_code as accUseCode, ");
		// 币种名称 curName 仅用于排序(前端 bSortable),展示由 service 层 setCurName 回填完整币种串
		// 使用通用标量子查询 MIN 作为排序列,兼容 MySQL / Oracle / PostgreSQL / 达梦 等所有数据库
		sql.append(" (SELECT MIN(cur.cur_name) FROM BT_BANK_ACC_APP_CUR ac " +
				" LEFT JOIN bt_currency cur ON cur.id = ac.CUR_ID " +
				" WHERE ac.APP_ID = acc.id) as curName ");
		sql.append(" from bt_bank_acc_app acc ");
		sql.append(" left join sys_corp c on c.id = acc.corp_id ");
		sql.append(" left join bt_bank_type t on acc.bank_type_id = t.id ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" LEFT JOIN bt_bank_acc b ON acc.BANK_ACC = b.BANK_ACC ");
		sql.append(" where acc.bus_type =  " + busType + "");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccAppDTO.class, "", false);

	}
	/**
	 * 查询审核列表信息
	 *
	 * @param dataTablesInput
	 * @param businessSql
	 * @return
	 */
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id as id,acc.acc_no as accNo,acc.acc_name as accName,acc.bank_acc as bankAcc,acc.corp_id as corpId, ");
		sql.append(" acc.acc_type as accType,att.id as attributeId, acc.create_date as createDate, ");
		sql.append(" nat.nature_name as natureName,att.attribute_name as attributeName, c.name as corpName, ");
		sql.append(" nat.id as natureId,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
		sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,acc.op_ression as opRession,acc.attachment_id as attachmentId, 'app' as isForm, acc.is_domestic_bank as isDomesticBank, ");
		// 币种名称 curName 展示由 service 层 setCurName 回填完整币种串,SQL 中该列仅用于排序(前端 bSortable)
		// 使用通用标量子查询 MIN 作为排序列,兼容 MySQL / Oracle / PostgreSQL / 达梦 等所有数据库
		sql.append(" (SELECT MIN(cur.cur_name) FROM BT_BANK_ACC_APP_CUR ac " +
				" LEFT JOIN bt_currency cur ON cur.id = ac.CUR_ID " +
				" WHERE ac.APP_ID = acc.id) as curName ");
		sql.append(" from bt_bank_acc_app acc ");
		sql.append(" left join sys_corp c on c.id = acc.corp_id ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		sql.append(" where 1=1");
		if(!EmptyUtils.isEmpty(businessSql)){
			sql.append(" and ").append(businessSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccAppDTO.class, "acc", false);
	}
	/**
	 * 根据Id得到实体账号
	 */
	public BtBankAccVO getBankAcc(String id) {
		String sql = "select * from BT_BANK_ACC where id=:id";
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id);
		BtBankAccVO btBankAcc = getBySQLAliasToBean(sql, param, BtBankAccVO.class);
		return btBankAcc;
	}
	/**
	 * 根据单位统计银行账户(离线)
	 * @param corpId
	 * @return
	 */
	public int getBankAccAppCountByCorpId(String corpId){
		String sql="select count(*) from bt_bank_acc_app where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		//0-暂存、11-待审批、-1-审批打回、95-审批通过（未办理）
		sql+=" and status in ('0','11','-1','95')";
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	/**
	 * 根据Id得到实体账号
	 */
	public BtBankAccAppVO getBankAccApp(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select app.id as id,app.acc_no as accNo,app.bank_acc as bankAcc,app.acc_name as accName,app.corp_id as corpId, ");
		sql.append(" app.acc_type as accType,app.attribute_Id as attributeId,app.bus_type busType, ");
		sql.append(" app.nature_Id as natureId,app.bank_Type_Id as bankTypeId,app.bank_name as bankName, ");
		sql.append(" app.prov as prov,app.city as city,app.electric_bill as electricBill,app.op_ression as opRession,app.attachment_id as attachmentId, ");
		sql.append(" app.regulars as regulars,app.agr_money as agrMoney,app.agr_contract as agrContract,app.agr_interest as agrInterest,app.bank_code as bankCode,");
		sql.append(" app.agr_begin_date as agrBeginDate,app.agr_end_date as agrEndDate,app.notice_type as noticeType,app.entity_acc_id as entityAccId,app.circulation_mode as circulationMode,app.create_by as createBy,");
		sql.append(" app.is_domestic_bank as isDomesticBank,app.is_Offshore_Account as isOffshoreAccount,");
		sql.append(" nat.nature_name as natureName, att.attribute_name as attributeName,c.name as corpName,");
		sql.append(" acc.RECEIPT_CODE as receiptCode,acc.FC_ACC_GROUP_CODE as fcAccGroupCode,acc.acc_use_code as accUseCode ");
		sql.append(" from bt_bank_acc_app app ");
		sql.append(" left join sys_corp c on c.id = app.corp_id ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on app.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature nat on app.nature_id = nat.id ");
		sql.append(" LEFT JOIN bt_bank_acc acc ON app.BANK_ACC = acc.BANK_ACC ");
		sql.append(" where app.id = :id");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, BtBankAccAppVO.class);
	}

	public List<Map<String,String>> getReceiptList(List<String> codes) {
		StringBuilder sql = new StringBuilder();
		sql.append("select receipt_code as receiptCode,receipt_name as receiptName ");
		sql.append("from fc_receipt_type ");
		sql.append("where receipt_code in (:codes) ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("codes", codes);
		//sql.append("where ").append(SqlUtil.getInSql(codes, "receipt_code",true));
		return this.listBySQL(sql.toString(),params);
	}

	public List<Map<String,String>> getAccGroupList(List<String> codes) {
		StringBuilder sql = new StringBuilder();
		sql.append("select acc_group_code as fcAccGroupCode,acc_group_name as fcAccGroupName ");
		sql.append("from fc_bank_acc_group ");
		sql.append("where acc_group_code in (:codes) ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("codes", codes);
		return this.listBySQL(sql.toString(),params);
	}
}
