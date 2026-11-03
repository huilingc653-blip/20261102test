package com.bytter.biz.finacial.outaccbook.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.closedeal.vo.FcOutSubjectDayBean;
import com.bytter.biz.finacial.outaccbook.dao.FcOutSubjectDayDao;
import com.bytter.biz.finacial.outaccbook.entity.FcOutSubjectDay;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
/**
* @Title: Service
* @Description: FC_OUT_SUBJECT_DAY
* @author onlineGenerator
* @date 2017-07-19 16:03:41
* @version 1.0
*
*/
@Service("fcOutSubjectDayService")
@Transactional
public class FcOutSubjectDayService extends BaseService<FcOutSubjectDay,String> {
	@Autowired
	private FcOutSubjectDayDao fcOutSubjectDayDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcOutSubjectDay entity){
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
 	public void save(FcOutSubjectDay entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcOutSubjectDay entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcOutSubjectDay entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcOutSubjectDay, String> getEntityDao() {
		return fcOutSubjectDayDao;
	}
	
	/**
	 * 功能介绍 : 保存科目日结单数据。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void saveOutSubjectDayBeans(List<FcOutSubjectDayBean> beans) {
		List<FcOutSubjectDay> list = new ArrayList<FcOutSubjectDay>();
		if (null != beans) {
			list = BeanUtils.copyListProperties(beans, FcOutSubjectDay.class);
			batchSave(list);
		}
	}
}
