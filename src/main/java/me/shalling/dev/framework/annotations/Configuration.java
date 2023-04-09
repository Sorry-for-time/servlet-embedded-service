package me.shalling.dev.framework.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;

/**
 * 配置扫描器注解
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/8 22:02
 */

@Documented
@Inherited
public @interface Configuration {
  String value() default "./";
}
