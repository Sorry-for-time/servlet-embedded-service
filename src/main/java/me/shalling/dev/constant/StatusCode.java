package me.shalling.dev.constant;

/**
 * 请求响应状态码
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 16:44
 */
public class StatusCode {
  /**
   * 请求成功
   */
  public static int OK = 201;

  /**
   * 无访问权限
   */
  public static int PERMISSION_FORBIDDEN = 301;

  /**
   * 请求失败
   */
  public static int REQUEST_FAILURE = 401;

  /**
   * 未知请求/处理错误
   */
  public static int UN_KNOW_REASON = 501;
}
