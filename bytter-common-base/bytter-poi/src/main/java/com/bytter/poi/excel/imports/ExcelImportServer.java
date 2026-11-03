package com.bytter.poi.excel.imports;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.excel.ExcelImportUtil;
import com.bytter.poi.excel.entity.ImportParams;
import com.bytter.poi.excel.entity.params.ExcelCollectionParams;
import com.bytter.poi.excel.entity.params.ExcelImportEntity;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.excel.entity.result.ExcelImportResult;
import com.bytter.poi.excel.entity.vo.TemplateExcelConstants;
import com.bytter.poi.excel.imports.base.ImportBaseService;
import com.bytter.poi.excel.imports.verifys.VerifyHandlerServer;
import com.bytter.poi.exception.excel.ExcelImportException;
import com.bytter.poi.exception.excel.enums.ExcelImportEnum;
import com.bytter.poi.util.*;
import com.google.common.collect.Maps;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.util.HtmlUtils;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
/**
 * Excel导入实现类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Slf4j
public class ExcelImportServer extends ImportBaseService {
    private CellValueServer cellValueServer = new CellValueServer();
    private VerifyHandlerServer verifyHandlerServer = new VerifyHandlerServer();
    private boolean verfiyFail = false;
    private CellStyle errorCellStyle;
    public ExcelImportServer() {
    }
    /***
     * 向List里面继续添加元素
     *
     * @param object
     *            添加对象
     * @param param
     *            导入集合
     * @param row
     *            行
     * @param titleMap
     *            标题集合
     * @param targetId
     *            id
     * @param pictures
     *            图片属性
     * @param params
     *            导入参数
     * @throws Exception
     */
    private void addListContinue(Object object, ExcelCollectionParams param, Row row, Map<Integer, String> titleMap,
            String targetId, Map<String, PictureData> pictures, ImportParams params) throws Exception {
        Collection collection = (Collection) PoiReflectorUtil.fromCache(object.getClass()).getValue(object,
                param.getName());
        Object entity = PoiPublicUtil.createObject(param.getType(), targetId);
        boolean isUsed = false;// 是否需要加上这个对象
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); ++i) {
            Cell cell = row.getCell(i);
            String titleString = titleMap.get(i);
            if (param.getExcelParams().containsKey(titleString)) {
                if ((param.getExcelParams().get(titleString)).getType() == 2) {
                    String picId = row.getRowNum() + "_" + i;
                    this.saveImage(object, picId, param.getExcelParams(), titleString, pictures, params);
                } else {
                    this.saveFieldValue(params, entity, cell, param.getExcelParams(), titleString, row);
                }
                isUsed = true;
            }
        }
        if (isUsed) {
            collection.add(entity);
        }
    }
    /**
     * 获取key的值,针对不同类型获取不同的值
     *
     * @param cell
     * @return
     */
    private String getKeyValue(Cell cell) {
        Object obj = PoiCellUtil.getCellValue(cell);
        return obj == null ? null : obj.toString().trim();
    }
    /**
     * 获取保存的真实路径
     *
     * @param excelImportEntity
     * @param object
     * @return
     * @throws Exception
     */
    private String getSaveUrl(ExcelImportEntity excelImportEntity, Object object) throws Exception {
        String url = "";
        if ("upload".equals(excelImportEntity.getSaveUrl())) {
            if (excelImportEntity.getMethods() != null && excelImportEntity.getMethods().size() > 0) {
                object = this.getFieldBySomeMethod(excelImportEntity.getMethods(), object);
            }
            // url = object.getClass().getName().split("\\.")[object.getClass().getName().split("\\.").length - 1];
            // return excelImportEntity.getSaveUrl() + "/" + url.substring(0, url.lastIndexOf("Entity"));
            url = object.getClass().getName().split("\\.")[object.getClass().getName().split("\\.").length - 1];
            return excelImportEntity.getSaveUrl() + "/" + url;
        } else {
            return excelImportEntity.getSaveUrl();
        }
    }
    /**
     * 导入
     *
     * @param result
     *            导入集合
     * @param sheet
     *            工作表
     * @param pojoClass
     *            导入对象类型
     * @param params
     *            导入参数
     * @param pictures
     *            图片属性
     * @param <T>
     *            泛型，返回对象
     * @return List集合
     * @throws Exception
     */
    private <T> List<T> importExcel(Collection<T> result, Sheet sheet, Class<?> pojoClass, ImportParams params,
            Map<String, PictureData> pictures) throws Exception {
        List collection = new ArrayList();
        Map<String, ExcelImportEntity> excelParams = new HashMap<String, ExcelImportEntity>();
        List<ExcelCollectionParams> excelCollection = new ArrayList<ExcelCollectionParams>();
        String targetId = null;
        if (!Map.class.equals(pojoClass)) {
            Field[] rows = PoiPublicUtil.getClassFields(pojoClass);
            ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
            if (etarget != null) {
                targetId = etarget.value();
            }
            this.getAllExcelField(targetId, rows, excelParams, excelCollection, pojoClass, (List) null);
        }
        Iterator<Row> rows = sheet.rowIterator();
        for (int j = 0; j < params.getTitleRows(); j++) {
            rows.next();
        }
        Map<Integer, String> titlemap = getTitleMap(rows, params, excelCollection);
        checkIsValidTemplate(titlemap, excelParams, params, excelCollection);
        Row row = null;
        Object object = null;
        String picId;
        int readRow = 0;
        // 跳过无效行
        for (int i = 0; i < params.getStartRows(); i++) {
            rows.next();
        }
        long startListTime = System.currentTimeMillis();
        while (rows.hasNext()
                && (row == null || sheet.getLastRowNum() - row.getRowNum() > params.getLastOfInvalidRow())) {
            if (params.getReadRows() > 0 && readRow > params.getReadRows()) {
                break;
            }
            // TODO: 周健 过滤读取最后行数
            if (params.getEndRows() > 0 && readRow >= params.getEndRows() + params.getReadRows()) {
                break;
            }
            row = rows.next();
            if (row.getFirstCellNum() < 0) {
                readRow++;
                continue;
            }
            // 判断是集合元素还是不是集合元素,如果是就继续加入这个集合,不是就创建新的对象
            // keyIndex 如果为空就不处理,仍然处理这一行
            // TODO: 处理存在空值 不过滤去掉整行数据
            if("com.bytter.biz.inneraccount.vo.BtBankAccDtlRpaVO".equals(pojoClass.getName())){
                params.setKeyIndex(3);
            }

            // if((row.getCell(params.getKeyIndex()) == null ||
            // StringPoiUtils.isEmpty(this.getKeyValue(row.getCell(params.getKeyIndex())))) && object != null) {
            // 只有包含明细集合的模板才将空主键行作为上一条数据的明细；普通模板按行独立解析。
            if (!excelCollection.isEmpty() && row.getCell(params.getKeyIndex()) == null && object != null) {
                for (ExcelCollectionParams param : excelCollection) {
                    addListContinue(object, param, row, titlemap, targetId, pictures, params);
                }
            } else {
                object = PoiPublicUtil.createObject(pojoClass, targetId);
                try {
                    for (int e = row.getFirstCellNum(), le = titlemap.size(); e < le; e++) {
                        Cell cell = row.getCell(e);
                        String titleString = titlemap.get(e);
                        if (excelParams.containsKey(titleString) || Map.class.equals(pojoClass)) {
                            if (excelParams.get(titleString) != null && (excelParams.get(titleString)).getType() == 2) {
                                picId = row.getRowNum() + "_" + e;
                                this.saveImage(object, picId, excelParams, titleString, pictures, params);
                            } else {
                                this.saveFieldValue(params, object, cell, excelParams, titleString, row);
                            }
                        }
                    }
                    for (ExcelCollectionParams param : excelCollection) {
                        addListContinue(object, param, row, titlemap, targetId, pictures, params);
                    }
                    // 判断对象属性是否都为空 2018年2月6日10:49:08 周健
                    if (PoiValidationUtil.isNullObject(object, false)) {
                        continue;
                    }
                    // if (verifyingDataValidity(object, row, params, pojoClass)) {
                    collection.add(object);
                    // }
                } catch (ExcelImportException var19) {
                    if (!var19.getType().equals(ExcelImportEnum.VERIFY_ERROR)) {
                        throw new ExcelImportException(var19.getType(), var19);
                    }
                } catch (Exception e) {
                    log.error("excel import error , row num:" + readRow + ",obj:"
                            + ReflectionToStringBuilder.toString(object));
                    throw new RuntimeException(e);
                }
            }
            readRow++;
        }
        log.info("组装List耗时：" + (System.currentTimeMillis() - startListTime));
        return collection;
    }
    /**
     * 获取表格字段列名对应信息
     *
     * @param rows
     *            行集合
     * @param params
     *            导入参数
     * @param excelCollection
     *            导入数据
     * @return 表格字段列名对应信息
     */
    private Map<Integer, String> getTitleMap(Iterator<Row> rows, ImportParams params,
            List<ExcelCollectionParams> excelCollection) {
        Map<Integer, String> titlemap = new LinkedHashMap<Integer, String>();
        Iterator<Cell> cellTitle;
        String collectionName = null;
        ExcelCollectionParams collectionParams = null;
        Row row = null;
        for (int j = 0; j < params.getHeadRows(); ++j) {
            row = rows.next();
            if (row != null) {
                cellTitle = row.cellIterator();
                while (cellTitle.hasNext()) {
                    Cell cell = cellTitle.next();
                    String value = getKeyValue(cell);
                    value = value.replace("\n", "");
                    int i = cell.getColumnIndex();
                    // 用以支持重名导入
                    if (StringPoiUtils.isNotEmpty(value)) {
                        if (titlemap.containsKey(i)) {
                            collectionName = titlemap.get(i);
                            collectionParams = getCollectionParams(excelCollection, collectionName);
                            titlemap.put(i, collectionName + "_" + value);
                        } else if (StringPoiUtils.isNotEmpty(collectionName) && collectionParams != null
                                && collectionParams.getExcelParams().containsKey(collectionName + "_" + value)) {
                            titlemap.put(i, collectionName + "_" + value);
                        } else {
                            collectionName = null;
                            collectionParams = null;
                        }
                        if (StringPoiUtils.isEmpty(collectionName)) {
                            titlemap.put(i, value);
                        }
                    }
                }
            }
        }
        return titlemap;
    }
    /**
     * 获取这个名称对应的集合信息
     *
     * @param excelCollection
     * @param collectionName
     * @return
     */
    private ExcelCollectionParams getCollectionParams(List<ExcelCollectionParams> excelCollection,
            String collectionName) {
        for (ExcelCollectionParams excelCollectionParams : excelCollection) {
            if (collectionName.equals(excelCollectionParams.getExcelName())) {
                return excelCollectionParams;
            }
        }
        return null;
    }
    /**
     * Excel 导入 field 字段类型 Integer,Long,Double,Date,String,Boolean
     *
     * @param inputstream
     *            导入流
     * @param pojoClass
     *            对象类型
     * @param params
     *            导入参数
     * @return {@link ExcelImportResult}
     * @throws Exception
     */
    public ExcelImportResult importExcelByIs(InputStream inputstream, Class<?> pojoClass, ImportParams params)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Excel import start ,class is " + pojoClass);
        }
        List<T> result = new ArrayList<T>();
        Workbook book = null;
        boolean isXSSFWorkbook = true;
        // xls和xlsx
        // 初始化时候创建临时缓存目录，用于规避POI在并发写bug
        createPOIFilesDirectory();
        long startTime = System.currentTimeMillis();
        if (POIFSFileSystem.hasPOIFSHeader(inputstream)) {
            book = (inputstream == null) ? new HSSFWorkbook() : new HSSFWorkbook(new POIFSFileSystem(inputstream));
            isXSSFWorkbook = false;
        } else if (DocumentFactoryHelper.hasOOXMLHeader(inputstream)) {
            book = StreamingReader.builder().rowCacheSize(100) // 缓存到内存中的行数，默认是10
                    .bufferSize(4096) // 读取资源时，缓存到内存的字节大小，默认是1024
                    .open(inputstream); // 打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            // book = (inputstream == null) ? new SXSSFWorkbook(500) : new SXSSFWorkbook(new XSSFWorkbook(inputstream));
        }
        // "csv"格式导入和txt格式导入
        if (null == book
                && ("csv".equalsIgnoreCase(params.getSuffix()) || "txt".equalsIgnoreCase(params.getSuffix()))) {
            String splitStr = ",";
            if ("txt".equalsIgnoreCase(params.getSuffix())) {
                splitStr = params.getImportCustomRules().getSeparator();
            }
            BufferedReader readTxt = new BufferedReader(new InputStreamReader(inputstream, params.getEncoding()));
            String inStr = "";
            // 目标文件
            book = new HSSFWorkbook();
            Sheet targetSheet = book.createSheet();
            Row targetRow;
            Cell targetCell;
            int rowIndex = 0;
            while ((inStr = readTxt.readLine()) != null) { // 读取
                targetRow = targetSheet.createRow(rowIndex); // 创建新行
                String[] str = inStr.split(splitStr); // 得到列值
                for (int i = 0; i < str.length; i++) {
                    targetCell = targetRow.createCell(i); // 创建新列
                    targetCell.setCellValue(str[i]); // 赋值
                }
                rowIndex++;
            }
            readTxt.close();
            isXSSFWorkbook = false;
        }
        Map<String, PictureData> pictures = null;
        log.info("Workbook耗时：" + (System.currentTimeMillis() - startTime));
        ExcelImportResult importResult = new ExcelImportResult(result, verfiyFail, book);
        // this.createErrorCellStyle(book);
        try {
            for (int i = params.getStartSheetIndex(); i < params.getStartSheetIndex() + params.getSheetNum(); i++) {
                if (log.isDebugEnabled()) {
                    log.debug(" start to read excel by is ,startTime is " + System.currentTimeMillis());
                }
                // if (isXSSFWorkbook) {
                // pictures = PoiPublicUtil.getSheetPictrues07((SXSSFSheet) book.getSheetAt(i), (SXSSFWorkbook) book);
                // } else {
                // pictures = PoiPublicUtil.getSheetPictrues03((HSSFSheet) book.getSheetAt(i), (HSSFWorkbook) book);
                // }
                if (log.isDebugEnabled()) {
                    log.debug(" end to read excel by is ,endTime is " + System.currentTimeMillis());
                }
                if (null != params.getImportCustomRules()) {
                    result.addAll(importExcelCustom(result, book.getSheetAt(i), pojoClass, params, pictures));
                } else {
                    result.addAll(importExcel(result, book.getSheetAt(i), pojoClass, params, pictures));
                }
                if (log.isDebugEnabled()) {
                    log.debug(" end to read excel list by pos ,endTime is " + System.currentTimeMillis());
                }
                if (params.isReadSingleCell()) {
                    readSingleCell(importResult, book.getSheetAt(i), params);
                    if (log.isDebugEnabled()) {
                        log.debug(" read Key-Value ,endTime is " + System.currentTimeMillis());
                    }
                }
            }
            if (params.isNeedSave()) {
                saveThisExcel(params, pojoClass, isXSSFWorkbook, book);
            }
            importResult.setVerfiyFail(verfiyFail);
        } catch (Exception e) {
            throw e;
        } finally {
            inputstream.close();
            book.close();
        }
        return importResult;
    }
    /**
     * 保存字段值(获取值,校验值,追加错误信息)
     *
     * @param params
     *            导入参数
     * @param object
     *            导入对象
     * @param cell
     *            单元格
     * @param excelParams
     *            导入字段配置
     * @param titleString
     *            标题字段
     * @param row
     *            行号
     * @throws Exception
     */
    private void saveFieldValue(ImportParams params, Object object, Cell cell,
            Map<String, ExcelImportEntity> excelParams, String titleString, Row row) throws Exception {
        Object value = this.cellValueServer.getValue(params.getDataHanlder(), object, cell, excelParams, titleString);
        if (value instanceof String) {
            // 防止XSS攻击
            value = HtmlUtils.htmlEscape(((String) value).trim(), "UTF-8");
        }
        if (object instanceof Map) {
            if (params.getDataHanlder() != null) {
                params.getDataHanlder().setMapValue((Map) object, titleString, value);
            } else {
                ((Map) object).put(titleString, value);
            }
            // 2017年9月11日15:46:39 zhoujian
            params.addTitleKeys(titleString, titleString);
        } else {
            this.setValues(excelParams.get(titleString), object, value);
            // TODO: 集合未处理 zhouj 2017年9月11日16:32:03
            String beanName = excelParams.get(titleString).getMethod().getName().replaceFirst("set", "");
            String enName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1, beanName.length());
            params.addTitleKeys(titleString, enName);
        }
    }
    /**
     * 保存图片
     *
     * @param object
     *            导入对象
     * @param picId
     *            图片保存ID
     * @param excelParams
     *            导入字段配置
     * @param titleString
     *            标题字段
     * @param pictures
     *            图片属性
     * @param params
     *            导入参数
     * @throws Exception
     */
    private void saveImage(Object object, String picId, Map<String, ExcelImportEntity> excelParams, String titleString,
            Map<String, PictureData> pictures, ImportParams params) throws Exception {
        if (pictures != null) {
            PictureData image = pictures.get(picId);
            if (image == null) {
                return;
            }
            byte[] data = image.getData();
            String fileName = "pic" + Math.round(Math.random() * 1.0E11D);
            fileName = fileName + "." + PoiPublicUtil.getFileExtendName(data);
            if ((excelParams.get(titleString)).getSaveType() == 1) {
                String path = PoiPublicUtil
                        .getWebRootPath(this.getSaveUrl((ExcelImportEntity) excelParams.get(titleString), object));
                File savefile = new File(path);
                if (!savefile.exists()) {
                    savefile.mkdirs();
                }
                savefile = new File(path + "/" + fileName);
                FileOutputStream fos = new FileOutputStream(savefile);
                fos.write(data);
                fos.close();
                this.setValues(excelParams.get(titleString), object,
                        this.getSaveUrl((ExcelImportEntity) excelParams.get(titleString), object) + "/" + fileName);
            } else {
                this.setValues(excelParams.get(titleString), object, data);
            }
            // TODO: 集合未处理 zhouj 2017年9月11日16:32:03
            String beanName = excelParams.get(titleString).getMethod().getName().replaceFirst("set", "");
            String enName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1, beanName.length());
            params.addTitleKeys(titleString, enName);
        }
    }
    private void createErrorCellStyle(Workbook workbook) {
        this.errorCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(Font.COLOR_RED);
        this.errorCellStyle.setFont(font);
    }
    /**
     * 按照键值对的方式取得Excel里面的数据
     *
     * @param result
     *            导入结果
     * @param sheet
     *            工作表
     * @param params
     *            导入参数
     */
    private void readSingleCell(ExcelImportResult result, Sheet sheet, ImportParams params) {
        if (result.getMap() == null) {
            result.setMap(new HashMap<String, Object>());
        }
        for (int i = 0; i < params.getTitleRows() + params.getHeadRows() + params.getStartRows(); i++) {
            getSingleCellValueForRow(result, sheet.getRow(i), params);
        }
        for (int i = sheet.getLastRowNum() - params.getLastOfInvalidRow(); i < sheet.getLastRowNum(); i++) {
            getSingleCellValueForRow(result, sheet.getRow(i), params);
        }
    }
    private void getSingleCellValueForRow(ExcelImportResult result, Row row, ImportParams params) {
        for (int j = row.getFirstCellNum(), le = row.getLastCellNum(); j < le; j++) {
            String text = PoiCellUtil.getCellValue(row.getCell(j));
            if (StringPoiUtils.isNotBlank(text) && text.endsWith(params.getKeyMark())) {
                if (result.getMap().containsKey(text)) {
                    if (result.getMap().get(text) instanceof String) {
                        List<String> list = new ArrayList<String>();
                        list.add((String) result.getMap().get(text));
                        result.getMap().put(text, list);
                    }
                    ((List) result.getMap().get(text)).add(PoiCellUtil.getCellValue(row.getCell(++j)));
                } else {
                    result.getMap().put(text, PoiCellUtil.getCellValue(row.getCell(++j)));
                }
            }
        }
    }
    /**
     * 检查是不是合法的模板
     *
     * @param titleMap
     *            标题集合
     * @param excelParams
     *            导入字段配置
     * @param params
     *            导入参数
     * @param excelCollection
     *            导入数据集合
     */
    private void checkIsValidTemplate(Map<Integer, String> titlemap, Map<String, ExcelImportEntity> excelParams,
            ImportParams params, List<ExcelCollectionParams> excelCollection) {
        if (params.getImportFields() != null) {
            for (int i = 0, le = params.getImportFields().length; i < le; i++) {
                if (!titlemap.containsValue(params.getImportFields()[i])) {
                    throw new ExcelImportException(ExcelImportEnum.IS_NOT_A_VALID_TEMPLATE);
                }
            }
        } else {
            Collection<ExcelImportEntity> collection = excelParams.values();
            for (ExcelImportEntity excelImportEntity : collection) {
                if (excelImportEntity.isImportField() && !titlemap.containsValue(excelImportEntity.getName())) {
                    String msg=excelImportEntity.getName() + "必须有,但是没找到";
                    log.error(msg);
                    ExcelImportEnum excelImportEnum = ExcelImportEnum.IS_NOT_A_VALID_TEMPLATE;
                    excelImportEnum.setMsg(msg);
                    throw new ExcelImportException(excelImportEnum);
                }
            }
            for (int i = 0, le = excelCollection.size(); i < le; i++) {
                ExcelCollectionParams collectionparams = excelCollection.get(i);
                collection = collectionparams.getExcelParams().values();
                for (ExcelImportEntity excelImportEntity : collection) {
                    if (excelImportEntity.isImportField() && !titlemap
                            .containsValue(collectionparams.getExcelName() + "_" + excelImportEntity.getName())) {
                        throw new ExcelImportException(ExcelImportEnum.IS_NOT_A_VALID_TEMPLATE);
                    }
                }
            }
        }
    }
    /**
     * 自定义规则导入
     *
     * @param result
     *            导入集合
     * @param sheet
     *            工作表
     * @param pojoClass
     *            导入对象类型
     * @param params
     *            导入参数
     * @param pictures
     *            图片属性
     * @param <T>
     *            泛型，返回对象
     * @return 导入数据
     * @throws Exception
     */
    private <T> List<T> importExcelCustom(Collection<T> result, Sheet sheet, Class<?> pojoClass, ImportParams params,
            Map<String, PictureData> pictures) throws Exception {
        // if (sheet.getLastRowNum() > 20000) {
        // throw new ExcelImportException("每次导入数据不超过2万条");
        // }
        long startTime = System.currentTimeMillis();
        List collection = new ArrayList();
        Map<String, ExcelImportEntity> excelParams = Maps.newHashMap();
        List<ExcelCollectionParams> excelCollection = new ArrayList<ExcelCollectionParams>();
        String targetId = null;
        if (!Map.class.equals(pojoClass)) {
            Field[] rows = PoiPublicUtil.getClassFields(pojoClass);
            ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
            if (etarget != null) {
                targetId = etarget.value();
            }
            this.getAllExcelField(targetId, rows, excelParams, excelCollection, pojoClass, (List) null);
        }
        log.info("Field耗时：" + (System.currentTimeMillis() - startTime));
        Iterator<Row> rows = sheet.rowIterator();
        Row row = null;
        if (params.getTitleRows() > 0) {
            while (rows.hasNext()) {
                row = rows.next();
                if (row.getRowNum() == params.getTitleRows()) {
                    break;
                }
            }
        }
        Map<Integer, String> titlemap = getTitleMapCustom(rows, params, excelCollection, row);
        checkIsValidTemplate(titlemap, excelParams, params, excelCollection);
        // Object obj = PoiPublicUtil.createObject(pojoClass, targetId);
        String picId;
        int readRow = 0;
        boolean oneLine = (!rows.hasNext() && !params.getImportCustomRules().isHead()
                && row.getRowNum() == params.getTitleRows());
        long startListTime = System.currentTimeMillis();
        while (rows.hasNext() && (row == null || sheet.getLastRowNum() - row.getRowNum() > params.getLastOfInvalidRow())
                || (oneLine && readRow == 0)) {
            if (params.getReadRows() > 0 && readRow > params.getReadRows()) {
                break;
            }
            // TODO: 周健 过滤读取最后行数
            if (params.getEndRows() > 0 && readRow >= params.getEndRows() + params.getReadRows()) {
                break;
            }
            if ((!params.getImportCustomRules().isHead() && readRow != 0) || params.getImportCustomRules().isHead()
                    || row == null) {
                row = rows.next();
            }
            Object object = pojoClass.newInstance();
            try {
                for (int e = 0; e < row.getLastCellNum(); e++) {
                    Cell cell = row.getCell(e);
                    String titleString = titlemap.get(e);
                    if (excelParams.containsKey(titleString) || Map.class.equals(pojoClass)) {
                        if (excelParams.get(titleString) != null && (excelParams.get(titleString)).getType() == 2) {
                            picId = row.getRowNum() + "_" + e;
                            this.saveImage(object, picId, excelParams, titleString, pictures, params);
                        } else {
                            this.saveFieldValue(params, object, cell, excelParams, titleString, row);
                        }
                    }
                }
                for (ExcelCollectionParams param : excelCollection) {
                    addListContinue(object, param, row, titlemap, targetId, pictures, params);
                }
                // //判断对象属性是否都为空 2018年2月6日10:49:08 周健
                if (PoiValidationUtil.isNullObject(object, false)) {
                    readRow++;
                    continue;
                }
                collection.add(object);
            } catch (ExcelImportException var19) {
                if (!var19.getType().equals(ExcelImportEnum.VERIFY_ERROR)) {
                    throw new ExcelImportException(var19.getType(), var19);
                }
            } catch (Exception e) {
                log.error("excel import error , row num:" + readRow + ",obj:"
                        + ReflectionToStringBuilder.toString(object));
                throw new RuntimeException(e);
            }
            readRow++;
        }
        log.info("组装List耗时：" + (System.currentTimeMillis() - startListTime));
        return collection;
    }
    /**
     * 获取表格字段列名对应信息
     *
     * @param rows
     *            行集合
     * @param params
     *            导入参数
     * @param excelCollection
     *            导入数据
     * @param row
     *            当前导入行
     * @return 表格字段列名对应信息
     */
    private Map<Integer, String> getTitleMapCustom(Iterator<Row> rows, ImportParams params,
            List<ExcelCollectionParams> excelCollection, Row row) {
        ImportCustomRules importCustomRules = params.getImportCustomRules();
        Map<Integer, String> titlemap = new LinkedHashMap<Integer, String>();
        if ((null != importCustomRules.getColumnPositio() && !importCustomRules.isHead())
                || "txt".equalsIgnoreCase(params.getSuffix())) {
            if ("txt".equalsIgnoreCase(params.getSuffix())) {
                for (Map.Entry<String, Integer> entry : importCustomRules.getSeparatorPosition().entrySet()) {
                    titlemap.put(entry.getValue() - 1, entry.getKey());
                }
            } else {
                for (Map.Entry<String, String> entry : importCustomRules.getColumnPositio().entrySet()) {
                    int colIndex = ExcelImportUtil.excelColStrToNum(entry.getValue(), entry.getValue().length());
                    titlemap.put(colIndex - 1, entry.getKey());
                }
            }
        } else {
            Iterator<Cell> cellTitle;
            String collectionName = null;
            ExcelCollectionParams collectionParams = null;
            for (int j = 0; j < params.getHeadRows(); ++j) {
                if (j != 0 || row == null) {
                    row = rows.next();
                }
                if (row != null) {
                    cellTitle = row.cellIterator();
                    while (cellTitle.hasNext()) {
                        Cell cell = cellTitle.next();
                        String value = getKeyValue(cell);
                        value = value.replace("\n", "");
                        // begin 自定义导入规则过滤
                        if (null != params.getImportCustomRules()) {
                            List columns = Arrays.asList(importCustomRules.getColumns());
                            if (!columns.contains(value)) {
                                continue;
                            }
                        }
                        // end
                        int i = cell.getColumnIndex();
                        // 用以支持重名导入
                        if (StringPoiUtils.isNotEmpty(value)) {
                            if (titlemap.containsKey(i)) {
                                collectionName = titlemap.get(i);
                                collectionParams = getCollectionParams(excelCollection, collectionName);
                                titlemap.put(i, collectionName + "_" + value);
                            } else if (StringPoiUtils.isNotEmpty(collectionName) && collectionParams != null
                                    && collectionParams.getExcelParams().containsKey(collectionName + "_" + value)) {
                                titlemap.put(i, collectionName + "_" + value);
                            } else {
                                collectionName = null;
                                collectionParams = null;
                            }
                            if (StringPoiUtils.isEmpty(collectionName)) {
                                titlemap.put(i, value);
                            }
                        }
                    }
                }
            }
        }
        return titlemap;
    }
    /**
     * 初始化时候创建临时缓存目录，用于规避POI在并发写bug
     */
    public static void createPOIFilesDirectory() {
        final String JAVA_IO_TMPDIR = "java.io.tmpdir";
        final String POIFILES = "poifiles";
        String tmpDir = System.getProperty(JAVA_IO_TMPDIR);
        if (tmpDir == null) {
            throw new RuntimeException(
                    "Systems temporary directory not defined - set the -D" + JAVA_IO_TMPDIR + " jvm property!");
        }
        File directory = new File(tmpDir, POIFILES);
        if (!directory.exists()) {
            syncCreatePOIFilesDirectory(directory);
        }
    }
    /**
     * 创建目录
     *
     * @param directory
     */
    private static synchronized void syncCreatePOIFilesDirectory(File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
