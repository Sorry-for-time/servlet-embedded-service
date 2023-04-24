package me.shalling.dev;

import me.shalling.dev.container.ApplicationLeftEnterPoint;
import framework.stereotype.ApplicationLeft;

/**
 * a simple tomcat embedded service entry
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/8 17:26
 */
@ApplicationLeft
public class Application {
  public static void main(String[] args) {
    ApplicationLeftEnterPoint.start(args);
  }
}
