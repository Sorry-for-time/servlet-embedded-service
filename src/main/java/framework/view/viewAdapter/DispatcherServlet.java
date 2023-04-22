package framework.view.viewAdapter;

import framework.handler.controllerMapping.ResolveControllerAnnotation;
import framework.handler.controllerMapping.RouteMethodRecord;
import framework.handler.paramsMapping.FullUriCallingChain;
import framework.handler.paramsMapping.MethodParamsAdapter;
import framework.handler.urilMapping.ResolveMethodsURIMapping;
import framework.util.GsonSerializableTool;
import framework.view.FrameworkServlet;
import framework.view.util.ServletRequestExtractTool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DispatcherServlet extends FrameworkServlet {
  @Serial
  private static final long serialVersionUID = 935062138257489247L;
  public static final String NOTFOUND_RESOURCE_TEMPLATE;

  static {
    try (
      InputStream stream = DispatcherServlet.class.getClassLoader().getResourceAsStream("default-presets/NotFound.html")
    ) {
      if (stream != null) {
        NOTFOUND_RESOURCE_TEMPLATE = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
      } else {
        throw new RuntimeException("the temple file not found!");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private final Map<String, RouteMethodRecord> uriMethodsMapping;

  private final Set<String> supportedMethods;

  private final FullUriCallingChain uriCallingChain;

  public DispatcherServlet() {
    super();
    this.supportedMethods = new HashSet<>();
    this.supportedMethods.add("GET");
    this.supportedMethods.add("POST");
    for (String supportedMethod : supportedMethods) {
      this.supportedMethods.add(supportedMethod.toUpperCase());
    }

    Map<String, Class<?>> topiControllerMapping = ResolveControllerAnnotation
      .collectAllRestControllerTaggedClass("me.shalling.dev.controller");
    this.uriMethodsMapping = ResolveMethodsURIMapping
      .resolveAllControllerURIAnnotatedMapping(topiControllerMapping);
    this.uriCallingChain = MethodParamsAdapter.resolveAllParamsTypeAndAssociation(this.uriMethodsMapping);
  }

  @Override
  public boolean isLegalRequest(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    RouteMethodRecord routeMethodRecord = this.uriMethodsMapping.get(requestURI);
    if (routeMethodRecord == null) {
      return false;
    }
    String requestMethod = request.getMethod();
    if (!routeMethodRecord.requestMethodType().equals(requestMethod)) {
      return false;
    }
    for (String method : this.supportedMethods) {
      if (method.equals(request.getMethod().toUpperCase())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void viewResolver(HttpServletRequest request, HttpServletResponse response) {
    response.setContentType("application/json");
    String requestURI = request.getRequestURI();
    FullUriCallingChain.InvokerDetail invokerDetail = this.uriCallingChain.uriInvokerDetailMap.get(requestURI);
    Class<?> methodReturnType = invokerDetail.methodReturnType;
    List<Class<?>> parameterTypeList = invokerDetail.parameterTypeList;
    Class<?> itSelfInvokerClass = invokerDetail.itSelfInvokerClass;
    RouteMethodRecord routeMethodRecord = this.uriMethodsMapping.get(requestURI);
    Object methodInvokerObject = this.uriCallingChain.instanceMap.get(itSelfInvokerClass);
    Object[] methodParams = new Object[parameterTypeList.size()];

    Object returnValue;
    try {
      if (parameterTypeList.size() > 0) {
        String requestJSONData = ServletRequestExtractTool.getRequestJSONData(request);
        System.out.println("parameterTypeList.get(0): " + parameterTypeList.get(0));
        System.out.println(requestJSONData);
        Object o = GsonSerializableTool.JSONToObject(requestJSONData, parameterTypeList.get(0));
        methodParams[0] = o;
        returnValue = routeMethodRecord.uriRelativeMethod().invoke(methodInvokerObject, methodParams);
      } else {
        returnValue = routeMethodRecord.uriRelativeMethod().invoke(methodInvokerObject);
      }
      if (methodReturnType != void.class) {
        String json = GsonSerializableTool.objectToJSON(returnValue);
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.close();
      }
    } catch (IOException | InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void failureHandler(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    try (PrintWriter writer = response.getWriter()) {
      response.setContentType("text/html");
      String html = NOTFOUND_RESOURCE_TEMPLATE
        .replace("${replace-title}", request.getRequestURI() + " not found")
        .replace("${reason}", request.getRequestURL());
      writer.write(html);
    }
  }
}
