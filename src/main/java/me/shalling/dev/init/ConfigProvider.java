package me.shalling.dev.init;

import me.shalling.dev.init.config.ServerConfig;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * 应用服务配置获取类
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/9 10:25
 */
public class ConfigProvider implements Serializable {
  @Serial
  private static final long serialVersionUID = 1889616408692218007L;

  private static volatile ServerConfig serverConfig;

  private ConfigProvider() {
  }

  /**
   * 获取默认服务器配置, 这个配置只会在加载一次, 后续每次取得的结果都是幂等的
   *
   * @return 从配置文件里读取的服务器配置
   */
  public static ServerConfig getConfiguration() {
    if (serverConfig == null) {
      synchronized (ConfigProvider.class) {
        if (serverConfig == null) {
          try (
            InputStream resourceAsStream = StartApplicationServer
              .class
              .getClassLoader()
              .getResourceAsStream("application.yml")
          ) {
            Yaml yaml = new Yaml(new Constructor(ServerConfig.class, new LoaderOptions()));
            serverConfig = yaml.load(resourceAsStream);
          } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
          }
        }
      }
    }

    return serverConfig;
  }
}
