package com.bytter.biz.finacial.inneraccount.service;
import java.util.List;
import com.bytter.biz.finacial.api.vo.FcAccSummonsVO;
import com.bytter.biz.finacial.api.vo.VoucherInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.inneraccount.dao.FcAccSummonsDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
@Service("fcAccSummonsService")
@Transactional
public class FcAccSummonsService extends BaseService<FcAccSummonsVO, String>{
	@Autowired
	private FcAccSummonsDao fcAccSummonsDao;
	@Override
	public BaseDao<FcAccSummonsVO, String> getEntityDao() {
		return fcAccSummonsDao;
	}
	/**
	 * 查询内传票凭证列表
	 * @param dataTablesInput
	 * @return
	 * 没有被调用，注释
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		return fcAccSummonsDao.getDataTablesUnion(dataTablesInput);
	}
	/**
	 *查询凭证详细信息
	 * @param vou
	 * @return
	 * 没有被调用，注释
	 */
	public List<VoucherInfoVO> getVoucherInfo(VoucherInfoVO vou){
		List<VoucherInfoVO> list = fcAccSummonsDao.getInfo(vou);
		return list;
	}
}
