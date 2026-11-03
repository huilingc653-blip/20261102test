package com.bytter.biz.reconcile.service;

import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.bdlink.api.service.IBisBankMonthStatementDtlService;
import com.bytter.biz.bdlink.vo.BisBankMonthStatementDtlVO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBillFileService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
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
import java.net.URLEncoder;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 银行对账单Service
 *
 * @author huangx 2022-12-01
 */
@Service("bisBankMonthStatementDtlService")
@Transactional
public class BisBankMonthStatementDtlService extends AbstractService {
    @DubboReference(check = false)
    private IBisBankMonthStatementDtlService bisBankMonthStatementDtlService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
	private IBillFileService billFileService;
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

    public DataTablesOutput getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        String ins_corpId = (String) input.getFormdata().get("ins_corpId");
        String corpAuthSql;
        List<String> corpAuth =new ArrayList<>();
        //若未选开户单位，则默认查询当前登录人所有管理单位
        if (StringUtils.isEmpty(ins_corpId)) {
            corpAuth =corpService.getUserCorpIds(ResourceConstant.NETBANK_BIS_BISBANKMONTHSTATEMENTDTL_QUERY, user.getId());
        } else {
            corpAuthSql = SqlUtil.getInSql(ins_corpId.split(","), "acc.corp_Id", true);
            for(String corp:ins_corpId.split(",")){
                corpAuth.add(corp);
            }
            input.getFormdata().remove("ins_corpId");
        }
        Map<String, Object> params = new HashMap();
        List<BtBankAccDTO> accDTOList = iBtBankAccService.getBankAccByCorpIdList(corpAuth,"", new HashMap<>());
        StringBuilder commonSql = new StringBuilder();
        if (accDTOList.size() > 0) {
            List<String> accList = accDTOList.stream().map(BtBankAccDTO::getBankAcc).collect(Collectors.toList());
            if (accList != null && accList.size() > 0) {
                commonSql.append(" and ").append(SqlUtil.getInSql(accList, "t.customer_Account", true));
            }
        }
        //设置账号授权控制
        List<String> bankAccGrantList = accountGrantService.listByMultiValAccs(input.getCurrentUserId(), ResourceConstant.NETBANK_BIS_BISBANKMONTHSTATEMENTDTL_QUERY,null,
                AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE,
                AccountGrantConstant.QUERY_DETAIL);
        commonSql.append(" and ").append(SqlUtil.getInSql(bankAccGrantList, "t.customer_Account", true));
        DataTablesOutput output = bisBankMonthStatementDtlService.getDataTablesUnion(input, commonSql);
        List<BisBankMonthStatementDtlVO> voList=output.getData();
        if(voList.size()>0) {
            Map<String, BtBankAccDTO> accMap = new HashMap<>();
            for (BtBankAccDTO dto : accDTOList) {
                accMap.put(dto.getBankAcc(), dto);
            }
            for (BisBankMonthStatementDtlVO vo : voList) {
                BtBankAccDTO accDTO=accMap.get(vo.getCustomerAccount());
                vo.setCorpId(accDTO.getCorpId());
                vo.setCorpName(accDTO.getCorpName());
                vo.setCurName(accDTO.getCurName());
                vo.setCustomerAccountName(accDTO.getAccName());
                vo.setBifName(accDTO.getBifName());
            }
        }
        return output;
    }
    /**
     * 根据ID获取银行对账单
     *
     * @param id
     * @return
     */
    public BisBankMonthStatementDtlVO getByIdUnion(String id) {
        return bisBankMonthStatementDtlService.getByIdUnion(id);
    }
    /**
     * 预览页面加载图片
     *
     * @return
     */
    public void initImgV2(String id, HttpServletRequest request, HttpServletResponse response) {
        try {
            BisBankMonthStatementDtlVO bisBankMonthStatementDtl = this.getByIdUnion(id);
            if(bisBankMonthStatementDtl!=null){
                if("2".equals(bisBankMonthStatementDtl.getSaveType())){
                    if ("pdf".equalsIgnoreCase(bisBankMonthStatementDtl.getFileType().trim())) {
                        this.streamOperPdfBypath(request, response, bisBankMonthStatementDtl);
                    } else {
                        this.streamOperByPath(request, response, bisBankMonthStatementDtl);
                    }
                }else{
                    if ("pdf".equalsIgnoreCase(bisBankMonthStatementDtl.getFileType().trim())) {
                        this.streamOperPdf(request, response, bisBankMonthStatementDtl);
                    } else {
                        this.streamOperV2(request, response, bisBankMonthStatementDtl);
                    }
                }
            }
            if (bisBankMonthStatementDtl != null && bisBankMonthStatementDtl.getStatementImg() == null) {
                int width = 500;
                int height = 100;
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = tag.createGraphics();
                graphics.setBackground(Color.WHITE);
                graphics.clearRect(0, 0, width, height);
                graphics.setColor(Color.BLACK);
                graphics.drawString("找不到对账单文件！", 150, 40);
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
     * 对字节流进行处理，图片显示，其他提供下载
     *
     * @param bisBankMonthStatementDtl
     * @return
     */
    public void streamOperV2(HttpServletRequest request, HttpServletResponse response, BisBankMonthStatementDtlVO bisBankMonthStatementDtl) throws IOException {
        String fileType = bisBankMonthStatementDtl.getFileType().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (!EmptyUtils.isEmpty(bisBankMonthStatementDtl.getStatementImg())) {
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
            String blobString = new String(bisBankMonthStatementDtl.getStatementImg());
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
     * @param bisBankMonthStatementDtl
     * @return
     */
    public void streamOperByPath(HttpServletRequest request, HttpServletResponse response, BisBankMonthStatementDtlVO bisBankMonthStatementDtl) throws IOException {
        String fileType = bisBankMonthStatementDtl.getFileType().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (!EmptyUtils.isEmpty(bisBankMonthStatementDtl.getStatementImg())) {
            //修改IE不兼容JPEG,故修改为PNG
            mime = "image/png";
        }
        if (mime == null) {
            mime = "application/x-msdownload";
        }
        response.setContentType(mime);
        byte[] bytes = billFileService.getFileByte(bisBankMonthStatementDtl.getFilePath());
        if (null == bytes) {
            int width = 500;
            int height = 100;
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = tag.createGraphics();
            graphics.setBackground(Color.WHITE);
            graphics.clearRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            graphics.drawString("找不到对账单文件！", 150, 40);
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
     * @param bisBankMonthStatementDtl 对象数据
     * @throws IOException
     */
    public void streamOperPdf(HttpServletRequest request, HttpServletResponse response, BisBankMonthStatementDtlVO bisBankMonthStatementDtl) throws IOException {
        String fileType = bisBankMonthStatementDtl.getFileType().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (mime == null) {
            mime = "application/x-msdownload";
        }
        try {
            String blobString = new String(bisBankMonthStatementDtl.getStatementImg());
            byte[] buf = this.hexStringToByte(blobString);
            BufferedImage bufferedImage;
            //若使用icepdf工具转图片报错，则更改为apache工具将pdf进行转换
            try {
                //考虑IE清晰度的问题，优先用icepdf工具进行转换
                bufferedImage = getBufferedImageByPdf(new ByteArrayInputStream(buf));
            } catch (Exception e) {
                bufferedImage = getBufferedImageByIcepdf(new ByteArrayInputStream(buf));
            }
            if ("pdf".equalsIgnoreCase(bisBankMonthStatementDtl.getFileType().trim())) {
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
     * PDF转图片后下载
     *
     * @param response
     * @param bisBankMonthStatementDtl
     * @throws IOException
     */
    public void streamOperPdfBypath(HttpServletRequest request, HttpServletResponse response, BisBankMonthStatementDtlVO bisBankMonthStatementDtl) throws IOException {
        String fileType = bisBankMonthStatementDtl.getFileType().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (mime == null) {
            mime = "application/x-msdownload";
        }
        byte[] buf = billFileService.getFileByte(bisBankMonthStatementDtl.getFilePath());
        if (null == buf) {
            int width = 500;
            int height = 100;
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = tag.createGraphics();
            graphics.setBackground(Color.WHITE);
            graphics.clearRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            graphics.drawString("找不到对账单文件！", 150, 40);
            graphics.dispose();
            response.setContentType("image/png");
            ImageIO.write(tag, "png", response.getOutputStream());
            return;
        }
        try {
            BufferedImage bufferedImage;
            //若使用icepdf工具转图片报错，则更改为apache工具将pdf进行转换
            try {
                //考虑IE清晰度的问题，优先用icepdf工具进行转换
                bufferedImage = getBufferedImageByPdf(new ByteArrayInputStream(buf));
            } catch (Exception e) {
                bufferedImage = getBufferedImageByIcepdf(new ByteArrayInputStream(buf));
            }
            if ("pdf".equalsIgnoreCase(bisBankMonthStatementDtl.getFileType().trim())) {
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
        return getBufferedImage(maxWidth, height, null);
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
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        BisBankMonthStatementDtlVO vo = this.getByIdUnion(id);
        String codedFileName = getCodedFileName(vo.getFileType().trim(), request, response);
        try (OutputStream os = response.getOutputStream()) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download;");
            response.addHeader("Content-Disposition", "attachment;fileName=" + codedFileName);
            byte[] buf = null;
            if("2".equals(vo.getSaveType())){
                buf = billFileService.getFileByte(vo.getFilePath());
            }else{
                String blobString = new String(vo.getStatementImg());
                buf = this.hexStringToByte(blobString);
            }
            BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(buf));
            int length;
            while ((length = bis.read(buf)) > 0) {
                os.write(buf, 0, length);
            }
        } catch (Exception e) {
            logger.error("对账单[{}]下载失败", vo.getId(), e);
        }
    }
    public void batchDownload(String ids, HttpServletRequest request, HttpServletResponse response) {
        //pdf合并工具类
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        try (PDDocument doc = new PDDocument()) {
            //控制排序
            String[] idList = ids.split(CommonConstant.COMMA_SPLIT);
            List<BisBankMonthStatementDtlVO> list = bisBankMonthStatementDtlService.listByIds(ids);
            HashMap<String, BisBankMonthStatementDtlVO> map = new HashMap<>();
            for (BisBankMonthStatementDtlVO vo : list) {
                map.put(vo.getId(), vo);
            }
            for(String id:idList){
                BisBankMonthStatementDtlVO vo = map.get(id);
                byte[] buf = null;
                if("2".equals(vo.getSaveType())){
                    buf = billFileService.getFileByte(vo.getFilePath());
                }else{
                    String blobString = new String(vo.getStatementImg());
                    buf = this.hexStringToByte(blobString);
                }
                if (!"pdf".equals(vo.getFileType().trim())) {
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
        } catch (IOException e) {
            logger.info("对账单批量下载加载失败", e);
        }
    }
    public String getCodedFileName(String suffix, HttpServletRequest request, HttpServletResponse response) {
        String codedFileName = "";
        String name = "对账单." + suffix;
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
}
