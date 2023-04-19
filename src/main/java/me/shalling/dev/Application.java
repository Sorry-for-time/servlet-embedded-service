package me.shalling.dev;

import framework.container.init.ApplicationLeftEnterPoint;
import framework.stereotype.ApplicationLeft;
import lombok.extern.slf4j.Slf4j;

/**
 * a simple tomcat embedded service entry
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/8 17:26
 */
@Slf4j
@ApplicationLeft
public class BootstrapApplication {
  public static void main(String[] args) {
    ApplicationLeftEnterPoint.start(args);
  }
}
