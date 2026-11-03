package com.bytter.poi.exception.word;
import com.bytter.poi.exception.word.enmus.WordExportEnum;
/**
 * word导出枚举类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class WordExportException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public WordExportException() {
    }
    public WordExportException(String msg) {
        super(msg);
    }
    public WordExportException(WordExportEnum exception) {
        super(exception.getMsg());
    }
}
