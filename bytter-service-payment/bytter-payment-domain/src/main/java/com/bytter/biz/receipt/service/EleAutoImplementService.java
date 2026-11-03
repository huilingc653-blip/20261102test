package com.bytter.biz.receipt.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.receipt.vo.BisBankReceiptPdfVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBillFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
/**
 * @Title: service
 * @description: 银行电子回单执行service
 * @author: qijh
 * @create: 2019-12-05 11:09
 **/
@Service("eleAutoImplementService")
@Slf4j
public class EleAutoImplementService {
    @Autowired(required = false)
    private BisBankReceiptDtlService bisBankReceiptDtlService;
    @Autowired(required = false)
    private BisBankReceiptDtlHisService bisBankReceiptDtlHisService;
    @DubboReference(check = false)
	private IBillFileService billFileService;
    @Autowired(required = false)
    private BisBankReceiptPdfService bisBankReceiptPdfService;
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void runBisBankReceiptDtlHis(List<BisBankReceiptDtlVO> bisBankReceiptDtlVOList) {
        try {
            if (EmptyUtils.isNotEmpty(bisBankReceiptDtlVOList)) {
                Date nowDate = DateUtils.getDate();
                int year = DateUtils.getYear(nowDate);
                int month = DateUtils.getMonth(nowDate);
                int day = DateUtils.getDay(nowDate);
                for (BisBankReceiptDtlVO bisBankReceiptDtlVO : bisBankReceiptDtlVOList) {
                    BisBankReceiptDtlVO bisBankReceiptDtlHis = new BisBankReceiptDtlVO();
                    BeanUtils.copyNotNullProperties(bisBankReceiptDtlVO, bisBankReceiptDtlHis);
//                    Blob img = bisBankReceiptDtlVO.getEbillimg();
                    UUID uuid = UUID.randomUUID();
                    String pdfName = uuid.toString().replace("-", "");
                    String filepath = "bill" + File.separator + year + File.separator + month + File.separator + day + File.separator + pdfName;
                    if ("pdf".equals(bisBankReceiptDtlVO.getFiletype().trim())) {
                        filepath += ".pdf";
                    } else {
                        filepath += ".png";
                    }
                    BisBankReceiptDtlHisVO bisBankReceiptDtlHisVO = bisBankReceiptDtlHisService.getByIdUnion(bisBankReceiptDtlVO.getId());
                    if(EmptyUtils.isEmpty(bisBankReceiptDtlHisVO)) {
                        if (ObjectUtil.isNotEmpty(bisBankReceiptDtlVO.getEbillimg())) {
                            String blobString = new String(bisBankReceiptDtlVO.getEbillimg());
                            byte[] bytes = this.hexStringToByte(blobString);
                            billFileService.uploadFile(filepath, bytes);
                            bisBankReceiptDtlHis.setFilePath(filepath);
                        }

                        bisBankReceiptDtlHis.setId(bisBankReceiptDtlVO.getId());
                        //保存历史电子回单
                        bisBankReceiptDtlHisService.saveEntity(bisBankReceiptDtlHis);
                    }
                    //删除原电子回单
                    bisBankReceiptDtlService.delete(bisBankReceiptDtlVO.getId());
                    BisBankReceiptPdfVO bisBankReceiptPdf = bisBankReceiptPdfService.getByReceiptId(bisBankReceiptDtlVO.getId());
                    if (Objects.nonNull(bisBankReceiptPdf)) {
                        bisBankReceiptPdfService.delete(bisBankReceiptPdf.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("电子回单同步失败", e);
        }
    }
    /**
     * 16进制字符串转化为二进制数据
     *
     * @param hex
     * @return
     */
    public byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }
    /**
     * 私有处理方法
     */
    private String getRootPath() {
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String rootPath = "";
        //windows下
        if ("\\".equals(File.separator)) {
            rootPath = classPath.substring(1, classPath.indexOf("/WEB-INF"));
            rootPath = rootPath.replace("/", "\\");
        }
        //linux下
        if ("/".equals(File.separator)) {
            rootPath = classPath.substring(0, classPath.indexOf("/WEB-INF"));
            rootPath = rootPath.replace("\\", "/");
        }
        return rootPath;
    }
}
