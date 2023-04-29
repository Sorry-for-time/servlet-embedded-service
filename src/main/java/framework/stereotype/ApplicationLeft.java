package framework.stereotype;

import java.lang.annotation.*;

/**
 * 被此注解标记的类会被当成一个入口启动类, but 实际的功能还没去做...
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/19 10:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface ApplicationLeft {
}
