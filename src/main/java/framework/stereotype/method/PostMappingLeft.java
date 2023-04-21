package framework.stereotype.methodMapping;

import java.lang.annotation.*;

/**
 * POST 请求注解
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/17 22:57
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostMappingLeft {
  /**
   * 路径列表, 要求以: <code>/</code> 形式开头, 如: <code>/foo</code>
   */
  String value() default "";
}
