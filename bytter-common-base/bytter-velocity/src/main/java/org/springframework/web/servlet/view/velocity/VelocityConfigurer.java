/*
 * Copyright 2002-2012 the original author or authors.
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
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.web.context.ServletContextAware;
import javax.servlet.ServletContext;
import java.io.IOException;
public class VelocityConfigurer extends VelocityEngineFactory
        implements VelocityConfig, InitializingBean, ResourceLoaderAware, ServletContextAware {
    /** the name of the resource loader for Spring's bind macros */
    private static final String SPRING_MACRO_RESOURCE_LOADER_NAME = "springMacro";
    /** the key for the class of Spring's bind macro resource loader */
    private static final String SPRING_MACRO_RESOURCE_LOADER_CLASS = "springMacro.resource.loader.class";
    /** the name of Spring's default bind macro library */
    private static final String SPRING_MACRO_LIBRARY = "org/springframework/web/servlet/view/velocity/spring.vm";
    private VelocityEngine velocityEngine;
    private ServletContext servletContext;
    /**
     * Set a pre-configured VelocityEngine to use for the Velocity web configuration: e.g. a shared one for web and
     * email usage, set up via {@link org.springframework.ui.velocity.VelocityEngineFactoryBean}.
     * <p>
     * Note that the Spring macros will <i>not</i> be enabled automatically in case of an external VelocityEngine passed
     * in here. Make sure to include {@code spring.vm} in your template loader path in such a scenario (if there is an
     * actual need to use those macros).
     * <p>
     * If this is not set, VelocityEngineFactory's properties (inherited by this class) have to be specified.
     */
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    /**
     * Initialize VelocityEngineFactory's VelocityEngine if not overridden by a pre-configured VelocityEngine.
     * 
     * @see #createVelocityEngine
     * @see #setVelocityEngine
     */
    @Override
    public void afterPropertiesSet() throws IOException, VelocityException {
        if (this.velocityEngine == null) {
            this.velocityEngine = createVelocityEngine();
        }
    }
    /**
     * Provides a ClasspathResourceLoader in addition to any default or user-defined loader in order to load the spring
     * Velocity macros from the class path.
     * 
     * @see ClasspathResourceLoader
     */
    @Override
    protected void postProcessVelocityEngine(VelocityEngine velocityEngine) {
        velocityEngine.setApplicationAttribute(ServletContext.class.getName(), this.servletContext);
        velocityEngine.setProperty(SPRING_MACRO_RESOURCE_LOADER_CLASS, ClasspathResourceLoader.class.getName());
        velocityEngine.addProperty(VelocityEngine.RESOURCE_LOADER, SPRING_MACRO_RESOURCE_LOADER_NAME);
        velocityEngine.addProperty(VelocityEngine.VM_LIBRARY, SPRING_MACRO_LIBRARY);
        if (logger.isInfoEnabled()) {
            logger.info("ClasspathResourceLoader with name '" + SPRING_MACRO_RESOURCE_LOADER_NAME
                    + "' added to configured VelocityEngine");
        }
    }
    @Override
    public VelocityEngine getVelocityEngine() {
        return this.velocityEngine;
    }
}
