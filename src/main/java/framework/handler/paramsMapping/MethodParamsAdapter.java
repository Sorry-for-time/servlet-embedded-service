package framework.annotationHandler.paramsMapping;

import framework.annotationHandler.controllerMapping.RouteMethodRecord;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/19 21:46
 */
public class MethodParamsAdapter {
  public static FullUriCallingChain resolveAllParamsTypeAndAssociation(Map<String, RouteMethodRecord> stringRouteMethodRecordMap) {
    FullUriCallingChain fullUriCallingChain = new FullUriCallingChain();

    stringRouteMethodRecordMap.forEach((k, v) -> {
      // 获取方法上所有参数
      List<Class<?>> parameterClassList = new ArrayList<>();
      Parameter[] parameters = v.uriRelativeMethod().getParameters();
      for (Parameter parameter : parameters) {
        parameterClassList.add(parameter.getType());
      }
      if (fullUriCallingChain.instanceMap.get(v.iteSelfInvokerClass()) == null) {
        try {
          Constructor<?> constructor = v.iteSelfInvokerClass().getConstructor();
          fullUriCallingChain.instanceMap.put(v.iteSelfInvokerClass(), constructor.newInstance());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }

      fullUriCallingChain
        .uriInvokerDetailMap
        .put(
          k,
          new FullUriCallingChain
            .InvokerDetail(
            parameterClassList,
            v.iteSelfInvokerClass(),
            v.uriRelativeMethod().getReturnType())
        );
    });

    return fullUriCallingChain;
  }
}
