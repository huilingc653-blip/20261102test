package com.bytter.cloud.common.web.format;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 * 反序列化时间，支持多种时间格式
 *
 * @author zhoul 2021-05-27
 * @since 1.0.0
 */
@Slf4j
public class CustomDateFormatDeserializer extends DateDeserializers.DateDeserializer {
    private static final String[] PATTERNS = new String[]{
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm",
            "HH:mm",
            "HH:mm:ss",
            "HH:mm:ss.SSS",
            "yyyy-MM-dd",
    };
    public CustomDateFormatDeserializer() {
        super();
    }
    public CustomDateFormatDeserializer(DateDeserializers.DateDeserializer base, DateFormat df, String formatString) {
        super(base, df, formatString);
    }
    @Override
    protected DateDeserializers.DateDeserializer withDateFormat(DateFormat df, String formatString) {
        return new CustomDateFormatDeserializer(this, df, formatString);
    }
    @Override
    protected java.util.Date _parseDate(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            String str = p.getText().trim();
            for (String pattern : PATTERNS) {
                if (pattern.length() == str.length()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    return simpleDateFormat.parse(str);
                }
            }
            return super._parseDate(p, ctxt);
        } catch (Exception e) {
            log.warn("custom parse date fail:", e);
            return super._parseDate(p, ctxt);
        }
    }
}
