package framework.serverContainer.init.config.base;

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
  private String driver;

  private String url;

  private String user;

  private String password;
}
