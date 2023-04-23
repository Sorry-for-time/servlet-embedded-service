package me.shalling.dev.interceptor;

import framework.container.config.base.TokenStorage;
import framework.util.GsonSerializableTool;
import jakarta.servlet.FilterChain;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.shalling.dev.constant.StatusCode;
import me.shalling.dev.interceptor.meta.TokenMeta;
import me.shalling.dev.util.source.ThreadLocalDataSource;
import me.shalling.dev.vo.Result;

import java.io.PrintWriter;
import java.io.Serial;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 拦截所有的后台操作请求, 在确认附带的 token 信息里存在合法行校验后再进行放行
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 16:04
 */
@WebFilter(urlPatterns = {"/admin/*"})
public class AdminRequestInterceptor extends HttpFilter {
  @Serial
  private static final long serialVersionUID = -2133082575529882131L;
  public static final String JSON_TYPE = "application/json";

  /**
   * token 请求头标识
   */
  public static final String HEADER_TOKEN = "auth-token";

  private final Map<String, TokenMeta> TOKEN_MAP = TokenStorage.getTokenMapSingleton();

  @Override
  protected void doFilter(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain
  ) throws UnsupportedEncodingException {
    request.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    // 获取请求头信息
    String receivedToken = request.getHeader(HEADER_TOKEN);
    System.out.println(receivedToken);

    // 是否允许本次访问
    boolean requestAccessible;
    if (receivedToken == null) {
      requestAccessible = false;
    } else {
      TokenMeta tokenMeta = this.TOKEN_MAP.get(receivedToken);
      if (tokenMeta == null) {
        requestAccessible = false;
      } else {
        // 判断 token 时间是否有效
        long currentTime = System.currentTimeMillis();
        requestAccessible = tokenMeta.getCratedTime() + tokenMeta.getExpiredTime() > currentTime;

        // 移除过期 token
        if (!requestAccessible) {
          TOKEN_MAP.remove(tokenMeta.getKey());
        }
      }
    }

    try {
      // 如果 token 有效, 放行, 并为当前处理线程分配数据库连接资源
      if (requestAccessible) {
        try {
          // 为当前线程分配连接
          ThreadLocalDataSource.getConnection();
          chain.doFilter(request, response);
        } catch (Exception e) {
          // 假设实际上没问题...
          e.printStackTrace();
        } finally {
          // 回收连接
          ThreadLocalDataSource.close();
        }
      }
      // 如果不存在 token 或者已经过期, 提示客户端被禁止原因(粗暴处理, 不做更多信息设置)
      else {
        try (
          PrintWriter writer = response.getWriter()
        ) {
          response.setContentType(JSON_TYPE);
          Result<String> responseHandler = new Result<String>()
            .setStatusCode(StatusCode.PERMISSION_FORBIDDEN)
            .setData("请登录后再进行操作")
            .setMsg("未登录, 或登录已过期, 请重新登录");
          writer.write(GsonSerializableTool.objectToJSON(responseHandler));
        }
      }
    } catch (Exception e) {
      // 懒得处理了, 直接控制台打印
      e.printStackTrace();
    }
  }
}
