package com.bytter.biz.wage.dao;
import com.bytter.biz.wage.entity.NisWageDetail;
import com.bytter.biz.wage.vo.NisWageDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: NIS_WAGE_DETAIL
* @author onlineGenerator
* @date 2018-01-08 17:19:59
* @version 1.0
*
*/
@Repository
public class NisWageDetailDao extends BaseDao<NisWageDetail,String> {

	/**
	 * 查询报销单据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_Dtl_No as wageDtlNo,n.wage_Id as wageId,n.work_No as workNo,n.user_Name as userName,n.bank_Acc as bankAcc,n.acc_Name as accName,");
		sql.append(" n.wages_Verify as wagesVerify,n.purpose as purpose,n.prov as prov,n.city as city,n.bank_Code as bankCode,n.bank_Name as bankName,n.dif_Bank as difBank,");
		sql.append(" n.area_Sign as areaSign,n.voucher_Stat as voucherStat,n.first_return_Msg as firstReturnMsg,n.next_return_Msg as nextReturnMsg,n.return_Time as returnTime,");
		sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate, n.voucher_no as voucherNo,n.id_Type as idType,n.id_No as idNo ");
		sql.append(" from Nis_Wage_Detail n left join Nis_Wage w on n.wage_Id = w.id ");
		sql.append(" where 1 = 1 ");
		if (!ObjectUtils.isEmpty(dataTablesInput.getFormdata().get("operationType"))) {
			String operationType = (String) dataTablesInput.getFormdata().get("operationType");
			if (StringUtils.isNotEmpty(operationType) && "listDeal".equals(operationType)) {
				sql.append(" and (n.voucher_stat <> '" + BdlinkConstant.SUCCESS + "' or n.voucher_stat is null or n.voucher_stat = '') ");
			}
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), NisWageDetailVO.class, "n", false);
	}

	/**
	 * 根据工资Id获取工资明细列表
	 *
	 * @param wageId
	 * @return
	 */
	public List<NisWageDetailVO> listByWageId(String wageId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_Dtl_No as wageDtlNo,n.wage_Id as wageId,n.work_No as workNo,n.user_Name as userName,n.bank_Acc as bankAcc,n.acc_Name as accName,");
		sql.append(" n.wages_Verify as wagesVerify,n.purpose as purpose,n.prov as prov,n.city as city,n.bank_Code as bankCode,n.bank_Name as bankName,n.dif_Bank as difBank,");
		sql.append(" n.area_Sign as areaSign,n.voucher_Stat as voucherStat,n.first_return_Msg as firstReturnMsg,n.next_return_Msg as nextReturnMsg,n.return_Time as returnTime,");
		sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate, n.voucher_no as voucherNo,n.id_Type as idType,n.id_No as idNo ");
		sql.append(" from Nis_Wage_Detail n where n.wage_Id = :wageId ");
		sql.append(" order by n.wage_Dtl_No");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("wageId", wageId);
		return listBySQLAliasToBean(sql.toString(), params, NisWageDetailVO.class);
	}
	/**
	 * 根据工资Id获取非交易成功的工资明细
	 *
	 * @param wageId
	 * @return
	 */
	public List<NisWageDetailVO> listByWageIdNotFailure(String wageId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_Dtl_No as wageDtlNo,n.wage_Id as wageId,n.work_No as workNo,n.user_Name as userName,n.bank_Acc as bankAcc,n.acc_Name as accName,");
		sql.append(" n.wages_Verify as wagesVerify,n.purpose as purpose,n.prov as prov,n.city as city,n.bank_Code as bankCode,n.bank_Name as bankName,n.dif_Bank as difBank,");
		sql.append(" n.area_Sign as areaSign,n.voucher_Stat as voucherStat,n.first_return_Msg as firstReturnMsg,n.next_return_Msg as nextReturnMsg,n.return_Time as returnTime,");
		sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate, n.voucher_no as voucherNo,n.id_Type as idType,n.id_No as idNo ");
		sql.append(" from Nis_Wage_Detail n where n.wage_Id = :wageId ");
		sql.append(" and ( n.voucher_Stat is null or  n.voucher_Stat = ''  or  n.voucher_Stat in ('" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "'))");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("wageId", wageId);
		logger.info("根据工资Id获取非交易成功的工资明细:" + sql.toString()+";wageId="+wageId);
		return listBySQLAliasToBean(sql.toString(), params, NisWageDetailVO.class);
	}
	/**
	 * 获取重复明细
	 *
	 * @param wageId
	 * @return
	 */
	public List<NisWageDetail> listRepeatData(String id, String wageId, String bankAcc, String purpose) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from NisWageDetail n");
		hql.append(" where n.id<>:id and n.wageId=:wageId and n.bankAcc=:bankAcc and n.purpose=:purpose");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("wageId", wageId);
		params.put("bankAcc", bankAcc);
		params.put("purpose", purpose);
		return this.listByHQL(hql.toString(), params);
	}
	/**
	 * 根据工资Id获取交易成功的工资明细
	 *
	 * @param wageId
	 * @return
	 */
	public List<NisWageDetailVO> listByWageIdSuccess(String wageId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_Dtl_No as wageDtlNo,n.wage_Id as wageId,n.work_No as workNo,n.user_Name as userName,n.bank_Acc as bankAcc,n.acc_Name as accName,");
		sql.append(" n.wages_Verify as wagesVerify,n.purpose as purpose,n.prov as prov,n.city as city,n.bank_Code as bankCode,n.bank_Name as bankName,n.dif_Bank as difBank,");
		sql.append(" n.area_Sign as areaSign,n.voucher_Stat as voucherStat,n.first_return_Msg as firstReturnMsg,n.next_return_Msg as nextReturnMsg,n.return_Time as returnTime,");
		sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate, n.voucher_no as voucherNo,n.id_Type as idType,n.id_No as idNo ");
		sql.append(" from Nis_Wage_Detail n ");
		sql.append(" where n.wage_Id = :wageId and n.voucher_stat = '"+ BdlinkConstant.SUCCESS+"'");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("wageId", wageId);
		return listBySQLAliasToBean(sql.toString(), params, NisWageDetailVO.class);
	}
	/**
	 * 带原交易状态更新代发工资明细单
	 * @param vo 代发工资对象
	 * @return
	 */
	public int updateNisWageDetailVoucherStat(NisWageDetailVO vo) {
		StringBuilder hql=new StringBuilder("update NisWageDetail set voucherStat = :voucherStat");
		Map<String,Object> params = new HashMap<>();
		params.put("voucherStat", vo.getVoucherStat());
		if (StringUtils.isNotEmpty(vo.getVoucherNo())) {
			hql.append(",voucherNo=:voucherNo ");
			params.put("voucherNo", vo.getVoucherNo());
		}
		if (StringUtils.isNotEmpty(vo.getUpdateBy())) {
			hql.append(",updateBy=:updateBy ");
			params.put("updateBy", vo.getUpdateBy());
		}
		if (vo.getUpdateDate() != null) {
			hql.append(",updateDate=:updateDate ");
			params.put("updateDate", vo.getUpdateDate());
		}
		hql.append(" where id=:id and ( voucher_Stat is null or  voucher_Stat  =  ''  or voucher_Stat in ('" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "'))");
		params.put("id", vo.getId());
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 根据工资Id获取等待结果的代发工资明细
	 *
	 * @param wageId
	 * @return
	 */
	public List<NisWageDetailVO> listByWageIdWaitResult(String wageId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_Dtl_No as wageDtlNo,n.wage_Id as wageId,n.work_No as workNo,n.user_Name as userName,n.bank_Acc as bankAcc,n.acc_Name as accName,");
		sql.append(" n.wages_Verify as wagesVerify,n.purpose as purpose,n.prov as prov,n.city as city,n.bank_Code as bankCode,n.bank_Name as bankName,n.dif_Bank as difBank,");
		sql.append(" n.area_Sign as areaSign,n.voucher_Stat as voucherStat,n.first_return_Msg as firstReturnMsg,n.next_return_Msg as nextReturnMsg,n.return_Time as returnTime,");
		sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate, n.voucher_no as voucherNo,n.id_Type as idType,n.id_No as idNo ");
		sql.append(" from Nis_Wage_Detail n ");
		sql.append(" where n.wage_Id = :wageId and n.voucher_stat = '"+ BdlinkConstant.WAIT_RESULT+"'");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("wageId", wageId);
		return listBySQLAliasToBean(sql.toString(), params, NisWageDetailVO.class);
	}
	/**
	 * 根据指令编号获取代发工资明细
	 * @param voucherNo
	 * @param wageId 代发工资ID
	 * @return
	 */
	public List<NisWageDetailVO> listByVoucherNo(String voucherNo, String wageId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_Dtl_No as wageDtlNo,n.wage_Id as wageId,n.work_No as workNo,n.user_Name as userName,n.bank_Acc as bankAcc,n.acc_Name as accName,");
		sql.append(" n.wages_Verify as wagesVerify,n.purpose as purpose,n.prov as prov,n.city as city,n.bank_Code as bankCode,n.bank_Name as bankName,n.dif_Bank as difBank,");
		sql.append(" n.area_Sign as areaSign,n.voucher_Stat as voucherStat,n.first_return_Msg as firstReturnMsg,n.next_return_Msg as nextReturnMsg,n.return_Time as returnTime,");
		sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate, n.voucher_no as voucherNo,n.id_Type as idType,n.id_No as idNo ");
		sql.append(" from Nis_Wage_Detail n ");
		sql.append(" where n.wage_Id = :wageId and n.voucher_no = :voucherNo");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("wageId", wageId);
		params.put("voucherNo", voucherNo);
		return listBySQLAliasToBean(sql.toString(), params, NisWageDetailVO.class);
	}
	/**
	 * 根据指令编号更新代发工资明细
	 * @param voucherNo 指令编号
	 * @param wageId 代发工资ID
	 * @return
	 */
	public int updateVoucherStatByVoucherNo(String voucherNo, String wageId, String newVoucherNo){
		StringBuilder hql=new StringBuilder("update NisWageDetail set voucherStat=:voucherStat, voucherNo=:newVoucherNo");
		hql.append(" where wageId = :wageId and voucherNo=:voucherNo and voucherStat in ('"+BdlinkConstant.BANK_FAILED+"','"+BdlinkConstant.INTERFACE_FAILED+"')");
		Map<String,Object> params = new HashMap<>();
		params.put("wageId", wageId);
		params.put("voucherNo", voucherNo);
		params.put("newVoucherNo", newVoucherNo);
		params.put("voucherStat", BdlinkConstant.PRE_SEND);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 根据指令编号更新代发工资明细
	 * @param wageId 代发工资ID
	 * @return
	 */
	public int updateIdNo(String wageId){
		StringBuilder sql = new StringBuilder();
		sql.append(" update nis_wage_detail d set d.id_no = (select p.idcard from nis_personnel_detail p where p.work_no = d.work_no)");
		sql.append(" where d.wage_Id=:wageId");
		Map<String,Object> params = new HashMap<>();
		params.put("wageId", wageId);
		return this.batchSQLExecute(sql.toString(), params);
	}
	/**
	 * 根据指令编号和代发工资ID获取等待结果的费用报销明细列表
	 * @param voucherNo 付款指令编号
	 * @param wageId 代发工资ID
	 * @return 代发工资明细集合
	 */
	public List<NisWageDetailVO> listByVoucherNoWaitResult(String voucherNo, String wageId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_Dtl_No as wageDtlNo,n.wage_Id as wageId,n.work_No as workNo,n.user_Name as userName,n.bank_Acc as bankAcc,n.acc_Name as accName,");
		sql.append(" n.wages_Verify as wagesVerify,n.purpose as purpose,n.prov as prov,n.city as city,n.bank_Code as bankCode,n.bank_Name as bankName,n.dif_Bank as difBank,");
		sql.append(" n.area_Sign as areaSign,n.voucher_Stat as voucherStat,n.first_return_Msg as firstReturnMsg,n.next_return_Msg as nextReturnMsg,n.return_Time as returnTime,");
		sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate, n.voucher_no as voucherNo ");
		sql.append(" from Nis_Wage_Detail n ");
		sql.append(" where n.wage_Id = :wageId and n.voucher_no = :voucherNo and n.voucher_Stat=:voucherStat");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("wageId", wageId);
		params.put("voucherNo", voucherNo);
		params.put("voucherStat", BdlinkConstant.WAIT_RESULT);
		return listBySQLAliasToBean(sql.toString(), params, NisWageDetailVO.class);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param voucherNo 付款指令编号
	 * @param wageId 代发工资ID
	 * @param voucherStat 修改的指令
	 * @return
	 */
	public int updateVoucherStatById(String voucherNo, String wageId, String voucherStat){
		StringBuilder hql=new StringBuilder("update NisWageDetail set voucherStat=:voucherStat");
		hql.append(" where voucherNo=:voucherNo and wageId=:wageId and voucherStat = '"+BdlinkConstant.WAIT_RESULT+"'");
		Map<String,Object> params = new HashMap<>();
		params.put("voucherNo", voucherNo);
		params.put("wageId", wageId);
		params.put("voucherStat", voucherStat);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param voucherNo 付款指令编号
	 * @param wageId 代发工资ID
	 * @param voucherStat 修改的指令
	 * @return
	 */
	public int updateVoucherStatById(String voucherNo, String wageId, String voucherStat, List<String> dtlIds, boolean isIn){
		StringBuilder hql=new StringBuilder("update NisWageDetail set voucherStat=:voucherStat");
		hql.append(" where voucherNo=:voucherNo and wageId=:wageId and voucherStat = '"+BdlinkConstant.WAIT_RESULT+"'");
		if(isIn){
			String idAuthSql = SqlUtil.getInSql(dtlIds, "id", true);
			hql.append(" and ").append(idAuthSql);
		}else{
			String idAuthSql = SqlUtil.getNotinSql(dtlIds, "id", true);
			hql.append(" and ").append(idAuthSql);
		}
		Map<String,Object> params = new HashMap<>();
		params.put("voucherNo", voucherNo);
		params.put("wageId", wageId);
		params.put("voucherStat", voucherStat);
		return this.batchExecute(hql.toString(), params);
	}
}
