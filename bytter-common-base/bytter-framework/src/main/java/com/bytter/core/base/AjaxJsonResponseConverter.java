package com.bytter.core.base;

import com.bytter.core.common.context.LocalMessageService;
import com.bytter.core.common.model.json.AjaxJson;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

/**
 * 兼容老T6 AjaxJson返回封装国际化问题,重写MappingJackson2HttpMessageConverter
 */
public class AjaxJsonResponseConverter extends MappingJackson2HttpMessageConverter {

    @Autowired
    private LocalMessageService localMessageService;

    public AjaxJsonResponseConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        super.writePrefix(generator, object);
        if (!(object instanceof AjaxJson)) {
            return;
        }
        AjaxJson ajaxJson = (AjaxJson) object;
        //枚举转换
        ajaxJson.setMsg(localMessageService.getLangByAjaxJson(ajaxJson));
    }
}
