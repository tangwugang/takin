package org.takinframework.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Transactional;

/**
 * 自定义相应多数据源对应的事物处理
 * <bean id="tran_1"
 *	class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
	p:dataSource-ref="dataSource">
	<qualifier value="tran_1"/>
   * </bean>
	<bean id="tran_2"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
		p:dataSource-ref="dataSource">
		<qualifier value="tran_2"/>
	*</bean>
 * @author twg
 *
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional("tran_1")
public @interface CustomerTransactional {

}
