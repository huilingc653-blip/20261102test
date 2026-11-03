package com.bytter.system.api;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import com.bytter.system.api.dto.FcSubjectDayBeanDTO;
import java.util.List;
/**
 * @author YonJar
 * @date 2023/4/29.
 * @description
 */
public interface IFcSubjectDayService {
    List<FcSubjectDayBeanDTO> querySetMonthSubDay(FcMonthCloseDTO closeDTO);
    void saveSubjectDayBeans(List<FcSubjectDayBeanDTO> beans);

    /**
     * 删除科目日结数据
     */
    boolean deleteSubjectDay(FcDayCloseDTO fcDayCloseDTO);

    /**
     * 功能介绍 : 是否存在损益类科目不为零。<br>
     * <b>日期：</b>2017年7月17日<br>
     *
     * @version 1.0
     * @author liyh
     */
    boolean isIncomBlanceZero(FcMonthCloseDTO closeDTO);
}
