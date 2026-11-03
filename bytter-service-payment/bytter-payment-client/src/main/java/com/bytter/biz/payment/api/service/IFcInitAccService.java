package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.FcAccInitCheckBeanDTO;
import com.bytter.biz.payment.api.dto.FcDayCloseDTO;
import com.bytter.biz.payment.api.dto.FcInitAccDTO;
import com.bytter.biz.payment.api.dto.FcInitSubjectDTO;
import java.util.List;
/**
 * @author YonJar
 * @date 2023/4/27.
 * @description
 */
public interface IFcInitAccService {
    /**
     * TODO SQL 参考 FcControlCheckDao.findNoCheckAccInit(FcDayCloseDTO dayClose)
     * @param dayClose
     * @return
     */
    List<FcAccInitCheckBeanDTO> findNoCheckAccInit(FcDayCloseDTO dayClose);
    /**
     * @param netId
     * @return
     * TODO 参考 SQL
     * sql.append(" Select a.Cur_id as curId, ");
     * 		sql.append(" a.Subject_id as subjectId, ");
     * 		sql.append(" Sum(a.Year_Balance) As yearBalance, ");
     * 		sql.append(" Sum(a.Year_Total) As yearTotal ");
     * 		sql.append(" From Fc_Init_Acc a ");
     * 		sql.append(" Where a.status = 95 ");
     * 		sql.append(" And a.net_id = :netId  ");
     * 		sql.append(" Group By a.Cur_id, a.Subject_id ");
     *
     * 		Map<String, Object> params = new HashMap<String, Object>();
     * 		params.put("netId", closeDTO.getNetCode());
     *
     * 		return listBySQLAliasToBean(sql.toString(), params, FcInitSubjectDTO.class);
     */
    List<FcInitSubjectDTO> getInitSubjectDTO(String netId);

    /**
     * 功能介绍 : 查询网点初始化记录。<br>
     * <b>日期：</b>2017年7月18日<br>
     * @version 1.0
     * @author liyh
     */
    List<FcInitAccDTO> queryInitAccByNetId(String netId);
}
