package framework.stereotype.methodMapping;

import java.lang.annotation.*;

/**
 * GET 请求注解
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/17 22:55
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMappingLeft {
  /**
   * 路径列表, 要求以: <code>/</code> 形式开头, 如: <code>/qux</code>
   */
  String value() default "";
}
