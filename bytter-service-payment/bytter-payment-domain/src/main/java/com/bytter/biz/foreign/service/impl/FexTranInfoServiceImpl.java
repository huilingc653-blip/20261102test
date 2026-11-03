package com.bytter.biz.foreign.service.impl;

import com.bytter.biz.foreign.entity.FexTranInfo;
import com.bytter.biz.foreign.service.FexTranInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;

@Service("fexTranInfoServiceImpl")
@Transactional
public class FexTranInfoServiceImpl {
    @Autowired
    private FexTranInfoService fexTranInfoService;

    public Annotation[] getMultiInjectAnnotations(Annotation annotation) {
        return new Annotation[0];
    }

    public String getInjectAttributeAnnotation(Annotation annotation) {
        return null;
    }

    public String returnInjectValue(String injectFieldName, Object value, Annotation annotation) {
        String tranCode = value.toString();
        FexTranInfo fexTranInfo = fexTranInfoService.getByTranCode(tranCode);
        return fexTranInfo == null ? "" : fexTranInfo.getTranName();
    }
}
