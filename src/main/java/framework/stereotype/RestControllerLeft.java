package framework.stereotype;

import java.lang.annotation.*;

/**
 * 这个注解会将被标记的 controller 内部返回的所有方法均返回 json 处理视图数据
 * json 序列化依赖于特定的解析库实现
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/17 22:49
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestControllerLeft {
  /**
   * 路径列表, 要求以: <code>/</code> 形式开头, 如: <code>/qux</code>
   */
  String value() default "";
}
