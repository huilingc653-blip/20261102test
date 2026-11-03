package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import com.bytter.system.api.IExcelExportService;
/**
 * excel导出服务类
 * @author zhenghj
 *
 */
@Service("abExcelExportService")
@Transactional
public class AbExcelExportService extends BaseService<String, AbAccrecords> {
	
	// @DubboReference(check = false)
	// private IExcelExportService excelExportService;
	//
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	// public void exportExcel(Class classVo, String serviceName, Object dataParam, ExportParams params, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response){
	// 	excelExportService.exportExcel(classVo,serviceName,dataParam,params,modelMap,request,response);
	// }
}
