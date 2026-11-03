package com.bytter.poi.util;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelCollection;
import com.bytter.poi.annotation.ExcelEntity;
import com.bytter.poi.annotation.ExcelIgnore;
import com.bytter.poi.word.entity.WordImageEntity;
import com.bytter.poi.word.entity.params.ExcelListEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;
import static com.bytter.poi.util.PoiElUtil.END_STR;
import static com.bytter.poi.util.PoiElUtil.START_STR;
/**
 * 公共基础类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Slf4j
public final class PoiPublicUtil {
    private PoiPublicUtil() {
    }
    @SuppressWarnings({ "unchecked" })
    public static <K, V> Map<K, V> mapFor(Object... mapping) {
        Map<K, V> map = new HashMap<K, V>();
        for (int i = 0; i < mapping.length; i += 2) {
            map.put((K) mapping[i], (V) mapping[i + 1]);
        }
        return map;
    }
    /**
     * 彻底创建一个对象
     *
     * @param clazz
     *            对象类型
     * @param targetId
     *            对象类型
     * @return 对象
     */
    public static Object createObject(Class<?> clazz, String targetId) {
        Object obj = null;
        try {
            if (clazz.equals(Map.class)) {
                return new HashMap<String, Object>();
            }
            obj = clazz.newInstance();
            Field[] fields = getClassFields(clazz);
            for (Field field : fields) {
                if (isNotUserExcelUserThis(null, field, targetId)) {
                    continue;
                }
                if (isCollection(field.getType())) {
                    ExcelCollection collection = field.getAnnotation(ExcelCollection.class);
                    PoiReflectorUtil.fromCache(clazz).setValue(obj, field.getName(), collection.type().newInstance());
                } else if (!isJavaClass(field)) {
                    PoiReflectorUtil.fromCache(clazz).setValue(obj, field.getName(),
                            createObject(field.getType(), targetId));
                }
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException("创建对象异常");
        }
        return obj;
    }
    /**
     * 获取class，包括父类的字段
     *
     * @param clazz
     *            对象类型
     * @return 字段集合
     */
    public static Field[] getClassFields(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        Field[] fields;
        do {
            fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                list.add(fields[i]);
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class && clazz != null);
        return list.toArray(fields);
    }
    /**
     * 获取图片格式
     *
     * @param photoByte
     *            图片
     * @return 图片格式
     */
    public static String getFileExtendName(byte[] photoByte) {
        String strFileExtendName = "JPG";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
                && ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97)) {
            strFileExtendName = "GIF";
        } else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70)) {
            strFileExtendName = "JPG";
        } else if ((photoByte[0] == 66) && (photoByte[1] == 77)) {
            strFileExtendName = "BMP";
        } else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71)) {
            strFileExtendName = "PNG";
        }
        return strFileExtendName;
    }
    /**
     * 获取Excel2003图片
     *
     * @param sheet
     *            当前sheet对象
     * @param workbook
     *            工作簿对象
     * @return Map key:图片单元格索引（1_1）String，value:图片流PictureData
     */
    public static Map<String, PictureData> getSheetPicture03(HSSFSheet sheet, HSSFWorkbook workbook) {
        Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
        List<HSSFPictureData> pictures = workbook.getAllPictures();
        if (!pictures.isEmpty()) {
            for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
                HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
                if (shape instanceof HSSFPicture) {
                    HSSFPicture pic = (HSSFPicture) shape;
                    int pictureIndex = pic.getPictureIndex() - 1;
                    HSSFPictureData picData = pictures.get(pictureIndex);
                    String picIndex = String.valueOf(anchor.getRow1()) + "_" + String.valueOf(anchor.getCol1());
                    sheetIndexPicMap.put(picIndex, picData);
                }
            }
            return sheetIndexPicMap;
        } else {
            return sheetIndexPicMap;
        }
    }
    /**
     * 获取Excel2007图片
     *
     * @param sheet
     *            当前sheet对象
     * @param workbook
     *            工作簿对象
     * @return Map key:图片单元格索引（1_1）String，value:图片流PictureData
     */
    @Deprecated
    public static Map<String, PictureData> getSheetPicture07(XSSFSheet sheet, XSSFWorkbook workbook) {
        return new HashMap<String, PictureData>();
    }
    /**
     * 获取模块源路径
     *
     * @param filePath
     *            文件路径
     * @return 路径
     */
    public static String getWebRootPath(String filePath) {
        try {
            String path = PoiPublicUtil.class.getClassLoader().getResource("").toURI().getPath();
            path = path.replace("WEB-INF/classes/", "");
            path = path.replace("file:/", "");
            log.error("模块资源路径：【{}】",(path + filePath));
            return path + filePath;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 判断是不是集合的实现类
     *
     * @param clazz
     *            对象类型
     * @return 是不是集合的实现类
     */
    public static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }
    /**
     * 是不是java基础类
     *
     * @param field
     *            字段
     * @return 是不是java基础类
     */
    public static boolean isJavaClass(Field field) {
        Class<?> fieldType = field.getType();
        boolean isBaseClass = false;
        if (fieldType.isArray()) {
            isBaseClass = false;
        } else if (fieldType.isPrimitive() || fieldType.getPackage() == null
                || "java.lang".equals(fieldType.getPackage().getName())
                || "java.math".equals(fieldType.getPackage().getName())
                || "java.sql".equals(fieldType.getPackage().getName())
                || "java.util".equals(fieldType.getPackage().getName())) {
            isBaseClass = true;
        }
        return isBaseClass;
    }
    /**
     * 判断是否不要在这个excel操作中
     *
     * @param exclusionsList
     *            集合
     * @param field
     *            字段
     * @param targetId
     *            targetId
     * @return 是否不要在这个excel操作中
     */
    public static boolean isNotUserExcelUserThis(List<String> exclusionsList, Field field, String targetId) {
        boolean boo = true;
        if (field.getAnnotation(ExcelIgnore.class) != null) {
            boo = true;
        } else if (boo && field.getAnnotation(ExcelCollection.class) != null
                && isUseInThis(field.getAnnotation(ExcelCollection.class).name(), targetId) && (exclusionsList == null
                        || !exclusionsList.contains(field.getAnnotation(ExcelCollection.class).name()))) {
            boo = false;
        } else if (boo && field.getAnnotation(Excel.class) != null
                && isUseInThis(field.getAnnotation(Excel.class).name(), targetId)
                && (exclusionsList == null || !exclusionsList.contains(field.getAnnotation(Excel.class).name()))) {
            boo = false;
        } else if (boo && field.getAnnotation(ExcelEntity.class) != null
                && isUseInThis(field.getAnnotation(ExcelEntity.class).name(), targetId) && (exclusionsList == null
                        || !exclusionsList.contains(field.getAnnotation(ExcelEntity.class).name()))) {
            boo = false;
        }
        return boo;
    }
    private static boolean isUseInThis(String exportName, String targetId) {
        return targetId == null || "".equals(exportName) || exportName.indexOf("_") < 0
                || exportName.indexOf(targetId) != -1;
    }
    private static Integer getImageType(String type) {
        if ("JPG".equalsIgnoreCase(type) || "JPEG".equalsIgnoreCase(type)) {
            return XWPFDocument.PICTURE_TYPE_JPEG;
        }
        if ("GIF".equalsIgnoreCase(type)) {
            return XWPFDocument.PICTURE_TYPE_GIF;
        }
        if ("BMP".equalsIgnoreCase(type)) {
            return XWPFDocument.PICTURE_TYPE_GIF;
        }
        if ("PNG".equalsIgnoreCase(type)) {
            return XWPFDocument.PICTURE_TYPE_PNG;
        }
        return XWPFDocument.PICTURE_TYPE_JPEG;
    }
    /**
     * 返回流和图片类型 2013-11-20
     *
     * @param entity
     * @return (byte[]) isAndType[0],(Integer)isAndType[1]
     * @throws Exception
     */
    public static Object[] getIsAndType(WordImageEntity entity) throws Exception {
        Object[] result = new Object[2];
        String type;
        if (entity.getType().equals(WordImageEntity.URL)) {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            BufferedImage bufferImg;
            String path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath()
                    + entity.getUrl();
            path = path.replace("WEB-INF/classes/", "");
            path = path.replace("file:/", "");
            bufferImg = ImageIO.read(new File(path));
            ImageIO.write(bufferImg,
                    entity.getUrl().substring(entity.getUrl().indexOf(".") + 1, entity.getUrl().length()),
                    byteArrayOut);
            result[0] = byteArrayOut.toByteArray();
            type = entity.getUrl().split("/.")[entity.getUrl().split("/.").length - 1];
        } else {
            result[0] = entity.getData();
            type = PoiPublicUtil.getFileExtendName(entity.getData());
        }
        result[1] = getImageType(type);
        return result;
    }
    /**
     * 获取参数值
     *
     * @param params
     *            参数
     * @param object
     *            对象
     * @return 参数值
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Object getParamsValue(String params, Object object) throws Exception {
        if (params.indexOf(".") != -1) {
            String[] paramsArr = params.split("\\.");
            return getValueDoWhile(object, paramsArr, 0);
        }
        if (object instanceof Map) {
            return ((Map) object).get(params);
        }
        return PoiReflectorUtil.fromCache(object.getClass()).getValue(object, params);
    }
    /**
     * 解析数据
     *
     * @param currentText
     *            文本内容
     * @param map
     *            解析集合
     * @return 解析后对象
     * @throws Exception
     */
    public static Object getRealValue(String currentText, Map<String, Object> map) throws Exception {
        String params = "";
        while (currentText.indexOf(START_STR) != -1) {
            params = currentText.substring(currentText.indexOf(START_STR) + 2, currentText.indexOf(END_STR));
            Object obj = PoiElUtil.eval(params.trim(), map);
            // 判断图片或者是集合
            if (obj instanceof WordImageEntity || obj instanceof List || obj instanceof ExcelListEntity) {
                return obj;
            } else {
                currentText = currentText.replace(START_STR + params + END_STR, obj.toString());
            }
        }
        return currentText;
    }
    /**
     * 通过遍历过去对象值
     *
     * @param object
     * @param paramsArr
     * @param index
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Object getValueDoWhile(Object object, String[] paramsArr, int index) throws Exception {
        if (object == null) {
            return "";
        }
        if (object instanceof WordImageEntity) {
            return object;
        }
        if (object instanceof Map) {
            object = ((Map) object).get(paramsArr[index]);
        } else {
            object = PoiReflectorUtil.fromCache(object.getClass()).getValue(object, paramsArr[index]);
        }
        return (index == paramsArr.length - 1) ? (object == null ? "" : object)
                : getValueDoWhile(object, paramsArr, ++index);
    }
    /**
     * double to String 防止科学计数法
     *
     * @param value
     * @return
     */
    public static String doubleToString(Double value) {
        String temp = value.toString();
        if (temp.contains("E")) {
            BigDecimal bigDecimal = new BigDecimal(temp);
            temp = bigDecimal.toPlainString();
        }
        return temp;
    }
    /**
     * 统一 key的获取规则
     *
     * @param key
     * @param targetId
     * @return
     */
    public static String getValueByTargetId(String key, String targetId, String defalut) {
        if (StringUtils.isEmpty(targetId) || key.indexOf("_") < 0) {
            return key;
        }
        String[] arr = key.split(",");
        String[] tempArr;
        for (String str : arr) {
            tempArr = str.split("_");
            if (tempArr == null || tempArr.length < 2) {
                return defalut;
            }
            if (targetId.equals(tempArr[1])) {
                return tempArr[0];
            }
        }
        return defalut;
    }
    /**
     * 支持换行操作
     *
     * @param currentRun
     * @param currentText
     */
    public static void setWordText(XWPFRun currentRun, String currentText) {
        if (StringUtils.isNotBlank(currentText)) {
            String[] tempArr = currentText.split("\r\n");
            for (int i = 0, le = tempArr.length - 1; i < le; i++) {
                currentRun.setText(tempArr[i], i);
                currentRun.addBreak();
            }
            currentRun.setText(tempArr[tempArr.length - 1], tempArr.length - 1);
        }
    }
}
