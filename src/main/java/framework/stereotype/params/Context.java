package framework.stereotype.params;

import java.lang.annotation.*;

/**
 * 获取当前的 servlet 上下文对象
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/24 8:21
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Context {
}
