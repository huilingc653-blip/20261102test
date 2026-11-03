package com.bytter.subject.service.impl;
import com.bytter.subject.service.FcSubjectDayService;
import com.bytter.system.api.IFcSubjectDayService;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import com.bytter.system.api.dto.FcSubjectDayBeanDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author YonJar
 * @date 2023/4/29.
 * @description
 */
@Service("fcSubjectDayServiceI")
@Transactional
@DubboService
public class FcSubjectDayServiceImpl implements IFcSubjectDayService {
    @Autowired
    private FcSubjectDayService fcSubjectDayService;
    @Override
    public List<FcSubjectDayBeanDTO> querySetMonthSubDay(FcMonthCloseDTO closeDTO) {
        return fcSubjectDayService.querySetMonthSubDay(closeDTO);
    }
    @Override
    public void saveSubjectDayBeans(List<FcSubjectDayBeanDTO> beans) {
        fcSubjectDayService.saveSubjectDayBeans(beans);
    }

    @Override
    public boolean deleteSubjectDay(FcDayCloseDTO fcDayCloseDTO) {
        return fcSubjectDayService.deleteSubjectDay(fcDayCloseDTO);
    }

    @Override
    public boolean isIncomBlanceZero(FcMonthCloseDTO closeDTO) {
        return fcSubjectDayService.isIncomBlanceZero(closeDTO);
    }
}
