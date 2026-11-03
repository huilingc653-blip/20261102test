package com.bytter.biz.receipt.service;

import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlService;
import com.bytter.biz.bdlink.vo.BisBankDtlBillVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.payment.api.dto.VNisPayBillDTO;
import com.bytter.biz.receipt.dao.BisBankReceiptDtlDao;
import com.bytter.biz.receipt.entity.BisBankReceiptDtl;
import com.bytter.biz.receipt.enums.IsFromEnum;
import com.bytter.biz.receipt.vo.EBankMoreBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.ofdrw.converter.export.OFDExporter;
import org.ofdrw.converter.export.PDFExporterIText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
/**
 * 银行电子回单
 *
 * @author zuoy 2018-04-09
 */
@Service("bisBankReceiptDtlService")
@Transactional
public class BisBankReceiptDtlService extends BaseService<BisBankReceiptDtl, String> {
    @Autowired
    private BisBankReceiptDtlDao bisBankReceiptDtlDao;
    @Autowired
    private BisBankReceiptDtlHisService bisBankReceiptDtlHisService;
    @DubboReference(check = false)
    private IBisBankReceiptDtlService iBisBankReceiptDtlService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtBankAccService bankAccService;
    @DubboReference(check = false)
	private IBillFileService billFileService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    private final static Map<String, String> MIME = new Hashtable<>();
    static {
        MIME.put("jpeg", "image/jpeg");
        MIME.put("jpg", "image/jpeg");
        MIME.put("jfif", "image/jpeg");
        MIME.put("jfif-tbnl", "image/jpeg");
        MIME.put("jpe", "image/jpeg");
        MIME.put("tiff", "image/tiff");
        MIME.put("tif", "image/tiff");
        MIME.put("gif", "image/gif");
        MIME.put("xls", "application/x-msexcel");
        MIME.put("doc", "application/msword");
        MIME.put("ppt", "application/x-mspowerpoint");
        MIME.put("zip", "application/x-zip-compressed");
        MIME.put("pdf", "application/pdf");
        MIME.put("png", "image/png");
    }
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;

    /**
     * 根据有效标志过滤
     * @param validSign 可空
     * @return
     */
    @Transactional(readOnly = true)
    public List<BisBifInitDTO> listByValidSign(String validSign){
        return bisBifInitService.listByValidSign(validSign);
    }
    @Override
    public BaseDao<BisBankReceiptDtl, String> getEntityDao() {
        return bisBankReceiptDtlDao;
    }
    public DataTablesOutput<BisBankReceiptDtlVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        String ins_corpId = (String) input.getFormdata().get("ins_corpId");
        //若未选开户单位，则默认查询当前登录人所有管理单位
        StringBuilder commonSql = new StringBuilder();
        if (StringUtils.isEmpty(ins_corpId)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_RECEIPT_BANKBILL_QUERY, user.getId()), "a.corp_id", true);
            commonSql.append(" and ").append(corpAuthSql);
        } else {
            String corpAuthSql = SqlUtil.getInSql(ins_corpId.split(","), "a.corp_id", true);
            commonSql.append(" and ").append(corpAuthSql);
            input.getFormdata().remove("ins_corpId");
        }
        DataTablesOutput<BisBankReceiptDtlVO> dataTablesOutput = this.iBisBankReceiptDtlService.getDataTablesUnion(input, commonSql);
        Map<String, BisBifInitDTO> bisBifInitMap = bisBifInitService.getBisBifInitMap();
        Map<String, BtCurrencyDTO> mapCur = new HashMap<String, BtCurrencyDTO>();
        List<BisBankReceiptDtlVO> bisDatas = dataTablesOutput.getData();
        if(CollectionUtils.isNotEmpty(bisDatas)) {
			List<String> curIds = bisDatas.stream().map(vo -> vo.getCurId()).collect(Collectors.toList());
        	mapCur = currencyService.getCurrencyByIds(curIds);
        }
        for (int i = 0; i < dataTablesOutput.getData().size(); i++) {//DAO循环取会有性能问题
            BisBankReceiptDtlVO bisBankReceiptDtlVO = (BisBankReceiptDtlVO) dataTablesOutput.getData().get(i);
            if(!StringUtils.isEmpty(bisBankReceiptDtlVO.getCdSign())) {
            	if(IsFromEnum.CREDIT_FLAG_IN.getValue().equals(bisBankReceiptDtlVO.getCdSign())) {
            		bisBankReceiptDtlVO.setCdSignName(IsFromEnum.CREDIT_FLAG_IN.getName());
            	}else if(IsFromEnum.CREDIT_FLAG_OUT.getValue().equals(bisBankReceiptDtlVO.getCdSign())) {
            		bisBankReceiptDtlVO.setCdSignName(IsFromEnum.CREDIT_FLAG_OUT.getName());
            	}
            }
            if(StringUtils.isEmpty(bisBankReceiptDtlVO.getIsFrom())) {
            	bisBankReceiptDtlVO.setIsFromName(IsFromEnum.FROM_BANK_LINK.getName());
            }else {
            	if(IsFromEnum.FROM_BANK_LINK.getValue().equals(bisBankReceiptDtlVO.getIsFrom())) {
            		bisBankReceiptDtlVO.setIsFromName(IsFromEnum.FROM_BANK_LINK.getName());
            	}else if(IsFromEnum.FROM_OCR.getValue().equals(bisBankReceiptDtlVO.getIsFrom())) {
            		bisBankReceiptDtlVO.setIsFromName(IsFromEnum.FROM_OCR.getName());
            	}else {
            		bisBankReceiptDtlVO.setIsFromName(IsFromEnum.FROM_BANK_LINK.getName());
            	}
            }
            if(!StringUtils.isEmpty(bisBankReceiptDtlVO.getCurId())) {
            	bisBankReceiptDtlVO.setCurName(!ObjectUtils.isEmpty(mapCur.get(bisBankReceiptDtlVO.getCurId())) ? (String)mapCur.get(bisBankReceiptDtlVO.getCurId()).getCurName() : "");
            }
            List<BisBankDtlBillVO> bisBankDtlBillVOS= bisBankReceiptDtlVO.getBisBankDtlBillVOList();
            if (EmptyUtils.isNotEmpty(bisBankDtlBillVOS)) {
                boolean oneDtl = true;
                for (BisBankDtlBillVO bisBankDtlBillVO : bisBankDtlBillVOS) {
                    if (StringUtils.isEmpty(bisBankReceiptDtlVO.getDtlId())) {
                        bisBankReceiptDtlVO.setDtlId(bisBankDtlBillVO.getDtlId());
                    } else {
                        bisBankReceiptDtlVO.setDtlId(bisBankReceiptDtlVO.getDtlId() + "/" + bisBankDtlBillVO.getDtlId());
                        oneDtl = false;
                    }
                }
                if (oneDtl) {
                    List<EBankMoreBillVO> eBankMoreBillVOS = bisBankReceiptDtlDao.getMoreBill(bisBankReceiptDtlVO.getDtlId());
                    for (EBankMoreBillVO eBankMoreBillVO : eBankMoreBillVOS) {
                        if (StringUtils.isEmpty(bisBankReceiptDtlVO.getBillId())) {
                            bisBankReceiptDtlVO.setBillId(eBankMoreBillVO.getBillId());
                            bisBankReceiptDtlVO.setBillFrom(eBankMoreBillVO.getIsFrom());
                        } else {
                            bisBankReceiptDtlVO.setBillId(bisBankReceiptDtlVO.getBillId() + "/" + eBankMoreBillVO.getBillId());
                        }
                    }
                } else {
                    boolean allDtlBill = true;//是否流水全勾对付款单
                    for (BisBankDtlBillVO bisBankDtlBillVO1 : bisBankDtlBillVOS) {
                        List<EBankMoreBillVO> eBankMoreBillVOS = bisBankReceiptDtlDao.getMoreBill(bisBankDtlBillVO1.getDtlId());
                        if (eBankMoreBillVOS.size() == 0) {
                            allDtlBill = false;
                        }
                    }
                    if (allDtlBill) {
                        bisBankReceiptDtlVO.setBillFrom("no_one");
                    }
                }
            }
            if(null != bisBifInitMap){
                BisBifInitDTO bisBifInitDTO = bisBifInitMap.get(bisBankReceiptDtlVO.getBifCode());
                if(null != bisBifInitDTO){
                    bisBankReceiptDtlVO.setBifName(bisBifInitDTO.getName());
                }
            }
        }
        return dataTablesOutput;
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    public BisBankReceiptDtlVO getByIdUnion(String id) {
        return this.iBisBankReceiptDtlService.getByIdUnion(id);
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    public BisBankReceiptDtlVO getByIdUnionV2(String id) {
        return this.iBisBankReceiptDtlService.getByIdUnionV2(id);
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
        return (byte) "0123456789abcdef".indexOf(c);
    }
    /**
     * 对字节流进行处理，图片显示，其他提供下载
     *
     * @param bisBankReceiptDtl
     * @return
     */
    public void streamOper(HttpServletRequest request, HttpServletResponse response, BisBankReceiptDtlVO bisBankReceiptDtl) throws IOException {
        String fileType = bisBankReceiptDtl.getFiletype().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (!EmptyUtils.isEmpty(bisBankReceiptDtl.getPdfToImg())) {
            //修改IE不兼容JPEG,故修改为PNG
            mime = "image/png";
        }
        if(!EmptyUtils.isEmpty(fileType) && ("jpg".equals(fileType) || "jpeg".equals(fileType))){
            //修改IE不兼容JPEG,故修改为PNG
            mime = "image/png";
        }
        if (mime == null) {
            mime = "application/x-msdownload";
        }
        response.setContentType(mime);
        try {
            String blobString = new String(bisBankReceiptDtl.getEbillimg());
            byte[] bytes = this.hexStringToByte(blobString);
            OutputStream outStream = response.getOutputStream();
            outStream.write(bytes, 0, bytes.length);
            outStream.flush();
        } catch (Exception e) {
            logger.error("streamOper exception:", e);
        }
    }
    /**
     * 对字节流进行处理，图片显示，其他提供下载
     *
     * @param bisBankReceiptDtl
     * @return
     */
    public void streamOperV2(HttpServletRequest request, HttpServletResponse response, BisBankReceiptDtlVO bisBankReceiptDtl) throws IOException {
        String fileType = bisBankReceiptDtl.getFiletype().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (!EmptyUtils.isEmpty(bisBankReceiptDtl.getPdfToImg())) {
            //修改IE不兼容JPEG,故修改为PNG
            mime = "image/png";
        }
        if(!EmptyUtils.isEmpty(fileType) && ("jpg".equals(fileType) || "jpeg".equals(fileType))){
            //修改IE不兼容JPEG,故修改为PNG
            mime = "image/png";
        }
        if (mime == null) {
            mime = "application/x-msdownload";
        }
        response.setContentType(mime);
        try {
            OutputStream outStream = response.getOutputStream();
            String blobString = new String(bisBankReceiptDtl.getEbillimg());
            byte[] bytes = this.hexStringToByte(blobString);
            ByteArrayInputStream arrrayIn=new ByteArrayInputStream(bytes);
            BufferedImage inimg=ImageIO.read(arrrayIn);
            ImageIO.write(inimg,"png",response.getOutputStream());
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            logger.error("streamOper exception:", e);
        }
    }
    /**
     * 对字节流进行处理，图片显示，其他提供下载
     *
     * @param bisBankReceiptDtl
     * @return
     */
    public void streamOperByPath(HttpServletRequest request, HttpServletResponse response, BisBankReceiptDtlVO bisBankReceiptDtl) throws IOException {
        String fileType = bisBankReceiptDtl.getFiletype().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (!EmptyUtils.isEmpty(bisBankReceiptDtl.getPdfToImg())) {
            //修改IE不兼容JPEG,故修改为PNG
            mime = "image/png";
        }
        if (mime == null) {
            mime = "application/x-msdownload";
        }
        response.setContentType(mime);
        byte[] bytes = billFileService.getFileByte(bisBankReceiptDtl.getFilePath());
        if (null == bytes) {
            int width = 500;
            int height = 100;
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = tag.createGraphics();
            graphics.setBackground(Color.WHITE);
            graphics.clearRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            graphics.drawString("找不到电子回单文件！", 150, 40);
            graphics.dispose();
            response.setContentType("image/png");
            ImageIO.write(tag, "png", response.getOutputStream());
            return;
        }
        try (OutputStream outStream = response.getOutputStream()) {
            outStream.write(bytes, 0, bytes.length);
            outStream.flush();
        } catch (Exception e) {
            logger.error("streamOper exception:", e);
        }
    }
    /**
     * PDF转图片后下载
     *
     * @param request           http请求
     * @param response          http相应
     * @param bisBankReceiptDtl 对象数据
     * @throws IOException
     */
    public void streamOperPdf(HttpServletRequest request, HttpServletResponse response, BisBankReceiptDtlVO bisBankReceiptDtl) throws IOException {
        String fileType = bisBankReceiptDtl.getFiletype().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (mime == null) {
            mime = "application/x-msdownload";
        }
        try {
            String blobString = new String(bisBankReceiptDtl.getEbillimg());
            byte[] buf = this.hexStringToByte(blobString);
            BufferedImage bufferedImage;
            //若使用icepdf工具转图片报错，则更改为apache工具将pdf进行转换
            try {
                //考虑IE清晰度的问题，优先用icepdf工具进行转换
                bufferedImage = getBufferedImageByPdf(new ByteArrayInputStream(buf));
            } catch (Exception e) {
                bufferedImage = getBufferedImageByIcepdf(new ByteArrayInputStream(buf));
            }
            if ("pdf".equalsIgnoreCase(bisBankReceiptDtl.getFiletype().trim())) {
                //修改IE不兼容JPEG,故修改为PNG
                mime = "image/png";
            }
            response.setContentType(mime);
            ImageIO.write(bufferedImage, "png", response.getOutputStream());
        } catch (Exception e) {
            response.getOutputStream().write(("预览失败:" + e.getMessage()).getBytes());
            logger.error("预览失败", e);
        }
    }
    /**
     * PDF转图片后下载
     *
     * @param response
     * @param bisBankReceiptDtl
     * @throws Exception
     */
    public void streamOperPdfBypath(HttpServletRequest request, HttpServletResponse response, BisBankReceiptDtlVO bisBankReceiptDtl) throws Exception {
        String fileType = bisBankReceiptDtl.getFiletype().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (mime == null) {
            mime = "application/x-msdownload";
        }

        byte[] buf = billFileService.getFileByteOss(bisBankReceiptDtl.getFilePath());
        if (null == buf) {
            int width = 500;
            int height = 100;
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = tag.createGraphics();
            graphics.setBackground(Color.WHITE);
            graphics.clearRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            graphics.drawString("找不到电子回单文件！", 150, 40);
            graphics.dispose();
            response.setContentType("image/png");
            ImageIO.write(tag, "png", response.getOutputStream());
            return;
        }
        try {
            BufferedImage bufferedImage = null;
            //若使用icepdf工具转图片报错，则更改为apache工具将pdf进行转换
            if("ofd".equalsIgnoreCase(fileType)) {
                // OFD转PDF再转图片
                ByteArrayOutputStream outputStream = null;
                try {
                    outputStream = new ByteArrayOutputStream();
                    // 使用 buf 直接转换为 InputStream
                    try (InputStream inputStream = new ByteArrayInputStream(buf);
                         BufferedInputStream bis = new BufferedInputStream(inputStream);
                         OFDExporter exporter = new PDFExporterIText(bis, outputStream)) {
                        exporter.export();
                    }

                    // PDF转图片
                    bufferedImage = convertPdfToImage(new ByteArrayInputStream(outputStream.toByteArray()));

                } catch (Exception e) {
                    logger.error("OFD转换失败，", e);
                    writeErrorImage(response, "OFD文件转换失败：" + e.getMessage());
                    return;
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            logger.warn("关闭输出流失败", e);
                        }
                    }
                }
            }else {
            	 try {
                     //考虑IE清晰度的问题，优先用icepdf工具进行转换
                     bufferedImage = getBufferedImageByPdf(new ByteArrayInputStream(buf));
                 } catch (Exception e) {
                     bufferedImage = getBufferedImageByIcepdf(new ByteArrayInputStream(buf));
                 }
                 if ("pdf".equalsIgnoreCase(bisBankReceiptDtl.getFiletype().trim())) {
                     //修改IE不兼容JPEG,故修改为PNG
                     mime = "image/png";
                 }
                 response.setContentType(mime);
            }

            ImageIO.write(bufferedImage, "png", response.getOutputStream());
        } catch (Exception e) {
            response.getOutputStream().write(("预览失败:" + e.getMessage()).getBytes());
            logger.error("预览失败", e);
        }
    }

    /**
     * 将PDF转换为图片
     */
    private BufferedImage convertPdfToImage(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("PDF输入流不能为空");
        }

        BufferedImage bufferedImage = null;
        Exception lastException = null;

        // 优先使用 getBufferedImageByPdf（清晰度更好）
        try {
            bufferedImage = getBufferedImageByPdf(inputStream);
            if (bufferedImage != null) {
                return bufferedImage;
            }
        } catch (Exception e) {
            lastException = e;
            logger.warn("使用getBufferedImageByPdf转换失败，尝试使用getBufferedImageByIcepdf", e);
            // 重置输入流
            try {
                if (inputStream.markSupported()) {
                    inputStream.reset();
                } else {
                    // 如果不支持reset，重新创建输入流
                    logger.warn("输入流不支持reset操作");
                }
            } catch (Exception resetEx) {
                logger.warn("重置输入流失败", resetEx);
            }
        }

        // 降级使用 getBufferedImageByIcepdf
        try {
            bufferedImage = getBufferedImageByPdf(inputStream);
            if (bufferedImage != null) {
                return bufferedImage;
            }
        } catch (Exception e) {
            lastException = e;
            logger.error("使用getBufferedImageByIcepdf转换也失败", e);
        }

        throw new Exception("PDF转图片失败", lastException);
    }
    /**
     * 使用 Apache PDFBox 将PDF转换为图片
     * @param inputStream PDF输入流
     * @return BufferedImage 图片对象
     * @throws Exception 转换异常
     */
    private BufferedImage getBufferedImageByPdf(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("PDF输入流不能为空");
        }

        PDDocument document = null;
        try {
            // 加载PDF文档
            document = PDDocument.load(inputStream);

            // 获取PDF页数
            int pages = document.getNumberOfPages();
            if (pages == 0) {
                throw new Exception("PDF文件没有页面");
            }

            // 创建PDF渲染器
            PDFRenderer renderer = new PDFRenderer(document);

            // 设置渲染DPI（提高图片清晰度）
            int dpi = 150; // 可根据需要调整，范围通常96-300
            float scale = dpi / 72f; // PDF默认DPI为72

            // 只转换第一页（可根据需要转换其他页）
            BufferedImage image = renderer.renderImageWithDPI(0, dpi);

            return image;
        } catch (Exception e) {
            logger.error("PDF转图片失败", e);
            throw new Exception("PDF转图片失败: " + e.getMessage(), e);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (Exception e) {
                    logger.warn("关闭PDF文档失败", e);
                }
            }
        }
    }


    /**
     * 返回错误提示图片
     */
    private void writeErrorImage(HttpServletResponse response, String errorMsg) {
        try {
            int width = 500;
            int height = 100;
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = tag.createGraphics();
            try {
                graphics.setBackground(Color.WHITE);
                graphics.clearRect(0, 0, width, height);
                graphics.setColor(Color.BLACK);
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Font font = new Font("微软雅黑", Font.PLAIN, 14);
                graphics.setFont(font);

                // 处理文字长度，避免超出图片边界
                FontMetrics fm = graphics.getFontMetrics();
                int textWidth = fm.stringWidth(errorMsg);
                int x = (width - textWidth) / 2;
                int y = (height + fm.getAscent() - fm.getDescent()) / 2;
                graphics.drawString(errorMsg, x, y);
            } finally {
                graphics.dispose();
            }

            response.setContentType("image/png");
            ImageIO.write(tag, "png", response.getOutputStream());
        } catch (Exception e) {
            logger.error("生成错误图片失败", e);
            try {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write(errorMsg);
            } catch (Exception ex) {
                logger.error("返回错误信息失败", ex);
            }
        }
    }

    /**
     * 通过ice工具转换pdf
     *
     * @param byteArrayInputStream 回单数据
     * @return 回单图片
     * @throws Exception 异常信息
     */
    public BufferedImage getBufferedImageByIcepdf(ByteArrayInputStream byteArrayInputStream) throws Exception {
        //缩放比例
        float scale = 2f;
        //旋转角度
        float rotation = 0f;
        String tempPath = System.getProperty("java.io.tmpdir") + File.separator + "ice-" + UUID.randomUUID() + ".pdf";
        try (FileOutputStream outputStream = new FileOutputStream(tempPath)) {
            StreamUtils.copy(byteArrayInputStream, outputStream);
            Document document = new Document();
            document.setFile(tempPath);
            int maxWidth = 0;
            int height = 0;
            List<BufferedImage> list = new ArrayList<>();
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);
                maxWidth = Math.max(maxWidth, image.getWidth());
                height += image.getHeight();
                list.add(image);
            }
            document.dispose();
            return getBufferedImage(maxWidth, height, list);
        } finally {
            File file = new File(tempPath);
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }
    /**
     * 通过apache的pdf工具转换
     *
     * @param byteArrayInputStream 回单数据
     * @return 回单
     * @throws IOException 存在IO异常
     */
    public BufferedImage getBufferedImageByPdf(ByteArrayInputStream byteArrayInputStream) throws IOException {
        int maxWidth = 0;
        int height = 0;
        //pdfbox解析
        PDDocument doc = PDDocument.load(byteArrayInputStream);
        PDFRenderer renderer = new PDFRenderer(doc);
        int pageCount = doc.getNumberOfPages();
        List<BufferedImage> list = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, 296);
            maxWidth = Math.max(maxWidth, image.getWidth());
            height += image.getHeight();
            list.add(image);
        }
        doc.close();
        return getBufferedImage(maxWidth, height, list);
    }
    /**
     * 若pdf中存在多张图片，则合并成为一张
     *
     * @param width  图片最大宽度
     * @param height 图片最大高度
     * @param list   回单集合
     * @return 合并后的回单图片
     */
    private BufferedImage getBufferedImage(int width, int height, List<BufferedImage> list) {
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = tag.createGraphics();
        int currentHeight = 0;
        for (int i = 0; i < list.size(); i++) {
            BufferedImage bufferedImage = list.get(0);
            graphics.drawImage(bufferedImage, 0, currentHeight, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
            if (i != 0) {
                currentHeight = bufferedImage.getHeight();
            }
        }
        graphics.dispose();
        return tag;
    }
    /**
     * 更新打印次数
     *
     * @param id
     */
    public void updatePrint(String id) {
        this.iBisBankReceiptDtlService.updatePrint(id);
    }
    /**
     * 自动迁移数据调用方法
     *
     * @param date
     * @return
     */
    public DataTablesOutput getByTime(DataTablesInput input, Date date) {
        return this.iBisBankReceiptDtlService.getByTime(input, date);
    }
    /**
     * 自动迁移数据调用方法 获取需求迁移的总数
     *
     * @param date
     * @return
     */
    public BigDecimal getByTimeCount(Date date) {
        return this.iBisBankReceiptDtlService.getByTimeCount(date);
    }
    /**
     * 付款单类型通过id获取
     *
     * @param id
     * @return
     */
    public VNisPayBillDTO getVNisPayBill(String id) {
        return bisBankReceiptDtlDao.getVNisPayBill(id);
    }
    /**
     * 流水ID得到勾对的付款单id
     *
     * @param id
     * @return
     */
    public EBankMoreBillVO getMoreBill(String id) {
        List<EBankMoreBillVO> list = bisBankReceiptDtlDao.getMoreBill(id);
        EBankMoreBillVO eBankMoreBillVO= new EBankMoreBillVO();
        for (EBankMoreBillVO leBankMoreBillVO : list) {
           if(StringUtils.isEmpty(eBankMoreBillVO.getBillId())){
               eBankMoreBillVO.setBillId(leBankMoreBillVO.getBillId());
               eBankMoreBillVO.setIsFrom(leBankMoreBillVO.getIsFrom());
           }else {
               eBankMoreBillVO.setBillId(eBankMoreBillVO.getBillId()+"/"+leBankMoreBillVO.getBillId());
           }
        }
        return eBankMoreBillVO;
    }
    public String getCodedFileName(String suffix, HttpServletRequest request, HttpServletResponse response) {
        String codedFileName = "";
        String name = "电子回单." + suffix;
        try {
            if (ServletUtils.isIE(request)) {
                codedFileName = URLEncoder.encode(name, "UTF8");
                //判断后缀代码，避免在IE的情况下显示不正确，by zhoul 2018-02-01 17:36
                if ("jpg".equals(suffix)) {
                    response.setContentType("image/jpeg");
                } else if ("png".equals(suffix)) {
                    response.setContentType("image/png");
                } else if ("gif".equals(suffix)) {
                    response.setContentType("image/gif");
                } else if ("bmp".equals(suffix)) {
                    response.setContentType("image/bmp");
                } else if ("pdf".equals(suffix)) {
                    response.setContentType("application/pdf");
                }
            } else {
                codedFileName = new String(name.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            codedFileName = name;
        }
        return codedFileName;
    }
    public void download(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	BisBankReceiptDtlVO vo = iBisBankReceiptDtlService.getByIdUnion(id);
        String codedFileName = getCodedFileName(vo.getFiletype().trim(), request, response);
        if("2".equals(vo.getSaveType()) && !StringUtils.isEmpty(vo.getFilePath()) && vo.getFilePath().indexOf(".ofd")>-1){
        	PDFMergerUtility mergePdf = new PDFMergerUtility();
            try (PDDocument doc = new PDDocument()) {
                    byte[] buf = null;
            		String path = "";
            		if(!vo.getFilePath().startsWith(CommonConstant.SLASH)) {
                		path = CommonConstant.SLASH+vo.getFilePath();
                	}else {
                		path = vo.getFilePath();
                	}
                path = path.replace("https:", "http:");
                buf = billFileService.getFileByteOss(path);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try (InputStream inputStream = new ByteArrayInputStream(buf);
                     BufferedInputStream bis = new BufferedInputStream(inputStream);
                     OFDExporter exporter = new PDFExporterIText(bis, outputStream)) {
                    exporter.export();
                }
                buf = outputStream.toByteArray();
                // pdf直接添加合并
                mergePdf.addSource(new ByteArrayInputStream(buf));
            } catch (IOException e) {
                logger.info("批量下载图片失败", e);
            }
            try (OutputStream os = response.getOutputStream()) {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/force-download;");
                response.addHeader("Content-Disposition", "attachment;fileName=" + getCodedFileName("pdf", request, response));
                //设置合并生成pdf文件名称
                mergePdf.setDestinationStream(os);
                //合并pdf
                mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
                // 更新打印次数
                updatePrint(id);
            } catch (IOException e) {
                logger.info("电子回单批量下载加载失败", e);
            }
        }else {
          try (OutputStream os = response.getOutputStream()) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download;");
            response.addHeader("Content-Disposition", "attachment;fileName=" + codedFileName);
            byte[] buf = null;
            if("2".equals(vo.getSaveType())){
            	if(!vo.getFilePath().startsWith(CommonConstant.SLASH)) {
            		buf = billFileService.getFileByte(CommonConstant.SLASH+vo.getFilePath());
            	}else {
            		buf = billFileService.getFileByte(vo.getFilePath());
            	}
            }else{
                String blobString = new String(vo.getEbillimg());
                buf = this.hexStringToByte(blobString);
            }
            BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(buf));
            int length;
            while ((length = bis.read(buf)) > 0) {
                os.write(buf, 0, length);
            }
            // 更新打印次数
            updatePrint(id);
        } catch (Exception e) {
            logger.error("电子回单[{}]下载失败", vo.getId(), e);
        }
      }
    }
    public void batchDownload(String ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //pdf合并工具类
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        try (PDDocument doc = new PDDocument()) {
            //控制排序
            String[] idList = ids.split(CommonConstant.COMMA_SPLIT);
            List<BisBankReceiptDtlVO> list = iBisBankReceiptDtlService.listByIds(ids);
            HashMap<String, BisBankReceiptDtlVO> map = new HashMap<>();
            for (BisBankReceiptDtlVO vo : list) {
                map.put(vo.getId(), vo);
            }
            for(String id:idList){
                BisBankReceiptDtlVO vo = map.get(id);
                byte[] buf = null;
                if("2".equals(vo.getSaveType())){
                	if("ofd".equalsIgnoreCase(vo.getFiletype())) {
                		String path = "";
                		if(!vo.getFilePath().startsWith(CommonConstant.SLASH)) {
                    		path = CommonConstant.SLASH+vo.getFilePath();
                    	}else {
                    		path = vo.getFilePath();
                    	}
                		path = path.replace("https:", "http:");
                        buf = billFileService.getFileByteOss(path);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        try (InputStream inputStream = new ByteArrayInputStream(buf);
                             BufferedInputStream bis = new BufferedInputStream(inputStream);
                             OFDExporter exporter = new PDFExporterIText(bis, outputStream)) {
                            exporter.export();
                        }
                        buf = outputStream.toByteArray();
                	}else {
                		if(!vo.getFilePath().startsWith(CommonConstant.SLASH)) {
                    		buf = billFileService.getFileByte(CommonConstant.SLASH+vo.getFilePath());
                    	}else {
                    		buf = billFileService.getFileByte(vo.getFilePath());
                    	}
                	}

                }else{
                    String blobString = new String(vo.getEbillimg());
                    buf = this.hexStringToByte(blobString);
                }
                if (!"pdf".equals(vo.getFiletype().trim()) && !"ofd".equals(vo.getFiletype().trim())) {
                    // 非pdf格式的，批量预览先将图片转换pdf然后合并展示
                    final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(buf));
                    float width = bufferedImage.getWidth();
                    float height = bufferedImage.getHeight();
                    final PDPage blankPage = new PDPage(new PDRectangle(width, height));
                    doc.addPage(blankPage);
                    final PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bufferedImage);
                    final PDPageContentStream contentStream = new PDPageContentStream(doc, blankPage,
                            PDPageContentStream.AppendMode.APPEND, false, false);
                    contentStream.drawImage(pdImage, 0, 0, width, height);
                    contentStream.close();
                    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        doc.save(out);
                        doc.removePage(blankPage);
                        mergePdf.addSource(new ByteArrayInputStream(out.toByteArray()));
                    }
                } else {
                    // pdf直接添加合并
                    mergePdf.addSource(new ByteArrayInputStream(buf));
                }
            }
        } catch (IOException e) {
            logger.info("批量下载图片失败", e);
        }
        try (OutputStream os = response.getOutputStream()) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download;");
            response.addHeader("Content-Disposition", "attachment;fileName=" + getCodedFileName("pdf", request, response));
            //设置合并生成pdf文件名称
            mergePdf.setDestinationStream(os);
            //合并pdf
            mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            // 更新打印次数
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                updatePrint(id);
            }
        } catch (IOException e) {
            logger.info("电子回单批量下载加载失败", e);
        }
    }
    /**
     * 预览页面加载图片
     *
     * @return
     */
    public void  initImg(String id, HttpServletRequest request, HttpServletResponse response) {
        try {
            BisBankReceiptDtlVO bisBankReceiptDtl = this.getByIdUnion(id);
            if(bisBankReceiptDtl!=null){
                if ("pdf".equalsIgnoreCase(bisBankReceiptDtl.getFiletype().trim())) {
                    this.streamOperPdf(request, response, bisBankReceiptDtl);
                } else {
                    this.streamOper(request, response, bisBankReceiptDtl);
                }
            }
            //兼容历史电子回单查询
            BisBankReceiptDtlHisVO bisBankReceiptDtlHis =null;
            if(bisBankReceiptDtl==null){
                bisBankReceiptDtlHis = this.bisBankReceiptDtlHisService.getByIdUnion(id);
                if(bisBankReceiptDtlHis !=null) {
                    if ("pdf".equalsIgnoreCase(bisBankReceiptDtlHis.getFiletype().trim())) {
                        this.bisBankReceiptDtlHisService.streamOperPdf(request, response, bisBankReceiptDtlHis);
                    } else {
                        this.bisBankReceiptDtlHisService.streamOper(request, response, bisBankReceiptDtlHis);
                    }
                }
            }
            if ((bisBankReceiptDtlHis==null && bisBankReceiptDtl == null)
                    || (bisBankReceiptDtl != null && bisBankReceiptDtl.getEbillimg() == null)
                    || (bisBankReceiptDtlHis != null && bisBankReceiptDtlHis.getEbillimg() == null)) {
                int width = 500;
                int height = 100;
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = tag.createGraphics();
                graphics.setBackground(Color.WHITE);
                graphics.clearRect(0, 0, width, height);
                graphics.setColor(Color.BLACK);
                graphics.drawString("找不到电子回单文件！", 150, 40);
                graphics.dispose();
                response.setContentType("image/png");
                ImageIO.write(tag, "png", response.getOutputStream());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 预览页面加载图片
     *
     * @return
     * @throws Exception
     */
    public void initImgV2(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            BisBankReceiptDtlVO bisBankReceiptDtl = this.getByIdUnionV2(id);
            if(bisBankReceiptDtl!=null){
                if("2".equals(bisBankReceiptDtl.getSaveType())){
                    //FTP
                    if ("pdf".equalsIgnoreCase(bisBankReceiptDtl.getFiletype().trim()) || "ofd".equalsIgnoreCase(bisBankReceiptDtl.getFiletype().trim())) {
                        this.streamOperPdfBypath(request, response, bisBankReceiptDtl);
                    } else {
                        this.streamOperByPath(request, response, bisBankReceiptDtl);
                    }
                }else if("1".equals(bisBankReceiptDtl.getSaveType())){
                    //数据库
                    if ("pdf".equalsIgnoreCase(bisBankReceiptDtl.getFiletype().trim())) {
                        this.streamOperPdf(request, response, bisBankReceiptDtl);
                    } else {
                        this.streamOperV2(request, response, bisBankReceiptDtl);
                    }
                }else {
                    //minio
                }
            }
            //兼容历史电子回单查询
            BisBankReceiptDtlHisVO bisBankReceiptDtlHis =null;
            if(bisBankReceiptDtl==null){
                bisBankReceiptDtlHis = this.bisBankReceiptDtlHisService.getByIdUnion(id);
                if(bisBankReceiptDtlHis !=null) {
                    if ("pdf".equalsIgnoreCase(bisBankReceiptDtlHis.getFiletype().trim())) {
                        this.bisBankReceiptDtlHisService.streamOperPdf(request, response, bisBankReceiptDtlHis);
                    } else {
                        this.bisBankReceiptDtlHisService.streamOper(request, response, bisBankReceiptDtlHis);
                    }
                }
            }
            if ((bisBankReceiptDtlHis==null && bisBankReceiptDtl == null)
                    || (bisBankReceiptDtl != null && bisBankReceiptDtl.getEbillimg() == null)
                    || (bisBankReceiptDtlHis != null && bisBankReceiptDtlHis.getEbillimg() == null)) {
                int width = 500;
                int height = 100;
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = tag.createGraphics();
                graphics.setBackground(Color.WHITE);
                graphics.clearRect(0, 0, width, height);
                graphics.setColor(Color.BLACK);
                graphics.drawString("找不到电子回单文件！", 150, 40);
                graphics.dispose();
                response.setContentType("image/png");
                ImageIO.write(tag, "png", response.getOutputStream());
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 根据付款表id得到电子回单表ID集合
     * @param payId
     * @return 回单表ID集合
     */
    public List<String> getIdsByPayId(String payId) {
        return this.bisBankReceiptDtlDao.getIdsByPayId(payId);
    }

    public InputStream getInputStream(String path) throws Exception {
        try {
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            return inputStream;
            // 使用inputStream进行操作，例如保存或显示图片
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getFileData(String path) {
        byte[] buffer = new byte[1024];
        try (
        	 ByteArrayOutputStream baos = new ByteArrayOutputStream();
             InputStream fis = getInputStream(path);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            int length;
            while ((length = bis.read(buffer)) > 0) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception exception) {
        	logger.error("path=={}文件流获取错误", path, exception);
        }
        return null;
    }
}
