package me.shalling.dev.init;

import lombok.extern.slf4j.Slf4j;
import me.shalling.dev.init.config.ServerCommonDetail;
import me.shalling.dev.init.config.ServerConfig;
import me.shalling.dev.init.egg.BannerOutput;
import me.shalling.dev.init.egg.ConsoleColors;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.coyote.http2.Http2Protocol;
import org.apache.tomcat.util.net.SSLHostConfig;

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
  public static final String WORK_HOME = System.getProperty("user.dir");
  @Serial
  private static final long serialVersionUID = -6432398288697156075L;
  public static final int DEFAULT_PORT = 8080;
  public static final String DEFAULT_HOSTNAME = "localhost";
  public static final String STORE_DIR = System.getProperty("user.dir") + "/target";
  public static final String WEBAPP_PATHNAME = "./";

  /**
   * 启动 tomcat 容器服务
   */
  public static void start() {
    /*
     * load user custom properties from application.yml file
     *  */
    ServerConfig configurationSingleton = ConfigProvider.getConfiguration();
    ServerCommonDetail serverConfigDetail = configurationSingleton.getServer();
    BannerOutput.displayBanner(configurationSingleton);
    System.out.println(ConsoleColors.TEXT_BRIGHT_YELLOW + configurationSingleton + ConsoleColors.TEXT_RESET);

    final var tomcatInstance = new Tomcat();
    tomcatInstance.setBaseDir(STORE_DIR);
    tomcatInstance.setAddDefaultWebXmlToWebapp(false);

    // setup properties if exists
    if (serverConfigDetail != null) {
      if (serverConfigDetail.getPort() != null) {
        tomcatInstance.setPort(serverConfigDetail.getPort());
      } else {
        tomcatInstance.setPort(DEFAULT_PORT);
      }
      if (serverConfigDetail.getHostName() != null) {
        tomcatInstance.setHostname(serverConfigDetail.getHostName());
      } else {
        tomcatInstance.setHostname(DEFAULT_HOSTNAME);
      }
    } else {
      tomcatInstance.setPort(DEFAULT_PORT);
      tomcatInstance.setHostname(DEFAULT_HOSTNAME);
    }

    StandardContext context = (StandardContext) tomcatInstance
      .addWebapp(
        "/",
        new File(WEBAPP_PATHNAME).getAbsolutePath()
      );
    context.setCookies(true);
    context.setSessionCookiePathUsesTrailingSlash(true);
    context.setUseHttpOnly(true);
    setupResource(context);

    Connector connector = tomcatInstance.getConnector();
    connector.setSecure(true);
    connector.addUpgradeProtocol(new Http2Protocol());
    connector.addSslHostConfig(new SSLHostConfig());
    connector.setAllowTrace(true);

    showConnectorBasicInfo(connector);

    try {
      tomcatInstance.start();
      tomcatInstance
        .getServer()
        .await();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * 对上下文对象进行初始化配置
   *
   * @param context 上限=下文对象配置
   */
  private static void setupResource(StandardContext context) {
    System.out.println(ConsoleColors.TEXT_BRIGHT_PURPLE + "WORK HOME: " + WORK_HOME + ConsoleColors.TEXT_RESET);
    File classedDir = new File(WORK_HOME, "target/classes");
    File jarDir = new File(WORK_HOME, "");
    var resourceRoot = new StandardRoot(context);
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

  private static void showConnectorBasicInfo(Connector connector) {
    System.out.println(ConsoleColors.TEXT_BRIGHT_CYAN + "server bind port: " + connector.getPort() + ConsoleColors.TEXT_RESET);
    System.out.println(ConsoleColors.TEXT_BRIGHT_CYAN + "server scheme: " + connector.getScheme() + ConsoleColors.TEXT_RESET);
    System.out.println(ConsoleColors.TEXT_BRIGHT_CYAN + "server objectName: " + connector.getObjectName() + ConsoleColors.TEXT_RESET);
    System.out.println(ConsoleColors.TEXT_BRIGHT_CYAN + "server executorName: " + connector.getExecutorName() + ConsoleColors.TEXT_RESET);
    System.out.println(ConsoleColors.TEXT_BRIGHT_CYAN + "server protocol: " + connector.getProtocol() + ConsoleColors.TEXT_RESET);
  }
}
