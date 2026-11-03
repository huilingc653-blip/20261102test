package com.bytter.cloud.common.core.util;

import org.apache.commons.lang3.StringUtils;
import org.ofdrw.converter.ImageMaker;
import org.ofdrw.reader.OFDReader;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * ofd文件工具类
 */
public class OfdUtils {
    public static final Base64.Decoder DECODER = Base64.getDecoder();

    private OfdUtils() {
    }

    /***
     * ofdBase64转图片，返回base64编码集合
     *
     * @param ofdBase64  pdf类型base64
     * @return 图片base64
     */
    public static List<String> ofdToImageBase64(String ofdBase64){
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(ofdBase64)){
            return list;
        }
        return ImageUtils.imageToBase64(OfdUtils.ofdToImage(ofdBase64));
    }

    /***
     * ofdBase64转图片，返回图片对象
     *
     * @param ofdBase64  pdf类型base64
     * @return BufferedImage
     */
    public static List<BufferedImage> ofdToImage(String ofdBase64){
        List<BufferedImage> list = new ArrayList<>();
        if (StringUtils.isEmpty(ofdBase64)){
            return list;
        }
        byte[] decode = DECODER.decode(ofdBase64.getBytes(StandardCharsets.UTF_8));
        try(
                ByteArrayInputStream stream = new ByteArrayInputStream(decode);
                OFDReader reader = new OFDReader(stream);
        ) {
            ImageMaker imageMaker = new ImageMaker(reader, 15);
            for (int i = 0; i < imageMaker.pageSize(); i++) {
                // 指定页码转换图片
                list.add(imageMaker.makePage(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}