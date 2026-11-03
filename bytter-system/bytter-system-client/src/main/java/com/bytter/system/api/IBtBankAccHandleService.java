package com.bytter.system.api;
import java.util.List;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
public interface IBtBankAccHandleService {
	DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String appSql,String accSql, String busType);
	List<BtBankAccVO> getBankaccList();
	/**
	 * 根据银行接口代码得到银行账号，用与银企直联
	 * BT_BANK_ACC table列表
	 *
	 * @return
	 */
	DataTablesOutput getDataTableByBifCode(DataTablesInput dataTablesInput, String joinSql);
	/**
	 * 查询账号是否已发生业务
	 *
	 * @param bankAcc
	 * @return
	 */
	boolean checkQuoteBusiness(String bankAcc);
	/**
	 * 查询账号是否存在银行账户父子关系
	 *
	 * @param id
	 * @return
	 */
	boolean checkQuotePrentBankAcc(String id);
	/**
	 * 查询账号是否存在内部账户父子关系
	 *
	 * @param id
	 * @return
	 */
	boolean checkQuoteFcAcc(String id);
	/**
	 * 查询账号是否作为实体账号被引用
	 *
	 * @param id
	 * @return
	 */
	boolean checkQuoteEntityBankAcc(String id);
	/**
	 * 是否有父账号
	 *
	 * @param id
	 * @return
	 */
	boolean isHasParentAccById(String id);
	/**
	 * 是否有子账号
	 *
	 * @param id
	 * @return
	 */
	boolean isHasChildAccById(String id);
	/**
	 * 根据账户定义的年检到期日获取到期时间内的
	 *
	 * @author qinl
	 * @date 2019-07-04
	 * @param searchDate [账户年检到期提醒提前天数]
	 * @return 年检到期的账户集合
	 */
	List<BtBankAccDTO> listByExpireDate(String searchDate);
	List<BtBankAccDTO> listByEndDate(String AccType,String searchDate);
	DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String joinSql);
}
