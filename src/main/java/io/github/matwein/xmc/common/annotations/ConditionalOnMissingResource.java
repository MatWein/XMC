package io.github.matwein.xmc.common.annotations;

import org.springframework.context.annotation.Conditional;
import io.github.matwein.xmc.common.OnMissingResourceCondition;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMissingResourceCondition.class)
public @interface ConditionalOnMissingResource {
    String[] resources() default {};
}
