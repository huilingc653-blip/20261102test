package com.bytter.system.velocity.tags;

import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.velocity.tools.config.DefaultKey;

@DefaultKey("configTag")
public class ConfigTag {
    private final IConfigService configService;

    public ConfigTag() {
        this.configService = ApplicationContextUtil.getContext().getBean("configServiceI", IConfigService.class);
    }

    public String getConfig(String type, String name) {
        ConfigDTO config = configService.getConfig(type, name);
        String result = "";
        if (null != config) {
            result = config.getValue();
        }
        return result;
    }

    public String getGlobalConfig(String type, String name) {
        ConfigDTO config = configService.getGlobalConfig(type, name);
        String result = "";
        if (null != config) {
            result = config.getValue();
        }
        return result;
    }
}
