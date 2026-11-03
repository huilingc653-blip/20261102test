package com.bytter.system.web.controller;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiresPermissions {
	/**
	 * @return the Spring-EL expression to be evaluated before invoking the protected
	 * method
	 */
	String value();
}
