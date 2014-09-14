package jfxtras.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Implements
{
   /**
   * The set of interface that the specified method implements
   */
   Class[] interfaces();
}
