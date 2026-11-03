package com.bytter.core.utils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
@Slf4j
/**
 * 文件缓存管理类 Created by zhouj on 2017/6/19.
 */
public class FileCacheManagerUtils {
    private static LoadingCache loadingCache;
    public static InputStream getFile(String url) {
        try {
            byte[] e = Arrays.copyOf((byte[]) loadingCache.get(url), ((byte[]) loadingCache.get(url)).length);
            return new ByteArrayInputStream(e);
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
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
