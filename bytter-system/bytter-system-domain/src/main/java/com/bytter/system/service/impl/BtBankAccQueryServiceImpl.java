package com.bytter.system.service.impl;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IBtBankAccQueryService;
import com.bytter.system.api.vo.BtBankAccQueryVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.service.BtBankAccQueryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service("btBankAccQueryServiceI")
@DubboService
@Transactional
public class BtBankAccQueryServiceImpl implements IBtBankAccQueryService{
	
	@Autowired
	private BtBankAccQueryService btBankAccQueryService;
	@Override
	public DataTablesOutput<BtBankAccVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		return btBankAccQueryService.getDataTablesUnion(dataTablesInput, user);
	}

	@Override
	public DataTablesOutput<BtBankAccVO> getDataTablesUnionWithAllCurId(DataTablesInput dataTablesInput, ICurrentUser user) {
		return btBankAccQueryService.getDataTablesUnionWithAllCurId(dataTablesInput, user);
	}
	@Override
	public String getBelongingCorpIdsInSql(DataTablesInput dataTablesInput) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DataTablesOutput<BtBankAccVO> getDataTablesUnionSYS(DataTablesInput dataTablesInput, ICurrentUser user) {
		return btBankAccQueryService.getDataTablesUnionSYS(dataTablesInput, user);
	}
	@Override
	public DataTablesOutput getNotDeleteAndValid(DataTablesInput input, ICurrentUser user) {
		return btBankAccQueryService.getNotDeleteAndValid(input, user);
	}
	@Override
	public DataTablesOutput setRefresh(DataTablesOutput output) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<BtBankAccQueryVO> listReqByBankAcc(String bankAcc) {
		List<com.bytter.system.vo.BtBankAccQueryVO> list = btBankAccQueryService.listReqByBankAcc(bankAcc);
		List<com.bytter.system.api.vo.BtBankAccQueryVO> listVO = BeanUtils.copyListProperties(list, com.bytter.system.api.vo.BtBankAccQueryVO.class);
		return listVO;
	}


	@Override
	public void addAttachment(String id, String attachmentId) {
		btBankAccQueryService.addAttachment(id, attachmentId);
	}

	@Override
	public List<BtBankAccVO> getAllBankAccToStatistics(List<String> corpIds, String bankTypeId, String bankAcc, String status, String bindingBankAcc) {
		return btBankAccQueryService.getAllBankAccToStatistics(corpIds, bankTypeId, bankAcc, status, bindingBankAcc);
	}
}
