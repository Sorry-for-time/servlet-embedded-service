package me.shalling.dev.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.nio.charset.StandardCharsets;

/**
 * global request/response character encoding process
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/8 21:41
 */
@WebFilter(
  urlPatterns = {"/*"},
  asyncSupported = true,
  description = "global request/response character encoding process",
  filterName = "GLOBAL_CHARSET_ENCODING_FILTER"
)
public class GlobalEncodingInterceptor extends HttpFilter {
  @Serial
  private static final long serialVersionUID = -7034346804707528498L;

  @Override
  protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    request.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    chain.doFilter(request, response);
  }
}
