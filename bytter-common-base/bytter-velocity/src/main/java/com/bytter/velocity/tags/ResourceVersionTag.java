package com.bytter.velocity.tags;
import org.apache.velocity.tools.config.DefaultKey;
import org.springframework.util.StringUtils;
/**
 * Created by zhoul on 2017/9/25.
 */
@DefaultKey("resourceVersion")
public class ResourceVersionTag extends StringUtils {
    static Long resourceVersion = System.currentTimeMillis();
    public static Long get() {
        return resourceVersion;
    }
}
