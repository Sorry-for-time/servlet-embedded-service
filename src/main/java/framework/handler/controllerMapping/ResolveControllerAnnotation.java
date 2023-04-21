package framework.annotationHandler.controllerMapping;

import framework.scanner.PackageClassScanner;
import framework.stereotype.RestControllerLeft;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static framework.annotationHandler.util.ClassAnnotationResolveHandler.getAllTagMatchedClassMap;

/**
 * RestControllerLeft 注解处理器
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/19 18:38
 */
public final class ResolveControllerAnnotation {
  /**
   * 扫描所有被 {@code @framework.stereotype.RestControllerLeft} 所标注的类的, 并返回其收集的 map 视图
   *
   * @param packageName 扫描的包路径, 例如: <code>xxx.dev.controller</code>
   * @return 返回所有被 {@code @framework.stereotype.RestControllerLeft} 所标注的类的 map 映射对象, key 为注解所设置的路径
   */
  public static Map<String, Class<?>> collectAllRestControllerTaggedClass(String packageName) {
    List<String> classFullNameList = PackageClassScanner.getClassFullNameList(packageName);
    return getAllTagMatchedClassMap(
      classFullNameList,
      (aClass, annotations, classAndPathMap) -> {
        for (Annotation annotation : annotations) {
          if (annotation.annotationType() == RestControllerLeft.class) {
            Method[] declaredMethods = annotation.annotationType().getDeclaredMethods();

            for (var method : declaredMethods) {
              try {
                String routeStr = (String) method.invoke(annotation);
                // 如果顶级路由路径已经存在, 则拒绝覆盖添加
                if (classAndPathMap.get(routeStr) != null) {
                  throw new RuntimeException("The topic route path: { " + routeStr + " } always exist");
                } else {
                  classAndPathMap.put(routeStr, aClass);
                }
              } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
              }
            }
          }
        }
      });
  }
}
