package framework.handler.paramsMapping;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/20 8:46
 */
public class FullUriCallingChain {
  // map(class, invoker)
  // map<uri, detail>   uri: <{method, extract params[paramClassType1, paramClassType2, ....], class invoker} >
  public final Map<Class<?>, Object> instanceMap;

  public final Map<String, InvokerDetail> uriInvokerDetailMap;

  public static class InvokerDetail {
    public final List<Class<?>> parameterTypeList;
    public final Class<?> itSelfInvokerClass;
    public final Class<?> methodReturnType;

    public InvokerDetail(List<Class<?>> classList, Class<?> itSelfInvokerClass, Class<?> methodReturnType) {
      this.parameterTypeList = classList;
      this.itSelfInvokerClass = itSelfInvokerClass;
      this.methodReturnType = methodReturnType;
    }
  }

  public FullUriCallingChain() {
    this.instanceMap = new ConcurrentHashMap<>();
    this.uriInvokerDetailMap = new ConcurrentHashMap<>();
  }
}
