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
import me.shalling.dev.constant.StatusCode;
import me.shalling.dev.vo.Result;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DispatcherServlet extends FrameworkServlet {
  public static final String NOTFOUND_RESOURCE_TEMPLATE;
  @Serial
  private static final long serialVersionUID = 935062138257489247L;
  public static final String CONTENT_TYPE = "application/json";

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
    response.setContentType(CONTENT_TYPE);

    String requestURI = request.getRequestURI();
    FullUriCallingChain.InvokerDetail invokerDetail = this.uriCallingChain.uriInvokerDetailMap.get(requestURI);
    Class<?> methodReturnType = invokerDetail.methodReturnType;
    List<Class<?>> parameterTypeList = invokerDetail.parameterTypeList;
    Class<?> itSelfInvokerClass = invokerDetail.itSelfInvokerClass;
    RouteMethodRecord routeMethodRecord = this.uriMethodsMapping.get(requestURI);
    Object methodInvokerObject = this.uriCallingChain.instanceMap.get(itSelfInvokerClass);
    Object[] methodParams = new Object[parameterTypeList.size()];

    Object returnValue;
    PrintWriter writer = null;
    try {
      writer = response.getWriter();
      if (parameterTypeList.size() > 0) {
        String requestJSONData = ServletRequestExtractTool.getRequestJSONData(request);
        System.out.println("requestJSONData: " + requestJSONData);
        Object o = GsonSerializableTool.JSONToObject(requestJSONData, parameterTypeList.get(0));
        methodParams[0] = o;
        System.out.println("provider params" + Arrays.toString(methodParams));
        System.out.println("require parameters: " + Arrays.toString(routeMethodRecord.uriRelativeMethod().getParameters()));
        returnValue = routeMethodRecord.uriRelativeMethod().invoke(methodInvokerObject, methodParams);
      } else {
        returnValue = routeMethodRecord.uriRelativeMethod().invoke(methodInvokerObject);
      }
      if (methodReturnType != void.class) {
        String json = GsonSerializableTool.objectToJSON(returnValue);
        writer.write(json);
        writer.close();
      }
    } catch (IOException | InvocationTargetException | IllegalAccessException e) {
      Result<String> errorResponse = new Result<String>()
        .setData(e.getCause().getMessage())
        .setMsg("请求错误")
        .setStatusCode(StatusCode.UN_KNOW_REASON);

      if (writer != null) {
        writer.write(GsonSerializableTool.objectToJSON(errorResponse));
      }
      e.printStackTrace();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  @Override
  public void failureHandler(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    try (PrintWriter writer = response.getWriter()) {
      response.setContentType("text/html");
      String htmlTemplate = NOTFOUND_RESOURCE_TEMPLATE
        .replace("${replace-title}", request.getRequestURI() + " not found")
        .replace("${reason}", request.getRequestURL());
      writer.write(htmlTemplate);
    }
  }
}
