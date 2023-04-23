package framework.container.config.base;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/9 10:51
 */
@Data
public class Server implements Serializable {
  @Serial
  private static final long serialVersionUID = 6660141947986396627L;

  /**
   * 服务器启动端口
   */
  private Integer port;

  /**
   * 服务器 host
   */
  private String hostName;

  /**
   * 服务器名称
   */
  private String serverName;

  private String banner = "banner.txt";
}
