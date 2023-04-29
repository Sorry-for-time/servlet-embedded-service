package framework.stereotype.field;

import java.lang.annotation.*;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/24 7:53
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ResourceLeft {
  String value() default "byName";
  String mode() default "singleton";
}
