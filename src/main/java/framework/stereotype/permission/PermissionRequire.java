package framework.stereotype.permission;

import java.lang.annotation.*;

/**
 * 此注解用来修饰所访问方法所需要的权限, 通过角色字符方式, 提供给注解处理器以进行对方法的处理
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/19 19:43
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionRequire {
  String[] value() default {};
}
