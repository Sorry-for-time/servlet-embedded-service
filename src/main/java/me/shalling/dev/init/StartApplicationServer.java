package me.shalling.dev.init;

import lombok.extern.slf4j.Slf4j;
import me.shalling.dev.init.config.ServerCommonDetail;
import me.shalling.dev.init.config.ServerConfig;
import me.shalling.dev.init.egg.BannerOutput;
import me.shalling.dev.init.egg.ConsoleColors;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/8 21:08
 */
@Slf4j
public final class StartApplicationServer implements Serializable {
  @Serial
  private static final long serialVersionUID = -6432398288697156075L;
  public static final int DEFAULT_PORT = 9012;
  public static final String DEFAULT_HOSTNAME = "127.0.0.1";

  public static void start() {
    final var tomcatContainer = new Tomcat();
    // 获取用户配置
    ServerConfig configurationSingleton = ConfigProvider.getConfiguration();
    ServerCommonDetail server = configurationSingleton.getServer();
    BannerOutput.displayBanner(configurationSingleton);
    System.out.println(ConsoleColors.TEXT_BRIGHT_YELLOW + configurationSingleton + ConsoleColors.TEXT_RESET);

    if (server != null) {
      if (server.getPort() != null) {
        tomcatContainer.setPort(server.getPort());
      } else {
        tomcatContainer.setPort(DEFAULT_PORT);
      }
      if (server.getHostName() != null) {
        tomcatContainer.setHostname(server.getHostName());
      } else {
        tomcatContainer.setHostname(DEFAULT_HOSTNAME);
      }
    } else {
      tomcatContainer.setPort(DEFAULT_PORT);
      tomcatContainer.setHostname(DEFAULT_HOSTNAME);
    }

    // 获取嵌入式 Tomcat 使用的默认 HTTP 连接器, 它是服务中第一个配置的连接器, 9.0+ 版本需要显示获取1
    Connector connector = tomcatContainer.getConnector();
    connector.setSecure(true);
    Context context = tomcatContainer.addWebapp("", new File("./").getAbsolutePath());
    // 配置上下文, 路径等信息
    setupResource((StandardContext) context);

    System.out.println(ConsoleColors.TEXT_BRIGHT_CYAN + "server bind port: " + connector.getPort() + ConsoleColors.TEXT_RESET);
    System.out.println(ConsoleColors.TEXT_BRIGHT_CYAN + "server scheme: " + connector.getScheme() + ConsoleColors.TEXT_RESET);
    System.out.println(ConsoleColors.TEXT_BRIGHT_CYAN + "server objectName: " + connector.getObjectName() + ConsoleColors.TEXT_RESET);
    System.out.println(ConsoleColors.TEXT_BRIGHT_CYAN + "server executorName: " + connector.getExecutorName() + ConsoleColors.TEXT_RESET);
    System.out.println(ConsoleColors.TEXT_BRIGHT_CYAN + "server protocol: " + connector.getProtocol() + ConsoleColors.TEXT_RESET);

    // 启动服务器
    try {
      tomcatContainer.start();
    } catch (LifecycleException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    tomcatContainer
      .getServer()
      .await();
  }

  private static void setupResource(StandardContext context) {
    String workHome = System.getProperty("user.dir");
    System.out.println(ConsoleColors.TEXT_BRIGHT_PURPLE + "WORK HOME: " + workHome + ConsoleColors.TEXT_RESET);
    File classedDir = new File(workHome, "target/classes");
    File jarDir = new File(workHome, "");
    WebResourceRoot resourceRoot = new StandardRoot(context);

    if (classedDir.exists()) {
      resourceRoot
        .addPreResources(
          new DirResourceSet(
            resourceRoot,
            "/WEB-INF/classes",
            classedDir.getAbsolutePath(),
            "/"
          )
        );
      log.info("Resources added: [classes]");
    } else if (jarDir.exists()) {
      resourceRoot
        .addJarResources(
          new DirResourceSet(
            resourceRoot,
            "/WEB-INF/lib",
            classedDir.getAbsolutePath(),
            "/"
          )
        );
      log.info("Resources added: [jars]");
    } else {
      resourceRoot
        .addJarResources(
          new EmptyResourceSet(resourceRoot)
        );
      log.info("Resources added: [empty]");
    }

    context.setResources(resourceRoot);
  }
}
