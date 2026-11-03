package com.bytter.check.api;

import com.bytter.check.vo.ErpTemplateStoreVO;
import com.bytter.check.vo.ErpTemplateVO;

public interface ITemplateService {
	
	public ErpTemplateVO getErpTemplate(String templateCode);
	
	public void save(ErpTemplateStoreVO store);
}
