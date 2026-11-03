/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.web.servlet.view.velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
public class VelocityToolboxView extends VelocityView {
    private String toolboxConfigLocation;
    /**
     * Set a Velocity Toolbox config location, for example "/WEB-INF/toolbox.xml", to automatically load a Velocity
     * Tools toolbox definition file and expose all defined tools in the specified scopes. If no config location is
     * specified, no toolbox will be loaded and exposed.
     * <p>
     * The specified location string needs to refer to a ServletContext resource, as expected by ServletToolboxManager
     * which is part of the view package of Velocity Tools.
     * 
     * @see org.apache.velocity.tools.view.servlet.ServletToolboxManager#getInstance
     */
    public void setToolboxConfigLocation(String toolboxConfigLocation) {
        this.toolboxConfigLocation = toolboxConfigLocation;
    }
    /**
     * Return the Velocity Toolbox config location, if any.
     */
    protected String getToolboxConfigLocation() {
        return this.toolboxConfigLocation;
    }
    /**
     * Overridden to create a ChainedContext, which is part of the view package of Velocity Tools, as special context.
     * ChainedContext is needed for initialization of ViewTool instances.
     * 
     * @see #initTool
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // Create a ChainedContext instance.
        org.apache.velocity.tools.view.context.ChainedContext velocityContext = new org.apache.velocity.tools.view.context.ChainedContext(
                new VelocityContext(model), getVelocityEngine(), request, response, getServletContext());
        // Load a Velocity Tools toolbox, if necessary.
        if (getToolboxConfigLocation() != null) {
            org.apache.velocity.tools.view.ToolboxManager toolboxManager = org.apache.velocity.tools.view.servlet.ServletToolboxManager
                    .getInstance(getServletContext(), getToolboxConfigLocation());
            Map toolboxContext = toolboxManager.getToolbox(velocityContext);
            velocityContext.setToolbox(toolboxContext);
        }
        return velocityContext;
    }
    /**
     * Overridden to check for the ViewContext interface which is part of the view package of Velocity Tools. This
     * requires a special Velocity context, like ChainedContext as set up by {@link #createVelocityContext} in this
     * class.
     */
    @Override
    protected void initTool(Object tool, Context velocityContext) throws Exception {
        // Velocity Tools 1.3: a class-level "init(Object)" method.
        Method initMethod = ClassUtils.getMethodIfAvailable(tool.getClass(), "init", Object.class);
        if (initMethod != null) {
            ReflectionUtils.invokeMethod(initMethod, tool, velocityContext);
        }
    }
}
