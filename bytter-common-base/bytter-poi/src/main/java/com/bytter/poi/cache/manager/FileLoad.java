package com.bytter.poi.cache.manager;
import com.bytter.poi.util.PoiPublicUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Slf4j
class FileLoad {
    FileLoad() {
    }
    /**
     * 根据url获取文件
     *
     * @param url
     *            路径
     * @return 文件
     */
    public byte[] getFile(String url) {
        FileInputStream files = null;
        ByteArrayOutputStream baos = null;
        try {
            try {
                files = new FileInputStream(url);
            } catch (FileNotFoundException var19) {
                String len = PoiPublicUtil.getWebRootPath(url);
                files = new FileInputStream(len);
            }
            baos = new ByteArrayOutputStream();
            byte[] e = new byte[1024];
            int len1;
            while ((len1 = files.read(e)) > -1) {
                baos.write(e, 0, len1);
            }
            baos.flush();
            byte[] var6 = baos.toByteArray();
            return var6;
        } catch (FileNotFoundException var20) {
            log.error(var20.getMessage(), var20);
        } catch (IOException var21) {
            log.error(var21.getMessage(), var21);
        } finally {
            try {
                if (files != null) {
                    files.close();
                }
                if (files != null) {
                    baos.close();
                }
            } catch (IOException var18) {
                log.error(var18.getMessage(), var18);
            }
        }
        log.error(files + "这个路径文件没有找到,请查询");
        return null;
    }
}
