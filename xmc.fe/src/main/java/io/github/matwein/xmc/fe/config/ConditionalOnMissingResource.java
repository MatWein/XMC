package io.github.matwein.xmc.fe.config;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMissingResourceCondition.class)
public @interface ConditionalOnMissingResource {
    String[] resources() default {};
}
