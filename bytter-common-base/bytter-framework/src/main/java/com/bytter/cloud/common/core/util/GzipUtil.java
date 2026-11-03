package com.bytter.cloud.common.core.util;
import org.springframework.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.*;
/**
 * Gzip、ZIP压缩和解压工具类
 *
 * @author zhoul
 * @since 1.0.0
 */
public class GzipUtil {
    private GzipUtil() {
    }
    /**
     * 压缩GZip
     *
     * @param data 待压缩数据
     * @return 压缩完成数据
     */
    public static byte[] gZip(byte[] data) {
        byte[] b;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(data);
            gzip.finish();
            b = bos.toByteArray();
        } catch (Exception ex) {
            throw ExceptionUtil.unchecked(ex);
        }
        return b;
    }
    /**
     * 解压GZip
     *
     * @param data 已压缩数据
     * @return 解压后数据
     */
    public static byte[] unGzip(byte[] data) {
        byte[] b;
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                GZIPInputStream gzip = new GZIPInputStream(bis);
        ) {
            b = StreamUtils.copyToByteArray(gzip);
        } catch (Exception ex) {
            throw ExceptionUtil.unchecked(ex);
        }
        return b;
    }
    /**
     * 压缩Zip
     *
     * @param data 待数据
     * @return 压缩后的数据
     */
    public static byte[] zip(byte[] data) {
        byte[] b;
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ZipOutputStream zip = new ZipOutputStream(bos)
        ) {
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(data.length);
            zip.putNextEntry(entry);
            zip.write(data);
            zip.closeEntry();
            b = bos.toByteArray();
        } catch (Exception ex) {
            throw ExceptionUtil.unchecked(ex);
        }
        return b;
    }
    /**
     * 解压Zip
     *
     * @param data 待解压数据
     * @return 解压后的数据
     */
    public static byte[] unZip(byte[] data) {
        byte[] b = null;
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ZipInputStream zip = new ZipInputStream(bis)
        ) {
            while (zip.getNextEntry() != null) {
                b = StreamUtils.copyToByteArray(zip);
            }
        } catch (Exception ex) {
            throw ExceptionUtil.unchecked(ex);
        }
        return b;
    }
}
