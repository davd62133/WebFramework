package Annotationes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface WebGet {
    String value() default "/";
}
