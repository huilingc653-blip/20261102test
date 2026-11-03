package com.bytter.velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.config.ConfigurationUtils;
import org.apache.velocity.tools.config.FactoryConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by zhoul on 2017/11/24.
 */
public class VelocityUtils {
    private static Context context;
    private static BytterVelocityViewResolver bytterVelocityViewResolver;
    private static VelocityConfigurer velocityConfigurer;
    private static VelocityEngine velocityEngine;
    public VelocityUtils(BytterVelocityViewResolver bytterVelocityViewResolver, VelocityConfigurer configurer) {
        VelocityUtils.bytterVelocityViewResolver = bytterVelocityViewResolver;
        VelocityUtils.velocityConfigurer = configurer;
        toolContextInit();
    }
    /**
     * 初始化工具上下文
     */
    private void toolContextInit() {
        if (context != null) {
            return;
        }
        ToolManager toolManager = new ToolManager();
        String configLocation = bytterVelocityViewResolver.getToolboxConfigLocation();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(configLocation)) {
            toolManagerInit(toolManager, configLocation, getVelocityEngine());
        }
        context = toolManager.createContext();
    }
    static void toolManagerInit(ToolManager toolManager, String configLocation, VelocityEngine velocityEngine) {
        toolManager.setVelocityEngine(velocityEngine);
        String[] configLocations = configLocation.split(",");
        FactoryConfiguration configuration = new FactoryConfiguration();
        for (String location : configLocations) {
            if (Objects.nonNull(ConfigurationUtils.findInClasspath(location))) {
                configuration.addConfiguration(ConfigurationUtils.findInClasspath(location));
            }
        }
        toolManager.configure(configuration);
    }
    public static VelocityEngine getVelocityEngine() {
        if (velocityEngine == null) {
            velocityEngine = velocityConfigurer.getVelocityEngine();
        }
        return velocityEngine;
    }
    private static Context getContext() {
        return context;
    }
    public static String evaluate(Map<String, Object> variables, String logTag, String instring) {
        VelocityEngine velocityEngine = getVelocityEngine();
        Context context = getContext();
        if (!StringUtils.isEmpty(variables)) {
            for (Map.Entry<String, Object> variable : variables.entrySet()) {
                context.put(variable.getKey(), variable.getValue());
            }
        }
        StringWriter stringWriter = new StringWriter();
        velocityEngine.evaluate(context, stringWriter, logTag, instring);
        return stringWriter.toString();
    }
    /**
     * 自定义渲染模板
     * 
     * @param template
     *            模版
     * @param params
     *            参数
     * @return
     */
    public static String processTemplate(String template, Map<String, Object> params) {
        if (template == null || params == null)
            return null;
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("\\$\\{\\w+\\}").matcher(template);
        while (m.find()) {
            String param = m.group();
            Object value = params.get(param.substring(2, param.length() - 1));
            m.appendReplacement(sb, value == null ? "" : value.toString());
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
