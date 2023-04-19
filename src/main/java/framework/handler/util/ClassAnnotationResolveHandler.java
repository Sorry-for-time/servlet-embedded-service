package framework.handler.util;

import framework.util.AnnotationMatchedHandler;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于解析类上被标注注解的类
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/18 15:44
 */
public final class ClassAnnotationResolveHandler {
  private ClassAnnotationResolveHandler() {
  }

  /**
   * 返回一个收集所有匹配规则的 class 的 map 视图
   *
   * @param classFullNameList 完整类路径列表
   * @param matchHandler      匹配规则执行函数
   * @param <T>               map 视图的键类型
   * @return 所有匹配规则的 class 的 map 视图
   */
  public static <T> Map<T, Class<?>> getAllTagMatchedClassMap(
    List<String> classFullNameList,
    AnnotationMatchedHandler<Class<?>, Annotation[], Map<T, Class<?>>> matchHandler
  ) {
    final var classHashMap = new ConcurrentHashMap<T, Class<?>>();

    classFullNameList.forEach(item -> {
      try {
        Class<?> aClass = Class.forName(item);
        Annotation[] annotations = aClass.getAnnotations();
        matchHandler.doAction(aClass, annotations, classHashMap);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    });

    return classHashMap;
  }
}
