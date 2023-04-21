package framework.stereotype.params;

import java.lang.annotation.*;

/**
 * 这个注解会尝试将 HttpServletRequest 请求头中的 json 请求数据转化提取到为方法里所标注的对象数据
 * 这个实现依赖于 jackson 的序列化
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/18 16:57
 */
@Documented
@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostBody {
}
