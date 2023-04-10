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
 * get user custom server config
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
   * get user custom properties from file, just load once, more operation is idempotent(singleton instance)
   *
   * @return load server config from `application.yml`
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
