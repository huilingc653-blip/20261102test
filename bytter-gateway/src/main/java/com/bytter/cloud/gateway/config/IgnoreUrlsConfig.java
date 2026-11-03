package com.bytter.cloud.gateway.config;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;
/**
 * 网关白名单配置
 * Created by macro on 2020/6/17.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix="gate.ignore.authentication.startwith")
public class IgnoreUrlsConfig {
    private  String ignoreUrls;
}
