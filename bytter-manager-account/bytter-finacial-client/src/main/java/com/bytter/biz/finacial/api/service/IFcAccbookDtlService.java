package com.bytter.biz.finacial.api.service;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import java.util.List;
/**
 * @author YonJar
 * @date 2023/4/23.
 * @description
 */
public interface IFcAccbookDtlService {
    List<FcAccbookDtlDTO> getPageDtlsByBookId(String bookId);
    /**
     * 根据全局id获取流水数据
     *
     * @param receiveBusGlobalId 业务全局id
     * @return 流水数据集合
     */
    List<FcAccbookDtlDTO> getBookDtlsByBusGlobalId(String receiveBusGlobalId);
}
