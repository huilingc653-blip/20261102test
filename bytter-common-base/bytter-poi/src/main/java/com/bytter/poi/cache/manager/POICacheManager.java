package com.bytter.poi.cache.manager;
import com.bytter.core.utils.FileUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
/**
 * poi缓存管理
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Slf4j
public final class POICacheManager {
    private static LoadingCache loadingCache;
    public POICacheManager() {
    }
    /**
     * 获取缓存文件
     *
     * @param id
     *            缓存文件ID
     * @return 缓存文件流
     */
    public static InputStream getFile(String id) {
        try {
            byte[] e = Arrays.copyOf((byte[]) loadingCache.get(id), ((byte[]) loadingCache.get(id)).length);
            return new ByteArrayInputStream(e);
        } catch (ExecutionException var2) {
            log.error(var2.getMessage(), var2);
            return null;
        }
    }
    static {
        loadingCache = CacheBuilder.newBuilder().expireAfterWrite(7L, TimeUnit.DAYS).maximumSize(50L)
                .build(new CacheLoader<String, byte[]>() {
                    @Override
                    public byte[] load(String url) throws Exception {
                        return FileUtils.getFileByte(url);
                    }
                });
    }
}
