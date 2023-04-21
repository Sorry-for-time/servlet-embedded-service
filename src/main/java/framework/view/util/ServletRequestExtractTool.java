package framework.view.util;

import jakarta.servlet.ServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**
 * 用于提取请求头中的 json 数据
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/2/25 14:48
 */
public class ServletRequestExtractTool implements Serializable {
  @Serial
  private static final long serialVersionUID = 4853803793088731477L;

  /**
   * 获取 json 数据
   *
   * @param request servlet-request instance
   * @return the json data trim-string
   * @throws IOException IOException
   */
  public static String getRequestJSONData(ServletRequest request) throws IOException {
    BufferedReader reader = request.getReader();
    StringBuilder stringBuilder = new StringBuilder(100);
    String str;
    while ((str = reader.readLine()) != null) {
      stringBuilder.append(str.trim());
    }
    reader.close();
    return stringBuilder.toString().trim();
  }
}
