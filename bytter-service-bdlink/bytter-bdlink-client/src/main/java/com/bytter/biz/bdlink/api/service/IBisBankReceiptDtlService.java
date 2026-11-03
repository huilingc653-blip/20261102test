package com.bytter.biz.bdlink.api.service;
import com.bytter.biz.bdlink.api.dto.BisBankReceiptDtlDTO;
import com.bytter.biz.bdlink.api.dto.BisBankReceiptPdfDTO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * qijh
 */
public interface IBisBankReceiptDtlService {
    DataTablesOutput<BisBankReceiptDtlVO> getDataTablesUnion(DataTablesInput input, StringBuilder commonSql);
    BisBankReceiptDtlVO getByIdUnion(String id);
    /**
     * 更新打印次数
     *
     * @param id
     */
    void updatePrint(String id);
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    BisBankReceiptDtlVO getByIdUnionV2(String id);
    /**
     * 自动迁移数据调用方法
     *
     * @param date
     * @return
     */
    DataTablesOutput getByTime(DataTablesInput input, Date date);
    /**
     * 自动迁移数据调用方法 获取需求迁移的总数
     *
     * @param date
     * @return
     */
    BigDecimal getByTimeCount(Date date);
    List<BisBankReceiptDtlVO> listByIds(String ids);
    /**
     * 外系统接口保存电子回单
     *
     * @param bisBankReceiptDtlDTO
     * @param bisBankReceiptPdfDTO
     */
    void saveBankBill(BisBankReceiptDtlDTO bisBankReceiptDtlDTO, BisBankReceiptPdfDTO bisBankReceiptPdfDTO);
    /**
     * 根据id得到电子回单
     *
     * @param id
     * @return
     */
    BisBankReceiptDtlDTO getById(String id);

    /**
     * 根据id得到电子回单
     * @param id
     * @return
     */
    void initImg(String id, HttpServletRequest request, HttpServletResponse response);


    /**
     * 根据付款表id得到电子回单表ID集合
     * @param payId
     * @return 回单表ID集合
     */
    List<String> getIdsByPayId(String payId);
}
