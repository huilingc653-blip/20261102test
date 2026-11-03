package com.bytter.cloud.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ConfigurationProperties("h5-router")
public class H5ResourcesProperties {

    //key为请求地址 value为资源
    private Map<String, String> staticResources = new LinkedHashMap<>();
}
