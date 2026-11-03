package com.bytter.biz.erp.utils;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.erp.entity.ErpBankReceiptImgView;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBillFileService;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件下载工具类
 */
@Slf4j
public class FileDownloadUtil {

    /**
     * 保存类型常量
     */
    public static final int SAVE_TYPE_DB = 1;        // 保存到DB中
    public static final int SAVE_TYPE_FTP = 2;       // 保存到FTP服务器中
    public static final int SAVE_TYPE_MINIO = 4;      // 保存到MINIO
    public static final int SAVE_TYPE_NAS = 6;        // 保存到NAS盘
    public final static String UNIX_FILE_SEPARATOR = "/";
    public final static String WINDOWS_FILE_SEPARATOR = "\\\\";

    /**
     * 根据saveType下载文件
     *
     * @return 文件资源
     */
    public static byte[] downloadFile(ErpBankReceiptImgView op, IBillFileService billFileService) throws IOException {
        String filePath = op.getFilePath();
        String saveType = op.getSaveType();
        switch (Integer.parseInt(saveType)) {
            case SAVE_TYPE_DB:
                return downloadFromDb(op);
            case SAVE_TYPE_FTP:
                return downloadFromFtp(filePath, billFileService);
//            case SAVE_TYPE_MINIO:
//                return downloadFromMinio(filePath);
            case SAVE_TYPE_NAS:
                return downloadFromNas(filePath);
            default:
                throw new IllegalArgumentException("不支持的保存类型: " + saveType);
        }
    }

    /**
     * 从数据库中下载文件
     *
     * @param op 电子回单对象
     * @return byte[] 文件字节
     */
    private static byte[] downloadFromDb(ErpBankReceiptImgView op) throws IOException {
        byte[] fileBytes = null;
        if (ObjectUtil.isNotEmpty(op.getEbillimg()) || ObjectUtil.isNotEmpty(op.getPdfToImg())) {
            byte[] ebilling = op.getEbillimg();
            byte[] pdfToImg = op.getPdfToImg();

            if (ebilling != null || pdfToImg != null) {
                if (ebilling != null) {
                    String ebillImgString = new String(ebilling);
                    fileBytes = hexStringToByte(ebillImgString);
                }
                if (pdfToImg != null) {
                    String pdfToImgString = new String(pdfToImg);
                    fileBytes = hexStringToByte(pdfToImgString);
                }
            }
        }
        return fileBytes;
    }

    /**
     * 从FTP中下载文件
     *
     * @param relativePath    ftp文件路径
     * @param billFileService ftp文件服务
     * @return byte[] 文件字节
     */
    private static byte[] downloadFromFtp(String relativePath, IBillFileService billFileService) throws IOException {
        byte[] fileBytes = null;
        if (StringUtils.isNotEmpty(relativePath)) {
            if (!relativePath.startsWith(CommonConstant.SLASH)) {
                fileBytes = billFileService.getFileByte(CommonConstant.SLASH + relativePath);
            } else {
                fileBytes = billFileService.getFileByte(relativePath);
            }
        }
        return fileBytes;
    }

    /**
     * 从NAS盘下载文件
     */
    private static byte[] downloadFromNas(String filePath) throws IOException {
        Path fullPath = Paths.get(filePath);

        if (!Files.exists(fullPath)) {
            throw new IOException("NAS文件不存在: " + fullPath.toString());
        }

        if (Files.isDirectory(fullPath)) {
            throw new IOException("指定路径是目录而非文件: " + fullPath.toString());
        }

        return Files.readAllBytes(fullPath);
    }

    // 保存文件到本地
    public static void saveBytesToFile(byte[] bytes, String filePath) {
        try {
            // 使用Files.write方法将字节数组写入文件
            Files.write(Paths.get(filePath), bytes);
            log.info("文件已成功保存到: " + filePath);
        } catch (IOException e) {
            log.info("保存文件时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 过滤 \b \t \n \f \r
     * 避免因为路径中保存相关字符而被转义
     *
     * @param path 文件路径
     * @return 路径
     */
    public static String filtrationPath(String path) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        return path.replaceAll("\b", "/b").replaceAll("\t", "/t")
                .replaceAll("\n", "/n").replaceAll("\f", "/f")
                .replaceAll("\r", "/r");
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }


    public static void main(String[] args) throws IOException {
        byte[] bytes = downloadFromNas("D:/ebill/123.pdf");
        String ebillimgInfo = new BASE64Encoder().encode(bytes);
        System.out.println(ebillimgInfo);
    }
}
