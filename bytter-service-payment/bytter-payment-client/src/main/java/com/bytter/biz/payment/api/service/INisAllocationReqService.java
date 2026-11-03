package com.bytter.biz.payment.api.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import java.util.List;
/**
 * 付款接口类
 * @author wangzx
 * @since 2018-01-12 09:49:44
 */
public interface INisAllocationReqService {
    /**
     * 头寸调拨生成（外模块生成头寸调拨单据）
     * @param entity 资金调拨单
	 * @param user 用户
	 */
    void saveNisAllocationReq(NisAllocationReqDTO entity,ICurrentUser user);
    /**
     * 修改头寸调拨单据
     * @param nisAllocationReqDTO 资金调拨单
     * @param user 用户
     */
    void updateNisAllocationReq(NisAllocationReqDTO nisAllocationReqDTO, ICurrentUser user);
    /**
     * 更新头寸调拨单据（银行借款反写接口）
     * @param erpId ERP单号
     * @param status 状态
     * @param msg 信息
	 * @param user 用户
	 */
    void updateNisAllocationReq(String erpId, Integer status, String msg, ICurrentUser user);
    /**
     * 根据外部单号查找资金调拨单据
     * @param erpId ERP单号
     * @return 资金调拨单据
     */
    NisAllocationReqDTO getNisAllocationReqByErpId(String erpId);
    /**
     * 根据id删除头寸调拨单据
     * @param erpId  ERP单号
     */
    void deleteNisAllocationReq(String erpId);
	/**
	 * 手工记账查询
	 * @param dataTablesInput 表格封装对象
	 * @param user 用户
	 * @return 记账信息
	 */
	DataTablesOutput getDataTables(DataTablesInput dataTablesInput, ICurrentUser user);
	/**
	 * 根据单号查找资金调拨单据
	 * @param billNo 业务单号
	 * @return 资金调拨单
	 */
	NisAllocationReqDTO getNisAllocationReqByBillNo(String billNo);
	/**
	 * 判断是否存在在途数据
	 * @param corpId 单位ID
	 * @return true/false
	 */
	boolean isExistNotFinishedNisAllocationReq(String corpId);
	/**
	 * 待记账查询
	 * */
	List<NisAllocationReqDTO> queryTallyNisAllReq(Integer menuType);

	List<NisAllocationReqDTO> getRepayBisExc(NisAllocationReqDTO nisAllocationReqDTO);
}
