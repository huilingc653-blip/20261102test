package com.bytter.biz.receipt.service.impl;
import com.bytter.biz.bdlink.api.dto.BisBankReceiptDtlDTO;
import com.bytter.biz.bdlink.api.dto.BisBankReceiptPdfDTO;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlService;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.receipt.entity.BisBankReceiptDtl;
import com.bytter.biz.receipt.entity.BisBankReceiptPdf;
import com.bytter.biz.receipt.service.BisBankReceiptDtlService;
import com.bytter.biz.receipt.service.BisBankReceiptPdfService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * add by qijh
 */
@Service("bisBankReceiptDtlServiceImpl")
@DubboService
public class BisBankReceiptDtlServiceImpl implements IBisBankReceiptDtlService {
    @Autowired
    private BisBankReceiptDtlService bisBankReceiptDtlService;
    @Autowired
    private BisBankReceiptPdfService bisBankReceiptPdfService;
    public DataTablesOutput<BisBankReceiptDtlVO> getDataTablesUnion(DataTablesInput input, StringBuilder commonSql) {
        return bisBankReceiptDtlService.getDataTablesUnion(input, commonSql);
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    public BisBankReceiptDtlVO getByIdUnion(String id) {
        return this.bisBankReceiptDtlService.getByIdUnion(id);
    }
    /**
     * 更新打印次数
     *
     * @param id
     */
    public void updatePrint(String id) {
        this.bisBankReceiptDtlService.updatePrint(id);
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    public BisBankReceiptDtlVO getByIdUnionV2(String id) {
        return bisBankReceiptDtlService.getByIdUnionV2(id);
    }
    /**
     * 自动迁移数据调用方法
     *
     * @param date
     * @return
     */
    public DataTablesOutput getByTime(DataTablesInput input, Date date) {
        return this.bisBankReceiptDtlService.getByTime(input, date);
    }
    /**
     * 自动迁移数据调用方法 获取需求迁移的总数
     *
     * @param date
     * @return
     */
    public BigDecimal getByTimeCount(Date date) {
        return this.bisBankReceiptDtlService.getByTimeCount(date);
    }
    public List<BisBankReceiptDtlVO> listByIds(String ids) {
        return bisBankReceiptDtlService.listByIds(ids);
    }
    /**
     * 外接口保存电子回单
     *
     * @param bisBankReceiptDtlDTO
     * @param bisBankReceiptPdfDTO
     */
    @Override
    public void saveBankBill(BisBankReceiptDtlDTO bisBankReceiptDtlDTO, BisBankReceiptPdfDTO bisBankReceiptPdfDTO) {
        BisBankReceiptDtl bisBankReceiptDtl = new BisBankReceiptDtl();
        BeanUtils.copyProperties(bisBankReceiptDtlDTO, bisBankReceiptDtl);
        bisBankReceiptDtlService.saveEntity(bisBankReceiptDtl);
        BisBankReceiptPdf bisBankReceiptPdf = new BisBankReceiptPdf();
        BeanUtils.copyProperties(bisBankReceiptPdfDTO, bisBankReceiptPdf);
        bisBankReceiptPdf.setReceiptId(bisBankReceiptDtl.getId());
        bisBankReceiptPdfService.saveEntity(bisBankReceiptPdf);
    }
    /**
     * 外接口根据id查询电子回单
     *
     * @param id
     * @return
     */
    @Override
    public BisBankReceiptDtlDTO getById(String id) {
        BisBankReceiptDtl dtl = bisBankReceiptDtlService.get(id, true);
        BisBankReceiptDtlDTO dto = new BisBankReceiptDtlDTO();
        if (!EmptyUtils.isEmpty(dtl)) {
            BeanUtils.copyProperties(dtl, dto);
        }
        return dto;
    }

    @Override
    public void initImg(String id, HttpServletRequest request, HttpServletResponse response) {
      //  bisBankReceiptDtlService.initImg(id,request,response);
    }

    /**
     * 根据付款表id得到电子回单表ID集合
     * @param payId
     * @return 回单表ID集合
     */
    @Override
    public List<String> getIdsByPayId(String payId) {
        return bisBankReceiptDtlService.getIdsByPayId(payId);
    }

}
