package com.bytter.biz.finacial.closedeal.service.impl;
import com.bytter.biz.finacial.closedeal.entity.FcDayClose;
import com.bytter.biz.finacial.closedeal.service.FcDayCloseService;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.Date;
/**
 * 日结时间接口实现
 * @author liyh
 *
 */
@Service("fcDayCloseServiceImpl")
@Transactional
@DubboService
public class FcDayCloseServiceImpl implements IFcDayCloseService {
	
	@Autowired
	private FcDayCloseService closeService;
	@Override
    public void initNetDayClose(String netId, Date closeDate, String userId) {
		closeService.initNetCloseDate(netId, closeDate, userId);
	}
	
	@Override
	public void initNetDayClose(String netId, String userId) {
		initNetDayClose(netId, null, userId);
	}
	@Override
	public Date getDayCloseFinalDate(String netId) {
		FcDayCloseDTO dayCloseDTO = closeService.getDayCloseFinalDate(netId);
		return dayCloseDTO.getCloseDate();
	}
    @Override
    public void initPoolNetCloseDate(String netId, String userId) {
        closeService.initPoolNetCloseDate(netId, null, userId);
    }
    @Override
    public Date getPoolDayCloseDate(String netId) {
		FcDayCloseDTO dayCloseDTO = closeService.getPoolDayCloseFinalDate(netId);
		return dayCloseDTO.getCloseDate();
	}
	@Override
	public Calendar getMonthCloseDate(String netId) {
		FcDayClose fcDayClose = closeService.getMonthCloseDate(netId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(fcDayClose.getCloseDate());
		//待月结月份
		cal.add(Calendar.MONTH, 1);//默认下一个月为月结日期
		return cal;
	}
}
