package framework.test;

import framework.handler.controllerMapping.RouteMethodRecord;

import java.util.Map;

import static framework.handler.controllerMapping.ResolveControllerAnnotation.collectAllRestControllerTaggedClass;
import static framework.handler.urilMapping.ResolveMethodsURIMapping.resolveAllControllerURIAnnotatedMapping;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/19 21:38
 */
public class MainTest {
  public static void main(String[] args) {
    Map<String, RouteMethodRecord> recordMap = resolveAllControllerURIAnnotatedMapping(
      collectAllRestControllerTaggedClass("me.shalling.dev.controller")
    );

    recordMap.forEach((k, v) -> {
      System.out.println(k + " --->  " + v.requestMethodType());
    });
  }
}
