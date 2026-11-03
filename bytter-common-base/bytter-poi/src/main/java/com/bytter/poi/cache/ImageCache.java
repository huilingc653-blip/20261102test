/**
 * Copyright 2013-2015 JueYue (qrb.jueyue@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bytter.poi.cache;
import com.bytter.poi.cache.manager.POICacheManager;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.poi.util.IOUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
/**
 * Excel转变成为Image的缓存
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class ImageCache {
    private static LoadingCache<String, byte[]> loadingCache;
    static {
        loadingCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).maximumSize(2000)
                .build(new CacheLoader<String, byte[]>() {
                    @Override
                    public byte[] load(String imagePath) throws Exception {
                        InputStream is = POICacheManager.getFile(imagePath);
                        BufferedImage bufferImg = ImageIO.read(is);
                        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                        try {
                            ImageIO.write(bufferImg,
                                    imagePath.substring(imagePath.indexOf(".") + 1, imagePath.length()), byteArrayOut);
                            return byteArrayOut.toByteArray();
                        } finally {
                            IOUtils.closeQuietly(is);
                            IOUtils.closeQuietly(byteArrayOut);
                        }
                    }
                });
    }
    /**
     * 根据路径获取图片
     *
     * @param imagePath
     *            路径
     * @return byte[]
     */
    public static byte[] getImage(String imagePath) {
        try {
            return loadingCache.get(imagePath);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 加载image缓存
     *
     * @param loadingCache
     *            缓存对象
     */
    public static void setLoadingCache(LoadingCache<String, byte[]> loadingCache) {
        ImageCache.loadingCache = loadingCache;
    }
}
