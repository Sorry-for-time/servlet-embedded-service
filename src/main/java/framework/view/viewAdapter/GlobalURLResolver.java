package framework.view.viewAdapter;

import framework.handler.controllerMapping.ResolveControllerAnnotation;
import framework.handler.controllerMapping.RouteMethodRecord;
import framework.handler.paramsMapping.FullUriCallingChain;
import framework.handler.paramsMapping.MethodParamsAdapter;
import framework.handler.urilMapping.ResolveMethodsURIMapping;
import framework.util.GsonSerializableTool;
import framework.view.FrameworkServlet;
import framework.view.util.ServletRequestExtractTool;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/20 0:05
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class GlobalURLResolver extends FrameworkServlet {
  @Serial
  private static final long serialVersionUID = 935062138257489247L;
  public static final String ERROR_HTML_TEMPLATE = """
    <!DOCTYPE html>
    <html lang="zh">
      <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Failure</title>
        <style>
          html,
          body {
            margin: 0;
            padding: 0;
            background-color: hsl(0, 0%, 9%);
          }
          h1 {
            color: white;
          }
        </style>
      </head>
      <body>
        <h1>WE ARE TOO SORRY, BUT CURRENT REQUEST: ${reason} DOES NOT SUPPORT</h1>
      </body>
    </html>
    """;
  private final Map<String, RouteMethodRecord> uriMethodsMapping;

  private final Set<String> supportedMethods;

  private final FullUriCallingChain uriCallingChain;

  public GlobalURLResolver() {
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
      writer.write(ERROR_HTML_TEMPLATE.replace("${reason}", request.getRequestURI()));
    }
  }
}
