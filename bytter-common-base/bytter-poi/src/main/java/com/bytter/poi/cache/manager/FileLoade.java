package com.bytter.poi.cache.manager;
import com.bytter.poi.util.PoiPublicUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * Created by zhouj on 2017/6/2.
 */
@Slf4j
class FileLoade {
    FileLoade() {
    }
    public byte[] getFile(String url) {
        FileInputStream fileis = null;
        ByteArrayOutputStream baos = null;
        try {
            try {
                fileis = new FileInputStream(url);
            } catch (FileNotFoundException var19) {
                String len = PoiPublicUtil.getWebRootPath(url);
                fileis = new FileInputStream(len);
            }
            baos = new ByteArrayOutputStream();
            byte[] e = new byte[1024];
            int len1;
            while ((len1 = fileis.read(e)) > -1) {
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
                if (fileis != null) {
                    fileis.close();
                }
                if (fileis != null) {
                    baos.close();
                }
            } catch (IOException var18) {
                log.error(var18.getMessage(), var18);
            }
        }
        log.error(fileis + "这个路径文件没有找到,请查询");
        return null;
    }
}
