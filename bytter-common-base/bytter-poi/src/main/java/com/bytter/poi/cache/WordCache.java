package com.bytter.poi.cache;
import com.bytter.poi.cache.manager.POICacheManager;
import com.bytter.poi.word.entity.MyXWPFDocument;
import lombok.extern.slf4j.Slf4j;
import java.io.InputStream;
/**
 * Excel转变成为Word的缓存
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Slf4j
public class WordCache {
    public WordCache() {
    }
    /**
     * 根据路径获取word文档
     *
     * @param url
     *            路径
     * @return MyXWPFDocument
     */
    public static MyXWPFDocument getXWPFDocumen(String url) {
        InputStream is = null;
        try {
            is = POICacheManager.getFile(url);
            MyXWPFDocument e = new MyXWPFDocument(is);
            MyXWPFDocument var3 = e;
            return var3;
        } catch (Exception var13) {
            log.error(var13.getMessage(), var13);
        } finally {
            try {
                is.close();
            } catch (Exception var12) {
                log.error(var12.getMessage(), var12);
            }
        }
        return null;
    }
}
