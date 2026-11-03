package com.bytter.biz.receipt.service;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlHisService;
import com.bytter.biz.bdlink.vo.BisBankDtlBillVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.receipt.dao.BisBankReceiptDtlDao;
import com.bytter.biz.receipt.entity.BisBankReceiptDtlHis;
import com.bytter.biz.receipt.vo.EBankMoreBillVO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.IBillFileService;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;
/**
 * 银行电子回单
 *
 * @author zuoy 2018-04-09
 */
@Service("bisBankReceiptDtlHisService")
public class BisBankReceiptDtlHisService extends AbstractService {
    @Autowired(required = false)
    private BisBankReceiptDtlService bisBankReceiptDtlService;
    @DubboReference(check = false)
    private IBisBankReceiptDtlHisService iBisBankReceiptDtlHisService;
    @DubboReference(check = false)
	private IBillFileService billFileService;
    @Autowired
    private BisBankReceiptDtlDao bisBankReceiptDtlDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IDictService dictService;
    @Value("${bytter.bfs.files.oss.cdnDomian}")
    private String cdnDomian;


    private final static Map<String, String> MIME = new Hashtable<String, String>();
    static {
        MIME.put("jpeg", "image/jpeg");
        MIME.put("jpg", "image/jpeg");
        MIME.put("jfif", "image/jpeg");
        MIME.put("jfif-tbnl", "image/jpeg");
        MIME.put("jpe", "image/jpeg");
        MIME.put("jfif", "image/jpeg");
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

    public void saveEntity(BisBankReceiptDtlVO vo){
        iBisBankReceiptDtlHisService.saveEntity(vo);
    }

    public DataTablesOutput<BisBankReceiptDtlHisVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        StringBuilder commonSql = new StringBuilder();
        String ins_corpId = (String) input.getFormdata().get("ins_corpId");
        String corpAuthSql;
        //若未选开户单位，则默认查询当前登录人所有管理单位
        if (StringUtils.isEmpty(ins_corpId)) {
            corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_RECEIPT_BANKBILL_QUERY, user.getId()), "acc.corp_id", true);
            commonSql.append(" and ").append(corpAuthSql);
        } else {
            corpAuthSql = SqlUtil.getInSql(ins_corpId.split(","), "acc.corp_id", true);
            commonSql.append(" and ").append(corpAuthSql);
            input.getFormdata().remove("ins_corpId");
        }
        List<BtBankAccDTO> accDTOS= bankAccService.getBankAccNotUnionByCorpIdList(null,commonSql.toString(), new HashMap<>());
        Map<String ,BtBankAccDTO> accMap=new HashMap<>();
        for(BtBankAccDTO acc:accDTOS){
            accMap.put(acc.getBankAcc(),acc);
        }
        DataTablesOutput<BisBankReceiptDtlHisVO> dataTablesOutput = this.iBisBankReceiptDtlHisService.getDataTablesUnion(input,commonSql.toString(), user);
        Map<String, BisBifInitDTO> bisBifInitMap = null;
        Map<String, BtCurrencyDTO> mapCur = null;
        if(!CollectionUtils.isEmpty(dataTablesOutput.getData())) {
        	List<BisBankReceiptDtlHisVO> list = dataTablesOutput.getData();
        	List<String> curIds = list.stream().map(vo -> vo.getCurId()).collect(Collectors.toList());
        	mapCur = currencyService.getCurrencyByIds(curIds);//币别
        	List<String> bifCodes = list.stream().map(vo -> vo.getBifCode()).collect(Collectors.toList());
        	bisBifInitMap = bisBifInitService.listBifInitByIds(bifCodes);//银行编码
        }
        List<DictDTO> dictList = dictService.list("btBankAccDtl.cdSign");//借贷标志
		Map<String, String> cdSignMap = dictList.stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));

        for (int i = 0; i < dataTablesOutput.getData().size(); i++) {
            BisBankReceiptDtlHisVO bisBankReceiptDtlHisVO = (BisBankReceiptDtlHisVO) dataTablesOutput.getData().get(i);
            BtBankAccDTO acc=accMap.get(bisBankReceiptDtlHisVO.getPayAccNo());
            if(!StringUtils.isEmpty(bisBankReceiptDtlHisVO.getCurId())) {
            	bisBankReceiptDtlHisVO.setCurName(mapCur.get(bisBankReceiptDtlHisVO.getCurId()).getCurName());
            }
			if(!StringUtils.isEmpty(bisBankReceiptDtlHisVO.getBifCode())) {
				bisBankReceiptDtlHisVO.setBifName(bisBifInitMap.get(bisBankReceiptDtlHisVO.getBifCode()).getName());
            }
			if(!StringUtils.isEmpty(bisBankReceiptDtlHisVO.getCdSign())) {
				bisBankReceiptDtlHisVO.setCdSignName(cdSignMap.get(bisBankReceiptDtlHisVO.getCdSign()));
			}
            if(null != acc) {
                bisBankReceiptDtlHisVO.setCorpId(acc.getCorpId());
                bisBankReceiptDtlHisVO.setCorpName(acc.getCorpName());
            }
            List<BisBankDtlBillVO> bisBankDtlBillVOS= bisBankReceiptDtlHisVO.getBisBankDtlBillVOList();
            if (EmptyUtils.isNotEmpty(bisBankDtlBillVOS)) {
                boolean oneDtl = true;
                for (BisBankDtlBillVO bisBankDtlBillVO : bisBankDtlBillVOS) {
                    if (StringUtils.isEmpty(bisBankReceiptDtlHisVO.getDtlId())) {
                        bisBankReceiptDtlHisVO.setDtlId(bisBankDtlBillVO.getDtlId());
                    } else {
                        bisBankReceiptDtlHisVO.setDtlId(bisBankReceiptDtlHisVO.getDtlId() + "/" + bisBankDtlBillVO.getDtlId());
                        oneDtl = false;
                    }
                }
                if (oneDtl) {
                    List<EBankMoreBillVO> eBankMoreBillVOS = bisBankReceiptDtlDao.getMoreBill(bisBankReceiptDtlHisVO.getDtlId());
                    for (EBankMoreBillVO eBankMoreBillVO : eBankMoreBillVOS) {
                        if (StringUtils.isEmpty(bisBankReceiptDtlHisVO.getBillId())) {
                            bisBankReceiptDtlHisVO.setBillId(eBankMoreBillVO.getBillId());
                            bisBankReceiptDtlHisVO.setBillFrom(eBankMoreBillVO.getIsFrom());
                        } else {
                            bisBankReceiptDtlHisVO.setBillId(bisBankReceiptDtlHisVO.getBillId() + "/" + eBankMoreBillVO.getBillId());
                        }
                    }
                } else {
                    bisBankReceiptDtlHisVO.setBillFrom("no_one");
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
    public BisBankReceiptDtlHisVO getByIdUnion(String id) {
        return this.iBisBankReceiptDtlHisService.getByIdUnion(id);
    }
    /**
     * 对字节流进行处理，图片显示，其他提供下载
     *
     * @param bisBankReceiptDtlHis
     * @return
     */
    public void streamOper(HttpServletRequest request, HttpServletResponse response, BisBankReceiptDtlHisVO bisBankReceiptDtlHis) throws IOException {
        String fileType = bisBankReceiptDtlHis.getFiletype().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (!EmptyUtils.isEmpty(bisBankReceiptDtlHis.getPdfToImg())) {
            //修改IE不兼容JPEG,故修改为PNG
            mime = "image/png";
        }
        if (mime == null) {
            mime = "application/x-msdownload";
        }
        response.setContentType(mime);
        byte[] bytes = billFileService.getFileByte(bisBankReceiptDtlHis.getFilePath());
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
     * @param response
     * @param bisBankReceiptDtlHis
     * @throws IOException
     */
    public void streamOperPdf(HttpServletRequest request, HttpServletResponse response, BisBankReceiptDtlHisVO bisBankReceiptDtlHis) throws IOException {
        String fileType = bisBankReceiptDtlHis.getFiletype().trim();
        String mime = MIME.get(fileType.toLowerCase());
        if (mime == null) {
            mime = "application/x-msdownload";
        }

        //历史回单的的图片路径和回单的路径保持一致
        String path = "";
        if(!bisBankReceiptDtlHis.getFilePath().startsWith(CommonConstant.SLASH)) {
            path = cdnDomian+CommonConstant.SLASH+bisBankReceiptDtlHis.getFilePath();
        }else {
            path = cdnDomian+bisBankReceiptDtlHis.getFilePath();
        }
        path = path.replace("https:", "http:");
        byte[] buf = getFileData(path);
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
            BufferedImage bufferedImage;
            //若使用apache工具将pdf转图片报错，则更改为icepdf工具进行转换
            try {
                bufferedImage = getBufferedImageByPdf(new ByteArrayInputStream(buf));
            } catch (Exception e) {
                bufferedImage = getBufferedImageByIcepdf(new ByteArrayInputStream(buf));
            }
            if ("pdf".equalsIgnoreCase(bisBankReceiptDtlHis.getFiletype().trim())) {
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
        this.iBisBankReceiptDtlHisService.updatePrint(id);
    }
    public String getCodedFileName(String suffix, HttpServletRequest request, HttpServletResponse response) {
        String codedFileName = "";
        String name = "银行电子回单." + suffix;
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
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        BisBankReceiptDtlHisVO vo = iBisBankReceiptDtlHisService.getByIdUnion(id);
        String codedFileName = getCodedFileName(vo.getFiletype().trim(), request, response);
        try (OutputStream os = response.getOutputStream()) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download;");
            response.addHeader("Content-Disposition", "attachment;fileName=" + codedFileName);
//            byte[] buf = billFileService.getFileByte(vo.getFilePath());

            //历史回单的的图片路径和回单的路径保持一致
            String path = "";
            if(!vo.getFilePath().startsWith(CommonConstant.SLASH)) {
                path = cdnDomian+CommonConstant.SLASH+vo.getFilePath();
            }else {
                path = cdnDomian+vo.getFilePath();
            }
            path = path.replace("https:", "http:");
            byte[] buf = getFileData(path);

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
    public void batchDownload(String ids, HttpServletRequest request, HttpServletResponse response) {
        //pdf合并工具类
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        try (PDDocument doc = new PDDocument()) {
            //控制排序
            String[] idList = ids.split(CommonConstant.COMMA_SPLIT);
            List<BisBankReceiptDtlHisVO> list = iBisBankReceiptDtlHisService.listByIds(ids);
            HashMap<String, BisBankReceiptDtlHisVO> map = new HashMap<>();
            for (BisBankReceiptDtlHisVO vo : list) {
                map.put(vo.getId(), vo);
            }
            for(String id:idList){
                BisBankReceiptDtlHisVO vo = map.get(id);
                byte[] buf = billFileService.getFileByte(vo.getFilePath());
                if (!"pdf".equals(vo.getFiletype().trim())) {
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
                    if (null != buf) {
                        mergePdf.addSource(new ByteArrayInputStream(buf));
                    }
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
            logger.info("历史电子回单批量下载加载失败", e);
        }
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
