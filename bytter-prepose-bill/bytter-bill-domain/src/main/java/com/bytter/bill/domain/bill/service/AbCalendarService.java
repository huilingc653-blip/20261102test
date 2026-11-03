package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.ICalendarService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
/**
 * 日历工作日接口服务类
 * @author zhenghj
 *
 */
@Service("abCalendarService")
@Transactional
public class AbCalendarService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private ICalendarService calendarService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 根据时间查询如果没有则延顺到下一个工作日or非工作日
	 * @param time
	 * @param status true工作日 false非工作日
	 * @return Date
	 */
	@Transactional(readOnly = true)
	public Date checkWorkDay(Object time, boolean status){
		return calendarService.checkWorkDay(time,status);
	}
	/**
	 * 根据传递的日期获取后面指定天数工作日的日期
	 * @param date 开始时间
	 * @param workdayNum 工作日天数
	 * @return 指定天数工作日的日期
	 */
	@Transactional(readOnly = true)
	public Date getWorkDay(Date date,int workdayNum){
		return calendarService.getWorkDay(date,workdayNum);
	}
}
