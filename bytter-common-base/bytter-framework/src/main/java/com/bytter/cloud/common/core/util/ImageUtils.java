package com.bytter.cloud.common.core.util;

import cn.hutool.core.util.ObjectUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * 图片工具
 *
 * @author huangx
 * @date 2023/10/9 16:19
 */
public class ImageUtils {
    public static final Base64.Encoder ENCODER = Base64.getEncoder();

    private ImageUtils() {
    }

    /***
     * 图片对象转字节
     *
     * @param images  图片对象
     * @return List<byte[]>
     */
    public static List<byte[]> imageToByte(List<BufferedImage> images){
        List<byte[]> list = new ArrayList<>();
        if (ObjectUtil.isEmpty(images)){
            return list;
        }
        try(
                ByteArrayOutputStream os = new ByteArrayOutputStream();
        ) {
            for (BufferedImage image : images) {
                ImageIO.write(image, "jpg", os);
                //base64编码
                list.add(os.toByteArray());
                // 重置
                os.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /***
     * 图片对象转换base64
     *
     * @param images  图片对象
     * @return 图片base64
     */
    public static List<String> imageToBase64(List<BufferedImage> images){
        List<String> list = new ArrayList<>();
        if (ObjectUtil.isEmpty(images)){
            return list;
        }
        try(
                ByteArrayOutputStream os = new ByteArrayOutputStream();
        ) {
            for (BufferedImage image : images) {
                ImageIO.write(image, "jpg", os);
                byte[] dataList = os.toByteArray();
                //base64编码
                list.add(ENCODER.encodeToString(dataList));
                // 重置
                os.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /***
     * 图片对象转字节
     *
     * @param images  图片对象
     * @return List<byte[]>
     */
    public static byte[] imageToByte(BufferedImage images){
        try(
                ByteArrayOutputStream os = new ByteArrayOutputStream();
        ) {
            ImageIO.write(images, "jpg", os);
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 竖直合并图片
     *
     * @param images 图片对象
     * @return BufferedImage
     */
    public static BufferedImage mergePic(List<BufferedImage> images) {
        //最终图片宽、高，所有图片中最宽
        int desWidth = 0, desHeight = 0, maxWidth = 0;
        //新图片
        BufferedImage desImage;
        //没有图片返回null
        if (images.isEmpty()) {
            return null;
        }
        if(images.size() == 1){
            return images.get(0);
        }
        //合并图片
        //计算合成的图片长高
        for (BufferedImage img : images) {
            desWidth = img.getWidth();
            //获取最宽的图片
            if (desWidth > maxWidth) {
                maxWidth = desWidth;
            }
            desHeight += img.getHeight();
        }
        desWidth = maxWidth;
        //创建新图片
        desImage = new BufferedImage(desWidth, desHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = desImage.createGraphics();
        //合并子图到新图片
        //像素点起始x,y坐标
        int w_x = 0, h_y = 0;
        for (BufferedImage img : images) {
            int width = img.getWidth();
            int height = img.getHeight();
            graphics.drawImage(img, 0, h_y, null);
            w_x += width;
            h_y += height;
        }
        return desImage;
    }
}