package framework.util;

/**
 * 用于处理三元集的函数式接口
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/19 18:00
 */
@FunctionalInterface
public interface AnnotationMatchedHandler<T, O, E> {
  void doAction(T t, O o, E e);
}
