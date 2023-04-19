package framework.handler.urilMapping;

import framework.handler.controllerMapping.RouteMethodRecord;
import framework.stereotype.methodMapping.GetMappingLeft;
import framework.stereotype.methodMapping.PostMappingLeft;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static framework.handler.urilMapping.RouteURIMapping.resolveAllChildRoutes;

/**
 * 匹配被 RestControllerLeft 标注的 Controller 下所有被 GET 和 POST 请求注解标记的方法
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/19 20:14
 */
public class ResolveMethodsURIMapping {
  /**
   * @param allRestControllerTaggedClass 被解析过的 controller 的顶级路径和对应 class 映射的 map 视图映射集合
   * @return 所有 post, get 方法的处理视图, 以及方法所包含的元信息
   */
  public static Map<String, RouteMethodRecord> resolveAllControllerURIAnnotatedMapping(Map<String, Class<?>> allRestControllerTaggedClass) {
    return resolveAllChildRoutes(
      allRestControllerTaggedClass,
      (rootPath, controllerMethod, methodRecordMap) -> {
        // 获取方法上所有的注解
        Annotation[] annotations = controllerMethod.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
          // 只处理 get 装饰器和 post 装饰器类型
          if (annotation.annotationType() == PostMappingLeft.class || annotation.annotationType() == GetMappingLeft.class) {
            Method[] annotationMethods = annotation.annotationType().getDeclaredMethods();

            // 获取注解上的路径值, 拼接完整路径
            for (Method annotationMethod : annotationMethods) {
              String childRoutePath;
              try {
                childRoutePath = (String) annotationMethod.invoke(annotation);
              } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
              }
              String fullPath = rootPath + childRoutePath;

              // 如果路由路径已经存在方法, 那么拒绝覆盖添加, 直接抛出异常
              if (methodRecordMap.get(fullPath) != null) {
                throw new RuntimeException("the route path: { " + fullPath + " } always exist, don't allow the same request path in a method");
              } else {
                Class<? extends Annotation> aClass = annotation.annotationType();
                // 获取轻轻方式
                String requestWay;
                if (aClass == (PostMappingLeft.class)) {
                  requestWay = "POST";
                } else {
                  requestWay = "GET";
                }

                // 把完整路由映射和它对应的方法聚合信息保存到 map
                methodRecordMap
                  .put(fullPath, new RouteMethodRecord(fullPath, requestWay, annotationMethod, annotationMethod.getClass())
                  );
              }
            }
          }
        }
      }
    );
  }
}
