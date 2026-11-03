package com.bytter.check.api;
import com.bytter.check.dto.FcCompareAccDTO;
import java.util.Date;
import java.util.List;
public interface IFcCompareErpService {
    /**
     * 根据开始时间，结束时间查询对账信息
     * @param tallyStartDate
     * @param tallyEndDate
     * @return
     */
    List<FcCompareAccDTO>  listForEBill(Date tallyStartDate, Date tallyEndDate);
}
