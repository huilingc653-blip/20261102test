package com.bytter.ras.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.ras.dao.RasImportConfDtlDao;
import com.bytter.ras.entity.RasImportConfDtl;
import com.bytter.ras.vo.RasImportConfDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;

@Service("rasImportConfDtlService")
@Transactional
public class RasImportConfDtlService extends BaseService<RasImportConfDtl, String>
implements IExcelVerifyHandler<RasImportConfDtlVO>{

	@Autowired
	private RasImportConfDtlDao confDtlDao;
	
	
	/**
     * 根据ids批量修改营收数据明细表数据状态
     * @param params
     * @return
     */
    public void batchUpdate(String ircId){
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("ircId", ircId);
    	confDtlDao.batchUpdate(params);
    }
	
	@Override
	public ExcelVerifyHanlderResult verifyHandler(RasImportConfDtlVO arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void verifyHandler(List<RasImportConfDtlVO> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseDao<RasImportConfDtl, String> getEntityDao() {
		// TODO Auto-generated method stub
		return null;
	}

}
