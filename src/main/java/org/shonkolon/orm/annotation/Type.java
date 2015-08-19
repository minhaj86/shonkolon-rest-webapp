package org.shonkolon.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.shonkolon.orm.DBColumn;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) 
public @interface Type {

	public DBColumn value();

}
