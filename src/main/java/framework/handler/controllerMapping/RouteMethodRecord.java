package framework.handler.controllerMapping;

import java.lang.reflect.Method;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/19 19:38
 */
public record RouteMethodRecord(
  String fullRouteName,
  String requestMethod,
  Method method,
  Class<?> iteSelfInvokerClass) {
}
