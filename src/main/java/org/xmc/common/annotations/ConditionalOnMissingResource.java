package org.xmc.common.annotations;

import org.springframework.context.annotation.Conditional;
import org.xmc.common.OnMissingResourceCondition;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMissingResourceCondition.class)
public @interface ConditionalOnMissingResource {
    String[] resources() default {};
}
