package com.bytter.bill.api.bill.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import java.util.Map;
/**
 * 凭证套打回调接口
 *
 * @author zhengg
 */
public interface IVoucherPrintService {
    /**
     * 打印完成回调方法
     *
     * @param dataId 打印数据唯一ID
     * @param vouNo  占用凭证号
     */
    void printComplete(String dataId, String vouNo, ICurrentUser userVo);
    /**
     * 从票据模块获取承兑汇票商票的打印数据。
     *
     * @param input      查询条件。
     * @param listFields 查询字段。
     * @param where      查询条件（单位编号）。
     * @param params     查询参数值。
     * @param isOrder    是否需要排序。
     * @return 承兑汇票商票的打印数据。
     */
    DataTablesOutput<Map<String, Object>> listBvPrintVouOp(DataTablesInput input, String listFields,
                                                           String where, Map<String, Object> params, boolean isOrder);

    Map<String,Object> queryBvPrintVouOpById(String listFields, String dataId, String tableName);
}
