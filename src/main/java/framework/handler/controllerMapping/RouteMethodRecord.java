package framework.handler.controllerMapping;

import java.lang.reflect.Method;

/**
 * 资源模块标记信息
 *
 * @param fullRouteName       路由全路径
 * @param requestMethodType   请求方法类型
 * @param uriRelativeMethod   请求资源路径所关联的方法
 * @param iteSelfInvokerClass 方法所在的类
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/19 19:38
 */
public record RouteMethodRecord(
  String fullRouteName,
  String requestMethodType,
  Method uriRelativeMethod,
  Class<?> iteSelfInvokerClass) {
}
