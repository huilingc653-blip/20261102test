package com.bytter.system.velocity.tags;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.system.api.Ii18nService;
import org.apache.velocity.tools.config.DefaultKey;
import org.springframework.beans.factory.annotation.Autowired;
@DefaultKey("i18nTag")
public class I18nTag {
    private Ii18nService ii18nService;
    public I18nTag() {
        this.ii18nService = ApplicationContextUtil.getContext().getBean("i18nServiceI", Ii18nService.class);
    }
    /**
     * 获取业务基础数据国际化
     *
     * @param keyId 业务基础数据ID
     * @return 业务基础数据国际化
     */
    public String getBusinessBaseContext(String keyId) {
        return ii18nService.getBusinessBaseContext(keyId);
    }
}
