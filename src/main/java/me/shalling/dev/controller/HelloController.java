package me.shalling.dev.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.shalling.dev.framework.util.JSONSerializableTool;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;

/**
 * 测试接口
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/8 21:36
 */
@WebServlet(urlPatterns = {"/hello"}, loadOnStartup = 0)
public class HelloController extends HttpServlet {
  @Serial
  private static final long serialVersionUID = 8635175282541188111L;

  /**
   * 一个简单测试类
   *
   * @param habitat     习性
   * @param maxLifeTime 最长生命日期
   * @param existsTime  已经存在时间
   */
  private record Creature(
    String habitat,
    Integer maxLifeTime,
    Integer existsTime) {
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    ServletContext servletContext = this.getServletContext();
    Enumeration<String> attributeNames = servletContext.getAttributeNames();
    System.out.println("-".repeat(30));
    while (attributeNames.hasMoreElements()) {
      System.out.println(attributeNames.nextElement());
    }

    System.out.println("-".repeat(30));
    Enumeration<String> headerNames = req.getHeaderNames();
    headerNames.asIterator().forEachRemaining(h -> System.out.println(h + ": " + req.getHeader(h)));

    System.out.println("~".repeat(30));
    Enumeration<String> initParameterNames = this.getServletContext().getInitParameterNames();
    while (initParameterNames.hasMoreElements()) {
      String s = initParameterNames.nextElement();
      System.out.println(s + ": " + this.getServletContext().getInitParameter(s));
    }
    System.out.println("*".repeat(30));
    System.out.println("server-name: " + req.getServletContext().getInitParameter("server-name"));
    System.out.println("version: " + req.getServletContext().getInitParameter("version"));

    PrintWriter writer = resp.getWriter();
    resp.setContentType("application/json");
    Random random = new Random();
    Creature[] creatures = new Creature[4];
    for (int i = 0; i < creatures.length; i++) {
      creatures[i] = new Creature(
        UUID.randomUUID() + "--> || ===", random.nextInt(), random.nextInt());
    }
    writer.write(JSONSerializableTool.toJson(creatures));
    writer.flush();
    writer.close();
  }
}
