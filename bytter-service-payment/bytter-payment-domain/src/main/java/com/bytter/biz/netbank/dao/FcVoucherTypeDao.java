package com.bytter.biz.netbank.dao;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.netbank.vo.FcVoucherTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @Description 付款方式维护
 * @date 2017.9.14
 */
@Repository
public class FcVoucherTypeDao extends BaseDao<FcVoucherType, String> {
	/**
	 * 根据编码获取付款方式
	 *
	 * @param voucherCode
	 * @return
	 */
	public FcVoucherType findByCode(String voucherCode) {
		return getUniqueBy("voucherType", voucherCode);
	}
	/**
	 * 根据编码获取付款方式
	 *
	 * @param voucherCode
	 * @return
	 */
	public FcVoucherTypeDTO getFcVoucherTypeByCode(String voucherCode) {
		if (EmptyUtils.isEmpty(voucherCode)) {
			return null;
		}
		FcVoucherType fcVoucherType = getUniqueBy("voucherType", voucherCode);
		FcVoucherTypeDTO fcVoucherTypeDTO = new FcVoucherTypeDTO();
		BeanUtils.copyProperties(fcVoucherType, fcVoucherTypeDTO);
		return fcVoucherTypeDTO;
	}
	/**
	 * <b>功能：</b>查询所有有效、记账的付款方式<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	public List<FcVoucherTypeDTO> getFcVoucherType() {
		String hql = "FROM FcVoucherType  WHERE validSign=:validSign and isAccount=:isAccount order by voucherType";
		Map<String, Object> params = new HashMap<>();
		params.put("validSign", "1");
		params.put("isAccount", "1");
		List<FcVoucherTypeVO> fcVoucherType = listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
		List<FcVoucherTypeDTO> fcVoucherTypeDTO = BeanUtils.copyListProperties(fcVoucherType, FcVoucherTypeDTO.class);
		return fcVoucherTypeDTO;
	}
	/**
	 * <b>功能：</b>查询所有有效的付款方式<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	public List<FcVoucherTypeDTO> getValidFcVoucherType() {
		String hql = "FROM FcVoucherType  WHERE validSign=:validSign order by voucherType";
		Map<String, Object> params = new HashMap<>();
		params.put("validSign", "1");
		List<FcVoucherTypeVO> fcVoucherType = listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
		List<FcVoucherTypeDTO> fcVoucherTypeDTO = BeanUtils.copyListProperties(fcVoucherType, FcVoucherTypeDTO.class);
		return fcVoucherTypeDTO;
	}
	/**
	 * 查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<FcVoucherTypeVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder();
		sql.append("select f.id as id,f.voucher_Type as voucherType,f.voucher_Name as voucherNameShow,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,");
		sql.append(" f.cash_Trans_Sign as cashTransSign,f.settl_Type as settlType,f.is_Electron_Pay as isElectronPay,f.is_System as isSystem,");
		sql.append(" f.is_Account as isAccount,f.is_Budget as isBudget,f.rmk as rmk,f.valid_Sign as validSign,b.sys_Voucher_Name as sysVoucherName,b.id as sysVoucherId,");
		sql.append(" f.send_Mode as sendMode");
		sql.append(" from Fc_Voucher_Type f,Bt_Pay_Type b where f.sys_Voucher_Code=b.sys_Voucher_Code");
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcVoucherTypeVO.class, "f", false);
	}
	/**
	 * 根据ID获取付款方式信息
	 *
	 * @param id
	 * @return
	 */
	public FcVoucherTypeVO getByIdUnion(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append("select f.id as id,f.voucherType as voucherType,f.voucherName as voucherName,f.sysVoucherCode as sysVoucherCode,");
		hql.append(" f.cashTransSign as cashTransSign,f.settlType as settlType,f.isElectronPay as isElectronPay,f.isSystem as isSystem,");
		hql.append(" f.isAccount as isAccount,f.isBudget as isBudget,f.rmk as rmk,f.validSign as validSign,b.sysVoucherName as sysVoucherName,b.id as sysVoucherId,");
		hql.append(" f.sendMode as sendMode");
		hql.append(" from FcVoucherType f,BtPayType b where f.sysVoucherCode=b.sysVoucherCode and f.id=:id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
	}


    /**
     * 根据ID获取付款方式信息
     *
     * @param id
     * @return
     */
    public FcVoucherTypeVO getByIdUnionById(String id) {
        StringBuilder hql = new StringBuilder();
        hql.append("select f.id as id,f.voucherType as voucherType,f.voucherName as voucherName,f.sysVoucherCode as sysVoucherCode,");
        hql.append(" f.cashTransSign as cashTransSign,f.settlType as settlType,f.isElectronPay as isElectronPay,f.isSystem as isSystem,");
        hql.append(" f.isAccount as isAccount,f.isBudget as isBudget,f.rmk as rmk,f.validSign as validSign,b.sysVoucherName as sysVoucherName,b.id as sysVoucherId,");
        hql.append(" f.sendMode as sendMode");
        hql.append(" from FcVoucherType f left join BtPayType b on f.sysVoucherCode=b.sysVoucherCode   where  f.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
    }

	/**
	 * 根据ID获取付款方式信息
	 *
	 * @param id
	 * @return
	 */
	public FcVoucherType getById(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append("select f.id as id,f.voucherType as voucherType,f.voucherName as voucherName,f.sysVoucherCode as sysVoucherCode,");
		hql.append(" f.cashTransSign as cashTransSign,f.settlType as settlType,f.isElectronPay as isElectronPay,f.isSystem as isSystem,");
		hql.append(" f.isAccount as isAccount,f.isBudget as isBudget,f.rmk as rmk,f.validSign as validSign,");
		hql.append(" f.sendMode as sendMode,f.billSectionControl as billSectionControl ");
		hql.append(" from FcVoucherType f where f.id=:id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getByHQLAliasToBean(hql.toString(), params, FcVoucherType.class);
	}
	/**
	 * 根据系统结算方式获取对应的初始化的付款方式
	 *
	 * @param sysVoucherCode
	 * @return
	 */
	public FcVoucherTypeVO getSystemBySysVoucherCode(String sysVoucherCode) {
		StringBuilder hql = new StringBuilder();
		hql.append("select f.id as id,f.voucherType as voucherType,f.voucherName as voucherName,f.sysVoucherCode as sysVoucherCode,");
		hql.append(" f.cashTransSign as cashTransSign,f.settlType as settlType,f.isElectronPay as isElectronPay,f.isSystem as isSystem,");
		hql.append(" f.isAccount as isAccount,f.isBudget as isBudget,f.rmk as rmk,f.validSign as validSign,b.sysVoucherName as sysVoucherName,");
		hql.append(" f.sendMode as sendMode");
		hql.append(" from FcVoucherType f,BtPayType b where f.sysVoucherCode=b.sysVoucherCode and f.sysVoucherCode=:sysVoucherCode and f.isSystem=:isSystem");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sysVoucherCode", sysVoucherCode);
		params.put("isSystem", "1");
		List<FcVoucherTypeVO> fcVoucherTypeVOS = listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
		if(fcVoucherTypeVOS.size()==0){
			return null;
		}
		return fcVoucherTypeVOS.get(0);
	}

	/**
	 * 根据系统结算方式获取对应的初始化的付款方式
	 *
	 * @param
	 * @return
	 */
	public List<FcVoucherTypeVO> getFcVoucherTypeVOBySysVoucherCodeS(String[] sysVoucherCodes) {
		StringBuilder hql = new StringBuilder();
		hql.append("select f.id as id,f.voucherType as voucherType,f.voucherName as voucherName,f.sysVoucherCode as sysVoucherCode,");
		hql.append(" f.cashTransSign as cashTransSign,f.settlType as settlType,f.isElectronPay as isElectronPay,f.isSystem as isSystem,");
		hql.append(" f.isAccount as isAccount,f.isBudget as isBudget,f.rmk as rmk,f.validSign as validSign,b.sysVoucherName as sysVoucherName,");
		hql.append(" f.sendMode as sendMode");
		hql.append(" from FcVoucherType f,BtPayType b where f.sysVoucherCode=b.sysVoucherCode and f.sysVoucherCode in (:sysVoucherCode) and f.isSystem=:isSystem");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sysVoucherCode", sysVoucherCodes);
		params.put("isSystem", "1");
		return listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
	}
	/**
	 * 根据系统结算方式获取有效的付款方式信息
	 *
	 * @param sysVoucherCode
	 * @return
	 */
	public List<FcVoucherTypeVO> listBySysVoucherCode(String sysVoucherCode) {
		StringBuilder hql = new StringBuilder();
		hql.append("select f.id as id,f.voucherType as voucherType,f.voucherName as voucherName,f.sysVoucherCode as sysVoucherCode,");
		hql.append(" f.cashTransSign as cashTransSign,f.settlType as settlType,f.isElectronPay as isElectronPay,f.isSystem as isSystem,");
		hql.append(" f.isAccount as isAccount,f.isBudget as isBudget,f.rmk as rmk,f.validSign as validSign,b.sysVoucherName as sysVoucherName,");
		hql.append(" f.sendMode as sendMode");
		hql.append(" from FcVoucherType f,BtPayType b where f.sysVoucherCode=b.sysVoucherCode and f.sysVoucherCode=:sysVoucherCode and f.validSign=:validSign");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sysVoucherCode", sysVoucherCode);
		params.put("validSign", "1");
		return listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
	}
	/**
	 * <b>功能：</b>查询所有有效供付款方式分配的付款方式<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	public List<FcVoucherTypeDTO> getFcVoucherTypeForAssign() {
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM FcVoucherType WHERE validSign=:validSign  ");
		hql.append("and sysVoucherCode not in ('" + NetbankConstant.PAY_TYPE_01 + "','" + NetbankConstant.PAY_TYPE_10 + "','" + NetbankConstant.PAY_TYPE_48 + "','" + NetbankConstant.PAY_TYPE_37 +
				"','" + NetbankConstant.PAY_TYPE_30 + "','" + NetbankConstant.PAY_TYPE_31 + "','" + NetbankConstant.PAY_TYPE_58 + "','" + NetbankConstant.PAY_TYPE_59 + "')");
		Map<String, Object> params = new HashMap<>();
		params.put("validSign", "1");
		hql.append(" order by sysVoucherCode ");
		List<FcVoucherTypeVO> fcVoucherType = listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
		List<FcVoucherTypeDTO> fcVoucherTypeDTO = BeanUtils.copyListProperties(fcVoucherType, FcVoucherTypeDTO.class);
		return fcVoucherTypeDTO;
	}
	/**
	 * <b>功能：</b>查询所有有效供付款方式分配的付款方式(允许挂预算的)<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	public List<FcVoucherTypeDTO> getVoucherTypeForBudget() {
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM FcVoucherType WHERE validSign=:validSign and isBudget=:isBudget ");
		hql.append("and sysVoucherCode not in ('" + NetbankConstant.PAY_TYPE_01 + "','" + NetbankConstant.PAY_TYPE_10 + "','" + NetbankConstant.PAY_TYPE_48 + "','" + NetbankConstant.PAY_TYPE_37 +
				"','" + NetbankConstant.PAY_TYPE_30 + "','" + NetbankConstant.PAY_TYPE_31 + "','" + NetbankConstant.PAY_TYPE_58 + "','" + NetbankConstant.PAY_TYPE_59 +"')");
		Map<String, Object> params = new HashMap<>();
		params.put("validSign", "1");
		params.put("isBudget", "1");
		hql.append(" order by sysVoucherCode ");
		List<FcVoucherTypeVO> fcVoucherType = listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
		List<FcVoucherTypeDTO> fcVoucherTypeDTO = BeanUtils.copyListProperties(fcVoucherType, FcVoucherTypeDTO.class);
		return fcVoucherTypeDTO;
	}
	/**
	 * 查询所有有效供付款方式分配的付款方式(用于ERP付款转移的)
	 * @return
	 */
	public List<FcVoucherTypeDTO> getVoucherTypeForErp() {
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM FcVoucherType WHERE validSign=:validSign ");
		hql.append("and sysVoucherCode not in ('" + NetbankConstant.PAY_TYPE_01 + "','" + NetbankConstant.PAY_TYPE_10 + "','" + NetbankConstant.PAY_TYPE_37 +
				"','" + NetbankConstant.PAY_TYPE_30 + "','" + NetbankConstant.PAY_TYPE_31 + "','" + NetbankConstant.PAY_TYPE_58 + "','" + NetbankConstant.PAY_TYPE_59 +"')");
		Map<String, Object> params = new HashMap<>();
		params.put("validSign", "1");
		hql.append(" order by sysVoucherCode ");
		List<FcVoucherTypeVO> fcVoucherType = listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
		List<FcVoucherTypeDTO> fcVoucherTypeDTO = BeanUtils.copyListProperties(fcVoucherType, FcVoucherTypeDTO.class);
		return fcVoucherTypeDTO;
	}
	/**
	 * 查询所有有效供付款方式分配的付款方式（内部账号流水查询）
	 * @return
	 */
	public List<FcVoucherTypeDTO> getFcVoucherTypeForReport() {
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM FcVoucherType WHERE validSign=:validSign  ");
		Map<String, Object> params = new HashMap<>();
		params.put("validSign", "1");
		hql.append(" order by sysVoucherCode ");
		List<FcVoucherTypeVO> fcVoucherType = listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
		List<FcVoucherTypeDTO> fcVoucherTypeDTO = BeanUtils.copyListProperties(fcVoucherType, FcVoucherTypeDTO.class);
		return fcVoucherTypeDTO;
	}
	/**
	 * 查询所有有效的结算业务相关的付款方式
	 * @return
	 */
	public List<FcVoucherTypeDTO> getFcVoucherTypeForSettlement() {
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM FcVoucherType WHERE validSign=:validSign  ");
		hql.append("and sysVoucherCode not in ('"
				+ NetbankConstant.PAY_TYPE_30 + "','" + NetbankConstant.PAY_TYPE_31 + "','" + NetbankConstant.PAY_TYPE_58 + "','" + NetbankConstant.PAY_TYPE_59 + "')");
		Map<String, Object> params = new HashMap<>();
		params.put("validSign", "1");
		hql.append(" order by sysVoucherCode ");
		List<FcVoucherTypeVO> fcVoucherType = listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
		List<FcVoucherTypeDTO> fcVoucherTypeDTO = BeanUtils.copyListProperties(fcVoucherType, FcVoucherTypeDTO.class);
		return fcVoucherTypeDTO;
	}
	/**
	 * <b>功能：</b>查询所有有效且生成指令的的付款方式<br>
	 * <b>日期：</b> 2022-07-27 <br>
	 */
	public List<FcVoucherTypeDTO> getValidVoucherTypeForExc() {
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM FcVoucherType WHERE validSign=:validSign  ");
		hql.append("and sysVoucherCode in ('" + NetbankConstant.PAY_TYPE_34 + "','" + NetbankConstant.PAY_TYPE_02 + "','" + NetbankConstant.PAY_TYPE_33 + "','" + NetbankConstant.PAY_TYPE_10 +
				"','" + NetbankConstant.PAY_TYPE_37 + "','" + NetbankConstant.PAY_TYPE_48 + "','" + NetbankConstant.PAY_TYPE_35 + "')");
		Map<String, Object> params = new HashMap<>();
		params.put("validSign", "1");
		hql.append(" order by sysVoucherCode ");
		List<FcVoucherTypeVO> fcVoucherType = listByHQLAliasToBean(hql.toString(), params, FcVoucherTypeVO.class);
		List<FcVoucherTypeDTO> fcVoucherTypeDTO = BeanUtils.copyListProperties(fcVoucherType, FcVoucherTypeDTO.class);
		return fcVoucherTypeDTO;
	}
	public List<FcVoucherTypeDTO> getFcVoucherTypeByIds(List<String> voucherIds) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from fc_voucher_type where 1=1");
		if (CollectionUtils.isNotEmpty(voucherIds)){
			sql.append(" and ").append(getSQLIn(voucherIds, 900, "id"));
		}
		return listBySQLAliasToBean(sql.toString(),null,FcVoucherTypeDTO.class);
	}

	public List<FcVoucherTypeDTO> getFcVoucherTypeLikeName(String voucherName) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select * from fc_voucher_type where voucher_name like:voucherName");
		params.put("voucherName", "%"+voucherName+"%");
		return listBySQLAliasToBean(sql.toString(),params,FcVoucherTypeDTO.class);
	}
}
