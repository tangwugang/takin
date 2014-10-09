package org.takinframework.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 获取数据库表名
 * @author 
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JwebEntityTitle {
	  String name();
}
