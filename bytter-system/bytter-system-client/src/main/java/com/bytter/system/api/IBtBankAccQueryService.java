package com.bytter.system.api;
import java.util.List;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.vo.BtBankAccQueryVO;
import com.bytter.system.api.vo.BtBankAccVO;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
public interface IBtBankAccQueryService{
	DataTablesOutput<BtBankAccVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user);

	DataTablesOutput<BtBankAccVO> getDataTablesUnionWithAllCurId(DataTablesInput dataTablesInput, ICurrentUser user);
	/**
	 * 组装开户单位的条件SQL子句
	 *
	 * @param dataTablesInput 表单数据
	 * @return 条件SQL子句
	 */
	String getBelongingCorpIdsInSql(DataTablesInput dataTablesInput);
	/**
	 * Created by chenlj 2018/4/19 基础平台权限问题
	 *
	 * @param dataTablesInput
	 * @param user
	 * @return
	 */
	DataTablesOutput<BtBankAccVO> getDataTablesUnionSYS(DataTablesInput dataTablesInput, ICurrentUser user);
	
	
	DataTablesOutput getNotDeleteAndValid(DataTablesInput input, ICurrentUser user);
	/**
	 * 重构列表展示，一个账号成一行
	 * 多币别合并成一个字段展示
	 *
	 * @param output
	 * @return
	 */
	DataTablesOutput setRefresh(DataTablesOutput output);
	/**
	 * 根据账号查询账号对应的流水信息（开户、变更、注销、冻结、解冻）
	 *
	 * @param bankAcc
	 * @return
	 */
	List<BtBankAccQueryVO> listReqByBankAcc(String bankAcc);

	/**
	 * 添加附件
	 * @param id 银行账号id
	 * @param attachmentId 附件id
	 */
	void addAttachment(String id,String attachmentId);

	List<BtBankAccVO> getAllBankAccToStatistics(List<String> corpIds, String bankTypeId, String bankAcc, String status, String bindingBankAcc);
}
