package framework.dispatcher;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/18 8:29
 */
public class FrameworkServlet extends HttpServlet {
  public List<String> urlPatterns = new ArrayList<>();

  @Serial
  private static final long serialVersionUID = -1614536645863122417L;

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    this.urlPatterns.add(req.getRequestURI());
  }
}
