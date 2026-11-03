package com.bytter.poi.excel.entity;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.handler.inter.ICustomVerifyHandler;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import java.util.LinkedHashMap;
/**
 * 导入参数
 * 
 * @author zhouj
 * @since 2017/6/2.
 */
public class ImportParams extends ExcelBaseParams {
    private int titleRows = 0;// 表格标题行数,默认0
    private int headRows = 1;// 表头行数,默认1
    private int startRows = 0;// 段真正值和列标题之间的距离 默认0
    private int endRows = 0;// 读到哪一行
    private int keyIndex = 0;// 主键设置,如何这个cell没有值,就跳过 或者认为这个是list的下面的值
    private int startSheetIndex = 0;// 开始读取的sheet位置,默认为0
    private int sheetNum = 1;// 上传表格需要读取的sheet 数量,默认为1
    private boolean needSave = false;// 否需要保存上传的Excel,默认为false
    private boolean needVerfiy = false;// 是否需要校验上传的Excel,默认为false
    /**
     * 保存上传的Excel目录,默认是 如 TestEntity这个类保存路径就是 upload/excelUpload/Test/yyyyMMddHHmss_***** 保存名称上传时间_五位随机数
     */
    private String saveUrl = "upload/excelUpload";
    private IExcelVerifyHandler verifyHanlder;// 校验处理接口
    private IUserExcelVerifyHandler userVerifyHandler;// 校验处理接口
    private int lastOfInvalidRow = 0;// 最后的无效行数
    private int readRows = 0;// 手动控制读取的行数
    private String[] importFields;// 导入时校验数据模板,是不是正确的Excel
    private String keyMark = ":";// Key-Value 读取标记,以这个为Key,后面一个Cell 为Value,多个改为ArrayList
    /**
     * 按照Key-Value 规则读取全局扫描Excel,但是跳过List读取范围提升性能 仅仅支持titleRows + headRows + startRows 以及 lastOfInvalidRow
     */
    private boolean readSingleCell = false;
    // 读取表头与对象的关联对应关系
    private boolean readTitleKey = false;
    // 临时保存表头和属性关系数据
    private LinkedHashMap<String, String> titleKey;
    // 临时保存用户信息
    private Object currentUser;
    // 临时第二个参数
    private Object params;
    // 自定义规则
    private ImportCustomRules importCustomRules;
    private ICustomVerifyHandler iCustomVerifyHandler;// 校验处理接口
    /**
     * 文件后缀
     */
    private String suffix;
    /**
     * txt编码格式
     */
    private String encoding;
    public ImportParams() {
    }
    public ImportParams(IExcelVerifyHandler handler) {
        super();
        this.verifyHanlder = handler;
    }
    public ImportParams addVerifyHandler(Class<?> handler) {
        try {
            this.verifyHanlder = (IExcelVerifyHandler) handler.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public void addTitleKeys(String title, String key) {
        if (this.isReadTitleKey()) {
            if (this.getTitleKey() == null) {
                this.setTitleKey(new LinkedHashMap<String, String>());
            }
            this.getTitleKey().put(title, key);
        }
    }
    public int getHeadRows() {
        return this.headRows;
    }
    public int getKeyIndex() {
        return this.keyIndex;
    }
    public String getSaveUrl() {
        return this.saveUrl;
    }
    public int getSheetNum() {
        return this.sheetNum;
    }
    public int getStartRows() {
        return this.startRows;
    }
    public int getTitleRows() {
        return this.titleRows;
    }
    public IExcelVerifyHandler getVerifyHanlder() {
        return this.verifyHanlder;
    }
    public boolean isNeedSave() {
        return this.needSave;
    }
    public void setHeadRows(int headRows) {
        this.headRows = headRows;
    }
    public void setKeyIndex(int keyIndex) {
        this.keyIndex = keyIndex;
    }
    public void setNeedSave(boolean needSave) {
        this.needSave = needSave;
    }
    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }
    public void setSheetNum(int sheetNum) {
        this.sheetNum = sheetNum;
    }
    public void setStartRows(int startRows) {
        this.startRows = startRows;
    }
    public void setTitleRows(int titleRows) {
        this.titleRows = titleRows;
    }
    public void setVerifyHanlder(IExcelVerifyHandler verifyHanlder) {
        this.verifyHanlder = verifyHanlder;
    }
    public int getLastOfInvalidRow() {
        return this.lastOfInvalidRow;
    }
    public void setLastOfInvalidRow(int lastOfInvalidRow) {
        this.lastOfInvalidRow = lastOfInvalidRow;
    }
    public int getStartSheetIndex() {
        return startSheetIndex;
    }
    public void setStartSheetIndex(int startSheetIndex) {
        this.startSheetIndex = startSheetIndex;
    }
    public boolean isNeedVerfiy() {
        return needVerfiy;
    }
    public void setNeedVerfiy(boolean needVerfiy) {
        this.needVerfiy = needVerfiy;
    }
    public int getReadRows() {
        return readRows;
    }
    public void setReadRows(int readRows) {
        this.readRows = readRows;
    }
    public String[] getImportFields() {
        return importFields;
    }
    public void setImportFields(String[] importFields) {
        this.importFields = importFields;
    }
    public String getKeyMark() {
        return keyMark;
    }
    public void setKeyMark(String keyMark) {
        this.keyMark = keyMark;
    }
    public boolean isReadSingleCell() {
        return readSingleCell;
    }
    public void setReadSingleCell(boolean readSingleCell) {
        this.readSingleCell = readSingleCell;
    }
    public boolean isReadTitleKey() {
        return readTitleKey;
    }
    public void setReadTitleKey(boolean readTitleKey) {
        this.readTitleKey = readTitleKey;
    }
    public LinkedHashMap<String, String> getTitleKey() {
        return titleKey;
    }
    public void setTitleKey(LinkedHashMap<String, String> titleKey) {
        this.titleKey = titleKey;
    }
    public int getEndRows() {
        return endRows;
    }
    public void setEndRows(int endRows) {
        this.endRows = endRows;
    }
    public Object getUserId() {
        return currentUser;
    }
    public void setCurrentUser(Object currentUser) {
        this.currentUser = currentUser;
    }
    public IUserExcelVerifyHandler getUserVerifyHandler() {
        return userVerifyHandler;
    }
    public void setUserVerifyHandler(IUserExcelVerifyHandler userVerifyHandler) {
        this.userVerifyHandler = userVerifyHandler;
    }
    public Object getParams() {
        return params;
    }
    public void setParams(Object params) {
        this.params = params;
    }
    public ImportCustomRules getImportCustomRules() {
        return importCustomRules;
    }
    public void setImportCustomRules(ImportCustomRules importCustomRules) {
        this.importCustomRules = importCustomRules;
    }
    public ICustomVerifyHandler getiCustomVerifyHandler() {
        return iCustomVerifyHandler;
    }
    public void setiCustomVerifyHandler(ICustomVerifyHandler iCustomVerifyHandler) {
        this.iCustomVerifyHandler = iCustomVerifyHandler;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public String getEncoding() {
        return encoding;
    }
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
