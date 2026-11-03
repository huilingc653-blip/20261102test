package com.bytter.biz.finacial.api.service;
import java.util.List;
import com.bytter.biz.finacial.api.vo.VoucherInfoVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
public interface IFcAccSummonsService{
	
	/**
	 * 查询内传票凭证列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput);
	/**
	 *查询凭证详细信息
	 * @param vou
	 * @return
	 */
	public List<VoucherInfoVO> getVoucherInfo(VoucherInfoVO vou);
}
