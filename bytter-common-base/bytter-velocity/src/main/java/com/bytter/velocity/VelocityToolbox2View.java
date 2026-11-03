package com.bytter.velocity;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.ToolboxFactory;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
/**
 * Created by zhouj on 2017/4/18.
 */
@SuppressWarnings("deprecation")
public class VelocityToolbox2View extends VelocityToolboxView {
    private static ToolboxFactory toolboxFactory;
    private static ToolManager toolManager;
    public VelocityToolbox2View() {
    }
    public ToolManager getToolManager() {
        return toolManager;
    }
    @Override
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ViewToolContext vtc;
        vtc = new ViewToolContext(getVelocityEngine(), request, response, this.getServletContext());
        vtc.putAll(model);
        if (toolManager == null) {
            String configLocation = getToolboxConfigLocation();
            if (StringUtils.isNotBlank(configLocation)) {
                toolManager = new ToolManager();
                VelocityUtils.toolManagerInit(toolManager, configLocation, getVelocityEngine());
            }
        }
        toolboxFactory = toolManager.getToolboxFactory();
        if (toolboxFactory.hasTools(Scope.REQUEST)) {
            vtc.addToolbox(toolboxFactory.createToolbox(Scope.REQUEST));
        }
        if (toolboxFactory.hasTools(Scope.APPLICATION)) {
            vtc.addToolbox(toolboxFactory.createToolbox(Scope.APPLICATION));
        }
        if (toolboxFactory.hasTools(Scope.SESSION)) {
            vtc.addToolbox(toolboxFactory.createToolbox(Scope.SESSION));
        }
        return vtc;
    }
}
