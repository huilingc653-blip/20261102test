package com.bytter.system.service.impl;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IBtBankAccMaintainService;
import com.bytter.system.service.BtBankAccMaintainService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("btBankAccMaintainServiceI")
@DubboService
public class BtBankAccMaintainServiceImpl implements IBtBankAccMaintainService{
	
	@Autowired
	BtBankAccMaintainService btBankAccMaintainService;
	@Override
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		return btBankAccMaintainService.getDataTablesUnion(dataTablesInput, user);
	}
	@Override
	public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, ICurrentUser user) {
		return btBankAccMaintainService.getDataTablesUnionForPool(dataTablesInput, user);
	}
	@Override
	public DataTablesOutput dataTableForCounterAcc(DataTablesInput dataTablesInput, ICurrentUser user, String corpId,String id) {
		return btBankAccMaintainService.dataTableForCounterAcc(dataTablesInput, user, corpId,id);
	}
	@Override
	public DataTablesOutput dataTableForCounterPoolAcc(DataTablesInput dataTablesInput, ICurrentUser user,
			String corpId) {
		return btBankAccMaintainService.dataTableForCounterPoolAcc(dataTablesInput, user, corpId);
	}
	@Override
	public void toAdd(String id, String blnctrAcc, String flag, String curId) {
		btBankAccMaintainService.toAdd(id, blnctrAcc, flag, curId);
	}
	@Override
	public void toDel(String id, String flag, String curId) {
		btBankAccMaintainService.toDel(id, flag, curId);
	}
}
