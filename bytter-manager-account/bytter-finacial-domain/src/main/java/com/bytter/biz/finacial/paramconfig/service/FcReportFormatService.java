package com.bytter.biz.finacial.paramconfig.service;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.paramconfig.dao.FcReportFormatDao;
import com.bytter.biz.finacial.paramconfig.entity.FcReportFormat;
import com.bytter.biz.finacial.paramconfig.vo.FcReportFormatVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IMultiLangService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
* @Title: Service
* @Description: 格式定义表
* @author onlineGenerator
* @date 2017-08-30 17:58:29
* @version 1.0
*
*/
@Service("fcReportFormatService")
@Transactional
public class FcReportFormatService extends BaseService<FcReportFormat,String> {
	@Autowired
	private FcReportFormatDao fcReportFormatDao;
	@DubboReference
	private IMultiLangService multiLangServiceI;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcReportFormat entity){
		super.deleteEntity(entity);
	}
	
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
 	
	/**
	* 保存
	* @param entity
	*/
 	public void save(FcReportFormat entity){
 		super.saveEntity(entity);
 	}
 	
	/**
	* 更新
	* @param entity
	*/
	public void update(FcReportFormat entity){
		super.updateEntity(entity);
	}
	
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcReportFormat entity){
 		super.saveOrUpdate(entity);
 	}
 	
 	/**
	* 保存
	* @param vo
	*/
 	public void submit(FcReportFormatVO vo){
 		List<FcReportFormat> list = vo.getFormat();
 		fcReportFormatDao.deleteAll(vo.getReportType(),vo.getNetCode());
 		if(!EmptyUtils.isEmptyList(list)){
 			for (int i = 0 ; i < list.size() ;i++) {
 				check(list.get(i) , i);
 			}
 			this.batchSave(list);
 		}
 	}
 	
 	/***
 	 * 报表格式数据校验
 	 */
 	private void check(FcReportFormat format ,int index) {
 		int current = index + 1;
 		String the = multiLangServiceI.getLang("common.table.the");//第
		String no = multiLangServiceI.getLang("table.fcReportFormat.orderNo");//行
 		
 		//行号不能为空
		if(EmptyUtils.isEmpty(format.getLineNo())){
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_REPORT_LINENO_NOTNULL, the +current+ no);
		}
		//行次不能为空
		if(EmptyUtils.isEmpty(format.getOrderNo())){
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_REPORT_ORDERNO_NOTNULL, "第"+current+"行");
		}
		
		//项目名不能为空
		if(EmptyUtils.isEmpty(format.getItem())){
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_ITEM_NOTNULL , the +current+ no);
		}
//		左右不能为空
		if(StringUtils.isNotEmpty(format.getDirection())&&!StringUtils.isNotEmpty(format.getDirection().replace(" ",""))){
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_DIRECTION_NOTNULL , the +current+ no);
		}
	}
	/**
 	 * 复制
 	 */
 	public void copySubmit(FcReportFormatVO format) {
 		List<FcReportFormat> data = getReportFormat(format.getNetCode(),format.getReportType());
 		List<FcReportFormat> list = new ArrayList<FcReportFormat>();
 		if(data != null){
 			for (FcReportFormat fcReportFormat : data) {
 	 			if(fcReportFormat.getNetCode().equals(format.getNewNetCode())){
 	 				throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_NETID_NOTCOPY);
 	 	 		}else{
 	 	 			FcReportFormat  entity = new FcReportFormat();
 	 	 			entity.setReportType(fcReportFormat.getReportType());
 	 	 			entity.setNetCode(format.getNewNetCode());
 	 	 			entity.setLineNo(fcReportFormat.getLineNo());
 	 				entity.setDirection(fcReportFormat.getDirection());
 	 				entity.setOrderNo(fcReportFormat.getOrderNo());
 	 				entity.setItem(fcReportFormat.getItem());
 	 				entity.setRect(fcReportFormat.getRect());
 	 				entity.setSubject(fcReportFormat.getSubject());
 	 				entity.setFormula(fcReportFormat.getFormula());
 	 				entity.setInitYear(fcReportFormat.getInitYear());
 	 				entity.setCreateTime(fcReportFormat.getCreateTime());
 	 				entity.setCreateBy(fcReportFormat.getCreateBy());
					entity.setItemId(fcReportFormat.getItemId());
					list.add(entity);
 	 	 		}
 			}
 		
 			this.batchSave(list);
 		//fcReportFormatDao.copy(format);
 		}
	}
 	
 	
 	@Transactional(readOnly = true)
 	public List<FcReportFormat> getReportFormat(String netCode , String reportType){
 		return fcReportFormatDao.getReportFormat(netCode ,reportType);
 	}
 	
	public BaseDao<FcReportFormat, String> getEntityDao() {
		return fcReportFormatDao;
	}
}
