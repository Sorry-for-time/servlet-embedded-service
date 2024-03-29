package me.shalling.dev.container.egg;

import me.shalling.dev.container.config.ApplicationConfig;
import me.shalling.dev.container.config.base.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/9 12:40
 */
public class BannerOutput implements Serializable {

  @Serial
  private static final long serialVersionUID = -5495196089687622745L;

  private BannerOutput() {
  }

  /**
   * 启动项 banner 输出
   *
   * @param configuration 用户配置
   */
  public static void displayBanner(ApplicationConfig configuration) {
    Server server = configuration.getServer();
    if (server != null) {
      String banner = server.getBanner();
      if (banner != null && !"".equals(banner.trim())) {
        try (
          InputStream resourceAsStream = BannerOutput.class.getClassLoader().getResourceAsStream(banner)
        ) {
          byte[] bytes = Objects.requireNonNull(resourceAsStream).readAllBytes();
          String s = new String(bytes);
          System.out.println(ConsoleColors.TEXT_BRIGHT_GREEN + s + ConsoleColors.TEXT_RESET);
        } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }
    }
  }
}
