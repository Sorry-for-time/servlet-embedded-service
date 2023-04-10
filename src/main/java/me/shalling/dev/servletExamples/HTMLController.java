package me.shalling.dev.servletExamples;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/9 21:40
 */
@WebServlet(
  urlPatterns = {"/index"},
  loadOnStartup = 0,
  asyncSupported = true
)
public class HTMLController extends HttpServlet {
  @Serial
  private static final long serialVersionUID = 3780201951703022752L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    resp.setContentType("text/html");
    writer.write("<h1>hello world</h1>");
  }
}
