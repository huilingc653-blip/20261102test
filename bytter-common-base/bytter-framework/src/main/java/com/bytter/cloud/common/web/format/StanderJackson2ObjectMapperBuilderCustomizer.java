package com.bytter.cloud.common.web.format;
import com.bytter.cloud.common.core.util.DatePattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/**
 * 用于统一系统的时间为：yyyy-MM-dd HH:mm:ss.SSS
 * date和LocalDateTime
 *
 * @author zhoul 2021-05-26
 * @since 1.0.0
 */
public class StanderJackson2ObjectMapperBuilderCustomizer implements Jackson2ObjectMapperBuilderCustomizer {
    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder.locale(Locale.CHINA);
        jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        //为null字段的属性不做序列化
        jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略未知字段
        jacksonObjectMapperBuilder.failOnUnknownProperties(false);
        //下面为时间格式化序列化和反序列化统一配置
        jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class,
                new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_MS_FORMAT));
        jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class,
                new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_MS_FORMAT));
        jacksonObjectMapperBuilder.serializerByType(Date.class, new DateSerializer(false, new SimpleDateFormat(DatePattern.NORM_DATETIME_MS_PATTERN)));
        //覆盖默认的Date反序列化，第一个参数为需要反序列化的类，第二个为具体的序列化格式
        jacksonObjectMapperBuilder.deserializerByType(Date.class, new CustomDateFormatDeserializer());
    }
}
