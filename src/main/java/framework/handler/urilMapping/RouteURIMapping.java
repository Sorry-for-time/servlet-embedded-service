package framework.handler.urilMapping;

import framework.handler.controllerMapping.RouteMethodRecord;
import framework.util.AnnotationMatchedHandler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于处理 Controller 下子方法子路由路径参数映射
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/19 19:02
 */
public final class RouteURIMapping {
  public static Map<String, RouteMethodRecord> resolveAllChildRoutes(
    Map<String, Class<?>> topicControllerHashMap,
    AnnotationMatchedHandler<String, Method, Map<String, RouteMethodRecord>> resolveHandler
  ) {
    final Map<String, RouteMethodRecord> methodMap = new ConcurrentHashMap<>();

    topicControllerHashMap.forEach((k, v) -> {
      Method[] methods = v.getDeclaredMethods();
      for (Method method : methods) {
        resolveHandler.doAction(k, method, methodMap);
      }
    });

    return methodMap;
  }
}
