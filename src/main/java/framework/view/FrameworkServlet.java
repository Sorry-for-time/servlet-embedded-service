package framework.view;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

/**
 * 视图处理模板定义
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/18 8:29
 */
public abstract class FrameworkServlet extends HttpServlet {
  @Serial
  private static final long serialVersionUID = -1614536645863122417L;

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    boolean legalRequestMethod = this.isLegalRequest(req);
    if (legalRequestMethod) {
      this.viewResolver(req, resp);
    } else {
      this.failureHandler(req, resp);
    }
  }

  /**
   // * 请求是否请求
   *
   * @param request http 请求头信息
   * @return 访问资源有效性
   */
  public abstract boolean isLegalRequest(HttpServletRequest request);

  /**
   * 视图解析处理
   *
   * @param request  httpRequest 请求信息
   * @param response 响应信息
   */
  public abstract void viewResolver(HttpServletRequest request, HttpServletResponse response);

  /**
   * 资源未匹配处理信息
   *
   * @param request  http 请求信息
   * @param response http 响应信息
   * @throws IOException IO 异常
   */
  public abstract void failureHandler(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
