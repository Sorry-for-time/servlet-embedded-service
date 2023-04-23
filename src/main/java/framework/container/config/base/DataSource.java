package framework.container.config.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源配置
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/18 17:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSource {
  /**
   * 驱动路径
   */
  private String driver;

  /**
   * 连接 url
   */
  private String url;

  /**
   * 用户
   */
  private String user;

  /**
   * 密码
   */
  private String password;

  /**
   * 最大连接数
   */
  private Integer max = 8;
}
