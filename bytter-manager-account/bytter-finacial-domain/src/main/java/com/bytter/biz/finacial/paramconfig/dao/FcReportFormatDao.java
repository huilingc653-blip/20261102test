package com.bytter.biz.finacial.paramconfig.dao;
import com.bytter.biz.finacial.paramconfig.entity.FcReportFormat;
import com.bytter.biz.finacial.paramconfig.vo.FcReportFormatVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 格式定义表
* @author onlineGenerator
* @date 2017-08-30 17:58:29
* @version 1.0
*
*/
@Repository
public class FcReportFormatDao extends BaseDao<FcReportFormat,String>{
	/**
	 * 根据种类删除报表格式
	 * */
	public void deleteAll(String reportType,String netCode){
		Map<String, Object> params = new HashMap<String, Object>(); 	
		String sql = "delete from fc_report_format where report_type=:reportType and net_code=:netCode";
		params.put("reportType", reportType);
		params.put("netCode", netCode);
		createSQLQuery(sql,params).executeUpdate();
	}
	public List<FcReportFormat> getReportFormat(String netCode , String reportType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select * from fc_report_format f where f.report_type=:reportType and f.net_Code =:netCode order by f.direction,f.line_no ASC ";
		params.put("reportType", reportType);
		params.put("netCode", netCode);
		return listBySQLAliasToBean(sql.toString(), params, FcReportFormat.class);
	}
	/*****
 	 * 复制
 	 */
	public void copy(FcReportFormatVO format) {
		Map<String, Object> params = new HashMap<String, Object>(); 	
		String sql = "update fc_report_format f set f.net_code=:newNetCode where f.net_code =:netCode and f.report_Type =:reportType";
		params.put("newNetCode", format.getNewNetCode());
		params.put("netCode", format.getNetCode());
		params.put("reportType", format.getReportType());
		createSQLQuery(sql,params).executeUpdate();
	}
}
