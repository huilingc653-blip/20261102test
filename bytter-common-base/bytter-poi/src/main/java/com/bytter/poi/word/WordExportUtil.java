package com.bytter.poi.word;
import com.bytter.poi.word.parse.ParseWord07;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.util.Map;
/**
 * Created by zhouj on 2017/6/2.
 */
public final class WordExportUtil {
    private WordExportUtil() {
    }
    public static XWPFDocument exportWord07(String url, Map<String, Object> map) throws Exception {
        return (new ParseWord07()).parseWord(url, map);
    }
    public static void exportWord07(XWPFDocument document, Map<String, Object> map) throws Exception {
        (new ParseWord07()).parseWord(document, map);
    }
}
