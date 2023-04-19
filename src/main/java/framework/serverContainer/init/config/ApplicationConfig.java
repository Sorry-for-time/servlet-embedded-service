package framework.serverContainer.init.config;

import framework.serverContainer.init.config.base.DataSource;
import framework.serverContainer.init.config.base.Server;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 服务配置信息
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/9 10:51
 */
@Data
public class ApplicationConfig implements Serializable {
  @Serial
  private static final long serialVersionUID = -683637772313299295L;

  private Server server;

  private DataSource dataSource;
}
