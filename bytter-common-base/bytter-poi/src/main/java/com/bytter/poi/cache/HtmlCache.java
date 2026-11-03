package com.bytter.poi.cache;
import com.bytter.poi.excel.entity.ExcelToHtmlParams;
import com.bytter.poi.excel.html.ExcelToHtmlServer;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;
/**
 * Excel 转变成为Html 的缓存
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class HtmlCache {
    private static LoadingCache<ExcelToHtmlParams, String> loadingCache;
    static {
        loadingCache = CacheBuilder.newBuilder().expireAfterWrite(7, TimeUnit.DAYS).maximumSize(200)
                .build(new CacheLoader<ExcelToHtmlParams, String>() {
                    @Override
                    public String load(ExcelToHtmlParams params) throws Exception {
                        return new ExcelToHtmlServer(params).printPage();
                    }
                });
    }
    /**
     * Excel转变成为Html的缓存
     *
     * @param params
     *            Excel
     * @return html字符串
     */
    public static String getHtml(ExcelToHtmlParams params) {
        try {
            return loadingCache.get(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 加载html缓存
     *
     * @param loadingCache
     *            缓存对象
     */
    public static void setLoadingCache(LoadingCache<ExcelToHtmlParams, String> loadingCache) {
        HtmlCache.loadingCache = loadingCache;
    }
}
