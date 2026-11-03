package com.bytter.bill.domain.billbank.pool.dao;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolBail;
import com.bytter.bill.domain.billbank.pool.vo.PoolBailVO;
import com.bytter.bill.domain.billbank.dao.BaseDao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <br>
 * <b>功能：</b>保证金操作DAO<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-19 <br>
 */
@SuppressWarnings("rawtypes")
@Repository("billPoolBailImpDao")
public class BillPoolBailImpDao extends BaseDao {

	@DubboReference(check = false)
	private IUserService userService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;

	private static final long serialVersionUID = 1L;
	
	/**
	 * 根据保证金账户获取保证金账户信息表
	 * @param bankAcc 保证金账户
	 * @return
	 */
	public AbPoolBail getBillPoolBailByBankAcc(String bankAcc){
		if(StrUtil.isEmpty(bankAcc)){
			return null;
		}
		String hql="from AbPoolBail where bankAcc=:bankAcc order by createTime desc";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		List list=this.listByHQL(hql, params);
		if(list!=null&&list.size()>0){
			return (AbPoolBail) list.get(0);
		}
		return null;
	}
	
	/**
	 * <b>功能：</b>根据查询参数获取保证金数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param busId 业务ID
	 * @return
	 */
	public List<PoolBailVO> queryBailInfo(String type, String busId){
		return queryBailInfo(type, null, null, null, busId);
	}
	
	/**
	 * <b>功能：</b>根据查询参数获取保证金数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param optStatus 指令状态
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<PoolBailVO> queryBailInfo(String type,String optStatus,String bifCode,String reCode){
		return queryBailInfo(type, optStatus, bifCode, reCode, null);
	}
	/**
	 * <b>功能：</b>根据查询参数获取保证金数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 存取类型1存2取
	 * @param bifCode 银行接口代码
	 * @param optStatus 指令状态
	 * @param reCode 银行返回代码
	 * @param busId 业务ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PoolBailVO> queryBailInfo(String type,String optStatus,String bifCode,String reCode,String busId){
		List<PoolBailVO> list = new ArrayList<PoolBailVO>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("select d.id            as busId, ");
		sqlselect.append("pi.bif_code     as bifCode, ");
		sqlselect.append("be.apply_Id     as applyId, ");
		sqlselect.append("be.bank_Id      as bankId, ");
		sqlselect.append("b.corp_id       as corpId, ");
		sqlselect.append("p.id            as bPId, ");
		sqlselect.append("p.bp_code       as poolNo, ");
		sqlselect.append("p.bp_name       as poolName, ");
		sqlselect.append("s.orig_code     as bankOrgCode, ");
		sqlselect.append("s.orig_name     as bankOrgName, ");
		sqlselect.append("d.type, ");
		sqlselect.append("d.app_date      as appDate, ");
		sqlselect.append("d.money, ");
		sqlselect.append("d.BILL_ID		  as billId, ");
		sqlselect.append("b.bank_acc      as bankAcc, ");
		// sqlselect.append("ba.acc_name     as accName, ");
		// sqlselect.append("ba.bank_code    as bankNo, ");
		// sqlselect.append("ba.bank_name    as bankName, ");
		sqlselect.append("d.opp_acc       as oppBankAcc, ");
		sqlselect.append("d.opp_acc_name  as oppAccName, ");
		sqlselect.append("d.opp_bank_no   as oppBankNo, ");
		sqlselect.append("d.opp_bank_name as oppBankName, ");
		sqlselect.append("d.create_id     as opId, ");
		// sqlselect.append("su.username     as opName, ");
		sqlselect.append("d.opt_status    as optstatus ");
		sqlselect.append("from Ab_Pool_Bail_Dtl d ");
		sqlselect.append("inner join ab_pool_bail b ");
		sqlselect.append("  on b.id = d.bail_id ");
		sqlselect.append("left join ab_pool p ");
		sqlselect.append("  on b.bp_id = p.id ");
		sqlselect.append("left join ab_poolif pi ");
		sqlselect.append("  on pi.id = p.pif_id ");
		// sqlselect.append("left join bt_bank_acc ba ");
		// sqlselect.append("  on ba.bank_acc = b.bank_acc ");
    	// sqlselect.append("  and ba.valid_sign = '1' ");
	    sqlselect.append("left join ab_pool_sign_info s ");
	    sqlselect.append("  on s.corp_id = b.corp_id and s.bp_id=p.id ");
	    sqlselect.append("left join ab_pool_bank_exc be ");
	    sqlselect.append("  on be.id = d.id ");
	    // sqlselect.append("left join sys_user su ");
	    // sqlselect.append("  on su.id = d.create_id ");
	    sqlselect.append("where d.type=:type ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		if(StrUtil.isNotEmpty(bifCode)){
			sqlselect.append("and pi.bif_code = :bifCode ");
			params.put("bifCode", bifCode);
		}
		if(StrUtil.isNotEmpty(optStatus)){
			sqlselect.append("and d.opt_status=:optStatus  ");
			params.put("optStatus", optStatus);
		}
		if(StrUtil.isNotEmpty(reCode)){
			sqlselect.append("and (be.re_code is null or be.re_code=:reCode) ");
			params.put("reCode", reCode);
		}
		if(StrUtil.isNotEmpty(busId)){
			sqlselect.append("and d.id=:busId  ");
			params.put("busId", busId);
		}
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), params);
		list = BeanUtils.copyMapListProperties(mapList, PoolBailVO.class);
		if (ObjectUtil.isNotEmpty(list)) {
			List<String> userIdList = list.stream().map(PoolBailVO::getOpId).distinct().collect(Collectors.toList());
			List<String> bankAccList = list.stream().map(PoolBailVO::getBankAcc).distinct().collect(Collectors.toList());
			List<UserDTO> userDTOList = userService.listByUserIds(userIdList);
			Map<String, UserDTO> userMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, user -> user));
			Map<String, BtBankAccDTO> bankAccMap = btBankAccService.getBankAccInfoByBankAccList(bankAccList);
			list.forEach(item -> {
				Optional.ofNullable(item.getOpId()).flatMap(userId -> Optional.ofNullable(userMap)
								.flatMap(listUser -> Optional.ofNullable(userMap.get(userId))))
						.ifPresent(user -> item.setOpName(user.getUsername()));
				Optional.ofNullable(item.getBankAcc()).flatMap(bankAcc -> Optional.ofNullable(bankAccMap)
								.flatMap(listMap -> Optional.ofNullable(bankAccMap.get(bankAcc))))
						.ifPresent(bank -> {
							item.setAccName(bank.getAccName());
							item.setBankNo(bank.getBankCode());
							item.setBankName(bank.getBankName());
						});
			});
		}
		return list;
	}
	
	/**
	 * 根据业务id修改指令状态
	 * @param type 业务类型
	 * @param optStatus
	 * @param billId 外围系统流水号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int updateBailOptStatusByBusId(String type,String busId,String optStatus,String returnMsg,String billId){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("update Ab_Pool_Bail_Dtl set opt_status=:optStatus,return_msg=:returnMsg ");
		if(StrUtil.isNotEmpty(billId)){
			sql.append(",bill_Id=:billId  ");
			params.put("billId", billId);
		}
		sql.append(" where id=:busId and type=:type ");
		params.put("type", type);
		params.put("optStatus", optStatus);
		params.put("busId", busId);
		params.put("returnMsg", returnMsg);
		return this.batchSQLExecute(sql.toString(), params);
	}
}
