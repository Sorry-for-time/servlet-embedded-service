package framework.container;

import framework.container.config.ApplicationConfig;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/9 2:33
 */
@Slf4j
public final class WebApplicationServletContainerInitializer implements Serializable, ServletContainerInitializer {
  public static final String SERVER_NAME = "SHALLING_SERVER";
  public static final String VERSION = "V0.01";
  @Serial
  private static final long serialVersionUID = 1177834535244014444L;

  @Override
  public void onStartup(Set<Class<?>> c, ServletContext ctx) {
    ApplicationConfig configuration = ConfigProvider.getConfiguration();
    String serverName = null;
    if (configuration != null) {
      if (configuration.getServer().getServerName() != null) {
        serverName = configuration.getServer().getServerName();
      }
    }
    log.info("set initial param on server startup");
    ctx.setInitParameter("server-name", serverName != null ? serverName : SERVER_NAME);
    ctx.setInitParameter("version", VERSION);
  }
}
